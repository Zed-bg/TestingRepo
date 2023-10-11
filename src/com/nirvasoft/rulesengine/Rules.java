/*     */ package com.nirvasoft.rulesengine;
/*     */ 
/*     */ import com.nirvasoft.rp.util.Util;
/*     */ import java.util.Arrays;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class Rules {
/*     */   public static String processID(String id) {
/*   8 */     return processID(null, id, null, null);
/*     */   }
/*     */   
/*     */   public static String processID(String id, TreeMap<String, String> aVariables) {
/*   9 */     return processID(null, id, aVariables, null);
/*     */   }
/*     */   
/*     */   public static String processID(String[] aInput, String id, TreeMap<String, String> aVariables) {
/*  10 */     return processID(aInput, id, aVariables, null);
/*     */   }
/*     */   
/*     */   public static String processID(String[] aInput, String id, TreeMap<String, String> aVariables, StringBuffer aLog) {
/*  12 */     String rules = Ruleset.findInFolder(id, Ruleset.getPath());
/*  13 */     return process(aInput, rules, aVariables, aLog);
/*     */   }
/*     */   
/*     */   public static String process(String aRules) {
/*  15 */     return process(new String[0], aRules, null, null);
/*     */   }
/*     */   
/*     */   public static String process(String aRules, StringBuffer aLog) {
/*  16 */     return process(null, aRules, null, aLog);
/*     */   }
/*     */   
/*     */   public static String process(String[] aInput, String aRules) {
/*  17 */     return process(aInput, aRules, null, null);
/*     */   }
/*     */   
/*     */   public static String process(String[] aInput, String aRules, TreeMap<String, String> aVariables) {
/*  18 */     return process(aInput, aRules, aVariables, null);
/*     */   }
/*     */   
/*     */   public static String process(String[] aInput, String aRules, StringBuffer aLog) {
/*  19 */     return process(aInput, aRules, null, aLog);
/*     */   }
/*     */   
/*     */   public static String process(String[] aInput, String aRules, TreeMap<String, String> aVariables, StringBuffer aLog) {
/*  21 */     String ret = "", sKey = "", sPackage = "com.nirvasoft.rulesengine";
/*  22 */     if (!aRules.equals(""))
/*     */       try {
/*     */         TreeMap<String, String> Variables;
/*  24 */         aRules = aRules.replace("\n", "");
/*  25 */         TreeMap<String, Integer> LineIndexes = new TreeMap<String, Integer>();
/*  27 */         if (aVariables == null) {
/*  27 */           Variables = new TreeMap<String, String>();
/*     */         } else {
/*  28 */           Variables = aVariables;
/*     */         } 
/*  29 */         String[] rules = aRules.split(";");
/*  30 */         String[] lines = new String[rules.length];
/*  31 */         Util.log(aLog, "Input: " + Arrays.toString((Object[])aInput) + "\n");
/*  32 */         Util.log(aLog, "Rules: " + aRules + "\n" + "Execution: \n");
/*     */         int i;
/*  33 */         for (i = 0; i < rules.length; i++) {
/*  34 */           if (rules[i].matches(" *(.+): *(.+)")) {
/*  35 */             sKey = Regex.extract(rules[i], Regex.getLineIndex, 1);
/*  36 */             lines[i] = sKey;
/*  37 */             rules[i] = Regex.extract(rules[i], Regex.getLineIndex, 2);
/*  38 */             LineIndexes.put(sKey, new Integer(i));
/*     */           } else {
/*  39 */             lines[i] = "";
/*     */           } 
/*     */         } 
/*  41 */         if (aInput != null)
/*  42 */           for (i = 0; i < aInput.length; i++) {
/*  43 */             if (aInput[i].matches("([a-zA-Z]+[a-zA-Z0-9]*)[ ]*= *(.+)[ ]*")) {
/*  44 */               sKey = Regex.extract(aInput[i], Regex.getValueAssignment, 1);
/*  45 */               String Value = Regex.extract(aInput[i], Regex.getValueAssignment, 2);
/*  46 */               Variables.put(sKey, Value);
/*     */             } 
/*     */           }  
/*  49 */         for (i = 0; i < rules.length; i++) {
/*     */           String rule;
/*  50 */           sKey = "";
/*  52 */           Util.log(aLog, " " + (lines[i].equals("") ? "" : (String.valueOf(lines[i]) + ": ")) + rules[i] + "\n");
/*  54 */           String sIf = "", sThen = "", sElse = "";
/*  55 */           if (rules[i].matches("[ ]*[iI][fF] \\((.+)\\) [tT][hH][eE][nN] +([^ ]+) +[eE][lL][sS][eE] *([^ ;]*)[ ;]*")) {
/*  56 */             sIf = Regex.extract(rules[i], Regex.getIF, 1);
/*  57 */             sThen = Regex.extract(rules[i], Regex.getIF, 2);
/*  58 */             sElse = Regex.extract(rules[i], Regex.getIF, 3);
/*  59 */             if (ifTrue(sIf, Variables, sPackage, aLog)) {
/*  59 */               rule = sThen;
/*     */             } else {
/*  60 */               rule = sElse;
/*     */             } 
/*     */           } else {
/*  62 */             rule = rules[i];
/*     */           } 
/*  64 */           if (rule.matches("([a-zA-Z]+[a-zA-Z0-9]*)[ ]*= *(.+)[ ]*")) {
/*  65 */             sKey = Regex.extract(rule, Regex.getValueAssignment, 1);
/*  66 */             rule = Regex.extract(rule, Regex.getValueAssignment, 2);
/*     */           } 
/*  68 */           if (rule.toLowerCase().contains("end")) {
/*  69 */             Util.log(aLog, "   <End>\n");
/*  70 */             i = rules.length;
/*  71 */           } else if (rule.toLowerCase().matches("goto\\((.+)\\) *")) {
/*  72 */             String sGoto = Regex.extract(rule, Regex.getGoto, 1);
/*  73 */             Util.log(aLog, "   Goto Line: " + sGoto + "\n");
/*  74 */             Integer index = LineIndexes.get(sGoto);
/*  75 */             i = index.intValue() - 1;
/*     */           } else {
/*  77 */             ret = execute(rule, Variables, sKey, sPackage, aLog);
/*  78 */             Util.log(aLog, "   " + ret + "\n");
/*     */           } 
/*     */         } 
/*     */       } catch (Exception ex) {
/*  81 */         Util.log(aLog, "*** Exception @ RuleProcessor.processing :" + ex.getMessage() + "\n");
/*     */       }  
/*  82 */     return ret;
/*     */   }
/*     */   
/*     */   static String execute(String rule, TreeMap<String, String> Variables, String sKey, String sPackage, StringBuffer aLog) {
/*  85 */     String ret = "";
/*  86 */     if (rule.equalsIgnoreCase("true") || rule.equalsIgnoreCase("false")) {
/*  87 */       ret = rule.toLowerCase();
/*  88 */       if (!sKey.equals(""))
/*  88 */         Variables.put(sKey, ret); 
/*  89 */     } else if (rule.matches("[a-zA-Z]+[a-zA-Z0-9]*")) {
/*  90 */       ret = Variables.get(Regex.extract(rule, Regex.getVariableName, 0));
/*  91 */       if (!sKey.equals(""))
/*  91 */         Variables.put(sKey, ret); 
/*  92 */     } else if (rule.matches("'(.*)'") || rule.matches("[-+]?[0-9]+\\.?[0-9]*[^%]?")) {
/*  93 */       ret = rule;
/*  94 */       if (!sKey.equals(""))
/*  94 */         Variables.put(sKey, ret); 
/*  95 */     } else if (rule.matches("[^ ]+ *([\\+\\-\\*\\/]{1}) *[^ ]+.?")) {
/*  96 */       rule = rule.replace("+", " + ");
/*  97 */       rule = rule.replace("-", " - ");
/*  98 */       rule = rule.replace("*", " * ");
/*  99 */       rule = rule.replace("/", " / ");
/* 100 */       String[] para = rule.split("[ ]+");
/* 101 */       if (para[0].matches("[a-zA-Z]+[a-zA-Z0-9]*"))
/* 101 */         para[0] = Variables.get(Regex.extract(para[0], Regex.getVariableName, 0)); 
/* 102 */       double val = Util.dbl(para[0]);
/* 103 */       for (int l = 2; l < para.length; l += 2) {
/* 104 */         if (para[l].matches("[a-zA-Z]+[a-zA-Z0-9]*"))
/* 104 */           para[l] = Variables.get(Regex.extract(para[l], Regex.getVariableName, 0)); 
/* 105 */         if (para[l - 1].equals("+")) {
/* 105 */           val += Util.dbl(para[l]);
/* 106 */         } else if (para[l - 1].equals("-")) {
/* 106 */           val -= Util.dbl(para[l]);
/* 107 */         } else if (para[l - 1].equals("*")) {
/* 107 */           val *= Util.dbl(para[l]);
/* 108 */         } else if (para[l - 1].equals("/")) {
/* 108 */           val /= Util.dbl(para[l]);
/*     */         } 
/*     */       } 
/* 110 */       ret = Lib.zeroTail(val);
/* 111 */       if (!sKey.equals(""))
/* 111 */         Variables.put(sKey, ret); 
/* 112 */       Util.log(aLog, "   Calculation :  " + ret + "\n");
/*     */     } else {
/* 114 */       String FunctionCode = Regex.extract(rule, Regex.getFunctionName, 1);
/* 115 */       Util.log(aLog, "   Function Code: " + FunctionCode + "\n");
/* 116 */       String[] para = para(rule, Variables, aLog);
/* 117 */       ret = reflect(FunctionCode, para, Variables, sKey, sPackage, aLog);
/*     */     } 
/* 119 */     return ret;
/*     */   }
/*     */   
/*     */   static String[] para(String rule, TreeMap<String, String> Variables, StringBuffer aLog) {
/* 122 */     String[] ret = Regex.extract(rule, Regex.getInnerRoundBracket, 1).split(",");
/* 123 */     for (int i = 0; i < ret.length; i++) {
/* 124 */       if (ret[i].matches("[a-zA-Z]+[a-zA-Z0-9]*"))
/* 125 */         ret[i] = Variables.get(Regex.extract(ret[i], Regex.getVariableName, 0)); 
/*     */     } 
/* 128 */     return ret;
/*     */   }
/*     */   
/*     */   static String reflect(String aFunctionCode, String[] aPara, TreeMap<String, String> aData, String aKey, String aPackage, StringBuffer aLog) {
/* 131 */     String ret = Util.reflect(aFunctionCode, aPara, aPackage, aLog);
/* 132 */     if (!aKey.equals(""))
/* 132 */       aData.put(aKey, ret); 
/* 133 */     return ret;
/*     */   }
/*     */   
/*     */   static boolean ifTrue(String sIf, TreeMap<String, String> Variables, String pkg, StringBuffer aLog) {
/* 137 */     boolean ret = false;
/* 138 */     if (sIf.matches(" *(.*?)\\(.*")) {
/* 139 */       String FunctionCode = Regex.extract(sIf, Regex.getFunctionName, 1);
/* 140 */       Util.log(aLog, "   Function Code: " + FunctionCode + "\n");
/* 141 */       String[] para = para(sIf, Variables, aLog);
/* 142 */       ret = reflect(FunctionCode, para, Variables, "", pkg, aLog).equalsIgnoreCase("true");
/*     */     } else {
/* 144 */       ret = condition(sIf, Variables, aLog);
/*     */     } 
/* 146 */     return ret;
/*     */   }
/*     */   
/*     */   static boolean condition(String stmt, TreeMap<String, String> Variables, StringBuffer aLog) {
/* 149 */     boolean ret = false;
/* 150 */     String a = "", b = "";
/* 151 */     String opt = "=";
/* 152 */     String[] stmtOR = stmt.split(" +OR +");
/* 153 */     for (int i = 0; i < stmtOR.length; i++) {
/* 154 */       String[] stmtAND = stmtOR[i].split(" +AND +");
/* 155 */       boolean bAnd = true;
/* 156 */       for (int j = 0; j < stmtAND.length; j++) {
/* 158 */         String S = stmtAND[j];
/* 159 */         if (S.matches("[ ]*([A-Za-z0-9']*) *([><!=]{1,2}) *([A-Za-z0-9']*)[ ]*")) {
/* 160 */           String A = Regex.extract(S, Regex.getConStatement, 1);
/* 161 */           if (A.matches("[a-zA-Z]+[a-zA-Z0-9]*")) {
/* 162 */             a = Variables.get(A);
/*     */           } else {
/* 163 */             a = A;
/*     */           } 
/* 164 */           opt = Regex.extract(S, Regex.getConStatement, 2);
/* 165 */           String B = Regex.extract(S, Regex.getConStatement, 3);
/* 166 */           if (!B.equalsIgnoreCase("true") && !B.equalsIgnoreCase("false") && B.matches("[a-zA-Z]+[a-zA-Z0-9]*")) {
/* 167 */             b = Variables.get(B);
/*     */           } else {
/* 168 */             b = B;
/*     */           } 
/*     */         } 
/* 170 */         boolean bcurrent = isTrue(a, opt, b);
/* 171 */         if (stmtAND.length > 1)
/* 171 */           Util.log(aLog, "    "); 
/* 172 */         Util.log(aLog, "   " + S + " = " + bcurrent + "\n");
/* 173 */         bAnd = (bAnd && bcurrent);
/*     */       } 
/* 175 */       ret = !(!ret && !bAnd);
/* 176 */       if (stmtAND.length > 1)
/* 176 */         Util.log(aLog, "   " + stmtOR[i] + " >>> " + bAnd + "\n"); 
/*     */     } 
/* 178 */     Util.log(aLog, "   Condition : \"" + stmt + "\" = " + ret + "\n");
/* 179 */     return ret;
/*     */   }
/*     */   
/*     */   static boolean isTrue(String left, String opt, String right) {
/* 182 */     if (left.startsWith("'")) {
/* 183 */       if (opt.equals("==") || opt.equals("="))
/* 184 */         return left.equals(right); 
/* 185 */       return false;
/*     */     } 
/* 186 */     if (right.equalsIgnoreCase("true") || right.equalsIgnoreCase("false")) {
/* 187 */       if (opt.equals("==") || opt.equals("="))
/* 188 */         return left.equals(right); 
/* 189 */       return false;
/*     */     } 
/* 191 */     double a = Util.dbl(left), b = Util.dbl(right);
/* 192 */     if (opt.equals("==") || opt.equals("="))
/* 193 */       return (a == b); 
/* 194 */     if (opt.equals("!="))
/* 195 */       return (a != b); 
/* 196 */     if (opt.equals(">="))
/* 197 */       return (a >= b); 
/* 198 */     if (opt.equals("<="))
/* 199 */       return (a <= b); 
/* 200 */     if (opt.equals(">"))
/* 201 */       return (a > b); 
/* 202 */     if (opt.equals("<"))
/* 203 */       return (a < b); 
/* 204 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\Yan Naing Win\cbsapiservice\cbsapiservice_04052023\cbsapiservice\WebContent\WEB-INF\lib\bre.jar!\com\nirvasoft\rulesengine\Rules.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */