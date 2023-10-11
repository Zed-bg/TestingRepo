package com.nirvasoft.rp.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.framework.Ref;
import com.nirvasoft.rp.mgr.DBAccountMgr;
import com.nirvasoft.rp.mgr.DBTransactionMgr;
import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.CallDepositAccountData;
import com.nirvasoft.rp.shared.CurrentInterestData;
import com.nirvasoft.rp.shared.CustomerData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.SignatureData;
import com.nirvasoft.rp.shared.TransferDataResult;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import java.time.LocalDate;

public class AccountDao {

	public static String mTableName = "Accounts";
	public static String mTableNameCurrent = "CurrentAccount";
	private String mLoanAccount = "LoanAccount";
	private String mLoanCollateral = "CollateralInfo";
	private String mODCollateral = "CollateralInfo";
	
	private LoanAccountData LoanAccBean;
	private ArrayList<LoanAccountData> lstLoanAccBean;
	
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
		return lstLoanAccBean;
	}

	private AccountData AccBean;
	private ArrayList<AccountData> AccLinkBean;
	ArrayList<SignatureData> lstSignData = new ArrayList<SignatureData>();
	
	public ArrayList<SignatureData> getLstSignData() {
		return lstSignData;
	}
	public void setLstSignData(ArrayList<SignatureData> lstSignData) {
		this.lstSignData = lstSignData;
	}
	
	public AccountDao() {
		LoanAccBean = new LoanAccountData();
		lstLoanAccBean = new ArrayList<LoanAccountData> ();
	}
	
	public boolean getAccount(String aAccNumber, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		boolean result = false;		
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT a.AccNumber, a.CurrencyCode, a.OpeningBalance, a.OpeningDate, a.CurrentBalance, a.ClosingDate, "
						+ " a.LastUpdate, a.Status, a.LastTransDate, a.DrawType, a.AccName, a.AccNRC, a.SAccNo, a.Remark, "
						+ "t.N1 FROM Accounts a  inner join T00005 t on a.AccNumber = t.t1 "
						+ " where a.AccNumber = ? ");

		pstmt.setString(1, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			AccBean = new AccountData();
			readRecord(AccBean, rs, "single", conn);
			AccBean.setCurrencyRate(1);			
			result = true;
		}
		pstmt.close();
		rs.close();		
		return result;
	}
	
	private void readRecord(AccountData aAccBean, ResultSet aRS, String pFrom, Connection pConn) throws SQLException {
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();

		aAccBean.setAccountNumber(aRS.getString("AccNumber"));

		aAccBean.setCurrencyCode(aRS.getString("CurrencyCode"));
		aAccBean.setOpeningBalance(StrUtil.round2decimals(aRS.getDouble("OpeningBalance")));
		aAccBean.setOpeningDate(SharedUtil.formatDBDate2MIT(aRS.getString("OpeningDate")));
		aAccBean.setCurrentBalance(StrUtil.round2decimals(aRS.getDouble("CurrentBalance")));
		aAccBean.setClosingDate(SharedUtil.formatDBDate2MIT(aRS.getString("ClosingDate")));
		aAccBean.setLastUpdate(SharedUtil.formatDBDate2MIT(aRS.getString("LastUpdate")));
		aAccBean.setStatus(aRS.getByte("Status"));
		aAccBean.setLastTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("LastTransDate")));
		aAccBean.setDrawingType(aRS.getString("DrawType"));
		aAccBean.setAccountName(aRS.getString("AccName"));
		aAccBean.setAccountID(aRS.getString("AccNRC"));
		aAccBean.setShortAccountCode(aRS.getString("SAccNo"));
		aAccBean.setRemark(aRS.getString("Remark"));
		aAccBean.setZone(aRS.getInt("N1"));
		//aAccBean.setODStatus(aRS.getInt("ODStatus"));//add od

		if (pFrom.equalsIgnoreCase("browser")) {
			aAccBean.setAccountDescription(aRS.getString("Name"));
			aAccBean.setZoneCode(aRS.getInt("ZONECODE"));
			aAccBean.setProduct(aRS.getString("PRODUCT"));
			aAccBean.setType(aRS.getString("TYPE"));
			aAccBean.setBranchCode(aRS.getString("BRANCHCODE"));
			aAccBean.setProductGL(aRS.getString("PRODUCTGL"));
			aAccBean.setCashInHandGL(aRS.getString("CASHINHANDGL"));
		} else {
			aAccBean = l_AccExtDAO.getData(aAccBean, pConn);
		}
		aAccBean.setMinimumBalance(SharedLogic.getSystemData().getpProductList().get(aAccBean.getProduct()).getMinBalance());
		//aAccBean.setMinimumBalance(SharedLogic.getAccountMinBalance(aAccBean.getProduct()));

	}
	
	public AccountData getAccountsBean() {
		return AccBean;
	}
	public void setAccountsBean(AccountData acc) {
		this.AccBean=acc;
	}
	public ArrayList<AccountData>  getAccountsLinkBean() {
		return AccLinkBean;
	}
	
	public boolean isGL(String accno, Connection conn) throws SQLException{
		boolean res = false;
		int total = 0;
		String query = "select count(AccNo) as total from GL where AccNo=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, accno);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			total = rs.getInt("total");
		}
		
		if(total > 0){
			res = true;
		}
		return res;
	}
	
	public DataResult getTransAccount(String aAccNumber,String type,double transAmount, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		DataResult result = new DataResult();
		String transDesc= "";
		String statusDesc ="";
		int status = 0;
		boolean isLoan = false;
		boolean isFD = false;
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT a.AccNumber, a.CurrencyCode, a.OpeningBalance, a.OpeningDate, a.CurrentBalance, a.ClosingDate, "
						+ " a.LastUpdate, a.Status, a.LastTransDate, a.DrawType, a.AccName, a.AccNRC, a.SAccNo, a.Remark, "
						+ " t.N1,s.Description,t.N1, t.T1, t.T2, t.T3, t.T4, t.T5, t.T6, t.T7,ISNULL(Sum(ab.BarAmount),0) AS EarMarkedAmount "
						+ " FROM Accounts a inner join T00005 t on a.AccNumber = t.t1 "
						+ " LEFT JOIN REFACCOUNTSTATUS s ON a.Status=s.StatusCode and "
						+ " s.Description<> (case when a.Status=0 and a.LastTransDate='01/01/1900' then 'Active' else 'New' end) "
						+ " left join AccBar ab on a.AccNumber=ab.AccNumber "
						+ " where a.AccNumber = ? group by  a.AccNumber, a.CurrencyCode, a.OpeningBalance, a.OpeningDate, a.CurrentBalance,"
						+ " a.ClosingDate, a.LastUpdate, a.Status, a.LastTransDate, a.DrawType, a.AccName, a.AccNRC, a.SAccNo, a.Remark,"
						+ " t.N1,s.Description,t.N1, t.T1, t.T2, t.T3, t.T4, t.T5, t.T6, t.T7 ");

		pstmt.setString(1, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			AccBean = new AccountData();
			
			statusDesc = rs.getString("Description");//status description
			status = rs.getInt("Status");
			readRecord(AccBean, rs, conn);
			AccBean.setCurrencyRate(1);		
			AccBean.setpStatusDesc(statusDesc);
			result.setStatus(true);
		}
		
		
		if(result.getStatus()) {
			isLoan = SharedLogic.isLoan(AccBean.getProduct());
			if(!isLoan)
				isFD= SharedLogic.isFD(AccBean.getProduct());
			// Check Account Status
			result.setStatus(false);
			//1 Suspend, 3 Closed, 4 Dormant, 7 Stopped Payment
			if(type.equalsIgnoreCase("Dr")) {
				transDesc = "Debit";
				if (isLoan || isFD) { // loan and fixed not allow
					result.setDescription("Not Allow "+transDesc+" Loan or Fixed Account!");
				}else if( (status==0 && (!SharedUtil.formatMIT2DateStr(AccBean.getLastTransDate()).equals("01/01/1900"))) 
						 ) { //|| (status==2 && transAmount == AccBean.getCurrentBalance())
					result.setStatus(true);
				}else { //status==1 || status==3 || status==4 || status==7 || (statusDesc.equals("New") && !SharedLogic.isLoan(AccBean.getProduct())) || (transAmount != AccBean.getCurrentBalance()) 
					result.setDescription("Invalid "+transDesc+" Account Status ( " +statusDesc +")!");
				}
			}else if(type.equalsIgnoreCase("Cr")){
				transDesc = "Credit";
				if (isLoan || isFD) { // loan and fixed not allow
					result.setDescription("Not Allow "+transDesc+" Loan or Fixed Account!");
				}else if(status==0 || status==4 || status==7) {
					result.setStatus(true);
				}else {// status==1 || status==2 || status==3
					result.setDescription("Invalid "+transDesc+" Account Status ( " +statusDesc +")!");
				}
			}
		}else {
			result.setDescription("Invalid "+transDesc+" Account Number!");
		}
		
		pstmt.close();
		rs.close();		
		return result;
	}
	
	public DataResult getTransAccountCredit(String aAccNumber, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		DataResult result = new DataResult();
		String transDesc = "";
		String statusDesc = "";
		int status = 0;
		boolean isLoan = false;
		boolean isFD = false;
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT  a.CurrentBalance,  a.LastUpdate, a.Status, a.LastTransDate, t.T2, t.T3, t.T4, t.T5, t.T6, t.T7 "
						+ " FROM Accounts a inner join T00005 t on a.AccNumber = t.t1 where a.AccNumber = ? ");

		pstmt.setString(1, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			AccBean = new AccountData();
			AccBean.setAccountNumber(aAccNumber);
			readRecordUpdate(AccBean, rs, conn);
			statusDesc = Ref.getAccountStatusDescription(AccBean);// status description
			status = rs.getInt("Status");
			AccBean.setCurrencyRate(1);
			result.setStatus(true);
		}

		if (result.getStatus()) {
			isLoan = SharedLogic.isLoan(AccBean.getProduct());
			if (!isLoan)
				isFD = SharedLogic.isFD(AccBean.getProduct());
			// Check Account Status
			result.setStatus(false);

			transDesc = "Credit";
			if (isLoan || isFD) { // loan and fixed not allow
				result.setDescription("Not Allow " + transDesc + " Loan or Fixed Account!");
			} else if (status == 0 || status == 4 || status == 7) {
				result.setStatus(true);
			} else {// status==1 || status==2 || status==3
				result.setDescription("Invalid " + transDesc + " Account Status ( " + statusDesc + ")!");
			}

		} else {
			result.setDescription("Invalid " + transDesc + " Account Number!");
		}

		pstmt.close();
		rs.close();
		return result;
	}
	
	public DataResult getTransAccountDebit(String aAccNumber, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		DataResult result = new DataResult();
		String transDesc = "";
		String statusDesc = "";
		int status = 0;
		boolean isLoan = false;
		boolean isFD = false;
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT  a.CurrentBalance,  a.LastUpdate, a.Status, a.LastTransDate, t.T2, t.T3, t.T4, t.T5, t.T6, t.T7 "
						+ " FROM Accounts a inner join T00005 t on a.AccNumber = t.t1 where a.AccNumber = ? ");

		pstmt.setString(1, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			AccBean = new AccountData();
			AccBean.setAccountNumber(aAccNumber);
			readRecordUpdate(AccBean, rs, conn);
			statusDesc = Ref.getAccountStatusDescription(AccBean);// status description
			status = rs.getInt("Status");
			AccBean.setCurrencyRate(1);
			result.setStatus(true);
		}

		if (result.getStatus()) {
			pstmt = conn.prepareStatement(
					"SELECT COALESCE(SUM(BarAmount), 0) AS EarMarkedAmount FROM AccBar where AccNumber = ?");

			pstmt.setString(1, aAccNumber);
			pstmt.setQueryTimeout(DAOManager.getNormalTime());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				AccBean.setAvailableBalance(rs.getDouble("EarMarkedAmount"));
			}

			isLoan = SharedLogic.isLoan(AccBean.getProduct());
			if (!isLoan)
				isFD = SharedLogic.isFD(AccBean.getProduct());
			// Check Account Status
			result.setStatus(false);
			// 1 Suspend, 3 Closed, 4 Dormant, 7 Stopped Payment
			transDesc = "Debit";
			if (isLoan || isFD) { // loan and fixed not allow
				result.setDescription("Not Allow " + transDesc + " Loan or Fixed Account!");
			} else if ((status == 0
					&& (!SharedUtil.formatMIT2DateStr(AccBean.getLastTransDate()).equals("01/01/1900")))) { // ||
																											// (status==2
																											// &&
																											// transAmount
																											// ==
																											// AccBean.getCurrentBalance())
				result.setStatus(true);
			} else { // status==1 || status==3 || status==4 || status==7 || (statusDesc.equals("New")
						// && !SharedLogic.isLoan(AccBean.getProduct())) || (transAmount !=
						// AccBean.getCurrentBalance())
				result.setDescription("Invalid " + transDesc + " Account Status ( " + statusDesc + ")!");
			}

		} else {
			result.setDescription("Invalid " + transDesc + " Account Number!");
		}

		pstmt.close();
		rs.close();
		return result;
	}

	
	public double getAvaliableBalance(AccountData l_AccData, double lMinAmount, int pProcessType, Connection conn) throws  IOException,ClassNotFoundException,SQLException, ParserConfigurationException, SAXException{

		double lCurrentBal=0;
		int lAccStatus = 0 ;
		double lEarMarkedAmountBal=0;
		double lRetAmount=0;
		
		int l_BarMinBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN3();
		
		lCurrentBal = l_AccData.getCurrentBalance();
		lAccStatus = l_AccData.getStatus() ;
		lEarMarkedAmountBal = l_AccData.getAvailableBalance();
		
		if(pProcessType==4) lRetAmount= 0;

		if(lEarMarkedAmountBal>0) {
			if(l_BarMinBalSetting==1)
				lRetAmount=lCurrentBal-lEarMarkedAmountBal-lMinAmount;
			else
				lRetAmount=lCurrentBal-lEarMarkedAmountBal;
		} else {
			if(lAccStatus == 2)
				lRetAmount=lCurrentBal ;
			else
				lRetAmount=lCurrentBal-lMinAmount;
		}		
		return lRetAmount;
	}
	
	public DataResult updateAccountBalanceCurrency(String tmp_Transpreapre,String l_TableName,String l_TableName1, Connection conn ) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		DataResult ret= new DataResult();
		String l_Query ="";
		PreparedStatement pstmt=null;		
		//Making Lock Accounts
			
		//l_Query ="UPDATE A  SET CurrentBalance = CurrentBalance FROM "+l_TableName+" A INNER JOIN "+tmp_Transpreapre+" B " + " ON A.AccNumber= B.account ";
		l_Query ="UPDATE A  SET CurrentBalance = CurrentBalance FROM "+l_TableName+" A WHERE A.AccNumber IN (SELECT distinct account FROM "+tmp_Transpreapre+")";
		pstmt = conn.prepareStatement(l_Query);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		pstmt.executeUpdate();
		pstmt.close();
		
		
		if(!l_TableName1.equals("")){//Making Lock Accounts_Balances			
			//l_Query = "UPDATE A  SET CurrentBalance = CurrentBalance FROM "+l_TableName1+" A INNER JOIN "+tmp_Transpreapre+" B " + " ON A.AccNumber= B.account";
			l_Query ="UPDATE A  SET CurrentBalance = CurrentBalance FROM "+l_TableName1+" A WHERE A.AccNumber IN (SELECT distinct account FROM "+tmp_Transpreapre+")";
			pstmt = conn.prepareStatement(l_Query);
			pstmt.executeUpdate();
			pstmt.close();
		}
		// Debit (transtype >= 500) , Credit (transtype < 500)
		l_Query = "UPDATE A SET " 
				+ " CurrentBalance = CASE WHEN  B.transtype >= 500 THEN CONVERT(numeric(18,2),(A.CurrentBalance - B.amount )) "
				+ " ELSE CONVERT(numeric(18,2),(A.CurrentBalance + B.amount )) END, "
				+ " A.LastTransDate=B.transdate, A.LastUpdate=B.transdate " 
				+ " FROM "+l_TableName+" A INNER JOIN (select account,transtype,SUM(amount) as amount,transdate from "
				+ tmp_Transpreapre+" GROUP BY account,transtype,transdate) B ON A.AccNumber=B.account ";	
		pstmt = conn.prepareStatement(l_Query);			
		if(pstmt.executeUpdate()>0) { ret.setStatus(true); }
		pstmt.close();
		
		if(ret.getStatus()){
			if(!l_TableName1.equals("")) {
				ret.setStatus(false);
				l_Query = "UPDATE A SET " 
						+ " CurrentBalance = CASE WHEN  B.transtype >= 500 THEN CONVERT(numeric(18,2),(A.CurrentBalance - B.amount )) "
						+ " ELSE CONVERT(numeric(18,2),(A.CurrentBalance + B.amount )) END, "
						+ " A.LastTransDate=B.transdate, A.LastUpdate=B.transdate " 
						+ " FROM "+l_TableName1+" A INNER JOIN (select account,transtype,SUM(amount) as amount,transdate from "
						+ tmp_Transpreapre+" GROUP BY account,transtype,transdate) B ON A.AccNumber=B.account ";	
				pstmt = conn.prepareStatement(l_Query);			
				if(pstmt.executeUpdate()>0) { ret.setStatus(true); }
				pstmt.close();
			}
		}
		return ret;
	}
	
	public TransferDataResult updateAccountBalanceMulti(TransferDataResult transferDataResult,
			String l_TableName, String l_TableName1, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {

		String l_Query = "";
		PreparedStatement pstmt = null;
		Double availableBalance = 0.0;
		AccountData accdata = new AccountData();
		TransferData fromTransferData = new TransferData();
		TransferData toTransferData = new TransferData();
		int fromtrans = 0 , totrans = 0,accountcount = 0;
		
		for(int j = 0; j < transferDataResult.getTransferlist().size(); j++) {
		    if(!transferDataResult.getTransferlist().get(j).getIsGL())
			{
			  if(transferDataResult.getTransferlist().get(j).getIbtTransType() >= 500){
			  fromTransferData = transferDataResult.getTransferlist().get(j);
			  fromtrans = j;
			  }else{
			  toTransferData = transferDataResult.getTransferlist().get(j);
			  totrans = j ;
			  }
			  accountcount++;
			}
		}
		accdata.setAvailableBalance(fromTransferData.getAccountBarAmount());
		
		l_Query = "SELECT AccNumber,CurrentBalance,status FROM " + l_TableName
				+ " WITH (ROWLOCK) WHERE AccNumber   ";
		if(accountcount == 1){
			l_Query +=" = ?";
		}else{
		l_Query +=" in (?,?)";
		}

		pstmt = conn.prepareStatement(l_Query);
		if(accountcount == 1){
			if(!fromTransferData.getFromAccNumber().equals("")) {
				pstmt.setString(1, fromTransferData.getFromAccNumber());
			}else {
				pstmt.setString(1, toTransferData.getFromAccNumber());
			}
		} else {
			pstmt.setString(1, fromTransferData.getFromAccNumber());
			pstmt.setString(2, toTransferData.getFromAccNumber());
		}
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (fromTransferData.getFromAccNumber().equals(rs.getString("AccNumber"))) {
				accdata.setCurrentBalance(rs.getDouble("currentbalance"));
				accdata.setStatus(rs.getInt("status"));
				transferDataResult.setStatus(true);
				transferDataResult.getTransferlist().get(fromtrans).setPrevBalance(rs.getDouble("CurrentBalance"));

				availableBalance = getAvaliableBalance(accdata, fromTransferData.getProductMinOpeningBalance(), 0,
						conn);
				// Check For A/C Status Closed Pending
				if (accdata.getStatus() != 2) {
					if (fromTransferData.getAmount() > availableBalance) {
						transferDataResult.setStatus(false);
						transferDataResult.setDescription(fromTransferData.getFromAccNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((fromTransferData.getAmount() - availableBalance)));
						break;
					}
				} else {
					if (fromTransferData.getAmount() != accdata.getCurrentBalance()) {
						transferDataResult.setStatus(false);
						transferDataResult.setDescription(fromTransferData.getFromAccNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma(
										(fromTransferData.getAmount() - accdata.getCurrentBalance())));
						break;
					}
				}

			} else {
				transferDataResult.setStatus(true);
				transferDataResult.getTransferlist().get(totrans).setPrevBalance(rs.getDouble("CurrentBalance"));
				
			}

		}
		pstmt.close();

		if (transferDataResult.isStatus()) {
			l_Query = "UPDATE " + l_TableName + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
					+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
			pstmt = conn.prepareStatement(l_Query);
			pstmt.setDouble(1, fromTransferData.getAmount());
			pstmt.setString(2, fromTransferData.getTransactionDate());
			pstmt.setString(3, fromTransferData.getTransactionDate());
			pstmt.setString(4, fromTransferData.getFromAccNumber());
			if (pstmt.executeUpdate() > 0) {
				transferDataResult.setStatus(true);
			}
			pstmt.close();

			if (transferDataResult.isStatus()) {
				if (!l_TableName1.equals("")) {
					transferDataResult.setStatus(false);
					l_Query = "UPDATE " + l_TableName1 + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
							+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
					pstmt = conn.prepareStatement(l_Query);
					pstmt.setDouble(1, fromTransferData.getAmount());
					pstmt.setString(2, fromTransferData.getTransactionDate());
					pstmt.setString(3, fromTransferData.getTransactionDate());
					pstmt.setString(4, fromTransferData.getFromAccNumber());
					if (pstmt.executeUpdate() > 0) {
						transferDataResult.setStatus(true);
					}
					pstmt.close();
				}
			}

			if (!toTransferData.getFromAccNumber().equals("")) {
				l_Query = "UPDATE " + l_TableName
						+ " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
						+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
				pstmt = conn.prepareStatement(l_Query);
				pstmt.setDouble(1, toTransferData.getAmount());
				pstmt.setString(2, toTransferData.getTransactionDate());
				pstmt.setString(3, toTransferData.getTransactionDate());
				pstmt.setString(4, toTransferData.getFromAccNumber());
				if (pstmt.executeUpdate() > 0) {
					transferDataResult.setStatus(true);
				}
				pstmt.close();

				if (transferDataResult.isStatus()) {
					if (!l_TableName1.equals("")) {
						transferDataResult.setStatus(false);
						l_Query = "UPDATE " + l_TableName1
								+ " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
								+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
						pstmt = conn.prepareStatement(l_Query);
						pstmt.setDouble(1, toTransferData.getAmount());
						pstmt.setString(2, toTransferData.getTransactionDate());
						pstmt.setString(3, toTransferData.getTransactionDate());
						pstmt.setString(4, toTransferData.getFromAccNumber());
						if (pstmt.executeUpdate() > 0) {
							transferDataResult.setStatus(true);
						}
						pstmt.close();
					}
				}
			}
		}
		return transferDataResult;
	}
	
	public TransferDataResult updateAccountBalanceMultiUpdate(TransferDataResult transferDataResult,
			String l_TableName, String l_TableName1, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {

		String l_Query = "";
		PreparedStatement pstmt = null;
		Double availableBalance = 0.0;
		AccountData accdata = new AccountData();
		TransferData fromTransferData = new TransferData();
		TransferData toTransferData = new TransferData();

		for (int j = 0; j < transferDataResult.getTransferlist().size(); j++) {
			fromTransferData = new TransferData();
			toTransferData = new TransferData();
			if (!transferDataResult.getTransferlist().get(j).getIsGL()) {
				if (transferDataResult.getTransferlist().get(j).getIbtTransType() >= 500) {
					fromTransferData = transferDataResult.getTransferlist().get(j);					
				} else {
					toTransferData = transferDataResult.getTransferlist().get(j);
				}

				accdata.setAvailableBalance(fromTransferData.getAccountBarAmount());

				l_Query = "SELECT AccNumber,CurrentBalance,status FROM " + l_TableName
						+ " WITH (ROWLOCK) WHERE AccNumber = ? ";

				pstmt = conn.prepareStatement(l_Query);
				
					if (!fromTransferData.getFromAccNumber().equals("")) {
						pstmt.setString(1, fromTransferData.getFromAccNumber());
					} else {
						pstmt.setString(1, toTransferData.getFromAccNumber());
					}
				
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					if (fromTransferData.getFromAccNumber().equals(rs.getString("AccNumber"))) {
						accdata.setCurrentBalance(rs.getDouble("currentbalance"));
						accdata.setStatus(rs.getInt("status"));
						transferDataResult.setStatus(true);
						transferDataResult.getTransferlist().get(j)
								.setPrevBalance(rs.getDouble("CurrentBalance"));

						availableBalance = getAvaliableBalance(accdata, fromTransferData.getProductMinOpeningBalance(),
								0, conn);
						// Check For A/C Status Closed Pending
						if (accdata.getStatus() != 2) {
							if (fromTransferData.getAmount() > availableBalance) {
								transferDataResult.setStatus(false);
								transferDataResult.setDescription(fromTransferData.getFromAccNumber()
										+ " insufficient balance " + StrUtil.formatNumberwithComma(
												(fromTransferData.getAmount() - availableBalance)));
								break;
							}
						} else {
							if (fromTransferData.getAmount() != accdata.getCurrentBalance()) {
								transferDataResult.setStatus(false);
								transferDataResult.setDescription(fromTransferData.getFromAccNumber()
										+ " insufficient balance " + StrUtil.formatNumberwithComma(
												(fromTransferData.getAmount() - accdata.getCurrentBalance())));
								break;
							}
						}

					} else {
						transferDataResult.setStatus(true);
						transferDataResult.getTransferlist().get(j)
								.setPrevBalance(rs.getDouble("CurrentBalance"));

					}

				}
				pstmt.close();

				if (transferDataResult.isStatus()) {
					l_Query = "UPDATE " + l_TableName
							+ " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
							+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
					pstmt = conn.prepareStatement(l_Query);
					pstmt.setDouble(1, fromTransferData.getAmount());
					pstmt.setString(2, fromTransferData.getTransactionDate());
					pstmt.setString(3, fromTransferData.getTransactionDate());
					pstmt.setString(4, fromTransferData.getFromAccNumber());
					if (pstmt.executeUpdate() > 0) {
						transferDataResult.setStatus(true);
					}
					pstmt.close();

					if (transferDataResult.isStatus()) {
						if (!l_TableName1.equals("")) {
							transferDataResult.setStatus(false);
							l_Query = "UPDATE " + l_TableName1 + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
									+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
							pstmt = conn.prepareStatement(l_Query);
							pstmt.setDouble(1, fromTransferData.getAmount());
							pstmt.setString(2, fromTransferData.getTransactionDate());
							pstmt.setString(3, fromTransferData.getTransactionDate());
							pstmt.setString(4, fromTransferData.getFromAccNumber());
							if (pstmt.executeUpdate() > 0) {
								transferDataResult.setStatus(true);
							}
							pstmt.close();
						}
					}

					l_Query = "UPDATE " + l_TableName
							+ " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
							+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
					pstmt = conn.prepareStatement(l_Query);
					pstmt.setDouble(1, toTransferData.getAmount());
					pstmt.setString(2, toTransferData.getTransactionDate());
					pstmt.setString(3, toTransferData.getTransactionDate());
					pstmt.setString(4, toTransferData.getFromAccNumber());
					if (pstmt.executeUpdate() > 0) {
						transferDataResult.setStatus(true);
					}
					pstmt.close();

					if (transferDataResult.isStatus()) {
						if (!l_TableName1.equals("")) {
							transferDataResult.setStatus(false);
							l_Query = "UPDATE " + l_TableName1 + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
									+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
							pstmt = conn.prepareStatement(l_Query);
							pstmt.setDouble(1, toTransferData.getAmount());
							pstmt.setString(2, toTransferData.getTransactionDate());
							pstmt.setString(3, toTransferData.getTransactionDate());
							pstmt.setString(4, toTransferData.getFromAccNumber());
							if (pstmt.executeUpdate() > 0) {
								transferDataResult.setStatus(true);
							}
							pstmt.close();
						}
					}
				}
			}
		}
		return transferDataResult;
	}
	
	public TransferDataResult updateAccountBalanceCurrencyUpdate(TransferDataResult transferDataResult,String l_TableName,String l_TableName1, Connection conn ) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{

		String l_Query = "";
		PreparedStatement pstmt = null;
		Double availableBalance = 0.0;
		AccountData accdata = new AccountData();
		TransferData fromTransferData = new TransferData();
		TransferData toTransferData = new TransferData();
		if(transferDataResult.getTransferlist().size()<3) {
			fromTransferData = transferDataResult.getTransferlist().get(0);
			toTransferData = transferDataResult.getTransferlist().get(1);
		}else {
		fromTransferData = transferDataResult.getTransferlist().get(0);
		toTransferData = transferDataResult.getTransferlist().get(3);
		}
		accdata.setAvailableBalance(fromTransferData.getAccountBarAmount());
		l_Query = "SELECT AccNumber,CurrentBalance,status FROM " + l_TableName
				+ " WITH (ROWLOCK) WHERE AccNumber in (?,?) ";

		pstmt = conn.prepareStatement(l_Query);
		pstmt.setString(1, fromTransferData.getFromAccNumber());
		pstmt.setString(2, toTransferData.getFromAccNumber());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (fromTransferData.getFromAccNumber().equals(rs.getString("AccNumber"))) {
				accdata.setCurrentBalance(rs.getDouble("currentbalance"));
				accdata.setStatus(rs.getInt("status"));
				transferDataResult.setStatus(true);
				transferDataResult.getTransferlist().get(0).setPrevBalance(rs.getDouble("CurrentBalance"));

				availableBalance = getAvaliableBalance(accdata, fromTransferData.getProductMinOpeningBalance(), 0,
						conn);
				// Check For A/C Status Closed Pending
				if (accdata.getStatus() != 2) {
					if (fromTransferData.getAmount() > availableBalance) {
						transferDataResult.setStatus(false);
						transferDataResult.setDescription(fromTransferData.getFromAccNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((fromTransferData.getAmount() - availableBalance)));
						break;
					}
				} else {
					if (fromTransferData.getAmount() != accdata.getCurrentBalance()) {
						transferDataResult.setStatus(false);
						transferDataResult.setDescription(fromTransferData.getFromAccNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma(
										(fromTransferData.getAmount() - accdata.getCurrentBalance())));
						break;
					}
				}

			} else {
				transferDataResult.setStatus(true);
				if(transferDataResult.getTransferlist().size()<3) {
					transferDataResult.getTransferlist().get(1).setPrevBalance(rs.getDouble("CurrentBalance"));
				}else {
					transferDataResult.getTransferlist().get(3).setPrevBalance(rs.getDouble("CurrentBalance"));
				}
				
			}

		}
		pstmt.close();

		if (transferDataResult.isStatus()) {
			l_Query = "UPDATE " + l_TableName + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
					+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
			pstmt = conn.prepareStatement(l_Query);
			pstmt.setDouble(1, fromTransferData.getAmount());
			pstmt.setString(2, fromTransferData.getTransactionDate());
			pstmt.setString(3, fromTransferData.getTransactionDate());
			pstmt.setString(4, fromTransferData.getFromAccNumber());
			if (pstmt.executeUpdate() > 0) {
				transferDataResult.setStatus(true);
			}
			pstmt.close();

			if (transferDataResult.isStatus()) {
				if (!l_TableName1.equals("")) {
					transferDataResult.setStatus(false);
					l_Query = "UPDATE " + l_TableName1 + " SET CONVERT(numeric(18,2),(CurrentBalance - ? )), "
							+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
					pstmt = conn.prepareStatement(l_Query);
					pstmt.setDouble(1, fromTransferData.getAmount());
					pstmt.setString(2, fromTransferData.getTransactionDate());
					pstmt.setString(3, fromTransferData.getTransactionDate());
					pstmt.setString(4, fromTransferData.getFromAccNumber());
					if (pstmt.executeUpdate() > 0) {
						transferDataResult.setStatus(true);
					}
					pstmt.close();
				}
			}

			l_Query = "UPDATE " + l_TableName + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
					+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
			pstmt = conn.prepareStatement(l_Query);
			pstmt.setDouble(1, toTransferData.getAmount());
			pstmt.setString(2, toTransferData.getTransactionDate());
			pstmt.setString(3, toTransferData.getTransactionDate());
			pstmt.setString(4, toTransferData.getFromAccNumber());
			if (pstmt.executeUpdate() > 0) {
				transferDataResult.setStatus(true);
			}
			pstmt.close();

			if (transferDataResult.isStatus()) {
				if (!l_TableName1.equals("")) {
					transferDataResult.setStatus(false);
					l_Query = "UPDATE " + l_TableName1 + " SET CONVERT(numeric(18,2),(CurrentBalance + ? )), "
							+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
					pstmt = conn.prepareStatement(l_Query);
					pstmt.setDouble(1, toTransferData.getAmount());
					pstmt.setString(2, toTransferData.getTransactionDate());
					pstmt.setString(3, toTransferData.getTransactionDate());
					pstmt.setString(4, toTransferData.getFromAccNumber());
					if (pstmt.executeUpdate() > 0) {
						transferDataResult.setStatus(true);
					}
					pstmt.close();
				}
			}
		}
		return transferDataResult;
	}
	
	private void readRecord(AccountData aAccBean, ResultSet aRS, Connection pConn) throws SQLException {

		aAccBean.setAccountNumber(aRS.getString("AccNumber"));

		aAccBean.setCurrencyCode(aRS.getString("CurrencyCode"));
		aAccBean.setOpeningBalance(StrUtil.round2decimals(aRS.getDouble("OpeningBalance")));
		aAccBean.setOpeningDate(SharedUtil.formatDBDate2MIT(aRS.getString("OpeningDate")));
		aAccBean.setCurrentBalance(StrUtil.round2decimals(aRS.getDouble("CurrentBalance")));
		aAccBean.setClosingDate(SharedUtil.formatDBDate2MIT(aRS.getString("ClosingDate")));
		aAccBean.setLastUpdate(SharedUtil.formatDBDate2MIT(aRS.getString("LastUpdate")));
		aAccBean.setStatus(aRS.getByte("Status"));
		aAccBean.setLastTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("LastTransDate")));
		aAccBean.setDrawingType(aRS.getString("DrawType"));
		aAccBean.setAccountName(aRS.getString("AccName"));
		aAccBean.setAccountID(aRS.getString("AccNRC"));
		aAccBean.setShortAccountCode(aRS.getString("SAccNo"));
		aAccBean.setRemark(aRS.getString("Remark"));
		aAccBean.setZone(aRS.getInt("N1"));
		aAccBean.setAvailableBalance(aRS.getDouble("EarMarkedAmount"));
		//aAccBean = l_AccExtDAO.getData(aAccBean, pConn);
		aAccBean.setZoneCode(aRS.getInt("N1"));
		aAccBean.setProduct(aRS.getString("T2"));
		aAccBean.setType(aRS.getString("T3"));
		aAccBean.setCurrencyCode(aRS.getString("T4"));
		aAccBean.setBranchCode(aRS.getString("T5"));
		aAccBean.setProductGL(aRS.getString("T6"));
		aAccBean.setCashInHandGL(aRS.getString("T7"));
		
		aAccBean.setMinimumBalance(SharedLogic.getSystemData().getpProductList().get(aAccBean.getProduct()).getMinBalance());

	}
	
	public boolean getAutoLinkAccount(String aAccNumber, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		boolean result = false;		
		AccLinkBean = new ArrayList<AccountData>();
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT a.AccNumber, a.CurrencyCode, a.OpeningBalance, a.OpeningDate, a.CurrentBalance, a.ClosingDate, "
						+ " a.LastUpdate, a.Status, a.LastTransDate, a.DrawType, a.AccName, a.AccNRC, a.SAccNo, a.Remark, "
						+ "al.ParentAccount,al.ChildAccount,al.Priority,al.LinkDate, t.N1, t.T1, t.T2, t.T3, t.T4, t.T5, t.T6, t.T7  FROM Accounts a   "
						+ " inner join AccountLinks al ON a.AccNumber=al.ChildAccount inner join T00005 t on a.AccNumber=t.T1  WHERE al.ParentAccount=? ");

		pstmt.setString(1, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			AccBean = new AccountData();
			readLARecord(AccBean, rs, "single", conn);
			AccBean.setCurrencyRate(1);		
			AccLinkBean.add(AccBean);
			result = true;
		}
		pstmt.close();
		rs.close();		
		return result;
	}
	
	public DataResult getAutoLinkAccount(String aAccNumber,String tmp_linkaccpreapre,TransferData pTransactionData,double l_AmountNeeded,double pCurRate,String pCurCode,boolean hasCheque, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		String pByForceCheque = "",l_Query="";
	    pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
	    l_Query="SELECT ROW_NUMBER() OVER(order by a.AccNumber) as Rowno,a.AccNumber, a.CurrencyCode, a.OpeningBalance, a.OpeningDate, a.CurrentBalance, a.ClosingDate, "
				+ " a.LastUpdate, a.Status, a.LastTransDate, a.DrawType, a.AccName, a.AccNRC, a.SAccNo, "
				+ " al.ParentAccount,al.ChildAccount,al.Priority,al.LinkDate, t.N1 ZoneCode , t.T1, t.T2 Product, t.T3 Type, "
				+ " t.T5 BranchCode, t.T6 ProductGL, t.T7 CashInHandGL ,ISNULL(Sum(ab.BarAmount),0) AS EarMarkedAmount, CAST(0.0 as decimal(18,2)) as Available, CAST(0.0 as decimal(18,2)) as CommAmt,"
				+ " ? as AmountNeeded,? as CurRate,? as CurCode,? as UserID,? as AuthorizerID, CAST( ? as datetime) as TransactionTime,"
				+ " CAST( ? as datetime) as TransactionDate,CAST( ? as datetime) as EffectiveDate,? as ReferenceNo,? as FromBranchCode,? as FromAccNumber,? as Remark,? as ByForceCheque "
				+ " INTO "+tmp_linkaccpreapre+" FROM Accounts a inner join AccountLinks al ON a.AccNumber=al.ChildAccount "
				+ " inner join T00005 t on a.AccNumber=t.T1 left join AccBar ab on a.AccNumber=ab.AccNumber WHERE al.ParentAccount=? "
				+ " GROUP BY a.AccNumber, a.CurrencyCode, a.OpeningBalance, a.OpeningDate, a.CurrentBalance, a.ClosingDate,"
				+ "	a.LastUpdate, a.Status, a.LastTransDate, a.DrawType, a.AccName, a.AccNRC, a.SAccNo, a.Remark, "
				+ "	al.ParentAccount,al.ChildAccount,al.Priority,al.LinkDate, t.N1, t.T1, t.T2, t.T3, t.T4, t.T5,t.T6,t.T7 ";
		DataResult l_DataResult = new DataResult();
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		int index = 1;
		pstmt.setDouble(index++, l_AmountNeeded);
		pstmt.setDouble(index++, pCurRate);
		pstmt.setString(index++, pCurCode);
		pstmt.setString(index++, pTransactionData.getUserID());
		pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
		pstmt.setString(index++, pTransactionData.getTransactionTime());
		pstmt.setString(index++, pTransactionData.getTransactionDate());
		pstmt.setString(index++, pTransactionData.getEffectiveDate());
		pstmt.setString(index++, pTransactionData.getReferenceNo());	
		pstmt.setString(index++, pTransactionData.getFromBranchCode());
		pstmt.setString(index++, aAccNumber);
		pstmt.setString(index++, pTransactionData.getRemark());
		pstmt.setString(index++, hasCheque ? pByForceCheque :"");
		pstmt.setString(index++, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		int count = pstmt.executeUpdate();
		if (count>0) {
			l_DataResult.setStatus(true);
			l_DataResult.setCount(count);
		}
			
		
		pstmt.close();
		return l_DataResult;
	}
	
	private void readLARecord(AccountData aAccBean, ResultSet aRS, String pFrom, Connection pConn) throws SQLException {
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();

		aAccBean.setAccountNumber(aRS.getString("AccNumber"));

		aAccBean.setCurrencyCode(aRS.getString("CurrencyCode"));
		aAccBean.setOpeningBalance(StrUtil.round2decimals(aRS.getDouble("OpeningBalance")));
		aAccBean.setOpeningDate(SharedUtil.formatDBDate2MIT(aRS.getString("OpeningDate")));
		aAccBean.setCurrentBalance(StrUtil.round2decimals(aRS.getDouble("CurrentBalance")));
		aAccBean.setClosingDate(SharedUtil.formatDBDate2MIT(aRS.getString("ClosingDate")));
		aAccBean.setLastUpdate(SharedUtil.formatDBDate2MIT(aRS.getString("LastUpdate")));
		aAccBean.setStatus(aRS.getByte("Status"));
		aAccBean.setLastTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("LastTransDate")));
		aAccBean.setDrawingType(aRS.getString("DrawType"));
		aAccBean.setAccountName(aRS.getString("AccName"));
		aAccBean.setAccountID(aRS.getString("AccNRC"));
		aAccBean.setShortAccountCode(aRS.getString("SAccNo"));
		aAccBean.setRemark(aRS.getString("Remark"));
		//aAccBean.setZone(aRS.getInt("N1"));
		//aAccBean.setODStatus(aRS.getInt("ODStatus"));//add od

		if (pFrom.equalsIgnoreCase("browser")) {
			aAccBean.setAccountDescription(aRS.getString("Name"));
			aAccBean.setZoneCode(aRS.getInt("ZONECODE"));
			aAccBean.setProduct(aRS.getString("PRODUCT"));
			aAccBean.setType(aRS.getString("TYPE"));
			aAccBean.setBranchCode(aRS.getString("BRANCHCODE"));
			aAccBean.setProductGL(aRS.getString("PRODUCTGL"));
			aAccBean.setCashInHandGL(aRS.getString("CASHINHANDGL"));
		} else {
			aAccBean.setZoneCode(aRS.getInt("N1"));
			aAccBean.setProduct(aRS.getString("T2"));
			aAccBean.setType(aRS.getString("T3"));
			aAccBean.setCurrencyCode(aRS.getString("T4"));
			aAccBean.setBranchCode(aRS.getString("T5"));
			aAccBean.setProductGL(aRS.getString("T6"));
			aAccBean.setCashInHandGL(aRS.getString("T7"));
		}
		aAccBean.setMinimumBalance(SharedLogic.getSystemData().getpProductList().get(aAccBean.getProduct()).getMinBalance());
		//aAccBean.setMinimumBalance(SharedLogic.getAccountMinBalance(aAccBean.getProduct()));

	}
	
	//for Loan Expire case by wlt
		public AccountData getLoanExpireData(String l_TodayDate,String accountNumber, Connection conn) throws SQLException {
			AccountData data =new AccountData();
			String sql = "SELECT A.*,C.N11 FROM  LAODF A INNER JOIN T00005 B ON B.T1=A.AccNUmber INNER JOIN T00033 C ON B.T2=C.T1 " +
					"AND A.LoanType=C.N1 Where AccNumber =?  And BatchNo <> '0' And Status = 1 and ExpDate < ? ";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, accountNumber);
			pstmt.setString(2, l_TodayDate);
		     
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				data.setExpire(true);
				if(rs.getInt("N11")==1)
					data.setSchedule(true);
			}
			pstmt.close();
			return data;
		}
		
		public boolean getCLHeader(String aAccNumber, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			boolean result=false;

			PreparedStatement pstmt=conn.prepareStatement("SELECT AccNumber, LastTransNo, LastLineNo, LastPage FROM CLHeader WHERE AccNumber=?");

			pstmt.setString(1, aAccNumber);
			pstmt.setQueryTimeout(DAOManager.getNormalTime());   
			ResultSet rs=pstmt.executeQuery();

			while(rs.next()){
				ReadCLHeaderData(AccBean, rs);
				result=true;
			}
			pstmt.close();

			return result;
		}
		
		private void ReadCLHeaderData(AccountData aCLHeaderBean,ResultSet aRS) throws SQLException
		{		
			aCLHeaderBean.setAccountNumber(aRS.getString("AccNumber"));
			aCLHeaderBean.setLastTransNo(aRS.getInt("LastTransNo"));
			aCLHeaderBean.setLastLineNo(aRS.getInt("LastLineNo"));
			aCLHeaderBean.setLastPage(aRS.getInt("LastPage"));		
		}
		
		public AccountData getODAvailableBalance(String pDate, String pAccNumber, int pProcessType, Connection pConn) {
			AccountData l_AccData = new AccountData();
			try {
				String l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
						+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
						+ "inner join currentaccount b on a.accnumber = b.accnumber "
						+ "inner join accounts c on  a.accnumber = c.accnumber "
						+ "where a.status in ('1') ";
				if(!(pProcessType==1  || pProcessType==4  || pProcessType== 8))
					l_Query += 	"and a.expdate >= ? ";
				l_Query +=	"and a.batchno <> '0' "
						+ "and a.odtype <> 0 "
						+ "and a.odtype <> 100 "
						+ "and a.accnumber = ? "
						+ "group by todexpdate, todlimit, currentbalance ";

				PreparedStatement pstmt = pConn.prepareStatement(l_Query);
				int i =1;
				pstmt.setDate(i++,java.sql.Date.valueOf(GeneralUtility.changeDateFormatyyyyMMdd(pDate)) );
				if(!(pProcessType==1  || pProcessType==4  || pProcessType== 8))
					pstmt.setDate(i++,java.sql.Date.valueOf(GeneralUtility.changeDateFormatyyyyMMdd(pDate)) );
				pstmt.setString(i++, pAccNumber);

				ResultSet l_RS = pstmt.executeQuery();

				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
					l_AccData.setOdLimit(l_RS.getDouble("ODLimit"));
					l_AccData.setTODLimit(l_RS.getDouble("TODLimit"));
				}

				if(pProcessType == 4 || pProcessType == 8){
					l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
							+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
							+ "inner join currentaccount b on a.accnumber = b.accnumber "
							+ "inner join accounts c on  a.accnumber = c.accnumber "
							+ "where a.status in ('1') "
							+ "and (a.batchno = '0' or a.odtype in (0,100)) "
							+ "and a.accnumber = ? "
							+ "group by todexpdate, todlimit, currentbalance ";

					pstmt = pConn.prepareStatement(l_Query);
					pstmt.setDate(1, java.sql.Date.valueOf(GeneralUtility.changeDateFormatyyyyMMdd(pDate)));
					pstmt.setString(2, pAccNumber);

					l_RS = pstmt.executeQuery();

					while (l_RS.next()) {				
						l_AccData.setOdLimit(l_AccData.getOdLimit() + l_RS.getDouble("ODLimit"));
						l_AccData.setTODLimit(l_AccData.getTODLimit() + l_RS.getDouble("TODLimit"));				
					}
				}

				l_Query = "select currentbalance from accounts where accnumber = ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pAccNumber);
				l_RS = pstmt.executeQuery();			
				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
				}
				if(pProcessType != 4 || pProcessType != 8){
					l_Query = "Select ExpDate from AODF where BatchNo <> '0' and BatchNo = (select Max(BatchNo) from AODF where AccNumber = ? and odtype<>0 and odtype<>100) and AccNumber = ? And Status = 1 and ExpDate < ? ";
					pstmt = pConn.prepareStatement(l_Query);
					pstmt.setString(1, pAccNumber);
					pstmt.setString(2, pAccNumber);
					pstmt.setDate(3,java.sql.Date.valueOf(GeneralUtility.changeDateFormatyyyyMMdd(pDate)));
					l_RS = pstmt.executeQuery();
					while (l_RS.next()) {
						l_AccData.setExpire(true);
					}
				}
				pstmt.close();

			} catch (Exception e) {			
				e.printStackTrace();
			}
			return l_AccData;
		}
		
		public static Short getBusinessClassByAccNo(String pAccNo,String pBatchNo,Connection pConn) throws SQLException{
			Short result = 0;		
			PreparedStatement pstmt=pConn.prepareStatement("SELECT BusinessClass from AODF where AccNumber = ? and BatchNo=?");
			pstmt.setString(1, pAccNo);
			pstmt.setString(2, pBatchNo.equals("")?"1":pBatchNo);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				result = rs.getShort(1);			
			}		
			return result;
		}
		
		public LoanAccountData isExitLoanAccount(String s, Connection conn) throws SQLException {//mtdk
			LoanAccountData data =new LoanAccountData();
					String sql = "select max(ExpDate)As ExpDate,LoanType  from LAODF WHERE AccNumber='"+s+"' group  by LoanType ";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				data.setExpDate(SharedUtil.formatDBDate2MIT(rs.getString("ExpDate")));
				data.setLoanType(rs.getByte("LoanType"));
			}
			pstmt.close();
			return data;
			
		}
		
		public String getPBNumber(String aAccNumber, Connection conn) throws SQLException{
			String ret = "";
			Boolean PSSQL = false;
			PreparedStatement pstmt = null;

			
				if(SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN1() !=4){
					pstmt=conn.prepareStatement("SELECT TOP(1) PBNumber FROM PassBookHistory WHERE AccNumber=?  AND (PBStatus=? OR PBStatus =? ) ORDER BY PBStatus,PBSerialNo");
				}else{//MWD sql 2000 can't accept ()
					pstmt=conn.prepareStatement("SELECT TOP 1 PBNumber FROM PassBookHistory WHERE AccNumber=?  AND (PBStatus=? OR PBStatus =? ) ORDER BY PBStatus,PBSerialNo");
				}
			
			pstmt.setString(1, aAccNumber);
			pstmt.setInt(2, 1);
			pstmt.setInt(3, 5);
			pstmt.setQueryTimeout(DAOManager.getNormalTime());  //hnw added
			ResultSet rs=pstmt.executeQuery();

			while(rs.next()){
				ret = rs.getString("PBNumber");
			}
			pstmt.close();

			return ret;
		}	
		
		public static double getAmountlimit(String accountNumber, Connection conn) throws SQLException {
			 PreparedStatement pstmt = conn.prepareStatement("Select LimitAmount from UniversalAccAmountLimit where AccNumber=?");
	        double ret = 0.00;
	        pstmt.setString(1, accountNumber.trim());
	        ResultSet rs=pstmt.executeQuery();
	               
	        while(rs.next()){
	        	ret = rs.getFloat("LimitAmount");
	        }
	        rs.close();
	        pstmt.close();	    
	        return ret;
		}
		
		public String getLastDepositDate(String accNumber, Connection conn) throws SQLException{
			String LastDepositDate = "";
			String LastDate="";
			//zzm

			String query="select * from UniversalAccProvision Where AccNumber='"+accNumber+"' and CONVERT(datetime,'01/'+EntryMonth+'/'+EntryYear,103) In (" +
					"SELECT MAX(CONVERT(datetime,'01/'+EntryMonth+'/'+EntryYear,103)) AS MonthYear FROM UniversalAccProvision WHERE AccNumber='"+accNumber+"')";
			try {
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();

				while(rs.next()){
					LastDate=SharedUtil.formatYYYYMMDD2MIT(rs.getString("EffectiveDate"));
					LastDate=SharedUtil.formatMIT2DDMMYYYY(LastDate);
					LastDepositDate = String.valueOf(rs.getString("EntryMonth")) + "/" + String.valueOf(rs.getString("EntryYear")) + " ("+LastDate+")";
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return LastDepositDate;
		}
		
		public CallDepositAccountData getCallDepositAccountData(String pAccNo, Connection pConn) throws SQLException {
			CallDepositAccountData l_result = new CallDepositAccountData();
			String l_Query = "SELECT A.AccNumber,A.IntPayCode,A.AccTobePaid FROM CallDepositAccount A "
					+ " where A.AccNumber =? ";
			PreparedStatement l_pstmt = pConn.prepareStatement(l_Query);
			l_pstmt.setString(1, pAccNo);
			ResultSet rs = l_pstmt.executeQuery();
			while(rs.next()){
				l_result.setAccNumber(rs.getString("AccNumber"));
				l_result.setAccTobePaid(rs.getString("AccTobePaid"));
				l_result.setIntPayCode(rs.getInt("IntPayCode"));
				
			}
			return l_result;
		}
		
		public String getSavingAccToBePaid(String aAccNumber, Connection conn) throws SQLException{		
			String l_AccNumber= "";

			PreparedStatement pstmt=conn.prepareStatement("SELECT AccToBePaid FROM SavingsAccount WHERE AccNumber=?");

			pstmt.setString(1, aAccNumber);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				l_AccNumber = rs.getString("AccToBePaid");			
			}
			pstmt.close();
			return l_AccNumber;
		}	
		
		public String getSpecialAccToBePaid(String aAccNumber, Connection conn) throws SQLException{		
			String l_AccNumber= "";

			PreparedStatement pstmt=conn.prepareStatement("SELECT AccToBePaid FROM SpecialDepositAccount WHERE AccNumber=?");

			pstmt.setString(1, aAccNumber);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				l_AccNumber = rs.getString("AccToBePaid");			
			}
			pstmt.close();
			return l_AccNumber;
		}
		
		public AddressData getAddress(String aAccNumber,Connection conn) throws SQLException{	
			AddressData ret = new AddressData();
			PreparedStatement pstmt=conn.prepareStatement("SELECT HouseNo, Street, Ward, Township, City, " +
					" Division, Country, Phone, Email, Fax, PostalCode, Status, Website FROM MailingAddress WHERE AccNumber=?");

			pstmt.setString(1, aAccNumber);
			ResultSet rs=pstmt.executeQuery();

			while(rs.next()){
				ReadAddressData(ret, rs);
			}
			pstmt.close();
			return ret;
		}
		
		private void ReadAddressData(AddressData ret,ResultSet aRS)
		{
			try {
				ret.setHouseNo(aRS.getString("HouseNo"));
				ret.setStreet(aRS.getString("Street"));
				ret.setWard(aRS.getString("Ward"));
				ret.setTownship(aRS.getString("Township"));
				ret.setDistrict(aRS.getString("City"));
				ret.setDivision(aRS.getString("Division"));
				ret.setCountry(aRS.getString("Country"));
				ret.setTel1(aRS.getString("Phone"));
				ret.setEmail(aRS.getString("Email")); 
				ret.setFax(aRS.getString("Fax")); 
				ret.setStatus(aRS.getString("Status")); 
				ret.setWebsite(aRS.getString("Website")); 
				ret.setPostalCode(aRS.getString("PostalCode"));			
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		
		public boolean getMultipleSignature(String pAccountNo,String pFileName,Connection conn) {

			boolean flag = false;
			try {
				PreparedStatement pstmt=null;
				
					pstmt = conn.prepareStatement("SELECT * FROM CollateralPhoto..localsignature"+pFileName+" WHERE AccNumber = ? Order By SerialNo");
					pstmt.setString(1, pAccountNo);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					SignatureData l_SignData = new SignatureData();
					readSignData(l_SignData, rs);
					readRecordForSign(l_SignData,rs,pFileName); 
					lstSignData.add(l_SignData);
					flag = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
		}
		
		private void readSignData(SignatureData pSignData, ResultSet rs) {
			try {
				pSignData.setAccountNumber(rs.getString("AccNumber"));
				pSignData.setAccountName(rs.getString("AccName"));
				pSignData.setAccountType(rs.getString("AccType"));
				pSignData.setRemark(rs.getString("Remark"));
				pSignData.setN1(rs.getShort("N1"));
				pSignData.setSerialNo(rs.getString("SerialNo"));
				pSignData.setExtension(rs.getString("Extension"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void readRecordForSign(SignatureData pData, ResultSet prs,String pFileName) {		
			try {
				pData.setSignature(prs.getBytes("Signature"));
				if(prs.getBytes("Signature") != null){
					byte[] fileBytes = prs.getBytes("Signature");
					OutputStream targetFile;
					/* try {		
						targetFile = new FileOutputStream(SingletonServer.getAbsPath()+"cache//photos//signature//"+pData.getAccountNumber()+"_"+pData.getSerialNo()+ pData.getExtension());
						targetFile.write(fileBytes);
						targetFile.close();
					} catch (FileNotFoundException e) {					
						e.printStackTrace();
					} catch (IOException e) {					
						e.printStackTrace();					
					} */
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public AccountData getODAvailableBalanceForInformation(String pDate, String pAccNumber, Connection pConn) {
			AccountData l_AccData = new AccountData();
			try {
				String l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
						+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
						+ "inner join currentaccount b on a.accnumber = b.accnumber "
						+ "inner join accounts c on  a.accnumber = c.accnumber "
						+ "where a.status in ('1') "			
						+ "and a.expdate >= ? "
						+ "and a.batchno <> 0 "
						+ "and a.odtype <> 0 "
						+ "and a.odtype <> 100 "
						+ "and a.accnumber = ? "
						+ "group by todexpdate, todlimit, currentbalance ";

				PreparedStatement pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pDate);
				pstmt.setString(2, pDate);
				pstmt.setString(3, pAccNumber);

				ResultSet l_RS = pstmt.executeQuery();

				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
					l_AccData.setOdLimit(l_RS.getDouble("ODLimit"));
					l_AccData.setTODLimit(l_RS.getDouble("TODLimit"));
				}

				l_Query = "select currentbalance from accounts where accnumber = ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pAccNumber);
				l_RS = pstmt.executeQuery();			
				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
				}
				pstmt.close();

			} catch (Exception e) {			
				e.printStackTrace();
			}
			return l_AccData;
		}
		
		public AccountData getODAvailableBalanceForInformation(String pDate, String pAccNumber, String type, Connection pConn) {
			AccountData l_AccData = new AccountData();
			boolean ps = false;
			String l_Query = "";
			try {
			
					l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
							+ "(case when Convert(datetime , b.todexpdate) >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
							+ "inner join currentaccount b on a.accnumber = b.accnumber "
							+ "inner join accounts c on  a.accnumber = c.accnumber "
							+ "where a.status in ('1') ";
					if(type.equals("")){
						l_Query += "and Convert(datetime , a.expdate) >= ? ";
					}
				
					l_Query += "and Convert(datetime , a.sanctiondatebr) <= ? "
							+ "and a.batchno <> '0' "
							+ "and a.odtype <> 0 "
							+ "and a.odtype <> 100 "
							+ "and a.accnumber = ? "
							+ "group by todexpdate, todlimit, currentbalance ";

				PreparedStatement pstmt = pConn.prepareStatement(l_Query);
				if(ps)
					pstmt.setObject(1, LocalDate.parse(pDate.substring(0, 4) + 
							"-" + pDate.substring(4, 6) + 
							"-" + pDate.substring(6, pDate.length())));
				else
					pstmt.setString(1, pDate);
				if(type.equals("")){
					if(ps) {
						pstmt.setObject(2, LocalDate.parse(pDate.substring(0, 4) + 
								"-" + pDate.substring(4, 6) + 
								"-" + pDate.substring(6, pDate.length())));
						pstmt.setObject(3, LocalDate.parse(pDate.substring(0, 4) + 
								"-" + pDate.substring(4, 6) + 
								"-" + pDate.substring(6, pDate.length())));
					}else {
						pstmt.setString(2, pDate);
						pstmt.setString(3, pDate);
					}
					pstmt.setString(4, pAccNumber);
				} else {
					if(ps)
						pstmt.setObject(2, LocalDate.parse(pDate.substring(0, 4) + 
								"-" + pDate.substring(4, 6) + 
								"-" + pDate.substring(6, pDate.length())));
					else
						pstmt.setString(2, pDate);
					pstmt.setString(3, pAccNumber);
				}			

				ResultSet l_RS = pstmt.executeQuery();

				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
					l_AccData.setOdLimit(l_RS.getDouble("ODLimit"));
					l_AccData.setTODLimit(l_RS.getDouble("TODLimit"));
				}

				l_Query = "select currentbalance from accounts where accnumber = ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pAccNumber);
				l_RS = pstmt.executeQuery();			
				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
				}
				pstmt.close();

			} catch (Exception e) {			
				e.printStackTrace();
			}
			return l_AccData;
		}
		
		public double getAvaliableBalance(String aAccNumber, int pProcessType, Connection conn) throws  IOException,ClassNotFoundException,SQLException, ParserConfigurationException, SAXException{

			double lCurrentBal=0;
			int lAccStatus = 0 ;
			double lEarMarkedAmountBal=0;
			double lRetAmount=0;
			double lMinAmount=0;
			String lPrdCode=null;
			AccountData l_AccData = new AccountData();
			int l_MinBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN1();
			int l_CurBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN2();
			int l_BarMinBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN3();

			PreparedStatement pstmt=null;
			
			if(getAccount(aAccNumber, conn)) {
				l_AccData = getAccountsBean();
			}
			lCurrentBal = l_AccData.getCurrentBalance();
			lAccStatus = l_AccData.getStatus() ;
			if (conn.getMetaData().getDatabaseProductName().contains("PostgreSQL")) {
				pstmt=conn.prepareStatement("SELECT COALESCE(Sum(BarAmount),0) AS EarMarkedAmount  FROM AccBar WHERE AccNumber=?");
			}else {
				pstmt=conn.prepareStatement("SELECT ISNULL(Sum(BarAmount),0) AS EarMarkedAmount  FROM AccBar WHERE AccNumber=?");
			}
			

			pstmt.setString(1, aAccNumber);
			ResultSet rsBar=pstmt.executeQuery();

			while(rsBar.next()){
				lEarMarkedAmountBal = rsBar.getDouble("EarMarkedAmount");
			}    

			pstmt.close();

			lPrdCode= l_AccData.getProduct();
			if(l_MinBalSetting == 1) {
				lPrdCode = l_AccData.getProduct()+ l_AccData.getType();
			}

			int curId = 0;
			if(l_CurBalSetting ==1 ) {
				for(int i = 0;i<SharedLogic.getCurrencyCodes().size();i++){
					if(SharedLogic.getCurrencyCodes().get(i).getCode().equals(l_AccData.getCurrencyCode()) && !l_AccData.getCurrencyCode().equals("MMK")){
						curId = SharedLogic.getCurrencyCodes().get(i).getKey();break;
					}
				}
				lPrdCode = lPrdCode+((curId==0?"":curId));
			}
			if(pProcessType!=4) {
				pstmt=conn.prepareStatement("SELECT ProductId,ProductType,MinBalance  FROM ProductSetup WHERE ProductId=?");
		
				pstmt.setString(1, lPrdCode);
				pstmt.setQueryTimeout(DAOManager.getNormalTime());
				ResultSet rsMin=pstmt.executeQuery();
		
				while(rsMin.next()){
					lMinAmount = rsMin.getDouble("MinBalance");
				}    
			}
		
			pstmt.close();

			if(lEarMarkedAmountBal>0) {
				if(l_BarMinBalSetting==1)
					lRetAmount=lCurrentBal-lEarMarkedAmountBal-lMinAmount;
				else
					lRetAmount=lCurrentBal-lEarMarkedAmountBal;
			}
			else {
				if(lAccStatus == 2)
					lRetAmount=lCurrentBal ;
				else
					lRetAmount=lCurrentBal-lMinAmount;
			}		
			return lRetAmount;
		}
		
		public AccountData getLAAvailableBalanceForInformation(String pDate, String pAccNumber, Connection pConn) {
			AccountData l_AccData = new AccountData();
			try {
				String l_Query = "select sum(a.sanctionamountbr) as Limit " 
						+ "from laodf a "
						+ "inner join accounts c on  a.accnumber = c.accnumber "
						+ "where a.status in ('1') "
						+ "and a.expdate >= ? "
						+ "and a.sanctiondatebr <= ? "					
						+ "and a.accnumber = ? ";

				PreparedStatement pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pDate);
				pstmt.setString(2, pDate);
				pstmt.setString(3, pAccNumber);

				ResultSet l_RS = pstmt.executeQuery();

				while (l_RS.next()) {
					l_AccData.setOdLimit(l_RS.getDouble("Limit"));
				}

				l_Query = "select currentbalance from accounts where accnumber = ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pAccNumber);
				l_RS = pstmt.executeQuery();			
				while (l_RS.next()) {
					l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
				}
				pstmt.close();

			} catch (Exception e) {			
				e.printStackTrace();
			}
			return l_AccData;
		}
		
		public double getCurrentBalance(String aAccNumber, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			double result=0;

			PreparedStatement pstmt=conn.prepareStatement("SELECT CurrentBalance FROM Accounts WHERE AccNumber=?");
			pstmt.setString(1, aAccNumber);
			ResultSet rs=pstmt.executeQuery();

			while(rs.next()){
				result = rs.getDouble(1);
			}
			pstmt.close();
			return result;
		}
		
		public boolean updateAccountStatus(String pAccNumber, Connection conn) throws SQLException {
			boolean ret=false;
			PreparedStatement pstmt;
			
			String date = GeneralUtility.getCurrentDate();
			int pStatus = AccountData.Status_Closed;
			int index = 1;
			
			pstmt=conn.prepareStatement("UPDATE Accounts SET Status=?,LastUpdate=?,ClosingDate=? WHERE AccNumber=?");
			pstmt.setInt(index++, pStatus);
			pstmt.setString(index++, SharedUtil.formatDateStr2MIT(date));	
			pstmt.setString(index++, SharedUtil.formatDateStr2MIT(date));
			pstmt.setString(index++, pAccNumber);
			if(pstmt.executeUpdate()>0)  ret=true; 
			
			if(ret){
				if(SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() ==1){
					index = 1;
					pstmt=conn.prepareStatement("UPDATE Accounts_Balance SET Status=?,LastUpdate=?,ClosingDate=? WHERE AccNumber=?");
					pstmt.setInt(index++, pStatus);
					pstmt.setString(index++, SharedUtil.formatDateStr2MIT(date));	
					pstmt.setString(index++, SharedUtil.formatDateStr2MIT(date));	
					pstmt.setString(index++, pAccNumber);
					if(pstmt.executeUpdate()>0)  ret=true; 
				}				
			}
			pstmt.close();			
			return ret;
		}
		
		public DataResult getTransAccount_TransferOLTPUpdateQuery(String fromAccNumber, String toAccNumber, Connection conn)
				throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
			DataResult result = new DataResult();
			ArrayList<AccountData> accountDataList = new ArrayList<AccountData>();
			String statusDesc = "";

			PreparedStatement pstmt = conn.prepareStatement(
					"SELECT a.AccNumber,a.CurrentBalance, a.LastUpdate, a.Status, a.LastTransDate, t.T2, t.T3, t.T4, t.T5, t.T6, t.T7"
							+ " FROM Accounts a	INNER JOIN (SELECT t1, T2, T3, T4, T5, T6, T7  FROM T00005) t ON a.AccNumber = t.t1	WHERE a.AccNumber IN (?,?)");

			pstmt.setString(1, fromAccNumber);
			pstmt.setString(2, toAccNumber);
			pstmt.setQueryTimeout(DAOManager.getNormalTime());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccBean = new AccountData();
				AccBean.setAccountNumber(rs.getString("AccNumber"));
				readRecordUpdate(AccBean, rs, conn);
				statusDesc = Ref.getAccountStatusDescription(AccBean);// status description
				AccBean.setCurrencyRate(1);
				if (fromAccNumber.equals(rs.getString("AccNumber"))) {
					result = checkDebitAndCreditAcc("Dr", statusDesc, rs.getInt("Status"));
					if (result.getStatus()) {
						PreparedStatement pstmt_bar = conn.prepareStatement(
								"SELECT SUM(BarAmount) AS EarMarkedAmount FROM AccBar where AccNumber = ?");
						pstmt_bar.setString(1, fromAccNumber);
						pstmt_bar.setQueryTimeout(DAOManager.getNormalTime());
						ResultSet rs_bar = pstmt_bar.executeQuery();
						if (rs_bar.next()) {
							AccBean.setAvailableBalance(rs_bar.getDouble("EarMarkedAmount"));
						}
						rs_bar.close();
						pstmt_bar.close();
					}
				} else {
					result = checkDebitAndCreditAcc("Cr", statusDesc, rs.getInt("Status"));
				}

				if (result.getStatus()) {
					accountDataList.add(AccBean);
				} else {
					break;
				}
			}

			if (result.getStatus() && accountDataList.size() == 2) {
				result.setListAccountData(accountDataList);
			} else {
				result.setStatus(false);
				result.setDescription("Invalid Account Number");
			}
			pstmt.close();
			rs.close();
			return result;
		}
		
		private void readRecordUpdate(AccountData aAccBean, ResultSet aRS, Connection pConn) throws SQLException {
			aAccBean.setProduct(aRS.getString("T2"));
			aAccBean.setStatus(aRS.getByte("Status"));
			aAccBean.setLastTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("LastTransDate")));
			aAccBean.setCurrencyCode(aRS.getString("T4"));
			aAccBean.setBranchCode(aRS.getString("T5"));
			aAccBean.setLastUpdate(SharedUtil.formatDBDate2MIT(aRS.getString("LastUpdate")));
			aAccBean.setProductGL(aRS.getString("T6"));
			aAccBean.setCashInHandGL(aRS.getString("T7"));
			aAccBean.setCurrentBalance(StrUtil.round2decimals(aRS.getDouble("CurrentBalance")));
			aAccBean.setType(aRS.getString("T3"));
		}
		
		public DataResult checkDebitAndCreditAcc(String type, String statusDesc, int status)
				throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
			DataResult result = new DataResult();
			String transDesc = "";
			boolean isLoan = false;
			boolean isFD = false;
			isLoan = SharedLogic.isLoan(AccBean.getProduct());
			if (!isLoan)
				isFD = SharedLogic.isFD(AccBean.getProduct());
			// Check Account Status
			// 1 Suspend, 3 Closed, 4 Dormant, 7 Stopped Payment
			if (type.equalsIgnoreCase("Dr")) {
				transDesc = "Debit";
				if (isLoan || isFD) { // loan and fixed not allow
					result.setDescription("Not Allow " + transDesc + " Loan or Fixed Account!");
				} else if ((status == 0
						&& (!SharedUtil.formatMIT2DateStr(AccBean.getLastTransDate()).equals("01/01/1900")))) { // ||
																												// (status==2
																												// &&
																												// transAmount
																												// ==
																												// AccBean.getCurrentBalance())
					result.setStatus(true);
				} else { // status==1 || status==3 || status==4 || status==7 || (statusDesc.equals("New")
							// && !SharedLogic.isLoan(AccBean.getProduct())) || (transAmount !=
							// AccBean.getCurrentBalance())
					result.setDescription("Invalid " + transDesc + " Account Status ( " + statusDesc + ")!");
				}
			} else if (type.equalsIgnoreCase("Cr")) {
				transDesc = "Credit";
				if (isLoan || isFD) { // loan and fixed not allow
					result.setDescription("Not Allow " + transDesc + " Loan or Fixed Account!");
				} else if (status == 0 || status == 4 || status == 7) {
					result.setStatus(true);
				} else {// status==1 || status==2 || status==3
					result.setDescription("Invalid " + transDesc + " Account Status ( " + statusDesc + ")!");
				}
			}
			return result;
		}
		
		public TransferDataResult updateAccountBalance_TransferUpdate(TransferDataResult transferDataResult,
				String l_TableName, String l_TableName1, Connection conn)
				throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {

			String l_Query = "";
			PreparedStatement pstmt = null;
			Double availableBalance = 0.0;
			AccountData accdata = new AccountData();
			TransferData fromTransferData = new TransferData();
			TransferData toTransferData = new TransferData();
			if(transferDataResult.getTransferlist().size()<3) {
				fromTransferData = transferDataResult.getTransferlist().get(0);
				toTransferData = transferDataResult.getTransferlist().get(1);
			}else if(transferDataResult.getTransferlist().size()<4) {
				fromTransferData = transferDataResult.getTransferlist().get(0);
				toTransferData = transferDataResult.getTransferlist().get(2);
			}else if(transferDataResult.getTransferlist().size()<5) {
				fromTransferData = transferDataResult.getTransferlist().get(0);
				toTransferData = transferDataResult.getTransferlist().get(3);
			}else {
			fromTransferData = transferDataResult.getTransferlist().get(0);
			toTransferData = transferDataResult.getTransferlist().get(4);
			}
			accdata.setAvailableBalance(fromTransferData.getAccountBarAmount());
			l_Query = "SELECT AccNumber,CurrentBalance,status FROM " + l_TableName
					+ " WITH (ROWLOCK) WHERE AccNumber in (?,?) ";

			pstmt = conn.prepareStatement(l_Query);
			pstmt.setString(1, fromTransferData.getFromAccNumber());
			pstmt.setString(2, toTransferData.getFromAccNumber());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (fromTransferData.getFromAccNumber().equals(rs.getString("AccNumber"))) {
					accdata.setCurrentBalance(rs.getDouble("currentbalance"));
					accdata.setStatus(rs.getInt("status"));
					transferDataResult.setStatus(true);
					transferDataResult.getTransferlist().get(0).setPrevBalance(rs.getDouble("CurrentBalance"));

					availableBalance = getAvaliableBalance(accdata, fromTransferData.getProductMinOpeningBalance(), 0,
							conn);
					// Check For A/C Status Closed Pending
					if (accdata.getStatus() != 2) {
						if (fromTransferData.getAmount() > availableBalance) {
							transferDataResult.setStatus(false);
							transferDataResult.setDescription(fromTransferData.getFromAccNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma((fromTransferData.getAmount() - availableBalance)));
							break;
						}
					} else {
						if (fromTransferData.getAmount() != accdata.getCurrentBalance()) {
							transferDataResult.setStatus(false);
							transferDataResult.setDescription(fromTransferData.getFromAccNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma(
											(fromTransferData.getAmount() - accdata.getCurrentBalance())));
							break;
						}
					}

				} else {
					transferDataResult.setStatus(true);
					if(transferDataResult.getTransferlist().size()<3) {
						transferDataResult.getTransferlist().get(1).setPrevBalance(rs.getDouble("CurrentBalance"));
					}else if(transferDataResult.getTransferlist().size()<4) {
						transferDataResult.getTransferlist().get(2).setPrevBalance(rs.getDouble("CurrentBalance"));
					}else if(transferDataResult.getTransferlist().size()<5) {
						transferDataResult.getTransferlist().get(3).setPrevBalance(rs.getDouble("CurrentBalance"));
					}else {
						transferDataResult.getTransferlist().get(4).setPrevBalance(rs.getDouble("CurrentBalance"));
					}
					
				}

			}
			pstmt.close();

			if (transferDataResult.isStatus()) {
				l_Query = "UPDATE " + l_TableName + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
						+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
				pstmt = conn.prepareStatement(l_Query);
				pstmt.setDouble(1, fromTransferData.getAmount());
				pstmt.setString(2, fromTransferData.getTransactionDate());
				pstmt.setString(3, fromTransferData.getTransactionDate());
				pstmt.setString(4, fromTransferData.getFromAccNumber());
				if (pstmt.executeUpdate() > 0) {
					transferDataResult.setStatus(true);
				}
				pstmt.close();

				if (transferDataResult.isStatus()) {
					if (!l_TableName1.equals("")) {
						transferDataResult.setStatus(false);
						l_Query = "UPDATE " + l_TableName1 + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), "
								+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
						pstmt = conn.prepareStatement(l_Query);
						pstmt.setDouble(1, fromTransferData.getAmount());
						pstmt.setString(2, fromTransferData.getTransactionDate());
						pstmt.setString(3, fromTransferData.getTransactionDate());
						pstmt.setString(4, fromTransferData.getFromAccNumber());
						if (pstmt.executeUpdate() > 0) {
							transferDataResult.setStatus(true);
						}
						pstmt.close();
					}
				}

				l_Query = "UPDATE " + l_TableName + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
						+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
				pstmt = conn.prepareStatement(l_Query);
				pstmt.setDouble(1, toTransferData.getAmount());
				pstmt.setString(2, toTransferData.getTransactionDate());
				pstmt.setString(3, toTransferData.getTransactionDate());
				pstmt.setString(4, toTransferData.getFromAccNumber());
				if (pstmt.executeUpdate() > 0) {
					transferDataResult.setStatus(true);
				}
				pstmt.close();

				if (transferDataResult.isStatus()) {
					if (!l_TableName1.equals("")) {
						transferDataResult.setStatus(false);
						l_Query = "UPDATE " + l_TableName1 + " SET CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), "
								+ " LastTransDate=?, LastUpdate=? WHERE AccNumber =?";
						pstmt = conn.prepareStatement(l_Query);
						pstmt.setDouble(1, toTransferData.getAmount());
						pstmt.setString(2, toTransferData.getTransactionDate());
						pstmt.setString(3, toTransferData.getTransactionDate());
						pstmt.setString(4, toTransferData.getFromAccNumber());
						if (pstmt.executeUpdate() > 0) {
							transferDataResult.setStatus(true);
						}
						pstmt.close();
					}
				}
			}
			return transferDataResult;
		}
		
		public DataResult saveAccountTransaction_Transfer(ArrayList<TransferData> transferlist, Connection conn)
				throws SQLException {
			DataResult result = new DataResult();
//			long l_TranRef = GeneralUtility.generateSyskey();
			int l_TranRef = DBTransactionMgr.nextValue();
			int lasttransno = 0 ;
			String sql = "";
			PreparedStatement pstmt;
			TransferData transferdata = new TransferData();
			sql = "Insert Into AccountTransaction(BranchCode,WorkStation,TransREf,TellerId,SupervisorId,TransTime,TransDate,Description,"
					+ "ChequeNo,CurrencyCode,CurrencyRate,Amount,TransType,AccNumber,PrevBalance, PrevUpdate,EffectiveDate,ContraDate,Status,Remark, SubRef,AccRef) "
					+ "Values ";
			
			for (int i = 0; i < transferlist.size(); i++) {
				if(i == 0) {
					sql +="(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				}else {
					sql +=",(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				}
			}
			sql += "SELECT SCOPE_IDENTITY() as lasttransno;";
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			for (int i = 0; i < transferlist.size(); i++) {
				transferdata = transferlist.get(i);
				index = updateATRecord_TransferUpdate(transferdata, l_TranRef, index, pstmt);
			}
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				lasttransno = rs.getInt("lasttransno");
			}

			sql = "UPDATE Accounttransaction set transref = ? WHERE transref = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lasttransno);
			pstmt.setInt(2, l_TranRef);
			pstmt.executeUpdate();
			
			sql = "Insert Into AccountGLTransaction(TransNo,AccNumber,GLCode1,GLCode2,GLCode3,GLCode4,GLCode5,BaseCurCode,BaseCurOperator,"
					+ "BaseRate,BaseAmount,MediumCurCode, MediumCurOperator,MediumRate,MediumAmount,TrCurOperator,TrRate,TrAmount,TrCurCode,TrPrevBalance, T2, T3) "
					+ "SELECT TransNo,AccNumber,SubRef,AccRef,'',"
					+ "'','',CurrencyCode,'/',CurrencyRate,amount,'','',0,0,'/',CurrencyRate,"
					+ "Amount,CurrencyCode,PrevBalance, SupervisorID,TellerID FROM AccountTransaction where TransREf = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lasttransno);
			pstmt.executeUpdate();
			pstmt.close();

			result.setStatus(true);
			result.setTransRef(lasttransno);
			return result;
		}
		
		private int updateATRecord_Transfer(TransferData transferData, Long transref, int index, PreparedStatement aPS)
				throws SQLException {
			aPS.setString(index++, transferData.getFromBranchCode() == null ? "001" : transferData.getFromBranchCode());
			aPS.setString(index++, "Mobile");
			aPS.setLong(index++, transref);
			aPS.setString(index++, transferData.getUserID());
			aPS.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			aPS.setString(index++,
					transferData.getTransactionTime() == null ? "12:00:00" : transferData.getTransactionTime());
			aPS.setString(index++, transferData.getTransactionDate() == null ? SharedUtil.getCurrentDate()
					: transferData.getTransactionDate());
			aPS.setString(index++, transferData.getPostDescription() == null ? "" : transferData.getPostDescription());
			aPS.setString(index++, transferData.getReferenceNo());
			aPS.setString(index++, transferData.getCurrencyCode() == null ? "MMK" : transferData.getCurrencyCode());
			aPS.setDouble(index++, transferData.getBasecurrate() == 0 ? 1 : transferData.getBasecurrate());
			aPS.setDouble(index++, transferData.getBaseAmount());
			aPS.setInt(index++, transferData.getIbtTransType());
			aPS.setString(index++, transferData.getFromAccNumber());
			aPS.setDouble(index++, transferData.getPrevBalance());
			aPS.setString(index++,
					transferData.getPreviousDate() == null ? SharedUtil.getCurrentDate() : transferData.getPreviousDate());
			aPS.setString(index++, transferData.getEffectiveDate() == null ? SharedUtil.getCurrentDate()
					: transferData.getEffectiveDate());
			aPS.setString(index++, "01/01/1900");
			aPS.setInt(index++, 1);
			aPS.setString(index++, transferData.getRemark() == null ? "" : transferData.getRemark());
			aPS.setString(index++, transferData.getProductGL());
			aPS.setString(index++, transferData.getCashInHandGL());
			return index;
		}
		
		private int updateATRecord_TransferUpdate(TransferData transferData, int transref, int index, PreparedStatement aPS)
				throws SQLException {
			aPS.setString(index++, transferData.getFromBranchCode() == null ? "001" : transferData.getFromBranchCode());
			aPS.setString(index++, "Mobile");
			aPS.setInt(index++, transref);
			aPS.setString(index++, transferData.getUserID());
			aPS.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			aPS.setString(index++,
					transferData.getTransactionTime() == null ? "12:00:00" : transferData.getTransactionTime());
			aPS.setString(index++, transferData.getTransactionDate() == null ? SharedUtil.getCurrentDate()
					: transferData.getTransactionDate());
			aPS.setString(index++, transferData.getPostDescription() == null ? "" : transferData.getPostDescription());
			aPS.setString(index++, transferData.getReferenceNo());
			aPS.setString(index++, transferData.getCurrencyCode() == null ? "MMK" : transferData.getCurrencyCode());
			aPS.setDouble(index++, transferData.getBasecurrate() == 0 ? 1 : transferData.getBasecurrate());
			aPS.setDouble(index++, transferData.getBaseAmount());
			aPS.setInt(index++, transferData.getIbtTransType());
			aPS.setString(index++, transferData.getFromAccNumber());
			aPS.setDouble(index++, transferData.getPrevBalance());
			aPS.setString(index++,
					transferData.getPreviousDate() == null ? SharedUtil.getCurrentDate() : transferData.getPreviousDate());
			aPS.setString(index++, transferData.getEffectiveDate() == null ? SharedUtil.getCurrentDate()
					: transferData.getEffectiveDate());
			aPS.setString(index++, "01/01/1900");
			aPS.setInt(index++, 1);
			aPS.setString(index++, transferData.getRemark() == null ? "" : transferData.getRemark());
			aPS.setString(index++, transferData.getProductGL());
			aPS.setString(index++, transferData.getCashInHandGL());
			return index;
		}
}
