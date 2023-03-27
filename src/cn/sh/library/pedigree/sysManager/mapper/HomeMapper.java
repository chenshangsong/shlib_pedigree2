package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.DoiSysModel;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;

@Repository
public interface HomeMapper {
	/**
	 * 得到需要开发DOIList
	 * 
	 * @param dpudc
	 * @return
	 */
	List<DoiSysModel> getDoisysList();
	/**
	 * 获取所有姓氏数据
	 * @return
	 */
	List<SurnameModel> getSurnameList(@Param(value = "initial") String initial);

	/**
	 * 获取单个姓氏数据
	 * @return
	 */
	SurnameModel getSurname(@Param(value = "familyNameS") String familyNameS);
}
