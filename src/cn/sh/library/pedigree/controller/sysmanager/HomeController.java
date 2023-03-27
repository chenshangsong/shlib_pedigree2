package cn.sh.library.pedigree.controller.sysmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sysManager.mapper.HomeMapper;
import cn.sh.library.pedigree.sysManager.model.HomeSearchModel;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

	@Autowired
	private HomeMapper homeMapper;
	@Resource
	private PersonService personService;
	@Resource
	private WorkService workService;
	
	/**
	 * 姓氏的首字母
	 */
	private String chars = "ABCDEFGHJKLMNOPQRSTWXYZ";

	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(HomeController.class);

	/**
	 * 首页入口
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest req) throws Exception {
		try {
			/*doi预加载获取	@author 陈铭
			List<DoiSysModel> doiList = PreloadDoiList.getInstance().getDoiList();
			if(doiList==null){
				req.getSession().setAttribute("doiList", doiList);
			}*/
			
			// logger.info("关系库请求开始："+req.getSession().getId()+"----"+DateUtilC.getNowDateTime());
			// return data
			Map<String, Object> data = new HashMap<>();

			String initial = String
					.valueOf(chars.charAt((int) (Math.random() * 23)));
			// 获取所有姓氏数据
			List<SurnameModel> listSurname = homeMapper.getSurnameList(initial);
			if (initial.equals("A")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("B"));
			} else if (initial.equals("E")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("F"));
			} else if (initial.equals("O")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("P"));
			} else if (initial.equals("R")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("S"));
			}
			data.put("surname", listSurname);

			// logger.info("关系库请求结束："+req.getSession().getId()+"----"+DateUtilC.getNowDateTime());
			// 随机获取所有姓氏中1个
			int listSize = listSurname.size();
			int rdoSurname = 0;
			if (listSize > 1) {
				rdoSurname = this.getRandomNumber(listSize - 1);
			}
			SurnameModel currentSurname = listSurname.get(rdoSurname);
			data.put("currentSurname", currentSurname);
			// logger.info("VT名人库请求开始："+req.getSession().getId()+"----"+DateUtilC.getNowDateTime());
			// 获取历史名人
			List<Person> listPerson = this.getPersons(currentSurname);

			data.put("person", listPerson);
			// logger.info("VT名人库请求结束："+req.getSession().getId()+"----"+DateUtilC.getNowDateTime());

			// 获取名人家谱
			// logger.info("VT家谱库请求开始："+req.getSession().getId()+"----"+DateUtilC.getNowDateTime());

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
			data.put("work", listWork);
			Work currentWork = new Work();
			if (listWork != null && listWork.size() > 0) {
				currentWork = listWork.get(0);
			}
			data.put("currentWork", currentWork);

			modelAndView.setViewName("sysmanager/home/index");
			modelAndView.addObject("data", data);
		} catch (Exception e) {
			logger.info("HomeController-index错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return modelAndView;
	}

	/**
	 * 根据英文首字母查询姓
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doInitial", method = RequestMethod.POST)
	public Map<String, Object> doInitial(HomeSearchModel searchModel)
			throws Exception {

		Map<String, Object> result = new HashMap<>();
		try {
			// 获取所有姓氏数据
			List<SurnameModel> listSurname = homeMapper.getSurnameList(searchModel
					.getFirstChar());

			if (searchModel.getFirstChar().equals("A")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("B"));
			} else if (searchModel.getFirstChar().equals("E")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("F"));
			} else if (searchModel.getFirstChar().equals("O")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("P"));
			} else if (searchModel.getFirstChar().equals("R")) {
				listSurname.addAll(listSurname.size(),
						homeMapper.getSurnameList("S"));
			}

			result.put("surname", listSurname);
		} catch (Exception e) {
			logger.info("HomeController-doInitial错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		
		return result;
	}

	/**
	 * 根据姓氏查询名人和家谱
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doSurname", method = RequestMethod.POST)
	public Map<String, Object> doSurname(HomeSearchModel searchModel)
			throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			// 获取单个姓氏数据
			SurnameModel currentSurname = homeMapper.getSurname(searchModel
					.getFirstChar());
			result.put("currentSurname", currentSurname);
			// 获取历史名人
			List<Person> listPerson = this.getPersons(currentSurname);
			result.put("person", listPerson);
			// 获取名人家谱
			List<Work> listWork = new ArrayList<Work>();
			Person currentPerson = new Person();
			for (Person p : listPerson) {
				if (!StringUtilC.isEmpty(p.getRelatedWork())) {
					listWork = this.gerWorks(p.getRelatedWork(), p.getUri());
					currentPerson = p;
					break;
				}
			}
			result.put("currentPerson", currentPerson);
			result.put("work", listWork);
			Work currentWork = new Work();
			if (listWork != null && listWork.size() > 0) {
				currentWork = listWork.get(0);
			}
			result.put("currentWork", currentWork);
		} catch (Exception e) {
			logger.info("HomeController-doSurname错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		
		return result;
	}

	/**
	 * 根据姓名查询家谱
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doFullName", method = RequestMethod.POST)
	public Map<String, Object> dofullName(HomeSearchModel searchModel)
			throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			// 获取名人家谱
			List<Work> listWork = this.gerWorks(searchModel.getFirstChar(),
					searchModel.getPuri());
			result.put("work", listWork);
			Work currentWork = new Work();
			if (listWork != null && listWork.size() > 0) {
				currentWork = listWork.get(0);
			}
			result.put("currentWork", currentWork);
		} catch (Exception e) {
			logger.info("HomeController-doFullName错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		
		return result;
	}

	/**
	 * 根据姓名查询家谱
	 * 
	 * @param searchModel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkByUri", method = RequestMethod.POST)
	public Map<String, Object> getWorkByUri(String uri, String puri)
			throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			Work w = workService.getWork(uri, false);
			// 如果正题名为空，则取副题名
			if (StringUtilC.isEmpty(w.getTitle())) {
				w.setTitle(w.getSubTitle());
			}
			// personUri
			w.setPuri(puri);
			result.put("work", w);
		} catch (Exception e) {
			logger.info("HomeController-getWorkByUri错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		
		return result;
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
		if (!StringUtilC.isEmpty(surname.getCelebrityCnt())
				&& !"0".equals(surname.getCelebrityCnt())) {
			Pager pager = new Pager();
			pager.setPageSize(5);
			pager.calcPageCount(Long.parseLong(surname.getCelebrityCnt()));
			List<Integer> pageths = pager.getPageths();
			int pageth = this.getRandomNumber(pageths.get(pageths.size() - 1));
			pager.setPageth(pageth);
			listPerson = this.personService.getPersonsInHome(surname.getUri(),
					pager);
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
			w = workService.getWork(urls[i], false);
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
