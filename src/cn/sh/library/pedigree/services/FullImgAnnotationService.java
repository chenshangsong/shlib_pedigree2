package cn.sh.library.pedigree.services;
/**
 * 
 * @author chenxu
 *
 */

import java.util.List;

import cn.sh.library.pedigree.sysManager.model.FullImgAnnotationModel;

public interface FullImgAnnotationService {
	//查询
	List<FullImgAnnotationModel> getFIAnnotations(FullImgAnnotationModel fullImgAnnotationModel);
	//插入
	void insertFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel);
	//修改
	void updateFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel);
	//删除
	void deleteFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel);
}
