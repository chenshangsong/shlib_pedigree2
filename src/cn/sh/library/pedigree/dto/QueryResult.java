package cn.sh.library.pedigree.dto;

import java.util.ArrayList;
import java.util.List;

public class QueryResult<T> {

	private List<T> resultList = new ArrayList<T>();
	private Long totalrecord = 0L;
	
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public Long getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(Long totalrecord) {
		if( totalrecord == null ){
			this.totalrecord = 0L;
		} else {
			this.totalrecord = totalrecord;
		}
	}
	
}
