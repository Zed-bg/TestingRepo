/*     */ package com.nirvasoft.rulesengine;
/*     */ 
/*     */ import com.nirvasoft.common.Util;
/*     */ import com.nirvasoft.common.XML;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class Ruleset {
/*     */   public String getRuleID() {
/*  10 */     return this.RuleID;
/*     */   }
/*     */   
/*     */   public void setRuleID(String ruleID) {
/*  13 */     this.RuleID = ruleID;
/*     */   }
/*     */   
/*     */   public String getRuleText() {
/*  16 */     return this.RuleText;
/*     */   }
/*     */   
/*     */   public void setRuleText(String ruleText) {
/*  19 */     this.RuleText = ruleText;
/*     */   }
/*     */   
/*     */   public ArrayList<Dataset> getDatasets() {
/*  22 */     return this.Datasets;
/*     */   }
/*     */   
/*     */   public void setDatasets(ArrayList<Dataset> datasets) {
/*  25 */     this.Datasets = datasets;
/*     */   }
/*     */   
/*     */   public static String getPath() {
/*  28 */     return Path;
/*     */   }
/*     */   
/*     */   public static void setPath(String path) {
/*  31 */     Path = path;
/*     */   }
/*     */   
/*  33 */   private static String Path = "rules";
/*     */   
/*     */   private String RuleID;
/*     */   
/*     */   private String RuleText;
/*     */   
/*     */   private ArrayList<Dataset> Datasets;
/*     */   
/*     */   public static ArrayList<Ruleset> loadText(String txt, ArrayList<Ruleset> rulesets) {
/*  39 */     ArrayList<String> rs = XML.getMultiple(txt.replaceAll("\r", ""), "ruleset");
/*  40 */     if (rulesets == null)
/*  40 */       rulesets = new ArrayList<Ruleset>(); 
/*  41 */     for (int j = 0; j < rs.size(); j++) {
/*  42 */       Ruleset ruleset = new Ruleset();
/*  43 */       ruleset.setRuleID(XML.get(rs.get(j), "id"));
/*  44 */       ruleset.setRuleText(XML.get(rs.get(j), "rule"));
/*  45 */       ArrayList<String> ds = XML.getMultiple(rs.get(j), "dataset");
/*  46 */       ArrayList<Dataset> datasets = new ArrayList<Dataset>();
/*  47 */       for (int k = 0; k < ds.size(); k++) {
/*  48 */         Dataset dataset = new Dataset();
/*  49 */         dataset.setData(XML.getMultiple(ds.get(k), "data"));
/*  50 */         dataset.setResult(XML.getMultiple(ds.get(k), "result"));
/*  51 */         datasets.add(dataset);
/*     */       } 
/*  53 */       ruleset.setDatasets(datasets);
/*  54 */       rulesets.add(ruleset);
/*     */     } 
/*  56 */     return rulesets;
/*     */   }
/*     */   
/*     */   public static String findInFolder(String ruleid, String path) {
/*  59 */     ArrayList<String> arlRuleFiles = Util.getFileList(path);
/*  60 */     ArrayList<Ruleset> rulesets = new ArrayList<Ruleset>();
/*  61 */     for (int i = 0; i < arlRuleFiles.size(); i++) {
/*  62 */       if (((String)arlRuleFiles.get(i)).endsWith(".xml")) {
/*  63 */         String txt = Util.readText(arlRuleFiles.get(i));
/*  64 */         rulesets = loadText(txt, rulesets);
/*     */       } 
/*     */     } 
/*  67 */     return find(ruleid, rulesets);
/*     */   }
/*     */   
/*     */   public static String loadFolder(String path) {
/*  71 */     ArrayList<String> arlRuleFiles = Util.getFileList(path);
/*  72 */     ArrayList<Ruleset> rulesets = new ArrayList<Ruleset>();
/*  73 */     for (int i = 0; i < arlRuleFiles.size(); i++) {
/*  74 */       if (((String)arlRuleFiles.get(i)).endsWith(".xml")) {
/*  75 */         String txt = Util.readText(arlRuleFiles.get(i));
/*  76 */         rulesets = loadText(txt, rulesets);
/*     */       } 
/*     */     } 
/*  79 */     return toString(rulesets);
/*     */   }
/*     */   
/*     */   public static String listFolder(String path) {
/*  82 */     ArrayList<String> arlRuleFiles = Util.getFileList(path);
/*  83 */     return Util.toString(arlRuleFiles);
/*     */   }
/*     */   
/*     */   public static String findInFile(String ruleid, String path) {
/*  86 */     ArrayList<Ruleset> arlRS = loadText(Util.readText(path), null);
/*  87 */     return find(ruleid, arlRS);
/*     */   }
/*     */   
/*     */   public static String find(String ruleid, ArrayList<Ruleset> arlRS) {
/*  91 */     String rules = "";
/*  92 */     for (int i = 0; i < arlRS.size(); i++) {
/*  93 */       if (((Ruleset)arlRS.get(i)).getRuleID().equalsIgnoreCase(ruleid))
/*  94 */         rules = ((Ruleset)arlRS.get(i)).getRuleText(); 
/*     */     } 
/*  97 */     return rules;
/*     */   }
/*     */   
/*     */   public static String toString(ArrayList<Ruleset> rs) {
/* 100 */     StringBuffer sb = new StringBuffer();
/* 101 */     for (int i = 0; i < rs.size(); i++) {
/* 102 */       sb.append("<<< " + ((Ruleset)rs.get(i)).getRuleID() + " >>>\n" + ((Ruleset)rs.get(i)).getRuleText() + "\n");
/* 103 */       for (int j = 0; j < ((Ruleset)rs.get(i)).Datasets.size(); j++)
/* 104 */         sb.append(
/* 105 */             String.valueOf(Util.toString(((Dataset)((Ruleset)rs.get(i)).getDatasets().get(j)).getData())) + "\n" + 
/* 106 */             Util.toString(((Dataset)((Ruleset)rs.get(i)).getDatasets().get(j)).getResult()) + "\n"); 
/*     */     } 
/* 110 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\Yan Naing Win\cbsapiservice\cbsapiservice_04052023\cbsapiservice\WebContent\WEB-INF\lib\bre.jar!\com\nirvasoft\rulesengine\Ruleset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */