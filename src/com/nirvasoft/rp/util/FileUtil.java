package com.nirvasoft.rp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/* 
TUN THURA THET 2011 04 21
*/
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.output.FileWriterWithEncoding;

import com.nirvasoft.rp.dao.DAOManager;

/**
 * 
 * TUN THURA THET @ NTU 2007-2009
 */
public class FileUtil {
	static public String LogFile;

	public static String datetoString() {
		String l_date = "";
		java.util.Date l_Date = new java.util.Date();
		SimpleDateFormat l_sdf = new SimpleDateFormat("yyyy-MM-dd");
		l_date = l_sdf.format(l_Date);

		return l_date;
	}

	public static boolean deleteFile(String f) {
		boolean isdeleted = false;
		File file = new File(f);
		try {
			isdeleted = file.delete();

		} catch (Exception e) {
			System.out.println("File Delete: " + e.toString());
		}
		return isdeleted;
	}

	public static boolean deleteFiles(String d) {
		boolean isdeleted = false;
		File dir = new File(d);
		String[] list = dir.list();
		File file;
		if (list.length == 0)
			return isdeleted;
		for (int i = 0; i < list.length; i++) {
			file = new File(d + list[i]);
			isdeleted = file.delete();
		}
		return isdeleted;
	}

	public static boolean deleteFiles(String d, String start) {
		boolean isdeleted = false;
		File dir = new File(d);
		String[] list = dir.list();
		File file;
		if (list.length == 0)
			return isdeleted;
		for (int i = 0; i < list.length; i++) {
			if (list[i].startsWith(start)) {
				file = new File(d + list[i]);
				isdeleted = file.delete();
				System.out.println("deleted: " + d + list[i]);
			}
		}
		return isdeleted;
	}

	public static String[] getFileArray(String strPath) {
		ArrayList<String> arl = getFileList(strPath);
		String[] a = new String[arl.size()];
		a = arl.toArray(a);
		return a;
	}

