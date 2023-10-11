package com.nirvasoft.rp.shared;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class depositaccdataresult {

	private String retcode = "";
	private String retmessage = "";
	private depositaccdata[] datalist = null;
	private BalanceTotalResponse[] balancedatalist = null;
	
	public depositaccdataresult() {
		clearproperty();
	}

	void clearproperty() {
		retcode = "";
		retmessage = "";
		datalist = null;
		balancedatalist = null;
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

	public depositaccdata[] getDatalist() {
		return datalist;
	}

	public void setDatalist(depositaccdata[] datalist) {
		this.datalist = datalist;
	}

	public BalanceTotalResponse[] getBalancedatalist() {
		return balancedatalist;
	}

	public void setBalancedatalist(BalanceTotalResponse[] balancedatalist) {
		this.balancedatalist = balancedatalist;
	}

	
}
