package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.Trans2PinYin;
import cn.sh.library.pedigree.common.dataImport.DataNewUtil;
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.common.dataImport.ModelMakeUtil;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.services.CategoryService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.CategoryManagerMapper;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.CategoryTypeModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 取词词表
 * 
 * @author chenss
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/categoryManager")
public class CategoryManagerController extends BaseController {

	@Resource
	private CategoryService categoryService;
	@Autowired
	private CategoryManagerMapper categoryManagerMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	/**
	 * 取值词表分类url
	 */
	private String categoryTypeUrl = "http://data.library.sh.cn/%svocab/%s/";
	/**
	 * 取值词表分类map
	 */
	private static Map<String, CategoryTypeModel> ctMap = new HashMap<String, CategoryTypeModel>();
	// 需要关闭的字段
	private static String[] closeList = { "categoryValue", "hasAnnotation",
			"identifiedBy", "relatedTo", "temporalValue", "place", "temporal","partOf","code","accessLevel" ,"content","DOI" };
//,"partOf","code","accessLevel" ,"content","DOI"
	/**
	 * 分类删除
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/categoryTypeDelete", method = RequestMethod.POST)
	public String categoryTypeDelete(@Param(value = "id") Integer id)
			throws Exception {

		boolean ifSuccess = true;
		try {

			if (StringUtilC.isEmpty(id)) {
				ifSuccess = false;
			} else {
				// 删除分类
				Integer ret = categoryManagerMapper.deleteCategoryType(id);
				if (ret > 0) {
					ifSuccess = true;
				} else {
					ifSuccess = false;
				}
			}

		} catch (Exception e) {
			ifSuccess = false;
			System.out.println("数据删除错误：" + e.getMessage());
		}

		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 分类编辑提交
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/categoryTypeSubmit", method = RequestMethod.POST)
	public String categoryTypeSubmit(@RequestBody CategoryTypeModel model,HttpSession httpSession)
			throws Exception {
		try {

			Integer id = model.getId();
			Integer ifSucess = 0;
			String userId = StringUtilC.getString(getUser(httpSession).getId());
			model.setCREATED_BY(userId);
			// 如果用户输入“用于数据集”，则进行拼接
			if (!StringUtilC.isEmpty(model.getCategoryUriIn())) {
				model.setDataNamespace(String.format(categoryTypeUrl,
						model.getCategoryUriIn() + "/",
						model.getCategoryNameEn()));
			} else {
				model.setDataNamespace(String.format(categoryTypeUrl, "",
						model.getCategoryNameEn()));
			}
			//翻译转换-去除特殊字符
			model=(CategoryTypeModel)StringUtilC.convertModel(model);
			// 如果数据ID为空，则进行新增操作，否则进行更新操作
			if (StringUtilC.isEmpty(id)) {
				ifSucess = categoryManagerMapper.insertCategoryType(model);
			} else {
				ifSucess = categoryManagerMapper.updateCategoryType(model);
			}
			// 如果成功
			if (ifSucess > 0) {
				jsonResult.put(result, FWConstants.result_success);
			}
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 分类编辑页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/categoryTypeEdit", method = RequestMethod.GET)
	public ModelAndView categoryTypeEdit(@Param(value = "id") Integer id)
			throws Exception {
		try {
			CategoryTypeModel data = null;
			if (!StringUtilC.isEmpty(id)) {
				// 获取分类对象
				data = categoryManagerMapper.getCategoryType(id);
			}
			modelAndView
					.setViewName("sysmanager/categorymanager/categoryTypeEdit");
			modelAndView.addObject("data", data);
			return modelAndView;
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
			return null;
		}
	}

	/**
	 * 分类列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/categoryTypelist", method = RequestMethod.GET)
	public ModelAndView categoryTypelist() throws Exception {
		try {
			// 获取分类列表List
			List<CategoryTypeModel> dataList = categoryManagerMapper
					.getCategoryTypeList();
			modelAndView.addObject("data", dataList);
			modelAndView
					.setViewName("sysmanager/categorymanager/categoryTypeList");
			return modelAndView;

		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
			return null;
		}
	}

	/**
	 * 初始化页面
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() throws Exception {
		try {
			// 分类下拉单数据
			List<CategoryTypeModel> ctList = categoryManagerMapper
					.getCategoryTypeList();
			// 存放取值词表分类Map,如果发现数据库中记录条数，与map现有条数不一致，则进行map初始化，暂时使用
			// if (ctList.size() != ctMap.size()) {
			ctMap = new HashMap<String, CategoryTypeModel>();
			for (CategoryTypeModel categoryTypeModel : ctList) {
				ctMap.put(categoryTypeModel.getCategoryNameEn(),
						categoryTypeModel);
			}
			// }
			modelAndView.addObject("data", ctList);
			modelAndView.setViewName("sysmanager/categorymanager/list");
			return modelAndView;
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
			return null;
		}
	}

	/**
	 * 弹框列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/conflist", method = RequestMethod.GET)
	public ModelAndView conflist(@Param(value = "ctType") String ctType)
			throws Exception {
		try {
			String ns = "bf:Category";
			String type = "bf:label";
			List<Map<String, String>> list = categoryService.list(
					Constant.GRAPH_BASEINFO, ns, ctType, type);
			modelAndView.addObject("data", list);
			modelAndView.setViewName("sysmanager/categorymanager/Conflist");
			return modelAndView;
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 取值词表新增页面
	 * 
	 * @param ctType
	 *            ：分类类别
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dataContentAdd", method = RequestMethod.GET)
	public ModelAndView dataContentAdd(@Param(value = "ctType") String ctType)
			throws Exception {
		try {
			@SuppressWarnings("unchecked")
			// 取值词表类下的数据
			List<Map<String, String>> thisList = vocabSparql
					.getSubjectProperties("bf:Category");

			// 新增时，dto做成
			DataEditViewDto addDto = DataManagerDto.fillAddDto(thisList);
			// dataType
			addDto.setDataType(DataTypeGroup.Category.getGroup());
			// GRAPH
			addDto.setGraphUri(Constant.GRAPH_BASEINFO);
			// pageUrl
			addDto.setPageUrl("ancestraltempleManager");
			for (int i = 0; i < addDto.getEditList().size(); i++) {
				DataEditDto tempDto = addDto.getEditList().get(i);
				// 如果是需要关闭的字段
				if (Arrays.asList(closeList).contains(tempDto.getOldEnName())) {
					// 关闭
					addDto.getEditList().get(i).setOpenFlg(FWConstants.isTrue);
				}
				// 如果是规范名，则证明是两个值:简体，繁体,便于绘制新增页面是分割简繁体
				if (tempDto.getOldEnName().contains("label")) {
					addDto.getEditList().get(i)
							.setOldValue(EditDtoCommon.moreValueString);
					// 多值标识
					addDto.getEditList().get(i).setIsMore(FWConstants.isTrue);
				}
				// 如果是分类类型
				if (tempDto.getOldEnName().contains("categoryType")) {
					// uri标识
					addDto.getEditList().get(i).setIsUri(FWConstants.isTrue);
					// 新值，英文
					addDto.getEditList().get(i).setNewValue(ctType);
					// 新值，中文
					addDto.getEditList()
							.get(i)
							.setNewValueCn(
									ctMap.get(ctType).getCategoryNameCn());
				}
			}
			// 被编辑对象以及下属属性列表
			modelAndView.addObject("data", addDto);
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		modelAndView.setViewName("sysmanager/categorymanager/dataContentAdd");
		return modelAndView;
	}

	/**
	 * 取值词，新增保存处理
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doSaveAdd", method = { RequestMethod.POST })
	public String doSaveAdd(@RequestBody DataEditViewDto dto,HttpSession hs) throws Exception {
		boolean ifSuccess = true;
		// 新插入主表ID
		Integer insertMainId = null;
		try {
			/*----------------------------实体做成--------------------------------*/
			CategoryModel model = ModelMakeUtil.getCategoryModel(dto);
			// 规范名，汉语-拼音
			String pinyin = Trans2PinYin.convertAll(model.getLabel());
			// 拼接，做成dataUri
			model.setSelfUri(ctMap.get(model.getType()).getDataNamespace()
					+ pinyin);
			/*----------------------------保存历史表--------------------------------*/
			List<DataChangeHistoryListModel> list = new ArrayList<DataChangeHistoryListModel>();
			// 将页面对象，转换为历史列表对象
			list = ModelMakeUtil.ContentToHistoryList(dto);
			if (list.size() > 0) {
				DataChangeHistoryMainModel mainModel = new DataChangeHistoryMainModel();
				mainModel.setDataType(dto.getDataType());
				// 父URI
				mainModel.setPdataUri(dto.getPdataUri());
				mainModel.setRemarks(dto.getRemarks());
				mainModel.setCreateUser(StringUtilC.getString(getUser(hs).getId()));
				// 如果ID为空，则判断为新增模式，根据数据类型，自动生成Uri
				if (StringUtilC.isEmpty(dto.getId())) {
					dto.setId(model.getSelfUri());
					// 新增标识
					mainModel.setAddFlg(FWConstants.isTrue);
				}
				mainModel.setDataUri(dto.getId());
				mainModel.setGraphUri(dto.getGraphUri());
				// 保存主表
				dataChangeHistoryMapper.insertHistoryMain(mainModel);
				insertMainId = mainModel.getId();
				for (DataChangeHistoryListModel model2 : list) {
					model2.setMainId(mainModel.getId());
					// 保存明细表
					dataChangeHistoryMapper.insertHistoryList(model2);
				}
			}
			/*----------------------------发布，暂时屏蔽--------------------------------*/
			// 发布到SQL
			categoryManagerMapper.insertShlCategory(model);
			// 发布到VT
			DataNewUtil.InsertCategory(model);
			// 更新历史主表状态
			dataChangeHistoryMapper.updateHistoryMain(
					StringUtilC.getInteger(insertMainId), StringUtilC.getString(getUser(hs).getId()), DateUtilC.getNowDate());
			ifSuccess = true;
		} catch (Exception e) {
			ifSuccess = false;
			System.out.println("数据保存错误：" + e.getMessage());
		}
		jsonResult.put(result, ifSuccess);
		jsonResult.put("id", dto.getId());
		// 新插入主表ID
		jsonResult.put("insertHistoryMainId", insertMainId);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 取值词表List
	 * 
	 * @param search
	 * @param pager
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<String, Object> loadList(WorkSearchBean search, Pager pager)
			throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {

			String g = Constant.GRAPH_BASEINFO;
			// String ns = "http://bibframe.org/vocab/categoryType";
			String ns = "bf:Category";

			// String val = "roleOfFamily";
			String val = "edition";
			if (StringUtilC.isEmpty(search.getTang())) {
				search.setTang(val);
			}
			// String type = "http://bibframe.org/vocab/categoryValue";
			String type = "bf:label";
			List<Map<String, String>> list = categoryService.list(g, ns,
					search.getTang(), type);
			result.put("works", list);

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dataContentlist", method = RequestMethod.GET)
	public ModelAndView dataContentlist(@Param(value = "id") String id)
			throws Exception {
		try {
			OutputStream _outStream = commonSparql.getJsonLD4Resource(
					Constant.GRAPH_BASEINFO, id);
			// 规范数据编辑属性
			DataEditViewDto dataEditView = DataManagerDto
					.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
			// 设置ID，防止新增时，打开本页面，ID为空
			dataEditView.setId(id);
			// new add by chenss 新增时，使用
			if (StringUtilC.isEmpty(dataEditView.getId())) {
				dataEditView.setId(id);
			}
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_BASEINFO);
			// pageUrl
			dataEditView.setPageUrl("ancestraltempleManager");
			// 规范数据类型区分：取词词表
			dataEditView.setDataType(DataTypeGroup.Category.getGroup());
			for (int i = 0; i < dataEditView.getEditList().size(); i++) {
				DataEditDto tempDto = dataEditView.getEditList().get(i);
				// 如果是需要关闭的字段
				if (Arrays.asList(closeList).contains(tempDto.getOldEnName())) {
					// 关闭
					dataEditView.getEditList().get(i).setOpenFlg("1");
				}
				// 根据英文URI得到中文名称
				String ChName = vocabSparql.getPropertyLabel(tempDto
						.getOldCnNameUri());
				// 根据英文URI得到注释名称
				String ChComment = vocabSparql.getPropertyComment(tempDto
						.getOldCnNameUri());
				// 得到中文名称
				dataEditView.getEditList().get(i).setOldCnName(ChName);
				// 得到中文名称
				dataEditView.getEditList().get(i).setOldComment(ChComment);
				if (tempDto.getOldValue().contains("http")
						|| tempDto.getOldValue().contains("_:b")) {
					// 是URI标记
					dataEditView.getEditList().get(i).setIsUri("1");
				}
				// 如果是：类型，则进行英文，中文翻译
				if (tempDto.getOldEnName().contains("categoryType")) {
					// 类别中文名称
					String ctTypeName = ctMap.get(tempDto.getOldValue())
							.getCategoryNameCn();
					// 新值，中文
					dataEditView.getEditList().get(i).setNewValueCn(ctTypeName);
					// 新值，中文
					dataEditView.getEditList().get(i).setNewValue(ctTypeName);
					// 旧值：中文
					dataEditView.getEditList().get(i).setOldValue(ctTypeName);
				}
			}
			// 列表处理
			dataEditView = EditDtoCommon.listEmptyChange(dataEditView);
			// 被编辑对象以及下属属性列表
			modelAndView.addObject("data1", dataEditView);

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		modelAndView.setViewName("sysmanager/dataInfo/dataContentList");
		return modelAndView;
	}
}
