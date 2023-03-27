package cn.sh.library.pedigree.sysManager.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ApiTaskDto;
import cn.sh.library.pedigree.sysManager.model.SearchTaskAndTeamUserDto;
/**
 * @author 陈铭
 */
@Repository
public interface ApiTaskMapper {

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
	 * 修改协同情况
	 */
	Integer updateApiTaskCooperation(ApiTaskDto dto);
	
	/**
	 * 查询我参加的任务
	 */
	ArrayList<ApiTaskDto> getMyAttendTaskListPage(SearchTaskAndTeamUserDto dto);
	
}
