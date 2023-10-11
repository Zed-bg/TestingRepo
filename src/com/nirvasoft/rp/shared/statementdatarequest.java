package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class statementdatarequest {
	private String userid;
	private String token;
	private String accnumber;
	private String fromdate;
	private String todate;
	private int currentpage;
	private int durationtype;
	private int pagecount;
	private int pagesize;
	private int totalcount;
	private String syskey;
	
	public statementdatarequest() {
		clearproperty();
	}

	void clearproperty() {
		userid = "";
		token = "";
		accnumber = "";
		fromdate = "";
		todate = "";
		currentpage = 0;
		durationtype = 0;
		pagecount = 0;
		pagesize = 0;
		totalcount = 0;
		syskey = "";
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

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public int getDurationtype() {
		return durationtype;
	}

	public int getPagecount() {
		return pagecount;
	}

	public int getPagesize() {
		return pagesize;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public void setDurationtype(int durationtype) {
		this.durationtype = durationtype;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	
}
