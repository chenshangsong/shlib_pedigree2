package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.SDMailModel;
import cn.sh.library.pedigree.sysManager.model.SDPersonModel;
import cn.sh.library.pedigree.sysManager.model.SDPlaceTempModel;

@Repository
public interface SDMailMapper {
	
	/**
	 * 获得ListSDMail
	 * @return
	 */
	List<SDMailModel> getListSDMail();
	
	/**
	 * 获得SDPerson
	 * @return
	 */
	SDPersonModel getSDPerson(@Param(value = "originalWord") String originalWord);
	
	/**
	 * 获得SDPlaceTemp
	 * @return
	 */
	SDPlaceTempModel getSDPlaceTemp(@Param(value = "orginalPlace") String orginalPlace);
	
	/**
	 * 更新SDMail
	 * @param model
	 * @return
	 */
	int updateSDMail(SDMailModel model);

}
