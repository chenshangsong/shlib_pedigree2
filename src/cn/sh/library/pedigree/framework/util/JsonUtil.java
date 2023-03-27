package  cn.sh.library.pedigree.framework.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sh.library.pedigree.framework.model.BaseDto;



/**
 * クラス名 : JsonUtil <br>
 * 機能概要 : </br> 
 * コピーライト: Copyright © 2011 NC Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class JsonUtil {
	
	/**
	 * LabelValueBeanのArrayListをJsonへ変換する
	 * @param list
	 * @return
	 */
	public static String getDtoJson(List<BaseDto> list){
		ObjectMapper objMap = new ObjectMapper();
		try {
		    return objMap.writeValueAsString(list) ;
//			return URLEncoder.encode(objMap.writeValueAsString(list),"UTF-8").trim();
		} catch (Exception e) {
			return "";
		} 
	}
	
	public static String getDtoJson(BaseDto dto){
	    List<BaseDto> list = new ArrayList<BaseDto>();
		list.add(dto);
		return getDtoJson(list);
	}
	

	
	public static void writeValue(HttpServletResponse response, Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();	
			objectMapper.writeValue(response.getOutputStream(), obj);
		} catch (Exception e) {
		}
	}
	

	
	public static String getJsonString(Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();	
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			return "";
		}
	}
}
