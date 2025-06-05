package cn.sh.library.pedigree.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import cn.sh.library.pedigree.common.CodeMsgUtil;

public class ApiTestClient {
    private static final String API_URL = "http://127.0.0.1:8080/pedigree/service/work/get?uri=http%3A%2F%2Fdata.library.sh.cn%2Fjp%2Fresource%2Fwork%2Fdi6stes3yfy2hzwl";
    private static final int DELAY_MS = 1000; // 1秒间隔
    private static final Logger FAILURE_LOGGER = Logger.getLogger("FailureLogger");
    private static final Logger ERROR_LOGGER = Logger.getLogger("ErrorLogger");

    static {
        try {
            // 配置失败日志（非200响应）输出到 failure.log
            FileHandler failureHandler = new FileHandler("./failure.log", true); // true表示追加模式
            failureHandler.setFormatter(new SimpleFormatter());
            FAILURE_LOGGER.addHandler(failureHandler);
            FAILURE_LOGGER.setUseParentHandlers(false); // 避免输出到控制台

            // 配置错误日志（异常）输出到 error.log
            FileHandler errorHandler = new FileHandler("./error.log", true);
            errorHandler.setFormatter(new SimpleFormatter());
            ERROR_LOGGER.addHandler(errorHandler);
            ERROR_LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("日志处理器初始化失败: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    	
        int requestCount = 0;
        while (true) {
            try {
                String response = sendGetRequest(API_URL);
                System.out.printf("第 %d 次调用结果：%s%n", ++requestCount, response);
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                String errorMsg = "休眠被中断: " + e.getMessage();
                ERROR_LOGGER.log(Level.SEVERE, errorMsg, e); // 记录错误日志（含堆栈）
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                String errorMsg = "调用API失败: " + e.getMessage();
                ERROR_LOGGER.log(Level.SEVERE, errorMsg, e); // 记录错误日志（含堆栈）
            }
        }
    }

    private static String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                String failureMsg = String.format("请求失败，响应码: %d，URL: %s", responseCode, url);
                FAILURE_LOGGER.log(Level.WARNING, failureMsg); // 记录失败日志（非200响应）
                return failureMsg;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return "请求成功，响应内容: " + response;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
