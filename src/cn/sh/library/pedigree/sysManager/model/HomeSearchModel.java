package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class HomeSearchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstChar;
	/**
	 * 人名URI，可能包含逗号分隔
	 */
	private String puri;
	public String getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}

	public String getPuri() {
		return puri;
	}

	public void setPuri(String puri) {
		this.puri = puri;
	}
}
