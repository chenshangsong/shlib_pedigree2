package cn.sh.library.pedigree.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.sysManager.model.ChaodaiModel;
import net.sf.json.JSONObject;

public class WebApiUtils {

	/**
	 * 根据年份，得到所在朝代信息
	 * 
	 * @param strYear
	 * @return
	 */
	public static ChaodaiModel getCdInfoByYear(String strYear) {
		String uri = CodeMsgUtil.getConfig("timeUrl", strYear);
		ChaodaiModel model = new ChaodaiModel();
		try {
			String dynasty = WebApiUtils.getWebApiData(uri);
			if (!StringUtilC.isEmpty(dynasty)) {
				// 解析json数据
				JSONObject jb = JSONObject.fromObject(dynasty).getJSONObject(
						"result");
				model = JSonUtils.readValue(jb.toString(), ChaodaiModel.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * 取得webapi的数据
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getWebApiData(String url)
			throws ClientProtocolException, IOException {

		String strRet = "";
		// 创建HttpClient实例
		HttpClient httpclient = new DefaultHttpClient();
		// 创建Get方法实例
		HttpGet httpgets = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpgets);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			strRet = convertStreamToString(instreams);
			// System.out.println( strRet);
			httpgets.abort();
		}

		return strRet;
	}

	/**
	 * 从缓冲流读取webapi的数据
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String convertStreamToString(InputStream is)
			throws IOException {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	/**
	 * 根据DOI获取家谱封面
	 * @param strdoi
	 * @return
	 */
	public static String GetImagePathByDoi(String strdoi) {
		//WEB-INF绝对路径
		String Realpath = Constant.ProjectRealPath + "/res/images/jpfm/";
		//web路径
		String path = Constant.ProjectPath + "/res/images/jpfm/";
		String fileName = "";
		String[] dois = strdoi.split(";");
		for (String doi : dois) {
			if (doi.startsWith("STJP") && doi.length() >= 10) {
				doi = doi.substring(0, 10);
			}
			fileName = Realpath  + doi.toUpperCase() + ".jpg";
			File tempFile = new File(fileName);
			try {
				if (tempFile.exists()) {
					return path+ doi.toUpperCase() + ".jpg";
				} else {//如果大写文件名找不到文件，则找小写文件名
					String fileNameLower = "";
					fileNameLower = Realpath  + doi.toLowerCase() + ".jpg";
					File tempFileLower = new File(fileNameLower);
					if (tempFileLower.exists()) {
						return path+ doi.toLowerCase() + ".jpg";
					}
				}
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}
}
