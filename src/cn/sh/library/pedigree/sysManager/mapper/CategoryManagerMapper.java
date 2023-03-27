package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.CategoryModel;
import cn.sh.library.pedigree.sysManager.model.CategoryTypeModel;

@Repository
public interface CategoryManagerMapper {

	/**
	 * 获得分类列表
	 * @return
	 */
	List<CategoryTypeModel> getCategoryTypeList();
	
	/**
	 * 获得分类
	 * @param id
	 * @return
	 */
	CategoryTypeModel  getCategoryType(@Param(value = "id") Integer id);
	
	/**
	 * 添加分类
	 * @param model
	 * @return
	 */
	Integer insertCategoryType(CategoryTypeModel model);
	
	/**
	 * 更新分类
	 * @param model
	 * @return
	 */
	Integer updateCategoryType(CategoryTypeModel model);
	
	/**
	 * 添加取词值
	 * @param model
	 * @return
	 */
	Integer insertShlCategory(CategoryModel model);
	
	/**
	 * 删除分类
	 * @param id
	 * @return
	 */
	Integer deleteCategoryType(@Param(value = "id") Integer id);

}
