package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.shared.LAODFData;
import com.nirvasoft.rp.shared.LoanAllowSetupData;
import com.nirvasoft.rp.shared.LoanRepaymentData;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.util.SharedUtil;

public class LAODFDAO {
	
	String mTableName = "LAODF";
	String mTableNameHistory = "LAODFHistory";
	String mLoanAccount = "LoanAccount";
	String mLoanCommitmentFee = "T00043";
	
	private LAODFData mLAODFData ;
	private ArrayList<LAODFData> mLAODFDataList ;
	
	public LAODFData getLAODFData() {
		return mLAODFData;
	}

	public void setLAODFData(LAODFData mLAODFData) {
		this.mLAODFData = mLAODFData;
	}
	
	public ArrayList<LAODFData> getLAODFDataList() {
		return mLAODFDataList;
	}

	public void setLAODFDataList(ArrayList<LAODFData> mLAODFDataList) {
		this.mLAODFDataList = mLAODFDataList;
	}
	
	
	public String getLastModifiedDate(String pAccNumber,Connection pConn) {
		String result = new String(); 
		PreparedStatement pstmt;
		try {
			pstmt = pConn.prepareStatement("select max(modified_date) as modified_date from "+mTableNameHistory+" where accnumber = '"+pAccNumber+"' and history_type = 6 ");
			
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()){
				result = GeneralUtility.formatDBDate2MIT(rs.getString("modified_date"));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public double getTotalLimtByAccNumber(String pAccNo, Connection pConn)throws ParserConfigurationException,SAXException,IOException,ClassNotFoundException,SQLException {
		double l_TotalSanctionAmtBr = 0;
		
		String l_Query = "select sum(sanctionamountbr) as sanctionamountbr from laodf where accnumber = '"+pAccNo+"' and status = 1 ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			l_TotalSanctionAmtBr = rs.getDouble("sanctionamountbr");
		}
		rs.close();
		pstmt.close();
		return l_TotalSanctionAmtBr;
	}
	
	public ArrayList<LoanRepaymentData> getMCBLoanInterestinLAODF(String pAccNumber,Connection conn) throws SQLException{
		ArrayList<LoanRepaymentData> l_dataList = new ArrayList<LoanRepaymentData>();
		LoanRepaymentData l_data = null;
		String query = "SELECT pir.YearlyRate,ISNULL(lsc.Rate,0) AS ServiceChrRate,t33.T2 AS LoanTypeDesc,laodf.LoanType,min(laodf.SanctionDateBr) as SanctionDateBr,"+
				"laodf.ExpDate,laodf.Status,sum(laodf.SanctionAmountBr) as SanctionAmountBr,a.CurrentBalance,CF.N1 AS CFRate,CF.N4 AS OverDay FROM dbo.LAODF laodf " +
				"INNER JOIN dbo.T00005 t5 ON t5.T1 = laodf.AccNumber " +
				"INNER JOIN t00033 t33 ON t33.N1 = laodf.LoanType AND t33.t1 = t5.T2 " +
				"INNER JOIN accounts a ON a.AccNumber = laodf.AccNumber " +
				"LEFT JOIN dbo.ProductInterestRate pir ON pir.AccType = t5.T2 AND laodf.LoanType = pir.Tenure AND laodf.SanctionDateBr BETWEEN pir.DateFrom AND pir.DateTo " +
				"LEFT JOIN dbo.LoanServiceCharges lsc ON lsc.AccType = t5.T2 AND lsc.LoanType = pir.Tenure AND laodf.SanctionDateBr BETWEEN lsc.DateFrom AND lsc.DateTo " +
				"LEFT JOIN "+mLoanCommitmentFee+" CF ON CF.T3 = t5.T2 AND CF.N3 = pir.Tenure AND laodf.SanctionDateBr BETWEEN CF.T1 AND CF.T2 " +
				"WHERE laodf.AccNumber = ? AND laodf.Status <> 3 "+
				"group by pir.YearlyRate,lsc.Rate,t33.T2,laodf.LoanType,laodf.ExpDate,laodf.Status,a.CurrentBalance,CF.N1,CF.N4 ";
				/*"order by laodf.serialno ";*/
		
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, pAccNumber);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_data = new LoanRepaymentData();
			l_data.setInterestrate(rs.getDouble("yearlyrate"));
			l_data.setServicechargesrate(rs.getDouble("ServiceChrRate"));
			l_data.setLoantype(rs.getInt("LoanType"));
			l_data.setLoantypedesc(rs.getString("LoanTypeDesc"));
			l_data.setSanctiondate(rs.getString("SanctionDateBr"));
			l_data.setExpdate(rs.getString("ExpDate"));
			l_data.setStatus(rs.getString("status"));
			l_data.setLoanlimit(rs.getDouble("SanctionAmountBr"));
			l_dataList.add(l_data);
		}
		rs.close();
		pstmt.close();
		return l_dataList;
	}
	
	public ArrayList<LoanRepaymentData> getLoanInterestinLAODF(String pAccNumber,Connection conn) throws SQLException{
		ArrayList<LoanRepaymentData> l_dataList = new ArrayList<LoanRepaymentData>();
		LoanRepaymentData l_data = null;
		String query = "";
		
			query = "SELECT pir.YearlyRate,ISNULL(lsc.Rate,0) AS ServiceChrRate,t33.T2 AS LoanTypeDesc,laodf.LoanType,"
					+ " min(laodf.SanctionDateBr) SanctionDateBr,max(laodf.ExpDate) ExpDate,sum(laodf.SanctionAmountBr) SanctionAmountBr FROM dbo.LAODF laodf " +
					"INNER JOIN dbo.T00005 t5 ON t5.T1 = laodf.AccNumber " +
					"INNER JOIN t00033 t33 ON t33.N1 = laodf.LoanType AND t33.t1 = t5.T2 " +
					"INNER JOIN accounts a ON a.AccNumber = laodf.AccNumber " +
					"LEFT JOIN dbo.ProductInterestRate pir ON pir.AccType = t5.T2 AND laodf.LoanType = pir.Tenure AND laodf.SanctionDateBr BETWEEN pir.DateFrom AND pir.DateTo " +
					"LEFT JOIN dbo.LoanServiceCharges lsc ON lsc.AccType = t5.T2 AND lsc.LoanType = pir.Tenure AND laodf.SanctionDateBr BETWEEN lsc.DateFrom AND lsc.DateTo " +				
					"WHERE laodf.AccNumber = ? AND laodf.Status <> 3 group by pir.YearlyRate , lsc.Rate, t33.T2, laodf.LoanType";
		
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, pAccNumber);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_data = new LoanRepaymentData();
			l_data.setInterestrate(rs.getDouble("yearlyrate"));
			l_data.setServicechargesrate(rs.getDouble("ServiceChrRate"));
			l_data.setLoantype(rs.getInt("LoanType"));
			l_data.setLoantypedesc(rs.getString("LoanTypeDesc"));
			l_data.setSanctiondate(rs.getString("SanctionDateBr"));
			l_data.setExpdate(rs.getString("ExpDate"));
			//l_data.setStatus(rs.getString("status"));
			l_data.setLoanlimit(rs.getDouble("SanctionAmountBr"));
			l_dataList.add(l_data);
		}
		rs.close();
		pstmt.close();
		return l_dataList;
	}
	
	public ArrayList<LAODFData> getLAODFDataListByAccNumber(String pAccNo,Connection pConn)throws ParserConfigurationException,SAXException,IOException,ClassNotFoundException,SQLException {
		mLAODFDataList = new ArrayList<LAODFData>();
		String l_Query = "SELECT A.*,B.AccLinked,0 as CurrentBalance FROM "+mTableName+" A inner join "+mLoanAccount+" B on A.AccNumber = B.AccNumber Where 1 = 1 and A.AccNumber = B.AccNumber ";
		if(!pAccNo.equals("")){
			l_Query += "And A.AccNumber = ? ";
		}
		
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		if(!pAccNo.equals("")){
			pstmt.setString(1, pAccNo);
		}
		ResultSet rs = pstmt.executeQuery();
		 int i = 0;
		while(rs.next()){
			mLAODFData = new LAODFData();
        	readRecords(mLAODFData, rs, "Bwr");
        	mLAODFDataList.add(i++, mLAODFData);
		}
		pstmt.close();
		return mLAODFDataList;
	}
	
	private void readRecords(LAODFData aLAODFData,ResultSet aRS,String aFrom) {
		try {
			aLAODFData.setSerialNo(aRS.getInt("SerialNo"));
			aLAODFData.setAccNumber(aRS.getString("AccNumber"));
			aLAODFData.setEntryDate(SharedUtil.formatDBDate2MIT(aRS.getString("EntryDate")));
			aLAODFData.setSanctionDateHO(SharedUtil.formatDBDate2MIT(aRS.getString("SanctionDateHO")));
			aLAODFData.setSanctionDateBr(SharedUtil.formatDBDate2MIT(aRS.getString("SanctionDateBr")));
			aLAODFData.setSanctionAmountHO(aRS.getDouble("SanctionAmountHO"));
			aLAODFData.setSanctionAmountBr(aRS.getDouble("SanctionAmountBr"));
			aLAODFData.setUsageAmt(aRS.getDouble("UsageAmt"));
			aLAODFData.setSanctionPercent(aRS.getInt("SanctionPercent"));
			aLAODFData.setApprovalNo(aRS.getString("ApprovalNo"));
			aLAODFData.setApprovalNoHO(aRS.getString("ApprovalNOHO"));
			aLAODFData.setStatus(aRS.getShort("Status"));
			aLAODFData.setSettleAmount(aRS.getDouble("SettleAmount"));
			aLAODFData.setBatchNo(aRS.getString("BatchNo"));
			aLAODFData.setDrPriority(aRS.getInt("DrPriority"));
			aLAODFData.setLoanType(aRS.getShort("LoanType"));
			aLAODFData.setCancelDate(SharedUtil.formatDBDate2MIT(aRS.getString("CancelDate")));
			aLAODFData.setExpDate(SharedUtil.formatDBDate2MIT(aRS.getString("ExpDate")));
			aLAODFData.setBusinessClass(aRS.getShort("BusinessClass"));
			if(aFrom.equalsIgnoreCase("Bwr")){
				aLAODFData.setAccLinked(aRS.getString("AccLinked"));
			}			
			aLAODFData.setN1(aRS.getDouble("CurrentBalance"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<LoanAllowSetupData> getLAODDataForLimitAmt(String accNumber, int loantype, Connection conn) throws SQLException {
		ArrayList<LoanAllowSetupData> l_data = new ArrayList<LoanAllowSetupData>();
		String query="";
		if (loantype!=1){
			query = "select a.AccNumber,AccLinked,Sum(Limit) as repayment,YearlyRate from LoanAccount a inner join LAODF b "
				+ " on a.AccNumber=b.AccNumber inner join ProductInterestRate c on b.LoanType=c.Tenure and c.AccType='04'"
				+ " where b.Status=1 and A.AccNumber=? group by AccLinked,A.AccNumber,YearlyRate";
		}else{
			query = "select a.AccNumber,AccLinked,Sum(Limit) as repayment from LoanAccount a inner join LAODF b "
					+ " on a.AccNumber=b.AccNumber "
					+ " where b.Status=1 and A.AccNumber=? group by AccLinked,A.AccNumber";
		}
		PreparedStatement pstmt=conn.prepareStatement(query);
		pstmt.setString(1, accNumber);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			LoanAllowSetupData data=new LoanAllowSetupData();
			data.setAccNumber(rs.getString("AccNumber"));
			data.setLimitAmount(rs.getDouble("Repayment"));
			data.setCusAccNumber(rs.getString("AccLinked"));
			if (loantype != 1)data.setYearlyRate(rs.getInt("YearlyRate"));
			data.setStatus(1);
			l_data.add(data);
		}
		return l_data;
	}
	
}
