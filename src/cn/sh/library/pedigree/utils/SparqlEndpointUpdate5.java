package cn.sh.library.pedigree.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.ConnectionPoolDataSource;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;

import cn.sh.library.pedigree.sysManager.sysMnagerSparql.Namespace;
import cn.sh.library.pedigree.webApi.sparql.SparqlNew.SparqlExecutionNew;
import virtuoso.jena.driver.VirtGraph;

/**
 * 增强版数据迁移工具类（优化子查询与SPARQL模板） 优化点： 1. 移除所有冗余子查询，改用多图联合匹配 2. 修正CONSTRUCT中变量未绑定问题
 * 3. 用三元组直接匹配替代FILTER，提升查询效率 4. 保持类型化配置扩展能力
 */
public class SparqlEndpointUpdate5 {
	private static final Logger logger = Logger.getLogger(SparqlEndpointUpdate5.class.getName());

	// 源数据库配置
	private static final DbServerConfig SOURCE_SERVER = new DbServerConfig("127.0.0.1", 1111, "dba", "Shlibrary123");
	// 目标数据库列表（可扩展）
	private static final List<DbServerConfig> TARGET_SERVERS = Arrays.asList(
			new DbServerConfig("127.0.0.1", 1112, "dba", "dba"), new DbServerConfig("127.0.0.1", 1113, "dba", "dba"));

	// 实体类型配置（优化后）
	private enum EntityType {
		WORK("Work", "http://gen.library.sh.cn/graph/work", // sourceGraph
				"http://gen.library.sh.cn/graph/work", // toGraph
				// 查询模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "CONSTRUCT { <%2$s> ?p ?o } " + // 第2个参数：workUri
						" from <%1$s> WHERE { " + "  GRAPH <%1$s> { " + // 第1个参数：sourceGraph
						"    <%2$s> ?p ?o . " + // 第2个参数：workUri（重复使用）
						"  } " + "}",
				// 删除模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "WITH <%1$s> " + // 第1个参数：targetGraph
						"DELETE WHERE { " + "  <%2$s> ?p ?o . " + // 第2个参数：workUri（重复使用）
						"}"),
		INSTANCE("Instance", "http://gen.library.sh.cn/graph/instance", "http://gen.library.sh.cn/graph/instance",
				// 查询模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "CONSTRUCT { ?insUri ?p ?o } " + " from <%1$s> WHERE { "
						+ "  GRAPH <%1$s> { " + // 第1个参数：sourceGraph
						"    ?insUri ?p ?o ; " + "             a bf:Instance ; " + "             bf:instanceOf <%2$s> ."
						+ // 第2个参数：workUri
						"  } " + "}",
				// 删除模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "WITH <%1$s> " + // 第1个参数：targetGraph
						"DELETE WHERE { " + "  ?insUri ?p ?o ; " + "           a bf:Instance ; "
						+ "           bf:instanceOf <%2$s> ." + // 第2个参数：workUri（重复使用）
						"}"),
		ITEM("Item", "http://gen.library.sh.cn/graph/item", "http://gen.library.sh.cn/graph/item",
				// 查询模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "CONSTRUCT { ?itemUri ?p ?o } "
						+ " from <http://gen.library.sh.cn/graph/instance> from  <%1$s> WHERE { " + "  GRAPH <%1$s> { "
						+ "    ?itemUri ?p ?o ; " + "             a bf:Item ; " + "             bf:itemOf ?insUri . "
						+ "  } " + "  GRAPH <http://gen.library.sh.cn/graph/instance> { "
						+ "    ?insUri a bf:Instance ; " + "            bf:instanceOf <%2$s> . " + // 第2个参数：workUri（重复使用）
						"  } " + "}",
				// 删除模板（显式指定Item目标图 + Instance固定图）
				Namespace.getNsPrefixString() + "DELETE WHERE { " + "  GRAPH <%1$s> { " + // 显式指定Item目标图（确保删除目标图数据）
						"    ?itemUri ?p ?o ; " + "             a bf:Item ; " + "             bf:itemOf ?insUri . "
						+ "  } " + "  GRAPH <http://gen.library.sh.cn/graph/instance> { "
						+ "    ?insUri a bf:Instance ; " + "            bf:instanceOf <%2$s> . " + // 第2个参数：workUri
						"  } " + "}"),
		PERSON("Person", "http://gen.library.sh.cn/graph/person", "http://gen.library.sh.cn/graph/person",
				// 查询模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "CONSTRUCT { ?pUri ?p ?o } " + "WHERE { " + "  GRAPH <%1$s> { " + // 第1个参数：sourceGraph
						"    ?pUri ?p ?o ; " + "            a shl:Person ; " + "            shl:relatedWork <%2$s> ." + // 第2个参数：workUri
						"  } " + "}",
				// 删除模板（数字占位符明确参数顺序）
				Namespace.getNsPrefixString() + "WITH <%1$s> " + // 第1个参数：targetGraph
						"DELETE WHERE { " + "  ?pUri ?p ?o ; " + "           a shl:Person ; "
						+ "           shl:relatedWork <%2$s> ." + // 第2个参数：workUri（重复使用）
						"}");

