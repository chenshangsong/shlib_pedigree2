package cn.sh.library.pedigree.sysManager.model;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

/**
 * item属性类
 * 
 * @author chenss
 *
 */
public class DataItemChangeHistoryModel extends DtoJsonPageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private String itemUri;
	private String graphUri;
	private String engName;
	private String oldValueUri;
	private String oldValueCn;
	private String newValueUri;
	private String newValueCn;
	private String oldValueCnBook;
	private String newValueCnBook;
	private String engNameBook;
	private String oldValueCnDoi;
	private String newValueCnDoi;
	private String engNameDoi;
	private String checkStatus;
	private String instanceUri;
	private Integer mainInstanceId;
	/**
	 * DOI开关 chenss2017-03-15
	 */
	private String oldValueAccessLevel;
	private String newValueAccessLevel;
	private String engNameAccessLevel;

	private String deleteFlg = "0";

	public String getItemUri() {
		return itemUri;
	}

	public void setItemUri(String itemUri) {
		this.itemUri = itemUri;
	}

	public String getGraphUri() {
		return graphUri;
	}

	public void setGraphUri(String graphUri) {
		this.graphUri = graphUri;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getOldValueUri() {
		return oldValueUri;
	}

	public void setOldValueUri(String oldValueUri) {
		this.oldValueUri = oldValueUri;
	}

	public String getOldValueCn() {
		return oldValueCn;
	}

	public void setOldValueCn(String oldValueCn) {
		this.oldValueCn = oldValueCn;
	}

	public String getNewValueUri() {
		return newValueUri;
	}

	public void setNewValueUri(String newValueUri) {
		this.newValueUri = newValueUri;
	}

	public String getNewValueCn() {
		return newValueCn;
	}

	public void setNewValueCn(String newValueCn) {
		this.newValueCn = newValueCn;
	}

	public String getOldValueCnBook() {
		return oldValueCnBook;
	}

	public void setOldValueCnBook(String oldValueCnBook) {
		this.oldValueCnBook = oldValueCnBook;
	}

	public String getNewValueCnBook() {
		return newValueCnBook;
	}

	public void setNewValueCnBook(String newValueCnBook) {
		this.newValueCnBook = newValueCnBook;
	}

	public String getEngNameBook() {
		return engNameBook;
	}

	public void setEngNameBook(String engNameBook) {
		this.engNameBook = engNameBook;
	}

	public String getInstanceUri() {
		return instanceUri;
	}

	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}

	public void setDeleteFlg(String deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getDeleteFlg() {
		return deleteFlg;
	}

	public Integer getMainInstanceId() {
		return mainInstanceId;
	}

	public void setMainInstanceId(Integer mainInstanceId) {
		this.mainInstanceId = mainInstanceId;
	}

	public String getOldValueCnDoi() {
		return oldValueCnDoi;
	}

	public void setOldValueCnDoi(String oldValueCnDoi) {
		this.oldValueCnDoi = oldValueCnDoi;
	}

	public String getNewValueCnDoi() {
		return newValueCnDoi;
	}

	public void setNewValueCnDoi(String newValueCnDoi) {
		this.newValueCnDoi = newValueCnDoi;
	}

	public String getEngNameDoi() {
		return engNameDoi;
	}

	public void setEngNameDoi(String engNameDoi) {
		this.engNameDoi = engNameDoi;
	}

	public String getOldValueAccessLevel() {
		return oldValueAccessLevel;
	}

	public void setOldValueAccessLevel(String oldValueAccessLevel) {
		this.oldValueAccessLevel = oldValueAccessLevel;
	}

	public String getNewValueAccessLevel() {
		return newValueAccessLevel;
	}

	public void setNewValueAccessLevel(String newValueAccessLevel) {
		this.newValueAccessLevel = newValueAccessLevel;
	}

	public String getEngNameAccessLevel() {
		return engNameAccessLevel;
	}

	public void setEngNameAccessLevel(String engNameAccessLevel) {
		this.engNameAccessLevel = engNameAccessLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
