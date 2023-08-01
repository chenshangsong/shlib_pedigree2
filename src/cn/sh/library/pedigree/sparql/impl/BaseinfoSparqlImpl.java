package cn.sh.library.pedigree.sparql.impl;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.MergeSearchParts;
import cn.sh.library.pedigree.utils.PinyinUtil;
import cn.sh.library.pedigree.utils.RDFUtils;

@Repository
@GraphDefine(name = "http://gen.library.sh.cn/graph/baseinfo")
public class BaseinfoSparqlImpl extends BaseDaoImpl implements BaseinfoSparql {

	@Resource
	private StringBuffer nsPrefix;

	public ArrayList getTemporal() {
		/* 30 */ String query = this.nsPrefix + "SELECT ?uri ?name ?begin ?end " + "WHERE { "
				+ "   ?uri a shl:Temporal ; " + "        shl:dynasty ?name ; " + "        shl:beginYear ?begin ; "
				+ "        shl:endYear ?end . " + "} ORDER BY ASC(?begin)";

		/* 39 */ return SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "name", "begin", "end" });
	}

	public Map getTime4Dyansty(String uri) {
		/* 44 */ String query = this.nsPrefix + "SELECT ?begin ?end " + "WHERE { " + "   <" + uri
				+ "> a shl:Temporal ; " + "        shl:beginYear ?begin ; " + "        shl:endYear ?end . " + "}";

		/* 52 */ return (Map) SparqlExecution.vQuery(this.graph, query, new String[] { "begin", "end" }).get(0);
	}

	public ArrayList getTemporals4TL() {
		/* 57 */ String query = this.nsPrefix + "SELECT ?uri ?name " + "WHERE {" + "   ?uri a shl:Temporal ; "
				+ "        shl:dynasty ?name ; " + "        rdfs:label 'timeline' ; "
				+ "        shl:beginYear ?begin . " + "} ORDER BY DESC(?begin)";

		/* 66 */ return SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "name" });
	}

	public QueryResult<Map<String, Object>> getPersons(String temporal_uri, int start, int size) {
		/* 71 */ String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?person) AS ?count) FROM <"
				+ "http://gen.library.sh.cn/graph/person" + "> " + "WHERE {" + "       ?person a shl:Person ; "
				+ "            shl:temporal <" + temporal_uri + "> . " + "}";

		/* 78 */ String query = this.nsPrefix + "SELECT DISTINCT ?person ?name FROM <"
				+ "http://gen.library.sh.cn/graph/person" + "> " + "WHERE {" + "       ?person a shl:Person ; "
				+ "            foaf:name ?name ; " + "            foaf:name ?py ; " + "            shl:temporal <"
				+ temporal_uri + "> . " + "   FILTER (lang(?name) = '')" + "   FILTER (lang(?py) = 'en')"
				+ "} ORDER BY ?py " + "OFFSET " + start + " LIMIT " + size;

		/* 90 */ Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		/* 91 */ Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		/* 93 */ QueryResult result = new QueryResult();
		/* 94 */ result.setTotalrecord(count);
		/* 95 */ if (count.longValue() > 0L) {
			/* 96 */ result.setResultList(SparqlExecution.vQuery(this.graph, query, new String[] { "person", "name" }));
		}
		/* 98 */ return result;
	}

	public QueryResult<Map<String, Object>> getWorks(String temporal_uri, int start, int size) {
		String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?work) AS ?count) FROM <"
				+ "http://gen.library.sh.cn/graph/work" + "> " + "WHERE {" + "   ?work bf:hasInstance ?instance ; "
				+ "         dc:title ?title . " + "   {SELECT ?instance ?begin FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       ?instance bf:publishedOn/shl:beginYear ?b ; "
				+ "                 bf:publishedOn/shl:endYear ?e ." + "       {SELECT ?begin ?end WHERE {"
				+ "           <" + temporal_uri + "> shl:beginYear ?begin ; " + "              shl:endYear ?end . "
				+ "       }}" + "       FILTER ((?b >= ?begin) && (?b <= ?end))" + "   }}" + "}";

		String query = this.nsPrefix + "SELECT DISTINCT ?work ?title FROM <" + "http://gen.library.sh.cn/graph/work"
				+ "> " + "WHERE {" + "   ?work bf:hasInstance ?instance ; " + "         dc:title ?title . "
				+ "   {SELECT ?instance ?begin FROM <" + "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       ?instance bf:publishedOn/shl:beginYear ?b ; "
				+ "                 bf:publishedOn/shl:endYear ?e ." + "       {SELECT ?begin ?end WHERE {"
				+ "           <" + temporal_uri + "> shl:beginYear ?begin ; " + "              shl:endYear ?end . "
				+ "       }}" + "       FILTER ((?b >= ?begin) && (?b <= ?end))" + "   }}" + "} ORDER BY ASC(?begin) "
				+ "OFFSET " + start + " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(SparqlExecution.vQuery(this.graph, query, new String[] { "work", "title" }));
		}
		return result;
	}

	public String getCHT4CHS(String predicate, String chs_str) {
		String query = this.nsPrefix + "SELECT ?cht " + "WHERE { " + "   ?s " + predicate + " '" + chs_str + "'@chs ;"
				+ predicate + " ?cht . " + "FILTER langMatches(lang(?cht), 'cht') " + "}";

		ArrayList results = SparqlExecution.jQuery(this.model, query, new String[] { "cht" });

		if (results.size() > 0) {
			return ((Map) results.get(0)).get("cht").toString();
		}

		return null;
	}

	public ArrayList getAllFamilyNames() {
		String query = this.nsPrefix + "SELECT (UCASE(?cha) AS ?char) ?uri ?chs ?cht ?en " + "WHERE { "
				+ "   ?uri a shl:FamilyName ; " + "      bf:label ?chs ; " + "      bf:label ?cht ; "
				+ "      bf:label ?en . " + "FILTER langMatches(lang(?chs),'chs') "
				+ "FILTER langMatches(lang(?cht),'cht') " + "FILTER langMatches(lang(?en),'en') "
				+ "BIND (SUBSTR(?en, 1, 1) AS ?cha) " + "} ORDER BY ASC(?char) ASC(?en)";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "char", "uri", "chs", "cht", "en" });
	}

	public ArrayList getFamilyNames(String q,Boolean accurateFlag) {
		String filter = "";

		if (StringUtils.isNotBlank(q)) {
			filter = " FILTER CONTAINS(str(?label), '" + q + "')";
			//如果是精确匹配 chenss 2023 0728 解决家谱联合编目0727姓氏匹配问题
			if(accurateFlag) {
				filter = " FILTER (str(?label)= '" + q + "')";
			}

		}

		String query = this.nsPrefix + "SELECT DISTINCT ?uri (UCASE(?cha) AS ?char) ?chs ?cht ?en " + "WHERE { "
				+ "   ?uri a shl:FamilyName ; " + "      bf:label ?chs ; " + "      bf:label ?cht ; "
				+ "      bf:label ?label ; " + "      bf:label ?en . " + filter + "FILTER (lang(?chs) = 'chs') "
				+ "FILTER (lang(?cht) = 'cht') " + "FILTER (lang(?en) = 'en') " + "BIND (SUBSTR(?en, 1, 1) AS ?cha) ";

		query = query + "} ORDER BY ASC(?char) ASC(?en)";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "char", "uri", "chs", "cht", "en" });
	}

	public ArrayList getPersonFamilyNames() {
		String query = this.nsPrefix + "SELECT DISTINCT ?uri " + "WHERE {" + "   ?uri a shl:FamilyName . " + "{"
				+ "   SELECT ?uri FROM <" + "http://gen.library.sh.cn/graph/person" + ">" + "   WHERE {"
				+ "       ?s a shl:Person ; " + "          foaf:familyName ?uri ." + "   }" + "}" + "}";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "uri" });
	}

	public QueryResult<Map<String, Object>> getAncestralTemple(AncTempSearchBean search, int start, int size) {
		String filter = MergeSearchParts.ancTempSearchClause(search);

		String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?uri) AS ?count) " + "WHERE {"
				+ "   ?uri a shl:TitleOfAncestralTemple ; " + "        bf:label ?label ; " + "        bf:label ?chs ; "
				+ "        shl:familyName ?fn . " + "   ?fn a shl:FamilyName ; " + "       bf:label ?fn_en ; "
				+ "       bf:label ?fn_chs . " + "FILTER (lang(?chs) = 'chs') " + "FILTER (lang(?fn_chs) = 'chs') "
				+ "FILTER (lang(?fn_en) = 'en') " + filter + "}";

		String query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?fn_chs ?fn_en " + "WHERE {"
				+ "   ?uri a shl:TitleOfAncestralTemple ; " + "        bf:label ?label ; " + "        bf:label ?chs ; "
				+ "        shl:familyName ?fn . " + "   ?fn a shl:FamilyName ; " + "       bf:label ?fn_en ; "
				+ "       bf:label ?fn_chs . " + "FILTER (lang(?chs) = 'chs') " + "FILTER (lang(?fn_chs) = 'chs') "
				+ "FILTER (lang(?fn_en) = 'en') " + filter + "} ORDER BY ?fn_en ?fn_chs " + "OFFSET " + start
				+ " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(
					SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs", "fn_chs", "fn_en" }));
		}
		return result;
	}
	
	public QueryResult<Map<String, Object>> getAncestralTempleForShiGuang(AncTempSearchBean search, int start, int size) {
		String filter = MergeSearchParts.ancTempSearchClause(search);

		String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?chs) AS ?count) " + " WHERE {"
				+ "   ?uri a shl:TitleOfAncestralTemple ; " + "        bf:label ?label ; " + "        bf:label ?chs ; "
				+ "        shl:familyName ?fn . " + "   ?fn a shl:FamilyName ; " + "       bf:label ?fn_en ; "
				+ "       bf:label ?fn_chs . " + "FILTER (lang(?chs) = 'chs') " + "FILTER (lang(?fn_chs) = 'chs') "
				+ "FILTER (lang(?fn_en) = 'en') " + filter + "}";

		String query = this.nsPrefix + "SELECT DISTINCT ?chs" + " WHERE {"
				+ "   ?uri a shl:TitleOfAncestralTemple ; " + "        bf:label ?label ; " + "        bf:label ?chs ; "
				+ "        shl:familyName ?fn . " + "   ?fn a shl:FamilyName ; " + "       bf:label ?fn_en ; "
				+ "       bf:label ?fn_chs . " + "FILTER (lang(?chs) = 'chs') " + "FILTER (lang(?fn_chs) = 'chs') "
				+ "FILTER (lang(?fn_en) = 'en') " + filter + "} ORDER BY ?fn_en ?fn_chs " + "OFFSET " + start
				+ " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(
					SparqlExecution.vQuery(this.graph, query, new String[] { "chs" }));
		}
		return result;
	}
	public QueryResult<Map<String, Object>> getOrganization(String q, int start, int size) {
		String filter = "";

		if (StringUtils.isNotBlank(q)) {
			filter = "FILTER CONTAINS(str(?label), '" + q + "')";
		}

		String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?uri) AS ?count) " + "WHERE {"
				+ "   ?uri a shl:Organization ; " + "        bf:label|shl:abbreviateName ?label . " + filter + "}";

		String query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?ab_chs ?place " + "WHERE {"
				+ "   ?uri a shl:Organization " + "{?uri  bf:label ?chs } "
				+ " OPTIONAL {?uri shl:abbreviateName ?ab_chs } " + "{?uri bf:label|shl:abbreviateName ?label }"
				+ "  OPTIONAL {?uri shl:region ?p . " + filter + "{SELECT ?p ?place FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {" + "   ?p bf:label ?place . "
				+ "FILTER (lang(?place) = 'chs') " + "}}}" + ".FILTER (lang(?chs) = 'chs') "
				+ "FILTER (lang(?ab_chs) = 'chs') " + filter + "} " + "OFFSET " + start + " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(
					SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs", "ab_chs", "place" }));
		}
		return result;
	}

	// 编目系统使用 chenss_20200630
	public QueryResult<Map<String, Object>> getOrganizationForBM(String q, int start, int size) {
		String filter = "";

		if (StringUtils.isNotBlank(q)) {
			filter = ".?label <bif:contains> '\"" + q + "\"'";
		}

		String countQuery = this.nsPrefix + "select (count (distinct ?uri ) as ?count)  from <http://gen.library.sh.cn/graph/baseinfo> where {?uri a shl:Organization"+
		"{"+
			 "{?uri bf:label ?label "+filter+"}"+
			 " union "+
			 "{?uri shl:abbreviateName ?label "+filter+"}"+
			"}"+
			 "} ";

		String query =  this.nsPrefix + "select distinct ?uri ?chs   from <http://gen.library.sh.cn/graph/baseinfo> where {?uri a shl:Organization;bf:label ?chs"+
				"{"+
				 "{?uri bf:label ?label "+filter+"}"+
				 " union "+
				 "{?uri shl:abbreviateName ?label "+filter+"}"+
				"}"+
				 ".filter(lang(?chs)='chs')} " + "OFFSET " + start + " LIMIT " + size;

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));  

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs" }));
		}
		return result;
	}

	public Map<String, String> getXings(String xing) {
		String query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?cht " + "WHERE { " + "   ?uri a shl:FamilyName ; "
				+ "      bf:label ?chs ; " + "      bf:label ?cht ; " + "      bf:label ?label . "
				+ "FILTER langMatches(lang(?chs),'chs') " + "FILTER langMatches(lang(?cht),'cht') "
				+ "FILTER (STR(?label) = '" + xing + "') " + "}";

		ArrayList lists = SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs", "cht" });

		if (lists.size() > 0) {
			return (Map) lists.get(0);
		}

		return null;
	}

	public String getPersonFamilyName(String uri) {
		String query = this.nsPrefix + "SELECT ?chs " + "WHERE {" + "   ?s bf:label ?chs . " + "{SELECT ?s FROM <"
				+ "http://gen.library.sh.cn/graph/migration" + "> WHERE {" + "   <" + uri + "> foaf:familyName ?s . "
				+ "}}" + "FILTER (lang(?chs) = 'chs') " + "}";

		ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "chs" });

		if (results.size() > 0) {
			return RDFUtils.getValue(((Map) results.get(0)).get("chs").toString());
		}

		return "";
	}

	public void generatePinYin() {
		String query = this.nsPrefix + "SELECT ?uri ?chs " + "WHERE { " + "   ?uri a shl:FamilyName ; "
				+ "        bf:label ?chs . " + "FILTER NOT EXISTS { " + "   ?uri bf:label ?py . "
				+ "   FILTER langMatches(lang(?py),'en') " + "} " + "FILTER langMatches(lang(?chs),'chs') " + "}";

		ArrayList results = SparqlExecution.jQuery(this.model, query, new String[] { "uri", "chs" });

		for (int i = 0; i < results.size(); i++) {
			String uri = ((Map) results.get(i)).get("uri").toString();
			String chs = RDFUtils.getValue(((Map) results.get(i)).get("chs").toString());

			String chs_py = PinyinUtil.getPingYin(chs);
			chs_py = chs_py.replaceFirst(chs_py.substring(0, 1), chs_py.substring(0, 1).toUpperCase());

			String sql = "INSERT DATA { GRAPH <http://gen.library.sh.cn/graph/baseinfo> { <" + uri + "> bf:label '"
					+ chs_py + "'@en . " + "}" + "} ";

			SparqlExecution.update(this.graph, sql);
		}
	}

	public void export() {
		this.model.write(System.out, "TTL");
	}
}
