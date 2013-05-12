package com.mw.carequote;

import java.util.Hashtable;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.control.Controller;
import com.mw.customfields.BorderLabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;

public class DemoScreen extends MainScreen {

	private GridFieldManager Grid2;
	private static final int hbgColor = 0x000072BC;
	private final ButtonField button;
	Font font = Font.getDefault().derive(Font.PLAIN, 7, Ui.UNITS_pt);

	public DemoScreen(Hashtable hashtable, final String[] CqRiderPremiumList,
			String Cqname, String CqCode) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("Care Quote");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		VerticalFieldManager v1 = new VerticalFieldManager(
				Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL);

		HorizontalFieldManager h1 = new HorizontalFieldManager(
				Field.USE_ALL_WIDTH | Manager.NO_HORIZONTAL_SCROLL) {
			public void paint(Graphics g) {
				g.setBackgroundColor(0x00B0E2FF);
				g.clear();
				invalidate();
				super.paint(g);
			}
		};
		h1.setMargin(0, 5, 0, 5);
		LabelField productLabelField = new LabelField("Product Name :");
		LabelField productproductLabelField = new LabelField(CqCode);
		h1.add(productLabelField);
		h1.add(productproductLabelField);

		HorizontalFieldManager h2 = new HorizontalFieldManager(
				Field.USE_ALL_WIDTH | Manager.NO_HORIZONTAL_SCROLL) {
			public void paint(Graphics g) {
				g.setBackgroundColor(0x00B0E2FF);
				g.clear();
				invalidate();
				super.paint(g);
			}
		};
		h2.setMargin(5, 5, 0, 5);
		LabelField nameLabelField = new LabelField("Name :");
		LabelField namenameLabelField = new LabelField(Cqname);
		h2.add(nameLabelField);
		h2.add(namenameLabelField);

		HorizontalFieldManager h3 = new HorizontalFieldManager(
				Field.USE_ALL_WIDTH | Manager.NO_HORIZONTAL_SCROLL);
		GridFieldManager Grid1 = new GridFieldManager(2, 4, 0) {
			public void paint(Graphics g) {
				g.setBackgroundColor(0x00B0E2FF);
				g.clear();
				invalidate();
				super.paint(g);
			}
		};
		h3.setMargin(0, 5, 0, 5);
		Grid1.setPadding(5, 0, 0, 0);
		Grid1.setColumnProperty(0, GridFieldManager.FIXED_SIZE, 150);
		Grid1.setColumnProperty(1, GridFieldManager.FIXED_SIZE, 125);
		Grid1.setColumnProperty(2, GridFieldManager.FIXED_SIZE, 100);
		Grid1.setColumnProperty(3, GridFieldManager.FIXED_SIZE, 115);

		Grid1
				.add(new LabelField("Age :", Field.USE_ALL_WIDTH
						| DrawStyle.LEFT));
		Grid1.add(new LabelField("" + CareQuoteDetailsScreen.ageEdit.getText(),
				Field.USE_ALL_WIDTH | DrawStyle.LEFT));

		Grid1.add(new LabelField("Gender :", Field.USE_ALL_WIDTH
				| DrawStyle.LEFT));
		Grid1.add(new LabelField("" + CareQuoteDetailsScreen.selectedSex,
				Field.USE_ALL_WIDTH | DrawStyle.LEFT));

		Grid1.add(new LabelField("Sum Assured:", Field.USE_ALL_WIDTH
				| DrawStyle.LEFT));
		Grid1.add(new LabelField(""
				+ CareQuoteDetailsScreen.sumAssuredEdit.getText(),
				Field.USE_ALL_WIDTH | DrawStyle.LEFT));

		Grid1.add(new LabelField("", Field.USE_ALL_WIDTH | DrawStyle.LEFT));
		Grid1.add(new LabelField("", Field.USE_ALL_WIDTH | DrawStyle.LEFT));

		h3.add(Grid1);

		GridTable2(hashtable);
		HorizontalFieldManager h4 = new HorizontalFieldManager(
				Manager.HORIZONTAL_SCROLL);
		h4.add(Grid2);
		h4.setPadding(5, 0, 3, 3);

