/*     */ package cn.sh.library.pedigree.sparql.impl;
/*     */ 
/*     */ /*     */ import java.util.ArrayList;
/*     */ import java.util.Map;

/*     */ import javax.annotation.Resource;

/*     */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*     */ import cn.sh.library.pedigree.common.SparqlExecution;
/*     */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*     */ import cn.sh.library.pedigree.sparql.VocabSparql;
/*     */ import cn.sh.library.pedigree.utils.RDFUtils;
/*     */ 
/*     */ @Repository
/*     */ @GraphDefine(name="http://gen.library.sh.cn/graph/vocab")
/*     */ public class VocabSparqlImpl extends BaseDaoImpl
/*     */   implements VocabSparql
/*     */ {
/*     */ 
/*     */   @Resource
/*     */   private StringBuffer nsPrefix;
/*     */ 
/*     */   public ArrayList getAllClasses()
/*     */   {
/*  25 */     String query = this.nsPrefix + "SELECT DISTINCT ?s " + "WHERE {" + "   ?s a rdfs:Class . " + "}";
/*     */ 
/*  31 */     return SparqlExecution.jQuery(this.model, query, true, new String[] { "s" });
/*     */   }
/*     */ 
/*     */   public ArrayList getSubjectProperties(String class_uri)
/*     */   {
/*  36 */     if ((!class_uri.startsWith("http://")) && (class_uri.contains(":"))) {
/*  37 */       class_uri = RDFUtils.getLink(this.model, class_uri);
/*     */     }
/*     */ 
/*  40 */     String query = this.nsPrefix + "SELECT DISTINCT ?p ?label ?comment ?range " + "WHERE {" + "   ?p a rdf:Property ; rdfs:label ?label ; rdfs:range ?range . " + "   OPTIONAL {?p rdfs:comment ?comment .}" + "   {SELECT DISTINCT ?p WHERE {" + "       {" + "           ?p rdfs:domain <" + class_uri + "> ." + "       } UNION { " + "           <" + class_uri + "> rdfs:subClassOf* ?super . ?p rdfs:domain ?super. " + "       } UNION { " + "           <" + class_uri + "> rdfs:subClassOf* ?super . ?s owl:equivalentClass ?super . ?p rdfs:domain ?s. " + "       }" + "   }}" + "}";
/*     */ 
/*  56 */     return SparqlExecution.jQuery(this.model, query, true, new String[] { "p", "label", "comment", "range" });
/*     */   }
/*     */ 
/*     */   public String getPropertyType(String prop_uri)
/*     */   {
/*  61 */     if ((!prop_uri.startsWith("http://")) && (prop_uri.contains(":"))) {
/*  62 */       prop_uri = RDFUtils.getLink(this.model, prop_uri);
/*     */     }
/*     */ 
/*  65 */     String query = this.nsPrefix + "SELECT ?r " + "WHERE {" + "   <" + prop_uri + "> rdfs:range ?r ." + "}";
/*     */ 
/*  71 */     ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "r" });
/*     */ 
/*  73 */     if (results.size() > 0) {
/*  74 */       if (((Map)results.get(0)).get("r").toString().endsWith("Literal")) {
/*  75 */         return "DP";
/*     */       }
/*  77 */       return "OP";
/*     */     }
/*     */ 
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPropertyLabel(String property)
/*     */   {
/*  86 */     property = RDFUtils.getLink(this.model, property);
/*  87 */     String graph_vocab = "http://gen.library.sh.cn/graph/vocab";
/*     */ 
/*  89 */     String query = this.nsPrefix + "SELECT ?label " + "WHERE {" + "   <" + property + "> rdfs:label ?label ." + "}";
/*     */ 
/*  95 */     ArrayList results = SparqlExecution.vQuery(getModel(graph_vocab), query, new String[] { "label" });
/*     */ 
/*  97 */     if (results.size() > 0) {
/*  98 */       return ((Map)results.get(0)).get("label").toString();
/*     */     }
/*     */ 
/* 101 */     return property;
/*     */   }
/*     */ 
/*     */   public String getPropertyComment(String property)
/*     */   {
/* 106 */     property = RDFUtils.getLink(this.model, property);
/* 107 */     String graph_vocab = "http://gen.library.sh.cn/graph/vocab";
/*     */ 
/* 109 */     String query = this.nsPrefix + "SELECT ?comment " + "WHERE {" + "   <" + property + "> rdfs:comment ?comment ." + "}";
/*     */ 
/* 115 */     ArrayList results = SparqlExecution.vQuery(getModel(graph_vocab), query, new String[] { "comment" });
/*     */ 
/* 117 */     if (results.size() > 0) {
/* 118 */       return ((Map)results.get(0)).get("comment").toString();
/*     */     }
/*     */ 
/* 121 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.VocabSparqlImpl
 * JD-Core Version:    0.6.2
 */