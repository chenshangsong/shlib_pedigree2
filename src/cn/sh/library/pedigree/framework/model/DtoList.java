package  cn.sh.library.pedigree.framework.model;

import java.util.ArrayList;
import java.util.List;



public class DtoList extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -207382037574663996L;
	
	private List<Object> baseDtoList = new ArrayList<Object>();

	public List<Object> getBaseDtoList() {
		return baseDtoList;
	}

	public void setBaseDtoList(List<Object> baseDtoList) {
		this.baseDtoList = baseDtoList;
	}
	
	public Object getObject(int index, Object defaultVal) {
		List<Object> list = baseDtoList;
		if(list != null){
			if(index < baseDtoList.size()){
				return list.get(index);
			}
		}
		return defaultVal;
	}
}
