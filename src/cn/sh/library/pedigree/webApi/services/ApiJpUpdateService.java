package cn.sh.library.pedigree.webApi.services;

import net.sf.json.JSONObject;
import cn.sh.library.pedigree.services.BaseService;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

public interface ApiJpUpdateService extends BaseService {

	public void insertJp(JSONObject jsonObject, UserInfoModel userInfoModel,
			String workUri, String instanceUri)
			throws Exception;

	public void updateJp(JSONObject jsonObject, UserInfoModel userInfoModel);
	
	public void updateJpWorkAccFlag(JSONObject jsonObject, UserInfoModel userInfoModel);
	
	public void deleteJp(String workUri, String instanceUri,UserInfoModel userInfoModel);

	public void  chageItemOf (JSONObject jsonObject, UserInfoModel userInfoModel);

}
