package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.shared.CCTDrawingData;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;

public class CCTDrawingDao {

	public static String getSettleSrNo(Connection pConn) throws SQLException {
		String id="";
		PreparedStatement pstmt = pConn.prepareStatement("select Format(IsNull(Max(Convert(int,SettleSrNumber)),0)+1,'0') as SettleSrNumber From CCTDrawing");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()){
			id=rs.getString("SettleSrNumber");
		}
		pstmt.close();
		return id;
	}
	
	public static String getRecNumberSerialDataList(Connection pConn, String credCategory,int pBYear) throws SQLException {
		String id="";
		PreparedStatement pstmt = null;
		pstmt = pConn.prepareStatement("select IsNull(Max(Convert(int,RecNumberSerial)),0)+1 as RecNumberSerial from CCTDrawing " +
				"Where Prefixed=?");
		//pstmt.setString(1, credCategory);
		pstmt.setInt(1, pBYear);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()){
			id = rs.getString("RecNumberSerial");
		}
		pstmt.close();
		return id;
	}
	
	public static String getTransID(int priority, Connection l_conn) throws SQLException {
		String id="", bnkCode = "", date = "", shortCode = "";
		String no = "";
		int sysKey = 0;
		PreparedStatement pstmt = null;
		
		pstmt = l_conn.prepareStatement("SELECT BankCode + T2 + FORMAT(GETDATE(), 'yyMMdd') AS BANKCODE, GETDATE() todayDate FROM Banks WHERE N1 = 1");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			bnkCode = rs.getString("BANKCODE");
			date = rs.getString("todayDate");
		}
		
		pstmt = l_conn.prepareStatement("SELECT T10 FROM T00077 WHERE N1 = ?");
		pstmt.setInt(1, priority);
		rs = pstmt.executeQuery();
		if (rs.next()) shortCode = rs.getString("T10");		
		
		pstmt = l_conn.prepareStatement("SELECT * FROM T00083 WHERE CONVERT(datetime, t1) < ?");
		pstmt.setString(1, StrUtil.getCurrentDateyyyyMMdd());
		rs = pstmt.executeQuery();
		if (rs.next()) {
			pstmt = l_conn.prepareStatement("TRUNCATE TABLE T00083");
			pstmt.executeUpdate();
		} 	
		
		pstmt = l_conn.prepareStatement("INSERT INTO T00083 (T1, T2) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, date);
		pstmt.setString(2, shortCode);
		pstmt.executeUpdate();
		rs = pstmt.getGeneratedKeys();
		if(rs != null && rs.next()) sysKey = rs.getInt(1) ;
		no = SharedUtil.leadZeros(sysKey, 9);
		
		id = bnkCode + shortCode + no;
		
		pstmt = l_conn.prepareStatement("UPDATE T00083 SET T3 = ? WHERE sysKey = ?");
		pstmt.setString(1, id);
		pstmt.setInt(2, sysKey);
		pstmt.executeUpdate();
		
		pstmt.close();
		return id;
	}
	
	//save
		public static boolean addCCTDrawingData(CCTDrawingData lData, Connection l_conn)
				throws ParserConfigurationException, SAXException, IOException,
				ClassNotFoundException, SQLException{
			boolean result = false;
			try {
				PreparedStatement pstmt = l_conn.prepareStatement("INSERT INTO CCTDrawing"
						+"(EffectiveDate,EffectiveTime,SettleSrNumber,RecNumberSerial,RecNumber,DebtorBrCode,CreditorBrCode," +
						" CurrencyCode,CurrencyRate,Amount,InclusiveAmount,AmtInWord,TotalSettleAmount,TransferAmount,Commission," +
						" Fax,Sundry2,Comm4,Comm5,Comm6,TotalAmount,EventCode,Note1,Note2,CrAccount,DebAgentBrNo,DebAgentBrName,DebCategory,DebId," +
						" DebName,DebAdd,DebPhone,DebOtherInfo,CredAgentBrNo,CredAgentBrName,CredCategory,CredId,CredName,CredAdd," +
						" CredPhone,CredOtherInfo,Status,ToAccNumber,IncomeAccount,Sundry1Account,Sundry2Account,Comm4Account,Comm5Account,Comm6Account,CommCash,ChequeNo," +
						" Prefixed,RefNo, QueuingCategoryCode, ChargeBearerCode,Priority,N4,T2,SETTLEMENTMETHOD, askACK, TransID, NRCDROTH, NRCCROTH,N6,N7) "
						+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");	
				int i = 1;
				
				pstmt.setString(i++, lData.getEffectiveDate());
				pstmt.setString(i++, lData.getEffectiveTime());
				pstmt.setString(i++, lData.getSettleSrNumber());
				pstmt.setString(i++, lData.getRecNumberSerail());
				pstmt.setString(i++, lData.getRecNumber());
				pstmt.setString(i++, lData.getDebtorBrCode());
				pstmt.setString(i++, lData.getCreditorBrCode());
				pstmt.setString(i++, lData.getCurrencyCode());
				pstmt.setDouble(i++, lData.getCurrencyRate());
				pstmt.setDouble(i++, lData.getAmount());
				pstmt.setDouble(i++, lData.getInclusiveAmount());
				pstmt.setString(i++, lData.getAmtInWord());
				pstmt.setDouble(i++, lData.getTotalSettleAmount());
				pstmt.setDouble(i++, lData.getTransferAmount());
				pstmt.setDouble(i++, lData.getCommission());
				pstmt.setDouble(i++, lData.getFax());
				pstmt.setDouble(i++, lData.getSundry2());
				pstmt.setDouble(i++, lData.getComm4());
				pstmt.setDouble(i++, lData.getComm5());
				pstmt.setDouble(i++, lData.getComm6());
				pstmt.setDouble(i++, lData.getTotalAmount());
				pstmt.setString(i++, lData.getEventCode());
				pstmt.setString(i++, lData.getNote1());
				pstmt.setString(i++, lData.getNote2());
				pstmt.setString(i++, lData.getCrAccount());
				pstmt.setString(i++, lData.getDebAgentBrNo());
				pstmt.setString(i++, lData.getDebAgentBrName());
				pstmt.setString(i++, lData.getDebCategory());
				pstmt.setString(i++, lData.getDebId());
				pstmt.setString(i++, lData.getDbName());
				pstmt.setString(i++, lData.getDebtorAdd());
				pstmt.setString(i++, lData.getDebtorPhone());
				pstmt.setString(i++, lData.getDebOtherInfo());
				pstmt.setString(i++, lData.getCredAgentBrNo());
				pstmt.setString(i++, lData.getCredAgentBrName());
				pstmt.setString(i++, lData.getCredCategory());
				pstmt.setString(i++, lData.getCredId());
				pstmt.setString(i++, lData.getCredName());
				pstmt.setString(i++, lData.getCreditorAdd());
				pstmt.setString(i++, lData.getCreditorPhone());
				pstmt.setString(i++, lData.getCredOtherInfo());
				pstmt.setInt(i++, lData.getStatus());
				pstmt.setString(i++, lData.getToAccNumber());
				pstmt.setString(i++, lData.getIncomeAccount());
				pstmt.setString(i++, lData.getSundry1Account());
				pstmt.setString(i++, lData.getSundry2Account());
				pstmt.setString(i++, lData.getComm4Account());
				pstmt.setString(i++, lData.getComm5Account());
				pstmt.setString(i++, lData.getComm6Account());
				pstmt.setInt(i++, lData.getCommCash());
				pstmt.setString(i++, lData.getChequeNo());
				pstmt.setInt(i++, lData.getPreFixed());
				pstmt.setString(i++, lData.getRefNo());
				pstmt.setString(i++, lData.getQCategoryCode());
				pstmt.setString(i++, lData.getChargebearerCode());
				pstmt.setInt(i++, lData.getPriority());	
				if(lData.getPriority()!= 0){
					pstmt.setInt(i++, 1);
				}else{
					pstmt.setInt(i++, lData.getN4());
				}	
				pstmt.setString(i++, lData.getT2());
				pstmt.setString(i++, lData.getSettlementMethod().toString());
				pstmt.setInt(i++, lData.getAskACK());
				pstmt.setString(i++, lData.getTransID());
				pstmt.setInt(i++, lData.getNrcDROTH());
				pstmt.setInt(i++, lData.getNrcCROTH());
				pstmt.setInt(i++, lData.getN6());
				pstmt.setInt(i++, lData.getN7());
				if(pstmt.executeUpdate() > 0)
					result = true;
				pstmt.close();                            
			} catch (SQLException e) {					
				e.printStackTrace();
				result=false;
			}
			return result;
		
		}
}
