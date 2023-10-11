package com.nirvasoft.rp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedUtil {
	public static String formatDate2DDMMYYYY(Date d) {
		String ret = "";
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			ret = f.format(d);
		} catch (Exception ex) {
		}
		return ret;
	}

	public static String formatDateStr2MIT(String p) {
		return formatDDMMYYYY2MIT(p); // <<<<<<<<<<<<<<<<<<<<<<<<<<<< COFIG
	}

	public static String formatDBDate2MIT(String p) {
		return formatYYYYMMDD2MIT(p); // <<<<<<<<<<<<<<<<<<<<<<<<<<<< COFIG
	}

	public static String formatDBDateTime2MIT(String p) {
		return formatYYYYMMDDHHMMSS2MIT(p); // <<<<<<<<<<<<<<<<<<<<<<<<<<<<
											// COFIG
	}

	public static String formatDDMMYYYY2MIT(String p) {
		String ret = "";
		try {
			int y = Integer.parseInt(p.split("/")[2]);
			if (y < 25)
				y = y + 2000;
			else if (y < 100)
				y = y + 1900;
			int m = Integer.parseInt(p.split("/")[1]);
			int d = Integer.parseInt(p.split("/")[0]);
			String dd = leadZeros("" + d, 2);
			String mm = leadZeros("" + m, 2);
			String yyyy = leadZeros("" + y, 4);
			ret = yyyy + mm + dd;
		} catch (Exception exp) {
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

	public static String formatYYYYMMDDHHMMSS2MIT(String p) {
		String ret = "";
		try {
			if (p.length() >= 10) {
				ret = p.replaceAll("-", "");
			} else {
				ret = "19000101 00:00:00";
			}

		} catch (Exception exp) {
		}
		return ret;
	}

	public static String getCurrentDate() {
		// String pattern = "dd/MM/yyyy";
		// SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date d = new Date();
		return formatDate2DDMMYYYY(d);
		// return format.format(new Date()).substring(1);
	}

	public static boolean isNumber(String n) {
		if (Character.isDigit(n.charAt(0)))
			return true;
		else
			return false;
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

}
