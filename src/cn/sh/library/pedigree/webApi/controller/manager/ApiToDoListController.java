package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.utils.StringUtilC;

@Controller
@RequestMapping("/webapi/manager/todoList")
public class ApiToDoListController extends BaseController {
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(ApiToDoListController.class);
	/**
	 * 获取数据修改列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(String dataType, String checkStatus, Pager pager,
			HttpSession hs) throws Exception {
		try {
			if (ifExsitUser(hs)) {
			jsonResult = new HashMap<>();
			DataChangeHistoryMainModel searchModel = new DataChangeHistoryMainModel();
			// 专家，管理员用户，查询所有
			String[] admin = new String[] { "2", "3" };
			if (!Arrays.asList(admin).contains(getUser(hs).getRoleId())) {
				searchModel.setCreateUser(StringUtilC.getString(getUser(hs)
						.getUserId()));
			}
			if (!StringUtilC.isEmpty(dataType)) {
				searchModel.setDataType(dataType);
			}
			if (!StringUtilC.isEmpty(checkStatus)) {
				searchModel.setCheckStatus(checkStatus);
			}

			DtoJsonPageData grid = new DtoJsonPageData(this);
			// 查询条件
			searchModel.setPage(pager.getPageth());// 当前yem
			searchModel.setRows(pager.getPageSize());
			List<DataChangeHistoryMainModel> _list = dataChangeHistoryMapper
					.selectLoginHistoryMainListPage(searchModel);
			for (DataChangeHistoryMainModel model : _list) {
				String name = DataTypeMap.getDataTypeName(model.getDataType());
				model.setDataTypeName(name);
				if (!StringUtilC.isEmpty(model.getCreateUser())) {
					model.setCreateUser(userInfoMapper.getUserById(
							model.getCreateUser()).getUserName());
				}
			}
			// 设定输出对象
			grid.print2JsonObj(searchModel, _list);
			pager.calcPageCount(StringUtilC.getLong(String.valueOf(searchModel
					.getRecords())));
			jsonResult.put("pager", pager);
			jsonResult.put("datas", _list); 
			jsonResult.put(result, FWConstants.result_success);}else {
				jsonResult.put(result, FWConstants.result_usernull);
				jsonResult.put("errCode", FWConstants.result_usernull);
				jsonResult.put("errMsg", FWConstants.result_usernull_msg);
			}
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println(e);
			jsonResult.put(result, FWConstants.result_error);
			jsonResult.put("errCode", FWConstants.result_error);
			jsonResult.put("errMsg", FWConstants.result_error_msg);
			return JSonUtils.toJSon(jsonResult);
		}

	}

}