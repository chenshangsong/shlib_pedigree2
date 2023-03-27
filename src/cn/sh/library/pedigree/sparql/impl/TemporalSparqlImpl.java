/*    */ package cn.sh.library.pedigree.sparql.impl;
/*    */ 
/*    */ /*    */ import java.util.ArrayList;

/*    */ import javax.annotation.Resource;

/*    */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*    */ import cn.sh.library.pedigree.common.SparqlExecution;
/*    */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*    */ import cn.sh.library.pedigree.sparql.TemporalSparql;
/*    */ 
/*    */ @GraphDefine(name="http://gen.library.sh.cn/graph/temporal")
/*    */ @Repository
/*    */ public class TemporalSparqlImpl extends BaseDaoImpl
/*    */   implements TemporalSparql
/*    */ {
/*    */ 
/*    */   @Resource
/*    */   private StringBuffer nsPrefix;
/*    */ 
/*    */   public ArrayList getTemporals4TL()
/*    */   {
/* 23 */     String query = this.nsPrefix + "SELECT DISTINCT ?uri ?name ?begin ?end " + "WHERE {" + "   ?uri a shl:Temporal ;  " + "        bf:label ?name . " + "OPTIONAL {?uri shl:beginYear ?begin . }" + "OPTIONAL {?uri shl:endYear ?end . }" + "FILTER NOT EXISTS{?uri shl:monarchName ?t .}" + "} ORDER BY DESC(?begin)";
/*    */ 
/* 33 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "name", "begin", "end" });
/*    */   }
/*    */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.TemporalSparqlImpl
 * JD-Core Version:    0.6.2
 */