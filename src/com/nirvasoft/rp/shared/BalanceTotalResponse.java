package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BalanceTotalResponse {
    
	private String availablebalance;
    private String productname;
    private String producttype;
	public String getAvailablebalance() {
		return availablebalance;
	}
	public void setAvailablebalance(String availablebalance) {
		this.availablebalance = availablebalance;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getProducttype() {
		return producttype;
	}
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
    
	public BalanceTotalResponse() {
		this.availablebalance ="";
		this.productname = "";
		this.producttype = "";
	}
}
