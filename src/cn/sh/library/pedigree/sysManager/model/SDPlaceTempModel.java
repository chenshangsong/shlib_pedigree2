package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class SDPlaceTempModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	
	private Long ID;
	private String orginalID;
	private String orginalPlace;
	private String labelS;
	private String labelT;
	private String guojiaS;
	private String shengS;
	private String shengT;
	private String shiS;
	private String shiT;
	private String xianS;
	private String xianT;
	private String generateUri;
	private String selfUri;
    private String point;
    private String oldpoint;
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getOrginalID() {
		return orginalID;
	}
	public void setOrginalID(String orginalID) {
		this.orginalID = orginalID;
	}
	public String getOrginalPlace() {
		return orginalPlace;
	}
	public void setOrginalPlace(String orginalPlace) {
		this.orginalPlace = orginalPlace;
	}
	public String getLabelS() {
		return labelS;
	}
	public void setLabelS(String labelS) {
		this.labelS = labelS;
	}
	public String getLabelT() {
		return labelT;
	}
	public void setLabelT(String labelT) {
		this.labelT = labelT;
	}
	public String getGuojiaS() {
		return guojiaS;
	}
	public void setGuojiaS(String guojiaS) {
		this.guojiaS = guojiaS;
	}
	public String getShengS() {
		return shengS;
	}
	public void setShengS(String shengS) {
		this.shengS = shengS;
	}
	public String getShengT() {
		return shengT;
	}
	public void setShengT(String shengT) {
		this.shengT = shengT;
	}
	public String getShiS() {
		return shiS;
	}
	public void setShiS(String shiS) {
		this.shiS = shiS;
	}
	public String getShiT() {
		return shiT;
	}
	public void setShiT(String shiT) {
		this.shiT = shiT;
	}
	public String getXianS() {
		return xianS;
	}
	public void setXianS(String xianS) {
		this.xianS = xianS;
	}
	public String getXianT() {
		return xianT;
	}
	public void setXianT(String xianT) {
		this.xianT = xianT;
	}
	public String getGenerateUri() {
		return generateUri;
	}
	public void setGenerateUri(String generateUri) {
		this.generateUri = generateUri;
	}
	public String getSelfUri() {
		return selfUri;
	}
	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getOldpoint() {
		return oldpoint;
	}
	public void setOldpoint(String oldpoint) {
		this.oldpoint = oldpoint;
	}
	
}
