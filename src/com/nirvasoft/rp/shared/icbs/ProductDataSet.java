package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.ProductData;

public class ProductDataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ProductData> pDataset;
	private int pTotalCount; // total number of records. useful for pagination
	private int pCurrentPage;
	private int pPageSize;
	private String pFields;
	private String pOrderBy;

	public int getCurrentPage() {
		return pCurrentPage;
	}

	public ArrayList<ProductData> getDataset() {
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

	public void setDataset(ArrayList<ProductData> p) {
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
