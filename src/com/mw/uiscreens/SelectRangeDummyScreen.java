package com.mw.uiscreens;

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
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.picker.DateTimePicker;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.NewAuthenticator;
import com.mw.customfields.LabelFieldImpl;
import com.mw.persist.PersistProvider;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.utils.ReplaceAll;
import com.mw.utils.ShowDialog;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class SelectRangeDummyScreen extends MainScreen {

	private LabelFieldImpl datelabel;
	private ButtonField getDateButton;
	private ObjectChoiceField getChannelButton;
	private FieldChangeListener listener;

	private Date date;
	private Calendar cal;

	public static String labelDateForWebService;
	private Hashtable paramshashTable;
	private ButtonField getData;
	private String curDateForDisplayLbl;
	public static LabelField displayDateLbl;
	private HorizontalFieldManager horizontalFieldManager2;
	private boolean isFutureDate;
	public static String selDate;
	private static String[] channelVal;
	private static String[] channelListName;

	public static SelectRangeDummyScreen INSTANCE = new SelectRangeDummyScreen(channelVal, channelListName);

	public SelectRangeDummyScreen(String[] channelVal, String[] channelListName) {

		Constant.oldChannelListName = channelListName;
		Constant.oldchannelVal = channelVal;

		SelectRangeDummyScreen.channelVal = channelVal;
		SelectRangeDummyScreen.channelListName = channelListName;
		paramshashTable = new Hashtable();

		showDateAndChannelFields();
	}

	private void showDateAndChannelFields() {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF, 0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("MIS Tracker");
		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();

		setTitle(verticalFieldManager);

		datelabel = new LabelFieldImpl("Date   ", FIELD_LEFT);
		getDateButton = new ButtonField("Calender", FIELD_RIGHT | ButtonField.CONSUME_CLICK);

		curDateForDisplayLbl = new SimpleDateFormat("dd-MMM-yyyy").formatLocal(System.currentTimeMillis());
		displayDateLbl = new LabelField(curDateForDisplayLbl, FIELD_LEFT);

		getChannelButton = new ObjectChoiceField("Select Channel ", channelListName, 0);

		getData = new ButtonField("Get Data", FIELD_HCENTER | ButtonField.CONSUME_CLICK);

		listener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				ButtonField field2 = (ButtonField) field;
				String selectedLabel = field2.getLabel();
				selectedLabel = ReplaceAll.INSTANCE.replaceAll(selectedLabel, " ", "");
				if (selectedLabel.trim().toLowerCase().equalsIgnoreCase("calender")) {
					showDatePicker();
				} else if (selectedLabel.trim().toLowerCase().equalsIgnoreCase("getdata")) {
					getWebServiceMethodParams();
				}
			}
		};

		getDateButton.setChangeListener(listener);
		getData.setChangeListener(listener);

		add(new LabelField());
		add(datelabel);
		add(new layoutComponentManager());
		add(getChannelButton);
		add(new LabelField());

		horizontalFieldManager2 = new HorizontalFieldManager(FIELD_HCENTER);
		horizontalFieldManager2.add(getData);

		add(horizontalFieldManager2);

		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField.getField(i);
			tab2.setActive(false);
		}
		getData.setFocus();
	}

	private void showDatePicker() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {

				final DateTimePicker dateTimePicker = DateTimePicker.createInstance(Calendar.getInstance(),
						"yyyy:MM:dd", null);
				dateTimePicker.doModal();

				cal = dateTimePicker.getDateTime();

				date = cal.getTime();
				if (date != null) {

					selDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date(date.getTime()));

					String curDate = new SimpleDateFormat("dd-MM-yyyy").formatLocal(System.currentTimeMillis());
					
					//curDateForDisplayLbl = new SimpleDateFormat("dd-MMM-yyyy").formatLocal(System.currentTimeMillis());

					isFutureDate = doCheckFutureDate(selDate, curDate);
					if (isFutureDate) {
//						displayDateLbl.setText(Constant.DATELABELVALUE);
						displayDateLbl.setText(curDateForDisplayLbl);
						Dialog.alert("Future date is not allowed.");
					} else {
						String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy").format(new Date(date.getTime()));
						displayDateLbl.setText(validDateForlbl);
					}
				}
			}
		});
	}

	public boolean doCheckFutureDate(String selectedDate, String currentDate) {
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
		} else {
			return false;
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
		for (int i = 0; i < toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField.getField(i);
			tab2.setActive(false);
		}
	}

	public void getWebServiceMethodParams() {

		int index = getChannelButton.getSelectedIndex();
		int newIndex = Integer.parseInt(channelVal[index]);

		final String valueOfChannel = Integer.toString(newIndex);

		setOldChoiceChannel(valueOfChannel);
		paramshashTable.put("ChannelID", valueOfChannel);
		PersistProvider.INSTANCE.saveObject(Constant.PERSISTCHANNELINDEX, valueOfChannel);

		labelDateForWebService = displayDateLbl.getText().toUpperCase();

		if (labelDateForWebService != null && valueOfChannel != null) {

			setOlddate(labelDateForWebService);
			paramshashTable.put("SelectedDate", labelDateForWebService);
			PersistProvider.INSTANCE.saveObject(Constant.PERSISTDATE, labelDateForWebService);
			Controller.showScreen(new PopupSpinnerScreen("Retrieving Collection MIS.."));
			Thread thread = new Thread() {
				public void run() {
					boolean flag = true;
					flag = CallService.INSTANCE.GetCollReport(valueOfChannel, labelDateForWebService,
							Constant.COLLECTIONREPORT, "");
					while (flag == false) {
						flag = CallService.INSTANCE.GetCollReport(valueOfChannel, labelDateForWebService,
								Constant.COLLECTIONREPORT, "");
					}
				}
			};
			thread.start();
		}
	}

	public void manageObject(Object object) {

		if (object != null) {
			String response = object.toString();

			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			String tempchecksuccess = (String) hashtable.get("perrormessageOut");

			if (tempchecksuccess != null) {
				String checkcheck = tempchecksuccess.toLowerCase();

				if (checkcheck.equalsIgnoreCase("success")) {

					CollectionTableScreen collectionTableScreen = new CollectionTableScreen(hashtable, paramshashTable);
					Controller.showScreen(collectionTableScreen);
				} else {
					ShowDialog.INSTANCE.show("MIS " + Constant.COLLECTIONREPORT
							+ " is not available. Check for Success Response: " + tempchecksuccess);
				}
			} else {
				ShowDialog.INSTANCE.show("MIS " + Constant.COLLECTIONREPORT
						+ " is not available. Check for Success Response: " + tempchecksuccess);
			}

		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch "
											+ Constant.COLLECTIONREPORT
											+ ". Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	public void setOldChoiceChannel(String StringchoiceText) {
		PersistProvider.INSTANCE.saveObject(Constant.OLDCHANNEL, StringchoiceText);
	}

	public void getOldChoiceChannel() {
		String channelPersist = PersistProvider.INSTANCE.getObject(Constant.OLDCHANNEL);

		getChannelButton.setSelectedIndex(channelPersist);
	}

	private void setOlddate(String persisstDate) {
		PersistProvider.INSTANCE.saveObject(Constant.OLDDATE, persisstDate);
	}

	public void getOlddate() {
		String olddateFordisplay = PersistProvider.INSTANCE.getObject(Constant.OLDDATE);
		displayDateLbl.setText(olddateFordisplay);
	}

	public int getChannelSortedIndex(String selectedChoiceText) {

		int selectedSortedIntIndex = 0;

		return selectedSortedIntIndex;
	}

	class layoutComponentManager extends Manager {

		public layoutComponentManager() {
			super(USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);

			add(displayDateLbl);
			add(getDateButton);
		}

		protected void sublayout(int width, int height) {

			if (width > Display.getWidth()) {
				width = Display.getWidth();
			}

			layoutChild(displayDateLbl, width, height);
			layoutChild(getDateButton, width, height);

			int displayDateLblHeight = displayDateLbl.getHeight();

			int maxHeight = displayDateLblHeight;

			int displayDateLabelWidth = displayDateLbl.getWidth();
			int displayDateLabelWidthLabelHeight = displayDateLbl.getHeight();

			if (displayDateLabelWidthLabelHeight > maxHeight) {
				maxHeight = displayDateLabelWidthLabelHeight;
			}

			setPositionChild(displayDateLbl, 0, 0);
			setPositionChild(getDateButton, (width - displayDateLabelWidth), 0);

			setExtent(width, getDateButton.getHeight());
		}
	}
}
