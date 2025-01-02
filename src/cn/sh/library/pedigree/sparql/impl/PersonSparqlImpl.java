package cn.sh.library.pedigree.sparql.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.AppConfig;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.sparql.MergeSearchParts;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.Namespace;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;

@GraphDefine(name = "http://gen.library.sh.cn/graph/person")
@Repository
public class PersonSparqlImpl extends BaseDaoImpl implements PersonSparql {
	private List rules;

	@Resource
	private StringBuffer nsPrefix;

	@Resource
	private AppConfig appConfig;

	public QueryResult<Map<String, Object>> getPersons(PersonSearchBean bean, int start, int size) {
		String clause = MergeSearchParts.personSearchClause(bean);

		String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?name) AS ?count) " + "WHERE {"
				+ "   ?s a shl:Person ; " + "      bf:label ?name ; " + "      shl:relatedWork ?work ; "
				+ "      foaf:familyName ?f . " + "   FILTER (lang(?name)='cht')" + clause + "}";

		String query = this.nsPrefix
				+ "SELECT DISTINCT ?name (GROUP_CONCAT(DISTINCT ?s;separator=';') AS ?uri) (GROUP_CONCAT(DISTINCT ?w;separator=';') AS ?work) "
				+ "WHERE {" + "   ?s a shl:Person ; " + "      bf:label ?name ; " + "      shl:relatedWork ?w ; "
				+ "      foaf:familyName ?f . " + "FILTER (lang(?name) = 'cht')" + clause
				+ "} GROUP BY ?name ?uri ?work ORDER BY ASC(?name) OFFSET " + start + "LIMIT " + size;
		Long count = 0L;
		List<Map<String, String>> _list = RDFUtils
				.transformListMap(SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }));
		count = StringUtilC.getLong(_list.get(0).get("count"));
		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "name", "work" }));
		}
		return result;
	}

	public OutputStream getPersons4API(PersonSearchBean bean, String format) {
		String clause = MergeSearchParts.personSearchClause(bean);
		String uri = "";

		if (StringUtils.isNotBlank(bean.getUri())) {
			uri = " FILTER (?s = <" + bean.getUri() + ">)";
		}

		String query = this.nsPrefix + "CONSTRUCT {?s ?p ?o ; rel:parentOf ?child.} " + "WHERE {" + "   ?s ?p ?o ."
				+ "   OPTIONAL {?child rel:childOf ?s .} " + "   {SELECT DISTINCT ?s " + "   WHERE {"
				+ "       ?s a shl:Person ; " + "          foaf:familyName ?f . " + clause + uri + "   } LIMIT 100}"
				+ "}";

		Model temp = ModelFactory.createDefaultModel();
		temp.add(SparqlExecution.construct(this.graph, query));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		temp.write(stream, format);

		return stream;
	}

	public QueryResult<Map<String, Object>> getPersonsInHome(String familyName_uri, int start, int size) {
		QueryResult result = new QueryResult();
		String countQuery = this.nsPrefix + "SELECT (COUNT(DISTINCT ?name) AS ?count) " + "WHERE {"
				+ "   ?uri a shl:Person; bf:label ?name ; " + "        shl:roleOfFamily ?r ; "
				+ "        foaf:familyName <" + familyName_uri + "> ; " + "        shl:relatedWork ?work . "
				+ "   FILTER (lang(?name) = 'cht')" + "}";

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		result.setTotalrecord(count - size);
		if (count.longValue() > 0L) {

			if (start > count) {

				start = count.intValue() - size;
			} else {
				result.setTotalrecord(count);
			}
			String str = this.nsPrefix
					+ "SELECT DISTINCT ?name (GROUP_CONCAT(?s; separator=',') AS ?uri) (GROUP_CONCAT(?w; separator=',') AS ?work) "
					+ "WHERE {" + "   ?s a shl:Person; bf:label ?name ; " + "        shl:roleOfFamily ?r ; "
					+ "        foaf:familyName <" + familyName_uri + "> ; " + "        shl:relatedWork ?w . "
					+ "   FILTER (lang(?name) = 'cht')" + "} OFFSET " + start + " LIMIT " + size;

			result.setResultList(SparqlExecution.vQuery(this.graph, str, new String[] { "uri", "name", "work" }));
		}
		return result;
	}

	public String countPersons(String time_uri) {
		String query = this.nsPrefix + "SELECT (COUNT(DISTINCT ?s) AS ?count) " + "WHERE {" + "   ?s a shl:Person ; "
				+ "      foaf:name ?name ; " + "      shl:temporal <" + time_uri + "> . "
				+ "FILTER langMatches(lang(?name),'') " + "FILTER CONTAINS(STR(?s), '/Person/')" + "}";

		ArrayList results = SparqlExecution.vQuery(this.model, query, new String[] { "count" });

		if (results.size() > 0) {
			return (String) ((Map.Entry) RDFUtils.transform((Map) results.get(0)).entrySet().iterator().next())
					.getValue();
		}

		return "0";
	}

	public ArrayList getPersonsInDynasty(int limit) {
		String query = this.nsPrefix + "SELECT ?time ?dynasty ?begin ?name " + "WHERE {"
				+ "   ?s a shl:Person;foaf:name ?name ; " + "      foaf:name ?py ; " + "      shl:temporal ?time . "
				+ "{SELECT ?time ?dynasty ?begin FROM <" + "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "   ?time shl:beginYear ?begin ; " + "         shl:dynasty ?dynasty . " + "}}"
				+ "FILTER CONTAINS(STR(?s),'/Person/')" + "FILTER (lang(?name)='')" + "FILTER (lang(?py)='en')"
				+ "} ORDER BY ?time ?py";

		return SparqlExecution.vQuery(this.model, query, limit, "time",
				new String[] { "time", "dynasty", "begin", "name" });
	}

	/**
	 * 新增，更近人的URI，获取人的详细信息 chenss 20170821
	 * 
	 * @param uri
	 * @return
	 */
	public List<Map<String, String>> getInfos4Person(String uri) {
		try {
			
			if (uri.contains("jp")) {
				String query = this.nsPrefix
						+ "SELECT DISTINCT ?s ?p ?o WHERE { ?s a shl:Person; ?p ?o.filter(STR(?s)='" + uri + "')}";
				List<Map<String, String>> _Jplist = SparqlExecution.vQuery(this.model, query, 
						new String[] { "s", "p", "o" });
				return _Jplist;
			} else {
				String RDF_QUERY_PO4S_LITERAL = Namespace.getNsPrefixString() + "SELECT ?p ?o " + " WHERE {"
						+ "   <%1$s> ?p ?o . " + "  FILTER (lang(?o) = '' || lang(?o) = 'cht') }";

				List<Map<String, String>> _Mrlist = RDFUtils.transformListMap(SparqlExecution.vQuery(
						CodeMsgUtil.getConfig("remoteEndpoint"),
						String.format(RDF_QUERY_PO4S_LITERAL, new Object[] { uri }), new String[] { "p", "o" }));

				return _Mrlist;
			}

		} catch (Exception e) {
			System.out.println("vt错误：" + e + "URI:" + uri);
			return null;
		}
	}

	public ArrayList getImg(String uri) {

		String sparql = Namespace.getNsPrefixString() + "SELECT (foaf:img as ?p) ?o WHERE {<" + uri + "> foaf:img ?o }";

		ArrayList list = SparqlExecution.vQuery(CodeMsgUtil.getConfig("remoteEndpoint"),
				String.format(sparql, new Object[] { uri }), new String[] { "p", "o" });

		return list;

	}

	public ArrayList getPersons4Work(String work_uri) {
		String query = this.nsPrefix + "SELECT ?uri ?name ?dynasty " + "WHERE {" + "   ?uri a shl:Person ; "
				+ "        foaf:name ?name ; " + "        bf:relatedTo <" + work_uri + "> ." + "OPTIONAL {"
				+ "   ?uri shl:temporal ?time . " + "   {SELECT ?time ?dynasty ?begin FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {" + "       ?time shl:dynasty ?dynasty ;"
				+ "             shl:beginYear ?begin . " + "   }}" + "}" + "FILTER (lang(?name) = '') "
				+ "FILTER CONTAINS(STR(?uri),'/Person/') " + "} ORDER BY ASC(?begin)";

		return SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "name", "dynasty" });
	}

	public ArrayList getFamRels4Work(String work_uri) {
		String query = this.nsPrefix + "SELECT distinct ?uri ?name (GROUP_CONCAT(DISTINCT ?role; separator=';') AS ?roles)  (GROUP_CONCAT(DISTINCT ?time; separator=';') AS ?time) ?serialNo  ?order\r\n"
				+ "from <http://gen.library.sh.cn/graph/person>\r\n"
				+ "from <http://gen.library.sh.cn/graph/baseinfo>\r\n"
				+ " WHERE {   \r\n"
				+ "?uri a shl:Person\r\n"
				+ "{?uri shl:relatedWork <"+work_uri+">}\r\n"
				+ "{?uri bf:label ?name .FILTER (lang(?name)='chs')}\r\n"
				+ "{?uri  shl:roleOfFamily ?rUri. {?rUri bf:label ?role .FILTER (lang(?role)='chs')} \r\n"
				+ "}\r\n"
				+ "OPTIONAL {?uri shl:temporalValue ?time  }\r\n"
				+ "OPTIONAL {?uri shl:serialNo ?serialNo  }\r\n"
				+ "OPTIONAL {?uri shl:orderOfSeniority ?order .}\r\n"
				+ "\r\n"
				+ "} order by  ?name  ?serialNo ?order desc(?time) limit 50";
	
		return SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "serialNo",  "name", "roles", "order", "time" });
	}

	private void getTriples(List fl, Model temp, String uri) {
		try {
			String graph_name = "http://gen.library.sh.cn/graph/baseinfo";

			if ((uri.contains("/creator/")) || (uri.contains("/person/")))
				graph_name = "http://gen.library.sh.cn/graph/person";
			else if ((uri.contains("/Instance/")) || (uri.contains("/Temporal/published/")))
				graph_name = "http://gen.library.sh.cn/graph/instance";
			else if ((uri.contains("/work/")) || (uri.contains("/Title/")))
				graph_name = "http://gen.library.sh.cn/graph/work";
			else if (uri.contains("/annotation/"))
				graph_name = "http://gen.library.sh.cn/graph/annotation";
			else if (uri.contains("/place/"))
				graph_name = "http://gen.library.sh.cn/graph/place";
			else if (uri.contains("/place/"))
				graph_name = "http://gen.library.sh.cn/graph/place";
			else if (uri.contains("/ontology/"))
				graph_name = "http://gen.library.sh.cn/graph/vocab";
			else {
				graph_name = "http://gen.library.sh.cn/graph/baseinfo";
			}

			if (!fl.contains(uri)) {
				String query = this.nsPrefix + "CONSTRUCT {<" + uri + "> ?p ?o} " + "FROM <" + graph_name + "> "
						+ "WHERE {" + "   <" + uri
						+ "> ?p ?o . FILTER (?p != <http://www.library.sh.cn/ontology/description>)}";

				temp.add(SparqlExecution.construct(this.graph, query));
				fl.add(uri);

				query = this.nsPrefix + "SELECT DISTINCT ?p ?o " + "FROM <" + graph_name + "> " + "WHERE { "
						+ "   ?s ?p ?o ." + "FILTER (STR(?s)='" + uri + "') "
						+ "FILTER ((?p != rel:spouseOf) && (?p != rel:childOf))" + "FILTER isIRI(?o) " + "}";

				ArrayList results = SparqlExecution.vQuery(this.graph, query, new String[] { "p", "o" });

				if (results.size() > 0)
					for (int i = 0; i < results.size(); i++) {
						String p = ((Map) results.get(i)).get("p").toString();

						if (!p.endsWith("summaryOf")) {
							String u = ((Map) results.get(i)).get("o").toString();
							getTriples(fl, temp, u);
						}
					}
			}
		} catch (Exception e) {
			System.out.println("vt错误-getTriples：" + uri + ":" + e);
		}

	}

	public void export() {
		this.model.write(System.out, "TTL");
	}

	public QueryResult<Map<String, Object>> getPersons(String q, int start, int size) {
		String filter = "";

		if (StringUtils.isNotBlank(q))
			filter = "FILTER CONTAINS(?label, '" + q + "')";
		else {
			filter = "FILTER (lang(?label) = 'cht')";
		}

		String countQuery = this.nsPrefix + "SELECT count(DISTINCT ?uri) as ?count " + "WHERE {"
				+ "{?uri a shl:Person ; " + "      bf:label ?label . " + "} UNION {" + "   ?uri a shl:Person ; "
				+ "        foaf:familyName ?f . " + "   {SELECT ?f ?label FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {" + "       ?f bf:label ?label . " + "   }}"
				+ "}" + filter + "}";

		String query = this.nsPrefix
				+ "SELECT DISTINCT ?uri ?chs ?fn_chs (if (STRLEN(?cat) > 0, STR(?cat), 'Compiler') AS ?role) WHERE { "
				+ "   ?uri a shl:Person ; bf:label ?chs ; foaf:familyName ?f. " + filter + "OPTIONAL { "
				+ "   ?uri shl:roleOfFamily ?r . " + "   {SELECT ?r ?cat FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {" + "       ?r bf:label ?cat ."
				+ "   FILTER (lang(?cat) = 'cht')}}" + "}" + "{SELECT ?f ?fn_chs FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {" + "   ?f bf:label ?fn_chs ."
				+ "FILTER (lang(?fn_chs) = 'cht')}}" + "{SELECT ?uri WHERE {"
				+ "   {?uri a shl:Person ; bf:label ?label . " + filter + "   } UNION { "
				+ "   ?uri a shl:Person ; foaf:familyName ?f . " + "   {SELECT ?f FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE{" + "       ?f bf:label ?label ." + filter
				+ "   }}}" + "} OFFSET " + start + " LIMIT " + size + " }" + "FILTER (lang(?chs) = 'cht') "
				+ "} ORDER BY ?fn_chs";

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(
					SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs", "fn_chs", "role" }));
		}
		return result;
	}

	public QueryResult<Map<String, Object>> getPersons(PersonSearchBean search, int tag, String type, int start,
			int size) {
		String countQuery = "";
		String query = "";

		if (0 == tag) {
			countQuery = this.nsPrefix + "SELECT COUNT(DISTINCT ?uri) AS ?count " + "WHERE {"
					+ "   ?uri a shl:Person ; " + "        shl:roleOfFamily ?r ; " + "        foaf:familyName ?f ; "
					+ "        shl:relatedWork ?work ; " + "        bf:label ?name ; " + "        bf:label ?chs . "
					+ "   FILTER (?r != <http://data.library.sh.cn/jp/vocab/ancestor/ming-ren>)"
					+ "   {SELECT ?f ?fn_en ?r ?cat_label FROM <" + "http://gen.library.sh.cn/graph/baseinfo"
					+ "> WHERE {" + "       ?f a shl:FamilyName ; " + "          bf:label ?fn_label ; "
					+ "          bf:label ?fn_en . " + "       ?r a bf:Category ; " + "          bf:label ?cat_label . "
					+ "   FILTER (lang(?fn_en) = 'en') " + MergeSearchParts.baseClause(search) + "   }}"
					+ MergeSearchParts.personClause(search) + "}";

			query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?cat_label ?fn_en " + "WHERE {"
					+ "   ?uri a shl:Person ; " + "        shl:roleOfFamily ?r ; " + "        foaf:familyName ?f ; "
					+ "        shl:relatedWork ?work ; " + "        bf:label ?name ; " + "        bf:label ?chs . "
					+ "   FILTER (lang(?chs) = 'cht')"
					+ "   FILTER (?r != <http://data.library.sh.cn/jp/vocab/ancestor/ming-ren>)"
					+ "   {SELECT ?f ?fn_en ?r ?cat_label FROM <" + "http://gen.library.sh.cn/graph/baseinfo"
					+ "> WHERE {" + "       ?f a shl:FamilyName ; " + "          bf:label ?fn_label ; "
					+ "          bf:label ?fn_en . " + "       ?r a bf:Category ; " + "          bf:label ?cat_label . "
					+ "   FILTER (lang(?cat_label)='cht')" + "   FILTER (lang(?fn_en) = 'en') "
					+ MergeSearchParts.baseClause(search) + "   }}" + MergeSearchParts.personClause(search)
					+ "} ORDER BY ?fn_en ?chs " + "OFFSET " + start + " LIMIT " + size;
		} else if (1 == tag) {
			countQuery = this.nsPrefix + "SELECT COUNT(DISTINCT ?uri) AS ?count " + "WHERE {"
					+ "   ?uri a shl:Person ; " + "        shl:roleOfFamily ?r ; " + "        foaf:familyName ?f ; "
					+ "        shl:relatedWork ?work ; " + "        bf:label ?name ; " + "        bf:label ?chs . "
					+ "   FILTER (?r = <http://data.library.sh.cn/jp/vocab/ancestor/ming-ren>)"
					+ "   {SELECT ?f ?fn_en ?r ?cat_label FROM <" + "http://gen.library.sh.cn/graph/baseinfo"
					+ "> WHERE {" + "       ?f a shl:FamilyName ; " + "          bf:label ?fn_label ; "
					+ "          bf:label ?fn_en . " + "       ?r a bf:Category ; " + "          bf:label ?cat_label . "
					+ "   FILTER (lang(?fn_en) = 'en') " + MergeSearchParts.baseClause(search) + "   }}"
					+ MergeSearchParts.personClause(search) + "}";

			query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?cat_label ?fn_en " + "WHERE {"
					+ "   ?uri a shl:Person ; " + "        shl:roleOfFamily ?r ; " + "        foaf:familyName ?f ; "
					+ "        shl:relatedWork ?work ; " + "        bf:label ?name ; " + "        bf:label ?chs . "
					+ "   FILTER (lang(?chs) = 'cht')"
					+ "   FILTER (?r = <http://data.library.sh.cn/jp/vocab/ancestor/ming-ren>)"
					+ "   {SELECT ?f ?fn_en ?r ?cat_label FROM <" + "http://gen.library.sh.cn/graph/baseinfo"
					+ "> WHERE {" + "       ?f a shl:FamilyName ; " + "          bf:label ?fn_label ; "
					+ "          bf:label ?fn_en . " + "       ?r a bf:Category ; " + "          bf:label ?cat_label . "
					+ "   FILTER (lang(?cat_label)='cht')" + "   FILTER (lang(?fn_en) = 'en') "
					+ MergeSearchParts.baseClause(search) + "   }}" + MergeSearchParts.personClause(search)
					+ "} ORDER BY ?fn_en ?chs " + "OFFSET " + start + " LIMIT " + size;
		} else if (2 == tag) {
			countQuery = this.nsPrefix + "SELECT COUNT(DISTINCT ?uri) AS ?count " + "WHERE {"
					+ "   ?uri a shl:Person ; " + "        foaf:familyName ?f ; " + "        bf:label ?name ; "
					+ "        bf:role ?r ; " + "        bf:label ?chs . " + "   {SELECT ?r ?cat_label FROM <"
					+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
					+ "       ?r a bf:Category ; bf:label ?cat_label ; bf:label ?label . "
					+ "       FILTER (lang(?cat_label)='cht')" + MergeSearchParts.baseCatClause(search) + "   }}"
					+ "   {SELECT ?f ?fn_en FROM <" + "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
					+ "       ?f a shl:FamilyName ; " + "          bf:label ?fn_label ; "
					+ "          bf:label ?fn_en . " + "       FILTER (lang(?fn_en) = 'en') "
					+ MergeSearchParts.baseClause(search) + "   }}" + MergeSearchParts.personClause(search) + "}";

			query = this.nsPrefix + "SELECT DISTINCT ?uri ?chs ?cat_label ?fn_en " + "WHERE {"
					+ "   ?uri a shl:Person ; " + "        foaf:familyName ?f ; " + "        bf:label ?name ; "
					+ "        bf:role ?r ; " + "        bf:label ?chs . " + "   FILTER (lang(?chs) = 'cht')"
					+ "   {SELECT ?r ?cat_label FROM <" + "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
					+ "       ?r a bf:Category ; bf:label ?cat_label ; bf:label ?label . "
					+ "       FILTER (lang(?cat_label)='cht')" + MergeSearchParts.baseCatClause(search) + "   }}"
					+ "   {SELECT ?f ?fn_en FROM <" + "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
					+ "       ?f a shl:FamilyName ; " + "          bf:label ?fn_label ; "
					+ "          bf:label ?fn_en . " + "   FILTER (lang(?fn_en) = 'en') "
					+ MergeSearchParts.baseClause(search) + "   }}" + MergeSearchParts.personClause(search)
					+ "} ORDER BY ?fn_en ?chs " + "OFFSET " + start + " LIMIT " + size;
		}

		Map countMap = (Map) SparqlExecution.vQuery(this.graph, countQuery, new String[] { "count" }).get(0);
		Long count = Long.valueOf(Long.parseLong(RDFUtils.toString(countMap.get("count"))));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			result.setResultList(
					SparqlExecution.vQuery(this.graph, query, new String[] { "uri", "chs", "cat_label", "fn_en" }));
		}
		return result;
	}

	public ArrayList<Map<String, String>> countResState(String fn, int tag) {
		if (0 == tag) {
			String str = "";

			if (StringUtils.isNotBlank(fn)) {
				str = this.nsPrefix
						+ "SELECT ?label ?uri ?pc ?wc ?my ?mb (if (STRLEN(STR(?my)) > 0, ?my, ?mb) AS ?year) "
						+ "WHERE {"
						+ "   SELECT ?uri (COUNT(DISTINCT ?label) AS ?pc) (COUNT(DISTINCT ?work) AS ?wc) (min(?y) AS ?my) (min(?begin) AS ?mb) WHERE {"
						+ "       ?s a shl:Person;foaf:familyName ?uri ; shl:roleOfFamily ?r ; bf:label ?label. "
						+ "       FILTER (lang(?label)='cht') " + "       {SELECT ?uri FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
						+ "           ?uri a shl:FamilyName; bf:label ?label . FILTER REGEX(STR(?label), '^" + fn
						+ "$') " + "       }}" + "   }" + "}";
			} else {
				str = this.nsPrefix
						+ "SELECT ?label ?uri ?pc ?wc ?my ?mb (if (STRLEN(STR(?my)) > 0, ?my, ?mb) AS ?year) "
						+ "WHERE {"
						+ "   SELECT ?label ?uri ?py (COUNT(DISTINCT ?label) AS ?pc) (COUNT(DISTINCT ?work) AS ?wc) (min(?y) AS ?my) (min(?begin) AS ?mb) WHERE {"
						+ "       ?s a shl:Person; foaf:familyName ?uri ; shl:roleOfFamily ?r ; bf:label ?label. "
						+ "       FILTER (lang(?label)='cht') " + "       {SELECT ?uri ?label ?py FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
						+ "           ?uri a shl:FamilyName; bf:label ?label; bf:label ?py. "
						+ "           FILTER (lang(?label)='cht') " + "           FILTER (lang(?py)='en')" + "       }}"
						+ "   } " + "} ORDER BY ASC(?py)";
			}

			return SparqlExecution.vQuery(this.graph, str, new String[] { "label", "uri", "pc", "wc", "year" });
		}
		String str = "";

		if (StringUtils.isNotBlank(fn)) {
			str = this.nsPrefix + "SELECT ?label ?uri ?pc ?wc ?my ?mb (if (STRLEN(STR(?my)) > 0, ?my, ?mb) AS ?year) "
					+ "WHERE {"
					+ "   SELECT ?uri (COUNT(DISTINCT ?s) AS ?pc) (COUNT(DISTINCT ?work) AS ?wc) (min(?y) AS ?my) (min(?begin) AS ?mb) "
					+ "   FROM <" + "http://gen.library.sh.cn/graph/work" + "> WHERE {"
					+ "       ?work a bf:Work; bf:subject ?uri ." + "       OPTIONAL {SELECT ?work ?begin ?y FROM <"
					+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
					+ "           ?s a bf:Instance; bf:instanceOf ?work ."
					+ "           OPTIONAL {?s shl:temporal ?time . " + "               {SELECT ?time ?begin FROM <"
					+ "http://gen.library.sh.cn/graph/temporal" + "> WHERE {"
					+ "                   ?time shl:beginYear ?begin ." + "               }}" + "           }"
					+ "           OPTIONAL {?s shl:temporalValue ?y . FILTER (datatype(?y) = xsd:integer)  }"
					+ "       }}" + "       {SELECT ?uri FROM <" + "http://gen.library.sh.cn/graph/baseinfo"
					+ "> WHERE {" + "           ?uri a shl:FamilyName; bf:label ?label . FILTER REGEX(STR(?label), '^"
					+ fn + "$') " + "       }}" + "   }" + "}";
		} else {
			str = this.nsPrefix + "SELECT ?label ?uri ?pc ?wc ?my ?mb (if (STRLEN(STR(?my)) > 0, ?my, ?mb) AS ?year) "
					+ "WHERE {"
					+ "   SELECT ?uri (COUNT(DISTINCT ?s) AS ?pc) (COUNT(DISTINCT ?work) AS ?wc) (min(?y) AS ?my) (min(?begin) AS ?mb) "
					+ "   FROM <" + "http://gen.library.sh.cn/graph/work" + "> WHERE {"
					+ "       ?work bf:subject ?uri ." + "       OPTIONAL {SELECT ?work ?begin ?y FROM <"
					+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {" + "           ?s bf:instanceOf ?work ."
					+ "           OPTIONAL {?s shl:temporal ?time . " + "               {SELECT ?time ?begin FROM <"
					+ "http://gen.library.sh.cn/graph/temporal" + "> WHERE {"
					+ "                   ?time shl:beginYear ?begin ." + "               }}" + "           }"
					+ "           OPTIONAL {?s shl:temporalValue ?y . FILTER (datatype(?y) = xsd:integer)  }"
					+ "       }}" + "       {SELECT ?uri ?label ?py FROM <" + "http://gen.library.sh.cn/graph/baseinfo"
					+ "> WHERE {" + "           ?uri a shl:FamilyName; bf:label ?label; bf:label ?py. "
					+ "           FILTER (lang(?label)='cht') " + "           FILTER (lang(?py)='en')" + "       }}"
					+ "   } " + "} ORDER BY ASC(?py)";
		}

		return SparqlExecution.vQuery(this.graph, str, new String[] { "label", "uri", "pc", "wc", "year" });
	}
}