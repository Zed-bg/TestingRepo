package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import java_cup.internal_error;

import com.nirvasoft.rp.core.SingletonServer;
import com.nirvasoft.rp.core.util.StrUtil;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.AgentTypeData;
import com.nirvasoft.rp.shared.DataResult;

public class MobileUserDao {
	public String CheckPinbyToPhone(String pPhoneNo,Connection conn) throws SQLException {
        String  ret = "";
		String l_Query = "";
		
		l_Query = "SELECT * FROM MobileUser WHERE UserId = '"+pPhoneNo+"'";
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			ret = rs.getString("password");
			
		}
		rs.close();
		pstmt.close();
		
		return ret;
	}
	public boolean checkPIN(String pPhoneNo, String pPIN, Connection conn) throws SQLException {
	    boolean  ret = false;
			String l_Query = "";
			
			l_Query = "SELECT * FROM MobileUser WHERE UserId = '"+pPhoneNo+"' AND password = '"+pPIN+"'";
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				ret = true;
			}
			rs.close();
			pstmt.close();
			
			return ret;
	}
	public boolean changePIN(String pPhoneNo, String pPIN, Connection conn) throws SQLException {
	    boolean  ret = false;
			String l_Query = "";
			
			l_Query = "UPDATE MobileUser SET password='"+pPIN+"' WHERE UserId = '"+pPhoneNo+"'";
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			int rs = pstmt.executeUpdate();
			
			if(rs>0){
				ret = true;
			}
			pstmt.close();
			return ret;
	}
	public static DataResult UpdateMobleUser(AccountData accontData,
			Connection pConn) {
		String l_MajorCustomerName = getMajorCustomerName(accontData, pConn);
		accontData.getMobileUserData().setpassword(StrUtil.encryptPIN(accontData.getMobileUserData().getpassword())); 
		DataResult result = new DataResult();
		int rs=0;
		PreparedStatement pstmt;
		try {
			pstmt = pConn.prepareStatement("UPDATE MobileUser SET UserName=?,Password=?,modifiedDate=?,modifiedUser=?,status=?,t1=?,t2=?,t3=?,t4=?,t5=?,n1=?,n2=?,n3=?,n4=?,n5=? WHERE UserId=?");
		
        updateMobileRecord(accontData,l_MajorCustomerName, pstmt, 1);	
        rs = pstmt.executeUpdate();
        pstmt.close();
        
        if(rs>0){
           result.setStatus(true);
        }
        if(result.getStatus())
        {
        	PreparedStatement stmt=pConn.prepareStatement("UPDATE mobilejunction SET SIMID=?,t1=?,t2=?,t3=?,t4=?,t5=?,n1=?,n2=?,n3=?,n4=?,n5=? WHERE PhoneNo=? AND AccountNumber=?");
            updateJunctionRecord(accontData,l_MajorCustomerName, stmt, 1);	
            rs = stmt.executeUpdate();
            stmt.close();
            
            if(rs>0){
               result.setStatus(true);
            }
        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean SaveMobileUser(AccountData accontData, Connection pConn) {
		String l_MajorCustomerName = getMajorCustomerName(accontData, pConn);
		String l_CustomerMobilePhoneNo =  getCustomerMobilePhoneNo(accontData, pConn);
		accontData.getMobileUserData().setpassword(StrUtil.encryptPIN(accontData.getMobileUserData().getpassword()));
		accontData.getMobileUserData().setphoneNo(l_CustomerMobilePhoneNo);
		boolean result=false;
		int rs=0;
		
		try {
			PreparedStatement pstmt=pConn.prepareStatement("INSERT INTO MobileUser (UserId,UserName,Password,createdDate,modifiedDate,modifiedUser,status,t1,t2,t3,t4,t5,n1,n2,n3,n4,n5) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            updateMobileRecord(accontData,l_MajorCustomerName, pstmt, 0);	
            rs= pstmt.executeUpdate();
            pstmt.close();
            result=true;
            if(rs>0){
               result=true;
            }
				
		if(result==true)
		{
			PreparedStatement stmt=pConn.prepareStatement("INSERT INTO mobilejunction (AccNumber,CustomerId,AccType,RType,PhoneNo,SIMID,t1,t2,t3,t4,t5,n1,n2,n3,n4,n5) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            updateJunctionRecord(accontData,l_MajorCustomerName, stmt, 0);	
            rs = stmt.executeUpdate();
            stmt.close();
            
            if(rs>0){
               result=true;
            }
		}
		} catch (SQLException e) {
			
			e.printStackTrace();
			result=false;
		}
       return result;
	}

	private static void updateJunctionRecord(AccountData accontData,
			String l_MajorCustomerName, PreparedStatement stmt, int pData) throws SQLException {
		int i=1;
		if(pData==0){
		stmt.setString(i++, accontData.getAccountNumber());
		stmt.setString(i++, accontData.getAccountCustomers().get(0).getCustomerID());
		stmt.setString(i++,String.valueOf(110));
		stmt.setString(i++,String.valueOf(110));
		stmt.setString(i++, accontData.getMobileUserData().getphoneNo());
		}
		stmt.setString(i++, accontData.getMobileUserData().getSIMID());
		stmt.setString(i++, accontData.getMobileUserData().getT1());
		stmt.setString(i++, accontData.getMobileUserData().getT2());
		stmt.setString(i++, accontData.getMobileUserData().getT3());
		stmt.setString(i++, accontData.getMobileUserData().getT4());
		stmt.setString(i++, accontData.getMobileUserData().getT5());
		stmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN1()));
		stmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN2()));
		stmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN3()));
		stmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN4()));
		stmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN5()));
		if(pData==1){
			stmt.setString(i++, accontData.getMobileUserData().getphoneNo());
			stmt.setString(i++, accontData.getAccountNumber());
		}
	}

	private static void updateMobileRecord(AccountData accontData,String l_MajorCustomerName,
			PreparedStatement pstmt,int pData) throws SQLException {
		// TODO Auto-generated method stub
		int i=1;
		if(pData==0)
		pstmt.setString(i++, accontData.getMobileUserData().getphoneNo());
		pstmt.setString(i++, l_MajorCustomerName);
		pstmt.setString(i++, accontData.getMobileUserData().getpassword());
		
		if(pData==0){
		pstmt.setString(i++, accontData.getOpeningDate());
		pstmt.setString(i++, accontData.getOpeningDate());
		}
	else{
		pstmt.setString(i++, accontData.getLastTransDate());	
		}
		pstmt.setString(i++, SingletonServer.getUser().getUserID());
		pstmt.setString(i++, String.valueOf(accontData.getMobileUserData().getmStatus()));
		
		pstmt.setString(i++, accontData.getMobileUserData().getT1());
		pstmt.setString(i++, accontData.getMobileUserData().getT2());
		pstmt.setString(i++, accontData.getMobileUserData().getT3());
		pstmt.setString(i++, accontData.getMobileUserData().getT4());
		pstmt.setString(i++, accontData.getMobileUserData().getT5());
		pstmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN1()));
		pstmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN2()));
		pstmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN3()));
		pstmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN4()));
		pstmt.setString(i++, String.valueOf(accontData.getMobileUserData().getN5()));
		if(pData!=0){
			pstmt.setString(i++, accontData.getMobileUserData().getphoneNo());
		}
	}
	
	private static String getMajorCustomerName(AccountData object,Connection conn)
	{
		String ret = "";		
		CustomerDAO l_CustomerDAO = new CustomerDAO();
		try {					
			for (int i=0; i<object.getAccountCustomers().size();i++){			
				if(object.getAccountCustomers().get(i).getAccountType() == object.getAccountCustomers().get(i).getRelationType()){					
					l_CustomerDAO.getCustomer(object.getAccountCustomers().get(i).getCustomerID(), conn);
					ret = l_CustomerDAO.getObjCustBean().getName().trim();break;
				}
			}			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	private static String getCustomerMobilePhoneNo(AccountData object,Connection conn)
	{
		String ret = "";		
		CustomerDAO l_CustomerDAO = new CustomerDAO();
		try {					
			for (int i=0; i<object.getAccountCustomers().size();i++){			
				if(object.getAccountCustomers().get(i).getAccountType() == object.getAccountCustomers().get(i).getRelationType()){					
					l_CustomerDAO.getCustomer(object.getAccountCustomers().get(i).getCustomerID(), conn);
					ret = l_CustomerDAO.getObjCustBean().getAddress().getTel3().trim();break;
				}
			}			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return ret;
	}
	public String getPasswordbyPhone(String agentPhoneNo, Connection conn) throws SQLException {
		    String  ret = "";
			String l_Query = "";
			
			l_Query = "SELECT * FROM MobileUser WHERE UserId = '"+agentPhoneNo+"'";
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				ret = rs.getString("Password");
			}
			rs.close();
			pstmt.close();
			
			return ret;
	}
	
	public boolean changeMobileUser(String phoneNumber, String password,int agent,String fcAccNo,
			Connection conn) {
		  boolean  ret = false;
			String l_Query = "";
			
			l_Query = "UPDATE MobileUser SET password='"+password+"',N1='"+agent+"' WHERE UserId = '"+phoneNumber+"'";
			PreparedStatement pstmt;
			try {
				pstmt = conn.prepareStatement(l_Query);
		
			int rs = pstmt.executeUpdate();
			
			if(rs>0){
				l_Query = "UPDATE MobileJunction SET T1 = '"+fcAccNo+"' WHERE PhoneNo = '"+phoneNumber+"'";
				pstmt = conn.prepareStatement(l_Query);
				rs = pstmt.executeUpdate();
				if(rs>0) {
					ret = true;
				}
			}
			pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
	}
	
	public boolean changeMobileUser(String phoneNumber, String password,int agent,String fcAccNo,String usrName,
			int pGroupType, Connection conn) {
		  boolean  ret = false;
			String l_Query = "";
			
			l_Query = "UPDATE MobileUser SET password='"+password+"',N1='"+agent+"' ,userName='"+usrName+"',n2='"+pGroupType+"' WHERE UserId = '"+phoneNumber+"'";
			PreparedStatement pstmt;
			try {
				pstmt = conn.prepareStatement(l_Query);
		
			int rs = pstmt.executeUpdate();
			
			if(rs>0){
				l_Query = "UPDATE MobileJunction SET T1 = '"+fcAccNo+"' WHERE PhoneNo = '"+phoneNumber+"'";
				pstmt = conn.prepareStatement(l_Query);
				rs = pstmt.executeUpdate();
				if(rs>0) {
					ret = true;
				}
			}
			pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
	}
	public AgentTypeData getAgentType(String phoneNo, Connection conn) {
		AgentTypeData l_AgentTypeData = new AgentTypeData();
		String l_Query = "";
		PreparedStatement pstmt;
		
		l_Query = "SELECT [MobileUser].[N2],[AgentType].[T1],[AgentType].[N3],[AgentType].[N4],[AgentType].[N5] FROM MobileUser,AgentType WHERE [MobileUser].[N2]=[AgentType].[N1] and [MobileUser].[userid]='"+phoneNo+"'";
		try {
			pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				l_AgentTypeData.setType(rs.getInt("N2"));
				l_AgentTypeData.setDescription(rs.getString("T1"));
				l_AgentTypeData.setComrate(rs.getFloat("N4"));
				l_AgentTypeData.setLimitPerDay(rs.getFloat("N3"));
				l_AgentTypeData.setLimitPerTrans(rs.getFloat("N5"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return l_AgentTypeData;
	}
	public String getUserName(String s, Connection conn) {
		String l_UserName = "";
		String l_Query = "";
		PreparedStatement pstmt;
		l_Query = "SELECT [MU].UserName UserName FROM MobileUser MU INNER JOIN MobileJunction MJ ON MU.Userid = MJ.PhoneNo WHERE MJ.AccNumber ='"+s+"'";
		try {
			pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()) {
				l_UserName = rs.getString("UserName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l_UserName;
	}
	public DataResult checkCustomer(String pPhoneNo, Connection conn) {
		DataResult ret = new DataResult();
		try {
			PreparedStatement pmst = conn
					.prepareStatement("Select UserID from MobileUser where UserID='"
							+ pPhoneNo + "'");
			ResultSet rs = pmst.executeQuery();
			while (rs.next()) {
				ret.setStatus(true);
				ret.setData(rs.getString("UserID"));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return ret;
	}
	public void updateUserName(String pName,String pPhoneNo, Connection conn) {
		try {
			PreparedStatement pmst = conn
					.prepareStatement("Update MobileUser set UserName ='"+pName+"',N3 = 1  where UserID='"
							+ pPhoneNo + "'");
			pmst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
