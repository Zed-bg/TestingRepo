package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.shared.ChequeEnquiryRes;
import com.nirvasoft.rp.shared.ResponseData;
import com.nirvasoft.rp.shared.acctransferreq;
import com.nirvasoft.rp.shared.chequebookreq;
import com.nirvasoft.rp.shared.chequebookres;
import com.nirvasoft.rp.shared.stopchequeres;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;

public class service003Dao {

	public double getTransAmountPerDayAccTransfer(String fromAccount, String userid, String transdate, Connection conn)
			throws SQLException {
		double ret = 0.00;
		String sql = "select sum(Amount) as amount from dispaymenttransaction where fromAccount = ? and t24 = ? and transdate = ? and T20= ? and N20 = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		int i = 1;
		ps.setString(i++, fromAccount);
		ps.setString(i++, userid);
		ps.setString(i++, transdate);
		ps.setString(i++, "");
		ps.setInt(i++, 0);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ret = rs.getDouble(1);
		}
		ps.close();
		rs.close();
		return ret;
	}
	
	public boolean checkUserAndAccount(String aUserID, String aFromAcc, Connection conn) throws SQLException {
		boolean ret = false;
		String sql = "select AccNumber from UAJunction where UserID = ? and AccNumber=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, aUserID);
		ps.setString(2, aFromAcc);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ret = true;
		}
		ps.close();
		rs.close();
		return ret;
	}
	
	//------------------- dochequebookrequest --------------------------------
	
	public chequebookres dochequebookrequest(chequebookreq req,String branchcode,String currcode, Connection conn) throws SQLException{
        chequebookres res = new chequebookres();
        String sql ="",l_Branchcode="",l_CurrencyCode = "";
        PreparedStatement ps = null;
        int c_length = 0;
        int no_check = 0;
        boolean flag = false;
        String l_TodayDate = GeneralUtility.getTodayDate();
        sql = "select N1,N2 from SystemSetting where t1='CHQ'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            c_length = rs.getInt("N1");
            no_check = rs.getInt("N2");
            if(no_check == 0)
                no_check = 9;
        }
        if(SharedLogic.getSystemData().getpSystemSettingDataList().get("CHQBASE").getN1() == 1){
            l_Branchcode = branchcode;
        }
        if(SharedLogic.getSystemData().getpSystemSettingDataList().get("CHQBASE").getN2()== 1){
            l_CurrencyCode = currcode;
        }
        sql = "Insert Into CheckIssue "
                + " SELECT ?,ISNULL(MAX(CAST(ChkNoFrom AS INT)),1)+"+c_length+" As ChqNoFrom,"
                + " ?,?,0,(REPLICATE('0',"+c_length+"-LEN(0))+'0'),'',?,'',?,'',"
                + " 0,0,0 FROM Checkissue WHERE ChkNoChar = '' ";
        if(!l_Branchcode.equals("")){
            sql += "AND T2 = '"+l_Branchcode+"' ";
        }
        if(!l_CurrencyCode.equals("")){
            sql += "AND CurrencyCode = '"+l_CurrencyCode+"' ";
        }
        ps = conn.prepareStatement(sql);
        int i = 1;
        ps.setString( i++ , req.getAccnumber());
        ps.setString( i++ , l_TodayDate);
        ps.setString( i++ , l_TodayDate);
        ps.setString( i++ , currcode);
        ps.setString( i++ , branchcode);
        if(!l_Branchcode.equals("")){
            ps.setString( i++ , l_Branchcode);
        }       
        if(!l_CurrencyCode.equals("")){
            ps.setString( i++ , l_CurrencyCode);
        }               
        if (ps.executeUpdate() > 0 ){
            flag = true;
        }
        if(flag){
            sql = "SELECT REPLICATE('0',"+ no_check +"-LEN(MAX(ChkNoFrom))) + cast(MAX(ChkNoFrom) as varchar) as cnostart, "
                    + " REPLICATE('0',"+ no_check +"-(LEN(MAX(ChkNoFrom)+? - 1) )) + cast((MAX(ChkNoFrom)+? - 1) as varchar) as cnoend "
                    + " from Checkissue WHERE AccNumber= ?";
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setInt( i++ , c_length);
            ps.setInt( i++ , c_length);
            ps.setString( i++ , req.getAccnumber());
            //ps.setString( i++ , "");
            rs = ps.executeQuery();
            if (rs.next()) {
                res.setRetcode("300");
                res.setRetmessage("Cheque Book Request Successfully");
                res.setStartcheque(rs.getString("cnostart"));
                res.setEndcheque(rs.getString("cnoend"));
            }
        }else{
            res.setRetcode("210");
            res.setRetmessage("Cheque Book Request Failed");
        }
        return res;
    }

	
	public String[] checkaccountno(String accno, Connection conn) throws SQLException {
		String branchcode = "",currcode;
		String[] arr = {"",""};
		String sql = "select BranchCode,CurrencyCode From accounts where AccNumber=? and Status not in (3,4)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, accno);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			branchcode = rs.getString("BranchCode");
			currcode = rs.getString("CurrencyCode");
			arr[0]=branchcode;
			arr[1]=currcode;
		}
		ps.close();
		rs.close();
		return arr;
	}
	
	public stopchequeres stopCheque(String accountNo, String chequeNo,String narration, Connection conn) throws SQLException {
		stopchequeres ret = new stopchequeres();
        String chqChar = "";
        String chqNum = "";
        String stopStatus = "2";
        int status = 0;
        String desc = "";
        long chqNumLong = 0;
        if (!chequeNo.matches("\\d")) {
            for (int i = 0; i < chequeNo.length(); i++) {
                String tmpChar = "";
                tmpChar = String.valueOf(chequeNo.charAt(i));
                if (tmpChar.matches("\\d")) {
                    chqNum += tmpChar;
                } else {
                    chqChar += tmpChar;
                }
            }
        }
        if (!chqNum.equalsIgnoreCase("")) {
            chqNumLong = Long.parseLong(chqNum);
            chqNum = String.valueOf(chqNumLong);
        }
        String sql1 = "select right(substring(sheets,1,(?-chknofrom+1)),1) from ( "
                + " select AccNumber, ChknoFrom,ChkNoFrom+(len(sheets)-1) as endchq, (len(sheets)) as lengthChq,sheets, "
                + " chknochar,dateissued,dateapproved,status  from checkissue where accnumber=?) A where  "
                + " A.chknofrom <= ? and A.endchq >=? and chknochar=?";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        int i = 1;
        ps1.setString(i++, chqNum);
        ps1.setString(i++, accountNo);
        ps1.setString(i++, chqNum);
        ps1.setString(i++, chqNum);
        ps1.setString(i++, chqChar);
        ResultSet rs1 = ps1.executeQuery();
        if (rs1.next()) {
            status = rs1.getInt(1);
            if (status == 0) {
                String sql2 = "update Checkissue Set sheets=substring(A.sheets,1,(?-A.chknofrom))+?+"
                        + "substring(A.sheets,(?-A.chknofrom)+2,A.lengthchq) from (select AccNumber, ChknoFrom,ChkNoFrom+(len(sheets)-1) "
                        + "as endchq, (len(sheets)) as lengthChq,sheets,chknochar,dateissued,dateapproved,status  "
                        + "from checkissue where accnumber=?) A inner join Checkissue C on A.Accnumber=C.AccNumber "
                        + "and A.Chknofrom=C.Chknofrom  where A.chknofrom <= ? and A.endchq >=? and A.chknochar=? ";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                int j = 1;
                ps2.setString(j++, chqNum);
                ps2.setString(j++, stopStatus);
                ps2.setString(j++, chqNum);
                ps2.setString(j++, accountNo);
                ps2.setString(j++, chqNum);
                ps2.setString(j++, chqNum);
                ps2.setString(j++, chqChar);
                if (ps2.executeUpdate() > 0) {
                    ret.setRetcode("300");
                    ret.setRetmessage("Updated successfully.");
                }
                ps2.close();
                
                //cheque verify
                String sql3 = "update chequeverify Set N1=2,T6=?,T7=? where AccNumber=? and ChequeNo=?";
                PreparedStatement ps3 = conn.prepareStatement(sql3);
                int k = 1;
                ps3.setString(k++, narration);
                ps3.setString(k++, GeneralUtility.getTodayDate());
                ps3.setString(k++, accountNo);
                ps3.setString(k++, chequeNo);
                if (ps3.executeUpdate() > 0) {
                    ret.setRetcode("300");
                    ret.setRetmessage("Updated successfully.");
                }
                ps3.close();
                
                //DELETE AccBar
                sql3 = "DELETE FROM AccBar WHERE AccNumber=? and BarType=? and RefNo=?";
                ps3 = conn.prepareStatement(sql3);
                k = 1;
                ps3.setString(k++, accountNo);
                ps3.setString(k++, "6");
                ps3.setString(k++, chequeNo);
                if (ps3.executeUpdate() > 0) {
                    ret.setRetcode("300");
                    ret.setRetmessage("Updated successfully.");
                }
                ps3.close();
                
                sql3 = "select * From chequeverify where AccNumber=? and ChequeNo=?";
                ps3 = conn.prepareStatement(sql3);
                int k3 = 1;
                ps3.setString(k3++, accountNo);
                ps3.setString(k3++, chequeNo);
                ResultSet rs2 = ps3.executeQuery();
                if (rs2.next()) {
                    //ret.setTophone(rs2.getString("T5"));
                }
            } else {
                if (status == 1) {
                    desc = "Paid";
                } else if (status == 2) {
                    desc = "Stopped";
                } else if (status == 3) {
                    desc = "Cancel";
                }
                ret.setRetcode("210");
                ret.setRetmessage("Cheque status is " + desc);
            }
        } else {
            ret.setRetcode("210");
            ret.setRetmessage("Cheque not found");
        }
        ps1.close();
        rs1.close();
        return ret;
    }
	
	public boolean checkAlreadySetChequeAmount(String accountNo, String chequeNo, Connection conn) throws SQLException {
		boolean alreadyExist = true;
		StringBuffer l_query = new StringBuffer("");
		l_query.append("SELECT * FROM ChequeVerify WHERE AccNumber=? AND ChequeNo=? AND N1=?");

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		updateRecord_checkAlreadySetChequeAmount(accountNo, chequeNo, pstmt);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			alreadyExist = true;
		} else {
			alreadyExist = false;
		}

		pstmt.close();
		return alreadyExist;
	}
	
	private void updateRecord_checkAlreadySetChequeAmount(String accountNo, String chequeNo, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		pstmt.setString(i++, accountNo);
		pstmt.setString(i++, chequeNo);
		pstmt.setInt(i++, 0);
	}
	
	public int getDueDate(Connection conn) throws SQLException {
		int noOfDays = 0;
		StringBuffer l_query = new StringBuffer("");
		l_query.append("SELECT n6 FROM SystemSetting WHERE t1='CHQ'");

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			noOfDays = rs.getInt("n6");
		}
		pstmt.close();
		return noOfDays;
	}
	
	public boolean setChequeAmount(String accountNo, String chequeNo, String amount, String entryDate, String dueDate,
			String t1, String t2, String t3, String t4, String t5, String n1, String n2, String n3, String n4,
			String n5, Connection conn) throws SQLException {
		boolean ret = false;
		StringBuffer l_query = new StringBuffer("");
		l_query.append("INSERT INTO ChequeVerify (AccNumber,ChequeNo,BarAmount,EntryDate,DueDate,T1,T2,T3,T4,T5,N1,N2,N3,N4,N5) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		updateRecord(accountNo, chequeNo, amount, entryDate, dueDate, t1, t2, t3, t4, t5, n1, n2, n3, n4, n5, pstmt);

		int rs = pstmt.executeUpdate();
		if (rs > 0) {
			ret = true;
		}
		pstmt.close();
		return ret;
	}
	
	private void updateRecord(String accountNo, String chequeNo, String amount, String entryDate, String dueDate,
			String t1, String t2, String t3, String t4, String t5, String n1, String n2, String n3, String n4,
			String n5, PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, accountNo);
		pstmt.setString(2, chequeNo);
		pstmt.setDouble(3, Double.parseDouble(amount));
		pstmt.setString(4, entryDate);
		pstmt.setString(5, dueDate);
		pstmt.setString(6, t1);
		pstmt.setString(7, t2);
		pstmt.setString(8, t3);
		pstmt.setString(9, t4);
		pstmt.setString(10, t5);
		pstmt.setInt(11, Integer.parseInt(n1));
		pstmt.setInt(12, Integer.parseInt(n2));
		pstmt.setInt(13, Integer.parseInt(n3));
		pstmt.setDouble(14, Double.parseDouble(n4));
		pstmt.setDouble(15, Double.parseDouble(n5));
	}
	
	public boolean setChequeAmount_AccBar(String accountNo, String amount, String entryDate, String remark,
			String barType, String refNo, Connection conn) throws SQLException {
		boolean ret = false;
		StringBuffer l_query = new StringBuffer("");
		l_query.append("INSERT INTO AccBar (AccNumber,BarAmount,Date,Remark,BarType,RefNo) VALUES (?,?,?,?,?,?)");

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		updateRecord_AccBar(accountNo, amount, entryDate, remark, barType, refNo, pstmt);

		int rs = pstmt.executeUpdate();
		if (rs > 0) {
			ret = true;
		}
		pstmt.close();
		return ret;
	}

	private void updateRecord_AccBar(String accountNo, String amount, String entryDate, String remark, String barType,
			String refNo, PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, accountNo);
		pstmt.setDouble(2, Double.parseDouble(amount));
		pstmt.setString(3, entryDate);
		pstmt.setString(4, remark);
		pstmt.setInt(5, Integer.parseInt(barType));
		pstmt.setString(6, refNo);
	}
	
	public static String[] getBranchData(String fromaccnumber,Connection conn) throws SQLException{
		String arr[]= {"0","0","0"};
		String bCode = "", zCode ="", acctype="";
		
		String query = "select BranchCode,b.N1 as zonecode,b.T3 as acctype "
				+ "From Accounts a inner join T00005 b on a.AccNumber=b.T1 where AccNumber=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, fromaccnumber);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			bCode = rs.getString("BranchCode");
			zCode = rs.getString("zonecode");
			acctype = rs.getString("acctype");
		}
		arr[0]= bCode;
		arr[1]= zCode;
		arr[2]= acctype;
		return arr;
	}
	
	public result checkTransref(acctransferreq aReq, Connection conn) throws SQLException{
		result response = new result();
		double amt = Double.parseDouble(aReq.getAmount());
		boolean f_State = false;
		boolean t_State = false;
		boolean a_State = false;
		String query = "select AccNumber,Amount,TransType from accounttransaction where transref=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setString(index++, aReq.getRefno());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			int tranType = rs.getInt("TransType");
			String accno = rs.getString("AccNumber");
			double amount = rs.getDouble("Amount");
			if(tranType > 500 && accno.equals(aReq.getFromaccnumber())){
				f_State = true;
			}
			if( tranType < 500 && accno.equals(aReq.getToaccnumber())){
				t_State = true;
			}
			if(amount == amt){
				a_State = true;
			}
		}
		if(f_State && t_State && a_State){
			response.setState(true);
		}else{
			response.setMsgDesc("Invalid Trans Ref Number");
		}
		return response;
	}
	
	public String getBankSrNo(String bCode, Connection conn) throws SQLException{
		String srno = "";
		int syskey = 0;
		String query = "select syskey from banks where bankcode=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setString(index++, bCode);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			syskey = rs.getInt("syskey");
		}
		
		srno = String.valueOf(syskey);
		return srno;
	}
}
