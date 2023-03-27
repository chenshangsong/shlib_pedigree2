package  cn.sh.library.pedigree.framework.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.commom.MsgId;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.exception.SysException;
import cn.sh.library.pedigree.framework.model.BaseDto;

public class SessionTimeOutInterceptor extends BaseInterceptor {

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
			if (request.getSession().getAttribute(FWConstant.SYS_SESSION) == null) {
				isAccessible = false;
				if (!("XMLHttpRequest").equals(request.getHeader("x-requested-with"))) {
					response.sendRedirect(redirctUrl+"?code=EMSG9003");
				} else {
					try {
						ObjectMapper mapper = new ObjectMapper();  
						List<BaseDto> list = new ArrayList<BaseDto>();
						list.add(new SysException(MsgId.E9003).getErrDto());
						mapper.writeValue(response.getOutputStream(),list);  
					} catch (Exception e) {
					} 
				}
			}
		return isAccessible;
	}
}
