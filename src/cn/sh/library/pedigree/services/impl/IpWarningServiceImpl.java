package cn.sh.library.pedigree.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.IpWarningService;
import cn.sh.library.pedigree.sysManager.mapper.IpWarningMapper;
import cn.sh.library.pedigree.sysManager.model.IpWarningModel;

@Service
public class IpWarningServiceImpl extends BaseServiceImpl implements
		IpWarningService {

	@Autowired
	private IpWarningMapper ipWarningMapper;

	@Override
	public void insertip(IpWarningModel ipwar) {
		ipWarningMapper.insertip(ipwar);

	}

}
