package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetOutstandingBillReqData {
	private String sessionID;
	private String userID;
	private String meterID;
	private String merchantID;
	private String field1;
	private String field2;

	public GetOutstandingBillReqData() {
		clearProperties();
	}

	private void clearProperties() {
		this.sessionID = "";
		this.userID = "";
		this.meterID = "";
		this.field1 = "";
		this.field2 = "";
		this.merchantID = "";
	}

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public String getMeterID() {
		return meterID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getUserID() {
		return userID;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "{sessionID=" + sessionID + ", userID=" + userID + ", meterID=" + meterID + ", field1=" + field1
				+ ",field2=" + field2 + ",merchantID=" + merchantID + "}";
	}
}
