package com.nirvasoft.rp.mgr;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.dao.service001Dao;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.shared.Constant;
import com.nirvasoft.rp.shared.CurrencyRateDataSet;
import com.nirvasoft.rp.shared.NewTokenUserReq;
import com.nirvasoft.rp.shared.tokenreqdata;
import com.nirvasoft.rp.shared.tokenresdata;
import com.nirvasoft.rp.shared.TokenResult;
import com.nirvasoft.rp.shared.TokenSetupData;
import com.nirvasoft.rp.util.TokenUtil;

public class authmgr {
	
	public String generateRandomNum(){
		SecureRandom rnd = new SecureRandom();
	    int number = rnd.nextInt(99999999);
	    return String.format("%08d", number);
	}
	
	public static String gettoken(tokenreqdata reqdata, boolean isexpired) {
		Map<String, Object> payload = new HashMap<String, Object>();
		String jwt = "";
		payload.put("appID", reqdata.getAppid());
		payload.put("appKey", reqdata.getAppkey());
		payload.put("userID", reqdata.getUserid());
		payload.put("cifID", reqdata.getCifid());
		jwt = TokenUtil.generateToken(new Gson().toJson(payload), isexpired);
		return jwt;
	}
	
	public tokenresdata generatetoken(tokenreqdata reqdata){
		tokenresdata res = new tokenresdata();
		DAOManager dao = new DAOManager();
		service001Dao s_dao = new service001Dao();
		try {
            String jwt = "";

            jwt = gettoken(reqdata, true);
            if (!jwt.equals("")) {
            	res.setRetcode("300");
            	res.setRetmessage("Request Token Successfully.");
            	res.setToken(jwt);
            }else{
        		res.setRetcode("220");
        		res.setRetmessage("Request Token Failed.");
        	}
			
		} catch ( Exception e) {
			// TODO Auto-generated catch block
		}
		return res;
	}
	
