package com.nirvasoft.rp.shared.icbs;

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
	private int hlType = 0;
	private int odType = 0;
	private String AType;
	//mmmyint 
	private String caiType;
	
	public String getAType() {
		return AType;
	}
	public void setAType(String aType) {
		AType = aType;
	}
	public String getBranchCode() {return branchCode;}
	public void setBranchCode(String p) {branchCode = p;}
	
	public String getOtherBranchCode() {return OtherbranchCode;}
	public void setOtherBranchCode(String p) { OtherbranchCode = p;}
	
	public String getCurrencyCode() {return currencyCode;}
	public void setCurrencyCode(String p) {currencyCode = p;}
	
	public String getTocurrencyCode() {return tocurrencyCode;}
	public void setTocurrencyCode(String p) {tocurrencyCode = p;}
	
	public Enum.FunctionID getType() {return fIDType;}
	public void setType(Enum.FunctionID p) {fIDType = p;}
	
	public int getPoType(){return poType;}
	public void setPoType(int p){poType = p;}
	
	public int getGcType(){return gcType;}
	public void setGcType(int p){gcType = p;}
	
	public int getHPType(){return hpType;}
	public void setHPType(int p){hpType = p;}
	
	public int getLaType() {return laType;}
	public void setLaType(int p){laType = p;}
	
	public int getOdType() {return odType;}
	public void setOdType(int p){odType = p;}
	public String getCAIType() {
		return caiType;
	}
	public void setCAIType(String caiType) {
		this.caiType = caiType;
	}
	public int getHLType() {
		return hlType;
	}
	public void setHLType(int hlType) {
		this.hlType = hlType;
	}
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getCommission() {
		return pCommission;
	}
	public void setCommission(String pCommission) {
		this.pCommission = pCommission;
	}

	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	
	public FunctionID() {
		clearProperty();
	}
	
	public FunctionID(String currencyCode, String branchCode, Enum.FunctionID fIDtType) {
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
		branchCode = "";
		currencyCode = "";
		tocurrencyCode = "";
		Tenure="";
		poType = 0;
		bankCode = "";
		pCommission = "";
		gcType = 0;
		saiType = "";
		glCode = "";
		OtherbranchCode = "";
		AccPType = "";
		laType = 0;
		AType="";
	}
	
	public String getTenure() {
		return Tenure;
	}
	public void setTenure(String tenure) {
		Tenure = tenure;
	}
		
	public String getSAIType() {
		return saiType;
	}
	public void setSAIType(String saiType) {
		this.saiType = saiType;
	}
	public String getAccPType() {
		return AccPType;
	}
	public void setAccPType(String accPType) {
		AccPType = accPType;
	}
	public String getFunctionID() {
		String ret = "";
		if (fIDType == Enum.FunctionID.IBT) {
			ret = "IBT"+ AType + currencyCode + branchCode+AccPType ;
		} else if (fIDType == Enum.FunctionID.IBTCom3) {
			ret = "IBTCOM"+ pCommission + currencyCode + branchCode;
		} else if (fIDType == Enum.FunctionID.POC) {
			ret = "POC" + poType+ currencyCode + branchCode ;
		} else if (fIDType == Enum.FunctionID.POF) {
			ret = "POF" + poType+ currencyCode + branchCode ;
		}else if (fIDType == Enum.FunctionID.PO){
			ret = "PO"+ poType + currencyCode +branchCode;
		} else if(fIDType==Enum.FunctionID.FDI) {
			ret="FDI" + AccPType +  Tenure + AType + currencyCode + branchCode;
		} else if(fIDType==Enum.FunctionID.FDA) {
			ret="FDA"+ AccPType + Tenure+currencyCode + branchCode;
		} else if(fIDType==Enum.FunctionID.FDS) {
			ret="FDS"+ AccPType + Tenure+currencyCode + branchCode;
		}else if(fIDType ==  Enum.FunctionID.OBRDN) {
			ret = "OBRDN" + currencyCode + bankCode + branchCode;
		}else if(fIDType ==  Enum.FunctionID.OBREN) {
			ret = "OBREN" + currencyCode + bankCode + branchCode;
		}else if(fIDType ==  Enum.FunctionID.OBRD) {
			ret = "OBRD" + currencyCode + bankCode + branchCode;
		}else if(fIDType == Enum.FunctionID.OBRE) {
			ret = "OBRE" + currencyCode + bankCode + branchCode;
		}else if(fIDType == Enum.FunctionID.OBRComm) {
			ret = "OBR" + pCommission + currencyCode  + bankCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODI){
			ret = "ODI" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODS){
			ret = "ODS" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODEXT){
			ret = "ODEXT" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODC){
			ret = "ODC" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODSUNDRY){
			ret = "ODSUNDRY" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODP){
			ret = "ODP" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODL){
			ret = "ODL" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ADJ){
			ret = "ADJ" + currencyCode + branchCode;
		}else if (fIDType == Enum.FunctionID.GCComm) {
			ret = "GCCOM" + gcType + currencyCode + branchCode;
		}else if (fIDType == Enum.FunctionID.GC){
			ret = "GC" + gcType + currencyCode + branchCode ;
		}else if (fIDType == Enum.FunctionID.CYP) {
			ret = "CYP" +currencyCode+ tocurrencyCode+AccPType + branchCode;//+ toCurrency 
		}else if (fIDType == Enum.FunctionID.CYPEQU) {
			ret = "CYPEQU" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.RMTD) {
			ret = "RMTD" + currencyCode + branchCode + OtherbranchCode;
		}else if(fIDType == Enum.FunctionID.RMTDN) {
			ret = "RMTDN" + currencyCode + branchCode + OtherbranchCode;
		}else if(fIDType == Enum.FunctionID.RMTE) {
			ret = "RMTE" + currencyCode + branchCode + OtherbranchCode  ;
		}else if(fIDType == Enum.FunctionID.RMTEN) {
			ret = "RMTEN" + currencyCode + branchCode + OtherbranchCode ;
		}	
		else if(fIDType == Enum.FunctionID.RMTComm) {
			ret = "RMT"+ pCommission + currencyCode + branchCode + OtherbranchCode;
		}else if(fIDType == Enum.FunctionID.HP) {
			ret = "HP" + hpType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HPD) {
			ret = "HPD" + hpType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HPINT) {
			ret = "HPINT" + hpType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HPINT1) {
			ret = "HPI" + hpType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HPA) {
			ret = "HPA" + hpType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HPComm) {
			ret = "HPCOM" + hpType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HPPEN) {
			ret = "HPPEN" + hpType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HPMER) {
			ret = "HPMER" + hpType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HPMERIBS) {
			ret = "HPMERIBS" + hpType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HPSVC) {
			ret = "HPSVC" + hpType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HPODINT) {
			ret = "HPODINT" + hpType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HPODSC) {
			ret = "HPODSC" + hpType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAA){
			ret = "LAA" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAI){
			if(laType==0)
				ret = "LAI" + currencyCode + branchCode;
			else
				ret = "LAI" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAIM){
			ret = "LAIM" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAAM){
			ret = "LAAM" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAS){
			ret = "LAS"+ laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAEXT){
			ret = "LAEXT"+ laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAC){
			ret = "LAC" + currencyCode + branchCode;	
		}else if(fIDType == Enum.FunctionID.LASUNDRY){
			if(laType==0)
				ret = "LASUNDRY" + currencyCode + branchCode;
			else
				ret = "LASUNDRY" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAMSUNDRY){
			if(laType==0)
				ret = "LAMSUNDRY" + currencyCode + branchCode;
			else
				ret = "LAMSUNDRY" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LASP){
			ret = "LASP" + laType + currencyCode + branchCode;	
		}else if(fIDType == Enum.FunctionID.ATMDEP){
			ret = "ATMDEP" + branchCode;
		}else if(fIDType == Enum.FunctionID.POR) {
			ret = "POR"+ currencyCode+ poType + bankCode + branchCode;
		}else if(fIDType == Enum.FunctionID.CLR) {
			ret = "CLR"+ currencyCode+ branchCode ;
		}else if(fIDType == Enum.FunctionID.SIACOM) {
			ret = "SIACOM" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.SIF) {
			ret = "SIF" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ALKCOM) {
			ret = "ALKCOM" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.SAI) {
			ret = "SAI" + saiType + AccPType + AType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.SAA){
			ret = "SAA" + saiType + AccPType + AType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.CLN){
			ret = "CLN" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODNPL){
			ret = "ODNPL" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODANPL){
			ret = "ODANPL" + odType + currencyCode + branchCode;
		}else if (fIDType == Enum.FunctionID.IBTCom) {
			ret = "IBTCOM" + pCommission + currencyCode + branchCode;
		} else if (fIDType == Enum.FunctionID.OBRECOM) {
			ret = "OBRECOM" + currencyCode + bankCode +branchCode;
		} else if (fIDType == Enum.FunctionID.OBRDFCOM) {
			ret = "OBRDFCOM" + currencyCode + bankCode +branchCode;
		 }else if(fIDType == Enum.FunctionID.SPI) {
			ret = "SPI" + currencyCode + branchCode;
		} else if(fIDType == Enum.FunctionID.SPA){
			ret = "SPA" + currencyCode + branchCode;
		}else if (fIDType == Enum.FunctionID.CDI) {
			ret = "CDI" + AccPType + currencyCode + branchCode;
		}else if (fIDType == Enum.FunctionID.CDA) {
			ret = "CDA" + AccPType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LAP){
			ret= "LAP" + odType + currencyCode +branchCode;
		}else if(fIDType == Enum.FunctionID.UAI) {
			ret = "UAI" + saiType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.UAA){
			ret = "UAA" + saiType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.DOR){
			ret = "DOR" +AccPType+ currencyCode+branchCode;
		}else if(fIDType == Enum.FunctionID.DORINT){
			ret = "DORINT" + AccPType+currencyCode+branchCode;
		}else if(fIDType == Enum.FunctionID.BD){
			ret = "BD" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.BDCOM){
			ret = "BD" + pCommission + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.BDD){
			ret = "BDD" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.BDDCOM){
			ret = "BDD" + pCommission + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.FECSUP){	//nlkm
			ret= "FEC" + "SUP" +currencyCode+ OtherbranchCode + branchCode;
		}else if(fIDType == Enum.FunctionID.FECI){
			ret= "FEC" + "I" +currencyCode+ OtherbranchCode + branchCode;
		}else if(fIDType == Enum.FunctionID.SIA){
			ret= "SIA" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HPB){
			ret= "HPB" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.IDCP){
			ret= "IDCP" + currencyCode + branchCode+AccPType;
		}else if(fIDType == Enum.FunctionID.IDCPI){
			ret= "IDCPI" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.IBS){
			ret= "IBS" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.IDCPIBS){
			ret= "IDCPIBS" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.IDCPSundryDep){
			ret= "IDCPS" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.IDCPBR){
			ret= "IDCPBR" + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGAccDr){
			ret="PGD" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGAccCr){
			ret="PGC" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGIntCr){
			ret="PG" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGCOM1){
			ret="PGCOM1" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGLCr){
			ret="PGL" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.CCTD){
			ret = "CCTD"+ AccPType +currencyCode+ bankCode + branchCode;
		}else if (fIDType==Enum.FunctionID.CCTComm){
			ret = "CCT" + AccPType+ pCommission + currencyCode  + bankCode + branchCode;
		}else if (fIDType==Enum.FunctionID.CCTE){
			 ret = "CCTE"+ AccPType+  currencyCode+ bankCode + branchCode;
		}else if (fIDType==Enum.FunctionID.CCTECOM){
			ret = "CCTE"+ AccPType +"COM" + currencyCode  + bankCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGGL1){
			ret="PGGL1" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.PGGL2){
			ret="PGGL2" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.ODA){
			ret="ODA" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.MERCOM){
			ret = "MERCOM" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.WTR){
			ret = "WTR" + branchCode;
		}else if(fIDType == Enum.FunctionID.WTRT){
			ret = "WTR" + AType + branchCode;
		}else if(fIDType == Enum.FunctionID.WTRCOM){
			ret = "WTRCOM" + AType + currencyCode  + branchCode;
		}else if(fIDType == Enum.FunctionID.ODAC){
			ret = "ODAC" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODAL){
			ret = "ODAL" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.CLNHO){
			ret = "CLNHO" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.CLRHO){
			ret = "CLRHO" + currencyCode + branchCode;

		}else if(fIDType ==  Enum.FunctionID.WUD) {
			ret = "WUD" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.WUComm) {
			ret = "WUD" + pCommission + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.WUE) {
			ret = "WUE" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.WUECOM) {
			ret = "WUECOM" + currencyCode + branchCode;
		} else if (fIDType==Enum.FunctionID.CCTDs){
			ret = "CCTDS"+ AccPType+  currencyCode+ bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CCTComms){
        	ret = "CCTS" + AccPType + pCommission + currencyCode  + bankCode + branchCode;
        }//wlt
        else if (fIDType==Enum.FunctionID.BBTCR){
            ret = "BBTCR"  + currencyCode + bankCode  + branchCode;
        }//wlt
        else if (fIDType==Enum.FunctionID.BBTDR){
            ret = "BBTDR"  + currencyCode + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.BBTDSDR){
            ret = "BBTDSDR"  + currencyCode + bankCode  + branchCode;
        }//wlt
        else if (fIDType==Enum.FunctionID.BBTDSCR){
            ret = "BBTDSCR"  + currencyCode + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CCTIS){//wlt
            ret = "CCTIS"+ currencyCode+ bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CCTISCOM){
            ret = "CCTIS" + pCommission + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CCTIDS){
            ret = "CCTIDS"+ currencyCode+ bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CCTIDSCOM){
        	ret = "CCTIDS" + pCommission + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CCTCAS) {
        	ret = "CCTCAS"  + currencyCode + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.CDDCAS) {
        	ret = "CDDCAS"  + currencyCode + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.REV) {
        	ret = "REV"  + AType+currencyCode + branchCode;
        }else if (fIDType==Enum.FunctionID.REVG) {
        	ret = "REVG"  + AType+currencyCode + branchCode;
        }else if (fIDType==Enum.FunctionID.REVL) {
        	ret = "REVL" + AType+ currencyCode + branchCode;
        }else if (fIDType==Enum.FunctionID.IRDS){
            ret = "IRDS"+ currencyCode+ bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.IRD){
        	ret = "IRD"+ AccPType + currencyCode+ bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.IRDCOMS){
        	ret = "IRDS" + AccPType + pCommission + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.IRDCOM){
        	ret = "IRD" + AccPType + pCommission + currencyCode  + bankCode + branchCode;
        }else if (fIDType == Enum.FunctionID.FX) {
			ret = "FX" + currencyCode + branchCode+AccPType ;
		}else if (fIDType==Enum.FunctionID.BATD){
            ret = "BATD"+ currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.BATC){
            ret = "BATC"+ AccPType + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.BATED){
            ret = "BATED"+ AccPType + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.BATEC){
            ret = "BATEC" + currencyCode  + bankCode + branchCode;
        } else if (fIDType == Enum.FunctionID.BATDI) {
			ret = "BATDI" + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.BATP) {
			ret = "BATP" + currencyCode + bankCode + branchCode;
		}else if(fIDType == Enum.FunctionID.CAI) {
        	ret = "CAI" + caiType + AccPType + AType + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.DVPD){
            ret = "DVPD"+ currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.DVPE){
            ret = "DVPE" + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.DVPSD){
            ret = "DVPDS"+ AccPType + currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.DVPSE){
            ret = "DVPES"+ currencyCode  + bankCode + branchCode;
        }else if (fIDType==Enum.FunctionID.FGDr){
			ret="FGD" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.FGCr){
			ret="FGC" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.FGCOM1){
			ret="FGCOM" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.FGL){
			ret="FGL" + Tenure + currencyCode + branchCode;
		}else if (fIDType==Enum.FunctionID.FXCOM){
			ret="FXCOM" + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HL) {		//nlkm 06042017
			ret = "HL" + hlType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.HLINT) {
			ret = "HLI" + hlType + currencyCode + branchCode ;
		}else if(fIDType == Enum.FunctionID.HLPEN) {
			ret = "HLPEN" + hlType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.TMF) {//tzo
			ret = "TMF"+ currencyCode+ branchCode ;//TMF001MMK
		}else if(fIDType == Enum.FunctionID.TMFCOM) {//tzo
			ret = "TMFCOM"+ currencyCode+ branchCode ;//TMFCOM001MMK
		}else if (fIDType==Enum.FunctionID.CCTTAXCR) {
       	 ret = "CCTTAXCR" + currencyCode  + branchCode;
       } else if (fIDType==Enum.FunctionID.CCTTAXCOM1) {
      	 	ret = "CCTTAXCOM1" + currencyCode  + branchCode;
       }else if (fIDType==Enum.FunctionID.CCTTAXCOM2) {
      	 	ret = "CCTTAXCOM2" + currencyCode  + branchCode;
       }else if (fIDType==Enum.FunctionID.CCTTAXCOM3) {
      	 	ret = "CCTTAXCOM3" + currencyCode  + branchCode;
       }else if (fIDType==Enum.FunctionID.LAL) {
    	   ret = "LAL"+ laType + currencyCode + branchCode;
       }else if (fIDType==Enum.FunctionID.LAAL) {
    	   ret = "LAAL"+ laType + currencyCode + branchCode;
       }else if (fIDType==Enum.FunctionID.CYPS) {
    	   ret = "CYPS"+ currencyCode + branchCode;
       }else if (fIDType == Enum.FunctionID.DVPDr) {
			ret = "DVPDR" + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.DVPCr) {
			ret = "DVPCR" + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.DVPDI) {
			ret = "DVPDI" + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.DVPDA) {
			ret = "DVPDA" + currencyCode + bankCode + branchCode;
		} else if (fIDType == Enum.FunctionID.DVPEDr) {
			ret = "DVPEDr" + AccPType + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.DVPECr) {
			ret = "DVPECr" + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.DVPEI) {
			ret = "DVPEI" + currencyCode + bankCode + branchCode;
		}else if (fIDType == Enum.FunctionID.DVPEA) {
			ret = "DVPEA" + currencyCode + bankCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODSUNDRY){
		    ret = "ODSUNDRY" + odType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.LASUNDEP){
		    ret = "LASUNDEP" + laType + currencyCode + branchCode;
		}else if(fIDType == Enum.FunctionID.ODSUNDEP){
		    ret = "ODSUNDEP" + odType + currencyCode + branchCode;
		}
		return ret;
	}
	
}
