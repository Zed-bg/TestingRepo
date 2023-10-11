package com.nirvasoft.rp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.shared.CutOffTimeData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.icbs.RateAmountRangeSetupData;
public class GeneralUtility {
	public static String readDate = "";

	public static String path = System.getenv(ServerGlobal.getmEnvPath1());//ConnAdmin.readEnvConfig("envName1", "")

	// Please re add redpanda.jar because of Accounts And Accounts_Balance Table
	// MWD
	private static String mVersionNo = "3.0.88.37";// For Remark For Card
													// Transaction - APH

	public static double calculateIBTCharges(double pAmount, double pCharges, double pPerAmount) {
		double l_Fee = 0;
		int l_multiple = 1;

		if (pPerAmount > 0) {
			while (pAmount > pPerAmount) {
				l_multiple += 1;
				pAmount = pAmount - pPerAmount;
			}
		}

		l_Fee = l_multiple * pCharges;

		return l_Fee;
	}

	

	// HMT 11/05/2012
	public static double calculatePercentage(double pAmount, double pRate) {
		double l_Result = 0;
		l_Result = (pAmount * pRate) / 100;
		return roundTo2Decimals(l_Result);
	}

	public static double calculateRateAmount(double minamt, double rate, double txnamt, double maxamt) {
		double chargeamtrate = 0.0;
		chargeamtrate = txnamt * (rate / 100);
		if (chargeamtrate < minamt) {
			return minamt;
		} else if (chargeamtrate > maxamt) {
			return maxamt;
		} else {
			return chargeamtrate;
		}
	}

	

	public static DataResult checkTransactionAccounts(ArrayList<TransactionData> pTransactionList) {
		DataResult ret = new DataResult();
		ret.setStatus(true);
		for (int i = 0; i < pTransactionList.size(); i++) {
			if (pTransactionList.get(i).getAccountNumber().equals("")) {
				ret.setStatus(false);
				ret.setDescription("INVALID ACCOUNT NUMBER");
				ret.setErrorFlag(1);
				ret.setCode("50");
			}
			if (ret.getStatus() == false) {
				break;
			}
		}
		return ret;
	}
	

	public static String datetimeformatHHMM2() throws Exception {
		String l_Pattern = "HH:mm";
		String ret = "";
		Date date = new Date();
		String todayDate = new SimpleDateFormat("HH:mm").format(date);
		Date l_Date = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(todayDate);
		Date l_CutOfTimeDate = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(readDate);

		if ((l_CutOfTimeDate.getTime() >= l_Date.getTime())) {
			System.out.println("It is not Cut Of Time");
			ret = "false";
		} else {
			System.out.println("It is Cut of Time");
			ret = "true";
		}
		return ret;
	}

	public static String formatMIT2DateStr(String p) {
		return formatMIT2DDMMYYYY(p); // <<<<<<<<<<<<<<<<<<<<<<<<<<<< COFIG
	}

	public static String formatMIT2DDMMYYYY(String p) {
		String ret = "";
		try {
			int y = Integer.parseInt(p.substring(0, 4));
			int m = Integer.parseInt(p.substring(4, 6));
			int d = Integer.parseInt(p.substring(6, 8));
			ret = d + "/" + m + "/" + y;
			ret = p.substring(6, 8) + "/" + p.substring(4, 6) + "/" + p.substring(0, 4);
		} catch (Exception ex) {
		}
		return ret;
	}

