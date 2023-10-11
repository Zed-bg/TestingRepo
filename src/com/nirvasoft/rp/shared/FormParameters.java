package com.nirvasoft.rp.shared;

import com.nirvasoft.rp.client.util.ClientUtil;

public class FormParameters {

	private static boolean pUniSearch=false;
	private static String pUniSearchText="";
	private static byte pDualTransactionSwitch;
	private static byte pForeignDualTransactionSwitch;
	private static byte pAccountSwitch;
	private static String pCardNo = "";
	private static String pAccountNo = "";
	private static String pCustomerID = "";
	private static String pIP ="";
	private static String pMenuID ="";
	private static String pAccountOpen ="";
	private static double pDenoAmt;
	private static String pCurrencyCode = "";
	private static String pCustomerSwitch = "";
	private static int pPOStatus = 0;
	
	private static String pCurrencyRate="";
	private static String pDenominationSetup="";
	private static String pFECTransaction="";
	private static byte pFECTransactionSwitch;
	private static byte pPOFormSwitch;		//PO
	private static byte pGCFormSwitch;
	private static byte pTransactionBrowserSwitch;
	private static String pRefNo = "";
	private static String pNoofChequeBooks = "1";
	private static String paccType;
	private static short pRefTrialType;
	private static String pYearEndDate;
	private static byte pMultiTransferFormSwitch;
	private static String pProductType = "";
	private static String pBranchCode = "";
	private static String pStatus = "";
	private static byte pServiceChargesSetupSwitch;
	private static byte pCommitmentFeesSetupSwitch;
	private static String pODAndLA = "";
	private static boolean pOD = false;
	private static byte pTransactionSwitch;//EYMH
	private static boolean pLA = false;
	private static int pBarType = 0;
	private static byte pAccCloseEnquiry;
	//mmmyint
	private static String pOthBKCode = "";
	private static String pOthBCCode = "";
	/*// For Collateral
	private static String pCollateralType = "";*/
	
	//FEC
	private static byte pFECSwitch;
	private static String  denoName;
	private static int  denoQty;
	private static String  CurCode;
	private static String  sessionID;
	private static String  counterID;
	private static boolean isHP=false;
	
	public static void fecDeposit(){pFECSwitch=0;}
	public static void fecWidthdrawl(){pFECSwitch=1;}
	public static void fecBuy(){pFECSwitch=2;}
	public static void fecSell(){pFECSwitch=3;}
	
	public static boolean isFECDeposit(){return pFECSwitch==0;}
	public static boolean isFECWithdrawl(){return pFECSwitch==1;}
	public static boolean isFECBuy(){return pFECSwitch==2;}
	public static boolean isFECSell(){return pFECSwitch==3;}
	//FEC end
	
	public static void passBookTransactionBrowser() {pTransactionBrowserSwitch = 1;}
	public static void normalTransactionBrowser() {pTransactionBrowserSwitch = 0;}
	
	// For Getting First IP
	private static Boolean pFirstIp = false;
	
	//HMT 07/02/2012
	private static int pMenuTab = 0;
	private static boolean pIsCommand = false;
	private static String pReportUrl = "";
	private static String piRSSvrUrl = "";
	
	//HMT 22/02/2012
	private static long pRelationshipKey = 0;
	private static String pParentID = "";
	private static int pFormType = 0;
	
	// For Collateral ZZM
	private static String pCollateralType = "";
	
	public static String getCollateralType() {
		return pCollateralType;
	}
	public static void setCollateralType(String pCollateralType) {
		FormParameters.pCollateralType = pCollateralType;
	}
	
	public static long getRelationship() {
		return pRelationshipKey;
	}
	public static void setRelationship(long pRelationship) {
		FormParameters.pRelationshipKey = pRelationship;
	}		
	public static String getParentID() {
		return pParentID;
	}
	public static void setParentID(String pParentID) {
		FormParameters.pParentID = pParentID;
	}
	
	public static boolean getIsCommand() {
		return pIsCommand;
	}
	public static void setIsCommand(boolean pIsCommand) {
		FormParameters.pIsCommand = pIsCommand;
	}
	public static int getMenuTab() {
		return pMenuTab;
	}
	public static void setMenuTab(int pMenuTab) {
		FormParameters.pMenuTab = pMenuTab;
	}

