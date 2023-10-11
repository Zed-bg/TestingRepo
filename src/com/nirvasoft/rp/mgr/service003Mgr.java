package com.nirvasoft.rp.mgr;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.client.Ref;
import com.nirvasoft.rp.client.util.ClientUtil;
import com.nirvasoft.rp.core.SingletonServer;
import com.nirvasoft.rp.mgr.DBAccountMgr;
import com.nirvasoft.rp.dao.CCTDrawingDao;
import com.nirvasoft.rp.dao.service003Dao;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.AccountTransactionResponse;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.CCTDrawingData;
import com.nirvasoft.rp.shared.ChequeEnquiryRes;
import com.nirvasoft.rp.shared.ChequeListResult;
import com.nirvasoft.rp.shared.ChequeSetAmountResult;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.DisPaymentTransactionData;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ResponseData;
import com.nirvasoft.rp.shared.SharedUtil;
import com.nirvasoft.rp.shared.SignatureData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.accdepositreq;
import com.nirvasoft.rp.shared.acctransferreq;
import com.nirvasoft.rp.shared.accwithdrawreq;
import com.nirvasoft.rp.shared.bulkpaymentdatareq;
import com.nirvasoft.rp.shared.bulkpaymentrequest;
import com.nirvasoft.rp.shared.chequebookreq;
import com.nirvasoft.rp.shared.chequebookres;
import com.nirvasoft.rp.shared.chequelistres;
import com.nirvasoft.rp.shared.chequeverifyreq;
import com.nirvasoft.rp.shared.chequeverifyres;
import com.nirvasoft.rp.shared.coderesponsedata;
import com.nirvasoft.rp.shared.fixedaccountopenreq;
import com.nirvasoft.rp.shared.fixedaccountopenres;
import com.nirvasoft.rp.shared.gopaymentresponse;
import com.nirvasoft.rp.shared.merchantpaymentdatareq;
import com.nirvasoft.rp.shared.paymenttransactiondata;
import com.nirvasoft.rp.shared.stopchequeres;
import com.nirvasoft.rp.shared.paymenttopupreq;
import com.nirvasoft.rp.shared.icbs.DepositData;
import com.nirvasoft.rp.shared.icbs.SMSReturnData;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.icbs.WithdrawData;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.util.ServerGlobal;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.StrUtil;

public class service003Mgr {

