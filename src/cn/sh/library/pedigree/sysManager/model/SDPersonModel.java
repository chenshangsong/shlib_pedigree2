package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class SDPersonModel  implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long ID;
	private String originalID;
	private String originalWord;
	private String wordType;
	private String generateUri;
	private String selfUri;

	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getOriginalID() {
		return originalID;
	}
	public void setOriginalID(String originalID) {
		this.originalID = originalID;
	}
	public String getOriginalWord() {
		return originalWord;
	}
	public void setOriginalWord(String originalWord) {
		this.originalWord = originalWord;
	}
	public String getWordType() {
		return wordType;
	}
	public void setWordType(String wordType) {
		this.wordType = wordType;
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

}
