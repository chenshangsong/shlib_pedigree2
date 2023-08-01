package cn.sh.library.pedigree.webApi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.RoleRelation;
import cn.sh.library.pedigree.controller.BaseController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.framework.util.PreloadApiPersonsFromFamilyRules;
import cn.sh.library.pedigree.sysManager.model.RelationLinkModel;
import cn.sh.library.pedigree.sysManager.model.RelationNodeModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiPersonService;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;
import net.sf.json.JSONArray;

/**
 * 首页Controller
 * 
 * @author chenss
 * 
 */
@Controller
@RequestMapping("/webapi/person")
public class ApiPersonController extends BaseController {
	@Resource
	private ApiPersonService apiPersonService;
	@Resource
	private ApiWorkService apiWorkService;

	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(ApiPersonController.class);

	@ResponseBody
	@RequestMapping(value = "/getPersonList", method = RequestMethod.GET)
	public String getPersonList(PersonSearchBean search, @Valid Pager pager) {
		jsonResult = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("persons",
					this.apiPersonService.list(search, null, pager));
			result.put("pager", pager);
			// 朝代信息
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("PersonController-list错误：" + DateUtilC.getNowDateTime()
					+ "----" + e);
			return "error";
		}
	}

	/**
	 * 根据人名URI，返回人名详细信息
	 * 
	 * @param puri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getInfoByUri", method = RequestMethod.GET)
	public String getInfoByUri(@Valid String uri) {
		jsonResult = new HashMap<>();
		String[] uris = uri.split(";");
		try {
			Person person = this.apiPersonService.get(uris[0], null);
			person.setWorks(this.apiWorkService.listWork4Person(uri, null));
			jsonResult.put("data", person);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("ApiHomeController-get错误：" + DateUtilC.getNowDateTime()
					+ "----" + e);
			return "error";
		}
	}

	/**
	 * 获取姓氏列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyNameList", method = RequestMethod.GET)
	public String getFamilyNameList(String fname,Boolean accurateFlag) {
		jsonResult = new HashMap<>();
		if(accurateFlag ==null) {
			accurateFlag=false;
		}
		try {
			List<Map<String, String>> familyNames = this.apiPersonService
					.listFamilyNames(fname,accurateFlag);
			jsonResult.put("data", familyNames);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("PersonController-familyName错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 谱系图
	 * 
	 * @param out
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPedigreeListByUri", method = RequestMethod.GET)
	public String getPedigreeList(String furi, @Valid String uri,
			boolean ispageInit) {
		jsonResult = new HashMap<>();
		try {
			String[] uris = StringUtilC.getString(uri).split(";");
			uri = uris[0];
			Map<String, Object> result = new HashMap<>();
			List<RelationNodeModel> nodes = new ArrayList<RelationNodeModel>();
			List<RelationLinkModel> links = new ArrayList<RelationLinkModel>();
			Map<String, String> mm = this.apiPersonService.getPxt(uri);
			RelationNodeModel node;

			// 本人
			if (ispageInit) {
				node = new RelationNodeModel();
				node.setId(mm.get("uri"));
				node.setTitle(mm.get("name"));
				node.setClick(false);
				node.setRole(RoleRelation.me.getGroup());
				node.setPerson(this.apiPersonService.get(mm.get("uri"), false));
				nodes.add(node);
			}

			// 妻子
			if (!StringUtilC.isEmpty(mm.get("wifeNames"))) {
				String[] wifes = mm.get("wifeNames").split(";");
				for (int i = 0; i < wifes.length; i++) {
					node = new RelationNodeModel();
					node.setId(mm.get("uri") + "wife" + i);
					node.setTitle(wifes[i]);
					node.setClick(false);
					node.setRole(RoleRelation.wife.getGroup());
					nodes.add(node);
				}
			}

			// 父亲
			if (!StringUtilC.getString(furi).equals(mm.get("father"))) {
				node = new RelationNodeModel();
				node.setId(mm.get("father"));
				node.setTitle(mm.get("fatherName"));
				node.setRole(RoleRelation.father.getGroup());
				node.setPerson(this.apiPersonService.get(mm.get("father"),
						false));
				nodes.add(node);
			}

			// 儿子
			if (!StringUtilC.isEmpty(mm.get("sonUris"))) {
				String[] sons = mm.get("sonUris").split(";");
				for (int i = 0; i < sons.length; i++) {
					if (furi.equals(sons[i])) {
						continue;
					}
					node = new RelationNodeModel();
					node.setId(sons[i]);
					node.setTitle(mm.get("sonNames").split(";")[i]);
					node.setRole(RoleRelation.son.getGroup());
					Person thisPerson = this.apiPersonService.get(sons[i],
							false);
					if (thisPerson != null
							&& !StringUtilC.isEmpty(thisPerson.getUri())) {
						node.setPerson(thisPerson);
					}
					nodes.add(node);
				}
			}

			// link
			for (RelationNodeModel no : nodes) {
				if (no.getId().equals(uri)) {
					continue;
				}
				RelationLinkModel link = new RelationLinkModel();
				if (no.getRole().equals(RoleRelation.father.getGroup())) {
					link.setId(no.getId());
					link.setFrom(no.getId());
					link.setTo(uri);
					links.add(link);
				} else {
					link.setId(no.getId());
					link.setFrom(uri);
					link.setTo(no.getId());
					links.add(link);
				}
			}
			result.put("nodes", nodes);
			result.put("links", links);
			jsonResult.put("data", result);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("ApiPersonController-dendrogram错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}

	/**
	 * 获取家规家训相关所有 人物 列表级相关workUri
	 */
	@ResponseBody
	@RequestMapping(value = "/getPersonsFromFamilyRules", method = RequestMethod.GET)
	public String getPersonsFromFamilyRules() {
		jsonResult = new HashMap<>();
		try {
			//预加载中获取persons	@author陈铭
			List<Map<String, String>> persons = PreloadApiPersonsFromFamilyRules.getInstance().getPersons();
			
			jsonResult.put("data", persons);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("PersonController-getPersonsFromFamilyRules错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
	
	/**
	 * 获取家规家训相关所有 姓氏 列表级相关workUri
	 */
	@ResponseBody
	@RequestMapping(value = "/getFamilyNamesFromFamilyRules", method = RequestMethod.GET)
	public String getFamilyNamesFromFamilyRules() {
		jsonResult = new HashMap<>();
		try {
			//预加载中获取familyNames	@author陈铭
			List<Map<String, String>> familyNames = PreloadApiPersonsFromFamilyRules.getInstance().getFamilyNames();
			
			jsonResult.put("data", familyNames);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("PersonController-getPersonsFromFamilyRules错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
	
	/**
	 * 获取家规家训相关所有 省份 列表级相关workUri
	 */
	@ResponseBody
	@RequestMapping(value = "/getProvsFromFamilyRules", method = RequestMethod.GET)
	public String getProvsFromFamilyRules() {
		jsonResult = new HashMap<>();
		try {
			//预加载中获取Provs	@author陈铭
			List<Map<String, String>> provsFromFamilyRules = PreloadApiPersonsFromFamilyRules.getInstance().getProvsFromFamilyRules();
			
			jsonResult.put("data", provsFromFamilyRules);
			return JSONArray.fromObject(jsonResult).toString();
		} catch (Exception e) {
			logger.info("PersonController-getPersonsFromFamilyRules错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return "error";
		}
	}
	
}
