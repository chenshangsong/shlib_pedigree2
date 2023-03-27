/*     */ package cn.sh.library.pedigree.ontology.check;
/*     */ 
/*     */ /*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;

import com.hp.hpl.jena.rdf.arp.ALiteral;
/*     */ import com.hp.hpl.jena.rdf.arp.AResource;
/*     */ import com.hp.hpl.jena.rdf.arp.StatementHandler;
/*     */ import com.sun.org.apache.regexp.internal.RE;
/*     */ import com.sun.org.apache.regexp.internal.RESyntaxException;
/*     */ 
/*     */ 
/*     */ public class PrintTable
/*     */   implements StatementHandler
/*     */ {
/*     */   PrintWriter out;
/*     */   PrintWriter pw;
/*     */   boolean isNTriples;
/*     */   boolean printTriples;
/*     */   boolean printGraph;
/*     */   boolean anonNodesEmpty;
/*     */   int numStatements;
/*     */   int numLiterals;
/*     */   Hashtable subjects;
/*     */   int numSubjects;
/*     */   String gFormat;
/*  25 */   static boolean AT_LEAST_ONE_TRIPLE = false;
/*     */   private static final String ANON_NODE = "genid:";
/*     */   private static final String FORMAT_ISV_ZVTM = "ISV_ZVTM";
/*  30 */   private static float resTBh = 0.3333333F;
/*  31 */   private static float resTBs = 0.3714286F;
/*  32 */   private static float resTBv = 0.4117647F;
/*  33 */   private static float prpTh = 0.6680911F;
/*  34 */   private static float prpTs = 0.5679612F;
/*  35 */   private static float prpTv = 0.8078432F;
/*  36 */   private static float litTBh = 0.1287879F;
/*  37 */   private static float litTBs = 0.5F;
/*  38 */   private static float litTBv = 0.5176471F;
/*     */ 
/*     */   public PrintTable(PrintWriter out, boolean isNTriples, boolean printTriples)
/*     */   {
/*  53 */     this.out = out;
/*  54 */     this.isNTriples = isNTriples;
/*  55 */     this.printTriples = printTriples;
/*     */ 
/*  57 */     this.numStatements = 0;
/*  58 */     this.numLiterals = 0;
/*     */ 
/*  60 */     this.subjects = new Hashtable();
/*  61 */     this.numSubjects = 0;
/*     */   }
/*     */ 
/*     */   public void statement(AResource subj, AResource pred, AResource obj)
/*     */   {
/*  73 */     if (!AT_LEAST_ONE_TRIPLE) {
/*  74 */       AT_LEAST_ONE_TRIPLE = true;
/*     */     }
/*  76 */     if (this.printTriples)
/*  77 */       statementResource(subj, pred, obj);
/*  78 */     if (this.printGraph)
/*  79 */       statementDotResource(subj, pred, obj);
/*     */   }
/*     */ 
/*     */   public void statement(AResource subj, AResource pred, ALiteral lit)
/*     */   {
/*  91 */     if (!AT_LEAST_ONE_TRIPLE) {
/*  92 */       AT_LEAST_ONE_TRIPLE = true;
/*     */     }
/*  94 */     this.numLiterals += 1;
/*  95 */     if (this.printTriples)
/*  96 */       statementLiteral(subj, pred, lit);
/*  97 */     if (this.printGraph)
/*  98 */       statementDotLiteral(subj, pred, lit);
/*     */   }
/*     */ 
/*     */   public void statementResource(AResource subj, AResource pred, AResource obj)
/*     */   {
/* 110 */     this.numStatements += 1;
/*     */ 
/* 112 */     if (this.isNTriples)
/* 113 */       printNTriple(this.out, subj, pred, obj, null);
/*     */     else
/* 115 */       printTableRow(this.out, subj, pred, obj, null, this.numStatements);
/*     */   }
/*     */ 
/*     */   private static void printNTriple(PrintWriter out, AResource subj, AResource pred, AResource objRes, ALiteral objLit) {
/* 119 */     printResource(out, subj);
/* 120 */     printResource(out, pred);
/* 121 */     if (objRes != null)
/* 122 */       printResource(out, objRes);
/*     */     else
/* 124 */       printNTripleLiteral(out, objLit);
/* 125 */     out.println(".");
/*     */   }
/*     */ 
/*     */   private static void printResource(PrintWriter out, AResource r)
/*     */   {
/* 135 */     if (r.isAnonymous())
/* 136 */       out.print("_:j" + r.getAnonymousID() + " ");
/*     */     else
/* 138 */       out.print("&lt;" + r.getURI() + "&gt; ");
/*     */   }
/*     */ 
/*     */   private static void printNTripleLiteral(PrintWriter out, ALiteral l)
/*     */   {
/* 148 */     out.print("\"");
/* 149 */     char[] ar = l.toString().toCharArray();
/*     */ 
/* 151 */     for (int i = 0; i < ar.length; i++)
/* 152 */       switch (ar[i]) {
/*     */       case '\\':
/* 154 */         out.print("\\\\");
/* 155 */         break;
/*     */       case '"':
/* 157 */         out.print("\\\"");
/* 158 */         break;
/*     */       case '\n':
/* 160 */         out.print("\\n");
/* 161 */         break;
/*     */       case '\r':
/* 163 */         out.print("\\r");
/* 164 */         break;
/*     */       case '\t':
/* 166 */         out.print("\\t");
/* 167 */         break;
/*     */       default:
/* 169 */         if ((ar[i] >= ' ') && (ar[i] <= '')) {
/* 170 */           out.print(ar[i]);
/* 171 */         } else if ((ar[i] < 55296) || (ar[i] >= 57344)) {
/* 172 */           out.print("\\u" + hexPadd(ar[i], 4));
/*     */         }
/* 174 */         else if (ar[i] >= 56320) {
/* 175 */           out.print("{{{error: lone low surrogate}}}"); } else {
/* 176 */           i++; if (i >= ar.length) {
/* 177 */             out.print("{{{error: lone surrogate at end of string}}}");
/* 178 */           } else if ((ar[i] < 56320) || (ar[i] >= 57344)) {
/* 179 */             out.print("{{{error: high surrogate not followed by low surrogate}}}");
/*     */           } else {
/* 181 */             int scalarvalue = 65536 + ar[(i - 1)] * 'Ѐ' + ar[i];
/* 182 */             out.print("\\U" + hexPadd(scalarvalue, 8));
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/* 187 */     out.print("\" ");
/* 188 */     String s1 = "";
/* 189 */     if ((l.getLang() != null) && (l.getLang().length() > 0)) {
/* 190 */       s1 = s1 + "@" + l.getLang();
/*     */     }
/* 192 */     if (l.getDatatypeURI() != null) {
/* 193 */       String s2 = l.getDatatypeURI();
/* 194 */       s2 = replaceString(s2, "<", "&lt;");
/* 195 */       s2 = replaceString(s2, ">", "&gt;");
/* 196 */       s2 = replaceString(s2, "&", "&amp;");
/* 197 */       if (s2.length() > 0) {
/* 198 */         s1 = s1 + "^^" + s2;
/*     */       }
/*     */     }
/* 201 */     if (s1.length() > 0) out.print(s1);
/*     */   }
/*     */ 
/*     */   private static String hexPadd(int number, int length)
/*     */   {
/* 213 */     String t = Integer.toHexString(number).toUpperCase();
/* 214 */     int hexlength = t.length();
/*     */ 
/* 216 */     if (hexlength > length) {
/* 217 */       hexlength = length;
/*     */     }
/*     */ 
/* 220 */     int zerolength = length - hexlength;
/* 221 */     String r = "";
/*     */ 
/* 223 */     for (int i = 0; i < zerolength; i++) {
/* 224 */       r = r + "0";
/*     */     }
/* 226 */     for (int i = 0; i < hexlength; i++) {
/* 227 */       r = r + t.charAt(i);
/*     */     }
/* 229 */     return r;
/*     */   }
/*     */ 
/*     */   private static String replaceString(String input, String key, String replacement) {
/*     */     try {
/* 234 */       RE re = new RE(key);
/* 235 */       return re.subst(input, replacement); } catch (RESyntaxException e) {
/*     */     }
/* 237 */     return input;
/*     */   }
/*     */ 
/*     */   public void statementLiteral(AResource subj, AResource pred, ALiteral lit)
/*     */   {
/* 250 */     this.numStatements += 1;
/*     */ 
/* 252 */     if (this.isNTriples)
/* 253 */       printNTriple(this.out, subj, pred, null, lit);
/*     */     else
/* 255 */       printTableRow(this.out, subj, pred, null, lit, this.numStatements);
/*     */   }
/*     */ 
/*     */   private static String addAnchor(AResource r)
/*     */   {
/* 266 */     if (r.isAnonymous()) {
/* 267 */       return "genid:" + r.getAnonymousID();
/*     */     }
/* 269 */     return "<a href='" + r.getURI() + "'>" + r.getURI() + "</a>";
/*     */   }
/*     */ 
/*     */   public void printTripleTableHeader(PrintWriter out, boolean nTriples)
/*     */   {
/*     */     try
/*     */     {
/* 280 */       if (nTriples) {
/* 281 */         out.println("<h3><a name='triples' id='triples'>Triples of the Data Model in <a href=\"http://www.w3.org/2001/sw/RDFCore/ntriples/\">N-Triples</a> Format (Sub, Pred, Obj)</a></h3><pre>");
/*     */       }
/*     */       else
/*     */       {
/* 287 */         out.println("<hr title='triples' />");
/* 288 */         out.println("<h3><a name='triples' id='triples'>Triples of the Data Model</a></h3>");
/*     */ 
/* 290 */         out.println("<table frame='border' rules='all'><tr><td><b>Number</b></td><td><b>Subject</b></td><td><b>Predicate</b></td><td><b>Object</b></td></tr>");
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 298 */       System.err.println("Exception (printTripleTableHeader): " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printTripleTableFooter(PrintWriter out, boolean nTriples)
/*     */   {
/*     */     try
/*     */     {
/* 310 */       if (nTriples)
/* 311 */         out.println("</pre>");
/*     */       else
/* 313 */         out.println("</table>");
/*     */     } catch (Exception e) {
/* 315 */       System.err.println("Exception (printTripleTableFooter): " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printTableRow(PrintWriter out, AResource subj, AResource pred, AResource objRes, ALiteral objLit, int num)
/*     */   {
/* 330 */     out.println("<tr><td>" + num + "</td>");
/* 331 */     out.println("<td>" + addAnchor(subj) + "</td>");
/* 332 */     out.println("<td>" + addAnchor(pred) + "</td>");
/* 333 */     if (objRes != null) {
/* 334 */       out.println("<td>" + addAnchor(objRes) + "</td>");
/*     */     } else {
/* 336 */       out.println("<td>");
/* 337 */       String s1 = objLit.toString().trim();
/* 338 */       s1 = replaceString(s1, "<", "&lt;");
/* 339 */       s1 = replaceString(s1, ">", "&gt;");
/* 340 */       s1 = replaceString(s1, "&", "&amp;");
/* 341 */       s1 = "&quot;" + s1 + "&quot;";
/* 342 */       if ((objLit.getLang() != null) && (objLit.getLang().length() > 0)) {
/* 343 */         s1 = s1 + "@" + objLit.getLang();
/*     */       }
/* 345 */       if (objLit.getDatatypeURI() != null) {
/* 346 */         String s3 = objLit.getDatatypeURI();
/* 347 */         s3 = replaceString(s3, "<", "&lt;");
/* 348 */         s3 = replaceString(s3, ">", "&gt;");
/* 349 */         s3 = replaceString(s3, "&", "&amp;");
/* 350 */         if (s3.length() > 0) {
/* 351 */           s1 = s1 + "^^" + s3;
/*     */         }
/*     */       }
/* 354 */       out.println(s1);
/* 355 */       out.println("</td>");
/*     */     }
/* 357 */     out.println("</tr>");
/*     */   }
/*     */ 
/*     */   public void printFirstPart(AResource subj)
/*     */   {
/* 368 */     if (subj.isAnonymous()) {
/* 369 */       if (this.anonNodesEmpty) {
/* 370 */         Integer n = (Integer)this.subjects.get(subj.getAnonymousID());
/* 371 */         if (n == null) {
/* 372 */           this.numSubjects += 1;
/* 373 */           this.subjects.put(subj.getAnonymousID(), new Integer(this.numSubjects));
/* 374 */           this.pw.println("\"genid:" + subj.getAnonymousID() + "\" [label=\"   \"];");
/*     */         }
/*     */       }
/* 377 */       this.pw.print("\"genid:" + subj.getAnonymousID());
/*     */     } else {
/* 379 */       if ((this.gFormat != null) && (this.gFormat.equals("ISV_ZVTM")))
/* 380 */         this.pw.println("\"" + subj.getURI() + "\" [fontcolor=\"" + Float.toString(resTBh) + "," + Float.toString(resTBs) + "," + Float.toString(resTBv) + "\",URL=\"" + subj.getURI() + "\"];");
/*     */       else {
/* 382 */         this.pw.println("\"" + subj.getURI() + "\" [URL=\"" + subj.getURI() + "\"];");
/*     */       }
/* 384 */       this.pw.print("\"" + subj.getURI());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void statementDotResource(AResource subj, AResource pred, AResource obj)
/*     */   {
/* 406 */     if (this.pw == null) return;
/*     */ 
/* 408 */     printFirstPart(subj);
/*     */ 
/* 410 */     this.pw.print("\" -> ");
/*     */ 
/* 412 */     if (obj.isAnonymous()) {
/* 413 */       if (this.anonNodesEmpty) {
/* 414 */         if ((this.gFormat != null) && (this.gFormat.equals("ISV_ZVTM")))
/* 415 */           this.pw.println("\"genid:" + obj.getAnonymousID() + "\" [fontcolor=\"" + Float.toString(prpTh) + "," + Float.toString(prpTs) + "," + Float.toString(prpTv) + "\",label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/*     */         else {
/* 417 */           this.pw.println("\"genid:" + obj.getAnonymousID() + "\" [label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/*     */         }
/*     */       }
/* 420 */       else if ((this.gFormat != null) && (this.gFormat.equals("ISV_ZVTM")))
/* 421 */         this.pw.println("\"genid:" + obj.getAnonymousID() + "\" [fontcolor=\"" + Float.toString(prpTh) + "," + Float.toString(prpTs) + "," + Float.toString(prpTv) + "\",label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/*     */       else {
/* 423 */         this.pw.println("\"genid:" + obj.getAnonymousID() + "\" [label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/*     */       }
/*     */ 
/*     */     }
/* 427 */     else if ((this.gFormat != null) && (this.gFormat.equals("ISV_ZVTM"))) {
/* 428 */       this.pw.println("\"" + obj.getURI() + "\" [fontcolor=\"" + Float.toString(prpTh) + "," + Float.toString(prpTs) + "," + Float.toString(prpTv) + "\",label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/* 429 */       this.pw.println("\"" + obj.getURI() + "\" [fontcolor=\"" + Float.toString(resTBh) + "," + Float.toString(resTBs) + "," + Float.toString(resTBv) + "\",URL=\"" + obj.getURI() + "\"];");
/*     */     } else {
/* 431 */       this.pw.println("\"" + obj.getURI() + "\" [label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/* 432 */       this.pw.println("\"" + obj.getURI() + "\" [URL=\"" + obj.getURI() + "\"];");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void statementDotLiteral(AResource subj, AResource pred, ALiteral lit)
/*     */   {
/* 455 */     if (this.pw == null) return;
/*     */ 
/* 457 */     printFirstPart(subj);
/*     */ 
/* 467 */     String s1 = new String(lit.toString());
/* 468 */     s1 = s1.replace('\n', ' ');
/* 469 */     s1 = s1.replace('\f', ' ');
/* 470 */     s1 = s1.replace('\r', ' ');
/* 471 */     if (s1.indexOf(34) != -1)
/* 472 */       s1 = replaceString(s1, "\"", "\\\"");
/*     */     String tmpObject;
/* 476 */     if (s1.length() >= 80)
/* 477 */       tmpObject = s1.substring(0, 80) + " ...";
/*     */     else {
/* 479 */       tmpObject = s1.substring(0, s1.length());
/*     */     }
/*     */ 
/* 484 */     String tmpName = "Literal_" + Integer.toString(this.numLiterals);
/* 485 */     this.pw.print("\" -> \"" + tmpName);
/*     */ 
/* 487 */     if ((this.gFormat != null) && (this.gFormat.equals("ISV_ZVTM"))) {
/* 488 */       this.pw.println("\" [fontcolor=\"" + Float.toString(prpTh) + "," + Float.toString(prpTs) + "," + Float.toString(prpTv) + "\",label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/* 489 */       this.pw.println("\"" + tmpName + "\" [fontcolor=\"" + Float.toString(litTBh) + "," + Float.toString(litTBs) + "," + Float.toString(litTBv) + "\",shape=box,label=\"" + tmpObject + "\"];");
/*     */     } else {
/* 491 */       this.pw.println("\" [label=\"" + pred.getURI() + "\",URL=\"" + pred.getURI() + "\"];");
/* 492 */       this.pw.println("\"" + tmpName + "\" [shape=box,label=\"" + tmpObject + "\"];");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.check.PrintTable
 * JD-Core Version:    0.6.2
 */