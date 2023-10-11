package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class coderesponsedata {
	private String retcode;
	private String retmessage;
	private String transid;
	private String recno;
	
	public coderesponsedata() {
		clearproperty();
	}

	void clearproperty() {
		retcode = "";
		retmessage = "";
		transid = "";
		recno = "";
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getTransid() {
		return transid;
	}

	public String getRecno() {
		return recno;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public void setRecno(String recno) {
		this.recno = recno;
	}
}
