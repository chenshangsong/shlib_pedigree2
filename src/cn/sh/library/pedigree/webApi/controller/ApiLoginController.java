package cn.sh.library.pedigree.webApi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.annotations.Param;
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
import cn.sh.library.pedigree.common.RoleGroup;
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
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.HttpUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户登录Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi/login")
public class ApiLoginController extends BaseController {
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

	UserInfoModel _userLoginModel = new UserInfoModel();

	boolean iscode = false;
	private UserLoginResultModel userLoginResultModel = new UserLoginResultModel();

	@ResponseBody
	@RequestMapping(value = "/tologin", method = RequestMethod.GET)
	public ModelAndView tologin(HttpSession httpSession) throws Exception {
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
	@RequestMapping(value = "/getLogin", method = { RequestMethod.GET })
	public String getLogin(UserInfoModel userinfo) throws Exception {
		try {
			jsonResult = new HashMap<>();
			// 清空Session
			// httpSession.setAttribute(FW_LoginUser, null);
			// 进行本地数据库验证判定是否插入
			UserInfoModel checkUser = checkuseinfo(userinfo);
			UserInfoModel _resultMap = userInfoMapper
					.getUserByShLibIdentityNo(userinfo.getShLibIdentityNo());
			// 验证通过，重新查询
			if (checkUser != null && !StringUtilC.isEmpty(checkUser.getId())) {
				// 设置权限菜单
				SetMenuList(_resultMap);
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			jsonResult.put("data", _resultMap);
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 登录处理:webApi使用
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getlogin2", method = { RequestMethod.GET })
	public String getlogin2(UserLoginModel userLoginModel,
			HttpSession httpSession) throws Exception {
		try {
			jsonResult = new HashMap<>();
			// 清空Session
			httpSession.setAttribute(FW_LoginUser, null);
			// 外网登录获取信息
			userLoginResultModel = loginNormalService.login(userLoginModel);
			// 进行本地数据库验证判定是否插入
			boolean ifSucess = checkUser(userLoginResultModel);
			UserInfoModel _userInfo = new UserInfoModel();
			// 验证通过，重新查询
			if (ifSucess) {
				// 根据外网信息，得到本地数据库用户信息
				_userInfo = getSysUserInfo(userLoginResultModel);
				// 登录成功
				if ("0".equals(userLoginResultModel.getResultCode())) {
					// 设置权限菜单
					SetMenuList(_userInfo);
					// 存入session
					httpSession.setAttribute(FW_LoginUser, _userInfo);
					// 存入sesion
					loginUserMap.put(httpSession.getId(), _userInfo);
				}
				jsonResult.put("datas", _userInfo);
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			jsonResult.put(result, "-1");
			return JSonUtils.toJSon(jsonResult);
		}
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
			jsonResult = new HashMap<>();
			// 清空Session
			httpSession.setAttribute(FW_LoginUser, null);
			// 外网登录获取信息
			userLoginResultModel = loginNormalService.login(userLoginModel);
			// 进行本地数据库验证判定是否插入
			boolean ifSucess = checkUser(userLoginResultModel);
			UserInfoModel _userInfo = new UserInfoModel();
			// 验证通过，重新查询
			if (ifSucess) {
				// 根据外网信息，得到本地数据库用户信息
				_userInfo = getSysUserInfo(userLoginResultModel);
				// 登录成功
				if ("0".equals(userLoginResultModel.getResultCode())) {
					// 设置权限菜单
					SetMenuList(_userInfo);
					// 存入session
					httpSession.setAttribute(FW_LoginUser, _userInfo);
					// 存入sesion
					loginUserMap.put(httpSession.getId(), _userInfo);
				}
			}
			jsonResult.put(result, userLoginResultModel);
			jsonResult.put("user", _userInfo);
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 设置权限菜单
	 * 
	 * @param _userInfo
	 */
	private void SetMenuList(UserInfoModel _userInfo) {
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
				return arg0.getMenuSort().compareTo(arg1.getMenuSort());
			}
		});
		_userInfo.setMenuList(menuList);
		// 角色名称
		_userInfo
				.setRoleName(DataTypeMap.getDataRoleName(_userInfo.getRoleId()));
	}

	/**
	 * 退出
	 * 
	 * @param httpSession
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(HttpSession httpSession) throws Exception {
		// 清空Session
		jsonResult = new HashMap<>();
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
		UserInfoModel temUser = null;
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
				temUser = userInfoMapper.selectCountUser(_searchInfo);
				// 如果不存在，则插入
				if (temUser == null
						|| StringUtilC.isEmpty(temUser.getShLibIdentityNo())) {
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
					// 更新用户
					// 实体转换
					CommonUtils.convertLoginResultToUserInfo(_loginUserInfo,
							temUser);
					// 执行更新
					userInfoMapper.updateUserById(temUser);
					ifSuccess = true;
				}
			}
			return ifSuccess;
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
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

	/**
	 * 进行本地数据库验证，存在信息，直接返回，不存在信息，插入本地数据库。
	 * 
	 * @param _loginUserInfo
	 */
	private UserInfoModel checkuseinfo(UserInfoModel userinfosearch) {
		// 查询用户是否存在
		UserInfoModel temUser = null;
		// 是否成功
		try {
			// 查询用户是否存在
			temUser = userInfoMapper.selectCountUser(userinfosearch);
			// 如果不存在，则插入
			if (temUser == null
					|| StringUtilC.isEmpty(temUser.getShLibIdentityNo())) {

				userinfosearch.setRoleId("1");// 默认角色1：普通用户
				int _count = userInfoMapper.insertUser(userinfosearch);
				if (_count > 0) {
					PreloadUserList.getInstance().reloadInfo();
				}
			} else {
				// 执行更新
				userInfoMapper.updateUserById(temUser);
			}
			if (temUser == null) {
				// 重新查询赋值
				temUser = userInfoMapper.selectCountUser(userinfosearch);
			}

		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return temUser;
	}

	/**
	 * 利用OAuth2.0 Code 同步本地用户信息
	 * 
	 * @param _loginUserInfo
	 */
	@ResponseBody
	@RequestMapping(value = "/getLoginByToken", method = { RequestMethod.GET })
	public String getLoginByToken(@Param(value = "token") String token)
			throws Exception {
		try {
			UserInfoModel _userInfo = new UserInfoModel();
			jsonResult = new HashMap<>();
			Map<String, Object> userinfoParams = new HashMap<String, Object>();
			Long timestamp = new Date().getTime();
			userinfoParams.put("accessToken", token);
			userinfoParams.put("clientId", "734572174");
			// userinfoParams.put("clientId", "617329775"); login_old
			userinfoParams.put("timestamp", timestamp);
			userinfoParams.put("sign", sign(timestamp));
			String uesrResponse = HttpUtils.connGet(
					"https://passport.library.sh.cn:4430/rs/api/v1/info",
					userinfoParams);
			// 同步本地库用户信息，并查出
			UserInfoModel _userInfoMap = null;
			if (uesrResponse != null) {
				JSONObject infoJsonObj = JSONObject.fromObject(uesrResponse);
				JSONObject infoResultStatus = infoJsonObj
						.getJSONObject("resultStatus");
				int infoCode = infoResultStatus.getInt("code");
				if (infoCode == 1) {
					JSONObject infoResultValue = infoJsonObj
							.getJSONObject("resultValue");
					JSONArray shlibCard = infoResultValue
							.getJSONArray("shlibCardVoList");
					_userInfo.setUserId(infoResultValue.getString("username"));
					_userInfo.setRoleId(RoleGroup.normal.getGroup());
					_userInfo.setUserName(infoResultValue.getString("nameCn"));
					_userInfo.setShLibIdentityNo(infoResultValue
							.getString("identityNo"));
					_userInfo.setShLibSex(infoResultValue.getString("sex"));
					if (infoResultStatus.get("mobile") != null) {
						_userInfo
								.setMobile(infoResultValue.getString("mobile"));
					}
					if (infoResultStatus.get("shlibBorrower") != null) {
						_userInfo.setShLibBorrower(infoResultValue
								.getString("shlibBorrower"));
					}
					_userInfo.setMail(infoResultValue.getString("email"));
					if (shlibCard != null && shlibCard.size() > 0) {
						JSONObject obj = (JSONObject) shlibCard.get(0);
						_userInfo.setShLibAskNo(obj.getString("shlibCardNo"));
					}
					// 同步本地库用户信息，并查出
					_userInfoMap = checkuseinfo(_userInfo);
					if (_userInfoMap != null
							&& !StringUtilC.isEmpty(_userInfoMap.getId())) {
						jsonResult.put(result, FWConstants.result_success);
						// 设置权限菜单
						SetMenuList(_userInfoMap);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			jsonResult.put("data", _userInfoMap);
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
		}
		return JSonUtils.toJSon(jsonResult);

	}

	protected static String sign(long timestamp) {
		return DigestUtils
				.sha256Hex("734572174"
						+ timestamp
						+ "0d8f039d3e8155bffa22c5a020644104ebe8f572eac42f104010b12d0da25255");
		/*
		 * return DigestUtils .sha256Hex("617329775" + timestamp +
		 * "e419b304ef1e4b0ee7b37d8633ddb5da1d68236dde3d4bc17449841650213a92");
		 */

	}
}
