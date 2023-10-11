package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountTransactionResponse {
	private String xref;
	private String code;
	private String desc;
	private String flexcubeid;

	private String param1;// phone no.
	private String param2;// email
	private String charges;
	
	private String field1;
	private boolean isNWD;
	
	private String effectiveDate;
	private String flextext;
	private String  flex_approval_time;

	public AccountTransactionResponse() {
		clearProperty();
	}

	void clearProperty() {

		xref = "";
		code = "";
		desc = "";
		flexcubeid = "";
		param1 = "";
		param2 = "";
		charges = "";
		field1="";
		isNWD=false;
		effectiveDate = "";
		flextext = "";
		flex_approval_time = "";
	}

	public String getCharges() {
		return charges;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public String getFlexcubeid() {
		return flexcubeid;
	}

	public String getParam1() {
		return param1;
	}

	public String getParam2() {
		return param2;
	}

	public String getXref() {
		return xref;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setFlexcubeid(String flexcubeid) {
		this.flexcubeid = flexcubeid;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public void setXref(String xref) {
		this.xref = xref;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public boolean isNWD() {
		return isNWD;
	}

	public void setNWD(boolean isNWD) {
		this.isNWD = isNWD;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getFlextext() {
		return flextext;
	}

	public void setFlextext(String flextext) {
		this.flextext = flextext;
	}

	public String getFlex_approval_time() {
		return flex_approval_time;
	}

	public void setFlex_approval_time(String flex_approval_time) {
		this.flex_approval_time = flex_approval_time;
	}

}
