package cn.sh.library.pedigree.common;


/**
 * 规范数据类型区分
 * 
 * @author chenss
 *
 */
public enum RoleGroup {
	/**
	 * 普通用户
	 */
	normal("1"),
	/**
	 * 专家
	 */
	zj("2"),
	/**
	 * 管理员
	 */
	admin("3");
	
	/** 定义枚举类型自己的属性 **/
	private final String group;

	private RoleGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public static RoleGroup getDataTypeGroup(String group) {
		for (RoleGroup cg : RoleGroup.values()) {
			if (cg.group.equals(group)) {
				return cg;
			}
		}
		return null;
	}
	 
}
