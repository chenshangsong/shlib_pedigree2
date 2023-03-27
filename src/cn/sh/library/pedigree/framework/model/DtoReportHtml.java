package  cn.sh.library.pedigree.framework.model;

import java.io.Serializable;

public class DtoReportHtml implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -207382037574663996L;

	public DtoReportHtml(){
	}
	public DtoReportHtml(String html, String totalPage){
		this.html = html;
		this.totalPage = totalPage;
	}
	public DtoReportHtml(String error){
		this.error = error;
	}
	private String error;
	private String html;
	private String page;
	private String totalPage;
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}

}
