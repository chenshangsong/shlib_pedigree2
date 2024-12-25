package cn.sh.library.pedigree.services.impl;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.controller.RDFController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.PersonService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.PersonPxtSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * Created by ly on 2014-12-18.
 */
@Service
public class PersonServiceImpl extends BaseServiceImpl implements PersonService {
	@Resource
	private PersonSparql personSparql;
	@Resource
	private PersonPxtSparql PersonPxtSparql;
	@Resource
	private CommonSparql commonSparql;
	@Resource
	private BaseinfoSparql baseinfoSparql;

	@Override
	public List list(PersonSearchBean search, Boolean inference, Pager pager) {
		if (inference == null)
			inference = false;
		// 加入推理，去除重复人名
		QueryResult<Map<String, Object>> queryResult = this.personSparql
				.getPersons(search, pager.getStartIndex(), pager.getPageSize());
		pager.calcPageCount(queryResult.getTotalrecord());

		List<Map<String, Object>> list = queryResult.getResultList();
		List<Person> resultList = new ArrayList<>();
		for (Map<String, Object> obj : list) {
			resultList.add(transform(obj));
		}
		return resultList;
	}

	@Override
	public Person get(String uri, Boolean inference) {
		inference = inference == null ? false : inference;
		List list = null;
		try {
			Person person = new Person();
			if (uri.contains("jp")) {
				list = this.personSparql.getInfos4Person(uri);
				person = transform(list);
				return person;
			} else {
				Map<String, Object> personList = RDFUtils.getMap(
						(ArrayList) this.personSparql.getInfos4Person(uri),
						null, new String[] {});

				person.setName(RDFUtils.toString(personList.get("name")));
				person.setBirthday(StringUtilC.getString(personList
						.get("birthday")));
				person.setCourtesyName(StringUtilC.getString(personList
						.get("courtesyName")));
				// if (!StringUtilC.isEmpty(StringUtilC.getString(personList
				// .get("briefBiography")))) {
				// List des = (List) personList.get("briefBiography");
				// person.setDescription((String) des.get(0));
				// }

				person.setDeathday(StringUtilC.getString(personList
						.get("deathday")));
				person.setPseudonym(StringUtilC.getString(personList
						.get("pseudonym")));
				person.setUri(uri);

				return person;
			}

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	@Override
	public List<Map<String, String>> listFamilyNames() {
		return RDFUtils.transformListMap(this.baseinfoSparql
				.getAllFamilyNames());
	}

	@Override
	public List<Map<String, String>> listPersonFamilyNames() {
		return RDFUtils.transformListMap(this.baseinfoSparql
				.getPersonFamilyNames());
	}

	@Override
	public String rdf(String uri) {
		ByteArrayOutputStream bos = (ByteArrayOutputStream) this.commonSparql
				.getTriples(RDFController.getGraphName(uri), false, uri);
		byte[] data = bos.toByteArray();
		String rdfData = new String(data, Charset.forName("utf-8"));
		return rdfData;
	}

	private Person transform(Object obj) {
		try {
			if (obj instanceof Map) {
				Map<String, Object> map = (Map) obj;
				Person person = new Person();
				person.setName(RDFUtils.toString(map.get("name")));
				String Uri = RDFUtils.toString(map.get("uri"));
				String Uris[] = Uri.split(";");
				if (Uris.length > 20) {
					Uri = Uri.substring(0,
							StringUtilC.getFromIndex(Uri, ";", 20));
				}
				// 陈尚松 2018-02-06
				// person.setUri(Uri.split(";")[0]);
				person.setUri(Uri);
				person.setRelatedWork(RDFUtils.toString(map.get("work")));
				return person;
			} else if (obj instanceof List) {
				Person person = new Person();
				List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
				person.setTriples(new ArrayList<Map<String, String>>());
				for (Map<String, Object> map : list) {
					String s = RDFUtils.toString(map.get("s"));
					String p = RDFUtils.toString(map.get("p"));
					// String o = RDFUtils.toString(map.get("o"));
					String o = map.get("o").toString();
					Map<String, String> tmp = new HashMap<>();
					tmp.put("s", s);
					tmp.put("p", p);
					tmp.put("o", o);
					person.getTriples().add(tmp);
					if (StringUtils.isEmpty(person.getUri())
							&& s.contains("/person/")) {
						person.setUri(s);
					}

					if (p.equalsIgnoreCase("rdf:type")) {
						person.setType(o);
					} else if (p.equalsIgnoreCase("shl:endYear")) {
						person.setEndYear(StringUtilC.getString(RDFUtils
								.getValue(o)));
					} else if (p.equalsIgnoreCase("shl:beginYear")) {
						person.setBeginYear(StringUtilC.getString(RDFUtils
								.getValue(o)));
					} else if (p.equalsIgnoreCase("shl:temporalValue")) {
						person.setDynasty(o);
					} else if (p.equalsIgnoreCase("shl:courtesyName")) {// 字
						person.setCourtesyName(o);
					} else if (p.equalsIgnoreCase("shl:relatedWork")) {// shl:relatedWork
						person.setRelatedWork(o);

					}

					else if (p.equalsIgnoreCase("shl:pseudonym")) {// 号
						person.setPseudonym(o);
					} else if (p.equalsIgnoreCase("shl:orderOfSeniority")) {// 行
						person.setOrderOfSeniority(o);
					} else if (p.equalsIgnoreCase("shl:posthumousName")) {// 谥
						person.setPosthumousName(o);
					} else if (p.equalsIgnoreCase("shl:birthday")) {// 生
						person.setBirthday(o);

					} else if (p.equalsIgnoreCase("shl:deathday")) {// 卒
						person.setDeathday(o);
					} else if (p.equalsIgnoreCase("bf:label")) {
						if (s.toString().contains("/person/")) {
							if (o.endsWith("@en")) {
								person.setEnName(o.substring(0, o.length() - 3));
							} else if (o.endsWith("@cht")) {
								person.setName(RDFUtils.getValue(o));
							} else {
								person.setName(RDFUtils.getValue(o));
							}
						}
					}
				}
				return person;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * 纂修者翻译 chenss
	 * 
	 * @param obj
	 * @return
	 */
	private Person transformZxz(Object obj) {
		if (obj instanceof Map) {
			Map<String, Object> map = (Map) obj;
			Person person = new Person();
			person.setName(RDFUtils.toString(map.get("chs")));
			person.setUri(RDFUtils.toString(map.get("uri")));
			return person;
		}
		return null;
	}

	@Override
	public List<Person> list(PersonSearchBean search, Integer tag, String type,
			Pager pager) {
		try {
			// 加入推理，去除重复人名
			QueryResult<Map<String, Object>> queryResult = this.personSparql
					.getPersons(search, tag, type, pager.getStartIndex(),
							pager.getPageSize());
			pager.calcPageCount(queryResult.getTotalrecord());
			List<Map<String, Object>> list = queryResult.getResultList();
			List<Person> resultList = new ArrayList<>();
			for (Map<String, Object> obj : list) {
				resultList.add(transformZxz(obj));
			}
			return resultList;
		} catch (Exception o) {
			System.out.println(o);
		}
		return null;
	}

	@Override
	public List<Person> getPersonsInHome(String familyName_uri, Pager pager) {
		try {
			// 加入推理，去除重复人名
			QueryResult<Map<String, Object>> queryResult = this.personSparql
					.getPersonsInHome(familyName_uri, pager.getStartIndex(),
							pager.getPageSize());
			pager.calcPageCount(queryResult.getTotalrecord());
			List<Person> resultList = new ArrayList<>();
			for (Map<String, Object> obj : queryResult.getResultList()) {
				resultList.add(transform(obj));
			}
			return resultList;
		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
		}
		return null;
	}

	@Override
	public Map<String, String> getPxt(String uri) {
		// TODO Auto-generated method stub
		return PersonPxtSparql.getPxt(uri);
	}
}
