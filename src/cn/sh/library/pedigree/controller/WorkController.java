package cn.sh.library.pedigree.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.services.DynastyService;
import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 谱籍表
 * 
 * @author liuyi
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/service/work")
public class WorkController extends BaseController {

	@Resource
	private WorkService workService;

	@Resource
	private PlaceService placeService;

	@Resource
	private DynastyService dynastyService;

	@Autowired
	private RedisUtils redisUtil;
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(WorkController.class);

	/*
	 * @RequestMapping(value = "/list", method = RequestMethod.GET) public String
	 * list(WorkSearchBean search) { return "work/list"; }
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView adv(WorkSearchBean search) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("work/list");
		modelAndView.addObject("data", search);
		return modelAndView;
	}

	/**
	 * 先祖名人点击跳转页面，后来新增
	 * 
	 * @return
	 */
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ModelAndView persons(String uri) {
		modelAndView.addObject("uri", uri);
		modelAndView.setViewName("work/persons");
		return modelAndView;
	}

	/**
	 * 谱系图页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/dendrogram", method = RequestMethod.GET)
	public ModelAndView dendrogram(String uri) {
		modelAndView.addObject("uri", uri);
		modelAndView.setViewName("work/dendrogram");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<String, Object> loadList(WorkSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();

		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit(30, 1)) {
			result.put("result", "-1");// 数据来源索引标记
			result.put("code", "43003");// 数据来源索引标记
			result.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return result;
		}
		try {
			if (!StringUtilC.isEmpty(search.getFamilyName())) {
				search.setFamilyName(search.getFamilyName().replace("氏", ""));
				if ("all".equals(search.getFamilyName())) {
					search.setFamilyName("");
				}
			}
			result.put("pager", pager);
			if (StringUtils.isNotBlank(search.getPlace_uri())) {
				Map<String, String> standPlace = this.placeService.getStandPlace(search.getPlace_uri());
				result.put("standPlace", standPlace);
				List<Map<String, String>> works = this.placeService.getPlaceWorks(standPlace, search.getFamilyName());
				result.put("works", works);
			} else {
				result.put("works", this.workService.list(search, pager));
			}
		} catch (Exception e) {
			logger.info("WorkController-list高级检索错误：" + DateUtilC.getNowDateTime() + "----" + e);
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/countItemByWork", method = RequestMethod.POST)
	public Map<String, Object> countItemByWork(WorkSearchBean search) {
		Map<String, Object> result = new HashMap<>();
		try {
			if (!StringUtilC.isEmpty(search.getFamilyName())) {
				search.setFamilyName(search.getFamilyName().replace("氏", ""));
				if ("all".equals(search.getFamilyName())) {
					search.setFamilyName("");
				}
			}
			result.put("count", this.workService.countItemsByWork(search));
		} catch (Exception e) {
			logger.info("WorkController-list高级检索错误：" + DateUtilC.getNowDateTime() + "----" + e);
		}

		return result;
	}

	/**
	 * 全文检索
	 * 
	 * @param keyword
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, Object> list(String keyword, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit(30, 1)) {
			result.put("result", "-1");// 数据来源索引标记
			result.put("code", "43003");// 数据来源索引标记
			result.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			return result;
		}
		try {
			List<Map<String, String>> mapList = workService.list(keyword, pager);
			result.put("pager", pager);
			result.put("works", mapList);
		} catch (Exception e) {
			logger.info("WorkController-list简单检索错误" + DateUtilC.getNowDateTime() + "----" + e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Work getWork(String uri) {
		Work _work = new Work();
		Constant.virtuosoRetryConn=false;
		// 1分钟30次访问限制
		if (!redisUtil.ifLimitVisit(30, 1)) {
			jsonResult = new HashMap<>();
			jsonResult.put("result", "-1");// 数据来源索引标记
			jsonResult.put("code", "43003");// 数据来源索引标记
			jsonResult.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
			_work.setResultVist(jsonResult);
			_work.setDtitle( "对不起，您访问过于频繁，请稍后再试。");
			return _work;
		}
		try {
			_work = this.workService.getWork(uri, true);
			//如果系统进行了重连，则进行服务重启,再次请求负载均衡到其他机器 chensss 20240113
			if( Constant.virtuosoRetryConn) {
				StringUtilC.restartService();
			}
			return _work;
		} catch (Exception e) {
			logger.info("WorkController-get错误" + DateUtilC.getNowDateTime() + "----" + e);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/getInGeo", method = RequestMethod.GET)
	public List<Map<String, String>> works(String beginY, String endY, String uri, int unit, String name) {
		try {
			if (StringUtils.isNotBlank(uri)) {
				return workService.getWorksInTimeline(StringUtilC.StringFilter(beginY), StringUtilC.StringFilter(endY),
						StringUtilC.getInt(StringUtilC.StringFilter(unit)), StringUtilC.StringFilter(name));
			}
		} catch (Exception e) {
			logger.info("WorkController-getInGeo错误" + DateUtilC.getNowDateTime() + "----" + e);
		}

		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/getTimes", method = RequestMethod.GET)
	public List<Map<String, String>> times() {
		return dynastyService.listTemps4TL();
	}
}
