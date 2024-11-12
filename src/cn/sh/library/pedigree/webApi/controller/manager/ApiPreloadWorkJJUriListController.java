package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.util.PreloadWorkJJUriList;
import cn.sh.library.pedigree.webApi.services.ApiFamousService;

/**
 * @author CM
 */
@Controller
@RequestMapping("/webapi/manager/preloadWorkJJUriList")
public class ApiPreloadWorkJJUriListController extends BaseController {
	
	@Resource
	private ApiFamousService apiFamousService;
	
	/**
	 * 通过预加载,判断是否为上图胶卷---接口
	 */
	@ResponseBody
	@RequestMapping(value = "/IsSTJJWork", method = RequestMethod.GET)
	public String IsSTJJWork(String workUri){
		jsonResult = new HashMap<>();
		boolean flag = PreloadWorkJJUriList.getInstance().IsSTJJWork(workUri);
		jsonResult.put("flag", flag);
		return JSonUtils.toJSon(jsonResult);
	}

}
