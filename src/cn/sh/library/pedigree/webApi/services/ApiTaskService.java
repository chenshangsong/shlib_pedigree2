package cn.sh.library.pedigree.webApi.services;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.ApiTaskDto;
import cn.sh.library.pedigree.sysManager.model.SearchTaskAndTeamUserDto;

/**
 * @author 陈铭
 */
public interface ApiTaskService extends BaseService {

	/**
	 * 添加任务
	 */
	Integer insertApiTask(ApiTaskDto dto);
	
	/**
	 * 查询任务(根据不同检索条件)
	 */
	ArrayList<ApiTaskDto> getApiTaskListPage(SearchTaskAndTeamUserDto dto);
	
	/**
	 * 查询单个任务(根据id)
	 */
	ApiTaskDto getApiTaskById(@Param(value = "id") Integer id);
	
	/**
	 * 更新任务-任务状态
	 */
	Integer updateApiTaskStatusByAdmin(ApiTaskDto dto);
	
	/**
	 * 更新任务-开放状态
	 */
	Integer updateApiTaskIsOpenByAdmin(ApiTaskDto dto);
	
	/**
	 * 专家认领任务
	 */
	Integer updateApiTaskLeadByExpert(ApiTaskDto dto);
	
	/**
	 * 修改协同状态
	 */
	Integer updateApiTaskCooperation(ApiTaskDto dto);
	
	/**
	 * 查询我参加的任务
	 */
	ArrayList<ApiTaskDto> getMyAttendTaskListPage(SearchTaskAndTeamUserDto dto);
}
