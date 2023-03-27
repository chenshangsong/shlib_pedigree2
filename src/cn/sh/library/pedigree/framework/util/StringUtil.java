package  cn.sh.library.pedigree.framework.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * クラス名 : StringUtilC <br>
 * 機能概要 : </br> コピーライト: Copyright © 2011 NC Corporation, All Rights
 * Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class StringUtil {
	public static String getSqlEscape(String oStr) {
		String a = "";
		for (char c : oStr.toCharArray()) {
			a = a + "$" + c;
		}
		return a;
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
			return obj.toString();
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
	 * 
	 * @param text：传入数字字符串
	 * @param scale：保留的小数位数
	 * @param round：取小数精度的方式
	 * @return
	 */
	public static BigDecimal getBigDecimal(String text, int scale, int round) {
		if (text == null) {
			return new BigDecimal("0");
		} else {
			try {
				return new BigDecimal(text.trim()).setScale(scale,round);
			} catch (Exception e) {
				return new BigDecimal("0");
			}
		}
	}
	/**
	 * 重载BigDecimal方法，默认保留2位小数
	 * @param text
	 * @return
	 */
	public static BigDecimal getBigDecimal(String text) {
		if (text == null) {
			return new BigDecimal("0");
		} else {
			try {
				return new BigDecimal(text.trim()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			} catch (Exception e) {
				return new BigDecimal("0");
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
	 * @param str
	 *            金额
	 * @return
	 */
	public static String getAmount(String str) {
		if (StringUtil.isEmpty(str)) {
			return "";
		} else {
			Double dbl = StringUtil.getDouble(str);
			if (dbl == 0) {
				return "0.00";
			} else {
				NumberFormat formater = new DecimalFormat("###,###.00");
				return formater.format(dbl);
			}
		}
	}

	/**
	 * 获取金额逗号分隔
	 * 
	 * @param str
	 *            金额
	 * @param fmt
	 *            格式
	 * @return
	 */
	public static String getAmount(String str, String fmt) {
		if (StringUtil.isEmpty(str)) {
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
	 * @param lenght
	 *            长度
	 * @returns
	 */
	public static String padLeftZero(Object obj, int lenght) {
		// 获取字符
		String str = StringUtil.getString(obj);
		while (str.length() < lenght) {
			str = '0' + str;
		}
		return str;
	}
}
