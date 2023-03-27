package cn.sh.library.pedigree.webApi.services;

import java.util.Map;

import cn.sh.library.pedigree.bean.FullLink4ESJPSearchBean;
import cn.sh.library.pedigree.services.BaseService;

/**
 * @author 陈铭
 */
public interface ApiFullLinkService extends BaseService {

	Map<String,String> getFulltextLink(FullLink4ESJPSearchBean fullLink4ESJPSearchBean);

	
}
