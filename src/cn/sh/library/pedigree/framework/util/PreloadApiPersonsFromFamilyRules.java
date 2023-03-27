package cn.sh.library.pedigree.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.webApi.services.ApiPersonService;

/**
 * 预加载 @author陈铭
 */
public class PreloadApiPersonsFromFamilyRules implements IfPreloadBean {

	@Resource
	private ApiPersonService apiPersonService;

	private static PreloadApiPersonsFromFamilyRules instance;

	public List<String> provinces = new ArrayList<String>();
	public List<Map<String, String>> provsFromFamilyRules = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> persons = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> familyNames = new ArrayList<Map<String, String>>();

	/**
	 * 实例化方法
	 */
	public static synchronized PreloadApiPersonsFromFamilyRules getInstance() {
		if (null == instance) {
			instance = new PreloadApiPersonsFromFamilyRules();
		}
		return instance;
	}

	@Override
	public void loadInfo() {

		// 判断SearchSource是否被实例化
		if (instance == null) {
			getInstance();
		}
		// 判断InstanceService是否被实例化
		if (instance.apiPersonService == null) {
			instance.apiPersonService = apiPersonService;
		}

		// 获取所有省份
		instance.provinces = instance.apiPersonService.getProvList();

		// 获取家规家训相关所有 省份 列表
		instance.provsFromFamilyRules = instance.apiPersonService
				.getProvsFromFamilyRules();

		// 获取家规家训相关所有 人物 列表
		instance.persons = instance.apiPersonService
				.getPersonsFromFamilyRules();

		List<Map<String, String>> persons = instance.persons;
		// 按照 familyNameUri 降序
		Collections.sort(persons, new Comparator<Map<String, String>>() {
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				return (o2.get("familyNameUri")).compareTo((o1
						.get("familyNameUri")));
			}
		});

		List<Map<String, String>> familyNames = new ArrayList<Map<String, String>>();
		Map<String, String> newMap = null;
		String lastUri = "";

		for (Map<String, String> tempMap : persons) {
			if (!(lastUri.equals(tempMap.get("familyNameUri")))) {
				newMap = new HashMap<String, String>();
				newMap.put("familyNameUri", tempMap.get("familyNameUri"));
				newMap.put("releatedWorks", tempMap.get("releatedWorks"));
				newMap.put("familyName", tempMap.get("familyName"));
				familyNames.add(newMap);
			} else if (!newMap.get("releatedWorks").contains(
					tempMap.get("releatedWorks"))) {
				newMap.put("releatedWorks", newMap.get("releatedWorks") + ","
						+ tempMap.get("releatedWorks"));
			}
			lastUri = tempMap.get("familyNameUri");
		}
		// 获取家规家训相关所有 姓氏 列表
		instance.familyNames = familyNames;
	}

	@Override
	public void reloadInfo() {
		loadInfo();
	}

	// getProvinces()方法
	public List<String> getProvinces() {
		if (instance == null) {
			reloadInfo();
		}
		return instance.provinces;
	}

	// getProvsFromFamilyRules()方法
	public List<Map<String, String>> getProvsFromFamilyRules() {
		if (instance == null) {
			reloadInfo();
		}
		return instance.provsFromFamilyRules;
	}

	// getPersons()方法
	public List<Map<String, String>> getPersons() {
		if (instance == null) {
			reloadInfo();
		}
		return instance.persons;
	}

	// getFamilyNames()方法
	public List<Map<String, String>> getFamilyNames() {
		if (instance == null) {
			reloadInfo();
		}
		return instance.familyNames;
	}

}
