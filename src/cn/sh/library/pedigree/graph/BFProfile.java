/*    */ package cn.sh.library.pedigree.graph;
/*    */ 
/*    */ /*    */ import java.util.Date;
/*    */ import java.util.UUID;

import com.hp.hpl.jena.rdf.model.Model;
/*    */ import com.hp.hpl.jena.rdf.model.ModelFactory;
/*    */ import com.hp.hpl.jena.rdf.model.Resource;
/*    */ import com.hp.hpl.jena.vocabulary.DC;
/*    */ import com.hp.hpl.jena.vocabulary.DCTerms;
/*    */ import com.hp.hpl.jena.vocabulary.OWL;
/*    */ import com.hp.hpl.jena.vocabulary.RDFS;

/*    */ import net.sf.json.JSONArray;
/*    */ import net.sf.json.JSONObject;
/*    */ 
/*    */ public class BFProfile
/*    */ {
/*    */   public Model parse(String json_str)
/*    */   {
/* 18 */     Model model = ModelFactory.createDefaultModel();
/*    */ 
/* 20 */     JSONObject json = JSONObject.fromObject(json_str);
/* 21 */     JSONObject profile = (JSONObject)json.get("Profile");
/* 22 */     parseProfile(model, profile);
/*    */ 
/* 24 */     return model;
/*    */   }
/*    */ 
/*    */   private void parseProfile(Model model, JSONObject profile) {
/* 28 */     Resource template = model.createResource("urn:uuid:" + UUID.randomUUID());
/*    */ 
/* 30 */     if (profile.containsKey("id")) {
/* 31 */       template.addProperty(DC.identifier, profile.get("id").toString());
/*    */     }
/*    */ 
/* 34 */     if (profile.containsKey("title")) {
/* 35 */       template.addProperty(DC.title, profile.get("title").toString());
/*    */     }
/*    */ 
/* 38 */     long timestamp = new Date().getTime();
/* 39 */     template.addProperty(DC.date, model.createLiteral(Long.toString(timestamp)));
/*    */ 
/* 41 */     if (profile.containsKey("resourceTemplates")) {
/* 42 */       JSONArray resources = profile.getJSONArray("resourceTemplates");
/* 43 */       parseResource(template, model, resources);
/*    */     }
/*    */   }
/*    */ 
/*    */   private void parseResource(Resource res, Model model, JSONArray resources) {
/* 48 */     for (int i = 0; i < resources.size(); i++) {
/* 49 */       Resource r = model.createResource("urn:uuid:" + UUID.randomUUID());
/* 50 */       res.addProperty(DCTerms.hasPart, r);
/* 51 */       JSONObject resource = (JSONObject)resources.get(i);
/* 52 */       r.addProperty(OWL.equivalentClass, model.createResource(resource.get("resourceURI").toString()));
/* 53 */       r.addProperty(DC.identifier, resource.get("id").toString());
/* 54 */       r.addProperty(RDFS.label, resource.get("resourceLabel").toString());
/*    */ 
/* 56 */       JSONArray properties = resource.getJSONArray("propertyTemplates");
/* 57 */       parseProperty(r, model, properties);
/*    */     }
/*    */   }
/*    */ 
/*    */   private void parseProperty(Resource res, Model model, JSONArray properties) {
/* 62 */     for (int i = 0; i < properties.size(); i++) {
/* 63 */       Resource r = model.createResource("urn:uuid:" + UUID.randomUUID());
/* 64 */       res.addProperty(DCTerms.hasPart, r);
/* 65 */       JSONObject property = (JSONObject)properties.get(i);
/* 66 */       r.addProperty(OWL.equivalentProperty, model.createResource(((JSONObject)properties.get(i)).get("propertyURI").toString()));
/* 67 */       r.addProperty(RDFS.label, property.get("propertyLabel").toString());
/* 68 */       r.addProperty(DC.type, property.get("type").toString());
/*    */ 
/* 70 */       if (property.containsKey("mandatory")) {
/* 71 */         r.addProperty(DC.format, "mandatory:" + property.get("mandatory").toString());
/*    */       }
/*    */ 
/* 74 */       if (property.containsKey("valueConstraint")) {
/* 75 */         JSONObject constraints = (JSONObject)property.get("valueConstraint");
/* 76 */         parseValue(r, model, constraints);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   private void parseValue(Resource res, Model model, JSONObject constraints) {
/* 82 */     Resource r = model.createResource("urn:uuid:" + UUID.randomUUID());
/* 83 */     res.addProperty(DCTerms.hasPart, r);
/*    */ 
/* 85 */     if (constraints.containsKey("repeatable")) {
/* 86 */       r.addProperty(DC.format, "repeatable:" + constraints.get("repeatable").toString());
/*    */     }
/*    */ 
/* 89 */     if (constraints.containsKey("useValuesFrom")) {
/* 90 */       JSONArray values = constraints.getJSONArray("useValuesFrom");
/*    */ 
/* 92 */       for (int j = 0; j < values.size(); j++) {
/* 93 */         r.addProperty(OWL.allValuesFrom, values.get(j).toString());
/*    */       }
/*    */     }
/*    */ 
/* 97 */     if (constraints.containsKey("valueTemplateRefs")) {
/* 98 */       JSONArray refs = constraints.getJSONArray("valueTemplateRefs");
/*    */ 
/* 100 */       for (int j = 0; j < refs.size(); j++)
/* 101 */         r.addProperty(OWL.oneOf, refs.get(j).toString());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.graph.BFProfile
 * JD-Core Version:    0.6.2
 */