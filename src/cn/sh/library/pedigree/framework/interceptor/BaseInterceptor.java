package  cn.sh.library.pedigree.framework.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.sh.library.pedigree.framework.util.StringUtil;

/**
 * 类名 : BaseInterceptor <br>
 * 机能概要 : InterceptorHandler基类</br> 版权所有: Copyright © 2011 TES Corporation, All
 * Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {

	protected String notCareUrls;
	protected String redirctUrl;
	
	
	public String getNotCareUrls() {
		return notCareUrls;
	}

	public void setNotCareUrls(String notCareUrls) {
		this.notCareUrls = notCareUrls;
	}

	public String getRedirctUrl() {
		return redirctUrl;
	}

	public void setRedirctUrl(String redirctUrl) {
		this.redirctUrl = redirctUrl;
	}
	
	public boolean isNotCareUrl(HttpServletRequest request) throws Exception {
		for(String url : notCareUrls.split(" ")){
			if(StringUtil.isEmpty(url)){
				continue;
			}
			//所有以*开头的url，如果请求地址以此为结尾，则通过
			if(url.startsWith("*")){
				if(request.getServletPath().endsWith(url.replace("*", ""))){
					return true;
				}
			}else if(url.endsWith("*")){
				//所有以*结尾的url，如果请求地址以此为开始，则通过
				 Pattern p =  Pattern.compile("^"+url.replace("*", ""),
					      Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					    Matcher m = p.matcher(request.getServletPath());
					    while(m.find()){
					    	return true;
					    }
					    	
			}else if(request.getServletPath().endsWith(url)){
				//所有和请求地址相同，则通过
				return true;
			}
		}
		return false;
	}
}