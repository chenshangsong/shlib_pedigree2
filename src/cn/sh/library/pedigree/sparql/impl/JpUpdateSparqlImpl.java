package cn.sh.library.pedigree.sparql.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.impl.LiteralLabelFactory;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.sparql.JpUpdateSparql;
import cn.sh.library.pedigree.sparql.Namespace;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
@GraphDefine(name = Constant.GRAPH_PERSON)
public class JpUpdateSparqlImpl extends BaseDaoImpl implements JpUpdateSparql {

	@Resource
	private StringBuffer nsPrefix;

	private Graph itemGraph = getModel("http://gen.library.sh.cn/graph/item").getGraph();

	private Graph instanceGraph = getModel("http://gen.library.sh.cn/graph/instance").getGraph();

	private Graph workGraph = getModel("http://gen.library.sh.cn/graph/work").getGraph();

	// chenss 2020-04-27 begin
	private Graph baseinfoGraph = getModel("http://gen.library.sh.cn/graph/baseinfo").getGraph();

	private Graph personGraph = getModel("http://gen.library.sh.cn/graph/person").getGraph();

	private Graph titleGraph = getModel("http://gen.library.sh.cn/graph/title").getGraph();

	// 姓氏URI，全局。
	private static String _staticFnameUri = "";
	// chenss 2020-04-27 end
	// 先祖类型
	private static String[] _staticPersonKeys = new String[] { "yuanzu", "shizu", "xianzu", "shiqianzu", "zhizu",
			"mingren" };
	private static String[] _staticPersonRoles = new String[] { "http://data.library.sh.cn/jp/vocab/ancestor/yuan-zu",
			"http://data.library.sh.cn/jp/vocab/ancestor/shi-zu", "http://data.library.sh.cn/jp/vocab/ancestor/xian-zu",
			"http://data.library.sh.cn/jp/vocab/ancestor/shi-qian-zu",
			"http://data.library.sh.cn/jp/vocab/ancestor/zhi-zu",
			"http://data.library.sh.cn/jp/vocab/ancestor/ming-ren" };

	// 用以检查同时插入多条一样数据来不及查重的问题
	private static Vector<String> persons4Sg = new Vector<String>();

	@Override
	public void insertJp(JSONObject jsonObject, String workUri, String instanceUri, JSONArray items) throws Exception {
		_staticFnameUri = "";
		// 姓氏URI列表，由于不知道对应关系，只取第一个，赋予先祖、责任者等人物。
		if (jsonObject.containsKey("familyName") && jsonObject.getJSONObject("familyName").containsKey("uriList")) {
			_staticFnameUri = jsonObject.getJSONObject("familyName").getJSONArray("uriList").getString(0);

		}
		// // 检查是否存在
		/*
		 * String chsName = jsonObject.getString("shelfMark").trim(); String birthday =
		 * jsonObject.getString("doi").trim(); String temporal =
		 * jsonObject.getString("temporal").trim(); String nativeP =
		 * jsonObject.getString("nativePlace").trim(); String key =
		 * chsName.concat(temporal).concat(nativeP); if (persons4Sg.contains(key)) {
		 * throw new Exception("该数据正在编辑中！"); } else { persons4Sg.add(key); }
		 */
		// persons4Sg.remove(key);
		insertWork(jsonObject, workUri);
		insertInstance(jsonObject, workUri, instanceUri);
		insertItems(instanceUri, items);

	}

	public void insertItems(String instanceUri, JSONArray items) throws Exception {
		try {
			List<Triple> item = new ArrayList<Triple>();
			itemGraph.getTransactionHandler().begin();
			for (int i = 0; i < items.size(); i++) {
				JSONObject object = items.getJSONObject(i);
				String uri = object.getString("itemUri");

				// shelfMark

				if (object.containsKey("shelfMark") && StringUtils.isNotBlank(object.getString("shelfMark"))) {

					item.add(buildTriple(uri, Namespace.BF.getUri() + "shelfMark",
							object.getString("shelfMark").trim()));
				}
				//description
				if (object.containsKey("description") && StringUtils.isNotBlank(object.getString("description"))) {

					item.add(buildTriple(uri, Namespace.SHL.getUri() + "description",
							object.getString("description").trim()));
				}
				// extent 册数：同 instance下的册数。
				if (object.containsKey("extent") && StringUtils.isNotBlank(object.getString("extent"))) {
					item.add(buildTriple(uri, Namespace.BF.getUri() + "extent", object.getString("extent").trim()));
				}

				// accessLevel
				if (object.containsKey("accessLevel") && StringUtils.isNotBlank(object.getString("accessLevel"))) {

					item.add(buildTriple(uri, Namespace.SHL.getUri() + "accessLevel",
							object.getString("accessLevel").trim()));
				}

				// DOI
				if (object.containsKey("DOI") && StringUtils.isNotBlank(object.getString("DOI"))) {
					item.add(buildTriple(uri, Namespace.SHL.getUri() + "DOI", object.getString("DOI").trim()));
				}

				// heldBy
				if (object.containsKey("heldBy") && StringUtils.isNotBlank(object.getString("heldBy"))) {
					item.add(buildTriple(uri, Namespace.BF.getUri() + "heldBy", object.getString("heldBy").trim()));
				}
				// carrierCategory 纸本 http://data.library.sh.cn/vocab/carrierCategory/zhi-ben
				// 20220907
				if (object.containsKey("carrierCategory")
						&& StringUtils.isNotBlank(object.getString("carrierCategory"))) {
					item.add(buildTriple(uri, Namespace.BF.getUri() + "carrierCategory",
							object.getString("carrierCategory").trim()));
				}

				// hasFullImg
				if (object.containsKey("hasFullImg") && StringUtils.isNotBlank(object.getString("hasFullImg"))) {
					item.add(buildTriple(uri, Namespace.SHL.getUri() + "hasFullImg",
							object.getString("hasFullImg").trim()));

				}
//
				item.add(buildTriple(uri, Namespace.BF.getUri() + "itemOf", instanceUri));

				item.add(buildTriple(uri, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "Item"));

			}

			GraphUtil.add(itemGraph, item);
			itemGraph.getTransactionHandler().commit();

		} catch (Exception e) {
			System.out.println("insertItem:插入失败。" + e);
			itemGraph.getTransactionHandler().abort();
			throw new Exception(e);
		} finally {
			itemGraph.getTransactionHandler().abort();
		}
		System.out.println("insertItem:插入成功。");
	}

