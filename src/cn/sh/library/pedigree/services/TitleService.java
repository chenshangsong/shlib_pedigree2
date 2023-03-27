package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dto.Pager;

/**
 * 谱名
 * @author chenss
 *
 */
public interface TitleService extends BaseService {

    /**
     * 查询谱籍列表
     * @param search
     * @param pager
     * @return
     */
    public List<Map<String, String>> list(String search, Pager pager);

   
}
