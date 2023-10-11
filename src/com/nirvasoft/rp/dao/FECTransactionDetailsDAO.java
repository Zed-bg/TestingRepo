package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.FECSheetDetailData;
import com.nirvasoft.rp.shared.FECTransactionDetailsData;
import com.nirvasoft.rp.shared.RefDenominationData;
import com.nirvasoft.rp.shared.Result;

public class FECTransactionDetailsDAO {
	private FECTransactionDetailsData objTrDetails;
	private ArrayList<FECTransactionDetailsData> objTrDetailsList;
	
	public void setObjTrDetails(FECTransactionDetailsData objTrDetails) {
		this.objTrDetails = objTrDetails;
	}

	public FECTransactionDetailsData getObjTrDetails() {
		return objTrDetails;
	}

	public void setObjTrDetailsList(ArrayList<FECTransactionDetailsData> objTrDetailsList) {
		this.objTrDetailsList = objTrDetailsList;
	}

	public ArrayList<FECTransactionDetailsData> getObjTrDetailsList() {
		return objTrDetailsList;
	}
	
	public void ClearProperties()
	{
		objTrDetails.ClearProperties();
		objTrDetailsList.clear();
	}
	
	public FECTransactionDetailsDAO()
	{
		setObjTrDetails(new FECTransactionDetailsData());
		setObjTrDetailsList(new ArrayList<FECTransactionDetailsData>());
	}

	private void updateRecord(FECTransactionDetailsData aTrDetailsBean, PreparedStatement aPS, boolean update) throws SQLException
	{
		int ind = 1;
		if(!update){
			aPS.setLong(ind++, aTrDetailsBean.getHeaderNo());
			aPS.setLong(ind++, aTrDetailsBean.getDetailNo());
		}
		aPS.setString(ind++, aTrDetailsBean.getTrCurCode());
		aPS.setInt(ind++, aTrDetailsBean.getTrNote());
		aPS.setString(ind++, aTrDetailsBean.getTrNoteUnit());
		aPS.setString(ind++, aTrDetailsBean.getTrNoteUnitType());
		aPS.setString(ind++, aTrDetailsBean.getTrDenoName());
		aPS.setDouble(ind++, aTrDetailsBean.getTrCurRate());
		aPS.setString(ind++, aTrDetailsBean.getTrCurOperator());
		aPS.setInt(ind++, aTrDetailsBean.getDenoQty());
		aPS.setDouble(ind++, aTrDetailsBean.getTrAmount());
		aPS.setString(ind++, aTrDetailsBean.getBaseCurCode());
		aPS.setDouble(ind++, aTrDetailsBean.getBaseAmout());
		aPS.setDouble(ind++, aTrDetailsBean.getBaseNetAmount());
		aPS.setShort(ind++, aTrDetailsBean.getDrCr());
		aPS.setInt(ind++, aTrDetailsBean.getN1());
		aPS.setInt(ind++, aTrDetailsBean.getN2());
		aPS.setInt(ind++, aTrDetailsBean.getN3());
		aPS.setDouble(ind++, aTrDetailsBean.getN4());
		aPS.setDouble(ind++, aTrDetailsBean.getN5());
		aPS.setDouble(ind++, aTrDetailsBean.getN6());
		aPS.setString(ind++, aTrDetailsBean.getT1());
		aPS.setString(ind++, aTrDetailsBean.getT2());
		aPS.setString(ind++, aTrDetailsBean.getT3());
		aPS.setString(ind++, aTrDetailsBean.getT4());
		
		if(update){
			aPS.setLong(ind++, aTrDetailsBean.getHeaderNo());
			aPS.setLong(ind++, aTrDetailsBean.getDetailNo());
		}
	}
	
