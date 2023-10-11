package com.nirvasoft.rp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXB;

import com.google.gson.Gson;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.shared.CMSModuleConfigData;
import com.nirvasoft.rp.shared.depositaccdata;
public class GeneralUtil {
	private static String apppath = "";

	public static String addMonth(String date1, int i) {
		String[] date = date1.split("-");
		System.out.println("Result 1 : " + date);

		int day = Integer.parseInt(date[2]);
		System.out.println("Result day: " + day);

		int month = Integer.parseInt(date[1]);
		System.out.println("Result month : " + month);

		int year = Integer.parseInt(date[0]);
		System.out.println("Result year : " + year);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		System.out.println("set date is " + dateFormat.format(c.getTime()));
		c.add(Calendar.MONTH, i);
		System.out.println("add date " + dateFormat.format(c.getTime()));
		return dateFormat.format(c.getTime());
	}

	public static String changedateformat(String pdate) {
		String l_date = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			pdate = pdate.replace('/', '-');
			Date varDate = dateFormat.parse(pdate);
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			l_date = dateFormat.format(varDate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return l_date;
	}

	// wcs
	public static int compareDate(String date1, String date2) {
		//
		Date d1 = new Date();// expdate
		Date d2 = new Date();// todaydate
		int ret = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			d1 = sdf.parse(date1);
			d2 = sdf.parse(date2);

			ret = d1.compareTo(d2);
			// ret >0 =>Date1 is after Date2
			// ret <0 =>Date1 is before Date2
			// ret =0 =>Date1 is before Date2

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static long diffDateTime(String date1, String date2) {
		//
		Date d1 = new Date();// expdate
		Date d2 = new Date();// todaydate
		long diffMinutes = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d1 = sdf.parse(date1);
			d2 = sdf.parse(date2);
			long diff = d1.getTime() - d2.getTime();
			diffMinutes = diff / (60 * 1000) % 60; 

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diffMinutes;
	}
	
	public static long diffDateTimeYYYYMMDDHHMMSS(String date1, String date2) {
		//
		Date d1 = new Date();// expdate
		Date d2 = new Date();// todaydate
		long diffMinutes = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			d1 = sdf.parse(date1);
			d2 = sdf.parse(date2);
			long diff = d1.getTime() - d2.getTime();
			diffMinutes = diff / (60 * 1000) % 60; 

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diffMinutes;
	}
	
	public static String datetoddMMMyyyywithSlach(String aDate) {
		String l_date = "";
		java.util.Date l_Date = new java.util.Date();
		SimpleDateFormat l_sdf = new SimpleDateFormat("dd/MMM/yyyy");
		SimpleDateFormat l_sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			l_Date = l_sdf.parse(aDate);
			l_date = l_sdf2.format(l_Date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l_date;
	}

	public static String datetoString() {
		String l_date = "";
		java.util.Date l_Date = new java.util.Date();
		SimpleDateFormat l_sdf = new SimpleDateFormat("yyyyMMdd");
		l_date = l_sdf.format(l_Date);

		return l_date;
	}

	public static String datetoStringFlexcubeFormat() {
		String l_date = "";
		java.util.Date l_Date = new java.util.Date();
		SimpleDateFormat l_sdf = new SimpleDateFormat("yyyy-MM-dd");
		l_date = l_sdf.format(l_Date);

		return l_date;
	}

	public static String diffDay(String d1, String d2) {
		Date date1;
		Date date2;
		String dayDifference = "";
		SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

		// Setting dates
		try {
			date1 = dates.parse(d1);
			date2 = dates.parse(d2);
			if (date1.getTime() >= date2.getTime()) {
				// Comparing dates
				long difference = date1.getTime() - date2.getTime();
				long differenceDates = difference / (24 * 60 * 60 * 1000);

				// Convert long to String
				dayDifference = Long.toString(differenceDates);
				System.out.println("Result : " + dayDifference);
			} else {
				dayDifference = "-1";// Go To Force Change Password!
				// System.out.println("fff");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayDifference;
	}

	public static String formatddMMYYYY2YYYYMMdd(String p) {
		String ret = "";
		String dd = "";
		String mm = "";
		try {
			int d = Integer.parseInt(p.substring(0, 2));

			int m = Integer.parseInt(p.substring(3, 5));

			int y = Integer.parseInt(p.substring(6, 10));

			if (d < 10) {
				dd = "0" + d;
			} else {
				dd = p.substring(0, 2);
			}
			if (m < 10) {
				mm = "0" + m;
			} else {
				mm = p.substring(3, 5);
			}
			ret = y + "" + mm + "" + dd;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	public static String formatddMMYYYY2YYYYMMDD(String p) {
		String ret = "";
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat f2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date d;
		try {
			d = f.parse(p);
			ret = f2.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String formatNumber(double pAmount) {
		String l_result = "0.00";
		DecimalFormat l_df = new DecimalFormat("#,###.00");
		l_result = l_df.format(pAmount);
		return l_result;
	}
	
	public static double formatDecimal(double pAmount) {
		double l_result = 0.00;
		DecimalFormat l_df = new DecimalFormat("###.00");
		l_result = Double.parseDouble(l_df.format(pAmount));
		return l_result;
	}
	
	public static String formatDecimalString(double pAmount) {
		String l_result = "0.00";
		if(pAmount != 0) {
		DecimalFormat l_df = new DecimalFormat("###.00");
		l_result = l_df.format(pAmount);
		}
		return l_result;
	}

	public static String getDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dat = sdf.format(d);
		return dat;
	}

	public static String getTimeA() {
		String l_Time = "";
		SimpleDateFormat l_DateFormat = new SimpleDateFormat("hh:mm:ss a");
		Calendar l_Canlendar = Calendar.getInstance();
		l_Time = l_DateFormat.format(l_Canlendar.getTime());

		return l_Time;
	}
	
	public static String getDateTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		String dat = sdf.format(d);
		return dat;
	}
	
	public static String getDateTimeYMD() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		String dat = sdf.format(d);
		return dat;
	}

	public static String getDateYYYYMMDD() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dat = sdf.format(d);
		return dat;
	}

	public static String getDateYYYYMMDDHHMMSS() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dat = sdf.format(d);
		return dat;
	}

	public static String getDateYYYYMMDDHHMMSSSLASH() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String dat = sdf.format(d);
		return dat;
	}

	public static String getDateYYYYMMDDSimple() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dat = sdf.format(d);
		return dat;
	}

	public static String getTime() {
		String l_Time = "";
		SimpleDateFormat l_DateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar l_Canlendar = Calendar.getInstance();
		l_Time = l_DateFormat.format(l_Canlendar.getTime());

		return l_Time;
	}
	
	public static String getTime2() {
		String l_Time = "";
		SimpleDateFormat l_DateFormat = new SimpleDateFormat("hh:00 a");
		Calendar l_Canlendar = Calendar.getInstance();
		l_Time = l_DateFormat.format(l_Canlendar.getTime());

		return l_Time;
	}

	public static boolean isWeekEnd(String pDate) { // Date Format yyyymmdd
		boolean ret = false;
		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day); // month start from 0
		if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
			ret = true;
		}
		return ret;
	}

