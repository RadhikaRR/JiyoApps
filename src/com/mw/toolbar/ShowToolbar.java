package com.mw.toolbar;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.mw.attendance.GetAttendanceColorCodes;
import com.mw.cameraCapture.FileSystemJournalListenerImpl;
import com.mw.cameraCapture.dialogPopUpScreen;
import com.mw.carequote.CQProductListClass;
import com.mw.carequote.CareQuoteDetailsScreen;
import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.customfields.CustomTitlebar;
import com.mw.form.GetVisitInfoICCodes;
import com.mw.form.SubmitFormWithAutoCompleteListField;
import com.mw.mis.screens.MISEntryScreen;
import com.mw.uiscreens.EntryScreen2;
import com.mw.utils.StringSplitter;

public class ShowToolbar extends MainScreen {

	private Bitmap newsFeedtoolBarbitmap;
	private Bitmap newsFeedFocustoolBarbitmap;
	private Bitmap MIStoolBarbitmap;
	private Bitmap MISFocustoolBarbitmap;
	private Bitmap attendanceMarkertoolBarbitmap;
	private Bitmap attendanceMarkerFocustoolBarbitmap;
	private Bitmap visitInfoFormtoolBarbitmap;
	private Bitmap visitInfoFormFocustoolBarbitmap;
	private Bitmap careQuotetoolBarbitmap;
	private Bitmap careQuoteFocustoolBarbitmap;
	private Bitmap imageCapturetoolBarbitmap;
	private Bitmap imageCaptureFocustoolBarbitmap;

	public ToolBarField toolBarField;

	private ToolBarButtonField newsFeedToolBarButtonField;
	private ToolBarButtonField MISToolBarButtonField;
	private ToolBarButtonField attendanceMarkerToolBarButtonField;
	private ToolBarButtonField visitInfoFormToolBarButtonField;
	private ToolBarButtonField careQuoteToolBarButtonField;
	private ToolBarButtonField imageCaptureToolBarButtonField;

	private FocusChangeListener toolbarfocusChangeListener;
	private FieldChangeListener toolbarFieldChangeListener;

	private VerticalFieldManager verticalFieldManager;

	private Bitmap titleBitmap;
	private CustomTitlebar titleBarField;
	private String screenTitle;

	boolean isCameraApplicationInvoked;

	public static ShowToolbar INSTANCE = new ShowToolbar();

