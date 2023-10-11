package com.nirvasoft.rp.shared;

import java.io.Serializable;
import java.util.ArrayList;

/* 
TUN THURA THET 2011 04 21
*/
public class TransactionData implements Serializable {
	private static final long serialVersionUID = -4515767800629231541L;
	
	public TransactionData()
	{
		ClearProperty();
	}
	
	private long pTransactionNumber;
	private long pTransactionReference;
	private int pTransactionType;
	private String pTransactionDate;
	private String pTransactionTime;
	private String pEffectiveDate;
	private String pBranchCode;
	private String pAccountNumber;
	private String pReferenceNumber;  //check number
	private double pAmount;
	private int pTransactionCount;
	public String pRemark;
	private String pName = "";
	private String pSupervisorId = "";
	public double pTrRate = 0;
	public String pWorkstation;
	public int pSerial;
	public String pUserID;
	public String pAuthorizerID;
	public String pDescription;
	public String pCurrencyCode;	
	public String pCommCurrencyCode;
	public double pCurrencyRate;
	public double pCommCurrencyRate;
	public double pPrevBalance;
	public String pPreviousDate;
	public String pContraDate;
	public int pStatus;
	public String pAccRef;
	public String pSubRef;
	public int pSystemCode;
	public ArrayList<DenominationData> pDenorminations;
	private String pCashInHandGL;
	private String pProductGL;
	private double pBaseAmount;
	private boolean pIsByForceOD;
	private String pTrCurCode;
	private double pTrAmount;
	private double pTrPrevBalance;
	private double pN1;
	private double pN2;
	private double pN3;
	private String pT1;
	private String pT2;
	private String pT3;
	private String pFromBatchProcess;

	private double pDrAmt;
	private double pDrBaseAmt;
	private double pCrAmt;
	private double pCrBaseAmt;
	private String pVoucherSerialNo;
	private int pDenoStatus;
	private int pDenoSrNo;
	private String pRefNo;
	private boolean pIsMobile;
	private int pProcessType;
	private String pContraRef;//ODByForce form
	private String pType;
	ArrayList<String> lstUniEntryDate = new ArrayList<String>();
	public boolean checkbecAccount=false;
	public boolean checkbecCrAccount=false;
	public int pOfflineType;
	
	public int Chqcertified;//mtdk
	public String Certified="";

	//mmmyint 07.08.2018 icbs 3.0 copy
	private boolean chkInclusive;
	private boolean isComTransaction;
	
	// For Detail Trial 
	private double pDebit = 0;
	private double pCredit = 0;
	private double pBaseDebit = 0;
	private double pBaseCredit = 0;
	
	private double pBaseOpening = 0;
	private double pBaseClosing = 0;
	private int chkowncr=0;
	private double Comm;
	private double trRate;
	
	private String productType;
	private String accType;
	private int accStatus;
	private double avaliableBal;
	private String accShortCode;
	
	//For FX Acc Transaction 
	private String pDrAccNo;
	private String pCrAccNo;
	private String pDrCurCode;
	private String pCrCurCode;
	private double pDrCurRate;
	private double pCrCurRate;
	private double pCom2;
	private String pCom2CurCode;
	private String pDrComGL;
	private double unit;
	private boolean isIBTDeposit;
	private boolean isIBTWithdraw;
	
	private boolean gl;
	
	public String getDrCurCode() {
		return pDrCurCode;
	}
	public void setDrCurCode(String pDrCurCode) {
		this.pDrCurCode = pDrCurCode;
	}
	public String getCrCurCode() {
		return pCrCurCode;
	}
	public void setCrCurCode(String pCrCurCode) {
		this.pCrCurCode = pCrCurCode;
	}
	public ArrayList<DenominationData> getDenominations(){return pDenorminations;}
	public void setDenominations(ArrayList<DenominationData> p){pDenorminations =p;}
	
