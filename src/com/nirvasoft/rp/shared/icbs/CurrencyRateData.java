package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrencyRateData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3005364482463803417L;
	private long pAutokey;
	private String pCurrencyCode;
	private String pBranchCode;
	private String pDenoName;
	private String pUnitName;
	private String pUnitType;
	private int pNote;
	private double pBuyRate;
	private String pBuyRateOperator;
	private double pSellRate;
	private String pSellRateOperator;
	private String pLastUpdatedDate;
	private String pLastUpdatedTime;
	private String pRemark;
	private String pUserID;
	private double pN1;
	private int pN2;
	private int pN3;
	private String pT1;
	private String pT2;
	private String pT3;
	private String pT4;
	private String pT5;

	ArrayList<CurrencyRateData> pCurList = new ArrayList<CurrencyRateData>();

	public CurrencyRateData() {
		ClearProperties();
	}

	public void ClearProperties() {
		pAutokey = 0;
		pCurrencyCode = "";
		pBranchCode = "";
		pDenoName = "";
		pUnitName = "";
		pUnitType = "";
		pNote = 0;
		pBuyRate = 0;
		pBuyRateOperator = "";
		pSellRate = 0;
		pSellRateOperator = "";
		pLastUpdatedDate = "";
		pLastUpdatedTime = "";
		pRemark = "";
		pUserID = "";
		pN1 = 0;
		pN2 = 0;
		pN3 = 0;
		pT1 = "";
		pT2 = "";
		pT3 = "";
		pT4 = "";
		pT5 = "";
	}

	public long getAutokey() {
		return pAutokey;
	}

	public String getBranchCode() {
		return pBranchCode;
	}

	public double getBuyRate() {
		return pBuyRate;
	}

	public String getBuyRateOperator() {
		return pBuyRateOperator;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public String getDenoName() {
		return pDenoName;
	}

	public String getLastUpdatedDate() {
		return pLastUpdatedDate;
	}

	public String getLastUpdatedTime() {
		return pLastUpdatedTime;
	}

	public int getN2() {
		return pN2;
	}

	public int getN3() {
		return pN3;
	}

	public double getNaturalRate() {
		return pN1;
	}

	public String getNaturalRateOperator() {
		return pT1;
	}

	public int getNote() {
		return pNote;
	}

	public ArrayList<CurrencyRateData> getpCurList() {
		return pCurList;
	}

	public String getRemark() {
		return pRemark;
	}

	public double getSellRate() {
		return pSellRate;
	}

	public String getSellRateOperator() {
		return pSellRateOperator;
	}

	public String getT2() {
		return pT2;
	}

	public String getT3() {
		return pT3;
	}

	public String getT4() {
		return pT4;
	}

	public String getT5() {
		return pT5;
	}

	public String getUnitName() {
		return pUnitName;
	}

	public String getUnitType() {
		return pUnitType;
	}

	public String getUserID() {
		return pUserID;
	}

	public void setAutokey(long pAutokey) {
		this.pAutokey = pAutokey;
	}

	public void setBranchCode(String pBranchCode) {
		this.pBranchCode = pBranchCode;
	}

	public void setBuyRate(double pBuyRate) {
		this.pBuyRate = pBuyRate;
	}

	public void setBuyRateOperator(String pBuyRateOperator) {
		this.pBuyRateOperator = pBuyRateOperator;
	}

	public void setCurrencyCode(String pCurrencyCode) {
		this.pCurrencyCode = pCurrencyCode;
	}

	public void setDenoName(String pDenoName) {
		this.pDenoName = pDenoName;
	}

	public void setLastUpdatedDate(String pLastUpdatedDate) {
		this.pLastUpdatedDate = pLastUpdatedDate;
	}

	public void setLastUpdatedTime(String pLastUpdatedTime) {
		this.pLastUpdatedTime = pLastUpdatedTime;
	}

	public void setN2(int pN2) {
		this.pN2 = pN2;
	}

	public void setN3(int pN3) {
		this.pN3 = pN3;
	}

	public void setNaturalRate(double pNaturalRate) {
		this.pN1 = pNaturalRate;
	}

	public void setNaturalRateOperator(String pT1) {
		this.pT1 = pT1;
	}

	public void setNote(int pNote) {
		this.pNote = pNote;
	}

	public void setpCurList(ArrayList<CurrencyRateData> pCurList) {
		this.pCurList = pCurList;
	}

	public void setRemark(String pRemark) {
		this.pRemark = pRemark;
	}

	public void setSellRate(double pSellRate) {
		this.pSellRate = pSellRate;
	}

	public void setSellRateOperator(String pSellRateOperator) {
		this.pSellRateOperator = pSellRateOperator;
	}

	public void setT2(String pT2) {
		this.pT2 = pT2;
	}

	public void setT3(String pT3) {
		this.pT3 = pT3;
	}

	public void setT4(String pT4) {
		this.pT4 = pT4;
	}

	public void setT5(String pT5) {
		this.pT5 = pT5;
	}

	public void setUnitName(String pUnitName) {
		this.pUnitName = pUnitName;
	}

	public void setUnitType(String pUnitType) {
		this.pUnitType = pUnitType;
	}

	public void setUserID(String pUserID) {
		this.pUserID = pUserID;
	}
}
