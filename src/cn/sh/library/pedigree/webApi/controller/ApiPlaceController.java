package cn.sh.library.pedigree.webApi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.framework.util.PreloadChaodaiList;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.dto.WorkApiDto;
import cn.sh.library.pedigree.webApi.services.ApiPlaceService;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/webapi")
public class ApiPlaceController extends BaseController {
	@Resource
	private ApiPlaceService api_placeService;
	@Resource
	private ApiWorkService api_workService;
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(ApiPlaceController.class);

	@ResponseBody
	@RequestMapping(value = "/getWorklistInArea", method = RequestMethod.GET)
	public String listInArea(@Valid String points, @Valid String fname) {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("data", this.api_placeService.list(points, fname));
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorklistInArea列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
	/**
	 * 根据圆形经纬度，半径查询
	 * @param points 120.259438,31.618869
	 * @param radius
	 * @param fname
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorklistInAreaByCircle", method = RequestMethod.GET)
	public String getWorklistInAreaByCircle(@Valid String points,@Valid Integer radius, @Valid String fname) {
		jsonResult = new HashMap<>();
		try {
			radius=radius>3000?3000:radius;//最大为中国半径，地球半径：6371
			jsonResult.put("data",this.api_placeService.listByCircle(points,radius, fname));
			
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorklistInArea列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/worklistInArea", method = RequestMethod.GET)
	public String worklistInArea(@Valid String points, @Valid String fname, String freetext, String startYear, String endYear) {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("data", this.api_placeService.area(points, fname, freetext, startYear, endYear));
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorklistInArea列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/worklistInAreaCount", method = RequestMethod.GET)
	public String worklistInAreaCount(@Valid String points, @Valid String fname, String freetext, String startYear, String endYear) {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("data", this.api_placeService.areaCount(points, fname, freetext, startYear, endYear));
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorklistInArea列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 地图上点击小旗子展开查询家谱列表
	 * 
	 * @param uri
	 * @param familyName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkListByPlaceUriAndFname", method = RequestMethod.GET)
	public String workListByPlaceUriAndFname(@Valid String placeUri,
			@Valid String fname) {
		jsonResult = new HashMap<>();
		try {
			Map<String, Object> result = new HashMap<>();
			if ("all".equals(fname)) {
				fname = "";
			}
			Map<String, String> standPlace = this.api_placeService
					.getStandPlace(placeUri);
			result.put("standPlace", standPlace);
			List<Map<String, String>> works = this.api_placeService
					.getPlaceWorks(standPlace, fname);
			result.put("works", works);
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorkListByPlaceUriAndFname列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}


	/**
	 * 地图上点击小旗子展开查询家谱列表
	 * 
	 * @param uri
	 * @param familyName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workListByPlaceUriAndFname", method = RequestMethod.GET)
	public String workListByPlaceUriAndFname(@Valid String placeUri,
			@Valid String fname, String freetext, String startYear, String endYear) {
		jsonResult = new HashMap<>();
		try {
			Map<String, Object> result = new HashMap<>();
			if ("all".equals(fname)) {
				fname = "";
			}
			Map<String, String> standPlace = this.api_placeService
					.getStandPlace(placeUri);
			result.put("standPlace", standPlace);
			List<Map<String, String>> works = this.api_placeService
					.getPlaceWorks(standPlace, fname, freetext, startYear, endYear);
			result.put("works", works);
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorkListByPlaceUriAndFname列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
	/**
	 * 首页 时空检索
	 * 
	 * @param year
	 * @param isMore
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDynastyByYear/{year}", method = RequestMethod.GET)
	public String getDynastyByYear(@PathVariable("year") String year) {
		jsonResult = new HashMap<>();
		String dynasty = "";
		try {
			// 如果年份不为空 and 年份是数字
			if (!StringUtilC.isEmpty(year) && StringUtilC.isInteger(year)) {
				// 判断年份是否小于1950年
				if (StringUtilC.getInteger(year) < 1950) {

					/*
					 * ChaodaiModel cdInfo = WebApiUtils.getCdInfoByYear(year);
					 * if(!StringUtilC.isEmpty(cdInfo.getData())){
					 * dynasty=cdInfo.getData().split(",")[0]; }
					 */

					// 根据年份获得朝代信息 @autjor陈铭
					dynasty = PreloadChaodaiList.getInstance()
							.getDynastyByYear(year);

				} else {
					dynasty = "当代";
				}

			} else {
				dynasty = "";
			}

			Map<String, Object> result = new HashMap<>();
			// 朝代信息
			result.put("dynasty", dynasty);
			// 年份信息
			result.put("year", year);
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorkListInMapByYear列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 首页 时空检索
	 * 
	 * @param year
	 * @param isMore
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkListInMapByYear", method = RequestMethod.GET)
	public String getWorkListInMapByYear(@Valid String year,
			@Valid Boolean isMore) {
		jsonResult = new HashMap<>();
		try {
			Map<String, Object> result = new HashMap<>();
			List<WorkApiDto> works = api_placeService.listPlacesInChao(year,
					isMore);
			// 更多查询显示

			// 更多查询显示
			if (works != null && works.size() >= 20) {
				result.put("isShowMore", true);
			} else {
				result.put("isShowMore", false);
			}
			result.put("works", works);
			// 朝代信息
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-getWorkListInMapByYear列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 国内地名列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlaceCList", method = RequestMethod.GET)
	public String getPlaceCList(String keyword) {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("data", this.api_placeService.list(keyword));
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-listPlaces列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 海外地名列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlaceFList", method = RequestMethod.GET)
	public String getPlaceFList(String keyword) {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("data",
					this.api_placeService.getForeignPlaces(keyword));
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("WebApiPlaceController-listPlaces列表错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
}
