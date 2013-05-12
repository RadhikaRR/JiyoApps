package com.mw.uiscreens;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.mw.splashscreen.PopupSpinnerScreen;

public class ErrorScreenDialog extends PopupScreen {
	final Timer timerr;
	private LabelField msgTextMessage;
	private ButtonField retry;

	public ErrorScreenDialog(String msg) {
		super(new VerticalFieldManager(), Field.FOCUSABLE);

		VerticalFieldManager vfm = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL);

		msgTextMessage = new LabelField(msg, Field.USE_ALL_WIDTH
				| Field.FIELD_HCENTER | Field.NON_FOCUSABLE);

		timerr = new Timer();
		timerr.schedule(new removeDialog(), 55 * 1000);

		retry = new ButtonField("OK", Field.FIELD_HCENTER);
		retry.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == retry) {
					Screen screen = UiApplication.getUiApplication()
							.getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Screen screen3 = UiApplication.getUiApplication()
							.getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen3);
					Screen screen2 = UiApplication.getUiApplication()
							.getActiveScreen();
					if (screen2 instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen2);
					}
					// NewAuthenticator.INSTANCE
					// .authenticate("Retrying,Please wait!");
					timerr.cancel();
				}
			}
		});

		vfm.add(msgTextMessage);
		vfm.add(retry);
		add(vfm);
	}

	public boolean onClose() {
		synchronized (UiApplication.getEventLock()) {
			Screen screen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen);
			Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
			if (screen2 instanceof PopupSpinnerScreen) {
				UiApplication.getUiApplication().popScreen(screen2);
			}
		}
		return super.onClose();
	}

	public class removeDialog extends TimerTask {
		public void run() {
			synchronized (UiApplication.getEventLock()) {
				Screen screen = UiApplication.getUiApplication()
						.getActiveScreen();
				if (screen instanceof ErrorScreenDialog) {
					UiApplication.getUiApplication().popScreen(screen);
				}
				Screen screen2 = UiApplication.getUiApplication()
						.getActiveScreen();
				if (screen2 instanceof PopupSpinnerScreen) {
					UiApplication.getUiApplication().popScreen(screen2);
				}
			}
			timerr.cancel();
		}
	}
}
