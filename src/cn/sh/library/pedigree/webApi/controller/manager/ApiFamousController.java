package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.framework.util.PreloadUserList;
import cn.sh.library.pedigree.sysManager.model.ApiFamousDto;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiFamousService;

/**
 * @author 陈铭
 */
@Controller
@RequestMapping("/webapi/manager/famous")
public class ApiFamousController extends BaseController {
	
	@Resource
	private ApiFamousService apiFamousService;
	
	/**
	 * 根据 用户id(uid)与 查询条件 , 获取捐赠信息-列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(ApiFamousDto search, Pager pager){
		
		jsonResult = new HashMap<>();
		
		//判断用户是否为空
		if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(search.getUid())).getId())){
		
			DtoJsonPageData grid = new DtoJsonPageData(this);
			
			ArrayList<ApiFamousDto> list = apiFamousService.getApiFamousListPage(search);
			// 设定输出对象
			grid.print2JsonObj(search, list);
			pager.calcPageCount(StringUtilC.getLong(String.valueOf(search.getRecords())));
		
			jsonResult.put("pager", pager);
			jsonResult.put("datas", list);
			jsonResult.put(result, FWConstants.result_success);
			
		} else {
			jsonResult.put(result, FWConstants.result_usernull);
			jsonResult.put("errCode", FWConstants.result_usernull);
			jsonResult.put("errMsg", FWConstants.result_usernull_msg);
		}
		
		return JSonUtils.toJSon(jsonResult);
	}
	
	/**
	 * 根据id,查询捐赠信息
	 */
	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(@Valid Integer id, @Valid Integer uid){
	
		jsonResult = new HashMap<>();
		
		//判断用户是否为空
		if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(uid)).getId())){
			
			//判断id是否为空
			if(!StringUtilC.isEmpty(id)){
		
				ApiFamousDto dto = apiFamousService.getApiFamousById(id);
			
				jsonResult.put("data", dto);
				jsonResult.put(result, FWConstants.result_success);
				
			} else {
				jsonResult.put(result, FWConstants.result_error);
				jsonResult.put("errCode", FWConstants.result_error);
				jsonResult.put("errMsg", FWConstants.result_error_msg);
			}
			
		} else {
			jsonResult.put(result, FWConstants.result_usernull);
			jsonResult.put("errCode", FWConstants.result_usernull);
			jsonResult.put("errMsg", FWConstants.result_usernull_msg);
		}
		
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 *添加捐赠信息
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ApiFamousDto dto)throws Exception{
		
		try {
			jsonResult = new HashMap<>();
			
			//判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(dto.getUid())).getId())){
				
				dto.setCreatedUser(dto.getUid());
					
				//添加
				Integer ifSucess = apiFamousService.insertApiFamous(dto);
				
				//如果成功
				if (ifSucess >= 0) {
					jsonResult.put(result, FWConstants.result_success);
				}
					
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
				jsonResult.put("errCode", FWConstants.result_usernull);
				jsonResult.put("errMsg", FWConstants.result_usernull_msg);
			}
			return JSonUtils.toJSon(jsonResult);
			
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			jsonResult.put("errCode", FWConstants.result_error);
			jsonResult.put("errMsg", FWConstants.result_error_msg);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 *根据id,更新捐赠信息
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ApiFamousDto dto)throws Exception{
		
		try {
			jsonResult = new HashMap<>();
			
			//判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(dto.getUid())).getId())){
				
				//判断id是否为空
				if(!StringUtilC.isEmpty(dto.getId())){
				
					//更新
					int ifSucess = apiFamousService.updateApiFamous(dto);
					
					//如果成功
					if (ifSucess>= 0) {
						jsonResult.put(result, FWConstants.result_success);
					}
					
				} else {
					jsonResult.put(result, FWConstants.result_error);
					jsonResult.put("errCode", FWConstants.result_error);
					jsonResult.put("errMsg", FWConstants.result_error_msg);
				}	
				
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
				jsonResult.put("errCode", FWConstants.result_usernull);
				jsonResult.put("errMsg", FWConstants.result_usernull_msg);
			}
			return JSonUtils.toJSon(jsonResult);
			
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			jsonResult.put("errCode", FWConstants.result_error);
			jsonResult.put("errMsg", FWConstants.result_error_msg);
			return JSonUtils.toJSon(jsonResult);
		}
	}
	
	/**
	 * 根据id,删除捐赠信息
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@Valid Integer id, @Valid Integer uid)throws Exception{
		
		try {
			jsonResult = new HashMap<>();
			
			//判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(uid)).getId())){
				
				//判断id是否为空
				if(!StringUtilC.isEmpty(id)){
				
					//删除
					int ifSucess = apiFamousService.deleteApiFamousById(id);
					
					// 如果成功
					if (ifSucess>= 0) {
						jsonResult.put(result, FWConstants.result_success);
					}
					
				} else {
					jsonResult.put(result, FWConstants.result_error);
					jsonResult.put("errCode", FWConstants.result_error);
					jsonResult.put("errMsg", FWConstants.result_error_msg);
				}
				
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
				jsonResult.put("errCode", FWConstants.result_usernull);
				jsonResult.put("errMsg", FWConstants.result_usernull_msg);
			}
			return JSonUtils.toJSon(jsonResult);
			
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			jsonResult.put("errCode", FWConstants.result_error);
			jsonResult.put("errMsg", FWConstants.result_error_msg);
			return JSonUtils.toJSon(jsonResult);
		}
	}

}

