package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dto.ClassView;
import cn.sh.library.pedigree.dto.Vocab;

/**
 * Created by Yi on 2014/10/27 0027.
 */
public interface VocabService extends BaseService {

    /**
     * 加载所有类
     * @return
     */
    public List<Vocab> listClasses();

    /**
     * 加载所有属性
     * @return
     */
    public List<Vocab> listProperties();

    /**
     * 加载类
     * @return
     */
    public List<ClassView> listClassViews(String parent);

    /**
     * 加载类属性
     * @param className
     * @return
     */
    public Map<String, Map<String, String>> listClassProperties(String className);

    /**
     * 加载model view classes
     * @return
     */
    public List<String> listModelClasses();

    /**
     * 获取类链接地址
     * @param className
     * @return
     */
    public String getLink(String className);

    /**
     * 获取描述信息
     * @param className
     * @return
     */
    public String getComment(String className);

    /**
     * 获取rdf数据
     * @return
     */
    public String rdfData();

    /**
     * 获取rdf数据
     * @return
     */
    public String rdfData(String term);

    /**
     * 生成class图表
     * @param className
     * @return
     */
    public String modelGraph(String className, String uri);
}
