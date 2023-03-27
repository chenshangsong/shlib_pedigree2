/*     */ package cn.sh.library.pedigree.sparql.impl;
/*     */ 
/*     */ /*     */ import java.util.ArrayList;
/*     */ import java.util.Map;

/*     */ import javax.annotation.Resource;

/*     */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*     */ import cn.sh.library.pedigree.common.SparqlExecution;
/*     */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*     */ import cn.sh.library.pedigree.sparql.MigrationSparql;
/*     */ 
/*     */ @Repository
/*     */ @GraphDefine(name="http://gen.library.sh.cn/graph/migration")
/*     */ public class MigrationSparqlImpl extends BaseDaoImpl
/*     */   implements MigrationSparql
/*     */ {
/*     */ 
/*     */   @Resource
/*     */   private StringBuffer nsPrefix;
/*     */ 
/*     */   public ArrayList getMigrations()
/*     */   {
/*  23 */     String query = this.nsPrefix + "SELECT ?uri ?name ?begin ?start_label (CONCAT(?sl, ' ', ?st) AS ?start_geo) ?dest_label (CONCAT(?dl, ' ' , ?dt) AS ?dest_geo) " + "WHERE {" + "   ?uri a shl:Migration ; " + "        bf:eventAgent/foaf:name ?name ; " + "        shl:locality ?dest ; " + "        shl:originalLocality ?start ; " + "        shl:temporal/shl:beginYear ?begin. " + "   ?start geo:long ?sl ; " + "          geo:lat ?st ; " + "          bf:label ?start_label ." + "   ?dest geo:long ?dl ; " + "         geo:lat ?dt ; " + "         bf:label ?dest_label ." + "} ORDER BY ASC(?begin)";
/*     */ 
/*  39 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "name", "begin", "start_label", "start_geo", "dest_label", "dest_geo" });
/*     */   }
/*     */ 
/*     */   public ArrayList getMigrationLocations()
/*     */   {
/*  44 */     String query = this.nsPrefix + "SELECT DISTINCT ?label ?long ?lat " + "WHERE { " + "   ?uri a shl:Migration ; " + "        ?p ?s ; " + "        shl:temporal/shl:beginYear ?begin . " + "   ?s geo:long ?long ; " + "      geo:lat ?lat ; " + "      bf:label ?label . " + "} order by asc(?begin)";
/*     */ 
/*  55 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "label", "long", "lat" });
/*     */   }
/*     */ 
/*     */   public ArrayList getMigrationLines()
/*     */   {
/*  60 */     String query = this.nsPrefix + "SELECT DISTINCT ?start ?dest " + "WHERE { " + "   ?uri a shl:Migration ; " + "        shl:locality ?dest_uri ; " + "        shl:originalLocality ?start_uri ; " + "        shl:temporal/shl:beginYear ?begin. " + "   ?start_uri bf:label ?start . " + "   ?dest_uri bf:label ?dest . " + "} order by asc(?begin)";
/*     */ 
/*  71 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "start", "dest" });
/*     */   }
/*     */ 
/*     */   public String getFirstLevel()
/*     */   {
/*  76 */     String query = this.nsPrefix + "SELECT ?uri " + "WHERE {" + "   ?uri a shl:Person . " + "FILTER NOT EXISTS {?uri rel:childOf ?parent . }" + "}";
/*     */ 
/*  83 */     ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "uri" });
/*     */ 
/*  85 */     if (results.size() > 0) {
/*  86 */       return ((Map)results.get(0)).get("uri").toString();
/*     */     }
/*     */ 
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public ArrayList getAfterLevel(String uri)
/*     */   {
/*  94 */     String query = this.nsPrefix + "SELECT ?uri " + "WHERE {" + "   ?uri a shl:Person ; " + "        rel:childOf <" + uri + "> . " + "OPTIONAL { " + "   ?uri shl:orderOfSeniority ?order . " + "   ?seq rdfs:comment ?order ; rdfs:label ?num . } " + "} ORDER BY ?num ";
/*     */ 
/* 104 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "uri" });
/*     */   }
/*     */ 
/*     */   public Map getInfos(String uri)
/*     */   {
/* 174 */     String query = this.nsPrefix + "SELECT (CONCAT(?sname, '/', ?oname) AS ?fns) " + "?sn ?zi ?hao ?order ?start ?end (GROUP_CONCAT(DISTINCT ?desc; separator=';') AS ?descs) " + "WHERE {" + "   <" + uri + "> a shl:Person ;" + "    foaf:name ?sname ; shl:name ?sn . " + "OPTIONAL {<" + uri + "> shl:pseudonym ?hao .}" + "OPTIONAL {<" + uri + "> shl:courtesyName ?zi .}" + "OPTIONAL {<" + uri + "> shl:orderOfSeniority ?order .}" + "OPTIONAL {<" + uri + "> shl:description ?desc .}" + "OPTIONAL {<" + uri + "> shl:birthday ?start .}" + "OPTIONAL {<" + uri + "> shl:deathday ?end .}" + "OPTIONAL {<" + uri + "> foaf:name ?oname . FILTER (!CONTAINS(?oname, ?sn))} " + "FILTER CONTAINS(?sname, ?sn)" + "}";
/*     */ 
/* 191 */     return (Map)SparqlExecution.vQuery(this.graph, query, new String[] { "fns", "sn", "zi", "hao", "order", "start", "end", "descs" }).get(0);
/*     */   }
/*     */ 
/*     */   public String getUriFromName(String name)
/*     */   {
/* 196 */     String query = this.nsPrefix + "SELECT ?uri " + "WHERE {" + "   ?uri foaf:name '" + name + "' . " + "}";
/*     */ 
/* 202 */     ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "uri" });
/*     */ 
/* 204 */     if (results.size() > 0) {
/* 205 */       return ((Map)results.get(0)).get("uri").toString();
/*     */     }
/*     */ 
/* 208 */     return "";
/*     */   }
/*     */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.MigrationSparqlImpl
 * JD-Core Version:    0.6.2
 */