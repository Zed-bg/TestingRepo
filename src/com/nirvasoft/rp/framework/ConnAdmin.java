package com.nirvasoft.rp.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.shared.Constant;
import com.nirvasoft.rp.util.FileUtil;
import com.nirvasoft.rp.util.ReadFile;
import com.nirvasoft.rp.util.ServerGlobal;
import com.nirvasoft.rp.util.ServerUtil;

import password.DESedeEncryption;

public class ConnAdmin {

	public static String servername = "";

	public static String port = "";
	public static String instance = "";
	public static String dbname = "";
	public static String dbUsr = "";
	public static String dbPwd = "";
	public static String connType = "";
	static String path = "";
	static String url = "";
	static DESedeEncryption myEncryptor;
	static String driver = "", userID = "", password = "";
	

	

	public static void readConnection() {		
		try {
			myEncryptor = new DESedeEncryption();
			ArrayList<String> oracleConnList;
			oracleConnList = ReadFile.readConnection(DAOManager.AbsolutePath + "WEB-INF//data//ConnectionConfig.txt");
			if (oracleConnList.size() > 0) {
				driver = oracleConnList.get(0).split("Driver:")[1];
				url = oracleConnList.get(1).split("URL:")[1];
				userID = oracleConnList.get(2).split("UserName:")[1];
				password = myEncryptor.decrypt(oracleConnList.get(3).split("Password:")[1]);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static Connection getConn(String oId, String externalpath) {
		Connection ret = null;
		String driver = "", url = "", userID = "", password = "";
		try {
			myEncryptor = new DESedeEncryption();
			ArrayList<String> oracleConnList;
			oracleConnList = ReadFile.readConnection(DAOManager.AbsolutePath + "WEB-INF//data//ConnectionConfig.txt");
			if (oracleConnList.size() > 0) {
				driver = oracleConnList.get(0).split("Driver:")[1];
				url = oracleConnList.get(1).split("URL:")[1];
				userID = oracleConnList.get(2).split("UserName:")[1];
				password = oracleConnList.get(3).split("Password:")[1];
			}
			Class.forName(driver);
			ret = DriverManager.getConnection(url, userID, myEncryptor.decrypt(password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


	private static void readAnotherConnectionString(String fileName, String pOID) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		path = ServerSession.serverPath + "data//" + fileName;

		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(pOID)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split(",");
		servername = l_split[1];
		port = l_split[2];
		instance = l_split[3];
		dbname = l_split[4];
		dbUsr = l_split[5];
		dbPwd = ServerUtil.decryptPIN(l_split[6]);
		connType = l_split[7];
	}

	public static void readConfig() {
		ArrayList<String> arl = new ArrayList<String>();
		String path = DAOManager.AbsolutePath + "WEB-INF//reference//config.txt";
		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				
			}
		}		
	}

	private static void readConnectionString(String pOID, String externalpath) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		if (externalpath.equals("")) {
			path = ServerSession.serverPath + "data//ConncetionConfig.txt";
		} else {
			path = externalpath + "data//ConncetionConfig.txt";
		}

		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(pOID)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split(",");
		servername = l_split[1];
		port = l_split[2];
		instance = l_split[3];
		dbname = l_split[4];
		dbUsr = l_split[5];
		dbPwd = ServerUtil.decryptPIN(l_split[6]);
		connType = l_split[7];
	}

	public static String readEnvConfig(String param, String externalpath) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		// externalpath = ServerSession.serverPath + "reference//config.txt";
		externalpath = ServerSession.serverPath + "WEB-INF/" + "reference/config.txt";
		try {
			arl = FileUtil.readFile(externalpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(param)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split(":");
		System.out.println("Environment Key: "+l_ret);
		
		if (!l_split[0].equals("")) {
			l_ret = l_split[1];
		}
		if (l_ret.equals("")) {
			l_ret = Constant.envVar;
		}
		if(param.equalsIgnoreCase("envName1") ) {
			ServerGlobal.setmEnvPath1(l_ret);
		}else if(param.equalsIgnoreCase("envName2") ) {
			ServerGlobal.setmEnvPath2(l_ret);
		}
		
		return l_ret;
	}

	public static String readExternalUrl(String pOID) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		String path = DAOManager.AbsolutePath + "WEB-INF//data//ExternalConnectionConfig.txt";
		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(pOID)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split("_");
		return l_ret = l_split[1].trim();
	}

	private static void readOracleConnectionString(String pOID) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		path = ServerSession.serverPath + "data//OracleConncetionConfig.txt";

		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(pOID)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split(",");
		servername = l_split[1];
		port = l_split[2];
		instance = l_split[3];
		dbname = l_split[4];
		dbUsr = l_split[5];
		dbPwd = ServerUtil.decryptPIN(l_split[6]);
		connType = l_split[7];
	}

	public ConnAdmin() {
		super();

	}
	
	public static String readFCMConfig(String param) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		String path = ServerSession.serverPath + "WEB-INF//reference//config.txt";
		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(param)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split("__");
		if (!l_split[0].equals("")) {
			return l_ret = l_split[1];
		}
		return l_ret;
	}
	
	public static String readConfig(String param) {
		String l_ret = "";
		ArrayList<String> arl = new ArrayList<String>();
		String path = ServerSession.serverPath + "WEB-INF//reference//config2.txt";
		try {
			arl = FileUtil.readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arl.size(); i++) {
			if (!arl.get(i).equals("")) {
				if (arl.get(i).startsWith(param)) {
					l_ret = arl.get(i);
					break;
				}
			}
		}
		String[] l_split = l_ret.split(":");
		if (!l_split[0].equals("")) {
			return l_ret = l_split[1];
		}
		return l_ret;
	}

	public static Connection setRPConn(String oId, String externalpath) {
		Connection ret = null;
		String driver = "", url = "", userID = "", password = "";
		try {
			myEncryptor = new DESedeEncryption();
			ArrayList<String> oracleConnList;
			oracleConnList = ReadFile.readConnection(DAOManager.AbsolutePath + "WEB-INF//data//ConnectionConfig.txt");
			if (oracleConnList.size() > 0) {
				//com.nirvasoft.rp.core.ccbs.dao.DAOManager.Driver = oracleConnList.get(0).split("Driver:")[1];
				com.nirvasoft.rp.core.ccbs.dao.DAOManager.URL = oracleConnList.get(1).split("URL:")[1];
				com.nirvasoft.rp.core.ccbs.dao.DAOManager.UserName = oracleConnList.get(2).split("UserName:")[1];
				com.nirvasoft.rp.core.ccbs.dao.DAOManager.Password = myEncryptor.decrypt(oracleConnList.get(3).split("Password:")[1]);
				com.nirvasoft.rp.core.ccbs.dao.DAOManager.ConnString = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static boolean readConnConfig() {
		String driver = "", url = "", userID = "", password = "";
		boolean result=false;
		try {
			myEncryptor = new DESedeEncryption();
			ArrayList<String> oracleConnList;
			oracleConnList = ReadFile.readConnection(DAOManager.AbsolutePath + "WEB-INF//data//ConnectionConfig.txt");
			if (oracleConnList.size() > 0) {
				driver = oracleConnList.get(0).split("Driver:")[1];
				url = oracleConnList.get(1).split("URL:")[1];
				userID = oracleConnList.get(2).split("UserName:")[1];
				password = oracleConnList.get(3).split("Password:")[1];
				result = true;
				System.out.println("Read Connection Successful!");
			}
			if(result) {
				ServerGlobal.setmDriver(driver);
				ServerGlobal.setmUrl(url);
				ServerGlobal.setmUserid(userID);
				ServerGlobal.setmPassword(myEncryptor.decrypt(password));
			}else {
				System.out.println("Read Connection Failed!");
			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Connection getConn() {
		Connection ret = null;
		try {
			Class.forName(ServerGlobal.getmDriver());
			ret = DriverManager.getConnection(ServerGlobal.getmUrl(), ServerGlobal.getmUserid(),ServerGlobal.getmPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static Connection getConn3() {
		Connection ret = null;
		Context initContext;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
			ret = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static Connection CreateConnection()
	{
		Connection conn=null;
		try {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//DriverManager.setLoginTimeout(DAOManager.getConnectionTime());	
			conn=DriverManager.getConnection(url,userID,password);
			
			//conn=DriverManager.getConnection(ConnString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
