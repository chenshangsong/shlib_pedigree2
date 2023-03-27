package cn.sh.library.pedigree.webApi.services.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.mapper.ApiGenealogyDonateMapper;
import cn.sh.library.pedigree.sysManager.model.ApiGenealogyDonateDto;
import cn.sh.library.pedigree.webApi.services.ApiGenealogyDonateService;

/**
 * @author 陈铭
 */

@Service
public class ApiGenealogyDonateServiceImpl implements ApiGenealogyDonateService {

	@Autowired
	private ApiGenealogyDonateMapper apiGenealogyDonateMapper;
	
	@Override
	public ArrayList<ApiGenealogyDonateDto> getApiGenealogyDonateListPage(ApiGenealogyDonateDto dto) {
		
		return apiGenealogyDonateMapper.getApiGenealogyDonateListPage(dto);
	}

	@Override
	public ApiGenealogyDonateDto getApiGenealogyDonateById(Integer id) {
		
		return apiGenealogyDonateMapper.getApiGenealogyDonateById(id);
	}

	@Override
	public Integer insertApiGenealogyDonate(ApiGenealogyDonateDto dto) {
		
		return apiGenealogyDonateMapper.insertApiGenealogyDonate(dto);
	}

	@Override
	public Integer updateApiGenealogyDonate(ApiGenealogyDonateDto dto) {
		
		return apiGenealogyDonateMapper.updateApiGenealogyDonate(dto);
	}

	@Override
	public Integer deleteApiGenealogyDonateById(Integer id) {
		
		return apiGenealogyDonateMapper.deleteApiGenealogyDonateById(id);
	}

}