	// 添加instance
	public void insertInstance(JSONObject jsonObject, String workUri, String instanceUri) throws Exception {

		List<Triple> instance = new ArrayList<Triple>();
		instanceGraph.getTransactionHandler().begin();
		// Node空节点三元组 家谱编目新增属性 空节点：chenss 20200616
		List<Triple> noteTriple = new ArrayList<Triple>();
		try {

			instance.add(buildTriple(instanceUri, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "Instance"));

			// 朝代literal
			if (jsonObject.containsKey("temporalValue")
					&& StringUtils.isNotBlank(jsonObject.getString("temporalValue"))) {
				instance.add(buildTriple(instanceUri, Namespace.SHL.getUri() + "temporalValue",
						jsonObject.getString("temporalValue").trim()));
			}

			// 朝代 Int
			if (jsonObject.containsKey("temporalValueInt")) {
				String strInt = jsonObject.getString("temporalValueInt");
				// 如果不为空，且为正整数 且长度小于5.
				if (!StringUtil.isBlank(strInt) && StringUtilC.isNumber(strInt) && strInt.length() < 5) {
					instance.add(buildTriple(instanceUri, Namespace.SHL.getUri() + "temporalValue", strInt.trim(),
							XSDDatatype.XSDinteger));
				}

			}
			// 册数
			if (jsonObject.containsKey("extent") && StringUtils.isNotBlank(jsonObject.getString("extent"))) {
				instance.add(buildTriple(instanceUri, Namespace.BF.getUri() + "extent",
						jsonObject.getString("extent").trim()));
			}

			instance.add(buildTriple(instanceUri, Namespace.BF.getUri() + "instanceOf", workUri));

			// 朝代uri
			if (jsonObject.containsKey("temporalUri") && StringUtils.isNotBlank(jsonObject.getString("temporalUri"))) {
				instance.add(buildTriple(instanceUri, Namespace.SHL.getUri() + "temporal",
						jsonObject.getString("temporalUri").trim()));
			}
			// categoryUri
			if (jsonObject.containsKey("categoryUri") && StringUtils.isNotBlank(jsonObject.getString("categoryUri"))) {

				instance.add(buildTriple(instanceUri, Namespace.BF.getUri() + "category",
						jsonObject.getString("categoryUri").trim()));
			}

			// edition
			if (jsonObject.containsKey("editionUri") && StringUtils.isNotBlank(jsonObject.getString("editionUri"))) {

				instance.add(buildTriple(instanceUri, Namespace.BF.getUri() + "edition",
						jsonObject.getString("editionUri").trim()));
			}
			/*--------------------------------------------家谱编目新增属性 chenss 20200616 begin-----------------------------------------------------------------*/

			// 出版发行者 dc:publisher: 家谱编目新增属性 chenss 20200616
			if (jsonObject.containsKey("publisher") && StringUtils.isNotBlank(jsonObject.getString("publisher"))) {

				instance.add(buildTriple(instanceUri, Namespace.DC.getUri() + "publisher",
						jsonObject.getString("publisher").trim()));
			}

			// 版本项说明:bf:note 家谱编目新增属性 空节点：chenss 20200616
			// 版本项说明 bf:note shl:Resource bf:Note bf:Instance 空节点
			// bf:noteType “版本项说明”
			// rdfs:label Literal
			// bf:note[a bf:Note;bf:noteType "版本项说明";rdfs:label
			// jsonObject.getString("note")]
			if (jsonObject.containsKey("ins_banbenx") && StringUtils.isNotBlank(jsonObject.getString("ins_banbenx"))) {

				// 获取空节点的主语值 ，nodeID://b+6为数字随机码：如：nodeID://b347192
				String nodeSins_banbenx = StringUtilC.getRandomForBlankNode();
				noteTriple.add(buildTriple(nodeSins_banbenx, Namespace.RDFS.getUri() + "label",
						jsonObject.getString("ins_banbenx").trim()));
				noteTriple.add(buildTriple(nodeSins_banbenx, Namespace.BF.getUri() + "noteType", "版本项说明"));
				noteTriple.add(
						buildTriple(nodeSins_banbenx, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "Note"));
				// 空节点属性值，家谱编目新增属性 空节点：chenss 20200616
				instance.add(buildBlankNodeTriple(instanceUri, Namespace.BF.getUri() + "note", nodeSins_banbenx));
			}

			// bf:note[a bf:Note;bf:noteType "附注项";rdfs:label
			// jsonObject.getString("fuzhu")]：附注项
			if (jsonObject.containsKey("ins_fuzhux") && StringUtils.isNotBlank(jsonObject.getString("ins_fuzhux"))) {

				// 获取空节点的主语值 ，nodeID://b+6为数字随机码：如：nodeID://b347192
				String nodeSins_fuzhux = StringUtilC.getRandomForBlankNode();
				noteTriple.add(buildTriple(nodeSins_fuzhux, Namespace.RDFS.getUri() + "label",
						jsonObject.getString("ins_fuzhux").trim()));
				noteTriple.add(buildTriple(nodeSins_fuzhux, Namespace.BF.getUri() + "noteType", "附注项"));
				noteTriple.add(
						buildTriple(nodeSins_fuzhux, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "Note"));
				// 空节点属性值，家谱编目新增属性 空节点：chenss 20200616
				instance.add(buildBlankNodeTriple(instanceUri, Namespace.BF.getUri() + "note", nodeSins_fuzhux));
			}
			/*--------------------------------------------家谱编目新增属性 chenss 20200616 end-----------------------------------------------------------------*/

			// 家谱编目新增属性 空节点：chenss 20200616
			GraphUtil.add(instanceGraph, noteTriple);

			GraphUtil.add(instanceGraph, instance);

			instanceGraph.getTransactionHandler().commit();

		} catch (Exception e) {
			System.out.println("insertInstance:插入失败。" + e);
			instanceGraph.getTransactionHandler().abort();
			throw new Exception(e);
		} finally {
			instanceGraph.getTransactionHandler().abort();
		}
		System.out.println("insertInstance:插入成功。");
	}

