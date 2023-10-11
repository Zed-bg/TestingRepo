package com.nirvasoft.rp.mgr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.dao.AccountDao;
import com.nirvasoft.rp.dao.AccountTransactionDAO;
import com.nirvasoft.rp.dao.AccountsDAO;
import com.nirvasoft.rp.dao.BankHolidayDAO;
import com.nirvasoft.rp.dao.CCTDrawingDao;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.dao.GLDAO;
import com.nirvasoft.rp.dao.LAODFDAO;
import com.nirvasoft.rp.dao.LAODFScheduleDAO;
import com.nirvasoft.rp.dao.LastDatesDAO;
import com.nirvasoft.rp.dao.LoanAccountDAO;
import com.nirvasoft.rp.dao.LoanInterestDAO;
import com.nirvasoft.rp.dao.LoanServiceChargesExtendedDAO;
import com.nirvasoft.rp.dao.ProductFeatureDAO;
import com.nirvasoft.rp.dao.ProductInteRateDAO;
import com.nirvasoft.rp.dao.ProductsDAO;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.shared.AccountGLTransactionData;
import com.nirvasoft.rp.shared.BankHolidayDataRes;
import com.nirvasoft.rp.shared.CCTDrawingData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.LAODFData;
import com.nirvasoft.rp.shared.LAODFScheduleData;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.LoanAllowSetupData;
import com.nirvasoft.rp.shared.LoanRepaymentReq;
import com.nirvasoft.rp.shared.LoanRepaymentRes;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ProductSetupData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.ResponseData;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.TransferDataResult;
import com.nirvasoft.rp.shared.gopaymentresponse;
import com.nirvasoft.rp.shared.paymenttransactiondata;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.icbs.DepositData;
import com.nirvasoft.rp.shared.icbs.RateAmountRangeSetupData;
import com.nirvasoft.rp.shared.icbs.RateSetupData;
import com.nirvasoft.rp.shared.icbs.ReferenceAccountsData;
import com.nirvasoft.rp.shared.icbs.SMSReturnData;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.icbs.WithdrawData;
import com.nirvasoft.rp.shared.icbs.Enum.TransCode;
import com.nirvasoft.rp.shared.icbs.FixedDepositAccountData;
import com.nirvasoft.rp.shared.icbs.Enum;
import com.nirvasoft.rp.util.FunctionID;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;

public class TransactionMgr {
	public static String AbsolutePath = "";
	private static int mOwnCrTransType = 320;
	private static int mOwnDrTransType = 820;
	private static int mTrCrTransType = 320;
	private static int mTrDrTransType = 820;
	private static int mDonationCrTransType = 326;
	private static int mDonationDrTransType = 826;
	private static int mGiftCardCrTransType = 328;
	private static int mGiftCardDrTransType = 828;
	private static int mWLTrCrTransType = 321;
	private static int mWLTrDrTransType = 821;
	private static int mPaymentCrTransType = 324;
	private static int mPaymentDrTransType = 824;
	private static int mDTrCrTransType = 322;
	private static int mDTrDrTransType = 822;
	private static int mETrCrTransType = 323;
	private static int mETrDrTransType = 823;
	private static int mOBTCrTransType = 329;
	private static int mOBTDrTransType = 829;
	private static long m_AutoPrintTransRef = 0;
	private static int mMobileOwnTRCom_SameBr = 12;
	private static int mMobileOwnTRCom_DiffBr_SameRegion = 14;
	private static int mMobileOwnTRCom_DiffBr_DiffRegion = 15;
	private static int mMobileInterTRCom_DiffBr_SameRegion = 16;
	private static int mMobileInterTRCom_DiffBr_DiffRegion = 17;
	private static int mMobileInterTRCom_SameBr = 13;
	private static int mLoanRepaymentDr = 840;
	private static int mLoanRepaymentCr = 340;

