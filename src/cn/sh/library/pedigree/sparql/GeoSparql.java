package cn.sh.library.pedigree.sparql;

import cn.sh.library.pedigree.dao.BaseDao;

public abstract interface GeoSparql extends BaseDao
{
  public abstract String getLongLat(String paramString);

  public abstract String getCitySubject(String paramString);

  public abstract String getCitySubject(String paramString1, String paramString2);

  public abstract String getTownNumber(String paramString);

  public abstract String getCity4Point(String paramString1, String paramString2);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.GeoSparql
 * JD-Core Version:    0.6.2
 */