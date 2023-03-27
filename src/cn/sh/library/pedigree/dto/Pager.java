package cn.sh.library.pedigree.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * Pager对象封装分页和排序查询
 * @author liuyi
 */
public class Pager implements Serializable {

	private static final long serialVersionUID = 2041867004894668270L;
	private Logger logger = Logger.getLogger(Pager.class);

	/** 默认页码列表大小 */
	public final static int DEFAULT_DISPLAY_PAGETHS_COUNT = 6;
	/** 开始索引 */
	Integer startIndex = 0;
	/** 分页大小 */
	Integer pageSize = 10;
	/** 排序字段 */
	String sort = "insertTime";
	/** 排序方向 */
	String dir = "desc";
	/** 当前页码 */
	Integer pageth = 1;
	/** 页码列表 */
	List<Integer> pageths;
	/** 总页码数 */
	Integer pageCount = 0;
	/** 总记录数 */
	Long rowCount = 0L;	
	
	public Pager() {}

	public Pager(Integer startIndex, Integer pageSize) {
		setStartIndex(startIndex);
		setPageSize(pageSize);
	}

	public Pager(Integer startIndex, Integer pageSize, String sort, String dir) {
		setStartIndex(startIndex);
		setPageSize(pageSize);
		setSort(sort);
		setDir(dir);
	}

	public void calcPageCount(Long rowCount) {
		setRowCount(rowCount);
		if (pageSize != 0) {
			pageCount = rowCount.intValue() / pageSize;
			if (pageCount * pageSize < rowCount) {
				pageCount++;
			}
		}
		if (rowCount == 0) {
			pageth = 1;
		}
		calcPageths();
		logger.debug("开始索引：startIndex=" + startIndex);
		logger.debug("分页大小：pageSize=" + pageSize);
		logger.debug("排序字段：sort=" + sort);
		logger.debug("排序方向：dir=" + dir);
		logger.debug("当前页码：pageth=" + pageth);
		logger.debug("总页码数：pageCount=" + pageCount);
		logger.debug("总记录数：rowCount=" + rowCount);
		logger.debug("页码列表：pageths=" + ArrayUtils.toString(pageths));

	}

	private void calcPageths() {
        int half = DEFAULT_DISPLAY_PAGETHS_COUNT / 2;
        int left = half, right = half;
        if( this.pageth < half + 1 ){
            left = left - (half + 1 - this.pageth);
            right = right + (half + 1 - this.pageth);
        }
        if( this.pageCount - this.pageth < right){
            left = left + (right - (this.pageCount - this.pageth));
            right = this.pageCount - this.pageth;
        }

        this.pageths = new ArrayList<Integer>();
        this.pageths.add(1);
        for( int i = this.pageth - 1,count = 1; i > 1 && count < left; i --, count ++ ){
            this.pageths.add(i);
        }
        if(this.pageth.intValue() > 1 ){
            this.pageths.add(this.pageth);
        }

        for( int i = this.pageth + 1, count = 1; i > 0 && i < pageCount && count < right; i ++, count ++ ){
            this.pageths.add(i);
        }

        if( this.pageth.intValue() < this.pageCount.intValue()){
            this.pageths.add(this.pageCount);
        }

        Collections.sort(this.pageths);
	}
	
	public Integer getPageth() {return pageth;}
	public void setPageth(Integer pageth) {
		this.pageth = pageth;
		Integer ps = getPageSize();
		startIndex = (getPageth() - 1) * ps;
	}

	public Integer getPageCount() {return pageCount;}
	public void setPageCount(Integer pageCount) {this.pageCount = pageCount;}
	
	public Integer getPageSize() {return pageSize;}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		setPageth(getPageth());
	}

	public Long getRowCount() {return rowCount;}
	public void setRowCount(Long rowCount) {this.rowCount = rowCount;}

	public String getSort() {return sort;}
	public void setSort(String sort) {
		if("undefined".equals(sort)||"null".equals(sort)){
        	sort = null;
        }
		this.sort = sort;
	}

	public Integer getStartIndex() {return startIndex;}
	public void setStartIndex(Integer startIndex) {this.startIndex = startIndex;}

	public String getDir() {return dir;}
	public void setDir(String dir) {this.dir = dir;}

	public List<Integer> getPageths() {return this.pageths;}
	public void setPageths(List<Integer> pageths) {this.pageths = pageths;}

}
