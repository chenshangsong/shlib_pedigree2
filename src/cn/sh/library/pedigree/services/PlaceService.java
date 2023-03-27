package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

/**
 * 地域
 * @author liuyi
 * @date 2014/12/27 0027
 */
public interface PlaceService {

	public List<Map<String, String>> getRemoteAllPlaces();
	
    /**
     * 加载地域列表
     * @return
     */
    public List<Map<String, String>> list();

    /**
     * 根据区域加载地域列表
     * @param points
     * @return
     */
    public List<Map<String, String>> list(String points, String familyName);

    /**
     * 根据经纬度获取地名
     * @param longx
     * @param lat
     * @return
     */
    public String getCityByPoint(String longx, String lat);

    /**
     * 加载谱籍地名表
     * @return
     */
    public List<Map<String, String>> listPlacesInOrigin();
    /**
     * 获取place rdf数据
     * @param uri
     * @return
     */
    public List<Map<String, String>> listPlacesInChao(String year,String uri);

    /**[已升级]
     * 获取家谱地名标准规范名
     * @param uri
     * @return
     */
    public Map<String, String> getStandPlace(String uri);

    /**
     * 根据坐标信息获取works列表
     * @return
     */
    public List<Map<String, String>> getPlaceWorks(String uri);
    public List<Map<String, String>> getPlaceWorks(Map standPlace,String familyName);

    /**
     * 获取place rdf数据
     * @param uri
     * @return
     */
    public List<Map<String, String>> getRDF(String uri);
    public List<Map<String, String>> getForeignPlaces();
}
