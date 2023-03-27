package  cn.sh.library.pedigree.framework.model;



public class DtoMessage extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -207382037574663996L;
	
	private String msgId;
	private String msgInfo;
	private String msgTarget;
	private String msgTargetIndex;
 
	public DtoMessage(Exception e){
		this.setMsgId("-1");
		this.setMsgInfo(e.getMessage());
	}
	
	public DtoMessage(String msgId,String msgInfo){
		this.setMsgId(msgId);
		this.setMsgInfo(msgInfo);
		this.setMsgTarget(msgId);
	}
	
	public DtoMessage(String msgId,String msgInfo,String msgTarget){
		this.setMsgId(msgId);
		this.setMsgInfo(msgInfo);
		this.setMsgTarget(msgTarget);
	}
	
	public DtoMessage(String msgId,String msgInfo,String msgTarget,String msgTargetIndex){
		this.setMsgId(msgId);
		this.setMsgInfo(msgInfo);
		this.setMsgTarget(msgTarget);
		this.setMsgTargetIndex(msgTargetIndex);
	}

	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the msgInfo
	 */
	public String getMsgInfo() {
		return msgInfo;
	}

	/**
	 * @param msgInfo the msgInfo to set
	 */
	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public String getMsgTarget() {
		return msgTarget;
	}

	public void setMsgTarget(String msgTarget) {
		this.msgTarget = msgTarget;
	}

	public String getMsgTargetIndex() {
		return msgTargetIndex;
	}

	public void setMsgTargetIndex(String msgTargetIndex) {
		this.msgTargetIndex = msgTargetIndex;
	}
	
}
