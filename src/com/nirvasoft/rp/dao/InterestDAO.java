package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.BudgetYearData;
import com.nirvasoft.rp.shared.CallDepositInterestData;
import com.nirvasoft.rp.shared.CurrentInterestData;
import com.nirvasoft.rp.shared.SavingInterestData;
import com.nirvasoft.rp.shared.SpecialInterestData;
import com.nirvasoft.rp.shared.UniversalInterestData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;
import com.nirvasoft.rp.util.GeneralUtility;

public class InterestDAO {
	
	String c_TableName = "CurrentInterestProvision";
	String s_TableName = "SavingsInterestProvision";
	String sp_TableName = "SpecialInterestProvision";
	String cl_TableName = "CallInterestProvision";
	String u_TableName = "UniversalInterestProvision";
	private static String QtrStart;
	private static String QtrEnd;
	
	public CurrentInterestData getProvidedAmountCAIType1(String pAccNo,String Date,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0;
		CurrentInterestData l_SavData = new CurrentInterestData();
		String l_Query = "";
		PreparedStatement pstmt =null;
		
			l_Query = "Select ISNULL(Sum(Interest) ,0)As Total From " + c_TableName + " Where AccNumber = ? And Status = 0 And Date < ? Group By AccNumber";
			pstmt = pConn.prepareStatement(l_Query);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, Date);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		l_SavData.setInterest(l_InterestAmt);
		
