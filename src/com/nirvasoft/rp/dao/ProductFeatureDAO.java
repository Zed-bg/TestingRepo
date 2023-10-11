package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.icbs.ProductFeatureData;

public class ProductFeatureDAO {
	private String mTableName = "T00001";
	private ProductFeatureData mProductFeatureData;
	
	public ArrayList<ProductFeatureData> getProdcutFeatures(String pProductId, Connection pConn) {
		ArrayList<ProductFeatureData> ret = new ArrayList<ProductFeatureData>();
		try {
			String l_Query = "SELECT SYSKEY, T1, T2, T3, T4, T5, N1, N2, N3, N4, N5 FROM " + mTableName
					+ " WHERE 1=1 AND T1 = ?";

			PreparedStatement pstmt = pConn.prepareStatement(l_Query);
			pstmt.setString(1, pProductId);

			ResultSet l_Rs = pstmt.executeQuery();
			while (l_Rs.next()) {
				ProductFeatureData object = new ProductFeatureData();
				readRecord(object, l_Rs);
				ret.add(object);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	private void readRecord(ProductFeatureData pProductFeatureData, ResultSet pRs) throws SQLException {
		pProductFeatureData.setSysKey(pRs.getLong("SysKey"));
		pProductFeatureData.setProductId(pRs.getString("T1"));
		pProductFeatureData.setFeatureId(pRs.getLong("N1"));
		pProductFeatureData.setFeatureValue(pRs.getLong("N2"));
		pProductFeatureData.setFeatureDescription(pRs.getString("T2"));
		pProductFeatureData.setExpression(pRs.getString("T3"));
		pProductFeatureData.setGLCode(pRs.getString("T4"));
	}
	
	public String getCashInHandGL(String aCurrency,int pCurrencySetting,Connection aConn) throws SQLException{
		String  l_GLCode = "";ResultSet rs = null;PreparedStatement l_pstmt= null;
				
		String l_query = "SELECT Distinct(T4) FROM T00001 T INNER JOIN CurrencyTable C ON C.CurCode= ?  AND T.N2=C.N1 Where T.N1=16 ";
		
		l_pstmt = aConn.prepareStatement(l_query);
		l_pstmt.setString(1, aCurrency);
		rs = l_pstmt.executeQuery();
		while(rs.next()){
			l_GLCode = rs.getString(1);
		}
		if(l_GLCode.equals("")){
			if(aCurrency.equals("MMK")){
				l_query = "SELECT Distinct(T4) FROM T00001 T INNER JOIN CurrencyTable C ON C.CurCode= ?  AND (T.N2=C.N1 OR T.N2=0) Where T.N1=10 ";
			} else {
				l_query = "SELECT Distinct(T4) FROM T00001 T INNER JOIN CurrencyTable C ON C.CurCode= ?  AND T.N2=C.N1 Where T.N1=10 ";
			}			
			l_pstmt = aConn.prepareStatement(l_query);
			l_pstmt.setString(1, aCurrency);
			rs = l_pstmt.executeQuery();
			while(rs.next()){
				l_GLCode = rs.getString(1);
			}
		}
		
		return l_GLCode;
	}
}
