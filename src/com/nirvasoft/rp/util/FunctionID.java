package com.nirvasoft.rp.util;
import com.nirvasoft.rp.shared.icbs.Enum;

public class FunctionID {
	  public static final int CashInHand = 0;
	  
	  public static final int RemitOutOnLine = 10000;
	  
	  public static final int RemitInOnLine = 10500;
	  
	  public static final int RemitOutOnlCom = 11000;
	  
	  public static final int RemitReceived = 250;
	  
	  public static final int CashPO = 2000;
	  
	  private String branchCode;
	  
	  private String OtherbranchCode;
	  
	  private String currencyCode;
	  
	  private String tocurrencyCode;
	  
	  private String Tenure;
	  
	  private Enum.FunctionID fIDType;
	  
	  private int poType;
	  
	  private String bankCode;
	  
	  private String pCommission;
	  
	  private int gcType = 0;
	  
	  private int hpType = 0;
	  
	  private int laType = 0;
	  
	  private String saiType;
	  
	  private String glCode;
	  
	  private String AccPType;
	  
	  private int odType = 0;
	  
	  private String AType;
	  
	  private String caiType;
	  
	  public String getAType() {
	    return this.AType;
	  }
	  
	  public void setAType(String aType) {
	    this.AType = aType;
	  }
	  
	  public String getBranchCode() {
	    return this.branchCode;
	  }
	  
	  public void setBranchCode(String p) {
	    this.branchCode = p;
	  }
	  
	  public String getOtherBranchCode() {
	    return this.OtherbranchCode;
	  }
	  
	  public void setOtherBranchCode(String p) {
	    this.OtherbranchCode = p;
	  }
	  
	  public String getCurrencyCode() {
	    return this.currencyCode;
	  }
	  
	  public void setCurrencyCode(String p) {
	    this.currencyCode = p;
	  }
	  
	  public String getTocurrencyCode() {
	    return this.tocurrencyCode;
	  }
	  
	  public void setTocurrencyCode(String p) {
	    this.tocurrencyCode = p;
	  }
	  
	  public Enum.FunctionID getType() {
	    return this.fIDType;
	  }
	  
	  public void setType(Enum.FunctionID p) {
	    this.fIDType = p;
	  }
	  
	  public int getPoType() {
	    return this.poType;
	  }
	  
	  public void setPoType(int p) {
	    this.poType = p;
	  }
	  
	  public int getGcType() {
	    return this.gcType;
	  }
	  
	  public void setGcType(int p) {
	    this.gcType = p;
	  }
	  
	  public int getHPType() {
	    return this.hpType;
	  }
	  
	  public void setHPType(int p) {
	    this.hpType = p;
	  }
	  
	  public int getLaType() {
	    return this.laType;
	  }
	  
	  public void setLaType(int p) {
	    this.laType = p;
	  }
	  
	  public int getOdType() {
	    return this.odType;
	  }
	  
	  public void setOdType(int p) {
	    this.odType = p;
	  }
	  
	  public String getBankCode() {
	    return this.bankCode;
	  }
	  
	  public void setBankCode(String bankCode) {
	    this.bankCode = bankCode;
	  }
	  
	  public String getCommission() {
	    return this.pCommission;
	  }
	  
	  public void setCommission(String pCommission) {
	    this.pCommission = pCommission;
	  }
	  
	  public String getGlCode() {
	    return this.glCode;
	  }
	  
	  public void setGlCode(String glCode) {
	    this.glCode = glCode;
	  }
	  
	  public FunctionID() {
	    clearProperty();
	  }
	  
	  public FunctionID(String branchCode, String currencyCode, Enum.FunctionID fIDtType) {
	    this.branchCode = branchCode;
	    this.currencyCode = currencyCode;
	    this.fIDType = fIDtType;
	  }
	  
	  public FunctionID(String branchCode, String currencyCode, String currencyCodeTo, Enum.FunctionID fIDtType) {
	    this.branchCode = branchCode;
	    this.currencyCode = currencyCode;
	    this.tocurrencyCode = currencyCodeTo;
	    this.fIDType = fIDtType;
	    this.AccPType = "";
	  }
	  
	  public void clearProperty() {
	    this.branchCode = "";
	    this.currencyCode = "";
	    this.tocurrencyCode = "";
	    this.Tenure = "";
	    this.poType = 0;
	    this.bankCode = "";
	    this.pCommission = "";
	    this.gcType = 0;
	    this.saiType = "";
	    this.glCode = "";
	    this.OtherbranchCode = "";
	    this.AccPType = "";
	    this.laType = 0;
	    this.AType = "";
	  }
	  
	  public String getTenure() {
	    return this.Tenure;
	  }
	  
