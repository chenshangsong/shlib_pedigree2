package cn.sh.library.pedigree.webApi.sparql;

import cn.sh.library.pedigree.sparql.Namespace;

public enum RdfQuery {

	RDF_CONSTRUCT_INSTANCE_PROPERTIES(Namespace.getNsPrefixString()
			+ " CONSTRUCT {?s ?p ?o} "
			+ " where {?itemUri a bf:Item  ;shl:DOI ?doi;bf:itemOf ?s. "
			+ " {?s a bf:Instance;?p ?o}.filter (?doi='%1$s')} "),

	RDF_CONSTRUCT_ITEM_PROPERTIES(Namespace.getNsPrefixString()
			+ " CONSTRUCT {?s ?p ?o} "
			+ " where {?s a bf:Item  ;?p ?o;bf:shelfMark ?shelfMark "
			+ " .filter (?shelfMark='%1$s')} "),

	RDF_CONSTRUCT_WORK_PROPERTIES(
			Namespace.getNsPrefixString()
					+ " CONSTRUCT {?s ?p ?o} "
					+ " where {?itemUri a bf:Item  ;shl:DOI ?doi;bf:itemOf ?instanceUri. "
					+ " {?instanceUri a bf:Instance;bf:instanceOf ?s. "
					+ " {?s a bf:Work;?p ?o}}.filter (?doi='%1$s')} ");

	protected String _query;

	RdfQuery(String query) {
		this._query = query;
	}

	@Override
	public String toString() {
		return this._query;
	}

	/**
	 * Query with formatting
	 * 
	 * @param args
	 * @return
	 */
	public String toString(Object... args) {
		return String.format(this._query, args);
	}
}
