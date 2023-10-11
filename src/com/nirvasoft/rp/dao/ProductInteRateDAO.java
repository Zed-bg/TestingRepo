package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.CommitmentFeeData;
import com.nirvasoft.rp.shared.InterestRateForAllPTypeData;
import com.nirvasoft.rp.shared.ODTypeDescData;
import com.nirvasoft.rp.shared.ProductInterestRateData;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.util.SharedUtil;

public class ProductInteRateDAO {
	private final String mLoanCommitmentFee = "T00043";
	
	public ODTypeDescData getLoanRateByAccNo(String pAccNumber,Connection conn) throws SQLException {		
		String l_Query = "";
		ODTypeDescData object = new ODTypeDescData();
		l_Query += "SELECT A.* FROM T00033 A INNER JOIN T00005 T ON T.T2=A.T1 INNER JOIN (SELECT AccNumber,loanType,Min(SanctionDateBr) SanctionDateBr FROM Laodf" +
				" Group by AccNumber,loanType) L ON L.AccNumber=T.T1 AND A.N1=L.LoanType Where L.AccNumber=?";
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNumber);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			object = new ODTypeDescData();
			object.setAccType(rs.getString("T1"));
			object.setOdtypedesc(rs.getString("T2"));
			object.setOdtype(rs.getInt("N1"));
			object.setYearsofLife(rs.getInt("N2"));
			object.setUnchangeED(rs.getInt("N3"));
			object.setFreeSC(rs.getInt("N4"));
			object.setpN5(rs.getInt("N5"));
			object.setScheduleFormula(rs.getInt("N6"));	
          
