package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class TempData implements Serializable {

	private static final long serialVersionUID = 51525865987834667L;

	private String pT1;
	private String pT2;
	private String pT3;
	private String pT4;
	private String pT5;
	private String pT6;
	private String pT7;
	private String pT8;
	private String pT9;
	private String pT10;
	private double pN1;
	private double pN2;
	private double pN3;
	private double pN4;
	private double pN5;
	private double pN6;
	private double pN7;
	private double pN8;
	private double pN9;
	private double pN10;
	private double pN11;
	private int pSysKey;

	public TempData() {
		clearProperty();
	}

	private void clearProperty() {
		setT1("");
		setT2("");
		setT3("");
		setT4("");
		setT5("");
		setT6("");
		setT7("");
		setT8("");
		setT9("");
		setT10("");
		setN1(0.0);
		setN2(0.0);
		setN3(0.0);
		setN4(0.0);
		setN5(0.0);
		setN6(0.0);
		setN7(0.0);
		setN8(0.0);
		setN9(0.0);
		setN10(0.0);
		setN11(0.0);
		setSysKey(0);
	}

	public double getN1() {
		return pN1;
	}

	public double getN10() {
		return pN10;
	}

	public double getN11() {
		return pN11;
	}

	public double getN2() {
		return pN2;
	}

	public double getN3() {
		return pN3;
	}

	public double getN4() {
		return pN4;
	}

	public double getN5() {
		return pN5;
	}

	public double getN6() {
		return pN6;
	}

	public double getN7() {
		return pN7;
	}

	public double getN8() {
		return pN8;
	}

	public double getN9() {
		return pN9;
	}

	public int getSysKey() {
		return pSysKey;
	}

	public String getT1() {
		return pT1;
	}

	public String getT10() {
		return pT10;
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

	public String getT6() {
		return pT6;
	}

	public String getT7() {
		return pT7;
	}

	public String getT8() {
		return pT8;
	}

	public String getT9() {
		return pT9;
	}

	public void setN1(double pN1) {
		this.pN1 = pN1;
	}

	public void setN10(double pN10) {
		this.pN10 = pN10;
	}

	public void setN11(double pN11) {
		this.pN11 = pN11;
	}

	public void setN2(double pN2) {
		this.pN2 = pN2;
	}

	public void setN3(double pN3) {
		this.pN3 = pN3;
	}

	public void setN4(double pN4) {
		this.pN4 = pN4;
	}

	public void setN5(double pN5) {
		this.pN5 = pN5;
	}

	public void setN6(double pN6) {
		this.pN6 = pN6;
	}

	public void setN7(double pN7) {
		this.pN7 = pN7;
	}

	public void setN8(double pN8) {
		this.pN8 = pN8;
	}

	public void setN9(double pN9) {
		this.pN9 = pN9;
	}

	public void setSysKey(int pSysKey) {
		this.pSysKey = pSysKey;
	}

	public void setT1(String pT1) {
		this.pT1 = pT1;
	}

	public void setT10(String pT10) {
		this.pT10 = pT10;
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

	public void setT6(String pT6) {
		this.pT6 = pT6;
	}

	public void setT7(String pT7) {
		this.pT7 = pT7;
	}

	public void setT8(String pT8) {
		this.pT8 = pT8;
	}

	public void setT9(String pT9) {
		this.pT9 = pT9;
	}

}
