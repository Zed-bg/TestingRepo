package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.nirvasoft.rp.shared.SystemSettingData;
import com.nirvasoft.rp.util.SharedUtil;

public class SystemSettingDAO {

	public SystemSettingData getSystemSettingData(SystemSettingData pSystemSettingData, Connection conn)
			throws SQLException {
		SystemSettingData ret = new SystemSettingData();
		StringBuffer l_query = new StringBuffer();
		int l_BNK = 0;
		String l_Query = "SELECT N1 FROM SystemSetting Where T1='BNK'";
		PreparedStatement pstmt1 = conn.prepareStatement(l_Query);
		ResultSet rs1 = pstmt1.executeQuery();
		while (rs1.next()) {
			l_BNK = rs1.getInt(1);
		}
		if (l_BNK != 4) {
			l_query.append(
					"SELECT " + "[SystemSetting].[syskey], [SystemSetting].[autokey], [SystemSetting].[createddate], "
							+ "[SystemSetting].[userid], [SystemSetting].[SyncStatus], [SystemSetting].[SyncBatch], "
							+ "[SystemSetting].[t1], [SystemSetting].[t2], [SystemSetting].[t3], "
							+ "[SystemSetting].[t4], [SystemSetting].[n1], [SystemSetting].[n2], [SystemSetting].[n3], [SystemSetting].[n4], "
							+ "[SystemSetting].[n5],[SystemSetting].[n6],[SystemSetting].[n7] FROM [SystemSetting] "
							+ "WHERE 1=1");
		} else {
			l_query.append(
					"SELECT " + "[SystemSetting].[syskey], [SystemSetting].[autokey], [SystemSetting].[createddate], "
							+ "[SystemSetting].[userid], [SystemSetting].[SyncStatus], [SystemSetting].[SyncBatch], "
							+ "[SystemSetting].[t1], [SystemSetting].[t2], [SystemSetting].[t3], "
							+ "[SystemSetting].[t4], [SystemSetting].[n1], [SystemSetting].[n2], [SystemSetting].[n3], [SystemSetting].[n4], "
							+ "[SystemSetting].[n5] FROM [SystemSetting] " + "WHERE 1=1");
		}

		prepareWhereClause(pSystemSettingData, l_query);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		prepareCriteria(pstmt, pSystemSettingData);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			readRecord(ret, rs, l_BNK);
		}

