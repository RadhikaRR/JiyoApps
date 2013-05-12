package com.mw.constants;

import java.util.Hashtable;

import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.device.api.i18n.SimpleDateFormat;

public class Constant {

	public static final String NAMESPACE = "http://com.bajajallianz/BagicMobApp.xsd";
	public static final String URL = "http://202.90.192.38/GetMobDataDWH/GetMobDataDWH";
	public static final String SOAP_ACTION = "GetMobDataDWHPortType";
	public static final String SAVE_AGENTLOCATION_METHOD = "getData";

	public static final String MOBILENO = "pMobileNo";
	public static final String LATITUDE = "pLatitude";
	public static final String LONGITUDE = "pLongitude";

	public static final String NAMESPACE1 = "http://conntodwh/CeoTracker.xsd";
	public static final String URL1 = "http://web7.bajajallianz.com/BagicMobApp/BagicMobApp";
	public static final String SOAP_ACTION1 = "BagicMobAppPortType";
	public static final String SAVE_AGENTLOCATION_METHOD1 = "getChannelData1";

	public static final String CHANNEL = "pChannel";
	public static final String DATE = "pDate";
	public static final String REPORTNAME = "pReportName";
	public static final String IMEI = "pImeiNumber";
	public static final String COLLECTIONMOBILE = "pMobileNo";

	public static final String COLLECTIONREPORT = "COLLECTION_REPORT";
	public static final String PRODUCTREPORT = "PRODUCT_REPORT";
	public static final String PRODUCTPERCENTAGEREPORT = "PRODUCT_PERC_REPORT";

	public static final String OLDDATE = "olddate";
	public static final String OLDCHANNEL = "oldchannel";
	public static final String PERSISTCHANNELINDEX = "persistChannelIndex";
	public static final String PERSISTDATE = "PersistDate";

	public static final String AGENCYOLDDATE = "agencyolddate";
	public static final String AGENCYPERSISTDATE = "agencyPersistDate";

	public static final String SMSFLAG = "smsflag";
		
	public static final String NewCareProductsvalue = "CareProductsvalue";

	public static final String URLAGENCY = "http://web7.bajajallianz.com/BagicMobApp/BagicMobApp";
	public static final String NAMESPACEAGENCY = "http://com.bajajallianz/BagicMobApp.xsd";
	public static final String SOAPACTIONAGENCY = "BagicMobAppPortType";
	public static final String METHODZONEAGENCY = "getZoneMasterLst";

	public static final String REGIONMETHODAGENCY = "getRegionMasterLst";
	public static final String GETREGIONZONEINPUTPARAM = "pZone";

	public static final String DIVISIONMETHODAGENCY = "getDivisionMasterLst";
	public static final String GETDIVISIONREGIONINPUTPARAM = "pRegion";

	public static final String UNITCODEMETHODAGENCY = "getUnitCodeLst";
	public static final String GETUNITCODEINPUTPARAM = "pDivision";

	public static final String NOZONEFROMWEBSERVICE = "Web service data not available.";
	public static final String AGENCYERRORMESSAGE = "The mentioned report does not exist in the system.";

	public static String CHOICETITLE = null;
	public static String GETBUTTONLABEL = null;

	public static String DATELABELVALUE = new SimpleDateFormat("dd-MMM-yyyy")
			.formatLocal(System.currentTimeMillis());

	public static final String AGENCYSMCOSTTABLEZONEINPUTPARAM = "pZone";
	public static final String AGENCYSMCOSTTABLREGIONINPUTPARAM = "pRegion";
	public static final String AGENCYSMCOSTTABLEDIVISIONINPUTPARAM = "pDivision";
	public static final String AGENCYSMCOSTTABLEUNITCODEINPUTPARAM = "pUnitCode";
	public static final String AGENCYSMCOSTTABLEDATEINPUTPARAM = "pDate";

	public static final String GETAGENCYTABLEMETHOD = "getAgencyChannelDtls";
	public static final String GETNEWAGENCYTABLEMETHOD = "getAgencyDataNew";
	public static final String GETNEWAGENCYTABLEMETHOD2 = "getAgencyDataNew1";
	public static final String GETSMCOSTTABLEMETHOD = "getAgencyChannelDtls1";

	public static int screenSwitchCounter = 0;
	
	public static final String AUTHENTICATIONURL = "http://web7.bajajallianz.com/BagicWap/BagicWap";
	public static final String AUTHENTICATIONNAMESPACE = "http://com.bajajallianz/BagicWap.xsd";
	public static final String AUTHENTICATIONSOAPACTION = "BagicWapPortType";
	public static final String AUTHENTICATIONMETHOD = "verifyUserDtls";
	
	public static final String NEWSFEEDNMETHOD = "userFeeds";

