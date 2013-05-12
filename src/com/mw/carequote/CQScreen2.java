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
import net.rim.device.api.ui.component.NumericChoiceField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.webservice.CallService;

public class CQScreen2 extends MainScreen {

	private LabelField PolicyTermLabel, SumAssuredLabel;
	private EditField PolicyTermTextField;
	private EditField SumAssuredTextField;
	private ButtonField btn;
	private String CqRider;
	private String imei, Cqname, CqAge, CqGender, CqSa, CqPremTerm, CqTerm;
	public static String CqCode;

	private int hbgColor = 0x000072BC;
	private NumericChoiceField numericChoiceField;
	private ObjectChoiceField pPTObjectChoiceField;
	private LabelField[] labelField = new LabelField[Constant.riderCount];
	private EditField[] editField = new EditField[Constant.riderCount];
	private String[] headers = new String[20];
	private String[] cqRiderProductArrayGroup;
	private String neumericChoice, objectChoice, stringAdd, supercashgainProducts;

	private String nextGpTxtFieldName = "";

	private int c = 0;
	private int m = 0;
	private int custom = 0;

	private String CqsumAssured = "";

	private HorizontalFieldManager policyHorizontalFieldManager, sumAssuredHorizontalFieldManager;

	private boolean policyFlag = false;
	private boolean superFlag = false;
	private boolean supersaverFlag = false;
	private boolean termCareFlag = false;

	private String str1 = "";

	public CQScreen2(String CqsumAssured, final String[] cqRiderProductArray, final String[] cqRiderProductArrayGroup,
			final String CqCode) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF, 0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		this.CqsumAssured = CqsumAssured;

		ShowToolbar.INSTANCE.setScreenTitle("Care Quote");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		CQScreen2.CqCode = CqCode;
		this.cqRiderProductArrayGroup = cqRiderProductArrayGroup;

