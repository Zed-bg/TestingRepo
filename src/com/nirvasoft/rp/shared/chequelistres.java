package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequelistres {
	private String retcode;
	private String retmessage;
	private ArrayList<chequelistdata> datalist = null;
	
	public chequelistres() {
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

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public ArrayList<chequelistdata> getDatalist() {
		return datalist;
	}

	public void setDatalist(ArrayList<chequelistdata> datalist) {
		this.datalist = datalist;
	}
}
