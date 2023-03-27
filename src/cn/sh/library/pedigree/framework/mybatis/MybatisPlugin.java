package cn.sh.library.pedigree.framework.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.MyBatisSystemException;

import cn.sh.library.pedigree.framework.commom.MsgId;
import cn.sh.library.pedigree.framework.exception.ExclusiveException;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.framework.util.ReflectHelper;
import cn.sh.library.pedigree.framework.util.StringUtil;

/**
 * 类名 : MybatisPlugin <br>
 * 机能概要 : Mybatis数据库访问的插件，主要进行分页，排他控制，以及特殊字段参数的解析</br> 版权所有: Copyright © 2011
 * TES Corporation, All Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MybatisPlugin implements Interceptor {

	private static String dialect = ""; // 数据库方言
	private static String pageSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)
	private static String insertSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)
	private static String updateSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)
	private static String exclusiveField = ""; // 排他字段
	private static String exclusiveSqlId = ""; // 排他方法名结束符

	public Object intercept(Invocation ivk) throws Throwable {
		// TODO Auto-generated method stub
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk
					.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper
					.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper
					.getValueByFieldName(delegate, "mappedStatement");
			BoundSql boundSql = delegate.getBoundSql();
			Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
			if (mappedStatement.getId().matches(pageSqlId)) { // 拦截需要分页的SQL
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject尚未实例化！");
				} else {
					Connection connection = (Connection) ivk.getArgs()[0];
					String sqlCount = boundSql.getSql();
					//System.out.println("pagesql_Befor:"+sqlCount);
					String countStart = "select count(0) from ( ";
					String countEnd = " ) as tmp_count";
					int orderIndex = sqlCount.lastIndexOf("ORDER BY");
					if (orderIndex > 0) {
						sqlCount = countStart
								+ sqlCount.substring(0, orderIndex) + countEnd; // 记录统计
					} else {
						sqlCount = countStart + sqlCount + countEnd; // 记录统计
					}
					PreparedStatement countStmt = connection
							.prepareStatement(sqlCount);
					BoundSql countBS = new BoundSql(
							mappedStatement.getConfiguration(), sqlCount,
							boundSql.getParameterMappings(), parameterObject);
					setParameters(countStmt, mappedStatement, countBS,
							parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					DtoJsonPageData page = (DtoJsonPageData) parameterObject;
					page.setRecords(count);
					String pageSql = generatePageSql(boundSql.getSql(), page);
					//System.out.println("pageSql_After:"+pageSql);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
				}
			} else if (mappedStatement.getId().matches(insertSqlId)
					|| mappedStatement.getId().matches(updateSqlId)) {
			
				String sql = boundSql.getSql();/*
				if (sql.indexOf("getUser()") >= 0
						|| sql.indexOf("getStoreId()") >= 0) {
					DtoUser user = ControllerUtil.getDtoUser();
					sql = sql.replace("getUser()", user.getUserId());
					sql = sql.replace("getStoreId()", user.getStore_id());
				}*/
				// 排他check
				if (mappedStatement.getId().matches(updateSqlId)
						&& !StringUtil.isEmpty(exclusiveField)
						&& mappedStatement.getId().endsWith(exclusiveSqlId)) {

					String _sql = "SELECT " + exclusiveField + " FROM "
							+ sql.split(" ")[1];
					int whereI = sql.indexOf("WHERE");
					if (whereI > 0) {
						_sql = _sql + " " + sql.substring(whereI);
					}
					// 取得原sql的参数MapMapping
					List<ParameterMapping> list = new ArrayList<ParameterMapping>();
					list.addAll(boundSql.getParameterMappings());
					// 根据？较少的个数
					int updateParamCount = sql.length()
							- sql.replace("?", "").length();
					int selectParamCount = _sql.length()
							- _sql.replace("?", "").length();
					for (int i = 0; i < updateParamCount - selectParamCount; i++) {
						list.remove(0);
					}
					Connection connection = (Connection) ivk.getArgs()[0];
					PreparedStatement stmt = connection.prepareStatement(_sql);
					//System.out.println("listSql:"+_sql);
					BoundSql countBS = new BoundSql(
							mappedStatement.getConfiguration(), _sql, list,
							parameterObject);
					setParameters(stmt, mappedStatement, countBS,
							parameterObject);
					ResultSet rs = stmt.executeQuery();
					String oldUpdateTime = "";
					if (rs.next()) {
						oldUpdateTime = rs.getString(exclusiveField);
					}

					rs.close();
					stmt.close();
					String newUpdateTime = (String) ReflectHelper
							.getValueByFieldName(parameterObject, "updated_on");
					if (oldUpdateTime == null
							|| !oldUpdateTime.equals(newUpdateTime)) {
						throw new MyBatisSystemException(
								new ExclusiveException(MsgId.E0008));
					}

				}
				// 把Sql中的getUser()替换为登录用户Id
				//System.out.println("insertSqlId:"+sql);
				ReflectHelper.setValueByFieldName(boundSql, "sql", sql);
			}
		}
		return ivk.proceed();
	}

	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.
	 * DefaultParameterHandler
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters")
				.object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration
					.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null
					: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry
							.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName
							.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(
											propertyName.substring(prop
													.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject
								.getValue(propertyName);
					}
					@SuppressWarnings("unchecked")
					TypeHandler<Object> typeHandler = parameterMapping
							.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException(
								"There was no TypeHandler found for parameter "
										+ propertyName + " of statement "
										+ mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value,
							parameterMapping.getJdbcType());
				}
			}
		}
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql, DtoJsonPageData page) {
		if (page != null && !StringUtil.isEmpty(dialect)) {
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + page.getPage() * page.getRows()
						+ "," + page.getRows());
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") as tmp_tb where ROWNUM<=");
				pageSql.append(page.getPage() * page.getRows() + page.getRows());
				pageSql.append(") where row_id>");
				pageSql.append(page.getPage() * page.getRows());
			} else if ("sqlserver".equals(dialect)) {
				int currentPage = page.getPage();
				if (currentPage == 0) {
					currentPage = 1;
				}
				pageSql.append("SELECT * FROM (");
				pageSql.append("               SELECT row_number() OVER (ORDER BY tempColumn) tempRowNumber, *");
				pageSql.append("               FROM (SELECT TOP " + currentPage
						* page.getRows() + " tempColumn = 0,"
						+ sql.replaceFirst("SELECT", "") + ") t) tt");
				pageSql.append(" WHERE tempRowNumber > " + (currentPage - 1)
						* page.getRows());
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (StringUtil.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		pageSqlId = p.getProperty("pageSqlId");
		if (StringUtil.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		insertSqlId = p.getProperty("insertSqlId");
		updateSqlId = p.getProperty("updateSqlId");
		exclusiveField = p.getProperty("exclusiveField");
		exclusiveSqlId = p.getProperty("exclusiveSqlId");
	}
}