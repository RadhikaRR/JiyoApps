package com.mw.smnew;

import java.util.Hashtable;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.mw.agency.screens.BMScoreCardScreen;
import com.mw.agency.screens.DMScoreCardScreen;
import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.utils.ShowDialog;
import com.mw.webservice.CallService;

public class NewSMTableListScreen {

	public static NewSMTableListScreen INSTANCE = new NewSMTableListScreen();

	public NewSMTableListScreen() {

	}

	public void callService() {

		if (Constant.SM_CODE != null) {
			Controller.showScreen(new PopupSpinnerScreen(
					"Retrieving ScoreCard..."));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;
					flag = CallService.INSTANCE.getAllSalesManagerTables(Constant.SM_CODE);
					while(flag == false){
						flag = CallService.INSTANCE.getAllSalesManagerTables(Constant.SM_CODE);
					}
				}
			};
			thread.start();
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog
							.alert("Pre requisite default SM Code configurations is not available on the server.");
				}
			});
		}
	}

	public void manageAllSalesManagerTablesObject(Object object) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);
			if (hashtable != null) {
				manageHashTable(hashtable);
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch SM Table. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	private void manageHashTable(Hashtable hashtable) {

		String tempchecksuccess = (String) hashtable.get("string80");

		if (tempchecksuccess != null) {
			String checkcheck = tempchecksuccess.trim().toLowerCase();

			if (checkcheck.equalsIgnoreCase("success")) {

				Controller.showScreen(new FinalNewSMScoreCardTableScreen(
						hashtable));
			} else {
				ShowDialog.INSTANCE
						.show("Sales Manager Report not available, Check Success Response :"
								+ tempchecksuccess);
			}
		} else {
			ShowDialog.INSTANCE
					.show("Sales Manager Report not available, Check Success Response :"
							+ tempchecksuccess);
		}
	}

	public void getDMScoreCardResult(Object object) {
		if (object != null) {

			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);
			if (hashtable != null) {

				final String checkForSuccessResponse = (String) hashtable
						.get("string80");
				if (checkForSuccessResponse.trim().toLowerCase()
						.equalsIgnoreCase("success")) {

					Controller.showScreen(new DMScoreCardScreen(hashtable));

				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication()
								.getActiveScreen();
						UiApplication.getUiApplication()
								.popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication()
								.getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {

								public void run() {
									Dialog.alert(checkForSuccessResponse);
								}
							});
				}
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Status
								.show(
										"Failed to fetch DM Score Card Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
										3000);
					}
				});
			}
		}
	}

	public void getBMScoreCardResult(Object object) {
		if (object != null) {

			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);
			if (hashtable != null) {

				final String checkForSuccessResponse = (String) hashtable
						.get("string80");
				if (checkForSuccessResponse.equals("SUCCESS")) {

					Controller.showScreen(new BMScoreCardScreen(hashtable));

				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication()
								.getActiveScreen();
						UiApplication.getUiApplication()
								.popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication()
								.getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {

								public void run() {
									Dialog.alert(checkForSuccessResponse);
								}
							});
				}
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Status
								.show(
										"Failed to fetch DM Score Card Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
										3000);
					}
				});
			}
		}
	}
}
