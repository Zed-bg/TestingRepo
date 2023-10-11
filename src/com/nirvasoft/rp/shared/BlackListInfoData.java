package com.nirvasoft.rp.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.nirvasoft.rp.client.util.ClientUtil;

@XmlRootElement
//@JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlackListInfoData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7048372750079948555L;
	private String pBlacklistID;
	private String pCustomerID;
	private int pCustomerType;
	private String pTitle;
	private String pName;
	private String pAlias;
	private byte pSex;//TBA
	private String pDOB;
	private String pIC;//field NRCNo  National Identification Number
	private String pOldIC;//field OldNRC, Old NRC for Myanmar,  Passbook
	private String pOtherIC;//BcNo for Army Officer
	private String pStatus;
	private String pOccupation;
	private String pFatherName;
	private String pUniversalID;
	private int pUniversalIDStatus;//No University 0,File Name MStatus, used in centralized 
	private String pNationality;// Field M1
	private String pRace;// Field M1
	private String pReligion;//Field M2
	private String pOther;
	private String pT1;
	private String pT2;
	private String pT3;
	private String pT4;
	private String pT5;
	private String pT6;
	private int pN1;
	private int pN2;
	private int pN3;
	private int pN4;
	private int pN5;
	private int pN6;
	private String pSector;
	private ArrayList<CustomerDocModel> custDoc;
	private String pModifiedDate;	
	private String pSaveDate;
	private String pCancelDate;
	private String Modified_Date;
	
	//new col add by ts
	private String pCreatedDate;
	private String pPassportNo;
	private String pBuildingName;
	
	public ArrayList<CustomerDocModel> getCustDoc() {
		return custDoc;
	}

	public void setCustDoc(ArrayList<CustomerDocModel> custDoc) {
		this.custDoc = custDoc;
	}

	private CustomerPhoto pCustomerPhoto = new CustomerPhoto();
	private AddressData pAddress = new AddressData();
	
	public BlackListInfoData() {
		clearProperty();
	}
	
	public CustomerPhoto getCustomerPhoto() { return pCustomerPhoto;   }
	public void setCustomerPhoto(CustomerPhoto pCustomerPhoto) { this.pCustomerPhoto = pCustomerPhoto;	}
	// This field is not in table. for future use	
	public void setBlacklistID(String customerID) {pBlacklistID = customerID;}	
	public String getBlacklistID() {return pBlacklistID;}
	public void setCustomerID(String customerID) {pCustomerID = customerID;}	
	public String getCustomerID() {return pCustomerID;}
	public void setCustomerType(int p) {pCustomerType = p;}
	public int getCustomerType() {return pCustomerType;}
	public boolean isOrganization(){if (pCustomerType>=100) return true; else return false;}
	public boolean isPerson(){return !isOrganization();}
	public void setTitle(String title) {pTitle = title;}
	public String getTitle() {return pTitle;}
	public void setName(String name) {pName = name;}
	public String getName() {return pName;}
	public String getDescription() {return pTitle + " " + pName +"; "+getICs()+";"+((!pDOB.equals(""))?ClientUtil.formatMIT2DateStr(pDOB):"");}
	public String getDescriptionCharAcc() {return pName +"; "+getICs()+";"+((!pDOB.equals(""))?ClientUtil.formatMIT2DateStr(pDOB):"");}
	public String getNameIC() {	//HMT 19/05/2012
		String l_IC = getICs();
		String l_NameIC = "";		
		if(getCustomerID().equals("")){
			l_NameIC = "";
		}
		else if(l_IC.equals(""))
			l_NameIC = pTitle + " " + pName + "; ";
		else{
			l_NameIC = pTitle + " " + pName + "; " + l_IC + "; ";
		}
		return l_NameIC;
	}
	public void setAlias(String p) { pAlias = p;}
	public String getAlias() { return pAlias;}
	public void setDOB(String dateOfBirth) { pDOB = dateOfBirth;}
	public String getDOB() { return pDOB;}
	public void setSex(byte sex) { pSex = sex;}
	public byte getSex() { return pSex;}
	public void setIC(String nrcNo) { pIC = nrcNo;}
	public String getIC() { return pIC;}
	public void setStatus(String status) { pStatus = status;}
	public String getStatus() { return pStatus;}
	public void setOccupation(String occupation) { pOccupation = occupation;}
	public String getOccupation() { return pOccupation;}
	public void setFatherName(String fatherName) { pFatherName = fatherName;}
	public String getFatherName() { return pFatherName;}
	public void setUniversalID(String universalID) { pUniversalID = universalID;}
	public String getUniversalID() { return pUniversalID;}
	public void setMaritalStatus(int mstatus) { pUniversalIDStatus = mstatus;}
	public int getMaritalStatus() { return pUniversalIDStatus;}
	public void setOldIC(String oldIC) { pOldIC = oldIC;}
	public String getOldIC() { return pOldIC;}
	public void setNationality(String m1) { pNationality = m1;}
	public String getNationality() { return pNationality;}
	public void setRace(String m1) { pRace = m1;}
	public String getRace() { return pRace;}
	public void setReligion(String m2) { pReligion = m2;}
	public String getReligion() { return pReligion;}
	public void setT1(String t1) { pT1 = t1;}
	public String getT1() { return pT1;}
	public void setT2(String t2) { pT2 = t2;}
	public String getT2() { return pT2;}
	public void setT3(String t3) { pT3 = t3;}
	public String getT3() { return pT3;}
	public void setT4(String t4) { pT4 = t4;}
	public String getT4() { return pT4;}
	public void setT5(String t5) { pT5 = t5;}
	public String getT5() { return pT5;}
	public void setT6(String t6) { pT6 = t6;}
	public String getT6() { return pT6;}
	public void setBcNo(String bcNo) { pOtherIC = bcNo;}
	public String getBcNo() { return pOtherIC;}	
	public void setAddress(AddressData address) { pAddress = address;}
	public AddressData getAddress() {return pAddress;}
	public void setOther(String other) { pOther = other; }
	public String getOther() { return pOther; }

	public String getICs() {
		String ret = pIC;
		ret += (!pOldIC.equals("") ? (!pIC.equals("") ? "; " : "") + pOtherIC + pOldIC : "" );
		return ret;
	}
	public String getNCs() {
		String ret = pIC;
		ret += (!pOldIC.equals("") ? (!pIC.equals("") ? "; " : "") + pOldIC : "" );
		return ret;
	}
	public int getN1() {
		return pN1;
	}
	public void setN1(int pN1) {
		this.pN1 = pN1;
	}
	public int getN2() {
		return pN2;
	}
	public void setN2(int pN2) {
		this.pN2 = pN2;
	}
	public int getN3() {
		return pN3;
	}
	public void setN3(int pN3) {
		this.pN3 = pN3;
	}
	public int getN4() {
		return pN4;
	}
	public void setN4(int pN4) {
		this.pN4 = pN4;
	}
	public int getN5() {
		return pN5;
	}
	public void setN5(int pN5) {
		this.pN5 = pN5;
	}
	public int getN6() {
		return pN6;
	}
	public void setN6(int pN6) {
		this.pN6 = pN6;
	}
	
	public String getSector() {
		return pSector;
	}
	public void setSector(String pSector) {
		this.pSector = pSector;
	}
	
	
	public String getpModifiedDate() {
		return pModifiedDate;
	}

	public void setpModifiedDate(String pModifiedDate) {
		this.pModifiedDate = pModifiedDate;
	}

	public String getpSaveDate() {
		return pSaveDate;
	}

	public void setpSaveDate(String pSaveDate) {
		this.pSaveDate = pSaveDate;
	}

	public String getpCancelDate() {
		return pCancelDate;
	}

	public void setpCancelDate(String pCancelDate) {
		this.pCancelDate = pCancelDate;
	}

	public String getModified_Date() {
		return Modified_Date;
	}

	public void setModified_Date(String modified_Date) {
		Modified_Date = modified_Date;
	}
	
	//new col add by ts
	public String getpCreatedDate() {
		return pCreatedDate;
	}

	public void setpCreatedDate(String pCreatedDate) {
		this.pCreatedDate = pCreatedDate;
	}

	public String getpPassportNo() {
		return pPassportNo;
	}

	public void setpPassportNo(String pPassportNo) {
		this.pPassportNo = pPassportNo;
	}

	public String getpBuildingName() {
		return pBuildingName;
	}

	public void setpBuildingName(String pBuildingName) {
		this.pBuildingName = pBuildingName;
	}
	

	private void clearProperty()
	{
		pBlacklistID = "";
		pCustomerID = "";
		pCustomerType = 0;
		pTitle = "";
		pName = "";
		pAlias = "";
		pSex = 0;
		pDOB = "";
		pIC = "";
		pOldIC = "";
		pOtherIC = "";
		pStatus = "";
		pOccupation = "";
		pFatherName = "";
		pUniversalID = "";
		pUniversalIDStatus = 0; 
		pNationality = "";
		pOther = "";
		pRace = "";
		pReligion = "";
		pT1 = "";
		pT2 = "";
		pT3 = "";
		pT4 = "";
		pT5 = "";
		pT6 = "19000101";
		pN1=0;
		pN2=0;
		pN3=0;
		pN4=0;
		pN5=0;
		pN6=0;
		pSector="";		
		pModifiedDate = "";
		pSaveDate = "";
		pCancelDate = "";
		Modified_Date = "";
		pCreatedDate = "";
		pPassportNo = "";
		pBuildingName = "";
	}
	
}
