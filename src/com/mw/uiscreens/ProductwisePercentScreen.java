package com.mw.uiscreens;

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

public class ProductwisePercentScreen extends MainScreen {

	private GridFieldManager grid2;

	public ProductwisePercentScreen(Hashtable productPercenthashtable) {
		super(Manager.HORIZONTAL_SCROLL | Manager.VERTICAL_SCROLL);

		showTable(productPercenthashtable);
	}

	private void showTable(Hashtable hashtable) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		VerticalFieldManager verticalFieldManager1 = ShowToolbar.INSTANCE
				.show();

		setTitle(verticalFieldManager1);
		ShowToolbar.INSTANCE.setScreenTitle("MIS Tracker");

		LabelFieldImpl tableTitle = new LabelFieldImpl(
				"Productwise Percentage", FIELD_HCENTER);
		Font myfont = Font.getDefault().derive(Font.BOLD);// , 7, Ui.UNITS_pt);
		tableTitle.setFont(myfont);
		tableTitle.setFontColor(0x000072BC);
		add(tableTitle);

		grid2 = new GridFieldManager(11, 4, FIELD_HCENTER | FIELD_VCENTER);

		HeadingBackgrndLabelFieldImpl productMixlbl = new HeadingBackgrndLabelFieldImpl(
				"Product Mix", Field.FOCUSABLE | USE_ALL_WIDTH);
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
		HeadingBackgrndLabelFieldImpl ytdHeaderlbl = new HeadingBackgrndLabelFieldImpl(
				"YTD    ", Field.FOCUSABLE | USE_ALL_WIDTH);
		ytdHeaderlbl.setFont(myfont);
		grid2.add(ytdHeaderlbl);

		grid2.add(new BackgrndLabelFieldImpl("Group Trad-RP", Field.FOCUSABLE
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

		grid2.add(new OddBackGrndLabelImpl("Group Trad-SP", Field.FOCUSABLE
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

		grid2.add(new BackgrndLabelFieldImpl("Group ULIP-RP", Field.FOCUSABLE
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

		grid2.add(new OddBackGrndLabelImpl("Group ULIP-SP", Field.FOCUSABLE
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

		BackgrndLabelFieldImpl groupTotalLbl = new BackgrndLabelFieldImpl(
				"GROUP TOTAL", Field.FOCUSABLE | USE_ALL_WIDTH);
		Font font = Font.getDefault().derive(Font.BOLD);// , 7, Ui.UNITS_pt);
		groupTotalLbl.setFont(font);
		grid2.add(groupTotalLbl);

		String string25 = (String) hashtable.get("string25");
		BackgrndLabelFieldImpl ftdTotalLbl = new BackgrndLabelFieldImpl(
				string25, Field.FOCUSABLE | USE_ALL_WIDTH);
		ftdTotalLbl.setFont(font);
		grid2.add(ftdTotalLbl);

		String string26 = (String) hashtable.get("string26");
		BackgrndLabelFieldImpl mtdTotalLbl = new BackgrndLabelFieldImpl(
				string26, Field.FOCUSABLE | USE_ALL_WIDTH);
		mtdTotalLbl.setFont(font);
		grid2.add(mtdTotalLbl);

		String string27 = (String) hashtable.get("string27");
		BackgrndLabelFieldImpl ytdTotalLbl = new BackgrndLabelFieldImpl(
				string27, Field.FOCUSABLE | USE_ALL_WIDTH);
		ytdTotalLbl.setFont(font);
		grid2.add(ytdTotalLbl);

		grid2.add(new OddBackGrndLabelImpl("Trad-RP", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string7 = (String) hashtable.get("string7");
		grid2.add(new OddBackGrndLabelImpl(string7, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string8 = (String) hashtable.get("string8");
		grid2.add(new OddBackGrndLabelImpl(string8, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string9 = (String) hashtable.get("string9");
		grid2.add(new OddBackGrndLabelImpl(string9, Field.FOCUSABLE
				| USE_ALL_WIDTH));

		grid2.add(new BackgrndLabelFieldImpl("Trad-SP", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string10 = (String) hashtable.get("string10");
		grid2.add(new BackgrndLabelFieldImpl(string10, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string11 = (String) hashtable.get("string11");
		grid2.add(new BackgrndLabelFieldImpl(string11, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string12 = (String) hashtable.get("string12");
		grid2.add(new BackgrndLabelFieldImpl(string12, Field.FOCUSABLE
				| USE_ALL_WIDTH));

		grid2.add(new OddBackGrndLabelImpl("ULIP-RP", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string19 = (String) hashtable.get("string19");
		grid2.add(new OddBackGrndLabelImpl(string19, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string20 = (String) hashtable.get("string20");
		grid2.add(new OddBackGrndLabelImpl(string20, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string21 = (String) hashtable.get("string21");
		grid2.add(new OddBackGrndLabelImpl(string21, Field.FOCUSABLE
				| USE_ALL_WIDTH));

		grid2.add(new BackgrndLabelFieldImpl("ULIP-SP", Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string22 = (String) hashtable.get("string22");
		grid2.add(new BackgrndLabelFieldImpl(string22, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string23 = (String) hashtable.get("string23");
		grid2.add(new BackgrndLabelFieldImpl(string23, Field.FOCUSABLE
				| USE_ALL_WIDTH));
		String string24 = (String) hashtable.get("string24");
		grid2.add(new BackgrndLabelFieldImpl(string24, Field.FOCUSABLE
				| USE_ALL_WIDTH));

		OddBackGrndLabelImpl indvTotalLbl = new OddBackGrndLabelImpl(
				"INDV TOTAL", Field.FOCUSABLE | USE_ALL_WIDTH);
		Font font2 = Font.getDefault().derive(Font.BOLD);// , 7, Ui.UNITS_pt);
		indvTotalLbl.setFont(font2);
		grid2.add(indvTotalLbl);

		String string28 = (String) hashtable.get("string28");
		OddBackGrndLabelImpl ftdUlipTotalLbl = new OddBackGrndLabelImpl(
				string28, Field.FOCUSABLE | USE_ALL_WIDTH);
		ftdUlipTotalLbl.setFont(font2);
		grid2.add(ftdUlipTotalLbl);

		String string29 = (String) hashtable.get("string29");
		OddBackGrndLabelImpl mtdUlipTotalLbl = new OddBackGrndLabelImpl(
				string29, Field.FOCUSABLE | USE_ALL_WIDTH);
		mtdUlipTotalLbl.setFont(font2);
		grid2.add(mtdUlipTotalLbl);

		String string30 = (String) hashtable.get("string30");
		OddBackGrndLabelImpl ytdUlipTotalLbl = new OddBackGrndLabelImpl(
				string30, Field.FOCUSABLE | USE_ALL_WIDTH);
		ytdUlipTotalLbl.setFont(font2);
		grid2.add(ytdUlipTotalLbl);

		add(grid2);

	}

	public boolean onClose() {

		synchronized (UiApplication.getEventLock()) {
			try {
				Screen screen = UiApplication.getUiApplication()
						.getActiveScreen();
				UiApplication.getUiApplication().popScreen(screen);
				Screen screen2 = UiApplication.getUiApplication()
						.getActiveScreen();
				UiApplication.getUiApplication().popScreen(screen2);

			} catch (Exception e) {

			}
		}
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
}
