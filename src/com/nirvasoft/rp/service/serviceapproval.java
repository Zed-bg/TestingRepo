package com.nirvasoft.rp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.framework.ServerSession;
import com.nirvasoft.rp.mgr.DBLoanMgr;
import com.nirvasoft.rp.mgr.DBSystemMgr;
import com.nirvasoft.rp.mgr.authmgr;
import com.nirvasoft.rp.mgr.service003Mgr;
import com.nirvasoft.rp.shared.acctransferreq;
import com.nirvasoft.rp.shared.accwithdrawreq;
import com.nirvasoft.rp.shared.bulkpaymentdatareq;
import com.nirvasoft.rp.shared.bulkpaymentrequest;
import com.nirvasoft.rp.shared.chequebookreq;
import com.nirvasoft.rp.shared.chequebookres;
import com.nirvasoft.rp.shared.chequeverifyreq;
import com.nirvasoft.rp.shared.chequeverifyres;
import com.nirvasoft.rp.shared.coderequestcct;
import com.nirvasoft.rp.shared.coderesponsedata;
import com.nirvasoft.rp.shared.fixedaccountopenreq;
import com.nirvasoft.rp.shared.fixedaccountopenres;
import com.nirvasoft.rp.shared.AccountTransactionResponse;
import com.nirvasoft.rp.shared.ChequeEnquiry;
import com.nirvasoft.rp.shared.ChequeEnquiryRes;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.DisPaymentTransactionData;
import com.nirvasoft.rp.shared.LAODFScheduleData;
import com.nirvasoft.rp.shared.LoanRepaymentReq;
import com.nirvasoft.rp.shared.LoanRepaymentRes;
import com.nirvasoft.rp.shared.LoanScheduleRepaymentReq;
import com.nirvasoft.rp.shared.ResponseData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.accdepositreq;
import com.nirvasoft.rp.shared.gopaymentresponse;
import com.nirvasoft.rp.shared.merchantpaymentdatareq;
import com.nirvasoft.rp.shared.paymenttransactiondata;
import com.nirvasoft.rp.shared.stopchequeres;
import com.nirvasoft.rp.shared.userdata;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.paymenttopupreq;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.ServerGlobal;
import com.nirvasoft.rp.shared.SharedLogic;

@Path("/service-approval")
public class serviceapproval {
	
	@Context
	HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	
	@POST
	@Path("approve-cheque-book")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public chequebookres dochequebookrequest(chequebookreq req) {
		
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-book Request Message : ", GeneralUtil.toGson(req));
		
		chequebookres response = new chequebookres();
		ResponseData vres = new ResponseData();
		service003Mgr sbmgr = new service003Mgr();
		//validate req
		vres = validatechequebookreq(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-book Response Message : ", GeneralUtil.toGson(response));
			
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 6);
		if(tres.isState()){
			response = sbmgr.dochequebookrequest(req);
			if(response.getRetcode().equals("300")){
				
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-book Response Message : ", GeneralUtil.toGson(response));
				
			}else{
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-book Response Message : ", GeneralUtil.toGson(response));
			}
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-book Response Message : ", GeneralUtil.toGson(response));
			
		}
		
		return response;
	}
	
	@POST
	@Path("approve-stop-cheque")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public stopchequeres dostopcheque(ChequeEnquiry req) {
		
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getToken(), "Do approve-stop-cheque Request Message : ", GeneralUtil.toGson(req));
		
