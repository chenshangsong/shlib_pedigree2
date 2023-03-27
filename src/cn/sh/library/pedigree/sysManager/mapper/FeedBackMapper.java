package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.FeedBackConfModel;
import cn.sh.library.pedigree.sysManager.model.FeedBackDetailModel;
import cn.sh.library.pedigree.sysManager.model.FeedBackMainModel;

@Repository
public interface FeedBackMapper {

	/**
	 * 反馈信息
	 * @param userId
	 * @return
	 */
	List<FeedBackMainModel> getFeedBackMainList(@Param(value = "createdUser") String userId);
	/**
	 * 反馈信息弹框
	 * @param userId
	 * @return
	 */
	List<FeedBackConfModel> getFeedBackConfList(FeedBackConfModel detail);
	
	/**
	 * 反馈详细
	 * @param userId
	 * @return
	 */
	FeedBackDetailModel getDetail(@Param(value = "feedbackMainId") Integer feedbackMainId);
	
	/***
	 * 更新批注信息
	 * 
	 * @param info
	 */
	Integer updatePostil(FeedBackDetailModel detail);
	
	/***
	 * 添加反馈标题列表
	 * 
	 * @param info
	 */
	Integer insertFeedBackMain(FeedBackDetailModel detail);
	
	/***
	 * 添加反馈信息
	 * 
	 * @param info
	 */
	Integer insertFeedBackList(FeedBackDetailModel detail);
}
