package com.mw.uiscreens;

import java.util.Hashtable;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customfields.BackgrndLabelFieldImpl;
import com.mw.customfields.HeadingBackgrndLabelFieldImpl;
import com.mw.customfields.LabelFieldImpl;
import com.mw.customfields.OddBackGrndLabelImpl;
import com.mw.persist.PersistProvider;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.utils.ReplaceAll;
import com.mw.utils.ShowDialog;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class CollectionTableScreen extends MainScreen {

	private GridFieldManager grid;

	private ButtonField misbuttonFieldImpl;

	private ButtonField percentbuttonFieldImpl;

	private FieldChangeListener listener;

	private boolean buttonVisibility;

	public static CollectionTableScreen INSTANCE = new CollectionTableScreen();

	public CollectionTableScreen() {

	}

	public CollectionTableScreen(Hashtable hashtable, final Hashtable paramsHashTable) {

		super(Manager.HORIZONTAL_SCROLL | Manager.VERTICAL_SCROLL);

		showTable(hashtable, paramsHashTable);

		HorizontalFieldManager buttonHorizontalFieldManager = new HorizontalFieldManager(Field.FIELD_HCENTER);
		misbuttonFieldImpl = new ButtonField("Productwise MIS", FIELD_HCENTER | ButtonField.CONSUME_CLICK);

		percentbuttonFieldImpl = new ButtonField("Productwise %", FIELD_HCENTER | ButtonField.CONSUME_CLICK);

		listener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				ButtonField field2 = (ButtonField) field;
				String selectedLabel = field2.getLabel();
				selectedLabel = ReplaceAll.INSTANCE.replaceAll(selectedLabel, " ", "");

				if (selectedLabel.trim().equalsIgnoreCase("productwisemis")) {

					getParamsForProductMISReport(paramsHashTable);

				} else if (selectedLabel.trim().equalsIgnoreCase("productwise%")) {

					getParamsForPercentMISReport(paramsHashTable);
				}
			}
		};

		percentbuttonFieldImpl.setChangeListener(listener);
		misbuttonFieldImpl.setChangeListener(listener);

		String curDate = new SimpleDateFormat("dd-MMM-yyyy").formatLocal(System.currentTimeMillis());

		// String selectedDate = SelectRangeDummyScreen.selDate;
		String selectedDate = SelectRangeDummyScreen.displayDateLbl.getText();
		
		if(curDate.equalsIgnoreCase(selectedDate)){
			
		}
		//buttonVisibility = doCheckFutureDate(selectedDate, curDate);

