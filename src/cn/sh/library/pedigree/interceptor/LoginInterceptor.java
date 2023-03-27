package cn.sh.library.pedigree.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.common.RoleGroup;
import cn.sh.library.pedigree.framework.util.HttpUtil;
import cn.sh.library.pedigree.framework.util.PreloadUserList;
import cn.sh.library.pedigree.fullContentLink.FullLink;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

public class LoginInterceptor implements HandlerInterceptor {

	private static final String LOGIN_URL = "/userlogin/tologin";
	// 不拦截的URL
	private static final String[] noCatchList = { "page/about", "page/contact",
			"dologin", "tologin", "ipwarning/error", "service", "rdfdata",
			"home", "map/place", "view", "vocab", "webapi","full-img" };

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object handler) throws Exception {
		HttpSession session = req.getSession(true);
		// 访问用户的IP
		String IP = FullLink.getIpAddr(req);
		HttpUtil.setSessionValue(req, "userIp", IP);
		// 访问页面路径
		String url = req.getRequestURI().toString();
		
		// 临时加(过滤所有)||url.contains("service")
		for (String string : noCatchList) {
			// if (url.contains(string) && !url.contains("manager")) { return true; }
			if (url.contains(string)) {
				return true;
			}
		}
		
		// 新家谱-"数据维护" ——> 老家谱-"数据维护", 跳转链接: jpv1.library.sh.cn/jp/dataManager/datalist 20220819 chenss 注释
//		String userId =req.getHeader("userId");
//		if(url.contains("/dataManager/datalist") && userId!=null && !"".equals(userId)){
//			//获取用户登录id
//			UserInfoModel userInfoModel = PreloadUserList.getUserById(userId);
//			session.setAttribute(CommonUtils.userSession, userInfoModel);
//		}
		
		// 从session 里面获取用户名的信息
		Object userSession = session.getAttribute(CommonUtils.userSession);
		// 判断如果没有取到用户信息，就跳转到登陆页面，提示用户进行登陆
		if (userSession == null || "".equals(userSession.toString())) {
			// return true;
			// 跳转到登录页面
			// session.setAttribute("durl", null);
			// String durl=(String) session.getAttribute("durl");
			// if (durl==null) {
			// durl="http://gj.library.sh.cn/gjxz/index";
			// }
			session.setAttribute("durl", url);
			// res.sendRedirect(req.getContextPath() + LOGIN_URL);
			String oauthurl = Constant.OAUTH_URL;
			String responsetype = Constant.RESPONSE_TYPE;
			String clientid = Constant.CLIENTID;
			String redirectturl = Constant.REDIRECT_URI;
			String loginurl = oauthurl + responsetype + "&client_id="
					+ clientid + "&redirect_uri=" + redirectturl;
			res.sendRedirect(loginurl);
		} else {
			UserInfoModel _userModel = ((UserInfoModel) userSession);
			String[] noCatchListAdminList = { "systemSetting",
					"userInfo/userList", "userInfo/toEditUser" };
			if (url.toLowerCase().contains("manager")) {
				if (!RoleGroup.admin.getGroup().equals(_userModel.getRoleId())
						&& !RoleGroup.zj.getGroup().equals(
								_userModel.getRoleId())) {
					session.setAttribute("durl", url);
					// res.sendRedirect(req.getContextPath() + LOGIN_URL);
					String oauthurl = Constant.OAUTH_URL;
					String responsetype = Constant.RESPONSE_TYPE;
					String clientid = Constant.CLIENTID;
					String redirectturl = Constant.REDIRECT_URI;
					String loginurl = oauthurl + responsetype + "&client_id="
							+ clientid + "&redirect_uri=" + redirectturl;
					res.sendRedirect(loginurl);
					return true;
				}
			}
			for (String string : noCatchListAdminList) {
				if (url.contains(string)) {
					if (!_userModel.getRoleId().equals(
							RoleGroup.admin.getGroup())) {
						session.setAttribute("durl", url);
						// res.sendRedirect(req.getContextPath() + LOGIN_URL);
						String oauthurl = Constant.OAUTH_URL;
						String responsetype = Constant.RESPONSE_TYPE;
						String clientid = Constant.CLIENTID;
						String redirectturl = Constant.REDIRECT_URI;
						String loginurl = oauthurl + responsetype + "&client_id="
								+ clientid + "&redirect_uri=" + redirectturl;
						res.sendRedirect(loginurl);
						return true;
					}
				}
				// 如果是权限管理页面，则进行是否是管理员验证

			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object arg2, Exception arg3)
			throws Exception {
	}

}
