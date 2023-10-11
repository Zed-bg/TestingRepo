package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class refdataresponse {
	private String retcode;
	private String retmessage;
	private ArrayList<refdata> datalist;
	
	public refdataresponse() {
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

	public ArrayList<refdata> getDatalist() {
		return datalist;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setDatalist(ArrayList<refdata> datalist) {
		this.datalist = datalist;
	}
}
