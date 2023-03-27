package cn.sh.library.pedigree.webApi.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.webApi.services.ApiInstanceService;
import cn.sh.library.pedigree.webApi.sparql.ApiInstanceSparql;

/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
@Service
public class ApiInstanceServiceImpl extends BaseServiceImpl implements
		ApiInstanceService {

	@Resource
	private ApiInstanceSparql apiInstanceSparql;

	@Override
	public List<Map<String, String>> getFamilyRules(String keyword,
			Pager pager, String[] workUris) {

		QueryResult<Map<String, Object>> queryResult = apiInstanceSparql
				.getFamilyRules(keyword, pager.getStartIndex(),
						pager.getPageSize(), workUris);
		pager.calcPageCount(queryResult.getTotalrecord());

		return RDFUtils.transformListMap(queryResult.getResultList());
	}

	@Override
	public Map<String, Object> getFamilyRuleInfo(String uri, boolean isWithSub) {
		return apiInstanceSparql.getFamilyRuleInfo(uri, isWithSub);
	}

	@Override
	public List<Map<String, Object>> getFamilyRulesByWorks(String[] workUris) {

		QueryResult<Map<String, Object>> queryResult = apiInstanceSparql
				.getFamilyRulesByWorks(workUris, 0, 9999);
		return queryResult.getResultList();
	}
	
	@Override
	public List<Map<String, Object>> getFamilyRulesByWorks(String[] workUris, Pager pager) {
		QueryResult<Map<String, Object>> queryResult = apiInstanceSparql
				.getFamilyRulesByWorks(workUris, pager.getStartIndex(),
						pager.getPageSize());
		pager.calcPageCount(queryResult.getTotalrecord());

		return (queryResult.getResultList());
	}

	@Override
	public List<Map<String, Object>> getSubFamilyRules(String uri,
			boolean isWithSub) {
		return apiInstanceSparql.getSubFamilyRules(uri, isWithSub);
	}

}
