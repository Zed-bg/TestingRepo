package com.nirvasoft.rp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import com.nirvasoft.rp.shared.icbs.AccountData;

public class DataResult implements Serializable{
	
	  private static final long serialVersionUID = -2327728558305381390L;
	  
	  private String pExpDate = "";
	  
	  private String Vov = "";
	  
	  private String vNo = "";
	  
	  private boolean pStatus = false;
	  
	  private String pDescription = "";
	  
	  private String pCode = "";
	  
	  private String pData = "";
	  
	  private double pBalance;
	  
	  private int pTransactionNumber;
	  
	  private String pCashCardAccountNumber;
	  
	  private String pSecureCardAccountNumber;
	  
	  private ArrayList<TempData> plstTempData = new ArrayList<TempData>();
	  
	  private int pErrorFlag = 0;
	  
	  private long autoLinkTransRef;
	  
	  private long secAutoLinkTransRef;
	  
	  private String effectiveDate = "";
	  
	  private boolean universalstatus = false;
	  
	  private boolean bcestatus = false;
	  
	  private long pAutoKey;
	  
	  private ArrayList<SignatureData> plstSigData = new ArrayList<SignatureData>();
	  
	  private String str = "";
	  
//	  private long transRef = 0;
	  private int transRef = 0;
	  
	  private String poSerial = "";
	  
	  private int recordStatus = 0;
	  
	  private String pRefNo = "";
	  
	  private double pTotalAmount;
	  
	  private double pCount;
	  
	  private ArrayList<AccountData> plstAccountData;
	  
	  
	  public long getAutoLinkTransRef() {
	    return this.autoLinkTransRef;
	  }
	  
	  public void setAutoLinkTransRef(long autoLinkTransRef) {
	    this.autoLinkTransRef = autoLinkTransRef;
	  }
	  
	  public long getSecAutoLinkTransRef() {
		return secAutoLinkTransRef;
	  }

	  public void setSecAutoLinkTransRef(long secAutoLinkTransRef) {
		this.secAutoLinkTransRef = secAutoLinkTransRef;
	  }

	public boolean getStatus() {
	    return this.pStatus;
	  }
	  
	  public void setStatus(boolean p) {
	    this.pStatus = p;
	  }
	  
	  public String getDescription() {
	    return this.pDescription;
	  }
	  
	  public void setDescription(String p) {
	    this.pDescription = p;
	  }
	  
	  public String getCode() {
	    return this.pCode;
	  }
	  
	  public void setCode(String p) {
	    this.pCode = p;
	  }
	  
	  public String getData() {
	    return this.pData;
	  }
	  
	  public void setData(String p) {
	    this.pData = p;
	  }
	  
	  public double getBalance() {
	    return this.pBalance;
	  }
	  
	  public void setBalance(double p) {
	    this.pBalance = p;
	  }
	  
	  public int getTransactionNumber() {
	    return this.pTransactionNumber;
	  }
	  
	  public void setTransactionNumber(int p) {
	    this.pTransactionNumber = p;
	  }
	  
	  public String getCashCardAccountNumber() {
	    return this.pCashCardAccountNumber;
	  }
	  
	  public void setCashCardAccountNumber(String p) {
	    this.pCashCardAccountNumber = p;
	  }
	  
	  public String getSecureCardAccountNumber() {
	    return this.pSecureCardAccountNumber;
	  }
	  
	  public void setSecureCardAccountNumber(String p) {
	    this.pSecureCardAccountNumber = p;
	  }
	  
	  public ArrayList<TempData> getTempDataList() {
	    return this.plstTempData;
	  }
	  
	  public void setTempDataList(ArrayList<TempData> plstTempData) {
	    this.plstTempData = plstTempData;
	  }
	  
	  public int getErrorFlag() {
	    return this.pErrorFlag;
	  }
	  
	  public void setErrorFlag(int pErrorFlag) {
	    this.pErrorFlag = pErrorFlag;
	  }
	  
	  public String getVNo() {
	    return this.vNo;
	  }
	  
	  public void setVNo(String vNo) {
	    this.vNo = vNo;
	  }
	  
	  public String getVov() {
	    return this.Vov;
	  }
	  
	  public void setVov(String vov) {
	    this.Vov = vov;
	  }
	  
	  public String getExpDate() {
	    return this.pExpDate;
	  }
	  
	  public void setExpDate(String pExpDate) {
	    this.pExpDate = pExpDate;
	  }
	  
	  public boolean getUniversalstatus() {
	    return this.universalstatus;
	  }
	  
	  public void setUniversalstatus(boolean p) {
	    this.universalstatus = p;
	  }
	  
	  public String getEffectiveDate() {
	    return this.effectiveDate;
	  }
	  
	  public void setEffectiveDate(String effectiveDate) {
	    this.effectiveDate = effectiveDate;
	  }
	  
	  public boolean getBcestatus() {
	    return this.bcestatus;
	  }
	  
	  public void setBcestatus(boolean bcestatus) {
	    this.bcestatus = bcestatus;
	  }
	  
	  public long getpAutoKey() {
	    return this.pAutoKey;
	  }
	  
	  public void setpAutoKey(long pAutoKey) {
	    this.pAutoKey = pAutoKey;
	  }
	  
	  public ArrayList<SignatureData> getlistSigData() {
	    return this.plstSigData;
	  }
	  
	  public void setlistSigData(ArrayList<SignatureData> plstSigData) {
	    this.plstSigData = plstSigData;
	  }
	  
	  public String getStr() {
	    return this.str;
	  }
	  
	  public void setStr(String str) {
	    this.str = str;
	  }
	  
	  public String getpRefNo() {
	    return this.pRefNo;
	  }
	  
	  public void setpRefNo(String pRefNo) {
	    this.pRefNo = pRefNo;
	  }
	  
	  public int getTransRef() {
	    return this.transRef;
	  }
	  
	  public void setTransRef(int transRef) {
	    this.transRef = transRef;
	  }
	  
	  public int getRecordStatus() {
	    return this.recordStatus;
	  }
	  
	  public void setRecordStatus(int recordStatus) {
	    this.recordStatus = recordStatus;
	  }
	  
	  public String getPoSerial() {
	    return this.poSerial;
	  }
	  
	  public void setPoSerial(String poSerial) {
	    this.poSerial = poSerial;
	  }
	  
	  public ArrayList<AccountData> getListAccountData() {
	    return this.plstAccountData;
	  }
	  
	  public void setListAccountData(ArrayList<AccountData> plstAccountData) {
	    this.plstAccountData = plstAccountData;
	  }
	  
	  public void setTotalAmount(double pTotalAmount) {
	    this.pTotalAmount = pTotalAmount;
	  }
	  
	  public double getTotalAmount() {
	    return this.pTotalAmount;
	  }
	  
	  public void setCount(double pCount) {
	    this.pCount = pCount;
	  }
	  
	  public double getCount() {
	    return this.pCount;
	  }

}
