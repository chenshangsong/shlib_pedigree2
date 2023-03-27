package cn.sh.library.pedigree.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.DynastyService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.TemporalSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * @author liuyi
 * @date 2014/12/27 0027
 */
@Service
public class DynastyServiceImpl extends BaseServiceImpl implements
		DynastyService {

	@Resource
	private BaseinfoSparql baseinfoSparql;
	@Resource
	private PersonSparql personSparql;
	@Resource
	private TemporalSparql temporalSparql;

	@Override
	public List<Map<String, String>> list() {
		List<Map<String, String>> result = RDFUtils
				.transformListMap(this.baseinfoSparql.getTemporal());
		for (Map<String, String> item : result) {
			String count = this.personSparql.countPersons(item.get("uri"));
			item.put("personCount", count);
		}
		return result;
	}

	@Override
	public List<Map<String, String>> listPersons(String uri, Pager pager) {
		QueryResult<Map<String, Object>> queryResult = this.baseinfoSparql
				.getPersons(uri, pager.getStartIndex(), pager.getPageSize());
		pager.calcPageCount(queryResult.getTotalrecord());
		return RDFUtils.transformListMap(queryResult.getResultList());
	}

	@Override
	public List<Map<String, String>> listWorks(String uri, Pager pager) {
		QueryResult<Map<String, Object>> queryResult = this.baseinfoSparql
				.getWorks(uri, pager.getStartIndex(), pager.getPageSize());
		pager.calcPageCount(queryResult.getTotalrecord());
		return RDFUtils.transformListMap(queryResult.getResultList());
	}

	@Override
	public List<Map<String, String>> listTemps4TL() {
		try {
			List<Map<String, String>> result = RDFUtils
					.transformListMap(this.temporalSparql.getTemporals4TL());
			return result;
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return null;
	}
}
