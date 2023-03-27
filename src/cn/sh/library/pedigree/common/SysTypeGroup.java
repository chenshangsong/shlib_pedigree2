package cn.sh.library.pedigree.common;


/**
 * 系统类型区分
 * 
 * @author chenss
 *
 */
public enum SysTypeGroup {
	/**
	 *家谱
	 */
	jp("1"),
	/**
	 * 盛档
	 */
	sd("2"),
	/**
	 * 古籍
	 */
	gj("3"),
	/**
	 * 名人
	 */
	mr("4"),
	/**
	 * 手稿
	 */
	sg("5"),
	/**
	 * 手稿手机端
	 */
	sgMb("6");
	
	/** 定义枚举类型自己的属性 **/
	private final String group;

	private SysTypeGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public static SysTypeGroup getDataTypeGroup(String group) {
		for (SysTypeGroup cg : SysTypeGroup.values()) {
			if (cg.group.equals(group)) {
				return cg;
			}
		}
		return null;
	}
	 
}
