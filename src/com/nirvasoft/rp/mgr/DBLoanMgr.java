package com.nirvasoft.rp.mgr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.dao.AccBalanceArchiveDAO;
import com.nirvasoft.rp.dao.AccountDao;
import com.nirvasoft.rp.dao.LAODFDAO;
import com.nirvasoft.rp.dao.LoanAccountDAO;
import com.nirvasoft.rp.dao.LoanAllowSetupDAO;
import com.nirvasoft.rp.dao.LoanInterestDAO;
import com.nirvasoft.rp.dao.LoanServiceChargesExtendedDAO;
import com.nirvasoft.rp.dao.ProductInteRateDAO;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.shared.AccountTransactionResponse;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.LAODFData;
import com.nirvasoft.rp.shared.LAODFScheduleData;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.LoanAllowSetupData;
import com.nirvasoft.rp.shared.LoanRepaymentReq;
import com.nirvasoft.rp.shared.LoanRepaymentRes;
import com.nirvasoft.rp.shared.LoanScheduleRepaymentReq;
import com.nirvasoft.rp.shared.ODTypeDescData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.shared.bulkpaymentdatareq;
import com.nirvasoft.rp.shared.gopaymentresponse;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.icbs.SMSReturnData;
import com.nirvasoft.rp.shared.icbs.TransferData;
import com.nirvasoft.rp.shared.icbs.WithdrawData;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.util.ServerGlobal;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;

public class DBLoanMgr {	
	
	public static LoanRepaymentRes postLoanRepayment(LoanRepaymentReq aReq) {//type= 1(topup), 2(merchant), 3 bulkPayment
		LoanRepaymentRes res = new LoanRepaymentRes();
		TransactionMgr transMgr = new TransactionMgr();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				res.setRetcode("220");
				res.setRetmessage("Internal Error (DB connection Failed!)");
				return res;
			}
				
			l_Conn.setAutoCommit(false);
			
			/// Post
			TransferData pTransferData = new TransferData();

			pTransferData.setFromAccNumber(aReq.getLinkedaccnumber());
			pTransferData.setFromBranchCode(aReq.getLinkedaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				//add To acc
			pTransferData.setToAccNumber(aReq.getLoanaccnumber());
			pTransferData.setToBranchCode(aReq.getLoanaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			
			pTransferData.setAmount(aReq.getAmount());
			pTransferData.setInterest(aReq.getInterestamt());
			pTransferData.setServiceCharges(aReq.getServicechargesamt());
			
			pTransferData.setRemark("");
			pTransferData.setFinishCutOffTime(false);
			
			result tres = authmgr.validateToken(aReq.getToken(),"",aReq.getLoanaccnumber(),"",2,l_Conn);
			if(tres.isState()){
				res = transMgr.postLoanRepayment(pTransferData,aReq,l_Conn);
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
	
	public static LoanRepaymentRes postLoanScheduleRepayment(LoanScheduleRepaymentReq aReq) {//type= 1(topup), 2(merchant), 3 bulkPayment
		LoanRepaymentRes res = new LoanRepaymentRes();
		TransactionMgr transMgr = new TransactionMgr();
		Connection l_Conn = null;
		try {
			l_Conn = ConnAdmin.getConn3();
			if( l_Conn == null) {
				res.setRetcode("220");
				res.setRetmessage("Internal Error (DB connection Failed!)");
				return res;
			}
				
			l_Conn.setAutoCommit(false);
			
			LAODFScheduleData scheduledata = aReq.getData();
			scheduledata.setMebinterestamount("0");
			/// Post
			TransferData pTransferData = new TransferData();

			pTransferData.setFromAccNumber(scheduledata.getBackaccnumber());
			pTransferData.setFromBranchCode(scheduledata.getBackaccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
				//add To acc
			pTransferData.setToAccNumber(scheduledata.getAccnumber());
			pTransferData.setToBranchCode(scheduledata.getAccnumber().substring(ServerGlobal.getmBrCodeStart(), ServerGlobal.getmBrCodeEnd()));
			
			pTransferData.setAmount(Double.parseDouble(scheduledata.getRepaymentamount()));
			
			pTransferData.setRemark("");
			pTransferData.setFinishCutOffTime(false);
			
			result tres = authmgr.validateToken(aReq.getToken(),"",scheduledata.getAccnumber(),"",2,l_Conn);
			if(tres.isState()){
				res = transMgr.postLoanScheduleRepayment(pTransferData,scheduledata,l_Conn);
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
	
}
