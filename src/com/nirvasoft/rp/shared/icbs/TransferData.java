package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

import com.nirvasoft.rp.shared.ProductData;

public class TransferData extends ATMParameterData implements Serializable {
	private static final long serialVersionUID = -8583503791598502899L;
	private String pFromAccNumber;
	private String pToAccNumber;
	private String FromZone;
	private String ToZone;
	private String FromBranchCode;
	private String ToBranchCode;
	private String UserID;
	private double LimitAmount;
	private String fromName;
	private String toName;
	private String fromNrc;
	private String toNrc;
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

	private String pByForceCheque;
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
	private String transdescription;

	private double interest;
	private double serviceCharges;
	
	private double basecurrate;
	private double baseAmount;
	private String postDescription;
	private int ibtTransType;
	private String cashInHandGL;
	private String glCode1;
	private String glCode2;
	private String productGL;
	private String previousDate;
	private double prevBalance;
	private int accountStatus;
	private Double accountBarAmount;
	private Double productMinOpeningBalance;
	private Boolean isGL = false;
	private String refcode;
	
	public TransferData() {
		super.clearProperty();

		pFromAccNumber = "";
		pToAccNumber = "";
		FromZone = "";
		ToZone = "";
		FromBranchCode = "";
		ToBranchCode = "";
		UserID = "";
		LimitAmount = 0;
		fromName = "";
		toName = "";
		fromNrc = "";
		toNrc = "";
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
		pByForceCheque = "";
		isNewFD = false;
		toBankCode = "";
		comm1 = 0;
		comm2 = 0;
		isInclusive = false;
		pTransactionDate = "";
		pTransactionTime = "";
		pEffectiveDate = "";
		transdescription = "";
		interest = 0;
		serviceCharges = 0;
		basecurrate = 0.0;
		baseAmount = 0.0;
		postDescription = "";
		ibtTransType = 0;
		cashInHandGL = "";
		glCode1 = "";
		glCode2 = "";
		productGL = "";
		previousDate = "";
		prevBalance = 0.00;
		accountStatus = 0;
		accountBarAmount = 0.0;
		productMinOpeningBalance = 0.0;
		isGL = false;
		refcode = "";
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

	public String getFromAccNumber() {
		return pFromAccNumber;
	}

	public String getFromBranchCode() {
		return FromBranchCode;
	}

	public String getFromName() {
		return fromName;
	}

	public String getFromNrc() {
		return fromNrc;
	}

	public String getFromProductCode() {
		return fromProductCode;
	}

	public String getFromZone() {
		return FromZone;
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

	public String getToAccNumber() {
		return pToAccNumber;
	}

	public String getToBranchCode() {
		return ToBranchCode;
	}

	public String getToName() {
		return toName;
	}

	public String getToNrc() {
		return toNrc;
	}

	public String getToProductCode() {
		return toProductCode;
	}

	public String getToZone() {
		return ToZone;
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

	public void setFromAccNumber(String pFromAccNumber) {
		this.pFromAccNumber = pFromAccNumber;
	}

	public void setFromBranchCode(String fromBranchCode) {
		FromBranchCode = fromBranchCode;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public void setFromNrc(String fromNrc) {
		this.fromNrc = fromNrc;
	}

	public void setFromProductCode(String fromProductCode) {
		this.fromProductCode = fromProductCode;
	}

	public void setFromZone(String fromZone) {
		FromZone = fromZone;
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

	public void setToAccNumber(String pToAccNumber) {
		this.pToAccNumber = pToAccNumber;
	}

	public void setToBranchCode(String toBranchCode) {
		ToBranchCode = toBranchCode;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public void setToNrc(String toNrc) {
		this.toNrc = toNrc;
	}

	public void setToProductCode(String toProductCode) {
		this.toProductCode = toProductCode;
	}

	public void setToZone(String toZone) {
		ToZone = toZone;
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

	public String getpByForceCheque() {
		return pByForceCheque;
	}

	public void setpByForceCheque(String pByForceCheque) {
		this.pByForceCheque = pByForceCheque;
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

	public String getpFromAccNumber() {
		return pFromAccNumber;
	}

	public String getpToAccNumber() {
		return pToAccNumber;
	}

	public boolean isNewFD() {
		return isNewFD;
	}

	public void setpFromAccNumber(String pFromAccNumber) {
		this.pFromAccNumber = pFromAccNumber;
	}

	public void setpToAccNumber(String pToAccNumber) {
		this.pToAccNumber = pToAccNumber;
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

	public String getTransdescription() {
		return transdescription;
	}

	public void setTransdescription(String transdescription) {
		this.transdescription = transdescription;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getServiceCharges() {
		return serviceCharges;
	}

	public void setServiceCharges(double serviceCharges) {
		this.serviceCharges = serviceCharges;
	}

	public String getpTransactionTime() {
		return pTransactionTime;
	}

	public void setpTransactionTime(String pTransactionTime) {
		this.pTransactionTime = pTransactionTime;
	}

	public double getBasecurrate() {
		return basecurrate;
	}

	public void setBasecurrate(double basecurrate) {
		this.basecurrate = basecurrate;
	}

	public double getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(double baseAmount) {
		this.baseAmount = baseAmount;
	}

	public String getPostDescription() {
		return postDescription;
	}

	public void setPostDescription(String postDescription) {
		this.postDescription = postDescription;
	}

	public int getIbtTransType() {
		return ibtTransType;
	}

	public void setIbtTransType(int ibtTransType) {
		this.ibtTransType = ibtTransType;
	}

	public String getCashInHandGL() {
		return cashInHandGL;
	}

	public void setCashInHandGL(String cashInHandGL) {
		this.cashInHandGL = cashInHandGL;
	}

	public String getGlCode1() {
		return glCode1;
	}

	public void setGlCode1(String glCode1) {
		this.glCode1 = glCode1;
	}

	public String getGlCode2() {
		return glCode2;
	}

	public void setGlCode2(String glCode2) {
		this.glCode2 = glCode2;
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

	public double getPrevBalance() {
		return prevBalance;
	}

	public void setPrevBalance(double prevBalance) {
		this.prevBalance = prevBalance;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Double getAccountBarAmount() {
		return accountBarAmount;
	}

	public void setAccountBarAmount(Double accountBarAmount) {
		this.accountBarAmount = accountBarAmount;
	}

	public Double getProductMinOpeningBalance() {
		return productMinOpeningBalance;
	}

	public void setProductMinOpeningBalance(Double productMinOpeningBalance) {
		this.productMinOpeningBalance = productMinOpeningBalance;
	}

	public Boolean getIsGL() {
		return isGL;
	}

	public void setIsGL(Boolean isGL) {
		this.isGL = isGL;
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
}
