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
import com.nirvasoft.rp.mgr.service001mgr;
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
import com.nirvasoft.rp.shared.LoanRequest;
import com.nirvasoft.rp.shared.LoanResponse;
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

@Path("/service-loan")
public class serviceloan {
	
	@Context
	HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	
	
		
		@POST
		@Path("loan")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public LoanRepaymentRes loanRepayment(LoanRepaymentReq aReq) {

			GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(aReq));
			LoanRepaymentRes response = new LoanRepaymentRes();
			result validateResult = new result();
			DBLoanMgr l_mgr = new DBLoanMgr();
			validateResult = validateLoanRepayment(aReq);
			if (!validateResult.isState()) {
				response.setRetcode(validateResult.getMsgCode());
				response.setRetmessage(validateResult.getMsgDesc());
				GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				return response;
			}
			try {
				response = l_mgr.postLoanRepayment(aReq);
				if(!response.getRetcode().equals("300")){
						GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.setRetcode("220");
					response.setRetmessage("Internal server error");
					GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
			}
			return response;
		}
	
		@POST
		@Path("loan-schedule")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public LoanRepaymentRes postLoanScheduleRepayment(LoanScheduleRepaymentReq aReq) {

			GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(aReq));
			LoanRepaymentRes response = new LoanRepaymentRes();
			result validateResult = new result();
			DBLoanMgr l_mgr = new DBLoanMgr();
			validateResult = validateLoanScheduleRepayment(aReq);
			if (!validateResult.isState()) {
				response.setRetcode(validateResult.getMsgCode());
				response.setRetmessage(validateResult.getMsgDesc());
				GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				return response;
			}
			try {
				response = l_mgr.postLoanScheduleRepayment(aReq);
				if(!response.getRetcode().equals("300")){
						GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.setRetcode("220");
					response.setRetmessage("Internal server error");
					GeneralUtil.writeDebugLog(aReq.getToken(), "Do Loan Repayment Response Message : ", GeneralUtil.toGson(response));
			}
			return response;
		}
		
		@POST
		@Path("loan-information")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public LoanResponse getLoanInformation(LoanRequest req) {
			/*-------Request log-------*/
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Loan Data Request Message : ", GeneralUtil.toGson(req));
			/*-------Request log-------*/
			LoanResponse response = new LoanResponse();
			ResponseData vres = new ResponseData();
			service001mgr smgr = new service001mgr();
			//validate req
			vres = validateLoanRequest(req);
			if(!vres.getCode().equalsIgnoreCase("300")){
				response.setRetcode(vres.getCode());
				response.setRetmessage(vres.getDesc());
				/*-------Response log-------*/
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Loan Data Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return response;
			} 
			//check token
			result tres = authmgr.validateToken(req.getToken(),"","","", 0);
			if(tres.isState()){
				response = smgr.getLoanInformation(req);
			}else{
				response.setRetcode("210");
				response.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Type Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
			}
			
			return response;
		}
		
	//==================================== validation ==========================================
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
	
	public ResponseData validateLoanRequest(LoanRequest reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else if (reqData.getAccnumber().equalsIgnoreCase("null") || reqData.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account Number is manadatory!");
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
}
