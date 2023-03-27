package cn.sh.library.pedigree.fullContentLink;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;
import cn.sh.library.pedigree.framework.util.HttpUtil;
import cn.sh.library.pedigree.utils.StringUtilC;

public class FullLink {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// new FullLink().isInnerIP("58.1.188.89");
	}

	/**
	 * 获取全文链接图标链接
	 * 
	 * @param accessLevelFlg
	 * @param dois
	 * @param value
	 * @param i
	 * @param tempcallNo
	 * @return
	 */
	public static String GetFullTextImg(String accessLevelFlg, String[] dois, String value, int i, String tempcallNo,
			String hasimg) {
		String tempV = dois[i];
		// 如果不是胶卷并且不为空
		if (!"9".equals(accessLevelFlg) && !StringUtilC.isEmpty(accessLevelFlg)) {
			if (tempV.startsWith("STJP") && tempV.length() >= 10) {
				String link = "";
				// 外网访问地址
				if (accessLevelFlg.equals("0")) {
					if ("true".equals(hasimg)) {
						// 外网放开 chenss 20181109
						link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
						link = MessageFormat.format(link, "jiapu", tempV.substring(0, 10));
						link = FullLink.GetOutFullLinkTemp(link);
					}
					// 临用
					/*
					 * if (tempV.equals("STJP011280")) { link =
					 * "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}"; link =
					 * MessageFormat.format(link, "brvqlrg8y55v1b5q", tempV); link=
					 * FullLink.GetOutFullLinkTemp(link); } else{ link =
					 * FullLink.GetOutFullLink(tempV.substring(0, 10)); }
					 */
				} else if (accessLevelFlg.equals("1")) {
					if ("true".equals(hasimg)) {// 如果内网有全文标记
						// 临用
						if (tempV.equals("STJP011280")) {
							link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
							link = MessageFormat.format(link, "brvqlrg8y55v1b5q", tempV);
							return FullLink.GetOutFullLinkTemp(link);
						}
						link = FullLink.GetFulltext(tempV.substring(0, 10));
					}
				}
				value += "DOI为" + tempV + tempcallNo + link + "<br>";
			} else {
				value += tempV + "<br>";
			}
		}
		return value;
	}

	public static String GetFullTextImg(String accessLevelFlg, String doi) {
		String link = "";
		if (doi.startsWith("STJP") && doi.length() >= 10) {
			// 外网访问地址
			if (accessLevelFlg.equals("0")) {
				// 外网放开 chenss 20181109
				link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
				link = MessageFormat.format(link, "jiapu", doi.substring(0, 10));
				return FullLink.GetOutFullLinkTemp(link);
			} else if (accessLevelFlg.equals("1")) {
				link = FullLink.GetFulltext(doi.substring(0, 10));
			}
		}
		return link;
	}

	/**
	 * 获取内网DOI链接
	 * 
	 * @param doi
	 * @return
	 */
	public static String GetOutFullLink(String doi) {
//		String DOILink = "<a href='http://search.library.sh.cn/jiapu/PicBrowse.cgi?stjpID=" + doi chenss 20230215 pdf浏览插件升级
//		+ "' target=_blank ><img border='0' src='" + Constant.ProjectPath
//		+ "/res/images/pdf2.png' width='20px' height='20px' title='点击查看全文'></a>";

String DOILink = "<a href='https://dhapi.library.sh.cn/pdfview?innerflag=0&dbname=jp&doi=" + doi //chenss 20230215 pdf浏览插件升级
		+ "' target=_blank ><img border='0' src='" + Constant.ProjectPath
		+ "/res/images/pdf2.png' width='20px' height='20px' title='点击查看PDF全文'></a>";
		return DOILink;
	}

	/**
	 * 获取内网DOI链接
	 * 
	 * @param doi
	 * @return
	 */
	public static String GetOutFullLinkTemp(String link) {
		String DOILink = "<a href='" + link + "' target=_blank ><img border='0' src='" + Constant.ProjectPath
				+ "/res/images/pdf.png' width='20px' height='20px' title='点击查看PDF全文'></a>";
		return DOILink;
	}

	// 参数说明：db="jp"，DOI为家谱文献的DOI，ipaddress=访问者IP
	public static String GetFulltext(String DOI) {
		// String ipaddress = CommonUtils.userIp; chenss 2018-05-30
		// 改为从Session中获取。chenss 2018-05-30
		String ipaddress = StringUtilC
				.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"));
		String[] result = new String[2];
		String db = "jp";
		SQLDriver sqlManager = new SQLDriver();
		sqlManager.OpenStatement();
		sqlManager.ExecuteSQL("select fulltext_webroot from __db where name = '" + db + "'", 0);
		result[0] = sqlManager.FieldByIndex(1) + "?dbname=" + db;  //chenss 20230215 pdf浏览插件升级
//		result[0] = "https://dhapi.library.sh.cn/pdfview?innerflag=0&dbname="+db; //chenss 20230215 pdf浏览插件升级
		sqlManager.CloseStatement();

		int cnt = 0;
		String callno_modi = "";

		ZyddSQLDriver zyddManager = new ZyddSQLDriver();
		String items = "";

		sqlManager.OpenStatement();
		sqlManager.ExecuteSQL("select * from " + db + "_meta_item where meta_doi ='" + DOI + "'", 0);
		while (sqlManager.bHasResult) {
			callno_modi = sqlManager.FieldByName("callno_modi");

			zyddManager.OpenStatement();
			zyddManager.ExecuteSQL("select * from " + db + "_item where callno = '" + callno_modi + "'", 0);
			while (zyddManager.bHasResult) {
				cnt++;
				items += ";" + zyddManager.FieldByName("item_id");
				zyddManager.Next();
			}
			zyddManager.CloseStatement();

			sqlManager.Next();
		}
		sqlManager.CloseStatement();
		zyddManager.Disconnect();
		if (cnt > 0) {
			if (!isInnerIP(ipaddress)) {
				// chenss20180130
				// result[1] =
				// "&nbsp;<a href='javascript:alert(&quot;请到上海图书馆家谱阅览室浏览电子全文&quot;)'><img
				// src='"+Constant.ProjectPath+"/res/images/pdf.png' width='20px' height='20px'
				// title='点击查看全文'></a>";
				result[1] = "";

			} else {
				if (cnt > 1) {
					result[0] += "&type=item&idvalue=" + URLEncoder.encode(items);
				} else {
					result[0] += "&type=item&idvalue=" + URLEncoder.encode(items.substring(1));
				}
				result[1] = "<a href='" + result[0] + "' target=_blank><img border='0' src='" + Constant.ProjectPath
						+ "/res/images/pdf.png' width='20px' height='20px' title='点击查看全文'></a>";
			}
		} else {
			result[1] = "&nbsp;请至上海图书馆家谱阅览室借阅";
		}
		sqlManager.Disconnect();

		return result[1];
	}

	private static boolean isInnerIP(String ip) {
		boolean bInnerIP = false;

		// 私有地址的范围:
		// 1.A类地址中:10.0.0.0到10.255.255.255
		// 2.B类地址中:172.16.0.0到172.31.255.255
		// 3.C类地址中:192.168.0.0到192.168.255.255
		// 4.本机地址: 127.0.0.1
		utility ut = new utility();
		String[] ips = ut.SharpString(ip, ".");
		int[] ipaddress = new int[ips.length];
		for (int i = 0; i < ipaddress.length; i++) {
			ipaddress[i] = Integer.parseInt(ips[i]);
		}

		if (ipaddress[0] == 10)
			bInnerIP = true;
		if (ipaddress[0] == 172 && ipaddress[1] >= 16 && ipaddress[1] <= 31)
			bInnerIP = true;
		if (ipaddress[0] == 192 && ipaddress[1] == 168)
			bInnerIP = true;
		if (ip.equals("127.0.0.1"))
			bInnerIP = true;
		System.out.println(bInnerIP);
		return bInnerIP;
	}

	/**
	 * 获取客户端用户IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// System.out.println("用户IP：" + ip);
		return ip;
	}

}
