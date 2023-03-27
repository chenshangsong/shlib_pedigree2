package cn.sh.library.pedigree.common.dataImport;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.sh.library.pedigree.common.RoleGroup;

/**
 * 规范数据Map
 * 
 * @author chenss
 *
 */
public class DataTypeMap {
	public Map<String, String> GRAPH_MAP = new LinkedHashMap<>();
	public Map<String, String> ROLE_MAP = new LinkedHashMap<>();
	public Map<String, String> DATAURI_MAP = new LinkedHashMap<>();

	public DataTypeMap() {
		GRAPH_MAP.put(DataTypeGroup.Ancestors.getGroup(), "先祖");
		GRAPH_MAP.put(DataTypeGroup.Famous.getGroup(), "名人");
		GRAPH_MAP.put(DataTypeGroup.Writer.getGroup(), "纂修者");
		GRAPH_MAP.put(DataTypeGroup.Ancestraltemple.getGroup(), "堂号");
		GRAPH_MAP.put(DataTypeGroup.FamilyName.getGroup(), "姓氏");
		GRAPH_MAP.put(DataTypeGroup.Organization.getGroup(), "机构");
		GRAPH_MAP.put(DataTypeGroup.Title.getGroup(), "谱名");
		GRAPH_MAP.put(DataTypeGroup.Place.getGroup(), "谱籍");
		GRAPH_MAP.put(DataTypeGroup.Category.getGroup(), "取值词表");
		GRAPH_MAP.put(DataTypeGroup.Work.getGroup(), "书目");

		DATAURI_MAP.put(DataTypeGroup.Ancestors.getGroup(),
				"http://data.library.sh.cn/jp/entity/person/");
		DATAURI_MAP.put(DataTypeGroup.Famous.getGroup(),
				"http://data.library.sh.cn/jp/entity/person/");
		DATAURI_MAP.put(DataTypeGroup.Writer.getGroup(),
				"http://data.library.sh.cn/jp/entity/person/");
		DATAURI_MAP
				.put(DataTypeGroup.Ancestraltemple.getGroup(),
						"http://data.library.sh.cn/jp/authority/titleofancestraltemple/");
		DATAURI_MAP.put(DataTypeGroup.FamilyName.getGroup(),
				"http://data.library.sh.cn/authority/familyname/");
		DATAURI_MAP.put(DataTypeGroup.Organization.getGroup(),
				"http://data.library.sh.cn/entity/organization/");
		DATAURI_MAP.put(DataTypeGroup.Title.getGroup(),
				"http://data.library.sh.cn/jp/authority/title/");
		DATAURI_MAP.put(DataTypeGroup.Place.getGroup(),
				"http://data.library.sh.cn/entity/place/");
		DATAURI_MAP.put(DataTypeGroup.Work.getGroup(),
				"http://data.library.sh.cn/jp/resource/work/");

		ROLE_MAP.put(RoleGroup.normal.getGroup(), "普通用户");
		ROLE_MAP.put(RoleGroup.zj.getGroup(), "专家用户");
		ROLE_MAP.put(RoleGroup.admin.getGroup(), "管理员");

	}

	/**
	 * 根据Key得到name
	 * 
	 * @param key
	 * @return
	 */
	public static String getDataTypeName(String key) {
		return new DataTypeMap().GRAPH_MAP.get(key);
	}

	/**
	 * 根据Key得到name
	 * 
	 * @param key
	 * @return
	 */
	public static String getDataUriName(String key) {
		return new DataTypeMap().DATAURI_MAP.get(key);
	}
	/**
	 * 根据Key得到name
	 * 
	 * @param key
	 * @return
	 */
	public static String getDataRoleName(String key) {
		return new DataTypeMap().ROLE_MAP.get(key);
	}
	/**
	 * 获取待办事项的功能列表
	 * @return
	 */
	public static  Map<String, String> getDataTypeList() {
		return new DataTypeMap().GRAPH_MAP;
	}
	/**
	 * 获取角色列表
	 * @param key
	 * @return
	 */
	public static  Map<String, String> getRoleList() {
		return new DataTypeMap().ROLE_MAP;
	}
	/**
	 * 获取graph列表
	 * @param key
	 * @return
	 */
	public static  Map<String, String> getGraphList() {
		return new DataTypeMap().DATAURI_MAP;
	}
}
