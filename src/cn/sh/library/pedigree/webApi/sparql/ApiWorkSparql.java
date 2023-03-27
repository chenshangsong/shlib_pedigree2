package cn.sh.library.pedigree.webApi.sparql;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.dto.QueryResult;
import cn.sh.library.pedigree.webApi.dto.searchBean.ApiWorkSearchBean;

public abstract interface ApiWorkSparql extends BaseDao
{

  public abstract QueryResult<Map<String, Object>> getWorks(ApiWorkSearchBean paramApiWorkSearchBean, int paramInt1, int paramInt2);

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

  public abstract ArrayList getWorksInChao(String paramString,Boolean isMore);

  public abstract ArrayList getAllWorksWithGeo();

  public abstract OutputStream getWorkAllInfos(String paramString);

  public abstract OutputStream getResource(String paramString);

  public abstract OutputStream getTriples(ApiWorkSearchBean paramApiWorkSearchBean, String paramString);
  
  public abstract ArrayList getFacetCount(ApiWorkSearchBean bean);
  
  public ArrayList getFacetCountOthers(ApiWorkSearchBean bean);
  
  public abstract ArrayList getFreeResultList(String free_text,Integer maxCount);
  public abstract Map getDoiByWorkUri(String workUri);
  public abstract List tongji(String furi);
  public abstract Map getDetailByWorkUri(String workUri);

ArrayList getWorksInPlace(Map standPlace, String familyName, String freetext, String startYear, String endYear);

//东馆用：迁徙统计
public abstract Map getQxTjInfo(String fname);
//东馆用：姓氏分布统计
public abstract Map getPlaceTjInfo(String fname);

public  Integer getWorkCountByPlaceAndFname(List<String> place,String fnameUri);
}