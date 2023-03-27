package cn.sh.library.pedigree.sysManager.mapper;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.DateChangeLogModel;

@Repository
public interface DataChangeLogMapper {

	void updateJpLog(DateChangeLogModel dataChangeLogModel);

}
