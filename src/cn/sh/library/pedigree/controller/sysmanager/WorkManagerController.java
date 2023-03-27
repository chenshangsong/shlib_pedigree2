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
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataNewUtil;
import cn.sh.library.pedigree.common.dataImport.DataTypeGroup;
import cn.sh.library.pedigree.common.dataImport.DataUtilC;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.services.PlaceService;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.DataItemChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.DataItemChangeHistoryModel;
import cn.sh.library.pedigree.sysManager.model.WorkDelHistoryModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONObject;

/**
 * 谱名表
 * 
 * @author chenss
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/workManager")
public class WorkManagerController extends BaseController {
	@Resource
	private PlaceService placeService;
	@Resource
	private WorkService workService;
	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	private DataItemChangeHistoryMapper itemMapper;
	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	public static Map<String, DataItemChangeHistoryModel> oldItemList = new HashMap<String, DataItemChangeHistoryModel>();

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "sysmanager/workmanager/list";
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
			// 清空Item列表
			oldItemList = new HashMap<String, DataItemChangeHistoryModel>();
			OutputStream _outStream = commonSparql.getJsonLD4Resource(
					Constant.GRAPH_WORK, id);
			// 规范数据编辑属性
			DataEditViewDto dataEditView = DataManagerDto
					.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
			// 设置ID，防止新增时，打开本页面，ID为空
			dataEditView.setId(id);
			// 需要关闭的字段
			String[] closeList = { "parentCountry", "event", "label",
					"temporalCoverageNote", "temporalValue", "titleType",
					"hasAnnotation", "identifier", "relatedTo", "role",
					"identifiedBy", "temporal","partOf","code","accessLevel" ,"content","DOI" };
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_WORK);
			// pageUrl
			dataEditView.setPageUrl("workManager");
			// 规范数据类型区分：谱籍
			dataEditView.setDataType(DataTypeGroup.Work.getGroup());
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
				// 是URI标记
				if (dataEditView.getEditList().get(i).getOldValue()
						.contains("http")
						|| dataEditView.getEditList().get(i).getOldValue()
								.contains("_:b")) {
					// 是URI标记
					dataEditView.getEditList().get(i).setIsUri("1");
					OutputStream _thisStream = null;
					JSONObject _thisJson = null;
					String oldROldvalue = dataEditView.getEditList().get(i)
							.getOldEnName();
					String graph = "";
					// relatedTo单独处理
					if (oldROldvalue.equals("title")) {
						graph = Constant.GRAPH_TITLE;
					}
					if (oldROldvalue.equals("creator")
							|| oldROldvalue.equals("contributor")) {
						graph = Constant.GRAPH_PERSON;
					}
					if (oldROldvalue.equals("place")) {
						graph = Constant.GRAPH_PLACE;
					}
					if (oldROldvalue.equals("subject")) {
						graph = Constant.GRAPH_BASEINFO;
					}
					if (!StringUtilC.isEmpty(graph)) {
						String title = dataEditView.getEditList().get(i)
								.getOldValue();
						// 如果是多个值
						if (title.contains(",")) {
							String[] obj = title.split(",");
							String label = "";
							for (String _title : obj) {
								// 获取题名类型
								String titleType = commonSparql
										.getResourceClass(graph, _title);
								String titleTypeName = "";
								if (titleType.contains("WorkTitle")) {
									titleTypeName = "【正题名】";
								} else if (titleType.contains("VariantTitle")) {
									titleTypeName = "【变异题名】";
								} else if (titleType.contains("FamilyName")) {
									titleTypeName = "【姓氏】";
								} else if (titleType
										.contains("TitleOfAncestralTemple")) {
									titleTypeName = "【堂号】";
								}
								// 得到中文旧值
								_thisStream = commonSparql.getJsonLD4Resource(
										graph, _title);
								_thisJson = JSONObject.fromObject(StringUtilC
										.StreamToString(_thisStream));
								if (_thisJson != null && _thisJson.size() > 0) {
									if (_thisJson.getString("label") != null) {
										label += titleTypeName
												+ EditDtoCommon
														.getMoreValueCht(_thisJson
																.getString("label"))
												+ ",";
									}
								}
							}
							label = label.substring(0, label.lastIndexOf(','));
							dataEditView.getEditList().get(i)
									.setOldValueCn(label);
						} else {
							String titleTypeName = "";
							// 题名
							if (oldROldvalue.equals("title")
									|| oldROldvalue.equals("subject")) {
								// 获取题名类型
								String titleType = commonSparql
										.getResourceClass(graph, dataEditView
												.getEditList().get(i)
												.getOldValue());
								if (titleType.contains("WorkTitle")) {
									titleTypeName = "【正题名】";
								} else if (titleType.contains("VariantTitle")) {
									titleTypeName = "【变异题名】";
								} else if (titleType.contains("FamilyName")) {
									titleTypeName = "【姓氏】";
								} else if (titleType
										.contains("TitleOfAncestralTemple")) {
									titleTypeName = "【堂号】";
								}
							}
							// 得到中文旧值
							_thisStream = commonSparql.getJsonLD4Resource(
									graph, dataEditView.getEditList().get(i)
											.getOldValue());
							_thisJson = JSONObject.fromObject(StringUtilC
									.StreamToString(_thisStream));
							if (_thisJson != null && _thisJson.size() > 0) {
								if (_thisJson.getString("label") != null) {
									String label = "";
									// 有多个值的字段
									String[] morVlist = new String[] { "title",
											"subject", "creator", "contributor" };
									if (Arrays.asList(morVlist).contains(
											oldROldvalue)) {
										label += titleTypeName
												+ EditDtoCommon
														.getMoreValueCht(_thisJson
																.getString("label"));
									} else {
										label = _thisJson.getString("label");
									}
									dataEditView.getEditList().get(i)
											.setOldValueCn(label);
								}
							}
						}

					}

				}

			}
			// 列表处理
			dataEditView = EditDtoCommon.listEmptyChange(dataEditView);
			// 被编辑对象以及下属属性列表
			modelAndView.addObject("work", dataEditView);
			Work _work = this.workService.getWork(id, false);
			String insUri = null;
			if (_work.getInstances().size() > 0) {
				insUri = _work.getInstances().get(0).get("ins").toString();
			}
			modelAndView
					.addObject("instance", this.getInstancelist(id, insUri));
			// modelAndView.addObject("item", _work);
			modelAndView.addObject("item", this.getItemlist(_work));

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		modelAndView.setViewName("sysmanager/dataInfo/workContentList");
		return modelAndView;
	}

	/**
	 * InstanceList
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public DataEditViewDto getInstancelist(String workUri, String id)
			throws Exception {
		try {
			OutputStream _outStream = commonSparql.getJsonLD4Resource(
					Constant.GRAPH_INSTANCE, id);
			// 规范数据编辑属性
			DataEditViewDto dataEditView = DataManagerDto
					.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
			// 需要关闭的字段
			String[] closeList = { "place", "relatedTo", "instanceOf",
					"identifiedBy", "hasAnnotation", "carrierCategory",
					"label", "description", "temporal", "provider","partOf","code","accessLevel" ,"content","DOI" };
			// GRAPH
			dataEditView.setGraphUri(Constant.GRAPH_INSTANCE);
			// pageUrl
			dataEditView.setPageUrl("workManager");
			// 父URI
			dataEditView.setPdataUri(workUri);
			// 规范数据类型区分：INSTANCE
			dataEditView.setDataType(DataTypeGroup.Work.getGroup());
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
				// 是URI标记
				if ((dataEditView.getEditList().get(i).getOldValue()
						.contains("http") || dataEditView.getEditList().get(i)
						.getOldValue().contains("_:b"))) {
					// 是URI标记
					dataEditView.getEditList().get(i).setIsUri("1");
					OutputStream _thisStream = null;
					JSONObject _thisJson = null;
					String oldROldvalue = dataEditView.getEditList().get(i)
							.getOldEnName();
					String graph = "";
					graph = Constant.GRAPH_BASEINFO;
					if (oldROldvalue.equals("instanceOf")) {
						graph = Constant.GRAPH_WORK;
					}
					if (!StringUtilC.isEmpty(graph)) {
						// 得到中文旧值
						_thisStream = commonSparql
								.getJsonLD4Resource(graph, dataEditView
										.getEditList().get(i).getOldValue());
						_thisJson = JSONObject.fromObject(StringUtilC
								.StreamToString(_thisStream));
						if (_thisJson != null && _thisJson.size() > 0) {
							if (_thisJson.getString("label") != null) {
								String label = _thisJson.getString("label");
								dataEditView.getEditList().get(i)
										.setOldValueCn(label);
							}
						}
					}

				}

			}
			// 列表处理
			dataEditView = EditDtoCommon.listEmptyChange(dataEditView);
			return dataEditView;
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		return null;
	}

	/**
	 * Item编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataEditViewDto getItemlist(Work work) throws Exception {
		try {
			// 规范数据编辑属性
			DataEditViewDto dataEditView = new DataEditViewDto();
			dataEditView.setGraphUri(Constant.GRAPH_ITEM);
			// pageUrl
			dataEditView.setPageUrl("workManager");
			// instanceUri
			dataEditView.setId(work.getInstances().get(0).get("ins"));
			// WrokURI
			dataEditView.setPdataUri(work.getUri());
			// 规范数据类型区分：INSTANCE
			dataEditView.setDataType(DataTypeGroup.Work.getGroup());
			for (Map<String, String> ele : work.getInstances()) {
				DataItemChangeHistoryModel item = new DataItemChangeHistoryModel();
				if (!StringUtilC.isEmpty(ele.get("item"))) {
					// itemUri
					item.setItemUri(ele.get("item"));
					// instanceUri
					item.setInstanceUri(ele.get("ins"));
					// 馆藏地URi
					item.setOldValueUri(ele.get("hbs"));
					// 馆藏地中文
					item.setOldValueCn(ele.get("org"));
					// 馆藏地英文地址
					item.setEngName("bf:heldBy");
					// 索书号
					item.setOldValueCnBook(ele.get("shelf"));
					item.setEngNameBook("bf:shelfMark");
					// 备注 chenss 2017-03-15
					/*
					 * item.setOldValueCnDoi(ele.get("description"));
					 * item.setEngNameDoi("shl:description");
					 */
					// DOI chenss add 2017-03-15
					item.setOldValueCnDoi(ele.get("doi"));
					item.setEngNameDoi("shl:DOI");
					// DOI开关标记 chenss add 2017-03-15
					item.setOldValueAccessLevel(ele.get("accessLevel"));
					item.setEngNameAccessLevel("shl:accessLevel");
					dataEditView.getItemList().add(item);
					// 旧Item常量
					oldItemList.put(item.getItemUri(), item);
				}
			}
			return dataEditView;
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 保存处理（保存Item信息，work,instance保存使用共通）
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doSave", method = { RequestMethod.POST })
	public String doSave(@RequestBody DataEditViewDto dto) throws Exception {
		// 新规URI
		String newItemUri = "http://data.library.sh.cn/jp/resource/item/";
		boolean ifSuccess = true;
		try {
			List<DataItemChangeHistoryModel> list = new ArrayList<DataItemChangeHistoryModel>();
			for (DataItemChangeHistoryModel editDto : dto.getItemList()) {
				// 如果itemUri为空，则为新增
				if (StringUtilC.isEmpty(editDto.getItemUri())) {
					editDto.setItemUri(newItemUri
							+ DataUtilC.getRandomUriValue(16));
					// 新增
					editDto.setDeleteFlg(FWConstants.type_add);
				} else if (oldItemList.containsKey(editDto.getItemUri())) {
					// 编辑
					editDto.setDeleteFlg(FWConstants.type_edit);
					// 移除已包含的项目
					oldItemList.remove(editDto.getItemUri());
				}
				list.add(editDto);
			}
			// 循环被删除的Item
			for (String key : oldItemList.keySet()) {
				// 删除
				oldItemList.get(key).setDeleteFlg(FWConstants.type_delete);
				list.add(oldItemList.get(key));
			}
			// 循环处理最终保存数据库ItemList
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setGraphUri(dto.getGraphUri());
				// 主表ID
				list.get(i).setMainInstanceId(
						StringUtilC.getInteger(dto.getPdataUri()));
				list.get(i).setInstanceUri(dto.getId());
				// 如果是编辑，则判断新值，旧值是否相同
				if (list.get(i).getDeleteFlg().equals(FWConstants.type_edit)) {
					// 如果不相等，则进行保存
					if (!list.get(i).getOldValueUri()
							.equals(list.get(i).getNewValueUri())
							|| !list.get(i).getOldValueCnBook()
									.equals(list.get(i).getNewValueCnBook())
							|| !list.get(i).getOldValueCnDoi()
									.equals(list.get(i).getNewValueCnDoi())
							|| !list.get(i)
									.getOldValueAccessLevel()
									.equals(list.get(i)
											.getNewValueAccessLevel())) {
						itemMapper.insertItemHistory(list.get(i));
					}
				} else {
					// 数据保存处理
					itemMapper.insertItemHistory(list.get(i));
				}
			}

		} catch (Exception e) {
			ifSuccess = false;
		} finally {
			ifSuccess = true;
		}
		jsonResult.put(result, ifSuccess);
		jsonResult.put("id", dto.getId());
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
			// 得到Main对象
			DataChangeHistoryMainModel dto = dataChangeHistoryMapper
					.getHistoryMain(id, null);
			List<DataChangeHistoryListModel> listMolde;
			listMolde = dataChangeHistoryMapper.selectHistoryList(StringUtilC
					.getString(dto.getId()));
			// 插入work信息
			httpSession = hs;
			ifSuccess = insertVt(dto, listMolde);
			if (ifSuccess) {
				listMolde = dataChangeHistoryMapper
						.selectInstanceHistoryList(StringUtilC.getString(dto
								.getId()));
				// 得到Main对象
				DataChangeHistoryMainModel instanceDto = dataChangeHistoryMapper
						.getHistoryMain(null, id);
				// 插入instance信息
				ifSuccess = insertVt(instanceDto, listMolde);
				if (ifSuccess) {
					List<DataItemChangeHistoryModel> itemlistMolde;
					// 得到itemList列表
					itemlistMolde = itemMapper
							.selectItemHistoryList(StringUtilC.getString(dto
									.getId()));
					ifSuccess = insertVtItem(itemlistMolde);

				}
			}
		} catch (Exception e) {
			ifSuccess = false;
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
			itemMapper.deleteItemHistoryList(id);
			// 再删除主表，顺序不能颠倒
			itemMapper.deleteHistoryMain(id);

		} catch (Exception e) {
			ifSuccess = false;
		}
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 插入vt数据库
	 * 
	 * @param dto
	 * @param listMolde
	 * @return
	 */
	private boolean insertVt(DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		boolean ifSuccess = false;
		// 如果只是修改了item，没有修改work，或者instance，也判定为保存正确
		if (listMolde.size() <= 0) {
			ifSuccess = true;
		} else {
			String g = dto.getGraphUri();
			String s = dto.getDataUri();
			for (DataChangeHistoryListModel editDto : listMolde) {
				String p = StringUtilC.getString(editDto.getOldCnNameUri());
				String old_o = StringUtilC.getString(editDto.getOldValue());
				String o = StringUtilC.getString(editDto.getNewValue());
				// 英文名称
				// 旧值不为空，删除旧值
				if (!StringUtilC.isEmpty(old_o)) {
					// 旧值，多值循环先删除
					if (old_o.contains(",")) {
						String[] oldlist = old_o.split(",");
						for (String thisO : oldlist) {
							String val = "";
							// 如果是整形数据，则直接使用
							if (StringUtilC.isInteger(thisO)) {
								val = thisO;
							} else {
								val = EditDtoCommon.getValue(thisO);
							}
							ifSuccess = commonSparql
									.changeRDF(g, s, p, val, "");
						}

					}// 旧值，单值直接删除
					else {
						old_o = EditDtoCommon.getValue(old_o);
						ifSuccess = commonSparql.changeRDF(g, s, p, old_o, "");
					}
				}
				// 新值不为空
				if (!StringUtilC.isEmpty(o)) {
					// 新值，多值循环插入
					if (o.contains(",")) {
						String[] olist = o.split(",");
						// 如果是日期。带整形
						for (String thisO : olist) {
							String val = "";
							// 如果是整形数据，则直接使用
							if (StringUtilC.isInteger(thisO)) {
								val = thisO;
							} else {
								val = EditDtoCommon.getValue(thisO);
							}
							ifSuccess = commonSparql
									.changeRDF(g, s, p, "", val);
						}

					}// 新值，单值直接插入
					else {
						o = EditDtoCommon.getValue(o);
						ifSuccess = commonSparql.changeRDF(g, s, p, "", o);
					}
				}
			}
		}
		if (ifSuccess) {
			itemMapper.updateHistoryMain(dto.getId(),
					StringUtilC.getString(getUser(httpSession).getId()),
					DateUtilC.getNowDate());
		}
		return ifSuccess;
	}

	/**
	 * 将Item发布到vt数据库
	 * 
	 * @param dto
	 * @param listMolde
	 * @return
	 */
	private boolean insertVtItem(List<DataItemChangeHistoryModel> itemlistMolde) {
		boolean ifSuccess = false;
		// 如果是0,则默认插入成功
		if (itemlistMolde.size() == 0) {
			return true;
		}
		for (DataItemChangeHistoryModel editDto : itemlistMolde) {
			String g = editDto.getGraphUri();
			String s = editDto.getItemUri();
			// bf:heldBy
			String p = editDto.getEngName();
			// bf:shelfMark
			String pBook = editDto.getEngNameBook();
			// shl:description
			String pDoi = editDto.getEngNameDoi();
			// 开关
			String pAccessLevel = editDto.getEngNameAccessLevel();
			// 编辑模式：0，新增，1修改，2删除
			String deleteFlg = editDto.getDeleteFlg();
			// 如果是新增
			if (deleteFlg.equals(FWConstants.type_add)) {
				// 数据导入
				ifSuccess = DataNewUtil.InsertItem_Work(editDto);
				// 更新历史表状态为已保存
				itemMapper.updateItemHistoryDeleteFlg(editDto.getId(),
						FWConstants.type_add_save);
			}
			// 如果是编辑
			else if (deleteFlg.equals(FWConstants.type_edit)) {
				// 馆藏地，旧URI
				String oldValue = EditDtoCommon.getValue(editDto
						.getOldValueUri());
				// 馆藏地，新URI
				String newValue = EditDtoCommon.getValue(editDto
						.getNewValueUri());
				// 索书号，旧值
				String oldValueBook = EditDtoCommon.getValue(editDto
						.getOldValueCnBook());
				// 索书号，新值
				String newValueBook = EditDtoCommon.getValue(editDto
						.getNewValueCnBook());
				// DOI，旧值
				String oldValueDoi = EditDtoCommon.getValue(editDto
						.getOldValueCnDoi());
				// DOI，新值
				String newValueDoi = EditDtoCommon.getValue(editDto
						.getNewValueCnDoi());
				// acc，旧值
				String oldValueAccessLevel = EditDtoCommon.getValue(editDto
						.getOldValueAccessLevel());
				// acc，新值
				String newValueAccessLevel = EditDtoCommon.getValue(editDto
						.getNewValueAccessLevel());
				if (!oldValue.equals(newValue)) {
					ifSuccess = commonSparql.changeRDF(g, s, p, oldValue,
							newValue);
				}
				if (!oldValueBook.equals(newValueBook)) {
					ifSuccess = commonSparql.changeRDF(g, s, pBook,
							oldValueBook, newValueBook);
				}
				if (!oldValueDoi.equals(newValueDoi)) {
					ifSuccess = commonSparql.changeRDF(g, s, pDoi, oldValueDoi,
							newValueDoi);
				}
				if (!oldValueAccessLevel.equals(newValueAccessLevel)) {
					ifSuccess = commonSparql.changeRDF(g, s, pAccessLevel,
							oldValueAccessLevel, newValueAccessLevel);
				}
				// 更新历史表状态为已保存
				itemMapper.updateItemHistoryDeleteFlg(editDto.getId(),
						FWConstants.type_edit_save);
				// 其他属性，可后续追加
			}
			// 如果是删除
			else if (deleteFlg.equals(FWConstants.type_delete)) {
				// 根据ItemUri，删除该Item
				ifSuccess = commonSparql.deleteResource(g, s);
				// 更新历史表状态为已保存
				itemMapper.updateItemHistoryDeleteFlg(editDto.getId(),
						FWConstants.type_delete_save);
			}

		}
		return ifSuccess;
	}

	/**
	 * 删除VT库，并存留关系库删除记录
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doDeleteVt", method = { RequestMethod.POST })
	public String doDeleteVt(@Param(value = "workUri") String workUri,
			HttpSession hs) throws Exception {
		boolean ifSuccess = false;
		WorkDelHistoryModel _model = new WorkDelHistoryModel();
		Work _work = this.workService.getWork(workUri, false);
		// 删除Work
		commonSparql.deleteResource(Constant.GRAPH_WORK, workUri);
		if (_work.getInstances() != null && _work.getInstances().size() > 0) {
			// 删除Instance
			commonSparql.deleteResource(Constant.GRAPH_INSTANCE, _work
					.getInstances().get(0).get("ins"));
			for (Map<String, String> item : _work.getInstances()) {
				// 删除Item
				commonSparql.deleteResource(Constant.GRAPH_ITEM,
						item.get("item"));
			}
			_model.setInstanceUri(_work.getInstances().get(0).get("ins"));
			_model.setDeleteUserId(StringUtilC.getString(getUser(hs).getId()));
			_model.setWorkDtitle(_work.getDtitle());
			_model.setCreator(_work.getCreator());
			_model.setWorkUri(workUri);
			//翻译转换-去除特殊字符
			_model=(WorkDelHistoryModel)StringUtilC.convertModel(_model);
			// 删除记录插入关系库
			if (dataChangeHistoryMapper.insertWorkDelHistory(_model) > 0) {
				ifSuccess = true;
			}
		}
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 用户删除文件列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/workDelList", method = RequestMethod.GET)
	public ModelAndView workDelList() throws Exception {
		try {
			List<WorkDelHistoryModel> _List = dataChangeHistoryMapper
					.selectWorkDelHistoryList();
			modelAndView.setViewName("sysmanager/workmanager/workdellist");
			modelAndView.addObject("data", _List);
		} catch (Exception e) {
		}
		return modelAndView;
	}

}
