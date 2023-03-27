package cn.sh.library.pedigree.dto.sysmanager;

public class DataEditDto {
	/**
	 * 谓语英文名称：如：courtesyName
	 */
	private String oldEnName;
	/**
	 * 谓语中文名称：如：字
	 */
	private String oldCnName;
	/**
	 * 谓语URI如：http://gen.library.sh.cn/vocab/migration
	 */
	private String oldCnNameUri;
	/**
	 * 旧值（uri或汉字）
	 */
	private String oldValue;
	/**
	 * 旧值（lable值）
	 */
	private String oldValueCn;
	
	/**
	 * 字段注释
	 */
	private String oldComment;
	/**
	 * 新值
	 */
	private String newValue;
	
	/**
	 * uri时，新值中文名称
	 */
	private String newValueCn;
	/**
	 * 是否显示开关
	 */
	private String openFlg;
	/**
	 * 是否多值
	 */
	private String isMore="0";
	/**
	 * 是否是URI
	 */
	private String isUri="0";

	public String getOldEnName() {
		return oldEnName;
	}

	public void setOldEnName(String oldEnName) {
		this.oldEnName = oldEnName;
	}

	public String getOldCnName() {
		return oldCnName;
	}

	public void setOldCnName(String oldCnName) {
		this.oldCnName = oldCnName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getOldValueCn() {
		return oldValueCn;
	}

	public void setOldValueCn(String oldValueCn) {
		this.oldValueCn = oldValueCn;
	}

	public String getOldCnNameUri() {
		return oldCnNameUri;
	}

	public void setOldCnNameUri(String oldCnNameUri) {
		this.oldCnNameUri = oldCnNameUri;
	}

	public String getOpenFlg() {
		return openFlg;
	}

	public void setOpenFlg(String openFlg) {
		this.openFlg = openFlg;
	}

	public String getIsUri() {
		return isUri;
	}

	public void setIsUri(String isUri) {
		this.isUri = isUri;
	}

	public String getOldComment() {
		return oldComment;
	}

	public void setOldComment(String oldComment) {
		this.oldComment = oldComment;
	}

	public String getIsMore() {
		return isMore;
	}

	public void setIsMore(String isMore) {
		this.isMore = isMore;
	}

	public String getNewValueCn() {
		return newValueCn;
	}

	public void setNewValueCn(String newValueCn) {
		this.newValueCn = newValueCn;
	}
}