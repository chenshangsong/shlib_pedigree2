package cn.sh.library.pedigree.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by ly on 2014-12-18.
 */
public class Person {

	/** 姓名 */
	private String name;
	/** 英文姓名 */
	private String enName;

	private String category;

	/** uri */
	private String uri;

	/** 类型 */
	private String type;

	private String endYear;

	private String beginYear;
	/* 生 */
	private String birthday;

	private String imgUri;

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
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

	/* 卒 */
	private String deathday;
	/** 朝代 */
	private String dynasty;
	/** 字 */
	private String courtesyName;

	/** 号 */
	private String pseudonym;

	/** 行 */
	private String orderOfSeniority;
	/** 谥 */
	private String posthumousName;

	private String relatedWork;
	
	//from 人名规范库 chenss 20190624 begin
	private String speciality;
	
	private String description;
	
	private String gender;
	//from 人名规范库 chenss 20190624 end
	/**
	 * 人物图像
	 */
	private String personImgPath;
	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	private List<Map<String, String>> triples;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private List<Work> works;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getBeginYear() {
		return beginYear;
	}

	public void setBeginYear(String beginYear) {
		this.beginYear = beginYear;
	}

	public String getDynasty() {
		return dynasty;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}

	public List<Map<String, String>> getTriples() {
		return triples;
	}

	public void setTriples(List<Map<String, String>> triples) {
		this.triples = triples;
	}

	public List<Work> getWorks() {
		return works;
	}

	public void setWorks(List<Work> works) {
		this.works = works;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCourtesyName() {
		return courtesyName;
	}

	public void setCourtesyName(String courtesyName) {
		this.courtesyName = courtesyName;
	}

	public String getPseudonym() {
		return pseudonym;
	}

	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}

	public String getOrderOfSeniority() {
		return orderOfSeniority;
	}

	public void setOrderOfSeniority(String orderOfSeniority) {
		this.orderOfSeniority = orderOfSeniority;
	}

	public String getPosthumousName() {
		return posthumousName;
	}

	public void setPosthumousName(String posthumousName) {
		this.posthumousName = posthumousName;
	}

	public String getRelatedWork() {
		return relatedWork;
	}

	public void setRelatedWork(String relatedWork) {
		this.relatedWork = relatedWork;
	}

	public String getPersonImgPath() {
		return personImgPath;
	}

	public void setPersonImgPath(String personImgPath) {
		this.personImgPath = personImgPath;
	}
}
