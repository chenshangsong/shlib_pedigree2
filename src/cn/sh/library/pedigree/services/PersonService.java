package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Person;

/**
 * Created by ly on 2014-12-18.
 */
public interface PersonService extends BaseService {

    /**
     * 按首字母查询人
     * @param search Search Bean
     * @return
     */
    public List<Person> list(PersonSearchBean search, Boolean inference, Pager pager);
    /**
     * 首页姓氏查询人员
     * @param search
     * @param inference
     * @param pager
     * @return
     */
    public List<Person> getPersonsInHome(String familyName_uri, Pager pager);
    /**
     * 新增查询方法chenss
     * @param search Search Bean
     * @return
     */
    public List<Person> list(PersonSearchBean search,Integer tag,String type, Pager pager);

    /**
     * 根据uri获取person
     * @param uri
     * @return
     */
    public Person get(String uri, Boolean inference);

    /**
     * 获取所有的姓氏
     * @return
     */
    public List<Map<String, String>> listFamilyNames();

    /**
     * 获取具有Person的姓氏
     * @return
     */
    public List<Map<String, String>> listPersonFamilyNames();

    /**
     * 根据uri获取rdf数据
     * @param uri
     * @return
     */
    public String rdf(String uri);
    /**
     * 获取所有的姓氏普系统
     * @return
     */
    public Map<String, String> getPxt(String uri);
}
