package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.core.ccbs.data.db.DBAccountMgr;
import com.nirvasoft.rp.core.ccbs.data.db.DBTransactionMgr;
import com.nirvasoft.rp.framework.result;
import com.nirvasoft.rp.mgr.DBInterestProcessingMgr;
import com.nirvasoft.rp.shared.accountactivitydata;
import com.nirvasoft.rp.shared.accountactivityrequest;
import com.nirvasoft.rp.shared.accountactivityresponse;
import com.nirvasoft.rp.shared.accountcheckres;
import com.nirvasoft.rp.shared.accountnumberdataresponse;
import com.nirvasoft.rp.shared.accstatusdata;
import com.nirvasoft.rp.shared.bulkpaymentdatareq;
import com.nirvasoft.rp.shared.bulkpaymentdatares;
import com.nirvasoft.rp.shared.bulkpaymentrequest;
import com.nirvasoft.rp.shared.bulkpaymentresponse;
import com.nirvasoft.rp.shared.custaccinforesult;
import com.nirvasoft.rp.shared.customerinfodata;
import com.nirvasoft.rp.shared.customerinfodataresult;
import com.nirvasoft.rp.shared.fixeddepositinfodata;
import com.nirvasoft.rp.shared.fixeddepositinfores;
import com.nirvasoft.rp.shared.getaccountcustomerinforeq;
import com.nirvasoft.rp.shared.refdata;
import com.nirvasoft.rp.shared.refdataresponse;
import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.AccountInfo;
import com.nirvasoft.rp.shared.AccountInfoData;
import com.nirvasoft.rp.shared.AccountLinksData;
import com.nirvasoft.rp.shared.CheckIssueCriteriaData;
import com.nirvasoft.rp.shared.CheckIssueData;
import com.nirvasoft.rp.shared.CheckSheetData;
import com.nirvasoft.rp.shared.Constant;
import com.nirvasoft.rp.shared.CurrencyRateData;
import com.nirvasoft.rp.shared.CurrencyRateDataSet;
import com.nirvasoft.rp.shared.CustInfoResult;
import com.nirvasoft.rp.shared.CutOffDataResult;
import com.nirvasoft.rp.shared.CutOffTimeData;
import com.nirvasoft.rp.shared.ForeignExchangeRateData;
import com.nirvasoft.rp.shared.ForeignExchangeRateResult;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.ProductData;
import com.nirvasoft.rp.shared.SystemSettingData;
import com.nirvasoft.rp.shared.TempData;
import com.nirvasoft.rp.shared.statementdata;
import com.nirvasoft.rp.shared.statementdatarequest;
import com.nirvasoft.rp.shared.statementdataresponse;
import com.nirvasoft.rp.shared.transactioninforequest;
import com.nirvasoft.rp.shared.transactioninforesult;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.TokenResult;
import com.nirvasoft.rp.shared.TokenSetupData;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;
import com.nirvasoft.rp.mgr.DBSystemMgr;
import com.nirvasoft.rp.shared.FormParameters;

public class service001Dao {

	public TokenResult insertTokenData(TokenSetupData req, Connection conn) throws SQLException{
		TokenResult res = new TokenResult();
		long autoKey = 0;
		autoKey = getAutoKey("TokenSetData",conn);
		
		String query = "INSERT INTO TokenSetData(syskey,userid,password,createddate,modifieddate,status) "
					+ "VALUES(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setLong(index++, autoKey);
		pstmt.setString(index++, req.getUserId());
		pstmt.setString(index++, req.getPassword());
		pstmt.setString(index++, GeneralUtil.getDateYYYYMMDDHHMMSS());
		pstmt.setString(index++, GeneralUtil.getDateYYYYMMDDHHMMSS());
		pstmt.setInt(index++, 1);
		if (pstmt.executeUpdate() > 0) {				
			res.setState(true);
			res.setMsgCode(Constant.successcode);
			res.setMsgDesc("Insert User Successfully.");
		} else {
			res.setState(false);
			res.setMsgCode(Constant.errorcode);
			res.setMsgDesc("Insert User Failed.");
		}
		return res;
		
	}
	
	public long getAutoKey(String table,Connection conn) throws SQLException{
		long autoKey = 0;
		String query = "select COUNT(*) as Total from "+table;
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.executeQuery();
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			autoKey = rs.getLong("Total");
		}
		
