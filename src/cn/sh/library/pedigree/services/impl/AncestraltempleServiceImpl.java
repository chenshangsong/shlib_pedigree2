package cn.sh.library.pedigree.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.AncestraltempleService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**堂号
 * @author chenss
 * @date 2015/1/5 0005
 */
@Service
public class AncestraltempleServiceImpl extends BaseServiceImpl implements AncestraltempleService {

    @Resource
    private BaseinfoSparql baseinfoSparql;


    @Override
    public List<Map<String, String>> list(AncTempSearchBean search, Pager pager) {
        QueryResult<Map<String, Object>> queryResult = this.baseinfoSparql.getAncestralTemple(search, pager.getStartIndex(), pager.getPageSize());
        pager.calcPageCount(queryResult.getTotalrecord());
        return RDFUtils.transformListMap(queryResult.getResultList());
    }

    @Override
    public List<Map<String, String>> listForShiGuang(AncTempSearchBean search, Pager pager) {
        QueryResult<Map<String, Object>> queryResult = this.baseinfoSparql.getAncestralTempleForShiGuang(search, pager.getStartIndex(), pager.getPageSize());
        pager.calcPageCount(queryResult.getTotalrecord());
        return RDFUtils.transformListMap(queryResult.getResultList());
    }

}