	public AccountTransactionResponse doAccountTransfer(DisPaymentTransactionData disData, String trType, String reqTrnRef,
			String narrative) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		try {
				// prepare data
				pTransferData.setAmount(disData.getAmount());
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey(trType);
				// to calculate
				//pTransferData.setFinishCutOffTime(GeneralUtility.checkCutOfTime());
				pTransferData.setFinishCutOffTime(false);
				pTransferData.setFromAccNumber(disData.getFromAccount());
				pTransferData.setFromBranchCode(disData.getFromBranch());
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo(reqTrnRef);
				pTransferData.setRemark(narrative);
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(disData.getToAccount());
				pTransferData.setToBranchCode(disData.getToBranch());
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				
				pResData = icbsMgr.postTransfer(pTransferData);
				if (pResData.getStatus()) {
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setCharges(String.valueOf(pResData.getComm1()));
					ret.setParam1(pResData.getCcy());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public gopaymentresponse dopaymentTransfer(paymenttransactiondata[] datalist,String remark,int type){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData[] pTransferData = new TransferData[datalist.length];
		TransferData data = new TransferData();
		try {
			//GeneralUtil.readBrCodeLength();
		
		for(int i=0; i<datalist.length; i++){
			data = new TransferData();
			// prepare data
			data.setAmount(datalist[i].getAmount());
			data.setCurrencyCode(datalist[i].getFromcurcode());
			//data.setFinishCutOffTime(GeneralUtility.checkCutOfTime());
			data.setFinishCutOffTime(false);
			data.setFromAccNumber(datalist[i].getFromaccnumber());
			data.setFromBranchCode(datalist[i].getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			data.setToAccNumber(datalist[i].getToaccnumber());
			data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			data.setRemark(remark);
			pTransferData[i]=data;
		}
		res = transMgr.dopaymentTransfer(pTransferData,type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}
		return res;
	}
	
	public gopaymentresponse dopayment(paymenttransactiondata[] datalist,String paymentType,String refCode){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData[] pTransferData = new TransferData[datalist.length];
		TransferData data = new TransferData();
		try {
			//GeneralUtil.readBrCodeLength();
		
		for(int i=0; i<datalist.length; i++){
			data = new TransferData();
			// prepare data
			data.setAmount(datalist[i].getAmount());
			//data.setFinishCutOffTime(GeneralUtility.checkCutOfTime());
			data.setFinishCutOffTime(false);
			//add from acc
			if(!(datalist[i].getFromaccnumber().equals("") || datalist[i].getFromaccnumber().equals("null") )){
				data.setCurrencyCode(datalist[i].getFromcurcode());
				data.setFromAccNumber(datalist[i].getFromaccnumber());
				data.setFromBranchCode(datalist[i].getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			}
			//add to acc
			if(!(datalist[i].getToaccnumber().equals("") || datalist[i].getToaccnumber().equals("null") )){
				data.setToAccNumber(datalist[i].getToaccnumber());
				data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			}	
			data.setReferenceNo(refCode);
			data.setRemark(datalist[i].getDescription());
			pTransferData[i]=data;
		}
		res = transMgr.dopayment(pTransferData,paymentType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}
		return res;
	}
	
	public AccountTransactionResponse checkAccountTraansferTransLimit(String fromAccount, String userID, double amount) throws SQLException {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		String minTransLimit = "";
		String dayTransLimit = "";
		String maxTransLimit = "";
		double transAmtPerday = 0.00;
		Connection conn = null;
		conn = ConnAdmin.getConn3();
		minTransLimit = ConnAdmin.readConfig("AccTransferMinTransLimit");
		dayTransLimit = ConnAdmin.readConfig("AccTransferDayTransLimit");
		maxTransLimit = ConnAdmin.readConfig("AccTransferMaxTransLimit");
		if (minTransLimit.equals("")) {
			minTransLimit = "0";
		}
		if (dayTransLimit.equals("")) {
			dayTransLimit = "0";
		}
		transAmtPerday = new service003Dao().getTransAmountPerDayAccTransfer(fromAccount, userID,
				GeneralUtil.datetoString(), conn);
		
		if (amount > Double.parseDouble(maxTransLimit)) {
			conn.close();
			ret.setCode("0014");
			ret.setDesc("Transfer Amount exceeded over " + GeneralUtil.formatNumber(Double.valueOf(maxTransLimit))
					+ " MMK");
			return ret;
		} else {
			ret.setCode("0000");
			ret.setDesc("Success");
		}
		if (amount + transAmtPerday > Double.parseDouble(dayTransLimit)) {
			conn.close();
			ret.setCode("0014");
			ret.setDesc("Transaction exceeded over daily transaction limit "
					+ GeneralUtil.formatNumber(Double.valueOf(dayTransLimit)) + " MMK");
			return ret;
		} else {
			ret.setCode("0000");
			ret.setDesc("Success");
		}
		conn.close();
		return ret;
	}
	
	public boolean checkUserAndAccount(String aUserID, String aFromAcc) {
		Connection conn = null;
		boolean result = false;
		service003Dao aUCdao = new service003Dao();
		try {
			conn = ConnAdmin.getConn3();
			result = aUCdao.checkUserAndAccount(aUserID, aFromAcc, conn);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	
	public chequebookres dochequebookrequest(chequebookreq req){
		chequebookres response = new chequebookres();
		Connection conn = null;
		
		service003Dao sdao = new service003Dao();
		try {
			conn = ConnAdmin.getConn3();
			conn.setAutoCommit(false);
			String[] arr = sdao.checkaccountno(req.getAccnumber(), conn);
			if(arr[0].equals("")){
				response.setRetcode("210");
				response.setRetmessage("Account Number is invalid.");
				return response;
			}

			//SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			//com.nirvasoft.rp.shared.SharedLogic.setSystemData(SharedLogic.getSystemData());	
			response = sdao.dochequebookrequest(req , arr[0],arr[1], conn);
			if(response.getRetcode().equals("300")){
				conn.commit();
			}else{
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setRetcode("220");
			response.setRetmessage("Internal server error");
		} finally {
			try {
				if (!conn.isClosed() || conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				response.setRetcode("220");
				response.setRetmessage("Internal server error");
			}
		}
		return response;
	}
	
	public stopchequeres stopCheque(String accountNo, String chequeNo, String narration) {
		 
		service003Dao l_dao = new service003Dao();
		stopchequeres ret = new stopchequeres();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			conn.setAutoCommit(false);
			ret = l_dao.stopCheque(accountNo, chequeNo, narration, conn);
			if(ret.getRetcode().equals("300")) {
				conn.commit();
				ret.setRetmessage("Stopped Cheque Request Successfully");
			}else {
				conn.rollback();
				ret.setRetmessage("Stopped Cheque Request Failed");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setRetcode("220");
			ret.setRetmessage("Internal server error");

		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret.setRetcode("220");
				ret.setRetmessage("Internal server error");
			}
		}
		return ret;
	}
	
	public ChequeSetAmountResult setChequeAmount(String accountNo, String chequeNo, String amount, String entryDate,
			String dueDate, String t1, String t2, String t3, String t4, String t5, String n1, String n2, String n3,
			String n4, String n5) {
		ChequeSetAmountResult cusres = new ChequeSetAmountResult();
		service003Dao l_dao = new service003Dao();
		service001mgr smgr = new service001mgr();
		Connection conn = null;
		try {
			boolean success = false;

			conn = ConnAdmin.getConn3();
			conn.setAutoCommit(false);

			boolean alreadyExist = true;
			alreadyExist = l_dao.checkAlreadySetChequeAmount(accountNo, chequeNo, conn);
			//SharedLogic.setSystemData(com.nirvasoft.rp.core.ccbs.data.db.DBSystemMgr.readSystemData(conn));
			if (alreadyExist) {
				cusres.setCode("210");
				cusres.setDesc("This cheque is already verified.");
				cusres.setAccountNo(accountNo);
			}else if(!com.nirvasoft.rp.core.ccbs.data.db.DBTransactionMgr.canWithdrawWithMultiCurrency(accountNo,Double.parseDouble(amount), conn)){
				cusres.setCode("210");
				cusres.setDesc("Please check your avaliable balance.");
				cusres.setAccountNo(accountNo);
			} else {
				// prepare due date and get number of days from SystemSetting
				// table
				int numberOfDueDaysFromDatabase = l_dao.getDueDate(conn);
				numberOfDueDaysFromDatabase = numberOfDueDaysFromDatabase - 1;

				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(entryDate);
				entryDate = new SimpleDateFormat("yyyyMMdd").format(dt);

				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, numberOfDueDaysFromDatabase);
				dt = c.getTime();
				dueDate = new SimpleDateFormat("yyyyMMdd").format(dt);

				chequelistres chqList = new chequelistres();
				chqList = smgr.getChequeList(accountNo);
				
				String status = "";

				for (int i = 0; i < chqList.getDatalist().size(); i++) {
						if (chequeNo.equals(
								chqList.getDatalist().get(i).getChequenumber())) {
							status = chqList.getDatalist().get(i).getStatus();
						}
				}

				if (status.equals("Unpaid")) {
					success = l_dao.setChequeAmount(accountNo, chequeNo, amount, entryDate, dueDate, t1, t2, t3, t4, t5,
							n1, n2, n3, n4, n5, conn);

					if (success) {
						String remark = "Cheque Verification";
						String barType = "6";
						String refNo = chequeNo;

						success = l_dao.setChequeAmount_AccBar(accountNo, amount, entryDate, remark, barType, refNo,
								conn);

						if (success) {
							conn.commit();

							cusres.setCode("300");
							cusres.setDesc("Cheque Verify Successfully");
							cusres.setAccountNo(accountNo);
							cusres.setChequeNo(chequeNo);
							cusres.setAmount(amount);
							cusres.setEntryDate(entryDate);
							cusres.setDueDate(dueDate);
							cusres.setT1(t1);
							cusres.setT2(t2);
							cusres.setT3(t3);
							cusres.setT4(t4);
							cusres.setT5(t5);
							cusres.setN1(n1);
							cusres.setN2(n2);
							cusres.setN3(n3);
							cusres.setN4(n4);
							cusres.setN5(n5);
						} else {
							conn.rollback();

							cusres.setCode("210");
							cusres.setDesc("Cheque Verification Fail. [005]");
							cusres.setAccountNo(accountNo);
						}
					} else {
						conn.rollback();

						cusres.setCode("210");
						cusres.setDesc("Cheque Verification Fail. [004]");
						cusres.setAccountNo(accountNo);
					}
				} else {
					cusres.setCode("210");
					cusres.setDesc("This cheque is " + status.toLowerCase() + ".");
					cusres.setAccountNo(accountNo);
				}
			}
		} catch (Exception e) {
			String code = "";
			String desc = "";

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				code = "210";
				desc = "Cheque Verification Fail. [002]";
			}

			// TODO Auto-generated catch block
			e.printStackTrace();
			if (code.equals("")) {
				code = "210";
				desc = "Cheque Verification Fail. [003]";
			}

			cusres.setCode(code);
			cusres.setDesc(desc);
			cusres.setAccountNo(accountNo);

		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				cusres.setCode("210");
				cusres.setDesc("Cheque Verification Fail. [001]");
				cusres.setAccountNo(accountNo);
			}
		}

		return cusres;
	}
	
	public chequeverifyres dochequeverify(chequeverifyreq req) {
		chequeverifyres response = new chequeverifyres();
		
		try {
			//SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			//com.nirvasoft.rp.shared.SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			//com.nirvasoft.rp.shared.SharedLogic.getSystemData().setCurrencyCodes(DBSystemMgr.readCurrencyCodes());

			String entryDate = com.nirvasoft.rp.core.ccbs.data.db.DBTransactionMgr.getCurrentDate();
			ChequeSetAmountResult infoResult =  setChequeAmount(req.getAccnumber(), req.getChequenumber(), req.getAmount(), entryDate,
					"", "", req.getT2(), req.getT3(), req.getT4(), req.getT5(), "0", "0", "0", "0", "0");
			if (infoResult.getCode().equals("300")) {
				response.setRetcode(infoResult.getCode());
				response.setRetmessage(infoResult.getDesc());
				response.setDuedate(infoResult.getDueDate());
				
				String dueDate = response.getDuedate();
				Date dt;
				try {
					dt = new SimpleDateFormat("yyyyMMdd").parse(dueDate);
				
					String dueDatePrepare = new SimpleDateFormat("dd-MM-yyyy").format(dt);
		
					String ymd[] = dueDatePrepare.split("-");
					String dd = ymd[0];
					String mm = ymd[1];
					String yyyy = ymd[2];
					String month = GeneralUtil.getMonthString(mm);
					dueDate = dd + " " + month + " " + yyyy;
					response.setDuedate(dueDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				response.setRetcode(infoResult.getCode());
				response.setRetmessage(infoResult.getDesc());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			response.setRetcode("220");
			response.setRetmessage("Internal server error");
		}
		return response;
	}
		
	public fixedaccountopenres dofixedaccountopen(fixedaccountopenreq req){
		fixedaccountopenres response = new fixedaccountopenres();
		service003Dao l_dao = new service003Dao();
		AccountData accdata = new AccountData();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			conn.setAutoCommit(false);
			boolean matuity = false;
			if(req.getMaturity().equals("1"))
				matuity = true;
			
			//SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			//com.nirvasoft.rp.core.SingletonServer.setSystemData(SharedLogic.getSystemData());
			//com.nirvasoft.rp.shared.SharedLogic.setSystemData(SharedLogic.getSystemData());
			//com.nirvasoft.rp.shared.SharedLogic.getSystemData().setCurrencyCodes(SharedLogic.getCurrencyCodes());
			accdata = parseAccount( req.getCustomerid(), req.getFromaccnumber(), Integer.parseInt(req.getTenure()), req.getStartdate(), Integer.parseInt(req.getInterestpaytype()), matuity ,
					Double.parseDouble(req.getYearlyrate()), req.getAmount(),Integer.parseInt(req.getAcctype()),Integer.parseInt(req.getRtype()),
					Integer.parseInt(req.getDatatenure()), req.getProductcode(),conn);
			
			
			ConnAdmin.setRPConn("001", "");
			com.nirvasoft.rp.shared.DataResult l_res = new com.nirvasoft.rp.shared.DataResult();
			l_res = DBAccountMgr.save(accdata, conn);
			
			if(l_res.getStatus()){
				//GeneralUtil.readBrCodeLength();

				pTransferData.setAmount(Double.parseDouble(req.getAmount().replace(",", "")));
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey("1");
				// to calculate
				//pTransferData.setFinishCutOffTime(GeneralUtility.checkCutOfTime());
				if (SharedLogic.getSystemData().isCutOfTime()) {
				pTransferData.setFinishCutOffTime(true);
				}else {
					pTransferData.setFinishCutOffTime(false);
				}
				pTransferData.setFromAccNumber(req.getFromaccnumber());
				pTransferData.setFromBranchCode(
						req.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo("");
				pTransferData.setRemark("Opening Fixed Deposit Account");
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(l_res.getData());
				pTransferData.setToBranchCode(
						l_res.getData().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				
				pResData = icbsMgr.openDepositAcc(pTransferData,conn);
				if (pResData.getStatus()) {
					conn.commit();
					response.setRetcode("300");
					response.setRetmessage("Request Successfully");
					response.setToaccnumber(pTransferData.getToAccNumber());
					response.setStartdate(GeneralUtil.changedateformat2(accdata.getFixedDepositAccount().getStartDate()));
					response.setDuedate(GeneralUtil.changedateformat2(accdata.getFixedDepositAccount().getDueDate()));
					response.setBankrefnumber(String.valueOf(pResData.getTransactionNumber()));
					response.setTransdate(GeneralUtil.getDate());
					response.setEffectivedate(GeneralUtil.changedateyyyyMMddtoyyyyMMdddash(pResData.getEffectiveDate()));
				} else {
					response.setRetcode("220");
					response.setRetmessage(pResData.getDescription());
					conn.rollback();
				}
				
			} else {
				response.setRetcode("210");
				response.setRetmessage(l_res.getDescription());
				conn.rollback();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setRetcode("220");
			response.setRetmessage(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.setRetcode("220");
				response.setRetmessage(e.getMessage());
				
			}
		}
		return response;
	}
	
	private static AccountData parseAccount(String customerid, String fromaccnumber, int tenure, String startdate, int interestpaytype, boolean maturity,
			double yearlyrate,String amount,int accType,int rType, int datatenure, String productcode, Connection conn) throws Exception {
					service003Dao s3_dao = new service003Dao();
					AccountData currentObject = new AccountData();
					ProductData l_PrdData = new ProductData();
					/*SharedLogic.setSystemData(DBSystemMgr.readSystemData());
					com.nirvasoft.rp.shared.SharedLogic.setSystemData(DBSystemMgr.readSystemData());*/

					String l_TodayDate = GeneralUtil.getDate();
					//startdate = GeneralUtil.changedateformat1(startdate);
					int pCurCode =0 , pSetting = com.nirvasoft.rp.shared.SharedLogic.getSystemSettingT12N2("ACTMINBAL");
					String arr[] = null;
					arr =s3_dao.getBranchData(fromaccnumber,conn);
					
					if(pSetting == 1) {			
						pCurCode = 1;			
						l_PrdData = com.nirvasoft.rp.shared.SharedLogic.Account.getProductCodeAccountCodeToProductData(productcode,"",pCurCode+"");
					}else{
						l_PrdData = com.nirvasoft.rp.shared.SharedLogic.Account.getProductCodeAccountCodeToProductData(productcode,arr[2]);
					}

					currentObject.setAccountNumber("TBA");
					currentObject.setAccountName("");
					currentObject.setPassbook("");
					currentObject.setOpeningDate(GeneralUtil.getDateYYYYMMDD());
					currentObject.setType(arr[2]);
					currentObject.setProduct(l_PrdData.getProductCode());
					currentObject.setBranchCode(arr[0]);
					currentObject.setCurrencyCode("MMK");
					currentObject.setDrawingType("100");
					currentObject.setStatus(0);
					currentObject.setZoneCode(Integer.parseInt(arr[1]));
					
					currentObject.setAccountCustomers(new ArrayList<AccountCustomerData>());
					
					AccountCustomerData ac = new AccountCustomerData();
					ac.setRelationType(rType);
					ac.setAccountType(accType);
					ac.setCustomerID(customerid); 			
					ac.setAccountNumber("TBA");

					currentObject.getAccountCustomers().add(ac);

					currentObject.setRemark("");
					currentObject.setMailingAddress(new AddressData());
					
					currentObject.getFixedDepositAccount().setAccountNumber("TBA");		
					currentObject.getFixedDepositAccount().setRefNo("R-1");
					currentObject.getFixedDepositAccount().setTenure(Short.parseShort(String.valueOf(datatenure)));
					
					if(GeneralUtil.compareDate(startdate,l_TodayDate) <= 0)
						startdate = l_TodayDate;

					currentObject.getFixedDepositAccount().setStartDate(startdate);
					
					String dueDate = GeneralUtil.calculateDuedate(startdate, tenure, datatenure, 1);
					
					currentObject.getFixedDepositAccount().setDueDate(dueDate);
					currentObject.getFixedDepositAccount().setIntToBePaid(0);
					currentObject.getFixedDepositAccount().setIntPayCode(Short.parseShort(String.valueOf(interestpaytype)));
					if (interestpaytype==2)
						currentObject.getFixedDepositAccount().setAccToBePaid(fromaccnumber);
					else
						currentObject.getFixedDepositAccount().setAccToBePaid("");
					
					currentObject.getFixedDepositAccount().setAutoRenew(maturity);

					currentObject.getFixedDepositAccount().setOtherFacility(Byte.parseByte("0"));
					currentObject.getFixedDepositAccount().setAmount(Double.parseDouble(amount.replace(",", "")));
					currentObject.getFixedDepositAccount().setLastTransDate("19000101");		
					currentObject.getFixedDepositAccount().setStatus(Byte.parseByte("0"));

					currentObject.getFixedDepositAccount().setOpeningDate(GeneralUtil.getDateYYYYMMDD());
					currentObject.getFixedDepositAccount().setClosingDate("19000101");
					currentObject.getFixedDepositAccount().setApprovalNo("1");
					
					int Days = Integer.parseInt(GeneralUtil.diffDay(currentObject.getFixedDepositAccount().getDueDate(), currentObject.getFixedDepositAccount().getStartDate()));
					Days ++;		
					currentObject.getFixedDepositAccount().setTotalDays(Days);	

					currentObject.getFixedDepositAccount().setYearlyRate(yearlyrate);
					currentObject.getFixedDepositAccount().setReceiptNo("0");
					
					if(!l_PrdData.getAcccountTypeGL().equals("")) {
						currentObject.setProductGL(l_PrdData.getAcccountTypeGL());
					}else if (!l_PrdData.getCurrencyGL().equals("")) {
						currentObject.setProductGL(l_PrdData.getCurrencyGL());
					} else {
						currentObject.setProductGL(l_PrdData.getProductGL());
					}

					if(!l_PrdData.getAccountTypeCashGL().equals("")) {
						currentObject.setCashInHandGL(l_PrdData.getAccountTypeCashGL());
					}else if (!l_PrdData.getCurrencyCashGL().equals("")) {
						currentObject.setCashInHandGL(l_PrdData.getCurrencyCashGL());
					}else {
						currentObject.setCashInHandGL(l_PrdData.getCashInHandGL());
					}
					currentObject.setSignatureData(new SignatureData());
					currentObject.getSignatureData().setpFilePath("cache\\photos\\signature");
					
					
				return currentObject;
					
	}
	
	public AccountTransactionResponse doOtherBankTransfer(DisPaymentTransactionData disData, String trType, String reqTrnRef,
			String narrative, int isInclusive) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		try {
				// prepare data
				pTransferData.setAmount(disData.getAmount());
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey(trType);
				// to calculate
				//pTransferData.setFinishCutOffTime(GeneralUtility.checkCutOfTime());
				pTransferData.setFinishCutOffTime(false);
				pTransferData.setFromAccNumber(disData.getFromAccount());
				pTransferData.setFromBranchCode(disData.getFromBranch());
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo(reqTrnRef);
				pTransferData.setRemark(narrative);
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(disData.getToAccount());
				pTransferData.setToBranchCode(disData.getToBranch());
//				pTransferData.setToBankCode(getBankSrNo(disData.getToBankCode()));
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				pTransferData.setComm1(disData.getComm1());
				pTransferData.setComm2(disData.getComm2());
				if(isInclusive==1)
					pTransferData.setInclusive(true);
				
				pResData = icbsMgr.doOtherBankTransfer(pTransferData);
				if (pResData.getStatus()) {
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setCharges(String.valueOf(pResData.getComm1()));
					ret.setParam1(pResData.getCcy());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public DataResult reversalOtherBankTransfer(String trnRef) {
		Connection conn = null;

		DataResult l_DataResult = new DataResult();
		try {
			// com.nirvasoft.rp.shared.SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			// com.nirvasoft.rp.shared.SharedLogic.getSystemData().setCurrencyCodes(DBSystemMgr.readCurrencyCodes());
			conn = ConnAdmin.getConn3();
			l_DataResult = DBTransactionMgr.reversalTransactionUpdate(Integer.parseInt(trnRef), true, "", "",
					SharedLogic.getSystemData().getpAuthorizerID(), conn);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (l_DataResult.getStatus()) {
					conn.commit();
					conn.close();
				} else {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return l_DataResult;
	}
	
	public result checkTransref(acctransferreq aReq){
		result response = new result();
		service003Dao s3Dao = new service003Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			response = s3Dao.checkTransref(aReq, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setMsgCode("220");
			response.setMsgDesc("Internal Server Error");
		}finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		
		return response;
	}
	
	public coderesponsedata generateidforcct(int priority ,String branchCode){
		coderesponsedata response = new coderesponsedata();
		CCTDrawingData lData = new CCTDrawingData();
		Connection conn = null;
		
		String transid = "";
		String SettleSrNo = "";
		String recno = "";
		String RecNumber = "";
		lData.setPriority(priority);
		try {
			 //SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			 //com.nirvasoft.rp.shared.SharedLogic.setSystemData(DBSystemMgr.readSystemData());
             //com.nirvasoft.rp.shared.SharedLogic.getSystemData().setCurrencyCodes(DBSystemMgr.readCurrencyCodes());
			conn = ConnAdmin.getConn3();
			SettleSrNo = CCTDrawingDao.getSettleSrNo(conn);
			lData.setSettleSrNumber(SettleSrNo);
			
			String aDate = StrUtil.getCurrentDateyyyyMMdd();
			String[] l_BYear = StrUtil.getBudgetYear(aDate);
			String yr = l_BYear[1];
			int pBYear = Integer.parseInt(aDate.substring(0, 4));
			String str = "CBMOL";
			lData.setRecNumberSerail(CCTDrawingDao.getRecNumberSerialDataList(conn,"", pBYear));
			transid = CCTDrawingDao.getTransID(4, conn);
			
			RecNumber = SharedUtil.leadZeros(String.valueOf(lData.getRecNumberSerail()), 4);
			recno = String.valueOf(branchCode) + str + yr + 1 + RecNumber;
			if(!(transid.equals("") && recno.equals(""))){
				response.setRecno(recno);
				response.setTransid(transid);
				response.setRetcode("300");
				response.setRetmessage("Success");
			}else{
				response.setRetcode("210");
				response.setRetmessage("Failed");
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setRetcode("220");
			response.setRetmessage("Internal Server Error");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (!conn.isClosed() || conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public String getBankSrNo(String bCode ,Connection conn){
		String srno = "";
		service003Dao s3Dao = new service003Dao();
		try {
			srno = s3Dao.getBankSrNo(bCode, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return srno;
	}
	
	//------------ YNDB --------
	public AccountTransactionResponse domerchanttransfer(DisPaymentTransactionData disData, String trType, String reqTrnRef,
			String narrative, int isInclusive) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		try {
				// prepare data
				pTransferData.setAmount(disData.getAmount());
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey(trType);
				// to calculate
				//pTransferData.setFinishCutOffTime(GeneralUtility.checkCutOfTime());
				pTransferData.setFinishCutOffTime(false);
				pTransferData.setFromAccNumber(disData.getFromAccount());
				pTransferData.setFromBranchCode(disData.getFromBranch());
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo(reqTrnRef);
				pTransferData.setRemark(narrative);
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(disData.getToAccount());
				pTransferData.setToBranchCode(disData.getToBranch());
//				pTransferData.setToBankCode(getBankSrNo(disData.getToBankCode()));
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				pTransferData.setComm1(disData.getComm1());
				pTransferData.setComm2(disData.getComm2());
				if(isInclusive==1)
					pTransferData.setInclusive(true);
				
				pResData = icbsMgr.domerchanttransfer(pTransferData);
				if (pResData.getStatus()) {
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setCharges(String.valueOf(pResData.getComm1()));
					ret.setParam1(pResData.getCcy());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public DataResult reversalmerchanttransfer( String trnRef) {
		Connection conn = null;
       
        DataResult l_DataResult = new DataResult();
        DBTransactionMgr mgr = new DBTransactionMgr();
        try {
        	 //com.nirvasoft.rp.shared.SharedLogic.setSystemData(DBSystemMgr.readSystemData());
             //com.nirvasoft.rp.shared.SharedLogic.getSystemData().setCurrencyCodes(DBSystemMgr.readCurrencyCodes());
			 conn = ConnAdmin.getConn3();
			l_DataResult = DBTransactionMgr.reversalTransactionUpdate(Integer.parseInt(trnRef), true, "", "",
			        SharedLogic.getSystemData().getpAuthorizerID(), conn);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (l_DataResult.getStatus()) {
					conn.commit();
					conn.close();
				} else {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return l_DataResult;
	}
	
	public gopaymentresponse domultitransfer(paymenttransactiondata[] datalist,String paymentType,String refCode){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData[] pTransferData = new TransferData[datalist.length];
		TransferData data = new TransferData();
		try {
		
		for(int i=0; i<datalist.length; i++){
			data = new TransferData();
			// prepare data
			data.setAmount(datalist[i].getAmount());
			data.setFinishCutOffTime(false);
			//add from acc
			if(!(datalist[i].getFromaccnumber().equals("") || datalist[i].getFromaccnumber().equals("null") )){
				data.setCurrencyCode(datalist[i].getFromcurcode());
				data.setFromAccNumber(datalist[i].getFromaccnumber());
				data.setFromBranchCode(datalist[i].getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			}
			//add to acc
			if(!(datalist[i].getToaccnumber().equals("") || datalist[i].getToaccnumber().equals("null") )){
				data.setToAccNumber(datalist[i].getToaccnumber());
				data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			}	
			data.setReferenceNo(refCode);
			data.setRemark(datalist[i].getDescription());
			pTransferData[i]=data;
		}
		res = transMgr.domultitransfer(pTransferData,paymentType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}
		return res;
	}
	
	
	//======================== Update Transfer ===================
	
	public AccountTransactionResponse doAccountTransferNew(acctransferreq aReq ) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
			
			l_Conn.setAutoCommit(false);
			
			TransferData pTransferData = new TransferData();
			// prepare data
			pTransferData.setAmount(Double.parseDouble(aReq.getAmount()));
			pTransferData.setCreditIBTKey("");
			pTransferData.setCurrencyCode("");
			pTransferData.setCustomerID("");
			pTransferData.setDebitIBTKey(aReq.getTransfertype());
			pTransferData.setFinishCutOffTime(false);
			pTransferData.setFromAccNumber(aReq.getFromaccnumber());
			pTransferData.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			pTransferData.setFromName("");
			pTransferData.setFromNrc("");
			pTransferData.setFromProductCode("");
			pTransferData.setFromZone("1");
			pTransferData.setInstitutionCode("");
			pTransferData.setIsAgent(false);
			pTransferData.setIsCreditAgent(false);
			pTransferData.setLimitAmount(0);
			pTransferData.setMerchantID("");
			pTransferData.setOnlineSerialNo(0);
			pTransferData.setReferenceNo(aReq.getRefno());
			pTransferData.setRemark(aReq.getRemark());
			pTransferData.setSameAgent(false);
			pTransferData.setTerminalId("");
			pTransferData.setToAccNumber(aReq.getToaccnumber());
			pTransferData.setToBranchCode(aReq.getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			pTransferData.setToName("");
			pTransferData.setToNrc("");
			pTransferData.setToProductCode("");
			pTransferData.setToZone("1");
			pTransferData.setTransactionFee(Double.parseDouble(aReq.getBankcharges()));
			pTransferData.setTransdescription(aReq.getTransdescription());
			
			result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),aReq.getToaccnumber(), 3, l_Conn);
			if(tres.isState()){
				pResData = icbsMgr.postTransferInternal(pTransferData,l_Conn);
				if (pResData.getStatus()) {
					l_Conn.commit();
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					l_Conn.rollback();
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
			}else{
				ret.setCode("210");
				ret.setDesc(tres.getMsgDesc());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public AccountTransactionResponse doInternalTransfer(acctransferreq aReq ) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
				
			l_Conn.setAutoCommit(false);
			
			TransferData pTransferData = new TransferData();
			// prepare data
			pTransferData.setAmount(Double.parseDouble(aReq.getAmount()));
			pTransferData.setCreditIBTKey("");
			pTransferData.setCurrencyCode("");
			pTransferData.setCustomerID("");
			pTransferData.setDebitIBTKey(aReq.getTransfertype());
			pTransferData.setFinishCutOffTime(false);
			pTransferData.setFromAccNumber(aReq.getFromaccnumber());
			pTransferData.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			pTransferData.setFromName("");
			pTransferData.setFromNrc("");
			pTransferData.setFromProductCode("");
			pTransferData.setFromZone("1");
			pTransferData.setInstitutionCode("");
			pTransferData.setIsAgent(false);
			pTransferData.setIsCreditAgent(false);
			pTransferData.setLimitAmount(0);
			pTransferData.setMerchantID("");
			pTransferData.setOnlineSerialNo(0);
			pTransferData.setReferenceNo(aReq.getRefno());
			pTransferData.setRemark(aReq.getRemark());
			pTransferData.setSameAgent(false);
			pTransferData.setTerminalId("");
			pTransferData.setToAccNumber(aReq.getToaccnumber());
			pTransferData.setToBranchCode(aReq.getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			pTransferData.setToName("");
			pTransferData.setToNrc("");
			pTransferData.setToProductCode("");
			pTransferData.setToZone("1");
			pTransferData.setTransactionFee(Double.parseDouble(aReq.getBankcharges()));
			pTransferData.setTransdescription(aReq.getTransdescription());
			aReq.setRefcode(aReq.getRefcode().trim());
			if(aReq.getRefcode().length() > 0) {
				pTransferData.setRefcode(" , "+aReq.getRefcode());
			}
			
			
			result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),aReq.getToaccnumber(), 4,l_Conn);
			if(tres.isState()){
				pResData = icbsMgr.postTransferInternal(pTransferData,l_Conn);
				if (pResData.getStatus()) {
					l_Conn.commit();
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					l_Conn.rollback();
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
			}else{
				ret.setCode("210");
				ret.setDesc(tres.getMsgDesc());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public AccountTransactionResponse domerchanttransfernew(acctransferreq aReq) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
				
			l_Conn.setAutoCommit(false);
			// fill require data
				pTransferData.setAmount(Double.parseDouble(aReq.getAmount()));
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey("");
				pTransferData.setFinishCutOffTime(false);
				pTransferData.setFromAccNumber(aReq.getFromaccnumber());
				pTransferData.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo(aReq.getRefno());
				pTransferData.setRemark("");
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(aReq.getToaccnumber());
				pTransferData.setToBranchCode(aReq.getTobranchcode());
				pTransferData.setToBankCode(getBankSrNo(aReq.getTobankcode(),l_Conn));
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				pTransferData.setComm1(Double.parseDouble(aReq.getDrcommamt()));
				pTransferData.setComm2(Double.parseDouble(aReq.getCrcommamt()));
				if(Integer.parseInt(aReq.getInclusive())==1)
					pTransferData.setInclusive(true);
				pTransferData.setTransdescription(aReq.getTransdescription());
				
				result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),"", 2, l_Conn);
				if(tres.isState()){
					pResData = icbsMgr.domerchanttransfernew(pTransferData,l_Conn);
					if (pResData.getStatus()) {
						l_Conn.commit();
						ret.setCode("300");
						ret.setDesc(pResData.getDescription());
						ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
						ret.setCharges(String.valueOf(pResData.getComm1()));
						ret.setParam1(pResData.getCcy());
						ret.setEffectiveDate(pResData.getEffectiveDate());
					} else {
						l_Conn.rollback();
						ret.setCode("220");
						ret.setDesc(pResData.getDescription());
						ret.setEffectiveDate(pResData.getEffectiveDate());
					}
				}else{
					ret.setCode("210");
					ret.setDesc(tres.getMsgDesc());
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public gopaymentresponse dopaymentTopup(paymenttopupreq aReq,String remark){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData data = new TransferData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				res.setRetcode("220");
				res.setRetmessage("Internal Error (DB connection Failed!)");
				return res;
			}
				
			l_Conn.setAutoCommit(false);
			
			paymenttransactiondata[] datalist = aReq.getDatalist();
			TransferData[] pTransferData = new TransferData[datalist.length];
			for(int i=0; i<datalist.length; i++){
				data = new TransferData();
				// prepare data
				data.setAmount(datalist[i].getAmount());
				data.setCurrencyCode(datalist[i].getFromcurcode());
				data.setFinishCutOffTime(false);
				data.setFromAccNumber(datalist[i].getFromaccnumber());
				data.setFromBranchCode(datalist[i].getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				data.setToAccNumber(datalist[i].getToaccnumber());
				data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				data.setRemark(remark);
				data.setTransdescription(aReq.getTransdescription());
				pTransferData[i]=data;
			}
			
			result tres = authmgr.validateToken(aReq.getToken(),"",datalist[0].getFromaccnumber(),"",2,l_Conn);
			if(tres.isState()){
				res = transMgr.dopaymenttopup(pTransferData,l_Conn);
				if (res.getRetcode().equals("300")) {
					l_Conn.commit();
				} else {
					l_Conn.rollback();
				}
			}else{
				res.setRetcode("210");
				res.setRetmessage(tres.getMsgDesc());
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public gopaymentresponse domerchantpayment(merchantpaymentdatareq aReq,String remark){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData data = new TransferData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				res.setRetcode("220");
				res.setRetmessage("Internal Error (DB connection Failed!)");
				return res;
			}
				
			l_Conn.setAutoCommit(false);
			
			paymenttransactiondata[] datalist = aReq.getDatalist();
			TransferData[] pTransferData = new TransferData[datalist.length];
			for(int i=0; i<datalist.length; i++){
				data = new TransferData();
				// prepare data
				data.setAmount(datalist[i].getAmount());
				data.setCurrencyCode(datalist[i].getFromcurcode());
				data.setFinishCutOffTime(false);
				data.setFromAccNumber(datalist[i].getFromaccnumber());
				data.setFromBranchCode(datalist[i].getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				data.setToAccNumber(datalist[i].getToaccnumber());
				data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				data.setRemark(remark);
				data.setTransdescription(aReq.getTransdescription());
				pTransferData[i]=data;
			}
			
			result tres = authmgr.validateToken(aReq.getToken(),"",datalist[0].getFromaccnumber(),"",2,l_Conn);
			if(tres.isState()){
				res = transMgr.domerchantpayment(pTransferData,l_Conn);
				if (res.getRetcode().equals("300")) {
					l_Conn.commit();
				} else {
					l_Conn.rollback();
				}
			}else{
				res.setRetcode("210");
				res.setRetmessage(tres.getMsgDesc());
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	
	public gopaymentresponse dobulkpayment(bulkpaymentrequest aReq){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData data = new TransferData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				res.setRetcode("220");
				res.setRetmessage("Internal Error (DB connection Failed!)");
				return res;
			}
				
			l_Conn.setAutoCommit(false);
			
			bulkpaymentdatareq[] datalist = aReq.getDatalist();
			TransferData[] pTransferData = new TransferData[datalist.length];
			double drAmt = 0, crAmt=0;
			for(int i=0; i<datalist.length; i++){
				data = new TransferData();

				if(aReq.getPaymenttype().equals("10")){
					drAmt = aReq.getTotalamount();
					crAmt += datalist[i].getAmount();
					
				}else if(aReq.getPaymenttype().equals("20")){
					crAmt = aReq.getTotalamount();
					drAmt += datalist[i].getAmount();
				}
				//add from acc
				data.setFromAccNumber(aReq.getFromaccnumber());
				data.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				//add To acc
				data.setToAccNumber(datalist[i].getToaccnumber());
				data.setAmount(datalist[i].getAmount());
				data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				data.setRemark(datalist[i].getDescription());
				data.setReferenceNo(aReq.getRefcode());
				data.setFinishCutOffTime(false);
				data.setTransdescription(aReq.getTransdescription());
				pTransferData[i]=data;
			}
			if(drAmt != crAmt){
				res.setRetcode("210");
				res.setRetmessage("Invalid Amount!");
				return res;
			}
			
			result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),"",2,l_Conn);
			if(tres.isState()){
				res = transMgr.dobulkpayment(pTransferData,aReq.getPaymenttype(),aReq.getTotalamount(),l_Conn);
				if (res.getRetcode().equals("300")) {
					l_Conn.commit();
				} else {
					l_Conn.rollback();
				}
			}else{
				res.setRetcode("210");
				res.setRetmessage(tres.getMsgDesc());
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public AccountTransactionResponse doOtherBankTransfernew(acctransferreq aReq) {
		//DisPaymentTransactionData disData, String trType, String reqTrnRef, String narrative, int isInclusive
		//transdata, aReq.getTransfertype(), aReq.getRefno(),aReq.getRemark(), Integer.parseInt(aReq.getInclusive())
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
				
			l_Conn.setAutoCommit(false);
				// prepare data
				pTransferData.setAmount(Double.parseDouble(aReq.getAmount()));
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey(aReq.getTransfertype());
				pTransferData.setFinishCutOffTime(false);
				pTransferData.setFromAccNumber(aReq.getFromaccnumber());
				pTransferData.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo(aReq.getRefno());
				pTransferData.setRemark(aReq.getRemark());
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(aReq.getToaccnumber());
				pTransferData.setToBranchCode(aReq.getTobranchcode());
				pTransferData.setToBankCode(getBankSrNo(aReq.getTobankcode(),l_Conn));
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				pTransferData.setComm1(Double.parseDouble(aReq.getDrcommamt()));
				pTransferData.setComm2(Double.parseDouble(aReq.getCrcommamt()));
				if(Integer.parseInt(aReq.getInclusive())==1)
					pTransferData.setInclusive(true);
				pTransferData.setTransdescription(aReq.getTransdescription());
				
				result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),"", 2,l_Conn);
				if(tres.isState()){
					pResData = icbsMgr.doOtherBankTransfernew(pTransferData,l_Conn);
					if (pResData.getStatus()) {
						l_Conn.commit();
						ret.setCode("300");
						ret.setDesc(pResData.getDescription());
						ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
						ret.setCharges(String.valueOf(pResData.getComm1()));
						ret.setParam1(pResData.getCcy());
						ret.setEffectiveDate(pResData.getEffectiveDate());
					} else {
						l_Conn.rollback();
						ret.setCode("220");
						ret.setDesc(pResData.getDescription());
						ret.setEffectiveDate(pResData.getEffectiveDate());
					}
				}else{
					ret.setCode("210");
					ret.setDesc(tres.getMsgDesc());
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public gopaymentresponse domultitransfernew(bulkpaymentrequest aReq){//type= 1(topup), 2(merchant), 3 bulkPayment
		gopaymentresponse res = new gopaymentresponse();
		TransactionMgr transMgr = new TransactionMgr();
		TransferData data = new TransferData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				res.setRetcode("220");
				res.setRetmessage("Internal Error (DB connection Failed!)");
				return res;
			}
				
			l_Conn.setAutoCommit(false);
			
			bulkpaymentdatareq[] datalist = aReq.getDatalist();
			TransferData[] pTransferData = new TransferData[datalist.length];
			double drAmt = 0, crAmt=0;
			for(int i=0; i<datalist.length; i++){
				data = new TransferData();

				if(aReq.getPaymenttype().equals("10")){
					drAmt = aReq.getTotalamount();
					crAmt += datalist[i].getAmount();
					
				}else if(aReq.getPaymenttype().equals("20")){
					crAmt = aReq.getTotalamount();
					drAmt += datalist[i].getAmount();
				}
				//add from acc
				data.setFromAccNumber(aReq.getFromaccnumber());
				data.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				//add To acc
				data.setToAccNumber(datalist[i].getToaccnumber());
				data.setAmount(datalist[i].getAmount());
				data.setToBranchCode(datalist[i].getToaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				data.setRemark(datalist[i].getDescription());
				data.setReferenceNo(aReq.getRefcode());
				data.setFinishCutOffTime(false);
				data.setTransdescription(aReq.getTransdescription());
				pTransferData[i]=data;
			}
			if(drAmt != crAmt){
				res.setRetcode("210");
				res.setRetmessage("Invalid Amount!");
				return res;
			}
			
			result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),"",2,l_Conn);
			if(tres.isState()){
				res = transMgr.domultitransfernew(pTransferData,aReq.getPaymenttype(),aReq.getTotalamount(),l_Conn);
				if (res.getRetcode().equals("300")) {
					l_Conn.commit();
				} else {
					l_Conn.rollback();
				}
			}else{
				res.setRetcode("210");
				res.setRetmessage(tres.getMsgDesc());
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Internal server error");
		}finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public AccountTransactionResponse doAccountDeposit(accdepositreq aReq ) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
			
			l_Conn.setAutoCommit(false);
			
			DepositData pDepositData = new DepositData();
			// prepare data
			pDepositData.setAmount(Double.parseDouble(aReq.getAmount()));
			pDepositData.setCreditIBTKey("");
			pDepositData.setCurrencyCode("");
			pDepositData.setCustomerID("");
			pDepositData.setFinishCutOffTime(false);
			pDepositData.setAccNumber(aReq.getAccnumber());
			if(aReq.getGlcheck().equals("1")) {
				pDepositData.setBranchCode(aReq.getBranchcode());
				pDepositData.setCurrencyCode(aReq.getCurrencycode());
				pDepositData.setIsGL(true);
			}else {
				pDepositData.setBranchCode(aReq.getAccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			}
			pDepositData.setFromProductCode("");
			pDepositData.setInstitutionCode("");
			pDepositData.setIsAgent(false);
			pDepositData.setIsCreditAgent(false);
			pDepositData.setLimitAmount(0);
			pDepositData.setMerchantID("");
			pDepositData.setOnlineSerialNo(0);
			pDepositData.setReferenceNo(aReq.getRefno());
			pDepositData.setRemark(aReq.getRemark());
			pDepositData.setSameAgent(false);
			pDepositData.setTerminalId("");
			pDepositData.setToProductCode("");
			pDepositData.setTransactionFee(0);
			pDepositData.setTransDescription("");
		
			result tres = authmgr.validateToken(aReq.getToken(),aReq.getGlcheck(),aReq.getAccnumber(),"", 7, l_Conn);
			if(tres.isState()){
				pResData = icbsMgr.postDeposit(pDepositData,l_Conn);
				if (pResData.getStatus()) {
					l_Conn.commit();
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					l_Conn.rollback();
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
			}else{
				ret.setCode("210");
				ret.setDesc(tres.getMsgDesc());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public AccountTransactionResponse doAccountWithdraw(accwithdrawreq aReq ) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
			
			l_Conn.setAutoCommit(false);
			
			WithdrawData pWithdrawData = new WithdrawData();
			// prepare data
			pWithdrawData.setAmount(Double.parseDouble(aReq.getAmount()));
			pWithdrawData.setCreditIBTKey("");
			pWithdrawData.setCurrencyCode("");
			pWithdrawData.setCustomerID("");
			pWithdrawData.setFinishCutOffTime(false);
			pWithdrawData.setAccNumber(aReq.getAccnumber());
			if(aReq.getGlcheck().equals("1")) {
				pWithdrawData.setBranchCode(aReq.getBranchcode());
				pWithdrawData.setCurrencyCode(aReq.getCurrencycode());
				pWithdrawData.setIsGL(true);
			}else {
				pWithdrawData.setBranchCode(aReq.getAccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			}
			pWithdrawData.setFromProductCode("");
			pWithdrawData.setInstitutionCode("");
			pWithdrawData.setIsAgent(false);
			pWithdrawData.setIsCreditAgent(false);
			pWithdrawData.setLimitAmount(0);
			pWithdrawData.setMerchantID("");
			pWithdrawData.setOnlineSerialNo(0);
			pWithdrawData.setReferenceNo(aReq.getRefno());
			pWithdrawData.setRemark(aReq.getRemark());
			pWithdrawData.setSameAgent(false);
			pWithdrawData.setTerminalId("");
			pWithdrawData.setToProductCode("");
			pWithdrawData.setTransactionFee(0);
			pWithdrawData.setTransDescription("");
			pWithdrawData.setReferenceNo(aReq.getRefno());
			
			result tres = authmgr.validateToken(aReq.getToken(),aReq.getGlcheck(),aReq.getAccnumber(),"", 7, l_Conn);
			if(tres.isState()){
				pResData = icbsMgr.postWithdraw(pWithdrawData,l_Conn);
				if (pResData.getStatus()) {
					l_Conn.commit();
					ret.setCode("300");
					ret.setDesc(pResData.getDescription());
					ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
					ret.setEffectiveDate(pResData.getEffectiveDate());
				} else {
					l_Conn.rollback();
					ret.setCode("220");
					ret.setDesc(pResData.getDescription());
					ret.setEffectiveDate(pResData.getEffectiveDate());
				}
			}else{
				ret.setCode("210");
				ret.setDesc(tres.getMsgDesc());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public AccountTransactionResponse otherBankReversal(acctransferreq aReq) {
		AccountTransactionResponse ret = new AccountTransactionResponse();
		TransactionMgr icbsMgr = new TransactionMgr();
		TransferData pTransferData = new TransferData();
		SMSReturnData pResData = new SMSReturnData();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				ret.setCode("220");
				ret.setDesc("Internal Error (DB connection Failed!)");
				return ret;
			}
				
			l_Conn.setAutoCommit(false);
			// fill require data
				pTransferData.setAmount(Double.parseDouble(aReq.getAmount()));
				pTransferData.setCreditIBTKey("");
				pTransferData.setCurrencyCode("");
				pTransferData.setCustomerID("");
				pTransferData.setDebitIBTKey("");
				pTransferData.setFinishCutOffTime(false);
				pTransferData.setFromAccNumber(aReq.getFromaccnumber());
				pTransferData.setFromBranchCode(aReq.getFromaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				pTransferData.setFromName("");
				pTransferData.setFromNrc("");
				pTransferData.setFromProductCode("");
				pTransferData.setFromZone("1");
				pTransferData.setInstitutionCode("");
				pTransferData.setIsAgent(false);
				pTransferData.setIsCreditAgent(false);
				pTransferData.setLimitAmount(0);
				pTransferData.setMerchantID("");
				pTransferData.setOnlineSerialNo(0);
				pTransferData.setReferenceNo("");
				pTransferData.setRemark("");
				pTransferData.setSameAgent(false);
				pTransferData.setTerminalId("");
				pTransferData.setToAccNumber(aReq.getToaccnumber());
				pTransferData.setToBranchCode("");
				pTransferData.setToBankCode("");
				pTransferData.setToName("");
				pTransferData.setToNrc("");
				pTransferData.setToProductCode("");
				pTransferData.setToZone("1");
				pTransferData.setTransactionFee(0);
				pTransferData.setComm1(0.00);
				pTransferData.setComm2(0.00);
				pTransferData.setTransdescription("");
				
				result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getFromaccnumber(),"", 2, l_Conn);
				if(tres.isState()){
					pResData = icbsMgr.doOtherBankReversal(pTransferData,l_Conn);
					if (pResData.getStatus()) {
						l_Conn.commit();
						ret.setCode("300");
						ret.setDesc(pResData.getDescription());
						ret.setFlexcubeid(String.valueOf(pResData.getTransactionNumber()));
						ret.setCharges(String.valueOf(pResData.getComm1()));
						ret.setParam1(pResData.getCcy());
						ret.setEffectiveDate(pResData.getEffectiveDate());
					} else {
						l_Conn.rollback();
						l_Conn.rollback();
						ret.setCode("220");
						ret.setDesc(pResData.getDescription());
						ret.setEffectiveDate(pResData.getEffectiveDate());
					}
				}else{
					ret.setCode("210");
					ret.setDesc(tres.getMsgDesc());
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!l_Conn.isClosed() || l_Conn != null) {
					l_Conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
}