		return l_SavData;		
	}
	
	public CurrentInterestData getProvidedAmountCAIType3(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		CurrentInterestData l_SavData = new CurrentInterestData();
		String[] l_Query = new String[2];
		//Interest Amt
		 l_Query[0] = "Select ISNULL(Sum(Interest),0) As Total From " + c_TableName + " Where AccNumber = ? And Status = 0 " +
				" And Date > ? And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		//Accured Amount
		l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+c_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
		
		pstmt = pConn.prepareStatement(l_Query[1]);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		
		rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_SavData.setInterest(l_InterestAmt);
		l_SavData.setAccured(l_AccuredAmt);
		return l_SavData;
	}
	
	public CurrentInterestData getProvidedAmountCAIType4(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		CurrentInterestData l_SavData = new CurrentInterestData();
		String[] l_Query = new String[2];
		 l_Query[0] = "Select ISNUll(Sum(Interest),0) As  Total From " + c_TableName +" Where AccNumber = ? And Status = 0 And" +
		 		" Date > ? And Date < ? Group By AccNumber";
			PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, pDate2);
			pstmt.setString(3, pProvisionDate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_InterestAmt = rs.getDouble("Total");
			}
			//Accured Amount
			l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+c_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
			
			pstmt = pConn.prepareStatement(l_Query[1]);		
			pstmt.setString(1, pDate2);
			pstmt.setString(2, pAccNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				l_AccuredAmt = rs.getDouble("SumInt");
			}
			l_SavData.setInterest(l_InterestAmt);
			l_SavData.setAccured(l_AccuredAmt);
		return l_SavData;
	}
	
	public CurrentInterestData getProvidedAmountCAIType5(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		CurrentInterestData l_SavData = new CurrentInterestData();
		String l_Query = "Select ISNULL(sum(Interest),0)AS SumInt From "+c_TableName+" Where Date<=? And Accnumber= ? and Status=0  Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_SavData.setAccured(l_AccuredAmt);
		
		return l_SavData;
	}
	
	public CurrentInterestData getProvidedAmountCAIType6(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		CurrentInterestData l_SavData = new CurrentInterestData();
		String l_Query = "Select ISNULL(Sum(Interest),0) As Total From " + c_TableName +  " Where AccNumber = ? And Status = 0 " +
				"And Date>? And Date< ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("Total");
		}
		l_SavData.setAccured(l_AccuredAmt);
		
		return l_SavData;
	}
	
	public SavingInterestData getProvidedAmountSAIType1(String pAccNo,String Date,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0;
		SavingInterestData l_SavData = new SavingInterestData();
		String l_Query = "";
		
		PreparedStatement pstmt=null;
			l_Query = "Select ISNULL(Sum(Interest) ,0)As Total From " + s_TableName + " Where AccNumber = ? And Status = 0 And Date < ? Group By AccNumber";
			pstmt = pConn.prepareStatement(l_Query);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, Date);
		
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		l_SavData.setInterest(l_InterestAmt);
		
		return l_SavData;		
	}	
	
	public SavingInterestData getProvidedAmountSAIType3(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		SavingInterestData l_SavData = new SavingInterestData();
		String[] l_Query = new String[2];
		//Interest Amt
		 l_Query[0] = "Select ISNULL(Sum(Interest),0) As Total From " + s_TableName + " Where AccNumber = ? And Status = 0 " +
				" And Date > ? And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		//Accured Amount
		l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+s_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
		
		pstmt = pConn.prepareStatement(l_Query[1]);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		
		rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_SavData.setInterest(l_InterestAmt);
		l_SavData.setAccured(l_AccuredAmt);
		return l_SavData;
	}
	
	public SavingInterestData getProvidedAmountSAIType4(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		SavingInterestData l_SavData = new SavingInterestData();
		String[] l_Query = new String[2];
		 l_Query[0] = "Select ISNUll(Sum(Interest),0) As  Total From " + s_TableName +" Where AccNumber = ? And Status = 0 And" +
		 		" Date > ? And Date < ? Group By AccNumber";
			PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, pDate2);
			pstmt.setString(3, pProvisionDate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_InterestAmt = rs.getDouble("Total");
			}
			//Accured Amount
			l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+s_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
			
			pstmt = pConn.prepareStatement(l_Query[1]);		
			pstmt.setString(1, pDate2);
			pstmt.setString(2, pAccNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				l_AccuredAmt = rs.getDouble("SumInt");
			}
			l_SavData.setInterest(l_InterestAmt);
			l_SavData.setAccured(l_AccuredAmt);
		return l_SavData;
	}
	
	public SavingInterestData getProvidedAmountSAIType5(String pCode,String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		SavingInterestData l_SavData = new SavingInterestData();

		String l_Query= "Select ISNULL(sum(Interest),0)AS SumInt From ";
		if(pCode.equals("1")){
			l_Query+= " SavingsInterestProvision ";
		}else if(pCode.equals("2")){
			l_Query+= " CallInterestProvision " ;
		}else if(pCode.equals("3")){
			l_Query+= " SpecialInterestProvision" ;
		}
		l_Query+= " Where Date<= ? And Accnumber= ? and Status=0  Group By AccNumber ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		pstmt.setString(1, pProvisionDate);
		pstmt.setString(2, pAccNo);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_SavData.setAccured(l_AccuredAmt);
		
		return l_SavData;
	}
	
	public SavingInterestData getProvidedAmountSAIType6(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		SavingInterestData l_SavData = new SavingInterestData();
		String l_Query = "Select ISNULL(Sum(Interest),0) As Total From " + s_TableName +  " Where AccNumber = ? And Status = 0 " +
				"And Date>? And Date< ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("Total");
		}
		l_SavData.setAccured(l_AccuredAmt);
		
		return l_SavData;
	}
	
	public SavingInterestData getProvidedAmountSAIType7(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		SavingInterestData l_SavData = new SavingInterestData();
		String l_Query = "Select ISNULL(Sum(Interest),0) As Total From " + s_TableName +  " Where AccNumber = ? And Status = 0 " +
				"And Date>? And Date< ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("Total");
		}
		l_SavData.setInterest(l_AccuredAmt);
		
		return l_SavData;
	}
	
	public SpecialInterestData getProvidedAmountSPIType1(String paccountNumber,String l_Date, Connection pconn) throws SQLException {
		Double l_InterestAmt = 0.0;
		SpecialInterestData l_SpData = new SpecialInterestData();
		String l_Query = "";boolean isPG=false;
		
		l_Query = "Select ISNULL(Sum(Interest) ,0)As Total From " + sp_TableName + " Where AccNumber = ? And Status = 0 And Date < ? Group By AccNumber";
		
		
		PreparedStatement pstmt = pconn.prepareStatement(l_Query);
		pstmt.setString(1, paccountNumber);

			pstmt.setString(2, l_Date);

		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		l_SpData.setInterest(l_InterestAmt);
		
		return l_SpData;	
	}
	
	public SpecialInterestData getProvidedAmountSPIType3(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		SpecialInterestData l_SpData = new SpecialInterestData();
		String[] l_Query = new String[2];
		//Interest Amt
		 l_Query[0] = "Select ISNULL(Sum(Interest),0) As Total From " + sp_TableName + " Where AccNumber = ? And Status = 0 " +
				" And Date > ? And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		//Accured Amount
		l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+sp_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
		
		pstmt = pConn.prepareStatement(l_Query[1]);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		
		rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_SpData.setInterest(l_InterestAmt);
		l_SpData.setAccured(l_AccuredAmt);
		return l_SpData;
	}
	
	public SpecialInterestData getProvidedAmountSPIType4(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		SpecialInterestData l_SpData = new SpecialInterestData();
		String[] l_Query = new String[2];
		 l_Query[0] = "Select ISNUll(Sum(Interest),0) As  Total From " + sp_TableName +" Where AccNumber = ? And Status = 0 And" +
		 		" Date > ? And Date < ? Group By AccNumber";
			PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, pDate2);
			pstmt.setString(3, pProvisionDate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_InterestAmt = rs.getDouble("Total");
			}
			//Accured Amount
			l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+sp_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
			
			pstmt = pConn.prepareStatement(l_Query[1]);		
			pstmt.setString(1, pDate2);
			pstmt.setString(2, pAccNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				l_AccuredAmt = rs.getDouble("SumInt");
			}
			l_SpData.setInterest(l_InterestAmt);
			l_SpData.setAccured(l_AccuredAmt);
		return l_SpData;
	}
	
	public SpecialInterestData getProvidedAmountSPIType5(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		SpecialInterestData l_SpData = new SpecialInterestData();
		String l_Query = "Select ISNULL(sum(Interest),0)AS SumInt From "+sp_TableName+" Where Date<=? And Accnumber= ? and Status=0  Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_SpData.setAccured(l_AccuredAmt);
		
		return l_SpData;
	}
	
	public SpecialInterestData getProvidedAmountSPIType6(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		SpecialInterestData l_SpData = new SpecialInterestData();
		String l_Query = "Select ISNULL(Sum(Interest),0) As Total From " + sp_TableName +  " Where AccNumber = ? And Status = 0 " +
				"And Date>? And Date< ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("Total");
		}
		l_SpData.setAccured(l_AccuredAmt);
		
		return l_SpData;
	}
	
	public double calculateSPInterestForAccountClose(String pOpeningDate, String pAccount, String date, Connection pConn) throws SQLException{
		String fromDate = "";
		String []l_Query;
		PreparedStatement pstmt = null;
		boolean result = false;
		double interest = 0;
		
		fromDate = getMaxInterestDate(pAccount, date, pConn);
		if(fromDate == null || fromDate.equalsIgnoreCase(""))
			fromDate = StrUtil.getPreviousDateYYYYMMDD(pOpeningDate);
		
		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("SPI").getN5() == 1)
			l_Query = prepareSPInterest(pAccount, fromDate, date, pConn);
		else
			l_Query = prepareSPInterest1(pAccount, fromDate, date, pConn);
		
		for(int i=0; i<l_Query.length; i++){
			pstmt = pConn.prepareStatement(l_Query[i]);
			if(pstmt.executeUpdate() > 0)
				result = true;
		}
		if(result){
			String sql = "Select Sum(Interest) Interest From ##Special_"+pAccount+" Where AccNumber = '"+pAccount+"'";
			pstmt = pConn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				interest = rs.getDouble("Interest");
			}
		}
		return interest;		
	}
	
	public String getMaxInterestDate(String pAccount, String pDate, Connection pConn) throws SQLException{
		String maxDate = "";
		String sql = "Select Max(Date) Date From SpecialInterestProvision Where Accnumber = ? And Date < ?";
		PreparedStatement pstmt = pConn.prepareStatement(sql);
		pstmt.setString(1, pAccount);
		pstmt.setString(2, pDate);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			maxDate = SharedUtil.formatDBDate2MIT(rs.getString("Date"));
		}
		return maxDate;		
	}
	
	private String[] prepareSPInterest(String pAccount, String fromDate, String toDate, Connection pConn) throws SQLException{
		String[] l_Query = new String[6];
		int stylegetProductinterestRate= SharedLogic.getSystemData().getpSystemSettingDataList().get("SPY").getN5();
		int l_SPYN4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("SPY").getN4() ;
		int l_DaysInYear = SharedLogic.getSystemData().getpSystemSettingDataList().get("SPY").getN3() ;
		
		if(l_SPYN4 == 0){
			l_Query[0] = "Create Table ##Special_"+pAccount+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest float, AccType Varchar (10)) ";
		} else if(l_SPYN4 == 1){
			l_Query[0] = "Create Table ##Special_"+pAccount+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest Numeric(18, 3) Default 0, AccType Varchar (10)) ";
		} else if(l_SPYN4 == 2){
			l_Query[0] = "Create Table ##Special_"+pAccount+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest float, AccType Varchar (10)) ";
		}
		
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();
		AccountData l_AccData = new AccountData();
		l_AccData.setAccountNumber(pAccount);
		l_AccData = l_AccExtDAO.getData(l_AccData, pConn);
		String productCode = l_AccData.getProduct();
		
		l_Query [1] = "Insert Into ##Special_"+pAccount+"(AccNumber, CurrentBalance, StartDate, EndDate, Rate, NoDays, Interest, AccType) "
					+ "Select AccNumber, CurrentBalance, AsDate, '"+toDate+"', 0, 0, 0, '"+productCode+"' From AccBalanceArchive Where "
					+ "Accnumber = '"+pAccount+"' And AsDate > '"+fromDate+"' And AsDate < '"+toDate+ "'";
		
		if (stylegetProductinterestRate==1){	
			l_Query [2] = "Update ##Special_"+pAccount+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ "##Special_"+pAccount+" CD On P.AccType = CD.AccType INNER JOIN DrawingType D On P.DrawingType=D.DCode " 
					+ "INNER JOIN AccType A On D.DCode=A.AccType INNER JOIN T00005 T ON T.T3=A.AccountCode AND T.T1=CD.AccNumber "
					+ "Where P.AccType = " +productCode+ " And "
					+ "(CD.CurrentBalance Between P.AmountFrom And P.AmountTo) And (CD.StartDate Between P.DateFrom And P.DateTo) ";
		}else if (stylegetProductinterestRate==2) {
			l_Query [2] = "Update ##Special_"+pAccount+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ "##Special_"+pAccount+" CD On P.AccType = CD.AccType Where P.AccType = " +productCode+ " And "
					+ "(CD.CurrentBalance Between P.AmountFrom And P.AmountTo) And (CD.StartDate Between P.DateFrom And P.DateTo) ";
		}else{
			l_Query [2] = "Update ##Special_"+pAccount+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ "##Special_"+pAccount+" CD On P.AccType = CD.AccType Where P.AccType = " +productCode+ " And CD.StartDate Between P.DateFrom And P.DateTo And P.Tenure = 0 ";
		}		
		
		l_Query [3] = "Update ##Special_"+pAccount+" Set Rate = InterestRate From banktobankaccount b "
					+ "Inner Join ##Special_"+pAccount+" c On b.AccNumber = c.AccNumber  Where c.StartDate >= b.EntryDate ";
		
		l_Query [4] = "Update ##Special_"+pAccount+" Set Rate = 0 From interestratezero i Inner Join ##Special_"+pAccount+" c On i.AccNumber = c.AccNumber ";
	
		if(l_SPYN4 == 1){
			l_Query[5] = "Update ##Special_"+pAccount+" Set Interest = Convert(numeric(18,3), Convert(numeric(18,4),((CurrentBalance * Rate)/ "
			         + "Convert(Numeric(18,2), (100 * " + l_DaysInYear + ")))))";			
		} else if(l_SPYN4 == 0){
			l_Query[5] = "Update ##Special_"+pAccount+" Set Interest = (CurrentBalance * Rate )/ "
			         + "Convert(Numeric(18, 2), (100 * " + l_DaysInYear + "))";
		} else if(l_SPYN4 == 2){
			l_Query[5] = "Update ##Special_"+pAccount+" Set Interest = (CurrentBalance * Rate)/ "
			         + "Convert(Numeric(18, 2), (100 * " + l_DaysInYear + "))";
		}
		return l_Query;
	}

	private String[] prepareSPInterest1(String pAccount, String fromDate, String toDate, Connection pConn) throws SQLException{
		String[] l_Query = new String[6];
		int l_DaysInYear = SharedLogic.getSystemSettingT12N3("SPY");
		int l_SPYN7 =  SharedLogic.getSystemData().getpSystemSettingDataList().get("SPI").getN7() ;

		l_Query[0] = "Create Table ##Special_"+pAccount+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
				+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
				+ "Interest Numeric(18, 2) Default 0, AccType Varchar (10)) ";
		
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();
		AccountData l_AccData = new AccountData();
		l_AccData.setAccountNumber(pAccount);
		l_AccData = l_AccExtDAO.getData(l_AccData, pConn);
		String productCode = l_AccData.getProduct();
		
		if(l_SPYN7 == 1){	
			l_Query [1] = "Insert Into ##Special_"+pAccount+"(AccNumber, CurrentBalance, StartDate, EndDate, Rate, NoDays, Interest, AccType) "
						+ "Select AccNumber, Convert(Numeric(18,2),CurrentBalance), AsDate, '"+toDate+"', 0, 0, 0, '"+productCode+"' From AccBalanceArchive Where "
						+ "Accnumber = '"+pAccount+"' And AsDate > '"+fromDate+"' And AsDate < '"+toDate+ "'";
		}else{
			l_Query [1] = "Insert Into ##Special_"+pAccount+"(AccNumber, CurrentBalance, StartDate, EndDate, Rate, NoDays, Interest, AccType) "
					+ "Select AccNumber, Convert(bigint,CurrentBalance), AsDate, '"+toDate+"', 0, 0, 0, '"+productCode+"' From AccBalanceArchive Where "
					+ "Accnumber = '"+pAccount+"' And AsDate > '"+fromDate+"' And AsDate < '"+toDate+ "'";

		}
		l_Query [2] = "Update ##Special_"+pAccount+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ "##Special_"+pAccount+" CD On P.AccType = CD.AccType Where P.AccType = " +productCode+ " And CD.StartDate Between P.DateFrom And P.DateTo And P.Tenure = 0 ";
		
		l_Query [3] = "Update ##Special_"+pAccount+" Set Rate = InterestRate From banktobankaccount b "
					+ "Inner Join ##Special_"+pAccount+" c On b.AccNumber = c.AccNumber  Where c.StartDate >= b.EntryDate ";
		
		l_Query [4] = "Update ##Special_"+pAccount+" Set Rate = 0 From interestratezero i Inner Join ##Special_"+pAccount+" c On i.AccNumber = c.AccNumber ";
	
		if(l_SPYN7 == 1){
			l_Query[5] = "Update ##Special_"+pAccount+" Set Interest = convert(numeric(18,2),Round(((CurrentBalance * Rate * NoOfDay) / (" + l_DaysInYear +" * 100 )),2))";			
		}else{
			l_Query[5] = "Update ##Special_"+pAccount+" Set Interest = convert(numeric(18,2),Round(((CurrentBalance * Rate * NoOfDay) / (" + l_DaysInYear + " * 100 )),2,1))";	
		}
		return l_Query;
	}
	
	public boolean isInterestNoPay(String pAccount, Connection pCon) throws SQLException{
		boolean result = false;
		String sql = "Select * from interestratezero Where AccNumber = '"+pAccount+"'";
		PreparedStatement pstmt = pCon.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			result = true;
		}
		return result;		
	}
	
	public CallDepositInterestData getProvidedAmountCDIType1(String pAccNo,String Date,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0;
		CallDepositInterestData callData = new CallDepositInterestData();
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();
		String l_Query = "";
		l_Query = "Select ISNULL(Sum(Interest) ,0)As Total From " + cl_TableName + " Where AccNumber = ? And Status = 0 And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, Date);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		whichQuarter(Date, l_BudgetData);
		callData.setInterest(l_InterestAmt);
		
		return callData;		
	}
	
	public CallDepositInterestData getProvidedAmountCDIType3(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		CallDepositInterestData callData = new CallDepositInterestData();
		String[] l_Query = new String[2];
		//Interest Amt
		 l_Query[0] = "Select ISNULL(Sum(Interest),0) As Total From " + cl_TableName + " Where AccNumber = ? And Status = 0 " +
				" And Date > ? And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		//Accured Amount
		l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+cl_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
		
		pstmt = pConn.prepareStatement(l_Query[1]);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		
		rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		callData.setInterest(l_InterestAmt);
		callData.setAccured(l_AccuredAmt);
		return callData;
	}
	
	public CallDepositInterestData getProvidedAmountCDIType4(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		CallDepositInterestData callData = new CallDepositInterestData();
		String[] l_Query = new String[2];
		 l_Query[0] = "Select ISNUll(Sum(Interest),0) As  Total From " + cl_TableName +" Where AccNumber = ? And Status = 0 And" +
		 		" Date > ? And Date < ? Group By AccNumber";
			PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, pDate2);
			pstmt.setString(3, pProvisionDate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_InterestAmt = rs.getDouble("Total");
			}
			//Accured Amount
			l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+cl_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
			
			pstmt = pConn.prepareStatement(l_Query[1]);		
			pstmt.setString(1, pDate2);
			pstmt.setString(2, pAccNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				l_AccuredAmt = rs.getDouble("SumInt");
			}
			callData.setInterest(l_InterestAmt);
			callData.setAccured(l_AccuredAmt);
		return callData;
	}
	
	public CallDepositInterestData getProvidedAmountCDIType5(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		CallDepositInterestData callData = new CallDepositInterestData();
		String l_Query = "Select ISNULL(sum(Interest),0)AS SumInt From "+cl_TableName+" Where Date<=? And Accnumber= ? and Status=0  Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		callData.setAccured(l_AccuredAmt);
		
		return callData;
	}
	
	public CallDepositInterestData getProvidedAmountCDIType6(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		CallDepositInterestData callData = new CallDepositInterestData();
		String l_Query = "Select ISNULL(Sum(Interest),0) As Total From " + cl_TableName +  " Where AccNumber = ? And Status = 0 " +
				"And Date>? And Date< ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("Total");
		}
		callData.setAccured(l_AccuredAmt);
		
		return callData;
	}
	
	public static void whichQuarter(String pDate, BudgetYearData l_BData){
		int month = Integer.parseInt(pDate.substring(4,6));
		int year = Integer.parseInt(pDate.substring(0,4));
		if(month >= l_BData.getpN2() && month <= l_BData.getpN3()){
			QtrStart = year+l_BData.getpT1(); //"01/04/"+year;
			QtrEnd = year+l_BData.getpT2(); //30/06/"+year;
		}else if(month >= l_BData.getpN4() && month <= l_BData.getpN5()){
			QtrStart = year+l_BData.getpT3(); //01/07/"+year;
			QtrEnd = year+l_BData.getpT4(); //30/09/"+year;
		}else if(month >= l_BData.getpN6() && month <= l_BData.getpN7()){
			QtrStart = year+l_BData.getpT5(); //"01/10/"+year;
		    QtrEnd = year+l_BData.getpT6(); //"31/12/"+year;
		}else if(month >= l_BData.getpN8() && month <= l_BData.getpN9()){
			QtrStart = year+l_BData.getpT7(); //"01/01/"+year;
		    QtrEnd = year+l_BData.getpT8(); //"31/03/"+year;		
		}			
	}
	
	public double calculateCallInterestForAccountClose(String pOpeningDate, String pAccount, String date, Connection pConn) throws SQLException{
		String fromDate = "";
		String []l_Query;
		PreparedStatement pstmt = null;
		boolean result = false;
		double interest = 0;
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("CDI").getN6() ==8)
			fromDate = StrUtil.getPreviousDateYYYYMMDD(StrUtil.formatDDMMYYYY2MIT(StrUtil.getStartDay(date)));
		else
			fromDate = getMaxInterestDate(pAccount, date, pConn);
		if(fromDate == null || fromDate.equalsIgnoreCase(""))
			fromDate = StrUtil.getPreviousDateYYYYMMDD(pOpeningDate);;
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("CDY").getN4() == 3 && 
				SharedLogic.getSystemData().getpSystemSettingDataList().get("CDI").getN6() !=8 ){
			result = prepareInterest_MDB(pAccount, fromDate, date, pConn);
		} else {
			l_Query = prepareInterest(pAccount, fromDate, date, pConn);			
			for(int i=0; i<l_Query.length; i++){				
				pstmt =pConn.prepareStatement(l_Query[i]);
				if(pstmt.executeUpdate() > 0)
					result = true;
			}
		}
		if(result){
			String sql = "Select Sum(Interest) Interest From ##Call"+pAccount+" Where AccNumber = '"+pAccount+"'";
			pstmt = pConn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				interest = rs.getDouble("Interest");
			}
		}
		return interest;		
	}
	
	public boolean prepareInterest_MDB(String pAccount, String fromDate, String toDate, Connection conn) throws SQLException {
		boolean ret = false;
		String tblName = "##CALLINTEREST_" + pAccount;
		String tblMain = "##Call"+pAccount;
		
		String query ="If Exists (Select * From SysObjects Where Id = Object_Id('"+tblName+"') And SysStat & 0xf = 3) "
				+ "Drop Table "+tblName+" ";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		query ="If Exists (Select * From SysObjects Where Id = Object_Id('"+tblMain+"') And SysStat & 0xf = 3) "
				+ "Drop Table "+tblMain+" ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		query = "SELECT A.AccNumber, A.AsDate, A.CurrentBalance, ISNULL(LEAD(A.CurrentBalance) " +
				" OVER (PARTITION BY A.ACCNUMBER ORDER BY ASDATE), 0)ONEDAYMIN, (A.CurrentBalance - ISNULL(LEAD(A.CurrentBalance) " +
				" OVER (PARTITION BY A.ACCNUMBER ORDER BY ASDATE), 0))  DIFFBAL, " +
				" (CASE WHEN (A.CurrentBalance - ISNULL(LEAD(A.CurrentBalance) OVER (PARTITION BY A.ACCNUMBER ORDER BY ASDATE), 0)) = 0 " +
				" THEN 0 ELSE 1 END) NEWCOL, ROW_NUMBER () OVER (PARTITION BY A.ACCNUMBER ORDER BY ASDATE) ROWNUM, 0 GROUPNUM " +
				" INTO "+tblName+" FROM AccBalanceArchive A INNER JOIN T00005 B ON A.AccNumber = B.T1 INNER JOIN ACCOUNTS C " +
				" ON C.AccNumber = A.AccNumber WHERE AsDate > ? AND AsDate <= ? AND C.Status NOT IN (2, 3) " +
				" AND C.AccNumber = ? AND C.LastTransDate >= '19000101' And C.OpeningDate <= ? ORDER BY AccNumber";
		int index = 1;
		pstmt = conn.prepareStatement(query);
		pstmt.setString(index++, fromDate);
		pstmt.setString(index++, toDate);
		pstmt.setString(index++, pAccount);
		pstmt.setString(index++, toDate);
		pstmt.executeUpdate();
		
		query = "UPDATE A SET ONEDAYMIN = (CASE WHEN (B.CurrentBalance - B.ONEDAYMIN) = 0 THEN A.CurrentBalance ELSE 0 END), " +
				" DIFFBAL = (CASE WHEN (B.CurrentBalance - B.ONEDAYMIN) = 0 THEN 0 ELSE A.CurrentBalance END), " +
				" NEWCOL = (CASE WHEN (B.CurrentBalance - B.ONEDAYMIN) = 0 THEN 0 ELSE 1 END) FROM "+tblName+" A " +
				" INNER JOIN (SELECT * FROM "+tblName+" WHERE AsDate = DATEADD(DAY, -1, ?)) B ON " +
				" A.AccNumber = B.AccNumber WHERE A.AsDate = ?";
		index = 1;
		pstmt = conn.prepareStatement(query);
		pstmt.setString(index++, toDate);
		pstmt.setString(index++, toDate);
		pstmt.executeUpdate();
		
		query = "UPDATE A SET GROUPNUM = B.ROWNUM FROM "+tblName+" A INNER JOIN (SELECT AccNumber,ISNULL(LAG(p.AsDate)" +
				" OVER (PARTITION BY ACCNUMBER ORDER BY p.AsDate), ?)PreviousValue, p.AsDate, ROWNUM " +
				" FROM "+tblName+" p WHERE NEWCOL = 1 GROUP BY AccNumber, AsDate, ROWNUM) B ON A.AccNumber = B.AccNumber " +
				" AND A.AsDate BETWEEN B.PreviousValue AND B.AsDate AND NEWCOL = 0";
		index = 1;
		pstmt = conn.prepareStatement(query);
		pstmt.setString(index++, fromDate);
		pstmt.executeUpdate();
		
		query = "UPDATE A SET GROUPNUM = (CASE WHEN NEWCOL = 1 THEN ROWNUM ELSE MAXNUM END) FROM "+tblName+" A INNER JOIN " +
				" (SELECT MAX(ROWNUM) MAXNUM, AccNumber FROM "+tblName+" GROUP BY AccNumber) B ON A.AccNumber = B.AccNumber" +
				" WHERE GROUPNUM = 0";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		query = "Create Table "+tblMain+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest float, AccType Varchar (10)) ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		query = "INSERT INTO "+tblMain+" SELECT AccNumber, CurrentBalance, ?, ?, 0, COUNT(GROUPNUM) NOOFCOUNT, 0, T5.T2 " +
				" FROM "+tblName+" A INNER JOIN T00005 T5 ON T5.T1 = A.AccNumber group by AccNumber, GROUPNUM, T5.T2, CurrentBalance order by AccNumber";
		index = 1;
		pstmt = conn.prepareStatement(query);
		pstmt.setString(index++, fromDate);
		pstmt.setString(index++, toDate);
		pstmt.executeUpdate();
		
		query = "UPDATE "+tblMain+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ tblMain +" CD On P.AccType = CD.AccType INNER JOIN T00005 T5 ON T5.T2 = P.ACCTYPE Where T5.T1 = ? And "
					+ "(CD.CurrentBalance Between P.AmountFrom And P.AmountTo) And (CD.StartDate Between P.DateFrom And P.DateTo) ";
		index = 1;
		pstmt = conn.prepareStatement(query);
		pstmt.setString(index++, pAccount);
		pstmt.executeUpdate();
		
		query = "Update "+tblMain+" Set Rate = InterestRate From banktobankaccount b "
				+ "Inner Join "+tblMain+" c On b.AccNumber = c.AccNumber  Where c.StartDate >= b.EntryDate ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
	
		query = "Update "+tblMain+" Set Rate = 0 From interestratezero i Inner Join "+tblMain+" c On i.AccNumber = c.AccNumber ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		query = "Delete From "+tblMain+" Where CurrentBalance = 0 OR Rate = 0 ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		int l_DaysInYear = SharedLogic.getSystemSettingT12N3("CDY");
		query = "Update "+tblMain+" Set Interest = Convert(Numeric(18, 2), ((CurrentBalance * Rate) / (100 * "+l_DaysInYear+")) *  NoDays)";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		ret = true;
		return ret;
	}
	
	private String[] prepareInterest(String pAccount, String fromDate, String toDate, Connection pConn) throws SQLException{
		String[] l_Query = new String[5];
		int l_DaysInYear = SharedLogic.getSystemSettingT12N3("CDY");
		int gCDIN5=SharedLogic.getSystemData().getpSystemSettingDataList().get("CDI").getN5() ;
		
		if(SharedLogic.getSystemSettingT12N4("CDY") == 0){
			l_Query[0] = "Create Table ##Call"+pAccount+" (AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest Numeric(18, 2) Default 0, AccType Varchar (10)) ";
		} else if(SharedLogic.getSystemSettingT12N4("CDY") == 1){
			l_Query[0] = "Create Table ##Call"+pAccount+" (AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest Numeric(18, 3) Default 0, AccType Varchar (10)) ";
		} else if(SharedLogic.getSystemSettingT12N4("CDY") == 2){
			l_Query[0] = "Create Table ##Call"+pAccount+" (AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest float, AccType Varchar (10)) ";
		}else if(SharedLogic.getSystemSettingT12N4("CDY") == 3){
			l_Query[0] = "Create Table ##Call"+pAccount+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest Numeric(18, 2) Default 0, AccType Varchar (10)) ";
		}else if(SharedLogic.getSystemSettingT12N4("CDY") == 4){
			l_Query[0] = "Create Table ##Call"+pAccount+"(AccNumber Varchar (30), CurrentBalance Numeric(18, 2) Default 0, "
					+ "StartDate DateTime, EndDate DateTime, Rate Numeric(5, 2) Default 0, NoDays Numeric(5, 2) Default 0, "
					+ "Interest Numeric(18, 2) Default 0, AccType Varchar (10)) ";
		}
		
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();
		AccountData l_AccData = new AccountData();
		l_AccData.setAccountNumber(pAccount);
		l_AccData = l_AccExtDAO.getData(l_AccData, pConn);
		String productCode = l_AccData.getProduct();
		
		
		if (gCDIN5 > 0) {
			l_Query [1] = "Insert Into ##Call"+pAccount+" (AccNumber, CurrentBalance, StartDate, EndDate, Rate, NoDays, Interest, AccType) "
					+ "Select AccNumber, CurrentBalance - (CONVERT(DECIMAL, CurrentBalance)  % "+gCDIN5+"), AsDate, '"+toDate+"', 0, 0, 0, '"+productCode+"' From AccBalanceArchive Where "
					+ "Accnumber = '"+pAccount+"' And AsDate > '"+fromDate+"' And AsDate < '"+toDate+ "'";
		} else {
			l_Query [1] = "Insert Into ##Call"+pAccount+" (AccNumber, CurrentBalance, StartDate, EndDate, Rate, NoDays, Interest, AccType) "
					+ "Select AccNumber, CurrentBalance, AsDate, '"+toDate+"', 0, 0, 0, '"+productCode+"' From AccBalanceArchive Where "
					+ "Accnumber = '"+pAccount+"' And AsDate > '"+fromDate+"' And AsDate < '"+toDate+ "'";
		}
		
		if(SharedLogic.getSystemSettingT12N3("CDI")==1)
			l_Query [2] = "Update ##Call"+pAccount+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ "##Call"+pAccount+" CD On P.AccType = CD.AccType INNER JOIN DrawingType D On P.DrawingType=D.DCode INNER JOIN AccType A On D.DCode=A.AccType "
					+ " INNER JOIN T00005 T ON T.T1=CD.AccNumber and T.T3=A.AccountCode "
					+ "Where P.AccType = " +productCode+ " And "
					+ "(CD.CurrentBalance Between P.AmountFrom And P.AmountTo) And (CD.StartDate Between P.DateFrom And P.DateTo) ";
		else
			l_Query [2] = "Update ##Call"+pAccount+" Set Rate = YearlyRate from ProductInterestRate P Inner Join "
					+ "##Call"+pAccount+" CD On P.AccType = CD.AccType Where P.AccType = " +productCode+ " And "
					+ "(CD.CurrentBalance Between P.AmountFrom And P.AmountTo) And (CD.StartDate Between P.DateFrom And P.DateTo) ";
					
		l_Query [3] = "Update ##Call"+pAccount+" Set Rate = InterestRate From banktobankaccount b "
				+ "Inner Join ##Call"+pAccount+" c On b.AccNumber = c.AccNumber  Where c.StartDate >= b.EntryDate ";		
		
		if(SharedLogic.getSystemSettingT12N4("CDY") == 1){
			l_Query[4] = "Update ##Call"+pAccount+" Set Interest = Convert(numeric(18,3), Convert(numeric(18,4),((CurrentBalance * Rate)/ "
			         + "Convert(Numeric(18,2), (100 * " + l_DaysInYear + ")))))";			
		} else if(SharedLogic.getSystemSettingT12N4("CDY") == 0){
			l_Query[4] = "Update ##Call"+pAccount+" Set Interest = (CurrentBalance * Rate )/ "
			         + "Convert(Numeric(18, 2), (100 * " + l_DaysInYear + "))";
		} else if(SharedLogic.getSystemSettingT12N4("CDY") == 2){
			l_Query[4] = "Update ##Call"+pAccount+" Set Interest = (CurrentBalance * Rate)/ "
			         + "Convert(Numeric(18, 2), (100 * " + l_DaysInYear + "))";
		}else if(SharedLogic.getSystemSettingT12N4("CDY") == 3){
			l_Query[4] = "Update ##Call"+pAccount+" Set Interest = (CurrentBalance * Rate)/ "
			         + "Convert(Numeric(18, 2), (100 * " + l_DaysInYear + "))";
		}else if (SharedLogic.getSystemSettingT12N4("CDY") == 4) {
			l_Query[4]="Update ##Call"+pAccount+" Set Interest = Convert(numeric(18,2),(CurrentBalance * " +
					"ROUND(1*Rate/Convert(Numeric(18, 2), (100 * " + l_DaysInYear + ")),6)))";
		}
		return l_Query;
	}
	
	public UniversalInterestData getProvidedAmountUAIType1(String pAccNo,String Date,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0;
		UniversalInterestData l_UniData = new UniversalInterestData();
		String l_Query = "";
		l_Query = "Select ISNULL(Sum(Interest) ,0)As Total From " + u_TableName + " Where AccNumber = ? And Status = 0 And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, Date);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		l_UniData.setInterest(l_InterestAmt);
		
		return l_UniData;		
	}
	
	public UniversalInterestData getProvidedAmountUAIType3(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		UniversalInterestData l_UAIData = new UniversalInterestData();
		String[] l_Query = new String[2];
		//Interest Amt
		 l_Query[0] = "Select ISNULL(Sum(Interest),0) As Total From " + u_TableName + " Where AccNumber = ? And Status = 0 " +
				" And Date > ? And Date < ? Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_InterestAmt = rs.getDouble("Total");
		}
		//Accured Amount
		l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+u_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
		
		pstmt = pConn.prepareStatement(l_Query[1]);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		
		rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_UAIData.setInterest(l_InterestAmt);
		l_UAIData.setAccured(l_AccuredAmt);
		return l_UAIData;
	}
	
	public UniversalInterestData getProvidedAmountUAIType4(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_InterestAmt = 0.0,l_AccuredAmt=0.00;
		UniversalInterestData l_UAIData = new UniversalInterestData();
		String[] l_Query = new String[2];
		 l_Query[0] = "Select ISNUll(Sum(Interest),0) As  Total From " + u_TableName +" Where AccNumber = ? And Status = 0 And" +
		 		" Date > ? And Date < ? Group By AccNumber";
			PreparedStatement pstmt = pConn.prepareStatement(l_Query[0]);
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, pDate2);
			pstmt.setString(3, pProvisionDate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				l_InterestAmt = rs.getDouble("Total");
			}
			//Accured Amount
			l_Query[1] = "Select ISNUll(sum(Interest),0) AS SumInt From "+u_TableName+" Where Date<=? And Accnumber=? and  Status=0 Group By AccNumber";
			
			pstmt = pConn.prepareStatement(l_Query[1]);		
			pstmt.setString(1, pDate2);
			pstmt.setString(2, pAccNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				l_AccuredAmt = rs.getDouble("SumInt");
			}
			l_UAIData.setInterest(l_InterestAmt);
			l_UAIData.setAccured(l_AccuredAmt);
		return l_UAIData;
	}
	
	public UniversalInterestData getProvidedAmountUAIType5(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		UniversalInterestData l_UAIData = new UniversalInterestData();
		String l_Query = "Select ISNULL(sum(Interest),0)AS SumInt From "+u_TableName+" Where Date<=? And Accnumber= ? and Status=0  Group By AccNumber";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		pstmt.setString(1, pDate2);
		pstmt.setString(2, pAccNo);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("SumInt");
		}
		l_UAIData.setAccured(l_AccuredAmt);
		
		return l_UAIData;
	}
	
	public UniversalInterestData getProvidedAmountUAIType6(String pAccNo,String pDate2,String pProvisionDate,Connection pConn) throws SQLException {
		Double l_AccuredAmt=0.00;
		UniversalInterestData l_UniData = new UniversalInterestData();
		String l_Query = "Select ISNULL(Sum(Interest),0) As Total From " + u_TableName  +  " Where AccNumber = ? And Status = 0 " +
				"And Date>? And Date< ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);		
		
		pstmt.setString(1, pAccNo);
		pstmt.setString(2, pDate2);
		pstmt.setString(3, pProvisionDate);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_AccuredAmt = rs.getDouble("Total");
		}
		l_UniData.setAccured(l_AccuredAmt);
		
		return l_UniData;
	}
	
}
