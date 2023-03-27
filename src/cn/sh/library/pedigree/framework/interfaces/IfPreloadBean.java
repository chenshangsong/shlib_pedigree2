package  cn.sh.library.pedigree.framework.interfaces;

/**
 * 类名 : IfPreloadBean <br>
 * 机能概要 : web服务启动时进行一些主要信息加载的基类</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public interface IfPreloadBean {
	/**
	 * 加载信息
	 */
	void loadInfo();
	
	
	void reloadInfo();
}
