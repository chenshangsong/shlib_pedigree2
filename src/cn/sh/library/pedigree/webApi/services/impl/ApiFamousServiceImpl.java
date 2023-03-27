package cn.sh.library.pedigree.webApi.services.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.mapper.ApiFamousMapper;
import cn.sh.library.pedigree.sysManager.model.ApiFamousDto;
import cn.sh.library.pedigree.webApi.services.ApiFamousService;

/**
 * @author 陈铭
 */

@Service
public class ApiFamousServiceImpl implements ApiFamousService {
	
	@Resource
	private ApiFamousMapper apiFamousMapper;

	@Override
	public ArrayList<ApiFamousDto> getApiFamousListPage(ApiFamousDto dto) {
		
		return apiFamousMapper.getApiFamousListPage(dto);
	}

	@Override
	public ApiFamousDto getApiFamousById(Integer id) {
		
		return apiFamousMapper.getApiFamousById(id);
	}

	@Override
	public Integer insertApiFamous(ApiFamousDto dto) {
		
		return apiFamousMapper.insertApiFamous(dto);
	}

	@Override
	public Integer updateApiFamous(ApiFamousDto dto) {
		
		return apiFamousMapper.updateApiFamous(dto);
	}

	@Override
	public Integer deleteApiFamousById(Integer id) {
		
		return apiFamousMapper.deleteApiFamousById(id);
	}

}
