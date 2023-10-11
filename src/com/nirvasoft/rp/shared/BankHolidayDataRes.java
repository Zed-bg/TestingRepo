package com.nirvasoft.rp.shared;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BankHolidayDataRes {

	private String retcode;
	private String retmessage;
	private String description;
	
	public BankHolidayDataRes() {
		clearProperty();
	}

	void clearProperty() {
		retcode = "";
		retmessage = "";
		description = "";
	}
	

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
