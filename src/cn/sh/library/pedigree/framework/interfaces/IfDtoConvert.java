package  cn.sh.library.pedigree.framework.interfaces;

import cn.sh.library.pedigree.framework.model.BaseDto;

public interface  IfDtoConvert{
	/**
	 * 
	 * @param dto
	 */
	void toFormDto(BaseDto dto);
	void toDbDto(BaseDto dto);

}
