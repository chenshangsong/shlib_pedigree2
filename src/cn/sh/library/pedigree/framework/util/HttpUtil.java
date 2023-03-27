package  cn.sh.library.pedigree.framework.util;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.model.DtoUser;

/**
 * 类名 : HttpUtil <br>
 * 机能概要 : Http请求相关操作工具</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class HttpUtil {
	
	/**
	 * 系统Session取得
	 * @param request request
	 * @return 系统Session
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getSysSession(HttpServletRequest request){
		Object obj = request.getSession().getAttribute(FWConstant.SYS_SESSION);
		if(obj != null){
			return (HashMap<String, Object>) obj;
		}else{
			return new HashMap<String, Object>();
		}
	}
	
	
	/**
	 * 系统Session清空
	 * @param request request
	 */
	public static void removeAllSession(HttpServletRequest request){
		request.getSession().setAttribute(FWConstant.SYS_SESSION, new HashMap<String, Object>());
	}
	
	/**
	 * 登录用户取得
	 * @param request
	 * @return　DtoUser
	 */
	public static DtoUser getLoginUser(HttpServletRequest request){
		return (DtoUser)getSysSession(request).get(FWConstant.LOGIN_USER);
	}
	
	/**
	 * 系统Session保存
	 * @param request
	 * @param key
	 * @param value
	 */
	public static void setSessionValue(HttpServletRequest request, String key, Object value){
		HashMap<String, Object> sysSession = getSysSession(request);
		sysSession.put(key, value);
		request.getSession().setAttribute(FWConstant.SYS_SESSION, sysSession);
	}
	
	/**
	 * 系统Session取得
	 * @param request request
	 * @param key key
	 * @param value Object
	 */
	public static Object getSessionValue(HttpServletRequest request, String key){
		HashMap<String, Object> sysSession = getSysSession(request);
		return sysSession.get(key);
	}
}
