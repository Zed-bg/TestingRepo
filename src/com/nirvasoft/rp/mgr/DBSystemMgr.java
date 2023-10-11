package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.nirvasoft.rp.core.ccbs.dao.CurrencyTableDao;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.dao.LastDatesDAO;
import com.nirvasoft.rp.dao.ProductsDAO;
import com.nirvasoft.rp.dao.ReferenceAccountsDAO;
import com.nirvasoft.rp.dao.SystemSettingDAO;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.shared.BudgetYearData;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ReferenceAccountsCriteriaData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.SystemData;
import com.nirvasoft.rp.shared.SystemSettingData;
import com.nirvasoft.rp.shared.icbs.ReferenceAccountsData;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;

public class DBSystemMgr {

	public static String getByForceCheck() throws Exception {
		String ret = "";
		DAOManager myConnectionUtil = new DAOManager();
		Connection conn = ConnAdmin.getConn3();//myConnectionUtil.openICBSConnection();
		try {
			ret = getByForceCheck(conn);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
		return ret;
	}
	
	public static String getByForceCheck(Connection conn) {
		String ret = "";
		SystemSettingDAO l_DAO = new SystemSettingDAO();
		SystemSettingData l_Data = new SystemSettingData();
		try {
			l_Data.setT1("CHQ");
			l_Data = l_DAO.getSystemSettingData(l_Data, conn);
			ret = l_Data.getT4();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	public static ReferenceAccountsData getReferenceAccCode(String pCode) throws Exception {
		ReferenceAccountsData l_ReferenceAccountData = new ReferenceAccountsData();
		DAOManager myConnectionUtil = new DAOManager();
		Connection conn = ConnAdmin.getConn3();//myConnectionUtil.openICBSConnection();

		try {
			l_ReferenceAccountData = getReferenceAccCode(pCode, conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
		return l_ReferenceAccountData;
	}
	
	public static ReferenceAccountsData getReferenceAccCode(String pCode, Connection pConn) {
		ReferenceAccountsData object = new ReferenceAccountsData();
		ReferenceAccountsDAO l_DAO = new ReferenceAccountsDAO();
		try {
			if (l_DAO.getReferenceAccCode(pCode, pConn)) {
				object = l_DAO.getReferenceAccountsDataBean();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static SystemData readSystemData() throws Exception {
		SystemData ret = new SystemData();
//		DAOManager myConnectionUtil = new DAOManager();
		Connection l_Conn = ConnAdmin.getConn3();//myConnectionUtil.openICBSConnection();
		try {
			ret.setProducts(readProducts(l_Conn));
			ret.setProductsType(DBProductMgr.getProductsType(l_Conn));
			ret.setProductsNumber(DBProductMgr.getProductsNumber(l_Conn));
			ret.setProductsConfiguration(DBProductMgr.getProductsConfiguration(l_Conn));
			ret.setSystemSettingDatas(getSystemSettingDatas(l_Conn));
			ret.setCurrencyCodes(DBSystemMgr.readCurrencyCodes(l_Conn));
			
			ret.setpByForceCheque(DBSystemMgr.getByForceCheck(l_Conn));
			ret.setAccountStatus(DBAccountMgr.getAccountStatus(l_Conn));
			ret.setpAuthorizerID(ConnAdmin.readExternalUrl("MobileMakerID"));
			ret.setpProductDataList(DBSystemMgr.readProductDataList(l_Conn)); // Product GL, Cash in hand GL (by product)
			ret.setpSystemSettingDataList(DBSystemMgr.getSystemSettingDataList(l_Conn));
			ret.setAccMinSetting(ret.getpSystemSettingDataList().get("ACTMINBAL").getN1());
			ret.setCurBalSetting(ret.getpSystemSettingDataList().get("ACTMINBAL").getN2());
			ret.setBarminbalSetting(ret.getpSystemSettingDataList().get("ACTMINBAL").getN3());
			ret.setpProductList(readProductList(l_Conn));
			ret.setpReferenceAccCodeList(DBSystemMgr.getReferenceAccCodeList(l_Conn));
			ret.setCutOfTime(GeneralUtility.checkCutOfTime());
		} finally {
			if (l_Conn != null) {
				if (!l_Conn.isClosed())
					l_Conn.close();
			}
		}

		return ret;
	}
	
	public static ArrayList<ProductData> readProducts(Connection pConn) throws SQLException {
		ArrayList<ProductData> ret = new ArrayList<ProductData>();
		ProductsDAO l_ProductDAO = new ProductsDAO();
		try {
			if (l_ProductDAO.getProducts(pConn))
				ret = l_ProductDAO.getProductDataList();
			else
				ret = null;
			//

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static ArrayList<SystemSettingData> getSystemSettingDatas(Connection pConn) throws SQLException {
		ArrayList<SystemSettingData> ret = new ArrayList<SystemSettingData>();
		SystemSettingDAO l_DAO = new SystemSettingDAO();
		try {
			ret = l_DAO.getSystemSettingDatas(pConn);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	public static ArrayList<ReferenceData> readCurrencyCodes() {
		ArrayList<ReferenceData> ret = new ArrayList<ReferenceData>();
		Connection conn = ConnAdmin.getConn3();// lConn.openICBSConnection();
		try {
			ret = readCurrencyCodes(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public static ArrayList<ReferenceData> readCurrencyCodes(Connection pConn) throws SQLException {
		ArrayList<ReferenceData> ret = new ArrayList<ReferenceData>();
		CurrencyTableDao lCurDAO = new CurrencyTableDao();
		if (lCurDAO.getCurrencyCodes(pConn))
			ret = lCurDAO.getCurrencyDataList();
		else
			ret = null;
		return ret;
	}
	
	public static  HashMap<String,ProductData> readProductDataList(Connection pConn) throws SQLException{
		return new ProductsDAO().readProduct( pConn );
	}
	
	public static HashMap<String,SystemSettingData> getSystemSettingDataList(Connection pConn) throws SQLException {
		HashMap<String,SystemSettingData> ret = new HashMap<String,SystemSettingData>();
		SystemSettingDAO l_DAO = new SystemSettingDAO();
		try {
			ret = l_DAO.getSystemSettingDataList(pConn);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	public static HashMap<String,ProductData> readProductList(Connection pConn) throws SQLException {
		HashMap<String,ProductData> ret = new HashMap<String,ProductData>();
		ProductsDAO l_ProductDAO = new ProductsDAO();
		try {
				ret = l_ProductDAO.getProductDataList(pConn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static HashMap<String,Double> getFECCurrencyRateList() throws Exception {
		HashMap<String,Double> ret = new HashMap<String,Double>();
		Connection conn = ConnAdmin.getConn3();;//lConn.openICBSConnection();
		ret = DBFECCurrencyRateMgr.getFECCurrencyRateList(conn);
		conn.close();
		return ret;
	}
	
	public static HashMap<String, ReferenceAccountsData> getReferenceAccCodeList(Connection conn) throws SQLException {
		return new ReferenceAccountsDAO().getReferenceAccCodeList(conn);
	}
	
	public static BudgetYearData getBYearData(int pType) throws SQLException {
		Connection conn =null;	
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();
		try{
			conn = ConnAdmin.getConn3();
			l_BudgetData=l_lastDateDAO.getBudgetYearData(pType, conn);
		}finally{
			conn.close();
		}
		return l_BudgetData;
	}
	
	public static String getGLCode(String pFunctionID, Connection conn) throws SQLException{
		String ret = "";
		ReferenceAccountsCriteriaData l_Cri = new ReferenceAccountsCriteriaData();
		ReferenceAccountsData object = new ReferenceAccountsData();
		ReferenceAccountsDAO l_DAO = new ReferenceAccountsDAO();
		if(!pFunctionID.equals("")){
			l_Cri.GLAccNumber = pFunctionID;			
			object = l_DAO.getReferenceAccount(l_Cri, conn);
		}		
		ret = object.getGLCode();
		return ret;
	}
	
	public static ArrayList<ProductData> readProducts() throws SQLException {
		ArrayList<ProductData> ret = new ArrayList<ProductData>();
		Connection conn = ConnAdmin.getConn3();
		ret = readProducts(conn);
		if (!conn.isClosed()) {
			conn.close();
		}

		return ret;
	}
	
	
}
