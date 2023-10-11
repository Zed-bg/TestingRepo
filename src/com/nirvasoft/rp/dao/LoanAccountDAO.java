package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.shared.AccBalanceArchiveData;
import com.nirvasoft.rp.shared.BudgetYearData;
import com.nirvasoft.rp.shared.CommitmentFeeData;
import com.nirvasoft.rp.shared.LoanAccData;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.LoanRepaymentData;
import com.nirvasoft.rp.shared.LoanRepaymentReq;
import com.nirvasoft.rp.shared.LoanResponse;
import com.nirvasoft.rp.shared.ODTypeDescData;
import com.nirvasoft.rp.shared.ProductInterestRateData;
import com.nirvasoft.rp.shared.ServiceChargesData;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;
import com.nirvasoft.rp.mgr.DBSystemMgr;

public class LoanAccountDAO {
	private LoanAccountData LoanAccBean;
	private ArrayList<LoanAccountData> lstLoanAccBean;
	private String mLoanAccount = "LoanAccount";
	
	public void setLoanAccBean(LoanAccountData loanAccBean) {
		LoanAccBean = loanAccBean;
	}
	
	public LoanAccountData getLoanAccBean() {
		return LoanAccBean;
	}
	
	public void setLstLoanAccBean(ArrayList<LoanAccountData> lstLoanAccBean) {
		this.lstLoanAccBean = lstLoanAccBean;
	}
	
	public ArrayList<LoanAccountData> getLstLoanAccBean() {
		return this.lstLoanAccBean;
	}
	
	public LoanAccountDAO() {
		LoanAccBean = new LoanAccountData();
		lstLoanAccBean =  new ArrayList<LoanAccountData>();
	}
	
	public boolean isUseForLoanAccount(String pAccountNumber,Connection pConn) throws SQLException {
		boolean result = false ;
		int Status = 0;
		ArrayList<Integer> l_StatusList = new ArrayList<Integer>(); 
		boolean l_IsCancel = true;
		
		PreparedStatement pstmt = pConn.prepareStatement("SELECT L.AccNumber,L.AccLinked,A.Status FROM dbo.LoanAccount L " +
				"INNER JOIN Accounts A ON A.AccNumber = L.AccNumber WHERE L.AccLinked = ? ");
		pstmt.setString(1,pAccountNumber);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			Status = rs.getInt("Status");
			l_StatusList.add(Status);
			
		}
		
		for(int i=0; i < l_StatusList.size(); i++){
			if(l_StatusList.get(i) != AccountData.Status_Closed){
				l_IsCancel = false;
			}
		}
		
