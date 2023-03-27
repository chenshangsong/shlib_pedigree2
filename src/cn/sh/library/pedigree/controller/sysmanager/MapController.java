package cn.sh.library.pedigree.controller.sysmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.framework.util.PreloadChaodaiList;
import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.model.ApiChaodaiDto;

/**
 * @author liuyi
 * @date 2014/12/27 0027
 */
@Controller
@RequestMapping("/map/place")
public class MapController {
	@Autowired
	public WorkSparql ws;
	@Resource
	private PlaceService placeService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		return "sysmanager/map/list";
	}

	@ResponseBody
	@RequestMapping(value = "/name")
	public String getCityName(String longx, String lat) {
		return this.placeService.getCityByPoint(longx, lat);
	}

	@ResponseBody
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public Map<String, Object> view(String year, Boolean isAll) {
		// 根据年份得到朝代信息	@author 陈铭
		//ChaodaiModel cdInfo = WebApiUtils.getCdInfoByYear(year);
		ApiChaodaiDto dto = PreloadChaodaiList.getInstance().getDynastyAllInfoByYear(year);
		Map<String, Object> result = new HashMap<>();
		// uri = "http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q";
		List<Map<String, String>> works = placeService.listPlacesInChao(year,dto.getUri());
		
		// 更多查询显示
		if (works != null && works.size() > 20) {
			result.put("all", true);
		} else {
			result.put("all", false);
		}

		// 查询全部
		if (!isAll) {
			while (works.size() > 20) {
				works.remove(works.size() - 1);
			}
		}

		result.put("works", works);
		// 朝代信息
		result.put("cdInfo", dto);
		return result;
	}

}
