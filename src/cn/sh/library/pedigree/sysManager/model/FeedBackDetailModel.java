package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class FeedBackDetailModel extends DtoJsonPageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String feedbackContent;
	private String postilContent;
	private String postilDate;
	private String postilUser;
	private Integer markId;
	private Integer feedbackMainId;
	private String roleId; 
	private String genealogyUri;
	/**
	 * 家谱title
	 */
	private String genealogyTitle;
	private String createdDate;
	private String createdUser;
	private String status;
	private String feedbackDate;
	private String feedbackUser;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public Integer getMarkId() {
		return markId;
	}
	public void setMarkId(Integer markId) {
		this.markId = markId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPostilDate() {
		return postilDate;
	}
	public void setPostilDate(String postilDate) {
		this.postilDate = postilDate;
	}
	public String getPostilUser() {
		return postilUser;
	}
	public void setPostilUser(String postilUser) {
		this.postilUser = postilUser;
	}
	public Integer getFeedbackMainId() {
		return feedbackMainId;
	}
	public void setFeedbackMainId(Integer feedbackMainId) {
		this.feedbackMainId = feedbackMainId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(String feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	public String getFeedbackUser() {
		return feedbackUser;
	}
	public void setFeedbackUser(String feedbackUser) {
		this.feedbackUser = feedbackUser;
	}
	public String getGenealogyTitle() {
		return genealogyTitle;
	}
	public void setGenealogyTitle(String genealogyTitle) {
		this.genealogyTitle = genealogyTitle;
	}
}
