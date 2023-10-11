package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.ServerSession;
import com.nirvasoft.rp.util.FileUtil;
import com.nirvasoft.rp.util.ReadFile;
import com.nirvasoft.rp.util.ServerGlobal;
import com.nirvasoft.rpmini.framework.keyfactory.KeyFactory;

import password.DESedeEncryption;

public class DAOManager {
	public static String Driver = "";
	public static String URL = "";
	public static String UserName = "";
	public static String Password = "";
	public static String ConnString = "";
	public static String AbsolutePath = "";
	private static int normalTime = 0;
	private static int transTime = 0;
	private static int schedulTime = 0;
	private static int multiTransTime = 0;
	private static int ConnectionTime = 0;

	public static int getConnectionTime() {
		return ConnectionTime;
	}

	public static int getMultiTransTime() {
		return multiTransTime;
	}

	public static int getNormalTime() {
		return normalTime;
	}

	public static int getSchedulTime() {
		return schedulTime;
	}

	public static int getTransTime() {
		return transTime;
	}

	public static String leadZeros(String p, int size) {
		String ret = p;
		for (int i = p.length(); i < size; i++) {
			ret = "0" + ret;
		}
		return ret;
	}

	public static void setConnectionTime(int connectionTime) {
		ConnectionTime = connectionTime;
	}

	public static void setMultiTransTime(int multiTransTime) {
		DAOManager.multiTransTime = multiTransTime;
	}

	public static void setNormalTime(int normalTime) {
		DAOManager.normalTime = normalTime;
	}

	public static void setSchedulTime(int schedulTime) {
		DAOManager.schedulTime = schedulTime;
	}

	public static void setTransTime(int transTime) {
		DAOManager.transTime = transTime;
	}

	DESedeEncryption myEncryptor;

	public DAOManager() {
		try {
			myEncryptor = new DESedeEncryption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Loading Connection From Config Files
		// ReadConnectionString();
		// ReadConnectionConfig();

		if (Driver.equals("")) {
			Driver = "net.sourceforge.jtds.jdbc.Driver";
		}
		if (URL.equals("")) {
			URL = "jdbc:jtds:sqlserver://localhost:1433/Maha_001;";
		}
		if (UserName.equals("")) {
			UserName = "sa";
		}
		if (Password.equals("")) {
			Password = "123";
		}

		if (ConnString.equals("")) {
			ConnString = "jdbc:sqlserver://localhost:1433;" + "databaseName=Maha_001;user=sa;password=123";
		}
	}

	public Connection CreateConnection() {
		Connection conn = null;
		try {
			try {
				Class.forName(Driver);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(URL, UserName, Password);
			// conn=DriverManager.getConnection(ConnString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public String getNewAccoutNumber(Connection aConn) {
		String ret = new String();
		long l_Key = 0;
		String l_Prefix = "";
		try {

			KeyFactory keyfactory = KeyFactory.getInstance();
			l_Key = keyfactory.generateKey("mmppm", aConn);

			if (l_Key != 0) {
				ret = leadZeros("" + l_Prefix + l_Key, 10);
			} else {
				ret = "Key is not generated!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (aConn != null)
				try {
					if (!aConn.isClosed())
						aConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ret;
	}

	public String getNewCustomerID(Connection aConn) {
		String ret = new String();
		long l_Key = 0;
		String l_Prefix = "";
		try {

			KeyFactory keyfactory = KeyFactory.getInstance();
			l_Key = keyfactory.generateKey("mmppm", aConn);

			if (l_Key != 0) {
				ret = leadZeros("" + l_Prefix + l_Key, 7);
			} else {
				ret = "Key is not generated!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (aConn != null)
				try {
					if (!aConn.isClosed())
						aConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ret;
	}

	// by MD
	public String getRefKey(Connection aConn) {
		String ret = new String();
		long l_Key = 0;
		String l_Prefix = "Ref";
		try {

			KeyFactory keyfactory = KeyFactory.getInstance();
			l_Key = keyfactory.generateKey("mmppm", aConn);

			if (l_Key != 0) {
				ret = l_Prefix + l_Key;
			} else {
				ret = "Key is not generated!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (aConn != null)
				try {
					if (!aConn.isClosed())
						aConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ret;
	}

	public Connection openConnection() throws Exception {
		Connection ret = null;
		String driver = "", url = "", userID = "", password = "";
		ArrayList<String> oracleConnList;
		oracleConnList = ReadFile.readConnection(
				System.getenv(ServerSession.serverPath + "WEB-INF/data/" + "ConnectionConfig.txt"));
		if (oracleConnList.size() > 0) {
			driver = oracleConnList.get(0).split("Driver:")[1];
			url = oracleConnList.get(1).split("URL:")[1];
			userID = oracleConnList.get(2).split("UserName:")[1];
			password = oracleConnList.get(3).split("Password:")[1];
		}
		Class.forName(driver);
		ret = DriverManager.getConnection(url, userID, myEncryptor.decrypt(password));
		return ret;
	}

	public Connection openICBSConnection() throws Exception {
		Connection ret = null;
		String driver = "", url = "", userID = "", password = "";
		ArrayList<String> oracleConnList;
		oracleConnList = ReadFile.readConnection(
				System.getenv(ServerSession.serverPath + "WEB-INF/data/" + "ConnectionConfig.txt"));
		if (oracleConnList.size() > 0) {
			driver = oracleConnList.get(0).split("Driver:")[1];
			url = oracleConnList.get(1).split("URL:")[1];
			userID = oracleConnList.get(2).split("UserName:")[1];
			password = oracleConnList.get(3).split("Password:")[1];
		}
		Class.forName(driver);
		ret = DriverManager.getConnection(url, userID, myEncryptor.decrypt(password));
		return ret;
	}

	private void ReadConnectionString() {
		String l_Driver = "";
		String l_URL = "";
		String l_UserName = "";
		String l_Password = "";
		try {
			ArrayList<String> arl = FileUtil.readList( System.getenv(ServerGlobal.getmEnvPath1()) + "/data/" + "ConnectionConfig.txt");
			for (int i = 0; i < arl.size(); i++) {
				if (!arl.get(i).equals("")) {
					if (arl.get(i).startsWith("Driver:")) {
						l_Driver = arl.get(i).split("Driver:")[1];
					} else if (arl.get(i).startsWith("URL:")) {
						l_URL = arl.get(i).split("URL:")[1];
					} else if (arl.get(i).startsWith("UserName:")) {
						l_UserName = arl.get(i).split("UserName:")[1];
					} else if (arl.get(i).startsWith("Password:")) {
						l_Password = arl.get(i).split("Password:")[1];
					}
				}
			}

			if ((!l_Driver.equals("")) && (!l_URL.equals("")) && (!l_UserName.equals("")) && (!l_Password.equals(""))) {
				DAOManager.Driver = l_Driver.trim();
				DAOManager.URL = l_URL.trim();
				DAOManager.UserName = l_UserName.trim();
				DAOManager.Password = myEncryptor.decrypt(l_Password.trim());

				// X.say(DAOManager.Driver);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