	// 添加work
	public void insertWork(JSONObject jsonObject, String workUri) throws Exception {

		List<Triple> workbase = new ArrayList<Triple>();
		try {
			workbase.add(buildTriple(workUri, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "Work"));
			String titlePlace = StringUtils.isEmpty(jsonObject.getString("placeValue"))?"": "（" + jsonObject.getString("placeValue") + "）";
			workbase.add(buildTriple(workUri, Namespace.DC.getUri() + "title",StringUtilC.getChs(jsonObject.getString("title").trim() +titlePlace)+ "@chs"));
			workbase.add(buildTriple(workUri, Namespace.DC.getUri() + "title",StringUtilC.getCht(jsonObject.getString("title").trim() + titlePlace)+ "@cht"));
			workGraph.getTransactionHandler().begin();
			GraphUtil.add(workGraph, workbase);
			insertWorkProperties(jsonObject, workUri);
			workGraph.getTransactionHandler().commit();
			// creator
			insertCreatorOfWork(jsonObject, workUri);
			// 堂号插入 chenss 2020-04-28
			insertTanghaoOfWork(jsonObject, workUri);
			// 元祖/鼻祖
			insertPersonOfWork(jsonObject, workUri);
			// title
			insertTitleOfWork(jsonObject, workUri);

		} catch (Exception e) {
			System.out.println("insertWork :插入失败。" + e);
			personGraph.getTransactionHandler().abort();
			baseinfoGraph.getTransactionHandler().abort();
			titleGraph.getTransactionHandler().abort();
			workGraph.getTransactionHandler().abort();
			throw new Exception(e);
		} finally {
			personGraph.getTransactionHandler().abort();
			baseinfoGraph.getTransactionHandler().abort();
			titleGraph.getTransactionHandler().abort();
			workGraph.getTransactionHandler().abort();
		}
		System.out.println("insertWork :插入成功。");
	}

	// 添加work属性
	public void insertWorkProperties(JSONObject jsonObject, String workUri) throws Exception {
		List<Triple> triples = new ArrayList<Triple>();

		// identifiedBy
		if (jsonObject.containsKey("identifiedBy") && StringUtils.isNotBlank(jsonObject.getString("identifiedBy"))) {
			triples.add(
					buildTriple(workUri, Namespace.BF.getUri() + "identifiedBy", jsonObject.getString("identifiedBy")));
		}
		// 姓氏URI列表 chenss 2020-04-28
		if (jsonObject.containsKey("familyName") && jsonObject.getJSONObject("familyName").containsKey("label")
				&& jsonObject.getJSONObject("familyName").containsKey("uriList")) {
			// 姓氏文本
			triples.add(buildTriple(workUri, Namespace.DC.getUri() + "subject",
					jsonObject.getJSONObject("familyName").getString("label")));
			// 获取姓氏列表 List
			JSONArray _fammilyNameUriList = jsonObject.getJSONObject("familyName").getJSONArray("uriList");
			// 循环列表，逐个加入。
			for (int i = 0; i < _fammilyNameUriList.size(); i++) {
				triples.add(buildTriple(workUri, Namespace.BF.getUri() + "subject", _fammilyNameUriList.getString(i)));
			}

		}
		// description
		if (jsonObject.containsKey("description") && StringUtils.isNotBlank(jsonObject.getString("description"))) {
			triples.add(
					buildTriple(workUri, Namespace.SHL.getUri() + "description", jsonObject.getString("description")));
		}
		// place
		if (jsonObject.containsKey("place") && StringUtils.isNotBlank(jsonObject.getString("place"))) {
			triples.add(buildTriple(workUri, Namespace.SHL.getUri() + "place", jsonObject.getString("place")));
		}
		// creatorValue 文本
		if (jsonObject.containsKey("creatorNameValue")
				&& StringUtils.isNotBlank(jsonObject.getString("creatorNameValue"))) {
			triples.add(buildTriple(workUri, Namespace.DC.getUri() + "creator",
					StringUtilC.getChs(jsonObject.getString("creatorNameValue").trim()) + "@chs"));
			triples.add(buildTriple(workUri, Namespace.DC.getUri() + "creator",
					StringUtilC.getCht(jsonObject.getString("creatorNameValue").trim()) + "@cht"));
		}
		// placeValue
		if (jsonObject.containsKey("placeValue") && StringUtils.isNotBlank(jsonObject.getString("placeValue"))) {
			triples.add(
					buildTriple(workUri, Namespace.SHL.getUri() + "placeValue", jsonObject.getString("placeValue")));
		}
		// workAccFlag 是否开放查询标记 chenss 20230704：1：不开放。
				if (jsonObject.containsKey("workAccFlag") && StringUtils.isNotBlank(jsonObject.getString("workAccFlag"))) {
					triples.add(
							buildTriple(workUri, Namespace.DC.getUri() + "workAccFlag", jsonObject.getString("workAccFlag")));
				}
		
		// 将三元组添加到 work的graph下。
		GraphUtil.add(workGraph, triples);
	}

