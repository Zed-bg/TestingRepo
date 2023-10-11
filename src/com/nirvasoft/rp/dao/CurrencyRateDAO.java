package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.nirvasoft.rp.shared.CurrencyRateData;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.shared.SharedLogic;

public class CurrencyRateDAO {
	private String mTableName = "CurrencyRate";
	
	private CurrencyRateData objCurrencyRateData;
	private ArrayList<CurrencyRateData>  objCurrenyReteDataList;
	
	
	public void setObjCurrencyRateData(CurrencyRateData objCurrencyRateData) {
		this.objCurrencyRateData = objCurrencyRateData;
	}
	public CurrencyRateData getObjCurrencyRateData() {
		return objCurrencyRateData;
	}
	public void setObjCurrenyReteDataList(ArrayList<CurrencyRateData> objCurrenyReteDataList) {
		this.objCurrenyReteDataList = objCurrenyReteDataList;
	}
	public ArrayList<CurrencyRateData> getObjCurrenyReteDataList() {
		return objCurrenyReteDataList;
	}
	
	public CurrencyRateDAO()
	{
		objCurrencyRateData = new CurrencyRateData();
		objCurrenyReteDataList = new ArrayList<CurrencyRateData>();
		
		//if(SharedLogic.getSystemSettingT12N1("BNK") == 4){
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN1() == 4) {
			mTableName = "T00030";
		}
	}
	
	public Double getRate(String pColumn,Connection p_conn) throws SQLException {
		double l_Rate=0;
		
		String l_query="SELECT Rate FROM "+mTableName+" WHERE 1=1";
		
		if(!pColumn.equals("") || !pColumn.equals("null")){
			l_query += " And Type="+pColumn;
		}
		if(objCurrencyRateData.getAutokey() != 0)
			l_query += " And autokey = " + objCurrencyRateData.getAutokey();
		if(!objCurrencyRateData.getBranchCode().equals(""))
			l_query += " And branchcode = '" + objCurrencyRateData.getBranchCode() + "'";
		if(!objCurrencyRateData.getCurrencyCode().equals(""))
			l_query += " And currencyCode = '" + objCurrencyRateData.getCurrencyCode() + "'";
		if(!objCurrencyRateData.getUnitType().equals(""))
			l_query += " And unittype = '" + objCurrencyRateData.getUnitType() + "'";
		if(!objCurrencyRateData.getUnitName().equals(""))
			l_query += " And unitname = '" + objCurrencyRateData.getUnitName() + "'";
		if(objCurrencyRateData.getNote() != 0)
			l_query += " And note = "  + objCurrencyRateData.getNote();
		if(objCurrencyRateData.getBuyRate() != 0)
			l_query += " And buyrate = " + objCurrencyRateData.getBuyRate();
		if(!objCurrencyRateData.getBuyRateOperator().equals(""))
			l_query += " And buyrateoperator = '" + objCurrencyRateData.getBuyRateOperator() + "'";
		if(objCurrencyRateData.getSellRate() != 0)
			l_query += " And sellrate = " + objCurrencyRateData.getSellRate();
		if(!objCurrencyRateData.getSellRateOperator().equals(""))
			l_query += " And sellrateoperator = '" + objCurrencyRateData.getSellRateOperator() + "'";
		if(!objCurrencyRateData.getDenoName().equals("")) {
			l_query += " And DenoName = '" + objCurrencyRateData.getDenoName() + "'";
		}
		PreparedStatement l_pstmt=p_conn.prepareStatement(l_query);
		ResultSet l_RS=l_pstmt.executeQuery();
		while(l_RS.next()) {
			l_Rate=l_RS.getDouble(1);
		}		
		return l_Rate;
	}
	
	public HashMap<String,Double> getRateList(Connection p_conn) throws SQLException {
		double l_Rate=0;
		String key="";
		HashMap<String,Double> ratelist = new HashMap<String,Double>();
		String l_query="select a.rate,b.t2 as formname,Currencycode,BranchCode from "+mTableName+" a "
				+ " inner join SystemSetting b on cast(a.Type as varchar)= b.t3 and b.t1='FERate' ";
		
		if(objCurrencyRateData.getAutokey() != 0)
			l_query += " And autokey = " + objCurrencyRateData.getAutokey();
		if(!objCurrencyRateData.getBranchCode().equals(""))
			l_query += " And branchcode = '" + objCurrencyRateData.getBranchCode() + "'";
		if(!objCurrencyRateData.getCurrencyCode().equals(""))
			l_query += " And currencyCode = '" + objCurrencyRateData.getCurrencyCode() + "'";
		if(!objCurrencyRateData.getUnitType().equals(""))
			l_query += " And unittype = '" + objCurrencyRateData.getUnitType() + "'";
		if(!objCurrencyRateData.getUnitName().equals(""))
			l_query += " And unitname = '" + objCurrencyRateData.getUnitName() + "'";
		if(objCurrencyRateData.getNote() != 0)
			l_query += " And note = "  + objCurrencyRateData.getNote();
		if(objCurrencyRateData.getBuyRate() != 0)
			l_query += " And buyrate = " + objCurrencyRateData.getBuyRate();
		if(!objCurrencyRateData.getBuyRateOperator().equals(""))
			l_query += " And buyrateoperator = '" + objCurrencyRateData.getBuyRateOperator() + "'";
		if(objCurrencyRateData.getSellRate() != 0)
			l_query += " And sellrate = " + objCurrencyRateData.getSellRate();
		if(!objCurrencyRateData.getSellRateOperator().equals(""))
			l_query += " And sellrateoperator = '" + objCurrencyRateData.getSellRateOperator() + "'";
		if(!objCurrencyRateData.getDenoName().equals("")) {
			l_query += " And DenoName = '" + objCurrencyRateData.getDenoName() + "'";
		}
		PreparedStatement l_pstmt=p_conn.prepareStatement(l_query);
		ResultSet l_RS=l_pstmt.executeQuery();
		while(l_RS.next()) {
			l_Rate=l_RS.getDouble("rate");
			key=l_RS.getString("formname")+"_"+l_RS.getString("Currencycode")+"_"+l_RS.getString("BranchCode");
			
			ratelist.put(key, l_Rate);
			
		}		
		return ratelist;
	}
	
	public ArrayList<TempData> getBuyCurrencyNotesforRev(String pCurCode,String pBranchCode,String pCounterID,int pNotes,int pSellSetting,Connection pConn) throws SQLException {
		StringBuffer l_Query = new StringBuffer();
		ResultSet l_RS = null;
		PreparedStatement pstmt = null;
		int isCounterWise=SharedLogic.getSystemSettingT12N3("CT");
		int col=1;
		ArrayList<TempData> l_tmpData = new ArrayList<TempData>();
		
		l_Query.append(" SELECT (TrCurRate) BuyRate,TD.N3 RemainBal,TD.autokey,DenoQty  FROM TransactionHeader TH INNER JOIN TransactionDetails TD ON TH.HeaderNo=TD.HeaderNo AND TH.ExchangeType=2 " +
				"WHERE 1=1 AND TrCurCode= ? ");
		if(!pBranchCode.equalsIgnoreCase("All") && !pBranchCode.equals("999"))
			l_Query.append(" and BranchCode = ? ");  
		if(!pCounterID.equals("") && isCounterWise==1)
			l_Query.append(" AND CounterID= ?");			
		l_Query.append(" AND TrNote=? AND DrCr=1 AND (TD.n3=0 OR TD.n3<DenoQty) Order by TD.autokey DESC");
		if(pSellSetting==1)
			l_Query.append("ASC");
		pstmt = pConn.prepareStatement(l_Query.toString());
		
		pstmt.setString(col++, pCurCode);
		if(!pBranchCode.equalsIgnoreCase("All") && !pBranchCode.equals("999"))
			pstmt.setString(col++, pBranchCode);
		if(!pCounterID.equals("") && isCounterWise==1)
			pstmt.setString(col++, pCounterID);
		pstmt.setInt(col++, pNotes);
		l_RS = pstmt.executeQuery();
		while (l_RS.next()) {
			TempData l_Data = new TempData();
			l_Data.setN1(l_RS.getDouble(1));
			l_Data.setN14(l_RS.getInt(2));
			l_Data.setN15(l_RS.getInt(3));
			l_Data.setN16(l_RS.getInt(4));
			l_tmpData.add(l_Data);
		}			
		return l_tmpData;
	}
	
	public boolean updatebuyRemainingNotesNStatus(TempData pData, long pStatus, long pTransRef, Connection pConn) throws SQLException{
		StringBuffer l_Query = new StringBuffer();
		PreparedStatement pstmt = null;
		boolean ret = false;
		l_Query.append("UPDATE TransactionDetails SET N1=?,N2=?,N3= ? WHERE 1=1 AND autokey= ? ");
		pstmt = pConn.prepareStatement(l_Query.toString());
		pstmt.setLong(1, pStatus);
		pstmt.setLong(2, pTransRef);
		pstmt.setInt(3, pData.getN14());
		pstmt.setInt(4, pData.getN15());
		if(pstmt.executeUpdate()>0)			
			ret =true;
		return ret;
		
	}
	
}
