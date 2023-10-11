package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class acctransferreq {

	private String userid;
	private String token;
	private String fromaccnumber;
	private String toaccnumber;
	private String merchantid;
	private String amount;
	private String bankcharges;
	private String remark;
	private String refno;
	private String transfertype;
	private String field2;
	private String skey;
	private String fromname;
	private String toname;
	private String receiverid;
	private String deviceid;
	private String stoken;
	private String iv;
	private String dm;
	private String salt;
	private String tobankcode;
	private String tobranchcode;
	private String drcommamt;
	private String crcommamt;
	private String inclusive;
	private String syskey;
	private String transdescription;
	private String refcode;
	
	public acctransferreq() {
		clearproperty();
	}

	void clearproperty() {
		userid = "";
		token = "";
		fromaccnumber = "";
		toaccnumber = "";
		merchantid = "";
		amount = "0.00";
		bankcharges = "0.00";
		remark = "";
		refno = "";
		transfertype = "";
		field2 = "";
		skey = "";
		fromname = "";
		toname = "";
		receiverid = "";
		deviceid = "";
		stoken="";
		iv="";
		dm="";
		salt="";
		tobankcode="";
		tobranchcode="";
		drcommamt = "0.00";
		crcommamt = "0.00";
		inclusive = "";
		syskey = "";
		transdescription = "";
		refcode = "";
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBankcharges() {
		return bankcharges;
	}

	public void setBankcharges(String bankcharges) {
		this.bankcharges = bankcharges;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getFromname() {
		return fromname;
	}

	public void setFromname(String fromname) {
		this.fromname = fromname;
	}

	public String getToname() {
		return toname;
	}

	public void setToname(String toname) {
		this.toname = toname;
	}

	public String getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(String receiverid) {
		this.receiverid = receiverid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getStoken() {
		return stoken;
	}

	public void setStoken(String stoken) {
		this.stoken = stoken;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getFromaccnumber() {
		return fromaccnumber;
	}

	public String getToaccnumber() {
		return toaccnumber;
	}

	public String getRemark() {
		return remark;
	}

	public String getTransfertype() {
		return transfertype;
	}

	public void setFromaccnumber(String fromaccnumber) {
		this.fromaccnumber = fromaccnumber;
	}

	public void setToaccnumber(String toaccnumber) {
		this.toaccnumber = toaccnumber;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setTransfertype(String transfertype) {
		this.transfertype = transfertype;
	}

	public String getTobankcode() {
		return tobankcode;
	}

	public String getTobranchcode() {
		return tobranchcode;
	}

	public void setTobankcode(String toBankCode) {
		this.tobankcode = toBankCode;
	}

	public void setTobranchcode(String toBranchCode) {
		this.tobranchcode = toBranchCode;
	}

	

	public String getInclusive() {
		return inclusive;
	}

	public void setInclusive(String inclusive) {
		this.inclusive = inclusive;
	}

	public String getDrcommamt() {
		return drcommamt;
	}

	public String getCrcommamt() {
		return crcommamt;
	}

	public void setDrcommamt(String drcommamt) {
		this.drcommamt = drcommamt;
	}

	public void setCrcommamt(String crcommamt) {
		this.crcommamt = crcommamt;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	public String getTransdescription() {
		return transdescription;
	}

	public void setTransdescription(String transdescription) {
		this.transdescription = transdescription;
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	
}
