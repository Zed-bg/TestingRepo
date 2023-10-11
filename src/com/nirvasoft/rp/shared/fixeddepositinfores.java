package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class fixeddepositinfores {
	
	private String retcode;
	private String retmessage;
	private ArrayList<fixeddepositinfodata> datalist;
	
	public fixeddepositinfores() {
		clearProperty();
	}

	private void clearProperty() {
		retcode = "";
		retmessage = "";
		datalist = null;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public ArrayList<fixeddepositinfodata> getDatalist() {
		return datalist;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setDatalist(ArrayList<fixeddepositinfodata> datalist) {
		this.datalist = datalist;
	}
}
