package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransactionViewArr {

	private String messageCode;
	private String messageDesc;
	TransactionViewData[] data;

	public TransactionViewArr() {
		clearProperty();
	}

	private void clearProperty() {
		messageCode = "";
		messageDesc = "";
	}

	public TransactionViewData[] getData() {
		return data;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public String getMessageDesc() {
		return messageDesc;
	}

	public void setData(TransactionViewData[] data) {
		this.data = data;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}
}
