package cn.sh.library.pedigree.dto.sysmanager;

import java.util.ArrayList;
import java.util.List;

import cn.sh.library.pedigree.sysManager.model.DataItemChangeHistoryModel;

public class DataEditViewDto {
	/**
	 * 主语URI如：http://gen.library.sh.cn/Person/25971
	 */
	private String id;
	/**
	 * 
	 */
	private String graphUri;
	/**
	 * 父URI
	 */
	private String pdataUri;
	/**
	 * 页面URL
	 */
	private String pageUrl;

	/**
	 * 规范数据类型区分
	 */
	private String dataType;

	/**
	 * 备考
	 */
	private String remarks;
	/**
	 * 是否同时发布
	 */
	private String isRelease;
	/**
	 * 说明
	 */
	private String description;
	/**
	 * 编辑属性列表
	 */
	private List<DataEditDto> editList = new ArrayList<DataEditDto>();
	/**
	 * 编辑属性列表
	 */
	private List<DataItemChangeHistoryModel> itemList = new ArrayList<DataItemChangeHistoryModel>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<DataEditDto> getEditList() {
		return editList;
	}

	public void setEditList(List<DataEditDto> editList) {
		this.editList = editList;
	}

	public String getGraphUri() {
		return graphUri;
	}

	public void setGraphUri(String graphUri) {
		this.graphUri = graphUri;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<DataItemChangeHistoryModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<DataItemChangeHistoryModel> itemList) {
		this.itemList = itemList;
	}

	public String getPdataUri() {
		return pdataUri;
	}

	public void setPdataUri(String pdataUri) {
		this.pdataUri = pdataUri;
	}

	public String getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(String isRelease) {
		this.isRelease = isRelease;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}