	  public void setTenure(String tenure) {
	    this.Tenure = tenure;
	  }
	  
	  public String getSAIType() {
	    return this.saiType;
	  }
	  
	  public void setSAIType(String saiType) {
	    this.saiType = saiType;
	  }
	  
	  public String getAccPType() {
	    return this.AccPType;
	  }
	  
	  public void setAccPType(String accPType) {
	    this.AccPType = accPType;
	  }
	  
	  public String getFunctionID() {
	    String ret = "";
	    if (this.fIDType == Enum.FunctionID.IBT) {
	      ret = "IBT" + this.currencyCode + this.branchCode + this.AccPType;
	    } else if (this.fIDType == Enum.FunctionID.IBTCom3) {
	      ret = "IBTCOM" + this.pCommission + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.POC) {
	      ret = "POC" + this.poType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.POF) {
	      ret = "POF" + this.poType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PO) {
	      ret = "PO" + this.poType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FDI) {
	      ret = "FDI" + this.AccPType + this.Tenure + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FDA) {
	      ret = "FDA" + this.AccPType + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FDS) {
	      ret = "FDS" + this.AccPType + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBRDN) {
	      ret = "OBRDN" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBREN) {
	      ret = "OBREN" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBRD) {
	      ret = "OBRD" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBRE) {
	      ret = "OBRE" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBRComm) {
	      ret = "OBR" + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODI) {
	      ret = "ODI" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODS) {
	      ret = "ODS" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODC) {
	      ret = "ODC" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODP) {
	      ret = "ODP" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ADJ) {
	      ret = "ADJ" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.GCComm) {
	      ret = "GCCOM" + this.gcType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.GC) {
	      ret = "GC" + this.gcType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CYP) {
	      ret = "CYP" + this.currencyCode + this.tocurrencyCode + this.AccPType + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CYPEQU) {
	      ret = "CYPEQU" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.RMTD) {
	      ret = "RMTD" + this.currencyCode + this.branchCode + this.OtherbranchCode;
	    } else if (this.fIDType == Enum.FunctionID.RMTDN) {
	      ret = "RMTDN" + this.currencyCode + this.branchCode + this.OtherbranchCode;
	    } else if (this.fIDType == Enum.FunctionID.RMTE) {
	      ret = "RMTE" + this.currencyCode + this.branchCode + this.OtherbranchCode;
	    } else if (this.fIDType == Enum.FunctionID.RMTEN) {
	      ret = "RMTEN" + this.currencyCode + this.branchCode + this.OtherbranchCode;
	    } else if (this.fIDType == Enum.FunctionID.RMTComm) {
	      ret = "RMT" + this.pCommission + this.currencyCode + this.branchCode + this.OtherbranchCode;
	    } else if (this.fIDType == Enum.FunctionID.HP) {
	      ret = "HP" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPD) {
	      ret = "HPD" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPINT) {
	      ret = "HPINT" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPINT1) {
	      ret = "HPI" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPA) {
	      ret = "HPA" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPComm) {
	      ret = "HPCOM" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPPEN) {
	      ret = "HPPEN" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPMER) {
	      ret = "HPMER" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPMERIBS) {
	      ret = "HPMERIBS" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPSVC) {
	      ret = "HPSVC" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPODINT) {
	      ret = "HPODINT" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPODSC) {
	      ret = "HPODSC" + this.hpType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LAA) {
	      ret = "LAA" + this.laType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LAI) {
	      ret = "LAI" + this.laType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LAS) {
	      ret = "LAS" + this.laType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LAC) {
	      ret = "LAC" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LASUNDRY) {
	      ret = "LASUNDRY" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LASP) {
	      ret = "LASP" + this.laType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ATMDEP) {
	      ret = "ATMDEP" + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.POR) {
	      ret = "POR" + this.currencyCode + this.poType + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CLR) {
	      ret = "CLR" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SIACOM) {
	      ret = "SIACOM" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SIF) {
	      ret = "SIF" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ALKCOM) {
	      ret = "ALKCOM" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SAI) {
	      ret = "SAI" + this.saiType + this.AccPType + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SAA) {
	      ret = "SAA" + this.saiType + this.AccPType + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CLN) {
	      ret = "CLN" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODNPL) {
	      ret = "ODNPL" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IBTCom) {
	      ret = "IBTCOM" + this.pCommission + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBRECOM) {
	      ret = "OBRECOM" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.OBRDFCOM) {
	      ret = "OBRDFCOM" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SPI) {
	      ret = "SPI" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SPA) {
	      ret = "SPA" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CDI) {
	      ret = "CDI" + this.AccPType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CDA) {
	      ret = "CDA" + this.AccPType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.LAP) {
	      ret = "LAP" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.UAI) {
	      ret = "UAI" + this.saiType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.UAA) {
	      ret = "UAA" + this.saiType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.DOR) {
	      ret = "DOR" + this.AccPType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.DORINT) {
	      ret = "DORINT" + this.AccPType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BD) {
	      ret = "BD" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BDCOM) {
	      ret = "BD" + this.pCommission + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BDD) {
	      ret = "BDD" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BDDCOM) {
	      ret = "BDD" + this.pCommission + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FECSUP) {
	      ret = "FECSUP" + this.currencyCode + this.OtherbranchCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FECI) {
	      ret = "FECI" + this.currencyCode + this.OtherbranchCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.SIA) {
	      ret = "SIA" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.HPB) {
	      ret = "HPB" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IDCP) {
	      ret = "IDCP" + this.currencyCode + this.branchCode + this.AccPType;
	    } else if (this.fIDType == Enum.FunctionID.IDCPI) {
	      ret = "IDCPI" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IBS) {
	      ret = "IBS" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IDCPIBS) {
	      ret = "IDCPIBS" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IDCPSundryDep) {
	      ret = "IDCPS" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IDCPBR) {
	      ret = "IDCPBR" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGAccDr) {
	      ret = "PGD" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGAccCr) {
	      ret = "PGC" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGIntCr) {
	      ret = "PG" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGCOM1) {
	      ret = "PGCOM1" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGLCr) {
	      ret = "PGL" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTD) {
	      ret = "CCTD" + this.AccPType + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTComm) {
	      ret = "CCT" + this.AccPType + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTE) {
	      ret = "CCTE" + this.AccPType + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTECOM) {
	      ret = "CCTE" + this.AccPType + "COM" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGGL1) {
	      ret = "PGGL1" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.PGGL2) {
	      ret = "PGGL2" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODA) {
	      ret = "ODA" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.MERCOM) {
	      ret = "MERCOM" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WTR) {
	      ret = "WTR" + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WTRT) {
	      ret = "WTR" + this.AType + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WTRCOM) {
	      ret = "WTRCOM" + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.ODAC) {
	      ret = "ODAC" + this.odType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CLNHO) {
	      ret = "CLNHO" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CLRHO) {
	      ret = "CLRHO" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WUD) {
	      ret = "WUD" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WUComm) {
	      ret = "WUD" + this.pCommission + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WUE) {
	      ret = "WUE" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.WUECOM) {
	      ret = "WUECOM" + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTDs) {
	      ret = "CCTDS" + this.AccPType + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTComms) {
	      ret = "CCTS" + this.AccPType + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BBTCR) {
	      ret = "BBTCR" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BBTDR) {
	      ret = "BBTDR" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BBTDSDR) {
	      ret = "BBTDSDR" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.BBTDSCR) {
	      ret = "BBTDSCR" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTIS) {
	      ret = "CCTIS" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTISCOM) {
	      ret = "CCTIS" + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTIDS) {
	      ret = "CCTIDS" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTIDSCOM) {
	      ret = "CCTIDS" + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CCTCAS) {
	      ret = "CCTCAS" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.REV) {
	      ret = "REV" + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.REVG) {
	      ret = "REVG" + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.REVL) {
	      ret = "REVL" + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IRDS) {
	      ret = "IRDS" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IRD) {
	      ret = "IRD" + this.AccPType + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IRDCOMS) {
	      ret = "IRDS" + this.AccPType + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.IRDCOM) {
	      ret = "IRD" + this.AccPType + this.pCommission + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FX) {
	      ret = "FX" + this.currencyCode + this.branchCode + this.AccPType;
	    } else if (this.fIDType == Enum.FunctionID.BATD) {
	      ret = "BATD" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.CAI) {
	      ret = "CAI" + this.caiType + this.AccPType + this.AType + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.DVPD) {
	      ret = "DVPD" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.DVPE) {
	      ret = "DVPE" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.DVPSD) {
	      ret = "DVPDS" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.DVPSE) {
	      ret = "DVPES" + this.currencyCode + this.bankCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FGDr) {
	      ret = "FGD" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FGCr) {
	      ret = "FGC" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FGCOM1) {
	      ret = "FGCOM" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FGL) {
	      ret = "FGL" + this.Tenure + this.currencyCode + this.branchCode;
	    } else if (this.fIDType == Enum.FunctionID.FXCOM) {
	      ret = "FXCOM" + this.currencyCode + this.branchCode;
	    } 
	    return ret;
	  }
	  
	  public String getCAIType() {
	    return this.caiType;
	  }
	  
	  public void setCAIType(String caiType) {
	    this.caiType = caiType;
	  }

}
