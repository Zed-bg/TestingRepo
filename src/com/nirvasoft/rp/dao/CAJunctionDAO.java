package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.CustomerData;
import com.nirvasoft.rp.util.SharedUtil;

import jdk.internal.org.xml.sax.SAXException;

public class CAJunctionDAO {
	private ArrayList<AccountCustomerData> lstCAJunBean;
	private AccountCustomerData CAJunBean;
	
	
	public void setCAJunctionBean(AccountCustomerData aCAJunBean) {
		CAJunBean = aCAJunBean;
	}
	public AccountCustomerData getCAJunctionBean() {
		return CAJunBean;
	}
	
	public void setCAJunctionBeanList(ArrayList<AccountCustomerData> aCAJunBeanList) {
		lstCAJunBean = aCAJunBeanList;
	}
	public ArrayList<AccountCustomerData> getCAJunctionBeanList() {
		return lstCAJunBean;
	}	
	
	public CAJunctionDAO() {
		lstCAJunBean = new ArrayList<AccountCustomerData>();
		CAJunBean = new AccountCustomerData();		
	}
	
	  public boolean getCAJunctions(String aAccNumber, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
	    	boolean result=false;

	    	PreparedStatement pstmt= null;
	    	pstmt=conn.prepareStatement("SELECT AccNumber, CustomerId, AccType, RType,RowNo FROM CAJunction  WHERE AccNumber=? ORDER BY RowNo,RType");
	        pstmt.setString(1, aAccNumber);
	        pstmt.setQueryTimeout(DAOManager.getNormalTime());  
	        ResultSet rs=pstmt.executeQuery();
	        
	        int i=0;
	        while(rs.next()){
	        	CustomerDAO l_CusDAO = new CustomerDAO();
	        	AccountCustomerData l_CAJunBean = new AccountCustomerData();
	        	readRecord(l_CAJunBean, rs);
	        	if(l_CusDAO.getCustomer(l_CAJunBean.getCustomerID(), conn))
	        	{
	        		l_CAJunBean.setCustomer(l_CusDAO.getObjCustBean());
	        	}
	        	lstCAJunBean.add(i++, l_CAJunBean);
	        	result=true;
	        }
	        pstmt.close();

	        return result;
	 }
	  
	  private void readRecord(AccountCustomerData aCAJunBean,ResultSet aRS) throws SQLException { 
			aCAJunBean.setAccountNumber(aRS.getString("AccNumber"));
			aCAJunBean.setCustomerID(aRS.getString("CustomerId"));
			aCAJunBean.setAccountType(aRS.getInt("AccType"));
			aCAJunBean.setRelationType(aRS.getInt("RType"));
			aCAJunBean.setRowNo(aRS.getInt("RowNo"));	
	  }
	  
}
