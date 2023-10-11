package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequeverifyreq {
	private String token;
	private String accnumber;
	private String amount; 
	private String chequenumber;
	private String t2;	// 	"MMK",
	private String t3;	//	Name, 
	private String t4;	//	NRC, 
	private String t5;	//	PhoneNo
	private String syskey;
	
	public chequeverifyreq() {
		clearProperty();
	}

	private void clearProperty() {
		token = "";
		accnumber = "";
		amount = "";
		chequenumber = "";
		t2 = "";
		t3 = "";
		t4 = "";
		t5 = "";
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public String getAmount() {
		return amount;
	}

	public String getT2() {
		return t2;
	}

	public String getT3() {
		return t3;
	}

	public String getT4() {
		return t4;
	}

	public String getT5() {
		return t5;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public void setT5(String t5) {
		this.t5 = t5;
	}

	public String getChequenumber() {
		return chequenumber;
	}

	public void setChequenumber(String chequenumber) {
		this.chequenumber = chequenumber;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
