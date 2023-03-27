package cn.sh.library.pedigree.framework;

import cn.sh.library.pedigree.framework.util.StringUtil;



/**
 * 类名 : Check <br>
 * 机能概要 : SQL XML工具类</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * Sample:
 * <br>&lt;if test="@ cn.sh.library.pedigree.framework.handler.ognl@isNotEmpty(userId)">
 * <br>　　　　and user_id = #{userId}
 * <br>
 * &lt;/if></br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class Ognl {
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Object obj) {		
		if(obj == null){
			return true;
		}
		if(getEmptyCount(obj) == 1){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Object obj1, Object obj2) {		
		if(getEmptyCount(obj1, obj2) == 2){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {		
		if(obj == null){
			return false;
		}
		if(getEmptyCount(obj) == 0){
			return true;
		}
		return false;
	}
//	/**
//	 * 
//	 * @param c
//	 * @return
//	 */
//	public static boolean isNotEmpty(Object obj, String notStr) {		
//		if(obj == null){
//			return false;
//		}
//		if(getEmptyCount(obj) == 0){
//			if(obj.equals(notStr)){
//				return false;
//			}
//			return true;
//		}
//		return false;
//	}
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object obj1, Object obj2) {		
		if(getEmptyCount(obj1, obj2) == 0){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean hasValue(Object obj1, Object obj2) {		
		return getEmptyCount(obj1, obj2) > 0;
	}

	
	
	private static int getEmptyCount(Object... objs) {
		int i = 0;
		for(Object obj : objs){
			if (obj != null){
				if (obj instanceof String || obj instanceof Integer) {
					if(StringUtil.isEmpty(obj)){
						i ++;
					}
				}else {
					throw new IllegalArgumentException(
							"Illegal argument type,must be : String. but was:"
									+ obj.getClass());
				}
			}else{
				i ++;
			}
		}
		return i;
	}
	
	public static boolean isEqual(Object obj, String str) {
		if (!(obj instanceof String)) {
			throw new IllegalArgumentException(
					"Illegal argument type,must be : String. but was:"
							+ obj.getClass());
		}else{
			if (StringUtil.isEmpty(obj)) {
				return str.equals(obj);
			}else{
				return obj.equals(str);
			}
			
		}
	}

	

}
