package cn.sh.library.pedigree.webApi.services.impl;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.controller.RDFController;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sysManager.sysMnagerSparql.PersonPxtSparql;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiPersonService;
import cn.sh.library.pedigree.webApi.sparql.ApiPersonSparql;

/**
 * Created by ly on 2014-12-18.
 */
@Service
public class ApiPersonServiceImpl extends BaseServiceImpl implements ApiPersonService {
	@Resource
	private PersonSparql personSparql;
	@Resource
	private ApiPersonSparql apipersonSparql;
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
		QueryResult<Map<String, Object>> queryResult = this.personSparql.getPersons(search, pager.getStartIndex(),
				pager.getPageSize());
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
			// list = this.personSparql.getInfos4Person(uri,
			// true);切换为新作接口。chenss20170821
			Person person = new Person();
			if (uri.contains("jp")) {
				list = this.personSparql.getInfos4Person(uri);
				person = transform(list);
				return person;
			} else {
				Map<String, Object> personList = RDFUtils.getMap((ArrayList) this.personSparql.getInfos4Person(uri),
						null, new String[] {});

				Map<String, Object> img = RDFUtils.getMap(this.personSparql.getImg(uri), null, new String[] {});

				if (img != null && img.size() > 0) {

					person.setPersonImgPath("//img.library.sh.cn/person/"
							+ uri.substring(uri.length() - 16, uri.length()) + ".jpg");

				}

				person.setName(RDFUtils.toString(personList.get("name")));
				person.setBirthday(StringUtilC.getString(personList.get("birthday")));
				person.setCourtesyName(StringUtilC.getString(personList.get("courtesyName")));
				person.setDeathday(StringUtilC.getString(personList.get("deathday")));
				person.setPseudonym(StringUtilC.getString(personList.get("pseudonym")));
				person.setUri(uri);
				// 人物小传，取一个。
				person.setSpeciality(StringUtilC.getString(personList.get("speciality")));
				//person.setDynasty(getCdNameByUri(StringUtilC.getString(personList.get("temporal"))));
				Object _bir = personList.get("briefBiography");
				if (_bir != null) {
					List _briList = (ArrayList) personList.get("briefBiography");
					person.setDescription(StringUtilC.getString(_briList.get(0)));
				}
		    	person.setGender(StringUtilC.getString(personList.get("gender")));
				return person;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

	// 根据URI，获取朝代名称
	private String getCdNameByUri(String uri) {
		if (StringUtil.isBlank(uri)) {
			return null;
		}
		String cdName = "";
		switch (uri) {
		case "http://data.library.sh.cn/authority/temporal/pizh5ypd5d17u795":
			cdName = "宋";
			break;
		case "http://data.library.sh.cn/authority/temporal/qafeg12mhoqfidor":
			cdName = "元";
			break;
		case "http://data.library.sh.cn/authority/temporal/yex4deivsad41p9q":
			cdName = "明";
			break;
		case "http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q":
			cdName = "清";
			break;
		case "http://data.library.sh.cn/authority/temporal/7sl8ulx4ouipdbg2":
			cdName = "民国";
			break;
		case "http://data.library.sh.cn/authority/temporal/j6mijtwmcfzq3t8b":
			cdName = "当代";
			break;
		default:
			cdName = "";

		}
		return cdName;
		/*
		 * Map<String,String> _map = new HashMap<String,String>();
		 * _map.put("http://data.library.sh.cn/authority/temporal/pizh5ypd5d17u795",
		 * "宋");
		 * _map.put("http://data.library.sh.cn/authority/temporal/qafeg12mhoqfidor",
		 * "元");
		 * _map.put("http://data.library.sh.cn/authority/temporal/yex4deivsad41p9q",
		 * "明");
		 * _map.put("http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q",
		 * "清");
		 * _map.put("http://data.library.sh.cn/authority/temporal/7sl8ulx4ouipdbg2",
		 * "民国");
		 * _map.put("http://data.library.sh.cn/authority/temporal/j6mijtwmcfzq3t8b",
		 * "当代"); return _map.get(uri);
		 */
	}

	@Override
	public List<Map<String, String>> listFamilyNames(String fname) {
		if(StringUtil.isBlank(fname)) {
			return RDFUtils.transformListMap(this.baseinfoSparql.getAllFamilyNames());
		}
		return RDFUtils.transformListMap(this.baseinfoSparql.getFamilyNames(fname));
	}

	@Override
	public List<Map<String, String>> listPersonFamilyNames() {
		return RDFUtils.transformListMap(this.baseinfoSparql.getPersonFamilyNames());
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
				// TODO:图片路径待取
				person.setPersonImgPath("");

				if (Uris.length > 20) {
					Uri = Uri.substring(0, StringUtilC.getFromIndex(Uri, ";", 20));
				}
				// 陈尚松 2018-02-06
				// person.setUri(Uri.split(";")[0]);
				person.setUri(Uri);
				person.setRelatedWork(RDFUtils.toString(map.get("work")));
				return person;
			} else if (obj instanceof List) {
				Person person = new Person();
				List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
				for (Map<String, Object> map : list) {
					String s = RDFUtils.toString(map.get("s"));
					String p = RDFUtils.toString(map.get("p"));
					String o = map.get("o").toString();
					if (StringUtils.isEmpty(person.getUri()) && s.contains("/person/")) {
						person.setUri(s);
					}
					if (p.equalsIgnoreCase("rdf:type")) {
						person.setType(o);
					} else if (p.equalsIgnoreCase("shl:endYear")) {
						person.setEndYear(StringUtilC.getString(RDFUtils.getValue(o)));
					} else if (p.equalsIgnoreCase("shl:beginYear")) {
						person.setBeginYear(StringUtilC.getString(RDFUtils.getValue(o)));
					} else if (p.equalsIgnoreCase("shl:temporalValue")) {
						person.setDynasty(o);
					} else if (p.equalsIgnoreCase("shl:courtesyName")) {// 字
						person.setCourtesyName(o);
					} else if (p.equalsIgnoreCase("shl:relatedWork")) {// shl:relatedWork
						person.setRelatedWork(o);

					} else if (p.equalsIgnoreCase("shl:pseudonym")) {// 号
						person.setPseudonym(o);
					} else if (p.equalsIgnoreCase("shl:orderOfSeniority")) {// 行
						person.setOrderOfSeniority(o);
					} else if (p.equalsIgnoreCase("shl:posthumousName")) {// 谥
						person.setPosthumousName(o);
					} else if (p.equalsIgnoreCase("shl:birthday")) {// 生
						person.setBirthday(o);
					} else if (p.equalsIgnoreCase("shl:deathday")) {// 卒
						person.setDeathday(o);
					} else if (p.equalsIgnoreCase("shl:description")) {// 人物描述
						person.setDescription(o);
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
			// 陈尚松 2018-02-06
			person.setUri(RDFUtils.toString(map.get("uri")).split(";")[0]);
			return person;
		}
		return null;
	}

	@Override
	public List<Person> list(PersonSearchBean search, Integer tag, String type, Pager pager) {
		try {
			// 加入推理，去除重复人名
			QueryResult<Map<String, Object>> queryResult = this.personSparql.getPersons(search, tag, type,
					pager.getStartIndex(), pager.getPageSize());
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
			QueryResult<Map<String, Object>> queryResult = this.personSparql.getPersonsInHome(familyName_uri,
					pager.getStartIndex(), pager.getPageSize());
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
		return PersonPxtSparql.getPxt(uri);
	}

	@Override
	public List<Map<String, String>> listFnamePlaceList() {
		List<Map<String, String>> _provFUriList = new ArrayList<>();
		// 省做为 key,存放姓氏列表
		List<Map<String, String>> _provMapList = apipersonSparql.getProvList();
		for (Map<String, String> map : _provMapList) {
			_provFUriList.addAll(apipersonSparql.getPlacesInfos4Person(map.get("pro")));

		}
		// 按照数量降序
		Collections.sort(_provFUriList, new Comparator<Map<String, String>>() {
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				return StringUtilC.getInteger(o2.get("count")).compareTo(StringUtilC.getInteger(o1.get("count")));
			}
		});
		return _provFUriList;
	}

	@Override
	public List<Map<String, String>> getPersonsFromFamilyRules() {
		return apipersonSparql.getPersonsFromFamilyRules();
	}

	/**
	 * @author 陈铭 获取所有的省份
	 */
	@Override
	public List<String> getProvList() {
		List<String> list = new ArrayList<String>();
		List<Map<String, String>> provMapList = apipersonSparql.getProvList();
		for (Map<String, String> map : provMapList) {
			list.add(StringUtilC.getCht(map.get("pro")));
		}
		return list;
	}

	@Override
	public List<Map<String, String>> getProvsFromFamilyRules() {
		List<Map<String, String>> _mapList = apipersonSparql.getProvsFromFamilyRules();
		for (Map<String, String> map : _mapList) {
			map.put("province", StringUtilC.getCht(map.get("province")));
		}
		return _mapList;
	}

}
