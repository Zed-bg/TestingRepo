package com.nirvasoft.rp.shared;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CutOffDataResult {

	private String retcode;
	private String retmessage;
	private String cutofftime = "";

	public CutOffDataResult() {
		clearProperty();
	}

	void clearProperty() {
		retcode = "";
		retmessage = "";
		cutofftime = "";
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

	public String getCutofftime() {
		return cutofftime;
	}

	public void setCutofftime(String cutofftime) {
		this.cutofftime = cutofftime;
	}
}
