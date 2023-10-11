package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

public class RateSetupData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7010939318145176038L;
	private int pSerialNo;
	private int pType;
	private String FromBank;
	private String FromBankCode;
	private String FromBranchCode;
	private String ToBankCode;
	private String ToBranchCode;
	private String pFromBranch;
	private String pToBank;
	private String pToBranch;
	private int pInOut;
	private double pCom1;
	private String pCom1Type;
	private double pCom1Min;
	private double pCom1Max;
	private double pCom2;
	private String pCom2Type;
	private double pCom2Min;
	private double pCom2Max;
	private double pCom3;
	private String pCom3Type;
	private double pCom3Min;
	private double pCom3Max;
	private double pCom4;
	private String pCom4Type;
	private double pCom4Min;
	private double pCom4Max;
	private double pCom5;
	private String pCom5Type;
	private double pCom5Min;
	private double pCom5Max;
	private double pCom6;
	private String pCom6Type;
	private double pCom6Min;
	private double pCom6Max;
	private double pCom7;
	private String pCom7Type;
	private double pCom7Min;
	private double pCom7Max;
	private double pCom8;
	private String pCom8Type;
	private double pCom8Min;
	private double pCom8Max;
	private double pCom9;
	private String pCom9Type;
	private double pCom9Min;
	private double pCom9Max;
	private double pCom10;
	private String pCom10Type;
	private double pCom10Min;
	private double pCom10Max;
	private double pCom11;
	private String pCom11Type;
	private double pCom11Min;
	private double pCom11Max;
	private double pCom12;
	private String pCom12Type;
	private double pCom12Min;
	private double pCom12Max;
	private int pN1;
	private int pN2;
	private int pN3;
	private String pT1;
	private String pT2;
	private String pT3;
	private int pCom1DecimalType;
	private int pCom2DecimalType;
	private int pCom3DecimalType;
	private int pCom4DecimalType;
	private int pCom5DecimalType;
	private int pCom6DecimalType;
	private int pCom7DecimalType;
	private int pCom8DecimalType;
	private int pCom9DecimalType;
	private int pCom10DecimalType;
	private int pCom11DecimalType;
	private int pCom12DecimalType;

	// SSH

	private ArrayList<RateAmountRangeSetupData> pCom1AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom2AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom3AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom4AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom5AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom6AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom7AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom8AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom9AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom10AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom11AmountRangeDatalist;
	private ArrayList<RateAmountRangeSetupData> pCom12AmountRangeDatalist;

	public RateSetupData() {
		clearProperty();
	}

	public void clearProperty() {
		pSerialNo = 0;
		pType = 0;
		FromBank = "";
		pFromBranch = "";
		pToBank = "";
		pToBranch = "";
		FromBankCode = "";
		FromBranchCode = "";
		ToBankCode = "";
		ToBranchCode = "";
		pInOut = 0;
		pCom1 = 0;
		pCom1Type = "";
		pCom1Min = 0;
		pCom1Max = 0;
		pCom2 = 0;
		pCom2Type = "";
		pCom2Min = 0;
		pCom2Max = 0;
		pCom3 = 0;
		pCom3Type = "";
		pCom3Min = 0;
		pCom3Max = 0;
		pCom4 = 0;
		pCom4Type = "";
		pCom4Min = 0;
		pCom4Max = 0;
		pCom5 = 0;
		pCom5Type = "";
		pCom5Min = 0;
		pCom5Max = 0;
		pCom6 = 0;
		pCom6Type = "";
		pCom6Min = 0;
		pCom6Max = 0;
		pCom7 = 0;
		pCom7Type = "";
		pCom7Min = 0;
		pCom7Max = 0;
		pCom8 = 0;
		pCom8Type = "";
		pCom8Min = 0;
		pCom8Max = 0;
		pCom9 = 0;
		pCom9Type = "";
		pCom9Min = 0;
		pCom9Max = 0;
		pCom10 = 0;
		pCom10Type = "";
		pCom10Min = 0;
		pCom10Max = 0;
		pCom11 = 0;
		pCom11Type = "";
		pCom11Min = 0;
		pCom11Max = 0;
		pCom12 = 0;
		pCom12Type = "";
		pCom12Min = 0;
		pCom12Max = 0;
		pN1 = 0;
		pN2 = 0;
		pN3 = 0;
		pT1 = "";
		pT2 = "";
		pT3 = "";
		pCom1DecimalType = 1;
		pCom2DecimalType = 1;
		pCom3DecimalType = 1;
		pCom4DecimalType = 1;
		pCom5DecimalType = 1;
		pCom6DecimalType = 1;
		pCom7DecimalType = 1;
		pCom8DecimalType = 1;
		pCom9DecimalType = 1;
		pCom10DecimalType = 1;
		pCom11DecimalType = 1;
		pCom12DecimalType = 1;

		pCom1AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom2AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom3AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom4AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom5AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom6AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom7AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom8AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom9AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom10AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom11AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();
		pCom12AmountRangeDatalist = new ArrayList<RateAmountRangeSetupData>();

	}

	public double getCom1() {
		return pCom1;
	}

	public double getCom10() {
		return pCom10;
	}

	public ArrayList<RateAmountRangeSetupData> getCom10AmountRangeDatalist() {
		return pCom10AmountRangeDatalist;
	}

	public int getCom10DecimalType() {
		return pCom10DecimalType;
	}

	public double getCom10Max() {
		return pCom10Max;
	}

	public double getCom10Min() {
		return pCom10Min;
	}

	public String getCom10Type() {
		return pCom10Type;
	}

	public double getCom11() {
		return pCom11;
	}

	public ArrayList<RateAmountRangeSetupData> getCom11AmountRangeDatalist() {
		return pCom11AmountRangeDatalist;
	}

	public int getCom11DecimalType() {
		return pCom11DecimalType;
	}

	public double getCom11Max() {
		return pCom11Max;
	}

	public double getCom11Min() {
		return pCom11Min;
	}

	public String getCom11Type() {
		return pCom11Type;
	}

	public double getCom12() {
		return pCom12;
	}

	public ArrayList<RateAmountRangeSetupData> getCom12AmountRangeDatalist() {
		return pCom12AmountRangeDatalist;
	}

	public int getCom12DecimalType() {
		return pCom12DecimalType;
	}

	public double getCom12Max() {
		return pCom12Max;
	}

	public double getCom12Min() {
		return pCom12Min;
	}

	public String getCom12Type() {
		return pCom12Type;
	}

	public ArrayList<RateAmountRangeSetupData> getCom1AmountRangeDatalist() {
		return pCom1AmountRangeDatalist;
	}

	public int getCom1DecimalType() {
		return pCom1DecimalType;
	}

	public double getCom1Max() {
		return pCom1Max;
	}

	public double getCom1Min() {
		return pCom1Min;
	}

	public String getCom1Type() {
		return pCom1Type;
	}

	public double getCom2() {
		return pCom2;
	}

	public ArrayList<RateAmountRangeSetupData> getCom2AmountRangeDatalist() {
		return pCom2AmountRangeDatalist;
	}

	public int getCom2DecimalType() {
		return pCom2DecimalType;
	}

	public double getCom2Max() {
		return pCom2Max;
	}

	public double getCom2Min() {
		return pCom2Min;
	}

	public String getCom2Type() {
		return pCom2Type;
	}

	public double getCom3() {
		return pCom3;
	}

	public ArrayList<RateAmountRangeSetupData> getCom3AmountRangeDatalist() {
		return pCom3AmountRangeDatalist;
	}

	public int getCom3DecimalType() {
		return pCom3DecimalType;
	}

	public double getCom3Max() {
		return pCom3Max;
	}

	public double getCom3Min() {
		return pCom3Min;
	}

	public String getCom3Type() {
		return pCom3Type;
	}

	public double getCom4() {
		return pCom4;
	}

	public ArrayList<RateAmountRangeSetupData> getCom4AmountRangeDatalist() {
		return pCom4AmountRangeDatalist;
	}

	public int getCom4DecimalType() {
		return pCom4DecimalType;
	}

	public double getCom4Max() {
		return pCom4Max;
	}

	public double getCom4Min() {
		return pCom4Min;
	}

	public String getCom4Type() {
		return pCom4Type;
	}

	public double getCom5() {
		return pCom5;
	}

	public ArrayList<RateAmountRangeSetupData> getCom5AmountRangeDatalist() {
		return pCom5AmountRangeDatalist;
	}

	public int getCom5DecimalType() {
		return pCom5DecimalType;
	}

	public double getCom5Max() {
		return pCom5Max;
	}

	public double getCom5Min() {
		return pCom5Min;
	}

	public String getCom5Type() {
		return pCom5Type;
	}

	public double getCom6() {
		return pCom6;
	}

	public ArrayList<RateAmountRangeSetupData> getCom6AmountRangeDatalist() {
		return pCom6AmountRangeDatalist;
	}

	public int getCom6DecimalType() {
		return pCom6DecimalType;
	}

	public double getCom6Max() {
		return pCom6Max;
	}

	public double getCom6Min() {
		return pCom6Min;
	}

	public String getCom6Type() {
		return pCom6Type;
	}

	public double getCom7() {
		return pCom7;
	}

	public ArrayList<RateAmountRangeSetupData> getCom7AmountRangeDatalist() {
		return pCom7AmountRangeDatalist;
	}

	public int getCom7DecimalType() {
		return pCom7DecimalType;
	}

	public double getCom7Max() {
		return pCom7Max;
	}

	public double getCom7Min() {
		return pCom7Min;
	}

	public String getCom7Type() {
		return pCom7Type;
	}

	public double getCom8() {
		return pCom8;
	}

	public ArrayList<RateAmountRangeSetupData> getCom8AmountRangeDatalist() {
		return pCom8AmountRangeDatalist;
	}

	public int getCom8DecimalType() {
		return pCom8DecimalType;
	}

	public double getCom8Max() {
		return pCom8Max;
	}

	public double getCom8Min() {
		return pCom8Min;
	}

	public String getCom8Type() {
		return pCom8Type;
	}

	public double getCom9() {
		return pCom9;
	}

	public ArrayList<RateAmountRangeSetupData> getCom9AmountRangeDatalist() {
		return pCom9AmountRangeDatalist;
	}

	public int getCom9DecimalType() {
		return pCom9DecimalType;
	}

	public double getCom9Max() {
		return pCom9Max;
	}

	public double getCom9Min() {
		return pCom9Min;
	}

	public String getCom9Type() {
		return pCom9Type;
	}

	public String getFromBank() {
		return FromBank;
	}

	public String getFromBankCode() {
		return FromBankCode;
	}

	public String getFromBranch() {
		return pFromBranch;
	}

	public String getFromBranchCode() {
		return FromBranchCode;
	}

	public int getInOut() {
		return pInOut;
	}

	public int getN1() {
		return pN1;
	}

	public int getN2() {
		return pN2;
	}

	public int getN3() {
		return pN3;
	}

	public int getSerialNo() {
		return pSerialNo;
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

	public String getToBank() {
		return pToBank;
	}

	public String getToBankCode() {
		return ToBankCode;
	}

	public String getToBranch() {
		return pToBranch;
	}

	public String getToBranchCode() {
		return ToBranchCode;
	}

	public int getType() {
		return pType;
	}

	public void setCom1(double pCom1) {
		this.pCom1 = pCom1;
	}

	public void setCom10(double pCom10) {
		this.pCom10 = pCom10;
	}

	public void setCom10AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom10AmountRangeDatalist) {
		this.pCom10AmountRangeDatalist = pCom10AmountRangeDatalist;
	}

	public void setCom10DecimalType(int pCom10DecimalType) {
		this.pCom10DecimalType = pCom10DecimalType;
	}

	public void setCom10Max(double pCom10Max) {
		this.pCom10Max = pCom10Max;
	}

	public void setCom10Min(double pCom10Min) {
		this.pCom10Min = pCom10Min;
	}

	public void setCom10Type(String pCom10Type) {
		this.pCom10Type = pCom10Type;
	}

	public void setCom11(double pCom11) {
		this.pCom11 = pCom11;
	}

	public void setCom11AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom11AmountRangeDatalist) {
		this.pCom11AmountRangeDatalist = pCom11AmountRangeDatalist;
	}

	public void setCom11DecimalType(int pCom11DecimalType) {
		this.pCom11DecimalType = pCom11DecimalType;
	}

	public void setCom11Max(double pCom11Max) {
		this.pCom11Max = pCom11Max;
	}

	public void setCom11Min(double pCom11Min) {
		this.pCom11Min = pCom11Min;
	}

	public void setCom11Type(String pCom11Type) {
		this.pCom11Type = pCom11Type;
	}

	public void setCom12(double pCom12) {
		this.pCom12 = pCom12;
	}

	public void setCom12AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom12AmountRangeDatalist) {
		this.pCom12AmountRangeDatalist = pCom12AmountRangeDatalist;
	}

	public void setCom12DecimalType(int pCom12DecimalType) {
		this.pCom12DecimalType = pCom12DecimalType;
	}

	public void setCom12Max(double pCom12Max) {
		this.pCom12Max = pCom12Max;
	}

	public void setCom12Min(double pCom12Min) {
		this.pCom12Min = pCom12Min;
	}

	public void setCom12Type(String pCom12Type) {
		this.pCom12Type = pCom12Type;
	}

	public void setCom1AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom1AmountRangeDatalist) {
		this.pCom1AmountRangeDatalist = pCom1AmountRangeDatalist;
	}

	public void setCom1DecimalType(int pCom1DecimalType) {
		this.pCom1DecimalType = pCom1DecimalType;
	}

	public void setCom1Max(double pCom1Max) {
		this.pCom1Max = pCom1Max;
	}

	public void setCom1Min(double pCom1Min) {
		this.pCom1Min = pCom1Min;
	}

	public void setCom1Type(String pCom1Type) {
		this.pCom1Type = pCom1Type;
	}

	public void setCom2(double pCom2) {
		this.pCom2 = pCom2;
	}

	public void setCom2AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom2AmountRangeDatalist) {
		this.pCom2AmountRangeDatalist = pCom2AmountRangeDatalist;
	}

	public void setCom2DecimalType(int pCom2DecimalType) {
		this.pCom2DecimalType = pCom2DecimalType;
	}

	public void setCom2Max(double pCom2Max) {
		this.pCom2Max = pCom2Max;
	}

	public void setCom2Min(double pCom2Min) {
		this.pCom2Min = pCom2Min;
	}

	public void setCom2Type(String pCom2Type) {
		this.pCom2Type = pCom2Type;
	}

	public void setCom3(double pCom3) {
		this.pCom3 = pCom3;
	}

	public void setCom3AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom3AmountRangeDatalist) {
		this.pCom3AmountRangeDatalist = pCom3AmountRangeDatalist;
	}

	public void setCom3DecimalType(int pCom3DecimalType) {
		this.pCom3DecimalType = pCom3DecimalType;
	}

	public void setCom3Max(double pCom3Max) {
		this.pCom3Max = pCom3Max;
	}

	public void setCom3Min(double pCom3Min) {
		this.pCom3Min = pCom3Min;
	}

	public void setCom3Type(String pCom3Type) {
		this.pCom3Type = pCom3Type;
	}

	public void setCom4(double pCom4) {
		this.pCom4 = pCom4;
	}

	public void setCom4AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom4AmountRangeDatalist) {
		this.pCom4AmountRangeDatalist = pCom4AmountRangeDatalist;
	}

	public void setCom4DecimalType(int pCom4DecimalType) {
		this.pCom4DecimalType = pCom4DecimalType;
	}

	public void setCom4Max(double pCom4Max) {
		this.pCom4Max = pCom4Max;
	}

	public void setCom4Min(double pCom4Min) {
		this.pCom4Min = pCom4Min;
	}

	public void setCom4Type(String pCom4Type) {
		this.pCom4Type = pCom4Type;
	}

	public void setCom5(double pCom5) {
		this.pCom5 = pCom5;
	}

	public void setCom5AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom5AmountRangeDatalist) {
		this.pCom5AmountRangeDatalist = pCom5AmountRangeDatalist;
	}

	public void setCom5DecimalType(int pCom5DecimalType) {
		this.pCom5DecimalType = pCom5DecimalType;
	}

	public void setCom5Max(double pCom5Max) {
		this.pCom5Max = pCom5Max;
	}

	public void setCom5Min(double pCom5Min) {
		this.pCom5Min = pCom5Min;
	}

	public void setCom5Type(String pCom5Type) {
		this.pCom5Type = pCom5Type;
	}

	public void setCom6(double pCom6) {
		this.pCom6 = pCom6;
	}

	public void setCom6AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom6AmountRangeDatalist) {
		this.pCom6AmountRangeDatalist = pCom6AmountRangeDatalist;
	}

	public void setCom6DecimalType(int pCom6DecimalType) {
		this.pCom6DecimalType = pCom6DecimalType;
	}

	public void setCom6Max(double pCom6Max) {
		this.pCom6Max = pCom6Max;
	}

	public void setCom6Min(double pCom6Min) {
		this.pCom6Min = pCom6Min;
	}

	public void setCom6Type(String pCom6Type) {
		this.pCom6Type = pCom6Type;
	}

	public void setCom7(double pCom7) {
		this.pCom7 = pCom7;
	}

	public void setCom7AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom7AmountRangeDatalist) {
		this.pCom7AmountRangeDatalist = pCom7AmountRangeDatalist;
	}

	public void setCom7DecimalType(int pCom7DecimalType) {
		this.pCom7DecimalType = pCom7DecimalType;
	}

	public void setCom7Max(double pCom7Max) {
		this.pCom7Max = pCom7Max;
	}

	public void setCom7Min(double pCom7Min) {
		this.pCom7Min = pCom7Min;
	}

	public void setCom7Type(String pCom7Type) {
		this.pCom7Type = pCom7Type;
	}

	public void setCom8(double pCom8) {
		this.pCom8 = pCom8;
	}

	public void setCom8AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom8AmountRangeDatalist) {
		this.pCom8AmountRangeDatalist = pCom8AmountRangeDatalist;
	}

	public void setCom8DecimalType(int pCom8DecimalType) {
		this.pCom8DecimalType = pCom8DecimalType;
	}

	public void setCom8Max(double pCom8Max) {
		this.pCom8Max = pCom8Max;
	}

	public void setCom8Min(double pCom8Min) {
		this.pCom8Min = pCom8Min;
	}

	public void setCom8Type(String pCom8Type) {
		this.pCom8Type = pCom8Type;
	}

	public void setCom9(double pCom9) {
		this.pCom9 = pCom9;
	}

	public void setCom9AmountRangeDatalist(ArrayList<RateAmountRangeSetupData> pCom9AmountRangeDatalist) {
		this.pCom9AmountRangeDatalist = pCom9AmountRangeDatalist;
	}

	public void setCom9DecimalType(int pCom9DecimalType) {
		this.pCom9DecimalType = pCom9DecimalType;
	}

	public void setCom9Max(double pCom9Max) {
		this.pCom9Max = pCom9Max;
	}

	public void setCom9Min(double pCom9Min) {
		this.pCom9Min = pCom9Min;
	}

	public void setCom9Type(String pCom9Type) {
		this.pCom9Type = pCom9Type;
	}

	public void setFromBank(String fromBank) {
		FromBank = fromBank;
	}

	public void setFromBankCode(String fromBankCode) {
		FromBankCode = fromBankCode;
	}

	public void setFromBranch(String pFromBranch) {
		this.pFromBranch = pFromBranch;
	}

	public void setFromBranchCode(String fromBranchCode) {
		FromBranchCode = fromBranchCode;
	}

	public void setInOut(int pInOut) {
		this.pInOut = pInOut;
	}

	public void setN1(int pN1) {
		this.pN1 = pN1;
	}

	public void setN2(int pN2) {
		this.pN2 = pN2;
	}

	public void setN3(int pN3) {
		this.pN3 = pN3;
	}

	public void setSerialNo(int pSerialNo) {
		this.pSerialNo = pSerialNo;
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

	public void setToBank(String pToBank) {
		this.pToBank = pToBank;
	}

	public void setToBankCode(String toBankCode) {
		ToBankCode = toBankCode;
	}

	public void setToBranch(String pToBranch) {
		this.pToBranch = pToBranch;
	}

	public void setToBranchCode(String toBranchCode) {
		ToBranchCode = toBranchCode;
	}

	public void setType(int pType) {
		this.pType = pType;
	}

}
