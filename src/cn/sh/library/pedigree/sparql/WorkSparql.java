package cn.sh.library.pedigree.sparql;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import cn.sh.library.pedigree.bean.WorkSearchBean;
import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface WorkSparql extends BaseDao
{
  public abstract QueryResult<Map<String, Object>> getWorksInFree(String paramString, int paramInt1, int paramInt2);

  public abstract QueryResult<Map<String, Object>> getWorks(WorkSearchBean paramWorkSearchBean, int paramInt1, int paramInt2);
  
  public Long countItemsByWork(WorkSearchBean bean);

  public abstract ArrayList getTitles(String paramString);

  public abstract ArrayList getCreator(String paramString);

  public abstract ArrayList getWorksInLatLong(String paramString);

  public abstract ArrayList getWorksInPlace(Map paramMap, String paramString);

  public abstract ArrayList getWorksInLatLong(String paramString1, String paramString2);

  public abstract ArrayList getWorkPlaces(String paramString);

  public abstract ArrayList getWorks4Person(String paramString, boolean paramBoolean);

  public abstract Map getWorkInfos(String paramString);

  public abstract ArrayList getInstances4Work(String paramString);

  public abstract ArrayList getWorksInYear(String paramString1, String paramString2, int paramInt);

  public abstract ArrayList getWorksInChao(String paramString);

  public abstract ArrayList getAllWorksWithGeo();

  public abstract OutputStream getWorkAllInfos(String paramString);

  public abstract OutputStream getResource(String paramString);

  public abstract OutputStream getTriples(WorkSearchBean paramWorkSearchBean, String paramString);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.WorkSparql
 * JD-Core Version:    0.6.2
 */