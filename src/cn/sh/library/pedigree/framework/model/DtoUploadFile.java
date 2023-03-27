package  cn.sh.library.pedigree.framework.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 类名 : DtoUploadFile <br>
 * 机能概要 : </br> 版权所有: Copyright © 2011 TES Corporation, All Rights
 * Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class DtoUploadFile extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CommonsMultipartFile uploadFile;
	private String formData;
	private String uploadId;



	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public CommonsMultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(CommonsMultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

}
