package com.mw.attendance;

import java.util.Hashtable;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Status;

import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customCalender.AttendanceCalendar;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.webservice.CallService;

public class GetAttendanceColorCodes {

	public static GetAttendanceColorCodes INSTANCE = new GetAttendanceColorCodes();
	// private Object colorCodeobject;
	private String imei;

	public void getAttendanceColorCodes() {
		imei = NewAuthenticator.INSTANCE.getIMEINumber();
		if (imei != null) {
			callWebService(imei);
		}
	}

	private void callWebService(final String imei) {
		Controller.showScreen(new PopupSpinnerScreen(
				"Retrieving Attendance Details.."));
		Thread thread = new Thread() {
			public void run() {
				boolean flag = true;
				flag = CallService.INSTANCE.getAttendanceColorCode(imei);
				while(flag == false){
					flag = CallService.INSTANCE.getAttendanceColorCode(imei);
				}
			}
		};
		thread.start();
	}

	public void parseColorCodeObject(Object object) {
		if (object != null) {

			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);
			if (hashtable != null) {
				convertHashToArray(hashtable);
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to Fetch Attendance Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	private void convertHashToArray(Hashtable hashtable) {
		int[] colorArray = new int[32];

		for (int i = 0; i < colorArray.length; i++) {
			String colorString = (String) hashtable.get("stringval" + i);
			if (colorString == null) {
				colorString = "0xFFFFFF";
			}
			try {
				colorArray[i] = Integer.parseInt(colorString.trim()
						.substring(2), 16);
				System.out.println("color " + i + "  " + colorArray[i]);
			} catch (Exception e) {
				System.out.println("err " + e + "  " + colorString);
				colorArray[i] = 0xFF;
			}
		}

		AttendanceCalendar attendanceCalendar = new AttendanceCalendar();
		attendanceCalendar.setColorArray(colorArray);

		AttendanceMarkerScreen attendanceMarkerScreen = new AttendanceMarkerScreen(
				attendanceCalendar);

		Controller.showScreen(attendanceMarkerScreen);
	}
}
