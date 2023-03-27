package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class RelationLinkModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String to;
	private String id;
	private String from;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
}
