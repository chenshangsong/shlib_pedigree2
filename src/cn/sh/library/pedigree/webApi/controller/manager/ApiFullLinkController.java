package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.FullLink4ESJPSearchBean;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.webApi.services.ApiFullLinkService;

/**
 * @author CM
 */
@Controller
@RequestMapping("/webapi/manager/fullLink")
public class ApiFullLinkController extends BaseController {
	
	@Resource
	private ApiFullLinkService apiFullLinkService;
	
	/**
	 * 根据 accessLevel与doi, 获取fulltextLink
	 */
	@ResponseBody
	@RequestMapping(value = "/getFulltextLink", method = RequestMethod.GET)
	public String getFulltextLink(FullLink4ESJPSearchBean fullLink4ESJPSearchBean){
		jsonResult = new HashMap<>();
		Map <String,String> _map=apiFullLinkService.getFulltextLink(fullLink4ESJPSearchBean);
		String fulltextLink = _map!=null?_map.get("link"):"";
		String fulltextLinkPDF = _map!=null?_map.get("linkPDF"):"";
		jsonResult.put("fulltextLink", fulltextLink);
		jsonResult.put("fulltextLinkPDF", fulltextLinkPDF);
		return JSonUtils.toJSon(jsonResult);
	}
	
}
