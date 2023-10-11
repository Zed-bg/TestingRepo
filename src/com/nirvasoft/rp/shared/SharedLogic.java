package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import com.nirvasoft.rp.framework.Ref;
import com.nirvasoft.rp.shared.AccountGLTransactionData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ProductSetupData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.SystemData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.icbs.Enum.TransCode;
import com.nirvasoft.rp.shared.icbs.ProductFeatureType;

public class SharedLogic {
	private static SystemData pSystemData;

	public static final String REGEX_NPS = "[A-Z]*INC([0-9]){8}[A-Za-z0-9]+";	//HMT for Settlement
	public static final String REGEX_MCC = "[A-Z]*MCC([0-9]){8}[A-Za-z0-9]+";	//HMT for Merchant Settlement
	// Currency
		public static String getBaseCurCode(){
			return pSystemData.getBaseCurCode();
		}
		
		public static double getBaseCurRate(){
			return pSystemData.getBaseCurRate();
		}
		
		public static String getBaseCurOperator(){
			return pSystemData.getBaseCurOperator();
		}
		// End of Currency
		
	public static ArrayList<ReferenceData> getCurrencyCodes()
	{
		return pSystemData.getCurrencyCodes();
	}

	public static DataResult checkAccStatus(int transType, double transAmount,AccountData object,ArrayList<ReferenceData> plstStatus) {
		DataResult ret = new DataResult();
		
		// For Debit Case
		if (transType > 500) {
			switch (object.getStatus()) {
			// New (or) Active
			case 0:
				// New Account
				
				if (SharedLogic.isLoan(object.getProduct())) {
					ret.setStatus(true); 
					break;
				}
				
				if (SharedUtil.formatMIT2DateStr(object.getLastTransDate())
						.equals("01/01/1900")) {
					ret.setDescription("Debit Account Status is New.");
					ret.setStatus(false);
				} else
					ret.setStatus(true);
				break;
				
				// Suspend Accont

			case 1:
				ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
						+ getAccountStatusDescription(object, plstStatus) + ".");
				ret.setStatus(false);
				break;
			// Close Pending
			case 2:
				if (transAmount != object.getCurrentBalance()) {
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object, plstStatus) + ".");
					ret.setStatus(false);
				} else
					ret.setStatus(true);
				break;
			// Closed
			case 3:
				ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
						+ getAccountStatusDescription(object,plstStatus) + ".");
				ret.setStatus(false);
				break;
			// Dormant
			case 4:
				ret.setDescription(object.getAccountNumber()+ "Debit Account is "
						+ getAccountStatusDescription(object,plstStatus) + ".");
				ret.setStatus(false);
				break;
			// Stopped Payment
			case 7:
				ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
						+ getAccountStatusDescription(object,plstStatus) + ".");
				ret.setStatus(false);
				break;
			default:
				break;
			}
			
		} else if (transType < 500) {
			// For Credit Case
			switch (object.getStatus()) {
			// New (or) Active
			case 0:
				ret.setStatus(true);
				break;
			// Suspend Accont
			case 1:
				ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
						+ getAccountStatusDescription(object,plstStatus) + ".");
				ret.setStatus(false);
				break;
			// Close Pending
			case 2:
				ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
						+ getAccountStatusDescription(object,plstStatus) + ".");
				ret.setStatus(false);
				break;
			// Closed
			case 3:
				ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
						+ getAccountStatusDescription(object,plstStatus) + ".");
				ret.setStatus(false);
				break;
			//Dormant
			case 4:
				ret.setStatus(true);
					break;
			// Stopped Payment
			case 7:
				ret.setStatus(true);
				break;
			default:
				break;
			}

		}
		return ret;
	}

	public static DataResult checkCashInHandGL(String pAccNumber) {
		DataResult ret = new DataResult();
		ret.setStatus(true);
		for (int i = 0; i < pSystemData.getCurrencyCodes().size(); i++) {
			String glcode = "";
			glcode = getCashinHandGL(pSystemData.getCurrencyCodes().get(i).getCode().toString());
			if (pAccNumber.equals(glcode)) {
				ret.setStatus(false);
				ret.setDescription("Transaction doesn't allow with Cash In Hand(GL).");
				ret.setErrorFlag(1);
				break;
			}
		}
		return ret;
	}

	public static DataResult checkCheckStatus(String pCheckNo, int pStatus) {
		DataResult ret = new DataResult();
		ret.setStatus(true);
		if (pStatus == 1) { // For Paid Check
			ret.setStatus(false);
			ret.setErrorFlag(1);
			ret.setDescription(pCheckNo + " is paid cheque.");
		}
		// For Stopped Check
		if (ret.getStatus()) {
			if (pStatus == 2) {
				ret.setStatus(false);
				ret.setErrorFlag(1);
				ret.setDescription(pCheckNo + " is stopped cheque.");
			}
		}
		// For Cancel Check
		if (ret.getStatus()) {
			if (pStatus == 3) {
				ret.setStatus(false);
				ret.setErrorFlag(1);
				ret.setDescription(pCheckNo + " is cancel cheque.");
			}
		}
		if (ret.getStatus()) {
			if (pStatus == 255) {
				ret.setStatus(false);
				ret.setErrorFlag(1);
				ret.setDescription("Invalid cheque number.");
			}
		}
		return ret;
	}

	public static DataResult checkMinOpnBalance(int transType, double amount, AccountData object) {
		DataResult ret = new DataResult();
		if (transType < 500) {
			double l_MinOpnBal = Account.getAccountMinOpeningBalance(object.getProduct(), object.getCurrencyCode());
			if (object.getStatus() == 0
					&& SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900")) {
				if (amount < l_MinOpnBal) {
					ret.setDescription("Amount is less than Minimum Opening Balance.");
					ret.setStatus(false);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMinOpnBalance(int transType, double amount, ProductData pPrdSetUpData,
			AccountData object) {
		DataResult ret = new DataResult();
		double l_MinOpnBal = 0;
		if (transType < 500) {
			l_MinOpnBal = pPrdSetUpData.getMinOpeningBalance();
			if (object.getStatus() == 0
					&& SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900")) {
				if (amount < l_MinOpnBal) {
					ret.setDescription("Amount is less than Minimum Opening Balance.");
					ret.setStatus(false);
					ret.setErrorFlag(1);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMinOpnBalanceCurrency(int transType, double amount, AccountData object) {
		DataResult ret = new DataResult();
		if (transType < 500) {
			double l_MinOpnBal = Account.getAccountMinOpeningBalance(object.getProduct(), object.getCurrencyCode());
			if (object.getStatus() == 0
					&& SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900")) {
				if (amount < l_MinOpnBal) {
					ret.setDescription("Amount is less than Minimum Opening Balance.");
					ret.setStatus(false);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMinWithdrawal(int transType, double amount, ProductData pPrdSetUpData) {
		DataResult ret = new DataResult();
		double l_MinWithdrawal = 0;
		if (transType > 500 && transType != 969) {
			l_MinWithdrawal = pPrdSetUpData.getMinWithdrawal();

			if (amount < l_MinWithdrawal) {
				ret.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
				ret.setStatus(false);
				ret.setErrorFlag(1);
			} else
				ret.setStatus(true);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMinWithdrawal(int transType, double amount, String productCode) {
		DataResult ret = new DataResult();
		if (transType > 500 && transType != 969) {
			double l_MinWithdrawal = Account.getAccountMinWithdrawal(productCode);
			if (amount < l_MinWithdrawal) {
				ret.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
				ret.setStatus(false);
			} else
				ret.setStatus(true);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMinWithdrawal(int transType, double amount, String productCode, String pCurCode) {
		DataResult ret = new DataResult();
		if (transType > 500 && transType != 969) {
			double l_MinWithdrawal = getAccountMinWithdrawal(productCode, pCurCode);
			if (amount < l_MinWithdrawal) {
				ret.setDescription("Amount is less than minimum withdrawal amount " + l_MinWithdrawal);
				ret.setStatus(false);
			} else
				ret.setStatus(true);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMultipleOf(double amount, ProductData pPrdSetUpData) {
		DataResult ret = new DataResult();
		double l_MultipleOf = 0;

		l_MultipleOf = pPrdSetUpData.getMultipleOf();
		if ((int) (amount % l_MultipleOf) != 0) {
			ret.setDescription("Amount not multiple of " + l_MultipleOf);
			ret.setStatus(false);
			ret.setErrorFlag(1);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static DataResult checkMultipleOf(double amount, String productCode) {
		DataResult ret = new DataResult();
		double l_MultipleOf = Account.getAccountMultipleOf(productCode);
		if ((int) (amount % l_MultipleOf) != 0) {
			ret.setDescription("Amount not multiple of " + l_MultipleOf);
			ret.setStatus(false);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static String formatChequeNo(String pChkNo) {
		String ret = "";
		int l_ChkNoCharLength = 0;
		StringBuffer l_ChkNoChar = new StringBuffer();
		StringBuffer l_ChequeDigit = new StringBuffer();
		seperateCheckNo(pChkNo, l_ChkNoChar, l_ChequeDigit);
		l_ChkNoCharLength = l_ChkNoChar.toString().length();
		ret = l_ChkNoChar.toString() + SharedUtil.leadZeros(l_ChequeDigit.toString(), 10 - l_ChkNoCharLength);
		return ret;
	}

	public static double getAccountMinBalance(String productCode) {
		double ret = 0;
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)) {
				ret = pSystemData.getProducts().get(i).getMinBalance();
				break;
			}
		}
		return ret;
	}

	public static double getAccountMinBalance(String productCode, String pCurCode) {
		double ret = 0;
		if (pSystemData.getProductDatasCurrency().size() > 0) {
			for (int i = 0; i < pSystemData.getProductDatasCurrency().size(); i++) {
				if (pSystemData.getProductDatasCurrency().get(i).getProductCode().equals(productCode)
						&& pSystemData.getProductDatasCurrency().get(i).getCurCode().equals(pCurCode)) {
					ret = pSystemData.getProductDatasCurrency().get(i).getMinBalance();
					break;
				}
			}
		} else {
			ret = getAccountMinBalance(productCode);
		}
		return ret;
	}

	public static double getAccountMinBalanceNew(String productCode) {
		double ret = 0;
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)) {
				if (pSystemData.getProducts().get(i).getProductSetupList().size() > 0)
					ret = pSystemData.getProducts().get(i).getProductSetupList().get(0).getMinBalance();
				break;
			}
		}
		return ret;
	}

	public static double getAccountMultipleOf(String productCode) {
		double ret = 0;
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			ArrayList<ProductSetupData> l_PrdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
			for (int j = 0; j < l_PrdSetupLst.size(); j++) {
				if (l_PrdSetupLst.get(j).getProductId().equalsIgnoreCase(productCode)) {
					ret = l_PrdSetupLst.get(j).getMultipleOf();
					break;
				}
			}
			if (ret != 0) {
				break;
			}
		}
		return ret;
	}

	public static String getAccountProductCode(String accountnumber) {
		String ret = "";
		// int t= pSystemData.getCountries().size();
		// X.say(t);
		if (isCustomerAccount(accountnumber)) {
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber.length()
						&& pSystemData.getProducts().get(i).getProductLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,
										pSystemData.getProducts().get(i).getProductStart()
												+ pSystemData.getProducts().get(i).getProductLen() - 1))) {
					ret = accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,
							pSystemData.getProducts().get(i).getProductStart()
									+ pSystemData.getProducts().get(i).getProductLen() - 1);
					break;
				}
			}
		} else if (isGLAccount(accountnumber)) {
			ret = "GL";
		} else {
			ret = "GL";
		}
		return ret;
	}

	public static String getAccountType(String accountnumber) {
		String ret = "";
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getLength() == accountnumber.length()
					&& pSystemData.getProducts().get(i).getTypeLen() > 0
					&& pSystemData.getProducts().get(i).getProductCode()
							.equals(accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,
									pSystemData.getProducts().get(i).getProductStart()
											+ pSystemData.getProducts().get(i).getProductLen() - 1))) {
				ret = accountnumber.substring(pSystemData.getProducts().get(i).getTypeStart() - 1,
						pSystemData.getProducts().get(i).getTypeStart() + pSystemData.getProducts().get(i).getTypeLen()
								- 1);
				break;
			}
		}
		return ret;
	}

	public static String getCashinHandGL(String pCurrencyCode) {
		String l_GLCode = "";
		String l_CurrencyCode = getCurrencyDigit(pCurrencyCode);
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (l_CurrencyCode.equals(pSystemData.getProducts().get(i).getCurCode())) {
				l_GLCode = pSystemData.getProducts().get(i).getCashInHandGL();
				break;
			}
		}
		return l_GLCode;
	}

	public static String getCurrencyDigit(String code) {
		String ret = "X";
		for (int i = 0; i < pSystemData.getCurrencyCodes().size(); i++) {
			if (pSystemData.getCurrencyCodes().get(i).getCode().equalsIgnoreCase(code)) {
				ret = "" + pSystemData.getCurrencyCodes().get(i).getKey();
				break;
			}
		}
		return ret;
	}

	public static ProductData getProductCodeToProductData(String productCode) {
		ProductData ret = new ProductData();
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)) {
				ret = pSystemData.getProducts().get(i);
			}
		}
		return ret;
	}

	public static int getProductFeature(String productCode) {
		int ret = 0;
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)) {
				ret = (int) pSystemData.getProducts().get(i).getProductFeatureType();
			}
		}
		return ret;
	}

	public static SystemData getSystemData() {
		return pSystemData;
	}

	public static int getSystemSettingT12N1(String pT1) {
		int ret = 255;
		for (int i = 0; i < pSystemData.getSystemSettingDatas().size(); i++) {
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)) {
				ret = pSystemData.getSystemSettingDatas().get(i).getN1();
				break;
			}
		}
		return ret;
	}

	public static int getSystemSettingT12N3(String pT1) {
		int ret = 255;
		for (int i = 0; i < pSystemData.getSystemSettingDatas().size(); i++) {
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)) {
				ret = pSystemData.getSystemSettingDatas().get(i).getN3();
				break;
			}
		}
		return ret;
	}

	public static int getSystemSettingT12N4(String pT1) {
		int ret = 255;
		for (int i = 0; i < pSystemData.getSystemSettingDatas().size(); i++) {
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)) {
				ret = pSystemData.getSystemSettingDatas().get(i).getN4();
				break;
			}
		}
		return ret;
	}

	public static String getSystemSettingT12T2(String pT1) {
		String ret = "";
		for (int i = 0; i < pSystemData.getSystemSettingDatas().size(); i++) {
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)) {
				ret = pSystemData.getSystemSettingDatas().get(i).getT2();
				break;
			}
		}
		return ret;
	}

	public static String getTransDescription(TransCode tc, String productCode) {
		String ret = "";
		switch (getTransType(tc, productCode)) {
		case 0:
			ret = "Credit";
			break;
		case 500:
			ret = "Debit";
			break;
		case 10:
			ret = "Current Account Cash Deposit";
			break;
		case 510:
			ret = "Current Account Cash Withdrawal";
			break;
		case 20:
			ret = "Savings Account Cash Deposit";
			break;
		case 520:
			ret = "Savings Account Cash Withdrawal";
			break;
		case 30:
			ret = "Fixed Deposit Account Cash Deposit";
			break;
		case 80:
			ret = "Card Account Cash Deposit";
			break;
		case 580:
			ret = "Card Account Cash Withdrawal";
			break;
		case 99:
			if (tc.equals(TransCode.SettlementCashCr))
				ret = "Settlement"; // HMT
			else if (tc.equals(TransCode.SettlementFeeCashCr))
				ret = "Settlement Fee"; // HMT
			else
				ret = "GL Account Cash Deposit";
			break;
		case 599:
			if (tc.equals(TransCode.SettlementCashDr))
				ret = "Settlement"; // HMT
			else if (tc.equals(TransCode.SettlementFeeCashDr))
				ret = "Settlement Fee"; // HMT
			else
				ret = "GL Account Cash Withdrawal";
			break;
		case 205:
			if (tc.equals(TransCode.MultipleTransferTo))
				ret = "Multiple Transafer To (Credit)";
			else if (tc.equals(TransCode.ScheduleTransferCr)) // HMT
				ret = "Transaction Schedule Posting (Credit)";
			else if (tc.equals(TransCode.PoTransferCr))
				ret = "Payment Order"; // HMT
			else
				ret = "Transfer To (Credit)";
			break;
		case 705:
			if (tc.equals(TransCode.MultipleTrnasferFrom))
				ret = "Multiple Transfer From (Debit)";
			else if (tc.equals(TransCode.ScheduleTransferDr)) // HMT
				ret = "Transaction Schedule Posting (Debit)";
			else if (tc.equals(TransCode.PoTransferDr))
				ret = "Payment Order"; // HMT
			else
				ret = "Transfer From (Debit)";
			break;
		case 203:
			ret = "Remit Inward";
			break;
		case 703:
			ret = "Remit Inward";
			break;
		case 191:
			ret = "Transaction Schedule Posting Cash (Credit)"; // HMT
			break;
		case 691:
			ret = "Transaction Schedule Posting Cash (Debit)"; // HMT
			break;
		case 532:
			ret = "Fixed Deposit Account Interest Withdraw";
			break;
		case 760:
			if (tc.equals(TransCode.FixedInterestDr)) {
				ret = "Fixed Deposit Interest Transfer From (Debit)";
			} else if (tc.equals(TransCode.FixedAutoRenewDr)) {
				ret = "Fixed Deposit Auto Renewal (Debit)";
			} else {
				ret = "Fixed Deposit Closing Withdraal(Debit)";
			}
			break;
		case 260:
			if (tc.equals(TransCode.FixedInterestCr)) {
				ret = "Fixed Deposit Interest Transfer To (Credit)";
			} else if (tc.equals(TransCode.FixedAutoRenewCr)) {
				ret = "Fixed Deposit Auto Renewal (Credit)";
			} else {
				ret = "Fixed Deposit Closing Withdraal(Credit)";
			}
			break;
		case 105:
		case 605:
			ret = "Payment Order"; // HMT
			break;
		case 840:
			ret = "Loan Repayment Debit";
			break;
		case 340:
			ret = "Loan Repayment Credit";
			break;
		case 930:
			ret = "Hire Purchase Repayment Debit Transaction";
			break;
		case 430:
			ret = "Hire Purchase Repayment Credit Transaction";
			break;
		case 810:
			ret = "Overdraft Cancel Debit Transaction";
			break;
		case 310:
			ret = "Overdraft Cancel Credit Transaction";
			break;
		case 201:
			if (tc.equals(TransCode.AccountLinkCr)) {
				ret = "Auto Link Transfer";
			} else if (tc.equals(TransCode.OtherBankRemitOutCr)) {
				ret = "Remit Out Transfer (Credit)";
			}
			break;
		case 701:
			if (tc.equals(TransCode.AccountLinkDr)) {
				ret = "Auto Link Transfer";
			} else if (tc.equals(TransCode.OtherBankRemitOutDr)) {
				ret = "Remit Out Transfer (Debit)";
			}
			break;
		case 740:
			ret = "Loan Account Active";
			break;
		case 240:
			ret = "Loan Account Active";
			break;
		case 106:
		case 206:
		case 606:
		case 706:
			ret = "Gift Cheque";
			break;
		case 104:
			ret = "Remit Out Income";
			break;
		case 660:
			ret = "FD Closing WDL";
			break;
		case 800:
			ret = "Revaluate From";
			break;
		case 300:
			ret = "Revaluate To";
			break;
		case 600:
			ret = "Revaluate Cash Dr";
			break;
		case 100:
			ret = "Revaluate Cash Cr";
			break;

		}
		return ret;
	}

	
	public static String getTransDescriptionNew(TransCode tc,int transType) {
		String ret = "";
		switch (transType) {
		case 0:
			ret = "Credit";
			break;
		case 500:
			ret = "Debit";
			break;
		case 10:
			ret = "Current Account Cash Deposit";
			break;
		case 510:
			ret = "Current Account Cash Withdrawal";
			break;
		case 20:
			ret = "Savings Account Cash Deposit";
			break;
		case 520:
			ret = "Savings Account Cash Withdrawal";
			break;
		case 30:
			ret = "Fixed Deposit Account Cash Deposit";
			break;
		case 80:
			ret = "Card Account Cash Deposit";
			break;
		case 580:
			ret = "Card Account Cash Withdrawal";
			break;
		case 99:
			if (tc.equals(TransCode.SettlementCashCr))
				ret = "Settlement"; // HMT
			else if (tc.equals(TransCode.SettlementFeeCashCr))
				ret = "Settlement Fee"; // HMT
			else
				ret = "GL Account Cash Deposit";
			break;
		case 599:
			if (tc.equals(TransCode.SettlementCashDr))
				ret = "Settlement"; // HMT
			else if (tc.equals(TransCode.SettlementFeeCashDr))
				ret = "Settlement Fee"; // HMT
			else
				ret = "GL Account Cash Withdrawal";
			break;
		}
		return ret;
	}

	public static int getTransType(TransCode tc, String productCode) {
		int ret = 0;

		if (tc == TransCode.Deposit) {
			if (SharedLogic.isCurrent(productCode)) {
				ret = 10;
			} else if (SharedLogic.isSavings(productCode)) {
				ret = 20;
			} else if (SharedLogic.isFD(productCode)) {
				ret = 30;
			} else if (productCode.equals("GL")) {

				ret = 99;
			} else if (SharedLogic.isCard(productCode)) {
				ret = 80;
			} else {
				ret = 0;
			}
		} else if (tc == TransCode.Withdraw) {
			if (SharedLogic.isCurrent(productCode)) {
				ret = 510;
			} else if (SharedLogic.isSavings(productCode)) {
				ret = 520;
			} else if (productCode.equals("GL")) {
				ret = 599;
			} else if (SharedLogic.isCard(productCode)) {
				ret = 580;
			} else {
				ret = 500;
			}

		} else if (tc == TransCode.TransferFrom) {
			ret = 705;
		} else if (tc == TransCode.TransferTo) {
			ret = 205;
		} else if (tc == TransCode.MultipleTrnasferFrom) {
			ret = 705;
		} else if (tc == TransCode.MultipleTransferTo) {
			ret = 205;
		} else if (tc == TransCode.ATMIBTTransferFrom) {
			ret = 969;
		} else if (tc == TransCode.ATMIBTTransferTo) {
			ret = 469;
		} else if (tc == TransCode.ScheduleCashCr) {
			ret = 191; // HMT
		} else if (tc == TransCode.ScheduleCashDr) {
			ret = 691;
		} else if (tc == TransCode.ScheduleTransferCr) {
			ret = 205;
		} else if (tc == TransCode.ScheduleTransferDr) {
			ret = 705;
		} else if (tc == TransCode.FixedDepositIntWdl) {
			ret = 532;
		} else if (tc == TransCode.FixedInterestDr) {
			ret = 760;
		} else if (tc == TransCode.FixedInterestCr) {
			ret = 260;
		} else if (tc == TransCode.FixedAutoRenewDr) {
			ret = 760;
		} else if (tc == TransCode.FixedAutoRenewCr) {
			ret = 260;
		} else if (tc == TransCode.FDWDLDr) {
			ret = 760;
		} else if (tc == TransCode.FDWDLCr) {
			ret = 260;
		} else if (tc == TransCode.PoCashCr) {
			ret = 105;
		} else if (tc == TransCode.PoCashDr) {
			ret = 605;
		} else if (tc == TransCode.PoTransferCr) {
			ret = 205;
		} else if (tc == TransCode.PoTransferDr) {
			ret = 705;
		} else if (tc == TransCode.ODDr) {
			ret = 810;
		} else if (tc == TransCode.ODCr) {
			ret = 310;
		} else if (tc == TransCode.ODDrNPL) {
			ret = 811;
		} else if (tc == TransCode.ODCrNPL) {
			ret = 311;
		} else if (tc == TransCode.ODSCrNPL) {
			ret = 312;
		} else if (tc == TransCode.LoanIntCollectDr) {
			ret = 840;
		} else if (tc == TransCode.LoanIntCollectCr) {
			ret = 340;
		} else if (tc == TransCode.HPTrDr) {
			ret = 930;
		} else if (tc == TransCode.HPTrCr) {
			ret = 430;
		} else if (tc == TransCode.ODIntCollectDr) {
			ret = 810;
		} else if (tc == TransCode.ODIntCollectCr) {
			ret = 310;
		} else if (tc == TransCode.AccountLinkCr) {
			ret = 201;
		} else if (tc == TransCode.AccountLinkDr) {
			ret = 701;
		} else if (tc == TransCode.LoanLimitDr) {
			ret = 740;
		} else if (tc == TransCode.LoanLimitCr) {
			ret = 240;
		} else if (tc == TransCode.GcCashCr) {
			ret = 106;
		} else if (tc == TransCode.GcCashDr) {
			ret = 606;
		} else if (tc == TransCode.GcTransferCr) {
			ret = 206;
		} else if (tc == TransCode.GcTransferDr) {
			ret = 706;
		} else if (tc == TransCode.RemitInWardDr) {
			ret = 703;
		} else if (tc == TransCode.RemitInWardCr) {
			ret = 203;
		} else if (tc == TransCode.FDCashWDL) {
			ret = 660;
		} else if (tc == TransCode.SettlementCashCr) {
			ret = 99;
		} else if (tc == TransCode.SettlementCashDr) {
			ret = 599;
		} else if (tc == TransCode.SettlementTrCr) {
			ret = 205;
		} else if (tc == TransCode.SettlementTrDr) {
			ret = 705;
		} else if (tc == TransCode.SettlementFeeCashCr) {
			ret = 99;
		} else if (tc == TransCode.SettlementFeeCashDr) {
			ret = 599;
		} else if (tc == TransCode.POSTrCr) {
			ret = 459;
		} else if (tc == TransCode.POSTrDr) {
			ret = 959;
		} else if (tc == TransCode.POSFeeTrCr) {
			ret = 459;
		} else if (tc == TransCode.POSFeeTrDr) {
			ret = 959;
		} else if (tc == TransCode.OtherBankRemitOutCash) {
			ret = 104;
		} else if (tc == TransCode.OtherBankRemitOutDr) {
			ret = 701;
		} else if (tc == TransCode.OtherBankRemitOutCr) {
			ret = 201;
		} else if (tc == TransCode.RevaluateFrom) {
			ret = 800;
		} else if (tc == TransCode.RevaluateTo) {
			ret = 300;
		} else if (tc == TransCode.RevaluateCashDr) {
			ret = 600;
		} else if (tc == TransCode.RevaluateCashCr) {
			ret = 100;
		} else if (tc == TransCode.RemitTransferDr) {
			ret = 703;
		} else if (tc == TransCode.RemitTransferCr) {
			ret = 203;
		} else if (tc == TransCode.RemitCashCr) {
			ret = 103;
		} else if (tc == TransCode.AllowLoanLimitDr) {
			ret = 740;
		} else if (tc == TransCode.AllowLoanLimitCr) {
			ret = 240;
		} else if (tc == TransCode.LoanRepaymentDr) {
			ret = 840;
		} else if (tc == TransCode.LoanRepaymentCr) {
			ret = 340;
		} else if (tc == TransCode.CaIntDr) {
			ret = 710;
		} else if (tc == TransCode.CaIntCr) {
			ret = 210;
		} else if (tc == TransCode.SaIntDr) {
			ret = 720;
		} else if (tc == TransCode.SaIntCr) {
			ret = 220;
		} else if (tc == TransCode.ODDr) {
			ret = 810;
		} else if (tc == TransCode.ODCr) {
			ret = 310;
		} else if (tc == TransCode.ODDrNPL) {
			ret = 811;
		} else if (tc == TransCode.ODCrNPL) {
			ret = 311;
		} else if (tc == TransCode.ODSCrNPL) {
			ret = 312;
		}
		return ret;
	}

	public static boolean isCall(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();// getProductFeature(productCode);
		if (l_ProdFeatureType == ProductFeatureType.Call) {
			ret = true;
		}
		return ret;
	}

	public static boolean isCard(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();// getProductFeature(productCode);
		if (l_ProdFeatureType == ProductFeatureType.Card) {
			ret = true;
		}
		return ret;
	}

	public static boolean isCredit(int i) {
		return i < 500 ? true : false;
	}

	public static boolean isCurrent(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();//getProductFeature(productCode);
		if ((l_ProdFeatureType == ProductFeatureType.CA)) {
			ret = true;
		}
		return ret;
	}

	public static boolean isCustomerAccount(String accountnumber) {
		boolean ret = false;
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getLength() == accountnumber.length()
					&& pSystemData.getProducts().get(i).getProductCode()
							.equals(accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,
									pSystemData.getProducts().get(i).getProductStart()
											+ pSystemData.getProducts().get(i).getProductLen() - 1))) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public static boolean isDebit(int i) {
		return !isCredit(i);
	}

	public static boolean isFD(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();//getProductFeature(productCode);
		if (l_ProdFeatureType == ProductFeatureType.FD) {
			ret = true;
		}
		return ret;
	}

	public static boolean isGLAccount(String a) {
		boolean ret = false;
		if (a.length() == 16 && !SharedUtil.isNumber(a.substring(0, 1)) && SharedUtil.isNumber(a.substring(1, 16)))
			ret = true;
		return ret;
	}

	public static boolean isLoan(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();// getProductFeature(productCode);
		if (l_ProdFeatureType == ProductFeatureType.LA) {
			ret = true;
		}
		return ret;
	}

	public static boolean isSavings(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();// getProductFeature(productCode);
		if ((l_ProdFeatureType == ProductFeatureType.SA)) {
			ret = true;
		}
		return ret;
	}

	public static boolean isUA(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();// getProductFeature(productCode);
		if (l_ProdFeatureType == ProductFeatureType.UA) {
			ret = true;
		}
		return ret;
	}

	public static boolean isSpecial(String productCode) {
		boolean ret = false;
		int l_ProdFeatureType = (int) SharedLogic.getSystemData().getpProductList().get(productCode).getProductFeatureType();// getProductFeature(productCode);
		if (l_ProdFeatureType == ProductFeatureType.Special) {
			ret = true;
		}
		return ret;
	}
	
	public static void seperateCheckNo(String pChkNo, StringBuffer pChkNoChar, StringBuffer pChequeDigit) {
		try {
			for (Character l_Char : pChkNo.toCharArray()) {
				// Letter Only
				if (Character.isLetter(l_Char)) {
					pChkNoChar.append(l_Char);
				}
				// Digit Only
				if (Character.isDigit((l_Char))) {
					pChequeDigit.append(l_Char);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void setSystemData(SystemData p) {
		pSystemData = p;

	}
	
	public static DataResult checkMultipleOf(double amount,String productCode,String pCurCode) {
		DataResult ret = new DataResult();
		double l_MultipleOf = getAccountMulipleofNew(productCode, pCurCode);
		if ((int) (amount % l_MultipleOf) != 0) {
			ret.setDescription("Amount not multiple of " + l_MultipleOf);
			ret.setStatus(false);
		} else
			ret.setStatus(true);
		return ret;
	}

	public static double getAccountMinBalanceNew(String productCode, String pCurCode) {
		double ret = 0;boolean isbreak= false;String l_PCode= productCode;int pCurID=0;
		int l_Setting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN2();
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getProductCode()
					.contains(productCode)) {
				for(int ii=0;ii<pSystemData.getProducts().get(i).getProductSetupList().size() ; ii++){
					l_PCode= productCode;
					if(l_Setting ==1) {
						for(int j=0;j<pSystemData.getCurrencyCodes().size();j++){
							if(pSystemData.getCurrencyCodes().get(j).getCode().equals(pCurCode) && !pCurCode.equals("MMK"))
								pCurID = pSystemData.getCurrencyCodes().get(j).getKey();
						}
						l_PCode = productCode + (pCurID==0?"":pCurID); 
					}
					if(pSystemData.getProducts().get(i).getProductSetupList().get(ii).getProductId().equals(l_PCode)){
						ret = pSystemData.getProducts().get(i).getProductSetupList().get(ii).getMinBalance();
						isbreak = true;
						break;
					}
					if(isbreak)
						break;
				}					
			}
		}
		return ret;
	}
	
	public static int getSystemSettingT12N2(String pT1){
		int ret = 255;
		for (int i=0; i < pSystemData.getSystemSettingDatas().size(); i++){
			if (pSystemData.getSystemSettingDatas()
					.get(i).getT1().equals(pT1)){
				ret = pSystemData.getSystemSettingDatas().get(i).getN2();
				break; 	// added
			}
		}
		return ret;
	}
	
	public static double getAccountMulipleofNew(String pProductCode,String pCurCode) {
		double ret = 0;boolean isbreak= false;String l_PCode= pProductCode;int pCurID=0;
		int l_Setting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN2();
		if(pSystemData.getProducts().size()>0) {
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.contains(pProductCode)) {
					ArrayList<ProductSetupData> l_ProdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
					for(int ii=0;ii<l_ProdSetupLst.size() ; ii++){
						l_PCode= pProductCode;
						if(l_Setting ==1) {
							for(int j=0;j<pSystemData.getCurrencyCodes().size();j++){
								if(pSystemData.getCurrencyCodes().get(j).getCode().equals(pCurCode) && !pCurCode.equals("MMK"))
									pCurID = pSystemData.getCurrencyCodes().get(j).getKey();
							}
							l_PCode = pProductCode + (pCurID==0?"":pCurID); 
						}
						if(l_ProdSetupLst.get(ii).getProductId().equals(l_PCode)){
							ret = l_ProdSetupLst.get(ii).getMultipleOf();
							isbreak = true;
							break;
						}
						if(isbreak)
							break;
					}					
				}
			}
		}else {
			ret=getAccountMultipleOf(pProductCode);
		}
		return ret;	
	}
	
	public static double getAccountMinWithdrawal(String productCode, String pCurCode) {
		double ret = 0;
		if(pSystemData.getProductDatasCurrency().size()>0) {
			for (int i = 0; i < pSystemData.getProductDatasCurrency().size(); i++) {
				if (pSystemData.getProductDatasCurrency().get(i).getProductCode()
						.equals(productCode) && pSystemData.getProductDatasCurrency()
						.get(i).getCurCode().equals(pCurCode)) {
					ret = pSystemData.getProductDatasCurrency().get(i).getMinWithdrawal();
					break;
				}
			}
		}
		else {
			ret=getAccountMinWithdrawal(productCode);
		}
		return ret;
	}
	
	public static double getAccountMinWithdrawal(String productCode) {
		double ret = 0;
		for (int i = 0; i < pSystemData.getProducts().size(); i++) {
			if (pSystemData.getProducts().get(i).getProductCode()
					.equals(productCode)) {
				ret = pSystemData.getProducts().get(i).getMinWithdrawal();
				break;
			}
		}
		return ret;
	}
	
	public static String getAccountStatusDescription(AccountData object,ArrayList<ReferenceData> plstStatus){
		String ret  = "";
		for(int i =0; i< plstStatus.size();i++) {
			if(object.getStatus()==0 && i <= 1 ) {				
				 if (SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900"))
					 ret = plstStatus.get(1).getDescription();
				 else
					 ret = plstStatus.get(0).getDescription();
			} else if(object.getStatus()==i-1) 			
				ret = plstStatus.get(i).getDescription();
		}
        return ret;        
    }
	
	public static String getCheckStatus(int p){
		String ret = "";
		if (p==0)
			ret = "Unpaid";
		else if(p==1)
			ret = "Paid";
		else if(p==2)
			ret = "Stopped";
		else if(p==3)
			ret = "Cancel";
		return ret;
	}
	
	public static class Account{

		public static boolean isAccount(String a) {
			boolean ret = false;
			if (isCustomerAccount(a) || isGLAccount(a))
				ret = true;
			return ret;
		}

		public static boolean isGLAccount(String a) {
			boolean ret = false;
			int gl_Length = getSystemSettingT12N1("GLS");
			int gl_setting = getSystemSettingT12N5("GLS");
			if(gl_setting==0){
				if ((a.length() == gl_Length && !SharedUtil.isNumber(a.substring(0, 1))
						&& SharedUtil.isNumber(a.substring(1, gl_Length))) || (a.length() == 8 && SharedUtil.isNumber(a.substring(0, 7))))
					ret = true;
			} else {
				if ((a.length() == gl_Length && (Integer.parseInt(a.substring(0, 1))>=1 && Integer.parseInt(a.substring(0, 1)) <9)
						&& SharedUtil.isNumber(a.substring(1, gl_Length))) || (a.length() == 8 && SharedUtil.isNumber(a.substring(0, 7))))
					ret = true;
			}
			return ret;
		}
		public static boolean isGLAccountYNDB(String a) {
			boolean ret = false;
			int gl_Length = getSystemSettingT12N1("GLS");
			if ((a.length() == gl_Length || !SharedUtil.isNumber(a.substring(0, 1))
					&& SharedUtil.isNumber(a.substring(1, gl_Length))) || (a.length() == 8 && SharedUtil.isNumber(a.substring(0, 7))))
				ret = true;
			return ret;
		}
		public static boolean isCustomerAccount(String accountnumber) {
			boolean ret = false;
			if(accountnumber.matches("\\d*")){
				for (int i = 0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getLength() == accountnumber
							.length()
							&& pSystemData
									.getProducts()
									.get(i)
									.getProductCode()
									.equals(accountnumber.substring(
											pSystemData.getProducts().get(i)
													.getProductStart() - 1,
											pSystemData.getProducts().get(i)
													.getProductStart()
													+ pSystemData.getProducts()
															.get(i).getProductLen()
													- 1))) {
						ret = true;
						break;
					}
				}
			}
			return ret;
		}
		
		public static boolean isCustomerAccountNew(String accountnumber) {
			boolean ret = false;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber.length() 
						&& pSystemData.getProducts().get(i).getProductCode().equals
						(accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,
												pSystemData.getProducts().get(i).getProductStart()
												+ pSystemData.getProducts().get(i).getProductLen()
												- 1))) {
					ret = true;
					break;
				}
			}
			return ret;
		}
				
		public static String formatAccountNo(String AccountNo) {
			String ret = AccountNo;
			int ProductStart = SharedLogic.Account.getAccountProductStart("01");
			int ProductLen = SharedLogic.Account.getAccountProductLen("01");
			
			String Product = AccountNo.substring(ProductStart-1, ProductStart + ProductLen -1);
			
			int BranchStart = SharedLogic.Account.getAccountBranchStart(Product);
			int BranchLen = SharedLogic.Account.getAccountBranchLen(Product);
			
			int AccTypeStart = SharedLogic.Account.getAccountTypeStart(Product);
			int AccTypeLen = SharedLogic.Account.getAccountTypeLen(Product);
			
			int CurrencyStart = SharedLogic.Account.getAccountCurrencyStart(Product);
			int CurrencyLen = SharedLogic.Account.getAccountCurrencyLen(Product);
			
			/*int ReservedStart = LogicMgr.Account.getAccountReserveStart(Product);*/
			int ReservedLen = SharedLogic.Account.getAccountReserveLength(Product);
			
			/*String ReservedString = "0";*/
			int AccSerialLen = SharedLogic.Account.getAccountSerialLen(Product);
			int CheckDig = 1;
			
			int InitialAccLen = ProductLen + BranchLen + AccTypeLen + CurrencyLen;
			int AccNoLen = ProductLen + BranchLen + AccTypeLen + CurrencyLen
					+ ReservedLen + AccSerialLen + CheckDig;

			if (BranchStart > 0 && AccTypeStart > 0)
				ret = AccountNo.substring(ProductStart-1, ProductStart + ProductLen -1)  
					+ AccountNo.substring(BranchStart - 1, BranchStart
					+ BranchLen - 1)
					+ AccountNo.substring(AccTypeStart - 1, AccTypeStart
					+ AccTypeLen - 1)
					+ AccountNo.substring(CurrencyStart - 1, CurrencyStart
					+ CurrencyLen - 1)
					+ SharedUtil.leadZeros(AccountNo.substring(InitialAccLen),
						AccNoLen - InitialAccLen);
			return ret;
		}
		public static String getCurrencyDigit(String code) {
			String ret = "X";
			for (int i = 0; i < pSystemData.getCurrencyCodes().size(); i++) {
				if (pSystemData.getCurrencyCodes().get(i).getCode()
						.equalsIgnoreCase(code)) {
					ret = "" + pSystemData.getCurrencyCodes().get(i).getKey();
					break;
				}
			}
			return ret;
		}
		
		public static String getCurrencyCode(String curDigit) {
			String ret = "X";
			for (int i = 0; i < pSystemData.getCurrencyCodes().size(); i++) {
				if (pSystemData.getCurrencyCodes().get(i).getKey() == Integer.parseInt(curDigit)) {
					ret = pSystemData.getCurrencyCodes().get(i).getCode();
					break;
				}
			}
			return ret;
		}

		public static String getNewAccountNumberNotAccType(String lastaccount,
				String product, String branch, String type, String currency,
				String reserve, String other) {
			String ret = "";
			int lastserial = 0;
			lastserial = Integer.parseInt(lastaccount);

			String serial = SharedUtil.leadZeros(lastserial + 1,
					getAccountSerialLen(product));
			/*
			 * String serial
			 * =ClientUtil.leadZeros(0,getAccountSerialLen(product)); if
			 * (!lastaccount.equalsIgnoreCase("")){ serial =
			 * lastaccount.substring(getAccountSerialStart(product)-1,
			 * getAccountSerialStart(product)-1+ getAccountSerialLen(product) );
			 * serial = ClientUtil.leadZeros(""+(ClientUtil.numLong(serial)+1),
			 * getAccountSerialLen(product)); }
			 */
			// LIMITATION : we need to add more IF when new sequence is
			// introduced.
			// MIT Standard format
			if (getAccountOtherStart(product) < getAccountProductStart(product)
					&& getAccountProductStart(product) < getAccountBranchStart(product)
					&& getAccountBranchStart(product) < getAccountTypeStart(product)
					&& getAccountTypeStart(product) < getAccountCurrencyStart(product)
					&& getAccountCurrencyStart(product) < getAccountReserveStart(product)
					&& getAccountReserveStart(product) < getAccountSerialStart(product)
					&& getAccountSerialStart(product) < getAccountCheckStart(product)) {
				ret = getCheckDigitAdded(product + branch + type + currency
						+ reserve + serial);
			}
			// Branch front format
			else if (getAccountOtherStart(product) < getAccountBranchStart(product)
					&& getAccountBranchStart(product) < getAccountProductStart(product)
					&& getAccountProductStart(product) < getAccountTypeStart(product)
					&& getAccountTypeStart(product) < getAccountCurrencyStart(product)
					&& getAccountCurrencyStart(product) < getAccountReserveStart(product)
					&& getAccountReserveStart(product) < getAccountSerialStart(product)
					&& getAccountSerialStart(product) < getAccountCheckStart(product))
				ret = getCheckDigitAdded(branch + product + branch + type
						+ currency + reserve + serial);
			// Currency front format
			else if (getAccountOtherStart(product) < getAccountBranchStart(product)
					&& getAccountCurrencyStart(product) < getAccountProductStart(product)
					&& getAccountProductStart(product) < getAccountBranchStart(product)
					&& getAccountBranchStart(product) < getAccountTypeStart(product)
					&& getAccountTypeStart(product) < getAccountReserveStart(product)
					&& getAccountReserveStart(product) < getAccountSerialStart(product)
					&& getAccountSerialStart(product) < getAccountCheckStart(product))
				ret = getCheckDigitAdded(branch + product + branch + type
						+ currency + reserve + serial);
			// MPU Card Format
			else if (getAccountProductStart(product) < getAccountSerialStart(product))
				ret = getCheckDigitAdded(product + serial);

			return ret;
		}

		public static String getNewAccountNumber(String lastaccount,
				String product, String branch, String type, String currency,
				String reserve, String other) {
			String ret = "";

			String serial = SharedUtil.leadZeros(0,
					getAccountSerialLen(product));
			if (!lastaccount.equalsIgnoreCase("")) {
				serial = lastaccount.substring(
						getAccountSerialStart(product) - 1,
						getAccountSerialStart(product) - 1
								+ getAccountSerialLen(product));
				serial = SharedUtil.leadZeros(""
						+ (SharedUtil.numLong(serial) + 1),
						getAccountSerialLen(product));
			}
			// LIMITATION : we need to add more IF when new sequence is
			// introduced.
			// MIT Standard format
			if (getAccountOtherStart(product) < getAccountProductStart(product)
					&& getAccountProductStart(product) < getAccountBranchStart(product)
					&& getAccountBranchStart(product) < getAccountTypeStart(product)
					&& getAccountTypeStart(product) < getAccountCurrencyStart(product)
					&& getAccountCurrencyStart(product) < getAccountReserveStart(product)
					&& getAccountReserveStart(product) < getAccountSerialStart(product)
					&& getAccountSerialStart(product) < getAccountCheckStart(product)) {
				ret = getCheckDigitAdded(product + branch + type + currency
						+ reserve + serial);
			}
			// Branch front format
			else if (getAccountOtherStart(product) < getAccountBranchStart(product)
					&& getAccountBranchStart(product) < getAccountProductStart(product)
					&& getAccountProductStart(product) < getAccountTypeStart(product)
					&& getAccountTypeStart(product) < getAccountCurrencyStart(product)
					&& getAccountCurrencyStart(product) < getAccountReserveStart(product)
					&& getAccountReserveStart(product) < getAccountSerialStart(product)
					&& getAccountSerialStart(product) < getAccountCheckStart(product))
				ret = getCheckDigitAdded(branch + product + branch + type
						+ currency + reserve + serial);
			// Currency front format
			else if (getAccountOtherStart(product) < getAccountBranchStart(product)
					&& getAccountCurrencyStart(product) < getAccountProductStart(product)
					&& getAccountProductStart(product) < getAccountBranchStart(product)
					&& getAccountBranchStart(product) < getAccountTypeStart(product)
					&& getAccountTypeStart(product) < getAccountReserveStart(product)
					&& getAccountReserveStart(product) < getAccountSerialStart(product)
					&& getAccountSerialStart(product) < getAccountCheckStart(product))
				ret = getCheckDigitAdded(branch + product + branch + type
						+ currency + reserve + serial);

			return ret;
		}
		
		// WHA ==> For Account Number Generate Order By 
		public static String getNewAccountNumberNew(String lastaccount, String product, String branch, String type, String currency,
				String reserve, String other, ArrayList<ProductNumberData> arlProductNumber){
			int lastserial = 0;
			lastserial = Integer.parseInt(lastaccount);
			String serial = SharedUtil.leadZeros(lastserial + 1,
					getAccountSerialLen(product));
			String ret = "";
			for (int i = 0; i < arlProductNumber.size(); i++) {
				if (arlProductNumber.get(i).getCode().equals("OC")) {
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(other, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("PC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(product, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("BC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(branch, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("AC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(type, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("RC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(currency, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("RW")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(reserve, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("SN")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(serial, arlProductNumber.get(i).getLength());
				}else if (arlProductNumber.get(i).getCode().equals("CD")){
					// Check digit add
					if (arlProductNumber.get(i).getLength() > 0 && arlProductNumber.get(i).getLength()==1)
						ret = getCheckDigitAdded(ret);
					else if(arlProductNumber.get(i).getLength() > 0 && arlProductNumber.get(i).getLength()==2)
						ret = getCheckDigitAdded2Digit(ret);
				} 
			}
			
			return ret;
		}
		
		// For Product ID
		public static String getProductCodeToKey(String productCode){
			String ret = "";
			for (int i=0;i<pSystemData.getProducts().size();i++)if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)) ret = pSystemData.getProducts().get(i).getProductID();
			return ret;
		}
		
		public static ProductData getProductCodeToProductData(String productCode) {
			ProductData ret = new ProductData();
			for (int i=0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i)
						.getProductCode().equals(productCode)){
					ret = pSystemData.getProducts().get(i);
					break;
				}
			}
			return ret;
		}
		//hmt
		public static ProductData getProductCodeAccountCodeToProductData(String productCode, String accountCode) {
			ProductData ret = new ProductData();
			if(accountCode.equals("")){
				for (int i=0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i)
							.getProductCode().equals(productCode)){
						ret = pSystemData.getProducts().get(i);
					}
				}
			}else{
				
				for (int i=0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)){
						if(pSystemData.getProducts().get(i).getAccType().equals("")) {
							ret = pSystemData.getProducts().get(i);
						}else if(Integer.parseInt(pSystemData.getProducts().get(i).getAccType())==Integer.parseInt(accountCode)) {
							ret = pSystemData.getProducts().get(i);
						}
						
					}
				}
			}
			return ret;
		}
		
		public static ProductData getProductCodeAccountCodeToProductData(String productCode, String accountCode,String pCurCode) {
			ProductData ret = new ProductData();			
			if(accountCode.equals("") && pCurCode.equals("")){
				for (int i=0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i)
							.getProductCode().equals(productCode)){
						ret = pSystemData.getProducts().get(i);
					}
				}
			}else if(!accountCode.equals("")){				
				for (int i=0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)){
						if(pSystemData.getProducts().get(i).getAccType().equals("")) {
							ret = pSystemData.getProducts().get(i);
						}else if(pSystemData.getProducts().get(i).getAccType().equals(accountCode)) {
							ret = pSystemData.getProducts().get(i);
						}
						
					}
				}
			}else if(!pCurCode.equals("")){				
				for (int i=0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)){
						if(pSystemData.getProducts().get(i).getCurCode1()==null) {
							ret = pSystemData.getProducts().get(i);
						}else{							
							if(pSystemData.getProducts().get(i).getCurCode1().equals(pCurCode)) 					
								ret = pSystemData.getProducts().get(i);
						}						
					}
				}
			}
			return ret;
		}
		
		public static ProductData getProductIdToProductData(String productID) {
			ProductData ret = new ProductData();
			for (int i=0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductID().equals(productID) || pSystemData.getProducts().get(i).getProductCode().equals(productID)){
					ret = pSystemData.getProducts().get(i);
				}
			}
			return ret;
		}
		// End of WHA

		public static int getAccountProductStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equalsIgnoreCase(productCode)) {
					ret = pSystemData.getProducts().get(i).getProductStart();
					break;
				}
			}
			return ret;
		}

		public static int getAccountBranchStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)
				// if(pSystemData.getBranchCodes().get(i).getCode().equals(productCode)
				) {
					ret = pSystemData.getProducts().get(i).getBranchStart();
					break;
				}
			}
			return ret;
		}
		
		public static String getProductFirstCode() {
			String ret = "";
			for (int i =0 ;i<pSystemData.getProducts().size();i++){
				if(!pSystemData.getProducts().get(i).getProductCode().equals("0000") || !pSystemData.getProducts().get(i).getProductCode().equals("00")) {
					ret = pSystemData.getProducts().get(i).getProductCode();
				}
			}			
			return ret;
		}

		public static int getAccountTypeStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getTypeStart();
					break;
				}
			}
			return ret;
		}
		
		public static int getAccountCurrencyStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getCurrencyStart();
					break;
				}
			}
			return ret;
		}

		public static int getAccountReserveStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getReserveStart();
					break;
				}
			}
			return ret;
		}
		
		public static int getAccountReserveLength(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getReserveLen();
					break;
				}
			}
			return ret;
		}

		public static int getAccountOtherStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getOtherStart();
					break;
				}
			}
			return ret;
		}

		public static int getAccountCheckStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getCheckStart();
					break;
				}
			}
			return ret;
		}

		public static int getAccountSerialStart(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getSerialLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getSerialStart();
					break;
				}
			}
			return ret;
		}

		public static int getAccountSerialLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getSerialLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getSerialLen();
					break;
				}
			}
			return ret;
		}

		public static int getAccountProductLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equalsIgnoreCase(productCode)) {
					ret = pSystemData.getProducts().get(i).getProductLen();
					break;
				}
			}
			return ret;
		}

		public static int getAccountTypeLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getTypeLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getTypeLen();
					break;
				}
			}
			return ret;
		}

		public static int getAccountBranchLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)
				// *if(pSystemData.getBranchCodes().get(i).getCode().equals(productCode)
				) {
					ret = pSystemData.getProducts().get(i).getBranchLen();
					break;
				}
			}
			return ret;
		}

		public static int getAccountBranchLen() { // Bwr Account List Smart
													// Search
			int ret = 3;
			/*
			 * for (int i=0;i<pSystemData.getProducts().size(); i++){ if
			 * (pSystemData.getProducts().get(i).getBranchLen() >0 &&
			 * pSystemData.getProducts().get(i).getProductCode().equals("01")) {
			 * ret= pSystemData.getProducts().get(i).getBranchLen(); break; } }
			 */
			return ret;
		}

		public static int getAccountBranchStart() {
			int ret = 3;
			/*
			 * for (int i=0;i<pSystemData.getProducts().size(); i++){ //Bwr
			 * Account List Smart Search if
			 * (pSystemData.getProducts().get(i).getBranchLen() >0 &&
			 * pSystemData.getProducts().get(i).getProductCode().equals("01") ){
			 * ret= pSystemData.getProducts().get(i).getBranchStart(); break; }
			 * }
			 */
			return ret;
		}

		public static int getAccountCurrencyLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getCurrencyLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getCurrencyLen();
					break;
				}
			}
			return ret;
		}

		public static int getAccountCheckLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getCurrencyLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getCheckLen();
					break;
				}
			}
			return ret;
		}
		
		public static int getAccountOtherLen(String productCode) {
			int ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getCurrencyLen() > 0
						&& pSystemData.getProducts().get(i).getProductCode()
								.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getOtherLen();
					break;
				}
			}
			return ret;
		}
		
		public static String getCheckDigitAdded(String accountnumber) {
			return accountnumber + getCheckDigit(accountnumber);
		}

		public static String getCheckDigit(String s) {
			int digit = 10 - doCheckDigit(s, true) % 10;
			String ret = "" + digit;
			return ret.substring(ret.length() - 1, ret.length());
		}
		
		public static String getCheckDigitAdded2Digit(String accountnumber) {
			String accNo = accountnumber;
			for(int i =0;i<2 ;i++){
				accNo = accNo + getCheckDigit(accNo);
			}			
			return accNo;			
		}
		
		
		public static String getCheckDigitAddedWithChar(String accountnumber, String product, String firstWord, int AC, int BC, int SN) {
			return accountnumber + getCheckDigitWithChar(accountnumber, product, firstWord, AC, BC, SN);
		}
		
		public static String getCheckDigitWithChar(String accNumber, String product, String firstWord, int AC, int BC, int SN) {
			String checkDigit = "";
			int digit = ((((AC*2) + (BC*2) + (AC*3) + (SN*4) + (3*5)) % 26 ) + (Math.abs(6-AC))) % 26;	
			if(product.equals("C")){
				digit = ( digit + 3 ) % 26;
			}else if(product.equals("S")){
				digit = ( digit + 19 ) % 26;
			}else if(product.equals("F")){
				digit = ( digit + 6 ) % 26;
			}else if(product.equals("A")){
				digit = ( digit + 19 ) % 26;
			}else if(product.equals("L")){
				digit = ( digit + 12 ) % 26;
			}else if(product.equals("U")){
				digit = ( digit + 19 ) % 26;
			}
			
			digit = ( digit + (int) firstWord.charAt(0) ) % 26;
			
			if(digit==0){
				checkDigit = "Z";
			}else{
				checkDigit = new Character((char) (digit + 64)).toString();
			}
			return checkDigit;
		}
		
		public static int doCheckDigit(String s, boolean evenPosition) {
			int sum = 0;
			for (int i = s.length() - 1; i >= 0; i--) {
				int n = Integer.parseInt(s.substring(i, i + 1));
				if (evenPosition) {
					n *= 2;
					if (n > 9) {
						n = (n % 10) + 1;
					}
				}
				sum += n;
				evenPosition = !evenPosition;
			}
			return sum;
		}

		public static boolean isValidCheckDigit(String s) {
			return doCheckDigit(s, false) % 10 == 0;
		}
		
		public static String getAccountProductCode(String accountnumber) {
			String ret = "";
			if (isCustomerAccount(accountnumber)){
				for (int i = 0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getLength() == accountnumber.length()	&& pSystemData.getProducts().get(i).getProductLen() > 0
							&& pSystemData.getProducts().get(i).getProductCode().equals(accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,
											pSystemData.getProducts().get(i).getProductStart()	+ pSystemData.getProducts()	.get(i).getProductLen()- 1))) {
						ret = accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1, pSystemData.getProducts().get(i).getProductStart()+ pSystemData.getProducts().get(i).getProductLen()- 1);
						break;
					}
				}
			} else if(isGLAccount(accountnumber)){
				ret = "GL";
			} else {
				ret = "GL";
			}	
			return ret;
		}
		
		public static String getAccountProductCodeNew(String accountnumber) {
			String ret = "";
			if (isCustomerAccountNew(accountnumber)){
				for (int i = 0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getLength() == accountnumber
							.length()
							&& pSystemData.getProducts().get(i).getProductLen() > 0
							&& pSystemData
									.getProducts()
									.get(i)
									.getProductCode()
									.equals(accountnumber.substring(
											pSystemData.getProducts().get(i)
													.getProductStart() - 1,
											pSystemData.getProducts().get(i)
													.getProductStart()
													+ pSystemData.getProducts()
															.get(i).getProductLen()
													- 1))) {
						ret = accountnumber.substring(pSystemData.getProducts()
								.get(i).getProductStart() - 1, pSystemData
								.getProducts().get(i).getProductStart()
								+ pSystemData.getProducts().get(i).getProductLen()
								- 1);
						break;
					}
				}
			} else if(isGLAccount(accountnumber)){
				ret = "GL";
			} else {
				ret = "GL";
			}	
			return ret;
		}

		public static String getAccountBranch(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber
						.length()
						&& pSystemData.getProducts().get(i).getBranchLen() > 0
						&& pSystemData
								.getProducts()
								.get(i)
								.getProductCode()
								.equals(accountnumber.substring(
										pSystemData.getProducts().get(i)
												.getProductStart() - 1,
										pSystemData.getProducts().get(i)
												.getProductStart()
												+ pSystemData.getProducts()
														.get(i).getProductLen()
												- 1))) {
					ret = accountnumber.substring(pSystemData.getProducts()
							.get(i).getBranchStart() - 1, pSystemData
							.getProducts().get(i).getBranchStart()
							+ pSystemData.getProducts().get(i).getBranchLen()
							- 1);
					break;
				}
			}
			return ret;
		}

		public static String getAccountSerial(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber
						.length()
						&& pSystemData.getProducts().get(i).getSerialLen() > 0
						&& pSystemData
								.getProducts()
								.get(i)
								.getProductCode()
								.equals(accountnumber.substring(
										pSystemData.getProducts().get(i)
												.getProductStart() - 1,
										pSystemData.getProducts().get(i)
												.getProductStart()
												+ pSystemData.getProducts()
														.get(i).getProductLen()
												- 1))) {
					ret = accountnumber.substring(pSystemData.getProducts()
							.get(i).getSerialStart() - 1, pSystemData
							.getProducts().get(i).getSerialStart()
							+ pSystemData.getProducts().get(i).getSerialLen()
							- 1);
					break;
				}
			}
			return ret;
		}

		public static String getAccountCurrency(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				
					if (pSystemData.getProducts().get(i).getLength() == accountnumber.length()
							&& pSystemData.getProducts().get(i).getCurrencyLen() > 0
							&& pSystemData.getProducts().get(i).getProductCode().equals(
							accountnumber.substring(pSystemData.getProducts().get(i).getProductStart() - 1,pSystemData.getProducts().get(i).getProductStart()
							+ pSystemData.getProducts().get(i).getProductLen()- 1))) {
						ret = accountnumber.substring(pSystemData.getProducts()
								.get(i).getCurrencyStart() - 1, pSystemData
								.getProducts().get(i).getCurrencyStart()
								+ pSystemData.getProducts().get(i).getCurrencyLen()
								- 1);
						break;
					}
			}
			return ret;
		}

		public static String getAccountType(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber
						.length()
						&& pSystemData.getProducts().get(i).getTypeLen() > 0
						&& pSystemData
								.getProducts()
								.get(i)
								.getProductCode()
								.equals(accountnumber.substring(
										pSystemData.getProducts().get(i)
												.getProductStart() - 1,
										pSystemData.getProducts().get(i)
												.getProductStart()
												+ pSystemData.getProducts()
														.get(i).getProductLen()
												- 1))) {
					ret = accountnumber
							.substring(pSystemData.getProducts().get(i)
									.getTypeStart() - 1, pSystemData
									.getProducts().get(i).getTypeStart()
									+ pSystemData.getProducts().get(i)
											.getTypeLen() - 1);
					break;
				}
			}
			return ret;
		}

		public static String getAccountReserveDigit(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber
						.length()
						&& pSystemData.getProducts().get(i).getReserveStart() > 0
						&& pSystemData
								.getProducts()
								.get(i)
								.getProductCode()
								.equals(accountnumber.substring(
										pSystemData.getProducts().get(i)
												.getProductStart() - 1,
										pSystemData.getProducts().get(i)
												.getProductStart()
												+ pSystemData.getProducts()
														.get(i).getProductLen()
												- 1))) {
					ret = accountnumber.substring(pSystemData.getProducts()
							.get(i).getReserveStart() - 1, pSystemData
							.getProducts().get(i).getReserveStart()
							+ pSystemData.getProducts().get(i).getReserveLen()
							- 1);
					break;
				}
			}
			return ret;
		}

		public static String getAccountCheckDigit(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber
						.length()
						&& pSystemData.getProducts().get(i).getCheckLen() > 0
						&& pSystemData
								.getProducts()
								.get(i)
								.getProductCode()
								.equals(accountnumber.substring(
										pSystemData.getProducts().get(i)
												.getProductStart() - 1,
										pSystemData.getProducts().get(i)
												.getProductStart()
												+ pSystemData.getProducts()
														.get(i).getProductLen()
												- 1))) {
					ret = accountnumber.substring(pSystemData.getProducts()
							.get(i).getCheckStart() - 1, pSystemData
							.getProducts().get(i).getCheckStart()
							+ pSystemData.getProducts().get(i).getCheckLen()
							- 1);
					break;
				}
			}
			return ret;
		}

		public static String getAccountOtherDigit(String accountnumber) {
			String ret = "";
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getLength() == accountnumber
						.length()
						&& pSystemData.getProducts().get(i).getOtherLen() > 0
						&& pSystemData
								.getProducts()
								.get(i)
								.getProductCode()
								.equals(accountnumber.substring(
										pSystemData.getProducts().get(i)
												.getProductStart() - 1,
										pSystemData.getProducts().get(i)
												.getProductStart()
												+ pSystemData.getProducts()
														.get(i).getProductLen()
												- 1))) {
					ret = accountnumber.substring(pSystemData.getProducts()
							.get(i).getOtherStart() - 1, pSystemData
							.getProducts().get(i).getOtherStart()
							+ pSystemData.getProducts().get(i).getOtherLen()
							- 1);
					break;
				}
			}
			return ret;
		}
		
		public static int getCollateralTable(String productCode) {
			int ret = 0;
			for (int i=0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.equals(productCode)){
					ret = (int) pSystemData.getProducts().get(i).getCollateralTable();
				}
			}
			return ret;
		}
		
		public static int getProductFeature(String productCode) {
			int ret = 0;
			for (int i=0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode().equals(productCode)){
					ret = (int) pSystemData.getProducts().get(i).getProductFeatureType();
					break;
				}
			}
			return ret;
		}
		
		public static String getProductCodesByFeature(int pFeatureType) {
			String ret = "";
			for (int i=0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductFeatureType()==pFeatureType){
					ret +=  "'" + pSystemData.getProducts().get(i).getProductCode() + "',";
				}
			}
			if(ret.length()>0){
				ret = ret.substring(0, ret.length()-1);
			}
			return ret;
		}
		
		//remove ''
		public static String getProductCode(String pProductCode){
			String ret = "";
			ret = pProductCode.replace("'", "");
			return ret;
		}
		
		public static boolean isCASA(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if ((l_ProdFeatureType == ProductFeatureType.SA) || (l_ProdFeatureType == ProductFeatureType.CA)) {
				ret = true;
			}
			return ret;
		}
		
		public static boolean isSavings(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if ((l_ProdFeatureType == ProductFeatureType.SA)) {
				ret = true;
			}
			return ret;
		}
		
		public static boolean isCurrent(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if ((l_ProdFeatureType == ProductFeatureType.CA)) {
				ret = true;
			}
			return ret;
		}
		
		public static boolean isLoan(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if (l_ProdFeatureType == ProductFeatureType.LA) {
				ret = true;
			}
			return ret;
		}
		
		public static boolean isFD(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if (l_ProdFeatureType == ProductFeatureType.FD) {
				ret = true;
			}
			return ret;
		}
		public static boolean isCall(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if (l_ProdFeatureType == ProductFeatureType.CD) {
				ret = true;
			}
			return ret;
		}
		public static boolean isSpecial(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if (l_ProdFeatureType == ProductFeatureType.SP) {
				ret = true;
			}
			return ret;
		}		
		public static boolean isUA(String productCode) {
			boolean ret = false;
			int l_ProdFeatureType = getProductFeature(productCode);
			if (l_ProdFeatureType == ProductFeatureType.UA) {
				ret = true;
			}
			return ret;
		}
		
		public static double getAccountMinBalance(String productCode) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getMinBalance();
					break;
				}
			}
			return ret;
		}
		//***
		public static double getAccountMinBalanceNew(String productCode, String pCurCode) {
			double ret = 0;boolean isbreak= false;String l_PCode= productCode;int pCurID=0;
			int l_Setting = getSystemSettingT12N2("ACTMINBAL");
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.contains(productCode)) {
					for(int ii=0;ii<pSystemData.getProducts().get(i).getProductSetupList().size() ; ii++){
						l_PCode= productCode;
						if(l_Setting ==1) {
							for(int j=0;j<pSystemData.getCurrencyCodes().size();j++){
								if(pSystemData.getCurrencyCodes().get(j).getCode().equals(pCurCode) && !pCurCode.equals("MMK"))
									pCurID = pSystemData.getCurrencyCodes().get(j).getKey();
							}
							l_PCode = productCode + (pCurID==0?"":pCurID); 
						}
						if(pSystemData.getProducts().get(i).getProductSetupList().get(ii).getProductId().equals(l_PCode)){
							ret = pSystemData.getProducts().get(i).getProductSetupList().get(ii).getMinBalance();
							isbreak = true;
							break;
						}
						if(isbreak)
							break;
					}					
				}
			}
			return ret;
		}
		
		
	
		public static double getAccountMinOpeningBalance(String productCode ) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.equals(productCode)) {
					ret = pSystemData.getProducts().get(i)
							.getMinOpeningBalance();
					break;
				}
			}
			return ret;
		}

		public static double getAccountMinWithdrawal(String productCode) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getMinWithdrawal();
					break;
				}
			}
			return ret;
		}

		public static double getAccountMultipleOfp(String productCode) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.equals(productCode)) {
					ret = pSystemData.getProducts().get(i).getMultipleOfp();
					break;
				}
			}
			return ret;
		}

		public static double getAccountMultipleOf(String productCode) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				ArrayList<ProductSetupData> l_PrdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
				for(int j = 0; j < l_PrdSetupLst.size(); j++) {
					if (l_PrdSetupLst.get(j).getProductId().equalsIgnoreCase(productCode)) {
						ret = l_PrdSetupLst.get(j).getMultipleOf();
						break;
					}
				}
				if(ret != 0) {
					break;
				}				
			}
			return ret;
		}
		
		// Original @ 20/Nov/2013
		/*public static double getAccountMultipleOf(String productCode) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				ArrayList<ProductSetupData> l_PrdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
				for(int j = 0; j < l_PrdSetupLst.size(); i++) {
					if (l_PrdSetupLst.get(j).getProductId().equals(productCode)) {
						ret = pSystemData.getProducts().get(i).getMultipleOf();
						break;
					}
				}
				if(ret != 0) {
					break;
				}				
			}
			return ret;
		}*/
		
		// WHA ==> For MultiCurrency
		public static double getAccountMinBalance(String productCode, String pCurCode) {
			double ret = 0;
			if (pSystemData.getProductDatasCurrency().size() > 0){
				for (int i = 0; i < pSystemData.getProductDatasCurrency().size(); i++) {
					if (pSystemData.getProductDatasCurrency().get(i).getProductCode()
							.equals(productCode) && pSystemData.getProductDatasCurrency()
							.get(i).getCurCode().equals(pCurCode)) {
						ret = pSystemData.getProductDatasCurrency().get(i).getMinBalance();
						break;
					}
				}
			} else {
				ret = getAccountMinBalanceNew(productCode,pCurCode);
			}
			return ret;
		}
		
		public static double getAccountMinOpeningBalance(String productCode, String pCurCode) {
			double ret = 0;
			if(pSystemData.getProductDatasCurrency().size()>0) {
				for (int i = 0; i < pSystemData.getProductDatasCurrency().size(); i++) {
					if (pSystemData.getProductDatasCurrency().get(i).getProductCode()
							.equals(productCode) && pSystemData.getProductDatasCurrency()
							.get(i).getCurCode().equals(pCurCode)) {
						ret = pSystemData.getProductDatasCurrency().get(i)
								.getMinOpeningBalance();
						break;
					}
				}
			}
			else {
				ret=getAccountMinOpeningBalance(productCode);
			}
			
			return ret;
		}

		public static double getAccountMinWithdrawal(String productCode, String pCurCode) {
			double ret = 0;
			if(pSystemData.getProductDatasCurrency().size()>0) {
				for (int i = 0; i < pSystemData.getProductDatasCurrency().size(); i++) {
					if (pSystemData.getProductDatasCurrency().get(i).getProductCode()
							.equals(productCode) && pSystemData.getProductDatasCurrency()
							.get(i).getCurCode().equals(pCurCode)) {
						ret = pSystemData.getProductDatasCurrency().get(i).getMinWithdrawal();
						break;
					}
				}
			}
			else {
				ret=getAccountMinWithdrawal(productCode);
			}
			
			return ret;
		}
		//SMM
		public static double getAccountMinWithdrawal_New(String productCode, String pCurCode) {
            double ret = 0;
            for (int i = 0; i < pSystemData.getProducts().size(); i++) {
                ArrayList<ProductSetupData> l_ProdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
                for(int j = 0; j<l_ProdSetupLst.size(); j++) {
                    if(l_ProdSetupLst.get(j).getProductId().equals(productCode)) {
                        ret = l_ProdSetupLst.get(j).getMinWithdrawal();
                        break;
                    }
                   
                }
                if(ret != 0)
                    break;
            }
            return ret;
        }
		// End of WHA
		
		public static double getAccountTotalBarAmount(ArrayList<AccBarData> arl) {
			double ret = 0;
			for (int i = 0; i < arl.size(); i++) {
				ret += arl.get(i).getBarAmount();
			}
			return ret;
		}
		
		public static boolean isUsedCheckBook(String pSheet){
			boolean ret = false;
			int result = 255;
			for (Character l_Char : pSheet.toCharArray()) {
				if (Character.isDigit((l_Char))) {
					result = Integer.parseInt(l_Char.toString());
					if (result == 1) // check Paid Cheque Or Not 
						ret = true;
					else if (result == 2)
						ret = true;
					else if (result == 3)
						ret = true;
				}
			}
			return ret;
		}
		
		public static String getCheckStatus(int p){
			String ret = "";
			if (p==0)
				ret = "Unpaid";
			else if(p==1)
				ret = "Paid";
			else if(p==2)
				ret = "Stopped";
			else if(p==3)
				ret = "Cancel";
			return ret;
		}
		
		public static DataResult checkCheckStatus(String pCheckNo, int pStatus){
			DataResult ret = new DataResult();
			ret.setStatus(true);
			if (pStatus == 1){ // For Paid Check
				ret.setStatus(false);
				ret.setDescription(pCheckNo + " is paid cheque.");
			}
			
			// For Stopped Check
			if (ret.getStatus()){
				if (pStatus == 2){
					ret.setStatus(false);
					ret.setDescription(pCheckNo + " is stopped cheque.");
				}
			}
			// For Cancel Check
			if (ret.getStatus()){
				if (pStatus == 3){
					ret.setStatus(false);
					ret.setDescription(pCheckNo + " is cancel cheque.");
				}
			}
			if (ret.getStatus()){
				if (pStatus == 255){
					ret.setStatus(false);
					ret.setDescription("Invalid cheque number.");
				}
			}
			return ret;
		}
		public static String getCashinHandGL(String pCurrencyCode) {
			String l_GLCode="";
			String l_CurrencyCode=getCurrencyDigit(pCurrencyCode);
			for(int i=0;i<pSystemData.getProducts().size();i++){
				if(l_CurrencyCode.equals(pSystemData.getProducts().get(i).getCurCode())){
					l_GLCode = pSystemData.getProducts().get(i).getCashInHandGL();
					break;
				}
			}
			if(l_GLCode.equals("")){
				if(pSystemData.getProducts().size()>0){					
					l_GLCode = pSystemData.getProducts().get(0).getCashInHandGL();										
				}
			}
			return l_GLCode;
		}
		
		public static String getCashinHandGL(ArrayList<ProductFeatureData> pData) {
			String l_GLCode="";	
			for(int i=0;i<pData.size();i++){
				if(pData.get(i).getFeatureId() == 10){
					l_GLCode = pData.get(i).getGLCode();
					break;
				}
			}
			return l_GLCode;
		}
		
		public static int getAccountlength(String pProductCode){
			int pAccNolength = 0;
			pAccNolength = getAccountProductLen(pProductCode)+getAccountBranchLen(pProductCode)+
					getAccountTypeLen(pProductCode)+getAccountCurrencyLen(pProductCode)+
					getAccountCheckLen(pProductCode)+getAccountSerialLen(pProductCode)+getAccountReserveLength(pProductCode);					
			return pAccNolength;
		}
		public static int getAccountlength(){	
			int pAccNoLength = 0;
			pAccNoLength = getAccountlength("01");	
			return pAccNoLength;
		}
		
		//New => SSH
		public static double getAccountMulipleofNew(String pProductCode,String pCurCode) {
			double ret = 0;boolean isbreak= false;String l_PCode= pProductCode;int pCurID=0;
			int l_Setting = getSystemSettingT12N2("ACTMINBAL");
			if(pSystemData.getProducts().size()>0) {
				for (int i = 0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getProductCode()
							.contains(pProductCode)) {
						ArrayList<ProductSetupData> l_ProdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
						for(int ii=0;ii<l_ProdSetupLst.size() ; ii++){
							l_PCode= pProductCode;
							if(l_Setting ==1) {
								for(int j=0;j<pSystemData.getCurrencyCodes().size();j++){
									if(pSystemData.getCurrencyCodes().get(j).getCode().equals(pCurCode) && !pCurCode.equals("MMK"))
										pCurID = pSystemData.getCurrencyCodes().get(j).getKey();
								}
								l_PCode = pProductCode + (pCurID==0?"":pCurID); 
							}
							if(l_ProdSetupLst.get(ii).getProductId().equals(l_PCode)){
								ret = l_ProdSetupLst.get(ii).getMultipleOf();
								isbreak = true;
								break;
							}
							if(isbreak)
								break;
						}					
					}
				}
			}else {
				ret=getAccountMultipleOf(pProductCode);
			}
			return ret;	
			
		}
		
		public static double getAccountMinWithdrawalNew(String pProductCode, String pCurCode) {
			double ret = 0;String l_PCode= pProductCode;int pCurID=0;boolean isbreak=false;
			int l_Setting = getSystemSettingT12N2("ACTMINBAL");
			if(pSystemData.getProducts().size()>0) {
				for (int i = 0; i < pSystemData.getProducts().size(); i++) {
					if (pSystemData.getProducts().get(i).getProductCode()
							.contains(pProductCode)) {
						ArrayList<ProductSetupData> l_ProdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();
						for(int ii=0;ii<l_ProdSetupLst.size() ; ii++){
							l_PCode= pProductCode;
							if(l_Setting ==1) {
								for(int j=0;j<pSystemData.getCurrencyCodes().size();j++){
									if(pSystemData.getCurrencyCodes().get(j).getCode().equals(pCurCode) && !pCurCode.equals("MMK"))
										pCurID = pSystemData.getCurrencyCodes().get(j).getKey();
								}
								l_PCode = pProductCode + (pCurID==0?"":pCurID); 
							}
							if(l_ProdSetupLst.get(ii).getProductId().equals(l_PCode)){
								ret = l_ProdSetupLst.get(ii).getMinWithdrawal();
								isbreak = true;
								break;
							}
							if(isbreak)
								break;
						}					
					}
				}
			}else {
				ret=getAccountMultipleOf(pProductCode);
			}
			return ret;
		}
		
		public static DataResult checkMinOpnBalanceNew(int transType,
				double amount, AccountData object) {
			DataResult ret = new DataResult();
			String l_ProductId = "";
			/*if(Ref.getSystemSettingT12N1("ACTMINBAL") == 1) {
				l_ProductId = object.getProduct() + object.getType();
			}else {*/
			l_ProductId = object.getProduct();
			//}			
			if (transType < 500) {
				double l_MinOpnBal = Account.getAccountMinOpeningBalanceNew(l_ProductId, object.getCurrencyCode());
				if (object.getStatus() == 0
						&& SharedUtil.formatMIT2DateStr(
								object.getLastTransDate()).equals("01/01/1900")) {
					if (amount < l_MinOpnBal) {
						ret.setDescription("Amount is less than Minimum Opening Balance.");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}
		
		public static double getAccountMinOpeningBalanceNew(String productCode, String pCurCode) {
			double ret = 0;boolean isbreak= false;String l_PCode= productCode;int pCurID=0;
			int l_Setting = getSystemSettingT12N2("ACTMINBAL");
			if(pSystemData.getProducts().size()>0 ) {
				for (int i = 0; i < pSystemData.getProducts().size(); i++) {					
					ArrayList<ProductSetupData> l_ProdSetupLst = pSystemData.getProducts().get(i).getProductSetupList();						
					for(int ii=0;ii<l_ProdSetupLst.size() ; ii++){
						if(l_ProdSetupLst.get(ii).getProductId().equals(productCode)){
							l_PCode= productCode;
							if(l_Setting ==1) {
								for(int j=0;j<pSystemData.getCurrencyCodes().size();j++){
									if(pSystemData.getCurrencyCodes().get(j).getCode().equals(pCurCode) && !pCurCode.equals("MMK"))
										pCurID = pSystemData.getCurrencyCodes().get(j).getKey();
								}
								l_PCode = productCode + (pCurID==0?"":pCurID); 
							}
							if(l_ProdSetupLst.get(ii).getProductId().equals(l_PCode)){
								ret = l_ProdSetupLst.get(ii).getMinOpeningBalance();
								isbreak = true;
								break;
							}
							if(isbreak)
								break;
						}
						/*  
						else
							break;	
							*/						
					}					
				}
			}else {
				ret=getAccountMinOpeningBalance(productCode);
			}
			return ret;							
		}
		
		//HNNS add Account Number Char
		public static String getNewAccountNumberWithChar(String lastaccount, String product, String branch, String type, String currency,
				String reserve, String other, ArrayList<ProductNumberData> arlProductNumber){
			int lastserial = 0;
			lastserial = Integer.parseInt(lastaccount) + 1;
			String serial = SharedUtil.leadZeros(lastserial, getAccountSerialLen(product));
			String ret = "";
			for (int i = 0; i < arlProductNumber.size(); i++) {
				if (arlProductNumber.get(i).getCode().equals("OC")) {
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(other, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("PC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(product, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("BC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(branch, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("AC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(type, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("RC")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(currency, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("RW")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(reserve, arlProductNumber.get(i).getLength());
				} else if (arlProductNumber.get(i).getCode().equals("SN")){
					// Checking Length
					if (arlProductNumber.get(i).getLength() > 0)
						ret = ret + SharedUtil.leadZeros(serial, arlProductNumber.get(i).getLength());
				} 
			}
			ret = getCheckDigitAddedWithChar(ret, product, other, Integer.parseInt(type), Integer.parseInt(branch), lastserial);
			return ret;
		}
		
		//added
		public static double getAccountMinBalanceNew(String productCode) {
			double ret = 0;
			for (int i = 0; i < pSystemData.getProducts().size(); i++) {
				if (pSystemData.getProducts().get(i).getProductCode()
						.equals(productCode)) {
					if(pSystemData.getProducts().get(i).getProductSetupList().size()>0)
						ret = pSystemData.getProducts().get(i).getProductSetupList().get(0).getMinBalance();
					break;
				}
			}
			return ret;
		}
	
	}
	
	public static int getSystemSettingT12N5(String pT1){
		int ret = 0;
		for (int i=0; i < pSystemData.getSystemSettingDatas().size(); i++){
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)){
				ret = pSystemData.getSystemSettingDatas().get(i).getN5();
			}
		}
		return ret;
	}
	
	public static int getSystemSettingT12N6(String pT1){
		int ret = 0;
		for (int i=0; i < pSystemData.getSystemSettingDatas().size(); i++){
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)){
				ret = pSystemData.getSystemSettingDatas().get(i).getN6();
			}
		}
		return ret;
	}
	
	public static int getSystemSettingT12N7(String pT1){
		int ret = 0;
		for (int i=0; i < pSystemData.getSystemSettingDatas().size(); i++){
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)){
				ret = pSystemData.getSystemSettingDatas().get(i).getN7();
			}
		}
		return ret;
	}
	
	public static class Transaction{

		
		public static boolean isCrossCurrencyTransfer(ArrayList<TransactionData> pArlTransactionDatas) {
			boolean result = false;
			
			String l_FromCurrency = "";
			String l_ToCurrency = "";
			
			if (pArlTransactionDatas.size() != 1) {
				if (pArlTransactionDatas.size() < 2) {
					l_FromCurrency = pArlTransactionDatas.get(0).getCurrencyCode();
					l_ToCurrency = pArlTransactionDatas.get(1).getCurrencyCode();
				} else {
					l_FromCurrency = pArlTransactionDatas.get(0).getCurrencyCode();
					l_ToCurrency = pArlTransactionDatas.get(pArlTransactionDatas.size()-1).getCurrencyCode();
				}
			}
			
			if (!l_FromCurrency.equals(l_ToCurrency)) 
				result = true;
		
			return result;
		}
		
		public static double getBaseAmount(ArrayList<TransactionData> pArlTransactionDatas) {
			double ret = 0;
			for (int i=0; i<pArlTransactionDatas.size(); i++) {
				if (pArlTransactionDatas.get(i).getCurrencyCode().equals(SharedLogic.getBaseCurCode())) {
					ret = pArlTransactionDatas.get(i).getAmount();
					break;
				}
			}
			return ret;
		}
		
		public static DataResult checkAccStatus(int transType, AccountData object,ArrayList<ReferenceData> plstStatus) {
			DataResult ret = new DataResult();
			ret = checkAccStatus(transType, 0, object,plstStatus);
			return ret;
		}
		
		public static DataResult checkAccStatus(int transType, double transAmount,
				AccountData object,ArrayList<ReferenceData> plstStatus) {
			DataResult ret = new DataResult();
			
			// For Debit Case
			if (transType > 500) {
				switch (object.getStatus()) {
				// New (or) Active
				case 0:
					// New Account
					
					if (SharedLogic.Account.isLoan(object.getProduct())) {
						ret.setStatus(true); 
						break;
					}
					
					if (SharedUtil.formatMIT2DateStr(object.getLastTransDate())
							.equals("01/01/1900")) {
						ret.setDescription("Debit Account Status is New.");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
					break;
					
					// Suspend Accont
	
				case 1:
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object, plstStatus) + ".");
					ret.setStatus(false);
					break;
				// Close Pending
				case 2:
					if (transAmount != object.getCurrentBalance()) {
						ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
								+ getAccountStatusDescription(object, plstStatus) + ".");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
					break;
				// Closed
				case 3:
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object,plstStatus) + ".");
					ret.setStatus(false);
					break;
				// Dormant
				case 4:
					ret.setDescription(object.getAccountNumber()+ "Debit Account is "
							+ getAccountStatusDescription(object,plstStatus) + ".");
					ret.setStatus(false);
					break;
				// Stopped Payment
				case 7:
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object,plstStatus) + ".");
					ret.setStatus(false);
					break;
				default:
					break;
				}
				
			} else if (transType < 500) {
				// For Credit Case
				switch (object.getStatus()) {
				// New (or) Active
				case 0:
					ret.setStatus(true);
					break;
				// Suspend Accont
				case 1:
					ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
							+ getAccountStatusDescription(object,plstStatus) + ".");
					ret.setStatus(false);
					break;
				// Close Pending
				case 2:
					ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
							+ getAccountStatusDescription(object,plstStatus) + ".");
					ret.setStatus(false);
					break;
				// Closed
				case 3:
					ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
							+ getAccountStatusDescription(object,plstStatus) + ".");
					ret.setStatus(false);
					break;
				//Dormant
				case 4:
					ret.setStatus(true);
						break;
				// Stopped Payment
				case 7:
					ret.setStatus(true);
					break;
				default:
					break;
				}

			}
			return ret;
		}

		public static DataResult checkMultipleOf(double amount,String productCode,String pCurCode) {
			DataResult ret = new DataResult();
			double l_MultipleOf = Account.getAccountMulipleofNew(productCode, pCurCode);
			if ((int) (amount % l_MultipleOf) != 0) {
				ret.setDescription("Amount not multiple of " + l_MultipleOf);
				ret.setStatus(false);
			} else
				ret.setStatus(true);
			return ret;
		}

		public static DataResult checkMinWithdrawal(int transType,
				double amount, String productCode) {
			DataResult ret = new DataResult();
			if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = Account
						.getAccountMinWithdrawal(productCode);
				if (amount < l_MinWithdrawal) {
					ret.setDescription("Amount is less than minimum withdrawal amount "
							+ l_MinWithdrawal);
					ret.setStatus(false);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}
		
		public static DataResult checkMinOpnBalance(int transType,
				double amount, AccountData object) {
			DataResult ret = new DataResult();
			String l_ProductID = "";
			if(getSystemSettingT12N1("ACTMINBAL") == 1) {
				l_ProductID = object.getProduct() + object.getType();
			}else {
				l_ProductID = object.getProduct();
			}			
			if (transType < 500) {
				double l_MinOpnBal = Account.getAccountMinOpeningBalanceNew(l_ProductID, object.getCurrencyCode());
				if (object.getStatus() == 0
						&& SharedUtil.formatMIT2DateStr(
								object.getLastTransDate()).equals("01/01/1900")) {
					if (amount < l_MinOpnBal) {
						ret.setDescription("Amount is less than Minimum Opening Balance.");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}

		// WHA ==> For MultiCurreny
		public static DataResult checkMinWithdrawal(int transType,
				double amount, String productCode, String pCurCode) {
			DataResult ret = new DataResult();
			if (transType > 500 && transType != 969) {
				double l_MinWithdrawal = Account.getAccountMinWithdrawal_New(productCode, pCurCode);
				if (amount < l_MinWithdrawal) {
					ret.setDescription("Amount is less than minimum withdrawal amount "
							+ l_MinWithdrawal);
					ret.setStatus(false);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}
		
		public static DataResult checkMinOpnBalanceCurrency(int transType,
				double amount, AccountData object) {
			DataResult ret = new DataResult();
			String l_ProductId = "";
			if(getSystemSettingT12N1("ACTMINBAL") == 1)
				l_ProductId= object.getProduct() + object.getType();
			else
				l_ProductId = object.getProduct();
				       
			if (transType < 500) {
				double l_MinOpnBal = Account.getAccountMinOpeningBalanceNew(l_ProductId, object.getCurrencyCode());
				if (object.getStatus() == 0
						&& SharedUtil.formatMIT2DateStr(
								object.getLastTransDate()).equals("01/01/1900")) {
					if (amount < l_MinOpnBal) {
						ret.setDescription("Amount is less than Minimum Opening Balance.");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}
		// End of WHA
	
		public static DataResult checkProductType(int transType,
				String productCode) {
			DataResult ret = new DataResult();
			/*
			 * if (productCode.equals("01") || productCode.equals("02") ||
			 * productCode.equals("95")) { ret.setStatus(true); } else {
			 * ret.setStatus(false);
			 * ret.setDescription("Allow Only Savings and Current Account"); }
			 */
			ret.setStatus(true);
			return ret;
		}

		public static DataResult checkTransaction(TransactionData pTransData,
				AccountData pAccData,ArrayList<ReferenceData> plstStatus) {
			DataResult ret = new DataResult();
			String l_ProductID = "";
			if(getSystemSettingT12N1("ACTMINBAL")== 1) {
				l_ProductID = pAccData.getProduct() + pAccData.getType();
			}else {
				l_ProductID = pAccData.getProduct();
			}
			
			// For Debit Case
			if (isDebit(pTransData.getTransactionType())) {
				// Check Cash Or Transfer
				if (pTransData.getTransactionType() > 500
						&& pTransData.getTransactionType() < 699) { // For Debit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pTransData.getAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					// Check Minimum Withdrawal
					if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), l_ProductID,pAccData.getCurrencyCode());
					
					// To Check Cheque Number
				} else if (pTransData.getTransactionType() > 700
						&& pTransData.getTransactionType() < 999) { // For Debit
																	// Transfer
																	// Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pTransData.getAmount(), 
								pAccData,plstStatus);
					// Check Minimum Withdrawal
					if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), l_ProductID,pAccData.getCurrencyCode());
					// Check Minimum Withdrawal with Currecny
					
					// To Check Cheque Number
				} else { // For Clearing Case
					ret.setStatus(true);
				}
				
				// Checking Max ATM Transaction Amount
				/*if (ret.getStatus()){
					if (checkTransTypeForCommission(pTransData)){
						ret = checkATMMaxAmount(pTransData);
					}
				}*/
				
			} else { // For Credit Case
				if (pTransData.getTransactionType() > 0
						&& pTransData.getTransactionType() < 200) { // For
																	// Credit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getType());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pTransData.getAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalance(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData);
				} else if (pTransData.getTransactionType() > 200
						&& pTransData.getTransactionType() <= 500) { // For
																		// Credit
																		// Transfer
																		// Transaction
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pTransData.getAmount(),
								pAccData,plstStatus);
				} else { // For Clearing Case
					ret.setStatus(true);
				}
			}
			return ret;
		}
		
		public static DataResult checkODTransaction(TransactionData pTransData,
				AccountData pAccData, AccountGLTransactionData pAccGLTransData,ArrayList<ReferenceData> plstStatus) {
			DataResult ret = new DataResult();
			String l_ProductID = "";
			if(getSystemSettingT12N1("ACTMINBAL")== 1) {
				l_ProductID = pAccData.getProduct() + pAccData.getType();
			}else {
				l_ProductID = pAccData.getProduct();
			}
			/*int curId = 0;
			if(getSystemSettingT12N2("ACTMINBAL") ==  1 ) {
				for(int k = 0;k<pSystemData.getCurrencyCodes().size();k++){
					if(pSystemData.getCurrencyCodes().get(k).getCode().equals(pAccData.getCurrencyCode()) && !pAccData.getCurrencyCode().equals("MMK")){
						curId = pSystemData.getCurrencyCodes().get(k).getKey();break;
					}
				}
				l_ProductID = l_ProductID+(curId==0?"":curId);
			}*/
			// For Debit Case
			if (isDebit(pTransData.getTransactionType())) {
				// Check Cash Or Transfer
				if (pTransData.getTransactionType() > 500
						&& pTransData.getTransactionType() < 699) { // For Debit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					// Check Minimum Withdrawal
					/*if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData.getProduct());*/
					
					// Check Minimum Withdrawal with Currency
					if (ret.getStatus())
					ret = checkMinWithdrawal(
							pTransData.getTransactionType(),
							pTransData.getAmount(), l_ProductID, pAccGLTransData.getTrCurCode());
					
					// To Check Cheque Number
				} else if (pTransData.getTransactionType() > 700
						&& pTransData.getTransactionType() < 999) { // For Debit
																	// Transfer
																	// Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Minimum Withdrawal
					/*if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData.getProduct());*/
					// Check Minimum Withdrawal with Currecny
					
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					//mmmyint
					if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								(pAccGLTransData.getTrPrevBalance() - pAccGLTransData.getTrAmount()), l_ProductID, pAccGLTransData.getTrCurCode());
					
					// To Check Cheque Number
				} else { // For Clearing Case
					ret.setStatus(true);
				}
				
			} else { // For Credit Case
				if (pTransData.getTransactionType() > 0
						&& pTransData.getTransactionType() < 200) { // For
																	// Credit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getType());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					
					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalanceCurrency(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), pAccData);
				} else if (pTransData.getTransactionType() > 200
						&& pTransData.getTransactionType() <= 500) { // For
																		// Credit
																		// Transfer
																		// Transaction
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());		

					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalanceCurrency(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), pAccData);
				
				} else { // For Clearing Case
					ret.setStatus(true);
				}
			}
			return ret;
		}
	//wmmh
		public static DataResult checkCashInHandGL(String pAccNumber){
			DataResult ret = new DataResult();
			ret.setStatus(true); 
			for(int i=0;i<pSystemData.getCurrencyCodes().size();i++){
			String glcode = "";
			glcode = Account.getCashinHandGL(pSystemData.getCurrencyCodes().get(i).getCode().toString());
				if(pAccNumber.equals(glcode)){
					ret.setStatus(false);
					ret.setDescription("Transaction doesn't allow with Cash In Hand(GL).");
					break;
				}
			}
			return ret;
		}		
		
		// WHA ==> For MultiCurrency CheckTransaction
		public static DataResult checkTransaction(TransactionData pTransData,
				AccountData pAccData, AccountGLTransactionData pAccGLTransData,ArrayList<ReferenceData> plstStatus) {
			DataResult ret = new DataResult();
			String l_ProductID = "";
			if(getSystemSettingT12N1("ACTMINBAL")== 1) {
				l_ProductID = pAccData.getProduct() + pAccData.getType();
			}else {
				l_ProductID = pAccData.getProduct();
			}
			/*int curId = 0;
	        if(getSystemSettingT12N2("ACTMINBAL") ==  1 ) {
		        for(int k = 0;k<pSystemData.getCurrencyCodes().size();k++){
		        	if(pSystemData.getCurrencyCodes().get(k).getCode().equals(pAccData.getCurrencyCode()) && !pAccData.getCurrencyCode().equals("MMK")){
		        		curId = pSystemData.getCurrencyCodes().get(k).getKey();break;
		        	}
		        }
		        l_ProductID = l_ProductID+(curId==0?"":curId);
	        }*/
			// For Debit Case
			if (isDebit(pTransData.getTransactionType())) {
				// Check Cash Or Transfer
				if (pTransData.getTransactionType() > 500
						&& pTransData.getTransactionType() < 699) { // For Debit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					// Check Minimum Withdrawal
					/*if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData.getProduct());*/
					
					// Check Minimum Withdrawal with Currency
					if (ret.getStatus())
					ret = checkMinWithdrawal(
							pTransData.getTransactionType(),
							pTransData.getAmount(), l_ProductID, pAccGLTransData.getTrCurCode());
					
					// To Check Cheque Number
				} else if (pTransData.getTransactionType() > 700
						&& pTransData.getTransactionType() < 999) { // For Debit
																	// Transfer
																	// Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Minimum Withdrawal
					/*if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData.getProduct());*/
					// Check Minimum Withdrawal with Currecny
					
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					if (ret.getStatus()){	//ZZM						
						ret = checkMinWithdrawal( 
								pTransData.getTransactionType(),//Hnns Fixed 
								(pAccGLTransData.getTrAmount()), l_ProductID, pAccGLTransData.getTrCurCode());
						
					
					}// To Check Cheque Number
				} else { // For Clearing Case
					ret.setStatus(true);
				}
				
			} else { // For Credit Case
				if (pTransData.getTransactionType() > 0
						&& pTransData.getTransactionType() < 200) { // For
																	// Credit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getType());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					
					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalanceCurrency(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), pAccData);
				} else if (pTransData.getTransactionType() > 200
						&& pTransData.getTransactionType() <= 500) { // For
																		// Credit
																		// Transfer
																		// Transaction
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData,plstStatus);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());		

					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalanceCurrency(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), pAccData);
				
				} else { // For Clearing Case
					ret.setStatus(true);
				}
			}
			return ret;
		}
		// End of WHA
		
		public static DataResult checkTransaction(TransactionData pTransData,
				AccountData pAccData, AccountGLTransactionData pAccGLTransData) {
			DataResult ret = new DataResult();
			String l_ProductID = "";
			if(getSystemSettingT12N1("ACTMINBAL")== 1) {
				l_ProductID = pAccData.getProduct() + pAccData.getType();
			}else {
				l_ProductID = pAccData.getProduct();
			}
	        
			// For Debit Case
			if (isDebit(pTransData.getTransactionType())) {
				// Check Cash Or Transfer
				if (pTransData.getTransactionType() > 500
						&& pTransData.getTransactionType() < 699) { // For Debit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(), pAccData);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					// Check Minimum Withdrawal
					/*if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData.getProduct());*/
					
					// Check Minimum Withdrawal with Currency
					if (ret.getStatus())
					ret = checkMinWithdrawal(
							pTransData.getTransactionType(),
							pTransData.getAmount(), l_ProductID, pAccGLTransData.getTrCurCode());
					
					// To Check Cheque Number
				} else if (pTransData.getTransactionType() > 700
						&& pTransData.getTransactionType() < 999) { // For Debit
																	// Transfer
																	// Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(),pTransData.getAmount(),pAccData);
					// Check Minimum Withdrawal
					/*if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pTransData.getAmount(), pAccData.getProduct());*/
					// Check Minimum Withdrawal with Currecny
					if (ret.getStatus())
						ret = checkMinWithdrawal(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), l_ProductID, pAccGLTransData.getTrCurCode());
					
					// To Check Cheque Number
				} else { // For Clearing Case
					ret.setStatus(true);
				}
				
			} else { // For Credit Case
				if (pTransData.getTransactionType() > 0
						&& pTransData.getTransactionType() < 200) { // For
																	// Credit
																	// Cash Case
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getType());
					// Check A/C Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData);
					// Check Multiple Of
					if (ret.getStatus())
						ret = checkMultipleOf(pTransData.getAmount(),
								l_ProductID,pAccData.getCurrencyCode());
					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalanceCurrency(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), pAccData);
				} else if (pTransData.getTransactionType() > 200
						&& pTransData.getTransactionType() <= 500) { // For
																		// Credit
																		// Transfer
																		// Transaction
					// Check Product Type
					ret = checkProductType(pTransData.getTransactionType(),
							pAccData.getProduct());
					// Check Status
					if (ret.getStatus())
						ret = checkAccStatus(pTransData.getTransactionType(), pAccGLTransData.getTrAmount(),
								pAccData);
					
					// Check Min Opening Balance
					if (ret.getStatus())
						ret = checkMinOpnBalanceCurrency(
								pTransData.getTransactionType(),
								pAccGLTransData.getTrAmount(), pAccData);
				
				} else { // For Clearing Case
					ret.setStatus(true);
				}
			}
			return ret;
		}	
		

		// WHA ==> Checking TransType For ATM Commission
		public static boolean checkTransTypeForCommission(TransactionData pData){
			boolean ret = false;
			try {
				if (pData.getTransactionType() >= 590 && pData.getTransactionType() <= 592)
					ret = true;
				else if (pData.getTransactionType() >= 951 && pData.getTransactionType() <= 968)
					ret = true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return ret;
		}
		
		public static DataResult checkATMMaxAmount(TransactionData pData){
			DataResult ret = new DataResult();
			ret.setStatus(true);
			
			
			if (pData.getAmount() > pSystemData.getATMMaxTransAmount()){
				ret.setStatus(false);
				ret.setDescription("ATM max transaction amount is 300,000.");
			}
			return ret;
		}
		
		private static DenominationData derivedDeno(DenominationData fromObj, DenominationData toObj){
			switch (fromObj.getDenoMoneyType()) {
			case 0:	// For Kyat Case
				if (fromObj.getSize() == 5000){
					toObj.setK5000((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 1000){
					toObj.setK1000((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 500){
					toObj.setK500((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 200){
					toObj.setK200((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 100){
					toObj.setK100((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 90){
					toObj.setK90((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 50){
					toObj.setK50((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 45){
					toObj.setK45((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 20){
					toObj.setK20((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 15){
					toObj.setK15((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 10){
					toObj.setK10((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 5){
					toObj.setK5((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 1){
					toObj.setK1((long)fromObj.getQuantity());
				}
				break;
			case 1:  // For Coin Case
				if (fromObj.getSize() == 100){
					toObj.setC100((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 50){
					toObj.setC50((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 10){
					toObj.setC10((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 5){
					toObj.setC5((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 1){
					toObj.setC1((long)fromObj.getQuantity());
				}
				break;
				
			case 2:	 // For Pyar Case
				if (fromObj.getSize() == 50){
					toObj.setP50((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 25){
					toObj.setP25((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 10){
					toObj.setP10((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 5){
					toObj.setP5((long)fromObj.getQuantity());
				}
				if (fromObj.getSize() == 1){
					toObj.setP1((long)fromObj.getQuantity());
				}
				break;
			default:
				break;
			}

			// For Checking Deno and Refund Size
			if(fromObj.getDenoType() != 1) // Denomination
				toObj.setSize(toObj.getSize() + fromObj.getSize());
			else if(fromObj.getDenoType() == 1 && fromObj.getQuantity() > 0) // Refund
				toObj.setSize(toObj.getSize() + fromObj.getSize());

			return toObj;
		}
		
		public static ArrayList<DenominationData> getDenominationData(ArrayList<DenominationData> arl){
			ArrayList<DenominationData> ret = new ArrayList<DenominationData>();
			DenominationData denoRefundObj = new DenominationData();
			DenominationData denoObj = new DenominationData();
			for (int i=0; i < arl.size(); i ++){
				DenominationData obj = new DenominationData();
				obj = arl.get(i);
				if (obj.getDenoType() == 0) {			// For Not Refund Case
					derivedDeno(obj, denoObj);
					denoObj.setDenoType((short)0);
				} else if (obj.getDenoType() == 1){		// For Refund Case
					derivedDeno(obj, denoRefundObj);
					denoRefundObj.setDenoType((short)1);
				}
			}
			ret.add(denoObj);
			
			// Checking Refund Exist Or Not
			if (denoRefundObj.getSize() > 0)
				ret.add(denoRefundObj);
			return ret;
		}
		
		public static ArrayList<RefDenominationData> getRefDenominationDatas(){
			return pSystemData.getRefDenominationDatas();
		}
		
		public static String getTransTypeDescription(TransactionData pdata) {
			String l_description="";
			if (pdata.getStatus() < 254 && pdata.getTransactionType() == 270 || pdata.getTransactionType() == 220){
				l_description="INT";
			}
			
			if (pdata.getStatus() < 254 && pdata.getTransactionType() <= 199){
				l_description="CD";
			}
			
			if (pdata.getStatus() < 254 && pdata.getTransactionType() >= 500 && pdata.getTransactionType() <= 699){
				l_description="CW";
			}
			
			if (pdata.getStatus() < 254 && pdata.getTransactionType() >= 700 && pdata.getTransactionType() <= 998){
				l_description="TW";
			}
			
			if (pdata.getStatus() < 254 && pdata.getTransactionType() > 200 && pdata.getTransactionType() <= 499){
				l_description="TD";
			}
			
			if (pdata.getStatus() > 253){
				l_description="REV";
			}
			
			if (pdata.getStatus() < 254 && pdata.getTransactionType() == 200){
				l_description="CLD";
			}
			
			if (pdata.getStatus() < 254 && pdata.getTransactionType() == 0){
				l_description="CB";
			}
			return l_description;
		}
		
		public static DataResult checkAccStatus(int transType, AccountData object) {
			DataResult ret = new DataResult();
			ret = checkAccStatus(transType, 0, object);
			return ret;
		}
		
		public static DataResult checkAccStatus(int transType, double transAmount,
				AccountData object) {
			DataResult ret = new DataResult();
			
			// For Debit Case
			if (transType > 500) {
				switch (object.getStatus()) {
				// New (or) Active
				case 0:
					// New Account
					
					if (SharedLogic.Account.isLoan(object.getProduct())) {
						ret.setStatus(true); 
						break;
					}
					
					if (SharedUtil.formatMIT2DateStr(object.getLastTransDate())
							.equals("01/01/1900")) {
						ret.setDescription("Debit Account Status is New.");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
					break;
					
					// Suspend Accont

				case 1:
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				// Close Pending
				case 2:
					if (transAmount != object.getCurrentBalance()) {
						ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
								+ getAccountStatusDescription(object) + ".");
						ret.setStatus(false);
					} else
						ret.setStatus(true);
					break;
				// Closed
				case 3:
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				// Dormant
				case 4:
					ret.setDescription(object.getAccountNumber()+ "Debit Account is "
							+ getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				// Stopped Payment
				case 7:
					ret.setDescription(object.getAccountNumber()+" Debit Account Status is "
							+ getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				default:
					break;
				}
				
			} else if (transType < 500) {
				// For Credit Case
				switch (object.getStatus()) {
				// New (or) Active
				case 0:
					ret.setStatus(true);
					break;
				// Suspend Accont
				case 1:
					ret.setDescription(object.getAccountNumber()+" Credit Account Status is "
							+ Ref.getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				// Close Pending
				case 2:
					ret.setDescription(object.getAccountNumber()+"Credit Account Status is "
							+ Ref.getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				// Closed
				case 3:
					ret.setDescription(object.getAccountNumber()+"Credit Account Status is "
							+ Ref.getAccountStatusDescription(object) + ".");
					ret.setStatus(false);
					break;
				//Dormant
				case 4:
					ret.setStatus(true);
						break;
				// Stopped Payment
				case 7:
					ret.setStatus(true);
					break;
				default:
					break;
				}

			}
			return ret;
		}

		//mmmyint 08.08.2018 icbs 3.0 to 3.1
		public static DataResult checkMultipleOf(double amount,ProductSetupData pPrdSetUpData) {
			DataResult ret = new DataResult();
			double l_MultipleOf = 0;
			
			l_MultipleOf = pPrdSetUpData.getMultipleOf();
			if ((int) (amount % l_MultipleOf) != 0) {
				ret.setDescription("Amount not multiple of " + l_MultipleOf);
				ret.setStatus(false);
				ret.setErrorFlag(1);
			} else
				ret.setStatus(true);
			return ret;
		}
		
		public static DataResult checkMinWithdrawal(int transType,
				double amount, ProductSetupData pPrdSetUpData) {
			DataResult ret = new DataResult();
			double l_MinWithdrawal = 0;
			if (transType > 500 && transType != 969) {
				l_MinWithdrawal= pPrdSetUpData.getMinWithdrawal();
						
				if (amount < l_MinWithdrawal) {
					ret.setDescription("Amount is less than minimum withdrawal amount "
							+ l_MinWithdrawal);
					ret.setStatus(false);
					ret.setErrorFlag(1);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}
		public static DataResult checkMinOpnBalance(int transType,
				double amount, ProductSetupData pPrdSetUpData, AccountData object) {
			DataResult ret = new DataResult();
			double l_MinOpnBal = 0;
			if (transType < 500) {
				l_MinOpnBal = pPrdSetUpData.getMinOpeningBalance();
				if (object.getStatus() == 0 
						&& SharedUtil.formatMIT2DateStr(
								object.getLastTransDate()).equals("01/01/1900")) {
					if (amount < l_MinOpnBal) {
						ret.setDescription("Amount is less than Minimum Opening Balance.");
						ret.setStatus(false);
						ret.setErrorFlag(1);
					} else
						ret.setStatus(true);
				} else
					ret.setStatus(true);
			} else
				ret.setStatus(true);
			return ret;
		}
		
	
	}
	
	
	
	public static String getAccountStatusDescription(AccountData object){
		String ret  = "";
		// Account Status
        switch (object.getStatus())
        {
            // New (or) Active
            case 0:
                if (SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900"))
                    ret = "New.";
               else
                    ret = "Active";
                break;

            // Suspend
            case 1:
                ret = "Suspend";
                break;

            // Closed Pending
            case 2:
                ret = "Close Pending";
                break;

            // Closed
            case 3:
            	ret = "Closed";
                break;

            // Stopped Payment
            case 7:
            	ret = "StoppedPayment";
                break;

            default:
            	ret = "";
                break;
        }
        return ret;
    }
	
	public static String getSystemSettingT12T4(String pT1){
		String ret = "";
		for (int i=0; i < pSystemData.getSystemSettingDatas().size(); i++){
			if (pSystemData.getSystemSettingDatas().get(i).getT1().equals(pT1)){
				ret = pSystemData.getSystemSettingDatas().get(i).getT4();
			}
		}
		return ret;
	}
}
