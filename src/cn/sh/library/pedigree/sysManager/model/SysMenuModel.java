package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class SysMenuModel extends DtoJsonPageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String menuId;
	private String parMenuId;
	private String menuName;
	private String menuFunction;
	private String menuEnglishName;
	private String menuSort;
	private String remarks;
	private String STATUS;
	private String CREATED_ON;
	private String CREATED_BY;
	private String UPDATED_ON;
	private String UPDATED_BY;
	private String level;

	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getParMenuId() {
		return parMenuId;
	}
	public void setParMenuId(String parMenuId) {
		this.parMenuId = parMenuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuFunction() {
		return menuFunction;
	}
	public void setMenuFunction(String menuFunction) {
		this.menuFunction = menuFunction;
	}
	public String getMenuEnglishName() {
		return menuEnglishName;
	}
	public void setMenuEnglishName(String menuEnglishName) {
		this.menuEnglishName = menuEnglishName;
	}
	public String getMenuSort() {
		return menuSort;
	}
	public void setMenuSort(String menuSort) {
		this.menuSort = menuSort;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
