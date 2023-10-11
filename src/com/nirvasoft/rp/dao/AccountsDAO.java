package com.nirvasoft.rp.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.core.SingletonServer;
import com.nirvasoft.rp.core.ccbs.LogicMgr;
import com.nirvasoft.rp.core.ccbs.dao.HPRegisterDAO;
import com.nirvasoft.rp.mgr.DBAccountMgr;
import com.nirvasoft.rp.mgr.DBFECCurrencyRateMgr;
import com.nirvasoft.rp.shared.AccountGLTransactionData;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.DataResult;
import com.nirvasoft.rp.shared.LinkDatabasesCriteriaData;
import com.nirvasoft.rp.shared.LoanAccountData;
import com.nirvasoft.rp.shared.ReferenceData;
import com.nirvasoft.rp.shared.SignatureData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.shared.icbs.FixedDepositAccountData;
import com.nirvasoft.rp.shared.SharedLogic;
import com.nirvasoft.rp.util.SharedUtil;
import com.nirvasoft.rp.util.StrUtil;

public class AccountsDAO {

	private AccountData AccBean;
	private ArrayList<AccountData> lstAccBean;
	private ReferenceData AccStatusBean;
	private ArrayList<ReferenceData> lstAccStatusBean;
	private String mDBName = "";
	
	public String getDBName() {
		return mDBName;
	}
	public void setAccountsBean(AccountData aAccBean) {
		AccBean = aAccBean;
	}
	public AccountData getAccountsBean() {
		return AccBean;
	}
	
	public void setDBName(String mDBName) {
		this.mDBName = mDBName;
	}
	
	ArrayList<SignatureData> lstSignData = new ArrayList<SignatureData>();
	
	public ArrayList<SignatureData> getLstSignData() {
		return lstSignData;
	}
	public void setLstSignData(ArrayList<SignatureData> lstSignData) {
		this.lstSignData = lstSignData;
	}
	
	public AccountsDAO()
	{
		AccBean = new AccountData();
		lstAccBean = new ArrayList<AccountData>();

		//TXL Start
		AccStatusBean = new ReferenceData();
		lstAccStatusBean = new ArrayList<ReferenceData>();
		//TXL End
		LinkDatabasesDAO l_LinkDatabasesDAO = new LinkDatabasesDAO();
		LinkDatabasesCriteriaData l_LinkCriData = new LinkDatabasesCriteriaData();
		l_LinkCriData.TypeNo = 4;
//		setDBName(l_LinkDatabasesDAO.getLinkDataBase(l_LinkCriData).getDataSource());
	}
	