		autoKey = autoKey + 1;
		return autoKey ;
	}
	
	public boolean isExisting(String userid, Connection conn) throws SQLException{
		boolean res = false;
		String query = "select syskey from TokenSetData where userid =?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, userid);
		pstmt.executeQuery();
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			res = true;
		}
		
		return res;
	}
	
	public boolean checkCifbyAccount(String cifID,String fromAccount, Connection conn) throws SQLException{
		boolean res = false;
		String query = "select accnumber from cajunction where customerid=? and accnumber=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setString(index++, cifID);
		pstmt.setString(index++, fromAccount);
		pstmt.executeQuery();
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			res = true;
		}
		
		return res;
	}
	
	public boolean checkAccount(String account, Connection conn) throws SQLException{
		boolean res = false;
		String query = "select accnumber From accounts where accnumber=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setString(index++, account);
		pstmt.executeQuery();
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			res = true;
		}
		
		return res;
	}
	
	public boolean checkGLAccount(String account, Connection conn) throws SQLException {
		boolean res = false;
		String query = "select accno  From gl where accno=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setString(index++, account);
		pstmt.executeQuery();
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			res = true;
		}
		return res;
	}
	
	public String checkAccountAndShortCode(String account, Connection conn) throws SQLException{
		String res = "";
		String query = "select accnumber From accounts where accnumber=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		int index = 1;
		pstmt.setString(index++, account);
		pstmt.executeQuery();
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			res = rs.getString("accnumber");
		}
		if(res.equalsIgnoreCase("")){
			query = "select accnumber From accounts where SAccNo=?";
			pstmt = conn.prepareStatement(query);
			index = 1;
			pstmt.setString(index++, account);
			pstmt.executeQuery();
			rs = pstmt.executeQuery();
			if (rs.next()) {
				res = rs.getString("accnumber");
			}
		}
		
		return res;
	}
	
	public CustInfoResult getAccountSummary(String custno, String fixedDeposit, String searchtype, Connection conn) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
		CustInfoResult cusret = new CustInfoResult();
		ArrayList<AccountInfo> ret = new ArrayList<AccountInfo>();
		AccountInfo [] accarr=null;
		boolean isData = false;
		double haveAmountTotal = 0.00; double oweAmountTotal = 0.00;
		String deposit = "";
		String condition="";
		if (fixedDeposit.equalsIgnoreCase("No")) {
			deposit = "and t.t2<>(SELECT t1 FROM T00001 Where N1=6 AND N2=4)";
		}
		
		String tmpTableName = "Tmp" + custno;
		String sql1 = "IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[" + tmpTableName
				+ "]') AND type in (N'U')) " + " DROP TABLE " + tmpTableName;
		PreparedStatement ps = conn.prepareStatement(sql1);
		ps.executeUpdate();
		
		if(searchtype.equalsIgnoreCase("10")){//Individual
			condition = "AND at.AccType=110 ";
		}else if(searchtype.equalsIgnoreCase("20")){
			//140 Co-Operative, 150 Association, 180 Organization
			condition = "AND at.AccType IN (130,140,150,180) ";
		}
		
		String sql2 = "select (case when(isnull(t12.t1,'')<>'') then 'Card Account' else p.ProcessCode5 end) as Description,p.ProductCode as ProductType,"
				+ " a.currentBalance, a.currencyCode, a.AccNumber,t12.t1, t12.t1 as CardAcc,at.Description as acctype,"
				+ " t.T5 as branchcode, CONVERT(nvarchar(50),'') as branchname, a.OpeningDate, a.LastUpdate, a.Status,CONVERT(nvarchar(20),'') as statusname,"
				+ " ca.CustomerId,CONVERT(nvarchar(50),'') as cusname into "+tmpTableName+" from CAJunction ca inner join accounts a "
				+ " on ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1" + deposit 
				+" inner join acctype at on t.t3 = at.AccountCode and ca.AccType = ca.RType"
				+ " inner join ProductType p on t.T2=p.ProductCode left join T00012 t12 on a.AccNumber=t12.t6 "
				+ " where ca.CustomerId =" + custno + " and a.status <> 3 " + condition;
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();
		
		String sql3 = "Update a set a.branchname=b.BranchName From "+tmpTableName+" a inner join BankDatabases b on a.branchcode=b.BranchCode";
		ps = conn.prepareStatement(sql3);
		ps.executeUpdate();
		
		String sql4 = "Update a set a.statusname=(case when a.Status=0 and a.LastUpdate='19000101' then 'New' else case when a.Status=0 and a.LastUpdate<>'19000101' then 'Active' else b.Description end end) "
				+ " From "+tmpTableName+" a inner join RefAccountStatus b on a.Status=b.StatusCode";
		ps = conn.prepareStatement(sql4);
		ps.executeUpdate();
		
		String sql5 = "Update a set a.cusname=(case when b.Title='' then b.name else (b.title+' '+b.Name) end) "
				+ " From "+tmpTableName+" a inner join Customer b on a.customerid=b.customerid ";
		ps = conn.prepareStatement(sql5);
		ps.executeUpdate();
		
		String query = "select * From "+tmpTableName + " order by currentBalance desc";
		ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			AccountInfo aData = new AccountInfo();
			aData.setAccnumber(rs.getString("AccNumber"));//accnumber
			aData.setCurcode(rs.getString("currencyCode"));//curcode
			aData.setAcctype(rs.getString("acctype"));//acctype
			aData.setProductname(rs.getString("Description"));
			aData.setProducttype(rs.getString("ProductType"));//producttype
			if(isLoanAcc(aData.getAccnumber(), conn)){
				aData.setAvailablebalance(rs.getDouble("currentBalance"));
			}else{
				double l_Available = DBAccountMgr.getAvailableBalance(aData.getAccnumber(), conn);
				aData.setAvailablebalance(l_Available);
			}
			aData.setCurrentbalance(rs.getDouble("currentBalance"));
			if(rs.getDouble("currentBalance") > 0)
				haveAmountTotal += rs.getDouble("currentBalance");
			else
				oweAmountTotal += rs.getDouble("currentBalance");
			if (rs.getString("CardAcc") == null) {
				aData.setCardacc("");
			} else {
				aData.setCardacc(rs.getString("CardAcc"));
			}
			aData.setAcctype(rs.getString("acctype"));
			aData.setAccname(rs.getString("cusname"));
			aData.setBrname(rs.getString("branchname"));
			aData.setBrcode(rs.getString("branchcode"));
			aData.setAccstatus(rs.getString("statusname"));//accstatus
			aData.setOpeningdate(rs.getString("OpeningDate"));//openingdate
			ret.add(aData);
			isData = true;
		}
		if(ret.size()>0){
			accarr = new AccountInfo [ret.size()];
			for(int i=0; i< ret.size(); i++){
				accarr[i] = ret.get(i);
			}
		}
		if (isData == true) {
			cusret.setHaveAmountTotal(haveAmountTotal);
			cusret.setOweAmountTotal(oweAmountTotal);
			cusret.setCode("300");
			cusret.setDesc("Successfully");

			cusret.setAccountInfoArr(accarr);

		} else {
			cusret.setCode("210");
			cusret.setDesc("There is no data.");
			cusret.setAccountInfoArr(accarr);

		}
		//Drop table
		ps = conn.prepareStatement(sql1);
		ps.executeUpdate();
		
		ps.close();
		rs.close();
		return cusret;
	}	
	
	public accountcheckres getaccinfo(String acctno, String producttype, Connection conn) throws SQLException {
		accountcheckres cusret = new accountcheckres();
		boolean isData = false;
		String sql = "select at.Description,t.T5 as branchcode,a.OpeningDate, a.currentBalance, a.currencyCode, a.AccNumber, p.ProcessCode5, p.ProcessCode3, at.Description as acctype, "
				+ " (case when cc.Title='' then cc.name else (cc.title+' '+cc.Name) end) as cusname ,bd.BranchName, "
				+ " (case when a.Status=0 and a.LastUpdate='19000101' then 'New' else case when a.Status=0 and a.LastUpdate<>'19000101' then 'Active' else RS.Description end end) as Status "
				+ " from CAJunction ca inner join accounts a on ca.AccNumber = a.AccNumber  inner join T00005 t on a.AccNumber = t.T1 "
				+ " inner join acctype at on t.t3 = at.AccountCode and ca.AccType = ca.RType inner join ProductType p on t.T2=p.ProductCode "
				+ " inner join Customer cc on cc.CustomerId = ca.CustomerId inner join BankDatabases bd on t.t5=bd.BranchCode "
				+ " inner join (select StatusCode,description From RefAccountStatus where description<>'New') RS on A.Status=RS.statuscode "
				+ " where a.AccNumber = ? and a.status <> 3";
		if (!producttype.equals("")) {
			sql += " and p.ProductType <> ?";
		}
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, acctno);
		if (!producttype.equals("")) {
			ps.setString(2, producttype);
		}
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			cusret.setAccname(rs.getString("cusname"));
			cusret.setAccnumber(rs.getString("AccNumber"));
			cusret.setBrname(rs.getString("BranchName"));
			cusret.setCurcode(rs.getString("currencyCode"));
			cusret.setCurrentbalance(GeneralUtil.formatDecimalString(rs.getDouble("currentBalance")));
			cusret.setCurrentbalancedec(rs.getDouble("currentBalance"));
			cusret.setStatus(rs.getString("Status"));
			cusret.setProductname(rs.getString("ProcessCode5"));
			cusret.setProducttype(rs.getString("ProcessCode3"));
			cusret.setAcctype(rs.getString("acctype"));
			cusret.setBrcode(rs.getString("branchcode"));
			cusret.setOpeningdate(rs.getString("OpeningDate")); 
			if(cusret.getCurrentbalancedec() > 0){
				isData = true;
			}else{
				isData = false;
			}
		}
		if (isData == true) {
			cusret.setRetcode("300");
			cusret.setRetmessage("Successfully");

		} else {
			cusret.setRetcode("210");
			cusret.setRetmessage("There is no data.");
		}
		ps.close();
		rs.close();
		return cusret;
	}
	
	public accountactivityresponse getAccountActivity(accountactivityrequest req, Connection conn) throws SQLException {
		int pagesize = req.getPagesize();
		int currentpage = req.getCurrentpage();
		int startPage = (currentpage - 1) * pagesize;
		int endPage = pagesize + startPage;
		accountactivityresponse ret = new accountactivityresponse();
		ArrayList<accountactivitydata> dataList = new ArrayList<accountactivitydata>();
		String tmpTableName = "Tmp" + req.getAccnumber();
		String sql1 = "IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[" + tmpTableName
				+ "]') AND type in (N'U')) " + "	DROP TABLE " + tmpTableName;
		PreparedStatement ps = conn.prepareStatement(sql1);
		ps.executeUpdate();
		String whereclause = "";
		String condition = "";
		if (!req.getAccnumber().equals("")) {
			whereclause += " and a.AccNumber = '" + req.getAccnumber() + "' and a.Status <254 "; // NRC
		}
		if (req.getDurationtype().equals("0")) {
			startPage = 0;
			endPage = 10;
			condition = " TOP(10) ";
			whereclause += " ORDER BY a.TransNo ";
		}
		if (req.getDurationtype().equals("1")) {
			String nowdate = GeneralUtil.oracledatetoString();
			String lasttwodate = GeneralUtil.oraclelastday(2); // TRUNC(to_date('2017-06-12','yyyy-mm-dd'))
			whereclause += " and a.TransDate >= '" + lasttwodate + "' and a.TransDate <= '" + nowdate + "'";
		}
		if (req.getDurationtype().equals("2")) {
			String nowdate = GeneralUtil.oracledatetoString();
			String lastfivedate = GeneralUtil.oraclelastday(5); // TRUNC(to_date('2017-06-12','yyyy-mm-dd'))
			whereclause += " and a.TransDate >= '" + lastfivedate + "' and a.TransDate <= '" + nowdate + "'";
		}
		if (req.getDurationtype().equals("3")) {
			String fdate = req.getFromdate().substring(0, 4) + "-" + req.getFromdate().substring(4, 6) + "-"
						+ req.getFromdate().substring(6, 8);
			String tdate = req.getTodate().substring(0, 4) + "-" + req.getTodate().substring(4, 6) + "-" 
						+ req.getTodate().substring(6, 8);
				whereclause += " and a.TransDate >= '" + fdate + "' and a.TransDate <= '" + tdate + "'";
		}
		String sql2 = "select "+condition+" b.TrCurCode,a.accnumber,b.TrAmount,a.TransDate,a.EffectiveDate,a.Description,a.TransRef,"
				+ "a.TransNo,(case when a.TransType>=500 then '1' else '2' end) as DrCr,a.remark,a.accref as transdesc into " + tmpTableName
				+ " from accounttransactionold a " + " inner join accountgltransaction b on a.transno=b.transno "
				+ whereclause;
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();
		String sql3 = "Insert Into " + tmpTableName
				+ "(TrCurCode,Accnumber,TrAmount,TransDate,EffectiveDate,Description,TransRef,TransNo,DrCr,remark,transdesc) "
				+ "select b.TrCurCode,a.accnumber,b.TrAmount,a.TransDate,a.EffectiveDate,a.Description,a.TransRef,"
				+ "a.TransNo,(case when a.TransType>=500 then '1' else '2' end) as DrCr,a.remark,a.accref as transdesc "
				+ "from accounttransaction a inner join accountgltransaction b on a.transno=b.transno " + whereclause;
		ps = conn.prepareStatement(sql3);
		ps.executeUpdate();
		String sql4 = "Select * from (select Row_Number() Over (Order By effectivedate desc, transno desc) as rownumber,* "
				+ "from " + tmpTableName + ") c where 1=1 and c.rownumber > " + startPage + " And c.rownumber <= "
				+ endPage + " " + "order By c.effectivedate desc, c.transno desc";
		ps = conn.prepareStatement(sql4);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			accountactivitydata data = new accountactivitydata();
			data = readAccountActivityData(rs);
			dataList.add(data);
		}
		accountactivitydata[] dataArr = new accountactivitydata[dataList.size()];
		if(dataList.size()>0){
			for(int i=0; i<dataList.size(); i++){
				dataArr[i]=dataList.get(i);
			}
		}
		
		String sql5 = "Select COUNT(*) As total from " + tmpTableName;
		ps = conn.prepareStatement(sql5);
		PreparedStatement pstm = conn.prepareStatement(sql5);
		ResultSet result = pstm.executeQuery();
		result.next();
		int totalcount = result.getInt("total");
		// if duration type is default,for only 10 record requirement
		if (req.getDurationtype().equals("0")) {
			if (totalcount > 10) {
				ret.setTotalcount(10);
			} else {
				ret.setTotalcount(result.getInt("total"));
			}
		} else {
			ret.setTotalcount(result.getInt("total"));
		}
		if (ret.getTotalcount() % req.getPagesize() != 0) {
			ret.setPagecount((ret.getTotalcount() / req.getPagesize()) + 1);
		} else {
			ret.setPagecount((ret.getTotalcount() / req.getPagesize()));
		}
		// end
		if (dataList.size() > 0) {
			ret.setDatalist(dataArr);
			ret.setRetcode("300");
			ret.setRetmessage("Successfully");
		} else {
			ret.setRetcode("210");
			ret.setRetmessage("There is no data");
		}
		ps = conn.prepareStatement(sql1);
		ps.executeUpdate();
		
		ps.close();
		rs.close();
		return ret;
	}
	
	accountactivitydata readAccountActivityData(ResultSet rs) throws SQLException {
		accountactivitydata ret = new accountactivitydata();
		ret.setCurcode(rs.getString("TrCurCode"));
		ret.setTransamount(GeneralUtil.formatDecimal(rs.getDouble("TrAmount")));
		ret.setTransdate(rs.getString("TransDate"));
		ret.setEffectivedate(rs.getString("EffectiveDate"));
		ret.setTransdescription(rs.getString("Description"));
		if (!rs.getString("remark").equalsIgnoreCase("")) {
			ret.setTransdescription(rs.getString("Description"));
		}
		if (rs.getString("TransRef") == null || rs.getString("TransRef") == "") {
			ret.setTransref("");
		} else {
			ret.setTransref(rs.getString("TransRef"));
		}
		ret.setTranscode(rs.getString("DrCr"));
		ret.setTranstypedescription(rs.getString("transdesc"));
		ret.setRemark(rs.getString("remark"));
		return ret;
	}
	
	public statementdataresponse getstatement(statementdatarequest req, Connection conn) throws SQLException{
		statementdataresponse res = new statementdataresponse();
		ArrayList<statementdata> dataList = new ArrayList<statementdata>();
		int pagesize = req.getPagesize();
		int currentpage = req.getCurrentpage();
		int startPage = (currentpage - 1) * pagesize;
		int endPage = pagesize + startPage;
		int totalcount = 0;
		String fDate = "";
		String tDate = "";
		
		if (req.getDurationtype() == 1) {
			fDate = GeneralUtil.oraclelastday(2); // TRUNC(to_date('2017-06-12','yyyy-mm-dd'))
			tDate= GeneralUtil.oracledatetoString();
		}
		if (req.getDurationtype() == 2) {
			 fDate = GeneralUtil.oraclelastday(5); // TRUNC(to_date('2017-06-12','yyyy-mm-dd'))
			 tDate = GeneralUtil.oracledatetoString();
		}
		if (req.getDurationtype() == 3) {
			fDate = req.getFromdate();
			tDate = req.getTodate();
		}else{//today date
			fDate = GeneralUtil.oracledatetoString();
			tDate= GeneralUtil.oracledatetoString();
		}
		String sDate =  GeneralUtil.minusDayFromDate(fDate);
		CallableStatement l_Call=null;
		l_Call = conn.prepareCall("{call [dbo].[SP_AccountStatement](?,?,?,?)}");
		int index = 1;
		l_Call.setString(index++,sDate.replace("-", ""));//06.07.2018  //06/Jul/2018
		l_Call.setString(index++, fDate);
		l_Call.setString(index++, tDate);
		l_Call.setString(index++, req.getAccnumber());
			
		ResultSet  rs=l_Call.executeQuery();
		while (rs.next()) {
			statementdata data = new statementdata();
			
			data.setTransdate(rs.getString("asdate"));	//date 
			data.setTransref(rs.getString("transref")); 	//refno (transref)
			data.setTransdescription(rs.getString("description"));	//description
			data.setTranscode(rs.getString("drcr"));	//txncode
			data.setTransamount(GeneralUtil.formatDecimal(rs.getDouble("tramount"))); 	//tramount
			data.setBalance(GeneralUtil.formatDecimal(rs.getDouble("balance")));	//balance
			data.setCurcode(rs.getString("currency"));	//ccy
			data.setRemark(rs.getString("remark"));	//remark
			data.setTranstypedescription(rs.getString("transdesc"));
			dataList.add(data);
		}
		statementdata[] dataArr = null;
		if(dataList.size()>0){
			if(dataList.size() >= startPage){
				if(dataList.size() < endPage)
					endPage = dataList.size();
				
				int size =endPage - startPage;
				dataArr = new statementdata[size];
				
				int x = 0;
				for(int i=startPage; i<endPage; i++){
					dataArr[x++]=dataList.get(i);
				}
			}
		}
		
		totalcount = dataList.size();
		if (req.getDurationtype() == 0) {
			if (totalcount > 10) {
				res.setTotalcount(10);
			} else {
				res.setTotalcount(totalcount);
			}
		} else {
			res.setTotalcount(totalcount);
		}
		if (res.getTotalcount() % req.getPagesize() != 0) {
			res.setPagecount((res.getTotalcount() / req.getPagesize()) + 1);
		} else {
			res.setPagecount((res.getTotalcount() / req.getPagesize()));
		}
		if (dataList.size() > 0) {
			res.setCurrentpage(req.getCurrentpage());
			res.setPagesize(req.getPagesize());
			res.setDurationtype(req.getDurationtype());
			res.setFromdate(req.getFromdate());
			res.setTodate(req.getTodate());
			res.setDatalist(dataArr);
			res.setRetcode("300");
			res.setRetmessage("Successfully");
		} else {
			res.setRetcode("210");
			res.setRetmessage("There is no data");
		}
		return res;
	}

	public ForeignExchangeRateResult getexchangerate(Connection conn) throws SQLException {
		ArrayList<ForeignExchangeRateData> ret = new ArrayList<ForeignExchangeRateData>();
		ForeignExchangeRateResult exchangeres = new ForeignExchangeRateResult();
		boolean isData = false;
		String sql = "";
			/*sql = "select A.CurrencyCode,'MMK' as currencyCode2, case when CurrencyCode = 'USD' then 'US DOLLAR' "
					+ "else case when CurrencyCode = 'EUR' then 'EURO'  else case when CurrencyCode = 'THB' then 'THAI BAT' "
					+ "else case when CurrencyCode = 'SGD' then 'SINGAPORE DOLLAR' else 'KYAT' end end end end as CurrencyName, "
					+ "'KYAT' as CurrencyName2, "
					+ " ISNULL((SELECT top 1 Rate FROM CurrencyRate WHERE CurrencyCode=A.CurrencyCode AND Type=1 ),0) AS BuyRate, "
					+ " ISNULL((SELECT top 1 Rate FROM CurrencyRate WHERE CurrencyCode=A.CurrencyCode AND Type=2 ),0) AS SellRate, "
					+ " ISNULL((SELECT top 1 Rate FROM CurrencyRate WHERE CurrencyCode=A.CurrencyCode AND Type=3 ),0) AS MidRate "
					+ " from CurrencyRate A where CurrencyCode <> 'MMK' Group By CurrencyCode";*/
		sql="select A.CurrencyCode,'MMK' as currencyCode2, B.Name as CurrencyName, 'KYAT' as CurrencyName2, "
			+ " ISNULL((SELECT top 1 Rate FROM CurrencyRate WHERE CurrencyCode=A.CurrencyCode AND Type=1 ),0) AS BuyRate,"
			+ " ISNULL((SELECT top 1 Rate FROM CurrencyRate WHERE CurrencyCode=A.CurrencyCode AND Type=2 ),0) AS SellRate,"
			+ " ISNULL((SELECT top 1 Rate FROM CurrencyRate WHERE CurrencyCode=A.CurrencyCode AND Type=3 ),0) AS MidRate "
			+ " from CurrencyRate A inner join CurrencyTable B on a.CurrencyCode=b.CurCode where CurrencyCode <> 'MMK' Group By CurrencyCode,B.Name,B.N1 Order by B.N1";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ForeignExchangeRateData aData = new ForeignExchangeRateData();
			aData.setFromcurcode(rs.getString("currencyCode"));
			aData.setTocurcode(rs.getString("currencyCode2"));
			aData.setFromcurcodename(rs.getString("CurrencyName"));
			aData.setTocurcodename(rs.getString("CurrencyName2"));
			aData.setBuyrate(rs.getDouble("BuyRate"));
			aData.setSellrate(rs.getDouble("SellRate"));
			aData.setMidrate(rs.getDouble("MidRate"));
			
			ret.add(aData);
			isData = true;
		}
		ForeignExchangeRateData[] dataArr = new ForeignExchangeRateData[ret.size()];
		if(ret.size()>0){
			for(int i=0; i<ret.size(); i++){
				dataArr[i]=ret.get(i);
			}
		}
		if (isData == true) {
			exchangeres.setRetcode("300");
			exchangeres.setRetmessage("Successfully");
			exchangeres.setDatalist(dataArr);

		} else {
			exchangeres.setRetcode("210");
			exchangeres.setRetmessage("There is no data");
			exchangeres.setDatalist(dataArr);

		}
		ps.close();
		rs.close();
		return exchangeres;
	}
	
	public String getchequeinquiry(String accountNo, String chequeNo, Connection conn) throws SQLException {
		String ret = "";
		String chqChar = "";
		String chqNum = "";
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
		String sql = "select right(substring(sheets,1,(?-chknofrom+1)),1) from ( "
				+ " select AccNumber, ChknoFrom,ChkNoFrom+(len(sheets)-1) as endchq, (len(sheets)) as lengthChq,sheets, "
				+ " chknochar,dateissued,dateapproved,status  from checkissue where accnumber=?) A where  "
				+ " A.chknofrom <= ? and A.endchq >=?  and chknochar=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		int i = 1;
		ps.setString(i++, chqNum);
		ps.setString(i++, accountNo);
		ps.setString(i++, chqNum);
		ps.setString(i++, chqNum);
		ps.setString(i++, chqChar);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			ret = String.valueOf(rs.getInt(1));
		}
		ps.close();
		rs.close();
		if (ret.equalsIgnoreCase("0")) {
			ret = "Unpaid";
		} else if (ret.equalsIgnoreCase("1")) {
			ret = "Paid";
		} else if (ret.equalsIgnoreCase("2")) {
			ret = "Stopped";
		} else if (ret.equalsIgnoreCase("3")) {
			ret = "Cancel";
		}

		return ret;
	}
	
	public bulkpaymentresponse checkpaymentdata(bulkpaymentrequest reqData, Connection conn) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException{
		bulkpaymentresponse response = new bulkpaymentresponse();
		if(reqData.getPaymenttype().equals("10")){// From Account Dr [one]  >> check right account and available balance
			if(DBTransactionMgr.canWithdrawWithMultiCurrency(reqData.getFromaccnumber(), reqData.getTotalamount(), conn)){
				response.setRetcode("300");
				response.setRetmessage("Checking Successfully");
			}else{
				response.setRetcode("240");
				response.setRetmessage("Insufficient Balance!");
			}
		}else{
			response.setRetcode("300");
			response.setRetmessage("Checking Successfully");
		}
		bulkpaymentdatares[] datalist = getAccStatus(reqData.getDatalist(),reqData.getPaymenttype(), conn);
		response.setDatalist(datalist);
		return response;
	}
	
	public bulkpaymentdatares[] getAccStatus(bulkpaymentdatareq[] req,String pType ,Connection conn) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException{
		bulkpaymentdatares[] datalist = null;
		ArrayList<accstatusdata> A_data = new ArrayList<accstatusdata>();
		String accno = "";
		String name = "";
		String query = "";
		for(int i=0; i<req.length ; i++){
			if(i == 0){
				accno += "'"+req[i].getToaccnumber()+"'";
				name +="'"+req[i].getToaccname()+"'";
			}else{
				accno += ","+"'"+req[i].getToaccnumber()+"'";
				name += ","+"'"+req[i].getToaccname()+"'";
			}
		}
		query = "select a.AccNumber,RS.Description from CAJunction ca "
				+ " inner join accounts a on ca.AccNumber = a.AccNumber inner join Customer cc on cc.CustomerId = ca.CustomerId "
				+ " inner join (select StatusCode,description From RefAccountStatus where description<>'New') RS on A.Status=RS.statuscode "
				+ " where a.AccNumber in ("+accno+") and ((cc.title+' '+cc.Name) in ("+name+") or cc.Name in ("+name+") )";
		
		PreparedStatement ps = conn.prepareStatement(query);
		//int index = 1;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			accstatusdata data = new accstatusdata();
			data.setAccnumber(rs.getString("AccNumber"));
			data.setAccountstatus(rs.getString("Description"));
			A_data.add(data);
		}

		ps.close();
		rs.close();
		
		datalist = new bulkpaymentdatares[req.length];
		
		for(int j=0; j< datalist.length; j++){
			bulkpaymentdatares data = new bulkpaymentdatares();
			boolean isvalidacc = false;
			for(int i=0; i< A_data.size(); i++){
				if(req[j].getToaccnumber().equals(A_data.get(i).getAccnumber())){
					data.setRowno(req[j].getRowno());
					data.setAccountstatus(A_data.get(i).getAccountstatus());
					if(data.getAccountstatus().equalsIgnoreCase("Closed")){
						data.setReturncode("002");
					}else{
						if(pType.equals("20")){
							if(DBTransactionMgr.canWithdrawWithMultiCurrency(req[j].getToaccnumber(),req[j].getAmount(), conn)){
								data.setReturncode("300");
							}else{
								data.setReturncode("003");
							}
						}else{
							data.setReturncode("300");
						}
					}
					isvalidacc = true;
					break;
				}
			}
			if(!isvalidacc){
				data.setRowno(req[j].getRowno());
				data.setAccountstatus("Invalid Account!");
				data.setReturncode("001");
			}
			datalist[j] = data;
		}
		
		return datalist;
	}
	
	public custaccinforesult getaccountcustomerinfo(getaccountcustomerinforeq reqData, Connection conn) throws SQLException{
		custaccinforesult result = new custaccinforesult();
		ArrayList<AccountInfoData> ret = new ArrayList<AccountInfoData>();
		boolean isData = false;
		String whereclause = "";
		String condition = "";
		String caption = "";
		if(!(reqData.getSearch().equalsIgnoreCase("null") || reqData.getSearch().equalsIgnoreCase(""))){
			if (reqData.getSearchway().equalsIgnoreCase("0")) {// contain
				whereclause = " AND ( LOWER(c.NrcNo) LIKE LOWER(?) OR LOWER(c.CustomerId) LIKE LOWER(?) OR "
						+ "LOWER(a.AccNumber) LIKE LOWER(?) OR LOWER(c.Name) LIKE LOWER(?) )";
			}else if (reqData.getSearchway().equalsIgnoreCase("1")) {// equals
				whereclause = " AND ( LOWER(c.NrcNo) = LOWER(?) OR LOWER(c.CustomerId) = LOWER(?) OR "
						+ "LOWER(a.AccNumber) = LOWER(?) OR LOWER(c.Name) = LOWER(?) )";
			}else{
				whereclause = "";
			}
			caption = reqData.getSearch();
		}
		if(reqData.getSearchtype().equalsIgnoreCase("10")){//Individual
			condition = "AND at.AccType=110 ";
		}else if(reqData.getSearchtype().equalsIgnoreCase("20")){
			condition = "AND at.AccType IN (130,140,150,180) ";
		}
		
		String sql = "select c.Title + ' ' +c.Name as Name, c.NrcNo, c.OldNrcNo, c.DateOfBirth , c.CustomerId, "
				+ " a.AccNumber, a.currencycode,c.CustomerType, a.OpeningDate ,p.ProcessCode5,p.ProductCode, at.Description as acctype, "
				+ " (case when a.Status=0 and a.LastUpdate='19000101' then 'New' else "
				+ " case when a.Status=0 and a.LastUpdate<>'19000101' then 'Active' else RS.Description end end) as AccStatus"
				+ " from Customer c inner join CAJunction ca on c.CustomerId = ca.CustomerId and ca.RType=ca.AccType"
				+ " inner join Accounts a on ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1"
				+ " inner join acctype at on t.t3 = at.AccountCode inner join ProductType p on t.T2=p.ProductCode"
				+ " inner join (select StatusCode,description From RefAccountStatus where description<>'New') RS"
				+ " on A.Status=RS.statuscode where a.Status <> 3 "+condition+ " "+ whereclause;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		int i = 1;
		if(!whereclause.equals("")){
			if (reqData.getSearchway().equalsIgnoreCase("0")) {//contain
				ps.setString(i++, "%" + caption + "%");
				ps.setString(i++, "%" + caption + "%");
				ps.setString(i++, "%" + caption + "%");
				ps.setString(i++, "%" + caption + "%");
			}else if (reqData.getSearchway().equalsIgnoreCase("1")) {//equals
				ps.setString(i++, caption);
				ps.setString(i++, caption);
				ps.setString(i++, caption);
				ps.setString(i++, caption);
			}
		}
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			AccountInfoData aData = new AccountInfoData();
			aData.setCifid(rs.getString("CustomerId"));
			aData.setAccdob(rs.getString("DateOfBirth"));
			aData.setAccname(rs.getString("Name"));
			aData.setAccnrc(rs.getString("NrcNo"));
			aData.setAccnumber(rs.getString("AccNumber"));
			aData.setAcctype(rs.getString("acctype"));
			aData.setCurcode(rs.getString("currencycode"));
			aData.setProducttype(rs.getString("ProcessCode5"));
			aData.setProductcode(rs.getString("ProductCode"));
			aData.setOpeningdate(rs.getString("OpeningDate"));
			aData.setAccstatus(rs.getString("AccStatus"));
			
			ret.add(aData);
			isData = true;
		}
		if (isData == true) {
			result.setRetcode("300");
			result.setRetmessage("Successfully");
			// Nrc
			result.setDatalist(ret);

		} else {
			result.setRetcode("210");
			result.setRetmessage("There is no data");
			result.setDatalist(ret);

		}
		ps.close();
		rs.close();
		
		return result;
	}
	
	public accountnumberdataresponse checkaccnumber(String accno, Connection conn) throws SQLException{
		accountnumberdataresponse response = new accountnumberdataresponse();
		boolean isData = false;
		String sql = "select a.AccNumber, T.T5 as branchcode, bd.BranchName, c.Title + ' ' +c.Name as Name "
				+ " from Customer c inner join CAJunction ca on c.CustomerId = ca.CustomerId and ca.RType=ca.AccType "
				+ " inner join accounts a on ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1 "
				+ " inner join BankDatabases bd on t.t5=bd.BranchCode where a.AccNumber = ? and a.status <> 3";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, accno);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			response.setAccnumber(rs.getString("AccNumber"));
			response.setBrcode(rs.getString("branchcode"));
			response.setBrname(rs.getString("BranchName"));
			response.setAccname(rs.getString("Name"));
			isData = true;
		}
		
		if(isData){
			response.setBankname(getBankName(conn));
			response.setRetcode("300");
			response.setRetmessage("Successfully");
		}else{
			response.setRetcode("210");
			response.setRetmessage("There is no data");
		}
		ps.close();
		rs.close();
		
		return response;
	}
	
	public accountnumberdataresponse checkaccnumber2(String accno, Connection conn) throws SQLException{
		accountnumberdataresponse response = new accountnumberdataresponse();
		boolean isData = false;
		
		String sql = "select accno,AccDesc from GL where AccNo=?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, accno);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			response.setAccnumber(rs.getString("accno"));
			response.setAccname(rs.getString("AccDesc"));
			isData = true;
		}
		
		if(isData){
			response.setBankname(getBankName(conn));
			response.setRetcode("300");
			response.setRetmessage("Successfully");
		}else{
			response.setRetcode("210");
			response.setRetmessage("There is no data");
		}
		ps.close();
		rs.close();
		
		return response;
	}
	
	public String getBankName(Connection conn) throws SQLException{
		String bankname = "";
		
		String sql = "SELECT TOP(1) BankName FROM Banksetup";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			bankname = rs.getString("BankName");
		}
		
		ps.close();
		rs.close();
		
		return bankname;
	}
	
	public customerinfodataresult getcustomerinfo(getaccountcustomerinforeq reqData, Connection conn) throws SQLException{
		customerinfodataresult result = new customerinfodataresult();
		ArrayList<customerinfodata> datalist = new ArrayList<customerinfodata>();
		boolean isData = false;
		String whereclause = "";
		String condition = "";
		String caption = "";
		if(!(reqData.getSearch().equalsIgnoreCase("null") || reqData.getSearch().equalsIgnoreCase(""))){
			if (reqData.getSearchway().equalsIgnoreCase("0")) {// contain
				whereclause = " AND ( LOWER(c.CustomerId) LIKE LOWER(?) OR LOWER(c.Name) LIKE LOWER(?) OR "
						+ "LOWER(c.NrcNo) LIKE LOWER(?) OR LOWER(c.Phone) LIKE LOWER(?) )";
			}else if (reqData.getSearchway().equalsIgnoreCase("1")) {// equals
				whereclause = " AND ( LOWER(c.CustomerId) = LOWER(?) OR LOWER(c.Name) = LOWER(?) OR "
						+ "LOWER(c.NrcNo) = LOWER(?) OR LOWER(c.Phone) = LOWER(?) )";
			}else{
				whereclause = "";
			}
			caption = reqData.getSearch();
		}
		if(reqData.getSearchtype().equalsIgnoreCase("10")){ //Individual
			condition = "AND c.CustomerType<100 ";
		}else if(reqData.getSearchtype().equalsIgnoreCase("20")){ //Company
			condition = "AND c.CustomerType>=100 ";
		}
		
		String sql = "select distinct c.CustomerId, c.Title + ' ' +c.Name as Name, c.NrcNo, c.DateOfBirth , c.Email, c.Phone , "
				+ "(c.HouseNo+' '+c.Street+' '+c.Ward+' '+c.Township) as address, c.City, c.Division, c.Country, "
				+ "c.PostalCode from Customer c inner join CAJunction ca on c.CustomerId = ca.CustomerId "
				+ "and ca.RType=ca.AccType WHERE 1=1 "+ condition  +" "+ whereclause;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		int i = 1;
		if(!whereclause.equals("")){
			if (reqData.getSearchway().equalsIgnoreCase("0")) {//contain
				ps.setString(i++, "%" + caption + "%");
				ps.setString(i++, "%" + caption + "%");
				ps.setString(i++, "%" + caption + "%");
				ps.setString(i++, "%" + caption + "%");
			}else if (reqData.getSearchway().equalsIgnoreCase("1")) {//equals
				ps.setString(i++, caption);
				ps.setString(i++, caption);
				ps.setString(i++, caption);
				ps.setString(i++, caption);
			}
		}
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			customerinfodata data = new customerinfodata();				
			data.setCifid(rs.getString("CustomerId"));	//Customer ID
			data.setAccdob(rs.getString("DateOfBirth"));	//Date of Birth
			data.setAccname(rs.getString("Name"));		//Name
			data.setAccnrc(rs.getString("NrcNo"));		//NRC
			String [] email = rs.getString("Email").split(";");
			if(email.length>0)
				data.setEmail(email[0]);			//Email
			else
			    data.setEmail("");
			
			String [] phone = rs.getString("Phone").split(",");
			if(phone.length>0)
				data.setPhone(phone[phone.length-1]);	//Phone Number
			else
				data.setPhone("");	//Phone Number
			data.setState(rs.getString("Division"));		//State
			data.setCity(rs.getString("City"));			//City
			data.setPostalcode(rs.getString("PostalCode"));//Postal Code
			data.setCountry(rs.getString("Country"));		//Country
			data.setAddress(rs.getString("address"));		//Address
			datalist.add(data);
			isData = true;
		}
		if (isData == true) {
			result.setDatalist(datalist);
			result.setRetcode("300");
			result.setRetmessage("Successfully");
		} else {
			result.setRetcode("210");
			result.setRetmessage("There is no data");
		}
		ps.close();
		rs.close();
		
		return result;
	}
	
	public transactioninforesult gettransinfo(transactioninforequest reqData,Connection conn) throws SQLException{
		transactioninforesult result = new transactioninforesult();
		boolean isData = false;
		String sql = "select isnull(sum(Amount),0) as totalamount,count(transno) as totalcount"
				+ " from (Select amount, transno from AccountTransaction"
				+ " where AccNumber=? and effectivedate>=? and effectivedate<=? and TransType>=500 "
				+ " Union Select amount, transno from AccountTransactionOld"
				+ " where AccNumber=? and effectivedate>=? and effectivedate<=? and TransType>=500) A";
		PreparedStatement ps = conn.prepareStatement(sql);
		int i=1;
		ps.setString(i++, reqData.getAccnumber());
		ps.setString(i++, reqData.getFromdate());
		ps.setString(i++, reqData.getTodate());
		ps.setString(i++, reqData.getAccnumber());
		ps.setString(i++, reqData.getFromdate());
		ps.setString(i++, reqData.getTodate());
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			result.setTotalcount(rs.getInt("totalcount"));
			result.setTotalamount(rs.getDouble("totalamount"));
			result.setTotalamount(GeneralUtil.formatDecimal(result.getTotalamount()));
			isData = true;
		}
		
		if (isData == true) {
			result.setRetcode("300");
			result.setRetmessage("Successfully");
		} else {
			result.setRetcode("210");
			result.setRetmessage("There is no data");
		}
		
		ps.close();
		rs.close();
		
		return result;
	}
	
	public ArrayList<CheckIssueData> getCheckBooks(CheckIssueCriteriaData pCheckIssueCriteriaBean, Connection conn)
			throws SQLException {
		ArrayList<CheckIssueData> ret = new ArrayList<CheckIssueData>();
		StringBuffer l_query = new StringBuffer("");

		l_query.append("SELECT [CheckIssue].[AccNumber], [CheckIssue].[ChkNoFrom], "
				+ "[CheckIssue].[DateIssued], [CheckIssue].[DateApproved], "
				+ "[CheckIssue].[Status], [CheckIssue].[Sheets], [CheckIssue].[ChkNoChar],"
				+ "[CheckIssue].[CurrencyCode],[CheckIssue].[T2] " + "FROM [CheckIssue] WHERE 1=1");

		PrepareWhereClause(pCheckIssueCriteriaBean, l_query);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		prepareCriteria(pCheckIssueCriteriaBean, pstmt);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			CheckIssueData object = new CheckIssueData();
			readRecord(object, rs);
			ret.add(object);
		}
		pstmt.close();
		return ret;
	}
	
	private void PrepareWhereClause(CheckIssueCriteriaData aCheckIssueCriteriaBean, StringBuffer aQuery) {
		if (!aCheckIssueCriteriaBean.AccountNumber.equals("")) {
			aQuery.append(" AND [CheckIssue].[AccNumber] = ?");
		}
		if (aCheckIssueCriteriaBean.ChkNoFrom != 0) {
			aQuery.append(" AND [CheckIssue].[ChkNoFrom] = ?");
		}
		if (!aCheckIssueCriteriaBean.ChkNoChar.equals("")) {
			aQuery.append(" AND [CheckIssue].[ChkNoChar] = ?");
		}
		if (aCheckIssueCriteriaBean.Status != 255) {
			aQuery.append(" AND [CheckIssue].[Status] = ?");
		}
		if (!aCheckIssueCriteriaBean.RequestNo.equals("")) {
			aQuery.append(" AND [CheckIssue].[T1] = ?");
		}
	}

	private void prepareCriteria(CheckIssueCriteriaData aCheckIssueCriteriaBean, PreparedStatement aPS)
			throws SQLException {
		int l_paraIndex = 0;
		if (!aCheckIssueCriteriaBean.AccountNumber.equals("")) {
			l_paraIndex += 1;
			aPS.setString(l_paraIndex, aCheckIssueCriteriaBean.AccountNumber);
		}
		if (aCheckIssueCriteriaBean.ChkNoFrom != 0) {
			l_paraIndex += 1;
			aPS.setInt(l_paraIndex, aCheckIssueCriteriaBean.ChkNoFrom);
		}
		if (!aCheckIssueCriteriaBean.ChkNoChar.equals("")) {
			l_paraIndex += 1;
			aPS.setString(l_paraIndex, aCheckIssueCriteriaBean.ChkNoChar);
		}
		if (aCheckIssueCriteriaBean.Status != 255) {
			l_paraIndex += 1;
			aPS.setInt(l_paraIndex, aCheckIssueCriteriaBean.Status);
		}
		if (!aCheckIssueCriteriaBean.RequestNo.equals("")) {
			l_paraIndex += 1;
			aPS.setString(l_paraIndex, aCheckIssueCriteriaBean.RequestNo);
		}
	}
	
	private void readRecord(CheckIssueData aCheckIssueBean, ResultSet aRS) throws SQLException {
		aCheckIssueBean.setAccountNumber(aRS.getString("AccNumber"));
		aCheckIssueBean.setChkNoFrom(aRS.getInt("ChkNoFrom"));
		aCheckIssueBean.setDateIssued(GeneralUtil.formatDBDate2MIT(aRS.getString("DateIssued")));
		aCheckIssueBean.setDateApproved(GeneralUtil.formatDBDate2MIT(aRS.getString("DateApproved")));
		aCheckIssueBean.setStatus(aRS.getInt("Status"));
		aCheckIssueBean.setSheets(aRS.getString("Sheets"));
		aCheckIssueBean.setChkNoChar(aRS.getString("ChkNoChar"));
		aCheckIssueBean.setCurrencyCode(aRS.getString("CurrencyCode"));
		aCheckIssueBean.setBranchCode(aRS.getString("T2"));
	}
	
	public int getCHQ(Connection conn) throws SQLException {
		int number = 0;
		StringBuffer l_query = new StringBuffer("");
		l_query.append("SELECT n2 FROM Systemsetting WHERE t1='CHQ'");

		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			number = rs.getInt("n2");
		}

		pstmt.close();
		return number;
	}
	
	public static boolean isLoanAcc(String acc, Connection conn) throws SQLException{
		boolean res = false;
		String l_query = "select count(AccNumber) as a_count From LAODF where accnumber=? and status<>3";
		PreparedStatement pstmt = conn.prepareStatement(l_query);
		pstmt.setString(1, acc);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			if(rs.getInt("a_count") > 0)
				res = true;
			else 
				res = false;
		}
		pstmt.close();
		
		return res;
	}
	
	public fixeddepositinfores getfixeddepositinfo(Connection conn) throws SQLException{
		fixeddepositinfores response = new fixeddepositinfores();
		ArrayList<fixeddepositinfodata> datalist = new ArrayList<fixeddepositinfodata>();
		
		String l_query = "select ProductCode,ProcessCode5,(case when (Tenure<=100 and Tenure<12) then Tenure "
				+ "when (Tenure<=100 and Tenure>=12) then Tenure/12 "
				+ "when (Tenure>100 and Tenure<=1000) then Tenure-100 else Tenure-1000 end) Tenure, "
				+ "(case when (Tenure<=100 and Tenure<12) then 'Months' when (Tenure<=100 and Tenure>=12) then 'Years' "
				+ "when (Tenure>100 and Tenure<=1000) then 'Days' else 'Weeks' end) TenureType, Tenure as dataTenure, YearlyRate "
				+ "from ProductType a inner join ProductInterestRate b "
				+ "on a.ProductCode=b.AccType where lower(a.processcode5) like 'fixed deposit%' and DateTo='9999-12-31' order by b.Tenure";
		PreparedStatement pstmt = conn.prepareStatement(l_query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			fixeddepositinfodata data = new fixeddepositinfodata();
			data.setProductcode(rs.getString("ProductCode"));
			data.setDescription(rs.getString("ProcessCode5"));
			data.setTenure(rs.getString("Tenure"));
			data.setYearlyrate(rs.getString("YearlyRate"));
			data.setTenuretype(rs.getString("TenureType"));
			data.setDatatenure(rs.getString("dataTenure"));
			datalist.add(data);
		}
		pstmt.close();
		rs.close();
		
		if (datalist.size() > 0) {
			response.setDatalist(datalist);
			response.setRetcode("300");
			response.setRetmessage("Successfully");
		} else {
			response.setRetcode("210");
			response.setRetmessage("There is no data");
		}
		
		return response;
	}
	
	public refdataresponse getbranchesbybank(String bankcode, Connection conn) throws SQLException{
		refdataresponse response = new refdataresponse();
		ArrayList<refdata> datalist = new ArrayList<refdata>();
		
		String l_query = "select A.BranchCode,A.BranchName from Branch A inner join Banks B on A.t4=B.BankCode and B.BankCode=?";
		PreparedStatement pstmt = conn.prepareStatement(l_query);
		pstmt.setString(1, bankcode);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			refdata data = new refdata();
			data.setCode(rs.getString("BranchCode"));
			data.setDescription(rs.getString("BranchName"));
			datalist.add(data);
		}
		pstmt.close();
		rs.close();
		
		if (datalist.size() > 0) {
			response.setDatalist(datalist);
			response.setRetcode("300");
			response.setRetmessage("Successfully");
		} else {
			response.setRetcode("210");
			response.setRetmessage("There is no data");
		}
		
		return response;
	}
	public accountcheckres getaccdata(String acctno, Connection conn) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
		accountcheckres cusret = new accountcheckres();
		boolean isData = false;
		String sql = "select t.T5 as branchcode,a.OpeningDate, a.currentBalance, a.currencyCode, a.AccNumber,"
				+ "p.ProcessCode5, p.ProcessCode3, at.Description as acctype, (case when cc.Title='' then cc.name else "
				+ "(cc.title+' '+cc.Name) end) as cusname ,bd.BranchName,(case when a.Status=0 and a.LastUpdate='19000101' "
				+ "then 'New' else case when a.Status=0 and a.LastUpdate<>'19000101' then 'Active' else RS.Description end end) "
				+ "as Status from accounts a inner join T00005 t on a.AccNumber = t.T1 inner join acctype at "
				+ "on t.t3 = at.AccountCode inner join CAJunction ca on ca.AccNumber=t.t1 and ca.AccType = ca.RType "
				+ "inner join ProductType p on t.T2=p.ProductCode inner join Customer cc on cc.CustomerId = ca.CustomerId "
				+ "inner join BankDatabases bd on t.t5=bd.BranchCode inner join "
				+ "(select StatusCode,description From RefAccountStatus where description<>'New') RS on A.Status=RS.statuscode "
				+ "where a.AccNumber = ? and a.status <> 3";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, acctno);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			cusret.setAccname(rs.getString("cusname"));
			cusret.setAccnumber(rs.getString("AccNumber"));
			cusret.setBrname(rs.getString("BranchName"));
			cusret.setCurcode(rs.getString("currencyCode"));
			cusret.setCurrentbalance(GeneralUtil.formatDecimalString(rs.getDouble("currentBalance")));
			cusret.setStatus(rs.getString("Status"));
			cusret.setProductname(rs.getString("ProcessCode5"));
			cusret.setProducttype(rs.getString("ProcessCode3"));
			cusret.setAcctype(rs.getString("acctype"));
			cusret.setBrcode(rs.getString("branchcode"));
			cusret.setOpeningdate(rs.getString("OpeningDate")); 
			if(isLoanAcc(cusret.getAccnumber(), conn)){
				cusret.setAvailablebalance(GeneralUtil.formatDecimalString(rs.getDouble("currentBalance")));
			}else{
				double l_Available = DBAccountMgr.getAvailableBalance(cusret.getAccnumber(), conn);
				cusret.setAvailablebalance(GeneralUtil.formatDecimalString(l_Available));
			}
			isData = true;
		}
		if (isData == true) {
			cusret.setRetcode("300");
			cusret.setRetmessage("Successfully");

		} else {
			cusret.setRetcode("210");
			cusret.setRetmessage("There is no data.");
		}
		ps.close();
		rs.close();
		return cusret;
	}
	
	public refdataresponse getacctype(Connection conn) throws SQLException{
		refdataresponse response = new refdataresponse();
		ArrayList<refdata> datalist = new ArrayList<refdata>();
		
		String l_query = "select Description,AccountCode From AccType";
		PreparedStatement pstmt = conn.prepareStatement(l_query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			refdata data = new refdata();
			data.setCode(rs.getString("AccountCode"));
			data.setDescription(rs.getString("Description"));
			datalist.add(data);
		}
		pstmt.close();
		rs.close();
		
		if (datalist.size() > 0) {
			response.setDatalist(datalist);
			response.setRetcode("300");
			response.setRetmessage("Successfully");
		} else {
			response.setRetcode("210");
			response.setRetmessage("There is no data");
		}
		
		return response;
	}
	
	public refdataresponse getBranchList(Connection conn) throws SQLException{
		refdataresponse response = new refdataresponse();
		ArrayList<refdata> datalist = new ArrayList<refdata>();
		
		String l_query = "select branchcode,BranchName from bankdatabases";
		PreparedStatement pstmt = conn.prepareStatement(l_query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			refdata data = new refdata();
			data.setCode(rs.getString("BranchCode"));
			data.setDescription(rs.getString("BranchName"));
			datalist.add(data);
		}
		pstmt.close();
		rs.close();
		
		if (datalist.size() > 0) {
			response.setDatalist(datalist);
			response.setRetcode("300");
			response.setRetmessage("Successfully");
		} else {
			response.setRetcode("210");
			response.setRetmessage("There is no data");
		}
		
		return response;
	}
	
	public static String getAccountDescription(String a,Connection conn){
		String str="";
		ArrayList<AccountCustomerData> ret = new ArrayList<AccountCustomerData>();
		CAJunctionDAO l_ACDAO = new CAJunctionDAO();
		
		try {
			if (l_ACDAO.getCAJunctions(a, conn))
			{
				ret = l_ACDAO.getCAJunctionBeanList();
			}
			
			for (int i=0;i<ret.size();i++){
				if (!str.equals("")) str+="; ";
				str += ret.get(i).getCustomer().getName();
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
        return str;
	}
	
	public static double getAvailableBalanceAccountLinks(String pAccNumber, Connection pConn) throws IOException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException {
		double l_AvailableBalance = 0;
		
		ArrayList<AccountLinksData> l_ArlAccLinksData = new ArrayList<AccountLinksData>();
		/*AccountLinksDao l_AccLinkDAO = new AccountLinksDao();*/
		
		l_AvailableBalance = getAvailableBalance(pAccNumber,0, pConn);
		
		// Checking Account Has Account Links
		l_ArlAccLinksData = AccountLinksDao.getAccountlinkList(pAccNumber, pConn);
		
		if (l_ArlAccLinksData.size() > 0) {
			for (int i=0; i<l_ArlAccLinksData.size(); i++) {
				l_AvailableBalance += getAvailableBalance(l_ArlAccLinksData.get(i).getChildAccount(),0, pConn);
			}
		}
		
		return l_AvailableBalance;
	}
	
	public static double getAvailableBalanceForInformation(String pAccNumber, String type, Connection pConn) throws IOException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException {
		double l_AvailableBalance = 0;
		double l_ProdMinBalance = 0;
		double l_BarAmount = 0;
		double l_TotalOD = 0;
		AccountData l_AccData = new AccountData();
		AccountDao l_AccDAO = new AccountDao();
		AccBarDAO l_AccBarDAO = new AccBarDAO();
		CurrentAccountDAO l_CurAccDAO = new CurrentAccountDAO();
		AccountData l_CurAccData = new AccountData();
			
		String l_TodayDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		// Checking Product Type
		if(l_AccDAO.getAccount(pAccNumber, pConn));
		l_AccData = l_AccDAO.getAccountsBean();
		String productCode = l_AccData.getProduct();
		
		if (SharedLogic.isCurrent(productCode)) {
			
			if(l_CurAccDAO.getAccount(pAccNumber, pConn)){
				// Current Account
				l_CurAccData = l_CurAccDAO.getCurrentAccBean();
				
				if(l_CurAccData.getIsOD() == 1){
					// OD Account
					l_AccData = l_AccDAO.getODAvailableBalanceForInformation(l_TodayDate, pAccNumber, type, pConn);
					
					// Get Bar Amount
					l_BarAmount = l_AccBarDAO.getTotalBarAmount(pAccNumber, pConn);
					
					// Getting Product Minimum Amount
					l_ProdMinBalance = SharedLogic.getAccountMinBalanceNew(productCode,l_AccData.getCurrencyCode());
					
					if (l_BarAmount != 0 ) {
						l_ProdMinBalance = 0;
					}
						
					l_TotalOD = 0;
					
					if (l_AccData.getOdLimit() > 0 || l_AccData.getTODLimit() > 0 ) {
						l_TotalOD = l_AccData.getOdLimit() + l_AccData.getTODLimit();
						l_ProdMinBalance = 0;
					}
					
					l_AvailableBalance = l_AccData.getCurrentBalance() + l_TotalOD - l_ProdMinBalance - l_BarAmount;
				}else{
					// Current Account
					l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber, 0, pConn);
				}
			}
								
		}else if (SharedLogic.isLoan(productCode)) {
			if(SharedLogic.getSystemData().getpSystemSettingDataList().get("LSC").getT3().equalsIgnoreCase("1")){
				// 1 = MCB Loan					
				l_AccData = l_AccDAO.getLAAvailableBalanceForInformation(l_TodayDate, pAccNumber, pConn);
				
				// Get Bar Amount
				l_BarAmount = l_AccBarDAO.getTotalBarAmount(pAccNumber, pConn);
				
				// Getting Product Minimum Amount
				l_ProdMinBalance = SharedLogic.getAccountMinBalanceNew(productCode, l_AccData.getCurrencyCode());
				
				if (l_BarAmount != 0 ) {
					l_ProdMinBalance = 0;
				}
				
				l_AvailableBalance = l_AccData.getCurrentBalance() + l_AccData.getOdLimit() - l_ProdMinBalance - l_BarAmount;
			}else{
				// 4 BANK LOAN
				l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber,0, pConn); 
			}
		}else {	// Other Product
			l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber, 0, pConn); 
		}
		
		if (l_AvailableBalance < 0 ) {
			l_AvailableBalance = 0;
		}
		l_AvailableBalance = StrUtil.round2decimals(l_AvailableBalance);
	
		
		return l_AvailableBalance;
	}
	
	public static double getAvailableBalance(String pAccNumber, int pProcessType, Connection pConn) throws IOException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException {
		double l_AvailableBalance = 0;
		double l_ProdMinBalance = 0;
		double l_BarAmount = 0;
		double l_TotalOD = 0;
		AccountData l_AccData = new AccountData();
		AccountsDAO l_AccDAO = new AccountsDAO(pConn);
		AccBarDAO l_AccBarDAO = new AccBarDAO();
		CurrentAccountDAO l_CurAccDAO = new CurrentAccountDAO();
		AccountData l_CurAccData = new AccountData();
			
		String l_TodayDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		// Checking Product Type
		if(l_AccDAO.getAccount(pAccNumber, pConn));
		l_AccData = l_AccDAO.getAccountsBean();
		String productCode = l_AccData.getProduct();
		int l_RemoveBar=SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN3();
		
		if (SharedLogic.isCurrent(productCode)) {
			
			if(l_CurAccDAO.getAccount(pAccNumber, pConn)){
				// Current Account
				l_CurAccData = l_CurAccDAO.getCurrentAccBean();
				
				if(l_CurAccData.getIsOD() == 1){
					// OD Account
					l_AccData = l_AccDAO.getODAvailableBalance(l_TodayDate, pAccNumber, pProcessType, pConn);
					
					// Get Bar Amount
					l_BarAmount = l_AccBarDAO.getTotalBarAmount(pAccNumber, pConn);
					
					// Getting Product Minimum Amount
					if( pProcessType!=4){
						l_ProdMinBalance = SharedLogic.getAccountMinBalanceNew(productCode,l_CurAccData.getCurrencyCode());
					}else
						l_ProdMinBalance = 0;					
					
					if (l_BarAmount != 0 ) {
						l_ProdMinBalance = 0;
					}					
					if(l_RemoveBar==1 && pProcessType==4){
						l_BarAmount=0;
					}						
					l_TotalOD = 0;
					
					if (l_AccData.getOdLimit() > 0 || l_AccData.getTODLimit() > 0 ) {
						l_TotalOD = l_AccData.getOdLimit() + l_AccData.getTODLimit();
						l_ProdMinBalance = 0;
					}
					
					l_AvailableBalance = l_AccData.getCurrentBalance() + l_TotalOD - l_ProdMinBalance - l_BarAmount;
				}else{
					// Current Account
					l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber, pProcessType, pConn);
				}
			}
			/*l_AccData = l_AccDAO.getAccountForODAvaBalance(pAccNumber, pConn);*/						
		}
		else {	// Other Product
			l_AvailableBalance = l_AccDAO.getAvaliableBalance(pAccNumber,pProcessType, pConn); 
		}
		
		if (l_AvailableBalance < 0 ) {
			l_AvailableBalance = 0;
		}
		l_AvailableBalance = StrUtil.round2decimals(l_AvailableBalance);
	
		
		return l_AvailableBalance;
	}//end
	
	public static ArrayList<CheckIssueData> getCheckSheets(String aAccNumber,Connection pConn) throws SQLException{
		AccountData object = new AccountData();
		AccountDao l_AccDAO = new AccountDao();
		ArrayList<CheckIssueData> ret = new ArrayList<CheckIssueData>();
		ArrayList<CheckIssueData> l_approvedBook = new ArrayList<CheckIssueData>();
		int showOnlyUnpaidCHQ=SharedLogic.getSystemData().getpSystemSettingDataList().get("CHQSERIES").getN2();
		try {
				if (l_AccDAO.getAccount(aAccNumber,pConn)){
					object = l_AccDAO.getAccountsBean();
					// Checking Product Type
					if (SharedLogic.getProductCodeToProductData(
							object.getProduct()).hasCheque()){
						// Get CheckIssue Data
						
						CheckIssueDAO l_DAO = new CheckIssueDAO();
						CheckIssueCriteriaData l_CheckIssueCriData = new CheckIssueCriteriaData();
						
						l_CheckIssueCriData.AccountNumber = object.getAccountNumber();
						if (showOnlyUnpaidCHQ==1){
							if(!FormParameters.isShowPaidChk())
								l_CheckIssueCriData.displayStatus="All";
						}
						ret = l_DAO.getCheckBooks(l_CheckIssueCriData, pConn);
						
						for(int i = 0; i < ret.size(); i++){
							if(ret.get(i).getStatus() == 0 || ret.get(i).getStatus() == 1){ //ME issue
								l_approvedBook.add(ret.get(i));
							}
						}
						
						if (l_approvedBook.size() > 0) {
							for (int i = 0; i < l_approvedBook.size(); i++) {
								CheckIssueData l_CheckIssue = new CheckIssueData();
								l_CheckIssue = l_approvedBook.get(i);
								l_approvedBook.get(i).setCheckSheetDataList(getCheckSheetList(l_CheckIssue,showOnlyUnpaidCHQ));
							}
						}							
					}
				 } else{					
					 l_approvedBook = null;
				 }
					
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return l_approvedBook;
	}
	
	private static ArrayList<CheckSheetData> getCheckSheetList(CheckIssueData aCheckIssueData,int aShowOnlyUnpaidCHQ) {
		
		ArrayList<CheckSheetData> l_lstCheckSheet = new ArrayList<CheckSheetData>();
		CheckSheetData l_CheckSheetData = new CheckSheetData();
		String l_CheckInfo = "";
		int l_CheckStatus = 255;
		String l_CheckSheetStart = "";
		String l_CheckSheetEnd = "";
		String l_CheckChar = aCheckIssueData.getChkNoChar();
		int l_ChkNoFrom = aCheckIssueData.getChkNoFrom();
		
		//HNNS
		ArrayList<SystemSettingData> lstData = new ArrayList<SystemSettingData>();
		int l_Checklength = 0;
		try {
			l_Checklength = SharedLogic.getSystemData().getpSystemSettingDataList().get("CHQ").getN2();
			
			if(l_Checklength==0)
				l_Checklength = 9;
			for (int i = 0; i < aCheckIssueData.getSheets().length(); i++) {
				l_CheckSheetData = new CheckSheetData();			
				
				l_CheckSheetStart = l_CheckChar + SharedUtil.leadZeros(aCheckIssueData.getChkNoFrom(), l_Checklength- l_CheckChar.length());
				l_CheckSheetEnd = l_CheckChar + SharedUtil.leadZeros((aCheckIssueData.getChkNoFrom() + aCheckIssueData.getSheets().length() - 1), l_Checklength - l_CheckChar.length());
				
				l_CheckStatus = Integer.parseInt(String.valueOf(aCheckIssueData.getSheets().charAt(i)));
				l_CheckInfo = aCheckIssueData.getChkNoChar()+ SharedUtil.leadZeros(l_ChkNoFrom++, l_Checklength - l_CheckChar.length()) ;
				
				l_CheckSheetData.setCheckSheetStatus((byte)l_CheckStatus);
				l_CheckSheetData.setCheckSheetStatusInWord(SharedLogic.getCheckStatus(l_CheckStatus));
				l_CheckSheetData.setCheckSheetNo(l_CheckInfo);
				l_CheckSheetData.setCheckSheetStart(l_CheckSheetStart);
				l_CheckSheetData.setCheckSheetEnd(l_CheckSheetEnd);
				
				if(aShowOnlyUnpaidCHQ==1){
					if(l_CheckSheetData.getCheckSheetStatus()==0)
						l_lstCheckSheet.add(l_CheckSheetData);
				}else{
					l_lstCheckSheet.add(l_CheckSheetData);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l_lstCheckSheet;
	}
	
	public CustInfoResult getAmountOfAccount(String custno, String fixedDeposit, String customertype,String[] excludedaccounts, Connection conn)
			throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
		CustInfoResult cusret = new CustInfoResult();
		ArrayList<AccountInfo> ret = new ArrayList<AccountInfo>();
		AccountInfo[] accarr = null;
		boolean isData = false;
		String deposit = "",sql2="";
		String condition = "" ,exclededcondition = "";
		PreparedStatement ps;
		
		String l_TodayDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		if (fixedDeposit.equalsIgnoreCase("No")) {
			deposit = "and t.t2<>(SELECT t1 FROM T00001 Where N1=6 AND N2=4)";
		}

		if (customertype.equalsIgnoreCase("10")) {// Individual
			condition = "AND at.AccType=110 ";
		} else if (customertype.equalsIgnoreCase("20")) {
			// 140 Co-Operative, 150 Association, 180 Organization
			condition = "AND at.AccType IN (130,140,150,180) ";
		}
		
		if (excludedaccounts != null && excludedaccounts.length > 0) {// for excluded accounts
			exclededcondition = " and a.accnumber not in (";
			for(int i = 0 ; i < excludedaccounts.length ; i++) {
				if(i == 0) {
					exclededcondition += "'"+excludedaccounts[i]+"'";
				}else {
					exclededcondition += ",'"+excludedaccounts[i]+"'";
				}
			}
			exclededcondition += ")";
		}
		
		String tmpTableName = "TmpAccounts" + custno;
		String sql1 = "IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[" + tmpTableName
				+ "]') AND type in (N'U')) " + " DROP TABLE " + tmpTableName;
		ps = conn.prepareStatement(sql1);
		ps.executeUpdate();
	
		if(SharedLogic.getSystemData().getAccMinSetting() == 1) {
			sql2 = "select a.accnumber,a.currentbalance,ca.customerid,t.t2,t.t3,c.n1,t.t4,a.CurrentBalance as minbalance,t.t2+t.t3 as productid,"
					+ "convert(decimal(18,2),0) as baramount, convert(decimal(18,2),0) as odlimit, convert(decimal(18,2),0) as todlimit, "
					+ "a.currentbalance as avabalance,CONVERT(varchar(50),'') as productname into "+tmpTableName+ " from CAJunction ca inner join accounts a on "
					+ "ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1 " + deposit
					+ "inner join acctype at on t.t3 = at.AccountCode and ca.AccType = ca.RType inner join CurrencyTable c on t.t4=c.CurCode "
					+ "where ca.CustomerId=? and a.status <> 3  " + condition+ exclededcondition ;
		}else {
			sql2 = "select a.accnumber,a.currentbalance,ca.customerid,t.t2,t.t3,c.n1,t.t4,a.CurrentBalance as minbalance,t.t2 as productid, "
					+ "a.currentbalance as avabalance,CONVERT(varchar(50),'') as productname into "+tmpTableName+ " from CAJunction ca inner join accounts a on "
					+ "ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1 " + deposit
					+ "inner join acctype at on t.t3 = at.AccountCode and ca.AccType = ca.RType inner join CurrencyTable c on t.t4=c.CurCode "
					+ "where ca.CustomerId=? and a.status <> 3  " + condition + exclededcondition ;
		}
		ps = conn.prepareStatement(sql2);
		ps.setString(1, custno);
		ps.executeUpdate();

		if(SharedLogic.getSystemData().getCurBalSetting() ==1) {
			sql2="Update "+tmpTableName+" set productid=productid+n1 where t4<>'MMK'";
			ps = conn.prepareStatement(sql2);
			ps.executeUpdate();
		}		
		
		sql2="Update a set a.minbalance=p.minbalance,a.productname=p.ProductName from "+tmpTableName+" a "
			+ "inner join ProductSetup p on a.ProductId=p.productid ";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();
		
		sql2 = "update a set a.baramount= BAmt, a.minbalance=(case when BAmt>0 then 0 else minbalance end) From "+tmpTableName+" a inner join "
			+ "(select Sum(a.BarAmount) BAmt,a.AccNumber FROM AccBar A inner join "+tmpTableName+" acc on a.AccNumber=acc.AccNumber group by a.AccNumber) b "
			+ "on a.AccNumber=b.AccNumber";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();
		
		sql2 = "update a set a.odlimit= t.ODLimit,a.todlimit=t.todlimit, a.minbalance=(case when t.ODLimit>0 or t.todlimit>0 then 0 else minbalance end) "
			+ "From "+tmpTableName+" a inner join "
			+ "(select sum(a.sanctionamountbr) as ODLimit, (case when b.todexpdate >= ? then b.todlimit else 0 end) as todlimit, "
			+ "a.AccNumber from aodf a inner join currentaccount b on a.accnumber = b.accnumber inner join "+tmpTableName+" c "
			+ "on b.AccNumber = c.AccNumber where a.status in ('1') and a.expdate >= ? and a.batchno <> 0 "
			+ "and a.odtype <> 0 and a.odtype <> 100 group by todexpdate, b.todlimit,a.AccNumber) t on a.AccNumber=t.AccNumber";
		ps = conn.prepareStatement(sql2);
		ps.setString(1, l_TodayDate);
		ps.setString(2, l_TodayDate);
		ps.executeUpdate();
		
		sql2 = "update a set a.odlimit= t.ODLimit,a.todlimit=t.todlimit, a.minbalance=(case when t.ODLimit>0 or t.todlimit>0 then 0 else minbalance end)"
			+ " From "+tmpTableName+" a inner join	"
			+ "(select sum(a.sanctionamountbr) as ODLimit, (case when CONVERT(datetime , b.todexpdate) >= ? then b.todlimit else 0 end) as todlimit, "
			+ "a.AccNumber from aodf a inner join currentaccount b on a.accnumber = b.accnumber "
			+ "inner join "+tmpTableName+" c on b.AccNumber = c.AccNumber where a.status in ('1') "
			+ "and CONVERT(datetime , a.expdate) >= ? and (a.batchno = 0 or a.odtype in (0,100)) "
			+ "group by todexpdate, b.todlimit, a.AccNumber) t on a.AccNumber=t.AccNumber";
		ps = conn.prepareStatement(sql2);
		ps.setString(1, l_TodayDate);
		ps.setString(2, l_TodayDate);
		ps.executeUpdate();		

		if (SharedLogic.getSystemData().getBarminbalSetting() == 1)
			sql2 = "Update "+tmpTableName+" set avabalance=CurrentBalance+odlimit+todlimit-baramount-minbalance";
		else
			sql2 = "Update "+tmpTableName+" set avabalance=CurrentBalance+odlimit+todlimit-baramount";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();		
		
		sql2 = "Update "+tmpTableName+" set productname='OD Account' where currentbalance<0 and productname like '%current%'" ;
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();		
		
		String query = "select sum(avabalance) avabal,t2,productname  from "+tmpTableName+" group by t2,productname";
		ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			AccountInfo aData = new AccountInfo();
			aData.setProductname(rs.getString("productname"));
			aData.setProducttype(rs.getString("t2"));
			aData.setAvailablebalance(rs.getDouble("avabal"));
			ret.add(aData);
			isData = true;
		}
		
		if (ret.size() > 0) {
			accarr = new AccountInfo[ret.size()];
			for (int i = 0; i < ret.size(); i++) {
				accarr[i] = ret.get(i);
			}
		}
		if (isData == true) {
			cusret.setCode("300");
			cusret.setDesc("Successfully");
			cusret.setAccountInfoArr(accarr);

		} else {
			cusret.setCode("210");
			cusret.setDesc("There is no data.");
			cusret.setAccountInfoArr(accarr);
		}

		ps.close();
		rs.close();
		return cusret;
	}	
	
	public CustInfoResult getAccountInfobyProduct(String custno, String fixedDeposit, String searchtype,String customertype,String[] excludedaccounts, Connection conn) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException 
	{
		CustInfoResult cusret = new CustInfoResult();
		ArrayList<AccountInfo> ret = new ArrayList<AccountInfo>();
		AccountInfo[] accarr = null;
		boolean isData = false;
		String deposit = "", sql2 = "";
		String condition = "",exclededcondition = "";
		PreparedStatement ps;
		String l_TodayDate = SharedUtil.formatDDMMYYYY2MIT(StrUtil.getCurrentDate());
		if (fixedDeposit.equalsIgnoreCase("No")) {
			deposit = "and t.t2<>(SELECT t1 FROM T00001 Where N1=6 AND N2=4)";
		}
		if (searchtype.equalsIgnoreCase("LOAN")) {// for all Loan product and OD
			condition = " AND a.CurrentBalance<0 ";
		} else {
			condition = " AND t.t2='" + searchtype + "'";
		}
		
		if(customertype.equalsIgnoreCase("10")){//Individual
			condition += " AND at.AccType=110 ";
		}else if(customertype.equalsIgnoreCase("20")){
			//140 Co-Operative, 150 Association, 180 Organization
			condition += " AND at.AccType IN (130,140,150,180) ";
		}
		
		if (excludedaccounts != null && excludedaccounts.length > 0) {// for excluded accounts
			exclededcondition = " and a.accnumber not in (";
			for(int i = 0 ; i < excludedaccounts.length ; i++) {
				if(i == 0) {
					exclededcondition += "'"+excludedaccounts[i]+"'";
				}else {
					exclededcondition += ",'"+excludedaccounts[i]+"'";
				}
			}
			exclededcondition += ")";
		}

		String tmpTableName = "TmpAccounts" + custno;
		String sql1 = "IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[" + tmpTableName
				+ "]') AND type in (N'U')) " + " DROP TABLE " + tmpTableName;
		ps = conn.prepareStatement(sql1);
		ps.executeUpdate();

		if (SharedLogic.getSystemData().getAccMinSetting() == 1) {
			sql2 = "select a.accnumber,a.currentbalance,ca.customerid,t.t2,t.t3,at.Description as acctype, c.n1,t.t4,t.t5 as branchcode,a.CurrentBalance as minbalance,"
					+ "t.t2+t.t3 as productid,"
					+ "convert(decimal(18,2),0) as baramount, convert(decimal(18,2),0) as odlimit, convert(decimal(18,2),0) as todlimit, "
					+ "a.currentbalance as avabalance,CONVERT(varchar(50),'') as productname,CONVERT(varchar(50),'') as branchname,"
					+ "a.OpeningDate, a.LastUpdate, a.Status,CONVERT(nvarchar(20),'') as statusname,"
					+ "CONVERT(nvarchar(50),'') as cusname into " + tmpTableName
					+ " from CAJunction ca inner join accounts a on "
					+ "ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1 " + deposit
					+ "inner join acctype at on t.t3 = at.AccountCode and ca.AccType = ca.RType inner join CurrencyTable c on t.t4=c.CurCode "
					+ "where ca.CustomerId=? and a.status <> 3 " + condition+exclededcondition;
		} else {
			sql2 = "select a.accnumber,a.currentbalance,ca.customerid,t.t2,t.t3,c.n1,t.t4,a.CurrentBalance as minbalance,t.t2 as productid, "
					+ "a.currentbalance as avabalance,CONVERT(varchar(50),'') as productname into " + tmpTableName
					+ " from CAJunction ca inner join accounts a on "
					+ "ca.AccNumber = a.AccNumber inner join T00005 t on a.AccNumber = t.T1 " + deposit
					+ "inner join acctype at on t.t3 = at.AccountCode and ca.AccType = ca.RType inner join CurrencyTable c on t.t4=c.CurCode "
					+ "where ca.CustomerId=? and a.status <> 3 " + condition+exclededcondition;
		}
		ps = conn.prepareStatement(sql2);
		ps.setString(1, custno);
		ps.executeUpdate();

		if (SharedLogic.getSystemData().getCurBalSetting() == 1) {
			sql2 = "Update " + tmpTableName + " set productid=productid+n1 where t4<>'MMK'";
			ps = conn.prepareStatement(sql2);
			ps.executeUpdate();
		}

		sql2 = "Update a set a.minbalance=p.minbalance,a.productname=p.ProductName from " + tmpTableName + " a "
				+ "inner join ProductSetup p on a.ProductId=p.productid ";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		sql2 = "update a set a.baramount= BAmt, a.minbalance=(case when BAmt>0 then 0 else minbalance end) From "
				+ tmpTableName + " a inner join "
				+ "(select Sum(a.BarAmount) BAmt,a.AccNumber FROM AccBar A inner join " + tmpTableName
				+ " acc on a.AccNumber=acc.AccNumber group by a.AccNumber) b " + "on a.AccNumber=b.AccNumber";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		sql2 = "update a set a.odlimit= t.ODLimit,a.todlimit=t.todlimit, a.minbalance=(case when t.ODLimit>0 or t.todlimit>0 then 0 else minbalance end) "
				+ "From " + tmpTableName + " a inner join "
				+ "(select sum(a.sanctionamountbr) as ODLimit, (case when b.todexpdate >= ? then b.todlimit else 0 end) as "
				+ "todlimit, "
				+ "a.AccNumber from aodf a inner join currentaccount b on a.accnumber = b.accnumber inner join "
				+ tmpTableName + " c "
				+ "on b.AccNumber = c.AccNumber where a.status in ('1') and a.expdate >= ? and a.batchno <> 0 "
				+ "and a.odtype <> 0 and a.odtype <> 100 group by todexpdate, b.todlimit,a.AccNumber) t on a.AccNumber=t.AccNumber";
		ps = conn.prepareStatement(sql2);
		ps.setString(1, l_TodayDate);
		ps.setString(2, l_TodayDate);
		ps.executeUpdate();

		sql2 = "update a set a.odlimit= t.ODLimit,a.todlimit=t.todlimit, a.minbalance=(case when t.ODLimit>0 or t.todlimit>0 then 0 else minbalance end)"
				+ " From " + tmpTableName + " a inner join "
				+ "(select sum(a.sanctionamountbr) as ODLimit, (case when CONVERT(datetime , b.todexpdate) >= ? "
				+ "then b.todlimit else 0 end) as todlimit, "
				+ "a.AccNumber from aodf a inner join currentaccount b on a.accnumber = b.accnumber " + "inner join "
				+ tmpTableName + " c on b.AccNumber = c.AccNumber where a.status in ('1') "
				+ "and CONVERT(datetime , a.expdate) >= ? and " + "(a.batchno = 0 or a.odtype in (0,100)) "
				+ "group by todexpdate, b.todlimit, a.AccNumber) t on a.AccNumber=t.AccNumber";
		ps = conn.prepareStatement(sql2);
		ps.setString(1, l_TodayDate);
		ps.setString(2, l_TodayDate);
		ps.executeUpdate();

		if (SharedLogic.getSystemData().getBarminbalSetting() == 1)
			sql2 = "Update " + tmpTableName + " set avabalance=CurrentBalance+odlimit+todlimit-baramount-minbalance where baramount > 0 ";
		else
			sql2 = "Update " + tmpTableName + " set avabalance=CurrentBalance+odlimit+todlimit-baramount where baramount > 0 ";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();
		
		
		sql2 = "Update " + tmpTableName + " set avabalance=CurrentBalance+odlimit+todlimit where baramount <= 0 and status = 2";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();
			
		sql2 = "Update " + tmpTableName + " set avabalance=CurrentBalance+odlimit+todlimit-minbalance where baramount <= 0 and status <> 2";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		sql2 = "Update " + tmpTableName
				+ " set productname='OD Account' where currentbalance<0 and productname like '%current%'";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		if (!searchtype.equalsIgnoreCase("LOAN")) {
			sql2 = "delete from "+tmpTableName +" where CurrentBalance<0";
			ps = conn.prepareStatement(sql2);
			ps.executeUpdate();
		}

		sql2 = "Update a set a.branchname=b.branchname from "+tmpTableName+" a inner join BankDatabases b on a.BranchCode=b.BranchCode";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		sql2 = "Update a set a.statusname=(case when a.Status=0 and a.LastUpdate='19000101' then 'New' else case when a.Status=0 "
				+ "and a.LastUpdate<>'19000101' then 'Active' else b.Description end end) From "+tmpTableName +" a inner join RefAccountStatus b on a.Status=b.StatusCode";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		sql2 = "Update a set a.cusname=(case when b.Title='' then b.name else (b.title+' '+b.Name) end) From "+tmpTableName+" a "
				+ "inner join Customer b on a.customerid=b.customerid";
		ps = conn.prepareStatement(sql2);
		ps.executeUpdate();

		String query = "select accnumber,t4,avabalance,acctype,currentbalance,cusname,statusname,openingdate,branchname,productname,t2,branchcode from "
				+ tmpTableName + " order by t2";
		ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			AccountInfo aData = new AccountInfo();
			aData.setAccnumber(rs.getString("accnumber"));
			aData.setCurcode(rs.getString("t4"));
			aData.setAvailablebalance(rs.getDouble("avabalance"));
			aData.setAcctype(rs.getString("acctype"));
			aData.setCurrentbalance(rs.getDouble("currentbalance"));
			aData.setAccname(rs.getString("cusname"));
			aData.setAccstatus(rs.getString("statusname"));
			aData.setOpeningdate(rs.getString("openingdate"));
			aData.setBrname(rs.getString("branchname"));
			aData.setProductname(rs.getString("productname"));
			aData.setProducttype(rs.getString("t2"));
			aData.setBrcode(rs.getString("branchcode"));
			ret.add(aData);
			isData = true;
		}

		if (ret.size() > 0) {
			accarr = new AccountInfo[ret.size()];
			for (int i = 0; i < ret.size(); i++) {
				accarr[i] = ret.get(i);
			}
		}
		if (isData == true) {
			cusret.setCode("300");
			cusret.setDesc("Successfully");
			cusret.setAccountInfoArr(accarr);
		} else {
			cusret.setCode("210");
			cusret.setDesc("There is no data.");
			cusret.setAccountInfoArr(accarr);
		}
		ps.close();
		rs.close();
		return cusret;
	}	
	
	public CutOffDataResult getcutoff() {
		CutOffDataResult res =new CutOffDataResult();
		if(CutOffTimeData.readDate.equals(null) || CutOffTimeData.readDate.equals("")) {
			res.setRetcode("210");
			res.setRetmessage("There is no cut off time");
			res.setCutofftime("23:59");
		}else {
			res.setRetcode("300");
			res.setRetmessage("Successfully");
			res.setCutofftime(CutOffTimeData.readDate);
		}
		return res;
	}
	
	
	public  void insertSysLog(String token,String request_body,int log_type,String service_name,String function_name, Connection conn) throws SQLException{
		TokenResult res = new TokenResult();
		String query = "INSERT INTO requestlog(syskey,createddate,token,request_body,log_type,service_name,function_name) "
					+ "VALUES(?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setLong(1,GeneralUtility.generateSyskey());
		pstmt.setString(2,GeneralUtility.getDateTimeYYYYDDMMmmss());
		pstmt.setString(3,token);
		pstmt.setString(4, request_body);
		pstmt.setInt(5, log_type);
		pstmt.setString(6, service_name);
		pstmt.setString(7, function_name);
		if (pstmt.executeUpdate() > 0) {				
			res.setState(true);
			res.setMsgCode(Constant.successcode);
			res.setMsgDesc("Insert SysLog Successfully.");
		} else {
			res.setState(false);
			res.setMsgCode(Constant.errorcode);
			res.setMsgDesc("Insert Syslog Failed.");
		}
		
	}
	
}
