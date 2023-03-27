package cn.sh.library.pedigree.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import cn.sh.library.pedigree.webServices.login.UserLoginService;
import cn.sh.library.pedigree.webServices.model.ParmResult;
import cn.sh.library.pedigree.webServices.model.UserLoginModel;
import cn.sh.library.pedigree.webServices.model.UserLoginResultModel;

/**
 * 
 * @author chenss
 *
 */

public class XMLUtils {
	/**
	 * 用户登录结果
	 */
	private static UserLoginResultModel _userLoginResultModel = new UserLoginResultModel();

	public static void main(String[] args) {
		try {
			// String xml = SoapInvoke.UserLogin("chen_emperor", "chen360121");
			// String xml = SoapInvoke.UserLogin_V2();
			UserLoginModel _model = new UserLoginModel();
			_model.setUid("chen_emperor");
			_model.setPwd("chen360121");
			String xml = UserLoginService.UserLogin_V2(_model);

			/*
			 * List<Map<String, String>> _list = parseDBXML(xml);
			 * 
			 * for (Map<String, String> map : _list) { System.out.println(map);
			 * }
			 */

			Document documentStr = StringToXML(xml);
			Map<String, Object> map = Dom2Map(documentStr);
			Map<String, Object> map2 = (Map) map.get("Body");
			Map<String, Object> map3 = (Map) map2.get("UserLogin_V2Response");
			System.out.println(map3.get("ParmResult"));
			String abc = map3.get("ParmResult").toString().replace("[{", "");
			abc = abc.replace("}]", "");
			abc = abc.replace("{", "");
			abc = abc.replace("name=", "");
			abc = abc.replace("value=", "");
			String[] arr = abc.split("},");
			List<String> list = java.util.Arrays.asList(arr);
			UserLoginResultModel _model2 = new UserLoginResultModel();
			for (String string : list) {
				String[] thisbe = string.split(",");
				String key = StringUtilC.getString(thisbe[0]);
				String value = StringUtilC.getString(thisbe[1]);
				System.out.println("key:" + key + "----------value:" + value);
				if (ParmResult.Sn.equals(key)) {
					_model2.getParmResult().setSn(value);
				}
				if (ParmResult.Cn.equals(key)) {
					_model2.getParmResult().setCn(value);
				}
				if (ParmResult.ShLibBirth.equals(key)) {
					_model2.getParmResult().setShLibBirth(value);
				}
				if (ParmResult.Mail.equals(key)) {
					_model2.getParmResult().setMail(value);
				}
				if (ParmResult.Mobile.equals(key)) {
					_model2.getParmResult().setMobile(value);
				}
				if (ParmResult.ShLibIdentityNo.equals(key)) {
					_model2.getParmResult().setShLibIdentityNo(value);
				}
				if (ParmResult.ShLibAskNo.equals(key)) {
					_model2.getParmResult().setShLibAskNo(value);
				}
				if (ParmResult.ShLibBorrower.equals(key)) {
					_model2.getParmResult().setShLibBorrower(value);
				}

			}
			// testMapVoid( (Map) map2.get("UserLogin_V2Response"));

			// 获取根节点元素对象
			Element node = documentStr.getRootElement();
			// 遍历所有的元素节点
			getUserLoginResult(node);
		} catch (Exception e) {

		}
		System.exit(0);
	}

	/*
	 * Create a XML document through String
	 */
	public static Document StringToXML(String str) throws DocumentException {
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
	public static UserLoginResultModel getUserLoginResult(Element node) {
		// System.out.println("当前节点的名称：：" + node.getName());
		// 获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();
		// 遍历属性节点
		for (Attribute attr : list) {
			// 总共参数数量
			if (attr.getName().equals("TotalParmNum")) {
				_userLoginResultModel.setTotalParmNum(attr.getValue());
			}
			// 访问结果Code
			if (attr.getName().equals("ResultCode")) {
				_userLoginResultModel.setResultCode(attr.getValue());
			}
			// 访问结果中文
			if (attr.getName().equals("Info")) {
				_userLoginResultModel.setInfo(attr.getValue());
			}
			// 访问时间
			if (attr.getName().equals("Time")) {
				_userLoginResultModel.setAskTime(DateUtilC.String2Date(attr
						.getValue()));
			}
		/*	System.out.println(attr.getText() + "-----" + attr.getName()
					+ "---" + attr.getValue());*/

		}
		// 当前节点下面子节点迭代器
		Iterator<Element> it = node.elementIterator();
		// 遍历
		while (it.hasNext()) {
			// 获取某个子节点对象
			Element e = it.next();
			// 对子节点进行遍历
			getUserLoginResult(e);
		}
		return _userLoginResultModel;
	}

	/**
	 * 设置返回参数
	 */
	public static void setParmResult(Document documentStr,
			UserLoginResultModel _model2) {
		// 得到第一层map
		Map<String, Object> map = Dom2Map(documentStr);
		// 得到第二层map
		Map<String, Object> map2 = (Map) map.get("Body");
		// 得到第三层map
		Map<String, Object> map3 = (Map) map2.get("UserLogin_V2Response");
		// 得到ParmResult字符串
		String stringValue = map3.get("ParmResult").toString()
				.replace("[{", "");
		// 字符串处理 Begin
		stringValue = stringValue.replace("}]", "");
		stringValue = stringValue.replace("{", "");
		stringValue = stringValue.replace("name=", "");
		stringValue = stringValue.replace("value=", "");
		// 字符串处理End
		String[] arr = stringValue.split("},");
		List<String> list = java.util.Arrays.asList(arr);
		for (String string : list) {
			String[] thisbe = string.split(",");
			String key = "";
			String value = "";
			if (thisbe.length ==1) {
				key = StringUtilC.getString(thisbe[0]).replace(" ", "");
			}
			else if (thisbe.length ==2) {
				key = StringUtilC.getString(thisbe[0]).replace(" ", "");
				value = StringUtilC.getString(thisbe[1]).replace(" ", "");
			}
			if (ParmResult.Sn.equals(key)) {
				_model2.getParmResult().setSn(value);
			} else if (ParmResult.Cn.equals(key)) {
				_model2.getParmResult().setCn(value);
			} else if (ParmResult.ShLibBirth.equals(key)) {
				_model2.getParmResult().setShLibBirth(value);
			} else if (ParmResult.Mail.equals(key)) {
				_model2.getParmResult().setMail(value);
			} else if (ParmResult.Mobile.equals(key)) {
				_model2.getParmResult().setMobile(value);
			} else if (ParmResult.ShLibIdentityNo.equals(key)) {
				_model2.getParmResult().setShLibIdentityNo(value);
			} else if (ParmResult.ShLibAskNo.equals(key)) {
				_model2.getParmResult().setShLibAskNo(value);
			} else if (ParmResult.ShLibBorrower.equals(key)) {
				_model2.getParmResult().setShLibBorrower(value);
			}

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

	public static List<Map<String, String>> parseDBXML(String configFile)
			throws Exception {
		List<Map<String, String>> dbConnections = new ArrayList<Map<String, String>>();
		Document document = StringToXML(configFile);
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

	@SuppressWarnings("unchecked")
	public static Map<String, Object> Dom2Map(Document doc) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			List list = e.elements();
			if (list.size() > 0) {
				map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	/**
	 * DOM2Map
	 * 
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}
}