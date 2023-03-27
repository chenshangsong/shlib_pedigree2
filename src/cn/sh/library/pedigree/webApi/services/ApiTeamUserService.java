package cn.sh.library.pedigree.webApi.services;

import java.util.ArrayList;

import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.ApiTeamUserDto;
import cn.sh.library.pedigree.sysManager.model.SearchTaskAndTeamUserDto;

/**
 * @author 陈铭
 */
public interface ApiTeamUserService extends BaseService {

	/**
	 * 添加协同者
	 */
	Integer insertApiTeamUser(ApiTeamUserDto dto);
	
	/**
	 * 更新协同状态（coopStatus）
	 */
	Integer updateCoopStatusByTeamUser(ApiTeamUserDto dto);
	
	/**
	 * 查询协同者（ 根据taskId [+coopStatus] ）
	 */
	ArrayList<ApiTeamUserDto> getApiTeamUserListByTaskId(Integer taskId,String coopStatus);
	
	/**
	 * 查询协同者（根据不同查询条件）
	 */
	ArrayList<ApiTeamUserDto> getApiTeamUserListPage(SearchTaskAndTeamUserDto dto);
	
	/**
	 * 删除协同者（根据taskId,coopId）
	 */
	Integer deleteApiTeamUser(Integer taskId, Integer coopId);
	
	/**
	 * 查询 coopStatus-协同状态（根据taskId-任务id, 1个任务可能有多个协同者）
	 */
	ArrayList<ApiTeamUserDto> getCoopStatusListByTaskId(SearchTaskAndTeamUserDto dto);
	
}