	public VerticalFieldManager show() {

		verticalFieldManager = new VerticalFieldManager(Manager.FIELD_HCENTER | FIELD_HCENTER
				| HorizontalFieldManager.FIELD_HCENTER);

		titleBitmap = Bitmap.getBitmapResource("logo.PNG");
		titleBarField = new CustomTitlebar(screenTitle, Color.WHITE, 0x000072BC, titleBitmap, Field.USE_ALL_WIDTH);

		toolBarField = new ToolBarField();

		newsFeedtoolBarbitmap = Bitmap.getBitmapResource("newbtn3.PNG");
		newsFeedFocustoolBarbitmap = Bitmap.getBitmapResource("newbtn3focus.PNG");

		MIStoolBarbitmap = Bitmap.getBitmapResource("newbtn1.PNG");
		MISFocustoolBarbitmap = Bitmap.getBitmapResource("newbtn1focus.PNG");

		attendanceMarkertoolBarbitmap = Bitmap.getBitmapResource("newbtn2.PNG");
		attendanceMarkerFocustoolBarbitmap = Bitmap.getBitmapResource("newbtn2focus.PNG");

		visitInfoFormtoolBarbitmap = Bitmap.getBitmapResource("visitnew.PNG");
		visitInfoFormFocustoolBarbitmap = Bitmap.getBitmapResource("visitfocusnew.PNG");

		careQuotetoolBarbitmap = Bitmap.getBitmapResource("careQuoteNew.PNG");
		careQuoteFocustoolBarbitmap = Bitmap.getBitmapResource("careQuoteFocusNew.PNG");

		imageCapturetoolBarbitmap = Bitmap.getBitmapResource("cameraUnfocus.PNG");
		imageCaptureFocustoolBarbitmap = Bitmap.getBitmapResource("cameraFocus.PNG");

		newsFeedToolBarButtonField = new ToolBarButtonField(newsFeedtoolBarbitmap, newsFeedFocustoolBarbitmap);
		MISToolBarButtonField = new ToolBarButtonField(MIStoolBarbitmap, MISFocustoolBarbitmap);
		attendanceMarkerToolBarButtonField = new ToolBarButtonField(attendanceMarkertoolBarbitmap,
				attendanceMarkerFocustoolBarbitmap);
		visitInfoFormToolBarButtonField = new ToolBarButtonField(visitInfoFormtoolBarbitmap,
				visitInfoFormFocustoolBarbitmap);
		careQuoteToolBarButtonField = new ToolBarButtonField(careQuotetoolBarbitmap, careQuoteFocustoolBarbitmap);
		imageCaptureToolBarButtonField = new ToolBarButtonField(imageCapturetoolBarbitmap,
				imageCaptureFocustoolBarbitmap);

		toolBarField.addButton(newsFeedToolBarButtonField, true);

		if (Constant.module1 || Constant.module2 || Constant.module7 || Constant.module8 || Constant.module9) {
			toolBarField.addButton(MISToolBarButtonField, true);
		}
		if (Constant.module3) {
			toolBarField.addButton(attendanceMarkerToolBarButtonField, true);
		}
		if (Constant.module4) {
			toolBarField.addButton(visitInfoFormToolBarButtonField, true);
		}
		if (Constant.module5) {
			toolBarField.addButton(careQuoteToolBarButtonField, true);
		}
		if (Constant.module6) {
			toolBarField.addButton(imageCaptureToolBarButtonField, true);
		}

		toolbarfocusChangeListener = new FocusChangeListener() {

			public void focusChanged(Field field, int eventType) {

				switch (eventType) {
				case FOCUS_CHANGED:
				case FOCUS_GAINED:
					int fieldWithFocusIndex = toolBarField.getFieldWithFocusIndex();

					for (int i = 0; i < toolBarField.getFieldCount(); i++) {
						ToolBarButtonField tab = (ToolBarButtonField) toolBarField.getField(i);
						tab.setActive(false);
					}

					ToolBarButtonField tab = (ToolBarButtonField) toolBarField.getField(fieldWithFocusIndex);
					tab.setActive(true);

					if (tab == newsFeedToolBarButtonField) {
						titleBarField.setTitle("News Feeds");
					} else if (tab == MISToolBarButtonField) {
						titleBarField.setTitle("MIS");
					} else if (tab == attendanceMarkerToolBarButtonField) {
						titleBarField.setTitle("Attendance Marker");
					} else if (tab == visitInfoFormToolBarButtonField) {
						titleBarField.setTitle("Visit Info");
					} else if (tab == careQuoteToolBarButtonField) {
						titleBarField.setTitle("Care Quote");
					} else if (tab == imageCaptureToolBarButtonField) {
						titleBarField.setTitle("Image Capture");
					}

					invalidate();
					break;

				case FOCUS_LOST:
					for (int i = 0; i < toolBarField.getFieldCount(); i++) {
						ToolBarButtonField tab2 = (ToolBarButtonField) toolBarField.getField(i);
						tab2.setActive(false);
					}
					titleBarField.setTitle(getScreenTitle());
					break;
				}
				invalidate();
			}
		};

		toolbarFieldChangeListener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {

				ToolBarButtonField clickedtoolBarButtonField = (ToolBarButtonField) field;

				if (clickedtoolBarButtonField == newsFeedToolBarButtonField) {

					titleBarField.setTitle("News Feeds");
					if (Constant.latestNewsHashTable != null) {

						boolean isUpdatedNews = doCheckLengthOfNewsForisNewNews(Constant.oldNewsHashTable,
								Constant.latestNewsHashTable);
						if (isUpdatedNews) {
							Controller.showScreen(new EntryScreen2(Constant.latestNewsHashTable));
						} else {
							Controller.showScreen(new EntryScreen2(Constant.oldNewsHashTable));
						}
					} else {
						Controller.showScreen(new EntryScreen2(Constant.oldNewsHashTable));
					}

				} else if (clickedtoolBarButtonField == MISToolBarButtonField
						&& (Constant.module1 || Constant.module2 || Constant.module7 || Constant.module8 || Constant.module9)) {
					titleBarField.setTitle("MIS");
					Controller.showScreen(new MISEntryScreen());

				} else if (clickedtoolBarButtonField == attendanceMarkerToolBarButtonField && Constant.module3) {
					titleBarField.setTitle("Attendance Marker");
					GetAttendanceColorCodes.INSTANCE.getAttendanceColorCodes();

				} else if (clickedtoolBarButtonField == visitInfoFormToolBarButtonField && Constant.module4) {
					titleBarField.setTitle("Visit Info");

					if (Constant.oldProductArray == null) {
						GetVisitInfoICCodes.INSTANCE.callgetICCodeWebService();
					} else {
						Controller.showScreen(new SubmitFormWithAutoCompleteListField(Constant.oldICCodeArray,
								Constant.oldProductArray));
					}

				} else if (clickedtoolBarButtonField == careQuoteToolBarButtonField && Constant.module5) {
					titleBarField.setTitle("Care Quote");

					// String persistCareProduct=PersistProvider.INSTANCE
					// .getObject(Constant.NewCareProductsvalue);
					if (Constant.oldCareQuoteProductArray == null) {
						CQProductListClass.INSTANCE.getCQProductList();
					} else {
						Controller.showScreen(new CareQuoteDetailsScreen(Constant.oldCareQuoteProductArray));
					}
				} else if (clickedtoolBarButtonField == imageCaptureToolBarButtonField && Constant.module6) {
					titleBarField.setTitle("Image Capture");
					startCamera();
				}
			}
		};

