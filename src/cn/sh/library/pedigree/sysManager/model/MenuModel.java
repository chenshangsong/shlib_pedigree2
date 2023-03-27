package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class MenuModel extends DtoJsonPageData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9042535473923609021L;
	
	@JsonProperty("selected")
	private boolean selected;
	
	@JsonProperty("menuId")
	private String menuId;
	
	@JsonProperty("parMenuId")
	private String parMenuId;
	
	@JsonProperty("menuName")
	private String menuName;
	
	@JsonProperty("menuFunction")
	private String menuFunction;
	
	@JsonProperty("menuEnglishName")
	private String menuEnglishName;
	
	@JsonProperty("menuSort")
	private Integer menuSort;
	
	@JsonProperty("isDel")
	private Integer isDel;
	
	@JsonProperty("remarks")
	private String remarks;
	
	@JsonProperty("STATUS")
	private String STATUS;
	
	@JsonProperty("CREATED_ON")
	private String CREATED_ON;
	
	@JsonProperty("CREATED_BY")
	private String CREATED_BY;
	
	@JsonProperty("UPDATED_ON")
	private String UPDATED_ON;
	
	@JsonProperty("UPDATED_BY")
	private String UPDATED_BY;
	
	@JsonProperty("level")
	private String level;
	
	@JsonProperty("color")
	private String color;
	
	@JsonProperty("icon")
	private String icon;
	
	@JsonProperty("systemId")
	private String systemId;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

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

	public Integer getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
