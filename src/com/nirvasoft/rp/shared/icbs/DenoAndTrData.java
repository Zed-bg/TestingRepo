package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class DenoAndTrData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4235862923369471102L;
	private String pCounterID;
    private String pEntryDate;
    private String pSerialNo;
    private String pT1;
    private String pT2;
    private int pTransNo;
    private int pTransRef;
    
    public DenoAndTrData(){
    	clearProperty();
    }
    
	public void setCounterID(String p) {pCounterID = p;}	
	public String getCounterID() {return pCounterID;}
	
	public void setEntryDate(String p) {pEntryDate = p;}	
	public String getEntryDate() {return pEntryDate;}
	
	public void setSerialNo(String p) {pSerialNo = p;}	
	public String getSerialNo() {return pSerialNo;}
	
	public void setT1(String p) {pT1 = p;}	
	public String getT1() {return pT1;}
	
	public void setT2(String p) {pT2 = p;}	
	public String getT2() {return pT2;}
	
	public void setTransNo(int p) {pTransNo = p;}	
	public int getTransNo() {return pTransNo;}
	
	public void setTransRef(int p) {pTransRef = p;}	
	public int getTransRef() {return pTransRef;}
	
	private void clearProperty(){
		 pCounterID = "";
	     pEntryDate = "";
	     pSerialNo = "";
	     pT1 = "";
	     pT2 = "";
	     pTransNo = 0;
	     pTransRef = 0;
	}
	
	
	
}
