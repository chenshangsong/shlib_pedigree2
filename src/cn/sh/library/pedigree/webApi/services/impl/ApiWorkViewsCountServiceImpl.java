package cn.sh.library.pedigree.webApi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.mapper.ApiWorkViewsCountMapper;
import cn.sh.library.pedigree.sysManager.model.ApiWorkViewsCountDto;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiWorkViewsCountService;
/**
 * 
 * @author	陈铭
 *  serviceImpl
 */
@Service
public class ApiWorkViewsCountServiceImpl implements ApiWorkViewsCountService {
	
	@Autowired
	private ApiWorkViewsCountMapper workViewsCountMapper;
	
	@Autowired
    private RedisUtils redisUtil; 

	//查询
	@Override
	public ApiWorkViewsCountDto getInfoByWorkUri(String workUri) {
		String redisKey = RedisUtils.key_work_view.concat(workUri);
		int tempviewCountNow = 0;
		ApiWorkViewsCountDto viewDto = new ApiWorkViewsCountDto();
		if(redisUtil.exists(redisKey)) {//如果redis缓存存在数据，则返回数据加一
			tempviewCountNow= StringUtilC.getInteger(redisUtil.get(redisKey))+1;
			workViewsCountMapper.updateViewsCount(workUri);//同时更新 
		}
		else {
			 viewDto = workViewsCountMapper.getInfoByWorkUri(workUri);
			
			if(viewDto==null || StringUtilC.isEmpty(viewDto.getViewCount())){
				workViewsCountMapper.insertViewsCount(workUri);
				tempviewCountNow=1;
			}else{
				tempviewCountNow=viewDto.getViewCount()+1;
				workViewsCountMapper.updateViewsCount(workUri);//同时更新 
			}
		}
		viewDto.setViewCount(tempviewCountNow);
		redisUtil.set(redisKey, tempviewCountNow);//更新 redis缓存中的浏览量
		
		return viewDto;
	}
}
