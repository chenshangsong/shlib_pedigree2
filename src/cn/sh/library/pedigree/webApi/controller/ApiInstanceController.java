package cn.sh.library.pedigree.webApi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.util.JsonUtil;
import cn.sh.library.pedigree.framework.util.PreloadApiPersonsFromFamilyRules;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiInstanceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi/instance")
public class ApiInstanceController extends BaseController {
	@Resource
	private ApiInstanceService apiInstanceService;

	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(ApiInstanceController.class);

	/**
	 * 家规家训接口
	 * 
	 * @param search
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyRules", method = RequestMethod.GET)
	public String getFamilyRules(@Valid String freeText, @Valid Pager pager,
			@Valid String works) {
		jsonResult = new HashMap<>();
		String[] workUris = null;
		if (!StringUtilC.isEmpty(works)) {
			workUris = works.split(",");
		}
		try {
			List<Map<String, String>> _mapList = this.apiInstanceService
					.getFamilyRules(freeText, pager, workUris);
			jsonResult.put("data", _mapList);
			jsonResult.put("pager", pager);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.error("ApiInstanceController-getFamilyRules错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 获取家规家训信息
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyRuleInfo", method = RequestMethod.GET)
	public String getFamilyRuleInfo(@Valid String uri) {
		jsonResult = new HashMap<>();
		try {
			Map<String, Object> _mapList = this.apiInstanceService
					.getFamilyRuleInfo(uri, false);
			return transJson(_mapList).toString();
		} catch (Exception e) {
			logger.error("ApiInstanceController-getFamilyRuleInfo错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 获取家规家训信息
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyRuleInfoWithSub", method = RequestMethod.GET)
	public String getFamilyRuleInfoWithSub(@Valid String uri) {
		jsonResult = new HashMap<>();
		try {
			Map<String, Object> _mapList = this.apiInstanceService
					.getFamilyRuleInfo(uri, true);
			// jsonResult.put("data", _mapList);
			return transJson(_mapList).toString();
		} catch (Exception e) {
			logger.error("ApiInstanceController-getFamilyRuleInfoWithSub错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 获取下一级家规家训标题，内容
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubFamilyRules", method = RequestMethod.GET)
	public String getSubFamilyRules(@Valid String uri) {
		jsonResult = new HashMap<>();
		try {
			List<Map<String, Object>> _mapList = this.apiInstanceService
					.getSubFamilyRules(uri, false);
			// jsonResult.put("data", _mapList);
			return transJson(_mapList).toString();
		} catch (Exception e) {
			logger.error("ApiInstanceController-getSubFamilyRules错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 获取下一级家规家训标题，内容 withSub
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubFamilyRulesWithSub", method = RequestMethod.GET)
	public String getSubFamilyRulesWithSub(@Valid String uri) {
		jsonResult = new HashMap<>();
		if (StringUtilC.isEmpty(uri)) {
			return "error";
		}
		try {
			List<Map<String, Object>> _mapList = this.apiInstanceService
					.getSubFamilyRules(uri, true);
			// jsonResult.put("data", _mapList);
			return transJson(_mapList).toString();
		} catch (Exception e) {
			logger.error("ApiInstanceController-getSubFamilyRulesWithSub错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 家规家训接口
	 * 
	 * @param search
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyRulesByWorks", method = RequestMethod.GET)
	public String getFamilyRulesByWorks(@Valid String works) {
		jsonResult = new HashMap<>();
		if (StringUtilC.isEmpty(works)) {
			return "error";
		}
		String[] workUris = works.split(",");
		try {
			List<Map<String, Object>> _mapList = this.apiInstanceService.getFamilyRulesByWorks(workUris);
			// jsonResult.put("data", _mapList);
			return transJson(_mapList).toString();
		} catch (Exception e) {
			logger.error("ApiInstanceController-getFamilyRulesByWorks错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * @author 陈铭
	 * @param familyNameUri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyRulesByFacet", method = RequestMethod.GET)
	public String getFamilyRulesByFacet(@Valid String val, @Valid Pager pager, Integer type) {
		jsonResult = new HashMap<>();
		if (StringUtilC.isEmpty(val) && StringUtilC.isEmpty(type)) {
			return "error";
		}
		
		String[] releatedWorks = {};
		//	1代表familyNameUri		2代表uri			3代表province
		switch (type) {
			case 1:
				List<Map<String, String>> familyNames = PreloadApiPersonsFromFamilyRules.getInstance().getFamilyNames();
				for (Map<String, String> map : familyNames) {
					if (val.equals(map.get("familyNameUri"))) {
						releatedWorks = (map.get("releatedWorks")).split(",");
						break;
					}
				}
			case 2:
				List<Map<String, String>> persons = PreloadApiPersonsFromFamilyRules.getInstance().getPersons();
				for (Map<String, String> map : persons) {
					if (val.equals(map.get("uri"))) {
						releatedWorks = (map.get("releatedWorks")).split(",");
						break;
					}
				}
			case 3:
				List<Map<String, String>> provsFromFamilyRules = PreloadApiPersonsFromFamilyRules.getInstance().getProvsFromFamilyRules();
				for (Map<String, String> map : provsFromFamilyRules) {
					if (val.equals(map.get("province"))) {
						releatedWorks = (map.get("releatedWorks")).split(",");
						break;
					}
				}
		}
		try {
			List<Map<String, Object>> releatedWorksList = this.apiInstanceService.getFamilyRulesByWorks(releatedWorks, pager);
			if (releatedWorksList!=null && releatedWorksList.size()>0) {
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			jsonResult.put("datas", releatedWorksList);
			jsonResult.put("pager", pager);
			return transJson(jsonResult).toString();

		} catch (Exception e) {
			jsonResult.put(result, FWConstants.result_syserror);
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return JsonUtil.getJsonString(jsonResult);
		}
	}

	
	private Object transJson(Object obj) {
		if (obj instanceof Map) {
			JSONObject object = new JSONObject();
			for (Map.Entry entry : ((Map<String, Object>) obj).entrySet()) {
				object.put(entry.getKey(), transJson(entry.getValue()));
			}
			return object;
		}
		if (obj instanceof List) {
			JSONArray array = new JSONArray();
			for (Object o : (List) obj) {
				array.add(transJson(o));
			}
			return array;
		}
		if(obj instanceof Pager){
			return JSONObject.fromObject(obj).toString();
		}
		return obj.toString();
	}
}
