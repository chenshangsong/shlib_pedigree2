 package cn.sh.library.pedigree.sparql.impl;
 
  import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.sparql.ApiWorkSparql;
 
 @Repository
 @GraphDefine(name="http://gen.library.sh.cn/graph/place")
 public class PlaceSparqlImpl extends BaseDaoImpl
   implements PlaceSparql
 {
 
   @javax.annotation.Resource
   private StringBuffer nsPrefix;
 
   @javax.annotation.Resource
   private BaseinfoSparql baseinfoSparql;
   
   @javax.annotation.Resource
   private ApiWorkSparql apiWorkSparql;
   
   @Override
   public List<Map<String, String>> getRemoteAllPlaces(){
	   String query = this.nsPrefix  
			   + " SELECT DISTINCT ?uri ?prov ?city ?county ?label "
			   + " WHERE{ ?uri a shl:Place ;  shl:province ?prov ; bf:label ?label. " 
			   + " OPTIONAL{?uri shl:city ?city .} "
			   + " OPTIONAL{?uri shl:county ?county .} "
			   + " .filter(lang(?label) = 'cht') "
			   + " } "
			   + " ORDER BY ASC(?prov) ASC(?city) ASC(?county) ";
	   ArrayList result = SparqlExecution.vQuery(CodeMsgUtil.getConfig("remoteEndpoint"), query, "uri", "prov", "city", "county", "label");
	   return RDFUtils.transformListMap(result);
   }
 
   public ArrayList getFullLocations(String province, String label)
   {
/*  35 */     String query = this.nsPrefix + "SELECT DISTINCT ((if (?cou = ?city, ?c, ?uri)) AS ?s) ?label " + "((if (?cou = ?city, 'null', ?cou)) AS ?county) ?city ?province " + "FROM <" + "http://www.cba.ac.cn/graph/geography" + "> " + "WHERE {" + "   ?uri rdfs:label ?label ;" + "      rdfs:label ?cou ; " + "      gn:parentADM2/rdfs:label ?city ; " + "      gn:parentADM2 ?c ." + "   ?c gn:parentADM1/rdfs:label ?province . ";
 
/*  46 */     if (StringUtils.isNotBlank(province)) {
/*  47 */       if (StringUtils.isNotBlank(label)) {
/*  48 */         query = query + "FILTER (REGEX(?label, '" + label + "') && REGEX(?province, '" + province + "'))" + "}";
       }
       else {
/*  51 */         query = this.nsPrefix + "SELECT DISTINCT ?s (?province AS ?label) ?county ?city ?province " + "FROM <" + "http://www.cba.ac.cn/graph/geography" + "> " + "WHERE {" + "   ?s rdfs:label ?province . " + "FILTER REGEX(?province, '" + province + "') " + "} ORDER BY ASC(?s) LIMIT 1";
       }
 
     }
     else
     {
/*  60 */       query = this.nsPrefix + "SELECT ?s ?label " + "FROM <" + "http://www.cba.ac.cn/graph/geography" + "> " + "WHERE {" + "   ?s rdfs:label ?label . " + "FILTER REGEX(?label, '" + label + "')" + "}";
     }
 
/*  69 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "s", "label", "county", "city", "province" });
   }
 
   public ArrayList getPlacesInArea(String points)
   {
/*  77 */     String query = this.nsPrefix + "SELECT DISTINCT ?place ?label ?long ?lat " + "WHERE {" + "   ?place owl:sameAs ?as ; " + "          bf:label ?label . " + "FILTER (lang(?label)='cht')" + "{" + "   SELECT ?as ?long ?lat FROM <" + "http://www.cba.ac.cn/graph/geography" + "> " + "   WHERE {" + "       ?as geo:geometry ?geo ; " + "           geo:long ?long ; " + "           geo:lat ?lat . " + "   FILTER (bif:st_within(bif:st_point(?long, ?lat), bif:st_geomfromtext('" + points + "'))) " + "   }" + "}" + "{" + "   SELECT ?place FROM <" + "http://gen.library.sh.cn/graph/work" + "> " + "   WHERE {" + "       ?work shl:place ?place . " +  "}" + "}" + "}";
 
/*  95 */     return SparqlExecution.vQuery(this.graph, query, new String[] { "place", "label", "long", "lat" });
   }

   public ArrayList getPlacesInArea(String points, String familyName)
   {
     Map xing = this.baseinfoSparql.getXings(familyName);
 
     if (xing.size() > 0)
     {
       String uri = xing.get("uri").toString();
 
       String query = this.nsPrefix + "SELECT DISTINCT ?place ?label ?long ?lat " + "WHERE {" + "   ?place owl:sameAs ?as ; " + "          bf:label ?label . " + "FILTER (lang(?label)='cht')" + "{" + "   SELECT ?as ?long ?lat FROM <" + "http://www.cba.ac.cn/graph/geography" + "> " + "   WHERE {" + "       ?as geo:geometry ?geo ; " + "           geo:long ?long ; " + "           geo:lat ?lat . " + "   FILTER (bif:st_within(bif:st_point(?long, ?lat), bif:st_geomfromtext('" + points + "'))) " + "   }" + "}" + "{" + "   SELECT ?place FROM <" + "http://gen.library.sh.cn/graph/work" + "> " + "   WHERE {" + "       ?work shl:place ?place ; " + "             bf:subject <" + uri + "> . " + "   }" + "}" + "}";
 
       return SparqlExecution.vQuery(this.graph, query, new String[] { "place", "label", "long", "lat" });
     }
 
     return null;
   }

	@Override
	public ArrayList getPlacesInArea(String points, String familyName, String freetext, String startYear,
			String endYear) {
		Map xing = this.baseinfoSparql.getXings(familyName);
		String clause = "";

		if (!StringUtilC.isEmpty(freetext)) {
			clause += "{   ?work a bf:Work ; dc:title ?dtitle" + ". ?dtitle bif:contains '\"" + freetext + "\"'"
					+ "} UNION {" + "   ?work bf:subject ?th . " + "   ?th bf:label ?slabel . ?slabel bif:contains '\""
					+ freetext + "\"'" + "   }" + " UNION {"
					+ "	   ?work a bf:Work . ?uri shl:relatedWork ?work ; bf:label ?clabel .?clabel  bif:contains '\""
					+ freetext + "\"'" + "" + "} ";
		}

		if (StringUtils.isNotBlank(startYear) || StringUtils.isNotBlank(endYear)) {
			clause += " { ?inst bf:instanceOf ?work; shl:temporalValue ?year. FILTER (isNumeric(?year)) ";
			if (StringUtils.isNotBlank(startYear)) {
				clause += ".FILTER (?year >= " + startYear + ")  ";
			}
			if (StringUtils.isNotBlank(endYear)) {
				clause += ".FILTER (?year <= " + endYear + ")  ";
			}
			clause += "}";
		}

		if (xing!=null && xing.size() > 0) {
			String uri = xing.get("uri").toString();
			clause += "{?work bf:subject <" + uri+ ">}";
		}

		String query = this.nsPrefix + "SELECT DISTINCT ?place ?label ?long ?lat (count(distinct ?work) as ?cnt)"
				+ " FROM <http://gen.library.sh.cn/graph/person>" 
				+ " FROM <http://gen.library.sh.cn/graph/baseinfo>"
				+ " FROM <http://www.cba.ac.cn/graph/geography>" 
				+ " FROM <http://gen.library.sh.cn/graph/work>"
				+ " FROM <http://gen.library.sh.cn/graph/instance>" 
				+ "WHERE {" 
				+ "   ?place owl:sameAs ?as ; "
				+ "   bf:label ?label . " + "FILTER (lang(?label)='cht')" 
				+ "  {"
				+ "   SELECT ?as ?long ?lat "
				+ "	   WHERE {"
				+ "       ?as geo:geometry ?geo ; " 
				+ "           geo:long ?long ; " 
				+ "           geo:lat ?lat . "
				+ "   FILTER (bif:st_within(bif:st_point(?long, ?lat), bif:st_geomfromtext('" + points + "'))) "
				+ "   }" 
				+ "}" 
				+ "{" + "   SELECT ?place ?work "
				+ "   WHERE {" + "       ?work shl:place ?place . "
				+ clause
				+ "   }" 
				+ "}" 
				+ "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "place", "label", "long", "lat", "cnt" });
	}

	@Override
	public ArrayList getPlacesInAreaCount(String points, String familyName, String freetext, String startYear,
			String endYear) {
		Map xing = this.baseinfoSparql.getXings(familyName);
		String clause = "";

		if (!StringUtilC.isEmpty(freetext)) {
			clause += "{   ?work a bf:Work ; dc:title ?dtitle" + ". ?dtitle bif:contains '\"" + freetext + "\"'"
					+ "} UNION {" + "   ?work bf:subject ?th . " + "   ?th bf:label ?slabel . ?slabel bif:contains '\""
					+ freetext + "\"'" + "   }" + " UNION {"
					+ "	   ?work a bf:Work . ?uri shl:relatedWork ?work ; bf:label ?clabel .?clabel  bif:contains '\""
					+ freetext + "\"'" + "" + "} ";
		}

		if (StringUtils.isNotBlank(startYear) || StringUtils.isNotBlank(endYear)) {
			clause += " { ?inst bf:instanceOf ?work; shl:temporalValue ?year. FILTER (isNumeric(?year)) ";
			if (StringUtils.isNotBlank(startYear)) {
				clause += ".FILTER (?year >= " + startYear + ")  ";
			}
			if (StringUtils.isNotBlank(endYear)) {
				clause += ".FILTER (?year <= " + endYear + ")  ";
			}
			clause += "}";
		}

		if (xing!=null && xing.size() > 0) {
			String uri = xing.get("uri").toString();
			clause += "{?work bf:subject <" + uri+ ">}";
		}

		String query = this.nsPrefix + "SELECT  (count(distinct ?work) as ?cnt)"
				+ " FROM <http://gen.library.sh.cn/graph/person>" 
				+ " FROM <http://gen.library.sh.cn/graph/baseinfo>"
				+ " FROM <http://www.cba.ac.cn/graph/geography>" 
				+ " FROM <http://gen.library.sh.cn/graph/work>"
				+ " FROM <http://gen.library.sh.cn/graph/instance>" 
				+ "WHERE {" 
				+ "   ?place owl:sameAs ?as ; "
				+ "   bf:label ?label . " + "FILTER (lang(?label)='cht')" 
				+ "  {"
				+ "   SELECT ?as ?long ?lat "
				+ "	   WHERE {"
				+ "       ?as geo:geometry ?geo ; " 
				+ "           geo:long ?long ; " 
				+ "           geo:lat ?lat . "
				+ "   FILTER (bif:st_within(bif:st_point(?long, ?lat), bif:st_geomfromtext('" + points + "'))) "
				+ "   }" 
				+ "}" 
				+ "{" + "   SELECT ?place ?work "
				+ "   WHERE {" + "       ?work shl:place ?place . "
				+ clause
				+ "   }" 
				+ "}" 
				+ "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "cnt" });
	}
	
	

   public ArrayList getAllPlaces(String keyWord)
   {
	   String sqlwhere="";
	   if(!StringUtilC.isEmpty(keyWord)){
		   sqlwhere+=".{?uri bf:label ?label .FILTER (CONTAINS(?label, '"+ keyWord + "'))}";
	   }
     String query = this.nsPrefix + "SELECT DISTINCT ?uri ?prov ?city ?county " + "WHERE { " + "   ?uri a shl:Place ; " + "        shl:province ?prov . " + "OPTIONAL {" + "   ?uri shl:city ?city . " + "}" + "OPTIONAL {" + "   ?uri shl:county ?county . " + "}" + sqlwhere+"} ORDER BY ASC(?prov) ASC(?city) ASC(?county)";
 
     return SparqlExecution.jQuery(this.model, query, new String[] { "uri", "prov", "city", "county" });
   }
   
   public ArrayList getAllPlaces()
   {
     String query = this.nsPrefix + "SELECT DISTINCT ?uri ?prov ?city ?county " + "WHERE { " + "   ?uri a shl:Place ; " + "        shl:province ?prov . " + "OPTIONAL {" + "   ?uri shl:city ?city . " + "}" + "OPTIONAL {" + "   ?uri shl:county ?county . " + "}" + "} ORDER BY ASC(?prov) ASC(?city) ASC(?county)";
 
     return SparqlExecution.jQuery(this.model, query, new String[] { "uri", "prov", "city", "county" });
   }
   public ArrayList getAllPlacesInOrigin()
   {
     String query = this.nsPrefix + "SELECT DISTINCT ?prov ?uri ?label " + "WHERE { " + "   ?uri bf:label ?label . " + "OPTIONAL {" + "   ?uri shl:province ?prov . " + "}" + "FILTER (lang(?label) = 'chs')" + "} ORDER BY ASC(?prov)";
 
     return SparqlExecution.jQuery(this.model, query, new String[] { "prov", "uri", "label" });
   }
 
   public ArrayList getRDF(String place_uri)
   {
     String query = this.nsPrefix + "SELECT ?s ?p ?o " + "WHERE {" + "   ?s ?p ?o ." + "FILTER (STR(?s) = '" + place_uri + "')" + "}";
 
     return SparqlExecution.vQuery(this.graph, query, new String[] { "s", "p", "o" });
   }
 
   public ArrayList getStandPlace(String place_uri)
   {
     String query = this.nsPrefix + "SELECT ?country ?prov ?city ?county ?label " + "WHERE {" + "   <" + place_uri + "> bf:label ?label ; " + "       shl:country ?country ." + "FILTER (lang(?country)='cht') " + "OPTIONAL {" + "   <" + place_uri + "> shl:province ?prov . " + "}" + "OPTIONAL {" + "   <" + place_uri + "> shl:city ?city . " + "}" + "OPTIONAL {" + "   <" + place_uri + "> shl:county ?county . " + "}" + "FILTER (lang(?label) = 'cht')" + "}";
 
     return SparqlExecution.vQuery(this.graph, query, new String[] { "country", "prov", "city", "county", "label" });
   }
 
   public ArrayList getPlaces(String work_uri)
   {
     String query = this.nsPrefix + "SELECT ?place ?label (CONCAT(?country,?prov,?city,?county) AS ?tag) " + "WHERE {" + "   ?place a shl:Place; bf:label ?label ." + "FILTER (lang(?label)='chs') " + "OPTIONAL { " + "   ?place shl:country ?country ." + "FILTER (lang(?country)='chs') " + "}" + "OPTIONAL { " + "   ?place shl:province ?prov . " + "}" + "OPTIONAL { " + "   ?place shl:city ?city . " + "}" + "OPTIONAL { " + "   ?place shl:county ?county . " + "}" + "{" + "   SELECT ?place FROM <" + "http://gen.library.sh.cn/graph/work" + "> WHERE {" + "       <" + work_uri + "> shl:place ?place . " + "   }" + "}" + "}";
 
     ArrayList results = SparqlExecution.vQuery(this.model, query, new String[] { "place", "label", "tag" });
 
     List<Map<String,String>> places = RDFUtils.transformListMap(results);
     /*
     modify by ChenSS and discuss with CM 20180911
     Map resultMap = new LinkedHashMap(); 
     Map map = new HashMap();
 
     if (CollectionUtils.isNotEmpty(results)) {
       for (Map<String,String> place : places) {
         String tag = (String)place.get("tag");
         String uri = (String)place.get("place");
         boolean exists = false;
         for (Object key : map.keySet())
         {
           if (String.valueOf(key).startsWith(tag)) {
             exists = true;
             break;
           }
 
           if (tag.contains(String.valueOf(key))) {
             map.remove(key);
             resultMap.remove(map.get(key));
           }
         }
         if (!exists) {
           map.put(tag, uri);
           resultMap.put(uri, place);
         }
       }
     }
     ArrayList resultList = new ArrayList();
     resultList.addAll(resultMap.values());
     return resultList;
     */
     ArrayList resultList = new ArrayList(places);
     return resultList;
   }
 
   public String getLabel(String place_uri)
   {
     String query = this.nsPrefix + "SELECT ?label " + "WHERE {" + "   <" + place_uri + "> bf:label ?label ." + "}";
 
     return ((Map)SparqlExecution.vQuery(this.graph, query, new String[] { "label" }).get(0)).get("label").toString();
   }
 
   public String getLongLat(String place_uri)
   {
     ArrayList results = getStandPlace(place_uri);
 
     String query = this.nsPrefix + "";
 
     if (results.size() > 0) {
       Object prov = ((Map)results.get(0)).get("prov");
       Object city = ((Map)results.get(0)).get("city");
       Object county = ((Map)results.get(0)).get("county");
       Object country = ((Map)results.get(0)).get("country");
       Object label = ((Map)results.get(0)).get("label");
 
       if (country.toString().contains("中國")) {
         if ((county != null) || (city != null))
         {
           if ((county != null) || (city != null)) {
             String place = "";
 
             if (county != null)
               place = county.toString();
             else {
               place = city.toString();
             }
 
             query = this.nsPrefix + "SELECT ?long ?lat FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?s rdfs:label '" + place + "' ; " + "      gn:parentADM2 ?a ; " + "      geo:long ?long ; " + "      geo:lat ?lat . " + "}";
           }
 
         }
         else if (prov != null)
         {
           query = this.nsPrefix + "SELECT ?long ?lat FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?s rdfs:label '" + prov
             .toString() + "' ;" + "      owl:seeAlso/geo:long ?long ;" + "      owl:seeAlso/geo:lat ?lat . " + "}";
         }
 
       }
       else
       {
         query = "SELECT ?long ?lat WHERE {   <" + place_uri + "> owl:sameAs ?same . " + "   {SELECT ?same ?long ?lat FROM <" + "http://www.cba.ac.cn/graph/geography" + "> WHERE {" + "       ?same geo:long ?long ; " + "             geo:lat ?lat . " + "   }}" + "}";
       }
 
       results = SparqlExecution.vQuery(this.model, query, new String[] { "long", "lat" });
 
       if (results.size() > 0)
         return "(" + RDFUtils.getValue(((Map)results.get(0)).get("long").toString()) + "," + RDFUtils.getValue(((Map)results.get(0)).get("lat").toString()) + ")";
     }
     else {
       return "";
     }
 
     return "";
   }
 
   public String getUri4StandPlace(String prov, String label)
   {
     String query = this.nsPrefix + "SELECT ?uri " + "WHERE {" + "   ?uri bf:label ?label ; " + "        shl:province ?prov . " + "FILTER STRSTARTS('" + label + "', ?label) " + "FILTER (?prov = '" + prov + "')" + "}";
 
     return ((Map)SparqlExecution.vQuery(this.graph, query, new String[] { "uri" }).get(0)).get("uri").toString();
   }
 
   public void linkPlace2Geo()
   {
     String query = this.nsPrefix + "SELECT ?uri ?label ?prov ?city ?county ?town " + "WHERE {" + "   ?uri shl:province ?prov ; " + "        bf:label ?label . " + "OPTIONAL {" + "   ?uri shl:city ?city . " + "} " + "OPTIONAL {" + "   ?uri shl:county ?county . " + "} " + "OPTIONAL {" + "   ?uri shl:town ?town . " + "} " + "FILTER NOT EXISTS {" + "   ?uri owl:sameAs ?as . " + "}" + "}";
 
     ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "label", "prov", "city", "county", "town" });
 
     for (int i = 0; i < results.size(); i++) {
       String uri = ((Map)results.get(i)).get("uri").toString();
 
       Object prov = ((Map)results.get(i)).get("prov");
       Object city = ((Map)results.get(i)).get("city");
       Object county = ((Map)results.get(i)).get("county");
       Object town = ((Map)results.get(i)).get("town");
 
       if ((town != null) || (county != null) || (city != null)) {
         if (town != null)
         {
           query = this.nsPrefix + "SELECT ?s FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?s rdfs:label '" + town
             .toString() + "' ; " + "      gn:parentADM3 ?a . " + "}";
         }
         else if ((county != null) || (city != null)) {
           String place = "";
 
           if (county != null)
             place = county.toString();
           else {
             place = city.toString();
           }
 
           query = this.nsPrefix + "SELECT ?s FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?s rdfs:label '" + place + "' ; " + "      gn:parentADM2 ?a . " + "}";
         }
 
       }
       else if (prov != null)
       {
         query = this.nsPrefix + "SELECT ?s FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?a rdfs:label '" + prov
           .toString() + "' ;" + "      owl:seeAlso ?s . " + "}";
       }
 
       ArrayList r = SparqlExecution.vQuery(this.model, query, new String[] { "s" });
 
       if (r.size() > 0) {
         String geo_s = ((Map)r.get(0)).get("s").toString();
 
         String g = "http://gen.library.sh.cn/graph/place";
         String sql = "INSERT DATA { GRAPH <" + g + "> { " + "   <" + uri + "> owl:sameAs <" + geo_s + "> ." + "}" + "} ";
 
         SparqlExecution.update(this.graph, sql);
       }
     }
   }
 
   public QueryResult<Map<String, Object>> getPlaces(String q, int start, int size)
   {
     String filter = "";
 
     if (StringUtils.isNotBlank(q)) {
       filter = "FILTER CONTAINS(?label, '" + q + "')";
     }
 
     String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?uri) AS ?count) " + "WHERE {" + "   ?uri a shl:Place ;" + "        bf:label ?label . " + filter + "}";
 
     String query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?province ?country " + "WHERE {" + "   ?uri a shl:Place ; " + "        bf:label ?chs ;" + "        bf:label ?label ; " + "        shl:country ?country . " + "OPTIONAL {?uri shl:province ?province .}" + "FILTER (lang(?chs) = 'chs') " + "FILTER (lang(?country) = 'chs') " + filter + "} " + "OFFSET " + start + " LIMIT " + size;
 
     Map countMap = (Map)SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
     Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));
 
     QueryResult result = new QueryResult();
     result.setTotalrecord(count);
     if (count.longValue() > 0L) {
       result.setResultList(SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs", "province", "country" }));
     }
     return result;
   }
 
   public OutputStream getTriples(String uri, String format)
   {
     Model temp_m = ModelFactory.createDefaultModel();
     String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o} " + "WHERE {" + "   ?s ?p ?o . " + "   FILTER (?s = <" + uri + ">)" + "   FILTER (?p != owl:sameAs)" + "}";
 
     temp_m.add(SparqlExecution.construct(this.graph, query));
 
     String loc = getLongLat(uri);
 
     if (StringUtils.isNotBlank(loc)) {
       String _long = loc.split(",")[0].split("\\(")[1];
       String _lat = loc.split(",")[1].split("\\)")[0];
       com.hp.hpl.jena.rdf.model.Resource s = temp_m.createResource(uri);
       s.addProperty(temp_m.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#long"), _long);
       s.addProperty(temp_m.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lat"), _lat);
     }
 
     ByteArrayOutputStream stream = new ByteArrayOutputStream();
     temp_m.write(stream, format);
 
     return stream;
   }
 
   public String getSameAs(Map pm)
   {
     String query = "";
 
     Object prov = null;
     Object city = null;
     Object county = null;
     Object town = null;
 
     if (pm.containsKey("province")) {
       prov = pm.get("province");
     }
     if (pm.containsKey("city")) {
       city = pm.get("city");
     }
     if (pm.containsKey("county")) {
       county = pm.get("county");
     }
     if (pm.containsKey("town")) {
       town = pm.get("town");
     }
 
     if ((town != null) || (county != null) || (city != null)) {
       if (town != null)
       {
         query = this.nsPrefix + "SELECT ?s FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?s rdfs:label '" + town
           .toString() + "' ; " + "      gn:parentADM3 ?a . " + "}";
       }
       else if ((county != null) || (city != null)) {
         String place = "";
 
         if (county != null)
           place = county.toString();
         else {
           place = city.toString();
         }
 
         query = this.nsPrefix + "SELECT ?s FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?s rdfs:label '" + place + "' ; " + "      gn:parentADM2 ?a . " + "}";
       }
 
     }
     else if (prov != null)
     {
       query = this.nsPrefix + "SELECT ?s FROM <" + "http://www.cba.ac.cn/graph/geography" + ">" + "WHERE {" + "   ?a rdfs:label '" + prov
         .toString() + "' ;" + "      owl:seeAlso ?s . " + "}";
     }
 
     ArrayList r = SparqlExecution.vQuery(this.model, query, new String[] { "s" });
 
     if (r.size() > 0) {
       return ((Map)r.get(0)).get("s").toString();
     }
 
     return null;
   }
 
   private static Map getMaxMin(String points) {
     Map map = new HashMap();
 
     String value = points.split("\\(\\(")[1].split("\\)\\)")[0];
     String[] values = value.split(",");
 
     double long_max = Double.parseDouble(values[0].split(" ")[0]);
     double lat_max = Double.parseDouble(values[0].split(" ")[1]);
     double long_min = Double.parseDouble(values[0].split(" ")[0]);
     double lat_min = Double.parseDouble(values[0].split(" ")[1]);
 
     for (int i = 1; i < values.length; i++) {
       double long_v = Double.parseDouble(values[i].split(" ")[0]);
       double lat_v = Double.parseDouble(values[i].split(" ")[1]);
 
       if (long_v > long_max) {
         long_max = long_v;
       }
 
       if (long_v < long_min) {
         long_min = long_v;
       }
 
       if (lat_v > lat_max) {
         lat_max = lat_v;
       }
 
       if (lat_v < lat_min) {
         lat_min = lat_v;
       }
     }
 
     map.put("long_max", Double.valueOf(long_max));
     map.put("long_min", Double.valueOf(long_min));
     map.put("lat_max", Double.valueOf(lat_max));
     map.put("lat_min", Double.valueOf(lat_min));
 
     return map;
   }

@Override
public Map getPlacesInAreaByCircle(String points,Integer distance, String fname) {
	 String xingFilter ="";
	 String furi ="";
	 Map _resultMap = new HashMap();
		if(!StringUtil.isEmpty(fname)) {
		        xingFilter ="{?work bf:subject ?furi .{?furi a shl:FamilyName; bf:label '"+fname+"'@chs }}";
		}
   
	String placeSql = "select distinct  ?place ?label ?long ?lat  \r\n"
			+ "FROM <http://www.cba.ac.cn/graph/geography>\r\n"
			+ "FROM <http://gen.library.sh.cn/graph/work>\r\n"
			+ "FROM <http://gen.library.sh.cn/graph/place>\r\n"
			+ "FROM <http://gen.library.sh.cn/graph/baseinfo>\r\n"
			+ "where {?work a bf:Work "+xingFilter+"\r\n"
			+ "{?work shl:place ?place .{?place a shl:Place;bf:label ?label;owl:sameAs ?as .{?as geo:geometry ?geo ; geo:long ?long ; geo:lat ?lat . FILTER (bif:st_within(bif:st_point(?long, ?lat),  bif:st_point ("+points+"), "+distance+"))}\r\n"
			+ ".FILTER (lang(?label)='cht') \r\n"
			+ "}}\r\n"
			+ "\r\n"
			+ "}\r\n";
     
     
     List<Map<String,Object>> _placeList = RDFUtils.transformListMap(SparqlExecution.vQuery(this.graph, placeSql, new String[] { "place", "label", "long", "lat" }));
     
     
     String workCountSql = "select count(distinct ?work) as ?count  \r\n"
 			+ "FROM <http://www.cba.ac.cn/graph/geography>\r\n"
 			+ "FROM <http://gen.library.sh.cn/graph/work>\r\n"
 			+ "FROM <http://gen.library.sh.cn/graph/place>\r\n"
 			+ "FROM <http://gen.library.sh.cn/graph/baseinfo>\r\n"
 			+ "where {?work a bf:Work "+xingFilter+"\r\n"
 			+ "{?work shl:place ?place .{?place a shl:Place;bf:label ?label;owl:sameAs ?as .{?as geo:geometry ?geo ; geo:long ?long ; geo:lat ?lat . FILTER (bif:st_within(bif:st_point(?long, ?lat),  bif:st_point ("+points+"), "+distance+"))}\r\n"
 			+ ".FILTER (lang(?label)='cht') \r\n"
 			+ "}}\r\n"
 			+ "\r\n"
 			+ "}\r\n";
     
     List<Map<String,Object>> workCount = RDFUtils.transformListMap(SparqlExecution.vQuery(this.graph, workCountSql, new String[] { "count" }));
     
     if(_placeList!=null && _placeList.size()>0) {
    	 //地点列表
    	 _resultMap.put("placeList", _placeList);
    	
    	//作品数量
    	_resultMap.put("workCount", workCount.get(0).get("count"));
    	//地点数量
    	_resultMap.put("placeCount", _placeList.size());
     }
     else {
    	 _resultMap.put("placeList", null);
    	//作品数量
     	_resultMap.put("workCount", 0);
     	//地点数量
     	_resultMap.put("placeCount", 0);
     }
    return _resultMap;
}
 }