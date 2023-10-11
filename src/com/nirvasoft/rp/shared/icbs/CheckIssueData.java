package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.CheckRequestData;
import com.nirvasoft.rp.shared.CheckSheetData;

public class CheckIssueData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pAccountNumber;
	private int pChkNoFrom;
	private String pDateIssued;
	private String pDateApproved;
	private int pStatus;
	private String pSheets;
	private String pChkNoChar;
	private String pStartingCheckNo; // like A1, A001
	private String pCurrencyCode;
	// wmmh
	private String pTitle;
	private String pName;
	private CheckRequestData pChequeRequestData = new CheckRequestData();;

	private ArrayList<CheckSheetData> pCheckSheetDataList = new ArrayList<CheckSheetData>();

	public String getAccountNumber() {
		return pAccountNumber;
	}

	public ArrayList<CheckSheetData> getCheckSheetDataList() {
		return pCheckSheetDataList;
	}

	public CheckRequestData getChequeRequestData() {
		return pChequeRequestData;
	}

	public String getChkNoChar() {
		return pChkNoChar;
	}

	public int getChkNoFrom() {
		return pChkNoFrom;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public String getDateApproved() {
		return pDateApproved;
	}

	public String getDateIssued() {
		return pDateIssued;
	}

	public String getName() {
		return pName;
	}

	public String getSheets() {
		return pSheets;
	}

	public String getStartingCheckNo() {
		return pStartingCheckNo;
	}

	public int getStatus() {
		return pStatus;
	}

	public String getTitle() {
		return pTitle;
	}

	public void setAccountNumber(String p) {
		pAccountNumber = p;
	}

	public void setCheckSheetDataList(ArrayList<CheckSheetData> pCheckSheetDataList) {
		this.pCheckSheetDataList = pCheckSheetDataList;
	}

	public void setChequeRequestData(CheckRequestData pChequeRequestData) {
		this.pChequeRequestData = pChequeRequestData;
	}

	public void setChkNoChar(String p) {
		pChkNoChar = p;
	}

	public void setChkNoFrom(int p) {
		pChkNoFrom = p;
	}

	public void setCurrencyCode(String p) {
		pCurrencyCode = p;
	}

	public void setDateApproved(String p) {
		pDateApproved = p;
	}

	public void setDateIssued(String p) {
		pDateIssued = p;
	}

	public void setName(String p) {
		pName = p;
	}

	public void setSheets(String p) {
		pSheets = p;
	}

	public void setStartingCheckNo(String p) {
		pStartingCheckNo = p;
	}

	public void setStatus(int p) {
		pStatus = p;
	}

	public void setTitle(String p) {
		pTitle = p;
	}

}
