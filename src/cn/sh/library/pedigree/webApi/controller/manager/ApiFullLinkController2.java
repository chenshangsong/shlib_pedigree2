package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.FullLink4ESJPSearchBean;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.IPUtils;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.webApi.services.ApiFullLinkService;
import net.sf.json.JSONArray;

/**
 * @author CM
 */
@Controller
//@RequestMapping("/webapi/manager/fullLink")
public class ApiFullLinkController2 extends BaseController {

	@Resource
	private ApiFullLinkService apiFullLinkService;

	@Autowired
	private RedisUtils redisUtil;

	/**
	 * 根据 accessLevel与doi, 获取fulltextLink
	 */
	@ResponseBody
	@RequestMapping(value = "/getFulltextLink", method = RequestMethod.GET)
	public String getFulltextLink(FullLink4ESJPSearchBean fullLink4ESJPSearchBean) {
		jsonResult = new HashMap<>();
		Map<String, String> _map = apiFullLinkService.getFulltextLink(fullLink4ESJPSearchBean);
		String fulltextLink = _map != null ? _map.get("link") : "";
		String fulltextLinkPDF = _map != null ? _map.get("linkPDF") : "";
		jsonResult.put("fulltextLink", fulltextLink);
		jsonResult.put("fulltextLinkPDF", fulltextLinkPDF);
		logger.info("webapi/manager/fullLink/getFulltextLink-chenss" + DateUtilC.getNowDateTime());
		return JSonUtils.toJSon(jsonResult);
	}

}
