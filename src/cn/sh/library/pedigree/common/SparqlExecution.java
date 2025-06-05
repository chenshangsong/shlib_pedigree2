package cn.sh.library.pedigree.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.ConnectionPoolDataSource;

import org.apache.log4j.Logger;

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
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.utils.RDFUtils;
import virtuoso.jdbc4.VirtuosoConnectionPoolDataSource;
import virtuoso.jdbc4.VirtuosoException;
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
	/**
	 * 日志
	 */
	private static final Logger logger = Logger.getLogger(SparqlExecution.class);
	private static ConnectionPoolDataSource dataSource;
	public static final String vtIp = CodeMsgUtil.getDbConfig("vtdb.host");
	public static final Integer vtInitialPoolSize = StringUtil
			.getInteger(CodeMsgUtil.getDbConfig("vtdb.initialPoolSize"));
	public static final Integer vtMinPoolSize = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.minPoolSize"));
	public static final Integer vtMaxPoolSize = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.maxPoolSize"));
	public static final Integer vtLoginTimeout = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.loginTimeout"));
	public static final Integer vtMaxIdleTime = StringUtil.getInteger(CodeMsgUtil.getDbConfig("vtdb.maxIdleTime"));

	public static void init() {
		try {
			VirtuosoConnectionPoolDataSource vtDataSource = new VirtuosoConnectionPoolDataSource();
			vtDataSource.setServerName(vtIp);
			vtDataSource.setPortNumber(1111);
			vtDataSource.setUser("dba");
			vtDataSource.setPassword("Shlibrary123");
			vtDataSource.setCharset("UTF-8");

			// 连接池大小配置
			vtDataSource.setInitialPoolSize(vtInitialPoolSize); // 初始连接数（高并发场景适当提高）
			vtDataSource.setMinPoolSize(vtMinPoolSize); // 最小空闲连接数
			vtDataSource.setMaxPoolSize(vtMaxPoolSize); // 最大连接数（根据数据库性能调整）
			// 连接超时配置
			vtDataSource.setLoginTimeout(vtLoginTimeout); // 连接数据库超时时间（秒）
			// 空闲连接管理
			vtDataSource.setMaxIdleTime(vtMaxIdleTime); // 连接最大空闲时间（秒）
			dataSource = vtDataSource;
		} catch (Exception e) {
			logger.error("Virtuoso连接池初始化失败", e);
			throw new RuntimeException("Virtuoso连接池初始化失败", e);
		}
	}

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

			dataSource = vtDataSource;
		} catch (Exception e) {
			logger.error("Virtuoso连接池初始化失败", e);
			throw new RuntimeException("Virtuoso连接池初始化失败", e);
		}
	}

	public static void reinit() {
		dataSource = null; // 清空旧连接池
		init(); // 重新初始化
		
	}

	private static boolean isConnectionError(Exception e) {
		// 检查是否是Virtuoso特有的连接异常（如连接拒绝、超时）
		if (e instanceof VirtuosoException) {
			int errorCode = ((VirtuosoException) e).getErrorCode();
			return errorCode == 10061; // 示例：连接拒绝错误码
		}
		// 兼容Jena异常
		return e instanceof JenaException && e.getMessage().toLowerCase().contains("connection");
	}

	public static VirtGraph getGraph(String graphName) {
		return new VirtGraph(graphName, dataSource);
	}

	public static VirtGraph getGraph() {
		init();
		return new VirtGraph(dataSource);
	}

	// Remote federated query
	public static ArrayList vQuery(String endpoint, String query, String... objects) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.sparqlService(endpoint, sparql);
			ResultSet rs = qe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-vQuery: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}

		}
	}

	public static ArrayList vQuery(VirtGraph set, String query, String... objects) {
		VirtuosoQueryExecution vqe = null;
//        return SparqlExecutionNew.vQuery(set, query, objects);
		try {
			vqe = VirtuosoQueryExecutionFactory.create(query, set);
			ResultSet rs = vqe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-vQuery111111111111111: " + query, e);
			// 判断是否是连接中断的异常 20250113

			// 在vQuery中重连时调用reinit()
			if (isConnectionError(e)) {
				logger.error("连接失败，尝试重建连接池...");
				reinit(); // 重建连接池
				return vQuery(getGraph(), query, objects);
			}

			return null;
		} finally {
			if (vqe != null) {
				vqe.close();
			}

		}
	}

	// Service
	public static ArrayList jQuery(String service, String query, String... objects) {
		QueryExecution vqe = null;
		try {
			Query sparql = QueryFactory.create(query);
			ARQ.getContext().setTrue(ARQ.useSAX);
			vqe = QueryExecutionFactory.sparqlService(service, sparql);
			ResultSet rs = vqe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQuery: " + query, e);
			return null;
		} finally {
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	// Construct
	public static Model construct(Model set, String query) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			return qe.execConstruct();
		} catch (Exception e) {
			logger.error("错误SparqlExecution-construct: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static Model construct(VirtGraph set, String query) {
		VirtuosoQueryExecution vqe = null;
		try {
			vqe = VirtuosoQueryExecutionFactory.create(query, set);
			return vqe.execConstruct();
		} catch (Exception e) {
			logger.error("错误SparqlExecution-construct: " + query, e);
			return null;
		} finally {
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	// Update
	public static void update(VirtGraph set, String query) {
		if (set == null || query == null || query.isEmpty()) {
			logger.error("Invalid input: VirtGraph or query is null/empty.");
			return;
		}
		VirtuosoUpdateRequest vur = null;
		try {
			vur = VirtuosoUpdateFactory.create(query, set);
			vur.exec();
		} catch (Exception e) {
			logger.error("错误SparqlExecution-update: " + query, e);
		}
	}

	public static void update(Model model, String query) {
		if (model == null || query == null || query.isEmpty()) {
			logger.error("Invalid input: Model or query is null/empty.");
			return;
		}

		try {
			UpdateRequest req = UpdateFactory.create(query);
			UpdateAction.execute(req, model);
		} catch (Exception e) {
			logger.error("Error executing SPARQL update on Model: " + query, e);
		}
	}

	// Ask
	public static boolean ask(Model set, String query) {
		ArrayList results = jQuery(set, query);
		return results != null && !results.isEmpty();
	}

	// Query all triples
	public static ArrayList jQuery(VirtModel set, String query) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			return extract(rs, "s", "p", "o");
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQuery: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static ArrayList jQuery(VirtModel set, String query, String... objects) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQuery: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static ArrayList jQuery(VirtModel set, String query, boolean flag, String... objects) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			if (flag) {
				return extract2Short(set, rs, objects);
			}
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQuery: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static ArrayList jQuery(Model set, String query, String... objects) {
		set.enterCriticalSection(Lock.READ);
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQuery: " + query, e);
			return null;
		} finally {
			set.leaveCriticalSection();
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static ArrayList jQuery(Model set, String query, boolean flag, String... objects) {
		set.enterCriticalSection(Lock.READ);
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			if (flag) {
				return extract2Short(set, rs, objects);
			}
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQuery: " + query, e);
			return null;
		} finally {
			set.leaveCriticalSection();
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static ArrayList vQuery(Model set, String query, String... objects) {
		set.enterCriticalSection(Lock.READ);
		QueryExecution vqe = null;
		try {
			vqe = VirtuosoQueryExecutionFactory.create(query, set);
			ResultSet rs = vqe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-vQuery: " + query, e);
			return null;
		} finally {
			set.leaveCriticalSection();
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	public static ArrayList vQuery(Model set, String query, int limit, String key, String... objects) {
		set.enterCriticalSection(Lock.READ);
		QueryExecution vqe = null;
		try {
			vqe = VirtuosoQueryExecutionFactory.create(query, set);
			ResultSet rs = vqe.execSelect();
			return extractLimited(rs, limit, key, objects);
		} catch (Exception e) {
			logger.error("错误SparqlExecution-vQuery: " + query, e);
			return null;
		} finally {
			set.leaveCriticalSection();
			if (vqe != null) {
				vqe.close();
			}

		}
	}

	public static OutputStream jQueryJson(VirtModel set, String query) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet rs = qe.execSelect();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(bos, rs);
			return bos;
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQueryJson: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static OutputStream jQueryRDF(VirtModel set, String query) {
		QueryExecution qe = null;
		try {
			Query sparql = QueryFactory.create(query);
			qe = QueryExecutionFactory.create(sparql, set);
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsRDF(baos, "RDF/XML-ABBREV", results);
			return baos;
		} catch (Exception e) {
			logger.error("错误SparqlExecution-jQueryRDF: " + query, e);
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	public static OutputStream vQueryJson(VirtModel set, String query) {
		QueryExecution vqe = null;
		try {
			Query sparql = QueryFactory.create(query);
			vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
			ResultSet rs = vqe.execSelect();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(bos, rs);
			return bos;
		} catch (Exception e) {
			logger.error("错误SparqlExecution-vQueryJson: " + query, e);
			return null;
		} finally {
			if (vqe != null) {
				vqe.close();
			}

		}
	}

	private static ArrayList extract(ResultSet resultSet, String... objects) {
		ArrayList list = new ArrayList<>();
		while (resultSet.hasNext()) {
			HashMap<String, RDFNode> rm = new HashMap<>();
			QuerySolution row = resultSet.nextSolution();
			if (objects != null) {
				for (String object : objects) {
					RDFNode node = row.get(object);
					rm.put(object, node);
				}
			}
			list.add(rm);
		}
		return list;
	}

	private static ArrayList extractLimited(ResultSet resultSet, int limit, String key, String... objects) {
		ArrayList list = new ArrayList<>();
		Map<String, Integer> map = new HashMap<>();
		while (resultSet.hasNext()) {
			HashMap<String, RDFNode> rm = new HashMap<>();
			QuerySolution row = resultSet.nextSolution();
			String tag = row.get(key).toString();
			map.putIfAbsent(tag, 0);
			if (map.get(tag) < limit) {
				if (objects != null) {
					for (String object : objects) {
						RDFNode node = row.get(object);
						rm.put(object, node);
					}
				}
				list.add(rm);
				map.put(tag, map.get(tag) + 1);
			}
		}
		return list;
	}

	private static ArrayList extract2Short(Model model, ResultSet resultSet, String... objects) {
		ArrayList list = new ArrayList();

		while (resultSet.hasNext()) {
			HashMap rm = new HashMap();
			QuerySolution row = resultSet.nextSolution();
			// System.out.println(row.toString());
			if (null != objects) {
				for (int i = 0; i < objects.length; i++) {
					RDFNode node = row.get(objects[i]);
//System.out.println(node.toString());
					if (node instanceof Resource) {
						rm.put(objects[i], RDFUtils.toShortLabel(model, node));
					} else {
						rm.put(objects[i], node);
					}
				}
			}

			list.add(rm);
		}

		return list;
	}
}
