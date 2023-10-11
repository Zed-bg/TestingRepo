package com.nirvasoft.rp.shared;

import java.util.ArrayList;

public class LAODFScheduleData {
		
		private String accnumber;
		private String startdate;
		private String duedate;
		private int termno;
		private String principleamount;
		private String interestamount;
		private String repaymentamount;
		private int batchno;
		private int status;
		private int loantype;
		private String penaltyamount;
		private String backaccnumber;
		private String mebinterestamount;
		private String prinoutamount;
		private String intoutamount;
		private String mebintoutamount;
		private String penaltyoutamount;
		private String chequeno;
		/* 
		 * 
		private double endBal;
		private double beginnigBal;
		private double cumulativeBal;
		private int schFormular;
		private int transNo;
		private String transDate;
		private int n1;
		private String cusName;
		private String t1;
		private int n2;
		private int n3;
		private String chequeNo;
		private double n4;
		private boolean partialPayment;
		private double paymentAmt;
		private String paymentType;
		private String PaidForMonth;
		private String LastUpdateDate;
		private ArrayList<LAODFData> l_laodfData = null;
		
		 */
		
		public LAODFScheduleData(){
			this.accnumber = "";
			this.duedate = "";
			this.startdate = "";
			this.termno = 0;
			this.principleamount = "";
			this.interestamount = "";
			this.repaymentamount = "";
			this.batchno=0;
			this.status=0;
			this.loantype=0;
			this.backaccnumber="";
			this.mebinterestamount = "";
			this.prinoutamount = "";
			this.intoutamount = "";
			this.mebintoutamount = "";
			this.penaltyoutamount = "";
			this.chequeno = "";
			/*
			
			this.endBal = 0;
			this.beginnigBal = 0;
			this.cumulativeBal = 0;
			this.l_laodfData = new ArrayList<LAODFData>();
			this.schFormular=0;
			this.transNo=0;
			this.transDate="";
			this.n1 = 0;
			this.cusName = "";
			this.t1 = "";
			this.chequeNo = "";
			this.setMebIntAmt(0);
			this.n4 = 0;
			this.paymentAmt = 0;
			this.partialPayment = false;
			this.setPaymentType("");
			this.prinOutAmt = 0;
			this.intOutAmt = 0;
			this.mebIntOutAmt = 0;
			this.penaltyOutAmt = 0;
			this.PaidForMonth = "";
			this.LastUpdateDate = "";
			 */
		}
		
		public int getLoantype() {
			return loantype;
		}

		public void setLoantype(int loanType) {
			this.loantype = loanType;
		}

		public String getAccnumber() {
			return accnumber;
		}
		public void setAccnumber(String accNumber) {
			this.accnumber = accNumber;
		}

		public String getDuedate() {
			return duedate;
		}
		public void setDuedate(String dueDate) {
			this.duedate = dueDate;
		}
		public int getTermno() {
			return termno;
		}
		public void setTermno(int termNo) {
			this.termno = termNo;
		}
		public String getPrincipleamount() {
			return principleamount;
		}
		public void setPrincipleamount(String prinAmt) {
			this.principleamount = prinAmt;
		}
		public String getInterestamount() {
			return interestamount;
		}
		public void setInterestamount(String intAmt) {
			this.interestamount = intAmt;
		}
		public String getRepaymentamount() {
			return repaymentamount;
		}
		public void setRepaymentamount(String payReq) {
			this.repaymentamount = payReq;
		}
		public int getBatchno() {
			return batchno;
		}
		public void setBatchno(int batchNo) {
			this.batchno = batchNo;
		}
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getStartdate() {
			return startdate;
		}

		public void setStartdate(String startDate) {
			this.startdate = startDate;
		}
		public String getPenaltyamount() {
			return penaltyamount;
		}

		public void setPenaltyamount(String penaltyamt) {
			this.penaltyamount = penaltyamt;
		}
		
		public String getBackaccnumber() {
			return backaccnumber;
		}

		public void setBackaccnumber(String backAccNum) {
			this.backaccnumber = backAccNum;
		}
		
		public String getMebinterestamount() {
			return mebinterestamount;
		}

