package  cn.sh.library.pedigree.framework.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 数据对象
 * @author chenshangsong
 * 
 * 11:01:10
 * BaseDto.java

 * lanMei
 */
public class BaseDto implements Serializable ,Comparable<Object>{

	/**
	 * 创建日期
	 */
	private String created_on = "";
	/**
	 * 创建人
	 */
	private String created_name = "";
	/**
	 * 创建人
	 */
	private String created_by = "";
	/**
	 * 修改时间
	 */
	private String updated_on = "";
	/**
	 * 修改人
	 */
	private String updated_name = "";
	/**
	 * 修改人
	 */
	private String updated_by = "";
	
	private Integer id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -559760894251414100L;
	
	@JsonIgnore
	protected BaseDto getSelf(String dtoStr){
		try {
			return new ObjectMapper().readValue(dtoStr, this.getClass());
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public int compareTo(Object o) {
	    return 0;
	}
	
	@JsonIgnore
	public String[] getListString(){
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getCreated_name() {
		return created_name;
	}

	public void setCreated_name(String created_name) {
		this.created_name = created_name;
	}

	public String getUpdated_name() {
		return updated_name;
	}

	public void setUpdated_name(String updated_name) {
		this.updated_name = updated_name;
	}

}
