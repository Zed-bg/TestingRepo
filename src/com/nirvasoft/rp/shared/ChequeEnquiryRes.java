package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChequeEnquiryRes {
	private String retcode;
	private String retmessage;
	private String chequestatus;
	private String tophone;

	public ChequeEnquiryRes() {
		clearProperty();
	}

	private void clearProperty() {
		retcode = "";
		retmessage = "";
		chequestatus = "";
		tophone = "";
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getChequestatus() {
		return chequestatus;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setChequestatus(String chequestatus) {
		this.chequestatus = chequestatus;
	}

	public String getTophone() {
		return tophone;
	}

	public void setTophone(String tophone) {
		this.tophone = tophone;
	}

}
