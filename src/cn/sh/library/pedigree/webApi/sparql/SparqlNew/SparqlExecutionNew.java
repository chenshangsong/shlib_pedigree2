package cn.sh.library.pedigree.webApi.sparql.SparqlNew;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.ConnectionPoolDataSource;

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
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

import cn.sh.library.pedigree.common.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.utils.RDFUtils;
import virtuoso.jdbc4.VirtuosoConnectionPoolDataSource;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class SparqlExecutionNew {
	public static ConnectionPoolDataSource thisDataSource;

	public static final String vtIp = CodeMsgUtil.getDbConfig("vtdb.host");
	public static final Integer vtInitialPoolSize = StringUtil
			.getInteger(CodeMsgUtil.getDbConfig("vtdb.initialPoolSize"));
	public static final Integer vtMinPoolSize = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.minPoolSize"));
	public static final Integer vtMaxPoolSize = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.maxPoolSize"));
	public static final Integer vtLoginTimeout = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.loginTimeout"));
	public static final Integer vtMaxIdleTime = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.maxIdleTime"));

	public static void init(String ip, int port, String user, String pwd) {
		try {
			VirtuosoConnectionPoolDataSource vtDataSource = new VirtuosoConnectionPoolDataSource();
			vtDataSource.setServerName(ip);
			vtDataSource.setPortNumber(port);
			vtDataSource.setUser(user);
			vtDataSource.setPassword(pwd);
			vtDataSource.setCharset("UTF-8");

			// 连接池大小配置
			vtDataSource.setInitialPoolSize(vtInitialPoolSize); // 初始连接数（高并发场景适当提高）
			vtDataSource.setMinPoolSize(vtMinPoolSize); // 最小空闲连接数
			vtDataSource.setMaxPoolSize(vtMaxPoolSize); // 最大连接数（根据数据库性能调整）
			// 连接超时配置
			vtDataSource.setLoginTimeout(vtLoginTimeout); // 连接数据库超时时间（秒）
			// 空闲连接管理
			vtDataSource.setMaxIdleTime(vtMaxIdleTime); // 连接最大空闲时间（秒）

			thisDataSource = vtDataSource;
		} catch (Exception e) {
			throw new RuntimeException("连接池初始化失败", e);
		}
	}
    public static ConnectionPoolDataSource createDataSource(String host, int port, String username, String password) {
        try {
            VirtuosoConnectionPoolDataSource dataSource = new VirtuosoConnectionPoolDataSource();
            dataSource.setServerName(host);
            dataSource.setPortNumber(port);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setCharset("UTF-8");

            // 连接池配置（从配置文件读取或固定值，根据实际情况调整）
            dataSource.setInitialPoolSize(5);   // 初始连接数
            dataSource.setMinPoolSize(3);       // 最小空闲连接数
            dataSource.setMaxPoolSize(20);      // 最大连接数
            dataSource.setLoginTimeout(30);     // 连接超时（秒）
            dataSource.setMaxIdleTime(60);      // 最大空闲时间（秒）
            thisDataSource = dataSource;
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("连接池初始化失败", e);
        }
    }
	public static VirtGraph getGraph(String graphName) {
		return new VirtGraph(graphName, thisDataSource);
	}

	   public static VirtGraph getGraph(String graphName, ConnectionPoolDataSource dataSource) {
	        if (dataSource == null) {
	            throw new IllegalArgumentException("数据源不能为空");
	        }
	        return new VirtGraph(graphName, dataSource);
	    }

	public static VirtGraph getGraph() {
		return new VirtGraph(thisDataSource);
	}

	public static ArrayList vQuery(String endpoint, String query, String[] objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, sparql);

		ResultSet rs = qe.execSelect();

		ArrayList list = extract(rs, objects);
		qe.close();
		return list;
	}

	// 原来用的
