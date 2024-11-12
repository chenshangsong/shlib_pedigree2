package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.PersonUtils;
import cn.sh.library.pedigree.common.dataImport.DataShumuUtilC;
import cn.sh.library.pedigree.common.dataImport.DataUtilC;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.TitleSparql;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.FamilyNameMapper;
import cn.sh.library.pedigree.sysManager.mapper.SDMailMapper;
import cn.sh.library.pedigree.sysManager.mapper.SDPlaceMapper;
import cn.sh.library.pedigree.sysManager.mapper.ShumuImpMapper;
import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.ChaodaiModel;
import cn.sh.library.pedigree.sysManager.model.ChaodaiYearModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.HuPersonModel;
import cn.sh.library.pedigree.sysManager.model.JigouModel;
import cn.sh.library.pedigree.sysManager.model.PersonModel;
import cn.sh.library.pedigree.sysManager.model.PlaceModel;
import cn.sh.library.pedigree.sysManager.model.SDMailModel;
import cn.sh.library.pedigree.sysManager.model.SDPersonModel;
import cn.sh.library.pedigree.sysManager.model.SDPlaceTempModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;
import cn.sh.library.pedigree.utils.HttpsUtil;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.sparql.ApiWorkSparql;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/dataImportManager")
public class DataImportController extends BaseController {

	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public ApiWorkSparql apiWorkSparl;
	@Autowired
	public PersonSparql personSparql;
	@Autowired
	public TitleSparql titleSparql;
	@Autowired
	public PlaceSparql placeSparql;
	@Autowired
	public BaseinfoSparql baseinfoSparql;
	@Resource
	private PersonService personService;
	@Autowired
	private FamilyNameMapper familyNameMapper;
	@Autowired
	private SDPlaceMapper sdPlaceMapper;
	@Autowired
	private SDMailMapper sdMailMapper;
	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	@Autowired
	private ShumuImpMapper shumuImpMapper;
	private static Map<String, String> cdMap = new HashMap<String, String>();
	private static Map<String, String> familyNameMap = new HashMap<String, String>();