			object.setGracePeriod(rs.getInt("N7"));
			object.setInterest(rs.getInt("N8"));
			object.setScheduleMonth(rs.getInt("N9"));
			object.setTranscation(rs.getInt("N10"));
			object.setChkschedule(rs.getInt("N11"));
			object.setRoundingby(rs.getInt("N12"));
			object.setFirstMonth(rs.getInt("N13"));
			object.setMonthEnd(rs.getInt("N14"));
			object.setExpiredType(rs.getInt("N15"));
			object.setIntcollectType(rs.getInt("N16"));
			object.setSameBackAccount(rs.getInt("N17"));
			object.setAllowByForce(rs.getInt("N18"));
		}
		rs.close();
		pstmt.close();
		return object;		
	}
	
	public ArrayList<ProductInterestRateData> getInterestRateDatasForRateChanges(String pAccNumber,String pTenure, Connection pConn) throws SQLException{
		int l_index = 1;
		ArrayList<ProductInterestRateData> lstIntRate = new ArrayList<ProductInterestRateData>();
		String l_Query = "SELECT P.* FROM ProductInterestRate P INNER JOIN T00005 ON P.AccType = T00005.T2 WHERE T00005.T1 = ? ";
		if(!pTenure.trim().equalsIgnoreCase("")){
			l_Query += "AND P.Tenure = ? ";
		}
		l_Query += "Order By P.Tenure,DateFrom,DateTo ";
		PreparedStatement pstmts = pConn.prepareStatement(l_Query);
		pstmts.setString(l_index++, pAccNumber);
		if(!pTenure.trim().equalsIgnoreCase("")){
			pstmts.setInt(l_index++, Integer.parseInt(pTenure));
		}
		
		ResultSet rs = pstmts.executeQuery();
		while(rs.next()){
			ProductInterestRateData pData = new ProductInterestRateData();
			readRecord(pData, rs);
			lstIntRate.add(pData);
		}
		return lstIntRate;
	}
	
	private void readRecord(ProductInterestRateData pData, ResultSet rs) throws SQLException{
		pData.setAccType(rs.getString("AccType"));
		pData.setTenure(rs.getInt("Tenure"));
		pData.setDateFrom(SharedUtil.formatDBDate2MIT(rs.getString("DateFrom")));
		pData.setDateTo(SharedUtil.formatDBDate2MIT(rs.getString("DateTo")));
		pData.setYearRate(rs.getDouble("YearlyRate"));
		pData.setYearlyPenaltyRate(rs.getDouble("YearlyPenaltyRate"));
		pData.setPenaltyDays(rs.getInt("PenaltyDays"));
		pData.setAmountFrom(rs.getDouble("AmountFrom"));
		pData.setAmountTo(rs.getDouble("AmountTo"));
		pData.setCalMethod(rs.getInt("CalMethod"));
		pData.setGracePeriod(rs.getInt("GracePeriod"));
	}
	
	public ArrayList<CommitmentFeeData> getLACommimentFeeForRateChanges(String pAccNumber,String pTenure, Connection pConn) throws SQLException{
		ArrayList<CommitmentFeeData> lstIntRate = new ArrayList<CommitmentFeeData>();
		String l_Query = "SELECT C.* FROM "+mLoanCommitmentFee+" C INNER JOIN T00005 ON C.T3 = dbo.T00005.T2 WHERE T00005.T1 = ? AND C.N3 = ? Order By C.T1,C.T2 ";
		PreparedStatement pstmts = pConn.prepareStatement(l_Query);
		pstmts.setString(1, pAccNumber);
		pstmts.setString(2, pTenure);
		ResultSet rs = pstmts.executeQuery();
		while(rs.next()){
			CommitmentFeeData pData = new CommitmentFeeData();
			readRecordForLoanCommitFee(pData, rs);
			lstIntRate.add(pData);
		}
		return lstIntRate;
	}

	void readRecordForLoanCommitFee(CommitmentFeeData pData,ResultSet rs) throws SQLException{
			pData.setCommitRate(rs.getDouble("N1"));
			pData.setDateFrom(rs.getString("T1"));
			pData.setDateTo(rs.getString("T2"));
			pData.setValid(rs.getBoolean("N2"));
			pData.setOdType(rs.getInt("N3"));
			pData.setOverDay(rs.getInt("N4"));
			pData.setAccType(rs.getString("T3"));
	}
	
	public ArrayList<TempData> getAllLoanRateByAccNo(String pDate,String pAccNumber,Connection conn) throws SQLException
	{
		ArrayList<TempData> l_result = new ArrayList<TempData>();
		TempData object = new TempData();		
		String l_Query = "";
		l_Query += "select p.*,l.rate as SCRate,l.datefrom as SCDateFrom,l.dateto as SCDateTo,l.valid" +
				",t33.t2 ,t33.n1,t33.n2 ,t33.n3 ,t33.n4,t33.n6,t33.n7,t33.n8,t33.n9,t33.n10,t33.n11,t33.n12," +
				"t33.n13,t33.n14,t33.n15,t33.n16,t33.n17,t33.n18,t33.n21 from productinterestrate p " +
				" INNER JOIN T00005 T ON T.T2=P.AccType INNER JOIN (SELECT AccNumber,loanType,Min(SanctionDateBr) SanctionDateBr "+
				"FROM Laodf WHERE ? BETWEEN EntryDate AND CancelDate AND AccNumber=? Group by AccNumber,loanType) LA ON LA.AccNumber=T.T1 AND P.Tenure=LA.LoanType " +
				"LEFT join loanservicecharges l on p.tenure = l.loantype and l.AccType = p.AccType and l.datefrom <= ? and l.dateto >= ? " +
				"LEFT join t00033 t33 on t33.t1 = p.acctype and t33.n1 = p.tenure " +
				"where 1=1 and p.Tenure <> 100 and p.datefrom <= ? and p.dateto >= ? ";
		
		l_Query += "and p.acctype in (SELECT T1 FROM T00001 Where N1=6 and N2=2) ";
				
		PreparedStatement pstmt = conn.prepareStatement(l_Query);	
			pstmt.setString(1, pDate);
			pstmt.setString(2, pAccNumber);
			pstmt.setString(3, pDate);
			pstmt.setString(4, pDate);
			pstmt.setString(5, pDate);
			pstmt.setString(6, pDate);	
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			object = new TempData();
			readRecordLoanRate(object, rs);
			object.setN14(rs.getInt("N6"));
			object.setN15(rs.getInt("N7"));
			object.setN16(rs.getInt("N8"));
			object.setN17(rs.getInt("N9"));
			object.setN18(rs.getInt("N10"));
			object.setN19(rs.getInt("N11"));//mtdk
			object.setN20(rs.getInt("N12"));
			object.setN21(rs.getInt("N13"));
			object.setN22(rs.getInt("N14"));
			object.setN23(rs.getInt("N15"));
			object.setN24(rs.getInt("N16"));
			object.setN25(rs.getInt("N17"));
			object.setN26(rs.getInt("N18"));
			object.setN27(rs.getDouble("YearlyRate1")); // For MEB Rate (SME Loan)
			object.setN28(rs.getInt("N21"));
			l_result.add(object);
		}
		rs.close();
		pstmt.close();	
		
		return l_result;
	}
	
	private void readRecordLoanRate(TempData pData, ResultSet rs) throws SQLException{
		pData.setT1(rs.getString("AccType"));
		pData.setN1(rs.getInt("Tenure"));
		pData.setT2(SharedUtil.formatDBDate2MIT(rs.getString("DateFrom")));
		pData.setT3(SharedUtil.formatDBDate2MIT(rs.getString("DateTo")));
		pData.setN2(rs.getDouble("YearlyRate"));
		pData.setN3(rs.getDouble("YearlyPenaltyRate"));
		pData.setN4(rs.getInt("PenaltyDays"));
		pData.setN5(rs.getDouble("AmountFrom"));
		pData.setN6(rs.getDouble("AmountTo"));
		pData.setN7(rs.getInt("CalMethod"));
		pData.setN8(rs.getDouble("SCRate"));
		pData.setT4(SharedUtil.formatDBDate2MIT(rs.getString("SCDateFrom")));
		pData.setT5(SharedUtil.formatDBDate2MIT(rs.getString("SCDateTo")));
		pData.setN9(rs.getBoolean("Valid") ? 1: 0);
		pData.setT6(rs.getString("T2"));
		pData.setN10(rs.getInt("N1"));
		pData.setN11(rs.getInt("N2"));
		pData.setN12(rs.getInt("N3"));
		pData.setN13(rs.getInt("N4"));
	}
	
}
