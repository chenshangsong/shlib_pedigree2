package cn.sh.library.pedigree.webApi.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.mapper.ApiChaodaiMapper;
import cn.sh.library.pedigree.sysManager.model.ApiChaodaiDto;
import cn.sh.library.pedigree.webApi.services.ApiChaodaiService;

/**
 * @author 陈铭
 */

@Service
public class ApiChaodaiServiceImpl implements ApiChaodaiService {
	
	@Resource
	private ApiChaodaiMapper apiChaodaiMapper;

	@Override
	public List<ApiChaodaiDto> getChaodaiList() {
		
		return apiChaodaiMapper.getChaodaiList();
	}


}
