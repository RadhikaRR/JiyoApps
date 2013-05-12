package com.mw.form;

import java.util.Hashtable;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.mw.agency.screens.AuthenticationResponsePopUpScreen;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class GetVisitInfoICCodes {

	private String IMEI;

	public static GetVisitInfoICCodes INSTANCE = new GetVisitInfoICCodes();

	public void callgetICCodeWebService() {

		IMEI = NewAuthenticator.INSTANCE.getIMEINumber();
		if (IMEI != null) {
			Controller.showScreen(new PopupSpinnerScreen(
					"Retrieving Visit Details.."));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;					
					flag = CallService.INSTANCE.getICCodeList(IMEI, false);
					while(flag == false){
						flag = CallService.INSTANCE.getICCodeList(IMEI, false);
					}
				}
			};
			thread.start();
		}
	}

	public void convertObjectToHash(Object object) {
		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);

			if (hashtable != null) {

				final String checkForSuccessResponse = (String) hashtable
						.get("stringval10");

				if (checkForSuccessResponse.equals("SUCCESS")) {
					convertHashToArray(hashtable);

				} else {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {

									Screen activeScreen = UiApplication
											.getUiApplication()
											.getActiveScreen();
									UiApplication.getUiApplication().popScreen(
											activeScreen);

									Controller
											.showScreen(new AuthenticationResponsePopUpScreen(
													"Fail to fetch Product and IC Code list, Check for Success Response: "
															+ checkForSuccessResponse));
								}
							});
				}
			}
		} else {

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Visit Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	private void convertHashToArray(Hashtable hashtable) {

		String icCodesString = (String) hashtable.get("stringval1");

		String productsString = (String) hashtable.get("stringval2");

		if (productsString != null) {
			String[] icCodesArray = null;
			if (icCodesString != null) {
				icCodesArray = StringSplitter.INSTANCE
						.split(icCodesString, "~");
			}

			String[] productsArray = StringSplitter.INSTANCE.split(
					productsString, "~");

			if (productsArray != null) {

				Controller.showScreen(new SubmitFormWithAutoCompleteListField(
						icCodesArray, productsArray));
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						Screen screen = UiApplication.getUiApplication()
								.getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen);
						Dialog
								.alert("Pre requisite Product and IC Code configurations are not available on the server.");
					}
				});
			}

		} else {

			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					Screen screen = UiApplication.getUiApplication()
							.getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Dialog
							.alert("Pre requisite Product and IC Code configurations are not available on the server.");
				}
			});
		}
	}
}
