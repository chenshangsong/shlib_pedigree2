package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

/**
 * @author 陈铭
 */
public class ApiChaodaiDto extends DtoJsonPageData implements Serializable {
	/**
	 * 朝代
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String chaodai;
	private String chaodaiT;
	private String beginY;
	private String beginM;
	private String beginD;
	private String endY;
	private String endM;
	private String endD;
	private String pinyin;
	private String selfUri;
	private String intervalDuring;
	private String uri;
	private String data;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChaodai() {
		return chaodai;
	}

	public void setChaodai(String chaodai) {
		this.chaodai = chaodai;
	}

	public String getBeginY() {
		return beginY;
	}

	public void setBeginY(String beginY) {
		this.beginY = beginY;
	}

	public String getBeginM() {
		return beginM;
	}

	public void setBeginM(String beginM) {
		this.beginM = beginM;
	}

	public String getBeginD() {
		return beginD;
	}

	public void setBeginD(String beginD) {
		this.beginD = beginD;
	}

	public String getEndY() {
		return endY;
	}

	public void setEndY(String endY) {
		this.endY = endY;
	}

	public String getEndM() {
		return endM;
	}

	public void setEndM(String endM) {
		this.endM = endM;
	}

	public String getEndD() {
		return endD;
	}

	public void setEndD(String endD) {
		this.endD = endD;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getChaodaiT() {
		return chaodaiT;
	}

	public void setChaodaiT(String chaodaiT) {
		this.chaodaiT = chaodaiT;
	}

	public String getIntervalDuring() {
		return intervalDuring;
	}

	public void setIntervalDuring(String intervalDuring) {
		this.intervalDuring = intervalDuring;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}

