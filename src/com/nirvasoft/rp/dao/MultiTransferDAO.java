package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.core.ccbs.LogicMgr;
import com.nirvasoft.rp.core.ccbs.data.db.DBDayBookExportClearingMgr;
import com.nirvasoft.rp.core.util.StrUtil;
import com.nirvasoft.rp.shared.Enum.DateFilterOperator;
import com.nirvasoft.rp.shared.MultiTransferCriteriaData;
import com.nirvasoft.rp.shared.MultiTransferData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.SharedUtil;

public class MultiTransferDAO {
	private ArrayList<MultiTransferData> lstMultiTransferData;
	private MultiTransferData MultiTransferDataBean;
	
	public void setMultiTransferBean(MultiTransferData aMultiTransferDataBean) {
		MultiTransferDataBean = aMultiTransferDataBean;
	}
	
	public MultiTransferData getCheckIssueBean() {
		return MultiTransferDataBean;
	}
	
	public void setMultiTransferBeanList(ArrayList<MultiTransferData> aMultiTransferBeanList) {
		lstMultiTransferData = aMultiTransferBeanList;
	}
	
	public ArrayList<MultiTransferData> getMultiTransferBeanList() {
		return lstMultiTransferData;
	}
	
	public boolean addMultiTransfer(MultiTransferData pMultiTransferData, Connection conn) throws SQLException{
		boolean result=false;
		
		PreparedStatement pstmt=conn.prepareStatement("INSERT INTO MULTITRANSFER (TRANSFERNO, DRCR, SERIALNO, " +
				"TRANSDATE, ACCTYPE, ACCNUMBER, COMMACCNUMBER, CHEQUENO, AMOUNT,COMMISSION, [NAME], NRC, BALANCE, " +
				"STATUS, TRANSREF, SUBREF, SYSTEMCODE, NARRATION, T1, T2, T3, N1, N2, N3, CURRENCYCODE," +
				"BRANCHCODE,N4,N5,N6) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        updateRecord(pMultiTransferData, pstmt, true);	
        if(pstmt.executeUpdate()>0)
        	result=true;
        
        pstmt.close();        
    	
       return result;
	}
	
	public boolean deleteMultiTransfer(int pTransferNo, String pBCode, Connection conn){
		boolean result = false;
		try {
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM MultiTransfer WHERE [MultiTransfer].[TransferNo] = ? " +
					"AND [MultiTransfer].[BranchCode] = ? ");
			pstmt.setInt(1, pTransferNo);
			pstmt.setString(2, pBCode);
			pstmt.executeUpdate();
			pstmt.close();
			result = true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public boolean updateTransactionStatus(int pTransRef, int pStatus, int pTransferNo,String pBCode, Connection conn){
		boolean result = false;
		try {
			PreparedStatement pstmt = conn.prepareStatement("UPDATE [MultiTransfer] SET " +
									"TransRef = ?, Status = ? WHERE [MultiTransfer].[TransferNo] = ? AND [MultiTransfer].[BranchCode]=?");
			
			int i = 1;
			pstmt.setInt(i++, pTransRef);
			pstmt.setInt(i++, pStatus);
			pstmt.setInt(i++, pTransferNo);
			pstmt.setString(i++, pBCode);
			pstmt.executeUpdate();
			pstmt.close();
			result = true;
			
		} catch (SQLException e) {
			
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean updateMultiTransferStatus(int pTransferNo,String pBCode, int pStatus, Connection conn) throws SQLException{
		boolean result = false;
		
		PreparedStatement pstmt = conn.prepareStatement("UPDATE [MultiTransfer] SET" +
								" Status = ? WHERE [MultiTransfer].[TransferNo] = ? AND [MultiTransfer].[BranchCode]= ?");

		int i = 1;
		pstmt.setInt(i++, pStatus);
		pstmt.setInt(i++, pTransferNo);
		pstmt.setString(i++, pBCode);
		pstmt.executeUpdate();
		pstmt.close();
		result = true;
		
		return result;
	}
	
	public ArrayList<MultiTransferData> getMultiTransfers(MultiTransferCriteriaData pCri, Connection conn) throws SQLException{
		ArrayList<MultiTransferData> ret = new ArrayList<MultiTransferData>();

		StringBuffer l_query = new StringBuffer();
		l_query.append("SELECT * FROM MultiTransfer WHERE 1=1 ");
		
		prepareWhereClause(pCri, l_query);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		prepareCriteria(pCri, pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			MultiTransferData object = new MultiTransferData();
			readRecord(object, rs, "GetDataList");
			String query = "select T1 from AccountGLTransaction WHERE transNo="+object.getTransRef();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet l_rs = stmt.executeQuery();
			if(l_rs.next())
			{
				object.setVNo(l_rs.getString("T1"));

			}
			ret.add(object);
		}
		
		return ret;
	}
	
	public int getMaxTransferNo(String pBCode, int pMultiTransferSetting, Connection conn) throws SQLException{
		int ret = 0;
		
		String l_Query = "SELECT ISNULL(MAX(TransferNo),0) AS MaxSerail FROM MultiTransfer WHERE 1=1 "; 
		if(pMultiTransferSetting==1){
			l_Query += " AND BranchCode='"+pBCode+"'";
		}
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next())
			ret = rs.getInt(1);
	
		pstmt.close();
			
		
		return ret;
	}
	
	public boolean existMultiTransfer(MultiTransferCriteriaData pCri, Connection conn) throws SQLException{
		boolean ret = false;
				
		StringBuffer l_query = new StringBuffer();
		l_query.append("SELECT COUNT([MultiTransfer].[TransferNo]) As Total_Records "
						+ "FROM [MultiTransfer] "
						+ "WHERE 1=1");
		
		prepareWhereClause(pCri, l_query);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		prepareCriteria(pCri, pstmt);		
		ResultSet rs = pstmt.executeQuery();		
		while (rs.next())
			ret = rs.getInt(1) > 0 ? true : false;
				
		
		return ret;
	}
	
	public MultiTransferData getTotalAmount(String pAccNumber, int pTransferNo, int pDrCr, Connection conn) throws SQLException{
		MultiTransferData ret = new MultiTransferData();
		
		PreparedStatement pstmt = conn.prepareStatement("SELECT AccNumber, SUM(Amount) As TotalAmount " +
								"From MultiTransfer WHERE TransferNO = ? AND DrCr = ? And AccNumber = ? Group By AccNumber ");
		
		pstmt.setInt(1, pTransferNo);
		pstmt.setInt(2, pDrCr);
		pstmt.setString(3, pAccNumber);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next())
			readRecord(ret, rs, "GetTotalAmount");
		
		
		return ret;
	}
	
	public ArrayList<MultiTransferData> getBrowserDatas(MultiTransferCriteriaData pCri, Connection conn){
		ArrayList<MultiTransferData> ret = new ArrayList<MultiTransferData>();
		try {
			StringBuffer l_query = new StringBuffer();
			l_query.append("SELECT [MultiTransfer].[TransferNo], [MultiTransfer].[TransDate], "
                        + "SUM([MultiTransfer].[Amount]) AS TotalAmount, [MultiTransfer].[Status], "
                        + "[MultiTransfer].[CurrencyCode],[MultiTransfer].[BranchCode]  FROM dbo.[MultiTransfer] "
                        + "WHERE [MultiTransfer].[DrCr] = '1' GROUP BY [MultiTransfer].[TransferNo]," 
                        + "[MultiTransfer].[TransDate], [MultiTransfer].[Status],[MultiTransfer].[CurrencyCode]," 
                        + "[MultiTransfer].[SystemCode],[MultiTransfer].[BranchCode] "
                        + "HAVING 1=1");
			
			prepareWhereClause(pCri, l_query);	
			l_query.append(" order by transferno desc");
			PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
			prepareCriteria(pCri, pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				MultiTransferData obj = new MultiTransferData();
				readRecord(obj, rs, "GetBrowserData");
				ret.add(obj);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return ret;
	}
	
	
	private void prepareWhereClause(MultiTransferCriteriaData aMultiTransferCriteriaBean, StringBuffer aQuery){
		if (aMultiTransferCriteriaBean.TransferNo != 0)
			aQuery.append(" AND [TransferNo] = ?");
		
		if (!aMultiTransferCriteriaBean.AccNumber.equals(""))
			aQuery.append(" AND [AccNumber] = ?");
		
		if (aMultiTransferCriteriaBean.SerialNo!= 0)
			aQuery.append(" AND [SerialNo] = ?");
		
		if (aMultiTransferCriteriaBean.DrCr != 255)
			aQuery.append(" AND [DrCr] = ?");
		
		if (aMultiTransferCriteriaBean.Status != 255)
			aQuery.append(" AND [Status] = ?");
		
		
		/*if (aMultiTransferCriteriaBean.SystemCode != 255){
			aQuery.append(" AND [SystemCode] = ?");
		}*/
		
		if (!aMultiTransferCriteriaBean.FromDate.equals("")){
			if(aMultiTransferCriteriaBean.FromDateOperator == DateFilterOperator.GreaterThanEqual)
				aQuery.append(" And CONVERT(VARCHAR(10),TransDate,111)>=? ");
			else 
				aQuery.append(" And CONVERT(VARCHAR(10),TransDate,111)=? ");
		}
		
		if (!aMultiTransferCriteriaBean.ToDate.equals("")){
			if(aMultiTransferCriteriaBean.FromDateOperator == DateFilterOperator.GreaterThanEqual)
				aQuery.append(" And CONVERT(VARCHAR(10),TransDate,111)<=?");
			else 
				aQuery.append(" And CONVERT(VARCHAR(10),TransDate,111)=?");
		}
		
		 if (aMultiTransferCriteriaBean.Amount != 0) {
			 aQuery.append(" AND SUM([MultiTransfer].[Amount]) >= ?");
	    }
		 if (aMultiTransferCriteriaBean.SystemCode != 255) {
			 aQuery.append(" AND [SystemCode] = ?");
	    }
		 if (aMultiTransferCriteriaBean.TransRef != 255) {
			 aQuery.append(" AND [TransRef] = ?");
	    }
		if(!aMultiTransferCriteriaBean.BranchCode.equals("") && !aMultiTransferCriteriaBean.BranchCode.equals("999") ) {
			aQuery.append(" AND [BranchCode] = ? ");
		}
			
	}
	
	private void prepareCriteria(MultiTransferCriteriaData aMultiTransferCriteriaBean, PreparedStatement aPS) throws SQLException{
		int l_paraIndex = 0;
		if (aMultiTransferCriteriaBean.TransferNo != 0){
			l_paraIndex +=1;
			aPS.setInt(l_paraIndex, aMultiTransferCriteriaBean.TransferNo);
		}
		if (!aMultiTransferCriteriaBean.AccNumber.equals("")){
			l_paraIndex +=1;
			aPS.setString(l_paraIndex, aMultiTransferCriteriaBean.AccNumber);
		}
		if (aMultiTransferCriteriaBean.SerialNo!= 0) {
			l_paraIndex +=1;
			aPS.setInt(l_paraIndex, aMultiTransferCriteriaBean.SerialNo);
		}
		if (aMultiTransferCriteriaBean.DrCr != 255){
			l_paraIndex +=1;
			aPS.setInt(l_paraIndex, aMultiTransferCriteriaBean.DrCr);
		}
		/*if (aMultiTransferCriteriaBean.SystemCode != 255){
			l_paraIndex +=1;
			aPS.setInt(l_paraIndex, aMultiTransferCriteriaBean.SystemCode);
		}*/
		if (aMultiTransferCriteriaBean.Status != 255){
			l_paraIndex +=1;
			aPS.setInt(l_paraIndex, aMultiTransferCriteriaBean.Status);
		}
		if (!aMultiTransferCriteriaBean.FromDate.equals("")) {
	        l_paraIndex += 1;
	        aPS.setString(l_paraIndex, aMultiTransferCriteriaBean.FromDate);
	    }
	        
	    if (!aMultiTransferCriteriaBean.ToDate.equals("")) {
	    	l_paraIndex += 1;
	        aPS.setString(l_paraIndex, aMultiTransferCriteriaBean.ToDate);
	    }
	    if (aMultiTransferCriteriaBean.Amount != 0) {
	    	l_paraIndex += 1;
	        aPS.setDouble(l_paraIndex, aMultiTransferCriteriaBean.Amount);
	    }
	    if (aMultiTransferCriteriaBean.SystemCode != 255) {
	    	l_paraIndex += 1;
	        aPS.setInt(l_paraIndex, aMultiTransferCriteriaBean.SystemCode);
	    }
	    if (aMultiTransferCriteriaBean.TransRef != 255) {
	    	l_paraIndex += 1;
	        aPS.setLong(l_paraIndex, aMultiTransferCriteriaBean.TransRef);
	    }
	    if(!aMultiTransferCriteriaBean.BranchCode.equals("")  && !aMultiTransferCriteriaBean.BranchCode.equals("999")) {
	    	l_paraIndex += 1;
	    	aPS.setString(l_paraIndex, aMultiTransferCriteriaBean.BranchCode);
	    }
	}
	
	private void updateRecord(MultiTransferData aMultiTransferDataBean, PreparedStatement aPS, boolean aIsNewRecord) throws SQLException{		
		int i = 1;
		if (aIsNewRecord){
			aPS.setInt(i++, aMultiTransferDataBean.getTransferNo());		
			aPS.setInt(i++, aMultiTransferDataBean.getDrCr());
			aPS.setInt(i++, aMultiTransferDataBean.getSerialNo());		
			aPS.setString(i++, aMultiTransferDataBean.getTransDate());
			aPS.setString(i++, aMultiTransferDataBean.getAccType());		
			aPS.setString(i++, aMultiTransferDataBean.getAccountNumber());
			aPS.setString(i++, aMultiTransferDataBean.getCommAccNumber());		
			aPS.setString(i++, aMultiTransferDataBean.getChequeNo());
			aPS.setDouble(i++, aMultiTransferDataBean.getAmount());		
			aPS.setDouble(i++, aMultiTransferDataBean.getCommission());
			aPS.setString(i++, aMultiTransferDataBean.getName());		
			aPS.setString(i++, aMultiTransferDataBean.getNRC());
			aPS.setDouble(i++, aMultiTransferDataBean.getBalance());		
			aPS.setInt(i++, aMultiTransferDataBean.getStatus());
			aPS.setInt(i++, aMultiTransferDataBean.getTransRef());		
			aPS.setString(i++, aMultiTransferDataBean.getSubRef());
			aPS.setInt(i++, aMultiTransferDataBean.getSystemCode());		
			aPS.setString(i++, aMultiTransferDataBean.getNarration());
			aPS.setString(i++, aMultiTransferDataBean.getT1());		
			aPS.setString(i++, aMultiTransferDataBean.getT2());
			aPS.setString(i++, aMultiTransferDataBean.getT3());		
			aPS.setDouble(i++, aMultiTransferDataBean.getN1());
			aPS.setDouble(i++, aMultiTransferDataBean.getN2());
			aPS.setDouble(i++, aMultiTransferDataBean.getN3());
			aPS.setString(i++, aMultiTransferDataBean.getCurrencyCode());
			aPS.setString(i++, aMultiTransferDataBean.getBranchCode());
			aPS.setDouble(i++, aMultiTransferDataBean.getN4());
			aPS.setInt(i++, aMultiTransferDataBean.getUniversalselectmonth());
			aPS.setDouble(i++, aMultiTransferDataBean.getN6());
		}		
	}
	
	private void readRecord(MultiTransferData aMultiTransferBean,ResultSet aRS, String aFrom) throws SQLException
	{		
		if (aFrom.equals("GetDataList")){
			aMultiTransferBean.setTransferNo(aRS.getInt("TransferNo"));
			aMultiTransferBean.setDrCr(aRS.getByte("DrCr"));
			aMultiTransferBean.setSerialNo(aRS.getInt("SerialNo"));
			aMultiTransferBean.setTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("TransDate")));
			aMultiTransferBean.setAccType(aRS.getString("AccType"));
			aMultiTransferBean.setAccountNumber(aRS.getString("AccNumber"));
			aMultiTransferBean.setCommAccNumber(aRS.getString("CommAccNumber") != null ? aRS.getString("CommAccNumber") : "");
			aMultiTransferBean.setChequeNo(aRS.getString("ChequeNo") != null ? aRS.getString("ChequeNo") : "");
			aMultiTransferBean.setAmount(aRS.getDouble("Amount"));
			aMultiTransferBean.setCommission(aRS.getDouble("Commission"));
			aMultiTransferBean.setName(aRS.getString("Name") != null ? aRS.getString("Name") : "");
			aMultiTransferBean.setNRC(aRS.getString("NRC") != null ? aRS.getString("NRC") : "");
			aMultiTransferBean.setBalance(aRS.getDouble("Balance"));
			aMultiTransferBean.setStatus(aRS.getInt("Status"));
			aMultiTransferBean.setTransRef(aRS.getInt("TransRef"));
			aMultiTransferBean.setSubRef(aRS.getString("SubRef") != null ? aRS.getString("SubRef") : "");
			aMultiTransferBean.setSystemCode(aRS.getInt("SystemCode"));
			aMultiTransferBean.setNarration(aRS.getString("Narration") != null ? aRS.getString("Narration") : "");
			aMultiTransferBean.setT1(aRS.getString("t1") != null ? aRS.getString("t1") : "");
			aMultiTransferBean.setT2(aRS.getString("t2") != null ? aRS.getString("t2") : "");
			aMultiTransferBean.setT3(aRS.getString("t3") != null ? aRS.getString("t3") : "");
			aMultiTransferBean.setN1(aRS.getInt("n1"));
			aMultiTransferBean.setN2(aRS.getInt("n2"));
			aMultiTransferBean.setN3(aRS.getInt("n3"));
			aMultiTransferBean.setN4(aRS.getDouble("n4"));
			aMultiTransferBean.setCurrencyCode(aRS.getString("CurrencyCode"));
			aMultiTransferBean.setBranchCode(aRS.getString("BranchCode"));
		} else if (aFrom.equals("GetTotalAmount")){
			aMultiTransferBean.setAccountNumber(aRS.getString("AccNumber"));
			aMultiTransferBean.setAmount(aRS.getDouble("TotalAmount"));
		} else if (aFrom.equals("GetBrowserData")){
			aMultiTransferBean.setTransferNo(aRS.getInt("TransferNo"));
			aMultiTransferBean.setTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("TransDate")));
			aMultiTransferBean.setAmount(aRS.getDouble("TotalAmount"));
			aMultiTransferBean.setStatus(aRS.getInt("Status"));
			aMultiTransferBean.setCurrencyCode(aRS.getString("CurrencyCode"));
			aMultiTransferBean.setBranchCode(aRS.getString("BranchCode"));
		}
		
		if (aFrom.equals("GetDataList")){
			aMultiTransferBean.setUniversalselectmonth(aRS.getInt("n5"));
		}
			
		
	}
	
