package cn.sh.library.pedigree.common.dataImport;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.DataItemChangeHistoryModel;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.JigouModel;
import cn.sh.library.pedigree.sysManager.model.PersonModel;
import cn.sh.library.pedigree.sysManager.model.PlaceModel;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataNewUtil {

	/**
	 * listToArray,谱籍
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertPlace(PlaceModel model) {

		try {

			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// label信息
			if (!StringUtilC.isEmpty(model.getLabelS())
					|| !StringUtilC.isEmpty(model.getLabelT())) {
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getLabelS(), "chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getLabelT(), "cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 国家信息
			if (!StringUtilC.isEmpty(model.getGuojiaS())
					|| !StringUtilC.isEmpty(model.getGuojiaT())) {
				_jsonArrayRoot4 = new JSONArray();
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:country");
				_rootObj4.put("uri",
						"http://www.library.sh.cn/ontology/country");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getGuojiaS(), "chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getGuojiaT(), "cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 描述信息
			filljsonDescription(_jsonArrayRoot3, model.getDescription());
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
			// 镇信息
			if (!StringUtilC.isEmpty(model.getXianS())) {
				_rootObj4 = new JSONObject();
				// _rootObj4.put("uri", "shl:county");
				_rootObj4.put("uri", "http://www.library.sh.cn/ontology/town");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getParm1S());
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
			// System.out.println(_jsonArrayRoot);
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Item插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertItem(DataItemChangeHistoryModel model) {
		JSONArray _jsonArrayRoot = new JSONArray();
		JSONArray _jsonArrayRoot3 = new JSONArray();
		// 根节点
		JSONObject _rootObj5 = new JSONObject();
		JSONObject _rootObj4 = new JSONObject();
		JSONObject _rootObj3 = new JSONObject();
		_rootObj4 = new JSONObject();
		_rootObj4.put("uri", "bf:itemOf");
		_rootObj4.put("type", "resource");
		_rootObj4.put("repeatable", "false");
		_rootObj5 = new JSONObject();
		_rootObj5.put("value", model.getInstanceUri());
		_rootObj4.put("objects", _rootObj5);
		_jsonArrayRoot3.add(_rootObj4);
		_rootObj4 = new JSONObject();
		// 馆藏地
		_rootObj4.put("uri", "bf:heldBy");
		_rootObj4.put("type", "resource");
		_rootObj4.put("repeatable", "false");
		_rootObj5 = new JSONObject();
		_rootObj5.put("value", model.getNewValueUri());
		_rootObj4.put("objects", _rootObj5);
		_jsonArrayRoot3.add(_rootObj4);
		// 索书号
		_rootObj4.put("uri", "bf:shelfMark");
		_rootObj4.put("type", "literal");
		_rootObj4.put("repeatable", "false");
		_rootObj5 = new JSONObject();
		_rootObj5.put("value", model.getNewValueCnBook());
		_rootObj4.put("objects", _rootObj5);
		_jsonArrayRoot3.add(_rootObj4);
		// DOI
		_rootObj4.put("uri", "shl:DOI");
		_rootObj4.put("type", "literal");
		_rootObj4.put("repeatable", "false");
		_rootObj5 = new JSONObject();
		_rootObj5.put("value", model.getNewValueCnDoi());
		_rootObj4.put("objects", _rootObj5);
		_jsonArrayRoot3.add(_rootObj4);
		// 描述信息
		filljsonDescription(_jsonArrayRoot3, model.getDescription());
		// DOI
		filljsonDescription(_jsonArrayRoot3, model.getNewValueCnDoi());
		// /////////////////////////////////倒数第三层array
		_rootObj3.put("graph", Constant.GRAPH_ITEM);
		// work,书目URI
		_rootObj3.put("uri", model.getItemUri());
		_rootObj3.put("type", "bf:Item");
		_rootObj3.put("properties", _jsonArrayRoot3);
		// 如果有值需要插入，则拼接
		if (_jsonArrayRoot3.size() > 0) {
			_jsonArrayRoot.add(_rootObj3);
		}

		// 插入数据
		DataUtilC.InsertV(_jsonArrayRoot);
		return true;
	}

	/**
	 * Item插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertItem_Work(DataItemChangeHistoryModel model) {
		JSONArray _jsonArrayRoot = new JSONArray();
		JSONArray _jsonArrayRoot3 = new JSONArray();
		// 根节点
		JSONObject _rootObj3 = new JSONObject();
		// 馆藏地
		fillJsonArrayRoot3(_jsonArrayRoot3, "bf:itemOf", true, false,
				model.getInstanceUri());
		// 馆藏地
		fillJsonArrayRoot3(_jsonArrayRoot3, "bf:heldBy", true, false,
				model.getNewValueUri());
		// 索书号
		fillJsonArrayRoot3(_jsonArrayRoot3, "bf:shelfMark", false, false,
				model.getNewValueCnBook());
		// DOI
		fillJsonArrayRoot3(_jsonArrayRoot3, "shl:DOI", false, false,
				model.getNewValueCnDoi());
		// 开关
		fillJsonArrayRoot3(_jsonArrayRoot3, "shl:accessLevel", false, false,
				model.getNewValueAccessLevel());
		// /////////////////////////////////倒数第三层array
		_rootObj3.put("graph", Constant.GRAPH_ITEM);
		// work,书目URI
		_rootObj3.put("uri", model.getItemUri());
		_rootObj3.put("type", "bf:Item");
		_rootObj3.put("properties", _jsonArrayRoot3);
		// 如果有值需要插入，则拼接
		if (_jsonArrayRoot3.size() > 0) {
			_jsonArrayRoot.add(_rootObj3);
		}
		DataUtilC.InsertV(_jsonArrayRoot);
		return true;
	}

	/**
	 * 堂号插入:插入所有堂号，没关联到姓氏信息的数据，同样插入。
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertTanghao(FamilyNameModel model) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();

			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// 描述信息
			filljsonDescription(_jsonArrayRoot3, model.getDescription());
			// label信息
			if (!StringUtilC.isEmpty(model.getFamilyNameS())
					|| !StringUtilC.isEmpty(model.getFamilyNameT())) {
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getFamilyNameS(),
						"chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getFamilyNameT(),
						"cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 姓氏信息
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:familyName", true, false,
					model.getFamilyUri());
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type",
					"http://www.library.sh.cn/ontology/TitleOfAncestralTemple");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * listToArray,先祖/名人/纂修者
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertPerson(PersonModel model) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// 描述信息
			filljsonDescription(_jsonArrayRoot3, model.getDescription());
			// label信息
			if (!StringUtilC.isEmpty(model.getLabelS())
					|| !StringUtilC.isEmpty(model.getLabelT())) {
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getLabelS(), "chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getLabelT(), "cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}

			// 姓氏信息
			fillJsonArrayRoot3(_jsonArrayRoot3, "foaf:familyName", true, false,
					model.getFamilyName());
			// roleOfFamily信息
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:roleOfFamily", true,
					false, model.getRoleUri());
			// 父信息
			fillJsonArrayRoot3(_jsonArrayRoot3, "rel:childOf", true, false,
					model.getChildOf());
			// 生于
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:birthday", false, false,
					model.getBirthday());
			// 死于
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:deathday", false, false,
					model.getDeathday());
			// 谱名信息
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:genealogyName", false,
					false, model.getGenealogyName());
			// 字
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:courtesyName", false,
					false, model.getZhi());
			// 行
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:orderOfSeniority", false,
					false, model.getHang());
			// 号
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:pseudonym", false, false,
					model.getHao());
			// 谥号
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:posthumousTitle", false,
					false, model.getShi());
			// 字辈
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:generationCharacter",
					false, false, model.getGenerationCharacter());
			// 时代
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:temporalValue", false,
					false, model.getShidai());
			// 时代URi
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:temporal", true, false,
					model.getShidaiUri());
			// 相关作品
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:relatedWork", true, false,
					model.getRelatedWork());
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_PERSON);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://www.library.sh.cn/ontology/Person");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 姓氏插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertFamilyName(FamilyNameModel model) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// 描述信息
			filljsonDescription(_jsonArrayRoot3, model.getDescription());
			// label信息
			if (!StringUtilC.isEmpty(model.getFamilyNameS())
					|| !StringUtilC.isEmpty(model.getFamilyNameT())
					|| !StringUtilC.isEmpty(model.getPinyin())) {
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getFamilyNameS(),
						"chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getFamilyNameT(),
						"cht");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getPinyin(), "en");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type",
					"http://www.library.sh.cn/ontology/FamilyName");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// 打印json
			System.out.println("插入JSON：" + _rootObj3.toString());
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * listToArray,机构
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean InsertJiGou(JigouModel model) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// 描述信息
			filljsonDescription(_jsonArrayRoot3, model.getDescription());
			// 地名，URI
			fillJsonArrayRoot3(_jsonArrayRoot3, "shl:region", true, false,
					model.getPlaceUri());
			// //////////////////////////////////////// 全称
			if (!StringUtilC.isEmpty(model.getQuanchengS())
					|| !StringUtilC.isEmpty(model.getQuanchengT())
					|| !StringUtilC.isEmpty(model.getAddressE())) {
				_rootObj4 = new JSONObject();
				_jsonArrayRoot4 = new JSONArray();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getQuanchengS(),
						"chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getQuanchengT(),
						"cht");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getAddressE(), "en");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// //////////////////////////////////////// 简称
			if (!StringUtilC.isEmpty(model.getJianchengS())
					|| !StringUtilC.isEmpty(model.getJianchengT())) {
				_rootObj4 = new JSONObject();
				_jsonArrayRoot4 = new JSONArray();
				_rootObj4.put("uri", "shl:abbreviateName");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getJianchengS(),
						"chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getJianchengT(),
						"cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// ////////////////////////////////////////地址
			if (!StringUtilC.isEmpty(model.getAddressS())
					|| !StringUtilC.isEmpty(model.getAddressT())
					|| !StringUtilC.isEmpty(model.getAddressO())) {
				_rootObj4 = new JSONObject();
				_jsonArrayRoot4 = new JSONArray();
				_rootObj4.put("uri", "shl:address");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getAddressS(), "chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getAddressT(), "cht");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getAddressO(), "en");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// //////////////////////////////////////// // 地名resouce

			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type",
					"http://www.library.sh.cn/ontology/Organization");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 取词词表插入
	 * 
	 * @param modelList
	 */
	public static boolean InsertCategory(CategoryModel model) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// 描述信息
			filljsonDescription(_jsonArrayRoot3, model.getDescription());
			// //////////////////////////////////////// 全称
			if (!StringUtilC.isEmpty(model.getLabel())
					|| !StringUtilC.isEmpty(model.getLabelT())) {
				_rootObj4 = new JSONObject();
				_jsonArrayRoot4 = new JSONArray();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// ///////////////////////////////////最里层Array
				filljsonArrayRoot4(_jsonArrayRoot4, model.getLabel(), "chs");
				filljsonArrayRoot4(_jsonArrayRoot4, model.getLabelT(), "cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// 类型URI
			fillJsonArrayRoot3(_jsonArrayRoot3, "bf:categoryType", false,
					false, model.getType());
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_BASEINFO);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "http://bibframe.org/vocab/Category");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			System.out.println(_jsonArrayRoot);
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
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

	/**
	 * 第三层
	 * 
	 * @param _jsonArrayRoot3
	 * @param str
	 * @param Lan
	 */
	public static void filljsonDescription(JSONArray _jsonArrayRoot3, String str) {
		if (!StringUtilC.isEmpty(str)) {
			JSONObject _rootObj4 = new JSONObject();
			_rootObj4.put("uri", "shl:description");
			_rootObj4.put("type", "literal");
			_rootObj4.put("repeatable", "false");
			JSONObject _rootObj5 = new JSONObject();
			_rootObj5.put("value", str);
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
		}

	}

	/**
	 * 
	 * @param _jsonArrayRoot3
	 * @param str
	 * @param moreFlg
	 * @param isUri
	 */
	public static void fillJsonArrayRoot3(JSONArray _jsonArrayRoot3,
			String uriValue, boolean isUri, boolean moreFlg, String str) {
		// 姓氏
		if (!StringUtilC.isEmpty(str)) {
			// 姓氏信息
			JSONObject _rootObj4 = new JSONObject();
			_rootObj4.put("uri", uriValue);
			if (isUri) {
				_rootObj4.put("type", "resource");
			} else {
				_rootObj4.put("type", "literal");
			}
			if (moreFlg) {
				_rootObj4.put("repeatable", "true");
			} else {
				_rootObj4.put("repeatable", "false");
			}
			JSONObject _rootObj5 = new JSONObject();
			_rootObj5.put("value", str);
			_rootObj4.put("objects", _rootObj5);
			_jsonArrayRoot3.add(_rootObj4);
		}
	}

}