		if (CqCode.equalsIgnoreCase("Cashrich Plan")) {
			policyFlag = false;
			numericChoiceField = new NumericChoiceField("Cash Back Period :", 5, 35, 1, 0) {
				public void paint(Graphics g) {
					g.drawLine(10, 50, 100, 50);
					invalidate();
					super.paint(g);
				}
			};
			numericChoiceField.setMargin(5, 10, 0, 10);

			String[] items = { "5", "10", "15", "20", "25", "30" };
			pPTObjectChoiceField = new ObjectChoiceField("Premium Paying Term :", items, 0);
			pPTObjectChoiceField.setMargin(5, 10, 0, 10);
			int objectIndex = pPTObjectChoiceField.getSelectedIndex();
			objectChoice = (String) pPTObjectChoiceField.getChoice(objectIndex);

			FieldChangeListener listener = new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					if (field instanceof ObjectChoiceField || field instanceof NumericChoiceField) {
						if (field == numericChoiceField) {

							int neumericIndex = numericChoiceField.getSelectedIndex();
							neumericChoice = (String) numericChoiceField.getChoice(neumericIndex);

							int objectIndex = pPTObjectChoiceField.getSelectedIndex();
							objectChoice = (String) pPTObjectChoiceField.getChoice(objectIndex);

							int resultadd = Integer.parseInt(neumericChoice) + Integer.parseInt(objectChoice);
							stringAdd = Integer.toString(resultadd);

							PolicyTermTextField.setText(stringAdd);

							invalidate();

						} else if (field == pPTObjectChoiceField) {

							int objectIndex = pPTObjectChoiceField.getSelectedIndex();
							objectChoice = (String) pPTObjectChoiceField.getChoice(objectIndex);

							int neumericIndex = numericChoiceField.getSelectedIndex();
							neumericChoice = (String) numericChoiceField.getChoice(neumericIndex);

							int resultadd = Integer.parseInt(neumericChoice) + Integer.parseInt(objectChoice);
							stringAdd = Integer.toString(resultadd);

							PolicyTermTextField.setText(stringAdd);

							invalidate();
						}
					}
				}
			};
			numericChoiceField.setChangeListener(listener);
			pPTObjectChoiceField.setChangeListener(listener);

		} else if (CqCode.equalsIgnoreCase("SUPER CASHGAIN SILVER") || CqCode.equalsIgnoreCase("SUPER CASHGAIN GOLD")
				|| CqCode.equalsIgnoreCase("SUPER CASHGAIN DIAMOND")
				|| CqCode.equalsIgnoreCase("SUPER CASHGAIN PLATINUM")) {

			superFlag = true;
			System.out.println("");
			numericChoiceField = new NumericChoiceField("Benifit Term :", 12, 24, 4, 0) {
				public void paint(Graphics g) {
					g.drawLine(10, 50, 100, 50);
					invalidate();
					super.paint(g);
				}
			};
			numericChoiceField.setMargin(5, 10, 0, 10);
			int neumericIndex = numericChoiceField.getSelectedIndex();
			// objectChoice = (String)
			// numericChoiceField.getChoice(neumericIndex);
			supercashgainProducts = (String) numericChoiceField.getChoice(neumericIndex);
			int x = (Integer.parseInt(supercashgainProducts)) - 5;
			String xx = String.valueOf(x);
			objectChoice = xx;

			String[] items = { "7" };
			pPTObjectChoiceField = new ObjectChoiceField("Premium Paying Term :", items, 0, NON_FOCUSABLE);
			pPTObjectChoiceField.setMargin(5, 10, 0, 10);

			FieldChangeListener listener = new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					if (field instanceof ObjectChoiceField || field instanceof NumericChoiceField) {
						if (field == numericChoiceField) {
							System.out.println("");
							int neumericIndex = numericChoiceField.getSelectedIndex();
							supercashgainProducts = (String) numericChoiceField.getChoice(neumericIndex);
							// objectChoice = p;
							int x = (Integer.parseInt(supercashgainProducts)) - 5;
							String xx = String.valueOf(x);
							objectChoice = xx;
							String[] rr = { xx };
							pPTObjectChoiceField.setChoices(rr);

							PolicyTermTextField.setText(objectChoice);
							invalidate();
						}
					}
				}
			};
			numericChoiceField.setChangeListener(listener);
			pPTObjectChoiceField.setChangeListener(listener);

		} else if (CqCode.equalsIgnoreCase("SUPER SAVER")) {
			supersaverFlag = true;
			System.out.println("");
			numericChoiceField = new NumericChoiceField("Benifit Term :", 10, 30, 1, 0) {
				public void paint(Graphics g) {
					g.drawLine(10, 50, 100, 50);
					invalidate();
					super.paint(g);
				}
			};
			numericChoiceField.setMargin(5, 10, 0, 10);
			int neumericIndex = numericChoiceField.getSelectedIndex();
			objectChoice = (String) numericChoiceField.getChoice(neumericIndex);

			String[] items = { "10" };
			pPTObjectChoiceField = new ObjectChoiceField("Premium Paying Term :", items, 0, NON_FOCUSABLE);
			pPTObjectChoiceField.setMargin(5, 10, 0, 10);

			FieldChangeListener listener = new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					if (field instanceof ObjectChoiceField || field instanceof NumericChoiceField) {
						if (field == numericChoiceField) {
							System.out.println("");
							int neumericIndex = numericChoiceField.getSelectedIndex();
							supercashgainProducts = (String) numericChoiceField.getChoice(neumericIndex);

							String[] rr = { supercashgainProducts };
							pPTObjectChoiceField.setChoices(rr);

							PolicyTermTextField.setText(supercashgainProducts);
							invalidate();
						}
					}
				}
			};
			numericChoiceField.setChangeListener(listener);
			pPTObjectChoiceField.setChangeListener(listener);
		} else if (CqCode.equalsIgnoreCase("TERM CARE - HEALTH") || CqCode.equalsIgnoreCase("TERM CARE - TOTAL")
				|| CqCode.equalsIgnoreCase("TERM CARE - PROTECT") || CqCode.equalsIgnoreCase("TERM CARE - ECONOMY")
				|| CqCode.equalsIgnoreCase("TERM CARE - SINGLE")) {
			termCareFlag = true;
			System.out.println("");
			numericChoiceField = new NumericChoiceField("Benifit Term :", 5, 40, 1, 0) {
				public void paint(Graphics g) {
					g.drawLine(10, 50, 100, 50);
					invalidate();
					super.paint(g);
				}
			};
			numericChoiceField.setMargin(5, 10, 0, 10);
			int neumericIndex = numericChoiceField.getSelectedIndex();
			objectChoice = (String) numericChoiceField.getChoice(neumericIndex);

			String[] items = { "5" };
			pPTObjectChoiceField = new ObjectChoiceField("Premium Paying Term :", items, 0, NON_FOCUSABLE);
			pPTObjectChoiceField.setMargin(5, 10, 0, 10);

			FieldChangeListener listener = new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					if (field instanceof ObjectChoiceField || field instanceof NumericChoiceField) {
						if (field == numericChoiceField) {
							System.out.println("");
							int neumericIndex = numericChoiceField.getSelectedIndex();
							supercashgainProducts = (String) numericChoiceField.getChoice(neumericIndex);

							String[] rr = { supercashgainProducts };
							pPTObjectChoiceField.setChoices(rr);

							PolicyTermTextField.setText(supercashgainProducts);
							invalidate();
						}
					}
				}
			};
			numericChoiceField.setChangeListener(listener);
			pPTObjectChoiceField.setChangeListener(listener);
		} else {
			policyFlag = true;
			numericChoiceField = new NumericChoiceField("Benifit Term :", 15, 30, 5, 0) {
				public void paint(Graphics g) {
					g.drawLine(10, 50, 100, 50);
					invalidate();
					super.paint(g);
				}
			};
			numericChoiceField.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					if (field == numericChoiceField) {
						int neumericIndex = numericChoiceField.getSelectedIndex();
						neumericChoice = (String) numericChoiceField.getChoice(neumericIndex);
						PolicyTermTextField.setText(neumericChoice);
						objectChoice = neumericChoice;
						String[] rr = { neumericChoice };
						pPTObjectChoiceField.setChoices(rr);
					}
				}
			});
			numericChoiceField.setMargin(5, 10, 0, 10);

			String[] item = { "15" };
			pPTObjectChoiceField = new ObjectChoiceField("Premium Paying Term :", item, 0, NON_FOCUSABLE);

			int objectIndex = pPTObjectChoiceField.getSelectedIndex();
			objectChoice = (String) pPTObjectChoiceField.getChoice(objectIndex);

			pPTObjectChoiceField.setMargin(5, 10, 0, 10);
		}

		policyHorizontalFieldManager = new HorizontalFieldManager() {
			public void paint(Graphics g) {
				g.setBackgroundColor(0x00B0E2FF);
				g.clear();
				invalidate();
				super.paint(g);
			}
		};
		policyHorizontalFieldManager.setMargin(5, 10, 0, 10);
		PolicyTermLabel = new LabelField("Policy Term :");
		PolicyTermTextField = new EditField(Field.FIELD_RIGHT | Field.NON_FOCUSABLE);
		if (policyFlag) {
			PolicyTermTextField.setText("" + 15);
		} else if (superFlag) {
			PolicyTermTextField.setText("7");
		} else if (supersaverFlag) {
			PolicyTermTextField.setText("10");
		} else if (termCareFlag) {
			PolicyTermTextField.setText("5");
		} else {
			PolicyTermTextField.setText("" + 10);
		}

		PolicyTermTextField.setPadding(0, 0, 0, 150);
		policyHorizontalFieldManager.add(PolicyTermLabel);
		policyHorizontalFieldManager.add(PolicyTermTextField);

		sumAssuredHorizontalFieldManager = new HorizontalFieldManager(Field.USE_ALL_WIDTH) {
			public void paint(Graphics g) {
				g.setBackgroundColor(0x00B0E2FF);
				g.clear();
				invalidate();
				super.paint(g);
			}
		};
		sumAssuredHorizontalFieldManager.setMargin(5, 10, 0, 10);
		SumAssuredLabel = new LabelField("Sum Assured :");
		SumAssuredTextField = new EditField(CqsumAssured, "", 20, Field.NON_FOCUSABLE);
		SumAssuredTextField.setPadding(0, 0, 0, 130);
		sumAssuredHorizontalFieldManager.add(SumAssuredLabel);
		sumAssuredHorizontalFieldManager.add(SumAssuredTextField);

		btn = new ButtonField("Submit", Field.FIELD_HCENTER);
		btn.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				if (field == btn) {

					imei = NewAuthenticator.INSTANCE.getIMEINumber();
					Cqname = CareQuoteDetailsScreen.nameEditField.getText();
					CqAge = CareQuoteDetailsScreen.ageEdit.getText();
					CqGender = CareQuoteDetailsScreen.selectedSex.substring(0, 1);
					CqSa = CareQuoteDetailsScreen.sumAssuredEdit.getText();
					CqPremTerm = objectChoice;
					if (superFlag == true) {
						CqTerm = supercashgainProducts;
					} else {
						CqTerm = PolicyTermTextField.getText();
					}

					getCqRiders(CqCode);
				}
			}
		});

		add(numericChoiceField);
		add(pPTObjectChoiceField);
		add(policyHorizontalFieldManager);
		add(sumAssuredHorizontalFieldManager);

		createCqRiderLabelEditScreen(CqsumAssured, cqRiderProductArray);

		add(btn);
		int counte = ShowToolbar.INSTANCE.toolBarField.getFieldCount();
		for (int i = 0; i < counte; i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField.getField(i);
			tab2.setActive(false);
		}
		numericChoiceField.setFocus();
	}

	public void createCqRiderLabelEditScreen(String CqsumAssured, String[] cqRiderProductArray) {
		int riderCount = Constant.riderCount;
		for (int i = 0; i < riderCount; i++) {
			headers[i] = cqRiderProductArray[i];
		}
		addViewsToLayout(CqsumAssured, headers, Constant.riderCount);
	}

	public void addViewsToLayout(String CqsumAssured, String[] headers, int count) {

		int i = 0;
		for (i = 0; i < count; i++) {
			labelField[i] = new LabelField("", Field.USE_ALL_WIDTH) {
				public void paint(Graphics g) {
					g.setBackgroundColor(hbgColor);
					g.setColor(0xFFFFFF);
					g.clear();
					invalidate();
					super.paint(g);
				}
			};
			labelField[i].setMargin(25, 10, 0, 10);

			final String enteredValue = CQProductListClass.val[i];
			editField[i] = new EditField(EditField.FILTER_NUMERIC) {
				public void paint(Graphics g) {
					if (getTextLength() == 0) {
						g.setColor(0x00a0a0a0);
						g.drawText("Enter value more than " + enteredValue, 0, 0);
						g.drawRect(0, 0, getWidth(), getHeight());
					}
					g.setColor(Color.BLACK);
					g.drawRect(0, 0, getWidth(), getHeight());
					invalidate();
					super.paint(g);
				}
			};
			editField[i].setMargin(1, 10, 0, 10);
			labelField[i].setText(headers[i]);

			String CqAgee = CareQuoteDetailsScreen.ageEdit.getText();

			if (i == 0) {
				editField[i] = new EditField(Field.NON_FOCUSABLE) {
					public void paint(Graphics g) {
						g.drawRect(0, 0, getWidth(), getHeight());
						invalidate();
						super.paint(g);
					}
				};
				editField[i].setMargin(1, 10, 20, 10);
				editField[i].setText(CqsumAssured);
			}
			add(labelField[i]);
			add(editField[i]);

			if ((Integer.parseInt(CqAgee) > 50 && Integer.parseInt(CqAgee) <= 60) && i != 0) {
				// labelField[i].setVisualState(VISUAL_STATE_DISABLED);
				// editField[i].setVisualState(VISUAL_STATE_DISABLED);
				delete(labelField[i]);
				delete(editField[i]);
			}
		}
	}

	public void getCqRiders(final String CqCode) {
		boolean groupCompleted = true;

		boolean groupUsed = false;
		int countee = cqRiderProductArrayGroup.length;
		for (int j = 0; ((j == -1) || (j < countee)); j++) {

			String temp = cqRiderProductArrayGroup[j].substring(0, cqRiderProductArrayGroup[j].indexOf("#"));

			if (!groupUsed) {
				// if the gp is used atleast once
				int riderCount = Constant.riderCount;
				for (int k = 0; k < riderCount; k++) {

					str1 = (String) labelField[k].getText();
					String str = editField[k].getText().toString();

					if (temp.equals(str1)) {
						if (!str.equals("")) {
							groupUsed = true;
							j = -1;
							break;
						}
					}
				}
			} else if (groupCompleted) {
				int riderCount = Constant.riderCount;
				// if the empty used-gp field is already detected
				for (int k = 0; k < riderCount; k++) {

					str1 = (String) labelField[k].getText();
					String str = editField[k].getText().toString();

					if (temp.equals(str1)) {
						if (str.equals("")) {
							if (cqRiderProductArrayGroup[j].substring(cqRiderProductArrayGroup[j].indexOf("#") + 1,
									cqRiderProductArrayGroup[j].length()).equalsIgnoreCase("0")) {
								editField[k].setText(CqSa);
								continue;
							}
							groupCompleted = false;
							// the next gp member to be filled
							nextGpTxtFieldName = temp;
							break;
						}
					}
				}
			}
		}

		String fin_val = "";

		boolean flag = false;
		boolean flagMax = false;
		int riderCount = Constant.riderCount;
		for (int i = 0; i < riderCount; i++) {

			str1 = (String) labelField[i].getText();
			String str = editField[i].getText().toString();

			if (str != null && str1 != null && !str.equalsIgnoreCase("")) {
				int a = Integer.parseInt(str);
				c = Integer.parseInt(CQProductListClass.val[i]);
				m = Integer.parseInt(CQProductListClass.valMax[i]);
				custom = Integer.parseInt(CQProductListClass.valMaxMax[i]);
				int BSA = Integer.parseInt(CqsumAssured);

				if (custom == 1) {
					m = Math.min(BSA, Integer.parseInt(CQProductListClass.valMax[i]));
				} else if (custom == 0 && m == 0) {
					m = Integer.parseInt(CqsumAssured);
				} else {

				}

				if (i == 0) {
					fin_val = fin_val.concat(str1 + "~" + str);

				} else {
					fin_val = fin_val.concat("#M#" + str1 + "~" + str);
				}

				if (a < c) {
					flag = true;
					break;
				}
				if (a > m && m != 0) {
					flagMax = true;
					break;
				} else {
					continue;
				}
			}
		}

		CqRider = fin_val;
		if (!groupCompleted) {
			if (nextGpTxtFieldName.equals("")) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						if (screen instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen);
						}
						Dialog.alert("'" + nextGpTxtFieldName + "' also need to be filled.");
					}
				});
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						if (screen instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen);
						}
						Dialog.alert("'" + nextGpTxtFieldName + "' also need to be filled.");
					}
				});
			}
		} else {
			if (flag == true) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						if (screen instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen);
						}
						Dialog.alert("Please Enter values to " + str1 + " more than " + c);
					}
				});
			} else if (flagMax == true) {

				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						if (screen instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen);
						}
						Dialog.alert("Please Enter values to " + str1 + " less than " + m);
					}
				});
			} else {
				Controller.showScreen(new PopupSpinnerScreen("Retrieving Premium details.."));

				Thread thread = new Thread() {
					public void run() {
						boolean flag = true;
						flag = CallService.INSTANCE.getCQpremium(imei, Cqname, CqAge, CqGender, CqSa, CqPremTerm,
								CqTerm, CqCode, CqRider);
						if (flag == false) {
							flag = CallService.INSTANCE.getCQpremium(imei, Cqname, CqAge, CqGender, CqSa, CqPremTerm,
									CqTerm, CqCode, CqRider);
						}
					};
				};
				thread.start();
			}
		}
	}

	public boolean onClose() {

		synchronized (UiApplication.getEventLock()) {

			Screen screen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen);
			Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
			if (screen2 instanceof PopupSpinnerScreen) {
				UiApplication.getUiApplication().popScreen(screen2);
			} else {
				UiApplication.getUiApplication().popScreen(screen2);

				Screen screen3 = UiApplication.getUiApplication().getActiveScreen();
				UiApplication.getUiApplication().popScreen(screen3);
			}
		}
		CareQuoteDetailsScreen.clearFields();
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
