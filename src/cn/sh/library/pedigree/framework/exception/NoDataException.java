package cn.sh.library.pedigree.framework.exception;

import cn.sh.library.pedigree.framework.model.DtoLabelValue;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;


/**
 * 类名 : NoDataException <br>
 * 机能概要 :无数据异常，用于画面迁移时当前画面数据已被删除的情况</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class NoDataException extends Exception {
	private DtoLabelValue errDto;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -298946734897106160L;
	
	public NoDataException(){
		super();
	}
	
	public NoDataException(String code){
		super();
		errDto = new DtoLabelValue(CodeMsgUtil.getMessage(code), code);
	}

	public DtoLabelValue getErrDto() {
		return errDto;
	}
}
