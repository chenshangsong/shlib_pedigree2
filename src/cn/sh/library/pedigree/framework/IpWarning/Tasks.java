package cn.sh.library.pedigree.framework.IpWarning;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.sh.library.pedigree.common.CommonUtils;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;
import cn.sh.library.pedigree.services.IpWarningService;
import cn.sh.library.pedigree.sysManager.model.IpWarningModel;
import cn.sh.library.pedigree.utils.StringUtilC;

public class Tasks {

	private static String mailAddxTo = CodeMsgUtil.getConfig("mailAddxTo");
	private static String mailAddrCC = CodeMsgUtil.getConfig("mailAddrCC");
	// private static String mailAddrBCC = CodeMsgUtil.getConfig("mailAddrBCC");

	@Autowired
	private IpWarningService ipWarningService;
	/*
	 * @Autowired private IpWarningMapper ipWarningMapper;
	 */
	// 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
	private static String myEmailAccount = "stjpadmin@libnet.sh.cn";
	private static String myEmailPassword = "sysnetstjp2016";

	// 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
	// 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
	private static String myEmailSMTPHost = "ps.libnet.sh.cn";

	// 收件人邮箱（替换为自己知道的有效邮箱）
	private static String receiveMailAccount = "sd918171@qq.com";
	// private LoginInterceptor login;

	private static List<String> ipList;
	private static String Nowtime = null;

	public void run() {
		ipList = new ArrayList<String>();
		for (String key : CommonUtils.countmap.keySet()) {
			//如果单位时间内访问时长大于1000
			if (CommonUtils.countmap.get(key) > StringUtilC
					.getInteger(CodeMsgUtil.getConfig("ipMaxCount"))) {
				if (!CommonUtils.timemap.containsKey(key)) {
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String str = sdf.format(date);
					CommonUtils.timemap.put(key, str);
					Nowtime = str;
					IpWarningModel ipwar = new IpWarningModel();
					ipwar.setIp(key);
					ipwar.setTime(Nowtime);
					ipWarningService.insertip(ipwar);

					ipList.add(key);
					// System.out.println("这个IP地址访问过于频繁禁止半小时访问网站" + key);
				}
			}
			CommonUtils.countmap.remove(key);
		}
		if (ipList != null && ipList.size() > 0) {
			sendMails();
		}
	}

	private void sendMails() {

		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", myEmailSMTPHost); // 发件人的邮箱的
																// SMTP
																// 服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

		// PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接,
		// 也可以自己开启),
		// 如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接”
		// 等错误,
		// 打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
		/*
		 * // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接, //
		 * 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助, // QQ邮箱的SMTP(SLL)端口为465或587,
		 * 其他邮箱自行去查看) final String smtpPort = "465";
		 * props.setProperty("mail.smtp.port", smtpPort);
		 * props.setProperty("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * props.setProperty("mail.smtp.socketFactory.fallback", "false");
		 * props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		 */

		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log

		// 3. 创建一封邮件
		MimeMessage message = null;
		try {
			message = createMimeMessage(session, myEmailAccount,
					receiveMailAccount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 4. 根据 Session 获取邮件传输对象
		Transport transport = null;
		try {
			transport = session.getTransport();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致,
		// 否则报错
		//
		// PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
		// 仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
		// 类型到对应邮件服务器的帮助网站上查看具体失败原因。
		//
		// PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
		// (1) 邮箱没有开启 SMTP 服务;
		// (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
		// (3) 邮箱服务器要求必须要使用 SSL 安全连接;
		// (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
		// (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
		//
		// PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
		try {
			transport.connect(myEmailAccount, myEmailPassword);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients()
		// 获取到的是在创建邮件对象时添加的所有收件人,
		// 抄送人, 密送人
		try {
			transport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 7. 关闭连接
		try {
			transport.close();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static MimeMessage createMimeMessage(Session session,
			String sendMail, String receiveMail) throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人
		message.setFrom(new InternetAddress(sendMail, "内部系统", "UTF-8"));

		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		// To
		String[] tos = mailAddxTo.split(",");
		Address[] toAddr = new InternetAddress[tos.length];

		for (int i = 0; i < tos.length; i++) {
			if (!StringUtilC.isEmpty(tos[i])) {
				toAddr[i] = new InternetAddress(tos[i], "", "UTF-8");
			}
		}
		message.setRecipients(MimeMessage.RecipientType.TO, toAddr);

		// CC
		if (!StringUtilC.isEmpty(mailAddrCC)) {
			String[] ccs = mailAddrCC.split(",");

			Address[] ccAddr = new InternetAddress[ccs.length];
			for (int i = 0; i < ccs.length; i++) {
				if (!StringUtilC.isEmpty(ccs[i])) {
					ccAddr[i] = new InternetAddress(ccs[i], "", "UTF-8");
				}
			}
			message.setRecipients(MimeMessage.RecipientType.CC, ccAddr);
		}
		// BCC
		// if (!StringUtilC.isEmpty(mailAddrBCC)) {
		// String[] bccs = mailAddrBCC.split(",");

		// Address[] bccAddr = new InternetAddress[bccs.length];
		// for (int i = 0; i < bccs.length; i++) {
		// if (!StringUtilC.isEmpty(bccs[i])) {
		// bccAddr[i] = new InternetAddress(bccs[i], "", "UTF-8");
		// }
		// }
		// message.setRecipients(MimeMessage.RecipientType.BCC, bccAddr);
		// }
		// 4. Subject: 邮件主题
		message.setSubject("IP频繁访问", "UTF-8");

		// 5. Content: 邮件正文（可以使用html标签）
		message.setContent(StringUtilC.getString(ipList)
				+ "这个IP地址对上海图书馆华人家谱总目系统访问过于频繁，请注意查看" + "------" + Nowtime,
				"text/html;charset=UTF-8");

		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}
}
