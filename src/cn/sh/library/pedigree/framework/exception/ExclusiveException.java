package cn.sh.library.pedigree.framework.exception;



/**
 * 类名 : ExclusiveException <br>
 * 机能概要 :排他异常</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class ExclusiveException extends SysException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -298946734897106160L;

	public ExclusiveException(String code){
		super(code);
	}
}
