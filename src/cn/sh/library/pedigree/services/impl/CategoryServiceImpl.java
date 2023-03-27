package cn.sh.library.pedigree.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.services.CategoryService;
import cn.sh.library.pedigree.utils.RDFUtils;

/**堂号
 * @author chenss
 * @date 2015/1/5 0005
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl implements CategoryService {

    @Resource
    private CommonSparql cs;

	@Override
	public  List<Map<String, String>> list(String g, String ns, String val, String type) {
		// TODO Auto-generated method stub
		 return RDFUtils.transformListMap(cs.getSpecPropValue(g, ns, val, type));
	}



}
