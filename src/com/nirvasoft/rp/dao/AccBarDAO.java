package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.shared.AccBarData;
import com.nirvasoft.rp.util.SharedUtil;

public class AccBarDAO {
	public double getTotalBarAmount(String pAccNumber, Connection pConn) throws SQLException {
		double ret = 0;
		
		String l_Query = "SELECT A.AccNumber, Sum(A.BarAmount) BAmt " +
				"FROM AccBar A WHERE A.AccNumber = ? Group By A.AccNumber";

		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNumber);

		ResultSet l_RS = pstmt.executeQuery();
		while (l_RS.next()) {
			ret = l_RS.getDouble("BAmt");
		}

		pstmt.close();		
		return ret;
	}
	
	public ArrayList<AccBarData> getAccBars(String pAccNumber, Connection conn) throws ParserConfigurationException, SAXException,ClassNotFoundException,SQLException{
		ArrayList<AccBarData> ret = new ArrayList<AccBarData>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT AccNumber, BarAmount, Date, Remark, BarType,RefNo FROM AccBar WHERE AccNumber = ? " );
			ps.setString(1, pAccNumber);

			ResultSet rs = ps.executeQuery();			
			while (rs.next()) {
				AccBarData object = new AccBarData();
				readRecord(object, rs);
				ret.add(object);				
			}
			ps.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	private void readRecord(AccBarData aAccBarBean, ResultSet aRS) throws SQLException
	{		
		aAccBarBean.setAccountNumber(aRS.getString("AccNumber"));
		aAccBarBean.setBarAmount(aRS.getDouble("BarAmount"));
		aAccBarBean.setDate(SharedUtil.formatDBDate2MIT(aRS.getDate("Date").toString()));
		aAccBarBean.setRemark(aRS.getString("Remark"));
		aAccBarBean.setBarType(aRS.getInt("BarType"));
		aAccBarBean.setRefNo(aRS.getString("RefNo"));		
	}
}
