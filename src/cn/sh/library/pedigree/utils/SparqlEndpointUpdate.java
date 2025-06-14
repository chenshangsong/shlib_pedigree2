package cn.sh.library.pedigree.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.ConnectionPoolDataSource;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import cn.sh.library.pedigree.sysManager.sysMnagerSparql.Namespace;
import cn.sh.library.pedigree.webApi.sparql.SparqlNew.SparqlExecutionNew;
import virtuoso.jena.driver.VirtGraph;

/**
 * 增强版数据迁移工具类（优化子查询与SPARQL模板） 优化点： 1. 移除所有冗余子查询，改用多图联合匹配 2. 修正CONSTRUCT中变量未绑定问题
 * 3. 用三元组直接匹配替代FILTER，提升查询效率 4. 保持类型化配置扩展能力
 */
public class SparqlEndpointUpdate {
	private static final Logger logger = Logger.getLogger(SparqlEndpointUpdate.class.getName());
	public static String[] dataUrls = new String[] { "http://data.library.sh.cn/jp/resource/work/gr1eanpvw6x2tabt",
			"http://data.library.sh.cn/jp/resource/work/4321h1wgajpwpdit",
			"http://data.library.sh.cn/jp/resource/work/pcui814bs7psk6jl",
			"http://data.library.sh.cn/jp/resource/work/3kyuhuqjd8cix3db",
			"http://data.library.sh.cn/jp/resource/work/b3bgiggvf7c28lg8",
			"http://data.library.sh.cn/jp/resource/work/2b6n216i7z7cfewk",
			"http://data.library.sh.cn/jp/resource/work/311h88o3kfdv6sj7",
			"http://data.library.sh.cn/jp/resource/work/73j69ptuqhufhibz",
			"http://data.library.sh.cn/jp/resource/work/cinp7phw4o8qusei",
			"http://data.library.sh.cn/jp/resource/work/y4hthgs7n6weu4te",
			"http://data.library.sh.cn/jp/resource/work/if402ox2na2ivshb",
			"http://data.library.sh.cn/jp/resource/work/jtfcdymlx69t9s8d",
			"http://data.library.sh.cn/jp/resource/work/uyhiwamopj5g5nct",
			"http://data.library.sh.cn/jp/resource/work/ilakengn287p43tz",
			"http://data.library.sh.cn/jp/resource/work/bf3o9wlrivgdxf2p",
			"http://data.library.sh.cn/jp/resource/work/ymtwzexbtdwkrxqf",
			"http://data.library.sh.cn/jp/resource/work/w6p8ado87dbvdofe",
			"http://data.library.sh.cn/jp/resource/work/8psn1m5k2pfhwtmu",
			"http://data.library.sh.cn/jp/resource/work/qsj8nbc6jvf9tvjq",
			"http://data.library.sh.cn/jp/resource/work/gtucctst1m6obftf",
			"http://data.library.sh.cn/jp/resource/work/z850zrxheemqykxt",
			"http://data.library.sh.cn/jp/resource/work/q6dzsvrsmy5ki7e9",
			"http://data.library.sh.cn/jp/resource/work/rqkywniu8no1uirc",
			"http://data.library.sh.cn/jp/resource/work/wxb741n42wfjvmhk",
			"http://data.library.sh.cn/jp/resource/work/v6wajce37wdpjm2u",
			"http://data.library.sh.cn/jp/resource/work/x34xoyefxpppquoo",
			"http://data.library.sh.cn/jp/resource/work/ush8h3wmo3cbjqoa",
			"http://data.library.sh.cn/jp/resource/work/i83oagirm8x6p9gs",
			"http://data.library.sh.cn/jp/resource/work/ge3n8p4hs5i5a868",
			"http://data.library.sh.cn/jp/resource/work/7te5cxnxe7lid595",
			"http://data.library.sh.cn/jp/resource/work/trfl1suy9ibmwllw",
			"http://data.library.sh.cn/jp/resource/work/r55bopc6v7pf1746",
			"http://data.library.sh.cn/jp/resource/work/rbgtr2arypma6o47",
			"http://data.library.sh.cn/jp/resource/work/7hoyj86ieog7i7gt",
			"http://data.library.sh.cn/jp/resource/work/lwk1tf6lwzmbjakz",
			"http://data.library.sh.cn/jp/resource/work/y2pd8tn99t6ay64y",
			"http://data.library.sh.cn/jp/resource/work/ma5s7cju4zga6blr",
			"http://data.library.sh.cn/jp/resource/work/aoc51xnslg5dv1kj",
			"http://data.library.sh.cn/jp/resource/work/1rflqcgxnc0t6zns",
			"http://data.library.sh.cn/jp/resource/work/om8t3xlyuodgoqop",
			"http://data.library.sh.cn/jp/resource/work/wtqrefrr4i6r1ad6",
			"http://data.library.sh.cn/jp/resource/work/9d6t66p1ozcb1cok",
			"http://data.library.sh.cn/jp/resource/work/cm4x8odv3wtpxp32",
			"http://data.library.sh.cn/jp/resource/work/rvf9blsa512wtf3w",
			"http://data.library.sh.cn/jp/resource/work/eec1btso6h924re6",
			"http://data.library.sh.cn/jp/resource/work/f2cbfutg51r4gutq",
			"http://data.library.sh.cn/jp/resource/work/7zectq7oug47fhgt",
			"http://data.library.sh.cn/jp/resource/work/28hps42p5kb76nww",
			"http://data.library.sh.cn/jp/resource/work/jb8bifiyu9yaijpz",
			"http://data.library.sh.cn/jp/resource/work/zob12xez79xb8a7p",
			"http://data.library.sh.cn/jp/resource/work/rphqxk54gymais5p",
			"http://data.library.sh.cn/jp/resource/work/4qbly47irl8i8txx",
			"http://data.library.sh.cn/jp/resource/work/8b0kavpjv4ufrm6d",
			"http://data.library.sh.cn/jp/resource/work/kkgv5g289866rbm4",
			"http://data.library.sh.cn/jp/resource/work/ka2p6pvsxi18771e",
			"http://data.library.sh.cn/jp/resource/work/i6cinyvu1atnt3mv",
			"http://data.library.sh.cn/jp/resource/work/zj1mwbd2t7z47l7x",
			"http://data.library.sh.cn/jp/resource/work/gkyx47rjc4uj69zp",
			"http://data.library.sh.cn/jp/resource/work/3bhjarp5r935k79z",
			"http://data.library.sh.cn/jp/resource/work/zxww5ignvetnkd8d",
			"http://data.library.sh.cn/jp/resource/work/jklhb5c3ga1rvxe3" };
	// 源数据库配置
	private static final DbServerConfig SOURCE_SERVER = new DbServerConfig("10.1.31.194", 1111, "dba", "Shlibrary123");
	// 目标数据库列表（可扩展）
	private static final List<DbServerConfig> TARGET_SERVERS = Arrays.asList(
			new DbServerConfig("10.1.31.192", 1111, "dba", "Shlibrary123"),
			new DbServerConfig("172.29.45.107", 1111, "dba", "Shlibrary123"),
			new DbServerConfig("172.29.45.108", 1111, "dba", "Shlibrary123"));

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

//        System.out.print(_m);
		try {
//           openWorkData("http://data.library.sh.cn/jp/resource/work/ff6pp6x9s9ghcudh");   //公开
//			for (String str : dataUrls) {
////        		 deleteWorkData(str); //关闭
//
//				Thread.sleep(10 * 1000);
//			}
           deleteWorkData("http://data.library.sh.cn/jp/resource/work/ff6pp6x9s9ghcudh"); //关闭
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

	//更新全文开放标记状态
	public static void updateQWFlag(String workUri, String type, Integer status) {
		Map<String, Object> _parmMap = new HashMap<String, Object>();
		_parmMap.put("type", type);
		_parmMap.put("workUri", workUri);
		_parmMap.put("status", status);
		HttpsUtil.postJson("https://dhapi.library.sh.cn/service_pdf_race/race/pdf/set/status", null, _parmMap);
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