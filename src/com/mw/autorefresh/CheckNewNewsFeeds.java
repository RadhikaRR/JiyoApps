package com.mw.autorefresh;

import java.util.Hashtable;
import java.util.TimerTask;

import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.blackberry.api.messagelist.ApplicationIndicator;
import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.device.api.notification.NotificationsManager;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Status;

import com.mw.appupdates.AppUpdater;
import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.LogEventClass;
import com.mw.control.NewAuthenticator;
import com.mw.control.dialogForNewsFeedIndicator;
import com.mw.uiscreens.EntryScreen2;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class CheckNewNewsFeeds extends TimerTask {

	public static CheckNewNewsFeeds instance = new CheckNewNewsFeeds();
	public String latestNewsFeedsArray;

	private String IMEI;
	private Hashtable NewsNewsFeedshashtable;

	public void run() {

		IMEI = NewAuthenticator.INSTANCE.getIMEINumber();
		if (IMEI != null) {
			boolean flag = true;
			flag = CallService.INSTANCE.newsBackground(IMEI);
			while (flag == false) {
				flag = CallService.INSTANCE.newsBackground(IMEI);
			}
		}
	}

	public void handleNewsObject(Object neObject) {
		System.out.println("m in handleNewsObject");
		if (neObject != null) {
			//String response = neObject.toString();
			 String response =
			 "authentication object:com_bajajallianz_BagicWapImpl_verifyUserDtls_Out{pwapuserobjOut=com_bajajallianz_WeoRecStrings40User{stringval26=null; stringval15=null; stringval18=null; stringval5=South 2; stringval34=null; stringval10=USER-IS-ENABLED; stringval12=null; stringval27=null; stringval32=null; stringval37=null; stringval6=1100084420; stringval40=1.09; stringval36=null; stringval13=null; stringval30=null; stringval3=~20:14:36 Mark your attendance from mobile~Hi Rahul this is a new news,Please check; stringval14=null; stringval25=null; stringval28=null; stringval35=null; stringval22=null; stringval29=null; stringval31=null; stringval1=Rahul S. Saksule; stringval38=null; stringval21=null; stringval23=null; stringval19=null; stringval2=~1~2~3~4~5~6~7~8~9; stringval20=null; stringval7=null; stringval16=null; stringval9=null; stringval33=null; stringval24=null; stringval8=null; stringval39=http://general.bajajallianz.com/BagicHCM/MobileApps; stringval17=null; stringval4=BM; stringval11=null; };}";

			NewsNewsFeedshashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (NewsNewsFeedshashtable != null) {

				String authResponse = (String) NewsNewsFeedshashtable.get("stringval10");
				if (authResponse.trim().toLowerCase().equalsIgnoreCase("user-is-enabled")) {

					// chk for updates
					String newVersion = (String) NewsNewsFeedshashtable.get("stringval40");
					boolean downloadFlag = false;
					if (newVersion != null && !newVersion.equalsIgnoreCase("null")) {
						downloadFlag = AppUpdater.INSTANCE.doCheckForUpdates(newVersion);
					}

					if (downloadFlag) {
						String downloadPath = (String) NewsNewsFeedshashtable.get("stringval39");
						// get the full download path
						if (downloadPath != null && !downloadPath.equalsIgnoreCase("null")) {
							String fullDownloadpath = downloadPath + Constant.newAppDownloadAppendURL;

							AppUpdater.INSTANCE.openDownloadURL(fullDownloadpath);
						}
					} else {

						String modulesVisible = (String) NewsNewsFeedshashtable.get("stringval2");
						if (modulesVisible != null) {
							String[] moduleArray = StringSplitter.INSTANCE.split(modulesVisible, "~");

							Constant.module1 = false;
							Constant.module2 = false;
							Constant.module3 = false;
							Constant.module4 = false;
							Constant.module5 = false;
							Constant.module6 = false;
							Constant.module7 = false;
							Constant.module8 = false;
							Constant.module9 = false;

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
						Constant.managerName = (String) NewsNewsFeedshashtable.get("stringval1");
						Constant.managerRole = (String) NewsNewsFeedshashtable.get("stringval4");
						Constant.SM_CODE = (String) NewsNewsFeedshashtable.get("stringval6");
						Constant.managerArea = (String) NewsNewsFeedshashtable.get("stringval5");

						Constant.isSalesManager = false;
						Constant.isAllManager = false;
						Constant.isZonalManager = false;
						Constant.isRegionalManager = false;
						Constant.isDivisionalManager = false;
						Constant.isBranchManager = false;// true RR..if problem
															// occurs

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

						String newNews = (String) NewsNewsFeedshashtable.get("stringval3");
						if (newNews != null) {
							String[] newNewslistItemArray = StringSplitter.INSTANCE.split(newNews, "~");

							int newNewsArrayLength = newNewslistItemArray.length;

							String rr = newNewslistItemArray[newNewsArrayLength - 1];

							if (newNewsArrayLength != Constant.oldNewsLength) {
								UiApplication.getUiApplication().invokeLater(new Runnable() {
									public void run() {
										NotificationsManager.triggerImmediateEvent(Constant.Jiyo_Notification_ID, 0,
												null, null);
										showIcon(Constant.mIcon);
										LogEventClass.logAlwaysEvent("new JiyoApp news-feed");
									}
								});

								synchronized (Application.getEventLock()) {
									UiEngine ui = Ui.getUiEngine();
									dialogForNewsFeedIndicator screen = new dialogForNewsFeedIndicator(rr);
									ui.pushGlobalScreen(screen, 1, UiEngine.GLOBAL_QUEUE);
								}
							}
							Constant.oldNewsLength = newNewsArrayLength;
							Constant.latestNewsHashTable = NewsNewsFeedshashtable;

							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof EntryScreen2 && NewsNewsFeedshashtable != null) {
								Controller.showScreen(new EntryScreen2(NewsNewsFeedshashtable));
							}
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
		}
	}

	void showIcon(ApplicationIcon icon) {
		try {
			ApplicationIndicatorRegistry reg = ApplicationIndicatorRegistry.getInstance();
			ApplicationIndicator appIndicator = reg.getApplicationIndicator();
			// appIndicator.setIcon(icon);
			appIndicator.setVisible(true);
		} catch (Exception e) {
		}
	}
}
