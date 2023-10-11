/*    */ package com.nirvasoft.rulesengine;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class Regex {
/*    */   public static final String Numeric = "[-+]?[0-9]+\\.?[0-9]*[^%]?";
/*    */   
/*    */   public static final String FilePath = "([a-zA-Z]:)?(.)*([a-zA-Z0-9_]+[.][a-zA-Z0-9_]+)+";
/*    */   
/*    */   public static final String ConOperator = "([><!=]{1,2})";
/*    */   
/*    */   public static final String CalOperator = "[^ ]+ *([\\+\\-\\*\\/]{1}) *[^ ]+.?";
/*    */   
/*    */   public static final String Percent = "[-+]?[0-9]*\\.?[0-9]*[%]";
/*    */   
/*    */   public static final String Text = "'(.*)'";
/*    */   
/* 15 */   public static final Pattern getText = Pattern.compile("'(.*)'");
/*    */   
/*    */   public static final String ProcessCode = "[a-zA-Z]{2}[0-9]*";
/*    */   
/*    */   public static final String Goto = "goto\\((.+)\\) *";
/*    */   
/* 20 */   public static final Pattern getGoto = Pattern.compile("goto\\((.+)\\) *");
/*    */   
/*    */   public static final String LineIndex = " *(.+): *(.+)";
/*    */   
/* 22 */   public static final Pattern getLineIndex = Pattern.compile(" *(.+): *(.+)");
/*    */   
/*    */   public static final String FunctionName = " *(.*?)\\(.*";
/*    */   
/* 24 */   public static final Pattern getFunctionName = Pattern.compile(" *(.*?)\\(.*");
/*    */   
/* 25 */   public static final Pattern getInnerSingleQuote = Pattern.compile("'(.*?)'");
/*    */   
/* 26 */   public static final Pattern getInnerRoundBracket = Pattern.compile("\\((.*?)\\)");
/*    */   
/*    */   public static final String VariableName = "[a-zA-Z]+[a-zA-Z0-9]*";
/*    */   
/* 30 */   public static final Pattern getVariableName = Pattern.compile("[a-zA-Z]+[a-zA-Z0-9]*");
/*    */   
/*    */   public static final String ValueAssignment = "([a-zA-Z]+[a-zA-Z0-9]*)[ ]*= *(.+)[ ]*";
/*    */   
/* 32 */   public static final Pattern getValueAssignment = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9]*)[ ]*= *(.+)[ ]*");
/*    */   
/*    */   public static final String ConStatement = "[ ]*([A-Za-z0-9']*) *([><!=]{1,2}) *([A-Za-z0-9']*)[ ]*";
/*    */   
/* 35 */   public static final Pattern getConStatement = Pattern.compile("[ ]*([A-Za-z0-9']*) *([><!=]{1,2}) *([A-Za-z0-9']*)[ ]*");
/*    */   
/*    */   public static final String IF = "[ ]*[iI][fF] \\((.+)\\) [tT][hH][eE][nN] +([^ ]+) +[eE][lL][sS][eE] *([^ ;]*)[ ;]*";
/*    */   
/* 38 */   public static final Pattern getIF = Pattern.compile("[ ]*[iI][fF] \\((.+)\\) [tT][hH][eE][nN] +([^ ]+) +[eE][lL][sS][eE] *([^ ;]*)[ ;]*");
/*    */   
/*    */   public static final String RuleSet = "<ruleset>(.*)</ruleset>";
/*    */   
/* 41 */   public static final Pattern getRuleSet = Pattern.compile("<ruleset>(.*)</ruleset>");
/*    */   
/*    */   static String extract(String s, Pattern p, int g) {
/* 44 */     String ret = "";
/* 46 */     Matcher m = p.matcher(s);
/* 47 */     if (m.find())
/* 48 */       ret = m.group(g); 
/* 50 */     return ret;
/*    */   }
/*    */ }


/* Location:              E:\Yan Naing Win\cbsapiservice\cbsapiservice_04052023\cbsapiservice\WebContent\WEB-INF\lib\bre.jar!\com\nirvasoft\rulesengine\Regex.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */