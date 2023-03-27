package cn.sh.library.pedigree.controller.sysmanager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.JPDataImport;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.sysmanager.common.EditDtoCommon;
import cn.sh.library.pedigree.dto.Item;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.TitleSparql;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.sysManager.mapper.JpWorkImportMapper;
import cn.sh.library.pedigree.sysManager.model.JpPersonModel;
import cn.sh.library.pedigree.sysManager.model.JpWorkModel;

@Controller
@RequestMapping("/jpWorkImportManager")
public class JpWorkImportController extends BaseController {
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
	private WorkService workService;
	@Autowired
	private WorkSparql workSparql;

	@Autowired
	private JpWorkImportMapper jpWorkImport;

	/**
	 * 导入正题名
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importZTM", method = { RequestMethod.POST })
	public String importZTM() throws Exception {
		try {
			List<JpWorkModel> listP = jpWorkImport.getWorkList();
			JPDataImport.step1_InsertZhengshuming(listP);
		} catch (Exception e) {
			// TODO: handle exception
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 导入正题名
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importWork", method = { RequestMethod.POST })
	public String importWork() throws Exception {
		try {
			List<JpWorkModel> listP = jpWorkImport.getWorkList();
			if (JPDataImport.step2_InsertWork(listP)) {
				if (JPDataImport.step3_InsertInstance(listP)) {
					JPDataImport.step4_InsertItem(listP);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 导入 人
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/importPerson", method = { RequestMethod.POST })
	public String importPerson() throws Exception {
		try {
			List<JpPersonModel> listxzmr = jpWorkImport.getXzmrList();
			List<JpPersonModel> listzrz = jpWorkImport.getZrzList();
			List<JpPersonModel> listzrzQt = jpWorkImport.getZrzQtList();
			// 先祖名人
			if (JPDataImport.step5_InsertXzmr(listxzmr)) {
				// 责任者
				if (JPDataImport.step6_InsertZRZandQT(listzrz)) {
					// 其他责任者
					if (JPDataImport.step6_InsertZRZandQT(listzrzQt)) {
						// 责任者入Work
						if (JPDataImport.step7_InsertZrzToWork(listzrz)) {
							// 其他责任者入Work
							JPDataImport.step8_InsertZrzqtToWork(listzrzQt);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
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
	public String updateDoi() throws Exception {
		List<JpWorkModel> listP = jpWorkImport.getDOIAccesslevelList();
		for (JpWorkModel jpWorkModel : listP) {
			List<Item> _list = workService.getItemListByDoi(jpWorkModel
					.getDOI());
			if (_list.size() > 0) {
				for (Item item : _list) {
					String oldValue = EditDtoCommon.getValue(item
							.getAccessLevel());
					String newValue = EditDtoCommon.getValue(jpWorkModel.getAccesslevel());
					if (!oldValue.equals(newValue)) {
						commonSparql.changeRDF(Constant.GRAPH_ITEM,
								item.getUri(), "shl:accessLevel", oldValue,
								newValue);

					}
				}
			}

		}
		jsonResult.put(result, "0");
		return JSonUtils.toJSon(jsonResult);
	}
}