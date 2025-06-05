package cn.sh.library.pedigree.sparql.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.AppConfig;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.MergeSearchParts;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.webApi.sparql.Namespace;
import virtuoso.jena.driver.VirtGraph;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

@Repository
@GraphDefine(name = "http://gen.library.sh.cn/graph/work")
public class WorkSparqlImpl extends BaseDaoImpl implements WorkSparql {

	@Resource
	private StringBuffer nsPrefix;

	@Resource
	private PlaceSparql placeSparql;

	@Resource
	private AppConfig appConfig;

	@Resource
	private BaseinfoSparql baseinfoSparql;

	public QueryResult<Map<String, Object>> getWorksInFree(String free_text,
			int start, int size) {
		String sql = "WHERE { {   ?work a bf:Work ; "
				+ "dc:title ?dtitle .FILTER CONTAINS(STR(?dtitle), '"
				+ free_text
				+ "')"
				+ "} UNION {"
				+ "   ?work bf:subject ?th . "
				+ "   {SELECT ?th FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo"
				+ "> WHERE { "
				+ "       ?th bf:label ?slabel . FILTER CONTAINS(?slabel, '"
				+ free_text
				+ "') "
				+ "   }}"
				+ "} UNION {"
				+ "   ?work a bf:Work . "
				+ "   {SELECT ?work FROM <"
				+ "http://gen.library.sh.cn/graph/person"
				+ "> WHERE { "
				+ "       ?uri shl:relatedWork ?work ; bf:label ?clabel. "
				+ "       FILTER CONTAINS(?clabel, '"
				+ free_text
				+ "')"
				+ "   }}" + "}" + "}";

		String countQuery = Namespace.getNsPrefixString()
				+ "SELECT (COUNT(DISTINCT ?work) AS ?count) " + sql;

		/* 97 */String workQuery = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work "
				+ sql + "OFFSET " + start + " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery,
				new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap
				.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);

