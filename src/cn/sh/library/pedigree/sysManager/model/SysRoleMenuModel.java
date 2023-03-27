package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class SysRoleMenuModel extends DtoJsonPageData implements Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleId;
	private String menuId;
	private String CREATED_ON;
	private String CREATED_BY;
	private String UPDATED_ON;
	private String UPDATED_BY;
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getCREATED_ON() {
		return CREATED_ON;
	}
	public void setCREATED_ON(String cREATED_ON) {
		CREATED_ON = cREATED_ON;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public String getUPDATED_ON() {
		return UPDATED_ON;
	}
	public void setUPDATED_ON(String uPDATED_ON) {
		UPDATED_ON = uPDATED_ON;
	}
	public String getUPDATED_BY() {
		return UPDATED_BY;
	}
	public void setUPDATED_BY(String uPDATED_BY) {
		UPDATED_BY = uPDATED_BY;
	}
}