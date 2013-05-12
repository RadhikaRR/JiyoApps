package com.mw.uiscreens;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.messagelist.ApplicationIndicator;
import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.constants.Constant;
import com.mw.control.LogEventClass;
import com.mw.customfields.LabelFieldImpl;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.utils.StringSplitter;

public class EntryScreen2 extends MainScreen {

	private VerticalFieldManager _vfm;

	private ListField _lf;

	private LabelFieldImpl WelcomeNameLbl;
	private LabelFieldImpl DisplayTimeLbl;

	private Manager toolBarFieldManager;
	private HorizontalFieldManager horizontalFieldManager2;
	public Hashtable oldHashTable;
	private String userName;

	private String selectedFullNews;
	public static String[] listItemArray;
	
	private Font fontlb0;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	

	public EntryScreen2(Hashtable hashtable2) {		

		showEnterPhoneField(hashtable2);

		_vfm = new VerticalFieldManager();

		String strString = (String) hashtable2.get("stringval3");

		if (strString != null
				&& !strString.equalsIgnoreCase("null")) {			

			listItemArray = StringSplitter.INSTANCE.split(strString, "~");

			_lf = new ListField() {
				protected boolean navigationClick(int status, int time) {
					int selectedNewsIndex = _lf.getSelectedIndex();
					selectedFullNews = listItemArray[selectedNewsIndex];						
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {
									public void run() {
										Dialog.alert(selectedFullNews);											
										showIndicator(false);
									}
								});
					return true;
				}
			};

			ListCallBack _callback = new ListCallBack();
			_lf.setCallback(_callback);

			for (int i = 0; i < listItemArray.length; i++) {
				_lf.insert(i);
				_callback.insert(listItemArray[i], i);
			}
			
			Constant.oldNewsLength = listItemArray.length;

			_lf.setRowHeight(50);
			_vfm.add(_lf);

			add(_vfm);
		}
	}	

	public void showIndicator(boolean visible) {
		try {
			ApplicationIndicatorRegistry reg = ApplicationIndicatorRegistry
					.getInstance();
			ApplicationIndicator appIndicator = reg.getApplicationIndicator();
			appIndicator.setVisible(visible);
		} catch (Exception e) {
			LogEventClass.logErrorEvent("Indicator error-"+e.getMessage());
		}
	}

	public void showEnterPhoneField(Hashtable hashtable2) {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();

		String curDateForDisplayLbl = new SimpleDateFormat("dd-MMM-yyyy")
				.formatLocal(date.getTime());

		DisplayTimeLbl = new LabelFieldImpl(curDateForDisplayLbl,
				DrawStyle.RIGHT | Field.USE_ALL_WIDTH | Field.USE_ALL_HEIGHT);
		fontlb0 = Font.getDefault().derive(Font.PLAIN, 6, Ui.UNITS_pt);
		DisplayTimeLbl.setFont(fontlb0);
		DisplayTimeLbl.setBgColor(0x000072BC);
		DisplayTimeLbl.setFontColor(Color.WHITE);

		userName = (String) hashtable2.get("stringval1");
		if (userName == null) {
			userName = "";
		}
		setUserName(userName);
		WelcomeNameLbl = new LabelFieldImpl("Welcome:" + userName);
		WelcomeNameLbl.setFont(fontlb0);
		WelcomeNameLbl.setBgColor(0x000072BC);
		WelcomeNameLbl.setFontColor(Color.WHITE);

		horizontalFieldManager2 = new HorizontalFieldManager(
				HorizontalFieldManager.USE_ALL_WIDTH);
		horizontalFieldManager2.add(WelcomeNameLbl);
		horizontalFieldManager2.add(DisplayTimeLbl);

		toolBarFieldManager = ShowToolbar.INSTANCE.show();
		toolBarFieldManager.add(horizontalFieldManager2);
		setTitle(toolBarFieldManager);
		ShowToolbar.INSTANCE.setScreenTitle("News Feeds");
	}

	public boolean onClose() {
		UiApplication.getUiApplication().requestBackground();
		return true;
	}

	protected void onExposed() {

		Constant.DATELABELVALUE = new SimpleDateFormat("dd-MMM-yyyy")
				.formatLocal(System.currentTimeMillis());

		String curDateForDisplayLbl = new SimpleDateFormat("dd-MMM-yyyy")
				.formatLocal(System.currentTimeMillis());
		DisplayTimeLbl = new LabelFieldImpl(curDateForDisplayLbl,
				DrawStyle.RIGHT | Field.USE_ALL_WIDTH | Field.USE_ALL_HEIGHT);
		DisplayTimeLbl.setFont(fontlb0);
		DisplayTimeLbl.setBgColor(0x000072BC);
		DisplayTimeLbl.setFontColor(Color.WHITE);

		String uName = getUserName();
		WelcomeNameLbl = new LabelFieldImpl("Welcome:" + uName);		
		WelcomeNameLbl.setFont(fontlb0);
		WelcomeNameLbl.setBgColor(0x000072BC);
		WelcomeNameLbl.setFontColor(Color.WHITE);

		horizontalFieldManager2 = new HorizontalFieldManager(
				HorizontalFieldManager.USE_ALL_WIDTH);
		horizontalFieldManager2.add(WelcomeNameLbl);
		horizontalFieldManager2.add(DisplayTimeLbl);

		VerticalFieldManager verticalFieldManager = ShowToolbar.INSTANCE.show();
		verticalFieldManager.add(horizontalFieldManager2);
		setTitle(verticalFieldManager);

		ToolBarField toolBarField = (ToolBarField) verticalFieldManager
				.getField(1);
		for (int i = 0; i < toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField
					.getField(i);
			tab2.setActive(false);
		}
	};

	protected int moveFocus(int amount, int status, int time) {
		invalidate();
		return super.moveFocus(amount, status, time);
	}

	protected boolean onSavePrompt() {
		return true;
	}

	class ListCallBack implements ListFieldCallback {
		private Vector listElements = new Vector();

		public void drawListRow(ListField list, Graphics g, int index, int y,
				int w) {
			Font font = g.getFont();					
			String text = (String) listElements.elementAt(index);
			g.drawText(text, 0, y + (list.getRowHeight() - font.getHeight())
					/ 2, 0, w);
		}

		public Object get(ListField list, int index) {
			return listElements.elementAt(index);
		}

		public int getPreferredWidth(ListField list) {
			return Display.getWidth();
		}

		public void insert(String toInsert, int index) {
			listElements.insertElementAt(toInsert, index);
		}

		public void erase() {
			listElements.removeAllElements();
		}

		public int indexOfList(ListField listField, String prefix, int start) {
			return listElements.indexOf(listField);
		}
	}
}