package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenSetupData {
	private long syskey;
	private String claim1;
	private String userId;
	private String password;
	private String claim2;
	private String issureclaim;	
	private String subjectclaim;
	private String audienceclaim;
	private String notbeforeclaim;
	private String issuedatclaim;
	private String jwtidclaim;
	private String secretkey;
	private int expiretime;
	private String publicclaimname;
	private String privateclaimname;
	private String type;
	private String contenttype;
	private String createddate;
	private int forcedchgpwdflag;
	private String modifiedDate;
	private int status;
	private String t1;
	private String t2;
	private String t3;
	private String t4;
	private int n1;
	private int n2;
	private double n3;
	private double n4;
	private boolean state;
	public TokenSetupData()
	{
		syskey =0;
		claim1 = "";
		userId = "";
		password = "";
		claim2 = "";
		issureclaim = "";	
		subjectclaim = "";
		audienceclaim = "";
		notbeforeclaim = "";
		issuedatclaim = "";
		jwtidclaim = "";
		secretkey = "";
		expiretime =0;
		publicclaimname = "";
		privateclaimname = "";
		type = "";
		contenttype = "";
		forcedchgpwdflag = 0;
		
		createddate="";
		modifiedDate="";
		status=0;
		t1="";
		t2="";
		t3="";
		t4="";
		n1=0;
		n2=0;
		n3=0;
		n4=0;
		state = false;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getT1() {
		return t1;
	}
	public void setT1(String t1) {
		this.t1 = t1;
	}
	public String getT2() {
		return t2;
	}
	public void setT2(String t2) {
		this.t2 = t2;
	}
	public String getT3() {
		return t3;
	}
	public void setT3(String t3) {
		this.t3 = t3;
	}
	public String getT4() {
		return t4;
	}
	public void setT4(String t4) {
		this.t4 = t4;
	}
	public int getN1() {
		return n1;
	}
	public void setN1(int n1) {
		this.n1 = n1;
	}
	public int getN2() {
		return n2;
	}
	public void setN2(int n2) {
		this.n2 = n2;
	}
	public double getN3() {
		return n3;
	}
	public void setN3(double n3) {
		this.n3 = n3;
	}
	public double getN4() {
		return n4;
	}
	public void setN4(double n4) {
		this.n4 = n4;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userid) {
		this.userId = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getForcedchgpwdflag() {
		return forcedchgpwdflag;
	}
	public void setForcedchgpwdflag(int forcedchgpwdflag) {
		this.forcedchgpwdflag = forcedchgpwdflag;
	}
	public long getSyskey() {
		return syskey;
	}
	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}
	public String getClaim1() {
		return claim1;
	}
	public void setClaim1(String claim1) {
		this.claim1 = claim1;
	}
	public String getClaim2() {
		return claim2;
	}
	public void setClaim2(String claim2) {
		this.claim2 = claim2;
	}
	public String getIssureclaim() {
		return issureclaim;
	}
	public void setIssureclaim(String issureclaim) {
		this.issureclaim = issureclaim;
	}
	public String getSubjectclaim() {
		return subjectclaim;
	}
	public void setSubjectclaim(String subjectclaim) {
		this.subjectclaim = subjectclaim;
	}
	public String getAudienceclaim() {
		return audienceclaim;
	}
	public void setAudienceclaim(String audienceclaim) {
		this.audienceclaim = audienceclaim;
	}
	public String getNotbeforeclaim() {
		return notbeforeclaim;
	}
	public void setNotbeforeclaim(String notbeforeclaim) {
		this.notbeforeclaim = notbeforeclaim;
	}
	public String getIssuedatclaim() {
		return issuedatclaim;
	}
	public void setIssuedatclaim(String issuedatclaim) {
		this.issuedatclaim = issuedatclaim;
	}
	public String getJwtidclaim() {
		return jwtidclaim;
	}
	public void setJwtidclaim(String jwtidclaim) {
		this.jwtidclaim = jwtidclaim;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public int getExpiretime() {
		return expiretime;
	}
	public void setExpiretime(int expiretime) {
		this.expiretime = expiretime;
	}
	public String getPublicclaimname() {
		return publicclaimname;
	}
	public void setPublicclaimname(String publicclaimname) {
		this.publicclaimname = publicclaimname;
	}
	public String getPrivateclaimname() {
		return privateclaimname;
	}
	public void setPrivateclaimname(String privateclaimname) {
		this.privateclaimname = privateclaimname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContenttype() {
		return contenttype;
	}
	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	public String getCreateddate() {
		return createddate;
	}
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	

}