package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class DataChangeHistoryListModel extends DtoJsonPageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int mainId;
	private String oldCnNameUri;
	private String oldValue;
	private String oldValueCn;
	private String isUri;
	private String newValue;
	private String oldComment;
	private String oldCnName;
	private String oldEnName;
	/**
	 * uri时，新值中文名称
	 */
	private String newValueCn;
	public int getMainId() {
		return mainId;
	}
	public void setMainId(int mainId) {
		this.mainId = mainId;
	}
	public String getOldCnNameUri() {
		return oldCnNameUri;
	}
	public void setOldCnNameUri(String oldCnNameUri) {
		this.oldCnNameUri = oldCnNameUri;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getOldComment() {
		return oldComment;
	}
	public void setOldComment(String oldComment) {
		this.oldComment = oldComment;
	}
	public String getOldCnName() {
		return oldCnName;
	}
	public void setOldCnName(String oldCnName) {
		this.oldCnName = oldCnName;
	}
	public String getOldEnName() {
		return oldEnName;
	}
	public void setOldEnName(String oldEnName) {
		this.oldEnName = oldEnName;
	}
	public String getOldValueCn() {
		return oldValueCn;
	}
	public void setOldValueCn(String oldValueCn) {
		this.oldValueCn = oldValueCn;
	}
	public String getIsUri() {
		return isUri;
	}
	public void setIsUri(String isUri) {
		this.isUri = isUri;
	}
	public String getNewValueCn() {
		return newValueCn;
	}
	public void setNewValueCn(String newValueCn) {
		this.newValueCn = newValueCn;
	}
}
