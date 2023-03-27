/*     */ package cn.sh.library.pedigree.ontology.check;
/*     */ 
/*     */ /*     */ import java.io.PrintWriter;

/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;

import com.hp.hpl.jena.rdf.arp.ParseException;
/*     */ import com.sun.org.apache.regexp.internal.RE;
/*     */ import com.sun.org.apache.regexp.internal.RESyntaxException;
/*     */ 
/*     */ 
/*     */ public class RDFErrorHandler
/*     */   implements ErrorHandler
/*     */ {
/*     */   PrintWriter out;
/*  15 */   String fatalErrors = "";
/*  16 */   String errors = "";
/*  17 */   String warnings = "";
/*  18 */   String datatypeErrors = "";
/*     */ 
/*     */   public RDFErrorHandler(PrintWriter out)
/*     */   {
/*  27 */     this.out = out;
/*     */   }
/*     */ 
/*     */   private static String format(SAXParseException e)
/*     */   {
/*  37 */     String msg = e.getMessage();
/*  38 */     if (msg == null) {
/*  39 */       msg = e.toString();
/*     */     }
/*     */ 
/*  42 */     msg = replaceString(msg, "&", "&amp;");
/*  43 */     msg = replaceString(msg, "<", "&lt;");
/*  44 */     msg = replaceString(msg, ">", "&gt;");
/*  45 */     msg = replaceString(msg, "\"", "&quot;");
/*  46 */     msg = replaceString(msg, "'", "&apos;");
/*     */ 
/*  48 */     return msg + "[Line = " + e.getLineNumber() + ", Column = " + e.getColumnNumber() + "]";
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/*  57 */     if ((e instanceof ParseException))
/*  58 */       this.errors = (this.errors + "Error: " + format(e) + "<br />");
/*     */     else
/*  60 */       this.errors = (this.errors + "Error: " + format(e) + "<br />");
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/*  70 */     this.fatalErrors = (this.fatalErrors + "FatalError: " + format(e) + "<br />");
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/*  79 */     if ((e instanceof ParseException))
/*  80 */       this.warnings = (this.warnings + "Warning: " + format(e) + "<br />");
/*     */     else
/*  82 */       this.errors = (this.errors + "Warning: " + format(e) + "<br />");
/*     */   }
/*     */ 
/*     */   public String getErrors()
/*     */   {
/*  93 */     return this.errors;
/*     */   }
/*     */ 
/*     */   public String getDatatypeErrors() {
/*  97 */     return this.datatypeErrors;
/*     */   }
/*     */ 
/*     */   public String getFatalErrors()
/*     */   {
/* 107 */     return this.fatalErrors;
/*     */   }
/*     */ 
/*     */   public String getWarnings()
/*     */   {
/* 117 */     return this.warnings;
/*     */   }
/*     */ 
/*     */   private static String replaceString(String input, String key, String replacement) {
/*     */     try {
/* 122 */       RE re = new RE(key);
/* 123 */       return re.subst(input, replacement); } catch (RESyntaxException e) {
/*     */     }
/* 125 */     return input;
/*     */   }
/*     */ 
/*     */   public void printErrorMessages(PrintWriter out, RDFErrorHandler eh)
/*     */   {
/*     */     try
/*     */     {
/* 132 */       boolean c = true;
/*     */ 
/* 134 */       out.println("<h2><a name='messages' id='messages'>Validation Results</a></h2>");
/*     */ 
/* 137 */       String s = eh.getFatalErrors();
/* 138 */       if ((s != null) && (s.length() >= 1)) {
/* 139 */         out.println("<h3>Fatal Error Messages</h3>" + s);
/* 140 */         c = false;
/*     */       }
/*     */ 
/* 143 */       s = eh.getErrors();
/* 144 */       if ((s != null) && (s.length() >= 1)) {
/* 145 */         out.println("<h3>Error Messages</h3>" + s);
/* 146 */         c = false;
/*     */       }
/*     */ 
/* 149 */       s = eh.getWarnings();
/* 150 */       if ((s != null) && (s.length() >= 1)) {
/* 151 */         out.println("<h3>Warning Messages</h3>" + s);
/* 152 */         c = false;
/*     */       }
/*     */ 
/* 155 */       if (c) {
/* 156 */         if (PrintTable.AT_LEAST_ONE_TRIPLE)
/* 157 */           out.println("<p>Your RDF document validated successfully.</p>");
/*     */         else {
/* 159 */           out.println("<p>Error: Your document does not contain any RDF statement.</p>");
/*     */         }
/* 161 */         PrintTable.AT_LEAST_ONE_TRIPLE = false;
/*     */       }
/*     */ 
/* 165 */       s = eh.getDatatypeErrors();
/* 166 */       if ((s != null) && (s.length() >= 2)) {
/* 167 */         out.println("<h3>Note about datatypes</h3>");
/* 168 */         out.println("<p>Datatypes are used on lines " + s.substring(0, s.length() - 2) + ". This RDF feature is not yet supported by the RDF Validator. Literals are treated as untyped.</p>");
/*     */       }
/*     */     } catch (Exception e) {
/* 171 */       System.err.println("Error printing error messages.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.check.RDFErrorHandler
 * JD-Core Version:    0.6.2
 */