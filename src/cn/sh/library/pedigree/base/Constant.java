package cn.sh.library.pedigree.base;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.sh.library.pedigree.common.CodeMsgUtil;

/**
 * Created by Administrator on 14-3-5.
 */
public class Constant {
	public static String ProjectPath = "";
	public static String ProjectRealPath = "";
	private static final String HOST = "http://gen.library.sh.cn/";
	private static final String HOST_SD = "http://sd.library.sh.cn/";
	// private static final String DATA_HOST = "http://data.library.sh.cn/";
	private static final String CBA = "http://www.cba.ac.cn/";

	// Ontology
	public static final String GRAPH_VOCAB = HOST + "graph/vocab"; // 本体词表
	// public static final String GRAPH_DATA = DATA_HOST + "graph/baseinfo";
	// //数据

	// Graph
	public static final String GRAPH_PERSON = HOST + "graph/person"; // 家谱名人+责任者
	public static final String GRAPH_BASEINFO = HOST + "graph/baseinfo"; // 基础信息，朝代+姓氏表+Category
	public static final String GRAPH_INSTANCE = HOST + "graph/instance"; // 家谱实例
	public static final String GRAPH_WORK = HOST + "graph/work"; // 家谱作品
	public static final String GRAPH_PLACE = HOST + "graph/place"; // 谱籍地
	public static final String GRAPH_ANNOTATION = HOST + "graph/annotation"; // Work备注
	public static final String GRAPH_FAMILY = HOST + "graph/family"; // 家族
	public static final String GRAPH_MIGRATION = HOST + "graph/migration"; // 迁徙图
	public static final String GRAPH_TEMPLATE = HOST + "graph/profile"; // 模板
	public static final String GRAPH_GEO = CBA + "graph/geography"; // 省市县
	public static final String GRAPH_TITLE = HOST + "graph/title"; // 谱名
	public static final String GRAPH_ITEM = HOST + "graph/item"; // 谱名
	public static final String GRAPH_PERSON_SD = HOST_SD + "graph/person"; // 谱名
	public static final String GRAPH_TEMPORAL = HOST + "graph/temporal"; // 朝代

	// Prefix
	public static final String PREFIX_PERSON = HOST + "Person/";
	public static final String PREFIX_PERSON_SD = HOST_SD + "Person/";
	public static final String PREFIX_PLACE = HOST + "Place/";
	public static final String PREFIX_WORK = HOST + "Work/";
	public static final String PREFIX_INSTANCE = HOST + "Instance/";
	public static final String PREFIX_FAMILY = HOST + "Family/";
	public static final String PREFIX_FAMILYNAME = HOST + "FamilyName/";
	public static final String PREFIX_ANNOTATION = HOST + "Annotation/";
	public static final String PREFIX_TEMPORAL = HOST + "Temporal/";
	public static final String PREFIX_CATEGORY = HOST + "Category/";
	public static final String PREFIX_TITLE = HOST + "Title/";
	public static final String PREFIX_INSTANCE_TEMPORAL = HOST
			+ "Temporal/published/";

	// uri prefix
	public static final String URI_PREFIX_PERSON = "http://data.library.sh.cn/jp/entity/person/";
	public static final String URI_PREFIX_TANGHAO = "http://data.library.sh.cn/jp/authority/titleofancestraltemple/";
	public static final String URI_PREFIX_FAMILYNAME = "http://data.library.sh.cn/authority/familyname/";
	public static final String URI_PREFIX_TITLE = "http://data.library.sh.cn/jp/authority/title/";
	public static final String URI_PREFIX_WORK = "http://data.library.sh.cn/jp/resource/work/";
	public static final String URI_PREFIX_INSTANCE = "http://data.library.sh.cn/jp/resource/instance/";
	public static final String URI_PREFIX_ITEM = "http://data.library.sh.cn/jp/resource/item/";
	public static final String URI_PREFIX_CATEGORY = "http://data.library.sh.cn/vocab/binding/";
	public static final String URI_PREFIX_EDITION = "http://data.library.sh.cn/vocab/edition/";

	public Map<String, String> GRAPH_MAP = new LinkedHashMap<>();

	public static final String OAUTH_URL = CodeMsgUtil.getConfig("OAUTH_URL");

	public static final String RESPONSE_TYPE = CodeMsgUtil
			.getConfig("RESPONSE_TYPE");

	public static final String CLIENTID = CodeMsgUtil.getConfig("CLIENTID");

	public static final String REDIRECT_URI = CodeMsgUtil
			.getConfig("REDIRECT_URI");

	public static final String GRANT_TYPE = CodeMsgUtil.getConfig("GRANT_TYPE");

	public static final String CLIENT_SECRET = CodeMsgUtil
			.getConfig("CLIENT_SECRET");

	public static final String LOGIN_URL = OAUTH_URL + RESPONSE_TYPE
			+ "&client_id=" + CLIENTID + "&redirect_uri=" + REDIRECT_URI;

	public static final String HTTPS_LOGIN = CodeMsgUtil
			.getConfig("HTTPS_LOGIN");

	// Init
	public Constant() {
		/*
		 * GRAPH_MAP.put(GRAPH_PERSON, GRAPH_PERSON);
		 * GRAPH_MAP.put(GRAPH_BASEINFO, GRAPH_BASEINFO);
		 * GRAPH_MAP.put(GRAPH_INSTANCE, GRAPH_INSTANCE);
		 * GRAPH_MAP.put(GRAPH_WORK, GRAPH_WORK); GRAPH_MAP.put(GRAPH_PLACE,
		 * GRAPH_PLACE); GRAPH_MAP.put(GRAPH_ANNOTATION, GRAPH_ANNOTATION);
		 * GRAPH_MAP.put(GRAPH_FAMILY, GRAPH_FAMILY);
		 * GRAPH_MAP.put(GRAPH_MIGRATION, GRAPH_MIGRATION);
		 * GRAPH_MAP.put(GRAPH_TEMPLATE, GRAPH_TEMPLATE);
		 * GRAPH_MAP.put(GRAPH_TITLE, GRAPH_TITLE);
		 */
		GRAPH_MAP.put(GRAPH_INSTANCE, GRAPH_INSTANCE);
		GRAPH_MAP.put(GRAPH_ITEM, GRAPH_ITEM);
		GRAPH_MAP.put(GRAPH_PERSON_SD, GRAPH_PERSON_SD);
	}
}
