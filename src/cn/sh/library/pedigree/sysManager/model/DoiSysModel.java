package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class DoiSysModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private String doi;
	private String openFlg;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getOpenFlg() {
		return openFlg;
	}
	public void setOpenFlg(String openFlg) {
		this.openFlg = openFlg;
	}
	

}
