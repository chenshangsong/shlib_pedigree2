package cn.sh.library.pedigree.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.ConnectionPoolDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import cn.sh.library.pedigree.annoation.GraphDefine;
import cn.sh.library.pedigree.dao.BaseDao;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Created by ly on 2014-10-17.
 */
public abstract class BaseDaoImpl implements BaseDao {

	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ConnectionPoolDataSource dataSource;
	// @Resource
	// private ApplicationContext applicationContext;
	private static Map<String, Model> modelCache = new HashMap<>();

	protected VirtGraph graph;

	protected Model model;

	@PostConstruct
	private void init() {
		GraphDefine graphDefine = this.getClass().getAnnotation(
				GraphDefine.class);
		if (graphDefine == null || StringUtils.isEmpty(graphDefine.name())) {
			throw new IllegalArgumentException("missing @GraphDefine");
		}

		String graphDefineName = graphDefine.name();
		model = modelCache.get(graphDefineName);
		if (model == null || model.isClosed()) {
			model = getModel(graphDefine.name());
			modelCache.put(graphDefine.name(), model);
		}
		modelCache.put("http://gen.library.sh.cn/graph/place",
				getModel("http://gen.library.sh.cn/graph/place"));
		modelCache.put("http://gen.library.sh.cn/graph/baseinfo",
				getModel("http://gen.library.sh.cn/graph/baseinfo"));
		modelCache.put("http://gen.library.sh.cn/graph/instance",
				getModel("http://gen.library.sh.cn/graph/instance"));
		modelCache.put("http://gen.library.sh.cn/graph/work",
				getModel("http://gen.library.sh.cn/graph/work"));
		modelCache.put("http://gen.library.sh.cn/graph/item",
				getModel("http://gen.library.sh.cn/graph/item"));
		modelCache.put("http://sd.library.sh.cn/graph/person",
				getModel("http://sd.library.sh.cn/graph/person"));
		graph = new VirtGraph(graphDefine.name(), dataSource);

	}

	protected Model getModel(String name) {
		Model model = modelCache.get(name);
		if (model == null || model.isClosed()) {
			model = VirtModel.openDatabaseModel(name, dataSource);
			modelCache.put(name, model);
		}
		if (model == null) {
			throw new NullPointerException(name + "不存在");
		}
		return model;
	}

	protected Model createModel(String name) {
		return null;
	}

}
