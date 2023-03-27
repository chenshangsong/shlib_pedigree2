package  cn.sh.library.pedigree.framework.model;

import java.util.Map;

import cn.sh.library.pedigree.framework.util.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.StringUtil;


/**
 * クラス名 : DtoLabelValue <br>
 * 機能概要 : </br> 
 * コピーライト: Copyright © 2011 NC Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class DtoLabelValue  extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 634180713847694123L;
	
	private String label;
	private String value;
	
	public DtoLabelValue(){
	}
	
	public DtoLabelValue(String msgId, Object... param){
		this.label = CodeMsgUtil.getMessage(msgId, param);
		this.value = msgId;
	}
	
	public DtoLabelValue(Object label,Object value){
		this.label = StringUtil.getString(label);
		this.value = StringUtil.getString(value);
	}
	
	public DtoLabelValue(Map<String, Object> map,String labelKey,String valueKey){
		if(map == null){
			this.label = "";
			this.value = "";
		}else{
			this.label = StringUtil.getString(map.get(labelKey));
			this.value = StringUtil.getString(map.get(valueKey));
		}
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public int compareTo(Object o) {
		DtoLabelValue mst=(DtoLabelValue)o; 
	    return (value).compareTo(mst.value);
	}


}
