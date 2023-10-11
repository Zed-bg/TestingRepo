/*     */ package com.nirvasoft.rp.util;
import com.nirvasoft.common.XML;
/*     */ 
/*     */ import com.nirvasoft.rulesengine.Lib;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class Util {
/*     */   public static void main(String[] args) {
/*  18 */     System.out.println(inc("B0001"));
/*     */   }
/*     */   
/*     */   public static String inc(String s) {
/*  21 */     String prefix = reg(s, "([a-zA-Z]+)([0-9]+)", 1);
/*  22 */     String number = reg(s, "([a-zA-Z]+)([0-9]+)", 2);
/*  23 */     return String.valueOf(prefix) + leadZero(integer(number) + 1, number.length());
/*     */   }
/*     */   
/*     */   public static String reg(String s, String r, int g) {
/*  26 */     Pattern comp = Pattern.compile(r, 34);
/*  27 */     ArrayList<String> arl = new ArrayList<String>();
/*  29 */     Matcher m = comp.matcher(s);
/*  30 */     while (m.find())
/*  31 */       arl.add(m.group(g)); 
/*  33 */     return (arl.size() > 0) ? arl.get(0) : "";
/*     */   }
/*     */   
/*     */   public static String readText(String strPath) {
/*  36 */     StringBuffer sbTemp = new StringBuffer();
/*     */     try {
/*  38 */       BufferedReader in = new BufferedReader(new FileReader(strPath));
/*     */       String str;
/*  40 */       while ((str = in.readLine()) != null)
/*  41 */         sbTemp.append(String.valueOf(str) + "\r"); 
/*  43 */       in.close();
/*  44 */     } catch (Exception e) {
/*  45 */       System.out.println(e.getMessage());
/*     */     } 
/*  47 */     return sbTemp.toString();
/*     */   }
/*     */   
/*     */   public static ArrayList<String> getFileList(String strPath) {
/*  50 */     ArrayList<String> arlFiles = null;
/*     */     try {
/*  52 */       arlFiles = new ArrayList<String>();
/*  53 */       File f = new File(strPath);
/*  54 */       File[] fileList = f.listFiles();
/*  55 */       for (int i = 0; i < fileList.length; i++) {
/*  56 */         if (fileList[i].isFile()) {
/*  57 */           arlFiles.add(fileList[i].getAbsolutePath());
/*     */         } else {
/*  59 */           ArrayList<String> arlSubFiles = getFileList(fileList[i].getAbsolutePath());
/*  60 */           arlFiles.addAll(arlSubFiles);
/*     */         } 
/*     */       } 
/*  63 */     } catch (Exception e) {
/*  64 */       System.out.println(e.toString());
/*     */     } 
/*  66 */     return arlFiles;
/*     */   }
/*     */   
/*     */   public static String printTree(TreeMap<String, String> Variables) {
/*  69 */     StringBuffer sb = new StringBuffer();
/*  70 */     for (Map.Entry<String, String> entry : Variables.entrySet())
/*  70 */       sb.append((String)entry.getKey() + "=" + (String)entry.getValue() + "; "); 
/*  71 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String[] toArray(ArrayList<String> arl) {
/*  74 */     String[] ar = new String[arl.size()];
/*  75 */     arl.toArray(ar);
/*  76 */     return ar;
/*     */   }
/*     */   
/*     */   public static String toString(ArrayList<String> s) {
/*  78 */     return toString(toArray(s));
/*     */   }
/*     */   
/*     */   public static String toString(String[] s) {
/*  79 */     return Arrays.toString((Object[])s);
/*     */   }
/*     */   
/*     */   public static String toString(ArrayList<String> s, String r) {
/*  81 */     return toString(toArray(s), r);
/*     */   }
/*     */   
/*     */   public static String toString(String[] s, String r) {
/*  83 */     StringBuffer sb = new StringBuffer();
/*  84 */     for (int i = 0; i < s.length; i++) {
/*  85 */       if (!sb.toString().equals(""))
/*  85 */         sb.append(r); 
/*  86 */       sb.append(s[i]);
/*     */     } 
/*  88 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String toString(TreeMap<String, String> tm) {
/*  91 */     StringBuffer sb = new StringBuffer();
/*  92 */     for (Map.Entry<String, String> entry : tm.entrySet()) {
/*  93 */       if (!sb.toString().equals(""))
/*  93 */         sb.append("|"); 
/*  94 */       sb.append(String.valueOf(entry.getKey()) + "=" + (String)entry.getValue());
/*     */     } 
/*  96 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static TreeMap<String, String> toTreeMap(String s, TreeMap<String, String> tm) {
/*     */     try {
/* 100 */       ArrayList<String> arl = XML.getMultiple(s, "var");
/* 101 */       for (int i = 0; i < arl.size(); i++)
/* 102 */         tm.put(((String)arl.get(i)).split("=")[0], ((String)arl.get(i)).split("=")[1]); 
/* 104 */     } catch (Exception exception) {}
/* 105 */     return tm;
/*     */   }
/*     */   
/*     */   public static String toXML(TreeMap<String, String> tm, String tag) {
/* 108 */     StringBuffer sb = new StringBuffer();
/* 109 */     for (Map.Entry<String, String> entry : tm.entrySet())
/* 110 */       sb.append("<" + tag + ">" + (String)entry.getKey() + "=" + (String)entry.getValue() + "</" + tag + ">"); 
/* 112 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String set(String s) {
/* 115 */     return (s != null) ? s : "";
/*     */   }
/*     */   
/*     */   public static String leadZero(String... a) {
/* 118 */     return leadZero(a[0], integer(a[1]));
/*     */   }
/*     */   
/*     */   public static String leadZero(int num, int digits) {
/* 119 */     return leadZero(num, digits);
/*     */   }
/*     */   
/*     */   public static String leadZero(String num, int digits) {
/* 121 */     StringBuffer s = new StringBuffer();
/* 122 */     int zeroes = digits - num.length();
/* 123 */     for (int i = 0; i < zeroes; i++)
/* 124 */       s.append(0); 
/* 126 */     return s.append(num).toString();
/*     */   }
/*     */   
/*     */   public static void log(StringBuffer aLog, String s) {
/* 130 */     if (aLog != null)
/* 130 */       aLog.append(s); 
/*     */   }
/*     */   
/*     */   public static double dbl(String s) {
/* 133 */     double value = 0.0D;
/*     */     try {
/* 134 */       value = Double.parseDouble(s);
/* 134 */     } catch (Exception exception) {}
/* 135 */     return value;
/*     */   }
/*     */   
/*     */   public static int integer(String s) {
/* 138 */     int value = 0;
/*     */     try {
/* 139 */       value = Integer.parseInt(s);
/* 139 */     } catch (Exception exception) {}
/* 140 */     return value;
/*     */   }
/*     */   
/*     */   public static String reflect(String aFunctionCode, String[] aPara) {
/* 142 */     return reflect(aFunctionCode, aPara, "", null);
/*     */   }
/*     */   
/*     */   public static String reflect(String aFunctionCode, String[] aPara, String aPackage) {
/* 143 */     return reflect(aFunctionCode, aPara, aPackage, null);
/*     */   }
/*     */   
/*     */   public static String reflect(String aFunctionCode, String[] aPara, String aPackage, StringBuffer aLog) {
/* 145 */     Object object = null;
/* 145 */     String mName = null, cName = "";
/*     */     try {
/* 147 */       String[] parts = aFunctionCode.split("\\.");
/* 148 */       mName = parts[parts.length - 1];
/* 149 */       if (parts.length == 1) {
/* 149 */         cName = String.valueOf(aPackage) + "." + "Lib";
/* 150 */       } else if (parts.length == 2) {
/* 150 */         cName = String.valueOf(aPackage) + "." + aFunctionCode.substring(0, aFunctionCode.indexOf(mName) - 1);
/*     */       } else {
/* 151 */         cName = aFunctionCode.substring(0, aFunctionCode.indexOf(mName) - 1);
/*     */       } 
/* 152 */       Method method = Class.forName(cName).getMethod(mName, new Class[] { String[].class });
/* 153 */       object = method.invoke(null, new Object[] { aPara });
/* 154 */       object = Lib.zeroTail((String)object);
/* 155 */     } catch (Exception e) {
/* 156 */       log(aLog, "*** Exception @ RuleProcessor.reflector :" + e.getMessage() + " Source :" + cName + "." + mName + "\n");
/*     */     } 
/* 158 */     return (String)object;
/*     */   }
/*     */ }


/* Location:              E:\Yan Naing Win\cbsapiservice\cbsapiservice_04052023\cbsapiservice\WebContent\WEB-INF\lib\common.jar!\com\nirvasoft\common\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */