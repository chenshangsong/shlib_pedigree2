/*    */ package cn.sh.library.pedigree.ontology.check;
/*    */ 
/*    */ /*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringReader;
/*    */ import java.io.StringWriter;

/*    */ import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.arp.ARP;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ARPValidation
/*    */ {
/*    */   public static String run(String str)
/*    */   {
/* 13 */     StringWriter stringWriter = new StringWriter();
/* 14 */     PrintWriter out = new PrintWriter(stringWriter);
/* 15 */     StringWriter sw = new StringWriter();
/* 16 */     PrintWriter outtmp = new PrintWriter(sw);
/*    */ 
/* 18 */     ARP arp = new ARP();
/*    */ 
/* 21 */     PrintTable sh = new PrintTable(outtmp, false, true);
/* 22 */     RDFErrorHandler errorHandler = new RDFErrorHandler(out);
/*    */ 
/* 24 */     arp.getHandlers().setErrorHandler(errorHandler);
/* 25 */     arp.getHandlers().setStatementHandler(sh);
/* 26 */     arp.getOptions().setEmbedding(true);
/* 27 */     arp.getOptions().setStrictErrorMode();
/*    */ 
/* 30 */     sh.printTripleTableHeader(outtmp, false);
/*    */     try
/*    */     {
/* 33 */       arp.load(new StringReader(str));
/*    */     } catch (SAXException e) {
/* 35 */       e.printStackTrace();
/*    */     } catch (IOException e) {
/* 37 */       e.printStackTrace();
/*    */     }
/*    */ 
/* 41 */     sh.printTripleTableFooter(outtmp, false);
/*    */ 
/* 44 */     errorHandler.printErrorMessages(out, errorHandler);
/*    */ 
/* 46 */     StringBuffer buffer = stringWriter.getBuffer();
/* 47 */     if (buffer.toString().contains("Your RDF document validated successfully.")) {
/* 48 */       out.print(sw);
/*    */     }
/*    */     else {
/* 51 */       PrintListing.run(out, str, true);
/*    */     }
/*    */ 
/* 54 */     buffer = stringWriter.getBuffer();
/* 55 */     return buffer.toString();
/*    */   }
/*    */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.check.ARPValidation
 * JD-Core Version:    0.6.2
 */