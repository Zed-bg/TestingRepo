package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class getaccountcustomerinforeq {
	private String token;
	private String search;
	private String searchway;
	private String searchtype;
	private String syskey;
	
	public getaccountcustomerinforeq() {
		clearproperty();
	}

	void clearproperty() {
		token = "";
		search = "";
		searchway = "";
		searchtype = "";
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getSearch() {
		return search;
	}

	public String getSearchway() {
		return searchway;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void setSearchway(String searchway) {
		this.searchway = searchway;
	}

	public String getSearchtype() {
		return searchtype;
	}

	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
