package cn.sh.library.pedigree.controller.sysmanager;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sysManager.mapper.FeedBackMapper;
import cn.sh.library.pedigree.sysManager.mapper.UserInfoMapper;
import cn.sh.library.pedigree.sysManager.model.FeedBackConfModel;
import cn.sh.library.pedigree.sysManager.model.FeedBackDetailModel;
import cn.sh.library.pedigree.sysManager.model.FeedBackMainModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 反馈Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/feedBack")
public class FeedBackController extends BaseController {

	@Autowired
	private FeedBackMapper feedBackMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Resource
	private WorkService workService;

	/**
	 * 反馈列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/feedBackMainList", method = RequestMethod.GET)
	public ModelAndView feedBackMainList(HttpSession httpSession) throws Exception {
		try {
			//专家，管理员用户，查询所有
			String[] admin = new String[] { "2", "3" };
			String userId = StringUtilC
					.getString(getUser(httpSession).getId());
			if (Arrays.asList(admin).contains(getUser(httpSession).getRoleId())) {
				userId = "";
			}
			List<FeedBackMainModel> feedBackMainList = feedBackMapper
					.getFeedBackMainList(userId);
			for (FeedBackMainModel feedMain : feedBackMainList) {
				UserInfoModel user = this.getUserInfoModel(feedMain
						.getCreatedUser());
				if (user != null) {
					feedMain.setName(user.getUserName());
				}
				if (feedMain.getStatus().equals("0")) {
					feedMain.setStatus("未回复");
				} else {
					feedMain.setStatus("已回复");
				}
			}

			modelAndView.setViewName("sysmanager/feedBack/feedBackMainList");
			modelAndView.addObject("data", feedBackMainList);
			return modelAndView;

		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
			return null;
		}
	}

	/**
	 * 反馈登录页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/feedBackInsertPage", method = RequestMethod.GET)
	public ModelAndView feedBackInsertPage(@Param(value = "uri") String uri)
			throws Exception {
		FeedBackDetailModel detail = new FeedBackDetailModel();
		// 如果是新增，则证明从其他页面跳入，有URI参数
		detail.setGenealogyUri(uri);
		modelAndView.addObject("detail", detail);
		modelAndView.setViewName("sysmanager/feedBack/feedBackInsert");
		return modelAndView;
	}

	/**
	 * 反馈列表弹框列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/feedBackMainListConf", method = RequestMethod.GET)
	public ModelAndView feedBackMainListConf(@Param(value = "uri") String uri)
			throws Exception {
		try {
			Work work = workService.getWork(uri, false);
			FeedBackConfModel model = new FeedBackConfModel();
			if (work != null && !StringUtilC.isEmpty(work.getDtitle())) {
				model.setGenealogyTitle(work.getDtitle());
			}
			model.setGenealogyUri(uri);
			List<FeedBackConfModel> feedBackMainList = feedBackMapper
					.getFeedBackConfList(model);
			for (FeedBackConfModel feedMain : feedBackMainList) {
				if (feedMain.getStatus().equals("0")) {
					feedMain.setStatus("未回复");
				} else {
					feedMain.setStatus("已回复");
				}
			}
			modelAndView
					.setViewName("sysmanager/feedBack/feedBackMainListConf");
			modelAndView.addObject("data", feedBackMainList);
			modelAndView.addObject("model", model);
			return modelAndView;

		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
			return null;
		}
	}

	/**
	 * 反馈弹框页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/feedBackInsertConf", method = RequestMethod.GET)
	public ModelAndView feedBackInsertConf(@Param(value = "uri") String uri)
			throws Exception {
		FeedBackDetailModel model = new FeedBackDetailModel();
		Work work = workService.getWork(uri, false);
		if (work != null && !StringUtilC.isEmpty(work.getDtitle())) {
			model.setGenealogyTitle(work.getDtitle());
		}
		// 如果是新增，则证明从其他页面跳入，有URI参数
		model.setGenealogyUri(work.getUri());
		modelAndView.addObject("detail", model);
		modelAndView.setViewName("sysmanager/feedBack/feedBackInsertConf");
		return modelAndView;
	}

	/**
	 * 反馈提交
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doFeedBackInsert", method = RequestMethod.POST)
	public String doFeedBackInsert(@RequestBody FeedBackDetailModel _detail,HttpSession httpSession)
			throws Exception {
		try {
			String userId = getUser(httpSession).getId().toString();
			_detail.setFeedbackUser(userId);
			_detail.setFeedbackDate(DateUtilC.getNowDate());
			// _detail.setGenealogyUri("1");
			_detail.setCreatedDate(DateUtilC.getNowDate());
			_detail.setCreatedUser(userId);
			_detail.setStatus("0");
			//翻译转换-去除特殊字符
			_detail=(FeedBackDetailModel)StringUtilC.convertModel(_detail);
			feedBackMapper.insertFeedBackMain(_detail);
			_detail.setFeedbackMainId(_detail.getId());
			Integer ifSucess = feedBackMapper.insertFeedBackList(_detail);
			// 如果成功
			if (ifSucess > 0) {
				jsonResult.put(result, FWConstants.result_success);
			}
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 批注提交
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doFeedBackUpdate", method = RequestMethod.POST)
	public String doFeedBackUpdate(@RequestBody FeedBackDetailModel _detail,HttpSession httpSession)
			throws Exception {
		try {

			_detail.setPostilUser(StringUtilC.getString(getUser(httpSession)
					.getId()));
			_detail.setPostilDate(DateUtilC.getNowDate());
			//翻译转换-去除特殊字符
			_detail=(FeedBackDetailModel)StringUtilC.convertModel(_detail);
			int ifSucess = feedBackMapper.updatePostil(_detail);
			if (ifSucess > 0) {
				jsonResult.put(result, FWConstants.result_success);
			}
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 反馈信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/toEditFeedBack", method = RequestMethod.GET)
	public ModelAndView toEditFeedBack(@Param(value = "id") String id,HttpSession httpSession)
			throws Exception {

		FeedBackDetailModel detail = feedBackMapper.getDetail(Integer
				.parseInt(id));
		if (detail != null) {
			UserInfoModel user = this
					.getUserInfoModel(detail.getFeedbackUser());
			detail.setFeedbackUser(user.getUserName());
			detail.setRoleId(getUser(httpSession).getRoleId());
		}
		modelAndView.addObject("detail", detail);
		modelAndView.setViewName("sysmanager/feedBack/feedBackEdit");

		return modelAndView;
	}
 
	private UserInfoModel getUserInfoModel(String userId) {

		UserInfoModel user = userInfoMapper.getUserById(userId);
		return user;
	}
}
