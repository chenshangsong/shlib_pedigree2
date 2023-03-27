package cn.sh.library.pedigree.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.DateUtilC;

/**
 * @author liuyi
 * @date 2014/12/27 0027
 */
@Controller
@RequestMapping("/service/place")
public class PlaceController {
	@Autowired
	public WorkSparql ws;
	@Resource
	private PlaceService placeService;

	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(PlaceController.class);
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		return "place/list";
	}
	@RequestMapping(value = "/listfull", method = RequestMethod.GET)
	public String listfull(Model model) {
		return "place/listfull";
	}
	
	/**
	 * 地图浏览，newadd chenss
	 * @return
	 */
	@RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map(){
        return "place/map";
    }
	/**
	 * 地名列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listPlaces", method = RequestMethod.GET)
	public List<Map<String, String>> list() {
		try {
			return this.placeService.list();
		} catch (Exception e) {
			logger.info("PlaceController-listPlaces列表错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return null;
	}
	/**
	 * 行政机构列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listOrigin", method = RequestMethod.GET)
	public List<Map<String, String>> listOrigin() {
		try {
			return this.placeService.listPlacesInOrigin();
		} catch (Exception e) {
			logger.info("PlaceController-listOrigin列表错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return null;
	}
	/**
	 * 国外机构
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listOriginF", method = RequestMethod.GET)
	public List<Map<String, String>> listOriginF() {
		try {
			return this.placeService.getForeignPlaces();
		} catch (Exception e) {
			logger.info("PlaceController-listOriginF列表错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/listInArea", method = RequestMethod.GET)
	public List<Map<String, String>> listInArea(String points, String familyName) {
		try {
			return this.placeService.list(points, familyName);
		} catch (Exception e) {
			logger.info("PlaceController-listInArea列表错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/name")
	public String getCityName(String longx, String lat) {
		return this.placeService.getCityByPoint(longx, lat);
	}

	
	@ResponseBody
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public Map<String, Object> view(String uri, String familyName) {
		Map<String, Object> result = new HashMap<>();
		try {
			if("all".equals(familyName)){
				familyName="";
			}
			Map<String, String> standPlace = this.placeService.getStandPlace(uri);
			result.put("standPlace", standPlace);
			List<Map<String, String>> works = this.placeService
					.getPlaceWorks(standPlace,familyName);
			result.put("works", works);
			List<Map<String, String>> rdfs = this.placeService.getRDF(uri);
			result.put("rdfs", rdfs);
		} catch (Exception e) {
			logger.info("PlaceController-view列表错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		
		return result;
	}

}
