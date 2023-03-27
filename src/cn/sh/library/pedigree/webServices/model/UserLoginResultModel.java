package cn.sh.library.pedigree.webServices.model;

import java.util.Date;

public class UserLoginResultModel {
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
	/**
	 * 用户昵称
	 */
	protected String userNickname;
	/**
	 * 用户姓名
	 */
	protected String userName;

	/**
	 * 访问状态
	 */
	protected String askno;

	/**
	 * 访问结果代码
	 */
	protected String resultCode;

	/**
	 * 参数数量
	 */
	protected String totalParmNum;
	/**
	 * 访问时间
	 */
	protected Date askTime;
	/**
	 * 访问结果
	 */
	protected String info;

	/**
	 * 返回参数
	 */
	protected UserLoginParmResult ParmResult = new UserLoginParmResult();
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

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAskno() {
		return askno;
	}

	public void setAskno(String askno) {
		this.askno = askno;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Date getAskTime() {
		return askTime;
	}

	public void setAskTime(Date askTime) {
		this.askTime = askTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTotalParmNum() {
		return totalParmNum;
	}

	public void setTotalParmNum(String totalParmNum) {
		this.totalParmNum = totalParmNum;
	}

	public UserLoginParmResult getParmResult() {
		return ParmResult;
	}

	public void setParmResult(UserLoginParmResult parmResult) {
		ParmResult = parmResult;
	}

}
