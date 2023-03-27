package  cn.sh.library.pedigree.framework.handler;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import cn.sh.library.pedigree.framework.util.FileUtil;

/**
 * 类名 : ContextLoaderHandler <br>
 * 机能概要 : </br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class ContextLoaderHandler extends ContextLoaderListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextDestroyed(event);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextInitialized(event);
		FileUtil.WEB_INF_PATH = event.getServletContext().getRealPath("/WEB-INF/");
		
	}

}
