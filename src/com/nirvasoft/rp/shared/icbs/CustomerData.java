package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

import com.nirvasoft.rp.shared.AddressData;

@XmlRootElement
public class CustomerData {
	private String customerID;
	private String name;
	private String nrc;
	private String accountNo;
	private String balance;
	private String message;
	private AddressData pAddress;
	private String pIC;
	private String pOldIC;
	private String pOtherIC;

	public CustomerData() {
		clearProperty();
	}

	void clearProperty() {
		customerID = "";
		name = "";
		nrc = "";
		accountNo = "";
		balance = "";
		message = "";
	}

	public String getAccountNo() {
		return accountNo;
	}

	public AddressData getAddress() {
		return pAddress;
	}

	public String getBalance() {
		return balance;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getICs() {
		String ret = pIC;
		ret += (!pOldIC.trim().equals("") ? (!pIC.equals("") ? "; " : "") + pOtherIC + pOldIC : "");
		return ret;
	}

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name;
	}

	public String getNrc() {
		return nrc;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setAddress(AddressData address) {
		pAddress = address;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setBcNo(String bcNo) {
		pOtherIC = bcNo;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public void setIC(String nrcNo) {
		pIC = nrcNo;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public void setOldIC(String oldIC) {
		pOldIC = oldIC;
	}

	@Override
	public String toString() {
		return "{customerID=" + customerID + ", name=" + name + ", nrc=" + nrc + ", accountNo=" + accountNo
				+ ", balance=" + balance + ", message=" + message + "}";
	}
}
