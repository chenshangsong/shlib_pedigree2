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
		dto = apiWorkFavoriterMapper.getApiWorkFavoriteByWorkUri(userId, workUri);
		return dto;
	}

	@Override
	public ApiWorkFavoriteDto getApiWorkFavoriteById(Integer id) {

		return apiWorkFavoriterMapper.getApiWorkFavoriteById(id);
	}

	@Override
	public Integer insertApiWorkFavorite(ApiWorkFavoriteDto model) {

		 apiWorkFavoriterMapper.insertApiWorkFavorite(model);
			
		 return model.getId();
	}

	@Override
	public Integer deleteApiWorkFavoriteById(Integer id,Integer userId) {
		return apiWorkFavoriterMapper.deleteApiWorkFavoriteById(id,userId);
	}

	@Override
	public Integer deleteApiWorkFavoriteByWorkUri(Integer userId, String workUri) {
		return apiWorkFavoriterMapper.deleteApiWorkFavoriteByWorkUri(userId, workUri);
	}

}
