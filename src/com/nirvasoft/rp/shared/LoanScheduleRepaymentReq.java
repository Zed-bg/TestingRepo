package com.nirvasoft.rp.shared;

public class LoanScheduleRepaymentReq {
	private String syskey;
	private String token;
	private LAODFScheduleData data;
	
	public LoanScheduleRepaymentReq() {
		clearProperty();
	}

	private void clearProperty() {
		syskey = "";
		token = "";
		data = null;
		
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LAODFScheduleData getData() {
		return data;
	}

	public void setData(LAODFScheduleData data) {
		this.data = data;
	}

}