		return ret;
	}
	
	private void prepareWhereClause(SystemSettingData aSystemSettingDataBean, StringBuffer aQuery) {
		if (!aSystemSettingDataBean.getT1().equals("")) {
			aQuery.append(" AND [SystemSetting].[t1] = ?");
		}
	}
	
	private void prepareCriteria(PreparedStatement aPS, SystemSettingData aSystemSettingDataBean) throws SQLException {
		int i = 1;
		if (!aSystemSettingDataBean.getT1().equals("")) {
			aPS.setString(i++, aSystemSettingDataBean.getT1());
		}
	}
	
	private void readRecord(SystemSettingData aSystemSettingDataBean, ResultSet aRS, int pBNK) throws SQLException {

		aSystemSettingDataBean.setsyskey(aRS.getLong("syskey"));
		aSystemSettingDataBean.setautokey(aRS.getLong("autokey"));
		aSystemSettingDataBean.setCreatedDate(SharedUtil.formatDBDate2MIT(aRS.getString("createddate")));
		aSystemSettingDataBean.setUserId(aRS.getString("userid"));
		aSystemSettingDataBean.setSyncStatus(aRS.getInt("SyncStatus"));
		aSystemSettingDataBean.setSyncBatch(aRS.getLong("SyncBatch"));
		aSystemSettingDataBean.setT1(aRS.getString("t1"));
		aSystemSettingDataBean.setT2(aRS.getString("t2"));
		aSystemSettingDataBean.setT3(aRS.getString("t3"));
		aSystemSettingDataBean.setT4(aRS.getString("t4"));
		aSystemSettingDataBean.setN1(aRS.getInt("n1"));
		aSystemSettingDataBean.setN2(aRS.getInt("n2"));
		aSystemSettingDataBean.setN3(aRS.getInt("n3"));
		aSystemSettingDataBean.setN4(aRS.getInt("n4"));
		aSystemSettingDataBean.setN5(aRS.getInt("n5"));

		if (pBNK != 4) {
			aSystemSettingDataBean.setN6(aRS.getInt("n6"));
			aSystemSettingDataBean.setN7(aRS.getInt("n7"));
		}
	}
	
	public ArrayList<SystemSettingData> getSystemSettingDatas(Connection conn) throws SQLException {
		ArrayList<SystemSettingData> ret = new ArrayList<SystemSettingData>();
		StringBuffer l_query = new StringBuffer();

		int l_BNK = 0;
		String l_Query = "SELECT N1 FROM SystemSetting Where T1='BNK'";
		PreparedStatement pstmt1 = conn.prepareStatement(l_Query);
		ResultSet rs1 = pstmt1.executeQuery();
		while (rs1.next()) {
			l_BNK = rs1.getInt(1);
		}

		if (l_BNK != 4) {
			l_query.append(
					"SELECT " + "[SystemSetting].[syskey], [SystemSetting].[autokey], [SystemSetting].[createddate], "
							+ "[SystemSetting].[userid], [SystemSetting].[SyncStatus], [SystemSetting].[SyncBatch], "
							+ "[SystemSetting].[t1], [SystemSetting].[t2], [SystemSetting].[t3], "
							+ "[SystemSetting].[t4], [SystemSetting].[n1], [SystemSetting].[n2], [SystemSetting].[n3], [SystemSetting].[n4], "
							+ "[SystemSetting].[n5],[SystemSetting].[n6],[SystemSetting].[n7] FROM [SystemSetting] "
							+ "WHERE 1=1");
		} else {
			l_query.append(
					"SELECT " + "[SystemSetting].[syskey], [SystemSetting].[autokey], [SystemSetting].[createddate], "
							+ "[SystemSetting].[userid], [SystemSetting].[SyncStatus], [SystemSetting].[SyncBatch], "
							+ "[SystemSetting].[t1], [SystemSetting].[t2], [SystemSetting].[t3], "
							+ "[SystemSetting].[t4], [SystemSetting].[n1], [SystemSetting].[n2], [SystemSetting].[n3], [SystemSetting].[n4], "
							+ "[SystemSetting].[n5] FROM [SystemSetting] " + "WHERE 1=1");
		}

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			SystemSettingData object = new SystemSettingData();
			readRecord(object, rs, l_BNK);
			ret.add(object);
		}
		pstmt.close();
		return ret;
	}
	
	//SSH for Currency Rate
	public String GetRateColumnName(String FormName,Connection conn) throws SQLException {
		String l_columnName=null;
		String T1="FERate";		
		String l_query="SELECT t3 FROM SystemSetting WHERE t1=? AND t2=?";
		PreparedStatement l_pstmt=conn.prepareStatement(l_query);
		l_pstmt.setString(1, T1);
		l_pstmt.setString(2, FormName);
		ResultSet l_Rs=l_pstmt.executeQuery();
		while(l_Rs.next()) {
			l_columnName=l_Rs.getString("t3");
		}		
		return l_columnName;
	}
	
	public HashMap<String,SystemSettingData> getSystemSettingDataList(Connection conn) throws SQLException {
		HashMap<String,SystemSettingData> ret = new HashMap<String,SystemSettingData>();
		StringBuffer l_query = new StringBuffer();

		int l_BNK = 0;
		String l_Query = "SELECT N1 FROM SystemSetting Where T1='BNK'";
		PreparedStatement pstmt1 = conn.prepareStatement(l_Query);
		ResultSet rs1 = pstmt1.executeQuery();
		while (rs1.next()) {
			l_BNK = rs1.getInt(1);
		}

		if (l_BNK != 4) {
			l_query.append(
					"SELECT " + "[SystemSetting].[syskey], [SystemSetting].[autokey], [SystemSetting].[createddate], "
							+ "[SystemSetting].[userid], [SystemSetting].[SyncStatus], [SystemSetting].[SyncBatch], "
							+ "[SystemSetting].[t1], [SystemSetting].[t2], [SystemSetting].[t3], "
							+ "[SystemSetting].[t4], [SystemSetting].[n1], [SystemSetting].[n2], [SystemSetting].[n3], [SystemSetting].[n4], "
							+ "[SystemSetting].[n5],[SystemSetting].[n6],[SystemSetting].[n7] FROM [SystemSetting] "
							+ "WHERE 1=1");
		} else {
			l_query.append(
					"SELECT " + "[SystemSetting].[syskey], [SystemSetting].[autokey], [SystemSetting].[createddate], "
							+ "[SystemSetting].[userid], [SystemSetting].[SyncStatus], [SystemSetting].[SyncBatch], "
							+ "[SystemSetting].[t1], [SystemSetting].[t2], [SystemSetting].[t3], "
							+ "[SystemSetting].[t4], [SystemSetting].[n1], [SystemSetting].[n2], [SystemSetting].[n3], [SystemSetting].[n4], "
							+ "[SystemSetting].[n5] FROM [SystemSetting] " + "WHERE 1=1");
		}

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			SystemSettingData object = new SystemSettingData();
			String key = rs.getString("t1");
			readRecord(object, rs, l_BNK);
			ret.put(key, object);
		}
		pstmt.close();
		return ret;
	}
	
}
