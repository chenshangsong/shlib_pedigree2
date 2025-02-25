/**
 * 
 */
/**
 * @author think
 *
 */
package cn.sh.library.pedigree.webApi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.utils.IPUtils;
import net.sf.json.JSONArray;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi/ipinfo/")
public class IpController extends BaseController {
	@ResponseBody
	@RequestMapping(value = "/ip", method = RequestMethod.GET)
    public String ipinfo() {
		jsonResult = new HashMap<>();
        try {
            Map<String,Object> _result = new HashMap<>();
            _result.put("client_ip", IPUtils.getIpAddr(RequestFilter.threadLocalRequest.get()));
//            _result.put("server_port", String.valueOf(RequestFilter.threadLocalRequest.get().getServerPort()));
//            _result.put("server_host", RequestFilter.threadLocalRequest.get().getLocalAddr());
            Boolean innerIpFlg = IPUtils.isPrivateIP(true);
            _result.put("isPrivateIp", innerIpFlg);
            jsonResult.put("data", _result);
        } catch (Exception e) {
            System.out.println(e);
            jsonResult.put("status",false);
        }
        return JSONArray.fromObject(jsonResult).toString();
	}
}