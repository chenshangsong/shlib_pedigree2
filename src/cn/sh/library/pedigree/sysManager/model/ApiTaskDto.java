package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class ApiTaskDto extends DtoJsonPageData implements Serializable {
	/**
	 * @author 陈铭
	 */
	private static final long serialVersionUID = 1L;
	//任务id
	private Integer id;
	//任务id(批量)
	private Integer[] ids;
	//家谱uri
	private String jpUri;
	//家谱提名
	private String jpTitle;
	//状态 1： 有效  0： 无效
	private String status;
	//任务状态 1：已认领 0：未认领 2 已协同（默认：0）
	private String task;
	//开放状态 1： 开放  0： 不开放（默认：0）
	private String isOpen;
	//协同情况  0:未分配(默认)  1:未协同  2:部分协同  3:全部协同
	private String cooperation;
	//认领人 user id （默认：空）
	private Integer leadId;
	//认领人姓名
	private String leadName;
	//认领人卡号
	private String leadCard;
	//认领时间（默认：空）
	private Date leadDate;
	//创建时间（当前时间）
	private Date createTime;
	//创建任务者id
	private Integer creatorId;
	//创建任务者姓名
	private String creatorName;
	//更新时间
	private Date updateTime;
	//更新人
	private Integer updatorId;
	//正题名
	private String title;
	//版本类型
	private String edition;
	//版本时间
	private String editionTemporal;
	//纂修者
	private String creator;
	//堂号
	private String tangh;
	//谱籍地
	private String place;
	//协同者列表
	private List<ApiTeamUserDto> teamUserList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getJpUri() {
		return jpUri;
	}
	public void setJpUri(String jpUri) {
		this.jpUri = jpUri;
	}
	public String getJpTitle() {
		return jpTitle;
	}
	public void setJpTitle(String jpTitle) {
		this.jpTitle = jpTitle;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public Integer getLeadId() {
		return leadId;
	}
	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}
	public Date getLeadDate() {
		return leadDate;
	}
	public void setLeadDate(Date leadDate) {
		this.leadDate = leadDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getUpdatorId() {
		return updatorId;
	}
	public void setUpdatorId(Integer updatorId) {
		this.updatorId = updatorId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getEditionTemporal() {
		return editionTemporal;
	}
	public void setEditionTemporal(String editionTemporal) {
		this.editionTemporal = editionTemporal;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getTangh() {
		return tangh;
	}
	public void setTangh(String tangh) {
		this.tangh = tangh;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Integer[] getIds() {
		return ids;
	}
	public void setIds(Integer[] ids) {
		this.ids = ids;
	}
	public String getLeadName() {
		return leadName;
	}
	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}
	public String getLeadCard() {
		return leadCard;
	}
	public void setLeadCard(String leadCard) {
		this.leadCard = leadCard;
	}
	public List<ApiTeamUserDto> getTeamUserList() {
		return teamUserList;
	}
	public void setTeamUserList(List<ApiTeamUserDto> teamUserList) {
		this.teamUserList = teamUserList;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCooperation() {
		return cooperation;
	}
	public void setCooperation(String cooperation) {
		this.cooperation = cooperation;
	}
}
