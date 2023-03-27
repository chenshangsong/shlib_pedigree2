package cn.sh.library.pedigree.webServices.model;

public class UserLoginModel {
	/**
	 * 用户ID
	 */
	protected String uid;
	/**
	 * 用户密码
	 */
	protected String pwd;
	/**
	 * 用户类型
	 */
	protected String userType;

	/**
	 * 密码类型（常规，MD5）
	 */
	protected String pwdType;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPwdType() {
		return pwdType;
	}

	public void setPwdType(String pwdType) {
		this.pwdType = pwdType;
	}

}
