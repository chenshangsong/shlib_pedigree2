package  cn.sh.library.pedigree.framework.taglib;


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 类名 : NumberTag <br>
 * 机能概要 : </br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class NumberTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private String id;				
	private Integer integerLen;				//最大整数位
	private Integer decimalLen;				//小数位数
	private String name;			
	private String cssClass;		
	private String style;			
	private String value;			
	private Integer tabindex;			
	private String readonly;				
	private String groupSeparator;			
	private String disabled;		
	private Integer size;  				
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		
		String strWrite = "<input type=\"text\" id=\"" + id +"\" ";
		if(integerLen != null)
			strWrite += "integerLen=\"" + integerLen + "\" ";
		if(decimalLen != null)
			strWrite += "decimalLen=\"" + decimalLen + "\" ";
		if(name != null && name.length() != 0)
			strWrite += "name=\"" + name + "\" ";
		if(size != null)
			strWrite += "size=\"" + size + "\" ";
		
		if(cssClass != null && cssClass.length() != 0)
			strWrite += "class=\"_amount " + cssClass + "\" ";
		else
			strWrite += "class=\"_amount\" ";
		
		if(style != null && style.length() != 0)
			strWrite += "style=\"" + style + "\" ";
		if(value != null && value.length() != 0)
			strWrite += "value=\"" + value + "\" ";
		if(tabindex != null)
			strWrite += "tabindex=\"" + tabindex + "\" ";
		if(readonly != null && readonly.length() != 0)
			strWrite += "readonly=\"" + readonly + "\" ";
		if(groupSeparator != null && groupSeparator.length() != 0)
			strWrite += "groupSeparator=\"" + groupSeparator + "\" ";
		if(disabled != null && disabled.length() != 0)
			strWrite += "disabled=\"" + disabled + "\" ";
		
		strWrite += "/> ";
		
		try {
			out.write(strWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE; 
	}


	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE; 
	}
	
	public Integer getIntegerLen() {
		return integerLen;
	}

	public void setIntegerLen(Integer integerLen) {
		this.integerLen = integerLen;
	}

	public Integer getDecimalLen() {
		return decimalLen;
	}

	public void setDecimalLen(Integer decimalLen) {
		this.decimalLen = decimalLen;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getTabindex() {
		return tabindex;
	}

	public void setTabindex(Integer tabindex) {
		this.tabindex = tabindex;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}


	public String getGroupSeparator() {
		return groupSeparator;
	}


	public void setGroupSeparator(String groupSeparator) {
		this.groupSeparator = groupSeparator;
	}


	public Integer getSize() {
		return size;
	}


	public void setSize(Integer size) {
		this.size = size;
	}

}
