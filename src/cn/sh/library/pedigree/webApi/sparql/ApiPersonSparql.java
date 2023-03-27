package cn.sh.library.pedigree.webApi.sparql;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;

public abstract interface ApiPersonSparql extends BaseDao {
	/**
	 * 根据姓氏URI，省份label,获取该省的
	 * 
	 * @param furi
	 * @param prov
	 * @return
	 */
	public List<Map<String, String>> getPlacesInfos4Person(String prov);

	/**
	 * 省列表
	 * 
	 * @param furi
	 * @param prov
	 * @return
	 */
	public List<Map<String, String>> getProvList();

	/**
	 * 获取家规家训相关所有人物列表级相关workUri
	 * 
	 * @return
	 */
	public List<Map<String, String>> getPersonsFromFamilyRules();

	public List<Map<String, String>> getProvsFromFamilyRules();
}
