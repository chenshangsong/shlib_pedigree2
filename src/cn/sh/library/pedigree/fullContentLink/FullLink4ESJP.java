package cn.sh.library.pedigree.fullContentLink;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.FullLink4ESJPSearchBean;
import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;
import cn.sh.library.pedigree.framework.util.HttpUtil;
import cn.sh.library.pedigree.utils.StringUtilC;

public class FullLink4ESJP {

	/**
	 * 获取全文链接图标链接(总方法)
	 * 
	 * @author CM
	 * @return String
	 */
	public static Map<String, String> GetFullTextImg(FullLink4ESJPSearchBean fullLink4ESJPSearchBean) {
		Map<String, String> _map = new HashMap<String, String>();
		if (fullLink4ESJPSearchBean.isFlag()) { // 是否为多个DOI, true为单个
			String link = "";
			if (fullLink4ESJPSearchBean.getDoi().startsWith("STJP")
					&& fullLink4ESJPSearchBean.getDoi().length() >= 10) {
				// 外网访问地址
				if (fullLink4ESJPSearchBean.getAccessLevelFlg().equals("0")
						&& "true".equals(fullLink4ESJPSearchBean.getHasimg())) {
					// 外网放开 chenss 20181109
					link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
					link = MessageFormat.format(link, "jiapu", fullLink4ESJPSearchBean.getDoi().substring(0, 10));
					link = GetOutFullLinkTemp(link);

				} else if (fullLink4ESJPSearchBean.getAccessLevelFlg().equals("1")
						&& "true".equals(fullLink4ESJPSearchBean.getHasimg())) {

					link = GetFulltext(fullLink4ESJPSearchBean.getDoi().substring(0, 10),
							fullLink4ESJPSearchBean.getIpAddress());
				}
			}
			// link IIIF
			_map.put("link", link);

			_map.put("linkPDF",
					GetFullTextImg_PDF(fullLink4ESJPSearchBean.getAccessLevelFlg(),
							fullLink4ESJPSearchBean.getDoi().substring(0, 10), fullLink4ESJPSearchBean.getHasimg(),
							fullLink4ESJPSearchBean.getIpAddress()));
		} else { // 是否为多个DOI, false为多个
			String tempV = "";
			if (fullLink4ESJPSearchBean.getDois().contains(";")) {
				String[] dois = fullLink4ESJPSearchBean.getDois().split(";");
				tempV = dois[fullLink4ESJPSearchBean.getI()];
			} else {
				tempV = fullLink4ESJPSearchBean.getDois();
			}
			// 如果不是胶卷并且不为空
			if (!"9".equals(fullLink4ESJPSearchBean.getAccessLevelFlg())
					&& !StringUtilC.isEmpty(fullLink4ESJPSearchBean.getAccessLevelFlg())) {
				if (tempV.startsWith("STJP") && tempV.length() >= 10) {
					String link = "";
					// 外网访问地址
					if (fullLink4ESJPSearchBean.getAccessLevelFlg().equals("0")
							&& "true".equals(fullLink4ESJPSearchBean.getHasimg())) {
						// 外网放开 chenss 20181109
						link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
						link = MessageFormat.format(link, "jiapu", tempV.substring(0, 10));
						link = GetOutFullLinkTemp(link);

					} else if (fullLink4ESJPSearchBean.getAccessLevelFlg().equals("1")
							&& "true".equals(fullLink4ESJPSearchBean.getHasimg())) {

						link = GetFulltext(tempV.substring(0, 10), fullLink4ESJPSearchBean.getIpAddress());
					}
					_map.put("linkPDF",
							GetFullTextImg_PDF(fullLink4ESJPSearchBean.getAccessLevelFlg(), tempV.substring(0, 10),
									fullLink4ESJPSearchBean.getHasimg(), fullLink4ESJPSearchBean.getIpAddress()));

					String value = fullLink4ESJPSearchBean.getValue();
					value += "DOI为" + tempV + fullLink4ESJPSearchBean.getTempcallNo() + link + "<br>";
					fullLink4ESJPSearchBean.setValue(value);
				} else {
					String value = fullLink4ESJPSearchBean.getValue();
					value += tempV + "<br>";
					fullLink4ESJPSearchBean.setValue(value);
				}
			}
			// link IIIF
			_map.put("link", fullLink4ESJPSearchBean.getValue());

		}
		return _map;
	}

