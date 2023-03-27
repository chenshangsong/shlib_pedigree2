/*     */ package cn.sh.library.pedigree.ontology.view;
/*     */ 
/*     */ /*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;

/*     */ import javax.annotation.Resource;

/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.springframework.stereotype.Repository;

/*     */ import com.hp.hpl.jena.ontology.OntModel;
/*     */ import com.hp.hpl.jena.rdf.model.Model;
/*     */ import com.hp.hpl.jena.rdf.model.ModelFactory;
/*     */ import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import cn.sh.library.pedigree.annoation.GraphDefine;
/*     */ import cn.sh.library.pedigree.common.CommonSparql;
/*     */ import cn.sh.library.pedigree.common.SparqlExecution;
/*     */ import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*     */ import cn.sh.library.pedigree.ontology.visualizer.AnalyzedGraph;
/*     */ import cn.sh.library.pedigree.utils.RDFUtils;
/*     */ import virtuoso.jena.driver.VirtModel;
/*     */ 
/*     */ @Repository("vocabOnt")
/*     */ @GraphDefine(name="http://gen.library.sh.cn/graph/vocab")
/*     */ public class VocabOntImpl extends BaseDaoImpl
/*     */   implements VocabOnt
/*     */ {
/*     */ 
/*     */   @Resource
/*     */   private OntModel ontModel;
/*     */ 
/*     */   @Resource
/*     */   private StringBuffer nsPrefix;
/*     */ 
/*     */   @Resource
/*     */   private CommonSparql commonSparql;
/*     */   private AnalyzedGraph aGraph;
/*     */ 
/*     */   protected void initialization()
/*     */   {
/*  43 */     if (this.model != null)
/*  44 */       this.aGraph = new AnalyzedGraph(this.model);
/*     */   }
/*     */ 
/*     */   public AnalyzedGraph getAnalyzedGraph()
/*     */   {
/*  50 */     return this.aGraph;
/*     */   }
/*     */ 
/*     */   public String getClassComment(String short_class)
/*     */   {
/*  55 */     String query = this.nsPrefix + "SELECT ?c " + "WHERE { " + "   " + short_class + " a rdfs:Class ; " + "   rdfs:comment ?c . " + "}";
/*     */ 
/*  62 */     List resultList = SparqlExecution.jQuery(this.model, query, new String[] { "c" });
/*  63 */     if (CollectionUtils.isNotEmpty(resultList)) {
/*  64 */       return ((Map)SparqlExecution.jQuery(this.model, query, new String[] { "c" }).get(0)).get("c").toString();
/*     */     }
/*  66 */     return "";
/*     */   }
/*     */ 
/*     */   public List getShortClasses()
/*     */   {
/*  73 */     List class_list = new ArrayList();
/*  74 */     ExtendedIterator ei = this.ontModel.listClasses();
/*     */ 
/*  76 */     while (ei.hasNext()) {
/*  77 */       class_list.add(RDFUtils.toShortLabel(this.model, ei.next()));
/*     */     }
/*     */ 
/*  81 */     Collections.sort(class_list);
/*     */ 
/*  83 */     return class_list;
/*     */   }
/*     */ 
/*     */   public List getShortProperties()
/*     */   {
/*  88 */     List property_list = new ArrayList();
/*  89 */     ExtendedIterator ei = this.ontModel.listDatatypeProperties();
/*     */ 
/*  91 */     while (ei.hasNext()) {
/*  92 */       property_list.add(RDFUtils.toShortLabel(this.model, ei.next()));
/*     */     }
/*     */ 
/*  95 */     ei = this.ontModel.listObjectProperties();
/*     */ 
/*  97 */     while (ei.hasNext()) {
/*  98 */       property_list.add(RDFUtils.toShortLabel(this.model, ei.next()));
/*     */     }
/*     */ 
/* 102 */     Collections.sort(property_list);
/*     */ 
/* 104 */     return property_list;
/*     */   }
/*     */ 
/*     */   public ArrayList getClassInfos(String short_class)
/*     */   {
	try {
		/* 112 */     String query = this.nsPrefix + "SELECT ?p ?o " + "WHERE { " + "   " + short_class + " a rdfs:Class ; " + "   ?p ?o . " + "FILTER (?p != rdf:type)" + "}";
		/*     */ 
		/* 120 */     return SparqlExecution.jQuery(this.model, query, true, new String[] { "p", "o" });
	} catch (Exception e) {
		System.out.println(e);
	}
return null;
/*     */   }
/*     */ 
/*     */   public ArrayList getPropertyInfos(String short_property)
/*     */   {
/* 128 */     String query = this.nsPrefix + "SELECT ?p ?o " + "WHERE { " + "   " + short_property + " a rdf:Property ; " + "   ?p ?o . " + "FILTER (?p != rdf:type)" + "}";
/*     */ 
/* 136 */     return SparqlExecution.jQuery(this.model, query, true, new String[] { "p", "o" });
/*     */   }
/*     */ 
/*     */   public List getClassProperties(String short_class)
/*     */   {
/* 141 */     List props = new ArrayList();
/*     */ 
/* 143 */     String query = this.nsPrefix + "SELECT DISTINCT ?p " + "WHERE { " + "   ?p a rdf:Property ; " + "      rdfs:domain " + short_class + " . " + "}";
/*     */ 
/* 150 */     ArrayList results = SparqlExecution.jQuery(this.model, query, true, new String[] { "p" });
/*     */ 
/* 152 */     if (results.size() > 0) {
/* 153 */       for (Iterator localIterator = results.iterator(); localIterator.hasNext(); ) { Object r = localIterator.next();
/* 154 */         props.add(((Map)r).get("p").toString());
/*     */       }
/*     */ 
/* 157 */       Collections.sort(props);
/*     */ 
/* 159 */       return props;
/*     */     }
/*     */ 
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   public List getModelViewClasses()
/*     */   {
/* 167 */     List list = new LinkedList();
/*     */ 
/* 169 */     String query = this.nsPrefix + "SELECT DISTINCT ?s " + "WHERE { " + "   ?s rdfs:label 'Model View' . " + "}";
/*     */ 
/* 175 */     ArrayList results = SparqlExecution.jQuery(getModel("http://gen.library.sh.cn/graph/baseinfo"), query, true, new String[] { "s" });
/*     */ 
/* 177 */     for (int i = 0; i < results.size(); i++) {
/* 178 */       list.add(((Map)results.get(i)).get("s"));
/*     */     }
/*     */ 
/* 181 */     Collections.sort(list);
/*     */ 
/* 183 */     return list;
/*     */   }
/*     */ 
/*     */   public List getSuperClasses()
/*     */   {
/* 188 */     List list = new LinkedList();
/*     */ 
/* 190 */     String query = this.nsPrefix + "SELECT DISTINCT ?sc " + "WHERE { " + "   ?s a rdfs:Class; " + "      rdfs:subClassOf ?sc . " + "FILTER NOT EXISTS{" + "   ?sc rdfs:subClassOf ?a ." + "} " + "}";
/*     */ 
/* 200 */     ArrayList results = SparqlExecution.jQuery(this.model, query, true, new String[] { "sc" });
/*     */ 
/* 202 */     for (int i = 0; i < results.size(); i++) {
/* 203 */       list.add(((Map)results.get(i)).get("sc"));
/*     */     }
/*     */ 
/* 206 */     Collections.sort(list);
/*     */ 
/* 208 */     return list;
/*     */   }
/*     */ 
/*     */   public List getSubClasses(String short_class)
/*     */   {
/* 213 */     List list = new LinkedList();
/*     */ 
/* 215 */     String prefix = short_class.split(":")[0];
/* 216 */     String uri = this.model.getNsPrefixURI(prefix) + short_class.split(":")[1];
/*     */ 
/* 221 */     String query = this.nsPrefix + "SELECT DISTINCT ?s " + "WHERE { " + "   ?s a rdfs:Class; " + "      rdfs:subClassOf <" + uri + "> . " + "}";
/*     */ 
/* 228 */     ArrayList results = SparqlExecution.jQuery(this.model, query, true, new String[] { "s" });
/*     */ 
/* 230 */     for (int i = 0; i < results.size(); i++) {
/* 231 */       list.add(((Map)results.get(i)).get("s"));
/*     */     }
/*     */ 
/* 234 */     Collections.sort(list);
/*     */ 
/* 236 */     return list;
/*     */   }
/*     */ 
/*     */   public OutputStream write()
/*     */   {
/* 241 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 242 */     this.ontModel.write(stream, "RDF/XML-ABBREV");
/*     */ 
/* 244 */     return stream;
/*     */   }
/*     */ 
/*     */   public OutputStream write(String short_term)
/*     */   {
/* 249 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/*     */ 
/* 251 */     String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "FROM <" + "http://gen.library.sh.cn/graph/vocab" + ">" + "WHERE { " + " ?s ?p ?o . " + "FILTER (?s = " + short_term + ")" + "}";
/*     */ 
/* 259 */     Model temp = ModelFactory.createDefaultModel();
/*     */     try
/*     */     {
/* 262 */       temp.add(SparqlExecution.construct((VirtModel)this.model, query));
/* 263 */       temp.write(stream, "RDF/XML-ABBREV");
/*     */ 
/* 265 */       return stream;
/*     */     } finally {
/* 267 */       temp.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean validate(String s, String type)
/*     */   {
/* 273 */     String rdf_type = "rdfs:Class";
/*     */ 
/* 275 */     if (type.toLowerCase().equals("property")) {
/* 276 */       rdf_type = "rdf:Property";
/*     */     }
/*     */ 
/* 279 */     String query = this.nsPrefix + "SELECT * " + "WHERE {" + "   " + s + " a ?o ." + "}";
/*     */ 
/* 285 */     if (SparqlExecution.ask(this.model, query)) {
/* 286 */       query = this.nsPrefix + "SELECT * " + "WHERE {" + "   " + s + " a " + rdf_type + " ." + "}";
/*     */ 
/* 292 */       if (SparqlExecution.ask(this.model, query)) {
/* 293 */         return true;
/*     */       }
/* 295 */       return false;
/*     */     }
/*     */ 
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean modify(String s, String p, String o, String type)
/*     */   {
/* 304 */     String rdf_type = "rdfs:Class";
/*     */ 
/* 306 */     if (type.toLowerCase().equals("property")) {
/* 307 */       rdf_type = "rdf:Property";
/*     */     }
/*     */ 
/* 310 */     String insert_str = "";
/*     */ 
/* 312 */     if (o.contains(";")) {
/* 313 */       String[] o_s = o.split(";");
/*     */ 
/* 315 */       for (int i = 0; i < o_s.length; i++)
/* 316 */         insert_str = insert_str + s + " " + p + " " + o_s[i] + ". ";
/*     */     }
/*     */     else {
/* 319 */       insert_str = s + " " + p + " " + o + ". ";
/*     */     }
/*     */ 
/* 322 */     String str = this.nsPrefix + "WITH <" + "http://gen.library.sh.cn/graph/vocab" + "> " + "DELETE {" + "   " + s + " " + p + " ?o ." + "} " + "INSERT {" + insert_str + "} " + "WHERE {" + "   " + s + " a " + rdf_type + " ;" + "              " + p + " ?o ." + "}";
/*     */ 
/* 333 */     SparqlExecution.update(this.graph, str);
/*     */ 
/* 335 */     return this.commonSparql.isExist("http://gen.library.sh.cn/graph/vocab", s, p, o);
/*     */   }
/*     */ 
/*     */   public boolean delete(String s, String p, String o, String type)
/*     */   {
/* 340 */     String rdf_type = "rdfs:Class";
/*     */ 
/* 342 */     if (type.toLowerCase().equals("property")) {
/* 343 */       rdf_type = "rdf:Property";
/*     */     }
/*     */ 
/* 346 */     String delete_str = "";
/*     */ 
/* 348 */     if (o.contains(";")) {
/* 349 */       String[] o_s = o.split(";");
/*     */ 
/* 351 */       for (int i = 0; i < o_s.length; i++)
/* 352 */         delete_str = delete_str + s + " " + p + " " + o_s[i] + ". ";
/*     */     }
/*     */     else {
/* 355 */       delete_str = s + " " + p + " " + o + ". ";
/*     */     }
/*     */ 
/* 358 */     String str = this.nsPrefix + "WITH <" + "http://gen.library.sh.cn/graph/vocab" + "> " + "DELETE {" + delete_str + "} " + "WHERE {" + "   " + s + " a " + rdf_type + ". " + delete_str + "}";
/*     */ 
/* 365 */     SparqlExecution.update(this.graph, str);
/*     */ 
/* 367 */     if (!this.commonSparql.isExist("http://gen.library.sh.cn/graph/vocab", s, p, o)) {
/* 368 */       return true;
/*     */     }
/*     */ 
/* 371 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean create(String s, String p, String o, String type)
/*     */   {
/* 376 */     String rdf_type = "rdfs:Class";
/*     */ 
/* 378 */     if (type.toLowerCase().equals("property")) {
/* 379 */       rdf_type = "rdf:Property";
/*     */     }
/*     */ 
/* 382 */     String insert_str = "";
/*     */ 
/* 384 */     if (o.contains(";")) {
/* 385 */       String[] o_s = o.split(";");
/*     */ 
/* 387 */       for (int i = 0; i < o_s.length; i++)
/* 388 */         insert_str = insert_str + s + " " + p + " " + o_s[i] + ". ";
/*     */     }
/*     */     else {
/* 391 */       insert_str = s + " " + p + " " + o + ". ";
/*     */     }
/*     */ 
/* 394 */     String str = this.nsPrefix + "WITH <" + "http://gen.library.sh.cn/graph/vocab" + "> " + "INSERT {" + "   " + s + " a " + rdf_type + ". " + insert_str + "}";
/*     */ 
/* 400 */     SparqlExecution.update(this.graph, str);
/*     */ 
/* 402 */     return this.commonSparql.isExist("http://gen.library.sh.cn/graph/vocab", s, p, o);
/*     */   }
/*     */ 
/*     */   public boolean importRDF(InputStream in, boolean full)
/*     */   {
/* 407 */     if (full) {
/* 408 */       this.model.removeAll();
/*     */     }
/*     */ 
/* 411 */     Model m = ModelFactory.createDefaultModel();
/* 412 */     m.read(in, "");
/*     */     try
/*     */     {
/* 415 */       this.model.add(m);
/*     */     } finally {
/* 417 */       m.close();
/*     */     }
/*     */ 
/* 420 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean importRDF(String str, boolean full)
/*     */   {
/* 425 */     if (full) {
/* 426 */       this.model.removeAll();
/*     */     }
/*     */ 
/* 429 */     Model m = ModelFactory.createDefaultModel();
/* 430 */     m.read(str);
/*     */     try
/*     */     {
/* 433 */       this.model.add(m);
/*     */     } finally {
/* 435 */       m.close();
/*     */     }
/*     */ 
/* 438 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.view.VocabOntImpl
 * JD-Core Version:    0.6.2
 */