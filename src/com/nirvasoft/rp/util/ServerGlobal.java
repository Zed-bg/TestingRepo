package com.nirvasoft.rp.util;

public class ServerGlobal {

	private static String mAppPath = "";
	private static String mSeparator = "\\|\\|\\_\\_";
	private static String mFCSeparator = "\\_\\_\\|";
	private static boolean mSetWriteLog = true;
	private static String fortuneYgnMerchant;
	private static String fortuneMdyMerchant;
	private static String mFCRTServiceURL = "";
	private static String mFCMakerID = "";
	private static String mFCCheckerID = "";
	private static String mEPIXServiceURL = "";

	private static String mFCACServiceURL = "";
	private static String mFCCUServiceURL = "";
	private static String mFCPCServiceURL = "";
	private static int mBrCodeStart = 0;
	private static int mBrCodeEnd = 0;
	private static String mXREFPrefix = "";
	private static String BankName = "";
	// CNP
	private static String mGetBillURL = "";
	private static String mMakePaymentURL = "";
	private static String mGetTxnURL = "";
	private static String mTopupURL = "";
	private static String mAPIKey = "";
	private static String DueTime;
	private static String DueDay;
	private static String BankRate = "";

	// Sky Pay
	private static String mRechargeUrl = "";
	private static String muserName = "";
	private static String mpass = "";
	private static String mpop = "";
	private static String mEntID = "";

	// CCD
	private static String mGetCCDURL = "";
	private static String mGetCCDKEY = "";
	private static String mGetCCDINITVECTOR = "";
	private static String mGetCCDPURL = "";
	private static String mGetCCDPaidUnPaidURL = "";
	private static String mGetCCDConnectTimeout = "";
	private static String mGetCCDReadTimeout = "";

	// NowNow
	private static String mGetNNUrl = "";
	private static String mGetNNOperatorUrl = "";
	private static String mGetNNUserName = "";
	private static String mGetNNPassword = "";

	// MPT
	private static String MPTUrl = "";
	private static String MPTASPID = "";
	private static String MPTServiceID = "";
	private static String MPTPassword = "";
	private static String MPTTimeStamp = "";
	private static String MPTBalanceType = "";
	private static int MPTPeriod = 10;

	private static String walletURL = "";

	// Agent Cash
	public static int CREDIT = 2;
	public static int DEBIT = 1;
	public static int REMIT = 1;
	public static int ENCASH = 2;
	public static int DEPOSIT = 3;
	public static int WITHDRAWL = 4;

	// TAX
	public static String taxUrl;
	public static String taxNotifyUrl;
	public static String taxUserID;
	
	// fcm
	public static String fcmUrl;
	public static String fcmMode;
	public static String fcmServerApiKey;
	public static String fcmTopicName;

	// skynet
	public static String skynetTokenUrl = "";
	public static String skynetInstallItemUrl = "";
	public static String skynetSubscriptionUrl = "";
	public static String skynetVoucherUrl = "";
	public static String skynetTopupUrl = "";
	public static String skynetUsername = "";
	public static String skynetPassword = "";
	public static String skynetOrganisation = "";
	public static String skynetMinuteBeforeExpiry = "";
	public static String skynetCatalogListUrl = "";
	public static String skynetAvaliablePPVUrl = "";
	public static String skynetAddPPVUrl = "";
	public static String skynetMovie = "";	
	
	// MPSS
	public static String mpssTPUrl = "";
	public static String mpssUsername = "";
	public static String mpssPassword = "";
	public static String mpssOrganisation = "";
	public static String mpssAPIKey = "";
	public static String mpssDPUrl = "";
	
	public static String mpssEGCUrl = "";
	public static String mpssEGCAPIKey = "";
	public static String mpssEGCUserRef = "";
	
	public static String mEnvPath1 = "";
	public static String mEnvPath2 = "";
	
	public static String mDriver = "";
	public static String mUrl = "";
	public static String mUserid = "";
	public static String mPassword = "";
	
	public static String getAppPath() {
		return mAppPath;
	}

	public static String getBankName() {
		return BankName;
	}

	public static String getBankRate() {
		return BankRate;
	}

	public static String getDueDay() {
		return DueDay;
	}

	public static String getDueTime() {
		return DueTime;
	}

	public static String getFCCheckerID() {
		return mFCCheckerID;
	}

	public static String getFCMakerID() {
		return mFCMakerID;
	}

	public static String getFCRTServiceURL() {
		return mFCRTServiceURL;
	}

	public static String getFCSeparator() {
		return mFCSeparator;
	}

