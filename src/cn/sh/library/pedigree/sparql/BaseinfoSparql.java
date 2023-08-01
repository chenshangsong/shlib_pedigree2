package cn.sh.library.pedigree.sparql;

import java.util.ArrayList;
import java.util.Map;

import cn.sh.library.pedigree.bean.AncTempSearchBean;
import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface BaseinfoSparql extends BaseDao {
	public abstract ArrayList getTemporal();

	public abstract Map getTime4Dyansty(String paramString);

	public abstract ArrayList getTemporals4TL();

	public abstract QueryResult<Map<String, Object>> getPersons(String paramString, int paramInt1, int paramInt2);

	public abstract QueryResult<Map<String, Object>> getWorks(String paramString, int paramInt1, int paramInt2);

	public abstract String getCHT4CHS(String paramString1, String paramString2);

	public abstract String getPersonFamilyName(String paramString);

	public abstract void generatePinYin();

	public abstract void export();

	public abstract ArrayList getFamilyNames(String paramString,Boolean accurateFlag);

	public abstract ArrayList getPersonFamilyNames();

	public abstract ArrayList getAllFamilyNames();

	public abstract QueryResult<Map<String, Object>> getAncestralTemple(AncTempSearchBean paramAncTempSearchBean,
			int paramInt1, int paramInt2);
	
	public abstract QueryResult<Map<String, Object>> getAncestralTempleForShiGuang(AncTempSearchBean paramAncTempSearchBean,
			int paramInt1, int paramInt2);
	
	public abstract QueryResult<Map<String, Object>> getOrganization(String paramString, int paramInt1, int paramInt2);

	//编目系统使用 chenss_20200630
	public abstract QueryResult<Map<String, Object>> getOrganizationForBM(String q, int start, int size);

	public abstract Map<String, String> getXings(String paramString);
}

/*
 * Location: C:\Users\chen\Desktop\pedigree-arq-1.06.jar Qualified Name:
 * cn.sh.library.pedigree.sparql.BaseinfoSparql JD-Core Version: 0.6.2
 */