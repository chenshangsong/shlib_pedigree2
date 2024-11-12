/**
 * 
 */
/**
 * @author think
 *
 */
package cn.sh.library.pedigree.webApi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.util.JsonUtil;
import cn.sh.library.pedigree.framework.util.PreloadApiFuriPlaceList;
import cn.sh.library.pedigree.services.AncestraltempleService;
import cn.sh.library.pedigree.services.OrganizationService;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi/common/")
public class ApiCommonController extends BaseController {
	@Resource
	private OrganizationService organizationService;
	
	@Resource
	private AncestraltempleService tanghaoService;//堂号 20220722
	@Autowired
	private RedisUtils redisUtil;
	/**
	 * 堂号列表20220722
	 * @param search
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tanghaoList", method = RequestMethod.GET)
	public Map<String, Object> tanghaoList(AncTempSearchBean search, Pager pager) {
		Map<String, Object> resultLast = new HashMap<>();
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit(redis_maxVistCount, redis_timeOut)) {
			resultLast.put("result", "-1");// 数据来源索引标记
			resultLast.put("code", "43003");// 数据来源索引标记
			resultLast.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return resultLast;
		}
		try {
			resultLast.put("pager", pager);
			List<Map<String, String>> list =null;
			if(StringUtilC.isEmpty(search.getIfDist())){
				list= this.tanghaoService.listForShiGuang(
						search, pager);
			}
			else {
				list = this.tanghaoService.list(
						search, pager);
			}
			resultLast.put(result, FWConstants.result_success);
			resultLast.put("datas", list);
		} catch (Exception e) {
			resultLast.put(result, FWConstants.result_syserror);
		}
		
		return resultLast;
	}
	
	/**
	 * 获取业务类型列表
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDataTypeList", method = RequestMethod.GET)
	public String getDataTypeList() {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("datas", DataTypeMap.getDataTypeList());
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 获取Graph列表
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUriTypeList", method = RequestMethod.GET)
	public String getUriTypeList() {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("datas", DataTypeMap.getGraphList());
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 获取角色列表
	 * 
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
	public String getRoleList() {
		jsonResult = new HashMap<>();
		try {
			jsonResult.put("datas", DataTypeMap.getRoleList());
			return (jsonResult).toString();
		} catch (Exception e) {
			return "error";
		}
	}
	/**
	 * 编码转换
	 * 
	 * @param str
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/str2Unicode", method = RequestMethod.POST)
	public String str2Unicode(@RequestBody String strJson) {
		jsonResult = new HashMap<>();
		String strTemp = String.valueOf(JSONObject.fromObject(strJson).get("str"));
		try {
			//jsonResult.put("data", StringUtilC.str2Unicode(strTemp));
			//return (jsonResult).toString();
			return StringUtilC.str2Unicode(strTemp);
		} catch (Exception e) {
			return "error";
		}
	}
	/**
	 * 编码转换
	 * 
	 * @param str
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/unicode2Str", method = RequestMethod.POST)
	public String unicode2Str(@RequestBody String strJson) {
		jsonResult = new HashMap<>();
		String strTemp = String.valueOf(JSONObject.fromObject(strJson).get("str"));
		try {
			//jsonResult.put("data", StringUtilC.unicode2Str(strTemp));
			//return (jsonResult).toString();
			return StringUtilC.unicode2Str(strTemp);
		} catch (Exception e) {
			return "error";
		}
	}
	/**
	 * 获取姓氏-地点分面列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFnameFacet", method = RequestMethod.GET)
	public String getFnameFacet(@Valid String furi) {
		jsonResult = new HashMap<>();
		try {
			List<Map<String, String>> list = PreloadApiFuriPlaceList.getInstance().getPlaceListbyFuri(furi);
			// List<Map<String, String>> list = null;

			if (list != null && list.size() > 0) {
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			jsonResult.put("data", list);
			return JsonUtil.getJsonString(jsonResult);
		} catch (Exception e) {
			jsonResult.put(result, FWConstants.result_syserror);
			return JsonUtil.getJsonString(jsonResult);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getOrgList", method = RequestMethod.GET)
	public Map<String, Object> getOrgList(WorkSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		result.put("pager", pager);
		List<Map<String, String>> list = this.organizationService.list(search.getOrganization(), pager);
		result.put("works", list);
		return result;
	}

	/**
	 * 编目系统使用
	 * 
	 * @param search
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrgListForBm", method = RequestMethod.GET)
	public Map<String, Object> getOrgListForBm(WorkSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		result.put("pager", pager);
		List<Map<String, String>> list = this.organizationService.listForBm(search.getOrganization(), pager);
		result.put("works", list);
		return result;
	}
}