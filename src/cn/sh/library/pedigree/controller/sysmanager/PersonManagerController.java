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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.FamilyNameMapper;
import cn.sh.library.pedigree.utils.StringUtilC;
import flexjson.JSONSerializer;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/personManager")
public class PersonManagerController extends BaseController {

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
	//,"partOf","code","accessLevel" ,"content","DOI" 新增加
	// 需要关闭的字段
	private static String[] closeList = { "parentCountry", "hasAnnotation", "identifier",
			"relatedTo", "family","identifiedBy","eventmigration","migration","role","event","place","headOf","name","temporal","partOf","code","accessLevel" ,"content","DOI" };
	// 多值字段
		private static String[] morValueCList = { "label" };
	/**
	 * 获取攥修者列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/writerList", method = RequestMethod.GET)
	public ModelAndView writerList() throws Exception {
		try {
			modelAndView.setViewName("sysmanager/writermanager/list");
		} catch (Exception e) {
			System.out.println("错误信息：" + e.getMessage());
		}
		// 规范数据类型区分：攥修者
		modelAndView.addObject("dataTypeId", DataTypeGroup.Writer.getGroup());
		return modelAndView;
	}
	/**
	 * 获取攥修者列表弹框
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/confwriterList", method = RequestMethod.GET)
	public ModelAndView confwriterList() throws Exception {
		try {
			modelAndView.setViewName("sysmanager/writermanager/Conflist");
		} catch (Exception e) {
			System.out.println("错误信息：" + e.getMessage());
		}
		// 规范数据类型区分：攥修者
		modelAndView.addObject("dataTypeId", DataTypeGroup.Writer.getGroup());
		return modelAndView;
	}
	/**
	 * 获取先祖信息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ancestorsList", method = RequestMethod.GET)
	public ModelAndView xzList() throws Exception {
		try {
			modelAndView.setViewName("sysmanager/ancestorsmanager/list");
		} catch (Exception e) {
			System.out.println("错误信息：" + e.getMessage());
		}
		// 规范数据类型区分：先祖
		modelAndView
				.addObject("dataTypeId", DataTypeGroup.Ancestors.getGroup());
		return modelAndView;
	}

	/**
	 * 获取名人信息列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/famousList", method = RequestMethod.GET)
	public ModelAndView famousList() throws Exception {
		try {
			modelAndView.setViewName("sysmanager/famousmanager/list");
		} catch (Exception e) {
			System.out.println("错误信息：" + e.getMessage());
		}
		// 规范数据类型区分：名人
		modelAndView.addObject("dataTypeId", DataTypeGroup.Famous.getGroup());
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
			PersonSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		result.put("pager", pager);
		// 规范数据业务区分：先祖，名人，攥修者
		result.put("dataTypeId", search.getDataTypeId());
		result.put(
				"persons",
				this.personService.list(search, search.getTag(),
						"", pager));
		/* result.put("persons", this.personService.list(search, true, pager)); */
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
	 * 编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dataContentlist", method = RequestMethod.GET)
	public ModelAndView dataContentlist(@Param(value = "id") String id,
			@Param(value = "dataTypeId") String dataTypeId) throws Exception {
		try {
			OutputStream _outStream = commonSparql.getJsonLD4Resource(
					Constant.GRAPH_PERSON, id);
			// 规范数据编辑属性
			DataEditViewDto dataEditView = DataManagerDto
					.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
			//设置ID，防止新增时，打开本页面，ID为空
			dataEditView.setId(id);
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_PERSON);
			// pageUrl
			dataEditView.setPageUrl("personManager");
			// 规范数据类型区分：先祖/名人/攥修者
			dataEditView.setDataType(dataTypeId);
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
					Person person = this.personService.get(
							dataEditView.getId(), false);
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
					// relatedTo单独处理
					if (dataEditView.getEditList().get(i).getOldEnName()
							.equals("relatedTo")) {
					} // 相关作品
					/*
					 * else if (dataEditView.getEditList().get(i).getOldEnName()
					 * .equals("relatedWork")) { // 得到中文旧值 _thisStream =
					 * ws.getWorkAllInfos(dataEditView
					 * .getEditList().get(i).getOldValue()); _thisJson =
					 * JSONObject.fromObject(_thisStream .toString()); String
					 * titleValue = null; if (_thisJson != null &&
					 * _thisJson.size() > 0) { JSONArray _array =
					 * _thisJson.getJSONArray("@graph"); for (int j = 0; j <
					 * _array.size(); j++) { JSONObject jo = (JSONObject)
					 * _array.get(j); Iterator keys = jo.keys(); while
					 * (keys.hasNext()) { String key = (String) keys.next();
					 * String value = jo.get(key).toString(); if
					 * (key.equals("title")) { titleValue = value; break; } } if
					 * (!StringUtilC.isEmpty(titleValue)) {
					 * dataEditView.getEditList().get(i)
					 * .setOldValueCn(titleValue); break; } } } }
					 */// 父：
					else if (dataEditView.getEditList().get(i).getOldEnName()
							.equals("childOf")) {
						// 得到person对象
						Person person = this.personService.get(dataEditView
								.getEditList().get(i).getOldValue(), false);
						dataEditView.getEditList().get(i)
								.setOldValueCn(person.getName());
					} else {
						// 朝代
						if ("temporal".equals(dataEditView.getEditList().get(i)
								.getOldEnName())) {
							// 得到中文旧值
							_thisStream = commonSparql
									.getJsonLD4Resource(
											Constant.GRAPH_TEMPLATE,
											dataEditView.getEditList().get(i)
													.getOldValue());
						} else {

							// 得到中文旧值
							_thisStream = commonSparql
									.getJsonLD4Resource(
											Constant.GRAPH_BASEINFO,
											dataEditView.getEditList().get(i)
													.getOldValue());
						}
						_thisJson = JSONObject.fromObject(StringUtilC
								.StreamToString(_thisStream));
						if (_thisJson != null && _thisJson.size() > 0) {

							if (_thisJson.getString("label") != null) {
								dataEditView
										.getEditList()
										.get(i)
										.setOldValueCn(
												_thisJson.get("label")
														.toString());
							}
						}
					}

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

	/**
	 * 保存处理
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doSave", method = { RequestMethod.POST })
	public String doSave(@RequestBody DataEditViewDto dto) throws Exception {
		boolean ifSuccess = false;
		try {
			String g = Constant.GRAPH_PERSON;
			String s = dto.getId();
			for (DataEditDto editDto : dto.getEditList()) {
				String p = StringUtilC.getString(editDto.getOldCnNameUri());
				String old_o = StringUtilC.getString(editDto.getOldValue());
				String o = StringUtilC.getString(editDto.getNewValue());
				old_o = EditDtoCommon.getValue(old_o);
				o = EditDtoCommon.getValue(o);
				// 旧值，新值都不为空，则进行更新
				if (!old_o.equals("") || !o.equals("")) {
					// 新值和旧值不相等的情况下，更新
					if (!old_o.equals(o)) {
						ifSuccess = commonSparql.changeRDF(g, s, p, old_o, o);
					}
				}
			}
		} catch (Exception e) {
			ifSuccess = false;
		}
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
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
	public ModelAndView dataContentAdd(@Param(value = "type") String type) throws Exception {
		try {
			@SuppressWarnings("unchecked")
			// 取值词表类下的数据
			List<Map<String, String>> thisList = vocabSparql
					.getSubjectProperties("shl:Person");

			// 新增时，dto做成
			DataEditViewDto addDto = DataManagerDto.fillAddDto(thisList);
			// dataType:4,5,6先祖，名人，纂修者
			addDto.setDataType(type);
			// GRAPH
			addDto.setGraphUri(Constant.GRAPH_PERSON);
			// pageUrl
			addDto.setPageUrl("personManager");
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
							.setOldValue(EditDtoCommon.moreValueString);
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