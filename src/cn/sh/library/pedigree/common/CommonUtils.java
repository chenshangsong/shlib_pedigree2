package cn.sh.library.pedigree.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

public class CommonUtils {
	public static HttpSession httpSession;
	public static String userSession = "userSession";
	public static UserInfoModel loginUser = new UserInfoModel();
	public static HttpResponse httpResponse;
	public static HttpRequest httpRequest;
	public static Map<String, Integer> countmap = new HashMap<String, Integer>();
	public static Map<String, String> timemap = new HashMap<String, String>();
	
	//分面缓存
	public static String cache_FacetJsonString;
	
	/**
	 * 用户登录信息转换为实体model
	 * 
	 * @param _loginUserInfo
	 * @return
	 */
	public static UserInfoModel convertLoginResultToUserInfo(
			UserLoginResultModel _loginUserInfo, UserInfoModel _info) {
		if (_loginUserInfo != null) {
			_info.setUserId(_loginUserInfo.getUid());
			_info.setUserName(_loginUserInfo.getUserName());
			_info.setUserPwd(_loginUserInfo.getPwd());
			_info.setMail(_loginUserInfo.getParmResult().getMail());
			_info.setShLibAskNo(_loginUserInfo.getParmResult().getShLibAskNo());
			_info.setShLibBorrower(_loginUserInfo.getParmResult()
					.getShLibBorrower());
			_info.setShLibIdentityNo(_loginUserInfo.getParmResult()
					.getShLibIdentityNo());
			_info.setShLibSex(_loginUserInfo.getParmResult().getShLibSex());
		}
		return _info;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		CommonUtils.httpSession = httpSession;
	}

	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpResponse httpResponse) {
		CommonUtils.httpResponse = httpResponse;
	}

	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpRequest httpRequest) {
		CommonUtils.httpRequest = httpRequest;
	}

	/**
	 * 清空用户
	 */
	public static void clearUserSeesion() {
		CommonUtils.httpSession.setAttribute(CommonUtils.userSession, null);
	}
}
