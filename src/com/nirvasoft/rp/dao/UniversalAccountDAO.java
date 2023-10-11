package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.DataResult;

public class UniversalAccountDAO {
	static ArrayList<String> lsuniversalmonth = new ArrayList<String>();
	
	public static ArrayList<String> getLsuniversalmonth() {
		return lsuniversalmonth;
	}
	
	public static void setLsuniversalmonth(ArrayList<String> lsuniversalmonth) {
		UniversalAccountDAO.lsuniversalmonth = lsuniversalmonth;
	}
	
	public boolean addUniversalDepositAccount(AccountData object, Connection conn) throws SQLException {
	    boolean l_result = false;
	    String l_Query = "INSERT INTO UniversalAccAmountLimit (AccNumber, LimitAmount, EntryDate,FromDate,ToDate) values (?, ?, ?,?,?)";
	    PreparedStatement l_pstmt = conn.prepareStatement(l_Query);
	    updateObject(object, l_pstmt);
	    if(l_pstmt.executeUpdate() > 0) {
	    	l_result = true;
	    }
	    l_pstmt.close();
	    return l_result;
	}
	
	private void updateObject(AccountData pdata, PreparedStatement pPS) throws SQLException {
		int l_index = 1;
		pPS.setString(l_index++, pdata.getAccountNumber());
		pPS.setDouble(l_index++, pdata.getpUniversalAmount());
		pPS.setString(l_index++, pdata.getOpeningDate());
		pPS.setString(l_index++, pdata.getOpeningDate());
		pPS.setString(l_index++, "9999-12-31 00:00:00");		
	}
	
	public static double getAmountlimit(String accountNumber, Connection conn) throws SQLException {
		 PreparedStatement pstmt = conn.prepareStatement("Select LimitAmount from UniversalAccAmountLimit where AccNumber=?");
        double ret = 0.00;
        pstmt.setString(1, accountNumber.trim());
        ResultSet rs=pstmt.executeQuery();
               
        while(rs.next()){
        	ret = rs.getFloat("LimitAmount");
        }
        rs.close();
        pstmt.close();	    
        return ret;
	}
	
	public boolean UpdateUniversalDepositAccount(AccountData object, Connection pConn) throws SQLException {
   		boolean result=false;
        PreparedStatement pstmt=null;
        pstmt = pConn.prepareStatement("UPDATE UniversalAccAmountLimit SET LimitAmount=? WHERE AccNumber=?");
		pstmt.setDouble(1, object.getpUniversalAmount());
		pstmt.setString(2, object.getAccountNumber());
       if(pstmt.executeUpdate()>0)
         	result=true;
           
		return result;
	}
	
	public DataResult CheckDenoForUni(String accnumber, String date,
		Connection l_Conn) throws SQLException {
	    DataResult ret = new DataResult();
	    String[] pDate = null;
	    StringBuffer  l_query = new StringBuffer("");
	    pDate = date.split("/");
	 	l_query.append("SELECT UniversalAccAmountLimit.LimitAmount  FROM UniversalAccProvision inner join " +
	 			"UniversalAccAmountLimit on UniversalAccProvision.Accnumber=UniversalAccAmountLimit.AccNumber " +
	 			"WHERE UniversalAccProvision.Accnumber='"+accnumber.trim()+"' and EntryMonth = '"+pDate[0]+"' and " +
	 					"EntryYear = '"+pDate[1]+"' and status=1 ");
		
	    PreparedStatement pstmt = l_Conn.prepareStatement(l_query.toString());
	   
	    ResultSet rs=pstmt.executeQuery();
	    while(rs.next()){
	    	ret.setBalance(rs.getDouble("LimitAmount"));
	    	ret.setStatus(true);
	    }
	  pstmt.close();				
      return ret;
    }
	
