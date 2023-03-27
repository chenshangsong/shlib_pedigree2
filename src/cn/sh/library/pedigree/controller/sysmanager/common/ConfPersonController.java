package cn.sh.library.pedigree.controller.sysmanager.common;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;

@Controller
@RequestMapping("/ConfPerson")
public class ConfPersonController extends BaseController {
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	public CommonSparql commonSparql;
	@Resource
	private PersonService personService;

	/**
	 * 获取人员信息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/personList", method = RequestMethod.GET)
	public ModelAndView personList() throws Exception {
		try {
			modelAndView.setViewName("sysmanager/dataInfo/ConfPersonList");
		} catch (Exception e) {
			System.out.println("错误信息：" + e.getMessage());
		}
		return modelAndView;
	}

	/**
	 * 获取人员信息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPersonList", method = RequestMethod.POST)
	public Map<String, Object> getPersonList(Model model,
			PersonSearchBean search, Boolean inference, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		result.put("pager", pager);
		result.put("persons", this.personService.list(search, inference, pager));
		return result;
	}

}