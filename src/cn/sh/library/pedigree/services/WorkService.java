package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.dto.Item;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Work;

/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
public interface WorkService extends BaseService {

	/**
	 * 全文检索
	 * 
	 * @param keyword
	 * @param pager
	 * @return
	 */
	public List<Map<String, String>> list(String keyword, Pager pager);

	/**
	 * 公共检索
	 * 
	 * @param search
	 * @param pager
	 * @return
	 */
	// public List<Map<String, String>> list(PublicSearchBean search, Pager
	// pager);

	/**
	 * 查询谱籍列表
	 * 
	 * @param search
	 * @param pager
	 * @return
	 */
	public List<Map<String, String>> list(WorkSearchBean search, Pager pager);


	/**
	 * 通过work查询item数量
	 * 
	 * @param search
	 * @return
	 */
	public Long countItemsByWork(WorkSearchBean search);
	
	
	/**
	 * 查询谱籍列表
	 * 
	 * @param personUri
	 * @param inference
	 * @return
	 */
	public List<Work> listWork4Person(String personUri, Boolean inference);

	/**
	 * 获取work
	 * 
	 * @param uri
	 * @return
	 */
	public Work getWork(String uri, boolean ifFullLink);

	public List<Map<String, String>> getWorksInTimeline(String year,
			String endY, int unit, String name);

	/**
	 * 根据DOI，查出Item对象集合。
	 * @param doi
	 * @return
	 */
	public List<Item> getItemListByDoi(String doi);

	/**
	 * 根据Item的Uri，更新层级标记0或1
	 * @param uri
	 * @param doi
	 * @return
	 */
	public boolean updateAccessLevelByItemUri(Item item,String doiSpread,String newValue);

	/**
	 * 根据传入的DOI，可包含分隔符，更新层级标记0或1
	 * @param uri
	 * @param doi
	 * @return
	 */
	public boolean updateAccessLevelByDoi(String doi,String flg);
	
}
