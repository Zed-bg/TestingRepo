package com.nirvasoft.rp.shared;

import java.util.ArrayList;
import com.nirvasoft.rp.shared.icbs.AccountData;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoanResponse {
	private String retcode;
	private String retmessage;
	private LoanAccData datalist;
	private LoanScheduleData scheduledatalist;
	
	public LoanResponse() {
		clearProperty();
	}

	private void clearProperty() {
		retcode = "";
		retmessage = "";
		datalist = null;
		scheduledatalist = null;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public LoanAccData getDatalist() {
		return datalist;
	}

	public void setDatalist(LoanAccData datalist) {
		this.datalist = datalist;
	}

	public LoanScheduleData getScheduledatalist() {
		return scheduledatalist;
	}

	public void setScheduledatalist(LoanScheduleData scheduledatalist) {
		this.scheduledatalist = scheduledatalist;
	}
	
}
