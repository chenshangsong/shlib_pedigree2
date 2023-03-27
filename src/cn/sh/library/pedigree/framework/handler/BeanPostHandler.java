package  cn.sh.library.pedigree.framework.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;

/**
 * 类名 : BeanPostHandler <br>
 * 机能概要 : 如果我们需要在Spring容器完成Bean的实例化，配置和其他的初始化后添加一些自己的逻辑处理，我们就可以定义一个或者多个BeanPostProcessor接口的实现。</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class BeanPostHandler implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object obj, String arg1)
			throws BeansException {
		if (obj instanceof IfPreloadBean){
			((IfPreloadBean) obj).loadInfo();
		}
		return obj;
	}

	@Override
	public Object postProcessBeforeInitialization(Object obj, String arg1)
			throws BeansException {
		return obj;
	}

}