	public String isExistPO(String pPONo,String pBCode,String pCurrencyCode,String pBYear,String pPOType,Connection pConn) throws SQLException {
		String pAccNumber = "";
		StringBuffer l_query = new StringBuffer();
		
		l_query.append("SELECT AccNumber FROM Multitransfer Where ChequeNo='" + pPONo + "' AND AccType =" + pPOType + " AND BranchCode= '" + pBCode 
				+ "' AND CurrencyCode='" + pCurrencyCode + "' AND t2='"+pBYear+"' AND Status=2 AND DrCr=2 ");
	
		PreparedStatement pstmt = pConn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			pAccNumber = rs.getString(1);
		}
		return pAccNumber;
	}
	
	public ArrayList<MultiTransferData> getBrowserDataSearchs(String pCondition, Connection conn){
		ArrayList<MultiTransferData> ret = new ArrayList<MultiTransferData>();
		try {
			StringBuffer l_query = new StringBuffer();
			l_query.append("SELECT [MultiTransfer].[TransferNo], [MultiTransfer].[TransDate], "
                        + "SUM([MultiTransfer].[Amount]) AS TotalAmount, [MultiTransfer].[Status], "
                        + "[MultiTransfer].[CurrencyCode] FROM dbo.[MultiTransfer] "
                        + "WHERE [MultiTransfer].[DrCr] = '1' GROUP BY [MultiTransfer].[TransferNo]," +
                        " [MultiTransfer].[TransDate], [MultiTransfer].[Status],[MultiTransfer].[CurrencyCode],[MultiTransfer].[SystemCode] "
                        + "HAVING 1=1");
			
			l_query.append(pCondition);
			l_query.append(" order by transferno desc");
			PreparedStatement pstmt = conn.prepareStatement(l_query.toString());			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				MultiTransferData obj = new MultiTransferData();
				readRecord(obj, rs, "GetBrowserData");
				ret.add(obj);
			}			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return ret;
	}
	
	public ArrayList<MultiTransferData> getCrMultiTransferData(MultiTransferCriteriaData pCri,Connection pConn) {
		ArrayList<MultiTransferData> ret = new ArrayList<MultiTransferData>();
		StringBuffer l_query = new StringBuffer();
		try {
			l_query.append("SELECT TransferNo,SerialNo,AccNumber,Narration,Amount FROM dbo.MultiTransfer WHERE DrCr=2");
			prepareWhereClause(pCri, l_query);			
			PreparedStatement pstmt;		
			pstmt = pConn.prepareStatement(l_query.toString());		
			prepareCriteria(pCri, pstmt);		
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				MultiTransferData obj = new MultiTransferData();
				obj.setTransferNo(rs.getInt("TransferNo"));
				obj.setSerialNo(rs.getInt("SerialNo"));
				obj.setAccountNumber(rs.getString("AccNumber"));
				obj.setNarration(rs.getString("Narration"));
				obj.setAmount(rs.getDouble("Amount"));
				ret.add(obj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public ArrayList<MultiTransferData> getCrMultiTransferData(String pCondition,Connection pConn) {
		ArrayList<MultiTransferData> ret = new ArrayList<MultiTransferData>();
		StringBuffer l_query = new StringBuffer();
		try {
			l_query.append("SELECT TransferNo,SerialNo,AccNumber,Narration,Amount FROM dbo.MultiTransfer WHERE DrCr=2 ");
			l_query.append(pCondition);			
			PreparedStatement pstmt;		
			pstmt = pConn.prepareStatement(l_query.toString());		
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				MultiTransferData obj = new MultiTransferData();
				obj.setTransferNo(rs.getInt("TransferNo"));
				obj.setSerialNo(rs.getInt("SerialNo"));
				obj.setAccountNumber(rs.getString("AccNumber"));
				obj.setNarration(rs.getString("Narration"));
				obj.setAmount(rs.getDouble("Amount"));
				ret.add(obj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getMaxTransferNo(Connection conn){
		int ret = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(TransferNo) AS MaxSerail FROM MultiTransfer");
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
				ret = rs.getInt(1);
		
			pstmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return ret;
	}
	
	public int getOnlSno(String pBYear, Connection pConn) throws SQLException{
		int l_DrOnlineSerialNo = 0;
		String l_query = "";		
		//N5 Online Serial No
		//T5 Budget Year
		l_query = "SELECT MAX(N5) FROM Multitransfer " +
				"WHERE T1 = ? ";			
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setString(1, pBYear);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){	
			l_DrOnlineSerialNo = rs.getInt(1);
		}
				
		return l_DrOnlineSerialNo;
	}
	
	//Clearing Export To HO
	public static ArrayList<MultiTransferData> DayBookExportClearing(String ip, String userID,String tsf, String tst,
			String dBDate, String bCode,  String ccyCode, String ps, String rf, String rb,Connection conn) throws SQLException{
		ArrayList<MultiTransferData> dataList=new ArrayList<MultiTransferData>();
		String pCri="";
		if(bCode.equalsIgnoreCase("ALL")) pCri+=" ";
		else pCri+=" and BranchCode='"+bCode+"'";
		if(ccyCode.equalsIgnoreCase("ALL")) pCri+=" ";
		else pCri+=" and CurrencyCode='"+ccyCode+"'";
		
		String l_Query="Select TransferNo,DrCr,SerialNo,Transdate,isnull(AccType,''), isnull(AccNumber,''),isnull(CommAccNumber,''), "
				+ "isnull(ChequeNo,''),isnull(Amount,0.0), isnull(Commission,0.0), isnull(Name,''), isnull(NRC,''), isnull(Balance,0.0),"
				+ " isnull(Status,0), isnull(TransRef,0), isnull(SubRef,''), isnull(SystemCode,0), isnull(Narration,''), "
				+ "isnull(t1,'Save'), isnull(t2,'Clearing'), isnull(t3,''),isnull(n1,0.0), isnull(n2,0.0),isnull(n3,0.0),"
				+ " isnull(CurrencyCode,''),isnull(BranchCode,''), "
				+ " isnull(t4,''), isnull(t5,''), isnull(t6,''), isnull(n4,0), isnull(n5,0), isnull(n6,0) "
				+ "From MultiTransfer "
				+ "Where Systemcode=1 And TransDate='"+dBDate+"' "+pCri;
;
		PreparedStatement pstmt;
		ResultSet rs = null;
		pstmt = conn.prepareStatement(l_Query);
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			MultiTransferData data=new MultiTransferData();
			data.setTransferNo(rs.getInt(1));
			data.setDrCr(rs.getByte(2));
			data.setSerialNo(rs.getInt(3));
			data.setTransDate(rs.getString(4));
			data.setAccType(rs.getString(5));
			data.setAccountNumber(rs.getString(6));
			data.setCommAccNumber(rs.getString(7));
			data.setChequeNo(rs.getString(8));
			data.setAmount(rs.getDouble(9));
			data.setCommission(rs.getDouble(10));
			
			data.setName(rs.getString(11));
			data.setNRC(rs.getString(12));
			data.setBalance(rs.getDouble(13));
			data.setStatus(rs.getInt(14));
			data.setTransRef(rs.getInt(15));
			data.setSubRef(rs.getString(16));
			data.setSystemCode(rs.getInt(17));
			data.setNarration(rs.getString(18));
			data.setT1(rs.getString(19));
			data.setT2(rs.getString(20));
			
			data.setT3(rs.getString(21));
			data.setN1(rs.getDouble(22));
			data.setN2(rs.getDouble(23));
			data.setN3(rs.getDouble(24));
			data.setCurrencyCode(rs.getString(25));
			data.setBranchCode(rs.getString(26));
			data.setT4(rs.getString(27));
			data.setT5(rs.getString(28));
			data.setT6(rs.getString(29));
			data.setN4(rs.getInt(30));
			data.setN5(rs.getInt(31));
			data.setN6(rs.getInt(32));
			dataList.add(data);
		}
		
		pstmt.close();
        rs.close();      
        return dataList;
	}

	public static void saveDayBookExportClearing(String ip, String userID,String tsf, String tst,
			String dBDate, String bCode,String ccyCode, String ps, String rf, String rb,
			ArrayList<MultiTransferData> dataList,Connection conn) throws SQLException{
		String pCri="";
		if(bCode.equalsIgnoreCase("ALL")) pCri+=" ";
		else pCri+=" and BranchCode='"+bCode+"'";
		if(ccyCode.equalsIgnoreCase("ALL")) pCri+=" ";
		else pCri+=" and CurrencyCode='"+ccyCode+"'";
		
		String l_Query="delete from MultiTransfer where TransDate=convert(datetime,('"+dBDate+"')) "+pCri;
		PreparedStatement pstmt;
		pstmt = conn.prepareStatement(l_Query);
		pstmt.executeUpdate();
		
		l_Query="insert into MultiTransfer values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(l_Query);
		for(MultiTransferData data : dataList){
			pstmt.setInt(1,data.getTransferNo());
			pstmt.setByte(2,data.getDrCr());
			pstmt.setInt(3,data.getSerialNo());
			pstmt.setString(4,data.getTransDate());
			pstmt.setString(5,data.getAccType());
			pstmt.setString(6,data.getAccountNumber());
			pstmt.setString(7,data.getCommAccNumber());
			pstmt.setString(8,data.getChequeNo());
			pstmt.setDouble(9,data.getAmount());
			pstmt.setDouble(10,data.getCommission());
			
			pstmt.setString(11,data.getName());
			pstmt.setString(12,data.getNRC());
			pstmt.setDouble(13,data.getBalance());
			pstmt.setInt(14,data.getStatus());
			pstmt.setInt(15,data.getTransRef());
			pstmt.setString(16,data.getSubRef());
			pstmt.setInt(17,data.getSystemCode());
			pstmt.setString(18,data.getNarration());
			pstmt.setString(19,data.getT1());
			pstmt.setString(20,data.getT2());
			
			pstmt.setString(21,data.getT3());
			pstmt.setDouble(22,data.getN1());
			pstmt.setDouble(23,data.getN2());
			pstmt.setDouble(24,data.getN3());
			pstmt.setString(25,data.getCurrencyCode());
			pstmt.setString(26,data.getBranchCode());
			pstmt.setString(27,data.getT4());
			pstmt.setString(28,data.getT5());
			pstmt.setString(29,data.getT6());
			pstmt.setDouble(30,data.getN4());
			pstmt.setDouble(31,data.getN5());
			pstmt.setDouble(32,data.getN6());
			pstmt.addBatch();			
		}
		pstmt.executeBatch();
		pstmt.close();       
	}

	public static void getDataBaseLink(Connection conn) throws Exception{
		
		PreparedStatement pstmt;
		String l_Query="select catalog,DataSource,userID,Password from linkdatabases where typeNo=3";
		ResultSet rs = null;
		pstmt = conn.prepareStatement(l_Query);
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			DBDayBookExportClearingMgr.Instance=rs.getString("DataSource");
			DBDayBookExportClearingMgr.URL=rs.getString("catalog");
			DBDayBookExportClearingMgr.UserName=rs.getString("userID");			
			DBDayBookExportClearingMgr.Password= StrUtil.decryptPIN(rs.getString("Password"));			
			//DBDayBookExportClearingMgr.Password= rs.getString("Password");
		}
		pstmt.executeBatch();
		pstmt.close();
		rs.close();
	}
	
	public ArrayList<MultiTransferData> getMobileVouTransaction(MultiTransferCriteriaData pCri,String pUID,Connection pConn){

		lstMultiTransferData = new ArrayList<MultiTransferData>();
		String l_TableName = "Tmpsuccess..RptMobileLocal"+pUID;
		String l_Cri="";
		String l_Cri1="";
		try {
			String l_Query = "If Exists (Select * From TmpSuccess..SysObjects " 
					+ "Where Id = Object_Id('"+l_TableName+"') And SysStat & " 
					+ "0xf = 3) " 
					+ "Drop Table "+l_TableName+"";
			PreparedStatement pstmt= pConn.prepareStatement(l_Query);				
			pstmt.executeUpdate();
			
			pCri.FromDate=StrUtil.formatMIT2DDMMYYYY(pCri.FromDate);
			if(!pCri.FromDate.equals("")) {
				l_Cri=" AND TransDate >= '" + pCri.FromDate + "'";
			}
			pCri.ToDate=StrUtil.formatMIT2DDMMYYYY(pCri.ToDate);
			if(!pCri.ToDate.equals("")) {
				l_Cri+=" AND TransDate <= '"+ pCri.ToDate + "'";
			}
			if(pCri.t1.equals("tba")) 
				l_Cri+=" AND t5=''";
			else
				l_Cri+=" AND t5=" + pCri.t1 + "";
			
			if(pCri.t2.equals("tba")) 
				l_Cri+=" AND t6=''";
			else
				l_Cri+=" AND t6=" + pCri.t2 + "";
			
			if(pCri.Amount != 0) {	//From amount
				l_Cri+=" AND Amount >=" + pCri.Amount;
			}
			if(pCri.Balance != 0) { //To amount
				l_Cri+=" AND Amount <=" + pCri.Balance;
			}
				
			if(!pCri.AccNumber.equals("")) {		//Dr
				l_Cri1=" AND A.AccNumber LIKE '" + pCri.AccNumber + "%'";
			}
			if(!pCri.CommAccNumber.equals("")) {		//Cr
				l_Cri1+=" AND B.AccNumber LIKE '" + pCri.CommAccNumber + "%'";
			}

			l_Query="SET DateFormat DMY;" +
			  "select A.SerialNo,A.TransDate,A.TransferNo,A.t2 As Onlsno,A.AccNumber As DrAccNumber,B.AccNumber As CrAccNumber,A.Amount," +
			  " A.ChequeNo,A.Name As DrName, A.NRC As DrNrc,B.Name As CrName, B.NRC As CrNrc,A.t1 As Byear,A.TransRef into " + l_TableName + " from " +
			  "(select * from " + pCri.t3 + " where SerialNo=1 And DrCr=1 " + l_Cri + ") a inner join (select * from " + pCri.t3 + 
			  " where SerialNo=1 And DrCr=2 " + l_Cri + ") b on a.transferno=b.transferno where 1=1 " + l_Cri1 ; 
			pstmt= pConn.prepareStatement(l_Query);				
			pstmt.executeUpdate();
			
			l_Query="SET DateFormat DMY; Insert Into " + l_TableName + 
			  " select A.SerialNo,A.TransDate,A.TransferNo,A.t2 As Onlsno,A.AccNumber As DrAccNumber,B.AccNumber As CrAccNumber,A.Amount," +
			  " A.ChequeNo,A.Name As DrName, A.NRC As DrNrc,B.Name As CrName, B.NRC As CrNrc,A.t1 As Byear,A.TransRef from " +
			  "(select * from " + pCri.t3 + " where SerialNo=2 And DrCr=1 " + l_Cri + ") a inner join (select * from " + pCri.t3 + 
			  " where SerialNo=2 And DrCr=2 " + l_Cri + ") b on a.transferno=b.transferno where 1=1 " + l_Cri1 ; 
			pstmt= pConn.prepareStatement(l_Query);				
			pstmt.executeUpdate();
			
			l_Query="select A.TransDate,A.TransferNo,A.Onlsno,A.DrAccNumber,A.CrAccNumber,isnull(B.CrAccNumber,'') As CrComAccNumber," +
					"A.Amount,isnull(B.Amount,0.00) As Commission,isnull(A.ChequeNo,'') ChequeNo,isnull(A.DrName,'') DrName, isnull(A.DrNrc,'') DrNrc," +
					"isnull(A.CrName,'') CrName, isnull(A.CrNrc,'') CrNrc,isnull(B.CrName,'') As ComCrName, isnull(A.CrNrc,'') As ComCrNrc,A.Byear,A.Transref from " +
					"(select * from " + l_TableName + " where SerialNo=1) a left join (select * from " + l_TableName + " where SerialNo=2) b " +
					"on a.transferno=b.transferno where 1=1 Order by A.TransDate";
			pstmt = pConn.prepareStatement(l_Query.toString());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				MultiTransferData data = new MultiTransferData();
				data.setTransDate(rs.getString("TransDate"));
				data.setTransferNo(rs.getInt("TransferNo"));
				data.setT2(rs.getString("Onlsno"));
				data.setAccountNumber(rs.getString("DrAccNumber"));
				data.setT4(rs.getString("CrAccNumber"));
				data.setCommAccNumber(rs.getString("CrComAccNumber"));
				data.setAmount(rs.getDouble("Amount"));
				data.setCommission(rs.getDouble("Commission"));
				data.setChequeNo(rs.getString("ChequeNo"));
				data.setName(rs.getString("DrName"));
				data.setNRC(rs.getString("DrNrc"));
				data.setT5(rs.getString("CrName"));
				data.setT6(rs.getString("CrNrc"));
				data.setT1(rs.getString("Byear"));
				data.setT3(rs.getString("ComCrName"));
				data.setTransRef(Integer.parseInt(rs.getString("Transref")));
				lstMultiTransferData.add(data);
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstMultiTransferData;
	}
	
	public boolean isSameCurrencyCheck(int pTransferNo, String pBCode,Connection pConn) throws SQLException{
		boolean isSameCurrency= false;
		int l_CurrencyCount = 0;
		int l_MuliTrasferSetting = LogicMgr.getSystemSettingT12N1("MLT");
		String l_query = "";		
		
		l_query = "SELECT Count(Distinct(CurrencyCode)) FROM MultiTransfer Where TransferNo= ? ";
		if(l_MuliTrasferSetting==1){
			l_query += " AND BranchCode='"+pBCode+"'";
		}
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setInt(1, pTransferNo);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){	
			l_CurrencyCount = rs.getInt(1);
		}
		if(l_CurrencyCount==1){
			isSameCurrency = true ;
		}
		return isSameCurrency;
	}
	
	
	public ReferenceData getCrDiffBranch(int pTransferNo, String pBCode,Connection pConn) throws SQLException{
		ReferenceData ret = new ReferenceData();		
		int l_MuliTrasferSetting = LogicMgr.getSystemSettingT12N1("MLT");
		String l_query = "";		
		
		l_query = "	SELECT SUM(CASE WHEN '"+pBCode+"'<>T.T5 THEN 1 ELSE 0 END),SUM(CASE WHEN '"+pBCode+"'<>T.T5 THEN a.Amount * a.N4 ELSE 0 END) Amt,SUM(CASE WHEN '"+pBCode+"'=T.T5 THEN 1 ELSE 0 END) FROM MultiTransfer a inner join t00005 t on a.AccNumber=T.t1 Where TransferNo= ? AND Drcr=1";
		if(l_MuliTrasferSetting==1){
			l_query += " AND BranchCode='"+pBCode+"'";
		}
				   
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setInt(1, pTransferNo);		
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			ret = new ReferenceData();
			ret.setN1(rs.getInt(1));
			ret.setN2(rs.getDouble(2));
			ret.setN3(rs.getDouble(3));
		}		
		return ret;
	}
	
	public ArrayList<ReferenceData> getCurrencyList(int pTransferNo, Connection pConn) throws SQLException{
		ArrayList<ReferenceData> lstRefData = new ArrayList<ReferenceData>();
		ReferenceData l_RefData = new ReferenceData();
		String l_query = "";		
		l_query = "SELECT  CurrencyCode,DrCr,SUM(Amount),Count(AccNumber) FROM MultiTransfer Where TransferNo=? Group By CurrencyCode,DrCr";	
		PreparedStatement pstmt = pConn.prepareStatement(l_query);
		pstmt.setInt(1, pTransferNo);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){	
			l_RefData = new ReferenceData();
			l_RefData.setCode(rs.getString(1));
			l_RefData.setKey(rs.getInt(2));
			l_RefData.setN1(rs.getDouble(3));
			l_RefData.setN2(rs.getInt(4));
		}
		return lstRefData;
	}
	
}


