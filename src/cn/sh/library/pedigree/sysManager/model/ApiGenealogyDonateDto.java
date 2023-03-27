package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.Date;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;

public class ApiGenealogyDonateDto extends DtoJsonPageData implements Serializable {
	/**
	 * @author 陈铭
	 */
	private static final long serialVersionUID = 1L;
	//主键id(自动生成)
	private Integer id;
	//用户id
	private Integer uid;
	//谱名
	private String gName;
	//谱籍地
	private String gPlace;
	//撰修者
	private String gCreator;
	//撰修时间
	private Date gCreateTime;
	//堂号
	private String gHall;
	//摘要
	private String gSummary;
	//名人始迁祖 id
	private Integer famousId;
	//上传文件相对路径
	private String filePath;
	//状态 1: 有效0：无效
	private Integer status;
	//捐赠人
	private String creator;
	//修改人
	private String updator;
	//修改时间
	private Date updateTime;
	//是否馆藏 1：是 0：否
	private Integer isCollect;
	//是否有电子证书 1：有 0：没有
	private Integer isCertificate;
	//捐赠人卡号
	private Integer cardId;
	//捐赠人地址
	private String place;
	//捐赠时间(自动生成)
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
	public String getgName() {
		return gName;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}
	public String getgPlace() {
		return gPlace;
	}
	public void setgPlace(String gPlace) {
		this.gPlace = gPlace;
	}
	public String getgCreator() {
		return gCreator;
	}
	public void setgCreator(String gCreator) {
		this.gCreator = gCreator;
	}
	public Date getgCreateTime() {
		return gCreateTime;
	}
	public void setgCreateTime(Date gCreateTime) {
		this.gCreateTime = gCreateTime;
	}
	public String getgHall() {
		return gHall;
	}
	public void setgHall(String gHall) {
		this.gHall = gHall;
	}
	public String getgSummary() {
		return gSummary;
	}
	public void setgSummary(String gSummary) {
		this.gSummary = gSummary;
	}
	public Integer getFamousId() {
		return famousId;
	}
	public void setFamousId(Integer famousId) {
		this.famousId = famousId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(Integer isCollect) {
		this.isCollect = isCollect;
	}
	public Integer getIsCertificate() {
		return isCertificate;
	}
	public void setIsCertificate(Integer isCertificate) {
		this.isCertificate = isCertificate;
	}
	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
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

