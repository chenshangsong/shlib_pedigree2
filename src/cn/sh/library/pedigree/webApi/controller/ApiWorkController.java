package cn.sh.library.pedigree.webApi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.util.JsonUtil;
import cn.sh.library.pedigree.framework.util.PreloadApiFuriPlaceList;
import cn.sh.library.pedigree.framework.util.PreloadWorkJJUriList;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.fullContentLink.FullLink;
import cn.sh.library.pedigree.sysManager.model.ApiWorkFavoriteDto;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.utils.UserUtil;
import cn.sh.library.pedigree.utils.WebApiUtils;
import cn.sh.library.pedigree.webApi.dto.PlaceFacet;
import cn.sh.library.pedigree.webApi.dto.searchBean.ApiWorkSearchBean;
import cn.sh.library.pedigree.webApi.services.ApiWorkFavoriteService;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;
import cn.sh.library.pedigree.webApi.services.ApiWorkViewsCountService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi/work")
public class ApiWorkController extends BaseController {
	@Resource
	private ApiWorkService apiWorkService;
	@Resource
	private ApiWorkViewsCountService apiWorkViewsCountService;
	@Resource
	private ApiWorkFavoriteService apiWorkFavoriteService;

	@Autowired
	private RedisUtils redisUtil;
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(ApiWorkController.class);

