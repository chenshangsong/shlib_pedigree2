package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.Date;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class ApiFamousDto extends DtoJsonPageData implements Serializable {
	/**
	 * @author 陈铭
	 */
	private static final long serialVersionUID = 1L;
	//名人始迁祖id(自动生成)
	private Integer id;
	//用户id
	private Integer uid;
	//姓名
	private String name;
	//生卒年
	private String birth;
	//父亲
	private String father;
	//配偶
	private String wife;
	//简介
	private String summary;
	//状态 1: 有效0：无效
	private Integer status;
	//类型 1：先祖名人 2：始迁祖
	private Integer type;
	//创建时间(自动生成)
	private Date createdDate;
	//用户id =uid
	private Integer createdUser;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	public String getWife() {
		return wife;
	}
	public void setWife(String wife) {
		this.wife = wife;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(Integer createdUser) {
		this.createdUser = createdUser;
	}
	
}

