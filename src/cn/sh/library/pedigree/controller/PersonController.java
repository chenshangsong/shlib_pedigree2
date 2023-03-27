package cn.sh.library.pedigree.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.RoleRelation;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.services.WorkService;
import cn.sh.library.pedigree.sysManager.model.RelationLinkModel;
import cn.sh.library.pedigree.sysManager.model.RelationNodeModel;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import flexjson.JSONSerializer;
import net.sf.json.JSONArray;

/**
 * Created by ly on 2014-12-18.
 */
@Controller
@RequestMapping("/service/person")
public class PersonController {
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(PersonController.class);

	public enum View {
		R("rdf"), V("view");
		private String view;

		private View(String view) {
			this.view = view;
		}

		public String getView() {
			return this.view;
		}
	}

	@Resource
	private PersonService personService;
	@Resource
	private WorkService workService;

	@ResponseBody
	@RequestMapping("/get")
	public Person personInfos(Model model, String uri, Boolean inference,
			View view) {
		String[] uris = uri.split(";");
		try {
			Person person = this.personService.get(uris[0], inference);
			person.setUri(uris[0]);
			model.addAttribute("person", person);
			if (view == View.V) {
				person.setWorks(workService.listWork4Person(uri, inference));
			}
			return person;
		} catch (Exception e) {
			logger.info("PersonController-get错误：" + DateUtilC.getNowDateTime()
					+ "----" + e);
		}
		return null;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "person/list";
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<String, Object> loadList(Model model, PersonSearchBean search,
			Boolean inference, Pager pager) {
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("pager", pager);
			result.put("persons",
					this.personService.list(search, inference, pager));
		} catch (Exception e) {
			logger.info("PersonController-list错误：" + DateUtilC.getNowDateTime()
					+ "----" + e);
		}
		return result;
	}

	@RequestMapping(value = "/familyName", method = RequestMethod.GET)
	public String familyName(Model model) {
		try {
			List<Map<String, String>> familyNames = this.personService
					.listFamilyNames();
			model.addAttribute("familyNames", familyNames);
			model.addAttribute("jsonData",
					new JSONSerializer().serialize(familyNames));
		} catch (Exception e) {
			logger.info("PersonController-familyName错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
		}

		return "person/familyName";
	}

	@ResponseBody
	@RequestMapping(value = "/personFamilyNames", method = RequestMethod.GET)
	public List<Map<String, String>> personFamilyNames() {
		try {
			List<Map<String, String>> list = this.personService
					.listPersonFamilyNames();
			return list;
		} catch (Exception e) {
			logger.info("PersonController-personFamilyNames错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
		}
		return null;
	}

	/**
	 * 谱系图
	 * 
	 * @param out
	 * @param uri
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/dendrogram", method = RequestMethod.GET)
	public Map<String, Object> getDendrogram(String out, String uri,
			boolean isMe) {
		try {
			String[] uris = uri.split(";");
			uri = uris[0];
			Map<String, Object> result = new HashMap<>();
			List<RelationNodeModel> nodes = new ArrayList<RelationNodeModel>();
			List<RelationLinkModel> links = new ArrayList<RelationLinkModel>();
			Map<String, String> mm = this.personService.getPxt(uri);
			RelationNodeModel node;

			// 本人
			if (isMe) {
				node = new RelationNodeModel();
				node.setId(mm.get("uri"));
				node.setTitle(mm.get("name"));
				node.setClick(false);
				node.setRole(RoleRelation.me.getGroup());
				node.setPerson(this.personService.get(mm.get("uri"), false));
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
			if (!out.equals(mm.get("father"))) {
				node = new RelationNodeModel();
				node.setId(mm.get("father"));
				node.setTitle(mm.get("fatherName"));
				node.setRole(RoleRelation.father.getGroup());
				node.setPerson(this.personService.get(mm.get("father"), false));
				nodes.add(node);
			}

			// 儿子
			if (!StringUtilC.isEmpty(mm.get("sonUris"))) {
				String[] sons = mm.get("sonUris").split(";");
				for (int i = 0; i < sons.length; i++) {
					if (out.equals(sons[i])) {
						continue;
					}
					node = new RelationNodeModel();
					node.setId(sons[i]);
					node.setTitle(mm.get("sonNames").split(";")[i]);
					node.setRole(RoleRelation.son.getGroup());
					Person thisPerson = this.personService.get(sons[i], false);
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

			JSONArray jsonNode = JSONArray.fromObject(nodes);
			result.put("nodes", jsonNode.toString());
			JSONArray jsonLink = JSONArray.fromObject(links);
			result.put("links", jsonLink.toString());

			return result;
		} catch (Exception e) {
			logger.info("PersonController-dendrogram错误："
					+ DateUtilC.getNowDateTime() + "----" + e);
			return null;
		}
	}
}