	/**
	 * 统计
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tongji_old", method = { RequestMethod.POST })
	public String tongji_old() throws Exception {
		try {

			List<FamilyNameModel> listF = familyNameMapper
					.getTableData(new FamilyNameModel());

			for (FamilyNameModel familyNameModel : listF) {
				SurnameModel model = new SurnameModel();
				/*
				 * Pager pager = new Pager(); pager.setPageCount(0);
				 * pager.setPageSize(90); pager.setPageth(1);
				 * this.personService.getPersonsInHome(
				 * familyNameModel.getSelfUri(), pager);
				 */
				// familyNameMapper.updateSurname(model);
				// 人
				List<Map<String, String>> plist = RDFUtils
						.transformListMap(JSonUtils.stringToObject(personSparql
								.countResState(
										familyNameModel.getFamilyNameS(), 0)));
				// 书目
				List<Map<String, String>> wlist = RDFUtils
						.transformListMap(JSonUtils.stringToObject(personSparql
								.countResState(
										familyNameModel.getFamilyNameS(), 1)));
				if (plist.size() > 0) {
					for (Map<String, String> map : plist) {
						model.setCelebrityCnt("0");
						if (StringUtilC.isEmpty(map.get("pc"))) {
							model.setCelebrityCnt("0");
						} else {
							model.setCelebrityCnt(map.get("pc"));
							// model.setCelebrityCnt(StringUtilC.getString(pager.getRowCount()));
						}
					}

				} else {
					model.setCelebrityCnt("0");
				}
				if (wlist.size() > 0) {
					for (Map<String, String> map : wlist) {
						model.setGenealogyCnt("0");
						if (StringUtilC.isEmpty(map.get("wc"))) {
							model.setGenealogyCnt("0");
						} else {
							model.setGenealogyCnt(map.get("wc"));
						}
						// 更新三个字段，根据URI
						model.setUri(map.get("uri"));
						model.setOriginally(map.get("year"));
					}

				} else {
					model.setGenealogyCnt("0");
				}
				// familyNameMapper.updateSurname(model);
			}
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 统计 chenss 2018-04-24
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tongji", method = { RequestMethod.GET })
	public String tongji() throws Exception {
		try {

			List<FamilyNameModel> listF = familyNameMapper
					.getTableData(new FamilyNameModel());
			int i=0;
			for (FamilyNameModel familyNameModel : listF) {
				SurnameModel model = new SurnameModel();
				List<Map<String, String>> plist = apiWorkSparl
						.tongji(familyNameModel.getSelfUri());
				model.setGenealogyCnt("0");
				if (plist != null && plist.size() > 0) {
					if (StringUtilC.isEmpty(plist.get(0).get("wcount"))) {
						model.setGenealogyCnt("0");
					} else {
						model.setGenealogyCnt(plist.get(0).get("wcount"));
					}
					model.setCelebrityCnt("0");
					if (StringUtilC.isEmpty(plist.get(0).get("pcount"))) {
						model.setCelebrityCnt("0");
					} else {
						model.setCelebrityCnt(plist.get(0).get("pcount"));
					}
					model.setOriginally(plist.get(0).get("minYear"));
					model.setUri(familyNameModel.getSelfUri());
					familyNameMapper.updateSurname(model);
					System.out.println(i++);
				}
				
			}
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	// 导入画面
	@RequestMapping(value = "dataImportMain")
	public ModelAndView dataImportMain(HttpServletResponse response)
			throws Exception {

		// 朝代列表
		List<ChaodaiModel> listP = familyNameMapper
				.getChaodaiData(new ChaodaiModel());

		// 姓氏列表
		List<FamilyNameModel> listP2 = familyNameMapper
				.getTableData(new FamilyNameModel());
		// 姓氏map
		for (FamilyNameModel familyNameModel : listP2) {
			familyNameMap.put(familyNameModel.getSelfUri(),
					familyNameModel.getFamilyNameT());
		}
		for (ChaodaiModel chaodaiModel : listP) {
			cdMap.put(chaodaiModel.getChaodaiT(), chaodaiModel.getSelfUri());
		}
		/*
		 * ArrayList _list = baseinfoSparql.getAllFamilyNames(); for (Object
		 * object : _list) { System.out.println(object); }
		 */
		/*
		 * System.out.println("姓氏：---->" + baseinfoSparql.getFamilyNames("陈"));
		 * System.out .println("堂号：---->" +
		 * baseinfoSparql.getAncestralTemple("陈", 0, 10) .getResultList());
		 * System.out.println("机构：---->" + baseinfoSparql.getOrganization("安徽",
		 * 0, 11).getResultList()); System.out.println("先祖：---->" +
		 * personSparql.getPersons("陈", 0, "先祖", 0, 10).getResultList());
		 * System.out.println("名人：---->" + personSparql.getPersons("陈", 0, "名人",
		 * 0, 10).getResultList()); System.out.println("纂修者：---->" +
		 * personSparql.getPersons("陈", 1, "", 0, 10).getResultList());
		 * System.out.println("谱名：---->" + titleSparql.getTitles("二甲周氏宗譜", 0,
		 * 1).getResultList()); System.out.println("谱籍：---->" +
		 * placeSparql.getPlaces("上海", 0, 1).getResultList());
		 */
		/*
		 * try {
		 * 
		 * OutputStream _outStream = commonSparql.getJsonLD4Resource(
		 * Constant.GRAPH_PERSON,
		 * "http://data.library.sh.cn/jp/entity/person/1ubjymob4xuw7bww");
		 * System
		 * .out.println("先祖名人--------"+StringUtilC.StreamToString(_outStream));
		 * //姓氏 System.out .println("姓氏：-----" + commonSparql
		 * .getJsonLD4Resource( Constant.GRAPH_BASEINFO,
		 * "http://data.library.sh.cn/authority/familyname/68n959cf8zdfkz3v")
		 * .toString());
		 * 
		 * } catch (Exception e) { System.out.println(e); }
		 */
		modelAndView.setViewName("sysmanager/dataInfo/dataImport");
		return modelAndView;
	}

	/**
	 * 胡适驾照URI生成
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importHSFamilyName", method = { RequestMethod.POST })
	public String importHSFamilyName() throws Exception {
		try {
			// 配偶信息列表
			/*
			 * List<HuPersonModel> peiouList = familyNameMapper
			 * .getHushiPeiouTableData(new HuPersonModel());
			 */
			// 胡适人员信息列表
			List<HuPersonModel> modelList = familyNameMapper
					.getHushiTableData(new HuPersonModel());
			for (HuPersonModel huPersonModel : modelList) {
				// 如果一个人的配偶不为空，则去陪偶表中查配偶信息
				if (!StringUtilC.isEmpty(huPersonModel.getPeiou())) {
					List<HuPersonModel> pList = familyNameMapper
							.getHushiPeiouByid(StringUtilC
									.getString(huPersonModel.getId()));
					// 如果查询到配偶数据，则进行塞入
					if (pList != null && pList.size() > 0) {
						huPersonModel.setPeiouList(pList);
					}
				}
			}
			// 男人
			DataUtilC.InsertHSPerson(modelList);

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		jsonResult.put(result, "0");
		// jsonResult.put("data", modelList);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 胡适配偶URI生成
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importHSPeiou", method = { RequestMethod.POST })
	public String importHSPeiou() throws Exception {
		try {
			// 配偶信息列表

			List<HuPersonModel> peiouList = familyNameMapper
					.getHushiPeiouTableData(new HuPersonModel());
			// 女人
			DataUtilC.InsertHSPersonPEIOU(peiouList);

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e.getMessage());
		}
		jsonResult.put(result, "0");
		// jsonResult.put("data", modelList);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 编辑页面
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importFamilyName", method = { RequestMethod.POST })
	public String importFamilyName() throws Exception {

		try {

			/*
			 * List<FamilyNameModel> modelList = familyNameMapper
			 * .getTableData(new FamilyNameModel()); for (int i = 0; i <
			 * modelList.size(); i++) { modelList.get(i).setSelfUri(
			 * DataImportUtil.getRandomUriValue(16));
			 * familyNameMapper.updateFamilyNameById(modelList.get(i)); }
			 */
			// 姓氏描述信息导入
			List<FamilyNameModel> modelList2 = familyNameMapper
					.getTableData(new FamilyNameModel());
			for (FamilyNameModel shumuModel : modelList2) {
				if (!StringUtilC.isEmpty(shumuModel.getDescription())) {
					boolean m = commonSparql
							.changeRDF(Constant.GRAPH_BASEINFO, shumuModel
									.getSelfUri(), "shl:description", "",
									EditDtoCommon.getValue(shumuModel
											.getDescription()));
					if (m) {
						System.out.println("已更新：" + shumuModel.getSelfUri());

					} else {
						System.out.println("失败：" + shumuModel.getSelfUri());
					}
				}

			}
			// DataUtilC.InsertFamilyName(modelList2);

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

	/**
	 * 先祖1
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importPerson1", method = { RequestMethod.POST })
	public String importPerson1() throws Exception {
		try {

			List<FamilyNameModel> listF = familyNameMapper
					.getTableData(new FamilyNameModel());
			List<FamilyNameModel> listP = familyNameMapper
					.getXZData(new FamilyNameModel());
			/*
			 * for (int i = 0; i < listP.size(); i++) {
			 * listP.get(i).setSelfUri(DataImportUtil.getRandomUriValue(16)); //
			 * 更新URI familyNameMapper.updatePersonById(listP.get(i)); }
			 */
			// 数据导入测试
			DataUtilC.InsertPerson(listP, listF, "xianzu");
			/*
			 * DataImportUtil.PersonTest(); System.out.println("第" +
			 * commonSparql.getResInfos(
			 * "http://gen.library.sh.cn/graph/person",
			 * "http://data.library.sh.cn/entity/person/0126"));
			 */

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 名人
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importPersonMR", method = { RequestMethod.POST })
	public String importPersonMR() throws Exception {
		try {
			List<FamilyNameModel> listF = familyNameMapper
					.getTableData(new FamilyNameModel());
			List<FamilyNameModel> listP = familyNameMapper
					.getMRData(new FamilyNameModel());
			/*
			 * for (int i = 0; i < listP.size(); i++) {
			 * listP.get(i).setSelfUri(DataImportUtil.getRandomUriValue(16)); //
			 * 更新URI familyNameMapper.updatePerson2ById(listP.get(i)); }
			 */
			// 数据导入测试
			DataUtilC.InsertPerson(listP, listF, "mingren");
			// 数据导入测试
			// DataImportUtil.InsertPerson(listP, listF);
			/*
			 * DataImportUtil.PersonTest(); System.out.println("第" +
			 * commonSparql.getResInfos(
			 * "http://gen.library.sh.cn/graph/person",
			 * "http://data.library.sh.cn/entity/person/0126"));
			 */

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 纂修者
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importPersonZXZ", method = { RequestMethod.POST })
	public String importPersonZXZ() throws Exception {
		try {
			List<FamilyNameModel> listF = familyNameMapper
					.getTableData(new FamilyNameModel());
			List<FamilyNameModel> listP = familyNameMapper
					.getZXZData(new FamilyNameModel());
			/*
			 * for (int i = 0; i < listP.size(); i++) {
			 * listP.get(i).setSelfUri(DataImportUtil.getRandomUriValue(16)); //
			 * 更新URI familyNameMapper.updatePersonZXZById(listP.get(i)); }
			 */
			// 数据导入测试
			DataUtilC.InsertPerson(listP, listF, "zxz");

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 堂号
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importTanghao", method = { RequestMethod.POST })
	public String importTanghao() throws Exception {
		try {
			/*
			 * List<FamilyNameModel> listP = familyNameMapper
			 * .getTangHaoData(new FamilyNameModel()); for (int i = 0; i <
			 * listP.size(); i++) {
			 * listP.get(i).setSelfUri(DataImportUtil.getRandomUriValue(16)); //
			 * 更新URI familyNameMapper.updateTanghaoById(listP.get(i)); }
			 */
			// 数据导入测试
			List<FamilyNameModel> modelList2 = familyNameMapper
					.getTangHaoData(new FamilyNameModel());
			DataUtilC.InsertTanghao(modelList2);

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 谱名
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importPuming", method = { RequestMethod.POST })
	public String importPuming() throws Exception {
		/*
		 * String json =
		 * "[{\"graph\":\"http://gen.library.sh.cn/graph/title\",\"uri\":\"http://data.library.sh.cn/authority/title/0124\",\"type\":\"bf:Title\",\"properties\":[{\"uri\":\"bf:titleValue\",\"type\":\"literal\",\"repeatable\":\"true\",\"objects\":[{\"value\":\"陈氏族谱\",\"language\":\"chs\"},{\"value\":\"陳氏族譜\",\"language\":\"cht\"}]}]}]"
		 * ; JSONArray _array = JSONArray.fromObject(json);
		 * DataImportUtil.InsertV(_array);
		 */
		/*
		 * System.out.println(placeSparql.getFullLocations("浙江", ""));
		 * System.out.println(placeSparql.getFullLocations("上海", "普陀"));
		 * System.out.println(placeSparql.getFullLocations("", "美国"));
		 * System.out.println(placeSparql.getFullLocations("", "全国"));
		 */
		try {
			List<FamilyNameModel> modelList2 = familyNameMapper
					.getPumingData(new FamilyNameModel());

			/*
			 * for (int i = 0; i < modelList2.size(); i++) {
			 * modelList2.get(i).setSelfUri
			 * (DataImportUtil.getRandomUriValue(16));
			 * familyNameMapper.updatePumingById(modelList2.get(i)); }
			 */
			DataUtilC.InsertPuming(modelList2);
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 盛档SDMail
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importSDMail", method = { RequestMethod.POST })
	public String importSDMail() throws Exception {

		try {

			List<SDMailModel> listSdMail = sdMailMapper.getListSDMail();
			for (SDMailModel sdMail : listSdMail) {

				System.out.println("index：" + sdMail.getId());

				// 用author匹配SDPerson.originalWord,将SDPerson.selfUri设到authorUri中。（author为多值时用“、”分隔）
				if (!StringUtilC.isEmpty(sdMail.getAuthor())) {
					String[] authors = sdMail.getAuthor().split("、");
					String authorsUrl = "";
					for (String author : authors) {
						SDPersonModel sdPerson = sdMailMapper
								.getSDPerson(author);
						if (sdPerson == null) {
							authorsUrl += "、";
						} else {
							authorsUrl += sdPerson.getSelfUri() + "、";
						}
					}
					if (!StringUtilC.isEmpty(authorsUrl)) {
						authorsUrl = authorsUrl.substring(0,
								authorsUrl.length() - 1);
					}
					sdMail.setAuthorUri(authorsUrl);
				}

				// 用sender匹配SDPerson.originalWord,将SDPerson.selfUri设到senderUri中。（sender为多值时用“、”分隔）
				if (!StringUtilC.isEmpty(sdMail.getSender())) {
					String[] senders = sdMail.getSender().split("、");
					String sendersUrl = "";
					for (String sender : senders) {
						SDPersonModel sdPerson = sdMailMapper
								.getSDPerson(sender);
						if (sdPerson == null) {
							sendersUrl += "、";
						} else {
							sendersUrl += sdPerson.getSelfUri() + "、";
						}
					}
					if (!StringUtilC.isEmpty(sendersUrl)) {
						sendersUrl = sendersUrl.substring(0,
								sendersUrl.length() - 1);
					}
					sdMail.setSendUri(sendersUrl);
				}

				// 用receiver匹配SDPerson.originalWord,将SDPerson.selfUri设到recieverUri中。（receiver为多值时用“、”分隔）
				if (!StringUtilC.isEmpty(sdMail.getReceiver())) {
					String[] receivers = sdMail.getReceiver().split("、");
					String receiversUrl = "";
					for (String receiver : receivers) {
						SDPersonModel sdPerson = sdMailMapper
								.getSDPerson(receiver);
						if (sdPerson == null) {
							receiversUrl += "、";
						} else {
							receiversUrl += sdPerson.getSelfUri() + "、";
						}
					}
					if (!StringUtilC.isEmpty(receiversUrl)) {
						receiversUrl = receiversUrl.substring(0,
								receiversUrl.length() - 1);
					}
					sdMail.setReceiverUri(receiversUrl);
				}

				// 用place匹配SDPlaceTemp.originalPlace,将SDPlaceTemp.selfUri设到placeUri中。
				if (!StringUtilC.isEmpty(sdMail.getPlace())) {
					String[] places = sdMail.getPlace().split("、");
					String placesUrl = "";
					for (String place : places) {
						SDPlaceTempModel sdPlaceTemp = sdMailMapper
								.getSDPlaceTemp(place);
						if (sdPlaceTemp == null) {
							placesUrl += "、";
						} else {
							placesUrl += sdPlaceTemp.getSelfUri() + "、";
						}
					}
					if (!StringUtilC.isEmpty(placesUrl)) {
						placesUrl = placesUrl.substring(0,
								placesUrl.length() - 1);
					}
					sdMail.setPlaceUri(placesUrl);
				}

				// 匹配subject
				// 将“;”分隔的各个词拆分
				if (!StringUtilC.isEmpty(sdMail.getSubject())) {
					String[] subjects = sdMail.getSubject().split(";");
					String subjectsUrl = "";
					for (String subject : subjects) {
						// 循环拆分后的单词，匹配SDPlaceTemp.originalPlace，获取SDPlaceTemp.selfUri
						// 加上 “1@”前缀，替换原值。
						SDPlaceTempModel sdPlaceTemp = sdMailMapper
								.getSDPlaceTemp(subject);
						if (sdPlaceTemp != null) {
							subjectsUrl += "1@" + sdPlaceTemp.getSelfUri()
									+ ";";
						} else {
							// 循环拆分后的单词，匹配SDPerson.originalWord,获取SDPerson.selfUri
							// 加上 “2@”前缀，替换原值。。
							SDPersonModel sdPerson = sdMailMapper
									.getSDPerson(subject);
							if (sdPerson != null) {
								subjectsUrl += "2@" + sdPerson.getSelfUri()
										+ ";";
							} else {
								subjectsUrl += ";";
							}
						}
					}
					if (!StringUtilC.isEmpty(subjectsUrl)) {
						subjectsUrl = subjectsUrl.substring(0,
								subjectsUrl.length() - 1);
					}
					sdMail.setSubjectUri(subjectsUrl);
				}

				// 如果sender和receiver都匹配到了Uri，将matchSR设为1
				if (!StringUtilC.isEmpty(sdMail.getSendUri())
						&& !StringUtilC.isEmpty(sdMail.getReceiverUri())) {
					sdMail.setMatchSR("1");
				}

				// 生成SelfUri
				sdMail.setSelfUri("http://data.library.sh.cn/sd/entity/mail/"
						+ DataUtilC.getRandomUriValue(16));

				// 生成instanceUri
				/*
				 * sdMail.setInstanceUri(
				 * "http://data.library.sh.cn/sd/resource/instance/" +
				 * DataUtilC.getRandomUriValue(16));
				 */

				// 更新sdMail
				sdMailMapper.updateSDMail(sdMail);
			}

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 盛档SDPerson
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importSDPerson", method = { RequestMethod.POST })
	public String importSDPerson() throws Exception {

		try {
			List<FamilyNameModel> modelList2 = familyNameMapper
					.getSDPersonData(new FamilyNameModel());
			for (int i = 0; i < modelList2.size(); i++) {

				modelList2.get(i).setSelfUri(
						"http://data.library.sh.cn/sd/entity/person/"
								+ DataUtilC.getRandomUriValue(16));

				modelList2.get(i).setSelfUri(DataUtilC.getRandomUriValue(16));

				modelList2.get(i).setSelfUri(DataUtilC.getRandomUriValue(16));

				familyNameMapper.updateSDPersonData(modelList2.get(i));
			}

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 盛档SDOrganization
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importSDOrganization", method = { RequestMethod.POST })
	public String importSDOrganization() throws Exception {

		try {
			List<FamilyNameModel> modelList2 = familyNameMapper
					.getSDOrganization(new FamilyNameModel());
			for (int i = 0; i < modelList2.size(); i++) {
				modelList2.get(i).setSelfUri(
						"http://data.library.sh.cn/entity/organization/"
								+ DataUtilC.getRandomUriValue(16));
				familyNameMapper.updateSDOrganization(modelList2.get(i));
			}

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 盛档SDPlace
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importSDPlace", method = { RequestMethod.POST })
	public String importSDPlace() throws Exception {
		try {
			// 读取临时表（SDPlaceTemp）中 generateUri = 1 的数据，如下处理。
			List<SDPlaceTempModel> listSDPlaceTemp = sdPlaceMapper
					.getListSDPlaceTemp();
			// 循环处理临时表数据
			for (SDPlaceTempModel temp : listSDPlaceTemp) {
				if (!StringUtilC.isEmpty(temp.getPoint()))
					commonSparql.changeRDF(Constant.GRAPH_PLACE,
							temp.getSelfUri(), "owl:sameAs",
							EditDtoCommon.getValue(temp.getOldpoint()),
							EditDtoCommon.getValue(temp.getPoint()));
				this.changeValueMoreValue(Constant.GRAPH_PLACE,
						temp.getSelfUri(), "label", "bf:label", new String[] {
								temp.getLabelS() + "@chs",
								temp.getLabelT() + "@cht" });
				this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
						"province", "shl:province",
						EditDtoCommon.getValue(temp.getShengS()));
				this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
						"city", "shl:city",
						EditDtoCommon.getValue(temp.getShiS()));
				this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
						"county", "shl:county",
						EditDtoCommon.getValue(temp.getXianS()));
				/*
				 * this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
				 * "sameAs", "owl:sameAs",
				 * EditDtoCommon.getValue(temp.getPoint()));
				 */

				// importChinaSDPlace(temp);
			}

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	private void importChinaSDPlace(SDPlaceTempModel temp) {
		ArrayList list;
		String shen = temp.getShengS();
		if (StringUtilC.isEmpty(temp.getShiS())) {
			list = placeSparql.getFullLocations(shen, "");
		} else {
			list = placeSparql.getFullLocations(shen, temp.getLabelS());
		}
		Map<String, String> map = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			map = (Map) list.get(0);
			// 更新 SDPlaceTemp
			sdPlaceMapper.updateSDPlaceTemp(temp.getID(),
					StringUtilC.getString(map.get("s")));
		}
	}

	/**
	 * 机构
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importJigou", method = { RequestMethod.POST })
	public String importJigou() throws Exception {
		try {
			/*
			 * String json =
			 * "[{\"graph\":\"http://gen.library.sh.cn/graph/baseinfo\",\"uri\":\"http://data.library.sh.cn/entity/organization/123\",\"type\":\"http://www.library.sh.cn/ontology/Organization\",\"properties\":[{\"uri\":\"shl:region\",\"type\":\"resource\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://data.library.sh.cn/entity/place/cpq7jkevh6dp275t\"}},{\"uri\":\"bf:lable\",\"type\":\"literal\",\"repeatable\":\"true\",\"objects\":[{\"value\":\"安徽省图书馆\",\"language\":\"chs\"},{\"value\":\"安徽省圖書館\",\"language\":\"cht\"}]},{\"uri\":\"shl:abbreviateName\",\"type\":\"literal\",\"repeatable\":\"true\",\"objects\":[{\"value\":\"安徽图\",\"language\":\"chs\"},{\"value\":\"安徽圖\",\"language\":\"cht\"}]}]}]"
			 * ; JSONArray _array = JSONArray.fromObject(json);
			 * DataImportUtil.InsertV(_array);
			 */
			List<JigouModel> listP = familyNameMapper
					.getJigouData(new JigouModel());
			// 数据导入测试
			DataUtilC.InsertJiGou(listP);
			/*
			 * for (int i = 0; i < listP.size(); i++) {
			 * listP.get(i).setSelfUri(DataImportUtil.getRandomUriValue(16));
			 * familyNameMapper.updateJigouById(listP.get(i)); }
			 */

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 书目
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importShumu", method = { RequestMethod.POST })
	public String importShumu() throws Exception {
		try {
			List<ShumuModel> listP = familyNameMapper
					.getShumuData(new ShumuModel());
			// ////////////////////////////////////测试end
			// 第一步,ID做成
			// step1(listP);
			// 第二步书目据导入
			// step2();
			// 第4步 题名/变异题名导入：title/bf:Work
			// step4();
			// 第5步 责任者时代更新：shl:Person
			// step5();
			// 第6步 责任者导入：bf:Work
			// step6(listP);
			// 第7步，责任方式更新：shl:Person
			// step7();
			// 第8步 其他责任者时代更新：shl:Person
			// step8();
			// 第9步 其他责任者导入：bf:Work
			// step9(listP);
			// 第10步，其他责任方式更新：shl:Person
			// step10();
			// 第11步：谱载体内容
			// step11(listP);
			// 第12步奏：堂号插入
			// step12(listP);
			// instance信息插入
			// step13(listP);
			// instance信息插入 补充版本年代朝代URI
			// step13_2(listP);
			// Item信息插入
			// step14(listP);
			// 先祖信息：shl:Person
			// step15(listP);
			// 始迁祖信息：shl:Person
			// step16(listP);
			// 名人信息：shl:Person
			// step17(listP);
			// relatedWork：shl:Person(后补)
			// step18(listP);
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 1书目数据ID做成
	 */
	private void step1(List<ShumuModel> listP) {
		String firstS = "http://data.library.sh.cn/jp/resource/work/";
		String firstInstanceS = "http://data.library.sh.cn/jp/resource/instance/";
		String firstItemS = "http://data.library.sh.cn/jp/resource/item/";
		for (int i = 0; i < listP.size(); i++) {
			// workUri
			listP.get(i).setSelfUri(firstS + DataUtilC.getRandomUriValue(16));
			// InstanceUri
			listP.get(i).setInstanceUri(
					firstInstanceS + DataUtilC.getRandomUriValue(16));
			// ItemUri
			listP.get(i).setItemUri(
					firstItemS + DataUtilC.getRandomUriValue(16));
			familyNameMapper.updateShumuById(listP.get(i));
		}
	}

	/**
	 * 2书目数据导入
	 */
	private void step2() {

		List<ShumuModel> listP = familyNameMapper
				.getShumuData(new ShumuModel());
		DataShumuUtilC.InsertShumu(listP);
	}

	/**
	 * 题名，变异题名导入
	 */
	private void step4() {
		List<ShumuModel> zhengshumingList = shumuImpMapper
				.getZhengshumingData(new ShumuModel());
		// 变异书名List
		List<ShumuModel> bianyishumingList = shumuImpMapper
				.getBianyishumingData(new ShumuModel());
		// 第四步 正书名导入
		// DataImportUtil.insertTest(null);
		DataShumuUtilC.InsertZhengshuming(zhengshumingList);
		// 变异题名导入
		DataShumuUtilC.InsertBianyishuming(bianyishumingList);
	}

	/**
	 * 第5步 责任者时代更新(Shl:Person)
	 */
	private void step5() {
		List<ShumuModel> zrzsdPersonList = shumuImpMapper
				.getZrzsdPersonList(new ShumuModel());
		String g = Constant.GRAPH_PERSON;
		// String p = "shl:temporalValue";
		String p = "shl:temporal";
		String old_value = "";
		/**
		 * 后添加
		 */

		for (ShumuModel shumuModel : zrzsdPersonList) {
			OutputStream _outStream = commonSparql.getJsonLD4Resource(
					Constant.GRAPH_PERSON, shumuModel.getZxzUri());
			// old_value = getOldValue(_outStream, "temporalValue");
			/*
			 * if (!StringUtilC.isEmpty(old_value)) {
			 * System.out.println(old_value); commonSparql.changeRDF(g,
			 * shumuModel.getZxzUri(), p, old_value, ""); }
			 */
			/*
			 * commonSparql .changeRDF(g, shumuModel.getZxzUri(), p, "",
			 * EditDtoCommon .getValue(shumuModel.getZherenzheshidai()));
			 */
			commonSparql.changeRDF(g, shumuModel.getZxzUri(), p, "",
					EditDtoCommon.getValue(shumuModel.getRefUri()));

		}

	}

	/**
	 * 责任者插入
	 * 
	 * @param listP
	 */
	private void step6(List<ShumuModel> listP) {
		// 书目总条目
		List<ShumuModel> lastList = new ArrayList<ShumuModel>();
		for (ShumuModel shumuModel : listP) {
			List<ShumuModel> tempzrzList = new ArrayList<ShumuModel>();
			tempzrzList = shumuImpMapper
					.getZrzPersonListByIdentifier(shumuModel.getIdentifier());
			if (tempzrzList != null && tempzrzList.size() > 0) {
				// 责任者List
				shumuModel.setOthersList(tempzrzList);
			}
			lastList.add(shumuModel);
		}
		DataShumuUtilC.InsertZherenzhe(lastList, "1");
	}

	/**
	 * // 责任者，责任方式的插入：shl:Person
	 */
	private void step7() {
		// 责任方式的插入
		List<ShumuModel> zrfsList = shumuImpMapper
				.getCategoryListByType("role");
		String g = Constant.GRAPH_PERSON;
		String p = "bf:role";
		String old_value = "";
		for (ShumuModel shumuModel : zrfsList) {
			/*
			 * 第二次插入，可调用此方法 OutputStream _outStream =
			 * commonSparql.getJsonLD4Resource( Constant.GRAPH_PERSON,
			 * shumuModel.getZxzUri()); old_value = getOldValue(_outStream,
			 * "role");
			 */
			boolean bf = commonSparql.changeRDF(g, shumuModel.getZxzUri(), p,
					"", EditDtoCommon.getValue(shumuModel.getRefUri()));
		}
	}

	/**
	 * 第8步 其他责任者时代更新(Shl:Person)
	 */
	private void step8() {
		List<ShumuModel> zrzsdPersonList = shumuImpMapper
				.getQtZrzsdPersonList(new ShumuModel());
		String g = Constant.GRAPH_PERSON;
		// String p = "shl:temporalValue";
		String p = "shl:temporal";
		String old_value = "";
		/**
		 * 后添加
		 */

		for (ShumuModel shumuModel : zrzsdPersonList) {
			/*
			 * OutputStream _outStream = commonSparql.getJsonLD4Resource(
			 * Constant.GRAPH_PERSON, shumuModel.getZxzUri());
			 */
			// old_value = getOldValue(_outStream, "temporalValue");
			/*
			 * if (!StringUtilC.isEmpty(old_value)) {
			 * System.out.println(old_value); commonSparql.changeRDF(g,
			 * shumuModel.getZxzUri(), p, old_value, ""); }
			 */
			/*
			 * commonSparql .changeRDF(g, shumuModel.getZxzUri(), p, "",
			 * EditDtoCommon .getValue(shumuModel.getQitazherenzheshidai()));
			 */
			commonSparql.changeRDF(g, shumuModel.getZxzUri(), p, "",
					EditDtoCommon.getValue(shumuModel.getRefUri()));

		}
		p = "shl:temporal";

	}

	/**
	 * 其他责任者插入
	 * 
	 * @param listP
	 */
	private void step9(List<ShumuModel> listP) {
		// 书目总条目
		List<ShumuModel> lastList = new ArrayList<ShumuModel>();
		for (ShumuModel shumuModel : listP) {
			List<ShumuModel> tempzrzList = new ArrayList<ShumuModel>();
			tempzrzList = shumuImpMapper
					.getQtZrzPersonListByIdentifier(shumuModel.getIdentifier());
			if (tempzrzList != null && tempzrzList.size() > 0) {
				// 责任者List
				shumuModel.setOthersList(tempzrzList);
			}
			lastList.add(shumuModel);
		}
		DataShumuUtilC.InsertZherenzhe(lastList, "2");
	}

	/**
	 * // 其他责任者，责任方式的插入：shl:Person
	 */
	private void step10() {
		// 责任方式的插入
		List<ShumuModel> zrfsList = shumuImpMapper
				.getQtCategoryListByType("role");
		String g = Constant.GRAPH_PERSON;
		String p = "bf:role";
		String old_value = "";
		for (ShumuModel shumuModel : zrfsList) {
			/*
			 * 第二次插入，可调用此方法 OutputStream _outStream =
			 * commonSparql.getJsonLD4Resource( Constant.GRAPH_PERSON,
			 * shumuModel.getZxzUri()); old_value = getOldValue(_outStream,
			 * "role");
			 */
			boolean bf = commonSparql.changeRDF(g, shumuModel.getZxzUri(), p,
					"", EditDtoCommon.getValue(shumuModel.getRefUri()));
		}
	}

	/**
	 * 谱载内容bf:Work
	 * 
	 * @param listP
	 */
	private void step11(List<ShumuModel> listP) {
		DataShumuUtilC.InsertPuzaiNeirong(listP);
	}

	/**
	 * 第12步堂号插入bf:Work
	 */
	private void step12(List<ShumuModel> listP) {
		// 书目总条目
		List<ShumuModel> lastList = new ArrayList<ShumuModel>();
		for (ShumuModel shumuModel : listP) {
			List<ShumuModel> tempzrzList = new ArrayList<ShumuModel>();
			tempzrzList = shumuImpMapper.getTanghaoListIdentifier(shumuModel
					.getIdentifier());
			if (tempzrzList != null && tempzrzList.size() > 0) {
				// 责任者List
				shumuModel.setOthersList(tempzrzList);
			}
			lastList.add(shumuModel);
		}
		DataShumuUtilC.InsertTanghao(lastList);
	}

	/**
	 * 第13步Instance信息插入bf:Instance
	 */
	private void step13(List<ShumuModel> listP) {
		DataShumuUtilC.InsertInstance(listP);
	}

	/**
	 * 第13步Instance信息插入shl:temporal：后补，如果新导入，则该方法可省去
	 */
	private void step13_2(List<ShumuModel> listP) {
		String g = Constant.GRAPH_INSTANCE;
		String p = "shl:temporal";
		for (ShumuModel shumuModel : listP) {
			if (!StringUtilC.isEmpty(shumuModel.getRefChaodaiUri()))
				commonSparql.changeRDF(g, shumuModel.getInstanceUri(), p, "",
						EditDtoCommon.getValue(shumuModel.getRefChaodaiUri()));
		}

	}

	/**
	 * 第14步Item信息插入bf:Item
	 */
	private void step14(List<ShumuModel> listP) {
		// 书目总条目
		List<ShumuModel> lastList = new ArrayList<ShumuModel>();
		for (ShumuModel shumuModel : listP) {
			List<ShumuModel> tempzrzList = new ArrayList<ShumuModel>();
			tempzrzList = shumuImpMapper.getItemListIdentifier(shumuModel
					.getIdentifier());
			if (tempzrzList != null && tempzrzList.size() > 0) {
				// 责任者List
				shumuModel.setOthersList(tempzrzList);
			}
			lastList.add(shumuModel);
		}
		DataShumuUtilC.InsertItem(lastList);
	}

	/**
	 * 第15步先祖信息更新shl:Person
	 */
	private void step15(List<ShumuModel> listP) {
		String g = Constant.GRAPH_PERSON;
		for (ShumuModel shumuModel : listP) {
			// 如果始祖数据不为空
			if (!StringUtilC.isEmpty(shumuModel.getShizhu())) {
				// 可能是多个始祖姓名。如：梁御史中丞昭（字晦甫）*謙，字潛翁，號益齋，行文二
				String tempshiZuNameList = shumuModel.getShizhu();
				// 始祖时代
				String tempshiZushidaiList = shumuModel.getShizhushidai();
				List<FamilyNameModel> szList = new ArrayList<FamilyNameModel>();
				FamilyNameModel _model = new FamilyNameModel();
				_model.setSelfID(shumuModel.getRefIdentifier());
				// 使用书目标识符匹配先祖标识符
				szList = familyNameMapper.getXZData(_model);
				// 如果标识符匹配到人员信息
				if (szList != null && szList.size() > 0) {
					for (FamilyNameModel fModel : szList) {
						// 得到一条人员信息，已分割好
						PersonModel pModel = PersonUtils.getXzName(
								tempshiZuNameList, tempshiZushidaiList,
								fModel.getXingshiT());
						for (PersonModel pModel2 : pModel.getPersonList()) {
							// 如果匹配到信息，则进行更新
							if (pModel2.getAllName().equals(
									fModel.getFamilyNameT())) {
								String newroleOfFamilyUri = "http://data.library.sh.cn/jp/vocab/ancestor/shi-zu";
								String oldroleOfFamilyUri = "http://data.library.sh.cn/jp/vocab/ancestor/xian-zu";

								String dataUri = fModel.getSelfUri();
								// 更新始祖URI：
								System.out.println("始祖姓名："
										+ pModel2.getAllName() + "---始祖URI:"
										+ newroleOfFamilyUri);
								commonSparql.changeRDF(g, dataUri,
										"shl:roleOfFamily", EditDtoCommon
												.getValue(oldroleOfFamilyUri),
										EditDtoCommon
												.getValue(newroleOfFamilyUri));
								System.out.println("URI" + fModel.getSelfUri());
								// 更新始祖URI：
								if (!StringUtilC.isEmpty(pModel2.getZhi())) {
									System.out.println("字：" + pModel2.getZhi());
									commonSparql.changeRDF(g, dataUri,
											"shl:courtesyName", "",
											EditDtoCommon.getValue(pModel2
													.getZhi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHao())) {
									System.out.println("号：" + pModel2.getHao());
									commonSparql
											.changeRDF(g, dataUri,
													"shl:pseudonym", "",
													EditDtoCommon
															.getValue(pModel2
																	.getHao()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHang())) {
									System.out
											.println("行：" + pModel2.getHang());
									commonSparql.changeRDF(g, dataUri,
											"shl:orderOfSeniority", "",
											EditDtoCommon.getValue(pModel2
													.getHang()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShi())) {
									System.out
											.println("谥号：" + pModel2.getShi());
									commonSparql.changeRDF(g, dataUri,
											"shl:posthumousName", "",
											EditDtoCommon.getValue(pModel2
													.getShi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShidai())) {
									System.out.println("始祖朝代："
											+ pModel2.getShidai());
									commonSparql.changeRDF(g, dataUri,
											"shl:temporalValue", "",
											EditDtoCommon.getValue(pModel2
													.getShidai()));
									String caodaiUri = cdMap.get(pModel2
											.getShidai());
									if (!StringUtilC.isEmpty(caodaiUri)) {
										System.out.println("始祖朝代URI"
												+ caodaiUri);
										commonSparql.changeRDF(g, dataUri,
												"shl:temporal", "",
												EditDtoCommon
														.getValue(caodaiUri));

									}

								}
								System.out.println("-------------------");
							}
						}

					}

				}
			}

		}
	}

	/**
	 * 第16步始迁祖信息更新shl:Person
	 */
	private void step16(List<ShumuModel> listP) {
		String g = Constant.GRAPH_PERSON;
		for (ShumuModel shumuModel : listP) {
			// 如果始祖数据不为空
			if (!StringUtilC.isEmpty(shumuModel.getShiqianzhu())) {
				// 可能是多个始祖姓名。如：梁御史中丞昭（字晦甫）*謙，字潛翁，號益齋，行文二
				String tempshiZuNameList = shumuModel.getShiqianzhu();
				// 始祖时代
				String tempshiZushidaiList = shumuModel.getShiqianzhushidai();
				List<FamilyNameModel> szList = new ArrayList<FamilyNameModel>();
				FamilyNameModel _model = new FamilyNameModel();
				_model.setSelfID(shumuModel.getRefIdentifier());
				// 使用书目标识符匹配先祖标识符
				szList = familyNameMapper.getXZData(_model);
				// 如果标识符匹配到人员信息
				if (szList != null && szList.size() > 0) {
					for (FamilyNameModel fModel : szList) {
						// 得到一条人员信息，已分割好
						PersonModel pModel = PersonUtils.getXzName(
								tempshiZuNameList, tempshiZushidaiList,
								fModel.getXingshiT());
						for (PersonModel pModel2 : pModel.getPersonList()) {
							// 如果匹配到信息，则进行更新
							if (pModel2.getAllName().equals(
									fModel.getFamilyNameT())) {
								String dataUri = fModel.getSelfUri();
								// 始迁祖
								String newroleOfFamilyUri = "http://data.library.sh.cn/jp/vocab/ancestor/shi-qian-zu";
								// 更新始祖URI：
								System.out.println("始祖姓名："
										+ pModel2.getAllName() + "---始祖URI:"
										+ newroleOfFamilyUri);
								this.changeValue2(g, dataUri, "roleOfFamily",
										"shl:roleOfFamily", EditDtoCommon
												.getValue(newroleOfFamilyUri));
								// 更新始祖URI：
								if (!StringUtilC.isEmpty(pModel2.getZhi())) {
									System.out.println("字：" + pModel2.getZhi());
									this.changeValue2(g, dataUri,
											"courtesyName", "shl:courtesyName",
											EditDtoCommon.getValue(pModel2
													.getZhi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHao())) {
									System.out.println("号：" + pModel2.getHao());
									this.changeValue2(g, dataUri, "pseudonym",
											"shl:pseudonym", EditDtoCommon
													.getValue(pModel2.getHao()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHang())) {
									System.out
											.println("行：" + pModel2.getHang());
									this.changeValue2(g, dataUri,
											"orderOfSeniority",
											"shl:orderOfSeniority",
											EditDtoCommon.getValue(pModel2
													.getHang()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShi())) {
									System.out
											.println("谥号：" + pModel2.getShi());
									this.changeValue2(g, dataUri,
											"orderOfSeniority",
											"shl:orderOfSeniority",
											EditDtoCommon.getValue(pModel2
													.getShi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShidai())) {
									System.out.println("始祖朝代："
											+ pModel2.getShidai());
									this.changeValue2(g, dataUri,
											"temporalValue",
											"shl:temporalValue", EditDtoCommon
													.getValue(pModel2
															.getShidai()));
									String caodaiUri = cdMap.get(pModel2
											.getShidai());
									if (!StringUtilC.isEmpty(caodaiUri)) {
										System.out.println("始祖朝代URI"
												+ caodaiUri);
										this.changeValue2(g, dataUri,
												"temporal", "shl:temporal",
												EditDtoCommon
														.getValue(caodaiUri));

									}

								}
								System.out.println("-------------------");
							}
						}

					}

				}
			}

		}
	}

	/**
	 * 第17步名人信息更新shl:Person
	 */
	private void step17(List<ShumuModel> listP) {
		String g = Constant.GRAPH_PERSON;
		for (ShumuModel shumuModel : listP) {
			// 如果名人数据不为空
			if (!StringUtilC.isEmpty(shumuModel.getMingren())) {
				// 可能是多个始祖姓名。如：梁御史中丞昭（字晦甫）*謙，字潛翁，號益齋，行文二
				String tempshiZuNameList = shumuModel.getMingren();
				// 始祖时代
				String tempshiZushidaiList = shumuModel.getMingrenshidai();
				List<FamilyNameModel> szList = new ArrayList<FamilyNameModel>();
				FamilyNameModel _model = new FamilyNameModel();
				_model.setSelfID(shumuModel.getRefIdentifier());
				// 使用书目标识符匹配先祖标识符
				szList = familyNameMapper.getMRData(_model);
				// 如果标识符匹配到人员信息
				if (szList != null && szList.size() > 0) {
					for (FamilyNameModel fModel : szList) {
						// 得到一条人员信息，已分割好
						PersonModel pModel = PersonUtils.getXzName(
								tempshiZuNameList, tempshiZushidaiList,
								fModel.getXingshiT());
						for (PersonModel pModel2 : pModel.getPersonList()) {
							// 如果匹配到信息，则进行更新
							if (pModel2.getAllName().equals(
									fModel.getFamilyNameT())) {
								String dataUri = fModel.getSelfUri();
								// 始迁祖
								String newroleOfFamilyUri = "http://data.library.sh.cn/jp/vocab/ancestor/ming-ren";
								// 更新始祖URI：
								System.out.println("始祖姓名："
										+ pModel2.getAllName() + "---始祖URI:"
										+ newroleOfFamilyUri);
								this.changeValue2(g, dataUri, "roleOfFamily",
										"shl:roleOfFamily", EditDtoCommon
												.getValue(newroleOfFamilyUri));
								// 更新始祖URI：
								if (!StringUtilC.isEmpty(pModel2.getZhi())) {
									System.out.println("字：" + pModel2.getZhi());
									this.changeValue2(g, dataUri,
											"courtesyName", "shl:courtesyName",
											EditDtoCommon.getValue(pModel2
													.getZhi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHao())) {
									System.out.println("号：" + pModel2.getHao());
									this.changeValue2(g, dataUri, "pseudonym",
											"shl:pseudonym", EditDtoCommon
													.getValue(pModel2.getHao()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHang())) {
									System.out
											.println("行：" + pModel2.getHang());
									this.changeValue2(g, dataUri,
											"orderOfSeniority",
											"shl:orderOfSeniority",
											EditDtoCommon.getValue(pModel2
													.getHang()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShi())) {
									System.out
											.println("谥号：" + pModel2.getShi());
									this.changeValue2(g, dataUri,
											"orderOfSeniority",
											"shl:orderOfSeniority",
											EditDtoCommon.getValue(pModel2
													.getShi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShidai())) {
									System.out.println("始祖朝代："
											+ pModel2.getShidai());
									this.changeValue2(g, dataUri,
											"temporalValue",
											"shl:temporalValue", EditDtoCommon
													.getValue(pModel2
															.getShidai()));
									String caodaiUri = cdMap.get(pModel2
											.getShidai());
									if (!StringUtilC.isEmpty(caodaiUri)) {
										System.out.println("始祖朝代URI"
												+ caodaiUri);
										this.changeValue2(g, dataUri,
												"temporal", "shl:temporal",
												EditDtoCommon
														.getValue(caodaiUri));

									}

								}
								System.out.println("-------------------");
							}
						}

					}

				}
			}

		}
	}

	/**
	 * 先祖，名人，relatedWork
	 * 
	 * @param listP
	 */
	private void step18(List<ShumuModel> listP) {
		String g = Constant.GRAPH_PERSON;
		// 先祖
		for (ShumuModel shumuModel : listP) {
			// 如果始祖数据不为空
			List<FamilyNameModel> szList = new ArrayList<FamilyNameModel>();
			FamilyNameModel _model = new FamilyNameModel();
			_model.setSelfID(shumuModel.getRefIdentifier());
			// 使用书目标识符匹配先祖标识符
			szList = familyNameMapper.getXZData(_model);
			// 如果标识符匹配到人员信息
			if (szList != null && szList.size() > 0) {
				for (FamilyNameModel fModel : szList) {
					this.changeValue2(g, fModel.getSelfUri(), "relatedWork",
							"shl:relatedWork",
							EditDtoCommon.getValue(shumuModel.getSelfUri()));
				}

			}

		}
		// 名人
		for (ShumuModel shumuModel : listP) {
			// 名人
			FamilyNameModel _model2 = new FamilyNameModel();
			_model2.setSelfID(shumuModel.getRefIdentifier());
			List<FamilyNameModel> mrList = new ArrayList<FamilyNameModel>();
			// 使用书目标识符匹配先祖标识符
			mrList = familyNameMapper.getMRData(_model2);
			// 如果标识符匹配到人员信息
			if (mrList != null && mrList.size() > 0) {
				for (FamilyNameModel fModel : mrList) {
					this.changeValue2(g, fModel.getSelfUri(), "relatedWork",
							"shl:relatedWork",
							EditDtoCommon.getValue(shumuModel.getSelfUri()));
				}

			}

		}
	}

	/**
	 * 得到旧值
	 * 
	 * @param _outStream
	 * @return
	 */
	private String getOldValue(OutputStream _outStream, String p) {
		String old_value = "";
		// 规范数据编辑属性
		DataEditViewDto dataEditView = DataManagerDto
				.fillPersonEditDto(StringUtilC.StreamToString(_outStream));
		// 去除NON等
		dataEditView = EditDtoCommon.listEmptyChange(dataEditView);
		for (DataEditDto _pDto : dataEditView.getEditList()) {
			if (_pDto.getOldEnName().equals(p)) {
				return EditDtoCommon.getValue(_pDto.getOldValue());
			}
		}
		return old_value;
	}

	/**
	 * 2次插入
	 * 
	 * @param g
	 * @param uri
	 * @param p
	 * @param pAll
	 * @param newValue
	 */
	private void changeValue2(String g, String uri, String p, String pAll,
			String newValue) {
		OutputStream _outStream = commonSparql.getJsonLD4Resource(g, uri);
		String old_value = getOldValue(_outStream, p);
		if (!old_value.equals(newValue)) {
			commonSparql.changeRDF(g, uri, pAll, old_value, newValue);
			System.out.println("旧址：" + old_value);
			System.out.println("新址：" + newValue);
		}
	}

	/**
	 * 2次插入
	 * 
	 * @param g
	 * @param uri
	 * @param p
	 * @param pAll
	 * @param newValue
	 */
	private void changeValueMoreValue(String g, String uri, String p,
			String pAll, String[] newValues) {
		OutputStream _outStream = commonSparql.getJsonLD4Resource(g, uri);
		String old_value = getOldValue(_outStream, p);
		String old[] = old_value.replace("\"", "").split(",");
		for (int i = 0; i < old.length; i++) {
			String ol = old[i];
			for (String newValue : newValues) {
				if (ol.substring(ol.length() - 4).equals(
						newValue.substring(newValue.length() - 4))) {
					commonSparql.changeRDF(g, uri, pAll,
							EditDtoCommon.getValue(ol),
							EditDtoCommon.getValue(newValue));
				}
			}

			System.out.println("旧址：" + old_value);
			System.out.println("新址：" + newValues);
		}
	}

	/**
	 * 取词词表
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importCategory", method = { RequestMethod.POST })
	public String importCategory() throws Exception {
		try {
			List<CategoryModel> listP = familyNameMapper
					.getCategoryData(new CategoryModel());
			DataUtilC.InsertCategory(listP);
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 朝代、年号纪年导入
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importChaodai", method = { RequestMethod.POST })
	public String importChaodai() throws Exception {
		try {

			/*
			 * OutputStream _outStream = commonSparql
			 * .getJsonLD4Resource(Constant.GRAPH_TITLE,
			 * "http://data.library.sh.cn/jp/authority/title/164zxo12vqc82p2d");
			 * System.out.println(StringUtilC.StreamToString(_outStream));
			 * ArrayList _list = vocabSparql.getSubjectProperties("bf:Title");
			 * System.out.println(_list.toString());
			 */
			// 朝代列表
			List<ChaodaiModel> listP = familyNameMapper
					.getChaodaiData(new ChaodaiModel());
			// id做成
			String firstS = "http://data.library.sh.cn/authority/temporal/";
			/*
			 * for (int i = 0; i < listP.size(); i++) { listP.get(i).setSelfUri(
			 * firstS + DataImportUtil.getRandomUriValue(16));
			 * familyNameMapper.updateChaodaiById(listP.get(i)); }
			 */
			// 插入朝代数据
			// DataUtilC.InsertChaodai(listP);
			// ///////////////////////////////////////////////////////////
			// 年号纪年列表
			List<ChaodaiYearModel> listP2 = familyNameMapper
					.getChaodaiYearData(new ChaodaiYearModel());
			// id做成

			/*
			 * for (int i = 0; i < listP2.size(); i++) {
			 * listP2.get(i).setSelfUri( firstS +
			 * DataUtilC.getRandomUriValue(16));
			 * familyNameMapper.updateChaodaiYearById(listP2.get(i)); }
			 */
			DataUtilC.InsertChaodaiYear(listP2);
		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 地名
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importPlace", method = { RequestMethod.POST })
	public String importPlace() throws Exception {
		try {
			/*
			 * String json =
			 * "[{\"graph\":\"http://gen.library.sh.cn/graph/place\",\"uri\":\"http://data.library.sh.cn/entity/place/cpq7jkevh6dp275t\",\"type\":\"http://www.library.sh.cn/ontology/Place\",\"properties\":[{\"uri\":\"bf:lable\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"奉贤\"}},{\"uri\":\"shl:country\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"中国\"}},{\"uri\":\"shl:province\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"上海市\"}},{\"uri\":\"shl:city\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"上海市\"}},{\"uri\":\"shl:county\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"奉贤区\"}},{\"uri\":\"owl:sameAs\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://www.cba.ac.cn/point/310119\"}}]},{\"graph\":\"http://gen.library.sh.cn/graph/place\",\"uri\":\"http://data.library.sh.cn/entity/place/ntwya73hddzoeonr\",\"type\":\"http://www.library.sh.cn/ontology/Place\",\"properties\":[{\"uri\":\"bf:lable\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"上海\"}},{\"uri\":\"shl:country\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"中国\"}},{\"uri\":\"shl:province\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"上海市\"}},{\"uri\":\"owl:sameAs\",\"type\":\"literal\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://www.cba.ac.cn/point/310000\"}}]}]"
			 * ; JSONArray _array = JSONArray.fromObject(json);
			 * DataImportUtil.InsertV(_array);
			 */

			List<PlaceModel> listP = familyNameMapper
					.getPlaceData(new PlaceModel());
			insertPace(listP);
			// DataUtilC.InsertPlace(listP);
			// System.out.println(placeSparql.getFullLocations("上海", "川沙"));

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 地名翻译，插入
	 */
	private void insertPace(List<PlaceModel> listP) {
		// DataUtilC.InsertPlace(listP);
		// 信息处理
		for (int i = 0; i < listP.size(); i++) {
			/*
			 * String parm1 = listP.get(i).getParm1S(); String parm2 =
			 * listP.get(i).getParm2S();
			 */
			String parm1 = listP.get(i).getShengS();
			String parm2 = listP.get(i).getLabelS();
			Map<String, String> map = getPlaceInfo(parm1, parm2);
			if (StringUtil.isEmpty(listP.get(i).getShiS())) {
				map = getPlaceInfo(parm1, "");
			} else {
				map = getPlaceInfo(parm1, parm2);
			}
			if (map != null || map.size() > 0) {
				// 经纬度
				if (!"".equals(StringUtilC.getString(map.get("s")))) {
					listP.get(i).setPoint(StringUtilC.getString(map.get("s")));
				}
				// 省
				if (!"".equals(StringUtilC.getString(map.get("province")))) {
					listP.get(i).setShengS(
							StringUtilC.getString(map.get("province")));
				} // 市
				if (!"".equals(StringUtilC.getString(map.get("city")))) {
					listP.get(i)
							.setShiS(StringUtilC.getString(map.get("city")));
				}
				// 县
				if (!"".equals(StringUtilC.getString(map.get("county")))) {
					listP.get(i).setXianS(
							StringUtilC.getString(map.get("county")));
				}
				// listP.get(i).setSelfUri(DataImportUtil.getRandomUriValue(16));
				familyNameMapper.updatePlaceById(listP.get(i));
			}
		}
	}

	/**
	 * 地名翻译
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private Map<String, String> getPlaceInfo(String p1, String p2) {
		ArrayList list = placeSparql.getFullLocations(p1, p2);
		Map<String, String> map = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			map = (Map) list.get(0);
			return map;
		}
		return null;
	}

	/**
	 * categoryType插入
	 */
	private void insetC() {
		// 先祖：
		String g = "http://gen.library.sh.cn/graph/baseinfo";
		String s = "http://data.library.sh.cn/jp/vocab/ancestor/xian-zu";
		String p = "http://bibframe.org/vocab/categoryType";
		String old_value = "";
		String value = "\"roleOfFamily\"";
		commonSparql.changeRDF(g, s, p, old_value, value);

		p = "http://bibframe.org/vocab/categoryValue";
		value = "\"先祖\"@chs";
		// commonSparql.changeRDF(g, s, p, old_value, value);
		value = "\"先祖\"@cht";
		commonSparql.changeRDF(g, s, p, old_value, value);
		// 名人：
		s = "http://data.library.sh.cn/jp/vocab/ancestor/ming-ren";
		p = "http://bibframe.org/vocab/categoryType";
		value = "\"roleOfFamily\"";
		commonSparql.changeRDF(g, s, p, old_value, value);
		p = "http://bibframe.org/vocab/categoryValue";
		value = "\"名人\"@chs";
		commonSparql.changeRDF(g, s, p, old_value, value);
		value = "\"名人\"@cht";
		commonSparql.changeRDF(g, s, p, old_value, value);
	}

	/**
	 * 姓氏的信息修改，更新到首页
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSureFamilyName", method = { RequestMethod.POST })
	public String updateSureFamilyName() throws Exception {
		boolean ifSuccess = false;
		List<DataChangeHistoryMainModel> _mainList = dataChangeHistoryMapper
				.getHistoryMainFamilyName();
		for (DataChangeHistoryMainModel dto : _mainList) {
			List<DataChangeHistoryListModel> listMolde;
			listMolde = dataChangeHistoryMapper.selectHistoryList(StringUtilC
					.getString(dto.getId()));
			String s = dto.getDataUri();
			SurnameModel fmodel = new SurnameModel();
			for (DataChangeHistoryListModel editDto : listMolde) {
				String o = StringUtilC.getString(editDto.getNewValue());
				String enName = StringUtilC.getString(editDto.getOldEnName());
				// 如果修改的是姓氏属性
				if (s.contains("http://data.library.sh.cn/authority/familyname")) {
					if (enName.equals("description")) {
						fmodel.setDescription(o);
						fmodel.setUri(s);
						familyNameMapper.updatesSurnameDesByUri(fmodel);
						break;
					} else if (enName.equals("label")) {
						fmodel.setFamilyNameS(EditDtoCommon.getValueFormat(o,
								"chs"));
						fmodel.setFamilyNameT(EditDtoCommon.getValueFormat(o,
								"cht"));
						fmodel.setPinyin(EditDtoCommon.getValueFormat(o, "en"));
						if (!StringUtilC.isEmpty(fmodel.getPinyin())) {
							// 拼音首字母更新
							fmodel.setInitial(fmodel.getPinyin()
									.substring(0, 1).toUpperCase());
						}
						fmodel.setUri(s);
						familyNameMapper.updatesSurnameDesByUri(fmodel);
						break;
					}
				}
			}
		}
		ifSuccess = true;
		jsonResult.put(result, ifSuccess);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * categoryType插入:干支
	 */
	private void insertGZ() {
		// 先祖：
		String g = "http://gen.library.sh.cn/graph/baseinfo";
		String s = "http://data.library.sh.cn/vocab/HSEB/geng-zi";
		String p = "http://bibframe.org/vocab/categoryType";
		String old_value = "";
		String value = "\"hseb\"";
		commonSparql.changeRDF(g, s, p, old_value, value);

		p = "http://bibframe.org/vocab/categoryValue";
		value = "\"庚子\"";
		commonSparql.changeRDF(g, s, p, old_value, value);
		String json = "[{\"graph\":\"http://gen.library.sh.cn/graph/person\",\"uri\":\"http://data.library.sh.cn/entity/person/3434343434\",\"type\":\"shl:Person\",\"properties\":[{\"uri\":\"bf:label\",\"type\":\"literal\",\"repeatable\":\"true\",\"objects\":[{\"value\":\"丁一中\",\"language\":\"chs\"},{\"value\":\"丁一中\",\"language\":\"cht\"}]},{\"uri\":\"shl:familyName\",\"type\":\"resource\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://data.library.sh.cn/authority/familyname/1\"}},{\"uri\":\"shl:roleOfFamily\",\"type\":\"resource\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://gen.library.sh.cn/Category/999\"}}]}]";
		/*
		 * _jsonArrayRoot = JSONArray.fromObject(json); InsertV(_jsonArrayRoot);
		 */
	}
}