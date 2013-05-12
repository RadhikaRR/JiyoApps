package com.mw.attendance;

import java.util.Hashtable;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.agency.screens.AuthenticationResponsePopUpScreen;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customCalender.AttendanceCalendar;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.webservice.CallService;

public class AttendanceMarkerScreen extends MainScreen {

	private ButtonField markButton;
	private FieldChangeListener fieldChangeListener;

	public static String CurDate;
	private String latitude;
	private String longitude;
	private String imei;
	private String respMesg;

	public static AttendanceMarkerScreen INSTANCE = new AttendanceMarkerScreen();

	public AttendanceMarkerScreen() {
		// TODO Auto-generated constructor stub
	}

	public AttendanceMarkerScreen(AttendanceCalendar attendanceCalendar2) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("Attendance Marker");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();

		setTitle(verticalFieldManager);

		CurDate = new SimpleDateFormat("dd-MMM-yyyy").formatLocal(System
				.currentTimeMillis());

		markButton = new ButtonField("Mark Attendance", FIELD_HCENTER);
		fieldChangeListener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				ButtonField field2 = (ButtonField) field;
				if (field2 == markButton) {
					doMarkAttendance();
				}
			}
		};

		markButton.setChangeListener(fieldChangeListener);
		markButton.setMargin(7, 0, 0, 0);
		add(attendanceCalendar2);

		add(markButton);

		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField
					.getField(i);
			tab2.setActive(false);
		}
		markButton.setFocus();
	}

	private void doMarkAttendance() {
		
		latitude = "0.0";
		longitude = "0.0";
		imei = NewAuthenticator.INSTANCE.getIMEINumber();

		if (imei != null && latitude != null && longitude != null
				&& CurDate != null) {
			Controller.showScreen(new PopupSpinnerScreen(
					"Marking Attendance.."));
			Thread thread = new Thread() {
				public void run() {
					boolean flagg = true;
					flagg = CallService.INSTANCE.markedAttendance(imei, latitude,longitude, CurDate);
					while(flagg == false){
						flagg = CallService.INSTANCE.markedAttendance(imei, latitude,longitude, CurDate);
					}
				}
			};
			thread.start();
		}
	}

	public void converObjectToHash(Object object) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);

			if (hashtable != null) {
				manageHashTable(hashtable);
			}
		} else {
			UiApplication.getUiApplication().invokeLater(
					new Runnable() {
						public void run() {
							Status
									.show("Failed to Marked Attendance. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",3000);
						}
					});
		}
	}

	private void manageHashTable(Hashtable hashtable) {
		if (hashtable != null) {
			respMesg = (String) hashtable.get("pmessageOut");

			if (respMesg.trim().equalsIgnoreCase("USER-IS-NOT-ENABLED")
					|| respMesg.trim().equalsIgnoreCase("USER-IS-DISABLED")
					|| respMesg.trim().equalsIgnoreCase("HANDSET-IS-DISABLED")) {
				
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen2 = UiApplication.getUiApplication()
								.getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen2);
						Controller.showScreen(new AuthenticationResponsePopUpScreen(
										"Fail to Mark Attendance: " + respMesg));
					}
				});

			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen2);
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						if(screen instanceof PopupSpinnerScreen){
							UiApplication.getUiApplication().popScreen(screen);
						}						
						if(respMesg == null || respMesg.equalsIgnoreCase("null")){
							Dialog.alert("Attendance Marker Response :" + respMesg);
						}else {
							Dialog.alert(respMesg);
						}
					}
				});
			}
		}
	}

	protected void onExposed() {

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager
				.getField(1);
		for (int i = 0; i < toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField
					.getField(i);
			tab2.setActive(false);
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}

	public boolean onClose() {
		UiApplication.getUiApplication().requestBackground();
		return true;
	}
}
