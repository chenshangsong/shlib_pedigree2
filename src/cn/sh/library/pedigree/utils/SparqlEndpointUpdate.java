package cn.sh.library.pedigree.utils;

import cn.sh.library.pedigree.sysManager.sysMnagerSparql.Namespace;
import cn.sh.library.pedigree.webApi.sparql.SparqlNew.SparqlExecutionNew;

import java.util.Arrays;
import java.util.List;

public class SparqlEndpointUpdate {
    // 定义三个数据库服务器信息
    private static final List<DbServerConfig> DB_SERVERS = Arrays.asList(
        new DbServerConfig("10.1.31.192", 1111, "dba", "Shlibrary123"),
        new DbServerConfig("172.29.45.107", 1111, "dba", "Shlibrary123"),
        new DbServerConfig("172.29.45.108", 1111, "dba", "Shlibrary123")
    );
    
    // 数据库服务器配置类
    private static class DbServerConfig {
        private String host;
        private int port;
        private String username;
        private String password;
        
        public DbServerConfig(String host, int port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }
        
        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
    }

    public static void main(String[] args) {
        String workUri = "http://data.library.sh.cn/jp/resource/work/fntpzzidar5xqdib";

        try {
            // 关闭工作（在所有三个数据库上执行）
//            closeWork(workUri);
//            System.out.println("工作已成功关闭（所有数据库）");
            
            // 打开工作（在所有三个数据库上执行）
            openWork(workUri);
            System.out.println("工作已成功打开（所有数据库）");
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 关闭工作：将 bf:Work 类型更改为 bf:Work_CLOSE
     * 在所有三个数据库上执行此操作
     * @param workUri 工作资源的URI
     */
    public static void closeWork(String workUri) throws Exception {
        String graphUri = "http://gen.library.sh.cn/graph/work";
        
        // 构建SPARQL更新语句
        String closeQuery = Namespace.getNsPrefixString() +
                           "WITH <" + graphUri + "> " +
                           "DELETE { <" + workUri + "> rdf:type bf:Work. } " +
                           "INSERT { <" + workUri + "> rdf:type bf:Work_CLOSE. } " +
                           "WHERE { <" + workUri + "> rdf:type bf:Work. }";

        // 在所有数据库上执行更新
        executeUpdateOnAllServers(graphUri, closeQuery);
    }

    /**
     * 打开工作：将 bf:Work_CLOSE 类型更改为 bf:Work
     * 在所有三个数据库上执行此操作
     * @param workUri 工作资源的URI
     */
    public static void openWork(String workUri) throws Exception {
        String graphUri = "http://gen.library.sh.cn/graph/work";
        
        // 构建SPARQL更新语句
        String openQuery = Namespace.getNsPrefixString() +
                          "WITH <" + graphUri + "> " +
                          "DELETE { <" + workUri + "> rdf:type bf:Work_CLOSE. } " +
                          "INSERT { <" + workUri + "> rdf:type bf:Work. } " +
                          "WHERE { <" + workUri + "> rdf:type bf:Work_CLOSE. }";

        // 在所有数据库上执行更新
        executeUpdateOnAllServers(graphUri, openQuery);
    }

    /**
     * 在所有配置的数据库服务器上执行更新操作
     */
    private static void executeUpdateOnAllServers(String graphUri, String updateQuery) throws Exception {
        Exception firstException = null;
        
        for (DbServerConfig server : DB_SERVERS) {
            try {
                // 初始化特定服务器的连接
                SparqlExecutionNew.init(server.getHost(), server.getPort(), 
                                       server.getUsername(), server.getPassword());
                
                // 执行更新
                SparqlExecutionNew.update(SparqlExecutionNew.getGraph(graphUri), updateQuery);
                
                System.out.println("在服务器 " + server.getHost() + " 上执行更新成功");
            } catch (Exception e) {
                System.err.println("在服务器 " + server.getHost() + " 上执行更新失败: " + e.getMessage());
                
                // 记录第一个异常，后续继续尝试其他服务器
                if (firstException == null) {
                    firstException = e;
                }
            }
        }
        
        // 如果所有服务器都失败，抛出第一个捕获到的异常
        if (firstException != null) {
            throw new Exception("在部分或全部数据库服务器上执行更新失败", firstException);
        }
    }
}