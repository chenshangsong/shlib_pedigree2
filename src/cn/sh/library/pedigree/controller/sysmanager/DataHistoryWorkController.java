package cn.sh.library.pedigree.controller.sysmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.DataItemChangeHistoryMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.DataItemChangeHistoryModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

/**
 * 历史数据
 * 
 * @author chenss
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/dataWorkHistory")
public class DataHistoryWorkController extends BaseController {

	@Autowired
	private DataChangeHistoryMapper dataChangeHistoryMapper;
	@Autowired
	private DataItemChangeHistoryMapper workMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;

	/**
	 * 修改历史列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ModelAndView history(@Param(value = "dataUri") String dataUri,
			@Param(value = "dataType") String dataType) throws Exception {

		List<DataChangeHistoryMainModel> data = new ArrayList<DataChangeHistoryMainModel>();

		try {

			data = dataChangeHistoryMapper.selectHistoryMain(dataUri, dataType);

			for (DataChangeHistoryMainModel model : data) {

				UserInfoModel user = this.getUserInfoModel(model
						.getCreateUser());
				model.setCreateUser(user.getUserName());

				if (model.getReleaseUser() != null
						&& !model.getReleaseUser().equals("")) {
					user = this.getUserInfoModel(model.getReleaseUser());
					model.setReleaseUser(user.getUserName());
				}

			}

		} catch (Exception e) {
			System.out.println("修改历史列表错误：" + e.getMessage());
		}

		modelAndView.addObject("data", data);
		modelAndView.setViewName("sysmanager/datahistory/confWorkHistory");
		return modelAndView;
	}

	/**
	 * 属性列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/confPropertylist", method = RequestMethod.GET)
	public ModelAndView confPropertylist(@Param(value = "id") String id)
			throws Exception {
		List<DataChangeHistoryListModel> workdata = new ArrayList<DataChangeHistoryListModel>();
		List<DataChangeHistoryListModel> instanceData = new ArrayList<DataChangeHistoryListModel>();
		List<DataItemChangeHistoryModel> itemData = new ArrayList<DataItemChangeHistoryModel>();
		try {
			workdata = dataChangeHistoryMapper.selectHistoryList(id);
			instanceData = dataChangeHistoryMapper.selectInstanceHistoryList(id);
			itemData=workMapper.selectItemHistoryList(id);
			// 被编辑对象以及下属属性列表
			//work列表
			modelAndView.addObject("workData", workdata);
			//instance列表
			modelAndView.addObject("instanceData", instanceData);
			//item列表
			modelAndView.addObject("itemData", itemData);
		} catch (Exception e) {
			System.out.println("修改历史详细页面错误：" + e.getMessage());
		}
		modelAndView.setViewName("sysmanager/datahistory/confWorkPropertylist");
		return modelAndView;
	}

	/**
	 * 历史保存排他
	 * 
	 * @param dataUri
	 * @param dataType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/isEditByOthers", method = RequestMethod.POST)
	public String isEditByOthers(@Param(value = "dataUri") String dataUri,
			@Param(value = "dataType") String dataType) {
		int count = dataChangeHistoryMapper.selectCountByDataUri(dataUri,
				dataType);
		if (count > 0) {
			jsonResult.put(result, FWConstants.result_error);
		} else {
			jsonResult.put(result, FWConstants.result_success);
		}
		return JSonUtils.toJSon(jsonResult);
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