	public static boolean isUniSearch(){boolean ret=pUniSearch; return ret;}
	public static void doneUniSearch(){pUniSearch=false;}
	
	public static String getUniSearch(){String ret=pUniSearchText; pUniSearchText=""; return ret;}
	public static void setUniSearch(String p){pUniSearchText=p;pUniSearch=true;}

	public static byte getDualTransactionSwitch(){return pDualTransactionSwitch;}
	public static void setDualTransactionSwitch(byte p){pDualTransactionSwitch=p;}
	public static void dualDeposit(){pDualTransactionSwitch= 0;}
	public static void dualWithdraw(){pDualTransactionSwitch= 1;}
	public static void dualTransfer(){pDualTransactionSwitch= 2;}
	public static boolean isDualDeposit(){return pDualTransactionSwitch==0;}
	public static boolean isDualWithdraw(){return pDualTransactionSwitch==1;}
	public static boolean isDualTransfer(){return pDualTransactionSwitch==2;}
	
	public static boolean isForeignDualDeposit(){return pForeignDualTransactionSwitch==0;}
	public static boolean isForeignDualWithdraw(){return pForeignDualTransactionSwitch==1;}
	public static boolean isForeignDualTransfer(){return pForeignDualTransactionSwitch==2;}
	
	public static byte getAccountSwitch(){return pAccountSwitch;}
	public static void setAccountSwitch(byte p){pAccountSwitch=p;}
	public static void Account(){pAccountSwitch= 0;}
	public static void CardAccount(){pAccountSwitch= 1;}
	public static boolean isAccount(){return pAccountSwitch==0;}
	public static boolean isCardAccount(){return pAccountSwitch==1;}
	public static String getCardNo(){return pCardNo;}
	public static void setCardNo(String p){pCardNo=p;}
	
	public static String getAccountNo(){return pAccountNo;}
	public static void setAccountNo(String p){pAccountNo=p;}
	
	public static String getAccountOpen(){return pAccountOpen;}
	public static void setAccountOpen(String p){pAccountOpen=p;}
	
	public static String getCustomerID(){return pCustomerID;}
	public static void setCustomerID(String p){pCustomerID=p;}

	public static String getIP(){return pIP;}
	public static void setIP(String p){pIP=p;}

	public static Boolean getFirstIP(){return pFirstIp;}
	public static void setFirstIP(Boolean p){pFirstIp=p;}
	
	public static String getMenuID(){return pMenuID;}
	public static void setMenuID(String p){pMenuID=p;}
	
	public static double getDenoAmt(){return pDenoAmt;}
	public static void setDenoAmt(double p){pDenoAmt = p;}
	
	public static String getCurrencyCode(){return pCurrencyCode;}
	public static void setCurrencyCode(String p){pCurrencyCode = p;}
	
	public static String getCustomerSwitch(){return pCustomerSwitch;}
	public static void setCustomerSwitch(String p){pCustomerSwitch= p;}
	
	//FEC
	public static void setCurrencyRate(String pCurrencyRate) {
		FormParameters.pCurrencyRate = pCurrencyRate;
	}
	public static String getCurrencyRate() {
		return pCurrencyRate;
	}
	public static void setDenominationSetup(String pDenominationSetup) {
		FormParameters.pDenominationSetup = pDenominationSetup;
	}
	public static String getDenominationSetup() {
		return pDenominationSetup;
	}
	public static void setFECTransaction(String pFECTransaction) {
		FormParameters.pFECTransaction = pFECTransaction;
	}
	public static String getFECTransaction() {
		return pFECTransaction;
	}
	public static byte getFECTransactionSwitch(){return pFECTransactionSwitch;}
	public static void setFECTransactionSwitch(byte p){pFECTransactionSwitch=p;}
	public static void feBuyByCash(){pFECTransactionSwitch = 0;}
	public static void feBuyByDomestic(){pFECTransactionSwitch = 1;}
	public static void feSellByCash(){pFECTransactionSwitch = 2;}
	public static void feSellByAccount(){pFECTransactionSwitch = 3;}
	public static void feDeposit(){pFECTransactionSwitch = 4;}
	public static void feWithdrawal(){pFECTransactionSwitch = 5;}
	
