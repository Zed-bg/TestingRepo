package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class bulkpaymentdatares {
	private String rowno;
	private String accountstatus;
	private String returncode;
	public bulkpaymentdatares() {
		clearproperty();
	}

	void clearproperty() {
		rowno = "";
		accountstatus = "";
		returncode = "";
	}

	public String getRowno() {
		return rowno;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setRowno(String rowno) {
		this.rowno = rowno;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
}
