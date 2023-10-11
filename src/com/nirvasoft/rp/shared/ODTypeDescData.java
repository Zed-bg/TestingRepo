package com.nirvasoft.rp.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ODTypeDescData implements Serializable{

/**
 * 
 */
private static final long serialVersionUID = 3758519663382888391L;

private String accType;
private String odtypedesc;
private String pT3;
private String pT4;
private String pT5;
private int odtype;
private int pYearsofLife;
private int pUnchangeED;
private int freeSC;
private int pN5;
private int scheduleFormula;//SMM
private int gracePeriod;
private int interest;
private int scheduleMonth;
private int transcation;	
private int chkschedule;	
private int roundingby;	
private int firstMonth;
private int monthEnd;
private int expiredType;
private int intcollectType;
private int changeExpDate;
private int sameBackAccount;
private int allowByForce;
private int shortage;

public String getAccType() {
	return accType;
}
public int getFreeSC() {
	return freeSC;
}
public void setFreeSC(int freeSC) {
	this.freeSC = freeSC;
}
public void setAccType(String accType) {
	this.accType = accType;
}
public String getOdtypedesc() {
	return odtypedesc;
}
public void setOdtypedesc(String odtypedesc) {
	this.odtypedesc = odtypedesc;
}
public String getT3() {
	return pT3;
}
public void setT3(String t3) {
	pT3 = t3;
}
public String getT4() {
	return pT4;
}
public void setT4(String t4) {
	pT4 = t4;
}
public String getT5() {
	return pT5;
}
public void setT5(String t5) {
	pT5 = t5;
}	
public int getOdtype() {
	return odtype;
}
public void setOdtype(int ODtype) {
	this.odtype = ODtype;
}
public int getYearsofLife() {
	return pYearsofLife;
}
public void setYearsofLife(int n2) {
	pYearsofLife = n2;
}
public int getUnchangeED() {
	return pUnchangeED;
}
public void setUnchangeED(int n3) {
	pUnchangeED = n3;
}
//public int getFreeSC() {
//	return pFreeSC;
//}
//public void setFreeSC(int pFreeSC) {
//	this.pFreeSC = pFreeSC;
//}
public int getpN5() {
	return pN5;
}
public void setpN5(int pN5) {
	this.pN5 = pN5;
}

public int getChkschedule() {
	return chkschedule;
}
public void setChkschedule(int chkschedule) {
	this.chkschedule = chkschedule;
}
public int getRoundingby() {
	return roundingby;
}
public void setRoundingby(int roundingby) {
	this.roundingby = roundingby;
}
private void clearproperty(){
	accType = "";
	odtypedesc = "";
	pT3 = "";
	pT4 = "";
	pT5 = "";
	odtype = -1;//default
	pYearsofLife = 0;
	pUnchangeED = 0;
	freeSC = 0;
	pN5 = 0;
	scheduleFormula = 0;
	gracePeriod = 0;
	interest = 0;
	scheduleMonth = 0;
	transcation = 0;
	chkschedule=0;
	roundingby=0;
	firstMonth=0;
	intcollectType=0;
	changeExpDate = 0;
	sameBackAccount =0;
	allowByForce = 0;
	shortage = 0;
}

public ODTypeDescData(){
	clearproperty();
}
public int getInterest() {
	return interest;
}
public void setInterest(int interest) {
	this.interest = interest;
}
public int getScheduleFormula() {
	return scheduleFormula;
}
public void setScheduleFormula(int scheduleFormula) {
	this.scheduleFormula = scheduleFormula;
}
public int getGracePeriod() {
	return gracePeriod;
}
public void setGracePeriod(int gracePeriod) {
	this.gracePeriod = gracePeriod;
}
public int getTranscation() {
	return transcation;
}
public void setTranscation(int transcation) {
	this.transcation = transcation;
}
public int getScheduleMonth() {
	return scheduleMonth;
}
public void setScheduleMonth(int scheduleMonth) {
	this.scheduleMonth = scheduleMonth;
}
public int getFirstMonth() {
	return firstMonth;
}
public void setFirstMonth(int firstMonth) {
	this.firstMonth = firstMonth;
}
public int getMonthEnd() {
	return monthEnd;
}
public void setMonthEnd(int monthEnd) {
	this.monthEnd = monthEnd;
}
public int getExpiredType() {
	return expiredType;
}
public void setExpiredType(int expiredType) {
	this.expiredType = expiredType;
}
public int getIntcollectType() {
	return intcollectType;
}
public void setIntcollectType(int intcollectType) {
	this.intcollectType = intcollectType;
}
public int getChangeExpDate() {
	return changeExpDate;
}
public void setChangeExpDate(int changeExpDate) {
	this.changeExpDate = changeExpDate;
}
public int getSameBackAccount() {
	return sameBackAccount;
}
public void setSameBackAccount(int sameBackAccount) {
	this.sameBackAccount = sameBackAccount;
}
public int getAllowByForce() {
	return allowByForce;
}
public void setAllowByForce(int allowByForce) {
	this.allowByForce = allowByForce;
}
public int getShortage() {
	return shortage;
}
public void setShortage(int shortage) {
	this.shortage = shortage;
}

}

