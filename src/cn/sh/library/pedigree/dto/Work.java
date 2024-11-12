package cn.sh.library.pedigree.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyi
 * @date 2014/12/29 0029
 */
public class Work {
	// http://gen.library.sh.cn/Work/xx
	private String uri;
	private String title;
	private String subTitle;
	private String doi;
	/**
	 * 家谱封面路径
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

	private List<Map<String, String>> places;
	private Map<String, Object> resultVist = new HashMap<>();
	private List<Map<String, String>> creators;

	private String creator;
	/**
	 * personUri
	 */
	private String puri;
	public Map<String, Object> getResultVist() {
		return resultVist;
	}

	public void setResultVist(Map<String, Object> resultVist) {
		this.resultVist = resultVist;
	}

	private List<Map<String, String>> instances;

	private List<Map<String, String>> familyRelations;
	/**
	 * 胶卷flg
	 */
	private String jjflag;
	/**
	 * 收藏ID
	 */
	private Integer favoriteId;
	/**
	 * 浏览量
	 */
	private Integer viewCount;
	
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

	public List<Map<String, String>> getPlaces() {
		return places;
	}

	public void setPlaces(List<Map<String, String>> places) {
		this.places = places;
	}

	public List<Map<String, String>> getCreators() {
		return creators;
	}

	public void setCreators(List<Map<String, String>> creators) {
		this.creators = creators;
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

	public Integer getFavoriteId() {
		return favoriteId;
	}

	public void setFavoriteId(Integer favoriteId) {
		this.favoriteId = favoriteId;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getImageFrontPath() {
		return imageFrontPath;
	}

	public void setImageFrontPath(String imageFrontPath) {
		this.imageFrontPath = imageFrontPath;
	}

	
}
