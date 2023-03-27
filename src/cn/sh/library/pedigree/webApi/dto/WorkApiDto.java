package cn.sh.library.pedigree.webApi.dto;

import java.util.List;
import java.util.Map;

/**
 * @author liuyi
 * @date 2014/12/29 0029
 */
public class WorkApiDto {
	// http://gen.library.sh.cn/Work/xx
	private String uri;
	private String title;
	private String subTitle;
	/**
	 * 
	 */
	private String imageFrontPath;
	/**
	 * 题名全称
	 */
	private String dtitle;
	// 笔记
	private String note;
	private String role;
	/**
	 * 堂号
	 */
	private String tangh;
	private String fulllink;

	private String creator;
	/**
	 * personUri
	 */
	private String puri;
	private List<Map<String, String>> instances;

	private List<Map<String, String>> familyRelations;
	/**
	 * 胶卷flg
	 */
	private String jjflag;
	private String lat;
	private String longitude;
	private String placeUri;
	private String place;
	// 是否全文
	private String isqw;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getNote() {
		return note;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<Map<String, String>> getInstances() {
		return instances;
	}

	public void setInstances(List<Map<String, String>> instances) {
		this.instances = instances;
	}

	public List<Map<String, String>> getFamilyRelations() {
		return familyRelations;
	}

	public void setFamilyRelations(List<Map<String, String>> familyRelations) {
		this.familyRelations = familyRelations;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getFulllink() {
		return fulllink;
	}

	public void setFulllink(String fulllink) {
		this.fulllink = fulllink;
	}

	public String getTangh() {
		return tangh;
	}

	public void setTangh(String tangh) {
		this.tangh = tangh;
	}

	public String getDtitle() {
		return dtitle;
	}

	public void setDtitle(String dtitle) {
		this.dtitle = dtitle;
	}

	public String getPuri() {
		return puri;
	}

	public void setPuri(String puri) {
		this.puri = puri;
	}

	public String getJjflag() {
		return jjflag;
	}

	public void setJjflag(String jjflag) {
		this.jjflag = jjflag;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPlaceUri() {
		return placeUri;
	}

	public void setPlaceUri(String placeUri) {
		this.placeUri = placeUri;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getIsqw() {
		return isqw;
	}

	public void setIsqw(String isqw) {
		this.isqw = isqw;
	}

	public String getImageFrontPath() {
		return imageFrontPath;
	}

	public void setImageFrontPath(String imageFrontPath) {
		this.imageFrontPath = imageFrontPath;
	}
}
