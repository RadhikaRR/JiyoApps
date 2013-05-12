package com.mw.cameraCapture;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.mw.control.Controller;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.webservice.CallService;

public class dialogPopUpScreen extends PopupScreen {

	public static LabelField label1, label2;
	private EditField edit1;
	private ButtonField btnUpload, btnCancel;
	private LabelField msgText;
	//public static Vector v;
	private String PolicyId;

	public dialogPopUpScreen(String msg, final Vector v) {

		super(new VerticalFieldManager(), Field.FOCUSABLE);

		//dialogPopUpScreen.v = v;

		msgText = new LabelField(msg, Field.USE_ALL_WIDTH | Field.FIELD_HCENTER
				| Field.NON_FOCUSABLE);

		HorizontalFieldManager oneHorizontalFieldManager = new HorizontalFieldManager();
		label1 = new LabelField("Claim No. : ", Field.NON_FOCUSABLE);
		edit1 = new EditField("", "", 24, FOCUSABLE);
		//edit1.setText("OC-11-1901-8403-00000001");
		oneHorizontalFieldManager.add(label1);
		oneHorizontalFieldManager.add(edit1);

		HorizontalFieldManager btnHorizontalFieldManager = new HorizontalFieldManager(
				Field.FIELD_HCENTER);

		btnUpload = new ButtonField("Upload", Field.FIELD_HCENTER);
		btnUpload.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				if (field == btnUpload) {
					PolicyId = edit1.getText().toString();
					if (PolicyId.trim().equalsIgnoreCase("")) {
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {
									public void run() {
										Screen screen = UiApplication
												.getUiApplication()
												.getActiveScreen();
										if (screen instanceof PopupSpinnerScreen) {
											UiApplication.getUiApplication()
													.popScreen(screen);
										}
										Dialog
												.alert("Please enter valid PolicyId!");
										edit1.setText("");
									}
								});
					} else {
						Controller.showScreen(new PopupSpinnerScreen(
								"Authenticating Policy Id..."));

						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;
								flag = CallService.INSTANCE.callvalidateClmPolicyWS(
										PolicyId, v);
								if(flag == false){
									flag = CallService.INSTANCE.callvalidateClmPolicyWS(
											PolicyId, v);
								}
							};
						};
						thread.start();
					}
				}
			}
		});

		btnCancel = new ButtonField("Cancel", Field.FIELD_HCENTER);
		btnCancel.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				if (field == btnCancel) {
					synchronized (UiApplication.getEventLock()) {
						Screen screen = UiApplication.getUiApplication()
								.getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			}
		});
		btnHorizontalFieldManager.add(btnUpload);
		btnHorizontalFieldManager.add(btnCancel);

		add(msgText);
		add(oneHorizontalFieldManager);
		add(btnHorizontalFieldManager);
	}
}
