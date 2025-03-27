/*     */ package cn.sh.library.pedigree.sparql.impl;
/*     */ 
/*     */ /*     */ import java.util.ArrayList;
/*     */ import java.util.Map;

/*     */ import javax.annotation.Resource;

/*     */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*     */ import cn.sh.library.pedigree.common.SparqlExecution;
/*     */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*     */ import cn.sh.library.pedigree.sparql.GeoSparql;
/*     */ import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.sparql.Namespace;
/*     */ 
/*     */ @Repository
/*     */ @GraphDefine(name="http://www.cba.ac.cn/graph/geography")
/*     */ public class GeoSparqlImpl extends BaseDaoImpl
/*     */   implements GeoSparql
/*     */ {
///*     */ 
///*     */   @Resource
///*     */   private StringBuffer nsPrefix;
/*     */ 
/*     */   public String getLongLat(String city)
/*     */   {
/*  26 */     String query = Namespace.getNsPrefixString() + "SELECT ?long ?lat " + "WHERE { " + "   ?s geo:long ?long ; " + "      geo:lat ?lat ;" + "      rdfs:label '" + city + "'. " + "}";
/*     */ 
/*  34 */     ArrayList results = SparqlExecution.vQuery(this.model, query, new String[] { "long", "lat" });
/*     */ 
/*  36 */     if (results.size() > 0) {
/*  37 */       String _long = RDFUtils.getValue(((Map)results.get(0)).get("long").toString());
/*  38 */       String _lat = RDFUtils.getValue(((Map)results.get(0)).get("lat").toString());
/*     */ 
/*  40 */       return _long + "," + _lat;
/*     */     }
/*     */ 
/*  43 */     return null;
/*     */   }
/*     */ 
/*     */   public String getCitySubject(String city)
/*     */   {
/*  48 */     String query = Namespace.getNsPrefixString() + "SELECT ?s " + "WHERE { " + "   ?s0 rdfs:label ?label ; " + "      gn:parentADM1 ?p1 . " + "   ?s rdfs:label ?label ; " + "      gn:parentADM2 ?p2 . " + "FILTER STRSTARTS(?label, '" + city + "')" + "}";
/*     */ 
/*  58 */     ArrayList results = SparqlExecution.vQuery(this.model, query, new String[] { "s" });
/*     */ 
/*  60 */     if (results.size() > 0) {
/*  61 */       return ((Map)results.get(0)).get("s").toString();
/*     */     }
/*     */ 
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   public String getCitySubject(String prov, String locate)
/*     */   {
/*  69 */     String query = "";
/*     */ 
/*  71 */     if (null == locate) {
/*  72 */       query = Namespace.getNsPrefixString() + "SELECT ?s " + "WHERE { " + "   ?s rdfs:label ?l . " + "FILTER STRSTARTS(?l, '" + prov + "')" + "}";
/*     */     }
/*     */     else
/*     */     {
/*  79 */       query = Namespace.getNsPrefixString() + "SELECT ?s " + "WHERE { " + "   ?s gn:parentADM2/gn:parentADM1 ?p ; rdfs:label ?l . " + "   ?p rdfs:label ?p_l . " + "FILTER STRSTARTS(?p_l, '" + prov + "')" + "FILTER STRSTARTS(?l, '" + locate + "')" + "}";
/*     */     }
/*     */ 
/*  89 */     ArrayList results = SparqlExecution.vQuery(this.model, query, new String[] { "s" });
/*     */ 
/*  91 */     if (results.size() > 0) {
/*  92 */       return ((Map)results.get(0)).get("s").toString();
/*     */     }
/*     */ 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTownNumber(String county_uri)
/*     */   {
/* 100 */     String query = Namespace.getNsPrefixString() + "SELECT ?s " + "WHERE { " + "   ?s gn:parentADM3 <" + county_uri + "> . " + "} ORDER BY DESC(?s) LIMIT 1";
/*     */ 
/* 106 */     ArrayList results = SparqlExecution.vQuery(this.model, query, new String[] { "s" });
/*     */ 
/* 108 */     if (results.size() > 0) {
/* 109 */       String uri = ((Map)results.get(0)).get("s").toString();
/* 110 */       String no = org.apache.commons.lang.StringUtils.substringAfterLast(uri, "/").split("-")[1];
/*     */ 
/* 112 */       int number = Integer.parseInt(no.replaceFirst("^0*", "")) + 1;
/* 113 */       return String.format("%04d", new Object[] { Integer.valueOf(number) });
/*     */     }
/*     */ 
/* 116 */     return "0001";
/*     */   }
/*     */ 
/*     */   public String getCity4Point(String _long, String lat)
/*     */   {
/* 121 */     String query = Namespace.getNsPrefixString() + "SELECT ?label " + "WHERE {" + "   ?s geo:long '" + _long + "'^^xsd:double ; " + "      geo:lat '" + lat + "'^^xsd:double ; " + "      rdfs:label ?label . " + "}";
/*     */ ArrayList _list =SparqlExecution.vQuery(this.graph, query, new String[] { "label" });
                    if(_list!=null && _list.size()>0){
                    	return StringUtilC.getString(((Map)_list.get(0)).get("label"));
                    }
                    return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.GeoSparqlImpl
 * JD-Core Version:    0.6.2
 */