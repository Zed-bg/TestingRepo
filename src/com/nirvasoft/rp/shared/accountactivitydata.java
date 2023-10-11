package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class accountactivitydata {
	private String curcode;
	private String transdate;
	private double transamount;
	private String transref;
	private String transdescription;
	private String transcode;
	private String transtypedescription;
	private String remark;
	private String effectivedate;
	
	public accountactivitydata() {
		clearproperty();
	}

	void clearproperty() {
		transdate = "";
		transamount = 0.00;
		transref = "";
		transdescription = "";
		transcode = "";
		curcode = "";
		transtypedescription = "";
		remark = "";
		effectivedate = "";
	}

	public String getCurcode() {
		return curcode;
	}

	public String getTransdate() {
		return transdate;
	}

	public double getTransamount() {
		return transamount;
	}

	public String getTransref() {
		return transref;
	}

	public String getTransdescription() {
		return transdescription;
	}

	public String getTranscode() {
		return transcode;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}

	public void setTransamount(double transamount) {
		this.transamount = transamount;
	}

	public void setTransref(String transref) {
		this.transref = transref;
	}

	public void setTransdescription(String transdescription) {
		this.transdescription = transdescription;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public String getTranstypedescription() {
		return transtypedescription;
	}

	public void setTranstypedescription(String transtypedescription) {
		this.transtypedescription = transtypedescription;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEffectivedate() {
		return effectivedate;
	}

	public void setEffectivedate(String effectivedate) {
		this.effectivedate = effectivedate;
	}
}