	public static boolean checkforuniversalornot(String accountNumber, Connection pConn) throws SQLException {
	    PreparedStatement pstmt = pConn.prepareStatement("Select AccNumber from UniversalAccAmountLimit where AccNumber=?");
        boolean ret = false;
        pstmt.setString(1, accountNumber.trim());
        ResultSet rs=pstmt.executeQuery();
               
        while(rs.next()){
        	ret = true;
        }
        rs.close();
        pstmt.close();
	    
        return ret;
	}
	
	public static boolean addUniversalAccountProvision(String accountNumber,double Amount,int pSerialNo,long transref, ArrayList<String> lstUniEntryDate,int savepost,String effectivedate, Connection conn) throws SQLException {
		boolean result = false;
		PreparedStatement pstmt = null;		
		for(int i=0;i<lstUniEntryDate.size();i++){
		result = false;
		String data = lstUniEntryDate.get(i);
		
		
		pstmt = conn.prepareStatement("INSERT INTO UniversalAccProvision VALUES (?,?,?,?,?,?,GETDATE(),?,"+savepost+")");
		pstmt.setInt(1, pSerialNo);
		pstmt.setLong(2, transref);
		pstmt.setString(3, accountNumber);
		pstmt.setString(4, data.split("/")[0]);
		pstmt.setString(5, data.split("/")[1]);
		pstmt.setDouble(6, Amount/lstUniEntryDate.size());	
		pstmt.setString(7, effectivedate);	
		
		if(pstmt.executeUpdate()>0)
		   result = true;
			pstmt.close();
		}
			
		return result;
	}
	
	public static boolean deleteUniversalprovision(String pSerialNo,Connection pconn) throws SQLException {
		boolean l_result=false;
		String l_query="DELETE FROM UniversalAccProvision WHERE SerialNo="+pSerialNo;
			PreparedStatement l_pstmt=pconn.prepareStatement(l_query);
			if(l_pstmt.executeUpdate()>0) {
				l_result=true;
			}
			l_pstmt.close();
		return l_result;
	}
	
	public static boolean deleteUniversalprovisionwithtransref(int transRef,Connection pconn) throws SQLException {
		boolean l_result=false;
		String l_query="DELETE FROM UniversalAccProvision WHERE transRef="+transRef;
			PreparedStatement l_pstmt=pconn.prepareStatement(l_query);
			if(l_pstmt.executeUpdate()>0) {
				l_result=true;
			}
			l_pstmt.close();
		return l_result;
	}
	
