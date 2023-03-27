package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class ApiWorkFavoriteDto extends DtoJsonPageData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String workUri;
	private String title;
	private Integer userId;
	private Integer uid;
	private String edition;
	private String editionTemporal;
	private String creator;
	private String tangh;
	private String place;
	private Integer createdUser;
	private String createdDate;
	public String getWorkUri() {
		return workUri;
	}
	public void setWorkUri(String workUri) {
		this.workUri = workUri;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getEditionTemporal() {
		return editionTemporal;
	}
	public void setEditionTemporal(String editionTemporal) {
		this.editionTemporal = editionTemporal;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getTangh() {
		return tangh;
	}
	public void setTangh(String tangh) {
		this.tangh = tangh;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Integer getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(Integer createdUser) {
		this.createdUser = createdUser;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
}

