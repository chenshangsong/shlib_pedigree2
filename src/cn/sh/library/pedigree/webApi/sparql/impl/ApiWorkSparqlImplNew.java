package cn.sh.library.pedigree.webApi.sparql.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.common.RoleGroup;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.dto.AppConfig;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.framework.util.PreloadApiFuriPlaceList;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.dto.searchBean.ApiWorkSearchBean;
import cn.sh.library.pedigree.webApi.sparql.ApiMergeSearchParts;
import cn.sh.library.pedigree.webApi.sparql.ApiWorkSparqlNew;
import cn.sh.library.pedigree.webApi.sparql.Namespace;
import virtuoso.jena.driver.VirtGraph;

@Repository
@GraphDefine(name = "http://gen.library.sh.cn/graph/work11111")
public class ApiWorkSparqlImplNew extends BaseDaoImpl implements ApiWorkSparqlNew {

//	@Resource
//	private StringBuffer nsPrefix;

	@Resource
	private PlaceSparql placeSparql;

	@Resource
	private AppConfig appConfig;

	@Resource
	private BaseinfoSparql baseinfoSparql;

	
	public QueryResult<Map<String, Object>> getWorks(ApiWorkSearchBean bean, int start, int size) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String sql = "";
		if (!StringUtils.isBlank(bean.getFacetPlaceAllUri())) {
			/*
			 * List<Map<String, String>> stanplaces = RDFUtils
			 * .transformListMap(placeSparql.getStandPlace(bean .getFacetPlaceAllUri()));
			 */
			/*
			 * if (stanplaces != null && stanplaces.size() > 0) {
			 * bean.setStandardPlace(stanplaces.get(0)); }
			 */
			Map<String, String> stanplace = (Map<String, String>) PreloadApiFuriPlaceList.getInstance()
					.getRemotePlacesAll().get(bean.getFacetPlaceAllUri());
			if (stanplace != null) {
				bean.setStandardPlace(stanplace);
			}

		}

		String clause = ApiMergeSearchParts.workSearchClause(bean);
		String orderByColumn = "";
		// 按撰修时间倒叙排序
		String zxsjsql = "";
		// 默认按标题降序排序
		if (StringUtilC.isEmpty(bean.getOrderType())) {
			bean.setOrderType("0");
		}
		// 按标题字符降序排列
		if ("0".equals(bean.getOrderType())) {
			orderByColumn = " ORDER BY DESC (?dtitle) ";
		} else if ("1".equals(bean.getOrderType())) {
			zxsjsql = " optional{SELECT ?work ?s  ?temporal FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporalValue ?temporal . FILTER (datatype(?temporal) = xsd:integer) }}";
			orderByColumn = " ORDER BY DESC(?temporal) ";
		}
		// 如果简单检索字段不为空，则进行简单检索
		if (!StringUtilC.isEmpty(bean.getFreeText())) {
			sql = "WHERE { {   ?work a bf:Work ; dc:title ?dtitle" + zxsjsql + ". ?dtitle bif:contains '\""
					+ bean.getFreeText() + "\"'" + "} UNION {" + "   ?work bf:subject ?th . " + "   {SELECT ?th FROM <"
					+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE { "
					+ "       ?th bf:label ?slabel . ?slabel bif:contains '\"" + bean.getFreeText() + "\"'" + "   }}"
					+ " UNION {?work a bf:Work .{SELECT ?work FROM <http://gen.library.sh.cn/graph/person> WHERE {  ?uri shl:relatedWork ?work ; bf:label ?clabel .?clabel  bif:contains '\""
					+ bean.getFreeText() + "\"'" + "}}}" + "} " + clause + "}";
		}
		// 高级检索
		else {

			sql = "WHERE {    ?work a bf:Work ; dc:title ?dtitle " + zxsjsql + clause + "}";
		}
		String countQuery = Namespace.getNsPrefixString() + "SELECT (COUNT(DISTINCT ?work) AS ?count) " + sql;
		String workQuery = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work " + sql + " OFFSET " + start + " LIMIT " + size;
		Long count = 0L;
		List<Map<String, String>> _listCount = RDFUtils
				.transformListMap(SparqlExecution.vQuery(_set, countQuery, new String[] { "count" }));
		count = StringUtilC.getLong(_listCount.get(0).get("count"));
		QueryResult result = new QueryResult();
		result.setTotalrecord(count);
		if (count.longValue() > 0L) {
			ArrayList works = SparqlExecution.vQuery(_set, workQuery, new String[] { "work" });
			String uris = "";
			for (int i = 0; i < works.size(); i++) {
				uris += "<" + ((Map) works.get(i)).get("work").toString() + ">";
			}
			/*--------------------可以删除 chenss 20180828 begin-----------------------------*/
			String fulllinkCalse = "";
			// 如果不是管理员
			if (!RoleGroup.admin.getGroup().equals(CommonUtils.loginUser.getRoleId())) {
				// fulllinkCalse =
				// ".FILTER(?access='0'||?access='1')";chenss20180424
				fulllinkCalse = ".FILTER (?access!='9')";
			} // 如果查看只有全文的
			if (!StringUtilC.isEmpty(bean.getAccFlg())) {
				fulllinkCalse = ".FILTER(?access='0')";
				// 如果是1，则只查询非全文数据
				if ("1".equals(bean.getAccFlg())) {
					fulllinkCalse = ".FILTER(?access='1')";
				}
				// 如果是-1，则只查询为空数据
				else if ("-1".equals(bean.getAccFlg())) {
					fulllinkCalse = ".filter (str(?access)='-1')";
				}
			}
			/*--------------------可以删除 chenss 20180828 end-----------------------------*/
			String query = Namespace.getNsPrefixString() + "select * from <http://gen.library.sh.cn/graph/work>"
					+ "from <http://gen.library.sh.cn/graph/instance> " + "from <http://gen.library.sh.cn/graph/item> "
					+ "from <http://gen.library.sh.cn/graph/baseinfo> " + "from <http://gen.library.sh.cn/graph/place> "
					+ "where{" + "SELECT DISTINCT ?work ?dtitle ?title "
					+ "(GROUP_CONCAT(DISTINCT  ?access;separator=';') AS  ?access)"
					+ "(GROUP_CONCAT(DISTINCT  ?doi;separator=';') AS  ?doi)"
					+ "(GROUP_CONCAT(DISTINCT  ?hasimg;separator=';') AS  ?hasimg)"
					+ " ?ps ?place ?long ?lat ?ins ?temporal" + "(GROUP_CONCAT(DISTINCT ?desc;separator=';') AS ?desc)"
					+ "(GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles) "
					+ "(GROUP_CONCAT(DISTINCT ?creator;separator=';') AS ?creators)"
					+ "(GROUP_CONCAT(DISTINCT ?label;separator=';') AS ?label)"
					+ "(GROUP_CONCAT(DISTINCT ?xing;separator=';') AS ?xing)"
					+ "WHERE {   ?work a bf:Work ;          dc:title ?dtitle .    FILTER (lang(?dtitle) = 'chs')"
					+ "OPTIONAL {?work bf:title ?ts .    {SELECT ?ts ?title FROM <http://gen.library.sh.cn/graph/title> WHERE {       ?ts a bf:WorkTitle; bf:label ?title . FILTER (lang(?title)='chs')   }}} "
					+ "OPTIONAL {?work bf:creator ?cs .        {SELECT ?cs ?creator FROM <http://gen.library.sh.cn/graph/person> WHERE {           ?cs bf:label ?creator . FILTER (lang(?creator) = 'chs')       }}   }  "
					+ "OPTIONAL {?work shl:description ?desc .} "
					+ "OPTIONAL {?work bf:subject ?th .        {SELECT ?th ?label FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {            ?th a shl:TitleOfAncestralTemple ; bf:label ?label . FILTER (lang(?label) = 'chs')        }}   } "
					+ "OPTIONAL {?work bf:subject ?fn .        {SELECT ?fn ?xing FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {            ?fn a shl:FamilyName ; bf:label ?xing . FILTER (lang(?xing) = 'chs')        }}   } "
					+ "OPTIONAL {SELECT ?work ?access ?doi ?ins ?item ?hasimg WHERE { ?item a bf:Item optional{?item shl:hasFullImg ?hasimg} {?item shl:accessLevel ?access} OPTIONAL{?item shl:DOI ?doi} {?item bf:itemOf ?ins.  {SELECT ?ins ?work ?temporal  WHERE {?ins a bf:Instance ;bf:instanceOf ?work      }}}"
					+ fulllinkCalse + "}}"
					+ "OPTIONAL {SELECT ?work ?s  ?temporal FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporalValue ?temporal . FILTER (datatype(?temporal) = xsd:integer) }}"
					+ "OPTIONAL {?work shl:place ?ps.{SELECT ?ps ?place ?long ?lat FROM <http://gen.library.sh.cn/graph/place> WHERE { ?ps bf:label ?place ; owl:sameAs ?same .FILTER (lang(?place)='chs'){SELECT DISTINCT ?same ?long ?lat FROM <http://www.cba.ac.cn/graph/geography> WHERE {?same geo:long ?long ; geo:lat ?lat . }}}}}"
					+ ".VALUES ?work{" + uris + "} }" + orderByColumn + "}";
			ArrayList _list = SparqlExecution.vQuery(_set, query,
					new String[] { "work", "dtitle", "title", "subtitles", "desc", "creators", "ps", "place", "long",
							"lat", "temporal", "ins", "label", "xing", "doi", "access", "hasimg" });
			result.setResultList(_list);
		}
		return result;
	}

