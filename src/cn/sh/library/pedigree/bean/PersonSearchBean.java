package cn.sh.library.pedigree.bean;

/**
 * Created by yesonme on 14-12-20.
 */
public class PersonSearchBean {
	private String familyName; // 姓
	private String fnameUri; // 姓URI
	private String time; // 朝代
	private String place; // 地域
	private String firstChar; // 首字母
	private String name; // 姓名
	private String uri; // uri
	private Integer tag; // 0先祖，1名人，2纂修者
	private String type; // type
	private String dataTypeId; // 规范数据类型区分
	private String role; // 角色

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataTypeId() {
		return dataTypeId;
	}

	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFnameUri() {
		return fnameUri;
	}

	public void setFnameUri(String fnameUri) {
		this.fnameUri = fnameUri;
	}
}
