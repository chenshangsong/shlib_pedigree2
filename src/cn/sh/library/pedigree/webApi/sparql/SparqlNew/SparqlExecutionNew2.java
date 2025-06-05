package cn.sh.library.pedigree.webApi.sparql.SparqlNew;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.sql.ConnectionPoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import cn.sh.library.pedigree.utils.RDFUtils;
import virtuoso.jdbc4.VirtuosoConnectionPoolDataSource;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class SparqlExecutionNew2 {
	private static final Logger logger = LoggerFactory.getLogger(SparqlExecutionNew2.class);
	private static ConnectionPoolDataSource dataSource;

	// 初始化数据源，使用默认配置
	private static void init() {
		try {
			VirtuosoConnectionPoolDataSource vtDataSource = new VirtuosoConnectionPoolDataSource();
			vtDataSource.setServerName("192.168.1.101");// 名人
			vtDataSource.setPortNumber(1111);
			vtDataSource.setUser("dba");
			vtDataSource.setPassword("Shlibrary123");
			vtDataSource.setCharset("UTF-8");
			vtDataSource.setInitialPoolSize(20);
			dataSource = vtDataSource;
			logger.info("数据源初始化成功，使用默认配置");
		} catch (Exception e) {
			logger.error("初始化数据源失败，使用默认配置：" + e.getMessage());
		}
	}

	// 初始化数据源，使用自定义配置
	public static void init(String ip, int port, String user, String pwd) {
		try {
			VirtuosoConnectionPoolDataSource vtDataSource = new VirtuosoConnectionPoolDataSource();
			vtDataSource.setServerName(ip);
			vtDataSource.setPortNumber(port);
			vtDataSource.setUser(user);
			vtDataSource.setPassword(pwd);
			vtDataSource.setCharset("UTF-8");
			vtDataSource.setInitialPoolSize(20);
			dataSource = vtDataSource;
			logger.info("数据源初始化成功，使用自定义配置：" + ip + "，" + port + "，" + user + "，" + pwd);
		} catch (Exception e) {
			logger.error("初始化数据源失败，使用自定义配置：" + ip + "，" + port + "，" + user + "，" + pwd + "，" + e.getMessage());
		}
	}

	// 获取指定图名的VirtGraph对象
	public static VirtGraph getGraph(String graphName) {
		return new VirtGraph(graphName, dataSource);
	}

	// 获取默认图的VirtGraph对象
	public static VirtGraph getGraph() {
		return new VirtGraph(dataSource);
	}

	/**
	 * 执行SPARQL查询，通过服务端点进行查询
	 * 
	 * @param endpoint SPARQL服务端点
	 * @param query    SPARQL查询语句
	 * @param objects  需要提取的结果变量数组，如果为null或长度为0，则提取所有结果变量
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList vQuery(String endpoint, String query, String[] objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, sparql);

		try {
			ResultSet rs = qe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行vQuery失败，端点：" + endpoint + "，查询语句：" + query + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 执行SPARQL查询，针对给定的VirtGraph进行查询
	 * 
	 * @param set     VirtGraph对象，指定查询的图
	 * @param query   SPARQL查询语句
	 * @param objects 需要提取的结果变量数组，如果为null或长度为0，则提取所有结果变量
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList vQuery(VirtGraph set, String query, String[] objects) {
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);

		try {
			ResultSet rs = vqe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行vQuery失败，图：" + set.getGraphName() + "，查询语句：" + query + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	/**
	 * 执行SPARQL查询，通过服务端点进行查询，适用于特定的查询场景（与vQuery类似，但可能有不同的使用场景）
	 * 
	 * @param service SPARQL服务地址
	 * @param query   SPARQL查询语句
	 * @param objects 需要提取的结果变量数组，如果为null或长度为0，则提取所有结果变量
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList jQuery(String service, String query, String[] objects) {
		Query sparql = QueryFactory.create(query);
		ARQ.getContext().setTrue(ARQ.useSAX);

		QueryExecution qexec = QueryExecutionFactory.sparqlService(service, sparql);

		try {
			ResultSet rs = qexec.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行jQuery失败，服务：" + service + "，查询语句：" + query + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			if (qexec != null) {
				qexec.close();
			}
		}
	}

	/**
	 * 根据给定的模型和查询语句构建新的模型
	 * 
	 * @param set   基础模型
	 * @param query SPARQL构建查询语句
	 * @return 构建后的新模型，如果执行失败返回null
	 */
	public static Model construct(Model set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);

		try {
			return qe.execConstruct();
		} catch (Exception e) {
			logger.error("执行construct失败，模型：" + set + "，查询语句：" + query + "，" + e.getMessage());
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 根据给定的VirtGraph和查询语句构建新的模型
	 * 
	 * @param set   VirtGraph对象，指定构建的基础图
	 * @param query SPARQL构建查询语句
	 * @return 构建后的新模型，如果执行失败返回null
	 */
	public static Model construct(VirtGraph set, String query) {
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);

		try {
			return vqe.execConstruct();
		} catch (Exception e) {
			logger.error("执行construct失败，图：" + set.getGraphName() + "，查询语句：" + query + "，" + e.getMessage());
			return null;
		} finally {
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	/**
	 * 通过服务端点执行SPARQL构建查询并返回构建的模型
	 * 
	 * @param endpoint SPARQL服务端点
	 * @param query    SPARQL构建查询语句
	 * @return 构建后的新模型，如果执行失败返回null
	 */
	public static Model construct(String endpoint, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, sparql);

		try {
			Model model = qe.execConstruct();
			return model;
		} catch (Exception e) {
			logger.error("执行construct失败，端点：" + endpoint + "，查询语句：" + query + "，" + e.getMessage());
			return null;
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 对给定的VirtGraph执行SPARQL更新操作
	 * 
	 * @param set   VirtGraph对象，指定更新的目标图
	 * @param query SPARQL更新语句
	 */
	public static void update(VirtGraph set, String query) {
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, set);

		try {
			vur.exec();
		} catch (Exception e) {
			logger.error("执行update失败，图：" + set.getGraphName() + "，更新语句：" + query + "，" + e.getMessage());
		}
	}

	/**
	 * 对给定的模型执行SPARQL更新操作
	 * 
	 * @param model 模型对象
	 * @param query SPARQL更新语句
	 */
	public static void update(Model model, String query) {
		model.enterCriticalSection(false);
		try {
			UpdateRequest req = UpdateFactory.create(query);
			UpdateAction.execute(req, model);
		} catch (Exception e) {
			logger.error("执行update失败，模型：" + model + "，更新语句：" + query + "，" + e.getMessage());
		} finally {
			model.leaveCriticalSection();
		}
	}

	/**
	 * 对给定的模型执行ASK形式的SPARQL查询
	 * 
	 * @param set   模型对象
	 * @param query SPARQL ASK查询语句
	 * @return 查询结果，如果查询结果集大小大于0则返回true，否则返回false
	 */
	public static boolean ask(Model set, String query) {
		ArrayList results = jQuery(set, query, new String[0]);
		return results.size() > 0;
	}

	/**
	 * 对给定的VirtModel执行SPARQL查询，并提取默认的结果变量（"s", "p", "o"）
	 * 
	 * @param set   VirtModel对象
	 * @param query SPARQL查询语句
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList jQuery(VirtModel set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);

		try {
			ResultSet rs = qe.execSelect();
			return extract(rs, new String[] { "s", "p", "o" });
		} catch (Exception e) {
			logger.error("执行jQuery失败，模型：" + set + "，查询语句：" + query + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 对给定的VirtModel执行SPARQL查询，并提取指定的结果变量
	 * 
	 * @param set     VirtModel对象
	 * @param query   SPARQL查询语句
	 * @param objects 需要提取的结果变量数组
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList jQuery(VirtModel set, String query, String[] objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);

		try {
			ResultSet rs = qe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行jQuery失败，模型：" + set + "，查询语句：" + query + "，对象数组：" + java.util.Arrays.toString(objects) + "，"
					+ e.getMessage());
			return new ArrayList();
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 对给定的VirtModel执行SPARQL查询，并根据标志位选择不同的结果提取方式
	 * 
	 * @param set     VirtModel对象
	 * @param query   SPARQL查询语句
	 * @param flag    标志位，true表示使用extract2Short方法提取结果，false表示使用普通的extract方法
	 * @param objects 需要提取的结果变量数组
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList jQuery(VirtModel set, String query, boolean flag, String[] objects) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);

		try {
			ResultSet rs = qe.execSelect();
			if (true == flag) {
				return extract2Short(set, rs, objects);
			}
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行jQuery失败，模型：" + set + "，查询语句：" + query + "，标志位：" + flag + "，对象数组："
					+ java.util.Arrays.toString(objects) + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 对给定的模型执行SPARQL查询，并提取指定的结果变量
	 * 
	 * @param set     模型对象
	 * @param query   SPARQL查询语句
	 * @param objects 需要提取的结果变量数组
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList jQuery(Model set, String query, String[] objects) {
		set.enterCriticalSection(true);
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		try {

			ResultSet rs = qe.execSelect();
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行jQuery失败，模型：" + set + "，查询语句：" + query + "，对象数组：" + java.util.Arrays.toString(objects) + "，"
					+ e.getMessage());
			return new ArrayList();
		} finally {
			set.leaveCriticalSection();
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 对给定的模型执行SPARQL查询，并根据标志位选择不同的结果提取方式
	 * 
	 * @param set     模型对象
	 * @param query   SPARQL查询语句
	 * @param flag    标志位，true表示使用extract2Short方法提取结果，false表示使用普通的extract方法
	 * @param objects 需要提取的结果变量数组
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList jQuery(Model set, String query, boolean flag, String[] objects) {
		set.enterCriticalSection(true);
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		try {

			ResultSet rs = qe.execSelect();
			ArrayList localArrayList;
			if (true == flag) {
				return extract2Short(set, rs, objects);
			}
			return extract(rs, objects);
		} catch (Exception e) {
			logger.error("执行jQuery失败，模型：" + set + "，查询语句：" + query + "，标志位：" + flag + "，对象数组："
					+ java.util.Arrays.toString(objects) + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			set.leaveCriticalSection();
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 对给定的模型执行SPARQL查询，并提取指定的结果变量，支持限制结果数量
	 * 
	 * @param set     模型对象
	 * @param query   SPARQL查询语句
	 * @param limit   限制结果数量
	 * @param key     用于限制结果数量的关键变量
	 * @param objects 需要提取的结果变量数组
	 * @return 包含查询结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList vQuery(Model set, String query, int limit, String key, String[] objects) {
		set.enterCriticalSection(true);
		QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
		ResultSet rs = vqe.execSelect();
		try {

			return extractLimited(rs, limit, key, objects);
		} catch (Exception e) {
			logger.error("执行vQuery失败，模型: " + set + "，查询语句: " + query + "，限制: " + limit + "，键: " + key + "，对象数组: "
					+ java.util.Arrays.toString(objects) + "，" + e.getMessage());
			return new ArrayList();
		} finally {
			set.leaveCriticalSection();
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	/**
	 * 执行SPARQL查询并将结果以JSON格式输出
	 * 
	 * @param set   VirtModel对象
	 * @param query SPARQL查询语句
	 * @return 包含查询结果的JSON格式的输出流
	 */
	public static OutputStream jQueryJson(VirtModel set, String query) {
		Query sparql = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(sparql, set);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ResultSet rs = qe.execSelect();
			ResultSetFormatter.outputAsJSON(bos, rs);
			return bos;
		} catch (Exception e) {
			logger.error("执行jQueryJson失败，模型: " + set + "，查询语句: " + query + "，" + e.getMessage());
			return new ByteArrayOutputStream();
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 执行SPARQL查询并将结果以RDF格式输出
	 * 
	 * @param set   VirtModel对象
	 * @param query SPARQL查询语句
	 * @return 包含查询结果的RDF格式的输出流
	 */
	public static OutputStream jQueryRDF(VirtModel set, String query) {
		QueryExecution qe = QueryExecutionFactory.create(query, set);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ResultSet results = qe.execSelect();
			// 注意：此处原代码未完成RDF格式化，可能需要根据实际需求补充
			return baos;
		} catch (Exception e) {
			logger.error("执行jQueryRDF失败，模型: " + set + "，查询语句: " + query + "，" + e.getMessage());
			return new ByteArrayOutputStream();
		} finally {
			if (qe != null) {
				qe.close();
			}
		}
	}

	/**
	 * 执行SPARQL查询并将结果以JSON格式输出（针对VirtModel的另一种实现方式）
	 * 
	 * @param set   VirtModel对象
	 * @param query SPARQL查询语句
	 * @return 包含查询结果的JSON格式的输出流
	 */
	public static OutputStream vQueryJson(VirtModel set, String query) {
		QueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, set);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ResultSet rs = vqe.execSelect();
			ResultSetFormatter.outputAsJSON(bos, rs);
			return bos;
		} catch (Exception e) {
			logger.error("执行vQueryJson失败，模型: " + set + "，查询语句: " + query + "，" + e.getMessage());
			return new ByteArrayOutputStream();
		} finally {
			if (vqe != null) {
				vqe.close();
			}
		}
	}

	/**
	 * 从查询结果集中提取数据
	 * 
	 * @param resultSet 查询结果集
	 * @param objects   需要提取的结果变量数组，如果为null或长度为0，则提取所有结果变量
	 * @return 包含提取结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	public static ArrayList extract(ResultSet resultSet, String[] objects) {
		ArrayList list = new ArrayList();

		// 处理objects参数，如果为null则初始化为空数组
		if (objects == null) {
			objects = new String[] {};
		}

		// 如果objects数组为空，则从结果集中获取所有变量名
		if (objects.length == 0) {
			objects = resultSet.getResultVars().toArray(new String[resultSet.getResultVars().size()]);
		}

		try {
			// 遍历结果集，提取每个结果行的数据
			while (resultSet.hasNext()) {
				HashMap<String, RDFNode> rm = new HashMap<>();
				QuerySolution row = resultSet.nextSolution();

				// 将每个指定的结果变量及其值存入HashMap
				for (String object : objects) {
					RDFNode node = row.get(object);
					rm.put(object, node);
				}

				list.add(rm);
			}
		} catch (Exception e) {
			logger.error("从结果集提取数据失败，" + e.getMessage());
		}

		return list;
	}

	/**
	 * 从查询结果集中提取数据，并限制每个key对应的结果数量
	 * 
	 * @param resultSet 查询结果集
	 * @param limit     每个key对应的最大结果数量
	 * @param key       用于分组的关键变量名
	 * @param objects   需要提取的结果变量数组
	 * @return 包含提取结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode
	 */
	private static ArrayList extractLimited(ResultSet resultSet, int limit, String key, String[] objects) {
		ArrayList list = new ArrayList();
		Map<String, Integer> keyCountMap = new HashMap<>(); // 记录每个key出现的次数

		try {
			// 遍历结果集，提取每个结果行的数据，并根据key限制数量
			while (resultSet.hasNext()) {
				HashMap<String, RDFNode> rm = new HashMap<>();
				QuerySolution row = resultSet.nextSolution();

				// 获取当前行的key值
				String tag = row.get(key).toString();

				// 获取该key已出现的次数，若不存在则初始化为0
				int count = keyCountMap.getOrDefault(tag, 0);

				// 如果未达到限制数量，则处理当前行数据
				if (count < limit) {
					// 将每个指定的结果变量及其值存入HashMap
					for (String object : objects) {
						RDFNode node = row.get(object);
						rm.put(object, node);
					}

					list.add(rm);
					keyCountMap.put(tag, count + 1);
				}
			}
		} catch (Exception e) {
			logger.error("从结果集提取有限数据失败，限制: " + limit + "，键: " + key + "，" + e.getMessage());
		}

		return list;
	}

	/**
	 * 从查询结果集中提取数据，并将资源节点转换为短标签形式
	 * 
	 * @param model     模型对象，用于解析资源节点
	 * @param resultSet 查询结果集
	 * @param objects   需要提取的结果变量数组
	 * @return 包含提取结果的ArrayList，每个元素为一个HashMap，键为结果变量名，值为对应的RDFNode或短标签
	 */
	private static ArrayList extract2Short(Model model, ResultSet resultSet, String[] objects) {
		ArrayList list = new ArrayList();

		try {
			// 遍历结果集，提取每个结果行的数据
			while (resultSet.hasNext()) {
				HashMap<String, Object> rm = new HashMap<>();
				QuerySolution row = resultSet.nextSolution();

				// 处理每个指定的结果变量
				for (String object : objects) {
					RDFNode node = row.get(object);

					// 如果是资源节点，则转换为短标签形式
					if (node instanceof com.hp.hpl.jena.rdf.model.Resource) {
						rm.put(object, RDFUtils.toShortLabel(model, node));
					} else {
						rm.put(object, node);
					}
				}

				list.add(rm);
			}
		} catch (Exception e) {
			logger.error("从结果集提取短标签数据失败，" + e.getMessage());
		}

		return list;
	}

	/**
	 * 关闭数据源连接，释放资源
	 */
	public static void closeDataSource() {
		// 由于使用的是连接池，通常不需要显式关闭，连接池会管理连接的生命周期
		// 这里可以添加一些必要的清理操作，如记录日志等
		logger.debug("数据源连接已关闭");
	}
}