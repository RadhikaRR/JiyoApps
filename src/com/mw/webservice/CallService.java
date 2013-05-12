package com.mw.webservice;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransport;

import com.mw.agency.screens.AgencyDateAndZoneScreen;
import com.mw.agency.screens.HourlyCollectionScreen;
import com.mw.agency.screens.PerformanceSummaryReportScreen;
import com.mw.agency.screens.RecruitmentSummaryScreen;
import com.mw.attendance.AttendanceMarkerScreen;
import com.mw.attendance.GetAttendanceColorCodes;
import com.mw.autorefresh.CheckNewNewsFeeds;
import com.mw.carequote.CQProductListClass;
import com.mw.connect.ConnectionFectory;
import com.mw.connect.ConnectionType;
import com.mw.connect.TransportDetective;
import com.mw.connect.URLFactory;
import com.mw.constants.Constant;
import com.mw.control.Controller;
import com.mw.control.LogEventClass;
import com.mw.control.NewAuthenticator;
import com.mw.form.GetVisitInfoICCodes;
import com.mw.form.RefreshIcCodeAndProductList;
import com.mw.form.SubmitFormWithAutoCompleteListField;
import com.mw.smnew.NewSMTableListScreen;
import com.mw.splashscreen.PopupSpinnerScreen;
import com.mw.uiscreens.CollectionTableScreen;
import com.mw.uiscreens.ErrorScreenDialog;
import com.mw.uiscreens.SelectRangeDummyScreen;
import com.mw.utils.StringSplitter;

public class CallService {

	public static CallService INSTANCE = new CallService();

	private Timer timer;
	private TimerTask timerTask;
	private boolean finishedWork;
	private boolean isTimeOutErrorScreenShown;
	private HttpTransport httpTransport;
	private int counter = 0;
	private String urlAgency = "";

	// private boolean connectTCP = true;
	// private boolean connectBIS = false;

