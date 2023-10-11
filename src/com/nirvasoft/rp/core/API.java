package com.nirvasoft.rp.core;

import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.common.XML;
import com.nirvasoft.rp.core.ccbs.dao.AccountsDAO;
import com.nirvasoft.rp.core.ccbs.dao.DAOManager;
import com.nirvasoft.rp.core.ccbs.dao.UserDAO;
import com.nirvasoft.rp.mgr.DBAccountMgr;
import com.nirvasoft.rp.core.ccbs.data.db.DBCustomerMgr;
import com.nirvasoft.rp.core.ccbs.data.db.DBSecurityMgr;
import com.nirvasoft.rp.mgr.DBSystemMgr;
import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.CustomerData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.MenuData;
import com.nirvasoft.rp.shared.MrBean;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.shared.SystemData;
import com.nirvasoft.rp.shared.UserData;

public class API {	
	public static String test(String... data){
		return "Hello World" + data[0];
	}
	public static void getConnected(String ... data){
		if (SingletonConnection.getSharedConn()==null) {
			SingletonServer.ReadConnectionString();
			DAOManager myConnectionUtil = new DAOManager();
			SingletonConnection.setSharedConn( myConnectionUtil.CreateConnection() );
		}
	}
	public static void beginTrans(String ... data){
		getConnected();
		try {
			SingletonConnection.getSharedConn().setAutoCommit(false);
		} catch (SQLException e) { e.printStackTrace(); }
	}
	public static void commitTrans(String ... data){
		try {
			SingletonConnection.getSharedConn().commit();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	public static void rollBackTrans(String ... data) {
		try {
			SingletonConnection.getSharedConn().rollback();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public static String login(String ... data) {
		getConnected();
		int pAppType=0;
		String ret="False";
		UserData l_UserData = new UserData();
		UserDAO l_UserDAO = new UserDAO();
		try {
			System.out.println(data[3]);
			 if (l_UserDAO.authenticate(data[0], data[1], data[2], pAppType, SingletonConnection.getSharedConn())) {
				 l_UserData = l_UserDAO.getUserData();
				 System.out.println("Log in Success!");
				 System.out.println(l_UserData.getName());
				 ret ="True";
			 } else {
				 System.out.println("Log in Fail!");
			 }
				 
		} catch (Exception e) { e.printStackTrace(); }
		return ret;
	}
	
	public static String getLastID(String ... data) {
		getConnected();
		String ret = "";
		try {
			AccountsDAO l_AccountDAO = new AccountsDAO();
			String l_PrdCode = "01";
			String l_AccTypeCode = "01";
			String l_BranchCode = "001";
			String l_CurCode = "0";
			int l_SerStart = 10;
			int l_SerLength = 6;
			int l_PrdStart = 1;
			int l_PrdLength = 2;
			int l_AccTypeStart = 6;
			int l_AccTypeLength = 2;
			int l_BranchStart = 3;
			int l_BranchLength = 3;
			int l_CurCodeStart = 8;
			int l_CurCodeLength = 1;
			ret = l_AccountDAO.getLastIDNotAccType(l_PrdCode, l_AccTypeCode, l_BranchCode, l_CurCode, l_SerStart, l_SerLength, 
					l_PrdStart, l_PrdLength, l_AccTypeStart, l_AccTypeLength, l_BranchStart, l_BranchLength, l_CurCodeStart, l_CurCodeLength, SingletonConnection.getSharedConn());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String authenticate(String ... data) {
		String ret = "False";
		getConnected();
		UserData l_UserData = new UserData();
		MrBean l_MrBeanData = new MrBean();
		
		try {
			l_MrBeanData = DBSecurityMgr.authenticate(data[0], data[1], data[2], SingletonConnection.getSharedConn());
			l_UserData = l_MrBeanData.getUser();
			System.out.println(data[2]);
			if (l_UserData != null) {
				 System.out.println("Log in Success!");
				 System.out.println(l_UserData.getName());
				 ret = l_UserData.getRights();
			} else {
				System.out.println("Log in Fail!");
				ret = "False";
			}
		} catch (Exception e) { 
			e.printStackTrace();
			ret = "False";
		} finally {
			try {SingletonConnection.getSharedConn().close();SingletonConnection.setSharedConn(null);} catch (SQLException e2) {e2.printStackTrace();}
		}
		return ret;
	}
	
	public static String getMenuItems(String ... data) {
		String ret = "";
		getConnected();
		
		try {
			ArrayList<MenuData> l_ArlMenuDatas = new ArrayList<MenuData>();
			l_ArlMenuDatas = DBSecurityMgr.getRoleMenuItems(data[0]);
			
			String l_Data = "";
			XML l_ResultXML = new XML();
			for (int i=0; i < l_ArlMenuDatas.size(); i++) {
				XML xml = new XML();
				xml.add(l_ArlMenuDatas.get(i).getMenuID(), "menuId");
				xml.add(l_ArlMenuDatas.get(i).getMenuCode(), "code");
				xml.add(l_ArlMenuDatas.get(i).getMenuDescription(), "description");
				xml.add(l_ArlMenuDatas.get(i).getParentID(), "parentId");
				xml.add(l_ArlMenuDatas.get(i).getButtonRole(), "buttonRole");
				xml.add(l_ArlMenuDatas.get(i).getMenuOrder() + "", "menuOrder");
				xml.add(l_ArlMenuDatas.get(i).getMenuCommand(), "command");
				
				l_Data = xml.getContent();
				l_ResultXML.add(l_Data, "menuData");
			}
			ret = l_ResultXML.getXML();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {SingletonConnection.getSharedConn().close();SingletonConnection.setSharedConn(null);} catch (SQLException e2) {e2.printStackTrace();}
		}
		return ret;
	}
	
	public static String saveAccount(String ... data) {
		String ret = "False";
		AccountData currentObject = new AccountData();
		currentObject = parseAccount(data);
		
		getConnected();
		SystemData l_Sd = new SystemData();
		
		
		DataResult l_Result = new DataResult();
		try {
			l_Sd.setCurrencyCodes(DBSystemMgr.readCurrencyCodes());
			l_Sd.setProducts(DBSystemMgr.readProducts());
			SingletonServer.setSystemData(l_Sd);
			SharedLogic.setSystemData(l_Sd);
			beginTrans();
			l_Result = DBAccountMgr.save(currentObject, SingletonConnection.getSharedConn());
			System.out.println(l_Result.getStatus());
			System.out.println(l_Result.getData());
			if (l_Result.getStatus()) {
				ret = l_Result.getData();
				System.out.println(ret);
				commitTrans();
			} else {
				rollBackTrans();
			}
		} catch (SQLException e) {			
			if(e.getErrorCode() == 2627) {
				l_Result.setStatus(false);
				l_Result.setDescription("Please Try Again !");
			}
			rollBackTrans();
		} finally {
			try {SingletonConnection.getSharedConn().close();SingletonConnection.setSharedConn(null);} catch (SQLException e2) {e2.printStackTrace();}
		}
		
		return ret;
	}
	
	private static AccountData parseAccount(String ... data) {
		AccountData ret = new AccountData();
		ret.setAccountNumber(data[0]);		// 0 - AccountNumber
		ret.setAccountName(data[1]);		// 1 - AccName
		ret.setPassbook(data[2]);			// 2 - PassBook
		ret.setOpeningDate(data[3]);		// 3 - OpeningDate
		ret.setType(data[4]);				// 4 - AccountType
		ret.setProduct(data[5]);			// 5 - Product
		ret.setBranchCode(data[6]);			// 6 - Branch
		ret.setCurrencyCode(data[7]);		// 7 - CurrencyCode
		ret.setDrawingType(data[8]);		// 8 - DrawingType
		ret.setStatus(Integer.parseInt(data[9]));// 9 - Status
		
		AccountCustomerData l_AccCustomer = new AccountCustomerData();
		l_AccCustomer.setCustomerID(data[10]);						//10 - CustomerID	
		l_AccCustomer.setRelationType(Integer.parseInt(data[11]));	//11 - RType
		l_AccCustomer.setAccountType(Integer.parseInt(data[12]));	//12 - AccType
		l_AccCustomer.setAccountNumber(ret.getAccountNumber());
		
		ret.getAccountCustomers().add(l_AccCustomer);
	
		ret.setRemark(data[13]);		//13 - Remark
		
		AddressData l_AddressData = new AddressData();
		l_AddressData.setHouseNo(data[14]);		//14 - HouseNo		-MailingAddress
		l_AddressData.setStreet(data[15]);		//15 - Street
		l_AddressData.setWard(data[16]);		//16 - Ward
		l_AddressData.setTownship(data[17]);	//17 - TownShip
		l_AddressData.setDistrict(data[18]);	//18 - District
		l_AddressData.setDivision(data[19]);	//19 - Division
		l_AddressData.setCity(data[20]);		//20 - City
		l_AddressData.setCountry(data[21]);		//21 - Country
		l_AddressData.setPostalCode(data[22]);	//22 - PostalCode
		l_AddressData.setFax(data[23]);			//23 - Fax
		l_AddressData.setTel1(data[24]);		//24 - Tel1
		l_AddressData.setEmail(data[25]);		//25 - Email
		l_AddressData.setWebsite(data[26]);		//26 - WebSite
		
		ret.setMailingAddress(l_AddressData);
		//27 - For Fixed , Later
		
		return ret;
	}
	
	public static String createAccountForCard(String ... data) {
		String ret = "False";
		String l_CardNo = data[0];
		String l_CustomerId = data[1];
		String l_OpnDate = data[2];
		DataResult l_result = new DataResult();
		SystemData l_Sd = new SystemData();
		SingletonServer.ReadConnectionString();
		
		getConnected();
		beginTrans();
		try {
			l_Sd.setProducts(DBSystemMgr.readProducts());
			SingletonServer.setSystemData(l_Sd);
			SharedLogic.setSystemData(l_Sd);
			
			l_result = DBAccountMgr.createAccountForCard(l_CardNo, l_CustomerId, l_OpnDate, SingletonConnection.getSharedConn());
			
			if (l_result.getStatus()) {
				commitTrans();
				ret = "True";
			} else {
				rollBackTrans();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rollBackTrans();
		} finally {
			try {SingletonConnection.getSharedConn().close();SingletonConnection.setSharedConn(null);} catch (SQLException e2) {e2.printStackTrace();}
		}
		return ret;
	}
	
	public static String saveCustomer(String ... data) {
		String ret = "False";
		DataResult l_result = new DataResult();
		CustomerData currentObject = new CustomerData();
		currentObject = parseCustomer(data);
		
		SingletonServer.ReadConnectionString();
		getConnected();
		beginTrans();
		try {
			l_result = DBCustomerMgr.saveNoDuplicateByIC(currentObject, SingletonConnection.getSharedConn());
			
			if (l_result.getStatus()) {
				commitTrans();
				ret = l_result.getData();
			} else {
				rollBackTrans();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rollBackTrans();
		} finally {
			try {SingletonConnection.getSharedConn().close();SingletonConnection.setSharedConn(null);} catch (SQLException e2) {e2.printStackTrace();}
		}
		
		return ret;
	}
	
	private static CustomerData parseCustomer(String ... data) {
		CustomerData ret = new CustomerData();
		ret.setCustomerID(data[0]);						// 0 - CustomerID
		ret.setIC(data[1]);								// 1 - IC
		ret.setTitle(data[2]);							// 2 - Title
		ret.setName(data[3]);							// 3 - Name
		ret.setDOB(data[4]);							// 4 - DOB
		ret.setFatherName(data[5]);						// 5 - Father Name
		ret.setAlias(data[6]);							// 6 - Alias
		ret.setCustomerType(Integer.parseInt(data[7]));	// 7 - Customer Type 
		ret.setSex(Byte.parseByte(data[8]));			// 8 - Sex
		ret.setMaritalStatus(Integer.parseInt(data[9]));// 9 - Marital Status
		ret.setOccupation(data[10]);					//10 - Occupation
		ret.setNationality(data[11]);					//11 - Nationality
		ret.setRace(data[12]);							//12 - Race
		ret.setReligion(data[13]);						//13 - Religion
		ret.setOldIC(data[14]);							//14 - OldIC	
		ret.setBcNo(data[15]);							//15 - BCNo
		ret.setUniversalID(data[16]);					//16 - UniversalID
		ret.setOther(data[17]);							//17 - Others

		ret.getAddress().setHouseNo(data[18]);			//18 - House No			// Address
		ret.getAddress().setStreet(data[19]);			//19 - Street
		ret.getAddress().setWard(data[20]);				//20 - Ward
		ret.getAddress().setTownship(data[21]);			//21 - Township
		ret.getAddress().setDivision(data[22]);			//22 - Division				
		ret.getAddress().setDistrict(data[23]);			//23 - District
		ret.getAddress().setCountry(data[24]);			//24 - Country
		ret.getAddress().setPostalCode(data[25]);		//25 - PostalCode, zip
		ret.getAddress().setTel1(data[26]);				//26 - Tel1
		ret.getAddress().setEmail(data[27]);			//27 - Email
		ret.getAddress().setFax(data[28]);				//28 - Fax
		ret.setT1(data[29]);							//29 - T1
		ret.setT2(data[30]);							//30 - T2
		ret.setT3(data[31]);							//31 - T3	// Other Customer Type
		
		return ret;
	}

	public static String generateAccountNo(String ... data ) {
		String ret = "";
		SystemData l_Sd = new SystemData();
		AccountData currentObject = new AccountData();
		SingletonServer.ReadConnectionString();
		try {
			getConnected();
			l_Sd.setProducts(DBSystemMgr.readProducts());
			l_Sd.setCurrencyCodes(DBSystemMgr.readCurrencyCodes());
			SingletonServer.getSystemData().setProducts(l_Sd.getProducts());
			SharedLogic.getSystemData().setProducts(l_Sd.getProducts());
			
			SingletonServer.getSystemData().setCurrencyCodes(l_Sd.getCurrencyCodes());
			SharedLogic.getSystemData().setCurrencyCodes(l_Sd.getCurrencyCodes());
			
			currentObject = parseAccountForgenerateAccountNo(data);
			ret = DBAccountMgr.generateAccountNo(currentObject, SingletonConnection.getSharedConn());
			System.out.println(ret);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	private static AccountData parseAccountForgenerateAccountNo(String ... data) {
		AccountData ret = new AccountData();
		ret.setAccountNumber("TBA");
		ret.setProduct(data[0]);		//01, 02, ...
		ret.setCurrencyCode(data[1]);	//MMK, ...
		ret.setType(data[2]);			//01, 02, ...
		ret.setBranchCode(data[3]);		//001, 002,...
		//hnns add for char AccNumber
		if(data.length>4){
			ret.setAccountName(data[4]);			
		}
		
		return ret;
	}
}


