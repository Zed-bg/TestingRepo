package com.nirvasoft.rp.shared;

import java.io.Serializable;

public class ProductTypeData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3168261602504528773L;
	private String pProductType;
	private String pProductCode;
	private String pProcessCode1;
	private String pProcessCode2;
	private String pProcessCode3;
	private String pProcessCode4;
	private String pProcessCode5;
	private String pProcessCode6;
	private String pProcessCode7;
	private String pProcessCode8;
	private String pProcessCode9;
	private String pProcessCode10;
	
	public ProductTypeData(){
		clearProperties();
	}
	
	private void clearProperties(){
		setProductType("");
		setProductCode("");
		setProcessCode1("");
		setProcessCode2("");
		setProcessCode3("");
		setProcessCode4("");
		setProcessCode5("");
		setProcessCode6("");
		setProcessCode7("");
		setProcessCode8("");
		setProcessCode9("");
		setProcessCode10("");
	}
	
	public String getProductType() {
		return pProductType;
	}
	public void setProductType(String pProductType) {
		this.pProductType = pProductType;
	}
	public String getProductCode() {
		return pProductCode;
	}
	public void setProductCode(String pProductCode) {
		this.pProductCode = pProductCode;
	}
	public String getProcessCode1() {
		return pProcessCode1;
	}
	public void setProcessCode1(String pProcessCode1) {
		this.pProcessCode1 = pProcessCode1;
	}
	public String getProcessCode2() {
		return pProcessCode2;
	}
	public void setProcessCode2(String pProcessCode2) {
		this.pProcessCode2 = pProcessCode2;
	}
	public String getProcessCode3() {
		return pProcessCode3;
	}
	public void setProcessCode3(String pProcessCode3) {
		this.pProcessCode3 = pProcessCode3;
	}
	public String getProcessCode4() {
		return pProcessCode4;
	}
	public void setProcessCode4(String pProcessCode4) {
		this.pProcessCode4 = pProcessCode4;
	}
	public String getProcessCode5() {
		return pProcessCode5;
	}
	public void setProcessCode5(String pProcessCode5) {
		this.pProcessCode5 = pProcessCode5;
	}
	public String getProcessCode6() {
		return pProcessCode6;
	}
	public void setProcessCode6(String pProcessCode6) {
		this.pProcessCode6 = pProcessCode6;
	}
	public String getProcessCode7() {
		return pProcessCode7;
	}
	public void setProcessCode7(String pProcessCode7) {
		this.pProcessCode7 = pProcessCode7;
	}
	public String getProcessCode8() {
		return pProcessCode8;
	}
	public void setProcessCode8(String pProcessCode8) {
		this.pProcessCode8 = pProcessCode8;
	}
	public String getProcessCode9() {
		return pProcessCode9;
	}
	public void setProcessCode9(String pProcessCode9) {
		this.pProcessCode9 = pProcessCode9;
	}
	public String getProcessCode10() {
		return pProcessCode10;
	}
	public void setProcessCode10(String pProcessCode10) {
		this.pProcessCode10 = pProcessCode10;
	}

}
