package cn.sh.library.pedigree.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.framework.util.PreloadDoiList;
import cn.sh.library.pedigree.sysManager.model.DoiSysModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;


public class BaseController{
	/**
	 * 日志
	 */
	private static Logger BaseControllerlogger = Logger.getLogger(BaseController.class);
	public static HttpSession httpSession;
	public static String FW_LoginUser="userSession";
	public static  Map<String,Map> workMap = new HashMap<String, Map>();

    
	public Map<String, Object> jsonResult = new HashMap<>();
	/**
	 * 处理结果
	 */
	public static String result = "result";
	// 使用ModelAndView保存对象于下一页面
	public static ModelAndView modelAndView = new ModelAndView();
	public String stringResult;
	public UserInfoModel loginUser;
	public Map<String,UserInfoModel> loginUserMap = new HashMap<String,UserInfoModel>();
	public Integer redis_maxVistCount = 30;//次
	public Integer redis_timeOut = 1;//分钟


	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, Object> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public UserInfoModel getUser(HttpSession httpSession) {
		return  (UserInfoModel)(httpSession.getAttribute(FW_LoginUser));
	}
	/**
	 * 是否开放 
	 * @param doi
	 * @return
	 */
	public static Boolean getDoiOpenFlg (String doi) {
		try {
			for (DoiSysModel doiSysModel : PreloadDoiList.getInstance().getDoiList()) {
				if(doiSysModel.getDoi().equals(doi)){
					return true;
				}
			}
		} catch (Exception e) {
			BaseControllerlogger.info("BaseControllerlogger-getDoiOpenFlg错误：" + DateUtilC.getNowDateTime()+ "----"+e 
					);
		}
		return false;
	}

	public void setLoginUser(UserInfoModel loginUser) {
		this.loginUser = loginUser;
	}

	public Map<String, UserInfoModel> getLoginUserMap() {
		return loginUserMap;
	}

	public void setLoginUserMap(Map<String, UserInfoModel> loginUserMap) {
		this.loginUserMap = loginUserMap;
	}
	public boolean ifExsitUser(HttpSession hs) {
		UserInfoModel userInfo = getUser(hs);
		if (userInfo == null || StringUtilC.isEmpty(userInfo.getId())) {
			return false;
		}
		return true;
	}
}
