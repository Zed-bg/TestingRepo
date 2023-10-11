package com.nirvasoft.rp.shared.icbs;

/* 
TUN THURA THET 2011 04 21
*/
import java.io.Serializable;

public class GLData implements Serializable {
	private static final long serialVersionUID = -3713131806719518212L;

	/*
	 * private String pAccountNumber; private String pDescription; private
	 * String pDeptNo;
	 * 
	 * public void setAccountNumber(String p) {pAccountNumber = p;} public
	 * String getAccountNumber() {return pAccountNumber;} public void
	 * setDeptNo(String p) {pDeptNo = p;} public String getDeptNo() {return
	 * pDeptNo;} public void setDescription(String p) {pDescription = p;} public
	 * String getDescription() {return pDescription;}
	 */

	private String pAccountNumber;

	private String DeptNo;
	private String pDept;
	private String pDescription;
	private double pOB;
	private byte pAccType;
	private String pMainGroup;
	private String pSubGroup;
	private String pSubGroupDesc;
	private String pAccTypeDescription;
	private double pCurrentBalance;
	private double pC1;
	private double pC2;
	private double pC3;
	private double pC4;
	private double pC5;
	private double pC6;
	private double pC7;
	private double pC8;
	private double pC9;
	private double pC10;
	private double pC11;
	private double pC12;
	private String pCurrencyCode;
	private String pBranchCode;

	public GLData() {
		clearProperty();
	}

	private void clearProperty() {
		pAccountNumber = "";
		DeptNo = "";
		pDept = "";
		pDescription = "";
		pOB = 0;
		pAccType = 0;
		pMainGroup = "";
		pSubGroup = "";
		pSubGroupDesc = "";
		pAccTypeDescription = "";
		pCurrentBalance = 0;
		pC1 = 0;
		pC2 = 0;
		pC3 = 0;
		pC4 = 0;
		pC5 = 0;
		pC6 = 0;
		pC7 = 0;
		pC8 = 0;
		pC9 = 0;
		pC10 = 0;
		pC11 = 0;
		pC12 = 0;
		pCurrencyCode = "";
		pBranchCode = "";
	}

	public String getAccountNumber() {
		return pAccountNumber;
	}

	public byte getAccType() {
		return pAccType;
	}

	public String getAccTypeDescription() {
		return pAccTypeDescription;
	}

	public String getBranchCode() {
		return pBranchCode;
	}

	public double getC1() {
		return pC1;
	}

	public double getC10() {
		return pC10;
	}

	public double getC11() {
		return pC11;
	}

	public double getC12() {
		return pC12;
	}

	public double getC2() {
		return pC2;
	}

	public double getC3() {
		return pC3;
	}

	public double getC4() {
		return pC4;
	}

	public double getC5() {
		return pC5;
	}

	public double getC6() {
		return pC6;
	}

	public double getC7() {
		return pC7;
	}

	public double getC8() {
		return pC8;
	}

	public double getC9() {
		return pC9;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public double getCurrentBalance() {
		return pCurrentBalance;
	}

	public String getDept() {
		return pDept;
	}

	public String getDeptNo() {
		return DeptNo;
	}

	public String getDescription() {
		return pDescription;
	}

	public String getMainGroup() {
		return pMainGroup;
	}

	public double getOB() {
		return pOB;
	}

	public String getSubGroup() {
		return pSubGroup;
	}

	public String getSubGroupDesc() {
		return pSubGroupDesc;
	}

	public void setAccountNumber(String p) {
		pAccountNumber = p;
	}

	public void setAccType(byte pAccType) {
		this.pAccType = pAccType;
	}

	public void setAccTypeDescription(String pAccTypeDescription) {
		this.pAccTypeDescription = pAccTypeDescription;
	}

	public void setBranchCode(String pBranchCode) {
		this.pBranchCode = pBranchCode;
	}

	public void setC1(double pC1) {
		this.pC1 = pC1;
	}

	public void setC10(double pC10) {
		this.pC10 = pC10;
	}

	public void setC11(double pC11) {
		this.pC11 = pC11;
	}

	public void setC12(double pC12) {
		this.pC12 = pC12;
	}

	public void setC2(double pC2) {
		this.pC2 = pC2;
	}

	public void setC3(double pC3) {
		this.pC3 = pC3;
	}

	public void setC4(double pC4) {
		this.pC4 = pC4;
	}

	public void setC5(double pC5) {
		this.pC5 = pC5;
	}

	public void setC6(double pC6) {
		this.pC6 = pC6;
	}

	public void setC7(double pC7) {
		this.pC7 = pC7;
	}

	public void setC8(double pC8) {
		this.pC8 = pC8;
	}

	public void setC9(double pC9) {
		this.pC9 = pC9;
	}

	public void setCurrencyCode(String pCurrencyCode) {
		this.pCurrencyCode = pCurrencyCode;
	}

	public void setCurrentBalance(double pCurrentBalance) {
		this.pCurrentBalance = pCurrentBalance;
	}

	public void setDept(String pDept) {
		this.pDept = pDept;
	}

	public void setDeptNo(String DeptNo) {
		this.DeptNo = DeptNo;
	}

	public void setDescription(String p) {
		pDescription = p;
	}

	public void setMainGroup(String pMainGroup) {
		this.pMainGroup = pMainGroup;
	}

	public void setOB(double pOB) {
		this.pOB = pOB;
	}

	public void setSubGroup(String pSubGroup) {
		this.pSubGroup = pSubGroup;
	}

	public void setSubGroupDesc(String pSubGroupDesc) {
		this.pSubGroupDesc = pSubGroupDesc;
	}
}
