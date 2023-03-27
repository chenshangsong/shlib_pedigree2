package cn.sh.library.pedigree.webApi.controller.manager;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.common.FWConstants;
import cn.sh.library.pedigree.common.JSonUtils;
import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.dto.Work;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.model.DtoJsonPageData;
import cn.sh.library.pedigree.framework.util.PreloadUserList;
import cn.sh.library.pedigree.sysManager.model.ApiWorkFavoriteDto;
import cn.sh.library.pedigree.utils.DateUtilC;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.webApi.services.ApiWorkFavoriteService;
import cn.sh.library.pedigree.webApi.services.ApiWorkService;

/**
 * 用户登录Controller
 * 
 * @author chenss
 *
 */
@Controller
@RequestMapping("/webapi/manager/favorite")
public class ApiWorkFavoriteController extends BaseController {
	@Resource
	private ApiWorkFavoriteService apiWorkFavoriteService;
	@Resource
	private ApiWorkService apiWorkService;
	
	/**
	 * 用户收藏列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(ApiWorkFavoriteDto search, Pager pager, HttpSession hs){
		
		jsonResult = new HashMap<>();
		
		if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(search.getUid())).getId())){
		/*if (ifExsitUser(hs)) {*/
		DtoJsonPageData grid = new DtoJsonPageData(this);
		// 查询条件
		if (search == null) {
			search = new ApiWorkFavoriteDto();
		}
		search.setPage(pager.getPageth());// 当前yem
		search.setRows(pager.getPageSize());
		//searchdto.setUserId(getUser(hs).getId());
		search.setUserId(search.getUid());
		List<ApiWorkFavoriteDto> _list = apiWorkFavoriteService.getApiWorkFavoriteListPage(search);
		// 设定输出对象
		grid.print2JsonObj(search, _list);
		pager.calcPageCount(StringUtilC.getLong(String.valueOf(search.getRecords())));
		jsonResult.put("pager", pager);
		jsonResult.put("datas", grid.getRoot());
		jsonResult.put(result, FWConstants.result_success);
		} else {
			jsonResult.put(result, FWConstants.result_usernull);
			jsonResult.put("errCode", FWConstants.result_usernull);
			jsonResult.put("errMsg", FWConstants.result_usernull_msg);
		}
		return JSonUtils.toJSon(jsonResult);
	}

	/**
	 * 添加收藏
	 * 
	 * @param workUri
	 * @param hs
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid String workUri,@Valid Integer uid, HttpSession hs)throws Exception {
		
		try {
			jsonResult = new HashMap<>();
			
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(uid)).getId())){
				
				/*if (ifExsitUser(hs)) {*/
				ApiWorkFavoriteDto _dtoTemp = apiWorkFavoriteService.getApiWorkFavoriteByWorkUri(uid, workUri);
				// 已存在
				if (_dtoTemp != null && !StringUtilC.isEmpty(_dtoTemp.getId())) {
					jsonResult.put(result, FWConstants.result_hasexsit);
					return JSonUtils.toJSon(jsonResult);
				}
				// 可收藏
				else {
					ApiWorkFavoriteDto dto = new ApiWorkFavoriteDto();
					dto.setWorkUri(workUri);
					//获取详细信息
					Work _work =apiWorkService.getWork(workUri, false);
					if(_work!=null && !StringUtilC.isEmpty(_work.getUri())){
						dto.setCreator(_work.getCreator());
						dto.setTangh(_work.getTangh());
						dto.setTitle(_work.getTitle());
						if(_work.getInstances()!=null &&_work.getInstances().size()>0){
							dto.setPlace(_work.getInstances().get(0).get("place"));
							dto.setEdition(_work.getInstances().get(0).get("edition"));
							dto.setEditionTemporal(_work.getInstances().get(0).get("temporal"));
						}
					}
					dto.setCreatedUser(uid);
					//dto.setCreatedUser(getUser(hs).getId());
					dto.setUserId(dto.getCreatedUser());
					Integer favoriteId = apiWorkFavoriteService.insertApiWorkFavorite(dto);
					// 收藏成功
					if (!StringUtilC.isEmpty(favoriteId)) {
						jsonResult.put(result, FWConstants.result_success);
						jsonResult.put("favoriteId", dto.getId());
						return JSonUtils.toJSon(jsonResult);
					}
				}
				
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
			}
			return JSonUtils.toJSon(jsonResult);
			
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			jsonResult.put("errCode", FWConstants.result_error);
			jsonResult.put("errMsg", FWConstants.result_error_msg);
			return JSonUtils.toJSon(jsonResult);
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@Valid Integer id, @Valid Integer uid,HttpSession hs)throws Exception {
		
		try {
			jsonResult = new HashMap<>();
			
			if (!StringUtilC.isEmpty(PreloadUserList.getUserById(StringUtilC.getString(uid)).getId())){
			/*if (ifExsitUser(hs)) {*/
				// 执行更新
				int ifSucess = apiWorkFavoriteService.deleteApiWorkFavoriteById(id);
				// 如果成功
				if (ifSucess>= 0) {
					jsonResult.put(result, FWConstants.result_success);
				}
			} else {
				jsonResult.put(result, FWConstants.result_usernull);
				jsonResult.put("errCode", FWConstants.result_usernull);
				jsonResult.put("errMsg", FWConstants.result_usernull_msg);
			}
			return JSonUtils.toJSon(jsonResult);
			
		} catch (Exception e) {
			logger.info(this.getClass().getName() + "错误：" + DateUtilC.getNowDateTime() + "----" + e);
			jsonResult.put(result, FWConstants.result_error);
			jsonResult.put("errCode", FWConstants.result_error);
			jsonResult.put("errMsg", FWConstants.result_error_msg);
			return JSonUtils.toJSon(jsonResult);
		}
	}

}
