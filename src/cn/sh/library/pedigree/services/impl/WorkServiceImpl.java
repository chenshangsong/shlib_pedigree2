package cn.sh.library.pedigree.services.impl;

import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.dataImport.DataShumuUtilC;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Item;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.framework.util.PreloadWorkJJUriList;
import cn.sh.library.pedigree.fullContentLink.FullLink;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.DOIChangeAccessLevelMapper;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.ItemSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.utils.WebApiUtils;

/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
@Service
public class WorkServiceImpl extends BaseServiceImpl implements WorkService {

	@Resource
	private WorkSparql workSparql;
	@Resource
	private PlaceSparql placeSparql;
	@Resource
	private ItemSparql itemSparql;
	@Resource
	private PersonSparql personSparql;
	@Resource
	private BaseinfoSparql baseinfoSparql;
	@Resource
	public CommonSparql commonSparql;
	@Resource
	public DOIChangeAccessLevelMapper doiChangeAccessLevelMapper;

	@Override
	public List<Map<String, String>> list(String keyword, Pager pager) {
		try {
			QueryResult<Map<String, Object>> queryResult = this.workSparql
					.getWorksInFree(keyword, pager.getStartIndex(),
							pager.getPageSize());
			pager.calcPageCount(queryResult.getTotalrecord());
			List<Map<String, Object>> _list = queryResult.getResultList();
			for (Map<String, Object> map : _list) {
				String doi = StringUtilC.getString(map.get("doi"));
				map.put("imageFrontPath", "");
				if (!StringUtilC.isEmpty(doi)) {
					map.put("imageFrontPath",
							WebApiUtils.GetImagePathByDoi(doi));
				}
			}
			return RDFUtils.transformListMap(_list);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Map<String, String>> list(WorkSearchBean search, Pager pager) {
		try {
			QueryResult<Map<String, Object>> queryResult = this.workSparql
					.getWorks(search, pager.getStartIndex(),
							pager.getPageSize());
			pager.calcPageCount(queryResult.getTotalrecord());
			List<Map<String, Object>> _list = queryResult.getResultList();
			for (Map<String, Object> map : _list) {
				String doi = StringUtilC.getString(map.get("doi"));
				map.put("imageFrontPath", "");
				if (!StringUtilC.isEmpty(doi)) {
					map.put("imageFrontPath",
							WebApiUtils.GetImagePathByDoi(doi));
				}
			}
			return RDFUtils.transformListMap(_list);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}
	
	@Override
	public Long countItemsByWork(WorkSearchBean search){
		return workSparql.countItemsByWork(search);
	}

	@Override
	public List<Work> listWork4Person(String personUri, Boolean inference) {
		List<Work> resultList = new ArrayList<>();
		inference = inference == null ? false : inference;
		String[] urilist = personUri.split(";");
		for (int i = 0; i < urilist.length; i++) {
			List<Work> resultT = new ArrayList<>();
			List<Map<String, Object>> objList = this.workSparql
					.getWorks4Person(urilist[i], inference);
			List<Map<String, String>> mapList = RDFUtils
					.transformListMap(objList);
			for (Map<String, String> map : mapList) {
				Work work = new Work();
				work.setUri(map.get("work"));
				work.setRole(RDFUtils.getValue(map.get("roles")));
				work.setTitle(map.get("title"));
				work.setDtitle(map.get("dtitle"));
				work.setSubTitle(map.get("subtitle"));
				work.setNote(map.get("note"));
				work.setCreator(map.get("creator"));
				List<Map<String, String>> places = this.placeSparql
						.getPlaces(work.getUri());
				work.setPlaces(places);
				List<Map<String, String>> instances = RDFUtils
						.transformListMap(this.workSparql
								.getInstances4Work(work.getUri()));
				work.setInstances(instances);
				resultT.add(work);
			}
			resultList.addAll(resultT);
		}

		return resultList;
	}

	@Override
	public Work getWork(String uri, boolean ifFullLink) {
		Map<String, String> workInfos = RDFUtils.transform(this.workSparql
				.getWorkInfos(uri));
		if (workInfos == null) {
			return null;
		}
		Work work = new Work();
		// work.setUri(workInfos.get("work"));
		work.setUri(uri);
		work.setTitle(workInfos.get("title"));
		work.setDtitle(workInfos.get("dtitle"));
		work.setSubTitle(workInfos.get("subtitle"));
		work.setNote(workInfos.get("note"));
		work.setCreator(workInfos.get("creator"));
		/**
		 * 堂号
		 */
		work.setTangh(workInfos.get("label"));
		// if (hasFullLink(uri)) {
		// work.setFulllink("true");
		// } else {
		// work.setFulllink("false");
		// }

		List<Map<String, String>> places = this.placeSparql.getPlaces(uri);
		work.setPlaces(places);

		// List<Map<String, String>> creators =
		// RDFUtils.transformListMap(this.workSparql.getCreator(uri));
		// work.setCreators(creators);

		List<Map<String, String>> instances = RDFUtils
				.transformListMap(this.workSparql.getInstances4Work(uri));
		// 全文链接处理
		if (ifFullLink) {
			getFullLink(instances, work);
		}
		work.setInstances(instances);
		List<Map<String, String>> familyRelations = RDFUtils
				.transformListMap(this.personSparql.getFamRels4Work(uri));
		work.setFamilyRelations(familyRelations);
		// 是否为上图胶卷
		if (PreloadWorkJJUriList.IsSTJJWork(uri)) {
			work.setJjflag("1");
		}
		return work;
	}

	@Override
	public List<Map<String, String>> getWorksInTimeline(String beginY,
			String endY, int unit, String name) {
		try {
			return RDFUtils
					.transformListMap((List<Map<String, Object>>) workSparql
							.getWorksInYear(beginY, endY, unit));
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	/**
	 * 获取全文链接
	 * 
	 * @param instances
	 */
	private static void getFullLink(List<Map<String, String>> instances,
			Work work) {
		String shangtu = "http://data.library.sh.cn/entity/organization/brvqlrg8y55v1b5q";
		for (Map<String, String> map : instances) {
			Set<String> keys = map.keySet();// 获取KEY集合
			for (String key : keys) {
				if (key.equals("doi")) {
					// doi
					String doi = StringUtilC.getString(map.get(key));
					// doi
					String hbs = StringUtilC.getString(map.get("hbs"));
					// 索书号
					String callno = StringUtilC.getString(map.get("shelf"));
					// 外网-内网标记
					String accessLevelFlg = StringUtilC.getString(map
							.get("accessLevel"));
					// 如果是上图，则信息全文获取
					if (hbs.equals(shangtu)) {
						if (!StringUtilC.isEmpty(doi)) {
							// 根据家谱DOI，获取家谱封面
							work.setImageFrontPath(WebApiUtils
									.GetImagePathByDoi(doi));
							// 原始数据中，多个DOI分号分隔
							String[] dois = doi.split(";");
							// 原始数据中，多个索书号，#号分隔
							String[] callnos = callno.split("#");
							String value = "";
							for (int i = 0; i < dois.length; i++) {
								String tempcallNo = "";
								// 如果索书号数组长度等于doi数组长度
								if (callnos.length == dois.length) {
									tempcallNo = "（索书号："
											+ StringUtilC.getString(callnos[i])
											+ "）";
								}
								String tempV = dois[i];
								if (tempV.startsWith("STJP")
										&& tempV.length() >= 10) {
									String link = "";
									// 内网访问地址
									if (accessLevelFlg.equals("0")
											&& BaseController
													.getDoiOpenFlg(tempV
															.substring(0, 10))) {
										link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
										link = MessageFormat.format(link,
												"jiapu", tempV.substring(
														0, 10));
										link = FullLink
												.GetOutFullLinkTemp(link);
										//临用
										/*if (tempV.substring(
												0, 10).equals("STJP011280")) {
											link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
											link = MessageFormat.format(link,
													"brvqlrg8y55v1b5q", tempV.substring(
															0, 10));
											link = FullLink
													.GetOutFullLinkTemp(link);
										} else {
											link = getOutFullLink(tempV.substring(
													0, 10));
										}*/
										
									} else {// 外网
										//临用
										link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
										link = MessageFormat.format(link,
												"brvqlrg8y55v1b5q", doi);
										link = FullLink
												.GetOutFullLinkTemp(link);
										if (doi.equals("STJP011280")) {
											link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
											link = MessageFormat.format(link,
													"brvqlrg8y55v1b5q", doi);
											link = FullLink
													.GetOutFullLinkTemp(link);
										} else {
											link = FullLink.GetFulltext(tempV
													.substring(0, 10));
										}

									}
									value += "DOI为" + tempV + tempcallNo + link
											+ "<br>";
								} else {
									value += tempV + "<br>";
								}
							}
							// 截取最后一个分隔符
							if (value.length() > 0) {
								if(value.lastIndexOf("<br>")>0){
									value = value.substring(0,
											value.lastIndexOf("<br>"));
								}
								
							}
							map.put("description", value);
						}
					}
				}
			}
		}
	}

	/**
	 * 获取全文链接:16-08-23
	 * 
	 * @param instances
	 */
	private static void getFullLink_back(List<Map<String, String>> instances) {
		for (Map<String, String> map : instances) {
			Set<String> keys = map.keySet();// 获取KEY集合
			for (String key : keys) {
				if (key.equals("description")) {
					// doi
					String doi = StringUtilC.getString(map.get(key));

					// 索书号
					String callno = StringUtilC.getString(map.get("shelf"));
					if (!StringUtilC.isEmpty(doi)) {
						// 原始数据中，多个DOI分号分隔
						String[] dois = doi.split(";");
						// 原始数据中，多个索书号，#号分隔
						String[] callnos = callno.split("#");
						String value = "";
						for (int i = 0; i < dois.length; i++) {
							String tempcallNo = "";
							// 如果索书号数组长度等于doi数组长度
							if (callnos.length == dois.length) {
								tempcallNo = "（索书号："
										+ StringUtilC.getString(callnos[i])
										+ "）";
							}
							String tempV = dois[i];
							if (tempV.startsWith("STJP")) {
								value += "DOI为"
										+ tempV
										+ tempcallNo
										+ FullLink.GetFulltext(tempV.substring(
												0, 10)) + "<br>";
							} else {
								value += tempV + "<br>";
							}
						}
						// 截取最后一个分隔符
						if (value.length() > 0) {
							value = value.substring(0,
									value.lastIndexOf("<br>"));
						}
						map.put(key, value);
					}
				}
			}
		}
	}

	/**
	 * 获取外网DOI链接
	 * 
	 * @param doi
	 * @return
	 */
	private static String getOutFullLink(String doi) {
		String DOILink = "&nbsp;<a href=\"http://search.library.sh.cn/jiapu/PicBrowse.cgi?stjpID="
				+ doi
				+ "\" target=_blank><img border='0' src='../../res/images/pdf.png' width='20px' height='20px' title='点击查看全文'></a>";
		return DOILink;
	}

	@Override
	public List<Item> getItemListByDoi(String doi) {
		List<Item> list = new ArrayList<Item>();
		List<Map<String, String>> tempMap = itemSparql.getItemByDoi(doi);
		if (tempMap != null && tempMap.size() > 0) {
			for (Map<String, String> map : tempMap) {
				Item _item = new Item();
				_item.setDoi(StringUtilC.getString(map.get("doi")));
				_item.setUri(StringUtilC.getString(map.get("uri")));
				_item.setAccessLevel(StringUtilC.getString(map.get("accessLevel")));
				_item.setHasFullImg(StringUtilC.getString(map.get("hasFullImg")));
				list.add(_item);
			}
		}
		return list;
	}

	@Override
	public boolean updateAccessLevelByItemUri(Item item, String doiSpread, String newValue) {
		boolean flag = false;
		String old_value = EditDtoCommon.getValue(item.getAccessLevel());
		String old_hasFullImg = EditDtoCommon.getValue(item.getHasFullImg());
		newValue = EditDtoCommon.getValue(newValue);
		if (!old_value.equals(newValue)) {
			flag = commonSparql.changeRDF(Constant.GRAPH_ITEM, item.getUri(), "shl:accessLevel", old_value, newValue);
			// 将数据插入关系库
			if(flag){
				doiChangeAccessLevelMapper.insertDOIChangeInfo(item.getUri(), doiSpread, item.getDoi(), old_value.substring(1, old_value.length()-1), newValue.substring(1, newValue.length()-1), "0");
			} else {
				doiChangeAccessLevelMapper.insertDOIChangeInfo(item.getUri(), doiSpread, item.getDoi(), old_value.substring(1, old_value.length()-1), newValue.substring(1, newValue.length()-1), "1");
			}
		}
		if("\"0\"".equals(newValue)){
			//更新hasFullImg属性
			commonSparql.changeRDF(Constant.GRAPH_ITEM, item.getUri(), "shl:hasFullImg", old_hasFullImg, EditDtoCommon.getValue("true"));
			doiChangeAccessLevelMapper.updateHasFullImg(doiSpread, "true");
		}
		if("\"1\"".equals(newValue)){
			if(!StringUtilC.isEmpty(old_hasFullImg)){
				//更新hasFullImg属性
				commonSparql.changeRDF(Constant.GRAPH_ITEM, item.getUri(), "shl:hasFullImg", old_hasFullImg, "");
				doiChangeAccessLevelMapper.updateHasFullImg(doiSpread, "");
			}
		}
		return true;
	}

	@Override
	public boolean updateAccessLevelByDoi(String doi, String flg) {
		if (!StringUtilC.isEmpty(doi)) {
			String[] dois = doi.split(";");
			try {
				for (String thisDoi : dois) {
					List<Item> _list = getItemListByDoi(thisDoi);
					for (Item item : _list) {
						if (!item.getAccessLevel().equals(flg)) {
							updateAccessLevelByItemUri(item, thisDoi, flg);
						}
					}
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

}
