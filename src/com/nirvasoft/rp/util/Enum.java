package com.nirvasoft.rp.util;

public class Enum {
	  public enum TransCode {
	    Deposit, Withdraw, TransferFrom, TransferTo, TransferBetween, MultipleTrnasferFrom, MultipleTransferTo, ATMIBTTransferFrom, ATMIBTTransferTo, ScheduleCashDr, ScheduleCashCr, ScheduleTransferDr, ScheduleTransferCr, FixedDepositIntWdl, PoCashDr, PoCashCr, PoTransferDr, PoTransferCr, LoanIntCollectDr, LoanIntCollectCr, HPTrDr, HPTrCr, ODIntCollectDr, ODIntCollectCr, AccountLinkDr, AccountLinkCr, LoanLimitDr, LoanLimitCr, GcCashDr, GcCashCr, GcTransferDr, GcTransferCr, RemitTransferDr, RemitTransferCr, RemitCashDr, RemitCashCr, RemitInWardDr, RemitInWardCr, RemitInWardDrCash, RemitInWardCrCash, FixedInterestDr, FixedAutoRenewDr, SavingsIntWdlDr, SavingsIntWdlCr, SavingsIntCashDr, FDWDLDr, FDWDLCr, FixedAutoRenewCr, FixedInterestCr, FDCashWDL, SettlementCashCr, SettlementCashDr, SettlementTrCr, SettlementTrDr, POSTrCr, POSTrDr, SettlementFeeCashCr, SettlementFeeCashDr, POSFeeTrCr, POSFeeTrDr, OtherBankRemitOutCash, OtherBankRemitOutDr, OtherBankRemitOutCr, RevaluateFrom, RevaluateTo, RevaluateCashDr, RevaluateCashCr, AllowLoanLimitDr, AllowLoanLimitCr, LoanRepaymentDr, LoanRepaymentCr, SaIntDr, SaIntCr, OBREncashDr, OBREncashCr, ODDr, ODCr, ODDrNPL, ODCrNPL, ODSCrNPL, FIDr, FICr, AccLinkFIDr, AccLinkFICr, OBRCashDr, ClearingTransferForm, ClearingTransferTo, LoanDr, LoanCr, SpDr, SpCr, HPIntDr, HPIntCr, UaIntDr, UaIntCr, DorTrDr, DorTrCr, DorIntDr, DorIntCr, BankDraftCash, BankDraftDr, BankDraftCr, CardTrCr, CardTrDr, IDCPTrDr, IDCPCashDr, IDCPTrCr, IDCPCashCr, CallIntDr, CallIntCr, HPCashCr, HPCashDr, CCTEncashDr, CCTEncashCr, CCTCashDr, PGCashCr, PGTrDr, PGTrCr, CallIntWdlDr, SpecialIntWdlDr, CallIntWdlCr, SpecialIntWdlCr, CurrentIntCashDr, CurrentIntWdlDr, CurrentIntWdlCr, CaIntDr, CaIntCr, CHQDr, CHQCr, ODDrNPLB, ODCrNPLB, SDODr, SDOCr, BBTCR, BBTDR, CashRevaluateFrom, CashRevaluateTo, CCTRemitOutCash, CCTRemitOutDr, CCTRemitOutCr, BATDR, BATCR, DVPDR, DVPCR, CCTTaxDr, CCTTaxCr, CCTTaxCashCr;
	  }
	  
	  public enum AppServerType {
	    TextDB, OracleDB, MSSQLDB, MySQLDB, Jboss, GlassFish;
	  }
	  
	  public enum ProductType {
	    CurrentAccount, SavingsAccount, FixedDepositAccount, LoanAccount, R1, R2, CashCardAccount, R3, SecureCashCardAccount;
	  }
	  
	  public enum TxTFilterOperator {
	    Equal, Contain, BeginWith, EndWith, IN;
	  }
	  
	  public enum DateFilterOperator {
	    All, Equal, GreaterThan, LessThan, GreaterThanEqual, LessThanEqual, Between;
	  }
	  
	  public enum NumFilterOperator {
	    Equal, GreaterThan, LessThan, GreaterThanEqual, LessThanEqual, Between;
	  }
	  
	  public enum Currency {
	    Kyats, Dollars, FEC, EURO;
	  }
	  
	  public enum StandardCurrency {
	    MMK, USD, SGD, FEC, EUR;
	  }
	  
