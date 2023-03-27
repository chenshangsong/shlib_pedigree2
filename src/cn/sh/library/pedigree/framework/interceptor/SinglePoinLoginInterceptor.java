package  cn.sh.library.pedigree.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.exception.SingleLoginException;
import cn.sh.library.pedigree.framework.util.ControllerUtil;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

public class SinglePoinLoginInterceptor extends BaseInterceptor {

	private String message;
	private boolean keepUrl;
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (handler instanceof BaseController) {
		}

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if(isNotCareUrl(request)){
			return true;
		}
		boolean isAccessible = true;
		if (handler instanceof BaseController) {
			BaseController bctrl = (BaseController) handler;
			// 只有home.do的时候
			if (!"1".equals(request.getParameter(FWConstant.FORCE_LOGIN))) {
				UserInfoModel sessionUser = bctrl.getUser();
//				DtoUser user = PreloadUserBean.getInstance().getEmployee(sessionUser);
	
				String nowIp = request.getRemoteAddr();
				if("0:0:0:0:0:0:0:1".equals(nowIp)){
					return true;
				}
//				String oldIp = user.getLoginIp();
				// 当前请求IP和用户登录IP不一致的情况
				String oldUserIp = null;//chenss;PreloadUserBean.getInstance().getUserIp(sessionUser.getUserId());
				if (!StringUtil.isEmpty(oldUserIp) && !nowIp.equals(oldUserIp)) {
					if (ControllerUtil.isReturnModelAndView(bctrl)) {
						MethodNameResolver mnresolver = new InternalPathMethodNameResolver();
						//home.do的时候（比如用户复制地址）不把请求地址保存session
						if(keepUrl && !mnresolver.getHandlerMethodName(request).endsWith("home")){
							String path = ControllerUtil.getRequestPath(bctrl);
							bctrl.setWebContainer(FWConstant.AFTER_LOGIN_PATH, path);
						}
						response.sendRedirect(redirctUrl);
						isAccessible = false;
					} else {
						ControllerUtil.printException(bctrl,
								new SingleLoginException(message));
						isAccessible = false;
					}
				}

			}
		}
		return isAccessible;
	} 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isKeepUrl() {
		return keepUrl;
	}

	public void setKeepUrl(boolean keepUrl) {
		this.keepUrl = keepUrl;
	}
}
