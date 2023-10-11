package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoanRepaymentData {
		private int serialno;
		private double interestrate;
		private double servicechargesrate;
		private int loantype;
		private String loantypedesc;
		private String sanctiondate;
		private String expdate;
		private double loanlimit;
		private String status;
		
		private double interestamount;
		private double servicechargesamount;
		private double currentbalance;
		private double commitmentfeeamount;
		
		public LoanRepaymentData() {
			clearProperty();
		}

		private void clearProperty() {
			serialno = 0;
			interestrate = 0;
			servicechargesrate =0;
			loantype = 0;
			loantypedesc = "";
			sanctiondate = "";
			expdate = "";
			loanlimit = 0;
			status = "";
			interestamount = 0;
			servicechargesamount = 0;
			currentbalance = 0;
			commitmentfeeamount = 0;
		}

		public int getSerialno() {
			return serialno;
		}

		public void setSerialno(int serialno) {
			this.serialno = serialno;
		}

		public double getInterestrate() {
			return interestrate;
		}

		public void setInterestrate(double interestrate) {
			this.interestrate = interestrate;
		}

		public double getServicechargesrate() {
			return servicechargesrate;
		}

		public void setServicechargesrate(double servicechargesrate) {
			this.servicechargesrate = servicechargesrate;
		}

		public int getLoantype() {
			return loantype;
		}

		public void setLoantype(int loantype) {
			this.loantype = loantype;
		}

		public String getLoantypedesc() {
			return loantypedesc;
		}

		public void setLoantypedesc(String loantypedesc) {
			this.loantypedesc = loantypedesc;
		}

		public String getSanctiondate() {
			return sanctiondate;
		}

		public void setSanctiondate(String sanctiondate) {
			this.sanctiondate = sanctiondate;
		}

		public String getExpdate() {
			return expdate;
		}

		public void setExpdate(String expdate) {
			this.expdate = expdate;
		}

		public double getLoanlimit() {
			return loanlimit;
		}

		public void setLoanlimit(double loanlimit) {
			this.loanlimit = loanlimit;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public double getInterestamount() {
			return interestamount;
		}

		public void setInterestamount(double interestamount) {
			this.interestamount = interestamount;
		}

		public double getServicechargesamount() {
			return servicechargesamount;
		}

		public void setServicechargesamount(double servicechargesamount) {
			this.servicechargesamount = servicechargesamount;
		}

		public double getCurrentbalance() {
			return currentbalance;
		}

		public void setCurrentbalance(double currentbalance) {
			this.currentbalance = currentbalance;
		}

		public double getCommitmentfeeamount() {
			return commitmentfeeamount;
		}

		public void setCommitmentfeeamount(double commitmentfeeamount) {
			this.commitmentfeeamount = commitmentfeeamount;
		}
	    
}