	public static boolean isFEBuyByCash(){return pFECTransactionSwitch == 0;}
	public static boolean isFEBuyByDomestic(){return pFECTransactionSwitch == 1;}
	public static boolean isFESellByCash(){return pFECTransactionSwitch == 2;}
	public static boolean isFESellByAccount(){return pFECTransactionSwitch == 3;}
	public static boolean isFEDepoist(){return pFECTransactionSwitch == 4;}
	public static boolean isFEWithdrawal(){return pFECTransactionSwitch == 5;}

	//TTT_PaymentOrder
	public static byte getPOSwitch(){return pPOFormSwitch;}
	public static void setPOSwitch(byte p){pPOFormSwitch = p;}
	public static void poIssue(){pPOFormSwitch = 0;}
	public static void poEncach(){pPOFormSwitch = 1;}
	public static void poNoChange() {pPOFormSwitch = 2;}
	
	public static boolean isPOIssue(){return pPOFormSwitch == 0;}
	public static boolean isPOEncash(){return pPOFormSwitch == 1;}
	public static boolean isPONoChange() {return pPOFormSwitch == 2;}
	
	//HNNS GC 
	public static byte getGCSwitch(){return pGCFormSwitch;}
	public static void setGCSwitch(byte p){pGCFormSwitch = p;}
	public static void GCIssue(){pGCFormSwitch = 0;}
	public static void GCEncash(){pGCFormSwitch = 1;}
	public static void GCChange(){pGCFormSwitch = 2;}//NSM
	
	public static boolean isGCIssue(){return pGCFormSwitch == 0;}
	public static boolean isGCEncash(){return pGCFormSwitch == 1;}
	public static boolean isGCChange(){return pGCFormSwitch == 2;}//NSM
	
	public static boolean isPassBookTransactionBrowser(){return pTransactionBrowserSwitch == 1;}
	public static boolean isNormalTransactionBroser() {return pTransactionBrowserSwitch == 0;}
	
