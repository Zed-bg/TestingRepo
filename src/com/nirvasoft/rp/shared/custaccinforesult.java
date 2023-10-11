package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class custaccinforesult {
	private String retcode;
	private String retmessage;
	private ArrayList<AccountInfoData> datalist;
	
	public custaccinforesult() {
		clearproperty();
	}

	void clearproperty() {
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
	
	public ArrayList<AccountInfoData> getDatalist() {
		return datalist;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setDatalist(ArrayList<AccountInfoData> datalist) {
		this.datalist = datalist;
	}
}
