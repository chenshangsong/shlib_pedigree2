package cn.sh.library.pedigree.sparql;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.sh.library.pedigree.dao.BaseDao;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

public abstract interface JpUpdateSparql extends BaseDao {

	// 新增人物
	public void insertJp(JSONObject jsonObject, String workUri,
			String instanceUri, JSONArray items) throws Exception;
	// 新增人物
//		public void updatePerson(JSONObject jsonObject, String workUri) throws Exception;

	public void updateJp(JSONObject jsonObject,JSONArray items) throws Exception;
	
	public void updateJpWorkAccFlag(JSONObject jsonObject) throws Exception;
	
	public void deleteJp(String workUri ,String instanceUri) throws Exception;
	
	public void deleteTriplesBySp(String s,String p ,String graph) throws Exception;
	
	public void chageItemOf(JSONObject jsonObject ) throws Exception;
}
