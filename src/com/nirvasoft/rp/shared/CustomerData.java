package com.nirvasoft.rp.shared;
/* 
TUN THURA THET 2011 04 21
*/
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@XmlRootElement
//@JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerData{
	
	public CustomerData()
	{
		ClearProperty();
	}
	
	private String pCustomerID;
	private int pCustomerType;
	private String emails;
	public String getEmails() {
		return emails;
	}
	public void setEmails(String emails) {
		this.emails = emails;
	}

	private String pTitle;
	private String pBranchcode;
	private boolean relatedparty;
	public boolean getRelatedparty() {
		return relatedparty;
	}
	public void setRelatedparty(boolean relatedparty) {
		this.relatedparty = relatedparty;
	}
	public String getpBranchcode() {
		return pBranchcode;
	}
	public void setpBranchcode(String pBranchcode) {
		this.pBranchcode = pBranchcode;
	}

	private String pName;
	private String pAlias;
	private byte pSex;//TBA
	private String pOldIC;//field OldNRC, Old NRC for Myanmar,  Passbook
	private String pOtherIC;//BcNo for Army Officer
	private String status;
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
	private String CusID;
	private int pN1;
	private int pN2;
	private int pN3;
	private int pN4;
	private int pN5;
	private int pN6;
	private int pN7;
	private String pSector;
	private boolean pisBlacklist;
	private boolean isMaha;
	//changes
	private String pIDType;
	private String pNationalityStatus;
	private String pSubmitedID;
	private String pHouseNo;
	private String pStreet;
	private String pWard;
	private String pTownship;
	private String pCity;
	private String pDivision;
	private String pCountry;
	private String pPostalCode;
	private String pRegistrationDate;
	private String ppassportNo;
	private String buildingName;
	private String pBuildingName;
	private String pPhone;
	private String pEmail;
	private String pFax;
	private ArrayList<CustomerDocModel> custDoc;
	private int registerType;
	private int serialNo;
	private String custType;
	private String adminNotes;
	private String blacklistID;
	private String pStatus;
	private String regDate;
	private boolean person;
	private String nameIC;
	private boolean organization;
	private String ncs;
	private String ics;
	private String pIC;	
	private String description;
	///private String pIC;	
	private String dob;
	private String ic;//field NRCNo  National Identification Number
	private ArrayList<String> plstBlacklist;
	private String pDOB;
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getIC() {
		return ic;
	}
	public void setIC(String ic) {
		this.ic = ic;
	}
	
	
	//	public String getpIC() {
