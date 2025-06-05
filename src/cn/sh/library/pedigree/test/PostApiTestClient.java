package cn.sh.library.pedigree.test;

import cn.sh.library.pedigree.utils.HttpsUtil;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostApiTestClient {
    private static final Logger FAILURE_LOGGER = Logger.getLogger("FailureLogger");  // 失败日志（非200响应）
    private static final Logger ERROR_LOGGER = Logger.getLogger("ErrorLogger");    // 错误日志（异常）
    private static final String API_URL = "http://127.0.0.1:8080/pedigree/webapi/jpUpdate/insert";
    private static final int DELAY_MS = 1000;  // 调用间隔1秒

    public static void main(String[] args) {
        int requestCount = 0;
        while (true) {  // 可修改为固定次数循环（如for循环）
            try {
                Map<String, Object> params = buildRequestParams();
                Map<String, String> headers = buildRequestHeaders();

                String response = HttpsUtil.postJson(API_URL, headers, params);

                if (response == null) {
                    String failureMsg = "第 " + (++requestCount) + " 次调用失败：响应为空";
                    FAILURE_LOGGER.warn(failureMsg);
                    System.out.println(failureMsg);
                } else {
                    System.out.println("第 " + (++requestCount) + " 次调用结果：" + response);
                }

                Thread.sleep(DELAY_MS);  // 休眠1秒
            } catch (InterruptedException e) {
                String errorMsg = "休眠被中断: " + e.getMessage();
                ERROR_LOGGER.error(errorMsg, e);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                String errorMsg = "调用API失败: " + e.getMessage();
                ERROR_LOGGER.error(errorMsg, e);
            }
        }
    }

    private static Map<String, String> buildRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cookie", "JSESSIONID=955DD960603005667D30DE87D0642123");
        return headers;
    }

    private static Map<String, Object> buildRequestParams() {
        Map<String, Object> params = new HashMap<>();

        // 顶层简单字段
        params.put("title", "chen20241226");
        params.put("volumes", "2");
        params.put("placeValue", "上海市");
        params.put("place", "http://data.library.sh.cn/entity/place/ygp51rc1l346iig4");
        params.put("ins_banbenx", "天天我的所爱");
        params.put("ins_fuzhux", "书名据封面题。");
        params.put("temporalValue", "2016");
        params.put("temporalUri", "http://data.library.sh.cn/authority/temporal/j6mijtwmcfzq3t8b");
        params.put("temporalValueInt", "2016");
        params.put("publisher", "");
        params.put("type", "7");
        params.put("typeName", "稿本");
        params.put("editionUri", "http://data.library.sh.cn/vocab/edition/gao-ben");
        params.put("edition", "稿本");
        params.put("tanghaoList", new String[]{""});  // 数组字段
        params.put("categoryUri", "http://data.library.sh.cn/vocab/binding/u-pan");
        params.put("binding_type", "U盘");
        params.put("remark", "");
        params.put("description", "33  44");
        params.put("creatorNameValue", "张三");
        params.put("extent", "2册");
        params.put("workUri", "http://data.library.sh.cn/jp/resource/work/58n6ta6jvnqxv9ho");
        params.put("instanceUri", "http://data.library.sh.cn/jp/resource/instance/3oa3qe41vseb61xd");
        params.put("platform_type", "editFieldAll");
        params.put("insert_type", "全字段修改");

        // 嵌套对象：familyName
        Map<String, Object> familyName = new HashMap<>();
        familyName.put("label", "张");
        familyName.put("uriList", new String[]{"http://data.library.sh.cn/authority/familyname/uqd67cehrueuei5k"});
        params.put("familyName", familyName);

        // 数组对象：creator（List<Map>）
        List<Map<String, Object>> creatorList = new ArrayList<>();
        Map<String, Object> creatorItem = new HashMap<>();
        creatorItem.put("creatorName", "张三");
        creatorItem.put("creatorRole", "http://data.library.sh.cn/jp/vocab/compile/zhu-xiu");
        creatorItem.put("temporalValue_zrz", "");
        creatorItem.put("temporal", "");
        creatorItem.put("serialNo", 1);
        creatorItem.put("creatorRole_han", "主修");
        creatorList.add(creatorItem);
        params.put("creator", creatorList);

        // 数组对象：items（List<Map>）
        List<Map<String, Object>> itemsList = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("shelfMark", "xp00933rr");
        item.put("DOI", "STJP1020618");
        item.put("collectionOrg", "上海图书馆");
        item.put("heldBy", "http://data.library.sh.cn/entity/organization/brvqlrg8y55v1b5q");
        item.put("extent", "2");
        item.put("copyDescription", "");
        item.put("itemOf", "http://data.library.sh.cn/jp/resource/instance/3oa3qe41vseb61xd");
        item.put("carrierCategory", "http://data.library.sh.cn/vocab/carrierCategory/sao-miao-ying-xiang");
        itemsList.add(item);
        params.put("items", itemsList);

        // 数组对象：yuanzu（List<Map>）
        List<Map<String, Object>> yuanzuList = new ArrayList<>();
        Map<String, Object> yuanzuItem = new HashMap<>();
        yuanzuItem.put("temporalValue", "唐");
        yuanzuItem.put("temporal", "http://data.library.sh.cn/authority/temporal/icdquglj1cgbekqk");
        yuanzuItem.put("pname", "元祖");
        yuanzuItem.put("serialNo", 1);
        yuanzuList.add(yuanzuItem);
        params.put("yuanzu", yuanzuList);

        // 数组对象：shizu（List<Map>）
        List<Map<String, Object>> shizuList = new ArrayList<>();
        Map<String, Object> shizuItem = new HashMap<>();
        shizuItem.put("temporalValue", "唐");
        shizuItem.put("temporal", "http://data.library.sh.cn/authority/temporal/icdquglj1cgbekqk");
        shizuItem.put("pname", "始祖");
        shizuItem.put("serialNo", 1);
        shizuList.add(shizuItem);
        params.put("shizu", shizuList);

        // 数组对象：shiqianzu（List<Map>）
        List<Map<String, Object>> shiqianzuList = new ArrayList<>();
        Map<String, Object> shiqianzuItem = new HashMap<>();
        shiqianzuItem.put("temporalValue", "唐");
        shiqianzuItem.put("temporal", "http://data.library.sh.cn/authority/temporal/icdquglj1cgbekqk");
        shiqianzuItem.put("pname", "始迁祖");
        shiqianzuItem.put("serialNo", 1);
        shiqianzuList.add(shiqianzuItem);
        params.put("shiqianzu", shiqianzuList);

        // 数组对象：xianzu（List<Map>）
        List<Map<String, Object>> xianzuList = new ArrayList<>();
        Map<String, Object> xianzuItem = new HashMap<>();
        xianzuItem.put("temporalValue", "唐");
        xianzuItem.put("temporal", "http://data.library.sh.cn/authority/temporal/icdquglj1cgbekqk");
        xianzuItem.put("pname", "先祖");
        xianzuItem.put("serialNo", 1);
        xianzuList.add(xianzuItem);
        params.put("xianzu", xianzuList);

        // 数组对象：zhizu（List<Map>）
        List<Map<String, Object>> zhizuList = new ArrayList<>();
        Map<String, Object> zhizuItem = new HashMap<>();
        zhizuItem.put("temporalValue", "唐");
        zhizuItem.put("temporal", "http://data.library.sh.cn/authority/temporal/icdquglj1cgbekqk");
        zhizuItem.put("pname", "支祖");
        zhizuItem.put("serialNo", 1);
        zhizuList.add(zhizuItem);
        params.put("zhizu", zhizuList);

        // 数组对象：mingren（List<Map>）
        List<Map<String, Object>> mingrenList = new ArrayList<>();
        Map<String, Object> mingrenItem = new HashMap<>();
        mingrenItem.put("temporalValue", "唐");
        mingrenItem.put("temporal", "http://data.library.sh.cn/authority/temporal/icdquglj1cgbekqk");
        mingrenItem.put("pname", "名人");
        mingrenItem.put("serialNo", 1);
        mingrenList.add(mingrenItem);
        params.put("mingren", mingrenList);

        return params;
    }
}
