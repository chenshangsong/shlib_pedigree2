package cn.sh.library.pedigree.common.dataImport;

import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.graph.DataParser;
import cn.sh.library.pedigree.sysManager.model.JpPersonModel;
import cn.sh.library.pedigree.sysManager.model.JpWorkModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JPDataImport {

	public static void main(String[] args) {
		// PersonTest();
		/*
		 * System.out.println("http://data.library.sh.cn/vocab/edition/" +
		 * Trans2PinYin.convertAll("陈尚"));
		 */

	}

	/**
	 * 
	 * 谱载内容+附注
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertPuzaiNeirong(List<ShumuModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// /////////////////////////////////////////////谱载内容+附注
			// 谱载内容+附注
			String decprition = model.getQianxixinxi()
					+ model.getPuzhaineirong() + model.getFuzhu();
			if (!StringUtilC.isEmpty(decprition)) {// 如果居地URI不为空
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:description");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				// 迁徙信息+ 谱载内容+附注
				_rootObj5.put("value", decprition);
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_WORK);
				_rootObj3.put("uri", model.getSelfUri());
				_rootObj3.put("type", "bf:Work");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
		}
		// 插入数据
		// 打印json
		InsertV(_jsonArrayRoot);

	}

	/**
	 * 正书名插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step1_InsertZhengshuming(List<JpWorkModel> modelList) {
		JSONArray _jsonArrayRoot = new JSONArray();
		for (JpWorkModel model : modelList) {
			if (!StringUtilC.isEmpty(model.getTiming())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// /////////////////////////////////////////////正书名
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
						StringUtilC.getChs(model.getTiming()), "chs");
				DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
						model.getTimingCht(), "cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_TITLE);
				_rootObj3.put("uri", model.getTimingUri());
				_rootObj3.put("type", "bf:Title");
				// _rootObj3.put("type", "bf:WorkTitle");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
		}
		// 插入数据
		InsertV(_jsonArrayRoot);
		step1_2_InsertZhengshuming(modelList);
		return true;
	}

	/**
	 * 正书名插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step1_2_InsertZhengshuming(List<JpWorkModel> modelList) {
		JSONArray _jsonArrayRoot = new JSONArray();
		for (JpWorkModel model : modelList) {
			if (!StringUtilC.isEmpty(model.getTiming())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// /////////////////////////////////////////////正书名
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
						StringUtilC.getChs(model.getTiming()), "chs");
				DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
						model.getTimingCht(), "cht");
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_TITLE);
				_rootObj3.put("uri", model.getTimingUri());
				_rootObj3.put("type", "bf:WorkTitle");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
		}
		// 插入数据
		InsertV(_jsonArrayRoot);
		return true;
	}

	/**
	 * 书目导入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step2_InsertWork(List<JpWorkModel> modelList) {
		try {
			JSONArray _jsonArrayRoot = new JSONArray();
			for (JpWorkModel model : modelList) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				JSONObject _rootObj5 = new JSONObject();
				// 题名
				if (!StringUtilC.isEmpty(model.getTitleInChs())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "dc:title");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "true");
					DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
							StringUtilC.getChs(model.getTitleInChs()), "chs");
					DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
							model.getTitleInCht(), "cht");
					_rootObj4.put("objects", _jsonArrayRoot4);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 正题名
				if (!StringUtilC.isEmpty(model.getTimingUri())) {
					// 如果居地URI不为空
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:title");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getTimingUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 堂号和姓氏
				if (!StringUtilC.isEmpty(model.getTanghaoUri())
						|| !StringUtilC.isEmpty(model.getFamilyNameUri())) {
					_rootObj4 = new JSONObject();
					_jsonArrayRoot4 = new JSONArray();
					_rootObj4.put("uri", "bf:subject");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "true");
					if (!StringUtilC.isEmpty(model.getTanghaoUri())) {
						filljsonArrayRoot4(_jsonArrayRoot4,
								model.getTanghaoUri());
					}
					if (!StringUtilC.isEmpty(model.getFamilyNameUri())) {
						filljsonArrayRoot4(_jsonArrayRoot4,
								model.getFamilyNameUri());
					}
					_rootObj4.put("objects", _jsonArrayRoot4);
					_jsonArrayRoot3.add(_rootObj4);
				}

				// /////////////////////////////////////////居地
				if (!StringUtilC.isEmpty(model.getPlaceUri())) {
					// 如果居地URI不为空
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:place");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getPlaceUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 谱载内容+附注
				String decprition = model.getZhaiyao() + model.getFuzhu();
				if (!StringUtilC.isEmpty(decprition)) {// 如果居地URI不为空
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:description");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					// 迁徙信息+ 谱载内容+附注
					_rootObj5.put("value", decprition);
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_WORK);
				_rootObj3.put("uri", model.getSelfUri());
				_rootObj3.put("type", "bf:Work");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
			// 插入数据
			InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Instance信息插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step3_InsertInstance(List<JpWorkModel> modelList) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			for (JpWorkModel model : modelList) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				// 根节点
				JSONObject _rootObj5 = new JSONObject();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// bf:instanceOf
				if (!StringUtilC.isEmpty(model.getSelfUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:instanceOf");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getSelfUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// /////////////////////////////////////////居地
				if (!StringUtilC.isEmpty(model.getPlaceUri())) {
					// 如果居地URI不为空
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:place");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getPlaceUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 版本年代
				if (!StringUtilC.isEmpty(model.getBbsj())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:temporalValue");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBbsj());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 版本年代Year
				if (!StringUtilC.isEmpty(model.getBbsjgyYear())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:temporalValue");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBbsjgyYear());
					_rootObj5.put("datatype", "int");
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 版本年代:URi格式
				if (!StringUtilC.isEmpty(model.getBbcdUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:temporal");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBbcdUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}

				// 版本类型 URI
				if (!StringUtilC.isEmpty(model.getBbxsUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:edition");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBbxsUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}// 版本类型 汉字
				else {
					if (!StringUtilC.isEmpty(model.getBbxs())) {
						_rootObj4 = new JSONObject();
						_rootObj4.put("uri", "shl:description");
						_rootObj4.put("type", "literal");
						_rootObj4.put("repeatable", "false");
						_rootObj5 = new JSONObject();
						_rootObj5.put("value", model.getBbxs());
						_rootObj4.put("objects", _rootObj5);
						_jsonArrayRoot3.add(_rootObj4);
					}
				}
				// 数量
				if (!StringUtilC.isEmpty(model.getShuliang())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:extent");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getShuliang());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_INSTANCE);
				// work,书目URI
				_rootObj3.put("uri", model.getInstanceUri());
				_rootObj3.put("type", "bf:Instance");
				_rootObj3.put("properties", _jsonArrayRoot3);
				// 如果有值需要插入，则拼接
				if (_jsonArrayRoot3.size() > 0) {
					_jsonArrayRoot.add(_rootObj3);
				}
			}

			// 插入数据
			InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Instance信息插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step4_InsertItem(List<JpWorkModel> modelList) {
		String shangtuUri = "http://data.library.sh.cn/entity/organization/brvqlrg8y55v1b5q";
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			for (JpWorkModel model : modelList) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				// 根节点
				JSONObject _rootObj5 = new JSONObject();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// bf:itemOf
				if (!StringUtilC.isEmpty(model.getInstanceUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:itemOf");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getInstanceUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				if (!StringUtilC.isEmpty(shangtuUri)) {
					// 馆藏地
					_rootObj4 = new JSONObject();
					// 馆藏地
					_rootObj4.put("uri", "bf:heldBy");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", shangtuUri);
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 索取号
				if (!StringUtilC.isEmpty(model.getSuoquhao())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:shelfMark");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getSuoquhao());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// DOI
				if (!StringUtilC.isEmpty(model.getDOI())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:DOI");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getDOI());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 开关
				if (!StringUtilC.isEmpty(model.getItemUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:accessLevel");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", "1");
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
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
			}

			// 插入数据
			InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
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
	public static boolean step5_InsertXzmr(List<JpPersonModel> list) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			for (JpPersonModel model : list) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// label信息
				if (!StringUtilC.isEmpty(model.getXingming())
						|| !StringUtilC.isEmpty(model.getXingmingCht())) {
					_rootObj4.put("uri", "bf:label");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "true");
					// ///////////////////////////////////最里层Array
					DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
							model.getXingming(), "chs");
					DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
							model.getXingmingCht(), "cht");
					_rootObj4.put("objects", _jsonArrayRoot4);
					_jsonArrayRoot3.add(_rootObj4);
				}

				// 姓氏信息
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"foaf:familyName", true, false,
						model.getFamilyNameUri());
				// roleOfFamily信息
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"shl:roleOfFamily", true, false, model.getTypeUri());
				// 时代
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"shl:temporalValue", false, false, model.getChaodai());
				// 时代URi
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3, "shl:temporal",
						true, false, model.getChaodaiUri());
				// 相关作品
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"shl:relatedWork", true, false, model.getWorkUri());
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_PERSON);
				_rootObj3.put("uri", model.getSelfUri());
				_rootObj3.put("type", "shl:Person");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}

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
	public static boolean step6_InsertZRZandQT(List<JpPersonModel> list) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			for (JpPersonModel model : list) {

				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// label信息
				if (!StringUtilC.isEmpty(model.getXingming())
						|| !StringUtilC.isEmpty(model.getXingmingCht())) {
					_rootObj4.put("uri", "bf:label");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "true");
					// ///////////////////////////////////最里层Array
					DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
							model.getXingming(), "chs");
					DataNewUtil.filljsonArrayRoot4(_jsonArrayRoot4,
							model.getXingmingCht(), "cht");
					_rootObj4.put("objects", _jsonArrayRoot4);
					_jsonArrayRoot3.add(_rootObj4);
				}

				// 姓氏信息
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"foaf:familyName", true, false,
						model.getFamilyNameUri());
				// roleOfFamily信息
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3, "bf:role",
						true, false, model.getTypeUri());
				// 时代
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"shl:temporalValue", false, false, model.getChaodai());
				// 时代URi
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3, "shl:temporal",
						true, false, model.getChaodaiUri());
				// 相关作品
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"shl:relatedWork", true, false, model.getWorkUri());
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_PERSON);
				_rootObj3.put("uri", model.getSelfUri());
				_rootObj3.put("type", "shl:Person");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
			// 插入数据
			DataUtilC.InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 责任者插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step7_InsertZrzToWork(List<JpPersonModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		try {

			for (JpPersonModel model : modelList) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				// 根节点
				JSONObject _rootObj3 = new JSONObject();
				// 时代URi
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3, "bf:creator",
						true, false, model.getSelfUri());
				_rootObj3.put("graph", Constant.GRAPH_WORK);
				_rootObj3.put("uri", model.getWorkUri());
				_rootObj3.put("type", "bf:Work");
				_rootObj3.put("properties", _jsonArrayRoot3);
				// 如果有值需要插入，则拼接
				if (_jsonArrayRoot3.size() > 0) {
					_jsonArrayRoot.add(_rootObj3);
				}
			}
			// 插入数据
			InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 责任者插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static boolean step8_InsertZrzqtToWork(List<JpPersonModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		try {

			for (JpPersonModel model : modelList) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				// 根节点
				JSONObject _rootObj3 = new JSONObject();
				// 时代URi
				DataNewUtil.fillJsonArrayRoot3(_jsonArrayRoot3,
						"bf:contributor", true, false, model.getSelfUri());
				_rootObj3.put("graph", Constant.GRAPH_WORK);
				_rootObj3.put("uri", model.getWorkUri());
				_rootObj3.put("type", "bf:Work");
				_rootObj3.put("properties", _jsonArrayRoot3);
				// 如果有值需要插入，则拼接
				if (_jsonArrayRoot3.size() > 0) {
					_jsonArrayRoot.add(_rootObj3);
				}
			}
			// 插入数据
			InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 数据插入
	 * 
	 * @param JsonArray
	 */
	private static void InsertV(JSONArray JsonArray) {
		if (JsonArray.size() > 0) {
			DataParser data = new DataParser();
			data.launch(JsonArray);
		}
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
	 * 得到旧值
	 * 
	 * @param _outStream
	 * @return
	 */
	public static String getOldValue(OutputStream _outStream, String p) {
		String old_value = "";
		// 规范数据编辑属性
		DataEditViewDto dataEditView = DataManagerDto
				.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
		// 去除NON等
		dataEditView = EditDtoCommon.listEmptyChange(dataEditView);
		for (DataEditDto _pDto : dataEditView.getEditList()) {
			if (_pDto.getOldEnName().equals(p)) {
				return EditDtoCommon.getValue(_pDto.getOldValue());
			}
		}
		return old_value;
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

}
