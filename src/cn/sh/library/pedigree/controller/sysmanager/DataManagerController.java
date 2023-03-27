package cn.sh.library.pedigree.controller.sysmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataNewUtil;
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.common.dataImport.DataUtilC;
import cn.sh.library.pedigree.common.dataImport.ModelMakeUtil;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.CategoryManagerMapper;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.FamilyNameMapper;
import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.JigouModel;
import cn.sh.library.pedigree.sysManager.model.PersonModel;
import cn.sh.library.pedigree.sysManager.model.PlaceModel;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import flexjson.JSONSerializer;

@Controller
@RequestMapping("/dataManager")
public class DataManagerController extends BaseController {
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	public CommonSparql commonSparql;
	@Resource
	private PersonService personService;
	@Autowired
	private FamilyNameMapper familyNameMapper;
	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	@Autowired
	private CategoryManagerMapper categoryManagerMapper;
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(DataManagerController.class);
	/*
	 * 规范数据编辑主页面
	 */
	@RequestMapping(value = "datalist")
	public ModelAndView datalist(HttpServletResponse response) throws Exception {
		modelAndView.setViewName("sysmanager/dataInfo/dataList");
		return modelAndView;
	}

	/**
	 * 获取人员信息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/personList", method = RequestMethod.GET)
	public ModelAndView personList() throws Exception {
		try {
			modelAndView.setViewName("sysmanager/dataInfo/PersonList");
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

	/**
	 * 获取姓氏列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/familyName", method = RequestMethod.GET)
	public ModelAndView familyName(Model model) {
		List<Map<String, String>> familyNames = this.personService
				.listFamilyNames();
		model.addAttribute("familyNames", familyNames);
		model.addAttribute("jsonData",
				new JSONSerializer().serialize(familyNames));
		modelAndView.setViewName("sysmanager/dataInfo/familyName");
		return modelAndView;
	}

	/**
	 * 获取姓氏列表
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/personFamilyNames", method = RequestMethod.GET)
	public List<Map<String, String>> personFamilyNames() {
		return this.personService.listPersonFamilyNames();
	}

	/**
	 * 保存处理
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doSave", method = { RequestMethod.POST })
	public String doSave(@RequestBody DataEditViewDto dto, HttpSession hs)
			throws Exception {
		boolean ifSuccess = true;
		// 新插入主表ID
		Integer insertMainId = null;
		try {

			boolean ifUpdate = false;
			List<DataChangeHistoryListModel> list = new ArrayList<DataChangeHistoryListModel>();
			//翻译转换-去除特殊字符
			dto=(DataEditViewDto)StringUtilC.convertModel(dto);
			// 将页面对象，转换为历史列表对象
			list = ModelMakeUtil.ContentToHistoryList(dto);
			// 如果属性列表数据大于0，则可更新
			if (list.size() > 0) {
				ifUpdate = true;
			}
			// 如果是书目编辑模式，则永远保存主表，保证数据完整性
			if (dto.getDataType().equals(DataTypeGroup.Work.getGroup())) {
				ifUpdate = true;
			}
			if (ifUpdate) {
				DataChangeHistoryMainModel mainModel = new DataChangeHistoryMainModel();
				mainModel.setDataType(dto.getDataType());
				// 父URI
				mainModel.setPdataUri(dto.getPdataUri());
				mainModel.setRemarks(dto.getRemarks());
				mainModel.setCreateUser(StringUtilC.getString(getUser(hs)
						.getId()));
				// 如果ID为空，则判断为新增模式，根据数据类型，自动生成Uri
				if (StringUtilC.isEmpty(dto.getId())) {
					String newID = DataUtilC.getUri(dto.getDataType());
					dto.setId(newID);
					// 新增标识
					mainModel.setAddFlg(FWConstants.isTrue);
				}
				mainModel.setDataUri(dto.getId());
				mainModel.setGraphUri(dto.getGraphUri());
				// 保存主表
				dataChangeHistoryMapper.insertHistoryMain(mainModel);
				insertMainId = mainModel.getId();
				for (DataChangeHistoryListModel model : list) {
					model.setMainId(mainModel.getId());
					// 保存明细表
					dataChangeHistoryMapper.insertHistoryList(model);
				}
			}
		} catch (Exception e) {
			ifSuccess = false;
			System.out.println("数据保存错误：" + e.getMessage());
			logger.error("数据保存错误："+e.toString());
		}
		jsonResult.put(result, ifSuccess);
		// 操作的URI
		jsonResult.put("id", dto.getId());
		// 新插入主表ID
		jsonResult.put("insertHistoryMainId", insertMainId);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 发布
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doRelease", method = { RequestMethod.POST })
	public String doRelease(@Param(value = "id") String id, HttpSession hs)
			throws Exception {
		boolean ifSuccess = false;
		try {
			// 获取历史主表信息
			DataChangeHistoryMainModel dto = dataChangeHistoryMapper
					.getHistoryMain(id, null);
			List<DataChangeHistoryListModel> listMolde;
			listMolde = dataChangeHistoryMapper.selectHistoryList(StringUtilC
					.getString(dto.getId()));
			String g = dto.getGraphUri();
			String s = dto.getDataUri();
			SurnameModel fmodel = new SurnameModel();
			for (DataChangeHistoryListModel editDto : listMolde) {
				String p = StringUtilC.getString(editDto.getOldCnNameUri());
				String old_o = StringUtilC.getString(editDto.getOldValue());
				String o = StringUtilC.getString(editDto.getNewValue());
				String enName = StringUtilC.getString(editDto.getOldEnName());
				// 如果修改的是姓氏属性
				if (s.contains("http://data.library.sh.cn/authority/familyname")) {
					if(enName.equals("description") ){
						fmodel.setDescription(o);
						fmodel.setUri(s);
						familyNameMapper.updatesSurnameDesByUri(fmodel);
					}
					if(enName.equals("label") ){
						fmodel.setFamilyNameS(EditDtoCommon.getValueFormat(o, "chs"));
						fmodel.setFamilyNameT(EditDtoCommon.getValueFormat(o, "cht"));
						String pinF = EditDtoCommon.getValueFormat(o, "en");
						if(!StringUtilC.isEmpty(pinF)){
							fmodel.setPinyin(EditDtoCommon.getValueFormat(o, "en"));
							//拼音首字母更新
							fmodel.setInitial(pinF.substring(0,1).toUpperCase());
						}
						fmodel.setUri(s);
						familyNameMapper.updatesSurnameDesByUri(fmodel);
					}
				}
				// g: http://gen.library.sh.cn/graph/person
				// s: http://gen.library.sh.cn/Person/17727
				// p: http://gen.library.sh.cn/vocab/family
				// old-o: <http://gen.library.sh.cn/FamilyName/17727>
				// o: <http://gen.library.sh.cn/FamilyName/492>

				// 旧值，新值都不为空，则进行更新

				// * if (!old_o.equals("") || !o.equals("")) { //
				// 新值和旧值不相等的情况下，更新
				// * if (!old_o.equals(o)) { System.out.println("当前更新字段：" +
				// * editDto.getOldEnName() + "--------" +
				// * editDto.getOldCnName()); ifSuccess =
				// * commonSparql.changeRDF(g, s, p, old_o, o); } }

				// 新值和旧值不相等的情况下，更新

				// 旧值不为空，删除旧值
				if (!StringUtilC.isEmpty(old_o)) {
					// 旧值，多值循环先删除，,英文地址，有逗号，当做单值处理
					if (old_o.contains(",") && !enName.equals("address")) {
						String[] oldlist = old_o.split(",");
						for (String thisO : oldlist) {
							thisO = EditDtoCommon.getValue(thisO);
							ifSuccess = commonSparql.changeRDF(g, s, p, thisO,
									"");
						}
					}// 旧值，单值直接删除
					else {
						old_o = EditDtoCommon.getValue(old_o);
						ifSuccess = commonSparql.changeRDF(g, s, p, old_o, "");
					}
				}
				// 新值不为空,插入新值
				if (!StringUtilC.isEmpty(o)) {
					// 新值，多值循环插入,英文地址，有逗号，当做单值处理
					if (o.contains(",") && !enName.equals("address")) {
						String[] olist = o.split(",");
						for (String thisO : olist) {
							thisO = EditDtoCommon.getValue(thisO);
							ifSuccess = commonSparql.changeRDF(g, s, p, "",
									thisO);
						}
					}// 新值，单值直接插入
					else {
						o = EditDtoCommon.getValue(o);
						ifSuccess = commonSparql.changeRDF(g, s, p, "", o);
					}
				}
			}
			// 更新历史主表状态
			if (ifSuccess) {
				String time = DateUtilC.getNowDate();
				dataChangeHistoryMapper.updateHistoryMain(
						StringUtilC.getInteger(id),
						StringUtilC.getString(getUser(hs).getId()), time);
			}

		} catch (Exception e) {
			ifSuccess = false;
			System.out.println("数据发布错误：" + e.getMessage());
			logger.error("数据发布错误："+e.toString());
		}
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 删除
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doDelete", method = { RequestMethod.POST })
	public String doDelete(@RequestBody DataChangeHistoryMainModel dto)
			throws Exception {
		boolean ifSuccess = true;
		try {
			Integer id = dto.getId();
			// 先删除明细表
			dataChangeHistoryMapper.deleteHistoryList(id);
			// 再删除主表，顺序不能颠倒
			dataChangeHistoryMapper.deleteHistoryMain(id);

		} catch (Exception e) {
			ifSuccess = false;
			System.out.println("数据删除错误：" + e.getMessage());
		}
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 新增时发布
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doNewAddRelease", method = { RequestMethod.POST })
	public String doNewAddRelease(@Param(value = "id") String id, HttpSession hs)
			throws Exception {
		boolean ifSuccess = false;
		try {
			// 获取历史主表信息
			DataChangeHistoryMainModel dto = dataChangeHistoryMapper
					.getHistoryMain(id, null);
			// 属性列表
			List<DataChangeHistoryListModel> listMolde = dataChangeHistoryMapper
					.selectHistoryList(StringUtilC.getString(dto.getId()));
			// 地名插入发布
			if (dto.getDataType().equals(DataTypeGroup.Place.getGroup())) {
				PlaceModel model = ModelMakeUtil.getPlaceModel(dto, listMolde);
				ifSuccess = DataNewUtil.InsertPlace(model);
			}
			// 堂号插入发布
			if (dto.getDataType().equals(
					DataTypeGroup.Ancestraltemple.getGroup())) {
				FamilyNameModel model = ModelMakeUtil.getTanghaoModel(dto,
						listMolde);
				ifSuccess = DataNewUtil.InsertTanghao(model);
			}
			// 人员插入发布
			if (dto.getDataType().equals(DataTypeGroup.Ancestors.getGroup())
					|| dto.getDataType()
							.equals(DataTypeGroup.Famous.getGroup())
					|| dto.getDataType()
							.equals(DataTypeGroup.Writer.getGroup())) {
				PersonModel model = ModelMakeUtil
						.getPersonModel(dto, listMolde);
				ifSuccess = DataNewUtil.InsertPerson(model);
			}
			// 姓氏插入发布
			if (dto.getDataType().equals(DataTypeGroup.FamilyName.getGroup())) {
				FamilyNameModel model = ModelMakeUtil.getFamilyModel(dto,
						listMolde);
				ifSuccess = DataNewUtil.InsertFamilyName(model);
			}
			// 机构插入发布
			if (dto.getDataType().equals(DataTypeGroup.Organization.getGroup())) {
				JigouModel model = ModelMakeUtil.getJigouModel(dto, listMolde);
				ifSuccess = DataNewUtil.InsertJiGou(model);
			}
			// 取值词表插入发布
			if (dto.getDataType().equals(DataTypeGroup.Category.getGroup())) {
				CategoryModel model = ModelMakeUtil.getCategoryModel(dto,
						listMolde);
				// 同时插入关系型数据库一条记录
				categoryManagerMapper.insertShlCategory(model);
				ifSuccess = DataNewUtil.InsertCategory(model);
			}
			// 更新历史主表状态
			if (ifSuccess) {
				String time = DateUtilC.getNowDate();
				dataChangeHistoryMapper.updateHistoryMain(
						StringUtilC.getInteger(id),
						StringUtilC.getString(getUser(hs).getId()), time);
			}

		} catch (Exception e) {
			ifSuccess = false;
			System.out.println("数据发布错误：" + e.getMessage());
		}
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

}