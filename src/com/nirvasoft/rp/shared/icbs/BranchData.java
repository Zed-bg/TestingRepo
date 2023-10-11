package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class BranchData implements Serializable {
	private static final long serialVersionUID = -4505380929965931110L;
	private String BranchCode;
	private String BranchName;
	private String BranchLocation;
	private String BranchOldCode;
	private String pServerName;
	private String pDbName;
	private String pUserId;
	private String pPassword;
	private boolean pIsLocal;
	private String pFax;
	private double pCommisssion;
	private boolean pIsRemit;
	private boolean pIsIrbs;
	private short pCreditCardZone;
	private int pBYear;

	public BranchData() {
		clearProperty();
	}

	public void clearProperty() {
		BranchCode = "";
		BranchName = "";
		BranchLocation = "";
		BranchOldCode = "";
		pServerName = "";
		pDbName = "";
		pUserId = "";
		pPassword = "";
		pIsLocal = false;
		pFax = "";
		pCommisssion = 0;
		pIsRemit = false;
		pIsIrbs = false;
		pCreditCardZone = 0;
		pBYear = 0;
	}

	public String getBranchCode() {
		return BranchCode;
	}

	public String getBranchLocation() {
		return BranchLocation;
	}

	public String getBranchName() {
		return BranchName;
	}

	public String getBranchOldCode() {
		return BranchOldCode;
	}

	public int getBYear() {
		return pBYear;
	}

	public double getCommission() {
		return pCommisssion;
	}

	public Short getCreditCardZone() {
		return pCreditCardZone;
	}

	public String getDbName() {
		return pDbName;
	}

	public String getFax() {
		return pFax;
	}

	public boolean getIsIrbs() {
		return pIsIrbs;
	}

	public boolean getIsLocal() {
		return pIsLocal;
	}

	public boolean getIsRemit() {
		return pIsRemit;
	}

	public String getPassword() {
		return pPassword;
	}

	public String getServerName() {
		return pServerName;
	}

	public String getUserId() {
		return pUserId;
	}

	public void setBranchCode(String p) {
		BranchCode = p;
	}

	public void setBranchLocation(String p) {
		BranchLocation = p;
	}

	public void setBranchName(String p) {
		BranchName = p;
	}

	public void setBranchOldCode(String branchOldCode) {
		BranchOldCode = branchOldCode;
	}

	public void setBYear(int p) {
		pBYear = p;
	}

	public void setCommission(double p) {
		pCommisssion = p;
	}

	public void setCreditCardZone(Short p) {
		pCreditCardZone = p;
	}

	public void setDbName(String p) {
		pDbName = p;
	}

	public void setFax(String p) {
		pFax = p;
	}

	public void setIsIrbs(boolean p) {
		pIsIrbs = p;
	}

	public void setIsLocal(boolean p) {
		pIsLocal = p;
	}

	public void setIsRemit(boolean p) {
		pIsRemit = p;
	}

	public void setPassword(String p) {
		pPassword = p;
	}

	public void setServerName(String p) {
		pServerName = p;
	}

	public void setUserid(String p) {
		pUserId = p;
	}
}
