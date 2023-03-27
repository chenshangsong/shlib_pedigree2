package cn.sh.library.pedigree.utils;

/**
 * Created by Administrator on 14-3-10.
 */
public class Formatter {
    //标准化日期格式
    public static String convert2SD(String date) {
        if (date.contains(".")) {
            date.replaceAll(".","-");
        }

        String[] cells = date.split("-");
        if (1 == cells.length) {
            return date + "-01-01";
        }

        if (2 == cells.length) {
            return date + "-01";
        }

        return date;
    }

    //标准化字符串，去除空格
    public static String trimNullStr(String str) {
        if (str.contains(" ")) {
            return str.replaceAll(" ", "-");
        }

        return str;
    }
}
