package cn.sh.library.pedigree.common.dataImport;


/**
 * 规范数据类型区分
 * 
 * @author chenss
 *
 */
public enum DataTypeGroup {
	/**
	 * 堂号
	 */
	Ancestraltemple("1"),
	/**
	 * 姓氏
	 */
	FamilyName("2"),
	/**
	 * 机构
	 */
	Organization("3"),
	/**
	 * 先祖
	 */
	Ancestors("4"),
	/**
	 * 名人
	 */
	Famous("5"),
	/**
	 * 纂修者
	 */
	Writer("6"),
	/**
	 * 谱籍
	 */
	Place("7"),
	/**
	 * 谱名
	 */
	Title("8"),
	/**
	 * 取值词表
	 */
	Category("9"),
	/**
	 *书目
	 */
	Work("10");
	/** 定义枚举类型自己的属性 **/
	private final String group;

	private DataTypeGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public static DataTypeGroup getDataTypeGroup(String group) {
		for (DataTypeGroup cg : DataTypeGroup.values()) {
			if (cg.group.equals(group)) {
				return cg;
			}
		}
		return null;
	}
	 
}
