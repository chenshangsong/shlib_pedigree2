package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.sysManager.service.LoginNormalService;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 用户登录Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/webapi/manager/user")
public class ApiUserInfoController extends BaseController {
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Resource
	private LoginNormalService loginNormalService;

	/**
	 * 用户列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(UserInfoModel searchModel, Pager pager)
			throws Exception {
		jsonResult = new HashMap<>();
		try {
			// 判断用户是否为空
			if ( userInfoMapper.getUserById(StringUtilC.getString(searchModel.getUid()))!=null){	
				DtoJsonPageData grid = new DtoJsonPageData(this);
				searchModel.setPage(pager.getPageth());// 当前yem
				searchModel.setRows(pager.getPageSize());
				List<UserInfoModel> _list = userInfoMapper.getTableDataListPage(searchModel);
				// 设定输出对象
				grid.print2JsonObj(searchModel, _list);
				pager.calcPageCount(StringUtilC.getLong(String
						.valueOf(searchModel.getRecords())));
				jsonResult.put("pager", pager);
				jsonResult.put("datas", grid.getRoot());
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println(e);
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}

	}
	/**
	 * 获取添加协同用户列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCoopUserList", method = RequestMethod.GET)
	public String getCoopUserList(UserInfoModel searchModel, Pager pager)
			throws Exception {
		jsonResult = new HashMap<>();
		try {
			// 判断用户是否为空
			if ( userInfoMapper.getUserById(StringUtilC.getString(searchModel.getUid()))!=null){	
				DtoJsonPageData grid = new DtoJsonPageData(this);
				searchModel.setPage(pager.getPageth());// 当前yem
				searchModel.setRows(pager.getPageSize());
				List<UserInfoModel> _list = userInfoMapper.getTableDataListPage(searchModel);
				// 设定输出对象
				grid.print2JsonObj(searchModel, _list);
				pager.calcPageCount(StringUtilC.getLong(String
						.valueOf(searchModel.getRecords())));
				jsonResult.put("pager", pager);
				jsonResult.put("datas", _list);
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println(e);
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}

	}
	@ResponseBody
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public String toEditUser(@Valid String id, HttpSession hs) throws Exception {
		if (ifExsitUser(hs)) {
			jsonResult = new HashMap<>();
			UserInfoModel _info = userInfoMapper.getUserById(id);
			jsonResult.put("datas", _info);
		} else {
			jsonResult.put(result, FWConstants.result_usernull);
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doUpdateUser", method = RequestMethod.POST)
	public String doUpdateUser(@Valid String uid, @Valid String id,
			HttpSession hs) throws Exception {
		try {
			if ( userInfoMapper.getUserById(StringUtilC.getString(uid))!=null){	

				jsonResult = new HashMap<>();
				UserInfoModel _userInfo = new UserInfoModel();
				_userInfo.setId(StringUtilC.getInteger(id));
				// 获取外网要更新信息
				UserInfoModel userInfo = userInfoMapper.getUserById(StringUtilC
						.getString(_userInfo.getId()));
				// 执行更新
				int ifSucess = userInfoMapper.updateUserById(userInfo);
				System.out.println("是否成功：" + ifSucess);
				// 如果成功
				if (ifSucess > 0) {
					jsonResult.put(result, FWConstants.result_success);
				}
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doUpdateUserRole", method = RequestMethod.POST)
	public String doUpdateUserRoleById(@Valid UserInfoModel _userInfo,
			HttpSession hs) throws Exception {
		jsonResult = new HashMap<>();
		try {
			if ( userInfoMapper.getUserById(StringUtilC.getString(_userInfo.getUid()))!=null){	
				int ifSucess = userInfoMapper.updateUserRoleById(_userInfo);
				System.out.println("是否成功：" + ifSucess);
				// 如果成功
				if (ifSucess > 0) {
					jsonResult.put(result, FWConstants.result_success);
				}
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}

	}
}