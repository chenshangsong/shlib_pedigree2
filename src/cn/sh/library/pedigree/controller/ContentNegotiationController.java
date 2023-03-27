package cn.sh.library.pedigree.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.WorkSparql;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 
 * @author chen
 *
 */
@Controller
@RequestMapping("/rdfdata/")
public class ContentNegotiationController extends BaseController {
	@Resource
	private CommonSparql commonSparql;
	@Resource
	private PlaceSparql placeSparql;
	@Resource
	private PersonSparql personSparql;
	@Resource
	private WorkSparql workSparql;
	@Resource
	private PersonService personService;

	@ResponseBody
	@RequestMapping(value = "/{f}", method = RequestMethod.GET)
	public String personFamilyNames(@PathVariable String f, String graph,
			String uri) {
		try {
			String jsonLDString = new String("");
			if (uri.contains("place")) {
				jsonLDString = StringUtilC.StreamToString(
						placeSparql.getTriples(uri, "JSON-LD")).replace("\r\n",
						"");
			} else {
				jsonLDString = StringUtilC.StreamToString(
						commonSparql.getTriples(graph, uri, "JSON-LD"))
						.replace("\r\n", "");
			}

			return jsonLDString;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

	@ResponseBody
	@RequestMapping(value = "/workRDF", method = RequestMethod.GET)
	public String workRDF(String title, String creator, String familyName,
			String place, String titleOfA, String org, String person, String des) {
		WorkSearchBean search = new WorkSearchBean();
		search.setTitle(StringUtilC.getNullEmpty(title));
		search.setCreator(StringUtilC.getNullEmpty(creator));
		search.setFamilyName(StringUtilC.getNullEmpty(familyName));
		search.setPlace(StringUtilC.getNullEmpty(place));
		search.setTang(StringUtilC.getNullEmpty(titleOfA));
		search.setNote(StringUtilC.getNullEmpty(des));
		search.setOrganization(StringUtilC.getNullEmpty(org));
		search.setPerson(StringUtilC.getNullEmpty(person));
		String jsonLDString = StringUtilC.StreamToString(
				workSparql.getTriples(search, "JSON-LD")).replace("\r\n", "");
		return jsonLDString;
	}

	/**
	 * 获取人员URI
	 * 
	 * @param model
	 * @param search
	 * @param inference
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/personUri", method = RequestMethod.GET)
	public String personUri(String name) {
		Pager pager = new Pager();
		pager.setPageCount(0);
		pager.setPageSize(90);
		pager.setPageth(1);
		PersonSearchBean search = new PersonSearchBean();
		search.setName(name);
		List<Person> list = this.personService.list(search, false, pager);
		if (list != null && list.size() > 0) {
			return list.get(0).getUri().trim();
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/personRDF", method = RequestMethod.GET)
	public String personRDF(String familyName, String personName,
			String personUri) {
		try {
			PersonSearchBean search = new PersonSearchBean();
			search.setFamilyName(StringUtilC.getNullEmpty(familyName));
			search.setName(StringUtilC.getNullEmpty(personName));
			search.setUri(StringUtilC.getNullEmpty(personUri));
			String jsonLDString = StringUtilC.StreamToString(personSparql
					.getPersons4API(search, "JSON-LD"));
			return jsonLDString;
		} catch (Exception e) {
			System.out.println(e);
			return null;
			// TODO: handle exception
		}

	}
}
