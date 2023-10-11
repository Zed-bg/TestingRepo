package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.dao.AccountTransactionDAO;
import com.nirvasoft.rp.dao.TransAmtLimitDAO;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.TransAmtLimitData;
import com.nirvasoft.rp.util.StrUtil;

public class DBTransAmtLimitMgr {
	public static DataResult checkTransAmtLimit(String pProductID,String pAccCode,int pTransType,String pAccNumber,String pDate,
			double Amt,boolean isIBTDeposit,boolean isIBTWithdraw, Connection pConn) throws SQLException{
		DataResult ret = new DataResult();
		TransAmtLimitDAO l_Dao = new TransAmtLimitDAO();
		AccountTransactionDAO l_TransDao = new AccountTransactionDAO();
		TransAmtLimitData l_Data = new TransAmtLimitData();
		ArrayList<TransAmtLimitData> lstData = new ArrayList<TransAmtLimitData>();
		int l_TransType =pTransType;
		if(isIBTDeposit && pTransType<500)
			l_TransType = 0;
		else if(isIBTWithdraw && pTransType>500)
			l_TransType = 500;
		lstData = l_Dao.getTransAmtLimits(pProductID, pAccCode, l_TransType, pConn);
		ret.setStatus(true);
		for(int i=0;i<lstData.size();i++) {
			if(ret.getStatus()){
				if(lstData.get(i).getTenureType()==TransAmtLimitData.mDaily){
					l_Data = l_TransDao.getTransCountNAmountPerDay(pAccNumber, pDate, lstData.get(i).getTransTypeFrom(), lstData.get(i).getTransTypeTo(), pConn);
					if(lstData.get(i).getTransCount()!=-1 && l_Data.getTransCount()+1>lstData.get(i).getTransCount()){
						ret.setStatus(false);
						ret.setDescription("Transaction times exceed the Daily limit, "+lstData.get(i).getTransCount());
					}
					if(ret.getStatus() && lstData.get(i).getTransAmount()!=0 && StrUtil.round2decimals(l_Data.getTransAmount()+Amt)>lstData.get(i).getTransAmount()){
						ret.setStatus(false);
						ret.setDescription("Amount exceeds the Daily limit, "+ StrUtil.formatNumberwithComma(lstData.get(i).getTransAmount()));
					}
				}
			}
			if(ret.getStatus()){
				if(lstData.get(i).getTenureType()==TransAmtLimitData.mWeekly){
					l_Data = l_TransDao.getTransCountNAmountPerWeek(pAccNumber, pDate, lstData.get(i).getTransTypeFrom(), lstData.get(i).getTransTypeTo(), pConn);
					if(lstData.get(i).getTransCount()!=-1 && l_Data.getTransCount()+1>lstData.get(i).getTransCount()){
						ret.setStatus(false);
						ret.setDescription("Transaction times exceeds the Weekly limit, "+lstData.get(i).getTransCount());
					}
					if(ret.getStatus() && lstData.get(i).getTransAmount()!=0 && StrUtil.round2decimals(l_Data.getTransAmount()+Amt)>lstData.get(i).getTransAmount()){
						ret.setStatus(false);
						ret.setDescription("Amount exceed the Weekly limit, "+ StrUtil.formatNumberwithComma(lstData.get(i).getTransAmount()));
					}
						
					
				}
			}
			if(ret.getStatus()){
				if(lstData.get(i).getTenureType()==TransAmtLimitData.mMonthly){
					l_Data = l_TransDao.getTransCountNAmountPerMonth(pAccNumber, pDate, lstData.get(i).getTransTypeFrom(), lstData.get(i).getTransTypeTo(), pConn);
					if(lstData.get(i).getTransCount()!=-1 && l_Data.getTransCount()+1>lstData.get(i).getTransCount()){
						ret.setStatus(false);
						ret.setDescription("Transaction times exceeds the Monthly limit, "+lstData.get(i).getTransCount());
					}
					if(ret.getStatus() && lstData.get(i).getTransAmount()!=0 && StrUtil.round2decimals(l_Data.getTransAmount()+Amt)>lstData.get(i).getTransAmount()){
						ret.setStatus(false);
						ret.setDescription("Amount exceed the Monthly limit, "+ StrUtil.formatNumberwithComma(lstData.get(i).getTransAmount()));
					}
				}
			}
			if(ret.getStatus()){
				if(lstData.get(i).getTenureType()==TransAmtLimitData.mYearly){
					l_Data = l_TransDao.getTransCountNAmountPerYear(pAccNumber, pDate, lstData.get(i).getTransTypeFrom(), lstData.get(i).getTransTypeTo(), pConn);
					if(lstData.get(i).getTransCount()!=-1 && l_Data.getTransCount()+1>lstData.get(i).getTransCount()){
						ret.setStatus(false);
						ret.setDescription("Transaction times exceeds the Yearly limit, "+lstData.get(i).getTransCount());
					}
					if(ret.getStatus() && lstData.get(i).getTransAmount()!=0 && StrUtil.round2decimals(l_Data.getTransAmount()+Amt)>lstData.get(i).getTransAmount()){
						ret.setStatus(false);
						ret.setDescription("Amount exceed the Yearly limit, "+ StrUtil.formatNumberwithComma(lstData.get(i).getTransAmount()));
					}
				}
			}
		}
		return ret;
	}
}
