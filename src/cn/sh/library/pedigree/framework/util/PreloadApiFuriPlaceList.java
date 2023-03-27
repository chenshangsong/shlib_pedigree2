package cn.sh.library.pedigree.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiPersonService;

/**
 * 上图胶卷
 * @author 陈铭
 * 单例使用
 */
public class PreloadApiFuriPlaceList implements IfPreloadBean {
	@Resource
	private ApiPersonService apiPersonService;
	
	@Resource
	private PlaceService placeService;
	
	private static PreloadApiFuriPlaceList instance;
	
	private List<Map<String, String>> list;
	
	private Map<String, Object> placeAll;
	
	/**
	 * 单例使用
	 * 预加载数据类
	 * @author 陈铭
	 */
	public static synchronized PreloadApiFuriPlaceList getInstance() {
		if (null == instance){
			instance =new PreloadApiFuriPlaceList();
		}
		return instance;
	}
	
	/**
	 * 得到用户列表
	 * @param dpudc
	 * @return
	 */
	@Override
	public void loadInfo() {
		if(instance == null){
			getInstance();
		}
		if(instance.apiPersonService == null){
			instance.apiPersonService = apiPersonService;
		}
		if(instance.placeService == null){
			instance.placeService = placeService;
		}
		instance.list = new ArrayList<>();
		instance.list =instance.apiPersonService.listFnamePlaceList() ;
		List<Map<String, String>> listPlaceAll = instance.placeService.getRemoteAllPlaces();
		instance.placeAll = new HashMap<String, Object>();
		String parent = "";
		Map<Integer, String> mapParent = new HashMap<Integer, String>();
		for(Map<String, String> place: listPlaceAll){
			String uri = place.get("uri").toString();
			String city = place.get("city").toString();
			String county = place.get("county").toString();
			String prov = place.get("prov").toString();
			if(StringUtilC.isEmpty(city)){
				mapParent.put(1, uri);
				parent = "";
			}
			else {
				if(StringUtilC.isEmpty(county)){
					if(prov.equals(city)){
						mapParent.put(2, mapParent.get(1));
					}
					else {
						mapParent.put(2, uri);
					}
					parent = mapParent.get(1);
				}
				else {
					parent = mapParent.get(2);
				}
			}
			place.put("parent", parent);
			instance.placeAll.put(uri, place);
		}
	}
	
	@Override
	public void reloadInfo() {
		loadInfo();
	}

	/**
	 * 根据用户ID，获取用户信息
	 * @param workUri
	 */
	public List<Map<String, String>> getPlaceListbyFuri(String furi) {
		List<Map<String, String>> list = new ArrayList<>();
		if (!StringUtilC.isEmpty(furi) &&  instance.list!=null) {
			for (Map<String, String> map : instance.list) {
				if(map.get("furi").equals(furi)){
					list.add(map);
				}
			}
		}
		return list;
	}
	
	public Map<String, Object> getRemotePlacesAll(){
		return instance.placeAll;
	}
}
