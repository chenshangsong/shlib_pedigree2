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
      /* String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();  
       System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);  
       String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();  
       System.out.println("协议名：//域名="+url2);  
       System.out.println("获取项目名="+request.getContextPath());  
       System.out.println("获取参数="+request.getQueryString());  
       System.out.println("获取全路径="+request.getRequestURL());  */
       Constant.ProjectRealPath=request.getSession().getServletContext().getRealPath("/").replace("\\", "/")+"/" + "WEB-INF";
   }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
/*    HttpServletResponse response = ServletActionContext.getResponse();  
    HttpServletRequest request = ServletActionContext.getRequest();  
String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();  
System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);  
String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();  
System.out.println("协议名：//域名="+url2);  


System.out.println("获取项目名="+request.getContextPath());  
System.out.println("获取参数="+request.getQueryString());  
System.out.println("获取全路径="+request.getRequestURL());  
request.getSession().getServletContext().getRealPath("/");//获取web项目的路径  
this.getClass().getResource("/").getPath()//获取类的当前目录  
*/
}
