package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.Random;

import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.utils.StringUtilC;

public class SurnameModel extends DtoJsonPageData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private final String[] colors = new String[]{
			"#D6D6D6","#D2D2AC","#FFFF31","#CDA837","#D28411","#5CC45C",
			"#08BBE8","#D5AD86","#D5AD86","#FEFE99","#FDCD98"};

	private String identifier = "";
	private String familyNameT = "";
	private String familyNameS = "";
	private String uri = "";
	private String pinyin = "";
	private String pinyin4 = "";
	private String originally = "";
	private String genealogyCnt = "";
	private String celebrityCnt = "";
	private String initial = "";
	private String randColor = "";
	private String description = "";

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getFamilyNameT() {
		return familyNameT;
	}

	public void setFamilyNameT(String familyNameT) {
		this.familyNameT = familyNameT;
	}

	public String getFamilyNameS() {
		return familyNameS;
	}

	public void setFamilyNameS(String familyNameS) {
		this.familyNameS = familyNameS;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPinyin4() {
		return pinyin4;
	}

	public void setPinyin4(String pinyin4) {
		this.pinyin4 = pinyin4;
	}

	public String getOriginally() {
		return originally;
	}

	public void setOriginally(String originally) {
		this.originally = originally;
	}

	public String getGenealogyCnt() {
		return genealogyCnt;
	}

	public void setGenealogyCnt(String genealogyCnt) {
		this.genealogyCnt = genealogyCnt;
	}

	public String getCelebrityCnt() {
		return celebrityCnt;
	}

	public void setCelebrityCnt(String celebrityCnt) {
		this.celebrityCnt = celebrityCnt;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getRandColor() {
		return randColor;
	}

	public void setRandColor(String randColor) {
		if (StringUtilC.isEmpty(randColor)){
			randColor = this.getRandColorCode();
		}
		this.randColor = randColor;
	}

	private String getRandColorCode() {

		Random random = new Random();
		int rdmNum = random.nextInt(this.colors.length - 1);
		return this.colors[rdmNum];
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
