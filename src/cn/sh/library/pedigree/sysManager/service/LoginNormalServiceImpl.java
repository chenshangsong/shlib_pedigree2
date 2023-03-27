package cn.sh.library.pedigree.sysManager.service;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.utils.XMLUtils;
import cn.sh.library.pedigree.webServices.login.UserLoginService;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

@Service
public class LoginNormalServiceImpl extends BaseServiceImpl implements
		LoginNormalService {

	/**
	 * 普通用户登录
	 * 
	 * @param _model
	 * @return
	 */
	@Override
	public UserLoginResultModel login(UserLoginModel _model) {
		try {
			String _resultXml = UserLoginService.UserLogin_V2(_model);
			Document documentStr = XMLUtils.StringToXML(_resultXml);
			
			// 获取根节点元素对象
			Element node = documentStr.getRootElement();
			UserLoginResultModel _remodel = new UserLoginResultModel();
			_remodel = XMLUtils.getUserLoginResult(node);
			//设置范围参数
			XMLUtils.setParmResult(documentStr, _remodel);
			_remodel.setUid(_model.getUid());
			_remodel.setPwd(_model.getPwd());
			//用户姓名
			_remodel.setUserName(_remodel.getParmResult().getCn());
			_remodel.setUserType(_model.getUserType());
			// 遍历所有的元素节点
			return _remodel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("-登录失败:用户名或密码错-");
		}
		return new UserLoginResultModel();
	}
}
