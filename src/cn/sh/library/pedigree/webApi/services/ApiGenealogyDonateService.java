package cn.sh.library.pedigree.webApi.services;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.ApiGenealogyDonateDto;

/**
 * @author 陈铭
 */
public interface ApiGenealogyDonateService extends BaseService {

	/**
	 * 根据 用户id(uid)与 查询条件 , 获取捐赠信息-列表
	 */
	ArrayList<ApiGenealogyDonateDto> getApiGenealogyDonateListPage(ApiGenealogyDonateDto dto);
	
	/**
	 * 根据id,查询捐赠信息
	 */
	ApiGenealogyDonateDto getApiGenealogyDonateById(@Param(value = "id") Integer id);
	
	/**
	 * 添加捐赠信息
	 */
	Integer insertApiGenealogyDonate(ApiGenealogyDonateDto dto);
	
	/**
	 * 根据id,更新捐赠信息
	 */
	Integer updateApiGenealogyDonate(ApiGenealogyDonateDto dto);
	

	/**
	 * 根据id,删除捐赠信息
	 */
	Integer deleteApiGenealogyDonateById(@Param(value = "id") Integer id);
}

