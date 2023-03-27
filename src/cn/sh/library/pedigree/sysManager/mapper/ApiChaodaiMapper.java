package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ApiChaodaiDto;

/**
 * @author 陈铭
 */
@Repository
public interface ApiChaodaiMapper {

	/**
	 * 获取朝代列表
	 * @param dto
	 * @return
	 */
	List<ApiChaodaiDto> getChaodaiList();
	
}
