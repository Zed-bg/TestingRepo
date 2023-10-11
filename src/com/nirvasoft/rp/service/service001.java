package com.nirvasoft.rp.service;

import java.text.SimpleDateFormat;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.framework.ServerSession;
import com.nirvasoft.rp.mgr.authmgr;
import com.nirvasoft.rp.mgr.service001mgr;
import com.nirvasoft.rp.shared.accountactivityrequest;
import com.nirvasoft.rp.shared.accountactivityresponse;
import com.nirvasoft.rp.shared.accountcheckres;
import com.nirvasoft.rp.shared.accountnumberdataresponse;
import com.nirvasoft.rp.shared.bulkpaymentrequest;
import com.nirvasoft.rp.shared.bulkpaymentresponse;
import com.nirvasoft.rp.shared.chequelistreq;
import com.nirvasoft.rp.shared.chequelistres;
import com.nirvasoft.rp.shared.custaccinforesult;
import com.nirvasoft.rp.shared.customerinfodataresult;
import com.nirvasoft.rp.shared.AccountData;
import com.nirvasoft.rp.shared.ChequeEnquiry;
import com.nirvasoft.rp.shared.ChequeEnquiryRes;
import com.nirvasoft.rp.shared.depositaccdata;
import com.nirvasoft.rp.shared.depositaccdataresult;
import com.nirvasoft.rp.shared.fixeddepositinfores;
import com.nirvasoft.rp.shared.getaccountcustomerinforeq;
import com.nirvasoft.rp.shared.refdataresponse;
import com.nirvasoft.rp.shared.ForeignExchangeRateResult;
import com.nirvasoft.rp.shared.LoanRequest;
import com.nirvasoft.rp.shared.LoanResponse;
import com.nirvasoft.rp.shared.ResponseData;
import com.nirvasoft.rp.shared.statementdatarequest;
import com.nirvasoft.rp.shared.statementdataresponse;
import com.nirvasoft.rp.shared.tokenreqdata;
import com.nirvasoft.rp.shared.tokenresdata;
import com.nirvasoft.rp.shared.transactioninforequest;
import com.nirvasoft.rp.shared.transactioninforesult;
import com.nirvasoft.rp.shared.userdata;
import com.nirvasoft.rp.util.GeneralUtil;

@Path("/service001")
public class service001 {
	
	@Context
	HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	
	@javax.ws.rs.core.Context
	ServletContext context;
	
	@POST
	@Path("gettoken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public tokenresdata gettoken(tokenreqdata reqdata) {
		tokenresdata res = new tokenresdata();
		authmgr a_mgr = new authmgr();
		res = a_mgr.validatetokenreq(reqdata);
		if(res.getRetcode().equals("300")){
			res = a_mgr.generatetoken(reqdata);
		}
		return res;
	}
	
