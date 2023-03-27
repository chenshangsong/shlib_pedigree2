package cn.sh.library.pedigree.sysManager.model;

/**
 * 
 * @author 陈铭
 *	实体类
 */

public class ApiWorkViewsCountDto {
	private Integer id;
	private String workUri;
	private int viewCount;
	private String createdDate;
	public String getWorkUri() {
		return workUri;
	}

	public void setWorkUri(String workUri) {
		this.workUri = workUri;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
}
