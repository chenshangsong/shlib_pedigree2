package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入书目时，使用personModel
 * 
 * @author chenss
 *
 */
public class PersonModel implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private String selfUri;
	/**
	 * 先祖类型
	 */
	private String roleUri;
	/**
	 * 全
	 */
	private String allName;
	/**
	 * 字
	 */
	private String zhi;
	/**
	 * 号
	 */
	private String hao;
	/**
	 * 行
	 */
	private String hang;
	/**
	 * 谥号
	 */
	private String shi;
	/**
	 * 时代
	 */
	private String shidai;
	/**
	 * 时代URI
	 */
	private String shidaiUri;
	
	private String labelS;
	private String labelT;
	//父
	private String childOf;
	//生于
	private String birthday;
	//死于
    private String deathday;
	//说明
	private String description;
	//谱名
	private String genealogyName;
	//字辈
	private String generationCharacter;
	//相关作品
	private String relatedWork;
	
    //姓氏Uri
    private String familyName;
	List<PersonModel> personList = new ArrayList<PersonModel>();

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getZhi() {
		return zhi;
	}

	public void setZhi(String zhi) {
		this.zhi = zhi;
	}

	public String getHao() {
		return hao;
	}

	public void setHao(String hao) {
		this.hao = hao;
	}

	public String getHang() {
		return hang;
	}

	public void setHang(String hang) {
		this.hang = hang;
	}

	public String getShidai() {
		return shidai;
	}

	public void setShidai(String shidai) {
		this.shidai = shidai;
	}

	public String getShidaiUri() {
		return shidaiUri;
	}

	public void setShidaiUri(String shidaiUri) {
		this.shidaiUri = shidaiUri;
	}

	public String getRoleUri() {
		return roleUri;
	}

	public void setRoleUri(String roleUri) {
		this.roleUri = roleUri;
	}

	public String getShi() {
		return shi;
	}

	public void setShi(String shi) {
		this.shi = shi;
	}

	public List<PersonModel> getPersonList() {
		return personList;
	}

	public void setPersonList(List<PersonModel> personList) {
		this.personList = personList;
	}

	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName = allName;
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

	public String getChildOf() {
		return childOf;
	}

	public void setChildOf(String childOf) {
		this.childOf = childOf;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDeathday() {
		return deathday;
	}

	public void setDeathday(String deathday) {
		this.deathday = deathday;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenealogyName() {
		return genealogyName;
	}

	public void setGenealogyName(String genealogyName) {
		this.genealogyName = genealogyName;
	}

	public String getGenerationCharacter() {
		return generationCharacter;
	}

	public void setGenerationCharacter(String generationCharacter) {
		this.generationCharacter = generationCharacter;
	}

	public String getRelatedWork() {
		return relatedWork;
	}

	public void setRelatedWork(String relatedWork) {
		this.relatedWork = relatedWork;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

}
