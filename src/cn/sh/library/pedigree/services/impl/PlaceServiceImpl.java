package cn.sh.library.pedigree.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.sparql.GeoSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.ForeignPlacesSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * @author liuyi
 * @date 2014/12/27 0027
 */
@Service
public class PlaceServiceImpl extends BaseServiceImpl implements PlaceService {

	@Resource
	private PlaceSparql placeSparql;
	@Resource
	private ForeignPlacesSparql foreignPlacesSparql;

	@Resource
	private WorkSparql workSparql;
	@Resource
	private GeoSparql geoSparql;

	public List<Map<String, String>> getRemoteAllPlaces() {
		return placeSparql.getRemoteAllPlaces();
	}
	
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
				.transformListMap(this.workSparql.getWorksInLatLong(uri));
		return mapList;
	}

	@Override
	public List<Map<String, String>> getPlaceWorks(Map standPlace,
			String familyName) {
		try {
			List<Map<String, String>> mapList = RDFUtils
					.transformListMap(this.workSparql.getWorksInPlace(
							standPlace, familyName));
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
	public List<Map<String, String>> listPlacesInChao(String year, String uri) {
		try {
			List<Map<String, String>> resultList = RDFUtils
					.transformListMap(workSparql.getWorksInChao(year));
			return resultList;
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Map<String, String>> getForeignPlaces() {
		return foreignPlacesSparql.getForeignPlaces();
	}

}
