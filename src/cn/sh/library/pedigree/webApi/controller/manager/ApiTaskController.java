package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.RoleGroup;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.framework.util.PreloadUserList;
import cn.sh.library.pedigree.sysManager.model.ApiTaskDto;
import cn.sh.library.pedigree.sysManager.model.ApiTeamUserDto;
import cn.sh.library.pedigree.sysManager.model.SearchTaskAndTeamUserDto;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiTaskService;
import cn.sh.library.pedigree.webApi.services.ApiTeamUserService;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;

/**
 * 任务中心Controller
 * 
 * @author 陈铭
 *
 */
@Controller
@RequestMapping("/webapi/manager/task")
public class ApiTaskController extends BaseController {

	@Resource
	private ApiTaskService apiTaskService;
	@Resource
	private ApiTeamUserService apiTeamUserService;
	@Resource
	private ApiWorkService apiWorkService;

	/**
	 * 管理员-批量添加任务
	 */
	@ResponseBody
	@RequestMapping(value = "/addBatchTasks", method = RequestMethod.POST)
	public String addBatchTasks(ApiTaskDto dto) throws Exception {

		try {

			String[] openWorks = {};

			jsonResult = new HashMap<>();
			int ifSuccess = 0;

			for (String workUri : openWorks) {
				// 获取详细信息
				Work work = apiWorkService.getWork(workUri, false);
				if (work != null && !StringUtilC.isEmpty(work.getUri())) {
					dto.setJpUri(workUri);
					dto.setCreator(work.getCreator());
					dto.setTangh(work.getTangh());
					dto.setTitle(work.getTitle());
					dto.setJpTitle(work.getDtitle());
					if (work.getInstances() != null
							&& work.getInstances().size() > 0) {
						dto.setPlace(work.getInstances().get(0).get("place"));
						dto.setEdition(work.getInstances().get(0)
								.get("edition"));
						dto.setEditionTemporal(work.getInstances().get(0)
								.get("temporal"));
					}
				}
				// 添加
				ifSuccess = apiTaskService.insertApiTask(dto);
			}
			// 如果成功
			if (ifSuccess > 0) {
				jsonResult.put(result, FWConstants.result_success);
			} else {
				jsonResult.put(result, FWConstants.result_error);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 管理员-单个添加任务
	 */
	@ResponseBody
	@RequestMapping(value = "/addTaskByAdmin", method = RequestMethod.POST)
	public String addTaskByAdmin(@Valid Integer uid, ApiTaskDto dto)
			throws Exception {

		try {
			jsonResult = new HashMap<>();
			// 获取登录用户(创建人用户-管理员)
			UserInfoModel loginUser = PreloadUserList.getUserById(StringUtilC
					.getString(uid));
			// 判断用户是否为空
			if (!StringUtilC.isEmpty(loginUser.getId())) {

				SearchTaskAndTeamUserDto searchTaskAndTeamUserDto = new SearchTaskAndTeamUserDto();
				// 通过jpUri获取对应任务(1条jpUri只对应1条任务)
				searchTaskAndTeamUserDto.setJpUri(dto.getJpUri());
				ArrayList<ApiTaskDto> list = apiTaskService
						.getApiTaskListPage(searchTaskAndTeamUserDto);
				// 判断数据库中的任务是否为空,如果存在
				if (list != null && list.size() > 0) {
					jsonResult.put(result, FWConstants.result_error);
				} else {// 如果不存在
					Work work = apiWorkService.getWork(dto.getJpUri(), false);
					if (work != null && !StringUtilC.isEmpty(work.getUri())) {
						// 赋值
						dto.setCreatorName(loginUser.getUserName());
						dto.setCreator(work.getCreator());
						dto.setTangh(work.getTangh());
						dto.setTitle(work.getTitle());
						dto.setJpTitle(work.getDtitle());
						if (work.getInstances() != null
								&& work.getInstances().size() > 0) {
							dto.setPlace(work.getInstances().get(0)
									.get("place"));
							dto.setEdition(work.getInstances().get(0)
									.get("edition"));
							dto.setEditionTemporal(work.getInstances().get(0)
									.get("temporal"));
						}
						dto.setCreatorId(uid);
						// 添加
						int ifSucess = apiTaskService.insertApiTask(dto);
						// 如果成功
						if (ifSucess > 0) {
							jsonResult.put(result, FWConstants.result_success);
						} else {
							jsonResult.put(result, FWConstants.result_error);
						}

					} else {
						jsonResult.put(result, FWConstants.result_error);
					}
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 管理员-更新任务-任务状态 更新字段：status; updateTime; updatorId;
	 */
	@ResponseBody
	@RequestMapping(value = "/updateApiTaskStatusByAdmin", method = RequestMethod.POST)
	public String updateApiTaskStatusByAdmin(@Valid Integer uid, ApiTaskDto dto)
			throws Exception {

		try {
			jsonResult = new HashMap<>();

			// 判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(
					StringUtilC.getString(uid)).getId())) {
				// 判断id是否为空
				if (dto.getIds() != null && dto.getIds().length > 0) {
					// 赋值
					dto.setUpdateTime(DateUtilC.getNowDateTime());
					dto.setUpdatorId(uid);
					// 更新
					int ifSucess = apiTaskService
							.updateApiTaskStatusByAdmin(dto);
					// 如果成功
					if (ifSucess > 0) {
						jsonResult.put(result, FWConstants.result_success);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 管理员-更新任务-开放状态 更新字段：isOpen; updateTime; updatorId;
	 */
	@ResponseBody
	@RequestMapping(value = "/updateApiTaskIsOpenByAdmin", method = RequestMethod.POST)
	public String updateApiTaskIsOpenByAdmin(@Valid Integer uid, ApiTaskDto dto)
			throws Exception {

		try {
			jsonResult = new HashMap<>();

			// 判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(
					StringUtilC.getString(uid)).getId())) {

				// 判断id是否为空
				if (dto.getIds() != null && dto.getIds().length > 0) {
					// 赋值
					dto.setUpdateTime(DateUtilC.getNowDateTime());
					dto.setUpdatorId(uid);
					// 更新
					int ifSucess = apiTaskService
							.updateApiTaskIsOpenByAdmin(dto);
					// 如果成功
					if (ifSucess > 0) {
						jsonResult.put(result, FWConstants.result_success);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 专家-认领任务 更新-部分字段 task-认领状态; leadId-认领人id; leadName-认领人姓名; leadCard-认领人卡号;
	 * leadDate-认领时间;
	 */
	@ResponseBody
	@RequestMapping(value = "/claimTaskByExpert", method = RequestMethod.POST)
	public String claimTaskByExpert(@Valid Integer uid, ApiTaskDto dto)
			throws Exception {

		try {
			jsonResult = new HashMap<>();

			// 获取登录用户(认领人用户)
			UserInfoModel loginUser = PreloadUserList.getUserById(StringUtilC
					.getString(uid));
			// 判断用户是否为空
			if (!StringUtilC.isEmpty(loginUser.getId())) {
				// 获取ids数组
				Integer[] ids = dto.getIds();
				List<Integer> idsCorrect = new ArrayList<Integer>();
				List<Integer> idsError = new ArrayList<Integer>();
				// 判断id是否为空
				if (ids != null && ids.length > 0) {
					// 循环获取数组中的id
					for (int i = 0; i < ids.length; i++) {
						// 判断任务的 协同状态(task),只有协同状态为0时，才能认领
						if (apiTaskService.getApiTaskById(ids[i]).getTask()
								.equals("0")) {
							idsCorrect.add(ids[i]);
						} else {
							idsError.add(ids[i]);
						}
					}
					if (idsCorrect.size() > 0) {
						// 赋值
						dto.setTask("1");
						dto.setIds((Integer[]) idsCorrect
								.toArray(new Integer[idsCorrect.size()]));
						dto.setLeadId(uid);
						dto.setLeadName(loginUser.getUserName());
						dto.setLeadCard(loginUser.getShLibAskNo());
						dto.setLeadDate(DateUtilC.getNowDateTime());
						// 更新
						int ifSucess = apiTaskService
								.updateApiTaskLeadByExpert(dto);
						// 如果成功
						if (ifSucess > 0) {
							jsonResult.put(result, FWConstants.result_success);
						} else {
							jsonResult.put(result, FWConstants.result_error);
						}
					}
					// 如果有不能更新的ID，则返回
					if (idsError.size() > 0) {
						jsonResult.put("errorIds", idsError);
						// 如果没进行更新操作，则返回1
						if (idsCorrect.size() == 0) {
							jsonResult.put(result, FWConstants.result_error);
						}
					}
				} else {
					jsonResult.put(result, FWConstants.result_error);
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 专家-添加协同者 传入3个参数 taskId(任务id) uid(认领人/登陆人的id) coopId(协同者id))
	 */
	@ResponseBody
	@RequestMapping(value = "/addTeamUserByExpert", method = RequestMethod.POST)
	public String addTeamUserByExpert(@Valid Integer uid, ApiTeamUserDto dto)
			throws Exception {

		try {
			jsonResult = new HashMap<>();

			// 获取登录用户(认领人用户)
			UserInfoModel loginUser = PreloadUserList.getUserById(StringUtilC
					.getString(uid));
			// 获取协同者用户
			UserInfoModel coopUser = PreloadUserList.getUserById(StringUtilC
					.getString(dto.getCoopId()));
			// 新建searchTaskAndTeamUserDto对象
			SearchTaskAndTeamUserDto searchTaskAndTeamUserDto = new SearchTaskAndTeamUserDto();
			// 赋值
			searchTaskAndTeamUserDto.setTaskId(dto.getTaskId());
			searchTaskAndTeamUserDto.setCoopId(dto.getCoopId());

			// 判断登陆用户是否为空
			if (!StringUtilC.isEmpty(loginUser.getId())) {
				ArrayList<ApiTeamUserDto> teamUserList = apiTeamUserService
						.getApiTeamUserListPage(searchTaskAndTeamUserDto);
				// 如果某任务ID下，不存在该协同者
				if (teamUserList == null || teamUserList.size() <= 0) {
					// 赋值
					dto.setLeadId(uid);
					dto.setLeadName(loginUser.getUserName());
					dto.setLeadCard(loginUser.getShLibAskNo());
					dto.setCoopName(coopUser.getUserName());
					dto.setCoopCard(coopUser.getShLibAskNo());
					// 添加
					int ifSucess = apiTeamUserService.insertApiTeamUser(dto);
					// 如果成功
					if (ifSucess > 0) {
						jsonResult.put(result, FWConstants.result_success);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

					// 获取该任务
					ApiTaskDto apiTaskDto = apiTaskService.getApiTaskById(dto
							.getTaskId());
					// 如果 该任务的 cooperation(协同情况) 为"0"未分配
					if (apiTaskDto.getCooperation().equals("0")) {
						// 将 该任务的 cooperation(协同情况) 修改为1(未协同)
						apiTaskDto.setCooperation("1");
						apiTaskService.updateApiTaskCooperation(apiTaskDto);
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 专家-根据taskId,coopId 删除协同者
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteTeamUserByExpert", method = RequestMethod.POST)
	public String deleteTeamUserByExpert(@Valid Integer uid, ApiTeamUserDto dto)
			throws Exception {

		try {
			jsonResult = new HashMap<>();

			// 判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(
					StringUtilC.getString(uid)).getId())) {
				// 判断 taskId,coopId 是否为空
				if (!StringUtilC.isEmpty(dto.getTaskId())
						&& !StringUtilC.isEmpty(dto.getCoopId())) {
					// 删除
					int ifSucess = apiTeamUserService.deleteApiTeamUser(
							dto.getTaskId(), dto.getCoopId());
					// 如果成功
					if (ifSucess > 0) {
						jsonResult.put(result, FWConstants.result_success);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

					// 查询 该任务下 是否还存在其他协同者
					SearchTaskAndTeamUserDto searchTaskAndTeamUserDto = new SearchTaskAndTeamUserDto();
					searchTaskAndTeamUserDto.setTaskId(dto.getTaskId());
					ArrayList<ApiTeamUserDto> teamUserList = apiTeamUserService.getApiTeamUserListPage(searchTaskAndTeamUserDto);
					// 获取该任务
					ApiTaskDto apiTaskDto = apiTaskService.getApiTaskById(dto.getTaskId());
					// 如果该任务下，不存在其他协同者
					if (teamUserList == null || teamUserList.size() <= 0) {
						// 将 该任务的 cooperation(协同情况) 修改为0(未分配)
						apiTaskDto.setCooperation("0");
						apiTaskService.updateApiTaskCooperation(apiTaskDto);
					// 若存在其他协同者
					} else {
						// 查询 该任务的 coopStatus(协同状态)
						ArrayList<ApiTeamUserDto> coopStatusList = apiTeamUserService.getCoopStatusListByTaskId(searchTaskAndTeamUserDto);
						//如果 coopStatusList 列表只有1条数据，且coopStatus为1，说明所有协同者已全部协同该任务
						if (coopStatusList.size() == 1 && coopStatusList.get(0).getCoopStatus().equals("1")) {
							// 将 该任务的 cooperation 赋值为3(全部协同)
							apiTaskDto.setCooperation("3");
							apiTaskService.updateApiTaskCooperation(apiTaskDto);
						//如果 coopStatusList 列表只有1条数据，且coopStatus为0，说明没有协同者协同该任务
						} else if (coopStatusList.size() == 1 && coopStatusList.get(0).getCoopStatus().equals("0")) {
							// 将 该任务的 cooperation 赋值为1(未协同)
							apiTaskDto.setCooperation("1");
							apiTaskService.updateApiTaskCooperation(apiTaskDto);
						// 有协同的协同者，也有未协同的协同者
						} else {
							// 将 该任务的 cooperation 赋值为2(部分协同)
							apiTaskDto.setCooperation("2");
							apiTaskService.updateApiTaskCooperation(apiTaskDto);
						}
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 查询协同者(只允许 "管理员" + "专家" 查询的方法)
	 */
	@ResponseBody
	@RequestMapping(value = "/getTeamUserList", method = RequestMethod.GET)
	public String getTeamUserList(@Valid Integer uid,
			SearchTaskAndTeamUserDto search, Pager pager) {

		jsonResult = new HashMap<>();

		// 判断用户是否为空
		if (!StringUtilC.isEmpty(PreloadUserList.getUserById(
				StringUtilC.getString(uid)).getId())) {

			DtoJsonPageData grid = new DtoJsonPageData(this);
			search.setPage(pager.getPageth());// 当前yem
			search.setRows(pager.getPageSize());
			// 查询
			ArrayList<ApiTeamUserDto> list = apiTeamUserService
					.getApiTeamUserListPage(search);
			// 设定输出对象
			grid.print2JsonObj(search, list);
			pager.calcPageCount(StringUtilC.getLong(String.valueOf(search
					.getRecords())));

			jsonResult.put("pager", pager);
			jsonResult.put("datas", grid.getRoot());
			jsonResult.put(result, FWConstants.result_success);

		} else {
			jsonResult.put(result, FWConstants.result_usernull);
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 查询-所有任务(根据不同检索条件) 只允许 "管理员" + "专家" 查询的方法 isOpen字段：传入1 为查询 开放的; 传入0
	 * 为查询不开放的; 不传值(null) 为全部查询(既查询开放的，也查询不开放的) 如果是"专家", isOpen赋值为1, 只查询开放状态的任务
	 */
	@ResponseBody
	@RequestMapping(value = "/getTaskList", method = RequestMethod.GET)
	public String getTaskList(@Valid Integer uid,
			SearchTaskAndTeamUserDto search, Pager pager) {

		jsonResult = new HashMap<>();
		// 获取登录用户
		UserInfoModel user = PreloadUserList.getUserById(StringUtilC
				.getString(uid));
		// 判断登录用户是否为空
		if (!StringUtilC.isEmpty(user.getId())) {
			// 判断角色权限, 如果是"专家", 则查询：已开放未认领的任务 + 并且是我（专家）已认领的任务 数据并集
			// 以及  我（专家）自己协同的任务 数据并集 【add by CM 2019-10-23】
			if (RoleGroup.zj.getGroup().equals(user.getRoleId())) {
				search.setSearchFlg("1"); // leadId=#{uid} or (isOpen = '1' and task = '0')
				search.setUid(uid);
				// 查询专家自己协同的任务 【add by CM 2019-10-23】
				search.setSearchZjCoopTaskFlg("1");
			}
			DtoJsonPageData grid = new DtoJsonPageData(this);
			search.setPage(pager.getPageth());// 当前yem
			search.setRows(pager.getPageSize());

			// 查询
			ArrayList<ApiTaskDto> list = apiTaskService
					.getApiTaskListPage(search);
			// 循环任务列表
			for (ApiTaskDto apiTaskDto : list) {
				// 获得某个任务的id
				Integer taskId = apiTaskDto.getId();
				// 通过taskId,查询teamUserList协同者列表:【需要查询全部协同者，注释bychenss 2018-05-18】
				/*ArrayList<ApiTeamUserDto> teamUserList = apiTeamUserService
						.getApiTeamUserListByTaskId(taskId, "1");*/
				ArrayList<ApiTeamUserDto> teamUserList = apiTeamUserService
						.getApiTeamUserListByTaskId(taskId, null);
				// 将teamUserList协同者列表,赋值给该任务
				apiTaskDto.setTeamUserList(teamUserList);
			}

			// 设定输出对象
			grid.print2JsonObj(search, list);
			pager.calcPageCount(StringUtilC.getLong(String.valueOf(search
					.getRecords())));
			jsonResult.put("pager", pager);
			jsonResult.put("datas", grid.getRoot());
			jsonResult.put(result, FWConstants.result_success);

		} else {
			jsonResult.put(result, FWConstants.result_usernull);
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 查询-用户自己参与的任务 传入uid(用户id),jpTitle(可选) 将uid赋值给coopId
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyAttendTaskList", method = RequestMethod.GET)
	public String getMyAttendTaskList(@Valid Integer uid,
			SearchTaskAndTeamUserDto search, Pager pager) {

		jsonResult = new HashMap<>();
		// 判断用户是否为空
		if (!StringUtilC.isEmpty(PreloadUserList.getUserById(
				StringUtilC.getString(uid)).getId())) {
			// 将uid赋值给coopId
			search.setCoopId(uid);
			search.setPage(pager.getPageth());// 当前yem
			search.setRows(pager.getPageSize());

			// 查询
			ArrayList<ApiTaskDto> list = apiTaskService
					.getMyAttendTaskListPage(search);
			// 循环任务列表
			for (ApiTaskDto apiTaskDto : list) {
				// 获得某个任务的id
				Integer taskId = apiTaskDto.getId();
				// 通过taskId,查询teamUserList协同者列表
				ArrayList<ApiTeamUserDto> teamUserList = apiTeamUserService
						.getApiTeamUserListByTaskId(taskId, null);
				// 将teamUserList协同者列表,赋值给该任务
				apiTaskDto.setTeamUserList(teamUserList);
			}

			// 设定输出对象
			DtoJsonPageData grid = new DtoJsonPageData(this);
			grid.print2JsonObj(search, list);
			pager.calcPageCount(StringUtilC.getLong(String.valueOf(search
					.getRecords())));

			jsonResult.put("pager", pager);
			jsonResult.put("datas", grid.getRoot());
			jsonResult.put(result, FWConstants.result_success);

		} else {
			jsonResult.put(result, FWConstants.result_usernull);
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 协同者中心模块-协同者-确认协同任务 传入参数：uid(用户id),taskId（任务id） 将uid赋值给coopId
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCoopStatusByTeamUser", method = RequestMethod.POST)
	public String updateCoopStatusByTeamUser(@Valid Integer uid,
			ApiTeamUserDto dto) throws Exception {

		try {
			jsonResult = new HashMap<>();
			// 获取登录用户(认领人用户)
			UserInfoModel loginUser = PreloadUserList.getUserById(StringUtilC
					.getString(uid));
			// 判断 uid(用户) 是否为空
			if (!StringUtilC.isEmpty(loginUser.getId())) {
				// 判断 taskId(任务id) 是否为空
				if (!StringUtilC.isEmpty(dto.getTaskId())) {
					// 赋值
					dto.setCoopId(uid);
					dto.setCoopStatus("1");
					dto.setCoopTime(DateUtilC.getNowDateTime());
					// 更新
					int ifSucess = apiTeamUserService
							.updateCoopStatusByTeamUser(dto);
					// 如果成功
					if (ifSucess > 0) {
						jsonResult.put(result, FWConstants.result_success);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

					// 通过 任务id 查询出该任务
					ApiTaskDto apiTaskDto = apiTaskService.getApiTaskById(dto
							.getTaskId());
					// 查询 该任务的 coopStatus(协同状态)
					SearchTaskAndTeamUserDto searchTaskAndTeamUserDto = new SearchTaskAndTeamUserDto();
					searchTaskAndTeamUserDto.setTaskId(dto.getTaskId());
					ArrayList<ApiTeamUserDto> coopStatusList = apiTeamUserService.getCoopStatusListByTaskId(searchTaskAndTeamUserDto);
					// 如果 coopStatusList 列表只有1条数据，且coopStatus为1，说明所有协同者已全部协同该任务
					if (coopStatusList.size() == 1 && coopStatusList.get(0).getCoopStatus().equals("1")) {
						// 将 该任务的 cooperation 赋值为3(全部协同)
						apiTaskDto.setCooperation("3");
						apiTaskService.updateApiTaskCooperation(apiTaskDto);
					} else {
						// 将 该任务的 cooperation 赋值为2(部分协同)
						apiTaskDto.setCooperation("2");
						apiTaskService.updateApiTaskCooperation(apiTaskDto);
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 协同者中心模块-协同者-取消协同任务 传入参数：uid(用户id),taskId（任务id） 将uid赋值给coopId
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCoopStatusByTeamUser", method = RequestMethod.POST)
	public String deleteCoopStatusByTeamUser(@Valid Integer uid,
			ApiTeamUserDto dto) throws Exception {

		try {
			jsonResult = new HashMap<>();

			// 判断用户是否为空
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(
					StringUtilC.getString(uid)).getId())) {
				// 判断 taskId 是否为空
				if (!StringUtilC.isEmpty(dto.getTaskId())) {
					// 赋值coopId
					dto.setCoopId(uid);
					// 删除
					int ifSucess = apiTeamUserService.deleteApiTeamUser(
							dto.getTaskId(), dto.getCoopId());
					// 如果成功
					if (ifSucess > 0) {
						jsonResult.put(result, FWConstants.result_success);
					} else {
						jsonResult.put(result, FWConstants.result_error);
					}

					// 查询 该任务下 是否还存在其他协同者
					SearchTaskAndTeamUserDto searchTaskAndTeamUserDto = new SearchTaskAndTeamUserDto();
					searchTaskAndTeamUserDto.setTaskId(dto.getTaskId());
					ArrayList<ApiTeamUserDto> teamUserList = apiTeamUserService.getApiTeamUserListPage(searchTaskAndTeamUserDto);
					// 获取该任务
					ApiTaskDto apiTaskDto = apiTaskService.getApiTaskById(dto.getTaskId());
					// 如果该任务下，不存在其他协同者
					if (teamUserList == null || teamUserList.size() <= 0) {
						// 将 该任务的 cooperation(协同情况) 修改为0(未分配)
						apiTaskDto.setCooperation("0");
						apiTaskService.updateApiTaskCooperation(apiTaskDto);
					// 若存在其他协同者
					} else {
						// 查询 该任务的 coopStatus(协同状态)
						ArrayList<ApiTeamUserDto> coopStatusList = apiTeamUserService.getCoopStatusListByTaskId(searchTaskAndTeamUserDto);
						//如果 coopStatusList 列表只有1条数据，且coopStatus为1，说明所有协同者已全部协同该任务
						if (coopStatusList.size() == 1 && coopStatusList.get(0).getCoopStatus().equals("1")) {
							// 将 该任务的 cooperation 赋值为3(全部协同)
							apiTaskDto.setCooperation("3");
							apiTaskService.updateApiTaskCooperation(apiTaskDto);
						//如果 coopStatusList 列表只有1条数据，且coopStatus为0，说明没有协同者协同该任务
						} else if (coopStatusList.size() == 1 && coopStatusList.get(0).getCoopStatus().equals("0")) {
							// 将 该任务的 cooperation 赋值为1(未协同)
							apiTaskDto.setCooperation("1");
							apiTaskService.updateApiTaskCooperation(apiTaskDto);
						// 有协同的协同者，也有未协同的协同者
						} else {
							// 将 该任务的 cooperation 赋值为2(部分协同)
							apiTaskDto.setCooperation("2");
							apiTaskService.updateApiTaskCooperation(apiTaskDto);
						}
					}

				} else {
					jsonResult.put(result, FWConstants.result_error);
				}

			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);

		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			return JSonUtils.toJSon(jsonResult);
		}
	}

}