	public AccountsDAO(Connection pConn)
	{
		AccBean = new AccountData();
		lstAccBean = new ArrayList<AccountData>();

		//TXL Start
		AccStatusBean = new ReferenceData();
		lstAccStatusBean = new ArrayList<ReferenceData>();
		//TXL End
		LinkDatabasesDAO l_LinkDatabasesDAO = new LinkDatabasesDAO();
		LinkDatabasesCriteriaData l_LinkCriData = new LinkDatabasesCriteriaData();
		l_LinkCriData.TypeNo = 4;
		try {
			setDBName(l_LinkDatabasesDAO.getLinkDataBase(l_LinkCriData, pConn).getDataSource());
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public boolean getAccount(String aAccNumber, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
		boolean result=false;

		PreparedStatement pstmt=conn.prepareStatement("SELECT AccNumber, CurrencyCode, OpeningBalance, OpeningDate, CurrentBalance, ClosingDate, " +
				" LastUpdate, Status, LastTransDate, DrawType, AccName, AccNRC, SAccNo, Remark FROM Accounts WHERE AccNumber=?");

		pstmt.setString(1, aAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());   
		ResultSet rs=pstmt.executeQuery();

		while(rs.next()){
			AccBean = new AccountData();
			readRecord(AccBean, rs, "single", conn);
			
			String fkey = "smDeposit"+"_"+AccBean.getCurrencyCode()+"_"+AccBean.getBranchCode();
			
			AccBean.setCurrencyRate(SharedLogic.getSystemData().getpFECCurrencyRateList().get(fkey));
			
			//AccBean.setCurrencyRate(DBFECCurrencyRateMgr.getFECCurrencyRate(AccBean.getCurrencyCode(), "smDeposit", AccBean.getBranchCode(), conn));
			result=true;
		}
		pstmt.close();

		return result;
	}
	
	private void readRecord(AccountData aAccBean,ResultSet aRS, String pFrom, Connection pConn) throws SQLException
	{
		AccountExtendedDAO l_AccExtDAO = new AccountExtendedDAO();
		
		aAccBean.setAccountNumber(aRS.getString("AccNumber"));
		aAccBean.setCurrencyCode(aRS.getString("CurrencyCode"));
		aAccBean.setOpeningBalance(StrUtil.round2decimals(aRS.getDouble("OpeningBalance")));
		aAccBean.setOpeningDate(SharedUtil.formatDBDate2MIT(aRS.getString("OpeningDate")));
		aAccBean.setCurrentBalance(StrUtil.round2decimals(aRS.getDouble("CurrentBalance")));
		aAccBean.setClosingDate(SharedUtil.formatDBDate2MIT(aRS.getString("ClosingDate")));
		aAccBean.setLastUpdate(SharedUtil.formatDBDate2MIT(aRS.getString("LastUpdate")));
		aAccBean.setStatus(aRS.getByte("Status"));
		aAccBean.setLastTransDate(SharedUtil.formatDBDate2MIT(aRS.getString("LastTransDate")));
		aAccBean.setDrawingType(aRS.getString("DrawType"));
		aAccBean.setAccountName(aRS.getString("AccName"));
		aAccBean.setAccountID(aRS.getString("AccNRC"));
		aAccBean.setShortAccountCode(aRS.getString("SAccNo"));
		aAccBean.setRemark(aRS.getString("Remark"));

		if (pFrom.equalsIgnoreCase("browser")) {
			aAccBean.setAccountDescription(aRS.getString("Name"));
			aAccBean.setZoneCode(aRS.getInt("ZONECODE"));
			aAccBean.setProduct(aRS.getString("PRODUCT"));
			aAccBean.setType(aRS.getString("TYPE"));
			aAccBean.setBranchCode(aRS.getString("BRANCHCODE"));
			aAccBean.setProductGL(aRS.getString("PRODUCTGL"));
			aAccBean.setCashInHandGL(aRS.getString("CASHINHANDGL"));
		} else {
			aAccBean = l_AccExtDAO.getData(aAccBean, pConn);
		}
		
		ProductsDAO proDAO=new ProductsDAO();
		
		double minBal=0;
		if (SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN1()==1)
			minBal = proDAO.getProduct(aAccBean.getProduct() + aAccBean.getType(), pConn).getMinBalance();
		else
			minBal = proDAO.getProduct(aAccBean.getProduct(), pConn).getMinBalance();
		
		aAccBean.setMinimumBalance(minBal);

		if(SharedLogic.isCurrent(aAccBean.getProduct())){
			AODFDAO l_AODFDAO = new AODFDAO();
			aAccBean.setHasSystemOD(l_AODFDAO.isActiveBatchNo(aRS.getString("AccNumber"), "0", pConn));
			if (!aAccBean.isHasSystemOD()) {
				aAccBean.setHasSystemOD(l_AODFDAO.isActiveTODBatchNo(aRS.getString("AccNumber"), "100", pConn));
			}
			if(aAccBean.isHasSystemOD()){
				Short pBusinessClass = l_AODFDAO.getODData().getBusinessClass();
				aAccBean.setBusinessClass(pBusinessClass);
			}
			LoanAccountDAO l_LoanAccountDAO = new LoanAccountDAO();
			aAccBean.setLoanBackAccount(l_LoanAccountDAO.isUseForLoanAccount(aRS.getString("AccNumber"), pConn));
		}else if( SharedLogic.isLoan(aAccBean.getProduct())){
			LoanAccountData l_LoanAccountData = new LoanAccountData();
			LoanAccountDAO l_LoanAccountDAO = new LoanAccountDAO();
			if(l_LoanAccountDAO.getActiveLoanAccount(aRS.getString("AccNumber"), pConn)){
				l_LoanAccountData = l_LoanAccountDAO.getLoanAccBean();
			}
			if(!l_LoanAccountData.getAccLinked().trim().equals("")){
				AODFDAO l_AODFDAO = new AODFDAO();
				aAccBean.setHasSystemOD(l_AODFDAO.isActiveBatchNo(l_LoanAccountData.getAccLinked(), "0", pConn));
				
				if (!aAccBean.isHasSystemOD()) {
					aAccBean.setHasSystemOD(l_AODFDAO.isActiveTODBatchNo(l_LoanAccountData.getAccLinked(), "100", pConn));
				}
			}
		}
	}
	
	
	
	public  DataResult updateBalance(String acc, Connection conn ) throws SQLException{
		DataResult ret= new DataResult();

		PreparedStatement pstmt=null;		
		pstmt = conn.prepareStatement("UPDATE Accounts SET CurrentBalance = CurrentBalance " +
				"WHERE AccNumber=?");
		pstmt.setString(1, acc);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		pstmt.executeUpdate();
		pstmt.close();
		ret.setStatus(true);		
		return ret;
	}
	
	public  DataResult updateAccounts_Balance(String acc, Connection conn ) throws SQLException{
		DataResult ret= new DataResult();

		PreparedStatement pstmt=null;
		
		pstmt = conn.prepareStatement("UPDATE Accounts_Balance SET " +
				"CurrentBalance = CurrentBalance " +
				"WHERE AccNumber=?");
		pstmt.setString(1, acc);
		pstmt.executeUpdate();
		pstmt.close();
		ret.setStatus(true);		
		return ret;
	}
	
	public AccountData getODAvailableBalance(String pDate, String pAccNumber, int pProcessType, Connection pConn) {
		AccountData l_AccData = new AccountData();
		try {
			String l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
					+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
					+ "inner join currentaccount b on a.accnumber = b.accnumber "
					+ "inner join accounts c on  a.accnumber = c.accnumber "
					+ "where a.status in ('1') ";
			if(!(pProcessType==1  || pProcessType==4  || pProcessType== 8))
				l_Query += 	"and a.expdate >= ? ";
					//+ "and a.sanctiondatebr <= ? "
			l_Query +=	"and a.batchno <> 0 "
					+ "and a.odtype <> 0 "
					+ "and a.odtype <> 100 "
					+ "and a.accnumber = ? "
					+ "group by todexpdate, todlimit, currentbalance ";

			PreparedStatement pstmt = pConn.prepareStatement(l_Query);
			int i =1;
			pstmt.setString(i++, pDate);
			if(!(pProcessType==1  || pProcessType==4  || pProcessType== 8))
				pstmt.setString(i++, pDate);
			pstmt.setString(i++, pAccNumber);

			ResultSet l_RS = pstmt.executeQuery();

			while (l_RS.next()) {
				l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
				l_AccData.setOdLimit(l_RS.getDouble("ODLimit"));
				l_AccData.setTODLimit(l_RS.getDouble("TODLimit"));

			}

			if(pProcessType == 4 || pProcessType == 8){
				l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
						+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
						+ "inner join currentaccount b on a.accnumber = b.accnumber "
						+ "inner join accounts c on  a.accnumber = c.accnumber "
						+ "where a.status in ('1') "
						+ "and (a.batchno = 0 or a.odtype in (0,100)) "
						+ "and a.accnumber = ? "
						+ "group by todexpdate, todlimit, currentbalance ";

				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pDate);
				pstmt.setString(2, pAccNumber);

				l_RS = pstmt.executeQuery();

				while (l_RS.next()) {				
					l_AccData.setOdLimit(l_AccData.getOdLimit() + l_RS.getDouble("ODLimit"));
					l_AccData.setTODLimit(l_AccData.getTODLimit() + l_RS.getDouble("TODLimit"));				
				}
			}

			l_Query = "select currentbalance from accounts where accnumber = ? ";
			pstmt = pConn.prepareStatement(l_Query);
			pstmt.setString(1, pAccNumber);
			l_RS = pstmt.executeQuery();			
			while (l_RS.next()) {
				l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
			}
			if(pProcessType != 4 || pProcessType != 8){
				l_Query = "Select ExpDate from AODF where BatchNo <> 0 and BatchNo = (select Max(BatchNo) from AODF where AccNumber = ? and odtype<>0 and odtype<>100) and AccNumber = ? And Status = 1 and ExpDate < ? ";
				pstmt = pConn.prepareStatement(l_Query);
				pstmt.setString(1, pAccNumber);
				pstmt.setString(2, pAccNumber);
				pstmt.setString(3, pDate);
				l_RS = pstmt.executeQuery();
				while (l_RS.next()) {
					l_AccData.setExpire(true);
				}
			}
			pstmt.close();

		} catch (Exception e) {			
			e.printStackTrace();
		}
		return l_AccData;
	}
	
	public double getAvaliableBalance(String aAccNumber, int pProcessType, Connection conn) throws  IOException,ClassNotFoundException,SQLException, ParserConfigurationException, SAXException{

		double lCurrentBal=0;
		int lAccStatus = 0 ;
		double lEarMarkedAmountBal=0;
		double lRetAmount=0;
		double lMinAmount=0;
		String lPrdCode=null;
		AccountData l_AccData = new AccountData();
		int l_MinBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN1();
		int l_CurBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN2();
		int l_BarMinBalSetting = SharedLogic.getSystemData().getpSystemSettingDataList().get("ACTMINBAL").getN3();
		PreparedStatement pstmt=null;
		
		if(getAccount(aAccNumber, conn)) {
			l_AccData = getAccountsBean();
		}
		lCurrentBal = l_AccData.getCurrentBalance();
		lAccStatus = l_AccData.getStatus() ;

		pstmt=conn.prepareStatement("SELECT ISNULL(Sum(BarAmount),0) AS EarMarkedAmount  FROM AccBar WHERE AccNumber=?");

		pstmt.setString(1, aAccNumber);
		ResultSet rsBar=pstmt.executeQuery();

		while(rsBar.next()){
			lEarMarkedAmountBal = rsBar.getDouble("EarMarkedAmount");
		}    

		pstmt.close();

		lPrdCode= l_AccData.getProduct();
		if(l_MinBalSetting == 1) {
			lPrdCode = l_AccData.getProduct()+ l_AccData.getType();
		}

		int curId = 0;
		if(l_CurBalSetting ==1 ) {
			for(int i = 0;i<SharedLogic.getCurrencyCodes().size();i++){
				if(SharedLogic.getCurrencyCodes().get(i).getCode().equals(l_AccData.getCurrencyCode()) && !l_AccData.getCurrencyCode().equals("MMK")){
					curId = SharedLogic.getCurrencyCodes().get(i).getKey();break;
				}
			}
			lPrdCode = lPrdCode+((curId==0?"":curId));
		}
		if(pProcessType!=4) {
			pstmt=conn.prepareStatement("SELECT ProductId,ProductType,MinBalance  FROM ProductSetup WHERE ProductId=?");
	
			pstmt.setString(1, lPrdCode);
			pstmt.setQueryTimeout(DAOManager.getNormalTime());
			ResultSet rsMin=pstmt.executeQuery();
	
			while(rsMin.next()){
				lMinAmount = rsMin.getDouble("MinBalance");
			}    
		}
	
		pstmt.close();

		if(lEarMarkedAmountBal>0) {
			if(l_BarMinBalSetting==1)
				lRetAmount=lCurrentBal-lEarMarkedAmountBal-lMinAmount;
			else
				lRetAmount=lCurrentBal-lEarMarkedAmountBal;
		}
		else {
			if(lAccStatus == 2)
				lRetAmount=lCurrentBal ;
			else
				lRetAmount=lCurrentBal-lMinAmount;
		}		
		return lRetAmount;
	}
	
	public AccountData getODAvailableBalance(String pDate, String pAccNumber, Connection pConn) throws SQLException {
		AccountData l_AccData = new AccountData();
		
		String l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
				+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
				+ "inner join currentaccount b on a.accnumber = b.accnumber "
				+ "inner join accounts c on  a.accnumber = c.accnumber "
				+ "where a.status in ('1') "
				+ "and a.expdate >= ? "
				+ "and a.batchno <> 0 "
				+ "and a.odtype <> 0 "
				+ "and a.odtype <> 100 "
				+ "and a.accnumber = ? "
				+ "group by todexpdate, todlimit, currentbalance ";

		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pDate);
		pstmt.setString(2, pDate);
		pstmt.setString(3, pAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet l_RS = pstmt.executeQuery();

		while (l_RS.next()) {
			l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
			l_AccData.setOdLimit(l_RS.getDouble("ODLimit"));
			l_AccData.setTODLimit(l_RS.getDouble("TODLimit"));

		}

		l_Query = "select sum(a.sanctionamountbr) as ODLimit, " 
				+ "(case when b.todexpdate >= ? then b.todlimit else 0 end  ) as todlimit,  c.currentbalance from aodf a "
				+ "inner join currentaccount b on a.accnumber = b.accnumber "
				+ "inner join accounts c on  a.accnumber = c.accnumber "
				+ "where a.status in ('1') "
				+ "and a.expdate >= ? "
				+ "and (a.batchno = 0 or a.odtype in (0,100)) "
				+ "and a.accnumber = ? "
				+ "group by todexpdate, todlimit, currentbalance ";

		pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pDate);
		pstmt.setString(2, pDate);
		pstmt.setString(3, pAccNumber);

		l_RS = pstmt.executeQuery();

		while (l_RS.next()) {				
			l_AccData.setOdLimit(l_AccData.getOdLimit() + l_RS.getDouble("ODLimit"));
			l_AccData.setTODLimit(l_AccData.getTODLimit() + l_RS.getDouble("TODLimit"));				
		}

		l_Query = "select currentbalance from accounts where accnumber = ? ";
		pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNumber);
		l_RS = pstmt.executeQuery();			
		while (l_RS.next()) {
			l_AccData.setCurrentBalance(l_RS.getDouble("CurrentBalance"));
		}
		pstmt.close();		
		return l_AccData;
	}
	
	public double getAvaliableBalanceForCreditCard(String aAccNumber, String today,String pProductCode, Connection conn) throws  IOException,ClassNotFoundException,SQLException, ParserConfigurationException, SAXException{
		 CurrentAccountDAO lCurrentDAO = new CurrentAccountDAO();
		 double lCurrentBal=0;
		 double BarAmount=0;
		 double lRetAmount=0;
		 double lLimitAmount = 0;
	      
		PreparedStatement pstmt=null;
	
		lCurrentBal = this.getCurrentBalance(aAccNumber, conn);		
  
      pstmt=conn.prepareStatement("SELECT ISNULL(Sum(BarAmount),0) AS BarAmount  FROM AccBar WHERE AccNumber=?");
     
      pstmt.setString(1, aAccNumber);
      pstmt.setQueryTimeout(DAOManager.getNormalTime());
      ResultSet rsBar=pstmt.executeQuery();

      while(rsBar.next()){
      	BarAmount = rsBar.getDouble("BarAmount");
      }
      pstmt.close(); 
      
      lLimitAmount = lCurrentDAO.getLimitAmount(aAccNumber, today, conn);
     if(lCurrentBal >= 0){
      	lRetAmount = lCurrentBal + lLimitAmount - BarAmount;
      }  else {
      	lRetAmount = lLimitAmount - BarAmount;
      }
	    return lRetAmount;
	}
	
	public double getCurrentBalance(String aAccNumber, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
		double result=0;

		PreparedStatement pstmt=conn.prepareStatement("SELECT CurrentBalance FROM Accounts WHERE AccNumber=?");
		pstmt.setString(1, aAccNumber);
		ResultSet rs=pstmt.executeQuery();

		while(rs.next()){
			result = rs.getDouble(1);
		}
		pstmt.close();
		return result;
	}
	
	public ArrayList<ReferenceData> getAccountStatus(Connection conn) throws SQLException{			

		StringBuffer  l_query = new StringBuffer("");
		l_query.append("SELECT STATUSCODE, DESCRIPTION FROM REFACCOUNTSTATUS WHERE 1=1 ");		
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());

		ResultSet rs=pstmt.executeQuery();
		int i=0;
		while(rs.next())
		{
			ReferenceData l_AccStatusBean = new ReferenceData();
			l_AccStatusBean.setKey(i);
			l_AccStatusBean.setCode(rs.getString("StatusCode"));
			l_AccStatusBean.setDescription(rs.getString("Description"));
			lstAccStatusBean.add(i++, l_AccStatusBean);
		}
		pstmt.close();				
		return lstAccStatusBean;
	}
	
	public static String getCurrentDateYYYYMMDDHHMMSS() {
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}
	
	public static DataResult prepareMultiTransfer(ArrayList<TransactionData> arl, ArrayList<AccountGLTransactionData> aAccGLTrList,int lTranRef,
			ArrayList<Integer> m_AutoLinkTransRef, Connection conn) throws SQLException, ClassNotFoundException, IOException, ParserConfigurationException, SAXException  {
		DataResult ret= new DataResult();
		String tmpTable ="TmpUpdateBalance_" + getCurrentDateYYYYMMDDHHMMSS();
		
		
		String query = "Create Table "+tmpTable
				+" (indx int default(0),AccNumber varchar(20),TrAmount float default(0),TransactionType int , "
				+ " TransactionDate datetime default('19000101'), EffectiveDate datetime default('19000101'), TrCurCode varchar(20),"
				+ " CurrentBalance float default(0),AvailableBal float default(0), Status int default(0), isLoan int default(0))";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		query = "insert into "+tmpTable+" values(?,?,?,?,?,?,?,0,0,0,0)";
		pstmt = conn.prepareStatement(query);
		for (int i=0; i< arl.size(); i++){
			if(!arl.get(i).isGl()){
				int index = 1;
				pstmt.setInt(index++, i);
				pstmt.setString(index++, arl.get(i).getAccountNumber());
				pstmt.setDouble(index++, aAccGLTrList.get(i).getTrAmount());
				pstmt.setInt(index++, arl.get(i).getTransactionType());
				pstmt.setString(index++, arl.get(i).getTransactionDate());
				pstmt.setString(index++, arl.get(i).getEffectiveDate());
				pstmt.setString(index++, aAccGLTrList.get(i).getTrCurCode());
				pstmt.executeUpdate();
			}
		}

		query = "UPDATE Accounts SET CurrentBalance = CurrentBalance " +
				"WHERE AccNumber in (select AccNumber from "+tmpTable + ")";
		pstmt = conn.prepareStatement(query);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		pstmt.executeUpdate();

		query = "update A SET A.CurrentBalance = B.CurrentBalance , A.Status = B.Status from "+tmpTable+" A, Accounts B where A.AccNumber=B.AccNumber ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();

		
		query = "update A SET A.AvailableBal = B.Limit, A.isLoan=1 from "+tmpTable+" A inner join LoanAccount B on A.AccNumber=B.AccNumber inner join T00005 T on B.AccNumber=T.t1 ";
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();

		query = "select * from " + tmpTable ;
		pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			ret.setStatus(true);
			int index = rs.getInt("indx");
			String acc = rs.getString("AccNumber");
			int isloan = rs.getInt("isLoan");
			int transType = rs.getInt("TransactionType");
			int status = rs.getInt("Status");
			double amt = rs.getDouble("TrAmount");
			double currentBalance = rs.getDouble("CurrentBalance");
			
			double l_AvailableBal = 0;
			if(isloan != 1){
				l_AvailableBal = DBAccountMgr.getAvailableBalance(acc, 0, conn);
			}
			if (SharedLogic.isDebit(transType)){
				// Check For A/C Status Closed Pending
				if (status != 2) {
					if (l_AvailableBal< amt){
						ret.setStatus(false);
						ret.setDescription(acc + " insufficient balance " + StrUtil.formatNumberwithComma((amt - l_AvailableBal)));
					}
				} else {
					if (currentBalance != amt){
						ret.setStatus(false);
						ret.setDescription(acc + " insufficient balance " + StrUtil.formatNumberwithComma((amt - l_AvailableBal)));
					}
				}

			} 
			if(ret.getStatus()){
					if (arl.get(index).getCurrencyCode().equals(aAccGLTrList.get(index).getTrCurCode())){
						arl.get(index).setPreviousBalance(StrUtil.round2decimals(currentBalance ,RoundingMode.HALF_UP));
					}else{ 
						arl.get(index).setPreviousBalance(StrUtil.round2decimals(currentBalance * aAccGLTrList.get(index).getTrRate(),RoundingMode.HALF_UP));
					}
					aAccGLTrList.get(index).setTrPrevBalance(StrUtil.round2decimals(currentBalance ,RoundingMode.HALF_UP));
					ret.setStatus(true);
			}else{
				break;
			}
		}
		
		if (ret.getStatus()) {
			query = "UPDATE A SET " +
					"A.CurrentBalance = case when B.TransactionType >= 500  then CONVERT(numeric(18,2),(A.CurrentBalance - B.TrAmount )) "
					+ " else CONVERT(numeric(18,2),(A.CurrentBalance + B.TrAmount )) end , A.LastTransDate=B.TransactionDate, A.LastUpdate=B.TransactionDate" +
					" from Accounts A,"+tmpTable+" B WHERE A.AccNumber = B.AccNumber ";	
			pstmt = conn.prepareStatement(query);			
			if(pstmt.executeUpdate()>0)
				ret.setStatus(true);
			pstmt.close();
		}

		query = "DROP TABLE IF EXISTS "+tmpTable;
		pstmt = conn.prepareStatement(query);
		pstmt.executeUpdate();
		
		////////////////// add transaction
		AccountTransactionDAO lAccTranDAO=new AccountTransactionDAO();
		ArrayList<AccountGLTransactionData> l_AccountGLTransactionList=new ArrayList<AccountGLTransactionData>();
		//For Voucher Serial
		String l_VoucherSerialNo = "";
		boolean lRlt =false;
		int lUncommTransNo=0;
		// Adding Account Transaction
		if (ret.getStatus()){
			
			for(int i = 0; i<arl.size(); i++){
				ret = lAccTranDAO.addAccTransaction(arl.get(i), conn);			//HNNS Fixed					
				if (ret.getStatus()){
					lRlt=true;
				}else{
					ret.setStatus(false);
					if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
						ret.setDescription("Please Try Again!!");
					}
					break;
				}
				
				if (ret.getStatus()){
					if (i==0){
						lUncommTransNo = (int) ret.getTransactionNumber();
					}
							
					if (lAccTranDAO.updateTransRef(lTranRef,lUncommTransNo,conn) && lRlt){
						ret.setStatus(true);
						ret.setTransactionNumber(lUncommTransNo);
						
					} else {
						ret.setStatus(false);
						if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
							ret.setDescription("Please Try Again!!");
						}
						break;
					}	
				}
			}
			
		} else {
			if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
				ret.setDescription("Please Try Again!!");
			}
		}
		
		//For Auto Link TransRef
		if (ret.getStatus() && m_AutoLinkTransRef.size()>0){
			for (int i=0;i<m_AutoLinkTransRef.size();i++){
				if (lAccTranDAO.addAutoLinkTransRef(lUncommTransNo,(int) m_AutoLinkTransRef.get(i), conn)){
					ret.setStatus(true);
					
				} else {
					ret.setStatus(false);
					if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
						ret.setDescription("Please Try Again!!");break;
					}
				}
			}
		}
		
		//SSH For Add in AccountGLTransaction
		if(ret.getStatus()) {
	
			l_AccountGLTransactionList=aAccGLTrList;
			AccountGLTransactionDAO l_AccGLTrDAO = new AccountGLTransactionDAO();
			//check table exist or not.
				ArrayList<TransactionData> l_TrDataList3 = new ArrayList<TransactionData>();
				if( lAccTranDAO.getAccTransactionsTransRef((int)ret.getTransactionNumber(), conn))
				{
					l_TrDataList3 = lAccTranDAO.getTransactionDataList();
				}
				
				int j = 0;
				if(l_TrDataList3.size() == 0){
					ret.setStatus(false);
					if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
						ret.setDescription("Please Try Again!!");
					}
				}else{
					for (AccountGLTransactionData accountGLTransactionData : l_AccountGLTransactionList) {				
						accountGLTransactionData.setTransNo(l_TrDataList3.get(j).getTransactionNumber());
						accountGLTransactionData.setT2(l_TrDataList3.get(j).getAuthorizerID());
						ret.setStatus(l_AccGLTrDAO.addAccountGLTransaction(accountGLTransactionData, conn)); 
						j++;
					}
					if(ret.getStatus()) {
						ret.setVNo(l_VoucherSerialNo);
						ret.setCode(l_VoucherSerialNo);
						ret.setTransactionNumber(lUncommTransNo);
					}else{
						ret.setStatus(false);
						if(ret.getDescription().equals("") || ret.getDescription().equals(null)){
							ret.setDescription("Please Try Again!!");
						}
					}
				}
		} 			
		
		if(ret.getStatus()){				
			ret.setEffectiveDate(arl.get(0).getEffectiveDate());
		}
		
		return ret;
		
	}
	
	
	public DataResult updateAccountBalanceCurrency(String acc, double amt, int transtype,String lLastTransDate,String pEfffectiveDate, String pCurCode, Connection conn ) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		DataResult ret= new DataResult();
		AccountData l_AccData = new AccountData();
		AccountsDAO l_AccDAO = new AccountsDAO(conn);
		LoanAccountDAO l_LoanDAO = new LoanAccountDAO();
		LastDatesDAO l_LastDao = new LastDatesDAO();
		String l_TableName = "", l_TableName1 = "";

		if(SharedLogic.getSystemData().getpSystemSettingDataList().get("BNK").getN4()==1){
			int comparedate = StrUtil.compareDate(pEfffectiveDate, lLastTransDate);
			if(comparedate > 0) {
				if(l_LastDao.isRunnedEOD(lLastTransDate, conn)){
					l_TableName1 = "Accounts_Balance";						 
				}
				l_TableName = "Accounts";
			} else { 
				l_TableName = "Accounts";
				l_TableName1 = "Accounts_Balance";
			}
		} else {
			l_TableName = "Accounts";
			l_TableName1 = "";
		}

		//Making Lock Accounts
		updateBalance(acc, conn);
		
		if(!l_TableName1.equals(""))//Making Lock Accounts_Balances			
			updateAccounts_Balance(acc, conn);

		if (l_AccDAO.getAccount(acc, conn)) {
			l_AccData = l_AccDAO.getAccountsBean();
		}

		ret.setStatus(true);

		double l_CurrentBal = 0;
		double l_AvailableBal = 0;

		l_CurrentBal = l_AccData.getCurrentBalance();

		if(l_LoanDAO.getLoanAccount(acc, conn)){
			l_AvailableBal = l_LoanDAO.getLoanAccBean().getLimit();
		}else{
			l_AvailableBal = DBAccountMgr.getAvailableBalance(acc, 0, conn);
		}			

		String l_Query = "" , l_Query1= "";

		if (SharedLogic.isDebit(transtype)){
			// Check For A/C Status Closed Pending
			if (l_AccData.getStatus() != 2) {
				if (l_AvailableBal>=amt){

				}
				else
				{
					ret.setStatus(false);
					ret.setDescription(acc + " insufficient balance " + StrUtil.formatNumberwithComma((amt - l_AvailableBal)));
				}
			} else {
				if (l_CurrentBal == amt){

				}
				else
				{
					ret.setStatus(false);
					ret.setDescription(acc + " insufficient balance " + StrUtil.formatNumberwithComma((amt - l_AvailableBal)));
				}
			}
			if(ret.getStatus()){
				l_Query = "UPDATE "+l_TableName+" SET " +
						"CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), LastTransDate=?, LastUpdate=?" +
						" WHERE AccNumber = ? ";	
				if(!l_TableName1.equals("")) {
					l_Query1 = "UPDATE "+l_TableName1+" SET " +
							"CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance - ? )), LastTransDate=?, LastUpdate=?" +
							" WHERE AccNumber = ? ";	
				}	
			}

		} else {
			l_Query = "UPDATE "+l_TableName+" SET " +
					"CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), LastTransDate=?, LastUpdate=?" +
					" WHERE AccNumber = ? ";
			if(!l_TableName1.equals("")) {
				l_Query1 = "UPDATE "+l_TableName1+" SET " +
						"CurrentBalance = CONVERT(numeric(18,2),(CurrentBalance + ? )), LastTransDate=?, LastUpdate=?" +
						" WHERE AccNumber = ? ";
			}
		}
		if (ret.getStatus()) {
			amt = StrUtil.round2decimals(amt,RoundingMode.HALF_UP);
			l_CurrentBal = StrUtil.round2decimals(l_CurrentBal,RoundingMode.HALF_UP);
			ret.setBalance(l_CurrentBal);
			ret.setData(lLastTransDate);

			PreparedStatement pstmt=null, pstmt1 = null;
			
			pstmt = conn.prepareStatement(l_Query);					

			pstmt.setDouble(1,amt);
			pstmt.setString(2, lLastTransDate);
			pstmt.setString(3, lLastTransDate);
			pstmt.setString(4, acc);
			if(pstmt.executeUpdate()>0)
				ret.setStatus(true);
			pstmt.close();
			if(ret.getStatus()){
				if(!l_TableName1.equals("")) {
					pstmt1 = conn.prepareStatement(l_Query1);
					pstmt1.setDouble(1,amt);
					pstmt1.setString(2, lLastTransDate);
					pstmt1.setString(3, lLastTransDate);
					pstmt1.setString(4, acc);
					if(pstmt1.executeUpdate()>0)
						ret.setStatus(true);
					pstmt1.close();
				}
			}
		}
		return ret;
	}
	public boolean updateFixedAccountData(FixedDepositAccountData pfixedAccountData,Connection pconn) {
		boolean l_ret=false;
		try {
			String l_query="UPDATE PBFixedDepositAccount SET LastTransDate =?,Status =? WHERE AccNumber=? ";
			PreparedStatement l_pstmt=pconn.prepareStatement(l_query);
			int index = 1;
			l_pstmt.setString(index++, pfixedAccountData.getLastTransDate());
			l_pstmt.setByte(index++, pfixedAccountData.getStatus());
			l_pstmt.setString(index++,pfixedAccountData.getAccountNumber());
			//l_pstmt.setString(index++,pfixedAccountData.getRefNo());
			if(l_pstmt.executeUpdate()>0) {
				l_ret=true;
			}
			l_pstmt.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			l_ret=false;
		}
		return l_ret;
	}
	
	public boolean getAccountByShortCode(String aAccountShortCode, Connection conn) throws 
	ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
		boolean result = false;

		PreparedStatement pstmt = conn.prepareStatement("SELECT AccNumber, CurrencyCode, OpeningBalance, OpeningDate, CurrentBalance, ClosingDate," 
				+" LastUpdate, Status, LastTransDate, DrawType, AccName, AccNRC, SAccNo, Remark FROM Accounts A "
				+" WHERE lower(A.SAccNo) = ? ");

		pstmt.setString(1, aAccountShortCode.toLowerCase());
		ResultSet rs=pstmt.executeQuery();

		while(rs.next()){
			AccBean = new AccountData();
			readRecord(AccBean, rs, "single", conn);
			AccBean.setCurrencyRate(DBFECCurrencyRateMgr.getFECCurrencyRate(AccBean.getCurrencyCode(), "smDeposit", AccBean.getBranchCode(), conn));
			result = true;
		}
		rs.close();
		pstmt.close();

		return result;
	}
	
	public double getMiniBalance(String productID, Connection conn) throws SQLException {
		double miniBalance = 0;
		String query = " SELECT MinBalance FROM ProductSetup WHERE ProductId=? ";
		PreparedStatement pstmt = conn.prepareStatement(query);
		 pstmt.setString(1, productID);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			miniBalance = rs.getDouble("MinBalance");
        }
		
		return miniBalance;
	}
	
	public DataResult updateAccountBalanceReversal(String pAccNumber, double pCurrentBalance, double pAmount, 
			int pTransType, String pEffectiveDate, String pLastOldTransDate,String lLastTransDate,double minBalance,String PrevDate, Connection conn) throws SQLException, IOException, ClassNotFoundException, ParserConfigurationException, SAXException{
		DataResult ret= new DataResult();
		ret.setStatus(true);

		String l_Query = "",l_Query1="";
		double l_CurrentBal = 0;
		l_CurrentBal = pCurrentBalance;
		String pTableName = "",pTableName1="";
		LastDatesDAO l_LastDao = new LastDatesDAO();

		if(SharedLogic.getSystemSettingT12N4("BNK")==1){
			int comparedate = StrUtil.compareDate(pEffectiveDate, pLastOldTransDate);
			if(comparedate > 0) {
				if(l_LastDao.isRunnedEOD(pLastOldTransDate, conn)){
					pTableName1 = "Accounts_Balance";						 
				}
				pTableName = "Accounts";
			} else { 
				pTableName = "Accounts";
				pTableName1 = "Accounts_Balance";
			}		
		}else {
			pTableName = "Accounts";
			pTableName1 = "";
		}
		//Making Lock
		updateBalance(pAccNumber, conn);
		if(!pTableName1.equals("")) //Making Accounts_Balance Lock
			updateAccounts_Balance(pAccNumber, conn);

		if (LogicMgr.isDebit(pTransType)){
			l_Query = "UPDATE "+pTableName+" SET " +
					"CurrentBalance = CurrentBalance + ?, LastTransDate=?, LastUpdate=?" +
					" WHERE AccNumber=?";
			if(!pTableName1.equals("")) {
				l_Query1 = "UPDATE "+pTableName1+" SET " +
						"CurrentBalance = CurrentBalance + ?, LastTransDate=?, LastUpdate=?" +
						" WHERE AccNumber=? ";
			}
		} else {
			double amt = (checkAvaliableBalance(pAccNumber, conn)-pAmount);
			if(PrevDate.equals("19000101")){
				amt = StrUtil.roundUP(amt+ minBalance);
			}
			if(amt>=0){
				l_Query = "UPDATE "+pTableName+" SET " +
						"CurrentBalance = CurrentBalance - ?, LastTransDate=?, LastUpdate=?" +
						" WHERE AccNumber=? ";
				if(!pTableName1.equals("")) {
					l_Query1 = "UPDATE "+pTableName1+" SET " +
							"CurrentBalance = CurrentBalance - ?, LastTransDate=?, LastUpdate=?" +
							" WHERE AccNumber=? ";
				}
			}else{
				if(SharedLogic.getSystemSettingT12N2("MRHF")==1){
					l_Query = "UPDATE "+pTableName+" SET " +
							"CurrentBalance = CurrentBalance - ?, LastTransDate=?, LastUpdate=?" +
							" WHERE AccNumber=? ";
					if(!pTableName1.equals("")) {
						l_Query1 = "UPDATE "+pTableName1+" SET " +
								"CurrentBalance = CurrentBalance - ?, LastTransDate=?, LastUpdate=?" +
								" WHERE AccNumber=? ";
					}
				}else{
					 ret.setStatus(false);
					 ret.setDescription("Insufficient Balance: Transfer Amount and Min Balance!");
				}
			}
		}

		if (ret.getStatus()) {
			ret.setBalance(l_CurrentBal);
			ret.setData(pLastOldTransDate);

			PreparedStatement pstmt=null,pstmt1=null;

			pstmt = conn.prepareStatement(l_Query);

			pstmt.setDouble(1, pAmount);
			pstmt.setString(2, pLastOldTransDate);
			pstmt.setString(3, pLastOldTransDate);
			pstmt.setString(4, pAccNumber);
			if(pstmt.executeUpdate()>0)
				ret.setStatus(true);
			pstmt.close();

			if(ret.getStatus()) {
				if(!pTableName1.equals("")) {
					pstmt1 = conn.prepareStatement(l_Query1);

					pstmt1.setDouble(1, pAmount);
					pstmt1.setString(2, pLastOldTransDate);
					pstmt1.setString(3, pLastOldTransDate);
					pstmt1.setString(4, pAccNumber);
					if(pstmt1.executeUpdate()>0)
						ret.setStatus(true);
					pstmt1.close();
				}   
			}
		}
		return ret;
	}
	
	private double checkAvaliableBalance(String pAccNumber, Connection conn) throws SQLException, IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		// TODO Auto-generated method stub
		double l_AvailableBal = 0;
		LoanAccountDAO l_LoanDAO = new LoanAccountDAO();
		HPRegisterDAO l_HPDAO = new HPRegisterDAO();
		if(l_LoanDAO.getLoanAccount(pAccNumber, conn)){
			l_AvailableBal = l_LoanDAO.getLoanAccBean().getLimit();
			if(LogicMgr.getSystemSettingT12N4("HPExt")==1 && l_HPDAO.getHPAccount(pAccNumber, conn)){
				l_AvailableBal = l_HPDAO.getHPDataBean().getHPAmt();
			}
		}else{
			l_AvailableBal = DBAccountMgr.getAvailableBalance(pAccNumber, 0, conn);
		}	
		return l_AvailableBal;
	}
	
	 public boolean checkClosingWithdrawReversal(String pAccNo, int pTransRef,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
	    	boolean result=false;
	    	
	        PreparedStatement pstmt=conn.prepareStatement("select max(TransRef) TransRef from (" +
	        		"select TransRef Transref from AccountTransaction A inner join Accounts B on A.AccNumber=B.AccNumber " +
	        		"where A.AccNumber='"+pAccNo+"' and B.Status=3 " +
	        		"union all " +
	        		"select TransRef Transref from AccountTransactionOld A inner join Accounts B on A.AccNumber=B.AccNumber " +
	        		"where A.AccNumber='"+pAccNo+"' and B.Status=3) As A");
	        ResultSet rs=pstmt.executeQuery();
	               
	        while(rs.next()){
	        	int TransRef= rs.getInt("TransRef");
	        	if(TransRef == pTransRef){
	        		result=true;
	        	}else{
	        		result=false;
	        	}
	        }
	        pstmt.close();
	        return result;
	 }
	 
	 public DataResult updateAccountBalanceWithdrawReversal(String pAccNumber, String pEffectiveDate, String pLastOldTransDate,String lLastTransDate, Connection conn) throws SQLException{
			DataResult ret= new DataResult();
			ret.setStatus(true);

			String l_Query = "",l_Query1="";
			String pTableName = "",pTableName1="";
			LastDatesDAO l_LastDao = new LastDatesDAO();

			if(SharedLogic.getSystemSettingT12N4("BNK")==1){
				int comparedate = StrUtil.compareDate(pEffectiveDate, pLastOldTransDate);
				if(comparedate > 0) {
					if(l_LastDao.isRunnedEOD(pEffectiveDate, conn)){
						pTableName1 = "Accounts_Balance";						 
					}
					pTableName = "Accounts";
				} else { 
					pTableName = "Accounts";
					pTableName1 = "Accounts_Balance";
				}		
			}else {
				pTableName = "Accounts";
				pTableName1 = "";
			}
			l_Query = "UPDATE "+pTableName+" SET " +
					"Status = 0 , ClosingDate='99991231'" +
					" WHERE AccNumber=?";
			if(!pTableName1.equals("")) {
				l_Query1 = "UPDATE "+pTableName1+" SET " +
						"Status = 0 , ClosingDate='99991231'" +
						" WHERE AccNumber=?";
			}
			if (ret.getStatus()) {
				PreparedStatement pstmt=null,pstmt1=null;
				pstmt = conn.prepareStatement(l_Query);

				pstmt.setString(1, pAccNumber);
				if(pstmt.executeUpdate()>0)
					ret.setStatus(true);
				pstmt.close();

				if(ret.getStatus()) {
					if(!pTableName1.equals("")) {
						pstmt1 = conn.prepareStatement(l_Query1);

						pstmt1.setString(1, pAccNumber);
						if(pstmt1.executeUpdate()>0)
							ret.setStatus(true);
						pstmt1.close();
					}   
				}
			}
			return ret;
		}
	 
	 public boolean getPassBookNo(String productCode,String passbook, Connection pConn) {
		 boolean result = false;
		 int index =1;	
		 try {
			 String query ="SELECT AccNumber, PBNumber FROM PassBookHistory PH INNER JOIN T00005 T5 ON PH.AccNumber = T5.T1 AND T2 = ?" +
			 		" AND PBNumber = ?";
			 PreparedStatement pstmt = pConn.prepareStatement(query);
		     pstmt.setString(index++, productCode);
		     pstmt.setString(index++, passbook);
		     ResultSet rs = pstmt.executeQuery();
		     while(rs.next()){
		    	 AccountData accData = new AccountData();
		    	 accData.setAccountNumber(rs.getString("AccNumber"));
		    	 accData.setPassbook(rs.getString("PBNumber"));
		    	 result = true;
		     }
		} catch (SQLException e) {		
			e.printStackTrace();
		}
			return result;
		}
	 
	 public boolean addAccount(AccountData aAccBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result=false;			
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO Accounts(AccNumber, CurrencyCode,OpeningBalance," +
									"OpeningDate,ClosingDate,Status,DrawType,AccName,AccNRC,SAccNo,Remark,BranchCode,LastTransDate,LastUpdate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			updateRecord(aAccBean, pstmt,true);	
			if(pstmt.executeUpdate() > 0)
				result=true; 
			if(result) {
				if(SharedLogic.getSystemSettingT12N4("BNK")==1){
					if(result) {
						result=false;			
						pstmt=conn.prepareStatement("INSERT INTO Accounts_Balance(AccNumber, CurrencyCode,OpeningBalance," +
								"OpeningDate,ClosingDate,Status,DrawType,AccName,AccNRC,SAccNo,Remark,BranchCode,LastTransDate,LastUpdate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) " );
						updateRecord(aAccBean, pstmt,true);					
						if(pstmt.executeUpdate() > 0)
							result=true;
						pstmt.close();  
					}
				}
			}

			return result;
		}	
	 
	 private void updateRecord(AccountData aAccBean,PreparedStatement aPS,boolean aIsNewRecord) throws SQLException
		{
			
				int i = 1;
				aPS.setString(i++, aAccBean.getAccountNumber());
				aPS.setString(i++, aAccBean.getCurrencyCode());
				if (aIsNewRecord) 
					aPS.setDouble(i++, aAccBean.getOpeningBalance());
				aPS.setString(i++, aAccBean.getOpeningDate());
				/*if (aIsNewRecord) {
					aPS.setDouble(i++, aAccBean.getCurrentBalance());
				}*/
				aPS.setString(i++, aAccBean.getClosingDate());
				//aPS.setString(i++, aAccBean.getLastUpdate());
				aPS.setInt(i++, aAccBean.getStatus());
				//aPS.setString(i++, aAccBean.getLastTransDate());
				aPS.setString(i++, aAccBean.getDrawingType());
				aPS.setString(i++, aAccBean.getAccountName());
				aPS.setString(i++, aAccBean.getAccountID());
				aPS.setString(i++, aAccBean.getShortAccountCode());
				aPS.setString(i++, aAccBean.getRemark());
				aPS.setString(i++, aAccBean.getBranchCode());	
				if (aIsNewRecord) {
					aPS.setString(i++, aAccBean.getLastTransDate());
					aPS.setString(i++, aAccBean.getLastUpdate());
				}
				
		}
	 
	 public boolean addAddress(String aAccNumber,AddressData aAddBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result=false;
			try {
				PreparedStatement pstmt=conn.prepareStatement("INSERT INTO MailingAddress VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				WriteAddressData(aAccNumber, aAddBean, pstmt);
				if(pstmt.executeUpdate()>0)
					result=true;
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result=false;
			}
			return result;
		}
	 
	 private void WriteAddressData(String aAccNumber,AddressData aAddBean,PreparedStatement aPS)
		{
			try {
				int i = 1;
				aPS.setString(i++, aAccNumber);
				aPS.setString(i++, aAddBean.getHouseNo());
				aPS.setString(i++, aAddBean.getStreet());
				aPS.setString(i++, aAddBean.getWard());
				aPS.setString(i++, aAddBean.getTownship());
				aPS.setString(i++, aAddBean.getCity());
				aPS.setString(i++, aAddBean.getDivision());
				aPS.setString(i++, aAddBean.getCountry());
				aPS.setString(i++, aAddBean.getTel1());
				aPS.setString(i++, aAddBean.getEmail());
				aPS.setString(i++, aAddBean.getFax());
				aPS.setString(i++, aAddBean.getPostalCode());
				aPS.setString(i++, aAddBean.getStatus());
				aPS.setString(i++, aAddBean.getWebsite());

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	 
	 public int getSignatureNo(String accountNumber, String pBrCode, Connection pConn) throws SQLException {
			int signNo = 0 ;
			PreparedStatement pstmt = pConn.prepareStatement("Select count(*) As COUNT from "+getDBName()+"..localsignature"+pBrCode+" where AccNumber = ?");
			pstmt.setString(1, accountNumber);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				signNo=rs.getInt("COUNT");
			}
			return signNo;
		}
	 
	 public boolean addLocalSingature(SignatureData aSignBean,String aSerialNo, String pFileName,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			
			boolean result=false; 
			//String fileName = aSignBean.getAccountNumber() +"_"+ aSignBean.getSerialNo();
			try {
				PreparedStatement pstmt=conn.prepareStatement("INSERT INTO "+getDBName()+"..localsignature"+aSignBean.getBranchCode()+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				WriteSignatureData(aSignBean,aSerialNo, pFileName,pstmt);
				pstmt.close();
				result=true;            
			} catch (SQLException e) {					
				e.printStackTrace();
				result=false;
			}
			return result;
		}

	 private void WriteSignatureData(SignatureData aSignBean,String aSerialNo,String FileName, PreparedStatement aPS)
		{		
			String fileName = aSignBean.getAccountNumber() +"_"+ aSignBean.getSerialNo() + aSignBean.getExtension();					
			try {	 			
				File file = new File("");
				file = new File(SingletonServer.getAbsPath() + aSignBean.getpFilePath().replace("/", "\\") +fileName);
				
				if(file.isFile()){
					FileInputStream fis = new FileInputStream(file);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];

					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						bos.write(buf, 0, readNum); 		                
					}
				
					byte[] bytes = bos.toByteArray();
					int i = 1;
					aPS.setString(i++, aSignBean.getAccountNumber());
					aPS.setString(i++, aSignBean.getAccountName());
					aPS.setString(i++, aSignBean.getAccountType());
					aPS.setString(i++, aSignBean.getRemark());
					aPS.setBytes(i++, bytes);
					aPS.setInt(i++, aSignBean.getStatus());
					aPS.setBytes(i++, aSignBean.getpImg1());
					aPS.setBytes(i++, aSignBean.getpImg2());
					aPS.setBytes(i++, aSignBean.getpImg3());
					aPS.setBytes(i++, aSignBean.getpImg4());
					aPS.setBytes(i++, aSignBean.getpImg5());
					aPS.setBytes(i++, aSignBean.getpImg6());
					aPS.setShort(i++, aSignBean.getN1());
					aPS.setString(i++, aSignBean.getSerialNo());	
					aPS.setString(i++,aSignBean.getExtension());
					aPS.executeUpdate();
					fis.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		}
	 
	 public boolean updateAccount(AccountData aAccBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException{
			boolean result=false;
			PreparedStatement pstmt=null;
			try {
				pstmt = conn.prepareStatement("UPDATE Accounts SET AccNumber=?,CurrencyCode=?,OpeningDate=?," +
						"ClosingDate=?,Status=?,DrawType=?,AccName=?,AccNRC=?," +
						"SAccNo=?,Remark=? WHERE AccNumber=?");

				updateRecord(aAccBean, pstmt,false);
				pstmt.setString(11, aAccBean.getAccountNumber());
				if(pstmt.executeUpdate()>0)
					result=true;
				if(result) {
					if(LogicMgr.getSystemSettingT12N4("BNK")==1) {
						pstmt = conn.prepareStatement("UPDATE Accounts_Balance SET AccNumber=?,CurrencyCode=?,OpeningDate=?," +
								"ClosingDate=?,Status=?,DrawType=?,AccName=?,AccNRC=?," +
								"SAccNo=?,Remark=? WHERE AccNumber=?");
						updateRecord(aAccBean, pstmt,false);
						pstmt.setString(11, aAccBean.getAccountNumber());
						if(pstmt.executeUpdate()>0)
							result=true;
					}
				}
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result=false;
			}
			return result;
		}
	 
	 public boolean updateAddress(String aAccNumber, AddressData aAddBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException{
			boolean result=false;
			PreparedStatement pstmt=null;
			try {
				pstmt = conn.prepareStatement("UPDATE MailingAddress SET AccNumber=?,HouseNo=?,Street=?,Ward=?," +
						"Township =?,City=?,Division=?,Country=?,Phone=?,Email=?,Fax=?,PostalCode=?," +
						"Status=?,Website=? WHERE AccNumber=?");

				WriteAddressData(aAccNumber, aAddBean, pstmt);
				pstmt.setString(15, aAccNumber);
				if(pstmt.executeUpdate() > 0)
					result=true;
				pstmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
				result=false;
			}
			return result;
		}
	 
	 public int getMaxSerialNo(String pAccountNo, String pBrCode, Connection pConn) throws SQLException {
			int result = 0;
			String l_Query = "";
			l_Query = "SELECT ISNULL(MAX(SerialNo),0) as MaxSrNo FROM "+getDBName()+"..localsignature"+pBrCode+" where 1 = 1 ";

			if(!pAccountNo.equals("")){
				l_Query += "and accnumber = '"+ pAccountNo +"' ";
			}

			/*PreparedStatement pstmt = pConn.prepareStatement("SELECT MAX(SerialNo) FROM CollateralInfo ");*/
			PreparedStatement pstmt = pConn.prepareStatement(l_Query);

			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				if(rs.getString(1) != null){
					result = Integer.parseInt(rs.getString("MaxSrNo"));
				}
			}
			rs.close();
			pstmt.close();
			return result;
		}
	 
	 public boolean deleteLocalSingnature(String aAccNumber,String aSerialNo,String pBrCode,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			boolean result=false;
			try{		        
				PreparedStatement pstmt=conn.prepareStatement("DELETE FROM "+getDBName()+"..localsignature"+pBrCode+" WHERE AccNumber=? and SerialNo=?");
				pstmt.setString(1, aAccNumber);
				pstmt.setString(2, aSerialNo);
				if(pstmt.executeUpdate() > 0 )
					result=true;
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result=false;
			}
			return result;
		}
	 
	 public boolean getMultipleSignature(String pAccountNo,String pBrCode,String pFileName,Connection conn) {

			boolean flag = false;
			try {
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM "+getDBName()+"..localsignature"+pBrCode+" WHERE AccNumber = ? Order By SerialNo");
				pstmt.setString(1, pAccountNo);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					SignatureData l_SignData = new SignatureData();
					readSignData(l_SignData, rs);
					readRecordForSign(l_SignData,rs,pFileName); 
					lstSignData.add(l_SignData);
					flag = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
		}
	 
	 private void readSignData(SignatureData pSignData, ResultSet rs) {
			try {
				pSignData.setAccountNumber(rs.getString("AccNumber"));
				pSignData.setAccountName(rs.getString("AccName"));
				pSignData.setAccountType(rs.getString("AccType"));
				pSignData.setRemark(rs.getString("Remark"));
				pSignData.setN1(rs.getShort("N1"));
				pSignData.setSerialNo(rs.getString("SerialNo"));
				pSignData.setExtension(rs.getString("Extension"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}
	 
	 private void readRecordForSign(SignatureData pData, ResultSet prs,String pFileName) {		
			try {
//				String mPath=SingletonServer.getAbsPath()+"cache//photos//signature//"+accNumber+"_"+serialNo+".docx";	
				
				if(prs.getBytes("Signature") != null){
					byte[] fileBytes = prs.getBytes("Signature");
					OutputStream targetFile;
					try {		
						targetFile = new FileOutputStream(SingletonServer.getAbsPath()+"cache//photos//signature//"+pData.getAccountNumber()+"_"+pData.getSerialNo()+ pData.getExtension());
						targetFile.write(fileBytes);
						targetFile.close();
					} catch (FileNotFoundException e) {					
						e.printStackTrace();
					} catch (IOException e) {					
						e.printStackTrace();					
					}
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	 
	 public String getLastIDNotAccType(String aPrdCode,String aAccTypeCode, String aBranchCode, String aCurCode,
				int aSerialStart, int aSerialLen,int aPrdStart,int aPrdLen,int aAccTypeStart,int aAccTypeLen, 
				int aBranchStart, int aBranchLen, 
				int aCurCodeStart,int aCurCodeLen, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{


			String sql = "SELECT MAX(CONVERT(int, SUBSTRING(AccNumber, " + aSerialStart + ", " + aSerialLen + "))) FROM Accounts WHERE ";
			sql += "SUBSTRING(AccNumber," + aBranchStart + "," + aBranchLen + ")= '" + StrUtil.leadZeros(aBranchCode, aBranchLen) + "'  ";
			if(aCurCodeLen>0)
				sql += " AND SUBSTRING(AccNumber," + aCurCodeStart + "," + aCurCodeLen + ")= '" + StrUtil.leadZeros(aCurCode, aCurCodeLen) + "'  ";
			if(aPrdLen>0)
				sql += " AND SUBSTRING(AccNumber," + aPrdStart + "," + aPrdLen + ")= '" + StrUtil.leadZeros(aPrdCode, aPrdLen) + "'";

			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			String id="";
			while(rs.next()){
				id=String.valueOf(rs.getInt(1));
			}
			pstmt.close();

			if (id.equals("0")) {
				id = "0000000000000000";
			}
			return id;
		}
	 
	 public String getLastIDNotAccTypeByFirstWord(String aPrdCode,String aBranchCode, String aCurCode, String aOtherCode,
				int aSerialStart, int aSerialLen,
				int aPrdStart,int aPrdLen, 
				int aBranchStart, int aBranchLen, 
				int aCurCodeStart,int aCurCodeLen, 
				int aOtherCodeStart,int aOtherCodeLen,
				Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{


			String sql = "SELECT MAX(CONVERT(int, SUBSTRING(AccNumber, " + aSerialStart + ", " + aSerialLen + "))) FROM Accounts WHERE ";
			sql += "SUBSTRING(AccNumber," + aBranchStart + "," + aBranchLen + ")= '" + StrUtil.leadZeros(aBranchCode, aBranchLen) + "' AND ";
			sql += "SUBSTRING(AccNumber," + aCurCodeStart + "," + aCurCodeLen + ")= '" + StrUtil.leadZeros(aCurCode, aCurCodeLen) + "' AND ";
			sql += "SUBSTRING(AccNumber," + aPrdStart + "," + aPrdLen + ")= '" + StrUtil.leadZeros(aPrdCode, aPrdLen) + "' AND ";
			sql += "SUBSTRING(AccNumber," + aOtherCodeStart + "," + aOtherCodeLen + ")= '" + StrUtil.leadZeros(aOtherCode, aOtherCodeLen) + "'";

			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			String id="";
			while(rs.next()){
				id=String.valueOf(rs.getInt(1));
			}
			pstmt.close();

			if (id.equals("0")) {
				id = "0000000000000000";
			}
			return id;
		}
	 
	 public String getLastAccNameMRHF(String prefix,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			String sql = "SELECT ISNULL(Max( Convert(int,SubString(AccName,8,13) ) ) ,0)+1 AS MaxCode FROM Accounts WHERE SubString(AccName,1,6)=?";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, prefix);
			ResultSet rs=pstmt.executeQuery();
			String id="";
			while(rs.next()){
				id=String.valueOf(rs.getInt(1));
			}
			pstmt.close();
			if (id.equals("0")) {
				id = "000000";
			}
			return id;
		}

	 public int getAccCountMRHF(Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			String sql = "SELECT count(AccNumber) AS AccCount FROM accounts";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			int id=0;
			while(rs.next()){
				id=rs.getInt(1);
			}
			pstmt.close();
			return id;
		}
	 
	 public boolean addCLHeader(AccountData aSavingAccBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result=false;

			int l_LastAccTransNo = getLastAccTransNo(conn);
			aSavingAccBean.setLastTransNo(l_LastAccTransNo);
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO CLHeader VALUES(?,?,?,?)");
			WriteCLHeaderData(aSavingAccBean, pstmt);	
			if(pstmt.executeUpdate() > 0)
				result=true;
			pstmt.close();

			return result;
		}
	 
	 public int getLastAccTransNo(Connection conn){

			int ret=0;

			PreparedStatement pstmt;
			try {
				pstmt = conn.prepareStatement("SELECT Max(TransNo) As MaxTran FROM AccountTransaction");

				ResultSet rs=pstmt.executeQuery();

				while(rs.next()){
					ret=rs.getInt(1);
				}
				pstmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}


			return ret;

		}
	 
	 private void WriteCLHeaderData(AccountData aCLHeaderBean,PreparedStatement aPS) throws SQLException
		{
			int i = 1;
			aPS.setString(i++, aCLHeaderBean.getAccountNumber());
			aPS.setInt(i++, aCLHeaderBean.getLastTransNo());
			aPS.setInt(i++, aCLHeaderBean.getLastLineNo());
			aPS.setInt(i++, aCLHeaderBean.getLastPage());			
		}
	 
	 public boolean addCurrentAccout(AccountData aCurrAccBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{
			boolean result=false;

			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO CurrentAccount VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			WriteCurrentData(aCurrAccBean, pstmt, "insert");	
			pstmt.executeUpdate();
			pstmt.close();
			result=true;

			return result;
		}
	 
	 private void WriteCurrentData(AccountData aCurrAccBean,PreparedStatement aPS, String pFrom) throws SQLException
		{

			int i = 1;
			if (pFrom.equals("insert")){
				aPS.setString(i++, aCurrAccBean.getAccountNumber());
				aPS.setDouble(i++, aCurrAccBean.getOdLimit());
				aPS.setString(i++, aCurrAccBean.getOdExpDate());
				aPS.setDouble(i++, aCurrAccBean.getIntToBeCollected());
				aPS.setInt(i++, aCurrAccBean.getBusinessType());
				aPS.setDouble(i++, aCurrAccBean.getCollateral());
				aPS.setString(i++, aCurrAccBean.getApproveNo());
				aPS.setString(i++, aCurrAccBean.getApproveID());
				aPS.setInt(i++, aCurrAccBean.getSecurityType());
				aPS.setInt(i++, aCurrAccBean.getIsOD());
				aPS.setString(i++, aCurrAccBean.getSanctionDate());
				aPS.setInt(i++, aCurrAccBean.getODStatus());
				aPS.setDouble(i++, aCurrAccBean.getTODLimit());
				aPS.setString(i++, aCurrAccBean.getTODExpDate());
				aPS.setString(i++, aCurrAccBean.getTODSanctionDate());
				aPS.setDouble(i++, aCurrAccBean.getSystemTOD());
				aPS.setInt(i++, aCurrAccBean.getIsCommit());
				aPS.setInt(i++, 0);
			} else {
				aPS.setDouble(i++, aCurrAccBean.getOdLimit());
				aPS.setString(i++, aCurrAccBean.getOdExpDate());
				aPS.setDouble(i++, aCurrAccBean.getIntToBeCollected());
				aPS.setInt(i++, aCurrAccBean.getBusinessType());
				aPS.setDouble(i++, aCurrAccBean.getCollateral());
				aPS.setString(i++, aCurrAccBean.getApproveNo());
				aPS.setString(i++, aCurrAccBean.getApproveID());
				aPS.setInt(i++, aCurrAccBean.getSecurityType());
				aPS.setInt(i++, aCurrAccBean.getIsOD());
				aPS.setString(i++, aCurrAccBean.getSanctionDate());
				aPS.setInt(i++, aCurrAccBean.getODStatus());
				aPS.setDouble(i++, aCurrAccBean.getTODLimit());
				aPS.setString(i++, aCurrAccBean.getTODExpDate());
				aPS.setString(i++, aCurrAccBean.getTODSanctionDate());
				aPS.setDouble(i++, aCurrAccBean.getSystemTOD());
				aPS.setInt(i++, aCurrAccBean.getIsCommit());
				aPS.setInt(i++, 0);
				aPS.setString(i++, aCurrAccBean.getAccountNumber());
			}				

		}
	 
	 public boolean addSavingAccout(AccountData aSavingAccBean,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result=false;		
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO SavingsAccount VALUES(?,?,?)");
			WriteSavingData(aSavingAccBean, pstmt);	
			if(pstmt.executeUpdate()>0)
				result=true;
			pstmt.close();
			return result;
		}

		private void WriteSavingData(AccountData aSavingAccBean,PreparedStatement aPS) throws SQLException
		{
			aPS.setString(1, aSavingAccBean.getAccountNumber());
			aPS.setDouble(2, aSavingAccBean.getIntToBePaid());	
			aPS.setString(3, aSavingAccBean.getAccToBePaid());
		}
		
		public boolean addFDPBHeader(AccountData object, double Tenure, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result = false;
			try {
				int l_LastAccTransNo = 0;
				object.setLastTransNo(l_LastAccTransNo);
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO PBHeader(AccNumber,LastLineNo,BeginTransNo,LastTransNo,Tenure) VALUES(?,?,?,?,?)");
				WritePBHeaderData(object, pstmt);	
				pstmt.setDouble(5, Tenure);
				if(pstmt.executeUpdate()>0)
					result = true;
				pstmt.close();            
			} catch (SQLException e) {					
				e.printStackTrace();
				result=false;
			}
			return result;
		}

		private void WritePBHeaderData(AccountData object,PreparedStatement aPS) throws SQLException
		{		
			int i = 1;
			aPS.setString(i++, object.getAccountNumber());
			aPS.setInt(i++, object.getLastTransNo());
			aPS.setInt(i++, object.getLastLineNo());
			aPS.setInt(i++, 1);		
		}
		
		//KMT start
		public boolean addFDPBHeader(AccountData object, double Tenure, String Ref, Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result = false;

			int l_LastAccTransNo = 0;
			object.setLastTransNo(l_LastAccTransNo);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO PBHeader(AccNumber,LastLineNo,BeginTransNo,LastTransNo,Tenure, RefNo) VALUES(?,?,?,?,?,?)");
			WritePBHeaderData(object, pstmt);	
			pstmt.setDouble(5, Tenure);
			pstmt.setString(6, Ref);
			if(pstmt.executeUpdate() > 0)
				result = true;
			pstmt.close();                            

			return result;
		}//KMT end
		
		public boolean addPBHistory(AccountData object,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result=false;

			String l_MaxSerialNo = "";
			l_MaxSerialNo = getLastSerialNo(object.getAccountNumber() , conn);
			object.setPBSerialNo(l_MaxSerialNo);
			object.setPBStatus(1);
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO PassBookHistory VALUES(?,?,?,?,?,?)");
			WritePBHistoryData(object, pstmt);	
			if(pstmt.executeUpdate()>0)
				result=true;
			pstmt.close();            


			return result;
		}
		
		public String getLastSerialNo(String aAccNumber,Connection conn){

			String ret="0";
			int id = 0;

			PreparedStatement pstmt;
			try {
				pstmt = conn.prepareStatement("SELECT ISNULL(MAX(CAST(PBSerialNo AS INT)),0) FROM PassBookHistory WHERE AccNumber=?");

				pstmt.setString(1, aAccNumber);

				ResultSet rs=pstmt.executeQuery();

				while(rs.next()){
					id=rs.getInt(1);
				}
				pstmt.close();

				id = id+1;
				ret = Integer.toString(id);
			} catch (SQLException e) {

				e.printStackTrace();
			}
			return ret;
		}
		
		private void WritePBHistoryData(AccountData object,PreparedStatement aPS) throws SQLException
		{

			int i = 1;
			aPS.setString(i++, object.getAccountNumber());
			aPS.setString(i++, object.getPassbook());
			aPS.setString(i++, object.getRemark());
			aPS.setString(i++, object.getPBSerialNo());
			aPS.setString(i++, object.getIssueDate());
			aPS.setInt(i++, object.getPBStatus());
		}
		
		public boolean addPBHeader(AccountData object,Connection conn) throws ParserConfigurationException, SAXException, IOException,ClassNotFoundException,SQLException{

			boolean result=false;

			//int l_LastAccTransNo = getLastAccTransNo(conn);
			int l_LastAccTransNo = 0;
			object.setLastTransNo(l_LastAccTransNo);
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO PBHeader(AccNumber,LastLineNo,BeginTransNo,LastTransNo) VALUES(?,?,?,?)");
			WritePBHeaderData(object, pstmt);	
			if(pstmt.executeUpdate()>0)
				result=true;
			pstmt.close();

			return result;
		}

		public DataResult updateAccTobePaid(AccountData pdata, Connection pConn) throws SQLException {
			DataResult l_result = new DataResult();
			String l_Query = "UPDATE SavingsAccount SET AccTobePaid=? WHERE AccNumber =?";
			PreparedStatement l_pstmt = pConn.prepareStatement(l_Query);
			l_pstmt.setString(1, pdata.getAccToBePaid());
			l_pstmt.setString(2, pdata.getAccountNumber());					
			if(l_pstmt.executeUpdate() > 0) {
				l_result.setStatus(true);
			}
			return l_result;
		}
		
		//KMT start
		public boolean isExist(String aAccNumber, String aRef, Connection conn) throws SQLException{
			boolean ret = false;
			String l_Query = "";

			l_Query = "SELECT * FROM PBHeader WHERE AccNumber = '"+aAccNumber+"' and RefNo = '"+aRef+"'";
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){
				ret = true;
			}
			rs.close();
			pstmt.close();		
			return ret;
		}//KMT end
		
		public boolean isExist(String aAccNumber, double aTenure, Connection conn) throws SQLException{
			boolean ret = false;
			String l_Query = "";

			l_Query = "SELECT * FROM PBHeader WHERE AccNumber = '"+aAccNumber+"' and Tenure = "+ aTenure;
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){
				ret = true;
			}
			rs.close();
			pstmt.close();

			return ret;
		}
}
