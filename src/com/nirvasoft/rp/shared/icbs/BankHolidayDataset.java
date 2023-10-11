package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

public class BankHolidayDataset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -977026019138101276L;

	private ArrayList<BankHolidayData> pDataset;
	private int pTotalCount;
	private int pCurrentPage;
	private int pPageSize;
	private String pFields;
	private String pOrderBy;

	public int getCurrentPage() {
		return pCurrentPage;
	}

	public ArrayList<BankHolidayData> getDataset() {
		return pDataset;
	}

	public String getFields() {
		return pFields;
	}

	public String getOrderBy() {
		return pOrderBy;
	}

	public int getPageSize() {
		return pPageSize;
	}

	public int getTotalCount() {
		return pTotalCount;
	}

	public void setCurrentPage(int p) {
		pCurrentPage = p;
	}

	public void setDataset(ArrayList<BankHolidayData> p) {
		pDataset = p;
	}

	public void setFields(String p) {
		pFields = p;
	}

	public void setOrderBy(String p) {
		pOrderBy = p;
	}

	public void setPageSize(int p) {
		pPageSize = p;
	}

	public void setTotalCount(int p) {
		pTotalCount = p;
	}
}
