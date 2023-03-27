package cn.sh.library.pedigree.webApi.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.services.BaseService;

/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
public interface ApiInstanceService extends BaseService {

	/**
	 * 全文检索
	 * 
	 * @param keyword
	 * @param pager
	 * @return
	 */
	public List<Map<String, String>> getFamilyRules(String keyword,
			Pager pager, String[] workUris);

	public Map<String, Object> getFamilyRuleInfo(String uri, boolean isWithSub);

	public List<Map<String, Object>> getFamilyRulesByWorks(String[] workUris);
	
	public List<Map<String, Object>> getFamilyRulesByWorks(String[] workUris, Pager pager);

	public List<Map<String, Object>> getSubFamilyRules(String uri,
			boolean isWithSub);
}
