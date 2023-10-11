package com.nirvasoft.rp.mgr;

import java.sql.Connection;
import java.util.ArrayList;

import com.nirvasoft.rp.dao.ProductsDAO;
import com.nirvasoft.rp.shared.ProductData;

public class DBProductMgr {

	public static ArrayList<ProductData> getProductsType(Connection pConn) throws Exception {
		ArrayList<ProductData> ret = new ArrayList<ProductData>();

		ProductsDAO l_productDAO = new ProductsDAO();
		if (l_productDAO.getProductsType(pConn))
			;
		{
			ret = l_productDAO.getProductDataList();
		}

		return ret;
	}
	
	public static ArrayList<ProductData> getProductsNumber(Connection pConn) throws Exception {
		ArrayList<ProductData> ret = new ArrayList<ProductData>();

		ProductsDAO l_productDAO = new ProductsDAO();
		if (l_productDAO.getProductsNumber(pConn))
			;
		{
			ret = l_productDAO.getProductDataList();
		}

		return ret;
	}
	
	public static ArrayList<ProductData> getProductsConfiguration(Connection pConn) throws Exception {
		ArrayList<ProductData> ret = new ArrayList<ProductData>();

		ProductsDAO l_productDAO = new ProductsDAO();
		if (l_productDAO.getProductsConfiguration(pConn))
			;
		{
			ret = l_productDAO.getProductDataList();
		}

		return ret;
	}
	
}