	//GetAccountSummary
	@POST
	@Path("getsummary")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public depositaccdataresult getsummary(userdata req) {
		//getPath();
		/*-------Request log-------*/
		////GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Summary Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		
		depositaccdataresult depresult = new depositaccdataresult();
		String cusid = "";
		depositaccdata[] depositaccdataarr = null;// Flexcube // Account // List
		service001mgr smgr = new service001mgr();
		
		ResponseData l_res = new ResponseData();
		l_res = validateID(req.getToken(), req.getCustomerid());
		if (l_res.getCode().equals("300")) {
			//validate Token
			result res = authmgr.validateToken(req.getToken(),req.getCustomerid(),"","", 1);
			if(res.isState()){
				cusid = req.getCustomerid();// Customer ID
				try {
					String fixeddeposit = ConnAdmin.readConfig("FixedDeposit");
					depresult = smgr.getaccbycustid(cusid, fixeddeposit,req.getSearchtype());
					if (!depresult.getRetcode().equals("300")) {
						/*-------Response log-------*/
						//GeneralUtil.readDebugLogStatus();
						GeneralUtil.writeDebugLog(req.getSyskey(), "Get Summary Response Message : ", GeneralUtil.toGson(depresult));
						/*-------Response log-------*/
						return depresult;
					} else {
						depositaccdataarr = depresult.getDatalist();// null
					}
				} catch (Exception e) {
					e.printStackTrace();
					/*-------Response log-------*/
					//GeneralUtil.readDebugLogStatus();
					GeneralUtil.writeDebugLog(req.getSyskey(), "Get Summary Response Message : ", GeneralUtil.toGson(depresult));
					/*-------Response log-------*/
					return depresult;
					
				}
				depresult.setDatalist(depositaccdataarr);
				
			}else{
				
				depresult.setRetcode("210");
				depresult.setRetmessage(res.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Summary Response Message : ", GeneralUtil.toGson(depresult));
				/*-------Response log-------*/
				return depresult;
			}
			
		} else {
			depresult.setRetcode(l_res.getCode());
			depresult.setRetmessage(l_res.getDesc());
			/*-------Request log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Summary Response Message : ", GeneralUtil.toGson(depresult));
			/*-------Request log-------*/
			return depresult;
		}
		return depresult;
	}
	
	// Get Acc Info
	@POST
	@Path("getaccinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public accountcheckres getaccinfo(userdata req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Info Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		//getPath();
		ResponseData res = new ResponseData();
		accountcheckres response = new accountcheckres();
		service001mgr sMgr = new service001mgr();
		res = validateCheckAccount(req);
		if (res.getCode().equalsIgnoreCase("300")) {
			result tres = authmgr.validateToken(req.getToken(),"","",req.getAccnumber(), 5);
			if(tres.isState()){
				req.setAccnumber(tres.getAccnumber());
				response = sMgr.getaccinfo(req.getAccnumber());// This is user's all accounts
			}else{
				response.setRetcode("210");
				response.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Info Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return response;
			}
		} else {
			response.setRetcode(res.getCode());
			response.setRetmessage(res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Info Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		
		return response;
	}
	
	//getAccountActivity
	@POST
	@Path("getaccountactivity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public accountactivityresponse getaccountactivity(accountactivityrequest req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Activity Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		//getPath();
		accountactivityresponse res = new accountactivityresponse();
		ResponseData response = new ResponseData();
		service001mgr sMgr = new service001mgr();
		response = validateactivity(req);
		if (response.getCode().equalsIgnoreCase("300")) {
			result tres = authmgr.validateToken(req.getToken(),"",req.getAccnumber(),"", 2);
			if(tres.isState()){
				res = sMgr.getAccountActivity(req);
				res.setCurrentpage(req.getCurrentpage());
				res.setPagesize(req.getPagesize());
				res.setDurationtype(req.getDurationtype());
				res.setFromdate(req.getFromdate());
				res.setTodate(req.getTodate());
			}else{
				res.setRetcode("210");
				res.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Activity Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return res;
			}
		} else {
			res.setRetcode(response.getCode());
			res.setRetmessage(response.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Activity Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}

		res.setAccnumber(req.getAccnumber());

		return res;
	}
	
	@POST
	@Path("getstatement")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public statementdataresponse getstatement(statementdatarequest req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Statement Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		//getPath();
		statementdataresponse res = new statementdataresponse();
		service001mgr sMgr = new service001mgr();
		ResponseData response = new ResponseData();
		response = validateStatement(req);
		if (response.getCode().equalsIgnoreCase("300")) {
			result tres = authmgr.validateToken(req.getToken(),"",req.getAccnumber(),"", 2);
			if(tres.isState()){
				res = sMgr.getstatement(req);
			}else{
				res.setRetcode("210");
				res.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Statement Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return res;
			}
		}else {
			res.setRetcode(response.getCode());
			res.setRetmessage(response.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Statement Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		
		return res;
	}
	
	@POST
	@Path("getexchangerate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ForeignExchangeRateResult getexchangerate(userdata aReq) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(aReq.getSyskey(), "Get Exchange Rate Request Message : ", GeneralUtil.toGson(aReq));
		/*-------Request log-------*/
		//getPath();
		//GeneralUtil.readDebugLogStatus();
		
		ForeignExchangeRateResult res = new ForeignExchangeRateResult();
		//DAOManager.AbsolutePath = context.getRealPath("");
		service001mgr smgr = new service001mgr();
		result tres = authmgr.validateToken(aReq.getToken(),"","","", 0);
		if(tres.isState()){
			res = smgr.getexchangerate();
		}else{
			res.setRetcode("210");
			res.setRetmessage(tres.getMsgDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(aReq.getSyskey(), "Get Exchange Rate Response Message : ", GeneralUtil.toGson(res));
			/*-------Response log-------*/
			return res;
		}
		return res;
	}
	
	@POST
	@Path("getchequeinquiry")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ChequeEnquiryRes getchequeinquiry(ChequeEnquiry reqdata) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(reqdata.getSyskey(), "Get Cheque Inquiry Request Message : ", GeneralUtil.toGson(reqdata));
		/*-------Request log-------*/
		ResponseData res = new ResponseData();
		ChequeEnquiryRes response = new ChequeEnquiryRes();
		service001mgr smgr = new service001mgr();
		//getPath();
		res = validateEnquiryCheque(reqdata);
		if (res.getCode().equalsIgnoreCase("300")) {
			result tres = authmgr.validateToken(reqdata.getToken(),"",reqdata.getAccnumber(),"", 2);
			if(tres.isState()){
				response = smgr.getchequeinquiry(reqdata.getAccnumber(), reqdata.getChequenumber());
			}else{
				response.setRetcode("210");
				response.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(reqdata.getSyskey(), "Get Cheque Inquiry Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return response;
			}
		} else {
			response.setRetcode(res.getCode());
			response.setRetmessage(res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(reqdata.getSyskey(), "Get Cheque Inquiry Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		return response;
	}
	
	@POST
	@Path("checkpaymentdata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public bulkpaymentresponse checkpaymentdata(bulkpaymentrequest reqData) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(reqData.getSyskey(), "Check Payment Data Request Message : ", GeneralUtil.toGson(reqData));
		/*-------Request log-------*/
		//getPath();
		bulkpaymentresponse response = new bulkpaymentresponse();
		ResponseData v_res = new ResponseData();
		service001mgr smgr = new service001mgr();
		
		v_res = validateBulkPayment(reqData);
		//v_res.setCode("300");
		if(v_res.getCode().equalsIgnoreCase("300")){
			result tres = authmgr.validateToken(reqData.getToken(),"",reqData.getFromaccnumber(),"", 2);
			if(tres.isState()){
				response = smgr.checkpaymentdata(reqData);
			}else{
				response.setRetcode("210");
				response.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(reqData.getSyskey(), "Check Payment Data Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return response;
			}
		}else {
			response.setRetcode(v_res.getCode());
			response.setRetmessage(v_res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(reqData.getSyskey(), "Check Payment Data Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		return response;
	}
	
	@POST
	@Path("getaccountcustomerinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public custaccinforesult getaccountcustomerinfo (getaccountcustomerinforeq reqData) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Account Customer Info Request Message : ", GeneralUtil.toGson(reqData));
		/*-------Request log-------*/
		custaccinforesult result = new custaccinforesult();
		ResponseData v_res = new ResponseData();
		service001mgr smgr = new service001mgr();
		
		v_res = validatecustomerinfo(reqData);
		if(v_res.getCode().equalsIgnoreCase("300")){
			result tres = authmgr.validateToken(reqData.getToken(),"","","", 0);
			if(tres.isState()){
				result = smgr.getaccountcustomerinfo(reqData);
			}else{
				result.setRetcode("210");
				result.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Account Customer Info Response Message : ", GeneralUtil.toGson(result));
				/*-------Response log-------*/
				return result;
			}
		}else {
			result.setRetcode(v_res.getCode());
			result.setRetmessage(v_res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Account Customer Info Response Message : ", GeneralUtil.toGson(result));
			/*-------Response log-------*/
		}
		
		return result;
	}
	
	@POST
	@Path("checkaccnumber")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public accountnumberdataresponse checkaccnumber(userdata reqData) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(reqData.getSyskey(), "Check Account Number Request Message : ", GeneralUtil.toGson(reqData));
		/*-------Request log-------*/
		accountnumberdataresponse response = new accountnumberdataresponse();
		ResponseData v_res = new ResponseData();
		service001mgr smgr = new service001mgr();
		
		v_res = validatecheckaccnumber(reqData);
		if(v_res.getCode().equalsIgnoreCase("300")){
			result tres = authmgr.validateToken(reqData.getToken(),"","","", 0);
			if(tres.isState()){
				response = smgr.checkaccnumber(reqData.getAccnumber());
			}else{
				response.setRetcode("210");
				response.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(reqData.getSyskey(), "Check Account Number Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return response;
			}
		}else {
			response.setRetcode(v_res.getCode());
			response.setRetmessage(v_res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(reqData.getSyskey(), "Check Account Number Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		return response;
	}
	
	@POST
	@Path("getcustomerinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public customerinfodataresult getcustomerinfo (getaccountcustomerinforeq reqData) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Customer Info Request Message : ", GeneralUtil.toGson(reqData));
		/*-------Request log-------*/
		customerinfodataresult result = new customerinfodataresult();
		ResponseData v_res = new ResponseData();
		service001mgr smgr = new service001mgr();
		
		v_res = validatecustomerinfo(reqData);
		if(v_res.getCode().equalsIgnoreCase("300")){
			result tres = authmgr.validateToken(reqData.getToken(),"","","", 0);
			if(tres.isState()){
				result = smgr.getcustomerinfo(reqData);
			}else{
				result.setRetcode("210");
				result.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Customer Info Response Message : ", GeneralUtil.toGson(result));
				/*-------Response log-------*/
				return result;
			}
		}else {
			result.setRetcode(v_res.getCode());
			result.setRetmessage(v_res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Customer Info Response Message : ", GeneralUtil.toGson(result));
			/*-------Response log-------*/
		}
		
		return result;
	}
	
	@POST
	@Path("gettransinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public transactioninforesult gettransinfo (transactioninforequest reqData) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Transaction Info Request Message : ", GeneralUtil.toGson(reqData));
		/*-------Request log-------*/
		transactioninforesult result = new transactioninforesult();
		ResponseData v_res = new ResponseData();
		service001mgr smgr = new service001mgr();
		
		v_res = validatetransinforeq(reqData);
		if(v_res.getCode().equalsIgnoreCase("300")){
			result tres = authmgr.validateToken(reqData.getToken(),"",reqData.getAccnumber(),"", 2);
			if(tres.isState()){
				result = smgr.gettransinfo(reqData);
			}else{
				result.setRetcode("210");
				result.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Transaction Info Response Message : ", GeneralUtil.toGson(result));
				/*-------Response log-------*/
				return result;
			}
		}else {
			result.setRetcode(v_res.getCode());
			result.setRetmessage(v_res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(reqData.getSyskey(), "Get Transaction Info Response Message : ", GeneralUtil.toGson(result));
			/*-------Response log-------*/
		}
		
		return result;
	}
	
	@POST
	@Path("getchequelist")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public chequelistres getchequelist(chequelistreq req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Cheque List Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		chequelistres response = new chequelistres();
		ResponseData vres = new ResponseData();
		service001mgr smgr = new service001mgr();
		//validate req
		vres = validatechequelistreq(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Cheque List Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"",req.getAccnumber(),"", 2);
		if(tres.isState()){
			response = smgr.getChequeList(req.getAccnumber());
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Cheque List Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		return response;
	}
	
	@POST
	@Path("getfixeddepositinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public fixeddepositinfores getfixeddepositinfo(userdata req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Fixed Deposit Info Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		fixeddepositinfores response = new fixeddepositinfores();
		ResponseData vres = new ResponseData();
		service001mgr smgr = new service001mgr();
		//validate req
		vres = validatefixeddepositres(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Fixed Deposit Info Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 0);
		if(tres.isState()){
			response = smgr.getfixeddepositinfo();
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Fixed Deposit Info Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		return response;
	}
		
	@POST
	@Path("getbranchesbybank")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public refdataresponse getbranchesbybank(userdata req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Branches by Bank Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		refdataresponse response = new refdataresponse();
		ResponseData vres = new ResponseData();
		service001mgr smgr = new service001mgr();
		//validate req
		vres = validatebranchlistres(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Branches by Bank Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 0);
		if(tres.isState()){
			response = smgr.getbranchesbybank(req.getBankcode());
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Branches by Bank Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		return response;
	}
	
	//get account data by select account
	@POST
	@Path("getaccdata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public accountcheckres getaccdata(userdata req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Data Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		//getPath();
		ResponseData res = new ResponseData();
		accountcheckres response = new accountcheckres();
		service001mgr sMgr = new service001mgr();
		res = validateCheckAccount(req);
		if (res.getCode().equalsIgnoreCase("300")) {
			result tres = authmgr.validateToken(req.getToken(),"",req.getAccnumber(),"", 2);
			if(tres.isState()){
				response = sMgr.getaccdata(req.getAccnumber());// This is user's all accounts
			}else{
				response.setRetcode("210");
				response.setRetmessage(tres.getMsgDesc());
				/*-------Response log-------*/
				//GeneralUtil.readDebugLogStatus();
				GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Data Response Message : ", GeneralUtil.toGson(response));
				/*-------Response log-------*/
				return response;
			}
		} else {
			response.setRetcode(res.getCode());
			response.setRetmessage(res.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Data Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
		}
		
		return response;
	}
	
	//get account type
	@POST
	@Path("getacctype")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public refdataresponse getacctype(userdata req) {
		/*-------Request log-------*/
		//GeneralUtil.readDebugLogStatus();
		GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Type Request Message : ", GeneralUtil.toGson(req));
		/*-------Request log-------*/
		refdataresponse response = new refdataresponse();
		ResponseData vres = new ResponseData();
		service001mgr smgr = new service001mgr();
		//validate req
		vres = validatefixeddepositres(req);
		if(!vres.getCode().equalsIgnoreCase("300")){
			response.setRetcode(vres.getCode());
			response.setRetmessage(vres.getDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Type Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
			return response;
		} 
		//check token
		result tres = authmgr.validateToken(req.getToken(),"","","", 0);
		if(tres.isState()){
			response = smgr.getacctype();
		}else{
			response.setRetcode("210");
			response.setRetmessage(tres.getMsgDesc());
			/*-------Response log-------*/
			//GeneralUtil.readDebugLogStatus();
			GeneralUtil.writeDebugLog(req.getSyskey(), "Get Account Type Response Message : ", GeneralUtil.toGson(response));
			/*-------Response log-------*/
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
	
	//======================== Validation ===========================
	
	public ResponseData validatetransinforeq(transactioninforequest reqData){
		ResponseData response = new ResponseData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else if (reqData.getAccnumber().equalsIgnoreCase("null") || reqData.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account Number is manadatory!");
		}else if (reqData.getFromdate().equalsIgnoreCase("null") || reqData.getFromdate().equals("") ) {
			response.setDesc("Start date is mandatory");
			response.setCode("210");
		}else if (reqData.getTodate().equalsIgnoreCase("null") || reqData.getTodate().equals("")) {
			response.setDesc("End date is mandatory");
			response.setCode("210");
		}else{
			try {
				if (!sdf.parse(reqData.getTodate()).equals(sdf.parse(reqData.getFromdate()))) {
					if (!sdf.parse(reqData.getTodate()).after(sdf.parse(reqData.getFromdate()))) {
						response.setDesc("Start date must not exceed end date!");
						response.setCode("210");
					}else {
						response.setCode("300");
						response.setDesc("Success");
					}
				}else {
					response.setCode("300");
					response.setDesc("Success");
				}
			} catch (java.text.ParseException e) {
					response.setCode("210");
					response.setDesc(e.getMessage());
					e.printStackTrace();
			}
		}
		return response;
	}
	
	public ResponseData validatecheckaccnumber(userdata reqData){
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
	
	public ResponseData validatecustomerinfo(getaccountcustomerinforeq reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else if (reqData.getSearchway().equalsIgnoreCase("null") || reqData.getSearchway().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Search way is manadatory!");
		}else if (reqData.getSearchtype().equalsIgnoreCase("null") || reqData.getSearchtype().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Search type is manadatory!");
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
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
		}else if (reqData.getDatalist().length <= 0 || reqData.getDatalist().equals(null)) {
			response.setCode("210");
			response.setDesc("Data list is manadatory!");
			/*bulkpaymentdatareq[] data = reqData.getDatalist();
			for(int i=0; i<data.length ; i++){
				if (data[i].getRowno().equalsIgnoreCase("null") || data[i].getRowno().equalsIgnoreCase("")) {
					response.setCode("210");
					response.setDesc("Row number is manadatory!");
					break;
				}else if (data[i].getToaccnumber().equalsIgnoreCase("null") || data[i].getToaccnumber().equalsIgnoreCase("")) {
					response.setCode("210");
					response.setDesc("To account number is manadatory!");
					break;
				}else if (data[i].getToaccname().equalsIgnoreCase("null") || data[i].getToaccname().equalsIgnoreCase("")) {
					response.setCode("210");
					response.setDesc("To account name is manadatory!");
					break;
				}else if (data[i].getAmount() <= 0) {
					response.setCode("210");
					response.setDesc("Amount is manadatory!");
					break;
				}else{
					response.setCode("300");
					response.setDesc("Success");
				}
			}*/
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	public ResponseData validateEnquiryCheque(ChequeEnquiry reqData){
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
	
	public ResponseData validateStatement(statementdatarequest req){
		ResponseData response = new ResponseData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//boolean isDate = true;
		if (req.getToken().equalsIgnoreCase("null") || req.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		} else if (req.getAccnumber().equalsIgnoreCase("null") || req.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account No. is manadatory");
		} else if (req.getDurationtype() == 3) {
			if (req.getFromdate().equalsIgnoreCase("null") || req.getFromdate().equals("") ) {
				response.setDesc("Start date is mandatory");
				response.setCode("210");
			} else if (req.getTodate().equalsIgnoreCase("null") || req.getTodate().equals("")) {
				response.setDesc("End date is mandatory");
				response.setCode("210");
			}else{
				try {
					if (!sdf.parse(req.getTodate()).equals(sdf.parse(req.getFromdate()))) {
						if (!sdf.parse(req.getTodate()).after(sdf.parse(req.getFromdate()))) {
							response.setDesc("Start date must not exceed end date!");
							response.setCode("210");
						}else {
							response.setCode("300");
							response.setDesc("Success");
						}
					}else {
						response.setCode("300");
						response.setDesc("Success");
					}
				} catch (java.text.ParseException e) {
						response.setCode("210");
						response.setDesc(e.getMessage());
						e.printStackTrace();
				}
			}
		} else {
				response.setCode("300");
				response.setDesc("Success");
		}
		return response;
		
	}
	public ResponseData validateactivity(accountactivityrequest req) {
		ResponseData response = new ResponseData();
		if (req.getToken().equalsIgnoreCase("null") || req.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		} else if (req.getAccnumber().equalsIgnoreCase("null") || req.getAccnumber().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Account No. is manadatory");
		} else if (req.getDurationtype().equals("3")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			if (req.getFromdate().equalsIgnoreCase("null") || req.getFromdate().equals("") ) {
				response.setDesc("Start date is mandatory");
				response.setCode("210");
			} else if (req.getTodate().equalsIgnoreCase("null") || req.getTodate().equals("")) {
				response.setDesc("End date is mandatory");
				response.setCode("210");
			} else{
				try {
					if (!sdf.parse(req.getTodate()).equals(sdf.parse(req.getFromdate()))) {
						if (!sdf.parse(req.getTodate()).after(sdf.parse(req.getFromdate()))) {
							response.setDesc("Start date must not exceed end date!");
							response.setCode("210");
						}else {
							response.setCode("300");
							response.setDesc("Success");
						}
					}else {
						response.setCode("300");
						response.setDesc("Success");
					}
				} catch (java.text.ParseException e) {
					response.setCode("210");
					response.setDesc(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else {
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	public ResponseData validateID(String token, String customerID) {
		ResponseData response = new ResponseData();
		if (token.equalsIgnoreCase("null") || token.equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is mandatory");
		} else if (customerID.equalsIgnoreCase("null") || customerID.equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Customer ID is mandatory");
		}else {
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	private ResponseData validateCheckAccount(userdata data) {
		ResponseData res = new ResponseData();
		if (data.getToken().equalsIgnoreCase("null") || data.getToken().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Token is mandatory");
		}else if (data.getAccnumber().equals("null") || data.getAccnumber().equalsIgnoreCase("")) {
			res.setCode("210");
			res.setDesc("Account number is manadatory");
			return res;
		} else {
			res.setCode("300");
			res.setDesc("Success");
		}
		return res;
	}
	
	public ResponseData validatechequelistreq(chequelistreq reqData){
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
	
	public ResponseData validatefixeddepositres(userdata reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
	}
	
	public ResponseData validatebranchlistres(userdata reqData){
		ResponseData response = new ResponseData();
		if (reqData.getToken().equalsIgnoreCase("null") || reqData.getToken().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Token is manadatory!");
		}if (reqData.getBankcode().equalsIgnoreCase("null") || reqData.getBankcode().equalsIgnoreCase("")) {
			response.setCode("210");
			response.setDesc("Bank Code is manadatory!");
		}else{
			response.setCode("300");
			response.setDesc("Success");
		}
		return response;
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
	//testing Tomcat api to Tomcat api, not use db
	@GET
	@Path("requesttesting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getrequesttesting(userdata reqData) {
		service001mgr smgr = new service001mgr();
		String response="";
		try {
			response = smgr.sendTestingAPI(reqData.getToken(), reqData.getCustomerid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return response;
	}
	
	@POST
	@Path("responsetesting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public tokenresdata getresponsetesting(userdata reqData) {
		tokenresdata response=new tokenresdata();
		try {
			response.setRetmessage("Response Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return response;
	}
	
	
	
}
