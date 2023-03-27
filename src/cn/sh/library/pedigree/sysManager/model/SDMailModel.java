package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;

public class SDMailModel  implements Serializable {

	private static final long serialVersionUID = 3096154202413606831L;
	private Long id;
	private Long originalID;
	private String author;
	private String authorUri;
	private String title;
	private String sender;
	private String sendUri;
	private String receiver;
	private String receiverUri;
	private String place;
	private String placeUri;
	private String subject;
	private String subjectUri;
	private String selfUri;
	private String matchSR;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOriginalID() {
		return originalID;
	}
	public void setOriginalID(Long originalID) {
		this.originalID = originalID;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorUri() {
		return authorUri;
	}
	public void setAuthorUri(String authorUri) {
		this.authorUri = authorUri;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSendUri() {
		return sendUri;
	}
	public void setSendUri(String sendUri) {
		this.sendUri = sendUri;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiverUri() {
		return receiverUri;
	}
	public void setReceiverUri(String receiverUri) {
		this.receiverUri = receiverUri;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getPlaceUri() {
		return placeUri;
	}
	public void setPlaceUri(String placeUri) {
		this.placeUri = placeUri;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubjectUri() {
		return subjectUri;
	}
	public void setSubjectUri(String subjectUri) {
		this.subjectUri = subjectUri;
	}
	public String getSelfUri() {
		return selfUri;
	}
	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}
	public String getMatchSR() {
		return matchSR;
	}
	public void setMatchSR(String matchSR) {
		this.matchSR = matchSR;
	}

}
