package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.POCommissionRateData;
import com.nirvasoft.rp.util.SharedUtil;

public class POCommissionRateDAO {
	
	private String mTableName = "";
	
	private POCommissionRateData mPOComRateData;
	
	private static String ServiceType = "T1";
	  
	  private static String EntryDate = "T3";
	  
	  private static String Remark = "T2";
	  
	  private static String CurrencyCode = "T4";
	  
	  private static String BranchCode = "T5";
	  
	  private static String TellerID = "T6";
	  
	  private static String SupervisorID = "T7";
	  
	  private static String WorkStation = "T8";
	  
	  private static String ComType = "T9";
	  
	  private static String POType = "N1";
	  
	  private static String Com = "N2";
	  
	  private static String ComMin = "N3";
	  
	  private static String ComMax = "N4";
	  
	  private static String productType = "T10";
	  
	  private static String FeesType = "T11";
	  
	  private static String Fees = "N6";
	  
	  private static String DecimalType = "N7";
	  
	  public POCommissionRateDAO() {
		    this.mTableName = "T00031";
		    this.mPOComRateData = new POCommissionRateData();
		  }
	  public POCommissionRateData getPOComRateData() {
		    return this.mPOComRateData;
		  }
		  
		  public void setPOComRateData(POCommissionRateData pPOComRateData) {
		    this.mPOComRateData = pPOComRateData;
		  }
	public POCommissionRateData getPOCommissionRateForALink(String pServiceType, String pCurency, int pPOType, String pProductType, Connection pConn) throws SQLException {
	    POCommissionRateData l_data = new POCommissionRateData();
	    String l_query = "SELECT " + ServiceType + "," + 
	      EntryDate + "," + 
	      Remark + "," + 
	      CurrencyCode + "," + 
	      BranchCode + "," + 
	      TellerID + "," + 
	      SupervisorID + "," + 
	      WorkStation + "," + 
	      ComType + "," + 
	      POType + "," + 
	      Com + "," + 
	      ComMin + "," + 
	      ComMax + "," + 
	      productType + "," + 
	      FeesType + "," + 
	      Fees + ", " + 
	      DecimalType + 
	      " FROM " + this.mTableName + " WHERE 1=1 AND " + ServiceType + "= ? AND " + 
	      CurrencyCode + " = ? ";
	    if (pServiceType.equalsIgnoreCase("01") || pServiceType.equalsIgnoreCase("04"))
	      l_query = String.valueOf(l_query) + " AND " + POType + "= " + pPOType; 
	    if (pServiceType.equalsIgnoreCase("03"))
	      l_query = String.valueOf(l_query) + " AND " + productType + "= " + pProductType; 
	    if (pServiceType.equalsIgnoreCase("10"))
	      l_query = String.valueOf(l_query) + " AND " + POType + "= " + pPOType + 
	        " AND " + productType + "= " + pProductType; 
	    PreparedStatement l_pstmt = pConn.prepareStatement(l_query);
	    l_pstmt.setString(1, pServiceType);
	    l_pstmt.setString(2, pCurency);
	    ResultSet l_RS = l_pstmt.executeQuery();
	    while (l_RS.next())
	      readRecords(l_RS, l_data); 
	    l_pstmt.close();
	    return l_data;
	  }
	
	private void readRecords(ResultSet pRS, POCommissionRateData pdata) throws SQLException {
	    pdata.setServiceType(pRS.getString(ServiceType));
	    pdata.setEntryDate(SharedUtil.formatDBDate2MIT(pRS.getString(EntryDate)));
	    pdata.setRemark(pRS.getString(Remark));
	    pdata.setCurrencyCode(pRS.getString(CurrencyCode));
	    pdata.setBranchCode(pRS.getString(BranchCode));
	    pdata.setTellerID(pRS.getString(TellerID));
	    pdata.setSupervisorID(pRS.getString(SupervisorID));
	    pdata.setWorkStation(pRS.getString(WorkStation));
	    pdata.setComType(pRS.getString(ComType));
	    pdata.setPOType(pRS.getInt(POType));
	    pdata.setCom(pRS.getDouble(Com));
	    pdata.setComMin(pRS.getDouble(ComMin));
	    pdata.setComMax(pRS.getDouble(ComMax));
	    pdata.setProductType(pRS.getString(productType));
	    pdata.setpFeesType(pRS.getString(FeesType));
	    pdata.setpFees(pRS.getDouble(Fees));
	    pdata.setDecimalType(pRS.getInt("N7"));
	  }
	
	public POCommissionRateData getPOCommissionRate(String pServiceType, String pCurency, String pBC, int pPOType, String pProductType, Connection pConn) throws SQLException {
	    POCommissionRateData l_data = new POCommissionRateData();
	    String l_query = "SELECT " + ServiceType + "," + 
	      EntryDate + "," + 
	      Remark + "," + 
	      CurrencyCode + "," + 
	      BranchCode + "," + 
	      TellerID + "," + 
	      SupervisorID + "," + 
	      WorkStation + "," + 
	      ComType + "," + 
	      POType + "," + 
	      Com + "," + 
	      ComMin + "," + 
	      ComMax + "," + 
	      productType + "," + 
	      FeesType + "," + 
	      Fees + ", " + 
	      DecimalType + 
	      " FROM " + this.mTableName + " WHERE 1=1 AND " + ServiceType + "= ? AND " + 
	      CurrencyCode + " = ? AND " + 
	      BranchCode + " = ?";
	    if (pServiceType.equalsIgnoreCase("01") || pServiceType.equalsIgnoreCase("04") || pServiceType.equalsIgnoreCase("05") || pServiceType.equalsIgnoreCase("08"))
	      l_query = String.valueOf(l_query) + " AND " + POType + "= " + pPOType; 
	    if (pServiceType.equalsIgnoreCase("10"))
	      l_query = String.valueOf(l_query) + " AND " + POType + "= " + pPOType + 
	        " AND " + productType + "= '" + pProductType + "'"; 
	    if (pServiceType.equalsIgnoreCase("03"))
	      l_query = String.valueOf(l_query) + " AND " + productType + "= " + pProductType; 
	    PreparedStatement l_pstmt = pConn.prepareStatement(l_query);
	    l_pstmt.setString(1, pServiceType);
	    l_pstmt.setString(2, pCurency);
	    l_pstmt.setString(3, pBC);
	    ResultSet l_RS = l_pstmt.executeQuery();
	    while (l_RS.next())
	      readRecords(l_RS, l_data); 
	    l_pstmt.close();
	    return l_data;
	  }
	
}
