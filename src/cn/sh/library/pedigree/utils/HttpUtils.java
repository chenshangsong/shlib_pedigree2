package cn.sh.library.pedigree.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;
import sun.net.www.protocol.http.HttpURLConnection;



/**
 * 日期操作公用类
 * 
 * @author chenss
 * 
 */
public class HttpUtils {
	public static String connPost(String urlStr, Map<String, Object> params) {
        try {
            URL url = new URL(urlStr);
            trustAllHosts();
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.connect();
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            String paramsStr = parseParams(params);
            out.writeBytes(paramsStr);
            out.flush();
            out.close();
            return readResponse(urlConnection);
        } catch (IOException e) {
        	e.printStackTrace();
            return null;
        }
    }
	/**
	 * 
	 * @param url
	 * @param jsonObject
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	   public static String HttpPost(String url, JSONObject jsonObject,String encoding) throws  IOException{
	       String body = "";

	       //创建httpclient对象
	       CloseableHttpClient client = HttpClients.createDefault();
	       //创建post方式请求对象
	       HttpPost httpPost = new HttpPost(url);

	       //装填参数
	       StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
	       entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
	       //设置参数到请求对象中
	       httpPost.setEntity(entity);
	       System.out.println("请求地址："+url);
	       httpPost.setHeader("Content-type", "application/json");
	       //执行请求操作，并拿到结果（同步阻塞）
	       CloseableHttpResponse response = client.execute(httpPost);
	       //获取结果实体
	       HttpEntity entityhttp = response.getEntity();
	       if (entityhttp != null) {
	           //按指定编码转换结果实体为String类型
	           body = EntityUtils.toString(entityhttp, encoding);
	       }
	       EntityUtils.consume(entityhttp);
	       //释放链接
	       response.close();
	       System.out.println("--创建结果--"+body);
	       return body;
	   }
	public static String readResponse(HttpsURLConnection urlConnection) throws IOException {
        // ------------------------------------------------------------------------
        InputStream is;
        if (urlConnection.getResponseCode() >= 400) {
            is = urlConnection.getErrorStream();
        } else {
            is = urlConnection.getInputStream();
        }
        // ------------------------------------------------------------------------
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        is.close();
        reader.close();
        urlConnection.disconnect();
        String response = sb.toString();
        return response;
    }

	

    /**
     * 转换参数
     * @param params
     * @return
     */
    protected static String parseParams(Map<String, Object> params/*, boolean encodeFlg*/) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                String value;
                if (entry.getValue() == null) {
                    value = "";
                } else {
//                    if (encodeFlg) {
                    value = URLEncoder.encode(entry.getValue().toString(), "UTF-8");
//                    } else {
//                        value = entry.getValue().toString();
//                    }
                }
                sb.append(entry.getKey())
                        .append("=")
                        .append(value)
                        .append("&");
            } catch (UnsupportedEncodingException ignored) {
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    /**
     * get
     * @param urlStr
     * @param params
     * @return
     */
    public static String connGet(String urlStr, Map<String, Object> params) {
        try {
            String paramsStr = parseParams(params);
            String tmpUrlStr = urlStr + "?" + paramsStr;
            URL url = new URL(tmpUrlStr);
            trustAllHosts();
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.connect();
            return readResponse(urlConnection);
        } catch (IOException e) {
            return null;
        }
    }
    
  
    
    
    static HostnameVerifier hv = new HostnameVerifier() {  
            public boolean verify(String urlHostName, SSLSession session) {  
                System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
                                   + session.getPeerHost());  
                return true;  
            }  
        };  
          
       
        static class miTM implements javax.net.ssl.TrustManager,  
                javax.net.ssl.X509TrustManager {  
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                return null;  
            }  
      
            public boolean isServerTrusted(  
                    java.security.cert.X509Certificate[] certs) {  
                return true;  
            }  
      
            public boolean isClientTrusted(  
                    java.security.cert.X509Certificate[] certs) {  
                return true;  
            }  
      
            public void checkServerTrusted(  
                    java.security.cert.X509Certificate[] certs, String authType)  
                    throws java.security.cert.CertificateException {  
                return;  
            }  
      
            public void checkClientTrusted(  
                    java.security.cert.X509Certificate[] certs, String authType)  
                    throws java.security.cert.CertificateException {  
                return;  
            }  
        }  
    
        private static void trustAllHosts() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    logger.info("trustAllHosts$checkClientTrusted");
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    logger.info("trustAllHosts$checkServerTrusted");
                }
            }};

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

}
