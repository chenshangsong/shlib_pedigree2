package cn.sh.library.pedigree.webApi.sparql.SparqlNew;


import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.utils.RDFUtils;
import cn.sh.library.pedigree.webApi.sparql.Namespace;

public class test {
	private static  int batchth=1;
	
		public static void main(String[] args) throws Exception {
			SparqlExecutionNew.init("10.1.31.194", 1111, "dba", "Shlibrary123");
			getYXLSList(batchth);
		}
		/**
		 * 优秀历史建筑
		 * @param batchth
		 * @throws Exception
		 */
		private static void getYXLSList(int batchth) throws Exception {
			String uri = "";
String[] header = new String[] { "uri","labelChs","labelCht","labelEn"};
			// 每个线程分配到需要执行的uri
			String getUriSparql = "select * where {?uri a shl:FamilyName; bf:label ?labelChs;bf:label ?labelCht;bf:label ?labelEn\r\n"
					+ "   .FILTER langMatches(lang(?labelChs), \"chs\" )\r\n"
					+ "   .FILTER langMatches(lang(?labelCht), \"cht\" )\r\n"
					+ "   .FILTER langMatches(lang(?labelEn), \"en\" ) }";
			
			List<Map<String, String>> dataList = RDFUtils
					.transformListMap(SparqlExecutionNew.vQuery(SparqlExecutionNew.getGraph("http://gen.library.sh.cn/graph/baseinfo"), Namespace.getNsPrefixString() + getUriSparql,  
							header));

			for (int i = 0; i < dataList.size(); i++) {
				System.out.println(i);
			}
		
		}
}
