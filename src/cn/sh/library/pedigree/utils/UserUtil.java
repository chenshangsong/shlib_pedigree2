package cn.sh.library.pedigree.utils;

import cn.sh.library.pedigree.common.CodeMsgUtil;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {

    public static Integer getUserId(HttpServletRequest request) {
        return getUserId(request.getHeader("Authorization"));
    }
    public static Integer getUserId(String token) {
        JSONObject userInfo = getUserInfo(token);
        if(userInfo != null && StringUtils.hasLength(userInfo.getString("id"))){
            String id_str = userInfo.getString("id");
            return Integer.parseInt(id_str);
        }
        return null;
    }
    public static JSONObject getUserInfo(HttpServletRequest request) {
        return getUserInfo(request.getHeader("Authorization"));
    }
    public static JSONObject getUserInfo(String token) {
        if(!StringUtils.hasLength(token)){
            return null;
        }
        String userInfoUrl = CodeMsgUtil.getConfig("USER_INFO_URL");
        String rs = HttpsUtil.postJson(userInfoUrl + token, null, null);
        if (StringUtils.hasLength(rs)) {
            JSONObject resData = JSONObject.fromObject(rs);
            if ("0".equals(resData.getString("result"))) {
                JSONObject userInfo = resData.getJSONObject("data");
                return userInfo;
            }
        }
        return null;
    }
}
