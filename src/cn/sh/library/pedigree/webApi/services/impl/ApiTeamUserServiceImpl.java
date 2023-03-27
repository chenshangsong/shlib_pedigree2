package cn.sh.library.pedigree.webApi.services.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.mapper.ApiTeamUserMapper;
import cn.sh.library.pedigree.sysManager.model.ApiTeamUserDto;
import cn.sh.library.pedigree.sysManager.model.SearchTaskAndTeamUserDto;
import cn.sh.library.pedigree.webApi.services.ApiTeamUserService;

/**
 * @author 陈铭
 */

@Service
public class ApiTeamUserServiceImpl implements ApiTeamUserService {
	
	@Resource
	private ApiTeamUserMapper apiTeamUserMapper;

	@Override
	public Integer insertApiTeamUser(ApiTeamUserDto dto) {

		return apiTeamUserMapper.insertApiTeamUser(dto);
	}

	@Override
	public Integer updateCoopStatusByTeamUser(ApiTeamUserDto dto) {

		return apiTeamUserMapper.updateCoopStatusByTeamUser(dto);
	}
	
	@Override
	public ArrayList<ApiTeamUserDto> getApiTeamUserListByTaskId(Integer taskId,String coopStatus) {

		return apiTeamUserMapper.getApiTeamUserListByTaskId(taskId,coopStatus);
	}
	
	@Override
	public ArrayList<ApiTeamUserDto> getApiTeamUserListPage(SearchTaskAndTeamUserDto dto) {

		return apiTeamUserMapper.getApiTeamUserListPage(dto);
	}

	@Override
	public Integer deleteApiTeamUser(Integer taskId, Integer coopId) {

		return apiTeamUserMapper.deleteApiTeamUser(taskId, coopId);
	}

	@Override
	public ArrayList<ApiTeamUserDto> getCoopStatusListByTaskId(SearchTaskAndTeamUserDto dto) {

		return apiTeamUserMapper.getCoopStatusListByTaskId(dto);
	}

}
