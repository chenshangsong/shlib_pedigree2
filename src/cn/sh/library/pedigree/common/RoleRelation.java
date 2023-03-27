package cn.sh.library.pedigree.common;


/**
 * 
 * 
 * @author chenss
 *
 */
public enum RoleRelation {
	
	son("1"),
	wife("2"),
	father("3"),
	me("4");
	
	private final String group;

	private RoleRelation(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public static RoleRelation getDataTypeGroup(String group) {
		for (RoleRelation cg : RoleRelation.values()) {
			if (cg.group.equals(group)) {
				return cg;
			}
		}
		return null;
	}
	 
}
