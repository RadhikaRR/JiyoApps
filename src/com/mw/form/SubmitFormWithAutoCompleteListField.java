package com.mw.form;

import java.util.Hashtable;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.agency.screens.AuthenticationResponsePopUpScreen;
import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customfields.FilteredList;
import com.mw.customfields.LabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.webservice.CallService;

public class SubmitFormWithAutoCompleteListField extends MainScreen {

	private LabelFieldImpl smNameLbl;
	private EditField smNameText;

	private LabelFieldImpl premiumLbl;
	private EditField premiumText;

	private LabelFieldImpl productLbl;

	private LabelFieldImpl icCodeLbl;

	private LabelFieldImpl mobileNoLbl;
	private EditField mobileNoText;

	private ButtonField submitButton;
	private FieldChangeListener fieldChangeListener;

	private String imei;
	private String Cname;
	private String primium;

	private String mobile;
	private String respMesg;
	private EditField iccodeEditField;
	private FilteredList iccodefilteredList;
	private FilteredList productfilteredList;

	private String selectedICCode = null;
	private String selectedProduct = null;

	public static SubmitFormWithAutoCompleteListField INSTANCE = new SubmitFormWithAutoCompleteListField();

	public SubmitFormWithAutoCompleteListField() {
		// TODO Auto-generated constructor stub
	}

