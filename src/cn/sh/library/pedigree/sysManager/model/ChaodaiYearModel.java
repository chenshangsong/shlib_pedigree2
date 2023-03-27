package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

/**
 * 年号纪年
 * 
 * @author chenss
 *
 */
public class ChaodaiYearModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private String chaodai;
	private String diwang;;
	private String diwangName;
	private String diwangNianhao;
	private String beginY;
	private String endY;
	private String selfUri;
	private String chaodaiUri;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChaodai() {
		return chaodai;
	}

	public void setChaodai(String chaodai) {
		this.chaodai = chaodai;
	}

	public String getDiwang() {
		return diwang;
	}

	public void setDiwang(String diwang) {
		this.diwang = diwang;
	}

	public String getDiwangName() {
		return diwangName;
	}

	public void setDiwangName(String diwangName) {
		this.diwangName = diwangName;
	}

	public String getDiwangNianhao() {
		return diwangNianhao;
	}

	public void setDiwangNianhao(String diwangNianhao) {
		this.diwangNianhao = diwangNianhao;
	}

	public String getBeginY() {
		return beginY;
	}

	public void setBeginY(String beginY) {
		this.beginY = beginY;
	}

	public String getEndY() {
		return endY;
	}

	public void setEndY(String endY) {
		this.endY = endY;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getChaodaiUri() {
		return chaodaiUri;
	}

	public void setChaodaiUri(String chaodaiUri) {
		this.chaodaiUri = chaodaiUri;
	}

}
