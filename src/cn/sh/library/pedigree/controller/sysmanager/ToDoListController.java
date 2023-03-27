package cn.sh.library.pedigree.controller.sysmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.StringUtilC;

@Controller
@RequestMapping("/toDoList")
public class ToDoListController extends BaseController {

	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "sysmanager/todolist/list";
	}

	/**
	 * 获取数据修改列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<String, Object> loadList(String selDataType,
			String selCheckStatus, Pager pager, HttpSession hs)
			throws Exception {

		Map<String, Object> result = new HashMap<>();
		List<DataChangeHistoryMainModel> data = new ArrayList<DataChangeHistoryMainModel>();
		//
		DataChangeHistoryMainModel searchModel = new    DataChangeHistoryMainModel();
		// 专家，管理员用户，查询所有
		String[] admin = new String[] { "2", "3" };
		if (!Arrays.asList(admin).contains(getUser(hs).getRoleId())) {
			searchModel.setCreateUser(StringUtilC
					.getString(getUser(hs).getId()));
		} 
		if (!StringUtilC.isEmpty(selDataType)) {
			searchModel.setDataType(selDataType);
		}
		if (!StringUtilC.isEmpty(selCheckStatus)) {
			searchModel.setCheckStatus(selCheckStatus);
		}
		try {
			DtoJsonPageData grid = new DtoJsonPageData(this);
			// 查询条件
			searchModel.setPage(pager.getPageth());// 当前yem
			searchModel.setRows(pager.getPageSize());
			data = dataChangeHistoryMapper
					.selectLoginHistoryMainListPage(searchModel);
			for (DataChangeHistoryMainModel model : data) {
				String name = DataTypeMap.getDataTypeName(model.getDataType());
				model.setDataTypeName(name);
				if(!StringUtilC.isEmpty(model.getCreateUser())){
					model.setCreateUser(getUserInfoModel(model.getCreateUser())
							.getUserName());
				}
			}
			// 设定输出对象
			grid.print2JsonObj(searchModel, data);
			pager.calcPageCount(StringUtilC.getLong(String.valueOf(searchModel
					.getRecords())));
			result.put("pager", pager);
			result.put("works", grid.getRoot());
		} catch (Exception e) {
			System.out.println("获取数据修改错误：" + e.getMessage());
		}

		return result;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	private UserInfoModel getUserInfoModel(String userId) {
		UserInfoModel user = userInfoMapper.getUserById(userId);
		return user;
	}
}