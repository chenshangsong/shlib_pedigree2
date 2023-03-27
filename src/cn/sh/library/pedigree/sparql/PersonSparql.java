package cn.sh.library.pedigree.sparql;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.bean.PersonSearchBean;
import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface PersonSparql extends BaseDao {
	public abstract QueryResult<Map<String, Object>> getPersons(
			PersonSearchBean paramPersonSearchBean, int paramInt1, int paramInt2);

	public abstract OutputStream getPersons4API(
			PersonSearchBean paramPersonSearchBean, String paramString);

	public abstract QueryResult<Map<String, Object>> getPersonsInHome(
			String paramString, int paramInt1, int paramInt2);

	public abstract String countPersons(String paramString);

	public abstract ArrayList getPersonsInDynasty(int paramInt);

	public abstract List<Map<String, String>> getInfos4Person(
			String paramString, boolean paramBoolean);

	/**
	 * 根據人的URI获取人的详细信息，chenss20170821
	 * 
	 * @param uri
	 * @return
	 */
	public List<Map<String, String>> getInfos4Person(String uri);

	public ArrayList getImg(String uri);

	public abstract ArrayList getPersons4Work(String paramString);

	public abstract ArrayList getFamRels4Work(String paramString);

	public abstract void export();

	public abstract QueryResult<Map<String, Object>> getPersons(
			String paramString, int paramInt1, int paramInt2);

	public abstract QueryResult<Map<String, Object>> getPersons(
			PersonSearchBean paramPersonSearchBean, int paramInt1,
			String paramString, int paramInt2, int paramInt3);

	public abstract ArrayList<Map<String, String>> countResState(
			String paramString, int paramInt);
}

/*
 * Location: C:\Users\chen\Desktop\pedigree-arq-1.06.jar Qualified Name:
 * cn.sh.library.pedigree.sparql.PersonSparql JD-Core Version: 0.6.2
 */