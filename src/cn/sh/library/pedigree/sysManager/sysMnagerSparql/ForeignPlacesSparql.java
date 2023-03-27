package cn.sh.library.pedigree.sysManager.sysMnagerSparql;


import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;

/**
 * Created by yesonme on 15-12-1.
 */
public interface ForeignPlacesSparql extends BaseDao {
    //获取中国所有地域
    public List<Map<String, String>> getAllPlaces();

    //获取海外谱籍地
    public List<Map<String, String>> getForeignPlaces();
    //获取海外谱籍地
    public List<Map<String, String>> getForeignPlaces(String keyword);
    public OutputStream getRDF(String g, String s, String t);
 // 获取RDF
 	public List<Map<String, String>> getRDF(String s);
}