	public static String getFortuneMdyMerchant() {
		return fortuneMdyMerchant;
	}

	public static String getFortuneYgnMerchant() {
		return fortuneYgnMerchant;
	}

	public static String getmAPIKey() {
		return mAPIKey;
	}

	public static String getmAppPath() {
		return mAppPath;
	}

	public static int getmBrCodeEnd() {
		return mBrCodeEnd;
	}

	public static int getmBrCodeStart() {
		return mBrCodeStart;
	}

	public static String getMEntID() {
		return mEntID;
	}

	public static String getmEPIXServiceURL() {
		return mEPIXServiceURL;
	}

	public static String getmFCACServiceURL() {
		return mFCACServiceURL;
	}

	public static String getmFCCUServiceURL() {
		return mFCCUServiceURL;
	}

	public static String getmFCPCServiceURL() {
		return mFCPCServiceURL;
	}

	public static String getmFCRTServiceURL() {
		return mFCRTServiceURL;
	}

	public static String getmFCSeparator() {
		return mFCSeparator;
	}

	public static String getmGetBillURL() {
		return mGetBillURL;
	}

	public static String getmGetCCDINITVECTOR() {
		return mGetCCDINITVECTOR;
	}

	public static String getmGetCCDKEY() {
		return mGetCCDKEY;
	}

	public static String getmGetCCDPURL() {
		return mGetCCDPURL;
	}

	public static String getmGetCCDURL() {
		return mGetCCDURL;
	}

	public static String getmGetNNOperatorUrl() {
		return mGetNNOperatorUrl;
	}

	public static String getmGetNNPassword() {
		return mGetNNPassword;
	}

	public static String getmGetNNUrl() {
		return mGetNNUrl;
	}

	public static String getmGetNNUserName() {
		return mGetNNUserName;
	}

	public static String getmGetTxnURL() {
		return mGetTxnURL;
	}

	public static String getmMakePaymentURL() {
		return mMakePaymentURL;
	}

	public static String getMpass() {
		return mpass;
	}

	public static String getMpop() {
		return mpop;
	}

	public static String getMPTASPID() {
		return MPTASPID;
	}

	public static String getMPTBalanceType() {
		return MPTBalanceType;
	}

	public static String getMPTPassword() {
		return MPTPassword;
	}

	public static int getMPTPeriod() {
		return MPTPeriod;
	}

	public static String getMPTServiceID() {
		return MPTServiceID;
	}

	public static String getMPTTimeStamp() {
		return MPTTimeStamp;
	}

	public static String getMPTUrl() {
		return MPTUrl;
	}

	public static String getmRechargeUrl() {
		return mRechargeUrl;
	}

	public static String getmSeparator() {
		return mSeparator;
	}

	public static String getmTopupURL() {
		return mTopupURL;
	}

	public static String getMuserName() {
		return muserName;
	}

	public static String getmXREFPrefix() {
		return mXREFPrefix;
	}

	public static String getSeparator() {
		return mSeparator;
	}

	public static String getWalletURL() {
		return walletURL;
	}

	public static boolean ismSetWriteLog() {
		return mSetWriteLog;
	}

	public static boolean isWriteLog() {
		return mSetWriteLog;
	}

	public static void setAppPath(String mAppPath) {
		ServerGlobal.mAppPath = mAppPath;
	}

	public static void setBankName(String bankName) {
		BankName = bankName;
	}

	public static void setBankRate(String bankRate) {
		BankRate = bankRate;
	}

	public static void setDueDay(String dueDay) {
		DueDay = dueDay;
	}

	public static void setDueTime(String dueTime) {
		DueTime = dueTime;
	}

	public static void setFCCheckerID(String mFCCheckerID) {
		ServerGlobal.mFCCheckerID = mFCCheckerID;
	}

	public static void setFCMakerID(String mFCMakerID) {
		ServerGlobal.mFCMakerID = mFCMakerID;
	}

	public static void setFCRTServiceURL(String mFCURL) {
		ServerGlobal.mFCRTServiceURL = mFCURL;
	}

	public static void setFortuneMdyMerchant(String fortuneMdyMerchant) {
		ServerGlobal.fortuneMdyMerchant = fortuneMdyMerchant;
	}

	public static void setFortuneYgnMerchant(String fortuneYgnMerchant) {
		ServerGlobal.fortuneYgnMerchant = fortuneYgnMerchant;
	}

	public static void setmAPIKey(String mAPIKey) {
		ServerGlobal.mAPIKey = mAPIKey;
	}

	public static void setmAppPath(String mAppPath) {
		ServerGlobal.mAppPath = mAppPath;
	}

