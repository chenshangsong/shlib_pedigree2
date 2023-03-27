package cn.sh.library.pedigree.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.sysManager.mapper.SystemMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.SystemRoleMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.MenuModel;
import cn.sh.library.pedigree.sysManager.model.SysRoleMenuModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 上图胶卷
 * @author think
 */
public class PreloadUserList implements IfPreloadBean {
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private SystemRoleMenuMapper systemRoleMenuMapper;
	@Resource
	private SystemMenuMapper systemMenuMapper;
	private List<UserInfoModel> userList;
	private static PreloadUserList _instance;

	/**
	 * @return
	 */
	public static synchronized PreloadUserList getInstance() {
		if (null == _instance) {
			_instance = new PreloadUserList();
		}
		return _instance;
	}

	/**
	 * 得到用户列表
	 * @param dpudc
	 * @return
	 */
	@Override
	public void loadInfo() {
		if (_instance == null) {
			getInstance();
		}
		if (_instance.userInfoMapper == null) {
			_instance.userInfoMapper = userInfoMapper;
		}
		if (_instance.systemRoleMenuMapper == null) {
			_instance.systemRoleMenuMapper = systemRoleMenuMapper;
		}
		if (_instance.systemMenuMapper == null) {
			_instance.systemMenuMapper = systemMenuMapper;
		}
		_instance.userList = new ArrayList<UserInfoModel>();

		UserInfoModel _userInfo = new UserInfoModel();
		_userInfo.setRows(999999);
		int count = 0;
		_instance.userList = (List<UserInfoModel>) _instance.userInfoMapper.getTableDataListPage(_userInfo);
		try {
			for (UserInfoModel _resultMap : _instance.userList) {
				// 判断是否存在用户Id
				if (!StringUtilC.isEmpty(_resultMap.getId())) {
					// 取得权限对应的菜单信息
					SysRoleMenuModel roleMenu = _instance.systemRoleMenuMapper.getRoleOfMenu(_resultMap.getRoleId());
					if (roleMenu != null) {
						// 取得菜单详细信息
						List<MenuModel> menuList = new ArrayList<MenuModel>();
						String[] menus = roleMenu.getMenuId().split(",");
						for (String menuId : menus) {
							MenuModel menu = _instance.systemMenuMapper
									.getMenuDetail(menuId);
							menuList.add(menu);
						}
						Collections.sort(menuList, new Comparator<MenuModel>() {
							public int compare(MenuModel arg0, MenuModel arg1) {
								return arg0.getMenuSort().compareTo(
										arg1.getMenuSort());
							}
						});
						_resultMap.setMenuList(menuList);
						// 角色名称
						_resultMap.setRoleName(DataTypeMap
								.getDataRoleName(_resultMap.getRoleId()));
						count++;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("PreloadUserList exception:" + e);
		}
		// 控制台打印输出
		System.out.println("PreloadUserList共加载" + _instance.userList.size() + "个用户");
	}

	@Override
	public void reloadInfo() {
		loadInfo();
	}

	/**
	 * 根据用户ID，获取用户信息
	 * @param workUri
	 * @return
	 */
	public static UserInfoModel getUserById(String id) {
		if (!StringUtilC.isEmpty(id)) {
			for (UserInfoModel userInfoModel : _instance.userList) {
				if (StringUtilC.getString(userInfoModel.getId()).equals(id)) {
					return userInfoModel;
				}
			}
		}
		return new UserInfoModel();
	}

	/**
	 * 根据用户ID，获取用户信息
	 * @param workUri
	 * @return
	 */
	public static UserInfoModel getUserByUserId(String userid) {
		if (!StringUtilC.isEmpty(userid)) {
			for (UserInfoModel userInfoModel : _instance.userList) {
				if (userInfoModel.getUserId().equals(userid)) {
					return userInfoModel;
				}
			}
		}
		return new UserInfoModel();
	}

	/**
	 * UserList
	 * @param workUri
	 * @return
	 */
	public static List<UserInfoModel> getUserList() {
		return _instance.userList;
	}
}
