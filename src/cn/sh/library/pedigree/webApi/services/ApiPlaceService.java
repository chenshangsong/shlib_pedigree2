package cn.sh.library.pedigree.webApi.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.webApi.dto.WorkApiDto;

/**
 * 地域
 * @author liuyi
 * @date 2014/12/27 0027
 */
public interface ApiPlaceService {

    /**
     * 加载地域列表
     * @return
     */
    public List<Map<String, String>> list();
    /**
     * 加载地域列表
     * @return
     */
    public List<Map<String, String>> list(String keyword);
    /**
     * 根据区域加载地域列表
     * @param points
     * @return
     */
    public List<Map<String, String>> list(String points, String familyName);
    /**
     * 根据区域加载地域列表 
     * @param points
     * @return
     */
    public Map listByCircle(String points, Integer distance,String familyName);
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
    public List<WorkApiDto> listPlacesInChao(String year,Boolean isMore);

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
    public List<Map<String, String>> getForeignPlaces(String keyword);
	List<Map<String, String>> area(String points, String familyName, String freetext, String startYear, String endYear);
	List<Map<String, String>> areaCount(String points, String familyName, String freetext, String startYear,
			String endYear);
	List<Map<String, String>> getPlaceWorks(Map standPlace, String familyName, String freetext, String startYear,
			String endYear);
}
