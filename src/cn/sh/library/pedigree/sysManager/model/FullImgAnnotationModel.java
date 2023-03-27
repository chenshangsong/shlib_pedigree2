package cn.sh.library.pedigree.sysManager.model;
/**
 * 
 * @author chenxu
 *
 */

import java.io.Serializable;
import java.sql.Timestamp;

public class FullImgAnnotationModel implements Serializable {
	
	private static final long serialVersionUID = -3774842353424635944L;
	
	private int id;
	private String annotationId;
	private String content;
	private String userId;
	private String userName;
	private String canvasId;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	private String creator;
	private String lastUpdate;
	private String systype;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnnotationId() {
		return annotationId;
	}
	public void setAnnotationId(String annotationId) {
		this.annotationId = annotationId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCanvasId() {
		return canvasId;
	}
	public void setCanvasId(String canvasId) {
		this.canvasId = canvasId;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getSystype() {
		return systype;
	}
	public void setSystype(String systype) {
		this.systype = systype;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annotationId == null) ? 0 : annotationId.hashCode());
		result = prime * result + ((canvasId == null) ? 0 : canvasId.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((systype == null) ? 0 : systype.hashCode());
		result = prime * result + ((updatedTime == null) ? 0 : updatedTime.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FullImgAnnotationModel other = (FullImgAnnotationModel) obj;
		if (annotationId == null) {
			if (other.annotationId != null)
				return false;
		} else if (!annotationId.equals(other.annotationId))
			return false;
		if (canvasId == null) {
			if (other.canvasId != null)
				return false;
		} else if (!canvasId.equals(other.canvasId))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (id != other.id)
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (systype == null) {
			if (other.systype != null)
				return false;
		} else if (!systype.equals(other.systype))
			return false;
		if (updatedTime == null) {
			if (other.updatedTime != null)
				return false;
		} else if (!updatedTime.equals(other.updatedTime))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FullImgAnnotationModel [id=" + id + ", annotationid=" + annotationId + ", content=" + content
				+ ", userId=" + userId + ", userName=" + userName + ", canvasId=" + canvasId + ", createdtime="
				+ createdTime + ", updatedtime=" + updatedTime + ", creator=" + creator + ", lastUpdate=" + lastUpdate
				+ ", systype=" + systype + "]";
	}
	
}
