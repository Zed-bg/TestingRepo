package com.nirvasoft.rp.mgr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.core.SingletonConnection;
import com.nirvasoft.rp.core.SingletonServer;
import com.nirvasoft.rp.util.LogicMgr;
import com.nirvasoft.rp.dao.AccountExtendedDAO;
import com.nirvasoft.rp.core.ccbs.dao.AccountGroupDAO;
import com.nirvasoft.rp.core.ccbs.dao.CAJunctionDAO;
import com.nirvasoft.rp.core.ccbs.dao.CallDepositAccountDAO;
import com.nirvasoft.rp.core.ccbs.dao.CustomerDAO;
import com.nirvasoft.rp.core.ccbs.dao.GLDAO;
import com.nirvasoft.rp.core.ccbs.dao.PBHeaderDAO;
import com.nirvasoft.rp.dao.ProductsDAO;
import com.nirvasoft.rp.core.ccbs.dao.SpecialDepositAccountDAO;
import com.nirvasoft.rp.core.ccbs.data.db.DBFixedDepositMgr;
import com.nirvasoft.rp.dao.MobileUserDao;
import com.nirvasoft.rp.dao.AccBarDAO;
import com.nirvasoft.rp.dao.AccountsDAO;
import com.nirvasoft.rp.dao.CurrentAccountDAO;
import com.nirvasoft.rp.dao.FixedDepositAccountDAO;
import com.nirvasoft.rp.dao.LoanAccountDAO;
import com.nirvasoft.rp.dao.PBFixedDepositAccountDAO;
import com.nirvasoft.rp.dao.UniversalAccountDAO;
import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.FixedDepositAccountData;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ProductNumberData;
import com.nirvasoft.rp.shared.ProductTypeData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.SignatureData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;
import com.nirvasoft.rulesengine.Rules;
import com.nirvasoft.rulesengine.Ruleset;

public class DBAccountMgr {

