package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

import com.nirvasoft.rp.shared.ProductData;

public class DepositData extends ATMParameterData implements Serializable {
	private static final long serialVersionUID = -8583503791598502899L;
	private String accNumber;
	private String branchCode;
	private String UserID;
	private double LimitAmount;
	private String name;
	private int transferKey;
	private String debitIBTKey;
	private String creditIBTKey;
	private boolean isAgent;
	private boolean isCreditAgent;
	private boolean isFinishCutOffTime;
	private String ReferenceNo;
	private boolean isSameAgent;
	private String customerID;
	private int onlineSerialNo;
	private String merchantID;
	private String remark;
	private String fromProductCode;
	private String toProductCode;
	private String cardNo;
	private ProductData fromproduct;
	private ProductData toproduct;
	private boolean isNewFD;
	private String toBankCode;
	private double comm1;
	private double comm2;
	private boolean isInclusive;
	
	private String pTransactionDate;
	private String pTransactionTime;
	private String pEffectiveDate;
	private String transDescription;
	private int transType;
	private Boolean isGL; 
	private String accRef;
	private int n3;
	private String cashInHandGL = "";
	private String glCode1 = "";
	private String glCode2 = "";
	private String t2 = "";
	private String t3 = "";
	private String authorizerID = "";
	private String productGL = "";
	private String previousDate = "";
	public DepositData() {
		super.clearProperty();
		UserID = "";
		LimitAmount = 0;
		transferKey = 0;
		debitIBTKey = "";
		creditIBTKey = "";
		isAgent = false;
		isCreditAgent = false;
		isFinishCutOffTime = false;
		ReferenceNo = "";
		isSameAgent = false;
		onlineSerialNo = 0;
		merchantID = "";
		remark = "";
		fromProductCode = "";
		toProductCode = "";
		cardNo = "";
		fromproduct = null;
		toproduct = null;
		isNewFD = false;
		toBankCode = "";
		comm1 = 0;
		comm2 = 0;
		isInclusive = false;
		pTransactionDate = "";
		pTransactionTime = "";
		pEffectiveDate = "";
		isGL = false;
		transType = 0;
		transDescription = "";
		accRef = "";
		n3 = 0;
		cashInHandGL = "";
		glCode2 = "";
		t2 = "";
		t3 = "";
		authorizerID = "";
		glCode1 = "";
		productGL = "";
		previousDate = ""; 
	}

