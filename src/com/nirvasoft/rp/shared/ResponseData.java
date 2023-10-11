package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseData {

	private String code;
	private String desc;
	private int count;
	private String tophone;
	
	public ResponseData() {
		clearProperty();
	}

	private void clearProperty() {
		code = "";
		desc = "";
		count = 0;
		tophone="";
	}

	public String getTophone() {
		return tophone;
	}

	public void setTophone(String tophone) {
		this.tophone = tophone;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "{code=" + code + ", desc=" + desc + ", tophone=" + tophone + "}";
	}

}
