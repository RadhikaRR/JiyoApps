package com.mw.control;

import java.util.Date;
import java.util.Timer;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.notification.NotificationsConstants;
import net.rim.device.api.notification.NotificationsManager;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.mw.autorefresh.CheckNewNewsFeeds;
import com.mw.constants.Constant;
import com.mw.form.RefreshIcCodeAndProductList;
import com.mw.persist.PersistProvider;
import com.mw.sms.MeSMSSender;
import com.mw.splashscreen.SplashScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.uiscreens.RefreshChannelList;
import com.mw.webservice.CallService;

public class Controller extends UiApplication {

	public static final long NOTIFICATIONS_ID_1 = 0xdc5bf2f81374095L;
	private Timer timer;
	private String formatted_date;

	public Controller() {
		showScreen(new SplashScreen());
		invokeLater(runnable);
	}

	Runnable runnable = new Runnable() {

		public void run() {

			timer = new Timer();

			timer.schedule(new CheckNewNewsFeeds(), 15 * 60 * 1000, 15 * 60 * 1000);

			timer.schedule(new RefreshIcCodeAndProductList(), 4 * 53 * 53 * 1000, 4 * 53 * 53 * 1000);

			timer.schedule(new RefreshChannelList(), 24 * 53 * 53 * 1000, 24 * 53 * 53 * 1000);

			String smsFlag = PersistProvider.INSTANCE.getObject(Constant.SMSFLAG);
			if (smsFlag == null) {

				Date dt = new Date();
				long date1 = dt.getTime() + (1000 * 60 * 15);
				SimpleDateFormat df = new SimpleDateFormat("h:mm aa");
				formatted_date = df.format(new Date(date1));

				MeSMSSender.meSMSSender.sendSMSMessage("09773500500");

				try {
					Thread.sleep(5000);
					Dialog.alert("Registered Successfully, please reopen application after " + formatted_date);
					System.exit(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				boolean nw = CallService.INSTANCE.checkNetworkConnection();
				if (nw == true) {
					// UiApplication.getUiApplication().pushScreen(new
					// ConnectionDialog());
					NewAuthenticator.INSTANCE.authenticate("Authenticating User..");
				} else {
					Dialog.alert("Network Connection is unavailable!");
					System.exit(0);
				}
			}
		}
	};

	public static void main(String[] args) {

		Controller controller = new Controller();

		Object theSource = new Object() {
			public String toString() {
				return "Jiyo App - NewsFeed";
			}
		};
		NotificationsManager.registerSource(Constant.Jiyo_Notification_ID, theSource, NotificationsConstants.IMPORTANT);

		EventLogger.register(Constant.GUID, Constant.APP_NAME, EventLogger.VIEWER_STRING);

		controller.enterEventDispatcher();
	}

	public static void showScreen(Screen screen) {
		if (screen == null) {
			return;
		}
		synchronized (getEventLock()) {
			getUiApplication().pushScreen(screen);
		}
	}

	public void activate() {
		ShowToolbar.INSTANCE.activate();
		super.activate();
	}
}
