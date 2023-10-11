package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.CurrentIBTInCriteriaData;
import com.nirvasoft.rp.shared.CurrentIBTInData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.shared.Enum.DateFilterOperator;
import com.nirvasoft.rp.shared.Enum.NumFilterOperator;
import com.nirvasoft.rp.shared.Enum.TxTFilterOperator;

public class CurrentIBTOutDAO {
	
	private  ArrayList<CurrentIBTInData> lstCurrentIBTOutData = new ArrayList<CurrentIBTInData>();
	String l_TableName = "CurrentIBTOut";
	private CurrentIBTInData mCurrentIBTInData;
	
	public CurrentIBTInData getCurrentIBTOutData(){
		return mCurrentIBTInData;
	}
	public void setCurrentIBTOutData(CurrentIBTInData mCurrentIBTInData){
		this.mCurrentIBTInData = mCurrentIBTInData;
	}
	
	public boolean saveCurrentIBTOut(CurrentIBTInData pData, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_query ="INSERT INTO "+l_TableName+" (MainKey , IBTType  , TransType , TransDate ,  Authorisecode  , OnlineSerialNo , CrAccNumber , "+
					  "DrAccNumber , FromBranch  , ToBranch , Amount  , Transref , Transno ,  PreviousBal  , Status , "+  
					  "ChequeNo ,  ToName  , ToNrc , SendTime , ReceiveTime ,  FromName , FromNrc , PrintStatus , "+  
					  "SaveTime ,  PostTime , CompleteTime  , Byear , Remark , T1 , N1,  OnlSno, "+
					  "TestKey ,  Prefixed  , PfixedDate,CurrencyCode,CurrencyRate,N2,N3,N4,T2,T3,T4 ) VALUES " +
					  "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement l_pstmt = pConn.prepareStatement(l_query);
		updateRecord(pData,l_pstmt, false);
		if(l_pstmt.executeUpdate() > 0){
			l_result = true;
		}
		return l_result;
	}
	
	public boolean updateCurrentIBTOut(CurrentIBTInData pData, Connection pConn) throws SQLException{
		boolean l_result = false;
		PreparedStatement pstmt = pConn.prepareStatement("UPDATE "+l_TableName+" SET " 
				+ " IBTType =?, TransType=?, TransDate = ?, Authorisecode = ?, OnlineSerialNo = ?, CrAccNumber = ?, DrAccNumber = ?, FromBranch = ?, ToBranch = ?, Amount = ?, "
				+ " Transref = ?, Transno = ?, PreviousBal = ?, Status = ?, ChequeNo = ?, ToName = ?, ToNrc = ?, SendTime = ?, ReceiveTime = ?, FromName = ?, "
				+ " FromNrc = ?, PrintStatus = ?, SaveTime = ?, PostTime = ?, CompleteTime = ?, Byear = ?, Remark = ?, T1 = ?, N1 = ?, OnlSno = ?, "
				+ " TestKey = ?, Prefixed = ?, PfixedDate = ?, CurrencyCode = ?,CurrencyRate = ?, N2 = ?, N3 = ?, N4 = ?, T2 = ?,T3 = ?, T4 = ?  "
				+ " WHERE MainKey = ? ");
		updateRecord(pData, pstmt, true);
		if(pstmt.executeUpdate() > 0){
			l_result=true;
		}
		pstmt.close();
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

	public ArrayList<CurrentIBTInData> getCurrentIBTTransaction(long pAutoKey,Connection pConn) throws SQLException{
		String l_query = "";		
		
		l_query = "SET DateFormat DMY;" +
				  "SELECT A.MainKey ,A.AutoKey, A.IBTType  , A.TransType , A.TransDate ,  A.Authorisecode  , A.OnlineSerialNo ," +
				  " A.CrAccNumber , A.DrAccNumber , A.FromBranch  , A.ToBranch ,A.Amount  , A.Transref , A.Transno , " +
				  " A.PreviousBal  , A.Status , A.ChequeNo ,  A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime ,  A.FromName ," +
				  " A.FromNrc , A.PrintStatus , A.SaveTime ,  A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1 ,A.T3," +
				  " A.N1 , A.AutoKey ,  A.OnlSno , A.TestKey ,  A.Prefixed  , A.PfixedDate,A.CurrencyCode,A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees," +
				  "	B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
				  " A INNER JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE A.AutoKey = ? " ; 			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setLong(1, pAutoKey);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			CurrentIBTInData l_CurrentIBTData = new CurrentIBTInData();
			readRecord(l_CurrentIBTData,rs,"Join");
			lstCurrentIBTOutData.add(l_CurrentIBTData);
		}
			
		
		return lstCurrentIBTOutData;
	}

	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionList(String condition, Connection conn) throws SQLException{
		ArrayList<CurrentIBTInData> ret = new ArrayList<CurrentIBTInData>();
		CurrentIBTInData l_Data = null;
		StringBuffer l_query = new StringBuffer();

		l_query.append("SELECT * FROM "+l_TableName+" WHERE 1=1");
		
		l_query.append(condition);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			l_Data = new CurrentIBTInData();
			readRecord(l_Data, rs,"");
			ret.add(l_Data);
		}
		
