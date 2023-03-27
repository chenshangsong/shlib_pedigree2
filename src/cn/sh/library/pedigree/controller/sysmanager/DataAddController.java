package cn.sh.library.pedigree.controller.sysmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataUtilC;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.sysManager.mapper.FamilyNameMapper;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.FeedBackDetailModel;


@Controller
@RequestMapping("/dataAddManager")
public class DataAddController extends BaseController {

	@Autowired
	private FamilyNameMapper familyNameMapper;

	@ResponseBody
	@RequestMapping(value = "/familyName", method = RequestMethod.GET)
	public ModelAndView feedBackMainList() throws Exception {

		try {

			modelAndView.setViewName("sysmanager/familyNameManager/add");
			return modelAndView;
			
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
			return null;
		}
	}

	/**
	 * 编辑页面
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addFamilyName", method = { RequestMethod.POST })
	public String importFamilyName(@RequestBody FeedBackDetailModel _detail) throws Exception {

		try {

			/*
			 * List<FamilyNameModel> modelList = familyNameMapper
			 * .getTableData(new FamilyNameModel()); for (int i = 0; i <
			 * modelList.size(); i++) { modelList.get(i).setSelfUri(
			 * DataImportUtil.getRandomUriValue(16));
			 * familyNameMapper.updateFamilyNameById(modelList.get(i)); }
			 */

			List<FamilyNameModel> modelList2 = familyNameMapper
					.getTableData(new FamilyNameModel());
			
			DataUtilC.InsertFamilyName(modelList2);

			/*
			 * List<FamilyNameModel> person1List = familyNameMapper
			 * .getXZData(new FamilyNameModel()); //DataImportUtil.PersonTest();
			 * System.out.println("人第" + 1 + "个：" + commonSparql.getResInfos(
			 * "http://gen.library.sh.cn/graph/baseinfo",
			 * "http://data.library.sh.cn/entity/person/0126"));
			 */

			// 数据导入测试

			// JSONArray JsonArray = DataImportUtil.parsJsonArray(modelList);
			// DataImportUtil.InsertV(JsonArray);

			/*
			 * int ii = 1; for (FamilyNameModel object : modelList) {
			 * System.out.println("姓氏第" + ii + "个：" + commonSparql.getResInfos(
			 * "http://gen.library.sh.cn/graph/baseinfo", object.getUri()));
			 * ii++; } System.out.println("姓氏共: " + modelList.size() + "  个");
			 */

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		jsonResult.put(result, "0");
		// jsonResult.put("data", modelList);
		return JSonUtils.toJSon(jsonResult);
	}

}