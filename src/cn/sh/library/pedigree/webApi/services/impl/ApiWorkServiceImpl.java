package cn.sh.library.pedigree.webApi.services.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.common.RoleGroup;
import cn.sh.library.pedigree.common.SparqlExecution;
import cn.sh.library.pedigree.common.dataImport.DataShumuUtilC;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Item;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.framework.util.PreloadWorkJJUriList;
import cn.sh.library.pedigree.fullContentLink.FullLink;
import cn.sh.library.pedigree.fullContentLink.FullLink4ESJP;
import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.ItemSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.utils.WebApiUtils;
import cn.sh.library.pedigree.webApi.dto.searchBean.ApiWorkSearchBean;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;
import cn.sh.library.pedigree.webApi.sparql.ApiWorkSparql;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
@Service
public class ApiWorkServiceImpl extends BaseServiceImpl implements ApiWorkService {

	@Resource
	private ApiWorkSparql apiworkSparql;
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

	@Override
	public List<Map<String, String>> list(ApiWorkSearchBean search, Pager pager) {
		try {
			QueryResult<Map<String, Object>> queryResult = this.apiworkSparql.getWorks(search, pager.getStartIndex(),
					pager.getPageSize());
			pager.calcPageCount(queryResult.getTotalrecord());
			List<Map<String, Object>> _list = queryResult.getResultList();
			// 根据DOI获取家谱封面
			for (Map<String, Object> map : _list) {
				String doi = StringUtilC.getString(map.get("doi"));
				// 内网是否有全文
				String hasimg = StringUtilC.getString(map.get("hasimg"));
				map.put("imageFrontPath", "");
				if (!StringUtilC.isEmpty(doi)) {
					map.put("imageFrontPath", WebApiUtils.GetImagePathByDoi(doi));
				}
				String strAccessLevel = StringUtilC.getString(map.get("access"));
				// 默认不开放
				map.put("accessLevel", "-1");
				map.put("fulltextLink", "");
				map.put("fulltextMore", "");
				// 外网全文标识
				if (!StringUtilC.isEmpty(strAccessLevel) && !StringUtilC.isEmpty(doi)) {
					String[] _accs = strAccessLevel.split(";");
					Integer inCount = 0;// 内网数量
					Integer outCount = 0;// 外网数量
					Integer nullCount = 0;// 无全文数量
					Integer nineCount = 0;// 胶卷数量
					for (int i = 0; i < _accs.length; i++) {
						switch (_accs[i]) {
						case "0":
//							inCount++;
							outCount++;
							break;
						case "1":
//							outCount++;
							inCount++;
							break;
						case "-1":
							nullCount++;
							break;
						case "9":
							nineCount++;
							break;
						default:
							break;
						}
					}
					if (inCount > 0) {
//						map.put("accessLevel", "0");
						map.put("accessLevel", "1");
						// 有多个内网开放全文
						if (inCount > 1) {
							map.put("fulltextMore", "1");
						}
						// 有一个内网开放全文
						else if (inCount == 1) {
							// 只有一个开放全文
							map.put("fulltextMore", "0");
							map.put("fulltextLink",
									FullLink.GetFullTextImg(StringUtilC.getString(map.get("accessLevel")), doi));
						}
					} else if (outCount > 0) {// 如果有一个或多个，并且全部外网开放
//						map.put("accessLevel", "1");
						map.put("accessLevel", "0");
						// 如果只查到一个外网开放的数据,并且外网有全文链接
						if (outCount == 1 && "true".equals(hasimg)) {
							// 只有一个开放全文
							map.put("fulltextMore", "0");
							map.put("fulltextLink",
									FullLink.GetFullTextImg(StringUtilC.getString(map.get("accessLevel")), doi));
						} else if (outCount > 1) {
							// 有多个全文
							map.put("fulltextMore", "1");
						}
					} else if (nineCount > 0) {// 胶卷
						map.put("accessLevel", "9");
					}
				}

			}
			return RDFUtils.transformListMap(_list);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Work> listWork4Person(String personUri, Boolean inference) {
		List<Work> resultList = new ArrayList<>();
		inference = inference == null ? false : inference;
		String[] urilist = personUri.split(";");
		for (int i = 0; i < urilist.length; i++) {
			List<Work> resultT = new ArrayList<>();
			List<Map<String, Object>> objList = this.apiworkSparql.getWorks4Person(urilist[i], inference);
			List<Map<String, String>> mapList = RDFUtils.transformListMap(objList);
			for (Map<String, String> map : mapList) {
				Work work = new Work();
				String doi = StringUtilC.getString(map.get("doi"));
				// 封面图片
				if (!StringUtilC.isEmpty(doi)) {
					work.setImageFrontPath(WebApiUtils.GetImagePathByDoi(doi));
				}
				work.setUri(map.get("work"));
				work.setRole(RDFUtils.getValue(map.get("roles")));
				work.setTitle(map.get("title"));
				work.setDtitle(map.get("dtitle"));
				work.setSubTitle(map.get("subtitle"));
				work.setNote(map.get("note"));
				work.setCreator(map.get("creator"));
				List<Map<String, String>> places = this.placeSparql.getPlaces(work.getUri());
				work.setPlaces(places);
				List<Map<String, String>> instances = RDFUtils
						.transformListMap(this.apiworkSparql.getInstances4Work(work.getUri()));
				work.setInstances(instances);
				resultT.add(work);
			}
			resultList.addAll(resultT);
		}

		return resultList;
	}

	@Override
	public Work getWork(String uri, boolean ifFullLink) {
		Map<String, String> workInfos = RDFUtils.transform(this.apiworkSparql.getWorkInfos(uri));
		if (workInfos == null) {
			return null;
		}
		Work work = new Work();
		work.setUri(uri);
		work.setTitle(workInfos.get("title"));
		work.setDtitle(workInfos.get("dtitle"));
		work.setSubTitle(workInfos.get("subtitle"));
		work.setNote(workInfos.get("note"));
		work.setCreator(workInfos.get("creator"));
		// 如果正题名为空，则取副题名
		if (StringUtilC.isEmpty(work.getTitle())) {
			work.setTitle(work.getDtitle());
		}
		/**
		 * 堂号
		 */
		work.setTangh(workInfos.get("label"));

		List<Map<String, String>> places = this.placeSparql.getPlaces(uri);
		work.setPlaces(places);
		List<Map<String, String>> instances = RDFUtils.transformListMap(this.apiworkSparql.getInstances4Work(uri));
		// 全文链接处理
		if (ifFullLink) {
			getFullLink(instances, work);
		}
		work.setInstances(instances);
		List<Map<String, String>> familyRelations = RDFUtils.transformListMap(this.personSparql.getFamRels4Work(uri));
		/*
		 * familyRelations.stream().forEach(item->{
		 * if(!StringUtil.isBlank(item.get("roles"))) {//多值的情况，取第一个 item.put("roles",
		 * item.get("roles").split(";")[0]); } if(!StringUtil.isBlank(item.get("time")))
		 * { item.put("time", item.get("time").split(";")[0]); } });
		 */
		work.setFamilyRelations(familyRelations);
		// 是否为上图胶卷
		if (PreloadWorkJJUriList.IsSTJJWork(uri)) {
			work.setJjflag("1");
		}
		return work;
	}

	/**
	 * 获取全文链接
	 * 
	 * @param instances
	 */
	private static void getFullLink(List<Map<String, String>> instances, Work work) {
		String shangtu = "http://data.library.sh.cn/entity/organization/brvqlrg8y55v1b5q";
		for (Map<String, String> map : instances) {
			Set<String> keys = map.keySet();// 获取KEY集合
			for (String key : keys) {
				if (key.equals("doi")) {
					// doi
					String doi = StringUtilC.getString(map.get(key));
					// 内网的全文标记。
					String hasImg = StringUtilC.getString(map.get("hasimg"));
					// 馆藏地
					String hbs = StringUtilC.getString(map.get("hbs"));
					// 索书号
					String callno = StringUtilC.getString(map.get("shelf"));
					// 外网-内网标记
					String accessLevelFlg = StringUtilC.getString(map.get("accessLevel"));
					// 如果是上图，则信息全文获取
					if (hbs.equals(shangtu)) {
						if (!StringUtilC.isEmpty(doi)) {
							// 根据家谱DOI，获取家谱封面
							work.setImageFrontPath(WebApiUtils.GetImagePathByDoi(doi));
							// 原始数据中，多个DOI分号分隔
							String[] dois = doi.split(";");
							// 原始数据中，多个索书号，#号分隔
							String[] callnos = callno.split("#");
							String value = "";
							for (int i = 0; i < dois.length; i++) {
								String tempcallNo = "";
								// 如果索书号数组长度等于doi数组长度
								if (callnos.length == dois.length) {
									tempcallNo = "（索书号：" + StringUtilC.getString(callnos[i]) + "）";
								}
								value = FullLink4ESJP.GetFullTextImg(accessLevelFlg, dois, value, i, tempcallNo,
										hasImg);
							}
							// 截取最后一个分隔符
							if (value.length() > 0) {
								if (value.lastIndexOf("<br>") > 0) {
									value = value.substring(0, value.lastIndexOf("<br>"));
								}

							}
							map.put("description", value);
							// 放入fullImgPath属性
							if (!StringUtilC.isEmpty(value)) {
								String regEx = "href=\'(.+?)\'";
								Pattern pattern = Pattern.compile(regEx);
								Matcher matcher = pattern.matcher(value);
								if (matcher.find()) {
									String fullImgPath = matcher.group(1);
									map.put("fullImgPath", fullImgPath);
								}
							}
						}
					}
				}
			}
		}
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
				list.add(_item);
			}
		}
		return list;
	}

