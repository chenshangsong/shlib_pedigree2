package cn.sh.library.pedigree.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.houbb.opencc4j.util.ZhConverterUtil;

import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author chenss
 * 
 */
public class StringUtilC {
	public static void main(String[] args) {
//		String traditional = "本谱着录了湖北、湖南、广东、广西、海南、重庆、四川、贵州、云南、西藏、港澳等地蔚姓概况。参见《蔚姓广谱》甘肃、山东等卷（馆藏XP4961-4968）";
//		String simplified = getCht(traditional);
//		System.out.println(simplified); // 输出：繁体中文
		restartService();

	}

	public static String str2Uri(String str) {
		if (!StringUtil.isEmpty(str)) {
			return "<" + str + ">";
		}
		return null;

	}

	public static String str2Rdf(String str) {
		if (!StringUtil.isEmpty(str)) {
			return "'" + StringUtilC.str2Unicode(str) + "'";
		}
		return null;

	}
	public  static String restartService()  {
        String command = CodeMsgUtil.getConfig("conf_restart_Service");
        if (!StringUtilC.isEmpty(command)){
        	 ProcessBuilder processBuilder = new ProcessBuilder(command);
             
             try {
                 // 启动进程并将其设置为后台执行
                 processBuilder.inheritIO(); // 继承IO，输出到控制台
                 processBuilder.start();
                 
                 // 不等待进程结束，直接返回
                 return "Service restart initiated successfully.";
             } catch (IOException e) {
                 e.printStackTrace();
                 return "An error occurred: " + e.getMessage();
             }
        }
        return "未配置执行脚本。";
    }
	/**
	 * 替换时光科技有限公司
	 * 
	 * @param dataMap
	 */
	public static void ProcessShiGuangMap(Map<String, Object> dataMap) {
		String needReplaceStr="江苏时光信息科技有限公司";
		String replaceStr="第三方社会团体";
		// 检查map中是否存在itemList节点并且该节点不为空
		if (dataMap != null && dataMap.containsKey("itemList") && dataMap.get("itemList") instanceof List) {
			List<Map<String, Object>> itemList = (List<Map<String, Object>>) dataMap.get("itemList");
			if (!itemList.isEmpty()) {
				// 遍历itemList中的每个item
				for (Map<String, Object> item : itemList) {
					// 检查orgShort和orgFull字段是否包含特定字符串
					if (item.containsKey("orgShort") && item.get("orgShort") != null
							&& item.get("orgShort").toString().contains(needReplaceStr)) {
						item.put("orgShort", replaceStr);
						// 将address字段替换为空字符串
						if (item.containsKey("address")) {
							item.put("address", "");
						}
					}
					if (item.containsKey("orgFull") && item.get("orgFull") != null
							&& item.get("orgFull").toString().contains(needReplaceStr)) {
						item.put("orgFull", replaceStr);
						// 将address字段替换为空字符串
						if (item.containsKey("address")) {
							item.put("address", "");
						}
					}

				}
			}
		}

		if (dataMap != null && dataMap.containsKey("note") && dataMap.get("note").toString().contains(needReplaceStr)) {
			dataMap.put("note", dataMap.get("note").toString().replaceAll(needReplaceStr, replaceStr));
		}

	}

	/**
	 * 将字符串转成unicode
	 * 
	 * @param str 待转字符串
	 * @return unicode字符串
	 */
	public static String str2Unicode(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2Str(String unicode) {

		StringBuffer string = new StringBuffer();

		String[] hex = unicode.split("\\\\u");

		for (int i = 1; i < hex.length; i++) {

			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);

			// 追加成string
			string.append((char) data);
		}

		return string.toString();
	}

	/*
	 * 空节点主语使用 家谱编目新增 chenss 20200616
	 */
	public static String getRandomForBlankNode() {
		// 生成6位随机数
//		String _num = String.valueOf((int)((Math.random()*9+1)*100000));
		// 生成6位随机数
		String _num = getRandomUriValue(16);
		return "nodeID://b".concat(_num);
	}

	public static String getSqlEscape(String oStr) {
		String a = "";
		for (char c : oStr.toCharArray()) {
			a = a + "$" + c;
		}
		return a;
	}

	public static String getNullEmpty(String oStr) {
		if ("null".equals(oStr) || isEmpty(oStr)) {
			return null;
		}
		return oStr;
	}