	public static void setmBrCodeEnd(int mBrCodeEnd) {
		ServerGlobal.mBrCodeEnd = mBrCodeEnd;
	}

	public static void setmBrCodeStart(int mBrCodeStart) {
		ServerGlobal.mBrCodeStart = mBrCodeStart;
	}

	public static void setMEntID(String mEntID) {
		ServerGlobal.mEntID = mEntID;
	}

	public static void setmEPIXServiceURL(String mEPIXServiceURL) {
		ServerGlobal.mEPIXServiceURL = mEPIXServiceURL;
	}

	public static void setmFCACServiceURL(String mFCACServiceURL) {
		ServerGlobal.mFCACServiceURL = mFCACServiceURL;
	}

	public static void setmFCCUServiceURL(String mFCCUServiceURL) {
		ServerGlobal.mFCCUServiceURL = mFCCUServiceURL;
	}

	public static void setmFCPCServiceURL(String mFCPCServiceURL) {
		ServerGlobal.mFCPCServiceURL = mFCPCServiceURL;
	}

	public static void setmFCRTServiceURL(String mFCRTServiceURL) {
		ServerGlobal.mFCRTServiceURL = mFCRTServiceURL;
	}

	public static void setmFCSeparator(String mFCSeparator) {
		ServerGlobal.mFCSeparator = mFCSeparator;
	}

	public static void setmGetBillURL(String mGetBillURL) {
		ServerGlobal.mGetBillURL = mGetBillURL;
	}

	public static void setmGetCCDINITVECTOR(String mGetCCDINITVECTOR) {
		ServerGlobal.mGetCCDINITVECTOR = mGetCCDINITVECTOR;
	}

	public static void setmGetCCDKEY(String mGetCCDKEY) {
		ServerGlobal.mGetCCDKEY = mGetCCDKEY;
	}

	public static void setmGetCCDPURL(String mGetCCDPURL) {
		ServerGlobal.mGetCCDPURL = mGetCCDPURL;
	}

	public static void setmGetCCDURL(String mGetCCDURL) {
		ServerGlobal.mGetCCDURL = mGetCCDURL;
	}

	public static void setmGetNNOperatorUrl(String mGetNNOperatorUrl) {
		ServerGlobal.mGetNNOperatorUrl = mGetNNOperatorUrl;
	}

	public static void setmGetNNPassword(String mGetNNPassword) {
		ServerGlobal.mGetNNPassword = mGetNNPassword;
	}

	public static void setmGetNNUrl(String mGetNNUrl) {
		ServerGlobal.mGetNNUrl = mGetNNUrl;
	}

	public static void setmGetNNUserName(String mGetNNUserName) {
		ServerGlobal.mGetNNUserName = mGetNNUserName;
	}

	public static void setmGetTxnURL(String mGetTxnURL) {
		ServerGlobal.mGetTxnURL = mGetTxnURL;
	}

	public static void setmMakePaymentURL(String mMakePaymentURL) {
		ServerGlobal.mMakePaymentURL = mMakePaymentURL;
	}

	public static void setMpass(String mpass) {
		ServerGlobal.mpass = mpass;
	}

	public static void setMpop(String mpop) {
		ServerGlobal.mpop = mpop;
	}

	public static void setMPTASPID(String mPTASPID) {
		MPTASPID = mPTASPID;
	}

	public static void setMPTBalanceType(String mPTBalanceType) {
		MPTBalanceType = mPTBalanceType;
	}

	public static void setMPTPassword(String mPTPassword) {
		MPTPassword = mPTPassword;
	}

	public static int setMPTPeriod(int mPTPeriod) {
		return MPTPeriod = mPTPeriod;
	}

	public static void setMPTServiceID(String mPTServiceID) {
		MPTServiceID = mPTServiceID;
	}

	public static void setMPTTimeStamp(String mPTTimeStamp) {
		MPTTimeStamp = mPTTimeStamp;
	}

	public static void setMPTUrl(String mPTUrl) {
		MPTUrl = mPTUrl;
	}

	public static void setmRechargeUrl(String mRechargeUrl) {
		ServerGlobal.mRechargeUrl = mRechargeUrl;
	}

	public static void setmSeparator(String mSeparator) {
		ServerGlobal.mSeparator = mSeparator;
	}

	public static void setmSetWriteLog(boolean mSetWriteLog) {
		ServerGlobal.mSetWriteLog = mSetWriteLog;
	}

	public static void setmTopupURL(String mTopupURL) {
		ServerGlobal.mTopupURL = mTopupURL;
	}