	public static result validateToken(String token,String customerID,String fromAccount,String toAccount, int type){
		result res = new result();
		res.setState(false);
		Connection conn = null;
		service001Dao s_dao = new service001Dao();
		try {
			if (!TokenUtil.isTokenExpired(token)) {
				conn = ConnAdmin.getConn3();
				String cifID ="";
				String appID ="";
				if(type == 6)
					appID = TokenUtil.getValueOfToken("appID",token);// App ID
				else
					cifID = TokenUtil.getValueOfToken("cifID",token);// customer ID
				
				if(type == 0){ // check customer ID
					if(!(cifID.equals("") && cifID.equals(null))){
						res.setState(true);
					} else{
						res.setMsgDesc("Invalid Request[000]");
						return res;
					}
				}else if(type == 1){ // check customer ID
					if(cifID.equals(customerID)){
						res.setState(true);
					} else{
						res.setMsgDesc("Invalid Request[001]");
						return res;
					}
				}else if(type == 2){ // check customer ID and from Account
					res.setState((s_dao.checkCifbyAccount(cifID,fromAccount, conn)));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[002]");
						return res;
					}
				}else if(type == 3){ // check customer ID and (from Account, to Account)
					res.setState(s_dao.checkCifbyAccount(cifID,fromAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[003]");
						return res;
					}
					res.setState(s_dao.checkCifbyAccount(cifID,toAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[004]");
						return res;
					}		
				}else if(type == 4){ // check (customer ID,from Account) and to Account
					res.setState(s_dao.checkCifbyAccount(cifID,fromAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[005]");
						return res;
					}
					res.setState(s_dao.checkAccount(toAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[006]");
						return res;
					}
				}else if(type == 5){
					res.setAccnumber(s_dao.checkAccountAndShortCode(toAccount, conn));
					if(res.getAccnumber().equalsIgnoreCase("")){
						res.setMsgDesc("Invalid Request[007]");
						return res;
					}else{
						res.setState(true);
					}
				}else if(type == 6){
					if(appID.equals("ibankingac")){
						res.setState(true);
					} else{
						res.setMsgDesc("Invalid Request[008]");
						return res;
					}
				}
			}else{
				res.setMsgDesc("Token is expired");
			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			res.setMsgDesc("Internal server error");
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
		return res;
	}
	
	/*public CurrencyRateDataSet getCurrencyRate(){
		CurrencyRateDataSet res = new CurrencyRateDataSet();
		Connection conn = null;
		DAOManager dao = new DAOManager();
		service001Dao s_dao = new service001Dao();
		try {
			conn = ConnAdmin.getConn("001", "");
			res = s_dao.getCurrencyRate(conn);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			res.setMsgCode("0014");
			res.setMsgDesc("Internal Error");
		}finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				res.setMsgCode("0014");
				res.setMsgDesc("Internal Error");
			}
		}
		return res;
	}*/
	
	//========Valildation==========================
	public TokenResult validateTUserReq(NewTokenUserReq treq){
		TokenResult res = new TokenResult();
		if (treq.getUserId().trim().equals("") || treq.getUserId().trim().equals(null)
				|| treq.getPassword().trim().equals("") || treq.getPassword().trim().equals(null)
				|| treq.getComfirmPassword().trim().equals("") || treq.getComfirmPassword().trim().equals(null)) {
			res.setMsgCode("0014");
			res.setMsgDesc("User ID or Password or Comfirm password must not be blank.");
		} else if (!treq.getPassword().equals(treq.getComfirmPassword())) {
			res.setMsgCode("0014");
			res.setMsgDesc("Password and Comfirm password must be same.");
		}else{
			res.setMsgCode("0000");
		}
		return res;
	}
	
	public tokenresdata validatetokenreq(tokenreqdata req){
		tokenresdata res = new tokenresdata();
		if(req.getAppid().trim().equals("") || req.getAppid().trim() == null){
			res.setRetcode("210");
			res.setRetmessage("App ID is mandatory");
		}else if(req.getAppkey().trim().equals("") || req.getAppkey().trim() == null){
			res.setRetcode("210");
			res.setRetmessage("App Key is mandatory");
		}else if(req.getUserid().trim().equals("") || req.getUserid().trim() == null){
			res.setRetcode("210");
			res.setRetmessage("User ID is mandatory");
		}else if(req.getCifid().trim().equals("") || req.getCifid().trim() == null){
			res.setRetcode("210");
			res.setRetmessage("Customer ID is mandatory");
		}else{
			res.setRetcode("300");
			res.setRetmessage("Success");
		}
		
		return res;
	}
	
	/*public CurrencyRateDataSet validateCurrencyReq(OrgResData req){
		CurrencyRateDataSet res = new CurrencyRateDataSet();
		if(req.getToken().trim().equals("") || req.getToken().trim() == null){
			res.setMsgCode("0014");
			res.setMsgDesc("Invalid Token");
		}else{
			res.setMsgCode("0000");
		}
		return res;
	}*/
	
	public static result validateToken(String token,String customerID,String fromAccount,String toAccount, int type,Connection conn){
		result res = new result();
		res.setState(false);
		service001Dao s_dao = new service001Dao();
		try {
			if (!TokenUtil.isTokenExpired(token)) {
				String cifID ="";
				String appID ="";
				if(type == 6)
					appID = TokenUtil.getValueOfToken("appID",token);// App ID
				else
					cifID = TokenUtil.getValueOfToken("cifID",token);// customer ID
				
				if(type == 0){ // check customer ID
					if(!(cifID.equals("") && cifID.equals(null))){
						res.setState(true);
					} else{
						res.setMsgDesc("Invalid Request[000]");
						return res;
					}
				}else if(type == 1){ // check customer ID
					if(cifID.equals(customerID)){
						res.setState(true);
					} else{
						res.setMsgDesc("Invalid Request[001]");
						return res;
					}
				}else if(type == 2){ // check customer ID and from Account
					res.setState((s_dao.checkCifbyAccount(cifID,fromAccount, conn)));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[002]");
						return res;
					}
				}else if(type == 3){ // check customer ID and (from Account, to Account)
					res.setState(s_dao.checkCifbyAccount(cifID,fromAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[003]");
						return res;
					}
					res.setState(s_dao.checkCifbyAccount(cifID,toAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[004]");
						return res;
					}		
				}else if(type == 4){ // check (customer ID,from Account) and to Account
					res.setState(s_dao.checkCifbyAccount(cifID,fromAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[005]");
						return res;
					}
					res.setState(s_dao.checkAccount(toAccount, conn));
					if(!res.isState()){
						res.setMsgDesc("Invalid Request[006]");
						return res;
					}
				}else if(type == 5){
					res.setAccnumber(s_dao.checkAccountAndShortCode(toAccount, conn));
					if(res.getAccnumber().equalsIgnoreCase("")){
						res.setMsgDesc("Invalid Request[007]");
						return res;
					}else{
						res.setState(true);
					}
				}else if(type == 6){
					if(appID.equals("ibankingac")){
						res.setState(true);
					} else{
						res.setMsgDesc("Invalid Request[008]");
						return res;
					}
				} else if (type == 7) { // deposit and withdraw
					if (customerID.equals("0")) {//chek gl or cus account 
						res.setState(s_dao.checkAccount(fromAccount, conn));
					} else {
						res.setState(s_dao.checkGLAccount(fromAccount, conn));
					}
					if (!res.isState()) {
						res.setMsgDesc("Invalid Request[003]");
						return res;
					}
				}
			}else{
				res.setMsgDesc("Token is expired");
			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			res.setMsgDesc("Internal server error");
		}finally {
			
		}
		return res;
	}
	
}
