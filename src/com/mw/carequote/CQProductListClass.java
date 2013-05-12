package com.mw.carequote;

import java.util.Hashtable;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class CQProductListClass {

	private String IMEI;
	public static String[] val;
	public static String[] valMax;
	public static String[] valMaxMax;

	public static CQProductListClass INSTANCE = new CQProductListClass();

	public void getCQProductList() {

		IMEI = NewAuthenticator.INSTANCE.getIMEINumber();
		if (IMEI != null) {
			Controller.showScreen(new PopupSpinnerScreen("Retrieving Product List.."));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;
					flag = CallService.INSTANCE.getCQList(IMEI, false);
					if (flag == false) {
						flag = CallService.INSTANCE.getCQList(IMEI, false);
					}
				}
			};
			thread.start();
		}
	}

	public void getCQproduct(Object object, String gender, String product, boolean isBackroundRefresh) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable.get("stringval10");

				if (checkForSuccessResponse.trim().toLowerCase().equalsIgnoreCase("success")) {
					if (isBackroundRefresh) {
						doRefreshProductList(hashtable);
					} else {
						convertHashToArray(hashtable);
					}

				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}

			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Care Quote Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	public void getCqPremium(Object object, boolean isBackroundRefresh, String Cqname, String CqCode) {
		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable.get("stringval20");

				if (checkForSuccessResponse.equals("SUCCESS")) {
					convertHashToArrayForCqPremium(hashtable, Cqname, CqCode);

				} else if (isBackroundRefresh) {
					doRefreshProductList(hashtable);
				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Care Quote Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	private void doRefreshProductList(Hashtable hashtable) {
		String cqProductString = (String) hashtable.get("stringval1");

		if (cqProductString != null) {
			String[] cqProductArray = StringSplitter.INSTANCE.split(cqProductString, "~");
			if (cqProductArray != null) {
				Constant.oldCareQuoteProductArray = cqProductArray;
			}
		} else {

			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Dialog
							.alert("Failed to Refresh Care Quote Product List, Pre requisite Product configurations are not available on the server.");
				}
			});
		}
	}

	private void convertHashToArray(Hashtable hashtable) {

		String cqProductString = (String) hashtable.get("stringval1");

		if (cqProductString != null) {
			//String[] cqProductArray = StringSplitter.INSTANCE.split(cqProductString, "~");
			 String[] cqProductArray = { "TERM CARE - HEALTH",
			 "TERM CARE - SINGLE", "TERM CARE - TOTAL", "TERM CARE - PROTECT",
			 "TERM CARE - ECONOMY", "SUPER SAVER", "Cashrich Plan",
			 "Cashgain Platinum Plan",
			 "Cashgain Economy Plan", "Cashgain Gold Plan",
			 "Cashgain Diamond Plan", "SUPER CASHGAIN SILVER",
			 "SUPER CASHGAIN GOLD", "SUPER CASHGAIN DIAMOND",
			 "SUPER CASHGAIN PLATINUM" };

			if (cqProductArray != null) {
				Controller.showScreen(new CareQuoteDetailsScreen(cqProductArray));
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen);
						Dialog
								.alert("Pre requisite Care Quote Product configurations are not available on the server.");
					}
				});
			}

		} else {

			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Dialog.alert("Pre requisite Care Quote Product configurations are not available on the server.");
				}
			});
		}
	}

	public void getCqRider(Object object, String CqsumAssured, String CqCode) {
		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable.get("stringval10");

				if (checkForSuccessResponse.equals("SUCCESS")) {
					convertHashToArrayForCqRiders(hashtable, CqsumAssured, CqCode);

				} else {
					synchronized (UiApplication.getEventLock()) {
						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Care Quote Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	private void convertHashToArrayForCqRiders(Hashtable hashtable, String CqsumAssured, String CqCode) {

		String cqRiderString = (String) hashtable.get("stringval1");
		// String cqRiderString =
		// "~Bajaj Allianz CashRich Insurance Plan Main Benefit#100123#100025#0~Accidental Death Benefit Rider#100000#5000000#1~Term Rider#50000#100321#0~Critical Illness Rider#100000#100030#1~Waiver of premium rider#100000#1236540#0~Total / Partial Permanent Disability#100000#100050#1~Hospital Cash Rider#50000#100060#0~Family Income Benefit#100000#100070#1";
		// String cqRiderString =
		// "~Bajaj Allianz CashRich Insurance Plan Main Benefit#0#0#0~Accidental Death Benefit Rider#100000#1252000#1~Term Rider#50000#52222#0~Critical Illness Rider#100000#0#0~Waiver of premium rider#100000#0#0~Total / Partial Permanent Disability#100000#0#0~Hospital Cash Rider#50000#0#0~Family Income Benefit#100000#0#0";

		String cqRiderStringGroup = (String) hashtable.get("stringval2");

		if (cqRiderString != null) {
			String[] cqRiderProductArray = StringSplitter.INSTANCE.split(cqRiderString, "~");

			String[] stringNameArray = new String[cqRiderProductArray.length];
			val = new String[cqRiderProductArray.length];
			valMax = new String[cqRiderProductArray.length];
			valMaxMax = new String[cqRiderProductArray.length];
			int cqRiderProductArrayLength = cqRiderProductArray.length;
			for (int i = 0; i < cqRiderProductArrayLength; i++) {

				String[] cqRiderProductArrayWithValue = StringSplitter.INSTANCE.split(cqRiderProductArray[i], "#");

				stringNameArray[i] = cqRiderProductArrayWithValue[0];
				val[i] = cqRiderProductArrayWithValue[1];
				valMax[i] = cqRiderProductArrayWithValue[2];
				valMaxMax[i] = cqRiderProductArrayWithValue[3];
				System.out.println("");
			}
			System.out.println("" + stringNameArray);
			System.out.println("" + val);

			String[] cqRiderProductArrayGroup = StringSplitter.INSTANCE.split(cqRiderStringGroup, "~");

			if (cqRiderProductArray != null && stringNameArray != null) {
				Constant.riderCount = cqRiderProductArray.length;

				Controller.showScreen(new CQScreen2(CqsumAssured, stringNameArray, cqRiderProductArrayGroup, CqCode));
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen);
						Dialog.alert("Pre requisite CqRider configurations are not available on the server.");
					}
				});
			}

		} else {

			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Dialog.alert("Pre requisite CqRider configurations are not available on the server.");
				}
			});
		}
	}

	public void convertHashToArrayForCqPremium(Hashtable hashtable, String Cqname, String CqCode) {

		String cqPremiumString = (String) hashtable.get("stringval17");

		if (cqPremiumString != null || !(cqPremiumString.equals(""))) {

			String[] CqRiderPremiumList = StringSplitter.INSTANCE.split(cqPremiumString, "#M#");

			if (CqRiderPremiumList != null) {
				Controller.showScreen(new DemoScreen(hashtable, CqRiderPremiumList, Cqname, CqCode));
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen);
						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
						Dialog.alert("Pre requisite CqPremium configurations are not available on the server.");
					}
				});
			}
		} else {

			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);

					Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
					if (screen2 instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen2);
					}
					Dialog.alert("Pre requisite CqPremium configurations are not available on the server.");
				}
			});
		}
	}
}