	public static String getReportUrl() {return pReportUrl;}
	public static void setReportUrl(String p) {
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("RPT").getN1() ==1) {
		     p=p.replaceFirst(ClientUtil.getUrl()+"/", ""); // http://192.168.1.20:8080/iRS_Server		    
		}
		pReportUrl = p;	
	}
	
	public static void setReportUrlSignature(String p) {
		pReportUrl = p;	
	}
	
	public static String getiRSSvrUrl() {return piRSSvrUrl;}
	
	public static void setiRSSvrUrl(String p) {
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("RPT").getN1() ==1) {
		     p=p.replaceFirst(ClientUtil.getUrl()+"/", ""); // http://192.168.1.20:8080/iRS_Server		    
		}
		piRSSvrUrl = p;  // http:///iRS_Server
		//System.out.println(piRSSvrUrl);
		//piRSSvrUrl = p;
	
	}
	public static String getRefNo() {
		return pRefNo;
	}
	public static void setRefNo(String pRefNo) {
		FormParameters.pRefNo = pRefNo;
	}
	public static String getNoofChequeBooks() {
		return pNoofChequeBooks;
	}
	public static void setNoofChequeBooks(String pNoofChequeBooks) {
		FormParameters.pNoofChequeBooks = pNoofChequeBooks;
	}
	public static String getaccType() {
		return paccType;
	}
	public static void setaccType(String paccType) {
		FormParameters.paccType = paccType;
	}
	public static short getRefTrialType() {
		return pRefTrialType;
	}
	public static void setRefTrialType(short pRefTrialType) {
		FormParameters.pRefTrialType = pRefTrialType;
	}
	public static String getYearEndDate() {
		return pYearEndDate;
	}
	public static void setYearEndDate(String pYearEndDate) {
		FormParameters.pYearEndDate = pYearEndDate;
	}	
	public static void MultiTransfer(){ pMultiTransferFormSwitch = 0;}
	public static void ClearingTransfer(){ pMultiTransferFormSwitch = 1;}
	public static boolean isMultiTransfer(){return pMultiTransferFormSwitch == 0;}
	public static boolean isClearingTransfer(){return pMultiTransferFormSwitch == 1;}
	
	public static String getProductType() {
		return pProductType;
	}
	public static void setProductType(String pProductType) {
		FormParameters.pProductType = pProductType;
	}
	public static String getBranchCode() {
		return pBranchCode;
	}
	public static void setBranchCode(String pBranchCode) {
		FormParameters.pBranchCode = pBranchCode;
	}
	public static String getStatus(){
		return pStatus;
	}
	public static void setStatus(String pStatus){
		FormParameters.pStatus = pStatus;
	}

	/*public static String getCollateralType() {
		return pCollateralType;
	}
	public static void setCollateralType(String pCollateralType) {
		FormParameters.pCollateralType = pCollateralType;
	}*/

	
	public static void ODServiceChargesSetup(){pServiceChargesSetupSwitch = 0;}
	public static void LoanServiceChargesSetup(){pServiceChargesSetupSwitch = 1;}
	
	public static boolean isODServiceChargesSetup(){return pServiceChargesSetupSwitch == 0;}
	public static boolean isLoanServiceChargesSetup(){return pServiceChargesSetupSwitch == 1;}
	
	public static void ODCommitmentFeesSetup(){pCommitmentFeesSetupSwitch = 0;}
	public static void LoanCommitmentFeesSetup(){pCommitmentFeesSetupSwitch = 1;}
	
	public static boolean isODCommitmentFeesSetup(){return pCommitmentFeesSetupSwitch == 0;}
	public static boolean isLoanCommitmentFeesSetup(){return pCommitmentFeesSetupSwitch == 1;}
	
	public static String getODAndLA() {
		return pODAndLA;
	}
	public static void setODAndLA(String pODAndLA) {
		FormParameters.pODAndLA = pODAndLA;
	}
	public static boolean isOD() {
		return pOD;
	}
	public static void setOD(boolean pOD) {
		FormParameters.pOD = pOD;
	}
	public static boolean isLA() {
		return pLA;
	}
	public static void setLA(boolean pLA) {
		FormParameters.pLA = pLA;
	}	
	public static int getPOStatus() {
		return pPOStatus;
	}
	public static void setPOStatus(int pPOStatus) {
		FormParameters.pPOStatus = pPOStatus;
	}
	//EYMH
	public static byte getpTransactionSwitch() {return pTransactionSwitch;}
	public static void setpTransactionSwitch(byte pTransactionSwitch) {FormParameters.pTransactionSwitch = pTransactionSwitch;}
	public static void TransactionList(){pTransactionSwitch= 1;}
	public static boolean isTransactionList(){return pTransactionSwitch==1;}
	public static void TransGLUpdate(){pTransactionSwitch=2;}
	public static boolean isTransGLUpdate(){return pTransactionSwitch==2;}
	public static int getBarType() {return pBarType;}
	public static void setBarType(int pBarType) {FormParameters.pBarType = pBarType;}
	public static int getFormType() {return pFormType;}
	public static void setFormType(int pFormType) {FormParameters.pFormType = pFormType;}
	
	public static void AccCloseEnquiry(){pAccCloseEnquiry = 1;}
	public static boolean isAccCloseEnquiry(){return pAccCloseEnquiry==1;}
	public static void AccClose(){pAccCloseEnquiry = 0;}
	public static boolean isAccClose(){return pAccCloseEnquiry==0;}
	//mmmyint
	public static String setOthBKCode(String bkCode){return pOthBKCode = bkCode;}
	public static String getOthBKCode(){ return pOthBKCode;}
	public static String setOthBCCode(String bcCode){return pOthBCCode = bcCode;}
	public static String getOthBCCode(){ return pOthBCCode;}
	
	private static boolean showPaidChk = false;
	public static boolean isShowPaidChk() {return showPaidChk;}
	public static void setShowPaidChk(boolean showPaidChk) { FormParameters.showPaidChk = showPaidChk;
	}
	public static String getDenoName() { return denoName; }
	public static void setDenoName(String denoName) { FormParameters.denoName = denoName; }
	public static int getDenoQty() { return denoQty; }
	public static void setDenoQty(int denoQty) { FormParameters.denoQty = denoQty; }
	public static String getCurCode() { return CurCode; }
	public static void setCurCode(String curCode) { CurCode = curCode; }
	public static String getSessionID() {  return sessionID; }
	public static void setSessionID(String sessionID) { FormParameters.sessionID = sessionID; }
	public static String getCounterID() { return counterID; }
	public static void setCounterID(String counterID) { FormParameters.counterID = counterID; }
	public static boolean isHP() {	return isHP;}
	public static void setHP(boolean isHP) {FormParameters.isHP = isHP;}
	
}


