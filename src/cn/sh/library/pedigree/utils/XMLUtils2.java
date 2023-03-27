package cn.sh.library.pedigree.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import cn.sh.library.pedigree.webServices.login.UserLoginService;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;

/**
 * 
 * @author chenss
 *
 */

public class XMLUtils2 {
	public static void main(String[] args) {
		XMLUtils2 dom4jSample = new XMLUtils2();
		try {
			// String xml = SoapInvoke.UserLogin("chen_emperor", "chen360121");
			// String xml = SoapInvoke.UserLogin_V2();
			UserLoginModel _model = new UserLoginModel();
			_model.setUid("chen_emperor");
			_model.setPwd("chen360121");
			String xml = UserLoginService.UserLogin_V2(_model);
			Document documentStr = dom4jSample.StringToXML(xml);
			// 获取根节点元素对象
			Element node = documentStr.getRootElement();
			// 遍历所有的元素节点
			listNodes(node);
			FindElement(documentStr);
			// 获取四大名著元素节点中，子节点名称为红楼梦元素节点。
			Element element = node.element("ParmResult");
			// System.out.println("asdfasdfasdf"+element());
		} catch (Exception e) {

		}
		System.exit(0);
	}

	/*
	 * Create a XML Document
	 */
	public Document createDocument() {
		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("root");

		Element author1 = root.addElement("Lynch");
		author1.addAttribute("Age", "25");
		author1.addAttribute("Country", "China");
		author1.addText("I am great!");

		Element author2 = root.addElement("Legend");
		author2.addAttribute("Age", "25");
		author2.addAttribute("Country", "China");
		author2.addText("I am great!too!");

		return document;
	}

	/*
	 * Create a XML document through String
	 */
	public Document StringToXML(String str) throws DocumentException {
		Document document = DocumentHelper.parseText(str);
		return document;
	}

	public static Element FindElement(Document document) {
		Element root = document.getRootElement();
		Element legend = null;
		for (Iterator i = root.elementIterator("soapenv:Body"); i.hasNext();) {
			legend = (Element) i.next();
		}
		System.out.println("登录结果：" + legend.elementTextTrim("ResultCode"));
		return legend;
	}

	/*
	 * Write a XML file
	 */
	public void FileWrite(Document document) throws IOException {
		FileWriter out = new FileWriter("C:/Dom2jSample.xml");
		document.write(out);
		out.close();
	}

	/*
	 * Write a XML format file
	 */
	public void XMLWrite(Document document) throws IOException {
		XMLWriter writer = new XMLWriter(
				new FileWriter("C:/Dom2jSampleStr.xml"));
		writer.write(document);
		writer.close();
	}

	/**
	 * 遍历当前节点元素下面的所有(元素的)子节点
	 * 
	 * @param node
	 */
	public static void listNodes(Element node) {
		System.out.println("当前节点的名称：：" + node.getName());
		// 获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();
		// 遍历属性节点
		for (Attribute attr : list) {
			System.out.println(attr.getText() + "-----" + attr.getName()
					+ "---" + attr.getValue());

		}

		if (!(node.getTextTrim().equals(""))) {
			System.out.println("文本内容：：：：" + node.getText());
		}

		// 当前节点下面子节点迭代器
		Iterator<Element> it = node.elementIterator();
		// 遍历
		while (it.hasNext()) {
			// 获取某个子节点对象
			Element e = it.next();
			// 对子节点进行遍历
			listNodes(e);
		}

	}

	/**
	 * 介绍Element中的element方法和elements方法的使用
	 * 
	 * @param node
	 */
	public void elementMethod(Element node) {
		// 获取node节点中，子节点的元素名称为西游记的元素节点。
		Element e = node.element("name");
		// 获取西游记元素节点中，子节点为作者的元素节点(可以看到只能获取第一个作者元素节点)
		Element author = e.element("作者");

		System.out.println(e.getName() + "----" + author.getText());

		// 获取西游记这个元素节点 中，所有子节点名称为作者元素的节点 。

		List<Element> authors = e.elements("作者");
		for (Element aut : authors) {
			System.out.println(aut.getText());
		}

		// 获取西游记这个元素节点 所有元素的子节点。
		List<Element> elements = e.elements();

		for (Element el : elements) {
			System.out.println(el.getText());
		}

	}
}