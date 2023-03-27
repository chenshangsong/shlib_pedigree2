package cn.sh.library.pedigree.framework.util;

import java.util.List;

import javax.annotation.Resource;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.sysManager.model.ApiChaodaiDto;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiChaodaiService;

/**
 * @author 陈铭
 */
public class PreloadChaodaiList implements IfPreloadBean {
	
	@Resource
	private ApiChaodaiService apiChaodaiService;
	private List<ApiChaodaiDto> chaodaiList;
	private static PreloadChaodaiList instance;

	/**
	 * 实例化方法
	 */
	public static synchronized PreloadChaodaiList getInstance() {
		if (null == instance) {
			instance = new PreloadChaodaiList();
		}
		return instance;
	}
	
	/**
	 * 获取朝代列表
	 */
	@Override
	public void loadInfo() {
		//判断PreloadChaodaiList是否被实例化
		if (instance == null) {
			getInstance();
		}
		//判断apiChaodaiService是否被实例化
		if (instance.apiChaodaiService == null) {
			instance.apiChaodaiService = apiChaodaiService;
		}
		//获取朝代列表
		instance.chaodaiList = instance.apiChaodaiService.getChaodaiList();
	}

	/**
	 *根据输入年份，获取对应朝代
	 *@return String
	 *返回  chaodai 字段
	 */
	public String getDynastyByYear(String year) {
		//1个年份可能对应多个朝代
		String chaodaiMore = "";
		//转为数字
		Integer intYear = null;
		
		//如果年份不为空  and 年份是数字
		if( !StringUtilC.isEmpty(year) && StringUtilC.isInteger(year) ){
			//将String转为Integer
			intYear = StringUtilC.getInteger(year);
			//循环朝代列表
			for (ApiChaodaiDto dto : instance.chaodaiList) {
				if( intYear >= StringUtilC.getInteger(dto.getBeginY()) && 
					intYear <= StringUtilC.getInteger(dto.getEndY()) ){
					//拼接所查询的朝代（如果有多个）
					chaodaiMore += "," + dto.getChaodai();
				}
			}
			//判断是否查询到了数据
			return StringUtilC.isEmpty(chaodaiMore)?chaodaiMore:chaodaiMore.substring(1);
			
		} else {
			//直接返回 ""
			return chaodaiMore;
		}
	}
	
	/**
	 *根据输入年份，获取对应朝代的所有信息
	 *@return ApiChaodaiDto
	 *返回  ApiChaodaiDto 对象
	 */
	public ApiChaodaiDto getDynastyAllInfoByYear(String year) {
		//转为数字
		Integer intYear = null;
		//所要查询的朝代信息
		ApiChaodaiDto apiChaodaiDto = new ApiChaodaiDto();
		
		//如果年份不为空  and 年份是数字
		if( !StringUtilC.isEmpty(year) && StringUtilC.isInteger(year) ){
			//将String转为Integer
			intYear = StringUtilC.getInteger(year);
			//循环朝代列表
			for (ApiChaodaiDto dto : instance.chaodaiList) {
				if( intYear >= StringUtilC.getInteger(dto.getBeginY()) && 
					intYear <= StringUtilC.getInteger(dto.getEndY()) ){
					//赋值
					apiChaodaiDto = dto;
					break;
				}
			}
		}
		return apiChaodaiDto;
	}
	
	@Override
	public void reloadInfo() {
		loadInfo();
	}

}
