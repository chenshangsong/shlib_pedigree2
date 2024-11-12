package cn.sh.library.pedigree.webApi.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.services.impl.BaseServiceImpl;
import cn.sh.library.pedigree.sparql.JpUpdateSparql;
import cn.sh.library.pedigree.sysManager.mapper.DataChangeLogMapper;
import cn.sh.library.pedigree.sysManager.model.DateChangeLogModel;
import cn.sh.library.pedigree.sysManager.model.UserInfoModel;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiJpUpdateService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ApiJpUpdateServiceImpl extends BaseServiceImpl implements ApiJpUpdateService {

	@Resource
	private JpUpdateSparql JpUpdateSparql;

	@Resource
	private DataChangeLogMapper dataChangeLogMapper;


	@Override
	public void insertJp(JSONObject jsonObject, UserInfoModel userInfoModel, String workUri, String instanceUri)
			throws Exception {
		// TODO Auto-generated method stub
		DateChangeLogModel model = new DateChangeLogModel();
		model.setWorkUri(workUri);
		model.setInstanceUri(instanceUri);

		JSONArray items = jsonObject.getJSONArray("items");

		for (int i = 0; i < items.size(); i++) {

			JSONObject object = items.getJSONObject(i);

			object.put("itemUri", Constant.URI_PREFIX_ITEM + StringUtilC.getRandomUriValue(16));
		}

		model.setItems(items.toString());

		model.setContent(jsonObject.toString());

		if (!StringUtilC.isEmpty(userInfoModel)) {

			if (userInfoModel.getUserId() != null) {
				model.setUserId(userInfoModel.getUserId());
			}

			if (userInfoModel.getUserName() != null) {
				model.setUserName(userInfoModel.getUserName());
			}

		}

		try {

			JpUpdateSparql.insertJp(jsonObject, workUri, instanceUri, items);

			model.setType("success");

			dataChangeLogMapper.updateJpLog(model);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(this.getClass().getName() + ";insertJp:---" + e);
			model.setType("failure");
			dataChangeLogMapper.updateJpLog(model);
			throw new Exception("编辑失败：" + e);
		}

	}

//	@Override
//	public void updatePerson(JSONObject jsonObject, UserInfoModel userInfoModel, String workUri) throws Exception {
//		// TODO Auto-generated method stub
//		DateChangeLogModel model = new DateChangeLogModel();
//		model.setWorkUri(workUri);
//		model.setContent(jsonObject.toString());
//
//		if (!StringUtilC.isEmpty(userInfoModel)) {
//
//			if (userInfoModel.getUserId() != null) {
//				model.setUserId(userInfoModel.getUserId());
//			}
//
//			if (userInfoModel.getUserName() != null) {
//				model.setUserName(userInfoModel.getUserName());
//			}
//
//		}
//
//		try {
//
//			JpUpdateSparql.updatePerson(jsonObject, workUri);
//
//			model.setType("success");
//
//			dataChangeLogMapper.updateJpLog(model);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(this.getClass().getName() + ";insertJp:---" + e);
//			model.setType("failure");
//			dataChangeLogMapper.updateJpLog(model);
//			throw new Exception("编辑失败：" + e);
//		}
//
//	}

	@Override
	public void updateJp(JSONObject jsonObject, UserInfoModel userInfoModel) {
		// TODO Auto-generated method stub
		DateChangeLogModel model = new DateChangeLogModel();
		String workUri = jsonObject.getString("workUri");
		String instanceUri = jsonObject.getString("instanceUri");
		model.setUri(workUri);
		model.setWorkUri(workUri);
		model.setInstanceUri(instanceUri);
		JSONArray items = new JSONArray();
		if (jsonObject.containsKey("items")) {
			items = jsonObject.getJSONArray("items");

			for (int i = 0; i < items.size(); i++) {

				JSONObject object = items.getJSONObject(i);

				object.put("itemUri", Constant.URI_PREFIX_ITEM + StringUtilC.getRandomUriValue(16));
			}

			model.setItems(items.toString());
		}

		model.setContent(jsonObject.toString());
		if (!StringUtilC.isEmpty(userInfoModel)) {
			model.setUserId(userInfoModel.getUserId());
			model.setUserName(userInfoModel.getUserName());
		}

		try {
			JpUpdateSparql.updateJp(jsonObject, items);
			model.setType("success");

			dataChangeLogMapper.updateJpLog(model);

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println(e.toString());
			model.setType("failure");
			dataChangeLogMapper.updateJpLog(model);
		}

	}

	@Override
	public void updateJpWorkAccFlag(JSONObject jsonObject, UserInfoModel userInfoModel) {
		// TODO Auto-generated method stub
		DateChangeLogModel model = new DateChangeLogModel();
		String workUri = jsonObject.getString("workUri");
		model.setUri(workUri);
		model.setWorkUri(workUri);
		model.setContent(jsonObject.toString());
		if (!StringUtilC.isEmpty(userInfoModel)) {
			model.setUserId(userInfoModel.getUserId());
			model.setUserName(userInfoModel.getUserName());
		}

		try {

			JpUpdateSparql.updateJpWorkAccFlag(jsonObject);

			model.setType("success");

			dataChangeLogMapper.updateJpLog(model);

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println(e.toString());
			model.setType("failure");
			dataChangeLogMapper.updateJpLog(model);
		}

	}

	@Override
	public void deleteJp(String workUri, String instanceUri, UserInfoModel userInfoModel) {
		// TODO Auto-generated method stub
		DateChangeLogModel model = new DateChangeLogModel();
		try {

			JpUpdateSparql.deleteJp(workUri, instanceUri);
			model.setUri(workUri);
			model.setWorkUri(workUri);
			model.setInstanceUri(instanceUri);
			model.setType("success");
			model.setContent("content delete.");
			dataChangeLogMapper.updateJpLog(model);
		} catch (Exception e) {
			// TODO: handle exception

			model.setType("failure");
			dataChangeLogMapper.updateJpLog(model);
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void chageItemOf(JSONObject jsonObject, UserInfoModel userInfoModel) {
		// TODO Auto-generated method stub
		DateChangeLogModel model = new DateChangeLogModel();
		String itemUri = jsonObject.getString("itemUri");
		model.setUri(itemUri);
		model.setWorkUri(itemUri);
		model.setContent(jsonObject.toString());
		if (!StringUtilC.isEmpty(userInfoModel)) {
			model.setUserId(userInfoModel.getUserId());
			model.setUserName(userInfoModel.getUserName());
		}

		try {

			JpUpdateSparql.chageItemOf(jsonObject);

			model.setType("success");

			dataChangeLogMapper.updateJpLog(model);

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println(e.toString());
			model.setType("failure");
			dataChangeLogMapper.updateJpLog(model);
		}

	}

	@Override
	public void deleteTriplesBySp(String s, String p, String graph) {
		try {
			JpUpdateSparql.deleteTriplesBySp(s, p, graph);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