	public static String formatMIT2DDMMYYYYHHMMSS(String p) {
		String ret = "";
		try {
			// int y = Integer.parseInt( p.substring(0, 4) );
			// System.out.println(y);
			// int m = Integer.parseInt( p.substring(5, 7) );
			// System.out.println(m);
			// int d = Integer.parseInt( p.substring(8, 10) );
			// System.out.println(d);
			// ret = d + "/" + m + "/" + y ;
			ret = p.substring(8, 10) + "/" + p.substring(5, 7) + "/" + p.substring(0, 4) + p.substring(10, p.length());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	public static String fromatDDMMYY2MIT(String aDate) {
		// aDate == 01/10/2011
		String[] l_date = aDate.split("/");
		int day = Integer.parseInt(l_date[0]);
		int month = Integer.parseInt(l_date[1]);
		int year = Integer.parseInt(l_date[2]);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);

		return dateFormat.format(c.getTime());
	}

	public static String getAbsolutePath(Object a) {

		// Getting File Path For ConnectionConfig
		String name = a.getClass().getName().replace('.', '/');
		String s = a.getClass().getResource("/" + name + ".class").toString();
		s = s.replace('/', File.separatorChar);

		int fastindex = s.indexOf("\\");
		int lastindex = s.lastIndexOf('!');
		String path = s.substring(fastindex + 1, lastindex + 1);
		System.out.println("path : " + path);
		lastindex = path.lastIndexOf("\\");
		System.out.println("lastIndex : " + lastindex);
		// path = path.substring(0, lastindex + 1);
		path = path.substring(0, lastindex - 3);
		System.out.println("path : " + path);
		return path;
	}



	public static String getAmountType(double pAmount) {
		String ret = "";
		if (pAmount > 0)
			ret = "Credit";
		else
			ret = "Debit";
		return ret;
	}

	public static String getCurrencyCode(String pCurrencyNo) {
		String l_CurrencyCode = "";

		if (pCurrencyNo.equals("008")) {
			l_CurrencyCode = "ALL";
		} else if (pCurrencyNo.equals("012")) {
			l_CurrencyCode = "DZD";
		} else if (pCurrencyNo.equals("032")) {
			l_CurrencyCode = "ARS";
		} else if (pCurrencyNo.equals("036")) {
			l_CurrencyCode = "AUD";
		} else if (pCurrencyNo.equals("044")) {
			l_CurrencyCode = "BSD";
		} else if (pCurrencyNo.equals("048")) {
			l_CurrencyCode = "BHD";
		} else if (pCurrencyNo.equals("050")) {
			l_CurrencyCode = "BDT";
		} else if (pCurrencyNo.equals("051")) {
			l_CurrencyCode = "AMD";
		} else if (pCurrencyNo.equals("052")) {
			l_CurrencyCode = "BBD";
		} else if (pCurrencyNo.equals("060")) {
			l_CurrencyCode = "BMD";
		} else if (pCurrencyNo.equals("064")) {
			l_CurrencyCode = "BTN";
		} else if (pCurrencyNo.equals("068")) {
			l_CurrencyCode = "BOB";
		} else if (pCurrencyNo.equals("072")) {
			l_CurrencyCode = "BWP";
		} else if (pCurrencyNo.equals("084")) {
			l_CurrencyCode = "BZD";
		} else if (pCurrencyNo.equals("090")) {
			l_CurrencyCode = "SBD";
		} else if (pCurrencyNo.equals("096")) {
			l_CurrencyCode = "BND";
		} else if (pCurrencyNo.equals("104")) {
			l_CurrencyCode = "MMK";
		} else if (pCurrencyNo.equals("108")) {
			l_CurrencyCode = "BIF";
		} else if (pCurrencyNo.equals("116")) {
			l_CurrencyCode = "KHR";
		} else if (pCurrencyNo.equals("124")) {
			l_CurrencyCode = "CAD";
		} else if (pCurrencyNo.equals("132")) {
			l_CurrencyCode = "CVE";
		} else if (pCurrencyNo.equals("136")) {
			l_CurrencyCode = "KYD";
		} else if (pCurrencyNo.equals("144")) {
			l_CurrencyCode = "LKR";
		} else if (pCurrencyNo.equals("152")) {
			l_CurrencyCode = "CLP";
		} else if (pCurrencyNo.equals("156")) {
			l_CurrencyCode = "CNY";
		} else if (pCurrencyNo.equals("170")) {
			l_CurrencyCode = "COP";
		} else if (pCurrencyNo.equals("174")) {
			l_CurrencyCode = "KMF";
		} else if (pCurrencyNo.equals("188")) {
			l_CurrencyCode = "CRC";
		} else if (pCurrencyNo.equals("191")) {
			l_CurrencyCode = "HRK";
		} else if (pCurrencyNo.equals("192")) {
			l_CurrencyCode = "CUP";
		} else if (pCurrencyNo.equals("203")) {
			l_CurrencyCode = "CZK";
		} else if (pCurrencyNo.equals("208")) {
			l_CurrencyCode = "DKK";
		} else if (pCurrencyNo.equals("214")) {
			l_CurrencyCode = "DOP";
		} else if (pCurrencyNo.equals("230")) {
			l_CurrencyCode = "ETB";
		} else if (pCurrencyNo.equals("232")) {
			l_CurrencyCode = "ERN";
		} else if (pCurrencyNo.equals("238")) {
			l_CurrencyCode = "FKP";
		} else if (pCurrencyNo.equals("242")) {
			l_CurrencyCode = "FJD";
		} else if (pCurrencyNo.equals("262")) {
			l_CurrencyCode = "DJF";
		} else if (pCurrencyNo.equals("270")) {
			l_CurrencyCode = "GMD";
		} else if (pCurrencyNo.equals("292")) {
			l_CurrencyCode = "GIP";
		} else if (pCurrencyNo.equals("320")) {
			l_CurrencyCode = "GTQ";
		} else if (pCurrencyNo.equals("324")) {
			l_CurrencyCode = "GNF";
		} else if (pCurrencyNo.equals("328")) {
			l_CurrencyCode = "GYD";
		} else if (pCurrencyNo.equals("332")) {
			l_CurrencyCode = "HTG";
		} else if (pCurrencyNo.equals("340")) {
			l_CurrencyCode = "HNL";
		} else if (pCurrencyNo.equals("344")) {
			l_CurrencyCode = "HKD";
		} else if (pCurrencyNo.equals("348")) {
			l_CurrencyCode = "HUF";
		} else if (pCurrencyNo.equals("352")) {
			l_CurrencyCode = "ISK";
		} else if (pCurrencyNo.equals("356")) {
			l_CurrencyCode = "INR";
		} else if (pCurrencyNo.equals("360")) {
			l_CurrencyCode = "IDR";
		} else if (pCurrencyNo.equals("364")) {
			l_CurrencyCode = "IRR";
		} else if (pCurrencyNo.equals("368")) {
			l_CurrencyCode = "IQD";
		} else if (pCurrencyNo.equals("376")) {
			l_CurrencyCode = "ILS";
		} else if (pCurrencyNo.equals("388")) {
			l_CurrencyCode = "JMD";
		} else if (pCurrencyNo.equals("392")) {
			l_CurrencyCode = "JPY";
		} else if (pCurrencyNo.equals("398")) {
			l_CurrencyCode = "KZT";
		} else if (pCurrencyNo.equals("400")) {
			l_CurrencyCode = "JOD";
		} else if (pCurrencyNo.equals("404")) {
			l_CurrencyCode = "KES";
		} else if (pCurrencyNo.equals("408")) {
			l_CurrencyCode = "KPW";
		} else if (pCurrencyNo.equals("410")) {
			l_CurrencyCode = "KRW";
		} else if (pCurrencyNo.equals("414")) {
			l_CurrencyCode = "KWD";
		} else if (pCurrencyNo.equals("417")) {
			l_CurrencyCode = "KGS";
		} else if (pCurrencyNo.equals("418")) {
			l_CurrencyCode = "LAK";
		} else if (pCurrencyNo.equals("422")) {
			l_CurrencyCode = "LBP";
		} else if (pCurrencyNo.equals("426")) {
			l_CurrencyCode = "LSL";
		} else if (pCurrencyNo.equals("428")) {
			l_CurrencyCode = "LVL";
		} else if (pCurrencyNo.equals("430")) {
			l_CurrencyCode = "LRD";
		} else if (pCurrencyNo.equals("434")) {
			l_CurrencyCode = "LYD";
		} else if (pCurrencyNo.equals("440")) {
			l_CurrencyCode = "LTL";
		} else if (pCurrencyNo.equals("446")) {
			l_CurrencyCode = "MOP";
		} else if (pCurrencyNo.equals("454")) {
			l_CurrencyCode = "MWK";
		} else if (pCurrencyNo.equals("458")) {
			l_CurrencyCode = "MYR";
		} else if (pCurrencyNo.equals("462")) {
			l_CurrencyCode = "MVR";
		} else if (pCurrencyNo.equals("478")) {
			l_CurrencyCode = "MRO";
		} else if (pCurrencyNo.equals("480")) {
			l_CurrencyCode = "MUR";
		} else if (pCurrencyNo.equals("484")) {
			l_CurrencyCode = "MXN";
		} else if (pCurrencyNo.equals("496")) {
			l_CurrencyCode = "MNT";
		} else if (pCurrencyNo.equals("498")) {
			l_CurrencyCode = "MDL";
		} else if (pCurrencyNo.equals("504")) {
			l_CurrencyCode = "MAD";
		} else if (pCurrencyNo.equals("512")) {
			l_CurrencyCode = "OMR";
		} else if (pCurrencyNo.equals("516")) {
			l_CurrencyCode = "NAD";
		} else if (pCurrencyNo.equals("524")) {
			l_CurrencyCode = "NPR";
		} else if (pCurrencyNo.equals("532")) {
			l_CurrencyCode = "ANG";
		} else if (pCurrencyNo.equals("533")) {
			l_CurrencyCode = "AWG";
		} else if (pCurrencyNo.equals("548")) {
			l_CurrencyCode = "VUV";
		} else if (pCurrencyNo.equals("554")) {
			l_CurrencyCode = "NZD";
		} else if (pCurrencyNo.equals("558")) {
			l_CurrencyCode = "NIO";
		} else if (pCurrencyNo.equals("566")) {
			l_CurrencyCode = "NGN";
		} else if (pCurrencyNo.equals("578")) {
			l_CurrencyCode = "NOK";
		} else if (pCurrencyNo.equals("586")) {
			l_CurrencyCode = "PKR";
		} else if (pCurrencyNo.equals("590")) {
			l_CurrencyCode = "PAB";
		} else if (pCurrencyNo.equals("598")) {
			l_CurrencyCode = "PGK";
		} else if (pCurrencyNo.equals("600")) {
			l_CurrencyCode = "PYG";
		} else if (pCurrencyNo.equals("604")) {
			l_CurrencyCode = "PEN";
		} else if (pCurrencyNo.equals("608")) {
			l_CurrencyCode = "PHP";
		} else if (pCurrencyNo.equals("634")) {
			l_CurrencyCode = "QAR";
		} else if (pCurrencyNo.equals("643")) {
			l_CurrencyCode = "RUB";
		} else if (pCurrencyNo.equals("646")) {
			l_CurrencyCode = "RWF";
		} else if (pCurrencyNo.equals("654")) {
			l_CurrencyCode = "SHP";
		} else if (pCurrencyNo.equals("678")) {
			l_CurrencyCode = "STD";
		} else if (pCurrencyNo.equals("682")) {
			l_CurrencyCode = "SAR";
		} else if (pCurrencyNo.equals("690")) {
			l_CurrencyCode = "SCR";
		} else if (pCurrencyNo.equals("694")) {
			l_CurrencyCode = "SLL";
		} else if (pCurrencyNo.equals("702")) {
			l_CurrencyCode = "SGD";
		} else if (pCurrencyNo.equals("704")) {
			l_CurrencyCode = "VND";
		} else if (pCurrencyNo.equals("706")) {
			l_CurrencyCode = "SOS";
		} else if (pCurrencyNo.equals("710")) {
			l_CurrencyCode = "ZAR";
		} else if (pCurrencyNo.equals("728")) {
			l_CurrencyCode = "SSP";
		} else if (pCurrencyNo.equals("748")) {
			l_CurrencyCode = "SZL";
		} else if (pCurrencyNo.equals("752")) {
			l_CurrencyCode = "SEK";
		} else if (pCurrencyNo.equals("756")) {
			l_CurrencyCode = "CHF";
		} else if (pCurrencyNo.equals("760")) {
			l_CurrencyCode = "SYP";
		} else if (pCurrencyNo.equals("764")) {
			l_CurrencyCode = "THB";
		} else if (pCurrencyNo.equals("776")) {
			l_CurrencyCode = "TOP";
		} else if (pCurrencyNo.equals("780")) {
			l_CurrencyCode = "TTD";
		} else if (pCurrencyNo.equals("784")) {
			l_CurrencyCode = "AED";
		} else if (pCurrencyNo.equals("788")) {
			l_CurrencyCode = "TND";
		} else if (pCurrencyNo.equals("800")) {
			l_CurrencyCode = "UGX";
		} else if (pCurrencyNo.equals("807")) {
			l_CurrencyCode = "MKD";
		} else if (pCurrencyNo.equals("818")) {
			l_CurrencyCode = "EGP";
		} else if (pCurrencyNo.equals("826")) {
			l_CurrencyCode = "GBP";
		} else if (pCurrencyNo.equals("834")) {
			l_CurrencyCode = "TZS";
		} else if (pCurrencyNo.equals("840")) {
			l_CurrencyCode = "USD";
		} else if (pCurrencyNo.equals("858")) {
			l_CurrencyCode = "UYU";
		} else if (pCurrencyNo.equals("860")) {
			l_CurrencyCode = "UZS";
		} else if (pCurrencyNo.equals("882")) {
			l_CurrencyCode = "WST";
		} else if (pCurrencyNo.equals("886")) {
			l_CurrencyCode = "YER";
		} else if (pCurrencyNo.equals("894")) {
			l_CurrencyCode = "ZMK";
		} else if (pCurrencyNo.equals("901")) {
			l_CurrencyCode = "TWD";
		} else if (pCurrencyNo.equals("931")) {
			l_CurrencyCode = "CUC";
		} else if (pCurrencyNo.equals("932")) {
			l_CurrencyCode = "ZWL";
		} else if (pCurrencyNo.equals("934")) {
			l_CurrencyCode = "TMT";
		} else if (pCurrencyNo.equals("936")) {
			l_CurrencyCode = "GHS";
		} else if (pCurrencyNo.equals("937")) {
			l_CurrencyCode = "VEF";
		} else if (pCurrencyNo.equals("938")) {
			l_CurrencyCode = "SDG";
		} else if (pCurrencyNo.equals("940")) {
			l_CurrencyCode = "UYI";
		} else if (pCurrencyNo.equals("941")) {
			l_CurrencyCode = "RSD";
		} else if (pCurrencyNo.equals("943")) {
			l_CurrencyCode = "MZN";
		} else if (pCurrencyNo.equals("944")) {
			l_CurrencyCode = "AZN";
		} else if (pCurrencyNo.equals("946")) {
			l_CurrencyCode = "RON";
		} else if (pCurrencyNo.equals("947")) {
			l_CurrencyCode = "CHE";
		} else if (pCurrencyNo.equals("948")) {
			l_CurrencyCode = "CHW";
		} else if (pCurrencyNo.equals("949")) {
			l_CurrencyCode = "TRY";
		} else if (pCurrencyNo.equals("950")) {
			l_CurrencyCode = "XAF";
		} else if (pCurrencyNo.equals("951")) {
			l_CurrencyCode = "XCD";
		} else if (pCurrencyNo.equals("952")) {
			l_CurrencyCode = "XOF";
		} else if (pCurrencyNo.equals("953")) {
			l_CurrencyCode = "XPF";
		} else if (pCurrencyNo.equals("955")) {
			l_CurrencyCode = "XBA";
		} else if (pCurrencyNo.equals("956")) {
			l_CurrencyCode = "XBB";
		} else if (pCurrencyNo.equals("957")) {
			l_CurrencyCode = "XBC";
		} else if (pCurrencyNo.equals("958")) {
			l_CurrencyCode = "XBD";
		} else if (pCurrencyNo.equals("959")) {
			l_CurrencyCode = "XAU";
		} else if (pCurrencyNo.equals("960")) {
			l_CurrencyCode = "XDR";
		} else if (pCurrencyNo.equals("961")) {
			l_CurrencyCode = "XAG";
		} else if (pCurrencyNo.equals("962")) {
			l_CurrencyCode = "XPT";
		} else if (pCurrencyNo.equals("963")) {
			l_CurrencyCode = "XTS";
		} else if (pCurrencyNo.equals("964")) {
			l_CurrencyCode = "XPD";
		} else if (pCurrencyNo.equals("968")) {
			l_CurrencyCode = "SRD";
		} else if (pCurrencyNo.equals("969")) {
			l_CurrencyCode = "MGA";
		} else if (pCurrencyNo.equals("970")) {
			l_CurrencyCode = "COU";
		} else if (pCurrencyNo.equals("971")) {
			l_CurrencyCode = "AFN";
		} else if (pCurrencyNo.equals("972")) {
			l_CurrencyCode = "TJS";
		} else if (pCurrencyNo.equals("973")) {
			l_CurrencyCode = "AOA";
		} else if (pCurrencyNo.equals("974")) {
			l_CurrencyCode = "BYR";
		} else if (pCurrencyNo.equals("975")) {
			l_CurrencyCode = "BGN";
		} else if (pCurrencyNo.equals("976")) {
			l_CurrencyCode = "CDF";
		} else if (pCurrencyNo.equals("977")) {
			l_CurrencyCode = "BAM";
		} else if (pCurrencyNo.equals("978")) {
			l_CurrencyCode = "EUR";
		} else if (pCurrencyNo.equals("979")) {
			l_CurrencyCode = "MXV";
		} else if (pCurrencyNo.equals("980")) {
			l_CurrencyCode = "UAH";
		} else if (pCurrencyNo.equals("981")) {
			l_CurrencyCode = "GEL";
		} else if (pCurrencyNo.equals("984")) {
			l_CurrencyCode = "BOV";
		} else if (pCurrencyNo.equals("985")) {
			l_CurrencyCode = "PLN";
		} else if (pCurrencyNo.equals("986")) {
			l_CurrencyCode = "BRL";
		} else if (pCurrencyNo.equals("990")) {
			l_CurrencyCode = "CLF";
		} else if (pCurrencyNo.equals("997")) {
			l_CurrencyCode = "USN";
		} else if (pCurrencyNo.equals("998")) {
			l_CurrencyCode = "USS";
		} else if (pCurrencyNo.equals("999")) {
			l_CurrencyCode = "XXX";
		} else if (pCurrencyNo.equals("Nil")) {
			l_CurrencyCode = "XFU";
		} else {
			l_CurrencyCode = "MMK";
		}
		return l_CurrencyCode;
	}

	public static String getCurrencyNumber(String pCurrencyCode) {
		String l_CurrencyNumber = "";

		if (pCurrencyCode.equals("ALL")) {
			l_CurrencyNumber = "008";
		} else if (pCurrencyCode.equals("DZD")) {
			l_CurrencyNumber = "012";
		} else if (pCurrencyCode.equals("ARS")) {
			l_CurrencyNumber = "032";
		} else if (pCurrencyCode.equals("AUD")) {
			l_CurrencyNumber = "036";
		} else if (pCurrencyCode.equals("BSD")) {
			l_CurrencyNumber = "044";
		} else if (pCurrencyCode.equals("BHD")) {
			l_CurrencyNumber = "048";
		}

		else if (pCurrencyCode.equals("BDT")) {
			l_CurrencyNumber = "050";
		} else if (pCurrencyCode.equals("AMD")) {
			l_CurrencyNumber = "051";
		} else if (pCurrencyCode.equals("BBD")) {
			l_CurrencyNumber = "052";
		} else if (pCurrencyCode.equals("BMD")) {
			l_CurrencyNumber = "060";
		} else if (pCurrencyCode.equals("BTN")) {
			l_CurrencyNumber = "064";
		} else if (pCurrencyCode.equals("BOB")) {
			l_CurrencyNumber = "068";
		} else if (pCurrencyCode.equals("BWP")) {
			l_CurrencyNumber = "072";
		} else if (pCurrencyCode.equals("BZD")) {
			l_CurrencyNumber = "084";
		} else if (pCurrencyCode.equals("SBD")) {
			l_CurrencyNumber = "090";
		} else if (pCurrencyCode.equals("BND")) {
			l_CurrencyNumber = "096";
		} else if (pCurrencyCode.equals("MMK")) {
			l_CurrencyNumber = "104";
		} else if (pCurrencyCode.equals("BIF")) {
			l_CurrencyNumber = "108";
		} else if (pCurrencyCode.equals("KHR")) {
			l_CurrencyNumber = "116";
		} else if (pCurrencyCode.equals("CAD")) {
			l_CurrencyNumber = "124";
		} else if (pCurrencyCode.equals("CVE")) {
			l_CurrencyNumber = "132";
		} else if (pCurrencyCode.equals("KYD")) {
			l_CurrencyNumber = "136";
		} else if (pCurrencyCode.equals("LKR")) {
			l_CurrencyNumber = "144";
		} else if (pCurrencyCode.equals("CLP")) {
			l_CurrencyNumber = "152";
		} else if (pCurrencyCode.equals("CNY")) {
			l_CurrencyNumber = "156";
		} else if (pCurrencyCode.equals("COP")) {
			l_CurrencyNumber = "170";
		} else if (pCurrencyCode.equals("KMF")) {
			l_CurrencyNumber = "174";
		} else if (pCurrencyCode.equals("CRC")) {
			l_CurrencyNumber = "188";
		} else if (pCurrencyCode.equals("HRK")) {
			l_CurrencyNumber = "191";
		} else if (pCurrencyCode.equals("CUP")) {
			l_CurrencyNumber = "192";
		} else if (pCurrencyCode.equals("CZK")) {
			l_CurrencyNumber = "203";
		} else if (pCurrencyCode.equals("DKK")) {
			l_CurrencyNumber = "208";
		} else if (pCurrencyCode.equals("DOP")) {
			l_CurrencyNumber = "214";
		} else if (pCurrencyCode.equals("ETB")) {
			l_CurrencyNumber = "230";
		} else if (pCurrencyCode.equals("ERN")) {
			l_CurrencyNumber = "232";
		} else if (pCurrencyCode.equals("FKP")) {
			l_CurrencyNumber = "238";
		} else if (pCurrencyCode.equals("FJD")) {
			l_CurrencyNumber = "242";
		} else if (pCurrencyCode.equals("DJF")) {
			l_CurrencyNumber = "262";
		} else if (pCurrencyCode.equals("GMD")) {
			l_CurrencyNumber = "270";
		} else if (pCurrencyCode.equals("GIP")) {
			l_CurrencyNumber = "292";
		} else if (pCurrencyCode.equals("GTQ")) {
			l_CurrencyNumber = "320";
		} else if (pCurrencyCode.equals("GNF")) {
			l_CurrencyNumber = "324";
		} else if (pCurrencyCode.equals("GYD")) {
			l_CurrencyNumber = "328";
		} else if (pCurrencyCode.equals("HTG")) {
			l_CurrencyNumber = "332";
		} else if (pCurrencyCode.equals("HNL")) {
			l_CurrencyNumber = "340";
		} else if (pCurrencyCode.equals("HKD")) {
			l_CurrencyNumber = "344";
		} else if (pCurrencyCode.equals("HUF")) {
			l_CurrencyNumber = "348";
		} else if (pCurrencyCode.equals("ISK")) {
			l_CurrencyNumber = "352";
		} else if (pCurrencyCode.equals("INR")) {
			l_CurrencyNumber = "356";
		} else if (pCurrencyCode.equals("IDR")) {
			l_CurrencyNumber = "360";
		} else if (pCurrencyCode.equals("IRR")) {
			l_CurrencyNumber = "364";
		} else if (pCurrencyCode.equals("IQD")) {
			l_CurrencyNumber = "368";
		} else if (pCurrencyCode.equals("ILS")) {
			l_CurrencyNumber = "376";
		} else if (pCurrencyCode.equals("JMD")) {
			l_CurrencyNumber = "388";
		} else if (pCurrencyCode.equals("JPY")) {
			l_CurrencyNumber = "392";
		} else if (pCurrencyCode.equals("KZT")) {
			l_CurrencyNumber = "398";
		} else if (pCurrencyCode.equals("JOD")) {
			l_CurrencyNumber = "400";
		} else if (pCurrencyCode.equals("KES")) {
			l_CurrencyNumber = "404";
		} else if (pCurrencyCode.equals("KPW")) {
			l_CurrencyNumber = "408";
		} else if (pCurrencyCode.equals("KRW")) {
			l_CurrencyNumber = "410";
		} else if (pCurrencyCode.equals("KWD")) {
			l_CurrencyNumber = "414";
		} else if (pCurrencyCode.equals("KGS")) {
			l_CurrencyNumber = "417";
		} else if (pCurrencyCode.equals("LAK")) {
			l_CurrencyNumber = "418";
		} else if (pCurrencyCode.equals("LBP")) {
			l_CurrencyNumber = "422";
		} else if (pCurrencyCode.equals("LSL")) {
			l_CurrencyNumber = "426";
		} else if (pCurrencyCode.equals("LVL")) {
			l_CurrencyNumber = "428";
		} else if (pCurrencyCode.equals("LRD")) {
			l_CurrencyNumber = "430";
		} else if (pCurrencyCode.equals("LYD")) {
			l_CurrencyNumber = "434";
		} else if (pCurrencyCode.equals("LTL")) {
			l_CurrencyNumber = "440";
		} else if (pCurrencyCode.equals("MOP")) {
			l_CurrencyNumber = "446";
		} else if (pCurrencyCode.equals("MWK")) {
			l_CurrencyNumber = "454";
		} else if (pCurrencyCode.equals("MYR")) {
			l_CurrencyNumber = "458";
		} else if (pCurrencyCode.equals("MVR")) {
			l_CurrencyNumber = "462";
		} else if (pCurrencyCode.equals("MRO")) {
			l_CurrencyNumber = "478";
		} else if (pCurrencyCode.equals("MUR")) {
			l_CurrencyNumber = "480";
		} else if (pCurrencyCode.equals("MXN")) {
			l_CurrencyNumber = "484";
		} else if (pCurrencyCode.equals("MNT")) {
			l_CurrencyNumber = "496";
		} else if (pCurrencyCode.equals("MDL")) {
			l_CurrencyNumber = "498";
		} else if (pCurrencyCode.equals("MAD")) {
			l_CurrencyNumber = "504";
		} else if (pCurrencyCode.equals("OMR")) {
			l_CurrencyNumber = "512";
		} else if (pCurrencyCode.equals("NAD")) {
			l_CurrencyNumber = "516";
		} else if (pCurrencyCode.equals("NPR")) {
			l_CurrencyNumber = "524";
		} else if (pCurrencyCode.equals("ANG")) {
			l_CurrencyNumber = "532";
		} else if (pCurrencyCode.equals("AWG")) {
			l_CurrencyNumber = "533";
		} else if (pCurrencyCode.equals("VUV")) {
			l_CurrencyNumber = "548";
		} else if (pCurrencyCode.equals("NZD")) {
			l_CurrencyNumber = "554";
		} else if (pCurrencyCode.equals("NIO")) {
			l_CurrencyNumber = "558";
		} else if (pCurrencyCode.equals("NGN")) {
			l_CurrencyNumber = "566";
		} else if (pCurrencyCode.equals("NOK")) {
			l_CurrencyNumber = "578";
		} else if (pCurrencyCode.equals("PKR")) {
			l_CurrencyNumber = "586";
		} else if (pCurrencyCode.equals("PAB")) {
			l_CurrencyNumber = "590";
		} else if (pCurrencyCode.equals("PGK")) {
			l_CurrencyNumber = "598";
		} else if (pCurrencyCode.equals("PYG")) {
			l_CurrencyNumber = "600";
		} else if (pCurrencyCode.equals("PEN")) {
			l_CurrencyNumber = "604";
		} else if (pCurrencyCode.equals("PHP")) {
			l_CurrencyNumber = "608";
		} else if (pCurrencyCode.equals("QAR")) {
			l_CurrencyNumber = "634";
		} else if (pCurrencyCode.equals("RUB")) {
			l_CurrencyNumber = "643";
		} else if (pCurrencyCode.equals("RWF")) {
			l_CurrencyNumber = "646";
		} else if (pCurrencyCode.equals("SHP")) {
			l_CurrencyNumber = "654";
		} else if (pCurrencyCode.equals("STD")) {
			l_CurrencyNumber = "678";
		} else if (pCurrencyCode.equals("SAR")) {
			l_CurrencyNumber = "682";
		} else if (pCurrencyCode.equals("SCR")) {
			l_CurrencyNumber = "690";
		} else if (pCurrencyCode.equals("SLL")) {
			l_CurrencyNumber = "694";
		} else if (pCurrencyCode.equals("SGD")) {
			l_CurrencyNumber = "702";
		} else if (pCurrencyCode.equals("VND")) {
			l_CurrencyNumber = "704";
		} else if (pCurrencyCode.equals("SOS")) {
			l_CurrencyNumber = "706";
		} else if (pCurrencyCode.equals("ZAR")) {
			l_CurrencyNumber = "710";
		} else if (pCurrencyCode.equals("SSP")) {
			l_CurrencyNumber = "728";
		} else if (pCurrencyCode.equals("SZL")) {
			l_CurrencyNumber = "748";
		} else if (pCurrencyCode.equals("SEK")) {
			l_CurrencyNumber = "752";
		} else if (pCurrencyCode.equals("CHF")) {
			l_CurrencyNumber = "756";
		} else if (pCurrencyCode.equals("SYP")) {
			l_CurrencyNumber = "760";
		} else if (pCurrencyCode.equals("THB")) {
			l_CurrencyNumber = "764";
		} else if (pCurrencyCode.equals("TOP")) {
			l_CurrencyNumber = "776";
		} else if (pCurrencyCode.equals("TTD")) {
			l_CurrencyNumber = "780";
		} else if (pCurrencyCode.equals("AED")) {
			l_CurrencyNumber = "784";
		} else if (pCurrencyCode.equals("TND")) {
			l_CurrencyNumber = "788";
		} else if (pCurrencyCode.equals("UGX")) {
			l_CurrencyNumber = "800";
		} else if (pCurrencyCode.equals("MKD")) {
			l_CurrencyNumber = "807";
		} else if (pCurrencyCode.equals("EGP")) {
			l_CurrencyNumber = "818";
		} else if (pCurrencyCode.equals("GBP")) {
			l_CurrencyNumber = "826";
		} else if (pCurrencyCode.equals("TZS")) {
			l_CurrencyNumber = "834";
		} else if (pCurrencyCode.equals("USD")) {
			l_CurrencyNumber = "840";
		} else if (pCurrencyCode.equals("UYU")) {
			l_CurrencyNumber = "858";
		} else if (pCurrencyCode.equals("UZS")) {
			l_CurrencyNumber = "860";
		} else if (pCurrencyCode.equals("WST")) {
			l_CurrencyNumber = "882";
		} else if (pCurrencyCode.equals("YER")) {
			l_CurrencyNumber = "886";
		} else if (pCurrencyCode.equals("ZMK")) {
			l_CurrencyNumber = "894";
		} else if (pCurrencyCode.equals("TWD")) {
			l_CurrencyNumber = "901";
		} else if (pCurrencyCode.equals("CUC")) {
			l_CurrencyNumber = "931";
		} else if (pCurrencyCode.equals("ZWL")) {
			l_CurrencyNumber = "932";
		} else if (pCurrencyCode.equals("TMT")) {
			l_CurrencyNumber = "934";
		} else if (pCurrencyCode.equals("GHS")) {
			l_CurrencyNumber = "936";
		} else if (pCurrencyCode.equals("VEF")) {
			l_CurrencyNumber = "937";
		} else if (pCurrencyCode.equals("SDG")) {
			l_CurrencyNumber = "938";
		} else if (pCurrencyCode.equals("UYI")) {
			l_CurrencyNumber = "940";
		} else if (pCurrencyCode.equals("RSD")) {
			l_CurrencyNumber = "941";
		} else if (pCurrencyCode.equals("MZN")) {
			l_CurrencyNumber = "943";
		} else if (pCurrencyCode.equals("AZN")) {
			l_CurrencyNumber = "944";
		} else if (pCurrencyCode.equals("RON")) {
			l_CurrencyNumber = "946";
		} else if (pCurrencyCode.equals("CHE")) {
			l_CurrencyNumber = "947";
		} else if (pCurrencyCode.equals("CHW")) {
			l_CurrencyNumber = "948";
		} else if (pCurrencyCode.equals("TRY")) {
			l_CurrencyNumber = "949";
		} else if (pCurrencyCode.equals("XAF")) {
			l_CurrencyNumber = "950";
		} else if (pCurrencyCode.equals("XCD")) {
			l_CurrencyNumber = "951";
		} else if (pCurrencyCode.equals("XOF")) {
			l_CurrencyNumber = "952";
		} else if (pCurrencyCode.equals("XPF")) {
			l_CurrencyNumber = "953";
		} else if (pCurrencyCode.equals("XBA")) {
			l_CurrencyNumber = "955";
		} else if (pCurrencyCode.equals("XBB")) {
			l_CurrencyNumber = "956";
		} else if (pCurrencyCode.equals("XBC")) {
			l_CurrencyNumber = "957";
		} else if (pCurrencyCode.equals("XBD")) {
			l_CurrencyNumber = "958";
		} else if (pCurrencyCode.equals("XAU")) {
			l_CurrencyNumber = "959";
		} else if (pCurrencyCode.equals("XDR")) {
			l_CurrencyNumber = "960";
		} else if (pCurrencyCode.equals("XAG")) {
			l_CurrencyNumber = "961";
		} else if (pCurrencyCode.equals("XPT")) {
			l_CurrencyNumber = "962";
		} else if (pCurrencyCode.equals("XTS")) {
			l_CurrencyNumber = "963";
		} else if (pCurrencyCode.equals("XPD")) {
			l_CurrencyNumber = "964";
		} else if (pCurrencyCode.equals("SRD")) {
			l_CurrencyNumber = "968";
		} else if (pCurrencyCode.equals("MGA")) {
			l_CurrencyNumber = "969";
		} else if (pCurrencyCode.equals("COU")) {
			l_CurrencyNumber = "970";
		} else if (pCurrencyCode.equals("AFN")) {
			l_CurrencyNumber = "971";
		} else if (pCurrencyCode.equals("TJS")) {
			l_CurrencyNumber = "972";
		} else if (pCurrencyCode.equals("AOA")) {
			l_CurrencyNumber = "973";
		} else if (pCurrencyCode.equals("BYR")) {
			l_CurrencyNumber = "974";
		} else if (pCurrencyCode.equals("BGN")) {
			l_CurrencyNumber = "975";
		} else if (pCurrencyCode.equals("CDF")) {
			l_CurrencyNumber = "976";
		} else if (pCurrencyCode.equals("BAM")) {
			l_CurrencyNumber = "977";
		} else if (pCurrencyCode.equals("EUR")) {
			l_CurrencyNumber = "978";
		} else if (pCurrencyCode.equals("MXV")) {
			l_CurrencyNumber = "979";
		} else if (pCurrencyCode.equals("UAH")) {
			l_CurrencyNumber = "980";
		} else if (pCurrencyCode.equals("GEL")) {
			l_CurrencyNumber = "981";
		} else if (pCurrencyCode.equals("BOV")) {
			l_CurrencyNumber = "984";
		} else if (pCurrencyCode.equals("PLN")) {
			l_CurrencyNumber = "985";
		} else if (pCurrencyCode.equals("BRL")) {
			l_CurrencyNumber = "986";
		} else if (pCurrencyCode.equals("CLF")) {
			l_CurrencyNumber = "990";
		} else if (pCurrencyCode.equals("USN")) {
			l_CurrencyNumber = "997";
		} else if (pCurrencyCode.equals("USS")) {
			l_CurrencyNumber = "998";
		} else if (pCurrencyCode.equals("XXX")) {
			l_CurrencyNumber = "999";
		} else if (pCurrencyCode.equals("XFU")) {
			l_CurrencyNumber = "Nil ";
		}
		return l_CurrencyNumber;
	}

	public static String getCurrentDateYYYYMMDDHHMMSS() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	public static String getCurrentDateYYYYMMDDHHMMSSNoSpace() {
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	public static String getDateTimeDDMMYYYYHHMMSS() {
		String l_Pattern = "dd/MM/yyyy HH:mm:ss";
		String l_DateTime = "01/01/1900 00:00:00";
		SimpleDateFormat format = new SimpleDateFormat(l_Pattern);
		l_DateTime = format.format(new Date());
		return l_DateTime;
	}

	public static String getDateTimeYYYYDDMM() {
		Date d = new Date(System.currentTimeMillis()); // liftOffApollo11.getTime();
		// DateFormat df1 = DateFormat.getDateTimeInstance("yyyyMMdd");
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
		String s1 = s.format(d);
		return s1;
	}

	public static String getDateTimeYYYYDDMMmmss() {
		Date d = new Date(System.currentTimeMillis()); // liftOffApollo11.getTime();
		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd h:m:s a");
		String s1 = s.format(d);
		return s1;
	}

	public static String getDrCr(int pTransType) {
		String ret = "";
		if (pTransType > 500)
			ret = "D";
		else
			ret = "C";
		return ret;
	}

	// APH 15/10/204
	public static double getInterestAmount(double pAmount, double pInterestRate, int pMonth) {
		double interestAmount = 0;
		interestAmount = (pAmount * pInterestRate) / 1200 * pMonth;
		return interestAmount;
	}

	public static String getMonth(String pYYYYMMDD) {
		String l_Month = pYYYYMMDD.substring(4, 6);
		return l_Month;
	}

	public static String getNextRuntime(String lastRuntime, int numberofday) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String nextruntimedt = "";

		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(lastRuntime));
			cal.add(Calendar.DATE, numberofday);
			nextruntimedt = format.format(cal.getTime());

			System.out.println("Date increase by one.." + nextruntimedt);
		} catch (Exception ex) {
		}