		if(!l_IsCancel){
			result = true;
		}else{
			result = false;
		}
		return result ;
	}
	
	public boolean getActiveLoanAccount(String pAccNumber,Connection pConn)throws SQLException {
		boolean l_Result = false;
		
		PreparedStatement pstmt = pConn.prepareStatement("select a.*,T.T2 ProductID from "+mLoanAccount+
				" a inner join accounts b on a.acclinked = b.accnumber and b.status not in (3) and a.accnumber = ? " +
				" inner join T00005 T on a.AccNumber=T.t1");		

		pstmt.setString(1,pAccNumber);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			readRecordLoan(LoanAccBean, rs);
			l_Result = true;
		}
		return l_Result;
	}
	
	private void readRecordLoan(LoanAccountData p_loanaccountdata,ResultSet p_rs) throws SQLException {		
		
		p_loanaccountdata.setAccNumber(p_rs.getString("AccNumber"));
		p_loanaccountdata.setTerm(p_rs.getByte("Term"));
		p_loanaccountdata.setLimit(p_rs.getDouble("Limit"));
		p_loanaccountdata.setExpDate(SharedUtil.formatDBDate2MIT(p_rs.getString("ExpDate")));
		p_loanaccountdata.setIntToBeCollected(p_rs.getDouble("IntToBeCollected"));
		p_loanaccountdata.setBusinessType(p_rs.getByte("BusinessType"));
		p_loanaccountdata.setCollateral(p_rs.getDouble("Collateral"));
		p_loanaccountdata.setLoanType(p_rs.getByte("LoanType"));
		p_loanaccountdata.setApproveNo(p_rs.getString("ApproveNo"));
		p_loanaccountdata.setApproveId(p_rs.getString("ApproveId"));
		p_loanaccountdata.setSecurityType(p_rs.getByte("SecurityType"));
		p_loanaccountdata.setDrawnDownDate(SharedUtil.formatDBDate2MIT(p_rs.getString("DrawnDownDate")));
		p_loanaccountdata.setRepaymentAmount(p_rs.getDouble("RepaymentAmount"));
		p_loanaccountdata.setNoOfRepayment(p_rs.getInt("NoOfRepayment"));
		p_loanaccountdata.setRepaymentStartDate(SharedUtil.formatDBDate2MIT(p_rs.getString("RepaymentStartDate")));
		p_loanaccountdata.setRepaymentDueDate(SharedUtil.formatDBDate2MIT(p_rs.getString("RepaymentDueDate")));
		p_loanaccountdata.setAccLinked(p_rs.getString("AccLinked"));
		p_loanaccountdata.setIsCommit(p_rs.getByte("IsCommit"));
		p_loanaccountdata.setProductID(p_rs.getString("ProductID"));		
		
	}
	
	public boolean getLoanAccount(String pAccNumber,Connection pConn)throws SQLException {
		boolean l_Result = false;
		
		PreparedStatement pstmt = pConn.prepareStatement("SELECT a.AccNumber,a.AccLinked,b.CurrentBalance,T.T2 ProductID FROM "+mLoanAccount+" a "
				+ " inner join accounts b on a.AccNumber = b.accnumber and b.status not in (3)  inner join T00005 T on a.AccNumber=T.t1" +
				" WHERE a.AccNumber = ? ");
		pstmt.setString(1,pAccNumber);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			LoanAccBean = new LoanAccountData();
			readRecordLoan2(LoanAccBean, rs);
			l_Result = true;
		}
		return l_Result;
	}
	
	private void readRecordLoan2(LoanAccountData p_loanaccountdata,ResultSet p_rs) throws SQLException {
		p_loanaccountdata.setAccNumber(p_rs.getString("AccNumber"));
		p_loanaccountdata.setAccLinked(p_rs.getString("AccLinked"));
		p_loanaccountdata.setCurrentBalance(StrUtil.round2decimals(p_rs.getDouble("CurrentBalance")));
		p_loanaccountdata.setProductID(p_rs.getString("ProductID"));
	}
	public boolean getLoanAccountByAccNumber(String pAccLinked,Connection pConn)throws SQLException {
		boolean l_Result = false;
		lstLoanAccBean = new ArrayList<LoanAccountData>();
		
		PreparedStatement pstmt = pConn.prepareStatement("SELECT a.*,T.T2 ProductID FROM "+mLoanAccount+" A INNER JOIN " +
				"(SELECT Distinct AccNumber FROM Laodf Where status<>3) B ON A.AccNumber=B.AccNumber " +
				"INNER JOIN T00005 T on a.AccNumber=T.t1 WHERE AccLinked = ? ");
		pstmt.setString(1,pAccLinked);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			LoanAccBean = new LoanAccountData();
			readRecordLoan(LoanAccBean, rs);
			lstLoanAccBean.add(LoanAccBean);
			l_Result = true;
		}
		return l_Result;
	}
	
	public LoanResponse getLoanInformation(String pAccNumber,Connection conn) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, ParseException{
		LoanResponse response = new LoanResponse();
		LoanAccData l_data = new LoanAccData();
		
		//get Link acc
		LoanAccountData link_acc_data = new LoanAccountData();
		if (getLoanAccount(pAccNumber, conn)) {             
				 	link_acc_data = getLoanAccBean();
				 	l_data.setLoanaccnumber(link_acc_data.getAccNumber());
				 	l_data.setLinkedaccnumber(link_acc_data.getAccLinked());
				 	l_data.setOutstandingbalance(StrUtil.formatNumber(link_acc_data.getCurrentBalance()));
				 	//get repayment list
					LoanRepaymentData l_lst = new LoanRepaymentData();
					l_lst = getLoanUsageAmount(pAccNumber,conn);
					l_data.setInterestrate(StrUtil.round2decimals(l_lst.getInterestrate()) );
					l_data.setInterestamount(StrUtil.formatNumber(l_lst.getInterestamount()));
					l_data.setServicechargesrate(StrUtil.round2decimals(l_lst.getServicechargesrate()));
					l_data.setServicechargesamount(StrUtil.formatNumber(l_lst.getServicechargesamount()));
					l_data.setLoantype(l_lst.getLoantype());
					l_data.setLoantypedesc(l_lst.getLoantypedesc());
					l_data.setSanctiondate(l_lst.getSanctiondate());
					l_data.setExpdate(l_lst.getExpdate());
					l_data.setLoanlimit(StrUtil.formatNumber(l_lst.getLoanlimit()));
					
					response.setDatalist(l_data);
					response.setRetcode("300");
					response.setRetmessage("Successful.");
		}else {
					response.setRetcode("220");
					response.setRetmessage("No Data Record.");
		}
		
		return response;
	}
	
	public boolean getLoanAccData(String pAccNumber,Connection pConn)throws SQLException {
		boolean l_Result = false;
		String query = "select l.AccNumber , l.AccLinked, a.CurrentBalance from LoanAccount l "
				+ " inner join Accounts a on a.AccNumber=l.AccNumber  WHERE l.AccNumber = ? ";
		PreparedStatement pstmt = pConn.prepareStatement(query);
		pstmt.setString(1,pAccNumber);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			readRecordLoanData(LoanAccBean, rs);
			l_Result = true;
		}
		return l_Result;
	}
	private void readRecordLoanData(LoanAccountData p_loanaccountdata,ResultSet p_rs) throws SQLException {		
		
		p_loanaccountdata.setAccNumber(p_rs.getString("AccNumber"));
		p_loanaccountdata.setAccLinked(p_rs.getString("AccLinked"));
		p_loanaccountdata.setCurrentBalance(p_rs.getDouble("CurrentBalance"));
		
	}
	public static LoanRepaymentData getLoanUsageAmount(String pAccNumber,Connection conn) throws SQLException, ParseException, 
		ParserConfigurationException, SAXException, IOException, ClassNotFoundException{
		double l_InterestAmt = 0.00;
		double l_ServiceChrAmt = 0.00;
		double l_CommitmentFeeAmt = 0.00;
		double interestrate = 0.00;
		double servicechrrate = 0.00;	
		double commitmentfeerate = 0.00;
		double l_UnusageBal = 0;
		double l_OverDays = 0;
		boolean l_IsMCBLoan = false;
		Date l_SanctionDateBr = null;
		Date l_SanctionQuarterEndDate = null;
		BudgetYearData l_BYearData=new BudgetYearData();
		l_BYearData=DBSystemMgr.getBYearData(1);
		ODTypeDescData l_loanRateData = new ODTypeDescData();
		ProductInteRateDAO l_ProdDao = new ProductInteRateDAO();		
		Date l_ThisMonthQuarterEndDate ;
		String l_TransactionDate="" ;	
		int mAccrueSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5();
		LoanInterestDAO l_LoanIntDao = new LoanInterestDAO();
		Date l_MonthStartDate =  new Date();
		String l_MonthSDate = "";
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		int l_NoOfSCDays = 0;		
		l_TransactionDate =SharedUtil.formatDDMMYYYY2MIT(GeneralUtility.getCurrentDate());		
		l_loanRateData = l_ProdDao.getLoanRateByAccNo(pAccNumber, conn);
		if(l_loanRateData.getIntcollectType()==0)
			l_ThisMonthQuarterEndDate = StrUtil.getQuarterEndDateInDate(l_TransactionDate, l_BYearData);
		else
			l_ThisMonthQuarterEndDate = StrUtil.getFirstDateInDate(l_TransactionDate);
		
		l_MonthStartDate = StrUtil.getFirstDateInDate(l_TransactionDate);
		l_MonthSDate = df.format(l_MonthStartDate);
		l_IsMCBLoan = false;
		
		String l_CurrentDate = df.format(new Date());
		String year = l_CurrentDate.substring(0,4);
		
		String l_quarterDate = df.format(l_ThisMonthQuarterEndDate);
		String qmonth = l_quarterDate.substring(4,6);
		
		String[] result = new String[3];
		result[0] = String.valueOf(Integer.parseInt(qmonth)-2);
		result[1] = String.valueOf(Integer.parseInt(qmonth)-1);
		result[2] = qmonth;
		
		int daysinyear = 0;
		boolean isleapyear = StrUtil.IsLeapYear(Integer.parseInt(year));
		int sys_LAY_N2 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAY").getN2();
		int sys_LAY_N3 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAY").getN3();
		int sys_LAY_N4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAY").getN4();

		int sys_LAREPAYMENT_N4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAREPAYMENT").getN4();
		
		int sys_LAR_N1 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN1();
		int sys_LAR_N2 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN2();
		int sys_LAR_N3 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN3();
		int sys_LAR_N5 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5();
		
		int sys_LAI_N7 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAI").getN7();
		
		int sys_LSC_N1 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LSC").getN1();
		int sys_LSC_N5 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LSC").getN5();
		String sys_LSC_T3 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LSC").getT3();
		
		// According to BCE
		if( sys_LAY_N2 == 0){
			daysinyear =  sys_LAY_N3;
		}else{
			if(isleapyear){
				daysinyear = 366;
			}else{
				daysinyear = sys_LAY_N3;
			}
		}
		
		ArrayList<LoanRepaymentData> l_Result = new ArrayList<LoanRepaymentData>();
		ArrayList<AccBalanceArchiveData> l_AccBalResult = new ArrayList<AccBalanceArchiveData>();
		
		ArrayList<ProductInterestRateData> lstPrdIntRate = new ArrayList<ProductInterestRateData>();
		ArrayList<ServiceChargesData> lstSrvCharges = new ArrayList<ServiceChargesData>();
		ArrayList<CommitmentFeeData> lstCommitmentFee = new ArrayList<CommitmentFeeData>();
		ServiceChargesDAO l_SCDao = new ServiceChargesDAO();
		ProductInteRateDAO l_PrdIntDao = new ProductInteRateDAO();
		AccBalanceArchiveDAO l_balAriDao = new AccBalanceArchiveDAO();
		LAODFDAO l_LAODFDAO = new LAODFDAO();
		Date l_FromDate;
		Date l_ToDate;
		
		LoanRepaymentData l_data = null;
		String l_LRDate = "19000101";
		
		if ( sys_LAREPAYMENT_N4 !=1){
			l_LRDate = l_LAODFDAO.getLastModifiedDate(pAccNumber, conn);
		}
		
		if(l_LRDate.equalsIgnoreCase("")){
			l_LRDate = "19000101";
		}
		Date l_LastRepaymentDate = df.parse(l_LRDate);
		double l_TotalSanctionAmtBr = 0;
		l_TotalSanctionAmtBr = l_LAODFDAO.getTotalLimtByAccNumber(pAccNumber, conn);
		
		if(mAccrueSetting !=2 ) {
			l_AccBalResult = l_balAriDao.getLoanInterestInABA(pAccNumber, year, result, l_CurrentDate, conn);
		}		
		
		if(l_IsMCBLoan){			
			l_Result = l_LAODFDAO.getMCBLoanInterestinLAODF(pAccNumber, conn);
		}else{
			l_Result = l_LAODFDAO.getLoanInterestinLAODF(pAccNumber, conn);
		}
		if(l_Result.size() > 0){			
			l_SanctionDateBr = df.parse(SharedUtil.formatDBDate2MIT(l_Result.get(0).getSanctiondate()));
			l_SanctionQuarterEndDate = StrUtil.getQuarterEndDateInDate(SharedUtil.formatDDMMYYYY2MIT(StrUtil.formatDate2DDMMYYYY(l_SanctionDateBr)), l_BYearData);
					
			if(l_SanctionQuarterEndDate.compareTo(l_ThisMonthQuarterEndDate) != 0){				
				
				Date l_FirstDateOfThisMonth = StrUtil.getFirstDateInDate(StrUtil.formatDDMMYYYY2MIT(GeneralUtility.getCurrentDate()));
				
				if(l_FirstDateOfThisMonth.compareTo(l_LastRepaymentDate) >= 0){
					l_NoOfSCDays = StrUtil.getDaysBetweentwoDates(l_FirstDateOfThisMonth, StrUtil.formatYYYYMMDDToDate(StrUtil.formatDDMMYYYY2MIT((GeneralUtility.getCurrentDate())))) + 1;
				}else{
					l_NoOfSCDays = StrUtil.getDaysBetweentwoDates(l_LastRepaymentDate, StrUtil.formatYYYYMMDDToDate(StrUtil.formatDDMMYYYY2MIT((GeneralUtility.getCurrentDate())))) + 1;
				}
			}
		
			if( sys_LAR_N1 != 0){
				lstPrdIntRate = l_PrdIntDao.getInterestRateDatasForRateChanges(pAccNumber, String.valueOf(l_Result.get(0).getLoantype()), conn);
			}
			if( sys_LAR_N2 != 0){
				lstSrvCharges = l_SCDao.getLoanServiceChargesForRateChanges(pAccNumber, String.valueOf(l_Result.get(0).getLoantype()), conn);
			}
			if(l_IsMCBLoan){
				if( sys_LAR_N3 != 0) {
					lstCommitmentFee = l_PrdIntDao.getLACommimentFeeForRateChanges(pAccNumber, String.valueOf(l_Result.get(0).getLoantype()), conn);			
				}
			}
	
			// For Interest
			Date l_Date = df.parse(SharedUtil.formatDDMMYYYY2MIT(GeneralUtility.getCurrentDate()));				
			if( sys_LAR_N1 == 3){
				for(int k = 0; k < lstPrdIntRate.size();k++){
					l_FromDate = df.parse(lstPrdIntRate.get(k).getDateFrom());
					l_ToDate = df.parse(lstPrdIntRate.get(k).getDateTo());
					
					if(l_Date.compareTo(l_FromDate) >= 0 && l_Date.compareTo(l_ToDate) <= 0){
						interestrate = lstPrdIntRate.get(k).getYearRate();								
					}
				}
			}else if( sys_LAR_N1 == 2){
				interestrate = lstPrdIntRate.get(lstPrdIntRate.size()-1).getYearRate();
			} else if ( sys_LAR_N1 == 1) {
				for(int k = 0; k < lstPrdIntRate.size();k++){
					l_FromDate = df.parse(lstPrdIntRate.get(k).getDateFrom());
					l_ToDate = df.parse(lstPrdIntRate.get(k).getDateTo());
					
					if(l_SanctionDateBr.compareTo(l_FromDate) >= 0 && l_SanctionDateBr.compareTo(l_ToDate) <= 0){
						interestrate = lstPrdIntRate.get(k).getYearRate();								
					}
				}
			}
			if( sys_LAR_N2 == 0){
				servicechrrate = l_Result.get(0).getServicechargesrate();
			}	
			
			// For Service charges
			if( sys_LSC_N1 == 0 || (sys_LSC_N1 == 1 && sys_LSC_N5 == 3)){
				if( sys_LAR_N2 == 2){
					servicechrrate = lstSrvCharges.get(lstSrvCharges.size()-1).getRate();
				}
				// LSC means loan service charges 
				if( sys_LAY_N4 == 2){
					l_ServiceChrAmt = StrUtil.round3decimalsHALFDOWN(StrUtil.round4decimalsDOWN((Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear)) *1000;
				}else if( sys_LAY_N4 == 0){
					l_ServiceChrAmt = (Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear ;
				}else if( sys_LAY_N4 == 1){
					l_ServiceChrAmt = StrUtil.RoundHALFUP((Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear);
				}else if( sys_LAY_N4 == 3){
					// 3 for MCB
					l_ServiceChrAmt = StrUtil.round3decimalsHALFDOWN((Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear);
				}
				
				l_ServiceChrAmt = l_ServiceChrAmt * l_NoOfSCDays;
				
				if( sys_LAY_N4 == 3){
					l_ServiceChrAmt = StrUtil.round1decimalsHALFDOWN(l_ServiceChrAmt);
				}
			}
			
			if(mAccrueSetting !=2 ) {
				for(int j=0; j < l_AccBalResult.size(); j++){
					Date l_AccBalDate = df.parse(SharedUtil.formatDBDate2MIT(l_AccBalResult.get(j).getAsDate()));
					l_UnusageBal = l_TotalSanctionAmtBr - Math.abs(l_AccBalResult.get(j).getCurrentBalance());
					
					// For Service charges
					if( sys_LSC_N1 == 0 ||  (sys_LSC_N1 == 1 && sys_LSC_N5 == 3)){
						if( sys_LAR_N2 == 1){
							for(int k = 0; k < lstSrvCharges.size();k++){
								l_FromDate = df.parse(lstSrvCharges.get(k).getDateFrom());
								l_ToDate = df.parse(lstSrvCharges.get(k).getDateTo());
								if(l_AccBalDate.compareTo(l_FromDate)>= 0 && l_AccBalDate.compareTo(l_ToDate) <= 0){
									servicechrrate = lstSrvCharges.get(k).getRate();break;								
								}
							}
						}
						
						if(l_SanctionQuarterEndDate.compareTo(l_ThisMonthQuarterEndDate) != 0){
							if(l_AccBalDate.compareTo(l_LastRepaymentDate) >= 0){
								// LSC means loan service charges 
								if( sys_LAY_N4 == 2){
									l_ServiceChrAmt += StrUtil.round3decimalsHALFDOWN(StrUtil.round4decimalsDOWN((Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear)) *1000;
								}else if( sys_LAY_N4 == 0){
									l_ServiceChrAmt += (Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear ;
								}else if( sys_LAY_N4 == 1){
									l_ServiceChrAmt += StrUtil.RoundHALFUP((Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear);
								}else if( sys_LAY_N4 == 3){
									// 3 for MCB
									l_ServiceChrAmt += StrUtil.round3decimalsHALFDOWN((Math.abs(l_TotalSanctionAmtBr) * (servicechrrate/100)) / daysinyear);
								}
							}
						}
					}
					
					// For Interest
					if( sys_LAR_N1 == 3){
						for(int k = 0; k < lstPrdIntRate.size();k++){
							l_FromDate = df.parse(lstPrdIntRate.get(k).getDateFrom());
							l_ToDate = df.parse(lstPrdIntRate.get(k).getDateTo());
							
							if(l_AccBalDate.compareTo(l_FromDate) >= 0 && l_AccBalDate.compareTo(l_ToDate) <= 0){
								interestrate = lstPrdIntRate.get(k).getYearRate();								
							}
						}
					}else if(sys_LAR_N1 == 2){
						interestrate = lstPrdIntRate.get(lstPrdIntRate.size()-1).getYearRate();
					} else if ( sys_LAR_N1 == 1) {
						for(int k = 0; k < lstPrdIntRate.size();k++){
							l_FromDate = df.parse(lstPrdIntRate.get(k).getDateFrom());
							l_ToDate = df.parse(lstPrdIntRate.get(k).getDateTo());
							
							if(l_SanctionDateBr.compareTo(l_FromDate) >= 0 && l_SanctionDateBr.compareTo(l_ToDate) <= 0){
								interestrate = lstPrdIntRate.get(k).getYearRate();								
							}
						}
					}
					
					// For Commitment Fee
					if( sys_LAR_N3 == 1){
						for(int k = 0; k < lstCommitmentFee.size();k++){
							l_FromDate = df.parse(lstCommitmentFee.get(k).getDateFrom());
							l_ToDate = df.parse(lstCommitmentFee.get(k).getDateTo());
							
							if(l_AccBalDate.compareTo(l_FromDate) >= 0 && l_AccBalDate.compareTo(l_ToDate) <= 0){
								commitmentfeerate = lstCommitmentFee.get(k).getCommitRate();
								l_OverDays = lstCommitmentFee.get(k).getOverDay();
								break;
							}
						}
					}else if( sys_LAR_N3 == 2){
						commitmentfeerate = lstCommitmentFee.get(lstCommitmentFee.size()-1).getCommitRate();
						l_OverDays = lstCommitmentFee.get(lstCommitmentFee.size()-1).getOverDay();					
					}
					
					Calendar l_Calendar = Calendar.getInstance();
					l_Calendar.clear();
					l_Calendar.setTime(l_SanctionDateBr);
					l_Calendar.add(Calendar.DAY_OF_MONTH, (int)l_OverDays);
					Date l_CommitmentFeeStartDate = l_Calendar.getTime();
									
					if(l_AccBalDate.compareTo(l_LastRepaymentDate) >= 0){
						// For Interest
						if( sys_LAY_N4 == 2){
							l_InterestAmt += StrUtil.round3decimalsHALFDOWN(StrUtil.round4decimalsDOWN((Math.abs(l_AccBalResult.get(j).getCurrentBalance()) * (interestrate/100)) / daysinyear)) *1000;						
						}else if( sys_LAY_N4 == 0){
							l_InterestAmt += (Math.abs(l_AccBalResult.get(j).getCurrentBalance()) * (interestrate/100)) / daysinyear;
						}else if( sys_LAY_N4 == 1){
							if( sys_LAR_N5 == 2)
								l_InterestAmt += StrUtil.RoundHALFUP((Math.abs(l_AccBalResult.get(j).getCurrentBalance()) * (interestrate/100)) / daysinyear);
							else
								l_InterestAmt += (Math.abs(l_AccBalResult.get(j).getCurrentBalance()) * (interestrate/100)) / daysinyear;
						}else if( sys_LAY_N4 == 3){
							// 3 for MCB
							l_InterestAmt += StrUtil.round3decimalsHALFDOWN((Math.abs(l_AccBalResult.get(j).getCurrentBalance()) * (interestrate/100)) / daysinyear);						
						}
						
						// For Commitment Fee
						if( sys_LSC_T3.equalsIgnoreCase("1")){
							// 1 = MCB Loan
							
							if(l_AccBalDate.compareTo(l_CommitmentFeeStartDate) >= 0){
								if( sys_LAY_N4 == 2){
									l_CommitmentFeeAmt += StrUtil.round3decimalsHALFDOWN(StrUtil.round4decimalsDOWN((l_UnusageBal * (commitmentfeerate/100)) / daysinyear)) *1000;						
								}else if( sys_LAY_N4 == 0){
									l_CommitmentFeeAmt += (l_UnusageBal * (commitmentfeerate/100)) / daysinyear;
								}else if( sys_LAY_N4 == 1){
									l_CommitmentFeeAmt += StrUtil.RoundHALFUP((l_UnusageBal * (commitmentfeerate/100)) / daysinyear);						
								}else if( sys_LAY_N4 == 3){
									// 3 for MCB
									l_CommitmentFeeAmt += StrUtil.round3decimalsHALFDOWN((l_UnusageBal * (commitmentfeerate/100)) / daysinyear);						
								}
							}
						}
					}else{
						break;
					}
				}
			} else {
				double[] lstAmt = new double[3];
				lstAmt = l_LoanIntDao.getLoanAccrueAmtByAccount(pAccNumber, l_MonthSDate, l_CurrentDate, conn);
				l_InterestAmt  = StrUtil.round2decimalsDOWN(lstAmt[0]);
				l_ServiceChrAmt = StrUtil.round2decimalsDOWN(lstAmt[1]);
				l_CommitmentFeeAmt = StrUtil.round2decimalsDOWN(lstAmt[2]);
			}	
			
			
			for(int i=0; i < l_Result.size(); i++){
				l_data = new LoanRepaymentData();
				l_data.setSerialno(i+1);
				l_data.setInterestrate(interestrate);
				l_data.setServicechargesrate(servicechrrate);
				l_data.setLoantype(l_Result.get(i).getLoantype());
				l_data.setLoantypedesc(l_Result.get(i).getLoantypedesc());
				l_data.setSanctiondate(l_Result.get(i).getSanctiondate());
				l_data.setExpdate(l_Result.get(i).getExpdate());
				l_data.setLoanlimit(l_Result.get(i).getLoanlimit());
				l_data.setStatus(l_Result.get(i).getStatus());
				
				if(mAccrueSetting!=2) {
					if( sys_LAY_N4 == 2){
						l_data.setInterestamount(StrUtil.round2decimalsDOWN(l_InterestAmt/1000));
					}else if( sys_LAY_N4 == 0 || sys_LAY_N4 == 1){
						if ( sys_LAY_N4 == 0 && sys_LAI_N7 == 4) {
							l_data.setInterestamount(StrUtil.round2decimalsDOWN(l_InterestAmt));
						} else {
							l_data.setInterestamount(StrUtil.RoundHALFUP(l_InterestAmt));
						}
					}else if( sys_LAY_N4 == 3){
						// 3 for MCB
						l_data.setInterestamount(StrUtil.round1decimalsHALFDOWN(l_InterestAmt));
					}
					
					if( sys_LAY_N4 == 2){
						l_data.setServicechargesamount(StrUtil.round2decimalsDOWN(l_ServiceChrAmt/1000));
					}else if( sys_LAY_N4 == 0 || sys_LAY_N4 == 1){
						l_data.setServicechargesamount(l_ServiceChrAmt);
					}else if( sys_LAY_N4 == 3){
						// 3 for MCB
						l_data.setServicechargesamount(StrUtil.round1decimalsHALFDOWN(l_ServiceChrAmt));
					}
					
					if( sys_LAY_N4 == 2){
						l_data.setCommitmentfeeamount(StrUtil.round2decimalsDOWN(l_CommitmentFeeAmt/1000));
					}else if( sys_LAY_N4 == 0 || sys_LAY_N4 == 1){
						l_data.setCommitmentfeeamount(l_CommitmentFeeAmt);
					}else if( sys_LAY_N4 == 3){
						// 3 for MCB
						l_data.setCommitmentfeeamount(StrUtil.round1decimalsHALFDOWN(l_CommitmentFeeAmt));
					}
				} else {
					if(i==0){
						l_data.setInterestamount(StrUtil.RoundHALFUP(l_InterestAmt));
						l_data.setServicechargesamount(StrUtil.RoundHALFUP(l_ServiceChrAmt));
						l_data.setCommitmentfeeamount(StrUtil.RoundHALFUP(l_CommitmentFeeAmt));
					}
				}
				
				
				//l_FinalResult.add(l_data);
			}
		}else{
			LoanRepaymentData l_Data = new LoanRepaymentData();
			//l_FinalResult.add(l_Data);
			return l_data;
		}
		return l_data;
	}
	
	public boolean saveLAODFHistoryTable( LoanAccountData p_loanaccountdata, double interestamt , Connection p_conn) {
		boolean l_result = false;

		try {
			int p_HistoryType = 0;
			if(interestamt > 0)
				p_HistoryType = LoanAccountData.HistoryType_Repayment;
			else
				p_HistoryType = LoanAccountData.HistoryType_RepaymentNoInt;
			
				Calendar cal1 = Calendar.getInstance();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = sdf1.format(cal1.getTime());
			String	l_query = "Set DateFormat YMD; Insert Into LAODFHistory(Modified_Date, History_Type, SerialNo, AccNumber, "
						+ "EntryDate, SanctionDateHO, SanctionDateBr, SanctionAmountHO, " 
						+ "SanctionAmountBr, UsageAmt, SanctionPercent, ApprovalNo, ApprovalNoHO, Status, SettleAmount, BatchNo, " 
						+ "DrPriority, LoanType, CancelDate, ExpDate, BusinessClass,SanctionNo,SanctionDate,SanctionAmt) " 
						+ "Select ?,?, SerialNo, AccNumber, EntryDate, SanctionDateHO, SanctionDateBr, "
						+ "SanctionAmountHO, SanctionAmountBr, UsageAmt, SanctionPercent, ApprovalNo, ApprovalNoHO, Status, "
						+  "?, BatchNo, DrPriority, LoanType, CancelDate, ExpDate, BusinessClass,SanctionNo,SanctionDate,SanctionAmt"
						+ " From LAODF Where AccNumber = ? And BatchNo = ? " ;
			PreparedStatement pstmt = p_conn.prepareStatement(l_query);
			int index=1;
			pstmt.setString(index++, date1);
			pstmt.setInt(index++, p_HistoryType);
			pstmt.setDouble(index++, p_loanaccountdata.getSettleAmount());
			pstmt.setString(index++, p_loanaccountdata.getAccNumber());
			pstmt.setInt(index++, p_loanaccountdata.getBatchNo());
			if (pstmt.executeUpdate() > 0)
				l_result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return l_result;
	}
	
	public boolean saveLAODFTable( LoanAccountData p_loanaccountdata, Connection p_conn) {
		boolean l_result = false;

		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(cal.getTime());
		
				Calendar cal1 = Calendar.getInstance();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = sdf1.format(cal1.getTime());
			String	l_query = "Insert Into LAODF(AccNumber, EntryDate, SanctionDateHO, SanctionDateBr, SanctionAmountHO, SanctionAmountBr, " 
					    + "SanctionPercent, ApprovalNo, ApprovalNoHO, Status, " 
						+ "SettleAmount, BatchNo, LoanType, CancelDate, "
					    + "ExpDate, DrPriority, BusinessClass,SanctionNo,SanctionDate,SanctionAmt)" 
						+ "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement	pstmt = p_conn.prepareStatement(l_query);
			int index = 1;
			pstmt.setString(index++, p_loanaccountdata.getAccNumber());
			pstmt.setString(index++, date1);
			pstmt.setString(index++, p_loanaccountdata.getSanctionDateHO() );
			pstmt.setString(index++, p_loanaccountdata.getSanctionDateBr());
			pstmt.setDouble(index++, p_loanaccountdata.getSanctionAmountHO());
			pstmt.setDouble(index++, p_loanaccountdata.getSanctionAmountBr());
			pstmt.setDouble(index++, p_loanaccountdata.getSanctionPrecent());
			pstmt.setString(index++, p_loanaccountdata.getApproveNo());
			pstmt.setString(index++, p_loanaccountdata.getApprovalNoHO());
			pstmt.setInt(index++, p_loanaccountdata.getStatus());
			pstmt.setDouble(index++, p_loanaccountdata.getSettleAmount());
			pstmt.setInt(index++, p_loanaccountdata.getBatchNo());
			pstmt.setByte(index++, p_loanaccountdata.getLoanType());
			pstmt.setString(index++, p_loanaccountdata.getCancelDate());
			pstmt.setString(index++, p_loanaccountdata.getExpDate() );
			pstmt.setInt(index++, p_loanaccountdata.getDrPriority());
			pstmt.setByte(index++, p_loanaccountdata.getBusinessClass());
			pstmt.setString(index++, p_loanaccountdata.getSanctionNo());
			pstmt.setString(index++, p_loanaccountdata.getSanctionDate());
			pstmt.setDouble(index++, p_loanaccountdata.getSanctionAmt());
			
			if (pstmt.executeUpdate() > 0)
				l_result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return l_result;
	}
	
	public boolean updateCollateralStatus_All(Connection p_conn, LoanAccountData p_loanaccountdata){
		boolean l_result = false;
		
		try {
			
			String l_query = "UPDATE CollateralInfo SET Status = 2 WHERE AccNumber = '" + p_loanaccountdata.getAccNumber() + "' and Code=2";
			PreparedStatement	pstmt = p_conn.prepareStatement(l_query);
			if(pstmt.executeUpdate() > 0)
				l_result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return l_result;
	}
	
	public boolean updateCollateralStatus(Connection p_conn, LoanAccountData p_loanaccountdata){
		boolean l_result = false;
		
		try {
			
			String l_query = "UPDATE LACollateralInfo SET Status = 2 WHERE AccNumber = '" + p_loanaccountdata.getAccNumber() + "' AND BatchNo = '" + p_loanaccountdata.getBatchNo() + "'";
			PreparedStatement pstmt = p_conn.prepareStatement(l_query);
			if(pstmt.executeUpdate() > 0)
				l_result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return l_result;
	}
	
	public boolean updateLoanStatus(String accnumber, Connection p_conn) throws SQLException {
		boolean l_result = false;
			
		String	l_query = "SET DATEFORMAT DMY;UPDATE LAODF SET Status = ? , CancelDate = ? "
					+ " WHERE AccNumber =? AND Status = ? " ;

		PreparedStatement pstmt = p_conn.prepareStatement(l_query);
		int index = 1;
		pstmt.setInt(index++, LoanAccountData.Status_Cancel);
		pstmt.setString(index++, GeneralUtility.getCurrentDate());
		pstmt.setString(index++, accnumber );
		pstmt.setInt(index++, LoanAccountData.Status_Active);
		if (pstmt.executeUpdate() > 0) {
			l_result = true;
		}					
		pstmt.close();
	
		return l_result;
	}
	
	public boolean insertLAODFRepayment(String accNumber, double amt, Connection l_conn) throws SQLException {
		boolean ret = false;
		double begBal = 0, endbal = 0;
		int termNo = 0, loanType = 0;
		PreparedStatement pstmt = null;
		String date = StrUtil.getCurrentDateyyyyMMdd();
		String sql  = "SELECT * FROM LAODFSchedule A INNER JOIN (SELECT DISTINCT" +
					" ACCNUMBER ACCNUMBER, LoanType N6 FROM LAODF) B ON A.ACCNUMBER = B.ACCNUMBER" +
					" WHERE A.AccNumber = ? AND ? BETWEEN StartDate AND DueDate AND StartDate <> DueDate";
			pstmt = l_conn.prepareStatement(sql);
			pstmt.setString(1, accNumber);
			pstmt.setString(2, date);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			termNo = rs.getInt("TermNo");
			begBal = rs.getDouble("BeginningBal");
			endbal = rs.getDouble("BeginningBal") - amt;
			loanType = rs.getInt("n6");
		}
		sql = "INSERT INTO LAODFSchedule (AccNumber, TermNo, Status, PrincipleAmount, PaymentReq," +
				" StartDate, DueDate, TransDate, SchFormular, batchNo, BeginningBal, EndingBal)" +
				" SELECT ?, ?, 1, ?, ?, ?, ?, ?, ?, 1, ?, ?";
		pstmt = l_conn.prepareStatement(sql);
		pstmt.setString(1, accNumber);
		pstmt.setInt(2, termNo);
		pstmt.setDouble(3, amt);
		pstmt.setDouble(4, amt);
		pstmt.setString(5, date);
		pstmt.setString(6, date);
		pstmt.setString(7, date);
		pstmt.setDouble(8, loanType);
		pstmt.setDouble(9, begBal);
		pstmt.setDouble(10, endbal);
		ret = pstmt.executeUpdate() > 0;
		return ret;
	}
	
	public boolean add(AccountData pLoanData, Connection pConn) throws SQLException {
		boolean ret = false;
		
		String l_Query = "INSERT INTO "+mLoanAccount+" (AccNumber) VALUES (?)";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pLoanData.getAccountNumber());
		
		if(pstmt.executeUpdate() > 0)
			ret = true;
		
		return ret;
	}
}