		// 成员变量和构造方法保持不变
		final String typeName;
		final String sourceGraph;
		final String targetGraph;
		final String queryTemplate;
		final String deleteTemplate;

		EntityType(String typeName, String sourceGraph, String targetGraph, String queryTemplate,
				String deleteTemplate) {
			this.typeName = typeName;
			this.sourceGraph = sourceGraph;
			this.targetGraph = targetGraph;
			this.queryTemplate = queryTemplate;
			this.deleteTemplate = deleteTemplate;
		}
	}

	public static void main(String[] args) {
        String workUri = "http://data.library.sh.cn/jp/resource/work/pucyw6kkiop3p71w";
        try {
        	getData();//查询并公开
        	
//        	  openWorkData(workUri);   //公开
//            deleteWorkData(workUri); //关闭
        } catch (Exception e) {
            logger.log(Level.SEVERE, "操作失败：" + e.getMessage(), e);
        }
    }

	/**
	 * 全量数据迁移（包含四类实体）
	 */
	public static void openWorkData(String workUri) throws Exception {
		logger.info("================== 全量数据迁移开始 ==================");
		try (DatabaseConnection sourceConn = new DatabaseConnection(SOURCE_SERVER)) {
			for (DbServerConfig targetServer : TARGET_SERVERS) {
				try (DatabaseConnection targetConn = new DatabaseConnection(targetServer)) {
					for (EntityType entity : EntityType.values()) {
						migrateEntity(sourceConn, targetConn, entity, workUri);
					}
				}
			}
		}
		logger.info("================== 全量数据迁移完成 ==================");
	}

