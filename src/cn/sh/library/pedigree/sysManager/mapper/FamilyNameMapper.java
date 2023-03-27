package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.ChaodaiModel;
import cn.sh.library.pedigree.sysManager.model.ChaodaiYearModel;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.HuPersonModel;
import cn.sh.library.pedigree.sysManager.model.JigouModel;
import cn.sh.library.pedigree.sysManager.model.PlaceModel;
import cn.sh.library.pedigree.sysManager.model.ShumuModel;
import cn.sh.library.pedigree.sysManager.model.SurnameModel;

@Repository
public interface FamilyNameMapper {

	/**
	 * 获得姓氏列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getTableData(FamilyNameModel dcsdpc);
	
	/**
	 * 获得胡适人员列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<HuPersonModel> getHushiTableData(HuPersonModel dcsdpc);
	/**
	 * 获取胡适家族配偶信息
	 * @param dcsdpc
	 * @return
	 */
	List<HuPersonModel> getHushiPeiouTableData(HuPersonModel dcsdpc);
	
	/**
	 * 根据老公ＩＤ获取胡适家族配偶信息
	 * @param dcsdpc
	 * @return
	 */
	List<HuPersonModel> getHushiPeiouByid(String id);
	/**
	 * 更新胡适人员列表信息
	 * 
	 * @param info
	 */
	int updateHushiFamilyNameById(HuPersonModel info);
	/**
	 * 获取先祖列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getXZData(FamilyNameModel dcsdpc);

	/**
	 * 获取名人列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getMRData(FamilyNameModel dcsdpc);

	/**
	 * 获取纂修者列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getZXZData(FamilyNameModel dcsdpc);

	/**
	 * 获取堂号列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getTangHaoData(FamilyNameModel dcsdpc);

	/**
	 * 获取puming列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getPumingData(FamilyNameModel dcsdpc);

	/**
	 * 更新用户信息
	 * 
	 * @param info
	 */
	int updateFamilyNameById(FamilyNameModel info);

	/**
	 * 更新先祖信息
	 * 
	 * @param info
	 */
	int updatePersonById(FamilyNameModel info);

	/**
	 * 更新名人信息
	 * 
	 * @param info
	 */
	int updatePerson2ById(FamilyNameModel info);

	/**
	 * 更新专修者信息
	 * 
	 * @param info
	 */
	int updatePersonZXZById(FamilyNameModel info);

	/**
	 * 更新堂号信息
	 * 
	 * @param info
	 */
	int updateTanghaoById(FamilyNameModel info);

	/**
	 * 更新谱名信息
	 * 
	 * @param info
	 */
	int updatePumingById(FamilyNameModel info);

	/**
	 * 获取地点列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<PlaceModel> getPlaceData(PlaceModel dcsdpc);

	/**
	 * 更新地点信息
	 * 
	 * @param info
	 */
	int updatePlaceById(PlaceModel info);

	/**
	 * 获取机构列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<JigouModel> getJigouData(JigouModel dcsdpc);

	/**
	 * 获取取词词表列表
	 * 
	 * @param dpudc
	 * @return
	 */
	List<CategoryModel> getCategoryData(CategoryModel dcsdpc);

	/**
	 * 更新机构信息
	 * 
	 * @param info
	 */
	int updateJigouById(JigouModel info);

	/**
	 * 获取书目列表
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ShumuModel> getShumuData(ShumuModel dcsdpc);

	/**
	 * 更新书目信息
	 * 
	 * @param info
	 */
	int updateShumuById(ShumuModel info);

	/**
	 * 获取朝代列表
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ChaodaiModel> getChaodaiData(ChaodaiModel dcsdpc);

	/**
	 * 更新朝代信息
	 * 
	 * @param info
	 */
	int updateChaodaiById(ChaodaiModel info);

	/**
	 * 获取年号纪年列表
	 * 
	 * @param dcsdpc
	 * @return
	 */
	List<ChaodaiYearModel> getChaodaiYearData(ChaodaiYearModel dcsdpc);

	/**
	 * 更新年号纪年信息
	 * 
	 * @param info
	 */
	int updateChaodaiYearById(ChaodaiYearModel info);
	
	/**
	 * 更新首页姓氏表
	 * @return
	 */
	int updateSurname(SurnameModel info);
	/**
	 * 更新首页姓氏描述
	 * @return
	 */
	int updatesSurnameDesByUri(SurnameModel info);
	
	/**
	 * 获取盛档SDPerson
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getSDPersonData(FamilyNameModel dcsdpc);
	/**
	 * 更新盛档SDPerson
	 * @return
	 */
	int updateSDPersonData(FamilyNameModel info);
	
	/**
	 * 获取盛档SDOrganization
	 * 
	 * @param dpudc
	 * @return
	 */
	List<FamilyNameModel> getSDOrganization(FamilyNameModel dcsdpc);
	/**
	 * 更新盛档SDOrganization
	 * @return
	 */
	int updateSDOrganization(FamilyNameModel info);
}