	/**
	 * 获取全文链接图标链接1
	 */
	public static String GetFullTextImg(String accessLevelFlg, String[] dois, String value, int i, String tempcallNo,
			String hasimg) {
		String ipaddress = StringUtilC
				.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"));
		String tempV = dois[i];
		// 如果不是胶卷并且不为空
		if (!"9".equals(accessLevelFlg) && !StringUtilC.isEmpty(accessLevelFlg)) {
			if (tempV.startsWith("STJP") && tempV.length() >= 10) {
				String link = "";
				// 外网访问地址
				if (accessLevelFlg.equals("0") && "true".equals(hasimg)) {
					// 外网放开 chenss 20181109
					link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}";
					link = MessageFormat.format(link, "jiapu", tempV.substring(0, 10));
					link = GetOutFullLinkTemp(link);
				} else if (accessLevelFlg.equals("1") && "true".equals(hasimg)) {
					link = GetFulltext(tempV.substring(0, 10), ipaddress);
				}
				value += "DOI为" + tempV + tempcallNo + link + "<br>";
			} else {
				value += tempV + "<br>";
			}
		}
		return value;
	}

	/**
	 * 获取全文链接图标链接1 chenss 20191205
	 */
	public static String GetFullTextImg4Detail(String accessLevel, String doi, String hasimg) {
		String ipaddress = StringUtilC
				.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"));
		String link = "";
		// 如果不是胶卷并且不为空
		if (!"9".equals(accessLevel) && !StringUtilC.isEmpty(accessLevel)) {
			if (doi.startsWith("STJP") && doi.length() >= 10) {
				// 外网访问地址
				if ("0".equals(accessLevel) && "true".equals(hasimg)) {
					// 外网放开 chenss 20181109
					link = MessageFormat.format("https://jpv1.library.sh.cn/jp/full-img/{0}/{1}", "jiapu",
							doi.substring(0, 10));
					link = GetOutFullLinkTemp(link);
				} else if ("1".equals(accessLevel) && "true".equals(hasimg)) {// 内外地址
					link = GetFulltext(doi.substring(0, 10), ipaddress);
				}
			}
		}
		return link;
	}

	/**
	 * 获取全文链接图标链接2PDF chenss 2023 0215 pdf全文链接升级。
	 */
	public static String GetFullTextImg_PDF(String accessLevelFlg, String doi, String hasimg, String ipAddress) {
		String link = "";
		if (doi.startsWith("STJP") && doi.length() >= 10) {
			// 外网访问地址
			if (accessLevelFlg.equals("0") && "true".equals(hasimg)) {
				link = GetOutFullLink_PDF(doi.substring(0, 10));
			} else if (accessLevelFlg.equals("1") && "true".equals(hasimg)) {
				link = GetFulltext_PDF(doi.substring(0, 10), ipAddress);
			}
		}
		return link;
	}

	/**
	 * 获取内网DOI链接(临用)
	 */
	public static String GetOutFullLinkTemp(String link) {
		String DOILink = "<a href='" + link + "' target=_blank ><img border='0' src='" + Constant.ProjectPath
				+ "/res/images/pdf.png' width='20px' height='20px' title='点击查看IIIF全文'></a>";
		return DOILink;
	}

	/**
	 * 新增修改接口, 原"GetFulltext"接口 判断内外网访问, 查看内外网全文图片
	 * 
	 * @author CM
	 */
	public static String GetFulltext(String DOI, String ipAddress) {
		String link = "";
		ipAddress = StringUtilC.isEmpty(ipAddress)
				? StringUtilC.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"))
				: ipAddress;
		if (isInnerIP(ipAddress)) {// 是内网
			link = "https://jpv1.library.sh.cn/jp/full-img/{0}/{1}/{2}";
			// "1"代表内网标识
			link = MessageFormat.format(link, "jiapu", DOI, "1");
			link = GetOutFullLinkTemp(link);
			return link;
		}
		return link;
	}

	/**
	 * 获取外网DOI链接:PDF
	 */
	public static String GetOutFullLink_PDF(String doi) {
//		String DOILink = "<a href='http://search.library.sh.cn/jiapu/PicBrowse.cgi?stjpID=" + doi chenss 20230215 pdf浏览插件升级
//				+ "' target=_blank ><img border='0' src='" + Constant.ProjectPath
//				+ "/res/images/pdf2.png' width='20px' height='20px' title='点击查看全文'></a>";

		String DOILink = "<a href='https://dhapi.library.sh.cn/pdfview?innerflag=0&dbname=jp&doi=" + doi // chenss
																											// 20230215
																											// pdf浏览插件升级
				+ "' target=_blank ><img border='0' src='" + Constant.ProjectPath
				+ "/res/images/pdf2.png' width='20px' height='20px' title='点击查看PDF全文'></a>";

		return DOILink;
	}

	// 参数说明：db="jp"，DOI为家谱文献的DOI，ipaddress=访问者IP 旧方法，读取上图数据库
	public static String GetFulltext_PDF(String DOI, String ipaddress) {
		try {
		SQLDriver sqlManager = new SQLDriver();
		ZyddSQLDriver zyddManager = new ZyddSQLDriver();
		if(sqlManager.conActive == null || zyddManager.conActive == null) {
			return "";
		}
			// String ipaddress = CommonUtils.userIp; chenss 2018-05-30
			// 改为从Session中获取。chenss 2018-05-30
			ipaddress = StringUtilC.isEmpty(ipaddress)
					? StringUtilC.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"))
					: ipaddress;

			String[] result = new String[2];

			String db = "jp";
			sqlManager.OpenStatement();
			sqlManager.ExecuteSQL("select fulltext_webroot from __db where name = '" + db + "'", 0);
			result[0] = sqlManager.FieldByIndex(1) + "?dbname=" + db; // chenss 20230215 pdf浏览插件升级
//				result[0] = "https://dhapi.library.sh.cn/pdfview?innerflag=1&dbname="+db; //chenss 20230215 pdf浏览插件升级
			sqlManager.CloseStatement();

			int cnt = 0;
			String callno_modi = "";

		
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
			zyddManager.Disconnect();
			if (cnt > 0) {
				// 外网开放
				if (!isInnerIP(ipaddress)) {
					result[1] = "<a href=\"javascript:alert(&quot;请到上海图书馆家谱阅览室浏览电子全文&quot;)\"><img img border='0' width='20px' height='20px' src='"
							+ Constant.ProjectPath + "/res/images/pdf2.png' alt='请到上海图书馆家谱阅览室浏览电子全文'  ></a>";

				} else {
					if (cnt > 1) {
						result[0] += "&type=item&idvalue=" + URLEncoder.encode(items);
					} else {
						result[0] += "&type=item&idvalue=" + URLEncoder.encode(items.substring(1));
					}
					result[1] = "<a href='" + result[0] + "' target=_blank><img border='0' src='" + Constant.ProjectPath
							+ "/res/images/pdf2.png' width='20px' height='20px' title='点击查看PDF全文'></a>";
				}
			} else {
				result[1] = "&nbsp;请至上海图书馆家谱阅览室借阅";
			}
			sqlManager.CloseStatement();
			sqlManager.Disconnect();
			return result[1];
		} catch (Exception e) {
			
			return "";
		} 

	}

	/**
	 * 获取客户端用户IP
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
		return ip;
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
		System.out.println(
				"IP：" + ip + ";ipaddress[0]： " + ipaddress[0] + ";ipaddress[1]： " + ipaddress[1] + ";是否内网：" + bInnerIP);
		return bInnerIP;
	}

}
