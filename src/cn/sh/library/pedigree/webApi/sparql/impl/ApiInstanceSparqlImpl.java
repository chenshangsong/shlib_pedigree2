package cn.sh.library.pedigree.webApi.sparql.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.sparql.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.sparql.ApiInstanceSparql;

@Repository
@GraphDefine(name = "http://gen.library.sh.cn/graph/instance")
public class ApiInstanceSparqlImpl extends BaseDaoImpl implements
		ApiInstanceSparql {

	@Resource
	private StringBuffer nsPrefix;

	@Override
	public QueryResult<Map<String, Object>> getFamilyRules(String keyword,
			int start, int pagesize, String[] workUris) {
		String filter = "";
		if (!StringUtilC.isEmpty(keyword)) {
			filter += ".filter ( contains(concat(?dcTitle, ' ' , ?label) , '" + keyword
					+ "')  || exists{?s1 a shl:FamilyRule; ?p ?o .filter(contains(str(?o), '"+keyword
					+"')) .?s1 dct:isPartOf* ?uri})";
		}
		if (workUris != null && workUris.length > 0) {
			filter += " . VALUES ?work {";
			for (String workUri : workUris) {
				filter += "<" + workUri + ">";
			}

			filter += "}";
		}

		String fieldCdtn = " .?uri dc:title ?title ;shl:code ?code "
				+ " .optional{?uri shl:description ?description} "
				+ " .?uri bf:familyRuleFrom ?instance  "
				+ " .optional{?instance bf:edition/bf:label ?label .filter(lang(?label)='cht')} "
				+ " .?instance bf:instanceOf ?work . ?work dc:title ?dcTitle .filter(lang(?dcTitle)='cht') "
				+ " .optional{?work bf:subject ?sub. ?sub a shl:FamilyName;bf:label ?familyName .filter(lang(?familyName)='cht')} "
				+ " .optional{?person a shl:Person; bf:label ?labelP; shl:relatedWork ?work ;shl:roleOfFamily ?role .values ?role {<http://data.library.sh.cn/jp/vocab/ancestor/shi-zu><http://data.library.sh.cn/jp/vocab/ancestor/shi-qian-zu><http://data.library.sh.cn/jp/vocab/ancestor/ming-ren>}. filter (lang(?labelP) = 'cht')}";

		String queryClause = this.nsPrefix
				+ " select %1$s "
				+ " from <http://gen.library.sh.cn/graph/baseinfo> "
				+ " from <http://gen.library.sh.cn/graph/work> "
				+ " from <http://gen.library.sh.cn/graph/instance> "
				+ " from <http://gen.library.sh.cn/graph/person> "
				+ " where{ "
				+ " ?uri a shl:FamilyRule "
				+ " .filter not exists{?uri dct:isPartOf ?isPartOf} %2$s  %3$s "
				// + (StringUtilC.isEmpty(keyword) ? "" : fieldCdtn) + filter
				+ " } %4$s ";

		String field = "(count(distinct ?uri) as ?cnt) ";

		ArrayList results = new ArrayList();
		results = SparqlExecution.vQuery(
				this.graph,
				String.format(queryClause,
						new Object[] { field,
								(StringUtilC.isEmpty(filter) ? "" : fieldCdtn),
								filter, "" }), new String[] { "cnt" });
		Long count = Long.valueOf(Long.parseLong(RDFUtils
				.getValue(((Map) results.get(0)).get("cnt").toString())));

		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			String limit = " order by ?code offset " + start + " limit "
					+ pagesize;
			field = " distinct ?uri ?title ?code ?description  (concat(?dcTitle, ' ' , ?label) as ?label) "
					+ "(str(?instance) as ?instanceUri) (str(?work) as ?workUri) "
					+ "(GROUP_CONCAT(str(?labelP); separator=' ') AS ?labelP)"
					+ " (str(?familyName) as ?familyName)";
			results = SparqlExecution.vQuery(
					this.graph,
					String.format(queryClause, new Object[] { field, fieldCdtn,
							filter, limit }), new String[] { "uri", "title",
							"code", "description", "label", "instanceUri",
							"workUri", "labelP", "familyName" });

			result.setResultList(results);
		}
		return result;
	}

	public Map<String, Object> getFamilyRuleInfo(String uri, boolean isWithSub) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		resMap.put("uri", uri);
		String RDF_QUERY_PO4S_LITERAL = this.nsPrefix + "SELECT ?p ?o "
				+ "WHERE {" + "   <%1$s> ?p ?o . " + "   FILTER isLiteral(?o)"
				+ "  FILTER (lang(?o) = '' || lang(?o) = 'cht') }";
		ArrayList list = SparqlExecution.vQuery(this.graph,
				String.format(RDF_QUERY_PO4S_LITERAL, new Object[] { uri }),
				new String[] { "p", "o" });
		resMap.putAll(getMap(list, null, new String[] { "content" }));

		String RDF_QUERY_PO4S_IRI = this.nsPrefix + "SELECT ?p ?o " + "WHERE {"
				+ "   <%1$s> ?p ?o . " + "   FILTER isIRI(?o) "
				+ "   FILTER (?p != rdf:type)" + "}";
		Map map = RDFUtils.getMap(SparqlExecution.vQuery(this.graph,
				String.format(RDF_QUERY_PO4S_IRI, new Object[] { uri }),
				new String[] { "p", "o" }));
		resMap.putAll(map);

		resMap.putAll(getFamilyRuleFromLabel(uri));
		if (isWithSub) {
			List<Map<String, Object>> subFamilyRules = getSubFamilyRules(uri,
					isWithSub);
			if (subFamilyRules.size() > 0) {
				resMap.put("subFamilyRules", subFamilyRules);
			}
		}

		return resMap;
	}

	public Map<String, Object> getFamilyRuleFromLabel(String uri) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String RDF_QUERY_FAMILYRULE_FROM = this.nsPrefix
				+ " SELECT ?workUri  (concat(?dcTitle, ' ' , ?label) as ?label)"
				+ " from <http://gen.library.sh.cn/graph/baseinfo> "
				+ " from <http://gen.library.sh.cn/graph/work> "
				+ " from <http://gen.library.sh.cn/graph/instance> "
				+ " where{"
				+ " <%1$s> bf:familyRuleFrom ?instance  "
				+ " .optional{?instance bf:edition/bf:label ?label .filter(lang(?label)='cht')} "
				+ " .?instance bf:instanceOf ?workUri.?workUri dc:title ?dcTitle .filter(lang(?dcTitle)='cht') "
				+ " }";

		ArrayList list = SparqlExecution.vQuery(this.graph,
				String.format(RDF_QUERY_FAMILYRULE_FROM, new Object[] { uri }),
				new String[] { "workUri", "label" });
		resMap.putAll((Map) list.get(0));
		return resMap;
	}

	public List<Map<String, Object>> getSubFamilyRules(String uri,
			boolean isWithSub) {

		List<Map<String, String>> subList = new ArrayList<Map<String, String>>();
		String RDF_QUERY_FAMILYRULE_FROM = this.nsPrefix
				+ " select ?s "
				+ " where{?s a shl:FamilyRule; dct:isPartOf <%1$s>; shl:code ?code} order by ?code ";
		if (StringUtilC.isEmpty(uri)) {
			RDF_QUERY_FAMILYRULE_FROM = this.nsPrefix + " select ?s "
					+ " where {" + "?s a shl:FamilyRule "
					+ ".filter not exists{?s dct:isPartOf ?isp} "
					+ "} limit 20";

		}
		List<Map<String, String>> list = RDFUtils
				.transformListMap(SparqlExecution.vQuery(this.graph,
						String.format(RDF_QUERY_FAMILYRULE_FROM,
								new Object[] { uri }), new String[] { "s" }));

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (list.size() > 0) {
			for (Map<String, String> map : list) {
				result.add(getFamilyRuleInfo(map.get("s"), isWithSub));
			}
		}
		return result;
	}

	@Override
	public QueryResult<Map<String, Object>> getFamilyRulesByWorks(String[] workUris,
			int start, int pagesize) {
		String worksFilter = "";
		for (int i = 0; i < workUris.length; i++) {
			if (StringUtilC.isEmpty(workUris[i])) {
				continue;
			}
			worksFilter += "<" + workUris[i] + ">";
		}
		String RDF_COUNT_FAMILYRULE_BYWORKS = this.nsPrefix
				+ "select count(distinct ?uri) as ?count  "
				+ "where{?uri a shl:FamilyRule; shl:code ?code .?uri bf:familyRuleFrom ?instance "
				+ ".filter not exists{?uri dct:isPartOf ?isPartOf} .?instance bf:instanceOf ?work"
				+ ". values ?work {" + worksFilter + "} "
				+ "}";
		ArrayList results = new ArrayList();
		results = SparqlExecution.vQuery(
				this.graph, RDF_COUNT_FAMILYRULE_BYWORKS, new String[] { "count" });
		Long count = Long.valueOf(Long.parseLong(RDFUtils
				.getValue(((Map) results.get(0)).get("count").toString())));
		
		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			String limit = "offset " + start + " limit "
					+ pagesize;
			
			String RDF_QUERY_FAMILYRULE_BYWORKS = this.nsPrefix
					+ "select distinct ?uri  "
					+ "where{?uri a shl:FamilyRule; shl:code ?code .?uri bf:familyRuleFrom ?instance "
					+ ".filter not exists{?uri dct:isPartOf ?isPartOf} .?instance bf:instanceOf ?work"
					+ ". values ?work {" + worksFilter + "} "
					+ "} order by ?code  " + limit;
	
			List<Map<String, String>> list = RDFUtils
					.transformListMap(SparqlExecution.vQuery(this.graph,
							RDF_QUERY_FAMILYRULE_BYWORKS, new String[] { "uri" }));
	
			List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
	
			for (Map<String, String> map : list) {
				detailList.add(getFamilyRuleInfo(map.get("uri"), false));
			}
			
			result.setResultList(detailList);
		}
		return result;
	}

	private Map<String, Object> getMap(ArrayList list,
			Map<String, String> renames, String[] listFields) {
		Map result = new HashMap();

		for (int i = 0; i < list.size(); i++) {
			String p = ((Map) list.get(i)).get("p").toString();
			p = p.substring(p.lastIndexOf("/") + 1);
			p = p.indexOf("#") != -1 ? p.substring(p.lastIndexOf("#") + 1) : p;

			String o = RDFUtils.getValue(((Map) list.get(i)).get("o")
					.toString());
			Object obj = result.get(p);
			if (obj == null) {
				Object value = o;
				if (listFields != null && Arrays.asList(listFields).contains(p)) {
					List sList = new ArrayList();
					sList.add(o);
					value = sList;
				}
				if (renames != null && renames.size() > 0) {
					for (int j = 0; j < renames.size(); j++) {
						for (Map.Entry entry : renames.entrySet()) {
							if (((String) entry.getKey()).equals(p)) {
								result.put(entry.getValue(), value);
								break;
							}
							result.put(p, value);
						}
					}
				} else {
					result.put(p, value);
				}
			} else if ((obj instanceof List)) {
				((List) obj).add(o);
			} else {
				List sList = new ArrayList();
				sList.add(obj.toString());
				sList.add(o);
				result.put(p, sList);
			}

		}

		return result;
	}
}
