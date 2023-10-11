package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.icbs.RateAmountRangeSetupData;

public class RateAmountRangeSetupDAO {
	private RateAmountRangeSetupData mAmountRangeData;
	private ArrayList<RateAmountRangeSetupData> mAmountRangeDataList;
	private String mTable = "RateAmountRangeSetup";
	
	public boolean getRateAmountRangeDataListByCommRangeType(int pComRangeType, Connection pConn) throws SQLException {
		boolean l_result = false;
		mAmountRangeDataList = new ArrayList<RateAmountRangeSetupData>();

		StringBuilder l_Query = new StringBuilder("");
		l_Query.append("SELECT SerialNo, ComRangeType, FromAmount, ToAmount, CRate, ComMin, ComMax, CommType From "
				+ mTable + " WHERE 1=1");
		l_Query.append(" AND ComRangeType = ?");

		System.out.println(l_Query);
		PreparedStatement l_pstmt = pConn.prepareStatement(l_Query.toString());
		l_pstmt.setInt(1, pComRangeType);
		ResultSet l_Rs = l_pstmt.executeQuery();
		while (l_Rs.next()) {
			l_result = true;
			mAmountRangeData = new RateAmountRangeSetupData();
			readRecord(mAmountRangeData, l_Rs);
			mAmountRangeDataList.add(mAmountRangeData);
		}
		return l_result;
	}
	
	private void readRecord(RateAmountRangeSetupData pdata, ResultSet pRs) throws SQLException {

		pdata.setSerialNo(pRs.getInt("SerialNo"));
		pdata.setComRangeType(pRs.getInt("ComRangeType"));
		pdata.setFromAmount(pRs.getDouble("FromAmount"));
		pdata.setToAmount(pRs.getDouble("ToAmount"));
		pdata.setCRate(pRs.getDouble("CRate"));
		pdata.setComMin(pRs.getDouble("ComMin"));
		pdata.setComMax(pRs.getDouble("ComMax"));
		pdata.setCommType(pRs.getString("CommType"));
	}
	
	public ArrayList<RateAmountRangeSetupData> getRateAmountRangeDataList() {
		return mAmountRangeDataList;
	}
}
