package cn.sh.library.pedigree.common.onlineInfomation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//2、统计用户信息,通过ServletRequestListener完成     --定义List容器来存储所有用户信息（通过ServletContext设置，获取）

import cn.sh.library.pedigree.fullContentLink.FullLink;
/**
 * 
 * @author chenss
 *
 */
//@WebListener
public class OnlineCountInfo implements ServletRequestListener {

	private ArrayList<OnLineUser> userList;// 存储访问用户的信息

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		// 获得ServletContext域中的list
		userList = (ArrayList<OnLineUser>) sre.getServletContext()
				.getAttribute("onlineUserlist");

		// if(list.isEmpty())
		if (userList == null) {
			userList = new ArrayList<OnLineUser>();
		}
		// 为了获得session对象，进行强制类型转换
		HttpServletRequest request = (HttpServletRequest) sre
				.getServletRequest();
		HttpSession session = request.getSession();
		String sessionId = session.getId();// sessionId

		// session的列表中没有当前的sessionId，即：以前的所有访问用户中，没有当前的访问用户，所以把当前的访问用户信息加入
		if (OnLineUtil.getByUserId(userList, sessionId) == null) {
			OnLineUser user = new OnLineUser();
			user.setSessionId(sessionId);
			user.setIp(FullLink.getIpAddr(request));
			// 设置时间
			user.setFirstTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()));
			userList.add(user);
		}
		sre.getServletContext().setAttribute("onlineUserlist", userList);// 保存所有用户信息到list中
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {

	}

}