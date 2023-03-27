package cn.sh.library.pedigree.sysManager.sysMnagerSparql;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.rdf.model.Model;

import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.dao.impl.BaseDaoImpl;
import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * Created by yesonme on 15-12-1.
 */
@Repository
@GraphDefine(name = Constant.GRAPH_PLACE)
public class ForeignPlacesSparqlImpl extends BaseDaoImpl implements
		ForeignPlacesSparql {
	@Override
	public List<Map<String, String>> getAllPlaces() {
		return RDFUtils.transformListMap(SparqlExecution.vQuery(graph,
				RdfQuery.RDF_SELECT_PLACES.toString(), "uri", "prov", "city",
				"county"));
	}

	@Override
	public List<Map<String, String>> getForeignPlaces() {
		try {
			List<Map<String, String>> list = RDFUtils
					.transformListMap(SparqlExecution.vQuery(graph,
							RdfQuery.RDF_SELECT_PLACES_FOREIGN.toString(),
							"uri", "label", "country"));
			return list;
		} catch (Exception e) {
			System.out.println("错误："+e.getMessage());
		}
		return null;
	}

	@Override
	public OutputStream getRDF(String g, String s, String t) {
		Model m = SparqlExecution.construct(graph,
				RdfQuery.RDF_CONSTRUCT_TEMPORAL.toString(s));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		m.write(stream, t);

		return stream;
	}

	@Override
	public List<Map<String, String>> getRDF(String time_uri) {
		String query = Namespace.getNsPrefixString() + "SELECT ?s ?p ?o "
				+ "WHERE {" + "   ?s ?p ?o ." + "FILTER (STR(?s) = '"
				+ time_uri + "')" + "}";
		return RDFUtils.transformListMap(SparqlExecution.vQuery(graph, query,
				new String[] { "s", "p", "o" }));

	}

	@Override
	public List<Map<String, String>> getForeignPlaces(String keyword) {
		String sqlwhere="";
		if(!StringUtilC.isEmpty(keyword)){
			sqlwhere+= "FILTER (CONTAINS(?label, '"+ keyword + "'))";
		}
		String sql = Namespace.getNsPrefixString()
				+ "SELECT DISTINCT ?uri ?label ?country " + "WHERE {"
				+ "   ?uri a shl:Place ; " + "        bf:label ?label ; "
				+ "        shl:country ?country ."
				+ "FILTER NOT EXISTS{?uri shl:province ?prov .} "
				+ "FILTER (lang(?label) = 'chs')"
				+sqlwhere
				+ "FILTER (lang(?country) = 'chs')" + "FILTER (?label != '全国'@chs)"
				+ "}";
		List<Map<String, String>> list = RDFUtils
				.transformListMap(SparqlExecution.vQuery(graph,sql,
						"uri", "label", "country"));
		return list;
	}
}
