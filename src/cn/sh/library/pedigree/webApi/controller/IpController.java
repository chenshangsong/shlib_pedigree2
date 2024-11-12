/**
 * 
 */
/**
 * @author think
 *
 */
package cn.sh.library.pedigree.webApi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.dataImport.DataTypeMap;
import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.framework.util.JsonUtil;
import cn.sh.library.pedigree.framework.util.PreloadApiFuriPlaceList;
import cn.sh.library.pedigree.services.AncestraltempleService;
import cn.sh.library.pedigree.services.OrganizationService;
import cn.sh.library.pedigree.utils.IPUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
            _result.put("server_port", String.valueOf(RequestFilter.threadLocalRequest.get().getServerPort()));
            _result.put("server_host", RequestFilter.threadLocalRequest.get().getLocalAddr());
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