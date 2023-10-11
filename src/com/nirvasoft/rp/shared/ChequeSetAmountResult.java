package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ACCOUNTINFO")
@XmlAccessorType(XmlAccessType.FIELD)

public class ChequeSetAmountResult {

	@XmlElement(name = "AccountNo")
	private String accountNo;

	@XmlElement(name = "Code")
	private String code;

	@XmlElement(name = "Desc")
	private String desc;

	@XmlElement(name = "ChequeNo")
	private String chequeNo;

	@XmlElement(name = "Amount")
	private String amount;

	@XmlElement(name = "EntryDate")
	private String entryDate;

	@XmlElement(name = "DueDate")
	private String dueDate;

	@XmlElement(name = "N1")
	private String n1;
	@XmlElement(name = "N2")
	private String n2;
	@XmlElement(name = "N3")
	private String n3;
	@XmlElement(name = "N4")
	private String n4;
	@XmlElement(name = "N5")
	private String n5;

	@XmlElement(name = "T1")
	private String t1;
	@XmlElement(name = "T2")
	private String t2;
	@XmlElement(name = "T3")
	private String t3;
	@XmlElement(name = "T4")
	private String t4;
	@XmlElement(name = "T5")
	private String t5;

	public ChequeSetAmountResult() {
		clearProperty();
	}

	void clearProperty() {
		code = "";
		desc = "";
		accountNo = "";
		chequeNo = "";
		amount = "";
		entryDate = "";
		dueDate = "";
		t1 = "";
		t2 = "";
		t3 = "";
		t4 = "";
		t5 = "";
		n1 = "";
		n2 = "";
		n3 = "";
		n4 = "";
		n5 = "";
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getN1() {
		return n1;
	}

	public void setN1(String n1) {
		this.n1 = n1;
	}

	public String getN2() {
		return n2;
	}

	public void setN2(String n2) {
		this.n2 = n2;
	}

	public String getN3() {
		return n3;
	}

	public void setN3(String n3) {
		this.n3 = n3;
	}

	public String getN4() {
		return n4;
	}

	public void setN4(String n4) {
		this.n4 = n4;
	}

	public String getN5() {
		return n5;
	}

	public void setN5(String n5) {
		this.n5 = n5;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public String getT3() {
		return t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getT4() {
		return t4;
	}

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public String getT5() {
		return t5;
	}

	public void setT5(String t5) {
		this.t5 = t5;
	}

}
