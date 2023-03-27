/*    */ package cn.sh.library.pedigree.graph;
/*    */ import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
/*    */ import com.hp.hpl.jena.rdf.model.Literal;
/*    */ import com.hp.hpl.jena.rdf.model.Model;
/*    */ import com.hp.hpl.jena.rdf.model.ModelFactory;
/*    */ import com.hp.hpl.jena.rdf.model.Property;
/*    */ import com.hp.hpl.jena.rdf.model.Resource;
/*    */ import com.hp.hpl.jena.vocabulary.RDF;

import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
/*    */ import cn.sh.library.pedigree.utils.RDFUtils;
/*    */ 
/*    */ /*    */ import net.sf.json.JSONArray;
/*    */ import net.sf.json.JSONObject;
/*    */ 
/*    */ public class DataParser extends BaseDaoImpl
/*    */ {
/*    */   public void launch(JSONArray json)
/*    */   {
/* 17 */     for (int i = 0; i < json.size(); i++) {
/* 18 */       JSONObject resource = (JSONObject)json.get(i);
/*    */ 
/* 20 */       if ((resource.containsKey("uri")) && (resource.containsKey("graph"))) {
/* 21 */         String graph = resource.get("graph").toString();
/* 22 */         Model model = getModel(graph);
/* 23 */         model.enterCriticalSection(false);
/*    */ 
/* 25 */         Model m = ModelFactory.createDefaultModel();
/* 26 */         m.setNsPrefixes(model.getNsPrefixMap());
/*    */ 
/* 28 */         Resource s = m.createResource(resource.get("uri").toString());
/* 29 */         s.addProperty(RDF.type, m.createResource(RDFUtils.getLink(model, resource.get("type").toString())));
/* 30 */         JSONArray properties = resource.getJSONArray("properties");
/*    */ 
/* 32 */         parseProperties(m, s, properties);
/*    */ 
/* 34 */         model.add(m);
/* 35 */         m.close();
/*    */ 
/* 37 */         model.leaveCriticalSection();
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   private void parseProperties(Model model, Resource s, JSONArray properties) {
/* 43 */     for (int i = 0; i < properties.size(); i++) {
/* 44 */       JSONObject property = (JSONObject)properties.get(i);
/*    */ 
/* 46 */       if (property.get("repeatable").toString().equals("true")) {
/* 47 */         JSONArray objects = property.getJSONArray("objects");
/* 48 */         parseObjects(model, s, property, objects);
/* 49 */       } else if (property.get("repeatable").toString().equals("false")) {
/* 50 */         JSONObject object = (JSONObject)property.get("objects");
/* 51 */         parseObjects(model, s, property, object);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   private void parseObjects(Model model, Resource s, JSONObject property, JSONObject object) {
/* 57 */     Property p = model.createProperty(RDFUtils.getLink(model, property.get("uri").toString()));
/*    */ 
/* 59 */     if (property.get("type").toString().toLowerCase().equals("literal")) {
/* 60 */       String v = object.get("value").toString();
/*    */ 
/* 62 */       if (object.containsKey("language")) {
/* 63 */         Literal o = model.createLiteral(v, object.get("language").toString());
/* 64 */         s.addProperty(p, o);
/* 65 */       } else if (object.containsKey("datatype")) {
/* 66 */         String dt = object.get("datatype").toString().toLowerCase();
/* 67 */         Literal o = null;
/*    */ 
/* 69 */         if (dt.equals("int"))
/* 70 */           o = model.createTypedLiteral(v, XSDDatatype.XSDinteger);
/* 71 */         else if (dt.equals("date"))
/* 72 */           o = model.createTypedLiteral(v, XSDDatatype.XSDdate);
/* 73 */         else if (dt.equals("boolean")) {
/* 74 */           o = model.createTypedLiteral(v, XSDDatatype.XSDboolean);
/*    */         }
/*    */ 
/* 77 */         s.addProperty(p, o);
/*    */       } else {
/* 79 */         Literal o = model.createLiteral(v);
/* 80 */         s.addProperty(p, o);
/*    */       }
/*    */     }
/*    */ 
/* 84 */     if (property.get("type").toString().toLowerCase().equals("resource"))
/* 85 */       s.addProperty(p, model.createResource(object.get("value").toString()));
/*    */   }
/*    */ 
/*    */   private void parseObjects(Model model, Resource s, JSONObject property, JSONArray objects)
/*    */   {
/* 90 */     for (int i = 0; i < objects.size(); i++) {
/* 91 */       JSONObject object = (JSONObject)objects.get(i);
/* 92 */       parseObjects(model, s, property, object);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.graph.DataParser
 * JD-Core Version:    0.6.2
 */