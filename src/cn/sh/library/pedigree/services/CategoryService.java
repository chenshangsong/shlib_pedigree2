package cn.sh.library.pedigree.services;


import java.util.List;
import java.util.Map;

/**
 * 取词词表
 * @author chenss
 *
 */
public interface CategoryService extends BaseService {

   /**
    * 
    * @param g graph
    * @param ns bf:Category
    * @param val 版本
    * @param type bf：categoryValue
    * @return
    */
    public  List<Map<String, String>> list(String g,String ns,String val,String type);

   
}
