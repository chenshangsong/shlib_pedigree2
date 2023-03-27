package cn.sh.library.pedigree.sparql;

import org.apache.commons.lang3.StringUtils;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.bean.WorkSearchBean;

public class MergeSearchParts {
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
		/*待 人名中挂接的错误姓氏数据修复完毕后，恢复该功能（2018-02-05）
		 * 	clause = clause
					+ "  {SELECT ?f ?fn_label FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {    "
					+ "   ?f bf:label ?fn_label . ?fn_label bif:contains '\""+bean.getFamilyName()+"\"'" + "   }} ";
		
		clause = clause
					+ " .filter strstarts(?name,'" + StringUtilC.getCht(bean.getFamilyName())
					+ "') ";*/
			
		clause = clause
					+ "{?s bf:label ?names.filter strstarts(?names,'" + bean.getFamilyName()+ "')}";
		
			
		
			
		}

		if (StringUtils.isNotBlank(bean.getFirstChar())) {
			clause = clause
					+ "{?s foaf:familyName ?fm . {SELECT ?fm ?family FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {   ?fm bf:label ?family ; a shl:FamilyName . FILTER (lang(?family) = 'en') FILTER STRSTARTS(lcase(?family), '"
					+ bean.getFirstChar().toLowerCase() + "') " + "}}}";
		}
		if (StringUtils.isNotBlank(bean.getFnameUri())) {
			clause = clause
					+ "{?s foaf:familyName <"+bean.getFnameUri()+"> }";
		}
		if (StringUtils.isNotBlank(bean.getName())) {
			clause = clause + "{?s bf:label ?label . ?label bif:contains '\""+bean.getName()+"\"'}";
		}

		if (StringUtils.isNotBlank(bean.getRole())) {
			clause = clause + "FILTER REGEX(?cat_label, '" + bean.getRole()
					+ "') ";
		}

		if (StringUtils.isNotBlank(bean.getTime())) {
			clause = clause
					+ "{?s shl:temporalValue ?time . ?time bif:contains '\""+bean.getTime()+"\"'}";
		}

		if (StringUtils.isNotBlank(bean.getPlace())) {
			clause = clause
					+ "{?s shl:relatedWork ?work . {SELECT ?work FROM <http://gen.library.sh.cn/graph/work>WHERE{  "
					+ " ?work shl:place ?place .   {SELECT ?place FROM <http://gen.library.sh.cn/graph/place>    WHERE {  "
					+ "     ?place ?p ?pl . ?pl bif:contains '\""+bean.getPlace()+"\"'" +"}}}}}";
		}

		if (StringUtils.isNotBlank(bean.getUri())) {
			clause = clause + "FILTER (STR(?s) = '" + bean.getUri() + "')";
		}

		return clause;
	}

	public static String workSearchClause(WorkSearchBean bean) {
		String clause = "";

		if (StringUtils.isNotBlank(bean.getTitle())) {
			clause = clause
					+ " OPTIONAL {?work bf:title ?ts .    {SELECT ?ts ?title FROM <http://gen.library.sh.cn/graph/title> WHERE {       ?ts bf:label ?title .    }}} FILTER (CONTAINS(STR(?dtitle), '"
					+ bean.getTitle() + "') || CONTAINS(STR(?title), '"
					+ bean.getTitle() + "')) ";
		}

		if (StringUtils.isNotBlank(bean.getPlace())) {
			clause = clause + "FILTER CONTAINS(?dtitle, '" + bean.getPlace()
					+ "') ";
		}

		if (StringUtils.isNotBlank(bean.getCreator())) {
			clause = clause
					+ " ?work bf:creator ?cs .    {SELECT ?cs FROM <http://gen.library.sh.cn/graph/person> WHERE {        ?cs bf:label ?creator. FILTER CONTAINS(?creator, '"
					+ bean.getCreator() + "')" + "   }}";
		}

		if (StringUtils.isNotBlank(bean.getFamilyName())) {
			clause = clause
					+ " ?work bf:subject ?fn .    {SELECT ?fn FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {       ?fn a shl:FamilyName; bf:label ?xing .        FILTER (STR(?xing)='"
					+ bean.getFamilyName() + "') " + "   }} ";
		}

		if (StringUtils.isNotBlank(bean.getPerson())) {
			clause = clause
					+ " {SELECT ?work FROM <http://gen.library.sh.cn/graph/person> WHERE {   ?person shl:relatedWork ?work ;            bf:label ?name .    FILTER CONTAINS(?name, '"
					+ bean.getPerson() + "')" + "}}";
		}

		if (StringUtils.isNotBlank(bean.getTang())) {
			clause = clause
					+ " ?work bf:subject ?th .    {SELECT ?th FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {        ?th a shl:TitleOfAncestralTemple; bf:label ?label . FILTER CONTAINS(?label, '"
					+ bean.getTang() + "') " + "   }}";
		}

		if (StringUtils.isNotBlank(bean.getNote())) {
			clause = clause
					+ " ?work shl:description ?desc . FILTER CONTAINS(?desc, '"
					+ bean.getNote() + "')";
		}

		if (StringUtils.isNotBlank(bean.getAccFlg())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;shl:accessLevel ?acc;"
					+ "bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}"
					+ ".FILTER(?acc='0')}}";
		}
		if (StringUtils.isNotBlank(bean.getSelfMark())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;bf:shelfMark ?slm;"
					+ "bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}"
					+ ".FILTER CONTAINS(?slm, '" + bean.getSelfMark() + "')}}";
		}
		if (StringUtils.isNotBlank(bean.getDoi())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;shl:DOI ?sldoi;"
					+ "bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}"
					+ ".FILTER CONTAINS(?sldoi, '" + bean.getDoi() + "')}}";
		}
		if (StringUtils.isNotBlank(bean.getOrganization())) {
			clause = clause
					+ " {SELECT ?work FROM <http://gen.library.sh.cn/graph/item> WHERE {   ?item a bf:Item; bf:itemOf ?ins.  {SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .   }}   {SELECT ?item WHERE {       {?item bf:heldBy ?hbs.            {SELECT ?hbs FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {               ?hbs shl:abbreviateName ?ab ;                     bf:label ?org .                FILTER (CONTAINS(?ab, '"
					+ bean.getOrganization() + "') || CONTAINS(?org, '"
					+ bean.getOrganization() + "'))" + "           }} "
					+ "       }  }}}}";
		}
		
		if(StringUtils.isNotBlank(bean.getTemporalUri())){
			if("其他".equals(bean.getTemporalUri())){
				clause = clause
						+ "{SELECT ?work ?s FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work .filter not exists{?s shl:temporal ?temporalUri }}}";
			}else {
				clause = clause
						+ "{SELECT ?work ?s FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporal <"+bean.getTemporalUri()+"> }}";
			}
		}
		/*************************分面检索开始**********************************/
		if (StringUtils.isNotBlank(bean.getFacetPlaceUri())) {
			clause = clause
					+ " {?work shl:place <"+bean.getFacetPlaceUri()+">} ";
		}
		if (StringUtils.isNotBlank(bean.getFacetTanghUri())) {
			clause = clause
					+ " {?work bf:subject <"+bean.getFacetTanghUri()+">} ";
		}
		if (StringUtils.isNotBlank(bean.getFacetTemporal())) {
			clause = clause
					+ "{SELECT ?work ?s FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work ;shl:temporalValue '"+bean.getFacetTemporal()+"' }}";
		}
		if (StringUtils.isNotBlank(bean.getFacetEditionUri())) {
			clause = clause
					+ "{SELECT  ?work  FROM <http://gen.library.sh.cn/graph/instance> WHERE {?s bf:instanceOf ?work; bf:edition <"+bean.getFacetEditionUri()+">}}";
		}
		if (StringUtils.isNotBlank(bean.getFacetOrgUri())) {
			clause = clause
					+ "{SELECT ?work  FROM <http://gen.library.sh.cn/graph/item>WHERE {?item a bf:Item ;bf:heldBy <"+bean.getFacetOrgUri()+"> {?item bf:itemOf ?ins.{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {?ins bf:instanceOf ?work .}}}}}";
		}
		if (StringUtils.isNotBlank(bean.getFacetAccFlg())) {
			clause = clause
					+ "{SELECT ?work FROM <http://gen.library.sh.cn/graph/item>"
					+ " WHERE {   ?item a bf:Item ;shl:accessLevel '"+bean.getFacetAccFlg()+"';"
					+ "bf:itemOf ?ins.  "
					+ "{SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .     }}"
					+ "}}";
		}
		/*
		 * if (StringUtils.isNotBlank(bean.getOrganization())) { clause = clause
		 * +
		 * " {SELECT ?work FROM <http://gen.library.sh.cn/graph/item> WHERE {   ?item a bf:Item; bf:itemOf ?ins.  {SELECT ?ins ?work FROM <http://gen.library.sh.cn/graph/instance> WHERE {       ?ins bf:instanceOf ?work .   }}   {SELECT ?item WHERE {       {?item bf:heldBy ?hbs.            {SELECT ?hbs FROM <http://gen.library.sh.cn/graph/baseinfo> WHERE {               ?hbs shl:abbreviateName ?ab ;                     bf:label ?org .                FILTER (CONTAINS(?ab, '"
		 * + bean.getOrganization() + "') || CONTAINS(?org, '" +
		 * bean.getOrganization() + "'))" + "           }} " +
		 * "       } UNION {?item shl:DOI|bf:shelfMark ?desc . FILTER CONTAINS(?desc, '"
		 * + bean.getOrganization() + "')}" + "   }}" + "}}"; }
		 */
		return clause;
	}
}

/*
 * Location: C:\Users\chen\Desktop\pedigree-arq-1.06.jar Qualified Name:
 * cn.sh.library.pedigree.sparql.MergeSearchParts JD-Core Version: 0.6.2
 */