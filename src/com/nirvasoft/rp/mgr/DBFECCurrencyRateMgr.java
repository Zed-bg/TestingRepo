package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.nirvasoft.rp.dao.CurrencyRateDAO;
import com.nirvasoft.rp.dao.SystemSettingDAO;

public class DBFECCurrencyRateMgr {

	public static Double getFECCurrencyRate(String pCurCode, String pFormName, String pBranchCode, Connection pConn) throws SQLException {
 		double ret = 0;
 		CurrencyRateDAO l_CurrencyRateDAO=new CurrencyRateDAO();
 		l_CurrencyRateDAO.getObjCurrencyRateData().setCurrencyCode(pCurCode);
 		l_CurrencyRateDAO.getObjCurrencyRateData().setNote(0);
 		l_CurrencyRateDAO.getObjCurrencyRateData().setUnitName("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setUnitType("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setDenoName("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setBranchCode(pBranchCode);
 		
 		SystemSettingDAO l_SystemSettingDAO=new SystemSettingDAO();
 		
		String l_columnName=l_SystemSettingDAO.GetRateColumnName(pFormName, pConn);
		if(l_columnName==null)
			l_columnName=l_SystemSettingDAO.GetRateColumnName("smDeposit", pConn);
		ret=l_CurrencyRateDAO.getRate(l_columnName, pConn);
		
 		return ret;
 	}
	
	public static HashMap<String,Double> getFECCurrencyRateList( Connection pConn) throws SQLException {
		HashMap<String,Double> ret = new HashMap<String,Double>();
 		CurrencyRateDAO l_CurrencyRateDAO=new CurrencyRateDAO();
 		l_CurrencyRateDAO.getObjCurrencyRateData().setNote(0);
 		l_CurrencyRateDAO.getObjCurrencyRateData().setUnitName("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setUnitType("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setDenoName("Non");
 		
		ret=l_CurrencyRateDAO.getRateList( pConn);
		
 		return ret;
 	}
	
	public static Double getFECCurrencyRate(String pCurCode, String pFormName, Connection pConn) throws SQLException {
 		double ret = 0;
 		CurrencyRateDAO l_CurrencyRateDAO=new CurrencyRateDAO();
 		l_CurrencyRateDAO.getObjCurrencyRateData().setCurrencyCode(pCurCode);
 		l_CurrencyRateDAO.getObjCurrencyRateData().setNote(0);
 		l_CurrencyRateDAO.getObjCurrencyRateData().setUnitName("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setUnitType("Non");
 		l_CurrencyRateDAO.getObjCurrencyRateData().setDenoName("Non");
 		
 		SystemSettingDAO l_SystemSettingDAO=new SystemSettingDAO();
 		
		String l_columnName=l_SystemSettingDAO.GetRateColumnName(pFormName, pConn);
		if(l_columnName==null)
			l_columnName=l_SystemSettingDAO.GetRateColumnName("smDeposit", pConn);
		ret=l_CurrencyRateDAO.getRate(l_columnName, pConn);
		
 		return ret;
 	}
	
}
