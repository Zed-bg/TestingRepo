package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExcludedAccountArr {
	private String code;
	private String desc;
	ExcludedAccount[] dataList;

	public ExcludedAccountArr() {
		clearProperty();
	}

	private void clearProperty() {
		code = "";
		desc = "";
		dataList = null;
	}

	public String getCode() {
		return code;
	}

	public ExcludedAccount[] getDataList() {
		return dataList;
	}

	public String getDesc() {
		return desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDataList(ExcludedAccount[] dataList) {
		this.dataList = dataList;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
