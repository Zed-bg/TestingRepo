package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class tokenreqdata {
	private String appid;
	private String appkey;
	private String userid;
	private String cifid;//(customerID)
	
	public tokenreqdata() {
		clearproperty();
	}

	void clearproperty() {
		appid = "";
		appkey = "";
		userid = "";
		cifid = "";
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCifid() {
		return cifid;
	}

	public void setCifid(String cifid) {
		this.cifid = cifid;
	}

	
}
