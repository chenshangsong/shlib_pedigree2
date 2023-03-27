package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.List;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class UserInfoModel extends DtoJsonPageData implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1109887885529913725L;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户密码
	 */
	private String userPwd;
	/**
	 * 卡号
	 */
	private String shLibAskNo;
	/**
	 * 有效证件类型ID
	 */
	private String shLibIdentityNo;
	/**
	 * 性别
	 */
	private String shLibSex;
	/**
	 * 借阅号
	 */
	private String shLibBorrower;
	/**
	 * 角色ID
	 */
	private String roleId;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 电话
	 */
	private String mobile;
	/**
	 * 邮箱
	 */
	private String mail;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * uid
	 */
	private String uid;
	/**
	 * 菜单信息
	 */
	private List<MenuModel> menuList;
	/**
	 * 系统id
	 */
	private String systemId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getShLibAskNo() {
		return shLibAskNo;
	}
	public void setShLibAskNo(String shLibAskNo) {
		this.shLibAskNo = shLibAskNo;
	}
	public String getShLibIdentityNo() {
		return shLibIdentityNo;
	}
	public void setShLibIdentityNo(String shLibIdentityNo) {
		this.shLibIdentityNo = shLibIdentityNo;
	}
	public String getShLibSex() {
		return shLibSex;
	}
	public void setShLibSex(String shLibSex) {
		this.shLibSex = shLibSex;
	}
	public String getShLibBorrower() {
		return shLibBorrower;
	}
	public void setShLibBorrower(String shLibBorrower) {
		this.shLibBorrower = shLibBorrower;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public List<MenuModel> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<MenuModel> menuList) {
		this.menuList = menuList;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
