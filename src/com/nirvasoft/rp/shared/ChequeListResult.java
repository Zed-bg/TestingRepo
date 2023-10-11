package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ACCOUNTINFO")
@XmlAccessorType(XmlAccessType.FIELD)

public class ChequeListResult {

	@XmlElement(name = "AccountNo")
	private String accountNo;

	@XmlElement(name = "Code")
	private String code;

	@XmlElement(name = "Desc")
	private String desc;

	@XmlElement(name = "ChequeList")
	private ArrayList<CheckIssueData> chequeList;

	public ChequeListResult() {
		clearProperty();
	}

	void clearProperty() {
		accountNo = "";
		code = "";
		desc = "";
		chequeList = new ArrayList<>();
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

	public ArrayList<CheckIssueData> getChequeList() {
		return chequeList;
	}

	public void setChequeList(ArrayList<CheckIssueData> chequeList) {
		this.chequeList = chequeList;
	}

}
