package cn.sh.library.pedigree.controller.sysmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.controller.RDFController;
import cn.sh.library.pedigree.sysManager.mapper.SystemMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.SystemRoleMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.MenuModel;
import cn.sh.library.pedigree.sysManager.model.SysRoleMenuModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.sysManager.service.LoginNormalService;
import cn.sh.library.pedigree.utils.HttpUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 用户登录Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/userlogin")
public class LoginController extends BaseController {
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
 
	@ResponseBody
	@RequestMapping(value = "/tologin", method = RequestMethod.GET)
	public boolean tologin(HttpSession httpSession) throws Exception {
		// 使用ModelAndView保存对象于下一页面
		// 清空Session
		httpSession.setAttribute(FW_LoginUser, null);
		boolean tologin=true;
		return tologin;
	}

	/**
	 * 登录处理
	 * 
	 * @param userLoginModel
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dologin", method = { RequestMethod.POST })
	public String doLogin(@RequestBody UserInfoModel userinfo,
			HttpSession httpSession) throws Exception {
		String durl=(String) httpSession.getAttribute("durl");
		jsonResult.put(result, durl);
		String mm= JSonUtils.toJSon(jsonResult);
		try {
			// 清空Session
			httpSession.setAttribute(FW_LoginUser, null);

			// 进行本地数据库验证判定是否插入
			boolean ifSucess = checkuseinfo(_userLoginModel);

			UserInfoModel _resultMap = userInfoMapper.getUserByShLibIdentityNo(_userLoginModel.getShLibIdentityNo());
			// 验证通过，重新查询
			if (ifSucess) {

				// 登录成功
				if (iscode) {

					// 取得权限对应的菜单信息
					SysRoleMenuModel roleMenu = systemRoleMenuMapper.getRoleOfMenu(_resultMap.getRoleId());

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
					_resultMap.setMenuList(menuList);
					// 角色名称
					_resultMap.setRoleName(DataTypeMap.getDataRoleName(_resultMap.getRoleId()));
					// 存入session
					httpSession.setAttribute(FW_LoginUser, _resultMap);
					// 存入sesion
					loginUserMap.put(httpSession.getId(), _resultMap);
				}
			}
			jsonResult.put("user", _resultMap);
			return JSonUtils.toJSon(jsonResult);
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
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
		jsonResult= new HashMap<>();
		httpSession.setAttribute(FW_LoginUser, null);
		jsonResult.put(result, FWConstants.result_success);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 获取当前页面
	 * 
	 * @param httpSession
	 */
	@ResponseBody
	@RequestMapping(value = "/localpage", method = RequestMethod.POST)
	public String locpage(HttpSession httpSession,String durl) throws Exception {
		jsonResult = new HashMap<>();
		httpSession.setAttribute("durl", null);
		String ifSuccess = "0";
		String loginurl=null;
		if (durl!=null && durl!="") {
			httpSession.setAttribute("durl", durl);
			ifSuccess="1";
		}
		String oauthurl=Constant.OAUTH_URL;
		String responsetype=Constant.RESPONSE_TYPE;
		String clientid=Constant.CLIENTID;
		String redirectturl=Constant.REDIRECT_URI;
		loginurl=oauthurl+responsetype+"&client_id="+clientid+"&redirect_uri="+redirectturl;
		jsonResult.put(result, ifSuccess);
		jsonResult.put("loginurl", loginurl);
		String mm= JSonUtils.toJSon(jsonResult);
		return mm;
	}

	
	
	
	/**
	 * 利用OAuth2.0获取用户信息
	 * 
	 * @param _loginUserInfo
	 */
	@ResponseBody
	@RequestMapping(value = "/loginer", method = { RequestMethod.POST })
	public String loginer(@Param(value = "code") String code, HttpSession httpSession) throws Exception {
		Map<String, Object> infoParams = new HashMap<String, Object>();
		infoParams.put("code"		, code);
		infoParams.put("clientId"	, Constant.CLIENTID);
		infoParams.put("redirectUri", Constant.REDIRECT_URI);
		//获取用户信息和菜单信息
        String loginInfo = HttpUtils.connPost(Constant.HTTPS_LOGIN, infoParams);
        if (loginInfo != null) {
			try {
				JSONObject userInfoJson = JSONObject.fromObject(loginInfo).getJSONObject("data");
				if (userInfoJson != null && !userInfoJson.isEmpty() && !userInfoJson.isNullObject()) {
					//删除freeText属性
					if (userInfoJson.has("freeText")) userInfoJson.discard("freeText");
					
					//将属性为menuList 转化为list集合
					List<MenuModel> menuList = null;
					JSONArray array = userInfoJson.getJSONArray("menuList");
					if (array != null && !array.isEmpty() && array.size() > 0) {
						menuList = JSONArray.toList(array, new MenuModel(), new JsonConfig());
						//删除menuList属性
						userInfoJson.discard("menuList");
					}
					
					_userLoginModel = (UserInfoModel) JSONObject.toBean(userInfoJson, UserInfoModel.class);
					_userLoginModel.setMenuList(menuList);
				}
				httpSession.setAttribute(FW_LoginUser, _userLoginModel);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

        jsonResult = new HashMap<String, Object>();
		jsonResult.put(result, (String) httpSession.getAttribute("durl"));
		// 存入sesion
		loginUserMap.put(httpSession.getId(), _userLoginModel);
		jsonResult.put("user", _userLoginModel);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 进行本地数据库验证，存在信息，直接返回，不存在信息，插入本地数据库。
	 * 
	 * @param _loginUserInfo
	 */
	private boolean checkuseinfo(UserInfoModel userinfosearch) {
		// 查询用户是否存在
		UserInfoModel temUser = null;
		// 是否成功
		boolean ifSuccess = false;
		try {
			// 查询用户是否存在
			temUser = userInfoMapper.selectCountUser(userinfosearch);
			// 如果不存在，则插入
			if (temUser == null || StringUtilC.isEmpty(temUser.getShLibIdentityNo())) {
				userinfosearch.setRoleId("1");// 默认角色1：普通用户
				int _count = userInfoMapper.insertUser(userinfosearch);
				if (_count > 0) {
					ifSuccess = true;
				}
			} else {
				// 执行更新
				userInfoMapper.updateUserById(temUser);
			
				ifSuccess = true;
			}

			return ifSuccess;
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return ifSuccess;
	}

	protected static String sign(long timestamp) {
		return DigestUtils
				.sha256Hex("617329775"
						+ timestamp
						+ "e419b304ef1e4b0ee7b37d8633ddb5da1d68236dde3d4bc17449841650213a92");
	}

}
