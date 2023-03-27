package cn.sh.library.pedigree.framework.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mybatis拦截器，将拦截mybatis执行时如下sql映射： <select id="getUser" resultType="int">
 * [resource="user:register"] SELECT login_name$needAuthc("loginName"),
 * password$needAuthc("password") FROM users WHERE login_name=#{loginName}
 * </select> 进行权限校验后返回拥有权限的字段查询结果
 * 
 * @version 1.00 2011-12-6
 * @author Yockii Hsu
 * 
 */
@Intercepts(value = {
		@Signature(args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }, method = "query", type = Executor.class),
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }) })
public class ColumnsControlInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(ColumnsControlInterceptor.class);

//	private Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation
				.getArgs()[0];
		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);

//		String methodName = invocation.getMethod().getName();

		String sql = boundSql.getSql().trim();

		if (sql.startsWith("[resource")) {
			StringBuffer resource = new StringBuffer(
					sql.split("\"]")[0].substring(sql.indexOf("\"") + 1));
			String nextSql = sql.split("\"]")[1].trim();

			if (nextSql
					.matches("^(?i)select.*\\$needAuthc\\(\"[a-z0-9]*\"\\).*")) {
				String regEx = "(?i)[_a-z0-9]+\\$needAuthc\\(\"[a-z0-9]*\"\\)";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(nextSql);
				List<String> list = new ArrayList<String>();
				while (m.find()) {
					list.add(m.group());
				}
				Subject subject = SecurityUtils.getSubject();
				for (int i = list.size() - 1; i >= 0; i--) {
					String str = list.get(i);
					String fieldPermission = str.substring(
							str.indexOf("\"") + 1, str.lastIndexOf("\""));
					String permission = resource.append(fieldPermission)
							.toString();
					if (subject.isPermitted(permission)) {
						list.remove(i);
					}
				}
				for (String str : list) {
					nextSql = nextSql.replaceAll(
							str.replaceAll("\\)", "\\\\\\\\\\)")
									.replaceAll("\\$", "\\\\\\\\\\$")
									.replaceAll("\\(", "\\\\\\\\\\("), "");
					logger.debug(str);
					logger.debug(nextSql);
				}
			}

			// 将sql转换为正常的sql语句
			sql = nextSql.replaceAll("(?i)\\$needAuthc\\(\"[a-z0-9]*\"\\)", "");
		}

		// 替换原来的拼装模板
		final BoundSql newBoundSql = new BoundSql(
				mappedStatement.getConfiguration(), sql,
				boundSql.getParameterMappings(), boundSql.getParameterObject());

		Builder builder = new MappedStatement.Builder(
				mappedStatement.getConfiguration(), mappedStatement.getId(),
				new SqlSource() {

					@Override
					public BoundSql getBoundSql(Object parameterObject) {
						return newBoundSql;
					}
				}, mappedStatement.getSqlCommandType());
		builder.cache(mappedStatement.getCache());
		builder.fetchSize(mappedStatement.getFetchSize());
		builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
		builder.keyGenerator(mappedStatement.getKeyGenerator());
		builder.keyProperty(mappedStatement.getKeyProperty());
		builder.resource(mappedStatement.getResource());
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.resultSetType(mappedStatement.getResultSetType());
		builder.statementType(mappedStatement.getStatementType());
		builder.timeout(mappedStatement.getTimeout());
		builder.useCache(mappedStatement.isUseCache());

		MappedStatement newMappedStatement = builder.build();
		invocation.getArgs()[0] = newMappedStatement;

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
//		this.properties = properties;
	}

}