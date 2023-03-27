package cn.sh.library.pedigree.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.utils.RDFUtils;



@GraphDefine(name="http://gen.library.sh.cn/graph/baseinfo")
@Repository
public class CommonSparqlImpl extends BaseDaoImpl
  implements CommonSparql
{

  @javax.annotation.Resource
  private StringBuffer nsPrefix;

  @javax.annotation.Resource
  private VocabSparql vs;

  public boolean changeRDF(String g, String s, String p, String old_o, String o)
  {
     p = RDFUtils.getLink(this.model, p);

     String query = "";

     if (StringUtils.isNotBlank(o)) {
       if (StringUtils.isNotBlank(old_o)) {
         if ((old_o.startsWith("<")) && (old_o.endsWith(">"))) {
           old_o = old_o.substring(1, old_o.length() - 1);
           old_o = "'" + old_o + "'";
        }

         query = this.nsPrefix + "WITH <" + g + "> " + "DELETE { <" + s + "> <" + p + "> ?o . } " + "INSERT { <" + s + "> <" + p + "> " + o + " . } " + "WHERE { <" + s + "> <" + p + "> ?o . FILTER REGEX(STR(?o), " + old_o + ")" + "}";
      }
      else
      {
         query = this.nsPrefix + "INSERT DATA {" + "GRAPH <" + g + "> {" + "   <" + s + "> <" + p + "> " + o + " ." + "}" + "}";
      }

    }
     else if (old_o.contains("\"@"))
    {
       query = this.nsPrefix + "WITH <" + g + "> " + "DELETE { " + "   <" + s + "> <" + p + "> ?o . " + "} WHERE {" + "   <" + s + "> <" + p + "> ?o . " + "FILTER regex(?o, " + old_o
         .split("@")[
         0] + ") " + "FILTER (lang(?o) = '" + old_o
         .split("@")[
         1] + "')" + "}";
    }
     else if (old_o.contains("\"^^"))
    {
       query = this.nsPrefix + "WITH <" + g + "> " + "DELETE DATA { " + "   <" + s + "> <" + p + "> " + old_o
         .split("^^")[
         0] + " . " + "}";
    }
    else {
       query = this.nsPrefix + "WITH <" + g + "> " + "DELETE DATA { " + "   <" + s + "> <" + p + "> " + old_o + " . " + "}";
    }

     SparqlExecution.update(this.graph, query);

     return true;
  }

  public OutputStream getJsonLD4Resource(String g, String s)
  {
     Model m = ModelFactory.createDefaultModel();

     String query = this.nsPrefix + "SELECT ?c " + "WHERE {" + "   <" + s + "> a ?c . " + "}";

     ArrayList results = SparqlExecution.vQuery(getModel(g), query, new String[] { "c" });
     com.hp.hpl.jena.rdf.model.Resource res = m.createResource(s);

     if (results.size() > 0) {
       String c = ((Map)results.get(0)).get("c").toString();

       ArrayList props = this.vs.getSubjectProperties(c);
       for (int i = 0; i < props.size(); i++) {
         String p = ((Map)props.get(i)).get("p").toString();
         query = this.nsPrefix + "SELECT ?v (isURI(?v) AS ?t) " + "WHERE {" + "   <" + s + "> " + p + " ?v ." + "}";

         ArrayList values = SparqlExecution.vQuery(getModel(g), query, new String[] { "v", "t" });
         if (values.size() > 0) {
           for (int j = 0; j < values.size(); j++)
             if (RDFUtils.getValue(((Map)values.get(j)).get("t").toString()).equals("1"))
               res.addProperty(m.createProperty(RDFUtils.getLink(this.model, p)), m.createResource(((Map)values.get(j)).get("v").toString()));
            else
               res.addProperty(m.createProperty(RDFUtils.getLink(this.model, p)), ((Map)values.get(j)).get("v").toString());
        }
        else
        {
           String pt = this.vs.getPropertyType(p);
           String pu = RDFUtils.getLink(this.model, p);

           if (StringUtils.isNotBlank(pt)) {
             if (pt.equals("DP")) {
               res.addProperty(m.createProperty(pu), "NoN");
            }

             if (pt.equals("OP")) {
               res.addProperty(m.createProperty(pu), m.createResource());
            }
          }
        }
      }
    }

     ByteArrayOutputStream stream = new ByteArrayOutputStream();

     if (g.equals("http://gen.library.sh.cn/graph/work")) {
       Model temp_m = ModelFactory.createDefaultModel();

       String str = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "WHERE {" + "   ?s ?p ?o . " + "   FILTER (?p != dc:title)" + "}";

       temp_m.add(SparqlExecution.construct(m, str));

       str = this.nsPrefix + "CONSTRUCT {?s dc:titleFullName ?o} " + "WHERE {" + "   ?s dc:title ?o ." + "}";

       temp_m.add(SparqlExecution.construct(m, str));

       temp_m.write(stream, "JSON-LD");
     } else if (g.equals("http://gen.library.sh.cn/graph/baseinfo")) {
       Model temp_m = ModelFactory.createDefaultModel();

       String str = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "WHERE {" + "   ?s ?p ?o . " + "   FILTER (?p != schema:address)" + "}";

       temp_m.add(SparqlExecution.construct(m, str));

       temp_m.write(stream, "JSON-LD");
    } else {
       m.write(stream, "JSON-LD");
    }

     return stream;
  }

  public String getResourceClass(String g, String s)
  {
     String _class = "";

     String query = this.nsPrefix + "SELECT ?c " + "WHERE {" + "   <" + s + "> a ?c . " + "}";

     ArrayList results = SparqlExecution.vQuery(getModel(g), query, new String[] { "c" });

     if (results.size() > 0) {
       for (int i = 0; i < results.size(); i++) {
         _class = _class + ((Map)results.get(i)).get("c").toString();

         if (i < results.size() - 1) {
           _class = _class + ";";
        }
      }
    }

     return _class;
  }

  public boolean deleteResource(String g, String s)
  {
     String sql = this.nsPrefix + "DELETE {<" + s + "> ?p ?o .} " + "WHERE {<" + s + "> ?p ?o .}";

              SparqlExecution.update(getModel(g), sql);

     return true;
  }

  public QueryResult<Map<String, Object>> getResources(String g, String c, int start, int size)
  {
     c = RDFUtils.getLink(this.model, c);

     String query = this.nsPrefix + "SELECT DISTINCT ?uri " + "WHERE { " + "   ?uri a <" + c + "> . " + "}" + "OFFSET " + start + " LIMIT " + size;

     String countQuery = this.nsPrefix + "SELECT count(DISTINCT ?uri) as ?count " + "WHERE {" + "   ?uri a <" + c + "> . " + "}";

     Map countMap = (Map)SparqlExecution.vQuery(getModel(g), countQuery, new String[] { "count" }).get(0);
     Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

     QueryResult result = new QueryResult();
     result.setTotalrecord(count);
     if (count.longValue() > 0L) {
       result.setResultList(SparqlExecution.vQuery(getModel(g), query, new String[] { "uri" }));
    }
     return result;
  }

  public ArrayList getResInfos(String g, String s)
  {
     s = RDFUtils.getLink(this.model, s);

     String query = this.nsPrefix + "SELECT ?p ?o " + "WHERE {" + "   <" + s + "> ?p ?o ." + "}";

     return SparqlExecution.jQuery(getModel(g), query, new String[] { "p", "o" });
  }

  public ArrayList getPropertyValue(String g, String s, String p)
  {
     s = RDFUtils.getLink(this.model, s);
     p = RDFUtils.getLink(this.model, p);

     String query = this.nsPrefix + "SELECT (sql:BEST_LANGMATCH(?l, 'chs;q=0.8, cht;q=0.7, ;q=0.6, en;q=0.5, *;q=0.1', 'chs')) as ?o " + "WHERE {" + "   <" + s + "> <" + p + "> ?l . " + "}";

     return SparqlExecution.vQuery(getModel(g), query, new String[] { "o" });
  }

  public ArrayList getSpecPropValue(String g, String c, String spec, String p)
  {
     c = RDFUtils.getLink(this.model, c);
     p = RDFUtils.getLink(this.model, p);

     String query = this.nsPrefix + "SELECT ?s (sql:BEST_LANGMATCH(?l, 'chs;q=0.8, cht;q=0.7, ;q=0.6, en;q=0.5, *;q=0.1', 'chs')) as ?o " + "WHERE {" + "   ?s a <" + c + "> ; " + "      ?p '" + spec + "' ; " + "      <" + p + "> ?l . " + "}";

     return SparqlExecution.vQuery(getModel(g), query, new String[] { "s", "o" });
  }

  public boolean isExist(String graph, String s, String p, String o)
  {
     String clause = "";

     if (o.contains(";")) {
       String[] cells = o.split(";");
       for (int i = 0; i < cells.length; i++)
         clause = clause + s + " " + p + " " + cells[i] + ". ";
    }
    else {
       clause = s + " " + p + " " + o + ". ";
    }

     String query = this.nsPrefix + "SELECT * " + "FROM <" + graph + ">" + "WHERE {" + clause + "}";

     return SparqlExecution.ask(this.model, query);
  }

  public OutputStream getTriples(String graph_name, boolean transmit, String uri)
  {
     if (!transmit) {
       String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "FROM <" + graph_name + ">" + "WHERE {" + "   ?s ?p ?o . " + "FILTER (STR(?s)='" + uri + "')" + "}";

       Model temp_m = ModelFactory.createDefaultModel();
       temp_m.add(SparqlExecution.construct(this.graph, query));

       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       temp_m.write(stream, "RDF/XML-ABBREV");

       return stream;
    }
     Model temp = ModelFactory.createDefaultModel();
     return getTriples(temp, uri);
  }

  public OutputStream getTriples(String graph_name, String uri, String format)
  {
     String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "FROM <" + graph_name + ">" + "WHERE {" + "   ?s ?p ?o . " + "FILTER (STR(?s)='" + uri + "')" + "}";

     Model temp_m = ModelFactory.createDefaultModel();
     temp_m.add(SparqlExecution.construct(this.graph, query));

     ByteArrayOutputStream stream = new ByteArrayOutputStream();
     temp_m.write(stream, format);

     return stream;
  }

  public List<Statement> getTriples(String graph_name, String uri)
  {
     List triples = new LinkedList();

     String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "FROM <" + graph_name + ">" + "WHERE {" + "   ?s ?p ?o . " + "FILTER (STR(?s)='" + uri + "')" + "}";

     Model temp_m = ModelFactory.createDefaultModel();
     temp_m.add(SparqlExecution.construct(this.graph, query));

     return temp_m.listStatements().toList();
  }

  public boolean moveTriples(String graph_from, String graph_to, String category)
  {
     Model model_from = getModel(graph_from);
     Model model_to = getModel(graph_to);

     String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "FROM <" + graph_from + "> " + "WHERE {" + "   ?s a " + category + " ; ?p ?o ." + "}";

     Model temp_m = ModelFactory.createDefaultModel();
     temp_m.add(SparqlExecution.construct(model_from, query));

     if (temp_m.size() > 0L)
    {
       model_to.add(temp_m);

       model_from.remove(temp_m);

       if (model_from.containsAny(temp_m)) {
         return false;
      }
    }

     return true;
  }

  public boolean copyTriples(String graph_from, String graph_to, String source)
  {
     Model model_to = getModel(graph_to);

     String query = this.nsPrefix + "CONSTRUCT {<" + source + "> ?p ?o} " + "FROM <" + graph_from + "> " + "WHERE {" + "   <" + source + "> ?p ?o. " + "}";

     Model temp_m = ModelFactory.createDefaultModel();
     temp_m.add(SparqlExecution.construct(this.graph, query));

     if (temp_m.size() > 0L)
    {
       model_to.add(temp_m);

       return true;
    }

     return false;
  }

  public boolean copyStatements(String graph_from, String graph_to, String predicate, String old_source, String new_source)
  {
     Model model_to = getModel(graph_to);

     String query = this.nsPrefix + "CONSTRUCT {<" + new_source + "> <" + predicate + "> ?o} " + "FROM <" + graph_from + "> " + "WHERE {" + "   <" + old_source + "> <" + predicate + "> ?o. " + "}";

     Model temp_m = ModelFactory.createDefaultModel();
     temp_m.add(SparqlExecution.construct(this.graph, query));

     if (temp_m.size() > 0L)
    {
       model_to.add(temp_m);

       return true;
    }

     return false;
  }

  public boolean insertTriples(String str)
  {
     String sql = this.nsPrefix + "INSERT DATA { " + "GRAPH <" + this.graph
       .getGraphName() + "> { " + str + "}" + "} ";

     SparqlExecution.update(this.graph, sql);

     return true;
  }

  private OutputStream getTriples(Model temp, String uri) {
     String graph_name = "http://gen.library.sh.cn/graph/baseinfo";

     if (uri.contains("/person/"))
       graph_name = "http://gen.library.sh.cn/graph/person";
     else if (uri.contains("/instance/"))
       graph_name = "http://gen.library.sh.cn/graph/instance";
     else if ((uri.contains("/work/")) || (uri.contains("/title/")))
       graph_name = "http://gen.library.sh.cn/graph/work";
     else if (uri.contains("/place/"))
       graph_name = "http://gen.library.sh.cn/graph/place";
     else if (uri.contains("/annotation/"))
       graph_name = "http://gen.library.sh.cn/graph/annotation";
     else if (uri.contains("/item/"))
       graph_name = "http://gen.library.sh.cn/graph/item";
     else if (uri.contains("/temporal/"))
       graph_name = "http://gen.library.sh.cn/graph/temporal";
     else if (uri.contains("www.cba.ac.cn/point/"))
       graph_name = "http://www.cba.ac.cn/graph/geography";
    else {
       graph_name = "http://gen.library.sh.cn/graph/baseinfo";
    }

     String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "FROM <" + graph_name + ">" + "WHERE {" + "   ?s ?p ?o . " + "FILTER (STR(?s)='" + uri + "')" + "}";

     temp.add(SparqlExecution.construct(getModel(graph_name), query));

     query = this.nsPrefix + "SELECT DISTINCT ?o " + "WHERE { " + "   ?s ?p ?o ." + "FILTER (STR(?s)='" + uri + "')" + "FILTER isIRI(?o) " + "}";

     ArrayList results = SparqlExecution.jQuery(getModel(graph_name), query, new String[] { "o" });

     if (results.size() > 0) {
       for (int i = 0; i < results.size(); i++) {
         String u = ((Map)results.get(i)).get("o").toString();
         getTriples(temp, u);
      }
    }

     ByteArrayOutputStream stream = new ByteArrayOutputStream();
     temp.write(stream, "RDF/XML-ABBREV");

     return stream;
  }
}