	public long getTransactionNumber(){return pTransactionNumber;}
	public void setTransactionNumber(long p){pTransactionNumber =p;}
	public long getTransactionReference(){return pTransactionReference;}
	public void setTransactionReference(long p){pTransactionReference =p;}
	public int getTransactionType(){return pTransactionType;}
	public void setTransactionType(int p){pTransactionType =p;}
	public String getTransactionDate(){return pTransactionDate;}
	public void setTransactionDate(String p){pTransactionDate =p;}
	public String getTransactionTime(){return pTransactionTime;}
	public void setTransactionTime(String p){pTransactionTime =p;}
	public String getEffectiveDate(){return pEffectiveDate;}
	public void setEffectiveDate(String p){pEffectiveDate =p;}
	public String getBranchCode(){return pBranchCode;}
	public void setBranchCode(String p){pBranchCode =p;}
	public String getAccountNumber(){return pAccountNumber;}
	public void setAccountNumber(String p){pAccountNumber =p;}
	public String getReferenceNumber(){return pReferenceNumber;}
	public void setReferenceNumber(String p){pReferenceNumber =p;}
	public double getAmount(){return pAmount;}
	public void setAmount(double p){pAmount =p;}
	public String getRemark(){return pRemark;}
	public void setRemark(String p){pRemark =p;}


	public int getStatus(){return pStatus;}
	public void setStatus(int p){pStatus =p;}
	
	public double getPreviousBalance(){return pPrevBalance;}
	public void setPreviousBalance(double p){pPrevBalance =p;}
	public double getCurrencyRate(){return pCurrencyRate;}
	public void setCurrencyRate(double p){pCurrencyRate =p;}
	public double getCommCurrencyRate(){return pCommCurrencyRate;}
	public void setCommCurrencyRate(double p){pCommCurrencyRate =p;}
	
	public int getSerial(){return pSerial;}
	public void setSerial(int p){pSerial =p;}
	public int getSystemCode(){return pSystemCode;}
	public void setSystemCode(int p){pSystemCode =p;}
	
	public String getWorkstation(){return pWorkstation;}
	public void setWorkstation(String p){pWorkstation =p;}
	public String getUserID(){return pUserID;}
	public void setUserID(String p){pUserID =p;}
	public String getAuthorizerID(){return pAuthorizerID;}
	public void setAuthorizerID(String p){pAuthorizerID =p;}
	public String getDescription(){return pDescription;}
	public void setDescription(String p){pDescription =p;}
	public String getCurrencyCode(){return pCurrencyCode;}
	public void setCurrencyCode(String p){pCurrencyCode =p;}
	public String getCommCurrencyCode(){return pCommCurrencyCode;}
	public void setCommCurrencyCode(String p){pCommCurrencyCode =p;}
	public String getContraDate(){return pContraDate;}
	public void setContraDate(String p){pContraDate =p;}
	public String getPreviousDate(){return pPreviousDate;}
	public void setPreviousDate(String p){pPreviousDate =p;}
	public String getAccRef(){return pAccRef;}
	public void setAccRef(String p){pAccRef =p;}
	public String getSubRef(){return pSubRef;}
	public void setSubRef(String p){pSubRef =p;}
	
	public String getCashInHandGL() {return pCashInHandGL;}
	public void setCashInHandGL(String p) {pCashInHandGL = p;}
	
	public String getProductGL() {return pProductGL;}
	public void setProductGL(String p) {pProductGL = p;}
	
	public double getBaseAmount() {return pBaseAmount;}
	public void setBaseAmount(double p) {pBaseAmount = p;}
	
	public boolean getIsByForceOD() {return pIsByForceOD;}
	public void setIsByForceOD(boolean pIsByForceOD) {this.pIsByForceOD = pIsByForceOD;}
	
	public String getRefNo() {
		return pRefNo;
	}
	public void setRefNo(String pRefNo) {
		this.pRefNo = pRefNo;
	}
	public int getDenoSrNo() {
		return pDenoSrNo;
	}
	public void setDenoSrNo(int pDenoSrNo) {
		this.pDenoSrNo = pDenoSrNo;
	}
	public String getTrCurCode() {
		return pTrCurCode;
	}
	public void setTrCurCode(String pTrCurCode) {
		this.pTrCurCode = pTrCurCode;
	}
	
