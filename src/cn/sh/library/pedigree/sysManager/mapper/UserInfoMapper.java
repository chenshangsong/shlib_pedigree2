package cn.sh.library.pedigree.sysManager.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.UserInfoModel;

@Repository
public interface UserInfoMapper {
	
	
	/**
	 * 登录查询
	 * 
	 * @param userId
	 * @param userpwd
	 * @return
	 */
	UserInfoModel getUser(@Param(value = "userId") String userId,
			@Param(value = "userPwd") String userPwd);

	/**
	 * 根据ID查询信息
	 * 
	 * @param id
	 * @return
	 */
	UserInfoModel getUserById(@Param(value = "id") String id);

	/**
	 * 查询用户信息
	 * 
	 * @param info
	 * @return
	 */
	UserInfoModel getUserByInfo(UserInfoModel info);
	
	/**
	 * 查询用户信息
	 * 
	 * @param info
	 * @return
	 */
	UserInfoModel getUserByShLibIdentityNo(@Param(value = "shLibIdentityNo") String shLibIdentityNo);
	/**
	 * 查询用户信息
	 * 
	 * @param userID
	 * @return
	 */
	UserInfoModel getUserByUserId(@Param(value = "userId") String userId);

	/**
	 * 得到一览画面
	 * 
	 * @param dpudc
	 * @return
	 */
	ArrayList<UserInfoModel> getTableDataListPage(UserInfoModel dcsdpc);

	/**
	 * 检查用户是否存在
	 * 
	 * @param info
	 * @return
	 */
	UserInfoModel selectCountUser(UserInfoModel info);

	/**
	 * 插入用户信息
	 * 
	 * @param info
	 * @return
	 */
	int insertUser(UserInfoModel info);

	/***
	 * 更新用户角色信息
	 * 
	 * @param info
	 */
	int updateUserRoleById(UserInfoModel info);

	/**
	 * 更新用户信息
	 * 
	 * @param info
	 */
	int updateUserById(UserInfoModel info);

	
}
