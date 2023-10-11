package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.sql.SQLException;

import com.nirvasoft.rp.dao.InterestDAO;
import com.nirvasoft.rp.dao.LastDatesDAO;
import com.nirvasoft.rp.shared.BudgetYearData;
import com.nirvasoft.rp.shared.CallDepositInterestData;
import com.nirvasoft.rp.shared.CurrentInterestData;
import com.nirvasoft.rp.shared.SavingInterestData;
import com.nirvasoft.rp.shared.SpecialInterestData;
import com.nirvasoft.rp.shared.UniversalInterestData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DBInterestProcessingMgr {
	
	public static CurrentInterestData getCurrentInterestData(String pAccountNumber,Connection pConn) {
		CurrentInterestData l_CaIntData = new CurrentInterestData();
		InterestDAO interestDao = new InterestDAO();
		String l_providedDate = "";
		l_providedDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		int l_CAIType = SharedLogic.getSystemData().getpSystemSettingDataList().get("CAI").getN1();
		try {
			if(l_CAIType == 1) {				
				l_CaIntData = interestDao.getProvidedAmountCAIType1(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_CAIType == 3) {
				l_CaIntData = getProvidedAmountCAIType3(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_CAIType == 4) {
				l_CaIntData = getProvidedAmountCAIType4(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_CAIType == 5) {
				l_CaIntData = getProvidedAmountCAIType5(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_CAIType == 6) {
				l_CaIntData = getProvidedAmountCAIType6(pAccountNumber, l_providedDate, pConn);					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		l_CaIntData.setAccNumber(pAccountNumber);
		
		return l_CaIntData;
	}	

	@SuppressWarnings("deprecation")
	private static CurrentInterestData getProvidedAmountCAIType3(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		CurrentInterestData l_SavIntData = new CurrentInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();		
		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		String[] l_DateArr = new String[2];
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
						
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SavIntData = l_SavIntDAO.getProvidedAmountCAIType3(pAccountNumber, Date2, pProvidedDate, pConn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_SavIntData;
	}
	
	@SuppressWarnings("deprecation")
	private static CurrentInterestData getProvidedAmountCAIType4(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		CurrentInterestData l_SavIntData = new CurrentInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();		
		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		String[] l_DateArr = new String[2];
		l_DateArr = StrUtil.WhichHalfYear(pProvidedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -6);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -6);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SavIntData = l_SavIntDAO.getProvidedAmountCAIType4(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_SavIntData ;
	}
	
	@SuppressWarnings("deprecation")
	private static CurrentInterestData getProvidedAmountCAIType5(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		CurrentInterestData l_SavIntData = new CurrentInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");			
		Date l_ProvDate= new Date();
		String Date2 = "";
		try {
			l_ProvDate = formatter.parse(pProvidedDate);			
			l_ProvDate.setDate(1);
			
			Date2 = formatter.format(l_ProvDate);
			l_SavIntData = l_SavIntDAO.getProvidedAmountCAIType5(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SavIntData ;
	}
	
	@SuppressWarnings("deprecation")
	private static CurrentInterestData getProvidedAmountCAIType6(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		CurrentInterestData l_SavIntData = new CurrentInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();
		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		String[] l_DateArr = new String[2];
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);		
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);			
			cal.setTime(d2);			
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
						
			l_SavIntData = l_SavIntDAO.getProvidedAmountCAIType6(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SavIntData ;
	}
	
	
	public static SavingInterestData getSavingInterestData(String pAccountNumber,Connection pConn) {
		SavingInterestData l_SavIntData = new SavingInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		String l_providedDate = "";
		l_providedDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		int l_SAIType = SharedLogic.getSystemData().getpSystemSettingDataList().get("SAI").getN1();
		try {
			if(l_SAIType == 1) {				
				l_SavIntData = l_SavIntDAO.getProvidedAmountSAIType1(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_SAIType == 3) {
				l_SavIntData = getProvidedAmountSAIType3(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_SAIType == 4) {
				l_SavIntData = getProvidedAmountSAIType4(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_SAIType == 5) {
				l_SavIntData = getProvidedAmountSAIType5(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_SAIType == 6) {
				l_SavIntData = getProvidedAmountSAIType6(pAccountNumber, l_providedDate, pConn);					
			}
			if (l_SAIType == 7) {
				l_SavIntData = getProvidedAmountSAIType7(pAccountNumber, l_providedDate, pConn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		l_SavIntData.setAccNumber(pAccountNumber);
		
		return l_SavIntData;
	}	
	
	@SuppressWarnings("deprecation")
	private static SavingInterestData getProvidedAmountSAIType3(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		SavingInterestData l_SavIntData = new SavingInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
						
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SavIntData = l_SavIntDAO.getProvidedAmountSAIType3(pAccountNumber, Date2, pProvidedDate, pConn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_SavIntData;
	}
	
	@SuppressWarnings("deprecation")
	private static SavingInterestData getProvidedAmountSAIType4(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		SavingInterestData l_SavIntData = new SavingInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(2, pConn);
		l_DateArr = StrUtil.WhichHalfYear(pProvidedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -6);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -6);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SavIntData = l_SavIntDAO.getProvidedAmountSAIType4(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_SavIntData ;
	}
	
	@SuppressWarnings("deprecation")
	private static SavingInterestData getProvidedAmountSAIType5(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		SavingInterestData l_SavIntData = new SavingInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");			
		Date l_ProvDate= new Date();
		String Date2 = "";
		try {
			l_ProvDate = formatter.parse(pProvidedDate);			
			l_ProvDate.setDate(1);
			
			Date2 = formatter.format(l_ProvDate);
			l_SavIntData = l_SavIntDAO.getProvidedAmountSAIType5("",pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SavIntData ;
	}
	
	@SuppressWarnings("deprecation")
	private static SavingInterestData getProvidedAmountSAIType6(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		SavingInterestData l_SavIntData = new SavingInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);		
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);			
			cal.setTime(d2);			
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
						
			l_SavIntData = l_SavIntDAO.getProvidedAmountSAIType6(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SavIntData ;
	}
	
	private static SavingInterestData getProvidedAmountSAIType7(String pAccountNumber, String l_providedDate, Connection pConn) throws SQLException {
		InterestDAO l_SavIntDAO = new InterestDAO();
		SavingInterestData l_SavIntData = new SavingInterestData();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();
		
		l_BudgetData=l_lastDateDAO.getBudgetYearData(2, pConn);
		l_DateArr = StrUtil.WhichHalfYear(l_providedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
		
			d2 = formatter.parse(l_DateArr[1]);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -6);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -6);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SavIntData = l_SavIntDAO.getProvidedAmountSAIType7(pAccountNumber, Date2, l_providedDate, pConn);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return l_SavIntData;
	}
	
	public static SpecialInterestData getSpecialInterestData(String pOpeningDate, String paccountNumber, Connection pconn) {
		SpecialInterestData l_SpData=new SpecialInterestData();
		InterestDAO l_SpDao=new InterestDAO();
		String l_providedDate = "";
		double interest = 0;
		l_providedDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		int l_SPIType = SharedLogic.getSystemData().getpSystemSettingDataList().get("SPI").getN1();
		try {
			if((l_SPIType == 1) || (l_SPIType == 8)){				
				l_SpData = l_SpDao.getProvidedAmountSPIType1(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_SPIType == 3) {
				l_SpData = getProvidedAmountSPIType3(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_SPIType == 4) {
				l_SpData = getProvidedAmountSPIType4(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_SPIType == 5) {
				l_SpData = getProvidedAmountSPIType5(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_SPIType == 6) {
				l_SpData = getProvidedAmountSPIType6(paccountNumber, l_providedDate, pconn);					
			}else if(l_SPIType == 7){
				interest = l_SpDao.calculateSPInterestForAccountClose(pOpeningDate, paccountNumber, l_providedDate, pconn);
				l_SpData.setInterest(interest);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		l_SpData.setAccNumber(paccountNumber);
		
		return l_SpData;
	}
	
	@SuppressWarnings("deprecation")
	private static SpecialInterestData getProvidedAmountSPIType3(String paccountNumber, String l_providedDate, Connection pconn) throws SQLException {
		SpecialInterestData l_SpIntData = new SpecialInterestData();
		InterestDAO l_SpIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pconn);
	
		l_DateArr = StrUtil.WhichQuarter(l_providedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
						
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SpIntData = l_SpIntDAO.getProvidedAmountSPIType3(paccountNumber, Date2, l_providedDate, pconn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_SpIntData;
	}	
	@SuppressWarnings("deprecation")
	private static SpecialInterestData getProvidedAmountSPIType4(String paccountNumber, String l_providedDate, Connection pconn) throws SQLException {
		SpecialInterestData l_SpIntData = new SpecialInterestData();
		InterestDAO l_SpIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(2, pconn);
		
		l_DateArr = StrUtil.WhichHalfYear(l_providedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -6);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -6);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_SpIntData = l_SpIntDAO.getProvidedAmountSPIType4(paccountNumber, Date2, l_providedDate, pconn);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return l_SpIntData ;
	}
	@SuppressWarnings("deprecation")
	private static SpecialInterestData getProvidedAmountSPIType5(String paccountNumber, String l_providedDate, Connection pconn) throws SQLException {
		SpecialInterestData l_SpIntData = new SpecialInterestData();
		InterestDAO l_SpIntDAO = new InterestDAO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");			
		Date l_ProvDate= new Date();
		String Date2 = "";
		try {
			l_ProvDate = formatter.parse(l_providedDate);			
			l_ProvDate.setDate(1);
			
			Date2 = formatter.format(l_ProvDate);
			l_SpIntData = l_SpIntDAO.getProvidedAmountSPIType5(paccountNumber, Date2, l_providedDate, pconn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SpIntData ;
	
	}
	
	@SuppressWarnings("deprecation")
	private static SpecialInterestData getProvidedAmountSPIType6(String paccountNumber, String l_providedDate, Connection pconn) throws SQLException {
		SpecialInterestData l_SpIntData = new SpecialInterestData();
		InterestDAO l_SpIntDAO = new InterestDAO();
		
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pconn);
		l_DateArr = StrUtil.WhichQuarter(l_providedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);		
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);			
			cal.setTime(d2);			
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
						
			l_SpIntData = l_SpIntDAO.getProvidedAmountSPIType6(paccountNumber, Date2, l_providedDate, pconn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SpIntData ;
	
	}
	
	
	public static CallDepositInterestData getCallInterestData(String pOpeningDate, String paccountNumber, Connection pconn) throws SQLException, ParseException{
		CallDepositInterestData callIntData = new CallDepositInterestData();
		InterestDAO callIntDao = new InterestDAO();
		String l_providedDate = "";
		double interest = 0;
		l_providedDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		int l_CDIType =  SharedLogic.getSystemData().getpSystemSettingDataList().get("CDI").getN6() ;
		if(!callIntDao.isInterestNoPay(paccountNumber, pconn)){
			if(l_CDIType == 1){
				callIntData = callIntDao.getProvidedAmountCDIType1(paccountNumber, l_providedDate, pconn);			
				interest = callIntDao.calculateCallInterestForAccountClose(pOpeningDate, paccountNumber, l_providedDate, pconn);
				callIntData.setInterest(callIntData.getInterest()+interest);
			} 
			else if(l_CDIType == 3) {
				callIntData = getProvidedAmountCDIType3(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_CDIType == 4) {
				callIntData = getProvidedAmountCDIType4(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_CDIType == 5) {
				callIntData = getProvidedAmountCDIType5(paccountNumber, l_providedDate, pconn);					
			}
			else if(l_CDIType == 6) {
				callIntData = getProvidedAmountCDIType6(paccountNumber, l_providedDate, pconn);
				interest = callIntDao.calculateCallInterestForAccountClose(pOpeningDate, paccountNumber, l_providedDate, pconn);
				callIntData.setInterest(callIntData.getInterest()+interest);
			}
			else if(l_CDIType == 7){
				interest = callIntDao.calculateCallInterestForAccountClose(pOpeningDate, paccountNumber, l_providedDate, pconn);
				callIntData.setInterest(interest);
			} else if (l_CDIType == 8) {
				double accured = 0; //Hnns Accrued
				accured = callIntDao.calculateCallInterestForAccountClose(pOpeningDate, paccountNumber, l_providedDate, pconn);

				callIntData.setAccured(accured);
			}
		}		
		return callIntData;		
	}
	
	private static CallDepositInterestData getProvidedAmountCDIType3(String paccountNumber, String l_providedDate, Connection pconn) throws SQLException, ParseException {
		CallDepositInterestData callIntData = new CallDepositInterestData();
		InterestDAO callDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();
		
		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pconn);
		l_DateArr = StrUtil.WhichQuarter(l_providedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		d1 = formatter.parse(l_DateArr[0]);
		d2 = formatter.parse(l_DateArr[1]);
					
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.add(Calendar.MONTH, -3);
		d1 = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.setTime(d2);
		cal.add(Calendar.MONTH, -3);		
		d2 = cal.getTime();
		
		cal = Calendar.getInstance();
		d2.setDate(1);		
		cal.setTime(d2);
		cal.add(Calendar.MONTH,+1);
		cal.add(Calendar.DATE,-1);
		d2 = cal.getTime();
		Date2 = formatter2.format(d2);
		
		callIntData = callDAO.getProvidedAmountCDIType3(paccountNumber, Date2, l_providedDate, pconn);
		
		return callIntData;
	}	
	
	private static CallDepositInterestData getProvidedAmountCDIType4(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException, ParseException {
		CallDepositInterestData callIntData = new CallDepositInterestData();
		InterestDAO callIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();
		l_BudgetData=l_lastDateDAO.getBudgetYearData(2, pConn);
		l_DateArr = StrUtil.WhichHalfYear(pProvidedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		d1 = formatter.parse(l_DateArr[0]);
		d2 = formatter.parse(l_DateArr[1]);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.add(Calendar.MONTH, -6);
		d1 = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.setTime(d2);
		cal.add(Calendar.MONTH, -6);		
		d2 = cal.getTime();
		
		cal = Calendar.getInstance();
		d2.setDate(1);		
		cal.setTime(d2);
		cal.add(Calendar.MONTH,+1);
		cal.add(Calendar.DATE,-1);
		d2 = cal.getTime();
		Date2 = formatter2.format(d2);
		
		callIntData = callIntDAO.getProvidedAmountCDIType4(pAccountNumber, Date2, pProvidedDate, pConn);			
		
		return callIntData ;
	}
	
	private static CallDepositInterestData getProvidedAmountCDIType5(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException, ParseException {
		CallDepositInterestData callIntData = new CallDepositInterestData();
		InterestDAO callIntDAO = new InterestDAO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");			
		Date l_ProvDate= new Date();
		String Date2 = "";
		
		l_ProvDate = formatter.parse(pProvidedDate);			
		l_ProvDate.setDate(1);
		
		Date2 = formatter.format(l_ProvDate);
		callIntData = callIntDAO.getProvidedAmountCDIType5(pAccountNumber, Date2, pProvidedDate, pConn);			
		
		return callIntData ;
	}
	
	private static CallDepositInterestData getProvidedAmountCDIType6(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException, ParseException {
		CallDepositInterestData callIntData = new CallDepositInterestData();
		InterestDAO callIntDAO = new InterestDAO();
		
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		d1 = formatter.parse(l_DateArr[0]);
		d2 = formatter.parse(l_DateArr[1]);		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.add(Calendar.MONTH, -3);
		d1 = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.setTime(d2);
		cal.add(Calendar.MONTH, -3);		
		d2 = cal.getTime();
		
		cal = Calendar.getInstance();
		d2.setDate(1);			
		cal.setTime(d2);			
		cal.add(Calendar.MONTH,+1);
		cal.add(Calendar.DATE,-1);
		d2 = cal.getTime();
		Date2 = formatter2.format(d2);
					
		callIntData = callIntDAO.getProvidedAmountCDIType6(pAccountNumber, Date2, pProvidedDate, pConn);			
			
		return callIntData ;
	}
	
	public static UniversalInterestData getUniversalInterestData(String pAccountNumber,Connection pConn) {
		UniversalInterestData l_UniIntData = new UniversalInterestData();
		InterestDAO l_UniIntDAO = new InterestDAO();
		String l_providedDate = "";
		l_providedDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		int l_UAIType = SharedLogic.getSystemData().getpSystemSettingDataList().get("UAI").getN1() ;
		try {
			if(l_UAIType == 1) {				
				l_UniIntData = l_UniIntDAO.getProvidedAmountUAIType1(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_UAIType == 3) {
				l_UniIntData = getProvidedAmountUAIType3(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_UAIType == 4) {
				l_UniIntData = getProvidedAmountUAIType4(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_UAIType == 5) {
				l_UniIntData = getProvidedAmountUAIType5(pAccountNumber, l_providedDate, pConn);					
			}
			if(l_UAIType == 6) {
				l_UniIntData = getProvidedAmountUAIType6(pAccountNumber, l_providedDate, pConn);					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		l_UniIntData.setAccNumber(pAccountNumber);
		
		return l_UniIntData;
	}
	
	private static UniversalInterestData getProvidedAmountUAIType3(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		UniversalInterestData l_UniIntData = new UniversalInterestData();
		InterestDAO l_UniIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate, l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
						
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_UniIntData = l_UniIntDAO.getProvidedAmountUAIType3(pAccountNumber, Date2, pProvidedDate, pConn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_UniIntData;
	}
	
	private static UniversalInterestData getProvidedAmountUAIType4(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		UniversalInterestData l_UAIIntData = new UniversalInterestData();
		InterestDAO l_UAIIntDAO = new InterestDAO();
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(2, pConn);
		l_DateArr = StrUtil.WhichHalfYear(pProvidedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");			
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -6);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -6);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);		
			cal.setTime(d2);
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
			
			l_UAIIntData = l_UAIIntDAO.getProvidedAmountUAIType4(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		return l_UAIIntData ;
	}
	
	private static UniversalInterestData getProvidedAmountUAIType5(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		UniversalInterestData l_SavIntData = new UniversalInterestData();
		InterestDAO l_SavIntDAO = new InterestDAO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");			
		Date l_ProvDate= new Date();
		String Date2 = "";
		try {
			l_ProvDate = formatter.parse(pProvidedDate);			
			l_ProvDate.setDate(1);
			
			Date2 = formatter.format(l_ProvDate);
			l_SavIntData = l_SavIntDAO.getProvidedAmountUAIType5(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_SavIntData ;
	}
	
	private static UniversalInterestData getProvidedAmountUAIType6(String pAccountNumber,String pProvidedDate, Connection pConn) throws SQLException {
		UniversalInterestData l_UAIIntData = new UniversalInterestData();
		InterestDAO l_UAIIntDAO = new InterestDAO();
		
		String[] l_DateArr = new String[2];
		BudgetYearData l_BudgetData=new BudgetYearData();
		LastDatesDAO l_lastDateDAO=new LastDatesDAO();

		l_BudgetData=l_lastDateDAO.getBudgetYearData(1, pConn);
		l_DateArr = StrUtil.WhichQuarter(pProvidedDate,l_BudgetData);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date d1= new Date();
		Date d2= new Date();
		
		String Date2 = "";
		try {
			d1 = formatter.parse(l_DateArr[0]);
			d2 = formatter.parse(l_DateArr[1]);		
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);
			cal.add(Calendar.MONTH, -3);
			d1 = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(d2);
			cal.add(Calendar.MONTH, -3);		
			d2 = cal.getTime();
			
			cal = Calendar.getInstance();
			d2.setDate(1);			
			cal.setTime(d2);			
			cal.add(Calendar.MONTH,+1);
			cal.add(Calendar.DATE,-1);
			d2 = cal.getTime();
			Date2 = formatter2.format(d2);
						
			l_UAIIntData = l_UAIIntDAO.getProvidedAmountUAIType6(pAccountNumber, Date2, pProvidedDate, pConn);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return l_UAIIntData ;
	}
	
}
