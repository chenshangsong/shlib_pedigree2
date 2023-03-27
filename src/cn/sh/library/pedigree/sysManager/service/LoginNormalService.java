package cn.sh.library.pedigree.sysManager.service;


import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

public interface LoginNormalService extends BaseService {
	
	/**
	 * 用户登录
	 * @param _model
	 * @return
	 */
	public  UserLoginResultModel login(UserLoginModel _model);
	

}
