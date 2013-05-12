package com.mw.control;

import java.util.Hashtable;

import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.blackberry.api.messagelist.ApplicationIndicator;
import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.mw.agency.screens.AgencyDateAndZoneScreen;
import com.mw.agency.screens.AuthenticationResponsePopUpScreen;
import com.mw.appupdates.AppUpdater;
import com.mw.constants.Constant;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.uiscreens.EntryScreen2;
import com.mw.utils.ShowDialog;
import com.mw.utils.StringSplitter;
import com.mw.utils.StringTokenizer;
import com.mw.webservice.CallService;

public class NewAuthenticator {

	public static NewAuthenticator INSTANCE = new NewAuthenticator();
	private String IMEI;

	public void authenticate(String message) {

		IMEI = getIMEINumber();
		if (IMEI != null) {
			Controller.showScreen(new PopupSpinnerScreen(message));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;
					flag = CallService.INSTANCE.authenticateViaWebService(IMEI);
					while (flag == false) {
						flag = CallService.INSTANCE.authenticateViaWebService(IMEI);
					}
				}
			};
			thread.start();
		}
	}

	public String getIMEINumber() {

		if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_CDMA) {
			try {
				IMEI = Integer.toString(CDMAInfo.getESN());
			} catch (Exception e) {
				synchronized (UiApplication.getEventLock()) {
					Dialog.alert("Failed to retrive IMEI for Authentication:" + e.getMessage());
				}
			}
		} else {
			try {
				IMEI = GPRSInfo.imeiToString(GPRSInfo.getIMEI(), false);
			} catch (Exception e) {
				synchronized (UiApplication.getEventLock()) {
					Dialog.alert("Failed to retrive IMEI for Authentication:" + e.getMessage());
				}
			}
		}
		return IMEI;
	}

	public void handleAuthenticationObject(Object object) {

		if (object != null) {
//			String response = object.toString();
			 String response =
			 "authentication object:com_bajajallianz_BagicWapImpl_verifyUserDtls_Out{pwapuserobjOut=com_bajajallianz_WeoRecStrings40User{stringval26=null; stringval15=null; stringval18=null; stringval5=South 2; stringval34=null; stringval10=USER-IS-ENABLED; stringval12=null; stringval27=null; stringval32=null; stringval37=null; stringval6=1100084420; stringval40=1.09; stringval36=null; stringval13=null; stringval30=null; stringval3=~20:14:36 Mark your attendance from mobile; stringval14=null; stringval25=null; stringval28=null; stringval35=null; stringval22=null; stringval29=null; stringval31=null; stringval1=Rahul S. Saksule; stringval38=null; stringval21=null; stringval23=null; stringval19=null; stringval2=~1~2~3~4~5~6~7~8~9; stringval20=null; stringval7=null; stringval16=null; stringval9=null; stringval33=null; stringval24=null; stringval8=null; stringval39=http://general.bajajallianz.com/BagicHCM/MobileApps; stringval17=null; stringval4=BM; stringval11=null; };}";

			Hashtable hashtable = converObjectToHashTable(response);
			Constant.oldNewsHashTable = hashtable;

			if (hashtable != null) {
				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.trim().equalsIgnoreCase("user-is-enabled")) {
					// chk for updates

					String newVersion = (String) hashtable.get("stringval40");
					boolean downloadFlag = false;
					if (newVersion != null && !newVersion.equalsIgnoreCase("null")) {
						downloadFlag = AppUpdater.INSTANCE.doCheckForUpdates(newVersion);
					}

					if (downloadFlag) {
						String downloadPath = (String) hashtable.get("stringval39");
						// get the full download path
						if (downloadPath != null && !downloadPath.equalsIgnoreCase("null")) {
							String fullDownloadpath = downloadPath + Constant.newAppDownloadAppendURL;
							// AppUpdater.INSTANCE.showDownloadDialog(newVersion);
							AppUpdater.INSTANCE.openDownloadURL(fullDownloadpath);
						}
					} else {
						// register Indicator Icon
						EncodedImage mImageGreen = EncodedImage.getEncodedImageResource("AppIndi.PNG");
						ApplicationIcon mIconGreen = new ApplicationIcon(mImageGreen);
						Constant.mIcon = mIconGreen;
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								registerIndicator();
							}
						});

						// first check module visible

						String modulesVisible = (String) hashtable.get("stringval2");

						if (modulesVisible != null) {

							String[] moduleArray = StringSplitter.INSTANCE.split(modulesVisible, "~");
							for (int i = 0; i < moduleArray.length; i++) {
								if (moduleArray[i].equals("1")) {
									Constant.module1 = true;
								} else if (moduleArray[i].equals("2")) {
									Constant.module2 = true;
								} else if (moduleArray[i].equals("3")) {
									Constant.module3 = true;
								} else if (moduleArray[i].equals("4")) {
									Constant.module4 = true;
								} else if (moduleArray[i].equals("5")) {
									Constant.module5 = true;
								} else if (moduleArray[i].equals("6")) {
									Constant.module6 = true;
								} else if (moduleArray[i].equals("7")) {
									Constant.module7 = true;
								} else if (moduleArray[i].equals("8")) {
									Constant.module8 = true;
								} else if (moduleArray[i].equals("9")) {
									Constant.module9 = true;
								}
							}
						}
						// then check Manager type
						Constant.managerName = (String) hashtable.get("stringval1");
						Constant.managerRole = (String) hashtable.get("stringval4");
						Constant.SM_CODE = (String) hashtable.get("stringval6");
						Constant.managerArea = (String) hashtable.get("stringval5");
						Constant.employeeCode = (String) hashtable.get("stringval6");

						// configure role for mis
						if (Constant.managerRole != null && !Constant.managerRole.equalsIgnoreCase("null")) {
							if (Constant.managerRole.equalsIgnoreCase("sm")) {
								Constant.isSalesManager = true;
							} else if (Constant.managerRole.equalsIgnoreCase("all")) {
								Constant.isAllManager = true;
							} else if (Constant.managerRole.equalsIgnoreCase("zm")) {
								Constant.isZonalManager = true;
							} else if (Constant.managerRole.equalsIgnoreCase("rm")) {
								Constant.isRegionalManager = true;
							} else if (Constant.managerRole.equalsIgnoreCase("dm")) {
								Constant.isDivisionalManager = true;
							} else if (Constant.managerRole.equalsIgnoreCase("bm")) {
								Constant.isBranchManager = true;
							}
						}
						Controller.showScreen(new EntryScreen2(hashtable));
					}
				} else {
					Controller.showScreen(new AuthenticationResponsePopUpScreen("Authentication failed: "
							+ authResponse + ", please contact customer support."));
				}
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Authentication failed. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	public void registerIndicator() {
		try {
			ApplicationIndicatorRegistry reg = ApplicationIndicatorRegistry.getInstance();
			ApplicationIndicator indicator = reg.register(Constant.mIcon, true, true);
			indicator.setVisible(false);
		} catch (Exception e) {
			LogEventClass.logErrorEvent(e.getMessage());
		}
	}

	public void newHandleZoneObject(Object object, final String listIdentifier) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = converObjectToHashTable(response);

			String tempchecksuccess = (String) hashtable.get("perrormessageOut");

			if (tempchecksuccess != null) {
				if (tempchecksuccess.equals("SUCCESS")) {
					StringSplitter splitter = new StringSplitter();
					String zoneString = "";
					if (zoneString != null) {
						zoneString += (String) hashtable.get("stringval1");

						// If supplied default stringval5 value is null or wrong
						// web service return null instaed of string seprated by
						// ~ so AgencyDateAndZoneScreen
						// displays Objectchoicefiled with null value to avoid
						// that if else
						if (!zoneString.equalsIgnoreCase("null")) {
							String[] zoneArray = splitter.split(zoneString, "~");

							if (zoneArray != null) {
								Controller.showScreen(new AgencyDateAndZoneScreen(zoneArray));
							}
						} else {
							ShowDialog.INSTANCE.show("Failed to Fetch " + listIdentifier + " List, as default "
									+ listIdentifier + " is invalid.");
						}
					}
				} else {
					ShowDialog.INSTANCE.show("Pre requisite " + listIdentifier
							+ " configurations are not available on the server.");
				}
			} else {
				ShowDialog.INSTANCE.show("Fail to Fetch " + listIdentifier + " List. Check for Success Response "
						+ tempchecksuccess);
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show("Failed to Fetch "
									+ listIdentifier
									+ " List. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
				}
			});
		}
	}

	public Hashtable converObjectToHashTable(String string) {

		Hashtable hashtable = new Hashtable();

		try {
			string = string.substring(string.indexOf("{") + 1, string.length() - 1);

			string = string.substring(string.indexOf("{") + 1);

			string = string.replace('}', ' ');

			StringTokenizer tokenizer = new StringTokenizer(string, ";");

			while (tokenizer.hasMoreElements()) {

				String token = (String) tokenizer.nextElement();

				if (token.indexOf("=") != -1) {

					String key = token.substring(0, token.indexOf("="));

					String value = (token.substring(token.indexOf("=") + 1)).trim();

					if (!(value.equals("null") || value.equals(""))) {

						hashtable.put(key.trim(), value.trim());
					}
				}
			}
		} catch (Exception e) {
			ShowDialog.INSTANCE.show("Fail to parsed object in converObjectToHashTable" + e.toString());
		}
		return hashtable;
	}
}