	public String getpFromBatchProcess() {
		return pFromBatchProcess;
	}
	public void setpFromBatchProcess(String pFromBatchProcess) {
		this.pFromBatchProcess = pFromBatchProcess;
	}
	//mtdk 06092018
	public int getChqcertified() {return Chqcertified;}
	public void setChqcertified(int chqcertified) {Chqcertified = chqcertified;}
	public String getCertified() {return Certified;}
	public void setCertified(String certified) {Certified = certified;}
	//mtdk
	private void ClearProperty()
	{
		pTransactionReference=0;
		pTransactionType=0;
		pTransactionDate=SharedUtil.formatDDMMYYYY2MIT("01/01/1900");
		pTransactionTime=SharedUtil.formatDDMMYYYY2MIT("01/01/1900");
		pEffectiveDate=SharedUtil.formatDDMMYYYY2MIT("01/01/1900");
		pBranchCode="001";
		pAccountNumber="";
		pReferenceNumber="";  //check number
		pAmount=0;
		pRemark="";
		
		pWorkstation="001";
		pSerial=0;
		pUserID="";
		pAuthorizerID="";
		pDescription="";
		pCurrencyCode="MMK";
		pCommCurrencyCode="MMK";
		pCurrencyRate=1;
		pCommCurrencyRate=1;
		pPrevBalance=0;
		pPreviousDate=SharedUtil.formatDDMMYYYY2MIT("01/01/1900");;
		pContraDate=SharedUtil.formatDDMMYYYY2MIT("01/01/1900");;
		pStatus=0;
		pAccRef="";
		pSubRef="";
		pSystemCode=0;
		pCashInHandGL = "";
		pProductGL = "";
		pBaseAmount = 0;
		setIsByForceOD(false);
		setTrCurCode("");
		pTrAmount = 0;
		pTrPrevBalance = 0;
		pTransactionCount = 0;
		
		pN1 = 0;
		pN2 = 0;
		pN3 = 0;
		pT1 = "";
		pT2 = "";
		pT3 = "";
		
		pDrAmt = 0;
		pDrBaseAmt = 0;
		pCrAmt = 0;
		pCrBaseAmt = 0;		
		pVoucherSerialNo = "";
		pDenoStatus = -1;
		pDenoSrNo = 0;
		pRefNo = "";
		pIsMobile = false;
		pProcessType = 0;
		pContraRef = "";
		pType = "";
		pFromBatchProcess="";
		pOfflineType = 0;
		Certified="";//mtdk

		pDebit = 0;
		pCredit = 0;
		pBaseDebit = 0;
		pBaseCredit = 0;
		isComTransaction = false;
		pBaseOpening = 0;
		pBaseClosing = 0;
		trRate = 0.00;
		
		productType ="";
		accType="";
		accStatus=0;
		avaliableBal=0;
		accShortCode = "";
		pDrAccNo = "";
		pCrAccNo = "";
		unit = 0.00;
		isIBTDeposit= false;
		isIBTWithdraw = false;
		
		gl = false;
		
	}
	public double getTrAmount() {
		return pTrAmount;
	}
	public void setTrAmount(double pTrAmount) {
		this.pTrAmount = pTrAmount;
	}
	public double getTrPrevBalance() {
		return pTrPrevBalance;
	}
	public void setTrPrevBalance(double pTrPrevBalance) {
		this.pTrPrevBalance = pTrPrevBalance;
	}
	public int getTransactionCount() {
		return pTransactionCount;
	}
	public void setTransactionCount(int transactionCount) {
		pTransactionCount = transactionCount;
	}
	public double getN1() {
		return pN1;
	}
	public void setN1(double pN1) {
		this.pN1 = pN1;
	}
	
