package cn.sh.library.pedigree.sysManager.service.impl;



import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.service.UserInfoService;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

@Service
public class  UserInfoServiceImpl implements UserInfoService {
	@Override
	public UserLoginResultModel login(UserLoginModel _model) {
		// TODO Auto-generated method stub
		return new UserLoginResultModel();
	}
	
	
	

}
