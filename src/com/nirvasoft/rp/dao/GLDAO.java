package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.shared.GLData;

public class GLDAO {
	private GLData objGLData;
	
	public GLDAO()
	{
		objGLData = new GLData();
	}
	
	public boolean getGL(String aAccNumber,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
    	boolean result=false;
    	
        PreparedStatement pstmt=conn.prepareStatement("SELECT AccNo, DeptNo, AccDesc, OB, AccType, MainGroup, " +
     				"SubGroup, SubGroupDesc, AccTypeDescription, Department, CurrentBalance, C1,C2,C3,C4,C5,C6,C7,C8,C9,C10,C11,C12,CurrencyCode,BranchCode FROM Gl WHERE AccNo=?");
        
        pstmt.setString(1, aAccNumber);
        pstmt.setQueryTimeout(DAOManager.getNormalTime());
        ResultSet rs=pstmt.executeQuery();
               
        while(rs.next()){
        	readRecord(objGLData, rs);
        	result=true;
        }
        pstmt.close();

        return result;
	}
	
	private void readRecord(GLData ret,ResultSet aRS) throws SQLException
	{		
		ret.setAccountNumber(aRS.getString("AccNo"));
		ret.setDeptNo(aRS.getString("DeptNo"));
		ret.setDescription(aRS.getString("AccDesc"));
		ret.setOB(aRS.getDouble("OB"));
		ret.setAccType(aRS.getByte("AccType"));
		ret.setMainGroup(aRS.getString("MainGroup"));
		ret.setSubGroup(aRS.getString("SubGroup"));
		ret.setSubGroupDesc(aRS.getString("SubGroupDesc"));
		ret.setAccTypeDescription(aRS.getString("AccTypeDescription"));
		ret.setDept(aRS.getString("Department"));
		ret.setCurrentBalance(aRS.getDouble("CurrentBalance"));
		ret.setC1(aRS.getDouble("C1"));
		ret.setC2(aRS.getDouble("C2"));
		ret.setC3(aRS.getDouble("C3"));
		ret.setC4(aRS.getDouble("C4"));
		ret.setC5(aRS.getDouble("C5"));
		ret.setC6(aRS.getDouble("C6"));
		ret.setC7(aRS.getDouble("C7"));
		ret.setC8(aRS.getDouble("C8"));
		ret.setC9(aRS.getDouble("C9"));
		ret.setC10(aRS.getDouble("C10"));
		ret.setC11(aRS.getDouble("C11"));
		ret.setC12(aRS.getDouble("C12"));
		ret.setCurrencyCode(aRS.getString("CurrencyCode"));
		ret.setBranchCode(aRS.getString("BranchCode"));			
	}
	
	public void setGLData(GLData p) {
		objGLData = p;
	}
	public GLData getGLData() {
		return objGLData;
	}
}
