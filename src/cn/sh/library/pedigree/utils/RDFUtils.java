package cn.sh.library.pedigree.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;

/**
 * Created by Yi on 2014/10/29 0029.
 */
public final class RDFUtils {

	public static List<Map<String, String>> transformListMap(
			List<Map<String, Object>> list) {
		List<Map<String, String>> resultList = new ArrayList<>();
		if (list != null) {
			for (Map<String, Object> item : list) {
				resultList.add(transform(item));
			}
		}
		return resultList;
	}

	public static Map<String, String> transform(Map<String, Object> map) {
		if (map == null) {
			return null;
		}
		Map<String, String> result = new LinkedHashMap<>();
		for (String key : map.keySet()) {
			result.put(key, RDFUtils.toString(map.get(key)));
		}
		return result;
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		if (obj instanceof Literal) {
			Literal literal = (Literal) obj;
			return literal.getString();
		}
		return obj.toString();
	}

	/**
	 * 根据前缀获取链接地址
	 * 
	 * @param model
	 * @param shortLabel
	 * @return
	 */
	public static String getLink(Model model, String shortLabel) {
		if (StringUtils.isEmpty(shortLabel)) {
			return "";
		}

		if (shortLabel.startsWith("http://")) {
			return shortLabel;
		}

		String[] temps = shortLabel.split(":");
		if (temps.length != 2)
			return "";
		String prefix = temps[0];
		String link = model.getNsPrefixURI(prefix);
		if (StringUtils.isEmpty(link)) {
			return "";
		}
		return link + temps[1];
	}

	// 返回Resource的缩写形式
	public static String toShortLabel(Model model, Object obj) {
		String shortLabel = "";
		try {
			if (obj == null)
				return "";
		

			if (obj instanceof Resource) {
				String label = "";
				String uri = "";
				if (obj.toString().contains("#")) {
					uri = StringUtils.substringBeforeLast(obj.toString(), "#")
							+ "#";
					label = StringUtils.substringAfterLast(obj.toString(), "#");
				} else {
					uri = StringUtils.substringBeforeLast(obj.toString(), "/")
							+ "/";
					label = StringUtils.substringAfterLast(obj.toString(), "/");
				}

				String prefix = model.getNsURIPrefix(uri);

				if (StringUtils.isNotBlank(prefix)) {
					shortLabel = prefix + ":" + label;
				} else {
					shortLabel = obj.toString();
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		

		return shortLabel;
	}

	// 返回Resource的Label
	public static String getLabel(Object obj) {
		if (obj == null)
			return "";

		if (obj.toString().contains("#")) {
			return StringUtils.substringAfterLast(obj.toString(), "#");
		}

		return StringUtils.substringAfterLast(obj.toString(), "/");
	}

	// 获取多语言的Value
	public static String getValue(String str) {
		if (str.contains("@")) {
			return str.split("@")[0];
		}

		if (str.contains("^^")) {
			return str.split("\\^\\^")[0];
		}

		return str;
	}

	// 获取多语言的Value
	public static String getValueLang(String str) {

		if (str.contains("^^")) {
			return str.split("\\^\\^")[0];
		}

		return str;
	}

	public static Map<String, Object> getMap(ArrayList list,
			Map<String, String> renames, String[] listFields) {
		Map result = new HashMap();

		for (int i = 0; i < list.size(); i++) {
			String p = ((Map) list.get(i)).get("p").toString();
			p = p.substring(p.lastIndexOf("/") + 1);
			p = p.indexOf("#") != -1 ? p.substring(p.lastIndexOf("#") + 1) : p;

			String o = RDFUtils.getValueLang(((Map) list.get(i)).get("o")
					.toString());
			Object obj = result.get(p);
			if (obj == null) {
				Object value = o;
				if (listFields != null && Arrays.asList(listFields).contains(p)) {
					List sList = new ArrayList();
					sList.add(o);
					value = sList;
				}
				if (renames != null && renames.size() > 0) {
					for (int j = 0; j < renames.size(); j++) {
						for (Map.Entry entry : renames.entrySet()) {
							if (((String) entry.getKey()).equals(p)) {
								result.put(entry.getValue(), value);
								break;
							}
							result.put(p, value);
						}
					}
				} else {
					result.put(p, value);
				}
			} else if ((obj instanceof List)) {
				((List) obj).add(o);
			} else {
				List sList = new ArrayList();
				sList.add(obj.toString());
				sList.add(o);
				result.put(p, sList);
			}

		}

		return result;
	}
	public static Map<String, Object> getMap(ArrayList<Map> list, String... args) {
	    Map<String, Object> result = new HashMap<>();
	    for (int i = 0; i < list.size(); i++) {
	      String p = ((Map)list.get(i)).get("p").toString();
	      p = p.substring(p.lastIndexOf("/") + 1);
	      p = (p.indexOf("#") != -1) ? p.substring(p.lastIndexOf("#") + 1) : p;
	      Object o = ((Map)list.get(i)).get("o");
	      Object obj = result.get(p);
	      if (obj == null) {
	        if (args.length == 2) {
	          if (args[0].equals(p)) {
	            if (StringUtils.isNotBlank(args[1])) {
	              result.put(args[1], getValue(o.toString()));
	            } else {
	              result.put(((LiteralImpl)o).getLanguage() + "_" + p, getValue(o.toString()));
	            } 
	          } else {
	            result.put(p, o);
	          } 
	        } else {
	          result.put(p, o);
	        } 
	      } else if (obj instanceof List) {
	        ((List<Object>)obj).add(o);
	      } else {
	        List<String> sList = new ArrayList<>();
	        sList.add(obj.toString());
	        sList.add(getValue(o.toString()));
	        result.put(p, sList);
	      } 
	    } 
	    return result;
	  }
	  
	  public static Map<String, Object> getMap(Model model, ArrayList<Map> list) {
	    Map<String, Object> result = new HashMap<>();
	    for (int i = 0; i < list.size(); i++) {
	      String p = ((Map)list.get(i)).get("p").toString();
	      String prefix_uri = "", node = "";
	      if (p.contains("#")) {
	        prefix_uri = p.substring(0, p.lastIndexOf("#") + 1);
	        node = p.substring(p.lastIndexOf("#") + 1);
	      } else {
	        prefix_uri = p.substring(0, p.lastIndexOf("/") + 1);
	        node = p.substring(p.lastIndexOf("/") + 1);
	      } 
	      p = model.getNsURIPrefix(prefix_uri) + "_" + node;
	      String o = getValue(((Map)list.get(i)).get("o").toString());
	      Object obj = result.get(p);
	      if (obj == null) {
	        result.put(p, o);
	      } else if (obj instanceof List) {
	        ((List<String>)obj).add(o);
	      } else {
	        List<String> sList = new ArrayList<>();
	        sList.add(obj.toString());
	        sList.add(o);
	        result.put(p, sList);
	      } 
	    } 
	    return result;
	  }
}
