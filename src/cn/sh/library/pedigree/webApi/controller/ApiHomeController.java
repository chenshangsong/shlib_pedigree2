package cn.sh.library.pedigree.webApi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.sysManager.mapper.FamilyNameMapper;
import cn.sh.library.pedigree.sysManager.mapper.HomeMapper;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.HttpsUtil;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiPersonService;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi")
public class ApiHomeController extends BaseController {

	@Autowired
	private HomeMapper homeMapper;
	@Resource
	private ApiPersonService ApipersonService2;
	@Resource
	private ApiWorkService apiworkService;
	@Autowired
	private RedisUtils redisUtil;

	@Autowired
	private FamilyNameMapper familyNameMapper;

	/**
	 * 姓氏的首字母
	 */
	private String chars = "ABCDEFGHJKLMNOPQRSTWXYZ";

	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(ApiHomeController.class);

	private static SurnameModel fnameInfo;
	private static List<Person> personList;

	/**
	 * 统计 chenss 2023-12-14
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tjes", method = { RequestMethod.GET })
	public String tjes() throws Exception {
		try {
			String apiResult = HttpsUtil.getJson("https://dhapi.library.sh.cn/es/jp/webapi/work/getWorkFacetList",
					null);

			JSONObject _fnameMap = JSONObject.fromObject(apiResult).getJSONArray("data").getJSONObject(6);
			JSONArray listF = _fnameMap.getJSONArray("facets");
			listF.stream().forEach(item -> {
				String _workCount = StringUtilC.getString(JSONObject.fromObject(item).get("count"));
				String fname = StringUtilC.getString(JSONObject.fromObject(item).get("fname"));
				SurnameModel model = new SurnameModel();
				model.setGenealogyCnt(_workCount);
				model.setFamilyNameS(fname);
				familyNameMapper.updateSurname(model);

			});
			// 删除redis缓存
			char c;
			for (c = 'A'; c <= 'Z'; ++c) {
				String redisWorkKey = RedisUtils.key_family.concat(String.valueOf(c));
				if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
					redisUtil.remove(redisWorkKey);
				}
			}
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 首页入口
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/homePageInit", method = RequestMethod.GET)
	public String homePageInit(Model model, HttpServletRequest req) throws Exception {
		Map<String, Object> data = new HashMap<>();
		jsonResult = new HashMap<>();
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit("api_homePageInit",redis_maxVistCount, redis_timeOut)) {
			jsonResult.put("result", "-1");// 数据来源索引标记
			jsonResult.put("code", "43003");// 数据来源索引标记
			jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return JSONArray.fromObject(jsonResult).toString();
		}
		try {
			String initial = String.valueOf(chars.charAt((int) (Math.random() * 23)));
			// 获取所有姓氏数据
			List<SurnameModel> listSurname = homeMapper.getSurnameList(initial);
			if (initial.equals("A")) {
				listSurname.addAll(listSurname.size(), homeMapper.getSurnameList("B"));
			} else if (initial.equals("E")) {
				listSurname.addAll(listSurname.size(), homeMapper.getSurnameList("F"));
			} else if (initial.equals("O")) {
				listSurname.addAll(listSurname.size(), homeMapper.getSurnameList("P"));
			} else if (initial.equals("R")) {
				listSurname.addAll(listSurname.size(), homeMapper.getSurnameList("S"));
			}
			data.put("familyNameList", listSurname);
			// 随机获取所有姓氏中1个
			int listSize = listSurname.size();
			int rdoSurname = 0;
			if (listSize > 1) {
				rdoSurname = this.getRandomNumber(listSize - 1);
			}
			SurnameModel currentSurname = listSurname.get(rdoSurname);
			data.put("currentFamilyName", currentSurname);
			List<Person> listPerson = this.getPersons(currentSurname);
			data.put("personList", listPerson);
			List<Work> listWork = new ArrayList<Work>();
			Person currentPerson = new Person();
			for (Person p : listPerson) {
				if (!StringUtilC.isEmpty(p.getRelatedWork())) {
					listWork = this.gerWorks(p.getRelatedWork(), p.getUri());
					currentPerson = p;
					break;
				}
			}
			data.put("currentPerson", currentPerson);
			data.put("workList", listWork);
			Work currentWork = new Work();
			if (listWork != null && listWork.size() > 0) {
				currentWork = listWork.get(0);
			}
			data.put("currentWork", currentWork);
		} catch (Exception e) {
			logger.info("HomeController-index错误：" + DateUtilC.getNowDateTime() + "----" + e);
		}

		jsonResult.put("data", data);
		return StringUtilC.getString(JSONArray.fromObject(jsonResult));
	}

	/**
	 * 姓氏浏览-列表:根据姓氏首字母
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getfamilynamelistByChart/{fchart}", method = RequestMethod.GET)
	public String getfamilynamelistByChart(@PathVariable("fchart") String fchart, HttpServletRequest req)
			throws Exception {
		jsonResult = new HashMap<>();
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit("api_getfamilynamelistByChart",redis_maxVistCount, redis_timeOut)) {
			jsonResult.put("result", "-1");// 数据来源索引标记
			jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return JSONArray.fromObject(jsonResult).toString();
		}
		try {
			if (!StringUtilC.isEmpty(fchart)) {
				// 获取所有姓氏数据
				List<SurnameModel> listSurname = null;
				String redisWorkKey = RedisUtils.key_family.concat(fchart.toUpperCase());
				if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
					// 取
					Object obj = RedisUtils.unserizlize((byte[]) redisUtil.get(redisWorkKey));
					listSurname = (List) obj;

				} else {// 如果不存在，则先查询，再放入缓存。
					listSurname = homeMapper.getSurnameList(fchart.toUpperCase());

					if (listSurname != null && listSurname.size() > 0) {
						// 存 字节
						redisUtil.set(redisWorkKey, RedisUtils.serialize(listSurname));

					}

				}
				jsonResult.put("data", listSurname);
				return JSONArray.fromObject(jsonResult).toString();
			}
		} catch (Exception e) {
			return "error";
		}
		return "error";
	}

	/**
	 * 根据姓氏查询详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getfamilyNameInfoByFname/{fname}", method = RequestMethod.GET)
	public String getfamilyNameInfoByFname(@PathVariable("fname") String fname) throws Exception {
		try {
			jsonResult = new HashMap<>();
			// 1分钟30次访问限制
			if (!redisUtil.ifLimitVisit("api_ggetfamilyNameInfoByFname",redis_maxVistCount, redis_timeOut)) {
				jsonResult.put("result", "-1");// 数据来源索引标记
				jsonResult.put("code", "43003");// 数据来源索引标记
				jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
				return JSONArray.fromObject(jsonResult).toString();
			}
			// 获取单个姓氏数据
			fnameInfo = homeMapper.getSurname(fname);
			if (fnameInfo != null && fnameInfo.getDescription() != null) {// 转为简体、半角转全角
				String desChs = StringUtilC.getChs(fnameInfo.getDescription()).replaceAll(",", "，");
				desChs = desChs.replaceAll("\"", "“");
				desChs = desChs.replaceAll(";", "；");
				desChs = desChs.replaceAll("\\.", "。");
				fnameInfo.setDescription(desChs);
			}
			jsonResult.put("data", fnameInfo);
			return JSONObject.fromObject(jsonResult).toString();
		} catch (Exception e) {
			return "error";
		}

	}

	/**
	 * 根据姓氏查询详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPersonListByFname/{fname}", method = RequestMethod.GET)
	public String getPersonListByFname(@PathVariable("fname") String fname) throws Exception {
		try {
			jsonResult = new HashMap<>();
			// 1分钟30次访问限制
			if (!redisUtil.ifLimitVisit("api_getPersonListByFname",redis_maxVistCount, redis_timeOut)) {
				jsonResult.put("result", "-1");// 数据来源索引标记
				jsonResult.put("code", "43003");// 数据来源索引标记
				jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
				return JSONArray.fromObject(jsonResult).toString();
			}
			if (fnameInfo != null) {
				// 获取单个姓氏数据
				fnameInfo = homeMapper.getSurname(fname);
			}
			// 获取历史名人
			personList = this.getPersons(fnameInfo);
			jsonResult.put("data", personList);
			return JSONObject.fromObject(jsonResult).toString();
		} catch (Exception e) {
			return "error";
		}

	}

	/**
	 * 根据人员URI，想查询workList信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkListByPersonAndWorkUri", method = RequestMethod.GET)
	public String getWorkListByPersonAndWorkUri(@Valid String puri, @Valid String rWorkUri) throws Exception {
		try {
			jsonResult = new HashMap<>();
			// 1分钟30次访问限制
			if (!redisUtil.ifLimitVisit("api_getWorkListByPersonAndWorkUri",redis_maxVistCount, redis_timeOut)) {
				jsonResult.put("result", "-1");// 数据来源索引标记
				jsonResult.put("code", "43003");// 数据来源索引标记
				jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
				return JSONArray.fromObject(jsonResult).toString();
			}
			List<Work> listWork = this.gerWorks(rWorkUri, puri);
			jsonResult.put("data", listWork);
			return JSONObject.fromObject(jsonResult).toString();
		} catch (Exception e) {
			return "error";
		}

	}

	/**
	 * 根据姓氏查询名人
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPersonsByFname/{fname}", method = RequestMethod.GET)
	public String getPersonsByFname(@PathVariable("fname") String fname) throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			jsonResult = new HashMap<>();
			// 1分钟30次访问限制
			if (!redisUtil.ifLimitVisit("api_getPersonsByFname",redis_maxVistCount, redis_timeOut)) {
				jsonResult.put("result", "-1");// 数据来源索引标记
				jsonResult.put("code", "43003");// 数据来源索引标记
				jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
				return JSONArray.fromObject(jsonResult).toString();
			}
			// 获取单个姓氏数据
			SurnameModel currentSurname = homeMapper.getSurname(fname);
			result.put("currentSurname", currentSurname);
			// 获取历史名人
			List<Person> listPerson = this.getPersons(currentSurname);
			result.put("personList", listPerson);
			Person currentPerson = new Person();
			for (Person p : listPerson) {
				if (!StringUtilC.isEmpty(p.getRelatedWork())) {
					currentPerson = p;
					break;
				}
			}
			result.put("currentPerson", currentPerson);
	
			jsonResult.put("data", result);
		} catch (Exception e) {
			logger.info("HomeController-doSurname错误：" + DateUtilC.getNowDateTime() + "----" + e);
		}
		return JSONArray.fromObject(jsonResult).toString();
	}

	/**
	 * 获取历史名人
	 * 
	 * @param surname
	 * @return
	 */
	private List<Person> getPersons(SurnameModel surname) {
		List<Person> listPerson = new ArrayList<Person>();
		// 随机抽取5个名人
		if (!StringUtilC.isEmpty(surname.getCelebrityCnt()) && !"0".equals(surname.getCelebrityCnt())) {
			Pager pager = new Pager();
			pager.setPageSize(5);
			pager.calcPageCount(Long.parseLong(surname.getCelebrityCnt()));
			List<Integer> pageths = pager.getPageths();
			int pageth = this.getRandomNumber(pageths.get(pageths.size() - 1));
			pager.setPageth(pageth);
			listPerson = this.ApipersonService2.getPersonsInHome(surname.getUri(), pager);
		}
		// 混乱排序
		Collections.shuffle(listPerson);
		return listPerson;
	}

