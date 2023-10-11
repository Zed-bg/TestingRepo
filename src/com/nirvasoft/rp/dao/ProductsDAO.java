package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.ProductNumberData;
import com.nirvasoft.rp.shared.ProductSetupData;
import com.nirvasoft.rp.shared.ProductTypeData;
import com.nirvasoft.rp.shared.icbs.ProductFeatureData;
import com.nirvasoft.rp.shared.icbs.ProductFeatureType;
import com.nirvasoft.rp.shared.SharedLogic;

public class ProductsDAO {
	private String mTableName = "ProductSetup";
	private ArrayList<ProductData> lstProductsData;
	private HashMap<String,ProductData> lstProductsDataList;
	private ProductData ProductsData;

	public ProductsDAO() {
		ProductsData = new ProductData();
		lstProductsData = new ArrayList<ProductData>();
		lstProductsDataList = new HashMap<String,ProductData>();
	}
	
	public ProductData getTProduct(String aProductID, String aAccType, Connection aConn) throws SQLException {
		ProductData l_productData = null;
		PreparedStatement l_pstmt = null;
		String l_Query = "";
		int accMinSetting=SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN1();
		l_Query = "select ps.MinBalance,ps.MinOpeningBalance,ps.MinWithdrawal,ps.MultipleOf,ps.ProductName,ps.MultipleOfp, "
				+ "pt.ProductType,pt.ProductCode,pt.ProcessCode6,pt.ProcessCode5  " + "from " + mTableName
				+ " ps inner join producttype pt on ";
		if (accMinSetting == 1)
			l_Query += "ps.productid = pt.productcode+? where pt.productcode = ?";
		else
			l_Query += "ps.productid = pt.productcode where pt.productcode = ?";
		l_pstmt = aConn.prepareStatement(l_Query);
		if (accMinSetting == 1){
			l_pstmt.setString(1, aAccType);
			l_pstmt.setString(2, aProductID);
		}else{
			l_pstmt.setString(1, aProductID);
		}
		ResultSet rs = l_pstmt.executeQuery();
		if (rs.next()) {
			l_productData = new ProductData();
			l_productData.setProcessCode6(rs.getString("ProcessCode6"));
			readRecordNew(l_productData, rs);

			// Getting Product Feature
			ProductFeatureDAO l_ProductFeatureDAO = new ProductFeatureDAO();
			ArrayList<ProductFeatureData> l_ArlProductFeatureDatas = new ArrayList<ProductFeatureData>();
			l_ArlProductFeatureDatas = l_ProductFeatureDAO.getProdcutFeatures(aProductID, aConn);

			setProductFeatures(l_productData, l_ArlProductFeatureDatas);

		}
		l_pstmt.close();
		rs.close();
		return l_productData;
	}
	
	private void readRecordNew(ProductData aProductData, ResultSet aRS) throws SQLException {
		aProductData.setProductID(aRS.getString("ProductType"));
		aProductData.setProductCode(aRS.getString("ProductCode"));
		aProductData.setProductName(aRS.getString("ProductName"));
		aProductData.setProductName(aRS.getString("ProcessCode5"));
		aProductData.setMinBalance(aRS.getDouble("MinBalance"));
		aProductData.setMinOpeningBalance(aRS.getDouble("MinOpeningBalance"));
		aProductData.setMinWithdrawal(aRS.getDouble("MinWithdrawal"));
		aProductData.setMultipleOfp(aRS.getDouble("MultipleOfp"));
		aProductData.setMultipleOf(aRS.getDouble("MultipleOf"));

	}
	
	private void setProductFeatures(ProductData pProductData, ArrayList<ProductFeatureData> pArlProductFeatureDatas) {
		for (ProductFeatureData productFeatureData : pArlProductFeatureDatas) {
			if (pProductData.getProductCode().equals(productFeatureData.getProductId())) {
				setProductFeatureType(pProductData, productFeatureData);
			}
		}
	}
	
