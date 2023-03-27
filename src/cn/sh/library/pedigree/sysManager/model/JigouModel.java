package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class JigouModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private Long identifier;
	private String dimingT;
	private String dimingS;
	private String quanchengT;
	private String quanchengS;
	private String jianchengT;
	private String jianchengS;
	private String placeUri;
	private String selfUri;
	private String parm1T;
	private String parm1S;
	private Long placeID;
	private String addressE;
	private String addressS;
	private String addressT;
	private String addressO;
	private String description;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	public String getDimingT() {
		return dimingT;
	}

	public void setDimingT(String dimingT) {
		this.dimingT = dimingT;
	}

	public String getDimingS() {
		return dimingS;
	}

	public void setDimingS(String dimingS) {
		this.dimingS = dimingS;
	}

	public String getQuanchengT() {
		return quanchengT;
	}

	public void setQuanchengT(String quanchengT) {
		this.quanchengT = quanchengT;
	}

	public String getQuanchengS() {
		return quanchengS;
	}

	public void setQuanchengS(String quanchengS) {
		this.quanchengS = quanchengS;
	}

	public String getJianchengT() {
		return jianchengT;
	}

	public void setJianchengT(String jianchengT) {
		this.jianchengT = jianchengT;
	}

	public String getJianchengS() {
		return jianchengS;
	}

	public void setJianchengS(String jianchengS) {
		this.jianchengS = jianchengS;
	}

	public String getPlaceUri() {
		return placeUri;
	}

	public void setPlaceUri(String placeUri) {
		this.placeUri = placeUri;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getParm1T() {
		return parm1T;
	}

	public void setParm1T(String parm1t) {
		parm1T = parm1t;
	}

	public String getParm1S() {
		return parm1S;
	}

	public void setParm1S(String parm1s) {
		parm1S = parm1s;
	}

	public Long getPlaceID() {
		return placeID;
	}

	public void setPlaceID(Long placeID) {
		this.placeID = placeID;
	}

	public String getAddressE() {
		return addressE;
	}

	public void setAddressE(String addressE) {
		this.addressE = addressE;
	}

	public String getAddressS() {
		return addressS;
	}

	public void setAddressS(String addressS) {
		this.addressS = addressS;
	}

	public String getAddressT() {
		return addressT;
	}

	public void setAddressT(String addressT) {
		this.addressT = addressT;
	}

	public String getAddressO() {
		return addressO;
	}

	public void setAddressO(String addressO) {
		this.addressO = addressO;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
