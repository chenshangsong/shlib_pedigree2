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
@GraphDefine(name = Constant.GRAPH_ITEM)
public class ItemSparqlImpl extends BaseDaoImpl implements ItemSparql {

	@Override
	public List<Map<String, String>> getItemByDoi(String doi) {
		String query = Namespace.getNsPrefixString()
				+ "SELECT ?uri ?doi ?accessLevel ?hasFullImg"
				+ " WHERE {?uri a bf:Item;shl:DOI ?doi; shl:accessLevel ?accessLevel optional{?uri shl:hasFullImg ?hasFullImg} . filter regex(?doi, '"
				+ doi + "')}";
		return RDFUtils.transformListMap(SparqlExecution.vQuery(graph, query,
				new String[] { "uri", "doi", "accessLevel","hasFullImg" }));

	}
}
