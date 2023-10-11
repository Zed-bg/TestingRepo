package com.nirvasoft.rp.framework;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class result {

	private double amount = 0.00;
	private double minamount = 0.00;
	private String Keyst = "";
	private boolean state = false;
	private String merchantID = "";
	private String userid = "";
	private String msgCode = "";
	private String msgDesc = "";
	private long keyResult = 0;
	private String loginID = ""; // Login ID
	private String phNo = "";
	private String sessionID = "";
	private String otpcode = "";
	private String email = "";
	private ArrayList<Long> longResult = new ArrayList<Long>();
	private ArrayList<String> stringResult = new ArrayList<String>();
	private String accnumber = "";
	private long[] key;

	public result() {
		clearProperties();
	}

	private void clearProperties() {
		state = false;
		merchantID = "";
		userid = "";
		msgCode = "";
		msgDesc = "";
		keyResult = 0;
		email = "";
		longResult = new ArrayList<Long>();
		stringResult = new ArrayList<String>();
		accnumber = "";
	}

	public double getMinamount() {
		return minamount;
	}

	public void setMinamount(double minamount) {
		this.minamount = minamount;
	}

	public double getAmount() {
		return amount;
	}

	public String getEmail() {
		return email;
	}

	public long[] getKey() {
		return key;
	}

	public long getKeyResult() {
		return keyResult;
	}

	public String getKeyst() {
		return Keyst;
	}

	public String getLoginID() {
		return loginID;
	}

	public ArrayList<Long> getLongResult() {
		return longResult;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public String getOtpcode() {
		return otpcode;
	}

	public String getPhNo() {
		return phNo;
	}

	public String getSessionID() {
		return sessionID;
	}

	public ArrayList<String> getStringResult() {
		return stringResult;
	}

	public String getUserid() {
		return userid;
	}

	public boolean isState() {
		return state;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setKey(long[] key) {
		this.key = key;
	}

	public void setKeyResult(long keyResult) {
		this.keyResult = keyResult;
	}

	public void setKeyst(String keyst) {
		Keyst = keyst;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public void setLongResult(ArrayList<Long> longResult) {
		this.longResult = longResult;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public void setOtpcode(String otpcode) {
		this.otpcode = otpcode;
	}

	public void setPhNo(String phNo) {
		this.phNo = phNo;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void setStringResult(ArrayList<String> stringResult) {
		this.stringResult = stringResult;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}
	
}
