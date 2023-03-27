package cn.sh.library.pedigree.bean;

/**
 * Created by yesonme on 15-9-27.
 */
public class FullLink4ESJPSearchBean {
	private boolean flag = false;
	private String accessLevelFlg = "";
	private String doi = "";
	private String ipAddress = "";
	private String dois = "";
	//经过全文处理后的链接
	private String value = "";
	//索引
	private int i = 0;
	//索书号
	private String tempcallNo = "";
	//是否有封面
	private String hasimg = "";
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getAccessLevelFlg() {
		return accessLevelFlg;
	}
	public void setAccessLevelFlg(String accessLevelFlg) {
		this.accessLevelFlg = accessLevelFlg;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDois() {
		return dois;
	}
	public void setDois(String dois) {
		this.dois = dois;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public String getTempcallNo() {
		return tempcallNo;
	}
	public void setTempcallNo(String tempcallNo) {
		this.tempcallNo = tempcallNo;
	}
	public String getHasimg() {
		return hasimg;
	}
	public void setHasimg(String hasimg) {
		this.hasimg = hasimg;
	}
	
}
