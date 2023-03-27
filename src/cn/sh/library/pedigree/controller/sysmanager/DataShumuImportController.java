package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.TitleSparql;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.FamilyNameMapper;
import cn.sh.library.pedigree.sysManager.mapper.ShumuImpMapper;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.PersonModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/dataShumuImportManager")
public class DataShumuImportController extends BaseController {
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	public CommonSparql commonSparql;
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
	@Resource
	private WorkService workService;
	@Autowired
	private WorkSparql workSparql;
	@Autowired
	private FamilyNameMapper familyNameMapper;

	@Autowired
	private ShumuImpMapper shumuImpMapper;

	// private static Map<String, String> cdMap = new HashMap<String, String>();

	/**
	 * 书目
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/chageWorkPlace", method = { RequestMethod.POST })
	public String chageWorkPlace() throws Exception {
		List<ShumuModel> zrzsdPersonList = shumuImpMapper.getWorkErrorPlace();
		String g = Constant.GRAPH_WORK;
		String gPlace = Constant.GRAPH_PLACE;
		/**
		 * 后添加
		 */

		for (ShumuModel shumuModel : zrzsdPersonList) {
			if (!StringUtilC.isEmpty(shumuModel.getJudiUri())) {
				try {
					commonSparql
							.changeRDF(g, shumuModel.getSelfUri(),
									"http://www.library.sh.cn/ontology/place",
									EditDtoCommon.getValue(shumuModel
											.getJudiUri()), "");
					commonSparql.changeRDF(g, shumuModel.getSelfUri(),
							"http://www.library.sh.cn/ontology/place", "",
							EditDtoCommon.getValue(shumuModel.getRefUri()));
					// 删除place
					commonSparql
							.deleteResource(gPlace, shumuModel.getJudiUri());
				} catch (Exception e) {
					// TODO: handle exception
				}
				// 更替URI
				/*
				 * this.changeValue2(g, shumuModel.getSelfUri(), "place",
				 * "shl:place", EditDtoCommon.getValue(shumuModel.getRefUri()));
				 */
			}

		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 更新DOI
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateDoi", method = { RequestMethod.POST })
	public String updateDoi(@Param(value = "doi") String doi,
			@Param(value = "flg") String flg) throws Exception {
		workService.updateAccessLevelByDoi(doi, flg);
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
	public String importShumu(@Param(value = "step") String step)
			throws Exception {
		try {
			List<ShumuModel> listP = familyNameMapper
					.getShumuData(new ShumuModel());

			// List<ShumuModel> listP = null;
			switch (step) {
			case "1":
				// 第一步,ID做成
				step1(listP);
				break;
			case "2":
				// 第二步书目据导入
				step2(listP);
				break;
			case "3":
				step4();
				break;
			case "4":
				step5();
				break;
			case "5":
				step6(listP);
				break;
			case "6":
				step7();
				break;
			case "7":
				step8();
				break;
			case "8":
				step9(listP);
				break;
			case "9":
				step10();
				break;
			case "10":
				step11(listP);
				break;
			case "11":
				step12(listP);
				break;
			case "12":
				step13(listP);
				break;
			case "13":
				step14(listP);
				break;
			case "14":
				step15(listP);
				break;
			case "15":
				step16(listP);
				break;
			case "16":
				step17(listP);
				break;
			case "17":
				step18(listP);
				break;
			case "18":
				deleteJudi();
				break;
			// DOI做成
			case "19":
				insertDOI();
				break;
			// 馆藏地香港中大修改
			case "20":
				updateErrorGCDList();
				break;
			// 姓氏的导入
			case "21":
				insertWorkFNUri();
				break;
			// 姓氏的导入
			case "22":
				insertSDPerson();
				break;
			default:
				break;
			}
			// ////////////////////////////////////测试end
			// 第一步,ID做成
			// step1(listP);
			// 第二步书目据导入
			// step2(listP);
			// step2_1(listP);//补充
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
			// step13_3(listP);//删
			// step13_2(listP);//插
			// step13_4(listP);//补充
			// step13(listP);//正常
			// instance信息插入 补充版本年代朝代URI
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
	 * 居地URI，补充
	 * 
	 * @param listP
	 */
	private void updateErrorGCDList() {
		String g = Constant.GRAPH_ITEM;
		List<ShumuModel> listP = shumuImpMapper.getErrorGCDList();
		for (ShumuModel shumuModel : listP) {
			this.changeValue2(g, shumuModel.getItemUri(), "description",
					"shl:description",
					EditDtoCommon.getValue(shumuModel.getDescription()));
			this.changeValue2(g, shumuModel.getItemUri(), "heldBy",
					"bf:heldBy",
					EditDtoCommon.getValue(shumuModel.getShouchangzhe()));
		}
	}

	/**
	 * 居地URI，补充
	 * 
	 * @param listP
	 */
	private void insertDOI() {

		String uri = null;
		try {
			String g = Constant.GRAPH_ITEM;
			List<ShumuModel> listP = shumuImpMapper.getDOIList();
			for (ShumuModel shumuModel : listP) {
				List<Map<String, String>> instances = RDFUtils
						.transformListMap(this.workSparql
								.getInstances4Work(shumuModel.getWorkUri()));
				for (Map<String, String> map : instances) {
					Set<String> keys = map.keySet();// 获取KEY集合
					for (String key : keys) {
						// 得到馆藏地的URI：上图
						if (key.equals("hbs")) {
							// 如果是上图
							if (StringUtilC.getString(map.get(key)).equals(
									shumuModel.getItemUri())) {
								// itemUri
								String itemUri = StringUtilC.getString(map
										.get("item"));
								uri = itemUri;
								// doi
								String doi = shumuModel.getDoi();
								// callno
								String callno = shumuModel.getCallno();
								/*
								 * this.changeValue2(g, itemUri, "description",
								 * "shl:description",
								 * EditDtoCommon.getValue(doi));
								 */
								/*
								 * this.changeValue2(g, itemUri, "DOI",
								 * "shl:DOI", EditDtoCommon.getValue(doi));
								 */
								commonSparql.changeRDF(g, itemUri, "shl:DOI",
										"", EditDtoCommon.getValue(doi));
								commonSparql.changeRDF(g, itemUri,
										"shl:accessLevel", "",
										EditDtoCommon.getValue("1"));
								this.changeValue2(g, itemUri, "shelfMark",
										"bf:shelfMark",
										EditDtoCommon.getValue(callno));
							}

						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("DOI导入错误" + e);
			System.out.println("DOI导入错误:Uri:" + uri);
		}
	}

	/**
	 * work补充姓氏URI，补充
	 * 
	 * @param listP
	 */
	private void insertWorkFNUri() {
		List<ShumuModel> listP = shumuImpMapper.getWorkFamilyNameUriList();
		DataShumuUtilC.InsertFamilyName(listP);

	}

	/**
	 * work补充姓氏URI，补充
	 * 
	 * @param listP
	 */
	private void insertSDPerson() {
		// 根Array
		DataUtilC.InsertV(JSONArray.fromObject(sdPersonStr));

	}

	/**
	 * 1书目数据ID做成
	 */
	private void step1(List<ShumuModel> listP) {
		String firstS = "http://data.library.sh.cn/jp/resource/work/";
		String firstInstanceS = "http://data.library.sh.cn/jp/resource/instance/";
		for (int i = 0; i < listP.size(); i++) {
			// workUri
			listP.get(i).setSelfUri(firstS + DataUtilC.getRandomUriValue(16));
			// InstanceUri
			listP.get(i).setInstanceUri(
					firstInstanceS + DataUtilC.getRandomUriValue(16));
			familyNameMapper.updateShumuById(listP.get(i));
		}
	}

	/**
	 * 2书目数据导入
	 */
	private void step2(List<ShumuModel> listP) {
		DataShumuUtilC.InsertShumu(listP);
	}

	/**
	 * 居地URI，补充
	 * 
	 * @param listP
	 */
	private void step2_1(List<ShumuModel> listP) {
		String g = Constant.GRAPH_WORK;
		for (ShumuModel shumuModel : listP) {
			if (!StringUtilC.isEmpty(shumuModel.getJudiUri()))
				this.changeValue2(g, shumuModel.getInstanceUri(), "place",
						"shl:place",
						EditDtoCommon.getValue(shumuModel.getJudiUri()));
		}

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
		/**
		 * 后添加
		 */

		for (ShumuModel shumuModel : zrzsdPersonList) {
			if (!StringUtilC.isEmpty(shumuModel.getZxzUri())) {
				this.changeValue2(g, shumuModel.getZxzUri(), "temporal",
						"shl:temporal",
						EditDtoCommon.getValue(shumuModel.getRefUri()));
			}

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
			 * boolean bf = commonSparql.changeRDF(g, shumuModel.getZxzUri(), p,
			 * "", EditDtoCommon.getValue(shumuModel.getRefUri()));
			 */
			if (!StringUtilC.isEmpty(shumuModel.getZxzUri())) {
				this.changeValue2(g, shumuModel.getZxzUri(), "role", "bf:role",
						EditDtoCommon.getValue(shumuModel.getRefUri()));
			}
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
			 * commonSparql.changeRDF(g, shumuModel.getZxzUri(), p, "",
			 * EditDtoCommon.getValue(shumuModel.getRefUri()));
			 */
			if (!StringUtilC.isEmpty(shumuModel.getZxzUri())) {
				this.changeValue2(g, shumuModel.getZxzUri(), "temporal",
						"shl:temporal",
						EditDtoCommon.getValue(shumuModel.getRefUri()));
			}

		}

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
			 * boolean bf = commonSparql.changeRDF(g, shumuModel.getZxzUri(), p,
			 * "", EditDtoCommon.getValue(shumuModel.getRefUri()));
			 */
			if (!StringUtilC.isEmpty(shumuModel.getZxzUri())) {
				this.changeValue2(g, shumuModel.getZxzUri(), "role", "bf:role",
						EditDtoCommon.getValue(shumuModel.getRefUri()));
			}
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
	 * 第13步Instance信息插入category更新：后补，如果新导入，则该方法可省去
	 */
	private void step13_4(List<ShumuModel> listP) {
		String g = Constant.GRAPH_INSTANCE;
		for (ShumuModel shumuModel : listP) {
			if (!StringUtilC.isEmpty(shumuModel.getZhuangdingUri()))
				this.changeValue2(g, shumuModel.getInstanceUri(), "category",
						"bf:category",
						EditDtoCommon.getValue(shumuModel.getZhuangdingUri()));
		}

	}

	/**
	 * 第13步Instance信息插入shl:temporal：后补，如果新导入，则该方法可省去
	 */
	private void step13_3(List<ShumuModel> listP) {
		String g = Constant.GRAPH_INSTANCE;
		String p = "shl:temporal";
		for (ShumuModel shumuModel : listP) {
			if (!StringUtilC.isEmpty(shumuModel.getRefChaodaiUri()))
				commonSparql.changeRDF(g, shumuModel.getInstanceUri(), p,
						EditDtoCommon.getValue(shumuModel.getRefChaodaiUri()),
						"");
		}

	}

	/**
	 * 第14步Item信息插入bf:Item
	 */
	private void step14(List<ShumuModel> listP) {
		// 书目总条目
		List<ShumuModel> lastList = new ArrayList<ShumuModel>();
		List<ShumuModel> tempzrzList = null;
		for (ShumuModel shumuModel : listP) {
			tempzrzList = new ArrayList<ShumuModel>();
			tempzrzList = shumuImpMapper.getItemListIdentifier(shumuModel
					.getIdentifier());
			// 收藏者List
			shumuModel.setOthersList(tempzrzList);
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
								this.changeValue2(g, dataUri, "roleOfFamily",
										"shl:roleOfFamily", EditDtoCommon
												.getValue(newroleOfFamilyUri));
								System.out.println("URI" + fModel.getSelfUri());
								// 更新始祖URI：
								if (!StringUtilC.isEmpty(pModel2.getZhi())) {
									System.out.println("字：" + pModel2.getZhi());
									/*
									 * commonSparql.changeRDF(g, dataUri,
									 * "shl:courtesyName", old_courtesyName,
									 * EditDtoCommon.getValue(pModel2
									 * .getZhi()));
									 */
									this.changeValue2(g, dataUri,
											"courtesyName", "shl:courtesyName",
											EditDtoCommon.getValue(pModel2
													.getZhi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHao())) {
									System.out.println("号：" + pModel2.getHao());
									/*
									 * commonSparql .changeRDF(g, dataUri,
									 * "shl:pseudonym", old_pseudonym,
									 * EditDtoCommon .getValue(pModel2
									 * .getHao()));
									 */
									this.changeValue2(g, dataUri, "pseudonym",
											"shl:pseudonym", EditDtoCommon
													.getValue(pModel2.getHao()));
								}
								if (!StringUtilC.isEmpty(pModel2.getHang())) {
									System.out
											.println("行：" + pModel2.getHang());
									/*
									 * commonSparql.changeRDF(g, dataUri,
									 * "shl:orderOfSeniority",
									 * old_orderOfSeniority,
									 * EditDtoCommon.getValue(pModel2
									 * .getHang()));
									 */
									this.changeValue2(g, dataUri,
											"orderOfSeniority",
											"shl:orderOfSeniority",
											EditDtoCommon.getValue(pModel2
													.getHang()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShi())) {
									System.out
											.println("谥号：" + pModel2.getShi());
									/*
									 * commonSparql.changeRDF(g, dataUri,
									 * "shl:posthumousName", old_posthumousName,
									 * EditDtoCommon.getValue(pModel2
									 * .getShi()));
									 */
									this.changeValue2(g, dataUri,
											"posthumousName",
											"shl:posthumousName", EditDtoCommon
													.getValue(pModel2.getShi()));
								}
								if (!StringUtilC.isEmpty(pModel2.getShidai())) {
									System.out.println("始祖朝代："
											+ pModel2.getShidai());
									/*
									 * commonSparql.changeRDF(g, dataUri,
									 * "shl:temporalValue", old_temporalValue,
									 * EditDtoCommon.getValue(pModel2
									 * .getShidai()));
									 */
									this.changeValue2(g, dataUri,
											"temporalValue",
											"shl:temporalValue", EditDtoCommon
													.getValue(pModel2
															.getShidai()));
									/*
									 * String caodaiUri = cdMap.get(pModel2
									 * .getShidai());
									 */
									String caodaiUri = getCdUri(pModel2
											.getShidai());

									if (!StringUtilC.isEmpty(caodaiUri)) {
										System.out.println("始祖朝代URI"
												+ caodaiUri);
										/*
										 * commonSparql.changeRDF(g, dataUri,
										 * "shl:temporal", old_temporal,
										 * EditDtoCommon .getValue(caodaiUri));
										 */
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
									/*
									 * String caodaiUri = cdMap.get(pModel2
									 * .getShidai());
									 */

									String caodaiUri = getCdUri(pModel2
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
									/*
									 * String caodaiUri = cdMap.get(pModel2
									 * .getShidai());
									 */
									String caodaiUri = getCdUri(pModel2
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
			// 使用书目标识符匹配名人标识符
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
		// 纂修者
		for (ShumuModel shumuModel : listP) {
			// 名人
			FamilyNameModel _model2 = new FamilyNameModel();
			_model2.setSelfID(shumuModel.getRefIdentifier());
			List<FamilyNameModel> mrList = new ArrayList<FamilyNameModel>();
			// 使用书目标识符匹配纂修者标识符
			mrList = familyNameMapper.getZXZData(_model2);
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
	 * 删除居地为空的place属性
	 */
	private void deleteJudi() {
		List<ShumuModel> list = shumuImpMapper.getShumuJudiNull();
		String g = Constant.GRAPH_WORK;
		for (ShumuModel shumuModel : list) {
			this.changeValue2(g, shumuModel.getSelfUri(), "place", "shl:place",
					"");
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
			try {
				commonSparql.changeRDF(g, uri, pAll, old_value, newValue);
				//System.out.println("旧址：" + old_value);
				//System.out.println("新址：" + newValue);
			} catch (Exception e) {
				System.out.println(e);
			}

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
			String pAll, String newValue) {
		OutputStream _outStream = commonSparql.getJsonLD4Resource(g, uri);
		String old_value = getOldValue(_outStream, p);
		if (!old_value.equals(newValue)) {
			String old[] = old_value.replace("\"", "").split(",");
			for (int i = 0; i < old.length; i++) {
				String ol = EditDtoCommon.getValue(old[i]);
				commonSparql.changeRDF(g, uri, pAll, ol, newValue);
			}
			System.out.println("旧址：" + old_value);
			System.out.println("新址：" + newValue);
		}
	}

	String sdPersonStr = "[{\"graph\":\"http://sd.library.sh.cn/graph/person\",\"uri\":\"http://data.library.sh.cn/sd/entity/person/ixonlqlk4px7xtl7\",\"type\":\"http://www.library.sh.cn/ontology/Person\",\"properties\":[{\"uri\":\"bf:label\",\"type\":\"literal\",\"repeatable\":\"true\",\"objects\":[{\"value\":\"丁一中\",\"language\":\"chs\"},{\"value\":\"丁一中\",\"language\":\"cht\"}]},{\"uri\":\"foaf:familyName\",\"type\":\"resource\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://data.library.sh.cn/authority/familyname/68n959cf8zdfkz3v\"}},{\"uri\":\"http://www.library.sh.cn/ontology/roleOfFamily\",\"type\":\"resource\",\"repeatable\":\"false\",\"objects\":{\"value\":\"http://data.library.sh.cn/sd/vocab/ancestor/xian-zu\"}}]}]";

	/**
	 * 根据朝代汉语字符串，得到朝代URI
	 * 
	 * @param cdName
	 * @return
	 */
	private String getCdUri(String cdName) {
		return shumuImpMapper.getCdUriByCdName(cdName);
	}

}