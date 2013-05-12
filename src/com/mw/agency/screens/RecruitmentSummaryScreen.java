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

public class RecruitmentSummaryScreen extends MainScreen {

	private static final int hbgColor = 0x000072BC;

	private GridFieldManager Grid2;

	Font font = Font.getDefault().derive(Font.PLAIN, 7, Ui.UNITS_pt);

	public RecruitmentSummaryScreen(Hashtable hashtable) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("MIS");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		HorizontalFieldManager labelHorizontalFieldManager = new HorizontalFieldManager();
		BorderLabelFieldImpl label = new BorderLabelFieldImpl(
				"Recruitment Summary", Field.FOCUSABLE
						| USE_ALL_WIDTH | DrawStyle.HCENTER) {
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
		HorizontalFieldManager gridHorizontalFieldManager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.VERTICAL_SCROLL);
		gridHorizontalFieldManager.add(Grid2);

		add(labelHorizontalFieldManager);
		add(gridHorizontalFieldManager);

	}

	public void GridTable(Hashtable hashtable) {

		Grid2 = new GridFieldManager(8, 3, Field.FIELD_HCENTER
				| Field.USE_ALL_WIDTH);

		Grid2.setColumnProperty(1, GridFieldManager.FIXED_SIZE, 100);
		Grid2.setColumnProperty(2, GridFieldManager.FIXED_SIZE, 100);
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

		BorderLabelFieldImpl b2 = new BorderLabelFieldImpl("FTM",
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

		BorderLabelFieldImpl b3 = new BorderLabelFieldImpl("YTD",
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
		
		BorderLabelFieldImpl b4 = new BorderLabelFieldImpl("URN Target",
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

		String one = (String) hashtable.get("string1");
		BorderLabelFieldImpl b5 = new BorderLabelFieldImpl(one, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b5.setFont(font);
		Grid2.add(b5);

		String two = (String) hashtable.get("string2");
		BorderLabelFieldImpl b6 = new BorderLabelFieldImpl(two, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b6.setFont(font);
		Grid2.add(b6);

		BorderLabelFieldImpl b7 = new BorderLabelFieldImpl("Achv",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b7.setFont(font);
		b7.setFontColor(Color.WHITE);
		b7.setBgColor(hbgColor);
		Grid2.add(b7);

		String three = (String) hashtable.get("string3");
		BorderLabelFieldImpl b8 = new BorderLabelFieldImpl(three, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b8.setFont(font);
		Grid2.add(b8);

		String four = (String) hashtable.get("string4");
		BorderLabelFieldImpl b9 = new BorderLabelFieldImpl(four, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b9.setFont(font);
		Grid2.add(b9);

		BorderLabelFieldImpl b10 = new BorderLabelFieldImpl("% Achv",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b10.setFont(font);
		b10.setFontColor(Color.WHITE);
		b10.setBgColor(hbgColor);
		Grid2.add(b10);

		String five = (String) hashtable.get("string5");
		BorderLabelFieldImpl b11 = new BorderLabelFieldImpl(five, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b11.setFont(font);
		Grid2.add(b11);

		String six = (String) hashtable.get("string6");
		BorderLabelFieldImpl b12 = new BorderLabelFieldImpl(six, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b12.setFont(font);
		Grid2.add(b12);

		BorderLabelFieldImpl b13 = new BorderLabelFieldImpl("License Target",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b13.setFont(font);
		b13.setFontColor(Color.WHITE);
		b13.setBgColor(hbgColor);
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

		BorderLabelFieldImpl b16 = new BorderLabelFieldImpl("Achv",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b16.setFont(font);
		b16.setFontColor(Color.WHITE);
		b16.setBgColor(hbgColor);
		Grid2.add(b16);

		String nine = (String) hashtable.get("string9");
		BorderLabelFieldImpl b17 = new BorderLabelFieldImpl(nine, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b17.setFont(font);
		Grid2.add(b17);

		String ten = (String) hashtable.get("string10");
		BorderLabelFieldImpl b18 = new BorderLabelFieldImpl(ten, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b18.setFont(font);
		Grid2.add(b18);

		BorderLabelFieldImpl b19 = new BorderLabelFieldImpl(
				"Achv > 10K Adj Premium", Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b19.setFont(font);
		b19.setFontColor(Color.WHITE);
		b19.setBgColor(hbgColor);
		Grid2.add(b19);

		String elev = (String) hashtable.get("string11");
		BorderLabelFieldImpl b20 = new BorderLabelFieldImpl(elev, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b20.setFont(font);
		Grid2.add(b20);

		String twe = (String) hashtable.get("string12");
		BorderLabelFieldImpl b21 = new BorderLabelFieldImpl(twe, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b21.setFont(font);
		Grid2.add(b21);

		BorderLabelFieldImpl b22 = new BorderLabelFieldImpl(
				"% Achv > 10K Adj Premium", Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b22.setFont(font);
		b22.setFontColor(Color.WHITE);
		b22.setBgColor(hbgColor);
		Grid2.add(b22);

		String thirt = (String) hashtable.get("string13");
		BorderLabelFieldImpl b23 = new BorderLabelFieldImpl(thirt, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b23.setFont(font);
		Grid2.add(b23);

		String fourt = (String) hashtable.get("string14");
		BorderLabelFieldImpl b24 = new BorderLabelFieldImpl(fourt, Field.FOCUSABLE
				| USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b24.setFont(font);
		Grid2.add(b24);
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
