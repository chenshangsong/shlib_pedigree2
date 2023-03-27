package  cn.sh.library.pedigree.framework.handler;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 类名 : WebBindHandler <br>
 * 机能概要 : </br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class WebBindHandler implements WebBindingInitializer {
	public void initBinder(WebDataBinder binder, WebRequest request) {
//		// 1. 使用spring自带的CustomDateEditor
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(
//				dateFormat, true));
//
//		// 2. 自定义的PropertyEditorSupport
//		// binder.registerCustomEditor(Date.class, new DateConvertEditor());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  //可以設定任意的日期格式  
//        dateFormat.setLenient(false);  
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));  
        

	}
}
