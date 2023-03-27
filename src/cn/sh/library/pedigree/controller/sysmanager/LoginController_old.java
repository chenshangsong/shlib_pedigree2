package cn.sh.library.pedigree.controller.sysmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.RDFController;
import cn.sh.library.pedigree.framework.util.PreloadUserList;
import cn.sh.library.pedigree.sysManager.mapper.SystemMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.SystemRoleMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.MenuModel;
import cn.sh.library.pedigree.sysManager.model.SysRoleMenuModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.sysManager.service.LoginNormalService;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

/**
 * 用户登录Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/userloginOld")
public class LoginController_old extends BaseController {
	@Resource
	private LoginNormalService loginNormalService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private SystemRoleMenuMapper systemRoleMenuMapper;
	@Autowired
	private SystemMenuMapper systemMenuMapper;

	private Logger logger = Logger.getLogger(RDFController.class);
	/**
	 * 用户登录结果信息
	 */
	private UserLoginResultModel userLoginResultModel = new UserLoginResultModel();

	@ResponseBody
	@RequestMapping(value = "/tologin", method = RequestMethod.GET)
	public ModelAndView tologin(HttpSession httpSession) throws Exception {
		// 使用ModelAndView保存对象于下一页面
		// 清空Session
		httpSession.setAttribute(FW_LoginUser, null);
		modelAndView.setViewName("login/login");
		return modelAndView;
	}

	/**
	 * 登录处理
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dologin", method = { RequestMethod.POST })
	public String doLogin(@RequestBody UserLoginModel userLoginModel,
			HttpSession httpSession) throws Exception {
		try {
			// 清空Session
			httpSession.setAttribute(FW_LoginUser, null);
			// 外网登录获取信息
			 userLoginResultModel = loginNormalService.login(userLoginModel);

			/*userLoginResultModel.setUid("chen_emperor");
			userLoginResultModel.setResultCode("0");
			userLoginResultModel.setPwd("chen360121");
			userLoginResultModel.getParmResult().setShLibIdentityNo(
					"410221198807065274");*/

			// 进行本地数据库验证判定是否插入
			boolean ifSucess = checkUser(userLoginResultModel);
			UserInfoModel _userInfo = new UserInfoModel();
			// 验证通过，重新查询
			if (ifSucess) {
				// 根据外网信息，得到本地数据库用户信息
				_userInfo = getSysUserInfo(userLoginResultModel);
				// 登录成功
				if ("0".equals(userLoginResultModel.getResultCode())) {
					// 取得权限对应的菜单信息
					SysRoleMenuModel roleMenu = systemRoleMenuMapper
							.getRoleOfMenu(_userInfo.getRoleId());

					// 取得菜单详细信息
					List<MenuModel> menuList = new ArrayList<MenuModel>();
					String[] menus = roleMenu.getMenuId().split(",");
					for (String menuId : menus) {
						MenuModel menu = systemMenuMapper.getMenuDetail(menuId);
						menuList.add(menu);
					}
					Collections.sort(menuList, new Comparator<MenuModel>() {
						public int compare(MenuModel arg0, MenuModel arg1) {
							return arg0.getMenuSort().compareTo(
									arg1.getMenuSort());
						}
					});
					_userInfo.setMenuList(menuList);
					// 角色名称
					_userInfo.setRoleName(DataTypeMap.getDataRoleName(_userInfo
							.getRoleId()));
					// 存入session
					httpSession.setAttribute(FW_LoginUser, _userInfo);
					//存入sesion
					loginUserMap.put(httpSession.getId(), _userInfo);
				}
			}
			jsonResult.put(result, userLoginResultModel);
			jsonResult.put("user", _userInfo);
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return null;
	}

	/**
	 * 主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView doHome(HttpSession httpSession) throws Exception {
		// 获取session
		modelAndView.addObject("userInfo", getUser(httpSession));
		modelAndView.setViewName("test/myjsp");
		return modelAndView;
	}

	/**
	 * 退出
	 * 
	 * @param httpSession
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(HttpSession httpSession) throws Exception {
		// 清空Session
		httpSession.setAttribute(FW_LoginUser, null);
		jsonResult.put(result, FWConstants.result_success);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 利用外网查询用户，验证通过，进行本地数据库验证，存在信息，直接返回，不存在信息，插入本地数据库。
	 * 
	 * @param _loginUserInfo
	 */
	private boolean checkUser(UserLoginResultModel _loginUserInfo) {
		UserInfoModel _searchInfo = new UserInfoModel();
		// 查询用户是否存在
		UserInfoModel temUser  = null;
		// 是否成功
		boolean ifSuccess = false;
		try {
			// 外网验证通过
			if (_loginUserInfo != null
					&& "0".equals(_loginUserInfo.getResultCode())) {
				// 身份证号
				_searchInfo.setShLibIdentityNo(_loginUserInfo.getParmResult()
						.getShLibIdentityNo());
				// 查询用户是否存在
				 temUser  = userInfoMapper.selectCountUser(_searchInfo);
				// 如果不存在，则插入
				if (temUser==null ||StringUtilC.isEmpty(temUser.getShLibIdentityNo())) {
					// 实体转换
					CommonUtils.convertLoginResultToUserInfo(_loginUserInfo,
							_searchInfo);
					_searchInfo.setRoleId("1");// 默认角色1：普通用户
					int _count = userInfoMapper.insertUser(_searchInfo);
					if (_count > 0) {
						PreloadUserList.getInstance().reloadInfo();
						ifSuccess = true;
					}
				} else {
					//更新用户
					//实体转换
					CommonUtils.convertLoginResultToUserInfo(_loginUserInfo,temUser);
					//执行更新
					 userInfoMapper.updateUserById(temUser);
					ifSuccess = true;
				}
			}
			return ifSuccess;
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return ifSuccess;
	}

	/**
	 * 站内用户信息获取
	 * 
	 * @param _model
	 * @return
	 */
	private UserInfoModel getSysUserInfo(UserLoginResultModel _model) {
		try {
			UserInfoModel _searchuserinfo = new UserInfoModel();
			_searchuserinfo.setUserId(_model.getUid());
			_searchuserinfo.setShLibIdentityNo(_model.getParmResult()
					.getShLibIdentityNo());
			_searchuserinfo.setShLibAskNo(_model.getParmResult()
					.getShLibAskNo());
			UserInfoModel _resultMap = userInfoMapper
					.getUserByInfo(_searchuserinfo);
			if (_resultMap != null) {
				return _resultMap;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new UserInfoModel();
	}

}
