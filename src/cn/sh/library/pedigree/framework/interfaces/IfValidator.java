package  cn.sh.library.pedigree.framework.interfaces;

import cn.sh.library.pedigree.framework.model.BaseDto;

public interface IfValidator {
	
	void validate(BaseDto arg0) throws Exception;
	
	/**
	 * 简单的check，包括字段长度，格式，以及多字段的关联性（开始，结束日期大小）
	 * @param dto check的BaseDto对象
	 * @throws Exception 
	 */
	void simpleCheck(BaseDto dto) throws Exception;
	
	/**
	 * 业务相关check，主要指设计数据库进行数据整合性判断
	 * @param dto check的BaseDto对象
	 */
	void businessCheck(BaseDto dto) throws Exception;
}
