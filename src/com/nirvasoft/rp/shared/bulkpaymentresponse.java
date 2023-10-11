package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class bulkpaymentresponse {
	private String retcode;
	private String retmessage;
	private bulkpaymentdatares[] datalist;
	public bulkpaymentresponse() {
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

	public bulkpaymentdatares[] getDatalist() {
		return datalist;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setDatalist(bulkpaymentdatares[] datalist) {
		this.datalist = datalist;
	}
}
