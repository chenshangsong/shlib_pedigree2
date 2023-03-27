package cn.sh.library.pedigree.sysManager.service;



import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

public interface UserInfoService {
	
	/**
	 * 用户登录
	 * @param _model
	 * @return
	 */
	public  UserLoginResultModel login(UserLoginModel _model);
	
}