	// work中的相关先祖、名人属性
	public void insertPersonOfWork(JSONObject jsonObject, String workUri) throws Exception {

		for (int i = 0; i < _staticPersonKeys.length; i++) {
			String personKey = _staticPersonKeys[i];
			String personRole = _staticPersonRoles[i];
			List<Triple> person = new ArrayList<Triple>();
			// bf:label 得到先祖、鼻祖等List信息
			if (jsonObject.containsKey(personKey) && StringUtils.isNotBlank(personKey)) {
				// 人物列表
				JSONArray _personList = JSONArray.fromObject(jsonObject.get(personKey));
				for (int j = 0; j < _personList.size(); j++) {
					// 得到每个人
					JSONObject personObject = _personList.getJSONObject(j);
					String _puri = Constant.URI_PREFIX_PERSON + StringUtilC.getRandomUriValue(16);
					String _pname = JSONObject.fromObject(personObject).getString("pname");
					if (StringUtils.isNotBlank(_pname)) {
						person.add(buildTriple(_puri, Namespace.BF.getUri() + "label",
								StringUtilC.getChs(_pname.trim()) + "@chs"));
						person.add(buildTriple(_puri, Namespace.BF.getUri() + "label",
								StringUtilC.getCht(_pname.trim()) + "@cht"));
						person.add(buildTriple(_puri, Namespace.SHL.getUri() + "roleOfFamily", personRole));
						person.add(buildTriple(_puri, Namespace.SHL.getUri() + "relatedWork", workUri));
						// shl:temporal
						if (personObject.containsKey("temporal")
								&& StringUtils.isNotBlank(personObject.getString("temporal"))) {
							person.add(buildTriple(_puri, Namespace.SHL.getUri() + "temporal",
									personObject.getString("temporal")));
						}
						// shl:temporalValue
						if (personObject.containsKey("temporalValue")
								&& StringUtils.isNotBlank(personObject.getString("temporalValue"))) {
							person.add(buildTriple(_puri, Namespace.SHL.getUri() + "temporalValue",
									personObject.getString("temporalValue")));
						}
						// foaf:familyName
						if (StringUtils.isNotBlank(_staticFnameUri)) {
							person.add(buildTriple(_puri, Namespace.FOAF.getUri() + "familyName", _staticFnameUri));
						}
						person.add(
								buildTriple(_puri, Namespace.RDF.getUri() + "type", Namespace.SHL.getUri() + "Person"));
					}

				}

			}
			personGraph.getTransactionHandler().begin();
			GraphUtil.add(personGraph, person);
			personGraph.getTransactionHandler().commit();
		}

	}

	// work中堂号属性插入 chenss 2020-04-28
	public void insertTanghaoOfWork(JSONObject objectAll, String workUri) throws Exception {
		try {
			List<Triple> tanghao = new ArrayList<Triple>();
			List<Triple> work = new ArrayList<Triple>();
			JSONArray tanghaoList = objectAll.getJSONArray("tanghaoList");
			// 如果姓氏有值传入，则发布堂号，否则堂号无法发布。chenss 2020-04-28
			if (!StringUtilC.isEmpty(_staticFnameUri)) {
				for (int i = 0; i < tanghaoList.size(); i++) {
					String tanghaoUri = Constant.URI_PREFIX_TANGHAO + StringUtilC.getRandomUriValue(16);

					// bf:label
					if (StringUtils.isNotBlank(tanghaoList.getString(i))) {
						tanghao.add(buildTriple(tanghaoUri, Namespace.BF.getUri() + "label",
								StringUtilC.getChs(tanghaoList.getString(i).trim()) + "@chs"));
						tanghao.add(buildTriple(tanghaoUri, Namespace.BF.getUri() + "label",
								StringUtilC.getCht(tanghaoList.getString(i).trim()) + "@cht"));
						// 姓氏URI,堂号、姓氏、都是多值输入，不知道堂号和姓氏的关系，只取一个姓氏。
						tanghao.add(buildTriple(tanghaoUri, Namespace.SHL.getUri() + "familyName", _staticFnameUri));

					}
					// rdf:type

					tanghao.add(buildTriple(tanghaoUri, Namespace.RDF.getUri() + "type",
							Namespace.SHL.getUri() + "TitleOfAncestralTemple"));
					work.add(buildTriple(workUri, Namespace.BF.getUri() + "subject", tanghaoUri));

				}
				baseinfoGraph.getTransactionHandler().begin();
				GraphUtil.add(baseinfoGraph, tanghao);
				baseinfoGraph.getTransactionHandler().commit();
				workGraph.getTransactionHandler().begin();
				GraphUtil.add(workGraph, work);
				workGraph.getTransactionHandler().commit();
			}

		} catch (Exception e) {

			throw new Exception(e);
		}

	}

