package com.nirvasoft.rp.shared;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class accountactivityresponse {

	private String retcode;
	private String retmessage;
	private String durationtype;
	private String fromdate;
	private String todate;
	private String accnumber;
	private int totalcount;
	private int currentpage;
	private int pagesize;
	private int pagecount;
	private accountactivitydata[] datalist = null;

	public accountactivityresponse() {
		clearproperty();
	}

	void clearproperty() {
		retcode = "";
		retmessage = "";
		durationtype = "";
		fromdate = "";
		todate = "";
		accnumber = "";
		totalcount = 0;
		currentpage = 0;
		pagesize = 0;
		pagecount = 0;
		datalist = null;
	}

	public String getDurationtype() {
		return durationtype;
	}

	public void setDurationtype(String durationtype) {
		this.durationtype = durationtype;
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

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public accountactivitydata[] getDatalist() {
		return datalist;
	}

	public void setDatalist(accountactivitydata[] datalist) {
		this.datalist = datalist;
	}


}
