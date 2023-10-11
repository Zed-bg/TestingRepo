package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetOutstandingBillData {
	private String scheduleName;
	private String merchantID;
	private String meterID;
	private String billNo;
	private String lastUnit;
	private String thisUnit;
	private String totalUnit;
	private String rate;
	private String conservationFee;
	private String amount;
	private String lastDate;
	private String status;
	private String penaltyDays;
	private String penaltyAmt;
	private String bankChargesAmt;
	private String code;
	private String desc;

	public GetOutstandingBillData() {
		clearProperties();
	}

	private void clearProperties() {
		this.scheduleName = "";
		this.merchantID = "";
		this.meterID = "";
		this.billNo = "";
		this.lastUnit = "0";
		this.thisUnit = "0";
		this.totalUnit = "0";
		this.rate = "0";
		this.conservationFee = "0";
		this.amount = "0.00";
		this.lastDate = "";
		this.status = "0";
		this.penaltyDays = "0";
		this.penaltyAmt = "0.00";
		this.bankChargesAmt = "0.00";
		this.code = "";
		this.desc = "";
	}

	public String getAmount() {
		return amount;
	}

	public String getBankChargesAmt() {
		return bankChargesAmt;
	}

	public String getBillNo() {
		return billNo;
	}

	public String getCode() {
		return code;
	}

	public String getConservationFee() {
		return conservationFee;
	}

	public String getDesc() {
		return desc;
	}

	public String getLastDate() {
		return lastDate;
	}

	public String getLastUnit() {
		return lastUnit;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public String getMeterID() {
		return meterID;
	}

	public String getPenaltyAmt() {
		return penaltyAmt;
	}

	public String getPenaltyDays() {
		return penaltyDays;
	}

	public String getRate() {
		return rate;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public String getStatus() {
		return status;
	}

	public String getThisUnit() {
		return thisUnit;
	}

	public String getTotalUnit() {
		return totalUnit;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setBankChargesAmt(String bankChargesAmt) {
		this.bankChargesAmt = bankChargesAmt;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setConservationFee(String conservationFee) {
		this.conservationFee = conservationFee;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public void setLastUnit(String lastUnit) {
		this.lastUnit = lastUnit;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}

	public void setPenaltyAmt(String penaltyAmt) {
		this.penaltyAmt = penaltyAmt;
	}

	public void setPenaltyDays(String penaltyDays) {
		this.penaltyDays = penaltyDays;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setThisUnit(String thisUnit) {
		this.thisUnit = thisUnit;
	}

	public void setTotalUnit(String totalUnit) {
		this.totalUnit = totalUnit;
	}

	@Override
	public String toString() {
		return "{scheduleName=" + scheduleName + ", merchantID=" + merchantID + ", meterID=" + meterID + ", billNo="
				+ billNo + ",lastUnit=" + lastUnit + ",thisUnit=" + thisUnit + ",totalUnit=" + totalUnit + ",rate="
				+ rate + ",conservationFee=" + conservationFee + ",amount=" + amount + ",lastDate=" + lastDate
				+ ",status=" + status + ",penaltyDays=" + penaltyDays + ",penaltyAmt=" + penaltyAmt + ",bankChargesAmt="
				+ bankChargesAmt + "}";
	}

}
