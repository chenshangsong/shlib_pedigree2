package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.services.OrganizationService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONObject;

/**
 * 机构
 * 
 * @author chenss
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/organizationManager")
public class OrganizationController extends BaseController {

	@Resource
	private OrganizationService organizationService;
	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	// 需要关闭的字段
	private static String[] closeList = { "parentCountry", "temporal",
			"hasAnnotation", "identifiedBy", "relatedTo", "role", "family",
			"faxNumber", "legalname", "postalCode", "telephone", "url",
			"event", "hasDataset", "migration", "organizationType",
			"parentOrganization", "place", "relatedWork", "temporalValue",
			"headOf", "name", "level" ,"addressSchema","partOf","code","accessLevel" ,"content","DOI"};
	//,"partOf","code","accessLevel" ,"content","DOI"
	// 多值字段
	private static String[] morValueCList = { "label", "abbreviateName",
			"address" };

	/**
	 * 弹框画面
	 * 
	 * @param search
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "sysmanager/organizationmanager/list";
	}

	@RequestMapping(value = "/confList", method = RequestMethod.GET)
	public String confList() {
		return "sysmanager/organizationmanager/confList";
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<String, Object> loadList(WorkSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		result.put("pager", pager);
		List<Map<String, String>> list = this.organizationService.list(
				search.getPlace(), pager);
		result.put("works", list);
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
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_BASEINFO);
			// pageUrl
			dataEditView.setPageUrl("organizationManager");
			// 规范数据类型区分：机构
			dataEditView.setDataType(DataTypeGroup.Organization.getGroup());
			for (int i = 0; i < dataEditView.getEditList().size(); i++) {
				// 如果是需要关闭的字段
				if (Arrays.asList(closeList).contains(
						dataEditView.getEditList().get(i).getOldEnName())) {
					// 关闭
					dataEditView.getEditList().get(i).setOpenFlg("1");
				}
				// 根据英文URI得到中文名称
				String ChName = vocabSparql.getPropertyLabel(dataEditView
						.getEditList().get(i).getOldCnNameUri());
				// 根据英文URI得到注释名称
				String ChComment = vocabSparql.getPropertyComment(dataEditView
						.getEditList().get(i).getOldCnNameUri());
				// 得到中文名称
				dataEditView.getEditList().get(i).setOldCnName(ChName);
				// 得到中文名称
				dataEditView.getEditList().get(i).setOldComment(ChComment);
				// lable
				if (dataEditView.getEditList().get(i).getOldEnName()
						.equals("label")) {
					// 得到person对象
					/*
					 * Person person = this.personService.get(
					 * dataEditView.getId(), false);
					 */
				}
				// 是URI标记
				else if (dataEditView.getEditList().get(i).getOldValue()
						.contains("http")
						|| dataEditView.getEditList().get(i).getOldValue()
								.contains("_:b")) {
					// 是URI标记
					dataEditView.getEditList().get(i).setIsUri("1");
					OutputStream _thisStream = null;
					JSONObject _thisJson = null;
					// 得到中文旧值
					_thisStream = commonSparql.getJsonLD4Resource(
							Constant.GRAPH_PLACE, dataEditView.getEditList()
									.get(i).getOldValue());
					_thisJson = JSONObject.fromObject(StringUtilC
							.StreamToString(_thisStream));
					if (_thisJson != null && _thisJson.size() > 0) {
						if (_thisJson.getString("label") != null) {
							dataEditView
									.getEditList()
									.get(i)
									.setOldValueCn(
											_thisJson.get("label").toString());
						}
					}
				}
			}
			// 列表处理
			dataEditView = EditDtoCommon.listEmptyChange(dataEditView);
			// 被编辑对象以及下属属性列表
			modelAndView.addObject("data1", dataEditView);
		} catch (Exception e) {
			modelAndView.addObject("data1", null);
			System.out.println("数据列表错误：" + e.getMessage());
		}
		modelAndView.setViewName("sysmanager/dataInfo/dataContentList");
		return modelAndView;
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
	public ModelAndView dataContentAdd()
			throws Exception {
		try {
			@SuppressWarnings("unchecked")
			// 取值词表类下的数据
			List<Map<String, String>> thisList = vocabSparql
					.getSubjectProperties("shl:Organization");

			// 新增时，dto做成
			DataEditViewDto addDto = DataManagerDto.fillAddDto(thisList);
			// dataType:4,5,6先祖，名人，纂修者
			addDto.setDataType(DataTypeGroup.Organization.getGroup());
			// GRAPH
			addDto.setGraphUri(Constant.GRAPH_BASEINFO);
			// pageUrl
			addDto.setPageUrl("organizationManager");
			for (int i = 0; i < addDto.getEditList().size(); i++) {
				DataEditDto tempDto = addDto.getEditList().get(i);
				// 如果是需要关闭的字段
				if (Arrays.asList(closeList).contains(tempDto.getOldEnName())) {
					// 关闭
					addDto.getEditList().get(i).setOpenFlg(FWConstants.isTrue);
				}
				// 如果是规范名，则证明是两个值:简体，繁体,便于绘制新增页面是分割简繁体
				if (Arrays.asList(morValueCList).contains(
						tempDto.getOldEnName())) {
					addDto.getEditList().get(i)
							.setOldValue(EditDtoCommon.moreValue3String);
					addDto.getEditList().get(i).setIsMore(FWConstants.isTrue);
				}

			}
			// 被编辑对象以及下属属性列表
			modelAndView.addObject("data", addDto);
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		modelAndView.setViewName("sysmanager/dataInfo/dataContentAdd");
		return modelAndView;
	}
}
