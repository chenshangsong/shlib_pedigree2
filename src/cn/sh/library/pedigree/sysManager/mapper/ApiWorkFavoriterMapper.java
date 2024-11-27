package cn.sh.library.pedigree.sysManager.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ApiWorkFavoriteDto;

@Repository
public interface ApiWorkFavoriterMapper {

	/**
	 * 根据用户ID，获取收藏列表
	 */
	ArrayList<ApiWorkFavoriteDto> getApiWorkFavoriteListPage(ApiWorkFavoriteDto dto);
	
	/**
	 * 根据用户ID，workUri,获取收藏信息
	 * @param id
	 * @return
	 */
	ApiWorkFavoriteDto  getApiWorkFavoriteByWorkUri(@Param(value = "userId") Integer userId,@Param(value = "workUri") String workUri);
	
	/**
	 * 根ID获取收藏信息
	 * @param model
	 * @return
	 */
	ApiWorkFavoriteDto getApiWorkFavoriteById(@Param(value = "id") Integer id);
	
	/**
	 * 添加收藏信息 
	 * @param model
	 * @return
	 */
	Integer insertApiWorkFavorite(ApiWorkFavoriteDto model);
	
	
	/**
	 * 根据ID 删除收藏信息 
	 * @param id
	 * @return
	 */
	Integer deleteApiWorkFavoriteById(@Param(value = "id") Integer id,@Param(value = "userId") Integer userId);
	
	/**
	 * 根据用户ID ,workId 删除收藏信息
	 * @param model
	 * @return
	 */
	Integer deleteApiWorkFavoriteByWorkUri(@Param(value = "userId") Integer userId,@Param(value = "workUri") String workUri);
	
}
