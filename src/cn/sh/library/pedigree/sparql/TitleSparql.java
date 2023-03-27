package cn.sh.library.pedigree.sparql;

import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface TitleSparql extends BaseDao
{
  public abstract QueryResult<Map<String, Object>> getTitles(String paramString, int paramInt1, int paramInt2);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.TitleSparql
 * JD-Core Version:    0.6.2
 */