	public static final String AUTHENTICATIONINPUTPARAM = "pImeiNo";
	public static final String NEWSFEEDINPUTPARAM = "pImeiNo";

	public static boolean module1 = false;
	public static boolean module2 = false;
	public static boolean module3 = false;
	public static boolean module4 = false;
	public static boolean module5 = false;
	public static boolean module6 = false;
	public static boolean module7 = false;
	public static boolean module8 = false;
	public static boolean module9 = false;
	

	public static String SM_CODE = null;
	public static final String SM_TABLE_METHOD = "getAgencyDataSm";
	public static final String NEW_ALL_SM_TABLES_METHOD = "salesManagerTable";
	public static final String SM_TABLE_SMCODE_INPUTPARAM = "pSmCode";
	public static final String SM_TABLE_DATE_INPUTPARAM = "pDate";
	
	public static Hashtable oldNewsHashTable; 
	public static Hashtable latestNewsHashTable; 
	public static int oldNewsLength;	
	public static int newNewsArrayLength;

	public static boolean isSalesManager = false;
	public static boolean isAllManager = false;
	public static boolean isZonalManager = false;
	public static boolean isRegionalManager = false;
	public static boolean isDivisionalManager = false;
	public static boolean isBranchManager = false;

	public static String managerArea = null;
	public static String managerRole = null;
	public static String managerName = null;
	public static String employeeCode = null;

	public static final String ATTENDANCE_METHOD = "markAttendance";
	public static final String ATTENDANCE_COLORCODE_DETLS_METHOD = "attendanceDtls";
	public static final String ATTENDANCE_IMEI_INPUTPARAM = "pImeiNo";
	public static final String ATTENDANCE_LATITUDE_INPUTPARAM = "pLatitude";
	public static final String ATTENDANCE_LONGITUDE_INPUTPARAM = "pLongitude";
	public static final String ATTENDANCE_DATE_INPUTPARAM = "pDate";

	public static final String VISTINFO_METHOD = "visitInfo";
	public static final String VISTINFO_IMEI_INPUTPARAM = "pImeiNo";
	public static final String VISTINFO_CUSTNAME_INPUTPARAM = "pCustomerName";
	public static final String VISTINFO_PREMIUM_INPUTPARAM = "pPreimum";
	public static final String VISTINFO_PRODUCT_INPUTPARAM = "pProduct";
	public static final String VISTINFO_CODENAME_INPUTPARAM = "pCodeName";
	public static final String VISTINFO_CONTACTNO_INPUTPARAM = "pContactNo";

	public static final String ICCODES_METHOD = "fetchSmLst";
	
	public static final String CqProduct_METHOD = "publishCqProduct";
	
	public static final String CqPremium_METHOD = "publishCqPremium";
	public static final String CqPremiumIMEI_METHOD = "pImeiNo";
	public static final String CqpCqCode_METHOD = "pCqCode";
	public static final String CqpCqSa_METHOD = "pCqSa";
	public static final String CqpCqAge_METHOD = "pCqAge";
	public static final String CqpCqTerm_METHOD = "pCqTerm";
	
	public static final String CqRider_METHOD = "publishCqRiders";
	public static final String CqRider_IMEI_METHOD = "pImeiNo";
	public static final String CqRider_PRODUCT = "pProductName";
	
	public static final String DmBmScoreCard_URL = "http://web7.bajajallianz.com/BagicMobApp/BagicMobApp";
	public static final String DmBmScoreCard_NAMESPACE = "http://com.bajajallianz/BagicMobApp.xsd";
	public static final String DmBmScoreCard_SOAP_ACTION = "BagicMobAppPortType";
	public static final String DmScoreCard_METHOD = "bmDmScoreCard";
	public static final String BmScoreCard_METHOD = "bmDmScoreCard";	

	public static final String HTTP_TIME_OUT = "60000";
	public static final int WEB_SERVICE_TIMEOUT = 60000;

	public static final String newAppDownloadAppendURL = "/BlackBerry/JiyoApps.jad";

	public static String[] oldICCodeArray = null;
	public static String[] oldProductArray = null;
	
	public static String[] oldCareQuoteProductArray = null;
	
	public static String[] oldChannelListName = null;
	public static String[] oldchannelVal = null;
	
	public static int riderCount = 0;
	
	public static ApplicationIcon mIcon;
	
	public static final long Jiyo_Notification_ID = 0x749cb23a75c60e2dL;
	
	public static final String publishCeoTrackerChannel_METHOD = "publishCeoTrackerChannel";
	
	public static final long GUID = 0x2051fd67b72d11L;
	public static final String APP_NAME = "JiyoApps";
	
	public static boolean connectTCP = true;
	public static boolean connectBIS = false;
}
