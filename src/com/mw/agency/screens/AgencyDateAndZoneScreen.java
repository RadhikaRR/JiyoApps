package com.mw.agency.screens;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.picker.DateTimePicker;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customfields.LabelFieldImpl;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.utils.ReplaceAll;
import com.mw.utils.ShowDialog;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class AgencyDateAndZoneScreen extends MainScreen {

	private VerticalFieldManager verticalFieldManager;
	private boolean isFutureDate;
	private ButtonField get;
	private LabelFieldImpl datelabel;

	private ButtonField getDateButton;
	private ObjectChoiceField zone1ChoiceButton;

	private FieldChangeListener listener;
	private FieldChangeListener zonelistener;

	private Date date;
	private Calendar cal;
	private String zoneString;
	private LabelField displayDateLbl;

	private ButtonField getAgencyTableButton;

	public static AgencyDateAndZoneScreen INSTANCE = new AgencyDateAndZoneScreen();
	public String selectedChoiceText;
	public String selectedDate;

	public AgencyDateAndZoneScreen() {

	}

	public AgencyDateAndZoneScreen(String[] zoneArray) {

		showDateAndChannelFields(zoneArray);
	}

	private void showDateAndChannelFields(String[] zoneArray) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("MobZone MIS");
		verticalFieldManager = ShowToolbar.INSTANCE.show();

		setTitle(verticalFieldManager);

		datelabel = new LabelFieldImpl("Date ", FIELD_LEFT);
		getDateButton = new ButtonField("Calender", FIELD_RIGHT
				| ButtonField.CONSUME_CLICK);

		displayDateLbl = new LabelField(Constant.DATELABELVALUE, FIELD_LEFT);
		String title = Constant.CHOICETITLE;
		if (zoneArray != null && title != null) {

			zone1ChoiceButton = new ObjectChoiceField(title, zoneArray, 0,
					FIELD_LEFT);
		}
		get = new ButtonField(Constant.GETBUTTONLABEL, FIELD_LEFT
				| ButtonField.CONSUME_CLICK);

		getAgencyTableButton = new ButtonField("  MIS  ", FIELD_HCENTER
				| ButtonField.CONSUME_CLICK);

		zonelistener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				ButtonField field2 = (ButtonField) field;
				if (field2 == get) {

					int index = zone1ChoiceButton.getSelectedIndex();
					String tempzoneString = (String) zone1ChoiceButton
							.getChoice(index);
					zoneString = tempzoneString;
					String title = zone1ChoiceButton.getLabel();
					title = ReplaceAll.INSTANCE.replaceAll(title, " ", "");

					if (Constant.screenSwitchCounter == 3) {
						Controller.showScreen(new PopupSpinnerScreen(
								"Retrieving Region List..."));
						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;
								flag = CallService.INSTANCE.getRegionList(zoneString);
									while(flag == false){
										flag = CallService.INSTANCE.getRegionList(zoneString);
									}
							};
						};
						thread.start();

						Constant.CHOICETITLE = "Select Region";
						Constant.GETBUTTONLABEL = "Division";
						Constant.DATELABELVALUE = displayDateLbl.getText();
						Constant.screenSwitchCounter -= 1;
						System.out.println("Constant.screenSwitchCounter:"
								+ Constant.screenSwitchCounter);

					} else if (Constant.screenSwitchCounter == 2) {
						Controller.showScreen(new PopupSpinnerScreen(
								"Retrieving Division List..."));
						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;
								flag = CallService.INSTANCE.getDivisionList(zoneString);
								while(flag == false){
									flag = CallService.INSTANCE.getDivisionList(zoneString);
								}
							};
						};
						thread.start();

						Constant.CHOICETITLE = "Select Division";
						Constant.GETBUTTONLABEL = "Unitcode";
						Constant.DATELABELVALUE = displayDateLbl.getText();
						Constant.screenSwitchCounter -= 1;
						System.out.println("Constant.screenSwitchCounter:"
								+ Constant.screenSwitchCounter);

					} else if (Constant.screenSwitchCounter == 1) {
						Controller.showScreen(new PopupSpinnerScreen(
								"Retrieving Unitcode List..."));
						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;								
								flag = CallService.INSTANCE.getUnitcodeList(zoneString);
								while(flag == false){
									flag = CallService.INSTANCE.getUnitcodeList(zoneString);
								}
							};
						};
						thread.start();

						Constant.CHOICETITLE = "Select Unitcode";
						Constant.DATELABELVALUE = displayDateLbl.getText();
						Constant.screenSwitchCounter -= 1;
						System.out.println("Constant.screenSwitchCounter:"
								+ Constant.screenSwitchCounter);
					} else if (Constant.screenSwitchCounter == 0) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Screen screen = UiApplication.getUiApplication()
										.getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Dialog.alert("Please click MIS button.");
							}
						});						
					}
				}
			}
		};

		listener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				ButtonField field2 = (ButtonField) field;
				String selectedLabel = field2.getLabel();
				selectedLabel = ReplaceAll.INSTANCE.replaceAll(selectedLabel,
						" ", "");

				if (selectedLabel.trim().toLowerCase().equalsIgnoreCase(
						"calender")) {
					showDatePicker();

				} else if (selectedLabel.trim().toLowerCase().equalsIgnoreCase(
						"mis")) {

					selectedDate = displayDateLbl.getText().toUpperCase();

					int selectedIndex = zone1ChoiceButton.getSelectedIndex();
					selectedChoiceText = (String) zone1ChoiceButton
							.getChoice(selectedIndex);

					if (selectedDate != null && selectedChoiceText != null) {

						Controller.showScreen(new PopupSpinnerScreen(
								"Retrieving MIS Data.."));
						Thread thread = new Thread() {
							public void run() {
								getWebServiceNewAgencyMethodParams(
										selectedChoiceText, selectedDate);
							};
						};
						thread.start();
					}
				}
			}
		};

		getDateButton.setChangeListener(listener);
		getAgencyTableButton.setChangeListener(listener);

		get.setChangeListener(zonelistener);

		add(new LabelField());
		add(new LabelField());
		add(new DateContainer());

		add(new LabelField());
		add(new ZoneContainer());

		add(new LabelField());

		add(getAgencyTableButton);
		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField
					.getField(i);
			tab2.setActive(false);
		}
		getAgencyTableButton.setFocus();
	}

	private void showDatePicker() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {

				final DateTimePicker dateTimePicker = DateTimePicker
						.createInstance(Calendar.getInstance(), "yyyy:MM:dd",
								null);
				dateTimePicker.doModal();

				cal = dateTimePicker.getDateTime();

				date = cal.getTime();
				if (date != null) {

					String selDate = new SimpleDateFormat("dd-MM-yyyy")
							.format(new Date(date.getTime()));

					String curDate = new SimpleDateFormat("dd-MM-yyyy")
							.formatLocal(System.currentTimeMillis());

					isFutureDate = doCheckFutureDate(selDate, curDate);
					if (isFutureDate) {
						displayDateLbl.setText(Constant.DATELABELVALUE);
						Dialog.alert("Future date is not allowed.");
					} else {
						String validDateForlbl = new SimpleDateFormat(
								"dd-MMM-yyyy").format(new Date(date.getTime()));
						displayDateLbl.setText(validDateForlbl);
					}
				}
			}
		});
	}

	public boolean doCheckFutureDate(String selectedDate, String currentDate) {
		String[] splitSelectedDate = StringSplitter.INSTANCE.split(
				selectedDate, "-");
		String[] splitCurrentDate = StringSplitter.INSTANCE.split(currentDate,
				"-");

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
		} else if (selectedDay > currentDay && selectedMonth >= currentMonth
				&& selectedYear >= currentYear) {
			return true;
		} else {
			return false;
		}
	}

	public boolean onClose() {

		Constant.screenSwitchCounter += 1;
		synchronized (UiApplication.getEventLock()) {
			Screen screen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen);
			Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(screen2);
		}
		return true;
	}

	protected void onExposed() {

		verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager
				.getField(1);
		for (int i = 0; i < toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField
					.getField(i);
			tab2.setActive(false);
		}
		getAgencyTableButton.setFocus();
	}

	public void getWebServiceNewAgencyMethodParams(
			final String selectedChoiceText, String selectedDate) {

		String title = zone1ChoiceButton.getLabel();
		title = ReplaceAll.INSTANCE.replaceAll(title, " ", "");

		boolean flag = true;
		if (Constant.screenSwitchCounter == 3) {			
			flag = CallService.INSTANCE.showNewAgencyTable(selectedChoiceText, "", "","", selectedDate);
			while(flag == false){
				flag = CallService.INSTANCE.showNewAgencyTable(selectedChoiceText, "", "","", selectedDate);
			}
		} else if (Constant.screenSwitchCounter == 2) {
			flag = CallService.INSTANCE.showNewAgencyTable("", selectedChoiceText, "","", selectedDate);
			while(flag == false){
				flag = CallService.INSTANCE.showNewAgencyTable("", selectedChoiceText, "","", selectedDate);
			}
		} else if (Constant.screenSwitchCounter == 1) {
			flag = CallService.INSTANCE.showNewAgencyTable("", "", selectedChoiceText,"", selectedDate);
			while(flag == false){
				flag = CallService.INSTANCE.showNewAgencyTable("", "", selectedChoiceText,"", selectedDate);
			}
		} else if (Constant.screenSwitchCounter == 0) {
			flag = CallService.INSTANCE.showNewAgencyTable("", "", "",selectedChoiceText, selectedDate);
			while(flag == false){
				flag = CallService.INSTANCE.showNewAgencyTable("", "", "",selectedChoiceText, selectedDate);
			}
		}
	}

	public void manageWebServiceObject(Object object) {

		if (object != null) {

			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE
					.converObjectToHashTable(response);

			if (hashtable != null) {
				String tempchecksuccess = (String) hashtable.get("string80");

				if (tempchecksuccess != null) {
					//String checkcheck = tempchecksuccess.trim().toLowerCase();

					if (tempchecksuccess.equals("SUCCESS")) {
						Controller.showScreen(new RestAllManagerTableScreen(
								hashtable));
					} else {
						ShowDialog.INSTANCE
								.show("MIS Report is not available. Check for Success Response "
										+ tempchecksuccess);
					}
				} else {
					ShowDialog.INSTANCE
							.show("MIS Report is not available. Check for Success Response "
									+ tempchecksuccess);
				}
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Status
								.show(
										"Failed to fetch MIS Data. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
										3000);
					}
				});
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch MIS Data. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	protected boolean onSave() {
		return true;
	}

	class DateContainer extends Manager {

		public DateContainer() {
			super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | USE_ALL_WIDTH);
			add(datelabel);
			add(displayDateLbl);
			add(getDateButton);
		}

		protected void sublayout(int width, int height) {

			if (width > Display.getWidth()) {
				width = Display.getWidth();
			}
			layoutChild(datelabel, width, height);
			layoutChild(displayDateLbl, width, height);
			layoutChild(getDateButton, width, height);

			int dateLAbelWidth = datelabel.getWidth();
			int dateLabelHeight = datelabel.getHeight();

			int maxHeight = dateLabelHeight;

			int displayLAbelWidth = displayDateLbl.getWidth();
			int displayLabelHeight = displayDateLbl.getHeight();

			if (displayLabelHeight > maxHeight) {
				maxHeight = displayLabelHeight;
			}
			int buttonWidth = getDateButton.getWidth();
			int buttonHeight = getDateButton.getHeight();

			if (buttonHeight > maxHeight) {
				maxHeight = buttonHeight;
			}

			setPositionChild(datelabel, 0, 0);
			setPositionChild(getDateButton, width - buttonWidth, 0);

			int spaceAvailable = width - (dateLAbelWidth + buttonWidth);
			int labX = dateLAbelWidth + (spaceAvailable - displayLAbelWidth)
					/ 2;

			setPositionChild(displayDateLbl, labX, 0);

			setExtent(width, maxHeight);
		}
	}

	class ZoneContainer extends Manager {

		public ZoneContainer() {
			super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | USE_ALL_WIDTH);

			add(zone1ChoiceButton);
			add(get);
		}

		protected void sublayout(int width, int height) {

			if (width > Display.getWidth()) {
				width = Display.getWidth();
			}
			layoutChild(zone1ChoiceButton, width, height);
			layoutChild(get, width, height);

			int dateLabelHeight = zone1ChoiceButton.getHeight();

			int maxHeight = dateLabelHeight;

			int displayLAbelWidth = get.getWidth();
			int displayLabelHeight = get.getHeight();

			if (displayLabelHeight > maxHeight) {
				maxHeight = displayLabelHeight;
			}

			setPositionChild(zone1ChoiceButton, 0, 0);
			setPositionChild(get, width - displayLAbelWidth, 0);

			setExtent(width, maxHeight);
		}
	}
}
