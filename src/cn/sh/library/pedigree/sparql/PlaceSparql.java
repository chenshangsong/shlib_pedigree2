package cn.sh.library.pedigree.sparql;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;

public abstract interface PlaceSparql extends BaseDao
{
	public List<Map<String, String>> getRemoteAllPlaces();
  public abstract ArrayList getFullLocations(String paramString1, String paramString2);

  public abstract String getSameAs(Map paramMap);

  public abstract ArrayList getPlacesInArea(String paramString);

  public abstract ArrayList getPlacesInArea(String paramString1, String paramString2);
  
  public abstract Map getPlacesInAreaByCircle(String point, Integer distance,String fname);
  

  public abstract ArrayList getAllPlaces();
  public abstract ArrayList getAllPlaces(String keyWord);
  public abstract ArrayList getAllPlacesInOrigin();

  public abstract ArrayList getRDF(String paramString);

  public abstract ArrayList getStandPlace(String paramString);

  public abstract ArrayList getPlaces(String paramString);

  public abstract String getLabel(String paramString);

  public abstract String getLongLat(String paramString);

  public abstract String getUri4StandPlace(String paramString1, String paramString2);

  public abstract void linkPlace2Geo();

  public abstract QueryResult<Map<String, Object>> getPlaces(String paramString, int paramInt1, int paramInt2);

  public abstract OutputStream getTriples(String paramString1, String paramString2);
ArrayList getPlacesInArea(String points, String familyName, String freetext, String startYear, String endYear);
ArrayList getPlacesInAreaCount(String points, String familyName, String freetext, String startYear, String endYear);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.PlaceSparql
 * JD-Core Version:    0.6.2
 */