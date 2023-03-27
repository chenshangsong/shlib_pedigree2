package cn.sh.library.pedigree.sysManager.sysMnagerSparql;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * Created by yesonme on 15-12-1.
 */
@Repository
@GraphDefine(name = Constant.GRAPH_PERSON)
public class PersonPxtSparqlImpl extends BaseDaoImpl implements PersonPxtSparql {

	@Override
	public Map<String, String> getPxt(String uri) {
		List<Map<String, Object>> _list = SparqlExecution.vQuery(graph,
				RdfQuery.PERSON_PXT_SELECT.toString(uri), "uri", "name", "father",
				"fatherName", "wifeNames", "sonUris", "sonNames");
		if (_list != null && _list.size() > 0) {
			return RDFUtils.transformListMap(_list).get(0);
		}
		return null;
	}

}
