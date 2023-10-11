package com.nirvasoft.rp.shared;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenResult {
	private boolean state = false;
	private String msgCode = "";
	private String msgDesc = "";

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	@Override
	public String toString() {
		return "Result [state=" + state + ", msgCode=" + msgCode + ", msgDesc=" + msgDesc
				+ "]";
	}


}