		public void setMebinterestamount(String mebIntAmt) {
			this.mebinterestamount = mebIntAmt;
		}
		/* 
		public double getEndBal() {
			return endBal;
		}
		public void setEndBal(double endBal) {
			this.endBal = endBal;
		}
		public double getBeginnigBal() {
			return beginnigBal;
		}
		public void setBeginnigBal(double beginnigBal) {
			this.beginnigBal = beginnigBal;
		}
		public double getCumulativeBal() {
			return cumulativeBal;
		}
		public void setCumulativeBal(double cumulativeBal) {
			this.cumulativeBal = cumulativeBal;
		}
		public ArrayList<LAODFData> getlaodfData() {
			return l_laodfData;
		}

		public void setlaodfData(ArrayList<LAODFData> l_laodfData) {
			this.l_laodfData = l_laodfData;
		}
		public int getSchFormular() {
			return schFormular;
		}

		public void setSchFormular(int schFormular) {
			this.schFormular = schFormular;
		}

		public int getTransNo() {
			return transNo;
		}

		public void setTransNo(int transNo) {
			this.transNo = transNo;
		}

		public String getTransDate() {
			return transDate;
		}

		public void setTransDate(String transDate) {
			this.transDate = transDate;
		}

		public int getN1() {
			return n1;
		}

		public void setN1(int n1) {
			this.n1 = n1;
		}

		public String getCusName() {
			return cusName;
		}

		public void setCusName(String cusName) {
			this.cusName = cusName;
		}
		public String getT1() {
			return t1;
		}

		public void setT1(String t1) {
			this.t1 = t1;
		}

		public String getChequeNo() {
			return chequeNo;
		}

		public void setChequeNo(String chequeNo) {
			this.chequeNo = chequeNo;
		}

		public int getN2() {
			return n2;
		}

		public void setN2(int n2) {
			this.n2 = n2;
		}

		public int getN3() {
			return n3;
		}

		public void setN3(int n3) {
			this.n3 = n3;
		}

		public double getN4() {
			return n4;
		}

		public void setN4(double n4) {
			this.n4 = n4;
		}

		public double getPrinOutAmt() {
			return prinOutAmt;
		}

		public void setPrinOutAmt(double prinOutAmt) {
			this.prinOutAmt = prinOutAmt;
		}

		public double getIntOutAmt() {
			return intOutAmt;
		}

		public void setIntOutAmt(double intOutAmt) {
			this.intOutAmt = intOutAmt;
		}

		public double getMebIntOutAmt() {
			return mebIntOutAmt;
		}

		public void setMebIntOutAmt(double mebIntOutAmt) {
			this.mebIntOutAmt = mebIntOutAmt;
		}

		public ArrayList<LAODFData> getL_laodfData() {
			return l_laodfData;
		}

		public void setL_laodfData(ArrayList<LAODFData> l_laodfData) {
			this.l_laodfData = l_laodfData;
		}

		public boolean isPartialPayment() {
			return partialPayment;
		}

		public void setPartialPayment(boolean partialPayment) {
			this.partialPayment = partialPayment;
		}

		public double getPaymentAmt() {
			return paymentAmt;
		}

		public void setPaymentAmt(double paymentAmt) {
			this.paymentAmt = paymentAmt;
		}

		public String getPaymentType() {
			return paymentType;
		}

		public void setPaymentType(String paymentType) {
			this.paymentType = paymentType;
		}

		public double getPenaltyOutAmt() {
			return penaltyOutAmt;
		}

		public void setPenaltyOutAmt(double penaltyOutAmt) {
			this.penaltyOutAmt = penaltyOutAmt;
		}

		public String getPaidForMonth() {
			return PaidForMonth;
		}

		public void setPaidForMonth(String paidForMonth) {
			PaidForMonth = paidForMonth;
		}

		public String getLastUpdateDate() {
			return LastUpdateDate;
		}

		public void setLastUpdateDate(String lastUpdateDate) {
			LastUpdateDate = lastUpdateDate;
		}
		 */

		public String getPrinoutamount() {
			return prinoutamount;
		}

		public void setPrinoutamount(String prinoutamount) {
			this.prinoutamount = prinoutamount;
		}

		public String getIntoutamount() {
			return intoutamount;
		}

		public void setIntoutamount(String intoutamount) {
			this.intoutamount = intoutamount;
		}

		public String getMebintoutamount() {
			return mebintoutamount;
		}

		public void setMebintoutamount(String mebintoutamount) {
			this.mebintoutamount = mebintoutamount;
		}

		public String getPenaltyoutamount() {
			return penaltyoutamount;
		}

		public void setPenaltyoutamount(String penaltyoutamount) {
			this.penaltyoutamount = penaltyoutamount;
		}

		public String getChequeno() {
			return chequeno;
		}

		public void setChequeno(String chequeno) {
			this.chequeno = chequeno;
		}
		

}
