package cn.sh.library.pedigree.controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;

/**
 * @author liuyi
 * @date 2015/3/1 0001
 */
@Controller
@RequestMapping("/service/search")
public class SearchController {

	@Resource
	private WorkService workService;

	@Autowired
	private RedisUtils redisUtil;
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(SearchController.class);
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "search/list";
	}

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
			result.put("datas", mapList);
		} catch (Exception e) {
			logger.info("SearchController-list错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return result;
	}

	@RequestMapping(value = "/adv", method = RequestMethod.GET)
	public ModelAndView adv(WorkSearchBean search) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("search/adv_search");
		modelAndView.addObject("data", search);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/adv", method = RequestMethod.POST)
	public Map<String, Object> adv(WorkSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		// 1分钟30次访问限制
				if (!redisUtil.ifLimitVisit(30, 1)) {
					result.put("result", "-1");// 数据来源索引标记
					result.put("code", "43003");// 数据来源索引标记
					result.put("msg", "对不起，您访问过于频繁，请稍后再试。");// 数据来源索引标记
					return result;
				}
		try {
			search.setFamilyName(search.getFamilyName().replace("氏", ""));
			result.put("pager", pager);
			result.put("datas", this.workService.list(search, pager));
		} catch (Exception e) {
			logger.info("SearchController-adv错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/homeAdv", method = RequestMethod.POST)
	public ModelAndView homeAdv(WorkSearchBean search) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		if (this.isFieldNull(search)) {
			modelAndView.setViewName("service/index");
		} else {
			//翻译转换
			search=(WorkSearchBean)StringUtilC.convertModel(search);
			modelAndView.setViewName("search/adv_search");
			modelAndView.addObject("data", search);
		}
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/serviceAdv", method = RequestMethod.POST)
	public ModelAndView serviceAdv(WorkSearchBean search) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("search/adv_search");
		modelAndView.addObject("data", search);
		return modelAndView;
	}

	private boolean isFieldNull(WorkSearchBean search)
			throws IllegalArgumentException, IllegalAccessException {
		boolean ret = true;
		Class<? extends WorkSearchBean> clz = search.getClass();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (!StringUtilC.isEmpty(field.get(search))){
				ret = false;
				break;
			}
		}

		return ret;
	}
	
}
