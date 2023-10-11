// 
// Decompiled by Procyon v0.5.36
// 

package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DisPaymentTransactionData
{
    private long autokey;
    private String transtime;
    private String transdate;
    private String mobileuserid;
    private String fromAccount;
    private String toAccount;
    private String toBranch;
    private String fromBranch;
    private String flexrefno;
    private String customerCode;
    private String customerName;
    private String customerNumber;
    private String chequeNo;
    private String recmethod;
    private String currencycode;
    private double amount;
    private String fileuploaddatetime;
    private String acerefno;
    private String checkAccType;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
    private String t5;
    private String t6;
    private String t7;
    private String t8;
    private String t9;
    private String t10;
    private String t11;
    private String t12;
    private String t13;
    private String t14;
    private String t15;
    private String t16;
    private String t17;
    private String t18;
    private String t19;
    private String t20;
    private String t21;
    private String t22;
    private String t23;
    private String t24;
    private String t25;
    private int n1;
    private int n2;
    private int n3;
    private int n4;
    private int n5;
    private int n6;
    private int n7;
    private int n8;
    private int n9;
    private int n10;
    private int n11;
    private int n12;
    private int n13;
    private int n14;
    private int n15;
    private int n16;
    private int n17;
    private int n18;
    private int n19;
    private int n20;
    private String sessionId;
    private int totalpage;
    private String telRefacc;
    private String isUser;
    private String isVIP;
    private double totalamount;
    private String chargescitystatus;
    private String phoneno;
    private String message;
    private String toemail;
    private String ccemail;
    private String branchName;
    private String flexBackBranch;
    private String toBankCode;
    private double comm1;
    private double comm2;
    
    public String getCcemail() {
        return this.ccemail;
    }
    
    public void setCcemail(final String ccemail) {
        this.ccemail = ccemail;
    }
    
    public String getToemail() {
        return this.toemail;
    }
    
    public void setToemail(final String toemail) {
        this.toemail = toemail;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getPhoneno() {
        return this.phoneno;
    }
    
    public void setPhoneno(final String phoneno) {
        this.phoneno = phoneno;
    }
    
    public String getChargescitystatus() {
        return this.chargescitystatus;
    }
    
    public void setChargescitystatus(final String chargescitystatus) {
        this.chargescitystatus = chargescitystatus;
    }
    
    public double getTotalamount() {
        return this.totalamount;
    }
    
    public void setTotalamount(final double totalamount) {
        this.totalamount = totalamount;
    }
    
    public String getCheckAccType() {
        return this.checkAccType;
    }
    
    public void setCheckAccType(final String checkAccType) {
        this.checkAccType = checkAccType;
    }
    
    public String getFlexBackBranch() {
        return this.flexBackBranch;
    }
    
    public void setFlexBackBranch(final String flexBackBranch) {
        this.flexBackBranch = flexBackBranch;
    }
    
    public String getIsVIP() {
        return this.isVIP;
    }
    
    public void setIsVIP(final String isVIP) {
        this.isVIP = isVIP;
    }
    
    public String getIsUser() {
        return this.isUser;
    }
    
    public void setIsUser(final String isUser) {
        this.isUser = isUser;
    }
    
    public String getT10() {
        return this.t10;
    }
    
    public void setT10(final String t10) {
        this.t10 = t10;
    }
    
    public String getT6() {
        return this.t6;
    }
    
    public void setT6(final String t6) {
        this.t6 = t6;
    }
    
    public String getT7() {
        return this.t7;
    }
    
    public void setT7(final String t7) {
        this.t7 = t7;
    }
    
    public String getT8() {
        return this.t8;
    }
    
    public void setT8(final String t8) {
        this.t8 = t8;
    }
    
    public String getT9() {
        return this.t9;
    }
    
    public void setT9(final String t9) {
        this.t9 = t9;
    }
    
    public String getAcerefno() {
        return this.acerefno;
    }
    
    public void setAcerefno(final String acerefno) {
        this.acerefno = acerefno;
    }
    
    public String getTelRefacc() {
        return this.telRefacc;
    }
    
    public void setTelRefacc(final String telRefacc) {
        this.telRefacc = telRefacc;
    }
    
    public int getN10() {
        return this.n10;
    }
    
    public void setN10(final int n10) {
        this.n10 = n10;
    }
    
    public int getN11() {
        return this.n11;
    }
    
    public void setN11(final int n11) {
        this.n11 = n11;
    }
    
    public int getN12() {
        return this.n12;
    }
    
    public void setN12(final int n12) {
        this.n12 = n12;
    }
    
    public int getN13() {
        return this.n13;
    }
    
    public void setN13(final int n13) {
        this.n13 = n13;
    }
    
    public int getN14() {
        return this.n14;
    }
    
    public void setN14(final int n14) {
        this.n14 = n14;
    }
    
    public int getN15() {
        return this.n15;
    }
    
    public void setN15(final int n15) {
        this.n15 = n15;
    }
    
    public int getN6() {
        return this.n6;
    }
    
    public void setN6(final int n6) {
        this.n6 = n6;
    }
    
    public int getN7() {
        return this.n7;
    }
    
    public void setN7(final int n7) {
        this.n7 = n7;
    }
    
    public int getN8() {
        return this.n8;
    }
    
    public void setN8(final int n8) {
        this.n8 = n8;
    }
    
    public int getN9() {
        return this.n9;
    }
    
    public void setN9(final int n9) {
        this.n9 = n9;
    }
    
    public String getT11() {
        return this.t11;
    }
    
    public void setT11(final String t11) {
        this.t11 = t11;
    }
    
    public String getT12() {
        return this.t12;
    }
    
    public void setT12(final String t12) {
        this.t12 = t12;
    }
    
    public String getT13() {
        return this.t13;
    }
    
    public void setT13(final String t13) {
        this.t13 = t13;
    }
    
    public String getT14() {
        return this.t14;
    }
    
    public void setT14(final String t14) {
        this.t14 = t14;
    }
    
    public String getT15() {
        return this.t15;
    }
    
    public void setT15(final String t15) {
        this.t15 = t15;
    }
    
    public String getT16() {
        return this.t16;
    }
    
    public void setT16(final String t16) {
        this.t16 = t16;
    }
    
    public String getT17() {
        return this.t17;
    }
    
    public void setT17(final String t17) {
        this.t17 = t17;
    }
    
    public String getT18() {
        return this.t18;
    }
    
    public void setT18(final String t18) {
        this.t18 = t18;
    }
    
    public String getT19() {
        return this.t19;
    }
    
    public void setT19(final String t19) {
        this.t19 = t19;
    }
    
    public String getT20() {
        return this.t20;
    }
    
    public void setT20(final String t20) {
        this.t20 = t20;
    }
    
    public int getTotalpage() {
        return this.totalpage;
    }
    
    public void setTotalpage(final int totalpage) {
        this.totalpage = totalpage;
    }
    
    public DisPaymentTransactionData() {
        this.checkAccType = "";
        this.chargescitystatus = "";
        this.phoneno = "";
        this.message = "";
        this.toemail = "";
        this.ccemail = "";
        this.branchName = "";
        this.clearproperty();
    }
    
    private void clearproperty() {
        this.isVIP = "";
        this.amount = 0.0;
        this.totalamount = 0.0;
        this.sessionId = "";
        this.autokey = 0L;
        this.chequeNo = "";
        this.currencycode = "";
        this.customerCode = "";
        this.flexrefno = "";
        this.mobileuserid = "";
        this.fromAccount = "";
        this.toAccount = "";
        this.toBranch = "";
        this.fromBranch = "";
        this.customerName = "";
        this.customerNumber = "";
        this.fileuploaddatetime = "";
        this.n1 = 0;
        this.n2 = 0;
        this.n3 = 0;
        this.n4 = 0;
        this.n5 = 0;
        this.n6 = 0;
        this.n7 = 0;
        this.n8 = 0;
        this.n9 = 0;
        this.n10 = 0;
        this.n11 = 0;
        this.n12 = 0;
        this.n13 = 0;
        this.n14 = 0;
        this.n15 = 0;
        this.n16 = 0;
        this.n17 = 0;
        this.n18 = 0;
        this.n19 = 0;
        this.n20 = 0;
        this.recmethod = "";
        this.transdate = "";
        this.transtime = "";
        this.t1 = "";
        this.t2 = "";
        this.t3 = "";
        this.t4 = "";
        this.t5 = "";
        this.t6 = "";
        this.t7 = "";
        this.t8 = "";
        this.t9 = "";
        this.t10 = "";
        this.t11 = "";
        this.t12 = "";
        this.t13 = "";
        this.t14 = "";
        this.t15 = "";
        this.t16 = "";
        this.t17 = "";
        this.t18 = "";
        this.t19 = "";
        this.t20 = "";
        this.t21 = "";
        this.t22 = "";
        this.t23 = "";
        this.t24 = "";
        this.t25 = "";
        this.telRefacc = "";
        this.acerefno = "";
        this.toBankCode = "";
        this.comm1 = 0;
        this.comm2 = 0;
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getCustomerName() {
        return this.customerName;
    }
    
    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerNumber() {
        return this.customerNumber;
    }
    
    public void setCustomerNumber(final String customerNumber) {
        this.customerNumber = customerNumber;
    }
    
    public String getFileuploaddatetime() {
        return this.fileuploaddatetime;
    }
    
    public void setFileuploaddatetime(final String fileuploaddatetime) {
        this.fileuploaddatetime = fileuploaddatetime;
    }
    
    public String getFromAccount() {
        return this.fromAccount;
    }
    
    public void setFromAccount(final String fromAccount) {
        this.fromAccount = fromAccount;
    }
    
    public String getFromBranch() {
        return this.fromBranch;
    }
    
    public void setFromBranch(final String fromBranch) {
        this.fromBranch = fromBranch;
    }
    
    public String getToAccount() {
        return this.toAccount;
    }
    
    public void setToAccount(final String toAccount) {
        this.toAccount = toAccount;
    }
    
    public String getToBranch() {
        return this.toBranch;
    }
    
    public void setToBranch(final String toBranch) {
        this.toBranch = toBranch;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(final double amount) {
        this.amount = amount;
    }
    
    public long getAutokey() {
        return this.autokey;
    }
    
    public void setAutokey(final long autokey) {
        this.autokey = autokey;
    }
    
    public String getChequeNo() {
        return this.chequeNo;
    }
    
    public void setChequeNo(final String chequeNo) {
        this.chequeNo = chequeNo;
    }
    
    public String getCurrencycode() {
        return this.currencycode;
    }
    
    public void setCurrencycode(final String currencycode) {
        this.currencycode = currencycode;
    }
    
    public String getCustomerCode() {
        return this.customerCode;
    }
    
    public void setCustomerCode(final String customerCode) {
        this.customerCode = customerCode;
    }
    
    public String getFlexrefno() {
        return this.flexrefno;
    }
    
    public void setFlexrefno(final String flexrefno) {
        this.flexrefno = flexrefno;
    }
    
    public String getMobileuserid() {
        return this.mobileuserid;
    }
    
    public void setMobileuserid(final String mobileuserid) {
        this.mobileuserid = mobileuserid;
    }
    
    public int getN1() {
        return this.n1;
    }
    
    public void setN1(final int n1) {
        this.n1 = n1;
    }
    
    public int getN2() {
        return this.n2;
    }
    
    public void setN2(final int n2) {
        this.n2 = n2;
    }
    
    public int getN3() {
        return this.n3;
    }
    
    public void setN3(final int n3) {
        this.n3 = n3;
    }
    
    public int getN4() {
        return this.n4;
    }
    
    public void setN4(final int n4) {
        this.n4 = n4;
    }
    
    public int getN5() {
        return this.n5;
    }
    
    public void setN5(final int n5) {
        this.n5 = n5;
    }
    
    public String getRecmethod() {
        return this.recmethod;
    }
    
    public void setRecmethod(final String recmethod) {
        this.recmethod = recmethod;
    }
    
    public String getT1() {
        return this.t1;
    }
    
    public void setT1(final String t1) {
        this.t1 = t1;
    }
    
    public String getT2() {
        return this.t2;
    }
    
    public void setT2(final String t2) {
        this.t2 = t2;
    }
    
    public String getT3() {
        return this.t3;
    }
    
    public void setT3(final String t3) {
        this.t3 = t3;
    }
    
    public String getT4() {
        return this.t4;
    }
    
    public void setT4(final String t4) {
        this.t4 = t4;
    }
    
    public String getT5() {
        return this.t5;
    }
    
    public void setT5(final String t5) {
        this.t5 = t5;
    }
    
    public String getTransdate() {
        return this.transdate;
    }
    
    public void setTransdate(final String transdate) {
        this.transdate = transdate;
    }
    
    public String getTranstime() {
        return this.transtime;
    }
    
    public void setTranstime(final String transtime) {
        this.transtime = transtime;
    }
    
    public String getBranchName() {
        return this.branchName;
    }
    
    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }
    
    public String getT21() {
        return this.t21;
    }
    
    public void setT21(final String t21) {
        this.t21 = t21;
    }
    
    public String getT22() {
        return this.t22;
    }
    
    public void setT22(final String t22) {
        this.t22 = t22;
    }
    
    public String getT23() {
        return this.t23;
    }
    
    public void setT23(final String t23) {
        this.t23 = t23;
    }
    
    public String getT24() {
        return this.t24;
    }
    
    public void setT24(final String t24) {
        this.t24 = t24;
    }
    
    public String getT25() {
        return this.t25;
    }
    
    public void setT25(final String t25) {
        this.t25 = t25;
    }
    
    public int getN16() {
        return this.n16;
    }
    
    public void setN16(final int n16) {
        this.n16 = n16;
    }
    
    public int getN17() {
        return this.n17;
    }
    
    public void setN17(final int n17) {
        this.n17 = n17;
    }
    
    public int getN18() {
        return this.n18;
    }
    
    public void setN18(final int n18) {
        this.n18 = n18;
    }
    
    public int getN19() {
        return this.n19;
    }
    
    public void setN19(final int n19) {
        this.n19 = n19;
    }
    
    public int getN20() {
        return this.n20;
    }
    
    public void setN20(final int n20) {
        this.n20 = n20;
    }

	public String getToBankCode() {
		return toBankCode;
	}

	public void setToBankCode(String toBankCode) {
		this.toBankCode = toBankCode;
	}

	public double getComm1() {
		return comm1;
	}

	public double getComm2() {
		return comm2;
	}

	public void setComm1(double comm1) {
		this.comm1 = comm1;
	}

	public void setComm2(double comm2) {
		this.comm2 = comm2;
	}
}
