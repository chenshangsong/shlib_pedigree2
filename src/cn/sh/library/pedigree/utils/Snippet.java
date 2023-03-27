package cn.sh.library.pedigree.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Snippet {
	/**
	 * 解析包含有DB连接信息的XML文件 格式必须符合如下规范： 1. 最多三级，每级的node名称自定义； 2.
	 * 二级节点支持节点属性，属性将被视作子节点； 3. CDATA必须包含在节点中，不能单独出现。
	 *
	 * 示例1——三级显示： <db-connections> <connection> <name>DBTest</name>
	 * <jndi></jndi> <url>
	 * <![CDATA[jdbc:mysql://localhost:3306/db_test?useUnicode
	 * =true&characterEncoding=UTF8]]> </url>
	 * <driver>org.gjt.mm.mysql.Driver</driver> <user>test</user>
	 * <password>test2012</password> <max-active>10</max-active>
	 * <max-idle>10</max-idle> <min-idle>2</min-idle> <max-wait>10</max-wait>
	 * <validation-query>SELECT 1+1</validation-query> </connection>
	 * </db-connections>
	 *
	 * 示例2——节点属性： <bookstore> <book category="cooking"> <title
	 * lang="en">Everyday Italian</title> <author>Giada De Laurentiis</author>
	 * <year>2005</year> <price>30.00</price> </book>
	 *
	 * <book category="children" title="Harry Potter" author="J K. Rowling"
	 * year="2005" price="$29.9"/> </bookstore>
	 *
	 * @param configFile
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> parseDBXML(String configFile)
			throws Exception {
		List<Map<String, String>> dbConnections = new ArrayList<Map<String, String>>();
		InputStream is = Snippet.class.getResourceAsStream(configFile);
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(is);
		Element connections = document.getRootElement();

		Iterator<Element> rootIter = connections.elementIterator();
		while (rootIter.hasNext()) {
			Element connection = rootIter.next();
			Iterator<Element> childIter = connection.elementIterator();
			Map<String, String> connectionInfo = new HashMap<String, String>();
			List<Attribute> attributes = connection.attributes();
			for (int i = 0; i < attributes.size(); ++i) { // 添加节点属性
				connectionInfo.put(attributes.get(i).getName(),
						attributes.get(i).getValue());
			}
			while (childIter.hasNext()) { // 添加子节点
				Element attr = childIter.next();
				connectionInfo
						.put(attr.getName().trim(), attr.getText().trim());
			}
			dbConnections.add(connectionInfo);
		}

		return dbConnections;
	}
}
