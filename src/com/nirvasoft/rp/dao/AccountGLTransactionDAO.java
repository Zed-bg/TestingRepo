package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.AccountGLTransactionData;

public class AccountGLTransactionDAO {

	public boolean addAccountGLTransaction(AccountGLTransactionData pAccountGLTransactionData, Connection conn){
		boolean result = false;PreparedStatement pstmt  = null;
		try {
			StringBuffer l_Query = new StringBuffer();
			l_Query.append("INSERT INTO ACCOUNTGLTRANSACTION (TransNo, AccNumber, GLCode1, GLCode2, GLCode3, GLCode4, " +
					"GLCode5, BaseCurCode, BaseCurOperator, BaseRate, BaseAmount, MediumCurCode, MediumCurOperator, " +
					"MediumRate, MediumAmount, TrCurCode,TrCurOperator, TrRate, TrAmount, TrPrevBalance, N1, N2, N3, T1, T2, T3) VALUES " +
					"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			pstmt = conn.prepareStatement(l_Query.toString());
			pstmt.setQueryTimeout(DAOManager.getTransTime());
			updateRecord(pAccountGLTransactionData, pstmt);
			pstmt.setQueryTimeout(DAOManager.getTransTime());
			if(pstmt.executeUpdate()>0){
				result = true;
			}			
			pstmt.close();
		} catch (SQLException e) {
			// TODO: handle exception
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public void updateRecord(AccountGLTransactionData pAccountGLTransactionData, PreparedStatement ps) throws SQLException{
		int index = 1;
		ps.setLong(index++, pAccountGLTransactionData.getTransNo());
		ps.setString(index++, pAccountGLTransactionData.getAccNumber());
		ps.setString(index++, pAccountGLTransactionData.getGLCode1());
		ps.setString(index++, pAccountGLTransactionData.getGLCode2());
		ps.setString(index++, pAccountGLTransactionData.getGLCode3());
		ps.setString(index++, pAccountGLTransactionData.getGLCode4());
		ps.setString(index++, pAccountGLTransactionData.getGLCode5());
		ps.setString(index++, pAccountGLTransactionData.getBaseCurCode());
		ps.setString(index++, pAccountGLTransactionData.getBaseCurOperator());
		ps.setDouble(index++, pAccountGLTransactionData.getBaseRate());
		ps.setDouble(index++, pAccountGLTransactionData.getBaseAmount());
		ps.setString(index++, pAccountGLTransactionData.getMediumCurCode());
		ps.setString(index++, pAccountGLTransactionData.getMediumCurOperator());
		ps.setDouble(index++, pAccountGLTransactionData.getMediumRate());
		ps.setDouble(index++, pAccountGLTransactionData.getMediumAmount());
		ps.setString(index++, pAccountGLTransactionData.getTrCurCode());
		ps.setString(index++, pAccountGLTransactionData.getTrCurOperator());
		ps.setDouble(index++, pAccountGLTransactionData.getTrRate());
		ps.setDouble(index++, pAccountGLTransactionData.getTrAmount());
		ps.setDouble(index++, pAccountGLTransactionData.getTrPrevBalance());
		ps.setInt(index++, pAccountGLTransactionData.getN1());
		ps.setInt(index++, pAccountGLTransactionData.getN2());
		ps.setInt(index++, pAccountGLTransactionData.getN3());
		ps.setString(index++, pAccountGLTransactionData.getT1());
		ps.setString(index++, pAccountGLTransactionData.getT2());
		ps.setString(index++, pAccountGLTransactionData.getT3());
	}
	
	public boolean addAccountGLTransaction(String tmp_GlTranspreapre, Connection conn){
		boolean result = false;PreparedStatement pstmt  = null;
		try {
			StringBuffer l_Query = new StringBuffer();
			l_Query.append("INSERT INTO ACCOUNTGLTRANSACTION SELECT TransNo, AccNumber, GLCode1, GLCode2, GLCode3, GLCode4, "
					+ "GLCode5, BaseCurCode, BaseCurOperator, BaseRate, BaseAmount, MediumCurCode, MediumCurOperator, "
					+ "MediumRate, MediumAmount, TrCurCode,TrCurOperator, TrRate, TrAmount, TrPrevBalance, N1, N2, N3, T1, T2, T3 "
					+ "FROM " + tmp_GlTranspreapre);
			pstmt = conn.prepareStatement(l_Query.toString());
			pstmt.setQueryTimeout(DAOManager.getTransTime());
			if(pstmt.executeUpdate()>0){
				result = true;
			}			
			pstmt.close();
		} catch (SQLException e) {
			// TODO: handle exception
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public AccountGLTransactionData getGLTransactions(Long pTransNo,Connection conn) {
		AccountGLTransactionData l_AccountGLTransactionData=new AccountGLTransactionData();
		try {
			String l_query="SELECT TransNo"
      +",AccNumber"
      +",GLCode1"
      +" ,GLCode2"
      +" ,GLCode3"
      +",GLCode4"
      +",GLCode5"
      +" ,BaseCurCode"
      +",BaseCurOperator"
      +",BaseRate"
      +",BaseAmount"
      +",MediumCurCode"
      +",MediumCurOperator"
      +",MediumRate"
      +",MediumAmount"
      +",TrCurCode"
      +",TrCurOperator"
      +",TrRate"
      +",TrAmount"
      +",TrPrevBalance"
      +" ,N1"
      +" ,T1"
      +" ,T2"
      +" ,T3"
      +" ,N2"
      +" ,N3"
  	  +" FROM [AccountGLTransaction] WHERE 1=1 AND TransNo=? AND N1 NOT IN(255)";
			PreparedStatement pstmt=conn.prepareStatement(l_query);
			pstmt.setLong(1, pTransNo);
			java.sql.ResultSet l_RS=pstmt.executeQuery();
			while(l_RS.next()) {
				l_AccountGLTransactionData=new AccountGLTransactionData();
				readRecord(l_AccountGLTransactionData, l_RS);
				
			}
			pstmt.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return l_AccountGLTransactionData;
	}
	
	public void readRecord(AccountGLTransactionData pAccountGLTransactionData,java.sql.ResultSet pRS){
		try {
			
			pAccountGLTransactionData.setTransNo(pRS.getInt("TransNo"));
			pAccountGLTransactionData.setAccNumber(pRS.getString("AccNumber"));
			pAccountGLTransactionData.setGLCode1(pRS.getString("GLCode1"));
			pAccountGLTransactionData.setGLCode2(pRS.getString("GLCode2"));
			pAccountGLTransactionData.setGLCode3(pRS.getString("GLCode3"));
			pAccountGLTransactionData.setGLCode4(pRS.getString("GLCode4"));
			pAccountGLTransactionData.setGLCode5(pRS.getString("GLCode5"));
			pAccountGLTransactionData.setBaseCurCode(pRS.getString("BaseCurCode"));
			pAccountGLTransactionData.setBaseCurOperator(pRS.getString("BaseCurOperator"));
			pAccountGLTransactionData.setBaseRate(pRS.getDouble("BaseRate"));
			pAccountGLTransactionData.setBaseAmount(pRS.getDouble("BaseAmount"));
			pAccountGLTransactionData.setMediumCurCode(pRS.getString("MediumCurCode"));
			pAccountGLTransactionData.setMediumCurOperator(pRS.getString("MediumCurOperator"));
			pAccountGLTransactionData.setMediumRate(pRS.getDouble("MediumRate"));
			pAccountGLTransactionData.setMediumAmount(pRS.getDouble("MediumAmount"));
			pAccountGLTransactionData.setTrCurCode(pRS.getString("TrCurCode"));
			pAccountGLTransactionData.setTrCurOperator(pRS.getString("TrCurOperator"));
			pAccountGLTransactionData.setTrRate(pRS.getDouble("TrRate"));
			pAccountGLTransactionData.setTrAmount(pRS.getDouble("TrAmount"));
			pAccountGLTransactionData.setTrPrevBalance(pRS.getDouble("TrPrevBalance"));
			pAccountGLTransactionData.setN1(pRS.getInt("N1"));
			pAccountGLTransactionData.setN2(pRS.getInt("N2"));
			pAccountGLTransactionData.setN3(pRS.getInt("N3"));
			pAccountGLTransactionData.setT1(pRS.getString("T1"));
			pAccountGLTransactionData.setT2(pRS.getString("T2"));
			pAccountGLTransactionData.setT3(pRS.getString("T3"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean UpdateGLAccountTransaction(AccountGLTransactionData pAccountTransactionData,Connection conn) {
		boolean result=false;
		try {
			String l_query="UPDATE [AccountGLTransaction]"
				  +" SET TransNo = ?"
				   	  +" ,AccNumber = ?"
				      +" ,GLCode1 = ?"
				      +" ,GLCode2 = ?"
				      +" ,GLCode3 = ?"
				      +" ,GLCode4 = ?"
				      +" ,GLCode5 = ?"
				      +" ,BaseCurCode = ?"
				      +" ,BaseCurOperator = ?"
				      +" ,BaseRate = ?"
				      +" ,BaseAmount = ?"
				      +" ,MediumCurCode = ?"
				      +" ,MediumCurOperator = ?"
				      +" ,MediumRate = ?"
				      +" ,MediumAmount =?"
				      +" ,TrCurCode = ?"
				      +" ,TrCurOperator = ?"
				      +" ,TrRate = ?"
				      +" ,TrAmount = ?"
				      +" ,TrPrevBalance = ?"
				      +" ,N1 = ?"
				      +" ,N2 = ?"
				      +" ,N3 = ?"
				      +" ,T1 = ?"
				      +" ,T2 = ?"
				      +" ,T3 = ?"
				      +" WHERE TransNo=?";
			PreparedStatement pstmt=conn.prepareStatement(l_query);
			updateRecord(pAccountTransactionData, pstmt);
			pstmt.setLong(27, pAccountTransactionData.getTransNo());
			pstmt.setQueryTimeout(DAOManager.getTransTime());
			if(pstmt.executeUpdate()>0) {
				result=true;
			}
			pstmt.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	public boolean CheckTableExist(String pTableName,Connection conn) {
		boolean l_result=false;
		try {
			String l_query="select case when exists((select * from information_schema.tables where table_name = '" + pTableName + "')) then 1 else 0 end";
			PreparedStatement l_pstmt=conn.prepareStatement(l_query);
			l_pstmt.setQueryTimeout(DAOManager.getTransTime());
			ResultSet l_RS=l_pstmt.executeQuery();
			while(l_RS.next()) {
				if(l_RS.getInt(1)==1) {
					l_result=true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			l_result=false;
		}
		return l_result;
	}
}
