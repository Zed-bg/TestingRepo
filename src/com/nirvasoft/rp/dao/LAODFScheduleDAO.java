package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.LAODFData;
import com.nirvasoft.rp.shared.LAODFScheduleData;
import com.nirvasoft.rp.shared.LoanResponse;
import com.nirvasoft.rp.shared.LoanScheduleData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.shared.SharedUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.util.StrUtil;

public class LAODFScheduleDAO {
	private String mTableName = "LAODFSchedule";
	private String mLoanTableName = "LoanAccount";
	public static String retDesc = "";
	
	public boolean checkLAODFSchedule(String accountNumber, Connection conn) throws SQLException {
		boolean res = false;
		String sql= "select count(AccNumber) cnt from LAODFSchedule where AccNumber = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, accountNumber);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			if(rs.getInt("cnt") > 0) {
				res = true;
			}
		}
		
		return res;
	}
	public LoanScheduleData getLAODFSchedule(String accountNumber, Connection conn) throws SQLException {
		ArrayList<LAODFScheduleData> ret = new ArrayList<LAODFScheduleData>();
		LoanScheduleData response = new LoanScheduleData();
		Double totPrinAmt = 0.0;
		Double totIntAmt = 0.0;
		
		String sql= "SET DATEFORMAT dMy;SELECT *, Convert(decimal(18,2), format((CASE WHEN DATEDIFF(DAY,DueDate,?) > 0 THEN" +
					" (CASE WHEN Status = 0 THEN (PaymentReq *  (DATEDIFF(DAY,DueDate,?))) * (YearlyPenaltyRate / 100) ELSE 0 END) ELSE 0 END), '###0.00')) penAmt FROM LAODFSchedule LAODFS INNER" +
					" JOIN LoanAccount LA ON LA.AccNumber = LAODFS.AccNumber INNER JOIN T00005 T5 ON LAODFS.AccNumber = T5.T1 " +
					" INNER JOIN ProductInterestRate a ON T5.T2 = A.AccType inner join t00033 b on AccType = b.T1 and Tenure = B.N1" +
					" AND LA.LoanType = B.N1 WHERE LA.ACCNUMBER=? AND ? BETWEEN A.DATEFROM AND A.DATETO AND LAODFS.status<>1 ORDER BY TermNo";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, GeneralUtility.getCurrentDate());
		pstmt.setString(2, GeneralUtility.getCurrentDate());
		pstmt.setString(3, accountNumber);
		pstmt.setString(4, GeneralUtility.getCurrentDate());
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			LAODFScheduleData data = new LAODFScheduleData();
			readRecord(rs, data);
			totPrinAmt += rs.getDouble("PrincipleAmount");
			totIntAmt += rs.getDouble("InterestAmount");
			ret.add(data);
		}
		
		sql = "SELECT A.*, B.T2, B.N1, C.BatchNo FROM " + mLoanTableName +" A INNER JOIN T00033 B ON A.LoanType=B.N1 " +
				"INNER JOIN LAODF C ON A.AccNumber=C.AccNumber INNER JOIN T00005 D ON A.AccNumber=D.T1 AND D.T2 = B.T1 WHERE A.ACCNUMBER=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, accountNumber);	
		rs = pstmt.executeQuery();
		while (rs.next()) {
			response.setLinkedaccnumber(rs.getString("AccLinked"));
			response.setBatchno(rs.getInt("BatchNo"));
			response.setLoantypedesc(rs.getString("T2"));
			response.setLoantype(rs.getShort("N1"));
		}
		
		if(ret.size() > 0) {
			response.setTotalprinamount(StrUtil.formatNumber(totPrinAmt));
			response.setTotalinterestamount(StrUtil.formatNumber(totIntAmt));
			response.setTotalcount(ret.size());
			
			response.setDatalist(ret);
		}
		return response;
	}
	
	private void readRecord(ResultSet rs, LAODFScheduleData data) throws SQLException{
		data.setAccnumber(rs.getString("AccNumber"));
		data.setTermno(rs.getInt("TermNo"));
		data.setStartdate(SharedUtil.formatDBDate2MIT(rs.getString("StartDate")));
		data.setDuedate(SharedUtil.formatDBDate2MIT(rs.getString("DueDate")));
		data.setPrincipleamount(StrUtil.formatNumber(rs.getDouble("PrincipleAmount")));
		data.setInterestamount(StrUtil.formatNumber(rs.getDouble("InterestAmount")));
		data.setRepaymentamount(StrUtil.formatNumber(rs.getDouble("PaymentReq")));
		data.setStatus(rs.getInt("Status"));
		data.setBatchno(rs.getInt("batchNo"));
		data.setPenaltyamount(StrUtil.formatNumber(rs.getDouble("penAmt")));
		data.setMebinterestamount(StrUtil.formatNumber(rs.getDouble("N2")));
		/*
		data.setEndBal(rs.getDouble("EndingBal"));
		data.setBeginnigBal(rs.getDouble("BeginningBal"));
		data.setCumulativeBal(rs.getDouble("CumulativeBal"));
		data.setSchFormular(rs.getInt("SchFormular"));
		data.setTransNo(rs.getInt("TransRef"));
		data.setTransDate(SharedUtil.formatDBDate2MIT(rs.getString("TransDate")));
		data.setN1(rs.getInt("N1"));
		data.setPrinOutAmt(rs.getDouble("n6"));
		data.setIntOutAmt(rs.getDouble("N7"));
		data.setMebIntOutAmt(rs.getDouble("n8"));
		data.setPenaltyOutAmt(rs.getDouble("n9"));
		data.setPrinOutAmt(rs.getDouble("N6"));
		data.setIntOutAmt(rs.getDouble("N7"));
		data.setMebIntOutAmt(rs.getDouble("N8"));
		data.setN4(rs.getDouble("YearlyRate1"));
		*/
	}
	
	public double getLAODAccruedAmt(LAODFScheduleData arrPost, Connection l_conn) throws SQLException {// t1=termno
		double ret = 0;
		//String sql = "SELECT SUM(INTEREST) intAmt FROM LoanAccruedInterest WHERE ACCNUMBER = ? AND N1 IN ("+arrPost.getT1()+") AND STATUS = 2";
		String sql = "SELECT SUM(INTEREST) intAmt FROM LoanAccruedInterest WHERE ACCNUMBER = ? AND N1 <="+arrPost.getTermno()+" AND STATUS = 2";
		PreparedStatement pstmt = l_conn.prepareStatement(sql);
		pstmt.setString(1, arrPost.getAccnumber());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			ret = rs.getDouble("intAmt");
		}
		return ret;
	}
	
	public double[] getLAODAccruedAmtForN6(String colName, String colName1, LAODFScheduleData arrPost, Connection l_conn) throws SQLException {
		double[] ret = new double[2];
		String sql = "SELECT SUM(case when a.Status =  0 then "+colName+" else 0 end) + Case when b.status = 2" +
				" then "+colName1+" else 0 end intAmt, SUM(case when a.Status =  0 then "+colName+" else 0 end) acturalInt FROM LoanInterest A INNER JOIN LAODFSchedule" +
				" B ON A.AccNumber = B.AccNumber AND" +
				" A.Date BETWEEN B.StartDate AND B.DueDate AND A.Date < ? WHERE A.AccNumber = ?" +
				" AND B.TermNo = ? group by b.Status, b.n7";
		PreparedStatement pstmt = l_conn.prepareStatement(sql);
		pstmt.setString(1, StrUtil.getCurrentDateyyyyMMdd());
		pstmt.setString(2, arrPost.getAccnumber());
		pstmt.setInt(3, arrPost.getTermno());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			ret[0] = rs.getDouble("intAmt");
			ret[1] = rs.getDouble("acturalInt");
		}
		return ret;
	}
	
	public boolean checkInterestMEB(LAODFScheduleData arrPost, double mebaccruedAmt, Connection l_conn) throws SQLException {
		String sql = "SELECT A.accnumber, termno, InterestAmount, SUM(B.N1) FROM LAODFSchedule A INNER JOIN" +
				" LoanInterest B ON A.AccNumber = B.AccNumber AND B.Date BETWEEN A.StartDate AND A.DueDate" +
				" WHERE A.AccNumber = ? AND TermNo <= ? AND A.STATUS = 0 AND InterestAmount = ?" +
				" group by A.accnumber, termno, InterestAmount HAVING SUM(B.N1) = ?";
		PreparedStatement pstmt = l_conn.prepareStatement(sql);
		pstmt.setString(1, arrPost.getAccnumber());
		pstmt.setInt(2, arrPost.getTermno());
		pstmt.setDouble(3, Double.parseDouble(arrPost.getInterestamount()));
		pstmt.setDouble(4, mebaccruedAmt);
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	}
	
	public ArrayList<LAODFScheduleData> getLAODFSchedule(String accNumber, int termNo, double mebAccruedInt, double accruedAmt, Connection l_conn) throws SQLException {
		ArrayList<LAODFScheduleData> ret = new ArrayList<LAODFScheduleData>();
		//String sql = "select * from LAODFSchedule where AccNumber = ? and TermNo IN ("+termNo+")";
		String sql = "select * from LAODFSchedule where AccNumber = ? and TermNo <="+termNo+" and Status<>1";
		PreparedStatement pstmt = l_conn.prepareStatement(sql);
		pstmt.setString(1, accNumber);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			LAODFScheduleData data = new LAODFScheduleData();
			readRecord1(rs, data);
			ret.add(data);
		}
		return ret;
	}
	
	private void readRecord1(ResultSet rs, LAODFScheduleData data) throws SQLException{
		data.setAccnumber(rs.getString("AccNumber"));
		data.setTermno(rs.getInt("TermNo"));
		data.setStartdate(SharedUtil.formatDBDate2MIT(rs.getString("StartDate")));
		data.setDuedate(SharedUtil.formatDBDate2MIT(rs.getString("DueDate")));
		data.setPrincipleamount(StrUtil.formatNumber(rs.getDouble("PrincipleAmount")));
		data.setInterestamount(StrUtil.formatNumber(rs.getDouble("InterestAmount")));
		data.setRepaymentamount(StrUtil.formatNumber(rs.getDouble("PaymentReq")));
		data.setStatus(rs.getInt("Status"));
		data.setBatchno(rs.getInt("batchNo"));
		data.setPenaltyamount(StrUtil.formatNumber(rs.getDouble("N9")));
		data.setMebinterestamount(StrUtil.formatNumber(rs.getDouble("N2")));
		data.setPrinoutamount(StrUtil.formatNumber(rs.getDouble("N6")));
		data.setPenaltyoutamount(StrUtil.formatNumber(rs.getDouble("n9")));
		data.setIntoutamount(StrUtil.formatNumber(rs.getDouble("N7")));
		data.setMebintoutamount(StrUtil.formatNumber(rs.getDouble("N8")));
		/*
		data.setEndBal(rs.getDouble("EndingBal"));
		data.setBeginnigBal(rs.getDouble("BeginningBal"));
		data.setCumulativeBal(rs.getDouble("CumulativeBal"));
		data.setSchFormular(rs.getInt("SchFormular"));
		data.setTransNo(rs.getInt("TransRef"));
		data.setTransDate(SharedUtil.formatDBDate2MIT(rs.getString("TransDate")));
		data.setN1(rs.getInt("N1"));
		data.setPrinOutAmt(rs.getDouble("n6"));
		data.setIntOutAmt(rs.getDouble("N7"));
		data.setMebIntOutAmt(rs.getDouble("n8"));
		data.setPenaltyOutAmt(rs.getDouble("n9"));
		data.setPrinOutAmt(rs.getDouble("N6"));
		data.setIntOutAmt(rs.getDouble("N7"));
		data.setMebIntOutAmt(rs.getDouble("N8"));
		data.setN4(rs.getDouble("YearlyRate1"));
		*/
	}
	
	public double getPenaltyAmount(LAODFScheduleData pData,int pHPType,double pPenalty, Connection pConn)throws SQLException{
		double l_penAmt = 0,l_RepaypenAmt = 0,l_penOust=0;
		String m_query = "SELECT (CASE WHEN N5=N9 THEN 0 ELSE N5 END) PenaltyAmt,RepayPenaltyAmt FROM LAODFSchedule A" +
				" INNER JOIN (SELECT ACCNUMBER, TERMNO, SUM(PenaltyAmt) RepayPenaltyAmt FROM LAODFRepaymentHistory GROUP BY ACCNUMBER," +
				" TERMNO) B ON A.AccNumber = B.ACCNUMBER AND A.TermNo = B.TERMNO WHERE A.AccNumber = ? AND A.batchNo = ?" +
				" AND A.TermNo = ? ";
		PreparedStatement m_pstmt = pConn.prepareStatement(m_query);
		m_pstmt.setString(1, pData.getAccnumber());		
		m_pstmt.setInt(2, pData.getBatchno());
		m_pstmt.setInt(3, pData.getTermno());
		ResultSet rs = m_pstmt.executeQuery();
		while(rs.next()) {
			l_penAmt = rs.getDouble("PenaltyAmt");	//21245.58
			l_RepaypenAmt = rs.getDouble("RepayPenaltyAmt"); //10000				
		}
		l_penOust = StrUtil.round2decimals(l_penAmt - l_RepaypenAmt -pPenalty);
		if(l_penOust>=0)
			l_penOust= l_penAmt;
		else if (l_penOust<0)
			l_penOust= l_penAmt+ Math.abs(l_penOust);
		m_pstmt.close();
		return l_penOust;
	}
	
	public DataResult saveHPRepaymentHistory(LAODFScheduleData data, long transref,double penAmt, String effDate, String brCode, Connection conn) throws SQLException {
		DataResult ret = new DataResult();
		
		String sql = "INSERT INTO LAODFRepaymentHistory(AccNumber,PaidForMonth,TransDate,principleAmt,InterestAmt,MebIntAmt" +
				",PenaltyAmt,CommissionAmt,TermNo," +
				"CurrencyCode,BranchCode,BatchNo,Transref,N1,N2,N3,T1,T2,T3) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		updateRecord(data, pstmt, transref, penAmt, effDate, false, brCode);	
		
		if (pstmt.executeUpdate() > 0) {
			ret.setStatus(true);
		}
		return ret;
	}
	
	private void updateRecord(LAODFScheduleData pData, PreparedStatement aPS, long transref, double penAmt, String effDate, boolean b, String brCode) throws SQLException {
		
		int i =1;
		aPS.setString(i++, pData.getAccnumber());
		aPS.setString(i++, effDate);
		aPS.setString(i++, effDate);
		aPS.setDouble(i++, StrUtil.round2decimals(Double.parseDouble(pData.getPrincipleamount())));
		aPS.setDouble(i++, StrUtil.round2decimals(Double.parseDouble(pData.getInterestamount())));
		aPS.setDouble(i++, StrUtil.round2decimals(Double.parseDouble(pData.getMebinterestamount())));
		aPS.setDouble(i++, penAmt);
		aPS.setDouble(i++, 0);
		aPS.setInt(i++, pData.getTermno());
		aPS.setString(i++, SharedLogic.getBaseCurCode());
		aPS.setString(i++, brCode);
		aPS.setInt(i++, pData.getBatchno());
		aPS.setLong(i++, transref);
		aPS.setInt(i++, 0);
		aPS.setInt(i++,0);
		aPS.setInt(i++, 0);
		aPS.setString(i++, "");
		aPS.setString(i++, "");
		aPS.setString(i++, "");
	}
	
	public DataResult updateLAODFSchedule(LAODFScheduleData data, long transactionNumber, String effDate, double accruedAmt, double mebaccruedAmt, Connection conn) throws SQLException {
		DataResult ret = new DataResult();	
		int status = 0;
		String sql = "";
		PreparedStatement pstmt;
		double totalAccrued = 0;
		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN6() == 1) {
				sql = "SELECT SUM(InterestAmt) prevIntRepaid FROM LAODFRepaymentHistory WHERE AccNumber = ? AND Transref < ? AND TermNo = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, data.getAccnumber());
				pstmt.setInt(2, (int) transactionNumber);
				pstmt.setInt(3, data.getTermno());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					totalAccrued = rs.getDouble("prevIntRepaid");
				}
		}
		int i = 1;
		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN6() == 1) 
			sql = "UPDATE LAODFSchedule SET TRANSDATE = ?, TransRef = ?, Status = ?, InterestAmount = ?, N5=?, n6=?, n7=?, n8=?, n9=?," +
				" chequeNo=? WHERE AccNumber = ? AND TERMNO = ? AND BatchNo=?";
		else
			sql = "UPDATE LAODFSchedule SET TRANSDATE = ?, TransRef = ?, Status = ?, N5=?, n6=?, n7=?, n8=?, n9=?," +
				" chequeNo=? WHERE AccNumber = ? AND TERMNO = ? AND BatchNo=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(i++, effDate);
		
		pstmt.setLong(i++, transactionNumber);
		if(Double.parseDouble(data.getPrinoutamount())==0 && Double.parseDouble(data.getIntoutamount())==0 
				&& Double.parseDouble(data.getPenaltyoutamount())==0 && Double.parseDouble(data.getMebintoutamount())==0)
			status=1;
		else
			status=2;
		pstmt.setInt(i++, status);
		if (SharedLogic.getSystemSettingT12N6("LAR") == 1)
			pstmt.setDouble(i++, Double.parseDouble(data.getInterestamount()) + Double.parseDouble(data.getIntoutamount()) + totalAccrued);
		pstmt.setDouble(i++, Double.parseDouble(data.getPenaltyamount()));
		pstmt.setDouble(i++, Double.parseDouble(data.getPrinoutamount()));
		pstmt.setDouble(i++, Double.parseDouble(data.getIntoutamount()));
		pstmt.setDouble(i++, Double.parseDouble(data.getMebintoutamount()));
		pstmt.setDouble(i++, Double.parseDouble(data.getPenaltyoutamount()));
		pstmt.setString(i++, data.getChequeno());
		pstmt.setString(i++, data.getAccnumber());
		pstmt.setInt(i++, data.getTermno());
		pstmt.setInt(i++, data.getBatchno());
		if (pstmt.executeUpdate() > 0) {
			ret.setStatus(true);
		}
		return ret;
	}
	
}
