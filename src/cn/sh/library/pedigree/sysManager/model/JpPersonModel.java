package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class JpPersonModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private String xingming;
	private String type;
	private String chaodai;
	private String chaodaiUri;
	private String identifier;
	private String selfUri;
	private String flg	;
	private String typeUri;
	private String familyNameUri;
	public String getFamilyNameUri() {
		return familyNameUri;
	}
	public void setFamilyNameUri(String familyNameUri) {
		this.familyNameUri = familyNameUri;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getChaodai() {
		return chaodai;
	}
	public void setChaodai(String chaodai) {
		this.chaodai = chaodai;
	}
	public String getChaodaiUri() {
		return chaodaiUri;
	}
	public void setChaodaiUri(String chaodaiUri) {
		this.chaodaiUri = chaodaiUri;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getSelfUri() {
		return selfUri;
	}
	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}
	public String getFlg() {
		return flg;
	}
	public void setFlg(String flg) {
		this.flg = flg;
	}
	public String getTypeUri() {
		return typeUri;
	}
	public void setTypeUri(String typeUri) {
		this.typeUri = typeUri;
	}
	public String getWorkUri() {
		return workUri;
	}
	public void setWorkUri(String workUri) {
		this.workUri = workUri;
	}
	public String getXingmingCht() {
		return xingmingCht;
	}
	public void setXingmingCht(String xingmingCht) {
		this.xingmingCht = xingmingCht;
	}
	private String workUri;
	private String xingmingCht;


}