	public static double getAvailableBalance(String pAccNumber, int pProcessType, Connection pConn) throws IOException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException {
		double l_AvailableBalance = 0;
		double l_ProdMinBalance = 0;
		double l_BarAmount = 0;
		double l_TotalOD = 0;
		AccountData l_AccData = new AccountData();
		AccountsDAO l_AccDAO = new AccountsDAO(pConn);
		AccBarDAO l_AccBarDAO = new AccBarDAO();
		CurrentAccountDAO l_CurAccDAO = new CurrentAccountDAO();
		AccountData l_CurAccData = new AccountData();
			
		String l_TodayDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		// Checking Product Type
		if(l_AccDAO.getAccount(pAccNumber, pConn));
		l_AccData = l_AccDAO.getAccountsBean();
		String productCode = l_AccData.getProduct();
		int l_RemoveBar=SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN3();
		
		if (SharedLogic.isCurrent(productCode)) {
			
			if(l_CurAccDAO.getAccount(pAccNumber, pConn)){
				// Current Account
				l_CurAccData = l_CurAccDAO.getCurrentAccBean();
				
				if(l_CurAccData.getIsOD() == 1){
					// OD Account
					l_AccData = l_AccDAO.getODAvailableBalance(l_TodayDate, pAccNumber, pProcessType, pConn);
					
					// Get Bar Amount
					l_BarAmount = l_AccBarDAO.getTotalBarAmount(pAccNumber, pConn);
					
					// Getting Product Minimum Amount
					if( pProcessType!=4){
						l_ProdMinBalance = SharedLogic.getAccountMinBalanceNew(productCode,l_CurAccData.getCurrencyCode());
					}else
						l_ProdMinBalance = 0;					
					
					if (l_BarAmount != 0 ) {
						l_ProdMinBalance = 0;
					}					
					if(l_RemoveBar==1 && pProcessType==4){
						l_BarAmount=0;
					}						
					l_TotalOD = 0;
					
					if (l_AccData.getOdLimit() > 0 || l_AccData.getTODLimit() > 0 ) {
						l_TotalOD = l_AccData.getOdLimit() + l_AccData.getTODLimit();
						l_ProdMinBalance = 0;
					}
					
					l_AvailableBalance = l_AccData.getCurrentBalance() + l_TotalOD - l_ProdMinBalance - l_BarAmount;
				}else{
					// Current Account
					l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber, pProcessType, pConn);
				}
			}
			/*l_AccData = l_AccDAO.getAccountForODAvaBalance(pAccNumber, pConn);*/						
		}
		else {	// Other Product
			l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber,pProcessType, pConn); 
		}
		
		if (l_AvailableBalance < 0 ) {
			l_AvailableBalance = 0;
		}
		l_AvailableBalance = StrUtil.round2decimals(l_AvailableBalance);
	
		
		return l_AvailableBalance;
	}//end
	
	public static double getAvailableBalance(String pAccNumber, Connection pConn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {
		double l_AvailableBalance = 0;
		double l_ProdMinBalance = 0;
		double l_BarAmount = 0;
		double l_TotalOD = 0;
		AccountData l_AccData = new AccountData();
		AccountsDAO l_AccDAO = new AccountsDAO(pConn);
		AccBarDAO l_AccBarDAO = new AccBarDAO();
		CurrentAccountDAO l_CurAccDAO = new CurrentAccountDAO();
		AccountData l_CurAccData = new AccountData();
			
		String l_TodayDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		
		// Checking Product Type
		if(l_AccDAO.getAccount(pAccNumber, pConn));
			l_AccData = l_AccDAO.getAccountsBean();
		String productCode = l_AccData.getProduct();
		
		if (SharedLogic.isCurrent(productCode)) {
			
			if(l_CurAccDAO.getAccount(pAccNumber, pConn)){
				// Current Account
				l_CurAccData = l_CurAccDAO.getCurrentAccBean();
				
				if(l_CurAccData.getIsOD() == 1){
					// OD Account
					l_AccData = l_AccDAO.getODAvailableBalance(l_TodayDate, pAccNumber, pConn);
					
					// Get Bar Amount
					l_BarAmount = l_AccBarDAO.getTotalBarAmount(pAccNumber, pConn);
					
					// Getting Product Minimum Amount
					l_ProdMinBalance = SharedLogic.getAccountMinBalanceNew(productCode,l_CurAccData.getCurrencyCode());
					
					if (l_BarAmount != 0 ) {
						l_ProdMinBalance = 0;
					}
						
					l_TotalOD = 0;
					
					if (l_AccData.getOdLimit() > 0 || l_AccData.getTODLimit() > 0 ) {
						l_TotalOD = l_AccData.getOdLimit() + l_AccData.getTODLimit();
						l_ProdMinBalance = 0;
					}
					
					l_AvailableBalance = l_AccData.getCurrentBalance() + l_TotalOD - l_ProdMinBalance - l_BarAmount;
				}
				else if(l_CurAccDAO.isCreditCardAccount(pAccNumber, pConn)){
					l_AvailableBalance = l_AccDAO.getAvaliableBalanceForCreditCard(pAccNumber, l_TodayDate, productCode,pConn);

				}
				else{
					// Current Account
					l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber,0, pConn);
				}
			}			
		}
		else {	// Other Product
			l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber,0, pConn); 
		}
		
		if (l_AvailableBalance < 0 ) {
			l_AvailableBalance = 0;
		}
		l_AvailableBalance = StrUtil.round2decimals(l_AvailableBalance);		
		
		return l_AvailableBalance;
	}
	
	public static ArrayList<ReferenceData> getAccountStatus(Connection pConn) throws SQLException
	{		
		ArrayList<ReferenceData> ret = new ArrayList<ReferenceData>();
		AccountsDAO l_DAO = new AccountsDAO(pConn);		
		ret = l_DAO.getAccountStatus(pConn);		
		return ret;
	}
	
	public static DataResult save(AccountData object, Connection pConn) throws SQLException {
		DataResult ret = new DataResult();
		String oldSerialNo = object.getSignatureData().getSerialNo();
		AccountsDAO l_AccountsDAO = new AccountsDAO(pConn);
		PBFixedDepositAccountDAO l_fixedDAO = new PBFixedDepositAccountDAO();
		String newone = "";
		int l_SystemSettingAccNo = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACO").getN1();
		int CurrentAutoActive = SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN7();
		int signNo = 0;
		try {
			ret.setStatus(false);
			// Checking Account
			ret = checkAccountSave(object, pConn);			
			if (ret.getStatus()) {	
				
				//SMM 
				// Checking Duliplicate Passbook No , By Branch , By Product 
				
				if(SharedLogic.getSystemSettingT12N4("PBL")  == 1){ 
					if (l_AccountsDAO.getPassBookNo(object.getProduct(),object.getPassbook(), pConn)) {
						ret.setStatus(false);
						ret.setDescription("Duplicate Passbook No!");
					} else {
						ret.setStatus(true);
					}
				}				
				
				if (object.getAccountNumber().equalsIgnoreCase("TBA")) {
					SingletonConnection.setSharedConn(pConn);
					
					String[] input = parseAccountForGenerateAccountNo(object, l_SystemSettingAccNo);
					StringBuffer log = new StringBuffer();
					
					Ruleset.setPath(SingletonServer.getAbsPath() + "//rules//");
					
					if (SharedLogic.getSystemSettingT12N2("MRHF") == 1) {
						newone = generateAccountNo(object, pConn);
						
					}else {
						if(l_SystemSettingAccNo==1){
							newone = Rules.processID(input, "r00107", null, log);
						} else
							newone = Rules.processID(input, "r00106", null, log);
					}
					
					/* Demo SingletonConnection.setSharedConn(pConn);*/
					SingletonConnection.setSharedConn(null);
					
					if(newone.equals("-1")){		
						ret.setStatus(false);
						ret.setDescription("Invalid Check Digit Method!");
					} else if(newone.equals("-2")){			
						ret.setStatus(false);
						ret.setDescription("Invalid Prefix!");
					} else if( newone==null ||newone.equals("") || newone.equals("null")  ){           
                        ret.setStatus(false);
                        ret.setDescription("Invalid GenerateNo.Please Try Again!");
                    } else
						object.setAccountNumber(newone);
					
					if(CurrentAutoActive == 1 && LogicMgr.Account.isCurrent(object.getProduct())){
						object.setLastTransDate(object.getOpeningDate());
						object.setLastUpdate(object.getOpeningDate());
					}
					
					if(l_SystemSettingAccNo==1){// HNNS AccountNumber Char short Code						
						if (SharedLogic.getSystemSettingT12N2("MRHF") == 1){
							object.setAccountName(generateAccountNameMRHF(object, pConn));
						}else{
							object.setAccountName(generateAccountName(object));
						}
					}
					object.getPBFixedDepAccountData().setAccNumber(newone);
					object.getFixedDepositAccount().setAccountNumber(newone);
					object.getSpecialDepositAccount().setAccNumber(newone);
					object.getCallDepositAccount().setAccNumber(newone);
					
					// Checking Product Type
					if(ret.getStatus()){
						if (l_AccountsDAO.addAccount(object, pConn)) {
							if(SaveAccountRelationTable(object,pConn)){
								if(SaveAccountDetailsTable(object, l_AccountsDAO, pConn)){
									if(object.getProduct().equals("09")) {									
										if(MobileUserDao.SaveMobileUser(object, pConn)) {
											l_AccountsDAO.addAddress(object.getAccountNumber(), object.getMailingAddress(), pConn);
											if (object.getSignatureData() == null ) {
												object.setSignatureData(new SignatureData());	
											}
											// Pearl
											object.getSignatureData().setAccountNumber(object.getAccountNumber());
											
											if(oldSerialNo.equals("TBA")){										
												signNo = l_AccountsDAO.getSignatureNo(object.getAccountNumber(),object.getBranchCode(), pConn);
												object.getSignatureData().setSerialNo(String.valueOf(signNo+1));										
											}
											object.getSignatureData().setBranchCode(object.getBranchCode());
											String pFileName=object.getSignatureData().getpFileName();
											l_AccountsDAO.addLocalSingature(object.getSignatureData(),oldSerialNo,pFileName, pConn);
											}
											ret.setData(object.getAccountNumber());
											ret.setStatus(true);
											ret.setVNo(object.getShortAccountCode());
											ret.setCode(object.getFixedDepositAccount().getRefNo());
											ret.setVov(object.getFixedDepositAccount().getRefNo());//KMT
											ret.setDescription("001");
										}else{
											l_AccountsDAO.addAddress(object.getAccountNumber(), object.getMailingAddress(), pConn);
											if (object.getSignatureData() == null ) {
												object.setSignatureData(new SignatureData());	
											}
											// Pearl
											object.getSignatureData().setAccountNumber(object.getAccountNumber());
											
											if(oldSerialNo.equals("TBA")){										
												signNo = l_AccountsDAO.getSignatureNo(object.getAccountNumber(),object.getBranchCode(), pConn);
												object.getSignatureData().setSerialNo(String.valueOf(signNo+1));										
											}
											object.getSignatureData().setBranchCode(object.getBranchCode());
											String pFileName=object.getSignatureData().getpFileName();
											l_AccountsDAO.addLocalSingature(object.getSignatureData(),oldSerialNo,pFileName, pConn);
											
											ret.setData(object.getAccountNumber());
											ret.setVNo(object.getShortAccountCode());
											ret.setStatus(true);
											ret.setCode(object.getFixedDepositAccount().getRefNo());
											ret.setVov(object.getFixedDepositAccount().getRefNo());//KMT
											ret.setDescription("001");
											//atds end
										}
									} else {
										l_AccountsDAO.addAddress(object.getAccountNumber(), object.getMailingAddress(), pConn);
										if (object.getSignatureData() == null ) {
											object.setSignatureData(new SignatureData());	
										}
										// Pearl
										object.getSignatureData().setAccountNumber(object.getAccountNumber());
										if(!object.getSignatureData().getSerialNo().equals("")){
											if(oldSerialNo.equals("TBA")){										
												signNo = l_AccountsDAO.getSignatureNo(object.getAccountNumber(),object.getBranchCode(), pConn);
												object.getSignatureData().setSerialNo(String.valueOf(signNo+1));										
											}
											object.getSignatureData().setBranchCode(object.getBranchCode());
											String pFileName=object.getSignatureData().getpFileName();
											l_AccountsDAO.addLocalSingature(object.getSignatureData(),oldSerialNo,pFileName, pConn);
										}
										ret.setData(object.getAccountNumber());
										ret.setVNo(object.getShortAccountCode());
										ret.setVov(object.getFixedDepositAccount().getRefNo());//KMT
										ret.setCode(object.getFixedDepositAccount().getRefNo());
										ret.setDescription("001");
										ret.setCode(object.getSignatureData().getSerialNo());
									}
								}
							}	
						}
					} else {
					//check for Acc to Paid
					if(!object.isSigSave() && l_fixedDAO.getAccountToBePaid(object.getAccountNumber(), pConn)) {
						if(StrUtil.getAccountStatusDescription(object).toUpperCase().equals("ACTIVE")) {
							ret.setStatus(true);
						}else {
							ret.setStatus(false);
							ret.setDescription("Can't Update for Account's Status: It is taken Accounts of "+l_fixedDAO.getPbFixedAccData().getAccNumber());
						}
					}else {
						ret.setStatus(true);
					}
					if(ret.getStatus()) {
						if(l_SystemSettingAccNo==1){// HNNS AccountNumber Char short Code
							object.setAccountName(generateAccountName(object));
						}
						if (l_AccountsDAO.updateAccount(object, pConn)) {
							if(SaveAccountRelationTable(object,pConn)){								
								ret.setStatus(true);
								ret.setDescription("005");
							} else{
								 ret.setDescription("Error in Saving Customer Relation.");
							}
							
							if (ret.getStatus()){
								// Updating Account Details Table
								ret=UpdateAccountDetailsTable(object, l_AccountsDAO, pConn);
							}
							
							if (ret.getStatus()){
								if(l_AccountsDAO.updateAddress(object.getAccountNumber(), object.getMailingAddress(), pConn)) {
									ret.setData(object.getAccountNumber());
								}
								
								if (object.getSignatureData() != null) {
									object.getSignatureData().setAccountNumber(object.getAccountNumber());
									
									
									if(oldSerialNo.equals("TBA")){
										signNo= l_AccountsDAO.getMaxSerialNo(object.getAccountNumber(),object.getBranchCode(), pConn);
										object.getSignatureData().setSerialNo(String.valueOf(signNo+1));
										
									}
									else		
										l_AccountsDAO.deleteLocalSingnature(object.getAccountNumber(),object.getSignatureData().getSerialNo(),object.getBranchCode(), pConn);
								
									object.getSignatureData().setBranchCode(object.getBranchCode());
									String pFileName=object.getSignatureData().getpFileName();
									l_AccountsDAO.addLocalSingature(object.getSignatureData(),oldSerialNo,pFileName, pConn);
									if(l_AccountsDAO.getMultipleSignature(object.getAccountNumber(),object.getBranchCode(),pFileName,pConn))
										object.setLstSignData(l_AccountsDAO.getLstSignData());
								}
							}
						}
						ret.setData(object.getAccountNumber());
						ret.setVNo(object.getShortAccountCode());
						ret.setVov(object.getFixedDepositAccount().getRefNo()); // aph
						ret.setCode(object.getFixedDepositAccount().getRefNo());
						if(oldSerialNo.equals("TBA")){
							ret.setDescription("001");
							ret.setCode(object.getSignatureData().getSerialNo());
						}
						else
							ret.setDescription("005");
					}					
				}
			}					
			
		} catch (ParserConfigurationException e) {			
			e.printStackTrace();
		} catch (SAXException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}		
		return ret;
	}
	
	public static DataResult checkAccountSave(AccountData pData, Connection conn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		DataResult ret = new DataResult();
		ret.setStatus(true);//for Other Account Open
		if (LogicMgr.Account.isCASA(pData.getProduct())){
			ret.setStatus(true);
		} else if (LogicMgr.Account.isFD(pData.getProduct())){
			ret = checkFixedAccountSave(pData, conn);
		}else if (LogicMgr.Account.isUA(pData.getProduct())){
			ret = checkUniAccountSave(pData, conn);
		}
		return ret;
	}
	
	private static DataResult checkFixedAccountSave(AccountData pData, Connection conn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		DataResult ret = new DataResult();
		String l_ProductID = "";		
		GLDAO l_GLDAO = new GLDAO();	
		AccountsDAO l_DAO = new AccountsDAO();
		AccountData l_Data = new AccountData(); 
		if(LogicMgr.getSystemSettingT12N1("ACTMINBAL")== 1) {
			l_ProductID = pData.getProduct() + pData.getType();
		}else {
			l_ProductID = pData.getProduct();
		}		
		// Checking Multiple Of Fixed
		ret = LogicMgr.Transaction.checkMultipleOf(pData.getFixedDepositAccount().getAmount(),
				l_ProductID,pData.getCurrencyCode());
		if(!ret.getStatus()) {
			ret.setDescription("05-024");
			ret.setData(LogicMgr.Account.getAccountMultipleOf(l_ProductID) + "");
		}
		
		if(ret.getStatus()) {
			ret=LogicMgr.Transaction.checkMinOpnBalance(0, pData.getFixedDepositAccount().getAmount(), pData);
			if(!ret.getStatus()) {
				ret.setDescription("11-006");
			}
		}
			
		// Checking Interest Pay Type
		if (ret.getStatus()){
			if (pData.getFixedDepositAccount().getIntPayCode() == 2){
				// Checking Acc To Be Paid
				if (pData.getFixedDepositAccount().getAccToBePaid().equals("")){
					ret.setStatus(false);
					ret.setDescription("05-012");
					ret.setData("05-023");
				} else if (ret.getStatus()){ 				// Checking Account Status
					
					if (l_DAO.getAccount(pData.getFixedDepositAccount().getAccToBePaid(), conn)){
						l_Data = l_DAO.getAccountsBean();
						if(!StrUtil.getAccountStatusDescription(l_Data).equals(""))
						{
							if (StrUtil.getAccountStatusDescription(l_Data).toUpperCase().equals("NEW")){
								ret.setStatus(false);
								ret.setDescription("05-027");
								ret.setData("New");
								
							}
							if (StrUtil.getAccountStatusDescription(l_Data).toUpperCase().equals("CLOSED")){
								ret.setStatus(false);
								ret.setDescription("05-027");
								ret.setData("Closed");
							}
							if (StrUtil.getAccountStatusDescription(l_Data).toUpperCase().equals("CLOSE PENDING")){
								ret.setStatus(false);
								ret.setDescription("05-027");
								ret.setData("Close Pending");
							}
						}
						
						// Check Local Branch
						if(LogicMgr.getSystemSettingT12N6("IBTGL")!=1){
							if (ret.getStatus()){
								if (!pData.getBranchCode().equals(l_Data.getBranchCode())){
									ret.setStatus(false);
									ret.setDescription("05-029");
								}
							}
						}
					
						if(ret.getStatus()) {
							if(!pData.getCurrencyCode().equals(l_Data.getCurrencyCode())) {
								ret.setStatus(false);
								ret.setDescription("05-034");
							}
						}
					} else if (pData.getFixedDepositAccount().getAccToBePaid().equals("")){
						ret.setStatus(false);
						ret.setDescription("05-012");
						ret.setData("Account for Interest to Paid");
					}
				} 
				else {ret.setStatus(true);}
				if (ret.getStatus() && !l_GLDAO.checkGL(pData.getFixedDepositAccount().getAccToBePaid(), conn)){
					// Getting Product Type					
					String pType = l_Data.getProduct();
					// Checking Product Type
					if (LogicMgr.Account.isFD(pType) || LogicMgr.Account.isLoan(pType)){
						ret.setStatus(false);
						ret.setDescription("05-025");
					} else {ret.setStatus(true);}
				}
			} else if (pData.getFixedDepositAccount().getIntPayCode() == 3){
				// Checking Auto Renew
				if (!pData.getFixedDepositAccount().getAutoRenew()){
					ret.setStatus(false);
					ret.setDescription("05-026");
				} else {ret.setStatus(true);}
			}
		} 
		return ret;
	}
	
	public static String[] parseAccountForGenerateAccountNo(AccountData pData, int pSystemSettingACO) {
		String [] input = null;		
		if(pSystemSettingACO==1){
			input = new String[]{ 
					"productCode=" + pData.getProduct(),
					"currencyCode=" + pData.getCurrencyCode(),
					"accountType=" + pData.getDrawingType(),
					"branchCode=" + pData.getBranchCode(),
					"firstWord=" + pData.getAccountName().substring(0, 1)
					};
		} else {
			input = new String[]{ 
					"productCode=" + pData.getProduct(),
					"currencyCode=" + pData.getCurrencyCode(),
					"accountType=" + pData.getType(),
					"branchCode=" + pData.getBranchCode(),
					};
		}
		return input;
	}
	
	public static String generateAccountNo(AccountData object, Connection pConn) {
		String ret = "";
		String l_LastKey = "";
		ProductsDAO l_ProdDAO = new ProductsDAO();
		AccountsDAO l_AccountsDAO = new AccountsDAO(pConn);
		ArrayList<ProductNumberData> lstProdcutNumber = new ArrayList<ProductNumberData>();
		
		try {
			// Getting Product ID // SA, CA
			String l_ProdType = LogicMgr.Account.getProductCodeToKey(object.getProduct());
			
			// Getting Product Numbers
			lstProdcutNumber = l_ProdDAO.getProductNumbers(l_ProdType, pConn);
			
			int l_SrStart = LogicMgr.Account.getAccountSerialStart(object.getProduct());
			int l_SrLen = LogicMgr.Account.getAccountSerialLen(object.getProduct());
			int l_PrdStart = LogicMgr.Account.getAccountProductStart(object.getProduct());
			int l_PrdLen = LogicMgr.Account.getAccountProductLen(object.getProduct());
			int l_TypeStart = LogicMgr.Account.getAccountTypeStart(object.getProduct());
			int l_TypeLen = LogicMgr.Account.getAccountTypeLen(object.getProduct());
			int l_BranchStart = LogicMgr.Account.getAccountBranchStart(object.getProduct());
			int l_BranchLen = LogicMgr.Account.getAccountBranchLen(object.getProduct());
			int l_CurStart = LogicMgr.Account.getAccountCurrencyStart(object.getProduct());
			int l_CurLen = LogicMgr.Account.getAccountCurrencyLen(object.getProduct());
			int l_OtherStart = LogicMgr.Account.getAccountOtherStart(object.getProduct());
			int l_OtherLen = LogicMgr.Account.getAccountOtherLen(object.getProduct());
			//HNNS Fixed
			int l_SystemSettingAccNo = SharedLogic.getSystemSettingT12N1("ACO");
			//HNNS Adding New Prefix
			String prefix = "";
			ProductTypeData l_PTData = l_ProdDAO.readProductType(object.getProduct(), pConn);
			prefix = l_PTData.getProcessCode3();
			if(prefix.equals("")){
				prefix= object.getProduct();
			}
			
			
//			int RW = SharedLogic.getSystemSettingT12N5("BNK");  
			int RW =  SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN5(); // YNW_Update
			if(RW==0 || RW==255)
				RW = 0;
		
			if (SharedLogic.getSystemSettingT12N2("MRHF") == 1){
				l_LastKey = l_AccountsDAO.getLastIDNotAccType(prefix, object.getType(), object.getBranchCode(),LogicMgr.Account.getCurrencyDigit(object.getCurrencyCode()) 
						, l_SrStart, l_SrLen, l_PrdStart, l_PrdLen, l_TypeStart, l_TypeLen,l_BranchStart,l_BranchLen,l_CurStart,l_CurLen, pConn);
				
				ret = LogicMgr.Account.getNewAccountNumberNew(l_LastKey, prefix, object.getBranchCode()
						, object.getType(), LogicMgr.Account.getCurrencyDigit(object.getCurrencyCode()), RW+"", "", lstProdcutNumber);
			
			}
			else {
				if(l_SystemSettingAccNo == 1 ) {
					l_LastKey = l_AccountsDAO.getLastIDNotAccTypeByFirstWord(prefix, object.getBranchCode(), LogicMgr.Account.getCurrencyDigit(object.getCurrencyCode()), object.getAccountName() 
							, l_SrStart, l_SrLen, l_PrdStart, l_PrdLen, l_BranchStart,l_BranchLen,l_CurStart,l_CurLen, l_OtherStart, l_OtherLen, pConn);
				
					ret = LogicMgr.Account.getNewAccountNumberWithChar(l_LastKey, prefix, object.getBranchCode()
							, object.getType(), LogicMgr.Account.getCurrencyDigit(object.getCurrencyCode()), RW+"", object.getAccountName(), lstProdcutNumber);
					
				} else {
					l_LastKey = l_AccountsDAO.getLastIDNotAccType(prefix, object.getType(), object.getBranchCode(),LogicMgr.Account.getCurrencyDigit(object.getCurrencyCode()) 
							, l_SrStart, l_SrLen, l_PrdStart, l_PrdLen, l_TypeStart, l_TypeLen,l_BranchStart,l_BranchLen,l_CurStart,l_CurLen, pConn);
					
					ret = LogicMgr.Account.getNewAccountNumberNew(l_LastKey, prefix, object.getBranchCode()
							, object.getType(), LogicMgr.Account.getCurrencyDigit(object.getCurrencyCode()), RW+"", "", lstProdcutNumber);
					
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String generateAccountNameMRHF(AccountData object,Connection conn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		AccountsDAO l_AccDAO = new AccountsDAO();
		String accName = "";
		String prefix=SharedLogic.getSystemSettingT12T2("ACC"); //CS1
		
		//Read Prefix: ProcessCode5 [Update For Money Loan]
		ProductsDAO l_ProductsDAO = new ProductsDAO();
		ProductTypeData l_ProdTypeData = l_ProductsDAO.readProductType(object.getProduct(), conn);
		if(l_ProdTypeData != null){
			prefix = l_ProdTypeData.getProcessCode5();
		}
		
		int year=SharedLogic.getSystemSettingT12N1("ACC"); //17
		int serial=SharedLogic.getSystemSettingT12N2("ACC"); //000001
		int length=SharedLogic.getSystemSettingT12N3("ACC"); //000001
		if(serial == 0 )
			accName=prefix+"-"+year+"-"+StrUtil.leadZeros(l_AccDAO.getLastAccNameMRHF(prefix+"-"+year,conn),length);
		else{
			if (l_AccDAO.getAccCountMRHF(conn)==0)
				accName=prefix+"-"+year+"-"+StrUtil.leadZeros(serial,length);
			else
				accName=prefix+"-"+year+"-"+StrUtil.leadZeros(l_AccDAO.getLastAccNameMRHF(prefix+"-"+year,conn),length);
		}
		return accName;
	}
	
	public static String generateAccountName(AccountData object){
		String accName = "";
		int l_SrStart = LogicMgr.Account.getAccountSerialStart(object.getProduct());
		int l_SrLen = LogicMgr.Account.getAccountSerialLen(object.getProduct());
		int l_PrdStart = LogicMgr.Account.getAccountProductStart(object.getProduct());
		int l_PrdLen = LogicMgr.Account.getAccountProductLen(object.getProduct());
		int l_OtherStart = LogicMgr.Account.getAccountOtherStart(object.getProduct());
		int l_OtherLen = LogicMgr.Account.getAccountOtherLen(object.getProduct());
		
		String prdCode = object.getAccountNumber().substring(l_PrdStart-1, (l_PrdStart-1)+l_PrdLen);
		int srNo = Integer.parseInt(object.getAccountNumber().substring(l_SrStart-1, (l_SrStart-1) + l_SrLen));
		String firstWord = object.getAccountNumber().substring(l_OtherStart-1, (l_OtherStart-1)+l_OtherLen);
		accName = prdCode + firstWord + srNo;
		return accName;
	}
	
	public static boolean SaveAccountRelationTable(AccountData object,Connection conn)
	{
		boolean ret = false;
		CAJunctionDAO l_ACDAO = new CAJunctionDAO();
		try {
			
			l_ACDAO.deleteCAJunction(object.getAccountNumber(), "", conn);			
			for (int i=0;i<object.getAccountCustomers().size();i++){			
				object.getAccountCustomers().get(i).setAccountNumber(object.getAccountNumber());
				ret = l_ACDAO.addCAJunction(object.getAccountCustomers().get(i), conn);
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	public static boolean SaveAccountDetailsTable(AccountData object,AccountsDAO aAccDAO,Connection conn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException
	{
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();
		AccountGroupDAO l_AccountGroupDAO = new AccountGroupDAO();
		String pAccType = LogicMgr.getSystemSettingT12T2("SPI");
		boolean result = false;
		if (aAccDAO.addCLHeader(object, conn)) {
			if (SharedLogic.getSystemSettingT12N1("BNK") == 7) {
				String l_MajorCustomerName = getMajorCustomerName(object, conn);
				String[] l_CustomerName = null;
				if (l_MajorCustomerName.contains("@")) {
					l_CustomerName = l_MajorCustomerName.split("@")[0].split(" ");
				} else {
					l_CustomerName = l_MajorCustomerName.split(" ");
				}
				String l_CusName = l_CustomerName[l_CustomerName.length - 1].substring(0, 1);

				int l_GroupSetting = SharedLogic.getSystemSettingT12N1("AccGroup");
	            if(l_GroupSetting == 1){
            	   l_CusName = object.getAccountName().split("-")[0];
            	   l_CusName = l_CusName.trim();
            	   l_CusName = l_CusName.substring(l_CusName.length()-1);
	            }
				int l_SrNo = l_AccountGroupDAO.getAccountGroup(l_CusName, conn);
				object.setShortAccountCode(l_SrNo + "");
			}
			
			result = l_AccExtDAO.addData(object, conn);
			
			if (result) {
				if (LogicMgr.Account.isCurrent(object.getProduct())) {
					result = aAccDAO.addCurrentAccout(object, conn);
				} else if (LogicMgr.Account.isSavings(object.getProduct())) {
					result = aAccDAO.addSavingAccout(object, conn);
					if(object.getType().equals(pAccType)){
						SpecialDepositAccountDAO l_SpecialDepositAccountDAO = new SpecialDepositAccountDAO();
						result = l_SpecialDepositAccountDAO.addSpecialDepositAccount(object.getSpecialDepositAccount(), conn);
					}
				} else if (LogicMgr.Account.isFD(object.getProduct())){
					FixedDepositAccountDAO l_FixedDAO = new FixedDepositAccountDAO();
					
					if (l_FixedDAO.addFixedDepositAccount(object.getFixedDepositAccount(), conn)){
						result = l_FixedDAO.addPBFixedDepositAccount(object.getFixedDepositAccount(), conn);
					}
				} else if (LogicMgr.Account.isLoan(object.getProduct())) {
					LoanAccountDAO l_LoanDAO = new LoanAccountDAO();
					result = l_LoanDAO.add(object, conn);
				} else if(LogicMgr.Account.isSpecial(object.getProduct())) {
					SpecialDepositAccountDAO l_SpecialDepositAccountDAO = new SpecialDepositAccountDAO();
					result = l_SpecialDepositAccountDAO.addSpecialDepositAccount(object.getSpecialDepositAccount(), conn);
				} else if(LogicMgr.Account.isCall(object.getProduct())) {
					CallDepositAccountDAO l_CallAccountDAO = new CallDepositAccountDAO();
					if(LogicMgr.getSystemSettingT12N1("CD")==1 && object.getCallDepositAccount().getAccTobePaid().equals(""))
						 object.getCallDepositAccount().setAccTobePaid(object.getCallDepositAccount().getAccNumber());
					result = l_CallAccountDAO.addCallDepositAccount(object.getCallDepositAccount(), conn);
				}else if(LogicMgr.Account.isUA(object.getProduct())){//add new function
					UniversalAccountDAO l_UniversalAccountDAO = new UniversalAccountDAO();
					result = l_UniversalAccountDAO.addUniversalDepositAccount(object, conn);
				}  else {
					result = true;
				}
		
			}
			
			if (result) {
				if (LogicMgr.Account.getProductCodeToProductData(object.getProduct()).hasPassbook()) {
					object.setIssueDate(SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate()));
					if(LogicMgr.Account.isFD(object.getProduct())) {
						if(object.getFixedDepositAccount().getReceiptNo().trim().equals("1")) {
							/*result = aAccDAO.addPBHeader(object, conn);*/
							if(SharedLogic.getSystemSettingT12N3("FD") == 1) {
								result = aAccDAO.addFDPBHeader(object, object.getFixedDepositAccount().getTenure(), object.getFixedDepositAccount().getRefNo(), conn);
							}else if(SharedLogic.getSystemSettingT12N3("FD") == 2) {
								result = aAccDAO.addFDPBHeader(object, 0, conn);
							}else {
								result = aAccDAO.addFDPBHeader(object, object.getFixedDepositAccount().getTenure(), conn);
							}
							if (result) {
								result = aAccDAO.addPBHistory(object, conn);
							}
						}
					}else {
						result = aAccDAO.addPBHeader(object, conn);
						if (result) {
							result = aAccDAO.addPBHistory(object, conn);
						}
					}
					
				}
			}
		}
		return result;
	}
	
	public static DataResult UpdateAccountDetailsTable(AccountData object,AccountsDAO aAccDAO,Connection conn){
		DataResult result = new DataResult();
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();
		AccountGroupDAO l_AccountGroupDAO = new AccountGroupDAO();
		PBFixedDepositAccountDAO l_FixedData = new PBFixedDepositAccountDAO();
		CallDepositAccountDAO l_CallDAO = new CallDepositAccountDAO();	
		FixedDepositAccountData pFixedData = object.getFixedDepositAccount();
		PBHeaderDAO l_PBHeaderDAO = new PBHeaderDAO();
		AccountsDAO l_AccDao = new AccountsDAO();
		String l_result = "";
		try {
			if (LogicMgr.Account.isCurrent(object.getProduct())) {
				// Update Current Account
				result.setStatus(true);
				/*result.setStatus(aAccDAO.updateCurrentAccount(object, conn));*/
			}else if (LogicMgr.Account.isSavings(object.getProduct())){
				// Update Fixed Deposit Accopunt
				/*FixedDepositAccountDAO l_FixedDAO = new FixedDepositAccountDAO();
				PBFixedDepositAccountDAO l_PBFixedDAO = new PBFixedDepositAccountDAO();*/				
				result =l_AccDao.updateAccTobePaid(object,conn);//atds
				/*if (l_FixedDAO.updateFixedDepositAccount(object.getFixedDepositAccount(), conn)){
					result.setStatus(l_PBFixedDAO.updatePBFixedDepositAccount(object.getPBFixedDepAccountData(), conn));
				}*/
			}else if(LogicMgr.Account.isCall(object.getProduct())){	
				if (SharedLogic.getSystemSettingT12N1("CD") == 1 || SharedLogic.getSystemSettingT12N1("CD") == 3){
					object.getCallDepositAccount().setAccNumber(object.getAccountNumber());
					result = l_CallDAO.updateAccTobePaid(object.getCallDepositAccount(),conn);
				} else
					result.setStatus(true);
			}else if(LogicMgr.Account.isFD(object.getProduct())) {
				if(object.getFixedDepositAccount().getReceiptNo().trim().equals("1")) {
					//KMT start
					if(SharedLogic.getSystemSettingT12N3("FD") == 1){					
						l_result = pFixedData.getRefNo();
						
						if(aAccDAO.isExist(object.getAccountNumber(), l_result, conn)){ 
							if(l_PBHeaderDAO.updateTenure(pFixedData, conn)){
								result.setStatus(true);
							}
						}else{
							l_result = l_FixedData.getMaxRefNo(object.getFixedDepositAccount().getAccountNumber(), conn);
							
							result.setStatus(aAccDAO.addFDPBHeader(object, object.getFixedDepositAccount().getTenure(), l_result, conn));
						}//KMT end
					}else if(SharedLogic.getSystemSettingT12N3("FD") !=2){
						if(aAccDAO.isExist(object.getAccountNumber(), object.getFixedDepositAccount().getTenure(), conn)){
							result.setStatus(true);
						}else{
							result.setStatus(aAccDAO.addFDPBHeader(object, object.getFixedDepositAccount().getTenure(), conn));
						}
					}else
						result.setStatus(true);
				}else{
					result.setStatus(true);
				}
				if(result.getStatus()){
					result = DBFixedDepositMgr.addFixedDetailData(object.getFixedDepositAccount(), conn);
				}
			}else if(LogicMgr.Account.isUA(object.getProduct())){//add new function
				UniversalAccountDAO l_UniversalAccountDAO = new UniversalAccountDAO();
				result.setStatus(l_UniversalAccountDAO.UpdateUniversalDepositAccount(object, conn));
			}
			else {
				result.setStatus(true);
			}
			if(result.getStatus()) {
				/*if(SharedLogic.getSystemSettingT12N1("BNK") == 7){
					String l_MajorCustomerName = getMajorCustomerName(object, conn);
					String[] l_CustomerName = l_MajorCustomerName.split(" ");
					String l_CusName = l_CustomerName[l_CustomerName.length-1].substring(0, 1);			
						
					int l_SrNo = l_AccountGroupDAO.getAccountGroup(l_CusName, conn);
					object.setShortAccountCode(l_SrNo+"");
				}*/
				if (SharedLogic.getSystemSettingT12N1("BNK") == 7) {
					String l_MajorCustomerName = getMajorCustomerName(object, conn);
					String[] l_CustomerName = null;
					if (l_MajorCustomerName.contains("@")) {
						l_CustomerName = l_MajorCustomerName.split("@")[0].split(" ");
					} else {
						l_CustomerName = l_MajorCustomerName.split(" ");
					}
					String l_CusName = l_CustomerName[l_CustomerName.length - 1].substring(0, 1);

					int l_GroupSetting = SharedLogic.getSystemSettingT12N1("AccGroup");
		            if(l_GroupSetting == 1){
	            	   l_CusName = object.getAccountName().split("-")[0];
	            	   l_CusName = l_CusName.trim();
	            	   l_CusName = l_CusName.substring(l_CusName.length()-1);
		            }
		            
					int l_SrNo = l_AccountGroupDAO.getAccountGroup(l_CusName, conn);
					object.setShortAccountCode(l_SrNo + "");
				}
				result.setStatus(l_AccExtDAO.updateData(object, conn));
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}
	
	private static DataResult checkUniAccountSave(AccountData pData, Connection conn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		DataResult ret = new DataResult();
		String l_ProductID = "";
		if(LogicMgr.getSystemSettingT12N1("ACTMINBAL")== 1) {
			l_ProductID = pData.getProduct() + pData.getType();
		}else {
			l_ProductID = pData.getProduct();
		}	
        
		ret = LogicMgr.Transaction.checkMultipleOf(pData.getpUniversalAmount(),
				l_ProductID,pData.getCurrencyCode());
		if(!ret.getStatus()) {
			ret.setDescription("05-024");
			ret.setData(LogicMgr.Account.getAccountMultipleOf(l_ProductID) + "");
		}
		
		if(ret.getStatus()) {
			ret=LogicMgr.Transaction.checkMinOpnBalance(0, pData.getpUniversalAmount(), pData);
			if(!ret.getStatus()) {
				ret.setDescription("11-006");
			}
		}
		return ret;
	}
	
	private static String getMajorCustomerName(AccountData object,Connection conn)
	{
		String ret = "";		
		CustomerDAO l_CustomerDAO = new CustomerDAO();
		try {					
			for (int i=0; i<object.getAccountCustomers().size();i++){			
				if(object.getAccountCustomers().get(i).getAccountType() == object.getAccountCustomers().get(i).getRelationType()){					
					l_CustomerDAO.getCustomer(object.getAccountCustomers().get(i).getCustomerID(), conn);
					ret = l_CustomerDAO.getObjCustBean().getName().trim();break;
				}
			}			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	public static DataResult createAccountForCard(String pCardNo, String pCustomerId, String pOpeningDate, Connection pConn) throws SQLException {
		DataResult ret = new DataResult();
		AccountData object = new AccountData();
		AccountsDAO l_AccountsDAO = new AccountsDAO();
		ProductData l_PrdData = new ProductData();
		l_PrdData = SharedLogic.Account.getProductCodeToProductData("95");
		
		try {
			ret.setStatus(false);
				
			object.setProduct("95");
			object.setType("01");
			object.setBranchCode("001");
			object.setAccountNumber(pCardNo);
			object.setOpeningDate(pOpeningDate);
			
			object.setAccountCustomers(new ArrayList<AccountCustomerData>());
			object.setMailingAddress(new AddressData());
			object.setCashInHandGL(l_PrdData.getCashInHandGL());
			object.setProductGL(l_PrdData.getProductGL());
			
			AccountCustomerData acc = new AccountCustomerData();
			acc.setAccountNumber(pCardNo);
			acc.setCustomerID(pCustomerId);
			object.getAccountCustomers().add(acc);
			
			if (l_AccountsDAO.addAccount(object, pConn)) {
				if(SaveAccountRelationTable(object,pConn)){
					if(SaveAccountDetailsTable(object, l_AccountsDAO, pConn)){
						ret.setStatus(l_AccountsDAO.addAddress(object.getAccountNumber(), object.getMailingAddress(), pConn));
						ret.setData(object.getAccountNumber());
					}
				}
			}
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		} catch (SAXException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		return ret;
	}
}