		pstmt.close();
		return ret;
	}
	
	
	
	public int getOnlSno(String pIBTType, String pToBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType IN("+pIBTType+")) AND ToBranch =? AND Byear =?";			
				   
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
	
	public int getOnlSnoNew(String pIBTType, String pFBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType IN("+pIBTType+")) AND FromBranch =? AND Byear =?";			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		//pstmt.setInt(1, pIBTType);
		pstmt.setString(1, pFBranch);
		pstmt.setString(2, pBYear);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){	
			l_DrOnlineSerialNo = rs.getInt(1);
		}
				
		return l_DrOnlineSerialNo;
	}
	
	public int getOnlSnoBetween(String pIBTType, String pFBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType IN("+pIBTType+")) AND T3 =? AND Byear =?";			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		//pstmt.setInt(1, pIBTType);
		pstmt.setString(1, pFBranch);
		pstmt.setString(2, pBYear);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){	
			l_DrOnlineSerialNo = rs.getInt(1);
		}
				
		return l_DrOnlineSerialNo;
	}
	
	public int getOnlSno_Style2(String pIBTType,String pFromBarnch, String pToBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType IN("+pIBTType+")) AND FromBranch= ? AND ToBranch =? AND Byear =?";			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		//pstmt.setInt(1, pIBTType);
		pstmt.setString(1, pFromBarnch);
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
		
		String m_query = "DELETE FROM "+ l_TableName +" WHERE AutoKey=? ";
		PreparedStatement m_pstmt = aConn.prepareStatement(m_query);
		m_pstmt.setLong(1, pData.getAutoKey());
		if(m_pstmt.executeUpdate()>0){
			l_ret =true;
		}
		m_pstmt.close();			
		
		return l_ret;
	}
	
	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionDatas(
			CurrentIBTInCriteriaData pCri, Connection aConn) throws SQLException {
		
		StringBuilder l_query = new StringBuilder();
		l_query.append("SET DateFormat DMY;"
				+ "SELECT DISTINCT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, "
				+ " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, "
				+ " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, "
				+ " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,A.T3, "
				+ " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, "
				+ " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM "
				+ l_TableName
				+ " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1");

		preparewhereClause(l_query, pCri, 0);
		PreparedStatement l_pstmt = aConn.prepareStatement(l_query
				.toString());
		ResultSet l_rs = l_pstmt.executeQuery();
		while (l_rs.next()) {
			mCurrentIBTInData = new CurrentIBTInData();
			readRecord(mCurrentIBTInData, l_rs, "Join");
			lstCurrentIBTOutData.add(mCurrentIBTInData);
		}

		
		return lstCurrentIBTOutData;
	}
	
	//Search Option
	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionDatas(String condition,int status,Connection aConn) throws SQLException {
		
		String m_query = "SET DateFormat DMY;" +
				  "SELECT DISTINCT Z.MainKey ,Z.AutoKey, Z.IBTType  , Z.TransType , Z.TransDate ,  Z.Authorisecode  , Z.OnlineSerialNo ," +
				  " Z.CrAccNumber , Z.DrAccNumber , Z.FromBranch  , Z.ToBranch ,Z.Amount  , Z.Transref , Z.Transno , " +
				  " Z.PreviousBal  , Z.Status , Z.ChequeNo ,  Z.ToName  , Z.ToNrc , Z.SendTime , Z.ReceiveTime ,  Z.FromName ," +
				  " Z.FromNrc , Z.PrintStatus , Z.SaveTime ,  Z.PostTime , Z.CompleteTime  , Z.Byear , Z.Remark , Z.T1,Z.T3 ," +
				  " Z.N1 , Z.AutoKey ,  Z.OnlSno , Z.TestKey ,  Z.Prefixed  , Z.PfixedDate,Z.CurrencyCode,Z.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees," +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,Z.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,Z.N2 As N2,Z.N3 As N3,Z.N4 As N4 FROM " + l_TableName + 
				  " Z LEFT JOIN IBTComLog B ON Z.OnlSno=B.OnlSno AND Z.Byear=B.BYear AND Z.IBTType=B.IBType AND Z.ToBranch=B.ToBranch AND Z.FromBranch =B.FromBranch WHERE 1=1  And Z.N1=0  ";
		m_query += condition;  
		if(status==1){
			m_query +="  And Z.Status  <>1 ";
		}
		PreparedStatement l_pstmt=aConn.prepareStatement(m_query);
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData  =new CurrentIBTInData();
			readRecord(mCurrentIBTInData, l_rs,"Join");
			lstCurrentIBTOutData.add(mCurrentIBTInData);
		}
		
		
		return 	lstCurrentIBTOutData;
	}
	private void preparewhereClause(StringBuilder pQuery,
			CurrentIBTInCriteriaData pCri, int pType) {
		if (!pCri.pFDate.equals("")) {
			if (pCri.pFDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pFDate + "'");
			} else if (pCri.pFDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pFDate + "'");
			} else if (pCri.pFDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '" + pCri.pFDate + "'");
			} else if (pCri.pFDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pFDate + "'");
			} else if (pCri.pFDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '" + pCri.pFDate + "'");
			}
		}
		if (!pCri.pTDate.equals("")) {
			if (pCri.pTDateOperator == DateFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.TransDate >= '" + pCri.pTDate + "'");
			} else if (pCri.pTDateOperator == DateFilterOperator.Equal) {
				pQuery.append(" AND A.TransDate = '" + pCri.pTDate + "'");
			} else if (pCri.pTDateOperator == DateFilterOperator.GreaterThan) {
				pQuery.append(" AND A.TransDate > '" + pCri.pTDate + "'");
			} else if (pCri.pTDateOperator == DateFilterOperator.LessThan) {
				pQuery.append(" AND A.TransDate < '" + pCri.pTDate + "'");
			} else if (pCri.pTDateOperator == DateFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.TransDate <= '" + pCri.pTDate + "'");
			}
		}
		String[] st = pCri.TransType.split(",");
		if (st[0].equalsIgnoreCase("2") || st[0].equalsIgnoreCase("0")) {
			if (!pCri.pFBranch.equals("")
					&& !pCri.pFBranch.equalsIgnoreCase("999")) {
				if (pCri.pFBranchOperator == TxTFilterOperator.BeginWith) {
					pQuery.append(" AND (A.FromBranch LIKE '" + pCri.pFBranch
							+ "%') ");
				} else if (pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
					pQuery.append(" AND (A.FromBranch LIKE '%" + pCri.pFBranch
							+ "') ");
				} else if (pCri.pFBranchOperator == TxTFilterOperator.Contain) {
					pQuery.append(" AND (A.FromBranch LIKE '%" + pCri.pFBranch
							+ "%') ");
				} else {
					pQuery.append(" AND (A.FromBranch = '" + pCri.pFBranch
							+ "')");
				}
			}
			if (!pCri.pTBranch.equals("")
					&& !pCri.pTBranch.equalsIgnoreCase("999")) {
				if (pCri.pTBranchOperator == TxTFilterOperator.BeginWith) {
					pQuery.append(" AND (A.ToBranch LIKE '" + pCri.pTBranch
							+ "%') ");
				} else if (pCri.pTBranchOperator == TxTFilterOperator.EndWith) {
					pQuery.append(" AND (A.ToBranch LIKE '%" + pCri.pTBranch
							+ "') ");
				} else if (pCri.pTBranchOperator == TxTFilterOperator.Contain) {
					pQuery.append(" AND (A.ToBranch LIKE '%" + pCri.pTBranch
							+ "%') ");
				} else {
					pQuery.append(" AND (A.ToBranch = '" + pCri.pTBranch + "')");
				}
			}
		}
		if (pCri.TransType.equalsIgnoreCase("1,3,9") && (pCri.pType != 4)) {
			if (pType == 1) {
				pQuery.append(" AND ((A.FromBranch='" + pCri.pTBranch
						+ "' OR  A.ToBranch='" + pCri.pTBranch + "') )");
			} else {
				pQuery.append(" AND ((A.FromBranch='" + pCri.pFBranch
						+ "' OR  A.ToBranch='" + pCri.pFBranch + "'))");
			}

		}
		if (pCri.pType == 4) {// Between
			pQuery.append(" AND A.T3 = '" + pCri.pBranch
					+ "' AND A.FromBranch<> '" + pCri.pBranch + "' ");
		}
		// mtdk just comment
		/*
		 * else { String[] st = pCri.TransType.split(","); if(st.length>0){
		 * for(int i=0 ;i<st.length;i++){ //for TransDeposit And Transfer
		 * Withdraw if(st[i].equals("1")){//Transfer Withdraw
		 * if(!pCri.pFBranch.equals("") &&
		 * !pCri.pFBranch.equalsIgnoreCase("999")) { if(pCri.pFBranchOperator ==
		 * TxTFilterOperator.BeginWith) {
		 * pQuery.append(" AND (A.ToBranch LIKE '" + pCri.pFBranch + "%'");
		 * }else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
		 * pQuery.append(" AND (A.ToBranch LIKE '%" + pCri.pFBranch + "'");
		 * }else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
		 * pQuery.append(" AND (A.ToBranch LIKE '%" + pCri.pFBranch + "%'");
		 * }else { pQuery.append(" AND (A.ToBranch = '" + pCri.pFBranch + "'");
		 * } } } else if(st[i].equals("3")){ //Transfer Deposit
		 * if(!pCri.pFBranch.equals("") &&
		 * !pCri.pFBranch.equalsIgnoreCase("999")) { if(pCri.pFBranchOperator ==
		 * TxTFilterOperator.BeginWith) {
		 * pQuery.append(" OR A.FromBranch LIKE '" + pCri.pFBranch + "%')");
		 * }else if(pCri.pFBranchOperator == TxTFilterOperator.EndWith) {
		 * pQuery.append(" OR A.FromBranch LIKE '%" + pCri.pFBranch + "')");
		 * }else if(pCri.pFBranchOperator == TxTFilterOperator.Contain) {
		 * pQuery.append(" OR A.FromBranch LIKE '%" + pCri.pFBranch + "%')");
		 * }else { pQuery.append(" OR A.FromBranch = '" + pCri.pFBranch + "')");
		 * } } } }
		 * 
		 * }
		 * 
		 * }
		 */
		if (!pCri.TransType.equals("")) {
			pQuery.append(" AND A.TransType IN (" + pCri.TransType + ")");
		}
		/*
		 * if(!pCri.pTBranch.equals("") && !pCri.pTBranch.equals("999")) {
		 * if(pCri.pTBranchOperator == TxTFilterOperator.BeginWith) {
		 * pQuery.append(" AND A.ToBranch LIKE '" + pCri.pTBranch + "%'"); }else
		 * if(pCri.pTBranchOperator == TxTFilterOperator.EndWith) {
		 * pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "'"); }else
		 * if(pCri.pTBranchOperator == TxTFilterOperator.Contain) {
		 * pQuery.append(" AND A.ToBranch LIKE '%" + pCri.pTBranch + "%'");
		 * }else { pQuery.append(" AND A.ToBranch = '" + pCri.pTBranch + "'"); }
		 * }
		 */
		if (!pCri.pDrAccNumber.equals("")) {
			if (pCri.pDrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '" + pCri.pDrAccNumber
						+ "%'");
			} else if (pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber
						+ "'");
			} else if (pCri.pDrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.DrAccNumber LIKE '%" + pCri.pDrAccNumber
						+ "%'");
			} else {
				pQuery.append(" AND A.DrAccNumber = '" + pCri.pDrAccNumber
						+ "'");
			}
		}
		if (!pCri.pCrAccNumber.equals("")) {
			if (pCri.pCrAccNumberOperator == TxTFilterOperator.BeginWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '" + pCri.pCrAccNumber
						+ "%'");
			} else if (pCri.pCrAccNumberOperator == TxTFilterOperator.EndWith) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber
						+ "'");
			} else if (pCri.pCrAccNumberOperator == TxTFilterOperator.Contain) {
				pQuery.append(" AND A.CrAccNumber LIKE '%" + pCri.pCrAccNumber
						+ "%'");
			} else {
				pQuery.append(" AND A.CrAccNumber = '" + pCri.pCrAccNumber
						+ "'");
			}
		}
		if (pCri.pFAmount != 0) {
			if (pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			} else if (pCri.pFAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pFAmount);
			} else if (pCri.pFAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pFAmount);
			} else if (pCri.pFAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pFAmount);
			} else {
				pQuery.append(" AND Amount =" + pCri.pFAmount);
			}
		}
		if (pCri.pTAmount != 0) {
			if (pCri.pFAmtOperator == NumFilterOperator.GreaterThan) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			} else if (pCri.pTAmtOperator == NumFilterOperator.GreaterThanEqual) {
				pQuery.append(" AND A.Amount >" + pCri.pTAmount);
			} else if (pCri.pTAmtOperator == NumFilterOperator.LessThan) {
				pQuery.append(" AND A.Amount <" + pCri.pTAmount);
			} else if (pCri.pTAmtOperator == NumFilterOperator.LessThanEqual) {
				pQuery.append(" AND A.Amount <=" + pCri.pTAmount);
			} else {
				pQuery.append(" AND Amount =" + pCri.pTAmount);
			}
		}
		if (!pCri.IBTType.equals("")) {
			pQuery.append(" AND A.IBTType IN (" + pCri.IBTType + ")");
		}

		/*
		 * if(!pCri.OnlineSrNo.equals("")) {
		 * pQuery.append(" AND A.OnlineSerialNo =" + pCri.OnlineSrNo ); }
		 */
		if (!pCri.OnlineSrNo.equals("")) {// mtdk
			pQuery.append(" AND A.OnlSno LIKE '%" + pCri.OnlineSrNo + "%'");
		}

		if (!pCri.BYear.equals("")) {
			pQuery.append(" AND A.BYear IN ('" + pCri.IBTType + "')");
		}
		if (pCri.AutoKey != 0) {
			pQuery.append(" AND A.AutoKey IN ('" + pCri.AutoKey + "')");
		}

		/*
		 * if(!pCri.pFBranch.equals("")){
		 * pQuery.append(" AND A.FromBranch ="+pCri.pFBranch); }
		 * if(!pCri.pTBranch.equals("")){
		 * pQuery.append(" AND A.ToBranch ="+pCri.pTBranch); }
		 */
		String Cri="";
		if (SharedLogic.getSystemSettingT12N1("BNK")==21){
			Cri=" AND A.Status <> 6 ";
		}
		if(pCri.pType==3 && st[0].equalsIgnoreCase("0")){ //Before Denomination for withdraw
			pQuery.append(" AND A.Status =2 ");
		}else if(pCri.pType==3 && !st[0].equalsIgnoreCase("0")){//Before Denomination for Deposit/Transfer
			pQuery.append(" AND A.Status <>6 and A.Status <>3 ");
		}
		else{
			if (!pCri.Status.equals("")) {//orginal code
				pQuery.append(" AND A.Status IN(" + pCri.Status + ")"+Cri);
			} else {
				if (pType == 1)
					pQuery.append(" AND A.Status = 3 ");
				else
					pQuery.append(" AND A.Status <> 3 "+Cri);
			}
		}
		

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
	
	public boolean updateTransRef(int pTransRef, long pAutoKey, Connection pConn) throws SQLException {
		boolean result = false;
		String l_Query = "UPDATE " + l_TableName + " SET TransNo = ?,TransRef=? WHERE AutoKey = ? ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		pstmt.setInt(1, pTransRef);
		pstmt.setInt(2, pTransRef);
		pstmt.setLong(3, pAutoKey);
		
		result = pstmt.executeUpdate() > 0 ? true : false;
		
		return result;
	}
	
	public boolean updateAfterPost(int pBStatus,int pTransRef,String pPostTime,String pCompleteTime,String pVNo,long pAutoKey, Connection pConn) throws SQLException {
		boolean result = false;
		String l_Query = "UPDATE " + l_TableName + " SET STATUS = ?,TransRef=?,PostTime=?,CompleteTime=?,T2=?  WHERE AutoKey=?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		pstmt.setInt(1, pBStatus);
		pstmt.setInt(2, pTransRef);
		pstmt.setString(3, pPostTime);
		pstmt.setString(4, pCompleteTime);
		pstmt.setString(5, pVNo);
		pstmt.setLong(6, pAutoKey);
		
		result = pstmt.executeUpdate() > 0 ? true : false;
		
		return result;
	}
	
	public CurrentIBTInData getCurrentIBTTransactionData(CurrentIBTInData pData,Connection aConn) throws SQLException {
		
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
			l_query.append(" AND A.Status = "+pData.getStatus());
		}
		if(pData.getTransType()!=-1){
			if(pData.getTransType()!=99)
				l_query.append(" AND A.TransType = "+pData.getTransType());
			else
				l_query.append(" AND A.TransType IN (1,3)");
		}
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData =new CurrentIBTInData();
			readRecord(mCurrentIBTInData, l_rs,"Join");				
		}
		
		return mCurrentIBTInData;
	}
	
	public boolean checkTransRefExist(int pTransRef, Connection pConn) throws SQLException {
		boolean result = false;
		
		String l_Query = "SELECT * FROM " + l_TableName + " WHERE TRANSREF = ? ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setInt(1, pTransRef);
		ResultSet l_Rs = pstmt.executeQuery();
 		
		while (l_Rs.next()) {
			result = true;
			readRecord(mCurrentIBTInData, l_Rs,"");			
		}
		
		return result;
	}
	

	public int getOnlSno(int pIBTType, String pToBranch, String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		
		l_query = "SELECT MAX(OnlineSerialNo) FROM "+l_TableName+" " +
				"WHERE (IBTType = ?) AND ToBranch =? AND Byear =?";			
				   
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
	
	public static boolean updateibtstatus(int pStatus,String onlineSerilNo,String frombranchCode, String tobranch, int transNo,Connection pConn) throws SQLException {
		boolean result = false;
		String l_Query = "UPDATE CurrentIBTOut set Status=?  WHERE onlineSerialNo=? and FromBranch=? and ToBranch=? and TransRef=? ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		pstmt.setInt(1, pStatus);
		pstmt.setString(2, onlineSerilNo);
		pstmt.setString(3, frombranchCode);
		pstmt.setString(4, tobranch);
		pstmt.setInt(5, transNo);
		
		result = pstmt.executeUpdate() > 0 ? true : false;
		
		return result;
	}
	
	//Search Option
	public ArrayList<CurrentIBTInData> getOfflineCurrentIBTTransactionDatas(String condition,Connection aConn) throws SQLException {
		
		String m_query = "SET DateFormat DMY;" +
				  "SELECT DISTINCT A.MainKey ,A.AutoKey, A.IBTType  , A.TransType , A.TransDate ,  A.Authorisecode  , A.OnlineSerialNo ," +
				  " A.CrAccNumber , A.DrAccNumber , A.FromBranch  , A.ToBranch ,A.Amount  , A.Transref , A.Transno , " +
				  " A.PreviousBal  , A.Status , A.ChequeNo ,  A.ToName  , A.ToNrc , A.SendTime , A.ReceiveTime ,  A.FromName ," +
				  " A.FromNrc , A.PrintStatus , A.SaveTime ,  A.PostTime , A.CompleteTime  , A.Byear , A.Remark , A.T1,A.T3 ," +
				  " A.N1 , A.AutoKey ,  A.OnlSno , A.TestKey ,  A.Prefixed  , A.PfixedDate,A.CurrencyCode,A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees," +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 	//N1=1 for Offline
				  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=1 ";	//nlkm (for Left join)
		m_query += condition;  
		PreparedStatement l_pstmt=aConn.prepareStatement(m_query);
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData  =new CurrentIBTInData();
			readRecord(mCurrentIBTInData, l_rs,"Join");
			lstCurrentIBTOutData.add(mCurrentIBTInData);
		}
		
		
		return 	lstCurrentIBTOutData;
	}
	
	public ArrayList<CurrentIBTInData> getOfflineCurrentIBTTransactionDatas(CurrentIBTInCriteriaData pCri,Connection aConn) throws SQLException {
		
		StringBuilder l_query = new StringBuilder();
		l_query.append( "SET DateFormat DMY;" +
				  "SELECT DISTINCT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
				  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
				  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
				  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,A.T3, " +
				  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode , A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
				  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
				  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=1 ");	 
		
		preparewhereClause(l_query, pCri,0);
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData =new CurrentIBTInData();
			readRecord(mCurrentIBTInData, l_rs,"Join");
			lstCurrentIBTOutData.add(mCurrentIBTInData);
		}
			
		
		return lstCurrentIBTOutData;	
	}
	
	public static void readRecord(CurrentIBTInData pCurrentIBTInData , ResultSet pRS,String pType) throws SQLException{		//nlkm change static to use from IBTIn
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		pCurrentIBTInData.setMainKey(pRS.getString("MainKey"));
		pCurrentIBTInData.setAutoKey(pRS.getLong("AutoKey"));				
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
		pCurrentIBTInData.setT3(pRS.getString("T3"));
		pCurrentIBTInData.setN1(pRS.getInt("N1"));
		pCurrentIBTInData.setN2(pRS.getInt("N2"));		//nlkm for Offline
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
			pCurrentIBTInData.setDrOnlineFeesGl(pRS.getString("DrOnlFeesGL"));
			pCurrentIBTInData.setpComTransRef(pRS.getInt("ComTransRef"));
			pCurrentIBTInData.setN2(pRS.getInt("N2"));		//nlkm for Offline
			pCurrentIBTInData.setN3(pRS.getInt("N3"));
			pCurrentIBTInData.setN4(pRS.getInt("N4"));
		}
				
	}
	//mmm
	public CurrentIBTInData getCurrentIBTTransactionDataOffline(CurrentIBTInData pData,Connection aConn) throws SQLException {
		
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
			l_query.append(" AND A.Status = "+pData.getStatus());
		}
		PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
		ResultSet l_rs=l_pstmt.executeQuery();
		while(l_rs.next()) {
			mCurrentIBTInData =new CurrentIBTInData();
			readRecord(mCurrentIBTInData, l_rs,"Join");				
		}
			
		
		return mCurrentIBTInData;
	}
	//mmm,IBT In,Out Online
	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionDatas(
		String l_IBTTableName, String condition, int Type, String branch,
		String Colbranch,String Transtype,String loginbranch, Connection aConn) throws SQLException {
	
		String m_query = "";
		String Cri="";
		if (SharedLogic.getSystemSettingT12N1("BNK")==21 && branch.equals("")&& Colbranch.equalsIgnoreCase("Z.Status") ){
			Cri=" AND Z.Status <> 6 ";
		}
		if (l_IBTTableName.equals("CurrentIBTIn")) {
			m_query = "SET DateFormat DMY;"
					+ "SELECT DISTINCT Z.MainKey ,Z.AutoKey, Z.IBTType  , Z.TransType , Z.TransDate ,  Z.Authorisecode  , Z.OnlineSerialNo ,"
					+ " Z.CrAccNumber , Z.DrAccNumber , Z.FromBranch  , Z.ToBranch ,Z.Amount  , Z.Transref , Z.Transno , "
					+ " Z.PreviousBal  , Z.Status , Z.ChequeNo ,  Z.ToName  , Z.ToNrc ,Z.SendTime , Z.ReceiveTime ,  Z.FromName ,"
					+ " Z.FromNrc , Z.PrintStatus , Z.SaveTime ,  Z.PostTime , Z.CompleteTime  , Z.Byear , Z.Remark , Z.T1 ,Z.T3, "
					+ " Z.N1 , Z.AutoKey ,  Z.OnlSno , Z.TestKey ,  Z.Prefixed  , Z.PfixedDate,Z.CurrencyCode,Z.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees,"
					+ " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,Z.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,Z.N2 As N2,Z.N3 As N3,Z.N4 As N4 FROM "
					+ l_IBTTableName
					+ " Z LEFT JOIN IBTComLog B ON Z.OnlSno=B.OnlSno AND Z.Byear=B.BYear AND Z.IBTType=B.IBType AND Z.ToBranch=B.ToBranch AND Z.FromBranch =B.FromBranch WHERE 1=1  And Z.N1=0  ";
		
		} else {
			m_query = "SET DateFormat DMY;"
					+ "SELECT DISTINCT Z.MainKey ,Z.AutoKey, Z.IBTType  , Z.TransType , Z.TransDate ,  Z.Authorisecode  , Z.OnlineSerialNo ,"
					+ " Z.CrAccNumber , Z.DrAccNumber , Z.FromBranch  , Z.ToBranch ,Z.Amount  , Z.Transref , Z.Transno , "
					+ " Z.PreviousBal  , Z.Status , Z.ChequeNo ,  Z.ToName  , Z.ToNrc , Z.SendTime , Z.ReceiveTime ,  Z.FromName ,"
					+ " Z.FromNrc , Z.PrintStatus , Z.SaveTime ,  Z.PostTime , Z.CompleteTime  , Z.Byear , Z.Remark , Z.T1 ,Z.T3, "
					+ " Z.N1 , Z.AutoKey ,  Z.OnlSno , Z.TestKey ,  Z.Prefixed  , Z.PfixedDate,Z.CurrencyCode,Z.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees,"
					+ " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,Z.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,Z.N2 As N2,Z.N3 As N3,Z.N4 As N4 FROM "
					+ l_IBTTableName
					+ " Z LEFT JOIN IBTComLog B ON Z.OnlSno=B.OnlSno AND Z.Byear=B.BYear AND Z.IBTType=B.IBType AND Z.ToBranch=B.ToBranch AND Z.FromBranch =B.FromBranch WHERE 1=1  And Z.N1=0  ";
		}
		if((Transtype.equalsIgnoreCase("2,2")||Transtype.equalsIgnoreCase("0"))&& Type==1 &&
				!(Colbranch.equalsIgnoreCase("Z.FromBranch")
				|| Colbranch.equalsIgnoreCase("Z.ToBranch"))){
			m_query +=" And Z.ToBranch ="+loginbranch+" ";
		}else if((Transtype.equalsIgnoreCase("2,2")||Transtype.equalsIgnoreCase("0"))&& Type==2
				&& !(Colbranch.equalsIgnoreCase("Z.FromBranch")
						|| Colbranch.equalsIgnoreCase("Z.ToBranch"))){
			m_query +=" And Z.FromBranch ="+loginbranch+" ";
		}
		m_query += condition+Cri;
		PreparedStatement l_pstmt = aConn.prepareStatement(m_query);
		ResultSet l_rs = l_pstmt.executeQuery();
		while (l_rs.next()) {
			mCurrentIBTInData = new CurrentIBTInData();
			readRecordIn(mCurrentIBTInData, l_rs, "Join");
			if (Colbranch.equalsIgnoreCase("Z.FromBranch")
					|| Colbranch.equalsIgnoreCase("Z.ToBranch") 
					) {
				if (Type != 4 && !(Transtype.equalsIgnoreCase("2,2")||Transtype.equalsIgnoreCase("0"))) {
					if (l_IBTTableName.equals("CurrentIBTOut")
							&& mCurrentIBTInData.getT3().equalsIgnoreCase(
									mCurrentIBTInData.getFromBranch())
							&& (branch.equalsIgnoreCase(mCurrentIBTInData
									.getFromBranch()))
							&& Colbranch.equalsIgnoreCase("Z.FromBranch")) {
						lstCurrentIBTOutData.add(mCurrentIBTInData);
					} else if (l_IBTTableName.equals("CurrentIBTOut")
							&& !mCurrentIBTInData.getT3().equalsIgnoreCase(
									mCurrentIBTInData.getToBranch())
							&& (branch.equalsIgnoreCase(mCurrentIBTInData
									.getToBranch()))
							&& Colbranch.equalsIgnoreCase("Z.ToBranch")) {
						lstCurrentIBTOutData.add(mCurrentIBTInData);
					} else if (l_IBTTableName.equals("CurrentIBTIn")
							&& !mCurrentIBTInData.getT3().equalsIgnoreCase(
									mCurrentIBTInData.getFromBranch())
							&& (branch.equalsIgnoreCase(mCurrentIBTInData
									.getFromBranch()))
							&& Colbranch.equalsIgnoreCase("Z.FromBranch")) {
						lstCurrentIBTOutData.add(mCurrentIBTInData);
					} else if (l_IBTTableName.equals("CurrentIBTIn")
							&& mCurrentIBTInData.getT3().equalsIgnoreCase(
									mCurrentIBTInData.getToBranch())
							&& (branch.equalsIgnoreCase(mCurrentIBTInData
									.getToBranch()))
							&& Colbranch.equalsIgnoreCase("Z.ToBranch")) {
						lstCurrentIBTOutData.add(mCurrentIBTInData);
					} else if (l_IBTTableName.equals("CurrentIBTIn")
							&& (mCurrentIBTInData.getTransType() == 2 || mCurrentIBTInData
									.getTransType() == 0)
							&& (!branch.equalsIgnoreCase(mCurrentIBTInData
									.getToBranch()))
							&& Colbranch.equalsIgnoreCase("Z.ToBranch")) {
						lstCurrentIBTOutData.add(mCurrentIBTInData);
					} else if (l_IBTTableName.equals("CurrentIBTIn")
							&& (mCurrentIBTInData.getTransType() == 2 || mCurrentIBTInData
									.getTransType() == 0)
							&& (!branch.equalsIgnoreCase(mCurrentIBTInData
									.getFromBranch()))
							&& Colbranch.equalsIgnoreCase("Z.FromBranch")) {
						lstCurrentIBTOutData.add(mCurrentIBTInData);
					}
				} else {
					lstCurrentIBTOutData.add(mCurrentIBTInData);
				}
			}
			else {
				lstCurrentIBTOutData.add(mCurrentIBTInData);
			}

		}

		
		return lstCurrentIBTOutData;
	}
		//mmm, For In,Out Online IBT
	public ArrayList<CurrentIBTInData> getCurrentIBTTransactionDatas( String l_IBTTableName, CurrentIBTInCriteriaData pCri,
			Connection aConn) throws SQLException {
		
		StringBuilder l_query = new StringBuilder();
		if (l_IBTTableName.equals("CurrentIBTIn")) {
			l_query.append("SET DateFormat DMY;"
					+ "SELECT DISTINCT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, "
					+ " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, "
					+ " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, "
					+ " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,A.T3,  "
					+ " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, "
					+ " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM "
					+ l_IBTTableName
					+ " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=0");
			preparewhereClause(l_query, pCri, 1);
		} else {
			l_query.append("SET DateFormat DMY;"
					+ "SELECT DISTINCT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, "
					+ " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, "
					+ " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, "
					+ " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,A.T3,  "
					+ " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, "
					+ " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM "
					+ l_IBTTableName
					+ " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=0");
			preparewhereClause(l_query, pCri, 0);
		}

		PreparedStatement l_pstmt = aConn.prepareStatement(l_query
				.toString());
		ResultSet l_rs = l_pstmt.executeQuery();
		while (l_rs.next()) {
			mCurrentIBTInData = new CurrentIBTInData();
			readRecordIn(mCurrentIBTInData, l_rs, "Join");
			if (pCri.TransType.equalsIgnoreCase("1,3,9") && pCri.pType != 3) {
				if (l_IBTTableName.equals("CurrentIBTOut")
						&& mCurrentIBTInData.getT3().equalsIgnoreCase(
								pCri.pBranch)) {
					lstCurrentIBTOutData.add(mCurrentIBTInData);
				} else if (l_IBTTableName.equals("CurrentIBTIn")
						&& !mCurrentIBTInData.getT3().equalsIgnoreCase(
								pCri.pBranch)) {
					lstCurrentIBTOutData.add(mCurrentIBTInData);
				}
			} else {
				lstCurrentIBTOutData.add(mCurrentIBTInData);
			}

		}
		
		return lstCurrentIBTOutData;
	}
	
		//mmm, in /out 
		public CurrentIBTInData getCurrentIBTTransactionData(int pType,CurrentIBTInData pData,Connection aConn) throws SQLException {
				
			StringBuilder l_query = new StringBuilder();
			if(pType==1){
				l_TableName="CurrentIBTIn";
				l_query.append( "SET DateFormat DMY;" +
						  "SELECT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
						  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
						  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
						  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1,'1' AS T3, " +
						  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
						  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
						  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=0");
			}
			else if(pType==2){
				l_TableName="CurrentIBTOut";
				l_query.append( "SET DateFormat DMY;" +
						  "SELECT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
						  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
						  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
						  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1, '0' AS T3, " +
						  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
						  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
						  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 AND A.N1=0");
			}else{
				l_TableName="CurrentIBTOut";
				l_query.append( "SET DateFormat DMY;" +
						  "SELECT A.MainKey,A.AutoKey, A.IBTType, A.TransType, A.TransDate,  A.Authorisecode, A.OnlineSerialNo, " +
						  " A.CrAccNumber, A.DrAccNumber, A.FromBranch, A.ToBranch, A.Amount, A.Transref, A.Transno, " +
						  " A.PreviousBal, A.Status, A.ChequeNo,  A.ToName, A.ToNrc, A.SendTime, A.ReceiveTime,  A.FromName, " +
						  " A.FromNrc, A.PrintStatus, A.SaveTime,  A.PostTime, A.CompleteTime, A.Byear, A.Remark, A.T1, '0' AS T3, " +
						  " A.N1, A.AutoKey,  A.OnlSno, A.TestKey,  A.Prefixed, A.PfixedDate, A.CurrencyCode, A.CurrencyRate, B.Amount  As IBTCom, B.N1 As OnlineFees, " +
						  " B.CrAccNumber As IBTGL ,B.T2 As OnlFeesGL,A.T2 As VNo,B.DrAccNumber AS DrIBTGL,B.T1 AS DrOnlFeesGL,B.Transref AS ComTransRef,A.N2 As N2,A.N3 As N3,A.N4 As N4 FROM " + l_TableName + 
						  " A LEFT JOIN IBTComLog B ON A.OnlSno=B.OnlSno AND A.Byear=B.BYear AND A.IBTType=B.IBType AND A.ToBranch=B.ToBranch AND A.FromBranch =B.FromBranch WHERE 1=1 ");
			}
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
				l_query.append(" AND A.Status = "+pData.getStatus());
			}
			PreparedStatement l_pstmt=aConn.prepareStatement(l_query.toString());
			ResultSet l_rs=l_pstmt.executeQuery();
			while(l_rs.next()) {
				mCurrentIBTInData =new CurrentIBTInData();
				readRecordIn(mCurrentIBTInData, l_rs,"Join");				
			}
				
			
			return mCurrentIBTInData;
		}
		//mmm
		public static void readRecordIn(CurrentIBTInData pCurrentIBTInData , ResultSet pRS,String pType) throws SQLException{		
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			pCurrentIBTInData.setMainKey(pRS.getString("MainKey"));
			pCurrentIBTInData.setAutoKey(pRS.getLong("AutoKey"));				
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
			pCurrentIBTInData.setN2(pRS.getInt("N2"));		//nlkm for Offline
			pCurrentIBTInData.setOnlSno(pRS.getString("OnlSno"));
			pCurrentIBTInData.setTestKey(pRS.getString("TestKey"));
			pCurrentIBTInData.setPrefixed(pRS.getInt("Prefixed"));
			pCurrentIBTInData.setPfixedDate(pRS.getString("PfixedDate"));	
			pCurrentIBTInData.setCurrencyCode(pRS.getString("CurrencyCode"));
			pCurrentIBTInData.setCurrencyRate(pRS.getDouble("CurrencyRate"));
			pCurrentIBTInData.setT3(pRS.getString("T3")); //In , Out
			if(!pType.equals("")){
				pCurrentIBTInData.setIBTComission(pRS.getDouble("IBTCom"));
				pCurrentIBTInData.setOnlineFees(pRS.getDouble("OnlineFees"));
				pCurrentIBTInData.setComGl(pRS.getString("IBTGL"));
				pCurrentIBTInData.setOnlineFeesGl(pRS.getString("OnlFeesGL"));
				pCurrentIBTInData.setT2(pRS.getString("VNo"));
				pCurrentIBTInData.setDrComGl(pRS.getString("DrIBTGL"));
				pCurrentIBTInData.setDrOnlineFeesGl(pRS.getString("DrOnlFeesGL"));
				pCurrentIBTInData.setpComTransRef(pRS.getInt("ComTransRef"));
				pCurrentIBTInData.setN2(pRS.getInt("N2"));		//nlkm for Offline
				pCurrentIBTInData.setN3(pRS.getInt("N3"));
				pCurrentIBTInData.setN4(pRS.getInt("N4"));
			}
					
		}
		
		public boolean updateCurrentIBTOutForPost(CurrentIBTInData pData, Connection pConn) throws SQLException{
			boolean l_result = false;
			String l_query ="Update CurrentIBTOut Set Status=? where onlSno=? and BYear=? and IBTType=?";
			PreparedStatement pstmt = pConn.prepareStatement(l_query);
			pstmt.setInt(1, pData.getStatus());
			pstmt.setString(2, pData.getOnlSno());
			pstmt.setString(3,pData.getByear());
			pstmt.setShort(4,pData.getIBTType());
			l_result = pstmt.executeUpdate() > 0 ? true : false;
			return l_result;
		}

}

