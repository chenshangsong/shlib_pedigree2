package cn.sh.library.pedigree.controller.sysmanager;

import java.util.List;

import javax.annotation.Resource;

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
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.sysManager.service.LoginNormalService;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 用户登录Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Resource
	private LoginNormalService loginNormalService;
	/**
	 * 用户列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public ModelAndView userList() throws Exception {
		// 使用ModelAndView保存对象于下一页面
		UserInfoModel _info = new UserInfoModel();
		DtoJsonPageData grid = new DtoJsonPageData(this);
		_info.setRows(999999);
		List<UserInfoModel> userInfoList = userInfoMapper.getTableDataListPage(_info);
		grid.print2JsonObj(_info, userInfoList);
		modelAndView.setViewName("sysmanager/userInfo/userList");
		modelAndView.addObject("data", grid);
		return modelAndView;
	}

	/**
	 * 用户列表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doUserList", method = RequestMethod.POST)
	public String userList(@RequestBody UserInfoModel _userInfo)
			throws Exception {
		_userInfo.setRows(999999);
		List<UserInfoModel> userInfoList = userInfoMapper.getTableDataListPage(_userInfo);
		jsonResult.put(result, userInfoList);
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 用户编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/toEditUser", method = RequestMethod.GET)
	public ModelAndView toEditUser(@Param(value = "id") String id)
			throws Exception {
		// 使用ModelAndView保存对象于下一页面
		UserInfoModel _info = userInfoMapper.getUserById(id);
		modelAndView.addObject("userInfo", _info);
		modelAndView.setViewName("sysmanager/userInfo/userEdit");
		return modelAndView;
	}
	/**
	 * 个人信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/personInfo", method = RequestMethod.GET)
	public ModelAndView personInfo()
			throws Exception {
		modelAndView.setViewName("sysmanager/userInfo/personInfo");
		return modelAndView;
	}

	/**
	 * 用户新增方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doInsertUser", method = RequestMethod.POST)
	public String doInsertUser() throws Exception {
		try {
			UserInfoModel _model = new UserInfoModel();
			_model.setUserId("song2");
			_model.setUserName("新用户");
			_model.setUserPwd(FWConstants.defaultPwd);
			//新规
			 userInfoMapper.insertUser(_model);
			//System.out.println("是否成功：" + mm);
			jsonResult.put(result, FWConstants.result_success);
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doUpdateUserRoleById", method = RequestMethod.POST)
	public String doUpdateUserRoleById(@RequestBody UserInfoModel _userInfo)
			throws Exception {
		try {
			int ifSucess = userInfoMapper.updateUserRoleById(_userInfo);
			// 如果成功
			if (ifSucess > 0) {
				jsonResult.put(result, FWConstants.result_success);
			}
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doUpdateUserById", method = RequestMethod.POST)
	public String doUpdateUserById(@RequestBody UserInfoModel _userInfo)
			throws Exception {
		try {
			//获取外网要更新信息
			UserInfoModel userInfo = userInfoMapper.getUserById(StringUtilC.getString(_userInfo.getId()));
		
			int ifSucess = userInfoMapper.updateUserById(userInfo);
			System.out.println("是否成功：" + ifSucess);
			// 如果成功
			if (ifSucess > 0) {
				jsonResult.put(result, FWConstants.result_success);
			}
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	
}