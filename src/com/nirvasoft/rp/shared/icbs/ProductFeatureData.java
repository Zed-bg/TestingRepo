package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class ProductFeatureData implements Serializable {
	private static final long serialVersionUID = 1L;
	private long pSysKey;
	private String pProductId;
	private long pFeatureId;
	private long pFeatureValue;
	private String pFeatureDescription;
	private String pExpression;
	private String pGLCode;

	public ProductFeatureData() {
		clearProperty();
	}

	private void clearProperty() {
		pSysKey = 0;
		pProductId = "";
		pFeatureId = 0;
		pFeatureValue = 0;
		pFeatureDescription = "";
		pExpression = "";
		pGLCode = "";
	}

	public String getExpression() {
		return pExpression;
	}

	public String getFeatureDescription() {
		return pFeatureDescription;
	}

	public long getFeatureId() {
		return pFeatureId;
	}

	public long getFeatureValue() {
		return pFeatureValue;
	}

	public String getGLCode() {
		return pGLCode;
	}

	public String getProductId() {
		return pProductId;
	}

	public long getSysKey() {
		return pSysKey;
	}

	public void setExpression(String pExpression) {
		this.pExpression = pExpression;
	}

	public void setFeatureDescription(String pFeatureDescription) {
		this.pFeatureDescription = pFeatureDescription;
	}

	public void setFeatureId(long pFeatureId) {
		this.pFeatureId = pFeatureId;
	}

	public void setFeatureValue(long pFeatureValue) {
		this.pFeatureValue = pFeatureValue;
	}

	public void setGLCode(String p) {
		pGLCode = p;
	}

	public void setProductId(String pProductId) {
		this.pProductId = pProductId;
	}

	public void setSysKey(long pSysKey) {
		this.pSysKey = pSysKey;
	}

}