	public static ArrayList<String> getuniversalmonth(int pSerialNo,Connection pconn) throws SQLException {
		ArrayList<String> ret = new ArrayList<String>();
			String query = "select Convert(varchar(10),Entrymonth)+'/'+Convert(varchar(10),Entrymonth) as EntryMonth from UniversalAccProvision where serialNo="+pSerialNo;
			PreparedStatement pstmt = pconn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String data = rs.getString("EntryMonth");
				ret.add(data);
			}
			pstmt.close();
		return ret;
	}
	
	public static boolean postSuccessUniversalTransaction(int aDenoSrNo,long transref,String effectiveDate,Connection pConn) throws SQLException {
		 boolean l_result = false;
		 int count =0;
		 String l_Query = "select  Count(AccNumber) from  UniversalAccProvision WHERE serialNo = ? ";
		 PreparedStatement l_pstmt = pConn.prepareStatement(l_Query);
		 l_pstmt.setInt(1, aDenoSrNo);	 
		 ResultSet rs = l_pstmt.executeQuery();
		 while(rs.next()){
			 count = rs.getInt(1);
		 }
		 if(count>0) {
			 String l_Query1 = "UPDATE UniversalAccProvision SET status = 1,TransRef="+transref+",EffectiveDate='"+effectiveDate+"' WHERE serialNo = ? ";
			 
			 PreparedStatement l_pstmt1 = pConn.prepareStatement(l_Query1);
			 l_pstmt1.setInt(1, aDenoSrNo);
			 
			 if(l_pstmt1.executeUpdate() > 0) {
				 l_result = true;
			 }
		 } else
			 l_result = true;
		 l_pstmt.close();
		 
		 return l_result;
	 }
	
	public static ArrayList<String> checkUniversalTransactionwithdate(String accountNumber,	ArrayList<String> lstUniEntryDate, Connection pConn) throws SQLException {
		ArrayList<String> lsuniversalmonth = new ArrayList<String>();
		String checkMonth = "";
		String checkYear = "";
		for(int i =0;i<lstUniEntryDate.size();i++){
			checkMonth=checkMonth+lstUniEntryDate.get(i).split("/")[0];
			checkYear = checkYear+lstUniEntryDate.get(i).split("/")[1];
			if(i!=lstUniEntryDate.size()-1){
				checkMonth=checkMonth+"','";
				checkYear=checkYear+"','";
			}
		}
		String query = "select Entrymonth+'/'+EntryYear as EntryMonth from UniversalAccProvision where AccNumber='"+accountNumber+"' and EntryMonth in ('"+checkMonth+"') and EntryYear in ('"+checkYear+"') and status=1";
		PreparedStatement pstmt = pConn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			String data = rs.getString("EntryMonth");
			lsuniversalmonth.add(data);
		}
		pstmt.close();
		return lsuniversalmonth;
	}
	
	public static ArrayList<String> checkUniversalTransactionwithdateForDeno(String accountNumber,	ArrayList<String> lstUniEntryDate, Connection pConn) throws SQLException {
		ArrayList<String> lsuniversalmonth = new ArrayList<String>();
		String checkMonth = "";
		String checkYear = "";
		for(int i =0;i<lstUniEntryDate.size();i++){
			checkMonth=checkMonth+lstUniEntryDate.get(i).split("/")[0];
			checkYear = checkYear+lstUniEntryDate.get(i).split("/")[1];
			if(i!=lstUniEntryDate.size()-1){
				checkMonth=checkMonth+"','";
				checkYear=checkYear+"','";
			}
		}
		String query = "select Entrymonth+'/'+EntryYear as EntryMonth from UniversalAccProvision where AccNumber='"+accountNumber+"' and EntryMonth in ('"+checkMonth+"') and EntryYear in ('"+checkYear+"')";
		PreparedStatement pstmt = pConn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			String data = rs.getString("EntryMonth");
			lsuniversalmonth.add(data);
		}
		pstmt.close();
		return lsuniversalmonth;
	}
	
	public boolean checkUniversalAccounts(String pAccNumber, Connection pConn) throws SQLException{
		boolean ret = false;
		String l_Query = "SELECT * FROM UniversalAccAmountLimit Where AccNumber= ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNumber);
		ResultSet rs =pstmt.executeQuery();
		
		while(rs.next()){
			ret = true ;
		}
		return ret;
	}
	
	public static ArrayList<String> checkEntryDate(String accountNumber, ArrayList<String> lstUniEntryDate, Connection pConn) throws SQLException {
		ArrayList<String> lsuniversalmonth = new ArrayList<String>();
		boolean ret = false;
		String checkMonth = "";
		String checkYear = "";
		PreparedStatement pstmt = null;
		for(int i =0;i<lstUniEntryDate.size();i++){
			checkMonth=lstUniEntryDate.get(i).split("/")[0];
			checkYear = lstUniEntryDate.get(i).split("/")[1];
			ret = true;
			String query = "SELECT SubString(Convert(nvarchar,EntryDate,103),4,8) FROM UniversalAccAmountLimit Where AccNumber=? AND SubString(Convert(nvarchar,EntryDate,111),0,8)>?";
			pstmt = pConn.prepareStatement(query);
			pstmt.setString(1, accountNumber);
			pstmt.setString(2, checkYear+"/" + checkMonth);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String data = rs.getString(1);
				lsuniversalmonth.add(data);
				ret = false;
			}
			if(!ret) break;
			pstmt.close();
		}
		
		return lsuniversalmonth;
	}
}
