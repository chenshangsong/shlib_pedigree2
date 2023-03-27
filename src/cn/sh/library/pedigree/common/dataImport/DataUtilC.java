package cn.sh.library.pedigree.common.dataImport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.graph.DataParser;
import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.ChaodaiModel;
import cn.sh.library.pedigree.sysManager.model.ChaodaiYearModel;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.HuPersonModel;
import cn.sh.library.pedigree.sysManager.model.JigouModel;
import cn.sh.library.pedigree.sysManager.model.PlaceModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataUtilC {

	public static void main(String[] args) {
		System.out.println(DataUtilC.getUri("1"));
	}

	/**
	 * 插入测试
	 * 
	 * @param jsonArrayString
	 */
	public static void insertTest(String jsonArrayString) {
		// jsonArrayString =
		// "{\"graph\":\"http://gen.library.sh.cn/graph/work\",\"uri\":\"http://data.library.sh.cn/jp/resource/work/chen123456\",\"type\":\"bf:Work\",\"properties\":[{\"uri\":\"bf:identifiedBy\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"0010001\"}},{\"uri\":\"shl:place\",\"type\":\"resource\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://data.library.sh.cn/entity/place/p74er1naz2m4791g\"}}]}";
		jsonArrayString = "{\"graph\":\"http://gen.library.sh.cn/graph/work\",\"uri\":\"http://data.library.sh.cn/jp/resource/work/chen123456\",\"type\":\"bf:Work\",\"properties\":[{\"uri\":\"bf:title\",\"type\":\"resource\",\"repeatable\":\"true\",\"objects\":[{\"value\":\"http://data.library.sh.cn/jp/authority/title/aaaaa\"},{\"value\":\"http://data.library.sh.cn/jp/authority/title/bbbbbbb\"}]}]}";
		JSONArray _jsonArrayRoot = new JSONArray();
		JSONObject _arry = JSONObject.fromObject(jsonArrayString);
		_jsonArrayRoot.add(_arry);
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * 姓氏插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertShumu(List<ShumuModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();

		for (ShumuModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// /////////////////////////////////////////////标识符
			_rootObj4 = new JSONObject();
			// _rootObj4.put("uri", "shl:region");
			_rootObj4.put("uri", "bf:identifiedBy");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getIdentifier());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////////////////居地
			if (!StringUtilC.isEmpty(model.getJudiUri())) {// 如果居地URI不为空
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:place");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getJudiUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			} else {// 如果居地匹配不到URI，且居地属性不为空，则写入bf:edition
				if (!StringUtilC.isEmpty(model.getJudi())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:edition");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getJudi());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_WORK);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "bf:Work");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// 打印json
			System.out.println("插入JSON：" + _rootObj3.toString());
		}
		// 插入数据

		// InsertV(_jsonArrayRoot);

	}

	/**
	 * 姓氏插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertFamilyName(List<FamilyNameModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (FamilyNameModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////倒数第二层Obj
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			_rootObj5.put("value", model.getFamilyNameS());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getFamilyNameT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getPinyin());
			_rootObj5.put("language", "en");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			// _rootObj3.put("uri", model.getUri());
			_rootObj3.put("uri", model.getSelfUri());
			// _rootObj3.put("type", "shl:FamilyName");
			_rootObj3.put("type",
					"http://www.library.sh.cn/ontology/FamilyName");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// 打印json
			System.out.println("插入JSON：" + _rootObj3.toString());
		}
		// 插入数据

		// InsertV(_jsonArrayRoot);

	}

	/**
	 * 谱名插入
	 * 
	 * @param modelList
	 */
	public static void InsertPuming(List<FamilyNameModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (FamilyNameModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////倒数第二层Obj
			// _rootObj4.put("uri", "bf:titleValue");
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			_rootObj5.put("value", model.getFamilyNameS());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getFamilyNameT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", "http://gen.library.sh.cn/graph/title");
			// _rootObj3.put("uri", model.getUri());
			_rootObj3.put("uri", model.getSelfUri());
			// _rootObj3.put("type", "shl:FamilyName");
			_rootObj3.put("type", "http://bibframe.org/vocab/Title");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
		}
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * 朝代表插入
	 * 
	 * @param modelList
	 */
	public static void InsertChaodai(List<ChaodaiModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ChaodaiModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////label
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getChaodai());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// ///////////////////////////////////朝代
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:dynasty");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getChaodai());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////////开始年
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:beginYear");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getBeginY());
			_rootObj5.put("datatype", "int");
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////////结束年
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:endYear");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getEndY());
			_rootObj5.put("datatype", "int");
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////////北宋，南宋,到宋
			if (!StringUtilC.isEmpty(model.getIntervalDuring())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "time:intervalDuring");// 期间：在某时间段期间，取值为时间段
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getIntervalDuring());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_TEMPORAL);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Temporal");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * 年号纪年表插入
	 * 
	 * @param modelList
	 */
	public static void InsertChaodaiYear(List<ChaodaiYearModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		JSONArray _jsonArrayRoot4 = new JSONArray();
		for (ChaodaiYearModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			if (!StringUtilC.isEmpty(model.getChaodai())
					|| !StringUtilC.isEmpty(model.getDiwangNianhao())) {
				String cd = StringUtilC.getString(model.getChaodai())
						.split(",")[0];
				String nh = StringUtilC.getString(model.getDiwangNianhao())
						.split(",")[0];
				// ///////////////////////////////////label
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				// 朝代+年号
				_rootObj5.put("value", cd + nh);
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// ///////////////////////////////////朝代
			if (!StringUtilC.isEmpty(model.getChaodai())) {
				_jsonArrayRoot4 = new JSONArray();
				String cd[] = StringUtilC.getString(model.getChaodai()).split(
						",");
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:dynasty");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				for (int i = 0; i < cd.length; i++) {
					filljsonArrayRoot4(_jsonArrayRoot4, cd[i]);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}

			// ///////////////////////////////////帝王
			if (!StringUtilC.isEmpty(model.getDiwang())) {
				_jsonArrayRoot4 = new JSONArray();
				String dw[] = StringUtilC.getString(model.getDiwang()).split(
						",");
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:monarch");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				for (int i = 0; i < dw.length; i++) {
					filljsonArrayRoot4(_jsonArrayRoot4, dw[i]);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// ///////////////////////////////////帝王姓名
			if (!StringUtilC.isEmpty(model.getDiwangName())) {
				_jsonArrayRoot4 = new JSONArray();
				String dw[] = StringUtilC.getString(model.getDiwangName())
						.split(",");
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:monarchName");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				for (int i = 0; i < dw.length; i++) {
					filljsonArrayRoot4(_jsonArrayRoot4, dw[i]);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			} else if (StringUtilC.isEmpty(model.getDiwangName())) {
				// 如果帝王姓名为空，则插入NON。
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:monarchName");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", "NON");
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// ///////////////////////////////////帝王年号
			if (!StringUtilC.isEmpty(model.getDiwangNianhao())) {
				_jsonArrayRoot4 = new JSONArray();
				String dw[] = StringUtilC.getString(model.getDiwangNianhao())
						.split(",");
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:reignTitle");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				for (int i = 0; i < dw.length; i++) {
					filljsonArrayRoot4(_jsonArrayRoot4, dw[i]);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// //////////////////////////////////////////开始年
			if (!StringUtilC.isEmpty(model.getBeginY())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:beginYear");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getBeginY());
				_rootObj5.put("datatype", "int");
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// //////////////////////////////////////////结束年
			if (!StringUtilC.isEmpty(model.getEndY())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:endYear");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getEndY());
				_rootObj5.put("datatype", "int");
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// //////////////////////////////////////////外链朝代
			if (!StringUtilC.isEmpty(model.getChaodaiUri())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "time:intervalDuring");// 期间：在某时间段期间，取值为时间段
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getChaodaiUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_TEMPORAL);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Temporal");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// System.out.println(_jsonArrayRoot);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * 年号纪年表插入
	 * 
	 * @param modelList
	 */
	public static void InsertChaodaiYear2(List<ChaodaiYearModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ChaodaiYearModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////label
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			// 朝代+年号
			_rootObj5.put("value",
					model.getChaodai() + model.getDiwangNianhao());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// ///////////////////////////////////朝代
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:dynasty");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getChaodai());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// ///////////////////////////////////帝王
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:monarch");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getDiwang());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// ///////////////////////////////////帝王姓名
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:monarchName");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getDiwangName());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// ///////////////////////////////////帝王年号
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:reignTitle");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getDiwangNianhao());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////////开始年
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:beginYear");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getBeginY());
			_rootObj5.put("datatype", "int");
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////////结束年
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:endYear");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getEndY());
			_rootObj5.put("datatype", "int");
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////////外链朝代
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "time:intervalDuring");// 期间：在某时间段期间，取值为时间段
			_rootObj4.put("type", "resource");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getChaodaiUri());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_TEMPORAL);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Temporal");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * 取词词表插入
	 * 
	 * @param modelList
	 */
	public static void InsertCategory(List<CategoryModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (CategoryModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////倒数第二层Obj
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			_rootObj5.put("value", model.getLabel());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getLabelT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			_rootObj4 = new JSONObject();
			// /////////////////////////////////////categoryType
			_rootObj4.put("uri", "http://bibframe.org/vocab/categoryType");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			// ///////////////////////////////////最里层Array
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getType());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://bibframe.org/vocab/Category");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
		}
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * 堂号插入:插入所有堂号，没关联到姓氏信息的数据，同样插入。
	 * 
	 * @param modelList
	 */
	public static void InsertTanghao(List<FamilyNameModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (FamilyNameModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////倒数第二层Obj
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			_rootObj5.put("value", model.getFamilyNameS());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getFamilyNameT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 如果姓氏为空则不进行拼接姓氏信息
			if (model.getFamilyUri() != null
					&& !StringUtilC.isEmpty(model.getFamilyUri())) {
				// 姓氏信息
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:familyName");
				_rootObj4.put("uri",
						"http://www.library.sh.cn/ontology/familyName");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getFamilyUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			// _rootObj3.put("uri", model.getUri());
			_rootObj3.put("uri", model.getSelfUri());
			// _rootObj3.put("type", "shl:FamilyName");
			_rootObj3.put("type",
					"http://www.library.sh.cn/ontology/TitleOfAncestralTemple");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
		}
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * listToArray,先祖/名人/纂修者
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertPerson(List<FamilyNameModel> listP,
			List<FamilyNameModel> listF, String type) {
		List<FamilyNameModel> _tempList = createList(listP, listF, type);
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (FamilyNameModel model : _tempList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////倒数第二层Obj
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			_rootObj5.put("value", model.getFamilyNameS());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getFamilyNameT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 如果姓氏为空则不进行拼接姓氏信息
			if (model.getFamilyUri() != null
					&& !StringUtilC.isEmpty(model.getFamilyUri())) {
				// 姓氏信息
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "foaf:familyName");
				// _rootObj4.put("uri",
				// "http://www.library.sh.cn/ontology/FamilyName");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getFamilyUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// roleOfFamily信息:纂修者无。
			// 如果roleOfFamily信息为空则不进行拼接roleOfFamily信息
			if (model.getRoleOfFamilyUri() != null
					&& !StringUtilC.isEmpty(model.getRoleOfFamilyUri())) {
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:roleOfFamily");
				_rootObj4.put("uri",
						"http://www.library.sh.cn/ontology/roleOfFamily");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getRoleOfFamilyUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_PERSON);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Person");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// System.out.println(_jsonArrayRoot);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * listToArray,先祖/名人/纂修者
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertHSPersonPEIOU(List<HuPersonModel> listP) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (HuPersonModel model : listP) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// 姓名
			_jsonArrayRoot4 = new JSONArray();
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			filljsonArrayRoot4(_jsonArrayRoot4,
					StringUtilC.getChs(model.getFname()), "chs");
			filljsonArrayRoot4(_jsonArrayRoot4, model.getFname(), "cht");
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 性别
			_jsonArrayRoot4 = new JSONArray();
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "foaf:gender");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			filljsonArrayRoot4(_jsonArrayRoot4, "女", "chs");
			filljsonArrayRoot4(_jsonArrayRoot4, "女", "cht");
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 如果姓氏为空则不进行拼接姓氏信息
			if (model.getFnameUri() != null
					&& !StringUtilC.isEmpty(model.getFnameUri())) {
				// 姓氏信息
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "foaf:familyName");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getFnameUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 老公URI。
			if (model.getLaogongUri() != null
					&& !StringUtilC.isEmpty(model.getLaogongUri())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "rel:spouseOf");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getLaogongUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_PERSON);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Person");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * listToArray,先祖/名人/纂修者
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertHSPerson(List<HuPersonModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (HuPersonModel model : modelList) {
			// 默认为先祖
			model.setRole("http://data.library.sh.cn/jp/vocab/ancestor/xian-zu");
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ///////////////////////////////////倒数第二层Obj
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			// 名字，简写
			_rootObj5.put("value",
					StringUtilC.getChs(model.getFname() + model.getMingzi()));
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			// 名字，繁体
			_rootObj5.put("value", model.getFname() + model.getMingzi());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 姓氏信息
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "foaf:familyName");
			_rootObj4.put("type", "resource");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getFnameUri());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// roleOfFamily信息:先祖。
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:roleOfFamily");
			_rootObj4.put("type", "resource");
			_rootObj4.put("repeatable", "false");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getRole());
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
			// 谱名
			if (!StringUtilC.isEmpty(model.getPuming())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:genealogyName");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getPuming());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 字辈
			if (!StringUtilC.isEmpty(model.getBei())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:generationCharacter");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getBei());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 字
			if (!StringUtilC.isEmpty(model.getZi())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:courtesyName");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getZi());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 号
			if (!StringUtilC.isEmpty(model.getHao())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:pseudonym");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getHao());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 谥号
			if (!StringUtilC.isEmpty(model.getShihao())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:posthumousTitle");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getShihao());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 行
			if (!StringUtilC.isEmpty(model.getHang())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:orderOfSeniority");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getHang());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 生于
			if (!StringUtilC.isEmpty(model.getShengyu())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:birthday");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getShengyu());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 卒于
			if (!StringUtilC.isEmpty(model.getZuyu())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:deathday");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getZuyu());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 父亲
			if (!StringUtilC.isEmpty(model.getFuqinUri())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "rel:childOf");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getFuqinUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 说明
			if (!StringUtilC.isEmpty(model.getShuoming())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:description");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getShuoming());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// ///////////////////////////////////配偶
			if (model.getPeiouList().size() > 0) {
				_jsonArrayRoot4 = new JSONArray();
				for (HuPersonModel object : model.getPeiouList()) {
					filljsonArrayRoot4(_jsonArrayRoot4, object.getSelfUri());
				}
				if (_jsonArrayRoot4.size() > 0) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "rel:spouseOf");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "true");
					_rootObj4.put("objects", _jsonArrayRoot4);
					_jsonArrayRoot3.add(_rootObj4);
				}
			}
			// ///////////////////////////////////releatedWork
			_jsonArrayRoot4 = new JSONArray();
			// "http://data.library.sh.cn/jp/resource/work/kqlipgffst2kqjcb"
			/*
			 * List<String> workUriList = Arrays .asList(new String[] {
			 * "http://data.library.sh.cn/jp/resource/work/jklhb5c3ga1rvxe3" });
			 */
			List<String> workUriList = Arrays.asList(new String[] { model
					.getReleatedWork() });
			for (String object : workUriList) {
				filljsonArrayRoot4(_jsonArrayRoot4, object);
			}
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:relatedWork");
			_rootObj4.put("type", "resource");
			_rootObj4.put("repeatable", "true");
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_PERSON);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "shl:Person");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * listToArray,谱籍
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertPlace(List<PlaceModel> listP) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (PlaceModel model : listP) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// label信息
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			// ///////////////////////////////////最里层Array
			_rootObj5.put("value", model.getLabelS());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getLabelT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 国家信息
			_jsonArrayRoot4 = new JSONArray();
			_rootObj4 = new JSONObject();
			// _rootObj4.put("uri", "shl:country");
			_rootObj4.put("uri", "http://www.library.sh.cn/ontology/country");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getGuojiaS());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getGuojiaT());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// 省份信息
			if (!StringUtilC.isEmpty(model.getShengS())) {
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:province");
				_rootObj4.put("uri",
						"http://www.library.sh.cn/ontology/province");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getShengS());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 城市信息
			if (!StringUtilC.isEmpty(model.getShiS())) {
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:city");
				_rootObj4.put("uri", "http://www.library.sh.cn/ontology/city");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getShiS());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 县城信息
			if (!StringUtilC.isEmpty(model.getXianS())) {
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:county");
				_rootObj4
						.put("uri", "http://www.library.sh.cn/ontology/county");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getXianS());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 经纬度信息
			if (!StringUtilC.isEmpty(model.getPoint())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "owl:sameAs");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getPoint());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_PLACE);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Place");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * listToArray,机构
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertJiGou(List<JigouModel> listP) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (JigouModel model : listP) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// ////////////////////////////////////////地址
			if (!StringUtilC.isEmpty(model.getAddressS())
					|| !StringUtilC.isEmpty(model.getAddressT())
					|| !StringUtilC.isEmpty(model.getAddressO())) {
				_rootObj4.put("uri", "shl:address");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				if (!StringUtilC.isEmpty(model.getAddressS())) {
					_rootObj5.put("value", model.getAddressS().trim());
					_rootObj5.put("language", "chs");
					_jsonArrayRoot4.add(_rootObj5);
				}
				if (!StringUtilC.isEmpty(model.getAddressT())) {
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getAddressT().trim());
					_rootObj5.put("language", "cht");
					_jsonArrayRoot4.add(_rootObj5);
				}
				if (!StringUtilC.isEmpty(model.getAddressO())) {
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getAddressO().trim());
					_rootObj5.put("language", "en");
					_jsonArrayRoot4.add(_rootObj5);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// //////////////////////////////////////// 全称
			_jsonArrayRoot4 = new JSONArray();
			_rootObj4 = new JSONObject();
			_rootObj4.put("uri", "bf:label");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			_rootObj5.put("value", model.getQuanchengS().trim());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getQuanchengT().trim());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			// 英文
			if (!StringUtilC.isEmpty(model.getAddressE())) {
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getAddressE().trim());
				_rootObj5.put("language", "en");
				_jsonArrayRoot4.add(_rootObj5);
			}
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////// 简称
			_jsonArrayRoot4 = new JSONArray();
			_rootObj4 = new JSONObject();
			// _rootObj4.put("uri", "shl:abbreviateName");
			_rootObj4.put("uri",
					"http://www.library.sh.cn/ontology/abbreviateName");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "true");
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getJianchengS().trim());
			_rootObj5.put("language", "chs");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj5 = new JSONObject();
			_rootObj5.put("value", model.getJianchengT().trim());
			_rootObj5.put("language", "cht");
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// //////////////////////////////////////// // 地名resouce
			if (!StringUtilC.isEmpty(model.getPlaceUri())) {
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:region");
				_rootObj4.put("uri", "shl:region");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getPlaceUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type",
					"http://www.library.sh.cn/ontology/Organization");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// System.out.println(_jsonArrayRoot);
		}
		// System.out.println(_jsonArrayRoot);
		// 插入数据
		// InsertV(_jsonArrayRoot);
	}

	/**
	 * 重新关联人员和姓氏，得到list
	 * 
	 * @param listP
	 * @param listF
	 * @return
	 */
	protected static List<FamilyNameModel> createList(
			List<FamilyNameModel> listP, List<FamilyNameModel> listF,
			String type) {
		List<FamilyNameModel> listPLast = new ArrayList<FamilyNameModel>();
		for (FamilyNameModel p : listP) {
			if (p.getSelfUri() != null) {
				// FamilyID
				Integer uritemp = StringUtilC.getInteger(p.getFamilyID());
				// 中文名称
				String unameChs = StringUtilC.getString(p.getFamilyNameS());
				// FamilyNameUri：获取姓氏URI。
				p.setFamilyUri(getRefUri(uritemp, unameChs, listF, type));
				listPLast.add(p);
			}
		}
		return listPLast;
	}

	/**
	 * 得到姓氏URI
	 * 
	 * @param uriId
	 * @param unameChs
	 * @param listF
	 * @return
	 */
	protected static String getRefUri(Integer uriId, String unameChs,
			List<FamilyNameModel> listF, String type) {
		String uri = "";
		Map<Integer, FamilyNameModel> famMap = new HashMap<Integer, FamilyNameModel>();
		for (FamilyNameModel f : listF) {
			famMap.put(StringUtilC.getInteger(f.getIdentifier()), f);
		}
		FamilyNameModel fModel = new FamilyNameModel();
		fModel = famMap.get(uriId);
		if (fModel != null) {
			if (type != "mingren") {
				// 如果ID相同且人名包含该姓氏，
				if (unameChs.contains(fModel.getFamilyNameS())) {
					uri = fModel.getSelfUri();
				}// ID相同，姓氏不同的，从高往低匹配姓氏
				else {
					for (FamilyNameModel f : listF) {
						for (int i = 5; i > 0; i--) {
							String pfname = null;
							if (unameChs.length() >= i) {
								pfname = unameChs.substring(0, i);
								// 608以外的，用名称的姓匹配
								if (f.getFamilyNameS().length() == i
										&& f.getFamilyNameS().equals(pfname)) {
									uri = f.getSelfUri();
									System.out.println("ID匹配，姓氏不匹配：人名:"
											+ unameChs + "----姓氏："
											+ f.getFamilyNameS());
									break;
								}
							}
						}
					}
				}
			} else {// 如果是名人，找到数据则结束。
				uri = fModel.getSelfUri();
			}
		}// 找不到姓氏的，从高往低匹配姓氏
		else {
			for (FamilyNameModel f : listF) {
				for (int i = 5; i > 0; i--) {
					String pfname = null;
					if (unameChs.length() >= i) {
						pfname = unameChs.substring(0, i);
						// 608以外的，用名称的姓匹配
						if (f.getFamilyNameS().length() == i
								&& f.getFamilyNameS().equals(pfname)) {
							uri = f.getSelfUri();
							// System.out.println("608以外：人名:" +
							// unameChs+"----姓氏："+f.getFamilyNameS());
							break;
						}
					}
				}
			}
		}
		if (uri == null || uri == "") {
			System.out.println(">--------------没有姓氏的人名:" + unameChs + ":---"
					+ uriId);
		}
		return uri;
	}

	/**
	 * 数据插入
	 * 
	 * @param JsonArray
	 */
	public static void InsertV(JSONArray JsonArray) {
		DataParser data = new DataParser();
		data.launch(JsonArray);
	}

	/**
	 * 得到URI的16位值
	 * 
	 * @param pwd_len
	 * @return
	 */
	public static String getRandomUriValue(int pwd_len) {
		// 34是因为数组是从1开始的，26个字母+9个 数字
		final int maxNum = 35;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止 生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 得到URI的16位值
	 * 
	 * @param pwd_len
	 * @return
	 */
	public static String getUri(String dataType) {
		String uri = DataTypeMap.getDataUriName(dataType)
				+ getRandomUriValue(16);
		return uri;
	}

	/**
	 * 根据语言类型，获取值
	 * 
	 * @param str
	 * @param type
	 * @chs,@cht,@en,""
	 * @return
	 */
	public static String getValueCTE(String str, String type) {
		if (StringUtilC.isEmpty(type)) {
			return str;
		}
		// 规范名,简体，繁体处理
		String[] valuelist = str.split(",");
		for (String string : valuelist) {
			if (string.contains(type)) {
				return string.replace(type, "");
			}
		}
		return str;
	}

	/**
	 * 制作json
	 * 
	 * @param _jsonArrayRoot4
	 * @param str
	 * @param Lan
	 */
	public static void filljsonArrayRoot4(JSONArray _jsonArrayRoot4, String str) {
		if (!StringUtilC.isEmpty(str)) {
			JSONObject _rootObj5 = new JSONObject();
			_rootObj5.put("value", str);
			_jsonArrayRoot4.add(_rootObj5);
		}

	}

	/**
	 * 制作json
	 * 
	 * @param _jsonArrayRoot4
	 * @param str
	 * @param Lan
	 */
	public static void filljsonArrayRoot4(JSONArray _jsonArrayRoot4,
			String str, String Lan) {
		if (!StringUtilC.isEmpty(str)) {
			JSONObject _rootObj5 = new JSONObject();
			_rootObj5.put("value", str);
			if (!StringUtilC.isEmpty(Lan)) {
				_rootObj5.put("language", Lan);
			}
			_jsonArrayRoot4.add(_rootObj5);
		}

	}
}
