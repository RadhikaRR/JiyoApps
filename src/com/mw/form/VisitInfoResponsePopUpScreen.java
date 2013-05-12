package com.mw.form;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.uiscreens.EntryScreen2;

public class VisitInfoResponsePopUpScreen extends PopupScreen {

	private ButtonField yesButton;
	private ButtonField noButton;
	private LabelField responseLabel;
	private LabelField requestLabel;
	private FieldChangeListener fieldChangeListener;
	private HorizontalFieldManager alignButtonHorizontalFieldManager;

	public VisitInfoResponsePopUpScreen(String response) {
		super(new VerticalFieldManager());
		responseLabel = new LabelField(response, FIELD_HCENTER);
		requestLabel = new LabelField("Want to upload another ?");

		yesButton = new ButtonField("Yes", FIELD_HCENTER);
		noButton = new ButtonField("No", FIELD_HCENTER);

		fieldChangeListener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == yesButton) {					
					if (
						 Constant.oldProductArray == null) {
						GetVisitInfoICCodes.INSTANCE.callgetICCodeWebService();
					} else {
						Controller
								.showScreen(new SubmitFormWithAutoCompleteListField(
										Constant.oldICCodeArray,
										Constant.oldProductArray));
					}
				} else if (field == noButton) {
					if(Constant.oldNewsHashTable!=null){
						 Controller.showScreen(new EntryScreen2(Constant.oldNewsHashTable));
					}                 
				}
			}
		};

		yesButton.setChangeListener(fieldChangeListener);
		noButton.setChangeListener(fieldChangeListener);

		alignButtonHorizontalFieldManager = new HorizontalFieldManager(
				FIELD_HCENTER);

		alignButtonHorizontalFieldManager.add(yesButton);
		alignButtonHorizontalFieldManager.add(noButton);

		add(responseLabel);
		add(requestLabel);
		add(new LabelField());
		add(alignButtonHorizontalFieldManager);
	}
}
