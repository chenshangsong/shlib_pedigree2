package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class JpWorkModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private String identifier;
	private String timing;
	private String juanshu;
	private String bbsj;
	private String bbsjgy;
	private String bbxs;
	private String pujidiming;
	private String tanghao;
	private String xzmr;
	private String zhaiyao;
	private String shuliang;
	private String fuzhu;
	private String suoquhao;
	private String DOI;
	private String accesslevel;

	public String getAccesslevel() {
		return accesslevel;
	}

	public void setAccesslevel(String accesslevel) {
		this.accesslevel = accesslevel;
	}

	private String familyName;
	private String familyNameUri;
	private String selfUri;
	private String instanceUri;
	private String tanghaoUri;
	private String bbxsUri;
	private String bbcdUri;
	private String placeUri;
	private String place;
	private String juanshuback;
	private String titleInChs;
	private String itemUri;
	private String timingCht;
	private String titleInCht;
	private String timingUri;
	private String judiS;
	private String bbsjgyYear;

	public String getBbsjgyYear() {
		return bbsjgyYear;
	}

	public void setBbsjgyYear(String bbsjgyYear) {
		this.bbsjgyYear = bbsjgyYear;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public String getJuanshu() {
		return juanshu;
	}

	public void setJuanshu(String juanshu) {
		this.juanshu = juanshu;
	}

	public String getBbsj() {
		return bbsj;
	}

	public void setBbsj(String bbsj) {
		this.bbsj = bbsj;
	}

	public String getBbsjgy() {
		return bbsjgy;
	}

	public void setBbsjgy(String bbsjgy) {
		this.bbsjgy = bbsjgy;
	}

	public String getBbxs() {
		return bbxs;
	}

	public void setBbxs(String bbxs) {
		this.bbxs = bbxs;
	}

	public String getPujidiming() {
		return pujidiming;
	}

	public void setPujidiming(String pujidiming) {
		this.pujidiming = pujidiming;
	}

	public String getTanghao() {
		return tanghao;
	}

	public void setTanghao(String tanghao) {
		this.tanghao = tanghao;
	}

	public String getXzmr() {
		return xzmr;
	}

	public void setXzmr(String xzmr) {
		this.xzmr = xzmr;
	}

	public String getZhaiyao() {
		return zhaiyao;
	}

	public void setZhaiyao(String zhaiyao) {
		this.zhaiyao = zhaiyao;
	}

	public String getShuliang() {
		return shuliang;
	}

	public void setShuliang(String shuliang) {
		this.shuliang = shuliang;
	}

	public String getFuzhu() {
		return fuzhu;
	}

	public void setFuzhu(String fuzhu) {
		this.fuzhu = fuzhu;
	}

	public String getSuoquhao() {
		return suoquhao;
	}

	public void setSuoquhao(String suoquhao) {
		this.suoquhao = suoquhao;
	}

	public String getDOI() {
		return DOI;
	}

	public void setDOI(String dOI) {
		DOI = dOI;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyNameUri() {
		return familyNameUri;
	}

	public void setFamilyNameUri(String familyNameUri) {
		this.familyNameUri = familyNameUri;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getInstanceUri() {
		return instanceUri;
	}

	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}

	public String getTanghaoUri() {
		return tanghaoUri;
	}

	public void setTanghaoUri(String tanghaoUri) {
		this.tanghaoUri = tanghaoUri;
	}

	public String getBbxsUri() {
		return bbxsUri;
	}

	public void setBbxsUri(String bbxsUri) {
		this.bbxsUri = bbxsUri;
	}

	public String getBbcdUri() {
		return bbcdUri;
	}

	public void setBbcdUri(String bbcdUri) {
		this.bbcdUri = bbcdUri;
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

	public String getJuanshuback() {
		return juanshuback;
	}

	public void setJuanshuback(String juanshuback) {
		this.juanshuback = juanshuback;
	}

	public String getTitleInChs() {
		return titleInChs;
	}

	public void setTitleInChs(String titleInChs) {
		this.titleInChs = titleInChs;
	}

	public String getItemUri() {
		return itemUri;
	}

	public void setItemUri(String itemUri) {
		this.itemUri = itemUri;
	}

	public String getTimingCht() {
		return timingCht;
	}

	public void setTimingCht(String timingCht) {
		this.timingCht = timingCht;
	}

	public String getTitleInCht() {
		return titleInCht;
	}

	public void setTitleInCht(String titleInCht) {
		this.titleInCht = titleInCht;
	}

	public String getTimingUri() {
		return timingUri;
	}

	public void setTimingUri(String timingUri) {
		this.timingUri = timingUri;
	}

	public String getJudiS() {
		return judiS;
	}

	public void setJudiS(String judiS) {
		this.judiS = judiS;
	}

	public String getJudiT() {
		return judiT;
	}

	public void setJudiT(String judiT) {
		this.judiT = judiT;
	}

	public String getJuanshuT() {
		return juanshuT;
	}

	public void setJuanshuT(String juanshuT) {
		this.juanshuT = juanshuT;
	}

	private String judiT;
	private String juanshuT;

}
