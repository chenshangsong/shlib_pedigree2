package cn.sh.library.pedigree.controller.sysmanager.common;

import java.util.Arrays;

import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditDtoCommon {
	// 多值字段定义
	/*
	 * public static String[] strMoreValueList = { "heldBy", "category",
	 * "edition", "creator", "contributor", "engravedBy", "place", "familyName",
	 * "region", "roleOfFamily" };
	 */
	public static String[] strMoreValueList = { "heldBy", "category",
			"edition", "engravedBy", "place", "familyName", "region",
			"roleOfFamily" };
	// 多值常量
	public static String moreValueString = "@chs,@cht";
	// 多值常量三种：中文，英文，拼音
	public static String moreValue3String = "@chs,@cht,@en";

	/**
	 * 空值处理
	 * 
	 * @param _dto
	 */
	public static DataEditViewDto listEmptyChange(DataEditViewDto _dto) {
		for (int i = 0; i < _dto.getEditList().size(); i++) {
			if (StringUtilC.getString(_dto.getEditList().get(i).getOldValue())
					.contains("_:b")) {
				_dto.getEditList().get(i).setOldValue("");
			}
			if (StringUtilC.getString(_dto.getEditList().get(i).getOldValue())
					.contains("NoN")) {
				_dto.getEditList().get(i).setOldValue("");
			}
			// 需多值处理的字段英文名
			String moreEnName = StringUtilC.getString(_dto.getEditList().get(i)
					.getOldEnName());
			// 姓氏，机构的地区单独处理
			if (Arrays.asList(strMoreValueList).contains(moreEnName)) {
				String value = StringUtilC.getString(_dto.getEditList().get(i)
						.getOldValueCn());
				if (value.length() > 0) {
					String lastValue = "";
					lastValue = getMoreValue(_dto.getEditList().get(i)
							.getOldValueCn());
					_dto.getEditList().get(i).setOldValueCn(lastValue);
				}
			}
		}
		return _dto;
	}

	/**
	 * 多值抽取其中简体中文
	 * 
	 * @param value
	 * @return
	 */
	public static String getMoreValue(String value) {
		// [\"卜世泰@cht\",\"卜世泰@chs\"],[\"卜仰湖@cht\",\"卜仰湖@chs\"],[\"卜商@cht\",\"卜商@chs\"]处理
		value = "[" + value.replace("[", "").replace("]", "") + "]";
		JSONArray array = JSONArray.fromObject(value);
		String v = "";
		for (Object object : array) {
			String tvalue = object.toString();
			if (tvalue.contains("chs")) {
				v += tvalue.substring(0, tvalue.indexOf("@")) + ",";
			}
		}
		// System.out.println(v.substring(0, v.lastIndexOf(",")));
		v = v.substring(0, v.lastIndexOf(","));
		return v;
	}

	/**
	 * 多值抽取其中繁体中文
	 * 
	 * @param value
	 * @return
	 */
	public static String getMoreValueCht(String value) {
		String v = "";
		if (value.contains("@cht") && value.contains("@chs")) {
			JSONArray array = JSONArray.fromObject(value);
			for (Object object : array) {
				String tvalue = object.toString();
				if (tvalue.contains("cht")) {
					v += tvalue.substring(0, tvalue.indexOf("@")) + ",";
				}
			}
			v = v.substring(0, v.lastIndexOf(","));
		} else if (value.contains("@cht") && !value.contains("@chs")) {
			value = value.replace("[", "").replace("]", "").replace("@cht", "");
			return value;
		}
		return v;
	}

	/**
	 * 保存时，值的处理
	 * 
	 * @param s
	 * @return
	 */
	public static String getValue(String s) {
		if (StringUtilC.isEmpty(s)) {
			return "";
		}
		// 导入时，使用
		// s=s.replace("\"","\\\\\"").replace("\n", "").replace(",",
		// "，").trim();
		if (s.contains("http")) {
			return "<" + s + ">";
		}
		if (s.endsWith("@en")) {
			s = "\"" + s.substring(0, s.length() - 3) + "\"" + "@en";
			return s;
		}
		if (s.endsWith("@cht")) {
			s = "\"" + s.substring(0, s.length() - 4) + "\"" + "@cht";
			return s;
		}
		if (s.endsWith("@chs")) {
			s = "\"" + s.substring(0, s.length() - 4) + "\"" + "@chs";
			return s;
		}
		if (s.endsWith("@int")) {
			s = s.substring(0, s.length() - 4);
			return s;
		}
		return "\"" + s + "\"";
	}

	/**
	 * /
	 * @param "信函@chs,信函@cht"
	 * @param formaten,cht,chs
	 * @return 信函
	 */
	public static String getValueFormat(String s, String format) {
		if (StringUtilC.isEmpty(s)) {
			return "";
		}
		String[] values = s.split(",");
		String f = "@" + format;
		for (String o : values) {
			if (f.equals("@en")) {
				if (o.endsWith(f)) {
					o = o.substring(0, o.length() - 3);
					return o;
				}
			} else {
				if (o.endsWith(f)) {
					o = o.substring(0, o.length() - 4);
					return o;
				}
			}
		}
		return "";
	}

	public static void launch(JSONArray json) {
		for (int i = 0; i < json.size(); i++) {
			JSONObject resource = (JSONObject) json.get(i);
			if ((resource.containsKey("uri"))
					&& (resource.containsKey("graph"))) {
				String graph = resource.get("graph").toString();
				System.out.println(graph);
			}
		}
	}
}
