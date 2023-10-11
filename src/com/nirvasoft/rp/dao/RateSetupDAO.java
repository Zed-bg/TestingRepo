package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.icbs.RateSetupData;

public class RateSetupDAO {
	private String mTable = "RateSetup";
	
	public RateSetupData getRateSetupData(int pType, String pFromBranch, String pToBank, String ptoBranch,
			Connection pConn) throws SQLException {
		RateSetupData l_RateSetupData = new RateSetupData();
		String l_Query = "SELECT SerialNo,Type,FromBank,FromBranch,ToBank,ToBranch,InOut,Com1,Com1Type,Com1Min,Com1Max,Com2,Com2Type,Com2Min,"
				+ "Com2Max,Com3,Com3Type,Com3Min,Com3Max,Com4,Com4Type,Com4Min,Com4Max,Com5,Com5Type,Com5Min,Com5Max,Com6,Com6Type,"
				+ "Com6Min,Com6Max,Com7,Com7Type,Com7Min,Com7Max,Com8,Com8Type,Com8Min,Com8Max,Com9,Com9Type,Com9Min,Com9Max,"
				+ "Com10,Com10Type,Com10Min,Com10Max,Com11,Com11Type,Com11Min,Com11Max,Com12,Com12Type,Com12Min,Com12Max,Com1DecimalType,"
				+ "Com2DecimalType,Com3DecimalType,Com4DecimalType,Com5DecimalType,Com6DecimalType,Com7DecimalType,Com8DecimalType,"
				+ "Com9DecimalType,Com10DecimalType,Com11DecimalType,Com12DecimalType FROM " + mTable
				+ " WHERE Type = ? " + "AND FromBank = 0 AND FromBranch = ? AND ToBank = ? AND ToBranch = ?";
		PreparedStatement l_pstmt = pConn.prepareStatement(l_Query);
		l_pstmt.setInt(1, pType);
		l_pstmt.setString(2, pFromBranch);
		l_pstmt.setString(3, pToBank);
		l_pstmt.setString(4, ptoBranch);
		ResultSet l_Rs = l_pstmt.executeQuery();
		while (l_Rs.next()) {
			readRecord(l_RateSetupData, l_Rs);
		}
		return l_RateSetupData;
	}
	
	private void readRecord(RateSetupData pdata, ResultSet pRS) throws SQLException {
		pdata.setSerialNo(pRS.getInt("SerialNo"));
		pdata.setType(pRS.getInt("Type"));
		pdata.setFromBank(pRS.getString("FromBank"));
		pdata.setFromBranch(pRS.getString("FromBranch"));
		pdata.setToBank(pRS.getString("ToBank"));
		pdata.setToBranch(pRS.getString("ToBranch"));
		pdata.setInOut(pRS.getInt("InOut"));
		pdata.setCom1(pRS.getDouble("Com1"));
		pdata.setCom1Type(pRS.getString("Com1Type"));
		pdata.setCom1Min(pRS.getDouble("Com1Min"));
		pdata.setCom1Max(pRS.getDouble("Com1Max"));
		pdata.setCom2(pRS.getDouble("Com2"));
		pdata.setCom2Type(pRS.getString("Com2Type"));
		pdata.setCom2Min(pRS.getDouble("Com2Min"));
		pdata.setCom2Max(pRS.getDouble("Com2Max"));
		pdata.setCom3(pRS.getDouble("Com3"));
		pdata.setCom3Type(pRS.getString("Com3Type"));
		pdata.setCom3Min(pRS.getDouble("Com3Min"));
		pdata.setCom3Max(pRS.getDouble("Com3Max"));
		pdata.setCom4(pRS.getDouble("Com4"));
		pdata.setCom4Type(pRS.getString("Com4Type"));
		pdata.setCom4Min(pRS.getDouble("Com4Min"));
		pdata.setCom4Max(pRS.getDouble("Com4Max"));
		pdata.setCom5(pRS.getDouble("Com5"));
		pdata.setCom5Type(pRS.getString("Com5Type"));
		pdata.setCom5Min(pRS.getDouble("Com5Min"));
		pdata.setCom5Max(pRS.getDouble("Com5Max"));
		pdata.setCom6(pRS.getDouble("Com6"));
		pdata.setCom6Type(pRS.getString("Com6Type"));
		pdata.setCom6Min(pRS.getDouble("Com6Min"));
		pdata.setCom6Max(pRS.getDouble("Com6Max"));
		pdata.setCom7(pRS.getDouble("Com7"));
		pdata.setCom7Type(pRS.getString("Com7Type"));
		pdata.setCom7Min(pRS.getDouble("Com7Min"));
		pdata.setCom7Max(pRS.getDouble("Com7Max"));
		pdata.setCom8(pRS.getDouble("Com8"));
		pdata.setCom8Type(pRS.getString("Com8Type"));
		pdata.setCom8Min(pRS.getDouble("Com8Min"));
		pdata.setCom8Max(pRS.getDouble("Com8Max"));
		pdata.setCom9(pRS.getDouble("Com9"));
		pdata.setCom9Type(pRS.getString("Com9Type"));
		pdata.setCom9Min(pRS.getDouble("Com9Min"));
		pdata.setCom9Max(pRS.getDouble("Com9Max"));
		pdata.setCom10(pRS.getDouble("Com10"));
		pdata.setCom10Type(pRS.getString("Com10Type"));
		pdata.setCom10Min(pRS.getDouble("Com10Min"));
		pdata.setCom10Max(pRS.getDouble("Com10Max"));
		pdata.setCom11(pRS.getDouble("Com11"));
		pdata.setCom11Type(pRS.getString("Com11Type"));
		pdata.setCom11Min(pRS.getDouble("Com11Min"));
		pdata.setCom11Max(pRS.getDouble("Com11Max"));
		pdata.setCom12(pRS.getDouble("Com12"));
		pdata.setCom12Type(pRS.getString("Com12Type"));
		pdata.setCom12Min(pRS.getDouble("Com12Min"));
		pdata.setCom12Max(pRS.getDouble("Com12Max"));
		pdata.setCom1DecimalType(pRS.getInt("Com1DecimalType"));
		pdata.setCom2DecimalType(pRS.getInt("Com2DecimalType"));
		pdata.setCom3DecimalType(pRS.getInt("Com3DecimalType"));
		pdata.setCom4DecimalType(pRS.getInt("Com4DecimalType"));
		pdata.setCom5DecimalType(pRS.getInt("Com5DecimalType"));
		pdata.setCom6DecimalType(pRS.getInt("Com6DecimalType"));
		pdata.setCom7DecimalType(pRS.getInt("Com7DecimalType"));
		pdata.setCom8DecimalType(pRS.getInt("Com8DecimalType"));
		pdata.setCom9DecimalType(pRS.getInt("Com9DecimalType"));
		pdata.setCom10DecimalType(pRS.getInt("Com10DecimalType"));
		pdata.setCom11DecimalType(pRS.getInt("Com11DecimalType"));
		pdata.setCom12DecimalType(pRS.getInt("Com12DecimalType"));
	}

	
}