		List list = new ArrayList();
		if (count.longValue() > 0L) {
			ArrayList works = SparqlExecution.vQuery(this.graph, workQuery,
					new String[] { "work" });
			for (int i = 0; i < works.size(); i++) {
				String uri = ((Map) works.get(i)).get("work").toString();

				String query = Namespace.getNsPrefixString()
						+ "SELECT DISTINCT ?work ?dtitle ?title "
						+ "?doi ?desc "
						+ "(GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles) "
						+ "(GROUP_CONCAT(DISTINCT ?creator;separator=';') AS ?creators) ?location "
						+ "(GROUP_CONCAT(DISTINCT ?label; separator=';') AS ?label) ?xing "
						+ "WHERE {"
						+ "   ?work a bf:Work ; "
						+ "         dc:title ?dtitle . "
						+ "   FILTER (lang(?dtitle) = 'cht')"
						+ "   OPTIONAL {?work bf:creator ?cs . "
						+ "       {SELECT ?cs ?creator FROM <"
						+ "http://gen.library.sh.cn/graph/person"
						+ "> WHERE {"
						+ "           ?cs bf:label ?creator . FILTER (lang(?creator) = 'cht')"
						+ "       }}"
						+ "   }"
						+ "   OPTIONAL {?work shl:description ?desc .}"
						+ "   OPTIONAL {?work bf:subject ?th . "
						+ "       {SELECT ?th ?label FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo"
						+ "> WHERE { "
						+ "           ?th a shl:TitleOfAncestralTemple ; bf:label ?label . FILTER (lang(?label) = 'cht') "
						+ "       }}"
						+ "   }"
						+ "   OPTIONAL {?work bf:subject ?fn . "
						+ "       {SELECT ?fn ?xing FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo"
						+ "> WHERE { "
						+ "           ?fn a shl:FamilyName ; bf:label ?xing . FILTER (lang(?xing) = 'cht') "
						+ "       }}"
						+ "   }"
						+ " OPTIONAL{SELECT ?work ?doi FROM <http://gen.library.sh.cn/graph/item> WHERE {   ?item a bf:Item ;shl:accessLevel ?acc;shl:DOI ?doi; bf:itemOf ?ins.  {SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}}}"
						+ "   OPTIONAL {?work shl:place ?ps . "
						+ "       {SELECT ?ps ?location FROM <"
						+ "http://gen.library.sh.cn/graph/place"
						+ "> WHERE { "
						+ "           ?ps a shl:Place ; bf:label ?location. FILTER (lang(?location) = 'cht')"
						+ "       }}" + "   }" + "   FILTER (?work = <" + uri
						+ ">)" + "}";

				ArrayList work = SparqlExecution
						.vQuery(this.graph, query, new String[] { "work",
								"dtitle", "title", "subtitles", "desc",
								"creators", "location", "label", "xing", "doi" });
				list.add((Map) work.get(0));
			}

			result.setResultList(list);
		}
		return result;
	}

	@Override
	public Long countItemsByWork(WorkSearchBean bean){
		String sparql = Namespace.getNsPrefixString() 
				+ " SELECT COUNT(DISTINCT ?item) as ?count "
				+ " from <" + Constant.GRAPH_WORK+ "> "
				+ " from <" + Constant.GRAPH_INSTANCE+ "> "
				+ " from <" + Constant.GRAPH_ITEM+ "> "
				+ " WHERE {?work a bf:Work; dc:title ?dtitle. "
				+ " ?s bf:instanceOf ?work. ?item bf:itemOf ?s . "
				+ "  " + MergeSearchParts.workSearchClause(bean) + " }";
		Map countMap = (Map) SparqlExecution.vQuery(this.graph, sparql,
				new String[] { "count" }).get(0);
		return Long.valueOf(Long.parseLong(RDFUtils.toString(countMap
				.get("count"))));
	}
	
	
	public QueryResult<Map<String, Object>> getWorks(WorkSearchBean bean,
			int start, int size) {
		String clause = MergeSearchParts.workSearchClause(bean);

		String sql = "WHERE {    ?work a bf:Work ; dc:title ?dtitle." + clause
				+ "}";

		String countQuery = Namespace.getNsPrefixString()
				+ "SELECT (COUNT(DISTINCT ?work) AS ?count) " + sql;

		String workQuery = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work " + sql
				+ "OFFSET " + start + " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery,
				new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap
				.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);

		List list = new ArrayList();
		if (count.longValue() > 0L) {
			ArrayList works = SparqlExecution.vQuery(this.graph, workQuery,
					new String[] { "work" });
			for (int i = 0; i < works.size(); i++) {
				String uri = ((Map) works.get(i)).get("work").toString();

				String query = Namespace.getNsPrefixString()
						+ "SELECT DISTINCT ?work ?dtitle ?title ?desc ?doi "
						+ "(GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles) "
						+ "(GROUP_CONCAT(DISTINCT ?creator;separator=';') AS ?creators) "
						+ "?location (GROUP_CONCAT(DISTINCT ?label; separator=';') AS ?label) ?xing "
						+ "WHERE {"
						+ "   ?work a bf:Work ; "
						+ "         dc:title ?dtitle . "
						+ "   FILTER (lang(?dtitle) = 'cht')"
						+ "   OPTIONAL {?work bf:creator ?cs . "
						+ "       {SELECT ?cs ?creator FROM <"
						+ "http://gen.library.sh.cn/graph/person"
						+ "> WHERE {"
						+ "           ?cs bf:label ?creator . FILTER (lang(?creator) = 'cht')"
						+ "       }}"
						+ "   }"
						+ "   OPTIONAL {?work shl:description ?desc .}"
						+ "   OPTIONAL {?work bf:subject ?th . "
						+ "       {SELECT ?th ?label FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo"
						+ "> WHERE { "
						+ "           ?th a shl:TitleOfAncestralTemple ; bf:label ?label . FILTER (lang(?label) = 'cht') "
						+ "       }}"
						+ "   }"
						+ " OPTIONAL{SELECT ?work ?doi FROM <http://gen.library.sh.cn/graph/item> WHERE {   ?item a bf:Item ;shl:accessLevel ?acc;shl:DOI ?doi; bf:itemOf ?ins.  {SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}}}"
						+ "   OPTIONAL {?work bf:subject ?fn . "
						+ "       {SELECT ?fn ?xing FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo"
						+ "> WHERE { "
						+ "           ?fn a shl:FamilyName ; bf:label ?xing . FILTER (lang(?xing) = 'cht') "
						+ "       }}" + "   }" + "   FILTER (?work = <" + uri
						+ ">)" + "}";

				ArrayList work = SparqlExecution
						.vQuery(this.graph, query, new String[] { "work",
								"dtitle", "title", "subtitles", "desc",
								"creators", "location", "label", "xing", "doi" });
				list.add((Map) work.get(0));
			}

			result.setResultList(list);
		}
		return result;
	}

	public ArrayList getTitles(String work_uri) {
		String query = Namespace.getNsPrefixString() + "SELECT ?title ?type " + "WHERE {"
				+ "   <" + work_uri + "> bf:workTitle ?t ."
				+ "   ?t bf:titleValue ?title ; "
				+ "      bf:titleType ?type . " + "FILTER (?type != '0')" + "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] {
				"title", "type" });
	}

	public ArrayList getCreator(String work_uri) {
		String query = Namespace.getNsPrefixString() + "SELECT ?name " + "WHERE {" + "   <"
				+ work_uri + "> dc:creator ?name . " + "}";

		return SparqlExecution.vQuery(this.graph, query,
				new String[] { "name" });
	}

	public ArrayList getWorksInLatLong(String place_str) {
		String query = Namespace.getNsPrefixString() + "";

		if (place_str.startsWith("http://")) {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {   ?work bf:title ?ts ;          shl:place <"
					+ place_str
					+ "> . "
					+ "{SELECT ?ts ?title FROM <"
					+ "http://gen.library.sh.cn/graph/title"
					+ "> WHERE {?ts a bf:WorkTitle ; bf:label ?title . FILTER (lang(?title)='cht')}}"
					+ "} LIMIT 20";
		} else {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {    ?work bf:title ?ts ;          shl:place ?place . {SELECT ?ts ?title FROM <http://gen.library.sh.cn/graph/title> WHERE {?ts a bf:WorkTitle ; bf:label ?title . FILTER (lang(?title)='cht')}}{   SELECT ?place FROM <http://gen.library.sh.cn/graph/place>    WHERE {        ?place ?p '"
					+ place_str + "' ." + "   }}" + "} LIMIT 20";
		}

		ArrayList results = SparqlExecution.vQuery(this.graph, query,
				new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points
						+ this.placeSparql.getLongLat(RDFUtils.toString(place
								.get("place"))) + ";";
			}

			if (points.endsWith(";")) {
				points = StringUtils.substringBeforeLast(points, ";");
			}

			((Map) results.get(i)).put("points", points);
		}

		return results;
	}

	public ArrayList getWorksInPlace(Map standPlace, String familyName) {
		String filter = "";

		if (StringUtils.isNotBlank(familyName)) {
			Map xing = this.baseinfoSparql.getXings(familyName);

			if (xing.size() > 0) {
				String uri = xing.get("uri").toString();
				filter = "FILTER (?subject = <" + uri + ">) ";
			}
		}

		String query = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work (if (STRLEN(STR(?title )) > 0, ?title, ?dtitle) AS ?title) "
				+ "WHERE { " + "   ?work a bf:Work; dc:title ?dtitle;bf:title ?ts ; "
				+ "         bf:subject ?subject ; "
				+ "         shl:place ?place . " + "optional{SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "   ?ts a bf:WorkTitle ; bf:label ?title . "
				+ "   FILTER (lang(?title)='cht') " + "}}" + filter
				+ "{SELECT ?place FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> " + "   WHERE { "
				+ "       ?place a shl:Place . ";

		if (StringUtils.isNotBlank(standPlace.get("country").toString())) {
			query = query + "?place shl:country '"
					+ standPlace.get("country").toString() + "'@cht .";
		}

		if (StringUtils.isNotBlank(standPlace.get("prov").toString())) {
			query = query + "?place shl:province '"
					+ standPlace.get("prov").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("city").toString())) {
			query = query + "?place shl:city '"
					+ standPlace.get("city").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("county").toString())) {
			query = query + "?place shl:county '"
					+ standPlace.get("county").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("label").toString())) {
			query = query + "?place bf:label '"
					+ standPlace.get("label").toString() + "'@cht .";
		}

		query = query + "}}.FILTER (lang(?dtitle)='cht')} LIMIT 20";

		ArrayList results = SparqlExecution.vQuery(this.graph, query,
				new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points
						+ this.placeSparql.getLongLat(RDFUtils.toString(place
								.get("place"))) + ";";
			}

			if (points.endsWith(";")) {
				points = StringUtils.substringBeforeLast(points, ";");
			}

			((Map) results.get(i)).put("points", points);
		}

		return results;
	}

	public ArrayList getWorksInLatLong(String place_str, String familyName) {
		String query = Namespace.getNsPrefixString() + "";

		if (place_str.startsWith("http://")) {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {   ?work dc:title ?title ;          bf:place <"
					+ place_str + "> . " + "FILTER CONTAINS(?title, '"
					+ familyName + "')" + "} LIMIT 20";
		} else {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {    ?work dc:title ?title ;          bf:place ?place . {   SELECT ?place FROM <http://gen.library.sh.cn/graph/place>    WHERE {        ?place ?p '"
					+ place_str + "' ." + "   }" + "}"
					+ "FILTER CONTAINS(?title, '" + familyName + "')" + "}";
		}

		ArrayList results = SparqlExecution.vQuery(this.graph, query,
				new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points
						+ this.placeSparql.getLongLat(RDFUtils.toString(place
								.get("place"))) + ";";
			}

			if (points.endsWith(";")) {
				points = StringUtils.substringBeforeLast(points, ";");
			}

			((Map) results.get(i)).put("points", points);
		}

		return results;
	}

	public ArrayList getWorkPlaces(String work_uri) {
		String query = Namespace.getNsPrefixString() + "SELECT ?place ?label " + "WHERE {"
				+ "   <" + work_uri + "> bf:place ?place . "
				+ "   {SELECT ?place ?label FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {"
				+ "       ?place bf:label ?label ." + "   }}" + "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] {
				"place", "label" });
	}

	public ArrayList getWorks4Person(String person_uri, boolean inference) {
		inference = false;
		String filter = "FILTER ((STR(?uri) = '" + person_uri + "') ";

		if ((inference) && (this.appConfig.getPersonSameAs() != null)) {
			String str = "";
			String str1 = "";

			String query = Namespace.getNsPrefixString() + "SELECT DISTINCT ?uri FROM <"
					+ "http://gen.library.sh.cn/graph/person" + "> "
					+ "WHERE {" + "   ?uri a shl:Person .";

			int num = 0;
			for (String ns : this.appConfig.getPersonSameAs()) {
				num++;
				str = str + "    ?uri " + ns + " ?v" + num + " . ";
				str1 = str1 + "    ?uri1 " + ns + " ?v" + num + " . ";
			}

			query = query + str + "{SELECT";

			for (int i = 1; i < num + 1; i++) {
				query = query + " ?v" + i;
			}

			query = query
					+ " FROM <http://gen.library.sh.cn/graph/person> WHERE {"
					+ str1 + "FILTER (STR(?uri1) = '" + person_uri + "')"
					+ "}}" + "FILTER (STR(?uri) != '" + person_uri + "')" + "}";

			ArrayList results = SparqlExecution.vQuery(this.graph, query,
					new String[] { "uri" });

			if (results.size() > 0) {
				for (int i = 0; i < results.size(); i++) {
					filter = filter + "|| (STR(?uri) = '"
							+ ((Map) results.get(i)).get("uri").toString()
							+ "') ";
				}
			}

		}

		filter = filter + ") ";

		String query = Namespace.getNsPrefixString()
				+ "SELECT ?work (GROUP_CONCAT(DISTINCT ?cr;separator=';') AS ?creator) ?dtitle ?title (GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles) ?note ?roles FROM <"
				+ "http://gen.library.sh.cn/graph/work"
				+ "> "
				+ "WHERE{"
				+ "   ?work dc:title ?dtitle . "
				+ "   FILTER (lang(?dtitle) = 'cht')"
				+ "   OPTIONAL {?work shl:description ?note .} "
				+ "OPTIONAL {?work bf:title ?ts . "
				+ "   {SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "       ?ts a bf:WorkTitle; bf:label ?title . FILTER (lang(?title)='cht')"
				+ "   }}"
				+ "}"
				+ "OPTIONAL {?work bf:title ?sts . "
				+ "   {SELECT ?sts ?t FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "       ?sts a bf:VariantTitle; bf:label ?t . FILTER (lang(?t)='cht')"
				+ "   }}"
				+ "}"
				+ "OPTIONAL {?work bf:creator ?cs ."
				+ "   {SELECT ?cs ?cr FROM <"
				+ "http://gen.library.sh.cn/graph/person"
				+ "> WHERE {"
				+ "       ?cs bf:label ?cr . "
				+ "   FILTER (lang(?cr)='cht')"
				+ "   }}"
				+ "}"
				+ "   {SELECT ?work (GROUP_CONCAT(?role; separator=';') AS ?roles) FROM <"
				+ "http://gen.library.sh.cn/graph/person" + "> WHERE{"
				+ "       <" + person_uri + "> shl:relatedWork ?work . "
				+ "   OPTIONAL {<" + person_uri + "> shl:roleOfFamily ?r . "
				+ "   {SELECT ?r ?role FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "       ?r bf:label ?role . "
				+ "   FILTER (lang(?role)='cht')" + "   }}" + "}" + "   }}"
				+ "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "work",
				"creator", "dtitle", "title", "subtitles", "note", "roles" });
	}

	public Map getWorkInfos(String work_uri) {
		
		String query = Namespace.getNsPrefixString()
				+ "SELECT ?accessLevelUC ?dtitle ?title "
				+ "(GROUP_CONCAT(DISTINCT ?tv; separator=';') AS ?subtitle) "
				+ "(GROUP_CONCAT(DISTINCT ?creator; separator=';') AS ?creator) ?note (GROUP_CONCAT(DISTINCT ?label; separator=';') AS ?label) "
				+ "WHERE{"
				+ "   <"
				+ work_uri
				+ "> a bf:Work ; dc:title ?dtitle . "
				+ "   FILTER (lang(?dtitle) = 'cht')"
				+ "OPTIONAL {<"
				+ work_uri
				+ "> bf:title ?ts ."
				+ "   {SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "       ?ts a bf:WorkTitle ; "
				+ "           bf:label ?title . "
				+ "       FILTER (lang(?title)='cht') "
				+ "   }}"
				+ "}"
				+ "OPTIONAL { <"
				+ work_uri
				+ "> shl:accessLevelUC ?accessLevelUC} "
				+ "OPTIONAL { <"
				+ work_uri
				+ "> shl:description ?note . } "
				+ "OPTIONAL { <"
				+ work_uri
				+ "> bf:creator ?cs . "
				+ "   {SELECT ?cs ?creator FROM <"
				+ "http://gen.library.sh.cn/graph/person"
				+ "> WHERE {"
				+ "       ?cs bf:label ?creator . "
				+ "   FILTER (lang(?creator) = 'cht')"
				+ "   }}"
				+ "} "
				+ "OPTIONAL { "
				+ "   <"
				+ work_uri
				+ "> bf:title ?tss . "
				+ "   {SELECT ?tss ?tv FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "       ?tss a bf:VariantTitle ; "
				+ "            bf:label ?tv . "
				+ "   FILTER (lang(?tv)='cht') "
				+ "   }}"
				+ "}"
				+ "OPTIONAL { "
				+ "   <"
				+ work_uri
				+ "> bf:subject ?th . "
				+ "   {SELECT ?th ?label FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo"
				+ "> WHERE {"
				+ "       ?th a shl:TitleOfAncestralTemple ; bf:label ?label . "
				+ "   FILTER (lang(?label)='cht')" + "   }}" + "} .FILTER(!BOUND(?accessLevelUC) || str(?accessLevelUC) != '9')}";

		List list = SparqlExecution.vQuery(this.graph, query, new String[] {
				"dtitle", "accessLevelUC","title", "subtitle", "creator", "note", "label" });
		if (CollectionUtils.isNotEmpty(list)) {
			return (Map) list.get(0);
		}
		return null;
	}

	public ArrayList getInstances4Work(String work_uri) {
		String query = Namespace.getNsPrefixString()
				+ "SELECT DISTINCT ?ins ?temporal ?extent ?edition ?category ?item (GROUP_CONCAT(DISTINCT ?description ;separator=';') AS ?description ) ?hbs ?ab ?place ?org ?addr ?shelf ?doi ?accessLevel "
				+ "FROM <"
				+ "http://gen.library.sh.cn/graph/instance"
				+ "> "
				+ "WHERE {       "
				+ "   ?ins bf:instanceOf <"
				+ work_uri
				+ "> . "
				+ "   OPTIONAL {?ins shl:temporalValue ?temporal . FILTER (datatype(?temporal) != xsd:integer) }"
				+ "   OPTIONAL {"
				+ "       ?ins bf:edition ?es . "
				+ "       {SELECT ?es ?edition FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo"
				+ "> WHERE {"
				+ "           ?es bf:label ?edition ."
				+ "           FILTER (lang(?edition)='chs') "
				+ "       }}"
				+ "   }"
				+ "   OPTIONAL {"
				+ "       ?ins bf:category ?cs ."
				+ "       {SELECT ?cs ?category FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo"
				+ "> WHERE {"
				+ "           ?cs bf:label ?category ."
				+ "           FILTER (lang(?category)='chs')"
				+ "       }}"
				+ "   }"
				+ "   OPTIONAL {?ins bf:extent ?extent .}"
				+ "   OPTIONAL {"
				+ "       SELECT ?ins ?item ?description ?doi ?accessLevel ?hbs ?ab ?place ?org ?addr ?shelf FROM <"
				+ "http://gen.library.sh.cn/graph/item"
				+ "> WHERE {"
				+ "           ?item a bf:Item ; bf:itemOf ?ins ."
				+ "           OPTIONAL {?item shl:description ?description .}"
				+ "           OPTIONAL {?item shl:DOI ?doi .}"
				+ "           OPTIONAL {?item shl:accessLevel ?accessLevel .}"
				+ "           OPTIONAL {?item bf:shelfMark ?shelf .}"
				+ "           OPTIONAL {"
				+ "               ?item bf:heldBy ?hbs ."
				+ "               {SELECT ?hbs ?ab ?org FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo"
				+ "> WHERE {"
				+ "                   ?hbs shl:abbreviateName ?ab ; bf:label ?org ."
				+ "                   FILTER (lang(?ab)='chs')"
				+ "                   FILTER (lang(?org)='chs')"
				+ "               }}" + "               OPTIONAL {"
				+ "                   SELECT ?hbs ?addr FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "                       ?hbs shl:address ?addr ."
				+ "                   }" + "               }"
				+ "               OPTIONAL {"
				+ "                   SELECT ?hbs ?place FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "                       ?hbs shl:region ?rs . "
				+ "                       {SELECT ?rs ?place FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {"
				+ "                           ?rs bf:label ?place . "
				+ "                           FILTER (lang(?place)='chs')"
				+ "                       }} " + "               }}"
				+ "           }" + "   }}" + "} ORDER BY (?ab)";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "ins",
				"temporal", "edition", "extent", "category", "item",
				"description", "org", "addr", "hbs", "ab", "place", "shelf",
				"doi", "accessLevel" });
	}

	public ArrayList getAllWorksWithGeo() {
		String query = Namespace.getNsPrefixString() + "SELECT ?work ?title ?note ?begin ?end "
				+ "WHERE {" + "   ?work dc:title ?title ;"
				+ "         bf:place ?place . "
				+ "   OPTIONAL { ?work bf:hasAnnotation ?ann . "
				+ "       {SELECT ?ann ?note FROM <"
				+ "http://gen.library.sh.cn/graph/annotation" + "> WHERE{"
				+ "           ?ann bf:label ?note . " + "       }}" + "   }"
				+ "{" + "   SELECT ?instance ?begin ?end FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> "
				+ "   WHERE {"
				+ "       ?instance bf:publishedOn/shl:beginYear ?begin ; "
				+ "                 bf:publishedOn/shl:endYear ?end . "
				+ "   }" + "}" + "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "work",
				"title", "note", "begin", "end" });
	}

	public ArrayList getWorksInYear(String begin, String end, int limit) {
		String query = Namespace.getNsPrefixString()
				+ "SELECT ?work ?dtitle ?title (GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles) ?note (if (STRLEN(STR(?time)) > 0, ?time, ?tb) AS ?begin) ?label ?long ?lat "
				+ "WHERE {"
				+ "   ?work bf:title ?ts ; dc:title ?dtitle ; shl:place ?ps . "
				+ "   FILTER (lang(?dtitle) = 'cht')"
				+ "   OPTIONAL {?work shl:description ?note .}"
				+ "   {SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "       ?ts a bf:WorkTitle ; bf:label ?title . "
				+ "       FILTER (lang(?title)='cht')"
				+ "   }}"
				+ "   OPTIONAL {?work bf:title ?sts . "
				+ "       {SELECT ?sts ?t FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "           ?sts a bf:VariantTitle; bf:label ?t . FILTER (lang(?t)='cht')"
				+ "       }}" + "   }" + "   {SELECT ?work ?time ?tb FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       {" + "           ?ins bf:instanceOf ?work ; "
				+ "                shl:temporalValue ?time . "
				+ "           FILTER ((?time >= " + begin + ") && (?time <= "
				+ end + "))" + "       } UNION {"
				+ "           ?ins shl:temporal ?ts . "
				+ "           {SELECT ?tb FROM <"
				+ "http://gen.library.sh.cn/graph/temporal" + "> WHERE {"
				+ "               ?ts shl:beginYear ?tb . FILTER ((?tb >= "
				+ begin + ") && (?tb <= " + end + "))" + "           }}"
				+ "       }" + "    }}"
				+ "   {SELECT ?ps ?label ?long ?lat FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {"
				+ "       ?ps bf:label ?label ; "
				+ "              owl:sameAs ?same . "
				+ "       FILTER (lang(?label)='cht') "
				+ "       {SELECT DISTINCT ?same ?long ?lat FROM <"
				+ "http://www.cba.ac.cn/graph/geography" + "> WHERE {"
				+ "           ?same geo:long ?long ; "
				+ "                 geo:lat ?lat . " + "       }}" + "   }}"
				+ "} ORDER BY ASC(xsd:integer(?begin)) ";

		if (limit > 0) {
			query = query + " LIMIT " + limit;
		}

		return SparqlExecution.vQuery(this.graph, query, new String[] { "work",
				"dtitle", "title", "subtitles", "note", "begin", "end",
				"label", "long", "lat" });
	}

	public ArrayList getWorksInChao(String year) {
		String query = Namespace.getNsPrefixString()
				+ "SELECT DISTINCT ?work ?title ?place"
				+ " (if (STRLEN(STR(?time)) > 0, ?time, ?tb) AS ?begin) ?long ?lat "
				+ "WHERE {"
				+ "   ?work shl:place ?ps ; bf:title ?ts. "
				+ "   {SELECT ?ps ?place ?long ?lat FROM <"
				+ "http://gen.library.sh.cn/graph/place"
				+ "> WHERE {"
				+ "       ?ps bf:label ?place ; "
				+ "              owl:sameAs ?same . "
				+ "       FILTER (lang(?place)='chs') "
				+ "       {SELECT DISTINCT ?same ?long ?lat FROM <"
				+ "http://www.cba.ac.cn/graph/geography"
				+ "> WHERE {"
				+ "           ?same geo:long ?long ; "
				+ "                 geo:lat ?lat . "
				+ "       }}"
				+ "   }}"
				+ "   {SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title"
				+ "> WHERE {"
				+ "       ?ts bf:label ?title ; a bf:WorkTitle . FILTER (lang(?title)='cht') "
				+ "   }}"
				+ "   {SELECT ?work ?time ?tb FROM <"
				+ "http://gen.library.sh.cn/graph/instance"
				+ "> WHERE {"
				+ "       {"
				+ "           ?ins bf:instanceOf ?work ; shl:temporalValue ?time . FILTER (?time = "
				+ year + ") " + "       } UNION {"
				+ "           ?ins bf:instanceOf ?work ; shl:temporal ?ts . "
				+ "           {SELECT ?tb FROM <"
				+ "http://gen.library.sh.cn/graph/temporal" + "> WHERE {"
				+ "               ?ts shl:beginYear ?tb . FILTER (?tb = "
				+ year + ") " + "           }}" + "       }" + "   }}" + "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "work",
				"title", "place", "begin", "long", "lat" });
	}

	public OutputStream getWorkAllInfos(String work_uri) {
		Model m = ModelFactory.createDefaultModel();

		String query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "WHERE {{"
				+ "   ?s ?p ?o ." + "   FILTER (STR(?s) = '" + work_uri + "')"
				+ "} UNION {" + "   <" + work_uri + "> bf:workTitle ?s ."
				+ "   ?s ?p ?o ." + "}" + "}";

		m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <"
				+ "http://gen.library.sh.cn/graph/annotation" + "> "
				+ "WHERE {" + "   ?s ?p ?o ." + "{SELECT DISTINCT ?s WHERE {"
				+ "   <" + work_uri + "> bf:hasAnnotation ?s ." + "}}" + "}";

		m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> " + "WHERE {{"
				+ "   ?s ?p ?o ." + "   {SELECT DISTINCT ?s WHERE {"
				+ "       <" + work_uri + "> bf:hasInstance ?s ." + "   }}"
				+ "} UNION {" + "   ?s ?p ?o ;" + "      shl:holdingFor ?in ."
				+ "   {SELECT DISTINCT ?in WHERE {" + "       <" + work_uri
				+ "> bf:hasInstance ?s . " + "   }}" + "}" + "}";

		m.add(SparqlExecution.construct(this.graph, query));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		m.write(stream, "JSON-LD");

		return stream;
	}

	public OutputStream getResource(String work_uri) {
		Model temp_m = ModelFactory.createDefaultModel();
		String query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <"
				+ "http://gen.library.sh.cn/graph/work" + "> " + "WHERE {"
				+ "   ?s ?p ?o . " + "   FILTER (?s = <" + work_uri + ">)"
				+ "}";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> " + "WHERE {"
				+ "   ?s ?p ?o ; bf:instanceOf <" + work_uri + "> ." + "}";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <"
				+ "http://gen.library.sh.cn/graph/item" + "> " + "WHERE {"
				+ "   ?s bf:itemOf ?ins ; ?p ?o ." + "   {SELECT ?ins FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       ?ins bf:instanceOf <" + work_uri + "> ." + "   }}"
				+ "}";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		temp_m.write(stream, "RDF/XML-ABBREV");

		return stream;
	}

	public OutputStream getTriples(WorkSearchBean search, String format) {
		Model temp = ModelFactory.createDefaultModel();

		String clause = MergeSearchParts.workSearchClause(search);

		String sql = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work " + "WHERE { "
				+ "   ?work a bf:Work ; dc:title ?dtitle." + clause
				+ "} LIMIT 100";

		ArrayList works = SparqlExecution.vQuery(this.graph, sql,
				new String[] { "work" });
		for (int i = 0; i < works.size(); i++) {
			String uri = ((Map) works.get(i)).get("work").toString();

			temp.add(getWorkModel(uri));
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		temp.write(stream, format);

		return stream;
	}

	private Model getWorkModel(String work_uri) {
		Model temp_m = ModelFactory.createDefaultModel();
		String query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <"
				+ "http://gen.library.sh.cn/graph/work" + "> " + "WHERE {"
				+ "   ?s ?p ?o . " + "   FILTER (?s = <" + work_uri + ">)"
				+ "}";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> " + "WHERE {"
				+ "   ?s ?p ?o ; bf:instanceOf <" + work_uri + "> ." + "}";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <"
				+ "http://gen.library.sh.cn/graph/item" + "> " + "WHERE {"
				+ "   ?s bf:itemOf ?ins ; ?p ?o ." + "   {SELECT ?ins FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       ?ins bf:instanceOf <" + work_uri + "> ." + "   }}"
				+ "}";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s shl:relatedWork <" + work_uri
				+ "> .} " + "FROM <" + "http://gen.library.sh.cn/graph/person"
				+ "> " + "WHERE {" + "   ?s shl:roleOfFamily ?role ; "
				+ "      shl:relatedWork <" + work_uri + "> ." + "} LIMIT 50";

		temp_m.add(SparqlExecution.construct(this.graph, query));

		return temp_m;
	}
}
