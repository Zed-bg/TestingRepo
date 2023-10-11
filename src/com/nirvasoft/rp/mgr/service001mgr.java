package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.dao.AccountDao;
import com.nirvasoft.rp.dao.LAODFScheduleDAO;
import com.nirvasoft.rp.dao.LoanAccountDAO;
import com.nirvasoft.rp.dao.service001Dao;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.shared.accountactivityrequest;
import com.nirvasoft.rp.shared.accountactivityresponse;
import com.nirvasoft.rp.shared.accountcheckres;
import com.nirvasoft.rp.shared.accountnumberdataresponse;
import com.nirvasoft.rp.shared.bulkpaymentrequest;
import com.nirvasoft.rp.shared.bulkpaymentresponse;
import com.nirvasoft.rp.shared.chequelistdata;
import com.nirvasoft.rp.shared.chequelistres;
import com.nirvasoft.rp.shared.custaccinforesult;
import com.nirvasoft.rp.shared.customerinfodata;
import com.nirvasoft.rp.shared.customerinfodataresult;
import com.nirvasoft.rp.shared.BalanceTotalResponse;
import com.nirvasoft.rp.shared.BankHolidayDataRes;
import com.nirvasoft.rp.shared.CheckIssueCriteriaData;
import com.nirvasoft.rp.shared.CheckIssueData;
import com.nirvasoft.rp.shared.CheckSheetData;
import com.nirvasoft.rp.shared.ChequeEnquiryRes;
import com.nirvasoft.rp.shared.CustInfoResult;
import com.nirvasoft.rp.shared.CutOffDataResult;
import com.nirvasoft.rp.shared.ForeignExchangeRateResult;
import com.nirvasoft.rp.shared.LAODFScheduleData;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.LoanRepaymentData;
import com.nirvasoft.rp.shared.LoanRequest;
import com.nirvasoft.rp.shared.LoanResponse;
import com.nirvasoft.rp.shared.LoanScheduleData;
import com.nirvasoft.rp.shared.SystemData;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.shared.depositaccdata;
import com.nirvasoft.rp.shared.depositaccdataresult;
import com.nirvasoft.rp.shared.fixeddepositinfores;
import com.nirvasoft.rp.shared.getaccountcustomerinforeq;
import com.nirvasoft.rp.shared.refdataresponse;
import com.nirvasoft.rp.shared.statementdatarequest;
import com.nirvasoft.rp.shared.statementdataresponse;
import com.nirvasoft.rp.shared.transactioninforequest;
import com.nirvasoft.rp.shared.transactioninforesult;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.StrUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class service001mgr {

	public depositaccdataresult getaccbycustid(String customerid, String fixeddeposit, String searchtype)
			throws Exception {
		depositaccdataresult result = new depositaccdataresult();
		service001Dao sDao = new service001Dao();
		SystemData sysdata = new SystemData();
		Connection conn = null;
		conn = ConnAdmin.getConn3();
		try {

			// sysdata.setSystemSettingDatas(DBSystemMgr.getSystemSettingDatas(conn));
			// sysdata.setProducts(DBSystemMgr.readProducts(conn));
			// SharedLogic.setSystemData(sysdata);
			// com.nirvasoft.rp.shared.SharedLogic.setSystemData(SharedLogic.getSystemData());

			CustInfoResult cres = sDao.getAccountSummary(customerid, fixeddeposit, searchtype, conn);
			if (cres.getCode().equals("300")) {
				int arrlength = cres.getAccountInfoArr().length;
				if (arrlength > 0) {
					depositaccdata[] depAccDataArr = new depositaccdata[arrlength];
					for (int i = 0; i < arrlength; i++) {
						depositaccdata accdata = new depositaccdata();
						accdata.setAccnumber(cres.getAccountInfoArr()[i].getAccnumber());
						accdata.setAvailablebalance(
								GeneralUtil.formatDecimalString(cres.getAccountInfoArr()[i].getAvailablebalance()));
						accdata.setCurrentbalance(
								GeneralUtil.formatDecimalString(cres.getAccountInfoArr()[i].getCurrentbalance()));
						accdata.setCurcode(cres.getAccountInfoArr()[i].getCurcode());
						accdata.setAcctype(cres.getAccountInfoArr()[i].getAcctype());
						accdata.setAccname(cres.getAccountInfoArr()[i].getAccname());
						accdata.setStatus(cres.getAccountInfoArr()[i].getAccstatus());
						accdata.setOpeningdate(cres.getAccountInfoArr()[i].getOpeningdate());
						accdata.setBrname(cres.getAccountInfoArr()[i].getBrname());
						accdata.setProductname(cres.getAccountInfoArr()[i].getProductname());
						accdata.setProducttype(cres.getAccountInfoArr()[i].getProducttype());
						accdata.setBrcode(cres.getAccountInfoArr()[i].getBrcode());
						depAccDataArr[i] = accdata;
					}
					result.setDatalist(depAccDataArr);
				}
			}
			result.setRetcode(cres.getCode());
			result.setRetmessage(cres.getDesc());
		} catch (Exception e) {
			result.setRetcode("220");
			result.setRetmessage("Server Error");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return result;
	}
	


	public accountcheckres getaccinfo(String acctno) {
		service001Dao l_dao = new service001Dao();
		accountcheckres res = new accountcheckres();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			res = l_dao.getaccinfo(acctno, "FD", conn);
			if (!res.getRetcode().equals("300")) {
				res.setRetcode("210");
				res.setRetmessage("Account Number is invalid.");
				return res;
			} else {
				res.setRetcode("300");
				res.setRetmessage("Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Server Error!");
		} finally {
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

	public accountactivityresponse getAccountActivity(accountactivityrequest req) {
		service001Dao l_dao = new service001Dao();
		accountactivityresponse res = new accountactivityresponse();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			res = l_dao.getAccountActivity(req, conn);
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Server Error!");
		} finally {
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

	public statementdataresponse getstatement(statementdatarequest req) {
		statementdataresponse res = new statementdataresponse();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			res = l_dao.getstatement(req, conn);
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Server Error!");
		} finally {
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

	public ForeignExchangeRateResult getexchangerate() {
		ForeignExchangeRateResult res = new ForeignExchangeRateResult();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			res = l_dao.getexchangerate(conn);
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Server Error!");
		} finally {
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

	public ChequeEnquiryRes getchequeinquiry(String accountNo, String chequeNo) {
		ChequeEnquiryRes ret = new ChequeEnquiryRes();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		String status = "";
		try {
			conn = ConnAdmin.getConn3();
			status = l_dao.getchequeinquiry(accountNo, chequeNo, conn);
			if (!status.equals("")) {
				ret.setRetcode("300");
				ret.setRetmessage("Successfully");
				ret.setChequestatus(status);
			} else {
				ret.setRetcode("210");
				ret.setRetmessage("Cheque not found");
				ret.setChequestatus(status);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setRetcode("220");
			ret.setRetmessage(e.getMessage());

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
				ret.setRetmessage(e.getMessage());
			}

		}
		return ret;
	}

	public bulkpaymentresponse checkpaymentdata(bulkpaymentrequest reqData) {
		bulkpaymentresponse response = new bulkpaymentresponse();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			// SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			// com.nirvasoft.rp.shared.SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			conn = ConnAdmin.getConn3();
			response = l_dao.checkpaymentdata(reqData, conn);

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

	public custaccinforesult getaccountcustomerinfo(getaccountcustomerinforeq reqData) {
		custaccinforesult result = new custaccinforesult();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			result = l_dao.getaccountcustomerinfo(reqData, conn);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setRetcode("220");
			result.setRetmessage(e.getMessage());

		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setRetcode("220");
				result.setRetmessage(e.getMessage());
			}
		}
		return result;
	}

	public accountnumberdataresponse checkaccnumber(String accno) {
		accountnumberdataresponse response = new accountnumberdataresponse();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			response = l_dao.checkaccnumber(accno, conn);

			if (!response.getRetcode().equals("300"))
				response = l_dao.checkaccnumber2(accno, conn);

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

	public customerinfodataresult getcustomerinfo(getaccountcustomerinforeq reqData) {
		customerinfodataresult result = new customerinfodataresult();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			result = l_dao.getcustomerinfo(reqData, conn);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setRetcode("220");
			result.setRetmessage(e.getMessage());

		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setRetcode("220");
				result.setRetmessage(e.getMessage());
			}
		}
		return result;
	}

	public transactioninforesult gettransinfo(transactioninforequest reqData) {
		transactioninforesult result = new transactioninforesult();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			result = l_dao.gettransinfo(reqData, conn);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setRetcode("220");
			result.setRetmessage(e.getMessage());

		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setRetcode("220");
				result.setRetmessage(e.getMessage());
			}
		}
		return result;
	}

	public chequelistres getChequeList(String accountNo) {
		ArrayList<chequelistdata> dataList = new ArrayList<chequelistdata>();
		ArrayList<CheckIssueData> chequeList = new ArrayList<CheckIssueData>();
		ArrayList<CheckIssueData> chequeListFromServer = new ArrayList<CheckIssueData>();
		service001Dao l_dao = new service001Dao();
		chequelistres response = new chequelistres();
		Connection conn = null;
		org.json.JSONObject payload = new org.json.JSONObject();
		String Token = "";
		try {
			conn = ConnAdmin.getConn3();
			CheckIssueCriteriaData l_CheckIssueCriData = new CheckIssueCriteriaData();
			l_CheckIssueCriData.AccountNumber = accountNo;
			chequeListFromServer = l_dao.getCheckBooks(l_CheckIssueCriData, conn);

			for (int i = 0; i < chequeListFromServer.size(); i++) {
				if (chequeListFromServer.get(i).getStatus() == 0) {
					chequeList.add(chequeListFromServer.get(i));
				}
			}

			if (chequeList.size() > 0) {
				for (int i = 0; i < chequeList.size(); i++) {
					CheckIssueData l_CheckIssue = new CheckIssueData();
					l_CheckIssue = chequeList.get(i);
					chequeList.get(i).setCheckSheetDataList(getCheckSheetList(l_CheckIssue, conn));
				}
			}

			if (chequeList.size() > 0) {
				for (int i = 0; i < chequeList.size(); i++) {
					for (int j = 0; j < chequeList.get(i).getCheckSheetDataList().size(); j++) {
						chequelistdata data = new chequelistdata();
						data.setChequenumber(chequeList.get(i).getCheckSheetDataList().get(j).getCheckSheetNo());
						data.setStatus(chequeList.get(i).getCheckSheetDataList().get(j).getCheckSheetStatusInWord());
						dataList.add(data);
					}
				}
				if (chequeList.size() > 0) {
					response.setRetcode("300");
					response.setRetmessage("Success");
					response.setDatalist(dataList);
				}

			} else {
				response.setRetcode("210");
				response.setRetmessage("This account does not have any Cheque No.");
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

	private static ArrayList<CheckSheetData> getCheckSheetList(CheckIssueData aCheckIssueData, Connection conn) {
		ArrayList<CheckSheetData> l_lstCheckSheet = new ArrayList<CheckSheetData>();
		CheckSheetData l_CheckSheetData = new CheckSheetData();
		service001Dao l_dao = new service001Dao();
		String l_CheckInfo = "";
		int l_CheckStatus = 255;
		String l_CheckSheetStart = "";
		String l_CheckSheetEnd = "";
		String l_CheckChar = aCheckIssueData.getChkNoChar();
		int l_ChkNoFrom = aCheckIssueData.getChkNoFrom();
		// HNNS
		int l_Checklength = 0;
		try {
			l_Checklength = l_dao.getCHQ(conn);

			if (l_Checklength == 0)
				l_Checklength = 9;
			for (int i = 0; i < aCheckIssueData.getSheets().length(); i++) {
				l_CheckSheetData = new CheckSheetData();

				l_CheckSheetStart = l_CheckChar
						+ GeneralUtil.leadZeros(aCheckIssueData.getChkNoFrom(), l_Checklength - l_CheckChar.length());
				l_CheckSheetEnd = l_CheckChar + GeneralUtil.leadZeros(
						(aCheckIssueData.getChkNoFrom() + aCheckIssueData.getSheets().length() - 1),
						l_Checklength - l_CheckChar.length());

				l_CheckStatus = Integer.parseInt(String.valueOf(aCheckIssueData.getSheets().charAt(i)));
				l_CheckInfo = aCheckIssueData.getChkNoChar()
						+ GeneralUtil.leadZeros(l_ChkNoFrom++, l_Checklength - l_CheckChar.length());

				l_CheckSheetData.setCheckSheetStatus((byte) l_CheckStatus);
				l_CheckSheetData.setCheckSheetStatusInWord(GeneralUtil.getCheckStatus(l_CheckStatus));
				l_CheckSheetData.setCheckSheetNo(l_CheckInfo);
				l_CheckSheetData.setCheckSheetStart(l_CheckSheetStart);
				l_CheckSheetData.setCheckSheetEnd(l_CheckSheetEnd);

				l_lstCheckSheet.add(l_CheckSheetData);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l_lstCheckSheet;
	}

	public fixeddepositinfores getfixeddepositinfo() {
		fixeddepositinfores response = new fixeddepositinfores();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			response = l_dao.getfixeddepositinfo(conn);
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

	public refdataresponse getbranchesbybank(String bankcode) {
		refdataresponse response = new refdataresponse();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			response = l_dao.getbranchesbybank(bankcode, conn);
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

	public accountcheckres getaccdata(String acctno) {
		service001Dao l_dao = new service001Dao();
		accountcheckres res = new accountcheckres();
		// SystemData sysdata = new SystemData();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			// sysdata.setSystemSettingDatas(DBSystemMgr.getSystemSettingDatas(conn));
			// sysdata.setProducts(DBSystemMgr.readProducts(conn));
			// SharedLogic.setSystemData(sysdata);
			// com.nirvasoft.rp.shared.SharedLogic.setSystemData(SharedLogic.getSystemData());
			res = l_dao.getaccdata(acctno, conn);
			if (!res.getRetcode().equals("300")) {
				res.setRetcode("210");
				res.setRetmessage("Account Number is invalid.");
				return res;
			} else {
				res.setRetcode("300");
				res.setRetmessage("Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setRetcode("220");
			res.setRetmessage("Server Error!");
		} finally {
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

	public refdataresponse getacctype() {
		refdataresponse response = new refdataresponse();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			response = l_dao.getacctype(conn);
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

	public refdataresponse getBranchList() {
		refdataresponse response = new refdataresponse();
		service001Dao l_dao = new service001Dao();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();
			response = l_dao.getBranchList(conn);
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

	public static String sendTestingAPI(String url, String req) throws Exception {

		Client client = Client.create();

		// for ssl certificate
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		// end for ssl certificate

		WebResource webResource = client.resource(url);
		ClientResponse response = null;
		JSONObject obj = new JSONObject();
		obj.put("customerid", req);
		response = webResource.type("application/json").post(ClientResponse.class, obj.toString());
		String jsonStr = response.getEntity(String.class);
		JSONParser parser = new JSONParser();
		Object object = parser.parse(jsonStr);
		JSONObject jsonObject = (JSONObject) object;
		String responseMessage = (String) jsonObject.get("retmessage");

		return responseMessage;
	}

	public LoanResponse getLoanInformation(LoanRequest req) {
		LoanResponse response = new LoanResponse();
		LoanAccountDAO dao = new LoanAccountDAO();
		LAODFScheduleDAO s_dao = new LAODFScheduleDAO();
		Connection conn = null;
		try {
			conn = ConnAdmin.getConn3();

			if (s_dao.checkLAODFSchedule(req.getAccnumber(), conn)) {
				LoanScheduleData data = new LoanScheduleData();
				data = s_dao.getLAODFSchedule(req.getAccnumber(), conn);
				if (data.getDatalist().size() > 0) {
					response.setScheduledatalist(data);
					response.setRetcode("300");
					response.setRetmessage("Successful.");
				} else {
					response.setRetcode("220");
					response.setRetmessage("No Data Record.");
				}

			} else {
				response = dao.getLoanInformation(req.getAccnumber(), conn);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public depositaccdataresult getAmountOfAccount(String customerid, String fixeddeposit, String customertype,
			String[] excludedaccounts) throws Exception {
		depositaccdataresult result = new depositaccdataresult();
		service001Dao sDao = new service001Dao();
		Connection conn = null;
		conn = ConnAdmin.getConn3();
		try {
			CustInfoResult cres = sDao.getAmountOfAccount(customerid, fixeddeposit, customertype, excludedaccounts, conn);
			if (cres.getCode().equals("300")) {
				int arrlength = cres.getAccountInfoArr().length;
				if (arrlength > 0) {
					BalanceTotalResponse[] balanceTotalDataArr = new BalanceTotalResponse[arrlength];
					for (int i = 0; i < arrlength; i++) {
						BalanceTotalResponse accdata = new BalanceTotalResponse();
						accdata.setAvailablebalance(
								GeneralUtil.formatDecimalString(cres.getAccountInfoArr()[i].getAvailablebalance()));
						accdata.setProductname(cres.getAccountInfoArr()[i].getProductname());
						accdata.setProducttype(cres.getAccountInfoArr()[i].getProducttype());
						balanceTotalDataArr[i] = accdata;
					}
					result.setBalancedatalist(balanceTotalDataArr);
				}
			}
			result.setRetcode(cres.getCode());
			result.setRetmessage(cres.getDesc());
		} catch (Exception e) {
			result.setRetcode("220");
			result.setRetmessage("Server Error");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return result;
	}

	public depositaccdataresult getAccountsByProduct(String customerid, String fixeddeposit, String searchtype,String customertype,
			String[] excludedaccounts) throws Exception {
		depositaccdataresult result = new depositaccdataresult();
		service001Dao sDao = new service001Dao();
		Connection conn = null;
		conn = ConnAdmin.getConn3();
		try {
			CustInfoResult cres = sDao.getAccountInfobyProduct(customerid, fixeddeposit, searchtype,customertype, excludedaccounts,
					conn);
			if (cres.getCode().equals("300")) {
				int arrlength = cres.getAccountInfoArr().length;
				if (arrlength > 0) {
					depositaccdata[] depAccDataArr = new depositaccdata[arrlength];
					for (int i = 0; i < arrlength; i++) {
						depositaccdata accdata = new depositaccdata();
						accdata.setAccnumber(cres.getAccountInfoArr()[i].getAccnumber());
						accdata.setAvailablebalance(
								GeneralUtil.formatDecimalString(cres.getAccountInfoArr()[i].getAvailablebalance()));
						accdata.setCurrentbalance(
								GeneralUtil.formatDecimalString(cres.getAccountInfoArr()[i].getCurrentbalance()));
						accdata.setCurcode(cres.getAccountInfoArr()[i].getCurcode());
						accdata.setAcctype(cres.getAccountInfoArr()[i].getAcctype());
						accdata.setAccname(cres.getAccountInfoArr()[i].getAccname());
						accdata.setStatus(cres.getAccountInfoArr()[i].getAccstatus());
						accdata.setOpeningdate(cres.getAccountInfoArr()[i].getOpeningdate());
						accdata.setBrname(cres.getAccountInfoArr()[i].getBrname());
						accdata.setProductname(cres.getAccountInfoArr()[i].getProductname());
						accdata.setProducttype(cres.getAccountInfoArr()[i].getProducttype());
						accdata.setBrcode(cres.getAccountInfoArr()[i].getBrcode());
						depAccDataArr[i] = accdata;
					}
					result.setDatalist(depAccDataArr);
				}
			}
			result.setRetcode(cres.getCode());
			result.setRetmessage(cres.getDesc());
		} catch (Exception e) {
			result.setRetcode("220");
			result.setRetmessage("Server Error");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return result;
	}

	public CutOffDataResult getcutoff() {
		CutOffDataResult res = new CutOffDataResult();
		service001Dao l_dao = new service001Dao();
		res = l_dao.getcutoff();
		return res;
	}

	public BankHolidayDataRes checkBankHoliday(String checkdate){
		BankHolidayDataRes res = new BankHolidayDataRes();
		Connection conn = null;
		conn = ConnAdmin.getConn3();
		try {
			checkdate = GeneralUtil.changedateformat3(checkdate);
			res = TransactionMgr.checkBankHoliday(checkdate, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	
	public static void sysLogWrite(String token,String request_body,int log_type,String service_name,String function_name){
		service001Dao dao = new service001Dao();
		Connection conn = null;
		conn = ConnAdmin.getConn();
		try {
			dao.insertSysLog(token,request_body,log_type,service_name,function_name, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
