package cn.sh.library.pedigree.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.sh.library.pedigree.utils.HttpsUtil;
import cn.sh.library.pedigree.utils.IPUtils;
import cn.sh.library.pedigree.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.common.dataImport.DataUtilC;
import cn.sh.library.pedigree.services.FullImgAnnotationService;
import cn.sh.library.pedigree.sysManager.model.FullImgAnnotationModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 谱籍表
 * 
 * @author liuyi
 * @date 2015/1/5 0005
 */
@Controller
@RequestMapping("/full-img")
public class FullLinkViewerController extends BaseController {
	@Resource
	private FullImgAnnotationService fullImgAnnotationService;

	@RequestMapping(value = "/{org:.+}/{manifest:.+}", method = RequestMethod.GET)
	public ModelAndView Plist(@PathVariable(value = "org") String org,
			@PathVariable(value = "manifest") String manifest, 
			String uri,HttpSession hs,String token) {
		ModelAndView modelAndView = new ModelAndView();

		// 外网未登录，不能查看。
		Boolean privateIP = IPUtils.isPrivateIP(true);
		if(!privateIP){
			if(!StringUtils.hasLength(token)){
				modelAndView.setViewName("error/NotLoginError");
				return modelAndView;
			}
			Integer uid = null;
			try {
				uid = UserUtil.getUserId(token);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if (uid == null) {
				modelAndView.setViewName("error/NotLoginError");
				return modelAndView;
			}
		}

		UserInfoModel user = this.getUser(hs);
		String userId = "";
		if (user != null) {
			userId = user.getUserId();
		}
		modelAndView.addObject("org", org);
		modelAndView.addObject("manifest", manifest);
		modelAndView.setViewName("fulllink/viewer");
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}
	
	@RequestMapping(value = "/{org:.+}/{manifest:.+}/{isInnerIP:.+}", method = RequestMethod.GET)
	public ModelAndView Plist2(@PathVariable(value = "org") String org,
			@PathVariable(value = "manifest") String manifest, @PathVariable(value = "isInnerIP") String isInnerIP,
			String uri,HttpSession hs) {
		ModelAndView modelAndView = new ModelAndView();
		UserInfoModel user = this.getUser(hs);
		String userId = "";
		if (user != null) {
			userId = user.getUserId();
		}
		modelAndView.addObject("org", org);
		modelAndView.addObject("manifest", manifest);
		modelAndView.addObject("isInnerIP", isInnerIP);
		modelAndView.setViewName("fulllink/viewer");
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}
	
	//查询
	@ResponseBody
	@RequestMapping(value = "/search",method = RequestMethod.GET)
	public String search(String uri, HttpSession session) {
		try {
			System.out.println("canvasId: "+ uri);
			UserInfoModel user = this.getUser(session);
			String userId = "";
			if(user != null) {
				userId = user.getUserId();
				String systype = CodeMsgUtil.getConfig("systype");
				FullImgAnnotationModel fullImgAnnotationModel = new FullImgAnnotationModel();
				fullImgAnnotationModel.setCanvasId(uri);
				fullImgAnnotationModel.setUserId(userId);
				fullImgAnnotationModel.setSystype(systype);
				
				List<FullImgAnnotationModel> list = fullImgAnnotationService.getFIAnnotations(fullImgAnnotationModel);
				
				JSONArray annotations = new JSONArray();
				for(FullImgAnnotationModel model : list) {
					annotations.add(JSONObject.fromObject(model.getContent()));
				}
				jsonResult = new HashMap<String,Object>();
				jsonResult.put("rows", annotations);
				return JSonUtils.toJSon(jsonResult);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	//删除
	@ResponseBody
	@RequestMapping(value = "/destroy",method = RequestMethod.GET)
	public String destroy(String id, HttpSession session) {
		try {
			System.out.println("annotationId: "+ id);
			UserInfoModel user = this.getUser(session);
			String userId = "";
			if(user != null) {
				userId = user.getUserId();
				FullImgAnnotationModel fullImgAnnotationModel = new FullImgAnnotationModel();
				fullImgAnnotationModel.setAnnotationId(id);
				fullImgAnnotationModel.setUserId(userId);
				fullImgAnnotationService.deleteFIAnnotation(fullImgAnnotationModel);
				jsonResult = new HashMap<String,Object>();
				jsonResult.put("id", id);
				return JSonUtils.toJSon(jsonResult);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	//修改
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@RequestBody JSONObject jsonObject, HttpSession session) {
		try {
			System.out.println("update: "+ jsonObject);
			UserInfoModel user = this.getUser(session);
			String userId = "";
			if(user != null) {
				userId = user.getUserId();
				String canvasId = jsonObject.getString("uri");
				String annotationId = jsonObject.getString("id");
				FullImgAnnotationModel fullImgAnnotationModel = new FullImgAnnotationModel();
				fullImgAnnotationModel.setCanvasId(canvasId);
				fullImgAnnotationModel.setAnnotationId(annotationId);
				fullImgAnnotationModel.setContent(jsonObject.toString());
				fullImgAnnotationModel.setUserId(userId);
				fullImgAnnotationModel.setLastUpdate(userId);
				fullImgAnnotationService.updateFIAnnotation(fullImgAnnotationModel);
				
				return jsonObject.toString();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	//插入
	@ResponseBody
	@RequestMapping(value = "/create",  method = RequestMethod.POST)
	public String create(
			@RequestBody JSONObject jsonObject, HttpSession session) {
		try {
			System.out.println("create: "+ jsonObject);
			UserInfoModel user = this.getUser(session);
			String userId = "";
			if(user != null) {
				userId = user.getUserId();
				String canvasId = jsonObject.getString("uri");
				String annotationId = canvasId.substring(0, canvasId.length() - 5)
						+ "/annotation/" + DataUtilC.getRandomUriValue(16)
						+ ".json";
				String systype = CodeMsgUtil.getConfig("systype");
				jsonObject.put("id", annotationId);
				
				FullImgAnnotationModel fullImgAnnotationModel = new FullImgAnnotationModel();
				fullImgAnnotationModel.setCanvasId(canvasId);
				fullImgAnnotationModel.setAnnotationId(annotationId);
				fullImgAnnotationModel.setContent(jsonObject.toString());
				fullImgAnnotationModel.setUserId(userId);
				fullImgAnnotationModel.setCreator(userId);
				fullImgAnnotationModel.setLastUpdate(userId);
				fullImgAnnotationModel.setSystype(systype);
				fullImgAnnotationService.insertFIAnnotation(fullImgAnnotationModel);
				
				return jsonObject.toString();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
}












