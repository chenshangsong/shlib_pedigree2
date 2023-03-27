package cn.sh.library.pedigree.webApi.sparql;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.common.RoleGroup;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.dto.searchBean.ApiWorkSearchBean;

public class ApiMergeSearchParts {
	public static String ancTempSearchClause(AncTempSearchBean bean) {
		String clause = "";

		if (StringUtils.isNotBlank(bean.getFirstC())) {
			clause = clause + "FILTER STRSTARTS(lcase(?fn_en), '"
					+ bean.getFirstC().toLowerCase() + "') ";
		}

		if (StringUtils.isNotBlank(bean.getLabel())) {
			clause = clause + "FILTER REGEX(?label, '" + bean.getLabel()
					+ "') ";
		}

		if (StringUtils.isNotBlank(bean.getFamilyName())) {
			clause = clause
					+ "?fn bf:label ?familyName . FILTER ((?familyName = '"
					+ bean.getFamilyName() + "'@chs) && (?familyName = '"
					+ bean.getFamilyName() + "'@cht)) ";
		}

		return clause;
	}

	public static String personClause(PersonSearchBean bean) {
		String clause = "";

		if (StringUtils.isNotBlank(bean.getName())) {
			clause = clause + "FILTER CONTAINS(STR(?name), '" + bean.getName()
					+ "') ";
		}

		return clause;
	}

	public static String baseCatClause(PersonSearchBean bean) {
		String clause = "";

		if (StringUtils.isNotBlank(bean.getRole())) {
			clause = clause + "FILTER CONTAINS(STR(?label), '" + bean.getRole()
					+ "') ";
		}

		return clause;
	}

	public static String baseClause(PersonSearchBean bean) {
		String clause = "";

		if (StringUtils.isNotBlank(bean.getFamilyName())) {
			clause = clause + "FILTER ((?fn_label = '" + bean.getFamilyName()
					+ "'@chs) && (?fn_label = '" + bean.getFamilyName()
					+ "'@cht)) ";
		}

		if (StringUtils.isNotBlank(bean.getFirstChar())) {
			clause = clause + "FILTER STRSTARTS(lcase(?fn_en), '"
					+ bean.getFirstChar().toLowerCase() + "') ";
		}

		return clause;
	}

	public static String personSearchClause(PersonSearchBean bean) {
		String clause = "";

		if (StringUtils.isNotBlank(bean.getFamilyName())) {
			clause = clause
					+ "  {SELECT ?f ?fn_label FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {       ?f bf:label ?fn_label .        FILTER (STR(?fn_label) = '"
					+ bean.getFamilyName() + "') " + "   }} ";
		}

		if (StringUtils.isNotBlank(bean.getFirstChar())) {
			clause = clause
					+ "?s foaf:familyName ?fm . {SELECT ?fm ?family FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {   ?fm bf:label ?family ; a shl:FamilyName . FILTER (lang(?family) = 'en') FILTER STRSTARTS(lcase(?family), '"
					+ bean.getFirstChar().toLowerCase() + "') " + "}}";
		}
		if (StringUtils.isNotBlank(bean.getFnameUri())) {
			clause = clause + "?s foaf:familyName <" + bean.getFnameUri()
					+ "> ";
		}
		if (StringUtils.isNotBlank(bean.getName())) {
			clause = clause + "?s bf:label ?label . FILTER REGEX(?label, '"
					+ bean.getName() + "') ";
		}

		if (StringUtils.isNotBlank(bean.getRole())) {
			clause = clause + "FILTER REGEX(?cat_label, '" + bean.getRole()
					+ "') ";
		}

		if (StringUtils.isNotBlank(bean.getTime())) {
			clause = clause
					+ "?s shl:temporalValue ?time . FILTER CONTAINS(?time, '"
					+ bean.getTime() + "')";
		}

		if (StringUtils.isNotBlank(bean.getPlace())) {
			clause = clause
					+ "?s shl:relatedWork ?work . {SELECT ?work FROM <http://gen.library.sh.cn/graph/work>WHERE{   ?work shl:place ?place .   {SELECT ?place FROM <http://gen.library.sh.cn/graph/place>    WHERE {       ?place ?p ?pl .    FILTER CONTAINS(STR(?pl), '"
					+ bean.getPlace() + "')" + "   }}" + "}}";
		}

		if (StringUtils.isNotBlank(bean.getUri())) {
			clause = clause + "FILTER (STR(?s) = '" + bean.getUri() + "')";
		}

		return clause;
	}