	public SubmitFormWithAutoCompleteListField(final String[] icCodesArray,
			final String[] productArray) {		

		Constant.oldICCodeArray = icCodesArray;
		Constant.oldProductArray = productArray;

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("Visit Info");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();

		setTitle(verticalFieldManager);

		smNameLbl = new LabelFieldImpl("Customer Name:", FIELD_LEFT
				| USE_ALL_WIDTH);
		smNameLbl.setMargin(0, 10, 0, 10);
		smNameText = new EditField("", "", 500, FIELD_HCENTER) {

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
		smNameText.setMargin(0, 10, 0, 10);

		premiumLbl = new LabelFieldImpl("Premium :", FIELD_LEFT | USE_ALL_WIDTH);
		premiumLbl.setMargin(0, 10, 0, 10);
		premiumText = new EditField("", "", 8, FIELD_HCENTER
				| EditField.FILTER_NUMERIC) {

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
		premiumText.setMargin(0, 10, 0, 10);

		productLbl = new LabelFieldImpl("Product :", FIELD_LEFT | USE_ALL_WIDTH);
		productLbl.setMargin(0, 10, 0, 10);

		productfilteredList = new FilteredList(productArray, FIELD_LEFT);
		
		productfilteredList.setMargin(0, 10, 0, 10);

		icCodeLbl = new LabelFieldImpl("IC Code :", FIELD_LEFT);
		icCodeLbl.setMargin(0, 10, 0, 10);

		if (icCodesArray != null) {
			iccodefilteredList = new FilteredList(icCodesArray, USE_ALL_WIDTH| EditField.FILTER_NUMERIC);
			
			iccodefilteredList.setSearchIndex(4);
			iccodefilteredList.setMargin(0, 10, 0, 10);
		} else {
			iccodeEditField = new EditField("", "", 50, FIELD_HCENTER
					| EditField.FILTER_NUMERIC) {

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
			iccodeEditField.setMargin(0, 10, 0, 10);
		}

		mobileNoLbl = new LabelFieldImpl("Mobile No :", FIELD_LEFT
				| USE_ALL_WIDTH);
		mobileNoLbl.setMargin(0, 10, 0, 10);
		mobileNoText = new EditField("", "", 10, FIELD_HCENTER
				| EditField.FILTER_NUMERIC) {

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
		mobileNoText.setMargin(0, 10, 0, 10);

		submitButton = new ButtonField("Submit", FIELD_HCENTER);

		fieldChangeListener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {

				ButtonField field2 = (ButtonField) field;
				if (field2 == submitButton) {
					imei = NewAuthenticator.INSTANCE.getIMEINumber();

					Cname = smNameText.getText();
					primium = premiumText.getText();
					if (icCodesArray != null) {
						selectedICCode = iccodefilteredList.getText();
					} else {
						selectedICCode = iccodeEditField.getText();
					}

					selectedProduct = productfilteredList.getText();

					mobile = mobileNoText.getText();

					boolean isproductMatchedFound = comapreArray(
							selectedProduct, productArray);
					boolean isicCodeMatchedFound = false;
					if (icCodesArray != null) {
						isicCodeMatchedFound = comapreArray(selectedICCode,
								icCodesArray);
					}

					if (Cname.equalsIgnoreCase("")) {
						showMessageDialog("Please enter valid Customer Name.");
						smNameText.setFocus();

					} else if (primium.equalsIgnoreCase("")) {
						showMessageDialog("Please enter valid Premium.");
						premiumText.setFocus();
					} else if (isproductMatchedFound) {
						showMessageDialog("Please select Product from list.");

						productfilteredList.setFocus();
						clearToolBarFocus();
						productfilteredList.setText("");
					} else if (isicCodeMatchedFound && icCodesArray != null) {
						showMessageDialog("Please select IC Code from list.");

						iccodefilteredList.setFocus();
						clearToolBarFocus();
						iccodefilteredList.setText("");
					} else if (icCodesArray == null
							&& selectedICCode.equalsIgnoreCase("")) {
						showMessageDialog("Please enter valid IC Code.");
						iccodeEditField.setFocus();
					} else if (mobile.length() < 10
							|| !((mobile.startsWith("9"))
									|| (mobile.startsWith("8")) || (mobile
									.startsWith("7")))) {
						showMessageDialog("Please enter valid 10 digit Mobile No..");
						mobileNoText.setFocus();
					} else {
						if (imei != null) {
							Controller.showScreen(new PopupSpinnerScreen(
									"Submitting details.."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;
									flag = CallService.INSTANCE.sumitFormData(imei,
											Cname, primium, selectedProduct,
											selectedICCode, mobile);
									if(flag == false){
									flag = CallService.INSTANCE.sumitFormData(imei,
											Cname, primium, selectedProduct,
											selectedICCode, mobile);
									}
								};
							};
							thread.start();
						} else {
							showMessageDialog("Failed to fetch IMEI for submitting visit details.");
						}
					}
				}
			}
		};
		submitButton.setChangeListener(fieldChangeListener);
		add(smNameLbl);
		add(smNameText);

		add(premiumLbl);
		add(premiumText);

		add(productLbl);

		add(productfilteredList);

		add(icCodeLbl);
		if (icCodesArray != null) {
			add(iccodefilteredList);
		} else {
			add(iccodeEditField);
		}

		add(mobileNoLbl);
		add(mobileNoText);
		add(submitButton);

		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField
					.getField(i);
			tab2.setActive(false);
		}
		smNameText.setFocus();
	}

	public void submitDataToWebService(Object formObject) {

		if (formObject != null) {
			Hashtable formHashTable = manageObject(formObject);
			if (formHashTable != null) {
				respMesg = (String) formHashTable.get("pmessageOut");

				if (respMesg.trim().equalsIgnoreCase("USER-IS-NOT-ENABLED")
						|| respMesg.trim().equalsIgnoreCase("USER-IS-DISABLED")
						|| respMesg.trim().equalsIgnoreCase(
								"HANDSET-IS-DISABLED")) {

					synchronized (UiApplication.getEventLock()) {

						Screen screen2 = UiApplication.getUiApplication()
								.getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen2);
						Controller
								.showScreen(new AuthenticationResponsePopUpScreen(
										"Visit Info Submit Response :"
												+ respMesg));
					}
				} else {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									if (respMesg == null
											|| respMesg
													.equalsIgnoreCase("null")) {
										Controller
												.showScreen(new VisitInfoResponsePopUpScreen(
														"Visit Info Response :"
																+ respMesg));
									} else {
										Controller
												.showScreen(new VisitInfoResponsePopUpScreen(
														respMesg));
									}
								}
							});
				}

			}
		} else {
			UiApplication.getUiApplication().invokeLater(
					new Runnable() {
						public void run() {
							Status
									.show("Failed to Submit Visit information. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",3000);
						}
					});
		}
	}

	private Hashtable manageObject(Object formObject2) {

		String response = formObject2.toString();

		Hashtable hashtable = NewAuthenticator.INSTANCE
				.converObjectToHashTable(response);
		return hashtable;
	}

	protected void onExposed() {
	}

	public boolean onClose() {
		UiApplication.getUiApplication().requestBackground();
		return true;
	}

	protected boolean onSavePrompt() {
		return true;
	}

	private boolean comapreArray(String selected, String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (selected.equalsIgnoreCase(array[i])) {
				return false;
			}
		}
		return true;
	}

	private void showMessageDialog(final String message) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog.alert(message);
			}
		});
	}

	private void clearToolBarFocus() {
		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField
					.getField(i);
			tab2.setActive(false);
		}
	}
}