	public SMSReturnData postTransfer(TransferData pTransferData) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		boolean isSameBranch = false;
		boolean isGL = false;
		double trFee = 0;
		Connection l_Conn1 = null;
		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();
		// reference
		if (pTransferData.isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		Connection l_Conn = ConnAdmin.getConn3();

		l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getFromAccNumber(), l_Conn));
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
		} else {
			l_SMSReturnData.setDescription("Invalid From Account No.");
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setCode("0014");
			return l_SMSReturnData;
		}
		if (l_DataResult.getStatus()) {
			l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getToAccNumber(), l_Conn));
			if (l_DataResult.getStatus()) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
				pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
			} else {
				l_SMSReturnData.setDescription("Invalid To Account No.");
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		}
		pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
		pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
		pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());
		if (l_FromAccountData.getBranchCode().equalsIgnoreCase(l_ToAccountData.getBranchCode())) {
			isSameBranch = true;
		}
		try {
			if (l_DataResult.getStatus()) {
				l_DataResult = checkAccount(l_FromAccountData.getProduct(), l_ToAccountData.getProduct(),
						l_FromAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn);
			}
			if (l_DataResult.getStatus()) {
				String fkey = "", tkey = "";
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
					tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
					tkey = l_ToAccountData.getProduct();
				}
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
				toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

				// fromproduct = prddao.getTProduct(l_FromAccountData.getProduct(),
				// l_FromAccountData.getType(), l_Conn);
				// toproduct = prddao.getTProduct(l_ToAccountData.getProduct(),
				// l_ToAccountData.getType(), l_Conn);

				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
				// get commission
				// trFee = getMobileTransferCharges(pTransferData.getAmount(),
				// pTransferData.getDebitIBTKey(),
				// l_FromAccountData.getBranchCode(), l_ToAccountData.getBranchCode(),
				// l_FromAccountData.getZone(),
				// l_ToAccountData.getZone());
				pTransferData.setTransactionFee(trFee);
				// Preparation
				if (isSameBranch) {
					l_TransactionList = prepareTransferSameBranchWithoutComm(pTransferData, l_TodayDate,
							l_EffectiveDate, l_ArlLogString, "", l_TransactionTime, l_TrDateTime, pByForceCheque,
							fromproduct, toproduct, l_Conn);
				} else {
					l_TransactionList = prepareIBTTransfer_Centralized(pTransferData, l_TodayDate, l_EffectiveDate,
							l_ArlLogString, l_Conn, l_TransactionTime, l_TrDateTime, pByForceCheque, fromproduct,
							toproduct, l_Conn);
				}

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// End Preparation
				// Check Transaction and post link
				// Check Transaction Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						if (SharedLogic.isDebit(l_TransactionList.get(i).getTransactionType())) {
							pAccData = l_FromAccountData;
						} else {
							pAccData = l_ToAccountData;
						}
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}

				}
				if (!l_Conn.isClosed()) {
					l_Conn.close();
				}
				if (l_DataResult.getStatus()) {
					l_Conn1 = ConnAdmin.getConn();
					l_Conn1.setAutoCommit(false);
					l_ArlLogString.add("	Post Transaction ");
					l_DataResult = DBTransactionMgr.postNew(l_TransactionList, l_AccGLList, l_Conn1);
					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_Conn1.commit();
						l_SMSReturnData.setStatus(true);
						l_SMSReturnData.setCode("0000");
						l_SMSReturnData.setTransactionNumber(l_DataResult.getTransactionNumber());
						l_SMSReturnData.setComm1(trFee);
						l_SMSReturnData.setCcy(pTransferData.getCurrencyCode());
						l_SMSReturnData.setDescription("Posted successfully");
					} else {
						l_SMSReturnData.setStatus(false);
						l_SMSReturnData.setCode("0014");
						if (isLink) {
							l_SMSReturnData.setDescription(
									l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
						} else {
							l_SMSReturnData.setDescription(l_DataResult.getDescription());
						}
						l_Conn1.rollback();
					}

					if (!l_Conn1.isClosed() || l_Conn1 != null) {
						l_Conn1.close();
					}
				}
			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}
		return l_SMSReturnData;
	}

	public static String getCurrentDateYYYYMMDDHHMMSS() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	public static String getEffectiveTransDate(String aDate) throws Exception {
		String effectiveDate = "";
		Connection conn = ConnAdmin.getConn3();
		try {
			BankHolidayDAO l_BankHolidayDAO = new BankHolidayDAO();
			LastDatesDAO l_LastDateDAO = new LastDatesDAO();
			effectiveDate = getEffectiveTransDate(aDate, l_BankHolidayDAO, l_LastDateDAO, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}

		return effectiveDate;
	}

	public static String getEffectiveTransDate(String pDate, BankHolidayDAO pDAO, LastDatesDAO pLastDateDAO,
			Connection conn) throws SQLException {
		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));
		Calendar cal = Calendar.getInstance();
		month--; // In Java Calendar, month is starting zero.
		cal.set(year, month, day);

		DateFormat df = new SimpleDateFormat("yyyyMMdd");

		String effectiveDate = pDate;
		// Checking is Runned EOD
		if (pLastDateDAO.isRunnedEOD(pDate, conn)) {
			cal.clear();
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			effectiveDate = getEffectiveTransDate(effectiveDate, pDAO, pLastDateDAO, conn);
		} else if (StrUtil.isWeekEnd(pDate)) {
			cal.clear();
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			effectiveDate = getEffectiveTransDate(effectiveDate, pDAO, pLastDateDAO, conn);
		} else if (pDAO.getBankHolidayCheck(pDate, conn)) { // Checking
															// BankHoliday
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			effectiveDate = getEffectiveTransDate(effectiveDate, pDAO, pLastDateDAO, conn);
		} else {
			effectiveDate = df.format(cal.getTime());
		}
		return effectiveDate;
	}

	private DataResult checkAccount(String pFromProduct, String pToProduct, String pFromAccount, String pToAccount,
			Connection pConn) throws Exception {
		DataResult l_Result = new DataResult();
		// Checking Account is Exist Or Not
		l_Result.setStatus(true);
		if (GeneralUtility.isCardAccount(pFromAccount)) {
			l_Result.setStatus(true);
		}
		if (l_Result.getStatus()) {
			if (SharedLogic.isFD(pFromProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Fixed Deposit Account.");
			}
		}
		if (l_Result.getStatus()) {
			if (SharedLogic.isLoan(pFromProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Loan Account.");
			}
		}
		if (GeneralUtility.isCardAccount(pToAccount)) {
			l_Result.setStatus(true);
		}

		if (l_Result.getStatus()) {
			if (SharedLogic.isFD(pToProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Fixed Deposit Account.");
			}
		}
		if (l_Result.getStatus()) {
			if (SharedLogic.isLoan(pToProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Loan Account.");
			}
		}
		return l_Result;
	}

	public double getMobileTransferCharges(double pTrAmount, String pTransferType, String FromBr, String ToBr,
			int FromZone, int ToZone) throws Exception {
		RateSetupData l_RateSetupData = new RateSetupData();
		RateAmountRangeSetupData l_AmountRangeData = new RateAmountRangeSetupData();
		int rateType = 0;
		double l_TotalFee = 0.0;
		if (pTransferType.equals("1")) { // own
			if (FromBr.equals(ToBr)) { // same br
				rateType = mMobileOwnTRCom_SameBr;
			} else { // diff br
				if (FromZone == ToZone) { // same region
					rateType = mMobileOwnTRCom_DiffBr_SameRegion;
				} else { // diff region
					rateType = mMobileOwnTRCom_DiffBr_DiffRegion;
				}
			}
		} else { // Inter
			if (FromBr.equals(ToBr)) { // same br
				rateType = mMobileInterTRCom_SameBr;
			} else { // diff br
				if (FromZone == ToZone) { // same region
					rateType = mMobileInterTRCom_DiffBr_SameRegion;
				} else { // diff region
					rateType = mMobileInterTRCom_DiffBr_DiffRegion;
				}
			}
		}
		l_RateSetupData = DBRateSetupMgr.getCommissionRateSetupData(rateType, FromBr, "0", "0");

		// Total Fee on Com 12
		if (l_RateSetupData.getCom12() > 0) {
			if (l_RateSetupData.getCom12Type().equals("F")) {
				l_TotalFee = l_RateSetupData.getCom12();
			} else if (l_RateSetupData.getCom12Type().equals("R")) {
				l_TotalFee = pTrAmount * l_RateSetupData.getCom12();
				if (l_TotalFee < l_RateSetupData.getCom12Min()) {
					l_TotalFee = l_RateSetupData.getCom12Min();
				} else if (l_TotalFee > l_RateSetupData.getCom12Max()) {
					if (l_RateSetupData.getCom12Max() != 0) {
						l_TotalFee = l_RateSetupData.getCom12Max();
					}
				}
			} else {
				l_AmountRangeData = GeneralUtility.getRateByAmountRange(pTrAmount,
						l_RateSetupData.getCom12AmountRangeDatalist());
				l_TotalFee = pTrAmount * l_AmountRangeData.getCRate();
				if (l_TotalFee < l_AmountRangeData.getComMin()) {
					l_TotalFee = l_AmountRangeData.getComMin();
				} else if (l_TotalFee > l_AmountRangeData.getComMax()) {
					if (l_AmountRangeData.getComMax() != 0) {
						l_TotalFee = l_AmountRangeData.getComMax();
					}
				}
			}
		}
		return l_TotalFee;
	}

	private ArrayList<TransactionData> prepareTransferSameBranchWithoutComm(TransferData pTransferData,
			String p_TransactionDate, String p_EffectiveDate, ArrayList<String> l_ArlLogString, String pType,
			String l_TransactionTime, String l_TrDateTime, String pByForceCheque, ProductData fromproduct,
			ProductData toproduct, Connection conn) throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();

		String fkey = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
		String tkey = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();

		double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);
		double crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey);

		// double drCCYRate =
		// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData.getFromCCY(),
		// "smTransfer",
		// pTransferData.getFromBranchCode(), conn);
		// double crCCYRate =
		// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData.getToCCY(),
		// "smTransfer",
		// pTransferData.getToBranchCode(), conn);
		// double drCCYRate = 1;
		// double crCCYRate = 1;
		double totalfee = pTransferData.getAmount() + pTransferData.getTransactionFee();

		// l_RefData = DBSystemMgr.getReferenceAccCode("MTRCOM" +
		// pTransferData.getCurrencyCode()
		// + pTransferData.getFromBranchCode() + fromproduct.getProductCode());

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRCOM"
				+ pTransferData.getCurrencyCode() + pTransferData.getFromBranchCode() + fromproduct.getProductCode());
		String l_ComGL = "";
		if (l_RefData != null)
			l_ComGL = l_RefData.getGLCode();

		if (totalfee > 0) {
			// Dr From Customer
			l_TransactionData = new TransactionData();
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getFromAccNumber());
			if (fromproduct.hasCheque())
				l_TransactionData.setReferenceNumber(pByForceCheque);
			else
				l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(pTransferData.getFromBranchCode());
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getFromCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(totalfee);
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
			l_TransactionData
					.setDescription(pTransferData.getToAccNumber() + ", Mobile Transfer" + ", " + l_TrDateTime);
			l_TransactionData.setTransactionType(mTrDrTransType);
			l_TransactionData.setSubRef(pTransferData.getReferenceNo());
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("");
			l_TransactionData.setProductGL(fromproduct.getProductGL());
			l_TransactionList.add(l_TransactionData);
			// Cr To Customer
			l_TransactionData = new TransactionData();
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getToAccNumber());
			l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(pTransferData.getFromBranchCode());
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getToCCY());
			l_TransactionData.setCurrencyRate(crCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(pTransferData.getAmount());
			if (!pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY())) {
				l_TransactionData.setAmount(
						StrUtil.round2decimals(getTransAmount(pTransferData.getAmount(), drCCYRate, crCCYRate, "Dr")));
			}
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
			l_TransactionData
					.setDescription(pTransferData.getFromAccNumber() + ", Mobile  Transfer" + ", " + l_TrDateTime);
			l_TransactionData.setTransactionType(mTrCrTransType);
			l_TransactionData.setSubRef(pTransferData.getReferenceNo());
			l_TransactionData.setCashInHandGL(toproduct.getCashInHandGL());
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("");
			l_TransactionData.setProductGL(toproduct.getProductGL());
			l_TransactionList.add(l_TransactionData);
			if (pTransferData.getTransactionFee() > 0) {
				// Cr To Income GL
				l_TransactionData = new TransactionData();
				l_TransactionData.setGl(true);
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(l_ComGL);
				l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(pTransferData.getFromBranchCode());
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData.getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData.getRemark());
				l_TransactionData.setAmount(pTransferData.getAmount());
				l_TransactionData
						.setDescription(pTransferData.getFromAccNumber() + ", Mobile  Transfer" + ", " + l_TrDateTime);
				l_TransactionData.setTransactionType(mTrCrTransType);
				l_TransactionData.setSubRef(pTransferData.getReferenceNo());
				l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("GL");
				l_TransactionData.setProductGL("");
				l_TransactionList.add(l_TransactionData);
			}
		}

		return l_TransactionList;
	}

	public static double getTransAmount(double aTrAmt, double aDrCurRate, double aCrCurRate, String aIsDrAmt) {
		double l_TransAmount = 0;
		if (aIsDrAmt.equalsIgnoreCase("Dr")) {
			l_TransAmount = (aTrAmt * aDrCurRate) / aCrCurRate;
		} else {
			l_TransAmount = (aTrAmt * aCrCurRate) / aDrCurRate;
		}
		if (l_TransAmount == Double.NaN) {
			l_TransAmount = 0.0;
		}
		return l_TransAmount;
	}

	private ArrayList<TransactionData> prepareIBTTransfer_Centralized(TransferData pTransferData,
			String p_TransactionDate, String p_EffectiveDate, ArrayList<String> l_ArlLogString, Connection pConn,
			String l_TransactionTime, String l_TrDateTime, String pByForceCheque, ProductData fromproduct,
			ProductData toproduct, Connection conn) throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String l_IBTGLFrom = "";
		String l_IBTGLTo = "";
		String l_FromBranch = pTransferData.getFromBranchCode();
		String l_ToBranch = pTransferData.getToBranchCode();

		String fkey = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
		String tkey = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();

		double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);
		double crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey);

		// double drCCYRate =
		// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData.getFromCCY(),
		// "smTransfer",
		// pTransferData.getFromBranchCode(), conn);
		// double crCCYRate =
		// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData.getToCCY(),
		// "smTransfer",
		// pTransferData.getToBranchCode(), conn);
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		/*
		 * l_RefData = DBSystemMgr.getReferenceAccCode( "MTR" +
		 * pTransferData.getFromCCY() + pTransferData.getFromBranchCode() +
		 * fromproduct.getProductCode()); l_IBTGLFrom = l_RefData.getGLCode(); l_RefData
		 * = DBSystemMgr.getReferenceAccCode( "MTR" + pTransferData.getToCCY() +
		 * pTransferData.getToBranchCode() + toproduct.getProductCode()); l_IBTGLTo =
		 * l_RefData.getGLCode(); l_RefData = DBSystemMgr.getReferenceAccCode("MTRCOM" +
		 * pTransferData.getCurrencyCode() + pTransferData.getFromBranchCode() +
		 * fromproduct.getProductCode()); String l_ComGL = l_RefData.getGLCode();
		 */
		String l_ComGL = "";
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(
				"MTR" + pTransferData.getFromCCY() + pTransferData.getFromBranchCode() + fromproduct.getProductCode());
		if (l_RefData != null)
			l_IBTGLFrom = l_RefData.getGLCode();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
				.get("MTR" + pTransferData.getToCCY() + pTransferData.getToBranchCode() + toproduct.getProductCode());
		if (l_RefData != null)
			l_IBTGLTo = l_RefData.getGLCode();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRCOM"
				+ pTransferData.getCurrencyCode() + pTransferData.getFromBranchCode() + fromproduct.getProductCode());
		if (l_RefData != null)
			l_ComGL = l_RefData.getGLCode();

		double totalfee = pTransferData.getAmount() + pTransferData.getTransactionFee();
		if (totalfee > 0) {
			// Dr From Customer
			l_TransactionData = new TransactionData();
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getFromAccNumber());
			if (fromproduct.hasCheque())
				l_TransactionData.setReferenceNumber(pByForceCheque);
			else
				l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_FromBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getFromCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(totalfee);
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getToAccNumber() + ", Mobile Transfer, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrDrTransType);
			l_TransactionData.setSubRef("");
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("");
			l_TransactionData.setProductGL(fromproduct.getProductGL());
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			// Cr IBT GL
			l_TransactionData = new TransactionData();
			l_TransactionData.setGl(true);
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(l_IBTGLFrom);
			l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_FromBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getFromCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(pTransferData.getAmount());
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getFromAccNumber() + ", Mobile Transfer, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrCrTransType);
			l_TransactionData.setSubRef(pTransferData.getTraceNumber());
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("GL");
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			if (pTransferData.getTransactionFee() > 0) {
				// Cr Com GL
				l_TransactionData = new TransactionData();
				l_TransactionData.setGl(true);
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(l_ComGL);
				l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(l_FromBranch);
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData.getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData.getRemark());
				l_TransactionData.setAmount(pTransferData.getTransactionFee());
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData.getTransactionFee() * drCCYRate));
				l_TransactionData
						.setDescription(pTransferData.getFromAccNumber() + ", Mobile Transfer, " + l_TrDateTime);
				l_TransactionData.setTransactionType(mDTrCrTransType);
				l_TransactionData.setSubRef(pTransferData.getTraceNumber());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("GL");
				l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
				l_TransactionList.add(l_TransactionData);
			}
			// Dr IBT GL
			l_TransactionData = new TransactionData();
			l_TransactionData.setGl(true);
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(l_IBTGLTo);
			l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_ToBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getToCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(pTransferData.getAmount());
			if (!pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY())) {
				l_TransactionData.setAmount(
						StrUtil.round2decimals(getTransAmount(pTransferData.getAmount(), drCCYRate, crCCYRate, "Dr")));
			}
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getToAccNumber() + ", Mobile Transfer, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrDrTransType);
			l_TransactionData.setSubRef("");
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("GL");
			l_TransactionData.setCashInHandGL(toproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			// Cr To Account
			l_TransactionData = new TransactionData();
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getToAccNumber());
			l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_ToBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getToCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(pTransferData.getAmount());
			if (!pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY())) {
				l_TransactionData.setAmount(
						StrUtil.round2decimals(getTransAmount(pTransferData.getAmount(), drCCYRate, crCCYRate, "Dr")));
			}
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getFromAccNumber() + ", Mobile Transfer, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrCrTransType);
			l_TransactionData.setSubRef(pTransferData.getTraceNumber());
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("");
			l_TransactionData.setCashInHandGL(toproduct.getCashInHandGL());
			l_TransactionData.setProductGL(toproduct.getProductGL());
			l_TransactionList.add(l_TransactionData);
		}

		return l_TransactionList;
	}

	public gopaymentresponse dopaymentTransfer(TransferData[] pTransferData, int type) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		HashMap<String, AccountData> l_accDataList = new HashMap<String, AccountData>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		Connection l_Conn1 = null;
		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();
		String fkey = "", tkey = "";
		// // by
		// reference
		if (pTransferData[0].isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		Connection l_Conn = ConnAdmin.getConn3();
		for (int i = 0; i < pTransferData.length; i++) {
			l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData[i].getFromAccNumber(), l_Conn));
			if (l_DataResult.getStatus()) {
				l_FromAccountData = l_AccDAO.getAccountsBean();
				pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
				pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
				l_accDataList.put(pTransferData[i].getFromAccNumber(), l_FromAccountData);
				// -------check status
			} else {
				res.setRetcode("210");
				res.setRetmessage("Invalid From Account No.");
				return res;
			}

			if (l_DataResult.getStatus()) {
				l_DataResult = checkAccount(l_FromAccountData.getProduct(), l_FromAccountData.getProduct(),
						l_FromAccountData.getAccountNumber(), l_FromAccountData.getAccountNumber(), l_Conn);
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
				}
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);

				// fromproduct = prddao.getTProduct(l_FromAccountData.getProduct(),
				// l_FromAccountData.getType(), l_Conn);
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
				pTransferData[i].setFromproduct(fromproduct);
				pTransferData[i].setpByForceCheque(pByForceCheque);
			}
			if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData[i].getToAccNumber(), l_Conn)) {
				l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData[i].getToAccNumber(), l_Conn));
				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					pTransferData[i].setToBranchCode(l_ToAccountData.getBranchCode());
					l_accDataList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
					// ---- check status
				} else {
					res.setRetcode("210");
					res.setRetmessage("Invalid To Account No.");
					return res;
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = checkAccount(l_ToAccountData.getProduct(), l_ToAccountData.getProduct(),
							l_ToAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn);
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
					} else {
						tkey = l_ToAccountData.getProduct();
					}
					toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
					// toproduct = prddao.getTProduct(l_ToAccountData.getProduct(),
					// l_ToAccountData.getType(), l_Conn);
					pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
					pTransferData[i].setToproduct(toproduct);
					pTransferData[i].setpByForceCheque(pByForceCheque);
				}
			} else {
				if (l_DataResult.getStatus()) {
					pTransferData[i].setToAccNumber("(GL)" + pTransferData[i].getToAccNumber());
				}
			}
		}
		try {

			if (l_DataResult.getStatus()) {
				l_TransactionList = prepareTransferPaymentTopup(pTransferData, l_TodayDate, l_EffectiveDate,
						l_ArlLogString, l_TransactionTime, l_TrDateTime, type, l_Conn);

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// End Preparation
				// Check Transaction and post link
				// Check Transaction Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						pAccData = l_accDataList.get(l_TransactionList.get(i).getAccountNumber());
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}

				}
				if (!l_Conn.isClosed()) {
					l_Conn.close();
				}
				if (l_DataResult.getStatus()) {
					l_Conn1 = ConnAdmin.getConn();
					l_Conn1.setAutoCommit(false);
					l_ArlLogString.add("	Post Transaction ");

					l_DataResult = DBTransactionMgr.postNew(l_TransactionList, l_AccGLList, l_Conn1);
					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_Conn1.commit();
						res.setRetcode("300");
						String transDate = GeneralUtil.datetoString();
						res.setTransdate(GeneralUtil.mobiledateformat(transDate));
						res.setBankrefnumber(String.valueOf(l_DataResult.getTransactionNumber()));
						if (type == 1)
							res.setRetmessage("Payment Topup Successfully");
						else if (type == 2)
							res.setRetmessage("Merchant Payment Successfully");
					} else {
						res.setRetcode("210");
						res.setRetmessage(l_DataResult.getDescription());
						l_Conn1.rollback();
					}

					if (!l_Conn1.isClosed() || l_Conn1 != null) {
						l_Conn1.close();
					}
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(l_DataResult.getDescription());
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}
		return res;
	}

	public gopaymentresponse dopayment(TransferData[] pTransferData, String paymentType) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		HashMap<String, AccountData> l_accDataList = new HashMap<String, AccountData>();

		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		Connection l_Conn1 = null;
		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();
		// // by
		// reference
		if (pTransferData[0].isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		Connection l_Conn = ConnAdmin.getConn3();
		for (int i = 0; i < pTransferData.length; i++) {
			if (!(pTransferData[i].getFromAccNumber().equals("")
					|| pTransferData[i].getFromAccNumber().equals("null"))) {
				l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData[i].getFromAccNumber(), l_Conn));
				if (l_DataResult.getStatus()) {
					l_FromAccountData = l_AccDAO.getAccountsBean();
					pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
					pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
					pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
					l_accDataList.put(pTransferData[i].getFromAccNumber(), l_FromAccountData);
				} else {
					res.setRetcode("210");
					res.setRetmessage("Invalid From Account No.");
					return res;
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = checkAccount(l_FromAccountData.getProduct(), l_FromAccountData.getProduct(),
							l_FromAccountData.getAccountNumber(), l_FromAccountData.getAccountNumber(), l_Conn);

					String fkey = "";
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
					} else {
						fkey = l_FromAccountData.getProduct();
					}
					fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);

					// fromproduct = prddao.getTProduct(l_FromAccountData.getProduct(),
					// l_FromAccountData.getType(), l_Conn);
					pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
					pTransferData[i].setFromproduct(fromproduct);
					pTransferData[i].setpByForceCheque(pByForceCheque);
				}
			}

			if (!(pTransferData[i].getToAccNumber().equals("") || pTransferData[i].getToAccNumber().equals("null"))) {
				if (!l_AccDAO.isGL(pTransferData[i].getToAccNumber(), l_Conn)) {
					l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData[i].getToAccNumber(), l_Conn));
					if (l_DataResult.getStatus()) {
						l_ToAccountData = l_AccDAO.getAccountsBean();
						pTransferData[i].setToBranchCode(l_ToAccountData.getBranchCode());
						pTransferData[i].setToCCY(l_FromAccountData.getCurrencyCode());
						l_accDataList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
					} else {
						res.setRetcode("210");
						res.setRetmessage("Invalid To Account No.");
						return res;
					}
					if (l_DataResult.getStatus()) {
						l_DataResult = checkAccount(l_ToAccountData.getProduct(), l_ToAccountData.getProduct(),
								l_ToAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn);

						String tkey = "";
						if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
							tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
						} else {
							tkey = l_ToAccountData.getProduct();
						}
						toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

						// toproduct = prddao.getTProduct(l_ToAccountData.getProduct(),
						// l_ToAccountData.getType(), l_Conn);
						pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
						pTransferData[i].setToproduct(toproduct);
						pTransferData[i].setpByForceCheque(pByForceCheque);
					}
				} else {
					pTransferData[i].setToAccNumber("(GL)" + pTransferData[i].getToAccNumber());
				}
			}

		}
		try {

			if (l_DataResult.getStatus()) {
				l_TransactionList = prepareTransferPayment(pTransferData, l_TodayDate, l_EffectiveDate, l_ArlLogString,
						l_TransactionTime, l_TrDateTime, paymentType, l_Conn);

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// End Preparation
				// Check Transaction and post link
				// Check Transaction Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						pAccData = l_accDataList.get(l_TransactionList.get(i).getAccountNumber());
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}

				}
				if (!l_Conn.isClosed()) {
					l_Conn.close();
				}
				if (l_DataResult.getStatus()) {
					l_Conn1 = ConnAdmin.getConn();
					l_Conn1.setAutoCommit(false);
					l_ArlLogString.add("	Post Transaction ");

					l_DataResult = DBTransactionMgr.postMulti(l_TransactionList, l_AccGLList, l_Conn1);
					// l_DataResult = DBTransactionMgr.postNew(l_TransactionList,l_AccGLList,
					// l_Conn1);
					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_Conn1.commit();
						res.setRetcode("300");
						String transDate = GeneralUtil.datetoString();
						res.setTransdate(GeneralUtil.mobiledateformat(transDate));
						res.setBankrefnumber(String.valueOf(l_DataResult.getTransactionNumber()));
						res.setRetmessage("Payment Successfully");
					} else {
						res.setRetcode("210");
						res.setRetmessage(l_DataResult.getDescription());
						l_Conn1.rollback();
					}

					if (!l_Conn1.isClosed() || l_Conn1 != null) {
						l_Conn1.close();
					}
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(l_DataResult.getDescription());
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}
		return res;
	}

	private ArrayList<TransactionData> prepareTransferPaymentTopup(TransferData[] pTransferData,
			String p_TransactionDate, String p_EffectiveDate, ArrayList<String> l_ArlLogString,
			String l_TransactionTime, String l_TrDateTime, int type, Connection conn) throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String desc = "";
		if (type == 1) {// Payment topup
			desc = "Mobile Top Up";
		} else if (type == 2) {// Payment topup
			desc = "Merchant Payment";
		}
		for (int i = 0; i < pTransferData.length; i++) {
			String fkey = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_"
					+ pTransferData[i].getFromBranchCode();

			double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);

			// double drCCYRate =
			// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getFromCCY(),
			// "smTransfer",
			// pTransferData[i].getFromBranchCode(), conn);

			double totalfee = pTransferData[i].getAmount();
			if (totalfee > 0) {
				// Dr From Customer
				l_TransactionData = new TransactionData();
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(pTransferData[i].getFromAccNumber());
				if (pTransferData[i].getFromproduct().hasCheque())
					l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
				else
					l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(pTransferData[i].getFromBranchCode());
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData[i].getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData[i].getRemark());
				l_TransactionData.setAmount(totalfee);
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
				l_TransactionData.setDescription(desc + ", " + pTransferData[i].getRemark() + ", "
						+ pTransferData[i].getToAccNumber() + ", " + l_TrDateTime);
				l_TransactionData.setTransactionType(mPaymentDrTransType);
				l_TransactionData.setSubRef("");
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("");
				l_TransactionData.setProductGL(pTransferData[i].getFromproduct().getProductGL());
				l_TransactionData.setCashInHandGL(pTransferData[i].getFromproduct().getCashInHandGL());
				l_TransactionList.add(l_TransactionData);

				// Cr IBT GL
				l_TransactionData = new TransactionData();
				if (pTransferData[i].getToAccNumber().startsWith("(GL)")) {
					pTransferData[i].setToAccNumber(pTransferData[i].getToAccNumber().replace("(GL)", ""));
					l_TransactionData.setGl(true);
				}
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(pTransferData[i].getToAccNumber());
				if (pTransferData[i].getToproduct() != null) {
					if (pTransferData[i].getToproduct().hasCheque())
						l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
					else
						l_TransactionData.setReferenceNumber("");
				} else {
					l_TransactionData.setReferenceNumber("");
				}

				l_TransactionData.setBranchCode(pTransferData[i].getFromBranchCode());
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData[i].getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData[i].getRemark());
				l_TransactionData.setAmount(totalfee);
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
				l_TransactionData.setDescription(desc + ", " + pTransferData[i].getRemark() + ", "
						+ pTransferData[i].getFromAccNumber() + ", " + l_TrDateTime);
				l_TransactionData.setTransactionType(mPaymentCrTransType);
				l_TransactionData.setSubRef(pTransferData[i].getTraceNumber());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("");
				if (pTransferData[i].getToproduct() != null) {
					l_TransactionData.setProductGL(pTransferData[i].getToproduct().getProductGL());
					l_TransactionData.setCashInHandGL(pTransferData[i].getToproduct().getCashInHandGL());
				}
				l_TransactionList.add(l_TransactionData);

			}
		}
		return l_TransactionList;
	}

	private ArrayList<TransactionData> prepareTransferPayment(TransferData[] pTransferData, String p_TransactionDate,
			String p_EffectiveDate, ArrayList<String> l_ArlLogString, String l_TransactionTime, String l_TrDateTime,
			String paymentType, Connection conn) throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String desc = "";
		desc = "Bulk Payment";
		String fromacc = "";

		for (int i = 0; i < pTransferData.length; i++) {
			if (paymentType.equals("10")) {
				if (!(pTransferData[i].getFromAccNumber().equals("")
						|| pTransferData[i].getFromAccNumber().equals("null"))) {

					String fkey = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_"
							+ pTransferData[i].getFromBranchCode();

					double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);

					// double drCCYRate =
					// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getFromCCY(),
					// "smTransfer",
					// pTransferData[i].getFromBranchCode(), conn);

					double totalfee = pTransferData[i].getAmount();
					fromacc = pTransferData[i].getFromAccNumber();
					if (totalfee > 0) {
						// Dr From Customer
						l_TransactionData = new TransactionData();
						l_TransactionData.setIsMobile(true);
						l_TransactionData.setAccountNumber(pTransferData[i].getFromAccNumber());
						if (pTransferData[i].getFromproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
						l_TransactionData.setBranchCode(pTransferData[i].getFromBranchCode());
						l_TransactionData.setWorkstation("Mobile");
						l_TransactionData.setUserID(pTransferData[i].getUserID());
						l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
						l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
						l_TransactionData.setCurrencyRate(drCCYRate);
						l_TransactionData.setTransactionDate(p_TransactionDate);
						l_TransactionData.setTransactionTime(l_TransactionTime);
						l_TransactionData.setEffectiveDate(p_EffectiveDate);
						l_TransactionData.setContraDate("19000101");
						l_TransactionData.setStatus((byte) 1);
						l_TransactionData.setRemark(pTransferData[i].getRemark());
						l_TransactionData.setAmount(totalfee);
						l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
						l_TransactionData
								.setDescription(desc + ", " + pTransferData[i].getReferenceNo() + ", " + l_TrDateTime);
						l_TransactionData.setTransactionType(mPaymentDrTransType);
						l_TransactionData.setSubRef("");
						l_TransactionData.setAccRef("");
						l_TransactionData.setT1("");
						l_TransactionData.setProductGL(pTransferData[i].getFromproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getFromproduct().getCashInHandGL());
						l_TransactionList.add(l_TransactionData);
					}
				}
				if (!(pTransferData[i].getToAccNumber().equals("")
						|| pTransferData[i].getToAccNumber().equals("null"))) {

					String tkey = "smTransfer" + "_" + pTransferData[i].getToCCY() + "_"
							+ pTransferData[i].getToBranchCode();
					double crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey);

					// double crCCYRate =
					// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getToCCY(),
					// "smTransfer",
					// pTransferData[i].getToBranchCode(), conn);
					// Cr IBT GL
					l_TransactionData = new TransactionData();
					if (pTransferData[i].getToAccNumber().startsWith("(GL)")) {
						pTransferData[i].setToAccNumber(pTransferData[i].getToAccNumber().replace("(GL)", ""));
						l_TransactionData.setGl(true);
					}
					l_TransactionData.setIsMobile(true);
					l_TransactionData.setAccountNumber(pTransferData[i].getToAccNumber());
					if (pTransferData[i].getToproduct() != null) {
						if (pTransferData[i].getToproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
					} else {
						l_TransactionData.setReferenceNumber("");
					}

					l_TransactionData.setBranchCode(pTransferData[i].getToBranchCode());
					l_TransactionData.setWorkstation("Mobile");
					l_TransactionData.setUserID(pTransferData[i].getUserID());
					l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
					l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
					l_TransactionData.setCurrencyRate(crCCYRate);
					l_TransactionData.setTransactionDate(p_TransactionDate);
					l_TransactionData.setTransactionTime(l_TransactionTime);
					l_TransactionData.setEffectiveDate(p_EffectiveDate);
					l_TransactionData.setContraDate("19000101");
					l_TransactionData.setStatus((byte) 1);
					l_TransactionData.setRemark(pTransferData[i].getRemark());
					l_TransactionData.setAmount(pTransferData[i].getAmount());
					l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * crCCYRate));
					l_TransactionData.setDescription(
							desc + ", " + fromacc + ", " + pTransferData[i].getRemark() + ", " + l_TrDateTime);
					l_TransactionData.setTransactionType(mPaymentCrTransType);
					l_TransactionData.setSubRef(pTransferData[i].getTraceNumber());
					l_TransactionData.setAccRef("");
					l_TransactionData.setT1("");
					if (pTransferData[i].getToproduct() != null) {
						l_TransactionData.setProductGL(pTransferData[i].getToproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getToproduct().getCashInHandGL());
					}
					l_TransactionList.add(l_TransactionData);
				}
			} else if (paymentType.equals("20")) {
				if (!(pTransferData[i].getToAccNumber().equals("")
						|| pTransferData[i].getToAccNumber().equals("null"))) {
					double drCCYRate = DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getToCCY(),
							"smTransfer", pTransferData[i].getToBranchCode(), conn);
					fromacc = pTransferData[pTransferData.length - 1].getFromAccNumber();
					// Dr IBT GL
					l_TransactionData = new TransactionData();
					if (pTransferData[i].getToAccNumber().startsWith("(GL)")) {
						pTransferData[i].setToAccNumber(pTransferData[i].getToAccNumber().replace("(GL)", ""));
						l_TransactionData.setGl(true);
					}
					l_TransactionData.setIsMobile(true);
					l_TransactionData.setAccountNumber(pTransferData[i].getToAccNumber());
					if (pTransferData[i].getToproduct() != null) {
						if (pTransferData[i].getToproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
					} else {
						l_TransactionData.setReferenceNumber("");
					}

					l_TransactionData.setBranchCode(pTransferData[i].getToBranchCode());
					l_TransactionData.setWorkstation("Mobile");
					l_TransactionData.setUserID(pTransferData[i].getUserID());
					l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
					l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
					l_TransactionData.setCurrencyRate(drCCYRate);
					l_TransactionData.setTransactionDate(p_TransactionDate);
					l_TransactionData.setTransactionTime(l_TransactionTime);
					l_TransactionData.setEffectiveDate(p_EffectiveDate);
					l_TransactionData.setContraDate("19000101");
					l_TransactionData.setStatus((byte) 1);
					l_TransactionData.setRemark(pTransferData[i].getRemark());
					l_TransactionData.setAmount(pTransferData[i].getAmount());
					l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * drCCYRate));
					l_TransactionData.setDescription(
							desc + ", " + fromacc + ", " + pTransferData[i].getRemark() + ", " + l_TrDateTime);
					l_TransactionData.setTransactionType(mPaymentDrTransType);
					l_TransactionData.setSubRef(pTransferData[i].getTraceNumber());
					l_TransactionData.setAccRef("");
					l_TransactionData.setT1("");
					if (pTransferData[i].getToproduct() != null) {
						l_TransactionData.setProductGL(pTransferData[i].getToproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getToproduct().getCashInHandGL());
					}
					l_TransactionList.add(l_TransactionData);
				}
				if (!(pTransferData[i].getFromAccNumber().equals("")
						|| pTransferData[i].getFromAccNumber().equals("null"))) {
					double crCCYRate = DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getFromCCY(),
							"smTransfer", pTransferData[i].getFromBranchCode(), conn);

					double totalfee = pTransferData[i].getAmount();
					if (totalfee > 0) {
						// Cr From Customer
						l_TransactionData = new TransactionData();
						l_TransactionData.setIsMobile(true);
						l_TransactionData.setAccountNumber(pTransferData[i].getFromAccNumber());
						if (pTransferData[i].getFromproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
						l_TransactionData.setBranchCode(pTransferData[i].getFromBranchCode());
						l_TransactionData.setWorkstation("Mobile");
						l_TransactionData.setUserID(pTransferData[i].getUserID());
						l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
						l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
						l_TransactionData.setCurrencyRate(crCCYRate);
						l_TransactionData.setTransactionDate(p_TransactionDate);
						l_TransactionData.setTransactionTime(l_TransactionTime);
						l_TransactionData.setEffectiveDate(p_EffectiveDate);
						l_TransactionData.setContraDate("19000101");
						l_TransactionData.setStatus((byte) 1);
						l_TransactionData.setRemark(pTransferData[i].getRemark());
						l_TransactionData.setAmount(totalfee);
						l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * crCCYRate));
						l_TransactionData
								.setDescription(desc + ", " + pTransferData[i].getReferenceNo() + ", " + l_TrDateTime);
						l_TransactionData.setTransactionType(mPaymentCrTransType);
						l_TransactionData.setSubRef("");
						l_TransactionData.setAccRef("");
						l_TransactionData.setT1("");
						l_TransactionData.setProductGL(pTransferData[i].getFromproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getFromproduct().getCashInHandGL());
						l_TransactionList.add(l_TransactionData);
					}
				}
			}

		}
		return l_TransactionList;
	}

	public SMSReturnData openDepositAcc(TransferData pTransferData, Connection l_Conn1) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		DAOManager l_DAOManager = new DAOManager();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		HashMap<String, AccountData> l_accDataList = new HashMap<String, AccountData>();

		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		boolean isSameBranch = false;
		boolean isGL = false;
		double trFee = 0;

		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();

		if (pTransferData.isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getFromAccNumber(), l_Conn1));
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			l_accDataList.put(pTransferData.getFromAccNumber(), l_FromAccountData);
		} else {
			l_SMSReturnData.setDescription("Invalid From Account No.");
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setCode("0014");
			return l_SMSReturnData;
		}
		if (l_DataResult.getStatus()) {
			l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getToAccNumber(), l_Conn1));
			if (l_DataResult.getStatus()) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
				pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
				l_accDataList.put(pTransferData.getToAccNumber(), l_ToAccountData);
			} else {
				l_SMSReturnData.setDescription("Invalid To Account No.");
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		}
		pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
		pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
		pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());
		if (l_FromAccountData.getBranchCode().equalsIgnoreCase(l_ToAccountData.getBranchCode())) {
			isSameBranch = true;
		}
		try {
			if (l_DataResult.getStatus()) {
				l_DataResult = checkAccountFD(l_FromAccountData.getProduct(), l_ToAccountData.getProduct(),
						l_FromAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn1);
			}
			if (l_DataResult.getStatus()) {
				String fkey = "", tkey = "";
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
					tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
					tkey = l_ToAccountData.getProduct();
				}
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
				toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

				// fromproduct = prddao.getTProduct(l_FromAccountData.getProduct(),
				// l_FromAccountData.getType(), l_Conn1);
				// toproduct = prddao.getTProduct(l_ToAccountData.getProduct(),
				// l_ToAccountData.getType(), l_Conn1);
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
				// get commission
				// trFee = getMobileTransferCharges(pTransferData.getAmount(),
				// pTransferData.getDebitIBTKey(),
				// l_FromAccountData.getBranchCode(), l_ToAccountData.getBranchCode(),
				// l_FromAccountData.getZone(),
				// l_ToAccountData.getZone());
				pTransferData.setTransactionFee(trFee);
				// Preparation
				if (isSameBranch) {
					l_TransactionList = prepareTransferSameBranchWithoutComm(pTransferData, l_TodayDate,
							l_EffectiveDate, l_ArlLogString, "", l_TransactionTime, l_TrDateTime, pByForceCheque,
							fromproduct, toproduct, l_Conn1);
				} else {
					l_TransactionList = prepareIBTTransfer_Centralized(pTransferData, l_TodayDate, l_EffectiveDate,
							l_ArlLogString, l_Conn1, l_TransactionTime, l_TrDateTime, pByForceCheque, fromproduct,
							toproduct, l_Conn1);
				}

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn1);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// Check Account Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn1);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						pAccData = l_accDataList.get(l_TransactionList.get(i).getAccountNumber());
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}
				}
				if (l_DataResult.getStatus()) {
					l_ArlLogString.add("	Post Transaction ");

					l_DataResult = DBTransactionMgr.postNew(l_TransactionList, l_AccGLList, l_Conn1);
					if (l_DataResult.getStatus()) {
						FixedDepositAccountData l_fixedAccData = new FixedDepositAccountData();
						AccountsDAO l_fixedAccDAO = new AccountsDAO(l_Conn1);
						l_fixedAccData.setAccountNumber(l_ToAccountData.getAccountNumber());
						l_fixedAccData.setLastTransDate(l_SMSReturnData.getEffectiveDate());
						l_fixedAccData.setStatus((byte) 1);
						if (l_fixedAccDAO.updateFixedAccountData(l_fixedAccData, l_Conn1)) {
							l_DataResult.setStatus(true);
						} else {
							l_DataResult.setStatus(false);
							l_SMSReturnData.setDescription("Error in update Fixed Deposit Account");
						}
					}
					if (l_DataResult.getStatus()) {
						l_SMSReturnData.setStatus(true);
						l_SMSReturnData.setCode("0000");
						l_SMSReturnData.setTransactionNumber(l_DataResult.getTransactionNumber());
						l_SMSReturnData.setComm1(trFee);
						l_SMSReturnData.setCcy(pTransferData.getCurrencyCode());
						l_SMSReturnData.setDescription("Posted successfully");
					} else {
						l_SMSReturnData.setStatus(false);
						l_SMSReturnData.setCode("0014");
						if (isLink) {
							l_SMSReturnData.setDescription(
									l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
						} else {
							l_SMSReturnData.setDescription(l_DataResult.getDescription());
						}
					}
				}
			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}
		return l_SMSReturnData;
	}

	private DataResult checkAccountFD(String pFromProduct, String pToProduct, String pFromAccount, String pToAccount,
			Connection pConn) throws Exception {
		DataResult l_Result = new DataResult();
		// Checking Account is Exist Or Not
		l_Result.setStatus(true);
		if (GeneralUtility.isCardAccount(pFromAccount)) {
			l_Result.setStatus(true);
		}
		if (l_Result.getStatus()) {
			if (SharedLogic.isFD(pFromProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Fixed Deposit Account.");
			}
		}
		if (l_Result.getStatus()) {
			if (SharedLogic.isLoan(pFromProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Loan Account.");
			}
		}
		if (GeneralUtility.isCardAccount(pToAccount)) {
			l_Result.setStatus(true);
		}

		if (l_Result.getStatus()) {
			if (SharedLogic.isLoan(pToProduct)) {
				l_Result.setStatus(false);
				l_Result.setErrorFlag(1);
				l_Result.setDescription("Not Allowed Loan Account.");
			}
		}
		return l_Result;
	}

	public SMSReturnData doOtherBankTransfer(TransferData pTransferData) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		DAOManager l_DAOManager = new DAOManager();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		HashMap<String, AccountData> l_accDataList = new HashMap<String, AccountData>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		boolean isSameBranch = false;
		boolean isGL = false;
		double trFee = 0;
		Connection l_Conn1 = null;
		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();
		// // by
		// reference
		if (pTransferData.isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		Connection l_Conn = ConnAdmin.getConn3();
		l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getFromAccNumber(), l_Conn));
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			l_accDataList.put(pTransferData.getFromAccNumber(), l_FromAccountData);
		} else {
			l_SMSReturnData.setDescription("Invalid From Account No.");
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setCode("0014");
			return l_SMSReturnData;
		}

		pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
		pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
		try {
			if (l_DataResult.getStatus()) {
				l_DataResult = checkAccount(l_FromAccountData.getProduct(), l_FromAccountData.getProduct(),
						l_FromAccountData.getAccountNumber(), l_FromAccountData.getAccountNumber(), l_Conn);

				String fkey = "";
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
				}
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);

				// fromproduct = prddao.getTProduct(l_FromAccountData.getProduct(),
				// l_FromAccountData.getType(), l_Conn);
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
				pTransferData.setFromproduct(fromproduct);
				pTransferData.setpByForceCheque(pByForceCheque);
			}
			if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData.getToAccNumber(), l_Conn)) {
				l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getToAccNumber(), l_Conn));
				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
					l_accDataList.put(pTransferData.getToAccNumber(), l_ToAccountData);
				} else {
					l_SMSReturnData.setCode("210");
					l_SMSReturnData.setDescription("Invalid To Account No.");
					l_SMSReturnData.setStatus(false);
					return l_SMSReturnData;
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = checkAccount(l_ToAccountData.getProduct(), l_ToAccountData.getProduct(),
							l_ToAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn);

					String tkey = "";
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
					} else {
						tkey = l_ToAccountData.getProduct();
					}
					toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

					// toproduct = prddao.getTProduct(l_ToAccountData.getProduct(),
					// l_ToAccountData.getType(), l_Conn);
					pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
					pTransferData.setToproduct(toproduct);
					pTransferData.setpByForceCheque(pByForceCheque);
				}
			} else {
				if (l_DataResult.getStatus()) {
					pTransferData.setToBranchCode(pTransferData.getFromBranchCode());
					pTransferData.setToAccNumber("(GL)" + pTransferData.getToAccNumber());
				}
			}
			if (l_DataResult.getStatus()) {
				l_ToAccountData.setBranchCode(pTransferData.getToBranchCode());
				l_ToAccountData.setZone(Integer.parseInt(pTransferData.getToZone()));

				l_TransactionList = prepareTransferOBT(pTransferData, l_TodayDate, l_EffectiveDate, l_ArlLogString,
						l_Conn, l_TransactionTime, l_TrDateTime, pByForceCheque, fromproduct, toproduct, l_Conn);

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// End Preparation
				// Check Transaction and post link
				// Check Transaction Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						pAccData = l_accDataList.get(l_TransactionList.get(i).getAccountNumber());
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}
				}
				if (!l_Conn.isClosed()) {
					l_Conn.close();
				}
				if (l_DataResult.getStatus()) {
					l_Conn1 = ConnAdmin.getConn();
					l_Conn1.setAutoCommit(false);
					l_ArlLogString.add("	Post Transaction ");

					l_DataResult = DBTransactionMgr.postNew(l_TransactionList, l_AccGLList, l_Conn1);
					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_Conn1.commit();
						l_SMSReturnData.setStatus(true);
						l_SMSReturnData.setCode("0000");
						l_SMSReturnData.setTransactionNumber(l_DataResult.getTransactionNumber());
						l_SMSReturnData.setComm1(trFee);
						l_SMSReturnData.setCcy(pTransferData.getCurrencyCode());
						l_SMSReturnData.setDescription("Posted successfully");
					} else {
						l_SMSReturnData.setStatus(false);
						l_SMSReturnData.setCode("0014");
						if (isLink) {
							l_SMSReturnData.setDescription(
									l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
						} else {
							l_SMSReturnData.setDescription(l_DataResult.getDescription());
						}
						l_Conn1.rollback();
					}

					if (!l_Conn1.isClosed() || l_Conn1 != null) {
						l_Conn1.close();
					}
				}
			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}
		return l_SMSReturnData;
	}

	private ArrayList<TransactionData> prepareTransferOBT(TransferData pTransferData, String p_TransactionDate,
			String p_EffectiveDate, ArrayList<String> l_ArlLogString, Connection pConn, String l_TransactionTime,
			String l_TrDateTime, String pByForceCheque, ProductData fromproduct, ProductData toproduct, Connection conn)
			throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String l_FromBranch = pTransferData.getFromBranchCode();
		String l_ToBranch = pTransferData.getToBranchCode();

		String fkey = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();

		double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);

		// double drCCYRate =
		// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData.getFromCCY(),
		// "smTransfer",
		// pTransferData.getFromBranchCode(), conn);

		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		/*
		 * l_RefData = DBSystemMgr.getReferenceAccCode("CCTCOM1" +
		 * pTransferData.getCurrencyCode() + 1 + pTransferData.getFromBranchCode());
		 * String l_Com1GL = l_RefData.getGLCode();
		 * 
		 * l_RefData = DBSystemMgr.getReferenceAccCode("CCTCOM2" +
		 * pTransferData.getCurrencyCode() + pTransferData.getToBankCode() +
		 * pTransferData.getToBranchCode()); String l_Com2GL = l_RefData.getGLCode();
		 */

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
				.get("CCTCOM1" + pTransferData.getCurrencyCode() + 1 + pTransferData.getFromBranchCode());
		String l_Com1GL = "";
		if (l_RefData != null)
			l_Com1GL = l_RefData.getGLCode();

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("CCTCOM2"
				+ pTransferData.getCurrencyCode() + pTransferData.getToBankCode() + pTransferData.getToBranchCode());
		String l_Com2GL = "";
		if (l_RefData != null)
			l_Com2GL = l_RefData.getGLCode();

		double totalfee = 0, amount = 0, comm1Amt = 0, comm2Amt = 0;
		amount = pTransferData.getAmount();
		comm1Amt = pTransferData.getComm1();
		comm2Amt = pTransferData.getComm2();
		if (pTransferData.isInclusive())
			amount = amount - (comm1Amt + comm2Amt);

		totalfee = amount + comm1Amt + comm2Amt;

		if (totalfee > 0 && amount > 0) {
			// Dr From Customer
			l_TransactionData = new TransactionData();
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getFromAccNumber());
			if (fromproduct.hasCheque())
				l_TransactionData.setReferenceNumber(pByForceCheque);
			else
				l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_FromBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getFromCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(totalfee);
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getFromAccNumber() + ", Faster Payment, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrDrTransType);
			l_TransactionData.setSubRef("");
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("");
			l_TransactionData.setProductGL(fromproduct.getProductGL());
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			// Cr IBT GL
			l_TransactionData = new TransactionData();
			if (pTransferData.getToAccNumber().startsWith("(GL)")) {
				pTransferData.setToAccNumber(pTransferData.getToAccNumber().replace("(GL)", ""));
				l_TransactionData.setGl(true);
			}
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getToAccNumber());
			l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_ToBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(amount);
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(amount * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getToAccNumber() + ", Faster Payment, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrCrTransType);
			l_TransactionData.setSubRef(pTransferData.getTraceNumber());
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("GL");
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			if (comm1Amt > 0) {
				// Cr Com GL
				l_TransactionData = new TransactionData();
				l_TransactionData.setGl(true);
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(l_Com1GL);
				l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(l_FromBranch);
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData.getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData.getRemark());
				l_TransactionData.setAmount(comm1Amt);
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(comm1Amt * drCCYRate));
				l_TransactionData.setDescription(l_Com1GL + ", Faster Payment, " + l_TrDateTime);
				l_TransactionData.setTransactionType(mDTrCrTransType);
				l_TransactionData.setSubRef(pTransferData.getTraceNumber());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("GL");
				l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
				l_TransactionList.add(l_TransactionData);
			}
			if (comm2Amt > 0) {
				// Cr Com GL
				l_TransactionData = new TransactionData();
				l_TransactionData.setGl(true);
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(l_Com2GL);
				l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(l_FromBranch);
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData.getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData.getRemark());
				l_TransactionData.setAmount(comm2Amt);
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(comm2Amt * drCCYRate));
				l_TransactionData.setDescription(l_Com2GL + ", Faster Payment, " + l_TrDateTime);
				l_TransactionData.setTransactionType(mDTrCrTransType);
				l_TransactionData.setSubRef(pTransferData.getTraceNumber());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("GL");
				l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
				l_TransactionList.add(l_TransactionData);
			}
		}

		return l_TransactionList;
	}

	// --------- YNDB --------
	public SMSReturnData domerchanttransfer(TransferData pTransferData) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		DAOManager l_DAOManager = new DAOManager();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		HashMap<String, AccountData> l_accDataList = new HashMap<String, AccountData>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		boolean isSameBranch = false;
		boolean isGL = false;
		double trFee = 0;
		Connection l_Conn1 = null;
		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();
		// // by
		// reference
		if (pTransferData.isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		Connection l_Conn = ConnAdmin.getConn3();
		l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getFromAccNumber(), l_Conn));
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			l_accDataList.put(pTransferData.getFromAccNumber(), l_FromAccountData);
		} else {
			l_SMSReturnData.setDescription("Invalid From Account No.");
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setCode("0014");
			return l_SMSReturnData;
		}

		pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
		pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
		try {
			if (l_DataResult.getStatus()) {
				l_DataResult = checkAccount(l_FromAccountData.getProduct(), l_FromAccountData.getProduct(),
						l_FromAccountData.getAccountNumber(), l_FromAccountData.getAccountNumber(), l_Conn);

				String fkey = "";
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
				}
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);

				// fromproduct = prddao.getTProduct(l_FromAccountData.getProduct(),
				// l_FromAccountData.getType(), l_Conn);
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
				pTransferData.setFromproduct(fromproduct);
				pTransferData.setpByForceCheque(pByForceCheque);
			}
			if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData.getToAccNumber(), l_Conn)) {
				l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData.getToAccNumber(), l_Conn));
				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
					l_accDataList.put(pTransferData.getToAccNumber(), l_ToAccountData);
				} else {
					l_SMSReturnData.setCode("210");
					l_SMSReturnData.setDescription("Invalid To Account No.");
					l_SMSReturnData.setStatus(false);
					return l_SMSReturnData;
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = checkAccount(l_ToAccountData.getProduct(), l_ToAccountData.getProduct(),
							l_ToAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn);

					String tkey = "";
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
					} else {
						tkey = l_ToAccountData.getProduct();
					}
					toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

					// toproduct = prddao.getTProduct(l_ToAccountData.getProduct(),
					// l_ToAccountData.getType(), l_Conn);
					pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
					pTransferData.setToproduct(toproduct);
					pTransferData.setpByForceCheque(pByForceCheque);
				}
			} else {
				if (l_DataResult.getStatus()) {
					pTransferData.setToAccNumber("(GL)" + pTransferData.getToAccNumber());
				}
			}
			if (l_DataResult.getStatus()) {
				l_ToAccountData.setBranchCode(pTransferData.getToBranchCode());
				l_ToAccountData.setZone(Integer.parseInt(pTransferData.getToZone()));
				l_TransactionList = prepareTransferMCT(pTransferData, l_TodayDate, l_EffectiveDate, l_ArlLogString,
						l_Conn, l_TransactionTime, l_TrDateTime, pByForceCheque, fromproduct, toproduct, l_Conn);

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// End Preparation
				// Check Transaction and post link
				// Check Transaction Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						pAccData = l_accDataList.get(l_TransactionList.get(i).getAccountNumber());
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}
				}
				if (!l_Conn.isClosed()) {
					l_Conn.close();
				}
				if (l_DataResult.getStatus()) {
					l_Conn1 = ConnAdmin.getConn();
					l_Conn1.setAutoCommit(false);
					l_ArlLogString.add("	Post Transaction ");

					l_DataResult = DBTransactionMgr.postNew(l_TransactionList, l_AccGLList, l_Conn1);
					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_Conn1.commit();
						l_SMSReturnData.setStatus(true);
						l_SMSReturnData.setCode("0000");
						l_SMSReturnData.setTransactionNumber(l_DataResult.getTransactionNumber());
						l_SMSReturnData.setComm1(trFee);
						l_SMSReturnData.setCcy(pTransferData.getCurrencyCode());
						l_SMSReturnData.setDescription("Posted successfully");
					} else {
						l_SMSReturnData.setStatus(false);
						l_SMSReturnData.setCode("0014");
						if (isLink) {
							l_SMSReturnData.setDescription(
									l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
						} else {
							l_SMSReturnData.setDescription(l_DataResult.getDescription());
						}
						l_Conn1.rollback();
					}

					if (!l_Conn1.isClosed() || l_Conn1 != null) {
						l_Conn1.close();
					}
				}
			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}
		return l_SMSReturnData;
	}

	private ArrayList<TransactionData> prepareTransferMCT(TransferData pTransferData, String p_TransactionDate,
			String p_EffectiveDate, ArrayList<String> l_ArlLogString, Connection pConn, String l_TransactionTime,
			String l_TrDateTime, String pByForceCheque, ProductData fromproduct, ProductData toproduct, Connection conn)
			throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String l_FromBranch = pTransferData.getFromBranchCode();
		String l_ToBranch = pTransferData.getToBranchCode();

		String fkey = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();

		double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);

		// double drCCYRate =
		// DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData.getFromCCY(),
		// "smTransfer",
		// pTransferData.getFromBranchCode(), conn);

		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		/*
		 * l_RefData = DBSystemMgr.getReferenceAccCode("CCTCOM1" +
		 * pTransferData.getCurrencyCode() + 1 + pTransferData.getFromBranchCode());
		 * String l_Com1GL = l_RefData.getGLCode();
		 * 
		 * l_RefData = DBSystemMgr.getReferenceAccCode("CCTCOM2" +
		 * pTransferData.getCurrencyCode() + pTransferData.getToBankCode() +
		 * pTransferData.getToBranchCode()); String l_Com2GL = l_RefData.getGLCode();
		 */

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
				.get("CCTCOM1" + pTransferData.getCurrencyCode() + 1 + pTransferData.getFromBranchCode());
		String l_Com1GL = "";
		if (l_RefData != null)
			l_Com1GL = l_RefData.getGLCode();

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("CCTCOM2"
				+ pTransferData.getCurrencyCode() + pTransferData.getToBankCode() + pTransferData.getToBranchCode());
		String l_Com2GL = "";
		if (l_RefData != null)
			l_Com2GL = l_RefData.getGLCode();

		double totalfee = 0, amount = 0, comm1Amt = 0, comm2Amt = 0;
		amount = pTransferData.getAmount();
		comm1Amt = pTransferData.getComm1();
		comm2Amt = pTransferData.getComm2();
		if (pTransferData.isInclusive())
			amount = amount - (comm1Amt + comm2Amt);

		totalfee = amount + comm1Amt + comm2Amt;

		if (totalfee > 0 && amount > 0) {
			// Dr From Customer
			l_TransactionData = new TransactionData();
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getFromAccNumber());
			if (fromproduct.hasCheque())
				l_TransactionData.setReferenceNumber(pByForceCheque);
			else
				l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_FromBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getFromCCY());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(totalfee);
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getFromAccNumber() + ", Faster Payment, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrDrTransType);
			l_TransactionData.setSubRef("");
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("");
			l_TransactionData.setProductGL(fromproduct.getProductGL());
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			// Cr IBT GL
			l_TransactionData = new TransactionData();
			if (pTransferData.getToAccNumber().startsWith("(GL)")) {
				pTransferData.setToAccNumber(pTransferData.getToAccNumber().replace("(GL)", ""));
				l_TransactionData.setGl(true);
			}
			l_TransactionData.setIsMobile(true);
			l_TransactionData.setAccountNumber(pTransferData.getToAccNumber());
			l_TransactionData.setReferenceNumber("");
			l_TransactionData.setBranchCode(l_ToBranch);
			l_TransactionData.setWorkstation("Mobile");
			l_TransactionData.setUserID(pTransferData.getUserID());
			l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
			l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
			l_TransactionData.setCurrencyRate(drCCYRate);
			l_TransactionData.setTransactionDate(p_TransactionDate);
			l_TransactionData.setTransactionTime(l_TransactionTime);
			l_TransactionData.setEffectiveDate(p_EffectiveDate);
			l_TransactionData.setContraDate("19000101");
			l_TransactionData.setStatus((byte) 1);
			l_TransactionData.setRemark(pTransferData.getRemark());
			l_TransactionData.setAmount(amount);
			l_TransactionData.setBaseAmount(StrUtil.round2decimals(amount * drCCYRate));
			l_TransactionData.setDescription(pTransferData.getToAccNumber() + ", Faster Payment, " + l_TrDateTime);
			l_TransactionData.setTransactionType(mDTrCrTransType);
			l_TransactionData.setSubRef(pTransferData.getTraceNumber());
			l_TransactionData.setAccRef("");
			l_TransactionData.setT1("GL");
			l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
			l_TransactionList.add(l_TransactionData);
			if (comm1Amt > 0) {
				// Cr Com GL
				l_TransactionData = new TransactionData();
				l_TransactionData.setGl(true);
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(l_Com1GL);
				l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(l_FromBranch);
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData.getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData.getRemark());
				l_TransactionData.setAmount(comm1Amt);
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(comm1Amt * drCCYRate));
				l_TransactionData.setDescription(l_Com1GL + ", Faster Payment, " + l_TrDateTime);
				l_TransactionData.setTransactionType(mDTrCrTransType);
				l_TransactionData.setSubRef(pTransferData.getTraceNumber());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("GL");
				l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
				l_TransactionList.add(l_TransactionData);
			}
			if (comm2Amt > 0) {
				// Cr Com GL
				l_TransactionData = new TransactionData();
				l_TransactionData.setGl(true);
				l_TransactionData.setIsMobile(true);
				l_TransactionData.setAccountNumber(l_Com2GL);
				l_TransactionData.setReferenceNumber("");
				l_TransactionData.setBranchCode(l_FromBranch);
				l_TransactionData.setWorkstation("Mobile");
				l_TransactionData.setUserID(pTransferData.getUserID());
				l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
				l_TransactionData.setCurrencyCode(pTransferData.getCurrencyCode());
				l_TransactionData.setCurrencyRate(drCCYRate);
				l_TransactionData.setTransactionDate(p_TransactionDate);
				l_TransactionData.setTransactionTime(l_TransactionTime);
				l_TransactionData.setEffectiveDate(p_EffectiveDate);
				l_TransactionData.setContraDate("19000101");
				l_TransactionData.setStatus((byte) 1);
				l_TransactionData.setRemark(pTransferData.getRemark());
				l_TransactionData.setAmount(comm2Amt);
				l_TransactionData.setBaseAmount(StrUtil.round2decimals(comm2Amt * drCCYRate));
				l_TransactionData.setDescription(l_Com2GL + ", Faster Payment, " + l_TrDateTime);
				l_TransactionData.setTransactionType(mDTrCrTransType);
				l_TransactionData.setSubRef(pTransferData.getTraceNumber());
				l_TransactionData.setAccRef("");
				l_TransactionData.setT1("GL");
				l_TransactionData.setCashInHandGL(fromproduct.getCashInHandGL());
				l_TransactionList.add(l_TransactionData);
			}
		}

		return l_TransactionList;
	}

	public gopaymentresponse domultitransfer(TransferData[] pTransferData, String paymentType) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		ArrayList<AccountGLTransactionData> l_AccGLList = new ArrayList<AccountGLTransactionData>();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		AccountDao l_AccDAO = new AccountDao();
		ProductsDAO prddao = new ProductsDAO();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		HashMap<String, AccountData> l_accDataList = new HashMap<String, AccountData>();

		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		Connection l_Conn1 = null;
		String pByForceCheque = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		String l_TrDateTime = GeneralUtility.getDateTimeDDMMYYYYHHMMSS();
		// // by
		// reference
		if (pTransferData[0].isFinishCutOffTime()) {
			l_EffectiveDate = GeneralUtility.getTomorrowDate();
			l_EffectiveDate = getEffectiveTransDate(l_EffectiveDate);
		} else {
			l_EffectiveDate = getEffectiveTransDate(l_TodayDate);
		}
		Connection l_Conn = ConnAdmin.getConn3();
		for (int i = 0; i < pTransferData.length; i++) {
			if (!(pTransferData[i].getFromAccNumber().equals("")
					|| pTransferData[i].getFromAccNumber().equals("null"))) {
				l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData[i].getFromAccNumber(), l_Conn));
				if (l_DataResult.getStatus()) {
					l_FromAccountData = l_AccDAO.getAccountsBean();
					pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
					pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
					pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
					l_accDataList.put(pTransferData[i].getFromAccNumber(), l_FromAccountData);
				} else {
					res.setRetcode("210");
					res.setRetmessage("Invalid From Account No.");
					return res;
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = checkAccount(l_FromAccountData.getProduct(), l_FromAccountData.getProduct(),
							l_FromAccountData.getAccountNumber(), l_FromAccountData.getAccountNumber(), l_Conn);

					String fkey = "";
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
					} else {
						fkey = l_FromAccountData.getProduct();
					}
					fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);

					pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
					pTransferData[i].setFromproduct(fromproduct);
					pTransferData[i].setpByForceCheque(pByForceCheque);
				}
			}

			if (!(pTransferData[i].getToAccNumber().equals("") || pTransferData[i].getToAccNumber().equals("null"))) {
				if (!l_AccDAO.isGL(pTransferData[i].getToAccNumber(), l_Conn)) {
					l_DataResult.setStatus(l_AccDAO.getAccount(pTransferData[i].getToAccNumber(), l_Conn));
					if (l_DataResult.getStatus()) {
						l_ToAccountData = l_AccDAO.getAccountsBean();
						pTransferData[i].setToBranchCode(l_ToAccountData.getBranchCode());
						pTransferData[i].setToCCY(l_FromAccountData.getCurrencyCode());
						l_accDataList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
					} else {
						res.setRetcode("210");
						res.setRetmessage("Invalid To Account No.");
						return res;
					}
					if (l_DataResult.getStatus()) {
						l_DataResult = checkAccount(l_ToAccountData.getProduct(), l_ToAccountData.getProduct(),
								l_ToAccountData.getAccountNumber(), l_ToAccountData.getAccountNumber(), l_Conn);

						String tkey = "";
						if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
							tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
						} else {
							tkey = l_ToAccountData.getProduct();
						}
						toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

						pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();// DBSystemMgr.getByForceCheck();
						pTransferData[i].setToproduct(toproduct);
						pTransferData[i].setpByForceCheque(pByForceCheque);
					}
				} else {
					pTransferData[i].setToAccNumber("(GL)" + pTransferData[i].getToAccNumber());
				}
			}

		}
		try {

			if (l_DataResult.getStatus()) {
				l_TransactionList = prepareMultiTransfer(pTransferData, l_TodayDate, l_EffectiveDate, l_ArlLogString,
						l_TransactionTime, l_TrDateTime, paymentType, l_Conn);

				l_AccGLList = DBTransactionMgr.prepareAccountGLTransactions(l_TransactionList, l_Conn);
				l_TransactionList = DBTransactionMgr.prepareBaseCurrencys(l_TransactionList);
				// End Preparation
				// Check Transaction and post link
				// Check Transaction Status
				AccountData pAccData = new AccountData();
				ArrayList<ReferenceData> plstStatus = SharedLogic.getSystemData().getAccountStatus();// DBAccountMgr.getAccountStatus(l_Conn);
				for (int i = 0; i < l_TransactionList.size(); i++) {
					if (!l_TransactionList.get(i).isGl()) {
						pAccData = l_accDataList.get(l_TransactionList.get(i).getAccountNumber());
						l_DataResult = SharedLogic.checkAccStatus(l_TransactionList.get(i).getTransactionType(),
								l_AccGLList.get(i).getTrAmount(), pAccData, plstStatus);
						if (!l_DataResult.getStatus())
							break;
					}

				}
				if (!l_Conn.isClosed()) {
					l_Conn.close();
				}
				if (l_DataResult.getStatus()) {
					l_Conn1 = ConnAdmin.getConn();
					l_Conn1.setAutoCommit(false);
					l_ArlLogString.add("	Post Transaction ");

					l_DataResult = DBTransactionMgr.postMulti(l_TransactionList, l_AccGLList, l_Conn1);

					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_Conn1.commit();
						res.setRetcode("300");
						String transDate = GeneralUtil.datetoString();
						res.setTransdate(GeneralUtil.mobiledateformat(transDate));
						res.setBankrefnumber(String.valueOf(l_DataResult.getTransactionNumber()));
						res.setRetmessage("Multi Transfer Successfully!");
					} else {
						res.setRetcode("210");
						res.setRetmessage(l_DataResult.getDescription());
						l_Conn1.rollback();
					}

					if (!l_Conn1.isClosed() || l_Conn1 != null) {
						l_Conn1.close();
					}
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(l_DataResult.getDescription());
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}
		return res;
	}

	private ArrayList<TransactionData> prepareMultiTransfer(TransferData[] pTransferData, String p_TransactionDate,
			String p_EffectiveDate, ArrayList<String> l_ArlLogString, String l_TransactionTime, String l_TrDateTime,
			String paymentType, Connection conn) throws Exception {
		ArrayList<TransactionData> l_TransactionList = new ArrayList<TransactionData>();
		TransactionData l_TransactionData = new TransactionData();
		String desc = "";
		desc = "Multi Transfer";
		String fromacc = "";

		for (int i = 0; i < pTransferData.length; i++) {
			if (paymentType.equals("10")) {
				if (!(pTransferData[i].getFromAccNumber().equals("")
						|| pTransferData[i].getFromAccNumber().equals("null"))) {

					String fkey = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_"
							+ pTransferData[i].getFromBranchCode();

					double drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey);

					double totalfee = pTransferData[i].getAmount();
					fromacc = pTransferData[i].getFromAccNumber();
					if (totalfee > 0) {
						// Dr From Customer
						l_TransactionData = new TransactionData();
						l_TransactionData.setIsMobile(true);
						l_TransactionData.setAccountNumber(pTransferData[i].getFromAccNumber());
						if (pTransferData[i].getFromproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
						l_TransactionData.setBranchCode(pTransferData[i].getFromBranchCode());
						l_TransactionData.setWorkstation("Mobile");
						l_TransactionData.setUserID(pTransferData[i].getUserID());
						l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
						l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
						l_TransactionData.setCurrencyRate(drCCYRate);
						l_TransactionData.setTransactionDate(p_TransactionDate);
						l_TransactionData.setTransactionTime(l_TransactionTime);
						l_TransactionData.setEffectiveDate(p_EffectiveDate);
						l_TransactionData.setContraDate("19000101");
						l_TransactionData.setStatus((byte) 1);
						l_TransactionData.setRemark(pTransferData[i].getRemark());
						l_TransactionData.setAmount(totalfee);
						l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
						l_TransactionData
								.setDescription(desc + ", " + pTransferData[i].getReferenceNo() + ", " + l_TrDateTime);
						l_TransactionData.setTransactionType(mPaymentDrTransType);
						l_TransactionData.setSubRef("");
						l_TransactionData.setAccRef("");
						l_TransactionData.setT1("");
						l_TransactionData.setProductGL(pTransferData[i].getFromproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getFromproduct().getCashInHandGL());
						l_TransactionList.add(l_TransactionData);
					}
				}
				if (!(pTransferData[i].getToAccNumber().equals("")
						|| pTransferData[i].getToAccNumber().equals("null"))) {

					String tkey = "smTransfer" + "_" + pTransferData[i].getToCCY() + "_"
							+ pTransferData[i].getToBranchCode();
					double crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey);

					// Cr IBT GL
					l_TransactionData = new TransactionData();
					if (pTransferData[i].getToAccNumber().startsWith("(GL)")) {
						pTransferData[i].setToAccNumber(pTransferData[i].getToAccNumber().replace("(GL)", ""));
						l_TransactionData.setGl(true);
					}
					l_TransactionData.setIsMobile(true);
					l_TransactionData.setAccountNumber(pTransferData[i].getToAccNumber());
					if (pTransferData[i].getToproduct() != null) {
						if (pTransferData[i].getToproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
					} else {
						l_TransactionData.setReferenceNumber("");
					}

					l_TransactionData.setBranchCode(pTransferData[i].getToBranchCode());
					l_TransactionData.setWorkstation("Mobile");
					l_TransactionData.setUserID(pTransferData[i].getUserID());
					l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
					l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
					l_TransactionData.setCurrencyRate(crCCYRate);
					l_TransactionData.setTransactionDate(p_TransactionDate);
					l_TransactionData.setTransactionTime(l_TransactionTime);
					l_TransactionData.setEffectiveDate(p_EffectiveDate);
					l_TransactionData.setContraDate("19000101");
					l_TransactionData.setStatus((byte) 1);
					l_TransactionData.setRemark(pTransferData[i].getRemark());
					l_TransactionData.setAmount(pTransferData[i].getAmount());
					l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * crCCYRate));
					l_TransactionData.setDescription(
							desc + ", " + fromacc + ", " + pTransferData[i].getRemark() + ", " + l_TrDateTime);
					l_TransactionData.setTransactionType(mPaymentCrTransType);
					l_TransactionData.setSubRef(pTransferData[i].getTraceNumber());
					l_TransactionData.setAccRef("");
					l_TransactionData.setT1("");
					if (pTransferData[i].getToproduct() != null) {
						l_TransactionData.setProductGL(pTransferData[i].getToproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getToproduct().getCashInHandGL());
					}
					l_TransactionList.add(l_TransactionData);
				}
			} else if (paymentType.equals("20")) {
				if (!(pTransferData[i].getToAccNumber().equals("")
						|| pTransferData[i].getToAccNumber().equals("null"))) {
					double drCCYRate = DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getToCCY(),
							"smTransfer", pTransferData[i].getToBranchCode(), conn);
					fromacc = pTransferData[pTransferData.length - 1].getFromAccNumber();
					// Dr IBT GL
					l_TransactionData = new TransactionData();
					if (pTransferData[i].getToAccNumber().startsWith("(GL)")) {
						pTransferData[i].setToAccNumber(pTransferData[i].getToAccNumber().replace("(GL)", ""));
						l_TransactionData.setGl(true);
					}
					l_TransactionData.setIsMobile(true);
					l_TransactionData.setAccountNumber(pTransferData[i].getToAccNumber());
					if (pTransferData[i].getToproduct() != null) {
						if (pTransferData[i].getToproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
					} else {
						l_TransactionData.setReferenceNumber("");
					}

					l_TransactionData.setBranchCode(pTransferData[i].getToBranchCode());
					l_TransactionData.setWorkstation("Mobile");
					l_TransactionData.setUserID(pTransferData[i].getUserID());
					l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
					l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
					l_TransactionData.setCurrencyRate(drCCYRate);
					l_TransactionData.setTransactionDate(p_TransactionDate);
					l_TransactionData.setTransactionTime(l_TransactionTime);
					l_TransactionData.setEffectiveDate(p_EffectiveDate);
					l_TransactionData.setContraDate("19000101");
					l_TransactionData.setStatus((byte) 1);
					l_TransactionData.setRemark(pTransferData[i].getRemark());
					l_TransactionData.setAmount(pTransferData[i].getAmount());
					l_TransactionData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * drCCYRate));
					l_TransactionData.setDescription(
							desc + ", " + fromacc + ", " + pTransferData[i].getRemark() + ", " + l_TrDateTime);
					l_TransactionData.setTransactionType(mPaymentDrTransType);
					l_TransactionData.setSubRef(pTransferData[i].getTraceNumber());
					l_TransactionData.setAccRef("");
					l_TransactionData.setT1("");
					if (pTransferData[i].getToproduct() != null) {
						l_TransactionData.setProductGL(pTransferData[i].getToproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getToproduct().getCashInHandGL());
					}
					l_TransactionList.add(l_TransactionData);
				}
				if (!(pTransferData[i].getFromAccNumber().equals("")
						|| pTransferData[i].getFromAccNumber().equals("null"))) {
					double crCCYRate = DBFECCurrencyRateMgr.getFECCurrencyRate(pTransferData[i].getFromCCY(),
							"smTransfer", pTransferData[i].getFromBranchCode(), conn);

					double totalfee = pTransferData[i].getAmount();
					if (totalfee > 0) {
						// Cr From Customer
						l_TransactionData = new TransactionData();
						l_TransactionData.setIsMobile(true);
						l_TransactionData.setAccountNumber(pTransferData[i].getFromAccNumber());
						if (pTransferData[i].getFromproduct().hasCheque())
							l_TransactionData.setReferenceNumber(pTransferData[i].getpByForceCheque());
						else
							l_TransactionData.setReferenceNumber("");
						l_TransactionData.setBranchCode(pTransferData[i].getFromBranchCode());
						l_TransactionData.setWorkstation("Mobile");
						l_TransactionData.setUserID(pTransferData[i].getUserID());
						l_TransactionData.setAuthorizerID(SharedLogic.getSystemData().getpAuthorizerID());
						l_TransactionData.setCurrencyCode(pTransferData[i].getFromCCY());
						l_TransactionData.setCurrencyRate(crCCYRate);
						l_TransactionData.setTransactionDate(p_TransactionDate);
						l_TransactionData.setTransactionTime(l_TransactionTime);
						l_TransactionData.setEffectiveDate(p_EffectiveDate);
						l_TransactionData.setContraDate("19000101");
						l_TransactionData.setStatus((byte) 1);
						l_TransactionData.setRemark(pTransferData[i].getRemark());
						l_TransactionData.setAmount(totalfee);
						l_TransactionData.setBaseAmount(StrUtil.round2decimals(totalfee * crCCYRate));
						l_TransactionData
								.setDescription(desc + ", " + pTransferData[i].getReferenceNo() + ", " + l_TrDateTime);
						l_TransactionData.setTransactionType(mPaymentCrTransType);
						l_TransactionData.setSubRef("");
						l_TransactionData.setAccRef("");
						l_TransactionData.setT1("");
						l_TransactionData.setProductGL(pTransferData[i].getFromproduct().getProductGL());
						l_TransactionData.setCashInHandGL(pTransferData[i].getFromproduct().getCashInHandGL());
						l_TransactionList.add(l_TransactionData);
					}
				}
			}

		}
		return l_TransactionList;
	}

	// ============================== Update Transfer ============================

	public SMSReturnData postTransferNew(TransferData pTransferData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				l_EffectiveDate = GeneralUtility.getTomorrowDate();
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_transprepare" + pTransferData.getFromAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();

		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareAccountTransaction(pTransferData, tmp_Transpreapre, l_TableName, l_TableName1, l_Conn);

		try {
			if (l_DataResult.getStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				AccountDao lAccDAO = new AccountDao();
				int autoLinkTransref = (int) l_DataResult.getAutoLinkTransRef();
				// update Balance
				l_DataResult = lAccDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
						l_Conn);
				if (l_DataResult.getStatus()) {
					l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
				}

				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef ynw test
//						if(autoLinkTransref>0)
//							new AccountTransactionDAO().updateAutoLinkTransRef(autoLinkTransref,l_DataResult.getTransRef(),l_Conn);
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}

			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}

	public DataResult prepareAccountTransaction(TransferData pTransferData, String tmp_Transpreapre, String l_TableName,
			String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		boolean isSameBranch = false;
		String pByForceCheque = "";
		String fkey = "", tkey = "";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0;
		double crCCYRate = 0;
		int lUncommTransNo = DBTransactionMgr.nextValue();
		l_DataResult = l_AccDAO.getTransAccount(pTransferData.getFromAccNumber(), "Dr", pTransferData.getAmount(),
				l_Conn);
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
		}

		if (l_DataResult.getStatus()) {
			l_DataResult = l_AccDAO.getTransAccount(pTransferData.getToAccNumber(), "Cr", pTransferData.getAmount(),
					l_Conn);
			if (l_DataResult.getStatus()) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
			}
		}

		if (l_DataResult.getStatus()) {
			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				fkey = l_FromAccountData.getProduct();
				tkey = l_ToAccountData.getProduct();
			}
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

			// For Checking Transaction
			// Check Multiple Of
			double f_MultipleOf = fromproduct.getMultipleOf();
			double t_MultipleOf = toproduct.getMultipleOf();
			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if ((int) (pTransferData.getAmount() % f_MultipleOf) != 0) {
				l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
				l_DataResult.setStatus(false);
			} else {
				// if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = fromproduct.getMinWithdrawal();
				if (pTransferData.getAmount() < l_MinWithdrawal) {
					l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
					l_DataResult.setStatus(false);
				}
			}
			if (l_DataResult.getStatus()) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus()) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinOpeningBalance(), 0,
					l_Conn);
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (pTransferData.getAmount() > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (pTransferData.getAmount() != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}
			if (!l_DataResult.getStatus()) {
				l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrency(pTransferData, l_FromAccountData,
						l_TableName, l_TableName1, l_Available, pTransferData.getAmount(), l_Conn);
				// l_DataResult =
				// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
				// l_FromAccountData, l_TableName, l_TableName1, l_Available,
				// pTransferData.getAmount(), l_Conn);
				if (l_DataResult.getStatus()) {// Add Auto Link Transref
					AccountTransactionDAO lAccTranDAO = new AccountTransactionDAO();
					if (l_DataResult.getAutoLinkTransRef() > 0)
						lAccTranDAO.addAutoLinkTransRef(lUncommTransNo, (int) l_DataResult.getAutoLinkTransRef(),
								l_Conn);
					if (l_DataResult.getSecAutoLinkTransRef() > 0)
						lAccTranDAO.addAutoLinkTransRef(lUncommTransNo, (int) l_DataResult.getSecAutoLinkTransRef(),
								l_Conn);
					l_DataResult.setAutoLinkTransRef(lUncommTransNo);
				}

			}
		}
		if (l_DataResult.getStatus()) {

			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			if (l_FromAccountData.getBranchCode().equalsIgnoreCase(l_ToAccountData.getBranchCode())) {
				isSameBranch = true;
			}

			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
			pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
			pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
			pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());

			String fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
			String tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();

			drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
		}
		PreparedStatement pstmt = null;
		String l_Query = "";
		if (l_DataResult.getStatus()) {

			if (isSameBranch) {

				int index = 1;
				// Dr From Customer
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
				pstmt = l_Conn.prepareStatement(l_Query);
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				pstmt.setDouble(index++, pTransferData.getAmount());
				pstmt.setString(index++, pTransferData.getFromAccNumber());
				pstmt.setString(index++, pTransferData.getFromBranchCode());
				pstmt.setString(index++, pTransferData.getFromCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, drCCYRate);
				pstmt.setDouble(index++, pTransferData.getAmount() * drCCYRate);
				pstmt.setString(index++, pTransferData.getToAccNumber() + ", Mobile Transfer" + ", "
						+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, mTrDrTransType);
				pstmt.setString(index++, fromproduct.getCashInHandGL());
				pstmt.setString(index++, fromproduct.getProductGL());
				pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.executeUpdate();
				// Cr To Customer
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
				pstmt = l_Conn.prepareStatement(l_Query);
				index = 1;
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					pstmt.setDouble(index++, pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
				else
					pstmt.setDouble(index++,
							(pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

				pstmt.setString(index++, pTransferData.getToAccNumber());
				pstmt.setString(index++, pTransferData.getToBranchCode());
				pstmt.setString(index++, pTransferData.getToCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, crCCYRate);
				pstmt.setDouble(index++, pTransferData.getAmount() * drCCYRate);
				pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Mobile  Transfer" + ", "
						+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, mTrCrTransType);
				pstmt.setString(index++, toproduct.getCashInHandGL());
				pstmt.setString(index++, toproduct.getProductGL());
				pstmt.setString(index++, "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.executeUpdate();

			} else {
				// different branch
				String l_IBTGLFrom = "";
				String l_IBTGLTo = "";

				ReferenceAccountsData l_RefData = new ReferenceAccountsData();
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
						.get("MTRDR" + pTransferData.getFromCCY() + pTransferData.getFromBranchCode()
								+ fromproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLFrom = l_RefData.getGLCode();
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRCR"
						+ pTransferData.getToCCY() + pTransferData.getToBranchCode() + toproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLTo = l_RefData.getGLCode();

				if (!(l_IBTGLFrom.equals("") || l_IBTGLTo.equals(""))) {
					// Dr From Customer
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
					int index = 1;
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, pTransferData.getAmount());
					pstmt.setString(index++, pTransferData.getFromAccNumber());
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, pTransferData.getAmount() * drCCYRate);
					pstmt.setString(index++, pTransferData.getToAccNumber() + ", Mobile Transfer" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mDTrDrTransType);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, fromproduct.getProductGL());
					pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();

					// Cr IBT GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++,
								(pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_IBTGLFrom);
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Mobile  Transfer" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mDTrCrTransType);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, "");
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();

					// Dr IBT GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
					index = 1;
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++,
								(pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, l_IBTGLTo);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
					pstmt.setString(index++, pTransferData.getToAccNumber() + ", Mobile Transfer" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mDTrDrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, "");
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();

					// Cr To Account
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++,
								(pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, pTransferData.getToAccNumber());
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData.getAmount() * crCCYRate));
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Mobile  Transfer" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mDTrCrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				} else {
					l_DataResult.setStatus(false);
					l_DataResult.setCode("0014");
					l_DataResult.setDescription("Invalid GL Account!");
				}
			}
		}
		if (!l_DataResult.getStatus()) {
			l_DataResult.setCode("0014");
		}

		return l_DataResult;
	}

	public DataResult prepareAccountTransactionDeposit(DepositData pDepositData, String tmp_Transpreapre,
			String l_TableName, String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData AccountData = new AccountData();
		double crCCYRate = 0;
		ProductData product = new ProductData();
		String key = "";
		DepositData pGLDepositData = new DepositData();
		ProductFeatureDAO pFeatureDAO = new ProductFeatureDAO();

		if (!pDepositData.getIsGL()) {
			l_DataResult = l_AccDAO.getTransAccount(pDepositData.getAccNumber(), "Cr", pDepositData.getAmount(),
					l_Conn);
			if (l_DataResult.getStatus()) {
				AccountData = l_AccDAO.getAccountsBean();
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					key = AccountData.getProduct() + "" + AccountData.getType();
				} else {
					key = AccountData.getProduct();
				}
				product = SharedLogic.getSystemData().getpProductDataList().get(key);

				// For Checking Transaction
				// Check Multiple Of
				double MultipleOf = product.getMultipleOf();

				if (l_DataResult.getStatus()) { // For Credit
					if ((int) (pDepositData.getAmount() % MultipleOf) != 0) {
						l_DataResult.setDescription("Amount not multiple of " + MultipleOf);
						l_DataResult.setStatus(false);
					} else {
						double l_MinOpeningBalance = product.getMinOpeningBalance();
						if (AccountData.getStatus() == 0
								&& SharedUtil.formatMIT2DateStr(AccountData.getLastTransDate()).equals("01/01/1900")) {
							if (pDepositData.getAmount() < l_MinOpeningBalance) {
								l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
								l_DataResult.setStatus(false);
							}
						}
					}
				}
			}
		} else {
			l_DataResult.setStatus(true);
		}

		if (l_DataResult.getStatus()) {
			if (pDepositData.getIsGL()) {
				pGLDepositData = pDepositData;
				pDepositData.setTransType(99);
				pDepositData.setTransDescription(
						SharedLogic.getTransDescriptionNew(TransCode.Deposit, pDepositData.getTransType()));

				/*
				 * if (SharedLogic.getSystemData().getpSystemSettingDataList().get("FC").getN1()
				 * == 1) { String curdigit =
				 * SharedLogic.getCurrencyDigit(pDepositData.getCurrencyCode());
				 * pDepositData.setCashInHandGL(SharedLogic.getCashinHandGL(curdigit));
				 * pDepositData.setGlCode2(pDepositData.getCashInHandGL()); l_DataResult =
				 * SharedLogic.checkCashInHandGL(pGLDepositData.getAccNumber().toString());
				 * pDepositData.setT2(pDepositData.getAuthorizerID());
				 * pDepositData.setT3(pDepositData.getUserID()); } else {
				 */
				String CashInHandGL = pFeatureDAO.getCashInHandGL(pGLDepositData.getCurrencyCode(), 1, l_Conn);
				pDepositData.setCashInHandGL(CashInHandGL);
				pDepositData.setGlCode2(CashInHandGL);
				l_DataResult = SharedLogic.checkCashInHandGL(pDepositData.getAccNumber().toString());
				// }
				crCCYRate = 1;
			} else {
				pDepositData.setCurrencyCode(AccountData.getCurrencyCode());
				pDepositData.setBranchCode(AccountData.getBranchCode());
				String l_ProductCode = AccountData.getProduct();

				if (SharedLogic.isFD(l_ProductCode)) {
					if (SharedLogic.getSystemSettingT12N3("FD") == 2 && !pGLDepositData.getAccRef().equals(""))
						pDepositData.setN3(Integer.parseInt(pGLDepositData.getAccRef()));
				}
				pDepositData.setTransType(SharedLogic.getTransType(TransCode.Deposit, l_ProductCode));
				pDepositData.setTransDescription(
						SharedLogic.getTransDescriptionNew(TransCode.Deposit, pDepositData.getTransType()));
				pDepositData.setPreviousDate(AccountData.getLastUpdate());
				pDepositData.setCashInHandGL(AccountData.getCashInHandGL());
				pDepositData.setProductGL(AccountData.getProductGL());
				pDepositData.setGlCode1(AccountData.getProductGL());
				pDepositData.setGlCode2(AccountData.getCashInHandGL());

				String tkey1 = "smDeposit" + "_" + pDepositData.getCurrencyCode() + "_" + pDepositData.getBranchCode();
				crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
			}

			PreparedStatement pstmt = null;
			String l_Query = "";
			if (l_DataResult.getStatus()) {
				int index = 1;

				// Cr To Customer
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('API',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
				pstmt = l_Conn.prepareStatement(l_Query);
				index = 1;
				pstmt.setString(index++, pDepositData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
//					pstmt.setDouble(index++, pDepositData.getAmount());
				pstmt.setDouble(index++, pDepositData.getAmount() * (crCCYRate * 1000) / 1000);
				pstmt.setString(index++, pDepositData.getAccNumber());
				pstmt.setString(index++, pDepositData.getBranchCode());
				pstmt.setString(index++, pDepositData.getCurrencyCode());
				pstmt.setString(index++, pDepositData.getTransactionTime());
				pstmt.setString(index++, pDepositData.getTransactionDate());
				pstmt.setString(index++, pDepositData.getEffectiveDate());
				pstmt.setString(index++, pDepositData.getReferenceNo());
				pstmt.setString(index++, pDepositData.getRemark());
				pstmt.setDouble(index++, crCCYRate);
				pstmt.setDouble(index++, pDepositData.getAmount());
				pstmt.setString(index++, pDepositData.getAccNumber() + ", API Deposit" + ", "
						+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, pDepositData.getTransType());
				pstmt.setString(index++, pDepositData.getCashInHandGL());
				pstmt.setString(index++, pDepositData.getProductGL());
				pstmt.setString(index++, "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, AccountData.getCurrentBalance());
				pstmt.setString(index++, pDepositData.getTransDescription());
				pstmt.executeUpdate();

			}
		}

		if (!l_DataResult.getStatus()) {
			l_DataResult.setCode("0014");
		}
		return l_DataResult;
	}

	public DataResult prepareAccountTransactionWithdraw(WithdrawData pWithdrawData, String tmp_Transpreapre,
			String l_TableName, String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData AccountData = new AccountData();
		double drCCYRate = 0.00;
		ProductData product = new ProductData();
		String key = "";
		WithdrawData pGLWithdrawData = new WithdrawData();
		ProductFeatureDAO pFeatureDAO = new ProductFeatureDAO();
		String pByForceCheque = "";

		if (!pWithdrawData.getIsGL()) {
			l_DataResult = l_AccDAO.getTransAccount(pWithdrawData.getAccNumber(), "Dr", pWithdrawData.getAmount(),
					l_Conn);
			if (l_DataResult.getStatus()) {
				AccountData = l_AccDAO.getAccountsBean();
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					key = AccountData.getProduct() + "" + AccountData.getType();
				} else {
					key = AccountData.getProduct();
				}
				product = SharedLogic.getSystemData().getpProductDataList().get(key);

				// For Checking Transaction
				// Check Multiple Of
				double MultipleOf = product.getMultipleOf();

				if (l_DataResult.getStatus()) { // For Debit
					if ((int) (pWithdrawData.getAmount() % MultipleOf) != 0) {
						l_DataResult.setDescription("Amount not multiple of " + MultipleOf);
						l_DataResult.setStatus(false);
					} else {
						double l_MinWithdrawal = product.getMinWithdrawal();
						if (pWithdrawData.getAmount() < l_MinWithdrawal) {
							l_DataResult
									.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
							l_DataResult.setStatus(false);
						}
					}
				}

			}

			// check Debit Available Balance
			if (l_DataResult.getStatus()) {
				double l_Available = 0;
				l_Available = l_AccDAO.getAvaliableBalance(AccountData, product.getMinOpeningBalance(), 0, l_Conn);
				// Check For A/C Status Closed Pending
				if (AccountData.getStatus() != 2) {
					if (pWithdrawData.getAmount() > l_Available) {
						l_DataResult.setStatus(false);
						l_DataResult.setDescription(AccountData.getAccountNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((pWithdrawData.getAmount() - l_Available)));
					}
				} else {

					if (l_DataResult.getStatus()) {
						double l_CurrentBal = AccountData.getCurrentBalance();
						if (pWithdrawData.getAmount() != l_CurrentBal) {
							l_DataResult.setStatus(false);
							l_DataResult.setDescription(AccountData.getAccountNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma((pWithdrawData.getAmount() - l_CurrentBal)));
						}
					}
				}

			}
			if (l_DataResult.getStatus()) {
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
				if (SharedLogic.getProductCodeToProductData(AccountData.getProduct()).hasCheque()) {
					int l_ChqN4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("CHQ").getN4();
					if (l_ChqN4 == 1) {
						pWithdrawData.setReferenceNo(pByForceCheque);
					}
					// Checking Check Number
					l_DataResult = DBTransactionMgr.checkChequeNumberTransaction(pWithdrawData.getAccNumber(),
							pWithdrawData.getReferenceNo(), pByForceCheque, l_Conn);
				}
			}
			if (l_DataResult.getStatus()) {
				pWithdrawData.setCurrencyCode(AccountData.getCurrencyCode());
				pWithdrawData.setBranchCode(AccountData.getBranchCode());
				String l_ProductCode = AccountData.getProduct();
				pGLWithdrawData = pWithdrawData;

				if (SharedLogic.isFD(l_ProductCode)) {
					if (SharedLogic.getSystemSettingT12N3("FD") == 2 && !pGLWithdrawData.getAccRef().equals(""))
						pWithdrawData.setN3(Integer.parseInt(pGLWithdrawData.getAccRef()));
				}

				pWithdrawData.setTransType(SharedLogic.getTransType(TransCode.Withdraw, AccountData.getProduct()));
				pWithdrawData.setTransDescription(
						SharedLogic.getTransDescriptionNew(TransCode.Withdraw, pWithdrawData.getTransType()));
				pWithdrawData.setPreviousDate(AccountData.getLastUpdate());
				pWithdrawData.setCashInHandGL(AccountData.getCashInHandGL());
				pWithdrawData.setProductGL(AccountData.getProductGL());
				pWithdrawData.setGlCode1(AccountData.getProductGL());
				pWithdrawData.setGlCode2(AccountData.getCashInHandGL());

				if (SharedLogic.isSavings(AccountData.getProduct())) {
					if (pWithdrawData.isMobile()) {
						l_DataResult = canWithdrawforSavingMobile(pWithdrawData.getAccNumber(),
								pWithdrawData.getEffectiveDate(), l_Conn);
					} else {
						l_DataResult = canWithdrawforSaving(pWithdrawData.getAccNumber(),
								pWithdrawData.getEffectiveDate(), pWithdrawData.getTransType(), l_Conn);
					}
				}

				String tkey1 = "smWithdraw" + "_" + pWithdrawData.getCurrencyCode() + "_"
						+ pWithdrawData.getBranchCode();
				drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
			}
		} else {
			pWithdrawData.setTransType(599);
			pWithdrawData.setTransDescription(
					SharedLogic.getTransDescriptionNew(TransCode.Withdraw, pWithdrawData.getTransType()));

//			if (SharedLogic.getSystemData().getpSystemSettingDataList().get("FC").getN1() == 1) {
//				String curdigit = SharedLogic.getCurrencyDigit(pWithdrawData.getCurrencyCode());
//				pWithdrawData.setCashInHandGL(SharedLogic.getCashinHandGL(curdigit));
//				pWithdrawData.setGlCode2(pWithdrawData.getCashInHandGL());
//				l_DataResult = SharedLogic.checkCashInHandGL(pGLWithdrawData.getAccNumber().toString());
//				pWithdrawData.setT2(pWithdrawData.getAuthorizerID());
//				pWithdrawData.setT3(pWithdrawData.getUserID());
//			} else {
			String CashInHandGL = pFeatureDAO.getCashInHandGL(pWithdrawData.getCurrencyCode(), 1, l_Conn);
			pWithdrawData.setCashInHandGL(CashInHandGL);
			pWithdrawData.setGlCode2(CashInHandGL);
			l_DataResult = SharedLogic.checkCashInHandGL(pWithdrawData.getAccNumber().toString());
			drCCYRate = 1;

		}

		if (l_DataResult.getStatus()) {

			PreparedStatement pstmt = null;
			String l_Query = "";

			int index = 1;
			// Dr From Customer
			l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'API',?,?,?,?,?,?,?,?,?,?,?,"
					+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
			pstmt = l_Conn.prepareStatement(l_Query);
			pstmt.setString(index++, pWithdrawData.getUserID());
			pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
			pstmt.setDouble(index++, pWithdrawData.getAmount());
			pstmt.setString(index++, pWithdrawData.getAccNumber());
			pstmt.setString(index++, pWithdrawData.getBranchCode());
			pstmt.setString(index++, pWithdrawData.getCurrencyCode());
			pstmt.setString(index++, pWithdrawData.getTransactionTime());
			pstmt.setString(index++, pWithdrawData.getTransactionDate());
			pstmt.setString(index++, pWithdrawData.getEffectiveDate());
			pstmt.setString(index++, pWithdrawData.getReferenceNo());
			pstmt.setString(index++, pWithdrawData.getRemark());
			pstmt.setDouble(index++, drCCYRate);
			pstmt.setDouble(index++, pWithdrawData.getAmount() * drCCYRate);
			pstmt.setString(index++, pWithdrawData.getAccNumber() + ", API Withdraw" + ", "
					+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
			pstmt.setInt(index++, pWithdrawData.getTransType());
			pstmt.setString(index++, pWithdrawData.getCashInHandGL());
			pstmt.setString(index++, pWithdrawData.getProductGL());
			if (pWithdrawData.getIsGL()) {
				pstmt.setString(index++, "");
			} else {
				pstmt.setString(index++, product.hasCheque() ? pByForceCheque : "");
			}

			pstmt.setInt(index++, 1);
			pstmt.setDouble(index++, AccountData.getCurrentBalance());
			pstmt.setString(index++, pWithdrawData.getTransDescription());
			pstmt.executeUpdate();
		}

		if (!l_DataResult.getStatus()) {
			l_DataResult.setCode("0014");
		}
		return l_DataResult;
	}

	/*
	 * private void insertintoTmpAccPrepare(){ String l_Query = "INSERT INTO "
	 * +Tmp_accprepare+" VALUES('Mobile',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	 * PreparedStatement pstmt = l_Conn.prepareStatement(l_Query); int index=1;
	 * pstmt.setString(index++, pTransferData.getUserID()); pstmt.setString(index++,
	 * SharedLogic.getSystemData().getpAuthorizerID()); pstmt.setDouble(index++,
	 * pTransferData.getAmount()); pstmt.setString(index++,
	 * pTransferData.getFromAccNumber()); pstmt.setString(index++,
	 * pTransferData.getToAccNumber()); pstmt.setString(index++,
	 * pTransferData.getFromBranchCode()); pstmt.setString(index++,
	 * pTransferData.getToBranchCode()); pstmt.setString(index++,
	 * pTransferData.getFromCCY()); pstmt.setString(index++,
	 * pTransferData.getToCCY() ); pstmt.setString(index++,
	 * pTransferData.getTransactionTime()); pstmt.setString(index++,
	 * pTransferData.getTransactionDate()); pstmt.setString(index++,
	 * pTransferData.getEffectiveDate()); pstmt.setString(index++,
	 * pTransferData.getReferenceNo()); pstmt.setString(index++,
	 * pTransferData.getRemark()); pstmt.setDouble(index++, drCCYRate);
	 * pstmt.setDouble(index++, crCCYRate); pstmt.setDouble(index++,
	 * l_FromAccountData.getCurrentBalance()); pstmt.setDouble(index++,
	 * l_ToAccountData.getCurrentBalance()); pstmt.executeUpdate(); }
	 * 
	 * 
	 * private void prepareTransferSameBranchWithoutComm( String tmpTable,String
	 * Tmp_accprepare,String pByForceCheque, ProductData fromproduct, ProductData
	 * toproduct, Connection conn) throws Exception {
	 * 
	 * int index = 1; // Dr From Customer String l_Query = "INSERT INTO " + tmpTable
	 * +
	 * " Select workstation,userid,authid,amount,facc,fbrcode,fccy,transtime,transdate,effdate,refno,remark,"
	 * +
	 * "'19000101',1,drccyrate,amount*drccyrate,tacc+?,?,?,?,?,?,fprevbalance from "
	 * +Tmp_accprepare; PreparedStatement pstmt = conn.prepareStatement(l_Query);
	 * pstmt.setString(index++,", Mobile Transfer" + ", " +
	 * GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
	 * pstmt.setInt(index++,mTrDrTransType);
	 * pstmt.setString(index++,fromproduct.getCashInHandGL());
	 * pstmt.setString(index++,fromproduct.getProductGL());
	 * pstmt.setString(index++,fromproduct.hasCheque()?pByForceCheque:"");
	 * pstmt.setInt(index++,1);
	 * 
	 * pstmt.executeUpdate(); // Cr To Customer l_Query = "INSERT INTO " + tmpTable
	 * +
	 * " Select workstation,userid,authid,(CASE WHEN fccy=tccy THEN amount ELSE (amount*drccyrate)/crccyrate END) * (drccyrate*1000)/1000 ,"
	 * + " tacc,tbrcode,tccy,transtime,transdate,effdate,refno,remark," +
	 * " '19000101',1,crccyrate,amount*drccyrate,facc+?,?,?,?,?,?,tprevbalance from "
	 * +Tmp_accprepare; pstmt = conn.prepareStatement(l_Query); index = 1;
	 * pstmt.setString(index++, ", Mobile  Transfer" + ", " +
	 * GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
	 * pstmt.setInt(index++,mTrCrTransType);
	 * pstmt.setString(index++,toproduct.getCashInHandGL());
	 * pstmt.setString(index++,toproduct.getProductGL());
	 * pstmt.setString(index++,""); pstmt.setInt(index++,1);
	 * 
	 * pstmt.executeUpdate();
	 * 
	 * l_Query = "Drop Table " + Tmp_accprepare; pstmt =
	 * conn.prepareStatement(l_Query); pstmt.executeUpdate(); }
	 */

	public boolean createTmpTableForTransaction(String tmp_Transpreapre, Connection l_Conn) throws SQLException {
		boolean ret = false;
		String l_Query = "IF OBJECT_ID(N'" + tmp_Transpreapre + "', N'U') IS NOT NULL DROP TABLE " + tmp_Transpreapre;
		PreparedStatement pstmt = l_Conn.prepareStatement(l_Query);
		pstmt.executeUpdate();

		l_Query = "Create Table " + tmp_Transpreapre + "([autokey] [int] IDENTITY(1,1) NOT NULL,"
				+ "[workstation] [varchar](50) NOT NULL,"
				+ "[userid] [varchar](50) NOT NULL,[authid] [varchar](50) NOT NULL,"
				+ "[amount] [float] NOT NULL,[account] [varchar](50) NOT NULL," + "[brcode] [varchar](50) NOT NULL,"
				+ "[currencyCode] [varchar](20) NOT NULL,"
				+ "[transtime] [datetime] NOT NULL,[transdate] [datetime] NOT NULL,[effdate] [datetime] NOT NULL,"
				+ "[refno] [varchar](50) NOT NULL,[remark] [varchar](255) NOT NULL,"
				+ "[contradate] [datetime] NOT NULL,[status] [int] NOT NULL,[currencyrate] [real] NOT NULL,"
				+ "[baseamount][float] NOT NULL,[desc] [varchar](255) NOT NULL,[transtype] [smallint] NOT NULL,"
				+ "[cashinhandgl] [varchar](50) NOT NULL ,[productgl] [varchar](50) NOT NULL,[chequeno] [varchar](50) NOT NULL,"
				+ "[ismobile] [int] NOT NULL,[prevbalance] [float] NOT NULL,[transdescription] [varchar](255) NOT NULL,[prevupdate] [datetime] NOT NULL)";
		pstmt = l_Conn.prepareStatement(l_Query);
		pstmt.executeUpdate();
		ret = true;
		return ret;
	}

	public boolean createTmpTable(String Tmp_accprepare, Connection l_Conn) throws SQLException {
		boolean ret = false;
		String l_Query = "Create Table " + Tmp_accprepare + " ([autokey] [int] IDENTITY(1,1) NOT NULL,"
				+ "[workstation] [varchar](50) NOT NULL,"
				+ "[userid] [varchar](50) NOT NULL,[authid] [varchar](50) NOT NULL,"
				+ "[amount] [float] NOT NULL,[facc] [varchar](50) NOT NULL,[tacc] [varchar](50) NOT NULL,"
				+ "[fbrcode] [varchar](50) NOT NULL,[tbrcode] [varchar](50) NOT NULL,"
				+ "[fccy] [varchar](20) NOT NULL,[tccy] [varchar](20) NOT NULL,"
				+ "[transtime] [datetime] NOT NULL,[transdate] [datetime] NOT NULL,[effdate] [datetime] NOT NULL,"
				+ "[refno] [varchar](50) NOT NULL,[remark] [varchar](255) NOT NULL,"
				+ "[drccyrate] [float] NOT NULL,[crccyrate] [float] NOT NULL,[fprevbalance] [float] NOT NULL,[tprevbalance] [float] NOT NULL)";
		PreparedStatement pstmt = l_Conn.prepareStatement(l_Query);
		pstmt.executeUpdate();
		ret = true;
		return ret;
	}

	public boolean createTmpTableForGLTransaction(String tmp_GlTranspreapre, Connection l_Conn) throws SQLException {
		boolean ret = false;
		String l_Query = "IF OBJECT_ID(N'" + tmp_GlTranspreapre + "', N'U') IS NOT NULL DROP TABLE "
				+ tmp_GlTranspreapre;
		PreparedStatement pstmt = l_Conn.prepareStatement(l_Query);
		pstmt.executeUpdate();

		l_Query = "Create Table " + tmp_GlTranspreapre + "([autokey] [int] IDENTITY(1,1) NOT NULL,"
				+ "[TransNo] [int] NOT NULL, [AccNumber] [varchar](50) NOT NULL,"
				+ "[GLCode1] [varchar](50) NOT NULL, [GLCode2] [varchar](50) NOT NULL,"
				+ "[GLCode3] [varchar](50) NOT NULL, [GLCode4] [varchar](50) NOT NULL,"
				+ "[GLCode5] [varchar](50) NOT NULL,"
				+ "[BaseCurCode] [varchar](10) NOT NULL, [BaseCurOperator] [varchar](20) NOT NULL,"
				+ "[BaseRate] [float] NOT NULL, [BaseAmount] [float] NOT NULL,"
				+ "[MediumCurCode] [varchar](10) NOT NULL,[MediumCurOperator] [varchar](20) NOT NULL,"
				+ "[MediumRate] [float] NOT NULL, [MediumAmount] [float] NOT NULL,"
				+ "[TrCurCode] [varchar](10) NOT NULL,[TrCurOperator] [varchar](20) NOT NULL,"
				+ "[TrRate] [float] NOT NULL,[TrAmount] [float] NOT NULL," + "[TrPrevBalance] [float]  NOT NULL,"
				+ "[n1] [int] NOT NULL,[n2] [int] NOT NULL,[n3] [int] NOT NULL,"
				+ "[t1] [varchar](50) NOT NULL ,[t2] [varchar](50) NOT NULL," + "[t3] [varchar](50) NOT NULL)";

		pstmt = l_Conn.prepareStatement(l_Query);
		pstmt.executeUpdate();
		ret = true;
		return ret;
	}

	public void droptable(String tmp_table, Connection l_Conn) throws SQLException {
		String l_Query = "IF OBJECT_ID(N'" + tmp_table + "', N'U') IS NOT NULL DROP TABLE " + tmp_table;
		PreparedStatement pstmt = l_Conn.prepareStatement(l_Query);
		pstmt.executeUpdate();
	}

	public DataResult saveTmpTabletoAccountTransaction(String tmp_Transpreapre, Connection conn) throws SQLException {
		DataResult result = new DataResult();

		int l_TranRef = DBTransactionMgr.nextValue();
		int mintransRef = 0;
		String sql = "Insert Into AccountTransaction(BranchCode,WorkStation,TransREf,TellerId,SupervisorId,TransTime,TransDate,Description,"
				+ "ChequeNo,CurrencyCode,CurrencyRate,Amount,TransType,AccNumber,PrevBalance, PrevUpdate,EffectiveDate,ContraDate,Status,Remark, SubRef,AccRef) "
				+ "SELECT brcode,workstation," + l_TranRef
				+ ",userid,authid,transtime,transdate,[desc],chequeno,currencyCode,"
				+ "currencyrate,amount,transtype,account,prevbalance,prevupdate,effdate,'19000101',0,Remark,ISNULL(productgl,''),ISNULL(cashinhandgl,'') FROM "
				+ tmp_Transpreapre + " ORDER BY autokey";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();

		// PreparedStatement pstmt =
		// conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		// int count=pstmt.executeUpdate();
		/*
		 * if(count>0) { ResultSet rs = pstmt.getGeneratedKeys(); if (rs != null &&
		 * rs.next()) mintransRef = (int) (rs.getLong(1) - (count-1)) ; rs.close(); }
		 */

		sql = "SELECT MIN(TransNo) as mintransRef FROM AccountTransaction A INNER JOIN " + tmp_Transpreapre + " T  "
				+ " ON T.account=A.AccNumber AND A.TransType = T.transtype AND A.Description=T.[desc] AND A.TransRef= "
				+ l_TranRef;
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			mintransRef = rs.getInt("mintransRef");

		sql = "Update A Set TransRef=" + mintransRef + " FROM AccountTransaction A INNER JOIN " + tmp_Transpreapre
				+ " T  "
				+ " ON T.account=A.AccNumber AND A.TransType = T.transtype AND A.Description=T.[desc] AND A.TransRef= "
				+ l_TranRef;
		pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();

		// System.out.println("Trans ref ::"+mintransRef+", "+tmp_Transpreapre+" \n");

		pstmt.executeUpdate();
		sql = "Insert Into AccountGLTransaction(TransNo,AccNumber,GLCode1,GLCode2,GLCode3,GLCode4,GLCode5,BaseCurCode,BaseCurOperator,"
				+ "BaseRate,BaseAmount,MediumCurCode, MediumCurOperator,MediumRate,MediumAmount,TrCurOperator,TrRate,TrAmount,TrCurCode,TrPrevBalance, T2, T3) "
				+ "SELECT DISTINCT A.TransNo,A.AccNumber,ISNULL(T.productgl,''),ISNULL(T.cashinhandgl,''),'','','',A.CurrencyCode,'/',A.CurrencyRate,T.baseamount,'','',0,0,'/',A.CurrencyRate,"
				+ "A.Amount,A.CurrencyCode,A.PrevBalance, A.SupervisorID, A.TellerID FROM AccountTransaction A inner join "
				+ tmp_Transpreapre + " T "
				+ "ON T.account=A.AccNumber AND A.TransType = T.transtype AND A.Description=T.[desc] AND A.Amount=T.amount AND A.TransREf = "
				+ mintransRef;
		pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();
		result.setStatus(true);

		/*
		 * sql = "IF OBJECT_ID(N'" + tmp_Transpreapre +
		 * "', N'U') IS NOT NULL DROP TABLE " +tmp_Transpreapre; pstmt =
		 * conn.prepareStatement(sql); pstmt.executeUpdate();
		 */

		result.setTransRef(mintransRef);
		pstmt.close();
		return result;
	}

	public DataResult saveAccountTransaction(ArrayList<TransferData> transferlist, Connection conn)
			throws SQLException {
		DataResult result = new DataResult();
//			long l_TranRef = GeneralUtility.generateSyskey();
		int l_TranRef = DBTransactionMgr.nextValue();
		int lasttransno = 0;
		String sql = "";
		PreparedStatement pstmt;
		TransferData transferdata = new TransferData();
		sql = "Insert Into AccountTransaction(BranchCode,WorkStation,TransREf,TellerId,SupervisorId,TransTime,TransDate,Description,"
				+ "ChequeNo,CurrencyCode,CurrencyRate,Amount,TransType,AccNumber,PrevBalance, PrevUpdate,EffectiveDate,ContraDate,Status,Remark, SubRef,AccRef) "
				+ "Values ";

		for (int i = 0; i < transferlist.size(); i++) {
			if (i == 0) {
				sql += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			} else {
				sql += ",(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
		}
		sql += "SELECT SCOPE_IDENTITY() as lasttransno;";
		pstmt = conn.prepareStatement(sql);

		int index = 1;
		for (int i = 0; i < transferlist.size(); i++) {
			transferdata = transferlist.get(i);
			index = updateRecord_update(transferdata, l_TranRef, index, pstmt);
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

	private int updateRecord(TransferData transferData, Long transref, int index, PreparedStatement aPS)
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

	private int updateRecord_update(TransferData transferData, int transref, int index, PreparedStatement aPS)
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

	/*
	 * public SMSReturnData postTransferValid(SMSTransferData pTransferData) throws
	 * Exception { SMSReturnData l_SMSReturnData = new SMSReturnData(); DataResult
	 * l_DataResult = new DataResult(); ArrayList<String> l_ArlLogString = new
	 * ArrayList<String>(); String l_EffectiveDate = ""; m_AutoPrintTransRef = 0;
	 * boolean isLink = false; LastDatesDAO l_LastDao = new LastDatesDAO(); String
	 * l_TableName = "", l_TableName1 = ""; Connection l_Conn1 = null; String
	 * l_TodayDate = GeneralUtility.getTodayDate(); String l_TransactionTime =
	 * getCurrentDateYYYYMMDDHHMMSS(); Connection l_Conn = ConnAdmin.getConn("001",
	 * "");
	 * 
	 * if
	 * (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4()
	 * == 1) { boolean isruneod=true; if (GeneralUtility.datetimeformatHHMM())
	 * { l_EffectiveDate = GeneralUtility.getTomorrowDate(); isruneod =
	 * l_LastDao.isRunnedEOD(l_TodayDate, l_Conn); }else { String[] result =
	 * getEffectiveTransDateNew(l_TodayDate,l_Conn); l_EffectiveDate = result[0];
	 * if(result[1].equalsIgnoreCase("EOD")) { isruneod = true; } } int comparedate
	 * = StrUtil.compareDate(l_EffectiveDate, l_TodayDate); if (comparedate > 0) {
	 * if (isruneod) l_TableName1 = "Accounts_Balance"; l_TableName = "Accounts"; }
	 * else { l_TableName = "Accounts"; l_TableName1 = "Accounts_Balance"; } }else {
	 * l_TableName = "Accounts"; l_TableName1 = ""; l_EffectiveDate =
	 * getEffectiveTransDateNew(l_TodayDate,l_Conn)[0]; }
	 * 
	 * l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
	 * pTransferData.setTransactionDate(l_TodayDate);
	 * pTransferData.setTransactionTime(l_TransactionTime);
	 * pTransferData.setEffectiveDate(l_EffectiveDate);
	 * 
	 * //================ prepare Account Transaction ========================
	 * //String Query =
	 * "autokey,workstation,userid,authid, amount,facc,tacc, fbrcode,tbrcode,fccy,tccy,transtime,transdate,effdate,refno,remark,drCCYRate,drCCYRate"
	 * ; String Tmp_accprepare = "Tmp_accprepare"
	 * +pTransferData.getFromAccNumber()+""+
	 * GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace(); String tmp_Transpreapre
	 * = "Tmp_transprepare" +pTransferData.getFromAccNumber()+""+
	 * GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace(); //String
	 * tmp_GlTranspreapre = "Tmp_gltransprepare"
	 * +pTransferData.getFromAccNumber()+""+
	 * GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();
	 * 
	 * createTmpTable(Tmp_accprepare, l_Conn);
	 * createTmpTableForTransaction(tmp_Transpreapre, l_Conn);
	 * //createTmpTableForGLTransaction(tmp_GlTranspreapre, l_Conn);
	 * 
	 * l_DataResult =
	 * prepareAccountTransaction(pTransferData,Tmp_accprepare,tmp_Transpreapre,
	 * l_Conn);
	 * 
	 * try { if (l_DataResult.getStatus()) { //skip Transaction // Thread Testing if
	 * (l_DataResult.getStatus()) { l_SMSReturnData.setStatus(true);
	 * l_SMSReturnData.setCode("0000");
	 * l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
	 * l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
	 * l_SMSReturnData.setDescription("Posted successfully");
	 * 
	 * } else { l_SMSReturnData.setStatus(false); l_SMSReturnData.setCode("0014");
	 * if (isLink) { l_SMSReturnData.setDescription( l_DataResult.getDescription() +
	 * " and Already Posting Transaction For Link!"); } else {
	 * l_SMSReturnData.setDescription(l_DataResult.getDescription()); } }
	 * 
	 * } else { l_SMSReturnData.setDescription(l_DataResult.getDescription());
	 * l_SMSReturnData.setStatus(false); l_SMSReturnData.setCode("0014"); return
	 * l_SMSReturnData; } if (!l_Conn.isClosed()) { l_Conn.close(); } } catch
	 * (Exception e) { e.printStackTrace(); l_SMSReturnData.setStatus(false);
	 * l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
	 * l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() +
	 * e.getMessage()); l_SMSReturnData.setErrorCode("06"); }
	 * 
	 * return l_SMSReturnData; }
	 * 
	 * 
	 * public SMSReturnData postTransferTrans(SMSTransferData pTransferData) throws
	 * Exception { SMSReturnData l_SMSReturnData = new SMSReturnData(); DataResult
	 * l_DataResult = new DataResult(); ArrayList<String> l_ArlLogString = new
	 * ArrayList<String>(); String l_EffectiveDate = ""; m_AutoPrintTransRef = 0;
	 * boolean isLink = false; String l_TableName = "", l_TableName1 = ""; String
	 * l_TodayDate = GeneralUtility.getTodayDate(); String l_TransactionTime =
	 * getCurrentDateYYYYMMDDHHMMSS(); Connection l_Conn = ConnAdmin.getConn("001",
	 * ""); l_Conn.setAutoCommit(false);
	 * 
	 * l_TableName = "Accounts"; l_SMSReturnData.setEffectiveDate(l_TodayDate);
	 * pTransferData.setTransactionDate(l_TodayDate);
	 * pTransferData.setTransactionTime(l_TransactionTime);
	 * pTransferData.setEffectiveDate(l_TodayDate);
	 * 
	 * //================ prepare Account Transaction ========================
	 * //String Query =
	 * "autokey,workstation,userid,authid, amount,facc,tacc, fbrcode,tbrcode,fccy,tccy,transtime,transdate,effdate,refno,remark,drCCYRate,drCCYRate"
	 * ; String Tmp_accprepare = "Tmp_accprepare"
	 * +pTransferData.getFromAccNumber()+""+
	 * GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace(); String tmp_Transpreapre
	 * = "Tmp_transprepare" +pTransferData.getFromAccNumber()+""+
	 * GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();
	 * 
	 * createTmpTable(Tmp_accprepare, l_Conn);
	 * createTmpTableForTransaction(tmp_Transpreapre, l_Conn);
	 * //createTmpTableForGLTransaction(tmp_GlTranspreapre, l_Conn);
	 * 
	 * l_DataResult =
	 * prepareAccountOnlyTransaction(pTransferData,Tmp_accprepare,tmp_Transpreapre,
	 * l_Conn);
	 * 
	 * try { if (l_DataResult.getStatus()) {
	 * 
	 * l_ArlLogString.add("	Post Transaction "); AccountDao lAccDAO= new
	 * AccountDao(); //update Balance l_DataResult =
	 * lAccDAO.updateAccountBalanceCurrency( tmp_Transpreapre, l_TableName,
	 * l_TableName1, l_Conn); if(l_DataResult.getStatus()) { l_DataResult =
	 * saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn); }
	 * 
	 * // Thread Testing if (l_DataResult.getStatus()) { l_Conn.commit();
	 * l_SMSReturnData.setStatus(true); l_SMSReturnData.setCode("0000");
	 * l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
	 * l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
	 * l_SMSReturnData.setDescription("Posted successfully");
	 * 
	 * } else { l_SMSReturnData.setStatus(false); l_SMSReturnData.setCode("0014");
	 * if (isLink) { l_SMSReturnData.setDescription( l_DataResult.getDescription() +
	 * " and Already Posting Transaction For Link!"); } else {
	 * l_SMSReturnData.setDescription(l_DataResult.getDescription()); }
	 * l_Conn.rollback(); }
	 * 
	 * } else { l_SMSReturnData.setDescription(l_DataResult.getDescription());
	 * l_SMSReturnData.setStatus(false); l_SMSReturnData.setCode("0014"); return
	 * l_SMSReturnData; } if (!l_Conn.isClosed()) { l_Conn.close(); } } catch
	 * (Exception e) { e.printStackTrace(); l_SMSReturnData.setStatus(false);
	 * l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
	 * l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() +
	 * e.getMessage()); l_SMSReturnData.setErrorCode("06"); }
	 * 
	 * return l_SMSReturnData; }
	 * 
	 * public DataResult prepareAccountOnlyTransaction(SMSTransferData
	 * pTransferData,String Tmp_accprepare,String tmp_Transpreapre, Connection
	 * l_Conn ) throws Exception { DataResult l_DataResult = new DataResult();
	 * double drCCYRate = 1; double crCCYRate = 1;
	 * 
	 * pTransferData.setFromBranchCode("001"); pTransferData.setToBranchCode("001");
	 * pTransferData.setCurrencyCode("MMK"); pTransferData.setFromCCY("MMK");
	 * pTransferData.setToCCY("MMK");
	 * 
	 * 
	 * String l_Query = "INSERT INTO "
	 * +Tmp_accprepare+" VALUES('Mobile',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	 * PreparedStatement pstmt = l_Conn.prepareStatement(l_Query); int index=1;
	 * pstmt.setString(index++, pTransferData.getUserID()); pstmt.setString(index++,
	 * SharedLogic.getSystemData().getpAuthorizerID()); pstmt.setDouble(index++,
	 * pTransferData.getAmount()); pstmt.setString(index++,
	 * pTransferData.getFromAccNumber()); pstmt.setString(index++,
	 * pTransferData.getToAccNumber()); pstmt.setString(index++,
	 * pTransferData.getFromBranchCode()); pstmt.setString(index++,
	 * pTransferData.getToBranchCode()); pstmt.setString(index++,
	 * pTransferData.getFromCCY()); pstmt.setString(index++,
	 * pTransferData.getToCCY() ); pstmt.setString(index++,
	 * pTransferData.getTransactionTime()); pstmt.setString(index++,
	 * pTransferData.getTransactionDate()); pstmt.setString(index++,
	 * pTransferData.getEffectiveDate()); pstmt.setString(index++,
	 * pTransferData.getReferenceNo()); pstmt.setString(index++,
	 * pTransferData.getRemark()); pstmt.setDouble(index++, drCCYRate);
	 * pstmt.setDouble(index++, crCCYRate); pstmt.setDouble(index++, 0);
	 * pstmt.setDouble(index++, 0); pstmt.executeUpdate();
	 * 
	 * prepareTransferOlny(tmp_Transpreapre,Tmp_accprepare, l_Conn);
	 * 
	 * l_DataResult.setStatus(true); return l_DataResult; }
	 * 
	 * private void prepareTransferOlny( String tmpTable,String
	 * Tmp_accprepare,Connection conn) throws Exception {
	 * 
	 * int index = 1; // Dr From Customer String l_Query = "INSERT INTO " + tmpTable
	 * +
	 * " Select workstation,userid,authid,amount,facc,fbrcode,fccy,transtime,transdate,effdate,refno,remark,"
	 * +
	 * "'19000101',1,drccyrate,amount*drccyrate,tacc+?,?,?,?,?,?,fprevbalance from "
	 * +Tmp_accprepare; PreparedStatement pstmt = conn.prepareStatement(l_Query);
	 * pstmt.setString(index++,", Mobile Transfer" + ", " +
	 * GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
	 * pstmt.setInt(index++,mTrDrTransType); pstmt.setString(index++,"");
	 * pstmt.setString(index++,""); pstmt.setString(index++,"");
	 * pstmt.setInt(index++,1);
	 * 
	 * pstmt.executeUpdate(); // Cr To Customer l_Query = "INSERT INTO " + tmpTable
	 * +
	 * " Select workstation,userid,authid,(CASE WHEN fccy=tccy THEN amount ELSE (amount*drccyrate)/crccyrate END) * (drccyrate*1000)/1000 ,"
	 * + " tacc,tbrcode,tccy,transtime,transdate,effdate,refno,remark," +
	 * " '19000101',1,crccyrate,amount*drccyrate,facc+?,?,?,?,?,?,tprevbalance from "
	 * +Tmp_accprepare; pstmt = conn.prepareStatement(l_Query); index = 1;
	 * pstmt.setString(index++, ", Mobile  Transfer" + ", " +
	 * GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
	 * pstmt.setInt(index++,mTrCrTransType); pstmt.setString(index++,"");
	 * pstmt.setString(index++,""); pstmt.setString(index++,"");
	 * pstmt.setInt(index++,1);
	 * 
	 * pstmt.executeUpdate();
	 * 
	 * l_Query = "Drop Table " + Tmp_accprepare; pstmt =
	 * conn.prepareStatement(l_Query); pstmt.executeUpdate(); }
	 */

	public static String[] getEffectiveTransDateNew(String aDate, Connection conn) throws Exception {
		String[] result = null;
		try {
			BankHolidayDAO l_BankHolidayDAO = new BankHolidayDAO();
			LastDatesDAO l_LastDateDAO = new LastDatesDAO();
			result = getEffectiveTransDateNew(aDate, l_BankHolidayDAO, l_LastDateDAO, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String[] getEffectiveTransDateNew(String pDate, BankHolidayDAO pDAO, LastDatesDAO pLastDateDAO,
			Connection conn) throws SQLException {
		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));
		Calendar cal = Calendar.getInstance();
		month--; // In Java Calendar, month is starting zero.
		cal.set(year, month, day);

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String[] result = { "", "" };
		String effectiveDate = pDate;
		// Checking is Runned EOD
		if (pLastDateDAO.isRunnedEOD(pDate, conn)) {
			cal.clear();
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			result = getEffectiveTransDateNew(effectiveDate, pDAO, pLastDateDAO, conn);
			result[1] = "EOD";
		} else if (StrUtil.isWeekEnd(pDate)) {
			cal.clear();
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			result = getEffectiveTransDateNew(effectiveDate, pDAO, pLastDateDAO, conn);
			result[1] = "WeekEnd";
		} else if (pDAO.getBankHolidayCheck(pDate, conn)) { // Checking
															// BankHoliday
			cal.set(year, month, ++day);
			effectiveDate = df.format(cal.getTime());
			result = getEffectiveTransDateNew(effectiveDate, pDAO, pLastDateDAO, conn);
			result[1] = "Holiday";
		} else {
			effectiveDate = df.format(cal.getTime());
			result[0] = effectiveDate;
		}
		return result;
	}

	public SMSReturnData postTransferInternal(TransferData pTransferData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";
		String l_TodayDate = GeneralUtility.getTodayDate(), tomorrowDate = "";
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		AccountTransactionDAO accTransDao = new AccountTransactionDAO();
		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		TransferDataResult transferDataResult = new TransferDataResult();
		transferDataResult = prepareAccountTransactionForInternalTransferOLTPUpdateQuery(pTransferData, l_TableName,
				l_TableName1, l_Conn);

		try {
			if (transferDataResult.isStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				transferDataResult = lAccDAO.updateAccountBalance_TransferUpdate(transferDataResult, l_TableName,
						l_TableName1, l_Conn);
				if (transferDataResult.isStatus()) {
					l_DataResult = lAccDAO.saveAccountTransaction_Transfer(transferDataResult.getTransferlist(),
							l_Conn);
				}

				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef
					if (transferDataResult.getAutoLinkTransRef().size() > 0) {
						accTransDao.addAutoLinkTransRefUpdate(transferDataResult.getAutoLinkTransRef(),
								l_DataResult.getTransRef(), l_Conn);
					}
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}
			} else {
				l_SMSReturnData.setDescription(transferDataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}

	public SMSReturnData domerchanttransfernew(TransferData pTransferData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tomorrowdate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		// // by
		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountTransactionDAO accTransDao = new AccountTransactionDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowdate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tomorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowdate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		TransferDataResult transferDataResult = new TransferDataResult();
		transferDataResult = prepareMerchantTransaction(pTransferData, l_TableName, l_TableName1, l_Conn);

		try {

			if (transferDataResult.isStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				transferDataResult = lAccDAO.updateAccountBalanceMulti(transferDataResult, l_TableName, l_TableName1,
						l_Conn);
				if (transferDataResult.isStatus()) {
					l_DataResult = saveAccountTransaction(transferDataResult.getTransferlist(), l_Conn);
				} else {
					l_DataResult.setDescription(transferDataResult.getDescription());
				}
				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef
					if (transferDataResult.getAutoLinkTransRef().size() > 0) {
						accTransDao.addAutoLinkTransRefUpdate(transferDataResult.getAutoLinkTransRef(),
								l_DataResult.getTransRef(), l_Conn);
					}
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}
			} else {
				l_SMSReturnData.setDescription(transferDataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}

		} catch (Exception e) {

			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}

	public TransferDataResult prepareMerchantTransaction(TransferData pTransferData, String l_TableName,
			String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "";
		String pByForceCheque = "", desc = "Wallet Top Up, ",ibtdesc = "IBT, Wallet Top Up, ";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		boolean isGL = false;
		boolean isSameBranch = false;
		ArrayList<Integer> m_AutoLinkTransRef = new ArrayList<Integer>();
		TransferDataResult transferDataResult = new TransferDataResult();
		TransferData prepareTransferData = new TransferData();
		ArrayList<TransferData> transferlist = new ArrayList<TransferData>();

		l_DataResult = l_AccDAO.getTransAccountDebit(pTransferData.getFromAccNumber(), l_Conn);
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
		}

		if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData.getToAccNumber(), l_Conn)) {
			l_DataResult = l_AccDAO.getTransAccountCredit(pTransferData.getToAccNumber(), l_Conn);
			if (l_DataResult.getStatus()) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
			}
		} else {
			isGL = true;
			isSameBranch = true;
			pTransferData.setToCCY(l_FromAccountData.getCurrencyCode());
			pTransferData.setToBranchCode(l_FromAccountData.getBranchCode());
		}

		if (l_DataResult.getStatus()) {
			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				if (!isGL)
					tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				fkey = l_FromAccountData.getProduct();
				if (!isGL)
					tkey = l_ToAccountData.getProduct();
			}
			double f_MultipleOf = 0, t_MultipleOf = 0;
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			f_MultipleOf = fromproduct.getMultipleOf();
			if (!isGL) {
				toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
				t_MultipleOf = toproduct.getMultipleOf();
			}

			// For Checking Transaction
			// Check Multiple Of

			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if ((int) (pTransferData.getAmount() % f_MultipleOf) != 0) {
				l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
				l_DataResult.setStatus(false);
			} else {
				// if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = fromproduct.getMinWithdrawal();
				if (pTransferData.getAmount() < l_MinWithdrawal) {
					l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
					l_DataResult.setStatus(false);
				}
			}
			if (l_DataResult.getStatus() && !isGL) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus()) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinBalance(), 0,
					l_Conn);
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (pTransferData.getAmount() > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (pTransferData.getAmount() != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}

			if (!l_DataResult.getStatus()) {
				l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrencyUpdate(pTransferData,
						l_FromAccountData, l_Available, pTransferData.getAmount(), l_TableName, l_TableName1, l_Conn);
				if (l_DataResult.getStatus()) {
					m_AutoLinkTransRef.add(l_DataResult.getTransactionNumber());
					if (l_DataResult.getTransRef() != 0)
						m_AutoLinkTransRef.add(l_DataResult.getTransRef());
					transferDataResult.setAutoLinkTransRef(m_AutoLinkTransRef);
				}

			}
		}
		if (l_DataResult.getStatus()) {
			String fkey1 = "", tkey1 = "";

			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
			pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());

			fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
			drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			if (!isGL) {
				pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
				pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());
				tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();
				crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);

				if (l_FromAccountData.getBranchCode().equalsIgnoreCase(l_ToAccountData.getBranchCode())) {
					isSameBranch = true;
				}

			}
		}
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		PreparedStatement pstmt = null;
		String l_Query = "";
		String l_Com1GL = "", l_Com2GL = "";

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
				.get("WLCOM1" + pTransferData.getCurrencyCode() +pTransferData.getFromBranchCode());
		if (l_RefData != null)
			l_Com1GL = l_RefData.getGLCode();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("WLCOM2"
				+ pTransferData.getCurrencyCode() + pTransferData.getToBranchCode());
		if (l_RefData != null)
			l_Com2GL = l_RefData.getGLCode();

		if ((l_Com1GL.equals("") && pTransferData.getComm1() > 0)
				|| (l_Com2GL.equals("") && pTransferData.getComm2() > 0)) {
			transferDataResult.setStatus(false);
			transferDataResult.setCode("0014");
			transferDataResult.setDescription("Invalid GL Account!");
			return transferDataResult;
		}
		if (l_DataResult.getStatus()) {

			double totalfee = 0, amount = 0, comm1Amt = 0, comm2Amt = 0;
			amount = pTransferData.getAmount();
			comm1Amt = pTransferData.getComm1();
			comm2Amt = pTransferData.getComm2();
			if (pTransferData.isInclusive())
				amount = amount - (comm1Amt + comm2Amt);

			totalfee = amount + comm1Amt + comm2Amt;

			if (isSameBranch) {
				int index = 1;
				// Dr From Customer
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData.getUserID());
				prepareTransferData.setAmount(totalfee);
				prepareTransferData.setFromAccNumber(pTransferData.getFromAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData.getFromCCY());
				prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
				prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
				prepareTransferData.setRemark(pTransferData.getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData.getToAccNumber());
				prepareTransferData.setIbtTransType(mWLTrDrTransType);
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL(fromproduct.getProductGL());
				prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
				prepareTransferData.setPrevBalance(l_FromAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(l_FromAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData.getTransdescription());
				prepareTransferData.setIsGL(false);
				transferlist.add(prepareTransferData);

				// Cr GL or Customer
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData.getUserID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					prepareTransferData.setAmount(amount * (drCCYRate * 1000) / 1000);
				else
					prepareTransferData.setAmount((amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(pTransferData.getToAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
				prepareTransferData.setFromCCY(pTransferData.getToCCY());
				prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
				prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
				prepareTransferData.setRemark(pTransferData.getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(amount * drCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
				prepareTransferData.setIbtTransType(mWLTrCrTransType);
				prepareTransferData.setCashInHandGL(isGL ? fromproduct.getCashInHandGL() : toproduct.getCashInHandGL());
				prepareTransferData.setProductGL(isGL ? "" : toproduct.getProductGL());
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(isGL ? 0 : l_ToAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(isGL ? "19000101" :l_ToAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData.getTransdescription());
				prepareTransferData.setIsGL(isGL);
				transferlist.add(prepareTransferData);

				if (comm1Amt > 0) {
					// Cr Com GL
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(comm1Amt * (drCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount((comm1Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(l_Com1GL);
					prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getFromCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(comm1Amt * drCCYRate));
					prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
					prepareTransferData.setIbtTransType(mWLTrCrTransType);
					prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
					prepareTransferData.setProductGL("");
					prepareTransferData.setpByForceCheque("");
					prepareTransferData.setPrevBalance(0);
					prepareTransferData.setPreviousDate("19000101");
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setIsGL(true);
					transferlist.add(prepareTransferData);
				}

				if (comm2Amt > 0) {
					// Cr Com G
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(comm2Amt * (drCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount((comm2Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(l_Com2GL);
					prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getFromCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(comm2Amt * drCCYRate));
					prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
					prepareTransferData.setIbtTransType(mWLTrCrTransType);
					prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
					prepareTransferData.setProductGL("");
					prepareTransferData.setpByForceCheque("");
					prepareTransferData.setPrevBalance(0);
					prepareTransferData.setPreviousDate("19000101");
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setIsGL(true);
					transferlist.add(prepareTransferData);
				}

			} else {
				// different branch
				String l_IBTGLFrom = "";
				String l_IBTGLTo = "";

				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
						.get("MTRDR" + pTransferData.getFromCCY() + pTransferData.getFromBranchCode()
								+ fromproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLFrom = l_RefData.getGLCode();
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRCR"
						+ pTransferData.getToCCY() + pTransferData.getToBranchCode() + toproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLTo = l_RefData.getGLCode();

				if (!(l_IBTGLFrom.equals("") || l_IBTGLTo.equals(""))) {
					// Dr From Customer
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					prepareTransferData.setAmount(totalfee);
					prepareTransferData.setFromAccNumber(pTransferData.getFromAccNumber());
					prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getFromCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(totalfee * drCCYRate));
					prepareTransferData.setPostDescription(desc + pTransferData.getToAccNumber());
					prepareTransferData.setIbtTransType(mWLTrDrTransType);
					prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
					prepareTransferData.setProductGL(fromproduct.getProductGL());
					prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
					prepareTransferData.setPrevBalance(l_FromAccountData.getCurrentBalance());
					prepareTransferData.setPreviousDate(l_FromAccountData.getLastUpdate());
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setIsGL(false);
					transferlist.add(prepareTransferData);

					// Cr IBT GL
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(amount * (drCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount((amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(l_IBTGLFrom);
					prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getFromCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(amount * drCCYRate));
					prepareTransferData.setPostDescription(ibtdesc + pTransferData.getFromAccNumber());
					prepareTransferData.setIbtTransType(mWLTrCrTransType);
					prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
					prepareTransferData.setProductGL("");
					prepareTransferData.setpByForceCheque("");
					prepareTransferData.setPrevBalance(0);
					prepareTransferData.setPreviousDate("19000101");
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setIsGL(true);
					transferlist.add(prepareTransferData);

					// Dr IBT GL
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(amount * (drCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount((amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(l_IBTGLTo);
					prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getToCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(amount * drCCYRate));
					prepareTransferData.setPostDescription(ibtdesc + pTransferData.getToAccNumber());
					prepareTransferData.setIbtTransType(mWLTrDrTransType);
					prepareTransferData.setCashInHandGL(toproduct.getCashInHandGL());
					prepareTransferData.setProductGL("");
					prepareTransferData.setpByForceCheque("");
					prepareTransferData.setPrevBalance(0);
					prepareTransferData.setPreviousDate("19000101");
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setIsGL(true);
					transferlist.add(prepareTransferData);

					// Cr To Account
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(amount * (drCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount((amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(pTransferData.getToAccNumber());
					prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getToCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(amount * drCCYRate));
					prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
					prepareTransferData.setIbtTransType(mWLTrCrTransType);
					prepareTransferData.setCashInHandGL(toproduct.getCashInHandGL());
					prepareTransferData.setProductGL(toproduct.getProductGL());
					prepareTransferData.setpByForceCheque("");
					prepareTransferData.setPrevBalance(l_ToAccountData.getCurrentBalance());
					prepareTransferData.setPreviousDate(l_ToAccountData.getLastUpdate());
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setIsGL(false);
					transferlist.add(prepareTransferData);

					if (comm1Amt > 0) {
						// Cr Com GL
						prepareTransferData = new TransferData();
						prepareTransferData.setUserID(pTransferData.getUserID());
						if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
							prepareTransferData.setAmount(comm1Amt * (drCCYRate * 1000) / 1000);
						else
							prepareTransferData
									.setAmount((comm1Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
						prepareTransferData.setFromAccNumber(l_Com1GL);
						prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
						prepareTransferData.setFromCCY(pTransferData.getFromCCY());
						prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
						prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
						prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
						prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
						prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
						prepareTransferData.setRemark(pTransferData.getRemark());
						prepareTransferData.setBasecurrate(drCCYRate);
						prepareTransferData.setBaseAmount(StrUtil.round2decimals(comm1Amt * drCCYRate));
						prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
						prepareTransferData.setIbtTransType(mWLTrCrTransType);
						prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
						prepareTransferData.setProductGL("");
						prepareTransferData.setpByForceCheque("");
						prepareTransferData.setPrevBalance(0);
						prepareTransferData.setPreviousDate("19000101");
						prepareTransferData.setTransdescription(pTransferData.getTransdescription());
						prepareTransferData.setIsGL(true);
						transferlist.add(prepareTransferData);
					}

					if (comm2Amt > 0) {
						// Cr Com G
						prepareTransferData = new TransferData();
						prepareTransferData.setUserID(pTransferData.getUserID());
						if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
							prepareTransferData.setAmount(comm2Amt * (drCCYRate * 1000) / 1000);
						else
							prepareTransferData
									.setAmount((comm2Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
						prepareTransferData.setFromAccNumber(l_Com2GL);
						prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
						prepareTransferData.setFromCCY(pTransferData.getFromCCY());
						prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
						prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
						prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
						prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
						prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
						prepareTransferData.setRemark(pTransferData.getRemark());
						prepareTransferData.setBasecurrate(drCCYRate);
						prepareTransferData.setBaseAmount(StrUtil.round2decimals(comm2Amt * drCCYRate));
						prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
						prepareTransferData.setIbtTransType(mWLTrCrTransType);
						prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
						prepareTransferData.setProductGL("");
						prepareTransferData.setpByForceCheque("");
						prepareTransferData.setPrevBalance(0);
						prepareTransferData.setPreviousDate("19000101");
						prepareTransferData.setTransdescription(pTransferData.getTransdescription());
						prepareTransferData.setIsGL(true);
						transferlist.add(prepareTransferData);
					}

				} else {
					l_DataResult.setStatus(false);
					l_DataResult.setCode("0014");
					l_DataResult.setDescription("Invalid GL Account!");
				}
			}

		}
		if (!l_DataResult.getStatus()) {
			transferDataResult.setStatus(false);
			transferDataResult.setCode("0014");
			transferDataResult.setDescription(l_DataResult.getDescription());
		} else {
			transferDataResult.setTransferlist(transferlist);
			transferDataResult.setStatus(true);
		}

		return transferDataResult;
	}

	public gopaymentresponse dopaymenttopup(TransferData[] pTransferData, Connection l_Conn) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tommorrowdate = "";
		m_AutoPrintTransRef = 0;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountTransactionDAO accTransDao = new AccountTransactionDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tommorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tommorrowdate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tommorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tommorrowdate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}
		pTransferData[0].setTransactionDate(l_TodayDate);
		pTransferData[0].setTransactionTime(l_TransactionTime);
		pTransferData[0].setEffectiveDate(l_EffectiveDate);
		// ================ prepare Account Transaction ========================
		TransferDataResult transferDataResult = new TransferDataResult();

		transferDataResult = preparePaymentTopupTransaction(pTransferData, l_TableName, l_TableName1, l_Conn);

		try {
			// End Preparation
			// Check Transaction and post link
			// Check Transaction Status
			if (transferDataResult.isStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				transferDataResult = lAccDAO.updateAccountBalanceMultiUpdate(transferDataResult, l_TableName,
						l_TableName1, l_Conn);
				if (transferDataResult.isStatus()) {
					l_DataResult = saveAccountTransaction(transferDataResult.getTransferlist(), l_Conn);
				}

				if (l_DataResult.getStatus()) {
					res.setRetcode("300");
					res.setTransdate(GeneralUtil.getDate());
					res.setBankrefnumber(String.valueOf(l_DataResult.getTransRef()));
					res.setRetmessage("Payment Topup Successfully");
					res.setEffectivedate(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(l_EffectiveDate));
					// Update Auto link Transfef ynw test
					if (transferDataResult.getAutoLinkTransRef().size() > 0) {
						accTransDao.addAutoLinkTransRefUpdate(transferDataResult.getAutoLinkTransRef(),
								l_DataResult.getTransRef(), l_Conn);
					}

				} else {
					res.setRetcode("210");
					res.setRetmessage(l_DataResult.getDescription());
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(l_DataResult.getDescription());
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}

		return res;
	}

	public TransferDataResult preparePaymentTopupTransaction(TransferData[] pTransferData, String l_TableName,
			String l_TableName1, Connection l_Conn) throws Exception {
		TransferDataResult transferDataResult = new TransferDataResult();
		TransferData prepareTransferData = new TransferData();
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		HashMap<String, AccountData> l_FromAccountList = new HashMap<String, AccountData>();
		HashMap<String, AccountData> l_ToAccountList = new HashMap<String, AccountData>();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "", pByForceCheque = "", l_Query = "", desc = "Mobile Top Up , ",ibtdesc = "IBT, Mobile Top Up, ";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		boolean isGL = true, isautolink = false;
		PreparedStatement pstmt = null;
		int lUncommTransNo = DBTransactionMgr.nextValue();
		l_FromAccountList.put("", null);
		l_ToAccountList.put("", null);
		ArrayList<Integer> m_AutoLinkTransRef = new ArrayList<Integer>();
		ArrayList<TransferData> transferlist = new ArrayList<TransferData>();

		for (int i = 0; i < pTransferData.length; i++) {
			isGL = true;
			pTransferData[i].setTransactionDate(pTransferData[0].getTransactionDate());
			pTransferData[i].setTransactionTime(pTransferData[0].getTransactionTime());
			pTransferData[i].setEffectiveDate(pTransferData[0].getEffectiveDate());

			if (!l_FromAccountList.containsKey(pTransferData[i].getFromAccNumber())) {
				l_DataResult = l_AccDAO.getTransAccountDebit(pTransferData[i].getFromAccNumber(), l_Conn);
			} else {
				l_DataResult.setStatus(true);
				l_AccDAO.setAccountsBean(l_FromAccountList.get(pTransferData[i].getFromAccNumber()));
			}
			if (l_DataResult.getStatus()) {
				l_FromAccountData = l_AccDAO.getAccountsBean();
				pTransferData[i].setToCCY(l_FromAccountData.getCurrencyCode());
				l_FromAccountList.put(pTransferData[i].getFromAccNumber(), l_FromAccountData);
			}

			if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData[i].getToAccNumber(), l_Conn)) {
				isGL = false;
				l_DataResult.setStatus(false);
				if (!l_ToAccountList.containsKey(pTransferData[i].getToAccNumber())) {
					l_DataResult = l_AccDAO.getTransAccountCredit(pTransferData[i].getToAccNumber(), l_Conn);
				} else {
					l_DataResult.setStatus(true);
					l_AccDAO.setAccountsBean(l_ToAccountList.get(pTransferData[i].getToAccNumber()));
				}
				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					pTransferData[i].setToCCY(l_ToAccountData.getCurrencyCode());
					l_ToAccountList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
					} else {
						tkey = l_ToAccountData.getProduct();
					}
					toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
				} else {
					l_DataResult.setStatus(false);
					l_DataResult.setCode("210");
					l_DataResult.setDescription("Invalid Account Number!");
				}
			}

			if (l_DataResult.getStatus()) {
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
				}
				double f_MultipleOf = 0;
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
				f_MultipleOf = fromproduct.getMultipleOf();
				// For Checking Transaction
				// Check Multiple Of

				// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
				// < 999) { // For Debit // Transfer Case
				if ((int) (pTransferData[i].getAmount() % f_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType > 500 && transType != 969) {
					double l_MinWithdrawal = fromproduct.getMinWithdrawal();
					if (pTransferData[i].getAmount() < l_MinWithdrawal) {
						l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
						l_DataResult.setStatus(false);
					}
				}
			}

			// check Debit Available Balance
			if (l_DataResult.getStatus()) {
				double l_Available = 0;
				l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinBalance(), 0,
						l_Conn);
				// Check For A/C Status Closed Pending
				if (l_FromAccountData.getStatus() != 2) {
					if (pTransferData[i].getAmount() > l_Available) {
						l_DataResult.setStatus(false);
						l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_Available)));
					}
				} else {
					double l_CurrentBal = l_FromAccountData.getCurrentBalance();
					if (pTransferData[i].getAmount() != l_CurrentBal) {
						l_DataResult.setStatus(false);
						l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_CurrentBal)));
					}
				}
				if (!l_DataResult.getStatus()) {
					l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrencyUpdate(pTransferData[i],
							l_FromAccountData, l_Available, pTransferData[i].getAmount(), l_TableName, l_TableName1,
							l_Conn);
					// l_DataResult =
					// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
					// l_FromAccountData, l_TableName, l_TableName1, l_Available,
					// pTransferData.getAmount(), l_Conn);
					if (l_DataResult.getStatus()) {
						m_AutoLinkTransRef.add(l_DataResult.getTransactionNumber());
						if (l_DataResult.getTransRef() != 0)
							m_AutoLinkTransRef.add(l_DataResult.getTransRef());

					}

				}
			}

			if (l_DataResult.getStatus()) {
				String fkey1 = "";

				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
				pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
				pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setpByForceCheque(pByForceCheque);
				fkey1 = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_" + pTransferData[i].getFromBranchCode();
				drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);

			}

			if (l_DataResult.getStatus()) {

				prepareTransferData = new TransferData();
				// Dr From Customer
				prepareTransferData.setUserID(pTransferData[i].getUserID());
				prepareTransferData.setAmount(pTransferData[i].getAmount());
				prepareTransferData.setFromAccNumber(pTransferData[i].getFromAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData[i].getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData[i].getFromCCY());
				prepareTransferData.setCurrencyCode(pTransferData[i].getFromCCY());
				prepareTransferData.setpTransactionTime(pTransferData[i].getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData[i].getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData[i].getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData[i].getReferenceNo());
				prepareTransferData.setRemark(pTransferData[i].getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * drCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData[i].getToAccNumber());
				prepareTransferData.setIbtTransType(mPaymentDrTransType);
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL(fromproduct.getProductGL());
				prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
				prepareTransferData.setPrevBalance(l_FromAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(l_FromAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData[i].getTransdescription());
				transferlist.add(prepareTransferData);

				prepareTransferData = new TransferData();
				// Cr IBT GL
				prepareTransferData.setUserID(pTransferData[i].getUserID());
				if (pTransferData[i].getFromCCY().equalsIgnoreCase(pTransferData[i].getFromCCY()))
					prepareTransferData.setAmount(pTransferData[i].getAmount() * (drCCYRate * 1000) / 1000);
				else
					prepareTransferData.setAmount(
							(pTransferData[i].getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(pTransferData[i].getToAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData[i].getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData[i].getToCCY());
				prepareTransferData.setCurrencyCode(pTransferData[i].getToCCY());
				prepareTransferData.setpTransactionTime(pTransferData[i].getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData[i].getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData[i].getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData[i].getReferenceNo());
				prepareTransferData.setRemark(pTransferData[i].getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * drCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData[i].getFromAccNumber());
				prepareTransferData.setIbtTransType(mPaymentCrTransType);
				prepareTransferData.setCashInHandGL(isGL ? fromproduct.getCashInHandGL() : toproduct.getCashInHandGL());
				prepareTransferData.setProductGL(isGL ? "" : toproduct.getProductGL());
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(isGL ? 0 : l_ToAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(isGL ? "19000101" : l_ToAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData[i].getTransdescription());
				transferlist.add(prepareTransferData);

			}
			if (!l_DataResult.getStatus()) {
				l_DataResult.setCode("0014");
				break;
			}
		}

		if (!l_DataResult.getStatus()) {
			transferDataResult.setStatus(false);
			transferDataResult.setCode("0014");
			transferDataResult.setDescription(l_DataResult.getDescription());
		} else {
			transferDataResult.setAutoLinkTransRef(m_AutoLinkTransRef);
			transferDataResult.setTransferlist(transferlist);
			transferDataResult.setStatus(true);
		}

		return transferDataResult;
	}

	public gopaymentresponse domerchantpayment(TransferData[] pTransferData, Connection l_Conn) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tommorrowdate = "";
		m_AutoPrintTransRef = 0;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountTransactionDAO accTransDao = new AccountTransactionDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tommorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tommorrowdate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tommorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tommorrowdate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}
		pTransferData[0].setTransactionDate(l_TodayDate);
		pTransferData[0].setTransactionTime(l_TransactionTime);
		pTransferData[0].setEffectiveDate(l_EffectiveDate);
		// ================ prepare Account Transaction ========================
		TransferDataResult transferDataResult = new TransferDataResult();

		transferDataResult = prepareMerchantPaymentTransaction(pTransferData, l_TableName, l_TableName1, l_Conn);

		try {
			// End Preparation
			// Check Transaction and post link
			// Check Transaction Status
			if (transferDataResult.isStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				transferDataResult = lAccDAO.updateAccountBalanceMultiUpdate(transferDataResult, l_TableName,
						l_TableName1, l_Conn);
				if (transferDataResult.isStatus()) {
					l_DataResult = saveAccountTransaction(transferDataResult.getTransferlist(), l_Conn);
				}

				if (l_DataResult.getStatus()) {
					res.setRetcode("300");
					res.setTransdate(GeneralUtil.getDate());
					res.setBankrefnumber(String.valueOf(l_DataResult.getTransRef()));
					res.setRetmessage("Merchant Payment Successfully");
					res.setEffectivedate(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(l_EffectiveDate));
					// Update Auto link Transfef
					if (transferDataResult.getAutoLinkTransRef().size() > 0) {
						accTransDao.addAutoLinkTransRefUpdate(transferDataResult.getAutoLinkTransRef(),
								l_DataResult.getTransRef(), l_Conn);
					}
				} else {
					res.setRetcode("210");
					res.setRetmessage(l_DataResult.getDescription());
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(transferDataResult.getDescription());
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}

		return res;
	}

	public TransferDataResult prepareMerchantPaymentTransaction(TransferData[] pTransferData, String l_TableName,
			String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		TransferDataResult transferDataResult = new TransferDataResult();
		TransferData prepareTransferData = new TransferData();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "", pByForceCheque = "", l_Query = "", desc = "Bill Payment, ",ibtdesc = "IBT, Bill Payment, ";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		boolean isGL = true, isautolink = false;
		;
		PreparedStatement pstmt = null;

		int lUncommTransNo = DBTransactionMgr.nextValue();
		HashMap<String, AccountData> l_FromAccountList = new HashMap<String, AccountData>();
		HashMap<String, AccountData> l_ToAccountList = new HashMap<String, AccountData>();
		l_FromAccountList.put("", null);
		l_ToAccountList.put("", null);
		ArrayList<Integer> m_AutoLinkTransRef = new ArrayList<Integer>();
		ArrayList<TransferData> transferlist = new ArrayList<TransferData>();

		for (int i = 0; i < pTransferData.length; i++) {
			isGL = true;
			pTransferData[i].setTransactionDate(pTransferData[0].getTransactionDate());
			pTransferData[i].setTransactionTime(pTransferData[0].getTransactionTime());
			pTransferData[i].setEffectiveDate(pTransferData[0].getEffectiveDate());

			if (!l_FromAccountList.containsKey(pTransferData[i].getFromAccNumber())) {
				l_DataResult = l_AccDAO.getTransAccountDebit(pTransferData[i].getFromAccNumber(), l_Conn);
			} else {
				l_DataResult.setStatus(true);
				l_AccDAO.setAccountsBean(l_FromAccountList.get(pTransferData[i].getFromAccNumber()));
			}

			if (l_DataResult.getStatus()) {
				l_FromAccountData = l_AccDAO.getAccountsBean();
				pTransferData[i].setToCCY(l_FromAccountData.getCurrencyCode());
				l_FromAccountList.put(pTransferData[i].getFromAccNumber(), l_FromAccountData);
			}

			if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData[i].getToAccNumber(), l_Conn)) {
				isGL = false;
				l_DataResult.setStatus(false);
				if (!l_ToAccountList.containsKey(pTransferData[i].getToAccNumber())) {
					l_DataResult = l_AccDAO.getTransAccountCredit(pTransferData[i].getToAccNumber(), l_Conn);
				} else {
					l_DataResult.setStatus(true);
					l_AccDAO.setAccountsBean(l_ToAccountList.get(pTransferData[i].getToAccNumber()));
				}
				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					pTransferData[i].setToCCY(l_ToAccountData.getCurrencyCode());
					l_ToAccountList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
					if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
						tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
					} else {
						tkey = l_ToAccountData.getProduct();
					}
					toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
				} else {
					l_DataResult.setStatus(false);
					l_DataResult.setCode("210");
					l_DataResult.setDescription("Invalid Account Number!");
				}
			}

			if (l_DataResult.getStatus()) {
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				} else {
					fkey = l_FromAccountData.getProduct();
				}
				double f_MultipleOf = 0;
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
				f_MultipleOf = fromproduct.getMultipleOf();
				// For Checking Transaction
				// Check Multiple Of

				// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
				// < 999) { // For Debit // Transfer Case
				if ((int) (pTransferData[i].getAmount() % f_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType > 500 && transType != 969) {
					double l_MinWithdrawal = fromproduct.getMinWithdrawal();
					if (pTransferData[i].getAmount() < l_MinWithdrawal) {
						l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
						l_DataResult.setStatus(false);
					}
				}
			}

			// check Debit Available Balance
			if (l_DataResult.getStatus()) {
				double l_Available = 0;
				l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinBalance(), 0,
						l_Conn);
				// Check For A/C Status Closed Pending
				if (l_FromAccountData.getStatus() != 2) {
					if (pTransferData[i].getAmount() > l_Available) {
						l_DataResult.setStatus(false);
						l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_Available)));
					}
				} else {
					double l_CurrentBal = l_FromAccountData.getCurrentBalance();
					if (pTransferData[i].getAmount() != l_CurrentBal) {
						l_DataResult.setStatus(false);
						l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
								+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_CurrentBal)));
					}
				}
				if (!l_DataResult.getStatus()) {
					l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrencyUpdate(pTransferData[i],
							l_FromAccountData, l_Available, pTransferData[i].getAmount(), l_TableName, l_TableName1,
							l_Conn);
					// l_DataResult =
					// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
					// l_FromAccountData, l_TableName, l_TableName1, l_Available,
					// pTransferData.getAmount(), l_Conn);
					if (l_DataResult.getStatus()) {// Add Auto Link Transref
						m_AutoLinkTransRef.add(l_DataResult.getTransactionNumber());
						if (l_DataResult.getTransRef() != 0)
							m_AutoLinkTransRef.add(l_DataResult.getTransRef());

					}

				}
			}

			if (l_DataResult.getStatus()) {
				String fkey1 = "";

				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
				pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
				pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setpByForceCheque(pByForceCheque);
				fkey1 = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_" + pTransferData[i].getFromBranchCode();
				drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);

			}

			if (l_DataResult.getStatus()) {
				int index = 1;
				// Dr From Customer
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData[i].getUserID());
				prepareTransferData.setAmount(pTransferData[i].getAmount());
				prepareTransferData.setFromAccNumber(pTransferData[i].getFromAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData[i].getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData[i].getFromCCY());
				prepareTransferData.setCurrencyCode(pTransferData[i].getFromCCY());
				prepareTransferData.setpTransactionTime(pTransferData[i].getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData[i].getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData[i].getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData[i].getReferenceNo());
				prepareTransferData.setRemark(pTransferData[i].getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * drCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData[i].getToAccNumber());
				prepareTransferData.setIbtTransType(mPaymentDrTransType);
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL(fromproduct.getProductGL());
				prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
				prepareTransferData.setPrevBalance(l_FromAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(l_FromAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData[i].getTransdescription());
				transferlist.add(prepareTransferData);

				// Cr To AccNumber
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData[i].getUserID());
				if (pTransferData[i].getFromCCY().equalsIgnoreCase(pTransferData[i].getFromCCY()))
					prepareTransferData.setAmount(pTransferData[i].getAmount() * (drCCYRate * 1000) / 1000);
				else
					prepareTransferData.setAmount(
							(pTransferData[i].getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(pTransferData[i].getToAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData[i].getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData[i].getToCCY());
				prepareTransferData.setCurrencyCode(pTransferData[i].getToCCY());
				prepareTransferData.setpTransactionTime(pTransferData[i].getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData[i].getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData[i].getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData[i].getReferenceNo());
				prepareTransferData.setRemark(pTransferData[i].getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData[i].getAmount() * drCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData[i].getFromAccNumber());
				prepareTransferData.setIbtTransType(mPaymentCrTransType);
				prepareTransferData.setCashInHandGL(isGL ? fromproduct.getCashInHandGL() : toproduct.getCashInHandGL());
				prepareTransferData.setProductGL(isGL ? "" : toproduct.getProductGL());
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(isGL ? 0 : l_ToAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(isGL ? "19000101" : l_ToAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData[i].getTransdescription());
				prepareTransferData.setIsGL(isGL);
				transferlist.add(prepareTransferData);

			}
			if (!l_DataResult.getStatus()) {
				l_DataResult.setCode("0014");
				break;
			}
		}

		if (!l_DataResult.getStatus()) {
			transferDataResult.setStatus(false);
			transferDataResult.setCode("0014");
			transferDataResult.setDescription(l_DataResult.getDescription());
		} else {
			transferDataResult.setAutoLinkTransRef(m_AutoLinkTransRef);
			transferDataResult.setTransferlist(transferlist);
			transferDataResult.setStatus(true);
		}

		return transferDataResult;
	}

	public gopaymentresponse dobulkpayment(TransferData[] pTransferData, String payType, double totAmount,
			Connection l_Conn) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tomorrowDate = "";
		m_AutoPrintTransRef = 0;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}
		pTransferData[0].setTransactionDate(l_TodayDate);
		pTransferData[0].setTransactionTime(l_TransactionTime);
		pTransferData[0].setEffectiveDate(l_EffectiveDate);
		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_transprepare" + pTransferData[0].getFromAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();
		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareBulkPaymentTransaction(pTransferData, tmp_Transpreapre, payType, totAmount, l_TableName,
				l_TableName1, l_Conn);

		try {
			// End Preparation
			// Check Transaction and post link
			// Check Transaction Status
			if (l_DataResult.getStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				int autoLinkTransref = (int) l_DataResult.getAutoLinkTransRef();
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				l_DataResult = lAccDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
						l_Conn);
				if (l_DataResult.getStatus()) {
					l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
				}

				if (l_DataResult.getStatus()) {
					res.setRetcode("300");
					res.setTransdate(GeneralUtil.getDate());
					res.setBankrefnumber(String.valueOf(l_DataResult.getTransRef()));
					res.setRetmessage("Bulk Payment Successfully");
					res.setEffectivedate(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(l_EffectiveDate));
					// Update Auto link Transfef ynw test
//							if(autoLinkTransref>0)
//								new AccountTransactionDAO().updateAutoLinkTransRef(autoLinkTransref,l_DataResult.getTransRef(),l_Conn);
				} else {
					res.setRetcode("210");
					res.setRetmessage(l_DataResult.getDescription());
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(l_DataResult.getDescription());
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}

		return res;
	}

	public DataResult prepareBulkPaymentTransaction(TransferData[] pTransferData, String tmp_Transpreapre,
			String payType, double totAmount, String l_TableName, String l_TableName1, Connection l_Conn)
			throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "", pByForceCheque = "", l_Query = "", desc = "Payment Transfer, ",ibtdesc = "IBT, Payment Transfer, ", drcrtype1 = "",
				drcrtype2 = "";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		PreparedStatement pstmt = null;
		boolean isautolink = false;
		if (payType.equals("10")) {
			drcrtype1 = "Dr";
			drcrtype2 = "Cr";
		} else if (payType.equals("20")) {
			drcrtype1 = "Cr";
			drcrtype2 = "Dr";
		}
		int lUncommTransNo = DBTransactionMgr.nextValue();
		HashMap<String, AccountData> l_FromAccountList = new HashMap<String, AccountData>();
		l_FromAccountList.put("", null);

		for (int i = 0; i < pTransferData.length; i++) {
			pTransferData[i].setTransactionDate(pTransferData[0].getTransactionDate());
			pTransferData[i].setTransactionTime(pTransferData[0].getTransactionTime());
			pTransferData[i].setEffectiveDate(pTransferData[0].getEffectiveDate());
			if (i == 0) {
				l_DataResult = l_AccDAO.getTransAccount(pTransferData[0].getFromAccNumber(), drcrtype1, totAmount,
						l_Conn);
				if (l_DataResult.getStatus()) {
					l_FromAccountData = l_AccDAO.getAccountsBean();
				}
			}

			if (l_DataResult.getStatus()) {
				if (payType.equals("10")) {
					l_DataResult = l_AccDAO.getTransAccount(pTransferData[i].getToAccNumber(), drcrtype2,
							pTransferData[i].getAmount(), l_Conn);

				} else if (payType.equals("20")) {
					if (!l_FromAccountList.containsKey(pTransferData[i].getToAccNumber())) {
						l_DataResult = l_AccDAO.getTransAccount(pTransferData[i].getToAccNumber(), drcrtype2,
								pTransferData[i].getAmount(), l_Conn);
					} else {
						l_DataResult.setStatus(true);
						l_AccDAO.setAccountsBean(l_FromAccountList.get(pTransferData[i].getToAccNumber()));
					}
				}
				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					l_FromAccountList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
				}

			}

			if (l_DataResult.getStatus()) {
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					if (i == 0) {
						fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
					}
					tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
				} else {
					if (i == 0) {
						fkey = l_FromAccountData.getProduct();
					}
					tkey = l_ToAccountData.getProduct();
				}
				if (i == 0) {
					fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
					double f_MultipleOf = fromproduct.getMultipleOf();
					// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
					// < 999) { // For Debit // Transfer Case
					if ((int) (pTransferData[i].getAmount() % f_MultipleOf) != 0) {
						l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
						l_DataResult.setStatus(false);
					} else {
						if (payType.equals("10")) { // check Dr acc
							// if (transType > 500 && transType != 969) {
							double l_MinWithdrawal = fromproduct.getMinWithdrawal();
							if (pTransferData[i].getAmount() < l_MinWithdrawal) {
								l_DataResult.setDescription(
										"Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
								l_DataResult.setStatus(false);
							}
						} else if (payType.equals("20")) { // check Cr acc
							// if (transType < 500) {
							double l_MinOpeningBalance = fromproduct.getMinOpeningBalance();
							if (l_FromAccountData.getStatus() == 0 && SharedUtil
									.formatMIT2DateStr(l_FromAccountData.getLastTransDate()).equals("01/01/1900")) {
								if (pTransferData[i].getAmount() < l_MinOpeningBalance) {
									l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
									l_DataResult.setStatus(false);
								}
							}
						}

					}
				}

				// For Checking Transaction
				// Check Multiple Of

				double t_MultipleOf = toproduct.getMultipleOf();
				toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
				if (l_DataResult.getStatus()) {
					// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
					// <= 500) { // For Credit // Transfer Case
					if ((int) (pTransferData[i].getAmount() % t_MultipleOf) != 0) {
						l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
						l_DataResult.setStatus(false);
					} else {
						if (payType.equals("10")) { // check Cr acc
							// if (transType < 500) {
							double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
							if (l_ToAccountData.getStatus() == 0 && SharedUtil
									.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
								if (pTransferData[i].getAmount() < l_MinOpeningBalance) {
									l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
									l_DataResult.setStatus(false);
								}
							}
						} else if (payType.equals("20")) { // check Dr acc
							// if (transType > 500 && transType != 969) {
							double l_MinWithdrawal = toproduct.getMinWithdrawal();
							if (pTransferData[i].getAmount() < l_MinWithdrawal) {
								l_DataResult.setDescription(
										"Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
								l_DataResult.setStatus(false);
							}
						}

					}
				}

			}

			// check Debit Available Balance
			if (l_DataResult.getStatus()) {
				double l_Available = 0;
				if (payType.equals("10")) {
					if (i == 0) {
						l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData,
								fromproduct.getMinBalance(), 0, l_Conn);
						// Check For A/C Status Closed Pending
						if (l_FromAccountData.getStatus() != 2) {
							if (pTransferData[i].getAmount() > l_Available) {
								l_DataResult.setStatus(false);
								l_DataResult
										.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
												+ StrUtil.formatNumberwithComma((totAmount - l_Available)));
							}
						} else {
							double l_CurrentBal = l_FromAccountData.getCurrentBalance();
							if (pTransferData[i].getAmount() != l_CurrentBal) {
								l_DataResult.setStatus(false);
								l_DataResult
										.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
												+ StrUtil.formatNumberwithComma((totAmount - l_CurrentBal)));
							}
						}
						if (!l_DataResult.getStatus()) {
							l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrency(pTransferData[i],
									l_FromAccountData, l_TableName, l_TableName1, l_Available, totAmount, l_Conn);
							// l_DataResult =
							// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
							// l_FromAccountData, l_TableName, l_TableName1, l_Available,
							// pTransferData.getAmount(), l_Conn);
							if (l_DataResult.getStatus()) {// Add Auto Link Transref
								AccountTransactionDAO lAccTranDAO = new AccountTransactionDAO();
								if (l_DataResult.getAutoLinkTransRef() > 0)
									lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
											(int) l_DataResult.getAutoLinkTransRef(), l_Conn);
								if (l_DataResult.getSecAutoLinkTransRef() > 0)
									lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
											(int) l_DataResult.getSecAutoLinkTransRef(), l_Conn);
								isautolink = true;
							}

						}
					}
				} else if (payType.equals("20")) {
					l_Available = l_AccDAO.getAvaliableBalance(l_ToAccountData, toproduct.getMinBalance(), 0,
							l_Conn);
					// Check For A/C Status Closed Pending
					if (l_ToAccountData.getStatus() != 2) {
						if (pTransferData[i].getAmount() > l_Available) {
							l_DataResult.setStatus(false);
							l_DataResult.setDescription(l_ToAccountData.getAccountNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_Available)));
						}
					} else {
						double l_CurrentBal = l_ToAccountData.getCurrentBalance();
						if (pTransferData[i].getAmount() != l_CurrentBal) {
							l_DataResult.setStatus(false);
							l_DataResult.setDescription(l_ToAccountData.getAccountNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_CurrentBal)));
						}
					}
					if (!l_DataResult.getStatus()) {
						l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrency(pTransferData[i],
								l_ToAccountData, l_TableName, l_TableName1, l_Available, pTransferData[i].getAmount(),
								l_Conn);
						// l_DataResult =
						// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
						// l_FromAccountData, l_TableName, l_TableName1, l_Available,
						// pTransferData.getAmount(), l_Conn);
						if (l_DataResult.getStatus()) {// Add Auto Link Transref
							AccountTransactionDAO lAccTranDAO = new AccountTransactionDAO();
							if (l_DataResult.getAutoLinkTransRef() > 0)
								lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
										(int) l_DataResult.getAutoLinkTransRef(), l_Conn);
							if (l_DataResult.getSecAutoLinkTransRef() > 0)
								lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
										(int) l_DataResult.getSecAutoLinkTransRef(), l_Conn);
							isautolink = true;
						}

					}
				}

			}

			if (l_DataResult.getStatus()) {
				pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
				pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
				if (i == 0) {
					String fkey1 = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_"
							+ pTransferData[i].getFromBranchCode();
					drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
				}
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
				pTransferData[i].setToBranchCode(l_ToAccountData.getBranchCode());
				pTransferData[i].setToCCY(l_ToAccountData.getCurrencyCode());
				pTransferData[i].setpByForceCheque(pByForceCheque);

				String tkey1 = "smTransfer" + "_" + pTransferData[i].getToCCY() + "_"
						+ pTransferData[i].getToBranchCode();
				crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
			}
			if (l_DataResult.getStatus()) {
				int index = 1;
				if (payType.equals("10")) {
					if (i == 0) {
						// Dr From Customer
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ "'19000101',1,?,?,?,?,?,?,?,?,?,?,? ) ";
						pstmt = l_Conn.prepareStatement(l_Query);
						pstmt.setString(index++, pTransferData[i].getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						pstmt.setDouble(index++, totAmount);
						pstmt.setString(index++, pTransferData[i].getFromAccNumber());
						pstmt.setString(index++, pTransferData[i].getFromBranchCode());
						pstmt.setString(index++, pTransferData[i].getFromCCY());
						pstmt.setString(index++, pTransferData[i].getTransactionTime());
						pstmt.setString(index++, pTransferData[i].getTransactionDate());
						pstmt.setString(index++, pTransferData[i].getEffectiveDate());
						pstmt.setString(index++, pTransferData[i].getReferenceNo());
						pstmt.setString(index++, "");
						pstmt.setDouble(index++, drCCYRate);
						pstmt.setDouble(index++, StrUtil.round2decimals(totAmount * drCCYRate));
						pstmt.setString(index++, desc + pTransferData[i].getToAccNumber());
						pstmt.setInt(index++, mPaymentDrTransType);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, fromproduct.getProductGL());
						pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
						pstmt.setString(index++, pTransferData[i].getTransdescription());
						pstmt.setString(index++, l_FromAccountData.getLastUpdate());
						pstmt.executeUpdate();
					}

					// Cr To Account
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData[i].getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData[i].getFromCCY().equalsIgnoreCase(pTransferData[i].getToCCY()))
						pstmt.setDouble(index++, pTransferData[i].getAmount() * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++,
								(pTransferData[i].getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, pTransferData[i].getToAccNumber());
					pstmt.setString(index++, pTransferData[i].getToBranchCode());
					pstmt.setString(index++, pTransferData[i].getToCCY());
					pstmt.setString(index++, pTransferData[i].getTransactionTime());
					pstmt.setString(index++, pTransferData[i].getTransactionDate());
					pstmt.setString(index++, pTransferData[i].getEffectiveDate());
					pstmt.setString(index++, pTransferData[i].getReferenceNo());
					pstmt.setString(index++, pTransferData[i].getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData[i].getAmount() * crCCYRate));
					pstmt.setString(index++, desc + pTransferData[i].getFromAccNumber());
					pstmt.setInt(index++, mPaymentCrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, toproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData[i].getTransdescription());
					pstmt.setString(index++, l_ToAccountData.getLastUpdate());
					pstmt.executeUpdate();

				} else if (payType.equals("20")) {
					// Dr From Customer
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,?,? ) ";
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData[i].getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, pTransferData[i].getAmount());
					pstmt.setString(index++, pTransferData[i].getToAccNumber());
					pstmt.setString(index++, pTransferData[i].getToBranchCode());
					pstmt.setString(index++, pTransferData[i].getToCCY());
					pstmt.setString(index++, pTransferData[i].getTransactionTime());
					pstmt.setString(index++, pTransferData[i].getTransactionDate());
					pstmt.setString(index++, pTransferData[i].getEffectiveDate());
					pstmt.setString(index++, pTransferData[i].getReferenceNo());
					pstmt.setString(index++, pTransferData[i].getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData[i].getAmount() * crCCYRate));
					pstmt.setString(index++,
							desc + pTransferData[i].getFromAccNumber() + ", " + pTransferData[i].getRemark());
					pstmt.setInt(index++, mPaymentDrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, toproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData[i].getTransdescription());
					pstmt.setString(index++, "19000101");
					pstmt.executeUpdate();

					if (i == pTransferData.length - 1) {
						// Cr To Account
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
						pstmt = l_Conn.prepareStatement(l_Query);
						index = 1;
						pstmt.setString(index++, pTransferData[i].getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						pstmt.setDouble(index++, totAmount);
						pstmt.setString(index++, pTransferData[i].getFromAccNumber());
						pstmt.setString(index++, pTransferData[i].getFromBranchCode());
						pstmt.setString(index++, pTransferData[i].getFromCCY());
						pstmt.setString(index++, pTransferData[i].getTransactionTime());
						pstmt.setString(index++, pTransferData[i].getTransactionDate());
						pstmt.setString(index++, pTransferData[i].getEffectiveDate());
						pstmt.setString(index++, pTransferData[i].getReferenceNo());
						pstmt.setString(index++, "");
						pstmt.setDouble(index++, crCCYRate);
						pstmt.setDouble(index++, StrUtil.round2decimals(totAmount * drCCYRate));
						pstmt.setString(index++, desc + pTransferData[i].getToAccNumber());
						pstmt.setInt(index++, mPaymentCrTransType);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, fromproduct.getProductGL());
						pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
						pstmt.setString(index++, pTransferData[i].getTransdescription());
						pstmt.setString(index++, l_FromAccountData.getLastUpdate());
						pstmt.executeUpdate();
					}
				}
			}
			if (!l_DataResult.getStatus()) {
				l_DataResult.setCode("0014");
				break;
			}
		}
		if (isautolink)
			l_DataResult.setAutoLinkTransRef(lUncommTransNo);
		return l_DataResult;
	}

	public SMSReturnData doOtherBankTransfernew(TransferData pTransferData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tomorrowDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		// // by
		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_transprepare" + pTransferData.getFromAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();

		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);
		l_DataResult = prepareOTBTransaction(pTransferData, "Tmp_accprepare", tmp_Transpreapre, l_TableName,
				l_TableName1, l_Conn);

		try {

			if (l_DataResult.getStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				int autoLinkTransref = (int) l_DataResult.getAutoLinkTransRef();
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				l_DataResult = lAccDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
						l_Conn);
				if (l_DataResult.getStatus()) {
					l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
				}
				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef ynw test
//							if(autoLinkTransref>0)
//								new AccountTransactionDAO().updateAutoLinkTransRef(autoLinkTransref,l_DataResult.getTransRef(),l_Conn);
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}
			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}

	public DataResult prepareOTBTransaction(TransferData pTransferData, String Tmp_accprepare, String tmp_Transpreapre,
			String l_TableName, String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "";
		String pByForceCheque = "", transdescription = "Other Bank Transfer, ", ibttransdescription = "IBT, Other Bank Transfer, ";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		boolean isGL = false;
		boolean isSameBranch = false;
		int lUncommTransNo = DBTransactionMgr.nextValue();
//			CCTDrawingData lData = new CCTDrawingData();

		l_DataResult = l_AccDAO.getTransAccount(pTransferData.getFromAccNumber(), "Dr", pTransferData.getAmount(),
				l_Conn);
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
		}

		if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData.getToAccNumber(), l_Conn)) {
			l_DataResult = l_AccDAO.getTransAccount(pTransferData.getToAccNumber(), "Cr", pTransferData.getAmount(),
					l_Conn);
			if (l_DataResult.getStatus()) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
			}
		} else {
			isGL = true;
			isSameBranch = true;
			pTransferData.setToCCY(l_FromAccountData.getCurrencyCode());
		}

		if (l_DataResult.getStatus()) {
			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				if (!isGL)
					tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				fkey = l_FromAccountData.getProduct();
				if (!isGL)
					tkey = l_ToAccountData.getProduct();
			}
			double f_MultipleOf = 0, t_MultipleOf = 0;
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			f_MultipleOf = fromproduct.getMultipleOf();
			if (!isGL) {
				toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
				t_MultipleOf = toproduct.getMultipleOf();
			}

			// For Checking Transaction
			// Check Multiple Of

			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if ((int) (pTransferData.getAmount() % f_MultipleOf) != 0) {
				l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
				l_DataResult.setStatus(false);
			} else {
				// if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = fromproduct.getMinWithdrawal();
				if (pTransferData.getAmount() < l_MinWithdrawal) {
					l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
					l_DataResult.setStatus(false);
				}
			}
			if (l_DataResult.getStatus() && !isGL) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus()) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinBalance(), 0,
					l_Conn);
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (pTransferData.getAmount() > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (pTransferData.getAmount() != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}
			if (!l_DataResult.getStatus()) {
				l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrency(pTransferData, l_FromAccountData,
						l_TableName, l_TableName1, l_Available, pTransferData.getAmount(), l_Conn);
				// l_DataResult =
				// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
				// l_FromAccountData, l_TableName, l_TableName1, l_Available,
				// pTransferData.getAmount(), l_Conn);
				if (l_DataResult.getStatus()) {// Add Auto Link Transref
					AccountTransactionDAO lAccTranDAO = new AccountTransactionDAO();
					if (l_DataResult.getAutoLinkTransRef() > 0)
						lAccTranDAO.addAutoLinkTransRef(lUncommTransNo, (int) l_DataResult.getAutoLinkTransRef(),
								l_Conn);
					if (l_DataResult.getSecAutoLinkTransRef() > 0)
						lAccTranDAO.addAutoLinkTransRef(lUncommTransNo, (int) l_DataResult.getSecAutoLinkTransRef(),
								l_Conn);
					l_DataResult.setAutoLinkTransRef(lUncommTransNo);
				}

			}
		}
		if (l_DataResult.getStatus()) {
			String fkey1 = "", tkey1 = "";

			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
			pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
			pTransferData.setToBranchCode(l_FromAccountData.getBranchCode());
			fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
			drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			if (!isGL) {
				pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
				pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());
				tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();
				crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);

				if (l_FromAccountData.getBranchCode().equalsIgnoreCase(l_ToAccountData.getBranchCode())) {
					isSameBranch = true;
				}

			}
		}
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		PreparedStatement pstmt = null;
		String l_Query = "";
		String l_Com1GL = "", l_Com2GL = "";

		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
				.get("CCT3COM1" + pTransferData.getCurrencyCode() + pTransferData.getToBankCode() + pTransferData.getFromBranchCode());
		if (l_RefData != null)
			l_Com1GL = l_RefData.getGLCode();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("CCT3COM2"
				+ pTransferData.getCurrencyCode() + pTransferData.getToBankCode() + pTransferData.getFromBranchCode());
		if (l_RefData != null)
			l_Com2GL = l_RefData.getGLCode();

		// System.out.println(l_Com1GL+"=>COMM1: CCTCOM1" +
		// pTransferData.getCurrencyCode() + 1 + pTransferData.getFromBranchCode());
		// System.out.println(l_Com2GL+"=>COMM2: CCTCOM2" +
		// pTransferData.getCurrencyCode() + pTransferData.getToBankCode() +
		// pTransferData.getToBranchCode());
		if (l_DataResult.getStatus()) {
			if ((l_Com1GL.equals("") && pTransferData.getComm1() > 0)
					|| (l_Com2GL.equals("") && pTransferData.getComm2() > 0)) {
				l_DataResult.setStatus(false);
				l_DataResult.setCode("0014");
				l_DataResult.setDescription("Invalid GL Account!");
				return l_DataResult;
			}
		}

		if (l_DataResult.getStatus()) {
			double totalfee = 0, amount = 0, comm1Amt = 0, comm2Amt = 0;
			amount = pTransferData.getAmount();
			comm1Amt = pTransferData.getComm1();
			comm2Amt = pTransferData.getComm2();
			if (pTransferData.isInclusive())
				amount = amount - (comm1Amt + comm2Amt);
			totalfee = amount + comm1Amt + comm2Amt;
			
			if (isSameBranch) {
				int index = 1;
				// Dr From Customer
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ "'19000101',1,?,?,?,?,?,?,?,?,?,?,? ) ";
				pstmt = l_Conn.prepareStatement(l_Query);
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				pstmt.setDouble(index++, totalfee);
				pstmt.setString(index++, pTransferData.getFromAccNumber());
				pstmt.setString(index++, pTransferData.getFromBranchCode());
				pstmt.setString(index++, pTransferData.getFromCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, drCCYRate);
				pstmt.setDouble(index++, StrUtil.round2decimals(totalfee * drCCYRate));
				pstmt.setString(index++, transdescription + pTransferData.getToAccNumber());
				pstmt.setInt(index++, mOBTDrTransType);
				pstmt.setString(index++, fromproduct.getCashInHandGL());
				pstmt.setString(index++, fromproduct.getProductGL());
				pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.setString(index++, l_FromAccountData.getLastUpdate());
				pstmt.executeUpdate();

				// Cr IBT GL
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
				pstmt = l_Conn.prepareStatement(l_Query);
				index = 1;
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					pstmt.setDouble(index++, amount * (drCCYRate * 1000) / 1000);
				else
					pstmt.setDouble(index++, (amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
				pstmt.setString(index++, pTransferData.getToAccNumber());
				pstmt.setString(index++, pTransferData.getToBranchCode());
				pstmt.setString(index++, pTransferData.getToCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, drCCYRate);
				pstmt.setDouble(index++, amount * drCCYRate);
				pstmt.setString(index++, transdescription + pTransferData.getFromAccNumber());
				pstmt.setInt(index++, mOBTCrTransType);
				pstmt.setString(index++, isGL ? fromproduct.getCashInHandGL() : toproduct.getCashInHandGL());
				pstmt.setString(index++, isGL ? "" : toproduct.getProductGL());
				pstmt.setString(index++, "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, isGL ? 0 : l_ToAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.setString(index++, isGL ? "19000101" : l_ToAccountData.getLastUpdate());
				pstmt.executeUpdate();

				if (comm1Amt > 0) {
					// Cr Com GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, comm1Amt * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (comm1Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, l_Com1GL);
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, comm1Amt * drCCYRate);
					pstmt.setString(index++, transdescription + pTransferData.getFromAccNumber());
					pstmt.setInt(index++, mOBTCrTransType);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, "");
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.setString(index++, "19000101");
					pstmt.executeUpdate();
				}

				if (comm2Amt > 0) {
					// Cr Com G
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, comm2Amt * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (comm2Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, l_Com2GL);
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, comm2Amt * drCCYRate);
					pstmt.setString(index++, transdescription + pTransferData.getFromAccNumber());
					pstmt.setInt(index++, mOBTCrTransType);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, "");
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.setString(index++, "19000101");
					pstmt.executeUpdate();
				}

			} else {
				// different branch
				String l_IBTGLFrom = "";
				String l_IBTGLTo = "";

				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
						.get("MTRCR" + pTransferData.getFromCCY() + pTransferData.getFromBranchCode()
								+ fromproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLFrom = l_RefData.getGLCode();
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRDR"
						+ pTransferData.getToCCY() + pTransferData.getToBranchCode() + toproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLTo = l_RefData.getGLCode();

				if (!(l_IBTGLFrom.equals("") || l_IBTGLTo.equals(""))) {
					// Dr From Customer
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,?,? ) ";
					int index = 1;
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, totalfee);
					pstmt.setString(index++, pTransferData.getFromAccNumber());
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, totalfee * drCCYRate);
					pstmt.setString(index++, transdescription + pTransferData.getToAccNumber());
					pstmt.setInt(index++, mOBTDrTransType);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, fromproduct.getProductGL());
					pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.setString(index++, l_FromAccountData.getLastUpdate());
					pstmt.executeUpdate();

					// Cr IBT GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, amount * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_IBTGLFrom);
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(amount * drCCYRate));
					pstmt.setString(index++, ibttransdescription + pTransferData.getFromAccNumber());
					pstmt.setInt(index++, mOBTCrTransType);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, "");
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.setString(index++, "19000101");
					pstmt.executeUpdate();

					// Dr IBT GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,?,? ) ";
					index = 1;
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, amount * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, l_IBTGLTo);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(amount * drCCYRate));
					pstmt.setString(index++, ibttransdescription + pTransferData.getFromAccNumber());
					pstmt.setInt(index++, mOBTDrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, "");
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.setString(index++, "19000101");
					pstmt.executeUpdate();

					// Cr To Account
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, amount * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (amount * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, pTransferData.getToAccNumber());
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(amount * crCCYRate));
					pstmt.setString(index++, transdescription + pTransferData.getFromAccNumber());
					pstmt.setInt(index++, mOBTCrTransType);
					pstmt.setString(index++,  isGL ? fromproduct.getCashInHandGL() : toproduct.getCashInHandGL());
					pstmt.setString(index++,  isGL ? "" : toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, isGL ? 0 :l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.setString(index++, l_ToAccountData.getLastUpdate());
					pstmt.executeUpdate();

					if (comm1Amt > 0) {
						// Cr Com GL
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
						pstmt = l_Conn.prepareStatement(l_Query);
						index = 1;
						pstmt.setString(index++, pTransferData.getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
							pstmt.setDouble(index++, comm1Amt * (drCCYRate * 1000) / 1000);
						else
							pstmt.setDouble(index++, (comm1Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
						pstmt.setString(index++, l_Com1GL);
						pstmt.setString(index++, pTransferData.getFromBranchCode());
						pstmt.setString(index++, pTransferData.getFromCCY());
						pstmt.setString(index++, pTransferData.getTransactionTime());
						pstmt.setString(index++, pTransferData.getTransactionDate());
						pstmt.setString(index++, pTransferData.getEffectiveDate());
						pstmt.setString(index++, pTransferData.getReferenceNo());
						pstmt.setString(index++, pTransferData.getRemark());
						pstmt.setDouble(index++, drCCYRate);
						pstmt.setDouble(index++, comm1Amt * drCCYRate);
						pstmt.setString(index++, transdescription + pTransferData.getFromAccNumber());
						pstmt.setInt(index++, mOBTCrTransType);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, "");
						pstmt.setString(index++, "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, 0);
						pstmt.setString(index++, pTransferData.getTransdescription());
						pstmt.setString(index++, "19000101");
						pstmt.executeUpdate();
					}

					if (comm2Amt > 0) {
						// Cr Com G
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ " '19000101',1,?,?,?,?,?,?,?,?,?,?,? )";
						pstmt = l_Conn.prepareStatement(l_Query);
						index = 1;
						pstmt.setString(index++, pTransferData.getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
							pstmt.setDouble(index++, comm2Amt * (drCCYRate * 1000) / 1000);
						else
							pstmt.setDouble(index++, (comm2Amt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
						pstmt.setString(index++, l_Com2GL);
						pstmt.setString(index++, pTransferData.getFromBranchCode());
						pstmt.setString(index++, pTransferData.getFromCCY());
						pstmt.setString(index++, pTransferData.getTransactionTime());
						pstmt.setString(index++, pTransferData.getTransactionDate());
						pstmt.setString(index++, pTransferData.getEffectiveDate());
						pstmt.setString(index++, pTransferData.getReferenceNo());
						pstmt.setString(index++, pTransferData.getRemark());
						pstmt.setDouble(index++, drCCYRate);
						pstmt.setDouble(index++, comm2Amt * drCCYRate);
						pstmt.setString(index++, transdescription + pTransferData.getFromAccNumber());
						pstmt.setInt(index++, mOBTCrTransType);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, "");
						pstmt.setString(index++, "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, 0);
						pstmt.setString(index++, pTransferData.getTransdescription());
						pstmt.setString(index++, "19000101");
						pstmt.executeUpdate();
					}
				} else {
					l_DataResult.setStatus(false);
					l_DataResult.setCode("0014");
					l_DataResult.setDescription("Invalid GL Account!");
				}
			}
//				CCTDrawingDao.addCCTDrawingData(lData, l_Conn);
		}
		if (!l_DataResult.getStatus()) {
			l_DataResult.setCode("0014");
		}

		return l_DataResult;
	}

	public gopaymentresponse domultitransfernew(TransferData[] pTransferData, String payType, double totAmount,
			Connection l_Conn) throws Exception {
		gopaymentresponse res = new gopaymentresponse();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tomorrowDate = "";
		m_AutoPrintTransRef = 0;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}
		pTransferData[0].setTransactionDate(l_TodayDate);
		pTransferData[0].setTransactionTime(l_TransactionTime);
		pTransferData[0].setEffectiveDate(l_EffectiveDate);
		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_transprepare" + pTransferData[0].getFromAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();
		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareMultiTransferTransaction(pTransferData, tmp_Transpreapre, payType, totAmount, l_TableName,
				l_TableName1, l_Conn);

		try {
			// End Preparation
			// Check Transaction and post link
			// Check Transaction Status
			if (l_DataResult.getStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				int autoLinkTransref = (int) l_DataResult.getAutoLinkTransRef();
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				l_DataResult = lAccDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
						l_Conn);
				if (l_DataResult.getStatus()) {
					l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
				}

				if (l_DataResult.getStatus()) {
					res.setRetcode("300");
					String transDate = GeneralUtil.datetoString();
					res.setTransdate(GeneralUtil.mobiledateformat(transDate));
					res.setBankrefnumber(String.valueOf(l_DataResult.getTransRef()));
					res.setRetmessage("Multi Transfer Successfully");
					res.setEffectivedate(l_EffectiveDate);
				} else {
					res.setRetcode("210");
					res.setRetmessage(l_DataResult.getDescription());
				}
			} else {
				res.setRetcode("210");
				res.setRetmessage(l_DataResult.getDescription());
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			res.setRetmessage("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			res.setRetcode("220");
		}

		return res;
	}

	public DataResult prepareMultiTransferTransaction(TransferData[] pTransferData, String tmp_Transpreapre,
			String payType, double totAmount, String l_TableName, String l_TableName1, Connection l_Conn)
			throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "", pByForceCheque = "", l_Query = "", desc = "Multiple Accounts Transfer , ",
				drcrtype1 = "", drcrtype2 = "";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		PreparedStatement pstmt = null;
		boolean isautolink = false;
		if (payType.equals("10")) {
			drcrtype1 = "Dr";
			drcrtype2 = "Cr";
		} else if (payType.equals("20")) {
			drcrtype1 = "Cr";
			drcrtype2 = "Dr";
		}

		int lUncommTransNo = DBTransactionMgr.nextValue();

		HashMap<String, AccountData> l_FromAccountList = new HashMap<String, AccountData>();
		l_FromAccountList.put("", null);

		for (int i = 0; i < pTransferData.length; i++) {
			pTransferData[i].setTransactionDate(pTransferData[0].getTransactionDate());
			pTransferData[i].setTransactionTime(pTransferData[0].getTransactionTime());
			pTransferData[i].setEffectiveDate(pTransferData[0].getEffectiveDate());
			if (i == 0) {
				l_DataResult = l_AccDAO.getTransAccount(pTransferData[0].getFromAccNumber(), drcrtype1, totAmount,
						l_Conn);
				if (l_DataResult.getStatus()) {
					l_FromAccountData = l_AccDAO.getAccountsBean();
				}
			}

			if (l_DataResult.getStatus()) {
				if (payType.equals("10")) {
					l_DataResult = l_AccDAO.getTransAccount(pTransferData[i].getToAccNumber(), drcrtype2,
							pTransferData[i].getAmount(), l_Conn);

				} else if (payType.equals("20")) {
					if (!l_FromAccountList.containsKey(pTransferData[i].getToAccNumber())) {
						l_DataResult = l_AccDAO.getTransAccount(pTransferData[i].getToAccNumber(), drcrtype2,
								pTransferData[i].getAmount(), l_Conn);
					} else {
						l_DataResult.setStatus(true);
						l_AccDAO.setAccountsBean(l_FromAccountList.get(pTransferData[i].getToAccNumber()));
					}
				}

				if (l_DataResult.getStatus()) {
					l_ToAccountData = l_AccDAO.getAccountsBean();
					l_FromAccountList.put(pTransferData[i].getToAccNumber(), l_ToAccountData);
				}

			}

			if (l_DataResult.getStatus()) {
				if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
					if (i == 0) {
						fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
					}
					tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
				} else {
					if (i == 0) {
						fkey = l_FromAccountData.getProduct();
					}
					tkey = l_ToAccountData.getProduct();
				}
				if (i == 0) {
					fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
					double f_MultipleOf = fromproduct.getMultipleOf();
					// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
					// < 999) { // For Debit // Transfer Case
					if ((int) (pTransferData[i].getAmount() % f_MultipleOf) != 0) {
						l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
						l_DataResult.setStatus(false);
					} else {
						if (payType.equals("10")) { // check Dr acc
							// if (transType > 500 && transType != 969) {
							double l_MinWithdrawal = fromproduct.getMinWithdrawal();
							if (pTransferData[i].getAmount() < l_MinWithdrawal) {
								l_DataResult.setDescription(
										"Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
								l_DataResult.setStatus(false);
							}
						} else if (payType.equals("20")) { // check Cr acc
							// if (transType < 500) {
							double l_MinOpeningBalance = fromproduct.getMinOpeningBalance();
							if (l_FromAccountData.getStatus() == 0 && SharedUtil
									.formatMIT2DateStr(l_FromAccountData.getLastTransDate()).equals("01/01/1900")) {
								if (pTransferData[i].getAmount() < l_MinOpeningBalance) {
									l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
									l_DataResult.setStatus(false);
								}
							}
						}

					}
				}

				// For Checking Transaction
				// Check Multiple Of

				double t_MultipleOf = toproduct.getMultipleOf();
				toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
				if (l_DataResult.getStatus()) {
					// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
					// <= 500) { // For Credit // Transfer Case
					if ((int) (pTransferData[i].getAmount() % t_MultipleOf) != 0) {
						l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
						l_DataResult.setStatus(false);
					} else {
						if (payType.equals("10")) { // check Cr acc
							// if (transType < 500) {
							double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
							if (l_ToAccountData.getStatus() == 0 && SharedUtil
									.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
								if (pTransferData[i].getAmount() < l_MinOpeningBalance) {
									l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
									l_DataResult.setStatus(false);
								}
							}
						} else if (payType.equals("20")) { // check Dr acc
							// if (transType > 500 && transType != 969) {
							double l_MinWithdrawal = toproduct.getMinWithdrawal();
							if (pTransferData[i].getAmount() < l_MinWithdrawal) {
								l_DataResult.setDescription(
										"Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
								l_DataResult.setStatus(false);
							}
						}

					}
				}

			}

			// check Debit Available Balance
			if (l_DataResult.getStatus()) {
				double l_Available = 0;
				if (payType.equals("10")) {
					if (i == 0) {
						l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData,
								fromproduct.getMinBalance(), 0, l_Conn);
						// Check For A/C Status Closed Pending
						if (l_FromAccountData.getStatus() != 2) {
							if (pTransferData[i].getAmount() > l_Available) {
								l_DataResult.setStatus(false);
								l_DataResult
										.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
												+ StrUtil.formatNumberwithComma((totAmount - l_Available)));
							}
						} else {
							double l_CurrentBal = l_FromAccountData.getCurrentBalance();
							if (pTransferData[i].getAmount() != l_CurrentBal) {
								l_DataResult.setStatus(false);
								l_DataResult
										.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
												+ StrUtil.formatNumberwithComma((totAmount - l_CurrentBal)));
							}
						}
						if (!l_DataResult.getStatus()) {
							l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrency(pTransferData[i],
									l_FromAccountData, l_TableName, l_TableName1, l_Available, totAmount, l_Conn);
							// l_DataResult =
							// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
							// l_FromAccountData, l_TableName, l_TableName1, l_Available,
							// pTransferData.getAmount(), l_Conn);
							if (l_DataResult.getStatus()) {// Add Auto Link Transref
								AccountTransactionDAO lAccTranDAO = new AccountTransactionDAO();
								if (l_DataResult.getAutoLinkTransRef() > 0)
									lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
											(int) l_DataResult.getAutoLinkTransRef(), l_Conn);
								if (l_DataResult.getSecAutoLinkTransRef() > 0)
									lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
											(int) l_DataResult.getSecAutoLinkTransRef(), l_Conn);
								isautolink = true;
							}

						}
					}
				} else if (payType.equals("20")) {
					l_Available = l_AccDAO.getAvaliableBalance(l_ToAccountData, toproduct.getMinBalance(), 0,
							l_Conn);
					// Check For A/C Status Closed Pending
					if (l_ToAccountData.getStatus() != 2) {
						if (pTransferData[i].getAmount() > l_Available) {
							l_DataResult.setStatus(false);
							l_DataResult.setDescription(l_ToAccountData.getAccountNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_Available)));
						}
					} else {
						double l_CurrentBal = l_ToAccountData.getCurrentBalance();
						if (pTransferData[i].getAmount() != l_CurrentBal) {
							l_DataResult.setStatus(false);
							l_DataResult.setDescription(l_ToAccountData.getAccountNumber() + " insufficient balance "
									+ StrUtil.formatNumberwithComma((pTransferData[i].getAmount() - l_CurrentBal)));
						}
					}
					if (!l_DataResult.getStatus()) {
						l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrency(pTransferData[i],
								l_ToAccountData, l_TableName, l_TableName1, l_Available, pTransferData[i].getAmount(),
								l_Conn);
						// l_DataResult =
						// DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
						// l_FromAccountData, l_TableName, l_TableName1, l_Available,
						// pTransferData.getAmount(), l_Conn);
						if (l_DataResult.getStatus()) {// Add Auto Link Transref
							AccountTransactionDAO lAccTranDAO = new AccountTransactionDAO();
							if (l_DataResult.getAutoLinkTransRef() > 0)
								lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
										(int) l_DataResult.getAutoLinkTransRef(), l_Conn);
							if (l_DataResult.getSecAutoLinkTransRef() > 0)
								lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,
										(int) l_DataResult.getSecAutoLinkTransRef(), l_Conn);
							isautolink = true;
						}

					}
				}

			}

			if (l_DataResult.getStatus()) {
				pTransferData[i].setFromBranchCode(l_FromAccountData.getBranchCode());
				pTransferData[i].setCurrencyCode(l_FromAccountData.getCurrencyCode());
				pTransferData[i].setFromCCY(l_FromAccountData.getCurrencyCode());
				if (i == 0) {
					String fkey1 = "smTransfer" + "_" + pTransferData[i].getFromCCY() + "_"
							+ pTransferData[i].getFromBranchCode();
					drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
				}
				pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
				pTransferData[i].setToBranchCode(l_ToAccountData.getBranchCode());
				pTransferData[i].setToCCY(l_ToAccountData.getCurrencyCode());
				pTransferData[i].setpByForceCheque(pByForceCheque);

				String tkey1 = "smTransfer" + "_" + pTransferData[i].getToCCY() + "_"
						+ pTransferData[i].getToBranchCode();
				crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
			}
			if (l_DataResult.getStatus()) {
				int index = 1;
				if (payType.equals("10")) {
					if (i == 0) {
						// Dr From Customer
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
						pstmt = l_Conn.prepareStatement(l_Query);
						pstmt.setString(index++, pTransferData[i].getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						pstmt.setDouble(index++, totAmount);
						pstmt.setString(index++, pTransferData[i].getFromAccNumber());
						pstmt.setString(index++, pTransferData[i].getFromBranchCode());
						pstmt.setString(index++, pTransferData[i].getFromCCY());
						pstmt.setString(index++, pTransferData[i].getTransactionTime());
						pstmt.setString(index++, pTransferData[i].getTransactionDate());
						pstmt.setString(index++, pTransferData[i].getEffectiveDate());
						pstmt.setString(index++, pTransferData[i].getReferenceNo());
						pstmt.setString(index++, "");
						pstmt.setDouble(index++, drCCYRate);
						pstmt.setDouble(index++, StrUtil.round2decimals(totAmount * drCCYRate));
						pstmt.setString(index++, desc + pTransferData[i].getToAccNumber());
						pstmt.setInt(index++, mPaymentDrTransType);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, fromproduct.getProductGL());
						pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
						pstmt.setString(index++, pTransferData[i].getTransdescription());
						pstmt.executeUpdate();
					}

					// Cr To Account
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData[i].getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData[i].getFromCCY().equalsIgnoreCase(pTransferData[i].getToCCY()))
						pstmt.setDouble(index++, pTransferData[i].getAmount() * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++,
								(pTransferData[i].getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					pstmt.setString(index++, pTransferData[i].getToAccNumber());
					pstmt.setString(index++, pTransferData[i].getToBranchCode());
					pstmt.setString(index++, pTransferData[i].getToCCY());
					pstmt.setString(index++, pTransferData[i].getTransactionTime());
					pstmt.setString(index++, pTransferData[i].getTransactionDate());
					pstmt.setString(index++, pTransferData[i].getEffectiveDate());
					pstmt.setString(index++, pTransferData[i].getReferenceNo());
					pstmt.setString(index++, pTransferData[i].getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData[i].getAmount() * crCCYRate));
					pstmt.setString(index++, desc + pTransferData[i].getFromAccNumber());
					pstmt.setInt(index++, mPaymentCrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, toproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData[i].getTransdescription());
					pstmt.executeUpdate();

				} else if (payType.equals("20")) {
					// Dr From Customer
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData[i].getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, pTransferData[i].getAmount());
					pstmt.setString(index++, pTransferData[i].getToAccNumber());
					pstmt.setString(index++, pTransferData[i].getToBranchCode());
					pstmt.setString(index++, pTransferData[i].getToCCY());
					pstmt.setString(index++, pTransferData[i].getTransactionTime());
					pstmt.setString(index++, pTransferData[i].getTransactionDate());
					pstmt.setString(index++, pTransferData[i].getEffectiveDate());
					pstmt.setString(index++, pTransferData[i].getReferenceNo());
					pstmt.setString(index++, pTransferData[i].getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, StrUtil.round2decimals(pTransferData[i].getAmount() * crCCYRate));
					pstmt.setString(index++, desc + pTransferData[i].getFromAccNumber());
					pstmt.setInt(index++, mPaymentDrTransType);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, toproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData[i].getTransdescription());
					pstmt.executeUpdate();

					if (i == pTransferData.length - 1) {
						// Cr To Account
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
						pstmt = l_Conn.prepareStatement(l_Query);
						index = 1;
						pstmt.setString(index++, pTransferData[i].getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						pstmt.setDouble(index++, totAmount);
						pstmt.setString(index++, pTransferData[i].getFromAccNumber());
						pstmt.setString(index++, pTransferData[i].getFromBranchCode());
						pstmt.setString(index++, pTransferData[i].getFromCCY());
						pstmt.setString(index++, pTransferData[i].getTransactionTime());
						pstmt.setString(index++, pTransferData[i].getTransactionDate());
						pstmt.setString(index++, pTransferData[i].getEffectiveDate());
						pstmt.setString(index++, pTransferData[i].getReferenceNo());
						pstmt.setString(index++, "");
						pstmt.setDouble(index++, crCCYRate);
						pstmt.setDouble(index++, StrUtil.round2decimals(totAmount * drCCYRate));
						pstmt.setString(index++, desc + pTransferData[i].getToAccNumber());
						pstmt.setInt(index++, mPaymentCrTransType);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, fromproduct.getProductGL());
						pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
						pstmt.setString(index++, pTransferData[i].getTransdescription());
						pstmt.executeUpdate();
					}
				}
			}
			if (!l_DataResult.getStatus()) {
				l_DataResult.setCode("0014");
				break;
			}
		}
		if (isautolink)
			l_DataResult.setAutoLinkTransRef(lUncommTransNo);
		return l_DataResult;
	}

	public SMSReturnData postDeposit(DepositData pDepositData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		String l_TableName = "", l_TableName1 = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountDao accDAO = new AccountDao();

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				l_EffectiveDate = GeneralUtility.getTomorrowDate();
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pDepositData.setTransactionDate(l_TodayDate);
		pDepositData.setTransactionTime(l_TransactionTime);
		pDepositData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_deposittransprepare" + pDepositData.getAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();

		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareAccountTransactionDeposit(pDepositData, tmp_Transpreapre, l_TableName, l_TableName1,
				l_Conn);

		try {
			if (l_DataResult.getStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				int autoLinkTransref = (int) l_DataResult.getAutoLinkTransRef();
				if (!pDepositData.getIsGL()) {
					l_DataResult = accDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
							l_Conn);
				} else {
					l_DataResult.setStatus(true);
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
				}

				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef ynw test
//							if(autoLinkTransref>0)
//								new AccountTransactionDAO().updateAutoLinkTransRef(autoLinkTransref,l_DataResult.getTransRef(),l_Conn);
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}

			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}

	public SMSReturnData postWithdraw(WithdrawData pWithdrawData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		String l_TableName = "", l_TableName1 = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountDao accDAO = new AccountDao();

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				l_EffectiveDate = GeneralUtility.getTomorrowDate();
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pWithdrawData.setTransactionDate(l_TodayDate);
		pWithdrawData.setTransactionTime(l_TransactionTime);
		pWithdrawData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_withdrawtransprepare" + pWithdrawData.getAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();

		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareAccountTransactionWithdraw(pWithdrawData, tmp_Transpreapre, l_TableName, l_TableName1,
				l_Conn);

		try {
			if (l_DataResult.getStatus()) {
				l_ArlLogString.add("	Post Transaction ");
				int autoLinkTransref = (int) l_DataResult.getAutoLinkTransRef();

				if (!pWithdrawData.getIsGL()) {
					l_DataResult = accDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
							l_Conn);
				} else {
					l_DataResult.setStatus(true);
				}
				if (l_DataResult.getStatus()) {
					l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
				}

				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef ynw test
//							if(autoLinkTransref>0)
//								new AccountTransactionDAO().updateAutoLinkTransRef(autoLinkTransref,l_DataResult.getTransRef(),l_Conn);
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}

			} else {
				l_SMSReturnData.setDescription(l_DataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}

	public static DataResult canWithdrawforSavingMobile(String pAccNumber, String pEffictiveDate, Connection pConn)
			throws SQLException {
		DataResult l_result = new DataResult();
		AccountTransactionDAO l_AccTranDAO = new AccountTransactionDAO();
		TransactionData l_Data = new TransactionData();

		SimpleDateFormat l_df = new SimpleDateFormat("yyyyMMdd");
		Calendar l_calaner = Calendar.getInstance();
		String l_fromDate = "";
		int canwithdraw = SharedLogic.getSystemData().getpSystemSettingDataList().get("MWWL").getN1();

		if (canwithdraw != 0) {

			Date l_EffectiveDate;
			try {
				l_EffectiveDate = l_df.parse(pEffictiveDate);

				int l_day = l_EffectiveDate.getDay();
				if (l_day == 1) {
					l_fromDate = pEffictiveDate;
				} else {
					l_calaner.setTime(l_df.parse(pEffictiveDate));
					l_calaner.add(Calendar.DAY_OF_WEEK, -(l_day - 1));
					l_fromDate = l_df.format(l_calaner.getTime());
				}

				int l_Cnt;
				l_Cnt = l_AccTranDAO.getTotalCountofSADrTransactionOfWeekNew(pAccNumber, l_fromDate, pEffictiveDate,
						canwithdraw, pConn);

				if (l_Cnt < canwithdraw) {
					l_result.setStatus(true);
				} else {
					// SingletonServer.getUser().getRights();
					l_result.setStatus(false);
					l_result.setErrorFlag(1);
					l_result.setDescription(
							"Savings Account can't withdraw more than " + canwithdraw + " times in a week!");
					l_result.setData(l_Data.getPreviousDate());
				}

			} catch (ParseException e) {
				l_result.setStatus(false);
				l_result.setDescription(e.getMessage());
				e.printStackTrace();
			}

		} else if (canwithdraw == 0) {
			l_result.setStatus(true);
		}
		return l_result;
	}

	public static DataResult canWithdrawforSaving(String pAccNumber, String pEffictiveDate, int pTransType,
			Connection pConn) throws SQLException {
		DataResult l_result = new DataResult();
		AccountTransactionDAO l_AccTranDAO = new AccountTransactionDAO();
		TransactionData l_Data = new TransactionData();

		SimpleDateFormat l_df = new SimpleDateFormat("yyyyMMdd");
		Calendar l_calaner = Calendar.getInstance();
		String l_fromDate = "";

		try {

			int canwithdraw = SharedLogic.getSystemData().getpSystemSettingDataList().get("WWL").getN1();

			if (canwithdraw != 0) {
				Date l_EffectiveDate = l_df.parse(pEffictiveDate);
				int l_day = l_EffectiveDate.getDay();
				if (l_day == 1) {
					l_fromDate = pEffictiveDate;
				} else {
					l_calaner.setTime(l_df.parse(pEffictiveDate));
					l_calaner.add(Calendar.DAY_OF_WEEK, -(l_day - 1));
					l_fromDate = l_df.format(l_calaner.getTime());
				}
				if (pTransType >= 500 && pTransType < 699) {
					l_Data = l_AccTranDAO.getTotalCountofSADrTransactionOfWeek(pAccNumber, l_fromDate, pEffictiveDate,
							canwithdraw, pConn);

					if (l_Data.getPreviousDate().equals("19000101")) {
						l_result.setStatus(true);
					} else {
						// SingletonServer.getUser().getRights();
						l_result.setStatus(false);
						l_result.setDescription(
								"Savings Account can't withdraw more than " + canwithdraw + " times in a week!");
						l_result.setData(l_Data.getPreviousDate());
					}
				} else {
					l_result.setStatus(true);
				}

			} else if (canwithdraw == 0) {
				l_result.setStatus(true);
			}

		} catch (ParseException e) {
			l_result.setStatus(false);
			l_result.setDescription(e.getMessage());
			e.printStackTrace();
		}

		return l_result;
	}

	public LoanRepaymentRes postLoanRepayment(TransferData pTransferData, LoanRepaymentReq aReq, Connection l_Conn)
			throws Exception {
		LoanRepaymentRes l_SMSReturnData = new LoanRepaymentRes();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "",tomorrowDate ="";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountDao acc_dao = new AccountDao();
		String l_TableName = "", l_TableName1 = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}

		l_SMSReturnData.setTransdate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		String l_SanctionDateBr = GeneralUtility.getTodayDate(); // acc_dao.getSanctionDateBr(aReq.getLoanaccnumber(),
																	// l_Conn);
		LoanInterestDAO l_InterestDAO = new LoanInterestDAO();
		double checkAccrueAmt = 0;

		checkAccrueAmt = l_InterestDAO.checkAccrueAmt(pTransferData.getToAccNumber(), l_SanctionDateBr, l_Conn);
		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_transprepare" + pTransferData.getFromAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();

		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareAccountTransactionLoanRepayment(pTransferData, tmp_Transpreapre, l_TableName,
				l_TableName1, aReq.getLoantype(), l_SanctionDateBr, checkAccrueAmt, l_Conn);

		try {
			if (l_DataResult.getStatus()) {

				LoanAccountDAO l_loanaccountdao = new LoanAccountDAO();
				LoanAccountData p_loanaccountdata = new LoanAccountData();
				p_loanaccountdata.setSettleAmount(pTransferData.getAmount());
				p_loanaccountdata.setAccNumber(aReq.getLoanaccnumber());
				p_loanaccountdata.setBatchNo(1);
				l_DataResult.setStatus(
						l_loanaccountdao.saveLAODFHistoryTable(p_loanaccountdata, pTransferData.getInterest(), l_Conn));
				if (l_DataResult.getStatus()) {
					p_loanaccountdata.setLoanType(aReq.getLoantype());

					LAODFDAO l_LAODFDAO = new LAODFDAO();
					ArrayList<LAODFData> l_LAODFDataList = new ArrayList<LAODFData>();
					l_LAODFDataList = l_LAODFDAO.getLAODFDataListByAccNumber(aReq.getLoanaccnumber(), l_Conn);
					p_loanaccountdata
							.setExpDate(GeneralUtility.changeDateFormatyyyyMMdd(l_LAODFDataList.get(0).getExpDate()));
					p_loanaccountdata.setSanctionDateHO(
							GeneralUtility.changeDateFormatyyyyMMdd(l_LAODFDataList.get(0).getSanctionDateHO()));
					p_loanaccountdata.setApprovalNoHO(l_LAODFDataList.get(0).getApprovalNoHO());
					p_loanaccountdata.setDrPriority(l_LAODFDataList.get(0).getDrPriority());
					p_loanaccountdata.setBusinessClass((byte) l_LAODFDataList.get(0).getBusinessClass());
					double totSanctionAmt = 0;
					totSanctionAmt = l_LAODFDAO.getTotalLimtByAccNumber(aReq.getLoanaccnumber(), l_Conn);
					/*
					 * if(totSanctionAmt == 0) { DBLoanMgr db_mgr = new DBLoanMgr();
					 * ArrayList<LoanAllowSetupData> loanAllowDataList =
					 * db_mgr.getLoanAllowDataForAllow(aReq.getLoantype(), aReq.getLoanaccnumber(),
					 * l_Conn); for (LoanAllowSetupData data : loanAllowDataList) { if
					 * (data.getType() == 1 && data.getTransType() == "Dr") { totSanctionAmt =
					 * data.getLimitAmount(); } } }
					 */
					p_loanaccountdata.setSanctionAmountBr(totSanctionAmt);
					p_loanaccountdata.setSanctionDateBr(l_SanctionDateBr);
					l_DataResult.setStatus(l_loanaccountdao.saveLAODFTable(p_loanaccountdata, l_Conn));
				}
				// p_loanaccountdata.setCurrencyRate(DBFECCurrencyRateMgr.getFECCurrencyRate(l_LA_AccountData.getCurrencyCode(),
				// "smLoanAllowForm", l_Conn));
				// p_loanaccountdata.setN1(req.getInterestamt());
				// p_loanaccountdata.setN2(req.getServicechargesamt());

				if (l_DataResult.getStatus()) {
					l_ArlLogString.add("	Post Transaction ");
					AccountDao lAccDAO = new AccountDao();
					// update Balance
					l_DataResult = lAccDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1,
							l_Conn);
					if (l_DataResult.getStatus()) {
						l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
					}

					if (l_DataResult.getStatus()) {
						if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5() == 1) {
							if (checkAccrueAmt > 0) {
								if (l_InterestDAO.updateLoanInterestStatusByAccNOAndLessThenRepayDate(
										p_loanaccountdata.getAccNumber(), p_loanaccountdata.getSanctionDateBr(),
										l_Conn))
									l_DataResult.setStatus(true);
								else {
									l_DataResult.setStatus(false);
									l_DataResult.setDescription("Update Loan interest status Fail!");
								}
							}
						} else {
							boolean interestStatus = false;
							if (pTransferData.getInterest() > 0) {
								interestStatus = true;
							}
							if (interestStatus) {
								if (l_InterestDAO.updateLoanInterestStatusByAccNOAndLessThenRepayDate(
										p_loanaccountdata.getAccNumber(), p_loanaccountdata.getSanctionDateBr(),
										l_Conn))
									l_DataResult.setStatus(true);
								else {
									l_DataResult.setStatus(false);
									l_DataResult.setDescription("Update Loan interest status Fail!");
								}
							}
						}
					}

					// update loan related
					if (l_DataResult.getStatus()) {
						AccountDao l_accountdao = new AccountDao();
						double l_currentbalance = l_accountdao.getCurrentBalance(p_loanaccountdata.getAccNumber(),
								l_Conn);
						if (l_currentbalance == 0) {
							l_DataResult.setStatus(
									l_accountdao.updateAccountStatus(p_loanaccountdata.getAccNumber(), l_Conn));
							if (l_DataResult.getStatus()) {
								l_DataResult.setStatus(
										l_loanaccountdao.updateLoanStatus(p_loanaccountdata.getAccNumber(), l_Conn));
							}
						}
						if (l_DataResult.getStatus()) {
							if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN1() == 22) {
								l_DataResult.setStatus(
										l_loanaccountdao.insertLAODFRepayment(p_loanaccountdata.getAccNumber(),
												Math.abs(p_loanaccountdata.getSanctionAmountBr()), l_Conn));
							}

							if (l_DataResult.getStatus()) {
								if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LSC").getN5() == 3) {
									LoanServiceChargesExtendedDAO l_SCExtDAO = new LoanServiceChargesExtendedDAO();
									for (int i = 0; i < p_loanaccountdata.getTempDataList().size(); i++) {
										TempData l_SCExtData = new TempData();
										l_SCExtData.setT1(p_loanaccountdata.getAccNumber());
										l_SCExtData.setT2(p_loanaccountdata.getTempDataList().get(i).getT1());
										l_SCExtData.setN1(p_loanaccountdata.getTempDataList().get(i).getN1());

										if (l_InterestDAO.getLoanInterestByAccNumberAndDate(
												p_loanaccountdata.getAccNumber(),
												p_loanaccountdata.getTempDataList().get(i).getT1(), l_Conn)) {
											// update
											l_DataResult.setStatus(l_InterestDAO.updateLoanInterestByAccNumberAndDate(
													p_loanaccountdata.getAccNumber(),
													p_loanaccountdata.getTempDataList().get(i).getT1(), l_Conn));
										} else if (l_SCExtDAO.getLoanServiceCharges(l_SCExtData, l_Conn)) {
											// update
											l_DataResult.setStatus(l_SCExtDAO.updateLoanInterestByAccNumberAndDate(
													p_loanaccountdata.getAccNumber(),
													p_loanaccountdata.getTempDataList().get(i).getT1(),
													p_loanaccountdata.getTempDataList().get(i).getN1(), l_Conn));
										} else {
											// insert
											l_DataResult
													.setStatus(l_SCExtDAO.addLoanServiceCharges(l_SCExtData, l_Conn));
										}
									}
								}
							}
						}
					}
					// Thread Testing
					if (l_DataResult.getStatus()) {
						l_SMSReturnData.setRetcode("300");
						l_SMSReturnData.setBankrefnumber(String.valueOf(l_DataResult.getTransRef()));
						l_SMSReturnData.setTransdate(GeneralUtil.getDate());
						l_SMSReturnData.setEffectivedate(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(l_EffectiveDate));
						l_SMSReturnData.setRetmessage("Posted successfully");
						// Update Auto link Transfef
					} else {
						l_SMSReturnData.setRetcode("210");
						l_SMSReturnData.setRetmessage(l_DataResult.getDescription());
					}
				}

			} else {
				l_SMSReturnData.setRetmessage(l_DataResult.getDescription());
				l_SMSReturnData.setRetcode("210");
				return l_SMSReturnData;
			}

			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			l_SMSReturnData.setRetcode("210");
			l_SMSReturnData.setRetmessage("Transaction Failed." + e.getMessage());
		}

		return l_SMSReturnData;
	}

	public DataResult prepareAccountTransactionLoanRepayment(TransferData pTransferData, String tmp_Transpreapre,
			String l_TableName, String l_TableName1, int loanType, String l_SanctionDateBr, double checkAccrueAmt,
			Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String pByForceCheque = "";
		String fkey = "", tkey = "";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0;
		double crCCYRate = 0;
		int lUncommTransNo = DBTransactionMgr.nextValue();
		double totalTransAmt = pTransferData.getAmount() + pTransferData.getInterest()
				+ pTransferData.getServiceCharges();
		l_DataResult = l_AccDAO.getTransAccount(pTransferData.getFromAccNumber(), "Dr", totalTransAmt, l_Conn);
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
		}

		if (l_DataResult.getStatus()) {
			if (l_AccDAO.getAccount(pTransferData.getToAccNumber(), l_Conn)) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
			} else {
				l_DataResult.setStatus(false);
			}
		}

		if (l_DataResult.getStatus()) {
			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				fkey = l_FromAccountData.getProduct();
				tkey = l_ToAccountData.getProduct();
			}
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

			// For Checking Transaction
			// Check Multiple Of
			double f_MultipleOf = fromproduct.getMultipleOf();
			double t_MultipleOf = toproduct.getMultipleOf();
			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if ((int) (totalTransAmt % f_MultipleOf) != 0) {
				l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
				l_DataResult.setStatus(false);
			} else {
				// if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = fromproduct.getMinWithdrawal();
				if (totalTransAmt < l_MinWithdrawal) {
					l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
					l_DataResult.setStatus(false);
				}
			}
			if (l_DataResult.getStatus()) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus()) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinOpeningBalance(), 0,
					l_Conn);
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (totalTransAmt > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (totalTransAmt != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}
			/*
			 * if(!l_DataResult.getStatus()) { l_DataResult =
			 * DBTransactionMgr.checkAndPostLinkWithMultiCurrency( pTransferData,
			 * l_FromAccountData, l_TableName, l_TableName1, l_Available,
			 * pTransferData.getAmount(), l_Conn); //l_DataResult =
			 * DBTransactionMgr.checkAndPostLinkWithMultiCurrencynew(pTransferData,
			 * l_FromAccountData, l_TableName, l_TableName1, l_Available,
			 * pTransferData.getAmount(), l_Conn); if (l_DataResult.getStatus()) {// Add
			 * Auto Link Transref AccountTransactionDAO lAccTranDAO=new
			 * AccountTransactionDAO(); if(l_DataResult.getAutoLinkTransRef()>0)
			 * lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,(int)
			 * l_DataResult.getAutoLinkTransRef(), l_Conn);
			 * if(l_DataResult.getSecAutoLinkTransRef()>0)
			 * lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,(int)
			 * l_DataResult.getSecAutoLinkTransRef(), l_Conn);
			 * l_DataResult.setAutoLinkTransRef(lUncommTransNo); }
			 * 
			 * }
			 */
		}
		if (l_DataResult.getStatus()) {

			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
			pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
			pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
			pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());

			String fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
			String tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();

			drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
		}
		PreparedStatement pstmt = null;
		String l_Query = "";
		boolean interestStatus = false;
		if (l_DataResult.getStatus()) {
			int index = 1;
			if (pTransferData.getAmount() > 0) {
				// Dr From Customer
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
				pstmt = l_Conn.prepareStatement(l_Query);
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				pstmt.setDouble(index++, pTransferData.getAmount());
				pstmt.setString(index++, pTransferData.getFromAccNumber());
				pstmt.setString(index++, pTransferData.getFromBranchCode());
				pstmt.setString(index++, pTransferData.getFromCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, drCCYRate);
				pstmt.setDouble(index++, pTransferData.getAmount() * drCCYRate);
				pstmt.setString(index++, pTransferData.getToAccNumber() + ", Loan Repayment Debit, "
						+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, mLoanRepaymentDr);
				pstmt.setString(index++, fromproduct.getCashInHandGL());
				pstmt.setString(index++, fromproduct.getProductGL());
				pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.executeUpdate();

				// Cr To Customer
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
				pstmt = l_Conn.prepareStatement(l_Query);
				index = 1;
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					pstmt.setDouble(index++, pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
				else
					pstmt.setDouble(index++,
							(pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

				pstmt.setString(index++, pTransferData.getToAccNumber());
				pstmt.setString(index++, pTransferData.getToBranchCode());
				pstmt.setString(index++, pTransferData.getToCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, crCCYRate);
				pstmt.setDouble(index++, pTransferData.getAmount() * drCCYRate);
				pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Repayment Credit" + ", "
						+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, mLoanRepaymentCr);
				pstmt.setString(index++, toproduct.getCashInHandGL());
				pstmt.setString(index++, toproduct.getProductGL());
				pstmt.setString(index++, "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.executeUpdate();
			}
			FunctionID functionID = new FunctionID();
			ReferenceAccountsData l_RefData = new ReferenceAccountsData();
			String l_FID = "", l_GL = "", accrueGL = "", accrueFID = "";
			// Interest Amount
			if (pTransferData.getInterest() > 0) {

				functionID.setBranchCode(pTransferData.getFromBranchCode());
				functionID.setCurrencyCode(pTransferData.getFromCCY());

				interestStatus = true;
				functionID.setType(Enum.FunctionID.LAI);
				if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN4() == 1) {
					functionID.setLaType(loanType);
				}
				l_FID = functionID.getFunctionID();
				// l_GL = DBSystemMgr.getGLCode(l_FID, l_Conn);
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_FID);
				if (l_RefData != null)
					l_GL = l_RefData.getGLCode();

				functionID.setType(Enum.FunctionID.LAA);
				if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN4() == 1) {
					functionID.setLaType(loanType);
				}
				accrueFID = functionID.getFunctionID();
				// accrueGL = DBSystemMgr.getGLCode(accrueFID, l_Conn);
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(accrueFID);
				if (l_RefData != null)
					accrueGL = l_RefData.getGLCode();

				double diffIntAmt = 0, acccrueIntAmt2 = 0;

				if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5() == 1
						|| SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN5() == 2) {
					if (pTransferData.getInterest() == checkAccrueAmt) {
						// Dr BackAccount
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
						pstmt = l_Conn.prepareStatement(l_Query);
						index = 1;
						pstmt.setString(index++, pTransferData.getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						pstmt.setDouble(index++, pTransferData.getInterest());
						pstmt.setString(index++, pTransferData.getFromAccNumber());
						pstmt.setString(index++, pTransferData.getFromBranchCode());
						pstmt.setString(index++, pTransferData.getFromCCY());
						pstmt.setString(index++, pTransferData.getTransactionTime());
						pstmt.setString(index++, pTransferData.getTransactionDate());
						pstmt.setString(index++, pTransferData.getEffectiveDate());
						pstmt.setString(index++, pTransferData.getReferenceNo());
						pstmt.setString(index++, pTransferData.getRemark());
						pstmt.setDouble(index++, drCCYRate);
						pstmt.setDouble(index++, pTransferData.getInterest() * drCCYRate);
						pstmt.setString(index++, l_GL + ", Loan Repayment Interest Debit, "
								+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
						pstmt.setInt(index++, mLoanRepaymentDr);
						pstmt.setString(index++, fromproduct.getCashInHandGL());
						pstmt.setString(index++, fromproduct.getProductGL());
						pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
						pstmt.setString(index++, pTransferData.getTransdescription());
						pstmt.executeUpdate();

						// Cr GL
						l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
								+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
						pstmt = l_Conn.prepareStatement(l_Query);
						index = 1;
						pstmt.setString(index++, pTransferData.getUserID());
						pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
						if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
							pstmt.setDouble(index++, pTransferData.getInterest() * (drCCYRate * 1000) / 1000);
						else
							pstmt.setDouble(index++,
									(pTransferData.getInterest() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

						pstmt.setString(index++, accrueGL);
						pstmt.setString(index++, pTransferData.getToBranchCode());
						pstmt.setString(index++, pTransferData.getToCCY());
						pstmt.setString(index++, pTransferData.getTransactionTime());
						pstmt.setString(index++, pTransferData.getTransactionDate());
						pstmt.setString(index++, pTransferData.getEffectiveDate());
						pstmt.setString(index++, pTransferData.getReferenceNo());
						pstmt.setString(index++, pTransferData.getRemark());
						pstmt.setDouble(index++, crCCYRate);
						pstmt.setDouble(index++, pTransferData.getInterest() * drCCYRate);
						pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Repayment Interest Credit"
								+ ", " + GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
						pstmt.setInt(index++, mLoanRepaymentCr);
						pstmt.setString(index++, toproduct.getCashInHandGL());
						pstmt.setString(index++, toproduct.getProductGL());
						pstmt.setString(index++, "");
						pstmt.setInt(index++, 1);
						pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
						pstmt.setString(index++, pTransferData.getTransdescription());
						pstmt.executeUpdate();
					} else {
						if (SharedLogic.getSystemSettingT12N1("BNK") == 17) {
							diffIntAmt = pTransferData.getInterest() - checkAccrueAmt;

							if (checkAccrueAmt == 0) {
								acccrueIntAmt2 = pTransferData.getInterest(); // ---- if Accrue hasn't amount is
																				// InterestAmount
								// from Form
								accrueGL = l_GL; // ---- if Accrue hasn't CusAcc to InterestGL
								diffIntAmt = 0;

							} else {
								acccrueIntAmt2 = checkAccrueAmt;
							}

							// -----------------------------------------------//
							// Dr BackAccount
							l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
									+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
							pstmt = l_Conn.prepareStatement(l_Query);
							index = 1;
							pstmt.setString(index++, pTransferData.getUserID());
							pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
							pstmt.setDouble(index++, (acccrueIntAmt2 + diffIntAmt));
							pstmt.setString(index++, pTransferData.getFromAccNumber());
							pstmt.setString(index++, pTransferData.getFromBranchCode());
							pstmt.setString(index++, pTransferData.getFromCCY());
							pstmt.setString(index++, pTransferData.getTransactionTime());
							pstmt.setString(index++, pTransferData.getTransactionDate());
							pstmt.setString(index++, pTransferData.getEffectiveDate());
							pstmt.setString(index++, pTransferData.getReferenceNo());
							pstmt.setString(index++, pTransferData.getRemark());
							pstmt.setDouble(index++, drCCYRate);
							pstmt.setDouble(index++, (acccrueIntAmt2 + diffIntAmt) * drCCYRate);
							pstmt.setString(index++, accrueGL + ", Loan Repayment Interest Debit, "
									+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
							pstmt.setInt(index++, mLoanRepaymentDr);
							pstmt.setString(index++, fromproduct.getCashInHandGL());
							pstmt.setString(index++, fromproduct.getProductGL());
							pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
							pstmt.setInt(index++, 1);
							pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
							pstmt.setString(index++, pTransferData.getTransdescription());
							pstmt.executeUpdate();

							// Cr GL
							l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
									+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
							pstmt = l_Conn.prepareStatement(l_Query);
							index = 1;
							pstmt.setString(index++, pTransferData.getUserID());
							pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
							if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
								pstmt.setDouble(index++, acccrueIntAmt2 * (drCCYRate * 1000) / 1000);
							else
								pstmt.setDouble(index++,
										(acccrueIntAmt2 * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

							pstmt.setString(index++, accrueGL);
							pstmt.setString(index++, pTransferData.getToBranchCode());
							pstmt.setString(index++, pTransferData.getToCCY());
							pstmt.setString(index++, pTransferData.getTransactionTime());
							pstmt.setString(index++, pTransferData.getTransactionDate());
							pstmt.setString(index++, pTransferData.getEffectiveDate());
							pstmt.setString(index++, pTransferData.getReferenceNo());
							pstmt.setString(index++, pTransferData.getRemark());
							pstmt.setDouble(index++, crCCYRate);
							pstmt.setDouble(index++, acccrueIntAmt2 * drCCYRate);
							pstmt.setString(index++,
									pTransferData.getFromAccNumber() + ", Loan Repayment Interest Credit" + ", "
											+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
							pstmt.setInt(index++, mLoanRepaymentCr);
							pstmt.setString(index++, toproduct.getCashInHandGL());
							pstmt.setString(index++, toproduct.getProductGL());
							pstmt.setString(index++, "");
							pstmt.setInt(index++, 1);
							pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
							pstmt.setString(index++, pTransferData.getTransdescription());
							pstmt.executeUpdate();
							if (checkAccrueAmt > 0 && diffIntAmt > 0) {
								// Cr GL
								// TransCode.LoanRepaymentCr,pLoanData.getAccLinked(), l_GL,
								l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
										+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
								pstmt = l_Conn.prepareStatement(l_Query);
								index = 1;
								pstmt.setString(index++, pTransferData.getUserID());
								pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
								if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
									pstmt.setDouble(index++, diffIntAmt * (drCCYRate * 1000) / 1000);
								else
									pstmt.setDouble(index++,
											(diffIntAmt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

								pstmt.setString(index++, l_GL);
								pstmt.setString(index++, pTransferData.getToBranchCode());
								pstmt.setString(index++, pTransferData.getToCCY());
								pstmt.setString(index++, pTransferData.getTransactionTime());
								pstmt.setString(index++, pTransferData.getTransactionDate());
								pstmt.setString(index++, pTransferData.getEffectiveDate());
								pstmt.setString(index++, pTransferData.getReferenceNo());
								pstmt.setString(index++, pTransferData.getRemark());
								pstmt.setDouble(index++, crCCYRate);
								pstmt.setDouble(index++, diffIntAmt * drCCYRate);
								pstmt.setString(index++,
										pTransferData.getFromAccNumber() + ", Loan Repayment Interest Credit" + ", "
												+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
								pstmt.setInt(index++, mLoanRepaymentCr);
								pstmt.setString(index++, toproduct.getCashInHandGL());
								pstmt.setString(index++, toproduct.getProductGL());
								pstmt.setString(index++, "");
								pstmt.setInt(index++, 1);
								pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
								pstmt.setString(index++, pTransferData.getTransdescription());
								pstmt.executeUpdate();
							}

						} else {
							diffIntAmt = pTransferData.getInterest() - checkAccrueAmt;

							if (checkAccrueAmt == 0) {
								acccrueIntAmt2 = pTransferData.getInterest(); // ---- if Accrue hasn't amount is
																				// InterestAmount
								// from Form
								accrueGL = l_GL; // ---- if Accrue hasn't CusAcc to InterestGL
							} else {
								if (diffIntAmt < 0) {
									acccrueIntAmt2 = pTransferData.getInterest();
								} else {
									acccrueIntAmt2 = checkAccrueAmt;
								}
							}

							// Dr BackAccount
							l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
									+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
							pstmt = l_Conn.prepareStatement(l_Query);
							index = 1;
							pstmt.setString(index++, pTransferData.getUserID());
							pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
							pstmt.setDouble(index++, acccrueIntAmt2);
							pstmt.setString(index++, pTransferData.getFromAccNumber());
							pstmt.setString(index++, pTransferData.getFromBranchCode());
							pstmt.setString(index++, pTransferData.getFromCCY());
							pstmt.setString(index++, pTransferData.getTransactionTime());
							pstmt.setString(index++, pTransferData.getTransactionDate());
							pstmt.setString(index++, pTransferData.getEffectiveDate());
							pstmt.setString(index++, pTransferData.getReferenceNo());
							pstmt.setString(index++, pTransferData.getRemark());
							pstmt.setDouble(index++, drCCYRate);
							pstmt.setDouble(index++, acccrueIntAmt2 * drCCYRate);
							pstmt.setString(index++, accrueGL + ", Loan Repayment Interest Debit, "
									+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
							pstmt.setInt(index++, mLoanRepaymentDr);
							pstmt.setString(index++, fromproduct.getCashInHandGL());
							pstmt.setString(index++, fromproduct.getProductGL());
							pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
							pstmt.setInt(index++, 1);
							pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
							pstmt.setString(index++, pTransferData.getTransdescription());
							pstmt.executeUpdate();

							// Cr GL
							l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
									+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
							pstmt = l_Conn.prepareStatement(l_Query);
							index = 1;
							pstmt.setString(index++, pTransferData.getUserID());
							pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
							if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
								pstmt.setDouble(index++, acccrueIntAmt2 * (drCCYRate * 1000) / 1000);
							else
								pstmt.setDouble(index++,
										(acccrueIntAmt2 * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

							pstmt.setString(index++, accrueGL);
							pstmt.setString(index++, pTransferData.getToBranchCode());
							pstmt.setString(index++, pTransferData.getToCCY());
							pstmt.setString(index++, pTransferData.getTransactionTime());
							pstmt.setString(index++, pTransferData.getTransactionDate());
							pstmt.setString(index++, pTransferData.getEffectiveDate());
							pstmt.setString(index++, pTransferData.getReferenceNo());
							pstmt.setString(index++, pTransferData.getRemark());
							pstmt.setDouble(index++, crCCYRate);
							pstmt.setDouble(index++, acccrueIntAmt2 * drCCYRate);
							pstmt.setString(index++,
									pTransferData.getFromAccNumber() + ", Loan Repayment Interest Credit" + ", "
											+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
							pstmt.setInt(index++, mLoanRepaymentCr);
							pstmt.setString(index++, toproduct.getCashInHandGL());
							pstmt.setString(index++, toproduct.getProductGL());
							pstmt.setString(index++, "");
							pstmt.setInt(index++, 1);
							pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
							pstmt.setString(index++, pTransferData.getTransdescription());
							pstmt.executeUpdate();
							// -----------------------------------------------//
							if (checkAccrueAmt > 0) {
								if (diffIntAmt > 0) {
									// Dr BackAccount
									l_Query = "INSERT INTO " + tmp_Transpreapre
											+ " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
											+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
									pstmt = l_Conn.prepareStatement(l_Query);
									index = 1;
									pstmt.setString(index++, pTransferData.getUserID());
									pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
									pstmt.setDouble(index++, diffIntAmt);
									pstmt.setString(index++, pTransferData.getFromAccNumber());
									pstmt.setString(index++, pTransferData.getFromBranchCode());
									pstmt.setString(index++, pTransferData.getFromCCY());
									pstmt.setString(index++, pTransferData.getTransactionTime());
									pstmt.setString(index++, pTransferData.getTransactionDate());
									pstmt.setString(index++, pTransferData.getEffectiveDate());
									pstmt.setString(index++, pTransferData.getReferenceNo());
									pstmt.setString(index++, pTransferData.getRemark());
									pstmt.setDouble(index++, drCCYRate);
									pstmt.setDouble(index++, diffIntAmt * drCCYRate);
									pstmt.setString(index++, l_GL + ", Loan Repayment Interest Debit, "
											+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
									pstmt.setInt(index++, mLoanRepaymentDr);
									pstmt.setString(index++, fromproduct.getCashInHandGL());
									pstmt.setString(index++, fromproduct.getProductGL());
									pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
									pstmt.setInt(index++, 1);
									pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
									pstmt.setString(index++, pTransferData.getTransdescription());
									pstmt.executeUpdate();

									// Cr GL
									l_Query = "INSERT INTO " + tmp_Transpreapre
											+ " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
											+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
									pstmt = l_Conn.prepareStatement(l_Query);
									index = 1;
									pstmt.setString(index++, pTransferData.getUserID());
									pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
									if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
										pstmt.setDouble(index++, diffIntAmt * (drCCYRate * 1000) / 1000);
									else
										pstmt.setDouble(index++,
												(diffIntAmt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

									pstmt.setString(index++, l_GL);
									pstmt.setString(index++, pTransferData.getToBranchCode());
									pstmt.setString(index++, pTransferData.getToCCY());
									pstmt.setString(index++, pTransferData.getTransactionTime());
									pstmt.setString(index++, pTransferData.getTransactionDate());
									pstmt.setString(index++, pTransferData.getEffectiveDate());
									pstmt.setString(index++, pTransferData.getReferenceNo());
									pstmt.setString(index++, pTransferData.getRemark());
									pstmt.setDouble(index++, crCCYRate);
									pstmt.setDouble(index++, diffIntAmt * drCCYRate);
									pstmt.setString(index++,
											pTransferData.getFromAccNumber() + ", Loan Repayment Interest Credit" + ", "
													+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
									pstmt.setInt(index++, mLoanRepaymentCr);
									pstmt.setString(index++, toproduct.getCashInHandGL());
									pstmt.setString(index++, toproduct.getProductGL());
									pstmt.setString(index++, "");
									pstmt.setInt(index++, 1);
									pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
									pstmt.setString(index++, pTransferData.getTransdescription());
									pstmt.executeUpdate();
								} else {

									// Dr BackAccount
									l_Query = "INSERT INTO " + tmp_Transpreapre
											+ " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
											+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
									pstmt = l_Conn.prepareStatement(l_Query);
									index = 1;
									pstmt.setString(index++, pTransferData.getUserID());
									pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
									pstmt.setDouble(index++, diffIntAmt * -1);
									pstmt.setString(index++, accrueGL);
									pstmt.setString(index++, pTransferData.getFromBranchCode());
									pstmt.setString(index++, pTransferData.getFromCCY());
									pstmt.setString(index++, pTransferData.getTransactionTime());
									pstmt.setString(index++, pTransferData.getTransactionDate());
									pstmt.setString(index++, pTransferData.getEffectiveDate());
									pstmt.setString(index++, pTransferData.getReferenceNo());
									pstmt.setString(index++, pTransferData.getRemark());
									pstmt.setDouble(index++, drCCYRate);
									pstmt.setDouble(index++, (diffIntAmt * -1) * drCCYRate);
									pstmt.setString(index++, l_GL + ", Loan Repayment Interest Debit, "
											+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
									pstmt.setInt(index++, mLoanRepaymentDr);
									pstmt.setString(index++, fromproduct.getCashInHandGL());
									pstmt.setString(index++, fromproduct.getProductGL());
									pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
									pstmt.setInt(index++, 1);
									pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
									pstmt.setString(index++, pTransferData.getTransdescription());
									pstmt.executeUpdate();

									// Cr GL
									l_Query = "INSERT INTO " + tmp_Transpreapre
											+ " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
											+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
									pstmt = l_Conn.prepareStatement(l_Query);
									index = 1;
									pstmt.setString(index++, pTransferData.getUserID());
									pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
									if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
										pstmt.setDouble(index++, (diffIntAmt) * -1 * (drCCYRate * 1000) / 1000);
									else
										pstmt.setDouble(index++, ((diffIntAmt * -1) * drCCYRate / crCCYRate)
												* (drCCYRate * 1000) / 1000);

									pstmt.setString(index++, l_GL);
									pstmt.setString(index++, pTransferData.getToBranchCode());
									pstmt.setString(index++, pTransferData.getToCCY());
									pstmt.setString(index++, pTransferData.getTransactionTime());
									pstmt.setString(index++, pTransferData.getTransactionDate());
									pstmt.setString(index++, pTransferData.getEffectiveDate());
									pstmt.setString(index++, pTransferData.getReferenceNo());
									pstmt.setString(index++, pTransferData.getRemark());
									pstmt.setDouble(index++, crCCYRate);
									pstmt.setDouble(index++, (diffIntAmt * -1) * drCCYRate);
									pstmt.setString(index++, accrueGL + ", Loan Repayment Interest Credit" + ", "
											+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
									pstmt.setInt(index++, mLoanRepaymentCr);
									pstmt.setString(index++, toproduct.getCashInHandGL());
									pstmt.setString(index++, toproduct.getProductGL());
									pstmt.setString(index++, "");
									pstmt.setInt(index++, 1);
									pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
									pstmt.setString(index++, pTransferData.getTransdescription());
									pstmt.executeUpdate();
								}
							}
						}
					}
				} else {
					// Dr BackAccount
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					pstmt.setDouble(index++, pTransferData.getInterest());
					pstmt.setString(index++, pTransferData.getFromAccNumber());
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, pTransferData.getInterest() * drCCYRate);
					pstmt.setString(index++,
							l_GL + ", Loan Repayment Interest Debit, " + GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentDr);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, fromproduct.getProductGL());
					pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();

					// Cr GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, pTransferData.getInterest() * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++,
								(pTransferData.getInterest() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_GL);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, pTransferData.getRemark());
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, pTransferData.getInterest() * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Repayment Interest Credit"
							+ ", " + GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}

			}

			// Service Charges Amount
			if (pTransferData.getServiceCharges() > 0) {
				functionID.setType(Enum.FunctionID.LAS);
				if (SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN4() == 1) {
					functionID.setLaType(loanType);
				}
				l_FID = functionID.getFunctionID();
				// l_GL = DBSystemMgr.getGLCode(l_FID, conn);
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_FID);
				if (l_RefData != null)
					l_GL = l_RefData.getGLCode();

				// Dr BackAccount
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
				pstmt = l_Conn.prepareStatement(l_Query);
				index = 1;
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				pstmt.setDouble(index++, pTransferData.getServiceCharges());
				pstmt.setString(index++, pTransferData.getFromAccNumber());
				pstmt.setString(index++, pTransferData.getFromBranchCode());
				pstmt.setString(index++, pTransferData.getFromCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, drCCYRate);
				pstmt.setDouble(index++, pTransferData.getServiceCharges() * drCCYRate);
				pstmt.setString(index++,
						l_GL + ", Loan Repayment Service Charges Debit, " + GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, mLoanRepaymentDr);
				pstmt.setString(index++, fromproduct.getCashInHandGL());
				pstmt.setString(index++, fromproduct.getProductGL());
				pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.executeUpdate();

				// Cr GL
				l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
						+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
				pstmt = l_Conn.prepareStatement(l_Query);
				index = 1;
				pstmt.setString(index++, pTransferData.getUserID());
				pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					pstmt.setDouble(index++, pTransferData.getServiceCharges() * (drCCYRate * 1000) / 1000);
				else
					pstmt.setDouble(index++,
							(pTransferData.getServiceCharges() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

				pstmt.setString(index++, l_GL);
				pstmt.setString(index++, pTransferData.getToBranchCode());
				pstmt.setString(index++, pTransferData.getToCCY());
				pstmt.setString(index++, pTransferData.getTransactionTime());
				pstmt.setString(index++, pTransferData.getTransactionDate());
				pstmt.setString(index++, pTransferData.getEffectiveDate());
				pstmt.setString(index++, pTransferData.getReferenceNo());
				pstmt.setString(index++, pTransferData.getRemark());
				pstmt.setDouble(index++, crCCYRate);
				pstmt.setDouble(index++, pTransferData.getServiceCharges() * drCCYRate);
				pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Repayment Service Charges Credit"
						+ ", " + GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
				pstmt.setInt(index++, mLoanRepaymentCr);
				pstmt.setString(index++, toproduct.getCashInHandGL());
				pstmt.setString(index++, toproduct.getProductGL());
				pstmt.setString(index++, "");
				pstmt.setInt(index++, 1);
				pstmt.setDouble(index++, l_ToAccountData.getCurrentBalance());
				pstmt.setString(index++, pTransferData.getTransdescription());
				pstmt.executeUpdate();
			}
		}
		if (!l_DataResult.getStatus()) {
			l_DataResult.setCode("0014");
		}

		return l_DataResult;
	}

	public LoanRepaymentRes postLoanScheduleRepayment(TransferData pTransferData, LAODFScheduleData aReq,
			Connection l_Conn) throws Exception {
		LoanRepaymentRes l_SMSReturnData = new LoanRepaymentRes();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "",tomorrowDate="";
		m_AutoPrintTransRef = 0;
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";
		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowDate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowDate, l_Conn)[0];
			}else {
			l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}

		l_SMSReturnData.setTransdate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		String tmp_Transpreapre = "Tmp_transprepare" + pTransferData.getFromAccNumber() + ""
				+ GeneralUtility.getCurrentDateYYYYMMDDHHMMSSNoSpace();

		createTmpTableForTransaction(tmp_Transpreapre, l_Conn);

		l_DataResult = prepareAccountTransactionLoanScheduleRepayment(pTransferData, aReq, tmp_Transpreapre,
				l_TableName, l_TableName1, aReq.getLoantype(), l_Conn);

		try {
			// Thread Testing
			if (l_DataResult.getStatus()) {
				l_SMSReturnData.setRetcode("300");
				l_SMSReturnData.setBankrefnumber(String.valueOf(l_DataResult.getTransRef()));
				l_SMSReturnData.setTransdate(GeneralUtil.getDate());
				l_SMSReturnData.setEffectivedate(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(l_EffectiveDate));
				l_SMSReturnData.setRetmessage("Posted successfully");
				// Update Auto link Transfef
			} else {
				l_SMSReturnData.setRetcode("210");
				l_SMSReturnData.setRetmessage(l_DataResult.getDescription());
			}
			droptable(tmp_Transpreapre, l_Conn);

		} catch (Exception e) {
			droptable(tmp_Transpreapre, l_Conn);
			e.printStackTrace();
			l_SMSReturnData.setRetcode("210");
			l_SMSReturnData.setRetmessage("Transaction Failed." + e.getMessage());
		}

		return l_SMSReturnData;
	}

	public DataResult prepareAccountTransactionLoanScheduleRepayment(TransferData pTransferData, LAODFScheduleData aReq,
			String tmp_Transpreapre, String l_TableName, String l_TableName1, int loanType, Connection l_Conn)
			throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String pByForceCheque = "";
		String fkey = "", tkey = "";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0;
		double crCCYRate = 0;
		int lUncommTransNo = DBTransactionMgr.nextValue();
		double totalTransAmt = pTransferData.getAmount() + pTransferData.getInterest()
				+ pTransferData.getServiceCharges();
		l_DataResult = l_AccDAO.getTransAccount(pTransferData.getFromAccNumber(), "Dr", totalTransAmt, l_Conn);
		if (l_DataResult.getStatus()) {
			l_FromAccountData = l_AccDAO.getAccountsBean();
		}

		if (l_DataResult.getStatus()) {
			if (l_AccDAO.getAccount(pTransferData.getToAccNumber(), l_Conn)) {
				l_ToAccountData = l_AccDAO.getAccountsBean();
			} else {
				l_DataResult.setStatus(false);
			}
		}

		if (l_DataResult.getStatus()) {
			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				fkey = l_FromAccountData.getProduct();
				tkey = l_ToAccountData.getProduct();
			}
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

			// For Checking Transaction
			// Check Multiple Of
			double f_MultipleOf = fromproduct.getMultipleOf();
			double t_MultipleOf = toproduct.getMultipleOf();
			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if ((int) (totalTransAmt % f_MultipleOf) != 0) {
				l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
				l_DataResult.setStatus(false);
			} else {
				// if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = fromproduct.getMinWithdrawal();
				if (totalTransAmt < l_MinWithdrawal) {
					l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
					l_DataResult.setStatus(false);
				}
			}
			if (l_DataResult.getStatus()) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus()) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinOpeningBalance(), 0,
					l_Conn);
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (totalTransAmt > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (totalTransAmt != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}
		}
		if (l_DataResult.getStatus()) {

			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
			pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
			pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
			pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());

			String fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
			String tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();

			drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
		}

		PreparedStatement pstmt = null;
		String l_Query = "";
		boolean interestStatus = false;

		FunctionID fun = new FunctionID();
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		String l_FID = "", l_GL = "", l_MEBIntGL = "", l_AGL = "", l_PGL = "";
		fun.setBranchCode(pTransferData.getFromBranchCode());
		fun.setCurrencyCode(pTransferData.getFromCCY());
		fun.setType(Enum.FunctionID.LAI);
		fun.setLaType(loanType);
		l_FID = fun.getFunctionID();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_FID);
		if (l_RefData != null)
			l_GL = l_RefData.getGLCode();

		fun.setType(Enum.FunctionID.LAIM);
		l_FID = fun.getFunctionID();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_FID);
		if (l_RefData != null)
			l_MEBIntGL = l_RefData.getGLCode();

		fun.setType(Enum.FunctionID.LAA);
		l_FID = fun.getFunctionID();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_FID);
		if (l_RefData != null)
			l_AGL = l_RefData.getGLCode();

		fun.setType(Enum.FunctionID.LASP);
		l_FID = fun.getFunctionID();
		l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get(l_FID);
		if (l_RefData != null)
			l_PGL = l_RefData.getGLCode();

		int sys_LAR_N6 = SharedLogic.getSystemData().getpSystemSettingDataList().get("LAR").getN6();
		String sys_BNK_T4 = SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getT4();
		ProductInteRateDAO l_ProdIntDao = new ProductInteRateDAO();
		LAODFScheduleDAO l_DAO = new LAODFScheduleDAO();
		ArrayList<TempData> lstIntData = new ArrayList<TempData>();
		lstIntData = l_ProdIntDao.getAllLoanRateByAccNo(StrUtil.formatDDMMYYYY2MIT(GeneralUtility.getCurrentDate()),
				aReq.getAccnumber(), l_Conn);

		double[] arrAccruedAmt = new double[2];
		double[] arrMebaccruedAmt = new double[2];
		double accruedAmt = 0, mebaccruedAmt = 0;
		if (sys_LAR_N6 == 0 && lstIntData.size() > 0 & lstIntData.get(0).getN2() > 0)
			accruedAmt = l_DAO.getLAODAccruedAmt(aReq, l_Conn);
		else
			arrAccruedAmt = l_DAO.getLAODAccruedAmtForN6("Interest", "B.N7", aReq, l_Conn);

		if (sys_LAR_N6 == 1 && lstIntData.size() > 0 & lstIntData.get(0).getN27() > 0)
			arrMebaccruedAmt = l_DAO.getLAODAccruedAmtForN6("N1", "B.N8", aReq, l_Conn);

		accruedAmt = arrAccruedAmt[0];
		mebaccruedAmt = arrMebaccruedAmt[0];

		ArrayList<LAODFScheduleData> arr = new ArrayList<LAODFScheduleData>();
		arr = l_DAO.getLAODFSchedule(aReq.getAccnumber(), aReq.getTermno(), mebaccruedAmt, accruedAmt, l_Conn);

		AccountData l_LAAccData = new AccountData();
		AccountData l_CAAccData = new AccountData();
		GLDAO l_GLdao = new GLDAO();
		String drGLDesc = "", crGLDesc = "";
		if (sys_BNK_T4.equals("microfinance")) {
			l_LAAccData = l_ToAccountData;

			if (l_AccDAO.getAccount(aReq.getBackaccnumber(), l_Conn))
				l_CAAccData = l_AccDAO.getAccountsBean();

			if (l_GLdao.getGL(l_CAAccData.getProductGL(), l_Conn))
				drGLDesc = l_GLdao.getGLData().getDescription();
			if (l_GLdao.getGL(l_LAAccData.getProductGL(), l_Conn))
				crGLDesc = l_GLdao.getGLData().getDescription();
		}

		double intamt = 0;
		if (sys_LAR_N6 != 1)
			intamt = Double.parseDouble(aReq.getInterestamount()) - accruedAmt;
		double totalAccruedAmt = accruedAmt;
		double totalMebaccruedAmt = mebaccruedAmt;
		double mebintamt = Double.parseDouble(aReq.getMebinterestamount()) - mebaccruedAmt;

		double paymentAmt = 0;

		boolean chkAmt = true;

		if (sys_LAR_N6 == 1 && lstIntData.size() > 0 & lstIntData.get(0).getN27() > 0)
			chkAmt = l_DAO.checkInterestMEB(aReq, mebaccruedAmt, l_Conn);

		String remark = "";
		if (sys_BNK_T4.equals("microfinance"))
			remark = "Being Amount of transfer from " + drGLDesc + " to " + crGLDesc
					+ " for link transaction on dated (" + GeneralUtility.getCurrentDate() + "). ";
		else
			remark = "Loan Schedule Posting";
		int index = 1;
		if (l_DataResult.getStatus()) {

			if (chkAmt) {
				if ((sys_LAR_N6 == 0) || Double.parseDouble(aReq.getPrincipleamount()) + accruedAmt
						+ Double.parseDouble(aReq.getPenaltyamount()) + mebaccruedAmt > 0) {

					// Dr From Customer
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES( 'Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ "'19000101',1,?,?,?,?,?,?,?,?,?,? ) ";
					index = 1;
					pstmt = l_Conn.prepareStatement(l_Query);
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					double tramount = 0;
					if (sys_LAR_N6 == 1)
						tramount = Double.parseDouble(aReq.getPrincipleamount()) + accruedAmt
								+ Double.parseDouble(aReq.getPenaltyamount()) + mebaccruedAmt;
					else
						tramount = Double.parseDouble(aReq.getPrincipleamount()) + intamt
								+ Double.parseDouble(aReq.getPenaltyamount()) + mebintamt;
					pstmt.setDouble(index++, tramount);
					String fromacc = "";
					if (sys_BNK_T4.equals("microfinance"))
						fromacc = l_CAAccData.getProductGL();
					else
						fromacc = pTransferData.getFromAccNumber();
					pstmt.setString(index++, fromacc);
					pstmt.setString(index++, pTransferData.getFromBranchCode());
					pstmt.setString(index++, pTransferData.getFromCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					if (sys_BNK_T4.equals("microfinance"))
						pstmt.setString(index++, pTransferData.getFromAccNumber());
					else
						pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, drCCYRate);
					pstmt.setDouble(index++, tramount * drCCYRate);
					pstmt.setString(index++, pTransferData.getToAccNumber() + ", Loan Schedule Repayment Debit, "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentDr);
					pstmt.setString(index++, fromproduct.getCashInHandGL());
					pstmt.setString(index++, fromproduct.getProductGL());
					if (!sys_BNK_T4.equals("microfinance"))
						pstmt.setString(index++, fromproduct.hasCheque() ? pByForceCheque : "");
					else
						pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, l_FromAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}

				if (Double.parseDouble(aReq.getPrincipleamount()) > 0) {
					// Cr To Customer
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++,
								Double.parseDouble(aReq.getPrincipleamount()) * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (Double.parseDouble(aReq.getPrincipleamount()) * drCCYRate / crCCYRate)
								* (drCCYRate * 1000) / 1000);

					String toacc = "";
					if (sys_BNK_T4.equals("microfinance"))
						toacc = l_LAAccData.getProductGL();
					else
						toacc = pTransferData.getToAccNumber();
					pstmt.setString(index++, toacc);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					if (sys_BNK_T4.equals("microfinance"))
						pstmt.setString(index++, pTransferData.getFromAccNumber());
					else
						pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, Double.parseDouble(aReq.getPrincipleamount()) * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Principle Credit" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++,
							sys_BNK_T4.equals("microfinance") ? 0 : l_ToAccountData.getCurrentBalance());
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}

				if (accruedAmt > 0) {

					// Cr To GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, accruedAmt * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (accruedAmt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, sys_LAR_N6 == 0 ? l_AGL : l_GL);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, accruedAmt * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Accrued Interest Credit" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}
				if (mebaccruedAmt > 0) {

					// Cr To GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, mebaccruedAmt * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (mebaccruedAmt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_MEBIntGL);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, mebaccruedAmt * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Accrued Interest Credit" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}

				if (intamt > 0 && sys_LAR_N6 == 0) {
					// Cr To GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, intamt * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (intamt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_GL);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, intamt * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Interest Credit" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}
				if (mebintamt > 0) {
					// Cr To GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++, mebintamt * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (mebintamt * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_MEBIntGL);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, mebintamt * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan MEB Interest Credit" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}

				if (Double.parseDouble(aReq.getPenaltyamount()) > 0) {
					// Cr To GL
					l_Query = "INSERT INTO " + tmp_Transpreapre + " VALUES ('Mobile',?,?,?,?,?,?,?,?,?,?,?,"
							+ " '19000101',1,?,?,?,?,?,?,?,?,?,? )";
					pstmt = l_Conn.prepareStatement(l_Query);
					index = 1;
					pstmt.setString(index++, pTransferData.getUserID());
					pstmt.setString(index++, SharedLogic.getSystemData().getpAuthorizerID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						pstmt.setDouble(index++,
								Double.parseDouble(aReq.getPenaltyamount()) * (drCCYRate * 1000) / 1000);
					else
						pstmt.setDouble(index++, (Double.parseDouble(aReq.getPenaltyamount()) * drCCYRate / crCCYRate)
								* (drCCYRate * 1000) / 1000);

					pstmt.setString(index++, l_PGL);
					pstmt.setString(index++, pTransferData.getToBranchCode());
					pstmt.setString(index++, pTransferData.getToCCY());
					pstmt.setString(index++, pTransferData.getTransactionTime());
					pstmt.setString(index++, pTransferData.getTransactionDate());
					pstmt.setString(index++, pTransferData.getEffectiveDate());
					pstmt.setString(index++, pTransferData.getReferenceNo());
					pstmt.setString(index++, remark);
					pstmt.setDouble(index++, crCCYRate);
					pstmt.setDouble(index++, Double.parseDouble(aReq.getPenaltyamount()) * drCCYRate);
					pstmt.setString(index++, pTransferData.getFromAccNumber() + ", Loan Penalty Credit" + ", "
							+ GeneralUtility.getDateTimeDDMMYYYYHHMMSS());
					pstmt.setInt(index++, mLoanRepaymentCr);
					pstmt.setString(index++, toproduct.getCashInHandGL());
					pstmt.setString(index++, toproduct.getProductGL());
					pstmt.setString(index++, "");
					pstmt.setInt(index++, 1);
					pstmt.setDouble(index++, 0);
					pstmt.setString(index++, pTransferData.getTransdescription());
					pstmt.executeUpdate();
				}
			} else {
				l_DataResult.setStatus(false);
				l_DataResult.setDescription("Loan Schedule is not updated. Please Call Loan Account Data");
			}
		}
		if (l_DataResult.getStatus()) {
			int transref = 0;

			AccountDao lAccDAO = new AccountDao();
			// update Balance
			l_DataResult = lAccDAO.updateAccountBalanceCurrency(tmp_Transpreapre, l_TableName, l_TableName1, l_Conn);
			if (l_DataResult.getStatus()) {
				l_DataResult = saveTmpTabletoAccountTransaction(tmp_Transpreapre, l_Conn);
			}

			int[] arrPaidTerm;

			if (l_DataResult.getStatus()) {
//							transref = l_DataResult.getTransRef();  ynw test
				l_DataResult.setStatus(false);

				arrPaidTerm = new int[arr.size()];
				double l_TotInstllRepay = Double.parseDouble(aReq.getPrincipleamount()), l_TotIntRepay = intamt,
						l_TotMebIntRepay = mebintamt, l_TotPenaltyRepay = Double.parseDouble(aReq.getPenaltyamount()),
						l_prevpenatlyAmt = 0;

				if (sys_LAR_N6 == 1) {
					l_TotIntRepay = accruedAmt;
					l_TotMebIntRepay = mebaccruedAmt;
				} else {
					l_TotIntRepay = intamt + accruedAmt;
					l_TotMebIntRepay = mebintamt + mebaccruedAmt;
				}
				for (int i = 0; i < arr.size(); i++) {
					if (arr.get(i).getStatus() == 0 || arr.get(i).getStatus() == 2) {
						if (l_TotInstllRepay > 0) {
							l_TotInstllRepay = StrUtil
									.RoundHALFUP(l_TotInstllRepay - Double.parseDouble(arr.get(i).getPrinoutamount()));
							if (l_TotInstllRepay >= 0) {
								arr.get(i).setPrinoutamount("0");
							} else {
								arr.get(i).setPrinoutamount(
										StrUtil.formatNumber(Double.parseDouble(arr.get(i).getPrinoutamount())
												- Double.parseDouble(aReq.getPrincipleamount())));
							}
							arr.get(i).setPrincipleamount(aReq.getPrincipleamount());
						} else
							arr.get(i).setPrincipleamount("0");
						if (l_TotIntRepay > 0) {
							if (sys_LAR_N6 == 1)
								l_TotIntRepay = StrUtil.RoundHALFUP(l_TotIntRepay - totalAccruedAmt);
							else
								l_TotIntRepay = StrUtil
										.RoundHALFUP(l_TotIntRepay - Double.parseDouble(arr.get(i).getIntoutamount()));
							if (l_TotIntRepay >= 0) {
								arr.get(i).setIntoutamount("0");
							} else {
								if (sys_LAR_N6 == 1) {
									arr.get(i).setIntoutamount(StrUtil.formatNumber(totalAccruedAmt - accruedAmt));
								} else
									arr.get(i).setIntoutamount(StrUtil.formatNumber(
											Double.parseDouble(arr.get(i).getIntoutamount()) - (intamt + accruedAmt)));
							}
							arr.get(i).setInterestamount(StrUtil.formatNumber(intamt + accruedAmt));
						} else
							arr.get(i).setInterestamount("0");
						if (l_TotMebIntRepay > 0) {
							if (sys_LAR_N6 == 1)
								l_TotMebIntRepay = StrUtil.RoundHALFUP(l_TotMebIntRepay - totalMebaccruedAmt);
							else
								l_TotMebIntRepay = StrUtil.RoundHALFUP(
										l_TotMebIntRepay - Double.parseDouble(arr.get(i).getMebintoutamount()));
							if (l_TotIntRepay >= 0) {
								arr.get(i).setMebintoutamount("0");
							} else {
								if (sys_LAR_N6 == 1)
									arr.get(i).setMebintoutamount(
											StrUtil.formatNumber(totalMebaccruedAmt - mebaccruedAmt));
								else
									arr.get(i).setMebintoutamount(
											StrUtil.formatNumber(Double.parseDouble(arr.get(i).getMebintoutamount())
													- (mebintamt + mebaccruedAmt)));
							}
							arr.get(i).setMebinterestamount(StrUtil.formatNumber(mebintamt + mebaccruedAmt));
						} else
							arr.get(i).setMebinterestamount("0");
						if (l_TotPenaltyRepay > 0) {
							l_TotPenaltyRepay = StrUtil.RoundHALFUP(
									l_TotPenaltyRepay - Double.parseDouble(arr.get(i).getPenaltyoutamount()));
							if (l_TotPenaltyRepay >= 0) {
								arr.get(i).setPenaltyamount("0");
							} else {
								arr.get(i).setPenaltyoutamount(
										StrUtil.formatNumber(Double.parseDouble(arr.get(i).getPenaltyoutamount())
												- Double.parseDouble(aReq.getPenaltyamount())));
							}
							arr.get(i).setPenaltyamount(aReq.getPenaltyamount());
						} else
							arr.get(i).setPenaltyamount("0");
						if (i == arr.size() - 1) {
							l_prevpenatlyAmt = l_DAO.getPenaltyAmount(arr.get(i), aReq.getBatchno(),
									Double.parseDouble(arr.get(i).getPenaltyamount()), l_Conn);
							arr.get(i).setPenaltyamount(StrUtil.formatNumber(l_prevpenatlyAmt));
						}
						l_DataResult = l_DAO.saveHPRepaymentHistory(arr.get(i), transref,
								Double.parseDouble(arr.get(i).getPenaltyamount())
										- Double.parseDouble(arr.get(i).getPenaltyoutamount()),
								pTransferData.getEffectiveDate(), pTransferData.getFromBranchCode(), l_Conn);
						if (l_DataResult.getStatus())
							l_DataResult = l_DAO.updateLAODFSchedule(arr.get(i), transref,
									pTransferData.getEffectiveDate(), accruedAmt, mebaccruedAmt, l_Conn);
						else {
							l_DataResult.setDescription("Save History Fail!");
							break;
						}

						arrPaidTerm[i] = 0;
					} else {
						arrPaidTerm[i] = arr.get(i).getTermno();
					}
				}
			}

			if (l_DataResult.getStatus()) {
				l_DataResult.setTransRef(transref);
			}

		} else {
			l_DataResult.setCode("0014");
		}

		return l_DataResult;
	}

	public TransferDataResult prepareAccountTransactionForInternalTransferOLTPUpdateQuery(TransferData pTransferData,
			String table, String table1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		TransferDataResult transferDataResult = new TransferDataResult();
		AccountDao l_AccDAO = new AccountDao();
		TransferData prepareTransferData = new TransferData();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		boolean isSameBranch = false;
		String pByForceCheque = "", fkey = "", tkey = "", description = "",ibtdescription ="";
		TransferData transferData = new TransferData();
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0;
		double crCCYRate = 0;
		ArrayList<AccountData> accountDataList = new ArrayList<AccountData>();
		ArrayList<Integer> m_AutoLinkTransRef = new ArrayList<Integer>();
		int drTransType = 0, crTransType = 0;

		l_DataResult = l_AccDAO.getTransAccount_TransferOLTPUpdateQuery(pTransferData.getFromAccNumber(),
				pTransferData.getToAccNumber(), l_Conn);
		if (l_DataResult.getStatus()) {
			accountDataList = l_DataResult.getListAccountData();
			for (int i = 0; i < accountDataList.size(); i++) {
				if (pTransferData.getFromAccNumber().equals(accountDataList.get(i).getAccountNumber())) {
					l_FromAccountData = accountDataList.get(i);
				} else {
					l_ToAccountData = accountDataList.get(i);
				}
			}
		}

		if (l_DataResult.getStatus()) {
			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				fkey = l_FromAccountData.getProduct();
				tkey = l_ToAccountData.getProduct();
			}
			fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
			toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);

			// For Checking Transaction
			// Check Multiple Of
			double f_MultipleOf = fromproduct.getMultipleOf();
			double t_MultipleOf = toproduct.getMultipleOf();
			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if ((int) (pTransferData.getAmount() % f_MultipleOf) != 0) {
				l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
				l_DataResult.setStatus(false);
			} else {
				// if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = fromproduct.getMinWithdrawal();
				if (pTransferData.getAmount() < l_MinWithdrawal) {
					l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
					l_DataResult.setStatus(false);
				}
			}
			if (l_DataResult.getStatus()) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus()) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinBalance(), 0,
					l_Conn);
			transferData.setAccountStatus(l_FromAccountData.getStatus());
			transferData.setAccountBarAmount(l_FromAccountData.getAvailableBalance());
			transferData.setProductMinOpeningBalance(fromproduct.getMinOpeningBalance());
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (pTransferData.getAmount() > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (pTransferData.getAmount() != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}
			if (!l_DataResult.getStatus()) {
				l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrencyUpdate(pTransferData,
						l_FromAccountData, l_Available, pTransferData.getAmount(), table, table1, l_Conn);
				if (l_DataResult.getStatus()) {
//							m_AutoPrintTransRef = l_DataResult.getTransactionNumber();	
					m_AutoLinkTransRef.add(l_DataResult.getTransactionNumber());
					if (l_DataResult.getTransRef() != 0)
						m_AutoLinkTransRef.add(l_DataResult.getTransRef());
					transferDataResult.setAutoLinkTransRef(m_AutoLinkTransRef);
				}

			}
		}
		if (l_DataResult.getStatus()) {

			pByForceCheque = SharedLogic.getSystemData().getpByForceCheque();
			if (l_FromAccountData.getBranchCode().equalsIgnoreCase(l_ToAccountData.getBranchCode())) {
				isSameBranch = true;
			}

			pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
			pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
			pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
			pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());
			pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());

			String fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
			String tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();

			drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);
		}

		if (pTransferData.getDebitIBTKey().equalsIgnoreCase("1")) {
			drTransType = mOwnDrTransType;
			crTransType = mOwnCrTransType;
			description = "Own Account Transfer, ";
			ibtdescription = "IBT, Own Account Transfer, ";
		} else if (pTransferData.getDebitIBTKey().equalsIgnoreCase("4")) {
			drTransType = mDonationDrTransType;
			crTransType = mDonationCrTransType;
			description = "Donation" + pTransferData.getRefcode() + ", ";
			ibtdescription = "IBT, Donation" + pTransferData.getRefcode() + ", ";
		} else if (pTransferData.getDebitIBTKey().equalsIgnoreCase("5")) {
			drTransType = mGiftCardDrTransType;
			crTransType = mGiftCardCrTransType;
			description = "Gift Card" + pTransferData.getRefcode() + ", ";
			ibtdescription = "IBT, Gift Card" + pTransferData.getRefcode() + ", ";
		} else {
			drTransType = mTrDrTransType;
			crTransType = mTrCrTransType;
			description = "Other Account Transfer, ";
			ibtdescription = "IBT, Other Account Transfer, ";
		}

		
		String l_IBTGLFrom = "",l_IBTGLTo = "",comGl = "";
		Double totalamount = 0.0;
		ReferenceAccountsData l_RefData = new ReferenceAccountsData();
		if (pTransferData.getTransactionFee() > 0) {
			totalamount = pTransferData.getAmount()+pTransferData.getTransactionFee();
		}else {
			totalamount = pTransferData.getAmount();
		}
		
		if (l_DataResult.getStatus()) {
			ArrayList<TransferData> transferlist = new ArrayList<TransferData>();
			if (isSameBranch) {
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData.getUserID());
				prepareTransferData.setAmount(totalamount);
				prepareTransferData.setFromAccNumber(pTransferData.getFromAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData.getFromCCY());
				prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
				prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
				prepareTransferData.setRemark(pTransferData.getRemark());
				prepareTransferData.setBasecurrate(drCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(totalamount * drCCYRate));
				prepareTransferData.setPostDescription(description + pTransferData.getToAccNumber());
				prepareTransferData.setIbtTransType(drTransType);
				prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL(fromproduct.getProductGL());
				prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
				prepareTransferData.setPreviousDate(l_FromAccountData.getLastUpdate());
				prepareTransferData.setPrevBalance(l_FromAccountData.getCurrentBalance());
				prepareTransferData.setTransdescription(pTransferData.getTransdescription());
				transferlist.add(prepareTransferData);

				//commission
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRCOM"
						+ pTransferData.getFromCCY() + pTransferData.getFromBranchCode() + fromproduct.getProductCode());
				if (l_RefData != null)
					comGl = l_RefData.getGLCode();
				
				if (pTransferData.getTransactionFee() > 0) {
					if (!comGl.equals("")) {
						// credit Commission GL
						prepareTransferData = new TransferData();
						prepareTransferData.setUserID(pTransferData.getUserID());
						if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
							prepareTransferData.setAmount(pTransferData.getTransactionFee() * (drCCYRate * 1000) / 1000);
						else
							prepareTransferData.setAmount(
									(pTransferData.getTransactionFee() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
						prepareTransferData.setFromAccNumber(comGl);
						prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
						prepareTransferData.setFromCCY(pTransferData.getFromCCY());
						prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
						prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
						prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
						prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
						prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
						prepareTransferData.setRemark(pTransferData.getRemark());
						prepareTransferData.setBasecurrate(drCCYRate);
						prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getTransactionFee() * drCCYRate));
						prepareTransferData.setPostDescription(description + pTransferData.getToAccNumber());
						prepareTransferData.setIbtTransType(crTransType);
						prepareTransferData.setTransdescription(pTransferData.getTransdescription());
						prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
						transferlist.add(prepareTransferData);
					} else {
						l_DataResult.setStatus(false);
						l_DataResult.setCode("0014");
						l_DataResult.setDescription("Invalid GL Account!");
					}
				}
				
				//cr to account
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData.getUserID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					prepareTransferData.setAmount(pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
				else
					prepareTransferData.setAmount((pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(pTransferData.getToAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
				prepareTransferData.setFromCCY(pTransferData.getToCCY());
				prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
				prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
				prepareTransferData.setRemark(pTransferData.getRemark());
				prepareTransferData.setBasecurrate(crCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
				prepareTransferData.setPostDescription(description + pTransferData.getFromAccNumber());
				prepareTransferData.setIbtTransType(crTransType);
				prepareTransferData.setCashInHandGL(toproduct.getCashInHandGL());
				prepareTransferData.setProductGL(toproduct.getProductGL());
				prepareTransferData.setPreviousDate(l_ToAccountData.getLastUpdate());
				prepareTransferData.setPrevBalance(l_ToAccountData.getCurrentBalance());
				prepareTransferData.setTransdescription(pTransferData.getTransdescription());
				transferlist.add(prepareTransferData);
				transferDataResult.setTransferlist(transferlist);
				transferDataResult.setStatus(true);
			} else {
				// different branch
				
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList()
						.get("MTRCR" + pTransferData.getFromCCY() + pTransferData.getFromBranchCode()
								+ fromproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLFrom = l_RefData.getGLCode();
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRDR"
						+ pTransferData.getToCCY() + pTransferData.getToBranchCode() + toproduct.getProductCode());
				if (l_RefData != null)
					l_IBTGLTo = l_RefData.getGLCode();
				l_RefData = SharedLogic.getSystemData().getpReferenceAccCodeList().get("MTRCOM"
						+ pTransferData.getFromCCY() + pTransferData.getFromBranchCode() + fromproduct.getProductCode());
				if (l_RefData != null)
					comGl = l_RefData.getGLCode();

				if (!(l_IBTGLFrom.equals("") || l_IBTGLTo.equals(""))) {
					// Dr From Customer
					prepareTransferData.setUserID(pTransferData.getUserID());
					prepareTransferData.setAmount(totalamount);
					prepareTransferData.setFromAccNumber(pTransferData.getFromAccNumber());
					prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getFromCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(totalamount * drCCYRate);
					prepareTransferData.setPostDescription(description + pTransferData.getToAccNumber());
					prepareTransferData.setIbtTransType(drTransType);
					prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
					prepareTransferData.setProductGL(fromproduct.getProductGL());
					prepareTransferData.setpByForceCheque(fromproduct.hasCheque() ? pByForceCheque : "");
					prepareTransferData.setPreviousDate(l_FromAccountData.getLastUpdate());
					prepareTransferData.setPrevBalance(l_FromAccountData.getCurrentBalance());
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					transferlist.add(prepareTransferData);

					// Cr IBT GL
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(pTransferData.getAmount() * (drCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount(
								(pTransferData.getAmount() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(l_IBTGLFrom);
					prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getFromCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(drCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
					prepareTransferData.setPostDescription(ibtdescription + pTransferData.getToAccNumber());
					prepareTransferData.setIbtTransType(crTransType);
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
					transferlist.add(prepareTransferData);
					
					//commission
					if (pTransferData.getTransactionFee() > 0) {
						if (!comGl.equals("")) {
							// credit Commission GL
							prepareTransferData = new TransferData();
							prepareTransferData.setUserID(pTransferData.getUserID());
							if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
								prepareTransferData.setAmount(pTransferData.getTransactionFee() * (drCCYRate * 1000) / 1000);
							else
								prepareTransferData.setAmount(
										(pTransferData.getTransactionFee() * drCCYRate / crCCYRate) * (drCCYRate * 1000) / 1000);
							prepareTransferData.setFromAccNumber(comGl);
							prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
							prepareTransferData.setFromCCY(pTransferData.getFromCCY());
							prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
							prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
							prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
							prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
							prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
							prepareTransferData.setRemark(pTransferData.getRemark());
							prepareTransferData.setBasecurrate(drCCYRate);
							prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getTransactionFee() * drCCYRate));
							prepareTransferData.setPostDescription(description + pTransferData.getToAccNumber());
							prepareTransferData.setIbtTransType(crTransType);
							prepareTransferData.setTransdescription(pTransferData.getTransdescription());
							prepareTransferData.setCashInHandGL(fromproduct.getCashInHandGL());
							transferlist.add(prepareTransferData);
						} else {
							l_DataResult.setStatus(false);
							l_DataResult.setCode("0014");
							l_DataResult.setDescription("Invalid GL Account!");
						}
					}
					
					// Dr IBT GL
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());

					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(pTransferData.getAmount() * (crCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount(
								(pTransferData.getAmount() * drCCYRate / crCCYRate) * (crCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(l_IBTGLTo);
					prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getToCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(crCCYRate);
					prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * drCCYRate));
					prepareTransferData.setPostDescription(ibtdescription + pTransferData.getFromAccNumber());
					prepareTransferData.setIbtTransType(drTransType);
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					prepareTransferData.setCashInHandGL(toproduct.getCashInHandGL());
					transferlist.add(prepareTransferData);

					// Cr To Account
					prepareTransferData = new TransferData();
					prepareTransferData.setUserID(pTransferData.getUserID());
					if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
						prepareTransferData.setAmount(pTransferData.getAmount() * (crCCYRate * 1000) / 1000);
					else
						prepareTransferData.setAmount(
								(pTransferData.getAmount() * drCCYRate / crCCYRate) * (crCCYRate * 1000) / 1000);
					prepareTransferData.setFromAccNumber(pTransferData.getToAccNumber());
					prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
					prepareTransferData.setFromCCY(pTransferData.getToCCY());
					prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
					prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
					prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
					prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
					prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
					prepareTransferData.setRemark(pTransferData.getRemark());
					prepareTransferData.setBasecurrate(crCCYRate);
					prepareTransferData.setBaseAmount(pTransferData.getAmount() * crCCYRate);
					prepareTransferData.setPostDescription(description + pTransferData.getFromAccNumber());
					prepareTransferData.setIbtTransType(crTransType);
					prepareTransferData.setCashInHandGL(toproduct.getCashInHandGL());
					prepareTransferData.setProductGL(toproduct.getProductGL());
					prepareTransferData.setPreviousDate(l_ToAccountData.getLastUpdate());
					prepareTransferData.setPrevBalance(l_ToAccountData.getCurrentBalance());
					prepareTransferData.setTransdescription(pTransferData.getTransdescription());
					transferlist.add(prepareTransferData);
					transferDataResult.setTransferlist(transferlist);
					transferDataResult.setStatus(true);
				} else {

					l_DataResult.setStatus(false);
					l_DataResult.setCode("0014");
					l_DataResult.setDescription("Invalid GL Account!");
				}
			}
		}
		if (!l_DataResult.getStatus()) {
			transferDataResult.setStatus(false);
			transferDataResult.setCode("0014");
			transferDataResult.setDescription(l_DataResult.getDescription());
		}

		return transferDataResult;
	}

	public static BankHolidayDataRes checkBankHoliday(String pDate,
			Connection conn) throws SQLException {
		BankHolidayDataRes result = new BankHolidayDataRes();
		BankHolidayDAO pDAO  = new BankHolidayDAO();
		LastDatesDAO pLastDateDAO = new LastDatesDAO();
		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));
		Calendar cal = Calendar.getInstance();
		month--; // In Java Calendar, month is starting zero.
		cal.set(year, month, day);

		String effectiveDate = pDate;
		// Checking is Runned EOD
		result.setRetcode("300");
		result.setRetmessage("Success.");
		if (pLastDateDAO.isRunnedEOD(pDate, conn)) {
			result.setDescription(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(effectiveDate)+" is EOD ");
		} else if (StrUtil.isWeekEnd(pDate)) {
			result.setDescription(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(effectiveDate)+" is WeekEnd ");
		} else if (pDAO.getBankHolidayCheck(pDate, conn)) { // Checking
															// BankHoliday
			result.setDescription(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(effectiveDate)+" is Holiday ");
		} else {
			result.setDescription(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(effectiveDate)+" is Weekday ");
		}
		return result;
	}
	
	public SMSReturnData doOtherBankReversal(TransferData pTransferData, Connection l_Conn) throws Exception {
		SMSReturnData l_SMSReturnData = new SMSReturnData();
		DataResult l_DataResult = new DataResult();
		ArrayList<String> l_ArlLogString = new ArrayList<String>();
		String l_EffectiveDate = "", tomorrowdate = "";
		m_AutoPrintTransRef = 0;
		boolean isLink = false;

		String l_TodayDate = GeneralUtility.getTodayDate();
		String l_TransactionTime = getCurrentDateYYYYMMDDHHMMSS();
		// // by
		// reference
		LastDatesDAO l_LastDao = new LastDatesDAO();
		AccountTransactionDAO accTransDao = new AccountTransactionDAO();
		String l_TableName = "", l_TableName1 = "";

		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4() == 1) {
			boolean isruneod = false;
			if (GeneralUtility.datetimeformatHHMM()) {
				tomorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowdate, l_Conn)[0];
				isruneod = l_LastDao.isRunnedEOD(l_TodayDate, l_Conn);
			} else {
				String[] result = getEffectiveTransDateNew(l_TodayDate, l_Conn);
				l_EffectiveDate = result[0];
				if (result[1].equalsIgnoreCase("EOD")) {
					isruneod = true;
				}
			}
			int comparedate = StrUtil.compareDate(l_EffectiveDate, l_TodayDate);
			if (comparedate > 0) {
				if (isruneod)
					l_TableName1 = "Accounts_Balance";
				l_TableName = "Accounts";
			} else {
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
			if (GeneralUtility.datetimeformatHHMM()) { // check cut off
				tomorrowdate = GeneralUtility.getTomorrowDate();
				l_EffectiveDate = getEffectiveTransDateNew(tomorrowdate, l_Conn)[0];
			} else {
				l_EffectiveDate = getEffectiveTransDateNew(l_TodayDate, l_Conn)[0];
			}
		}

		l_SMSReturnData.setEffectiveDate(l_EffectiveDate);
		pTransferData.setTransactionDate(l_TodayDate);
		pTransferData.setTransactionTime(l_TransactionTime);
		pTransferData.setEffectiveDate(l_EffectiveDate);

		// ================ prepare Account Transaction ========================
		TransferDataResult transferDataResult = new TransferDataResult();
		transferDataResult = prepareOtherBankReversal(pTransferData, l_TableName, l_TableName1, l_Conn);

		try {

			if (transferDataResult.isStatus()) {
				AccountDao lAccDAO = new AccountDao();
				// update Balance
				transferDataResult = lAccDAO.updateAccountBalanceMulti(transferDataResult, l_TableName, l_TableName1,
						l_Conn);
				if (transferDataResult.isStatus()) {
					l_DataResult = saveAccountTransaction(transferDataResult.getTransferlist(), l_Conn);
				} else {
					l_DataResult.setDescription(transferDataResult.getDescription());
				}
				// Thread Testing
				if (l_DataResult.getStatus()) {
					l_SMSReturnData.setStatus(true);
					l_SMSReturnData.setCode("0000");
					l_SMSReturnData.setTransactionNumber(l_DataResult.getTransRef());
					l_SMSReturnData.setDescription("Posted successfully");
					// Update Auto link Transfef
					if (transferDataResult.getAutoLinkTransRef().size() > 0) {
						accTransDao.addAutoLinkTransRefUpdate(transferDataResult.getAutoLinkTransRef(),
								l_DataResult.getTransRef(), l_Conn);
					}
				} else {
					l_SMSReturnData.setStatus(false);
					l_SMSReturnData.setCode("0014");
					if (isLink) {
						l_SMSReturnData.setDescription(
								l_DataResult.getDescription() + " and Already Posting Transaction For Link!");
					} else {
						l_SMSReturnData.setDescription(l_DataResult.getDescription());
					}
				}
			} else {
				l_SMSReturnData.setDescription(transferDataResult.getDescription());
				l_SMSReturnData.setStatus(false);
				l_SMSReturnData.setCode("0014");
				return l_SMSReturnData;
			}

		} catch (Exception e) {

			e.printStackTrace();
			l_SMSReturnData.setStatus(false);
			l_SMSReturnData.setDescription("Transaction Failed." + e.getMessage());
			l_ArlLogString.add("Transaction Roll Back" + l_DataResult.getDescription() + e.getMessage());
			l_SMSReturnData.setErrorCode("06");
		}

		return l_SMSReturnData;
	}
	
	public TransferDataResult prepareOtherBankReversal(TransferData pTransferData, String l_TableName,
			String l_TableName1, Connection l_Conn) throws Exception {
		DataResult l_DataResult = new DataResult();
		AccountDao l_AccDAO = new AccountDao();
		AccountData l_FromAccountData = new AccountData();
		AccountData l_ToAccountData = new AccountData();
		String fkey = "", tkey = "";
		String desc = "Adjustment, Other Bank Transfer, ";
		ProductData fromproduct = new ProductData();
		ProductData toproduct = new ProductData();
		double drCCYRate = 0, crCCYRate = 0;
		boolean isGL = false;
		boolean isSameBranch = false;
		ArrayList<Integer> m_AutoLinkTransRef = new ArrayList<Integer>();
		TransferDataResult transferDataResult = new TransferDataResult();
		TransferData prepareTransferData = new TransferData();
		ArrayList<TransferData> transferlist = new ArrayList<TransferData>();

		l_DataResult = l_AccDAO.getTransAccountCredit(pTransferData.getFromAccNumber(), l_Conn);
		if (l_DataResult.getStatus()) {
			l_ToAccountData = l_AccDAO.getAccountsBean();
		}

		if (l_DataResult.getStatus() && !l_AccDAO.isGL(pTransferData.getToAccNumber(), l_Conn)) {
			l_DataResult = l_AccDAO.getTransAccountDebit(pTransferData.getToAccNumber(), l_Conn);
			if (l_DataResult.getStatus()) {
				l_FromAccountData = l_AccDAO.getAccountsBean();
			}
		} else {
			isGL = true;
			isSameBranch = true;
			pTransferData.setFromCCY(l_ToAccountData.getCurrencyCode());
			pTransferData.setCurrencyCode(l_ToAccountData.getCurrencyCode());
			pTransferData.setFromBranchCode(l_ToAccountData.getBranchCode());
		}

		if (l_DataResult.getStatus()) {

			if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
				if (!isGL) {
					fkey = l_FromAccountData.getProduct() + "" + l_FromAccountData.getType();
				}
				tkey = l_ToAccountData.getProduct() + "" + l_ToAccountData.getType();
			} else {
				if (!isGL) {
					fkey = l_FromAccountData.getProduct();
				}
				tkey = l_ToAccountData.getProduct();
			}
			double f_MultipleOf = 0, t_MultipleOf = 0;
			if (!isGL) {
				fromproduct = SharedLogic.getSystemData().getpProductDataList().get(fkey);
				f_MultipleOf = fromproduct.getMultipleOf();
			}

			toproduct = SharedLogic.getSystemData().getpProductDataList().get(tkey);
			t_MultipleOf = toproduct.getMultipleOf();

			// For Checking Transaction
			// Check Multiple Of

			// if (pTransData.getTransactionType() > 700 && pTransData.getTransactionType()
			// < 999) { // For Debit // Transfer Case
			if (!isGL) {
				if ((int) (pTransferData.getAmount() % f_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + f_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType > 500 && transType != 969) {
					double l_MinWithdrawal = fromproduct.getMinWithdrawal();
					if (pTransferData.getAmount() < l_MinWithdrawal) {
						l_DataResult.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
						l_DataResult.setStatus(false);
					}
				}
			}
			if (l_DataResult.getStatus()) {
				// if (pTransData.getTransactionType() > 200 && pTransData.getTransactionType()
				// <= 500) { // For Credit // Transfer Case
				if ((int) (pTransferData.getAmount() % t_MultipleOf) != 0) {
					l_DataResult.setDescription("Amount not multiple of " + t_MultipleOf);
					l_DataResult.setStatus(false);
				} else {
					// if (transType < 500) {
					double l_MinOpeningBalance = toproduct.getMinOpeningBalance();
					if (l_ToAccountData.getStatus() == 0
							&& SharedUtil.formatMIT2DateStr(l_ToAccountData.getLastTransDate()).equals("01/01/1900")) {
						if (pTransferData.getAmount() < l_MinOpeningBalance) {
							l_DataResult.setDescription("Amount is less than Minimum Opening Balance.");
							l_DataResult.setStatus(false);
						}
					}
				}
			}

		}

		// check Debit Available Balance
		if (l_DataResult.getStatus() && !isGL) {
			double l_Available = 0;
			l_Available = l_AccDAO.getAvaliableBalance(l_FromAccountData, fromproduct.getMinOpeningBalance(), 0,
					l_Conn);
			// Check For A/C Status Closed Pending
			if (l_FromAccountData.getStatus() != 2) {
				if (pTransferData.getAmount() > l_Available) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_Available)));
				}
			} else {
				double l_CurrentBal = l_FromAccountData.getCurrentBalance();
				if (pTransferData.getAmount() != l_CurrentBal) {
					l_DataResult.setStatus(false);
					l_DataResult.setDescription(l_FromAccountData.getAccountNumber() + " insufficient balance "
							+ StrUtil.formatNumberwithComma((pTransferData.getAmount() - l_CurrentBal)));
				}
			}

			if (!l_DataResult.getStatus()) {
				l_DataResult = DBTransactionMgr.checkAndPostLinkWithMultiCurrencyUpdate(pTransferData,
						l_FromAccountData, l_Available, pTransferData.getAmount(), l_TableName, l_TableName1, l_Conn);
				if (l_DataResult.getStatus()) {
					m_AutoLinkTransRef.add(l_DataResult.getTransactionNumber());
					if (l_DataResult.getTransRef() != 0)
						m_AutoLinkTransRef.add(l_DataResult.getTransRef());
					transferDataResult.setAutoLinkTransRef(m_AutoLinkTransRef);
				}

			}
		}
		if (l_DataResult.getStatus()) {
			String fkey1 = "", tkey1 = "";
			if (!isGL) {
				pTransferData.setFromBranchCode(l_FromAccountData.getBranchCode());
				pTransferData.setCurrencyCode(l_FromAccountData.getCurrencyCode());
				pTransferData.setFromCCY(l_FromAccountData.getCurrencyCode());

				fkey1 = "smTransfer" + "_" + pTransferData.getFromCCY() + "_" + pTransferData.getFromBranchCode();
				drCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey1);
			}

			pTransferData.setToBranchCode(l_ToAccountData.getBranchCode());
			pTransferData.setToCCY(l_ToAccountData.getCurrencyCode());
			tkey1 = "smTransfer" + "_" + pTransferData.getToCCY() + "_" + pTransferData.getToBranchCode();
			crCCYRate = SharedLogic.getSystemData().getpFECCurrencyRateList().get(tkey1);

			if (pTransferData.getFromBranchCode().equalsIgnoreCase(pTransferData.getToBranchCode())) {
				isSameBranch = true;
			}
		}

		if (l_DataResult.getStatus()) {
			if (isSameBranch) {

				// Dr GL or Customer Acc
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData.getUserID());
				prepareTransferData.setAmount(pTransferData.getAmount());
				prepareTransferData.setFromAccNumber(pTransferData.getToAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData.getToBranchCode());
				prepareTransferData.setFromCCY(pTransferData.getToCCY());
				prepareTransferData.setCurrencyCode(pTransferData.getToCCY());
				prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
				prepareTransferData.setRemark(pTransferData.getRemark());
				prepareTransferData.setBasecurrate(crCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * crCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData.getFromAccNumber());
				prepareTransferData.setIbtTransType(mOBTDrTransType);
				prepareTransferData.setCashInHandGL(isGL ? toproduct.getCashInHandGL() : fromproduct.getCashInHandGL());
				prepareTransferData.setProductGL(isGL ? "" : fromproduct.getProductGL());
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(isGL ? 0 : l_FromAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(isGL ? "19000101" : l_FromAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData.getTransdescription());
				prepareTransferData.setIsGL(isGL);
				transferlist.add(prepareTransferData);

				// Cr to Customer
				prepareTransferData = new TransferData();
				prepareTransferData.setUserID(pTransferData.getUserID());
				if (pTransferData.getFromCCY().equalsIgnoreCase(pTransferData.getToCCY()))
					prepareTransferData.setAmount(pTransferData.getAmount() * (crCCYRate * 1000) / 1000);
				else
					prepareTransferData
							.setAmount((pTransferData.getAmount() * crCCYRate / drCCYRate) * (crCCYRate * 1000) / 1000);
				prepareTransferData.setFromAccNumber(pTransferData.getFromAccNumber());
				prepareTransferData.setFromBranchCode(pTransferData.getFromBranchCode());
				prepareTransferData.setFromCCY(pTransferData.getFromCCY());
				prepareTransferData.setCurrencyCode(pTransferData.getFromCCY());
				prepareTransferData.setpTransactionTime(pTransferData.getTransactionTime());
				prepareTransferData.setTransactionDate(pTransferData.getTransactionDate());
				prepareTransferData.setEffectiveDate(pTransferData.getEffectiveDate());
				prepareTransferData.setReferenceNo(pTransferData.getReferenceNo());
				prepareTransferData.setRemark(pTransferData.getRemark());
				prepareTransferData.setBasecurrate(crCCYRate);
				prepareTransferData.setBaseAmount(StrUtil.round2decimals(pTransferData.getAmount() * crCCYRate));
				prepareTransferData.setPostDescription(desc + pTransferData.getToAccNumber());
				prepareTransferData.setIbtTransType(mOBTCrTransType);
				prepareTransferData.setCashInHandGL(toproduct.getCashInHandGL());
				prepareTransferData.setProductGL(toproduct.getProductGL());
				prepareTransferData.setpByForceCheque("");
				prepareTransferData.setPrevBalance(l_ToAccountData.getCurrentBalance());
				prepareTransferData.setPreviousDate(l_ToAccountData.getLastUpdate());
				prepareTransferData.setTransdescription(pTransferData.getTransdescription());
				prepareTransferData.setIsGL(false);
				transferlist.add(prepareTransferData);

			} else {
				// differenct branches
			}
		}
		if (!l_DataResult.getStatus()) {
			transferDataResult.setStatus(false);
			transferDataResult.setCode("0014");
			transferDataResult.setDescription(l_DataResult.getDescription());
		} else {
			transferDataResult.setTransferlist(transferlist);
			transferDataResult.setStatus(true);
		}

		return transferDataResult;
	}
}