	// work中的creator属性
	public void insertCreatorOfWork(JSONObject jsonObject, String workUri) throws Exception {
		try {
			if (jsonObject.containsKey("creator")) {
				JSONArray jsonArray = jsonObject.getJSONArray("creator");
				List<Triple> creator = new ArrayList<Triple>();
				List<Triple> work = new ArrayList<Triple>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String uri = Constant.URI_PREFIX_PERSON + StringUtilC.getRandomUriValue(16);
					// work属性增加 creators属性
					work.add(buildTriple(workUri, Namespace.BF.getUri() + "creator", uri));
					// 开始整理发布责任者
					// bf:label
					if (object.containsKey("creatorName") && StringUtils.isNotBlank(object.getString("creatorName"))) {
						creator.add(buildTriple(uri, Namespace.BF.getUri() + "label",
								StringUtilC.getChs(object.getString("creatorName").trim()) + "@chs"));
						creator.add(buildTriple(uri, Namespace.BF.getUri() + "label",
								StringUtilC.getCht(object.getString("creatorName").trim()) + "@cht"));
					}
					// foaf:familyName
					if (StringUtils.isNotBlank(_staticFnameUri)) {
						creator.add(buildTriple(uri, Namespace.FOAF.getUri() + "familyName", _staticFnameUri));
					}
					// shl:relatedWork
					creator.add(buildTriple(uri, Namespace.SHL.getUri() + "relatedWork", workUri));
					// bf:role
					if (object.containsKey("creatorRole") && StringUtils.isNotBlank(object.getString("creatorRole"))) {
						creator.add(buildTriple(uri, Namespace.BF.getUri() + "role", object.getString("creatorRole")));
					}
					// shl:temporal
					if (object.containsKey("temporal") && StringUtils.isNotBlank(object.getString("temporal"))) {
						creator.add(
								buildTriple(uri, Namespace.SHL.getUri() + "temporal", object.getString("temporal")));
					}
					// shl:temporalValue
					if (object.containsKey("temporalValue")
							&& StringUtils.isNotBlank(object.getString("temporalValue"))) {
						creator.add(buildTriple(uri, Namespace.SHL.getUri() + "temporalValue",
								object.getString("temporalValue")));
					}
					creator.add(buildTriple(uri, Namespace.RDF.getUri() + "type", Namespace.SHL.getUri() + "Person"));
				}
				personGraph.getTransactionHandler().begin();
				GraphUtil.add(personGraph, creator);
				personGraph.getTransactionHandler().commit();
				workGraph.getTransactionHandler().begin();
				GraphUtil.add(workGraph, work);
				workGraph.getTransactionHandler().commit();
			}
		} catch (Exception e) {
			System.out.println("insertWork SQLException" + e);
			throw new Exception(e);
		}

	}

	// work中的title属性
	public void insertTitleOfWork(JSONObject jsonObject, String workUri) throws Exception {
		List<Triple> title = new ArrayList<Triple>();
		List<Triple> work = new ArrayList<Triple>();
		String titleUri = Constant.URI_PREFIX_TITLE + StringUtilC.getRandomUriValue(16);

		int cnts = 0;
		// bf:label
		if (jsonObject.containsKey("title") && StringUtils.isNotBlank(jsonObject.getString("title"))) {
			title.add(buildTriple(titleUri, Namespace.BF.getUri() + "label",
					StringUtilC.getChs(jsonObject.getString("title").trim()) + "@chs"));
			title.add(buildTriple(titleUri, Namespace.BF.getUri() + "label",
					StringUtilC.getCht(jsonObject.getString("title").trim()) + "@cht"));
			cnts++;
		}
		/*--------------------------------------------家谱编目新增属性 chenss 20200616 begin-----------------------------------------------------------------*/
		// shl:volumes 卷数，家谱编目新增属性：chenss 20200616
		if (jsonObject.containsKey("volumes") && StringUtils.isNotBlank(jsonObject.getString("volumes"))) {
			title.add(buildTriple(titleUri, Namespace.SHL.getUri() + "volumes",
					StringUtilC.getChs(jsonObject.getString("volumes").trim()) + "@chs"));
			title.add(buildTriple(titleUri, Namespace.SHL.getUri() + "volumes",
					StringUtilC.getCht(jsonObject.getString("volumes").trim()) + "@cht"));
			cnts++;
		}
		/*--------------------------------------------家谱编目新增属性 chenss 20200616 end-----------------------------------------------------------------*/
		// rdf:type
		if (cnts > 0) {
			title.add(buildTriple(titleUri, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "Title"));
			title.add(buildTriple(titleUri, Namespace.RDF.getUri() + "type", Namespace.BF.getUri() + "WorkTitle"));
			work.add(buildTriple(workUri, Namespace.BF.getUri() + "title", titleUri));
		}
		// http://gen.library.sh.cn/graph/title
		titleGraph.getTransactionHandler().begin();
		GraphUtil.add(titleGraph, title);
		titleGraph.getTransactionHandler().commit();
		workGraph.getTransactionHandler().begin();
		GraphUtil.add(workGraph, work);
		workGraph.getTransactionHandler().commit();
	}

	private Triple buildTriple(String s, String p, String o) {
		Node oNode;
		// 如果宾语是URI，或者空节点，则都创建URI格式的宾语
		if (o.startsWith("http://")) {
			oNode = NodeFactory.createURI(o);
		} else if (o.contains("@")) {
			String o0 = o.split("@")[0];
			String olang = o.split("@")[1];
			oNode = NodeFactory.createLiteral(LiteralLabelFactory.create(o0, olang, true));
			// NodeFactory.createLiteral("", new
			// BaseDatatype("http://www.w3.org/2001/XMLSchema#integer"));
		} else {
			// 不带语言标识，并且不为XML格式，如不存储："测试090501"^^<http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral>。只存储
			// “测试09050”
			oNode = NodeFactory.createLiteral(LiteralLabelFactory.create(o, null, false));

		}

		Triple t = new Triple(NodeFactory.createURI(s), NodeFactory.createURI(p), oNode);
		return t;
	}

	/**
	 * 带格式的创建三元组方式，如 string、int chenss/2020-04-26
	 * 
	 * @param s
	 * @param p
	 * @param o
	 * @param type
	 * @return
	 */
	private Triple buildTriple(String s, String p, String o, XSDDatatype type) {
		Node oNode;
		// 如果宾语是URI，或者空节点，则都创建URI格式的宾语
		if (o.startsWith("http://")) {
			oNode = NodeFactory.createURI(o);
		} else if (o.contains("@")) {
			String o0 = o.split("@")[0];
			String olang = o.split("@")[1];
			oNode = NodeFactory.createLiteral(LiteralLabelFactory.create(o0, olang, true));
		} else {
			// 带 类型的传参方式。
			if (type != null) {
				oNode = NodeFactory.createLiteral(o, type);
			} else {
				// 不带语言标识，并且不为XML格式，如不存储："测试090501"^^<http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral>。只存储
				// “测试09050”
				oNode = NodeFactory.createLiteral(LiteralLabelFactory.create(o, null, false));
			}

		}

		Triple t = new Triple(NodeFactory.createURI(s), NodeFactory.createURI(p), oNode);

		return t;
	}

	/**
	 * 创建宾语是空节点，或者URI的
	 * 
	 * @param s
	 * @param p
	 * @param o
	 * @return
	 */
	private Triple buildBlankNodeTriple(String s, String p, String o) {
		Triple t = new Triple(NodeFactory.createURI(s), NodeFactory.createURI(p), NodeFactory.createURI(o));
		return t;
	}

	@Override
	public void updateJp(JSONObject jsonObject, JSONArray items) throws Exception {
		_staticFnameUri = "";
		// 姓氏URI列表，由于不知道对应关系，只取第一个，赋予先祖、责任者等人物。
		if (jsonObject.containsKey("familyName") && jsonObject.getJSONObject("familyName").containsKey("uriList")) {
			_staticFnameUri = jsonObject.getJSONObject("familyName").getJSONArray("uriList").getString(0);

		}
		// 修改数据类型区分
		String updateType = jsonObject.getString("updateType");
		switch (updateType) {
		case "0":
			updateWorkProperties(jsonObject);
			// 更新 Instance 中的 extent
			updateInstanceProperties(jsonObject);
			// 更新先祖、名人
			updatePersonProperties(jsonObject);
			// 更新item
			updateItemProperties(jsonObject, items);
			break;
		case "1":
			// 更新先祖、名人
			updatePersonProperties(jsonObject);
			break;
		case "2":
			// 更新 Instance 中的 extent
			updateInstanceProperties(jsonObject);
			// 更新item
			updateItemProperties(jsonObject, items);
			break;
		default:
			// 更新item
			updateItemProperties(jsonObject, items);
			break;
		}

	}

	// 先祖、名人等人物更新：家谱编目系统使用。
	public void updatePersonProperties(JSONObject jsonObject) throws Exception {
		try {
			personGraph.getTransactionHandler().begin();// 开启事务
			String workUri = jsonObject.getString("workUri");
			for (int i = 0; i < _staticPersonKeys.length; i++) {
				if (jsonObject.containsKey(_staticPersonKeys[i])) {
					// 得到先祖、名人等列表
					JSONArray _personList = jsonObject.getJSONArray(_staticPersonKeys[i]);
					for (int j = 0; j < _personList.size(); j++) {
						JSONObject _objPerson = _personList.getJSONObject(j);
						String puri = _objPerson.optString("puri");
						String pname = _objPerson.optString("pname");
						String temporal = _objPerson.optString("temporal");
						String temporalValue = _objPerson.optString("temporalValue");
						// 如果URI不为空，值为空，则进行删除
						if (StringUtils.isNotBlank(puri)) {
							// 删除主语为 puri的所有三元组
							Iterator<Triple> _trips = itemGraph
									.find(new Triple(NodeFactory.createURI(puri), Node.ANY, Node.ANY));
							// 删除整条先祖、名人数据
							GraphUtil.delete(personGraph, _trips);

						}
						// 如果先祖、名人名字不为空，则进行插入
						if (StringUtils.isNotBlank(pname)) {
							// 判断URI是否有值，如果没值，则赋新值，否则直接用旧Uri直接插入数据
							String inertPersonUri = StringUtils.isBlank(puri)
									? Constant.URI_PREFIX_PERSON + StringUtilC.getRandomUriValue(16)
									: puri;// 新作人物，生成新URI

							List<Triple> person = new ArrayList<Triple>();
							String personRole = _staticPersonRoles[i];// 先祖、名人role值，和personKeys索引对应。
							person.add(buildTriple(inertPersonUri, Namespace.BF.getUri() + "label",
									StringUtilC.getChs(pname.trim()) + "@chs"));
							person.add(buildTriple(inertPersonUri, Namespace.BF.getUri() + "label",
									StringUtilC.getCht(pname.trim().trim()) + "@cht"));
							person.add(
									buildTriple(inertPersonUri, Namespace.SHL.getUri() + "roleOfFamily", personRole));
							person.add(buildTriple(inertPersonUri, Namespace.SHL.getUri() + "relatedWork", workUri));

							if (StringUtils.isNotBlank(temporal)) {
								person.add(buildTriple(inertPersonUri, Namespace.SHL.getUri() + "temporal", temporal));
							}

							if (StringUtils.isNotBlank(temporalValue)) {
								person.add(buildTriple(inertPersonUri, Namespace.SHL.getUri() + "temporalValue",
										temporalValue));
							}
							// foaf:familyName
							if (StringUtils.isNotBlank(_staticFnameUri)) {
								person.add(buildTriple(inertPersonUri, Namespace.FOAF.getUri() + "familyName",
										_staticFnameUri));
							}
							person.add(buildTriple(inertPersonUri, Namespace.RDF.getUri() + "type",
									Namespace.SHL.getUri() + "Person"));
							GraphUtil.add(personGraph, person);
						}
					}

				}
			}

			personGraph.getTransactionHandler().commit();// 提交事务
			System.out.println("1:更新先祖、名人成功。");
		} catch (Exception e) {
			personGraph.getTransactionHandler().abort();// 终止事务
			System.out.println("updatePersonProperties-Exception:" + e);
			throw new Exception(e);
		}

	}

	// 添加work属性
	public void updateWorkProperties(JSONObject jsonObject) throws Exception {
		String workUri = jsonObject.getString("workUri");
		List<Triple> triples = new ArrayList<Triple>();
		// 将三元组添加到 work的graph下。

		workGraph.getTransactionHandler().begin();

		// identifiedBy
		if (jsonObject.containsKey("identifiedBy") && StringUtils.isNotBlank(jsonObject.getString("identifiedBy"))) {
			// 删除Work下的identifiedBy
			deleteProperties(workUri, Namespace.BF.getUri() + "identifiedBy", null, workGraph);
			triples.add(
					buildTriple(workUri, Namespace.BF.getUri() + "identifiedBy", jsonObject.getString("identifiedBy")));
		}
		// work_description
		if (jsonObject.containsKey("work_description")
				&& StringUtils.isNotBlank(jsonObject.getString("work_description"))) {
			// 删除Work下的description
			deleteProperties(workUri, Namespace.SHL.getUri() + "description", null, workGraph);
			triples.add(buildTriple(workUri, Namespace.SHL.getUri() + "description",
					jsonObject.getString("work_description")));
		}
		GraphUtil.add(workGraph, triples);
		workGraph.getTransactionHandler().commit();
		System.out.println("4:更新Work成功。");
	}

	

	// 添加work属性
	public void updateInstanceProperties(JSONObject jsonObject) throws Exception {
		String instanceUri = jsonObject.getString("instanceUri");
		List<Triple> triples = new ArrayList<Triple>();

		// 将三元组添加到 work的graph下。
		instanceGraph.getTransactionHandler().begin();
		// extent
		if (jsonObject.containsKey("extent") && StringUtils.isNotBlank(jsonObject.getString("extent"))) {
			// 删除Instance下的description
			deleteProperties(instanceUri, Namespace.BF.getUri() + "extent", null, instanceGraph);
			triples.add(buildTriple(instanceUri, Namespace.BF.getUri() + "extent", jsonObject.getString("extent")));
		}

		GraphUtil.add(instanceGraph, triples);
		instanceGraph.getTransactionHandler().commit();
		System.out.println("3:更新Instance成功。");
	}

	// Item信息删除/更新：家谱编目系统使用。
	public void updateItemProperties(JSONObject jsonObject, JSONArray items) throws Exception {
		try {
			itemGraph.getTransactionHandler().begin();// 开启事务
			String strInsUri = jsonObject.getString("instanceUri");
			// 得到宾语是strInsUri值的的三元组，itemOf是传入的instance值的所有旧Item先删除。
			Triple t = new Triple(Node.ANY, NodeFactory.createURI(Namespace.BF.getUri() + "itemOf"),
					NodeFactory.createURI(strInsUri));
			Iterator<Triple> tall = itemGraph.find(t);
			// 循环删除所有Item.
			while (tall.hasNext()) {
				// 得到的oldItemUri
				String oldItemUri = tall.next().getSubject().getURI();
				// 利用oldItemUri创建Triple对象进行删除oldItem所有信息。
				Triple itemT = new Triple(NodeFactory.createURI(oldItemUri), Node.ANY, Node.ANY);

				GraphUtil.delete(itemGraph, itemGraph.find(itemT));
				// System.out.println(oldItemUri + "： 删除成功。");
			}
			itemGraph.getTransactionHandler().commit();// 提交事务
			// 插入item新数据
			insertItems(strInsUri, items);
			System.out.println("2:更新Item成功。");
		} catch (Exception e) {
			itemGraph.getTransactionHandler().abort();// 终止事务
			System.out.println("updatePersonProperties-Exception:" + e);
			throw new Exception(e);
		}

	}

	@Override
	public void deleteJp(String workUri, String instanceUri) throws Exception {
		try {

			// 删除人物
			deleteProperties(null, Namespace.SHL.getUri() + "relatedWork", workUri, personGraph);
			// 删除堂号
			// 1、得到堂号URI三元组
			Triple tanghaoT = new Triple(NodeFactory.createURI(workUri),
					NodeFactory.createURI(Namespace.BF.getUri() + "subject"), Node.ANY);
			deleteByTriple(tanghaoT, baseinfoGraph, true);
			// System.out.println("堂号删除：" + tanghaoT.toString());
			// 删除题名
			// 1、得到题名URI三元组
			Triple titleT = new Triple(NodeFactory.createURI(workUri),
					NodeFactory.createURI(Namespace.BF.getUri() + "title"), Node.ANY);
			deleteByTriple(titleT, titleGraph, false);
			// System.out.println("题名删除：" + tanghaoT.toString());
			// 删除Item
			deleteProperties(null, Namespace.BF.getUri() + "itemOf", instanceUri, itemGraph);
			// System.out.println("item删除：" + instanceUri.toString());
			// 删除Instance
			deleteProperties(instanceUri,  Namespace.BF.getUri() + "note", null, instanceGraph);
			deleteProperties(instanceUri, null, null, instanceGraph);
			// System.out.println("instance删除：" + instanceUri.toString());
			// 删除Work
			deleteProperties(workUri, null, null, workGraph);
			// System.out.println("work删除：" + workUri.toString());

			// System.out.println("3:全部刪除成功。");

		} catch (Exception e) {
			// personGraph.getTransactionHandler().abort();// 终止事务
			System.out.println("updatePersonProperties-Exception:" + e);
			throw new Exception(e);
		}

	}

	/**
	 * 根据 triple 删除 数据
	 * 
	 * @param triple
	 * @param g
	 * @param iftanghao
	 */
	private void deleteByTriple(Triple triple, Graph g, Boolean iftanghao) {
		Iterator<Triple> Tall = g.find(triple);
		g.getTransactionHandler().begin();// 开启事务
		// 循环删除所有Item.
		while (Tall.hasNext()) {
			// 得到的oldItemUri
			String oldItemUri = Tall.next().getObject().getURI();
			if (iftanghao) {
				// 如果是堂号URI
				if (oldItemUri.contains("titleofancestraltemple")) {
					// 利用oldItemUri创建Triple对象进行删除oldItem所有信息。
					Triple tanghaoTemp = new Triple(NodeFactory.createURI(oldItemUri), Node.ANY, Node.ANY);
					GraphUtil.delete(g, g.find(tanghaoTemp));
				}
			}

		}
		g.getTransactionHandler().commit();// 提交事务
	}

	/**
	 * 根据多重条件删除三元组
	 * 
	 * @param s
	 * @param p
	 * @param o
	 * @param g
	 */
	private void deleteProperties(String s, String p, String o, Graph g) {

		g.getTransactionHandler().begin();// 开启事务
		// 如果主语为空
		if (StringUtils.isBlank(s)) {
			// 得到宾语是XXX值的的三元组
			Triple t = new Triple(Node.ANY, NodeFactory.createURI(p), NodeFactory.createURI(o));
			Iterator<Triple> tall = g.find(t);
			// 循环删除所有传入对象.
			while (tall.hasNext()) {
				// 得到旧对象的主语
				String olds = tall.next().getSubject().getURI();
				// 利用olds创建Triple对象进行删除旧数据所有信息。
				Triple personT = new Triple(NodeFactory.createURI(olds), Node.ANY, Node.ANY);
				GraphUtil.delete(g, g.find(personT));
				// System.out.println(olds + "： 删除成功。");
			}
		} else {// 如果主语不为空
			Iterator<Triple> _trips = null;
			// 删除主语为 XXX的所有三元组
			if (StringUtils.isBlank(p)) {
				_trips = g.find(new Triple(NodeFactory.createURI(s), Node.ANY, Node.ANY));
			} else {// 如果主语、谓语都不为空，则进行删除
				_trips = g.find(new Triple(NodeFactory.createURI(s), NodeFactory.createURI(p), Node.ANY));
			}
			// 删除整条数据
			GraphUtil.delete(g, _trips);
		}
		g.getTransactionHandler().commit();// 提交事务
	}

	@Override
	public void updateJpWorkAccFlag(JSONObject jsonObject) throws Exception {
		// 更新work停用标记：chenss20221009
		// 更新 Work的停用标记
				String workUri = jsonObject.getString("workUri");
				String accFlag = jsonObject.getString("workAccFlag");
				List<Triple> triples = new ArrayList<Triple>();
				// 将三元组添加到 work的graph下。
				workGraph.getTransactionHandler().begin();
				// 先删除Work下的accFlag，即：启用
				deleteProperties(workUri, Namespace.DC.getUri() + "workAccFlag", null, workGraph);
				// accFlag 1:前台禁用。2，前台禁用+后台查重禁用
				if (StringUtils.isNotBlank(accFlag) ) {
					triples.add(buildTriple(workUri, Namespace.DC.getUri() + "workAccFlag", accFlag));
				}
				GraphUtil.add(workGraph, triples);
				workGraph.getTransactionHandler().commit();
				System.out.println("5:更新Work访问标记成功。");
		
	}
	
	// Item信息删除/更新：家谱编目系统使用。 20221017
	@Override
	public void chageItemOf(JSONObject jsonObject) throws Exception {
		try {
			itemGraph.getTransactionHandler().begin();// 开启事务
			String itemUri = jsonObject.getString("itemUri");
			String delFlg = jsonObject.getString("delFlg");
			if(!StringUtil.isBlank(delFlg)) {//删除Item
				deleteProperties(itemUri, null, null, itemGraph);
				System.out.println("删除Item成功。");
			}
			else {//更新itemOf
				String instance_old = jsonObject.getString("instance_old");
				String instance_new = jsonObject.getString("instance_new");
				deleteProperties(itemUri, Namespace.BF.getUri() + "itemOf", instance_old, itemGraph);//删除旧 itemOf
				List<Triple> triples = new ArrayList<Triple>();
				triples.add(buildTriple(itemUri, Namespace.BF.getUri() + "itemOf", instance_new)); //新增新 itemOf
				GraphUtil.add(itemGraph, triples);
				System.out.println("更新Item成功。");
			}
			itemGraph.getTransactionHandler().commit();// 提交事务
		
		} catch (Exception e) {
			itemGraph.getTransactionHandler().abort();// 终止事务
			System.out.println("updatePersonProperties-Exception:" + e);
			throw new Exception(e);
		}

	}

	/*
	 * 获取系统当前最大的空节点的值 private String getMaxNodeId() { String sql =
	 * "select max(?o) as ?maxNodeId where {?s ?p ?o .filter isBlank(?o)} ";
	 * 
	 * }
	 */
}
