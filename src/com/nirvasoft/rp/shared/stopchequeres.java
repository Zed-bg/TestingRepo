package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class stopchequeres {

	private String retcode;
	private String retmessage;

	public stopchequeres() {
		clearProperty();
	}

	private void clearProperty() {
		retcode = "";
		retmessage = "";
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}


}
