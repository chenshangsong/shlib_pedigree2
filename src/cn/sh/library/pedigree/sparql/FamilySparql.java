package cn.sh.library.pedigree.sparql;

import java.util.ArrayList;

import cn.sh.library.pedigree.dao.BaseDao;

public abstract interface FamilySparql extends BaseDao
{
  public abstract ArrayList getFamilyRelations(String paramString);
}

/* Location:           C:\Users\chen\Desktop\pedigree-arq-1.06.jar
 * Qualified Name:     cn.sh.library.pedigree.sparql.FamilySparql
 * JD-Core Version:    0.6.2
 */