package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class CategoryModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private String label;
	private String labelT;
	private String selfUri;
	private String type;
	private String addFlag;
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabelT() {
		return labelT;
	}

	public void setLabelT(String labelT) {
		this.labelT = labelT;
	}

	public String getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
