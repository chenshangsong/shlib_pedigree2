package cn.sh.library.pedigree.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * Created by Yi on 2014/10/28 0028.
 */
@Component
public class FrontInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("ctx", request.getContextPath());
        //端口号
        String prot = StringUtilC.getString(request.getLocalPort());
       if(!StringUtilC.isEmpty(prot)){
    	   prot=":"+prot;
       }
       Constant.ProjectPath= request.getScheme()+"://"+ request.getServerName()+prot+request.getContextPath();
       Constant.ProjectRealPath=request.getSession().getServletContext().getRealPath("/").replace("\\", "/")+"/" + "WEB-INF";
   }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
