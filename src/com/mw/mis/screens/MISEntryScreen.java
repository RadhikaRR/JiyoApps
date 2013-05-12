package com.mw.mis.screens;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.smnew.NewSMTableListScreen;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.toolbar.ShowToolbar;
import com.mw.toolbar.ToolBarButtonField;
import com.mw.toolbar.ToolBarField;
import com.mw.uiscreens.SelectRangeDummyScreen;
import com.mw.utils.StringSplitter;
import com.mw.webservice.CallService;

public class MISEntryScreen extends MainScreen {

	private VerticalFieldManager verticalFieldManager;
	private VerticalFieldManager _vfm;
	private ListField _lf;
	private String role;

	public MISEntryScreen() {

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0093C3E2, 0x0093C3E2));

		ShowToolbar.INSTANCE.setScreenTitle("MIS");
		verticalFieldManager = ShowToolbar.INSTANCE.show();
		setTitle(verticalFieldManager);

		_vfm = new VerticalFieldManager();

		_lf = new ListField() {

			protected boolean navigationClick(int status, int time) {

				int selIndex = _lf.getSelectedIndex();
				ListFieldCallback callback = _lf.getCallback();
				String string = callback.get(_lf, selIndex).toString();

				if (string.trim().equalsIgnoreCase("MIS Tracker")) {

					if (Constant.oldChannelListName == null) {

						Controller.showScreen(new PopupSpinnerScreen(
								"Retrieving Channel List.."));

						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;	
								flag = CallService.INSTANCE.publishCeoTrackerChannel(false);
								if(flag == false){
									flag = CallService.INSTANCE.publishCeoTrackerChannel(false);
								}
							};
						};
						thread.start();
					} else {
						Controller.showScreen(new SelectRangeDummyScreen(
								Constant.oldchannelVal,
								Constant.oldChannelListName));
					}

				} else if (string.trim()
						.equalsIgnoreCase("Performance Summary")) {

					Controller.showScreen(new PopupSpinnerScreen(
							"Retrieving Performance Summary"));

					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;						
							flag = CallService.INSTANCE.PerformanceReportWS();
							if(flag == false){
								flag = CallService.INSTANCE.PerformanceReportWS();
							}
						};
					};
					thread.start();

				} else if (string.trim()
						.equalsIgnoreCase("Recruitment Summary")) {

					Controller.showScreen(new PopupSpinnerScreen(
							"Retrieving Recruitment Summary"));

					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallService.INSTANCE.RecruitmentReportWS();
							if(flag == false){
								flag = CallService.INSTANCE.RecruitmentReportWS();
							}
						};
					};
					thread.start();

				} else if (string.trim().equalsIgnoreCase("Hourly Collection")) {

					Controller.showScreen(new PopupSpinnerScreen(
							"Retrieving Hourly Collection"));

					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallService.INSTANCE.HourlyCollectionWS();
							if(flag == false){
								flag = CallService.INSTANCE.HourlyCollectionWS();
							}
						};
					};
					thread.start();

				} else if (string.trim().equalsIgnoreCase(role)) {

					if (Constant.isSalesManager) {

						NewSMTableListScreen.INSTANCE.callService();
					} else {

						if (Constant.isAllManager) {

							Constant.CHOICETITLE = "Select Zone";
							Constant.GETBUTTONLABEL = "Region";
							Constant.screenSwitchCounter = 3;

							Controller.showScreen(new PopupSpinnerScreen(
									"Retrieving Zone List..."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;
									flag = CallService.INSTANCE.GetZoneList();
									while(flag == false){
										flag = CallService.INSTANCE.GetZoneList();
									}
								};
							};
							thread.start();

						} else if (Constant.isZonalManager) {

							Constant.CHOICETITLE = "Select Region";
							Constant.GETBUTTONLABEL = "Division";
							Constant.screenSwitchCounter = 2;

							if (Constant.managerArea != null) {
								Controller.showScreen(new PopupSpinnerScreen(
										"Retrieving Region List..."));
								Thread thread = new Thread() {
									public void run() {
										boolean flag = true;
										flag = CallService.INSTANCE.getRegionList(Constant.managerArea);
										while(flag == false){
											flag = CallService.INSTANCE.getRegionList(Constant.managerArea);
										}
									};
								};
								thread.start();

							} else {

								UiApplication.getUiApplication().invokeLater(
										new Runnable() {

											public void run() {
												Dialog
														.alert("Pre requisite default Zone configurations is not available on the server.");
											}
										});
							}

						} else if (Constant.isRegionalManager) {

							Constant.CHOICETITLE = "Select Division";
							Constant.GETBUTTONLABEL = "UnitCode";
							Constant.screenSwitchCounter = 1;

							if (Constant.managerArea != null) {

								Controller.showScreen(new PopupSpinnerScreen(
										"Retrieving Division List..."));
								Thread thread = new Thread() {
									public void run() {
										CallService.INSTANCE
												.getDivisionList(Constant.managerArea);
									};
								};
								thread.start();

							} else {

								UiApplication.getUiApplication().invokeLater(
										new Runnable() {

											public void run() {
												Dialog
														.alert("Pre requisite default Region configurations is not available on the server.");
											}
										});
							}

						} else if (Constant.isDivisionalManager) {

							Constant.CHOICETITLE = "Select UnitCode";
							Constant.GETBUTTONLABEL = "UnitCode";
							Constant.screenSwitchCounter = 0;
							if (Constant.managerArea != null) {
								Controller.showScreen(new PopupSpinnerScreen(
										"Retrieving DM ScoreCard..."));
								Thread thread = new Thread() {
									public void run() {
										boolean flag = true;
										flag = CallService.INSTANCE.DMScoreCardWebService();
										if(flag == false){
											flag = CallService.INSTANCE.DMScoreCardWebService();
										}
									};
								};
								thread.start();

							} else {
								UiApplication.getUiApplication().invokeLater(
										new Runnable() {

											public void run() {
												Dialog
														.alert("Pre requisite default DM configurations is not available on the server.");
											}
										});
							}

						} else if (Constant.isBranchManager) {
							Controller.showScreen(new PopupSpinnerScreen(
									"Retrieving BM ScoreCard..."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;									
									flag = CallService.INSTANCE.BMScoreCardWebService();
									if(flag == false){
										flag = CallService.INSTANCE.BMScoreCardWebService();
									}
								};
							};
							thread.start();
						} else {
							UiApplication.getUiApplication().invokeAndWait(
									new Runnable() {

										public void run() {
											Dialog
													.alert("Pre requisite default BM configurations is not available on the server.");
										}
									});
						}
					}
				}
				return true;
			}
		};

		ListCallBack _callback = new ListCallBack();
		_lf.setCallback(_callback);
		role = Constant.managerRole + " ScoreCard";

		Hashtable hashTable = new Hashtable();
		if (Constant.module1) {
			hashTable.put("0", "MIS Tracker");
		}
		if (Constant.module2) {
			hashTable.put("1", role);
		}
		if (Constant.module7) {
			hashTable.put("2", "Performance Summary");
		}
		if (Constant.module8) {
			hashTable.put("3", "Recruitment Summary");
		}
		if (Constant.module9) {
			hashTable.put("4", "Hourly Collection");
		}
		Enumeration names = hashTable.keys();
		String rr = "";
		while (names.hasMoreElements()) {
			String str = (String) names.nextElement();
			String namee = (String) hashTable.get(str);
			rr = rr + "~" + namee;
		}
		String[] name = StringSplitter.INSTANCE.split(rr, "~");
		int length = name.length;
		for (int i = 0; i < length; i++) {
			_lf.insert(i);
			String namee = name[i];
			_callback.insert(namee, i);
		}
		_lf.setRowHeight(50);
		_vfm.add(_lf);

		add(_vfm);

		for (int i = 0; i < ShowToolbar.INSTANCE.toolBarField.getFieldCount(); i++) {
			ToolBarButtonField tab2 = (ToolBarButtonField) ShowToolbar.INSTANCE.toolBarField
					.getField(i);
			tab2.setActive(false);
		}
		_lf.setFocus();
	}

	public boolean onClose() {
		UiApplication.getUiApplication().requestBackground();
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
	}

	class ListCallBack implements ListFieldCallback {
		private Vector listElements = new Vector();

		public void drawListRow(ListField list, Graphics g, int index, int y,
				int w) {
			Font font = g.getFont().derive(Font.UNDERLINED);
			g.setFont(font);
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
