package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class statementdataresponse {
	private String retcode;
	private String retmessage;
	private statementdata[] datalist;
	private String fromdate;
	private String todate;
	private int currentpage;
	private int durationtype;
	private int pagecount;
	private int pagesize;
	private int totalcount;
	public statementdataresponse() {
		clearproperty();
	}

	void clearproperty() {
		retcode = "";
		retmessage = "";
		datalist = null;
		fromdate = "";
		todate = "";
		currentpage = 0;
		durationtype = 0;
		pagesize = 0;
		totalcount = 0;
		pagecount = 0;
	}

	public statementdata[] getDatalist() {
		return datalist;
	}

	public void setDatalist(statementdata[] datalist) {
		this.datalist = datalist;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public String getFromdate() {
		return fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public int getDurationtype() {
		return durationtype;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public void setDurationtype(int durationtype) {
		this.durationtype = durationtype;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	
	
}
