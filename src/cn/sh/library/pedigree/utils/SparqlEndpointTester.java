package cn.sh.library.pedigree.utils;

import cn.sh.library.pedigree.sysManager.sysMnagerSparql.Namespace;
import cn.sh.library.pedigree.webApi.sparql.SparqlNew.SparqlExecutionNew;

public class SparqlEndpointTester {
	public static void main(String[] args) {
//		10.1.31.192、172.29.45.107、172.29.45.108
		SparqlExecutionNew.init("10.1.31.194", 1111, "dba", "Shlibrary123");
		String graphUri = "http://gen.library.sh.cn/graph/work";

		// SPARQL更新语句
		String colseQuery = Namespace.getNsPrefixString() + "WITH <" + graphUri + "> "
				+ "DELETE {?work rdf:type bf:Work. } " + "INSERT {?work rdf:type bf:Work_CLOSE. } " + "WHERE { "
				+ "VALUES ?work { <http://data.library.sh.cn/jp/resource/work/fntpzzidar5xqdib> } "
				+ "?work rdf:type bf:Work. }";
		String openQuery = Namespace.getNsPrefixString() + "WITH <" + graphUri + "> "
				+ "DELETE {?work rdf:type bf:Work_CLOSE. } " + "INSERT {?work rdf:type bf:Work. } " + "WHERE { "
				+ "VALUES ?work { <http://data.library.sh.cn/jp/resource/work/fntpzzidar5xqdib> } "
				+ "?work rdf:type bf:Work_CLOSE. }";

		try {
			// 执行带认证的Virtuoso更新
//			SparqlExecutionNew.update(SparqlExecutionNew.getGraph(graphUri), colseQuery);
			// 执行带认证的Virtuoso更新
						SparqlExecutionNew.update(SparqlExecutionNew.getGraph(graphUri), openQuery);
			System.out.println("更新操作已成功执行");
		} catch (Exception e) {
			System.err.println("更新操作失败: " + e.getMessage());
			e.printStackTrace();
		}
	}

}