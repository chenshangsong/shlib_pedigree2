package cn.sh.library.pedigree.webApi.sparql;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface ApiInstanceSparql extends BaseDao {
	public QueryResult<Map<String, Object>> getFamilyRules(String keyword,
			int start, int limit, String[] workUris);

	public Map<String, Object> getFamilyRuleInfo(String uri, boolean isWithSub);
	
	public QueryResult<Map<String, Object>> getFamilyRulesByWorks(String[] workUris,
			int start, int pagesize);

	public List<Map<String, Object>> getSubFamilyRules(String uri,
			boolean isWithSub);

}