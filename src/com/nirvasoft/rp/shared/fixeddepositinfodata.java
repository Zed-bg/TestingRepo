package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class fixeddepositinfodata {
	private String productcode;
	private String description;
	private String tenure;
	private String yearlyrate;
	private String tenuretype;
	private String datatenure;
	
	public fixeddepositinfodata() {
		clearProperty();
	}

	private void clearProperty() {
		productcode = "";
		description = "";
		tenure = "";
		yearlyrate = "";
		tenuretype="";
		datatenure="";
	}

	public String getDatatenure() {
		return datatenure;
	}

	public void setDatatenure(String datatenure) {
		this.datatenure = datatenure;
	}

	public String getTenuretype() {
		return tenuretype;
	}

	public void setTenuretype(String tenuretype) {
		this.tenuretype = tenuretype;
	}

	public String getProductcode() {
		return productcode;
	}

	public String getDescription() {
		return description;
	}

	public String getTenure() {
		return tenure;
	}

	public String getYearlyrate() {
		return yearlyrate;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public void setYearlyrate(String yearlyrate) {
		this.yearlyrate = yearlyrate;
	}
}
