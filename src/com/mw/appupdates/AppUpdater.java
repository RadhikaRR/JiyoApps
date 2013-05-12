package com.mw.appupdates;

import java.io.IOException;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.mw.constants.Constant;
import com.mw.control.LogEventClass;
import com.mw.webservice.CallService;

public class AppUpdater {

	public static AppUpdater INSTANCE = new AppUpdater();

	public boolean doCheckForUpdates(String newVersion) {
		if (newVersion != null) {

			String oldVersion = getAppVersion();

			if (!newVersion.equalsIgnoreCase(oldVersion)) {
				return true;
			}
		}
		return false;
	}

	public String getAppVersion() {
		String version = ApplicationDescriptor.currentApplicationDescriptor().getVersion();
		return version;
	}

	String newURL = "";
	int counter = 0;

	public void openDownloadURL(String downloadFullURL) {
		try {
			if (Constant.connectTCP) {
				newURL = CallService.INSTANCE.connectTCP(downloadFullURL);
			} else if (Constant.connectBIS) {
				newURL = CallService.INSTANCE.connectBIS(downloadFullURL);
			}
			// final String newURL = CallService.INSTANCE
			// .doChecKConnectionWithoutTimeout(downloadFullURL);

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Browser.getDefaultSession().displayPage(newURL);
					System.exit(0);
				}
			});
		} catch (final IOException ioException) {
			LogEventClass.logAlwaysEvent("while openDownloadURL- "
					+ ioException.getMessage());
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("" + ioException.getMessage());
				}
			});
		} catch (Exception e) {
			LogEventClass.logAlwaysEvent("while openDownloadURL- "+ e.getMessage());
			counter += 1;
			if (counter == 1) {
				Constant.connectTCP = false;
				Constant.connectBIS = true;
				openDownloadURL(downloadFullURL);
			} else {
				counter = 0;
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert("unable to connect to internet,Please check connection and try again");
						System.exit(0);
					}
				});
			}			
		}
	}
}
