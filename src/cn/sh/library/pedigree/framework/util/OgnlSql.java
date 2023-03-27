package  cn.sh.library.pedigree.framework.util;

/**
 * クラス名 : Check <br>
 * 機能概要 : SQLツールクラス</br> 
 * コピーライト: Copyright © 2011 NC Corporation, All Rights Reserved.</br> 
 * 
 * Sample:
 * <br>&lt;if test="@framework.common.Ognl@isNotEmpty(userId)">
 * <br>　　　　and user_id = #{userId}
 * <br>
 * &lt;/if></br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */

public class OgnlSql {

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null){
			return true;
		}else if (obj instanceof String) {
			return ((String) obj).trim().length() == 0;
		}else {
			throw new IllegalArgumentException(
					"Illegal argument type,must be : String. but was:"
							+ obj.getClass());
		}
	}

	public static boolean allEmpty(Object... obj) {
		int iCnt = 0;
		for(int i = 0 ; i < obj.length; i ++){
			if(isEmpty(obj)){
				iCnt = iCnt + 1;
			}
		}
		if(iCnt == obj.length){
			return true;
		}
		return false;
	}

	private static int emptyObjectCount(Object... objects) {
		int cnt = 0;
		for (Object obj : objects) {
			if(isEmpty(obj)){
				cnt++;
			}
		}
		return cnt;
	}
	
	public static boolean allEmpty(Object obj1, Object obj2) {
		if(emptyObjectCount(obj1, obj2) == 2){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean allEmpty(Object obj1, Object obj2, Object obj3) {
		if(emptyObjectCount(obj1, obj2, obj3) == 3){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean allEmpty(Object obj1, Object obj2, Object obj3, Object obj4) {
		if(emptyObjectCount(obj1, obj2, obj3, obj4) == 4){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean allEmpty(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
		if(emptyObjectCount(obj1, obj2, obj3, obj4, obj5) == 5){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean notFirstValue(Object obj1, Object obj2) {
		if(isEmpty(obj2)){
			return false;
		}else{
			int iEmptys = emptyObjectCount(obj1);
			if(iEmptys == 1){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean notFirstValue(Object obj1, Object obj2, Object obj3) {
		if(isEmpty(obj3)){
			return false;
		}else{
			int iEmptys = emptyObjectCount(obj1, obj2);
			if(iEmptys == 2){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean notFirstValue(Object obj1, Object obj2, Object obj3, Object obj4) {
		if(isEmpty(obj4)){
			return false;
		}else{
			int iEmptys = emptyObjectCount(obj1, obj2, obj3);
			if(iEmptys == 3){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean notFirstValue(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
		if(isEmpty(obj5)){
			return false;
		}else{
			int iEmptys = emptyObjectCount(obj1, obj2, obj3, obj4);
			if(iEmptys == 4){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean hasValue(Object obj1, Object obj2) {
		int iEmptys = emptyObjectCount(obj1, obj2);
		if(iEmptys != 2){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean hasValue(Object obj1, Object obj2, Object obj3) {
		int iEmptys = emptyObjectCount(obj1, obj2, obj3);
		if(iEmptys != 3){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean hasValue(Object obj1, Object obj2, Object obj3, Object obj4) {
		int iEmptys = emptyObjectCount(obj1, obj2, obj3, obj4);
		if(iEmptys != 4){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean hasValue(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
		int iEmptys = emptyObjectCount(obj1, obj2, obj3, obj4, obj5);
		if(iEmptys != 5){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean noEmpty(Object obj1, Object obj2) {
		if(emptyObjectCount(obj1, obj2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean noEmpty(Object obj1, Object obj2, Object obj3) {
		if(emptyObjectCount(obj1, obj2, obj3) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean noEmpty(Object obj1, Object obj2, Object obj3, Object obj4) {
		if(emptyObjectCount(obj1, obj2, obj3, obj4) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean noEmpty(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
		if(emptyObjectCount(obj1, obj2, obj3, obj4, obj5) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static boolean isNotBlank(Object obj) {
		return !isBlank(obj);
	}

	public static boolean isNumber(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Number) {
			return true;
		}
		if (obj instanceof String) {
			try {
				Double.parseDouble((String) obj);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	public static boolean isBlank(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof String) {
			String str = (String) obj;
			return isBlank(str);
		}
		return false;
	}

	public static boolean isBlank(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}

		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isTrue(Object obj) {
		return Boolean.parseBoolean(obj+"");
	}

}
