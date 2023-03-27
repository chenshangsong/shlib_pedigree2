package cn.sh.library.pedigree.framework.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sh.library.pedigree.framework.controller.BaseController;

public class DtoJsonPageData extends BaseDto {
	BaseController bctrl;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8065750550923011045L;
	private String _search = ""; //
	private String nd = ""; //
	private int rows; // 每页行数
	private String sidx = ""; // 排序字段
	private String sord = "asc"; // 排序
	private int total; // 总页数
	private int page; // 当前页码
	private int records; // 总记录数
	private List<?> root;// JSONArray记录集合

	public DtoJsonPageData() {
		this.setTotal(0);
		this.setPage(0);
		this.setRecords(0);
		this.setRoot(new ArrayList<BaseDto>());
	}

	public DtoJsonPageData(BaseController bctrl) {
		this.setTotal(0);
		this.setPage(0);
		this.setRecords(0);
		this.setRoot(new ArrayList<BaseDto>());
		this.bctrl = bctrl;
	}

	public void setJsonPageData(DtoJsonPageData dtoCond, List<?> list) {
		// out = bctrl.getResponse().getWriter();
		if (dtoCond == null) {
			dtoCond = new DtoJsonPageData();
		}
		// 设置分页等参数
		if (list.size() == 0) {
			// 在查询条件变化的情况下，点击换页很可能没有数据
			this.setPage(1);
		} else {
			if (dtoCond.getPage() == 0) {
				dtoCond.setPage(1);
			}
			this.setPage(dtoCond.getPage());
		}
		this.setRows(dtoCond.getRows());
		this.setRecords(dtoCond.getRecords());
		this.setRoot(list);

	}

	public void print2JsonObj(List<?> list) {
		print2JsonObj(null, list);
	}

	public void print2JsonObj(DtoJsonPageData dtoCond, List<?> list) {
		try {
			setJsonPageData(dtoCond, list);
		} finally {
		}
	}

	public void print2JsonObj() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(bctrl.getResponse().getOutputStream(), this);
		} catch (IOException e) {
		} finally {
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.setTotal((int) Math.ceil(records * 1.0 / getRows()));
		this.records = records;
	}

	public List<?> getRoot() {
		return root;
	}

	public void setRoot(List<?> root) {
		this.root = root;
	}

	public String getNd() {
		return nd;
	}

	public void setNd(String nd) {
		this.nd = nd;
	}

	public String get_search() {
		return _search;
	}

	public void set_search(String _search) {
		this._search = _search;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public int getRows() {
		if (rows == 0) {
			return 20;
		}
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
}
