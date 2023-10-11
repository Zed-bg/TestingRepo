// 
// Decompiled by Procyon v0.5.36
// 

package com.nirvasoft.rp.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class CMSModuleConfigData implements Serializable
{
    private static final long serialVersionUID = -6874575197721414809L;
    private long AutoKey;
    private String SettingType;
    private String BankName;
    private int ActiveBank;
    private int AccLength;
    private int GLLength;
    private int BranchCodeStart;
    private int BranchCodeLength;
    private String AccClassSource;
    private String IsOtherParty;
    private String QRCode;
    private String DBSource;
    private String CashGL;
    private String XREFPrefix;
    private String Remark;
    private int n1;
    private int n2;
    private int n3;
    private String t1;
    private String t2;
    private String t3;
    private String OLXREFprefix;
    private int CNPTransType;
    
    public CMSModuleConfigData() {
        this.SettingType = "";
        this.BankName = "";
        this.ActiveBank = 0;
        this.AccLength = 0;
        this.GLLength = 0;
        this.BranchCodeStart = 0;
        this.BranchCodeLength = 0;
        this.AccClassSource = "";
        this.IsOtherParty = "";
        this.QRCode = "";
        this.DBSource = "";
        this.CashGL = "";
        this.XREFPrefix = "";
        this.Remark = "";
        this.n1 = 0;
        this.n2 = 0;
        this.n3 = 0;
        this.t1 = "";
        this.t2 = "";
        this.t3 = "";
        this.OLXREFprefix = "";
        this.CNPTransType = 0;
    }
    
    public long getAutoKey() {
        return this.AutoKey;
    }
    
    public void setAutoKey(final long autoKey) {
        this.AutoKey = autoKey;
    }
    
    public String getSettingType() {
        return this.SettingType;
    }
    
    public void setSettingType(final String settingType) {
        this.SettingType = settingType;
    }
    
    public String getBankName() {
        return this.BankName;
    }
    
    public void setBankName(final String bankName) {
        this.BankName = bankName;
    }
    
    public int getActiveBank() {
        return this.ActiveBank;
    }
    
    public void setActiveBank(final int activeBank) {
        this.ActiveBank = activeBank;
    }
    
    public int getAccLength() {
        return this.AccLength;
    }
    
    public void setAccLength(final int accLength) {
        this.AccLength = accLength;
    }
    
    public int getGLLength() {
        return this.GLLength;
    }
    
    public void setGLLength(final int gLLength) {
        this.GLLength = gLLength;
    }
    
    public int getBranchCodeStart() {
        return this.BranchCodeStart;
    }
    
    public void setBranchCodeStart(final int branchCodeStart) {
        this.BranchCodeStart = branchCodeStart;
    }
    
    public int getBranchCodeLength() {
        return this.BranchCodeLength;
    }
    
    public void setBranchCodeLength(final int branchCodeLength) {
        this.BranchCodeLength = branchCodeLength;
    }
    
    public String getAccClassSource() {
        return this.AccClassSource;
    }
    
    public void setAccClassSource(final String accClassSource) {
        this.AccClassSource = accClassSource;
    }
    
    public String getIsOtherParty() {
        return this.IsOtherParty;
    }
    
    public void setIsOtherParty(final String isOtherParty) {
        this.IsOtherParty = isOtherParty;
    }
    
    public String getQRCode() {
        return this.QRCode;
    }
    
    public void setQRCode(final String qRCode) {
        this.QRCode = qRCode;
    }
    
    public String getDBSource() {
        return this.DBSource;
    }
    
    public void setDBSource(final String dBSource) {
        this.DBSource = dBSource;
    }
    
    public String getCashGL() {
        return this.CashGL;
    }
    
    public void setCashGL(final String cashGL) {
        this.CashGL = cashGL;
    }
    
    public String getRemark() {
        return this.Remark;
    }
    
    public void setRemark(final String remark) {
        this.Remark = remark;
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
    
    public String getXREFPrefix() {
        return this.XREFPrefix;
    }
    
    public void setXREFPrefix(final String XREFPrefix) {
        this.XREFPrefix = XREFPrefix;
    }
    
    public String getOLXREFprefix() {
        return this.OLXREFprefix;
    }
    
    public void setOLXREFprefix(final String OLXREFprefix) {
        this.OLXREFprefix = OLXREFprefix;
    }
    
    public void setCNPTransType(final int CNPTransType) {
        this.CNPTransType = CNPTransType;
    }
    
    public int getCNPTransType() {
        return this.CNPTransType;
    }
}
