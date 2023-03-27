package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.FullImgAnnotationModel;

/**
 * 
 * @author chenxu
 *
 */
@Repository
public interface FullImgAnnotationMapper {
	//查询
	List<FullImgAnnotationModel> getFIAnnotations(FullImgAnnotationModel fullImgAnnotationModel);
	//插入
	void insertFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel);
	//修改
	void updateFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel);
	//删除
	void deleteFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel);
}
