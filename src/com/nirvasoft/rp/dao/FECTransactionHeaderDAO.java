package com.nirvasoft.rp.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.core.SingletonServer;
import com.nirvasoft.rp.core.ccbs.LogicMgr;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.Enum.DateFilterOperator;
import com.nirvasoft.rp.shared.Enum.NumFilterOperator;
import com.nirvasoft.rp.shared.Enum.TxTFilterOperator;
import com.nirvasoft.rp.shared.CustomerData;
import com.nirvasoft.rp.shared.CustomerPhoto;
import com.nirvasoft.rp.shared.FECCriteriaData;
import com.nirvasoft.rp.shared.FECTransactionDetailsData;
import com.nirvasoft.rp.shared.FECTransactionLimitData;
import com.nirvasoft.rp.shared.LinkDatabasesCriteriaData;
import com.nirvasoft.rp.shared.Result;
import com.nirvasoft.rp.shared.SharedUtil;
import com.nirvasoft.rp.shared.FECTransactionHeaderData;
import com.nirvasoft.rp.shared.FECTransactionHeaderDataSet;
import com.nirvasoft.rp.shared.TempData;

public class FECTransactionHeaderDAO {

	private ArrayList<FECTransactionHeaderData> lstObjTransactionHeader;
	private FECTransactionHeaderData objTransactionHeaderData;
	private String mDBName = "";
	public void setLstObjTransactionHeader(ArrayList<FECTransactionHeaderData> lstObjTransactionHeader) {
		this.lstObjTransactionHeader = lstObjTransactionHeader;
	}
	public ArrayList<FECTransactionHeaderData> getLstObjTransactionHeader() {
		return lstObjTransactionHeader;
	}
	public void setObjTransactionHeaderData(FECTransactionHeaderData objTransactionHeaderData) {
		this.objTransactionHeaderData = objTransactionHeaderData;
	}
	public FECTransactionHeaderData getObjTransactionHeaderData() {
		return objTransactionHeaderData;
	}
	public String getDBName() {
		return mDBName;
	}

	public void setDBName(String mDBName) {
		this.mDBName = mDBName;
	}
	
	public FECTransactionHeaderDAO()
	{
		lstObjTransactionHeader = new ArrayList<FECTransactionHeaderData>();
		objTransactionHeaderData = new FECTransactionHeaderData();
		
	}
	public FECTransactionHeaderDAO(Connection pConn)
	{
		lstObjTransactionHeader = new ArrayList<FECTransactionHeaderData>();
		objTransactionHeaderData = new FECTransactionHeaderData();
		LinkDatabasesDAO l_LinkDatabasesDAO = new LinkDatabasesDAO();
		LinkDatabasesCriteriaData l_LinkCriData = new LinkDatabasesCriteriaData();
		l_LinkCriData.TypeNo = 4;
		try {
			setDBName(l_LinkDatabasesDAO.getLinkDataBase(l_LinkCriData, pConn).getDataSource());
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public FECTransactionHeaderData Find(Connection aConn) throws SQLException
	{
		FECTransactionHeaderData pTrHeaderData = null;
		String qQuery = "Select * From TransactionHeader "+
		" Where HeaderNo = " + objTransactionHeaderData.getHeaderNo();
		
		PreparedStatement pstmt = aConn.prepareStatement(qQuery);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next())
		{
			pTrHeaderData = new FECTransactionHeaderData();
			ReadRecord(pTrHeaderData, rs);
		}
		pstmt.close();
		
		return pTrHeaderData;
	}
	
	private void ReadRecord(FECTransactionHeaderData aTrHeader, ResultSet aRS) throws SQLException
	{
		
		aTrHeader.setAutokey(aRS.getLong("autokey"));
		aTrHeader.setBranchCode(aRS.getString("BranchCode"));
		aTrHeader.setCauseDescription(aRS.getString("CauseDescription"));
		aTrHeader.setCustomerID(aRS.getString("CustomerID"));
		aTrHeader.setName(aRS.getString("Name"));
		aTrHeader.setTitle(aRS.getString("Title"));
		aTrHeader.setNrcNo(aRS.getString("NrcNo"));
		aTrHeader.setAddress(aRS.getString("Address"));
		aTrHeader.setSex(aRS.getInt("Sex"));
		aTrHeader.setPhone(aRS.getString("Phone"));
		aTrHeader.setEmail(aRS.getString("Email"));
		aTrHeader.setDenoType(aRS.getShort("DenoType"));
		aTrHeader.setExchangeType(aRS.getShort("ExchangeType"));
		aTrHeader.setHeaderNo(aRS.getLong("HeaderNo"));
		aTrHeader.setN1(aRS.getInt("N1"));
		aTrHeader.setN2(aRS.getInt("N2"));
		aTrHeader.setN3(aRS.getInt("N3"));
		aTrHeader.setRemark(aRS.getString("Remark"));
		aTrHeader.setBranchCode(aRS.getString("BranchCode"));
		aTrHeader.setUserID(aRS.getString("UserID"));
		aTrHeader.setT1(aRS.getString("T1"));
		aTrHeader.setT2(aRS.getString("T2"));
		aTrHeader.setT3(aRS.getString("T3"));
		aTrHeader.setTrDate(SharedUtil.formatDBDate2MIT(aRS.getString("TrDate")));
		aTrHeader.setTrTime(SharedUtil.formatDBDate2MIT(aRS.getString("TrTime")));
		aTrHeader.setCounterID(aRS.getString("CounterID"));
		aTrHeader.setT4(aRS.getString("T4"));
		aTrHeader.setT5(aRS.getString("T5"));
		aTrHeader.setT6(aRS.getString("T6"));
		aTrHeader.setT7(aRS.getString("T7"));
		aTrHeader.setN4(aRS.getInt("N4"));
		
	}
	
	public int getHeaderListCount(Connection aConn, String aCriString, int aRow, int aRowCount) throws SQLException
	{
		int l_TotalRowCount = 0;
		String pQuery = "";
		String pCri = "";
		String pCriType = "";
		String pValue = "";
	//	ArrayList<FECTransactionHeaderDataSet> pDataSet = new ArrayList<FECTransactionHeaderDataSet>();
		
		if(aCriString.contains(":"))
		{
			pCriType = aCriString.substring(0,aCriString.indexOf(':'));
			
			if(aCriString.length() > aCriString.indexOf(':') + 1)
			{
				pValue = aCriString.substring(aCriString.indexOf(':') +1, aCriString.length() - (aCriString.indexOf(':') +1));
			}
			else
			{
				pValue = aCriString;
			}
		}
		else
		{
			pValue = aCriString;
		}
		
		pCriType = pCriType.toLowerCase();
		
		if(pCriType.equals("srno"))
		{
			pCri = " And TH.HeaderNo = " + pValue;
		}
		else if(pCriType.equals("nrc"))
		{
			pCri = " And C.NRCNo = '" + pValue + "'";
		}
		else if(pCriType.equals("name"))
		{
			pCri = " And C.[Name] = '" + pValue + "'";
		}
		else if(pCriType.equals("cds"))
		{
			pCri = " And TH.CauseDescription Like '%" + pValue + "%'";
		}
		else if(pCriType.equals("tdate"))
		{
			pCri = " And TH.TrDate = '" + pValue + "'";
		}
		else if(pCriType.equals("othno"))
		{
			pCri = " And C.BCNo = '" + pValue + "'";
		}
		else if(pCriType.equals("teller"))
		{
			pCri = " And TH.UserID = '" + pValue + "'";
		}
		else
		{
			if(!pValue.equals(""))
			{
				pCri = " And TH.HeaderNo Like '%" + pValue + "%'";
                /*pCri += " Or C.[Name] like '%" + pValue + "%'";
                pCri += " Or C.NRCNo like '%" + pValue + "%'";
                pCri += " Or C.BCNo Like '%" + pValue + "%'";
                pCri += " Or TH.CauseDescription Like '%" + pValue + "%'";
                pCri += " Or TH.TrDate Like '%" + pValue + "%'";
                pCri += " Or TH.UserID Like '%" + pValue + "%'";*/
			}
		}
		
		if(!objTransactionHeaderData.getBranchCode().equals(""))
		{
			pCri += " And TH.BranchCode = '" + objTransactionHeaderData.getBranchCode() + "'";
		}
		if(!objTransactionHeaderData.getTrDate().equals("01/01/1900"))
		{
			pCri += " And TH.TrDate = '" + objTransactionHeaderData.getTrDate() + "'";
		}
		if(!objTransactionHeaderData.getExchangeTypeSearch().equals(""))
		{
			pCri += " AND ExchangeType IN ("+objTransactionHeaderData.getExchangeTypeSearch()+")";
		}
		if(objTransactionHeaderData.getN1()!=0)
		{
			pCri += " AND TH.N1 ="+objTransactionHeaderData.getN1();
		}
		pQuery ="Set Dateformat dmy Select Count(TH.HeaderNo) From TransactionHeader TH " +
        " Left Join Customer C On TH.CustomerID=C.CustomerID" +
        " Where TH.N1 <> 4 " + pCri  ;
		
		
		
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())
		{
			l_TotalRowCount = rs.getInt(1);
		}
		rs.close();
		pstmt.close();
		
		return l_TotalRowCount;
	}
	
