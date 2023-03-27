package cn.sh.library.pedigree.controller.sysmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Item;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sysManager.mapper.DOIChangeAccessLevelMapper;
import cn.sh.library.pedigree.sysManager.mapper.SystemMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.SystemRoleMenuMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.MenuModel;
import cn.sh.library.pedigree.sysManager.model.RoleSettingModel;
import cn.sh.library.pedigree.sysManager.model.SysRoleMenuModel;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 系统设置Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/systemSetting")
public class SystemSettingController extends BaseController {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private SystemMenuMapper systemMenuMapper;

	@Autowired
	private SystemRoleMenuMapper systemRoleMenuMapper;

	@Resource
	private WorkService workService;
	@Resource
	public DOIChangeAccessLevelMapper doiChangeAccessLevelMapper;

	/**
	 * 系统设置主页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main() {
		return "sysmanager/systemsetting/setting";
	}

	/**
	 * 权限下拉菜单选择时
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loadRoleOfMenu", method = RequestMethod.POST)
	public Map<String, Object> loadRoleOfMenu(RoleSettingModel model) {

		// 所有菜单
		List<MenuModel> allMenu = systemMenuMapper.getMenuList();
		// 取得权限对应的菜单信息
		List<SysRoleMenuModel> roleMenuList = systemRoleMenuMapper.getRoleOfMenuByRoleId(model.getRoleId());
		// 'checkbox'勾选
		if (roleMenuList != null && roleMenuList.size() > 0) {
			for (MenuModel menu : allMenu) {
				for (SysRoleMenuModel roleMenu : roleMenuList) {
					String menuId = roleMenu.getMenuId();
					if (menu.getMenuId().equals(menuId)) {
						menu.setSelected(true);
						break;
					}
				}
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("works", allMenu);
		return result;
	}

	/**
	 * Doi修改页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/toDoi", method = RequestMethod.GET)
	public ModelAndView toDoi() throws Exception {

		modelAndView.setViewName("sysmanager/systemsetting/doi");
		return modelAndView;
	}

	/**
	 * Doi的开和关
	 * 
	 * @param doi
	 * @param dataType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updDoi", method = RequestMethod.POST)
	public Map<String, Object> updDoi(String doi, String flg) {
		Map<String, Object> result = new HashMap<>();
		if (StringUtilC.isEmpty(doi)) {
			return result;
		}
		String[] dois = doi.split(";");
		for (String thisDoi : dois) {
			try {
				List<Item> _list = workService.getItemListByDoi(thisDoi);
				if(_list ==null ||_list.size()==0){
					System.out.println("未查询到数据的DOI:"+thisDoi);
					result.put(thisDoi, "未检索到数据");
					doiChangeAccessLevelMapper.insertDOIChangeInfo("", thisDoi, "", "", "", "1");
				}
				else{
					for (Item item : _list) {
						workService.updateAccessLevelByItemUri(item, thisDoi, flg);
					}
					result.put(thisDoi, "成功");
				}
			} catch (Exception e) {
				System.out.println("DOI更新失败"+e);
				result.put(thisDoi, "失败");
			}
		}

		return result;
	}

	/**
	 * 权限设置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/roleSetting", method = RequestMethod.GET)
	public ModelAndView roleSetting(HttpSession hs) throws Exception {

		try {

			RoleSettingModel model = new RoleSettingModel();
			model.setRoleId(getUser(hs).getRoleId());
			model.setRoles(new DataTypeMap().ROLE_MAP);
			modelAndView.setViewName("sysmanager/systemsetting/role");
			modelAndView.addObject("data", model);
			return modelAndView;

		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 菜单列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/menuList", method = RequestMethod.GET)
	public ModelAndView menuList() throws Exception {

		try {

			List<MenuModel> menuList = systemMenuMapper.getMenuList();
			modelAndView.setViewName("sysmanager/systemsetting/menuList");
			modelAndView.addObject("data", menuList);
			return modelAndView;

		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 权限保存
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRoleOfMenu", method = RequestMethod.POST)
	public String saveRoleOfMenu(@RequestBody RoleSettingModel model,
			HttpSession hs) throws Exception {

		try {
			//清除权限菜单
			int _ifSucess = systemRoleMenuMapper.cleanRoleOfMenuByRoleId(model.getRoleId());
			if(_ifSucess > 0) {
				// 更新权限的菜单
				SysRoleMenuModel roleMenuModel = new SysRoleMenuModel();
				roleMenuModel.setMenuId(model.getMenuId());
				roleMenuModel.setRoleId(model.getRoleId());
				roleMenuModel.setUPDATED_BY(StringUtilC.getString(getUser(hs).getId()));
				//更新权限的菜单
				int ifSucess = systemRoleMenuMapper.updateRoleOfMenu(roleMenuModel);
				if (ifSucess > 0) {
					jsonResult.put(result, FWConstants.result_success);
				}
			}
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 菜单修改页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/toEditMenu", method = RequestMethod.GET)
	public ModelAndView toEditMenu(@Param(value = "id") Integer id)
			throws Exception {

		try {

			if (id == null) {
				return null;
			}

			MenuModel model = systemMenuMapper.getEditMenu(id);
			modelAndView.setViewName("sysmanager/systemsetting/menuEdit");
			modelAndView.addObject("menu", model);

		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}

		return modelAndView;
	}

	/**
	 * 菜单修改
	 */
	@ResponseBody
	@RequestMapping(value = "/editMenu", method = RequestMethod.POST)
	public String editMenu(@RequestBody MenuModel model, HttpSession hs)
			throws Exception {

		try {

			if (StringUtilC.isEmpty(model.getMenuName())) {
				jsonResult.put(result, FWConstants.result_error);
			} else {
				// 更新菜单
				model.setUPDATED_BY(StringUtilC.getString(getUser(hs).getId()));
				//翻译转换-去重特殊字符
				model=(MenuModel)StringUtilC.convertModel(model);
				int ifSucess = systemMenuMapper.updateMenu(model);
				if (ifSucess > 0) {
					jsonResult.put(result, FWConstants.result_success);
				}
			}
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

}
