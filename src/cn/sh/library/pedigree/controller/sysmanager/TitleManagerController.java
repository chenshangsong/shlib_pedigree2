package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.services.TitleService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 谱名表
 * 
 * @author chenss
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/titleManager")
public class TitleManagerController extends BaseController {


	@Resource
	private TitleService titleService;
	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	// 需要关闭的字段
	private static String[] closeList = { "parentCountry", "temporal",
			"titleType", "hasAnnotation", "identifier", "relatedTo", "role",
			"family", "identifiedBy", "place", "temporalValue","partOf","code","accessLevel" ,"content","DOI"};

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "sysmanager/titlemanager/list";
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<String, Object> loadList(WorkSearchBean search, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		result.put("pager", pager);
		List<Map<String, String>> list = this.titleService.list(
				search.getTitle(), pager);
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
					Constant.GRAPH_TITLE, id);
			// 规范数据编辑属性
			DataEditViewDto dataEditView = DataManagerDto
					.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
			//设置ID，防止新增时，打开本页面，ID为空
			dataEditView.setId(id);
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_TITLE);
			// pageUrl
			dataEditView.setPageUrl("titleManager");
			// 规范数据类型区分：谱籍
			dataEditView.setDataType(DataTypeGroup.Title.getGroup());
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
					// relatedTo单独处理
					if (dataEditView.getEditList().get(i).getOldEnName()
							.equals("relatedTo")) {
					} // 相关作品
					else if (dataEditView.getEditList().get(i).getOldEnName()
							.equals("relatedWork")) {
						// 得到中文旧值
						_thisStream = ws.getWorkAllInfos(dataEditView
								.getEditList().get(i).getOldValue());
						_thisJson = JSONObject.fromObject(StringUtilC
								.StreamToString(_thisStream));
						String titleValue = null;
						if (_thisJson != null && _thisJson.size() > 0) {
							JSONArray _array = _thisJson.getJSONArray("@graph");
							for (int j = 0; j < _array.size(); j++) {
								JSONObject jo = (JSONObject) _array.get(j);
								Iterator keys = jo.keys();
								while (keys.hasNext()) {
									String key = (String) keys.next();
									String value = jo.get(key).toString();
									if (key.equals("title")) {
										titleValue = value;
										break;
									}
								}
								if (!StringUtilC.isEmpty(titleValue)) {
									dataEditView.getEditList().get(i)
											.setOldValueCn(titleValue);
									break;
								}
							}
						}
					} else {
						// 得到中文旧值
						_thisStream = commonSparql.getJsonLD4Resource(
								Constant.GRAPH_BASEINFO, dataEditView
										.getEditList().get(i).getOldValue());
						_thisJson = JSONObject.fromObject(StringUtilC
								.StreamToString(_thisStream));
						if (_thisJson != null && _thisJson.size() > 0) {
							if (_thisJson.getString("label") != null) {
								String label = _thisJson.getString("label");
								if (label.startsWith("[")) {
									label = label.replace("[", "");
									label = label.replace("]", "");
								}
								dataEditView.getEditList().get(i)
										.setOldValueCn(label);
							}
							// 简称
							if (_thisJson.getString("abbreviateName") != null) {
								String label = _thisJson
										.getString("abbreviateName");
								if (label.startsWith("[")) {
									label = label.replace("[", "");
									label = label.replace("]", "");
								}
								dataEditView.getEditList().get(i)
										.setOldValueCn(label);
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
}
