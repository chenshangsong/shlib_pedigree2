package cn.sh.library.pedigree.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.services.TitleService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.PersonSparql;
import cn.sh.library.pedigree.sparql.PlaceSparql;
import cn.sh.library.pedigree.sparql.TitleSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * @author chenss
 * @date 2015/1/5 0005
 */
@Service
public class TitleServiceImpl extends BaseServiceImpl implements TitleService {

    @Resource
    private TitleSparql titleSparql;
    @Resource
    private PlaceSparql placeSparql;
    @Resource
    private PersonSparql personSparql;
    @Resource
    private BaseinfoSparql baseinfoSparql;


    @Override
    public List<Map<String, String>> list(String search, Pager pager) {
        QueryResult<Map<String, Object>> queryResult = this.titleSparql.getTitles(search, pager.getStartIndex(), pager.getPageSize());
        pager.calcPageCount(queryResult.getTotalrecord());
        return RDFUtils.transformListMap(queryResult.getResultList());
    }


}