	public ArrayList<FECTransactionHeaderDataSet> GetHeaderList(Connection aConn, String aCriString, int aRow, int aRowCount) throws SQLException
	{
		String pQuery = "";
		String pCri = "";
		String pCriType = "";
		String pValue = "";
		ArrayList<FECTransactionHeaderDataSet> pDataSet = new ArrayList<FECTransactionHeaderDataSet>();
		
		if(aCriString.contains(":"))
		{
			pCriType = aCriString.substring(0,aCriString.indexOf(':'));
			
			if(aCriString.length() > aCriString.indexOf(':') + 1)
			{
				pValue = aCriString.substring(aCriString.indexOf(':') +1, aCriString.length() - (aCriString.indexOf(':') +1));
			}
			else
			{
				pValue = aCriString;
			}
		}
		else{
			pValue = aCriString;
		}
		
		pCriType = pCriType.toLowerCase();
		
		if(pCriType.equals("srno"))
		{
			pCri = " And TH.HeaderNo = " + pValue;
		}
		else if(pCriType.equals("nrc"))
		{
			pCri = " And C.NRCNo = '" + pValue + "'";
		}
		else if(pCriType.equals("name"))
		{
			pCri = " And C.[Name] = '" + pValue + "'";
		}
		else if(pCriType.equals("cds"))
		{
			pCri = " And TH.CauseDescription Like '%" + pValue + "%'";
		}
		else if(pCriType.equals("tdate"))
		{
			pCri = " And TH.TrDate = '" + pValue + "'";

		}
		else if(pCriType.equals("othno"))
		{
			pCri = " And C.BCNo = '" + pValue + "'";
		}
		else if(pCriType.equals("teller"))
		{
			pCri = " And TH.UserID = '" + pValue + "'";
		}
		else
		{
			if(!pValue.equals(""))
			{
				pCri = " And TH.HeaderNo Like '%" + pValue + "%'";
               /* pCri += " Or C.[Name] like '%" + pValue + "%'";
                pCri += " Or C.NRCNo like '%" + pValue + "%'";
                pCri += " Or C.BCNo Like '%" + pValue + "%'";
                pCri += " Or TH.CauseDescription Like '%" + pValue + "%'";
                pCri += " Or TH.TrDate Like '%" + pValue + "%'";
                pCri += " Or TH.UserID Like '%" + pValue + "%'";*/
			}
		}
		
		if(!objTransactionHeaderData.getBranchCode().equals(""))
		{
			pCri += " And TH.BranchCode = '" + objTransactionHeaderData.getBranchCode() + "'";
		}
		if(!objTransactionHeaderData.getTrDate().equals("01/01/1900"))
		{
			pCri += " And TH.TrDate = '" + objTransactionHeaderData.getTrDate() + "'";
		}
		if(!objTransactionHeaderData.getExchangeTypeSearch().equals(""))
		{
			pCri += " And TH.TrDate = '" + objTransactionHeaderData.getTrDate() + "'";
		}
		if(!objTransactionHeaderData.getExchangeTypeSearch().equals(""))
		{
			pCri += " AND ExchangeType IN ("+objTransactionHeaderData.getExchangeTypeSearch()+")";
		}
		if(objTransactionHeaderData.getN1()!=0)
		{
			pCri += " AND N1 ="+objTransactionHeaderData.getN1();
		}
		/*
		pQuery ="Set Dateformat dmy Select Count(TH.HeaderNo) From TransactionHeader TH " +
        " Left Join Customer C On TH.CustomerID=C.CustomerID" +
        " Where N1 <> 4 " + pCri;
		
		
		
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())
		{
			aTotalRowCount = rs.getObject(1);
		}
		rs.close();
		pstmt.close(); */
		
		pQuery = "Set Dateformat dmy Set rowcount " + aRowCount +
				" Select * From (Select row_number() over (Order By TH.HeaderNo) as [rowid], TH.BranchCode, " +
				"TH.HeaderNo SrNo, TH.ExchangeType,TH.CustomerID,[TH].Title,[TH].Name,[TH].NRCNo,[TH].Phone,[TH].Sex,"+
				"[TH].Address,[TH].Email,[TH].[t1] AS [T1],[TH].[t2] AS [T2],[TH].[T3] AS [T3]," +
				"TH.CauseDescription, Case When TH.ExchangeType=1 Then 'Deposit' " +//Change Here
				"Else(Case When TH.ExchangeType=2 Then 'Buy' Else(Case When TH.ExchangeType=3 Then 'Sell' " +
				"Else 'Withdrawal' End) End)  End [Transaction Type], Convert(Varchar,TH.TrDate,103) [Transaction Date], " +
				"TH.Remark, TH.UserID,TH.N2 as TranRef, TH.N1 as status From TransactionHeader TH " +//,TH.BICode			//add  TH.N1 as status
				" Where TH.N1 <> 4 " + pCri + 
				") as [kmb] Where [rowid] > " + aRow ;
		
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		
		ResultSet rs = pstmt.executeQuery();
		int i =0;
		while(rs.next())
		{
			FECTransactionHeaderDataSet pTrHeader = new FECTransactionHeaderDataSet();
			ReadDSRecord(pTrHeader, rs);
			pDataSet.add(i++, pTrHeader);
		}
		pstmt.close();
		
		return pDataSet;
	}
	