		stopchequeres response = new stopchequeres();
		ResponseData vres = new ResponseData();
		service003Mgr sbmgr = new service003Mgr();
		//validate req
		vres = validatestopchequereq(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-stop-cheque Response Message : ", GeneralUtil.toGson(response));
			
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 6);
		if(tres.isState()){
			response = sbmgr.stopCheque(req.getAccnumber(), req.getChequenumber(),"");
			if(response.getRetcode().equals("300")){
				
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-stop-cheque Response Message : ", GeneralUtil.toGson(response));
				
			}else {
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-stop-cheque Response Message : ", GeneralUtil.toGson(response));
			}
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-stop-cheque Response Message : ", GeneralUtil.toGson(response));
			
		}
		return response;
	}
	
	@POST
	@Path("approve-cheque-verify")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public chequeverifyres dochequeverify(chequeverifyreq req) {
		
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-verify Request Message : ", GeneralUtil.toGson(req));
		
		chequeverifyres response = new chequeverifyres();
		ResponseData vres = new ResponseData();
		service003Mgr sbmgr = new service003Mgr();
		//validate req
		vres = validatechequeverifyreq(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-verify Response Message : ", GeneralUtil.toGson(response));
			
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 6);
		if(tres.isState()){
			response = sbmgr.dochequeverify(req);
			if(!response.getRetcode().equals("300")){
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-verify Response Message : ", GeneralUtil.toGson(response));
			}else {
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-verify Response Message : ", GeneralUtil.toGson(response));
			}
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-cheque-verify Response Message : ", GeneralUtil.toGson(response));
			
		}
		return response;
	}
	
	@POST
	@Path("approve-product-fd")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public fixedaccountopenres dofixedaccountopen(fixedaccountopenreq req) {
		
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getToken(), "Do approve-product-fd  Request Message : ", GeneralUtil.toGson(req));
		
		//getPath();
		fixedaccountopenres response = new fixedaccountopenres ();
		ResponseData vres = new ResponseData();
		service003Mgr sbmgr = new service003Mgr();
		//validate req
		vres = validatefixedaccountopenreq(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-product-fd  Response Message : ", GeneralUtil.toGson(response));
			
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 6);
		if(tres.isState()){
			response = sbmgr.dofixedaccountopen(req);
			if(!response.getRetcode().equals("300")){
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-product-fd Response Message : ", GeneralUtil.toGson(response));	
			}else {
				GeneralUtil.writeDebugLog(req.getToken(), "Do approve-product-fd Response Message : ", GeneralUtil.toGson(response));
			}
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getToken(), "Do approve-product-fd Response Message : ", GeneralUtil.toGson(response));
			
		}
		return response;
	}
	
		
		@POST
		@Path("loan")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public LoanRepaymentRes loanRepayment(LoanRepaymentReq aReq) {

			GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(aReq));
			LoanRepaymentRes response = new LoanRepaymentRes();
			result validateResult = new result();
			DBLoanMgr l_mgr = new DBLoanMgr();
			validateResult = validateLoanRepayment(aReq);
			if (!validateResult.isState()) {
				response.setRetcode(validateResult.getMsgCode());
				response.setRetmessage(validateResult.getMsgDesc());
				GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				return response;
			}
			try {
				response = l_mgr.postLoanRepayment(aReq);
				if(!response.getRetcode().equals("300")){
						GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				}else {
					GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.setRetcode("220");
					response.setRetmessage("Internal server error");
					GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
			}
			return response;
		}
	
		@POST
		@Path("loan-schedule")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public LoanRepaymentRes postLoanScheduleRepayment(LoanScheduleRepaymentReq aReq) {

			GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(aReq));
			LoanRepaymentRes response = new LoanRepaymentRes();
			result validateResult = new result();
			DBLoanMgr l_mgr = new DBLoanMgr();
			validateResult = validateLoanScheduleRepayment(aReq);
			if (!validateResult.isState()) {
				response.setRetcode(validateResult.getMsgCode());
				response.setRetmessage(validateResult.getMsgDesc());
				GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				return response;
			}
			try {
				response = l_mgr.postLoanScheduleRepayment(aReq);
				if(response.getRetcode().equals("300")){
						GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				}else {
					GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.setRetcode("220");
					response.setRetmessage("Internal server error");
					GeneralUtil.writeDebugLog(aReq.getSyskey(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
			}
			return response;
		}
	//==================================== validation ==========================================
	public ResponseData validateBulkPayment(bulkpaymentrequest reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else if (reqData.getPaymenttype().equalsIgnoreCase("null") || reqData.getPaymenttype().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Payment type is manadatory!");
		}else if (reqData.getFromaccnumber().equalsIgnoreCase("null") || reqData.getFromaccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("From account number is manadatory!");
		}else if (reqData.getTotalamount() <= 0) {
			response.setCode("210");
			response.setDesc("Total amount is manadatory!");
		}else if (reqData.getDatalist().length>0) {
			bulkpaymentdatareq[] data = reqData.getDatalist();
			for(int i=0; i<data.length ; i++){
				if (data[i].getRowno().equalsIgnoreCase("null") || data[i].getRowno().equalsIgnoreCase("")) {
					response.setCode("210");
					response.setDesc("Row number is manadatory!");
					break;
				}else if (data[i].getToaccnumber().equalsIgnoreCase("null") || data[i].getToaccnumber().equalsIgnoreCase("")) {
					response.setCode("210");
					response.setDesc("To account number is manadatory!");
					break;
				}else if (data[i].getAmount() <= 0) {
					response.setCode("210");
					response.setDesc("Amount is manadatory!");
					break;
				}else{
					response.setCode("300");
					response.setDesc("Success");
				}
			}
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	private ResponseData validatepaymentmerchant(merchantpaymentdatareq aReq){
		ResponseData res = new ResponseData();
		if (aReq.getToken().equalsIgnoreCase("null") || aReq.getToken().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Token is manadatory!");
		}else if (aReq.getMerchantname().equalsIgnoreCase("null") || aReq.getMerchantname().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Merchant name is manadatory!");
		}else if (aReq.getReferencenumber().equalsIgnoreCase("null") || aReq.getReferencenumber().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Reference number is manadatory!");
		}else if (aReq.getDatalist() == null ) {
			res.setCode("210");
			res.setDesc("Data list is manadatory!");
		}else if (aReq.getDatalist().length>0) {
			paymenttransactiondata[] data = aReq.getDatalist();
			for(int i=0; i<data.length ; i++){
				if (data[i].getFromaccnumber().equalsIgnoreCase("null") || data[i].getFromaccnumber().equalsIgnoreCase("")) {
					res.setCode("210");
					res.setDesc("From account number is manadatory!");
				}else if (data[i].getToaccnumber().equalsIgnoreCase("null") || data[i].getToaccnumber().equalsIgnoreCase("")) {
					res.setCode("210");
					res.setDesc("To account number is manadatory!");
				}else if (data[i].getFromcurcode().equalsIgnoreCase("null") || data[i].getFromcurcode().equalsIgnoreCase("")) {
					res.setCode("210");
					res.setDesc("From currency code is manadatory!");
				}else if (data[i].getAmount()<=0) {
					res.setCode("210");
					res.setDesc("Amount is manadatory!");
				}else{
					res.setCode("300");
					res.setDesc("Success");
				}
			}
		}else{
			res.setCode("300");
			res.setDesc("Success");
		}
		return res;
	}
	
	private ResponseData validatepaymenttopup(paymenttopupreq aReq){
		ResponseData res = new ResponseData();
		if (aReq.getToken().equalsIgnoreCase("null") || aReq.getToken().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Token is manadatory!");
		}else if (aReq.getTopupnumber().equalsIgnoreCase("null") || aReq.getTopupnumber().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Topup number is manadatory!");
		}else if (aReq.getTopupamount() <= 0) {
			res.setCode("210");
			res.setDesc("Topup amount is manadatory!");
		}else if (aReq.getDatalist() == null ) {
			res.setCode("210");
			res.setDesc("Data list is manadatory!");
		}else if (aReq.getDatalist().length>0) {
			paymenttransactiondata[] data = aReq.getDatalist();
			for(int i=0; i<data.length ; i++){
				if (data[i].getFromaccnumber().equalsIgnoreCase("null") || data[i].getFromaccnumber().equalsIgnoreCase("")) {
					res.setCode("210");
					res.setDesc("From account number is manadatory!");
				}else if (data[i].getToaccnumber().equalsIgnoreCase("null") || data[i].getToaccnumber().equalsIgnoreCase("")) {
					res.setCode("210");
					res.setDesc("To account number is manadatory!");
				}else if (data[i].getFromcurcode().equalsIgnoreCase("null") || data[i].getFromcurcode().equalsIgnoreCase("")) {
					res.setCode("210");
					res.setDesc("From currency code is manadatory!");
				}else if (data[i].getAmount()<=0) {
					res.setCode("210");
					res.setDesc("Amount is manadatory!");
				}else{
					res.setCode("300");
					res.setDesc("Success");
				}
			}
		}else{
			res.setCode("300");
			res.setDesc("Success");
		}
		return res;
	}
	
	private result validateDoAccTransfer(acctransferreq aReq, String aTransferType) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		}
		if (aReq.getFromaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("From Account is mandatory");

			return res;
		}
		if (aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		} else {
			aReq.setAmount(aReq.getAmount().replace(",", ""));
		}
		return res;
	}
	
	private result validateGoAccTransfer(acctransferreq aReq, String aTransferType) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		}
		if (aReq.getFromaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("From Account is mandatory");

			return res;
		}
		if (aReq.getToaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("To Account is mandatory");

			return res;
		}
		if (aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		} else {
			aReq.setAmount(aReq.getAmount().replace(",", ""));
		}
		return res;
	}
	
	public ResponseData validatechequebookreq(chequebookreq reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else if (reqData.getAccnumber().equalsIgnoreCase("null") || reqData.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("From account number is manadatory!");
		}else if (reqData.getChequesheetlength() <= 0) {
			response.setCode("210");
			response.setDesc("Cheque sheet length is manadatory!");
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	public ResponseData validatestopchequereq(ChequeEnquiry reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		} else if (reqData.getAccnumber().equalsIgnoreCase("null") || reqData.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account No. is manadatory");
		}else if (reqData.getChequenumber().equalsIgnoreCase("null") || reqData.getChequenumber().equals("") ) {
			response.setDesc("Cheque No. is mandatory");
			response.setCode("210");
		}else {
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	public ResponseData validatechequeverifyreq(chequeverifyreq reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		} else if (reqData.getAccnumber().equalsIgnoreCase("null") || reqData.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account No. is manadatory");
		}else if (reqData.getChequenumber().equalsIgnoreCase("null") || reqData.getChequenumber().equals("") ) {
			response.setDesc("Cheque No. is mandatory");
			response.setCode("210");
		}else if (reqData.getAmount().equalsIgnoreCase("null") || reqData.getAmount().equals("") || reqData.getAmount().equals("0") || reqData.getAmount().equals("0.00")) {
			response.setDesc("Amount is mandatory");
			response.setCode("210");
		}else if (reqData.getT2().equalsIgnoreCase("null") || reqData.getT2().equals("") ) {
			response.setDesc("Currency is mandatory");
			response.setCode("210");
		}else if (reqData.getT3().equalsIgnoreCase("null") || reqData.getT3().equals("") ) {
			response.setDesc("Name is mandatory");
			response.setCode("210");
		}else if (reqData.getT4().equalsIgnoreCase("null") || reqData.getT4().equals("") ) {
			response.setDesc("NRC is mandatory");
			response.setCode("210");
		}else {
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	public ResponseData validatefixedaccountopenreq(fixedaccountopenreq reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else if (reqData.getFromaccnumber().equalsIgnoreCase("null") || reqData.getFromaccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("From Account Number is manadatory!");
		}else if (reqData.getCustomerid().equalsIgnoreCase("null") || reqData.getCustomerid().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Customer ID is manadatory!");
		}else if (reqData.getTenure().equalsIgnoreCase("null") || reqData.getTenure().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Tenure is manadatory!");
		}else if (reqData.getYearlyrate().equalsIgnoreCase("null") || reqData.getYearlyrate().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Yearly Rate is manadatory!");
		}else if (reqData.getAmount().equalsIgnoreCase("null") || reqData.getAmount().equalsIgnoreCase("") || reqData.getAmount().equals("0") || reqData.getAmount().equals("0.00")) {
			response.setCode("210");
			response.setDesc("Amount is manadatory!");
		}else if (reqData.getMaturity().equalsIgnoreCase("null") || reqData.getMaturity().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Maturity is manadatory!");
		}else if (reqData.getStarttype().equalsIgnoreCase("null") || reqData.getStarttype().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Start type is manadatory!");
		}else if (reqData.getStartdate().equalsIgnoreCase("null") || reqData.getStartdate().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Start date is manadatory!");
		}else if (reqData.getInterestpaytype().equalsIgnoreCase("null") || reqData.getInterestpaytype().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Interest pay type is manadatory!");
		}else if (reqData.getAcctype().equalsIgnoreCase("null") || reqData.getAcctype().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account type is manadatory!");
		}else if (reqData.getRtype().equalsIgnoreCase("null") || reqData.getRtype().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Relation type is manadatory!");
		}else {
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	private result validateOBTransfer(acctransferreq aReq) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		}
		if (aReq.getFromaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("From Account is mandatory");

			return res;
		}
		if (aReq.getToaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("To Account is mandatory");

			return res;
		}
		if (aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		}
		if (aReq.getTobankcode().equals("") || aReq.getTobankcode().equals("0")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Creditor Bank");
			return res;
		}
		if (aReq.getTobranchcode().equals("") || aReq.getTobranchcode().equals("0")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Creditor Branch");
			return res;
		}
		if (aReq.getInclusive().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Inclusive type");
			return res;
		}
		if (aReq.getDrcommamt().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Commission Amount");
			return res;
		}
		if (aReq.getCrcommamt().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Commission Amount");
			return res;
		}else {
			aReq.setAmount(aReq.getAmount().replace(",", ""));
		}
		return res;
	}
	
	private result validateOBTransferReverse(acctransferreq aReq) {
		result response = new result();
		if (aReq.getToken().equalsIgnoreCase("null") || aReq.getToken().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("Token is manadatory!");
		}else if (aReq.getRefno().equalsIgnoreCase("null") || aReq.getRefno().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("Reference number is manadatory!");
		}else if (aReq.getFromaccnumber().equalsIgnoreCase("null") || aReq.getFromaccnumber().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("From account number is manadatory!");
		}else if (aReq.getToaccnumber().equalsIgnoreCase("null") || aReq.getToaccnumber().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("To account number is manadatory!");
		}if (aReq.getToaccnumber().equalsIgnoreCase("null") || aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			response.setMsgCode("210");
			response.setMsgDesc("Amount is manadatory!");
		}else{
			response.setState(true);
			response.setMsgCode("300");
			response.setMsgDesc("Successful");
		}
		return response;
	}
	
	public ResponseData validategenidcctres(coderequestcct reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}if (reqData.getPriority().equalsIgnoreCase("null") || reqData.getPriority().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Priority is manadatory!");
		}if (reqData.getFrombranchcode().equalsIgnoreCase("null") || reqData.getFrombranchcode().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Branch Code year is manadatory!");
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	private result validateMCTransfer(acctransferreq aReq) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		}
		if (aReq.getFromaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("From Account is mandatory");

			return res;
		}
		if (aReq.getToaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("To Account is mandatory");

			return res;
		}
		if (aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		}
		if (aReq.getTobankcode().equals("") || aReq.getTobankcode().equals("0")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Creditor Bank");
			return res;
		}
		if (aReq.getTobranchcode().equals("") || aReq.getTobranchcode().equals("0")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Creditor Branch");
			return res;
		}
		if (aReq.getInclusive().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Inclusive type");
			return res;
		}
		if (aReq.getDrcommamt().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Commission Amount");
			return res;
		}
		if (aReq.getCrcommamt().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Commission Amount");
			return res;
		}else {
			aReq.setAmount(aReq.getAmount().replace(",", ""));
		}
		return res;
	}
	
	private result validateMCTransferReverse(acctransferreq aReq) {
		result response = new result();
		if (aReq.getToken().equalsIgnoreCase("null") || aReq.getToken().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("Token is manadatory!");
		}else if (aReq.getRefno().equalsIgnoreCase("null") || aReq.getRefno().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("Reference number is manadatory!");
		}else if (aReq.getFromaccnumber().equalsIgnoreCase("null") || aReq.getFromaccnumber().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("From account number is manadatory!");
		}else if (aReq.getToaccnumber().equalsIgnoreCase("null") || aReq.getToaccnumber().equalsIgnoreCase("")) {
			response.setMsgCode("210");
			response.setMsgDesc("To account number is manadatory!");
		}if (aReq.getToaccnumber().equalsIgnoreCase("null") || aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			response.setMsgCode("210");
			response.setMsgDesc("Amount is manadatory!");
		}else{
			response.setState(true);
			response.setMsgCode("300");
			response.setMsgDesc("Successful");
		}
		return response;
	}
	
	private result validateDoAccDeposit(accdepositreq aReq, String aTransferType) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		}
		if (aReq.getAccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Account is mandatory");

			return res;
		}
		if (aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		} else {
			aReq.setAmount(aReq.getAmount().replace(",", ""));
		}
		
		if (aReq.getGlcheck().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("GL Check is mandatory");
			return res;
		}
		
		if (aReq.getGlcheck().equals("0")) {
			if (aReq.getBranchcode().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("If GL Account , Branch Code is mandatory");

				return res;
			}
			if (aReq.getCurrencycode().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("If GL Account , Currency Code is mandatory");

				return res;
			}
		}
		return res;
	}
	
	
	private result validateDoAccWithdraw(accwithdrawreq aReq, String aTransferType) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		}
		if (aReq.getAccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Account is mandatory");

			return res;
		}
		if (aReq.getAmount().equals("") || aReq.getAmount().equals("0") || aReq.getAmount().equals("0.00")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		} else {
			aReq.setAmount(aReq.getAmount().replace(",", ""));
		}
		
		if (aReq.getRefno().equals("") ) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid Ref No");
			return res;
		} 
		
		if (aReq.getGlcheck().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("GL Check is mandatory");
			return res;
		}
		
		if (aReq.getGlcheck().equals("1")) {
			if (aReq.getBranchcode().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("If GL Account , Branch Code is mandatory");

				return res;
			}
			if (aReq.getCurrencycode().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("If GL Account , Currency Code is mandatory");

				return res;
			}
		}
		return res;
	}
	
	private result validateLoanRepayment(LoanRepaymentReq aReq) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		} else if (aReq.getLoanaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Account is mandatory");

			return res;
		} else if (aReq.getLinkedaccnumber().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Linked Account is mandatory");

			return res;
		} else if (aReq.getAmount() <= 0) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Invalid amount");
			return res;
		}
		return res;
	}
	
	private result validateLoanScheduleRepayment(LoanScheduleRepaymentReq aReq) {
		result res = new result();
		res.setState(true);
		if (aReq.getToken().equals("")) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Token is mandatory");
			return res;
		} else if (aReq.getData() == null) {
			res.setState(false);
			res.setMsgCode("210");
			res.setMsgDesc("Loan Data is mandatory");

			return res;
		}  else if (aReq.getData() != null) {
			LAODFScheduleData data = aReq.getData();
			if (data.getAccnumber().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("Loan Account is mandatory");

				return res;
			} else if (data.getBackaccnumber().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("Link Account is mandatory");

				return res;
			} else if (data.getRepaymentamount().equals("")) {
				res.setState(false);
				res.setMsgCode("210");
				res.setMsgDesc("Amount is mandatory");

				return res;
			}
		}
		
		return res;
	}
	
	
}
