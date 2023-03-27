package cn.sh.library.pedigree.webApi.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dto.Item;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.webApi.dto.searchBean.ApiWorkSearchBean;

/**
 * @author liuyi
 * @date 2015/1/5 0005
 */
public interface ApiWorkService extends BaseService {

	/**
	 * 查询谱籍列表
	 * 
	 * @param search
	 * @param pager
	 * @return
	 */
	public List<Map<String, String>> list(ApiWorkSearchBean search, Pager pager);

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
	
	/**
	 * workList分面
	 * @param search
	 * @return
	 */
	public List<Map<String, String>>  getWorkFacetList(ApiWorkSearchBean search);
	
	/**
	 * workList"其他"分面
	 * @param search
	 * @return
	 */
	public List<Map<String, String>> getWorkFacetOthersList(ApiWorkSearchBean search);
	
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
	public boolean updateAccessLevelByItemUri(String uri,String newValue);

	/**
	 * 根据传入的DOI，可包含分隔符，更新层级标记0或1
	 * @param uri
	 * @param doi
	 * @return
	 */
	public boolean updateAccessLevelByDoi(String doi,String flg);
	
	public  List<Map<String, String>>  getFreeResultList(String free_text,Integer maxCount);

	/**
	 * 根据WorkUri获取DOI
	 * @param workUri
	 * @return
	 */
	public Map getDoiByWorkUri(String workUri);
	
	/**
	 * 根据workUri获取详细信息 chenss 20191205
	 * @param workUri
	 * @return
	 */
	public Map getDetailByWorkUri(String workUri);
	public Map  getQxTjInfo(String fname);
	public Map  getPlceTjInfo(String fname);

public  Integer getWorkCountByPlaceAndFname(List<String> place,String fnameUri);
}
