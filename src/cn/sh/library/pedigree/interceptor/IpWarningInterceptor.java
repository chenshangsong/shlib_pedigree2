package cn.sh.library.pedigree.interceptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.HttpUtil;
import cn.sh.library.pedigree.fullContentLink.FullLink;
import cn.sh.library.pedigree.sysManager.mapper.IpWarningMapper;
import cn.sh.library.pedigree.utils.StringUtilC;

public class IpWarningInterceptor implements HandlerInterceptor {
	public static long STOP_TIME = StringUtilC.getLong(CodeMsgUtil.getConfig("ipStopTime"));
	public static String isIntercept = StringUtilC.getString(CodeMsgUtil.getConfig("isIpIntercept"));
	// 禁止访问的IP
	public static String[] ignoreIPlist = StringUtilC.getString(CodeMsgUtil.getConfig("ignoreIP")).split(",");
	// private static final int STOP_TIME = 60000;// 1分钟
	@Autowired
	private IpWarningMapper ipWarningMapper;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

		// 检查配置拦截

		if (!isIntercept.equals("true")) {
			return true;
		}

		String IP = FullLink.getIpAddr(req);
		HttpUtil.setSessionValue(req, "userIp", IP);
		// CommonUtils.userIp = IP;
		// 访问页面路径
		String url = req.getRequestURI().toString();
		// 如果包含禁止访问的IP，则不允许访问，直接返回到报错画面。
		for (String strIP : ignoreIPlist) {
			if (strIP.equals(IP)) {
				res.sendRedirect(req.getContextPath() + "/ipwarning/error");
				// 必须加返回，否则报错
				return true;
			}

		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (CommonUtils.timemap.containsKey(IP)) {

			String str2 = CommonUtils.timemap.get(IP);
			try {
				Date d1 = df.parse(str);
				Date d2 = df.parse(str2);
				long diff = d1.getTime() - d2.getTime();

				if (url.contains("/ipwarning/error")) {
					long timego = (STOP_TIME) - diff;

					HttpSession session = req.getSession();
					session.setAttribute("timego", timego);
					return true;
				}

				if (diff > STOP_TIME) {

					CommonUtils.timemap.remove(IP);

				} else {
					res.sendRedirect(req.getContextPath() + "/ipwarning/error");
					// 必须加返回，否则报错

					return true;
				}
			} catch (Exception e) {

			}

		}

		// 判断map中是否存在这个ip

		if (CommonUtils.countmap.containsKey(IP)) {

			// 如果ip存在，说明这个IP已经访问过本站
			// 获取访问次数
			Integer count = CommonUtils.countmap.get(IP);
			// 把访问次数+1
			count++;

			CommonUtils.countmap.put(IP, count);

		} else {
			// 因为这个IP是第一次访问，所以值为1
			CommonUtils.countmap.put(IP, 1);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
