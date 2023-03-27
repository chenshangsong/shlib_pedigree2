package  cn.sh.library.pedigree.framework.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.controller.ModelAndViewExt;
import cn.sh.library.pedigree.framework.util.ControllerUtil;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

/**
 * 类名 : CommonInterceptor <br>
 * 机能概要 : </br> 版权所有: Copyright © 2011 TES Corporation, All Rights
 * Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class CommonInterceptor extends BaseInterceptor {

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			String preAction = StringUtil.getString(modelAndView.getModel()
					.get(FWConstant.PREV_ACTION));
			if (!preAction.isEmpty()) {
				request.setAttribute(FWConstant.PREV_ACTION, preAction);
			} else {
				if (request.getAttribute(FWConstant.PREV_ACTION) == null
						&& !("XMLHttpRequest").equals(request
								.getHeader("x-requested-with"))) {
					request.setAttribute(FWConstant.PREV_ACTION, request
							.getServletPath().replace(".do", ""));
				}
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//new add chenss 20150915
				handler = ((HandlerMethod) handler).getBean();
		if (handler instanceof BaseController) {
			BaseController bctrl = (BaseController) handler;
			Annotation[] ass = ControllerUtil.getAnnotation(bctrl);
			for(Annotation as : ass){
				if(as.annotationType().getSimpleName().equals(Transactional.class.getSimpleName())){
					if(bctrl.getDbTransaction().getStatus() != null){
						if(!bctrl.getDbTransaction().getStatus().isCompleted()){
							bctrl.getDbTransaction().commit();
						}
					}
					break;
				}
			}
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		response.setHeader("Cache-Control", "no-cache");
		BaseController bctrl = null;
		ModelAndViewExt mave = null;
		//new add chenss 20150915
		handler = ((HandlerMethod) handler).getBean();
		if (handler instanceof BaseController) {
			bctrl = (BaseController) handler;
			mave = new ModelAndViewExt();
			bctrl.init(request, response, mave);
		}
		if(isNotCareUrl(request)){
			return true;
		}
		boolean isAccessible = true;
		if (handler instanceof BaseController) {
			if(request.getRequestURI().indexOf("init.do") > 0){
				bctrl.setWebContainer(FWConstant.SESSION_REPORT_LIST, null);
				bctrl.setWebContainer(FWConstant.SESSION_REPORT_SCO, null);
				bctrl.setWebContainer(FWConstant.SESSION_REPORT_TIME, null);
				bctrl.setWebContainer(FWConstant.SESSION_REPORT_OTHER, null);
			}
			UserInfoModel sessionUser = bctrl.getUser();
			if(sessionUser != null && request.getRequestURI().endsWith("init.do")){
				if(!request.getRequestURI().endsWith("/index/init.do")){
					mave.addObject("userId", sessionUser.getUserId());
					mave.addSubFunction();
				}
			}
			
			if (ControllerUtil.hasAnnotation(bctrl, Transactional.class)) {
				bctrl.getDbTransaction().begin();
			}
		}
		return isAccessible;
	}
}