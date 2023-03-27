package cn.sh.library.pedigree.sysManager.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.WorkDelHistoryModel;

@Repository
public interface DataChangeHistoryMapper {

	ArrayList<WorkDelHistoryModel> selectWorkDelHistoryList();

	/***
	 * 
	 * 
	 * @param model
	 */
	Integer insertWorkDelHistory(WorkDelHistoryModel model);

	/***
	 * 
	 * 
	 * @param model
	 */
	Integer insertHistoryMain(DataChangeHistoryMainModel model);

	/***
	 * 
	 * 
	 * @param model
	 */
	Integer insertHistoryList(DataChangeHistoryListModel model);

	/**
	 * 
	 * @param id
	 * @param releaseUser
	 * @param releaseDate
	 * @return
	 */
	Integer updateHistoryMain(@Param(value = "id") Integer id,
			@Param(value = "releaseUser") String releaseUser,
			@Param(value = "releaseDate") String releaseDate);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteHistoryMain(@Param(value = "id") Integer id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteHistoryList(@Param(value = "mainId") Integer mainId);

	/**
	 * 
	 * @param dataUri
	 * @param dataType
	 * @return
	 */
	List<DataChangeHistoryMainModel> selectHistoryMain(
			@Param(value = "dataUri") String dataUri,
			@Param(value = "dataType") String dataType);

	/**
	 * 
	 * @param createUser
	 * @return
	 */
	ArrayList<DataChangeHistoryMainModel> selectLoginHistoryMainListPage(
			DataChangeHistoryMainModel model);

	/**
	 * 
	 * @param mainId
	 * @return
	 */
	List<DataChangeHistoryListModel> selectHistoryList(
			@Param(value = "mainId") String mainId);

	/**
	 * 
	 * @param Instance信息
	 * @return
	 */
	List<DataChangeHistoryListModel> selectInstanceHistoryList(
			@Param(value = "mainId") String mainId);

	/**
	 * 从历史表查询某dataUri数据是否已存在且未发布。
	 * 
	 * @param dataUri
	 * @param dataType
	 * @return
	 */
	int selectCountByDataUri(@Param(value = "dataUri") String dataUri,
			@Param(value = "dataType") String dataType);

	/**
	 * 根据ID查询主表信息
	 * 
	 * @param mainId
	 * @return
	 */
	DataChangeHistoryMainModel getHistoryMain(
			@Param(value = "mainId") String mainId,
			@Param(value = "pId") String pId);

	/**
	 * 获取需要更新的姓氏批量信息
	 * 
	 * @return
	 */
	List<DataChangeHistoryMainModel> getHistoryMainFamilyName();
}
