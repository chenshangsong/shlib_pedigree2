package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class FamilyNameModel  implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private Long identifier;
	private String familyNameT;
	private String familyNameS;
	private String pinyin;
	private String selfUri;
	private String familyUri;
	private String roleOfFamilyUri;
	private String selfID;
	private String familyID;
	private String description;
	/**
	 * 姓氏繁体
	 */
	private String xingshiT;

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

	public String getFamilyNameT() {
		return familyNameT;
	}

	public void setFamilyNameT(String familyNameT) {
		this.familyNameT = familyNameT;
	}

	public String getFamilyNameS() {
		return familyNameS;
	}

	public void setFamilyNameS(String familyNameS) {
		this.familyNameS = familyNameS;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getFamilyUri() {
		return familyUri;
	}

	public void setFamilyUri(String familyUri) {
		this.familyUri = familyUri;
	}

	public String getSelfID() {
		return selfID;
	}

	public void setSelfID(String selfID) {
		this.selfID = selfID;
	}

	public String getFamilyID() {
		return familyID;
	}

	public void setFamilyID(String familyID) {
		this.familyID = familyID;
	}

	public String getRoleOfFamilyUri() {
		return roleOfFamilyUri;
	}

	public void setRoleOfFamilyUri(String roleOfFamilyUri) {
		this.roleOfFamilyUri = roleOfFamilyUri;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getXingshiT() {
		return xingshiT;
	}

	public void setXingshiT(String xingshiT) {
		this.xingshiT = xingshiT;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
