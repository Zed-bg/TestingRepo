package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequelistdata {
	private String chequenumber;
	private String status;
	private String t1;
	private String t2;
	private String t3;
	
	public chequelistdata() {
		clearProperty();
	}

	private void clearProperty() {
		chequenumber = "";
		status = "";
		t1 = "";
		t2 = "";
		t3 = "";
	}

	public String getChequenumber() {
		return chequenumber;
	}

	public String getStatus() {
		return status;
	}

	public void setChequenumber(String chequenumber) {
		this.chequenumber = chequenumber;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public String getT3() {
		return t3;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}
}
