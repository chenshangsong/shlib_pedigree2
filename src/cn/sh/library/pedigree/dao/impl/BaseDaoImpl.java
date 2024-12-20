package cn.sh.library.pedigree.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private DataSource dataSource;

    private static Map<String, Model> modelCache = new HashMap<>();

    protected VirtGraph graph;

    protected Model model;

    // 数据库密码
    private static final String DATABASE_PASSWORD = "Shlibrary123";

    public void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                System.out.println("Connection successful!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        // testConnection();
        GraphDefine graphDefine = this.getClass().getAnnotation(GraphDefine.class);
        if (graphDefine == null || StringUtils.isEmpty(graphDefine.name())) {
            throw new IllegalArgumentException("missing @GraphDefine");
        }

        String graphDefineName = graphDefine.name();
        model = modelCache.get(graphDefineName);
        if (model == null || model.isClosed()) {
            model = getModel(graphDefine.name());
            modelCache.put(graphDefine.name(), model);
        }
        modelCache.put("http://gen.library.sh.cn/graph/instance", getModel("http://gen.library.sh.cn/graph/instance"));
        modelCache.put("http://gen.library.sh.cn/graph/work", getModel("http://gen.library.sh.cn/graph/work"));
        modelCache.put("http://gen.library.sh.cn/graph/item", getModel("http://gen.library.sh.cn/graph/item"));
        // modelCache.put("http://sd.library.sh.cn/graph/person", getModel("http://sd.library.sh.cn/graph/person"));

        // 使用 DataSource 获取连接信息并初始化 VirtGraph
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                String jdbcUrl = connection.getMetaData().getURL();
                String username = connection.getMetaData().getUserName();

                // 初始化 VirtGraph
                graph = new VirtGraph(graphDefine.name(), jdbcUrl, username, DATABASE_PASSWORD);
                System.out.println(graph);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Model getModel(String name) {
        Model model = modelCache.get(name);
        if (model == null || model.isClosed()) {
            try (Connection connection = dataSource.getConnection()) {
                if (connection != null) {
                    String jdbcUrl = connection.getMetaData().getURL();
                    String username = connection.getMetaData().getUserName();

                    // 创建并返回 VirtModel 实例
                    model = VirtModel.openDatabaseModel(name, jdbcUrl, username, DATABASE_PASSWORD);
                    modelCache.put(name, model);
                }
            } catch (SQLException e) {
                logger.error("Failed to open database model: " + name, e);
                throw new RuntimeException("Failed to open database model: " + name, e);
            }
        }
        if (model == null) {
            throw new NullPointerException(name + "不存在");
        }
        return model;
    }
}
