package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class ReferenceAccountsData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pFunctionID = 0;
	private String pDescription = "";
	private String pGLCode = "";
	private String pGLAccNumber = "";
	private int pFunctionOldID = 0;

	public ReferenceAccountsData() {
		clearProperty();
	}

	public ReferenceAccountsData(int FunctionId, String Description, String GLcode) {
		this.pFunctionID = FunctionId;
		this.pDescription = Description;
		this.pGLCode = GLcode;
	}

	private void clearProperty() {
		pFunctionID = 0;
		pDescription = "";
		pGLCode = "";
		pGLAccNumber = "";
	}

	public String getDescription() {
		return pDescription;
	}

	public int getFunctionID() {
		return pFunctionID;
	}

	public int getFunctionOldID() {
		return pFunctionOldID;
	}

	public String getGLAccNumber() {
		return pGLAccNumber;
	}

	public String getGLCode() {
		return pGLCode;
	}

	public void setDescription(String p) {
		pDescription = p;
	}

	public void setFunctionID(int p) {
		pFunctionID = p;
	}

	public void setFunctionOldID(int p) {
		pFunctionOldID = p;
	}

	public void setGLAccNumber(String p) {
		pGLAccNumber = p;
	}

	public void setGLCode(String p) {
		pGLCode = p;
	}

}