	public double getDrAmt() {
		return pDrAmt;
	}
	public void setDrAmt(double pDrAmt) {
		this.pDrAmt = pDrAmt;
	}
	public double getDrBaseAmt() {
		return pDrBaseAmt;
	}
	public void setDrBaseAmt(double pDrBaseAmt) {
		this.pDrBaseAmt = pDrBaseAmt;
	}
	public double getCrAmt() {
		return pCrAmt;
	}
	public void setCrAmt(double pCrAmt) {
		this.pCrAmt = pCrAmt;
	}
	public double getCrBaseAmt() {
		return pCrBaseAmt;
	}
	public void setCrBaseAmt(double pCrBaseAmt) {
		this.pCrBaseAmt = pCrBaseAmt;
	}
	public String getVoucherSerialNo() {
		return pVoucherSerialNo;
	}
	public void setVoucherSerialNo(String pVoucherSerialNo) {
		this.pVoucherSerialNo = pVoucherSerialNo;
	}
	public int getDenoStatus() {
		return pDenoStatus;
	}
	public void setDenoStatus(int pDenoStatus) {
		this.pDenoStatus = pDenoStatus;
	}
	public double getN2() {
		return pN2;
	}
	public void setN2(double pN2) {
		this.pN2 = pN2;
	}
	public double getN3() {
		return pN3;
	}
	public void setN3(double pN3) {
		this.pN3 = pN3;
	}
	public String getT1() {
		return pT1;
	}
	public void setT1(String pT1) {
		this.pT1 = pT1;
	}
	public String getT2() {
		return pT2;
	}
	public void setT2(String pT2) {
		this.pT2 = pT2;
	}
	public String getT3() {
		return pT3;
	}
	public void setT3(String pT3) {
		this.pT3 = pT3;
	}
	public boolean getIsMobile() {	return pIsMobile;	}
	public void setIsMobile(boolean pIsMobile) {	this.pIsMobile = pIsMobile;	}
	
	public int getProcessType() {
		return pProcessType;
	}
	public void setProcessType(int pProcessType) {
		this.pProcessType = pProcessType;
	}
	public String getContraRef() {
		return pContraRef;
	}
	public void setContraRef(String pContraRef) {
		this.pContraRef = pContraRef;
	}
	public String getType() {
		return pType;
	}
	public void setType(String pType) {
		this.pType = pType;
	}	
	public ArrayList<String> getLstUniEntryDate() { return lstUniEntryDate; }

	public void setLstUniEntryDate(ArrayList<String> lstUniEntryDate) {	this.lstUniEntryDate = lstUniEntryDate;	}
	public boolean isCheckbecAccount() { 
		return checkbecAccount;
	}
	public void setCheckbecAccount(boolean checkbecAccount) {
		this.checkbecAccount = checkbecAccount;
	}
	public boolean isCheckbecCrAccount() {
		return checkbecCrAccount;
	}
	public void setCheckbecCrAccount(boolean checkbecCrAccount) {
		this.checkbecCrAccount = checkbecCrAccount;
	}
	public int getOfflineType() {
		return pOfflineType;
	}
	public void setOfflineType(int pOfflineType) {
		this.pOfflineType = pOfflineType;
	}
	
	public boolean getchkInclusive() {return chkInclusive;}
	public void setchkInclusive(boolean chkInclusive) {this.chkInclusive = chkInclusive;}

	public double getBaseOpening() {
		return pBaseOpening;
	}
	public void setBaseOpening(double pBaseOpening) {
		this.pBaseOpening = pBaseOpening;
	}
	
	public double getBaseClosing() {
		return pBaseClosing;
	}
	public void setBaseClosing(double pBaseClosing) {
		this.pBaseClosing = pBaseClosing;
	}
	public double getDebit() {
		return pDebit;
	}
	public void setDebit(double pDebit) {
		this.pDebit = pDebit;
	}
	public double getCredit() {
		return pCredit;
	}
	public void setCredit(double pCredit) {
		this.pCredit = pCredit;
	}	
	public double getBaseDebit() {
		return pBaseDebit;
	}
	public void setBaseDebit(double pBaseDebit) {
		this.pBaseDebit = pBaseDebit;
	}
	public double getBaseCredit() {
		return pBaseCredit;
	}
	public void setBaseCredit(double pBaseCredit) {
		this.pBaseCredit = pBaseCredit;
	}
	public int getChkowncr() {
		return chkowncr;
	}
	public void setChkowncr(int chkowncr) {
		this.chkowncr = chkowncr;
	}
	public boolean isComTransaction() {
		return isComTransaction;
	}
	public void setComTransaction(boolean isComTransaction) {
		this.isComTransaction = isComTransaction;
	}
	public double getComm() {
		return Comm;
	}
	public void setComm(double comm) {
		Comm = comm;
	}
	public double getTrRate() {
		return trRate;
	}
	public void setTrRate(double trRate) {
		this.trRate = trRate;
	}
	
