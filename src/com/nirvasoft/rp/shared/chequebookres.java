package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequebookres {
	private String retcode;
	private String retmessage;
	private String startcheque;
	private String endcheque;
	
	public chequebookres() {
		clearproperty();
	}

	void clearproperty() {
		retcode = "";
		retmessage = "";
		startcheque = "";
		endcheque = "";
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getStartcheque() {
		return startcheque;
	}

	public String getEndcheque() {
		return endcheque;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setStartcheque(String startcheque) {
		this.startcheque = startcheque;
	}

	public void setEndcheque(String endcheque) {
		this.endcheque = endcheque;
	}

}
