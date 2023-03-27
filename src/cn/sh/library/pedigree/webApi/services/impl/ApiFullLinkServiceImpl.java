package cn.sh.library.pedigree.webApi.services.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.bean.FullLink4ESJPSearchBean;
import cn.sh.library.pedigree.fullContentLink.FullLink4ESJP;
import cn.sh.library.pedigree.webApi.services.ApiFullLinkService;

/**
 * @author 陈铭
 */

@Service
public class ApiFullLinkServiceImpl implements ApiFullLinkService {

	@Override
	public Map<String,String> getFulltextLink(FullLink4ESJPSearchBean fullLink4ESJPSearchBean) {
		return FullLink4ESJP.GetFullTextImg(fullLink4ESJPSearchBean) ;
	}
}