	public static ArrayList<String> getFileList(String strPath) {
		ArrayList<String> arlFiles = null;
		try {
			arlFiles = new ArrayList<String>();
			File f = new File(strPath);
			File[] fileList = f.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					arlFiles.add(fileList[i].getAbsolutePath());
				} else {
					ArrayList<String> arlSubFiles = getFileList(fileList[i].getAbsolutePath());
					arlFiles.addAll(arlSubFiles);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return arlFiles;
	}

	public static String getMessageDescription(String messageCode) {

		String retMsgDesc = "";
		try {
			ArrayList<String> arl = FileUtil.readList(DAOManager.AbsolutePath + "/reference/" + "MessageCode.txt");
			for (int i = 0; i < arl.size(); i++) {
				if (!arl.get(i).equals("")) {

					if (!arl.get(i).equals("")) {

						if (arl.get(i).split(":")[0].equalsIgnoreCase(messageCode)) {
							System.out.println("msgdesc" + arl.get(i).split(":")[1]);
							retMsgDesc = arl.get(i).split(":")[1];
						}
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retMsgDesc;
	}

	public static String getTime() {
		Date d = new Date(System.currentTimeMillis()); // liftOffApollo11.getTime();
		DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		String s1 = df1.format(d);
		return s1;
	}

	public static void log(String strContent) {
		log(strContent, LogFile);
	}

	public static void log(String strContent, String File) {
		// System.out.print(strContent);
		writeText(File, strContent, true);
	}

	public static void logEmpty() {
		writeText(LogFile, "", false);
	}

	public static void logln(String strContent) {
		logln(strContent, LogFile);
	}

	public static void logln(String strContent, String File) {
		log(strContent + "\r\n", File);
	}

	public static void logTime(String strMsg) {
		logTime(strMsg, LogFile);
	}

	public static void logTime(String strMsg, String File) {
		logln(getTime() + " : " + strMsg + "\r\n", File);
	}

	public static void makeDir(String d) {
		try {
			new File(d).mkdirs();
		} catch (Exception e) {
		}
	}

	public static BufferedReader openReader(String strPath) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(strPath));
		} catch (Exception e) {
		}
		return in;
	}

	public static PrintWriter openWriter(String strPath, boolean bAppend) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(strPath, bAppend));
		} catch (IOException e) {
		}
		return out;
	}

	public static String readALine(String strPath) {
		StringBuffer sbTemp = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(strPath));
			String str;
			while ((str = in.readLine()) != null) {
				sbTemp.append(str);
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sbTemp.toString();
	}

	public static ArrayList<String> readFile(String aFilePath) throws Exception {
		ArrayList<String> l_result = new ArrayList<String>();

		try {
			BufferedReader l_BufferedReader = new BufferedReader(new FileReader(aFilePath));
			String l_str = "";
			while ((l_str = l_BufferedReader.readLine()) != null) {
				l_result.add(l_str);
			}
			l_BufferedReader.close();

		} catch (Exception e) {
			throw e;
		}

		return l_result;
	}

	public static ArrayList<String> readList(String strPath) {
		return readList(strPath, false);
	}

	public static ArrayList<String> readList(String strPath, boolean stem) {
		ArrayList<String> arlTemp = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(strPath));
			String str;
			while ((str = in.readLine()) != null) {
				String strOK = str;
				if (stem) {
					strOK = str;
				}
				if (!str.equals(""))
					arlTemp.add(strOK);
			}
			in.close();
		} catch (Exception e) {
			System.out.println("Error @ readList" + e.getMessage());
		}
		return arlTemp;
	}

	public static String readln(BufferedReader in) {
		String str = "";
		try {
			str = in.readLine();
		} catch (Exception e) {
		}
		return str;
	}

	public static ArrayList<String> readTable(String pTable) {
		ArrayList<String> arlRecords = null;
		try {
			arlRecords = new ArrayList<String>();
			File f = new File(pTable);
			File[] fileList = f.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					String fname = fileList[i].getAbsolutePath();
					// System.out.println(fname);
					if (fname.endsWith("txt"))
						arlRecords.add(readALine(fname) + "__|" + fname);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return arlRecords;
	}

	public static String readText(String strPath) {
		StringBuffer sbTemp = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(strPath));
			String str;
			while ((str = in.readLine()) != null) {
				sbTemp.append(str + "\r");
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sbTemp.toString();
	}

	public static String runFile(String exe) {
		// setPath();
		String sbs = new String();
		StringBuffer sb = new StringBuffer();
		try {
			StringBuffer buff = new StringBuffer();
			String modelCommand = exe;
			Runtime r1 = Runtime.getRuntime();
			Process p = r1.exec(modelCommand);
			BufferedInputStream bufIn = new BufferedInputStream(p.getInputStream());

			int ch;
			while ((ch = bufIn.read()) > -1) {
				buff.append((char) ch);
			}
			bufIn.close();
			sbs = buff.toString();
			System.out.println(sbs);
			sb.append(sbs);
			System.out.println(sbs);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return sb.toString();
	}

	public static void writeALine(String strPath, String strContent, boolean bAppend) {
		writeText(strPath, strContent + (char) 13 + (char) 10, bAppend);
	}

	public static void writeList(String strPath, ArrayList<String> arlContent, boolean bAppend) {
		StringBuffer sbTemp = new StringBuffer();
		for (int i = 0; i < arlContent.size(); i++) {
			sbTemp.append(arlContent.get(i) + (char) 13 + (char) 10);
		}
		writeText(strPath, sbTemp.toString(), bAppend);
	}

	public static void writeln(String strContent, PrintWriter out) {
		try {
			out.println(strContent);
		} catch (Exception e) {
		}
	}

	public static void writeTable(String pTable, String id, String data) {
		try {
			writeText(pTable + "/" + id + ".txt", data, false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void writeText(String strPath, String strContent, boolean bAppend) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriterWithEncoding(strPath, StandardCharsets.UTF_8, bAppend));
			out.write(strContent);
			out.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	public String hmacSha1(String value, String key) {
		try {
			// Get an hmac_sha1 key from the raw key bytes
			byte[] keyBytes = key.getBytes("UTF-8");
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

			// Get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			// Compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(value.getBytes("UTF-8"));

			// Convert raw bytes to Hex
			byte[] hexBytes = new Hex().encode(rawHmac);
			// Covert array of Hex bytes to a String

			return new String(hexBytes, "UTF-8").toUpperCase();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}