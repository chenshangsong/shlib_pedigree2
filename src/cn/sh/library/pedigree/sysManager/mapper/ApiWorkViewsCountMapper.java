package cn.sh.library.pedigree.sysManager.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ApiWorkViewsCountDto;

/**
 * 
 * @author 陈铭
 *
 */
@Repository
public interface ApiWorkViewsCountMapper {
	
	//查询
	ApiWorkViewsCountDto getInfoByWorkUri(@Param(value = "workUri") String workUri);
	
	//新增
	Integer insertViewsCount(@Param(value = "workUri") String workUri);
	
	//更新
	Integer updateViewsCount(@Param(value = "workUri") String workUri);

}