	public TransactionData(String AccNumber, String Name, String Description, double Amount, int TransType,
			String TellerId,String TerminalId,String CounterId, String SubRef,String Remark,long TransNo,
			String TrCurCode,double TrRate,String EffDate,long TransRef,double TrAmount){
		this.pAccountNumber = AccNumber;
		this.setName(Name);
		this.pDescription = Description;
		this.pAmount = Amount;
		this.pTransactionType = TransType;
		this.pUserID = TellerId;
		this.pWorkstation = TerminalId;
		this.setSupervisorId(CounterId);
		this.pSubRef = SubRef;
		this.pRemark = Remark;
		this.pTransactionNumber=TransNo;
		this.pTrCurCode=TrCurCode;
		this.pTrRate=TrRate;
		this.pEffectiveDate=EffDate;
		this.pTransactionReference=TransRef;
		this.unit = TrAmount;
	}
	public String getName() {
		return pName;
	}
	public void setName(String Name) {
		this.pName = Name;
	}
	public String getSupervisorId() {
		return pSupervisorId;
	}
	public void setSupervisorId(String SupervisorId) {
		this.pSupervisorId = SupervisorId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public int getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(int accStatus) {
		this.accStatus = accStatus;
	}
	public double getAvaliableBal() {
		return avaliableBal;
	}
	public void setAvaliableBal(Double avaliableBal) {
		this.avaliableBal = avaliableBal;
	}
	public String getAccShortCode() {
		return accShortCode;
	}
	public void setAccShortCode(String accShortCode) {
		this.accShortCode = accShortCode;
	}
	public String getDrAccNo() {
		return pDrAccNo;
	}
	public void setDrAccNo(String pDrAccNo) {
		this.pDrAccNo = pDrAccNo;
	}
	public String getCrAccNo() {
		return pCrAccNo;
	}
	public void setCrAccNo(String pCrAccNo) {
		this.pCrAccNo = pCrAccNo;
	}
	public double getDrCurRate() {
		return pDrCurRate;
	}
	public void setDrCurRate(double pDrCurRate) {
		this.pDrCurRate = pDrCurRate;
	}
	public double getCrCurRate() {
		return pCrCurRate;
	}
	public void setCrCurRate(double pCrCurRate) {
		this.pCrCurRate = pCrCurRate;
	}
	public double getCom2() {
		return pCom2;
	}
	public void setCom2(double pCom2) {
		this.pCom2 = pCom2;
	}
	public String getCom2CurCode() {
		return pCom2CurCode;
	}
	public void setCom2CurCode(String pCom2CurCode) {
		this.pCom2CurCode = pCom2CurCode;
	}
	public String getDrComGL() {
		return pDrComGL;
	}
	public void setDrComGL(String pDrComGL) {
		this.pDrComGL = pDrComGL;
	}
	public double getUnit() {
		return unit;
	}
	public void setUnit(double unit) {
		this.unit = unit;
	}
	public boolean isIBTDeposit() {
		return isIBTDeposit;
	}
	public void setIBTDeposit(boolean isIBTDeposit) {
		this.isIBTDeposit = isIBTDeposit;
	}
	public boolean isIBTWithdraw() {
		return isIBTWithdraw;
	}
	public void setIBTWithdraw(boolean isIBTWithdraw) {
		this.isIBTWithdraw = isIBTWithdraw;
	}
	public boolean isGl() {
		return gl;
	}
	public void setGl(boolean gl) {
		this.gl = gl;
	}	
}
