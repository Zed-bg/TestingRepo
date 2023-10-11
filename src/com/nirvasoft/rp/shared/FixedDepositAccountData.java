package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixedDepositAccountData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3804285295873802139L;
	
	private String pAccNumber;		
	private Short pTenure;		
	private String pStartDate;		
	private String pDueDate;	
	private double pIntToBePaid;	
	private Short pIntPayCode;	
	private String pAccToBePaid;	
	private boolean pAutoRenew;	
	private String pReceiptNo;
	
	private String pRefNo;
	private byte pOtherFacility;
	private double pAmount;
	private String pLastTransDate;
	private byte pStatus;
	private String pOpeningDate;
	private String pClosingDate;
	private String pApprovalNo;
	private int pTotalDays;
	private double pYearlyRate;
	private double pInterestAmt;
	private double pAccruedAmt;
	private String pCurrencyCode;
	private double pCurrencyRate;
	private String pBranchCode;
	private String pCounterID;
	private boolean pCreditToPO;
	private String pCreditPONo;
	private String pCreditPOGL;
	private int pPOType;	
	
	private String pPrincipalAccount;
	private String pInterestAccount;
	
	private String pProductType;
	private String pAccType;
	
	private String pAccruedStartDate;
	private String pAccruedEndDate;
	private String pAccByMonth;		//ZZM42
	private double pAccruedFlag;
	private String creditAcc;	
	private String crBCode;
	private String accBCode;
	private String BranchAcc;
	public double getpAccruedFlag() {
		return pAccruedFlag;
	}
	public void setpAccruedFlag(double pAccruedFlag) {
		this.pAccruedFlag = pAccruedFlag;
	}
	public String getpAccByMonth() {
		return pAccByMonth;
	}
	public void setpAccByMonth(String pAccByMonth) {
		this.pAccByMonth = pAccByMonth;
	}
	public String getAccountNumber() {
		return pAccNumber;
	}
	public void setAccountNumber(String pAccNumber) {
		this.pAccNumber = pAccNumber;
	}
	public Short getTenure() {
		return pTenure;
	}
	public void setTenure(Short pTenure) {
		this.pTenure = pTenure;
	}
	public String getStartDate() {
		return pStartDate;
	}
	public void setStartDate(String pStartDate) {
		this.pStartDate = pStartDate;
	}
	public String getDueDate() {
		return pDueDate;
	}
	public void setDueDate(String pDueDate) {
		this.pDueDate = pDueDate;
	}
	public double getIntToBePaid() {
		return pIntToBePaid;
	}
	public void setIntToBePaid(double pIntToBePaid) {
		this.pIntToBePaid = pIntToBePaid;
	}
	public Short getIntPayCode() {
		return pIntPayCode;
	}
	public void setIntPayCode(Short pIntPayCode) {
		this.pIntPayCode = pIntPayCode;
	}	
	
	public String getpAccToBePaid() {
		return pAccToBePaid;
	}
	public void setpAccToBePaid(String pAccToBePaid) {
		this.pAccToBePaid = pAccToBePaid;
	}
		public String getAccToBePaid() {
		return pAccToBePaid;
	}

	public void setAccToBePaid(String pAccToBePaid) {
		this.pAccToBePaid = pAccToBePaid;
	}
	public boolean getAutoRenew() {
		return pAutoRenew;
	}
	public void setAutoRenew(boolean pAutoRenew) {
		this.pAutoRenew = pAutoRenew;
	}
	public String getReceiptNo() {
		return pReceiptNo;
	}
	public void setReceiptNo(String pReceiptNo) {
		this.pReceiptNo = pReceiptNo;
	}
	
	public String getRefNo() {
		return pRefNo;
	}
	public void setRefNo(String pRefNo) {
		this.pRefNo = pRefNo;
	}
	public byte getOtherFacility() {
		return pOtherFacility;
	}
	public void setOtherFacility(byte pOtherFacility) {
		this.pOtherFacility = pOtherFacility;
	}
	public double getAmount() {
		return pAmount;
	}
	public void setAmount(double pAmount) {
		this.pAmount = pAmount;
	}
	public String getLastTransDate() {
		return pLastTransDate;
	}
	public void setLastTransDate(String pLastTransDate) {
		this.pLastTransDate = pLastTransDate;
	}
	public byte getStatus() {
		return pStatus;
	}
	public void setStatus(byte pStatus) {
		this.pStatus = pStatus;
	}
	public String getOpeningDate() {
		return pOpeningDate;
	}
	public void setOpeningDate(String pOpeningDate) {
		this.pOpeningDate = pOpeningDate;
	}
	public String getClosingDate() {
		return pClosingDate;
	}
	public void setClosingDate(String pClosingDate) {
		this.pClosingDate = pClosingDate;
	}
	public String getApprovalNo() {
		return pApprovalNo;
	}
	public void setApprovalNo(String pApprovalNo) {
		this.pApprovalNo = pApprovalNo;
	}
	public int getTotalDays() {
		return pTotalDays;
	}
	public void setTotalDays(int pTotalDays) {
		this.pTotalDays = pTotalDays;
	}
	
	public double getYearlyRate() {
		return pYearlyRate;
	}
	public void setYearlyRate(double pYearlyRate) {
		this.pYearlyRate = pYearlyRate;
	}
	public double getInterestAmt() {
		return pInterestAmt;
	}
	public void setInterestAmt(double pInterestAmt) {
		this.pInterestAmt = pInterestAmt;
	}
	public double getAccruedAmt() {
		return pAccruedAmt;
	}
	public void setAccruedAmt(double pAccruedAmt) {
		this.pAccruedAmt = pAccruedAmt;
	}
	
	public String getCurrencyCode() {
		return pCurrencyCode;
	}
	public void setCurrencyCode(String pCurrencyCode) {
		this.pCurrencyCode = pCurrencyCode;
	}
	public double getCurrencyRate() {
		return pCurrencyRate;
	}
	public void setCurrencyRate(double pCurrencyRate) {
		this.pCurrencyRate = pCurrencyRate;
	}
	public String getBranchCode() {
		return pBranchCode;
	}
	public void setBranchCode(String pBranchCode) {
		this.pBranchCode = pBranchCode;
	}
	public String getCounterID() {
		return pCounterID;
	}
	public void setCounterID(String pCounterID) {
		this.pCounterID = pCounterID;
	}
	
	public String getPrincipalAccount() {
		return pPrincipalAccount;
	}
	public void setPrincipalAccount(String pPrincipalAccount) {
		this.pPrincipalAccount = pPrincipalAccount;
	}
	public String getInterestAccount() {
		return pInterestAccount;
	}
	public void setInterestAccount(String pInterestAccount) {
		this.pInterestAccount = pInterestAccount;
	}
	
	public String getProductType() {
		return pProductType;
	}
	public void setProductType(String pProductType) {
		this.pProductType = pProductType;
	}
	public String getAccType() {
		return pAccType;
	}
	public void setAccType(String pAccType) {
		this.pAccType = pAccType;
	}	
	public String getAccruedStartDate() {
		return pAccruedStartDate;
	}
	public void setAccruedStartDate(String accruedStartDate) {
		pAccruedStartDate = accruedStartDate;
	}
	public String getAccruedEndDate() {
		return pAccruedEndDate;
	}
	public void setAccruedEndDate(String accruedEndDate) {
		pAccruedEndDate = accruedEndDate;
	}
	public void clearProperty(){
		 pAccNumber = "";	
		 pTenure = 0;	
		 pStartDate = "";	
		 pDueDate = "";
		 pIntToBePaid = 0;
		 pIntPayCode = 0;
		 pAccToBePaid = "";
		 pAutoRenew = false;
		 pReceiptNo = "0";
		 
		 pRefNo="";
		 pOtherFacility=(byte)0;
		 pAmount=0;
		 pLastTransDate="";
		 pStatus=(byte)0;
		 pOpeningDate="";
		 pClosingDate="";
		 pApprovalNo="";
		 pTotalDays=0;
		 pYearlyRate=0;
		 pInterestAmt=0;
		 pAccruedAmt=0;
		 pCurrencyCode="";
		 pCurrencyRate=0;
		 pBranchCode="";
		 pCounterID="";
		 pPrincipalAccount="";
		 pInterestAccount="";
		 pProductType = "";
		 pAccType = "";
		 pCreditToPO = false;
		 pCreditPONo = "";
		 pCreditPOGL = "";
		 pPOType = 0;
		 pAccruedStartDate = "";
		 pAccruedEndDate = "";
		 pAccByMonth="";
		 pAccruedFlag=0;
		 creditAcc="";
		 accBCode="";
		 crBCode="";
		 BranchAcc="";
	}
	
	public FixedDepositAccountData(){
		clearProperty();
	}
	public boolean isCreditToPO() {
		return pCreditToPO;
	}
	public void setCreditToPO(boolean pCreditToPO) {
		this.pCreditToPO = pCreditToPO;
	}
	public String getCreditPONo() {
		return pCreditPONo;
	}
	public void setCreditPONo(String pCreditPONo) {
		this.pCreditPONo = pCreditPONo;
	}
	public String getCreditPOGL() {
		return pCreditPOGL;
	}
	public void setCreditPOGL(String pCreditPOGL) {
		this.pCreditPOGL = pCreditPOGL;
	}
	public int getPOType() {
		return pPOType;
	}
	public void setPOType(int pPOType) {
		this.pPOType = pPOType;
	}
	public String getCreditAcc() {
		return creditAcc;
	}
	public void setCreditAcc(String creditAcc) {
		this.creditAcc = creditAcc;
	}
	public String getBranchAcc() {
		return BranchAcc;
	}
	public void setBranchAcc(String branchAcc) {
		BranchAcc = branchAcc;
	}
	public String getCrBCode() {
		return crBCode;
	}
	public void setCrBCode(String crBCode) {
		this.crBCode = crBCode;
	}
	public String getAccBCode() {
		return accBCode;
	}
	public void setAccBCode(String accBCode) {
		this.accBCode = accBCode;
	}	
	
}

