package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class CutOffTimeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2052816789245857502L;
	public static String readDate = "";
	public String hour = "";
	public String minutes = "";

	public String getHour() {
		return hour;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

}
