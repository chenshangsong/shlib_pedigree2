package cn.sh.library.pedigree.interceptor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 
 * @desc:特殊字符过滤
 */
public class StringFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		filterChain.doFilter(new HttpServletRequestWrapper(request) {

			@Override
			public String getParameter(String name) {
				// 返回值之前 先进行过滤
				String parm = super.getParameter(name);
				String parmLast = filterDangerString(parm);
				return parmLast;
			}

			@Override
			public String[] getParameterValues(String name) {
				// 返回值之前 先进行过滤
				String[] values = super.getParameterValues(name);
				if (values == null) {
					return null;
				}
				for (int i = 0; i < values.length; i++) {
					if(!StringUtilC.isEmpty(values[i])){
						values[i] = filterDangerString(values[i]);
					}
				}
				return values;
			}

			@Override
			public Map getParameterMap() {
				Map keys = super.getParameterMap();
				Set set = keys.entrySet();
				Iterator iters = set.iterator();
				while (iters.hasNext()) {
					Object key = iters.next();
					Object value = keys.get(key);
					keys.put(key, filterDangerString((String[]) value));
				}
				return keys;
			}

		/*	@Override
			public Object getAttribute(String name) { 
				Object object = super.getAttribute(name);
				if (object instanceof String) {
					System.out.println("4getAttribute-String");
					return filterDangerString((String) super.getAttribute(name));
				} else
					if (object instanceof Map) {
						System.out.println("4getAttribute-Map");
						return getParameterMap();
					}
				{
					System.out.println("4getAttribute-Last");
					return object;
				}
			}*/

		}, response);
	}

	public String filterDangerString(String value) {
		if (value == null) {
			return null;
		}
		value = value.replaceAll("\\{", "｛");
		// content = content.replaceAll("&", "&amp;");
		/*
		 * value = value.replaceAll("<", "&lt;"); value = value.replaceAll(">",
		 * "&gt;");
		 */
		value = value.replaceAll("\t", "    ");
		value = value.replaceAll("\r\n", "\n");
		value = value.replaceAll("\n", "<br/>");
		value = value.replaceAll("'", "&#39;");
		//value = value.replaceAll("\\\\", "&#92;");
	//	value = value.replaceAll("\"", "&quot;");
		value = value.replaceAll("\\}", "﹜").trim();
		value = value.replaceAll("\\|", "");
		value = value.replaceAll("'", "");
		value = value.replaceAll(">", "");
		value = value.replaceAll("<", "");
		//value = value.replaceAll("=", "");

		/*
		 * value = value.replaceAll(";", "");
		 * 
		 * value = value.replaceAll("@", "");
		 * 
		 * value = value.replaceAll("'", "");
		 * 
		 * value = value.replaceAll("\"", "");
		 * 
		 * value = value.replaceAll("<", "<");
		 * 
		 * value = value.replaceAll(">", ">");
		 * 
		 * value = value.replaceAll("\\(", "");
		 * 
		 * value = value.replaceAll("\\)", "");
		 * 
		 * value = value.replaceAll("\\+", "");
		 * 
		 * value = value.replaceAll("\r", "");
		 * 
		 * value = value.replaceAll("\n", "");
		 * 
		
		 * 
		 * 
		 * value = value.replaceAll("/", "");
		 */
		value = value.replaceAll("\"", "");
		 value = value.replaceAll("script", "");
		 value = value.replaceAll("onmouseover", "");
		 value = value.replaceAll("confirm", "");
		 // value = value.replaceAll("\\(", "");
		 // value = value.replaceAll("\\)", "");
		 value = value.replaceAll("\\+", "");
		return value;
	}

	public String[] filterDangerString(String[] value) {
		if (value == null) {
			return null;
		}
		for (int i = 0; i < value.length; i++) {
			String val = filterDangerString(value[i]);
			value[i] = val;
		}

		return value;
	}

}