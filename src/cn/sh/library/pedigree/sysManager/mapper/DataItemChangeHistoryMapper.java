package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.DataItemChangeHistoryModel;

@Repository
public interface DataItemChangeHistoryMapper {

	/***
	 * 
	 * 
	 * @param model
	 */
	Integer insertItemHistory(DataItemChangeHistoryModel model);

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
	 * 更新ItemHistory状态
	 * 
	 * @param id
	 * @param deleteFlg
	 * @return
	 */
	Integer updateItemHistoryDeleteFlg(@Param(value = "id") Integer id,
			@Param(value = "deleteFlg") String deleteFlg);

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
	Integer deleteItemHistoryList(@Param(value = "mainId") Integer mainId);

	/**
	 * 
	 * @param mainId
	 * @return
	 */
	List<DataItemChangeHistoryModel> selectItemHistoryList(
			@Param(value = "mainId") String mainId);

}