	public ArrayList getTitles(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String query = Namespace.getNsPrefixString() + "SELECT ?title ?type " + "WHERE {" + "   <" + work_uri + "> bf:workTitle ?t ."
				+ "   ?t bf:titleValue ?title ; " + "      bf:titleType ?type . " + "FILTER (?type != '0')" + "}";

		return SparqlExecution.vQuery(_set, query, new String[] { "title", "type" });
	}

	public ArrayList getCreator(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String query = Namespace.getNsPrefixString() + "SELECT ?name " + "WHERE {" + "   <" + work_uri + "> dc:creator ?name . " + "}";

		return SparqlExecution.vQuery(_set, query, new String[] { "name" });
	}

	public ArrayList getWorksInLatLong(String place_str) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String query = Namespace.getNsPrefixString() + "";

		if (place_str.startsWith("http://")) {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {   ?work a bf:Work; bf:title ?ts ;          shl:place <"
					+ place_str + "> . " + "{SELECT ?ts ?title FROM <" + "http://gen.library.sh.cn/graph/title"
					+ "> WHERE {?ts a bf:WorkTitle ; bf:label ?title . FILTER (lang(?title)='chs')}}" + "} LIMIT 20";
		} else {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {    ?work a bf:Work; bf:title ?ts ;          shl:place ?place . {SELECT ?ts ?title FROM <http://gen.library.sh.cn/graph/title> WHERE {?ts a bf:WorkTitle ; bf:label ?title . FILTER (lang(?title)='chs')}}{   SELECT ?place FROM <http://gen.library.sh.cn/graph/place>    WHERE {        ?place ?p '"
					+ place_str + "' ." + "   }}" + "} LIMIT 20";
		}

		ArrayList results = SparqlExecution.vQuery(_set, query, new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points + this.placeSparql.getLongLat(RDFUtils.toString(place.get("place"))) + ";";
			}

			if (points.endsWith(";")) {
				points = StringUtils.substringBeforeLast(points, ";");
			}

			((Map) results.get(i)).put("points", points);
		}

		return results;
	}

	@Override
	public ArrayList getWorksInPlace(Map standPlace, String familyName, String freetext, String startYear,
			String endYear) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
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

		if (StringUtils.isNotBlank(familyName)) {
			Map xing = this.baseinfoSparql.getXings(familyName);

			if (xing != null && xing.size() > 0) {
				String uri = xing.get("uri").toString();
				clause = "FILTER (?subject = <" + uri + ">) ";
			}
		}

		if (StringUtils.isNotBlank(standPlace.get("country").toString())) {
			clause = clause + "?place shl:country '" + standPlace.get("country").toString() + "'@cht .";
		}

		if (StringUtils.isNotBlank(standPlace.get("prov").toString())) {
			clause = clause + "?place shl:province '" + standPlace.get("prov").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("city").toString())) {
			clause = clause + "?place shl:city '" + standPlace.get("city").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("county").toString())) {
			clause = clause + "?place shl:county '" + standPlace.get("county").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("label").toString())) {
			clause = clause + "?place bf:label '" + standPlace.get("label").toString() + "'@cht .";
		}

		String query = Namespace.getNsPrefixString()
				+ " SELECT DISTINCT ?work (if (STRLEN(STR(?title )) > 0, ?title, ?dtitle) AS ?title)"
				+ " FROM <http://gen.library.sh.cn/graph/person>" + " FROM <http://gen.library.sh.cn/graph/baseinfo>"
				+ " FROM <http://www.cba.ac.cn/graph/geography>" + " FROM <http://gen.library.sh.cn/graph/work>"
				+ " FROM <http://gen.library.sh.cn/graph/instance>" + " FROM <http://gen.library.sh.cn/graph/title>"
				+ "WHERE {" + "   ?work a bf:Work; dc:title ?dtitle ; " + "         bf:subject ?subject ; "
				+ "         shl:place ?place . "
				+ "   OPTIONAL{?work bf:title ?ts.?ts a bf:WorkTitle ; bf:label ?title . }"
				+ "   FILTER (lang(?title)='chs') " + "}}" + clause + "FILTER (lang(?dtitle)='chs')} LIMIT 20";

		ArrayList results = SparqlExecution.vQuery(_set, query, new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points + this.placeSparql.getLongLat(RDFUtils.toString(place.get("place"))) + ";";
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
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		if (StringUtils.isNotBlank(familyName)) {
			Map xing = this.baseinfoSparql.getXings(familyName);

			if (xing.size() > 0) {
				String uri = xing.get("uri").toString();
				filter = "FILTER (?subject = <" + uri + ">) ";
			}
		}

		String query = Namespace.getNsPrefixString()
				+ "SELECT DISTINCT ?work (if (STRLEN(STR(?title )) > 0, ?title, ?dtitle) AS ?title) " + "WHERE { "
				+ "   ?work a bf:Work; dc:title ?dtitle;bf:title ?ts ; " + "         bf:subject ?subject ; "
				+ "         shl:place ?place . " + "optional{SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title" + "> WHERE {" + "   ?ts a bf:WorkTitle ; bf:label ?title . "
				+ "   FILTER (lang(?title)='chs') " + "}}" + filter + "{SELECT ?place FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> " + "   WHERE { " + "       ?place a shl:Place . ";

		if (StringUtils.isNotBlank(standPlace.get("country").toString())) {
			query = query + "?place shl:country '" + standPlace.get("country").toString() + "'@cht .";
		}

		if (StringUtils.isNotBlank(standPlace.get("prov").toString())) {
			query = query + "?place shl:province '" + standPlace.get("prov").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("city").toString())) {
			query = query + "?place shl:city '" + standPlace.get("city").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("county").toString())) {
			query = query + "?place shl:county '" + standPlace.get("county").toString() + "' .";
		}

		if (StringUtils.isNotBlank(standPlace.get("label").toString())) {
			query = query + "?place bf:label '" + standPlace.get("label").toString() + "'@cht .";
		}

		query = query + "}}.FILTER (lang(?dtitle)='chs')} LIMIT 20";

		ArrayList results = SparqlExecution.vQuery(_set, query, new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points + this.placeSparql.getLongLat(RDFUtils.toString(place.get("place"))) + ";";
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
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		if (place_str.startsWith("http://")) {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {   ?work a bf:Work; dc:title ?title ;          bf:place <"
					+ place_str + "> . " + "FILTER CONTAINS(?title, '" + familyName + "')" + "} LIMIT 20";
		} else {
			query = query
					+ "SELECT DISTINCT ?work ?title WHERE {    ?work a bf:Work; dc:title ?title ;          bf:place ?place . {   SELECT ?place FROM <http://gen.library.sh.cn/graph/place>    WHERE {        ?place ?p '"
					+ place_str + "' ." + "   }" + "}" + "FILTER CONTAINS(?title, '" + familyName + "')" + "}";
		}

		ArrayList results = SparqlExecution.vQuery(_set, query, new String[] { "work", "title" });

		for (int i = 0; i < results.size(); i++) {
			String work_uri = ((Map) results.get(i)).get("work").toString();

			ArrayList places = this.placeSparql.getPlaces(work_uri);
			String points = "";

			for (int j = 0; j < places.size(); j++) {
				Map place = (Map) places.get(j);
				points = points + this.placeSparql.getLongLat(RDFUtils.toString(place.get("place"))) + ";";
			}

			if (points.endsWith(";")) {
				points = StringUtils.substringBeforeLast(points, ";");
			}

			((Map) results.get(i)).put("points", points);
		}

		return results;
	}

	public ArrayList getWorkPlaces(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String query = Namespace.getNsPrefixString() + "SELECT ?place ?label " + "WHERE {" + "   <" + work_uri + "> bf:place ?place . "
				+ "   {SELECT ?place ?label FROM <" + "http://gen.library.sh.cn/graph/place" + "> WHERE {"
				+ "       ?place a shl:Place; bf:label ?label ." + "   }}" + "}";

		return SparqlExecution.vQuery(_set, query, new String[] { "place", "label" });
	}

	public ArrayList getWorks4Person(String person_uri, boolean inference) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		inference = false;
		String filter = "FILTER ((STR(?uri) = '" + person_uri + "') ";

		if ((inference) && (this.appConfig.getPersonSameAs() != null)) {
			String str = "";
			String str1 = "";

			String query = Namespace.getNsPrefixString() + "SELECT DISTINCT ?uri FROM <" + "http://gen.library.sh.cn/graph/person"
					+ "> " + "WHERE {" + "   ?uri a shl:Person .";

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

			query = query + " FROM <http://gen.library.sh.cn/graph/person> WHERE {" + str1 + "FILTER (STR(?uri1) = '"
					+ person_uri + "')" + "}}" + "FILTER (STR(?uri) != '" + person_uri + "')" + "}";

			ArrayList results = SparqlExecution.vQuery(_set, query, new String[] { "uri" });

			if (results.size() > 0) {
				for (int i = 0; i < results.size(); i++) {
					filter = filter + "|| (STR(?uri) = '" + ((Map) results.get(i)).get("uri").toString() + "') ";
				}
			}

		}

		filter = filter + ") ";
		String fulllinkCalse = "";
		// 如果不是管理员
		if (!RoleGroup.admin.getGroup().equals(CommonUtils.loginUser.getRoleId())) {
			// fulllinkCalse =
			// ".FILTER(?accessLevel='0'||?accessLevel='1')";chenss20180424
			fulllinkCalse = ".FILTER (?accessLevel!='9')";
		}
		String query = Namespace.getNsPrefixString() + "SELECT ?work "
				+ "(GROUP_CONCAT(DISTINCT  ?accessLevel;separator=';') AS  ?accessLevel)"
				+ "(GROUP_CONCAT(DISTINCT  ?doi;separator=';') AS  ?doi)"
				+ "(GROUP_CONCAT(DISTINCT  ?hasimg;separator=';') AS  ?hasimg)"
				+ "(GROUP_CONCAT(DISTINCT ?cr;separator=';') AS ?creator) ?dtitle ?title (GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles)"
				+ "(GROUP_CONCAT(DISTINCT ?note;separator=';') AS ?note)  ?roles FROM <"
				+ "http://gen.library.sh.cn/graph/work" + "> " + "WHERE{" + "   ?work a bf:Work; dc:title ?dtitle . "
				+ "   FILTER (lang(?dtitle) = 'chs')" + "   OPTIONAL {?work shl:description ?note .} "
				+ "OPTIONAL {?work bf:title ?ts . " + "   {SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "       ?ts a bf:WorkTitle; bf:label ?title . FILTER (lang(?title)='chs')" + "   }}" + "}"
				+ "OPTIONAL {?work bf:title ?sts . " + "   {SELECT ?sts ?t FROM <"
				+ "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "       ?sts a bf:VariantTitle; bf:label ?t . FILTER (lang(?t)='chs')" + "   }}" + "}"
				+ "OPTIONAL {SELECT ?work ?accessLevel ?doi ?ins ?item ?hasimg WHERE { ?item a bf:Item optional{?item shl:hasFullImg ?hasimg} {?item shl:accessLevel ?accessLevel} OPTIONAL{?item shl:DOI ?doi} {?item bf:itemOf ?ins.  {SELECT ?ins ?work ?temporal  WHERE {?ins a bf:Instance ;bf:instanceOf ?work      }}}"
				+ fulllinkCalse + "}}" + "OPTIONAL {?work bf:creator ?cs ." + "   {SELECT ?cs ?cr FROM <"
				+ "http://gen.library.sh.cn/graph/person" + "> WHERE {" + "       ?cs bf:label ?cr . "
				+ "   FILTER (lang(?cr)='chs')" + "   }}" + "}"
				+ "   {SELECT ?work (GROUP_CONCAT(?role; separator=';') AS ?roles) FROM <"
				+ "http://gen.library.sh.cn/graph/person" + "> WHERE{" + "       <" + person_uri
				+ "> shl:relatedWork ?work . " + "   OPTIONAL {<" + person_uri + "> shl:roleOfFamily ?r . "
				+ "   {SELECT ?r ?role FROM <" + "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "       ?r bf:label ?role . " + "   FILTER (lang(?role)='chs')" + "   }}" + "}" + "   }}" + "}";

		return SparqlExecution.vQuery(_set, query, new String[] { "work", "creator", "dtitle", "title",
				"subtitles", "note", "roles", "doi", "accessLevel", "hasimg" });
	}

	public Map getWorkInfos(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String fulllinkCalse = "";
		// 如果不是管理员
		if (!RoleGroup.admin.getGroup().equals(CommonUtils.loginUser.getRoleId())) {
			// fulllinkCalse =
			// ".FILTER(?access='0'||?access='1')";chenss20180424
			fulllinkCalse = ".FILTER (?access!='9')";
		}
		String query = Namespace.getNsPrefixString() + "SELECT ?dtitle ?title "
				+ "(GROUP_CONCAT(DISTINCT ?access;separator=';') AS  ?access)"
				+ "(GROUP_CONCAT(DISTINCT ?doi;separator=';') AS  ?doi)"
				+ "(GROUP_CONCAT(DISTINCT ?hasimg;separator=';') AS  ?hasimg)"
				+ "(GROUP_CONCAT(DISTINCT ?tv; separator=';') AS ?subtitle) "
				+ "(GROUP_CONCAT(DISTINCT ?creator; separator=';') AS ?creator)"
				+ "(GROUP_CONCAT(DISTINCT ?note; separator=';') AS ?note) "
				+ "(GROUP_CONCAT(DISTINCT ?label; separator=';') AS ?label) "
				+ " from<http://gen.library.sh.cn/graph/work>  from<http://gen.library.sh.cn/graph/instance> from<http://gen.library.sh.cn/graph/item> WHERE{"
				+ "   <" + work_uri + "> a bf:Work . " + "OPTIONAL { <" + work_uri
				+ "> dc:title ?dtitle . FILTER (lang(?dtitle) = 'chs') }" + "OPTIONAL { <" + work_uri
				+ "> bf:title ?ts . {SELECT ?ts ?title FROM <" + "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "       ?ts a bf:WorkTitle ; " + "           bf:label ?title . "
				+ "       FILTER (lang(?title)='chs') " + "   }}" + "}" + "OPTIONAL { <" + work_uri
				+ "> shl:description ?note . } " + "OPTIONAL { <" + work_uri + "> bf:creator ?cs . "
				+ "   {SELECT ?cs ?creator FROM <" + "http://gen.library.sh.cn/graph/person" + "> WHERE {"
				+ "       ?cs bf:label ?creator . " + "   FILTER (lang(?creator) = 'chs')" + "   }}" + "} "
				+ "OPTIONAL {SELECT ?work ?access ?doi ?ins ?item ?hasimg WHERE { ?item a bf:Item optional{?item shl:hasFullImg ?hasimg} {?item shl:accessLevel ?access} OPTIONAL{?item shl:DOI ?doi} {?item bf:itemOf ?ins.  {SELECT ?ins ?work ?temporal  WHERE {?ins a bf:Instance ;bf:instanceOf <"
				+ work_uri + "> }}}" + fulllinkCalse + "}}" + "OPTIONAL { " + "   <" + work_uri + "> bf:title ?tss . "
				+ "   {SELECT ?tss ?tv FROM <" + "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "       ?tss a bf:VariantTitle ; " + "            bf:label ?tv . " + "   FILTER (lang(?tv)='chs') "
				+ "   }}" + "}" + "OPTIONAL { " + "   <" + work_uri + "> bf:subject ?th . "
				+ "   {SELECT ?th ?label FROM <" + "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "       ?th a shl:TitleOfAncestralTemple ; bf:label ?label . " + "   FILTER (lang(?label)='chs')"
				+ "   }}" + "}" + "}";

		List list = SparqlExecution.vQuery(_set, query,
				new String[] { "dtitle", "title", "subtitle", "creator", "note", "label", "doi", "access", "hasimg" });
		if (CollectionUtils.isNotEmpty(list)) {
			return (Map) list.get(0);
		}
		return null;
	}

	public ArrayList getInstances4Work(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String fulllinkCalse = "";
		// 如果不是管理员
		if (!RoleGroup.admin.getGroup().equals(CommonUtils.loginUser.getRoleId())) {
			// fulllinkCalse =
			// ".FILTER(?accessLevel='0'||?accessLevel='1')";chenss 2018-04-25
			fulllinkCalse = ".FILTER(?accessLevel!='9')";
		}
		String query = Namespace.getNsPrefixString()
				+ "SELECT DISTINCT ?ins ?temporal ?extent ?edition ?category ?item (GROUP_CONCAT(DISTINCT ?description ;separator=';') AS ?description ) ?hbs ?ab ?place ?org ?addr ?shelf ?doi ?accessLevel ?hasimg"
				+ " FROM <http://gen.library.sh.cn/graph/work> FROM <http://gen.library.sh.cn/graph/instance> FROM <http://gen.library.sh.cn/graph/item> "
				+ "WHERE {       " + "   ?ins a bf:Instance; bf:instanceOf <" + work_uri + "> . "
				+ "   OPTIONAL {?ins shl:temporalValue ?temporal . FILTER (datatype(?temporal) != xsd:integer) }"
				+ "   OPTIONAL {" + "       ?ins bf:edition ?es . " + "       {SELECT ?es ?edition FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {" + "           ?es bf:label ?edition ."
				+ "           FILTER (lang(?edition)='chs') " + "       }}" + "   }" + "   OPTIONAL {"
				+ "       ?ins bf:category ?cs ." + "       {SELECT ?cs ?category FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {" + "           ?cs bf:label ?category ."
				+ "           FILTER (lang(?category)='chs')" + "       }}" + "   }"
				+ "   OPTIONAL {?ins bf:extent ?extent .}" + "   OPTIONAL {"
				+ "       SELECT ?ins ?item ?description ?doi ?accessLevel ?hbs ?ab ?place ?org ?addr ?shelf ?hasimg FROM <"
				+ "http://gen.library.sh.cn/graph/item" + "> WHERE {" + "           ?item a bf:Item ; bf:itemOf ?ins ."
				+ "           OPTIONAL {?item shl:description ?description .}"
				+ "           OPTIONAL {?item shl:DOI ?doi .}"
				+ "           OPTIONAL {?item shl:accessLevel ?accessLevel " + fulllinkCalse + "}"
				+ "           OPTIONAL {?item shl:hasFullImg ?hasimg .}"
				+ "           OPTIONAL {?item bf:shelfMark ?shelf .}" + "           OPTIONAL {"
				+ "               ?item bf:heldBy ?hbs ." + "               {SELECT ?hbs ?ab ?org FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "                   ?hbs shl:abbreviateName ?ab ; bf:label ?org ."
				+ "                   FILTER (lang(?ab)='chs')" + "                   FILTER (lang(?org)='chs')"
				+ "               }}" + "               OPTIONAL {" + "                   SELECT ?hbs ?addr FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "                       ?hbs shl:address ?addr ." + "                   }" + "               }"
				+ "               OPTIONAL {" + "                   SELECT ?hbs ?place FROM <"
				+ "http://gen.library.sh.cn/graph/baseinfo" + "> WHERE {"
				+ "                       ?hbs shl:region ?rs . " + "                       {SELECT ?rs ?place FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {"
				+ "                           ?rs bf:label ?place . "
				+ "                           FILTER (lang(?place)='chs')" + "                       }} "
				+ "               }}" + "           }" + "   }}" + "} ORDER BY (?ab)";

		return SparqlExecution.vQuery(_set, query,
				new String[] { "ins", "temporal", "edition", "extent", "category", "item", "description", "org", "addr",
						"hbs", "ab", "place", "shelf", "doi", "accessLevel", "hasimg" });
	}

	public ArrayList getAllWorksWithGeo() {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String query = Namespace.getNsPrefixString() + "SELECT ?work ?title ?note ?begin ?end " + "WHERE {"
				+ "   ?work a bf:Work; dc:title ?title ;" + "         bf:place ?place . "
				+ "   OPTIONAL { ?work bf:hasAnnotation ?ann . " + "       {SELECT ?ann ?note FROM <"
				+ "http://gen.library.sh.cn/graph/annotation" + "> WHERE{" + "           ?ann bf:label ?note . "
				+ "       }}" + "   }" + "{" + "   SELECT ?instance ?begin ?end FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> " + "   WHERE {"
				+ "       ?instance bf:publishedOn/shl:beginYear ?begin ; "
				+ "                 bf:publishedOn/shl:endYear ?end . " + "   }" + "}" + "}";

		return SparqlExecution.vQuery(_set, query, new String[] { "work", "title", "note", "begin", "end" });
	}

	public ArrayList getWorksInYear(String begin, String end, int limit) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String fulllinkCalse = "";
		// 如果不是管理员
		if (!RoleGroup.admin.getGroup().equals(CommonUtils.loginUser.getRoleId())) {
			// fulllinkCalse =
			// ".FILTER(?access='0'||?access='1')";chenss20180424
			fulllinkCalse = ".FILTER (?access!='9')";
		}
		String query = Namespace.getNsPrefixString() + "SELECT ?work " + "(GROUP_CONCAT(DISTINCT  ?access;separator=';') AS  ?access)"
				+ "(GROUP_CONCAT(DISTINCT  ?doi;separator=';') AS  ?doi)"
				+ "(GROUP_CONCAT(DISTINCT  ?hasimg;separator=';') AS  ?hasimg)"
				+ " ?dtitle ?title (GROUP_CONCAT(DISTINCT ?t;separator=';') AS ?subtitles)"
				+ " (GROUP_CONCAT(DISTINCT ?note;separator=';') AS ?note) (if (STRLEN(STR(?time)) > 0, ?time, ?tb) AS ?begin) ?label ?long ?lat "
				+ " from<http://gen.library.sh.cn/graph/work>  from<http://gen.library.sh.cn/graph/instance> from<http://gen.library.sh.cn/graph/item> WHERE {"
				+ "   ?work a bf:Work; bf:title ?ts ; dc:title ?dtitle ; shl:place ?ps . "
				+ "   FILTER (lang(?dtitle) = 'chs')" + "   OPTIONAL {?work shl:description ?note .}"
				+ "   {SELECT ?ts ?title FROM <" + "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "       ?ts a bf:WorkTitle ; bf:label ?title . " + "       FILTER (lang(?title)='chs')" + "   }}"
				+ "OPTIONAL {SELECT ?work ?access ?doi ?ins ?item ?hasimg WHERE { ?item a bf:Item optional{?item shl:hasFullImg ?hasimg} {?item shl:accessLevel ?access} OPTIONAL{?item shl:DOI ?doi} {?item bf:itemOf ?ins.  {SELECT ?ins ?work ?temporal  WHERE {?ins a bf:Instance ;bf:instanceOf ?work      }}}"
				+ fulllinkCalse + "}}" + "   OPTIONAL {?work bf:title ?sts . " + "       {SELECT ?sts ?t FROM <"
				+ "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "           ?sts a bf:VariantTitle; bf:label ?t . FILTER (lang(?t)='chs')" + "       }}" + "   }"
				+ "   {SELECT ?work ?time ?tb FROM <" + "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       {" + "           ?ins bf:instanceOf ?work ; " + "                shl:temporalValue ?time . "
				+ "           FILTER ((?time >= " + begin + ") && (?time <= " + end + "))" + "       } UNION {"
				+ "           ?ins shl:temporal ?ts . " + "           {SELECT ?tb FROM <"
				+ "http://gen.library.sh.cn/graph/temporal" + "> WHERE {"
				+ "               ?ts shl:beginYear ?tb . FILTER ((?tb >= " + begin + ") && (?tb <= " + end + "))"
				+ "           }}" + "       }" + "    }}" + "   {SELECT ?ps ?label ?long ?lat FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {" + "       ?ps bf:label ?label ; "
				+ "              owl:sameAs ?same . " + "       FILTER (lang(?label)='chs') "
				+ "       {SELECT DISTINCT ?same ?long ?lat FROM <" + "http://www.cba.ac.cn/graph/geography"
				+ "> WHERE {" + "           ?same geo:long ?long ; " + "                 geo:lat ?lat . " + "       }}"
				+ "   }}" + "} ORDER BY ASC(xsd:integer(?begin)) ";

		if (limit > 0) {
			query = query + " LIMIT " + limit;
		}

		return SparqlExecution.vQuery(_set, query, new String[] { "work", "dtitle", "title", "subtitles", "note",
				"begin", "end", "label", "long", "lat", "doi", "access", "hasimg" });
	}

	public ArrayList getWorksInChao(String year, Boolean isMore) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String fulllinkCalse = "";
		// 如果不是管理员
		if (!RoleGroup.admin.getGroup().equals(CommonUtils.loginUser.getRoleId())) {
			// fulllinkCalse =
			// ".FILTER(?access='0'||?access='1')";chenss20180424
			fulllinkCalse = ".FILTER (?access!='9')";
		}
		String query = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work "
				+ "(GROUP_CONCAT(DISTINCT  ?access;separator=';') AS  ?access)"
				+ "(GROUP_CONCAT(DISTINCT  ?doi;separator=';') AS  ?doi)"
				+ "(GROUP_CONCAT(DISTINCT  ?hasimg;separator=';') AS  ?hasimg)"
				+ "?tangh (GROUP_CONCAT(DISTINCT ?creator;separator=';') AS ?creators) ?title ?place ?placeUri (if (STRLEN(STR(?time)) > 0, ?time, ?tb) AS ?begin) ?long ?lat "
				+ " from<http://gen.library.sh.cn/graph/work>  from<http://gen.library.sh.cn/graph/instance> from<http://gen.library.sh.cn/graph/item> WHERE {"
				+ "   ?work shl:place ?placeUri ; bf:title ?ts. " + "   {SELECT ?placeUri ?place ?long ?lat FROM <"
				+ "http://gen.library.sh.cn/graph/place" + "> WHERE {"
				+ "       ?placeUri a shl:Place; bf:label ?place ; " + "              owl:sameAs ?same . "
				+ "       FILTER (lang(?place)='chs') " + "       {SELECT DISTINCT ?same ?long ?lat FROM <"
				+ "http://www.cba.ac.cn/graph/geography" + "> WHERE {" + "           ?same geo:long ?long ; "
				+ "                 geo:lat ?lat . " + "       }}" + "   }}" + "   {SELECT ?ts ?title FROM <"
				+ "http://gen.library.sh.cn/graph/title" + "> WHERE {"
				+ "       ?ts bf:label ?title ; a bf:WorkTitle . FILTER (lang(?title)='chs') " + "   }}"
				+ "OPTIONAL {?work bf:creator ?cs .{SELECT ?cs ?creator FROM <http://gen.library.sh.cn/graph/person> WHERE { ?cs bf:label ?creator . FILTER (lang(?creator) = 'chs')}}}"
				+ "OPTIONAL {SELECT ?work ?access ?doi ?ins ?item ?hasimg WHERE { ?item a bf:Item optional{?item shl:hasFullImg ?hasimg} {?item shl:accessLevel ?access} OPTIONAL{?item shl:DOI ?doi} {?item bf:itemOf ?ins.  {SELECT ?ins ?work ?temporal  WHERE {?ins a bf:Instance ;bf:instanceOf ?work      }}}"
				+ fulllinkCalse + "}}"
				+ " OPTIONAL {?work bf:subject ?th . {SELECT ?th ?tangh FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {?th a shl:TitleOfAncestralTemple ; bf:label ?tangh.  FILTER (lang(?tangh)='chs')    }} }"
				+ "   {SELECT ?work ?time ?tb FROM <" + "http://gen.library.sh.cn/graph/instance" + "> WHERE {"
				+ "       {" + "           ?ins bf:instanceOf ?work ; shl:temporalValue ?time . FILTER (?time = " + year
				+ ") " + "       } UNION {" + "           ?ins bf:instanceOf ?work ; shl:temporal ?ts . "
				+ "           {SELECT ?tb FROM <" + "http://gen.library.sh.cn/graph/temporal" + "> WHERE {"
				+ "               ?ts shl:beginYear ?tb . FILTER (?tb = " + year + ") " + "           }}" + "       }"
				+ "   }}" + "} ";
		if (!isMore) {
			query += " limit 20";
		}
		return SparqlExecution.vQuery(_set, query, new String[] { "work", "title", "creators", "tangh", "place",
				"placeUri", "begin", "long", "lat", "doi", "access", "hasimg" });
	}

	public OutputStream getWorkAllInfos(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		Model m = ModelFactory.createDefaultModel();

		String query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "WHERE {{" + "   ?s ?p ?o ." + "   FILTER (STR(?s) = '"
				+ work_uri + "')" + "} UNION {" + "   <" + work_uri + "> bf:workTitle ?s ." + "   ?s ?p ?o ." + "}"
				+ "}";

		m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <" + "http://gen.library.sh.cn/graph/annotation" + "> "
				+ "WHERE {" + "   ?s ?p ?o ." + "{SELECT DISTINCT ?s WHERE {" + "   <" + work_uri
				+ "> bf:hasAnnotation ?s ." + "}}" + "}";

		m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <" + "http://gen.library.sh.cn/graph/instance" + "> "
				+ "WHERE {{" + "   ?s ?p ?o ." + "   {SELECT DISTINCT ?s WHERE {" + "       <" + work_uri
				+ "> bf:hasInstance ?s ." + "   }}" + "} UNION {" + "   ?s ?p ?o ;" + "      shl:holdingFor ?in ."
				+ "   {SELECT DISTINCT ?in WHERE {" + "       <" + work_uri + "> bf:hasInstance ?s . " + "   }}" + "}"
				+ "}";

		m.add(SparqlExecution.construct(_set, query));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		m.write(stream, "JSON-LD");

		return stream;
	}

	public OutputStream getResource(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		Model temp_m = ModelFactory.createDefaultModel();
		String query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <" + "http://gen.library.sh.cn/graph/work" + "> "
				+ "WHERE {" + "   ?s ?p ?o . " + "   FILTER (?s = <" + work_uri + ">)" + "}";

		temp_m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <" + "http://gen.library.sh.cn/graph/instance" + "> "
				+ "WHERE {" + "   ?s ?p ?o ; bf:instanceOf <" + work_uri + "> ." + "}";

		temp_m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <" + "http://gen.library.sh.cn/graph/item" + "> "
				+ "WHERE {" + "   ?s bf:itemOf ?ins ; ?p ?o ." + "   {SELECT ?ins FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {" + "       ?ins bf:instanceOf <" + work_uri
				+ "> ." + "   }}" + "}";

		temp_m.add(SparqlExecution.construct(_set, query));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		temp_m.write(stream, "RDF/XML-ABBREV");

		return stream;
	}

	public OutputStream getTriples(ApiWorkSearchBean search, String format) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		Model temp = ModelFactory.createDefaultModel();

		String clause = ApiMergeSearchParts.workSearchClause(search);

		String sql = Namespace.getNsPrefixString() + "SELECT DISTINCT ?work " + "WHERE { " + "   ?work a bf:Work ; dc:title ?dtitle."
				+ clause + "} LIMIT 100";

		ArrayList works = SparqlExecution.vQuery(_set, sql, new String[] { "work" });
		for (int i = 0; i < works.size(); i++) {
			String uri = ((Map) works.get(i)).get("work").toString();

			temp.add(getWorkModel(uri));
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		temp.write(stream, format);

		return stream;
	}

	private Model getWorkModel(String work_uri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		Model temp_m = ModelFactory.createDefaultModel();
		String query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o} " + "FROM <" + "http://gen.library.sh.cn/graph/work" + "> "
				+ "WHERE {" + "   ?s ?p ?o . " + "   FILTER (?s = <" + work_uri + ">)" + "}";

		temp_m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <" + "http://gen.library.sh.cn/graph/instance" + "> "
				+ "WHERE {" + "   ?s ?p ?o ; bf:instanceOf <" + work_uri + "> ." + "}";

		temp_m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s ?p ?o}" + "FROM <" + "http://gen.library.sh.cn/graph/item" + "> "
				+ "WHERE {" + "   ?s bf:itemOf ?ins ; ?p ?o ." + "   {SELECT ?ins FROM <"
				+ "http://gen.library.sh.cn/graph/instance" + "> WHERE {" + "       ?ins bf:instanceOf <" + work_uri
				+ "> ." + "   }}" + "}";

		temp_m.add(SparqlExecution.construct(_set, query));

		query = Namespace.getNsPrefixString() + "CONSTRUCT {?s shl:relatedWork <" + work_uri + "> .} " + "FROM <"
				+ "http://gen.library.sh.cn/graph/person" + "> " + "WHERE {" + "   ?s shl:roleOfFamily ?role ; "
				+ "      shl:relatedWork <" + work_uri + "> ." + "} LIMIT 50";

		temp_m.add(SparqlExecution.construct(_set, query));

		return temp_m;
	}

	@Override
	public ArrayList getFacetCountOthers(ApiWorkSearchBean bean) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		if (null == bean.getIsMore()) {
			bean.setIsMore(false);
		}
		String clause = ApiMergeSearchParts.workSearchClause(bean);
		String facetQueryString = "";
		String limitString = "";
		String facetColumns = "";
		String[] facetRecords = new String[] {};

		switch (bean.getFacetType()) {
		// 地点分面
		case "1":
			facetColumns = "('其他' as ?placeUri)  ('其他' as ?place) ";
			facetQueryString = " .filter not exists{?work shl:place ?placeUriOther}";
			facetRecords = new String[] { "placeUri", "place", "count" };
			break;
		// 堂号分面
		case "2":
			facetColumns = "('其他' as ?tanghUri) ('其他' as ?tangh) ";
			facetQueryString = " .filter not exists{?work bf:subject ?tanghUriOther . {SELECT ?tanghUriOther FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {?tanghUriOther a shl:TitleOfAncestralTemple}}}";
			facetRecords = new String[] { "tanghUri", "tangh", "count" };
			break;
		// 朝代分面
		case "3":
			facetColumns = "('其他' as ?temporalUri) ('其他' as ?temporal)";
			// facetQueryString =
			// " .{SELECT ?work ?s ?temporal FROM <http://gen.library.sh.cn/graph/instance>
			// WHERE {?s bf:instanceOf ?work ;shl:temporalValue ?temporal . FILTER
			// (datatype(?temporal) != xsd:integer) }}";
			facetQueryString = ".filter not exists{SELECT ?work ?s ?temporalUriOther FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporal ?temporalUriOther }}";
			facetRecords = new String[] { "temporalUri", "temporal", "count" };
			break;
		// 版本分面
		case "4":
			facetColumns = " ('其他' as ?editionUri) ('其他' as ?edition)";
			facetQueryString = ".filter not exists{SELECT  ?work ?s ?editionUriOther FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work; bf:edition ?editionUriOther }}";
			facetRecords = new String[] { "editionUri", "edition", "count" };
			break;
		// 馆藏地分面
		case "5":
			facetColumns = " ('其他' as ?orgUri) ('其他' as ?org)";
			facetQueryString = ".filter not exists{SELECT ?work ?hbs FROM <http://gen.library.sh.cn/graph/item> WHERE {?item a bf:Item ;bf:heldBy ?hbs. {?item bf:itemOf ?ins.{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?ins bf:instanceOf ?work .}}}}}";
			facetRecords = new String[] { "orgUri", "org", "count" };
			break;
		default:
			break;
		}

		String sql = "WHERE { ?work a bf:Work; dc:title ?dtitle" + facetQueryString + "." + clause + "}";
		String queryLast = Namespace.getNsPrefixString() + "select " + facetColumns
				+ " (count(distinct ?work) as ?count) from<http://gen.library.sh.cn/graph/work>"
				+ "from<http://gen.library.sh.cn/graph/person>" + "from<http://gen.library.sh.cn/graph/instance>"
				+ "from<http://gen.library.sh.cn/graph/item>" + "from<http://gen.library.sh.cn/graph/baseinfo>"
				+ "from<http://gen.library.sh.cn/graph/temporal>" + sql;
		ArrayList works = SparqlExecution.vQuery(_set, queryLast, facetRecords);
		return works;
	}

	@Override
	public ArrayList getFacetCount(ApiWorkSearchBean bean) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		if (null == bean.getIsMore()) {
			bean.setIsMore(false);
		}
		String clause = ApiMergeSearchParts.workSearchClause(bean);
		String facetQueryString = "";
		String limitString = "";
		String facetColumns = "";
		// 如果isMore参数为false，则查询5条。，true则查询全部。
		/*
		 * if (!bean.getIsMore()) { limitString = " offset 0 limit 5"; }
		 */
		String[] facetRecords = new String[] {};
		String facetType = bean.getFacetType();
		switch (facetType) {
		// 地点分面
		case "1":
			facetColumns = "?placeUri  ?place ";
			facetQueryString = " .{?work shl:place ?placeUri.{SELECT ?placeUri ?place FROM <http://gen.library.sh.cn/graph/place> WHERE { ?placeUri bf:label ?place.FILTER (lang(?place)='chs')}}}";
			facetRecords = new String[] { "placeUri", "place", "count" };
			break;
		// 堂号分面
		case "2":
			facetColumns = "?tanghUri ?tangh ";
			facetQueryString = " .{?work bf:subject ?tanghUri . {SELECT ?tanghUri  ?tangh FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {?tanghUri a shl:TitleOfAncestralTemple ; bf:label ?tangh .  FILTER (lang(?tangh )='chs')    }}}";
			facetRecords = new String[] { "tanghUri", "tangh", "count" };
			break;
		// 朝代分面
		case "3":
			facetColumns = "?temporalUri ?temporal";
			// facetQueryString =
			// " .{SELECT ?work ?s ?temporal FROM <http://gen.library.sh.cn/graph/instance>
			// WHERE {?s bf:instanceOf ?work ;shl:temporalValue ?temporal . FILTER
			// (datatype(?temporal) != xsd:integer) }}";
			facetQueryString = ".{SELECT ?work ?s ?temporalUri ?temporal FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporal ?temporalUri .{SELECT ?temporalUri ?temporal WHERE {?temporalUri bf:label ?temporal.}} }}";
			facetRecords = new String[] { "temporalUri", "temporal", "count" };
			break;
		// 版本分面
		case "4":
			facetColumns = " ?editionUri ?edition";
			facetQueryString = ".{SELECT  ?work ?s ?editionUri ?edition FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work; bf:edition ?editionUri. {SELECT ?editionUri ?edition FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {?editionUri bf:label ?edition .FILTER (lang(?edition)='chs') } }}}";
			facetRecords = new String[] { "editionUri", "edition", "count" };
			break;
		// 馆藏地分面
		case "5":
			facetColumns = " (?hbs as ?orgUri) ?org";
			facetQueryString = ".{SELECT ?work ?hbs ?org FROM <http://gen.library.sh.cn/graph/item> WHERE {?item a bf:Item ;bf:heldBy ?hbs.{SELECT ?hbs ?org FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {?hbs shl:abbreviateName ?ab ; bf:label ?org.  FILTER (lang(?org)='chs')}} {?item bf:itemOf ?ins.{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?ins bf:instanceOf ?work .}}}}}";
			facetRecords = new String[] { "orgUri", "org", "count" };
			break;
		// 是否全文分面
		case "6":
			facetColumns = " ?acc";
			// 是否选择只查全文
			String thisCalus = StringUtils.isNotBlank(bean.getAccFlg()) ? ".filter (?acc='0')" : ".filter (?acc!='9')";
			// 如果是1，则只查询非全文数据
			if ("1".equals(bean.getAccFlg())) {
				thisCalus = ".FILTER(?acc='1')";
			}
			// 如果是-1，则只查询为空数据
			else if ("-1".equals(bean.getAccFlg())) {
				thisCalus = ".filter (?acc='-1')";
			}
			facetQueryString = ".{SELECT ?work ?acc FROM <http://gen.library.sh.cn/graph/item> WHERE {   ?item a bf:Item {?item shl:accessLevel ?acc} {?item bf:itemOf ?ins.{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?ins bf:instanceOf ?work .     }}}"
					+ thisCalus + "}}";
			facetRecords = new String[] { "acc", "count" };
			break;
		default:

			break;
		}
		String sql = "";
		// if (!StringUtilC.isEmpty(bean.getFreeText())) {
		// // 如果简单检索字段不为空，则进行简单检索
		// if (!StringUtilC.isEmpty(bean.getFreeText())) {
		// sql =
		// "WHERE { { ?work a bf:Work ; dc:title ?dtitle. ?dtitle bif:contains '\""
		// + bean.getFreeText()
		// + "\"'"
		// + "} UNION {"
		// + " ?work bf:subject ?th . "
		// + " {SELECT ?th FROM <"
		// + "http://gen.library.sh.cn/graph/baseinfo"
		// + "> WHERE { "
		// + " ?th bf:label ?slabel . ?slabel bif:contains '\""
		// + bean.getFreeText()
		// + "\"'"
		// + " }}"
		// +
		// " UNION {?work a bf:Work .{SELECT ?work FROM
		// <http://gen.library.sh.cn/graph/person> WHERE { ?uri shl:relatedWork ?work ;
		// bf:label ?clabel .?clabel bif:contains '\""
		// + bean.getFreeText()
		// + "\"'"
		// + "}}}"
		// + "}"
		// + facetQueryString + "}";
		// }
		// } else {
		// sql = "WHERE { ?work a bf:Work; dc:title ?dtitle"
		// + facetQueryString + "." + clause + "}";
		// }
		// freeText 的条件转到ApiMergeSearchParts.workSearchClause
		sql = "WHERE { ?work a bf:Work; dc:title ?dtitle" + facetQueryString + "." + clause + "}";
		String queryLast = Namespace.getNsPrefixString() + "select " + facetColumns
				+ " (count(distinct ?work) as ?count) from<http://gen.library.sh.cn/graph/work>"
				+ "from<http://gen.library.sh.cn/graph/person>" + "from<http://gen.library.sh.cn/graph/instance>"
				+ "from<http://gen.library.sh.cn/graph/item>" + "from<http://gen.library.sh.cn/graph/baseinfo>"
				+ "from<http://gen.library.sh.cn/graph/temporal>" + sql + " ORDER BY DESC (?count)" + limitString;
		ArrayList works = SparqlExecution.vQuery(_set, queryLast, facetRecords);
		return works;
	}

	@Override
	public ArrayList getFreeResultList(String free_text, Integer maxCount) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		free_text = StringUtilC.StringFilter(free_text.trim());
		String course = ". ?x bif:contains '\"" + free_text + "\"'";
		String sql = Namespace.getNsPrefixString() + "SELECT ?x " + "WHERE  { {  ?work a bf:Work ; dc:title ?x" + course + " } "
				+ "UNION {{SELECT  ?x FROM <http://gen.library.sh.cn/graph/baseinfo> " + "WHERE {[] bf:label ?x"
				+ course + "  }} }" + ".FILTER (lang(?x)='chs')" + "}" + "GROUP BY ?x " + "ORDER BY STRLEN(?x) (?x)"
				+ "LIMIT " + maxCount;
		ArrayList works = SparqlExecution.vQuery(_set, sql, new String[] { "x" });
		return works;
	}

	@Override
	public Map getDoiByWorkUri(String workUri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String sql = "SELECT DISTINCT ?work ?doi ?accessLevel WHERE {    ?work a bf:Work ; dc:title ?dtitle OPTIONAL{SELECT ?work ?accessLevel ?doi FROM <http://gen.library.sh.cn/graph/item> WHERE {   ?item a bf:Item ;shl:accessLevel ?accessLevel;shl:DOI ?doi; bf:itemOf ?ins.  {SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}}}.FILTER(str(?work) ='"
				+ workUri + "')}";
		ArrayList dois = SparqlExecution.vQuery(_set, sql, new String[] { "work", "doi", "accessLevel" });
		if (dois != null && dois.size() > 0) {
			return RDFUtils.transform((Map) dois.get(0));
		}
		return null;
	}

	@Override
	public List tongji(String furi) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String fFilter = "";
		if (!StringUtilC.isEmpty(furi)) {
			fFilter = ".FILTER(?fn=<" + furi + ">)";
		}
		String sql = "select (count(distinct ?work) as ?wcount)(count(distinct ?puri) as ?pcount) ?fn (min(?year) as ?minYear)"
				+ "from<http://gen.library.sh.cn/graph/work>" + "from<http://gen.library.sh.cn/graph/person>"
				+ "from<http://gen.library.sh.cn/graph/instance>" + "from<http://gen.library.sh.cn/graph/item>"
				+ "from<http://gen.library.sh.cn/graph/baseinfo>" + "from<http://gen.library.sh.cn/graph/temporal>"
				+ "where{" + "      {?work a bf:Work;bf:subject ?fn" + fFilter + "}"
				+ "      {?ins a bf:Instance;bf:instanceOf ?work. "
				+ "        OPTIONAL {?ins shl:temporalValue ?yer . FILTER (datatype(?yer)  = xsd:integer)}"
				+ "         OPTIONAL {?ins shl:temporal ?time .{SELECT ?time ?begin WHERE         {?time shl:beginYear ?begin .}}}"
				+ "      BIND(IF(STRLEN(str(?yer))>0,?yer,?begin) AS ?year)}" + "      {?item a bf:Item;bf:itemOf ?ins}"
				+ "      {?fn a shl:FamilyName; bf:label ?fname.filter(lang(?fname)='chs')}   "
				+ " OPTIONAL{?puri a shl:Person{?puri foaf:familyName ?fn} {?puri shl:relatedWork ?work}{?puri shl:roleOfFamily ?role}}"
				+ " }GROUP BY ?fn ORDER BY DESC (?wcount)";
		List _list = RDFUtils.transformListMap(
				SparqlExecution.vQuery(_set, sql, new String[] { "wcount", "pcount", "fn", "minYear" }));
		return _list;
	}

	@Override
	public Map getDetailByWorkUri(String workUri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String strGraph = " FROM <http://gen.library.sh.cn/graph/work>"
				+ " FROM <http://gen.library.sh.cn/graph/instance>" + " FROM <http://gen.library.sh.cn/graph/title>"
				+ " FROM <http://gen.library.sh.cn/graph/person>" + " FROM <http://gen.library.sh.cn/graph/baseinfo>"
				+ " FROM <http://gen.library.sh.cn/graph/place> FROM <http://gen.library.sh.cn/graph/temporal>";

		String strsql = "SELECT ?coverImage ?fnameLabel ?temporalUri ?temporalName ?editionUri ?categoryUri ?temporalInt ?temporal ?edition ?category ?extent ?ins ?work ?dtitle \r\n"
				+ "       (IF(STRLEN(str(?titlezheng))>0,?titlezheng,?dtitle) AS ?titlezheng)\r\n"
				+ "       (GROUP_CONCAT(DISTINCT ?titlefu ; separator=';') AS ?titlefu ) \r\n"
				+ "     ?note \r\n"
				+ "       (GROUP_CONCAT(DISTINCT ?creator ; separator=';') AS ?creator) \r\n"
				+ "       (GROUP_CONCAT(DISTINCT ?tanghao; separator=';') AS ?tanghao) \r\n"
				+ "     ?placeUri ?place ?place_province ?place_city\r\n"
				+strGraph
				+ "WHERE {\r\n"
				+ "?ins a bf:Instance;\r\n"
				+ "        bf:instanceOf ?work.\r\n"
				+ "  OPTIONAL {?work a bf:Work ;\r\n"
				+ "                  dc:title ?dtitle FILTER (lang(?dtitle) = 'chs') }\r\n"
				+ "  OPTIONAL {?work bf:identifiedBy ?identifiedBy}\r\n"
				+ "  OPTIONAL {?work dc:subject ?fnameLabel}\r\n"
				+ "  OPTIONAL {?work shl:coverImage ?coverImage}\r\n"
				+ "  OPTIONAL {?work bf:title ?titlezhengUri .{ ?titlezhengUri  a bf:WorkTitle;\r\n"
				+ "                  bf:label ?titlezheng FILTER (lang(?titlezheng)='chs') }}\r\n"
				+ "  OPTIONAL {?work bf:title ?titlefuUri .{?titlefuUri \r\n"
				+ "                  a bf:VariantTitle;\r\n"
				+ "                  bf:label ?titlefu FILTER (lang(?titlefu)='chs') }}\r\n"
				+ "  OPTIONAL {?work shl:description ?note }\r\n"
				+ "  OPTIONAL {?work dc:creator ?dccreator FILTER (lang(?dccreator) = 'chs')}\r\n"
				+ "  OPTIONAL {?work bf:creator ?creatorUri .{?creatorUri \r\n"
				+ "                  a shl:Person;\r\n"
				+ "                  bf:label ?bfcreator FILTER (lang(?bfcreator) = 'chs') }}\r\n"
				+ "  OPTIONAL {?work bf:subject ?thUri .{?thUri \r\n"
				+ "                  a shl:TitleOfAncestralTemple ;\r\n"
				+ "                  bf:label ?tanghao FILTER (lang(?tanghao)='chs') }}\r\n"
				+ "  OPTIONAL {?work shl:place ?placeUri.{?placeUri\r\n"
				+ "                  a shl:Place.\r\n"
				+ "                  OPTIONAL {?placeUri shl:city ?place_city}\r\n"
				+ "                  OPTIONAL {?placeUri shl:province ?place_province}\r\n"
				+ "                  OPTIONAL {?placeUri bf:label ?place FILTER (lang(?place)='chs')}\r\n"
				+ "}\r\n"
				+ "  }.\r\n"
				+ "  FILTER(?work = <" + workUri + ">)\r\n"
				+ "  OPTIONAL {?ins shl:temporalValue ?temporal FILTER (datatype(?temporal)!= xsd:integer) }\r\n"
				+ "  OPTIONAL {?ins shl:temporalValue ?temporalInt FILTER (datatype(?temporalInt) = xsd:integer) }\r\n"
				+ "  OPTIONAL {?ins shl:temporal ?temporalUri .{?temporalUri\r\n"
				+ "                  a shl:Temporal;\r\n"
				+ "                  shl:dynasty ?temporalName }}\r\n"
				+ "  OPTIONAL {?ins bf:edition ?editionUri .{?editionUri\r\n"
				+ "                  a bf:Category;\r\n"
				+ "                  bf:categoryType 'edition';\r\n"
				+ "                  bf:label ?edition FILTER (lang(?edition)='chs') }}\r\n"
				+ "  OPTIONAL {?ins bf:category ?categoryUri .{?categoryUri\r\n"
				+ "                  a bf:Category;\r\n"
				+ "                  bf:categoryType 'category';\r\n"
				+ "                  bf:label ?category FILTER (lang(?category)='chs') }}\r\n"
				+ "  OPTIONAL {?ins bf:extent ?extent }\r\n"
				+ "  BIND(IF(STRLEN(str(?dccreator))>0,?dccreator,?bfcreator) AS ?creator)\r\n"
				+ "}";
		// 获取work,instance单条信息
		List _list = SparqlExecution.vQuery(_set, strsql,
				new String[] {"coverImage",  "fnameLabel", "temporal", "edition", "category", "extent", "ins", "work",
						"dtitle", "titlezheng", "titlefu", "note", "creator", "tanghao", "placeUri", "place",
						"editionUri", "categoryUri", "temporalInt", "temporalUri", "temporalName" , "place_province","place_city"});
		if (_list != null && _list.size() > 0) {
			// 查询item列表
			String itemSql = "select ?item ?ins ?work ?copyDescription ?extent ?doi ?accessLevel ?shelfMark ?hasFullImg ?carrierUri ?carrierLabel ?description ?orgUri (IF(STRLEN(str(?orgShort))>0,?orgShort,?description) AS ?orgShort)  (IF(STRLEN(str(?orgFull))>0,?orgFull,?description) AS ?orgFull) ?address "
					+ " from<http://gen.library.sh.cn/graph/work>  "
					+ " from<http://gen.library.sh.cn/graph/instance>  " + " from<http://gen.library.sh.cn/graph/item>"
					+ " from<http://gen.library.sh.cn/graph/baseinfo>"
					+ " where {?item a bf:Item OPTIONAL {?item shl:DOI ?doi}"
					+ " OPTIONAL {?item shl:accessLevel ?accessLevel}" + " OPTIONAL {?item shl:hasFullImg ?hasFullImg}"
					+ " OPTIONAL {?item bf:shelfMark ?shelfMark}" + " OPTIONAL {?item bf:extent ?extent}"
					+ " OPTIONAL {?item bf:heldBy ?orgUri .{?orgUri a shl:Organization OPTIONAL {?orgUri shl:abbreviateName ?orgShort .FILTER (lang(?orgShort)='chs')} OPTIONAL{?orgUri bf:label ?orgFull.FILTER (lang(?orgFull)='chs')} OPTIONAL {?orgUri shl:address ?address.FILTER (lang(?address)='chs')}}}"
					+ " OPTIONAL {?item shl:description ?description}"
					+ " OPTIONAL {?item shl:copyDescription ?copyDescription}"
					+ " OPTIONAL {?item bf:carrierCategory ?carrierUri .{?carrierUri a bf:Category; bf:label ?carrierLabel .FILTER (lang(?carrierLabel)='chs')}} "
					+ "{?item bf:itemOf ?ins.{?ins a bf:Instance;bf:instanceOf ?work.filter(?work=<" + workUri + ">)}}"
					+ "} ORDER BY desc (?orgUri)";
			List _itemList = RDFUtils.transformListMap(SparqlExecution.vQuery(_set, itemSql,
					new String[] { "item", "ins", "work", "doi", "accessLevel", "shelfMark", "hasFullImg",
							"description", "copyDescription",  "orgUri", "orgShort", "orgFull", "address", "extent", "carrierUri", "carrierLabel" }));
			Map _map = RDFUtils.transform((Map) _list.get(0));
			// 将itemList放入map中
			_map.put("itemList", _itemList);

			// 新增姓氏字段抽取：chenss 2020-04-27 begin
			String familyNameSql = "select distinct ?fnameUri ?fname " + strGraph
					+ " where {?work a bf:Work ;bf:subject ?fnameUri.{?fnameUri a shl:FamilyName ; bf:label ?fname.FILTER (lang(?fname)='chs')}  "
					+ ".filter(?work=<" + workUri + ">)}";
			List _familyNameList = RDFUtils.transformListMap(
					SparqlExecution.vQuery(_set, familyNameSql, new String[] { "fnameUri", "fname" }));
			// 将姓氏列表放入map中
			_map.put("fnameList", _familyNameList);
			// 新增姓氏字段抽取：chenss 2020-04-27 end

			// 新增责任者字段抽取：chenss 2020-04-27 begin
			String[] createrAndContribution = new String[] { "bf:creator", "bf:contributor" };
			for (int i = 0; i < createrAndContribution.length; i++) {
				// 新增责任者字段抽取：chenss 2020-04-27 begin 新增排序号20231215 编目系统使用
				String creatorSql = "select distinct ?creatorUri ?creatorName ?serialNo ?creatorRoleUri ?creatorRoleName ?time ?timeUri"
						+ strGraph + " where {?work a bf:Work; " + createrAndContribution[i]
						+ " ?creatorUri .{?creatorUri a shl:Person; bf:label ?creatorName; "
						+ "bf:role ?creatorRoleUri .{?creatorRoleUri a bf:Category; bf:categoryType 'role'; bf:label ?creatorRoleName .FILTER (lang(?creatorRoleName)='chs')} .FILTER (lang(?creatorName) = 'chs')"
						+ " OPTIONAL {?creatorUri shl:temporalValue ?time } OPTIONAL {?creatorUri shl:serialNo ?serialNo } OPTIONAL {?creatorUri  shl:temporal ?timeUri }"
						+ "}" + ".filter(?work=<" + workUri + ">) } ORDER BY ?serialNo";
				List creatorList = RDFUtils
						.transformListMap(SparqlExecution.vQuery(_set, creatorSql, new String[] { "creatorUri",
								"creatorName", "serialNo","creatorRoleUri", "creatorRoleName", "time", "timeUri" }));
				// 将责任者、其他责任者列表放入map中：key为：createrList、contributorList
				_map.put(createrAndContribution[i].split(":")[1] + "List", creatorList);
			}
			// 新增责任者字段抽取：chenss 2020-04-27 end

			// 新增堂号字段抽取：chenss 2020-04-27 begin
			String tanghaoSql = "select distinct ?tanghaoUri ?tanghaoName " + strGraph
					+ " where {?work a bf:Work; bf:subject ?tanghaoUri.{?tanghaoUri a shl:TitleOfAncestralTemple ; bf:label ?tanghaoName. FILTER (lang(?tanghaoName)='chs')}"
					+ ".filter(?work=<" + workUri + ">)}";
			List tanghaoSqlList = RDFUtils.transformListMap(
					SparqlExecution.vQuery(_set, tanghaoSql, new String[] { "tanghaoUri", "tanghaoName" }));
			// 将堂号列表放入map中
			_map.put("tanghaoList", tanghaoSqlList);

			// 新增堂号字段抽取：chenss 2020-04-27 end
			//新增谱籍地抽取 begin 20201202
			List placeList = RDFUtils.transformListMap(placeSparql.getStandPlace(StringUtilC.getString(_map.get("placeUri"))));
			_map.put("placeMap",new HashMap());
			if(placeList!=null && placeList.size()>0) {
				_map.put("placeMap", placeList.get(0));
			}
			//新增谱籍地抽取end 20201202
			// 新增 联合编目系统卷数字段抽取：chenss 2020-06-16 begin

			String volumesSql = "select ?volumes " + strGraph
					+ " where {?work a bf:Work; bf:title ?titleUri.{?titleUri a bf:WorkTitle;shl:volumes ?volumes . FILTER (lang(?volumes)='chs')  }"
					+ ".filter(?work=<" + workUri + ">)}";
			List volumesList = RDFUtils
					.transformListMap(SparqlExecution.vQuery(_set, volumesSql, new String[] { "volumes" }));
			String volumes = "";
			if (volumesList != null && volumesList.size() > 0) {
				volumes = ((Map) volumesList.get(0)).get("volumes").toString();
			}
			// 将卷数字段放入map中
			_map.put("title_volumes", volumes);
			// 新增 出版发行者、版本说明项、附注项 字段抽取：chenss 2020-06-16 begin
			String pub_banben_fuzhuSql = "select ?ins ?work ?publisher ?banbenUri ?ins_banbenx ?fuzhuUri ?ins_fuzhux "
					+ strGraph + " where {?ins a bf:Instance;bf:instanceOf ?work. "
					+ "OPTIONAL{?ins dc:publisher ?publisher} "
					+ "OPTIONAL{?ins bf:note ?banbenUri .{?banbenUri a bf:Note; rdfs:label ?ins_banbenx ;bf:noteType '版本项说明'}} "
					+ "OPTIONAL{?ins bf:note ?fuzhuUri .{?fuzhuUri a bf:Note; rdfs:label ?ins_fuzhux ;bf:noteType '附注项'}} "
					+ ".filter(?work=<" + workUri + ">)}";
			List pub_banben_fuzhuList = RDFUtils.transformListMap(SparqlExecution.vQuery(_set,
					pub_banben_fuzhuSql,
					new String[] { "ins", "work", "publisher", "banbenUri", "ins_banbenx", "fuzhuUri", "ins_fuzhux" }));
			Map _ins_pub_banben_fuzhu = null;
			if (pub_banben_fuzhuList != null && pub_banben_fuzhuList.size() > 0) {
				// 将 出版发行者、版本说明项、附注项字段放入map中
				_ins_pub_banben_fuzhu = ((Map) pub_banben_fuzhuList.get(0));
				_map.put("ins_pub_banben_fuzhu", _ins_pub_banben_fuzhu);
			}
			// 新增 联合编目系统 将 出版发行者、版本说明项、附注项：chenss 2020-06-16 end
			return _map;
		}
		return null;
	}

	@Override
	public Map getQxTjInfo(String fname) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String sql = "select distinct ?family str(?fname) as ?fname count(distinct ?person) as ?personCount  count(distinct ?oriPlace) as ?oriPlaceCount  count(distinct ?locaPlace) as ?locaPlaceCount min(xsd:integer(?minyear)) as ?minYear  from  <http://gen.library.sh.cn/graph/migrationevent> from  <http://gen.library.sh.cn/graph/baseinfo>  where {?s a shl:MigrationEvent {?s foaf:familyName  ?family .{?family a shl:FamilyName; bf:label ?fname .filter (str(?fname)= '"+fname+"')}} optional{?s shl:person ?person} optional{?s shl:temporal ?tem{?tem a shl:Temporal ;shl:begin ?minyear .filter isBlank(?tem)}} optional{?s shl:originalLocality ?oriPlace} optional{?s  shl:locality ?locaPlace}  }"; 


		List volumesList = RDFUtils
				.transformListMap(SparqlExecution.vQuery(_set, sql, new String[] { "family","fname","personCount","oriPlaceCount" ,"locaPlaceCount","minYear"}));
		String volumes = "";
		if (volumesList != null && volumesList.size() > 0) {
			return  ((Map) volumesList.get(0));
		}
		return null;
	}

	@Override
	public Map getPlaceTjInfo(String fname) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		Map _map = new HashMap();
		String sqlCount = "select count(distinct ?s) as ?workCount count(distinct ?prov) as ?provCount from <http://gen.library.sh.cn/graph/work> from <http://gen.library.sh.cn/graph/baseinfo> from <http://gen.library.sh.cn/graph/place> where {?s a bf:Work;shl:place ?place.{?place a shl:Place;shl:province ?prov}{?s bf:subject ?f.{?f a shl:FamilyName;bf:label '"+fname+"'@chs}}}"; 
		String sqlProv = "select ?prov count(?s) as ?workCount from <http://gen.library.sh.cn/graph/work> from <http://gen.library.sh.cn/graph/baseinfo>  from <http://gen.library.sh.cn/graph/place> where {?s a bf:Work;bf:subject ?f.{?f a shl:FamilyName;bf:label '"+fname+"'@chs} {?s shl:place ?place.{?place a shl:Place;shl:province ?prov}} } group by ?prov order by desc (?workCount) limit 3"; 
		List volumesList = RDFUtils
				.transformListMap(SparqlExecution.vQuery(_set, sqlCount, new String[] { "workCount","provCount"}));
		
		if (volumesList != null && volumesList.size() > 0) {
			_map = ((Map) volumesList.get(0));
		}
		List provList = RDFUtils
				.transformListMap(SparqlExecution.vQuery(_set, sqlProv, new String[] { "prov","workCount"}));
		_map.put("provList", provList);
		_map.put("fname", fname);
		return _map;
	}
	
	@Override
	public Integer getWorkCountByPlaceAndFname(List<String> place, String fnameUri) {
		SparqlExecution.init();
		VirtGraph _set = SparqlExecution.getGraph(Constant.GRAPH_WORK);
		String fnameFilter="";
		//姓氏URI
		if(!StringUtil.isBlank(fnameUri)) {
			fnameFilter = "bf:subject <"+fnameFilter+">;";
		}
		String query ="select count(?s) as ?count from <http://gen.library.sh.cn/graph/work> where {?s a bf:Work;"
				+fnameFilter
				+ "shl:place ?place .values ?place {\r\n"
				+StringUtils.join(place.toArray()," ")
				+ "\r\n"
				+ "}}";
		
		List<Map<String,String>> _list =  RDFUtils
				.transformListMap(SparqlExecution.vQuery(_set, query, new String[] { "count"}));
		if(_list!=null && _list.size()>0) {
			return Integer.valueOf(_list.get(0).get("count"));
		 }
		return 0;
	}
}
