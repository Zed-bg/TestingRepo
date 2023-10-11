package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.TransAmtLimitData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;

public class AccountTransactionDAO {
	
	private TransactionData TransData;
	private ArrayList<TransactionData> lstTransData;
	
	public void setTransactionData(TransactionData pTransData) {
		TransData = pTransData;
	}
	
	public TransactionData getTransactionData() {
		return TransData;
	}
	
	public void setTransactionDataList(ArrayList<TransactionData> pTransDataList) {
		lstTransData = pTransDataList;
	}
	public ArrayList<TransactionData> getTransactionDataList() {
		return lstTransData;
	}
	
	public AccountTransactionDAO()
	{
		lstTransData = new ArrayList<TransactionData>();
		TransData = new TransactionData();
	}
	
	public TransAmtLimitData getTransCountNAmountPerDay(String pAccNumber, String pEffectiveDate, int pTransTypefrom, int pTransTypeTo, Connection conn) throws SQLException{
		TransAmtLimitData l_Data = new TransAmtLimitData();
		int l_Count = 0; double l_Amt = 0.00;
		String l_AmtSubQuery = "";
		boolean isCashDept = false,isCashWith = false,isTransfer= false;
		if(pTransTypefrom>=0 && pTransTypeTo<=199)
			isCashDept = true;
		else if(pTransTypefrom>=500 && pTransTypeTo<=699)
			isCashWith = true;
		else if((pTransTypefrom>200 && pTransTypeTo<=499) || (pTransTypefrom>700 && pTransTypeTo<=999)){
			isTransfer = true;
		}
		if(isCashDept || isCashWith){
			l_AmtSubQuery = "SUM(CASE WHEN TransType=705 THEN ISNULL(IBTAmt,0) ELSE TrAmount END)";
		}else {
			l_AmtSubQuery = "SUM(TrAmount)";
		}
		
		String l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransaction A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo " ;
				
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? and TransDate=? ) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? and TransDate=? ) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate=?";
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) and TransDate=? )" ;
		}
		PreparedStatement l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "D",isCashDept,isCashWith,isTransfer, l_pstmt);
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		ResultSet rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		
		l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransactionOld A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo ";
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? and TransDate=? ) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? and TransDate=? ) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate=?";
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) and TransDate=? )" ;
		}
		l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "D",isCashDept,isCashWith,isTransfer, l_pstmt);
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		l_pstmt.close();
		l_Data = new TransAmtLimitData();
		l_Data.setTransAmount(l_Amt);
		l_Data.setTransCount(l_Count);
		return l_Data;
	}
	
	private void updateRecordLimit(String pAccNumber, String pEffectiveDate, int pTransTypefrom, int pTransTypeTo,
			String pType,boolean isCashDep,boolean isCashWith,boolean isTransfer,PreparedStatement aPS) throws SQLException {
		int i=1;
		if(isCashDep || isCashWith){
			aPS.setString(i++, pAccNumber);
			aPS.setString(i++, pEffectiveDate);
			if(pType.equals("W")){
				aPS.setString(i++, pEffectiveDate);
				aPS.setString(i++, pEffectiveDate);
				aPS.setString(i++, pEffectiveDate);
			}else if (pType.equals("M") || pType.equals("Y")){
				aPS.setString(i++, pEffectiveDate);
			}			
		}
		aPS.setInt(i++, pTransTypefrom);
		aPS.setInt(i++, pTransTypeTo);
		aPS.setString(i++, pAccNumber);
		aPS.setString(i++, pEffectiveDate);			
		if(pType.equals("W")){
			aPS.setString(i++, pEffectiveDate);
			aPS.setString(i++, pEffectiveDate);
			aPS.setString(i++, pEffectiveDate);
		}else if (pType.equals("M") || pType.equals("Y")){
			aPS.setString(i++, pEffectiveDate);
		}
		if(isTransfer){
			aPS.setString(i++, pAccNumber);
			aPS.setString(i++, pAccNumber);
			aPS.setString(i++, pEffectiveDate);
			if(pType.equals("W")){
				aPS.setString(i++, pEffectiveDate);
				aPS.setString(i++, pEffectiveDate);
				aPS.setString(i++, pEffectiveDate);
			}else if (pType.equals("M") || pType.equals("Y")){
				aPS.setString(i++, pEffectiveDate);
			}
		}
	}
	
	public TransAmtLimitData getTransCountNAmountPerWeek(String pAccNumber, String pEffectiveDate, int pTransTypefrom, int pTransTypeTo, Connection conn) throws SQLException{
		TransAmtLimitData l_Data = new TransAmtLimitData();
		int l_Count = 0; double l_Amt = 0.00;
		String l_AmtSubQuery = "";
		boolean isCashDept = false,isCashWith = false,isTransfer= false;
		if(pTransTypefrom>=0 && pTransTypeTo<=199)
			isCashDept = true;
		else if(pTransTypefrom>=500 && pTransTypeTo<=699)
			isCashWith = true;
		else if((pTransTypefrom>200 && pTransTypeTo<=499) || (pTransTypefrom>700 && pTransTypeTo<=999)){
			isTransfer = true;
		}
		if(isCashDept || isCashWith){
			l_AmtSubQuery = "SUM(CASE WHEN TransType=705 THEN ISNULL(IBTAmt,0) ELSE TrAmount END)";
		}else {
			l_AmtSubQuery = "SUM(TrAmount)";
		}
		
		String l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransaction A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo " ;
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? and TransDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?),CAST(? AS DATE))  AND " +
					"TransDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE))) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? and TransDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?),CAST(? AS DATE))  " +
					"AND TransDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE))) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?)," +
				" CAST(? AS DATE))  AND EffectiveDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE)) ";
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) and TransDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?),CAST(? AS DATE))  " +
					"AND TransDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE)))" ;
		}
		PreparedStatement l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "W",isCashDept,isCashWith,isTransfer,l_pstmt);
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		ResultSet rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		
		l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransactionOld A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo " ;
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? and TransDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?),CAST(? AS DATE))  AND " +
					"TransDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE))) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? and TransDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?),CAST(? AS DATE))  " +
					"AND TransDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE))) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?)," +
				" CAST(? AS DATE))  AND EffectiveDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE)) ";
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) and TransDate>=DATEADD(DAY, 2 - DATEPART(WEEKDAY, ?),CAST(? AS DATE))  " +
					"AND TransDate<=DATEADD(DAY, 8 - DATEPART(WEEKDAY, ?), CAST(? AS DATE)))" ;
		}
		l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "W",isCashDept,isCashWith,isTransfer,l_pstmt);
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		l_pstmt.close();
		l_Data = new TransAmtLimitData();
		l_Data.setTransAmount(l_Amt);
		l_Data.setTransCount(l_Count);
		return l_Data;
	}
	
	public TransAmtLimitData getTransCountNAmountPerMonth(String pAccNumber, String pEffectiveDate, int pTransTypefrom, int pTransTypeTo, Connection conn) throws SQLException{
		TransAmtLimitData l_Data = new TransAmtLimitData();
		int l_Count = 0; double l_Amt = 0.00;
		String l_AmtSubQuery = "";
		boolean isCashDept = false,isCashWith = false,isTransfer= false;
		if(pTransTypefrom>=0 && pTransTypeTo<=199)
			isCashDept = true;
		else if(pTransTypefrom>=500 && pTransTypeTo<=699)
			isCashWith = true;
		else if((pTransTypefrom>200 && pTransTypeTo<=499) || (pTransTypefrom>700 && pTransTypeTo<=999)){
			isTransfer = true;
		}
		if(isCashDept || isCashWith){
			l_AmtSubQuery = "SUM(CASE WHEN TransType=705 THEN ISNULL(IBTAmt,0) ELSE TrAmount END)";
		}else {
			l_AmtSubQuery = "SUM(TrAmount)";
		}
		
		String l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransaction A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo " ;
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? AND TransDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) AND TransDate<=EOMONTH(?)) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? AND TransDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) AND TransDate<=EOMONTH(?)) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) " +
				"AND EffectiveDate<=EOMONTH(?)";
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) AND TransDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) AND TransDate<=EOMONTH(?) )" ;
		}
		
		PreparedStatement l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "M",isCashDept,isCashWith, isTransfer, l_pstmt);
		
		l_pstmt.setQueryTimeout(DAOManager.getConnectionTime());
		ResultSet rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		
		l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransactionOld A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo " ;
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? AND TransDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) AND TransDate<=EOMONTH(?)) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? AND TransDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) AND TransDate<=EOMONTH(?)) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) " +
				"AND EffectiveDate<=EOMONTH(?)";
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) AND TransDate>=DATEADD(DAY,1,EOMONTH(DATEADD(MONTH,-1,?))) AND TransDate<=EOMONTH(?) )" ;
		}
		l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "M",isCashDept,isCashWith,isTransfer, l_pstmt);
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}			
		l_Data = new TransAmtLimitData();
		l_Data.setTransAmount(l_Amt);
		l_Data.setTransCount(l_Count);
		return l_Data;
	}
	
	public TransAmtLimitData getTransCountNAmountPerYear(String pAccNumber, String pEffectiveDate, int pTransTypefrom, int pTransTypeTo, Connection conn) throws SQLException{
		TransAmtLimitData l_Data = new TransAmtLimitData();
		int l_Count = 0; double l_Amt = 0.00;
		String l_AmtSubQuery = "";
		boolean isCashDept = false,isCashWith=false,isTransfer= false;
		if(pTransTypefrom>=0 && pTransTypeTo<=199)
			isCashDept = true;
		else if(pTransTypefrom>=500 && pTransTypeTo<=699)
			isCashWith = true;
		else if((pTransTypefrom>200 && pTransTypeTo<=499) || (pTransTypefrom>700 && pTransTypeTo<=999)){
			isTransfer = true;
		}
		if(isCashDept || isCashWith){
			l_AmtSubQuery = "SUM(CASE WHEN TransType=705 THEN ISNULL(IBTAmt,0) ELSE TrAmount END)";
		}else {
			l_AmtSubQuery = "SUM(TrAmount)";
		}
		String l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransaction A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo ";
				
		
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? AND TransDate>=CONCAT(YEAR(?),'-01-01') AND TransDate<=CONCAT(YEAR(?),'-12-31')) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? AND TransDate>=CONCAT(YEAR(?),'-01-01') AND TransDate<=CONCAT(YEAR(?),'-12-31')) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate>=CONCAT(YEAR(?),'-01-01') " +
				"AND EffectiveDate<=CONCAT(YEAR(?),'-12-31')";
		
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) AND TransDate>=CONCAT(YEAR(?),'-01-01') AND TransDate<=CONCAT(YEAR(?),'-12-31'))" ;
		}
		
		PreparedStatement l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "Y", isCashDept, isCashWith, isTransfer, l_pstmt);
		
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		ResultSet rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		
		l_Query = "SELECT "+l_AmtSubQuery+",Count(A.TransNo) FROM AccountTransactionOld A INNER JOIN AccountGLTransaction B ON A.Transno=B.TransNo ";
					
		if(isCashWith){
			l_Query+="LEFT JOIN (SELECT DrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=0 and " +
					"status IN (2,3) and DrAccNumber=? AND TransDate>=CONCAT(YEAR(?),'-01-01') AND TransDate<=CONCAT(YEAR(?),'-12-31')) C ON C.DrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}else if(isCashDept){
			l_Query+="LEFT JOIN (SELECT CrAccNumber,Transref,(Amount) IBTAmt ,(OnlineSerialNo) IBTCount FROM CurrentIBTOut Where TransType=2 " +
					"and status IN (2,3) and CrAccNumber=? AND TransDate>=CONCAT(YEAR(?),'-01-01') AND TransDate<=CONCAT(YEAR(?),'-12-31')) C ON C.CrAccNumber =A.AccNumber AND C.Transref=A.Transref";
		}
		l_Query+= " WHERE Status<254 AND ((TransType BETWEEN ? AND ? )" ;					
		if(isCashWith)
			l_Query+= " OR (TransType=705)";
		else if(isCashDept)
			l_Query+= " OR (TransType=205)";
		l_Query+= ") AND A.AccNumber=? AND EffectiveDate>=CONCAT(YEAR(?),'-01-01') " +
				"AND EffectiveDate<=CONCAT(YEAR(?),'-12-31')";
		
		if(isTransfer){
			l_Query+=" AND Transref NOT IN (SELECT Transref FROM CurrentIBTOUT Where (TransType=0 OR TransType=2) AND " +
					"status IN (2,3) and (DrAccNumber=? OR CrAccNumber=?) AND TransDate>=CONCAT(YEAR(?),'-01-01') AND TransDate<=CONCAT(YEAR(?),'-12-31'))" ;
		}
		l_pstmt  = conn.prepareStatement(l_Query);
		updateRecordLimit(pAccNumber, pEffectiveDate, pTransTypefrom, pTransTypeTo, "Y", isCashDept, isCashWith, isTransfer, l_pstmt);
		l_pstmt.setQueryTimeout(DAOManager.getTransTime());
		rs = l_pstmt.executeQuery();
		while (rs.next()){				
			l_Amt += rs.getDouble(1);
			l_Count += rs.getInt(2);
		}
		
		l_Data = new TransAmtLimitData();
		l_Data.setTransAmount(l_Amt);
		l_Data.setTransCount(l_Count);
		return l_Data;
	}
	
	public DataResult addAccTransaction(TransactionData pAccTransData,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
        
		DataResult result= new DataResult();
		String sql = "INSERT INTO AccountTransaction VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedstatement	= conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
        updateRecord(pAccTransData, preparedstatement);	
        preparedstatement.setQueryTimeout(DAOManager.getTransTime());
        if(preparedstatement.executeUpdate()>0){
        	result.setStatus(true);
        	ResultSet rs = preparedstatement.getGeneratedKeys();
     		if (rs != null && rs.next()) {
     			result.setTransactionNumber(rs.getInt(1));
     		}else
     			result.setStatus(false);
     		rs.close();
        }
        preparedstatement.close();		
		
        return result;

	}	
	
	private void updateRecord(TransactionData pTransData,PreparedStatement aPS) throws SQLException
	{
		
		aPS.setString(1, pTransData.getBranchCode()==null ? "001" : pTransData.getBranchCode() );
		aPS.setString(2, pTransData.getWorkstation()==null ? "001" : pTransData.getWorkstation());
		aPS.setLong(3, pTransData.getTransactionReference());
		aPS.setInt(4, pTransData.getSerial());
		aPS.setString(5, pTransData.getUserID());
		aPS.setString(6, pTransData.getAuthorizerID());
		aPS.setString(7, pTransData.getTransactionTime()==null ? "12:00:00" : pTransData.getTransactionTime());
		aPS.setString(8, pTransData.getTransactionDate()==null ? SharedUtil.getCurrentDate() : pTransData.getTransactionDate());
		aPS.setString(9, pTransData.getDescription()==null ? "" : pTransData.getDescription());
		aPS.setString(10, pTransData.getReferenceNumber()==null ? "" : pTransData.getReferenceNumber());
		aPS.setString(11, pTransData.getCurrencyCode()==null ? "MMK" :  pTransData.getCurrencyCode());
		aPS.setDouble(12, pTransData.getCurrencyRate()==0 ? 1 : pTransData.getCurrencyRate());
		aPS.setDouble(13, pTransData.getAmount());
		aPS.setInt(14, pTransData.getTransactionType());
		aPS.setString(15, pTransData.getAccountNumber());
		aPS.setDouble(16, pTransData.getPreviousBalance());
		aPS.setString(17, pTransData.getPreviousDate()==null ? SharedUtil.getCurrentDate() :pTransData.getPreviousDate());
		aPS.setString(18, pTransData.getEffectiveDate()==null ? SharedUtil.getCurrentDate():pTransData.getEffectiveDate());
		aPS.setString(19, pTransData.getContraDate()==null ? "01/01/1900":  pTransData.getContraDate() );
		aPS.setInt(20, pTransData.getStatus());
		aPS.setString(21, pTransData.getAccRef()==null ? "": pTransData.getAccRef());
		aPS.setString(22, pTransData.getRemark()==null ? "":pTransData.getRemark());
		aPS.setInt(23, pTransData.getSystemCode());
		aPS.setString(24, pTransData.getSubRef()==null ? "" : pTransData.getSubRef());	
		
	}
	
	public boolean updateTransRef(int ltobefltRef,int ltobeupdatedRef,Connection conn) throws SQLException{
		
		boolean ret = false;
		PreparedStatement pstmt=null;
		
			pstmt = conn.prepareStatement("UPDATE AccountTransaction SET " +
					"TransRef =? " +
					" WHERE TransRef=?");			
			
			pstmt.setInt(1,ltobeupdatedRef);
			pstmt.setInt(2,ltobefltRef);
			pstmt.setQueryTimeout(DAOManager.getTransTime());
            
			if(pstmt.executeUpdate()>0){
	        	ret = true;
	        }
            pstmt.close();
		
		return ret;
	}
	
	public boolean addAutoLinkTransRef(int pTransRef, int pAutoLinkTransRef, Connection conn) throws SQLException{	        
		boolean result= false;
		String sql = "INSERT INTO TransactionExtend([TransRef],[AutoLinkTransRef],[Status]) VALUES(?,?,?)";
		PreparedStatement pstmt	= conn.prepareStatement(sql);
		pstmt.setInt(1, pTransRef);
		pstmt.setInt(2, pAutoLinkTransRef);
		pstmt.setInt(3, 1);
		
        if(pstmt.executeUpdate()>0){
        	result=true;
        }
        pstmt.close();					
        return result;
	}
	
	public boolean addAutoLinkTransRefUpdate(ArrayList<Integer> autoLinkTransref, int transRef, Connection conn) throws SQLException{	        
		boolean result= false;
		String sql = "INSERT INTO TransactionExtend([TransRef],[AutoLinkTransRef],[Status]) VALUES (?,?,?) ";
		if(autoLinkTransref.size() > 1) {
			sql += ", (?,?,?)";
		}
		
		PreparedStatement pstmt	= conn.prepareStatement(sql);
		int index = 1;
		for (int i = 0; i < autoLinkTransref.size(); i++) {			
			index = updateRecordAutoLink(autoLinkTransref.get(i), transRef, index, pstmt);
		}

        if(pstmt.executeUpdate()>0){
        	result=true;
        }
        pstmt.close();					
        return result;
	}
	
	private int updateRecordAutoLink(int transref, int autolinktransref, int index, PreparedStatement aPS)
			throws SQLException {
		aPS.setInt(index++, transref);
		aPS.setInt(index++, autolinktransref);
		aPS.setInt(index++, 1);
		return index;
	}
	
	public boolean getAccTransactionsTransRef(int pTransRef, Connection conn) throws SQLException{
		boolean result = false;
		
		String l_Query = "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,TransDate,Description,ChequeNo,CurrencyCode, " +
				 		"CurrencyRate,Amount,TransType,AccNumber,PrevBalance,PrevUpdate,EffectiveDate,ContraDate, " +
				 		"Status,CAST (AccRef AS INT),remark,SystemCode,SubRef FROM AccountTransactionOld Where TransRef = ? AND Status <254 Order By TransNo";
		
		String l_Query2 = "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,TransDate,Description,ChequeNo,CurrencyCode,CurrencyRate, " +
				 "Amount,TransType,AccNumber,PrevBalance,PrevUpdate,EffectiveDate,ContraDate,Status,AccRef,remark,SystemCode,SubRef " + 
				 "FROM AccountTransaction Where TransRef = ? AND Status <254 Order By TransNo";
		
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		pstmt.setInt(1, pTransRef);
		int i=0;
		
		ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	    	TransactionData l_TransData = new TransactionData();
	        readRecord(l_TransData, rs);
	        lstTransData.add(i++, l_TransData);
	        result = true;
	    }

    	pstmt = conn.prepareStatement(l_Query2);
		pstmt.setInt(1, pTransRef);
		
		rs = pstmt.executeQuery();
	    while(rs.next()){
	    	TransactionData l_TransData = new TransactionData();
	        readRecord(l_TransData, rs);
	        lstTransData.add(i++, l_TransData);
	        result = true;
	    }
	    
	    pstmt.close();		  
		rs.close();
		return result;
	}
	
	private static void readRecord(TransactionData pTransData,ResultSet aRS) throws SQLException
	{
		
		pTransData.setTransactionNumber(aRS.getInt("TransNo"));
		pTransData.setBranchCode(aRS.getString("BranchCode"));
		pTransData.setWorkstation(aRS.getString("WorkStation"));
		pTransData.setTransactionReference(aRS.getLong("TransRef"));
		pTransData.setSerial(aRS.getInt("SerialNo"));
		pTransData.setUserID(aRS.getString("TellerId"));
		pTransData.setAuthorizerID(aRS.getString("SupervisorId"));
		pTransData.setTransactionTime(SharedUtil.formatDBDateTime2MIT(aRS.getString("TransTime")));
		pTransData.setTransactionDate(SharedUtil.formatDBDate2MIT(aRS.getString("TransDate")));
		pTransData.setDescription(aRS.getString("Description"));
		pTransData.setReferenceNumber(aRS.getString("ChequeNo"));
		pTransData.setCurrencyCode(aRS.getString("CurrencyCode"));
		pTransData.setCurrencyRate(aRS.getDouble("CurrencyRate"));
		pTransData.setAmount(aRS.getDouble("Amount"));
		pTransData.setTransactionType(aRS.getInt("TransType"));
		pTransData.setAccountNumber(aRS.getString("AccNumber"));
		pTransData.setPreviousBalance(aRS.getDouble("PrevBalance"));
		pTransData.setPreviousDate(SharedUtil.formatDBDate2MIT(aRS.getString("PrevUpdate")));
		pTransData.setEffectiveDate(SharedUtil.formatDBDate2MIT(aRS.getString("EffectiveDate")));
		pTransData.setContraDate(SharedUtil.formatDBDate2MIT(aRS.getString("ContraDate")));
		pTransData.setStatus(aRS.getInt("Status"));
		pTransData.setAccRef(aRS.getString("AccRef"));
		pTransData.setRemark(aRS.getString("remark"));
		pTransData.setSystemCode(aRS.getInt("SystemCode"));
		pTransData.setSubRef(aRS.getString("SubRef"));
		
	}
	
	public DataResult addAccTransaction(String tmp_Transpreapre,int lTranRef,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
        
		DataResult result= new DataResult(); 
		int index = 0;
		String sql = "INSERT INTO AccountTransaction SELECT brcode,workstation,"+lTranRef+",0,userid,authid,transtime,transdate,[desc],chequeno,"
				+ "currencyCode,currencyrate,amount,transtype,account,prevbalance,"+SharedUtil.getCurrentDate()+",effdate,contradate,status,"
				+ "'',remark,0,'' FROM "+tmp_Transpreapre;
		PreparedStatement preparedstatement	= conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedstatement.setQueryTimeout(DAOManager.getTransTime());
        index = preparedstatement.executeUpdate();
        if(index >0 ){
        	ResultSet rs = preparedstatement.getGeneratedKeys();
     		if ( rs.next()) {
     			//System.out.println("this is transref:: "+rs.getLong(1));
     			result.setTransactionNumber(rs.getInt(1)-(index-1));
     			result.setStatus(true);
     		}
     		rs.close();
        }
        preparedstatement.close();		
		
        if(!result.getStatus()) result.setDescription("Please Try Again!!");
        
        return result;

	}	
	
	public boolean getAccTransactionsTransRef(String tmp_GlTranspreapre,int pTransRef, Connection conn) throws SQLException{
		boolean result = false;
		String l_Query = "UPDATE "+tmp_GlTranspreapre+" SET TransNo="+pTransRef+"";
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
        pstmt.executeUpdate();
        
		l_Query = "UPDATE A SET A.TransNo = B.TransNo FROM "+tmp_GlTranspreapre+" A INNER JOIN AccountTransactionOld B ON A.TransNo = B.TransRef AND A.AccNumber=B.AccNumber AND  B.Status <254 ";
		pstmt = conn.prepareStatement(l_Query);
        if(pstmt.executeUpdate()>0){
        	 result = true;
        }
        l_Query = "UPDATE A SET A.TransNo = B.TransNo FROM "+tmp_GlTranspreapre+" A INNER JOIN AccountTransaction B ON A.TransNo = B.TransRef AND A.AccNumber=B.AccNumber AND Status <254 ";
		
        pstmt = conn.prepareStatement(l_Query);
        if(pstmt.executeUpdate()>0){
        	 result = true;
        }
	    pstmt.close();		 
		return result;
	}

	public boolean updateAutoLinkTransRef(int ltobefltRef,int ltobeupdatedRef,Connection conn) throws SQLException{
		
		boolean ret = false;
		PreparedStatement pstmt=null;
		
			pstmt = conn.prepareStatement("UPDATE TransactionExtend SET " +
					"TransRef =? " +
					" WHERE TransRef=?");			
			
			pstmt.setInt(1,ltobeupdatedRef);
			pstmt.setInt(2,ltobefltRef);
			pstmt.setQueryTimeout(DAOManager.getTransTime());
            
			if(pstmt.executeUpdate()>0){
	        	ret = true;
	        }
            pstmt.close();
		
		return ret;
	}
	
	public int getTotalCountofSADrTransactionOfWeekNew(String pAccNo, String pFromDate, String pToDate, int pCount,
			Connection pConn) throws SQLException {
		int ret = 0;
		String l_Query = "SELECT COUNT(*) AS cnt FROM AccountTransactionOld AS AT WHERE AccNumber = ?"
				+ " AND EffectiveDate>=? AND EffectiveDate<=? AND TransType >= 500 AND Status < 254 ";
		PreparedStatement l_pstmt = pConn.prepareStatement(l_Query);
		l_pstmt.setString(1, pAccNo);
		l_pstmt.setString(2, pFromDate);
		l_pstmt.setString(3, pToDate);
		ResultSet l_RS = l_pstmt.executeQuery();

		while (l_RS.next()) {
			ret = l_RS.getInt("cnt");
		}

		l_Query = "SELECT COUNT(*) AS cnt FROM  AccountTransaction AS AT WHERE AccNumber = ?"
				+ " AND EffectiveDate>=? AND EffectiveDate<=? AND TransType >= 500 AND Status < 254 ";
		l_pstmt = pConn.prepareStatement(l_Query);
		l_pstmt.setString(1, pAccNo);
		l_pstmt.setString(2, pFromDate);
		l_pstmt.setString(3, pToDate);
		l_RS = l_pstmt.executeQuery();

		while (l_RS.next()) {
			ret += l_RS.getInt("cnt");
		}

		l_pstmt.close();
		return ret;
	}
	
	public TransactionData getTotalCountofSADrTransactionOfWeek(String pAccNo, String pFromDate, String pToDate,
			int pCount, Connection pConn) throws SQLException {
		TransactionData l_TranData = new TransactionData();
		int transCount = 0;
		String l_Query = "";
		PreparedStatement l_pstmt = null;
		ResultSet l_RS = null;

		l_Query = "SELECT COUNT(*) AS cnt,AT.AccNumber, AT.PrevUpdate FROM AccountTransaction AT "
				+ " WHERE AccNumber = ? AND EffectiveDate>=? AND EffectiveDate<=? AND TransType >= 500  AND TransType <= 699 AND "
				+ " Status < 254 GROUP BY AccNumber,PrevUpdate ";
		l_pstmt = pConn.prepareStatement(l_Query);
		l_pstmt.setString(1, pAccNo);
		l_pstmt.setString(2, pFromDate);
		l_pstmt.setString(3, pToDate);
		l_RS = l_pstmt.executeQuery();

		while (l_RS.next()) {
			transCount = l_RS.getInt("cnt");
			if (transCount >= pCount) {
				l_TranData.setPreviousDate(SharedUtil.formatDBDate2MIT(l_RS.getString("PrevUpdate")));
				l_TranData.setAccountNumber(l_RS.getString("AccNumber"));
			}
		}
		l_Query = l_Query.replace("AccountTransaction", "AccountTransactionOld");
		l_pstmt.clearBatch();
		l_pstmt.clearParameters();

		l_pstmt = pConn.prepareStatement(l_Query);
		l_pstmt.setString(1, pAccNo);
		l_pstmt.setString(2, pFromDate);
		l_pstmt.setString(3, pToDate);
		l_RS = l_pstmt.executeQuery();

		while (l_RS.next()) {
			transCount = l_RS.getInt("cnt");
			if (transCount >= pCount) {
				l_TranData.setPreviousDate(SharedUtil.formatDBDate2MIT(l_RS.getString("PrevUpdate")));
				l_TranData.setAccountNumber(l_RS.getString("AccNumber"));
			}
		}

		l_pstmt.close();
		return l_TranData;
	}
	
	public int getSlipNo(String branchCode, String slipNo, String modifiedDate) throws SQLException {
		int ret = 0;
		DAOManager l_DAOManager = new DAOManager();
		Connection conn = l_DAOManager.CreateConnection();

		CallableStatement l_Call = null;
		String l_Query = "{Call SP_GenerateSlipNo(?,?,?)}";
		l_Call = conn.prepareCall(l_Query);

		l_Call.setString(1, branchCode);
		l_Call.setString(2, slipNo);
		l_Call.setString(3, modifiedDate);

		ResultSet rs = l_Call.executeQuery();
		while (rs.next()) {
			ret = rs.getInt("NewSlipNo");
		}
		rs.close();
		conn.close();
		return ret;
	}
	
	public boolean getAccTransactionsTransRefNew(int pTransRef, Connection conn) throws SQLException{
		boolean ret = false;
		if(getAccTransactionsTransRefReversal(pTransRef, conn)){
			ret =true;
		}else{
			if(getAccTransactionsTransRefReversalOld(pTransRef, conn)){
				ret =true;
			}
		}
		return ret;
	}
	
	public boolean getAccTransactionsTransRefReversal(int pTransRef, Connection conn) throws SQLException{
		boolean result = false;
			String l_Query = "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,TransDate,Description,ChequeNo,CurrencyCode,CurrencyRate, " +
							 "Amount,TransType,AccNumber,PrevBalance,PrevUpdate,EffectiveDate,ContraDate,Status,AccRef,remark,SystemCode,SubRef " + 
							 "FROM AccountTransaction  WHERE 1=1 " +
							 "AND TransRef = ? AND Status <254 Order By TransNo";
			
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setInt(1, pTransRef);
			int i=0;
			pstmt.setQueryTimeout(DAOManager.getTransTime());
			ResultSet rs = pstmt.executeQuery();
		    while(rs.next()){
		    	TransactionData l_TransData = new TransactionData();
		        readRecord(l_TransData, rs);
		        lstTransData.add(i++, l_TransData);
		        result = true;
		    }
		    pstmt.close();				
		 return result;
	}
	
	public boolean getAccTransactionsTransRefReversalOld(int pTransRef, Connection conn) throws SQLException{
		boolean result = false;
		String l_Query = "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,TransDate,Description,ChequeNo,CurrencyCode,CurrencyRate, " +
						 "Amount,TransType,AccNumber,PrevBalance,PrevUpdate,EffectiveDate,ContraDate,Status,AccRef,remark,SystemCode,SubRef " + 
						 "FROM AccountTransactionOld  WHERE 1=1 " +
						 "AND TransRef = ? AND Status <254 Order By TransNo";
	
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		pstmt.setInt(1, pTransRef);
		int i=0;
		pstmt.setQueryTimeout(DAOManager.getTransTime());
		ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	    	TransactionData l_TransData = new TransactionData();
	        readRecord(l_TransData, rs);
	        lstTransData.add(i++, l_TransData);
	        result = true;
	    }
	    pstmt.close();				
		 return result;
	}
	
	public boolean updateAccTransaction(TransactionData pTransData,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException{
   		boolean result=false;
           PreparedStatement pstmt=null;
			try {
				pstmt = conn.prepareStatement("UPDATE AccountTransaction SET BranchCode=?,WorkStation=?,TransRef=?," +
						"SerialNo =?,TellerId=?,SupervisorId=?,TransTime=?,TransDate=?,Description=?,ChequeNo=?,CurrencyCode=?," +
						"CurrencyRate=?,Amount=?,TransType=?,AccNumber=?,PrevBalance=?,PrevUpdate=?,EffectiveDate=?,ContraDate=?," +
						" Status=?,AccRef=?,remark=?,SystemCode=?,SubRef=? WHERE TransNo=?");
				
				updateRecord(pTransData, pstmt);
	            pstmt.setLong(25, pTransData.getTransactionNumber());
	            pstmt.setQueryTimeout(DAOManager.getTransTime());
                pstmt.executeUpdate();
                pstmt.close();
                result=true;
			} catch (SQLException e) {
				
				e.printStackTrace();
				result=false;
			}
           return result;
}
	public boolean updateAccTransactionOld(TransactionData pTransData,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException{
   		boolean result=false;
           PreparedStatement pstmt=null;
			try {
				pstmt = conn.prepareStatement("UPDATE AccountTransactionOld SET BranchCode=?,WorkStation=?,TransRef=?," +
						"SerialNo =?,TellerId=?,SupervisorId=?,TransTime=?,TransDate=?,Description=?,ChequeNo=?,CurrencyCode=?," +
						"CurrencyRate=?,Amount=?,TransType=?,AccNumber=?,PrevBalance=?,PrevUpdate=?,EffectiveDate=?,ContraDate=?," +
						" Status=?,AccRef=?,remark=?,SystemCode=?,SubRef=? WHERE TransNo=?");
				
				updateRecord(pTransData, pstmt);
	            pstmt.setLong(25, pTransData.getTransactionNumber());
	            pstmt.setQueryTimeout(DAOManager.getTransTime());
                pstmt.executeUpdate();
                pstmt.close();
                result=true;
			} catch (SQLException e) {
				
				e.printStackTrace();
				result=false;
			}
           return result;
	}
	
	public DataResult checkLoanDebitBalance(int pTransRef, Connection conn) throws SQLException{
		DataResult ret = new DataResult();
		double TBalance, DrLimit = 0;
	
		try {
			String l_Query = "SET DATEFORMAT DMY SELECT [AT].AccNumber, [AT].Amount, [AT].TransType, [AT].TransDate, " +
							 "[AT].Status, [AT].TransRef, [AT].TransNo, A.CurrentBalance, " + 
							 "A.CurrentBalance - [AGL].TRAMOUNT TBalance, " +
							 "ISNULL(Od.TotOdLimit, 0) DrLimit " + 
							 "FROM AccountTransaction [AT] INNER JOIN " + 
							 "AccountGLTransaction AGL ON AT.TRANSNO = AGL.TRANSNO " + 
							 "INNER JOIN Accounts A " + 
							 "ON [AT].AccNumber = A.AccNumber LEFT OUTER JOIN " + 
							 "(SELECT AccNumber, SUM(SanctionAmountBr) TotOdLimit " + 
							 "FROM LAODF WHERE Status < 2 GROUP BY AccNumber) Od " +
							 "ON [AT].AccNumber = Od.AccNumber " +
							 "WHERE [AT].TransRef = ? AND " +
							 "[AT].TransType < 500 ";
			
			
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setInt(1, pTransRef);
			ResultSet rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			// Checking in AccountTransaction Table
			while(rs.next()){
				TBalance = rs.getDouble("TBalance");
				DrLimit = rs.getDouble("DrLimit");
				
				// Checking Dr Limt
				if (TBalance < 0 && Math.abs(TBalance) > DrLimit){
					ret.setStatus(false);
					ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
				} else {
					ret.setStatus(true);
				}
			}
			
			// Checking in AccountTransactionOld
			l_Query = l_Query.replace("AccountTransaction", "AccountTransactionOld");
			pstmt.clearBatch();
			pstmt.clearParameters();
				
			pstmt = conn.prepareStatement(l_Query);
			pstmt.setLong(1, pTransRef);
			rs = pstmt.executeQuery();
				
			// Checking in AccountTransaction Table
			while(rs.next()){
				TBalance = rs.getDouble("TBalance");
				DrLimit = rs.getDouble("DrLimit");
				// Checking Dr Limt
				if (TBalance < 0 && TBalance > DrLimit){
					ret.setStatus(false);
					ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
				} else {
					ret.setStatus(true);
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {			
			ret.setStatus(false);
			e.printStackTrace();
		}
		return ret;
	}
	
	public DataResult checkDebitBalanceFD(int pTransRef, int pTransNo, Connection conn) throws SQLException{
		DataResult ret = new DataResult();
		double TBalance, DrLimit =0;
	
		try {			
			String l_Query = "SET DATEFORMAT DMY SELECT AT.AccNumber,A.CurrentBalance - [AT].TRAMOUNT TBalance, ISNULL(Od.TotOdLimit, 0) DrLimit, AT.STATUS "
							+ "FROM (SELECT AT.AccNumber,AT.TransNo,SUM(AT.Amount) Amount, SUM(CASE WHEN TransType>500 THEN TRAMOUNT*-1 ELSE TRAMOUNT END) As TRAMOUNT, PB.Status "
							+ "FROM AccountTransaction [AT] INNER JOIN AccountGLTransaction AGL ON AT.TRANSNO = AGL.TRANSNO" +
							" INNER JOIN PBFixedDepositAccount PB ON PB.AccNumber = AT.AccNumber AND AT.ChequeNo = PB.RefNo" +
							" Where AT.TransRef=? GROUP BY AT.AccNumber,AT.TransNo, PB.Status ) AT "
							+ " INNER JOIN Accounts A ON [AT].AccNumber = A.AccNumber LEFT OUTER JOIN (SELECT AccNumber, SUM(SanctionAmountBr) TotOdLimit FROM "
							+ "AODF WHERE Status < 2 GROUP BY AccNumber) Od ON [AT].AccNumber = Od.AccNumber WHERE [AT].TransNo = ?";			
			
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setInt(1, pTransRef);
			pstmt.setInt(2, pTransNo);
			ResultSet rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			// Checking in AccountTransaction Table
			while(rs.next()){
				TBalance = rs.getDouble("TBalance");
				DrLimit = rs.getDouble("DrLimit");
								
				// Checking Dr Limt
				
				if (rs.getInt("Status") == 9) {
					if (Math.abs(TBalance) >= DrLimit){				
						ret.setStatus(true);
					} else {
						ret.setStatus(false);
						ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
					}
				} else {
					if (TBalance < 0 && Math.abs(TBalance) > DrLimit){				
						
						ret.setStatus(false);
						ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
					} else {
						ret.setStatus(true);
					}
				}
			}
			
			// Checking in AccountTransactionOld
			l_Query = l_Query.replace("AccountTransaction", "AccountTransactionOld");
			pstmt.clearBatch();
			pstmt.clearParameters();
				
			pstmt = conn.prepareStatement(l_Query);
			pstmt.setLong(1, pTransRef);
			pstmt.setInt(2, pTransNo);
			rs = pstmt.executeQuery();
				
			// Checking in AccountTransaction Table
			while(rs.next()){
				TBalance = rs.getDouble("TBalance");
				DrLimit = rs.getDouble("DrLimit");
				// Checking Dr Limt
				if (rs.getInt("Status") == 9) {
					if (Math.abs(TBalance) >= DrLimit){				
						ret.setStatus(true);
					} else {
						ret.setStatus(false);
						ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
					}
				} else {
					if (TBalance < 0 && Math.abs(TBalance) > DrLimit){				
						
						ret.setStatus(false);
						ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
					} else {
						ret.setStatus(true);
					}
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {			
			ret.setStatus(false);
			e.printStackTrace();
		}
		return ret;
	}
	
	public DataResult checkDebitBalance(int pTransRef, int pTransNo, Connection conn) throws SQLException{
		DataResult ret = new DataResult();
		double TBalance, DrLimit =0;
		String l_Query = "";
		int l_HPRegisterStyle = SharedLogic.getSystemData().getpSystemSettingDataList().get("HPExt").getN4();
		
	
		try {
			/*String l_Query = "SET DATEFORMAT DMY SELECT [AT].AccNumber, [AT].Amount, [AT].TransType, [AT].TransDate, " +
							 "[AT].Status, [AT].TransRef, [AT].TransNo, A.CurrentBalance, " + 
							 "A.CurrentBalance - [AGL].TRAMOUNT TBalance, " +
							 "ISNULL(Od.TotOdLimit, 0) DrLimit " + 
							 "FROM AccountTransaction [AT] INNER JOIN " + 
							 "AccountGLTransaction AGL ON AT.TRANSNO = AGL.TRANSNO " + 
							 "INNER JOIN Accounts A " + 
							 "ON [AT].AccNumber = A.AccNumber LEFT OUTER JOIN " + 
							 "(SELECT AccNumber, SUM(SanctionAmountBr) TotOdLimit " + 
							 "FROM AODF WHERE Status < 2 GROUP BY AccNumber) Od " +
							 "ON [AT].AccNumber = Od.AccNumber " +
							 "WHERE [AT].TransNo = ? AND " +
							 "[AT].TransType < 500 ";*/
			if(l_HPRegisterStyle==1){
				l_Query = "SET DATEFORMAT DMY SELECT AT.AccNumber,A.CurrentBalance - [AT].TRAMOUNT TBalance, ISNULL(Od.TotOdLimit, 0) DrLimit, ISNULL(HP.HPAmt, 0) HPAmt "
						+ "FROM (SELECT AT.AccNumber,AT.TransNo,SUM(Amount) Amount, SUM(CASE WHEN TransType>500 THEN TRAMOUNT*-1 ELSE TRAMOUNT END) As TRAMOUNT "
						+ "FROM AccountTransaction [AT] INNER JOIN AccountGLTransaction AGL ON AT.TRANSNO = AGL.TRANSNO Where AT.TransRef=? GROUP BY AT.AccNumber,AT.TransNo ) AT "
						+ " INNER JOIN Accounts A ON [AT].AccNumber = A.AccNumber LEFT OUTER JOIN (SELECT AccNumber, SUM(SanctionAmountBr) TotOdLimit FROM "
						+ "AODF WHERE Status < 2 GROUP BY AccNumber) Od ON [AT].AccNumber = Od.AccNumber LEFT OUTER JOIN (SELECT HPAccount, SUM(HPAmt) HPAmt "
						+"FROM HPRegister WHERE Status <>1 GROUP BY HPAccount)HP  ON [AT].AccNumber = HP.HPAccount WHERE [AT].TransNo = ?";
			}else {
				l_Query = "SET DATEFORMAT DMY SELECT AT.AccNumber,A.CurrentBalance - [AT].TRAMOUNT TBalance, ISNULL(Od.TotOdLimit, 0) DrLimit "
						+ "FROM (SELECT AT.AccNumber,AT.TransNo,SUM(Amount) Amount, SUM(CASE WHEN TransType>500 THEN TRAMOUNT*-1 ELSE TRAMOUNT END) As TRAMOUNT "
						+ "FROM AccountTransaction [AT] INNER JOIN AccountGLTransaction AGL ON AT.TRANSNO = AGL.TRANSNO Where AT.TransRef=? GROUP BY AT.AccNumber,AT.TransNo ) AT "
						+ " INNER JOIN Accounts A ON [AT].AccNumber = A.AccNumber LEFT OUTER JOIN (SELECT AccNumber, SUM(SanctionAmountBr) TotOdLimit FROM "
						+ "AODF WHERE Status < 2 GROUP BY AccNumber) Od ON [AT].AccNumber = Od.AccNumber WHERE [AT].TransNo = ?";
			}
						
			
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setInt(1, pTransRef);
			pstmt.setInt(2, pTransNo);
			ResultSet rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			// Checking in AccountTransaction Table
			while(rs.next()){
				TBalance = rs.getDouble("TBalance");
				if(l_HPRegisterStyle==1)
					DrLimit = rs.getDouble("DrLimit")+rs.getDouble("HPAmt");
				else
					DrLimit = rs.getDouble("DrLimit");
				// Checking Dr Limt
				if (TBalance < 0 && Math.abs(TBalance) > DrLimit){				
					
					ret.setStatus(false);
					ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
				} else {
					ret.setStatus(true);
				}
			}
			
			// Checking in AccountTransactionOld
			l_Query = l_Query.replace("AccountTransaction", "AccountTransactionOld");
			pstmt.clearBatch();
			pstmt.clearParameters();
				
			pstmt = conn.prepareStatement(l_Query);
			pstmt.setLong(1, pTransRef);
			pstmt.setInt(2, pTransNo);
			rs = pstmt.executeQuery();
				
			// Checking in AccountTransaction Table
			while(rs.next()){
				TBalance = rs.getDouble("TBalance");
				DrLimit = rs.getDouble("DrLimit");
				// Checking Dr Limt
				if (TBalance < 0 && TBalance > DrLimit){
					ret.setStatus(false);
					ret.setDescription("Balance insufficient to debit, " + rs.getString("AccNumber") + ".");
				} else {
					ret.setStatus(true);
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {			
			ret.setStatus(false);
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean reversal(int pTransRef, String pTransTime, String pTransDate, Connection conn) throws SQLException{
		boolean result = false;
		try {
			String l_Query = "INSERT INTO AccountTransaction(BranchCode, WorkStation, " +
							 "TransRef, SerialNo, TellerId, SupervisorId, TransTime, " +
							 "TransDate, Description, ChequeNo, CurrencyCode, " + 
							 "CurrencyRate, Amount, TransType, AccNumber, PrevBalance, " + 
							 "PrevUpdate, EffectiveDate, ContraDate, Status,Remark,SubRef,AccRef) " + 
							 "SELECT BranchCode, WorkStation, TransRef, SerialNo, " + 
							 "TellerId, SupervisorId, ?, " +
							 "?, 'Reversal,' + Substring(Description,10,245), ChequeNo, " + 
							 "CurrencyCode, CurrencyRate, Amount, " + 
							 "TransType = Case "  +
							 "When transtype > 500 Then transtype-500 " + 
							 "When transtype < 500 Then transtype+500 End, AccNumber, " + 
							 "PrevBalance = Case " + 
							 "When Transtype > 500 Then PrevBalance - Amount " + 
							 "When Transtype < 500 Then PrevBalance + Amount End, " + 
							 "PrevUpdate, EffectiveDate, ContraDate, 255, Remark,SubRef,accref " + 
							 "From (SELECT * FROM AccountTransaction WHERE TransRef = ? " + 
							 "UNION SELECT * FROM AccountTransactionOld WHERE TransRef = ?) As AccTrans ";
			
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setString(1, pTransTime);
			pstmt.setString(2, pTransDate);
			pstmt.setInt(3, pTransRef);
			pstmt.setInt(4, pTransRef);
			
			int i = pstmt.executeUpdate();
			if (i>0)
				result = true;
			else result = false;
		} catch (Exception e) {
			
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean reversal(int pTransRef, String pTransTime, String pTransDate, String pWorkStation, String pTellerId, String pSuperVisorId, Connection conn) throws SQLException{
		boolean result = false;
		try {
			String l_Query = "INSERT INTO AccountTransaction(BranchCode, WorkStation, " +
							 "TransRef, SerialNo, TellerId, SupervisorId, TransTime, " +
							 "TransDate, Description, ChequeNo, CurrencyCode, " + 
							 "CurrencyRate, Amount, TransType, AccNumber, PrevBalance, " + 
							 "PrevUpdate, EffectiveDate, ContraDate, Status,Remark,SubRef,AccRef) " + 
							 "SELECT BranchCode, ?, TransRef, SerialNo, " + 
							 "?, ?, ?, " +
							 "?, 'Reversal,' + Substring(Description,10,245), ChequeNo, " + 
							 "CurrencyCode, CurrencyRate, Amount, " + 
							 "TransType = Case "  +
							 "When transtype > 500 Then transtype-500 " + 
							 "When transtype < 500 Then transtype+500 End, AccNumber, " + 
							 "PrevBalance = Case " + 
							 "When Transtype > 500 Then PrevBalance - Amount " + 
							 "When Transtype < 500 Then PrevBalance + Amount End, " + 
							 "PrevUpdate, EffectiveDate, ContraDate, 255, Remark,SubRef,AccRef " + 
							 "From (SELECT * FROM AccountTransaction WHERE TransRef = ? " + 
							 "UNION SELECT * FROM AccountTransactionOld WHERE TransRef = ?) As AccTrans ";
			
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setString(1, pWorkStation);
			pstmt.setString(2, pTellerId);
			pstmt.setString(3, pSuperVisorId);
			pstmt.setString(4, pTransTime);
			pstmt.setString(5, pTransDate);
			pstmt.setInt(6, pTransRef);
			pstmt.setInt(7, pTransRef);
			
			int i = pstmt.executeUpdate();
			if (i>0)
				result = true;
			else result = false;
		} catch (Exception e) {
			
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<TransactionData> getAccountTransactionListTransref(int pTransRef,Boolean pIncludeReversl,Connection conn) {
		ArrayList<TransactionData> l_AccountTransactionList=new ArrayList<TransactionData>();
		try {
			/*String l_Query = "SELECT AccTrans.* FROM (SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,TransDate,Description,ChequeNo,CurrencyCode,CurrencyRate, " +
			 "Amount,TransType,AccNumber,PrevBalance,PrevUpdate,EffectiveDate,ContraDate,Status,AccRef,remark,SystemCode,SubRef " + 
			 "FROM AccountTransaction UNION " +
			 "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,TransDate,Description,ChequeNo,CurrencyCode, " +
			 "CurrencyRate,Amount,TransType,AccNumber,PrevBalance,PrevUpdate,EffectiveDate,ContraDate, " +
			 "Status,AccRef,remark,SystemCode,SubRef FROM AccountTransactionOld) AS AccTrans WHERE 1=1" +
			 "AND AccTrans.TransRef = ?"; */
			String l_Query = "";
			l_Query = "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,"+
					"TransDate,Description,ChequeNo,CurrencyCode,CurrencyRate, Amount,TransType,AccNumber,PrevBalance,"+
					"PrevUpdate,EffectiveDate,ContraDate,Status,AccRef,remark,SystemCode,SubRef FROM AccountTransaction "+
					"WHERE 1=1 AND TransRef = ? "; 
			
			if(!pIncludeReversl) {
				l_Query+=" AND Status <254";
			}
			
			l_Query+=" ORDER BY TransNo";
			
			PreparedStatement pstmt=conn.prepareStatement(l_Query);
			pstmt.setInt(1, pTransRef);
			
			ResultSet l_RS = pstmt.executeQuery();
			while(l_RS.next()){
		    	TransactionData l_TransData = new TransactionData();
		        readRecord(l_TransData, l_RS);
		        l_AccountTransactionList.add(l_TransData);
		    }
			
			l_Query = "SELECT TransNo,BranchCode,WorkStation,TransRef,SerialNo,TellerId,SupervisorId,TransTime,"+
					"TransDate,Description,ChequeNo,CurrencyCode,CurrencyRate, Amount,TransType,AccNumber,PrevBalance,"+
					"PrevUpdate,EffectiveDate,ContraDate,Status,AccRef,remark,SystemCode,SubRef FROM AccountTransactionOld "+
					"WHERE 1=1 AND TransRef = ? "; 
			if(!pIncludeReversl) {
				l_Query+=" AND Status <254";
			}
			
			l_Query+=" ORDER BY TransNo";
			pstmt=conn.prepareStatement(l_Query);
			pstmt.setInt(1, pTransRef);
			
			l_RS = pstmt.executeQuery();
			
			while(l_RS.next()){
		    	TransactionData l_TransData = new TransactionData();
		        readRecord(l_TransData, l_RS);
		        l_AccountTransactionList.add(l_TransData);
		    }
			//}
		    
		    pstmt.close();				
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return l_AccountTransactionList;
	}
}
