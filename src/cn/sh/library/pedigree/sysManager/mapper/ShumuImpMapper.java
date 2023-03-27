package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.ShumuModel;

@Repository
public interface ShumuImpMapper {
	/**
	 * 正书名
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ShumuModel> getZhengshumingData(ShumuModel dcsdpc);

	int updateZhengshumingById(ShumuModel info);

	/**
	 * 变异书名
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ShumuModel> getBianyishumingData(ShumuModel dcsdpc);

	/**
	 * 责任者时代
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ShumuModel> getZrzsdPersonList(ShumuModel dcsdpc);

	/**
	 * 匹配责任者，1对多插入
	 * 
	 * @param identifier
	 * @return
	 */
	List<ShumuModel> getZrzPersonListByIdentifier(
			@Param(value = "identifier") String identifier);

	/**
	 * 责任方式等匹配插入
	 * 
	 * @param type
	 * @return
	 */
	List<ShumuModel> getCategoryListByType(@Param(value = "type") String type);

	/**
	 * 其他责任者时代
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ShumuModel> getQtZrzsdPersonList(ShumuModel dcsdpc);

	/**
	 * 匹配其他责任者，1对多插入
	 * 
	 * @param identifier
	 * @return
	 */
	List<ShumuModel> getQtZrzPersonListByIdentifier(
			@Param(value = "identifier") String identifier);

	/**
	 * 其他责任方式等匹配插入
	 * 
	 * @param type
	 * @return
	 */
	List<ShumuModel> getQtCategoryListByType(@Param(value = "type") String type);

	/**
	 * 正书名匹配
	 * 
	 * @param identifier
	 * @return
	 */
	ShumuModel getZhengshumingById(
			@Param(value = "identifier") String identifier);

	/**
	 * 变异书名匹配
	 * 
	 * @param identifier
	 * @return
	 */
	List<ShumuModel> getBianyishumingById(
			@Param(value = "identifier") String identifier);

	/**
	 * 堂号
	 * 
	 * @param identifier
	 * @return
	 */
	List<ShumuModel> getTanghaoListIdentifier(
			@Param(value = "identifier") String identifier);

	/**
	 * ItemList
	 * 
	 * @param identifier
	 * @return
	 */
	List<ShumuModel> getItemListIdentifier(
			@Param(value = "identifier") String identifier);

	int updateBianyishumingById(ShumuModel info);

	/**
	 * 根据朝代汉文翻译URI
	 * 
	 * @param cdName
	 * @return
	 */
	String getCdUriByCdName(@Param(value = "cdName") String cdName);

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	List<ShumuModel> getShumuJudiNull();

	/**
	 * DOIlist
	 * 
	 * @return
	 */
	List<ShumuModel> getDOIList();

	/**
	 * 错误机构匹配，（香港中大）
	 * 
	 * @return
	 */
	List<ShumuModel> getErrorGCDList();

	/**
	 * 获取书目姓氏列表
	 * 
	 * @return
	 */
	List<ShumuModel> getWorkFamilyNameUriList();

	/**
	 * 书目Place错误数据
	 * @return
	 */
	List<ShumuModel> getWorkErrorPlace();
	
}
