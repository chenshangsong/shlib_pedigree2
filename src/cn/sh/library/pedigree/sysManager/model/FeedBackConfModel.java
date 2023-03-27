package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class FeedBackConfModel extends DtoJsonPageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String genealogyUri;
	private String genealogyTitle;
	private String createdDate;
	private String createdUser;
	private String feedbackContent;
	private String postilContent;
	private String postilUser;
	private String postilDate;
	private String createdname;
	private String postilname;
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
	public String getFeedbackContent() {
		return feedbackContent;
	}
	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}
	public String getPostilContent() {
		return postilContent;
	}
	public void setPostilContent(String postilContent) {
		this.postilContent = postilContent;
	}
	public String getPostilUser() {
		return postilUser;
	}
	public void setPostilUser(String postilUser) {
		this.postilUser = postilUser;
	}
	public String getPostilDate() {
		return postilDate;
	}
	public void setPostilDate(String postilDate) {
		this.postilDate = postilDate;
	}
	public String getCreatedname() {
		return createdname;
	}
	public void setCreatedname(String createdname) {
		this.createdname = createdname;
	}
	public String getPostilname() {
		return postilname;
	}
	public void setPostilname(String postilname) {
		this.postilname = postilname;
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
