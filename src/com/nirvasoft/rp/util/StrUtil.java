
package com.nirvasoft.rp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

import com.nirvasoft.rp.shared.BudgetYearData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.icbs.CurrencyRateData;

import password.DESedeEncryption;

/**
 * 
 * TUN THURA THET @ NTU 2007-2009
 */
public class StrUtil {

	public static ArrayList<String> replacee;

	public static String addDays(String pDate, int days) {
		String ret = "";

		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));

		day = day + days;

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day); // month start from 0

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		ret = dateFormat.format(cal.getTime());

		return ret;
	}

	public static String addField(String f1, String f2) {
		return addField(f1, f2, "__|");
	}

	public static String addField(String f1, String f2, String sym) {
		if (!isEmpty(f1) && !f1.endsWith(sym))
			f1 = sep(f1, sym);
		f1 += sep(f2, sym);
		return f1;
	}

	public static String addMonths(String pDate, int months) {
		String ret = "";

		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));

		month--; // month start from 0
		month = month + months;

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		ret = dateFormat.format(cal.getTime());

		return ret;
	}

	public static String addSpaces(long p, int size) {
		return addSpaces("" + p, size);
	}

	public static String addSpaces(String p, int size) {
		String ret = p;
		for (int i = p.length(); i < size; i++) {
			ret = ret + " ";
		}
		return ret;
	}

	public static boolean checkCurrencyRate(double aSpreadRate, double aRate, String aCurCode,
			ArrayList<CurrencyRateData> aCurrencySpreadRateDataList) {
		boolean l_result = false;
		double l_Max = 0;
		double l_Min = 0;
		String l_Type = "";
		String l_DecimalFormat = "";

		// l_DecimalFormat =
		// StrUtil.getDecimalFormat(SharedLogic.getDecimalCounts());
		for (int i = 0; i < aCurrencySpreadRateDataList.size(); i++) {
			if (aCurrencySpreadRateDataList.get(i).getCurrencyCode().equalsIgnoreCase(aCurCode)) {
				l_Type = aCurrencySpreadRateDataList.get(i).getT2();
				l_Max = aCurrencySpreadRateDataList.get(i).getBuyRate();
				l_Min = aCurrencySpreadRateDataList.get(i).getSellRate();
				break;
			}
		}
		if (l_Type.equalsIgnoreCase("F")) {
			l_Max = aRate + l_Max;
			l_Min = aRate - l_Min;
		} else if (l_Type.equalsIgnoreCase("P")) {
			l_Max = (aRate * (l_Max / 100));
			l_Min = (aRate * (l_Min / 100));

			l_Max = aRate + l_Max;
			l_Min = aRate - l_Min;
		}

		l_Max = Double.parseDouble(StrUtil.formatDecimals(l_Max, l_DecimalFormat));
		l_Min = Double.parseDouble(StrUtil.formatDecimals(l_Min, l_DecimalFormat));

		if ((l_Max >= aSpreadRate) && (l_Min <= aSpreadRate)) {
			l_result = true;
		}
		return l_result;
	}

	public static String chop(String strMain, String strStart, String strEnd) {
		int s = strMain.toLowerCase().indexOf(strStart.toLowerCase()) + strStart.length();
		if (s == -1)
			s = 0;
		int e = strMain.indexOf(strEnd, s);
		if (e == -1)
			e = strMain.length();
		return trimText(strMain.substring(s, e));
	}

	public static String chop(String strMain, String strStart, String strEnd, boolean inc) {
		int i, j;
		if (!inc) {
			i = strMain.toLowerCase().indexOf(strStart.toLowerCase()) + strStart.length();
			j = strMain.indexOf(strEnd, i);
		} else {
			i = strMain.toLowerCase().indexOf(strStart.toLowerCase());
			j = strMain.indexOf(strEnd, i) + strEnd.length();
		}
		return trimText(strMain.substring(i, j));
	}

	public static ArrayList<String> chop2ARL(String strMain, String strStart, String strEnd) {
		ArrayList<String> arl = new ArrayList<String>();
		int s = 0;
		while (strMain.toLowerCase().indexOf(strStart.toLowerCase(), s) >= 0) {
			s = strMain.toLowerCase().indexOf(strStart.toLowerCase(), s);
			int e = strMain.toLowerCase().indexOf(strEnd.toLowerCase(), s);
			if (e >= 0) {
				String result = strMain.substring(s, e + strEnd.length());
				arl.add(result);
				s = e;
			} else
				s++;
		}
		return arl;
	}

	public static ArrayList<String> chop2ARLwoTags(String strMain, String strStart, String strEnd) {
		ArrayList<String> arl = new ArrayList<String>();
		int s = 0;
		while (strMain.toLowerCase().indexOf(strStart.toLowerCase(), s) >= 0) {
			s = strMain.toLowerCase().indexOf(strStart.toLowerCase(), s);
			int e = strMain.toLowerCase().indexOf(strEnd.toLowerCase(), s);
			if (e >= 0) {
				String result = strMain.substring(s, e + strEnd.length());
				// arl.add(stripTags(result));
				arl.add(result);
				s = e;
			} else
				s++;
		}
		return arl;
	}

	public static String cleanBreakers(String str) {
		str = str.replaceAll("\\.'", "'");
		str = str.replaceAll("\\?'", "'");
		str = str.replaceAll("\\.\\)", ")");
		str = str.replaceAll("\\?\\)", ")");
		str = str.replaceAll("Mr\\.", "Mr");
		str = str.replaceAll("Mr\\.", "Mr");
		str = str.replaceAll("Mrs\\.", "Mrs");
		str = str.replaceAll("Ms\\.", "Ms");
		str = str.replaceAll("Dr\\.", "Dr");
		str = str.replaceAll("A\\.", "A");
		str = str.replaceAll("B\\.", "B");
		str = str.replaceAll("C\\.", "C");
		str = str.replaceAll("D\\.", "D");
		str = str.replaceAll("E\\.", "E");
		str = str.replaceAll("F\\.", "F");
		str = str.replaceAll("G\\.", "G");
		str = str.replaceAll("H\\.", "H");
		str = str.replaceAll("I\\.", "I");
		str = str.replaceAll("J\\.", "J");
		str = str.replaceAll("K\\.", "K");
		str = str.replaceAll("L\\.", "L");
		str = str.replaceAll("M\\.", "M");
		str = str.replaceAll("N\\.", "N");
		str = str.replaceAll("O\\.", "O");
		str = str.replaceAll("P\\.", "P");
		str = str.replaceAll("Q\\.", "Q");
		str = str.replaceAll("R\\.", "R");
		str = str.replaceAll("S\\.", "S");
		str = str.replaceAll("T\\.", "T");
		str = str.replaceAll("U\\.", "U");
		str = str.replaceAll("V\\.", "V");
		str = str.replaceAll("W\\.", "W");
		str = str.replaceAll("X\\.", "X");
		str = str.replaceAll("Y\\.", "Y");
		str = str.replaceAll("Z\\.", "Z");
		str = str.replaceAll("\\(", "");
		str = str.replaceAll("\\)", "");
		return str;
	}

	public static String cleanDblSpaces(String strText) {
		strText = strText.replaceAll("\r", "\n");
		strText = strText.replaceAll("\t", " ");
		strText = strText.replaceAll(" \n", "\n");
		strText = strText.replaceAll("\n ", "\n");
		while (strText.contains("  ")) {
			strText = strText.replace("  ", " ");
		}
		while (strText.contains(" \n")) {
			strText = strText.replace(" \n", "\n");
		}
		while (strText.contains("\n \n")) {
			strText = strText.replace("\n \n", "\n");
		}
		while (strText.contains("\n\n")) {
			strText = strText.replace("\n\n", "\n");
		}
		strText = strText.replaceAll(" \n", "\n");
		strText = strText.replaceAll("\n ", "\n");
		strText = trimText(strText);
		return strText;
	}

	public static String cleanSpaces(String strText) {
		strText = strText.replaceAll("\r", "\n");
		strText = strText.replaceAll("\t", " ");
		strText = strText.replaceAll(" \n", "\n");
		strText = strText.replaceAll("\n ", "\n");
		while (strText.contains("  ")) {
			strText = strText.replace("  ", " ");
		}
		while (strText.contains(" \n")) {
			strText = strText.replace(" \n", "\n");
		}
		while (strText.contains("\n \n")) {
			strText = strText.replace("\n \n", "\n");
		}
		strText = strText.replaceAll(" \n", "\n");
		strText = strText.replaceAll("\n ", "\n");
		strText = trimText(strText);
		return strText;
	}

	public static String cleanText(String str) {
		if (str == null)
			return " ";
		str = str.replace(".", " ");
		str = str.replace(",", " ");
		str = str.replace(":", " ");
		str = str.replace(";", " ");
		str = str.replace("?", " ");
		str = str.replace("!", " ");
		str = str.replace("#", " ");
		str = str.replace("@", " ");
		str = str.replace("+", " ");
		str = str.replace("*", " ");
		str = str.replace("=", " ");
		str = str.replace("/", " ");
		str = str.replace("\\", " ");
		str = str.replace("'s", "s");
		str = str.replace("'m", " m");
		str = str.replace("'", " ");
		str = str.replace("\"", " ");
		str = str.replace("(", " ");
		str = str.replace(")", " ");
		str = str.replace("{", " ");
		str = str.replace("}", " ");
		str = str.replace("[", " ");
		str = str.replace("]", " ");
		str = str.replace("`", " ");
		str = str.replace("-", " ");
		str = str.replace("%", " ");
		str = str.replace("$", " ");
		str = str.replace("&", " ");
		str = str.replace("~", " ");
		str = str.replace("_", " ");
		str = str.replace("|", " ");
		str = str.replace("\t", " ");
		str = str.replace("\n", " ");
		str = str.replace("\r", " ");
		return str;
	}

	public static String cleanWhiteSpaces(String strText) {
		strText = strText.replaceAll("\r", " ");
		strText = strText.replaceAll("\t", " ");
		strText = strText.replaceAll("\n", " ");
		return cleanDblSpaces(strText);
	}

	public static int compareDate(Date pDate1, Date pDate2) {
		int ret = 0;
		ret = pDate1.compareTo(pDate2);
		return ret;
	}

	public static int compareDate(String pDate1, String pDate2) {
		int ret = 0;
		Date startDate;
		Date endDate;
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			startDate = formatter.parse(pDate1);
			endDate = formatter.parse(pDate2);
			ret = compareDate(startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static int[] countEopEov(String str) {
		str = str.replaceAll("\r", " ");
		str = str.replaceAll("\n", " ");
		String ar[] = str.split(" ");
		int ret[] = { 0, 0, 0 };
		for (int i = 0; i < ar.length; i++) {
			if (ar[i].equals("EOP"))
				ret[0]++;
			if (ar[i].equals("EOV"))
				ret[1]++;
			if (ar[i].equals("EOR"))
				ret[2]++;
		}
		return ret;
	}

	/** Using Calendar - THE CORRECT WAY **/
	public static int daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		int daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	public static String decodeURL(String strFileName) {
		try {
			strFileName = strFileName.replace("^f", "\\");
			strFileName = strFileName.replace("^b", "/");
			strFileName = strFileName.replace("^c", ":");
			strFileName = strFileName.replace("^s", "*");
			strFileName = strFileName.replace("^q", "?");
			strFileName = strFileName.replace("^d", "\"");
			strFileName = strFileName.replace("^l", "<");
			strFileName = strFileName.replace("^g", ">");
			strFileName = strFileName.replace("^p", "|");
			strFileName = strFileName.replace("^a", "&");
		} catch (Exception x) {
		}
		return strFileName;
	}

	public static String decryptPIN(String p) {
		String ret = "";
		try {
			DESedeEncryption myEncryptor = new DESedeEncryption();
			ret = myEncryptor.decrypt(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}

	public static String encodeURL(String strURL) {
		try {
			strURL = strURL.replace("\\", "^f");
			strURL = strURL.replace("/", "^b");
			strURL = strURL.replace(":", "^c");
			strURL = strURL.replace("*", "^s");
			strURL = strURL.replace("?", "^q");
			strURL = strURL.replace("\"", "^d");
			strURL = strURL.replace("<", "^l");
			strURL = strURL.replace(">", "^g");
			strURL = strURL.replace("|", "^p");
			strURL = strURL.replace("&", "^a");
			strURL = strURL.replace("\n", "");
		} catch (Exception x) {
		}
		return strURL;
	}

	public static String encryptPIN(String p) {
		String ret = "";
		try {
			DESedeEncryption myEncryptor = new DESedeEncryption();
			ret = myEncryptor.encrypt(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static String expandTerm(String pTerm) {
		if (pTerm.endsWith("ed"))
			pTerm = pTerm.substring(0, pTerm.length() - 2);
		if (pTerm.endsWith("es"))
			pTerm = pTerm.substring(0, pTerm.length() - 2);
		return pTerm;
	}

	public static ArrayList<String> expandTermRoot(String pTerm) {
		ArrayList<String> arl = new ArrayList<String>();
		arl.add(pTerm);
		if (pTerm.endsWith("ed"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		if (pTerm.endsWith("ed"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("ied"))
			arl.add(pTerm.substring(0, pTerm.length() - 3) + "y");
		if (pTerm.endsWith("es"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		if (pTerm.endsWith("es"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("en"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		if (pTerm.endsWith("en"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("ing"))
			arl.add(pTerm.substring(0, pTerm.length() - 3));
		if (pTerm.endsWith("s"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("ly"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		return arl;
	}

	public static ArrayList<String> expandTerms(String pTerm) {
		ArrayList<String> arl = new ArrayList<String>();
		arl.add(pTerm);
		if (!pTerm.endsWith("ed"))
			arl.add(pTerm + "ed");
		if (pTerm.endsWith("ss"))
			arl.add(pTerm + "es");
		if (!pTerm.endsWith("s"))
			arl.add(pTerm + "s");
		if (pTerm.endsWith("y"))
			arl.add(pTerm.substring(0, pTerm.length() - 1) + "ied");
		if (pTerm.endsWith("te"))
			arl.add(pTerm.substring(0, pTerm.length() - 2) + "tten");
		if (!pTerm.endsWith("ing"))
			arl.add(pTerm + "ing");
		return arl;
	}

	public static String expandTerms(String pTerm, String pField) {
		StringBuffer sb = new StringBuffer("( 1=2 ");
		ArrayList<String> arl = new ArrayList<String>();
		if (pTerm.endsWith("ed"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		if (pTerm.endsWith("ed"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("ied"))
			arl.add(pTerm.substring(0, pTerm.length() - 3) + "y");
		if (pTerm.endsWith("es"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		if (pTerm.endsWith("es"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("en"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		if (pTerm.endsWith("en"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("ing"))
			arl.add(pTerm.substring(0, pTerm.length() - 3));
		if (pTerm.endsWith("s"))
			arl.add(pTerm.substring(0, pTerm.length() - 1));
		if (pTerm.endsWith("ly"))
			arl.add(pTerm.substring(0, pTerm.length() - 2));
		for (int i = 0; i < arl.size(); i++) {
			sb.append(" or " + pField + "='" + arl.get(i) + "'");
		}
		sb.append(" )");
		return sb.toString();
	}

	public static String fixCases(String str) {
		if (str == null)
			return " ";
		String[] ar = str.split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ar.length; i++) {
			if (ar[i].equalsIgnoreCase("I")) {
				sb.append("I ");
			} else if (ar[i].length() == 1) {
				sb.append(ar[i] + " ");
			} else if (ar[i].length() == 0) {
			} else if (!isLower(ar[i].substring(0, 1)) && isLower(ar[i].substring(1, 2))) {
				sb.append(ar[i] + " ");
			} else
				sb.append(ar[i].toLowerCase() + " ");
		}
		return sb.toString();
	}

	public static String fixOthers(String str) {
		if (str == null)
			return " ";
		String[] ar = str.split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ar.length; i++) {
			if (ar[i].equalsIgnoreCase("wasnt")) {
				sb.append("wasn't" + " ");
			} else if (ar[i].equalsIgnoreCase("didnt")) {
				sb.append("didn't" + " ");
			} else if (ar[i].equalsIgnoreCase("dont")) {
				sb.append("don't" + " ");
			} else if (ar[i].equalsIgnoreCase("isnt")) {
				sb.append("isn't" + " ");
			} else if (ar[i].equalsIgnoreCase("havent")) {
				sb.append("haven't" + " ");
			} else
				sb.append(ar[i] + " ");
		}
		return sb.toString();
	}

	public static String Fmt(int value, int digit) {
		String s = "" + value;
		for (int i = s.length(); i < digit; i++) {
			s = "0" + s;
		}
		return s;
	}

	public static String fmtDate(Date p) {
		return formatMDate(toMDate(p));
	}

	@SuppressWarnings("deprecation")
	public static String fmtTime(Date p) {

		String ret = "00:00:00";
		ret = p.getHours() + ":" + p.getMinutes() + ":" + p.getSeconds();
		return ret;
	}

	public static String formatDate2DDMMYYYY(Date d) {
		String ret = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			ret = fmt.format(d);
		} catch (Exception ex) {
		}
		return ret;
	}

	public static String formatDecimals(double p, String aDecimalFormat) {
		String ret = "";
		DecimalFormat l_df = new DecimalFormat(aDecimalFormat);
		ret = l_df.format(p);
		return ret;
	}

	public static String formatDecimals(String p, String aDecimalFormat) {
		String ret = "";
		try {
			ret = formatDecimals(Double.parseDouble(p), aDecimalFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String formatMDate(String p) {
		return formatMDate(p, "dd/MM/yyyy");
	}

	public static String formatMDate(String p, String fmt) {
		return (new SimpleDateFormat(fmt)).format(toDate(p, "yyyyMMdd"));
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
			int y = Integer.parseInt(p.substring(0, 4));
			int m = Integer.parseInt(p.substring(4, 6));
			int d = Integer.parseInt(p.substring(6, 8));
			ret = d + "/" + m + "/" + y;
			ret = p.substring(6, 8) + "/" + p.substring(4, 6) + "/" + p.substring(0, 4) + p.substring(8, p.length());
		} catch (Exception ex) {
		}
		return ret;
	}

	public static String formatNumber(double pAmount) {
		String l_result = "0.00";
		DecimalFormat l_df = new DecimalFormat("###0.00");
		l_result = l_df.format(pAmount);
		return l_result;
	}

	public static ArrayList<String> getAR2ARL(String[] ar) {
		ArrayList<String> s = new ArrayList<String>();
		for (int i = 0; i < ar.length; i++) {
			s.add(ar[i]);
		}
		return s;
	}

	public static String[] getARL2AR(ArrayList<String> arl) {
		String[] ar = new String[arl.size()];
		ar = arl.toArray(ar);
		return ar;
	}

	public static String[] getBudgetYear(String aDate) {
		// aDate ==> yyyyMMdd 20120322

		String[] l_BYear = new String[2];
		String l_FPeriod = "";
		String l_SPeriod = "";

		int l_Month = Integer.parseInt(aDate.substring(4, 6));
		int l_Year = Integer.parseInt(aDate.substring(2, 4));

		String l_SYear = (l_Year < 10) ? "0" + l_Year : l_Year + "";

		if (l_Month >= 4) {

			int l_nextYear = l_Year + 1;
			String l_SnextYear = l_nextYear + "";

			l_FPeriod = l_SYear;
			l_SPeriod = ((l_SnextYear.length() >= 2)
					? ((l_SnextYear.length() > 2) ? l_SnextYear.substring(1) : l_SnextYear) : "0" + l_SnextYear);

		} else {

			int l_preYear = (l_Year == 0) ? 99 : l_Year - 1;
			String l_SpreYear = (l_preYear < 10) ? "0" + l_preYear : l_preYear + "";

			l_FPeriod = l_SpreYear;
			l_SPeriod = l_SYear;
		}
		l_BYear[0] = l_FPeriod;
		l_BYear[1] = l_SPeriod;
		return l_BYear;
	}

	// hnns
	public static String getBudgetYearForSave(String aDate) {
		// aDate ==> yyyyMMdd 20120322

		String l_BYear = "";

		int l_Month = Integer.parseInt(aDate.substring(4, 6));
		int l_Year = Integer.parseInt(aDate.substring(0, 4));

		if (l_Month >= 4) {
			l_BYear = l_Year + "";

		} else {
			l_BYear = (l_Year == 0) ? 9999 + "" : l_Year - 1 + "";
		}

		return l_BYear;
	}

	public static String getCurrentDate() {
		String ret = "";
		DateFormat mit = new SimpleDateFormat("dd/MM/yyyy");
		ret = mit.format(getDate());
		return ret;
	}

	public static String getCurrentTime() {
		String ret = "";
		DateFormat df = new SimpleDateFormat("kk:mm:ss");
		ret = df.format(getDate());
		return ret;
	}

	public static Date getDate() {
		return new Date(System.currentTimeMillis());
	}

	public static int getDaysBetweentwoDates(String pStartDate, String pEndDate) {
		DateFormat formatter;
		Date sDate;
		Date eDate;
		int days = 0;
		formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			sDate = formatter.parse(pStartDate);
			eDate = formatter.parse(pEndDate);
			Calendar cal3 = Calendar.getInstance();
			cal3.setTime(sDate);
			Calendar cal4 = Calendar.getInstance();
			cal4.setTime(eDate);
			days = daysBetween(cal3, cal4);
			System.out.println("calculateDays : " + days);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return days;
	}

	public static String getDecimalFormat(int aDecimalCounts) {
		String l_Format = "###0.";
		if (aDecimalCounts >= 1) {
			for (int i = 1; i <= aDecimalCounts; i++) {
				l_Format += "0";
			}
		} else {
			l_Format = "###0.00";
		}

		return l_Format;
	}

	public static String getDecimalFormatWithSeparator(int aDecimalCounts) {
		String l_Format = "#,##0.";
		if (aDecimalCounts >= 1) {
			for (int i = 1; i <= aDecimalCounts; i++) {
				l_Format += "0";
			}
		} else {
			l_Format = "#,##0.00";
		}

		return l_Format;
	}

	public static String getInner(String strMain, String start, String end, int beginsAt) {
		start = start.toLowerCase();
		end = end.toLowerCase();
		int startAt = 0, endAt = 0;
		String strRet = "";
		try {
			startAt = strMain.toLowerCase().indexOf(start, beginsAt);
			if (startAt >= 0) {
				startAt++;
				endAt = strMain.toLowerCase().indexOf(end, startAt + start.length() - 1);
				if (endAt >= 0) {
					strRet = strMain.substring(startAt + start.length() - 1, endAt);
				}
			}
		} catch (Exception ex) {
			System.out.println("getInner : " + ex.toString());
		}
		return trimText(strRet);
	}

	public static String getInnerInclusive(String strMain, String start, String end, int beginsAt) {
		// what ?
		String ret = "";
		try {
			int s = indexOf(strMain, start, beginsAt);
			if (s > -1) {
				int i = s;
				for (i = s; i > 0; i--) {
					if (strMain.charAt(i) == '>')
						break;
				}
				beginsAt = i - 1;
				String st = ">";
				int startAt = 0, endAt = 0;
				startAt = strMain.toLowerCase().indexOf(st, beginsAt);
				if (startAt >= 0) {
					startAt++;
					endAt = strMain.toLowerCase().indexOf(end, startAt + start.length() - 1);
					if (endAt >= 0) {
						ret = strMain.substring(startAt + start.length() - 1, endAt + end.length());
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("getInnerInclusive : " + ex.toString());
		}
		return ret;
	}

	public static String getInnerInclusiveMultiple(String strMain, String start, String end, int beginsAt) {
		// text between 2 fixed Strings; (similar to simple but simple works
		// with multi attribute, exclusive of tags)
		String ret = "";
		ret = getInnerInclusive(strMain, start, end, beginsAt);
		String buffer = " ";
		int i = 0;
		while (buffer.length() > 0 && ++i <= 10) {
			beginsAt = indexOf(strMain, start, beginsAt) + buffer.length(); // i
																			// for
																			// \n
																			// to
																			// join
			buffer = getInnerInclusive(strMain, start, end, beginsAt);
			ret += "\n\n\n" + buffer;
		}
		return ret;
	}

	public static String getInsider(String strMain, String start, String end, int beginsAt) {
		String ret = "";
		try {
			int s = indexOf(strMain, start, beginsAt);
			if (s > -1) {
				int i = s;
				for (i = s; i > 0; i--) {
					if (strMain.charAt(i) == '>')
						break;
				}
				beginsAt = i - 1;
				ret = getInner(strMain, ">", end, beginsAt);
			}
		} catch (Exception ex) {
			System.out.println("getInnerInclusive : " + ex.toString());
		}
		return ret;
	}

	@SuppressWarnings("deprecation")
	public static int getMonthBetweenTwoDates(String pStartDate, String pEndDate) {
		DateFormat formatter;
		Date date;
		Date date1;

		long monthDiff = 0;
		formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			date = formatter.parse(pStartDate);
			date1 = formatter.parse(pEndDate);
			monthDiff = Math.abs(date1.getMonth() - date.getMonth() + (date1.getYear() - date.getYear()) * 12);
			System.out.println("MOnth:" + monthDiff);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (int) monthDiff;
	}

	public static String getMonthEnd(String aDate) {
		// aDate == 31/12/2011
		// Edit By WHA ==> For FEB Month End Case (31/02/2012) instead of
		// (29/02/2011)

		String[] l_date = aDate.split("/");
		int day = Integer.parseInt(l_date[0]);
		int month = Integer.parseInt(l_date[1]);
		int year = Integer.parseInt(l_date[2]);
		day = 1; // Cause We Only Need Month End

		month--; // In Java calendar month is starting from zero.
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(year, month, lastDate);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String ret = df.format(calendar.getTime());
		return ret;
	}

	// Getting Previouse Date ==> PL ==> Date Formate is dd/MM/yyyy
	public static String getPreviousDate(String aDate) {
		// aDate == 01/10/2011
		String[] l_date = aDate.split("/");
		int day = Integer.parseInt(l_date[0]);
		int month = Integer.parseInt(l_date[1]);
		int year = Integer.parseInt(l_date[2]);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		c.add(Calendar.DATE, -1);

		return dateFormat.format(c.getTime());
	}

	// pl end
	// Getting Previous Date ==> WHA ==> Date Format is yyyyMMdd
	public static String getPreviousDateYYYYMMDD(String pDate) {
		int year = Integer.parseInt(pDate.substring(0, 4));
		int month = Integer.parseInt(pDate.substring(4, 6));
		int day = Integer.parseInt(pDate.substring(6, 8));
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		c.add(Calendar.DATE, -1);

		return dateFormat.format(c.getTime());
	}

	public static int getTagEnd(String strMain, String tag, int beginsAt) {
		// look for corresponding end of the tag
		strMain = strMain.toLowerCase();
		tag = tag.toLowerCase();
		int i = 1, op, cl;
		int c = 0;
		try {
			c = beginsAt + strMain.substring(beginsAt).indexOf("<" + tag) + 1;
			while (i > 0 && c < strMain.length()) {
				op = indexOf(strMain, "<" + tag, c);
				cl = indexOf(strMain, "</" + tag, c);
				if ((op != -1) && (op < cl || cl == -1)) {
					c = op + 1;
					i++;
				} else if ((cl != -1) && (cl < op || op == -1)) {
					c = cl + 1;
					i--;
				} else {
					c = strMain.length() + 1;
				}
			}
		} catch (Exception ex) {
			System.out.println("getTagEnd : " + ex.toString());
		}
		return --c;
	}

	public static String getTextBetweenTagsSimple(String strMain, String startTag, String endTag, int beginsAt) {
		// Nested NOT OK; Multi attributes OK;
		return getTextBetweenTagsSimple(strMain, startTag.substring(0, startTag.length() - 1),
				startTag.substring(startTag.length() - 1, startTag.length()), endTag, beginsAt);
	}

	public static String getTextBetweenTagsSimple(String strMain, String startTag, String startTagEnd, String endTag,
			int beginsAt) {
		// Low Level Version main text <main > </main> start from here
		startTag = startTag.toLowerCase();
		startTagEnd = startTagEnd.toLowerCase();
		endTag = endTag.toLowerCase();
		int startAt = 0, endAt = 0;
		String strRet = "";
		try {
			startAt = strMain.toLowerCase().indexOf(startTag, beginsAt); // find
																			// start
																			// tag
			if (startAt >= 0) { // found
				startAt = strMain.toLowerCase().indexOf(startTagEnd, startAt);
				if (startAt >= 0) {
					startAt++;
					endAt = strMain.toLowerCase().indexOf(endTag, startAt);
					if (endAt >= 0) {
						strRet = strMain.substring(startAt, endAt);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return strRet;
	}

	public static String getTextBetweenTagsSpecific(String strMain, String tag, String startPhrase, int beginsAt) {
		// look for specific start
		tag = tag.toLowerCase();
		startPhrase = startPhrase.toLowerCase();
		String ret = "";
		try {
			if (strMain.toLowerCase().substring(beginsAt).contains(startPhrase)) {
				int start = indexOf(strMain, startPhrase, beginsAt);
				int end = getTagEnd(strMain, tag, start);
				ret = strMain.substring(start + startPhrase.length(), end);

			}
		} catch (Exception ex) {
			System.out.println("getTextBetweenTagsSpecific : " + ex.toString());
		}
		return ret;
	}

	public static String getTextBetweenTagsSpecificMultiple(String strMain, String tag, String startPhrase,
			int beginsAt, String seperator) {
		String ret = "";
		ret = getTextBetweenTagsSpecific(strMain, tag, startPhrase, beginsAt);
		// System.out.println( "Multiple of " + tag + " : "+ (multi++) );
		String buffer = " ";
		int i = 0;
		while (buffer.length() > 0 && ++i <= 300) {
			// System.out.println( "Multiple of " + tag + " : "+ (multi++) );
			beginsAt = indexOf(strMain, startPhrase, beginsAt) + buffer.length(); // i
																					// for
																					// \n
																					// to
																					// join
			buffer = getTextBetweenTagsSpecific(strMain, tag, startPhrase, beginsAt);
			ret += seperator + buffer;
		}
		return ret;
	}

	public static boolean hasNumber(String n) {
		for (int i = 0; i < n.length(); i++) {
			if (Character.isDigit(n.charAt(i)))
				return true;
		}
		try {
			Double.valueOf(n).doubleValue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int indexOf(String strMain, String strCode, int beginsAt) {
		// index of a substring after beginning position
		int i = -1;
		if (strMain.substring(beginsAt).toLowerCase().contains(strCode.toLowerCase())) {
			i = beginsAt + strMain.substring(beginsAt).toLowerCase().indexOf(strCode.toLowerCase());
		}
		return i;
	}

	public static String insertSpaceBetPunc(String str) {
		String[] sPunc1 = { "." }; // bcoz of regular expression
		String[] sPunc2 = { ",", ";", ":", "<", ">", "?", "(", ")", "'" };
		for (int i = 0; i < sPunc1.length; i++) {
			str = str.replaceAll("[" + sPunc1[i] + "]", " " + sPunc1[i]) + " ";
		}
		for (int i = 0; i < sPunc2.length; i++) {
			str = str.replace(sPunc2[i], " " + sPunc2[i] + " ");
		}
		return str;
	}

	public static boolean isDate(String p) {
		return (toDate(p) != null);
	}

	public static boolean isEmpty(String p) {
		if (p == null || trimText(p).equals(""))
			return true;
		else
			return false;
	}

	public static boolean isFYearEnd(String pDate) {
		boolean ret = false;
		String day = pDate.substring(6, 8);
		String month = pDate.substring(4, 6);
		if (day.equals("31") && month.equals("03")) {
			ret = true;
		}
		return ret;
	}

	public static boolean isHalfYearEnd(String pDate) {
		boolean ret = false;
		String day = pDate.substring(6, 8);
		String month = pDate.substring(4, 6);
		if (day.equals("30") && month.equals("09")) {
			ret = true;
		}
		return ret;
	}

	public static boolean isLower(String str) {
		if (str.equals(str.toLowerCase()))
			return true;
		else
			return false;
	}

	public static boolean isNumber(String n) {
		if (Character.isDigit(n.charAt(0)))
			return true;
		try {
			Double.valueOf(n).doubleValue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isQuarterEnd(String pDate) {
		boolean ret = false;
		String day = pDate.substring(6, 8);
		String month = pDate.substring(4, 6);
		if ((day.equals("31") && month.equals("03")) || (day.equals("30") && month.equals("06"))
				|| (day.equals("30") && month.equals("09")) || (day.equals("31") && month.equals("12"))) {
			ret = true;
		}
		return ret;
	}

	// Checking Saturday Sunday ==> WHA
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

	public static String leadSpaces(long p, int size) {
		return leadSpaces("" + p, size);
	}

	public static String leadSpaces(String p, int size) {
		String ret = p;
		for (int i = p.length(); i < size; i++) {
			ret = " " + ret;
		}
		return ret;
	}

	public static String leadZeros(long p, int size) {
		return leadZeros("" + p, size);
	}

	public static String leadZeros(String p, int size) {
		String ret = p;
		for (int i = p.length(); i < size; i++) {
			ret = "0" + ret;
		}
		return ret;
	}

	public static String lTrim(String strText) {
		String whitespace = new String(" \t\n\r "); //
		int j = 0;
		while (j < strText.length() && whitespace.indexOf(strText.charAt(j)) != -1)
			j++;
		strText = strText.substring(j, strText.length());
		return strText;
	}

	public static byte numByte(String p) {
		byte ret = 0;
		try {
			ret = Byte.parseByte(p);
		} catch (Exception e) {
		}
		return ret;
	}

	public static double numDbl(String p) {
		double ret = 0;
		try {
			ret = Double.parseDouble(p);
		} catch (Exception e) {
		}
		return ret;
	}

	public static int numInt(String p) {
		int ret = 0;
		try {
			ret = Integer.parseInt(p);
		} catch (Exception e) {
		}
		return ret;
	}

	public static long numLong(String p) {
		long ret = 0;
		try {
			ret = Long.parseLong(p);
		} catch (Exception e) {
		}
		return ret;
	}

	public static boolean onlyThem(String str) {
		String chars = ".,!?\r\n\t ";
		return onlyThem(str, chars);
	}

	public static boolean onlyThem(String str, String chars) {
		boolean onlythem = true;
		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i + 1);
			boolean thisOneHasOthers = true;
			for (int j = 0; j < chars.length(); j++) {
				String c = chars.substring(j, j + 1);
				if (c.equals(s))
					thisOneHasOthers = false;
			}
			if (thisOneHasOthers)
				onlythem = false;
		}
		return onlythem;
	}

	public static String printAR(String str[]) {
		return printAR(str, "");
	}

	public static String printAR(String str[], String enclose) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			if (!sb.toString().equals(""))
				sb.append(", ");
			sb.append(enclose + str[i] + enclose);
		}
		return sb.toString();
	}

	public static void printARL(ArrayList<String> arl, String strSep) {
		for (int i = 0; i < arl.size(); i++) {
			System.out.print(arl.get(i) + strSep);
		}
		System.out.println();
	}

	public static void printTM(TreeMap<String, Integer> tm, String strSep) {
		@SuppressWarnings("rawtypes")
		Iterator i = tm.keySet().iterator();
		while (i.hasNext()) {
			String obj = (String) i.next();
			System.out.print(obj + "\t" + tm.get(obj) + strSep);
		}
		System.out.println();
	}

	public static String removeTags(String strMain, String start, String end) {
		// Old Version
		// only for fixed Tag; Multi attributes NOT OK
		// Nested Tag = NOT OK
		String str = new String(strMain);
		try {
			int i = 0;
			while (str.toLowerCase().contains(start) && i++ < 999) {
				int s = str.toLowerCase().indexOf(start);
				int e = str.toLowerCase().indexOf(end, s) + end.length();
				String remove = str.substring(s, e);
				if (str.contains(remove))
					str = str.replace(remove, "");
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return str;
	}

	public static String removeTextBetweenTags(String strMain, String tag, int beginsAt) {
		// Eg. remove table in a give segment of text.
		// Nested Tag = OK; Multi attributes OK
		tag = tag.toLowerCase();
		String ret = strMain;
		try {
			if (strMain.toLowerCase().substring(beginsAt).contains("</" + tag)) {
				int start = indexOf(strMain, "<" + tag, beginsAt);
				int end = getTagEnd(strMain, tag, start);
				ret = strMain.substring(0, start) + strMain.substring(end + ("</" + tag + ">").length());
			}
		} catch (Exception ex) {
			System.out.println("removeThisTag : " + ex.toString());
		}
		return ret;
	}

	public static String removeTextBetweenTagsMultiple(String strMain, String tag, int beginsAt) {
		// Multiple version of String removeTextBetweenTags
		tag = tag.toLowerCase();
		String ret = strMain;
		try {
			boolean go = true;
			while (go) {
				String res = removeTextBetweenTags(ret, tag, beginsAt);
				if (res.length() == ret.length()) {
					go = false;
				} else {
					ret = res;
				}
			}
		} catch (Exception ex) {
			System.out.println("removeTheseTags* : " + ex.toString());
		}
		return ret;
	}

	// aph 11/09/2014
	// MMPPM
	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	public static String replaceCode(String pText, String pThis, String pThat) {

		pText = (" " + pText).replaceAll(" (?i)" + pThis + " ", " " + pThat + " ");
		pText = (" " + pText).replaceAll(" (?i)" + pThis + "'s", " " + pThat + "'s");
		pText = (" " + pText).replaceAll(" (?i)" + pThis + "\\.", " " + pThat + "\\.");
		pText = (" " + pText).replaceAll(" (?i)" + pThis + ",", " " + pThat + ",");
		pText = (" " + pText).replaceAll(" (?i)" + pThis + ":", " " + pThat + ":");
		pText = (" " + pText).replaceAll(" (?i)" + pThis + "'", " " + pThat + "'");
		pText = (" " + pText).replaceAll(" (?i)" + pThis + "\"", " " + pThat + "\"");
		if (pThis.length() > 7 || pThis.toLowerCase().contains("n't") || pThis.toLowerCase().endsWith("nt"))
			pText = (pText).replaceAll("(?i)" + pThis + "", "" + pThat + "");
		return pText.substring(1, pText.length());
	}

	public static String replaceCodes(String strText, String arlCodes[], String pCode) {
		replacee = new ArrayList<String>();
		for (int i = 0; i < arlCodes.length; i++) {
			ArrayList<String> arlVariants = new ArrayList<String>();
			arlVariants.add(arlCodes[i]); // all original
			String[] subNames = arlCodes[i].split(" ");
			if (subNames.length > 1) {
				for (int k = 0; k < subNames.length; k++) {
					arlVariants.add(subNames[k]);
				}
			}
			strText = " " + strText + " ";
			for (int j = 0; j < arlVariants.size(); j++) {
				String ori = strText;
				// strText= strText.replaceAll(" (?i)"+arlVariants.get(j),
				// pCode+Fmt((i*10)+j,3));
				strText = replaceCode(strText, arlVariants.get(j), pCode + Fmt((i * 100) + j, 5));
				if (!strText.equalsIgnoreCase(ori))
					replacee.add(arlVariants.get(j));
			}

		}
		return strText;
	}

	public static double round0decimals(double p) {
		return round0decimals(p, RoundingMode.HALF_EVEN);
	}

	public static double round0decimals(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}
	
	public static double round1decimalsHALFDOWN(double p){
		return round1decimalsHALFDOWN(p, RoundingMode.HALF_DOWN);
	}	
	public static double round1decimalsHALFDOWN(double pAmount, RoundingMode mode){
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.0");
		l_df.setRoundingMode(mode);
		
		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round2decimals(double p) {
		return round2decimals(p, RoundingMode.HALF_EVEN);
	}

	public static double round2decimals(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.00");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round2decimalsDOWN(double p) {
		return round2decimalsDOWN(p, RoundingMode.DOWN);
	}

	public static double round2decimalsDOWN(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.00");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round3decimals(double p) {
		return round3decimals(p, RoundingMode.HALF_EVEN);
	}

	public static double round3decimals(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.000");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round3decimalsHALFDOWN(double p) {
		return round3decimalsHALFDOWN(p, RoundingMode.HALF_DOWN);
	}

	public static double round3decimalsHALFDOWN(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.000");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round3decimalsHALFUP(double p) {
		return round3decimalsHALFUP(p, RoundingMode.HALF_UP);
	}

	public static double round3decimalsHALFUP(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.000");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round4decimals(double p) {
		return round4decimals(p, RoundingMode.HALF_EVEN);
	}

	public static double round4decimals(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.0000");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round4decimalsDOWN(double p) {
		return round4decimalsDOWN(p, RoundingMode.DOWN);
	}

	public static double round4decimalsDOWN(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.0000");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double round4decimalsHALFDOWN(double p) {
		return round4decimalsHALFDOWN(p, RoundingMode.HALF_DOWN);
	}

	public static double round4decimalsHALFDOWN(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0.0000");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		return l_result;
	}

	public static double roundUP(double p) {
		return roundUP(p, RoundingMode.CEILING);
	}

	public static double roundUP(double pAmount, RoundingMode mode) {
		double l_result = 0;
		DecimalFormat l_df = new DecimalFormat("###0");
		l_df.setRoundingMode(mode);

		l_result = Double.valueOf(l_df.format(pAmount));
		System.out.println("Ceiling : " + l_result);
		return l_result;
	}

	public static int roundUPtoInt(double p) {
		return roundUPtoInt(p, RoundingMode.CEILING);
	}

	public static int roundUPtoInt(double pAmount, RoundingMode mode) {

		BigDecimal bd = new BigDecimal(pAmount);
		bd = bd.setScale(0, RoundingMode.CEILING);

		return bd.intValue();
	}

	public static String rTrim(String strText) {
		String whitespace = new String(" \t\n\r ");
		;
		int j = strText.length() - 1;
		while (j >= 0 && whitespace.indexOf(strText.charAt(j)) != -1)
			j--;
		strText = strText.substring(0, j + 1);
		return strText;
	}

	public static String sep(double p) {
		return sep(p, "__|");
	}

	public static String sep(double p, String sym) {
		return p + sym;
	}

	public static String sep(long p) {
		return sep(p, "__|");
	}

	public static String sep(long p, String sym) {
		return p + sym;
	}

	public static String sep(String p) {
		return sep(p, "__|");
	}

	public static String sep(String p, String sym) {
		if (p == null)
			p = "__|";
		else
			p = p.replace("\\_\\_\\|", "") + sym;
		return p;
	}

	public static String serializeARL(ArrayList<String> arl, String strSep) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arl.size(); i++) {
			sb.append(arl.get(i) + strSep);
		}
		return sb.toString();
	}

	public static String[] split(String str, String s, int size) {
		String[] ret = new String[++size];
		String[] tmp = str.split(s);
		for (int i = 0; i < size; i++) {
			ret[i] = str(tmp, i);
		}
		return ret;
	}

	public static String str(String p) {
		if (p == null)
			return "";
		else
			return p;
	}

	public static String str(String[] arr, int i) {
		if (arr.length <= i)
			return "";
		else if (arr[i] != null)
			return arr[i];
		else
			return "";
	}

	public static String stripTags(String strMain) {
		StringBuffer sb = new StringBuffer();
		if (strMain.indexOf("<") >= 0) {
			int s = 0;
			int e = 0;
			while (s < strMain.length() - 1 && e < strMain.length() - 1 && e >= 0) {
				s = strMain.indexOf("<", e);
				if (s >= 0) {
					String temp = strMain.substring(e, s);
					sb.append(" " + temp);
				} else {
					String temp = strMain.substring(e, strMain.length());
					sb.append(" " + temp);
					break;
				}

				e = strMain.indexOf(">", s);
				if (e == -1) {
					break;
				}
				e++;
			}
		} else
			sb.append(strMain);
		return sb.toString();
	}

	public static Date toDate(String p) {
		return toDate(p, "dd/MM/yyyy");
	}

	public static Date toDate(String p, String fmt) {
		Date ret = null;
		try {
			DateFormat full = new SimpleDateFormat(fmt); // based on system
															// configuration
			new SimpleDateFormat("dd");
			new SimpleDateFormat("MM");
			new SimpleDateFormat("yyyy");
			ret = full.parse(p); // new Date(System.currentTimeMillis());
			// System.out.println("Date: " + full.format(ret) + " ["+
			// d.format(ret) + "-"+ m.format(ret) + "-"+ y.format(ret) + "] ");
		} catch (Exception e) {
		}
		return ret;
	}

	public static String toFile(String strURL) {
		strURL = trimText(strURL);
		return encodeURL(strURL) + ".xml";
	}

	public static String toMDate(Date p) {
		String ret = "";
		try {
			DateFormat mit = new SimpleDateFormat("yyyyMMdd");
			new SimpleDateFormat("dd");
			new SimpleDateFormat("MM");
			new SimpleDateFormat("yyyy");
			ret = mit.format(p);
			// System.out.println("Date: " + ret + " ["+ d.format(p) + "-"+
			// m.format(p) + "-"+ y.format(p) + "] ");
		} catch (Exception e) {
		}
		return ret;
	}

	public static String toURL(String strFileName) {
		try {
			// String prefix="^_";
			// if (strFileName.contains(prefix))
			// s=strFileName.indexOf(prefix)+prefix.length();
			strFileName = decodeURL(strFileName);
		} catch (Exception x) {
		}
		return strFileName;
	}

	public static double trancate(double x, int digit) {
		long de = 1;
		for (int i = 0; i < digit; i++) {
			de *= 10;
		}
		long y = (long) (x * de);
		double z = ((double) y) / de;

		return z;
	}

	public static String trimAll(String s) {
		return trimLeft(s).trim();
	}

	public static String trimLeft(String s) {
		String whitespace = new String(" \t\n\r "); //
		int j = 0;
		while (j < s.length() && whitespace.indexOf(s.charAt(j)) != -1)
			j++;
		s = s.substring(j, s.length());
		return s;
	}

	public static String trimText(String strText) {
		String s = rTrim(strText);
		s = lTrim(s);
		return s;
	}

	public static String[] uniqueAR(String[] s) {
		ArrayList<String> arlStr = new ArrayList<String>();

		for (int i = 0; i < s.length; i++) {
			if (!arlStr.contains(s[i])) {
				arlStr.add(s[i]);
			}
		}
		String[] r = new String[arlStr.size()];
		r = arlStr.toArray(r);
		java.util.Arrays.sort(r);
		return r;
	}

	public static ArrayList<String> uniqueARL(ArrayList<String> arl) {
		return getAR2ARL(uniqueAR(getARL2AR(arl)));
	}

	public static String[] WhichHalfYear(String aDate) {
		// aDate ==> yyyyMMdd 20130508
		int l_Month = Integer.parseInt(aDate.substring(4, 6));
		String l_Year = aDate.substring(0, 4);
		String[] l_DateArr = new String[2];
		if (l_Month >= 4 && l_Month <= 9) {
			l_DateArr[0] = "01/04/" + l_Year;
			l_DateArr[1] = "30/09/" + l_Year;
		} else if (l_Month >= 10 && l_Month <= 12) {
			l_DateArr[0] = "01/10/" + l_Year;
			l_DateArr[1] = "31/03/" + (Integer.parseInt(l_Year) + 1);
		} else if (l_Month >= 1 && l_Month <= 3) {
			l_DateArr[0] = "01/10/" + (Integer.parseInt(l_Year) - 1);
			l_DateArr[1] = "31/03/" + l_Year;
		}
		return l_DateArr;
	}

	public static String[] WhichQuarter(String aDate) {
		// aDate ==> yyyyMMdd 20130508
		int l_Month = Integer.parseInt(aDate.substring(4, 6));
		String l_Year = aDate.substring(0, 4);
		String[] l_DateArr = new String[2];
		if (l_Month >= 4 && l_Month <= 6) {
			l_DateArr[0] = "01/04/" + l_Year;
			l_DateArr[1] = "30/06/" + l_Year;
		} else if (l_Month >= 7 && l_Month <= 9) {
			l_DateArr[0] = "01/07/" + l_Year;
			l_DateArr[1] = "30/09/" + l_Year;
		} else if (l_Month >= 10 && l_Month <= 12) {
			l_DateArr[0] = "01/10/" + l_Year;
			l_DateArr[1] = "31/12/" + l_Year;
		} else if (l_Month >= 1 && l_Month <= 3) {
			l_DateArr[0] = "01/01/" + l_Year;
			l_DateArr[1] = "31/03/" + l_Year;
		}
		return l_DateArr;
	}

	// MM
	public String[] getMonthofQuarter(String month) {
		String[][] arr = { { "01", "02", "03" }, { "04", "05", "06" }, { "07", "08", "09" }, { "10", "11", "12" } };
		String[] result = new String[3];
		for (int i = 0; i < arr.length; i++) {
			String[] row = arr[i];
			if (row != null) {
				for (int j = 0; j < row.length; j++) {
					if (month.equals(row[j])) {
						result = row;
						break;
					}
				}
			}
		}

		return result;
	}
	
	public static String getCurrentDateyyyyMMdd() {
		    String ret = "";
		    DateFormat mit = new SimpleDateFormat("yyyyMMdd");
		    ret = mit.format(getDate());
		    return ret;
	}
	
	public static String formatNumberwithComma(double pAmount) {
		String l_result="0.00";
		DecimalFormat l_df=new DecimalFormat("#,##0.00");
		l_result=l_df.format(pAmount);
		return l_result;
	}
	
	public static String[] WhichQuarter(String aDate, BudgetYearData l_BData) {
		// aDate ==> yyyyMMdd 20130508
		int l_Month =Integer.parseInt(aDate.substring(4, 6));
		String l_Year = aDate.substring(0,4);
		String[] l_DateArr = new String[2];
		if(l_Month >= l_BData.getpN2() && l_Month <= l_BData.getpN3()){ 
			l_DateArr[0] = l_BData.getpT1().substring(2, 4) + "/" + l_BData.getpT1().substring(0, 2) + "/" + l_Year;
			l_DateArr[1] = l_BData.getpT2().substring(2, 4) + "/" + l_BData.getpT2().substring(0, 2) + "/" + l_Year;
		} else if(l_Month >= l_BData.getpN4() && l_Month <= l_BData.getpN5()){
			l_DateArr[0] = l_BData.getpT3().substring(2, 4) + "/" + l_BData.getpT3().substring(0, 2) + "/" + l_Year;
			l_DateArr[1] = l_BData.getpT4().substring(2, 4) + "/" + l_BData.getpT4().substring(0, 2) + "/" + l_Year;
		}else if(l_Month >= l_BData.getpN6() && l_Month <= l_BData.getpN7()){
			l_DateArr[0] = l_BData.getpT5().substring(2, 4) + "/" + l_BData.getpT5().substring(0, 2) + "/" + l_Year;
			l_DateArr[1] = l_BData.getpT6().substring(2, 4) + "/" + l_BData.getpT6().substring(0, 2) + "/" + l_Year;
		}else if(l_Month >= l_BData.getpN8() && l_Month <= l_BData.getpN9()){
			l_DateArr[0] = l_BData.getpT7().substring(2, 4) + "/" + l_BData.getpT7().substring(0, 2) + "/" + l_Year;
			l_DateArr[1] = l_BData.getpT8().substring(2, 4) + "/" + l_BData.getpT8().substring(0, 2) + "/" + l_Year;
		}			
		return l_DateArr;		  
	}
	
	public static String[] WhichHalfYear(String aDate, BudgetYearData l_BData) {
		// aDate ==> yyyyMMdd 20130508
		int l_Month =Integer.parseInt(aDate.substring(4, 6));
		String l_Year = aDate.substring(0,4);
		String[] l_DateArr = new String[2];
		if(l_Month >= l_BData.getpN2() && l_Month <= l_BData.getpN3()){ 
			l_DateArr[0] = l_BData.getpT1().substring(2, 4) + "/" + l_BData.getpT1().substring(0, 2) + "/" + l_Year;
			l_DateArr[1] = l_BData.getpT2().substring(2, 4) + "/" + l_BData.getpT2().substring(0, 2) + "/" + l_Year;
		} else if(l_Month >= l_BData.getpN4() && l_Month <= l_BData.getpN5()){
			l_DateArr[0] = l_BData.getpT3().substring(2, 4) + "/" + l_BData.getpT3().substring(0, 2) + "/" + l_Year;
			l_DateArr[1] = l_BData.getpT4().substring(2, 4) + "/" + l_BData.getpT4().substring(0, 2) + "/" + (Integer.parseInt(l_Year)+1);
		}else if(l_Month >= l_BData.getpN6() && l_Month <= l_BData.getpN7()){
			l_DateArr[0] = l_BData.getpT3().substring(2, 4) + "/" + l_BData.getpT3().substring(0, 2) + "/" + (Integer.parseInt(l_Year)-1);
			l_DateArr[1] = l_BData.getpT4().substring(2, 4) + "/" + l_BData.getpT4().substring(0, 2) + "/" + l_Year;
		}
		return l_DateArr;
	}
	
	public static String getStartDay(String aDate){
		
		String day = aDate.substring(6, 8);
		String month = aDate.substring(4, 6);
		String year = aDate.substring(0, 4);
		String ret = "";			
		if(!day.equals("01")){
			ret = "01/" + month + "/" + year;
		}else{
			ret = day + "/" + month + "/" + year ;
		}
		return ret;
	}
	
	public static String formatDDMMYYYY2MIT(String p) {
    	String ret="";
    	try{
    		int y = Integer.parseInt(   p.split("/")[2]  );
    		if (y<25) y=y+2000; else if (y<100) y=y+1900; 
    		int m = Integer.parseInt(   p.split("/")[1]  );
    		int d = Integer.parseInt(   p.split("/")[0]  );
    		String dd = leadZeros( ""+d, 2);
    		String mm = leadZeros( ""+m, 2);
    		String yyyy = leadZeros( ""+y, 4);
    		ret = yyyy + mm +dd ;
    	}catch (Exception exp){}
    	return ret;
    }
	
	public static Date getQuarterEndDateInDate(String pDate, BudgetYearData l_Bdata){
		//pDate in yyyyMMdd			
		int l_QuarterEndMonth = 0;
		int l_year = Integer.parseInt(pDate.substring(0, 4));
		int l_month = Integer.parseInt(pDate.substring(4, 6));
		
		Calendar l_Calendar = Calendar.getInstance();
		l_Calendar.clear();
					
		if(l_month >= l_Bdata.getpN8() && l_month <= l_Bdata.getpN9()){
			l_QuarterEndMonth = l_Bdata.getpN9();
		}else if(l_month >= l_Bdata.getpN2() && l_month <= l_Bdata.getpN3()){
			l_QuarterEndMonth = l_Bdata.getpN3();
		}else if(l_month >= l_Bdata.getpN4() && l_month <= l_Bdata.getpN5()){
			l_QuarterEndMonth = l_Bdata.getpN5();
		}else if(l_month >= l_Bdata.getpN6() && l_month <= l_Bdata.getpN7()){
			l_QuarterEndMonth = l_Bdata.getpN7();
		}			
		
		l_Calendar.set(l_year, l_QuarterEndMonth, 01);
		l_Calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		Date l_QuarterEndDate = l_Calendar.getTime();
		return l_QuarterEndDate;
	}
	
	public static Date getFirstDateInDate(String pDate){
		//pDate in yyyyMMdd	
		Calendar l_Calendar = Calendar.getInstance();
		int l_year = Integer.parseInt(pDate.substring(0, 4));
		int l_month = Integer.parseInt(pDate.substring(4, 6));
		l_month--;
		l_Calendar.clear();
		l_Calendar.set(l_year, l_month, 01);
					
		Date l_StartDate = l_Calendar.getTime();
		
		return l_StartDate;
	}
	
	public static Boolean IsLeapYear(int ayear){
		Boolean l_result = false;		
		if((ayear % 4 == 0) && (ayear % 100 != 0) || (ayear % 400 == 0)){
			l_result = true;			
		}
		return l_result;
	}
	
	  public static Date formatYYYYMMDDToDate(String pDate){
			//pDate in yyyyMMdd	
			int l_year = Integer.parseInt(pDate.substring(0, 4));
			int l_month = Integer.parseInt(pDate.substring(4, 6));
			int l_date = Integer.parseInt(pDate.substring(6, 8));
			
			Calendar l_Calendar = Calendar.getInstance();
			l_Calendar.clear();
			l_Calendar.set(l_year, --l_month, l_date);
			
			Date l_Date = l_Calendar.getTime();
			return l_Date;
		}
	  
	  public static int getDaysBetweentwoDates(Date pStartDate, Date pEndDate){	    	
	        int days = 0;			
	        Calendar l_Start = Calendar.getInstance();
	        l_Start.setTime(pStartDate);
	        Calendar l_End = Calendar.getInstance();
	        l_End.setTime(pEndDate);
	        days = daysBetween(l_Start, l_End);	        
	        return days;
	    }
	  
	  public static double RoundHALFUP(double amt){
			DecimalFormat l_df=new DecimalFormat("###0.00");
			l_df.setRoundingMode(RoundingMode.HALF_UP);
			double bfround = amt*100;
			String l_amtformat =l_df.format(bfround);
			String[] lstamt = l_amtformat.split("\\.");
			double l_amt = 0;
			if(lstamt.length>=1){
				l_amt = Double.valueOf(l_amtformat.split("\\.")[1]);
				if(l_amt>=50 && l_amt<60){
					l_amt = Double.valueOf(l_df.format(bfround))+.1;
				} else 
					l_amt = Double.valueOf(l_df.format(bfround));
			} else 
				l_amt = Double.valueOf(l_df.format(bfround));
			return Double.valueOf(l_df.format(l_amt/100));
		}
	  
	  public static String getAccountStatusDescription(AccountData object){
			String ret  = "";
			// Account Status
	        switch (object.getStatus())
	        {
	            // New (or) Active
	            case 0:
	                if (SharedUtil.formatMIT2DateStr(object.getLastTransDate()).equals("01/01/1900"))
	                    ret = "New";
	               else
	                    ret = "Active";
	                break;

	            // Suspend
	            case 1:
	                ret = "Suspend";
	                break;

	            // Closed Pending
	            case 2:
	                ret = "Close Pending";
	                break;

	            // Closed
	            case 3:
	            	ret = "Closed";
	                break;

	            // Stopped Payment
	            case 7:
	            	ret = "StoppedPayment";
	                break;

	            default:
	            	ret = "";
	                break;
	        }
	        return ret;
	    }
}
