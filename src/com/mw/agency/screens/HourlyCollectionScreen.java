package com.mw.agency.screens;

import java.util.Hashtable;

import com.mw.customfields.BorderLabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class HourlyCollectionScreen extends MainScreen {

	private static final int hbgColor = 0x000072BC;

	private GridFieldManager Grid2;

	Font font = Font.getDefault().derive(Font.PLAIN, 7, Ui.UNITS_pt);

	public HourlyCollectionScreen(Hashtable hashtable) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("MIS");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		HorizontalFieldManager labelHorizontalFieldManager = new HorizontalFieldManager();
		BorderLabelFieldImpl label = new BorderLabelFieldImpl(
				"Hourly Collection", Field.FOCUSABLE | USE_ALL_WIDTH
						| DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		label.setFont(font);
		label.setFontColor(Color.WHITE);
		label.setBgColor(hbgColor);
		label.setMargin(0, 3, 0, 3);
		labelHorizontalFieldManager.add(label);

		GridTable(hashtable);
		HorizontalFieldManager gridHorizontalFieldManager = new HorizontalFieldManager(
				Manager.HORIZONTAL_SCROLL);
		gridHorizontalFieldManager.add(Grid2);

		add(labelHorizontalFieldManager);
		add(gridHorizontalFieldManager);

	}

	public void GridTable(Hashtable hashtable) {

		Grid2 = new GridFieldManager(3, 5, Field.FIELD_HCENTER
				| Field.USE_ALL_WIDTH);
		Grid2.setPadding(0, 3, 0, 3);

		BorderLabelFieldImpl b1 = new BorderLabelFieldImpl("", Field.FOCUSABLE
				| USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b1.setFont(font);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(hbgColor);
		Grid2.add(b1);

		BorderLabelFieldImpl b2 = new BorderLabelFieldImpl("Total Collection",
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b2.setFont(font);
		b2.setFontColor(Color.WHITE);
		b2.setBgColor(hbgColor);
		Grid2.add(b2);

		BorderLabelFieldImpl b3 = new BorderLabelFieldImpl("CDA",
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b3.setFont(font);
		b3.setFontColor(Color.WHITE);
		b3.setBgColor(hbgColor);
		Grid2.add(b3);

		BorderLabelFieldImpl b4 = new BorderLabelFieldImpl("Cancel",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b4.setFont(font);
		b4.setFontColor(Color.WHITE);
		b4.setBgColor(hbgColor);
		Grid2.add(b4);

		BorderLabelFieldImpl b5 = new BorderLabelFieldImpl("Net Coll",
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b5.setFont(font);
		b5.setFontColor(Color.WHITE);
		b5.setBgColor(hbgColor);
		Grid2.add(b5);

		BorderLabelFieldImpl b6 = new BorderLabelFieldImpl("Amount",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b6.setFont(font);
		b6.setFontColor(Color.WHITE);
		b6.setBgColor(hbgColor);
		Grid2.add(b6);

		String one = (String) hashtable.get("string1");
		BorderLabelFieldImpl b7 = new BorderLabelFieldImpl(one, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b7.setFont(font);
		Grid2.add(b7);

		String two = (String) hashtable.get("string2");
		BorderLabelFieldImpl b8 = new BorderLabelFieldImpl(two, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b8.setFont(font);
		Grid2.add(b8);

		String three = (String) hashtable.get("string3");
		BorderLabelFieldImpl b9 = new BorderLabelFieldImpl(three, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b9.setFont(font);
		Grid2.add(b9);

		String four = (String) hashtable.get("string4");
		BorderLabelFieldImpl b10 = new BorderLabelFieldImpl(four, Field.FOCUSABLE
				| USE_ALL_WIDTH| DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b10.setFont(font);
		Grid2.add(b10);

		BorderLabelFieldImpl b11 = new BorderLabelFieldImpl("Proposal No",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b11.setFont(font);
		b11.setFontColor(Color.WHITE);
		b11.setBgColor(hbgColor);
		Grid2.add(b11);

		String five = (String) hashtable.get("string5");
		BorderLabelFieldImpl b12 = new BorderLabelFieldImpl(five, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b12.setFont(font);
		Grid2.add(b12);

		String six = (String) hashtable.get("string6");
		BorderLabelFieldImpl b13 = new BorderLabelFieldImpl(six, Field.FOCUSABLE
				| USE_ALL_WIDTH| DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b13.setFont(font);
		Grid2.add(b13);

		String seven = (String) hashtable.get("string7");
		BorderLabelFieldImpl b14 = new BorderLabelFieldImpl(seven, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b14.setFont(font);
		Grid2.add(b14);

		String eight = (String) hashtable.get("string8");
		BorderLabelFieldImpl b15 = new BorderLabelFieldImpl(eight, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};

		b15.setFont(font);
		Grid2.add(b15);
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
