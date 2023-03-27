package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class DataChangeHistoryMainModel extends DtoJsonPageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 父ID
	 */
	private String pdataUri;
	private String dataType;
	private String dataTypeName;
	private String remarks;
	private String createUser;
	private String createDate;
	private String dataUri;
	private String graphUri;
	private String checkStatus;
	private String releaseUser;
	private String releaseDate;
	private String addFlg;
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDataUri() {
		return dataUri;
	}
	public void setDataUri(String dataUri) {
		this.dataUri = dataUri;
	}
	public String getGraphUri() {
		return graphUri;
	}
	public void setGraphUri(String graphUri) {
		this.graphUri = graphUri;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getReleaseUser() {
		return releaseUser;
	}
	public void setReleaseUser(String releaseUser) {
		this.releaseUser = releaseUser;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getPdataUri() {
		return pdataUri;
	}
	public void setPdataUri(String pdataUri) {
		this.pdataUri = pdataUri;
	}
	public String getAddFlg() {
		return addFlg;
	}
	public void setAddFlg(String addFlg) {
		this.addFlg = addFlg;
	}
}
