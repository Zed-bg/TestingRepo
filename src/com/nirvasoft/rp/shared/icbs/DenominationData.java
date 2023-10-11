package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

public class DenominationData implements Serializable {
	private static final long serialVersionUID = 8692255645383445244L;

	public static String getCurrencySymbol(String currencyType, int denoMoneyType, int denoType) {
		String ret = "";
		switch (denoMoneyType) {
		case 0://
			if (currencyType.equals("MMK")) {
				ret = "K";
			} else if (currencyType.equals("USD")) {
				ret = "USD";
			} else if (currencyType.equals("SGD")) {
				ret = "SGD";
			} else if (currencyType.equals("FEC")) {
				ret = "FEC";
			} else if (currencyType.equals("EUR")) {
				ret = "EUR";
			}
			break;
		case 1: // Coin
			if (currencyType.equals("MMK")) {
				ret = "C";
			} else if (currencyType.equals("USD") || currencyType.equals("SGD")) {
				ret = "C";
			} else if (currencyType.equals("FEC")) {
				ret = "C"; // No Coin in FEC
			} else if (currencyType.equals("EUR")) {
				ret = "C";
			}
			break;
		case 2: // Cents and Pyas
			if (currencyType.equals("MMK")) {
				ret = "P";
			} else if (currencyType.equals("USD") || currencyType.equals("SGD")) {
				ret = "Cents";
			} else if (currencyType.equals("EUR")) {
				ret = "Cents";
			}
			break;
		default:
			break;
		}
		if (currencyType.equals("MMK")) {

		}
		return ret;
	}

	public static double getTotal(ArrayList<DenominationData> pArl) {
		return getTotalDeno(pArl) - getTotalRefund(pArl);
	}

	public static double getTotalDeno(ArrayList<DenominationData> pArl) {
		double ret = 0;
		for (int i = 0; i < pArl.size(); i++) {
			if (pArl.get(i).getDenoType() == 0) { // 0 for deno
				System.out.println(pArl.get(i).getSize());
				ret += pArl.get(i).getSize() * pArl.get(i).getQuantity();
			}
		}
		return ret;
	}

	public static double getTotalRefund(ArrayList<DenominationData> pArl) {
		double ret = 0;
		for (int i = 0; i < pArl.size(); i++) {
			if (pArl.get(i).getDenoType() == 1) { // 1 for refund
				ret += pArl.get(i).getSize() * pArl.get(i).getQuantity();
			}
		}
		return ret;
	}

	private double pSize;
	private long pQuantity;
	private short pDenoType; // For Refund Case
	private short pDenoMoneyType; // For Kyat, Pyar Case
	private String pCurrencyCode;
	private double pAmount;
	private double pBaseAmount;
	private String pDenoName;
	private String pReferenceModuleCode;

	private String pBranchCode;
	private int PreAccountTransactionRefNo;
	private int pDrCr;
	private double pTotalAmount;
	private long pTransNo;
	private int pDenoSrNo;
	private String pEntryDate;
	private double pK5000;
	private double pK1000;
	private double pK500;
	private double pK200;
	private double pK100;
	private double pK90;
	private double pK50;
	private double pK45;
	private double pK20;

	private double pK15;
	private double pK10;
	private double pK5;
	private double pK1;
	private double pC100;
	private double pC50;

	private double pC25;
	private double pC10;
	private double pC5;
	private double pC1;
	private double pP50;

	private double pP25;
	private double pP10;
	private double pP5;
	private double pP1;
	private String pRefNo;
	private double pSerialNo;
	private String pSlipNo;
	private String pAccountNumber;
	private String pT1;

	private String pTellerID;
	private String pCounterID;
	private String pTrpsNo;
	private String pUserID;
	private String pRemark;
	private String pUniqueNo;
	private double pN8 = 0.00;
	private double pN9 = 0.00;
	private double pN10 = 0.00;
	private String pT11 = "";

	private String pT12 = "";

	private String pT13 = "";
	private String pT14 = "";

	private String pT15 = "";

	public DenominationData() {
		clearProperty();
	}