	public static String workSearchClause(ApiWorkSearchBean bean) {
		String clause = "";

		/************************* 分面检索开始 **********************************/
		if (StringUtils.isNotBlank(bean.getFacetPlaceUri())) {
			if("其他".equals(bean.getFacetPlaceUri())){
				clause = clause + " .filter not exists{?work shl:place ?placeUriOther} ";
			}
			else {
				clause = clause + " {?work shl:place <" + bean.getFacetPlaceUri()
						+ ">} ";
			}
		}

		if (StringUtils.isNotBlank(bean.getFacetPlaceAllUri())) {
			if("其他".equals(bean.getFacetPlaceAllUri())){
				clause = clause + " .filter not exists{?work shl:place ?placeUriOther} ";
			}
			else if (bean.getStandardPlace() != null) {
				Map<String, String> stan = bean.getStandardPlace();
				String subClause = "";
				if (StringUtils.isNotBlank(stan.get("prov"))) {
					subClause += " ?facetPlace shl:province '"
							+ stan.get("prov") + "' .";
				}
				if (StringUtils.isNotBlank(stan.get("city"))) {
					subClause += " ?facetPlace shl:city '" + stan.get("city")
							+ "' .";
				}
				if (StringUtils.isNotBlank(stan.get("county"))) {
					subClause += " ?facetPlace shl:county '"
							+ stan.get("county") + "' .";
				}
				subClause = " select ?facetPlace from <http://gen.library.sh.cn/graph/place> where{?facetPlace a shl:Place. "
						+ subClause + "}";
				clause = clause + " {?work shl:place ?facetPlace. {"
						+ subClause + "} } ";
			} else {
				clause = clause + " {?work shl:place <"
						+ bean.getFacetPlaceAllUri() + ">} ";
			}
		}

		if (StringUtils.isNotBlank(bean.getFacetTanghUri())) {
			if("其他".equals(bean.getFacetTanghUri())){
				clause = clause + " .filter not exists{?work bf:subject ?tanghUriOther . {SELECT ?tanghUriOther FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {?tanghUriOther a shl:TitleOfAncestralTemple}}} ";
			}
			else {
				clause = clause + " {?work bf:subject <" + bean.getFacetTanghUri()
						+ ">} ";
			}
		}
		if (StringUtils.isNotBlank(bean.getFacetTemporal())) {
			/*
			 * clause = clause chenss 朝代文字---》朝代URI chenss 20180424 +
			 * "{SELECT ?work ?s FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporalValue '"
			 * + bean.getFacetTemporal() + "' }}";
			 */
			if("其他".equals(bean.getFacetTemporal())){
				clause = clause
						+ " .filter not exists{SELECT ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporal ?temporalUriOther}}";
			}
			else {
				clause = clause
						+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporal <"
						+ bean.getFacetTemporal() + ">}}";
			}
		}
		if (StringUtils.isNotBlank(bean.getFacetEditionUri())) {
			if("其他".equals(bean.getFacetEditionUri())){
				clause = clause
						+ " .filter not exists{SELECT  ?work  FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work; bf:edition ?editionUriOther}}";
			}
			else {
				clause = clause
						+ "{SELECT  ?work  FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work; bf:edition <"
						+ bean.getFacetEditionUri() + ">}}";
			}
		}
		if (StringUtils.isNotBlank(bean.getFacetOrgUri())) {
			if("其他".equals(bean.getFacetOrgUri())){
				clause = clause
						+ " .filter not exists{SELECT ?work  FROM <http://gen.library.sh.cn/graph/item>WHERE {?item a bf:Item ;bf:heldBy ?hbs {?item bf:itemOf ?ins.{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?ins bf:instanceOf ?work .}}}}}";
			}
			else {
				clause = clause
						+ "{SELECT ?work  FROM <http://gen.library.sh.cn/graph/item>WHERE {?item a bf:Item ;bf:heldBy <"
						+ bean.getFacetOrgUri()
						+ "> {?item bf:itemOf ?ins.{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?ins bf:instanceOf ?work .}}}}}";
			}
			
		}
		if (StringUtils.isNotBlank(bean.getFacetAccFlg())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;shl:accessLevel '"
					+ bean.getFacetAccFlg()
					+ "';"
					+ "bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}"
					+ "}}";
		}

		/************************* 查询条件检索开始 **********************************/
		if(StringUtils.isNotBlank(bean.getFreeText())){
			clause += "{   ?work a bf:Work ; dc:title ?dtitle1. ?dtitle1 bif:contains '\""
						+ bean.getFreeText()
						+ "\"'"
						+ "}UNION {"
						+ "   ?work bf:subject ?th . "
						+ "   {SELECT ?th FROM <"
						+ "http://gen.library.sh.cn/graph/baseinfo"
						+ "> WHERE { "
						+ "       ?th bf:label ?slabel . ?slabel bif:contains '\""
						+ bean.getFreeText()
						+ "\"'"
						+ "   }}}UNION {?work a bf:Work .{SELECT ?work FROM <http://gen.library.sh.cn/graph/person> WHERE {  ?uri shl:relatedWork ?work ; bf:label ?clabel .?clabel  bif:contains '\""
						+ bean.getFreeText()
						+ "\"'"
						+ "}}}";
			return clause;
		}

		if (StringUtils.isNotBlank(bean.getTitle())) {
			/*
			 * clause = clause + "FILTER CONTAINS(?dtitle, '" + bean.getTitle()
			 * + "') ";
			 */
			clause = clause
					+ "{?work dc:title ?dtitle1 .?dtitle1 bif:contains '\""
					+ bean.getTitle() + "\"'}";

		}
		
		if (StringUtils.isNotBlank(bean.getPlace())) {
			/*
			 * clause = clause + "FILTER CONTAINS(?dtitle, '" + bean.getPlace()
			 * + "') ";
			 */
			clause = clause
					+ "{?work dc:title ?dtitle2 .?dtitle2 bif:contains '\""
					+ bean.getPlace() + "\"'}";
		}

		if (StringUtils.isNotBlank(bean.getCreator())) {
			clause = clause
					+ " {?work bf:creator ?cs .    "
					+ "{SELECT ?cs FROM <http://gen.library.sh.cn/graph/person> WHERE {"
					+ " ?cs bf:label ?creator. ?creator bif:contains '\""
					+ bean.getCreator() + "\"'" + "   }}}";
		}

		if (StringUtils.isNotBlank(bean.getFamilyName())) {
			clause = clause
					+ " {?work bf:subject ?fn .   "
					+ " {SELECT ?fn FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {  "
					+ "     ?fn a shl:FamilyName; bf:label ?xing .        FILTER (STR(?xing)='"
					+ bean.getFamilyName() + "') " + "   }}} ";
		}

		if (StringUtils.isNotBlank(bean.getPerson())) {
			clause = clause
					+ " {SELECT ?work FROM <http://gen.library.sh.cn/graph/person> WHERE { "
					+ "  ?person shl:relatedWork ?work ;           "
					+ " bf:label ?name. ?name bif:contains '\""
					+ bean.getPerson() + "\"'" + "}}";
		}

		if (StringUtils.isNotBlank(bean.getTang())) {
			clause = clause
					+ " {?work bf:subject ?th .    "
					+ "{SELECT ?th FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {"
					+ "        ?th a shl:TitleOfAncestralTemple; bf:label ?label . ?label bif:contains '\""
					+ bean.getTang() + "\"'" + "   }}}";
		}

		if (StringUtils.isNotBlank(bean.getNote())) {
			clause = clause
					+ " {?work shl:description ?desc . ?desc bif:contains '\""
					+ bean.getNote() + "\"'}";
		}

		if (StringUtils.isNotBlank(bean.getAccFlg())) {
			// 是否选择只查全文
			String fulllinkCalse = ".filter (?acc='0')";
			// 如果是1，则只查询非全文数据
			if ("1".equals(bean.getAccFlg())) {
				fulllinkCalse = ".FILTER(?acc='1')";
			}
			// 如果是-1，则只查询为空数据
			else if ("-1".equals(bean.getAccFlg())) {
				fulllinkCalse = ".filter (str(?acc)='-1')";
			}
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item {?item shl:accessLevel ?acc}"
					+ "{?item bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}}"
					+ fulllinkCalse + "}}";
		} else {
			String fulllinkCalse = "";
			// 如果不是管理员
			if (!RoleGroup.admin.getGroup().equals(
					CommonUtils.loginUser.getRoleId())) {
				// fulllinkCalse =
				// ".FILTER(?access='0'||?access='1')";chenss20180424
				fulllinkCalse = ".FILTER (?acc!='9')";
			}
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item {?item shl:accessLevel ?acc"
					+ "}"
					+ "{?item bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}}"
					+ fulllinkCalse + "}}";

		}
		if (StringUtils.isNotBlank(bean.getSelfMark())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;bf:shelfMark ?slm"
					+ ";bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {"
					+ "       ?ins bf:instanceOf ?work .     }}.filter(contains(?slm,'"
					+ bean.getSelfMark() + "'))}}";
		}
		if (StringUtils.isNotBlank(bean.getDoi())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;shl:DOI ?sldoi"
					+ ";bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {  "
					+ "     ?ins bf:instanceOf ?work .     }}.filter(contains(?sldoi,'"
					+ bean.getDoi() + "'))}}";
		}
		if (StringUtils.isNotBlank(bean.getOrganization())) {
			clause = clause
					+ " {SELECT ?work FROM <http://gen.library.sh.cn/graph/item> WHERE { "
					+ "  ?item a bf:Item; bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {  "
					+ "     ?ins bf:instanceOf ?work .   }}   "
					+ "{SELECT ?item WHERE {       {?item bf:heldBy ?hbs.    "
					+ "        {SELECT ?hbs FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {         "
					+ "      ?hbs shl:abbreviateName ?ab ;                     bf:label ?org .            "
					+ "    FILTER (CONTAINS(?ab, '" + bean.getOrganization()
					+ "') || CONTAINS(?org, '" + bean.getOrganization() + "'))"
					+ "           }} " + "       }  }}}}";
		}
		//最后拼接 只能查开放全文：chenss20221009 dc:accFlag 新增属性：1：不能阅览。空为正常。
		clause = clause
				+ " optional{?work dc:workAccFlag ?workAccFlag} .filter (?workAccFlag !='1') ";
		return clause;
	}
}
