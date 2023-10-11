package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.nirvasoft.rp.core.util.StrUtil;
import com.nirvasoft.rp.shared.CurrentIBTInCriteriaData;
import com.nirvasoft.rp.shared.CurrentIBTInData;
import com.nirvasoft.rp.shared.Enum.DateFilterOperator;
import com.nirvasoft.rp.shared.Enum.NumFilterOperator;
import com.nirvasoft.rp.shared.Enum.TxTFilterOperator;

public class CurrentIBTInDAO {
	
	private  ArrayList<CurrentIBTInData> lstCurrentIBTInData = new ArrayList<CurrentIBTInData>();
	CurrentIBTInData l_CurrentIBTInData = new CurrentIBTInData();
	
	String l_TableName = "CurrentIBTIN";
	
	public String getTableName() {
		return l_TableName;
	}
	
	public void setTableName(String p) {
		l_TableName = p;
	}
	
	public CurrentIBTInData getCurrentIBTInData() {
		return l_CurrentIBTInData;
	}

	public void setCurrentIBTInData(CurrentIBTInData l_CurrentIBTInData) {
		this.l_CurrentIBTInData = l_CurrentIBTInData;
	}

	public ArrayList<CurrentIBTInData> getCurrentIBTTransaction(String pBranchCode,String pTBranchCode,String pFDate,String pTDate,String pCurrency,String pTransType,String pIBTType,Connection pConn) throws SQLException{
		String l_query = "";		
		
		l_query = "SET DateFormat DMY;" +
				  "SELECT MainKey , IBTType  , TransType , TransDate ,  Authorisecode  , OnlineSerialNo , CrAccNumber , "+
				  "DrAccNumber , FromBranch  , ToBranch , Amount  , Transref , Transno ,  PreviousBal  , Status , "+  
				  "ChequeNo ,  ToName  , ToNrc , SendTime , ReceiveTime ,  FromName , FromNrc , PrintStatus , "+  
				  "SaveTime ,  PostTime , CompleteTime  , Byear , Remark , T1 , N1 , AutoKey ,  OnlSno , "+
				  "TestKey ,  Prefixed  , PfixedDate FROM " + l_TableName + " WHERE 1=1 " ; 
		
		if(!pBranchCode.equals("")){
			l_query += " AND FromBranch IN( " + pBranchCode + "," + pTBranchCode + ")";
		}
		if(!pFDate.equals("")){
			l_query += " AND ToBranch IN (" + pBranchCode + "," + pTBranchCode + ")" ;
		}
		if(!pFDate.equals("")){
			l_query += " AND TransDate >= '" + pFDate +"'" ;
		}
		if(!pTDate.equals("")){
			l_query += " AND TransDate <= '" + pTDate +"'" ;
		}
		l_query += " AND IBTType IN (" + pIBTType + ") AND TransType IN (" + pTransType + ")" ;			
	   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			CurrentIBTInData l_CurrentIBTData = new CurrentIBTInData();
			readRecord(l_CurrentIBTData,rs,"","");
			lstCurrentIBTInData.add(l_CurrentIBTData);
		}
				
		
		return lstCurrentIBTInData;
	}

	public boolean updateStatus(int pStatus, int pTransRef, Connection pConn) throws SQLException {
		boolean result = false;
		String l_Query = "UPDATE " + l_TableName + " SET STATUS = ? WHERE TRANSREF = ? ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		pstmt.setInt(1, pStatus);
		pstmt.setInt(2, pTransRef);
		
		result = pstmt.executeUpdate() > 0 ? true : false;
		
		return result;
	}
	
	public boolean checkTransRefExist(int pTransRef, Connection pConn) throws SQLException {
		boolean result = false;
		
		String l_Query = "SELECT * FROM " + l_TableName + " WHERE TRANSREF = ? ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setInt(1, pTransRef);
		ResultSet l_Rs = pstmt.executeQuery();
 		
		while (l_Rs.next()) {
			result = true;
			readRecord(l_CurrentIBTInData, l_Rs,"","");			
		}
		
		return result;
	}
	

	public ArrayList<CurrentIBTInData> getCurrentIBTTransaction(CurrentIBTInCriteriaData pCri,Connection pConn) throws SQLException{
		StringBuilder l_query = new StringBuilder();
		lstCurrentIBTInData = new ArrayList<CurrentIBTInData>();
		String l_Typ = "";
		
		if(pCri.TransType.contains("2") || pCri.TransType.contains("3")  ) {
			l_query.append("SET DateFormat DMY;" +
					  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
					  "A.DrAccNumber ,A. FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
					  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
					  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1, A.N2 , A.AutoKey ,  A.OnlSno , "+
					  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
					  /*"B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL,REPLACE(P.ProcessCode5,' Account','') As PName "+
					  "FROM " + pCri.TableName + " A INNER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear INNER JOIN T00005 T5 ON T5.T1=A.CrAccNumber INNER JOIN ProductType P ON P.ProductCode=T5.t2 WHERE 1=1  ") ;*/
					  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL,'' As PName ");
			if(pCri.TableName.equalsIgnoreCase("currentibtin"))
				l_query.append( "FROM " + pCri.TableName + " A LEFT OUTER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType WHERE 1=1 ") ;
			else					
				l_query.append( "FROM " + pCri.TableName + " A INNER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType WHERE 1=1 ") ;
					
			l_Typ = "New";
		} else if(pCri.TransType.contains("0") || pCri.TransType.contains("1")){
			l_query.append("SET DateFormat DMY;" +
					  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
					  "A.DrAccNumber ,A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
					  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
					  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1, A.N2 , A.AutoKey ,  A.OnlSno , "+
					  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
					  /*"B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL,REPLACE(P.ProcessCode5,' Account','') As PName FROM " + 
					  pCri.TableName + " A INNER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear INNER JOIN T00005 T5 ON T5.T1=A.DrAccNumber INNER JOIN ProductType P ON P.ProductCode=T5.t2 WHERE 1=1  ") ;*/
					  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL,'' As PName FROM " );
			if(pCri.TableName.equalsIgnoreCase("currentibtin"))
				l_query.append(pCri.TableName + " A LEFT OUTER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType WHERE 1=1 ") ;
			else
				l_query.append(pCri.TableName + " A INNER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType WHERE 1=1 ") ;
			l_Typ = "New";
		}
		else {
			l_query.append("SET DateFormat DMY;" +
				  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
				  "A.DrAccNumber ,A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
				  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
				  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1 , A.N2, A.AutoKey ,  A.OnlSno , "+
				  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
				  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL FROM ");
			if(pCri.TableName.equalsIgnoreCase("currentibtin"))
				l_query.append(pCri.TableName + " A LEFT OUTER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType WHERE 1=1 ") ;
			else
				l_query.append(pCri.TableName + " A INNER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType WHERE 1=1 ") ;
			l_Typ = "";
		}
		if(pCri.ReportType.contains("1") || pCri.ReportType.contains("4")  || pCri.ReportType.contains("5")
				 || pCri.ReportType.contains("6")){
			String query =" AND A.Status <>1 ";
			l_query.append(query);
		}
		if(pCri.IBTType.contains("3,7") && (pCri.ReportType.contains("1") || pCri.ReportType.contains("3") )){
			String query =" AND A.Status <> 2 ";
			l_query.append(query);
		}
		
		preparewhereClause(l_query, pCri);				
		PreparedStatement pstmt = pConn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			CurrentIBTInData l_CurrentIBTData = new CurrentIBTInData();
			readRecord( l_CurrentIBTData,rs,"Join",l_Typ);
			lstCurrentIBTInData.add(l_CurrentIBTData);
		}
				
		
		return lstCurrentIBTInData;
	}
	
	private void preparewhereClause(StringBuilder pQuery, CurrentIBTInCriteriaData pCri) {
		if(!pCri.pFDate.equals("")) {
			if(pCri.pFDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '"+pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '"+ pCri.pFDate + "'");
			}
		}
		if(!pCri.pTDate.equals("")) {
			if(pCri.pTDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '"+pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '"+ pCri.pTDate + "'");
			}
		}
		if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
			if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.FromBranch LIKE '" + pCri.pFBranch + "%'");
			}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "'");
			}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "%'");
			}else {
				pQuery.append(" AND A.FromBranch = '" + pCri.pFBranch + "'");
			}
		}
		if(!pCri.pTBranch.equals("") && !pCri.pTBranch.equals("999")) {
			if(pCri.pTBranchOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.ToBranch LIKE '" + pCri.pTBranch + "%'");
			}else if(pCri.pTBranchOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "'");
			}else if(pCri.pTBranchOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "%'");
			}else {
				pQuery.append(" AND A.ToBranch = '" + pCri.pTBranch + "'");
			}
		}
		if(!pCri.pDrAccNumber.equals("")) {
			if(pCri.pDrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '" + pCri.pDrAccNumber + "%'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber + "'");
			}else if(pCri.pDrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber + "%'");
			}else {
				pQuery.append(" AND A.DrAccNumber = '" + pCri.pDrAccNumber + "'");
			}
		}
		if(!pCri.pCrAccNumber.equals("")) {
			if(pCri.pCrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '" + pCri.pCrAccNumber + "%'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber + "'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber + "%'");
			}else {
				pQuery.append(" AND A.CrAccNumber = '" + pCri.pCrAccNumber + "'");
			}
		}
		if(pCri.pFAmount != 0) {
			if(pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pFAmount);
			}else {
				pQuery.append(" AND A.Amount =" + pCri.pFAmount);
			}
		}
		if(pCri.pTAmount != 0) {
			if(pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pTAmount);
			}else {
				pQuery.append(" AND Amount =" + pCri.pTAmount);
			}
		}
		if(!pCri.IBTType.equals("")) {
			pQuery.append(" AND A.IBTType IN (" + pCri.IBTType + ")");
		}
		if(!pCri.TransType.equals("")) {
			pQuery.append(" AND A.TransType IN (" + pCri.TransType + ")");
		}
		if(!pCri.OnlineSrNo.equals("")) {
			pQuery.append(" AND A.OnlineSerialNo =" + pCri.OnlineSrNo );
		}
		if(!pCri.BYear.equals("")) {
			pQuery.append(" AND A.BYear IN ('" + pCri.IBTType + "')");
		}
		if(pCri.AutoKey!=0) {
			pQuery.append(" AND A.AutoKey IN ('" + pCri.AutoKey + "')");
		} 
	}
	
	public int getOnlSno(String pIBTType, String pToBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType IN("+ pIBTType+")) AND ToBranch =? AND Byear =?";			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		//pstmt.setInt(1, pIBTType);
		pstmt.setString(1, pToBranch);
		pstmt.setString(2, pBYear);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){	
			l_DrOnlineSerialNo = rs.getInt(1);
		}
				
		return l_DrOnlineSerialNo;
	}
	
	public boolean saveCurrentIBTIn(CurrentIBTInData pData, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_query ="INSERT INTO "+l_TableName+" (MainKey , IBTType  , TransType , TransDate ,  Authorisecode  , OnlineSerialNo , CrAccNumber , "+
					  "DrAccNumber , FromBranch  , ToBranch , Amount  , Transref , Transno ,  PreviousBal  , Status , "+  
					  "ChequeNo ,  ToName  , ToNrc , SendTime , ReceiveTime ,  FromName , FromNrc , PrintStatus , "+  
					  "SaveTime ,  PostTime , CompleteTime  , Byear , Remark , T1 , N1,  OnlSno, "+
					  " TestKey ,  Prefixed  , PfixedDate ,CurrencyCode,CurrencyRate,N2,N3,N4,T2,T3,T4) VALUES " +
					  "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement l_pstmt = pConn.prepareStatement(l_query);
		updateRecord(pData,l_pstmt, false);
		if(l_pstmt.executeUpdate() > 0){
			l_result = true;
		}
		return l_result;
	}
	
	public boolean updateCurrentIBTIn(CurrentIBTInData pData, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_query ="UPDATE  "+l_TableName+" SET IBTType =? , TransType=? , TransDate=? ,  Authorisecode=?  , OnlineSerialNo=? , CrAccNumber=? , "+
					  "DrAccNumber=? , FromBranch =? , ToBranch=? , Amount =? , Transref=? , Transno=? ,  PreviousBal =? , Status=?  , "+  
					  "ChequeNo = ?,  ToName =?  , ToNrc =? , SendTime=?, ReceiveTime=? ,  FromName=?  , FromNrc=?  , PrintStatus=? , "+  
					  "SaveTime =?  ,  PostTime=?  , CompleteTime=?   , Byear=?  , Remark=?  , T1=?  , N1=? ,  OnlSno=? , "+
					  " TestKey=?  ,  Prefixed=? , PfixedDate=?,CurrencyCode=?,CurrencyRate=?,N2 = ? ,N3 = ? ,N4 = ? ,T2 = ? ,T3 = ? , T4 = ?  WHERE MainKey= ?";
		PreparedStatement l_pstmt = pConn.prepareStatement(l_query);
		updateRecord(pData,l_pstmt, true);
		if(l_pstmt.executeUpdate() > 0){
			l_result = true;
		}
		return l_result;
	}
	
	private void updateRecord(CurrentIBTInData pData, PreparedStatement pstmt, Boolean isUpdate) throws SQLException{
		int i=1;
		
		if(!isUpdate){
			pstmt.setString(i++, pData.getMainKey());
			pstmt.setShort(i++, pData.getIBTType());
			pstmt.setShort(i++, pData.getTransType());
			pstmt.setString(i++, pData.getTransDate());
			pstmt.setString(i++, pData.getAuthorisecode());
			pstmt.setInt(i++, pData.getOnlineSerialNo());
			pstmt.setString(i++, pData.getCrAccNumber());
			pstmt.setString(i++, pData.getDrAccNumber());
			pstmt.setString(i++, pData.getFromBranch());
			pstmt.setString(i++, pData.getToBranch());
			pstmt.setDouble(i++, pData.getAmount());
			pstmt.setInt(i++, pData.getTransref());
			pstmt.setInt(i++, pData.getTransno());
			pstmt.setDouble(i++, pData.getPreviousBal());
			pstmt.setShort(i++, pData.getStatus());
			pstmt.setString(i++, pData.getChequeNo());
			pstmt.setString(i++, pData.getToName());
			pstmt.setString(i++, pData.getToNrc());
			pstmt.setString(i++, pData.getSendTime());
			pstmt.setString(i++, pData.getReceiveTime());
			pstmt.setString(i++, pData.getFromName());
			pstmt.setString(i++, pData.getFromNrc());
			pstmt.setShort(i++, pData.getPrintStatus());
			pstmt.setString(i++, pData.getSaveTime());
			pstmt.setString(i++, pData.getPostTime());
			pstmt.setString(i++, pData.getCompleteTime());
			pstmt.setString(i++, pData.getByear());
			pstmt.setString(i++, pData.getRemark());
			pstmt.setString(i++, pData.getT1());
			pstmt.setInt(i++, pData.getN1());
			pstmt.setString(i++, pData.getOnlSno());
			pstmt.setString(i++, pData.getTestKey());
			pstmt.setInt(i++, pData.getPrefixed());
			pstmt.setString(i++, pData.getPfixedDate());
			pstmt.setString(i++, pData.getCurrencyCode());
			pstmt.setDouble(i++, pData.getCurrencyRate());
			pstmt.setInt(i++, pData.getN2());
			pstmt.setInt(i++, pData.getN3());
			pstmt.setInt(i++, pData.getN4());
			pstmt.setString(i++, pData.getT2());
			pstmt.setString(i++, pData.getT3());
			pstmt.setString(i++, pData.getT4());			
		}else{			
			pstmt.setShort(i++, pData.getIBTType());
			pstmt.setShort(i++, pData.getTransType());
			pstmt.setString(i++, pData.getTransDate());
			pstmt.setString(i++, pData.getAuthorisecode());
			pstmt.setInt(i++, pData.getOnlineSerialNo());
			pstmt.setString(i++, pData.getCrAccNumber());
			pstmt.setString(i++, pData.getDrAccNumber());
			pstmt.setString(i++, pData.getFromBranch());
			pstmt.setString(i++, pData.getToBranch());
			pstmt.setDouble(i++, pData.getAmount());
			pstmt.setInt(i++, pData.getTransref());
			pstmt.setInt(i++, pData.getTransno()); 
			pstmt.setDouble(i++, pData.getPreviousBal());
			pstmt.setShort(i++, pData.getStatus());
			pstmt.setString(i++, pData.getChequeNo());
			pstmt.setString(i++, pData.getToName());
			pstmt.setString(i++, pData.getToNrc());
			pstmt.setString(i++, pData.getSendTime());
			pstmt.setString(i++, pData.getReceiveTime());
			pstmt.setString(i++, pData.getFromName());
			pstmt.setString(i++, pData.getFromNrc());
			pstmt.setShort(i++, pData.getPrintStatus());
			pstmt.setString(i++, pData.getSaveTime());
			pstmt.setString(i++, pData.getPostTime());
			pstmt.setString(i++, pData.getCompleteTime());
			pstmt.setString(i++, pData.getByear());
			pstmt.setString(i++, pData.getRemark());
			pstmt.setString(i++, pData.getT1());
			pstmt.setInt(i++, pData.getN1());		
			pstmt.setString(i++, pData.getOnlSno());
			pstmt.setString(i++, pData.getTestKey());
			pstmt.setInt(i++, pData.getPrefixed());
			pstmt.setString(i++, pData.getPfixedDate());
			pstmt.setString(i++, pData.getCurrencyCode());
			pstmt.setDouble(i++, pData.getCurrencyRate());
			pstmt.setInt(i++, pData.getN2());
			pstmt.setInt(i++, pData.getN3());
			pstmt.setInt(i++, pData.getN4());
			pstmt.setString(i++, pData.getT2());
			pstmt.setString(i++, pData.getT3());
			pstmt.setString(i++, pData.getT4());
			
			//Where
			pstmt.setString(i++, pData.getMainKey());
		}
	}

	public int getOnlSno(int pIBTType, String pToBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType = ?) AND FromBranch =? AND Byear =?";			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setInt(1, pIBTType);
		pstmt.setString(2, pToBranch);
		pstmt.setString(3, pBYear);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){	
			l_DrOnlineSerialNo = rs.getInt(1);
		}
				
		return l_DrOnlineSerialNo;
	}
	public boolean deleteData(CurrentIBTInData pData, Connection aConn) throws SQLException {
		// TODO Auto-generated method stub
		boolean l_ret=false;		
		
		String m_query = "DELETE FROM "+ l_TableName +" Where IBTType=? AND FromBranch=? AND ToBranch=? " +
				"AND Byear=? AND OnlineSerialNo=? AND OnlSno=? And status=1 ";
		PreparedStatement m_pstmt = aConn.prepareStatement(m_query);
		int i =1 ;
		m_pstmt.setShort(i++, pData.getIBTType());
		m_pstmt.setString(i++, pData.getFromBranch());
		m_pstmt.setString(i++, pData.getToBranch());
		m_pstmt.setString(i++, pData.getByear());
		m_pstmt.setInt(i++, pData.getOnlineSerialNo());
		m_pstmt.setString(i++, pData.getOnlSno());
		
		if(m_pstmt.executeUpdate()>0){
			l_ret =true;
		}
		m_pstmt.close();			
		
		return l_ret;
	}
	
	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionDatas(CurrentIBTInCriteriaData pCri,Connection aConn) throws SQLException {
		
		StringBuilder l_query = new StringBuilder();
		l_query.append( "SET DateFormat DMY;SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate ,  A.Authorisecode  , " +
				"A.OnlineSerialNo , A.CrAccNumber , A.DrAccNumber , A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , " +
				" A.PreviousBal  , A.Status , A.ChequeNo ,  A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime ,  A.FromName , A.FromNrc , " +
				"A.PrintStatus , A.SaveTime ,  A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1,  A.OnlSno, "+
				  " A.TestKey ,  A.Prefixed  , A.PfixedDate FROM " + l_TableName + "As A WHERE 1=1");
		
		preparewhereClause(l_query, pCri);
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			l_CurrentIBTInData =new CurrentIBTInData();
			readRecord(l_CurrentIBTInData, l_rs,"","");
			lstCurrentIBTInData.add(l_CurrentIBTInData);
		}
			
		
		return lstCurrentIBTInData;
	}
	
	//Search Option
	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionDatas(String condition,Connection aConn) throws SQLException {
		
		String m_query = "SET DateFormat DMY;SELECT MainKey , IBTType  , TransType , TransDate ,  Authorisecode  , " +
				"OnlineSerialNo , CrAccNumber , DrAccNumber , FromBranch  , ToBranch , Amount  , Transref , Transno , " +
				" PreviousBal  , Status , ChequeNo ,  ToName  , ToNrc , SendTime , ReceiveTime ,  FromName , FromNrc , " +
				"PrintStatus , SaveTime ,  PostTime , CompleteTime  , Byear , Remark , T1 , N1,  OnlSno, "+
				  " TestKey ,  Prefixed  , PfixedDate FROM " + l_TableName + " WHERE 1=1";
		m_query += condition;  
		PreparedStatement l_pstmt=aConn.prepareStatement(m_query);
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			l_CurrentIBTInData  =new CurrentIBTInData();
			readRecord(l_CurrentIBTInData, l_rs,"","");
			lstCurrentIBTInData.add(l_CurrentIBTInData);
		}
		
		
		return 	lstCurrentIBTInData;
	}
	public boolean updateTransRef(int pTransRef,String pOnlSno,int IBTType,String pFBranch,String BYear, Connection pConn) throws SQLException {
		boolean result = false;
		String l_Query = "UPDATE " + l_TableName + " SET TransNo = ?,TransRef=? WHERE OnlSno = ? AND IBTType=? AND FromBranch=? AND BYear=?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		pstmt.setInt(1, pTransRef);
		pstmt.setInt(2, pTransRef);
		pstmt.setString(3, pOnlSno);
		pstmt.setInt(4, IBTType);
		pstmt.setString(5, pFBranch);
		pstmt.setString(6, BYear);
		
		result = pstmt.executeUpdate() > 0 ? true : false;
		
		return result;
	}
	
	public boolean updateAfterPost(int pBStatus, int pTransRef,String pPostTime,String pCompleteTime,String pVNo,String pOnlSno,int pIBTType,String pByear, Connection pConn) throws SQLException {
		boolean result = false;
		String l_Query = "UPDATE " + l_TableName + " SET STATUS = ?,TransRef=?,PostTime=?,CompleteTime=?,T2=?  WHERE OnlSno=? AND IBTType=? AND BYear=?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		pstmt.setInt(1, pBStatus);
		pstmt.setInt(2, pTransRef);
		pstmt.setString(3, pPostTime);
		pstmt.setString(4, pCompleteTime);
		pstmt.setString(5, pVNo);		
		pstmt.setString(6, pOnlSno);
		pstmt.setInt(7, pIBTType);
		pstmt.setString(8, pByear);
		result = pstmt.executeUpdate() > 0 ? true : false;
		
		return result;
	}
	
	
	
	private void readRecord(CurrentIBTInData pCurrentIBTInData , ResultSet pRS,String pType,String pNew) throws SQLException{
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		pCurrentIBTInData.setMainKey(pRS.getString("MainKey"));
		pCurrentIBTInData.setAutoKey(pRS.getLong("AutoKey"));		//nlkm		
		pCurrentIBTInData.setIBTType(pRS.getShort("IBTType"));
		pCurrentIBTInData.setTransType(pRS.getShort("TransType"));
		pCurrentIBTInData.setTransDate(df.format(pRS.getDate("TransDate")));
		pCurrentIBTInData.setAuthorisecode(pRS.getString("Authorisecode"));
		pCurrentIBTInData.setOnlineSerialNo(pRS.getInt("OnlineSerialNo"));
		pCurrentIBTInData.setCrAccNumber(pRS.getString("CrAccNumber"));
		pCurrentIBTInData.setDrAccNumber(pRS.getString("DrAccNumber"));
		pCurrentIBTInData.setFromBranch(pRS.getString("FromBranch"));
		pCurrentIBTInData.setToBranch(pRS.getString("ToBranch"));
		pCurrentIBTInData.setAmount(pRS.getDouble("Amount"));
		pCurrentIBTInData.setTransref(pRS.getInt("Transref"));
		pCurrentIBTInData.setTransno(pRS.getInt("Transno"));
		pCurrentIBTInData.setPreviousBal(pRS.getDouble("PreviousBal"));
		pCurrentIBTInData.setStatus(pRS.getShort("Status"));
		pCurrentIBTInData.setChequeNo(pRS.getString("ChequeNo"));
		pCurrentIBTInData.setToName(pRS.getString("ToName"));
		pCurrentIBTInData.setToNrc(pRS.getString("ToNrc"));
		pCurrentIBTInData.setSendTime(pRS.getString("SendTime"));
		pCurrentIBTInData.setReceiveTime(pRS.getString("ReceiveTime"));
		pCurrentIBTInData.setFromName(pRS.getString("FromName"));
		pCurrentIBTInData.setFromNrc(pRS.getString("FromNrc"));
		pCurrentIBTInData.setPrintStatus(pRS.getShort("PrintStatus"));
		pCurrentIBTInData.setSaveTime(pRS.getString("SaveTime"));
		pCurrentIBTInData.setPostTime(pRS.getString("PostTime"));
		pCurrentIBTInData.setCompleteTime(pRS.getString("CompleteTime"));
		pCurrentIBTInData.setByear(pRS.getString("Byear"));
		pCurrentIBTInData.setRemark(pRS.getString("Remark"));
		pCurrentIBTInData.setT1(pRS.getString("T1"));
		pCurrentIBTInData.setN1(pRS.getInt("N1"));
		pCurrentIBTInData.setN2(pRS.getInt("N2"));		//nlkm
		pCurrentIBTInData.setOnlSno(pRS.getString("OnlSno"));
		pCurrentIBTInData.setTestKey(pRS.getString("TestKey"));
		pCurrentIBTInData.setPrefixed(pRS.getInt("Prefixed"));
		pCurrentIBTInData.setPfixedDate(pRS.getString("PfixedDate"));	
		pCurrentIBTInData.setCurrencyCode(pRS.getString("CurrencyCode"));
		pCurrentIBTInData.setCurrencyRate(pRS.getDouble("CurrencyRate"));
		if(!pType.equals("")){
			pCurrentIBTInData.setIBTComission(pRS.getDouble("IBTCom"));
			pCurrentIBTInData.setOnlineFees(pRS.getDouble("OnlineFees"));
			pCurrentIBTInData.setComGl(pRS.getString("IBTGL"));
			pCurrentIBTInData.setOnlineFeesGl(pRS.getString("OnlFeesGL"));
			pCurrentIBTInData.setT2(pRS.getString("VNo"));
			pCurrentIBTInData.setDrComGl(pRS.getString("DrIBTGL"));
			pCurrentIBTInData.setDrOnlineFeesGl(pRS.getString("DrOnlFeeGL"));
		}
		if(!pNew.equals("")){
			pCurrentIBTInData.setpPName(pRS.getString("PName"));
		}
				
	}
	
	public CurrentIBTInData getCurrentIBTTransactionData(CurrentIBTInData pData,Connection aConn) throws SQLException {
		CurrentIBTInData mCurrentIBTInData = null;
		
		StringBuilder l_query = new StringBuilder();
		l_query.append( "SET DateFormat DMY;" +
				  "SELECT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
				  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
				  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
				  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,A.T3, " +
				  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
				  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1");
		
		if(pData.getAutoKey() !=0){
			l_query.append(" AND A.Autokey = "+pData.getAutoKey());
		}
		if(!pData.getOnlSno().equals("")){
			l_query.append(" AND A.onlSno = '"+pData.getOnlSno()+"'");
		}
		if(!pData.getByear().equals("")){
			l_query.append(" AND A.Byear = '"+pData.getByear()+"'");
		}
		if(pData.getIBTType()!=0){
			l_query.append(" AND A.IBTType = "+pData.getIBTType());
		}
		if(pData.getTransref() !=0){
			l_query.append(" AND A.TransRef = '"+pData.getTransref()+"'");
		}
		if(pData.getStatus()!=0){
			l_query.append(" AND A.Status = "+pData.getStatus() );
		}
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData =new CurrentIBTInData();
			CurrentIBTOutDAO.readRecord(mCurrentIBTInData, l_rs,"Join");				
		}
			
		
		return mCurrentIBTInData;
	}
	
	public ArrayList<CurrentIBTInData> getOfflineCurrentIBTTransaction(CurrentIBTInCriteriaData pCri, String offlineFlag, Connection pConn) throws SQLException{
		StringBuilder l_query = new StringBuilder();
		lstCurrentIBTInData = new ArrayList<CurrentIBTInData>();
		String l_Typ = "";
		
		if(pCri.TransType.contains("2") || pCri.TransType.contains("3")  ) {
			l_query.append("SET DateFormat DMY;" +
					  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
					  "A.DrAccNumber ,A. FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
					  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
					  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1 , A.N2 , A.AutoKey ,  A.OnlSno , "+
					  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
					  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL,'' As PName "+
					  "FROM " + pCri.TableName + " A Left JOIN " +
					  "IBTComLog B ON A.OnlSno=B.OnlSno AND A.TransType=B.TransType AND A.Byear=B.BYear WHERE 1=1 " + offlineFlag) ; 
			l_Typ = "New";
		} else if(pCri.TransType.contains("0") || pCri.TransType.contains("1")){
			l_query.append("SET DateFormat DMY;" +
					  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
					  "A.DrAccNumber ,A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
					  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
					  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1 , A.N2 , A.AutoKey ,  A.OnlSno , "+
					  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
					  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL,'' As PName FROM " + 
					  pCri.TableName + " A Left JOIN " +
					  "IBTComLog B ON A.OnlSno=B.OnlSno AND A.TransType=B.TransType AND A.Byear=B.BYear WHERE 1=1  " + offlineFlag) ;
			l_Typ = "New";
		}
		else {
			l_query.append("SET DateFormat DMY;" +
				  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
				  "A.DrAccNumber ,A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
				  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
				  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1 , A.N2 ,A.AutoKey ,  A.OnlSno , "+
				  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
				  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL FROM " + pCri.TableName + " A Left JOIN " +
				  "IBTComLog B ON A.OnlSno=B.OnlSno AND A.TransType=B.TransType AND A.Byear=B.BYear WHERE 1=1 " + offlineFlag) ; 
			l_Typ = "";
		}
		preparewhereClause(l_query, pCri);		   
		PreparedStatement pstmt = pConn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			CurrentIBTInData l_CurrentIBTData = new CurrentIBTInData();
			readRecord( l_CurrentIBTData,rs,"Join",l_Typ);
			lstCurrentIBTInData.add(l_CurrentIBTData);
		}
				
		
		return lstCurrentIBTInData;
	}
	
	public ArrayList<CurrentIBTInData> getOfflineCurrentIBTTransactionDatas(CurrentIBTInCriteriaData pCri,Connection aConn) throws SQLException {
		
		StringBuilder l_query = new StringBuilder();
		l_query.append( "SET DateFormat DMY;" +
				  "SELECT DISTINCT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
				  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
				  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
				  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1, " +
				  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
				  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=1 ");	 
		
		preparewhereClause1(l_query, pCri);
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			l_CurrentIBTInData =new CurrentIBTInData();
			readRecord(l_CurrentIBTInData, l_rs,"Join","");
			lstCurrentIBTInData.add(l_CurrentIBTInData);
		}
			
		
		return lstCurrentIBTInData;
	}
	
	//Search Option
	public ArrayList<CurrentIBTInData> getOfflineCurrentIBTTransactionDatas(String condition,Connection aConn) throws SQLException {
		
		String m_query = "SET DateFormat DMY;" +
				  "SELECT DISTINCT A.MainKey ,A.AutoKey, A.IBTType  , A.TransType , A.TransDate ,  A.Authorisecode  , A.OnlineSerialNo ," +
				  " A.CrAccNumber , A.DrAccNumber , A.FromBranch  , A.ToBranch ,A.Amount  , A.Transref , A.Transno , " +
				  " A.PreviousBal  , A.Status , A.ChequeNo ,  A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime ,  A.FromName ," +
				  " A.FromNrc , A.PrintStatus , A.SaveTime ,  A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 ," +
				  " A.N1 , A.AutoKey ,  A.OnlSno , A.TestKey ,  A.Prefixed  , A.PfixedDate,A.CurrencyCode,A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees," +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 	//N1=1 for Offline
				  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=1 ";	//nlkm (for Left join)
		m_query += condition;  
		PreparedStatement l_pstmt=aConn.prepareStatement(m_query);
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			l_CurrentIBTInData  =new CurrentIBTInData();
			readRecord(l_CurrentIBTInData, l_rs,"Join","");//mmm
			lstCurrentIBTInData.add(l_CurrentIBTInData);
		}
		
		return 	lstCurrentIBTInData;
	}
	
	private void preparewhereClause1(StringBuilder pQuery, CurrentIBTInCriteriaData pCri) {
		if(!pCri.pFDate.equals("")) {
			if(pCri.pFDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '"+pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '"+ pCri.pFDate + "'");
			}
		}
		if(!pCri.pTDate.equals("")) {
			if(pCri.pTDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '"+pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '"+ pCri.pTDate + "'");
			}
		}
		/*
		 * if(!pCri.TransType.contains(",")){
			if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
				if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
					pQuery.append(" AND A.FromBranch LIKE '" + pCri.pFBranch + "%'");
				}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
					pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "'");
				}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
					pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "%'");
				}else {
					pQuery.append(" AND A.FromBranch = '" + pCri.pFBranch + "'");
				}
			}
		}
		 * */
		if(!pCri.TransType.contains(",")){//mtdk add this one
			if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
				if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
					pQuery.append(" AND A.FromBranch LIKE '" + pCri.pFBranch + "%'");
				}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
					pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "'");
				}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
					pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "%'");
				}else {
					pQuery.append(" AND A.FromBranch = '" + pCri.pFBranch + "'");
				}
			}
		
		}
		
		/*if(!pCri.TransType.contains(",")){
			if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
				if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
					pQuery.append(" AND A.ToBranch LIKE '" + pCri.pFBranch + "%'");
				}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
					pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pFBranch + "'");
				}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
					pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pFBranch + "%'");
				}else {
					pQuery.append(" AND A.ToBranch = '" + pCri.pFBranch + "'");
				}
			}
		}*/ //this is mtdk comment
		
		else {
			String[] st = pCri.TransType.split(",");
			if(st.length>0){
				for(int i=0 ;i<st.length;i++){
					//for TransDeposit And Transfer Withdraw
					if(st[i].equals("1")){//Transfer Withdraw	
						if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
							if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
								pQuery.append(" AND (A.ToBranch LIKE '" + pCri.pFBranch + "%'");
							}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
								pQuery.append(" AND (A.ToBranch LIKE '%" + pCri.pFBranch + "'");
							}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
								pQuery.append(" AND (A.ToBranch LIKE '%" + pCri.pFBranch + "%'");
							}else {
								pQuery.append(" AND (A.ToBranch = '" + pCri.pFBranch + "'");
							}
						}
					} else if(st[i].equals("3")){
						//Transfer Deposit
						if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
							if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
								pQuery.append(" OR A.FromBranch LIKE '" + pCri.pFBranch + "%')");
							}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
								pQuery.append(" OR A.FromBranch LIKE '%" + pCri.pFBranch + "')");
							}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
								pQuery.append(" OR A.FromBranch LIKE '%" + pCri.pFBranch + "%')");
							}else {
								pQuery.append(" OR A.FromBranch = '" + pCri.pFBranch + "')");
							}
						}
					}
				}
				
			}
			
		}
		if(!pCri.TransType.equals("")) {
			pQuery.append(" AND A.TransType IN (" + pCri.TransType + ")");
		}
		if(!pCri.pTBranch.equals("") && !pCri.pTBranch.equals("999")) {
			if(pCri.pTBranchOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.ToBranch LIKE '" + pCri.pTBranch + "%'");
			}else if(pCri.pTBranchOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "'");
			}else if(pCri.pTBranchOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "%'");
			}else {
				pQuery.append(" AND A.ToBranch = '" + pCri.pTBranch + "'");
			}
		}
		if(!pCri.pDrAccNumber.equals("")) {
			if(pCri.pDrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '" + pCri.pDrAccNumber + "%'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber + "'");
			}else if(pCri.pDrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber + "%'");
			}else {
				pQuery.append(" AND A.DrAccNumber = '" + pCri.pDrAccNumber + "'");
			}
		}
		if(!pCri.pCrAccNumber.equals("")) {
			if(pCri.pCrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '" + pCri.pCrAccNumber + "%'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber + "'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber + "%'");
			}else {
				pQuery.append(" AND A.CrAccNumber = '" + pCri.pCrAccNumber + "'");
			}
		}
		if(pCri.pFAmount != 0) {
			if(pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pFAmount);
			}else {
				pQuery.append(" AND Amount =" + pCri.pFAmount);
			}
		}
		if(pCri.pTAmount != 0) {
			if(pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pTAmount);
			}else {
				pQuery.append(" AND Amount =" + pCri.pTAmount);
			}
		}
		if(!pCri.IBTType.equals("")) {
			pQuery.append(" AND A.IBTType IN (" + pCri.IBTType + ")");
		}
		
		if(!pCri.OnlineSrNo.equals("")) {
			pQuery.append(" AND A.OnlSno =" + pCri.OnlineSrNo );
		}
		if(!pCri.BYear.equals("")) {
			pQuery.append(" AND A.BYear IN ('" + pCri.IBTType + "')");
		}
		if(pCri.AutoKey!=0) {
			pQuery.append(" AND A.AutoKey IN ('" + pCri.AutoKey + "')");
		}
		if(pCri.TransType.equals("3,1")|| pCri.equals("1")){
			pQuery.append(" AND (A.FromBranch='"+pCri.pFBranch+"' OR  A.ToBranch='"+pCri.pTBranch+"')");//mtdk change
		}
		if(!pCri.Status.equals("")){
			pQuery.append(" AND A.Status IN("+pCri.Status+")");
		}else{
			pQuery.append(" AND A.Status <>3 ");
		}
	}
	
	public ArrayList<CurrentIBTInData> getMobileVouTransaction(CurrentIBTInCriteriaData pCri,Connection pConn) throws SQLException{
		StringBuilder l_query = new StringBuilder();
		lstCurrentIBTInData = new ArrayList<CurrentIBTInData>();
		String l_Typ = "";
		
		if(pCri.TableName.contains("59")) {
			l_query.append("SET DateFormat DMY;" +
			  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A.Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
			  "A.DrAccNumber ,A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
			  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
			  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1 ,A.N2, A.AutoKey , A.OnlSno , "+
			  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate" +
			  " FROM " + pCri.TableName + " A WHERE 1=1  ") ; 
			l_Typ = "";
		} else if(pCri.TableName.contains("60")){
			l_query.append("SET DateFormat DMY;" +
			  "SELECT A.MainKey , A.IBTType  , A.TransType , A.TransDate , A. Authorisecode  , A.OnlineSerialNo , A.CrAccNumber , "+
			  "A.DrAccNumber ,A.FromBranch  , A.ToBranch , A.Amount  , A.Transref , A.Transno , A.PreviousBal  , A.Status , "+  
			  "A.ChequeNo , A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime , A.FromName , A.FromNrc , A.PrintStatus , "+  
			  "A.SaveTime , A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 , A.N1 ,A.N2 , A.AutoKey ,  A.OnlSno , "+
			  "A.TestKey , A.Prefixed  , A.PfixedDate, A.CurrencyCode, A.CurrencyRate,B.Amount  As IBTCom, B.N1 As OnlineFees," +
			  "B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber As DrIBTGL,B.T1 As DrOnlFeeGL FROM " + 
			  pCri.TableName + " A LEFT JOIN " + pCri.comTableName +
			  " B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear and A.IBTType=B.IBType-100 And A.FromBranch=B.FromBranch And A.ToBranch=B.ToBranch WHERE 1=1  ") ;
			l_Typ = "Out";
		}
		
		preparewhereClauseMobileVou(l_query, pCri);		   
		PreparedStatement pstmt = pConn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			CurrentIBTInData l_CurrentIBTData = new CurrentIBTInData();
			readRecord( l_CurrentIBTData,rs,l_Typ,"");
			lstCurrentIBTInData.add(l_CurrentIBTData);
		}
				
		
		return lstCurrentIBTInData;
	}
	
	private void preparewhereClauseMobileVou(StringBuilder pQuery, CurrentIBTInCriteriaData pCri) {		
		pCri.pFDate=StrUtil.formatMIT2DDMMYYYY(pCri.pFDate);
		if(!pCri.pFDate.equals("")) {
			if(pCri.pFDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '"+ pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pFDate + "'");
			}else if(pCri.pFDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '"+ pCri.pFDate + "'");
			}
		}
		pCri.pTDate=StrUtil.formatMIT2DDMMYYYY(pCri.pTDate);
		if(!pCri.pTDate.equals("")) {
			if(pCri.pTDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '"+ pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pTDate + "'");
			}else if(pCri.pTDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '"+ pCri.pTDate + "'");
			}
		}
		if(!pCri.pFBranch.equals("") && !pCri.pFBranch.equalsIgnoreCase("999")) {
			if(pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.FromBranch LIKE '" + pCri.pFBranch + "%'");
			}else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "'");
			}else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.FromBranch LIKE '%" + pCri.pFBranch + "%'");
			}else {
				pQuery.append(" AND A.FromBranch = '" + pCri.pFBranch + "'");
			}
		}
		if(!pCri.pTBranch.equals("") && !pCri.pTBranch.equals("999")) {
			if(pCri.pTBranchOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.ToBranch LIKE '" + pCri.pTBranch + "%'");
			}else if(pCri.pTBranchOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "'");
			}else if(pCri.pTBranchOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "%'");
			}else {
				pQuery.append(" AND A.ToBranch = '" + pCri.pTBranch + "'");
			}
		}
		if(!pCri.pDrAccNumber.equals("")) {
			if(pCri.pDrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '" + pCri.pDrAccNumber + "%'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber + "'");
			}else if(pCri.pDrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber + "%'");
			}else {
				pQuery.append(" AND A.DrAccNumber = '" + pCri.pDrAccNumber + "'");
			}
		}
		if(!pCri.pCrAccNumber.equals("")) {
			if(pCri.pCrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '" + pCri.pCrAccNumber + "%'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber + "'");
			}else if(pCri.pCrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber + "%'");
			}else {
				pQuery.append(" AND A.CrAccNumber = '" + pCri.pCrAccNumber + "'");
			}
		}
		if(pCri.pFAmount != 0) {
			if(pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pFAmount);
			}else if(pCri.pFAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pFAmount);
			}else {
				pQuery.append(" AND A.Amount =" + pCri.pFAmount);
			}
		}
		if(pCri.pTAmount != 0) {
			if(pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pTAmount);
			}else if(pCri.pTAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pTAmount);
			}else {
				pQuery.append(" AND Amount =" + pCri.pTAmount);
			}
		}
		if(!pCri.IBTType.equals("")) {
			pQuery.append(" AND A.IBTType=" + pCri.IBTType + "");
		}
		if(!pCri.TransType.equals("")) {
			pQuery.append(" AND A.TransType=" + pCri.TransType + "");
		}
		if(!pCri.Status.equals("")) {
			pQuery.append(" AND A.Status=" + pCri.Status + "");
		}
	}
	//mmm, Check OnlSrNo is exit or not For Offline Form
	public boolean CheckDuplicateOnlineSrNo(CurrentIBTInData pData, Connection aConn) throws SQLException {
		// TODO Auto-generated method stub
		boolean l_ret=false;		
	
		String m_query = "SELECT OnlSno  FROM CurrentIBTIn WHERE FromBranch=? AND ToBranch =? AND Byear=? AND OnlSno =? AND MainKey <> ?";
		PreparedStatement m_pstmt = aConn.prepareStatement(m_query);
		m_pstmt.setString(1, pData.getFromBranch());
		m_pstmt.setString(2, pData.getToBranch());

		m_pstmt.setString(3, pData.getByear());
		m_pstmt.setString(4, pData.getOnlSno());
		m_pstmt.setString(5, pData.getMainKey());
		if(pData.getTransType()==2 || pData.getTransType()==3 ) //For Cash Deposit , Debit Transfer
			m_query += " AND TransType= " +pData.getTransType();
		ResultSet rs = m_pstmt.executeQuery();
		while(rs.next()){
			l_ret =true;
		}
		m_pstmt.close();			
		
		return l_ret;
	}
	//mmm
	public CurrentIBTInData getCurrentIBTTransactionDataOffline(CurrentIBTInData pData,Connection aConn) throws SQLException {
		CurrentIBTInData mCurrentIBTInData = null;
		
		StringBuilder l_query = new StringBuilder();
		l_query.append( "SET DateFormat DMY;" +
				  "SELECT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
				  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
				  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
				  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,A.T3, " +
				  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
				  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1");
		
		//mmm
		if(!pData.getMainKey().equals("")){
			l_query.append(" AND A.MainKey = '"+pData.getMainKey()+"'");
		}
		
		if(pData.getAutoKey() !=0){
			l_query.append(" AND A.Autokey = "+pData.getAutoKey());
		}
		//mmm
		if(!pData.getOnlSno().equals("")){
			l_query.append(" AND A.onlSno = '"+pData.getOnlSno()+"'");
		}
		if(!pData.getByear().equals("")){
			l_query.append(" AND A.Byear = '"+pData.getByear()+"'");
		}
		if(pData.getIBTType()!=0){
			l_query.append(" AND A.IBTType = "+pData.getIBTType());
		}
		if(pData.getTransref() !=0){
			l_query.append(" AND A.TransRef = '"+pData.getTransref()+"'");
		}
		if(pData.getStatus()!=0){
			l_query.append(" AND A.Status = "+pData.getStatus() );
		}
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData =new CurrentIBTInData();
			CurrentIBTOutDAO.readRecord(mCurrentIBTInData, l_rs,"Join");				
		}	
		return mCurrentIBTInData;
	}
	
	public boolean updateCurrentIBTInForPost(CurrentIBTInData pData, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_query ="Update CurrentIBTIn Set Status=? where onlSno=? and BYear=? and IBTType=?";
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setInt(1, pData.getStatus());
		pstmt.setString(2, pData.getOnlSno());
		pstmt.setString(3,pData.getByear());
		pstmt.setShort(4,pData.getIBTType());
		l_result = pstmt.executeUpdate() > 0 ? true : false;
		return l_result;
	}
	
}