	public static void setMuserName(String muserName) {
		ServerGlobal.muserName = muserName;
	}

	public static void setmXREFPrefix(String mXREFPrefix) {
		ServerGlobal.mXREFPrefix = mXREFPrefix;
	}

	public static void setWalletURL(String walletURL) {
		ServerGlobal.walletURL = walletURL;
	}

	public static void setWriteLog(boolean mWLog) {
		ServerGlobal.mSetWriteLog = mWLog;
	}

	public static String getTaxUrl() {
		return taxUrl;
	}

	public static void setTaxUrl(String taxUrl) {
		ServerGlobal.taxUrl = taxUrl;
	}

	public static String getTaxUserID() {
		return taxUserID;
	}

	public static void setTaxUserID(String taxUserID) {
		ServerGlobal.taxUserID = taxUserID;
	}

	public static String getTaxNotifyUrl() {
		return taxNotifyUrl;
	}

	public static void setTaxNotifyUrl(String taxNotifyUrl) {
		ServerGlobal.taxNotifyUrl = taxNotifyUrl;
	}

	public static String getmGetCCDPaidUnPaidURL() {
		return mGetCCDPaidUnPaidURL;
	}

	public static void setmGetCCDPaidUnPaidURL(String mGetCCDPaidUnPaidURL) {
		ServerGlobal.mGetCCDPaidUnPaidURL = mGetCCDPaidUnPaidURL;
	}

	public static String getmGetCCDConnectTimeout() {
		return mGetCCDConnectTimeout;
	}

	public static void setmGetCCDConnectTimeout(String mGetCCDConnectTimeout) {
		ServerGlobal.mGetCCDConnectTimeout = mGetCCDConnectTimeout;
	}

	public static String getmGetCCDReadTimeout() {
		return mGetCCDReadTimeout;
	}

	public static void setmGetCCDReadTimeout(String mGetCCDReadTimeout) {
		ServerGlobal.mGetCCDReadTimeout = mGetCCDReadTimeout;
	}

	public static String getFcmUrl() {
		return fcmUrl;
	}

	public static void setFcmUrl(String fcmUrl) {
		ServerGlobal.fcmUrl = fcmUrl;
	}

	public static String getFcmMode() {
		return fcmMode;
	}

	public static void setFcmMode(String fcmMode) {
		ServerGlobal.fcmMode = fcmMode;
	}

	public static String getFcmServerApiKey() {
		return fcmServerApiKey;
	}

	public static void setFcmServerApiKey(String fcmServerApiKey) {
		ServerGlobal.fcmServerApiKey = fcmServerApiKey;
	}

	public static String getFcmTopicName() {
		return fcmTopicName;
	}

	public static void setFcmTopicName(String fcmTopicName) {
		ServerGlobal.fcmTopicName = fcmTopicName;
	}

	public static String getSkynetTokenUrl() {
		return skynetTokenUrl;
	}

	public static void setSkynetTokenUrl(String skynetTokenUrl) {
		ServerGlobal.skynetTokenUrl = skynetTokenUrl;
	}

	public static String getSkynetInstallItemUrl() {
		return skynetInstallItemUrl;
	}

	public static void setSkynetInstallItemUrl(String skynetInstallItemUrl) {
		ServerGlobal.skynetInstallItemUrl = skynetInstallItemUrl;
	}

	public static String getSkynetSubscriptionUrl() {
		return skynetSubscriptionUrl;
	}

	public static void setSkynetSubscriptionUrl(String skynetSubscriptionUrl) {
		ServerGlobal.skynetSubscriptionUrl = skynetSubscriptionUrl;
	}

	public static String getSkynetVoucherUrl() {
		return skynetVoucherUrl;
	}

	public static void setSkynetVoucherUrl(String skynetVoucherUrl) {
		ServerGlobal.skynetVoucherUrl = skynetVoucherUrl;
	}

	public static String getSkynetTopupUrl() {
		return skynetTopupUrl;
	}

	public static void setSkynetTopupUrl(String skynetTopupUrl) {
		ServerGlobal.skynetTopupUrl = skynetTopupUrl;
	}

	public static String getSkynetUsername() {
		return skynetUsername;
	}

	public static void setSkynetUsername(String skynetUsername) {
		ServerGlobal.skynetUsername = skynetUsername;
	}

	public static String getSkynetPassword() {
		return skynetPassword;
	}

	public static void setSkynetPassword(String skynetPassword) {
		ServerGlobal.skynetPassword = skynetPassword;
	}

	public static String getSkynetOrganisation() {
		return skynetOrganisation;
	}

