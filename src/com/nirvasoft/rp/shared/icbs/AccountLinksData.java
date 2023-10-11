package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class AccountLinksData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parentAccount = "";
	private String childAccount = "";
	private String linkDate = "";
	private String remark = "";
	private int priority = 0;

	public void AccountCustomerData() {
		ClearProperty();
	}

	private void ClearProperty() {
		parentAccount = "";
		childAccount = "";
		linkDate = "";
		remark = "";
		priority = 0;
	}

	public String getChildAccount() {
		return childAccount;
	}

	public String getLinkDate() {
		return linkDate;
	}

	public String getParentAccount() {
		return parentAccount;
	}

	public int getPriority() {
		return priority;
	}

	public String getRemark() {
		return remark;
	}

	public void setChildAccount(String childAccount) {
		this.childAccount = childAccount;
	}

	public void setLinkDate(String linkDate) {
		this.linkDate = linkDate;
	}

	public void setParentAccount(String parentAccount) {
		this.parentAccount = parentAccount;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
