package com.nirvasoft.rp.shared;
/* 
TUN THURA THET 2011 04 21
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.nirvasoft.rp.shared.icbs.ReferenceAccountsData;

public class SystemData implements Serializable{

	private static final long serialVersionUID = 51525865987834667L;
	private String pNewLabel="new*";
	private ArrayList<String> pSystemCodes;
	private ArrayList<String> pSuggests;
	private ArrayList<String> pTitles;
	private BankData pBankData;
	private ArrayList<ReferenceData> pPersonTypes;
	private ArrayList<ReferenceData> pOrganizationTypes;
	private ArrayList<ReferenceData> pTownships;
	private ArrayList<ReferenceData> pDistricts;
	private ArrayList<ReferenceData> pDivisions;
	private ArrayList<ReferenceData> pCountries;
	private ArrayList<ReferenceData> pAccountTypes;
	private ArrayList<ReferenceData> pDrawingTypes;
	private ArrayList<ReferenceData> pCurrencyCodes;
	private ArrayList<ReferenceData> pBranchCodes;
	private ArrayList<ReferenceData> pAllBranchCodes;
	private ArrayList<ReferenceData> pStatusCodes;
	private ArrayList<ReferenceData> pACRelationTypes;
	private ArrayList<TerminalData> pTerminalCodes;	
	private ArrayList<String> pNationalities;
	private ArrayList<String> pRaces;
	private ArrayList<String> pReligions;
	private ArrayList<ProductData> pProducts;
	private ArrayList<ReferenceData> pUserLevels;
	private ArrayList<ReferenceData> pPBookStatus;
	private ArrayList<ReferenceData> pTitlesSex;
	private ArrayList<ReferenceData> pAddressRefByTownship;
	private ArrayList<ReferenceData> pButtons;
	ArrayList<ProductData> pProductType;
	private ArrayList<ReferenceData> pAccBarType;
	private ArrayList<String> pNrcs;
	private ArrayList<RefDenoNameDataset> pRefDenoNameList;
	
	//WMMH OtherBranchCode
	private ArrayList<BranchReferenceData> pOtherBranchCodes;
	
	//TXL Start
	private ArrayList<ProductData> pProductsType;
	private ArrayList<ProductData> pProductsNumber;
	private ArrayList<ProductData> pProductsConfiguration;
	private ArrayList<ReferenceData> pAccountStatus;
	private ArrayList<ReferenceData> pAccountFormat;
	private ArrayList<ProductData> pAccountSeparateNumber;
	private int logOffTime = 0 ;
	
	private ArrayList<ReferenceData> pChequeType;
	private ArrayList<ReferenceData> pAccountProductCodes;
	private ArrayList<String> prdNumbering;
	private String pByForceCheque ;
	private String pAuthorizerID;
	private HashMap<String,ProductData> pProductDataList;
	int accMinSetting;
	private HashMap<String,SystemSettingData> pSystemSettingDataList;
	private HashMap<String,ProductData> pProductList;
	private HashMap<String,Double> pFECCurrencyRateList;
	private HashMap<String, ReferenceAccountsData> pReferenceAccCodeList;
	private boolean isCutOfTime;
	
	int curBalSetting;
	int barminbalSetting;

	public int getLogOffTime(){return this.logOffTime;}
	public void setLogOffTime(int value){
		if(value < 10){
			this.logOffTime = 10;
		}else{
			this.logOffTime = value/10;
		}
	}
	
	
	//TXL End
	private ArrayList<SystemSettingData> pSystemSettingDatas;
	// WHA
	private double pMaxATMTransAmount = 300000;
	// 03 Jan 2012
	private ArrayList<CollateralInformationData> pCollTypeReference;
	//HMT
	private ArrayList<CurrencyData> pCurrencyList;
	
	// Currency
	private String pBaseCurCode;
	private double pBaseCurRate = 1;
	private String pBaseCurOperator = "/";
	private ArrayList<RefDenominationData> pRefDenominationDatas;
	private ArrayList<ProductData> pProductDatasCurrency;
	private ArrayList<ZoneSetupData> pZoneSetupDatalist;
	private ArrayList<CounterData> pCounterTypList;	
	
	//WMMH Mobile junction status
	private ArrayList<ReferenceData> pMJunctionStatus;
	public ArrayList<ReferenceData> getMJunctionStatus(){return pMJunctionStatus;}
	public void setMJunctionStatus(ArrayList<ReferenceData> p) {pMJunctionStatus=p;}

	//SSH For Other Bank
	private ArrayList<BranchReferenceData> pBranchRefDatalist;
	
	public ArrayList<BranchReferenceData> getBranchRefDatalist() {
		return pBranchRefDatalist;
	}
	public void setBranchRefDatalist(
			ArrayList<BranchReferenceData> pBranchRefDatalist) {
		this.pBranchRefDatalist = pBranchRefDatalist;
	}
	public ArrayList<ZoneSetupData> getZoneSetupDatalist() {
		return pZoneSetupDatalist;
	}
	
	public void setZoneSetupDatalist(ArrayList<ZoneSetupData> pZoneSetupDatalist) {
		this.pZoneSetupDatalist = pZoneSetupDatalist;
	}	
	public ArrayList<CounterData> getCounterTypList() {
		return pCounterTypList;
	}
	public void setCounterTypList(ArrayList<CounterData> pCounterTypList) {
		this.pCounterTypList = pCounterTypList;
	}

	//HMT 05/08/2012
	public ArrayList<FeePolicyData> pFeePolicyDatas;		
	public ArrayList<FeePolicyData> getFeePolicyDatas() {	return pFeePolicyDatas;	}
	public void setFeePolicyDatas(ArrayList<FeePolicyData> pFeePolicyDatas) {this.pFeePolicyDatas = pFeePolicyDatas;	}
	public ArrayList<RefInstitutionData> pRefInstitutionDatas;	
	public ArrayList<RefInstitutionData> getRefInstitutionDatas() {	return pRefInstitutionDatas;	}
	public void setRefInstitutionDatas(ArrayList<RefInstitutionData> pRefInstitutionDatas) {
		this.pRefInstitutionDatas = pRefInstitutionDatas;
	}	
	public ArrayList<RefMerchantData> pRefMerchantDatas;
	public ArrayList<RefMerchantData> getRefMerchantDatas() {return pRefMerchantDatas;}
	public void setRefMerchantDatas(ArrayList<RefMerchantData> pRefMerchantDatas) {
		this.pRefMerchantDatas = pRefMerchantDatas;
	}
	//SSH For Interest
	private ArrayList<InterestPayTypeData> pInterestPayTypeDatas;
	
	// WHA for menu link data
	private ArrayList<MenuLinkData> pArlMenuLinkDatas;
	
	public ArrayList<ReferenceData> getAccBarType() {return pAccBarType;}
	public void setAccBarType(ArrayList<ReferenceData> pAccBarType) {this.pAccBarType = pAccBarType;}
	public String getNewLabel(){return pNewLabel;}
	public ArrayList<ProductData> getProducts(){return pProducts;}
	public void setProducts(ArrayList<ProductData> p) {pProducts=p;}
	public ArrayList<String> getSystemCodes(){return pSystemCodes;}
	public void setSystemCodes(ArrayList<String> p) {pSystemCodes=p;}
	public ArrayList<String> getTitles(){return pTitles;}
	public void setTitles(ArrayList<String> p) {pTitles=p;}
	public ArrayList<ReferenceData> getTitlesSex(){return pTitlesSex;}
	public void setTitlesSex(ArrayList<ReferenceData> p) {pTitlesSex=p;}
	public ArrayList<String> getSuggests(){return pSuggests;}
	public void setSuggests(ArrayList<String> p) {pSuggests=p;}
	public ArrayList<String> getNationalities(){return pNationalities;}
	public void setNationalities(ArrayList<String> p) {pNationalities=p;}
	public ArrayList<String> getRaces(){return pRaces;}
	public void setRaces(ArrayList<String> p) {pRaces=p;}
	public ArrayList<String> getReligions(){return pReligions;}
	public void setReligions(ArrayList<String> p) {pReligions=p;}
	public ArrayList<ReferenceData> getTownships(){return pTownships;}
	public void setTownships(ArrayList<ReferenceData> p) {pTownships=p;}
	public ArrayList<ReferenceData> getDistricts(){return pDistricts;}
	public void setDistricts(ArrayList<ReferenceData> p) {pDistricts=p;}
	public ArrayList<ReferenceData> getDivisions(){return pDivisions;}
	public void setDivisions(ArrayList<ReferenceData> p) {pDivisions=p;}
	public ArrayList<ReferenceData> getCountries(){return pCountries;}
	public void setCountries(ArrayList<ReferenceData> p) {pCountries=p;}	
	public ArrayList<ReferenceData> getOrganizationTypes(){return pOrganizationTypes;}
	public void setOrganizationTypes(ArrayList<ReferenceData> p) {pOrganizationTypes=p;}
	public ArrayList<ReferenceData> getPersonTypes(){return pPersonTypes;}
	public void setPersonTypes(ArrayList<ReferenceData> p) {pPersonTypes=p;}
	public ArrayList<ReferenceData> getAccountTypes(){return pAccountTypes;}
	public void setAccountTypes(ArrayList<ReferenceData> p) {pAccountTypes=p;}
	public ArrayList<ReferenceData> getACRelationTypes(){return pACRelationTypes;}
	public void setACRelationTypes(ArrayList<ReferenceData> p) {pACRelationTypes=p;}
	public ArrayList<ReferenceData> getDrawingTypes(){return pDrawingTypes;}
	public void setDrawingTypes(ArrayList<ReferenceData> p) {pDrawingTypes=p;}
	public ArrayList<ReferenceData> getCurrencyCodes(){return pCurrencyCodes;}
	public void setCurrencyCodes(ArrayList<ReferenceData> p) {pCurrencyCodes=p;}
	public ArrayList<ReferenceData> getBranchCodes(){return pBranchCodes;}
	public ArrayList<ReferenceData> getAllBranchCodes(){return pAllBranchCodes;}
	public void setBranchCodes(ArrayList<ReferenceData> p) {pBranchCodes=p;}
	public void setAllBranchCodes(ArrayList<ReferenceData> p) {pAllBranchCodes=p;}
	public ArrayList<TerminalData> getTerminalCodes(){return pTerminalCodes;}
	public void setTerminalCodes(ArrayList<TerminalData> p) {pTerminalCodes=p;}
	public ArrayList<ReferenceData> getStatusCodes(){return pStatusCodes;}
	public void setStatusCodes(ArrayList<ReferenceData> p) {pStatusCodes=p;}

	public ArrayList<ReferenceData> getUserLevels(){return pUserLevels;}
	public void setUserLevels(ArrayList<ReferenceData> p) {pUserLevels=p;}
	
	public ArrayList<ReferenceData> getPBookStatus(){return pPBookStatus;}
	public void setPBookStatus(ArrayList<ReferenceData> p) {pPBookStatus=p;}
	public ArrayList<ReferenceData> getAddressRefByTownship(){return pAddressRefByTownship;}
	public void setAddressRefByTownship(ArrayList<ReferenceData> p) {pAddressRefByTownship=p;}
	public ArrayList<ReferenceData> getButtons(){return pButtons;}
	public void setButtons(ArrayList<ReferenceData> p) {pButtons=p;}
	public ArrayList<ProductData> getProductType() {return pProductType;}
	public void setProductType(ArrayList<ProductData> pProductType) {this.pProductType = pProductType;}
	
	//TXL Start
	public ArrayList<ProductData> getProductsType(){return pProductsType;}
	public void setProductsType(ArrayList<ProductData> p) {pProductsType=p;}
	
	public ArrayList<ProductData> getProductsNumber(){return pProductsNumber;}
	public void setProductsNumber(ArrayList<ProductData> p) {pProductsNumber=p;}
	
	public ArrayList<ProductData> getProductsConfiguration(){return pProductsConfiguration;}
	public void setProductsConfiguration(ArrayList<ProductData> p) {pProductsConfiguration=p;}
	
	public ArrayList<ReferenceData> getAccountStatus(){return pAccountStatus;}
	public void setAccountStatus(ArrayList<ReferenceData> p) {pAccountStatus=p;}
	
	public ArrayList<ProductData> getAccountSeparateNumber(){return pAccountSeparateNumber;}
	public void setAccountSeparateNumber(ArrayList<ProductData> p) {pAccountSeparateNumber=p;}
	
	public ArrayList<ReferenceData> getAccountFormat(){return pAccountFormat;}
	public void setAccountFormat(ArrayList<ReferenceData> p) {pAccountFormat=p;}
	
	//TXL End
	
	//wnnt
	public ArrayList<String> getNrcs(){return pNrcs;}
	public void setNrcs(ArrayList<String> p) {pNrcs=p;}
	
	public ArrayList<SystemSettingData> getSystemSettingDatas(){return pSystemSettingDatas;}// WHA ==> For SystemSettingData
	public void setSystemSettingDatas(ArrayList<SystemSettingData> p) {pSystemSettingDatas = p;}
	
	// 03 Jan 2012
	public void setCollTypeReference(ArrayList<CollateralInformationData> pCollTypeReference) {this.pCollTypeReference = pCollTypeReference;}
	public ArrayList<CollateralInformationData> getCollTypeReference() {return pCollTypeReference;}
	
	public void setBankData(BankData p) {this.pBankData = p;}
	public BankData getBankData() {return pBankData;}
	
	public double getATMMaxTransAmount(){
		return pMaxATMTransAmount;
	}
	
	public void setBaseCurCode(String pBaseCurCode) {
		this.pBaseCurCode = pBaseCurCode;
	}
	public String getBaseCurCode() {
		return pBaseCurCode;
	}
	
	public void setATMMaxTransAmount(double p){
		pMaxATMTransAmount = p;
	}
	
	public double getBaseCurRate(){
		return pBaseCurRate;
	}
	
	
	public void setBaseCurRate(double p){
		pBaseCurRate = p;
	}
	
	public void setBaseCurOperator(String p){
		pBaseCurOperator = p;
	}
	
	public String getBaseCurOperator(){
		return pBaseCurOperator;
	}
	
	public void setRefDenominationDatas(ArrayList<RefDenominationData> p){
		pRefDenominationDatas = p;
	}
	
	public ArrayList<RefDenominationData> getRefDenominationDatas(){
		return pRefDenominationDatas;
	}
	
	public ArrayList<ProductData> getProductDatasCurrency(){
		return pProductDatasCurrency;
	}
	
	public void setProductDatasCurrency(ArrayList<ProductData> p){
		pProductDatasCurrency = p;
	}
	
	//fec
	
	public void setRefDenoNameList(ArrayList<RefDenoNameDataset> pRefDenoNameList) {
		this.pRefDenoNameList = pRefDenoNameList;
	}
	public ArrayList<RefDenoNameDataset> getRefDenoNameList() {
		return pRefDenoNameList;
	}
	//SSH
	public ArrayList<InterestPayTypeData> getInterestPayTypeDatas() {
		return pInterestPayTypeDatas;
	}
	public void setInterestPayTypeDatas(ArrayList<InterestPayTypeData> pInterestPayTypeDatas) {
		this.pInterestPayTypeDatas = pInterestPayTypeDatas;
	}
	//END SSH
	
	public ArrayList<MenuLinkData> getMenuLinkDatas() {
		return pArlMenuLinkDatas;
	}
	
	public void setMenuLinkDatas(ArrayList<MenuLinkData> pArlMenuLinkDatas) {
		this.pArlMenuLinkDatas = pArlMenuLinkDatas;
	}
	
	private ArrayList<ReferenceData> transactionDesc;
	public ArrayList<ReferenceData> getTransactionDesc(){return this.transactionDesc;}
	public void setTransactionDesc(ArrayList<ReferenceData> value){this.transactionDesc = value;}
	
	public SystemData(){
		pBaseCurCode = "MMK";
		pBaseCurRate = 1;
		pBaseCurOperator = "/";
		pProductDatasCurrency = new ArrayList<ProductData>();
		pArlMenuLinkDatas = new ArrayList<MenuLinkData>();
	}
	
	public void setCurrencyInfo(ArrayList<CurrencyData> pCurrencyList) {
		this.pCurrencyList = pCurrencyList;
	}	
	public ArrayList<CurrencyData> getCurrencyInfo(){
		return this.pCurrencyList;
	}
	
	//POType
	private ArrayList<POTypeData> lstPoType;
	public ArrayList<POTypeData> getPOTypeList() { return lstPoType; }
	public void setPOTypeList(ArrayList<POTypeData> pPOTypeList) { 	this.lstPoType = pPOTypeList; }
	public ArrayList<BranchReferenceData> getOtherBranchCodes() {
		return pOtherBranchCodes;
	}
	public void setOtherBranchCodes(ArrayList<BranchReferenceData> pOtherBranchCodes) {
		this.pOtherBranchCodes = pOtherBranchCodes;
	}
	//GCType
	private ArrayList<GCTypeData> lstgcType;
	public ArrayList<GCTypeData> getGCTypeList() { return lstgcType; }
	public void setGCTypeList(ArrayList<GCTypeData> pGCTypeList) { 	this.lstgcType = pGCTypeList; }
	
	public ArrayList<ReferenceData> getChequeType() {
		return pChequeType;
	}
	public void setChequeType(ArrayList<ReferenceData> pChequeType) {
		this.pChequeType = pChequeType;
	}
	public ArrayList<ReferenceData> getAccountProductCodes() {
		return pAccountProductCodes;
	}
	public void setAccountProductCodes(ArrayList<ReferenceData> pAccountProductCodes) {
		this.pAccountProductCodes = pAccountProductCodes;
	}
	
	public ArrayList<String> getProductNumbering() {
		return prdNumbering;
	}
	public void setProductNumbering(ArrayList<String> numbering) {
		this.prdNumbering = numbering;
	}
	private BudgetYearWhichQData pfirstBYear=null;
	private BudgetYearWhichQData plastBYear=null;

	public BudgetYearWhichQData getPfirstBYear() {
		return pfirstBYear;
	}
	public void setPfirstBYear(BudgetYearWhichQData pfirstBYear) {
		this.pfirstBYear = pfirstBYear;
	}
	public BudgetYearWhichQData getPlastBYear() {
		return plastBYear;
	}
	public void setPlastBYear(BudgetYearWhichQData plastBYear) {
		this.plastBYear = plastBYear;
	}

	private ArrayList<BranchData> pAllBranch;
	private ArrayList<ReferenceData> pCredBankRefDatalist;
	private ArrayList<ReferenceData> pCredBranchRefDatalist;
	private FIGatewayConfigData figwData;
	private ArrayList<PaymentTypeData> paymentTypeData = new ArrayList<PaymentTypeData>();
	private ArrayList<PaymentTypeData> taxTypeData = new ArrayList<PaymentTypeData>();
	
	public FIGatewayConfigData getFigwData() {
		return figwData;
	}
	public void setFigwData(FIGatewayConfigData figwData) {
		this.figwData = figwData;
	}
	public ArrayList<BranchData> getpAllBranch() {
		return pAllBranch;
	}
	public void setpAllBranch(ArrayList<BranchData> pAllBranch) {
		this.pAllBranch = pAllBranch;
	}	
	public ArrayList<ReferenceData> getCredBankRefDatalist() {
		return pCredBankRefDatalist;
	}
	public void setCredBankRefDatalist(ArrayList<ReferenceData> pCredBankRefDatalist) {
		this.pCredBankRefDatalist = pCredBankRefDatalist;
	}
	public ArrayList<ReferenceData> getCredBranchRefDatalist() {
		return pCredBranchRefDatalist;
	}
	public void setCredBranchRefDatalist(ArrayList<ReferenceData> pCredBranchRefDatalist) {
		this.pCredBranchRefDatalist = pCredBranchRefDatalist;
	}
	public ArrayList<PaymentTypeData> getPaymentTypeData() {
		return paymentTypeData;
	}
	public void setPaymentTypeData(ArrayList<PaymentTypeData> paymentTypeData) {
		this.paymentTypeData = paymentTypeData;
	}
	public ArrayList<PaymentTypeData> getTaxTypeData() {
		return taxTypeData;
	}
	public void setTaxTypeData(ArrayList<PaymentTypeData> taxTypeData) {
		this.taxTypeData = taxTypeData;
	}
	private ArrayList<LAODFData> LoanTypeData = new ArrayList<LAODFData>();
	public ArrayList<LAODFData> getLoanTypeData() {
		return LoanTypeData;
	}
	public void setLoanTypeData(ArrayList<LAODFData> loanTypeData) {
		this.LoanTypeData = loanTypeData;
	}
	public String getpByForceCheque() {
		return pByForceCheque;
	}
	public void setpByForceCheque(String pByForceCheque) {
		this.pByForceCheque = pByForceCheque;
	}
	public String getpAuthorizerID() {
		return pAuthorizerID;
	}
	public void setpAuthorizerID(String pAuthorizerID) {
		this.pAuthorizerID = pAuthorizerID;
	}
	public HashMap<String, ProductData> getpProductDataList() {
		return pProductDataList;
	}
	public void setpProductDataList(HashMap<String, ProductData> pProductDataList) {
		this.pProductDataList = pProductDataList;
	}
	public int getAccMinSetting() {
		return accMinSetting;
	}
	public void setAccMinSetting(int accMinSetting) {
		this.accMinSetting = accMinSetting;
	}
	public HashMap<String, SystemSettingData> getpSystemSettingDataList() {
		return pSystemSettingDataList;
	}
	public void setpSystemSettingDataList(HashMap<String, SystemSettingData> pSystemSettingDataList) {
		this.pSystemSettingDataList = pSystemSettingDataList;
	}
	public HashMap<String, ProductData> getpProductList() {
		return pProductList;
	}
	public void setpProductList(HashMap<String, ProductData> pProductList) {
		this.pProductList = pProductList;
	}
	public HashMap<String, Double> getpFECCurrencyRateList() {
		return pFECCurrencyRateList;
	}
	public void setpFECCurrencyRateList(HashMap<String, Double> pFECCurrencyRateList) {
		this.pFECCurrencyRateList = pFECCurrencyRateList;
	}
	public HashMap<String, ReferenceAccountsData> getpReferenceAccCodeList() {
		return pReferenceAccCodeList;
	}
	public void setpReferenceAccCodeList(HashMap<String, ReferenceAccountsData> pReferenceAccCodeList) {
		this.pReferenceAccCodeList = pReferenceAccCodeList;
	}
	public boolean isCutOfTime() {
		return isCutOfTime;
	}
	public void setCutOfTime(boolean isCutOfTime) {
		this.isCutOfTime = isCutOfTime;
	}
	public int getCurBalSetting() {
		return curBalSetting;
	}
	public void setCurBalSetting(int curBalSetting) {
		this.curBalSetting = curBalSetting;
	}
	public int getBarminbalSetting() {
		return barminbalSetting;
	}
	public void setBarminbalSetting(int barminbalSetting) {
		this.barminbalSetting = barminbalSetting;
	}
	
}
