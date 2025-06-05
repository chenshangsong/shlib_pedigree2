package cn.sh.library.pedigree.webApi.sparql.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.webApi.sparql.ApiPersonSparql;
import cn.sh.library.pedigree.webApi.sparql.Namespace;

@Repository
@GraphDefine(name = "http://gen.library.sh.cn/graph/person")
public class ApiPersonSparqlImpl extends BaseDaoImpl implements ApiPersonSparql {

//	@Resource
//	private StringBuffer nsPrefix;

	@Override
	public List<Map<String, String>> getPlacesInfos4Person(String prov) {
		String sql = Namespace.getNsPrefixString()
				+ "SELECT (?f as ?furi) ?proName (COUNT(DISTINCT ?s) AS ?count) FROM <http://gen.library.sh.cn/graph/person>  WHERE {?s a shl:Person ;       bf:label ?name ; shl:relatedWork ?work ;       foaf:familyName ?f .    FILTER (lang(?name)='cht')  { ?s shl:relatedWork ?work . {SELECT ?work ?proName FROM <http://gen.library.sh.cn/graph/work>WHERE{?work shl:place ?place .   {SELECT ?place ?proName FROM <http://gen.library.sh.cn/graph/place>    WHERE {       ?place shl:province ?proName . FILTER (STR(?proName)='"
				+ prov + "')   }}}}}} order by DESC(?count)";
		List<Map<String, String>> list = RDFUtils
				.transformListMap(SparqlExecution.vQuery(this.graph, sql, new String[] { "furi", "proName", "count" }));
		return list;
	}

	@Override
	public List<Map<String, String>> getProvList() {
		String sql = Namespace.getNsPrefixString()
				+ "select distinct ?pro from <http://gen.library.sh.cn/graph/place>where{?s a shl:Place; shl:province ?pro}";
		List<Map<String, String>> list = RDFUtils
				.transformListMap(SparqlExecution.vQuery(this.graph, sql, new String[] { "pro" }));
		return list;
	}

	@Override
	public List<Map<String, String>> getPersonsFromFamilyRules() {
		String RDF_QUERY_PERSONS_FROM_FAMILY_RULES = Namespace.getNsPrefixString()
				+ " select distinct (?person as ?uri)  (str(?label) as ?label) (?familyName as ?familyNameUri) "
				+ " (str(?familyNameCht) as ?familyName) (GROUP_CONCAT(str(?work); separator=',') AS ?releatedWorks) "
				+ "  from <http://gen.library.sh.cn/graph/baseinfo> " + " from <http://gen.library.sh.cn/graph/work>   "
				+ " from <http://gen.library.sh.cn/graph/instance> " + " from <http://gen.library.sh.cn/graph/person> "
				+ " where{ "
				+ " ?person a shl:Person; bf:label ?label;foaf:familyName ?familyName; shl:relatedWork ?work ;shl:roleOfFamily ?role "
				+ " .?familyName bf:label ?familyNameCht "
				+ " .values ?role {<http://data.library.sh.cn/jp/vocab/ancestor/shi-zu><http://data.library.sh.cn/jp/vocab/ancestor/shi-qian-zu><http://data.library.sh.cn/jp/vocab/ancestor/ming-ren>} "
				+ " . filter (lang(?label) = 'cht') " + " . filter (lang(?familyNameCht) = 'cht') "
				+ " .filter exists{?uri a shl:FamilyRule ; bf:familyRuleFrom/bf:instanceOf ?work . } " + " } ";

		List<Map<String, String>> list = RDFUtils
				.transformListMap(SparqlExecution.vQuery(this.graph, RDF_QUERY_PERSONS_FROM_FAMILY_RULES,
						new String[] { "uri", "label", "familyNameUri", "familyName", "releatedWorks" }));

		return list;
	}

	@Override
	public List<Map<String, String>> getProvsFromFamilyRules() {
		String RDF_QUERY_PROVS_FROM_FAMILY_RULES = Namespace.getNsPrefixString()
				+ " select distinct ?province (GROUP_CONCAT(distinct str(?work); separator=',') AS ?releatedWorks) "
				+ "  from <http://gen.library.sh.cn/graph/baseinfo> " + " from <http://gen.library.sh.cn/graph/work>   "
				+ " from <http://gen.library.sh.cn/graph/instance> " + " from <http://gen.library.sh.cn/graph/place> "
				+ " where{ " + " ?work a bf:Work; shl:place/shl:province ?province "
				+ " .?uri a shl:FamilyRule ; bf:familyRuleFrom/bf:instanceOf ?work "
				+ ".filter not exists{?uri dct:isPartOf ?isPartOf} " + " } ";

		List<Map<String, String>> list = RDFUtils.transformListMap(SparqlExecution.vQuery(this.graph,
				RDF_QUERY_PROVS_FROM_FAMILY_RULES, new String[] { "province", "releatedWorks" }));

		return list;
	}
}
