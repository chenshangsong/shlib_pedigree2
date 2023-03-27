package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.SysRoleMenuModel;

@Repository
public interface SystemRoleMenuMapper {

	/**
	 * 权限的菜单信息
	 */
	SysRoleMenuModel getRoleOfMenu(@Param(value = "roleId") String roleId);
	/**
	 * 权限的菜单信息
	 * @param roleId
	 * @return
	 */
	List<SysRoleMenuModel> getRoleOfMenuByRoleId(@Param(value = "roleId") String roleId);
	
	/**
	 * 更新权限的菜单
	 */
	Integer updateRoleOfMenu(SysRoleMenuModel roleMenuModel);
	/**
	 * 清除权限的菜单
	 * @param roleId
	 * @return
	 */
	Integer cleanRoleOfMenuByRoleId(@Param(value = "roleId") String roleId);
}
