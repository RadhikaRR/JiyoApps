package com.mw.carequote;

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

import com.mw.customfields.BorderLabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.utils.StringSplitter;

public class finalCqRiderPremiumScreen extends MainScreen {
	private GridFieldManager gridPremium;
	private static final int hbgColor = 0x000072BC;
	Font font = Font.getDefault().derive(Font.PLAIN, 7, Ui.UNITS_pt);

	public finalCqRiderPremiumScreen(String[] CqRiderPremiumList) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF, 0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("Care Quote");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		String[] name = new String[CqRiderPremiumList.length];
		String[] val1 = new String[CqRiderPremiumList.length];
		String[] val2 = new String[CqRiderPremiumList.length];

		gridPremium = new GridFieldManager(CqRiderPremiumList.length + 1, 3, Field.FIELD_HCENTER | Field.USE_ALL_WIDTH);

		gridPremium.setColumnProperty(2, GridFieldManager.FIXED_SIZE, 350);
		gridPremium.setRowPadding(30);
		gridPremium.setPadding(0, 0, 0, 2);

		BorderLabelFieldImpl b2 = new BorderLabelFieldImpl("Premium", Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b2.setFont(font);
		b2.setFontColor(Color.WHITE);
		b2.setBgColor(hbgColor);
		gridPremium.add(b2);

		BorderLabelFieldImpl b3 = new BorderLabelFieldImpl("Sum Assured", Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b3.setFont(font);
		b3.setFontColor(Color.WHITE);
		b3.setBgColor(hbgColor);
		gridPremium.add(b3);

		BorderLabelFieldImpl b1 = new BorderLabelFieldImpl("Cover Description", Field.FOCUSABLE | USE_ALL_WIDTH) {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				invalidate();
				super.paint(g);
			}
		};
		b1.setFont(font);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(hbgColor);
		gridPremium.add(b1);

		int CqRiderPremiumListLength = CqRiderPremiumList.length;
		for (int i = 0; i < CqRiderPremiumListLength; i++) {

			String temp_str = CqRiderPremiumList[i];
			String[] temp_arr = StringSplitter.INSTANCE.split(temp_str, "~");
			name[i] = temp_arr[0];
			val1[i] = temp_arr[1];
			val2[i] = temp_arr[2];

			BorderLabelFieldImpl b6 = new BorderLabelFieldImpl(val1[i], Field.USE_ALL_WIDTH | Field.FOCUSABLE
					| DrawStyle.HCENTER | DrawStyle.VCENTER) {
				protected void paint(Graphics g) {
					g.setColor(Color.BLACK);
					invalidate();
					super.paint(g);
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					this.setExtent(this.getWidth(), 50);
				}
			};
			b6.setFont(font);
			b6.setFontColor(Color.BLACK);
			gridPremium.add(b6);

			BorderLabelFieldImpl b5 = new BorderLabelFieldImpl(val2[i], Field.USE_ALL_WIDTH | Field.FOCUSABLE
					| DrawStyle.HCENTER | DrawStyle.VCENTER) {
				protected void paint(Graphics g) {
					g.setColor(Color.BLACK);
					invalidate();
					super.paint(g);
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					this.setExtent(this.getWidth(), 50);
				}
			};
			b5.setFont(font);
			b5.setFontColor(Color.BLACK);
			gridPremium.add(b5);

			BorderLabelFieldImpl b4 = new BorderLabelFieldImpl(name[i], Field.USE_ALL_WIDTH | Field.FOCUSABLE) {
				protected void paint(Graphics g) {
					g.setColor(Color.BLACK);
					invalidate();
					super.paint(g);
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					this.setExtent(this.getWidth(), 50);
				}
			};
			b4.setFont(font);
			b4.setFontColor(Color.BLACK);
			gridPremium.add(b4);
		}

		HorizontalFieldManager gridPremiumHorizontalFieldManager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL);
		gridPremiumHorizontalFieldManager.add(gridPremium);
		add(gridPremiumHorizontalFieldManager);

		int counte = ShowToolbar.INSTANCE.toolBarField.getFieldCount();
		for (int i = 0; i < counte; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField.getField(i);
			tab2.setActive(false);
		}
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

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager.getField(1);
		int count = toolBarField.getFieldCount();
		for (int i = 0; i < count; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField.getField(i);
			tab2.setActive(false);
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}
}
