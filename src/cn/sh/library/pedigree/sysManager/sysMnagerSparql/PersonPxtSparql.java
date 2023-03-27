package cn.sh.library.pedigree.sysManager.sysMnagerSparql;


import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;

/**
 * Created by yesonme on 15-12-1.
 */
public interface PersonPxtSparql extends BaseDao {
 // 获取普系统
 	public Map<String, String> getPxt(String uri);
}
