package  cn.sh.library.pedigree.framework.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;


/**
 * 类名 : CodeMsgUtil <br>
 * 机能概要 : Code和properties操作工具</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class CodeMsgUtil{
	public final static ResourceBundle messages = ResourceBundle.getBundle("resources.messages");
	public final static ResourceBundle config = ResourceBundle.getBundle("resources.config");
	/**
	 * res/messages.properties文件读取
	 * 
	 * @param key 读取字段key
	 * @param param 读取字段参数
	 * @return
	 */
	public static String getMessage(String key, Object... param){
		try{
			String msg = messages.getString(key);
			return MessageFormat.format(msg, param);
		}catch(Exception e){
			return "";
		}
		
	}
	
	/**
	 * res/messages.properties文件读取
	 * 
	 * @param key 读取字段key
	 * @param param 读取字段参数
	 * @return
	 */
	public static String getConfig(String key, Object... param){
		try{
			String msg = config.getString(key);
			return MessageFormat.format(msg, param);
		}catch(Exception e){
			return "";
		}
		
	}
}
