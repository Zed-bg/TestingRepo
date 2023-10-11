package com.nirvasoft.rp.shared.icbs;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetOutstandingBillResData {
	private String meterID;
	private String code;
	private String desc;
	private GetOutstandingBillData[] outData = null;

	public GetOutstandingBillResData() {
		clearProperties();
	}

	private void clearProperties() {
		this.meterID = "";
		this.code = "";
		this.desc = "";
		this.outData = null;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public String getMeterID() {
		return meterID;
	}

	public GetOutstandingBillData[] getOutData() {
		return outData;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}

	public void setOutData(GetOutstandingBillData[] outData) {
		this.outData = outData;
	}

	@Override
	public String toString() {
		return "{meterID=" + meterID + ", code=" + code + ", desc=" + desc + ", outData=" + Arrays.toString(outData)
				+ "}";
	}

}