//	public static ArrayList vQuery(VirtGraph set, String query, String[] objects) {
//		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
//		ResultSet rs = vqe.execSelect();
//		return extract(rs, objects);
//	}
	// 殷海丹改的 为啥改呢 因为用graph是常链接老是崩，close一下试一试
	public static ArrayList vQuery(VirtGraph set, String query, String[] objects) {
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
		ResultSet rs = vqe.execSelect();
		ArrayList list = extract(rs, objects);
		vqe.close();// 因为用graph长连接会崩 添加关闭测试一下阔不阔以不崩
		set.close();
		return list;
	}

	public static ArrayList jQuery(String service, String query, String[] objects) {
		Query sparql = QueryFactory.create(query);
		ARQ.getContext().setTrue(ARQ.useSAX);

		QueryExecution qexec = QueryExecutionFactory.sparqlService(service, sparql);

		ResultSet rs = qexec.execSelect();

		return extract(rs, objects);
	}

	public static Model construct(Model set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		Model model = qe.execConstruct();
		qe.close();
		return model;
	}

	public static Model construct(VirtGraph set, String query) {
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
		Model model = vqe.execConstruct();
		vqe.close();
		return model;
	}

	public static Model construct(String endpoint, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, sparql);
		Model model = qe.execConstruct();
		qe.close();

		return model;
	}

	public static void update(VirtGraph set, String query) {
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, set);
		vur.exec();
		set.close();// 很重要，用完就关闭
	}

	public static void update(Model model, String query) {
		model.enterCriticalSection(false);
		try {
			UpdateRequest req = UpdateFactory.create(query);
			UpdateAction.execute(req, model);

			model.leaveCriticalSection();
		} finally {
			model.leaveCriticalSection();
		}

	}

	public static boolean ask(Model set, String query) {
		ArrayList results = jQuery(set, query, new String[0]);

		if (results.size() > 0) {
			return true;
		}

		return false;
	}

	public static ArrayList jQuery(VirtModel set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		return extract(rs, new String[] { "s", "p", "o" });
	}

	public static ArrayList jQuery(VirtModel set, String query, String[] objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		return extract(rs, objects);
	}

	public static ArrayList jQuery(VirtModel set, String query, boolean flag, String[] objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		if (true == flag) {
			return extract2Short(set, rs, objects);
		}
		return extract(rs, objects);
	}

	public static ArrayList jQuery(Model set, String query, String[] objects) {
		set.enterCriticalSection(true);
		try {
			Query sparql = QueryFactory.create(query);
			QueryExecution qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();

			return extract(rs, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	public static ArrayList jQuery(Model set, String query, boolean flag, String[] objects) {
		set.enterCriticalSection(true);
		try {
			Query sparql = QueryFactory.create(query);
			QueryExecution qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			ArrayList localArrayList;
			if (true == flag) {
				return extract2Short(set, rs, objects);
			}
			return extract(rs, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	public static ArrayList vQuery(Model set, String query, String[] objects) {
		set.enterCriticalSection(true);
		try {
			QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
			ResultSet rs = vqe.execSelect();

			return extract(rs, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	public static ArrayList vQuery(Model set, String query, int limit, String key, String[] objects) {
		set.enterCriticalSection(true);
		try {
			QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
			ResultSet rs = vqe.execSelect();

			return extractLimited(rs, limit, key, objects);
		} finally {
			set.leaveCriticalSection();
		}
	}

	public static OutputStream jQueryJson(VirtModel set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ResultSet rs = qe.execSelect();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(bos, rs);

		return bos;
	}

	public static OutputStream jQueryRDF(VirtModel set, String query) {
		QueryExecution qe = QueryExecutionFactory.create(query, set);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSet results = qe.execSelect();
//		ResultSetFormatter.outputAsRDF(baos, "RDF/XML-ABBREV", results);

		return baos;
	}

	public static OutputStream vQueryJson(VirtModel set, String query) {
		QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
		ResultSet rs = vqe.execSelect();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(bos, rs);

		return bos;
	}

	public static ArrayList extract(ResultSet resultSet, String[] objects) {
		ArrayList list = new ArrayList();

//		if(objects == null){
//			objects = )
//		}
		if (objects == null) {
			objects = new String[] {};
		}
		if (objects.length == 0) {
//			objects = resultSet.getResultVars().toArray(new String[resultSet.getResultVars().size()]);
//			List<String> resVar = resultSet.getResultVars();
//			objects = new String[resultSet.getResultVars().size()];
//			int i = 0;
//			for(String field : resultSet.getResultVars()){
////				if(field.equals("graph")){
////					continue;
////				}
//				objects[i] = field;
//				i++;
//			}
			objects = resultSet.getResultVars().toArray(new String[resultSet.getResultVars().size()]);
		}
//		System.out.println();

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

	private static ArrayList extractLimited(ResultSet resultSet, int limit, String key, String[] objects) {
		ArrayList list = new ArrayList();
		Map map = new HashMap();

		int count = 0;
		while (resultSet.hasNext()) {
			HashMap rm = new HashMap();
			QuerySolution row = resultSet.nextSolution();

			String tag = row.get(key).toString();

			if (!map.containsKey(tag)) {
				count = 0;
				map.put(tag, Integer.valueOf(count));
			}

			if (((Integer) map.get(tag)).intValue() < limit) {
				map.put(tag, map.get(row.get(key)));

				if (null != objects) {
					for (int i = 0; i < objects.length; i++) {
						RDFNode node = row.get(objects[i]);
						rm.put(objects[i], node);
					}
				}

				list.add(rm);
				count++;
				map.put(tag, Integer.valueOf(count));
			}
		}

		return list;
	}

	private static ArrayList extract2Short(Model model, ResultSet resultSet, String[] objects) {
		ArrayList list = new ArrayList();

		while (resultSet.hasNext()) {
			HashMap rm = new HashMap();
			QuerySolution row = resultSet.nextSolution();

			if (null != objects) {
				for (int i = 0; i < objects.length; i++) {
					RDFNode node = row.get(objects[i]);

					if ((node instanceof Resource))
						rm.put(objects[i], RDFUtils.toShortLabel(model, node));
					else {
						rm.put(objects[i], node);
					}
				}
			}

			list.add(rm);
		}

		return list;
	}
}
