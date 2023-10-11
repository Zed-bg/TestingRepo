package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class accdepositreq {

	private String userid;
	private String token;
	private String accnumber;
	private String merchantid;
	private String amount;
	private String bankcharges;
	private String remark;
	private String refno;
	private String skey;
	private String name;
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
	private String glcheck;
	private String branchcode;
	private String currencycode;
	
	public accdepositreq() {
		clearproperty();
	}

	void clearproperty() {
		userid = "";
		token = "";
		merchantid = "";
		amount = "0.00";
		bankcharges = "0.00";
		remark = "";
		refno = "";
		skey = "";
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
		glcheck = "0";
		branchcode="";
		currencycode="";
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

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getAccnumber() {
		return accnumber;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGlcheck() {
		return glcheck;
	}

	public void setGlcheck(String glcheck) {
		this.glcheck = glcheck;
	}
	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}	

}
