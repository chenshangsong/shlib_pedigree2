package cn.sh.library.pedigree.webApi.services.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.sysManager.mapper.ApiTaskMapper;
import cn.sh.library.pedigree.sysManager.model.ApiTaskDto;
import cn.sh.library.pedigree.sysManager.model.SearchTaskAndTeamUserDto;
import cn.sh.library.pedigree.webApi.services.ApiTaskService;

/**
 * @author 陈铭
 */

@Service
public class ApiTaskServiceImpl implements ApiTaskService {
	
	@Resource
	private ApiTaskMapper apiTaskMapper;

	@Override
	public Integer insertApiTask(ApiTaskDto dto) {

		return apiTaskMapper.insertApiTask(dto);
	}

	@Override
	public ArrayList<ApiTaskDto> getApiTaskListPage(SearchTaskAndTeamUserDto dto) {

		return apiTaskMapper.getApiTaskListPage(dto);
	}
	
	@Override
	public ApiTaskDto getApiTaskById(Integer id) {

		return apiTaskMapper.getApiTaskById(id);
	}

	@Override
	public Integer updateApiTaskStatusByAdmin(ApiTaskDto dto) {

		return apiTaskMapper.updateApiTaskStatusByAdmin(dto);
	}

	@Override
	public Integer updateApiTaskIsOpenByAdmin(ApiTaskDto dto) {

		return apiTaskMapper.updateApiTaskIsOpenByAdmin(dto);
	}

	@Override
	public Integer updateApiTaskLeadByExpert(ApiTaskDto dto) {

		return apiTaskMapper.updateApiTaskLeadByExpert(dto);
	}

	@Override
	public Integer updateApiTaskCooperation(ApiTaskDto dto) {

		return apiTaskMapper.updateApiTaskCooperation(dto);
	}

	@Override
	public ArrayList<ApiTaskDto> getMyAttendTaskListPage(SearchTaskAndTeamUserDto dto) {

		return apiTaskMapper.getMyAttendTaskListPage(dto);
	}

}
