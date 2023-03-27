package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dto.Pager;

/**
 * 朝代
 * @author liuyi
 * @date 2014/12/27 0027
 */
public interface DynastyService {

    /**
     * 获取朝代列表
     * @return
     */
    public List<Map<String, String>> list();

    /**
     * 获取朝代名人
     * @param uri
     * @param pager
     * @return
     */
    public List<Map<String, String>> listPersons(String uri, Pager pager);

    /**
     * 获取朝代work
     * @param uri
     * @param pager
     * @return
     */
    public List<Map<String, String>> listWorks(String uri, Pager pager);

    public List<Map<String, String>> listTemps4TL();
}