	// 子字符串modelStr在字符串str中第count次出现时的下标
	public static int getFromIndex(String str, String modelStr, Integer count) {
		// 对子字符串进行匹配
		Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);
		int index = 0;
		// matcher.find();尝试查找与该模式匹配的输入序列的下一个子序列
		while (slashMatcher.find()) {
			index++;
			// 当modelStr字符第count次出现的位置
			if (index == count) {
				break;
			}
		}
		// matcher.start();返回以前匹配的初始索引。
		return slashMatcher.start();
	}

	/**
	 * 繁转简体
	 * 
	 * @param strCht
	 * @return
	 */
	public static String getChs(String strCht) {
		if (isEmpty(strCht)) {
			return "";
		}
//		return ZHConverter.getInstance(ZHConverter.SIMPLIFIED).convert(strCht); 更换简繁转换插件 为 opencc1.8 20240802 

		// 如果字符串中包含繁体 则进行转换
		if (ZhConverterUtil.containsTraditional(strCht)) {
			return ZhConverterUtil.toSimple(strCht);
		}
		return strCht;

	}

	public static String getMeta2Rdf(String strCht) {
		if (isEmpty(strCht)) {
			return "";
		}
//		return "'" + StringUtilC.str2Unicode(ZHConverter.getInstance(ZHConverter.SIMPLIFIED).convert(strCht)) + "'";
		return "'" + StringUtilC.str2Unicode(strCht) + "'";
	}

	public static String getChs2Rdf(String strCht) {
		if (isEmpty(strCht)) {
			return "";
		}
//		return "'" + StringUtilC.str2Unicode(ZHConverter.getInstance(ZHConverter.SIMPLIFIED).convert(strCht)) + "'";
		return "'" + StringUtilC.str2Unicode(ZhConverterUtil.toSimple(strCht)) + "'";
	}

	/**
	 * 简转繁体
	 * 
	 * @param strCht
	 * @return
	 */
	public static String getCht(String strChs) {
		if (isEmpty(strChs)) {
			return "";
		}

//		return ZHConverter.getInstance(ZHConverter.TRADITIONAL).convert(strChs);更换简繁转换插件 为 opencc1.8 20240802 
		// 如果字符串中包含简体 则进行转换
		if (ZhConverterUtil.containsSimple(strChs)) {
			return ZhConverterUtil.toTraditional(strChs);
		}
		return strChs;

	}

	public static String getCht2Rdf(String strChs) {
		if (isEmpty(strChs)) {
			return "";
		}

//		return "'" + StringUtilC.str2Unicode(ZHConverter.getInstance(ZHConverter.TRADITIONAL).convert(strChs)) + "'";
		return "'" + StringUtilC.str2Unicode(ZhConverterUtil.toTraditional(strChs)) + "'";

	}

	/**
	 * 删除list空项
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeEmptyList(List list) {
		List list1 = new ArrayList();
		if (list == null || list.size() <= 0)
			return null;
		// 循环第一层
		for (int i = 0; i < list.size(); i++) {
			// 进入每一个list
			List listi = (List) list.get(i);
			if (listi != null && listi.size() > 0)
				list1.add(listi);
		}

		return list1;
	}

	/**
	 * 导出文件名称中文防止乱码
	 * 
	 * @param oStr
	 * @return
	 */
	public static String encodeUtf8(String oStr) {
		try {
			return URLEncoder.encode(oStr, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean hasIntersection(Object from1, Object to1, Object from2, Object to2) {
		String dblFrom1 = getString(from1);
		String dblTo1 = getString(to1);
		String dblFrom2 = getString(from2);
		String dblTo2 = getString(to2);
		if ((dblFrom1.compareTo(dblFrom2) >= 0 && dblFrom1.compareTo(dblTo2) <= 0)
				|| (dblTo1.compareTo(dblFrom2) >= 0 && dblTo1.compareTo(dblTo2) <= 0)
				|| (dblFrom1.compareTo(dblFrom2) < 0 && dblTo2.compareTo(dblTo2) > 0)) {
			return true;
		}
		return false;
	}

	public static String getRandom(int max, int min) {
		Random random = new Random();
		return String.valueOf(random.nextInt(max) % (max - min + 1) + min);
	}

	public static String leftPad(int len, String str) {
		return leftPad(len, str, "0");
	}

	public static String leftPad(int len, String str, String padStr) {
		while (str.length() < len) {
			str = padStr + str;
		}
		return str;
	}

	public static String getDateFormat(String str) {
		if (str == null) {
			return "";
		}

		if (str.length() > 19) {
			return str.substring(0, 10);
		}

		if (str.length() != 8 || str.length() != 14) {
			return str;
		}
		return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
	}

	public static String getDateTimeFormat(String str) {
		if (str == null) {
			return "";
		}
		if (str.length() != 14) {
			return str;
		}
		return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10)
				+ ":" + str.substring(10, 12);
	}

	public static String indexToStrNoOne(int i) {
		if (i <= 1) {
			return "";
		} else {
			return String.valueOf(i);
		}
	}

	public static String getString(Object obj) {
		if (obj == null) {
			return "";
		} else {

			return obj.toString().trim();
		}
	}

	/**
	 * 取得int数据
	 * 
	 * @param text
	 * @return
	 */
	public static Integer getInt(final String text) {
		if (text == null) {
			return 0;
		} else {
			String str = text.trim().replace("+", "");
			if (text.trim().equals("-0")) {
				str = "0";
			}
			try {
				return (int) Double.parseDouble(str);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	/**
	 * 取得double数据
	 * 
	 * @param text
	 * @return
	 */
	public static Double getDouble(final String text) {
		if (text == null) {
			return 0.0;
		} else {
			String str = text.trim().replace("+", "");
			if (text.trim().equals("-0")) {
				str = "0";
			}
			try {
				return Double.parseDouble(str);
			} catch (Exception e) {
				return 0.0;
			}
		}
	}

	/**
	 * 取得double数据
	 * 
	 * @param text
	 * @return
	 */
	public static Double getDouble(final Object text) {
		if (text == null) {
			return 0.0;
		}
		try {
			return Double.parseDouble(text.toString());
		} catch (Exception e) {
			return 0.0;

		}
	}

	/**
	 * 四十五人，保留小数
	 * 
	 * @param value
	 * @param pointLength
	 * @return
	 */
	public static double Round(double value, Integer pointLength) {
		BigDecimal bg = new BigDecimal(value);
		return bg.setScale(pointLength, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * @param text  ：传入数字字符串
	 * @param scale ：保留的小数位数
	 * @param round ：取小数精度的方式
	 * @return
	 */
	public static BigDecimal getBigDecimal(String text, int scale, int round) {
		if (text == null) {
			return new BigDecimal("0");
		} else {
			try {
				return new BigDecimal(text.trim()).setScale(scale, round);
			} catch (Exception e) {
				return new BigDecimal("0");
			}
		}
	}

	/**
	 * 重载BigDecimal方法，默认保留2位小数
	 * 
	 * @param text
	 * @return
	 */
	public static BigDecimal getBigDecimal(String text) {
		if (text == null) {
			return new BigDecimal("0");
		} else {
			try {
				return new BigDecimal(text.trim()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			} catch (Exception e) {
				return new BigDecimal("0");
			}
		}
	}

	public static long getLong(String text) {
		if (text == null) {
			return 0;
		} else {
			try {
				return Long.parseLong(text);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	/**
	 * 取得Integer数据
	 * 
	 * @param text
	 * @return
	 */
	public static Integer getInteger(final String text) {
		if (text == null) {
			return null;
		} else {
			String str = text.trim().replace("+", "");
			if (text.trim().equals("-0")) {
				str = "0";
			}
			try {
				return (int) Double.parseDouble(str);
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * 取得Short数据
	 * 
	 * @param text
	 * @return
	 */
	public static Short getShort(final Object text) {
		if (text == null) {
			return null;
		}
		try {
			return Short.valueOf(StringUtilC.getString(text));
		} catch (Exception e) {
			return null;
		}
	}

	public static String getStringWithNull(Object obj) {
		if (obj == null) {
			return null;
		} else {
			return obj.toString();
		}
	}

	public static String substring(Object obj, int from, int to) {
		try {
			String str = getString(obj);
			return str.substring(from, to);
		} catch (Exception e) {
			return "";
		}

	}

	public static String format(String format, Object obj) {
		return String.format(format, getString(obj));
		// String.format("%03d",
		// (Integer.parseInt(bangou.get("SINKO_SUB_NO").toString()) +1));
	}

	public static String leftPad(int len, Object obj) {
		return leftPad(len, "0", obj);
	}

	public static String leftPad(int len, String padStr, Object obj) {
		String oStr = getString(obj);
		StringBuilder str = new StringBuilder("");
		int iPad = len - getString(obj).length();
		if (iPad > 0) {
			for (int i = 0; i < iPad; i++) {
				str.append(padStr);
			}
			str.append(oStr);
			return str.toString();
		}
		return oStr;
	}

	public static boolean isEmpty(Object obj) {
		return getString(obj).isEmpty();
	}

	public static boolean hasEmpty(Object... objs) {
		for (Object obj : objs) {
			if (getString(obj).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsKey(Map<String, String> map, Object obj) {
		if (map.containsKey(obj)) {
			if (!StringUtilC.isEmpty(map.get(obj))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 去除%字符，防止恶意查询所有数据
	public static Object parasF(Object obj) {
		if (!StringUtilC.isEmpty(obj)) {
			if (obj.toString().contains("%")) {
				return (Object) obj.toString().replace("%", "012@345#6&789@0" + DateUtilC.getNowTime());
			}
			return obj;
		}
		return obj;
	}

	public static boolean mapIsNull(Map<String, String> map) {
		if (map == null) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isInteger(Object value) {
		try {
			Integer.parseInt(getString(value));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isDouble(Object value) {
		try {
			Double.parseDouble(getString(value));
			if (getString(value).contains(".")) {
				return true;
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isNumber(Object value) {
		return isInteger(value) || isDouble(value);
	}

	public static Integer getInteger(Object obj) {
		if (obj == null) {
			return null;
		} else if (isInteger(obj)) {
			return Integer.parseInt(getString(obj));
		} else {
			return null;
		}
	}

	/**
	 * 获取金额逗号分隔
	 * 
	 * @param str 金额
	 * @return
	 */
	public static String getAmount(String str) {
		if (StringUtilC.isEmpty(str)) {
			return "";
		} else {
			Double dbl = StringUtilC.getDouble(str);
			if (dbl == 0) {
				return "0.00";
			} else {
				NumberFormat formater = new DecimalFormat("###,###.00");
				return formater.format(dbl);
			}
		}
	}

	/**
	 * 
	 * @param outputStream
	 * @return
	 */
	public static String StreamToString(OutputStream outputStream) {
		ByteArrayOutputStream baos = (ByteArrayOutputStream) (outputStream);
		String str = null;
		try {
			str = baos.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * string To byte
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] strToByteArray(String str) {
		if (str == null) {
			return null;
		}
		byte[] byteArray = str.getBytes();
		return byteArray;
	}

	/**
	 * 获取金额逗号分隔
	 * 
	 * @param str 金额
	 * @return
	 */
	public static String getAmount(double dbStr) {
		if (StringUtilC.isEmpty(dbStr)) {
			return "";
		} else {
			if (dbStr == 0) {
				return "0.00";
			} else {
				NumberFormat formater = new DecimalFormat("###,###.00");
				return formater.format(dbStr);
			}
		}
	}

	/**
	 * 获取金额逗号分隔
	 * 
	 * @param str 金额
	 * @param fmt 格式
	 * @return
	 */
	public static String getAmount(String str, String fmt) {
		if (StringUtilC.isEmpty(str)) {
			return "";
		}
		NumberFormat formater = new DecimalFormat(fmt);
		return formater.format(Double.parseDouble(str));
	}

	public static String getExceptionString(Exception ex) {
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		return writer.getBuffer().toString();
	}

	/**
	 * 不足位前面加0
	 * 
	 * @param str
	 * @param lenght 长度
	 * @returns
	 */
	public static String padLeftZero(Object obj, int lenght) {
		// 获取字符
		String str = StringUtilC.getString(obj);
		while (str.length() < lenght) {
			str = '0' + str;
		}
		return str;
	}

	/**
	 * 判断是否是整数
	 * 
	 * @param numberString
	 * @return
	 */
	public static boolean isNumber(String numberString) {
		String reg = "^-?\\d+$";
		return Pattern.compile(reg).matcher(numberString).find();
	}

	// 过滤特殊字符
	public static String StringFilter(Object str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		if (isEmpty(str)) {
			return "";
		}
		String mUri = str.toString();
		// 如果是URI格式，直接返回，不过滤
		if (mUri.contains("http://")) {
			return getString(str);
		}
		String regEx = "[`~!@#$%^&*()+|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？=]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(mUri);
		return m.replaceAll("").trim();
	}

	public static Object convertModel(Object bean) throws IllegalArgumentException, IllegalAccessException {
		/*
		 * 得到类中的所有属性集合
		 */
		Field[] fs = bean.getClass().getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (f.getName().equals("serialVersionUID"))
				continue;
			if (f.getType() == String.class) {
				f.set(bean, StringUtilC.StringFilter(f.get(bean)));
			} // 给属性设值
		}
		return bean;
	}

	/**
	 * 判断一某实体类中，所以属性是否为空
	 * 
	 * @param bean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static boolean isFieldNull(Object bean) {
		boolean ret = true;
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (!StringUtilC.isEmpty(field.get(bean))) {
					ret = false;
					break;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 得到URI的16位值
	 * 
	 * @param pwd_len
	 * @return
	 */
	public static String getRandomUriValue(int pwd_len) {
		// 34是因为数组是从1开始的，26个字母+9个 数字
		final int maxNum = 35;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止 生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

}
