package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.SDPlaceModel;
import cn.sh.library.pedigree.sysManager.model.SDPlaceTempModel;
import cn.sh.library.pedigree.sysManager.model.ShlPersonPlaceModel;

@Repository
public interface SDPlaceMapper {
	
	/**
	 * 获得OtherListSDPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<SDPlaceModel> getOtherListSDPlace(SDPlaceTempModel tempModel);
	
	/**
	 * 获得ChinaListSDPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<SDPlaceModel> getChinaListSDPlace(SDPlaceTempModel tempModel);

	/**
	 * 获得ListSDPlaceTemp
	 * 
	 * @param dpudc
	 * @return
	 */
	List<SDPlaceTempModel> getListSDPlaceTemp();
	
	/**
	 * 获得ListOtherShlPersonPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<ShlPersonPlaceModel> getOtherListShlPersonPlace(SDPlaceTempModel tempModel);

	/**
	 * 获得ListChinaShlPersonPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<ShlPersonPlaceModel> getChinaListShlPersonPlace(SDPlaceTempModel tempModel);

	/**
	 * 更新SDPlaceTemp
	 * @return
	 */
	int updateSDPlaceTemp(@Param(value = "ID") Long ID,
			@Param(value = "point") String point);

	/***
	 * 添加反馈标题列表
	 * 
	 * @param info
	 */
	int insertSDPlace(SDPlaceModel model);
}
