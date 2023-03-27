package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.Date;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class ApiTeamUserDto extends DtoJsonPageData implements Serializable {
	/**
	 * @author 陈铭
	 */
	private static final long serialVersionUID = 1L;
	
	//id（自增列）
	private Integer id;
	//任务id（根据此id增删查）
	private Integer taskId;
	//认领人id
	private Integer leadId;
	//认领人姓名
	private String leadName;
	//认领人卡号
	private String leadCard;
	//协同者id
	private Integer coopId;
	//协同者姓名
	private String coopName;
	//协同者卡号
	private String coopCard;
	//协同状态  （0:未确认协同; 1:已确认协同; 默认0）
	private String coopStatus;
	//协同者确认协同的时间
	private Date coopTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCoopName() {
		return coopName;
	}
	public void setCoopName(String coopName) {
		this.coopName = coopName;
	}
	public Integer getLeadId() {
		return leadId;
	}
	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getCoopId() {
		return coopId;
	}
	public void setCoopId(Integer coopId) {
		this.coopId = coopId;
	}
	public String getCoopCard() {
		return coopCard;
	}
	public void setCoopCard(String coopCard) {
		this.coopCard = coopCard;
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
	public String getCoopStatus() {
		return coopStatus;
	}
	public void setCoopStatus(String coopStatus) {
		this.coopStatus = coopStatus;
	}
	public Date getCoopTime() {
		return coopTime;
	}
	public void setCoopTime(Date coopTime) {
		this.coopTime = coopTime;
	}
}
