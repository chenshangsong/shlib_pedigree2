package cn.sh.library.pedigree.framework.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.model.DtoLabelValue;
import cn.sh.library.pedigree.framework.model.DtoMessage;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.ControllerUtil;
import cn.sh.library.pedigree.framework.util.JsonUtil;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

/**
 * 类名 : ModelAndViewExt <br>
 * 机能概要 : ModelAndViewExt扩展</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class ModelAndViewExt extends ModelAndView {
    private BaseController baseController;
    /**
     * 增加空的列表，主要适用于下拉列表第一选项为空白
     * @param listName 画面使用的名称
     */
	public void addEmptyList(String listName){
		ArrayList<DtoLabelValue> list = new ArrayList<DtoLabelValue>();
		list.add(new DtoLabelValue("               ",""));
		this.addObject(listName, list);
	}
	
	/**
	 * 设置Action Forward跳转
	 * @param forward
	 */
	public void setForward(String forward){
		this.setViewName("forward:" + forward + FWConstant.ACTION_SUFFIX);
	}
	
	/**
	 * 设置Action redirect跳转
	 * @param redirect
	 */
	public void setRedirect(String redirect){
		this.setViewName("redirect:" + redirect + FWConstant.ACTION_SUFFIX);
	}
	
	/**
	 * 设置Action redirect跳转
	 * @param redirect
	 */
	public void setRedirect(String redirect, Map<String, Object> map){
		this.getModelMap().addAllAttributes(map);
		this.setViewName("redirect:" + redirect + FWConstant.ACTION_SUFFIX);
	}
	
	/**
	 * 设置前画面Action
	 * @param preAction 前画面Action
	 */
	public void addPreAction(String preAction){
		this.addObject(FWConstant.PREV_ACTION, preAction);
	} 
	
	/**
	 * 设置画面机能名称
	 * @param funcName 画面机能名称
	 */
	public void addFuncName(String funcName){
		this.addObject(FWConstant.FUNC_NAME, funcName);
	} 
	
	/**
	 * 设置画面机能名称
	 * @param funcName 画面机能名称
	 */
	public void addSubFunction(){
		String fullpath = ControllerUtil.getRequestPath(baseController);
		UserInfoModel user = baseController.getUser();
		if(user != null){
			//List<DtoRoleFunction>  list =null;// PreloadUserBean.getInstance().getSubFunction(fullpath, user.getStore_id(), user.getUserId());
			List<String> list =null;
			if(list == null || list.size() == 0){
				this.addObject(FWConstant.SUB_FUNCTION, "[]");
			}else{
				this.addObject(FWConstant.SUB_FUNCTION, JsonUtil.getJsonString(list));
			}
		}

			
	} 
 

	public void setReturnButtonAction(String action){
		this.addObject("ReturnButtonAction", action+".do");
	}
 
	
	/**
	 *  设置画面错误代码
	 * @param code
	 */
	public void addErrMsg(String code){
		this.addObject(FWConstant.ERRORMSG, CodeMsgUtil.getMessage(code));
	} 

	/**
	 * 设置画面错误信息列表
	 * @param list
	 */
	public void addErrMsgList(ArrayList<DtoMessage> list){
		this.addObject(FWConstant.ERRORMSGLIST, list);
	}  

	/**
	 *  设置画面ViewName
	 */
	public void setViewName(String viewName) {
		super.setViewName(viewName);
	}
	
	
	/**
	 * 获取BaseController
	 * @return CommonUtils
	 */
	public BaseController getBaseController() {
		return baseController;
	}

	/**
	 * 设置BaseController
	 * @param bctrl CommonUtils
	 */
	public void setBaseController(BaseController baseController) {
		this.baseController = baseController;
	} 
	
}
