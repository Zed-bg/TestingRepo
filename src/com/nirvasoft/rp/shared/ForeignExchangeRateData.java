package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForeignExchangeRateData {

	private String fromcurcode;
	private String tocurcode;
	private String fromcurcodename;
	private String tocurcodename;
	private double buyrate;
	private double sellrate;
	private double midrate;

	public ForeignExchangeRateData() {
		clearProperty();
	}

	void clearProperty() {
		fromcurcode = "";
		tocurcode = "";
		fromcurcodename = "";
		tocurcodename = "";
		buyrate = 0.00;
		sellrate = 0.00;
		midrate = 0.00;
	}

	public String getFromcurcode() {
		return fromcurcode;
	}

	public String getTocurcode() {
		return tocurcode;
	}

	public String getFromcurcodename() {
		return fromcurcodename;
	}

	public double getBuyrate() {
		return buyrate;
	}

	public double getSellrate() {
		return sellrate;
	}

	public double getMidrate() {
		return midrate;
	}

	public void setFromcurcode(String fromcurcode) {
		this.fromcurcode = fromcurcode;
	}

	public void setTocurcode(String tocurcode) {
		this.tocurcode = tocurcode;
	}

	public void setFromcurcodename(String fromcurcodename) {
		this.fromcurcodename = fromcurcodename;
	}

	public void setBuyrate(double buyrate) {
		this.buyrate = buyrate;
	}

	public void setSellrate(double sellrate) {
		this.sellrate = sellrate;
	}

	public void setMidrate(double midrate) {
		this.midrate = midrate;
	}

	public String getTocurcodename() {
		return tocurcodename;
	}

	public void setTocurcodename(String tocurcodename) {
		this.tocurcodename = tocurcodename;
	}

	

}