	/**
	 * 分面接口
	 * 
	 * @param search
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkFacetList", method = RequestMethod.GET)
	public String getWorkFacetList(ApiWorkSearchBean search) {
		jsonResult = new HashMap<>();
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit("api_workGetWorkFacetList", redis_maxVistCount, redis_timeOut)) {
			jsonResult.put("result", "-1");// 数据来源索引标记
			jsonResult.put("code", "43003");// 数据来源索引标记
			jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return JSONArray.fromObject(jsonResult).toString();
		}
		String jsonString = "";
		// 如果空检，且缓存中已有数据，则读取缓存。
		if (StringUtilC.isFieldNull(search)) {
			if (!StringUtilC.isEmpty(CommonUtils.cache_FacetJsonString)) {
				return CommonUtils.cache_FacetJsonString;
			}
		}

		List<Object> _list = new ArrayList<>();
		try {
			String types[] = new String[] {};
			// 如果未传分面参数，则默认查询全部。
			if (StringUtilC.isEmpty(search.getFacetType())) {
				// types= new String[]{"1","2","3","4","5","6","7"}; chenss2018-04-24
				types = new String[] { "1", "2", "3", "4", "5", "6" };
			} else {
				types = search.getFacetType().split(",");
			}
			for (int i = 0; i < types.length; i++) {
				Map<String, Object> resulttemp = new HashMap<>();
				resulttemp.put("facetType", types[i]);
				search.setFacetType(types[i]);
				// if("7".equals(types[i])){search.setFacetType("1"); chenss2018-04-24
				if ("1".equals(types[i])) {
					resulttemp.put("facets", transPlaceNode(this.apiWorkService.getWorkFacetList(search)));
				} else {
					resulttemp.put("facets", this.apiWorkService.getWorkFacetList(search));
				}
				resulttemp.put("facetsOther", this.apiWorkService.getWorkFacetOthersList(search));
				_list.add(resulttemp);
			}
			// 分面数据获取完毕，分面属性恢复原值。
			search.setFacetType(null);
			jsonResult.put("data", _list);
			jsonString = JsonUtil.getJsonString(jsonResult);
			// 如果空检，且缓存中无数据，则存入缓存。
			if (StringUtilC.isFieldNull(search)) {
				if (StringUtilC.isEmpty(CommonUtils.cache_FacetJsonString)) {
					CommonUtils.cache_FacetJsonString = jsonString;
				}
			}
			return jsonString;
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	private List<PlaceFacet> transPlaceNode(List<Map<String, String>> facetPlaces) {
		List<PlaceFacet> result = new ArrayList<PlaceFacet>();
		Map<String, PlaceFacet> mapTmp = new HashMap<String, PlaceFacet>();
		Map<String, PlaceFacet> mapResult = new HashMap<String, PlaceFacet>();
		for (Map<String, String> object : facetPlaces) {
//			transFacet(placeFacet, result, mapTmp, placeFacet.getCount());
//			transFacet(object.get("placeUri"), result, mapTmp, mapResult, Integer.parseInt(object.get("count")));
//			transFacet(object.get("placeUri"), placeFacets, mapTmpFacets, Integer.parseInt(object.get("count")));
			PlaceFacet placeFacet;
			if (PreloadApiFuriPlaceList.getInstance().getRemotePlacesAll().containsKey(object.get("placeUri"))) {
				if (mapTmp.containsKey(object.get("placeUri"))) {
					placeFacet = mapTmp.get(object.get("placeUri"));
				} else {
					placeFacet = new PlaceFacet();
					placeFacet.setPlace(object.get("place"));
					placeFacet.setPlaceUri(object.get("placeUri"));
				}
				try {

					transFacet1(placeFacet, result, mapTmp, mapResult, Integer.parseInt(object.get("count")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Collections.sort(result, new Comparator<PlaceFacet>() {
			@Override
			public int compare(PlaceFacet o1, PlaceFacet o2) {
				return o2.getCount() - o1.getCount();
			}
		});
		return result;
	}

	private void transFacet(String placeUri, List<PlaceFacet> result, Map<String, PlaceFacet> mapTmp,
			Map<String, PlaceFacet> mapResult, int count) {
		Map<String, Object> placeAll = PreloadApiFuriPlaceList.getInstance().getRemotePlacesAll();
		PlaceFacet placeFacetThis;
		if (placeAll.containsKey(placeUri)) {
			String parenString = ((Map<String, String>) placeAll.get(placeUri)).get("parent");
			if (mapTmp.containsKey(placeUri)) {
				placeFacetThis = mapTmp.get(placeUri);
			} else {
				placeFacetThis = new PlaceFacet();
				placeFacetThis.setPlaceUri(((Map<String, String>) placeAll.get(placeUri)).get("uri"));
				placeFacetThis.setPlace(((Map<String, String>) placeAll.get(placeUri)).get("label"));
//				mapTmp.put(placeUri, placeFacetThis);
			}
			placeFacetThis.setCount(placeFacetThis.getCount() + count);

			// parent
			if (StringUtilC.isEmpty(parenString)) {
//				result.add(placeFacetThis);
				if (!mapResult.containsKey(parenString)) {
					result.add(placeFacetThis);
					mapResult.put(parenString, placeFacetThis);
					mapTmp.put(placeUri, placeFacetThis);
				}
			} else {
				PlaceFacet placeFacetParent;
				if (!placeAll.containsKey(parenString)) {
//					result.add(placeFacetThis);
				} else {
					if (!mapTmp.containsKey(parenString)) {
						placeFacetParent = new PlaceFacet();
						placeFacetParent.setPlaceUri(((Map<String, String>) placeAll.get(parenString)).get("uri"));
						placeFacetParent.setPlace(((Map<String, String>) placeAll.get(parenString)).get("label"));
					} else {
						placeFacetParent = mapTmp.get(parenString);
//						placeFacetParent.addSub(placeFacetThis);
					}
					if (!mapTmp.containsKey(placeUri)) {
						placeFacetParent.addSub(placeFacetThis);
						mapTmp.put(placeUri, placeFacetThis);
					}
					mapTmp.put(parenString, placeFacetParent);

					transFacet(parenString, result, mapTmp, mapResult, count);
				}
			}

		}
	}

	private static void transFacet1(PlaceFacet placeFacet, List<PlaceFacet> result, Map<String, PlaceFacet> mapTmp,
			Map<String, PlaceFacet> mapResult, int plus) {

		String uriString = placeFacet.getPlaceUri();
		placeFacet.setCount(placeFacet.getCount() + plus);
		Map<String, Object> placeAll = PreloadApiFuriPlaceList.getInstance().getRemotePlacesAll();
//		PlaceFacet placeFacetThis ;
		PlaceFacet placeFacetParent;
		String parentString = ((Map<String, String>) placeAll.get(uriString)).get("parent");
		if (StringUtilC.isEmpty(parentString)) {
//			if(!mapResult.containsKey(uriString)){
//				mapResult.put(uriString, placeFacet);
//				result.add(placeFacet);
//			}
			if (!mapTmp.containsKey(uriString)) {
				mapTmp.put(uriString, placeFacet);
				result.add(placeFacet);
			}
		} else {
			if (mapTmp.containsKey(parentString)) {
				placeFacetParent = mapTmp.get(parentString);
			} else {
				placeFacetParent = new PlaceFacet();
				placeFacetParent.setPlaceUri(((Map<String, String>) placeAll.get(parentString)).get("uri"));
				placeFacetParent.setPlace(((Map<String, String>) placeAll.get(parentString)).get("label"));
			}

			if (!mapTmp.containsKey(uriString)) {
				placeFacetParent.addSub(placeFacet);
			}
			mapTmp.put(uriString, placeFacet);
			transFacet1(placeFacetParent, result, mapTmp, mapResult, plus);
		}

	}

	private static void transFacet(PlaceFacet placeFacet, List<PlaceFacet> result, Map<String, PlaceFacet> mapTmp,
			int plus) {
		String uriString = placeFacet.getPlaceUri();
		Map<String, Object> placeAll = PreloadApiFuriPlaceList.getInstance().getRemotePlacesAll();
		if (placeAll.get(uriString) != null) {

			String parenString = ((Map<String, String>) placeAll.get(uriString)).get("parent");

			if (placeAll.containsKey(parenString)) {
				PlaceFacet parentFacet;
				if (mapTmp.containsKey(parenString)) {
					parentFacet = mapTmp.get(parenString);
				} else {
					parentFacet = new PlaceFacet();
					parentFacet.setPlaceUri(((Map<String, String>) placeAll.get(parenString)).get("uri"));
					parentFacet.setPlace(((Map<String, String>) placeAll.get(parenString)).get("label"));
				}
				if (!mapTmp.containsKey(uriString)) {
					mapTmp.put(uriString, placeFacet);
					parentFacet.addSub(placeFacet);
				}
				parentFacet.setCount(parentFacet.getCount() + plus);
				transFacet(parentFacet, result, mapTmp, plus);
			} else {
				if (!mapTmp.containsKey(uriString)) {
					result.add(placeFacet);
				}
				mapTmp.put(uriString, placeFacet);
			}
		}
	}

	/**
	 * 高级检索
	 * 
	 * @param search
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchALl(@Valid ApiWorkSearchBean search, @Valid Pager pager) {
		jsonResult = new HashMap<>();
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit("api_workSearch", redis_maxVistCount, redis_timeOut)) {
			jsonResult.put("result", "-1");// 数据来源索引标记
			jsonResult.put("code", "43003");// 数据来源索引标记
			jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return JSONArray.fromObject(jsonResult).toString();
		}
		Map<String, Object> result = new HashMap<>();
		try {
			if (search != null && search.getFamilyName() != null) {
				search.setFamilyName(search.getFamilyName().replace("氏", ""));
			}
			result.put("pager", pager);
			result.put("datas", this.apiWorkService.list(search, pager));
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 根据家谱uri获取work-instance-item信息 编目系统专用：chens 2020-06-20。
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getDetailByWorkUriForbm", method = RequestMethod.GET)
	public JSONObject getDetailByWorkUriForBM(@Valid String uri, Integer uid, HttpSession hs) throws Exception {
		jsonResult = new HashMap<>();
		Constant.virtuosoRetryConn = false;
		Map _mapTemp = null;
		try {
			String redisWorkKeyBm = RedisUtils.key_work_bm.concat(uri);
			if (redisUtil.exists(redisWorkKeyBm)) {// 如果redis缓存存在数据，则返回数据
				// 取
				Object obj = RedisUtils.unserizlize((byte[]) redisUtil.get(redisWorkKeyBm));
				_mapTemp = (Map) obj;
				jsonResult.put("indexResult", "true");// 数据来源索引标记
			} else {// 如果不存在，则先查询，再放入缓存。
				_mapTemp = apiWorkService.getDetailByWorkUri(uri);
				if (_mapTemp != null && _mapTemp.size() > 0) {

					// 结果繁体转简体
					_mapTemp = convertMapTosChs(_mapTemp); // 2024-04-07 chenss || 2024-08-31 chenss 放开简繁转换
					// V1直接向缓存中添加数据
					redisUtil.set(redisWorkKeyBm, RedisUtils.serialize(_mapTemp));
				}

			}
			jsonResult.put("data", _mapTemp);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put("msg", "error");
			StringUtilC.restartService(); // 重置链接 20240113
		}
		// 如果系统进行了重连，则进行服务重启,再次请求负载均衡到其他机器 chensss 20240113
		// 如果系统进行了重连，则进行服务重启,再次请求负载均衡到其他机器 chensss 20240113
		if (Constant.virtuosoRetryConn) {
			jsonResult.put("serverCtrl", StringUtilC.restartService());
		}
		jsonResult.put("server", RequestFilter.threadLocalRequest.get().getLocalAddr());
		return JSONObject.fromObject(jsonResult);
	}

	@ResponseBody
	@RequestMapping(value = "/getDetailByWorkUri", method = RequestMethod.GET)
	public JSONObject getDetailByWorkUri_redis(@Valid String uri, Integer uid, String referer,
			HttpServletRequest request) throws Exception {
		jsonResult = new HashMap<>();
		//提前处理Uri,如果有家谱编目系统进入，uri为18位，则按规则截取到16位
		String tempUri  = processUrl(uri);
		Constant.virtuosoRetryConn = false;
		try {
			// 1分钟30次访问限制
			if (!redisUtil.ifLimitVisit("api_workGetDetailByWorkUri", redis_maxVistCount, redis_timeOut)) {
				jsonResult.put("result", "-1");// 数据来源索引标记
				jsonResult.put("code", "43003");// 数据来源索引标记
				jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
				return JSONObject.fromObject(jsonResult);
			}
			if (uid == null) {
				uid = UserUtil.getUserId(request);
			}
			String redisWorkKey = RedisUtils.key_work.concat(tempUri);
			Map _mapTemp = null;

			if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
				// 取
				Object obj = RedisUtils.unserizlize((byte[]) redisUtil.get(redisWorkKey));
				_mapTemp = (Map) obj;
				jsonResult.put("indexResult", "true");// 数据来源索引标记
				FullLink.SetFullLinkHref(_mapTemp);// 从缓存中取的内容，需要重新设置全文链接

			} else {// 如果不存在，则先查询，再放入缓存。
				
				_mapTemp = apiWorkService.getDetailByWorkUri(tempUri);
				// 结果繁体转简体
//				_mapTemp = convertMapTosChs(_mapTemp); 2024-04-07 chenss
				if (_mapTemp != null && _mapTemp.size() > 0) {
					// V1直接向缓存中添加数据
					redisUtil.set(redisWorkKey, RedisUtils.serialize(_mapTemp));
					// V2 向消息中间件推送
					// String indexResult =
					// HttpUtil.postJson(CodeMsgUtil.getConfig("rabbitmq_provider_url").concat("/send/jiapu/redis/work"),null,_mapTemp);
					// jsonResult.put("indexResult", "create index:"+ indexResult);

				}

			}
			if (_mapTemp == null) {
				_mapTemp = new HashMap();
				_mapTemp.put("uri", tempUri);
				_mapTemp.put("msg", "无此数据。");
			}
			// 新增判断逻辑，如果不是编目系统进入，则9状态家谱禁止查看
			else { // 如果有数据，则继续处理后续业务
					// 权限处理
				if (_mapTemp.containsKey("accessLevelUC")) {
					String rdfaccessLevelUC = String.valueOf(_mapTemp.getOrDefault("accessLevelUC", ""));
					if ("9".equals(rdfaccessLevelUC) && uri.substring(uri.lastIndexOf('/')+1).length()!=18) {
						_mapTemp = new HashMap();
						_mapTemp.put("uri", uri);
						_mapTemp.put("msg", "无此数据或无权限查看。");
					}
				}
			}
			// 是否已收藏
			if (!StringUtilC.isEmpty(uid) && _mapTemp != null && _mapTemp.size() > 0) {
				ApiWorkFavoriteDto fdto = apiWorkFavoriteService.getApiWorkFavoriteByWorkUri(uid, tempUri);
				if (fdto != null && !StringUtilC.isEmpty(fdto.getId()) && _mapTemp != null) {
					_mapTemp.put("favoriteId", fdto.getId());
				}
			}
			if (_mapTemp != null && _mapTemp.size() > 0) {
				// 已查看数量
				_mapTemp.put("viewCount", apiWorkViewsCountService.getInfoByWorkUri(tempUri).getViewCount());
			}
			// 处理时光的相关数据
			StringUtilC.ProcessShiGuangMap(_mapTemp);
			jsonResult.put("data", _mapTemp);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put("msg", "error");
			jsonResult.put("msgType", "Exception");
//			StringUtilC.restartService(); 
		}
		// 如果系统进行了重连，则进行服务重启,再次请求负载均衡到其他机器 chensss 20250113
		if (Constant.virtuosoRetryConn) {
			jsonResult.put("serverCtrl", StringUtilC.restartService());
		}
		String ip = RequestFilter.threadLocalRequest.get().getLocalAddr();
		jsonResult.put("checkinfo", ip.substring(ip.lastIndexOf(".") + 1));
		return JSONObject.fromObject(jsonResult);
	}


	/**
	 * 无限流接口 chenss 20250225 家谱编目系统使用
	 * 
	 * @param uri
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getDetailByWorkUriNoLimit", method = RequestMethod.GET)
	public JSONObject getDetailByWorkUriNoLimit(@Valid String uri, HttpServletRequest request) throws Exception {
		jsonResult = new HashMap<>();
		Constant.virtuosoRetryConn = false;
		try {
			String redisWorkKey = RedisUtils.key_work.concat(uri);
			Map _mapTemp = null;

			if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
				// 取
				Object obj = RedisUtils.unserizlize((byte[]) redisUtil.get(redisWorkKey));
				_mapTemp = (Map) obj;
				jsonResult.put("indexResult", "true");// 数据来源索引标记
				FullLink.SetFullLinkHref(_mapTemp);// 从缓存中取的内容，需要重新设置全文链接

			} else {// 如果不存在，则先查询，再放入缓存。
				_mapTemp = apiWorkService.getDetailByWorkUri(uri);
				if (_mapTemp != null && _mapTemp.size() > 0) {
					// V1直接向缓存中添加数据
					redisUtil.set(redisWorkKey, RedisUtils.serialize(_mapTemp));

				}

			}
			// 处理时光的相关数据
			StringUtilC.ProcessShiGuangMap(_mapTemp);
			if (_mapTemp == null) {
				_mapTemp = new HashMap();
				_mapTemp.put("uri", uri);
				_mapTemp.put("msg", "无此数据。");
			}
			jsonResult.put("data", _mapTemp);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put("msg", "error");
			jsonResult.put("msgType", "Exception");
		}
		// 如果系统进行了重连，则进行服务重启,再次请求负载均衡到其他机器 chensss 20240113
		if (Constant.virtuosoRetryConn) {
			jsonResult.put("serverCtrl", StringUtilC.restartService());
		}
		String ip = RequestFilter.threadLocalRequest.get().getLocalAddr();
		jsonResult.put("checkinfo", ip.substring(ip.lastIndexOf(".") + 1));
		return JSONObject.fromObject(jsonResult);
	}

	/**
	 * 根据家谱uri获取work-instance-item信息chenss20191205
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getConvertImgByWorkUri", method = RequestMethod.GET)
	public String getConvertImgByWorkUri(@Valid String uri, HttpSession hs) throws Exception {
		String rediskeyWorkConvertImg = RedisUtils.key_work_convertImg.concat(uri);
		String convertImg = null;
		// 新增convertImage chenss 20240628
		if (redisUtil.exists(rediskeyWorkConvertImg)) {// 如果redis缓存存在数据，则返回数据
			Object obj = redisUtil.get(rediskeyWorkConvertImg);
			convertImg = StringUtilC.getString(obj);
			;
		} else {// 如果不存在，则先查询，再放入缓存。
			convertImg = PreloadWorkJJUriList.getInstance().getConvertImgByUri(uri);
			if (!StringUtilC.isEmpty(convertImg)) {
				redisUtil.set(rediskeyWorkConvertImg, convertImg);
			}
		}
		return convertImg;
	}

	/**
	 * 删除redis索引
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delRedis", method = RequestMethod.GET)
	public String delRedis(@Valid String uri) {
		Map<String, Object> jsonResult = new HashMap<>();
		Constant.virtuosoRetryConn = false;
		try {
			String redisWorkKey = RedisUtils.key_work.concat(uri);
			String redisWorkKeyBm = RedisUtils.key_work_bm.concat(uri);
			jsonResult.put("uri", uri);
			jsonResult.put("result", "0");
			// 定义 Redis 键和对应的 JSON 消息键
			Map<String, String> redisKeys = new HashMap<>();
			redisKeys.put(RedisUtils.key_work.concat(uri), "msg_work");
			redisKeys.put(RedisUtils.key_work_view.concat(uri), "msg_work_view");
			redisKeys.put(RedisUtils.key_work_convertImg.concat(uri), "msg_work_ConverImg");
			redisKeys.put(RedisUtils.key_work_bm.concat(uri), "msg_work_bm");
			// 删除 Redis 索引
			for (Map.Entry<String, String> entry : redisKeys.entrySet()) {
				String redisKey = entry.getKey();
				String jsonKey = entry.getValue();
				if (redisUtil.exists(redisKey)) {
					redisUtil.remove(redisKey);
					jsonResult.put(jsonKey, "Redis索引已删除");
				} else {
					jsonResult.put(jsonKey, "Redis索引未找到");
				}
			}

			// 添加索引
			try {
				Map<String, Object> _mapTemp = apiWorkService.getDetailByWorkUri(uri);
				if (_mapTemp != null && !_mapTemp.isEmpty()) {
					redisUtil.set(redisWorkKey, RedisUtils.serialize(_mapTemp));
					jsonResult.put("msg_work_AddFlag", "0");
					jsonResult.put("msg_work_AddMessage", "展示详情已重新添加索引");
					Map<String, Object> _mapTempBm = convertMapTosChs(_mapTemp);
					redisUtil.set(redisWorkKeyBm, RedisUtils.serialize(_mapTempBm));
					jsonResult.put("msg_work_bm_AddFlag", "0");
					jsonResult.put("msg_work_bm_AddMessage", "编目详情已重新添加索引");
				} else {
					jsonResult.put("msg_work_AddFlag", "1");
					jsonResult.put("msg_work_bm_AddFlag", "1");
					jsonResult.put("msg_redisAdd", "索引添加失败，未找到元数据。");
				}
			} catch (Exception e) {
				jsonResult.put("msg_work_AddFlag", "-1");
				jsonResult.put("msg_work_bmAddFlag", "-1");
				jsonResult.put("result", "1");
				jsonResult.put("msg_redisAdd", "索引添加失败，元数据获取异常。");
				return StringUtil.getString(JSONObject.fromObject(jsonResult));
			}
			// 如果系统进行了重连，则进行服务重启,再次请求负载均衡到其他机器 chensss 20240113
			if (Constant.virtuosoRetryConn) {
				jsonResult.put("serverCtrl", StringUtilC.restartService());
			}
			jsonResult.put("server", RequestFilter.threadLocalRequest.get().getLocalAddr());
			return StringUtil.getString(JSONObject.fromObject(jsonResult));
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put("msg", "Redis删除异常");
			jsonResult.put("result", "1");
			return StringUtil.getString(JSONObject.fromObject(jsonResult));
		}
	}

	/**
	 * 删除 pdf全文 索引
	 * 
	 * @param _map
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delDoiRedisList", method = RequestMethod.POST)
	public String delDoiRedisList(@RequestBody Map _map) throws Exception {
		jsonResult = new HashMap<>();
		List<String> strList = new ArrayList<>();
		try {
			((List<String>) _map.get("data")).stream().forEach(item -> {
				String redisWorkKey = RedisUtils.key_fulltextIn_pdf.concat(item);
				if (redisUtil.exists(redisWorkKey)) {
					redisUtil.remove(redisWorkKey);// 删除 pdf全文 详情索引
					strList.add(item);
				}

			});

			jsonResult.put("msg", "pdf_doi_Redis索引已删除: " + strList);
			jsonResult.put("result", "0");

		} catch (Exception e) {
			jsonResult.put("result", "1");
			jsonResult.put("data", "索引添加失败，出现异常");
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
		return StringUtil.getString(JSONObject.fromObject(jsonResult));
	}

	/**
	 * 添加redis索引
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addWorkRedis", method = RequestMethod.POST)
	public String addWorkRedis(@RequestBody Map workMap) throws Exception {
		jsonResult = new HashMap<>();
		String uri = StringUtilC.getString(workMap.get("work"));
		String redisWorkKey = RedisUtils.key_work.concat(uri);
		jsonResult.put("uri", uri);
		try {
			// 存 redis 字节
			if (!redisUtil.exists(redisWorkKey)) {// 如果redis不存在
				redisUtil.set(redisWorkKey, RedisUtils.serialize(workMap));
				jsonResult.put("data", "索引添加成功");
				jsonResult.put("result", "0");
			} else {
				jsonResult.put("data", "索引添加失败，索引已存在");
				jsonResult.put("result", "1");
			}

		} catch (Exception e) {
			jsonResult.put("result", "1");
			jsonResult.put("data", "索引添加失败，出现异常");
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
		return StringUtil.getString(JSONObject.fromObject(jsonResult));
	}

	/**
	 * 根据WorkUri获取封面数据
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getImageFrontPathByWorkUri", method = RequestMethod.GET)
	public String getImageFrontPathByWorkUri(@Valid String workUri) throws Exception {
		jsonResult = new HashMap<>();
		try {
			Map _doiMap = apiWorkService.getDoiByWorkUri(workUri);
			String dois = "";
			String filePath = "";
			if (_doiMap != null && _doiMap.size() > 0) {
				if (!StringUtilC.isEmpty(_doiMap.get("doi"))) {
					dois = StringUtilC.getString(_doiMap.get("doi"));
					filePath = WebApiUtils.GetImagePathByDoi(dois);
					Map<String, Object> thisResult = new HashMap<>();
					thisResult.put("workUri", workUri);
					thisResult.put("imageFrontPath", filePath);
					jsonResult.put("data", thisResult);
				}
			}
			if (!StringUtilC.isEmpty(filePath)) {
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			return JsonUtil.getJsonString(jsonResult);
		} catch (Exception e) {
			jsonResult.put(result, FWConstants.result_syserror);
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			return JsonUtil.getJsonString(jsonResult);
		}
	}

	/**
	 * 获取联想词列表
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCandList", method = RequestMethod.GET)
	public String getCandList(@Valid String freeText, Integer maxCount) throws Exception {
		jsonResult = new HashMap<>();
		if (StringUtilC.isEmpty(maxCount)) {
			maxCount = 10;
		} else {
			if (maxCount > 200) {
				maxCount = 200;
			}
		}
		try {
			List<String> _listLast = new ArrayList<String>();
			List<Map<String, String>> _list = apiWorkService.getFreeResultList(freeText, maxCount);
			if (_list != null && _list.size() > 0) {
				for (Map<String, String> map : _list) {
					_listLast.add(StringUtilC.getString(map.get("x")));
				}
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			jsonResult.put("datas", _listLast);
			return JsonUtil.getJsonString(jsonResult);
		} catch (Exception e) {
			jsonResult.put(result, FWConstants.result_syserror);
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			return JsonUtil.getJsonString(jsonResult);
		}
	}

	/**
	 * 获取朝代列表
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTemporalOfWorkList", method = RequestMethod.GET)
	public String getTemporalOfWorkList() {
		jsonResult = new HashMap<>();
		String[] _cdList = new String[] { "宋", "元", "明", "清", "民国", "当代" };
		jsonResult.put("datas", Arrays.asList(_cdList));
		jsonResult.put(result, FWConstants.result_success);
		return JsonUtil.getJsonString(jsonResult);
	}

	/**
	 * 结果繁体转简体
	 * 
	 * @param _map
	 * @return
	 */
	private Map convertMapTosChs(Map _map) {
		if (_map == null) {
			return null;
		}
		String[] urils = new String[] { "http://data.library.sh.cn/authority/familyname/e6yje5q4psiycska"// 于
		};
		// 检查逻辑 2024-10-09 chenss 特殊姓氏，不进行简繁转换。
		List<Map<String, String>> fnameList = (List<Map<String, String>>) _map.get("fnameList");
		if (fnameList != null && !fnameList.isEmpty()) {
			for (Map<String, String> tempItem : fnameList) {
				String fnameUri = tempItem.get("fnameUri");
				if (Arrays.asList(urils).contains(fnameUri)) {
					return _map;
				}
			}
		}

		Set<Entry<String, Object>> entrySet = _map.entrySet();
		for (Entry<String, Object> e : entrySet) {
			String strKey = e.getKey();
			Object strValue = e.getValue();
			if (strValue instanceof String) {
				// System.out.println(strKey+":"+strValue);
				_map.put(strKey, StringUtilC.getChs(StringUtilC.getString(strValue)));
			}
			if (strValue instanceof Map) {
				_map.put(strKey, convertMapTosChs((Map) strValue));
			}
			if (strValue instanceof List) {
				List<Map<String, Object>> tempList = new ArrayList();
				for (Map<String, Object> listKey : (List<Map<String, Object>>) strValue) {
					tempList.add(convertMapTosChs(listKey));
				}
				_map.put(strKey, tempList);
			}
		}

		return _map;
	}

