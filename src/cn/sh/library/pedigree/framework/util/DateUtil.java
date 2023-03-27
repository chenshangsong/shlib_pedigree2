package  cn.sh.library.pedigree.framework.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * クラス名 : DateUtilC <br>
 * 機能概要 : Date操作ツールクラス</br> コピーライト: Copyright © 2011 NC Corporation, All Rights
 * Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class DateUtil {

	public static String getMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		String firstday = new SimpleDateFormat("yyyy-MM-dd").format(f.getTime());
		return firstday;
	}

	public static String getMonthLastDay() {
		Calendar cal = Calendar.getInstance();
		Calendar l = (Calendar) cal.clone();
		l.clear();
		l.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		l.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		l.set(Calendar.MILLISECOND, -1);
		String lastday = new SimpleDateFormat("yyyy-MM-dd").format(l.getTime());
		return lastday;
	}

	public static boolean isDate(String pDateObj, boolean canEmpty) {
		if (canEmpty && StringUtil.isEmpty(pDateObj)) {
			return true;
		} else {
			return isDate(pDateObj);
		}
	}

	public static boolean isDate(String pDateObj) {
		boolean ret = true;
		if (pDateObj == null || pDateObj.replace("-", "").replace("/", "").length() != 8) {
			ret = false;
		}
		try {
			pDateObj = pDateObj.replace("-", "").replace("/", "");
			int year = new Integer(pDateObj.substring(0, 4)).intValue();
			int month = new Integer(pDateObj.substring(4, 6)).intValue();
			int day = new Integer(pDateObj.substring(6)).intValue();
			Calendar cal = Calendar.getInstance();
			cal.setLenient(false); // 允许严格检查日期格式
			cal.set(year, month - 1, day);
			cal.getTime();// 该方法调用就会抛出异常
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	public static String getNowYear() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.YEAR));
	}

	public static String getNowMonth() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.MONTH) + 1);
	}

	public static String getNowDay() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	}

	public static String getNowDate() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(cal.getTime());
	}
	public static String getBeforeNowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1); 
		String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return yestedayDate;
	}
	public static String getKJDateTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
		return dateFormat.format(date);
	}

	public static String getDateTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String getDateTime(Date date, String fmt) {
		DateFormat dateFormat = new SimpleDateFormat(fmt);
		return dateFormat.format(date);
	}

	public static String getKJDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return dateFormat.format(date);
	}

	public static String getDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static String getKJTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH時mm分ss秒");
		return dateFormat.format(date);
	}

	public static String getTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String getKJHM(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH時mm分");
		return dateFormat.format(date);
	}

	public static String getHM(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	public static String getDate(Date date, String format) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		} catch (Exception e) {
			return "";
		}

	}

	public static boolean isDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == (month - 1)
		        && cal.get(Calendar.DAY_OF_MONTH) == day) {
			return true;
		} else {
			return false;
		}
	}

}
