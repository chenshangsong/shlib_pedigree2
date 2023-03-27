package cn.sh.library.pedigree.dto.sysmanager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataManagerDto {
	private String id;

	/**
	 * 字
	 */
	private String courtesyName;

	/**
	 * 事件
	 */
	private String event;

	/**
	 * 家族
	 */
	private String family;

	/**
	 * 姓氏
	 */
	private String familyName;

	/**
	 * 迁徙
	 */
	private String migration;

	/**
	 * 名
	 */
	private String name;

	/**
	 * 行
	 */
	private String orderOfSeniority;

	/**
	 * 号
	 */
	private String pseudonym;

	/**
	 * 时代
	 */
	private String temporal;

	private String context;
	/*-----------------新增---------------------*/
	private String hasAnnotation;
	private String identifier;
	private String label;
	private String relatedTo;
	private String role;
	private String birthday;
	private String deathday;
	private String genealogyName;
	private String relatedWork;
	private String roleOfFamily;
	private String childOf;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setCourtesyName(String courtesyName) {
		this.courtesyName = courtesyName;
	}

	public String getCourtesyName() {
		return this.courtesyName;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent() {
		return this.event;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getFamily() {
		return this.family;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyName() {
		return this.familyName;
	}

	public void setMigration(String migration) {
		this.migration = migration;
	}

	public String getMigration() {
		return this.migration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setOrderOfSeniority(String orderOfSeniority) {
		this.orderOfSeniority = orderOfSeniority;
	}

	public String getOrderOfSeniority() {
		return this.orderOfSeniority;
	}

	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}

	public String getPseudonym() {
		return this.pseudonym;
	}

	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}

	public String getTemporal() {
		return this.temporal;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return this.context;
	}
	

	public static Map<String, Object> parserToMap(String s) {
		Map<String, Object> map = new HashMap();
		JSONObject json = JSONObject.fromObject(s);
		Iterator keys = json.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = json.get(key).toString();
			if (value.startsWith("{") && value.endsWith("}")) {
				map.put(key, parserToMap(value));
			} else {
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 构建页面编辑属性列表
	 * 
	 * @param jo
	 * @return
	 */
	public static DataEditViewDto fillPersonEditDto(String jsonString) {
		JSONObject jo = JSONObject.fromObject(jsonString);
		DataEditDto _dto = new DataEditDto();
		DataEditViewDto dataEditViewDto = new DataEditViewDto();
		// 得到@context描述下的资源信息
		JSONObject JSONContent = jo.getJSONObject("@context");
		// 编辑主语ID
		if (jo.containsKey("@id")) {
			dataEditViewDto.setId(jo.getString("@id"));
		}
		Iterator keys = jo.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = jo.get(key).toString();
			if (!key.equals("@context") && !key.equals("@id")) {
				_dto = new DataEditDto();
				_dto.setOldEnName(key);
				if ((!value.contains("_:b") && !value.contains("http:") && !key
						.contains("relatedTo"))
						|| (key.contains("temporalValue") || (key
								.contains("http:")))) {
					String cnUri = JSONContent.getString(key);
					// 标题转换
					if (key.equals("titleFullName")) {
						cnUri = "http://purl.org/dc/elements/1.1/title";
					}
					_dto.setOldCnNameUri(cnUri);
				} else {
					String cnUri = JSONContent.getJSONObject(key).getString(
							"@id");
					_dto.setOldCnNameUri(cnUri);
				}
				String val = jo.getString(key);
				
				// 如果是多值
				if (val.startsWith("[") && val.endsWith("]")) {
					// if(val.contains("@chs") || val.contains("@cht") ||
					// val.contains("@en")){
					JSONArray valArray = JSONArray.fromObject(val);
					val = new String();
					for (Object object : valArray) {
						if (StringUtilC.getString(object).contains("@id")) {
							JSONObject jo1 = JSONObject.fromObject(object);
							val += jo1.getString("@id") + ",";
						} else {
							// 如果是日期格式，则截取整形部分
							if (key.contains("temporalValue")) {
								val +=  RDFUtils.getValue(object.toString()) + ",";
							}
							else{
								val += object + ",";
							}
							
						}
					}
					val = val.substring(0, val.lastIndexOf(','));
					val = val.replace("\"", "");
					// 多值标志
					_dto.setIsMore("1");
					// }
				}
				_dto.setOldValue(val.replace("\"", "“"));
				dataEditViewDto.getEditList().add(_dto);
			}
		}
		return dataEditViewDto;
	}

	/**
	 * 构建页面新增属性列表
	 * 
	 * @param jo
	 * @return
	 */
	public static DataEditViewDto fillAddDto(List<Map<String, String>> mylist) {
		DataEditViewDto dataEditViewDto = new DataEditViewDto();
		for (Map<String, String> itemMap : mylist) {
			DataEditDto _dto = new DataEditDto();
			// 截取：后面一位,如：bf: Annotation得到：Annotation
			String range = StringUtilC.getString((Object) itemMap.get("range")
					.split(":")[1]);
			String comment = StringUtilC.getString((Object) itemMap
					.get("comment"));
			String label = StringUtilC.getString((Object) itemMap.get("label"));
			// bf: hasAnnotation得到：hasAnnotation
			String p = StringUtilC.getString((Object) itemMap.get("p").split(
					":")[1]);
			// 全称：bf:label
			String pUri = StringUtilC.getString((Object) itemMap.get("p"));
			// 字段英文名称
			_dto.setOldEnName(p);
			_dto.setOldCnNameUri(pUri);
			// 注释
			_dto.setOldComment(comment);
			// 字段中文名称
			_dto.setOldCnName(label);
			// 判断取值范围是否是资源类型
			if (!range.equals("Literal")) {
				_dto.setIsUri("1");
			}
			_dto.setIsMore("0");
			dataEditViewDto.getEditList().add(_dto);
		}
		return dataEditViewDto;
	}

	/**
	 * 根据JSONObject得到JSONArray对象
	 * 
	 * @param jo
	 * @return
	 */
	public static JSONArray fillJSONArray(JSONObject jo) {
		JSONArray _jsonArray = new JSONArray();
		JSONObject _tempJSON = new JSONObject();
		if (jo.containsKey("@id")) {
			_tempJSON = new JSONObject();
			_tempJSON.put("@id", jo.getString("@id"));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.courtesyName)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.courtesyName,
					jo.getString(PersonGroup.courtesyName));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.event)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.event, jo.getString(PersonGroup.event));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.family)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.family, jo.getString(PersonGroup.family));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.familyName)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.familyName,
					jo.getString(PersonGroup.familyName));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.migration)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.migration,
					jo.getString(PersonGroup.migration));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.name)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.name, jo.getString(PersonGroup.name));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.orderOfSeniority)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.orderOfSeniority,
					jo.getString(PersonGroup.orderOfSeniority));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.pseudonym)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.pseudonym,
					jo.getString(PersonGroup.pseudonym));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey(PersonGroup.temporal)) {
			_tempJSON = new JSONObject();
			_tempJSON.put(PersonGroup.temporal,
					jo.getString(PersonGroup.temporal));
			_jsonArray.add(_tempJSON);
		}
		if (jo.containsKey("@context")) {
			_tempJSON = new JSONObject();
			_tempJSON.put("@context", jo.getString("@context"));
			_jsonArray.add(_tempJSON);
		}
		return _jsonArray;
	}
}