		button = new ButtonField("Premium Breakup", Field.FIELD_HCENTER);
		button.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				if (field == button) {

					Controller.showScreen(new finalCqRiderPremiumScreen(
							CqRiderPremiumList));
				}
			}
		});

		v1.add(h1);
		v1.add(h2);
		v1.add(h3);
		v1.add(h4);

		add(v1);
		add(button);
		int count = ShowToolbar.INSTANCE.toolBarField.getFieldCount();
		for (int i = 0; i < count; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField
					.getField(i);
			tab2.setActive(false);
		}
		button.setFocus();
	}

	public void GridTable2(Hashtable hashtable) {

		Grid2 = new GridFieldManager(4, 5, Field.FIELD_HCENTER
				| Field.USE_ALL_WIDTH);

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

		BorderLabelFieldImpl b2 = new BorderLabelFieldImpl("Yearly    ",
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

		BorderLabelFieldImpl b3 = new BorderLabelFieldImpl("Half Yearly  ",
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

		BorderLabelFieldImpl b4 = new BorderLabelFieldImpl("Quarterly   ",
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
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

		BorderLabelFieldImpl b5 = new BorderLabelFieldImpl("Monthly    ",
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

		BorderLabelFieldImpl b6 = new BorderLabelFieldImpl("Premium",
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

		String str7 = (String) hashtable.get("stringval1");
		BorderLabelFieldImpl b7 = new BorderLabelFieldImpl(str7, USE_ALL_WIDTH
				| Field.FOCUSABLE | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b7.setFont(font);
		Grid2.add(b7);

		String str8 = (String) hashtable.get("stringval2");
		BorderLabelFieldImpl b8 = new BorderLabelFieldImpl(str8,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b8.setFont(font);
		Grid2.add(b8);

		String str9 = (String) hashtable.get("stringval3");
		BorderLabelFieldImpl b9 = new BorderLabelFieldImpl(str9,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b9.setFont(font);
		Grid2.add(b9);

		String str10 = (String) hashtable.get("stringval4");
		BorderLabelFieldImpl b10 = new BorderLabelFieldImpl(str10,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b10.setFont(font);
		Grid2.add(b10);

		BorderLabelFieldImpl b11 = new BorderLabelFieldImpl("Service Tax",
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

		String str12 = (String) hashtable.get("stringval5");
		BorderLabelFieldImpl b12 = new BorderLabelFieldImpl(str12,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b12.setFont(font);
		Grid2.add(b12);

		String str13 = (String) hashtable.get("stringval6");
		BorderLabelFieldImpl b13 = new BorderLabelFieldImpl(str13,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b13.setFont(font);
		Grid2.add(b13);

		String str14 = (String) hashtable.get("stringval7");
		BorderLabelFieldImpl b14 = new BorderLabelFieldImpl(str14,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b14.setFont(font);
		Grid2.add(b14);

		String str15 = (String) hashtable.get("stringval8");
		BorderLabelFieldImpl b15 = new BorderLabelFieldImpl(str15,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b15.setFont(font);
		Grid2.add(b15);

		BorderLabelFieldImpl b21 = new BorderLabelFieldImpl("Total",
				Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b21.setFont(font);
		b21.setFontColor(Color.WHITE);
		b21.setBgColor(hbgColor);
		Grid2.add(b21);

		String str22 = (String) hashtable.get("stringval13");
		BorderLabelFieldImpl b22 = new BorderLabelFieldImpl(str22,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b22.setFont(font);
		Grid2.add(b22);

		String str23 = (String) hashtable.get("stringval14");
		BorderLabelFieldImpl b23 = new BorderLabelFieldImpl(str23,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b23.setFont(font);
		Grid2.add(b23);

		String str24 = (String) hashtable.get("stringval15");
		BorderLabelFieldImpl b24 = new BorderLabelFieldImpl(str24,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b24.setFont(font);
		Grid2.add(b24);

		String str25 = (String) hashtable.get("stringval16");
		BorderLabelFieldImpl b25 = new BorderLabelFieldImpl(str25,
				Field.FOCUSABLE | USE_ALL_WIDTH | DrawStyle.HCENTER) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b25.setFont(font);
		Grid2.add(b25);
	}

	public boolean onClose() {

		synchronized (UiApplication.getEventLock()) {

			Screen screen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen);
			Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
			if (screen2 instanceof PopupSpinnerScreen) {
				UiApplication.getUiApplication().popScreen(screen2);
			}else{
				UiApplication.getUiApplication().popScreen(screen2);

				Screen screen3 = UiApplication.getUiApplication()
						.getActiveScreen();
				UiApplication.getUiApplication().popScreen(screen3);
			}
		}
		return true;
	}

	protected void onExposed() {

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager
				.getField(1);
		int copunt = toolBarField.getFieldCount();
		for (int i = 0; i < copunt; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField
					.getField(i);
			tab2.setActive(false);
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}
}
