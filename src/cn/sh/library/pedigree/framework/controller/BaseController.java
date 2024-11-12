package cn.sh.library.pedigree.framework.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.database.DbTransaction;
import cn.sh.library.pedigree.framework.interceptor.WebContainer;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 类名 : BaseController <br>
 * 机能概要 : </br> 版权所有: Copyright © 2011 TES Corporation, All Rights
 * Reserved.</br>
 * 
 * @author fanhoucheng
 * @version 1.0
 * 
 */
@Controller
public abstract class BaseController extends AbstractController {
	@Resource
	protected DbTransaction dbTransaction;
	public static String FW_LoginUser="userSession";
	protected ModelAndViewExt mave;

	protected HttpServletRequest request = null;

	protected HttpServletResponse response = null;
	
	public static HttpResponse httpResponse;
	public static HttpRequest httpRequest;
	public Map<String, Object> jsonResult = new HashMap<>();
	// 使用ModelAndView保存对象于下一页面
	public static ModelAndView modelAndView = new ModelAndView();
	public String stringResult;
	
	public Integer redis_maxVistCount = 30;//次
	public Integer redis_timeOut = 1;//分钟
	/**
	 * 处理结果
	 */
	public static String result = "result";

	/* end */
	public void init(HttpServletRequest req, HttpServletResponse resp,
			ModelAndViewExt mave) {
		request = req;
		response = resp;
		this.mave = mave;
		this.mave.setBaseController(this);
	}

	public WebContainer getWebContainer() {
		Object obj = request.getSession().getAttribute(FWConstant.SYS_SESSION);
		if (obj == null) {
			return new WebContainer();
		} else {
			return (WebContainer) obj;
		}
	}

	public Object getWebContainer(String key) {
		return getWebContainer().getContainerMap().get(key);
	}

	public UserInfoModel getUser() {
		return (UserInfoModel) getWebContainer().getContainerMap().get(
				FWConstant.LOGIN_USER);
	}

	public void setWebContainer(String key, Object obj) {
		WebContainer wc = getWebContainer();
		Map<String, Object> map = wc.getContainerMap();
		map.put(key, obj);
		wc.setContainerMap(map);
		request.getSession().setAttribute(FWConstant.SYS_SESSION, wc);
	}

	public void setMaveSession(String key, Object ifNullObj) {
		Object obj = getWebContainer(key);
		if (obj == null) {
			obj = ifNullObj;
		}
		mave.addObject(key, obj);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		return null;
	}

	public ModelAndViewExt getMave() {
		return mave;
	}

	public void setMave(ModelAndViewExt mave) {
		this.mave = mave;
	}

	public DbTransaction getDbTransaction() {
		return dbTransaction;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest _request) {
		request = _request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse _response) {
		response = _response;
	}

	public static HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public static void setHttpResponse(HttpResponse httpResponse) {
		BaseController.httpResponse = httpResponse;
	}

	public static HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public static void setHttpRequest(HttpRequest httpRequest) {
		BaseController.httpRequest = httpRequest;
	}

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, Object> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public static ModelAndView getModelAndView() {
		return modelAndView;
	}

	public static void setModelAndView(ModelAndView modelAndView) {
		BaseController.modelAndView = modelAndView;
	}

	public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
	}

	public static String getResult() {
		return result;
	}

	public static void setResult(String result) {
		BaseController.result = result;
	}

	public void setDbTransaction(DbTransaction dbTransaction) {
		this.dbTransaction = dbTransaction;
	}

	public UserInfoModel getUser(HttpSession httpSession) {
		return  (UserInfoModel)(httpSession.getAttribute(FW_LoginUser));
	}

	public boolean ifExsitUser(HttpSession hs) {
		UserInfoModel userInfo = getUser(hs);
		if (userInfo == null || StringUtilC.isEmpty(userInfo.getId())) {
			return false;
		}
		return true;
	}

}
