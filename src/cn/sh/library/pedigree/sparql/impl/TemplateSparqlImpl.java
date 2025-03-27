/*    */ package cn.sh.library.pedigree.sparql.impl;
/*    */ 
/*    */ /*    */ import java.util.ArrayList;
/*    */ import java.util.Map;

/*    */ import javax.annotation.Resource;

/*    */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*    */ import cn.sh.library.pedigree.common.SparqlExecution;
/*    */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*    */ import cn.sh.library.pedigree.graph.BFProfile;
/*    */ import cn.sh.library.pedigree.sparql.TemplateSparql;
import cn.sh.library.pedigree.webApi.sparql.Namespace;
/*    */ 
/*    */ 
/*    */ @Repository
/*    */ @GraphDefine(name="http://gen.library.sh.cn/graph/profile")
/*    */ public class TemplateSparqlImpl extends BaseDaoImpl
/*    */   implements TemplateSparql
/*    */ {
///*    */ 
///*    */   @Resource
///*    */   private StringBuffer nsPrefix;
/*    */ 
/*    */   public void addBFProfile(String template_str)
/*    */   {
/* 25 */     BFProfile profile = new BFProfile();
/*    */ 
/* 27 */     this.model.add(profile.parse(template_str));
/*    */   }
/*    */ 
/*    */   public String getLatestTemplate()
/*    */   {
/* 32 */     String query = Namespace.getNsPrefixString() + "SELECT ?s " + "WHERE {" + "   ?s dc:date ?date . " + "}" + "ORDER BY DESC(?date) LIMIT 1";
/*    */ 
/* 39 */     ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "s" });
/*    */ 
/* 41 */     if (results.size() > 0) {
/* 42 */       return ((Map)results.get(0)).get("s").toString();
/*    */     }
/*    */ 
/* 45 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.TemplateSparqlImpl
 * JD-Core Version:    0.6.2
 */