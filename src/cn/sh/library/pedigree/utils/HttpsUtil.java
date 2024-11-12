package cn.sh.library.pedigree.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import cn.sh.library.pedigree.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Component
public class HttpsUtil {
	private static Logger log = Logger.getLogger(HttpsUtil.class);

    /**
     * 请求连接构造对象
     */
    private static final HttpClientBuilder httpClientBuilder = HttpClients.custom();
 
    /**
     * 连接池最大连接数
     */
    private static final int MAX_TOTAL = 8;
 
    /**
     * 每个路由最大默认连接数
     */
    private static final int DEFAULT_MAX_RER_ROUTE = 8;
 
    /**
     * 获取连接获取超时时间
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;
 
    /**
     * 连接超时时间
     */
    private static final int CONNECTION_TIMEOUT = 2000;
 
    /**
     * 数据响应超时时间
     */
    private static final int SOCKET_TIMEOUT = 10000;
 
 
 
    static {
        /*
         1、绕开不安全的https请求的证书验证(不需要可以注释，然后使用空参数的PoolingHttpClientConnectionManager构造连接池管理对象)
         */
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trustHttpsCertificates())
                .build();
 
        /*
         2、创建请求连接池管理
         */
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 设置连接池最大连接数
        cm.setMaxTotal(MAX_TOTAL);
        // 设置每个路由最大默认连接数
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_RER_ROUTE);
        httpClientBuilder.setConnectionManager(cm);
 
        /*
        3、设置默认请求配置
         */
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 设置获取连接获取超时时间
                .setConnectTimeout(CONNECTION_TIMEOUT) // 设置连接超时时间
                .setSocketTimeout(SOCKET_TIMEOUT) // 设置数据响应超时时间
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
    }
 
 
    /**
     *  执行get请求（网页）
     * @param url 请求地址(含有特殊符号需要URLEncoder编码)
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String getPage(String url, Map<String, String> headers) {
 
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(url);
 
        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38");
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpGet.setHeader(headerKey, headers.get(headerKey));
            }
        }
 
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
            	log.error(String.format("请求地址(%s)失败:%s", url, statusCode));
            }
        } catch (Exception e) {
            log.error(String.format("请求地址(%s)失败:%s", url, e));
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }
 
    /**
     *  执行post请求（form表单）
     * @param url 请求地址
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
         // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }
 
        // 设置请求参数
        if (params!=null) {
            List<NameValuePair> nvList = new ArrayList<>(params.size());
            for (String paramKey : params.keySet()) {
                NameValuePair nv = new BasicNameValuePair(paramKey, params.get(paramKey));
                nvList.add(nv);
            }
            HttpEntity paramsEntity = new UrlEncodedFormEntity(nvList, StandardCharsets.UTF_8);
            httpPost.setEntity(paramsEntity);
        }
 
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
            	log.error(String.format("请求地址(%s)失败:%s", url, statusCode));
            }
        } catch (Exception e) {
            log.error(String.format("请求地址(%s)失败:%s", url, e));
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }
 
    /**
     *  执行post请求（接口）
     * @param url 请求地址
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String getJson(String url, Map<String, String> headers) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(url);
        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpGet.setHeader(headerKey, headers.get(headerKey));
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
            	log.error(String.format("请求地址(%s)失败:%s", url, statusCode));
            }
        } catch (Exception e) {
            log.error(String.format("请求地址(%s)失败:%s", url, e));
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }
 
    /**
     *  执行post请求（接口）
     * @param url 请求地址
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String postJson(String url, Map<String, String> headers, Map<String, Object> params) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }
        if (params!=null) {
            HttpEntity paramEntity = new StringEntity(JSONObject.fromObject(params).toString(), StandardCharsets.UTF_8);
            httpPost.setEntity(paramEntity);
        }
        //1connectTimeOut 建立连接超时时间, 众所周知 http的三次握手机智, 在正式进行数据传输之前需要先完成三次握手. connectTimeOut就是指在进行三次握手行为时所设置的超时. 例如在文末例子中,我访问了一个虚假的域名,http://74.125.203.100, 因为域名本身就不存在, 所以三次握手行为肯定是失败的, 所以链接肯定是无法建立的. 根据如下代码, 设置的时间是5000毫秒 所以运行程序会在五秒之后抛出链接超时异常
        //2socketTimeOut 建立链接成功数据传输导致的超时时间, 当三次握手行为成功后, 即可通过所建立的http通道进行数据传输, 此时如果超过设置时间并没有获取到对应的数据包就会抛出超时异常, 此处有个需要注意点是socketTimeOut所处理的超时时间是指相邻两个数据包传输之间所经历的时间. 例如链接建立成功后 由于数据过大 服务端每隔1秒传送一个数据包给客户端, 此时设置的超时时间为3秒,一共发送了10个数据包,总共耗时10秒, 请求总共花费10秒, 但是并不会报超时异常, 是因为每次数据包传输之间的时间都不超过3秒,所以不会抛出异常, 总结一下 socketTimeOut是指链接建立成功后,数据包传输之间时间超时限制.
        //.setConnectionRequestTimeout(1000) // 从数据库连接池获取连接超时时间设置
        //.setSocketTimeout(1000)   // socket连接建立成功, 数据传输响应超时
        //.setConnectTimeout(5000) // 建立socket链接超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).setConnectTimeout(5000).setSocketTimeout(1000).build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
            	log.error(String.format("请求地址(%s)失败:%s", url, statusCode));
            }
        } catch (Exception e) {
            log.error(String.format("请求地址(%s)失败:%s", url, e));
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }

    /**
     * 构建https安全连接工厂
     * @return 安全连接工厂
     */
    private static ConnectionSocketFactory trustHttpsCertificates() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }

            });
            SSLContext sslContext = sslContextBuilder.build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, // 支持的https安全认证协议
                    null, NoopHostnameVerifier.INSTANCE);
            return sslConnectionSocketFactory;
        } catch (Exception e) {
            log.error("构建安全连接工厂失败", e);
            throw new RuntimeException("构建安全连接工厂失败");
        }
    }

}