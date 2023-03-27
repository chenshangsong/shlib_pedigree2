package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ShlPersonPlaceModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;

@Repository
public interface PlaceErrorMapper {

	/**
	 * 获得OtherListSDPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<ShlPersonPlaceModel> getErrorPlaceList(@Param(value = "flg") String flg);

	/**
	 * 获得OtherListSDPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<ShlPersonPlaceModel> updateVTPlaceList();
	/**
	 * 获得OtherListSDPlace
	 * 
	 * @param dpudc
	 * @return
	 */
	List<ShumuModel> getWorkErrorPlace();
	
	/**
	 * 更新SDPlaceTemp
	 * 
	 * @return
	 */
	int updatePlace(ShlPersonPlaceModel model);
}