	public static void getData() throws Exception {
		logger.info("================== 全量数据迁移开始 ==================");
		try (DatabaseConnection sourceConn = new DatabaseConnection(SOURCE_SERVER)) {
			// 创建 VirtGraph 时传递数据源
			VirtGraph workGraph = SparqlExecutionNew.getGraph(EntityType.WORK.sourceGraph);
			String query = "select distinct ?workUri where {?workUri a bf:Work;?p ?o} limit 2000";
			List<Map<String, String>> _list = RDFUtils
					.transformListMap(SparqlExecutionNew.vQuery(workGraph, query, new String[] { "workUri" }));
			_list.stream().forEach(item -> {
				String uri = item.get("workUri");
				int i=0;
				try {
					logger.info(i+"迁移------------------------------------------------------------------>"+uri);
//					openWorkData(uri);
					deleteWorkData(uri);
					i++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		}
		logger.info("================== 全量数据迁移完成 ==================");
	}

	/**
	 * 全量数据删除（包含四类实体）
	 */
	public static void deleteWorkData(String workUri) throws Exception {
		logger.info("================== 全量数据删除开始 ==================");
		// 定义删除顺序：person → item → instance → work
		List<EntityType> deleteOrder = Arrays.asList(EntityType.PERSON, EntityType.ITEM, EntityType.INSTANCE,
				EntityType.WORK);

		for (DbServerConfig targetServer : TARGET_SERVERS) {
			try (DatabaseConnection targetConn = new DatabaseConnection(targetServer)) {
				// 按指定顺序执行删除
				for (EntityType entity : deleteOrder) {
					deleteEntity(targetConn, entity, workUri);
				}
			}
		}
		logger.info("================== 全量数据删除完成 ==================");
	}

	/**
	 * 通用实体迁移方法（无变化）
	 */
	private static void migrateEntity(DatabaseConnection sourceConn, DatabaseConnection targetConn, EntityType entity,
			String workUri) throws Exception {
		logger.info("开始迁移" + entity.typeName + "数据到服务器：" + targetConn.getConfig().getHost());
		// 获取源和目标的数据源（通过 DbServerConfig 创建）
		ConnectionPoolDataSource sourceDataSource = sourceConn.getDataSource();
		ConnectionPoolDataSource targetDataSource = targetConn.getDataSource();
		// 创建 VirtGraph 时传递数据源
		VirtGraph sourceGraph = SparqlExecutionNew.getGraph(entity.sourceGraph, sourceDataSource);
		VirtGraph targetGraph = SparqlExecutionNew.getGraph(entity.targetGraph, targetDataSource);

		try {
			String query = String.format(entity.queryTemplate, entity.sourceGraph, workUri);
//            logger.info("Generated SPARQL Query: " + query); // 输出完整查询语句
			Model model = SparqlExecutionNew.construct(sourceGraph, query);

			if (!model.isEmpty()) {
				insertModelWithTransaction(targetGraph, model);
				logger.info(entity.typeName + "迁移完成：" + model.size() + "条");
			} else {
				logger.info("无" + entity.typeName + "数据需要迁移");
			}
		} finally {
			closeResource(sourceGraph);
			closeResource(targetGraph);
		}
	}

	/**
	 * 通用实体删除方法（无变化）
	 */
	private static void deleteEntity(DatabaseConnection targetConn, EntityType entity, String workUri)
			throws Exception {
		logger.info("开始删除" + entity.typeName + "数据在服务器：" + targetConn.getConfig().getHost());

		// 获取源和目标的数据源（通过 DbServerConfig 创建）
		ConnectionPoolDataSource targetDataSource = targetConn.getDataSource();
		// 创建 VirtGraph 时传递数据源
		VirtGraph targetGraph = SparqlExecutionNew.getGraph(entity.targetGraph, targetDataSource);

		try {
			String deleteQuery = String.format(entity.deleteTemplate, entity.targetGraph, workUri);
//            logger.info("Generated SPARQL deleteQuery: " + deleteQuery); // 输出完整查询语句
			SparqlExecutionNew.update(targetGraph, deleteQuery);
			logger.info(entity.typeName + "数据删除成功");
		} finally {
			closeResource(targetGraph);
		}
	}

	/**
	 * 带事务的模型插入（无变化）
	 */
	private static void insertModelWithTransaction(VirtGraph targetGraph, Model model) {
		if (model.isEmpty()) {
			logger.info("无数据需要插入");
			return;
		}

		targetGraph.getTransactionHandler().begin();
		try {
			model.listStatements().forEachRemaining(stmt -> {
				Triple triple = stmt.asTriple();
				targetGraph.add(triple);
			});
			targetGraph.getTransactionHandler().commit();
			logger.info("成功插入" + model.size() + "条" + targetGraph.getGraphName() + "数据（事务提交）");
		} catch (Exception e) {
			targetGraph.getTransactionHandler().abort();
			logger.log(Level.SEVERE, "插入失败，事务回滚：" + e.getMessage(), e);
			throw new RuntimeException("数据插入失败", e);
		}
	}

	// --------------------- 辅助类和工具方法（与原代码保持一致） ---------------------

	private static class DatabaseConnection implements AutoCloseable {
		private final DbServerConfig config;
		private final ConnectionPoolDataSource dataSource; // 新增：连接池数据源

		public DatabaseConnection(DbServerConfig config) {
			this.config = config;
			// 初始化数据源（关键修改）
			this.dataSource = SparqlExecutionNew.createDataSource(config.getHost(), config.getPort(),
					config.getUsername(), config.getPassword());
			logger.info("连接到服务器：" + config.getHost() + ":" + config.getPort());
		}

		public DbServerConfig getConfig() {
			return config;
		}

		public ConnectionPoolDataSource getDataSource() {
			return dataSource; // 新增：暴露数据源
		}

		@Override
		public void close() {
			logger.info("断开连接：" + config.getHost() + ":" + config.getPort());
		}
	}

	private static class DbServerConfig {
		private final String host;
		private final int port;
		private final String username;
		private final String password;

		public DbServerConfig(String host, int port, String username, String password) {
			this.host = host;
			this.port = port;
			this.username = username;
			this.password = password;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}

	private static void closeResource(VirtGraph graph) {
		if (graph != null)
			graph.close();
	}
}