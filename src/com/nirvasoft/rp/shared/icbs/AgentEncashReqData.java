package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AgentEncashReqData {
	private String userID;
	private String sessionID;
	private String refno;
	private String field1;
	private String field2;
	private String nrc;
	private String password;
	private String tranid;
	private String iv;
	private String dm;
	private String salt;

	public AgentEncashReqData() {
		clearProperties();
	}

	private void clearProperties() {
		this.userID = "";
		this.sessionID = "";
		this.refno = "";
		this.field1 = "";
		this.field2 = "";
		this.nrc = "";
		this.password = "";
		this.tranid = "";
		this.iv = "";
		this.dm = "";
		this.salt = "";
	}

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}

	public String getRefno() {
		return refno;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getUserID() {
		return userID;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}
	
	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	

	public String getTranid() {
		return tranid;
	}

	public void setTranid(String tranid) {
		this.tranid = tranid;
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

	@Override
	public String toString() {
		return "AgentEncashReqData [userID=" + userID + ", sessionID=" + sessionID + ", refno=" + refno + ", field1="
				+ field1 + ", field2=" + field2 + ", nrc=" + nrc + ", password=" + password + ", tranid=" + tranid
				+ ", iv=" + iv + ", dm=" + dm + ", salt=" + salt + "]";
	}

	
}
