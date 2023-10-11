package com.nirvasoft.rp.mgr;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.core.SingletonServer;
import com.nirvasoft.rp.util.LogicMgr;
import com.nirvasoft.rp.dao.CheckIssueDAO;
import com.nirvasoft.rp.dao.CurrentIBTInDAO;
import com.nirvasoft.rp.core.ccbs.dao.DenominationDAO;
import com.nirvasoft.rp.dao.FECTransactionDetailsDAO;
import com.nirvasoft.rp.dao.FECTransactionHeaderDAO;
import com.nirvasoft.rp.core.ccbs.dao.FixedDepositAccountDAO;
import com.nirvasoft.rp.dao.MultiTransferDAO;
import com.nirvasoft.rp.dao.UniversalAccountDAO;
import com.nirvasoft.rp.core.ccbs.data.db.DBSystemLogMgr;
import com.nirvasoft.rp.dao.AccountDao;
import com.nirvasoft.rp.dao.AccountGLTransactionDAO;
import com.nirvasoft.rp.dao.AccountLinksDao;
import com.nirvasoft.rp.dao.AccountTransactionDAO;
import com.nirvasoft.rp.dao.AccountsDAO;
import com.nirvasoft.rp.dao.BankHolidayDAO;
import com.nirvasoft.rp.dao.GLDAO;
import com.nirvasoft.rp.dao.LastDatesDAO;
import com.nirvasoft.rp.dao.LoanAccountDAO;
import com.nirvasoft.rp.dao.LoanAllowSetupDAO;
import com.nirvasoft.rp.dao.LoanInterestDAO;
import com.nirvasoft.rp.dao.POCommissionRateDAO;
import com.nirvasoft.rp.dao.ProductFeatureDAO;
import com.nirvasoft.rp.shared.AccountGLTransactionData;
import com.nirvasoft.rp.shared.AccountLinksData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.LoanAllowSetupData;
import com.nirvasoft.rp.shared.MultiTransferCriteriaData;
import com.nirvasoft.rp.shared.POCommissionRateData;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ProductFeatureData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.TransferDataResult;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.DenoAndTrData;
import com.nirvasoft.rp.shared.icbs.ProductFeatureType;
import com.nirvasoft.rp.shared.icbs.Enum.TransCode;
import com.nirvasoft.rp.shared.FixedDepositAccountData;
import com.nirvasoft.rp.shared.icbs.ReferenceAccountsData;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.icbs.WithdrawData;
import com.nirvasoft.rp.shared.icbs.Enum;
import com.nirvasoft.rp.util.FunctionID;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;
import com.nirvasoft.rp.dao.DenoAndTrDAO;

public class DBTransactionMgr {

	private static final AtomicInteger counter = new AtomicInteger(123456789);
	
