package cn.sh.library.pedigree.webApi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.RedisUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiJpUpdateService;

@Controller
@RequestMapping("/webapi/jpUpdate")
public class ApiJpUpdateController extends BaseController {

	@Resource
	private ApiJpUpdateService apiJpUpdateService;
	@Autowired
	private RedisUtils redisUtil;

	/**
	 * 家谱修改
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/update")
	public String update(@RequestBody JSONObject jsonObject, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		result.put("result", FWConstants.result_error);
		String workUri = jsonObject.getString("workUri");
		String redisWorkKey = RedisUtils.key_work.concat(workUri);
		UserInfoModel _user = (UserInfoModel) (session.getAttribute("userSession"));
		try {

			apiJpUpdateService.updateJp(jsonObject, _user);
			if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
				redisUtil.remove(redisWorkKey);
			}
			result.put("result", FWConstants.result_success);
			result.put("data", "修改成功！");
			result.put("workUri", jsonObject.getString("workUri"));
			result.put("instanceUri", jsonObject.getString("instanceUri"));
		} catch (Exception e) {
			result.put("data", e.getMessage());
		}
		return JSonUtils.toJSon(result);
	}

	/**
	 * 家谱是否可访问标记修改方法 chenss20221012
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/updateWorkAccFlag")
	public String updateWorkAccFlag(@RequestBody JSONObject jsonObject, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		result.put("result", FWConstants.result_error);
		UserInfoModel _user = (UserInfoModel) (session.getAttribute("userSession"));
		try {
			String workUri = jsonObject.getString("workUri");
			String redisWorkKey = RedisUtils.key_work.concat(workUri);
			apiJpUpdateService.updateJpWorkAccFlag(jsonObject, _user);
			if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
				redisUtil.remove(redisWorkKey);
			}
			result.put("result", FWConstants.result_success);
			result.put("data", "修改成功！");
			result.put("workUri", jsonObject.getString("workUri"));
		} catch (Exception e) {
			result.put("data", e.getMessage());
		}
		return JSonUtils.toJSon(result);
	}

	/**
	 * 家谱itemOf修改方法 编目系统使用 chenss20221017
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/changeItemOf")
	public String changeItemOf(@RequestBody JSONObject jsonObject, HttpSession session, String source) {
		Map<String, Object> result = new HashMap<>();
		result.put("result", FWConstants.result_error);

		UserInfoModel _user = (UserInfoModel) (session.getAttribute("userSession"));
		try {
			apiJpUpdateService.chageItemOf(jsonObject, _user);
			result.put("result", FWConstants.result_success);
			result.put("data", "修改成功！");
			result.put("itemUri", jsonObject.getString("itemUri"));
		} catch (Exception e) {
			result.put("data", e.getMessage());
		}
		return JSonUtils.toJSon(result);
	}

	/**
	 * 新增家谱
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/insert")
	public synchronized String insert(@RequestBody JSONObject jsonObject, HttpSession session) {

		// 开启线程进行分别测试输出类的hashCode,测试是否申请到同一个类
		Map<String, Object> result = new HashMap<>();
		// System.out.println(jsonObject.toString());
		result.put("result", FWConstants.result_error);
		UserInfoModel _user = (UserInfoModel) (session.getAttribute("userSession"));
		String workUri = null;
		String instanceUri = null;
		try {
			// Thread.sleep(15000);
			// System.out.println("@RequestMapping(method = RequestMethod.POST, value =
			// \"/insert\") 开始时间："+ DateUtilC.getNowDateTime());
			workUri = jsonObject.optString("workUri");
			instanceUri = jsonObject.optString("instanceUri");
			// 如果workUri、instanceUri不为空，则先进行删除操作。
			if (StringUtils.isNotBlank(workUri) && StringUtils.isNotBlank(instanceUri)) {
				apiJpUpdateService.deleteJp(workUri, instanceUri, _user);
				System.out.println("Controller +delete 成功");
				result.put("data", "修改成功！");
			} // 否则进行URI重新生成、插入。
			else {
				workUri = Constant.URI_PREFIX_WORK + StringUtilC.getRandomUriValue(16);
				instanceUri = Constant.URI_PREFIX_INSTANCE + StringUtilC.getRandomUriValue(16);
				result.put("data", "新增成功！");
			}
			apiJpUpdateService.insertJp(jsonObject, _user, workUri, instanceUri);

			String redisWorkKey = RedisUtils.key_work.concat(workUri);

			if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
				redisUtil.remove(redisWorkKey);
			}

			System.out.println("Controller +insert 成功");
			result.put("result", FWConstants.result_success);
			result.put("workUri", workUri);
			result.put("instanceUri", instanceUri);
		} catch (Exception e) {
			result.put("data", e.getMessage());
		}
		return JSonUtils.toJSon(result);
	}

	/**
	 * 删除家谱
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/delete")
	public String delete(String workUri, String instanceUri, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		result.put("result", FWConstants.result_error);
		UserInfoModel _user = (UserInfoModel) (session.getAttribute("userSession"));

		try {
			String redisWorkKey = RedisUtils.key_work.concat(workUri);

			if (redisUtil.exists(redisWorkKey)) {// 如果redis缓存存在数据，则返回数据
				redisUtil.remove(redisWorkKey);
			}
			apiJpUpdateService.deleteJp(workUri, instanceUri, _user);
			result.put("data", "删除成功！");
			result.put("result", FWConstants.result_success);
			result.put("workUri", workUri);
			result.put("instanceUri", instanceUri);
		} catch (Exception e) {
			result.put("data", e.getMessage());
		}
		return JSonUtils.toJSon(result);
	}

}
