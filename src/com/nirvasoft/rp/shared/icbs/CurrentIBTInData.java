package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class CurrentIBTInData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1869875189185567196L;
	private String pMainKey;
	private Short pIBTType;
	private Short pTransType;
	private String pTransDate;
	private String pAuthorisecode;
	private int pOnlineSerialNo;
	private String pCrAccNumber;
	private String DrAccNumber;
	private String pFromBranch;
	private String pToBranch;
	private double pAmount;
	private int pTransref;
	private int pTransno;
	private double pPreviousBal;
	private short pStatus;
	private String pChequeNo;
	private String pToName;
	private String pToNrc;
	private String pSendTime;
	private String pReceiveTime;
	private String pFromName;
	private String pFromNrc;
	private short pPrintStatus;
	private String pSaveTime;
	private String pPostTime;
	private String pCompleteTime;
	private String pByear;
	private String pRemark;
	private String pT1;
	private int pN1;
	private long pAutoKey;
	private String pOnlSno;
	private String pTestKey;
	private int pPrefixed;
	private String pPfixedDate;
	private String pBCode;
	private String pCurrencyCode;
	private double pCurrencyRate;
	private String pWorkStation;
	private String pUserId;
	private double pIBTComission;
	private double pOnlineFees;
	private String pComGl;
	private String pOnlineFeesGl;
	private String pDrComGl;
	private String pDrOnlineFeesGl;
	private String pT2;
	private int pN2;
	private String pT3;
	private int pN3;
	private String pT4;
	private int pN4;
	private int pComTransRef;

	public CurrentIBTInData() {
		clearProperty();
	}

	private void clearProperty() {
		pMainKey = "";
		pIBTType = 0;
		pTransType = 0;
		pTransDate = "";
		pAuthorisecode = "";
		pOnlineSerialNo = 0;
		pCrAccNumber = "";
		DrAccNumber = "";
		pFromBranch = "";
		pToBranch = "";
		pAmount = 0;
		pTransref = 0;
		pTransno = 0;
		pPreviousBal = 0;
		pStatus = 0;
		pChequeNo = "";
		pToName = "";
		pToNrc = "";
		pSendTime = "";
		pReceiveTime = "";
		pFromName = "";
		pFromNrc = "";
		pPrintStatus = 0;
		pSaveTime = "";
		pPostTime = "";
		pCompleteTime = "";
		pByear = "";
		pRemark = "";
		pT1 = "";
		pN1 = 0;
		pAutoKey = 0;
		pOnlSno = "";
		pTestKey = "";
		pPrefixed = 0;
		pPfixedDate = "";
		pCurrencyCode = "";
		pCurrencyRate = 0.0;
		pBCode = "";
		pWorkStation = "";
		pUserId = "";
		pIBTComission = 0.0;
		pOnlineFees = 0.0;
		pComGl = "";
		pOnlineFeesGl = "";
		pN2 = 0;
		pN3 = 0;
		pN4 = 0;
		pT2 = "";
		pT3 = "";
		pT4 = "";
		pDrComGl = "";
		pDrOnlineFeesGl = "";
		pComTransRef = 0;
	}

	public double getAmount() {
		return pAmount;
	}

	public String getAuthorisecode() {
		return pAuthorisecode;
	}

	public long getAutoKey() {
		return pAutoKey;
	}

	public String getBCode() {
		return pBCode;
	}

	public String getByear() {
		return pByear;
	}

	public String getChequeNo() {
		return pChequeNo;
	}

	public String getComGl() {
		return pComGl;
	}

	public String getCompleteTime() {
		return pCompleteTime;
	}

	public String getCrAccNumber() {
		return pCrAccNumber;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public double getCurrencyRate() {
		return pCurrencyRate;
	}

	public String getDrAccNumber() {
		return DrAccNumber;
	}

	public String getDrComGl() {
		return pDrComGl;
	}

	public String getDrOnlineFeesGl() {
		return pDrOnlineFeesGl;
	}

	public String getFromBranch() {
		return pFromBranch;
	}

	public String getFromName() {
		return pFromName;
	}

	public String getFromNrc() {
		return pFromNrc;
	}

	public double getIBTComission() {
		return pIBTComission;
	}

	public Short getIBTType() {
		return pIBTType;
	}

	public String getMainKey() {
		return pMainKey;
	}

	public int getN1() {
		return pN1;
	}

	public int getN2() {
		return pN2;
	}

	public int getN3() {
		return pN3;
	}

	public int getN4() {
		return pN4;
	}

	public double getOnlineFees() {
		return pOnlineFees;
	}

	public String getOnlineFeesGl() {
		return pOnlineFeesGl;
	}

	public int getOnlineSerialNo() {
		return pOnlineSerialNo;
	}

	public String getOnlSno() {
		return pOnlSno;
	}

	public int getpComTransRef() {
		return pComTransRef;
	}

	public String getPfixedDate() {
		return pPfixedDate;
	}

	public String getPostTime() {
		return pPostTime;
	}

	public int getPrefixed() {
		return pPrefixed;
	}

	public double getPreviousBal() {
		return pPreviousBal;
	}

	public short getPrintStatus() {
		return pPrintStatus;
	}

	public String getReceiveTime() {
		return pReceiveTime;
	}

	public String getRemark() {
		return pRemark;
	}

	public String getSaveTime() {
		return pSaveTime;
	}

	public String getSendTime() {
		return pSendTime;
	}

	public short getStatus() {
		return pStatus;
	}

	public String getT1() {
		return pT1;
	}

	public String getT2() {
		return pT2;
	}

	public String getT3() {
		return pT3;
	}

	public String getT4() {
		return pT4;
	}

	public String getTestKey() {
		return pTestKey;
	}

	public String getToBranch() {
		return pToBranch;
	}

	public String getToName() {
		return pToName;
	}

	public String getToNrc() {
		return pToNrc;
	}

	public String getTransDate() {
		return pTransDate;
	}

	public int getTransno() {
		return pTransno;
	}

	public int getTransref() {
		return pTransref;
	}

	public Short getTransType() {
		return pTransType;
	}

	public String getUserId() {
		return pUserId;
	}

	public String getWorkStation() {
		return pWorkStation;
	}

	public void setAmount(double pAmount) {
		this.pAmount = pAmount;
	}

	public void setAuthorisecode(String pAuthorisecode) {
		this.pAuthorisecode = pAuthorisecode;
	}

	public void setAutoKey(long pAutoKey) {
		this.pAutoKey = pAutoKey;
	}

	public void setBCode(String pBCode) {
		this.pBCode = pBCode;
	}

	public void setByear(String pByear) {
		this.pByear = pByear;
	}

	public void setChequeNo(String pChequeNo) {
		this.pChequeNo = pChequeNo;
	}

	public void setComGl(String pComGl) {
		this.pComGl = pComGl;
	}

	public void setCompleteTime(String pCompleteTime) {
		this.pCompleteTime = pCompleteTime;
	}

	public void setCrAccNumber(String pCrAccNumber) {
		this.pCrAccNumber = pCrAccNumber;
	}

	public void setCurrencyCode(String pCurrencyCode) {
		this.pCurrencyCode = pCurrencyCode;
	}

	public void setCurrencyRate(double pCurrencyRate) {
		this.pCurrencyRate = pCurrencyRate;
	}

	public void setDrAccNumber(String drAccNumber) {
		DrAccNumber = drAccNumber;
	}

	public void setDrComGl(String pDrComGl) {
		this.pDrComGl = pDrComGl;
	}

	public void setDrOnlineFeesGl(String pDrOnlineFeesGl) {
		this.pDrOnlineFeesGl = pDrOnlineFeesGl;
	}

	public void setFromBranch(String pFromBranch) {
		this.pFromBranch = pFromBranch;
	}

	public void setFromName(String pFromName) {
		this.pFromName = pFromName;
	}

	public void setFromNrc(String pFromNrc) {
		this.pFromNrc = pFromNrc;
	}

	public void setIBTComission(double pIBTComission) {
		this.pIBTComission = pIBTComission;
	}

	public void setIBTType(Short pIBTType) {
		this.pIBTType = pIBTType;
	}

	public void setMainKey(String pMainKey) {
		this.pMainKey = pMainKey;
	}

	public void setN1(int pN1) {
		this.pN1 = pN1;
	}

	public void setN2(int pN2) {
		this.pN2 = pN2;
	}

	public void setN3(int pN3) {
		this.pN3 = pN3;
	}

	public void setN4(int pN4) {
		this.pN4 = pN4;
	}

	public void setOnlineFees(double pOnlineFees) {
		this.pOnlineFees = pOnlineFees;
	}

	public void setOnlineFeesGl(String pOnlineFeesGl) {
		this.pOnlineFeesGl = pOnlineFeesGl;
	}

	public void setOnlineSerialNo(int pOnlineSerialNo) {
		this.pOnlineSerialNo = pOnlineSerialNo;
	}

	public void setOnlSno(String pOnlSno) {
		this.pOnlSno = pOnlSno;
	}

	public void setpComTransRef(int pComTransRef) {
		this.pComTransRef = pComTransRef;
	}

	public void setPfixedDate(String pPfixedDate) {
		this.pPfixedDate = pPfixedDate;
	}

	public void setPostTime(String pPostTime) {
		this.pPostTime = pPostTime;
	}

	public void setPrefixed(int pPrefixed) {
		this.pPrefixed = pPrefixed;
	}

	public void setPreviousBal(double pPreviousBal) {
		this.pPreviousBal = pPreviousBal;
	}

	public void setPrintStatus(short pPrintStatus) {
		this.pPrintStatus = pPrintStatus;
	}

	public void setReceiveTime(String pReceiveTime) {
		this.pReceiveTime = pReceiveTime;
	}

	public void setRemark(String pRemark) {
		this.pRemark = pRemark;
	}

	public void setSaveTime(String pSaveTime) {
		this.pSaveTime = pSaveTime;
	}

	public void setSendTime(String pSendTime) {
		this.pSendTime = pSendTime;
	}

	public void setStatus(short pStatus) {
		this.pStatus = pStatus;
	}

	public void setT1(String pT1) {
		this.pT1 = pT1;
	}

	public void setT2(String pT2) {
		this.pT2 = pT2;
	}

	public void setT3(String pT3) {
		this.pT3 = pT3;
	}

	public void setT4(String pT4) {
		this.pT4 = pT4;
	}

	public void setTestKey(String pTestKey) {
		this.pTestKey = pTestKey;
	}

	public void setToBranch(String pToBranch) {
		this.pToBranch = pToBranch;
	}

	public void setToName(String pToName) {
		this.pToName = pToName;
	}

	public void setToNrc(String pToNrc) {
		this.pToNrc = pToNrc;
	}

	public void setTransDate(String pTransDate) {
		this.pTransDate = pTransDate;
	}

	public void setTransno(int pTransno) {
		this.pTransno = pTransno;
	}

	public void setTransref(int pTransref) {
		this.pTransref = pTransref;
	}

	public void setTransType(Short pTransType) {
		this.pTransType = pTransType;
	}

	public void setUserId(String pUserId) {
		this.pUserId = pUserId;
	}

	public void setWorkStation(String pWorkStation) {
		this.pWorkStation = pWorkStation;
	}

}
