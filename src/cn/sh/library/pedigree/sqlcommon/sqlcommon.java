package cn.sh.library.pedigree.sqlcommon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sqlcommon {
	private String url = "jdbc:sqlserver://";// SQLServer路径
	private String serverName = "localhost";// 服务器名称
	private String portNumber = "1433";// 端口号
	private String databaseName = "shlibrary";// 数据库名称
	private String userName = "sa";// 登陆名
	private String password = "chen360121";// 密码
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;

	public sqlcommon() {

	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getPortNumber() {
		return this.portNumber;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getdatabaseName() {
		return this.databaseName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	// 返回连接url
	private String getConnectionUrl() {
		return url + serverName + ":" + portNumber + ";databaseName="
				+ databaseName + ";";
	}

	// 获得数据库联接驱动
	public Connection getConnection() {
		try {
			// 加载驱动
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
		try {
			// 获得驱动对象
			con = DriverManager.getConnection(getConnectionUrl(), userName,
					password);
		} catch (SQLException ex1) {
			ex1.printStackTrace();
			return null;
		}
		return con;
	}

	// 获得连接对象
	public Statement getStatement() {
		try {
			st = getConnection().createStatement();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return st;
	}

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet getResultSet(String sql) {
		try {
			rs = getStatement().executeQuery(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return rs;
	}

	/**
	 * ResultSetTOlist
	 * 
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	/**
	 * 更新数据库信息
	 * 
	 * @param sql
	 * @return
	 */
	public int getUpdate(String sql) {
		try {
			return getStatement().executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	// 数据关闭驱动对象
	public void close() {
		try {
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// 关闭连接对象
	public void closeStatement() {
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// 关闭结果集
	public void closeRestultSet() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
			}
		}
	}
}