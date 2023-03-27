package cn.sh.library.pedigree.sysManager.sysMnagerSparql;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

/**
 * Created by Administrator on 14-3-4.
 */
public class SparqlExecution {
	// Remote federated query
	public static ArrayList vQuery(String endpoint, String query,
			String... objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint,
				sparql);

		ResultSet rs = qe.execSelect();

		return extract(rs, objects);
	}

	// Query sparql in Virtuoso linked database directly.
	public static ArrayList vQuery(VirtGraph set, String query,
			String... objects) {
		// System.out.println(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				query, set);
		ResultSet rs = vqe.execSelect();

		return extract(rs, objects);
	}

	// Service
	public static ArrayList jQuery(String service, String query,
			String... objects) {
		Query sparql = QueryFactory.create(query);
		ARQ.getContext().setTrue(ARQ.useSAX);
		// Executing SPARQL Query and pointing to the DBpedia SPARQL Endpoint
		QueryExecution qexec = QueryExecutionFactory.sparqlService(service,
				sparql);
		// Retrieving the SPARQL Query results
		ResultSet rs = qexec.execSelect();

		return extract(rs, objects);
	}

	// Construct
	public static Model construct(Model set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);

		return qe.execConstruct();
	}

	// Construct
	public static Model construct(VirtGraph set, String query) {
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				query, set);

		return vqe.execConstruct();
	}

	// Update
	public static void update(VirtGraph set, String query) {
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, set);
		vur.exec();
	}

	// Update
	public static void update(Model model, String query) {
		model.enterCriticalSection(Lock.WRITE);

		try {
			UpdateRequest req = UpdateFactory.create(query);
			UpdateAction.execute(req, model);
		} finally {
			model.leaveCriticalSection();
		}
	}

	// Ask
	public static boolean ask(VirtGraph set, String query) {
		ArrayList results = vQuery(set, query);

		if (results.size() > 0) {
			return true;
		}

		return false;
	}

	// Query all triples
	public static ArrayList jQuery(VirtModel set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		return extract(rs, "s", "p", "o");
	}

	// Query sparql in Virtuoso linked database with Jena API.
	public static ArrayList jQuery(VirtModel set, String query,
			String... objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		return extract(rs, objects);
	}

	// Query sparql in Virtuoso linked database with Jena API.
	public static ArrayList jQuery(Model set, String query, String... objects) {
		set.enterCriticalSection(Lock.READ);

		try {
			Query sparql = QueryFactory.create(query);
			QueryExecution qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();

			return extract(rs, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	// Query sparql in Virtuoso linked database directly.
	public static ArrayList vQuery(Model set, String query, String... objects) {
		set.enterCriticalSection(Lock.READ);

		try {
			QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query,
					set);
			ResultSet rs = vqe.execSelect();

			return extract(rs, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	// Query sparql in Virtuoso linked database with limited size in group.
	public static ArrayList vQuery(Model set, String query, int limit,
			String key, String... objects) {
		set.enterCriticalSection(Lock.READ);

		try {
			QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query,
					set);
			ResultSet rs = vqe.execSelect();

			return extractLimited(rs, limit, key, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	// Query sparql with Json format in Virtuoso linked database with Jena API.
	public static OutputStream jQueryJson(VirtModel set, String query) {
		ByteArrayOutputStream bos;

		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		bos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(bos, rs);

		return bos;
	}

	// Query sparql with Json format in Virtuoso linked database with Jena API.
	public static OutputStream jQueryRDF(VirtModel set, String query) {
		QueryExecution qe = QueryExecutionFactory.create(query, set);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSet results = qe.execSelect();
		ResultSetFormatter.outputAsRDF(baos, "RDF/XML-ABBREV", results);

		return baos;
	}

	// Query sparql with Json format in Virtuoso linked database directly.
	public static OutputStream vQueryJson(VirtModel set, String query) {
		ByteArrayOutputStream bos;

		QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
		ResultSet rs = vqe.execSelect();

		bos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(bos, rs);

		return bos;
	}

	// Query sparql with Json format in Virtuoso linked database directly.
	public static OutputStream vQueryJson(VirtGraph set, String query) {
		ByteArrayOutputStream bos;

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				query, set);
		ResultSet rs = vqe.execSelect();

		bos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(bos, rs);

		return bos;
	}

	// Extract triples' information in ResultSet according to required objects.
	private static ArrayList extract(ResultSet resultSet, String... objects) {
		ArrayList list = new ArrayList();

		while (resultSet.hasNext()) {
			HashMap rm = new HashMap();
			QuerySolution row = resultSet.nextSolution();

			if (null != objects) {
				for (int i = 0; i < objects.length; i++) {
					RDFNode node = row.get(objects[i]);
					rm.put(objects[i], node);
				}
			}

			list.add(rm);
		}

		return list;
	}

	// Extract limited size results in every group.
	private static ArrayList extractLimited(ResultSet resultSet, int limit,
			String key, String... objects) {
		ArrayList list = new ArrayList();
		Map<Object, Integer> map = new HashMap<>();

		int count = 0;
		while (resultSet.hasNext()) {
			HashMap rm = new HashMap();
			QuerySolution row = resultSet.nextSolution();

			String tag = row.get(key).toString();

			if (!map.containsKey(tag)) {
				count = 0;
				map.put(tag, count);
			}

			if (map.get(tag) < limit) {
				map.put(tag, map.get(row.get(key)));

				if (null != objects) {
					for (int i = 0; i < objects.length; i++) {
						RDFNode node = row.get(objects[i]);
						rm.put(objects[i], node);
					}
				}

				list.add(rm);
				count++;
				map.put(tag, count);
			}
		}

		return list;
	}

}