package com.nirvasoft.rp.shared;

import java.io.Serializable;

public class MultiTransferCriteriaData implements Serializable{
	private static final long serialVersionUID = -3549946625943141737L;
	
	public int TransferNo = 0;	
    public int DrCr = 255;
    public int SerialNo = 0;
    public String TransDate = "";
    public String AccType = "";
    public String AccNumber = "";
    public String CommAccNumber = "";
    public String ChequeNo = "";
    public double Amount = 0;
    public double Commission = 0;
    public String Name = "";
    public String NRC = "";
    public double Balance = 0;
    public int Status = 255;
    public int TransRef =  255;
    public String SubRef = "";
    public int SystemCode = 255;
    public String Narration = "";
    public String t1 = "";
    public String t2 = "";
    public String t3 = "";
    public double n1 = 0;
    public double n2 = 0;
    public double n3 = 0;
    public String FromDate = "";
    public String ToDate = "";
    public String BranchCode = "";
    
    public Enum.DateFilterOperator FromDateOperator = Enum.DateFilterOperator.GreaterThanEqual;
    public Enum.DateFilterOperator ToDateOperator = Enum.DateFilterOperator.LessThanEqual;
}