	/**
	 * 获取名人家谱
	 * 
	 * @param listPerson
	 */
	private List<Work> gerWorks(String relatedWork, String pUris) {

		List<Work> listWork = new ArrayList<Work>();

		// relatedWorkUri列表
		String[] urls = relatedWork.split(",");
		// 人名URI列表
		String[] puri = pUris.split(",");
		for (int i = 0; i < urls.length; i++) {
			Work w = new Work();
			w = apiworkService.getWork(urls[i], false);
			// 如果正题名为空，则取副题名
			if (w != null && StringUtilC.isEmpty(w.getTitle())) {
				w.setTitle(w.getSubTitle());
			}
			// 先祖名人URI
			w.setPuri(puri[i]);
			listWork.add(w);
		}
		if (listWork.size() <= 5) {
			return listWork;
		} else {
			List<Work> lastlistWork = new ArrayList<Work>();
			// 混乱排序，随机抽5.
			Collections.shuffle(listWork);
			for (int i = 0; i < 5; i++) {
				lastlistWork.add(listWork.get(i));
			}
			return lastlistWork;
		}

	}

	/**
	 * 获得随机数
	 * 
	 * @param n
	 * @return
	 */
	private int getRandomNumber(int n) {
		try {
			Random random = new Random();
			int radNum = 0;
			while (radNum == 0) {
				radNum = random.nextInt(n) + 1;
			}
			return radNum;
		} catch (Exception e) {
			System.out.println("随机数生成错误:" + e);
		}
		return 0;
	}
}