	@Override
	public boolean updateAccessLevelByItemUri(String uri, String newValue) {
		OutputStream _outStream = commonSparql.getJsonLD4Resource(Constant.GRAPH_ITEM, uri);
		String old_value = DataShumuUtilC.getOldValue(_outStream, "accessLevel");
		newValue = EditDtoCommon.getValue(newValue);
		if (!old_value.equals(newValue)) {
			try {
				commonSparql.changeRDF(Constant.GRAPH_ITEM, uri, "shl:accessLevel", old_value, newValue);
			} catch (Exception e) {
				System.out.println("DOI更新失败：" + e + ";Uri=" + uri);
				return false;
			}
		} else {
			return false;
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
							updateAccessLevelByItemUri(item.getUri(), flg);
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

	@Override
	public List<Map<String, String>> getWorkFacetList(ApiWorkSearchBean search) {
		return RDFUtils.transformListMap(apiworkSparql.getFacetCount(search));
	}

	@Override
	public List<Map<String, String>> getWorkFacetOthersList(ApiWorkSearchBean search) {
		List<Map<String, String>> result = RDFUtils.transformListMap(apiworkSparql.getFacetCountOthers(search));
		if (result != null && result.size() > 0) {
			if (!"0".equals(result.get(0).get("count"))) {
				return result;
			}
		}
		return new ArrayList<Map<String, String>>();
	}

	@Override
	public List<Map<String, String>> getFreeResultList(String free_text, Integer maxCount) {
		return RDFUtils
				.transformListMap((List<Map<String, Object>>) apiworkSparql.getFreeResultList(free_text, maxCount));
	}

	@Override
	public Map getDoiByWorkUri(String workUri) {
		if (StringUtilC.isEmpty(workUri)) {
			return null;
		}
		// TODO Auto-generated method stub
		return apiworkSparql.getDoiByWorkUri(workUri);
	}

	@Override
	public Map getDetailByWorkUri(String workUri) {

		String redisWorkKey = RedisUtils.key_work.concat(workUri);
		Map _mapTemp = null;
		// TODO Auto-generated method stub
		Map _map = apiworkSparql.getDetailByWorkUri(workUri);
		// 先祖名人列表
		List<Map<String, String>> familyRelations = RDFUtils
				.transformListMap(this.personSparql.getFamRels4Work(workUri));
		/*
		 * familyRelations.stream().forEach(item->{
		 * 
		 * if(!StringUtil.isBlank(item.get("roles"))) {//多值的情况，取第一个 item.put("roles",
		 * item.get("roles").split(";")[0]); } if(!StringUtil.isBlank(item.get("time")))
		 * { item.put("time", item.get("time").split(";")[0]); }
		 * 
		 * });
		 */
		//按照名字去重
		 List<Map<String,String>>  dataList = familyRelations .stream().collect(
	                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing( o -> o.get("name") ))),
	                        ArrayList::new)

	       );
		_map.put("familyRelations", dataList);
		FullLink.SetFullLinkHref(_map);

		return _map;
	}

	@Override
	public Map getQxTjInfo(String fname) {
		// TODO Auto-generated method stub
		if (!StringUtil.isBlank(fname)) {
			return apiworkSparql.getQxTjInfo(fname);
		}
		return null;
	}

	@Override
	public Map getPlceTjInfo(String fname) {
		// TODO Auto-generated method stub
		if (!StringUtil.isBlank(fname)) {
			return apiworkSparql.getPlaceTjInfo(fname);
		}
		return null;
	}

	@Override
	public Integer getWorkCountByPlaceAndFname(List<String> place, String fnameUri) {
		// TODO Auto-generated method stub
		return apiworkSparql.getWorkCountByPlaceAndFname(place, fnameUri);
	}

}
