package cn.sh.library.pedigree.webApi.services;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.ApiWorkFavoriteDto;

/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
public interface ApiWorkFavoriteService extends BaseService {

	/**
	 * 根据用户ID，获取收藏列表
	 */
	public List<ApiWorkFavoriteDto> getApiWorkFavoriteListPage(ApiWorkFavoriteDto dto);
	
	/**
	 * 根据用户ID，workUri,获取收藏信息
	 * @param id
	 * @return
	 */
	public ApiWorkFavoriteDto  getApiWorkFavoriteByWorkUri(@Param(value = "userId") Integer userId,@Param(value = "workUri") String workUri);
	
	/**
	 * 根ID获取收藏信息
	 * @param model
	 * @return
	 */
	public ApiWorkFavoriteDto getApiWorkFavoriteById(@Param(value = "id") Integer id);
	
	/**
	 * 添加收藏信息 
	 * @param model
	 * @return
	 */
	public Integer insertApiWorkFavorite(ApiWorkFavoriteDto model);
	
	
	/**
	 * 根据ID 删除收藏信息 
	 * @param id
	 * @return
	 */
	public Integer deleteApiWorkFavoriteById(@Param(value = "id") Integer id,@Param(value = "userId") Integer userId);
	
	/**
	 * 根据用户ID ,workId 删除收藏信息
	 * @param model
	 * @return
	 */
	public Integer deleteApiWorkFavoriteByWorkUri(@Param(value = "userId") Integer userId,@Param(value = "workUri") String workUri);
	
}