	private void setProductFeatureType(ProductData pProductData, ProductFeatureData pProductFeatureData) {
		if (pProductFeatureData.getFeatureId() == ProductFeatureType.Passbook) {
			pProductData.setHasPassbook(true);
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.Cheque) {
			pProductData.setHasCheque(true);
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.Collateral) {
			pProductData.setHasCollateral(true);
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.HP) {
			pProductData.setHasHirePurchase(true);
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.Card) {
			pProductData.setIsCard(true);
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.ProductType) {
			pProductData.setProductFeatureType(pProductFeatureData.getFeatureValue());
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.CollateralTable) {
			pProductData.setCollateralTable(pProductFeatureData.getFeatureValue());
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.Currency) {
			pProductData.setCurCode(String.valueOf(pProductFeatureData.getFeatureValue()));
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.CashInHandGL) {
			pProductData.setCashInHandGL(pProductFeatureData.getGLCode());
		}

		if (pProductFeatureData.getFeatureId() == ProductFeatureType.ProductGL) {
			pProductData.setProductGL(pProductFeatureData.getGLCode());
		}
	}
	
	public boolean getProducts(Connection conn) throws Exception {
		boolean result = false;
		ProductFeatureDAO l_ProdFeatureDAO = new ProductFeatureDAO();
		PreparedStatement pstmt = conn
				.prepareStatement("SELECT P.ProductType, P.ProductCode, P.ProcessCode5 " + "FROM ProductType P ");
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			ProductData l_ProductData = new ProductData();
			readRecord(l_ProductData, rs);
			// Getting Product Feature
			ArrayList<ProductFeatureData> l_ArlProductFeatureDatas = new ArrayList<ProductFeatureData>();
			l_ArlProductFeatureDatas = l_ProdFeatureDAO.getProdcutFeatures(l_ProductData.getProductCode(), conn);
			l_ProductData.setProductSetupList(getProductSetupDataList(l_ProductData.getProductID(), conn));
			getProductNumber(l_ProductData, conn);
			setProductFeatures(l_ProductData, lstProductsData, l_ArlProductFeatureDatas);
			result = true;
		}
		pstmt.close();
		return result;
	}
	
	private void readRecord(ProductData aProductData, ResultSet aRS) throws SQLException {
		aProductData.setProductID(aRS.getString("ProductType"));
		aProductData.setProductCode(aRS.getString("ProductCode"));
		// aProductData.setProductName(aRS.getString("ProductName"));
		aProductData.setProductName(aRS.getString("ProcessCode5"));
		// aProductData.setMinBalance(aRS.getDouble("MinBalance"));
		// aProductData.setMinOpeningBalance(aRS.getDouble("MinOpeningBalance"));
		// aProductData.setMinWithdrawal(aRS.getDouble("MinWithdrawal"));
		// aProductData.setMultipleOfp(aRS.getDouble("MultipleOfp"));
		// aProductData.setMultipleOf(aRS.getDouble("MultipleOf"));

	}
	
	public ArrayList<ProductSetupData> getProductSetupDataList(String pProductID, Connection pConn)
			throws SQLException {
		ArrayList<ProductSetupData> l_ProductSetupLst = new ArrayList<ProductSetupData>();
		String l_Query = "SELECT [ProductId],[ProductType],[MinBalance],[MinOpeningBalance],[MinWithdrawal],[MultipleOfp],"
				+ "[MultipleOf],[IsPassBook],[IsOpeningBalance],[IsOverdraftFacility],[IsReference],[ProductName]"
				+ " FROM ProductSetup WHERE ProductType = ?";
		PreparedStatement l_pstmt = pConn.prepareStatement(l_Query);
		l_pstmt.setString(1, pProductID);

		ResultSet l_Rs = l_pstmt.executeQuery();
		while (l_Rs.next()) {
			ProductSetupData l_Product = new ProductSetupData();
			readProductSetup(l_Product, l_Rs);
			l_ProductSetupLst.add(l_Product);
		}
		return l_ProductSetupLst;
	}
	
	private void readProductSetup(ProductSetupData pPrdouctSetupData, ResultSet pRS) throws SQLException {
		pPrdouctSetupData.setProductId(pRS.getString("ProductID"));
		pPrdouctSetupData.setProductName(pRS.getString("ProductName"));
		pPrdouctSetupData.setMinBalance(pRS.getDouble("MinBalance"));
		pPrdouctSetupData.setMinOpeningBalance(pRS.getDouble("MinOpeningBalance"));
		pPrdouctSetupData.setMinWithdrawal(pRS.getDouble("MinWithdrawal"));
		pPrdouctSetupData.setMultipleOfp(pRS.getInt("MultipleOfp"));
		pPrdouctSetupData.setMultipleOf(pRS.getDouble("MultipleOf"));
		pPrdouctSetupData.setProductType(pRS.getString("ProductType"));
	}
	
	private void getProductNumber(ProductData aProductData, Connection conn) throws Exception {
		PreparedStatement pstmt = conn
				.prepareStatement("SELECT Code, InfoType,Start,Len FROM ProductNumber WHERE ProductType=? ");
		pstmt.setString(1, aProductData.getProductID());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			readProductNumber(aProductData, rs);
		}
		pstmt.close();
	}
	
	private void readProductNumber(ProductData aProductData, ResultSet aRS) throws SQLException {
		if (aRS.getString("Code").equals("AC")) {
			// aProductData.setProductCode(aRS.getString("InfoType"));
			aProductData.setTypeStart(aRS.getInt("Start"));
			aProductData.setTypeLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("BC")) {
			aProductData.setBranchStart(aRS.getInt("Start"));
			aProductData.setBranchLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("CD")) {
			aProductData.setCheckStart(aRS.getInt("Start"));
			aProductData.setCheckLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("RC")) {
			aProductData.setCurrencyStart(aRS.getInt("Start"));
			aProductData.setCurrencyLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("OC")) {
			aProductData.setOtherStart(aRS.getInt("Start"));
			aProductData.setOtherLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("PC")) {
			aProductData.setProductStart(aRS.getInt("Start"));
			aProductData.setProductLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("RW")) {
			aProductData.setReserveStart(aRS.getInt("Start"));
			aProductData.setReserveLen(aRS.getInt("Len"));
		} else if (aRS.getString("Code").equals("SN")) {
			aProductData.setSerialStart(aRS.getInt("Start"));
			aProductData.setSerialLen(aRS.getInt("Len"));
		}
	}
	
	private void setProductFeatures(ProductData pProductData, ArrayList<ProductData> pProductList,
			ArrayList<ProductFeatureData> pArlProductFeatureDatas) {
		ArrayList<ProductFeatureData> l_lstAccountTypeFeature = new ArrayList<ProductFeatureData>();
		for (ProductFeatureData productFeatureData : pArlProductFeatureDatas) {
			if (pProductData.getProductCode().equals(productFeatureData.getProductId())) {
				setProductFeatureType(pProductData, productFeatureData);
				if (productFeatureData.getFeatureId() == ProductFeatureType.AccTypeGL
						|| productFeatureData.getFeatureId() == ProductFeatureType.AccTypeCashInHandGL) {
					l_lstAccountTypeFeature.add(productFeatureData);
				}
			}
		}
		int count = 0;
		ProductData l_ProductData = new ProductData();
		if (l_lstAccountTypeFeature.size() > 0) {
			for (int i = 0; i < l_lstAccountTypeFeature.size(); i++) {
				if (pProductData.getProductCode().equals(l_lstAccountTypeFeature.get(i).getProductId())) {
					count++;
					prepareProductData(l_ProductData, pProductData);
					if (l_lstAccountTypeFeature.get(i).getFeatureId() == ProductFeatureType.AccTypeGL) {
						l_ProductData.setAcccountTypeGL(l_lstAccountTypeFeature.get(i).getGLCode());
					} else if (l_lstAccountTypeFeature.get(i)
							.getFeatureId() == ProductFeatureType.AccTypeCashInHandGL) {
						l_ProductData.setAccountTypeCashGL(l_lstAccountTypeFeature.get(i).getGLCode());
					}
					if (count % 2 == 0) {
						if (l_lstAccountTypeFeature.get(i).getFeatureValue() == l_lstAccountTypeFeature.get(i - 1)
								.getFeatureValue()) {
							l_ProductData.setAccType(l_lstAccountTypeFeature.get(i).getFeatureValue() + "");
							pProductList.add(l_ProductData);
							l_ProductData = new ProductData();
						}
					}
				}

			}
		} else {
			pProductList.add(pProductData);
		}
	}
	
	private void prepareProductData(ProductData l_ProductData, ProductData pProductData) {
		l_ProductData.setHasPassbook(pProductData.hasPassbook());
		l_ProductData.setHasCheque(pProductData.hasCheque());
		l_ProductData.setHasCollateral(pProductData.hasCollateral());
		l_ProductData.setHasHirePurchase(pProductData.hasHirePurchase());
		l_ProductData.setIsCard(pProductData.isCard());
		l_ProductData.sethasCertificate(pProductData.hasCertificate());
		l_ProductData.setProductFeatureType(pProductData.getProductFeatureType());
		l_ProductData.setCollateralTable(pProductData.getCollateralTable());
		l_ProductData.setCurCode(pProductData.getCurCode());
		l_ProductData.setCashInHandGL(pProductData.getCashInHandGL());
		l_ProductData.setProductGL(pProductData.getProductGL());
		l_ProductData.setProductID(pProductData.getProductID());
		l_ProductData.setProductCode(pProductData.getProductCode());
		l_ProductData.setProductType(pProductData.getProductType());
		l_ProductData.setProductName(pProductData.getProductName());
		l_ProductData.setTypeStart(pProductData.getTypeStart());
		l_ProductData.setTypeLen(pProductData.getTypeLen());
		l_ProductData.setBranchStart(pProductData.getBranchStart());
		l_ProductData.setBranchLen(pProductData.getBranchLen());
		l_ProductData.setCheckStart(pProductData.getCheckStart());
		l_ProductData.setCheckLen(pProductData.getCheckLen());
		l_ProductData.setCurrencyStart(pProductData.getCurrencyStart());
		l_ProductData.setCurrencyLen(pProductData.getCurrencyLen());
		l_ProductData.setOtherStart(pProductData.getOtherStart());
		l_ProductData.setOtherLen(pProductData.getOtherLen());
		l_ProductData.setProductStart(pProductData.getProductStart());
		l_ProductData.setProductLen(pProductData.getProductLen());
		l_ProductData.setReserveStart(pProductData.getReserveStart());
		l_ProductData.setReserveLen(pProductData.getReserveLen());
		l_ProductData.setSerialStart(pProductData.getSerialStart());
		l_ProductData.setSerialLen(pProductData.getSerialLen());
		l_ProductData.setProductSetupList(pProductData.getProductSetupList());
	}
	
	public ArrayList<ProductData> getProductDataList() {
		return lstProductsData;
	}
	
	public HashMap<String,ProductData> getProductsList() {
		return lstProductsDataList;
	}
	
	
	public boolean getProductsType(Connection conn) throws Exception {
		boolean result = false;
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT PT.ProductType, PT.ProductCode, PT.ProcessCode4, PT.ProcessCode5, PS.ProductName "
						+ "FROM ProductType PT INNER JOIN ProductSetUp PS ON PT.ProductType = PS.ProductType WHERE 1=1 ");
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		while (rs.next()) {
			ProductData l_ProductData = new ProductData();
			setProductsType(l_ProductData, rs);
			lstProductsData.add(i++, l_ProductData);
			result = true;
		}
		pstmt.close();
		return result;
	}
	
	private void setProductsType(ProductData aProductData, ResultSet aRS) throws SQLException {
		aProductData.setProductType(aRS.getString("ProductType"));
		aProductData.setProductCode(aRS.getString("ProductCode"));
		aProductData.setProductName(aRS.getString("ProductName"));
	}
	
	public boolean getProductsNumber(Connection conn) throws Exception {
		boolean result = false;
		PreparedStatement pstmt = conn.prepareStatement("SELECT PRODUCTTYPE, PRODUCTCODE FROM PRODUCTTYPE ");
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		while (rs.next()) {
			ProductData l_ProductData = new ProductData();
			l_ProductData.setProductType(rs.getString("ProductType"));
			l_ProductData.setProductCode(rs.getString("ProductCode"));
			getProductNumberDetail(l_ProductData, conn);
			lstProductsData.add(i++, l_ProductData);
			result = true;
		}
		pstmt.close();
		return result;
	}
	
	private void getProductNumberDetail(ProductData aProductData, Connection conn) throws Exception {
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT Code, InfoType,Start,Len FROM ProductNumber WHERE ProductType=? Order By Start");
		pstmt.setString(1, aProductData.getProductType());

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			readProductNumber(aProductData, rs);
			// lstProductsData.add(i++, l_ProductData);
		}
		pstmt.close();

	}
	
	public boolean getProductsConfiguration(Connection conn) throws Exception {
		boolean result = false;
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT ProductType, ProductID, MinBalance, MinOpeningBalance, MinWithdrawal, MultipleOfp, MultipleOf, "
						+ "ProductName FROM ProductSetup WHERE 1=1 ");
		ResultSet rs = pstmt.executeQuery();

		int i = 0;
		while (rs.next()) {
			ProductData l_ProductData = new ProductData();
			setProductsConfiguration(l_ProductData, rs);
			lstProductsData.add(i++, l_ProductData);
			result = true;
		}
		pstmt.close();
		return result;
	}
	
	private void setProductsConfiguration(ProductData pProductData, ResultSet aRS) throws SQLException {
		pProductData.setProductType(aRS.getString("ProductType"));
		pProductData.setProductID(aRS.getString("ProductID"));
		pProductData.setMinBalance(aRS.getDouble("MinBalance"));
		pProductData.setMinOpeningBalance(aRS.getDouble("MinOpeningBalance"));
		pProductData.setMinWithdrawal(aRS.getDouble("MinWithdrawal"));
		pProductData.setMultipleOfp(aRS.getDouble("MultipleOfp"));
		pProductData.setMultipleOf(aRS.getDouble("MultipleOf"));
		pProductData.setProductName(aRS.getString("ProductName"));
	}
	
	public ProductData getProduct(String productID,Connection conn) throws SQLException{
		ProductData productData = new ProductData();
		PreparedStatement pstmt = null;
				
		pstmt = conn.prepareStatement("SELECT ProductId,ProductType,MinBalance,MinOpeningBalance,MinWithdrawal,MultipleOf,ProductName FROM ProductSetup WHERE ProductId=?");			
		pstmt.setString(1, productID);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			productData.setProductID(rs.getString("ProductId"));
			readRecords(productData, rs);
		}			
				
		return productData;
	}
	
	private void readRecords(ProductData productData, ResultSet rs) throws SQLException{	
		productData.setProductID(rs.getString("ProductId"));
		productData.setProductCode(rs.getString("ProductType"));
		productData.setMinBalance(rs.getDouble("MinBalance"));	
		productData.setMinOpeningBalance(rs.getDouble("MinOpeningBalance"));
		productData.setMinWithdrawal(rs.getDouble("MinWithdrawal"));
		productData.setMultipleOf(rs.getDouble("MultipleOf"));
		productData.setProductName(rs.getString("ProductName"));		
	}
	
	public HashMap<String,ProductData> readProduct( Connection aConn) throws SQLException {
		HashMap<String,ProductData> productList = new HashMap<String,ProductData>();
		ProductData l_productData = null;
		PreparedStatement l_pstmt = null;
		String l_Query = "";
		//int accMinSetting=SharedLogic.getSystemSettingT12N1("ACTMINBAL");
		l_Query = "select ps.productid,pt.productcode,ps.MinBalance,ps.MinOpeningBalance,ps.MinWithdrawal,ps.MultipleOf,ps.ProductName,ps.MultipleOfp, "
				+ "pt.ProductType,pt.ProductCode,pt.ProcessCode6,pt.ProcessCode5  " + "from " + mTableName
				+ " ps inner join producttype pt on ";
		//if (accMinSetting == 1)
			l_Query += "substring(ps.productid,1,2) = pt.productcode order by ps.productid";
		//else
		//	l_Query += "ps.productid = pt.productcode order by ps.productid";
		l_pstmt = aConn.prepareStatement(l_Query);
		//if (accMinSetting == 1){
		//	l_pstmt.setString(1, aAccType);
		//	l_pstmt.setString(2, aProductID);
		//}else{
		//	l_pstmt.setString(1, aProductID);
		//}
		ResultSet rs = l_pstmt.executeQuery();
		while (rs.next()) {
			String productid = rs.getString("productid");
			String aProductID = rs.getString("productcode");
			l_productData = new ProductData();
			l_productData.setProcessCode6(rs.getString("ProcessCode6"));
			readRecordNew(l_productData, rs);

			// Getting Product Feature
			ProductFeatureDAO l_ProductFeatureDAO = new ProductFeatureDAO();
			ArrayList<ProductFeatureData> l_ArlProductFeatureDatas = new ArrayList<ProductFeatureData>();
			l_ArlProductFeatureDatas = l_ProductFeatureDAO.getProdcutFeatures(aProductID, aConn);

			setProductFeatures(l_productData, l_ArlProductFeatureDatas);
			
			productList.put(productid, l_productData);

		}
		l_pstmt.close();
		rs.close();
		return productList;
	}
	
	public boolean getProductList(Connection conn) throws Exception {
		boolean result = false;
		ProductFeatureDAO l_ProdFeatureDAO = new ProductFeatureDAO();
		PreparedStatement pstmt = conn
				.prepareStatement("SELECT P.ProductType, P.ProductCode, P.ProcessCode5 " + "FROM ProductType P ");
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			ProductData l_ProductData = new ProductData();
			readRecord(l_ProductData, rs);
			// Getting Product Feature
			ArrayList<ProductFeatureData> l_ArlProductFeatureDatas = new ArrayList<ProductFeatureData>();
			l_ArlProductFeatureDatas = l_ProdFeatureDAO.getProdcutFeatures(l_ProductData.getProductCode(), conn);
			l_ProductData.setProductSetupList(getProductSetupDataList(l_ProductData.getProductID(), conn));
			getProductNumber(l_ProductData, conn);
			setProductFeatures(l_ProductData, lstProductsData, l_ArlProductFeatureDatas);
			result = true;
		}
		pstmt.close();
		return result;
	}
	
	public HashMap<String,ProductData> getProductDataList(Connection conn) throws Exception {
		HashMap<String,ProductData> result = new HashMap<String,ProductData>();
		ProductFeatureDAO l_ProdFeatureDAO = new ProductFeatureDAO();
		PreparedStatement pstmt = conn
				.prepareStatement("SELECT P.ProductType, P.ProductCode, P.ProcessCode5 " + "FROM ProductType P ");
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			ProductData l_ProductData = new ProductData();
			readRecord(l_ProductData, rs);
			// Getting Product Feature
			ArrayList<ProductFeatureData> l_ArlProductFeatureDatas = new ArrayList<ProductFeatureData>();
			l_ArlProductFeatureDatas = l_ProdFeatureDAO.getProdcutFeatures(l_ProductData.getProductCode(), conn);
			l_ProductData.setProductSetupList(getProductSetupDataList(l_ProductData.getProductID(), conn));
			getProductNumber(l_ProductData, conn);
			setProductFeaturesnew(l_ProductData, result, l_ArlProductFeatureDatas);
		}
		pstmt.close();
		return result;
	}
	
	private void setProductFeaturesnew(ProductData pProductData, HashMap<String,ProductData> pProductList,
			ArrayList<ProductFeatureData> pArlProductFeatureDatas) {
		ArrayList<ProductFeatureData> l_lstAccountTypeFeature = new ArrayList<ProductFeatureData>();
		for (ProductFeatureData productFeatureData : pArlProductFeatureDatas) {
			if (pProductData.getProductCode().equals(productFeatureData.getProductId())) {
				setProductFeatureType(pProductData, productFeatureData);
				if (productFeatureData.getFeatureId() == ProductFeatureType.AccTypeGL
						|| productFeatureData.getFeatureId() == ProductFeatureType.AccTypeCashInHandGL) {
					l_lstAccountTypeFeature.add(productFeatureData);
				}
			}
		}
		int count = 0;
		String key= "";
		ProductData l_ProductData = new ProductData();
		if (l_lstAccountTypeFeature.size() > 0) {
			for (int i = 0; i < l_lstAccountTypeFeature.size(); i++) {
				if (pProductData.getProductCode().equals(l_lstAccountTypeFeature.get(i).getProductId())) {
					count++;
					prepareProductData(l_ProductData, pProductData);
					if (l_lstAccountTypeFeature.get(i).getFeatureId() == ProductFeatureType.AccTypeGL) {
						l_ProductData.setAcccountTypeGL(l_lstAccountTypeFeature.get(i).getGLCode());
					} else if (l_lstAccountTypeFeature.get(i)
							.getFeatureId() == ProductFeatureType.AccTypeCashInHandGL) {
						l_ProductData.setAccountTypeCashGL(l_lstAccountTypeFeature.get(i).getGLCode());
					}
					if (count % 2 == 0) {
						if (l_lstAccountTypeFeature.get(i).getFeatureValue() == l_lstAccountTypeFeature.get(i - 1)
								.getFeatureValue()) {
							l_ProductData.setAccType(l_lstAccountTypeFeature.get(i).getFeatureValue() + "");
							key = l_ProductData.getProductCode();
							pProductList.put(key,l_ProductData);
							l_ProductData = new ProductData();
							key = "";
						}
					}
				}

			}
		} else {
			key = pProductData.getProductCode();
			pProductList.put(key,pProductData);
		}
	}
	
	public ArrayList<ProductNumberData> getProductNumbers(String pProductType, Connection conn){
		ArrayList<ProductNumberData> ret = new ArrayList<ProductNumberData>();
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ProductNumber WHERE ProductType = ? ORDER BY Start ");
			pstmt.setString(1, pProductType);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				ProductNumberData obj = new ProductNumberData();
				readRecordProductNumbers(obj, rs);
				ret.add(obj);
			}
			pstmt.close();
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	} 
	
	private void readRecordProductNumbers(ProductNumberData aProductNumberDataBean, ResultSet aRS) throws SQLException{
		aProductNumberDataBean.setProductType(aRS.getString("ProductType"));
		aProductNumberDataBean.setCode(aRS.getString("Code"));
		aProductNumberDataBean.setInfoType(aRS.getString("InfoType"));
		aProductNumberDataBean.setStart(aRS.getInt("Start"));
		aProductNumberDataBean.setLength(aRS.getInt("Len"));
	}
	
	public ProductTypeData readProductType(String prdCode, Connection conn){
		ProductTypeData ret = new ProductTypeData();
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ProductType where productcode=? order by producttype");
			pstmt.setString(1, prdCode);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()){
				ret = new ProductTypeData();
				readProductTypeRecord(ret, rs);
			}
			pstmt.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private void readProductTypeRecord(ProductTypeData aProductData,ResultSet aRS)
	{
		try {
			aProductData.setProductType(aRS.getString("ProductType"));
			aProductData.setProductCode(aRS.getString("ProductCode"));
			aProductData.setProcessCode1(aRS.getString("ProcessCode1"));
			aProductData.setProcessCode2(aRS.getString("ProcessCode2"));
			aProductData.setProcessCode3(aRS.getString("ProcessCode3"));
			aProductData.setProcessCode4(aRS.getString("ProcessCode4"));
			aProductData.setProcessCode5(aRS.getString("ProcessCode5"));
			aProductData.setProcessCode6(aRS.getString("ProcessCode6"));
			aProductData.setProcessCode7(aRS.getString("ProcessCode7"));
			aProductData.setProcessCode8(aRS.getString("ProcessCode8"));
			aProductData.setProcessCode9(aRS.getString("ProcessCode9"));
			aProductData.setProcessCode10(aRS.getString("ProcessCode10"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