	public static ArrayList<AccountGLTransactionData> prepareAccountGLTransactions(
			ArrayList<TransactionData> pTransactionDatas, Connection conn) {
		ArrayList<AccountGLTransactionData> ret = new ArrayList<AccountGLTransactionData>();
		try {
			String fkey = "smTransfer"+"_"+pTransactionDatas.get(0).getCurrencyCode()+"_"+ pTransactionDatas.get(0).getBranchCode();
			
			double l_CurrencyRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);
			
			//double l_CurrencyRate = DBFECCurrencyRateMgr.getFECCurrencyRate(pTransactionDatas.get(0).getCurrencyCode(),
			//		"smTransfer", pTransactionDatas.get(0).getBranchCode(), conn);
			//double l_CurrencyRate = 1;
			for (int i = 0; i < pTransactionDatas.size(); i++) {
				ret.add(prepareAccountGLTransaction(pTransactionDatas.get(i), l_CurrencyRate));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return ret;
	}
	
	public static AccountGLTransactionData prepareAccountGLTransaction(TransactionData pTransData, double baseRate) {
		AccountGLTransactionData object = new AccountGLTransactionData();
		object.setTransNo(pTransData.getTransactionNumber());
		object.setAccNumber(pTransData.getAccountNumber());
		object.setGLCode1(pTransData.getProductGL());
		object.setGLCode2(pTransData.getCashInHandGL());
		object.setGLCode3("");
		object.setGLCode4("");
		object.setGLCode5("");
		object.setBaseCurCode("MMK");
		object.setBaseCurOperator("/");
		object.setBaseRate(baseRate);
		object.setBaseAmount(StrUtil.round2decimals(
				(pTransData.getAmount() * (pTransData.getCurrencyRate() * 1000) / 1000), RoundingMode.HALF_UP));
		object.setMediumCurCode("");
		object.setMediumCurOperator("");
		object.setTrCurCode(pTransData.getCurrencyCode());
		object.setTrCurOperator(""); // To Set Transaction Cur Operator
		object.setTrRate(pTransData.getCurrencyRate());
		object.setTrAmount(StrUtil.round2decimals(pTransData.getAmount(), RoundingMode.HALF_UP));
		object.setTrPrevBalance(StrUtil.round2decimals(pTransData.getPreviousBalance(), RoundingMode.HALF_UP));
		object.setN1(0);
		object.setN2(0);
		object.setN3(0);
		object.setT1("");
		object.setT2("");
		object.setT3("");
		return object;
	}
	
	public static ArrayList<TransactionData> prepareBaseCurrencys(ArrayList<TransactionData> pDatas) {
		ArrayList<TransactionData> ret = new ArrayList<TransactionData>();
		for (int i = 0; i < pDatas.size(); i++) {
			ret.add(prepareBaseCurrency(pDatas.get(i)));
		}
		return ret;
	}
	
	public static TransactionData prepareBaseCurrency(TransactionData pData) {
		TransactionData ret = new TransactionData();
		ret = pData;
		ret.setAmount(StrUtil.round2decimals((ret.getAmount() * (ret.getCurrencyRate() * 1000) / 1000),
				RoundingMode.HALF_UP));
		ret.setPreviousBalance(StrUtil.round2decimals(
				(ret.getPreviousBalance() * (ret.getCurrencyRate() * 1000) / 1000), RoundingMode.HALF_UP));
		ret.setCurrencyCode("MMK");
		ret.setCurrencyRate(1);
		return ret;
	}
	//========== new post =====================================
	public static DataResult checkTransactionNew(TransactionData pTransData,
			AccountData pAccData, AccountGLTransactionData pAccGLTransData) {
		DataResult ret = new DataResult();
		String l_ProductID = "";
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN1()== 1) {
			l_ProductID = pAccData.getProduct() + pAccData.getType();
		}else {
			l_ProductID = pAccData.getProduct();
		}
		// For Debit Case
		if (SharedLogic.isDebit(pTransData.getTransactionType())) {
			// Check Cash Or Transfer
			if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType() < 999) { // For Debit // Transfer Case
				// Check Multiple Of
				ret = SharedLogic.checkMultipleOf(pTransData.getAmount(),l_ProductID,pAccData.getCurrencyCode());
				if (ret.getStatus()){				
					ret = SharedLogic.checkMinWithdrawal(pTransData.getTransactionType(),(pAccGLTransData.getTrAmount()), l_ProductID, pAccGLTransData.getTrCurCode());
				}// To Check Cheque Number
			}
		} else { // For Credit Case
			if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType() <= 500) { // For Credit // Transfer Case
				// Check Multiple Of
				ret = SharedLogic.checkMultipleOf(pTransData.getAmount(), l_ProductID,pAccData.getCurrencyCode());		
				// Check Min Opening Balance
				if (ret.getStatus())
					ret = SharedLogic.checkMinOpnBalanceCurrency(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(), pAccData);
			}
		}
		return ret;
	}
	
	public static DataResult postNew(ArrayList<TransactionData> arl, ArrayList<AccountGLTransactionData> aAccGLTrList, Connection conn) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException {
		boolean lRlt =false;
		DataResult ret = new DataResult(); 
		
		int lUncommTransNo=0,lTranRef=0;
		String lByForceCheque = "",lByForceCheque1="";
		
		lTranRef= nextValue();
		
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		AccountTransactionDAO lAccTranDAO=new AccountTransactionDAO();
		AccountsDAO lAccDAO= new AccountsDAO(conn); //for Mobile 
		GLDAO l_GLDAO = new GLDAO();
		ProductFeatureDAO dao = new ProductFeatureDAO();
		
		ArrayList<AccountGLTransactionData> l_AccountGLTransactionList=new ArrayList<AccountGLTransactionData>();
		AccountData l_AccountData = new AccountData();

		//For Voucher Serial
		String l_VoucherSerialNo = "";
		
		long m_AutoPrintTransRef=0;
		ArrayList<Integer> m_AutoLinkTransRef=new ArrayList<Integer>();
		try {
			ret.setStatus(false);
			// Getting ByForce Cheque
			lByForceCheque1 = SharedLogic.getSystemData().getpByForceCheque();//DBSystemMgr.getByForceCheck(conn);

			for (int i=0;i<arl.size();i++){
				ret.setStatus(false);
				lByForceCheque = "";
				l_AccountData = new AccountData();				
				arl.get(i).setTransactionReference(lTranRef);
				arl.get(i).setAmount(StrUtil.round2decimals(arl.get(i).getAmount()));
				//======= note ::
				
				arl.get(i).setTransactionTime(GeneralUtil.getCurrentDateYYYYMMDDHHMMSS());
				
				// Checking Account is GL Account
				if (l_GLDAO.getGL(arl.get(i).getAccountNumber(), conn)){
					arl.get(i).setCashInHandGL(dao.getCashInHandGL(aAccGLTrList.get(i).getTrCurCode(), 1, conn));
					aAccGLTrList.get(i).setGLCode2(dao.getCashInHandGL(aAccGLTrList.get(i).getTrCurCode(), 1, conn));
					ret.setStatus(true);						
				} else {						
					// For Checking Transaction	
					ret = checkTransactionNew(arl.get(i), l_AccountData, aAccGLTrList.get(i));

					if(ret.getStatus()){ // For Checking Transaction Limit						
						ret = DBTransAmtLimitMgr.checkTransAmtLimit(l_AccountData.getProduct(), l_AccountData.getType(), arl.get(i).getTransactionType(),
								l_AccountData.getAccountNumber(), arl.get(i).getTransactionDate(), arl.get(i).getAmount(),
								arl.get(0).isIBTDeposit(),arl.get(0).isIBTWithdraw(), conn);						
					}
					
					// For Current Account , Cheque Number Case
					if (ret.getStatus()){
						//Checking Debit Case
						if (arl.get(i).getTransactionType()>=500){
							// Checking Product Type
							if (SharedLogic.getProductCodeToProductData(l_AccountData.getProduct()).hasCheque()){
								lByForceCheque = lByForceCheque1;
								arl.get(i).setReferenceNumber(lByForceCheque);
							}else{
								ret.setStatus(true);
							}
							if (ret.getStatus()){
								//HNNS For Check Can Withdraw and Post Link
								if(canWithdrawWithMultiCurrency(arl.get(i).getAccountNumber(),aAccGLTrList.get(i).getTrAmount(), conn)){
									ret.setStatus(true);
								}else{
									ret = com.nirvasoft.rp.core.ccbs.data.db.DBTransactionMgr.checkAndPostLinkWithMultiCurrency(arl.get(i), aAccGLTrList.get(i).getTrAmount(),aAccGLTrList.get(i).getTrCurCode(),aAccGLTrList.get(i).getTrRate(), conn);
									if(ret.getStatus()){
										m_AutoPrintTransRef = ret.getTransactionNumber();	
										m_AutoLinkTransRef.add((int)m_AutoPrintTransRef);
//										if (ret.getTransRef() != 0) m_AutoLinkTransRef.add(ret.getTransRef());  ynw_test
										if (lAccDAO.getAccount(arl.get(i).getAccountNumber(), conn)){
											l_AccountData = lAccDAO.getAccountsBean();
											arl.get(i).setPreviousBalance(l_AccountData.getCurrentBalance()*arl.get(i).getCurrencyRate());
											aAccGLTrList.get(i).setTrPrevBalance(l_AccountData.getCurrentBalance());													
										}
									}else{
										ret.setStatus(false);
										if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
											ret.setDescription("Insufficient Balance!!");
										}
									}
								}	
							}
						}else{
							ret.setStatus(true);
						}
					} 

					if (ret.getStatus()){
						// Updating Balance
						ret = lAccDAO.updateAccountBalanceCurrency(arl.get(i).getAccountNumber(), aAccGLTrList.get(i).getTrAmount(),arl.get(i).getTransactionType()
									, arl.get(i).getTransactionDate(),arl.get(i).getEffectiveDate(), aAccGLTrList.get(i).getTrCurCode(), conn);
						
						if (ret.getStatus()) {
							if (arl.get(i).getCurrencyCode().equals(aAccGLTrList.get(i).getTrCurCode())){
								arl.get(i).setPreviousBalance(StrUtil.round2decimals(ret.getBalance(),RoundingMode.HALF_UP));
							}else{ 
								arl.get(i).setPreviousBalance(StrUtil.round2decimals(ret.getBalance() * aAccGLTrList.get(i).getTrRate(),RoundingMode.HALF_UP));
							}
							aAccGLTrList.get(i).setTrPrevBalance(StrUtil.round2decimals(ret.getBalance(),RoundingMode.HALF_UP));
							ret.setStatus(true);
						}
					}
				}
				// Adding Account Transaction
				if (ret.getStatus()){
					arl.get(i).setAmount(StrUtil.round2decimals(arl.get(i).getAmount(),RoundingMode.HALF_UP));
					arl.get(i).setComm(StrUtil.round2decimals(arl.get(i).getComm(),RoundingMode.HALF_UP));//SMM
					ret = lAccTranDAO.addAccTransaction(arl.get(i), conn);			//HNNS Fixed					
					if (ret.getStatus()){
						lRlt=true;
					}else{
						ret.setStatus(false);
						if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
							ret.setDescription("Please Try Again!!");
						}
						break;
					}
					
					if (ret.getStatus()){
						if (i==0){
							lUncommTransNo = (int) ret.getTransactionNumber();
						}
								
						if (lAccTranDAO.updateTransRef(lTranRef,lUncommTransNo,conn) && lRlt){
							ret.setStatus(true);
							ret.setTransactionNumber(lUncommTransNo);
							
						} else {
							ret.setStatus(false);
							if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
								ret.setDescription("Please Try Again!!");
							}
						}	
					}
				} else {
					if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
						ret.setDescription("Please Try Again!!");
					}
					break;
				}
			}
		
			//For Auto Link TransRef
			if (ret.getStatus() && m_AutoLinkTransRef.size()>0){
				for (int i=0;i<m_AutoLinkTransRef.size();i++){
					if (lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,(int) m_AutoLinkTransRef.get(i), conn)){
						ret.setStatus(true);
						
					} else {
						ret.setStatus(false);
						if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
							ret.setDescription("Please Try Again!!");break;
						}
					}
				}
			}
			
			//SSH For Add in AccountGLTransaction
			if(ret.getStatus()) {
		
				l_AccountGLTransactionList=aAccGLTrList;
				AccountGLTransactionDAO l_AccGLTrDAO = new AccountGLTransactionDAO();
				//check table exist or not.
					ArrayList<TransactionData> l_TrDataList3 = new ArrayList<TransactionData>();
					if( lAccTranDAO.getAccTransactionsTransRef((int)ret.getTransactionNumber(), conn))
					{
						l_TrDataList3 = lAccTranDAO.getTransactionDataList();
					}
					
					int j = 0;
					if(l_TrDataList3.size() == 0){
						ret.setStatus(false);
						if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
							ret.setDescription("Please Try Again!!");
						}
					}else{
						for (AccountGLTransactionData accountGLTransactionData : l_AccountGLTransactionList) {				
							accountGLTransactionData.setTransNo(l_TrDataList3.get(j).getTransactionNumber());
							accountGLTransactionData.setT2(l_TrDataList3.get(j).getAuthorizerID());
							ret.setStatus(l_AccGLTrDAO.addAccountGLTransaction(accountGLTransactionData, conn)); 
							j++;
						}
						if(ret.getStatus()) {
							ret.setVNo(l_VoucherSerialNo);
							ret.setCode(l_VoucherSerialNo);
							ret.setTransactionNumber(lUncommTransNo);
						}else{
							ret.setStatus(false);
							if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
								ret.setDescription("Please Try Again!!");
							}
						}
					}
			} 			
			
			if(ret.getStatus()){				
				ret.setEffectiveDate(arl.get(0).getEffectiveDate());
			}

		} catch (ParserConfigurationException e) {			
			ret.setStatus(false);			
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} catch (SAXException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");		
			e.printStackTrace();
		} catch (IOException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} 
		return ret;
	}
	
	public static int nextValue() {
		return counter.getAndIncrement();
	}
	
	public static String getEffectiveTransDate(String pDate, BankHolidayDAO pDAO, LastDatesDAO pLastDateDAO, Connection conn)throws SQLException{
		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));
		Calendar cal = Calendar.getInstance();
		month --;	// In Java Calendar, month is starting zero.
		cal.set(year, month, day);	
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		String effectiveDate = pDate;
		// Checking is Runned EOD
		if (pLastDateDAO.isRunnedEOD(pDate, conn)){
			cal.clear();
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			effectiveDate = getEffectiveTransDate(effectiveDate, pDAO, pLastDateDAO, conn);
		} else if (StrUtil.isWeekEnd(pDate)){
			cal.clear();
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			effectiveDate = getEffectiveTransDate(effectiveDate, pDAO, pLastDateDAO, conn);
		} else if (pDAO.getBankHolidayCheck(pDate, conn)){ // Checking BankHoliday
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			effectiveDate = getEffectiveTransDate(effectiveDate, pDAO, pLastDateDAO, conn);
		} else {
			effectiveDate = df.format(cal.getTime());
		}
		return effectiveDate;
	} 
	
	public static boolean canWithdrawWithMultiCurrency(String pAccNo, double pAmount, Connection pConn)throws SQLException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException{
		boolean l_result = false;
		double l_Available = 0;
		LoanAccountDAO l_LoanDAO = new LoanAccountDAO();
		
		AccountData l_AccData = new AccountData();
		AccountsDAO l_AccDAO = new AccountsDAO(pConn);
		
		if (l_AccDAO.getAccount(pAccNo, pConn)) {
			l_AccData = l_AccDAO.getAccountsBean();
		}
		double l_CurrentBal = l_AccData.getCurrentBalance();
		
		if(l_LoanDAO.getLoanAccount(pAccNo, pConn)){
			l_Available = l_LoanDAO.getLoanAccBean().getLimit();
		}else{
			l_Available = DBAccountMgr.getAvailableBalance(pAccNo, pConn);
		}
		
		// Check For A/C Status Closed Pending
		if (l_AccData.getStatus() != 2) {
			if(pAmount <= l_Available){
				l_result = true;
			}else{
				l_result = false;
			}
		} else {
			if(pAmount == l_CurrentBal){
				l_result = true;
			}else{
				l_result = false;
			}
		}
		return l_result;
	}
	
	
	///=========== Post multi transaction ========
	public static DataResult postMulti(ArrayList<TransactionData> arl, ArrayList<AccountGLTransactionData> aAccGLTrList, Connection conn) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException {
		
		DataResult ret = new DataResult(); 
		
		int lTranRef=0;
		String lByForceCheque = "",lByForceCheque1="";
		
		lTranRef= nextValue();
		
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		AccountTransactionDAO lAccTranDAO=new AccountTransactionDAO();
		AccountsDAO lAccDAO= new AccountsDAO(conn); //for Mobile 
		GLDAO l_GLDAO = new GLDAO();
		ProductFeatureDAO dao = new ProductFeatureDAO();
		
		AccountData l_AccountData = new AccountData();
		
		long m_AutoPrintTransRef=0;
		ArrayList<Integer> m_AutoLinkTransRef=new ArrayList<Integer>();
		try {
			ret.setStatus(false);
			// Getting ByForce Cheque
			lByForceCheque1 = SharedLogic.getSystemData().getpByForceCheque();//DBSystemMgr.getByForceCheck(conn);

			for (int i=0;i<arl.size();i++){
				ret.setStatus(false);
				lByForceCheque = "";
				l_AccountData = new AccountData();				
				arl.get(i).setTransactionReference(lTranRef);
				arl.get(i).setAmount(StrUtil.round2decimals(arl.get(i).getAmount()));
				//======= note ::
				
				arl.get(i).setTransactionTime(GeneralUtil.getCurrentDateYYYYMMDDHHMMSS());
				
				// Checking Account is GL Account
				if (l_GLDAO.getGL(arl.get(i).getAccountNumber(), conn)){
					arl.get(i).setCashInHandGL(dao.getCashInHandGL(aAccGLTrList.get(i).getTrCurCode(), 1, conn));
					aAccGLTrList.get(i).setGLCode2(dao.getCashInHandGL(aAccGLTrList.get(i).getTrCurCode(), 1, conn));
					arl.get(i).setGl(true);
					ret.setStatus(true);						
				} else {						
					// For Checking Transaction	
					ret = checkTransactionNew(arl.get(i), l_AccountData, aAccGLTrList.get(i));

					if(ret.getStatus()){ // For Checking Transaction Limit						
						ret = DBTransAmtLimitMgr.checkTransAmtLimit(l_AccountData.getProduct(), l_AccountData.getType(), arl.get(i).getTransactionType(),
								l_AccountData.getAccountNumber(), arl.get(i).getTransactionDate(), arl.get(i).getAmount(),
								arl.get(0).isIBTDeposit(),arl.get(0).isIBTWithdraw(), conn);						
					}
					
					// For Current Account , Cheque Number Case
					if (ret.getStatus()){
						//Checking Debit Case
						if (arl.get(i).getTransactionType()>=500){
							// Checking Product Type
							if (SharedLogic.getProductCodeToProductData(l_AccountData.getProduct()).hasCheque()){
								lByForceCheque = lByForceCheque1;
								arl.get(i).setReferenceNumber(lByForceCheque);
							}else{
								ret.setStatus(true);
							}
							if (ret.getStatus()){
								//HNNS For Check Can Withdraw and Post Link
								if(canWithdrawWithMultiCurrency(arl.get(i).getAccountNumber(),aAccGLTrList.get(i).getTrAmount(), conn)){
									ret.setStatus(true);
								}else{
									ret = com.nirvasoft.rp.core.ccbs.data.db.DBTransactionMgr.checkAndPostLinkWithMultiCurrency(arl.get(i), aAccGLTrList.get(i).getTrAmount(),aAccGLTrList.get(i).getTrCurCode(),aAccGLTrList.get(i).getTrRate(), conn);
									if(ret.getStatus()){
										m_AutoPrintTransRef = ret.getTransactionNumber();	
										m_AutoLinkTransRef.add((int)m_AutoPrintTransRef);
//										if (ret.getTransRef() != 0) m_AutoLinkTransRef.add(ret.getTransRef()); ynw_test
										if (lAccDAO.getAccount(arl.get(i).getAccountNumber(), conn)){
											l_AccountData = lAccDAO.getAccountsBean();
											arl.get(i).setPreviousBalance(l_AccountData.getCurrentBalance()*arl.get(i).getCurrencyRate());
											aAccGLTrList.get(i).setTrPrevBalance(l_AccountData.getCurrentBalance());													
										}
									}else{
										ret.setStatus(false);
										if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
											ret.setDescription("Insufficient Balance!!");
										}
									}
								}	
							}
						}else{
							ret.setStatus(true);
						}
					} 
						// remove
				}
				arl.get(i).setAmount(StrUtil.round2decimals(arl.get(i).getAmount(),RoundingMode.HALF_UP));
				arl.get(i).setComm(StrUtil.round2decimals(arl.get(i).getComm(),RoundingMode.HALF_UP));//SMM
			}
			
			if (ret.getStatus()){
				// Updating Balance
				ret = lAccDAO.prepareMultiTransfer(arl,aAccGLTrList,lTranRef,m_AutoLinkTransRef, conn);
			}

		} catch (ParserConfigurationException e) {			
			ret.setStatus(false);			
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} catch (SAXException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");		
			e.printStackTrace();
		} catch (IOException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} 
		return ret;
	}
	
	public static void prepareAccountGLTransactions(String tablename,String tmp_Transpreapre,  Connection conn) {
		try {
			String l_Query = "INSERT INTO "+tablename
					+" SELECT 0,account,productgl,cashinhandgl,'','','','MMK','/',currencyrate,amount*(currencyrate*1000)/1000,'','',0,0,"
					+ "currencyCode,'',currencyrate,amount,prevbalance,0,0,0,'',authid,'' from "+tmp_Transpreapre ;
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
		    pstmt.executeUpdate();
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public static DataResult postNew(String tmp_Transpreapre,String tmp_GlTranspreapre, Connection conn) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException {
		DataResult ret = new DataResult(); 
		
		int lUncommTransNo=0,lTranRef=0;
		lTranRef= nextValue();
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		AccountTransactionDAO lAccTranDAO=new AccountTransactionDAO();
		long m_AutoPrintTransRef=0;
		ArrayList<Integer> m_AutoLinkTransRef=new ArrayList<Integer>();
		try {
			// Adding Account Transaction
			ret = lAccTranDAO.addAccTransaction(tmp_Transpreapre,lTranRef, conn);
			if (ret.getStatus()){
				lUncommTransNo = (int) ret.getTransactionNumber();
				if (lAccTranDAO.updateTransRef(lTranRef,lUncommTransNo,conn)){
					ret.setStatus(true);
					ret.setTransactionNumber(lUncommTransNo);
				} else {
					ret.setStatus(false);
					if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
						ret.setDescription("Please Try Again!!");
					}
				}	
			}
			
			//SSH For Add in AccountGLTransaction
			if(ret.getStatus()) {				
				AccountGLTransactionDAO l_AccGLTrDAO = new AccountGLTransactionDAO();
				if( lAccTranDAO.getAccTransactionsTransRef(tmp_GlTranspreapre,(int)ret.getTransactionNumber(), conn)) {
					ret.setStatus(l_AccGLTrDAO.addAccountGLTransaction(tmp_GlTranspreapre, conn)); 
				}
				if(!ret.getStatus()) {
					if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
						ret.setDescription("Please Try Again!!");
					}
				}
			} 			

		} catch (ParserConfigurationException e) {			
			ret.setStatus(false);			
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} catch (SAXException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");		
			e.printStackTrace();
		} catch (IOException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			ret.setStatus(false);
			ret.setDescription("Please Try Again!!");			
			e.printStackTrace();
		} 
		return ret;
	}
	
	public static DataResult checkAndPostLinkWithMultiCurrency(TransferData pTransactionData,AccountData l_ParentAccData,String l_TableName,String l_TableName1, double l_Available , double pAmount, Connection pConn)throws SQLException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException{
		DataResult l_DataResult = new DataResult();
		AccountDao lAccDAO=new AccountDao();
		ArrayList<AccountData> l_ChildAccount = new ArrayList<AccountData>();
		double l_AmountNeeded = 0;
		double com=0;
		AccountData l_AccData = new AccountData();
		
		//atds
		POCommissionRateDAO l_ComRateDAO = new POCommissionRateDAO();
		POCommissionRateData l_ComRateData = new POCommissionRateData();
		double l_CommAmt = 0;  
		boolean isLast = false;
		int firstTransref = 0;
		int secTransref = 0;
		String l_AmtNeededDiv =  SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getT2();//LogicMgr.getSystemSettingT12T2("ALK");
		
		int l_isUseComm = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN2();//LogicMgr.getSystemSettingT12N2("ALK");				
		int l_useALK = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN1();//LogicMgr.getSystemSettingT12N1("ALK");
		int l_N6 = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN6();//SharedLogic.getSystemSettingT12N6("ALK");
		int l_N7 = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN7();//SharedLogic.getSystemSettingT12N7("ALK");
		String l_T4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getT4();// SharedLogic.getSystemSettingT12T4("ALK");
		
		if(l_useALK == 1) {
			l_DataResult.setStatus(true);
		}else {
			l_DataResult.setStatus(false);
			l_DataResult.setDescription("Do not use Account Link!");
		}
		String fkey="";
		ProductData fromproduct = new ProductData();
		if(l_DataResult.getStatus()) {
			if(lAccDAO.getAutoLinkAccount(l_ParentAccData.getAccountNumber() , pConn)) {
				TransactionMgr t_mgr = new TransactionMgr();
				
				l_ChildAccount = lAccDAO.getAccountsLinkBean();
				if(l_ChildAccount.size()>0){
					l_AccData = l_ChildAccount.get(0);
					
					String tmp_LinkTranspreapre = "Tmp_LinkTransprepare" +l_ParentAccData.getAccountNumber()+""+l_AccData.getAccountNumber()+""+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();
					t_mgr.createTmpTableForTransaction(tmp_LinkTranspreapre, pConn);
					
					// ALK => N2 => 1=Comm, 0=not 
					if(l_isUseComm == 1) {
						//ALK => N6 => 0=Head Office ,1=according to branch 
						if(l_N6==0){
							l_ComRateData = l_ComRateDAO.getPOCommissionRateForALink("03", l_ParentAccData.getCurrencyCode(), 0,l_AccData.getProduct(), pConn);
						}else{
							l_ComRateData = l_ComRateDAO.getPOCommissionRate("03", l_ParentAccData.getCurrencyCode(), l_ParentAccData.getBranchCode(), 0,l_AccData.getProduct(), pConn);
						}
						if(l_ComRateData.getCom()!=0)
							l_CommAmt = DBFifthDayPostingMgr.getCommissionRate(pAmount, l_ComRateData);
						else
							l_CommAmt = 0.00;
						
					}
					
					if(l_AmtNeededDiv.equals(""))
						l_AmountNeeded = pAmount - l_Available;
					else{
						double transAmt = pAmount-l_Available;
						double getAmt = 0.0;
						double l_AmtNeededDivDou = Double.parseDouble(l_AmtNeededDiv);
						if(transAmt%l_AmtNeededDivDou!=0){
							getAmt = l_AmtNeededDivDou-transAmt%l_AmtNeededDivDou;
							l_AmountNeeded = getAmt+transAmt;
						} else
							l_AmountNeeded = transAmt;
					}
					
					if (SharedLogic.getSystemData().getAccMinSetting() == 1){
							fkey = l_AccData.getProduct()+""+l_AccData.getType();
					}else {
							fkey = l_AccData.getProduct();
					}
					fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
					double fAvailable = lAccDAO.getAvaliableBalance(l_AccData,fromproduct.getMinOpeningBalance(),0, pConn);
					
						if(fAvailable>=l_AmountNeeded+l_CommAmt){
							if(l_ChildAccount.size() == 1) isLast=true;
							//TransactionData pTransactionData, AccountData pParentAccount, AccountData pChildAccount,String tmp_Transpreapre, double pAmount, String pCurCode, double pCurRate, double pCommAmt, Connection l_Conn
							l_DataResult = doTransferLinkWithMultiCurrency(pTransactionData,l_ParentAccData,l_AccData, tmp_LinkTranspreapre, l_AmountNeeded,l_CommAmt,isLast, pConn);
							if(l_DataResult.getStatus()) {
								l_DataResult = postTransaction( tmp_LinkTranspreapre, l_TableName, l_TableName1,  pConn);
								if(l_DataResult.getStatus()) {}
//									firstTransref = l_DataResult.getTransRef();  -- ynw test
							}
						}else{
							if(l_ChildAccount.size() > 1){
								if((l_N7==0 && SharedLogic.isCall(l_AccData.getProduct())) || (l_N7==1 && SharedLogic.isSavings(l_AccData.getProduct())) || l_N7==2)
									com = l_CommAmt;
								if(fAvailable> com){
									l_DataResult = doTransferLinkWithMultiCurrency(pTransactionData,l_ParentAccData,l_AccData, tmp_LinkTranspreapre, fAvailable,com,isLast, pConn);
									if(l_DataResult.getStatus()) {
										l_AmountNeeded = l_AmountNeeded-(fAvailable-com);
										l_DataResult = postTransaction( tmp_LinkTranspreapre, l_TableName, l_TableName1,  pConn);
										if(l_DataResult.getStatus()) {}
				//							firstTransref = l_DataResult.getTransRef();  ynw test
									}
									
								}
								if(l_AmountNeeded != 0){
									l_AccData = l_ChildAccount.get(1);
									
									if(SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN3()!=0) {
										if(l_T4.equals("1")){
											if(l_isUseComm == 1) {
												//ALK => N6 => 0=Head Office ,1=according to branch 
												if(l_N6==0){
													l_ComRateData = l_ComRateDAO.getPOCommissionRateForALink("03", l_ParentAccData.getCurrencyCode(), 0,l_AccData.getProduct(), pConn);
												}else{
													l_ComRateData = l_ComRateDAO.getPOCommissionRate("03", l_ParentAccData.getCurrencyCode(), l_ParentAccData.getBranchCode(), 0,l_AccData.getProduct(), pConn);
												}
												if(l_ComRateData.getCom()!=0)
													l_CommAmt = DBFifthDayPostingMgr.getCommissionRate(pAmount, l_ComRateData);
												else
													l_CommAmt = 0.00;
											}
										}else {
											l_CommAmt = 0.00;
										}
									}
									
									double sAvailable = lAccDAO.getAvaliableBalance(l_AccData,fromproduct.getMinOpeningBalance(),0, pConn);
									double amt = 0.00;
									if((l_N7==0 && SharedLogic.isCall(l_AccData.getProduct())) || (l_N7==1 && SharedLogic.isSavings(l_AccData.getProduct())) || l_N7==2)
										com= l_CommAmt;
									else
										com = 0;
									
									if(sAvailable>=l_AmountNeeded+com) {
										isLast=true;
										tmp_LinkTranspreapre = "Tmp_LinkTransprepare" +l_ParentAccData.getAccountNumber()+""+l_AccData.getAccountNumber()+""+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();
										t_mgr.createTmpTableForTransaction(tmp_LinkTranspreapre, pConn);
										l_DataResult = doTransferLinkWithMultiCurrency(pTransactionData,l_ParentAccData,l_AccData, tmp_LinkTranspreapre, l_AmountNeeded,com,isLast, pConn);
										if(l_DataResult.getStatus()) {
											l_DataResult = postTransaction( tmp_LinkTranspreapre, l_TableName, l_TableName1,  pConn);
											if(l_DataResult.getStatus()) {}
//												secTransref = l_DataResult.getTransRef();
										}
									}else{
										l_DataResult.setStatus(false);
										amt = l_AmountNeeded+com-sAvailable;
										l_DataResult.setDescription(l_ParentAccData.getAccountNumber()+" is insufficient "+amt);
									}
								}	
							}else {
								l_DataResult.setStatus(false);
								Double amt = l_AmountNeeded+l_CommAmt-fAvailable;
								l_DataResult.setDescription(l_ParentAccData.getAccountNumber()+" is insufficient "+StrUtil.formatNumberwithComma(amt));
							}
						}	
						t_mgr.droptable(tmp_LinkTranspreapre,pConn);
				}
				
			}else {
				l_DataResult.setStatus(false);
	            l_DataResult.setDescription(l_ParentAccData.getAccountNumber() + " is insufficient balance " + StrUtil.formatNumberwithComma((pTransactionData.getAmount() - l_Available)));
			}
			if(l_DataResult.getStatus()) {
				l_DataResult.setAutoLinkTransRef(firstTransref);
				l_DataResult.setSecAutoLinkTransRef(secTransref);
			}
		}
		
		
		
		return l_DataResult;		
	}
	
	public static DataResult checkAndPostLinkWithMultiCurrencyUpdate(TransferData pTransactionData,AccountData l_ParentAccData, double l_Available , double pAmount,String l_TableName,String l_TableName1, Connection pConn)throws SQLException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException{
		DataResult l_DataResult = new DataResult();
		TransferDataResult transferDataResult = new TransferDataResult();
		AccountDao lAccDAO=new AccountDao();
		ArrayList<AccountData> l_ChildAccount = new ArrayList<AccountData>();
		double l_AmountNeeded = 0;
		double com=0;
		AccountData l_AccData = new AccountData();
		
		//atds
		POCommissionRateDAO l_ComRateDAO = new POCommissionRateDAO();
		POCommissionRateData l_ComRateData = new POCommissionRateData();
		double l_CommAmt = 0;  
		boolean isLast = false;
		int firstTransref = 0 , secTransref = 0;
		String l_AmtNeededDiv =  SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getT2();//LogicMgr.getSystemSettingT12T2("ALK");
		
		int l_isUseComm = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN2();//LogicMgr.getSystemSettingT12N2("ALK");				
		int l_useALK = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN1();//LogicMgr.getSystemSettingT12N1("ALK");
		int l_N6 = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN6();//SharedLogic.getSystemSettingT12N6("ALK");
		int l_N7 = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN7();//SharedLogic.getSystemSettingT12N7("ALK");
		String l_T4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getT4();// SharedLogic.getSystemSettingT12T4("ALK");
		
		if(l_useALK == 1) {
			l_DataResult.setStatus(true);
		}else {
			l_DataResult.setStatus(false);
			l_DataResult.setDescription("Do not use Account Link!");
		}
		String fkey="";
		ProductData fromproduct = new ProductData();
		if(l_DataResult.getStatus()) {
			if(lAccDAO.getAutoLinkAccount(l_ParentAccData.getAccountNumber() , pConn)) {
				TransactionMgr t_mgr = new TransactionMgr();
				
				l_ChildAccount = lAccDAO.getAccountsLinkBean();
				if(l_ChildAccount.size()>0){
					l_AccData = l_ChildAccount.get(0);
					
					// ALK => N2 => 1=Comm, 0=not 
					if(l_isUseComm == 1) {
						//ALK => N6 => 0=Head Office ,1=according to branch 
						if(l_N6==0){
							l_ComRateData = l_ComRateDAO.getPOCommissionRateForALink("03", l_ParentAccData.getCurrencyCode(), 0,l_AccData.getProduct(), pConn);
						}else{
							l_ComRateData = l_ComRateDAO.getPOCommissionRate("03", l_ParentAccData.getCurrencyCode(), l_ParentAccData.getBranchCode(), 0,l_AccData.getProduct(), pConn);
						}
						if(l_ComRateData.getCom()!=0)
							l_CommAmt = DBFifthDayPostingMgr.getCommissionRate(pAmount, l_ComRateData);
						else
							l_CommAmt = 0.00;
						
					}
					
					if(l_AmtNeededDiv.equals(""))
						l_AmountNeeded = pAmount - l_Available;
					else{
						double transAmt = pAmount-l_Available;
						double getAmt = 0.0;
						double l_AmtNeededDivDou = Double.parseDouble(l_AmtNeededDiv);
						if(transAmt%l_AmtNeededDivDou!=0){
							getAmt = l_AmtNeededDivDou-transAmt%l_AmtNeededDivDou;
							l_AmountNeeded = getAmt+transAmt;
						} else
							l_AmountNeeded = transAmt;
					}
					
					if (SharedLogic.getSystemData().getAccMinSetting() == 1){
							fkey = l_AccData.getProduct()+""+l_AccData.getType();
					}else {
							fkey = l_AccData.getProduct();
					}
					fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
					double fAvailable = lAccDAO.getAvaliableBalance(l_AccData,fromproduct.getMinOpeningBalance(),0, pConn);
					
						if(fAvailable>=l_AmountNeeded+l_CommAmt){
							if(l_ChildAccount.size() == 1) isLast=true;
							//TransactionData pTransactionData, AccountData pParentAccount, AccountData pChildAccount,String tmp_Transpreapre, double pAmount, String pCurCode, double pCurRate, double pCommAmt, Connection l_Conn
							
							transferDataResult = doTransferLinkWithMultiCurrencyUpdate(pTransactionData,l_ParentAccData,l_AccData,  l_AmountNeeded,l_CommAmt,isLast, pConn);
							
							if(transferDataResult.isStatus()) {
								l_DataResult = postTransactionUpdate( transferDataResult, l_TableName, l_TableName1,  pConn);
								if(l_DataResult.getStatus())
									firstTransref = l_DataResult.getTransRef();
							}
						}else{
							if(l_ChildAccount.size() > 1){
								if((l_N7==0 && SharedLogic.isCall(l_AccData.getProduct())) || (l_N7==1 && SharedLogic.isSavings(l_AccData.getProduct())) || l_N7==2)
									com = l_CommAmt;
								if(fAvailable> com){
									transferDataResult = doTransferLinkWithMultiCurrencyUpdate(pTransactionData,l_ParentAccData,l_AccData, fAvailable,com,isLast, pConn);
									if(l_DataResult.getStatus()) {
										l_AmountNeeded = l_AmountNeeded-(fAvailable-com);
										l_DataResult = postTransactionUpdate( transferDataResult, l_TableName, l_TableName1,  pConn);
										if(l_DataResult.getStatus())
											firstTransref = l_DataResult.getTransRef();
									}
									
								}
								if(l_AmountNeeded != 0){
									l_AccData = l_ChildAccount.get(1);
									
									if(SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN3()!=0) {
										if(l_T4.equals("1")){
											if(l_isUseComm == 1) {
												//ALK => N6 => 0=Head Office ,1=according to branch 
												if(l_N6==0){
													l_ComRateData = l_ComRateDAO.getPOCommissionRateForALink("03", l_ParentAccData.getCurrencyCode(), 0,l_AccData.getProduct(), pConn);
												}else{
													l_ComRateData = l_ComRateDAO.getPOCommissionRate("03", l_ParentAccData.getCurrencyCode(), l_ParentAccData.getBranchCode(), 0,l_AccData.getProduct(), pConn);
												}
												if(l_ComRateData.getCom()!=0)
													l_CommAmt = DBFifthDayPostingMgr.getCommissionRate(pAmount, l_ComRateData);
												else
													l_CommAmt = 0.00;
											}
										}else {
											l_CommAmt = 0.00;
										}
									}
									
									double sAvailable = lAccDAO.getAvaliableBalance(l_AccData,fromproduct.getMinOpeningBalance(),0, pConn);
									double amt = 0.00;
									if((l_N7==0 && SharedLogic.isCall(l_AccData.getProduct())) || (l_N7==1 && SharedLogic.isSavings(l_AccData.getProduct())) || l_N7==2)
										com= l_CommAmt;
									else
										com = 0;
									
									if(sAvailable>=l_AmountNeeded+com) {
										isLast=true;
										
										transferDataResult = doTransferLinkWithMultiCurrencyUpdate(pTransactionData,l_ParentAccData,l_AccData, l_AmountNeeded,com,isLast, pConn);
										if(l_DataResult.getStatus()) {
											l_DataResult = postTransactionUpdate( transferDataResult, l_TableName, l_TableName1,  pConn);
											if(l_DataResult.getStatus())
												secTransref = l_DataResult.getTransRef();
										}
									}else{
										l_DataResult.setStatus(false);
										amt = l_AmountNeeded+com-sAvailable;
										l_DataResult.setDescription(l_ParentAccData.getAccountNumber()+" is insufficient "+amt);
									}
								}	
							}else {
								l_DataResult.setStatus(false);
								Double amt = l_AmountNeeded+l_CommAmt-fAvailable;
								l_DataResult.setDescription(l_ParentAccData.getAccountNumber()+" is insufficient "+StrUtil.formatNumberwithComma(amt));
							}
						}	
						
				}
				
			}else {
				l_DataResult.setStatus(false);
	            l_DataResult.setDescription(l_ParentAccData.getAccountNumber() + " is insufficient balance " + StrUtil.formatNumberwithComma((pTransactionData.getAmount() - l_Available)));
			}
			
			if (firstTransref != 0 && secTransref != 0) {
				l_DataResult.setTransactionNumber(firstTransref);
				l_DataResult.setTransRef(secTransref);
			} else if (firstTransref != 0 && secTransref == 0) {
				l_DataResult.setTransactionNumber(firstTransref);
				l_DataResult.setTransRef(0);
			} else if (secTransref !=0 && firstTransref == 0) {
				l_DataResult.setTransRef(0);
			}
			
		}
		
		
		
		return l_DataResult;		
	}
	
	public static DataResult doTransferLinkWithMultiCurrency(TransferData pTransactionData, AccountData pParentAccount, AccountData pChildAccount,String tmp_Transpreapre, double pAmount, double pCommAmt,boolean isLast, Connection l_Conn) throws SQLException {
		DataResult l_DataResult = new DataResult();
		boolean isSameBranch = false;
		String fkey = "";
		ProductData fromproduct = new ProductData();
		String pByForceCheque = "",l_Query="";
		PreparedStatement pstmt = null;
		
		String l_IBTGLFrom = "", l_IBTGLTo = "", l_IBTGLComm="", l_funID ="";
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		l_DataResult.setStatus(true);
		if (pParentAccount.getBranchCode().equals(pChildAccount.getBranchCode()))
		    	isSameBranch = true; 
		
		if (SharedLogic.getSystemData().getAccMinSetting() == 1){
			fkey = pParentAccount.getProduct()+""+pParentAccount.getType();
		}else {
			fkey = pParentAccount.getProduct();
		}
		String pCurCode = pParentAccount.getCurrencyCode();
		String fkey1 = "smTransfer"+"_"+pParentAccount.getCurrencyCode()+"_"+pParentAccount.getBranchCode();
		double pCurRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
		fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
		pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN3()==0) {
			if(isLast )	pAmount = pAmount+pCommAmt;
		} else {
			if(!isLast )pAmount = pAmount-pCommAmt;
		}
		
			
			int index = 1;
			// Dr From Customer
			l_Query = "INSERT INTO " + tmp_Transpreapre  + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
					+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
			pstmt = l_Conn.prepareStatement(l_Query);
			pstmt.setString(index++,  pTransactionData.getUserID());
			pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			pstmt.setDouble(index++, pAmount);
			pstmt.setString(index++, pChildAccount.getAccountNumber());
			pstmt.setString(index++, pChildAccount.getBranchCode());
			pstmt.setString(index++, pCurCode);
			pstmt.setString(index++, pTransactionData.getTransactionTime());
			pstmt.setString(index++, pTransactionData.getTransactionDate());
			pstmt.setString(index++, pTransactionData.getEffectiveDate());
			pstmt.setString(index++, pTransactionData.getReferenceNo());	
			pstmt.setString(index++, pTransactionData.getRemark());
			pstmt.setDouble(index++, pCurRate);
			pstmt.setDouble(index++, pAmount * pCurRate);
			pstmt.setString(index++,String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) +   ", " + pCurCode);
			pstmt.setInt(index++, SharedLogic.getTransType(TransCode.AccountLinkDr,  fkey));
			pstmt.setString(index++,fromproduct.getCashInHandGL());
			pstmt.setString(index++,fromproduct.getProductGL());
		    pstmt.setString(index++,fromproduct.hasCheque()?pByForceCheque:"");
		    pstmt.setInt(index++,1);
		    pstmt.setDouble(index++, pChildAccount.getCurrentBalance());
		    pstmt.setString(index++, pTransactionData.getTransdescription());
		    pstmt.executeUpdate();	
		    
		    if (!isSameBranch) {
		    	// different branch
		        l_funID = getFunctionID(pTransactionData.getCurrencyCode(),pParentAccount.getBranchCode(),Enum.FunctionID.IBT,pParentAccount.getProduct());
			      
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
				if(l_RefData != null) l_IBTGLFrom = l_RefData.getGLCode();
				if(l_IBTGLFrom.equals("")) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription("Invalid GL Account!");
					return l_DataResult;
				}
			   // Cr IBT GL
				    l_Query = "INSERT INTO " + tmp_Transpreapre  + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
				    pstmt = l_Conn.prepareStatement(l_Query);
				    index = 1;
				    pstmt.setString(index++,  pTransactionData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, pAmount * (pCurRate*1000)/1000);
				    pstmt.setString(index++, l_IBTGLFrom);
					pstmt.setString(index++, pParentAccount.getBranchCode());
					pstmt.setString(index++, pTransactionData.getCurrencyCode() );
					pstmt.setString(index++, pTransactionData.getTransactionTime());
					pstmt.setString(index++, pTransactionData.getTransactionDate());
					pstmt.setString(index++, pTransactionData.getEffectiveDate());
					pstmt.setString(index++, pTransactionData.getReferenceNo());	
					pstmt.setString(index++, pTransactionData.getRemark());
					pstmt.setDouble(index++, pCurRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pAmount * pCurRate));
					pstmt.setString(index++,String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, fkey)) + ", " + pCurCode);
					pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkCr, fkey));
					pstmt.setString(index++,fromproduct.getCashInHandGL());
					pstmt.setString(index++,"");
					pstmt.setString(index++,"");
					pstmt.setInt(index++,1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransactionData.getTransdescription());
					pstmt.executeUpdate();	
					
				l_funID = getFunctionID(pTransactionData.getCurrencyCode(),pChildAccount.getBranchCode(),Enum.FunctionID.IBT,pParentAccount.getProduct());
			    
			      l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
			      if(l_RefData != null) l_IBTGLTo = l_RefData.getGLCode();
			      
			      if(l_IBTGLTo.equals("")) {
						l_DataResult.setStatus(false);
						l_DataResult.setDescription("Invalid GL Account!");
						return l_DataResult;
			      }
			      
			   // Dr IBT GL
					l_Query = "INSERT INTO " + tmp_Transpreapre  + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
					index=1;
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++,  pTransactionData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, pAmount * (pCurRate*1000)/1000);
					pstmt.setString(index++, l_IBTGLTo);
					pstmt.setString(index++, pParentAccount.getBranchCode());
					pstmt.setString(index++, pCurCode);
					pstmt.setString(index++, pTransactionData.getTransactionTime());
					pstmt.setString(index++, pTransactionData.getTransactionDate());
					pstmt.setString(index++, pTransactionData.getEffectiveDate());
					pstmt.setString(index++, pTransactionData.getReferenceNo());	
					pstmt.setString(index++, pTransactionData.getRemark());
					pstmt.setDouble(index++, pCurRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pAmount * pCurRate));
					pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) + ", " + pCurCode);
					pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++,"");
				    pstmt.setString(index++,"");
				    pstmt.setInt(index++,1);
				    pstmt.setDouble(index++, 0);
				    pstmt.setString(index++, pTransactionData.getTransdescription());
				    pstmt.executeUpdate();	
			      
			    } 
		    
				// Cr To Customer
		    l_Query = "INSERT INTO " + tmp_Transpreapre  + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
		    pstmt = l_Conn.prepareStatement(l_Query);
		    index = 1;
		    pstmt.setString(index++, pTransactionData.getUserID());
			pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			pstmt.setDouble(index++, pAmount * (pCurRate*1000)/1000);
		    pstmt.setString(index++, pParentAccount.getAccountNumber());
			pstmt.setString(index++, pParentAccount.getBranchCode());
			pstmt.setString(index++, pCurCode );
			pstmt.setString(index++, pTransactionData.getTransactionTime());
			pstmt.setString(index++, pTransactionData.getTransactionDate());
			pstmt.setString(index++, pTransactionData.getEffectiveDate());
			pstmt.setString(index++, pTransactionData.getReferenceNo());	
			pstmt.setString(index++, pTransactionData.getRemark());
			pstmt.setDouble(index++, pCurRate);
			pstmt.setDouble(index++, pAmount * pCurRate);
			pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, fkey))+  ", " + pCurCode );
			pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkCr, fkey));
			pstmt.setString(index++,fromproduct.getCashInHandGL());
			pstmt.setString(index++,fromproduct.getProductGL());
			pstmt.setString(index++,"");
			pstmt.setInt(index++,1);
			pstmt.setDouble(index++, pParentAccount.getCurrentBalance());
			pstmt.setString(index++, pTransactionData.getTransdescription());
			pstmt.executeUpdate();	
	
	    
	    int l_isUseComm = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN2();// SharedLogic.getSystemSettingT12N2("ALK");
	    if (l_isUseComm == 1 && pCommAmt > 0.0D) {
	   // Dr IBT GL
			l_Query = "INSERT INTO " + tmp_Transpreapre  + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
					+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
			index=1;
			pstmt = l_Conn.prepareStatement(l_Query);
			pstmt.setString(index++,  pTransactionData.getUserID());
			pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			pstmt.setDouble(index++, pCommAmt * (pCurRate*1000)/1000);
			pstmt.setString(index++, pParentAccount.getAccountNumber());
			pstmt.setString(index++, pChildAccount.getBranchCode());
			pstmt.setString(index++, pCurCode);
			pstmt.setString(index++, pTransactionData.getTransactionTime());
			pstmt.setString(index++, pTransactionData.getTransactionDate());
			pstmt.setString(index++, pTransactionData.getEffectiveDate());
			pstmt.setString(index++, pTransactionData.getReferenceNo());	
			pstmt.setString(index++, pTransactionData.getRemark());
			pstmt.setDouble(index++, pCurRate);
			pstmt.setDouble(index++, StrUtil.round2decimals(pCommAmt * pCurRate));
			pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) + ", Commission, " + pCurCode);
			pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
			pstmt.setString(index++, fromproduct.getCashInHandGL());
			pstmt.setString(index++,"");
		    pstmt.setString(index++, fromproduct.hasCheque() ?pByForceCheque:"");
		    pstmt.setInt(index++,1);
		    pstmt.setDouble(index++, 0);
		    pstmt.setString(index++, pTransactionData.getTransdescription());
		    pstmt.executeUpdate();	
	      
	      l_funID = getFunctionID(pTransactionData.getCurrencyCode(),pParentAccount.getBranchCode(),Enum.FunctionID.ALKCOM,"");
	      l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
	      if(l_RefData != null) l_IBTGLComm = l_RefData.getGLCode();
	      if(l_IBTGLComm.equals("")) {
				l_DataResult.setStatus(false);
				l_DataResult.setDescription("Invalid GL Account!");
				return l_DataResult;
	      }
	  	// Cr To Customer
		    l_Query = "INSERT INTO " + tmp_Transpreapre  + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
		    pstmt = l_Conn.prepareStatement(l_Query);
		    index = 1;
		    pstmt.setString(index++, pTransactionData.getUserID());
			pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			pstmt.setDouble(index++, pCommAmt * (pCurRate*1000)/1000);
		    pstmt.setString(index++, l_IBTGLComm);
			pstmt.setString(index++, pParentAccount.getBranchCode());
			pstmt.setString(index++, pCurCode );
			pstmt.setString(index++, pTransactionData.getTransactionTime());
			pstmt.setString(index++, pTransactionData.getTransactionDate());
			pstmt.setString(index++, pTransactionData.getEffectiveDate());
			pstmt.setString(index++, pTransactionData.getReferenceNo());	
			pstmt.setString(index++, pTransactionData.getRemark());
			pstmt.setDouble(index++, pCurRate);
			pstmt.setDouble(index++, pCommAmt * pCurRate);
			pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, ""))+  ", Commission " + pCurCode );
			pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkCr, ""));
			pstmt.setString(index++,fromproduct.getCashInHandGL());
			pstmt.setString(index++,fromproduct.getProductGL());
			pstmt.setString(index++,"");
			pstmt.setInt(index++,1);
			pstmt.setDouble(index++, pParentAccount.getCurrentBalance());
			pstmt.setString(index++, pTransactionData.getTransdescription());
			pstmt.executeUpdate();	
	    } 
	    
	    return l_DataResult;
	  }
	
		public static TransferDataResult doTransferLinkWithMultiCurrencyUpdate(TransferData pTransactionData,
				AccountData pParentAccount, AccountData pChildAccount,  double pAmount,
				double pCommAmt, boolean isLast, Connection l_Conn) throws SQLException {
			TransferDataResult transferDataResult = new TransferDataResult();
			boolean isSameBranch = false;
			TransferData prepareTransferData = new TransferData();
			String fkey = "";
			ProductData fromproduct = new ProductData();
			String pByForceCheque = "";
			String l_IBTGLFrom = "", l_IBTGLTo = "", l_IBTGLComm = "", l_funID = "";
			ReferenceAccountsData l_RefData = new ReferenceAccountsData();
	
			if (pParentAccount.getBranchCode().equals(pChildAccount.getBranchCode()))
				isSameBranch = true;

			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = pParentAccount.getProduct() + "" + pParentAccount.getType();
			} else {
				fkey = pParentAccount.getProduct();
			}
			String pCurCode = pParentAccount.getCurrencyCode();
			String fkey1 = "smTransfer" + "_" + pParentAccount.getCurrencyCode() + "_" + pParentAccount.getBranchCode();
			double pCurRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			if (SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN3() == 0) {
				if (isLast)
					pAmount = pAmount + pCommAmt;
			} else {
				if (!isLast)
					pAmount = pAmount - pCommAmt;
			}

			ArrayList<TransferData> transferlist = new ArrayList<TransferData>();
			prepareTransferData = new TransferData();
			// Dr From Customer
			prepareTransferData.setUserID(pTransactionData.getUserID());
			prepareTransferData.setAmount(pAmount);
			prepareTransferData.setFromAccNumber(pChildAccount.getAccountNumber());
			prepareTransferData.setFromBranchCode(pChildAccount.getBranchCode());
			prepareTransferData.setFromCCY(pCurCode);
			prepareTransferData.setCurrencyCode(pCurCode);
			prepareTransferData.setpTransactionTime(pTransactionData.getTransactionTime());
			prepareTransferData.setTransactionDate(pTransactionData.getTransactionDate());
			prepareTransferData.setEffectiveDate(pTransactionData.getEffectiveDate());
			prepareTransferData.setReferenceNo(pTransactionData.getReferenceNo());
			prepareTransferData.setRemark(pTransactionData.getRemark());
			prepareTransferData.setBasecurrate(pCurRate);
			prepareTransferData.setBaseAmount(pAmount * pCurRate);
			prepareTransferData.setPostDescription(
					String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) + ", " + pCurCode);
			prepareTransferData.setIbtTransType(SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
			prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
			prepareTransferData.setProductGL(fromproduct.getProductGL());
			prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
			prepareTransferData.setPrevBalance(pChildAccount.getCurrentBalance());
			prepareTransferData.setTransdescription(pTransactionData.getTransdescription());
			transferlist.add(prepareTransferData);

			if (!isSameBranch) {
				// different branch
				l_funID = getFunctionID(pTransactionData.getCurrencyCode(), pParentAccount.getBranchCode(),
						Enum.FunctionID.IBT, pParentAccount.getProduct());

				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
				if (l_RefData != null)
					l_IBTGLFrom = l_RefData.getGLCode();
				if (l_IBTGLFrom.equals("")) {
					transferDataResult.setStatus(false);
					transferDataResult.setDescription("Invalid GL Account!");
					return transferDataResult;
				}
				// Cr IBT GL
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransactionData.getUserID());
				prepareTransferData.setAmount(pAmount * (pCurRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(l_IBTGLFrom);
				prepareTransferData.setFromBranchCode(pParentAccount.getBranchCode());
				prepareTransferData.setFromCCY(pTransactionData.getCurrencyCode());
				prepareTransferData.setCurrencyCode(pTransactionData.getCurrencyCode());
				prepareTransferData.setpTransactionTime(pTransactionData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransactionData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransactionData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransactionData.getReferenceNo());
				prepareTransferData.setRemark(pTransactionData.getRemark());
				prepareTransferData.setBasecurrate(pCurRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pAmount * pCurRate));
				prepareTransferData.setPostDescription(
						String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, fkey)) + ", "
								+ pCurCode);
				prepareTransferData.setIbtTransType(SharedLogic.getTransType(TransCode.AccountLinkCr, fkey));
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL("");
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(0);
				prepareTransferData.setTransdescription(pTransactionData.getTransdescription());
				transferlist.add(prepareTransferData);

				l_funID = getFunctionID(pTransactionData.getCurrencyCode(), pChildAccount.getBranchCode(),
						Enum.FunctionID.IBT, pParentAccount.getProduct());

				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
				if (l_RefData != null)
					l_IBTGLTo = l_RefData.getGLCode();

				if (l_IBTGLTo.equals("")) {
					transferDataResult.setStatus(false);
					transferDataResult.setDescription("Invalid GL Account!");
					return transferDataResult;
				}

				// Dr IBT GL
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransactionData.getUserID());
				prepareTransferData.setAmount(pAmount * (pCurRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(l_IBTGLTo);
				prepareTransferData.setFromBranchCode(pParentAccount.getBranchCode());
				prepareTransferData.setFromCCY(pCurCode);
				prepareTransferData.setCurrencyCode(pCurCode);
				prepareTransferData.setpTransactionTime(pTransactionData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransactionData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransactionData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransactionData.getReferenceNo());
				prepareTransferData.setRemark(pTransactionData.getRemark());
				prepareTransferData.setBasecurrate(pCurRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pAmount * pCurRate));
				prepareTransferData.setPostDescription(
						String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) + ", "
								+ pCurCode);
				prepareTransferData.setIbtTransType(SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL("");
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(0);
				prepareTransferData.setTransdescription(pTransactionData.getTransdescription());
				transferlist.add(prepareTransferData);
			}

			// Cr To Customer
			prepareTransferData = new TransferData();
			prepareTransferData.setUserID(pTransactionData.getUserID());
			prepareTransferData.setAmount(pAmount * (pCurRate * 1000) / 1000);
			prepareTransferData.setFromAccNumber(pParentAccount.getAccountNumber());
			prepareTransferData.setFromBranchCode(pParentAccount.getBranchCode());
			prepareTransferData.setFromCCY(pCurCode);
			prepareTransferData.setCurrencyCode(pCurCode);
			prepareTransferData.setpTransactionTime(pTransactionData.getTransactionTime());
			prepareTransferData.setTransactionDate(pTransactionData.getTransactionDate());
			prepareTransferData.setEffectiveDate(pTransactionData.getEffectiveDate());
			prepareTransferData.setReferenceNo(pTransactionData.getReferenceNo());
			prepareTransferData.setRemark(pTransactionData.getRemark());
			prepareTransferData.setBasecurrate(pCurRate);
			prepareTransferData.setBaseAmount(StrUtil.round2decimals(pAmount * pCurRate));
			prepareTransferData.setPostDescription(
					String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, fkey)) + ", " + pCurCode);
			prepareTransferData.setIbtTransType(SharedLogic.getTransType(TransCode.AccountLinkCr, fkey));
			prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
			prepareTransferData.setProductGL(fromproduct.getProductGL());
			prepareTransferData.setpByForceCheque("");
			prepareTransferData.setPrevBalance(0);
			prepareTransferData.setPrevBalance(pParentAccount.getCurrentBalance());
			prepareTransferData.setTransdescription(pTransactionData.getTransdescription());
			transferlist.add(prepareTransferData);

			int l_isUseComm = SharedLogic.getSystemData().getpSystemSettingDataList().get("ALK").getN2();// SharedLogic.getSystemSettingT12N2("ALK");
			if (l_isUseComm == 1 && pCommAmt > 0.0D) {
				// Dr IBT GL
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransactionData.getUserID());
				prepareTransferData.setAmount(pCommAmt * (pCurRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(pParentAccount.getAccountNumber());
				prepareTransferData.setFromBranchCode(pChildAccount.getBranchCode());
				prepareTransferData.setFromCCY(pCurCode);
				prepareTransferData.setCurrencyCode(pCurCode);
				prepareTransferData.setpTransactionTime(pTransactionData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransactionData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransactionData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransactionData.getReferenceNo());
				prepareTransferData.setRemark(pTransactionData.getRemark());
				prepareTransferData.setBasecurrate(pCurRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pCommAmt * pCurRate));
				prepareTransferData.setPostDescription(
						String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey))
								+ ", Commission, " + pCurCode);
				prepareTransferData.setIbtTransType(SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL("");
				prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
				prepareTransferData.setPrevBalance(0);
				prepareTransferData.setPrevBalance(0);
				prepareTransferData.setTransdescription(pTransactionData.getTransdescription());
				transferlist.add(prepareTransferData);

				l_funID = getFunctionID(pTransactionData.getCurrencyCode(), pParentAccount.getBranchCode(),
						Enum.FunctionID.ALKCOM, "");
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
				if (l_RefData != null)
					l_IBTGLComm = l_RefData.getGLCode();
				if (l_IBTGLComm.equals("")) {
					transferDataResult.setStatus(false);
					transferDataResult.setDescription("Invalid GL Account!");
					return transferDataResult;
				}
				// Cr To Customer
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransactionData.getUserID());
				prepareTransferData.setAmount(pCommAmt * (pCurRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(l_IBTGLComm);
				prepareTransferData.setFromBranchCode(pParentAccount.getBranchCode());
				prepareTransferData.setFromCCY(pCurCode);
				prepareTransferData.setCurrencyCode(pCurCode);
				prepareTransferData.setpTransactionTime(pTransactionData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransactionData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransactionData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransactionData.getReferenceNo());
				prepareTransferData.setRemark(pTransactionData.getRemark());
				prepareTransferData.setBasecurrate(pCurRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pCommAmt * pCurRate));
				prepareTransferData.setPostDescription(
						String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey))
								+ ", Commission, " + pCurCode);
				prepareTransferData.setIbtTransType(SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL(fromproduct.getProductGL());
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(0);
				prepareTransferData.setPrevBalance(pParentAccount.getCurrentBalance());
				prepareTransferData.setTransdescription(pTransactionData.getTransdescription());
				transferlist.add(prepareTransferData);

				
			}
			transferDataResult.setTransferlist(transferlist);
			transferDataResult.setStatus(true);
			return transferDataResult;
		}
	
	public static String getFunctionID(String currencyCode,String branchCode,Enum.FunctionID type,String productType) {
		//(pTransactionData.getCurrencyCode(),pParentAccount.getBranchCode(),Enum.FunctionID.IBT,Enum.FunctionID.IBT,pParentAccount.getProduct())
		FunctionID l_FunID = new FunctionID();
		l_FunID.setCurrencyCode(currencyCode);
        l_FunID.setBranchCode(branchCode);
        l_FunID.setType(type);
        if(!productType.equals("")) {
        	if (SharedLogic.isCurrent(productType)) {
  	          l_FunID.setAccPType("CA");
  	        } else if (SharedLogic.isSavings(productType)) {
  	          l_FunID.setAccPType("SA");
  	        } else if (SharedLogic.isCall(productType)) {
  	          l_FunID.setAccPType("CD");
  	        } else if (SharedLogic.isUA(productType)) {
  	          l_FunID.setAccPType("SF");
  	        } else if (SharedLogic.isSpecial(productType)) {
  	          l_FunID.setAccPType("SP");
  	        }  
        }
	    	
	    return l_FunID.getFunctionID();
	}
	
	public static DataResult postTransaction(String tmp_Transpreapre,String l_TableName,String l_TableName1, Connection pConn) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException {
		DataResult l_DataResult = new DataResult();
		AccountDao lAccDAO=new AccountDao();
		TransactionMgr t_mgr = new TransactionMgr();
		//update Balance
		l_DataResult = lAccDAO.updateAccountBalanceCurrency( tmp_Transpreapre, l_TableName, l_TableName1, pConn);
		if(l_DataResult.getStatus()) {
			l_DataResult = t_mgr.saveTmpTabletoAccountTransaction(tmp_Transpreapre, pConn);
		}
		t_mgr.droptable(tmp_Transpreapre,pConn);
		return l_DataResult;
	}
	
	public static DataResult postTransactionUpdate(TransferDataResult transferDataResult,String l_TableName,String l_TableName1, Connection pConn) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException {
		DataResult l_DataResult = new DataResult();
		AccountDao lAccDAO=new AccountDao();
		TransactionMgr t_mgr = new TransactionMgr();
		TransferDataResult result = new TransferDataResult();
		//update Balance
		result = lAccDAO.updateAccountBalanceCurrencyUpdate( transferDataResult, l_TableName, l_TableName1, pConn);
		if(result.isStatus()) {
			l_DataResult = t_mgr.saveAccountTransaction(transferDataResult.getTransferlist(), pConn);
		}
		return l_DataResult;
	}

	public static DataResult preparetransautolinkacc(AccountData l_ParentAccData,String tmp_linkaccpreapre,String tmp_LinkTranspreapre,int l_isUseComm,String fkey, int lastcount,boolean isLast,int l_n3, Connection pConn) throws SQLException{
		PreparedStatement pstmt = null;
		DataResult l_DataResult = new DataResult();
		String l_IBTGLFrom = "", l_IBTGLTo = "", l_IBTGLComm="", l_funID ="",l_Query="",childBrCode="",pCurCode=l_ParentAccData.getCurrencyCode();
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		    	// different branch
		        l_funID = getFunctionID(l_ParentAccData.getCurrencyCode(),l_ParentAccData.getBranchCode(),Enum.FunctionID.IBT,l_ParentAccData.getProduct());
			      
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
				if(l_RefData != null) l_IBTGLFrom = l_RefData.getGLCode();
				if(l_IBTGLFrom.equals("")) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription("Invalid GL Account!");
					return l_DataResult;
				}
			   // Cr IBT GL
				if(isLast) {
					l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
							+ "(CASE WHEN "+l_n3+"=0 THEN AmountNeeded+CommAmt ELSE AmountNeeded END) * (CurRate*1000)/1000,"
							+ " ?,BranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,ReferenceNo,Remark, '19000101',1,"
							+ " CurRate,(CASE WHEN "+l_n3+"=0 THEN AmountNeeded+CommAmt ELSE AmountNeeded END)*CurRate,?,?,CashInHandGL,ProductGL,'',1,0 "
							+ " FROM "+tmp_linkaccpreapre +" WHERE Available>=AmountNeeded + CommAmt  AND Rowno="+lastcount+" AND FromBranchCode<> BranchCode";
				}else{
						l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
								+ "(CASE WHEN "+l_n3+"<>0 THEN Available-CommAmt ELSE Available END) * (CurRate*1000)/1000,"
								+ " ?,BranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,ReferenceNo,Remark, '19000101',1,"
								+ " CurRate,(CASE WHEN "+l_n3+"<>0 THEN Available-CommAmt ELSE Available END)*CurRate,?,?,CashInHandGL,ProductGL,'',1,0 "
								+ " FROM "+tmp_linkaccpreapre +" WHERE Available>CommAmt AND Rowno=1 AND FromBranchCode <> BranchCode";
				}
				    
				pstmt = pConn.prepareStatement(l_Query);
				int index = 1;
				pstmt.setString(index++, l_IBTGLFrom);
				pstmt.setString(index++,String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, fkey)) + ", " + pCurCode);
				pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkCr, fkey));
				if(pstmt.executeUpdate()>0) {
					if(isLast) 
						l_Query = "SELECT BranchCode FROM "+tmp_linkaccpreapre+" WHERE Rowno= "+lastcount ;
					else
						l_Query = "SELECT BranchCode FROM "+tmp_linkaccpreapre+" WHERE Rowno= 1" ;
					pstmt = pConn.prepareStatement(l_Query);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) childBrCode = rs.getString("BranchCode");
					

					l_funID = getFunctionID(l_ParentAccData.getCurrencyCode(),childBrCode,Enum.FunctionID.IBT,l_ParentAccData.getProduct());
				    
				      l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
				      if(l_RefData != null) l_IBTGLTo = l_RefData.getGLCode();
				      
				      if(l_IBTGLTo.equals("")) {
							l_DataResult.setStatus(false);
							l_DataResult.setDescription("Invalid GL Account!");
							return l_DataResult;
				      }
				      
				   // Dr IBT GL
				      if(isLast) {
				    	  l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
									+ " (CASE WHEN "+l_n3+"=0 THEN AmountNeeded+CommAmt ELSE AmountNeeded END)*(CurRate*1000)/1000,"
									+ " ?,BranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,ReferenceNo,Remark,'19000101',1,CurRate,"
									+ " (CASE WHEN "+l_n3+"=0 THEN AmountNeeded+CommAmt ELSE AmountNeeded END)*CurRate,?,?,CashInHandGL,ProductGL,'',1,0 "
									+ " FROM "+tmp_linkaccpreapre +" WHERE Available>=AmountNeeded + CommAmt AND Rowno="+lastcount+" AND FromBranchCode <> BranchCode";
				      }else {
				    	  l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
									+ " (CASE WHEN "+l_n3+"<>0 THEN Available-CommAmt ELSE Available END)*(CurRate*1000)/1000,"
									+ " ?,BranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,ReferenceNo,Remark, '19000101',1,CurRate,"
									+ " (CASE WHEN "+l_n3+"<>0 THEN Available-CommAmt ELSE Available END)*CurRate,?,?,CashInHandGL,ProductGL,'',1,0 "
									+ " FROM "+tmp_linkaccpreapre +" WHERE Available>=Available+CommAmt AND Rowno=1 AND FromBranchCode <> BranchCode";
				      }
				      index=1;
				      	pstmt = pConn.prepareStatement(l_Query);
						pstmt.setString(index++, l_IBTGLTo);
						pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) + ", " + pCurCode);
						pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
					    pstmt.executeUpdate();
				}
				
				l_DataResult.setStatus(false);
					// Cr To Customer
				    if(isLast) {
				    	l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
								+ " (CASE WHEN "+l_n3+"=0 THEN AmountNeeded+CommAmt ELSE AmountNeeded END)*(CurRate*1000)/1000,"
								+ " FromAccNumber,FromBranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,ReferenceNo,Remark, '19000101',1,CurRate,"
								+ " (CASE WHEN "+l_n3+"=0 THEN AmountNeeded+CommAmt ELSE AmountNeeded END)*CurRate,?,?,CashInHandGL,ProductGL,'',1,? "
								+ " FROM "+tmp_linkaccpreapre +" WHERE Available>=AmountNeeded+CommAmt AND Rowno="+lastcount+" ";
				    }else {
				    	l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
								+ " (CASE WHEN "+l_n3+"<>0 THEN Available-CommAmt ELSE Available END)*(CurRate*1000)/1000,"
								+ " FromAccNumber,FromBranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,ReferenceNo,Remark, '19000101',1,CurRate,"
								+ " (CASE WHEN "+l_n3+"<>0 THEN Available-CommAmt ELSE Available END)*CurRate,?,?,CashInHandGL,ProductGL,'',1,? "
								+ " FROM "+tmp_linkaccpreapre +" WHERE Available> CommAmt AND Rowno=1 ";
				    }
				    
				    pstmt = pConn.prepareStatement(l_Query);
				    index = 1;
					pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, fkey))+  ", " + pCurCode );
					pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkCr, fkey));
					pstmt.setDouble(index++, l_ParentAccData.getCurrentBalance());
					pstmt.executeUpdate();
					
					if (l_isUseComm == 1) {
					   // Dr IBT GL
						l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
								+ "CommAmt * (CurRate*1000)/1000, FromAccNumber,FromBranchCode,CurCode,TransactionTime,TransactionDate,"
								+ " EffectiveDate,ReferenceNo,Remark, '19000101',1,CurRate,CommAmt*CurRate,?,?,CashInHandGL,ProductGL,ByForceCheque,1,0 "
								+ " FROM "+tmp_linkaccpreapre +" WHERE CommAmt>0 AND Rowno="+lastcount;
							index=1;
							pstmt = pConn.prepareStatement(l_Query);
							pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkDr, fkey)) + ", Commission, " + pCurCode);
							pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkDr, fkey));
						    pstmt.executeUpdate();	
					      
					      l_funID = getFunctionID(l_ParentAccData.getCurrencyCode(),l_ParentAccData.getBranchCode(),Enum.FunctionID.ALKCOM,"");
					      l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_funID);
					      if(l_RefData != null) l_IBTGLComm = l_RefData.getGLCode();
					      if(l_IBTGLComm.equals("")) {
								l_DataResult.setStatus(false);
								l_DataResult.setDescription("Invalid GL Account!");
								return l_DataResult;
					      }
					  	// Cr To Customer
						    l_Query = "INSERT INTO " + tmp_LinkTranspreapre  + " SELECT 'Mobile',UserID,AuthorizerID,"
									+ " CommAmt * (CurRate*1000)/1000, ?,FromBranchCode,CurCode,TransactionTime,TransactionDate,EffectiveDate,"
									+ " ReferenceNo,Remark, '19000101',1,CurRate,CommAmt*CurRate,?,?,CashInHandGL,ProductGL,'',1,0 "
									+ " FROM "+tmp_linkaccpreapre +" WHERE CommAmt>0 AND Rowno="+lastcount;
						    pstmt = pConn.prepareStatement(l_Query);
						    index = 1;
						    pstmt.setString(index++, l_IBTGLComm);
							pstmt.setString(index++, String.valueOf(SharedLogic.getTransDescription(TransCode.AccountLinkCr, ""))+  ", Commission " + pCurCode );
							pstmt.setInt(index++,SharedLogic.getTransType(TransCode.AccountLinkCr, ""));
							pstmt.executeUpdate();	
					
					}
					l_DataResult.setStatus(true);
					return l_DataResult;
	}
	
	static DataResult checkChequeNumberTransaction(String accNumber,String referencenumber, String pByForceCheque, Connection conn)
			throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		CheckIssueDAO l_CheckDAO = new CheckIssueDAO();
		DataResult ret = new DataResult();
		int lCheckStatus = 255;

		// Checking ByForce Cheque Or Not
		if (pByForceCheque.equals(referencenumber)) {
			ret.setStatus(true);
		} else {
			if (referencenumber.equals("")) {
				ret.setStatus(false);
				ret.setDescription("Invalid cheque number.");
			} else {
				// Format Cheque Number

				lCheckStatus = l_CheckDAO.getCheckStatus(accNumber, referencenumber, conn);
				// Checking Check Status
				ret = SharedLogic.checkCheckStatus(accNumber, lCheckStatus);
			}
		}

		return ret;
	}			
	
	public static DataResult postRepaymentLoanLimit(LoanAccountData pLoanData, String pBranchCode, String pUser,
			String pIP, String pCounterID, String pFormType, String pType, Connection conn) {
		DataResult l_Result = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLTrList = new ArrayList<AccountGLTransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String l_GL = "";
		String l_FID = "";

		FunctionID functionID = new FunctionID();
		functionID.setBranchCode(pBranchCode);
		functionID.setCurrencyCode(pLoanData.getCurrencyCode());
		LoanInterestDAO l_InterestDAO = new LoanInterestDAO();
		double checkAccrueAmt = 0.0;
		double diffIntAmt = 0.0;
		double acccrueIntAmt2 = 0.0;
		boolean interestStatus = false;
		AccountData l_LAAccData = new AccountData();
		AccountData l_CAAccData = new AccountData();
		AccountDao l_AccDAO = new AccountDao();

		try {
			if (l_AccDAO.getAccount(pLoanData.getAccNumber(), conn))
				l_LAAccData = l_AccDAO.getAccountsBean();

			if (l_AccDAO.getAccount(pLoanData.getAccLinked(), conn))
				l_CAAccData = l_AccDAO.getAccountsBean();

			// Repayment Amount
			if (pLoanData.getSettleAmount() > 0) {
				// Dr BackAccount
				l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr, pLoanData.getAccLinked(),
						pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
						pLoanData.getSettleAmount(), pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(),
						l_CAAccData.getProduct());
				l_TransactionData.setDescription(pLoanData.getAccNumber() + ", " + l_TransactionData.getDescription());

				l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
				DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
				l_TransactionList.add(l_TransactionData);

				// Cr LoanAccount
				l_TransactionData = new TransactionData();
				l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, pLoanData.getAccNumber(),
						pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
						pLoanData.getSettleAmount(), pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(),
						l_LAAccData.getProduct());
				l_TransactionData.setDescription(pLoanData.getAccLinked() + ", " + l_TransactionData.getDescription());

				l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
				DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
				l_TransactionList.add(l_TransactionData);
			}

			// Interest Amount
			if (pLoanData.getN1() > 0) {
				interestStatus = true;
				functionID.setType(Enum.FunctionID.LAI);
				if (SharedLogic.getSystemSettingT12N4("LAR") == 1) {
					functionID.setLaType(pLoanData.getLoanType());
				}
				l_FID = functionID.getFunctionID();
				l_GL = DBSystemMgr.getGLCode(l_FID, conn);

				functionID.setType(Enum.FunctionID.LAA);

				if (SharedLogic.getSystemSettingT12N4("LAR") == 1) {
					functionID.setLaType(pLoanData.getLoanType());
				}

				String accrueFID = functionID.getFunctionID();
				String accrueGL = DBSystemMgr.getGLCode(accrueFID, conn);
				LoanInterestDAO l_IntDAO = new LoanInterestDAO();
				checkAccrueAmt = l_IntDAO.checkAccrueAmt(pLoanData.getAccNumber(), pLoanData.getSanctionDateBr(), conn);

				if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5() == 1 || SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5() == 2) {
					if (pLoanData.getN1() == checkAccrueAmt) {
						// Dr BackAccount
						l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr, pLoanData.getAccLinked(),
								pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
								pLoanData.getN1(), pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(),
								l_CAAccData.getProduct());
						l_TransactionData.setDescription(l_GL + ", Loan Interest");

						l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
						DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
						l_TransactionList.add(l_TransactionData);

						// Cr GL
						l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, accrueGL,
								pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
								pLoanData.getN1(), pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), "GL");
						l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Interest");

						l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
						DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
						l_TransactionList.add(l_TransactionData);
					} else {
						if (SharedLogic.getSystemSettingT12N1("BNK") == 17) {
							diffIntAmt = pLoanData.getN1() - checkAccrueAmt;

							if (checkAccrueAmt == 0) {
								acccrueIntAmt2 = pLoanData.getN1(); // ---- if Accrue hasn't amount is InterestAmount
																	// from Form
								accrueGL = l_GL; // ---- if Accrue hasn't CusAcc to InterestGL
								diffIntAmt = 0;

							} else {
								acccrueIntAmt2 = checkAccrueAmt;
							}
							l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr,
									pLoanData.getAccLinked(), pLoanData.getT1(), pLoanData.getCurrencyCode(),
									pLoanData.getCurrencyRate(), acccrueIntAmt2 + diffIntAmt, pBranchCode, pUser,
									pCounterID, pIP, pLoanData.getT2(), l_CAAccData.getProduct());
							l_TransactionData.setDescription(accrueGL + ", Loan Interest");

							l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
							DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
							l_TransactionList.add(l_TransactionData);

							// Cr GL
							l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, accrueGL,
									pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
									acccrueIntAmt2, pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), "GL");
							l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Interest");

							l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
							DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
							l_TransactionList.add(l_TransactionData);

							// -----------------------------------------------//
							if (checkAccrueAmt > 0) {
								// Cr GL
								l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, l_GL,
										pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
										diffIntAmt, pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), "GL");
								l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Interest");

								l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
								DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
								l_TransactionList.add(l_TransactionData);
							}
						} else {
							diffIntAmt = pLoanData.getN1() - checkAccrueAmt;

							if (checkAccrueAmt == 0) {
								acccrueIntAmt2 = pLoanData.getN1(); // ---- if Accrue hasn't amount is InterestAmount
																	// from Form
								accrueGL = l_GL; // ---- if Accrue hasn't CusAcc to InterestGL
							} else {
								if (diffIntAmt < 0) {
									acccrueIntAmt2 = pLoanData.getN1();
								} else {
									acccrueIntAmt2 = checkAccrueAmt;
								}
							}

							// Dr BackAccount with accrueGL
							l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr,
									pLoanData.getAccLinked(), pLoanData.getT1(), pLoanData.getCurrencyCode(),
									pLoanData.getCurrencyRate(), acccrueIntAmt2, pBranchCode, pUser, pCounterID, pIP,
									pLoanData.getT2(), l_CAAccData.getProduct());
							l_TransactionData.setDescription(accrueGL + ", Loan Interest");

							l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
							DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
							l_TransactionList.add(l_TransactionData);

							// Cr GL
							l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, accrueGL,
									pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
									acccrueIntAmt2, pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), "GL");
							l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Interest");

							l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
							DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
							l_TransactionList.add(l_TransactionData);

							// -----------------------------------------------//
							if (checkAccrueAmt > 0) {
								if (diffIntAmt > 0) {
									// Dr BackAccount with IntGL
									l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr,
											pLoanData.getAccLinked(), pLoanData.getT1(), pLoanData.getCurrencyCode(),
											pLoanData.getCurrencyRate(), diffIntAmt, pBranchCode, pUser, pCounterID,
											pIP, pLoanData.getT2(), l_CAAccData.getProduct());
									l_TransactionData.setDescription(l_GL + ", Loan Interest");

									l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
									DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
									l_TransactionList.add(l_TransactionData);

									// Cr GL
									l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, l_GL,
											pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
											diffIntAmt, pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), "GL");
									l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Interest");

									l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
									DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
									l_TransactionList.add(l_TransactionData);
								} else {

									// Dr GL
									l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr, accrueGL,
											pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
											diffIntAmt * -1, pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(),
											"GL");
									l_TransactionData.setDescription(l_GL + ", Loan Interest");

									l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
									DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
									l_TransactionList.add(l_TransactionData);

									// Cr BackAccount with IntGL
									l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, l_GL,
											pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
											diffIntAmt * -1, pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(),
											l_CAAccData.getProduct());
									l_TransactionData.setDescription(accrueGL + ", Loan Interest");

									l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
									DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
									l_TransactionList.add(l_TransactionData);
								}
							}
						}
					}
				} else {
					// Dr BackAccount with IntGL
					l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr, pLoanData.getAccLinked(),
							pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(),
							pLoanData.getN1(), pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(),
							l_CAAccData.getProduct());
					l_TransactionData.setDescription(l_GL + ", Loan Interest");

					l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
					DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
					l_TransactionList.add(l_TransactionData);

					// Cr GL
					l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, l_GL, pLoanData.getT1(),
							pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(), pLoanData.getN1(), pBranchCode,
							pUser, pCounterID, pIP, pLoanData.getT2(), "GL");
					l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Interest");

					l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
					DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
					l_TransactionList.add(l_TransactionData);
				}
			}

			// Service Charges Amount
			if (pLoanData.getN2() > 0) {
				if (pType.equals("Ext"))
					functionID.setType(Enum.FunctionID.LAEXT);
				else
					functionID.setType(Enum.FunctionID.LAS);
				if (SharedLogic.getSystemSettingT12N4("LAR") == 1) {
					functionID.setLaType(pLoanData.getLoanType());
				}
				l_FID = functionID.getFunctionID();
				l_GL = DBSystemMgr.getGLCode(l_FID, conn);

				// Dr BackAccount
				l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr, pLoanData.getAccLinked(),
						pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(), pLoanData.getN2(),
						pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), l_CAAccData.getProduct());
				if (pFormType.equalsIgnoreCase("Allow") && !pType.equals("Ext"))
					l_TransactionData.setDescription(l_GL + ", Loan Allow Service Charges");
				else if (pType.equals("Ext"))
					l_TransactionData.setDescription(l_GL + ", Loan Extension Fee");
				else
					l_TransactionData.setDescription(l_GL + ", Loan Service Charges");

				l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
				DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
				l_TransactionList.add(l_TransactionData);

				// Cr GL
				l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, l_GL, pLoanData.getT1(),
						pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(), pLoanData.getN2(), pBranchCode, pUser,
						pCounterID, pIP, pLoanData.getT2(), "GL");
				if (pFormType.equalsIgnoreCase("Allow") && !pType.equals("Ext"))
					l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Allow Service Charges");
				else if (pType.equals("Ext"))
					l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Extension Fee");
				else
					l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Service Charges");

				l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
				DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
				l_TransactionList.add(l_TransactionData);
			}

			// Commitment Fee
			if (pLoanData.getN3() > 0) {

				functionID.setType(Enum.FunctionID.LAC);
				if (SharedLogic.getSystemSettingT12N4("LAR") == 1) {
					functionID.setLaType(pLoanData.getLoanType());
				}
				l_FID = functionID.getFunctionID();
				l_GL = DBSystemMgr.getGLCode(l_FID, conn);

				// Dr BackAccount
				l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentDr, pLoanData.getAccLinked(),
						pLoanData.getT1(), pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(), pLoanData.getN3(),
						pBranchCode, pUser, pCounterID, pIP, pLoanData.getT2(), l_CAAccData.getProduct());
				l_TransactionData.setDescription(l_GL + ", Loan Commitment Fee");

				l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
				DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
				l_TransactionList.add(l_TransactionData);

				// Cr GL
				l_TransactionData = prepareAllowLoanLimit(TransCode.LoanRepaymentCr, l_GL, pLoanData.getT1(),
						pLoanData.getCurrencyCode(), pLoanData.getCurrencyRate(), pLoanData.getN3(), pBranchCode, pUser,
						pCounterID, pIP, pLoanData.getT2(), "GL");
				l_TransactionData.setDescription(pLoanData.getAccLinked() + ", Loan Commitment Fee");

				l_AccGLTrList.add(DBTransactionMgr.prepareAccountGLTransaction(l_TransactionData));
				DBTransactionMgr.prepareBaseCurrency(l_TransactionData);
				l_TransactionList.add(l_TransactionData);
			}
			
			// l_Result = post(l_TransactionList, l_AccGLTrList, conn);

			if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5() == 1) {
				if (l_Result.getStatus() == true && checkAccrueAmt > 0) {
					if (l_InterestDAO.updateLoanInterestStatusByAccNOAndLessThenRepayDate(pLoanData.getAccNumber(),
							pLoanData.getSanctionDateBr(), conn))
						l_Result.setStatus(true);
					else {
						l_Result.setStatus(false);
						l_Result.setDescription("Update Loan interest status Fail!");
					}
					// System.out.println("After doing Loan Repayment,Loan Interest table status
					// updating is " + update_Status);
				}
			} else {
				if (l_Result.getStatus() == true && interestStatus) {
					if (l_InterestDAO.updateLoanInterestStatusByAccNOAndLessThenRepayDate(pLoanData.getAccNumber(),
							pLoanData.getSanctionDateBr(), conn))
						l_Result.setStatus(true);
					else {
						l_Result.setStatus(false);
						l_Result.setDescription("Update Loan interest status Fail!");
					}
					// System.out.println("After doing Loan Repayment,Loan Interest table status
					// updating is " + update_Status);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l_Result;
	}
	
	public static TransactionData prepareAllowLoanLimit(TransCode pTransCode, String pAccNumber, String pChequeNo,
			String pCurrencyCode, double pCurrencyRate, double pTrAmount, String pBranchCode, String pUser,
			String pCounterID, String pIP, String pRemark, String pPID) throws SQLException {

		TransactionData currentObject = new TransactionData();

		currentObject.setBranchCode(pBranchCode);
		currentObject.setWorkstation(pIP);
		currentObject.setUserID(pUser);
		currentObject.setAuthorizerID(pCounterID);

		currentObject.setCurrencyCode(pCurrencyCode);
		currentObject.setCurrencyRate(pCurrencyRate);
		currentObject.setTransactionDate(SharedUtil.formatDDMMYYYY2MIT(GeneralUtility.getCurrentDate()));
		currentObject.setEffectiveDate(SharedUtil.formatDDMMYYYY2MIT(GeneralUtility.getCurrentDate()));

		currentObject.setContraDate("19000101");
		currentObject.setStatus(0);
		currentObject.setAmount(pTrAmount);
		/*
		 * if(pTransCode == TransCode.LoanRepaymentDr){
		 * currentObject.setReferenceNumber(pChequeNo); }
		 */
		currentObject.setRemark(pRemark);
		currentObject.setAccountNumber(pAccNumber);
		currentObject.setTransactionType(SharedLogic.getTransType(pTransCode, pPID));
		currentObject.setDescription(SharedLogic.getTransDescription(pTransCode, pPID));

		if (currentObject.getTransactionType() >= 500) {
			currentObject.setReferenceNumber(pChequeNo);
		}
		return currentObject;
	}
	
	public static AccountGLTransactionData prepareAccountGLTransaction(TransactionData pTransData) {
		AccountGLTransactionData object = new AccountGLTransactionData();
		AccountTransactionDAO trDAO = new AccountTransactionDAO();

		object.setTransNo(pTransData.getTransactionNumber());
		object.setAccNumber(pTransData.getAccountNumber());
		object.setGLCode1(pTransData.getProductGL());
		object.setGLCode2(pTransData.getCashInHandGL());
		object.setGLCode3("");
		object.setGLCode4("");
		object.setGLCode5("");
		object.setBaseCurCode(SharedLogic.getBaseCurCode());
		object.setBaseCurOperator(SharedLogic.getBaseCurOperator());
		object.setBaseRate(SharedLogic.getBaseCurRate());
		if (pTransData.getBaseAmount() > 0)
			object.setBaseAmount(pTransData.getBaseAmount());
		else
			object.setBaseAmount(StrUtil.round2decimals(pTransData.getAmount() * pTransData.getCurrencyRate(),
					RoundingMode.HALF_UP));

		object.setMediumCurCode("");
		object.setMediumCurOperator("");
		object.setTrCurCode(pTransData.getCurrencyCode());
		object.setTrCurOperator(""); // To Set Transaction Cur Operator
		object.setTrRate(pTransData.getCurrencyRate());
		object.setTrAmount(StrUtil.round2decimals(pTransData.getAmount(), RoundingMode.HALF_UP));
		object.setTrPrevBalance(StrUtil.round2decimals(pTransData.getPreviousBalance(), RoundingMode.HALF_UP));
		object.setN1(0);
		object.setN2(0);
		object.setN3((int) pTransData.getN3());
		object.setT1("");
		object.setT2("");
		object.setT3(pTransData.getUserID());
		if (SharedLogic.getSystemSettingT12N1("MFI") == 1) {
			if (pTransData.getTransactionType() == 705 || pTransData.getTransactionType() == 841
					|| pTransData.getTransactionType() == 850 || pTransData.getTransactionType() == 107
					|| (pTransData.getTransactionType() == 99 && pTransData.getN1() == 1)
					|| (pTransData.getTransactionType() == 599 && pTransData.getN1() == 1)
					|| pTransData.getTransactionType() == 761 || pTransData.getTransactionType() == 261
					|| pTransData.getTransactionType() == 762 || pTransData.getTransactionType() == 262
					|| pTransData.getTransactionType() == 842 || pTransData.getTransactionType() == 342
					|| pTransData.getTransactionType() == 843 || pTransData.getTransactionType() == 343
					|| pTransData.getTransactionType() == 851 || pTransData.getTransactionType() == 351
					|| pTransData.getTransactionType() == 852 || pTransData.getTransactionType() == 352
					|| pTransData.getTransactionType() == 871 || pTransData.getTransactionType() == 371
					|| pTransData.getTransactionType() == 872 || pTransData.getTransactionType() == 372
					|| pTransData.getTransactionType() == 870 || pTransData.getTransactionType() == 370
					|| pTransData.getTransactionType() == 853 || pTransData.getTransactionType() == 353
					|| pTransData.getTransactionType() == 854 || pTransData.getTransactionType() == 354
					|| pTransData.getTransactionType() == 764 || pTransData.getTransactionType() == 264
					|| pTransData.getTransactionType() == 765 || pTransData.getTransactionType() == 265
					|| pTransData.getTransactionType() == 844 || pTransData.getTransactionType() == 344
					|| pTransData.getTransactionType() == 845 || pTransData.getTransactionType() == 345
					|| pTransData.getTransactionType() == 846 || pTransData.getTransactionType() == 346) {
				// Transfer Slip
				String transType = "";
				String alreadyslip = "";
				if (pTransData.getTransactionType() == 705 || pTransData.getTransactionType() == 853) {
					transType = "JV";
				} else if (pTransData.getTransactionType() == 841 || pTransData.getTransactionType() == 599) {
					transType = "PV";
				} else if (pTransData.getTransactionType() == 850 || pTransData.getTransactionType() == 107
						|| pTransData.getTransactionType() == 99) {
					transType = "RV";
				} else if (pTransData.getTransactionType() == 761 || pTransData.getTransactionType() == 261
						|| pTransData.getTransactionType() == 851 || pTransData.getTransactionType() == 351
						|| pTransData.getTransactionType() == 852 || pTransData.getTransactionType() == 352
						|| pTransData.getTransactionType() == 764 || pTransData.getTransactionType() == 264) {
					transType = "PV";
					if (!pTransData.getSubRef().equalsIgnoreCase("0"))
						alreadyslip = "Yes";
				} else if (pTransData.getTransactionType() == 762 || pTransData.getTransactionType() == 262
						|| pTransData.getTransactionType() == 842 || pTransData.getTransactionType() == 342
						|| pTransData.getTransactionType() == 843 || pTransData.getTransactionType() == 343
						|| pTransData.getTransactionType() == 870 || pTransData.getTransactionType() == 370
						|| pTransData.getTransactionType() == 871 || pTransData.getTransactionType() == 371
						|| pTransData.getTransactionType() == 872 || pTransData.getTransactionType() == 372
						|| pTransData.getTransactionType() == 765 || pTransData.getTransactionType() == 265
						|| pTransData.getTransactionType() == 845 || pTransData.getTransactionType() == 345
						|| pTransData.getTransactionType() == 846 || pTransData.getTransactionType() == 346) {
					transType = "RV";
					if (!pTransData.getSubRef().equalsIgnoreCase("0"))
						alreadyslip = "Yes";
				} else if (pTransData.getTransactionType() == 854 || pTransData.getTransactionType() == 354
						|| pTransData.getTransactionType() == 844 || pTransData.getTransactionType() == 344) {
					transType = "JV";
					if (!pTransData.getSubRef().equalsIgnoreCase("0"))
						alreadyslip = "Yes";
				}
				int SerialNo;
				try {
					if (!alreadyslip.equalsIgnoreCase("Yes")) {
						SerialNo = trDAO.getSlipNo("",String.valueOf(transType) + pTransData.getEffectiveDate().substring(2, 4) + "/"
												+ pTransData.getEffectiveDate().substring(4, 6),
										pTransData.getEffectiveDate());
						String formatSerialNo = String.format("%03d", SerialNo);
						String slipNo = String.valueOf(transType) + pTransData.getEffectiveDate().substring(2, 4) + "/"
								+ pTransData.getEffectiveDate().substring(4, 6) + "/" + formatSerialNo;
						object.setN2(SerialNo);
						object.setT1(slipNo);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// System.out.println(pTransData);
			}
		}
		return object;
	}
	
	public static DataResult reversalTransaction(int pTransRef, boolean isReversalFromOthers, String pWorkStation, String pTellerId, String pSuperVisorId, Connection conn) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException {
		DataResult ret = new DataResult();
		ArrayList<TransactionData> arlReversedTramsactions =new ArrayList<TransactionData>();
		AccountTransactionDAO l_AccTransDAO = new AccountTransactionDAO();
		GLDAO l_GLDAO = new GLDAO();
		CheckIssueDAO l_CheckIssueDAO = new CheckIssueDAO();
		AccountsDAO l_AccDAO = new AccountsDAO(conn);
		String l_ByForceCheque = "";
		String l_Desc = "";
		String l_AccRef = "";
		String l_pTableName = "AccountGLTransaction";
		int l_TransType = 0;
		
		double minBalance =0.00;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Calendar c = java.util.Calendar.getInstance();
		int l_MinBalSetting = SharedLogic.getSystemData().getAccMinSetting();
		String AccType="";
		//SSH GLTransactions
		AccountGLTransactionDAO l_AccGLTransDAO = new AccountGLTransactionDAO();
		AccountGLTransactionData l_AccountGLTransactionData = null;
		double TrAmount=0;
				
		Date transDate = new Date();
		Date todayDate = new Date();
		int l_ReversalSettingN3 = 0;
		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("REV") != null)
		 l_ReversalSettingN3 = SharedLogic.getSystemData().getpSystemSettingDataList().get("REV").getN3();//LogicMgr.getSystemSettingT12N3("REV")
		
		// Getting Transactions Using TransRef
		//ret.setStatus(l_AccTransDAO.getAccTransactionsTransRef(pTransRef, conn));
		//mmmyint 09.08.2018 icbs 3.0 to 3.1
		ret.setStatus(l_AccTransDAO.getAccTransactionsTransRefNew(pTransRef, conn));
		
		if (ret.getStatus()){
			arlReversedTramsactions = l_AccTransDAO.getTransactionDataList();
			ret.setStatus(true);
		} else{
			ret.setDescription("No Transaction exist");
			ret.setStatus(false);
		}
		if (ret.getStatus()){
			transDate=StrUtil.formatYYYYMMDDToDate(arlReversedTramsactions.get(0).getEffectiveDate());
			todayDate=StrUtil.formatYYYYMMDDToDate(SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate()));
			/*
			 * if(l_ReversalSettingN3==1){ if (!SingletonServer.isOwnerRight()){ if
			 * (transDate.compareTo(todayDate)!=0){ ret.setStatus(false);
			 * ret.setDescription("Can't allow back date reversal."); } } }else
			 * if(l_ReversalSettingN3==2){ if (transDate.compareTo(todayDate)<0){
			 * ret.setStatus(false); ret.setDescription("Can't allow back date reversal.");
			 * } }else{
			 */
				ret.setStatus(true);
			//}
		}
		
		//KMT
		ArrayList<TransactionData> arlReversedTramsactionstemp =new ArrayList<TransactionData>();
		l_Desc = arlReversedTramsactions.get(0).getDescription();

		int loop = 0;
		// Getting ByForce Cheque;
		l_ByForceCheque = DBSystemMgr.getByForceCheck(conn);
		if (ret.getStatus()){
			AccountData l_AccData = new AccountData();
			for (int i=0; i< arlReversedTramsactions.size(); i++){
				l_Desc = arlReversedTramsactions.get(i).getDescription();
				l_AccRef = arlReversedTramsactions.get(i).getAccRef();
				l_TransType = arlReversedTramsactions.get(i).getTransactionType();

				ret.setStatus(true);
				
				if (ret.getStatus()){
					arlReversedTramsactions.get(i).setDescription("Reversed, "+arlReversedTramsactions.get(i).getDescription());
					arlReversedTramsactions.get(i).setStatus(254);
					
					// Updating Reversed Transactions
					if (l_AccTransDAO.updateAccTransaction(arlReversedTramsactions.get(i), conn)){
						ret.setStatus(l_AccTransDAO.updateAccTransactionOld(arlReversedTramsactions.get(i), conn));
					}
					
					// Updating Reversed Transactions in AccGLTrans
					if (ret.getStatus()){
						l_AccountGLTransactionData = l_AccGLTransDAO.getGLTransactions(arlReversedTramsactions.get(i).getTransactionNumber(), conn);
						l_AccountGLTransactionData.setN1(254);
						ret.setStatus(l_AccGLTransDAO.UpdateGLAccountTransaction(l_AccountGLTransactionData, conn));
					}
					

					if (ret.getStatus()){	
						if (l_GLDAO.getGL(arlReversedTramsactions.get(i).getAccountNumber(), conn)){
								ret.setStatus(true);
							} else {
								if (l_AccDAO.getAccount(arlReversedTramsactions.get(i).getAccountNumber(), conn)) {
									ret.setStatus(true);
									l_AccData = l_AccDAO.getAccountsBean();

									if (l_MinBalSetting == 1) {
										AccType = LogicMgr.Account.getAccountType(l_AccData.getAccountNumber());
										minBalance = l_AccDAO.getMiniBalance(l_AccData.getProduct() + AccType, conn);
										// ---minBalance =
										// LogicMgr.Account.getAccountMinBalanceNew(l_AccData.getProduct()+AccType,
										// l_AccData.getCurrencyCode()) ;
									} else {
										minBalance = LogicMgr.Account.getAccountMinBalance(l_AccData.getProduct(),
												l_AccData.getCurrencyCode());
									}

									l_AccountGLTransactionData = l_AccGLTransDAO.getGLTransactions(
											arlReversedTramsactions.get(i).getTransactionNumber(), conn);

									TrAmount = l_AccountGLTransactionData.getTrAmount();

									// Checking Debit Balance
									if (LogicMgr.isCredit(arlReversedTramsactions.get(i).getTransactionType())) {

										int l_TT = arlReversedTramsactions.get(i).getTransactionType();
										int l_TransNO = (int) arlReversedTramsactions.get(i).getTransactionNumber();

										ret = l_AccTransDAO.checkDebitBalance(pTransRef, l_TransNO, conn);

										// Update AccountBalance
										if (ret.getStatus()) {
											ret = l_AccDAO.updateAccountBalanceReversal(l_AccData.getAccountNumber(),
													l_AccData.getCurrentBalance(), TrAmount,
													arlReversedTramsactions.get(i).getTransactionType(),
													arlReversedTramsactions.get(i).getEffectiveDate(),
													arlReversedTramsactions.get(i).getTransactionDate(),
													df.format(c.getTime()), minBalance,
													arlReversedTramsactions.get(i).getPreviousDate(), conn);
										}
									} else {
										// Update Account Balance
										ret = l_AccDAO.updateAccountBalanceReversal(l_AccData.getAccountNumber(),
												l_AccData.getCurrentBalance(), TrAmount,
												arlReversedTramsactions.get(i).getTransactionType(),
												arlReversedTramsactions.get(i).getEffectiveDate(),
												arlReversedTramsactions.get(i).getTransactionDate(),
												df.format(c.getTime()), minBalance,
												arlReversedTramsactions.get(i).getPreviousDate(), conn);
									}

								} else {
									ret.setStatus(false);
									ret.setDescription("Account number does not exist.");
								}
							}
					} else {
						ret.setStatus(false);
						ret.setDescription("Error in Reversed Transactions");
					}
					
					if (!ret.getStatus()){
						break;
					}
				}
			}
			if (ret.getStatus()){
				if (l_AccTransDAO.reversal(pTransRef, df.format(c.getTime()), df.format(c.getTime()), conn)){
					ret.setStatus(true);
				} else {
					ret.setStatus(false);
					ret.setDescription("Error in Reversal");
				}
			}
		}
		
		//SSH
		if(ret.getStatus()){
			ret=ReverseAccountGLTransaction(pTransRef,conn);
		}
		
		String action = null;
		// Commit Or RollBack
		if (ret.getStatus()){
			ret.setTransactionNumber(pTransRef);
			action = "Successfully";
		} else {
			action = "Fail";
		}
		/*
		 * if(!isReversalFromOthers){ String remark =
		 * "TransRef- "+pTransRef+", Transaction- "+l_AccRef+", AccNumber - "
		 * +arlReversedTramsactions.get(0).getAccountNumber()
		 * +" Amount- "+arlReversedTramsactions.get(0).getAmount();
		 * DBSystemLogMgr.saveSystemLog("Reversal Form","Reverse",action, remark); }
		 */		
		
		return ret;
	}
	
	public static DataResult reversalTransactionUpdate(int pTransRef, boolean isReversalFromOthers, String pWorkStation,
			String pTellerId, String pSuperVisorId, Connection conn)
			throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException {
		DataResult ret = new DataResult();
		ArrayList<TransactionData> arlReversedTramsactions = new ArrayList<TransactionData>();
		AccountTransactionDAO l_AccTransDAO = new AccountTransactionDAO();
		GLDAO l_GLDAO = new GLDAO();
		AccountsDAO l_AccDAO = new AccountsDAO(conn);
		double minBalance = 0.00;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Calendar c = java.util.Calendar.getInstance();
		int l_MinBalSetting = SharedLogic.getSystemData().getAccMinSetting();
		String AccType = "";
		AccountGLTransactionDAO l_AccGLTransDAO = new AccountGLTransactionDAO();
		AccountGLTransactionData l_AccountGLTransactionData = null;
		double TrAmount = 0;

		// Getting Transactions Using TransRef
		// ret.setStatus(l_AccTransDAO.getAccTransactionsTransRef(pTransRef, conn));
		// mmmyint 09.08.2018 icbs 3.0 to 3.1
		ret.setStatus(l_AccTransDAO.getAccTransactionsTransRefNew(pTransRef, conn));

		if (ret.getStatus()) {
			arlReversedTramsactions = l_AccTransDAO.getTransactionDataList();
			ret.setStatus(true);
		} else {
			ret.setDescription("No Transaction exist");
			ret.setStatus(false);
		}

		// KMT
		if (ret.getStatus()) {
			AccountData l_AccData = new AccountData();
			for (int i = 0; i < arlReversedTramsactions.size(); i++) {
				arlReversedTramsactions.get(i)
						.setDescription("Reversed, " + arlReversedTramsactions.get(i).getDescription());
				arlReversedTramsactions.get(i).setStatus(254);

				// Updating Reversed Transactions
				if (l_AccTransDAO.updateAccTransaction(arlReversedTramsactions.get(i), conn)) {
					ret.setStatus(l_AccTransDAO.updateAccTransactionOld(arlReversedTramsactions.get(i), conn));
				}

				// Updating Reversed Transactions in AccGLTrans
				if (ret.getStatus()) {
					l_AccountGLTransactionData = l_AccGLTransDAO
							.getGLTransactions(arlReversedTramsactions.get(i).getTransactionNumber(), conn);
					l_AccountGLTransactionData.setN1(254);
					ret.setStatus(l_AccGLTransDAO.UpdateGLAccountTransaction(l_AccountGLTransactionData, conn));
				}

				if (ret.getStatus()) {
					if (l_GLDAO.getGL(arlReversedTramsactions.get(i).getAccountNumber(), conn)) {
						ret.setStatus(true);
					} else {
						if (l_AccDAO.getAccount(arlReversedTramsactions.get(i).getAccountNumber(), conn)) {
							ret.setStatus(true);
							l_AccData = l_AccDAO.getAccountsBean();

							if (l_MinBalSetting == 1) {
								AccType = LogicMgr.Account.getAccountType(l_AccData.getAccountNumber());
								minBalance = l_AccDAO.getMiniBalance(l_AccData.getProduct() + AccType, conn);
							} else {
								minBalance = LogicMgr.Account.getAccountMinBalance(l_AccData.getProduct(),
										l_AccData.getCurrencyCode());
							}

							l_AccountGLTransactionData = l_AccGLTransDAO
									.getGLTransactions(arlReversedTramsactions.get(i).getTransactionNumber(), conn);

							TrAmount = l_AccountGLTransactionData.getTrAmount();

							// Checking Debit Balance
							if (LogicMgr.isCredit(arlReversedTramsactions.get(i).getTransactionType())) {

								int l_TT = arlReversedTramsactions.get(i).getTransactionType();
								int l_TransNO = (int) arlReversedTramsactions.get(i).getTransactionNumber();

								ret = l_AccTransDAO.checkDebitBalance(pTransRef, l_TransNO, conn);

								// Update AccountBalance
								if (ret.getStatus()) {
									ret = l_AccDAO.updateAccountBalanceReversal(l_AccData.getAccountNumber(),
											l_AccData.getCurrentBalance(), TrAmount,
											arlReversedTramsactions.get(i).getTransactionType(),
											arlReversedTramsactions.get(i).getEffectiveDate(),
											arlReversedTramsactions.get(i).getTransactionDate(), df.format(c.getTime()),
											minBalance, arlReversedTramsactions.get(i).getPreviousDate(), conn);
								}
							} else {
								// Update Account Balance
								ret = l_AccDAO.updateAccountBalanceReversal(l_AccData.getAccountNumber(),
										l_AccData.getCurrentBalance(), TrAmount,
										arlReversedTramsactions.get(i).getTransactionType(),
										arlReversedTramsactions.get(i).getEffectiveDate(),
										arlReversedTramsactions.get(i).getTransactionDate(), df.format(c.getTime()),
										minBalance, arlReversedTramsactions.get(i).getPreviousDate(), conn);
							}

						} else {
							ret.setStatus(false);
							ret.setDescription("Account number does not exist.");
						}
					}
				} else {
					ret.setStatus(false);
					ret.setDescription("Error in Reversed Transactions");
				}

				if (!ret.getStatus()) {
					break;
				}
			}
			if (ret.getStatus()) {
				if (l_AccTransDAO.reversal(pTransRef, df.format(c.getTime()), df.format(c.getTime()), conn)) {
					ret.setStatus(true);
				} else {
					ret.setStatus(false);
					ret.setDescription("Error in Reversal");
				}
			}
		}

		// SSH
		if (ret.getStatus()) {
			ret = ReverseAccountGLTransaction(pTransRef, conn);
		}

		if (ret.getStatus()) {
			ret.setTransactionNumber(pTransRef);
			if (arlReversedTramsactions.size() > 0)
				ret.setEffectiveDate(arlReversedTramsactions.get(0).getEffectiveDate());
		}
		return ret;
	}
	
	public static DataResult ReverseAccountGLTransaction(int pTransRef,Connection conn) {
		DataResult ret=new DataResult();
		AccountTransactionDAO l_AccountTransactionDAO = new AccountTransactionDAO();
		ArrayList<TransactionData> l_TransactionDataList = new ArrayList<TransactionData>();
		ArrayList<TransactionData> l_ReversalTransactionList = new ArrayList<TransactionData>();
		
		AccountGLTransactionData l_AccountGLTransactionData = null;
		ArrayList<AccountGLTransactionData> l_AccGLTransList = new ArrayList<AccountGLTransactionData>();
		AccountGLTransactionDAO l_GLTransactionDAO = new AccountGLTransactionDAO();
		
		String l_TableName="AccountGLTransaction";
		//if(l_GLTransactionDAO.CheckTableExist(l_TableName, conn)) {
			//Get Transaction Data from AccountTransaction
			boolean l_includeReversal=true;
			l_TransactionDataList=l_AccountTransactionDAO.getAccountTransactionListTransref(pTransRef,l_includeReversal, conn);
					
			for(int i=0;i<l_TransactionDataList.size();i++) {
				if(l_TransactionDataList.get(i).getStatus()==254) {
					l_AccountGLTransactionData = l_GLTransactionDAO.getGLTransactions(l_TransactionDataList.get(i).getTransactionNumber(), conn);
					l_AccGLTransList.add(l_AccountGLTransactionData);
				}
			
				// Getting Reversal Transaction
				if(l_TransactionDataList.get(i).getStatus() == 255) {
					l_ReversalTransactionList.add(l_TransactionDataList.get(i));
				}
			}
			
			// Inserting Reversed Transactions in AccountGLTransaction
			for (int i=0; i < l_ReversalTransactionList.size(); i++) {
				l_AccGLTransList.get(i).setTransNo(l_ReversalTransactionList.get(i).getTransactionNumber());
				l_AccGLTransList.get(i).setN1(l_ReversalTransactionList.get(i).getStatus());
				if(l_ReversalTransactionList.get(i).getTransactionType() >= 500) {
					l_AccGLTransList.get(i).setTrPrevBalance(l_AccGLTransList.get(i).getTrPrevBalance() + l_AccGLTransList.get(i).getTrAmount());
				}else if(l_ReversalTransactionList.get(i).getTransactionType() < 500){
					l_AccGLTransList.get(i).setTrPrevBalance(l_AccGLTransList.get(i).getTrPrevBalance() - l_AccGLTransList.get(i).getTrAmount());
				}
				
				if(l_GLTransactionDAO.addAccountGLTransaction(l_AccGLTransList.get(i), conn)) {
					ret.setStatus(true);
				}
				else {
					ret.setStatus(false);
					ret.setDescription("Can't reverse!");
				}
			
			}
		/*}
		else {
			ret.setStatus(true);
		}*/
		
		return ret;
	}
}
