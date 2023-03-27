package  cn.sh.library.pedigree.framework.model;


public interface BaseConvert {

	void toServiceDto(BaseDto dto);
	
	void toControllerDto(BaseDto dto);
}
