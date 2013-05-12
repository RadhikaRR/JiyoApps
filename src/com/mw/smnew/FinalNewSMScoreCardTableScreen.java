package com.mw.smnew;

import java.util.Hashtable;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.constants.Constant;
import com.mw.customfields.BorderLabelFieldImpl;
import com.mw.customfields.LabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;

public class FinalNewSMScoreCardTableScreen extends MainScreen {

	private static final int hbgColor = 0x000072BC;

	private GridFieldManager grid2;

	public FinalNewSMScoreCardTableScreen(Hashtable hashtable) {
		super(Manager.HORIZONTAL_SCROLL | Manager.VERTICAL_SCROLL);

		showTable(hashtable);
	}

	private void showTable(Hashtable hashtable) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		Font myfont1 = Font.getDefault().derive(Font.BOLD);

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);
		ShowToolbar.INSTANCE.setScreenTitle("MobZone MIS");

		String salesManagerName = Constant.managerName;

		if (salesManagerName != null
				&& !salesManagerName.equalsIgnoreCase("null")) {

			LabelFieldImpl tblnamelbl = new LabelFieldImpl(salesManagerName
					+ " ScoreBoard", FIELD_HCENTER);
			tblnamelbl.setFont(myfont1);
			tblnamelbl.setFontColor(0x000072BC);			
			add(tblnamelbl);
		}

		String string17 = (String) hashtable.get("string17");
		LabelFieldImpl forMonthlbl = new LabelFieldImpl(string17, FIELD_HCENTER);
		//forMonthlbl.setFont(myfont);
		forMonthlbl.setFontColor(0x000072BC);
		add(forMonthlbl);

		grid2 = new GridFieldManager(8, 4, FIELD_HCENTER | FIELD_VCENTER);

		BorderLabelFieldImpl paralbl2 = new BorderLabelFieldImpl("",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		paralbl2.setFontColor(Color.WHITE);
		paralbl2.setBgColor(hbgColor);
		grid2.add(paralbl2);

		BorderLabelFieldImpl lnameHeaderlbl = new BorderLabelFieldImpl(
				"Target", Field.FOCUSABLE | USE_ALL_WIDTH);
		lnameHeaderlbl.setFontColor(Color.WHITE);
		lnameHeaderlbl.setBgColor(hbgColor);
		grid2.add(lnameHeaderlbl);

		BorderLabelFieldImpl reglbl = new BorderLabelFieldImpl("Actual",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		reglbl.setFontColor(Color.WHITE);
		reglbl.setBgColor(hbgColor);
		grid2.add(reglbl);

		BorderLabelFieldImpl gradelbl = new BorderLabelFieldImpl("Grade",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		gradelbl.setFontColor(Color.WHITE);
		gradelbl.setBgColor(hbgColor);
		grid2.add(gradelbl);

		BorderLabelFieldImpl cratiolbl = new BorderLabelFieldImpl("Cost Ratio",
				Field.FOCUSABLE | USE_ALL_WIDTH);

		grid2.add(cratiolbl);

		String string1 = (String) hashtable.get("string1");
		BorderLabelFieldImpl cratiolblval = new BorderLabelFieldImpl(string1,
				Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(cratiolblval);

		String string2 = (String) hashtable.get("string2");
		BorderLabelFieldImpl cratiolblvaltwo = new BorderLabelFieldImpl(
				string2, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(cratiolblvaltwo);

		String string3 = (String) hashtable.get("string3");
		BorderLabelFieldImpl cratiolblvalthree = new BorderLabelFieldImpl(
				string3, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(cratiolblvalthree);

		BorderLabelFieldImpl unqreglbl = new BorderLabelFieldImpl(
				"URN Generation", Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(unqreglbl);

		String string4 = (String) hashtable.get("string4");
		BorderLabelFieldImpl unqreglblval = new BorderLabelFieldImpl(string4,
				Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(unqreglblval);

		String string5 = (String) hashtable.get("string5");
		BorderLabelFieldImpl unqreglblvaltwo = new BorderLabelFieldImpl(
				string5, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(unqreglblvaltwo);

		String string6 = (String) hashtable.get("string6");
		BorderLabelFieldImpl unqreglblvalthree = new BorderLabelFieldImpl(
				string6, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(unqreglblvalthree);

		BorderLabelFieldImpl tcertilbl = new BorderLabelFieldImpl(
				"New Licensing", Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(tcertilbl);

		String string7 = (String) hashtable.get("string7");
		BorderLabelFieldImpl tcertilblval = new BorderLabelFieldImpl(string7,
				Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(tcertilblval);

		String string8 = (String) hashtable.get("string8");
		BorderLabelFieldImpl tcertilblvaltwo = new BorderLabelFieldImpl(
				string8, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(tcertilblvaltwo);

		String string9 = (String) hashtable.get("string9");
		BorderLabelFieldImpl tcertilblvalthree = new BorderLabelFieldImpl(
				string9, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(tcertilblvalthree);

		BorderLabelFieldImpl agliclbl = new BorderLabelFieldImpl(
				"IC Activisation", Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agliclbl);

		String string10 = (String) hashtable.get("string10");
		BorderLabelFieldImpl agliclblval = new BorderLabelFieldImpl(string10,
				Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agliclblval);

		String string11 = (String) hashtable.get("string11");
		BorderLabelFieldImpl agliclblvaltwo = new BorderLabelFieldImpl(
				string11, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agliclblvaltwo);

		String string12 = (String) hashtable.get("string12");
		BorderLabelFieldImpl agliclblvalthree = new BorderLabelFieldImpl(
				string12, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agliclblvalthree);

		BorderLabelFieldImpl agactlbl = new BorderLabelFieldImpl(
				"Activity Record", Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agactlbl);

		String string13 = (String) hashtable.get("string13");
		BorderLabelFieldImpl agactlblval = new BorderLabelFieldImpl(string13,
				Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agactlblval);

		String string14 = (String) hashtable.get("string14");
		BorderLabelFieldImpl agactlblvaltwo = new BorderLabelFieldImpl(
				string14, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agactlblvaltwo);

		String string15 = (String) hashtable.get("string15");
		BorderLabelFieldImpl agactlblvalthree = new BorderLabelFieldImpl(
				string15, Field.FOCUSABLE | USE_ALL_WIDTH);
		grid2.add(agactlblvalthree);

		BorderLabelFieldImpl scTotalLabel = new BorderLabelFieldImpl(
				"Score Card Total", Field.FOCUSABLE | USE_ALL_WIDTH);
		scTotalLabel.setFontColor(Color.WHITE);
		scTotalLabel.setBgColor(hbgColor);
		grid2.add(scTotalLabel);

		LabelFieldImpl dummy = new LabelFieldImpl("",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		dummy.setFontColor(Color.WHITE);
		dummy.setBgColor(hbgColor);
		grid2.add(dummy);

		LabelFieldImpl dummy2 = new LabelFieldImpl("",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		dummy2.setFontColor(Color.WHITE);
		dummy2.setBgColor(hbgColor);
		grid2.add(dummy2);

		String string16 = (String) hashtable.get("string16");
		BorderLabelFieldImpl scTotalLabelVal = new BorderLabelFieldImpl(
				string16, Field.FOCUSABLE | USE_ALL_WIDTH);
		scTotalLabelVal.setFontColor(Color.WHITE);
		scTotalLabelVal.setBgColor(hbgColor);
		grid2.add(scTotalLabelVal);
		
		BorderLabelFieldImpl incntvLabel = new BorderLabelFieldImpl(
				"Parameters Qualified", Field.FOCUSABLE | USE_ALL_WIDTH);
		incntvLabel.setFontColor(Color.WHITE);
		incntvLabel.setBgColor(hbgColor);
		grid2.add(incntvLabel);

		LabelFieldImpl dummy1 = new LabelFieldImpl("",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		dummy1.setFontColor(Color.WHITE);
		dummy1.setBgColor(hbgColor);
		grid2.add(dummy1);

		LabelFieldImpl dummy3 = new LabelFieldImpl("",
				Field.FOCUSABLE | USE_ALL_WIDTH);
		dummy3.setFontColor(Color.WHITE);
		dummy3.setBgColor(hbgColor);
		grid2.add(dummy3);

		String string18 = (String) hashtable.get("string18");
		BorderLabelFieldImpl incntvLabelVal = new BorderLabelFieldImpl(
				string18, Field.FOCUSABLE | USE_ALL_WIDTH);
		incntvLabelVal.setFontColor(Color.WHITE);
		incntvLabelVal.setBgColor(hbgColor);
		grid2.add(incntvLabelVal);
		add(grid2);
	}

	public boolean onClose() {
		backHandling();
		return true;
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

	protected boolean onSave() {
		return true;
	}

	private void backHandling() {

		synchronized (UiApplication.getEventLock()) {

			Screen screen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen);
			Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
			if (screen2 instanceof PopupSpinnerScreen) {
				UiApplication.getUiApplication().popScreen(screen2);
			}
		}
	}
}
