package cn.sh.library.pedigree.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.FullImgAnnotationService;
import cn.sh.library.pedigree.sysManager.mapper.FullImgAnnotationMapper;
import cn.sh.library.pedigree.sysManager.model.FullImgAnnotationModel;
/**
 * 
 * @author chenxu
 *
 */
@Service
public class FullImgAnnotationServiceImpl implements FullImgAnnotationService {
	@Resource
	private FullImgAnnotationMapper fullImgAnnotationMapper;
	
	//查询
	@Override
	public List<FullImgAnnotationModel> getFIAnnotations(FullImgAnnotationModel fullImgAnnotationModel) {
		return fullImgAnnotationMapper.getFIAnnotations(fullImgAnnotationModel);
	}
	//插入
	@Override
	public void insertFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel) {
		fullImgAnnotationMapper.insertFIAnnotation(fullImgAnnotationModel);
	}
	//修改
	@Override
	public void updateFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel) {
		fullImgAnnotationMapper.updateFIAnnotation(fullImgAnnotationModel);
	}
	//删除
	@Override
	public void deleteFIAnnotation(FullImgAnnotationModel fullImgAnnotationModel) {
		fullImgAnnotationMapper.deleteFIAnnotation(fullImgAnnotationModel);
	}
	
}
