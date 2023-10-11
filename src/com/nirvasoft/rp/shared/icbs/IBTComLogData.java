package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class IBTComLogData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283749901867094509L;

	private long pAutoKey;
	private String pOnlSno;
	private short pIBType;
	private String pTransDate;
	private double pAmount;
	private String pDrAccNumber;
	private String pCrAccNumber;
	private Short pStatus;
	private Short pTransType;
	private String pBYear;
	private String pT1;
	private String pT2;
	private String pT3;
	private double pN1;
	private double pN2;
	private double pN3;
	private String pFromBranch;
	private String pToBranch;
	private int pTransref;
	private int pTransno;

	public IBTComLogData() {
		clearProperty();
	}

	private void clearProperty() {
		pAutoKey = 0;
		pOnlSno = "";
		pIBType = 0;
		pTransDate = "";
		pAmount = 0;
		pDrAccNumber = "";
		pCrAccNumber = "";
		pStatus = 0;
		pTransType = 0;
		pBYear = "";
		pT1 = "";
		pT2 = "";
		pT3 = "";
		pN1 = 0;
		pN2 = 0;
		pN3 = 0;
		pFromBranch = "";
		pToBranch = "";
		pTransref = 0;
		pTransno = 0;
	}

	public double getAmount() {
		return pAmount;
	}

	public long getAutoKey() {
		return pAutoKey;
	}

	public String getBYear() {
		return pBYear;
	}

	public String getCrAccNumber() {
		return pCrAccNumber;
	}

	public String getDrAccNumber() {
		return pDrAccNumber;
	}

	public String getFromBranch() {
		return pFromBranch;
	}

	public short getIBType() {
		return pIBType;
	}

	public double getN1() {
		return pN1;
	}

	public double getN2() {
		return pN2;
	}

	public double getN3() {
		return pN3;
	}

	public String getOnlSno() {
		return pOnlSno;
	}

	public Short getStatus() {
		return pStatus;
	}

	public String getT1() {
		return pT1;
	}

	public String getT2() {
		return pT2;
	}

	public String getT3() {
		return pT3;
	}

	public String getToBranch() {
		return pToBranch;
	}

	public String getTransDate() {
		return pTransDate;
	}

	public int getTransno() {
		return pTransno;
	}

	public int getTransref() {
		return pTransref;
	}

	public Short getTransType() {
		return pTransType;
	}

	public void setAmount(double pAmount) {
		this.pAmount = pAmount;
	}

	public void setAutoKey(long pAutoKey) {
		this.pAutoKey = pAutoKey;
	}

	public void setBYear(String pBYear) {
		this.pBYear = pBYear;
	}

	public void setCrAccNumber(String pCrAccNumber) {
		this.pCrAccNumber = pCrAccNumber;
	}

	public void setDrAccNumber(String pDrAccNumber) {
		this.pDrAccNumber = pDrAccNumber;
	}

	public void setFromBranch(String pFromBranch) {
		this.pFromBranch = pFromBranch;
	}

	public void setIBType(short pIBType) {
		this.pIBType = pIBType;
	}

	public void setN1(double pN1) {
		this.pN1 = pN1;
	}

	public void setN2(double pN2) {
		this.pN2 = pN2;
	}

	public void setN3(double pN3) {
		this.pN3 = pN3;
	}

	public void setOnlSno(String pOnlSno) {
		this.pOnlSno = pOnlSno;
	}

	public void setStatus(Short pStatus) {
		this.pStatus = pStatus;
	}

	public void setT1(String pT1) {
		this.pT1 = pT1;
	}

	public void setT2(String pT2) {
		this.pT2 = pT2;
	}

	public void setT3(String pT3) {
		this.pT3 = pT3;
	}

	public void setToBranch(String pToBranch) {
		this.pToBranch = pToBranch;
	}

	public void setTransDate(String pTransDate) {
		this.pTransDate = pTransDate;
	}

	public void setTransno(int pTransno) {
		this.pTransno = pTransno;
	}

	public void setTransref(int pTransref) {
		this.pTransref = pTransref;
	}

	public void setTransType(Short pTransType) {
		this.pTransType = pTransType;
	}

}
