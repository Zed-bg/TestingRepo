package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class transactioninforesult {
	private String retcode;
	private String retmessage;
	private int totalcount;
	private double totalamount;
	
	public transactioninforesult() {
		clearproperty();
	}

	void clearproperty() {
		retcode = "";
		retmessage = "";
		totalcount = 0;
		totalamount = 0;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public double getTotalamount() {
		return totalamount;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
}
