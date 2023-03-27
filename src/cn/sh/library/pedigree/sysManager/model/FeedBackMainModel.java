package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class FeedBackMainModel extends DtoJsonPageData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	/**
	 * 家谱title
	 */
	private String genealogyTitle;
	private String genealogyUri;
	private String createdDate;
	private String createdUser;
	private String name;
	private String updatedDate;
	private String updatedUser;
	private String status;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenealogyUri() {
		return genealogyUri;
	}

	public void setGenealogyUri(String genealogyUri) {
		this.genealogyUri = genealogyUri;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGenealogyTitle() {
		return genealogyTitle;
	}

	public void setGenealogyTitle(String genealogyTitle) {
		this.genealogyTitle = genealogyTitle;
	}

}
