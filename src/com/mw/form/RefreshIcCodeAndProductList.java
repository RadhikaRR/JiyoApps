package com.mw.form;

import java.util.Hashtable;
import java.util.TimerTask;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.mw.agency.screens.AuthenticationResponsePopUpScreen;
import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class RefreshIcCodeAndProductList extends TimerTask {

	public static RefreshIcCodeAndProductList INSTANCE = new RefreshIcCodeAndProductList();

	public void run() {
		String IMEI = NewAuthenticator.INSTANCE.getIMEINumber();

		if (IMEI != null) {
			boolean flag = true;
			flag = CallService.INSTANCE.getICCodeList(IMEI, true);
			while(flag == false){
				flag = CallService.INSTANCE.getICCodeList(IMEI, true);
			}
			CallService.INSTANCE.getCQList(IMEI, true);
		}
	}

	public void manageObject(Object object) {

		if (object != null) {

			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);
			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable
						.get("stringval10");

				if (checkForSuccessResponse.equals("SUCCESS")) {
					convertHashTableToArray(hashtable);
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
													"Failed to Refresh Product and IC Code list for Visit info, Check for Success Response: "
															+ checkForSuccessResponse));
								}
							});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(
					new Runnable() {
						public void run() {
							Status
									.show("Failed to update IC Codes and Product List for Visit info. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",3000);
						}
					});
		}
	}

	private void convertHashTableToArray(Hashtable hashtable) {

		String icCodesString = (String) hashtable.get("stringval1");
		String productsString = (String) hashtable.get("stringval2");

	
		String[] icCodesArray = null;
		if (productsString != null) {

			if (icCodesString != null) {
				icCodesArray = StringSplitter.INSTANCE
						.split(icCodesString, "~");
			}

			String[] productsArray = StringSplitter.INSTANCE.split(
					productsString, "~");

			if (productsArray != null) {
				Constant.oldProductArray = productsArray;
			}
			if (icCodesArray != null) {
				Constant.oldICCodeArray = icCodesArray;
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					Dialog.alert("Failed to Refresh Product List for Visit info, Prerequisite configuration not available on server..");
					
				}
			});			
		}
	}
}
