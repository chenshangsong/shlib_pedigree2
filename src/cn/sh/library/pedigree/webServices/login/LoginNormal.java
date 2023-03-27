package cn.sh.library.pedigree.webServices.login;

import org.dom4j.Document;
import org.dom4j.Element;

import cn.sh.library.pedigree.utils.XMLUtils;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

/**
 * 普通用户登录
 * 
 * @author chenss
 *
 */
public class LoginNormal {

	/**
	 * 普通用户登录
	 * 
	 * @param _model
	 * @return
	 */
	public static UserLoginResultModel login(UserLoginModel _model) {
		try {
			String _resultXml = UserLoginService.UserLogin_V2(_model);
			Document documentStr = XMLUtils.StringToXML(_resultXml);
			// 获取根节点元素对象
			Element node = documentStr.getRootElement();
			// 遍历所有的元素节点
			return XMLUtils.getUserLoginResult(node);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserLoginResultModel();
	}
}