	public static void setSkynetOrganisation(String skynetOrganisation) {
		ServerGlobal.skynetOrganisation = skynetOrganisation;
	}

	public static String getSkynetMinuteBeforeExpiry() {
		return skynetMinuteBeforeExpiry;
	}

	public static void setSkynetMinuteBeforeExpiry(String skynetMinuteBeforeExpiry) {
		ServerGlobal.skynetMinuteBeforeExpiry = skynetMinuteBeforeExpiry;
	}

	public static String getSkynetCatalogListUrl() {
		return skynetCatalogListUrl;
	}

	public static void setSkynetCatalogListUrl(String skynetCatalogListUrl) {
		ServerGlobal.skynetCatalogListUrl = skynetCatalogListUrl;
	}

	public static String getSkynetAvaliablePPVUrl() {
		return skynetAvaliablePPVUrl;
	}

	public static void setSkynetAvaliablePPVUrl(String skynetAvaliablePPVUrl) {
		ServerGlobal.skynetAvaliablePPVUrl = skynetAvaliablePPVUrl;
	}

	public static String getSkynetAddPPVUrl() {
		return skynetAddPPVUrl;
	}

	public static void setSkynetAddPPVUrl(String skynetAddPPVUrl) {
		ServerGlobal.skynetAddPPVUrl = skynetAddPPVUrl;
	}
//zzm
	public static String getSkynetMovie() {
		return skynetMovie;
	}

	public static void setSkynetMovie(String skynetMovie) {
		ServerGlobal.skynetMovie = skynetMovie;
	}
	
	public static String getMpssTPUrl() {
		return mpssTPUrl;
	}

	public static void setMpssTPUrl(String mpssTPUrl) {
		ServerGlobal.mpssTPUrl = mpssTPUrl;
	}

	public static String getMpssDPUrl() {
		return mpssDPUrl;
	}

	public static void setMpssDPUrl(String mpssDPUrl) {
		ServerGlobal.mpssDPUrl = mpssDPUrl;
	}

	public static String getMpssUsername() {
		return mpssUsername;
	}

	public static void setMpssUsername(String mpssUsername) {
		ServerGlobal.mpssUsername = mpssUsername;
	}

	public static String getMpssPassword() {
		return mpssPassword;
	}

	public static void setMpssPassword(String mpssPassword) {
		ServerGlobal.mpssPassword = mpssPassword;
	}

	public static String getMpssOrganisation() {
		return mpssOrganisation;
	}

	public static void setMpssOrganisation(String mpssOrganisation) {
		ServerGlobal.mpssOrganisation = mpssOrganisation;
	}

	public static String getMpssAPIKey() {
		return mpssAPIKey;
	}

	public static void setMpssAPIKey(String mpssAPIKey) {
		ServerGlobal.mpssAPIKey = mpssAPIKey;
	}

	public static String getMpssEGCUrl() {
		return mpssEGCUrl;
	}

	public static void setMpssEGCUrl(String mpssEGCUrl) {
		ServerGlobal.mpssEGCUrl = mpssEGCUrl;
	}

	public static String getMpssEGCAPIKey() {
		return mpssEGCAPIKey;
	}

	public static void setMpssEGCAPIKey(String mpssEGCAPIKey) {
		ServerGlobal.mpssEGCAPIKey = mpssEGCAPIKey;
	}

	public static String getMpssEGCUserRef() {
		return mpssEGCUserRef;
	}

	public static void setMpssEGCUserRef(String mpssEGCUserRef) {
		ServerGlobal.mpssEGCUserRef = mpssEGCUserRef;
	}

	public static String getmEnvPath1() {
		return mEnvPath1;
	}

	public static void setmEnvPath1(String mEnvPath1) {
		ServerGlobal.mEnvPath1 = mEnvPath1;
	}

	public static String getmEnvPath2() {
		return mEnvPath2;
	}

	public static void setmEnvPath2(String mEnvPath2) {
		ServerGlobal.mEnvPath2 = mEnvPath2;
	}

	public static String getmDriver() {
		return mDriver;
	}

	public static void setmDriver(String mDriver) {
		ServerGlobal.mDriver = mDriver;
	}

	public static String getmUrl() {
		return mUrl;
	}

	public static void setmUrl(String mUrl) {
		ServerGlobal.mUrl = mUrl;
	}

	public static String getmUserid() {
		return mUserid;
	}

	public static void setmUserid(String mUserid) {
		ServerGlobal.mUserid = mUserid;
	}

	public static String getmPassword() {
		return mPassword;
	}

	public static void setmPassword(String mPassword) {
		ServerGlobal.mPassword = mPassword;
	}

}
