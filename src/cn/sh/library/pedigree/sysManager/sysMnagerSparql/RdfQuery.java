package cn.sh.library.pedigree.sysManager.sysMnagerSparql;

/**
 * Created by yesonme on 15-3-19.
 */
public enum RdfQuery {
	RDF_SELECT_RESOURCE_TRIPLES(Namespace.getNsPrefixString() + "SELECT ?p ?o "
			+ "WHERE {" + "   <%1$s> ?p ?o ." + "}"),

	RDF_SELECT_TEMPORAL_4DYNASTY(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?s " + "WHERE {" + "   ?s a shl:Temporal ; "
			+ "      shl:dynasty '%1$s' . " + "}"),

	RDF_SELECT_TEMPORAL_4TIME(
			Namespace.getNsPrefixString()
					+ "SELECT DISTINCT ?uri ?begin ?end (GROUP_CONCAT(DISTINCT ?l; separator=';') AS ?label) (GROUP_CONCAT(DISTINCT ?m; separator=';') AS ?monarch) "
					+ "(GROUP_CONCAT(DISTINCT ?mn; separator=';') AS ?monarchName) (GROUP_CONCAT(DISTINCT ?dynasty; separator=';') AS ?dynasty) (GROUP_CONCAT(DISTINCT ?reignTitle; separator=';') AS ?reignTitle)"

					+ "WHERE {" + "   ?uri a shl:Temporal . "
					+ "OPTIONAL {?uri bf:label ?l . } "
					+ "OPTIONAL {?uri shl:dynasty ?dynasty . } "
					+ "OPTIONAL {?uri shl:beginYear ?begin . } "
					+ "OPTIONAL {?uri shl:endYear ?end . } "
					+ "OPTIONAL {?uri shl:monarch ?m .}"
					+ "OPTIONAL {?uri shl:reignTitle ?reignTitle .}"
					+ "OPTIONAL {?uri shl:monarchName ?mn .}"
					+ "FILTER ((?begin <= %2$s) && (?end >= %1$s))"
					+ "} ORDER BY ASC(?begin) ASC(STRLEN(?l))"),

	RDF_SELECT_TEMPORAL_4YEAR_PARENT(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?label " + "WHERE {" + "   ?s a shl:Temporal ; "
			+ "      bf:label ?label ; " + "      shl:beginYear ?begin ; "
			+ "      shl:endYear ?end . "
			+ "FILTER ((?begin <= %1$s) && (?end >= %1$s)) "
			+ "FILTER NOT EXISTS{?s time:intervalDuring ?parent .}" + "}"),

	RDF_SELECT_TEMPORAL_4YEAR(
			Namespace.getNsPrefixString()
					+ "SELECT DISTINCT ?uri ?begin ?end (GROUP_CONCAT(DISTINCT ?l; separator=';') AS ?label) (GROUP_CONCAT(DISTINCT ?m; separator=';') AS ?monarch) "
					+ "(GROUP_CONCAT(DISTINCT ?mn; separator=';') AS ?monarchName) (GROUP_CONCAT(DISTINCT ?dynasty; separator=';') AS ?dynasty) (GROUP_CONCAT(DISTINCT ?reignTitle; separator=';') AS ?reignTitle)"
					+ "WHERE {" + "   ?uri a shl:Temporal . "
					+ "OPTIONAL {?uri bf:label ?l . } "
					+ "OPTIONAL {?uri shl:dynasty ?dynasty . } "
					+ "OPTIONAL {?uri shl:beginYear ?begin . } "
					+ "OPTIONAL {?uri shl:endYear ?end . } "
					+ "OPTIONAL {?uri shl:monarch ?m .}"
					+ "OPTIONAL {?uri shl:reignTitle ?reignTitle .}"
					+ "OPTIONAL {?uri shl:monarchName ?mn .}"
					+ "FILTER ((?begin <= %1$s) && (?end >= %1$s))"
					+ "} ORDER BY ASC(?begin) ASC(STRLEN(?l))"),

	RDF_SELECT_TEMPORAL(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?uri ?label ?begin ?end " + "WHERE {"
			+ "   ?uri a shl:Temporal ; " + "        bf:label ?label ;"
			+ "        shl:beginYear ?begin . "
			+ "FILTER NOT EXISTS{?uri shl:monarchName ?t .}"
			+ "} ORDER BY ASC(?begin)"),

	RDF_SELECT_TEMPORAL_FREE(
			Namespace.getNsPrefixString()
					+ "SELECT DISTINCT ?uri ?begin ?end (GROUP_CONCAT(DISTINCT ?l; separator=';') AS ?label) (GROUP_CONCAT(DISTINCT ?m; separator=';') AS ?monarch) "
					+ "(GROUP_CONCAT(DISTINCT ?mn; separator=';') AS ?monarchName) (GROUP_CONCAT(DISTINCT ?dynasty; separator=';') AS ?dynasty) (GROUP_CONCAT(DISTINCT ?reignTitle; separator=';') AS ?reignTitle)"
					+ "WHERE {"
					+ "   ?uri a shl:Temporal . "
					+ "OPTIONAL {?uri bf:label ?l . } "
					+ "OPTIONAL {?uri shl:dynasty ?dynasty . } "
					+ "OPTIONAL {?uri shl:beginYear ?begin . } "
					+ "OPTIONAL {?uri shl:endYear ?end . } "
					+ "OPTIONAL {?uri shl:monarch ?m .}"
					+ "OPTIONAL {?uri shl:reignTitle ?reignTitle .}"
					+ "OPTIONAL {?uri shl:monarchName ?mn .}"
					+ "FILTER (CONTAINS(?dynasty,'%1$s') || CONTAINS(?m,'%1$s') || CONTAINS(?l, '%1$s') || CONTAINS(?mn,'%1$s') || CONTAINS(?reignTitle,'%1$s'))"
					+ "} ORDER BY ASC(?begin) ASC(STRLEN(?l))"),

	// 获取朝代RDF
	RDF_CONSTRUCT_TEMPORAL(Namespace.getNsPrefixString() + "CONSTRUCT {"
			+ "   <%1$s> ?p ?o ." + "}" + "WHERE {" + "   <%1$s> ?p ?o ." + "}"),

	RDF_SELECT_TEMPORAL_SUBS(
			Namespace.getNsPrefixString()
					+ "SELECT DISTINCT ?uri ?begin ?end (GROUP_CONCAT(DISTINCT ?l; separator=';') AS ?label) (GROUP_CONCAT(DISTINCT ?m; separator=';') AS ?monarch) "
					+ "(GROUP_CONCAT(DISTINCT ?mn; separator=';') AS ?monarchName) (GROUP_CONCAT(DISTINCT ?dynasty; separator=';') AS ?dynasty) (GROUP_CONCAT(DISTINCT ?reignTitle; separator=';') AS ?reignTitle)"
					+ "WHERE {" + "   ?uri a shl:Temporal ; "
					+ "        time:intervalDuring*/bf:label '%1$s' . "
					+ "OPTIONAL {?uri bf:label ?l . } "
					+ "OPTIONAL {?uri shl:dynasty ?dynasty . } "
					+ "OPTIONAL {?uri shl:beginYear ?begin . } "
					+ "OPTIONAL {?uri shl:endYear ?end . } "
					+ "OPTIONAL {?uri shl:monarch ?m .}"
					+ "OPTIONAL {?uri shl:reignTitle ?reignTitle .}"
					+ "OPTIONAL {?uri shl:monarchName ?mn .}"
					+ "} ORDER BY ASC(?begin) ASC(STRLEN(?l))"),

	RDF_SELECT_TEMPORAL_URI(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?s  " + "WHERE {" + "   ?s a shl:Temporal ; "
			+ "      shl:dynasty '%1$s' . "
			+ "   FILTER NOT EXISTS{?s time:intervalDuring ?t.}" + "}"),

	RDF_SELECT_TEMPORAL_TIME(Namespace.getNsPrefixString()
			+ "SELECT ?s ?begin ?end " + "WHERE {" + "   ?s a shl:Temporal ; "
			+ "      bf:label '%1$s' . "
			+ "OPTIONAL {?s shl:beginYear ?begin . }"
			+ "OPTIONAL {?s shl:endYear ?end . }" + "}"),

	RDF_SELECT_TEMPORAL_VALUE(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?v  " + "WHERE {" + "   ?s a shl:Temporal ; "
			+ "      shl:dynasty '%1$s' ; " + "      ?p ?v . "
			+ "   FILTER STRENDS(STR(?p), '%2$s') " + "}"),

	RDF_SELECT_PLACES(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?uri ?prov ?city ?county " + "WHERE { "
			+ "   ?uri a shl:Place ; " + "        shl:province ?prov . "
			+ "OPTIONAL {" + "   ?uri shl:city ?city . " + "}" + "OPTIONAL {"
			+ "   ?uri shl:county ?county . " + "}"
			+ "} ORDER BY ASC(?prov) ASC(?city) ASC(?county)"),

	RDF_SELECT_PLACES_FOREIGN(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?uri ?label ?country " + "WHERE {"
			+ "   ?uri a shl:Place ; " + "        bf:label ?label ; "
			+ "        shl:country ?country ."
			+ "FILTER NOT EXISTS{?uri shl:province ?prov .} "
			+ "FILTER (lang(?label) = 'chs')"
			+ "FILTER (lang(?country) = 'chs')" + "FILTER (?label != '全国'@chs)"
			+ "}"),

	RDF_SELECT_ORG_PROVS(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?prov " + "WHERE {"
			+ "   ?uri a shl:Organization ; " + "        shl:region ?place . "
			+ "{SELECT ?place ?prov ?same FROM <" + Constant.GRAPH_PLACE
			+ "> WHERE {" + "   ?place a shl:Place ; "
			+ "          owl:sameAs ?same ; "
			+ "          shl:province ?prov . " + "}}"
			+ "} ORDER BY ASC(STR(?same))"),

	RDF_SELECT_ORGS_FREE(
			Namespace.getNsPrefixString()
					+ "SELECT DISTINCT ?uri ?full ?short ?en ?addr "
					+ "WHERE {"
					+ "   ?uri a shl:Organization ; "
					+ "        bf:label ?full ; "
					+ "        shl:abbreviateName ?short ; "
					+ "        bf:label ?en ; "
					+ "        shl:address ?addr ; "
					+ "        bf:label ?label . "
					+ "FILTER (lang(?full) = 'chs')"
					+ "FILTER (lang(?short) = 'chs')"
					+ "FILTER (lang(?en) = 'en')"
					+ "FILTER (CONTAINS(?label, '%1$s') || CONTAINS(?addr, '%1$s') || CONTAINS(?short, '%1$s'))"
					+ "} ORDER BY ASC(?en)"),

	RDF_SELECT_OTHER_ORGS(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?uri ?full ?short ?en ?addr " + "WHERE {"
			+ "   ?uri a shl:Organization ; " + "        bf:label ?full ; "
			+ "        shl:abbreviateName ?short ; "
			+ "        bf:label ?en ; " + "        shl:address ?addr ; "
			+ "        shl:region ?place . " + "FILTER (lang(?full) = 'chs')"
			+ "FILTER (lang(?short) = 'chs')" + "FILTER (lang(?en) = 'en')"
			+ "{SELECT ?place FROM <" + Constant.GRAPH_PLACE + "> WHERE {"
			+ "   ?place a shl:Place ; " + "          shl:country ?country . "
			+ "FILTER (?country != '中国'@chs)" + "FILTER (?country != '中國'@cht)"
			+ "}}" + "} ORDER BY ASC(?en)"),

	RDF_SELECT_PROV_ORGS(Namespace.getNsPrefixString()
			+ "SELECT DISTINCT ?uri ?full ?short ?en ?addr " + "WHERE {"
			+ "   ?uri a shl:Organization ; " + "        bf:label ?full ; "
			+ "        shl:abbreviateName ?short ; "
			+ "        bf:label ?en ; " + "        shl:address ?addr ; "
			+ "        shl:region ?place . " + "FILTER (lang(?full) = 'chs')"
			+ "FILTER (lang(?short) = 'chs')" + "FILTER (lang(?en) = 'en')"
			+ "{SELECT ?place FROM <" + Constant.GRAPH_PLACE + "> WHERE {"
			+ "   ?place a shl:Place ; " + "          shl:province '%1$s' . "
			+ "}}" + "} ORDER BY ASC(?en)"),

	PERSON_PXT_SELECT(
			Namespace.getNsPrefixString()
					+ "SELECT ?uri ?name ?father ?fatherName (GROUP_CONCAT(?wifeName; separator=';') AS ?wifeNames) ?sonUris ?sonNames where {?uri a shl:Person;"
					+ "bf:label?name "
					+ "OPTIONAL{ "
					+ "?uri rel:childOf ?father.{ "
					+ " SELECT DISTINCT ?father ?fatherName FROM <http://gen.library.sh.cn/graph/person> WHERE{ "
					+ "    ?father bf:label ?fatherName.FILTER(lang(?fatherName)='chs') "
					+ "} "
					+ "} "
					+ "} "
					+ "OPTIONAL{ "
					+ "   ?uri rel:spouseOf ?wife.{ "
					+ "    SELECT DISTINCT ?wife ?wifeName FROM <http://gen.library.sh.cn/graph/person> WHERE{ "
					+ "        ?wife bf:label ?wifeName .FILTER(lang(?wifeName )='chs') "
					+ "    } "
					+ " } "
					+ " } "
					+ "OPTIONAL{select (GROUP_CONCAT(?sonUri; separator=';') AS ?sonUris)(GROUP_CONCAT(?sonName; separator=';') AS ?sonNames) where {?sonUri a shl:Person ;rel:childOf ?pOf;bf:label?sonName.Filter(?pOf=<%1$s>) "
					+ ".FILTER(lang(?sonName)='chs')}} "
					+ ".Filter(?uri=<%1$s>) "
					+ ".FILTER(lang(?name)='chs')} " + "");
	protected String _query;

	RdfQuery(String query) {
		this._query = query;
	}

	@Override
	public String toString() {
		return this._query;
	}

	/**
	 * Query with formatting
	 * 
	 * @param args
	 * @return
	 */
	public String toString(Object... args) {
		return String.format(this._query, args);
	}
}
