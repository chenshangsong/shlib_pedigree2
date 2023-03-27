package cn.sh.library.pedigree.common;


/**
 * 语言类型区分
 * 
 * @author chenss
 *
 */
public enum LanguageGroup {
	/**
	 * 堂号
	 */
	Chs("@chs"),
	/**
	 * 姓氏
	 */
	Cht("@cht"),
	/**
	 * 机构
	 */
	En("@en"),
	/**
	 * 先祖
	 */
	Non("");

	/** 定义枚举类型自己的属性 **/
	private final String group;

	private LanguageGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public static LanguageGroup getDataTypeGroup(String group) {
		for (LanguageGroup cg : LanguageGroup.values()) {
			if (cg.group.equals(group)) {
				return cg;
			}
		}
		return null;
	}

}