		toolBarField.setFocusListener(toolbarfocusChangeListener);

		newsFeedToolBarButtonField.setChangeListener(toolbarFieldChangeListener);
		if (Constant.module1 || Constant.module2 || Constant.module7 || Constant.module8 || Constant.module9) {
			MISToolBarButtonField.setChangeListener(toolbarFieldChangeListener);
		}
		if (Constant.module3) {
			attendanceMarkerToolBarButtonField.setChangeListener(toolbarFieldChangeListener);
		}
		if (Constant.module4) {
			visitInfoFormToolBarButtonField.setChangeListener(toolbarFieldChangeListener);
		}
		if (Constant.module5) {
			careQuoteToolBarButtonField.setChangeListener(toolbarFieldChangeListener);
		}
		if (Constant.module6) {
			imageCaptureToolBarButtonField.setChangeListener(toolbarFieldChangeListener);
		}

		verticalFieldManager.add(titleBarField);
		verticalFieldManager.add(toolBarField);

		return verticalFieldManager;
	}

	private boolean doCheckLengthOfNewsForisNewNews(Hashtable oldhashtable, Hashtable newhashtable) {
		if (oldhashtable != null && newhashtable != null) {

			String oldNewsString = (String) oldhashtable.get("stringval3");
			String newNewsString = (String) newhashtable.get("stringval3");
			String[] oldNewsArray = StringSplitter.INSTANCE.split(oldNewsString, "~");
			String[] newNewsArray = StringSplitter.INSTANCE.split(newNewsString, "~");
			if (newNewsArray.length != oldNewsArray.length) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	private void startCamera() {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					int ask = Dialog.ask(Dialog.D_YES_NO,
									"This will invoke camera application. Please make sure that camera resolution is set to minimum.",
									Dialog.YES);

					if (ask == Dialog.YES) {
						FileSystemJournalListenerImpl.INSTANCE.startListening();
						isCameraApplicationInvoked = true;
						Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments(
								CameraArguments.ARG_CAMERA_APP));
					}

				} catch (Exception e) {
					Dialog.alert("Could not start Camera");
				}
			}
		};
		UiApplication.getUiApplication().invokeLater(runnable);
	}

	public void activate() {

		if (isCameraApplicationInvoked) {
			Runnable runnable = new Runnable() {
				public void run() {
					isCameraApplicationInvoked = false;
					FileSystemJournalListenerImpl.INSTANCE.stopListening();
					Vector v = FileSystemJournalListenerImpl.INSTANCE.getRecordedFilePath();
					System.out.println("size of vector v is-------------------------" + v.size());
					if (!(v.isEmpty())) {
						Controller.showScreen(new dialogPopUpScreen("" + v.size() + " Image(s) Saved successfully", v));
					} else {
						Dialog.alert("No Images captured,Exiting Camera!!!");
					}
				}
			};
			UiApplication.getUiApplication().invokeLater(runnable);
		}
	}
}
