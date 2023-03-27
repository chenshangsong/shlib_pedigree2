/*     */ package cn.sh.library.pedigree.graph;
/*     */ 
/*     */ /*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;

/*     */ import org.apache.commons.lang.StringUtils;

import com.github.jsonldjava.core.JsonLdError;
/*     */ import com.github.jsonldjava.core.JsonLdOptions;
/*     */ import com.github.jsonldjava.core.JsonLdProcessor;
/*     */ import com.github.jsonldjava.core.RDFDataset;
/*     */ import com.github.jsonldjava.utils.JsonUtils;
/*     */ import com.hp.hpl.jena.rdf.model.Model;
/*     */ import com.hp.hpl.jena.rdf.model.ModelFactory;
/*     */ import com.hp.hpl.jena.rdf.model.Property;
/*     */ import com.hp.hpl.jena.rdf.model.Resource;
/*     */ import com.hp.hpl.jena.vocabulary.DC;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TemplateGraph
/*     */ {
/*     */   private Object jsonObject;
/*     */ 
/*     */   public TemplateGraph(String template)
/*     */   {
/*     */     try
/*     */     {
/*  25 */       this.jsonObject = JsonUtils.fromString(template);
/*     */     } catch (IOException e) {
/*  27 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public TemplateGraph(InputStream template) {
/*     */     try {
/*  33 */       this.jsonObject = JsonUtils.fromInputStream(template);
/*     */     } catch (IOException e) {
/*  35 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Model parserTemplate()
/*     */   {
/*  41 */     List res_list = new ArrayList();
/*     */ 
/*  43 */     Model m = ModelFactory.createDefaultModel();
/*  44 */     long timestamp = new Date().getTime();
/*     */ 
/*  46 */     JsonLdOptions options = new JsonLdOptions();
/*  47 */     options.useNamespaces = Boolean.valueOf(true);
/*     */     try {
/*  49 */       RDFDataset rdf = (RDFDataset)JsonLdProcessor.toRDF(this.jsonObject, options);
/*  50 */       ListIterator listIterator = rdf.getQuads("@default").listIterator();
/*     */ 
/*  52 */       while (listIterator.hasNext()) {
/*  53 */         Object statement = listIterator.next();
/*     */ 
/*  55 */         Resource s = m.createResource(((RDFDataset.Quad)statement).getSubject().getValue());
/*     */ 
/*  57 */         if (!res_list.contains(s)) {
/*  58 */           res_list.add(s);
/*  59 */           m.add(s, DC.date, m.createLiteral(Long.toString(timestamp)));
/*     */         }
/*     */ 
/*  62 */         Property p = m.createProperty(((RDFDataset.Quad)statement).getPredicate().getValue());
/*     */ 
/*  64 */         if (((RDFDataset.Quad)statement).getObject().isIRI()) {
/*  65 */           m.add(s, p, m.createResource(((RDFDataset.Quad)statement).getObject().getValue()));
/*     */         }
/*  67 */         else if (StringUtils.isNotBlank(((RDFDataset.Quad)statement).getObject().getDatatype())) {
/*  68 */           if (!((RDFDataset.Quad)statement).getObject().getDatatype().toString().equals("http://www.w3.org/2001/XMLSchema#string"))
/*  69 */             m.add(s, p, m.createTypedLiteral(((RDFDataset.Quad)statement).getObject().getValue(), ((RDFDataset.Quad)statement).getObject().getDatatype()));
/*     */           else
/*  71 */             m.add(s, p, m.createLiteral(((RDFDataset.Quad)statement).getObject().getValue()));
/*     */         }
/*  73 */         else if (StringUtils.isNotBlank(((RDFDataset.Quad)statement).getObject().getLanguage()))
/*  74 */           m.add(s, p, m.createLiteral(((RDFDataset.Quad)statement).getObject().getValue(), ((RDFDataset.Quad)statement).getObject().getLanguage()));
/*     */         else
/*  76 */           m.add(s, p, m.createLiteral(((RDFDataset.Quad)statement).getObject().getValue()));
/*     */       }
/*     */     }
/*     */     catch (JsonLdError jsonLdError)
/*     */     {
/*  81 */       jsonLdError.printStackTrace();
/*     */     }
/*     */ 
/* 106 */     return m;
/*     */   }
/*     */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.graph.TemplateGraph
 * JD-Core Version:    0.6.2
 */