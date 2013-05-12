package com.mw.agency.screens;

import java.util.Hashtable;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.customfields.BackgrndLabelFieldImpl;
import com.mw.customfields.HeadingBackgrndLabelFieldImpl;
import com.mw.customfields.LabelFieldImpl;
import com.mw.customfields.OddBackGrndLabelImpl;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;

public class RestAllManagerTableScreen extends MainScreen {

	private GridFieldManager grid2;

	public RestAllManagerTableScreen(Hashtable hashtable) {
		super(Manager.HORIZONTAL_SCROLL | Manager.VERTICAL_SCROLL);

		showTable(hashtable);

	}

	private void showTable(Hashtable hashtable) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();

		setTitle(verticalFieldManager);
		ShowToolbar.INSTANCE.setScreenTitle("MobZone MIS");

		LabelFieldImpl tableTitle = new LabelFieldImpl("Agency Sales MIS",
				FIELD_HCENTER);
		Font myfont1 = Font.getDefault().derive(Font.BOLD);
		tableTitle.setFont(myfont1);
		tableTitle.setFontColor(0x000072BC);
		add(tableTitle);

		Font myfont = Font.getDefault().derive(Font.BOLD);

		grid2 = new GridFieldManager(16, 4, FIELD_HCENTER | FIELD_VCENTER);

		HeadingBackgrndLabelFieldImpl productMixlbl = new HeadingBackgrndLabelFieldImpl(
				"Activities", Field.FOCUSABLE | USE_ALL_WIDTH);
		productMixlbl.setFont(myfont);
		grid2.add(productMixlbl);
		
		HeadingBackgrndLabelFieldImpl ftdHeaderlbl = new HeadingBackgrndLabelFieldImpl(
				"FTD    ", Field.FOCUSABLE | USE_ALL_WIDTH);
		ftdHeaderlbl.setFont(myfont);
		grid2.add(ftdHeaderlbl);
		
		HeadingBackgrndLabelFieldImpl mtdHeaderlbl = new HeadingBackgrndLabelFieldImpl(
				"MTD    ", Field.FOCUSABLE | USE_ALL_WIDTH);
		mtdHeaderlbl.setFont(myfont);
		grid2.add(mtdHeaderlbl);
		
		HeadingBackgrndLabelFieldImpl lstMonthHeaderlbl = new HeadingBackgrndLabelFieldImpl(
				"YTD    ", Field.FOCUSABLE | USE_ALL_WIDTH);
		lstMonthHeaderlbl.setFont(myfont);
		grid2.add(lstMonthHeaderlbl);
		
		