	public boolean checkNetworkConnection() {
		if ((CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS))
				|| (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B))
				|| (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT)) != false) {
			return true;
		}
		return false;
	}

	public boolean GetCollReport(String index, String date, String reportName, String mobile) {

		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency = doConnectionFirst(Constant.URL1);
				// String urlAgency = sampleConnection(Constant.URL1);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URL1);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URL1);
				}
				// else{
				// urlAgency = connectOther(Constant.URL1);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACE1, Constant.SAVE_AGENTLOCATION_METHOD1);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.CHANNEL, index);
				request.addProperty(Constant.DATE, date);
				request.addProperty(Constant.REPORTNAME, reportName);

				request.addProperty(Constant.COLLECTIONMOBILE, mobile);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAP_ACTION1, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;

				System.out.println("object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				if (reportName.trim().equalsIgnoreCase(Constant.COLLECTIONREPORT)) {
					SelectRangeDummyScreen.INSTANCE.manageObject(object);
				} else if (reportName.trim().equalsIgnoreCase(Constant.PRODUCTREPORT)) {
					CollectionTableScreen.INSTANCE.manageProductMISReport(object);
				} else if (reportName.trim().equalsIgnoreCase(Constant.PRODUCTPERCENTAGEREPORT)) {
					CollectionTableScreen.INSTANCE.managePercentReport(object);
				}

			} catch (IOException e) {
				LogEventClass.logErrorEvent("exception in GetCollReport " + e.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("Web service error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						if (reportName.trim().equalsIgnoreCase(Constant.COLLECTIONREPORT)) {
							return false;
						} else if (reportName.trim().equalsIgnoreCase(Constant.PRODUCTREPORT)) {
							return false;
						} else if (reportName.trim().equalsIgnoreCase(Constant.PRODUCTPERCENTAGEREPORT)) {
							return false;
						}
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to fetch "
											+ reportName
											+ "\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean GetZoneList() {
		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency = doConnectionFirst(Constant.URLAGENCY);
				// String urlAgency = sampleConnection(Constant.URLAGENCY);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URLAGENCY);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URLAGENCY);
				}
				// else{
				// urlAgency = connectOther(Constant.URLAGENCY);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACEAGENCY, Constant.METHODZONEAGENCY);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAPACTIONAGENCY, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;

				System.out.println("zone object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				NewAuthenticator.INSTANCE.newHandleZoneObject(object, "Zone");

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in GetZoneList " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("Web service GetZone Error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						// GetZoneList();
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Fetch Zone List.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getRegionList(String zone) {

		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency = doConnectionFirst(Constant.URLAGENCY);
				// String urlAgency = sampleConnection(Constant.URLAGENCY);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URLAGENCY);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URLAGENCY);
				}
				// else{
				// urlAgency = connectOther(Constant.URLAGENCY);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACEAGENCY, Constant.REGIONMETHODAGENCY);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.GETREGIONZONEINPUTPARAM, zone);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAPACTIONAGENCY, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;
				System.out.println("Region object:" + object.toString());

				Object object1 = resultsRequestSOAP.getProperty(0);
				System.out.println("object1 " + object1);
				Object object2 = resultsRequestSOAP.getProperty(1);
				System.out.println("object2 " + object2);
				Object object3 = resultsRequestSOAP.getProperty(2);
				System.out.println("object3 " + object3);

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				NewAuthenticator.INSTANCE.newHandleZoneObject(object, "Region");

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getRegionList " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("Web service GetRegion Error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Fetch Region List.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getDivisionList(String region) {

		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency = doConnectionFirst(Constant.URLAGENCY);
				// String urlAgency = sampleConnection(Constant.URLAGENCY);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URLAGENCY);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URLAGENCY);
				}
				// else{
				// urlAgency = connectOther(Constant.URLAGENCY);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACEAGENCY, Constant.DIVISIONMETHODAGENCY);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.GETDIVISIONREGIONINPUTPARAM, region);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAPACTIONAGENCY, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;
				System.out.println("Region object:" + object.toString());

				Object object1 = resultsRequestSOAP.getProperty(0);
				System.out.println("object1 " + object1);
				Object object2 = resultsRequestSOAP.getProperty(1);
				System.out.println("object2 " + object2);
				Object object3 = resultsRequestSOAP.getProperty(2);
				System.out.println("object3 " + object3);

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				NewAuthenticator.INSTANCE.newHandleZoneObject(object, "Division");

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getDivisionList " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("Web service GetDivision Error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Fetch Division List.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getUnitcodeList(String division) {
		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();
				// String urlAgency = doConnectionFirst(Constant.URLAGENCY);
				// String urlAgency = sampleConnection(Constant.URLAGENCY);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URLAGENCY);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URLAGENCY);
				}
				// else{
				// urlAgency = connectOther(Constant.URLAGENCY);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACEAGENCY, Constant.UNITCODEMETHODAGENCY);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.GETUNITCODEINPUTPARAM, division);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAPACTIONAGENCY, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;
				System.out.println("Region object:" + object.toString());

				Object object1 = resultsRequestSOAP.getProperty(0);
				System.out.println("object1 " + object1);
				Object object2 = resultsRequestSOAP.getProperty(1);
				System.out.println("object2 " + object2);
				Object object3 = resultsRequestSOAP.getProperty(2);
				System.out.println("object3 " + object3);

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				NewAuthenticator.INSTANCE.newHandleZoneObject(object, "UnitCode");

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getUnitcodeList " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("Web service GetUnitcode Error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
						// String temp = AgencyDateAndZoneScreen.zoneString;
						// getUnitcodeList(temp);
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Fetch UnitCode List.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean showNewAgencyTable(String zone, String region, String division, String unitcode, String date) {

		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency = doConnectionFirst(Constant.URLAGENCY);
				// String urlAgency = sampleConnection(Constant.URLAGENCY);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URLAGENCY);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URLAGENCY);
				}
				// else{
				// urlAgency = connectOther(Constant.URLAGENCY);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACEAGENCY, Constant.GETNEWAGENCYTABLEMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.AGENCYSMCOSTTABLEZONEINPUTPARAM, zone);
				request.addProperty(Constant.AGENCYSMCOSTTABLREGIONINPUTPARAM, region);
				request.addProperty(Constant.AGENCYSMCOSTTABLEDIVISIONINPUTPARAM, division);
				request.addProperty(Constant.AGENCYSMCOSTTABLEUNITCODEINPUTPARAM, unitcode);
				request.addProperty(Constant.AGENCYSMCOSTTABLEDATEINPUTPARAM, date);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAPACTIONAGENCY, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;
				System.out.println("new agency table object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				AgencyDateAndZoneScreen.INSTANCE.manageWebServiceObject(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in showNewAgencyTable " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("New Agency Web service Table Error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to fetch MIS Data.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean authenticateViaWebService(String imei) {

		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
					System.exit(0);
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.AUTHENTICATIONMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.AUTHENTICATIONINPUTPARAM, "35798902533787701");

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("authentication object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				NewAuthenticator.INSTANCE.handleAuthenticationObject(object);

			} catch (final IOException ioException) {
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				LogEventClass.logErrorEvent(ioException.getMessage());
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						if (screen instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen);
						}
						Dialog.alert("" + ioException.getMessage() + ", please check apn setting and activate GPRS");
					}
				});
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
						// authenticateViaWebService(IMEI);
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Authentication failed.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
						System.exit(0);
					}
				}
			}
		}
		return true;
	}

	public boolean newsBackground(String imei) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.AUTHENTICATIONMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.AUTHENTICATIONINPUTPARAM, "35798902533787701");

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("authentication object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				CheckNewNewsFeeds.instance.handleNewsObject(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in newsBackground" + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// newsBackground(IMEI);

						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Authentication failed.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getAllSalesManagerTables(String smCode) {

		Object object = null;
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();
				// String urlAgency = doConnectionFirst(Constant.URLAGENCY);
				// String urlAgency = sampleConnection(Constant.URLAGENCY);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.URLAGENCY);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.URLAGENCY);
				}
				// else{
				// urlAgency = connectOther(Constant.URLAGENCY);
				// }

				SoapObject request = new SoapObject(Constant.NAMESPACEAGENCY, Constant.NEW_ALL_SM_TABLES_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.SM_TABLE_SMCODE_INPUTPARAM, smCode);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.SOAPACTIONAGENCY, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				object = (Object) resultsRequestSOAP;
				System.out.println("New Sales Manager table object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				NewSMTableListScreen.INSTANCE.manageAllSalesManagerTablesObject(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getAllSalesManagerTables " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// getAllSalesManagerTables(Constant.SM_CODE);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;

						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to fetch SM Table.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getAttendanceColorCode(String imei) {

		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE,
						Constant.ATTENDANCE_COLORCODE_DETLS_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.ATTENDANCE_IMEI_INPUTPARAM, "35798902533787701");

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("Attendance color code object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				GetAttendanceColorCodes.INSTANCE.parseColorCodeObject(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getAttendanceColorCode " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// getAttendanceColorCode(IMEI);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Get Attendance Details.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean markedAttendance(String imei, String latitude, String longitude, String date) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.ATTENDANCE_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.ATTENDANCE_IMEI_INPUTPARAM, "35798902533787701");
				request.addProperty(Constant.ATTENDANCE_LATITUDE_INPUTPARAM, latitude);
				request.addProperty(Constant.ATTENDANCE_LONGITUDE_INPUTPARAM, longitude);
				request.addProperty(Constant.ATTENDANCE_DATE_INPUTPARAM, date);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("Attendance marked  object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				AttendanceMarkerScreen.INSTANCE.converObjectToHash(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exceptiom in markedAttendance " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String temp = AttendanceMarkerScreen.CurDate;
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// markedAttendance(IMEI, "0.0", "0.0", temp);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Marke Attendance.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getICCodeList(String imei, boolean isBackgroungService) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				if (!isBackgroungService) {
					startTimer();
				}

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.ICCODES_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.VISTINFO_IMEI_INPUTPARAM, "35798902533787701");

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("IC Codes object:" + object.toString());

				if (!isBackgroungService) {
					timerTask.cancel();
					timer.cancel();
					finishedWork = true;
					GetVisitInfoICCodes.INSTANCE.convertObjectToHash(object);
				} else {
					RefreshIcCodeAndProductList.INSTANCE.manageObject(object);
				}
			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getICCodeList " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				System.out.println("IC Code Web service Error:" + e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// getICCodeList(IMEI, false);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to fetch Visit Details.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean sumitFormData(String imei, String cname, String primium, String product, String iccode, String mobile) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.VISTINFO_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.VISTINFO_IMEI_INPUTPARAM, "35798902533787701");
				request.addProperty(Constant.VISTINFO_CUSTNAME_INPUTPARAM, cname);
				request.addProperty(Constant.VISTINFO_PREMIUM_INPUTPARAM, primium);
				request.addProperty(Constant.VISTINFO_PRODUCT_INPUTPARAM, product);
				request.addProperty(Constant.VISTINFO_CODENAME_INPUTPARAM, iccode);
				request.addProperty(Constant.VISTINFO_CONTACTNO_INPUTPARAM, mobile);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("Visit information  object:" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;
				SubmitFormWithAutoCompleteListField.INSTANCE.submitDataToWebService(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in sumitFormData " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// String Cname =
						// SubmitFormWithAutoCompleteListField.Cname;
						// String primiumm =
						// SubmitFormWithAutoCompleteListField.primium;
						// String selectedProduct =
						// SubmitFormWithAutoCompleteListField.selectedProduct;
						// String selectedICCode =
						// SubmitFormWithAutoCompleteListField.selectedICCode;
						// String mobilee =
						// SubmitFormWithAutoCompleteListField.mobile;
						//
						// sumitFormData(IMEI, Cname, primiumm, selectedProduct,
						// selectedICCode, mobilee);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Submit Visit information.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	// public String doConnectionFirst(String url) throws IOException {
	// try {
	// ConnectionFectory.getPossibleConnectionList();
	// if(ConnectionFectory.coverageTCP){
	// url = url + ";deviceside=true";
	// LogEventClass.logAlwaysEvent("Final url for tcpip is "+url);
	// }else if(ConnectionFectory.coverageBIS){
	// url = url + ";deviceside=false";
	// LogEventClass.logAlwaysEvent("url for BIS is "+url);
	//				
	// }else if(ConnectionFectory.coverageWiFi){
	// url = url + ";interface=wifi;deviceside=true";
	// LogEventClass.logAlwaysEvent("Final url for WIFI is "+url);
	//				
	// }else if(ConnectionFectory.coverageBES){
	// url = url + ";deviceside=false";
	// LogEventClass.logAlwaysEvent("Final url for BES is "+url);
	//				
	// }else if(ConnectionFectory.coverageWAP2){
	// ServiceRecord record =
	// ConnectionFectory.getServiceRecord(ConnectionType.WAP2);
	// if (record != null) {
	// url = url + ";deviceside=true;ConnectionUID="+ record.getUid();
	// LogEventClass.logAlwaysEvent("Final url for WAP2 is "+url);
	// }
	// }
	// } catch (Exception e) {
	// LogEventClass.logErrorEvent(e.getMessage());
	// UiEngine ui = Ui.getUiEngine();
	// ErrorScreenDialog errorDialog = new ErrorScreenDialog(
	// "Please check connectivity, or try after some time.");
	// ui.pushScreen(errorDialog);
	// }
	// throw new IOException("Connection not found");
	// }

	// public String doConnectionFirst(String url) throws IOException {
	// try {
	// int[] connectionList = ConnectionFectory
	// .getPossibleConnectionList();
	//
	// if (connectionList != null && connectionList.length > 0) {
	// int connType = connectionList[0];
	// LogEventClass.logAlwaysEvent("Finally "+connType+" connection is used for connection which is chosen from connectionList[0]"+connectionList[0]);
	//
	// switch (connType) {
	//
	// case ConnectionType.TCP_IP:
	// url = url + ";deviceside=true";
	// LogEventClass.logAlwaysEvent("Final url for tcpip is "+url);
	// break;
	// case ConnectionType.BIS:
	// url = url + ";deviceside=false";
	// LogEventClass.logAlwaysEvent("url for BIS is "+url);
	// break;
	// case ConnectionType.WIFI:
	// url = url + ";interface=wifi;deviceside=true";
	// LogEventClass.logAlwaysEvent("Final url for WIFI is "+url);
	// break;
	// case ConnectionType.BES:
	// url = url + ";deviceside=false";
	// LogEventClass.logAlwaysEvent("Final url for BES is "+url);
	// break;
	// case ConnectionType.WAP2:
	// ServiceRecord record =
	// ConnectionFectory.getServiceRecord(ConnectionType.WAP2);
	// if (record != null) {
	// url = url + ";deviceside=true;ConnectionUID="+ record.getUid();
	// LogEventClass.logAlwaysEvent("Final url for WAP2 is "+url);
	// }
	// break;
	// case ConnectionType.WAP:
	// // url = url + ";deviceside=false";
	// break;
	// case ConnectionType.UNITE:
	// // url = url + ";deviceside=false";
	// break;
	// }
	// url = url + ";ConnectionTimeout=" + Constant.HTTP_TIME_OUT;
	// LogEventClass.logAlwaysEvent("Last & Final url for Connection is "+url);
	//
	// return url;
	// }
	// } catch (Exception e) {
	// LogEventClass.logErrorEvent(e.getMessage());
	// UiEngine ui = Ui.getUiEngine();
	// ErrorScreenDialog errorDialog = new ErrorScreenDialog(
	// "Please check connectivity, or try after some time.");
	// ui.pushScreen(errorDialog);
	// }
	// throw new IOException("Connection not found");
	// }

	// public String doChecKConnectionWithoutTimeout(String url) {
	// try {
	// ConnectionFectory.getPossibleConnectionList();
	// if(ConnectionFectory.coverageTCP){
	// url = url + ";deviceside=true";
	// LogEventClass.logAlwaysEvent("Final url for tcpip is "+url);
	//				
	// }else if(ConnectionFectory.coverageBIS){
	// url = url + ";deviceside=false";
	// LogEventClass.logAlwaysEvent("url for BIS is "+url);
	//				
	// }else if(ConnectionFectory.coverageWiFi){
	// url = url + ";interface=wifi;deviceside=true";
	// LogEventClass.logAlwaysEvent("Final url for WIFI is "+url);
	//				
	// }else if(ConnectionFectory.coverageBES){
	// url = url + ";deviceside=false";
	// LogEventClass.logAlwaysEvent("Final url for BES is "+url);
	//				
	// }else if(ConnectionFectory.coverageWAP2){
	// ServiceRecord record =
	// ConnectionFectory.getServiceRecord(ConnectionType.WAP2);
	// if (record != null) {
	// url = url + ";deviceside=true;ConnectionUID="+ record.getUid();
	// LogEventClass.logAlwaysEvent("Final url for WAP2 is "+url);
	// }
	// }
	// } catch (Exception e) {
	// UiEngine ui = Ui.getUiEngine();
	// ErrorScreenDialog errorDialog = new ErrorScreenDialog(
	// "Please check connectivity, or try after some time.");
	// ui.pushScreen(errorDialog);
	// }
	// return url;
	// }

	public String doChecKConnectionWithoutTimeout(String url) {
		try {
			int[] connectionList = ConnectionFectory.getPossibleConnectionList();

			if (connectionList != null && connectionList.length > 0) {
				int connType = connectionList[0];

				switch (connType) {
				case ConnectionType.WIFI:
					url = url + ";interface=wifi;deviceside=true";
					break;
				case ConnectionType.BIS:
					break;
				case ConnectionType.BES:
					url = url + ";deviceside=false";
					break;
				case ConnectionType.WAP2:
					ServiceRecord record = ConnectionFectory.getServiceRecord(ConnectionType.WAP2);
					if (record != null) {
						url = url + ";deviceside=true;ConnectionUID=" + record.getUid();
					}
					break;
				case ConnectionType.TCP_IP:
					url = url + ";deviceside=true";
					break;
				case ConnectionType.WAP:
					// url = url + ";deviceside=false";
					break;
				case ConnectionType.UNITE:
					// url = url + ";deviceside=false";
					break;
				}

			}
		} catch (Exception e) {
			UiEngine ui = Ui.getUiEngine();
			ErrorScreenDialog errorDialog = new ErrorScreenDialog("Please check connectivity, or try after some time.");
			ui.pushScreen(errorDialog);
		}
		return url;
	}

	private void startTimer() {
		finishedWork = false;
		isTimeOutErrorScreenShown = false;
		// if(timer!=null){
		timer = new Timer();
		timerTask = new TimerTask() {

			public void run() {
				if (!finishedWork) {
					httpTransport.reset();
					// synchronized (UiApplication.getEventLock()) {
					// isTimeOutErrorScreenShown = true;

					// UiApplication.getUiApplication().invokeLater(
					// new Runnable() {
					// public void run() {
					// Screen screen = UiApplication
					// .getUiApplication()
					// .getActiveScreen();
					// if (screen instanceof PopupSpinnerScreen) {
					// UiApplication.getUiApplication()
					// .popScreen(screen);
					// }
					// Status
					// .show("Oops! Could not load content.Please check your connection");
					// }
					// });
					//
					// }
					try {
						isTimeOutErrorScreenShown = true;

						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
								if (screen2 instanceof ErrorScreenDialog) {
									UiApplication.getUiApplication().popScreen(screen2);
								}
								Status.show("Oops! Could not load content.Please check your connection");
							}
						});
					} catch (Exception e) {
						LogEventClass.logErrorEvent(e.getMessage());

						isTimeOutErrorScreenShown = true;
						synchronized (UiApplication.getEventLock()) {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
							Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
							if (screen2 instanceof ErrorScreenDialog) {
								UiApplication.getUiApplication().popScreen(screen2);
							}
						}
					}
				}
			}
		};
		timer.schedule(timerTask, Constant.WEB_SERVICE_TIMEOUT);
	}

	public boolean getCQList(String imei, boolean isBackroungRefresh) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				if (!isBackroungRefresh) {
					startTimer();
				}

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.CqProduct_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constant.CqPremiumIMEI_METHOD, "35798902533787701");

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("CQ Products are:" + object.toString());

				if (!isBackroungRefresh) {
					timerTask.cancel();
					timer.cancel();
					finishedWork = true;
					CQProductListClass.INSTANCE.getCQproduct(object, "", "", false);
				} else {
					CQProductListClass.INSTANCE.getCQproduct(object, "", "", true);
				}
			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getCQList " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// getCQList(IMEI, false);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve CQ Products Details.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getCQpremium(String imei, String Cqname, String CqAge, String CqGender, String CqSa,
			String CqPremTerm, String CqTerm, String CqCode, String CqRider) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.CqPremium_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty("pImeiNo", "35798902533787701");
				request.addProperty("pCqName", Cqname);
				request.addProperty("pCqAge", CqAge);
				request.addProperty("pCqGender", CqGender);
				request.addProperty("pCqSa", CqSa);
				request.addProperty("pCqPremTerm", CqPremTerm);
				request.addProperty("pCqTerm", CqTerm);
				request.addProperty("pCqCode", CqCode);
				request.addProperty("pCqRiders", CqRider);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				CQProductListClass.INSTANCE.getCqPremium(object, false, Cqname, CqCode);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in getCQpremium " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String imeii = CQScreen2.imei;
						// String Cqnamee = CQScreen2.Cqname;
						// String CqAgee = CQScreen2.CqAge;
						// String CqGenderr = CQScreen2.CqGender;
						// String CqSaa = CQScreen2.CqSa;
						// String CqPremTermm = CQScreen2.CqPremTerm;
						// String CqTermm = CQScreen2.CqTerm;
						//
						// String CqCodee = CQScreen2.CqCode;
						// String CqRiderr = CQScreen2.CqRider;
						//
						// getCQpremium(imeii, Cqnamee, CqAgee, CqGenderr,
						// CqSaa,
						// CqPremTermm, CqTermm, CqCodee, CqRiderr);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve CQ Details. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean publishCqRiders(String imei, String CqCode, String CqsumAssured) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.AUTHENTICATIONURL);
				// String urlAgency =
				// sampleConnection(Constant.AUTHENTICATIONURL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.AUTHENTICATIONURL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.AUTHENTICATIONURL);
				}
				// else{
				// urlAgency = connectOther(Constant.AUTHENTICATIONURL);
				// }

				SoapObject request = new SoapObject(Constant.AUTHENTICATIONNAMESPACE, Constant.CqRider_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty("pImeiNo", "35798902533787701");
				request.addProperty("pProductName", CqCode);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.AUTHENTICATIONSOAPACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				CQProductListClass.INSTANCE.getCqRider(object, CqsumAssured, CqCode);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception iin publishCqRiders " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// String IMEI =
						// NewAuthenticator.INSTANCE.getIMEINumber();
						// String selectedProduct =
						// CareQuoteDetailsScreen.selectedProduct;
						// String CqsumAssuredd =
						// CareQuoteDetailsScreen.CqsumAssured;
						// publishCqRiders(IMEI, selectedProduct,
						// CqsumAssuredd);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve CqRider Details.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean callvalidateClmPolicyWS(String PolicyId, Vector v) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				// String urlAgency =
				// doConnectionFirst("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				// String urlAgency =
				// sampleConnection("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				if (Constant.connectTCP) {
					urlAgency = connectTCP("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				}
				// else{
				// urlAgency =
				// connectOther("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				// }

				SoapObject request = new SoapObject("http://www.mandarpathak.com/webservices/", "validateClmPolicy");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty("pFlag", "1");
				request.addProperty("pValue", PolicyId);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call("http://www.mandarpathak.com/webservices/validateClmPolicy", envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("Object o/p for Claim policy is------------------------" + object);

				getReturnedResultFromPloicyWS(object, PolicyId, v);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in callvalidateClmPolicyWS " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					// String PolicyIdd = dialogPopUpScreen.PolicyId;
					// Vector vect = dialogPopUpScreen.v;
					// callvalidateClmPolicyWS(PolicyIdd, vect);
					while (counter == 2) {
						Constant.connectBIS = true;
						Constant.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constant.connectBIS = false;
					Constant.connectTCP = true;
					synchronized (UiApplication.getEventLock()) {
						UiEngine ui = Ui.getUiEngine();
						ErrorScreenDialog errorDialog = new ErrorScreenDialog(
								"Failed to Retrieve ImageCapture Service. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
						ui.pushScreen(errorDialog);
					}
				}
			}
		}
		return true;
	}

	public void getReturnedResultFromPloicyWS(Object object, final String PolicyId, final Vector v) {

		if (object != null) {
			String response = object.toString();
			if (response != null) {
				if (response.trim().equals("0")) {

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							UiApplication.getUiApplication().popScreen(screen);
							Dialog.alert("Invalid Claim Id");
						}
					});
				} else {
					if (!(v.isEmpty())) {
						Controller.showScreen(new PopupSpinnerScreen("Uploading Image(s),Please Wait..."));

						Thread thread = new Thread() {
							public void run() {
								for (int i = 0; i < v.size(); i++) {

									byte[] temp = (byte[]) v.elementAt(i);

									CallService.INSTANCE.callUploadImageWS(temp, "", PolicyId, v);
								}
							};
						};
						thread.start();
					}
				}
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Screen screen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(screen);
						Dialog.alert("Pre requisite Policy configurations are not available on the server.");
					}
				});
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Dialog.alert("Pre requisite Policy configurations are not available on the server.");
				}
			});
		}
	}

	public void callUploadImageWS(byte[] data, String fileNameRR, String FolderName, Vector v) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date();
				date.setTime(System.currentTimeMillis());
				String Filename = dateFormat.format(date);
				String FileName = Filename + ".jpg";

				System.out.println("File name is------------" + FileName);

				// String urlAgency =
				// doConnectionFirst("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				// String urlAgency =
				// sampleConnection("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				if (Constant.connectTCP) {
					urlAgency = connectTCP("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				} else {
					urlAgency = connectOther("http://202.90.192.144/WSUploadFile/uploadFile.asmx");
				}

				SoapObject request = new SoapObject("http://www.mandarpathak.com/webservices/", "uploadFile");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				new MarshalBase64().register(envelope); // serialization
				request.addProperty("data", data);
				request.addProperty("pfileName", FileName);
				request.addProperty("pFolderName", FolderName);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call("http://www.mandarpathak.com/webservices/uploadFile", envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("o/p of WS is-----------------" + object.toString());

				getReturnedWSResultForUpdateImage(object, v);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in callUploadImageWS " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());

				synchronized (UiApplication.getEventLock()) {
					UiEngine ui = Ui.getUiEngine();

					Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
					if (screen2 instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen2);
					}

					ErrorScreenDialog errorDialog = new ErrorScreenDialog(
							"Failed to Retrieve ImageCapture Service.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
					ui.pushScreen(errorDialog);
				}
			}
		}
	}

	int county = 0;

	public void getReturnedWSResultForUpdateImage(Object object, final Vector v) {
		if (object != null) {
			String response = object.toString();
			if (response != null && response.equals("SUCCESS")) {
				county += 1;
				System.out.println("counter is ------------------------------" + county);
				System.out.println("size of vectro during counting --------------------------" + v.size());

				if (county != v.size()) {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Status.show(county + " image uploaded successfully", 750);
							System.out.println("-------------------STATUS SHOWN");
						}
					});
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen0 = UiApplication.getUiApplication().getActiveScreen();
							UiApplication.getUiApplication().popScreen(screen0);
							Screen screen1 = UiApplication.getUiApplication().getActiveScreen();
							UiApplication.getUiApplication().popScreen(screen1);
							Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
							if (screen2 instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen2);
							} else {
								UiApplication.getUiApplication().popScreen(screen2);
							}
							Dialog.alert("" + v.size() + " Image(s) Uploaded Successfully...");
							county = 0;
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);
					Dialog.alert("Pre requisite Policy configurations are not available on the server.");
				}
			});
		}
	}

	public boolean DMScoreCardWebService() {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.DmBmScoreCard_URL);
				// String urlAgency =
				// sampleConnection(Constant.DmBmScoreCard_URL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.DmBmScoreCard_URL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.DmBmScoreCard_URL);
				}
				// else{
				// urlAgency = connectOther(Constant.DmBmScoreCard_URL);
				// }

				SoapObject request = new SoapObject(Constant.DmBmScoreCard_NAMESPACE, Constant.DmScoreCard_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty("pBmDmCode", Constant.managerArea);
				request.addProperty("pBmDmFlag", Constant.managerRole);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.DmBmScoreCard_SOAP_ACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS output for DM ScoreCard is----------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				NewSMTableListScreen.INSTANCE.getDMScoreCardResult(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in DMScoreCardWebService " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// DMScoreCardWebService();
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve DM Score Card Details.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean BMScoreCardWebService() {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.DmBmScoreCard_URL);
				// String urlAgency =
				// sampleConnection(Constant.DmBmScoreCard_URL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.DmBmScoreCard_URL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.DmBmScoreCard_URL);
				}
				// else{
				// urlAgency = connectOther(Constant.DmBmScoreCard_URL);
				// }

				SoapObject request = new SoapObject(Constant.DmBmScoreCard_NAMESPACE, Constant.DmScoreCard_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty("pBmDmCode", Constant.managerArea);
				request.addProperty("pBmDmFlag", Constant.managerRole);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.DmBmScoreCard_SOAP_ACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("WS output for bM ScoreCard is----------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				NewSMTableListScreen.INSTANCE.getBMScoreCardResult(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in BMScoreCardWebService " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// BMScoreCardWebService();
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve BM Score Card Details.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean PerformanceReportWS() {

		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.DmBmScoreCard_URL);
				// String urlAgency =
				// sampleConnection(Constant.DmBmScoreCard_URL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.DmBmScoreCard_URL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.DmBmScoreCard_URL);
				}
				// else{
				// urlAgency = connectOther(Constant.DmBmScoreCard_URL);
				// }

				SoapObject request = new SoapObject(Constant.DmBmScoreCard_NAMESPACE, "performanceSummary");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				if (Constant.managerRole.trim().equalsIgnoreCase("SM")) {
					request.addProperty("pBranchSmCode", Constant.employeeCode);
					request.addProperty("pBranchType", "SM");
				} else {
					request.addProperty("pBranchSmCode", Constant.managerArea);
					request.addProperty("pBranchType", Constant.managerRole);
				}

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.DmBmScoreCard_SOAP_ACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("WS output for PerformanceReportWS is----------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				getReturnPerformanceReportWS(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in PerformanceReportWS " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// PerformanceReportWS();
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve Performance Report.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean RecruitmentReportWS() {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.DmBmScoreCard_URL);
				// String urlAgency =
				// sampleConnection(Constant.DmBmScoreCard_URL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.DmBmScoreCard_URL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.DmBmScoreCard_URL);
				}
				// else{
				// urlAgency = connectOther(Constant.DmBmScoreCard_URL);
				// }

				SoapObject request = new SoapObject(Constant.DmBmScoreCard_NAMESPACE, "recruitmentSummary");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				if (Constant.managerRole.trim().equalsIgnoreCase("SM")) {
					request.addProperty("pBranchSmCode", Constant.employeeCode);
					request.addProperty("pBranchType", "SM");
				} else {
					request.addProperty("pBranchSmCode", Constant.managerArea);
					request.addProperty("pBranchType", Constant.managerRole);
				}

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.DmBmScoreCard_SOAP_ACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("WS output for RecruitmentReportWS is----------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				getReturnRecruitmentReportWS(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in RecruitmentReportWS " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// RecruitmentReportWS();
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve Recruitment Report.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean HourlyCollectionWS() {
		boolean nw = checkNetworkConnection();
		if (nw == false && counter == 0) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				startTimer();

				// String urlAgency =
				// doConnectionFirst(Constant.DmBmScoreCard_URL);
				// String urlAgency =
				// sampleConnection(Constant.DmBmScoreCard_URL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.DmBmScoreCard_URL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.DmBmScoreCard_URL);
				}
				// else{
				// urlAgency = connectOther(Constant.DmBmScoreCard_URL);
				// }

				SoapObject request = new SoapObject(Constant.DmBmScoreCard_NAMESPACE, "hourlyCollection");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				if (Constant.managerRole.trim().equalsIgnoreCase("SM")) {
					request.addProperty("pBranchSmCode", Constant.employeeCode);
					request.addProperty("pBranchType", "SM");
				} else {
					request.addProperty("pBranchSmCode", Constant.managerArea);
					request.addProperty("pBranchType", Constant.managerRole);
				}

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.DmBmScoreCard_SOAP_ACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				System.out.println("WS output for HourlyCollectionWS is----------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				finishedWork = true;

				getReturnHourlyCollectionWS(object);

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in HourlyCollectionWS " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// HourlyCollectionWS();
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve Hourly Report.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public void getReturnPerformanceReportWS(Object object) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {

				final String checkForSuccessResponse = (String) hashtable.get("string80");

				if (checkForSuccessResponse.equals("SUCCESS")) {

					Controller.showScreen(new PerformanceSummaryReportScreen(hashtable));
				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Performance Report. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	public void getReturnRecruitmentReportWS(Object object) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable.get("string80");

				if (checkForSuccessResponse.equals("SUCCESS")) {

					Controller.showScreen(new RecruitmentSummaryScreen(hashtable));

				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {

						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Recruitment Report. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	public void getReturnHourlyCollectionWS(Object object) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable.get("string80");

				if (checkForSuccessResponse.equals("SUCCESS")) {
					Controller.showScreen(new HourlyCollectionScreen(hashtable));

				} else {
					synchronized (UiApplication.getEventLock()) {
						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch Hourly Collection. Unable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	public boolean publishCeoTrackerChannel(boolean isBackroungRefresh) {
		boolean nw = checkNetworkConnection();
		if (nw == false) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Status.show("Oops! Network Connection is unavailable");
				}
			});
		} else {
			try {
				if (!isBackroungRefresh) {
					startTimer();
				}

				// String urlAgency =
				// doConnectionFirst(Constant.DmBmScoreCard_URL);
				// String urlAgency =
				// sampleConnection(Constant.DmBmScoreCard_URL);
				if (Constant.connectTCP) {
					urlAgency = connectTCP(Constant.DmBmScoreCard_URL);
				} else if (Constant.connectBIS) {
					urlAgency = connectBIS(Constant.DmBmScoreCard_URL);
				}
				// else{
				// urlAgency = connectOther(Constant.DmBmScoreCard_URL);
				// }

				SoapObject request = new SoapObject(Constant.DmBmScoreCard_NAMESPACE,
						Constant.publishCeoTrackerChannel_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);
				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constant.DmBmScoreCard_SOAP_ACTION, envelope);

				Object resultsRequestSOAP = (Object) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;

				if (!isBackroungRefresh) {
					timerTask.cancel();
					timer.cancel();
					finishedWork = true;
					getReturnPublishCeoTrackerChannel(object, false);
				} else {
					getReturnPublishCeoTrackerChannel(object, true);
				}

			} catch (IOException ioException) {
				LogEventClass.logErrorEvent("exception in publishCeoTrackerChannel " + ioException.getMessage());
				Constant.connectTCP = true;
				Constant.connectBIS = false;
				synchronized (UiApplication.getEventLock()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent(e.getMessage());
				if (!isTimeOutErrorScreenShown) {
					counter = counter + 1;
					if (counter <= 2) {
						// publishCeoTrackerChannel(false);
						while (counter == 2) {
							Constant.connectBIS = true;
							Constant.connectTCP = false;
							break;
						}
						return false;
					} else {
						counter = 0;
						Constant.connectBIS = false;
						Constant.connectTCP = true;
						synchronized (UiApplication.getEventLock()) {
							UiEngine ui = Ui.getUiEngine();
							ErrorScreenDialog errorDialog = new ErrorScreenDialog(
									"Failed to Retrieve CeoTrackerChannel List.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.");
							ui.pushScreen(errorDialog);
						}
					}
				}
			}
		}
		return true;
	}

	public void getReturnPublishCeoTrackerChannel(Object object, boolean isBackroundRefresh) {
		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = NewAuthenticator.INSTANCE.converObjectToHashTable(response);

			if (hashtable != null) {
				final String checkForSuccessResponse = (String) hashtable.get("string40");

				if (checkForSuccessResponse.equals("SUCCESS")) {
					if (isBackroundRefresh) {
						String channelList = (String) hashtable.get("string1");
						if (channelList != null) {
							String[] CeoTrackerChannelListArray = StringSplitter.INSTANCE.split(channelList, "#M#");
							String[] channelListName = new String[CeoTrackerChannelListArray.length];
							String[] channelVal = new String[CeoTrackerChannelListArray.length];

							for (int i = 0; i < CeoTrackerChannelListArray.length; i++) {

								String temp_str = CeoTrackerChannelListArray[i];
								String[] temp_arr = StringSplitter.INSTANCE.split(temp_str, "~");
								channelVal[i] = temp_arr[0];
								channelListName[i] = temp_arr[1];
							}

							Constant.oldChannelListName = channelListName;
							Constant.oldchannelVal = channelVal;
						}
					} else {
						String channelList = (String) hashtable.get("string1");
						if (channelList != null) {
							String[] CeoTrackerChannelListArray = StringSplitter.INSTANCE.split(channelList, "#M#");
							String[] channelListName = new String[CeoTrackerChannelListArray.length];
							String[] channelVal = new String[CeoTrackerChannelListArray.length];

							for (int i = 0; i < CeoTrackerChannelListArray.length; i++) {

								String temp_str = CeoTrackerChannelListArray[i];
								String[] temp_arr = StringSplitter.INSTANCE.split(temp_str, "~");
								channelVal[i] = temp_arr[0];
								channelListName[i] = temp_arr[1];
							}
							Controller.showScreen(new SelectRangeDummyScreen(channelVal, channelListName));
						}
					}
				} else {
					synchronized (UiApplication.getEventLock()) {

						Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
						UiApplication.getUiApplication().popScreen(activeScreen);

						Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
						if (screen2 instanceof PopupSpinnerScreen) {
							UiApplication.getUiApplication().popScreen(screen2);
						}
					}

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(checkForSuccessResponse);
						}
					});
				}
			}
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Status
							.show(
									"Failed to fetch CeoTrackerChannel List.\nUnable to connect to the Internet, please try again later. If the problem persists please contact your service provider.",
									3000);
				}
			});
		}
	}

	// public String sampleConnection(String url) throws IOException {
	//
	// TransportDetective detective = new TransportDetective();
	//
	// int availableTransportCoverage =
	// detective.getAvailableTransportCoverage();
	//
	// if (availableTransportCoverage == 0) {
	// throw new IOException("No Transport coverage detected");
	// }
	//
	// URLFactory urlFactory = new URLFactory(url);
	//
	// if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI))
	// {
	// url = urlFactory.getHttpTcpWiFiUrl();
	// LogEventClass.logAlwaysEvent("TRANSPORT_TCP_WIFI is available with url is "+
	// url);
	// } else if
	// (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_CELLULAR))
	// {
	// url = url + ";deviceside=true";
	// LogEventClass.logAlwaysEvent("TRANSPORT_TCP_CELLULAR is available with url is "+
	// url);
	// } else if
	// (detective.isCoverageAvailable(TransportDetective.TRANSPORT_BIS_B)) {
	// url = urlFactory.getHttpBisUrl();
	// LogEventClass.logAlwaysEvent("TRANSPORT_BIS_B is available with url is "+
	// url);
	// } else if
	// (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR))
	// {
	// url = urlFactory.getHttpDefaultTcpCellularUrl(detective
	// .getDefaultTcpCellularServiceRecord());
	// LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is "+
	// url);
	// } else if
	// (detective.isCoverageAvailable(TransportDetective.TRANSPORT_MDS)) {
	// url = urlFactory.getHttpMdsUrl(false);
	// LogEventClass.logAlwaysEvent("TRANSPORT_MDS is available with url is "+
	// url);
	// } else if
	// (detective.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2)) {
	// url = urlFactory.getHttpWap2Url(detective.getWap2ServiceRecord());
	// LogEventClass.logAlwaysEvent("TRANSPORT_WAP2 is available with url is "+
	// url);
	// } else {
	// throw new IOException("No Supported Transport coverage detected");
	// }
	// int TIME_OUT = 60000;
	// url = url + ";ConnectionTimeout=" + TIME_OUT;
	// LogEventClass.logAlwaysEvent("Final URL is " + url);
	// return url;
	// }

	public String connectTCP(String url) throws IOException {
		LogEventClass.logAlwaysEvent("TCP is selected and control is inside TCP connection");
		TransportDetective detective = new TransportDetective();

		int availableTransportCoverage = detective.getAvailableTransportCoverage();

		if (availableTransportCoverage == 0) {
			throw new IOException("No Transport coverage detected");
		}

		URLFactory urlFactory = new URLFactory(url);

		if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI)) {
			url = urlFactory.getHttpTcpWiFiUrl();
			LogEventClass.logAlwaysEvent("TRANSPORT_TCP_WIFI is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_CELLULAR)) {
			url = url + ";deviceside=true";
			LogEventClass.logAlwaysEvent("TRANSPORT_TCP_CELLULAR is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR)) {
			url = urlFactory.getHttpDefaultTcpCellularUrl(detective.getDefaultTcpCellularServiceRecord());
			LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is " + url);
		} else {
			// throw new
			// IOException("No Supported Transport coverage detected");
		}
		LogEventClass.logAlwaysEvent("Final URL is " + url);
		return url;
	}

	public String connectBIS(String url) throws IOException {
		LogEventClass.logAlwaysEvent("BIS is selected and control is inside BIS connection");
		TransportDetective detective = new TransportDetective();

		int availableTransportCoverage = detective.getAvailableTransportCoverage();

		if (availableTransportCoverage == 0) {
			throw new IOException("No Transport coverage detected");
		}

		URLFactory urlFactory = new URLFactory(url);

		if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI)) {
			url = urlFactory.getHttpTcpWiFiUrl();
			LogEventClass.logAlwaysEvent("TRANSPORT_TCP_WIFI is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_BIS_B)) {
			url = urlFactory.getHttpBisUrl();
			LogEventClass.logAlwaysEvent("TRANSPORT_BIS_B is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR)) {
			url = urlFactory.getHttpDefaultTcpCellularUrl(detective.getDefaultTcpCellularServiceRecord());
			LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_MDS)) {
			url = urlFactory.getHttpMdsUrl(false);
			LogEventClass.logAlwaysEvent("TRANSPORT_MDS is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2)) {
			url = urlFactory.getHttpWap2Url(detective.getWap2ServiceRecord());
			LogEventClass.logAlwaysEvent("TRANSPORT_WAP2 is available with url is " + url);
		} else {
			throw new IOException("No Supported Transport coverage detected");
		}
		LogEventClass.logAlwaysEvent("Final URL is " + url);
		return url;
	}

	public String connectOther(String url) throws IOException {
		LogEventClass.logAlwaysEvent("Other selected and control is inside connectOther connection");
		TransportDetective detective = new TransportDetective();

		int availableTransportCoverage = detective.getAvailableTransportCoverage();

		if (availableTransportCoverage == 0) {
			throw new IOException("No Transport coverage detected");
		}

		URLFactory urlFactory = new URLFactory(url);

		if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_MDS)) {
			url = urlFactory.getHttpMdsUrl(false);
			LogEventClass.logAlwaysEvent("TRANSPORT_MDS is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2)) {
			url = urlFactory.getHttpWap2Url(detective.getWap2ServiceRecord());
			LogEventClass.logAlwaysEvent("TRANSPORT_WAP2 is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR)) {
			url = urlFactory.getHttpDefaultTcpCellularUrl(detective.getDefaultTcpCellularServiceRecord());
			LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is " + url);
		} else {
			throw new IOException("No Supported Transport coverage detected");
		}
		LogEventClass.logAlwaysEvent("Final URL is " + url);
		return url;
	}
}
