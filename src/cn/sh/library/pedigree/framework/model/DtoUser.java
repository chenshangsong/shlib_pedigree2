package  cn.sh.library.pedigree.framework.model;


/**
 * 类名 : DtoUser <br>
 * 机能概要 : </br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class DtoUser extends DtoJsonPageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -207382037574663996L;
	
	private String userId;
//	private String loginIp;
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
//	public String getLoginIp() {
//		if(loginIp == null){
//			return "";
//		}
//		return loginIp;
//	}
//	public void setLoginIp(String loginIp) {
//		this.loginIp = loginIp;
//	}
}
