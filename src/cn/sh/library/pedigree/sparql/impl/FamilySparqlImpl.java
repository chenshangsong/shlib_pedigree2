/*    */ package cn.sh.library.pedigree.sparql.impl;
/*    */ 
/*    */ /*    */ import java.util.ArrayList;

/*    */ import javax.annotation.Resource;

/*    */ import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*    */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*    */ import cn.sh.library.pedigree.sparql.FamilySparql;
/*    */ 
/*    */ @Repository
/*    */ @GraphDefine(name="http://gen.library.sh.cn/graph/family")
/*    */ public class FamilySparqlImpl extends BaseDaoImpl
/*    */   implements FamilySparql
/*    */ {
/*    */ 
/*    */   @Resource
/*    */   private StringBuffer nsPrefix;
/*    */ 
/*    */   public ArrayList getFamilyRelations(String work_uri)
/*    */   {
/* 22 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.impl.FamilySparqlImpl
 * JD-Core Version:    0.6.2
 */