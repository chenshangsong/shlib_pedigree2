package cn.sh.library.pedigree.webApi.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.sysManager.mapper.ApiWorkFavoriterMapper;
import cn.sh.library.pedigree.sysManager.model.ApiWorkFavoriteDto;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiWorkFavoriteService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * 
 * @author think
 *
 */
@Service
public class ApiWorkFavoriteServiceImpl extends BaseServiceImpl implements ApiWorkFavoriteService {

	@Autowired
	private ApiWorkFavoriterMapper apiWorkFavoriterMapper;

	@Autowired
	private RedisUtils redisUtil;

	@Override
	public List<ApiWorkFavoriteDto> getApiWorkFavoriteListPage(ApiWorkFavoriteDto dto) {
		try {
			return apiWorkFavoriterMapper.getApiWorkFavoriteListPage(dto);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public ApiWorkFavoriteDto getApiWorkFavoriteByWorkUri(Integer userId, String workUri) {
		ApiWorkFavoriteDto dto = new ApiWorkFavoriteDto();
		String redisFavoriteKey = RedisUtils.key_work_fav.concat(String.valueOf(userId)).concat("_").concat(workUri);
		if (redisUtil.exists(redisFavoriteKey)) {
			//取
	        Object obj = RedisUtils.unserizlize( (byte[]) redisUtil.get(redisFavoriteKey));
	        dto = (ApiWorkFavoriteDto) obj;
		} else {
			dto = apiWorkFavoriterMapper.getApiWorkFavoriteByWorkUri(userId, workUri);
			//存 字节
	        redisUtil.set(redisFavoriteKey, RedisUtils.serialize(dto));
		}
		return dto;
	}

	@Override
	public ApiWorkFavoriteDto getApiWorkFavoriteById(Integer id) {

		return apiWorkFavoriterMapper.getApiWorkFavoriteById(id);
	}

	@Override
	public Integer insertApiWorkFavorite(ApiWorkFavoriteDto model) {
		 String redisFavoriteKey = RedisUtils.key_work_fav.concat(String.valueOf(model.getUserId())).concat("_").concat(model.getWorkUri());
		 apiWorkFavoriterMapper.insertApiWorkFavorite(model);
			if (!redisUtil.exists(redisFavoriteKey)) {
		//存 字节  redis chenss20230322
	    redisUtil.set(redisFavoriteKey, RedisUtils.serialize(model));
			}
		 return model.getId();
	}

	@Override
	public Integer deleteApiWorkFavoriteById(Integer id) {
		return apiWorkFavoriterMapper.deleteApiWorkFavoriteById(id);
	}

	@Override
	public Integer deleteApiWorkFavoriteByWorkUri(Integer userId, String workUri) {
		String redisFavoriteKey = "favorite_" + userId + workUri;
		if (redisUtil.exists(redisFavoriteKey)) {
		  redisUtil.remove(redisFavoriteKey);//取消收藏 redis chenss20230322
		}
		return apiWorkFavoriterMapper.deleteApiWorkFavoriteByWorkUri(userId, workUri);
	}

}
