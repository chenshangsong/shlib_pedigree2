package cn.sh.library.pedigree.webApi.dto.searchBean;

import java.util.Map;

/**
 * Created by yesonme on 15-1-6.
 */
public class ApiWorkSearchBean {
	private String title;
	private String place;
	private String familyName;

	private String place_uri;
	private String uri;
	private String creator;

	private String person;
	private String tang;
	private String note;

	private String organization;
	private String freeText;
	private String accFlg;// 是否外网访问
	private String selfMark;// 索书号
	private String doi;// doi
	private String tempoLabel;//撰修时间 20181225
	private Boolean isMore;// 分面类型

	private String facetPlaceUri;
	private String facetTanghUri;
	private String facetTemporal;
	private String facetEditionUri;
	private String facetOrgUri;
	private String facetAccFlg;
	private String orderType;
	private String facetType;
	private boolean ifAdmin;
	private String facetPlaceAllUri;
	private Map<String, String> standardPlace;

	public Map<String, String> getStandardPlace() {
		return standardPlace;
	}

	public void setStandardPlace(Map<String, String> standardPlace) {
		this.standardPlace = standardPlace;
	}


	public String getTempoLabel() {
		return tempoLabel;
	}

	public void setTempoLabel(String tempoLabel) {
		this.tempoLabel = tempoLabel;
	}

	public String getFacetPlaceAllUri() {
		return facetPlaceAllUri;
	}

	public void setFacetPlaceAllUri(String facetPlaceAllUri) {
		this.facetPlaceAllUri = facetPlaceAllUri;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getSelfMark() {
		return selfMark;
	}

	public void setSelfMark(String selfMark) {
		this.selfMark = selfMark;
	}

	public String getAccFlg() {
		return accFlg;
	}

	public void setAccFlg(String accFlg) {
		this.accFlg = accFlg;
	}

	private String points;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPlace_uri() {
		return place_uri;
	}

	public void setPlace_uri(String place_uri) {
		this.place_uri = place_uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getTang() {
		return tang;
	}

	public void setTang(String tang) {
		this.tang = tang;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getFacetPlaceUri() {
		return facetPlaceUri;
	}

	public void setFacetPlaceUri(String facetPlaceUri) {
		this.facetPlaceUri = facetPlaceUri;
	}

	public String getFacetTanghUri() {
		return facetTanghUri;
	}

	public void setFacetTanghUri(String facetTanghUri) {
		this.facetTanghUri = facetTanghUri;
	}

	public String getFacetTemporal() {
		return facetTemporal;
	}

	public void setFacetTemporal(String facetTemporal) {
		this.facetTemporal = facetTemporal;
	}

	public String getFacetEditionUri() {
		return facetEditionUri;
	}

	public void setFacetEditionUri(String facetEditionUri) {
		this.facetEditionUri = facetEditionUri;
	}

	public String getFacetOrgUri() {
		return facetOrgUri;
	}

	public void setFacetOrgUri(String facetOrgUri) {
		this.facetOrgUri = facetOrgUri;
	}

	public String getFacetAccFlg() {
		return facetAccFlg;
	}

	public void setFacetAccFlg(String facetAccFlg) {
		this.facetAccFlg = facetAccFlg;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Boolean getIsMore() {
		return isMore;
	}

	public void setIsMore(Boolean isMore) {
		this.isMore = isMore;
	}

	public String getFacetType() {
		return facetType;
	}

	public void setFacetType(String facetType) {
		this.facetType = facetType;
	}

	public boolean getIfAdmin() {
		return ifAdmin;
	}

	public void setIfAdmin(boolean ifAdmin) {
		this.ifAdmin = ifAdmin;
	}

}
