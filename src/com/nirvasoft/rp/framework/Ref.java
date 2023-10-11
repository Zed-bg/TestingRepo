package com.nirvasoft.rp.framework;

import javax.xml.bind.annotation.XmlRootElement;

import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.util.SharedUtil;

@XmlRootElement
public class Ref {

	public static String getAccountStatusDescription(AccountData object) {
		String ret = "";
		// Account Status
		switch (object.getStatus()) {
		// New (or) Active
		case 0:
			if (SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900"))
				ret = "New";
			else
				ret = "Active";
			break;

		// Suspend
		case 1:
			ret = "Suspend";
			break;

		// Closed Pending
		case 2:
			ret = "Close Pending";
			break;

		// Closed
		case 3:
			ret = "Closed";
			break;

		case 4:
			ret = "Dormant";
			break;
		// Stopped Payment
		case 7:
			ret = "StoppedPayment";
			break;

		default:
			ret = "";
			break;
		}
		return ret;
	}

	private String value;

	private String caption;

	public Ref() {
		clearProperty();
	}

	void clearProperty() {
		value = "";
		caption = "";
	}

	public String getcaption() {
		return caption;
	}

	public String getvalue() {
		return value;
	}

	public void setcaption(String caption) {
		this.caption = caption;
	}

	public void setvalue(String value) {
		this.value = value;
	}

}
