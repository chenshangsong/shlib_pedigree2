package cn.sh.library.pedigree.webApi.services;

import java.util.List;

import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.ApiChaodaiDto;

/**
 * @author 陈铭
 */
public interface ApiChaodaiService extends BaseService {

	/**
	 * 获取朝代列表
	 * @param dto
	 * @return
	 */
	List<ApiChaodaiDto> getChaodaiList();
	
}