//		if (buttonVisibility) {
//
//		} 
		else {
			buttonHorizontalFieldManager.add(percentbuttonFieldImpl);
			buttonHorizontalFieldManager.add(misbuttonFieldImpl);
			add(buttonHorizontalFieldManager);
		}

		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField.getField(i);
			tab2.setActive(false);
		}
	}

	public boolean doCheckFutureDate(String selectedDate, String currentDate) {

		if (selectedDate != null) {
			String[] splitSelectedDate = StringSplitter.INSTANCE.split(selectedDate, "-");
			String[] splitCurrentDate = StringSplitter.INSTANCE.split(currentDate, "-");

			int currentDay = Integer.parseInt(splitCurrentDate[0]);
			int currentMonth = Integer.parseInt(splitCurrentDate[1]);
			int currentYear = Integer.parseInt(splitCurrentDate[2]);

			int selectedDay = Integer.parseInt(splitSelectedDate[0]);
			int selectedMonth = Integer.parseInt(splitSelectedDate[1]);
			int selectedYear = Integer.parseInt(splitSelectedDate[2]);

			if (selectedYear > currentYear) {
				return true;
			} else if (selectedMonth > currentMonth && selectedYear >= currentYear) {
				return true;
			} else if (selectedDay > currentDay && selectedMonth >= currentMonth && selectedYear >= currentYear) {
				return true;
			} else if (selectedDay >= currentDay && selectedMonth >= currentMonth && selectedYear >= currentYear) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}

	}

	private void showTable(Hashtable hashtable, final Hashtable paramshashTable) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF, 0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();

		setTitle(verticalFieldManager);
		ShowToolbar.INSTANCE.setScreenTitle("MIS Tracker");

		Font myfont = Font.getDefault().derive(Font.BOLD);

		LabelFieldImpl tableTitle = new LabelFieldImpl("Collection", FIELD_HCENTER);

		tableTitle.setFont(myfont);
		tableTitle.setFontColor(0x000072BC);
		add(tableTitle);

		grid = new GridFieldManager(5, 4, FIELD_HCENTER | FIELD_VCENTER);

		HeadingBackgrndLabelFieldImpl collectionLbl = new HeadingBackgrndLabelFieldImpl("Collection    ",
				Field.NON_FOCUSABLE | USE_ALL_WIDTH);

		collectionLbl.setFont(myfont);
		grid.add(collectionLbl);

		HeadingBackgrndLabelFieldImpl ftdHeaderLbl = new HeadingBackgrndLabelFieldImpl("FTD      ", Field.NON_FOCUSABLE
				| USE_ALL_WIDTH);
		ftdHeaderLbl.setFont(myfont);
		grid.add(ftdHeaderLbl);

		HeadingBackgrndLabelFieldImpl mtdHeaderLbl = new HeadingBackgrndLabelFieldImpl("MTD      ", Field.NON_FOCUSABLE
				| USE_ALL_WIDTH);
		mtdHeaderLbl.setFont(myfont);
		grid.add(mtdHeaderLbl);

		HeadingBackgrndLabelFieldImpl ytdHeaderLbl = new HeadingBackgrndLabelFieldImpl("YTD      ", Field.NON_FOCUSABLE
				| USE_ALL_WIDTH);
		ytdHeaderLbl.setFont(myfont);
		grid.add(ytdHeaderLbl);

		grid.add(new BackgrndLabelFieldImpl("Projection    ", Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string1 = (String) hashtable.get("string1");
		grid.add(new BackgrndLabelFieldImpl(string1, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string2 = (String) hashtable.get("string2");
		grid.add(new BackgrndLabelFieldImpl(string2, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string3 = (String) hashtable.get("string3");
		grid.add(new BackgrndLabelFieldImpl(string3, Field.NON_FOCUSABLE | USE_ALL_WIDTH));

		grid.add(new OddBackGrndLabelImpl("Acheived      ", Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string4 = (String) hashtable.get("string4");
		grid.add(new OddBackGrndLabelImpl(string4, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string5 = (String) hashtable.get("string5");
		grid.add(new OddBackGrndLabelImpl(string5, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string6 = (String) hashtable.get("string6");
		grid.add(new OddBackGrndLabelImpl(string6, Field.NON_FOCUSABLE | USE_ALL_WIDTH));

		grid.add(new BackgrndLabelFieldImpl("% Achieved", Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string7 = (String) hashtable.get("string7");
		grid.add(new BackgrndLabelFieldImpl(string7, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string8 = (String) hashtable.get("string8");
		grid.add(new BackgrndLabelFieldImpl(string8, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string9 = (String) hashtable.get("string9");
		grid.add(new BackgrndLabelFieldImpl(string9, Field.NON_FOCUSABLE | USE_ALL_WIDTH));

		grid.add(new OddBackGrndLabelImpl("NOP Achieved", Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string10 = (String) hashtable.get("string10");
		grid.add(new OddBackGrndLabelImpl(string10, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string11 = (String) hashtable.get("string11");
		grid.add(new OddBackGrndLabelImpl(string11, Field.NON_FOCUSABLE | USE_ALL_WIDTH));
		String string12 = (String) hashtable.get("string12");
		grid.add(new OddBackGrndLabelImpl(string12, Field.NON_FOCUSABLE | USE_ALL_WIDTH));

		add(grid);
		add(new LabelField());
	}

	private void getParamsForProductMISReport(Hashtable paramshashTable) {

		final String channelIndex = PersistProvider.INSTANCE.getObject(Constant.PERSISTCHANNELINDEX);
		final String selecteddate = PersistProvider.INSTANCE.getObject(Constant.PERSISTDATE);

		if (channelIndex != null && selecteddate != null) {
			Controller.showScreen(new PopupSpinnerScreen("Retrieving ProductWise MIS.."));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;
					flag = CallService.INSTANCE.GetCollReport(channelIndex, selecteddate, Constant.PRODUCTREPORT, "");
					while (flag == false) {
						flag = CallService.INSTANCE.GetCollReport(channelIndex, selecteddate, Constant.PRODUCTREPORT,
								"");
					}
				}
			};
			thread.start();
		}
	}

	public void manageProductMISReport(Object object) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			String tempchecksuccess = (String) hashtable.get("perrormessageOut");

			if (tempchecksuccess != null) {
				//String checkcheck = tempchecksuccess.toLowerCase();

				if (tempchecksuccess.equals("SUCCESS")) {
					ProductwiseMISScreen productwiseMISScreen = new ProductwiseMISScreen(hashtable);

					Controller.showScreen(productwiseMISScreen);
				} else {
					ShowDialog.INSTANCE.show("MIS " + Constant.PRODUCTREPORT
							+ " is not available. Check for Success Response: " + tempchecksuccess);
				}
			} else {
				ShowDialog.INSTANCE.show("MIS " + Constant.PRODUCTREPORT
						+ " is not available. Check for Success Response: " + tempchecksuccess);
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch "
											+ Constant.PRODUCTREPORT
											+ ". Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}

	}

	private void getParamsForPercentMISReport(Hashtable paramshashTable) {

		final String channelIndex = PersistProvider.INSTANCE.getObject(Constant.PERSISTCHANNELINDEX);
		final String selecteddate = PersistProvider.INSTANCE.getObject(Constant.PERSISTDATE);

		if (channelIndex != null && selecteddate != null) {
			Controller.showScreen(new PopupSpinnerScreen("Retrieving ProductWise % MIS.."));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;
					flag = CallService.INSTANCE.GetCollReport(channelIndex, selecteddate,
							Constant.PRODUCTPERCENTAGEREPORT, "");
					while (flag == false) {
						flag = CallService.INSTANCE.GetCollReport(channelIndex, selecteddate,
								Constant.PRODUCTPERCENTAGEREPORT, "");
					}
				}
			};
			thread.start();
		}
	}

	public void managePercentReport(Object object) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			String tempchecksuccess = (String) hashtable.get("perrormessageOut");

			if (tempchecksuccess != null) {
				//String checkcheck = tempchecksuccess.toLowerCase();

				if (tempchecksuccess.equals("SUCCESS")) {
					ProductwisePercentScreen productwisePercentScreen = new ProductwisePercentScreen(hashtable);

					Controller.showScreen(productwisePercentScreen);
				} else {
					ShowDialog.INSTANCE.show("MIS " + Constant.PRODUCTPERCENTAGEREPORT
							+ " is not available. Check for Success Response: " + tempchecksuccess);
				}
			} else {
				ShowDialog.INSTANCE.show("MIS " + Constant.PRODUCTPERCENTAGEREPORT
						+ " is not available. Check for Success Response: " + tempchecksuccess);
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch "
											+ Constant.PRODUCTPERCENTAGEREPORT
											+ ". Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}

	}

	public boolean onClose() {

		synchronized (UiApplication.getEventLock()) {
			Screen screen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen);
			Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen2);

		}
		return true;
	}

	protected boolean onSavePrompt() {
		return true;
	}

	protected void onExposed() {

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager.getField(1);
		for (int i = 0; i < toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField.getField(i);
			tab2.setActive(false);
		}
	}
}