	public boolean addTransactoinDetail(FECTransactionDetailsData aTrDetailsBean, Connection aConn)throws SQLException
	{
		boolean result = false;
		String query = "Insert Into TransactionDetails(HeaderNo,DetailNo,TrCurCode,TrNote,TrNoteUnit,TrNoteUnitType,TrDenoName,TrCurRate,TrCurOperator,DenoQty,TrAmount,BaseCurCode,BaseAmount,BaseNetAmount,DrCr,N1,N2,N3,N4,N5,N6,T1,T2,T3,T4) " +
			" Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		updateRecord(aTrDetailsBean, pstmt, false);
		
		if(pstmt.executeUpdate() > 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	//KMT
	public boolean updateTransactoinDetail(FECTransactionDetailsData aTrDetailsBean, Connection aConn)throws SQLException
	{
		boolean result = false;
		String query = "Set Dateformat dmy; Update TransactionDetails Set N1 = ? Where HeaderNo = ? ";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.setInt(1, aTrDetailsBean.getN1());
		pstmt.setLong(2, aTrDetailsBean.getHeaderNo());		
		if(pstmt.executeUpdate() > 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	public long GetMaxDetailsNo(Connection aConn) throws SQLException
	{
		long max = 0;
		String query = "Select Max(DetailNo) as DNo From TransactionDetails";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next())
		{
			max = rs.getLong("DNo");
		}
		pstmt.close();
		return max;
	}
	
	public boolean UpdateStatusByHeaderNo(long aHeaderNo, int aStatus, Connection aConn) throws SQLException
	{
		boolean l_result = false;
		String query = "Update TransactionDetails " +
		" Set N1 = " + aStatus + 
		" Where HeaderNo = " + aHeaderNo;
		
		/*if(objTrDetails.getN1()!=0) {
			query+=" AND N1="+objTrDetails.getN1();
		}
		*/
		PreparedStatement pstmt = aConn.prepareStatement(query);
		if(pstmt.executeUpdate() > 0)
			l_result = true;
		pstmt.close();
		
		return l_result;
	}
	
	public boolean TruncateTable(Connection aConn) throws SQLException
	{
		boolean l_result = false;
		String query = "Update TransactionDetails "+
		" Set N1 = 4";
		
		PreparedStatement pstmt = aConn.prepareStatement(query);
		if(pstmt.executeUpdate()> 0)
			l_result = true;
		pstmt.close();
		
		return l_result;
	}
	
	public ArrayList<FECTransactionDetailsData> GetDetailsList(Connection aConn) throws SQLException
	{
		ArrayList<FECTransactionDetailsData> trDetialsList = new ArrayList<FECTransactionDetailsData>();
		String query = "Select HeaderNo,TrCurCode,TrNote,TrNoteUnit,TrNoteUnitType,TrDenoName,TrCurRate," +
				"TrCurOperator,Sum(DenoQty) DenoQty,Sum(TrAmount) TrAmount,DrCr,BaseAmount FROM TransactionDetails Where HeaderNo = " + objTrDetails.getHeaderNo();
		if(objTrDetails.getDrCr()!=0){
			if(objTrDetails.getDrCr() ==1)
				query += " AND DrCr= "+objTrDetails.getDrCr() +" AND N1=3 ";
			else if(objTrDetails.getDrCr() == 2)
				query += " AND DrCr= "+objTrDetails.getDrCr() +" AND N1=2";
		}
		query += "GROUP BY HeaderNo,TrCurCode,TrNote,TrNoteUnit,TrNoteUnitType,TrDenoName,TrCurRate,TrCurOperator,DrCr,BaseAmount";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		ResultSet aRS = pstmt.executeQuery();
		int i = 0;
		while(aRS.next())
		{
			FECTransactionDetailsData trDetialsData = new FECTransactionDetailsData();
			trDetialsData.setHeaderNo(aRS.getLong("HeaderNo"));
			trDetialsData.setTrCurCode(aRS.getString("TrCurCode"));
			trDetialsData.setTrNote(aRS.getInt("TrNote"));
			trDetialsData.setTrNoteUnit(aRS.getString("TrNoteUnit"));
			trDetialsData.setTrNoteUnitType(aRS.getString("TrNoteUnitType"));
			trDetialsData.setTrDenoName(aRS.getString("TrDenoName"));
			trDetialsData.setTrCurRate(aRS.getDouble("TrCurRate"));
			trDetialsData.setTrCurOperator(aRS.getString("TrCurOperator"));
			trDetialsData.setDenoQty(aRS.getInt("DenoQty"));
			trDetialsData.setTrAmount(aRS.getDouble("TrAmount"));		
			trDetialsData.setDrCr(aRS.getShort("DrCr"));
			trDetialsData.setBaseAmount(aRS.getDouble("BaseAmount"));	
			
			trDetialsList.add(i++, trDetialsData);
		}
		pstmt.close();
		return trDetialsList;
	}
	
	public void ReadDSRecords(FECTransactionDetailsData aTrDetailsBean, ResultSet aRS) throws SQLException
	{
		aTrDetailsBean.setAutokey(aRS.getLong("autokey"));
		aTrDetailsBean.setHeaderNo(aRS.getLong("HeaderNo"));
		aTrDetailsBean.setDetailNo(aRS.getLong("DetailNo"));
		aTrDetailsBean.setTrCurCode(aRS.getString("TrCurCode"));
		aTrDetailsBean.setTrNote(aRS.getInt("TrNote"));
		aTrDetailsBean.setTrNoteUnit(aRS.getString("TrNoteUnit"));
		aTrDetailsBean.setTrNoteUnitType(aRS.getString("TrNoteUnitType"));
		aTrDetailsBean.setTrDenoName(aRS.getString("TrDenoName"));
		aTrDetailsBean.setTrCurRate(aRS.getDouble("TrCurRate"));
		aTrDetailsBean.setTrCurOperator(aRS.getString("TrCurOperator"));
		aTrDetailsBean.setDenoQty(aRS.getInt("DenoQty"));
		aTrDetailsBean.setTrAmount(aRS.getDouble("TrAmount"));
		aTrDetailsBean.setBaseCurCode(aRS.getString("BaseCurCode"));
		aTrDetailsBean.setBaseAmount(aRS.getDouble("BaseAmount"));
		aTrDetailsBean.setBaseNetAmount(aRS.getDouble("BaseNetAmount"));
		aTrDetailsBean.setDrCr(aRS.getShort("DrCr"));
		aTrDetailsBean.setN1(aRS.getInt("N1"));
		aTrDetailsBean.setN2(aRS.getInt("N2"));
		aTrDetailsBean.setN3(aRS.getInt("N3"));
		aTrDetailsBean.setN4(aRS.getInt("N4"));
		aTrDetailsBean.setN5(aRS.getInt("N5"));
		aTrDetailsBean.setN6(aRS.getInt("N6"));
		aTrDetailsBean.setT1(aRS.getString("T1"));
		aTrDetailsBean.setT2(aRS.getString("T2"));
		aTrDetailsBean.setT3(aRS.getString("T3"));
		aTrDetailsBean.setT4(aRS.getString("T4"));
		
	}
	
	//WKZ
	public ArrayList<FECTransactionDetailsData> getTrDetailForExcel(Connection aConn) throws SQLException{
		
		ArrayList< FECTransactionDetailsData> l_TrDetailList = new ArrayList<FECTransactionDetailsData>();
		
		String l_Query = "Select * from TransactionDetails Where N1 <> 4";
		PreparedStatement pStmt = aConn.prepareStatement(l_Query);
		ResultSet rs = pStmt.executeQuery();
		
		while(rs.next()){
		
			FECTransactionDetailsData l_TrDetailData = new FECTransactionDetailsData();
			ReadDSRecords(l_TrDetailData, rs);
			l_TrDetailList.add(l_TrDetailData);
			
		}
		
		return l_TrDetailList;
		
	}
	
	public boolean IsExistInTrans(RefDenominationData aRefDenoData, Connection aConn) throws Exception{
		
		int l_RecordCount = 0;
		String l_Query = "SELECT Count(D.autokey) as recordcount FROM  dbo.TransactionHeader as H " +
						 " INNER JOIN dbo.TransactionDetails as D ON " +
						 " H.HeaderNo = D.HeaderNo where H.branchcode= ? and D.TrCurCode = ? and D.TrNote= ? "+
						 " and D.TrNoteUnit = ? and D.TrNoteUnitType = ? and D.N1 <> 4 ";
		
		PreparedStatement pstmt = aConn.prepareStatement(l_Query);
		
		pstmt.setString(1,aRefDenoData.getBranchCode());
		pstmt.setString(2, aRefDenoData.getCurrencyCode());
		pstmt.setInt(3, aRefDenoData.getNote());
		pstmt.setString(4, aRefDenoData.getUnitName());
		pstmt.setString(5, aRefDenoData.getUnitType());
		
		ResultSet rs = pstmt.executeQuery();
		
		
		while(rs.next()){
			
			l_RecordCount = rs.getInt("recordcount");
		}
		pstmt.close();
		if(l_RecordCount > 0) return true;
		else return false;
		
	}
	
	//kmt
	public boolean updateStatusAndTransRef(long pHeaderNo, long pStatus, long pTransRef, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_Query = "UPDATE TransactionDetails SET N1 = ?, N2 = ? " +
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
	
	public boolean updateStatusAndTransRefForTransferIN(long pHeaderNo, long pStatus, long pTransRef, Connection pConn) throws SQLException{
		boolean l_result = false;
		String l_Query = "UPDATE TransactionDetails SET N1 = ?  ,N2=?" +
				"WHERE HeaderNo = ? AND DrCr=1";
		
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
	
	public boolean isExit(int aTransRef, Connection aConn) throws SQLException
	{
		boolean result = false;
		String pQuery = "Select count(N2)N2 from TransactionDetails Where N2 = ? ";
				
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setInt(1, aTransRef);
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
		String pQuery = "Set Dateformat dmy; Update TransactionDetails Set N1 = ? Where N2 = ? ";
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setInt(1, aStatus);
		pstmt.setInt(2, aTransRef);
		if(pstmt.executeUpdate()> 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	public boolean updateTransactoinDetails(FECTransactionDetailsData aTrDetailsBean, Connection aConn)throws SQLException
	{
		boolean result = false;
		String query = "Update TransactionDetails Set TrCurCode=?,TrNote=?,TrNoteUnit=?,TrNoteUnitType=?,TrDenoName=?,TrCurRate=?, "
					+ "TrCurOperator=?,DenoQty=?,TrAmount=?,BaseCurCode=?,BaseAmount=?,BaseNetAmount=?,DrCr=?,N1=?,N2=?,N3=?,T1=?, "
					+ "T2=?,T3=? Where HeaderNo = ? And DetailNo = ? And N1 = 1";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		updateRecord(aTrDetailsBean, pstmt, true);
		
		if(pstmt.executeUpdate() > 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	
	public Result deleteTransactoinDetails(long headerNo, Connection aConn)throws SQLException	
	{
		Result res = new Result();
		res.setState(false);
		String query = "delete from TransactionDetails Where HeaderNo = "+ headerNo+" And N1 = 1";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.executeUpdate();
		res.setState(true);
		
		pstmt.close();
		
		return res;
	}
	
		
	public boolean GetDetailsListByCurrency(Connection aConn) throws SQLException
	{
		boolean ret = false;
		objTrDetailsList = new ArrayList<FECTransactionDetailsData>();
		String query = "Select HeaderNo,TrCurCode,TrCurRate,BaseCurCode,DrCr,SUM(TrAmount) TrAmount,SUM(BaseAmount) BaseAmount " +
				"FROM TransactionDetails Where HeaderNo=" + objTrDetails.getHeaderNo();
		if(objTrDetails.getDrCr()!=0){
			if(objTrDetails.getDrCr() ==1)
				query += " AND DrCr= "+objTrDetails.getDrCr() +" AND N1=2 ";
			
			else if(objTrDetails.getDrCr() == 2)
				query += " AND DrCr= "+objTrDetails.getDrCr() +" AND N1=1 ";
			
		} else {
			query += " AND N1=1 ";
		}
		query += " Group By HeaderNo,TrCurCode,TrCurCode,BaseCurCode,DrCr,TrCurRate " ;
				
		PreparedStatement pstmt = aConn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		while(rs.next())
		{
			ret = true;
			FECTransactionDetailsData trDetialsData = new FECTransactionDetailsData();
			ReadRecordByCurrency(trDetialsData, rs);			
			objTrDetailsList.add(i++, trDetialsData);
		}
		pstmt.close();
		return ret;
	}
	
	private void ReadRecordByCurrency(FECTransactionDetailsData aTrDetailsBean,ResultSet aRS) throws SQLException{
		aTrDetailsBean.setHeaderNo(aRS.getLong("HeaderNo"));		
		aTrDetailsBean.setTrCurCode(aRS.getString("TrCurCode"));		
		aTrDetailsBean.setTrCurRate(aRS.getDouble("TrCurRate"));
		aTrDetailsBean.setTrAmount(aRS.getDouble("TrAmount"));
		aTrDetailsBean.setBaseCurCode(aRS.getString("BaseCurCode"));
		aTrDetailsBean.setBaseAmount(aRS.getDouble("BaseAmount"));
		aTrDetailsBean.setDrCr(aRS.getShort("DrCr"));
	}

	public boolean deleteDetailsByHeaderNo(Connection aConn, long aHeader) throws SQLException
	{
		boolean l_result = false;
		String pQuery = "delete from TransactionDetails where headerno = ?";
		
		PreparedStatement pstmt = aConn.prepareStatement(pQuery);
		pstmt.setLong(1, aHeader);
		if(pstmt.executeUpdate() > 0)
		{
			l_result = true;
		}
		pstmt.close();
		return l_result;
	}
	
	public ArrayList<FECTransactionDetailsData> GetDetailsListByType(int type, Connection aConn) throws SQLException
	{
		ArrayList<FECTransactionDetailsData> trDetialsList = new ArrayList<FECTransactionDetailsData>();
		String query = "Select * From TransactionDetails Where HeaderNo = " + objTrDetails.getHeaderNo() + " and DRCR = ? ";
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.setInt(1, type);
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		while(rs.next())
		{
			FECTransactionDetailsData trDetialsData = new FECTransactionDetailsData();
			ReadDSRecords(trDetialsData, rs);
			
			trDetialsList.add(i++, trDetialsData);
		}
		pstmt.close();
		return trDetialsList;
	}
	public boolean addFECSheetDetail(FECSheetDetailData pSheetData, Connection aConn)throws SQLException
	{
		boolean result = false;
		String query = "Set DateFormat dmy; INSERT INTO FECSheetDetail (HeaderNo,CurCode,Note,NoteUnit,DenoName,Type,EntryDate,T1,N4,N5) VALUES (?,?,?,?,?,?,?,?,?,?) ";
			
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.setLong(1, pSheetData.getHeaderNo());
		pstmt.setString(2, pSheetData.getCurCode());
		pstmt.setString(3, pSheetData.getNote());
		pstmt.setString(4, pSheetData.getNoteUnit());
		pstmt.setString(5, pSheetData.getDenoName());
		pstmt.setString(6, pSheetData.getType());
		pstmt.setString(7,pSheetData.getEntryDate());
		pstmt.setString(8, pSheetData.getT1());
		pstmt.setDouble(9, 0);
		pstmt.setDouble(10, 0);
		if(pstmt.executeUpdate() > 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	//delete
	public boolean deleteFECSheetDetail(String pDetailNo, Connection aConn)throws SQLException
	{
		boolean result = false;
		String query = "DELETE FROM FECSheetDetail WHERE HeaderNo=? ";
			
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.setString(1, pDetailNo);
		if(pstmt.executeUpdate() > 0)
			result = true;
		pstmt.close();
		
		return result;
	}
	//mmm
	public ArrayList<FECSheetDetailData> getSheetDetail(String pDetailNo, Connection aConn) throws SQLException
	{
		ArrayList<FECSheetDetailData> detailLst = new ArrayList<FECSheetDetailData>();
		String query = "Select HeaderNo,CurCode,Note,NoteUnit,DenoName,Type,EntryDate,T1 From FECSheetDetail Where HeaderNo = ? " ;
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.setString(1,pDetailNo);
		ResultSet rs = pstmt.executeQuery();

		while(rs.next())
		{
			FECSheetDetailData trDetialsData = new FECSheetDetailData();
			trDetialsData.setHeaderNo(rs.getLong("HeaderNo"));
			trDetialsData.setCurCode(rs.getString("CurCode"));
			trDetialsData.setNote(rs.getString("Note"));
			trDetialsData.setNoteUnit(rs.getString("NoteUnit"));
			trDetialsData.setDenoName(rs.getString("DenoName"));
			trDetialsData.setType(rs.getString("Type"));
			trDetialsData.setEntryDate(rs.getString("EntryDate"));
			trDetialsData.setT1(rs.getString("T1"));
			detailLst.add(trDetialsData);
		}
		pstmt.close();
		return detailLst;
	}
	
	
	public ArrayList<FECTransactionDetailsData> GetDetailsListByHeaderNo(long pHeaderNo, Connection aConn) throws SQLException
	{
		ArrayList<FECTransactionDetailsData> trDetialsList = new ArrayList<FECTransactionDetailsData>();
		String query = "Select * From TransactionDetails Where HeaderNo = ?" ;
		PreparedStatement pstmt = aConn.prepareStatement(query);
		pstmt.setLong(1, pHeaderNo);
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		while(rs.next())
		{
			FECTransactionDetailsData trDetialsData = new FECTransactionDetailsData();
			ReadDSRecords(trDetialsData, rs);			
			trDetialsList.add(i++, trDetialsData);
		}
		pstmt.close();
		return trDetialsList;
	}
}
