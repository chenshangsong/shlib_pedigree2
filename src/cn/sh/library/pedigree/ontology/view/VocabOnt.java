package cn.sh.library.pedigree.ontology.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.ontology.visualizer.AnalyzedGraph;

public abstract interface VocabOnt extends BaseDao
{
  public abstract String getClassComment(String paramString);

  public abstract List getShortClasses();

  public abstract List getShortProperties();

  public abstract ArrayList getClassInfos(String paramString);

  public abstract ArrayList getPropertyInfos(String paramString);

  public abstract List getModelViewClasses();

  public abstract List getSuperClasses();

  public abstract List getSubClasses(String paramString);

  public abstract List getClassProperties(String paramString);

  public abstract OutputStream write();

  public abstract OutputStream write(String paramString);

  public abstract AnalyzedGraph getAnalyzedGraph();

  public abstract boolean validate(String paramString1, String paramString2);

  public abstract boolean modify(String paramString1, String paramString2, String paramString3, String paramString4);

  public abstract boolean delete(String paramString1, String paramString2, String paramString3, String paramString4);

  public abstract boolean create(String paramString1, String paramString2, String paramString3, String paramString4);

  public abstract boolean importRDF(InputStream paramInputStream, boolean paramBoolean);

  public abstract boolean importRDF(String paramString, boolean paramBoolean);
}

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.view.VocabOnt
 * JD-Core Version:    0.6.2
 */