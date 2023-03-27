package cn.sh.library.pedigree.webServices.model;

/**
 * 返回结果参数
 * @author chenss
 *
 */
public class UserLoginParmResult {

	protected String uid;// 用户ID 16 位随机数
	protected String cn; // 用户姓名 中文的姓名
	protected String sn; // 姓氏 中文用户姓氏
	protected String userPassword; // 密码 密码
	protected String mail; // 电子邮件 电子邮件
	protected String mobile; // 手机号码 手机号码
	protected String telephoneNumber;// 电话号码 电话号码
	protected String englishName; // 英文名 英文姓名
	protected String shLibIdentityID; // 有效证件类型ID 有效证件类型ID
	protected String shLibIdentityType;// 有效证件类型 有效证件类型
	protected String shLibIdentityNo; // 有效证件号 有效证件号
	protected String shLibSex; // 性别 男为男性；女为女性
	protected String shLibBirth; // 出生日期 出生日期
	protected String shLibPyName; // 拼音姓名 拼音姓名
	protected String shLibAskNo; // 卡号 卡号
	protected String shLibBorrower; // 借阅号借阅号

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getShLibIdentityID() {
		return shLibIdentityID;
	}

	public void setShLibIdentityID(String shLibIdentityID) {
		this.shLibIdentityID = shLibIdentityID;
	}

	public String getShLibIdentityType() {
		return shLibIdentityType;
	}

	public void setShLibIdentityType(String shLibIdentityType) {
		this.shLibIdentityType = shLibIdentityType;
	}

	public String getShLibIdentityNo() {
		return shLibIdentityNo;
	}

	public void setShLibIdentityNo(String shLibIdentityNo) {
		this.shLibIdentityNo = shLibIdentityNo;
	}

	public String getShLibSex() {
		return shLibSex;
	}

	public void setShLibSex(String shLibSex) {
		this.shLibSex = shLibSex;
	}

	public String getShLibBirth() {
		return shLibBirth;
	}

	public void setShLibBirth(String shLibBirth) {
		this.shLibBirth = shLibBirth;
	}

	public String getShLibPyName() {
		return shLibPyName;
	}

	public void setShLibPyName(String shLibPyName) {
		this.shLibPyName = shLibPyName;
	}

	public String getShLibAskNo() {
		return shLibAskNo;
	}

	public void setShLibAskNo(String shLibAskNo) {
		this.shLibAskNo = shLibAskNo;
	}

	public String getShLibBorrower() {
		return shLibBorrower;
	}

	public void setShLibBorrower(String shLibBorrower) {
		this.shLibBorrower = shLibBorrower;
	}
}
