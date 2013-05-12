package com.mw.carequote;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customfields.FilteredList;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.webservice.CallService;

public class CareQuoteDetailsScreen extends MainScreen {

	private LabelField productLabel;
	public static FilteredList productfilteredList;

	private LabelField NameLabel;
	public static EditField nameEditField;
	private ObjectChoiceField gender;
	private LabelField ageLabel;
	public static EditField ageEdit;
	public static String selectedSex;

	private LabelField sumAssuredLabel;
	public static EditField sumAssuredEdit;
	private ButtonField getDatabutton;
	private FieldChangeListener listner;

	private String selectedProduct;
	private String CqsumAssured;

	public CareQuoteDetailsScreen(final String[] cqProductArray) {

		Constant.oldCareQuoteProductArray = cqProductArray;

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF, 0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("Care Quote");
		VerticalFieldManager vfm = ShowToolbar.INSTANCE.show();
		setTitle(vfm);

		productLabel = new LabelField("Select Product ", USE_ALL_WIDTH | FIELD_LEFT);
		productLabel.setPadding(0, 10, 0, 10);

		productfilteredList = new FilteredList(cqProductArray, USE_ALL_WIDTH);

		productfilteredList.setPadding(0, 10, 0, 10);

		NameLabel = new LabelField("Enter Name  ");
		NameLabel.setPadding(10, 0, 0, 10);
		nameEditField = new EditField("", "") {

			protected void paintBackground(Graphics g) {
				g.clear();
				if (isFocus()) {
					g.setColor(0x000072BC);
					invalidate();
				} else {
					g.setColor(Color.BLACK);
				}
				g.drawRect(0, 0, getWidth(), getHeight());
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		nameEditField.setMargin(0, 10, 0, 10);

		String[] items = { "Male", "Female" };
		gender = new ObjectChoiceField("Select Gender ", items);
		gender.setPadding(10, 10, 0, 10);

		HorizontalFieldManager ageHorizontalFieldManager = new HorizontalFieldManager();

		ageHorizontalFieldManager.setPadding(10, 10, 0, 10);

		ageLabel = new LabelField("Enter Entry Age");

		ageEdit = new EditField("", "", 2, EditField.FILTER_NUMERIC) {

			protected void paintBackground(Graphics g) {
				g.clear();
				if (isFocus()) {
					g.setColor(0x000072BC);
					invalidate();
				} else {
					g.setColor(Color.BLACK);
				}
				g.drawRect(0, 0, getWidth(), getHeight());
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		ageEdit.setMargin(0, 0, 0, 95);

		ageHorizontalFieldManager.add(ageLabel);
		ageHorizontalFieldManager.add(ageEdit);

		HorizontalFieldManager sumassuredHorizontalFieldManager = new HorizontalFieldManager();
		sumassuredHorizontalFieldManager.setPadding(10, 10, 10, 10);

		sumAssuredLabel = new LabelField("Enter Sum Assured");
		sumAssuredEdit = new EditField("", "", 8, FIELD_HCENTER | USE_ALL_WIDTH | EditField.FILTER_NUMERIC) {

			protected void paintBackground(Graphics g) {
				g.clear();
				if (isFocus()) {
					g.setColor(0x000072BC);
					invalidate();
				} else {
					g.setColor(Color.BLACK);
				}
				g.drawRect(0, 0, getWidth(), getHeight());
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		sumAssuredEdit.setMargin(0, 0, 0, 60);

		sumassuredHorizontalFieldManager.add(sumAssuredLabel);
		sumassuredHorizontalFieldManager.add(sumAssuredEdit);

		getDatabutton = new ButtonField("Submit", FIELD_HCENTER);
		listner = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == getDatabutton) {
					final String imei = NewAuthenticator.INSTANCE.getIMEINumber();
					selectedProduct = productfilteredList.getText();

					CqsumAssured = sumAssuredEdit.getText();
					final String CqAge = ageEdit.getText();

					int selectedIndex = gender.getSelectedIndex();
					if (selectedIndex == 0) {
						selectedSex = "Male";
					} else {
						selectedSex = "Female";
					}

					boolean isFound = comapreArray(selectedProduct, cqProductArray);

					if (nameEditField.getText().equalsIgnoreCase("") || CqAge.equalsIgnoreCase("")
							|| CqsumAssured.equalsIgnoreCase("")) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Dialog.alert("Please enter valid Care Quote details.");
							}
						});

					} else if (Integer.parseInt(CqAge) < 18) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Dialog.alert("Please enter age more than 18 years.");
							}
						});

					} else if (Integer.parseInt(CqAge) > 60) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Dialog.alert("Please enter age lesser than 60 years.");
							}
						});

					} else if (isFound) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Dialog.alert("Please select valid Care Quote Product.");
							}
						});
						productfilteredList.setText("");
					} else {
						Controller.showScreen(new PopupSpinnerScreen("Retrieving Rider details.."));

						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;
								flag = CallService.INSTANCE.publishCqRiders(imei, selectedProduct, CqsumAssured);
								if (flag == false) {
									flag = CallService.INSTANCE.publishCqRiders(imei, selectedProduct, CqsumAssured);
								}
							};
						};
						thread.start();
					}
				}
			}
		};
		getDatabutton.setChangeListener(listner);

		add(productLabel);
		add(productfilteredList);
		add(NameLabel);
		add(nameEditField);
		add(gender);
		add(ageHorizontalFieldManager);
		add(sumassuredHorizontalFieldManager);
		add(getDatabutton);
		int count = ShowToolbar.INSTANCE.toolBarField.getFieldCount();
		for (int i = 0; i < count; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField.getField(i);
			tab2.setActive(false);
		}
		productfilteredList.setFocus();
	}

	public boolean onClose() {
		UiApplication.getUiApplication().requestBackground();
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

	private boolean comapreArray(String selected, String[] array) {
		int arrayLength = array.length;
		for (int i = 0; i < arrayLength; i++) {
			if (selected.equalsIgnoreCase(array[i])) {
				return false;
			}
		}
		return true;
	}

	public static void clearFields() {
		productfilteredList.setText("");
		nameEditField.setText("");
		ageEdit.setText("");
		sumAssuredEdit.setText("");
	}
}