	void clearProperty() {
		pSize = 0;
		pQuantity = 0;
		pDenoType = 0;
		pDenoSrNo = 0;
		pEntryDate = "";

		pK5000 = 0;
		pK1000 = 0;
		pK500 = 0;
		pK200 = 0;
		pK100 = 0;
		pK90 = 0;
		pK50 = 0;
		pK45 = 0;
		pK20 = 0;
		pK15 = 0;
		pK10 = 0;
		pK5 = 0;
		pK1 = 0;

		pC100 = 0;
		pC50 = 0;
		pC25 = 0;
		pC10 = 0;
		pC5 = 0;
		pC1 = 0;

		pP50 = 0;
		pP25 = 0;
		pP10 = 0;
		pP5 = 0;
		pP1 = 0;

		pRefNo = "";
		pSerialNo = 0;
		pSlipNo = "";
		pAccountNumber = "";
		pT1 = "";
		pCounterID = "";
		pTellerID = "";
		pTrpsNo = "";
		pUserID = "";
		pDenoMoneyType = 0;
		pAmount = 0;
		pBaseAmount = 0;
		pDenoName = "";
		pBranchCode = "";
		pReferenceModuleCode = "";
		PreAccountTransactionRefNo = 0;
		pDrCr = 1;
		pTotalAmount = 0;
		pCurrencyCode = "";
		pRemark = "";
		pUniqueNo = "";
		pN8 = 0.00;
		pN9 = 0.00;
		pN10 = 0.00;
		pT11 = "";
		pT12 = "";
		pT13 = "";
		pT14 = "";
		pT15 = "";

	}

	public String getAccountNumber() {
		return pAccountNumber;
	}

	public double getAmount() {
		return pAmount;
	}

