package cn.sh.library.pedigree.sysManager.sysMnagerSparql;


import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;

/**
 * Created by yesonme on 15-12-1.
 */
public interface ItemSparql extends BaseDao {
 	public List<Map<String, String>> getItemByDoi(String doi);
}
