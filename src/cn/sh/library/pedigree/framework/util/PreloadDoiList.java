package cn.sh.library.pedigree.framework.util;

import java.util.List;

import javax.annotation.Resource;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.sysManager.mapper.HomeMapper;
import cn.sh.library.pedigree.sysManager.model.DoiSysModel;

/**
 * doiList预加载
 * @author 陈铭
 * 单例模式
 */
public class PreloadDoiList implements IfPreloadBean {
	@Resource
	private HomeMapper homeMapper;
	private List<DoiSysModel> doiList;
	private static PreloadDoiList instance;

	/**
	 * 实例化方法
	 */
	public static synchronized PreloadDoiList getInstance() {
		if (null == instance) {
			instance = new PreloadDoiList();
		}
		return instance;
	}

	/**
	 * 获取doiList列表
	 */
	@Override
	public void loadInfo() {
		//判断PreloadDoiList是否被实例化
		if (instance == null) {
			getInstance();
		}
		//判断homeMapper是否被实例化
		if (instance.homeMapper == null) {
			instance.homeMapper = homeMapper;
		}
		//获取doiList列表
		instance.doiList = instance.homeMapper.getDoisysList();
	}

	@Override
	public void reloadInfo() {
		loadInfo();
	}

	//getDoiList()方法
	public List<DoiSysModel> getDoiList() {
		if(instance==null){
			reloadInfo();
		}
		return instance.doiList;
	}
}
