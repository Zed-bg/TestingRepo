package com.nirvasoft.rp.shared;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForeignExchangeRateResult {

	private String retcode;
	private String retmessage;
	private ForeignExchangeRateData[] datalist = null;

	public ForeignExchangeRateResult() {
		clearProperty();
	}

	void clearProperty() {
		retcode = "";
		retmessage = "";
		datalist =  null;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public ForeignExchangeRateData[] getDatalist() {
		return datalist;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setDatalist(ForeignExchangeRateData[] datalist) {
		this.datalist = datalist;
	}


}
