package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.BlackListAccountData;
import com.nirvasoft.rp.shared.BlackListInfoData;
import com.nirvasoft.rp.util.SharedUtil;

public class BlackListInfoDAO {
	private ArrayList<BlackListInfoData> lstBlackListInfo = new ArrayList<BlackListInfoData>();
	private BlackListInfoData l_BlackListInfo = new BlackListInfoData();
	
	public ArrayList<BlackListInfoData> getBlackListInfoList() {
		return lstBlackListInfo;
	}
	public void setBlackListInfoList(ArrayList<BlackListInfoData> lstBlackListInfo) {
		this.lstBlackListInfo = lstBlackListInfo;
	}
	public BlackListInfoData getBlackListInfo() {
		return l_BlackListInfo;
	}
	public void setBlackListInfo(BlackListInfoData l_BlackListInfo) {
		this.l_BlackListInfo = l_BlackListInfo;
	}
	private ArrayList<BlackListAccountData> lstBlackListAccount = new ArrayList<BlackListAccountData>();
	
	
	public boolean FindBlCustomerExistByAccNumber(String pAccNumber,Connection pConn){		
		lstBlackListInfo = new ArrayList<BlackListInfoData>(); 
		boolean ret = false;
		try {
			String l_query = "Select Bla.* from BlackListInfo Bla INNER JOIN CAJunction C ON  bla.CustomerID= " +
					"C.CustomerID Where C.accNumber= ? AND Status='Active'";
			PreparedStatement ps = pConn.prepareStatement(l_query);
			ps.setString(1, pAccNumber);
			ResultSet rs = ps.executeQuery();	
			while (rs.next()) {
				BlackListInfoData object = new BlackListInfoData();
				readrecord(object, rs);
				lstBlackListInfo.add(object);	
				ret = true ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public void readrecord(BlackListInfoData aBlacklistBean, ResultSet aRS) throws SQLException {
		
		aBlacklistBean.setBlacklistID(aRS.getString("BlacklistID"));
		aBlacklistBean.setCustomerID(aRS.getString("CustomerID"));
		aBlacklistBean.setCustomerType(aRS.getInt("CustomerType"));
		aBlacklistBean.setTitle(aRS.getString("Title"));
		aBlacklistBean.setName(aRS.getString("Name"));
		aBlacklistBean.setAlias(aRS.getString("AliasName"));
		aBlacklistBean.setSex(aRS.getByte("Sex"));
		aBlacklistBean.setDOB(SharedUtil.formatDBDate2MIT(aRS.getString("DateOfBirth")));
		aBlacklistBean.setIC(aRS.getString("NrcNo"));
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
		
		aBlacklistBean.setAddress(objAddr);
		
		aBlacklistBean.setStatus(aRS.getString("Status"));
		aBlacklistBean.setOccupation(aRS.getString("Occupation"));
		aBlacklistBean.setFatherName(aRS.getString("FatherName"));
		aBlacklistBean.setUniversalID(aRS.getString("UniversalID"));
		
		aBlacklistBean.setMaritalStatus(aRS.getInt("MStatus"));
		aBlacklistBean.setOldIC(aRS.getString("OldNRCNo"));
		aBlacklistBean.setNationality(aRS.getString("M1"));
		aBlacklistBean.setReligion(aRS.getString("M2"));
		aBlacklistBean.setT1(aRS.getString("T1"));
		aBlacklistBean.setT2(aRS.getString("T2"));
		aBlacklistBean.setT3(aRS.getString("T3"));
		aBlacklistBean.setT4(aRS.getString("T4"));
		aBlacklistBean.setT5(aRS.getString("T5"));
		aBlacklistBean.setT6(aRS.getString("T6"));
		aBlacklistBean.setBcNo(aRS.getString("BCNo"));
		aBlacklistBean.setN1(aRS.getInt("N1"));
		aBlacklistBean.setN2(aRS.getInt("N2"));
		aBlacklistBean.setN3(aRS.getInt("N3"));
		aBlacklistBean.setN4(aRS.getInt("N4"));
		aBlacklistBean.setN5(aRS.getInt("N5"));
		aBlacklistBean.setN6(aRS.getInt("N6"));		
		aBlacklistBean.setpCreatedDate(SharedUtil.formatDBDate2MIT(aRS.getString("createdDate")));
		aBlacklistBean.setpPassportNo(aRS.getString("PassportNo"));
		aBlacklistBean.setpBuildingName(aRS.getString("BuildingName"));
		aBlacklistBean.setpSaveDate(SharedUtil.formatDBDate2MIT(aRS.getString("SaveDate")));
		aBlacklistBean.setpCancelDate(SharedUtil.formatDBDate2MIT(aRS.getString("CancelDate")));
		aBlacklistBean.setpModifiedDate(SharedUtil.formatDBDate2MIT(aRS.getString("ModifiedDate")));
	
	}
	
	public boolean FindBlNameExist(String aName,String aNRC, String NRCOld, Connection pConn){
		lstBlackListInfo = new ArrayList<BlackListInfoData>(); 
		boolean ret = false;		
		try {
				String l_query = "Select * From BlackListInfo Where Name = ? AND Status = 'Approve' ";
				if (aNRC !=null)
					if (!aNRC.equals(""))
					l_query += " AND NRCNo = ?";
				if (!NRCOld.equals(null) && !NRCOld.equals(""))
					l_query += " AND OldNRCNo = ?";
				PreparedStatement ps;
				int index = 1;
				ps = pConn.prepareStatement(l_query);
				ps.setString(index++, aName);
				if (aNRC !=null)
					if (!aNRC.equals(""))
					ps.setString(index++, aNRC);
				if (!NRCOld.equals(""))
					ps.setString(index++, NRCOld);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					BlackListInfoData object = new BlackListInfoData();
					//readRecord(object, rs);
					lstBlackListInfo.add(object);	
					ret = true ;
				}			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean FindBlAccountExist(String Acc,Connection pConn){
		lstBlackListAccount = new ArrayList<BlackListAccountData>(); 
		boolean ret = false;		
		try {
				String l_query = "Select * From BlackListAccount Where Accnumber= ? ";
				PreparedStatement ps;
				ps = pConn.prepareStatement(l_query);
				ps.setString(1, Acc);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					BlackListAccountData object = new BlackListAccountData();
					lstBlackListAccount.add(object);	
					ret = true ;
				}			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
}
