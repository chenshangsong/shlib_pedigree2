package cn.sh.library.pedigree.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期操作公用类
 * 
 * @author chenss
 * 
 */
public class DateUtilC {

	public static String getMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		String firstday = new SimpleDateFormat("yyyy-MM-dd")
				.format(f.getTime());
		return firstday;
	}

	/**
	 * 获取某月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		String firstday = new SimpleDateFormat("yyyy-MM-dd")
				.format(f.getTime());
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

	/**
	 * 获取某月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar l = (Calendar) cal.clone();
		l.clear();
		l.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		l.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		l.set(Calendar.MILLISECOND, -1);
		String lastday = new SimpleDateFormat("yyyy-MM-dd").format(l.getTime());
		return lastday;
	}

	public static boolean isDate(String pDateObj, boolean canEmpty) {
		if (canEmpty && StringUtilC.isEmpty(pDateObj)) {
			return true;
		} else {
			return isDate(pDateObj);
		}
	}

	public static boolean isDate(String pDateObj) {
		boolean ret = true;
		if (pDateObj == null
				|| pDateObj.replace("-", "").replace("/", "").length() != 8) {
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

	/**
	 * 年月日格式
	 * 
	 * @return
	 */
	public static String getNowDateC() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return dateFormat.format(cal.getTime());
	}

	public static String getNextMonthNowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nextMonth = dateFormat.format(cal.getTime());
		return nextMonth;
	}

	static Calendar systemCalendar = new GregorianCalendar();

	/**
	 * 获取日期的年份
	 */
	public static int getYear(Date date) {
		systemCalendar.setTimeInMillis(date.getTime());
		return systemCalendar.get(Calendar.YEAR);
	}

	/**
	 * 获取日期的月份
	 */
	public static int getMonth(Date date) {
		systemCalendar.setTimeInMillis(date.getTime());
		return systemCalendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期型的日期
	 */
	public static int getDay(Date date) {
		systemCalendar.setTimeInMillis(date.getTime());
		return systemCalendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 时间往后推n天
	 * 
	 * @param dd
	 * @param n
	 *            天数，正后n天，负前n天
	 * @return
	 */
	public static Date addDay(Date dd, int n) {
		// if(time>0) time*=-1;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dd);
		calendar.add(Calendar.DAY_OF_WEEK, n); // 前三天
		return calendar.getTime();
	}

	/**
	 * 时间往后推n天，可为小数，折算为秒
	 * 
	 * @param dd
	 * @param n
	 *            天数，正后n秒，负前n秒
	 * @return
	 */
	public static Date addDay(Date dd, double ds) {
		// if(time>0) time*=-1;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dd);
		int second = (int) (ds * 24 * 3600);// 把天打算为秒
		calendar.add(Calendar.SECOND, second); // 前三天
		return calendar.getTime();
	}

	/**
	 * 两个日期相差多少天
	 * 
	 * @param dd
	 * @param dd2
	 * @return 秒
	 */
	public static int dateDiff(Date dd, Date dd2) {
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		calst.setTime(dd);
		caled.setTime(dd2);
		// 设置时间为0时
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
				.getTime().getTime() / 1000)) / 3600 / 24;

		return days == 0 ? 1 : Math.abs(days);
	}

	/**
	 * 得到某个日期的下几天的日期
	 * 
	 * @param _dateDay
	 * @param days
	 * @return
	 */
	public static String getNextDayWhenDate(Date _dateDay, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_dateDay);
		cal.add(Calendar.DATE, days);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nextMonth = dateFormat.format(cal.getTime());
		return nextMonth;
	}

	/**
	 * 得到某个日期的下几个月的日期
	 * 
	 * @param _dateDay
	 * @param months
	 * @return
	 */
	public static String getNextMonthWhenDate(Date _dateDay, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_dateDay);
		cal.add(Calendar.MONTH, months);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nextMonth = dateFormat.format(cal.getTime());
		return nextMonth;
	}

	/**
	 * 得到某个日期的下几个月的日期的前一天
	 * 
	 * @param _dateDay
	 * @param months
	 * @return
	 */
	public static String getBeforeNextMonthWhenDate(Date _date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);
		cal.add(Calendar.MONTH, months);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nextMonth = dateFormat.format(cal.getTime());
		Date beforedays = getBeforeDate(String2Date(nextMonth));
		return getDate(beforedays);
	}

	/**
	 * 根据某个日期得到前一天日期
	 * 
	 * @param d
	 * @return
	 */
	public static Date getBeforeDate(Date d) {
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 得到某个日期的下几年的日期
	 * 
	 * @param _dateDay
	 * @param years
	 * @return
	 */
	public static String getNextYearWhenDate(Date _dateDay, int years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_dateDay);
		cal.add(Calendar.YEAR, years);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nextMonth = dateFormat.format(cal.getTime());
		return nextMonth;
	}

	/**
	 * 计算两个日期之间月数差
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMonthSpace(Date date1, Date date2) {
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return result == 0 ? 1 : Math.abs(result);
	}

	public static String getNowDateString() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(cal.getTime());
	}

	public static String getNowTime() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(cal.getTime());
	}

	public static Date getNowDateTime() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String2Datetime(dateFormat.format(cal.getTime()));
	}

	public static Date getNowDateD() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return String2Datetime(dateFormat.format(cal.getTime()));
	}

	public static String getBeforeNowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
		return yestedayDate;
	}

	/**
	 * 获取某个日期的前一天
	 * 
	 * @param date1
	 * @return
	 */
	public static String getBeforeWhenDate(String date1) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(String2Datetime(date1));
		cal.add(Calendar.DATE, -1);
		String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
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

	/*
	 * 获取XX年XX月格式
	 */
	public static String getDateYM(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");
		return dateFormat.format(date);
	}

	public static String getDate(Date date) {
		if (StringUtilC.isEmpty(date)) {
			return "";
		}
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
		if (cal.get(Calendar.YEAR) == year
				&& cal.get(Calendar.MONTH) == (month - 1)
				&& cal.get(Calendar.DAY_OF_MONTH) == day) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 把字符串转换为日期型
	 */
	public static Date String2Date(String s) {
		if (s == null || "".equals(s.trim()))
			return null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setLenient(true);
			java.util.Date date = df.parse(s);
			if (null == date)
				throw new Exception(s + " 不是有效有日期型字符串!");
			else
				return date;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 把字符串转换为日期型
	 */
	public static Date DateTime2Date(Date s) {
		if (s == null || "".equals(s))
			return null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setLenient(true);
			java.util.Date date = df.parse(s.toString());
			if (null == date)
				throw new Exception(s + " 不是有效有日期型字符串!");
			else
				return date;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 把字符串转换为日期型
	 */
	public static Date String2Datetime(String s) {
		if (s == null || "".equals(s.trim()))
			return null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormat.setLenient(true);
			java.util.Date date = dateFormat.parse(s);
			if (null == date)
				throw new Exception(s + " 不是有效有日期型字符串!");
			else
				return date;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据日期判断该日期属于第几季度
	 * 
	 * @param month
	 * @return
	 */
	public static Integer getQuarterByMonth(Date strdate) {
		Integer curQuarter;
		Integer month = DateUtilC.getMonth(strdate);
		switch (month) {
		case 1:
		case 2:
		case 3:
			curQuarter = 1;
			break;
		case 4:
		case 5:
		case 6:
			curQuarter = 2;
			break;
		case 7:
		case 8:
		case 9:
			curQuarter = 3;
			break;
		case 10:
		case 11:
		case 12:
			curQuarter = 4;
			break;
		default:
			curQuarter = 1;
			break;
		}
		return curQuarter;
	}

	/**
	 * 根据一个日期得到本季度最后一个月
	 * 
	 * @param month
	 * @return
	 */
	public static Integer getQuarterLastMonth(Date strdate) {
		// 得到季度
		Integer curQuarter = getQuarterByMonth(strdate);
		switch (curQuarter) {
		case 1:
			curQuarter = 3;
			break;
		case 2:
			curQuarter = 6;
			break;
		case 3:
			curQuarter = 9;
			break;
		case 4:
			curQuarter = 12;
			break;
		default:
			curQuarter = 3;
			break;
		}
		return curQuarter;
	}

	/**
	 * 根据一个日期得到本季度开始月
	 * 
	 * @param month
	 * @return
	 */
	public static Integer getQuarterFirtMonth(Date strdate) {
		// 得到季度
		Integer curQuarter = getQuarterByMonth(strdate);
		switch (curQuarter) {
		case 1:
			curQuarter = 1;
			break;
		case 2:
			curQuarter = 4;
			break;
		case 3:
			curQuarter = 7;
			break;
		case 4:
			curQuarter = 10;
			break;
		default:
			curQuarter = 1;
			break;
		}
		return curQuarter;
	}

	/**
	 * 根据一个日期查询所在季度的开始月，截止月
	 * 
	 * @param month
	 * @return
	 */
	public static Integer[] getQuarterFirstAndLastMonth(Date strDate) {
		Integer[] _strings = new Integer[2];
		_strings[0] = getQuarterFirtMonth(strDate);// 该日期所在季度开始月份
		_strings[1] = getQuarterLastMonth(strDate);// 该日期所在季度结束月份
		return _strings;
	}
}
