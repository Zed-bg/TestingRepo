package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AgentData {

	private String userID;
	private String parentID;
	private String accNumber;
	private String name;
	private int recordStatus;
	private String t1;
	private String t2;
	private int n1;
	private int n2;

	public AgentData() {
		clearProperty();
	}

	private void clearProperty() {
		this.userID = "";
		this.parentID = "";
		this.accNumber = "";
		this.name = "";
		this.recordStatus = 0;
		this.t1 = "";
		this.t2 = "";
		this.n1 = 0;
		this.n2 = 0;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public int getN1() {
		return n1;
	}

	public int getN2() {
		return n2;
	}

	public String getName() {
		return name;
	}

	public String getParentID() {
		return parentID;
	}

	public int getRecordStatus() {
		return recordStatus;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public String getUserID() {
		return userID;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public void setN1(int n1) {
		this.n1 = n1;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "{userID=" + userID + ", parentID=" + parentID + ", accNumber=" + accNumber + "" + ", name=" + name
				+ ", recordStatus=" + recordStatus + ", t1=" + t1 + ", t2=" + t2 + ", n1=" + n1 + ", n2=" + n2 + "}";
	}
}
