package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.sysManager.mapper.HomeMapper;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;

/**
 * @author CM
 */
@Controller
@RequestMapping("/webapi/manager/surname")
public class ApiSurnameController extends BaseController {
	@Autowired
	private HomeMapper homeMapper;

	/**
	 * 获取单个姓氏的所有数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getSurname", method = RequestMethod.GET)
	public String getSurname(String surname) {
		jsonResult = new HashMap<>();
		SurnameModel currentSurname = homeMapper.getSurname(surname);
		jsonResult.put("currentSurname", currentSurname);
		return JSonUtils.toJSon(jsonResult);
	}

}
