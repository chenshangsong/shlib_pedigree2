/*    */ package cn.sh.library.pedigree.sparql.impl;
/*    */ 
/*    */ /*    */ import java.util.Map;

/*    */ import javax.annotation.Resource;

/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*    */ import cn.sh.library.pedigree.common.SparqlExecution;
/*    */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*    */ import cn.sh.library.pedigree.dto.QueryResult;
/*    */ import cn.sh.library.pedigree.sparql.TitleSparql;
/*    */ import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.webApi.sparql.Namespace;
/*    */ 
/*    */ 
/*    */ @GraphDefine(name="http://gen.library.sh.cn/graph/title")
/*    */ @Repository
/*    */ public class TitleSparqlImpl extends BaseDaoImpl
/*    */   implements TitleSparql
/*    */ {
///*    */ 
///*    */   @Resource
///*    */   private StringBuffer nsPrefix;
/*    */ 
/*    */   public QueryResult<Map<String, Object>> getTitles(String q, int start, int size)
/*    */   {
/* 27 */     String filter = "";
/*    */ 
/* 29 */     if (StringUtils.isNotBlank(q)) {
/* 30 */       filter = "FILTER CONTAINS(?chs, '" + q + "')";
/*    */     }
/*    */ 
/* 33 */     String countQuery = Namespace.getNsPrefixString() + "SELECT (COUNT(DISTINCT ?uri) AS ?count) " + "WHERE {" + "   ?uri a bf:WorkTitle ;" + "        bf:label ?chs . " + filter + "}";
/*    */ 
/* 40 */     String query = Namespace.getNsPrefixString() + "SELECT DISTINCT ?uri (sql:BEST_LANGMATCH(?chs, 'cht;q=0.8, chs;q=0.7, ;q=0.6, en;q=0.5, *;q=0.1', 'cht')) as ?title " + "WHERE {" + "   ?uri a bf:WorkTitle ; " + "        bf:label ?chs . " + filter + "} " + "OFFSET " + start + " LIMIT " + size;
/*    */ 
/* 48 */     Map countMap = (Map)SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
/* 49 */     Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));
/*    */ 
/* 51 */     QueryResult result = new QueryResult();
/* 52 */     result.setTotalrecord(count);
/* 53 */     if (count.longValue() > 0L) {
/* 54 */       result.setResultList(SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "title" }));
/*    */     }
/* 56 */     return result;
/*    */   }
/*    */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.TitleSparqlImpl
 * JD-Core Version:    0.6.2
 */