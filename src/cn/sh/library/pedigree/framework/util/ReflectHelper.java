package  cn.sh.library.pedigree.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import cn.sh.library.pedigree.framework.model.BaseDto;

/** 
 * @author Administrator 
 *  反射工具 
 */  
public class ReflectHelper {  
    /** 
     * 获取obj对象fieldName的Field 
     * @param obj 
     * @param fieldName 
     * @return 
     */  
    public static Field getFieldByFieldName(Object obj, String fieldName) {  
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass  
                .getSuperclass()) {  
            try {  
                return superClass.getDeclaredField(fieldName);  
            } catch (NoSuchFieldException e) {  
            }  
        }  
        return null;  
    }  
  
    /** 
     * 获取obj对象fieldName的属性值 
     * @param obj 
     * @param fieldName 
     * @return 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */  
    public static Object getValueByFieldName(Object obj, String fieldName)  
            throws SecurityException, NoSuchFieldException,  
            IllegalArgumentException, IllegalAccessException {  
        Field field = getFieldByFieldName(obj, fieldName);  
        Object value = null;  
        if(field!=null){  
            if (field.isAccessible()) {  
                value = field.get(obj);  
            } else {  
                field.setAccessible(true);  
                value = field.get(obj);  
                field.setAccessible(false);  
            }  
        }  
        return value;  
    }  
  
    /** 
     * 设置obj对象fieldName的属性值 
     * @param obj 
     * @param fieldName 
     * @param value 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */  
    public static void setValueByFieldName(Object obj, String fieldName,  
            Object value) throws SecurityException, NoSuchFieldException,  
            IllegalArgumentException, IllegalAccessException {  
        Field field = obj.getClass().getDeclaredField(fieldName);  
        if (field.isAccessible()) {  
            field.set(obj, value);  
        } else {  
            field.setAccessible(true);  
            field.set(obj, value);  
            field.setAccessible(false);  
        }  
    }  
    

	public Object invokeMethod(Class<?> cls, String methodName, Object arg){  
		return invokeMethod(cls, methodName, new Object[]{arg});
	}
	
	public Object invokeMethod(Class<?> cls, String methodName){  
		return invokeMethod(cls, methodName, new Object[0]);
	}
	
	public Object invokeMethod(Class<?> cls, String methodName, Object[] args){  
		final Logger logger = Logger.getLogger(BaseDto.class);
		try{
		     Class<?>[] argsClass = new Class<?>[args.length];  
		     for (int i = 0; i < args.length; i++) {  
		         argsClass[i] = args[i].getClass();  
		     }  
		     Method method =cls.getMethod(methodName,argsClass);  
		     return method.invoke(this, args);  
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	} 
}  