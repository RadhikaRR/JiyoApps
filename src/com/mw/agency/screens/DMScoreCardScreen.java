package com.mw.agency.screens;

import java.util.Hashtable;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.customfields.BorderLabelFieldImpl;
import com.mw.customfields.HeadingBackgrndLabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;

public class DMScoreCardScreen extends MainScreen {

	private GridFieldManager Grid, Grid1;
	private Font myfont;

	public DMScoreCardScreen(Hashtable hashtable) {

		super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL);

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("MobZone MIS");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		myfont = Font.getDefault().derive(Font.BOLD);
		ShowDMGridTable(hashtable);
		ShowDMGridTable1(hashtable);

		VerticalFieldManager totalHorizontalFieldManager = new VerticalFieldManager(
				Manager.HORIZONTAL_SCROLL);

		HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager();
		horizontalFieldManager.add(Grid);

		HorizontalFieldManager horizontalFieldManager1 = new HorizontalFieldManager();
		horizontalFieldManager1.add(Grid1);

		totalHorizontalFieldManager.add(horizontalFieldManager);
		totalHorizontalFieldManager.add(horizontalFieldManager1);

		add(totalHorizontalFieldManager);
	}

	public void ShowDMGridTable(Hashtable hashtable) {

		Grid = new GridFieldManager(7, 4, FIELD_HCENTER | USE_ALL_WIDTH);

		Grid.setColumnProperty(0, GridFieldManager.FIXED_SIZE, 325);
		Grid.setColumnProperty(1, GridFieldManager.FIXED_SIZE, 200);
		Grid.setColumnProperty(2, GridFieldManager.FIXED_SIZE, 200);
		Grid.setColumnProperty(3, GridFieldManager.FIXED_SIZE, 200);

		HeadingBackgrndLabelFieldImpl productMixlbl1 = new HeadingBackgrndLabelFieldImpl(
				"Parameter", Field.FOCUSABLE | USE_ALL_WIDTH);
		productMixlbl1.setFont(myfont);
		Grid.add(productMixlbl1);

		HeadingBackgrndLabelFieldImpl productMixlbl2 = new HeadingBackgrndLabelFieldImpl(
				"Target", Field.FOCUSABLE | USE_ALL_WIDTH);
		productMixlbl2.setFont(myfont);
		Grid.add(productMixlbl2);

		HeadingBackgrndLabelFieldImpl productMixlbl3 = new HeadingBackgrndLabelFieldImpl(
				"Actual", Field.FOCUSABLE | USE_ALL_WIDTH);
		productMixlbl3.setFont(myfont);
		Grid.add(productMixlbl3);

		HeadingBackgrndLabelFieldImpl productMixlbl4 = new HeadingBackgrndLabelFieldImpl(
				"Points", Field.FOCUSABLE | USE_ALL_WIDTH);
		productMixlbl4.setFont(myfont);
		Grid.add(productMixlbl4);

		BorderLabelFieldImpl b5 = new BorderLabelFieldImpl(
				"% Achv on Credited Premium", Field.USE_ALL_WIDTH);
		Grid.add(b5);

		String one = (String) hashtable.get("string1");
		BorderLabelFieldImpl b6 = new BorderLabelFieldImpl(one, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b6);

		String two = (String) hashtable.get("string2");
		BorderLabelFieldImpl b7 = new BorderLabelFieldImpl(two, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b7);

		String three = (String) hashtable.get("string3");
		int index = three.indexOf('.');
		BorderLabelFieldImpl b8;
		String s = three.substring(index);
		if (s.length() < 4) {
			b8 = new BorderLabelFieldImpl(three, USE_ALL_WIDTH
					| Field.FOCUSABLE);
		} else {
			String s2 = three.substring(0, index + 5);
			b8 = new BorderLabelFieldImpl(s2, USE_ALL_WIDTH | Field.FOCUSABLE);
		}
		Grid.add(b8);

		BorderLabelFieldImpl b9 = new BorderLabelFieldImpl(
				"New Licensed IC 's", USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b9);

		String four = (String) hashtable.get("string4");
		BorderLabelFieldImpl b10 = new BorderLabelFieldImpl(four, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b10);

		String five = (String) hashtable.get("string5");
		BorderLabelFieldImpl b11 = new BorderLabelFieldImpl(five, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b11);

		String six = (String) hashtable.get("string6");
		BorderLabelFieldImpl b12 = new BorderLabelFieldImpl(six, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b12);

		BorderLabelFieldImpl b13 = new BorderLabelFieldImpl("Active IC's",
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b13);

		String seven = (String) hashtable.get("string7");
		BorderLabelFieldImpl b14 = new BorderLabelFieldImpl(seven,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b14);

		String eight = (String) hashtable.get("string8");
		BorderLabelFieldImpl b15 = new BorderLabelFieldImpl(eight,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b15);

		String nine = (String) hashtable.get("string9");
		BorderLabelFieldImpl b16 = new BorderLabelFieldImpl(nine, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b16);

		BorderLabelFieldImpl b17 = new BorderLabelFieldImpl(
				"SM/BDM >3 Time CTC", USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b17);

		String ten = (String) hashtable.get("string10");
		BorderLabelFieldImpl b18 = new BorderLabelFieldImpl(ten, USE_ALL_WIDTH
				| Field.FOCUSABLE);
		Grid.add(b18);

		String eleven = (String) hashtable.get("string11");
		BorderLabelFieldImpl b19 = new BorderLabelFieldImpl(eleven,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b19);

		String twelve = (String) hashtable.get("string12");
		BorderLabelFieldImpl b20 = new BorderLabelFieldImpl(twelve,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b20);

		BorderLabelFieldImpl b21 = new BorderLabelFieldImpl("Renewal GWP Achv",
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b21);

		String thirteen = (String) hashtable.get("string13");
		BorderLabelFieldImpl b22 = new BorderLabelFieldImpl(thirteen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b22);

		String fourteen = (String) hashtable.get("string14");
		BorderLabelFieldImpl b23 = new BorderLabelFieldImpl(fourteen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b23);

		String fifteen = (String) hashtable.get("string15");
		BorderLabelFieldImpl b24 = new BorderLabelFieldImpl(fifteen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b24);

		BorderLabelFieldImpl b25 = new BorderLabelFieldImpl(
				"BM/UM Earning GS Bonus", USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b25);

		String sixteen = (String) hashtable.get("string16");
		BorderLabelFieldImpl b26 = new BorderLabelFieldImpl(sixteen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b26);

		String seventeen = (String) hashtable.get("string17");
		BorderLabelFieldImpl b27 = new BorderLabelFieldImpl(seventeen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b27);

		String eighteen = (String) hashtable.get("string18");
		BorderLabelFieldImpl b28 = new BorderLabelFieldImpl(eighteen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid.add(b28);
	}

	public void ShowDMGridTable1(Hashtable hashtable) {
		Grid1 = new GridFieldManager(2, 2, FIELD_HCENTER | USE_ALL_WIDTH);
		Grid1.setColumnProperty(0, GridFieldManager.FIXED_SIZE, 729);
		Grid1.setColumnProperty(1, GridFieldManager.FIXED_SIZE, 200);

		HeadingBackgrndLabelFieldImpl productMixlbl29 = new HeadingBackgrndLabelFieldImpl(
				"Total Points", USE_ALL_WIDTH | Field.FOCUSABLE);

		productMixlbl29.setFont(myfont);
		Grid1.add(productMixlbl29);

		String nineteen = (String) hashtable.get("string19");
		BorderLabelFieldImpl b30 = new BorderLabelFieldImpl(nineteen,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid1.add(b30);

		HeadingBackgrndLabelFieldImpl productMixlbl33 = new HeadingBackgrndLabelFieldImpl(
				"Total parameters qualified", USE_ALL_WIDTH | Field.FOCUSABLE);
		productMixlbl33.setFont(myfont);
		Grid1.add(productMixlbl33);

		String twenty = (String) hashtable.get("string20");
		BorderLabelFieldImpl b34 = new BorderLabelFieldImpl(twenty,
				USE_ALL_WIDTH | Field.FOCUSABLE);
		Grid1.add(b34);
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
		return true;
	}

	protected void onExposed() {

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager
				.getField(1);
		int count = toolBarField.getFieldCount();
		for (int i = 0; i < count; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField
					.getField(i);
			tab2.setActive(false);
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}
}
