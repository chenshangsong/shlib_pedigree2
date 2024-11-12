package cn.sh.library.pedigree.utils.sprqlchenss;

import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.sparql.Namespace;
import cn.sh.library.pedigree.utils.RDFUtils;

public class Jp_ItemAll {
	private static int batchth = 1;

	public static void main(String[] args) throws Exception {
		SparqlExecution.init("127.0.0.1", 1111, "dba", "Shlibrary123");
		getYXLSList(batchth);
	}

	/**
	 * 优秀历史建筑
	 * 
	 * @param batchth
	 * @throws Exception
	 */
	private static void getYXLSList(int batchth) throws Exception {
		String uri = "";
		String[] header = new String[] { "work", "ins", "item", "shelfMark", "DOI", "hasFullImg", "accessLevel", "by",
				"orglabel", "orgabName" };
		// 每个线程分配到需要执行的uri
		String getUriSparql = "SELECT  DISTINCT ?work ?ins  ?item ?shelfMark ?DOI ?hasFullImg ?by ?accessLevel ?orglabel ?orgabName"
				+ "  FROM <http://gen.library.sh.cn/graph/baseinfo>\r\n"
				+ "  FROM <http://gen.library.sh.cn/graph/title>\r\n"
				+ "  FROM <http://gen.library.sh.cn/graph/work>\r\n"
				+ "  FROM <http://gen.library.sh.cn/graph/person>\r\n"
				+ "  FROM <http://gen.library.sh.cn/graph/place>\r\n"
				+ "  FROM <http://gen.library.sh.cn/graph/instance>\r\n"
				+ "  FROM <http://gen.library.sh.cn/graph/item>\r\n" + " WHERE {\r\n"
				+ "	?work a bf:Work ; dc:title ?dtitle .FILTER (lang(?dtitle) = 'chs')\r\n"

				+ "  {?ins a bf:Instance \r\n"

				+ "     {?ins bf:instanceOf ?work}\r\n" + "  }\r\n"
				+ "  {?item a bf:Item  OPTIONAL {?item shl:DOI ?DOI .} OPTIONAL {?item shl:accessLevel ?accessLevel .}  OPTIONAL {?item bf:shelfMark ?shelfMark .} OPTIONAL {?item shl:hasFullImg ?hasFullImg .} optional{?item bf:heldBy ?by .{?by bf:label ?orglabel; shl:abbreviateName ?orgabName.filter(lang(?orgabName) = 'chs') .filter(lang(?orglabel) = 'chs')}}  {?item bf:itemOf ?ins.{?ins a bf:Instance; bf:instanceOf ?work}}}\r\n"
				+ " } limit 10";

		List<Map<String, String>> dataList = RDFUtils.transformListMap(
				SparqlExecution.vQuery(SparqlExecution.getGraph("http://gen.library.sh.cn/graph/work"),
						Namespace.getNsPrefixString() + getUriSparql, header));

		System.out.println("记录条数：" + dataList.size());

		dataList.stream().forEach(item -> {
			System.out.println(item);
		});
	}
}
