package cn.sh.library.pedigree.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSonUtils {

	static ObjectMapper objectMapper;

	/**
	 * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
	 * (1)转换为普通JavaBean：readValue(json,Student.class)
	 * (2)转换为List:readValue(json,List
	 * .class).但是如果我们想把json转换为特定类型的List，比如List<Student>，就不能直接进行转换了。
	 * 因为readValue(json
	 * ,List.class)返回的其实是List<Map>类型，你不能指定readValue()的第二个参数是List<
	 * Student>.class，所以不能直接转换。
	 * 我们可以把readValue()的第二个参数传递为Student[].class.然后使用Arrays
	 * .asList();方法把得到的数组转换为特定类型的List。 (3)转换为Map：readValue(json,Map.class)
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们使用泛型，得到的也是泛型
	 *
	 * @param content
	 *            要转换的JavaBean类型
	 * @param valueType
	 *            原始json字符串数据
	 * @return JavaBean对象
	 */
	public static <T> T readValue(String content, Class<T> valueType) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.readValue(content, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 把JavaBean转换为json字符串 (1)普通对象转换：toJson(Model) (2)List转换：toJson(List)
	 * (3)Map转换:toJson(Map) 无论什么类型，都可以直接传入这个方法
	 *
	 * @param object
	 *            JavaBean对象
	 * @return json字符串
	 */
	public static String toJSon(Object object) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static List<Map<String, Object>> stringToObject(List<Map<String, String>> s) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, String> map : s) {
			Map<String, Object> obj = new HashMap<String, Object>(map);
			list.add(obj);
		}
		return list;

	}
}