	public double getBaseAmount() {
		return pBaseAmount;
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

	public double getC100() {
		return pC100;
	}

	public double getC25() {
		return pC25;
	}

	public double getC5() {
		return pC5;
	}

	public double getC50() {
		return pC50;
	}

	public String getCounterID() {
		return pCounterID;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public short getDenoMoneyType() {
		return pDenoMoneyType;
	}

	public String getDenoName() {
		return pDenoName;
	}

	public int getDenoSrNo() {
		return pDenoSrNo;
	}

	public short getDenoType() {
		return pDenoType;
	}

	public int getDrCr() {
		return pDrCr;
	}

	public String getEntryDate() {
		return pEntryDate;
	}

	public double getK1() {
		return pK1;
	}

	public double getK10() {
		return pK10;
	}

	public double getK100() {
		return pK100;
	}

	public double getK1000() {
		return pK1000;
	}

	public double getK15() {
		return pK15;
	}

	public double getK20() {
		return pK20;
	}

	public double getK200() {
		return pK200;
	}

	public double getK45() {
		return pK45;
	}

	public double getK5() {
		return pK5;
	}

	public double getK50() {
		return pK50;
	}

	public double getK500() {
		return pK500;
	}

	public double getK5000() {
		return pK5000;
	}

	public double getK90() {
		return pK90;
	}

	public double getP1() {
		return pP1;
	}

	public double getP10() {
		return pP10;
	}

	public double getP25() {
		return pP25;
	}

	public double getP5() {
		return pP5;
	}

	public double getP50() {
		return pP50;
	}

	public double getpN10() {
		return pN10;
	}

	public double getpN8() {
		return pN8;
	}

	public double getpN9() {
		return pN9;
	}

	public int getPreAccountTransactionRefNo() {
		return PreAccountTransactionRefNo;
	}

	public String getpT11() {
		return pT11;
	}

	public String getpT12() {
		return pT12;
	}

	public String getpT13() {
		return pT13;
	}

	public String getpT14() {
		return pT14;
	}

	public String getpT15() {
		return pT15;
	}

	public String getpUserID() {
		return pUserID;
	}

	public long getQuantity() {
		return pQuantity;
	}

	public String getReferenceModuleCode() {
		return pReferenceModuleCode;
	}

	public String getRefNo() {
		return pRefNo;
	}

	public String getRemark() {
		return pRemark;
	}

	public double getSerialNo() {
		return pSerialNo;
	}

	public double getSize() {
		return pSize;
	}

	public String getSlipNo() {
		return pSlipNo;
	}

	public String getT1() {
		return pT1;
	}

	public String getTellerID() {
		return pTellerID;
	}

	public double getTotalAmount() {
		return pTotalAmount;
	}

	public long getTransNo() {
		return pTransNo;
	}

	public String getTrpsNo() {
		return pTrpsNo;
	}

	public String getUniqueNo() {
		return pUniqueNo;
	}

	public void setAccountNumber(String p) {
		pAccountNumber = p;
	}

	public void setAmount(double pAmount) {
		this.pAmount = pAmount;
	}

	public void setBaseAmount(double pBaseAmount) {
		this.pBaseAmount = pBaseAmount;
	}

	public void setBranchCode(String pBranchCode) {
		this.pBranchCode = pBranchCode;
	}

	public void setC1(double p) {
		pC1 = p;
	}

	public void setC10(double p) {
		pC10 = p;
	}

	public void setC100(double p) {
		pC100 = p;
	}

	public void setC25(double p) {
		pC25 = p;
	}

	public void setC5(double p) {
		pC5 = p;
	}

	public void setC50(double p) {
		pC50 = p;
	}

	public void setCounterID(String p) {
		pCounterID = p;
	}

	public void setCurrencyCode(String p) {
		pCurrencyCode = p;
	}

	public void setDenoMoneyType(short p) {
		pDenoMoneyType = p;
	}

	public void setDenoName(String pDenoName) {
		this.pDenoName = pDenoName;
	}

	public void setDenoSrNo(int p) {
		pDenoSrNo = p;
	}

	public void setDenoType(short p) {
		pDenoType = p;
	}

	public void setDrCr(int pDrCr) {
		this.pDrCr = pDrCr;
	}

	public void setEntryDate(String p) {
		pEntryDate = p;
	}

	public void setK1(double p) {
		pK1 = p;
	}

	public void setK10(double p) {
		pK10 = p;
	}

	public void setK100(double p) {
		pK100 = p;
	}

	public void setK1000(double p) {
		pK1000 = p;
	}

	public void setK15(double p) {
		pK15 = p;
	}

	public void setK20(double p) {
		pK20 = p;
	}

	public void setK200(double p) {
		pK200 = p;
	}

	public void setK45(double p) {
		pK45 = p;
	}

	public void setK5(double p) {
		pK5 = p;
	}

	public void setK50(double p) {
		pK50 = p;
	}

	public void setK500(double p) {
		pK500 = p;
	}

	public void setK5000(double p) {
		pK5000 = p;
	}

	public void setK90(double p) {
		pK90 = p;
	}

	public void setN10(double pN10) {
		this.pN10 = pN10;
	}

	public void setN8(double pN8) {
		this.pN8 = pN8;
	}

	public void setN9(double pN9) {
		this.pN9 = pN9;
	}

	public void setP1(double p) {
		pP1 = p;
	}

	public void setP10(double p) {
		pP10 = p;
	}

	public void setP25(double p) {
		pP25 = p;
	}

	public void setP5(double p) {
		pP5 = p;
	}

	public void setP50(double p) {
		pP50 = p;
	}

	public void setPreAccountTransactionRefNo(int preAccountTransactionRefNo) {
		PreAccountTransactionRefNo = preAccountTransactionRefNo;
	}

	public void setQuantity(long p) {
		pQuantity = p;
	}

	public void setReferenceModuleCode(String pReferenceModuleCode) {
		this.pReferenceModuleCode = pReferenceModuleCode;
	}

	public void setRefNo(String p) {
		pRefNo = p;
	}

	public void setRemark(String pRemark) {
		this.pRemark = pRemark;
	}

	public void setSerialNo(double p) {
		pSerialNo = p;
	}

	public void setSize(double p) {
		pSize = p;
	}

	public void setSlipNo(String p) {
		pSlipNo = p;
	}

	public void setT1(String p) {
		pT1 = p;
	}

	public void setT11(String pT11) {
		this.pT11 = pT11;
	}

	public void setT12(String pT12) {
		this.pT12 = pT12;
	}

	public void setT13(String pT13) {
		this.pT13 = pT13;
	}

	public void setT14(String pT14) {
		this.pT14 = pT14;
	}

	public void setT15(String pT15) {
		this.pT15 = pT15;
	}

	public void setTellerID(String p) {
		pTellerID = p;
	}

	public void setTotalAmount(double pTotalAmount) {
		this.pTotalAmount = pTotalAmount;
	}

	public void setTransNo(long p) {
		pTransNo = p;
	}

	public void setTrpsNo(String p) {
		pTrpsNo = p;
	}

	public void setUniqueNo(String uniqueNo) {
		pUniqueNo = uniqueNo;
	}

	public void setUserID(String p) {
		pUserID = p;
	}

}
