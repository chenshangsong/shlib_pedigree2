/*    */ package cn.sh.library.pedigree.ontology.check;
/*    */ 
/*    */ /*    */ import java.io.PrintWriter;

import com.sun.org.apache.regexp.internal.RE;
/*    */ import com.sun.org.apache.regexp.internal.RESyntaxException;
/*    */ 
/*    */ 
/*    */ public class PrintListing
/*    */ {
/*    */   public static void run(PrintWriter out, String rdf, boolean needCR)
/*    */   {
/*    */     try
/*    */     {
/* 23 */       out.println("<hr title='original source' /><h3><a name='source' id='source'>The original RDF/XML document</a></h3><pre>");
/*    */ 
/* 28 */       String s = replaceString(rdf, "&", "&amp;");
/* 29 */       s = replaceString(s, "<", "&lt;");
/*    */ 
/* 32 */       int lineNum = 1;
/* 33 */       int nl = 0;
/* 34 */       String terminator = needCR ? "\n" : "";
/*    */       do
/*    */       {
/* 37 */         nl = s.indexOf('\n');
/*    */         String tok;
/* 38 */         if (nl == -1) {
/* 39 */           tok = s;
/*    */         } else {
/* 41 */           tok = s.substring(0, nl);
/* 42 */           s = s.substring(nl + 1);
/*    */         }
/* 44 */         out.print("<a name=\"" + lineNum + "\">" + lineNum + "</a>: " + tok + terminator);
/*    */ 
/* 46 */         lineNum++;
/* 47 */       }while (nl != -1);
/*    */ 
/* 49 */       out.println("</pre>");
/*    */     } catch (Exception e) {
/* 51 */       System.err.println("Exception (printListing): " + e.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   private static String replaceString(String input, String key, String replacement) {
/*    */     try {
/* 57 */       RE re = new RE(key);
/* 58 */       return re.subst(input, replacement); } catch (RESyntaxException e) {
/*    */     }
/* 60 */     return input;
/*    */   }
/*    */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.check.PrintListing
 * JD-Core Version:    0.6.2
 */