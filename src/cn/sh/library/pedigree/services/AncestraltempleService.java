package cn.sh.library.pedigree.services;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.dto.Pager;

/**
 * 堂号
 * @author chenss
 *
 */
public interface AncestraltempleService extends BaseService {

    /**
     * 查询堂号列表
     * @param search
     * @param pager
     * @return
     */
    public List<Map<String, String>> list(AncTempSearchBean search, Pager pager);

    /**
     * 查询堂号列表:按堂号名称去重
     * @param search
     * @param pager
     * @return
     */
    public List<Map<String, String>> listForShiGuang(AncTempSearchBean search, Pager pager);
}
