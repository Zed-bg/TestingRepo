package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class customerinfodata {
	private java.lang.String cifid;		//Customer ID
	private java.lang.String accdob;	//Date of Birth
	private java.lang.String accname;	//Name
	private java.lang.String accnrc;	//NRC
	private java.lang.String email;		//Email
	private java.lang.String phone;	//Phone Number
	private java.lang.String state;	//State
	private java.lang.String city;	//City
	private java.lang.String postalcode;	//Postal Code
	private java.lang.String country;	//Country
	private java.lang.String address;	//Address
	
	public customerinfodata() {
		clearproperty();
	}

	void clearproperty() {
		 cifid = "";
		 accdob = "";
		 accname = "";
		 accnrc = "";
		 email = "";
		 phone = "";
		 state = "";
		 city = "";
		 postalcode = "";
		 country = "";
		 address = "";
	}

	public java.lang.String getCifid() {
		return cifid;
	}

	public java.lang.String getAccdob() {
		return accdob;
	}

	public java.lang.String getAccname() {
		return accname;
	}

	public java.lang.String getAccnrc() {
		return accnrc;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public java.lang.String getState() {
		return state;
	}

	public java.lang.String getCity() {
		return city;
	}

	public java.lang.String getPostalcode() {
		return postalcode;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setCifid(java.lang.String cifid) {
		this.cifid = cifid;
	}

	public void setAccdob(java.lang.String accdob) {
		this.accdob = accdob;
	}

	public void setAccname(java.lang.String accname) {
		this.accname = accname;
	}

	public void setAccnrc(java.lang.String accnrc) {
		this.accnrc = accnrc;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public void setPostalcode(java.lang.String postalcode) {
		this.postalcode = postalcode;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}
}