	  public enum FunctionID {
	    CashInHand, IBSOtherBranch, IBSLocalBranch, IBSOtherBank, IBSLocalBank, Cash, RemitOutOnline, RemitInOnline, RemitOutOnlCom, IBT, IBTCom, IBTCom3, FXCOM, PO, POC, FDI, FDA, FDS, OBRDN, OBREN, OBRD, OBRE, OBRComm, ODI, ODS, ODC, ODP, ODNPL, ADJ, GC, GCComm, CYP, CYPEQU, RMTD, RMTE, RMTComm, HRV, HP, HPINT, HPINT1, HPA, HPComm, HPMERIBS, HPPEN, HPODINT, HPODSC, HPMER, HPD, HPSVC, LAI, LAS, LAC, LASUNDRY, LAP, ATMDEP, LASP, POR, CLR, SIACOM, SIF, ALKCOM, SAI, SAA, CLN, SPI, SPA, OBRECOM, RMTEN, RMTDN, OBRDFCOM, HRVCOM, CDI, CDA, SUP, IN, FEC, UAI, UAA, DOR, DORINT, BD, BDCOM, FECSUP, POF, CLRCOM, SIA, BDD, BDDCOM, HPB, CCTTAXCR, CCTTAXCOM1, CCTTAXCOM2, CCTTAXCOM3, IDCP, IDCPI, IBS, IDCPIBS, IDCPSundryDep, IDCPBR, FECI, CCTDs, CCTComms, BBTDR, BBTCR, BBTDSDR, BBTDSCR, CCTIS, CCTISCOM, CCTIDS, CCTIDSCOM, CCTD, CCTComm, CCTE, CCTECOM, CCTDN, CCTEN, PGLCr, PGIntCr, PGAccDr, PGAccCr, CAI, CAA, PGGL1, PGCOM1, PGGL2, CCHQ, ODA, LAA, MERCOM, WTR, WTRT, WTRCOM, WCUS, WM, WMCOM, ODAC, ODNPLB, LANPLB1, LANPLB2, CLNHO, CLRHO, SLI, SLP, SLR, WUD, WUComm, FGDr, FGCr, FGCOM1, FGL, WUPOR, WUE, WUECOM, CCTCAS, REV, REVG, REVL, WUOBRECOM, WUOBRComm, WUOBRE, WUOBRD, IRDS, IRDCOMS, IRD, IRDCOM, FX, DVPD, BATD, BATE, DVPE, DVPSD, DVPSE,LAEXT,LAIM;
	  }
	  
	  public enum RemitDrawingTransType {
	    NrcToNrc, NrcToAccNo, AccNoToAccNo, AccNoToNrc, GLToNrc, GLToAccNo;
	  }
	  
	  public enum MessageType {
	    Info, Success, Warning, Error;
	  }
	  
	  public enum DenoType {
	    Opening, Withdraw, Deposit, TransferFrom, TransferTo, Exchange;
	  }
	  
	  public enum SearchOptionDataType {
	    String, Number, DateTime, None, Time, DataTime;
	  }
	  
	  public enum SuggestType {
	    None, Bank, AllBranch, Branch, CustomerType, AccountStatus, GCStatus, GCType, OthEncashStatus, OtherBank, OtherBranch, POType, FIStatus, SIType, IntType, RMTDrawingStatus, AODFStatus, HPStatus, CollateralInfoStatus, CollateralProductType, CollateralInformationStatus, CounterType, BarType, Currency, LocalBranch, CurCode, ComType, SMSRoleType, OBRDrawingStatus, PBStatus, RecordStatus, Counter, IBTStatus, DenoType, Division, District, Township, AccBarStatus, BankDraftStatus, FIType, AllZone, HPScheduleType, IsUsed, LoanType, TransType, CCTEncashStatus, CCTDrawingStatus, DenoTypeNew, ProductType, AccType, TenureType, TransDesc, BLStatus, PGRStatus, ServiceType, OtherBankRate, OtherBankTestKey, BBTStatus, BBTDSStatus, RageType, CCTInitiationStatus, BATStatus, CusType, DVPSenderStatus, DVPReceiverStatus, AcclinkStatus;
	  }
	  
	  public enum SearchControlType {
	    Auto, SuggestBox, ListBox, MultiCombo;
	  }
	  
	  public enum DataTypeCri {
	    Equal, NotEqual, GreaterThan, GreaterThanEqual, LessThan, LessThanEqual, Contain, BeginWith, EndWith, Between, In;
	  }
	  
	  public enum ScheduleType {
	    Default(
	      0, "Default"),
	    Monthly(1, "Monthly"),
	    Quarterly(3, "Quarterly"),
	    HalfYearly(6, "Half Yearly"),
	    Yearly(12, "Yearly");
	    
	    private final int value;
	    
	    private final String description;
	    
	    public int value() {
	      return this.value;
	    }
	    
	    public String description() {
	      return this.description;
	    }
	    
	    ScheduleType(int aStatus, String desc) {
	      this.value = aStatus;
	      this.description = desc;
	    }
	  }
	  
	  public enum credtPostalAddress {
	    PSSD, CMD, FISD, FMD, AHRD, FEMD;
	  }
	  
	  public enum settlementMethod {
	    INDA, INGA, COVE, CLRG;
	  }

}