	/**
	 * 根据家谱uri获取work-instance-item信息 编目系统专用：chens 2020-06-20。
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getQxStatistics", method = RequestMethod.GET)
	public JSONObject getQxStatistics(@Valid String fname, HttpSession hs) throws Exception {
		jsonResult = new HashMap<>();
		try {
			Map _mapTemp = apiWorkService.getQxTjInfo(fname);

			jsonResult.put("data", _mapTemp);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put("msg", "error");
		}
		return JSONObject.fromObject(jsonResult);
	}

	/**
	 * 根据家谱uri获取work-instance-item信息 编目系统专用：chens 2020-06-20。
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlaceStatistics", method = RequestMethod.GET)
	public JSONObject getPlaceStatistics(@Valid String fname, HttpSession hs) throws Exception {
		jsonResult = new HashMap<>();
		try {
			Map _mapTemp = apiWorkService.getPlceTjInfo(fname);

			jsonResult.put("data", _mapTemp);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put("msg", "error");
		}
		return JSONObject.fromObject(jsonResult);
	}
	
    /**
     * 处理目标URL，按规则截取并调整最后一段路径
     * @param url 输入的原始URL（如：http://data.library.sh.cn/jp/resource/work/fntpzzidar5xqdib）
     * @return 处理后的完整URL
     */
    private static String processUrl(String url) {
        // 定义固定前缀
        final String PREFIX = "http://data.library.sh.cn/jp/resource/work/";
        
        // 1. 截取最后一个斜杠后的内容
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            throw new IllegalArgumentException("输入的URL格式不正确，缺少斜杠分隔符");
        }
        String lastSegment = url.substring(lastSlashIndex + 1);
        
        // 2. 长度判断与调整
        if (lastSegment.length() > 16) {
            // 确保字符串长度足够操作（至少12位才能删除11-12位）
            if (lastSegment.length() < 12) {
                throw new IllegalArgumentException("最后一段路径长度不足，无法执行删除操作");
            }
            // 删除第11、12位字符（Java字符串索引从0开始，对应索引10和11）
            lastSegment = lastSegment.substring(0, 10) + lastSegment.substring(12);
        }
        
        // 3. 拼接结果并返回
        return PREFIX + lastSegment;
    }
}
