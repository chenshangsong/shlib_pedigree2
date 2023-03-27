package cn.sh.library.pedigree.sysManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by rabbit on 14-5-25.
 */
public class ChangeDataSource {
	public static void main(String[] args) {
		// 初始化ApplicationContext
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"resources/pedigree-context.xml");

		//MySqlMapper mySqlMapper = applicationContext.getBean(MySqlMapper.class);

		SqlServerMapper sqlServerMapper = applicationContext
				.getBean(SqlServerMapper.class);

		// 设置数据源为MySql,使用了AOP测试时请将下面这行注释
	//	MultipleDataSource.setDataSourceKey("dataSource");
	//	mySqlMapper.getList();
		// 设置数据源为SqlServer,使用AOP测试时请将下面这行注释
		MultipleDataSource.setDataSourceKey("sqlServerDataSource");
		sqlServerMapper.getList();
	}
}