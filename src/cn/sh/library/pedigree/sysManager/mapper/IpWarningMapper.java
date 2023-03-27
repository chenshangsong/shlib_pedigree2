package cn.sh.library.pedigree.sysManager.mapper;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.IpWarningModel;

@Repository
public interface IpWarningMapper {

	void insertip(IpWarningModel ipWarningModel);

}
