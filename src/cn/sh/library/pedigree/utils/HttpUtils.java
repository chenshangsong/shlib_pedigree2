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
