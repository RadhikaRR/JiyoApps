package com.mw.splashscreen;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.mw.customfields.SpinnerField;

public class PopupSpinnerScreen extends PopupScreen {

	private VerticalFieldManager verticalFieldManager;

	public PopupSpinnerScreen(String info) {

		super(new HorizontalFieldManager());

		verticalFieldManager = new VerticalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		verticalFieldManager.add(new LabelField(info, FIELD_HCENTER));
		verticalFieldManager.add(new LabelField());
		verticalFieldManager.add(new SpinnerField());
		verticalFieldManager.add(new LabelField());

		add(verticalFieldManager);
	}
}