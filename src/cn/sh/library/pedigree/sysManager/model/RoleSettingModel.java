package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.Map;

public class RoleSettingModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleId;
	private Map<String, String> roles;
	private String menuId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}
