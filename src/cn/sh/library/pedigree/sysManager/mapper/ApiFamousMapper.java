package cn.sh.library.pedigree.sysManager.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ApiFamousDto;
/**
 * @author 陈铭
 */
@Repository
public interface ApiFamousMapper {

	/**
	 * 根据 用户id(uid)与 查询条件 , 获取捐赠信息-列表
	 */
	ArrayList<ApiFamousDto> getApiFamousListPage(ApiFamousDto dto);
	
	/**
	 * 根据id,查询捐赠信息
	 */
	ApiFamousDto getApiFamousById(@Param(value = "id") Integer id);
	
	/**
	 * 添加捐赠信息
	 */
	Integer insertApiFamous(ApiFamousDto dto);
	
	/**
	 * 根据id,更新捐赠信息
	 */
	Integer updateApiFamous(ApiFamousDto dto);
	

	/**
	 * 根据id,删除捐赠信息
	 */
	Integer deleteApiFamousById(@Param(value = "id") Integer id);
	
}
