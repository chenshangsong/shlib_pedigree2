package cn.sh.library.pedigree.common;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Statement;

import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface CommonSparql extends BaseDao
{
  public abstract boolean changeRDF(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);

  public abstract OutputStream getJsonLD4Resource(String paramString1, String paramString2);

  public abstract String getResourceClass(String paramString1, String paramString2);

  public abstract boolean deleteResource(String paramString1, String paramString2);

  public abstract QueryResult<Map<String, Object>> getResources(String paramString1, String paramString2, int paramInt1, int paramInt2);

  public abstract ArrayList getResInfos(String paramString1, String paramString2);

  public abstract ArrayList getPropertyValue(String paramString1, String paramString2, String paramString3);

  public abstract ArrayList getSpecPropValue(String paramString1, String paramString2, String paramString3, String paramString4);

  public abstract boolean isExist(String paramString1, String paramString2, String paramString3, String paramString4);

  public abstract OutputStream getTriples(String paramString1, boolean paramBoolean, String paramString2);

  public abstract OutputStream getTriples(String paramString1, String paramString2, String paramString3);

  public abstract List<Statement> getTriples(String paramString1, String paramString2);

  public abstract boolean moveTriples(String paramString1, String paramString2, String paramString3);

  public abstract boolean copyTriples(String paramString1, String paramString2, String paramString3);

  public abstract boolean copyStatements(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);

  public abstract boolean insertTriples(String paramString);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.common.CommonSparql
 * JD-Core Version:    0.6.2
 */