	public ArrayList<FECTransactionHeaderDataSet> GetHeaderListFroVoucher(Connection aConn, String aCriString, int aRow, int aRowCount, Object aTotalRowCount) throws SQLException
	{
		String pQuery = "";
		String pCri = "";
		String pCriType = "";
		String pValue = "";
		ArrayList<FECTransactionHeaderDataSet> pDataSet = new ArrayList<FECTransactionHeaderDataSet>();
		
		if(aCriString.contains(":"))
		{
			pCriType = aCriString.substring(0,aCriString.indexOf(':'));
			
			if(aCriString.length() > aCriString.indexOf(':') + 1)
			{
				pValue = aCriString.substring(aCriString.indexOf(':') +1, aCriString.length() - (aCriString.indexOf(':') +1));
			}
			else
			{
				pValue = aCriString;
			}
		}
		else
		{
			pValue = aCriString;
		}
		
		pCriType = pCriType.toLowerCase();
		
		if(pCriType == "srno")
		{
			pCri = " And TH.HeaderNo = " + pValue;
		}
		else if(pCriType == "nrc")
		{
			pCri = " And C.NRCNo = '" + pValue + "'";
		}
		else if(pCriType == "name")
		{
			pCri = " And C.[Name] = '" + pValue + "'";
		}
		else if(pCriType == "cds")
		{
			pCri = " And TH.CauseDescription Like '%" + pValue + "%'";
		}
		else if(pCriType == "tdate")
		{
			pCri = " And TH.TrDate = '" + pValue + "'";
		}
		else if(pCriType == "othno")
		{
			pCri = " And C.BCNo = '" + pValue + "'";
		}
		else if(pCriType == "teller")
		{
			pCri = " And TH.UserID = '" + pValue + "'";
		}
		else
		{
			if(pValue != "")
			{
				pCri = " And TH.HeaderNo Like '%" + pValue + "%'";
                pCri += " Or C.[Name] like '%" + pValue + "%'";
                pCri += " Or C.NRCNo like '%" + pValue + "%'";
                pCri += " Or C.BCNo Like '%" + pValue + "%'";
                pCri += " Or TH.CauseDescription Like '%" + pValue + "%'";
                pCri += " Or TH.TrDate Like '%" + pValue + "%'";
                pCri += " Or TH.UserID Like '%" + pValue + "%'";
			}
		}
		
		if(objTransactionHeaderData.getBranchCode() !=  "")
		{
			pCri += " And TH.BranchCode = '" + objTransactionHeaderData.getBranchCode() + "'";
		}
		if(objTransactionHeaderData.getTrDate() != "1/1/1900")
		{
			pCri += " And TH.TrDate = '" + objTransactionHeaderData.getTrDate() + "'";
		}
		
		pQuery ="Set Dateformat dmy Select TH.HeaderNo From TransactionHeader TH " +
        " Left Join Customer C On TH.CustomerID=C.CustomerID" +
        " Where TH.N1 <> 4 " + pCri;
		
		
		
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		aTotalRowCount = pstmt.getMaxRows();
		pstmt.close();
		
		pQuery = "Set Dateformat dmy Set rowcount " + aRowCount +
        " Select * From (Select row_number() over (Order By TH.HeaderNo) as [rowid], TH.BranchCode, TH.HeaderNo SrNo,TH.CauseDescription, Case When TH.ExchangeType=1 Then 'Buy' Else(Case When TH.ExchangeType=2 Then 'Sell' Else(Case When TH.ExchangeType=3 Then 'Deposit' Else 'Withdrawal' End) End)  End [Transaction Type], Convert(Varchar,TH.TrDate,103) [Transaction Date], TH.Remark, TH.UserID From TransactionHeader TH " +
        " Where TH.N1 <> 4 And TH.ExchangeType Not In (3,4) " + pCri +
        ") as [kmb] Where [rowid] > " + aRow;
		
		pstmt = aConn.prepareStatement(pQuery);
		
		ResultSet rs = pstmt.executeQuery();
		int i =0;
		while(rs.next())
		{
			FECTransactionHeaderDataSet pTrHeader = new FECTransactionHeaderDataSet();
			ReadDSRecord(pTrHeader, rs);
			pDataSet.add(i++, pTrHeader);
		}
		pstmt.close();
		
		return pDataSet;
	}
	
