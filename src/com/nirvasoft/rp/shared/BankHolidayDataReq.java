package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BankHolidayDataReq {
	
	private String token;
	private String date;
	
	public BankHolidayDataReq() {
		clearproperty();
	}

	void clearproperty() {
		
		token = "";
		date = "";
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