		return nextruntimedt;
	}

	public static long getNumberOfDayToProcess(String date) {
		Date date1;
		Date date2;
		long diffDays = 0;
		if (!date.equals("")) {
			java.util.Date l_Date = new java.util.Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String l_todaydate = format.format(l_Date);
			try {
				date1 = format.parse(l_todaydate);
				date2 = format.parse(date);
				long diff = date1.getTime() - date2.getTime();
				diffDays = diff / (24 * 60 * 60 * 1000);
			} catch (ParseException ex) {
			}
		}
		return diffDays;
	}

	public static String getPreviousDate(String aDate) {
		// aDate == 01/10/2011
		String[] l_date = aDate.split("/");
		int day = Integer.parseInt(l_date[0]);
		int month = Integer.parseInt(l_date[1]);
		int year = Integer.parseInt(l_date[2]);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		// System.out.println("Today's date was
		// "+dateFormat.format(c.getTime()));
		c.add(Calendar.DATE, -1);
		// System.out.println("Yesterday's date was
		// "+dateFormat.format(c.getTime()));

		return dateFormat.format(c.getTime());
	}

	public static String getSign(double pAmount) {
		String ret = "+";
		if (pAmount > 0)
			ret = "+";
		else
			ret = "-";
		return ret;
	}

	public static String getTellerId(String pTerminalId) {
		String l_TellerId = "";
		int l_TerminalCode = Integer.parseInt(pTerminalId.substring(0, 1));
		if (l_TerminalCode == 0) {
			l_TellerId = "ATM";
		} else if (l_TerminalCode == 1) {
			l_TellerId = "POS";
		}
		return l_TellerId;
	}

	public static String getTodayDate() {
		DateFormat l_DateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar l_Calendar = Calendar.getInstance();
		String l_TodayDate = l_DateFormat.format(l_Calendar.getTime());
		return l_TodayDate;
	}

	public static String getTodayDateDD_MM_YYYY() {
		DateFormat l_DateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar l_Calendar = Calendar.getInstance();
		String l_TodayDate = l_DateFormat.format(l_Calendar.getTime());
		return l_TodayDate;
	}

	public static String getTodayMonth() {
		String l_TodayDate = getTodayDate();
		String l_TodayMonth = l_TodayDate.substring(4, 6);
		return l_TodayMonth;
	}

	public static String getTodayTimeHHMMSS() {
		String l_DateTime;
		String l_Time;
		l_DateTime = getDateTimeDDMMYYYYHHMMSS();
		l_Time = l_DateTime.substring(11, 19);
		return l_Time;
	}

	public static String getTomorrowDate() {
		DateFormat l_DateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar l_Calendar = Calendar.getInstance();
		l_Calendar.add(Calendar.DATE, 1);
		String l_TodayDate = l_DateFormat.format(l_Calendar.getTime());
		return l_TodayDate;
	}

	public static String getVersionNo() {
		return mVersionNo;
	}

	public static Boolean isBankingHour(String pFromDateTime, String pToDateTime) {
		boolean ret = false;
		String l_Pattern = "dd/MM/yyyy HH:mm:ss";
		String l_DateTime = "01/01/1900 00:00:00";
		SimpleDateFormat format = new SimpleDateFormat(l_Pattern);
		String l_FromBankingHour = pFromDateTime;
		String l_ToBankingHour = pToDateTime;
		try {
			Date l_FromBankingHourDate = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(l_FromBankingHour);
			Date l_ToBankingHourDate = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(l_ToBankingHour);
			Date l_Today = new Date();

			l_DateTime = format.format(l_Today);
			System.out.println("From Banking Date	: " + l_FromBankingHour);
			System.out.println("To Banking Date		: " + l_ToBankingHour);
			System.out.println("Trans Date		: " + l_DateTime);
			System.out.println("From Banking Time	: " + l_FromBankingHourDate.getTime());
			System.out.println("To Banking Time		: " + l_ToBankingHourDate.getTime());
			System.out.println("Trans Time		: " + l_Today.getTime());
			if ((l_FromBankingHourDate.getTime() < l_Today.getTime())
					&& (l_Today.getTime() < l_ToBankingHourDate.getTime())) {
				System.out.println("Banking Hour");
				ret = true;
			} else {
				System.out.println("Not Banking Hour");
				ret = false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Boolean isBankingHour2(String pTodayDateTime) {
		boolean ret = false;
		String l_Pattern = "dd/MM/yyyy HH:mm:ss";
		String l_DateTime = "01/01/1900 00:00:00";
		SimpleDateFormat format = new SimpleDateFormat(l_Pattern);
		String l_BankingHour = pTodayDateTime;
		try {
			Date l_BankingHourDate = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(l_BankingHour);
			Date l_Today = new Date();

			l_DateTime = format.format(l_Today);
			System.out.println("Banking Date	: " + l_BankingHour);
			System.out.println("Trans Date		: " + l_DateTime);
			System.out.println("Banking Time	: " + l_BankingHourDate.getTime());
			System.out.println("Trans Time		: " + l_Today.getTime());

			if (l_BankingHourDate.compareTo(l_Today) > 0) {
				System.out.println("Banking Date Greater than Today");
			} else if (l_BankingHourDate.compareTo(l_Today) < 0) {
				System.out.println("Banking Date Less than Today");
			} else if (l_BankingHourDate.compareTo(l_Today) == 0) {
				System.out.println("Banking Date is Equal to Today");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	// APH 28-01-2015
	public static boolean isCardAccount(String pAccNo) {
		boolean ret = false;
		if (pAccNo.startsWith("999"))
			ret = true;
		return ret;
	}


	public static BigDecimal round2BigDecimals(double pAmount, RoundingMode mode) {
		BigDecimal l_result = new BigDecimal(0);
		DecimalFormat l_df = new DecimalFormat("###0.00");
		l_df.setRoundingMode(mode);

		l_result = new BigDecimal(l_df.format(pAmount));
		return l_result;
	}

	public static double roundTo2Decimals(double val) {
		DecimalFormat df2 = new DecimalFormat("###.##");
		return Double.valueOf(df2.format(val));
	}
	
	public static CutOffTimeData getConnection() {
		CutOffTimeData ret = new CutOffTimeData();
		ArrayList<String> oracleConnList;

		try {
			oracleConnList = ReadFile.readConnection(DAOManager.AbsolutePath + "WEB-INF//data//cutOfTime.txt");
			if (oracleConnList.size() > 0) {
				ret.setHour(oracleConnList.get(0).split("Hour:")[1]);
				ret.setMinutes(oracleConnList.get(1).split("Minutes:")[1]);
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}
	
	public static boolean checkCutOfTime() {
		boolean result = false;
		try {
			CutOffTimeData ret = getConnection();
			CutOffTimeData.readDate = ret.getHour() + ":" + ret.getMinutes();
			result = datetimeformatHHMM();
		} catch (Exception ex) {
			System.out.println("*** Error in Cut off Time : " + ex.getMessage());
		}
		return result;
	}
	
	public static boolean datetimeformatHHMM() throws Exception {
		String l_Pattern = "HH:mm";
		boolean ret = false;
		Date date = new Date();
		String todayDate = new SimpleDateFormat("HH:mm").format(date);
		Date l_Date = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(todayDate);
		Date l_CutOfTimeDate = new SimpleDateFormat(l_Pattern, Locale.ENGLISH).parse(CutOffTimeData.readDate);

		if ((l_CutOfTimeDate.getTime() >= l_Date.getTime())) {
			System.out.println("It is not Cut Of Time");
			ret = false;
		} else {
			System.out.println("It is Cut of Time");
			ret = true;
		}
		return ret;
	}
	
	public static RateAmountRangeSetupData getRateByAmountRange(double pAmount,
			ArrayList<RateAmountRangeSetupData> pAmountRangeList) {
		RateAmountRangeSetupData l_RateData = new RateAmountRangeSetupData();

		for (int i = 0; i < pAmountRangeList.size(); i++) {
			if (pAmount >= pAmountRangeList.get(i).getFromAmount()
					&& pAmount <= pAmountRangeList.get(i).getToAmount()) {
				l_RateData = pAmountRangeList.get(i);
				break;
			}
		}

		return l_RateData;
	}
	
	public static String changeDateFormatyyyyMMdd(String pdate) {
		String formatted = "";
		if(!pdate.equals("")) {
			// from 2017-12-06 0to 06-12-2017
			try {
				if(!pdate.equals(null) && !pdate.equals("")) {
				Date parsed = new SimpleDateFormat("yyyyMMdd").parse(pdate);
				formatted = new SimpleDateFormat("yyyy-MM-dd").format(parsed);
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return formatted;
	}
	
	
	public static String getCurrentDate(){
	    String pattern = "dd/MM/yyyy";
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    return format.format(new Date());
	}
	
	 public static String formatDBDate2MIT(String p) {
	    	int intValue = 0;
	    	String res = "";
	    	try {
			    intValue = Integer.parseInt(p);
			    res = p;
			} catch (NumberFormatException e) {				
				res = formatYYYYMMDD2MIT(p);
			}
	    	return res;     // <<<<<<<<<<<<<<<<<<<<<<<<<<<<  COFIG
	  }
	 
	 public static String formatYYYYMMDD2MIT(String p) {
	    	String ret="";
	    	try{
	    		if (p.length()>=10) {
	    			ret = p.replaceAll("-", "").substring(0, 8);
				}
	    		else
	    		{
	    			ret = "19000101";
	    		}
	
	    	}catch (Exception exp){}
	    	return ret;
	    }
	 
	 public static long generateSyskey() {
	        final UUID uid = UUID.randomUUID();
	        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
	        buffer.putLong(uid.getLeastSignificantBits());
	        buffer.putLong(uid.getMostSignificantBits());
	        final BigInteger bi = new BigInteger(buffer.array());
	        return Math.abs(bi.longValue());
	}
}
