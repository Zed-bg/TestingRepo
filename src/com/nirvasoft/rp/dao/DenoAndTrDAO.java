package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.nirvasoft.rp.shared.DenoAndTrData;
import com.nirvasoft.rp.shared.SharedUtil;

public class DenoAndTrDAO {
	private ArrayList<DenoAndTrData> lstDenoAndTrData;
	private DenoAndTrData DenoAndTrBean;
	
	private String DenoAndTr = "DenoAndTr";
	
	public void setDenoAndTrBean(DenoAndTrData aDenoAndTrBean) {
		DenoAndTrBean = aDenoAndTrBean;
	}
	
	public DenoAndTrData getDenoAndTrBean() {
		return DenoAndTrBean;
	}
	
	public void setlstDenoAndTrDataList(ArrayList<DenoAndTrData> aDenoAndTrBeanList) {
		lstDenoAndTrData = aDenoAndTrBeanList;
	}
	
	public ArrayList<DenoAndTrData> getlstDenoAndTrDataList() {
		return lstDenoAndTrData;
	}
	
	public boolean addDenoAndTr(DenoAndTrData pData, Connection conn) throws SQLException{
		boolean result = false;
		
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO DenoAndTr ([TransNo], [TransRef], [SerialNo], [CounterID], " +
												"[EntryDate], [T1], [T2]) VALUES (?, ?, ?, ?, ?, ?, ?) ");
		updateRecord(pData, pstmt, true);
		pstmt.executeUpdate();
		pstmt.close();
		result = true;
		
		return result;
	}
	
	public boolean deleteDenoAndTr(DenoAndTrData pData, Connection conn){
		boolean result = false;
		try {
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM DenoAndTr WHERE TransRef = ? AND SerialNo=?");
			pstmt.setInt(1, pData.getTransRef());
			pstmt.setString(2, pData.getSerialNo());
			pstmt.executeUpdate();
			result = true;
		} catch (SQLException e) {
			
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public int getMaxSerialNo(String pCounterID, String pEntryDate, Connection conn){
		int ret = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(SerialNo) As 'SerialNo' FROM DenoAndTr " +
															"WHERE CounterID = ? AND EntryDate = ? ");
			pstmt.setString(1, pCounterID);
			pstmt.setString(2, pEntryDate);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				ret = rs.getInt(1);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean getDataByTransRef(int aTransRef, Connection aConn){
		boolean l_result = false;
		try {
			PreparedStatement pstmt = aConn.prepareStatement("SELECT * FROM "+DenoAndTr+" WHERE TransRef = ? ");
			
			pstmt.setInt(1, aTransRef);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				DenoAndTrBean = new DenoAndTrData();
				DenoAndTrBean.setTransNo(rs.getInt("TransNo"));
				DenoAndTrBean.setTransRef(rs.getInt("TransRef"));
				DenoAndTrBean.setSerialNo(rs.getString("SerialNo"));
				DenoAndTrBean.setCounterID(rs.getString("CounterId"));
				DenoAndTrBean.setEntryDate(SharedUtil.formatMIT2DateStr(rs.getString("EntryDate")));					
				
				l_result = true;
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			l_result = false;
		}
		return l_result;
	}
	
	private void updateRecord(DenoAndTrData aDenoAndTrBean, PreparedStatement aPS, boolean aIsNewRecord) throws SQLException{
		int i=1;
		if (aIsNewRecord){
			aPS.setInt(i++, aDenoAndTrBean.getTransNo());
			aPS.setInt(i++, aDenoAndTrBean.getTransRef());
			aPS.setString(i++, aDenoAndTrBean.getSerialNo());
			aPS.setString(i++, aDenoAndTrBean.getCounterID());
			aPS.setString(i++, aDenoAndTrBean.getEntryDate());
			aPS.setString(i++, aDenoAndTrBean.getT1());
			aPS.setString(i++, aDenoAndTrBean.getT2());
		}
	}
}