		grid2.add(new BackgrndLabelFieldImpl("URG Gen", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string1 = (String) hashtable.get("string1");
		grid2.add(new BackgrndLabelFieldImpl(string1, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string2 = (String) hashtable.get("string2");
		grid2.add(new BackgrndLabelFieldImpl(string2, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string3 = (String) hashtable.get("string3");
		grid2.add(new BackgrndLabelFieldImpl(string3, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new OddBackGrndLabelImpl("TCC Recd", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string4 = (String) hashtable.get("string4");
		grid2.add(new OddBackGrndLabelImpl(string4, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string5 = (String) hashtable.get("string5");
		grid2.add(new OddBackGrndLabelImpl(string5, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string6 = (String) hashtable.get("string6");
		grid2.add(new OddBackGrndLabelImpl(string6, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		

		
		grid2.add(new BackgrndLabelFieldImpl("Agency Lic", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string7 = (String) hashtable.get("string7");
		grid2.add(new BackgrndLabelFieldImpl(string7, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string8 = (String) hashtable.get("string8");
		grid2.add(new BackgrndLabelFieldImpl(string8, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string9 = (String) hashtable.get("string9");
		grid2.add(new BackgrndLabelFieldImpl(string9, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		
		
		grid2.add(new OddBackGrndLabelImpl("Agency Actv", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string10 = (String) hashtable.get("string10");
		grid2.add(new OddBackGrndLabelImpl(string10, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string11 = (String) hashtable.get("string11");
		grid2.add(new OddBackGrndLabelImpl(string11, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string12 = (String) hashtable.get("string12");
		grid2.add(new OddBackGrndLabelImpl(string12, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new BackgrndLabelFieldImpl("NB Coll Pr", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string13 = (String) hashtable.get("string13");
		grid2.add(new BackgrndLabelFieldImpl(string13, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string14 = (String) hashtable.get("string14");
		grid2.add(new BackgrndLabelFieldImpl(string14, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string15 = (String) hashtable.get("string15");
		grid2.add(new BackgrndLabelFieldImpl(string15, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		
		
		grid2.add(new OddBackGrndLabelImpl("NB Coll No", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string16 = (String) hashtable.get("string16");
		grid2.add(new OddBackGrndLabelImpl(string16, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string17 = (String) hashtable.get("string17");
		grid2.add(new OddBackGrndLabelImpl(string17, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string18 = (String) hashtable.get("string18");
		grid2.add(new OddBackGrndLabelImpl(string18, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		
		
		grid2.add(new BackgrndLabelFieldImpl("Undwr (Pri)", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string19 = (String) hashtable.get("string19");
		grid2.add(new BackgrndLabelFieldImpl(string19, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string20 = (String) hashtable.get("string20");
		grid2.add(new BackgrndLabelFieldImpl(string20, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string21 = (String) hashtable.get("string21");
		grid2.add(new BackgrndLabelFieldImpl(string21, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new OddBackGrndLabelImpl("Undwrt (Pol)", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string22 = (String) hashtable.get("string22");
		grid2.add(new OddBackGrndLabelImpl(string22, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string23 = (String) hashtable.get("string23");
		grid2.add(new OddBackGrndLabelImpl(string23, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string24 = (String) hashtable.get("string24");
		grid2.add(new OddBackGrndLabelImpl(string24, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new BackgrndLabelFieldImpl("SMs < 2", Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string25 = (String) hashtable.get("string25");
		grid2.add(new BackgrndLabelFieldImpl(string25, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string26 = (String) hashtable.get("string26");
		grid2.add(new BackgrndLabelFieldImpl(string26, Field.FOCUSABLE
				| USE_ALL_WIDTH));		
		String string27 = (String) hashtable.get("string27");
		grid2.add(new BackgrndLabelFieldImpl(string27, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new OddBackGrndLabelImpl("SMs > 2", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string28 = (String) hashtable.get("string28");
		grid2.add(new OddBackGrndLabelImpl(string28, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string29 = (String) hashtable.get("string29");
		grid2.add(new OddBackGrndLabelImpl(string29, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string30 = (String) hashtable.get("string30");
		grid2.add(new OddBackGrndLabelImpl(string30, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		

		
		grid2.add(new BackgrndLabelFieldImpl("SMs > 3", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string31 = (String) hashtable.get("string31");
		grid2.add(new BackgrndLabelFieldImpl(string31, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string32 = (String) hashtable.get("string32");
		grid2.add(new BackgrndLabelFieldImpl(string32, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string33 = (String) hashtable.get("string33");
		grid2.add(new BackgrndLabelFieldImpl(string33, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		

		
		grid2.add(new OddBackGrndLabelImpl("SMs > 4", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string34 = (String) hashtable.get("string34");
		grid2.add(new OddBackGrndLabelImpl(string34, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string35 = (String) hashtable.get("string35");
		grid2.add(new OddBackGrndLabelImpl(string35, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string36 = (String) hashtable.get("string36");
		grid2.add(new OddBackGrndLabelImpl(string36, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new BackgrndLabelFieldImpl("SMs > 5", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string37 = (String) hashtable.get("string37");
		grid2.add(new BackgrndLabelFieldImpl(string37, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string38 = (String) hashtable.get("string38");
		grid2.add(new BackgrndLabelFieldImpl(string38, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string39 = (String) hashtable.get("string39");
		grid2.add(new BackgrndLabelFieldImpl(string39, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		

		
		grid2.add(new OddBackGrndLabelImpl("Bus Incntv earn", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string40 = (String) hashtable.get("string40");
		grid2.add(new OddBackGrndLabelImpl(string40, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string41 = (String) hashtable.get("string41");
		grid2.add(new OddBackGrndLabelImpl(string41, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string42 = (String) hashtable.get("string42");
		grid2.add(new OddBackGrndLabelImpl(string42, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		
		

		grid2.add(new BackgrndLabelFieldImpl("Comm Earn", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string43 = (String) hashtable.get("string43");
		grid2.add(new BackgrndLabelFieldImpl(string43, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string44 = (String) hashtable.get("string44");
		grid2.add(new BackgrndLabelFieldImpl(string44, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string45 = (String) hashtable.get("string45");
		grid2.add(new BackgrndLabelFieldImpl(string45, Field.FOCUSABLE
				| USE_ALL_WIDTH));

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
			UiApplication.getUiApplication().popScreen(screen2);
		}
	}
}
