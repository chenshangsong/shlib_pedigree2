package cn.sh.library.pedigree.dto;


/**
 * @author liuyi
 * @date 2014/12/29 0029
 */
public class Item {
	
	/**
	 * uri
	 */
	private String uri;
	
	/**
	 * doi
	 */
	private String doi;
	
	/**
	 * 层级
	 */
	private String accessLevel;
	
	/**
	 * 是否有全文("true":有, 空:无)
	 */
	private String hasFullImg;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getHasFullImg() {
		return hasFullImg;
	}

	public void setHasFullImg(String hasFullImg) {
		this.hasFullImg = hasFullImg;
	}

}
