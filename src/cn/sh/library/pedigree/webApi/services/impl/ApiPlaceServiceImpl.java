package cn.sh.library.pedigree.webApi.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.fullContentLink.FullLink;
import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.sparql.GeoSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.ForeignPlacesSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.utils.WebApiUtils;
import cn.sh.library.pedigree.webApi.dto.WorkApiDto;
import cn.sh.library.pedigree.webApi.services.ApiPlaceService;
import cn.sh.library.pedigree.webApi.sparql.ApiWorkSparql;

/**
 * @author liuyi
 * @date 2014/12/27 0027
 */
@Service
public class ApiPlaceServiceImpl extends BaseServiceImpl implements
		ApiPlaceService {

	@Resource
	private PlaceSparql placeSparql;
	@Resource
	private ForeignPlacesSparql foreignPlacesSparql;

	@Resource
	private ApiWorkSparql api_workSparql;
	@Resource
	private GeoSparql geoSparql;

	@Override
	public List<Map<String, String>> list() {
		List<Map<String, Object>> list = placeSparql.getAllPlaces();
		List<Map<String, String>> resultList = RDFUtils.transformListMap(list);
		return resultList;
	}

	@Override
	public List<Map<String, String>> list(String points, String familyName) {
		try {
			if (familyName.equals("all")) {

				return RDFUtils.transformListMap(placeSparql
						.getPlacesInArea(points));

			} else {
				return RDFUtils.transformListMap(placeSparql.getPlacesInArea(
						points, familyName));
			}
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());

		}
		return null;
	}

	@Override
	public List<Map<String, String>> area(String points, String familyName, String freetext, String startYear, String endYear) {
		try {
				return RDFUtils.transformListMap(placeSparql.getPlacesInArea(
						points, familyName, freetext, startYear, endYear));
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());

		}
		return null;
	}

	@Override
	public List<Map<String, String>> areaCount(String points, String familyName, String freetext, String startYear, String endYear) {
		try {
				return RDFUtils.transformListMap(placeSparql.getPlacesInAreaCount(
						points, familyName, freetext, startYear, endYear));
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());

		}
		return null;
	}

	@Override
	public List<Map<String, String>> listPlacesInOrigin() {
		List<Map<String, String>> resultList = RDFUtils
				.transformListMap(this.placeSparql.getAllPlacesInOrigin());
		return resultList;
	}

	@Override
	public Map<String, String> getStandPlace(String uri) {
		List<Map<String, String>> mapList = RDFUtils
				.transformListMap(this.placeSparql.getStandPlace(uri));
		if (CollectionUtils.isNotEmpty(mapList)) {
			return mapList.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, String>> getPlaceWorks(String uri) {
		List<Map<String, String>> mapList = RDFUtils
				.transformListMap(this.api_workSparql.getWorksInLatLong(uri));
		return mapList;
	}

	@Override
	public List<Map<String, String>> getPlaceWorks(Map standPlace,
			String familyName) {
		try {
			List<Map<String, String>> mapList = RDFUtils
					.transformListMap(this.api_workSparql.getWorksInPlace(
							standPlace, familyName));
			return mapList;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public List<Map<String, String>> getPlaceWorks(Map standPlace,
			String familyName, String freetext, String startYear, String endYear) {
		try {
			List<Map<String, String>> mapList = RDFUtils
					.transformListMap(this.api_workSparql.getWorksInPlace(
							standPlace, familyName, freetext, startYear, endYear));
			return mapList;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public List<Map<String, String>> getRDF(String uri) {
		List<Map<String, Object>> mapList = this.placeSparql.getRDF(uri);
		List<Map<String, String>> resultList = new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			Map<String, String> result = new HashMap<>();
			result.put("s", RDFUtils.toString(map.get("s")));
			result.put("p", RDFUtils.toString(map.get("p")));
			result.put("o", map.get("o").toString());
			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public String getCityByPoint(String longx, String lat) {
		return this.geoSparql.getCity4Point(longx, lat);
	}

	@Override
	public List<WorkApiDto> listPlacesInChao(String year, Boolean isMore) {
		try {
			List<WorkApiDto> _list = new ArrayList<>();
			List<Map<String, String>> mapList = RDFUtils
					.transformListMap(api_workSparql.getWorksInChao(year,
							isMore));

			for (Map<String, String> map : mapList) {
				WorkApiDto work = new WorkApiDto();
				String doi = StringUtilC.getString(map.get("doi"));
				// 封面图片
				if (!StringUtilC.isEmpty(doi)) {
					work.setImageFrontPath(WebApiUtils.GetImagePathByDoi(doi));
				}
				map.put("accessLevel", "");
				map.put("fulltextLink", "");
				map.put("fulltextMore", "");
				/*注释 chenss 2018-07-23*/
				//String hasimg= StringUtilC.getString(map.get("hasimg"));
				//String strAccessLevel = StringUtilC.getString(map.get("access"));
				//getOutFullLink(map, doi, hasimg, strAccessLevel);
				work.setUri(map.get("work"));
				work.setTitle(map.get("title"));
				work.setPlace(map.get("place"));
				work.setPlaceUri(map.get("placeUri"));
				work.setCreator(map.get("creators"));
				work.setTangh(map.get("tangh"));
				work.setLat(map.get("lat"));
				work.setLongitude(map.get("long"));
				/*注释 chenss 2018-07-23*/
				/*List<Map<String, String>> instances = RDFUtils
						.transformListMap(this.api_workSparql
								.getInstances4Work(work.getUri()));
				work.setInstances(instances);*/
				_list.add(work);
			}
			return _list;
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	private void getOutFullLink(Map<String, String> map, String doi,
			String hasimg, String strAccessLevel) {
		// 外网全文标识
		if (!StringUtilC.isEmpty(strAccessLevel)
				&& !StringUtilC.isEmpty(doi)) {
			if (strAccessLevel.contains("0")) {
				map.put("accessLevel", "0");
				// 有多个开放全文
				if (strAccessLevel.length()
						- strAccessLevel.replace("0", "").length() > 1) {
					map.put("fulltextMore", "1");
				}
				// 有一个开放全文
				if (strAccessLevel.length()
						- strAccessLevel.replace("0", "").length() == 1) {
					// 只有一个开放全文
					map.put("fulltextMore", "0");
					map.put("fulltextLink", FullLink.GetFullTextImg(
							StringUtilC.getString(map
									.get("accessLevel")), doi));
				}
			} else {// 如果有一个或多个，并且全部未开放
				map.put("accessLevel", "1");
				//如果只查到一个内网开放的数据,并且内网有全文链接
				if((strAccessLevel.length()
						- strAccessLevel.replace("1", "").length() == 1) && "true".equals(hasimg)){
					map.put("fulltextLink",
							FullLink.GetFullTextImg(StringUtilC.getString(map
									.get("accessLevel")), doi));
				}
			}
		}
	}

	@Override
	public List<Map<String, String>> list(String keyword) {
		List<Map<String, Object>> list = placeSparql.getAllPlaces(keyword);
		List<Map<String, String>> resultList = RDFUtils.transformListMap(list);
		return resultList;
	}

	@Override
	public List<Map<String, String>> getForeignPlaces(String keyword) {
		return foreignPlacesSparql.getForeignPlaces(keyword);
	}

	@Override
	public Map listByCircle(String points, Integer distance, String familyName) {
		try {
			if (familyName==null || familyName.equals("all")) {
				familyName="";
			}
			return placeSparql.getPlacesInAreaByCircle(
					points,distance, familyName);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());

		}
		return null;
	}

}
