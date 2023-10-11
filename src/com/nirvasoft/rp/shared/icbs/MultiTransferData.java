package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class MultiTransferData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pTransferNo;
	private byte pDrCr;
	private int pSerialNo;
	private String pTransDate;
	private String pAccType;
	private String pAccNumber;
	private String pCommAccNumber;
	private String pChequeNo;
	private double pAmount;
	private double pCommission;
	private String pName;
	private String pNRC;
	private double pBalance;
	private int pStatus;
	private int pTransRef;
	private String pSubRef;
	private int pSystemCode;
	private String pNarration;
	private String pt1;
	private String pt2;
	private String pt3;
	private double pn1;
	private double pn2;
	private double pn3;
	private String pCurrencyCode;
	private String pBranchCode;
	private double pN4;
	private String t4 = "";
	private String t5 = "";

	private String t6 = "";
	private long n5 = 0;
	private long n6 = 0;

	public String getAccountNumber() {
		return pAccNumber;
	}

	public String getAccType() {
		return pAccType;
	}

	public double getAmount() {
		return pAmount;
	}

	public double getBalance() {
		return pBalance;
	}

	public String getBranchCode() {
		return pBranchCode;
	}

	public String getChequeNo() {
		return pChequeNo;
	}

	public String getCommAccNumber() {
		return pCommAccNumber;
	}

	public double getCommission() {
		return pCommission;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public byte getDrCr() {
		return pDrCr;
	}

	public double getN1() {
		return pn1;
	}

	public double getN2() {
		return pn2;
	}

	public double getN3() {
		return pn3;
	}

	public double getN4() {
		return pN4;
	}

	public long getN5() {
		return n5;
	}

	public long getN6() {
		return n6;
	}

	public String getName() {
		return pName;
	}

	public String getNarration() {
		return pNarration;
	}

	public String getNRC() {
		return pNRC;
	}

	public int getSerialNo() {
		return pSerialNo;
	}

	public int getStatus() {
		return pStatus;
	}

	public String getSubRef() {
		return pSubRef;
	}

	public int getSystemCode() {
		return pSystemCode;
	}

	public String getT1() {
		return pt1;
	}

	public String getT2() {
		return pt2;
	}

	public String getT3() {
		return pt3;
	}

	public String getT4() {
		return t4;
	}

	public String getT5() {
		return t5;
	}

	public String getT6() {
		return t6;
	}

	public String getTransDate() {
		return pTransDate;
	}

	public int getTransferNo() {
		return pTransferNo;
	}

	public int getTransRef() {
		return pTransRef;
	}

	public void setAccountNumber(String p) {
		pAccNumber = p;
	}

	public void setAccType(String p) {
		pAccType = p;
	}

	public void setAmount(double p) {
		pAmount = p;
	}

	public void setBalance(double p) {
		pBalance = p;
	}

	public void setBranchCode(String p) {
		pBranchCode = p;
	}

	public void setChequeNo(String p) {
		pChequeNo = p;
	}

	public void setCommAccNumber(String p) {
		pCommAccNumber = p;
	}

	public void setCommission(double p) {
		pCommission = p;
	}

	public void setCurrencyCode(String p) {
		pCurrencyCode = p;
	}

	public void setDrCr(byte p) {
		pDrCr = p;
	}

	public void setN1(double p) {
		pn1 = p;
	}

	public void setN2(double p) {
		pn2 = p;
	}

	public void setN3(double p) {
		pn3 = p;
	}

	public void setN4(double pN4) {
		this.pN4 = pN4;
	}

	public void setN5(long n5) {
		this.n5 = n5;
	}

	public void setN6(long n6) {
		this.n6 = n6;
	}

	public void setName(String p) {
		pName = p;
	}

	public void setNarration(String p) {
		pNarration = p;
	}

	public void setNRC(String p) {
		pNRC = p;
	}

	public void setSerialNo(int p) {
		pSerialNo = p;
	}

	public void setStatus(int p) {
		pStatus = p;
	}

	public void setSubRef(String p) {
		pSubRef = p;
	}

	public void setSystemCode(int p) {
		pSystemCode = p;
	}

	public void setT1(String p) {
		pt1 = p;
	}

	public void setT2(String p) {
		pt2 = p;
	}

	public void setT3(String p) {
		pt3 = p;
	}

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public void setT5(String t5) {
		this.t5 = t5;
	}

	public void setT6(String t6) {
		this.t6 = t6;
	}

	public void setTransDate(String p) {
		pTransDate = p;
	}

	public void setTransferNo(int p) {
		pTransferNo = p;
	}

	public void setTransRef(int p) {
		pTransRef = p;
	}

}