//		return pIC;
//	}
//	public void setpIC(String pIC) {
//		this.pIC = pIC;
//	}
	public String getIcs() {
		String ret = ic;
		ret += (!pOldIC.equals("") ? (!ic.equals("") ? "; " : "") + pOtherIC + pOldIC : "" );
		return ret;
		//return ics;
	}
	public void setIcs(String ics) {
		this.ics = ics;
	}
	public String getNcs() {
		String ret = ic;
		ret += (!pOldIC.equals("") ? (!ic.equals("") ? "; " : "") + pOldIC : "" );
		return ret;
		//return ncs;
	}
	public void setNcs(String ncs) {
		this.ncs = ncs;
	}
	public boolean isOrganization() {
		return organization;
	}
	public void setOrganization(boolean organization) {
		this.organization = organization;
	}
	public String getNameIC() {
		return nameIC;
	}
	public void setNameIC(String nameIC) {
		this.nameIC = nameIC;
	}
	public boolean isPerson() {
		return person;
	}
	public void setPerson(boolean person) {
		this.person = person;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public String getpCustomerID() {
		return pCustomerID;
	}
	public void setpCustomerID(String pCustomerID) {
		this.pCustomerID = pCustomerID;
	}
	public int getpCustomerType() {
		return pCustomerType;
	}
	public void setpCustomerType(int pCustomerType) {
		this.pCustomerType = pCustomerType;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	
	
	
	public String getpStatus() {
		return pStatus;
	}
	public void setpStatus(String pStatus) {
		this.pStatus = pStatus;
	}
	public int getRegisterType() {
		return registerType;
	}
	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	public String getpPhone() {
		return pPhone;
	}
	public void setpPhone(String pPhone) {
		this.pPhone = pPhone;
	}
	public String getpEmail() {
		return pEmail;
	}
	public void setpEmail(String pEmail) {
		this.pEmail = pEmail;
	}
	public String getpFax() {
		return pFax;
	}
	public void setpFax(String pFax) {
		this.pFax = pFax;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getpBuildingName() {
		return pBuildingName;
	}
	public void setpBuildingName(String pBuildingName) {
		this.pBuildingName = pBuildingName;
	}
	public String getPpassportNo() {
		return ppassportNo;
	}
	public void setPpassportNo(String ppassportNo) {
		this.ppassportNo = ppassportNo;
	}

	private String pIsHasDoc;
	private int pIsSameAddress;
	
	public String getpUniversalID() {
		return pUniversalID;
	}
	public void setpUniversalID(String pUniversalID) {
		this.pUniversalID = pUniversalID;
	}

	private CustomerPhoto pCustomerPhoto = new CustomerPhoto();
	private AddressData pAddress = new AddressData();
	
	/*public String getpRegDate() {
		return pregDate;
	}
	public void setpRegDate(String pRegDate) {
		this.pregDate = pRegDate;
	}*/
	
	public CustomerPhoto getCustomerPhoto() { return pCustomerPhoto;   }
	public void setCustomerPhoto(CustomerPhoto pCustomerPhoto) { this.pCustomerPhoto = pCustomerPhoto;	}
	// This field is not in table. for future use	
	public void setCustomerID(String customerID) {pCustomerID = customerID;}	
	public String getCustomerID() {return pCustomerID;}
	public void setCustomerType(int p) {pCustomerType = p;}
	public int getCustomerType() {return pCustomerType;}
	///public boolean isOrganization(){if (pCustomerType>=100) return true; else return false;}
	////public boolean isPerson(){return !isOrganization();}
	public void setTitle(String title) {pTitle = title;}
	public String getTitle() {return pTitle;}
	public void setName(String name) {pName = name;}
	public String getName() {return pName;}
	//public String getDescription() {return description;}
	public String getDescription() {return pTitle + " " + pName +"; "+getIcs()+";"+((!dob.equals(null) &&!dob.equals("") )?SharedUtil.formatMIT2DateStr(dob):"");}
//	public String getDescriptionCharAcc() {return pName +"; "+getICs()+";"+((!DOB.equals(""))?SharedUtil.formatMIT2DateStr(DOB):"");}
//	public String getNameIC() {	//HMT 19/05/2012
//		String l_IC = getICs();
//		String l_NameIC = "";		
//		if(getCustomerID().equals("")){
//			l_NameIC = "";
//		}
//		else if(l_IC.equals(""))
//			l_NameIC = pTitle + " " + pName + "; ";
//		else{
//			l_NameIC = pTitle + " " + pName + "; " + l_IC + "; ";
//		}
//		return l_NameIC;
//	}

	public void setAlias(String p) { pAlias = p;}
	public String getAlias() { return pAlias;}
//	public void setDOB(String dateOfBirth) { DOB = dateOfBirth;}
//	public String getDOB() { return DOB;}
	public void setSex(byte sex) { pSex = sex;}
	public byte getSex() { return pSex;}
	
	public void setStatus(String status) { this.status = status;}
	public String getStatus() { return status;}
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
	

	/*public String getICs() {
		String ret = ic;
		ret += (!pOldIC.equals("") ? (!ic.equals("") ? "; " : "") + pOtherIC + pOldIC : "" );
		return ret;
	}*/
	/*public String getNCs() {
		String ret = ic;
		ret += (!pOldIC.equals("") ? (!ic.equals("") ? "; " : "") + pOldIC : "" );
		return ret;
	}*/
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
	public boolean isBlacklist() {
		return pisBlacklist;
	}
	public void setBlacklist(boolean pisBlacklist) {
		this.pisBlacklist = pisBlacklist;
	}
	
//	public String getIc() {
//		return ic;
//	}
//	public void setIc(String ic) {
//		this.ic = ic;
//	}
	private void ClearProperty()
	{
		pCustomerID = "";
		pCustomerType = 0;
		pTitle = "";
		pName = "";
		pAlias = "";
		pSex = 0;
		dob = "";
//		DOB = "";
		//pIC = "";
		pOldIC = "";
		pOtherIC = "";
		//pStatus = "";
		status = "";
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
		pN7=1;
		pSector="";
		pisBlacklist = false;
		//mmmyint 28.10.2016
		odData = new  ArrayList<ODData>();				
		fullname = "";	
		fulladdress = "";
		isMaha= false;
		adminNotes = "";
		ncs ="";
		pDOB = "";
	}
	
	//mmmyint 28.10.2016
	private ArrayList<ODData> odData;
	private String fullname;
	private String fulladdress;
	public  ArrayList<ODData> getOdData() {
		return odData;
	}
	public void setOdData( ArrayList<ODData> odData) {
		this.odData = odData;
	} 
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getFullAddress() {
		return fulladdress;
	}
	public void setFullAddress(String pAddress) {
		this.fulladdress = pAddress;
	}
	public boolean isMaha() {
		return isMaha;
	}
	public void setMaha(boolean isMaha) {
		this.isMaha = isMaha;
	}
	public int getN7() {
		return pN7;
	}
	public void setN7(int pN7) {
		this.pN7 = pN7;
	}
	public String getCusID() {
		return CusID;
	}
	public void setCusID(String cusID) {
		CusID = cusID;
	}
	public String getpIDType() {
		return pIDType;
	}
	public void setpIDType(String pIDType) {
		this.pIDType = pIDType;
	}
	public String getpNationalityStatus() {
		return pNationalityStatus;
	}
	public void setpNationalityStatus(String pNationalityStatus) {
		this.pNationalityStatus = pNationalityStatus;
	}
	public String getpSubmitedID() {
		return pSubmitedID;
	}
	public void setpSubmitedID(String pSubmitedID) {
		this.pSubmitedID = pSubmitedID;
	}
	public String getpHouseNo() {
		return pHouseNo;
	}
	public void setpHouseNo(String pHouseNo) {
		this.pHouseNo = pHouseNo;
	}
	public String getpStreet() {
		return pStreet;
	}
	public void setpStreet(String pStreet) {
		this.pStreet = pStreet;
	}
	public String getpWard() {
		return pWard;
	}
	public void setpWard(String pWard) {
		this.pWard = pWard;
	}
	public String getpTownship() {
		return pTownship;
	}
	public void setpTownship(String pTownship) {
		this.pTownship = pTownship;
	}
	public String getpCity() {
		return pCity;
	}
	public void setpCity(String pCity) {
		this.pCity = pCity;
	}
	public String getpDivision() {
		return pDivision;
	}
	public void setpDivision(String pDivision) {
		this.pDivision = pDivision;
	}
	public String getpCountry() {
		return pCountry;
	}
	public void setpCountry(String pCountry) {
		this.pCountry = pCountry;
	}
	public String getpPostalCode() {
		return pPostalCode;
	}
	public void setpPostalCode(String pPostalCode) {
		this.pPostalCode = pPostalCode;
	}
	public String getpRegistrationDate() {
		return pRegistrationDate;
	}
	public void setpRegistrationDate(String pRegistrationDate) {
		this.pRegistrationDate = pRegistrationDate;
	}
	
	public String getpIsHasDoc() {
		return pIsHasDoc;
	}
	public void setpIsHasDoc(String pIsHasDoc) {
		this.pIsHasDoc = pIsHasDoc;
	}
	public int getpIsSameAddress() {
		return pIsSameAddress;
	}
	public void setpIsSameAddress(int pIsSameAddress) {
		this.pIsSameAddress = pIsSameAddress;
	}
	public ArrayList<CustomerDocModel> getCustDoc() {
		return custDoc;
	}
	public void setCustDoc(ArrayList<CustomerDocModel> custDoc) {
		this.custDoc = custDoc;
	}
	public String getAdminNotes() {
		return adminNotes;
	}
	public void setAdminNotes(String adminNotes) {
		this.adminNotes = adminNotes;
	}
	public String getBlacklistID() {
		return blacklistID;
	}
	public void setBlacklistID(String blacklistID) {
		this.blacklistID = blacklistID;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<String> getpBlacklist() {
		return plstBlacklist;
	}
	public void setpBlacklist(ArrayList<String> pBlacklist) {
		this.plstBlacklist = pBlacklist;
	}
	public void setDOB(String dateOfBirth) { pDOB = dateOfBirth;}
}
