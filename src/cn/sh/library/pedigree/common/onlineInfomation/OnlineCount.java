package cn.sh.library.pedigree.common.onlineInfomation;

import java.util.ArrayList;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 
 * @author chenss
 *1、统计用户数量，通过HttpSessionListener完成             --定义number来存储数量（通过ServletContext设置，获取）
 */
//@WebListener
public class OnlineCount implements HttpSessionListener {

	private int num = 0;// 统计用户在线人数

	@Override
	public void sessionCreated(HttpSessionEvent se) {
	//	se.getSession().setMaxInactiveInterval(5);
		num++;
		// 设置到ServletContext域中
		se.getSession().getServletContext().setAttribute("onlineNum", num);
		//System.out.println(" add....");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

		num--;
		// 设置到ServletContext域中
		se.getSession().getServletContext().setAttribute("onlineNum", num);
		System.out.println(" remove....");

		// 注意，此处还要进行设置
		// 从session列表中移除session
		ArrayList<OnLineUser> list = null;
		list = (ArrayList<OnLineUser>) se.getSession().getServletContext()
				.getAttribute("onlineUserlist");

		if (OnLineUtil.getByUserId(list, se.getSession().getId()) != null) {
			list.remove(OnLineUtil.getByUserId(list, se.getSession().getId()));
		}
	}

}