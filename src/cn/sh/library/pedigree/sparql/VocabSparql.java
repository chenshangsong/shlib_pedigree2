package cn.sh.library.pedigree.sparql;

import java.util.ArrayList;

import cn.sh.library.pedigree.dao.BaseDao;

public abstract interface VocabSparql extends BaseDao
{
  public abstract ArrayList getAllClasses();

  public abstract ArrayList getSubjectProperties(String paramString);

  public abstract String getPropertyType(String paramString);

  public abstract String getPropertyLabel(String paramString);

  public abstract String getPropertyComment(String paramString);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.VocabSparql
 * JD-Core Version:    0.6.2
 */