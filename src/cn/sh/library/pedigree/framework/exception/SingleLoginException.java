package  cn.sh.library.pedigree.framework.exception;

import javax.servlet.http.HttpServletRequest;

/**
 * 类名 : SingleLoginException <br>
 * 机能概要 :单点登陆异常</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class SingleLoginException extends SysException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -298946734897106160L;
	
	public SingleLoginException(String code){
		super(code);
	}
	
	public SingleLoginException(HttpServletRequest request){
		super("RemoteAddr:"+request.getRemoteAddr()+ "    RemoteHost" +request.getRemoteHost()+ "    RemoteUser" +request.getRemoteUser());
	}
}
