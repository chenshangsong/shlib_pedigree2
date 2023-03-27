package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class CategoryTypeModel extends DtoJsonPageData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String STATUS;
	private String CREATED_ON;
	private String CREATED_BY;
	private String CREATED_BY_NAME;
	private String categoryNameCn;
	private String categoryNameEn;
	private String categoryUriIn;
	private String dataNamespace;
	
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
	public String getCategoryNameCn() {
		return categoryNameCn;
	}
	public void setCategoryNameCn(String categoryNameCn) {
		this.categoryNameCn = categoryNameCn;
	}
	public String getCategoryNameEn() {
		return categoryNameEn;
	}
	public void setCategoryNameEn(String categoryNameEn) {
		this.categoryNameEn = categoryNameEn;
	}
	public String getCategoryUriIn() {
		return categoryUriIn;
	}
	public void setCategoryUriIn(String categoryUriIn) {
		this.categoryUriIn = categoryUriIn;
	}
	public String getDataNamespace() {
		return dataNamespace;
	}
	public void setDataNamespace(String dataNamespace) {
		this.dataNamespace = dataNamespace;
	}
	public String getCREATED_BY_NAME() {
		return CREATED_BY_NAME;
	}
	public void setCREATED_BY_NAME(String cREATED_BY_NAME) {
		CREATED_BY_NAME = cREATED_BY_NAME;
	}

}
