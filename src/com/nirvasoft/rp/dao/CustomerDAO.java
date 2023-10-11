package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.CustomerData;
import com.nirvasoft.rp.util.SharedUtil;

import jdk.internal.org.xml.sax.SAXException;

public class CustomerDAO {
	private ArrayList<CustomerData> lstObjCustBean;
	private CustomerData ObjCustBean;	

	public List<CustomerData> getlstObjCust() 
	{
		return lstObjCustBean;
	}

	public void setObjCustBean(CustomerData objCustBean) {
		ObjCustBean = objCustBean;
	}

	public CustomerData getObjCustBean() {
		return ObjCustBean;
	}
	
	public CustomerDAO()
	{
		ObjCustBean = new CustomerData();
		lstObjCustBean = new ArrayList<CustomerData>();
		
	}
	
	public boolean getCustomer(String aCusID, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
    	boolean result=false;
    	
        PreparedStatement pstmt=conn.prepareStatement("select CustomerId,CustomerType,Title,name,AliasName," +
					"Sex,DateOfBirth,NrcNo,HouseNo,Street,Ward,Township,City,Division,Country," +
					"phone,email,Fax,PostalCode,Status,Occupation,FatherName,Universalid,MStatus," +
					"OldNrcNo,M1,M2,T1,T2,T3,T4,T5,T6,BCNo,N1,N2,N3,N4,N5,N6 from Customer Where CustomerId=?");
        
        pstmt.setString(1, aCusID);
        ResultSet rs=pstmt.executeQuery();
               
        while(rs.next()){
        	readRecord(ObjCustBean, rs);
        	result=true;
        }
        pstmt.close();

        return result;
	}
	
	// pearl cust info
			private void readRecord(CustomerData aCustomerBean,ResultSet aRS) throws SQLException
			{
				
					aCustomerBean.setCustomerID(aRS.getString("CustomerID"));
					aCustomerBean.setCustomerType(aRS.getInt("CustomerType"));
					aCustomerBean.setTitle(aRS.getString("Title"));
					aCustomerBean.setName(aRS.getString("Name"));
					aCustomerBean.setAlias(aRS.getString("AliasName"));
					aCustomerBean.setSex(aRS.getByte("Sex"));
					aCustomerBean.setDob(SharedUtil.formatDBDate2MIT(aRS.getString("DateOfBirth")));
					aCustomerBean.setIC(aRS.getString("NrcNo"));
					AddressData objAddr =new AddressData();
					
					objAddr.setHouseNo (aRS.getString("HouseNo"));
					objAddr.setStreet(aRS.getString("Street"));
					objAddr.setWard(aRS.getString("Ward"));
					objAddr.setTownship(aRS.getString("TownShip"));
					objAddr.setCity(aRS.getString("City"));
					objAddr.setDivision(aRS.getString("Division"));
					objAddr.setCountry(aRS.getString("Country"));
					try{
						if(!aRS.getString("Phone").trim().equals("") && aRS.getString("Phone").trim().split(",").length >0){
							String st = aRS.getString("Phone");
							objAddr.setTel1(st.split(",")[0]);
							objAddr.setTel2(st.split(",").length > 1 ? st.split(",")[1] : "");
							objAddr.setTel3(st.split(",").length > 2 ? st.split(",")[2] : "");
						}
					} catch (Exception e){}
					
					objAddr.setEmail(aRS.getString("Email"));
					
					objAddr.setFax(aRS.getString("Fax"));
					objAddr.setPostalCode(aRS.getString("PostalCode"));
					
					aCustomerBean.setAddress(objAddr);
					
					aCustomerBean.setStatus(aRS.getString("Status"));
					aCustomerBean.setOccupation(aRS.getString("Occupation"));
					aCustomerBean.setFatherName(aRS.getString("FatherName"));
					aCustomerBean.setUniversalID(aRS.getString("UniversalID"));
					
					aCustomerBean.setMaritalStatus(aRS.getInt("MStatus"));
					aCustomerBean.setOldIC(aRS.getString("OldNRCNo"));
					aCustomerBean.setNationality(aRS.getString("M1"));
					aCustomerBean.setReligion(aRS.getString("M2"));
					aCustomerBean.setT1(aRS.getString("T1"));
					aCustomerBean.setT2(aRS.getString("T2"));
					aCustomerBean.setT3(aRS.getString("T3"));
					aCustomerBean.setT4(aRS.getString("T4"));
					aCustomerBean.setT5(aRS.getString("T5"));
					aCustomerBean.setT6(aRS.getString("T6"));
					aCustomerBean.setBcNo(aRS.getString("BCNo"));
					aCustomerBean.setN1(aRS.getInt("N1"));
					aCustomerBean.setN2(aRS.getInt("N2"));
					aCustomerBean.setN3(aRS.getInt("N3"));
					aCustomerBean.setN4(aRS.getInt("N4"));
					aCustomerBean.setN5(aRS.getInt("N5"));
					aCustomerBean.setN6(aRS.getInt("N6"));	
			}
}
