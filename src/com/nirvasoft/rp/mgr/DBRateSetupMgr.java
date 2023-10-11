package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.dao.RateAmountRangeSetupDAO;
import com.nirvasoft.rp.dao.RateSetupDAO;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.shared.icbs.RateAmountRangeSetupData;
import com.nirvasoft.rp.shared.icbs.RateSetupData;

public class DBRateSetupMgr {

	public static RateSetupData getCommissionRateSetupData(int pType, String pFromBranch, String pToBank,
			String pToBranch) throws Exception {
		DAOManager l_DAO = new DAOManager();
		Connection l_Conn = ConnAdmin.getConn3();//l_DAO.openICBSConnection();
		RateSetupData l_RateSetup = new RateSetupData();
		try {
			l_RateSetup = getCommissionRateSetupData(pType, pFromBranch, pToBank, pToBranch, l_Conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!l_Conn.isClosed()) {
				l_Conn.close();
			}
		}
		return l_RateSetup;
	}
	
	public static RateSetupData getCommissionRateSetupData(int pType, String pFromBranch, String pToBank,
			String pToBranch, Connection pConn) throws SQLException {
		RateSetupData l_RateSetupData = new RateSetupData();
		RateSetupDAO l_RateSetupDAO = new RateSetupDAO();

		l_RateSetupData = l_RateSetupDAO.getRateSetupData(pType, pFromBranch, pToBank, pToBranch, pConn);
		l_RateSetupData.setCom12AmountRangeDatalist(getAmountRangeDatalist(l_RateSetupData.getCom12Type(), pConn));

		return l_RateSetupData;
	}
	
	private static ArrayList<RateAmountRangeSetupData> getAmountRangeDatalist(String pComType, Connection pConn)
			throws NumberFormatException, SQLException {
		ArrayList<RateAmountRangeSetupData> l_AmountRangeList = new ArrayList<RateAmountRangeSetupData>();
		RateAmountRangeSetupDAO l_AmountRangeDAO = new RateAmountRangeSetupDAO();

		if (!pComType.trim().equals("")) {
			if (!pComType.equalsIgnoreCase("F") && !pComType.equalsIgnoreCase("R")) {
				if (pComType.length() >= 2) {
					if (l_AmountRangeDAO.getRateAmountRangeDataListByCommRangeType(
							Integer.parseInt(pComType.substring(1, pComType.length())), pConn)) {
						l_AmountRangeList = l_AmountRangeDAO.getRateAmountRangeDataList();
					}
				}
			}
		}
		return l_AmountRangeList;
	}
	
}
