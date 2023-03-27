package  cn.sh.library.pedigree.framework.exception;

import cn.sh.library.pedigree.framework.model.DtoLabelValue;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;

/**
 * 类名 : SysException <br>
 * 机能概要 :系统异常</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class SysException extends Exception {
	private DtoLabelValue errDto;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -298946734897106160L;
	
	public SysException(String code){
		super();
		errDto = new DtoLabelValue(CodeMsgUtil.getMessage(code), code);
	}

	public SysException(String code, Object... param){
		super();
		errDto = new DtoLabelValue(CodeMsgUtil.getMessage(code, param), code);
	}

	public DtoLabelValue getErrDto() {
		return errDto;
	}
}
