package cn.sh.library.pedigree.webApi.services;

import org.apache.ibatis.annotations.Param;

import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.ApiWorkViewsCountDto;
/**
 * 
 * @author 陈铭
 *  service
 */
public interface ApiWorkViewsCountService extends BaseService{

	//查询
	public ApiWorkViewsCountDto getInfoByWorkUri(@Param(value = "workUri") String workUri);
	
}
