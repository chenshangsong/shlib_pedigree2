package cn.sh.library.pedigree.controller.sysmanager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.dto.sysmanager.DataManagerDto;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.PlaceErrorMapper;
import cn.sh.library.pedigree.sysManager.model.ShlPersonPlaceModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.utils.StringUtilC;

@Controller
@RequestMapping("/dataErrorPlaceImportManager")
public class DataErrorPlaceImportController extends BaseController {
	@Autowired
	public VocabSparql vocabSparql;
	@Autowired
	public WorkSparql ws;
	@Autowired
	public CommonSparql commonSparql;
	@Autowired
	public PlaceSparql placeSparql;
	@Autowired
	public BaseinfoSparql baseinfoSparql;
	@Autowired
	private WorkSparql workSparql;

	@Autowired
	private PlaceErrorMapper placeErrorMapper;

	// private static Map<String, String> cdMap = new HashMap<String, String>();

	/**
	 * 书目
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/chageWorkPlace", method = { RequestMethod.POST })
	public String chageWorkPlace() throws Exception {
		List<ShumuModel> zrzsdPersonList = placeErrorMapper.getWorkErrorPlace();
		String g = Constant.GRAPH_WORK;
		String gPlace = Constant.GRAPH_PLACE;
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
			}

		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateErrorPlace", method = { RequestMethod.POST })
	public String updateErrorPlace() throws Exception {
		try {
			// 2 获取Flag=0的数据，如果市不为空，用省和Lable获取经纬度。如果市为空，用省获取经纬度。（标识获取不到经纬度的地名）
			List<ShlPersonPlaceModel> listPlaceTemp = placeErrorMapper
					.getErrorPlaceList("0");
			// 循环处理临时表数据
			for (ShlPersonPlaceModel temp : listPlaceTemp) {
				// 2
				// 获取Flag=0的数据，如果市不为空，用省和Lable获取经纬度。如果市为空，用省获取经纬度。（标识获取不到经纬度的地名）
				updatePlace(temp);
				/*
				 * commonSparql.changeRDF(Constant.GRAPH_PLACE,
				 * temp.getSelfUri(), "owl:sameAs",
				 * EditDtoCommon.getValue(temp.getOldpoint()),
				 * EditDtoCommon.getValue(temp.getPoint()));
				 * this.changeValueMoreValue(Constant.GRAPH_PLACE,
				 * temp.getSelfUri(), "label", "bf:label", new String[] {
				 * temp.getLabelS() + "@chs", temp.getLabelT() + "@cht" });
				 * this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
				 * "province", "shl:province",
				 * EditDtoCommon.getValue(temp.getShengS()));
				 * this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
				 * "city", "shl:city", EditDtoCommon.getValue(temp.getShiS()));
				 * this.changeValue2(Constant.GRAPH_PLACE, temp.getSelfUri(),
				 * "county", "shl:county",
				 * EditDtoCommon.getValue(temp.getXianS()));
				 */

			}

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateVTPlace", method = { RequestMethod.POST })
	public String updateVTPlace() throws Exception {
		try {
			// 2 获取Flag=0的数据，如果市不为空，用省和Lable获取经纬度。如果市为空，用省获取经纬度。（标识获取不到经纬度的地名）
			List<ShlPersonPlaceModel> listPlaceTemp = placeErrorMapper
					.updateVTPlaceList();
			// 循环处理临时表数据
			for (ShlPersonPlaceModel temp : listPlaceTemp) {
				// 2
				// 获取Flag=0的数据，如果市不为空，用省和Lable获取经纬度。如果市为空，用省获取经纬度。（标识获取不到经纬度的地名）
				// updatePlace(temp);
				commonSparql.changeRDF(Constant.GRAPH_PLACE, temp.getSelfUri(),
						"owl:sameAs",
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

			}

		} catch (Exception e) {
			System.out.println("数据列表错误：" + e);
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 更新错误place表的经纬度
	 * 
	 * @param temp
	 */
	private void updatePlace(ShlPersonPlaceModel temp) {
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
			// 更新 Point
			ShlPersonPlaceModel _tem = new ShlPersonPlaceModel();
			_tem.setId(temp.getId());
			String point = StringUtilC.getString(map.get("s"));
			if (StringUtilC.isEmpty(point)) {
				// _tem.setFlg("9");
			} else {
				// _tem.setFlg("0");
				_tem.setPoint(point);
			}
			placeErrorMapper.updatePlace(_tem);
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
				System.out.println("旧址：" + old_value);
				System.out.println("新址：" + newValue);
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
		if (old.length > 1) {
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
		} else {// 新插入
			if (newValues.length > 1) {
				commonSparql.changeRDF(g, uri, pAll, "",
						EditDtoCommon.getValue(newValues[0]));
				commonSparql.changeRDF(g, uri, pAll, "",
						EditDtoCommon.getValue(newValues[1]));
			}
		}

	}
}