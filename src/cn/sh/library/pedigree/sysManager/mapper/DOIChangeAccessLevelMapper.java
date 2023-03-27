package cn.sh.library.pedigree.sysManager.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 陈铭
 */
@Repository
public interface DOIChangeAccessLevelMapper {

	/**
	 * 添加数据至关系库
	 */
	Integer insertDOIChangeInfo(@Param(value = "itemUri") String itemUri,
			@Param(value = "doiSpread") String doiSpread,
			@Param(value = "doiOrigin") String doiOrigin,
			@Param(value = "oldStatus") String oldStatus,
			@Param(value = "newStatus") String newStatus,
			@Param(value = "flg") String flg);
	
	/**
	 * 更新hasFullImg属性
	 */
	Integer updateHasFullImg(@Param(value = "doiSpread") String doiSpread, @Param(value = "hasFullImg") String hasFullImg);
}
