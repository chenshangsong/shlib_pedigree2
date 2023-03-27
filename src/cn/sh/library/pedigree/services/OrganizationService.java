package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dto.Pager;

/**
 * 机构
 * @author chenss
 *
 */
public interface OrganizationService extends BaseService {

    /**
     * 查询机构列表
     * @param search
     * @param pager
     * @return
     */
    public List<Map<String, String>> list(String search, Pager pager);

    /**
     * 查询机构列表编目系统使用
     * @param search
     * @param pager
     * @return
     */
    public List<Map<String, String>> listForBm(String search, Pager pager);
}
