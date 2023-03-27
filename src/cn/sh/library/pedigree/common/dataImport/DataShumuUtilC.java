package cn.sh.library.pedigree.common.dataImport;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.PersonUtils;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.graph.DataParser;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataShumuUtilC {

	public static void main(String[] args) {
		// PersonTest();
		/*
		 * System.out.println("http://data.library.sh.cn/vocab/edition/" +
		 * Trans2PinYin.convertAll("陈尚"));
		 */

	}

	/**
	 * 正题名，简繁体插入
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
			if (!StringUtilC.isEmpty(model.getZhengshuming())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "dc:title");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "true");
				// 居地简体
				String jds = StringUtilC.isEmpty(model.getJdS()) ? "" : "（"
						+ model.getJdS() + "）";
				// 居地繁体
				String jdt = StringUtilC.isEmpty(model.getJdT()) ? "" : "（"
						+ model.getJdT() + "）";
				// ///////////////////////////////////最里层Array
				_rootObj5.put("value",
						StringUtilC.getChs(model.getZhengshuming()) + jds);
				_rootObj5.put("language", "chs");
				_jsonArrayRoot4.add(_rootObj5);
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getZhengshuming() + jdt);
				_rootObj5.put("language", "cht");
				_jsonArrayRoot4.add(_rootObj5);
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////////////居地
			if (!StringUtilC.isEmpty(model.getJudiUri())) {
				// 如果居地URI不为空
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "shl:place");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getJudiUri());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			} else {
				// 如果居地匹配不到URI，且居地属性不为空，则写入bf:edition
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
		}
		// 插入数据
		InsertV(_jsonArrayRoot);

	}

	/**
	 * 书目插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertShumu33(List<ShumuModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			if (!StringUtilC.isEmpty(model.getZhengshuming())) {
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "dc:title");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getZhengshuming());
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
			}
			// /////////////////////////////////////////////标识符

			/*
			 * _rootObj4 = new JSONObject(); _rootObj4.put("uri",
			 * "bf:identifiedBy"); _rootObj4.put("type", "literal");
			 * _rootObj4.put("repeatable", "false"); _rootObj5 = new
			 * JSONObject(); _rootObj5.put("value", model.getIdentifier());
			 * _rootObj4.put("objects", _rootObj5);
			 * _jsonArrayRoot3.add(_rootObj4); //
			 * ///////////////////////////////////////////居地 if if
			 * (!StringUtilC.isEmpty(model.getJudiUri())) {// 如果居地URI不为空
			 * _rootObj4 = new JSONObject(); _rootObj4.put("uri", "shl:place");
			 * _rootObj4.put("type", "resource"); _rootObj4.put("repeatable",
			 * "false"); _rootObj5 = new JSONObject(); _rootObj5.put("value",
			 * model.getJudiUri()); _rootObj4.put("objects", _rootObj5);
			 * _jsonArrayRoot3.add(_rootObj4); } else { //
			 * 如果居地匹配不到URI，且居地属性不为空，则写入bf:edition if if
			 * (!StringUtilC.isEmpty(model.getJudi())) { _rootObj4 = new
			 * JSONObject(); _rootObj4.put("uri", "bf:edition");
			 * _rootObj4.put("type", "literal"); _rootObj4.put("repeatable",
			 * "false"); _rootObj5 = new JSONObject(); _rootObj5.put("value",
			 * model.getJudi()); _rootObj4.put("objects", _rootObj5);
			 * _jsonArrayRoot3.add(_rootObj4); } }
			 */
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_WORK);
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "bf:Work");
			_rootObj3.put("properties", _jsonArrayRoot3);
			_jsonArrayRoot.add(_rootObj3);
			// 打印json
			// System.out.println("插入JSON：" + _rootObj3.toString());
		}
		// 插入数据

		InsertV(_jsonArrayRoot);

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
	public static void InsertZhengshuming(List<ShumuModel> modelList) {
		// /////////////////////////正题名发布/////////////////////////////////////////
		// bf:WorkTitle
		JSONArray _jsonArrayRoot = new JSONArray();

		for (ShumuModel model : modelList) {
			if (!StringUtilC.isEmpty(model.getZhengshuming())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				// 根节点
				JSONObject _rootObj5 = new JSONObject();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// /////////////////////////////////////////////正书名
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getZhengshuming());
				_rootObj5.put("language", "cht");
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_TITLE);
				_rootObj3.put("uri", model.getPumingUri());
				_rootObj3.put("type", "bf:WorkTitle");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
			// 打印json
			// System.out.println("插入JSON：" + _rootObj3.toString());
		}
		// 插入数据

		InsertV(_jsonArrayRoot);
		/*--------------------------书目发布---------------------------------------------*/
		// 根Array
		_jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			if (!StringUtilC.isEmpty(model.getPumingUri())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				// 根节点
				JSONObject _rootObj5 = new JSONObject();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// /////////////////////////////////////////////正书名

				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:title");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "true");
				_rootObj5 = new JSONObject();
				// 正题名URi
				_rootObj5.put("value", model.getPumingUri());

				_jsonArrayRoot4.add(_rootObj5);
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);

				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_WORK);
				// work,书目URI
				_rootObj3.put("uri", model.getShumuUri());
				_rootObj3.put("type", "bf:Work");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
				// 打印json
				// System.out.println("插入JSON：" + _rootObj3.toString());
			}
		}
		// 插入数据

		InsertV(_jsonArrayRoot);

	}

	/**
	 * 变异名插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertBianyishuming(List<ShumuModel> modelList) {
		// /////////////////////////变异题名发布/////////////////////////////////////////
		// bf:VariantTitle 变异题名发布
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			if (!StringUtilC.isEmpty(model.getZhengshuming().trim())
					&& !StringUtilC.isEmpty(model.getPumingUri())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				// 根节点
				JSONObject _rootObj5 = new JSONObject();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// /////////////////////////////////////////////变异题名
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:label");
				_rootObj4.put("type", "literal");
				_rootObj4.put("repeatable", "false");
				_rootObj5 = new JSONObject();
				_rootObj5.put("value", model.getZhengshuming().trim());
				_rootObj5.put("language", "cht");
				_rootObj4.put("objects", _rootObj5);
				_jsonArrayRoot3.add(_rootObj4);
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_TITLE);
				_rootObj3.put("uri", model.getPumingUri());
				_rootObj3.put("type", "bf:VariantTitle");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
		}
		// 插入数据
		InsertV(_jsonArrayRoot);
		/*--------------------------书目发布---------------------------------------------*/
		// 根Array
		_jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			if (!StringUtilC.isEmpty(model.getPumingUri())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
				// 根节点
				JSONObject _rootObj5 = new JSONObject();
				JSONObject _rootObj4 = new JSONObject();
				JSONObject _rootObj3 = new JSONObject();
				// /////////////////////////////////////////////正书名
				_rootObj4 = new JSONObject();
				_rootObj4.put("uri", "bf:title");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "true");
				_rootObj5 = new JSONObject();
				// 变异题名URi
				_rootObj5.put("value", model.getPumingUri());
				_jsonArrayRoot4.add(_rootObj5);
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
				// /////////////////////////////////倒数第三层array
				_rootObj3.put("graph", Constant.GRAPH_WORK);
				// work,书目URI
				_rootObj3.put("uri", model.getShumuUri());
				_rootObj3.put("type", "bf:Work");
				_rootObj3.put("properties", _jsonArrayRoot3);
				_jsonArrayRoot.add(_rootObj3);
			}
		}
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * 责任者插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertZherenzhe(List<ShumuModel> modelList, String type) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// /////////////////////////////////////////////责任者List
			List<ShumuModel> zrzList = model.getOthersList();
			if (zrzList != null && zrzList.size() > 0) {// 如果居地URI不为空
				_rootObj4 = new JSONObject();
				// 其他责任者
				if (type == "2") {
					_rootObj4.put("uri", "bf:contributor");
				} else {
					_rootObj4.put("uri", "bf:creator");
				}
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "true");
				for (ShumuModel zrzInfo : zrzList) {
					_rootObj5 = new JSONObject();
					// 纂修者URi
					_rootObj5.put("value", zrzInfo.getZxzUri());
					_jsonArrayRoot4.add(_rootObj5);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			} else {
				// 其他责任者
				if (type == "2") {
					// 如果其他责任者匹配不到URI，且其他责任者属性不为空，则写入bf:edition
					if (!StringUtilC.isEmpty(model.getQitazherenzhe())) {
						_rootObj4 = new JSONObject();
						_rootObj4.put("uri", "bf:edition");
						_rootObj4.put("type", "literal");
						_rootObj4.put("repeatable", "false");
						_rootObj5 = new JSONObject();
						_rootObj5.put("value", model.getQitazherenzhe());
						_rootObj4.put("objects", _rootObj5);
						_jsonArrayRoot3.add(_rootObj4);
					}
				} else {
					// 如果责任者匹配不到URI，且责任者属性不为空，则写入bf:edition
					if (!StringUtilC.isEmpty(model.getZherenzhe())) {
						_rootObj4 = new JSONObject();
						_rootObj4.put("uri", "bf:edition");
						_rootObj4.put("type", "literal");
						_rootObj4.put("repeatable", "false");
						_rootObj5 = new JSONObject();
						_rootObj5.put("value", model.getZherenzhe());
						_rootObj4.put("objects", _rootObj5);
						_jsonArrayRoot3.add(_rootObj4);
					}
				}
			}

			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_WORK);
			// work,书目URI
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "bf:Work");
			_rootObj3.put("properties", _jsonArrayRoot3);
			// 如果有值需要插入，则拼接
			if (_jsonArrayRoot3.size() > 0) {
				// System.out.println(_jsonArrayRoot3.toString());
				_jsonArrayRoot.add(_rootObj3);
			}
			// System.out.println("责任者插入JSON：" + _rootObj3.toString());
		}
		// System.out.println("责任者插入JSONAll：" + _jsonArrayRoot.toString());
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * 堂号插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertTanghao(List<ShumuModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			// /////////////////////////////////////////////堂号List
			List<ShumuModel> zrzList = model.getOthersList();
			if (zrzList != null && zrzList.size() > 0) {// 如果居地URI不为空
				_rootObj4 = new JSONObject();
				// 堂号
				_rootObj4.put("uri", "bf:subject");
				_rootObj4.put("type", "resource");
				_rootObj4.put("repeatable", "true");
				for (ShumuModel zrzInfo : zrzList) {
					_rootObj5 = new JSONObject();
					// 堂号URi
					_rootObj5.put("value", zrzInfo.getRefUri());
					_jsonArrayRoot4.add(_rootObj5);
				}
				_rootObj4.put("objects", _jsonArrayRoot4);
				_jsonArrayRoot3.add(_rootObj4);
			} else {
				// 如果责任者匹配不到URI，且责任者属性不为空，则写入bf:edition
				if (!StringUtilC.isEmpty(model.getTanghao())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:edition");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getTanghao());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
			}

			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_WORK);
			// work,书目URI
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "bf:Work");
			_rootObj3.put("properties", _jsonArrayRoot3);
			// 如果有值需要插入，则拼接
			if (_jsonArrayRoot3.size() > 0) {
				_jsonArrayRoot.add(_rootObj3);
			}
			// System.out.println("责任者插入JSON：" + _rootObj3.toString());
		}
		// System.out.println("责任者插入JSONAll：" + _jsonArrayRoot.toString());
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * 姓氏插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertFamilyName(List<ShumuModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			JSONArray _jsonArrayRoot3 = new JSONArray();
			JSONArray _jsonArrayRoot4 = new JSONArray();
			// 根节点
			JSONObject _rootObj5 = new JSONObject();
			JSONObject _rootObj4 = new JSONObject();
			JSONObject _rootObj3 = new JSONObject();
			_rootObj4 = new JSONObject();
			// 姓氏
			_rootObj4.put("uri", "bf:subject");
			_rootObj4.put("type", "resource");
			_rootObj4.put("repeatable", "true");
			_rootObj5 = new JSONObject();
			// 堂号URi
			_rootObj5.put("value", model.getRefUri());
			_jsonArrayRoot4.add(_rootObj5);
			_rootObj4.put("objects", _jsonArrayRoot4);
			_jsonArrayRoot3.add(_rootObj4);
			// /////////////////////////////////倒数第三层array
			_rootObj3.put("graph", Constant.GRAPH_WORK);
			// work,书目URI
			_rootObj3.put("uri", model.getSelfUri());
			_rootObj3.put("type", "bf:Work");
			_rootObj3.put("properties", _jsonArrayRoot3);
			// 如果有值需要插入，则拼接
			if (_jsonArrayRoot3.size() > 0) {
				_jsonArrayRoot.add(_rootObj3);
			}
		}
		// 插入数据
		InsertV(_jsonArrayRoot);
	}

	/**
	 * Instance信息插入
	 * 
	 * @param modelList
	 * @return
	 */
	public static void InsertInstance(List<ShumuModel> modelList) {
		try {
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			for (ShumuModel model : modelList) {
				// 插入类型：
				/*
				 * String intanceType = getInstanceType(model
				 * .getBanbenleixingUri());
				 */
				// 如果版本类型不为空，则进行Instance插入

				JSONArray _jsonArrayRoot3 = new JSONArray();
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
				// 版本年代
				if (!StringUtilC.isEmpty(model.getBanbenniandai())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:temporalValue");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBanbenniandai());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 版本年代Year
				if (!StringUtilC.isEmpty(model.getBanbenniandaiYear())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:temporalValue");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBanbenniandaiYear());
					_rootObj5.put("datatype", "int");
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 版本年代:URi格式
				if (!StringUtilC.isEmpty(model.getRefChaodaiUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:temporal");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getRefChaodaiUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 镌刻者
				if (!StringUtilC.isEmpty(model.getJuankezhe())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:engravedBy");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getJuankezhe());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 版本类型 URI
				if (!StringUtilC.isEmpty(model.getBanbenleixingUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:edition");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBanbenleixingUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}// 版本类型 汉字
				else {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:description");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getBanbenleixing());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 装订 URI
				if (!StringUtilC.isEmpty(model.getZhuangdingUri())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "bf:category");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getZhuangdingUri());
					_rootObj4.put("objects", _rootObj5);
					_jsonArrayRoot3.add(_rootObj4);
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
		}
	}

	/**
	 * Item插入
	 * 
	 * @param modelList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static void InsertItem(List<ShumuModel> modelList) {
		try {

			String prex = "http://data.library.sh.cn/jp/resource/item/";
			// 根Array
			JSONArray _jsonArrayRoot = new JSONArray();
			for (ShumuModel model : modelList) {
				// 收藏者不为空，做入Item
				if (!StringUtilC.isEmpty(model.getShouchangzhe())) {
					JSONArray _jsonArrayRoot3 = new JSONArray();
					// 根节点
					JSONObject _rootObj5 = new JSONObject();
					JSONObject _rootObj4 = new JSONObject();
					JSONObject _rootObj3 = new JSONObject();
					// /////////////////////////////////////////////收藏者List
					List<ShumuModel> zrzList = model.getOthersList();
					// 所有的收藏者
					// //////////////////////匹配不到的机构listBegin/////////////////////////////////
					List<String> allSczList = new ArrayList<String>();
					String yString = model.getShouchangzhe().trim();
					if (!StringUtilC.isEmpty(yString)) {
						yString.replace("(", "（").replace(")", "）");
						yString = yString.replace("\"", "\\\\\"")
								.replace("\n", "").replace(",", "，").trim();
						allSczList.addAll(Arrays.asList(yString.split("\\*")));
					}
					// 在所有收藏者中，移除已经匹配到URI的收藏者，剩余的为个人收藏者
					if (zrzList != null && zrzList.size() > 0) {// 收藏者URI不为空
						for (Iterator it = allSczList.iterator(); it.hasNext();) {
							String str = (String) it.next();
							for (int j = 0; j < zrzList.size(); j++) {
								// 繁体
								String temStrT = zrzList.get(j)
										.getJigoujianchengT().trim();
								// 简体
								String temStrS = zrzList.get(j)
										.getJigoujianchengS().trim();
								if (str.startsWith(temStrT)
										|| str.startsWith(temStrS)) {
									it.remove();
									break;
								}
							}
						}
					}
					// //////////////////////////匹配不到的机构listEnd///////////////////////////////////
					if (zrzList != null && zrzList.size() > 0) {
						for (ShumuModel zrzInfo : zrzList) {
							_rootObj3 = new JSONObject();
							_jsonArrayRoot3 = new JSONArray();
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
							_rootObj4 = new JSONObject();
							// 馆藏地
							_rootObj4.put("uri", "bf:heldBy");
							_rootObj4.put("type", "resource");
							_rootObj4.put("repeatable", "false");
							_rootObj5 = new JSONObject();
							_rootObj5.put("value", zrzInfo.getRefUri());
							_rootObj4.put("objects", _rootObj5);
							_jsonArrayRoot3.add(_rootObj4);
							if (!StringUtilC.isEmpty(model.getShouchangzhe())) {
								// 括号内内容
								String descString = PersonUtils.getDescription(
										zrzInfo.getShouchangzhe(),
										zrzInfo.getJigoujianchengT());
								if (!StringUtilC.isEmpty(descString)) {
									_rootObj4 = new JSONObject();
									_rootObj4.put("uri", "shl:description");
									_rootObj4.put("type", "literal");
									_rootObj4.put("repeatable", "false");
									_rootObj5 = new JSONObject();
									_rootObj5.put("value", descString);
									_rootObj4.put("objects", _rootObj5);
									_jsonArrayRoot3.add(_rootObj4);
								}

							}
							// /////////////////////////////////倒数第三层array
							_rootObj3.put("graph", Constant.GRAPH_ITEM);
							String itemUri = prex + getRandomUriValue(16);
							// work,书目URI
							_rootObj3.put("uri", itemUri);
							_rootObj3.put("type", "bf:Item");
							_rootObj3.put("properties", _jsonArrayRoot3);
							// 如果有值需要插入，则拼接
							if (_jsonArrayRoot3.size() > 0) {
								_jsonArrayRoot.add(_rootObj3);
							}
						}
					}
					// /////////////////////////有匹配到，有匹配不到Begin////////////////////////////////
					if (allSczList != null && allSczList.size() > 0) {// 收藏者URI不为空
						for (String zrzInfo : allSczList) {
							_rootObj3 = new JSONObject();
							_jsonArrayRoot3 = new JSONArray();
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
							_rootObj4 = new JSONObject();
							_rootObj4.put("uri", "shl:description");
							_rootObj4.put("type", "literal");
							_rootObj4.put("repeatable", "false");
							_rootObj5 = new JSONObject();
							_rootObj5.put("value", zrzInfo);
							_rootObj4.put("objects", _rootObj5);
							_jsonArrayRoot3.add(_rootObj4);
							// /////////////////////////////////倒数第三层array
							_rootObj3.put("graph", Constant.GRAPH_ITEM);
							String itemUri = prex + getRandomUriValue(16);
							// work,书目URI
							_rootObj3.put("uri", itemUri);
							_rootObj3.put("type", "bf:Item");
							_rootObj3.put("properties", _jsonArrayRoot3);
							// 如果有值需要插入，则拼接
							if (_jsonArrayRoot3.size() > 0) {
								_jsonArrayRoot.add(_rootObj3);
							}
						}
						// /////////////////////////有匹配到，有匹配不到ENd////////////////////////////////

					}

				}
			}
			// 插入数据
			InsertV(_jsonArrayRoot);
		} catch (Exception e) {
			System.out.println("数据导入错误：" + e);
		}
	}

	/**
	 * 废弃
	 * 
	 * @param modelList
	 */
	public static void InsertItemBack(List<ShumuModel> modelList) {
		// 根Array
		JSONArray _jsonArrayRoot = new JSONArray();
		for (ShumuModel model : modelList) {
			// 收藏者不为空，做入Item
			if (!StringUtilC.isEmpty(model.getShouchangzhe())) {
				JSONArray _jsonArrayRoot3 = new JSONArray();
				JSONArray _jsonArrayRoot4 = new JSONArray();
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
				// /////////////////////////////////////////////收藏者List
				List<ShumuModel> zrzList = model.getOthersList();
				if (zrzList != null && zrzList.size() > 0) {// 收藏者URI不为空
					_rootObj4 = new JSONObject();
					// 堂号
					_rootObj4.put("uri", "bf:heldBy");
					_rootObj4.put("type", "resource");
					_rootObj4.put("repeatable", "true");
					for (ShumuModel zrzInfo : zrzList) {
						_rootObj5 = new JSONObject();
						// 堂号URi
						_rootObj5.put("value", zrzInfo.getRefUri());
						_jsonArrayRoot4.add(_rootObj5);
					}
					_rootObj4.put("objects", _jsonArrayRoot4);
					_jsonArrayRoot3.add(_rootObj4);
				}
				// 所有的收藏者，均写入shl:description
				if (!StringUtilC.isEmpty(model.getShouchangzhe())) {
					_rootObj4 = new JSONObject();
					_rootObj4.put("uri", "shl:description");
					_rootObj4.put("type", "literal");
					_rootObj4.put("repeatable", "false");
					_rootObj5 = new JSONObject();
					_rootObj5.put("value", model.getShouchangzhe());
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
			// System.out.println("责任者插入JSON：" + _jsonArrayRoot.toString());
		}
		// System.out.println("责任者插入JSONAll：" + _jsonArrayRoot.toString());
		// 插入数据
		InsertV(_jsonArrayRoot);
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
	
}
