package cn.sh.library.pedigree.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLReader {
	/**
	 * 根据XML
	 * 
	 * @param path
	 * @param columName
	 * @return
	 */
	public static List<String> getXMLList(String path, String columName) {
		List<String> _list = new ArrayList<>();
		Element element = null;
		// 可以使用绝对路劲
		/* File f = new File("src/resources/rules/uris.xml"); */
		File f = new File(path);
		// documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			// 返回documentBuilderFactory对象
			dbf = DocumentBuilderFactory.newInstance();
			// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
			db = dbf.newDocumentBuilder();
			// 得到一个DOM并返回给document对象
			Document dt = db.parse(f);
			// 得到一个elment根元素
			element = dt.getDocumentElement();
			// 获得根节点
			// System.out.println("根元素：" + element.getNodeName());
			// 获得根元素下的子节点
			NodeList childNodes = element.getChildNodes();
			// 遍历这些子节点
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (columName.equals(childNodes.item(i).getNodeName())) {
					// 获得每个对应位置i的结点
					_list.add(childNodes.item(i).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _list;
	}

	// 封面图 20240629 chenss
	public static List<Map<String, String>> readFileToMapList(String filePath) {
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			return lines.filter(line -> line != null && line.length() >= 16) // 过滤掉空行和长度不足16的行
					.map(line -> {
						Map<String, String> map = new HashMap<>();
						String uri = "http://data.library.sh.cn/jp/resource/work/" + line.substring(0, 16);
						map.put("uri", uri);
						map.put("fileName", line);
						return map;
					}).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
