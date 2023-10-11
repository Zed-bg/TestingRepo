package com.nirvasoft.rp.shared;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceChargesData implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double pRate;	
	private String pDateFrom;	
	private String pDateTo;	
	private boolean pValid;	
	private int pODType;
	private double pExtensionFee;
	
	
	public double getRate() {
		return pRate;
	}
	public void setRate(double pRate) {
		this.pRate = pRate;
	}
	public String getDateFrom() {
		return pDateFrom;
	}
	public void setDateFrom(String pDateFrom) {
		this.pDateFrom = pDateFrom;
	}
	public String getDateTo() {
		return pDateTo;
	}
	public void setDateTo(String pDateTo) {
		this.pDateTo = pDateTo;
	}
	public boolean ispValid() {
		return pValid;
	}
	public void setValid(boolean pValid) {
		this.pValid = pValid;
	}
	
	public int getODType() {
		return pODType;
	}
	public void setODType(int ODType) {
		this.pODType = ODType;
	}
	public double getExtensionFee() {
		return pExtensionFee;
	}
	public void setExtensionFee(double pExtensionFee) {
		this.pExtensionFee = pExtensionFee;
	}
	
	void clearproperty(){
		this.pRate=0;	
		this.pDateFrom="";	
		this.pDateTo="";	
		this.pValid=true;	
		this.pODType=0;
		this.pExtensionFee=0;
	}
	public ServiceChargesData() {
		super();
		clearproperty();
	}
			
}
