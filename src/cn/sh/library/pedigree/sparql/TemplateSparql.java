package cn.sh.library.pedigree.sparql;

import cn.sh.library.pedigree.dao.BaseDao;

public abstract interface TemplateSparql extends BaseDao
{
  public abstract void addBFProfile(String paramString);

  public abstract String getLatestTemplate();
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.TemplateSparql
 * JD-Core Version:    0.6.2
 */