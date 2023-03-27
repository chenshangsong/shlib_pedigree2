package cn.sh.library.pedigree.sparql;

import java.util.ArrayList;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;

public abstract interface MigrationSparql extends BaseDao
{
  public abstract ArrayList getMigrations();

  public abstract ArrayList getMigrationLocations();

  public abstract ArrayList getMigrationLines();

  public abstract String getFirstLevel();

  public abstract ArrayList getAfterLevel(String paramString);

  public abstract Map getInfos(String paramString);

  public abstract String getUriFromName(String paramString);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.MigrationSparql
 * JD-Core Version:    0.6.2
 */