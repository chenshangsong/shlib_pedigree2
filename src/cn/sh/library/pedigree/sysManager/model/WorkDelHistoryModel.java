package cn.sh.library.pedigree.sysManager.model;

public class WorkDelHistoryModel {
	private long id;
	private String workUri;
	private String workTitle;
	private String workDtitle;
	private String creator;
	private String instanceUri;
	private String deleteDate;
	private String deleteUserId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWorkUri() {
		return workUri;
	}

	public void setWorkUri(String workUri) {
		this.workUri = workUri;
	}

	public String getWorkTitle() {
		return workTitle;
	}

	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getInstanceUri() {
		return instanceUri;
	}

	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}

	public String getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(String deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public String getWorkDtitle() {
		return workDtitle;
	}

	public void setWorkDtitle(String workDtitle) {
		this.workDtitle = workDtitle;
	}

}
