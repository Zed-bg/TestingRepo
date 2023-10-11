package com.nirvasoft.rp.dao;
import com.nirvasoft.rp.core.ccbs.LogicMgr;
import com.nirvasoft.rp.shared.AccountLinksCriteriaData;
import com.nirvasoft.rp.shared.AccountLinksData;
import com.nirvasoft.rp.shared.Enum;
import com.nirvasoft.rp.shared.SharedUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
public class AccountLinksDao {
	
	public ArrayList<AccountLinksData> getAccountLinkByParent(String pAccNumber, Connection conn) throws SQLException {
	    ArrayList<AccountLinksData> lstAccLinksData = new ArrayList<AccountLinksData>();
	    PreparedStatement pstmt = null;
	    pstmt = conn.prepareStatement("SELECT ParentAccount,ChildAccount,Priority,LinkDate,Remark FROM AccountLinks WHERE ParentAccount=? order by Priority");
	    pstmt.setString(1, pAccNumber);
	    ResultSet rs = pstmt.executeQuery();
	    while (rs.next()) {
	      AccountLinksData l_AccLinkData = new AccountLinksData();
	      readRecord(l_AccLinkData, rs, "");
	      lstAccLinksData.add(l_AccLinkData);
	    } 
	    pstmt.close();
	    return lstAccLinksData;
	  }
	
	private static void readRecord(AccountLinksData aAccBean, ResultSet aRS, String pType) throws SQLException {
	    aAccBean.setParentAccount(aRS.getString("ParentAccount"));
	    aAccBean.setChildAccount(aRS.getString("ChildAccount"));
	    aAccBean.setPriority(aRS.getInt("Priority"));
	    aAccBean.setLinkDate(SharedUtil.formatDBDate2MIT(aRS.getString("LinkDate")));
	    if (!pType.equals("")) {
	      aAccBean.setCancelDate(SharedUtil.formatDBDate2MIT(aRS.getString("CancelDate")));
	    } else {
	      aAccBean.setCancelDate("99991231");
	    } 
	    aAccBean.setRemark(aRS.getString("Remark"));
	  }
	
	public static ArrayList<AccountLinksData> getAccountlinkList(String pAccNumber, Connection conn){
		ArrayList<AccountLinksData> l_AccountLinkList = new ArrayList<AccountLinksData>();
		PreparedStatement pstmt = null;				
		try {
			pstmt = conn.prepareStatement("SELECT ParentAccount,ChildAccount,Priority,LinkDate,Remark FROM AccountLinks WHERE ParentAccount=?  order by Priority");
			pstmt.setString(1, pAccNumber);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				AccountLinksData l_AccountLink=new AccountLinksData();
				readRecord(l_AccountLink, rs,"");
				l_AccountLinkList.add(l_AccountLink);
			}
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l_AccountLinkList;
	}
}
