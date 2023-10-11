package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequeverifyres {
	private String retcode;
	private String retmessage;
	private String duedate;
	
	public chequeverifyres() {
		clearProperty();
	}

	private void clearProperty() {
		retcode = "";
		retmessage = "";
		duedate = "";
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getDuedate() {
		return duedate;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

}
