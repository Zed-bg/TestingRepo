package com.nirvasoft.rp.shared;

import java.util.ArrayList;
import com.nirvasoft.rp.shared.icbs.TransferData;

public class TransferDataResult {
	private boolean status ;
	private String description ;
	private String code ;
	private ArrayList<Integer> autoLinkTransRef =new ArrayList<Integer>() ;
	private ArrayList<TransferData> transferlist = new ArrayList<TransferData>();

	public TransferDataResult() {
		ClearProperty();
	}

	private void ClearProperty() {
		status = false;
		description = "";
		code = "";
		autoLinkTransRef =new ArrayList<Integer>() ;
		transferlist = new ArrayList<TransferData>();
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<TransferData> getTransferlist() {
		return transferlist;
	}

	public void setTransferlist(ArrayList<TransferData> transferlist) {
		this.transferlist = transferlist;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<Integer> getAutoLinkTransRef() {
		return autoLinkTransRef;
	}

	public void setAutoLinkTransRef(ArrayList<Integer> autoLinkTransRef) {
		this.autoLinkTransRef = autoLinkTransRef;
	}

	
}
