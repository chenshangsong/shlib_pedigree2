package cn.sh.library.pedigree.fullContentLink;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.bean.FullLink4ESJPSearchBean;
import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.utils.HttpsUtil;
import cn.sh.library.pedigree.utils.IPUtils;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONObject;

@Component
public class FullLink4ESJP {
	@Autowired
	private RedisUtils redisUtil;

	private static FullLink4ESJP fullLink4ESJP;

	@PostConstruct
	public void init() {
		fullLink4ESJP = this;
		fullLink4ESJP.redisUtil = this.redisUtil;
	}

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
//		String ipaddress = StringUtilC
//		.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"));
//2034-01-08 chess 改为新方法
		String ipaddress = IPUtils.getIpAddr(RequestFilter.threadLocalRequest.get());
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
//		String ipaddress = StringUtilC
//				.getString(HttpUtil.getSessionValue(RequestFilter.threadLocalRequest.get(), "userIp"));
		// 2034-01-08 chess 改为新方法
		String ipaddress = IPUtils.getIpAddr(RequestFilter.threadLocalRequest.get());
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
		String DOILink = "<a href='" + link + "' target=_blank ><img border='0' src='https://jpv1.library.sh.cn/jp/res/images/pdf.png' width='20px' height='20px' title='点击查看IIIF全文'></a>";
		return DOILink;
	}

	/**
	 * 新增修改接口, 原"GetFulltext"接口 判断内外网访问, 查看内外网全文图片
	 * 
	 * @author CM
	 */
	public static String GetFulltext(String DOI, String ipAddress) {
		String link = "";
		if (IPUtils.isPrivateIP(true)) {// 是内网
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

		String DOILink = "<a href='javascript:;' name='aPdfLink'  data-val='https://dhapi.library.sh.cn/pdfview?innerflag=0&dbname=jp&doi="
				+ doi // chenss 20230215
				+ "'  ><img border='0' src='https://jpv1.library.sh.cn/jp/res/images/pdf2.png' width='20px' height='20px' title='点击查看PDF全文'></a>";

		return DOILink;
	}

	public static String GetFulltext_PDF(String doi, String ipAddress) {
	
		String result = "";
		// 如果客户端不是内网IP，则不允许查看

		if (!IPUtils.isPrivateIP(true)) {
			result = "<a href=\"javascript:alert(&quot;请到上海图书馆家谱阅览室浏览电子全文&quot;)\"><img img border='0' width='20px' height='20px' src='https://jpv1.library.sh.cn/jp/res/images/pdf2.png' alt='请到上海图书馆家谱阅览室浏览电子全文'  ></a>";
			return result;
		}
		String redisWorkKey = RedisUtils.key_fulltextIn_pdf.concat(doi);
		if (fullLink4ESJP.redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
			Object obj = fullLink4ESJP.redisUtil.get(redisWorkKey);
			result = String.valueOf(obj);
			return result;
		} else {
			Map<String, Object> _parms = new HashMap<>();
			_parms.put("doi", doi);
			String apiResult = HttpsUtil.postJson(CodeMsgUtil.getConfig("fulltext_pdf_url"), null, _parms);
			if (!StringUtil.isEmpty(apiResult)) {
				String apiResult_data = StringUtilC.getString(JSONObject.fromObject(apiResult).get("data"));
				if (!StringUtil.isEmpty(apiResult_data)) {
					result = apiResult_data;
					if (!fullLink4ESJP.redisUtil.exists(redisWorkKey)) {// 如果redis不存在
						// V1直接向缓存中添加数据
						fullLink4ESJP.redisUtil.set(redisWorkKey, result);
					}
				}
			}
			return result;
		}

	}

}