	public static String mobiledateformat(String pdate) {
		// yyyymmdd to dd-mm-yyyy
		String l_date = "";
		l_date = pdate.substring(6, 8) + '-' + pdate.substring(4, 6) + '-' + pdate.substring(0, 4);

		return l_date;
	}

	public static String oracledatetoString() {
		String l_date = "";
		java.util.Date l_Date = new java.util.Date();
		SimpleDateFormat l_sdf = new SimpleDateFormat("yyyyMMdd");
		l_date = l_sdf.format(l_Date);

		return l_date;
	}

	public static String oraclelastday(int lastday) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// Date date = new Date();
		// String todate = dateFormat.format(date);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -lastday); // 2 or 5
		Date todate1 = cal.getTime();
		String fromdate = dateFormat.format(todate1);

		System.out.println("Date Format :" + fromdate);

		return fromdate;
	}

	public static String readAdImage() {
		String ret = "";
		try {
			apppath = System.getenv(ServerGlobal.getmEnvPath1());
			ret = apppath + "/ad/image/";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static ArrayList<String> readAdLink() {
		ArrayList<String> l_resultList = null;
		try {
			apppath = System.getenv(ServerGlobal.getmEnvPath1());
			l_resultList = FileUtil.readList(apppath + "/ad/link.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l_resultList;
	}

	public static void readCCDAPIServiceSetting() {
		String url = "", initVector = "", key = "", pUrl = "", urlPaidUnPaid = "", connectTimeout = "", readTimeout = "";
		ArrayList<String> ccdConnList = new ArrayList<String>();
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			ccdConnList = FileUtil.readList(apppath + "/CCD/CcdApiConncetionConfig.txt");
			if (ccdConnList.size() > 0) {
				// int i = 0;
				url = ccdConnList.get(0).split("GetURL:")[1];
				key = ccdConnList.get(2).split("KEY:")[1];
				initVector = ccdConnList.get(3).split("InitVector:")[1];
				pUrl = ccdConnList.get(11).split("GetPenaltyURL:")[1];
				urlPaidUnPaid = ccdConnList.get(12).split("CheckPaidOrUnPaid:")[1];
				connectTimeout = ccdConnList.get(13).split("ConnectTimeout:")[1];
				readTimeout = ccdConnList.get(14).split("ReadTimeout:")[1];
			}
			ServerGlobal.setmGetCCDURL(url.trim());
			ServerGlobal.setmGetCCDKEY(key.trim());
			ServerGlobal.setmGetCCDINITVECTOR(initVector.trim());
			ServerGlobal.setmGetCCDPURL(pUrl.trim());
			ServerGlobal.setmGetCCDPaidUnPaidURL(urlPaidUnPaid.trim());
			ServerGlobal.setmGetCCDConnectTimeout(connectTimeout.trim());
			ServerGlobal.setmGetCCDReadTimeout(readTimeout.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readCNPAPIServiceSetting() {
		String url1 = "", url2 = "", url3 = "", url4 = "", apiKey = "", duetime = "", dueday = "", bankRate = "";
		ArrayList<String> cnpConnList = new ArrayList<String>();
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			cnpConnList = FileUtil.readList(apppath + "/CNP/CnpApiConncetionConfig.txt");
			if (cnpConnList.size() > 0) {
				int i = 0;
				url1 = cnpConnList.get(i++).split("GetBillURL:")[1];
				url2 = cnpConnList.get(i++).split("MakePaymentURL:")[1];
				url3 = cnpConnList.get(i++).split("GetTxnURL:")[1];
				url4 = cnpConnList.get(i++).split("TopupURL:")[1]; // remove for
																	// uat
				apiKey = cnpConnList.get(i++).split("ApiKey:")[1];
				dueday = cnpConnList.get(i++).split("DueDay:")[1];
				duetime = cnpConnList.get(11).split("StartHour:")[1]; // 15
				bankRate = cnpConnList.get(13).split("BankRate:")[1]; // 15
			}
			ServerGlobal.setmGetBillURL(url1.trim());
			ServerGlobal.setmMakePaymentURL(url2.trim());
			ServerGlobal.setmGetTxnURL(url3.trim());
			ServerGlobal.setmTopupURL(url4.trim());
			ServerGlobal.setmAPIKey(apiKey.trim());
			ServerGlobal.setDueDay(dueday.trim());
			ServerGlobal.setDueTime(duetime.trim());
			ServerGlobal.setBankRate(bankRate.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readCNPDebugLogStatus() {
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			ArrayList<String> l_resultList = FileUtil.readList(apppath + "/CNP/debugconfig/Log.txt");
			if (l_resultList != null && l_resultList.size() > 0) {
				if (l_resultList.get(0).split(":")[1].trim().equals("Yes")) {
					ServerGlobal.setWriteLog(true);
				} else {
					ServerGlobal.setWriteLog(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void readDataConfig() {

		ArrayList<String> l_resultList = null;
		try {
			l_resultList = FileUtil.readList(DAOManager.AbsolutePath + "/data/DataConfig.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String string : l_resultList) {
			if (string.startsWith("FortuneYgnMerchant")) {
				ServerGlobal.setFortuneYgnMerchant(string.split(ServerGlobal.getSeparator())[1]);
			}
			if (string.startsWith("FortuneMdyMerchant")) {
				ServerGlobal.setFortuneMdyMerchant(string.split(ServerGlobal.getSeparator())[1]);
			}

		}

	}

	// suwai
	public static void readDebugLogStatus() {
		try {

			apppath = System.getenv(ServerGlobal.getmEnvPath2());
			ArrayList<String> l_resultList = FileUtil.readList(apppath + "/CBSAPI/debugconfig/Log.txt");

			if (l_resultList != null && l_resultList.size() > 0) {
				System.out.println(l_resultList.get(0).split(":")[1]);
				if (l_resultList.get(0).split(":")[1].trim().equals("Yes")) {
					ServerGlobal.setWriteLog(true);
				} else {
					ServerGlobal.setWriteLog(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void readEPIXWSDebugLogStatus() {
		try {

			apppath = System.getenv(ServerGlobal.getmEnvPath2());
			ArrayList<String> l_resultList = FileUtil.readList(apppath + "/EPIXWS/debugconfig/Log.txt");

			if (l_resultList != null && l_resultList.size() > 0) {
				System.out.println(l_resultList.get(0).split(":")[1]);
				if (l_resultList.get(0).split(":")[1].trim().equals("Yes")) {
					ServerGlobal.setWriteLog(true);
				} else {
					ServerGlobal.setWriteLog(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void readFCACServiceSetting() {

		String mPath = System.getenv(ServerGlobal.getmEnvPath1());
		ArrayList<String> l_resultList = null;
		try {
			l_resultList = FileUtil.readList(mPath + "/data/ServiceSetting.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get Service Path URL
		for (String string : l_resultList) {
			if (string.startsWith("AC__")) {
				String url = string.split(ServerGlobal.getFCSeparator())[1];
				ServerGlobal.setmFCACServiceURL(url);
			}
		}
	}

	public static void readFCCUServiceSetting() {

		String mPath = System.getenv(ServerGlobal.getmEnvPath1());
		ArrayList<String> l_resultList = null;
		try {
			l_resultList = FileUtil.readList(mPath + "/data/ServiceSetting.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get Service Path URL
		for (String string : l_resultList) {
			if (string.startsWith("CU")) {
				String url = string.split(ServerGlobal.getFCSeparator())[1];
				ServerGlobal.setmFCCUServiceURL(url);
			}
		}
	}

	public static void readFCServiceSetting() {
		String mPath = System.getenv(ServerGlobal.getmEnvPath1());
		ArrayList<String> l_resultList = null;
		try {
			l_resultList = FileUtil.readList(mPath + "/data/ServiceSetting.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get Service Path URL
		for (String string : l_resultList) {
			if (string.startsWith("RT")) {
				String url = string.split(ServerGlobal.getFCSeparator())[1];
				String makerID = string.split(ServerGlobal.getFCSeparator())[2];
				String checkerID = string.split(ServerGlobal.getFCSeparator())[3];
				ServerGlobal.setFCRTServiceURL(url);
				ServerGlobal.setFCMakerID(makerID);
				ServerGlobal.setFCCheckerID(checkerID);
			}
			if (string.substring(0, 2).equalsIgnoreCase("AC")) {
				String url = string.split(ServerGlobal.getFCSeparator())[1];
				String makerID = string.split(ServerGlobal.getFCSeparator())[2];
				String checkerID = string.split(ServerGlobal.getFCSeparator())[3];
				ServerGlobal.setmFCACServiceURL(url);
				ServerGlobal.setFCMakerID(makerID);
				ServerGlobal.setFCCheckerID(checkerID);
			}
			if (string.startsWith("PC")) {
				String url = string.split(ServerGlobal.getFCSeparator())[1];
				String makerID = string.split(ServerGlobal.getFCSeparator())[2];
				String checkerID = string.split(ServerGlobal.getFCSeparator())[3];
				ServerGlobal.setmFCPCServiceURL(url);
				ServerGlobal.setFCMakerID(makerID);
				ServerGlobal.setFCCheckerID(checkerID);
			}
		}
	}

	public static void readMptAPIServiceSetting() {
		String url = "", aspID = "", serviceID = "", password = "";
		String timeStamp = "";
		String balanceType = "";
		String period = "";
		ArrayList<String> nnConnList = new ArrayList<String>();
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			nnConnList = FileUtil.readList(apppath + "/MPT/MptApiConncetionConfig.txt");
			if (nnConnList.size() > 0) {
				int i = 0;
				url = nnConnList.get(i++).split("GetURL:")[1];
				aspID = nnConnList.get(i++).split("AspID:")[1];
				serviceID = nnConnList.get(i++).split("ServiceID:")[1];
				password = nnConnList.get(i++).split("Password:")[1];
				balanceType = nnConnList.get(i++).split("BalanceType:")[1];
				timeStamp = nnConnList.get(i++).split("TimeStamp:")[1];
				period = nnConnList.get(i++).split("Period:")[1];
			}
			ServerGlobal.setMPTUrl(url.trim());
			ServerGlobal.setMPTASPID(aspID.trim());
			ServerGlobal.setMPTServiceID(serviceID.trim());
			ServerGlobal.setMPTPassword(password.trim());
			ServerGlobal.setMPTBalanceType(balanceType.trim());
			ServerGlobal.setMPTTimeStamp(timeStamp.trim());
			ServerGlobal.setMPTPeriod(Integer.parseInt(period));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void readNowNowAPIServiceSetting() {

		String url = "", operatorUrl = "", user = "", password = "";
		ArrayList<String> nnConnList = new ArrayList<String>();
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			nnConnList = FileUtil.readList(apppath + "/NowNow/NowNowApiConncetionConfig.txt");
			if (nnConnList.size() > 0) {
				// int i = 0;
				url = nnConnList.get(0).split("GetURL:")[1];
				operatorUrl = nnConnList.get(1).split("GetURLOperator:")[1];
				user = nnConnList.get(2).split("UserName:")[1];
				password = nnConnList.get(3).split("Password:")[1];
			}
			ServerGlobal.setmGetNNUrl(url.trim());
			ServerGlobal.setmGetNNUserName(user.trim());
			ServerGlobal.setmGetNNPassword(password.trim());
			ServerGlobal.setmGetNNOperatorUrl(operatorUrl.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void readSkyPayAPIServiceSetting() {
		ArrayList<String> apiDataList = new ArrayList<String>();
		String RechargeUrl = "";
		String userName = "";
		String pass = "";
		String pop = "";
		String entid = "";
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			apiDataList = FileUtil.readList(apppath + "/SkyPay/SkypayApiConnConfig.txt");
			if (apiDataList.size() > 0) {
				RechargeUrl = apiDataList.get(0).split("RechargeURL:")[1];
				userName = apiDataList.get(4).split("USER:")[1];
				pass = apiDataList.get(5).split("PASS:")[1];
				pop = apiDataList.get(6).split("POS:")[1];
				entid = apiDataList.get(7).split("ENTID:")[1];
			}

			ServerGlobal.setmRechargeUrl(RechargeUrl);
			ServerGlobal.setMuserName(userName);
			ServerGlobal.setMpass(pass);
			ServerGlobal.setMpop(pop);
			ServerGlobal.setMEntID(entid);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readSkyPayDebugLogStatus() {
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			ArrayList<String> l_resultList = FileUtil.readList(apppath + "/SkyPay/debugconfig/Log.txt");
			if (l_resultList != null && l_resultList.size() > 0) {
				if (l_resultList.get(0).split(":")[1].trim().equals("Yes")) {
					ServerGlobal.setWriteLog(true);
				} else {
					ServerGlobal.setWriteLog(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void readSMSDebugLogStatus() {
		try {

			apppath = System.getenv(ServerGlobal.getmEnvPath2());
			ArrayList<String> l_resultList = FileUtil.readList(apppath + "/SMS/debugconfig/Log.txt");

			if (l_resultList != null && l_resultList.size() > 0) {
				System.out.println(l_resultList.get(0).split(":")[1]);
				if (l_resultList.get(0).split(":")[1].trim().equals("Yes")) {
					ServerGlobal.setWriteLog(true);
				} else {
					ServerGlobal.setWriteLog(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void readWalletServiceSetting() {

		String mPath = System.getenv(ServerGlobal.getmEnvPath1());
		ArrayList<String> l_resultList = null;
		try {
			l_resultList = FileUtil.readList(mPath + "/data/ServiceSetting.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get Service Path URL
		for (String string : l_resultList) {
			if (string.startsWith("WL")) {
				String url = string.split(ServerGlobal.getFCSeparator())[1];
				ServerGlobal.setWalletURL(url);
			}
		}
	}

	public static void writeDebugLog(String syskey, String title, String description) {
		if (ServerGlobal.isWriteLog()) {
			ArrayList<String> l_err = new ArrayList<String>();
			l_err.add("Time : " + GeneralUtil.getTime());
			l_err.add("Login token : " + syskey);
			l_err.add(title + description);
			l_err.add("=============================================================================");
			GeneralUtil.writeLog(l_err, "/CASAPI/log/");
		}
	}
	
	

	public static boolean writeLog(ArrayList<String> pErrorList) {
		boolean l_result = false;

		try {
			String l_Date = datetoString();
			String l_Path = DAOManager.AbsolutePath + "log/Web";
			System.out.println(l_Path);
			File dir = new File(l_Path);
			dir.mkdirs();
			FileUtil.writeList(l_Path + "/" + l_Date + ".txt", pErrorList, true);
			l_result = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			l_result = false;
		}
		return l_result;

	}

	// suwai
	public static boolean writeLog(ArrayList<String> pErrorList, String logpath) {
		boolean l_result = false;

		try {
			String path = System.getenv(ServerGlobal.getmEnvPath2());
			String l_Date = datetoString();
			logpath = logpath.replace('\\','/');
			String l_Path = path + logpath;
			File dir = new File(l_Path);
			dir.mkdirs();
			FileUtil.writeList(l_Path + "/" + l_Date + ".txt", pErrorList, true);
			l_result = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			l_result = false;
		}
		return l_result;

	}

	public static void writeLog(String fileName, String msgType, String msgData) {
		try {
			String cpath = System.getenv(ServerGlobal.getmEnvPath2());
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new FileWriter(cpath + "/SMS/log/" + getDateYYYYMMDD() + "_" + fileName + ".txt", true)));
			writer.println(getDateTime() + "\t:\t" + msgType + " : " + msgData);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean writeLogWebService(ArrayList<String> pErrorList, String logpath) {
		boolean l_result = false;

		try {
			String path = System.getenv(ServerGlobal.getmEnvPath2());
			String l_Date = datetoString();
			logpath = logpath.replace('\\','/');
			String l_Path = path + logpath;
			File dir = new File(l_Path);
			dir.mkdirs();
			FileUtil.writeList(l_Path + "/" + l_Date + ".txt", pErrorList, true);
			l_result = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			l_result = false;
		}
		return l_result;

	}

	public static void readTaxAPIServiceSetting() {

		String url1 = "", url2 = "", url3 = "", url4 = "", userID = "";
		ArrayList<String> taxConnList = new ArrayList<String>();
		try {
			String apppath = System.getenv(ServerGlobal.getmEnvPath2());
			taxConnList = FileUtil.readList(apppath + "/TAX/TaxApiConncetionConfig.txt");
			if (taxConnList.size() > 0) {
				int i = 0;
				url1 = taxConnList.get(i++).split("GetURL:")[1];
				url2 = taxConnList.get(i++).split("GetNotifyURL:")[1];
			}
			ServerGlobal.setTaxUrl(url1.trim());
			ServerGlobal.setTaxNotifyUrl(url2.trim());
			ServerGlobal.setTaxUserID(userID.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public static String getMonthString(String m) {
		String month = "";
		if (m.equals("1") || m.equals("01")) {
			month = "Jan";
		} else if (m.equals("2") || m.equals("02")) {
			month = "Feb";
		} else if (m.equals("3") || m.equals("03")) {
			month = "Mar";
		} else if (m.equals("4") || m.equals("04")) {
			month = "Apr";
		} else if (m.equals("5") || m.equals("05")) {
			month = "May";
		} else if (m.equals("6") || m.equals("06")) {
			month = "Jun";
		} else if (m.equals("7") || m.equals("07")) {
			month = "Jul";
		} else if (m.equals("8") || m.equals("08")) {
			month = "Aug";
		} else if (m.equals("9") || m.equals("09")) {
			month = "Sep";
		} else if (m.equals("10")) {
			month = "Oct";
		} else if (m.equals("11")) {
			month = "Nov";
		} else if (m.equals("12")) {
			month = "Dec";
		}

		return month;
	}

	public static String formatNumberwithComma(double pAmount) {
		String l_result = "0.00";
		DecimalFormat l_df = new DecimalFormat("#,##0.00");
		l_result = l_df.format(pAmount);
		return l_result;
	}

	public static void writeDebugLogs(String filePath, ArrayList<String> logDetailList) {
		if (ServerGlobal.isWriteLog()) {
			ArrayList<String> l_err = new ArrayList<String>();
			l_err.add("");
			l_err.add("===================================================================================");
			for (String eachLine : logDetailList) {
				l_err.add(eachLine);
			}
			l_err.add("===================================================================================");
			l_err.add("");
			GeneralUtil.writeLog(l_err, filePath);
		}
	}

	public static boolean readFirebaseSetting() {
		boolean readConfig = false;

		try {
			String fcmUrl = "", mode = "", fcmServerApiKey = "", fcmTopicName = "";

			String cmsServicePath = System.getenv(ServerGlobal.getmEnvPath2());

			ArrayList<String> firebaseConfigList = new ArrayList<String>();
			firebaseConfigList = FileUtil.readList(cmsServicePath + "/Firebase/FirebaseConfig.txt");

			if (firebaseConfigList.size() > 0) {
				mode = firebaseConfigList.get(7).split("MODE:")[1];

				if (mode != null && mode.trim().equals("TQM")) {
					fcmServerApiKey = firebaseConfigList.get(5).split("FCM_SERVER_API_KEY_TQM:")[1];
					fcmTopicName = firebaseConfigList.get(6).split("FCM_TOPIC_NAME_TQM:")[1];
				} else if (mode != null && mode.trim().equals("UAT")) {
					fcmServerApiKey = firebaseConfigList.get(3).split("FCM_SERVER_API_KEY_UAT:")[1];
					fcmTopicName = firebaseConfigList.get(4).split("FCM_TOPIC_NAME_UAT:")[1];
				} else if (mode != null && mode.trim().equals("PROD")) {
					fcmServerApiKey = firebaseConfigList.get(1).split("FCM_SERVER_API_KEY_PROD:")[1];
					fcmTopicName = firebaseConfigList.get(2).split("FCM_TOPIC_NAME_PROD:")[1];
				}

				if (fcmServerApiKey != null && !fcmServerApiKey.trim().equals("") 
						&& fcmTopicName != null && !fcmTopicName.trim().equals("")) {
					fcmUrl = firebaseConfigList.get(0).split("FCM_URL:")[1];

					ServerGlobal.setFcmMode(mode);
					ServerGlobal.setFcmUrl(fcmUrl);
					ServerGlobal.setFcmServerApiKey(fcmServerApiKey);
					ServerGlobal.setFcmTopicName(fcmTopicName);

					readConfig = true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			GeneralUtil.readDebugLogStatus();
			if (ServerGlobal.isWriteLog()) {
				ArrayList<String> l_err = new ArrayList<String>();
				l_err.add("");
				l_err.add("=============================================================================");
				l_err.add("Time : " + GeneralUtil.getTime());
				l_err.add("Read Firebase Config : " + ex.getMessage());
				l_err.add("-----------------------------------------------------------------------------");
				GeneralUtil.writeLog(l_err, "/Firebase/log/");
			}
		}

		return readConfig;
	}

	public static boolean readSkynetSetting() {
		boolean readConfig = false;

		try {
			String skynetTokenUrl = "";
			String skynetInstallItemUrl = "";
			String skynetSubscriptionUrl = "";
			String skynetVoucherUrl = "";
			String skynetTopupUrl = "";
			String skynetUsername = "";
			String skynetPassword = "";
			String skynetOrganisation = "";
			String skynetMinuteBeforeExpiry = "";
			String skynetCatalogListUrl = "";
			String skynetAvaliablePPVUrl = "";
			String skynetAddPPVUrl = "";
			String skynetMovieList = "";	//zzm

			String cmsServicePath = System.getenv(ServerGlobal.getmEnvPath2());

			ArrayList<String> skynetConfigList = new ArrayList<String>();
			skynetConfigList = FileUtil.readList(cmsServicePath + "/SkynetPayment/SkynetConfig.txt");

			if (skynetConfigList.size() > 0) {
				skynetTokenUrl = skynetConfigList.get(0).split("SKYNET_TOKEN_POST_URL:")[1];
				skynetInstallItemUrl = skynetConfigList.get(1).split("SKYNET_INSTALL_ITEM_GET_URL:")[1];
				skynetSubscriptionUrl = skynetConfigList.get(2).split("SKYNET_SUBSCRIPTION_GET_URL:")[1];
				skynetVoucherUrl = skynetConfigList.get(3).split("SKYNET_VOUCHER_GET_URL:")[1];
				skynetTopupUrl = skynetConfigList.get(4).split("SKYNET_TOPUP_POST_URL:")[1];
				skynetUsername = skynetConfigList.get(5).split("USERNAME:")[1];
				skynetPassword = skynetConfigList.get(6).split("PASSWORD:")[1];
				skynetOrganisation = skynetConfigList.get(7).split("ORGANISATION:")[1];
				skynetMinuteBeforeExpiry = skynetConfigList.get(8).split("MINUTE_BEFORE_EXPIRY:")[1];
				skynetCatalogListUrl = skynetConfigList.get(9).split("SKYNET_CATALOG_LIST_GET_URL:")[1];
				skynetAvaliablePPVUrl = skynetConfigList.get(10).split("SKYNET_AVALIABLEPPV_GET_URL:")[1];
				skynetAddPPVUrl = skynetConfigList.get(11).split("SKYNET_ADDPPV_POST_URL:")[1];
				skynetMovieList=skynetConfigList.get(12).split("MOVIE:")[1];		//zzm

				if (skynetTokenUrl != null && !skynetTokenUrl.trim().equals("") 
						&& skynetInstallItemUrl != null && !skynetInstallItemUrl.trim().equals("")
						&& skynetSubscriptionUrl != null && !skynetSubscriptionUrl.trim().equals("")
						&& skynetVoucherUrl != null && !skynetVoucherUrl.trim().equals("")
						&& skynetTopupUrl != null && !skynetTopupUrl.trim().equals("")
						&& skynetUsername != null && !skynetUsername.trim().equals("")
						&& skynetPassword != null && !skynetPassword.trim().equals("")
						&& skynetOrganisation != null && !skynetOrganisation.trim().equals("")
						&& skynetMinuteBeforeExpiry != null && !skynetMinuteBeforeExpiry.trim().equals("")
						&& skynetCatalogListUrl != null && !skynetCatalogListUrl.trim().equals("")
						&& skynetAvaliablePPVUrl != null && !skynetAvaliablePPVUrl.trim().equals("")
						&& skynetAddPPVUrl != null && !skynetAddPPVUrl.trim().equals("")
						&& skynetMovieList != null && !skynetMovieList.trim().equals("")) {
					ServerGlobal.setSkynetTokenUrl(skynetTokenUrl);
					ServerGlobal.setSkynetInstallItemUrl(skynetInstallItemUrl);
					ServerGlobal.setSkynetSubscriptionUrl(skynetSubscriptionUrl);
					ServerGlobal.setSkynetVoucherUrl(skynetVoucherUrl);
					ServerGlobal.setSkynetTopupUrl(skynetTopupUrl);
					ServerGlobal.setSkynetUsername(skynetUsername);
					ServerGlobal.setSkynetPassword(skynetPassword);
					ServerGlobal.setSkynetOrganisation(skynetOrganisation);
					ServerGlobal.setSkynetMinuteBeforeExpiry(skynetMinuteBeforeExpiry);
					ServerGlobal.setSkynetCatalogListUrl(skynetCatalogListUrl);
					ServerGlobal.setSkynetAvaliablePPVUrl(skynetAvaliablePPVUrl);
					ServerGlobal.setSkynetAddPPVUrl(skynetAddPPVUrl);
					ServerGlobal.setSkynetMovie(skynetMovieList);
					readConfig = true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			GeneralUtil.readDebugLogStatus();
			if (ServerGlobal.isWriteLog()) {
				ArrayList<String> l_err = new ArrayList<String>();
				l_err.add("");
				l_err.add("=============================================================================");
				l_err.add("Time : " + GeneralUtil.getTime());
				l_err.add("Read Skynet Config : " + ex.getMessage());
				l_err.add("-----------------------------------------------------------------------------");
				GeneralUtil.writeLog(l_err, "/SkynetPayment/log/");
			}
		}

		return readConfig;
	}

	public static String getNowDateTimePlusMillisecond(long millisecond) {
		Date nowDateTime = new Date();
		long timeResult = nowDateTime.getTime() + millisecond;
		Date dateResult = new Date(timeResult);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String dateString = dateFormat.format(dateResult);
		return dateString;
	}
	
	public static boolean readMPSSSetting() {
		boolean readMPSSConfig = false;

		try {
			String MPSS_TPUrl = "";
			String MPSSUsername = "";
			String MPSSPassword = "";
			String MPSSOrganisation = "";
			String MPSSAPIKey = "";
			String MPSS_DPUrl = "";
			String MPSS_EGC_Url = "";
			String MPSSEGCAPIKey = "";
			String MPSSEGCUserRef = "";

			String cmsServicePath = System.getenv(ServerGlobal.getmEnvPath2());

			ArrayList<String> skynetConfigList = new ArrayList<String>();
			skynetConfigList = FileUtil.readList(cmsServicePath + "//MPSS//Config.txt");

			if (skynetConfigList.size() > 0) {
				MPSS_TPUrl = skynetConfigList.get(0).split("MPSS_TOPUP_URL:")[1];
				MPSSUsername = skynetConfigList.get(1).split("USERNAME:")[1];
				MPSSPassword = skynetConfigList.get(2).split("PASSWORD:")[1];
				MPSSOrganisation = skynetConfigList.get(3).split("ORGANISATION:")[1];
				MPSSAPIKey = skynetConfigList.get(4).split("MPSS_APIKEY:")[1];
				MPSS_DPUrl = skynetConfigList.get(5).split("MPSS_DATAPACKAGE_URL:")[1];
				MPSS_EGC_Url = skynetConfigList.get(6).split("EGC_URL:")[1];
				MPSSEGCAPIKey = skynetConfigList.get(7).split("EGC_APIKEY:")[1];
				MPSSEGCUserRef = skynetConfigList.get(8).split("EGC_USER_REF:")[1];

				if (MPSS_TPUrl != null && !MPSS_TPUrl.trim().equals("") 
					&& MPSSUsername != null && !MPSSUsername.trim().equals("")
					&& MPSSPassword != null && !MPSSPassword.trim().equals("")
					&& MPSSOrganisation != null && !MPSSOrganisation.trim().equals("")
					&& MPSSAPIKey != null && !MPSSAPIKey.trim().equals("")
					&& MPSS_DPUrl != null && !MPSS_DPUrl.trim().equals("") && MPSS_EGC_Url != null && !MPSS_EGC_Url.trim().equals("")
					&& MPSSEGCAPIKey != null && !MPSSEGCAPIKey.trim().equals("") && MPSSEGCUserRef != null && !MPSSEGCUserRef.trim().equals("")) {
					ServerGlobal.setMpssTPUrl(MPSS_TPUrl);
					ServerGlobal.setMpssUsername(MPSSUsername);
					ServerGlobal.setMpssPassword(MPSSPassword);
					ServerGlobal.setMpssOrganisation(MPSSOrganisation);
					ServerGlobal.setMpssAPIKey(MPSSAPIKey);
					ServerGlobal.setMpssDPUrl(MPSS_DPUrl);
					ServerGlobal.setMpssEGCUrl(MPSS_EGC_Url);
					ServerGlobal.setMpssEGCAPIKey(MPSSEGCAPIKey);
					ServerGlobal.setMpssEGCUserRef(MPSSEGCUserRef);
					readMPSSConfig = true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			GeneralUtil.readDebugLogStatus();
			if (ServerGlobal.isWriteLog()) {
				ArrayList<String> l_err = new ArrayList<String>();
				l_err.add("");
				l_err.add("=============================================================================");
				l_err.add("Time : " + GeneralUtil.getTime());
				l_err.add("Read MPSS Config : " + ex.getMessage());
				l_err.add("-----------------------------------------------------------------------------");
				GeneralUtil.writeLog(l_err, "//MPSS//log//");
			}
		}

		return readMPSSConfig;
	}
	
	public static String dateDiff(String date1, String date2){
		
		String time= "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime dateTime1= LocalDateTime.parse(date1, formatter);
		LocalDateTime dateTime2= LocalDateTime.parse(date2, formatter);

		long diffInMilli = java.time.Duration.between(dateTime1, dateTime2).toMillis();
		//long diffInSeconds = java.time.Duration.between(dateTime1, dateTime2).getSeconds();
		//long diffInMinutes = java.time.Duration.between(dateTime1, dateTime2).toMinutes();
		//long diffInHour = java.time.Duration.between(dateTime1, dateTime2).toHours();
		//long diffInDay = java.time.Duration.between(dateTime1, dateTime2).toDays();
		
		long seconds = diffInMilli / 1000 % 60;
		long minues = diffInMilli / (60 * 1000) % 60;
		long hours = diffInMilli / (60 * 60 * 1000) % 24;
		long days = diffInMilli / (24 * 60 * 60 * 1000);
		time = String.format("%02d",hours)+":"+String.format("%02d",minues)+":"+String.format("%02d",seconds);
		
		return time;
	}
	
	public static void readBrCodeLength() throws SQLException {
		//BranchMgr dbmgr = new BranchMgr();
		CMSModuleConfigData data = new CMSModuleConfigData();
		//data = dbmgr.getCMSModuleConfig();
		ServerGlobal.setmBrCodeStart(data.getBranchCodeStart());
		ServerGlobal.setmBrCodeEnd(data.getBranchCodeLength());
		ServerGlobal.setmXREFPrefix(data.getXREFPrefix());
		ServerGlobal.setBankName(data.getBankName());
	}
	
	public static depositaccdata[] removeArray(depositaccdata[] arr, int index) {
		List<depositaccdata> tempList = new ArrayList<depositaccdata>(Arrays.asList(arr));
		tempList.remove(index);
		return tempList.toArray(new depositaccdata[0]);
	}
	
	public static String minusDayFromDate(String date){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDate = LocalDate.parse(date, formatter);
	    LocalDate yesterday = localDate.minusDays(1);
	    
	    return yesterday.toString();
	}
	
	public static String formatDBDate2MIT(String p) {
		return formatYYYYMMDD2MIT(p); // <<<<<<<<<<<<<<<<<<<<<<<<<<<< COFIG
	}
	
	public static String formatYYYYMMDD2MIT(String p) {
		String ret = "";
		try {
			if (p.length() >= 10) {
				ret = p.replaceAll("-", "").substring(0, 8);
			} else {
				ret = "19000101";
			}

		} catch (Exception exp) {
		}
		return ret;
	}
	
	public static String leadZeros(int p, int size) {
		return leadZeros("" + p, size);
	}

	public static String leadZeros(String p, int size) {
		String ret = p;
		for (int i = p.length(); i < size; i++) {
			ret = "0" + ret;
		}
		return ret;
	}
	
	public static String getCheckStatus(int p) {
		String ret = "";
		if (p == 0)
			ret = "Unpaid";
		else if (p == 1)
			ret = "Paid";
		else if (p == 2)
			ret = "Stopped";
		else if (p == 3)
			ret = "Cancel";
		return ret;
	}
	
	public static String calculateDuedate(String date1, int tenure,int datatenure, int mday) {
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			d1 = sdf.parse(date1);		
			int day = d1.getDate();
			int month = d1.getMonth();
			int year = d1.getYear(); 			
			int diffday = 0;
			Date l_newDate = d1;
			if (datatenure<=100){	//months and years
				//System.out.println("l_newDate:" + day + "/" + month + "/" + year);
				int resultMonthCount = year * 12 + month + datatenure;	
				int resultYear = resultMonthCount / 12;		     
				int resultMonth = resultMonthCount - resultYear * 12;		  
				if(resultYear%4==0){
				  l_newDate.setYear(resultYear);	
				  l_newDate.setMonth(resultMonth);	
				  if(day>29 && resultMonth==1)
					  diffday =  day-30;
				  //System.out.println("diffday:" + diffday);
				}else {
				  l_newDate.setMonth(resultMonth); 		    	  
				  l_newDate.setYear(resultYear);	
				  if(day>28 && resultMonth==1)
					  diffday =  day-29;
				  //System.out.println("diffday:" + diffday);
				}		
				cal.setTime(l_newDate);
				//System.out.println(sdf.format(l_newDate));	
				cal.add(Calendar.DATE, -diffday-mday); //minus number would decrement the days
				//System.out.println(sdf.format(cal.getTime()));
			}else if (datatenure>100 && datatenure<=1000){ //days
				cal.setTime(l_newDate);
				//System.out.println(sdf.format(l_newDate));	
				diffday = tenure;
				cal.add(Calendar.DATE, diffday-mday); //minus number would decrement the days
				//System.out.println(sdf.format(cal.getTime()));
			}else{	//weeks
				cal.setTime(l_newDate);
				//System.out.println(sdf.format(l_newDate));	
				diffday = tenure*7;
				cal.add(Calendar.DATE, diffday-mday); //minus number would decrement the days
				//System.out.println(sdf.format(cal.getTime()));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}
	
	public static String changedateformat1(String pdate) {
		String formatted = "";
		// from  dd-MM-yyy TO yyyy-MM-dd
		try {
			Date parsed = new SimpleDateFormat("dd-MM-yyyy").parse(pdate);
			formatted = new SimpleDateFormat("yyyy-MM-dd").format(parsed);
			System.out.println(formatted);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatted;
	}
	
	public static String changedateyyyyMMddtoyyyyMMdddash(String pdate) {
		String formatted = "";
		// from  yyyyMMdd TO yyyy-MM-dd
		try {
			Date parsed = new SimpleDateFormat("yyyyMMdd").parse(pdate);
			formatted = new SimpleDateFormat("yyyy-MM-dd").format(parsed);
			System.out.println(formatted);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatted;
	}
	
	public static String changedateformat3(String pdate) {
		String formatted = "";
		// fro yyyy-MM-dd To yyyyMMdd
		try {
			Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(pdate);
			formatted = new SimpleDateFormat("yyyyMMdd").format(parsed);
			System.out.println(formatted);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatted;
	}
	
	public static String changedateformat2(String pdate) {
		String formatted = "";
		// from  yyyy-MM-dd TO yyyyMMdd
		try {
			Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(pdate);
			formatted = new SimpleDateFormat("dd-MM-yyyy").format(parsed);
			System.out.println(formatted);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatted;
	}
	
	public static String toGson(Object req){
		Gson gson = new Gson();
		//System.out.println(gson.toJson(req));
		String msg = gson.toJson(req);
	    return 	msg;
	}
	
	public static String getCurrentDateYYYYMMDDHHMMSS(){
	    String pattern = "yyyy-MM-dd HH:mm:ss";
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    return format.format(new Date());
	}
	
}
