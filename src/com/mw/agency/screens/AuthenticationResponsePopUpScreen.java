package com.mw.agency.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class AuthenticationResponsePopUpScreen extends PopupScreen {

	private ButtonField ok;

	private LabelField labelField;
	private VerticalFieldManager verticalFieldManager;

	public AuthenticationResponsePopUpScreen(String response) {
		super(new HorizontalFieldManager());

		labelField = new LabelField(response);
		ok = new ButtonField("Ok", FIELD_HCENTER | FIELD_VCENTER);

		FieldChangeListener listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == ok) {
					System.exit(0);
				}
			}
		};

		ok.setChangeListener(listener);

		verticalFieldManager = new VerticalFieldManager();
		verticalFieldManager.add(labelField);
		verticalFieldManager.add(ok);

		add(verticalFieldManager);
	}
}
