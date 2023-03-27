package cn.sh.library.pedigree.common.onlineInfomation;
/**
 * 
 * @author chenss
 *
 */
/*
 * 统计在线用户人数及用户信息
 * 1、在线用户的数量
 * 2、在线用户的信息：sessionId,ip地址，上一次访问时间
 * 
 * 做法：
 * 1、统计用户数量，通过HttpSessionListener完成             --定义number来存储数量（通过ServletContext设置，获取）
 * 2、统计用户信息,通过ServletRequestListener完成     --定义List容器来存储所有用户信息（通过ServletContext设置，获取）
 * 
 * 备注：使用时，需要自己打开不同的浏览器，才会有测试效果
 */
public class OnLineUser {

	private String sessionId;
	private String ip;
	private String firstTime;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(String firstTime) {
		this.firstTime = firstTime;
	}
	
}