package cn.sh.library.pedigree.ontology.model;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Property;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.AppConfig;

@Repository
@GraphDefine(name="http://gen.library.sh.cn/graph/vocab")
public class VocabOntModel extends BaseDaoImpl
{
   private Logger logger = Logger.getLogger(VocabOntModel.class);

  @javax.annotation.Resource
  private OntModel ontModel;

  @javax.annotation.Resource
  private StringBuffer nsPrefix;

  @javax.annotation.Resource
  private AppConfig appConfig;

   protected void initialization() { if (this.nsPrefix.length() > 0)
       return;
    try
    {
       setNS();
       createClass();
       createProperty();
    } catch (Exception e) {
       this.logger.error("初始化异常", e);
    } }

  private void setNS()
  {
     for (String ns : this.appConfig.getNsPrefixs()) {
       this.ontModel.setNsPrefix(ns, this.model.getNsPrefixURI(ns));
    }

     Map<String,String> map = this.ontModel.getNsPrefixMap();

     for (Map.Entry entry : map.entrySet())
       this.nsPrefix.append("PREFIX " + entry.getKey() + ":<" + entry.getValue() + ">\n");
  }

  private void createProperty()
  {
     String query = this.nsPrefix + "SELECT DISTINCT ?s " + "WHERE { " + "   ?s a rdf:Property . " + "} ORDER BY ASC(?s)";

     ArrayList results = SparqlExecution.jQuery(this.model, query, new String[] { "s" });

     for (int i = 0; i < results.size(); i++) {
       String p_s = ((Map)results.get(i)).get("s").toString();
       OntProperty op = null;

       query = this.nsPrefix + "SELECT ?r " + "WHERE { " + "   <" + p_s + "> rdfs:range ?r . " + "}";

       String range = ((Map)SparqlExecution.jQuery(this.model, query, new String[] { "r" }).get(0)).get("r").toString();

       if (range.endsWith("Literal"))
         op = this.ontModel.createDatatypeProperty(p_s);
      else {
         op = this.ontModel.createObjectProperty(p_s);
      }

       query = this.nsPrefix + "SELECT ?p ?o " + "WHERE { " + "   <" + p_s + "> ?p ?o . " + "}";

       ArrayList results1 = SparqlExecution.jQuery(this.model, query, new String[] { "p", "o" });

       for (int j = 0; j < results1.size(); j++) {
         Object p = ((Map)results1.get(j)).get("p");
         Object o = ((Map)results1.get(j)).get("o");

         if (p.toString().endsWith("label"))
           op.addLabel(this.ontModel.createLiteral(o.toString()));
         else if (p.toString().endsWith("comment")) {
           op.addComment(this.ontModel.createLiteral(o.toString()));
        }

         if (p.toString().endsWith("domain")) {
           com.hp.hpl.jena.rdf.model.Resource domain_class = this.ontModel.createResource(o.toString());

           op.addDomain(domain_class);
         } else if (p.toString().endsWith("range")) {
           com.hp.hpl.jena.rdf.model.Resource range_class = this.ontModel.createResource(o.toString());

           op.addRange(range_class);
        }

         if (p.toString().endsWith("subPropertyOf")) {
           Property super_property = this.ontModel.createProperty(o.toString());

           op.addSuperProperty(super_property);
        }
      }
    }
  }

  private void createClass() {
     String query = this.nsPrefix + "SELECT DISTINCT ?s " + "WHERE { " + "   ?s a rdfs:Class . " + "} ORDER BY ASC(?s)";

     ArrayList results = SparqlExecution.jQuery(this.model, query, new String[] { "s" });

     for (int i = 0; i < results.size(); i++) {
       String c_s = ((Map)results.get(i)).get("s").toString();
       OntClass oc = this.ontModel.createClass(c_s);

       query = this.nsPrefix + "SELECT ?p ?o " + "WHERE { " + "   <" + c_s + "> ?p ?o . " + "}";

       ArrayList results1 = SparqlExecution.jQuery(this.model, query, new String[] { "p", "o" });

       for (int j = 0; j < results1.size(); j++) {
         Object p = ((Map)results1.get(j)).get("p");
         Object o = ((Map)results1.get(j)).get("o");

         if (p.toString().endsWith("label"))
           oc.addLabel(this.ontModel.createLiteral(o.toString()));
         else if (p.toString().endsWith("comment")) {
           oc.addComment(this.ontModel.createLiteral(o.toString()));
        }

         if (p.toString().endsWith("subClassOf")) {
           com.hp.hpl.jena.rdf.model.Resource super_class = this.ontModel.createResource(o.toString());

           oc.addSuperClass(super_class);
        }

         if (p.toString().endsWith("equivalentClass")) {
           com.hp.hpl.jena.rdf.model.Resource equ_class = this.ontModel.createResource(o.toString());
           oc.addEquivalentClass(equ_class);
        }
      }
    }
  }
}
