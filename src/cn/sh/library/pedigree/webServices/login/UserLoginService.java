package cn.sh.library.pedigree.webServices.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;

/**
 * 上海图书馆读者认证Webservices 接口文档文件
 * 
 * @author chenss
 *
 */
public class UserLoginService {
	//private static String serviceUrl = "http://218.1.116.104:8080/axis2/services/UserLogin/";
	private static String serviceUrl =CodeMsgUtil.getConfig("serviceUrl");
	//private static String serviceUrl ="http://10.1.31.101:8080/axis2/services/UserLogin/";
	/**
	 * 登录接口1测试
	 * 
	 * @throws Exception
	 */
	public static String UserLogin(UserLoginModel _model) throws Exception {
		// 拼装soap请求报文
		StringBuilder soapHeader = new StringBuilder();
		soapHeader
				.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:user=\"http://www.library.sh.cn/UserLogin/\">");
		soapHeader.append("<soapenv:Header/>");
		soapHeader.append("<soapenv:Body>");
		soapHeader.append("<user:UserLogin requestApp=\"test\">");
		soapHeader.append("<uid>" + _model.getUid() + "</uid>");
		soapHeader.append("<pwd>" + _model.getPwd() + "</pwd>");
		soapHeader.append(" </user:UserLogin>");
		soapHeader.append(" </soapenv:Body>");
		soapHeader.append("</soapenv:Envelope>");
		// 定义输入流，获取soap响应报文
		InputStream input = httpURLConnection(soapHeader).getInputStream();
		// 返回结果XML
		String outXML = convertStreamToString(input);
		//System.out.println(outXML);
		return outXML;
	}

	/**
	 * 登录接口2测试
	 * 
	 * @throws Exception
	 */
	public static String UserLogin_V2(UserLoginModel _model) throws Exception {
		// 拼装soap请求报文
		StringBuilder soapHeader = new StringBuilder();
		/*--------------------------------------UserLogin_V2-------------------------------*/
		soapHeader
				.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:user=\"http://www.library.sh.cn/UserLogin/\">");
		soapHeader.append("<soapenv:Header/>");
		soapHeader.append("<soapenv:Body>");
		soapHeader.append("<user:UserLogin_V2 requestApp=\"PC\">");
		soapHeader.append("<uid>" + _model.getUid() + "</uid>");
		soapHeader.append("<pwd>" + _model.getPwd() + "</pwd>");
		soapHeader.append(" <!--Optional:-->");
		soapHeader.append("<ParmRequest>");
		soapHeader.append(" <!--Zero or more repetitions:-->");
		soapHeader.append(" <Name>Sn</Name>");
		soapHeader.append(" <Name>Cn</Name>");
		soapHeader.append("<Name>ShLibBirth</Name>");
		soapHeader.append(" <Name>Mail</Name>");
		soapHeader.append(" <Name>Mobile</Name>");
		soapHeader.append(" <Name>ShLibIdentityNo</Name>");
		soapHeader.append(" <Name>ShLibAskNo</Name>");
		soapHeader.append(" <Name>ShLibBorrower</Name>");
		soapHeader.append(" </ParmRequest>");
		soapHeader.append("</user:UserLogin_V2>");
		soapHeader.append("</soapenv:Body>");
		soapHeader.append("</soapenv:Envelope>");
		// 定义输入流，获取soap响应报文
		InputStream input = httpURLConnection(soapHeader).getInputStream();
		// 返回结果XML
		return convertStreamToString(input);
	}

	/**
	 * 登录接口3测试
	 * 
	 * @throws Exception
	 */
	public static String UserLogin_SEC(UserLoginModel _model) throws Exception {
		// 拼装soap请求报文
		StringBuilder soapHeader = new StringBuilder();
		/*--------------------------------------UserLogin_V2-------------------------------*/
		soapHeader
				.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:user=\"http://www.library.sh.cn/UserLogin/\">");
		soapHeader.append("<soapenv:Header/>");
		soapHeader.append("<soapenv:Body>");
		soapHeader.append("<user:UserLogin_SEC requestApp=\"mytest\">");
		soapHeader.append(" <!--Optional:-->");
		soapHeader.append("  <uid>chen_emperor</uid>");
		soapHeader.append(" <!--Optional:-->");
		soapHeader.append(" <pwd>53a1b8c1522429fc2bd7276aa00b8986</pwd>");
		soapHeader.append(" <!--Optional:32 位md5 加密，大小写均可-->");
		soapHeader.append(" <type>MD5</type>");
		soapHeader.append(" <!--Optional:-->");
		soapHeader.append(" <ParmRequest>");
		soapHeader.append("  <!--Zero or more repetitions:-->");
		soapHeader.append(" <Name>ShLibBirth</Name>");
		soapHeader.append(" <Name>ShLibAskno</Name>");
		soapHeader.append("</ParmRequest>");
		soapHeader.append("</user:UserLogin_SEC>");
		soapHeader.append(" </soapenv:Body>");
		soapHeader.append("</soapenv:Envelope>");
		// 定义输入流，获取soap响应报文
		InputStream input = httpURLConnection(soapHeader).getInputStream();
		// 返回结果XML
		String outXML = convertStreamToString(input);
		//System.out.println(outXML);
		return outXML;
	}

	/**
	 * 
	 * @param soapHeader
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection httpURLConnection(StringBuilder soapHeader)
			throws IOException {
		URL u = new URL(serviceUrl);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.setRequestProperty("Host", "localhost:8080");
		conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		conn.setRequestProperty("Content-Length",
				String.valueOf(soapHeader.length()));
		conn.setRequestProperty("UserLogin", "");
		conn.setRequestMethod("POST");
		// 定义输出流
		OutputStream output = conn.getOutputStream();
		if (null != soapHeader) {
			byte[] b = soapHeader.toString().getBytes("utf-8");
			// 发送soap请求报文
			output.write(b, 0, b.length);
		}
		output.flush();
		output.close();
		return conn;
	}

	/**
	 * 转换为中文
	 * 
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8 * 1024);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			sb.delete(0, sb.length());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				return null;
			}
		}

		return sb.toString();
	}
}