	public String getCreditIBTKey() {
		return creditIBTKey;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getDebitIBTKey() {
		return debitIBTKey;
	}

	public String getFromProductCode() {
		return fromProductCode;
	}

	public boolean getIsAgent() {
		return isAgent;
	}

	public boolean getIsCreditAgent() {
		return isCreditAgent;
	}

	public double getLimitAmount() {
		return LimitAmount;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public int getOnlineSerialNo() {
		return onlineSerialNo;
	}

	public String getReferenceNo() {
		return ReferenceNo;
	}

	public String getRemark() {
		return remark;
	}

	public String getToProductCode() {
		return toProductCode;
	}

	public int getTransferKey() {
		return transferKey;
	}

	public String getUserID() {
		return UserID;
	}

	public boolean isFinishCutOffTime() {
		return isFinishCutOffTime;
	}

	public boolean isSameAgent() {
		return isSameAgent;
	}

	public void setCreditIBTKey(String creditIBTKey) {
		this.creditIBTKey = creditIBTKey;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public void setDebitIBTKey(String debitIBTKey) {
		this.debitIBTKey = debitIBTKey;
	}

	public void setFinishCutOffTime(boolean isFinishCutOffTime) {
		this.isFinishCutOffTime = isFinishCutOffTime;
	}

	public void setFromProductCode(String fromProductCode) {
		this.fromProductCode = fromProductCode;
	}

	public void setIsAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}

	public void setIsCreditAgent(boolean isCreditAgent) {
		this.isCreditAgent = isCreditAgent;
	}

	public void setLimitAmount(double limitAmount) {
		LimitAmount = limitAmount;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public void setOnlineSerialNo(int onlineSerialNo) {
		this.onlineSerialNo = onlineSerialNo;
	}

	public void setReferenceNo(String referenceNo) {
		ReferenceNo = referenceNo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSameAgent(boolean isSameAgent) {
		this.isSameAgent = isSameAgent;
	}
	public void setToProductCode(String toProductCode) {
		this.toProductCode = toProductCode;
	}

	public void setTransferKey(int transferKey) {
		this.transferKey = transferKey;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}
	
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public ProductData getFromproduct() {
		return fromproduct;
	}

	public ProductData getToproduct() {
		return toproduct;
	}

	public void setFromproduct(ProductData fromproduct) {
		this.fromproduct = fromproduct;
	}

	public void setToproduct(ProductData toproduct) {
		this.toproduct = toproduct;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isNewFD() {
		return isNewFD;
	}
	
	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}

	public void setCreditAgent(boolean isCreditAgent) {
		this.isCreditAgent = isCreditAgent;
	}

	public void setNewFD(boolean isNewFD) {
		this.isNewFD = isNewFD;
	}

	public String getToBankCode() {
		return toBankCode;
	}

	public void setToBankCode(String toBankCode) {
		this.toBankCode = toBankCode;
	}

	public double getComm1() {
		return comm1;
	}

	public double getComm2() {
		return comm2;
	}

	public void setComm1(double comm1) {
		this.comm1 = comm1;
	}

	public void setComm2(double comm2) {
		this.comm2 = comm2;
	}

	public boolean isInclusive() {
		return isInclusive;
	}

	public void setInclusive(boolean isInclusive) {
		this.isInclusive = isInclusive;
	}
	
	public String getTransactionDate(){return pTransactionDate;}
	public void setTransactionDate(String p){pTransactionDate =p;}
	public String getTransactionTime(){return pTransactionTime;}
	public void setTransactionTime(String p){pTransactionTime =p;}
	public String getEffectiveDate(){return pEffectiveDate;}
	public void setEffectiveDate(String p){pEffectiveDate =p;}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpTransactionDate() {
		return pTransactionDate;
	}

	public void setpTransactionDate(String pTransactionDate) {
		this.pTransactionDate = pTransactionDate;
	}

	public String getpTransactionTime() {
		return pTransactionTime;
	}

	public void setpTransactionTime(String pTransactionTime) {
		this.pTransactionTime = pTransactionTime;
	}

	public String getpEffectiveDate() {
		return pEffectiveDate;
	}

	public void setpEffectiveDate(String pEffectiveDate) {
		this.pEffectiveDate = pEffectiveDate;
	}

	public Boolean getIsGL() {
		return isGL;
	}

	public void setIsGL(Boolean isGL) {
		this.isGL = isGL;
	}

	public String getTransDescription() {
		return transDescription;
	}

	public void setTransDescription(String transDescription) {
		this.transDescription = transDescription;
	}

	public int getTransType() {
		return transType;
	}

	public void setTransType(int transType) {
		this.transType = transType;
	}

	public String getAccRef() {
		return accRef;
	}

	public void setAccRef(String accRef) {
		this.accRef = accRef;
	}

	public int getN3() {
		return n3;
	}

	public void setN3(int n3) {
		this.n3 = n3;
	}

	public String getCashInHandGL() {
		return cashInHandGL;
	}

	public void setCashInHandGL(String cashInHandGL) {
		this.cashInHandGL = cashInHandGL;
	}

	public String getGlCode2() {
		return glCode2;
	}

	public void setGlCode2(String glCode2) {
		this.glCode2 = glCode2;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public String getT3() {
		return t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getAuthorizerID() {
		return authorizerID;
	}

	public void setAuthorizerID(String authorizerID) {
		this.authorizerID = authorizerID;
	}

	public String getGlCode1() {
		return glCode1;
	}

	public void setGlCode1(String glCode1) {
		this.glCode1 = glCode1;
	}

	public String getProductGL() {
		return productGL;
	}

	public void setProductGL(String productGL) {
		this.productGL = productGL;
	}

	public String getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(String previousDate) {
		this.previousDate = previousDate;
	}

	
}
