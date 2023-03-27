package cn.sh.library.pedigree.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.OrganizationService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * 堂号
 * 
 * @author chenss
 * @date 2015/1/5 0005
 */
@Service
public class OrganizationServiceImpl extends BaseServiceImpl implements OrganizationService {

	@Resource
	private BaseinfoSparql baseinfoSparql;

	@Override
	public List<Map<String, String>> list(String search, Pager pager) {
		QueryResult<Map<String, Object>> queryResult = this.baseinfoSparql.getOrganization(search,
				pager.getStartIndex(), pager.getPageSize());
		pager.calcPageCount(queryResult.getTotalrecord());
		return RDFUtils.transformListMap(queryResult.getResultList());
	}

	/**
	 * 编目系统使用
	 */
	@Override
	public List<Map<String, String>> listForBm(String search, Pager pager) {
		QueryResult<Map<String, Object>> queryResult = this.baseinfoSparql.getOrganizationForBM(search,
				pager.getStartIndex(), pager.getPageSize());
		pager.calcPageCount(queryResult.getTotalrecord());
		return RDFUtils.transformListMap(queryResult.getResultList());
	}

}
