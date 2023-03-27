package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.MenuModel;

@Repository
public interface SystemMenuMapper {

	/**
	 * 单个菜单取得
	 */
	MenuModel getEditMenu(@Param(value = "id") Integer id);
	
	/**
	 * 菜单信息
	 */
	List<MenuModel> getMenuList();

	/**
	 * 权限对应的菜单信息
	 */
	MenuModel getMenuDetail(@Param(value = "menuId") String menuId);
	
	/**
	 * 更新菜单
	 */
	Integer updateMenu(MenuModel menuModel);
}
