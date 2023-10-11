package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExcludedAccount {
	private long autokey;
	private String createdDate;
	private String modifiedDate;
	private String tellerID;
	private String userID;
	private String customerID;
	private String excludedAccount;
	private String t1;
	private String t2;
	private String t3;
	private String t4;
	private String t5;
	private int n1;
	private int n2;
	private int n3;
	private int n4;
	private int n5;
	private String code;
	private String desc;

	public ExcludedAccount() {
		clearProperty();
	}

	private void clearProperty() {
		autokey = 0;
		createdDate = "";
		modifiedDate = "";
		tellerID = "";
		userID = "";
		customerID = "";
		excludedAccount = "";
		t1 = "";
		t2 = "";
		t3 = "";
		t4 = "";
		t5 = "";
		n1 = 0;
		n2 = 0;
		n3 = 0;
		n4 = 0;
		n5 = 0;
		code = "";
		desc = "";
	}

	public long getAutokey() {
		return autokey;
	}

	public String getCode() {
		return code;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getDesc() {
		return desc;
	}

	public String getExcludedAccount() {
		return excludedAccount;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public int getN1() {
		return n1;
	}

	public int getN2() {
		return n2;
	}

	public int getN3() {
		return n3;
	}

	public int getN4() {
		return n4;
	}

	public int getN5() {
		return n5;
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

	public String getT4() {
		return t4;
	}

	public String getT5() {
		return t5;
	}

	public String getTellerID() {
		return tellerID;
	}

	public String getUserID() {
		return userID;
	}

	public void setAutokey(long autokey) {
		this.autokey = autokey;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setExcludedAccount(String excludedAccount) {
		this.excludedAccount = excludedAccount;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setN1(int n1) {
		this.n1 = n1;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public void setN3(int n3) {
		this.n3 = n3;
	}

	public void setN4(int n4) {
		this.n4 = n4;
	}

	public void setN5(int n5) {
		this.n5 = n5;
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

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public void setT5(String t5) {
		this.t5 = t5;
	}

	public void setTellerID(String tellerID) {
		this.tellerID = tellerID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