	public long GetMaxHeaderNo(Connection aConn) throws SQLException
	{
		long pMax = 0;
		String pQuery = "Select Max(HeaderNo) as maxhd From TransactionHeader";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())
		{
			pMax = rs.getLong("maxhd");
		}
		pstmt.close();
		return pMax;
	}
	
	public boolean UpdateStatusByHeaderNo(Connection aConn, long aHeader, long aStatus) throws SQLException
	{
		boolean l_result = false;
		String pQuery = "Update TransactionHeader" +
		" Set N1 = " + aStatus +
		" Where HeaderNo = " + aHeader;
		if(objTransactionHeaderData.getN1()!=0) {
			pQuery+=" AND N1="+objTransactionHeaderData.getN1();
		}
		
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		if(pstmt.executeUpdate() > 0)
		{
			l_result = true;
		}
		pstmt.close();
		return l_result;
	}
	
	
	public boolean updateStatusandTransRef(long pHeaderNo, long pStatus, long pTransRef, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_Query = "UPDATE TransactionHeader SET N1 = ?, N2 = ? " +
				"WHERE HeaderNo = ? ";
		
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setLong(1, pStatus);
		pstmt.setLong(2, pTransRef);
		pstmt.setLong(3, pHeaderNo);
		
		if (pstmt.executeUpdate() > 0){
			l_result = true;
		}
		pstmt.close();
		return l_result;
	}
	public DataResult updateStatusandTransRefReverse(FECTransactionHeaderData pHData, ArrayList<FECTransactionDetailsData> lstDData, long pStatus, long pTransRef, Connection pConn) throws SQLException{
		DataResult l_result = new DataResult();
		int l_SellSetting = LogicMgr.getSystemSettingT12N6("FEC");
		int l_FECSetting = LogicMgr.getSystemSettingT12N5("FEC");
		
		CurrencyRateDAO l_CurDao = new CurrencyRateDAO();
		String l_Query = "UPDATE TransactionHeader SET N1 = ?, N2 = ? " +
				"WHERE HeaderNo = ? ";
		
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setLong(1, pStatus);
		pstmt.setLong(2, pTransRef);
		pstmt.setLong(3, pHData.getHeaderNo());
		if(pstmt.executeUpdate() > 0)
			l_result.setStatus(true);
		else {
			l_result.setStatus(false);
			l_result.setDescription("Update TransactionHeader Fail!");
		}
		
		if(l_result.getStatus() && pHData.getExchangeType()==2) {//check sell data
			l_Query = "SELECT * FROM TransactionDetails Where HeaderNo=? AND (N3=0 OR N3<DenoQty) AND TrCurCode<>?";
			pstmt = pConn.prepareStatement(l_Query);
			pstmt.setLong(1, pHData.getHeaderNo());
			pstmt.setString(2, LogicMgr.getBaseCurCode());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_result.setStatus(false);
				l_result.setDescription("Already sold that Notes.Can't reverse!");break;
			}
			if(l_result.getStatus()) {
				l_Query = "UPDATE TransactionDetails SET N1 = ?, N2 = ? " +
						"WHERE HeaderNo = ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setLong(1, pStatus);
				pstmt.setLong(2, pTransRef);
				pstmt.setLong(3, pHData.getHeaderNo());
				if (pstmt.executeUpdate() > 0){
					l_result.setStatus(true);
				}else {
					l_result.setStatus(false);
					l_result.setDescription("Update TransactionDetails Fail!");
				}
			}
		}
		if(l_result.getStatus() && pHData.getExchangeType()==3){
			ArrayList<TempData>	lsttmpData =new ArrayList<TempData>();
		
			if(l_FECSetting==3){
				for (FECTransactionDetailsData fecTranDData : lstDData) {
					if(!fecTranDData.getTrCurCode().equals(LogicMgr.getBaseCurCode())){
						lsttmpData = l_CurDao.getBuyCurrencyNotesforRev(fecTranDData.getTrCurCode(), pHData.getBranchCode(), pHData.getCounterID(), 
								fecTranDData.getTrNote(),l_SellSetting, pConn);
						int SellQty = fecTranDData.getDenoQty();
						for(TempData l_tmpData: lsttmpData){
							if(SellQty>=l_tmpData.getN14()){
								SellQty = SellQty-l_tmpData.getN14();
								l_tmpData.setN14(l_tmpData.getN16());
								l_result.setStatus(l_CurDao.updatebuyRemainingNotesNStatus(l_tmpData, pStatus, pTransRef, pConn));
								if(!l_result.getStatus()) 
									l_result.setDescription("Update Remaining Notes Fail!");
								break;
							}else {								
								l_tmpData.setN14(l_tmpData.getN14()-SellQty);
								SellQty = 0;
								l_result.setStatus(l_CurDao.updatebuyRemainingNotesNStatus(l_tmpData,pStatus, pTransRef, pConn));
								if(!l_result.getStatus()) 
									l_result.setDescription("Update Remaining Notes Fail!");break;
								
							}
						}
					}else {
						l_Query = "UPDATE TransactionDetails SET N1 = ?, N2 = ? " +
								"WHERE HeaderNo = ? AND DetailNo=?";
						pstmt = pConn.prepareStatement(l_Query);
						pstmt.setLong(1, pStatus);
						pstmt.setLong(2, pTransRef);
						pstmt.setLong(3, pHData.getHeaderNo());
						pstmt.setLong(4, fecTranDData.getDetailNo());
						if (pstmt.executeUpdate() > 0){
							l_result.setStatus(true);
						}else {
							l_result.setStatus(false);
							l_result.setDescription("Update TransactionDetails Fail!");break;
						}
							
					}
				}
		
			}else {
				l_Query = "UPDATE TransactionDetails SET N1 = ?, N2 = ? " +
						"WHERE HeaderNo = ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setLong(1, pStatus);
				pstmt.setLong(2, pTransRef);
				pstmt.setLong(3, pHData.getHeaderNo());
				if (pstmt.executeUpdate() > 0){
					l_result.setStatus(true);
				}else {
					l_result.setStatus(false);
					l_result.setDescription("Update TransactionDetails Fail!");
				}
			}
		}
		pstmt.close();
		return l_result;
	}
	
	public boolean updateTransHeader(Connection pConn,FECTransactionHeaderData hdata) throws SQLException{
		boolean l_result = false;
		String l_Query = "UPDATE TransactionHeader SET T6 = ? WHERE HeaderNo = ? and n1=1 ";
		
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, hdata.getT6());
		pstmt.setLong(2, hdata.getHeaderNo());
		
		if (pstmt.executeUpdate() > 0){
			l_result = true;
		}
		pstmt.close();
		return l_result;
	}
	
	public boolean updateStatusandTransRefForTransIn(long pHeaderNo, long pStatus, long pTransRef, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_Query = "UPDATE TransactionHeader SET N1 = ?, N4 = ? " +
				"WHERE HeaderNo = ? ";
		
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setLong(1, pStatus);
		pstmt.setLong(2, pTransRef);
		pstmt.setLong(3, pHeaderNo);
		
		if (pstmt.executeUpdate() > 0){
			l_result = true;
		}
		pstmt.close();
		return l_result;
	}
	
	
	public boolean Truncate(Connection aConn) throws SQLException
	{
		boolean l_result = false;
		String pQuery = "Update TransactionHeader Set N1 = 4";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		
		if(pstmt.executeUpdate() > 0)
			l_result = true;
		
		return l_result;
	}
	
	private void ReadDSRecord(FECTransactionHeaderDataSet aTrDataSet, ResultSet aResultSet) throws SQLException	{		   
	    aTrDataSet.setExchangeType(aResultSet.getShort("ExchangeType"));
	    aTrDataSet.setCustomerID(aResultSet.getString("CustomerID"));
	    aTrDataSet.setName(aResultSet.getString("Title").equals("")?aResultSet.getString("Name"):
	    aResultSet.getString("Title")+" "+aResultSet.getString("Name"));
	    aTrDataSet.setNRC(aResultSet.getString("NrcNo"));
	    aTrDataSet.setAddress(aResultSet.getString("Address"));
	    aTrDataSet.setPhone(aResultSet.getString("Phone"));
	    aTrDataSet.setEmail(aResultSet.getString("Email"));
	    aTrDataSet.setT1(aResultSet.getString("t1"));
	    aTrDataSet.setT2(aResultSet.getString("t2"));
	    aTrDataSet.setT3(aResultSet.getString("t3"));
		aTrDataSet.setBranchCode(aResultSet.getString("BranchCode"));
		aTrDataSet.setCauseDescription(aResultSet.getString("CauseDescription"));
		aTrDataSet.setHeaderNo(aResultSet.getLong("SrNo"));		
		aTrDataSet.setRemark(aResultSet.getString("Remark"));
		//aTrDataSet.setRowID(aResultSet.getInt("rowid"));
		aTrDataSet.setpTranRef(aResultSet.getString("TranRef"));
		aTrDataSet.setTransactionType(aResultSet.getString("Transaction Type"));
		aTrDataSet.setUserID(aResultSet.getString("UserID"));
		aTrDataSet.setTrDate(aResultSet.getString("Transaction Date"));
		aTrDataSet.setpN1(aResultSet.getInt("status"));								//mmmyint 03.10.2016
		//aTrDataSet.setpBICode(aResultSet.getString("BICode"));
	}
	
	private void ReadRecordSmartSearch(FECTransactionHeaderDataSet aTrDataSet, ResultSet aResultSet) throws SQLException
	{
		aTrDataSet.setBranchCode(aResultSet.getString("BranchCode"));  
		aTrDataSet.setHeaderNo(aResultSet.getLong("SrNo"));
		aTrDataSet.setExchangeType(aResultSet.getShort("ExchangeType"));
		aTrDataSet.setCustomerID(aResultSet.getString("CustomerID"));
		aTrDataSet.setName(aResultSet.getString("Title").equals("")?aResultSet.getString("Name"):
	    aResultSet.getString("Title")+" "+aResultSet.getString("Name"));
	    aTrDataSet.setNRC(aResultSet.getString("NrcNo"));
	    aTrDataSet.setAddress(aResultSet.getString("Address"));
	    aTrDataSet.setPhone(aResultSet.getString("Phone"));
	    aTrDataSet.setEmail(aResultSet.getString("Email"));
	    aTrDataSet.setT1(aResultSet.getString("t1"));
	    aTrDataSet.setT2(aResultSet.getString("t2"));
	    aTrDataSet.setT3(aResultSet.getString("t3"));	    	
		aTrDataSet.setCauseDescription(aResultSet.getString("CauseDescription"));    
		aTrDataSet.setTransactionType(aResultSet.getString("Transaction Type"));    
		aTrDataSet.setTrDate(aResultSet.getString("Transaction Date"));	
		aTrDataSet.setRemark(aResultSet.getString("Remark"));	
		aTrDataSet.setUserID(aResultSet.getString("UserID"));
	}
	
	public boolean addTransactionHeader(Connection aConn, FECTransactionHeaderData aTrHeaderBean) throws SQLException
	{
		boolean result = false;
		String pQuery = "Set DateFormat dmy Insert Into TransactionHeader( HeaderNo, ExchangeType, DenoType, TrDate, TrTime, CustomerID, CauseDescription," +
				"Title,Name,Sex,NrcNo,Address,Phone,Email, Remark, BranchCode, UserID, N1, N2, N3, T1, T2, T3, CounterID,T4,T5,T6,T7,N4) " +//,VocherNo,CounterID,BICode
		" Values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";//, ?, ?, ?

		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		updateRecord(aTrHeaderBean, pstmt);
		if(pstmt.executeUpdate()> 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	private void updateRecord(FECTransactionHeaderData aTrHeaderBean, PreparedStatement aPS) throws SQLException
	{
		int i =1;
		aPS.setLong(i++, aTrHeaderBean.getHeaderNo());
		aPS.setShort(i++, aTrHeaderBean.getExchangeType());
		aPS.setShort(i++, aTrHeaderBean.getDenoType());
		aPS.setString(i++, aTrHeaderBean.getTrDate());
		aPS.setString(i++, aTrHeaderBean.getTrTime());
		aPS.setString(i++, aTrHeaderBean.getCustomerID());
		aPS.setString(i++, aTrHeaderBean.getCauseDescription());
		
		aPS.setString(i++, aTrHeaderBean.getTitle());
		aPS.setString(i++, aTrHeaderBean.getName());
		aPS.setInt(i++, aTrHeaderBean.getSex());
		aPS.setString(i++, aTrHeaderBean.getNrcNo());
		aPS.setString(i++, aTrHeaderBean.getAddress());
		aPS.setString(i++, aTrHeaderBean.getPhone());
		aPS.setString(i++, aTrHeaderBean.getEmail());
		
		aPS.setString(i++, aTrHeaderBean.getRemark());
		aPS.setString(i++, aTrHeaderBean.getBranchCode());
		aPS.setString(i++, aTrHeaderBean.getUserID());
		aPS.setInt(i++, aTrHeaderBean.getN1());
		aPS.setInt(i++, aTrHeaderBean.getN2());
		aPS.setInt(i++, aTrHeaderBean.getN3());

		aPS.setString(i++, aTrHeaderBean.getT1());
		aPS.setString(i++, aTrHeaderBean.getT2());
		aPS.setString(i++, aTrHeaderBean.getT3());
		aPS.setString(i++, aTrHeaderBean.getCounterID());
		aPS.setString(i++, aTrHeaderBean.getT4());
		aPS.setString(i++, aTrHeaderBean.getT5());
		aPS.setString(i++, aTrHeaderBean.getT6());
		aPS.setString(i++, aTrHeaderBean.getT7());
		aPS.setInt(i++, aTrHeaderBean.getN4());
		/*aPS.setString(17, aTrHeaderBean.getVocherNo());
		aPS.setString(19, aTrHeaderBean.getpBICode());*/
		
	}
	
	//KMT
	public boolean updateTransactionHeader(Connection aConn, FECTransactionHeaderData aTrHeaderBean) throws SQLException
	{
		boolean result = false;
		String pQuery = "Set Dateformat dmy; Update TransactionHeader Set TrDate=?, TrTime=?, CustomerID=?, CauseDescription=?," +
				"Title=?,Name=?,Sex=?,NrcNo=?,Address=?,Phone=?,Email=?, Remark=?,T1=?, T2=?, T3=? Where HeaderNo = ? And N1 = 1";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		updateRecords(aTrHeaderBean, pstmt);
		if(pstmt.executeUpdate()> 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	//KMT
	private void updateRecords(FECTransactionHeaderData aTrHeaderBean, PreparedStatement aPS) throws SQLException
	{	
		int i =1;
		aPS.setString(i++, aTrHeaderBean.getTrDate());
		aPS.setString(i++, aTrHeaderBean.getTrTime());
		aPS.setString(i++, aTrHeaderBean.getCustomerID());
		aPS.setString(i++, aTrHeaderBean.getCauseDescription());
		aPS.setString(i++, aTrHeaderBean.getTitle());
		aPS.setString(i++, aTrHeaderBean.getName());
		aPS.setInt(i++, aTrHeaderBean.getSex());
		aPS.setString(i++, aTrHeaderBean.getNrcNo());
		aPS.setString(i++, aTrHeaderBean.getAddress());
		aPS.setString(i++, aTrHeaderBean.getPhone());
		aPS.setString(i++, aTrHeaderBean.getEmail());
		aPS.setString(i++, aTrHeaderBean.getRemark());
		//aPS.setInt(6, aTrHeaderBean.getN1());
		aPS.setString(i++, aTrHeaderBean.getT1());
		aPS.setString(i++, aTrHeaderBean.getT2());
		aPS.setString(i++, aTrHeaderBean.getT3());
		aPS.setLong(i++, aTrHeaderBean.getHeaderNo());
	}
	
	//WKZ
	public ArrayList<FECTransactionHeaderData> getTrHeaderForExcel(Connection aConn) throws SQLException {
		
		
		ArrayList<FECTransactionHeaderData> l_TrHeaderList = new ArrayList<FECTransactionHeaderData>();
		String l_Query = "Select * From TransactionHeader Where 1=1 and N1<>4";
		PreparedStatement pstmt;
		pstmt = aConn.prepareStatement(l_Query);
		
		ResultSet rs = pstmt.executeQuery();
	
		
		while(rs.next())
		{
			FECTransactionHeaderData l_TransactionHeaderData = new FECTransactionHeaderData();
			ReadRecord(l_TransactionHeaderData, rs);
			l_TrHeaderList.add(l_TransactionHeaderData);
		}
		pstmt.close();
		
		return l_TrHeaderList;
	}
	private void PrepareWhereClause(FECCriteriaData aCir,StringBuffer aQuery){
		aQuery.append(" th.branchcode Like ?) and ");
		if(!aCir.header.equals(""))
		{
			//aQuery.append(" And TH.HeaderNo = " + aCir.header);
		}
		else if(aCir.equals("nrc"))
		{

			//aQuery.append(" And C.NRCNo = " + aCir.header);
		/*}
			pCri = " And C.NRCNo = '" + pValue + "'";*/
		}
		else if(aCir.equals("name"))
		{
			//aCir = " And C.[Name] = '" + pValue + "'";
		}
		else if(aCir.equals("cds"))
		{
			//pCri = " And TH.CauseDescription Like '%" + pValue + "%'";
		}
		else if(aCir.equals("tdate"))
		{
			//pCri = " And TH.TrDate = '" + pValue + "'";
		}
		else if(aCir.equals("othno"))
		{
			//pCri = " And C.BCNo = '" + pValue + "'";
		}
		else if(aCir.equals("teller"))
		{
			//pCri = " And TH.UserID = '" + pValue + "'";
		}
		else{
				aQuery.append("(TH.HeaderNo Like ? ");
				aQuery.append(" Or C.[Name]  like ? ");
				aQuery.append(" Or C.NRCNo like ? ");
				aQuery.append(" Or C.BCNo Like ? ");
				aQuery.append(" Or TH.CauseDescription Like ?");
				aQuery.append("  Or TH.TrDate = ? ");
				aQuery.append(" Or TH.UserID Like ? ");
		}
		aQuery.append(")");
		
		
	}
	private void PrepareCriteria(FECCriteriaData aCri,PreparedStatement aPstmt) throws SQLException{
		int l_paraIndex = 0;
		
		if(!aCri.branchCode.equals("999")){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,aCri.branchCode);
		}
		else{
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%%");
		}
			
		if(aCri.HeaderOperator==NumFilterOperator.Equal){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%"+aCri.searchtext+"%");
		}
		if(aCri.CustomerNameOperator==TxTFilterOperator.Contain){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%"+aCri.searchtext+"%");
		}
		if(aCri.NrcOperator==TxTFilterOperator.Contain){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%"+aCri.searchtext+"%");
		}
		if(aCri.BCNoOperator==TxTFilterOperator.Contain){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%"+aCri.searchtext+"%");
		}
		if(aCri.CauseDescOperator==TxTFilterOperator.Contain){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%"+aCri.searchtext+"%");
		}
		if(aCri.TrDateOperator==DateFilterOperator.Equal){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,aCri.searchtext);
		}
		if(aCri.UserIdOperator==TxTFilterOperator.Contain){
			l_paraIndex += 1;
			aPstmt.setString(l_paraIndex,"%"+aCri.searchtext+"%");
		}
	}
	public ArrayList<FECTransactionHeaderDataSet> getFECSmartSearch(FECCriteriaData aCri,Connection aConn) throws SQLException{
		ArrayList<FECTransactionHeaderDataSet> l_resultSet=new ArrayList<FECTransactionHeaderDataSet>();
		StringBuffer l_query=new StringBuffer(" set dateformat dmy Select TH.BranchCode, " +
	        "TH.HeaderNo SrNo, TH.ExchangeType,TH.CustomerID,[TH].Title,[TH].Name,[TH].NRCNo,[TH].Phone,[TH].Sex,"+
			"[TH].Address,[TH].Email,[TH].[t1] AS [T1],[TH].[t2] AS [T2],[TH].[T3] AS [T3],TH.CauseDescription,"+ 
	        "Case When TH.ExchangeType=1 Then 'Deposit' " +//Change exchange type
	        "Else(Case When TH.ExchangeType=2 Then 'Buy' Else(Case When TH.ExchangeType=3 Then 'Sell' " +
	        "Else 'Withdrawal' End) End)  End [Transaction Type], Convert(Varchar,TH.TrDate,103) [Transaction Date], " +
	        "TH.Remark, TH.UserID From TransactionHeader TH " +
	        " left Join Customer C On TH.CustomerID=C.CustomerID" +
	        " Where (TH.N1 <> 4 and TH.exchangeType="+objTransactionHeaderData.getExchangeType()+ " and ");
		PrepareWhereClause(aCri,l_query);
		PreparedStatement pstmt=aConn.prepareStatement(l_query.toString());
		PrepareCriteria(aCri,pstmt);
		ResultSet rs=pstmt.executeQuery();
		int i=0;
		while(rs.next()){
			FECTransactionHeaderDataSet l_data=new FECTransactionHeaderDataSet();
			//ReadRecord(l_data, rs);
			//ReadDSRecord(l_data, rs);
			ReadRecordSmartSearch(l_data, rs);
			l_resultSet.add(i++,l_data);
		}
		pstmt.close();
		return l_resultSet;
	}
	
	//KMT
	public boolean isExit(long aTransRef, Connection aConn) throws SQLException
	{
		boolean result = false;
		String pQuery = "Select count(N2)N2 from TransactionHeader Where N2 = ? ";
				
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setLong(1, aTransRef);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
			result = true;
		pstmt.close();
		rs.close();
		return result;
	}
	
	public boolean updateStatusByTransRef(int aTransRef, int aStatus, Connection aConn) throws SQLException
	{
		boolean result = false;
		String pQuery = "Set Dateformat dmy; Update TransactionHeader Set N1 = ? Where N2 = ? ";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setInt(1, aStatus);
		pstmt.setInt(2, aTransRef);
		if(pstmt.executeUpdate()> 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	public boolean isExitHeaderNo(long aHeaderNo, int pInorOut,Connection aConn) throws SQLException
	{
		boolean result = false;
		String pQuery="";
		if(pInorOut >0 )
			pQuery = "Select * from TransactionHeader Where HeaderNo = ? And N1 =  "+pInorOut;
		else	
			pQuery = "Select * from TransactionHeader Where HeaderNo = ? And N1 = 1 ";
				
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setLong(1, aHeaderNo);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			result = true;
			ReadRecord(objTransactionHeaderData,rs);
		}
		pstmt.close();
		rs.close();
		return result;
	}
	//mmm
/*
	public boolean getTransLimit( String curCode,String  exchangeType,double pAmount,String branchCode,Connection pConn) throws SQLException
	{
		boolean l_Result=false;
		
		
		double l_LimitData= getLimitAmountBuySell(curCode,exchangeType,pConn);
		if(pAmount > l_LimitData && l_LimitData > 0)
			l_Result=false;
		else
			l_Result=true;
		
		if(l_Result){
			if(getTransLimitBalance(curCode,exchangeType,pAmount,branchCode,pConn)){
				l_Result=true;
			}else
				l_Result=false;
		}
		return l_Result;
	}
*/
	public int getTransLimit( String curCode,String  exchangeType,double pAmount,String branchCode,Connection pConn) throws SQLException
	{  
		/*----------0=true, 1=Buy/Sell , 2=Min, 3=Max-----------------------------------------*/
		int l_Result=0;
		/* ------Check for transaction limit Balance----------------------------------------------*/
		double l_LimitData= getLimitAmountBuySell(curCode,exchangeType,pConn);
		if(pAmount > l_LimitData && l_LimitData > 0)
			l_Result=1;
		else
			l_Result=0;
		
		if (l_Result==0){/* ------Check for Minimum /Max limit Amount-----------------------------------------------*/
			l_Result=getTransLimitBalance(curCode,exchangeType,pAmount,branchCode,pConn) ;
		}
		return l_Result;
	}
	//mmm
	public int getTransLimitBalance( String curCode,String  exchangeType,double pAmount,String branchCode,Connection pConn) throws SQLException
	{
		int l_Result=0;
		String pQuery="";
	
		FECTransactionLimitData l_data=new FECTransactionLimitData();
		l_data= getLimitAmountBalance(curCode,branchCode,pConn);
		if( l_data.getpLimitAmtMin() !=0 && l_data.getpLimitAmtMax() !=0){
		double trAmount=0.0;
		pQuery = "SELECT SUM( Note * DenoQty ) AS TrAmount FROM CashOpeningClosing WHERE CurrencyCode=? AND BranchCode=?  ";
		PreparedStatement pstmt = pConn.prepareStatement(pQuery);
		pstmt.setString(1, curCode);
		pstmt.setString(2, branchCode);

		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			trAmount=rs.getDouble("TrAmount");
		}
		if(exchangeType.equalsIgnoreCase("Buy")){
			trAmount += pAmount;
		}else if(exchangeType.equalsIgnoreCase("Sell")){
			trAmount -= pAmount;
		}
		if( trAmount < l_data.getpLimitAmtMin() )
			l_Result=2;
		else if ( trAmount > l_data.getpLimitAmtMax() )
			l_Result=3;
		else
			l_Result=0;
		}
		return l_Result;
	}
	
	public double getLimitAmountBuySell(String curCode, String exchangeType, Connection aConn) throws SQLException
	{
		double l_Result= 0;
		String pQuery = "SELECT top 1 ISNULL(LimitAmountMax,0) AS LimitAmt  FROM FECTransLimit WHERE  CurrencyCode=? AND ExchangeType=?";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setString(1, curCode);
		pstmt.setString(2, exchangeType);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_Result=rs.getDouble("LimitAmt");
		}
		pstmt.close();
		rs.close();
		return l_Result;
	}
	public FECTransactionLimitData getLimitAmountBalance(String curCode, String branchCode, Connection aConn) throws SQLException
	{
		FECTransactionLimitData data=new FECTransactionLimitData();
		String pQuery = "SELECT top 1 ISNULL(LimitAmount,0) AS LimitAmtMin, ISNULL(LimitAmountMax,0) AS LimitAmtmax  FROM FECTransLimit WHERE  CurrencyCode=? AND BranchCode=?";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setString(1, curCode);
		pstmt.setString(2, branchCode);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			data.setpLimitAmtMin(rs.getDouble("LimitAmtMin"));
			data.setpLimitAmtMax(rs.getDouble("LimitAmtmax"));
		}
		pstmt.close();
		rs.close();
		return data;
	}
	

	public ArrayList<FECTransactionHeaderData> getFECWriteOffSearchoption(
			String condition, Connection conn) throws SQLException {
		ArrayList<FECTransactionHeaderData> poData= new ArrayList<FECTransactionHeaderData>();
		StringBuffer  l_query = new StringBuffer("");
     	l_query.append("SELECT a.*,b.branchName from TransactionHeader a inner join BankDatabases b on a.branchcode=b.branchcode  WHERE ExchangeType=6 ");	
     	l_query.append(condition);  
     	PreparedStatement pstmt = conn.prepareStatement(l_query.toString());      
        ResultSet rs=pstmt.executeQuery();
        int i=0;
        while(rs.next())
        {
        	FECTransactionHeaderData pData = new FECTransactionHeaderData();
        	pData.setAutokey(rs.getLong("autokey"));
        	pData.setHeaderNo(rs.getLong("HeaderNo"));
        	pData.setExchangeType(rs.getShort("ExchangeType"));
        	pData.setDenoType(rs.getShort("DenoType"));
        	pData.setTrDate(rs.getString("TrDate"));
        	pData.setTrTime(rs.getString("TrTime"));
        	pData.setCustomerID(rs.getString("CustomerID"));
        	pData.setCauseDescription(rs.getString("CauseDescription"));
        	pData.setRemark(rs.getString("Remark"));
        	pData.setBranchCode(rs.getString("BranchName"));
        	pData.setUserID(rs.getString("UserID"));
        	pData.setN1(rs.getInt("N1"));
        	pData.setN2(rs.getInt("N2"));
        	pData.setN3(rs.getInt("N3"));
        	pData.setT1(rs.getString("T1"));
        	pData.setT2(rs.getString("T2"));
        	pData.setT3(rs.getString("T3"));
        	pData.setCounterID(rs.getString("CounterID"));
        	pData.setT4(rs.getString("T4"));
        	pData.setT5(rs.getString("T5"));
        	pData.setT6(rs.getString("T6"));
        	pData.setT7(rs.getString("T7"));
        	pData.setN4(rs.getInt("N4"));
           	poData.add(i++, pData);
        }
         pstmt.close();				
         return poData;
	}
	
	public ArrayList<FECTransactionHeaderData> getFECWriteOffSearchoption(
			FECTransactionHeaderData data, Connection conn) throws SQLException {
		ArrayList<FECTransactionHeaderData> poData= new ArrayList<FECTransactionHeaderData>();
		StringBuffer  l_query = new StringBuffer("");
     	l_query.append("SELECT a.*,b.branchName from TransactionHeader a inner join BankDatabases b on a.branchcode=b.branchcode  WHERE ExchangeType=6 ");
     	if(data.getHeaderNo()!=0){
     		l_query.append(" and headerNo="+ data.getHeaderNo()+"");
     	}else if(!data.getT6().equals("")){
     		l_query.append(" and ( T6 like '%"+ data.getT6()+"%' ");
     	}else if(!data.getBranchCode().equals("")){
     		l_query.append(" OR BranchName like '%"+ data.getBranchCode()+"%' ) ");
     	}
     	PreparedStatement pstmt = conn.prepareStatement(l_query.toString());      
        ResultSet rs=pstmt.executeQuery();
        int i=0;
        while(rs.next())
        {
        	FECTransactionHeaderData pData = new FECTransactionHeaderData();
        	pData.setAutokey(rs.getLong("autokey"));
        	pData.setHeaderNo(rs.getLong("HeaderNo"));
        	pData.setExchangeType(rs.getShort("ExchangeType"));
        	pData.setDenoType(rs.getShort("DenoType"));
        	pData.setTrDate(rs.getString("TrDate"));
        	pData.setTrTime(rs.getString("TrTime"));
        	pData.setCustomerID(rs.getString("CustomerID"));
        	pData.setCauseDescription(rs.getString("CauseDescription"));
        	pData.setRemark(rs.getString("Remark"));
        	pData.setBranchCode(rs.getString("BranchName"));
        	pData.setUserID(rs.getString("UserID"));
        	pData.setN1(rs.getInt("N1"));
        	pData.setN2(rs.getInt("N2"));
        	pData.setN3(rs.getInt("N3"));
        	pData.setT1(rs.getString("T1"));
        	pData.setT2(rs.getString("T2"));
        	pData.setT3(rs.getString("T3"));
        	pData.setCounterID(rs.getString("CounterID"));
        	pData.setT4(rs.getString("T4"));
        	pData.setT5(rs.getString("T5"));
        	pData.setT6(rs.getString("T6"));
        	pData.setT7(rs.getString("T7"));
        	pData.setN4(rs.getInt("N4"));
           	poData.add(i++, pData);
        }
         pstmt.close();				
         return poData;
	}


	

	 public boolean deleteHeaderByHeaderNo(Connection aConn, long aHeader) throws SQLException
		{
			boolean l_result = false;
			String pQuery = "delete from TransactionHeader where headerno = ?";
			
			PreparedStatement pstmt = aConn.prepareStatement(pQuery);
			pstmt.setLong(1, aHeader);
			if(pstmt.executeUpdate() > 0)
			{
				l_result = true;
			}
			pstmt.close();
			return l_result;
		}
	 
	 public ArrayList<FECTransactionHeaderData> getFECTransferInSearchoption(
			 FECTransactionHeaderData pTransHeaderData, Connection conn) throws SQLException {
	        ArrayList<FECTransactionHeaderData> poData= new ArrayList<FECTransactionHeaderData>();
	        StringBuffer  l_query = new StringBuffer("");
	         l_query.append("SELECT * from TransactionHeader  WHERE ExchangeType=5 ");    
	         PreparedStatement pstmt = conn.prepareStatement(l_query.toString());      
	        ResultSet rs=pstmt.executeQuery();
	        int i=0;
	        while(rs.next())
	        {
	            FECTransactionHeaderData pData = new FECTransactionHeaderData();
	            pData.setAutokey(rs.getLong("autokey"));
	            pData.setHeaderNo(rs.getLong("HeaderNo"));
	            pData.setExchangeType(rs.getShort("ExchangeType"));
	            pData.setDenoType(rs.getShort("DenoType"));
	            pData.setTrDate(SharedUtil.formatDBDateTime2MIT(rs.getString("TrDate")));//2017-03-01 00:11:00 ==>20170301 00:11:00 ==>01/03/2017 00:11:00
	            pData.setTrTime(rs.getString("TrTime"));
	            pData.setCustomerID(rs.getString("CustomerID"));
	            pData.setCauseDescription(rs.getString("CauseDescription"));
	            pData.setRemark(rs.getString("Remark"));
	            pData.setBranchCode(rs.getString("BranchCode"));
	            pData.setUserID(rs.getString("UserID"));
	            pData.setN1(rs.getInt("N1"));
	            pData.setN2(rs.getInt("N2"));
	            pData.setN3(rs.getInt("N3"));
	            pData.setT1(rs.getString("T1"));
	            pData.setT2(rs.getString("T2"));
	            pData.setT3(rs.getString("T3"));
	            pData.setCounterID(rs.getString("CounterID"));
	            pData.setT4(rs.getString("T4"));
	            pData.setT5(rs.getString("T5"));
	            pData.setT6(rs.getString("T6"));
	            pData.setT7(rs.getString("T7"));
	            pData.setN4(rs.getInt("N4"));
	               poData.add(i++, pData);
	        }
	         pstmt.close();                
	         return poData;
	    }
	 
	 public ArrayList<FECTransactionHeaderData> getFECTransferInSearchoption(
	            String condition, Connection conn) throws SQLException {
	        ArrayList<FECTransactionHeaderData> poData= new ArrayList<FECTransactionHeaderData>();
	        StringBuffer  l_query = new StringBuffer("");
	         l_query.append("SET DATEFORMAT dmy;SELECT A.*, B.BranchName, C.BranchName from TransactionHeader As A inner join Bankdatabases As B on A.BranchCode = B.BranchCode " +
	        		 		" inner join BankDatabases C on A.T5 = C.BranchCode " +
	         				" WHERE A.ExchangeType=5  ");    
	         l_query.append(condition);  
	         PreparedStatement pstmt = conn.prepareStatement(l_query.toString());      
	        ResultSet rs=pstmt.executeQuery();
	        int i=0;
	        while(rs.next())
	        {
	            FECTransactionHeaderData pData = new FECTransactionHeaderData();
	           pData.setAutokey(rs.getLong("autokey"));
	            pData.setHeaderNo(rs.getLong("HeaderNo"));
	            pData.setExchangeType(rs.getShort("ExchangeType"));
	            pData.setDenoType(rs.getShort("DenoType"));
	            pData.setTrDate(rs.getString("TrDate"));
	            pData.setTrTime((rs.getString("TrTime")));//mtdk
	            pData.setCustomerID(rs.getString("CustomerID"));
	            pData.setCauseDescription(rs.getString("CauseDescription"));
	            pData.setRemark(rs.getString("Remark"));
	            pData.setBranchCode(rs.getString("BranchCode"));
	            pData.setUserID(rs.getString("UserID"));
	            pData.setN1(rs.getInt("N1"));
	            pData.setN2(rs.getInt("N2"));
	            pData.setN3(rs.getInt("N3"));
	            pData.setT1(rs.getString("T1"));
	            pData.setT2(rs.getString("T2"));
	            pData.setT3(rs.getString("T3"));
	            pData.setCounterID(rs.getString("CounterID"));
	            pData.setT4(rs.getString("T4"));
	            pData.setT5(rs.getString("T5"));
	            pData.setT6(rs.getString("T6"));
	            pData.setT7(rs.getString("T7"));
	            pData.setN4(rs.getInt("N4"));
	            pData.setT1(rs.getString("BranchName"));//mtdk
	            
          poData.add(i++, pData);
	        }
	         pstmt.close();                
	         return poData;
	    }
	 
	 public Result deleteTransactoinHead(long headerNo, Connection aConn)
				throws SQLException {
			Result res = new Result();
			res.setState(false);
			String query = "delete from TransactionHeader Where HeaderNo = "
					+ headerNo + " And N1 = 1";
			PreparedStatement pstmt = aConn.prepareStatement(query);
			pstmt.executeUpdate();
			res.setState(true);

			pstmt.close();

			return res;
		}
	 
	 public boolean addAttachmentPhoto(Long pHeaderNo,CustomerPhoto customerPhoto,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
        
		boolean result=false;
	
		PreparedStatement pstmt=conn.prepareStatement("INSERT INTO "+getDBName()+"..FXInfophoto VALUES(?,?,?,?,?)");
		WritePhoto(pHeaderNo,customerPhoto, pstmt);
		//File file = new File(SingletonServer.getAbsPath()+"cache//photos//customer//"+customerPhoto.getPhotoUrl()+".jpg");
	    //if(file.isFile())
	    	//file.delete();
        pstmt.executeUpdate();
        pstmt.close();
        result=true;
       return result;
	}
	 
	 public boolean deleteAttachPhoto(Long pHeaderNo, Connection pConn) throws SQLException {
		boolean ret = true;
		PreparedStatement pstmt = pConn.prepareStatement("delete from "+getDBName()+"..FXInfophoto  where HeaderNo = ?");
		pstmt.setLong(1, pHeaderNo);
		pstmt.executeUpdate();
			
		return ret;
	}
	 
	 public boolean deleteAttachPhoto(Long pHeaderNo,String FileName, Connection pConn) throws SQLException {
			boolean ret = true;
			PreparedStatement pstmt = pConn.prepareStatement("delete from "+getDBName()+"..FXInfophoto  where HeaderNo = ? and fileName=?");
			pstmt.setLong(1, pHeaderNo);
			pstmt.setString(2, FileName);
			pstmt.executeUpdate();
				
			return ret;
		}
	 
	 public ArrayList<CustomerPhoto> getAttachmentPhoto(Long pHeaderNo, Connection pConn) throws SQLException, IOException {
			
			CustomerPhoto l_CusPhoto = new CustomerPhoto();
			ArrayList<CustomerPhoto> lstAtt = new ArrayList<CustomerPhoto>();
			PreparedStatement pstmt = pConn.prepareStatement("Select * from "+getDBName()+"..FXInfophoto  where HeaderNo = ?");
			pstmt.setLong(1, pHeaderNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_CusPhoto = new CustomerPhoto();
				l_CusPhoto.setCustomerID(rs.getLong("headerNo")+"");
				l_CusPhoto.setSerialNo(rs.getInt("SerialNo"));
				l_CusPhoto.setpFileName(rs.getString("fileName"));
				if(rs.getBytes("photo") != null){					
					byte[] fileBytes = rs.getBytes("photo");
					OutputStream targetFile=  new FileOutputStream(SingletonServer.getAbsPath()+"cache//photos//fx//"
					+rs.getString("fileName")+".jpg");

					targetFile.write(fileBytes);
					targetFile.close();
				}
				l_CusPhoto.setRemark(rs.getString("Remark"));
				lstAtt.add(l_CusPhoto);
			}
			return lstAtt;
		}
	
	 private void WritePhoto(Long pHeaderNo,CustomerPhoto customerPhotoBean,PreparedStatement aPS) throws IOException
		{
			try {
				File file = new File(SingletonServer.getAbsPath()+"cache//photos//fx//"+customerPhotoBean.getpFileName()+".jpg");
				if(file.isFile()){								
				FileInputStream fis = new FileInputStream(file);		

		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        byte[] buf = new byte[1024];
		     
		            for (int readNum; (readNum = fis.read(buf)) != -1;) {
		                bos.write(buf, 0, readNum); 		                
		            }
		            byte[] bytes = bos.toByteArray();
		            int i = 1;
					aPS.setLong(i++, pHeaderNo);
					aPS.setInt(i++, customerPhotoBean.getSerialNo());
					aPS.setBytes(i++, bytes);
					aPS.setString(i++, customerPhotoBean.getpFileName());
					aPS.setString(i++, customerPhotoBean.getRemark());	
					
					fis.close();
					bos.close();
					} else {
						 int i = 1;
						 aPS.setLong(i++,pHeaderNo);
						 aPS.setInt(i++, customerPhotoBean.getSerialNo());
						 aPS.setBytes(i++,null );
						 aPS.setString(i++, customerPhotoBean.getpFileName());
						 aPS.setString(i++, customerPhotoBean.getRemark());
					}
				}
		        catch (SQLException e) {
					e.printStackTrace();
				}		
		}
	 
	 public CustomerData getFXCustomerInfo(String nrcNumber,Connection conn) throws SQLException {	
		CustomerData ret = new CustomerData();
		PreparedStatement pstmt=conn.prepareStatement("SELECT Title,Name,Sex,NrcNo,Address,Phone,Email,CauseDescription FROM TransactionHeader Where NrcNo=?");

		pstmt.setString(1, nrcNumber);
		ResultSet rs=pstmt.executeQuery();

		while(rs.next()){
			ret = new CustomerData();
			ret.setTitle(rs.getString("Title"));
			ret.setName(rs.getString("Name"));
			ret.setSex(rs.getByte("Sex"));
			ret.setIC(rs.getString("NrcNo"));
			ret.setT1(rs.getString("CauseDescription"));
			ret.getAddress().setHouseNo(rs.getString("Address"));
			ret.getAddress().setTel1(rs.getString("Phone"));
			ret.getAddress().setEmail(rs.getString("Email"));
		}
		pstmt.close();
		return ret;
	}
	 
	 public FECTransactionHeaderData getDataFromTransRef(long pTransref,Connection aConn) throws SQLException
		{
			FECTransactionHeaderData pTrHeaderData = null;
			String qQuery = "Select * From TransactionHeader "+
			" Where N2 = " + pTransref;
			
			PreparedStatement pstmt = aConn.prepareStatement(qQuery);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				pTrHeaderData = new FECTransactionHeaderData();
				ReadRecord(pTrHeaderData, rs);
			}
			pstmt.close();
			
			return pTrHeaderData;
		}

}
