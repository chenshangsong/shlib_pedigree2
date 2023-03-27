package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONObject;

/**
 * @author chenss
 * @date 2014/12/27 0027
 */
@Controller
@RequestMapping("/placeManager")
public class PlaceManagerController extends BaseController {

	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Resource
	private PlaceService placeService;
	// 需要关闭的字段
	private static String[] closeList = { "parentCountry", "temporal",
			"parentADM1", "parentADM2", "parentADM3", "parentADM4", "url",
			"hasAnnotation", "identifier", "relatedTo", "role", "family",
			"identifiedBy", "place", "temporalValue","partOf","code","accessLevel" ,"content","DOI" };
	//,"partOf","code","accessLevel" ,"content","DOI"
	// 多值字段
	private static String[] morValueCList = { "label", "country" };

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		return "sysmanager/placemanager/list";
	}

	/**
	 * 谱籍弹框页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/conflist", method = RequestMethod.GET)
	public String conflist(Model model) {
		return "sysmanager/placemanager/Conflist";
	}
	@ResponseBody
	@RequestMapping(value = "/listPlaces", method = RequestMethod.GET)
	public List<Map<String, String>> listPlaces() {
		return this.placeService.list();
	}

	@ResponseBody
	@RequestMapping(value = "/listInArea", method = RequestMethod.GET)
	public List<Map<String, String>> listInArea(String points, String familyName) {
		return this.placeService.list(points, familyName);
	}

	@ResponseBody
	@RequestMapping(value = "/name")
	public String getCityName(String longx, String lat) {
		return this.placeService.getCityByPoint(longx, lat);
	}

	@ResponseBody
	@RequestMapping(value = "/listOrigin", method = RequestMethod.GET)
	public List<Map<String, String>> listOrigin() {
		return this.placeService.listPlacesInOrigin();
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
					Constant.GRAPH_PLACE, id);
			// 规范数据编辑属性
			DataEditViewDto dataEditView = DataManagerDto
					.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
			//设置ID，防止新增时，打开本页面，ID为空
			dataEditView.setId(id);
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_PLACE);
			// pageUrl
			dataEditView.setPageUrl("placeManager");
			// 规范数据类型区分：谱籍
			dataEditView.setDataType(DataTypeGroup.Place.getGroup());
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
							Constant.GRAPH_BASEINFO, dataEditView.getEditList()
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
	public ModelAndView dataContentAdd() throws Exception {
		try {
			@SuppressWarnings("unchecked")
			// 取值词表类下的数据
			List<Map<String, String>> thisList = vocabSparql
					.getSubjectProperties("shl:Place");

			// 新增时，dto做成
			DataEditViewDto addDto = DataManagerDto.fillAddDto(thisList);
			// dataType
			addDto.setDataType(DataTypeGroup.Place.getGroup());
			// GRAPH
			addDto.setGraphUri(Constant.GRAPH_PLACE);
			// pageUrl
			addDto.setPageUrl("placeManager");
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
