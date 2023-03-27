package cn.sh.library.pedigree.common.dataImport;

import java.util.ArrayList;
import java.util.List;

import cn.sh.library.pedigree.common.LanguageGroup;
import cn.sh.library.pedigree.dto.sysmanager.DataEditDto;
import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;
import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryListModel;
import cn.sh.library.pedigree.sysManager.model.DataChangeHistoryMainModel;
import cn.sh.library.pedigree.sysManager.model.FamilyNameModel;
import cn.sh.library.pedigree.sysManager.model.JigouModel;
import cn.sh.library.pedigree.sysManager.model.PersonModel;
import cn.sh.library.pedigree.sysManager.model.PlaceModel;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 导入model做成
 * 
 * @author chenss
 *
 */
public class ModelMakeUtil {

	/**
	 * 填充place对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static PlaceModel getPlaceModel(DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		PlaceModel placemodel = new PlaceModel();
		// Uri
		placemodel.setSelfUri(dto.getDataUri());
		for (DataChangeHistoryListModel tis : listMolde) {
			String thisValue = tis.getNewValue();
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName.toLowerCase()) {
			// 规范名
			case "label":
				// 简体
				placemodel.setLabelS(chsValue);
				// 繁体
				placemodel.setLabelT(chtValue);
				break;
			// 国家
			case "country":
				// 简体
				placemodel.setGuojiaS(chsValue);
				// 繁体
				placemodel.setGuojiaT(chtValue);
				break;
			// 省
			case "province":
				// 简体
				placemodel.setShengS(NonValue);
				break;
			// 市
			case "city":
				// 简体
				placemodel.setShiS(NonValue);
				break;
			// 县
			case "county":
				// 简体
				placemodel.setXianS(NonValue);
				break;
			// 镇，临时使用该参数
			case "town":
				// 简体
				placemodel.setParm1S(NonValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				placemodel.setDescription(NonValue);
				break;
			default:
				break;
			}

		}
		return placemodel;
	}

	/**
	 * 填充堂号对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static FamilyNameModel getTanghaoModel(
			DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		FamilyNameModel fmodel = new FamilyNameModel();
		// Uri
		fmodel.setSelfUri(dto.getDataUri());
		for (DataChangeHistoryListModel tis : listMolde) {
			// 新值
			String thisValue = tis.getNewValue();
			// 英文名称
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName) {
			// 规范名
			case "label":
				// 简体
				fmodel.setFamilyNameS(chsValue);
				// 繁体
				fmodel.setFamilyNameT(chtValue);
				break;
			// 姓氏
			case "familyName":
				// 简体
				fmodel.setFamilyUri(NonValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				fmodel.setDescription(NonValue);
				break;
			default:
				break;
			}

		}
		return fmodel;
	}

	/**
	 * 填充堂号对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static PersonModel getPersonModel(DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		PersonModel fmodel = new PersonModel();
		// Uri
		fmodel.setSelfUri(dto.getDataUri());
		for (DataChangeHistoryListModel tis : listMolde) {
			// 新值
			String thisValue = tis.getNewValue();
			// 英文名称
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName) {
			// 规范名
			case "label":
				// 简体
				fmodel.setLabelS(chsValue);
				// 繁体
				fmodel.setLabelT(chtValue);
				break;
			// 姓氏
			case "familyName":
				// 简体
				fmodel.setFamilyName(NonValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				fmodel.setDescription(NonValue);
				break;
			// 先祖类型
			case "roleOfFamily":
				fmodel.setRoleUri(NonValue);
				break;
			// 字
			case "courtesyName":
				fmodel.setZhi(NonValue);
				break;
			// 号
			case "pseudonym":
				fmodel.setHao(NonValue);
				break;
			// 行
			case "orderOfSeniority":
				fmodel.setHang(NonValue);
				break;
			// 谥号
			case "posthumousTitle":
				fmodel.setShi(NonValue);
				break;
			// 时代
			case "temporalValue":
				fmodel.setShidai(NonValue);
				break;
			// 时代URI
			case "temporal":
				fmodel.setShidaiUri(NonValue);
				break;
			// 父
			case "childOf":
				fmodel.setChildOf(NonValue);
				break;
			// 生于
			case "birthday":
				fmodel.setBirthday(NonValue);
				break;
			// 死于
			case "deathday":
				fmodel.setDeathday(NonValue);
				break;
			// 谱名
			case "genealogyName":
				fmodel.setGenealogyName(NonValue);
				break;
			// 字辈
			case "generationCharacter":
				fmodel.setGenerationCharacter(NonValue);
				break;
			// 相关作品
			case "relatedWork":
				fmodel.setRelatedWork(NonValue);
				break;
			default:
				break;
			}

		}
		return fmodel;
	}

	/**
	 * 填充堂号对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static FamilyNameModel getFamilyModel(
			DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		FamilyNameModel fmodel = new FamilyNameModel();
		// Uri
		fmodel.setSelfUri(dto.getDataUri());
		for (DataChangeHistoryListModel tis : listMolde) {
			// 新值
			String thisValue = tis.getNewValue();
			// 英文名称
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 繁体
			String enValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.En.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName) {
			// 规范名
			case "label":
				// 简体
				fmodel.setFamilyNameS(chsValue);
				// 繁体
				fmodel.setFamilyNameT(chtValue);
				// 繁体
				fmodel.setPinyin(enValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				fmodel.setDescription(NonValue);
				break;
			default:
				break;
			}

		}
		return fmodel;
	}

	/**
	 * 填充堂号对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static JigouModel getJigouModel(DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		JigouModel fmodel = new JigouModel();
		// Uri
		fmodel.setSelfUri(dto.getDataUri());
		for (DataChangeHistoryListModel tis : listMolde) {
			// 新值
			String thisValue = tis.getNewValue();
			// 英文名称
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 繁体
			String enValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.En.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName) {
			// 规范名
			case "label":
				// 简体
				fmodel.setQuanchengS(chsValue);
				// 繁体
				fmodel.setQuanchengT(chtValue);
				// 英文
				fmodel.setAddressE(enValue);
				break;
			// 简称
			case "abbreviateName":
				// 简体
				fmodel.setJianchengS(chsValue);
				// 繁体
				fmodel.setJianchengT(chtValue);
				break;
			// 地址
			case "address":
				// 简体
				fmodel.setAddressS(chsValue);
				// 繁体
				fmodel.setAddressT(chtValue);
				// 繁体
				fmodel.setAddressO(enValue);
				break;
			// 地址Uri
			case "region":
				// 简体
				fmodel.setPlaceUri(NonValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				fmodel.setDescription(NonValue);
				break;
			default:
				break;
			}

		}
		return fmodel;
	}

	/**
	 * 填充堂号对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static CategoryModel getCategoryModel(
			DataChangeHistoryMainModel dto,
			List<DataChangeHistoryListModel> listMolde) {
		CategoryModel fmodel = new CategoryModel();
		// Uri
		fmodel.setSelfUri(dto.getDataUri());
		for (DataChangeHistoryListModel tis : listMolde) {
			// 新值
			String thisValue = tis.getNewValue();
			// 英文名称
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName) {
			// 规范名
			case "label":
				// 简体
				fmodel.setLabel(chsValue);
				// 繁体
				fmodel.setLabelT(chtValue);
				break;
			// 简称
			case "categoryType":
				// 简体
				fmodel.setType(NonValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				fmodel.setDescription(NonValue);
				break;
			default:
				break;
			}
		}
		return fmodel;
	}

	/**
	 * 填充堂号对象
	 * 
	 * @param listMolde
	 * @return
	 */
	public static CategoryModel getCategoryModel(DataEditViewDto dto) {
		CategoryModel fmodel = new CategoryModel();
		for (DataEditDto tis : dto.getEditList()) {
			// 新值
			String thisValue = tis.getNewValue();
			// 英文名称
			String oldEnName = tis.getOldEnName();
			// 简体
			String chsValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Chs.getGroup());
			// 繁体
			String chtValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Cht.getGroup());
			// 正常
			String NonValue = DataUtilC.getValueCTE(thisValue,
					LanguageGroup.Non.getGroup());
			switch (oldEnName) {
			// 规范名
			case "label":
				// 简体
				fmodel.setLabel(chsValue);
				// 繁体
				fmodel.setLabelT(chtValue);
				break;
			// 简称
			case "categoryType":
				// 简体
				fmodel.setType(NonValue);
				break;
			// 说明，临时使用该参数
			case "description":
				// 简体
				fmodel.setDescription(NonValue);
				break;
			default:
				break;
			}
		}
		return fmodel;
	}

	/**
	 * 页面对象转换为历史表对象
	 * 
	 * @param dto
	 * @param ifUpdate
	 * @return
	 */
	public static List<DataChangeHistoryListModel> ContentToHistoryList(
			DataEditViewDto dto) {
		List<DataChangeHistoryListModel> list = new ArrayList<DataChangeHistoryListModel>();
		for (DataEditDto editDto : dto.getEditList()) {
			String p = StringUtilC.getString(editDto.getOldCnNameUri());
			String old_o = StringUtilC.getString(editDto.getOldValue());
			String o = StringUtilC.getString(editDto.getNewValue());
			String comment = StringUtilC.getString(editDto.getOldComment());
			String cnName = StringUtilC.getString(editDto.getOldCnName());
			String enName = StringUtilC.getString(editDto.getOldEnName());
			String oldValueCn = StringUtilC.getString(editDto.getOldValueCn());
			String newValueCn = StringUtilC.getString(editDto.getNewValueCn());
			String isUri = StringUtilC.getString(editDto.getIsUri());
			// 新值和旧值不相等的情况下，更新
			if (!old_o.equals(o)) {
				DataChangeHistoryListModel model = new DataChangeHistoryListModel();
				model.setOldCnNameUri(p);
				model.setOldValue(old_o);
				model.setNewValue(o);
				model.setOldComment(comment);
				model.setOldCnName(cnName);
				model.setOldEnName(enName);
				model.setOldValueCn(oldValueCn);
				model.setIsUri(isUri);
				model.setNewValueCn(newValueCn);
				list.add(model);
			}
		}
		return list;
	}
}
