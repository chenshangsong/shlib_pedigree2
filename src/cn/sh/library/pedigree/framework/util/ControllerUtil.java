package cn.sh.library.pedigree.framework.util;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
//import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sh.library.pedigree.framework.baseDto.BaseDtoValidator;
import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.controller.ModelAndViewExt;
import cn.sh.library.pedigree.framework.exception.SysException;
import cn.sh.library.pedigree.framework.interceptor.WebContainer;
import cn.sh.library.pedigree.framework.model.BaseDto;
import cn.sh.library.pedigree.framework.model.DtoLabelValue;
import cn.sh.library.pedigree.framework.model.DtoMessage;
import cn.sh.library.pedigree.framework.model.DtoUser;

/**
 * 类名 : ControllerUtil <br>
 * 机能概要 : Controller共通操作工具</br> 版权所有: Copyright © 2011 TES Corporation, All
 * Rights Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class ControllerUtil {
	public static DtoUser getDtoUser() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (sra != null) {
			HttpServletRequest request = sra.getRequest();
			WebContainer map = (WebContainer) request.getSession()
					.getAttribute(FWConstant.SYS_SESSION);
			return (DtoUser) map.getContainerMap().get(FWConstant.LOGIN_USER);
		}
		return new DtoUser();
	}

	public static void printEmpty(BaseController baseCtrl) {
		baseCtrl.getResponse().setContentType("text/html;charset=UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(baseCtrl.getResponse().getOutputStream(), "");
		} catch (Exception e) {
		}
	}

	public static void printString(BaseController baseCtrl, String... param) {
		List<DtoLabelValue> list = new ArrayList<DtoLabelValue>();
		for (String str : param) {
			list.add(new DtoLabelValue(str, ""));
		}
		printList(baseCtrl, list);
	}

	public static void printException(BaseController baseCtrl, SysException ex) {
		List<BaseDto> list = new ArrayList<BaseDto>();
		list.add(ex.getErrDto());
		printList(baseCtrl, list);
	}

	public static void printMessageDto(BaseController baseCtrl, String msg,
			Object... param) {
		List<BaseDto> list = new ArrayList<BaseDto>();
		DtoLabelValue dto = new DtoLabelValue(msg, param);
		list.add(dto);
		printList(baseCtrl, list);
	}

	public static void printDto(BaseController baseCtrl, BaseDto dto) {
		List<BaseDto> list = new ArrayList<BaseDto>();
		list.add(dto);
		printList(baseCtrl, list);
	}

	public static void printObject(BaseController baseCtrl, Object dto) {
		baseCtrl.getResponse().setContentType("text/html;charset=UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(baseCtrl.getResponse().getOutputStream(), dto);
		} catch (Exception e) {
		}
	}

	public static void printList(BaseController baseCtrl, List<?> list) {
		baseCtrl.getResponse().setContentType("text/html;charset=UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(baseCtrl.getResponse().getOutputStream(), list);
		} catch (Exception e) {
		}
	}

	/**
	 * 请求参数check
	 * 
	 * @param baseCtrl
	 *            所属Controller
	 * @param dto
	 *            请求参数对象
	 * @param validator
	 *            check类实例
	 * @return check是否通过
	 * @throws Exception
	 */
	public static boolean checkFormDto(BaseController baseCtrl, BaseDto dto,
			Object validator) throws Exception {
		baseCtrl.getResponse().setContentType("text/html;charset=UTF-8");
		BaseDtoValidator bvalidator = (BaseDtoValidator) validator;
		bvalidator.setController(baseCtrl);
		bvalidator.validate(dto);
		ArrayList<DtoMessage> list = bvalidator.getListMsg();
		try {
			// 校验没有通过
			if (list.size() > 0) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(baseCtrl.getResponse().getOutputStream(),
						list);
			}
		} catch (Exception e) {

		} finally {

		}
		return list.size() > 0;
	}

	public static String getRequestPath(BaseController bctrl) {
		String path = "";
		try {
			MethodNameResolver mnresolver = new InternalPathMethodNameResolver();
			path = "./.." + ControllerUtil.getRequestMapping(bctrl);
			path = path + mnresolver.getHandlerMethodName(bctrl.getRequest());
			path = path + FWConstant.ACTION_SUFFIX;
			if (bctrl.getRequest().getQueryString() != null) {
				path = path + "?" + bctrl.getRequest().getQueryString();
			}
		} catch (NoSuchRequestHandlingMethodException e) {
		}
		return path;
	}

	public static String getRequestMapping(BaseController bctrl) {
		Annotation[] ass = bctrl.getClass().getAnnotations();
		if (ass != null) {
			for (Annotation as : ass) {
				if (as.annotationType().getSimpleName()
						.equals(RequestMapping.class.getSimpleName())) {
					RequestMapping rm = (RequestMapping) as;
					String[] aa = rm.value();
					if (aa != null && aa.length > 0) {
						return aa[0];
					}
				}
			}
		}
		return "";
	}

	public static Annotation[] getAnnotation(BaseController bctrl) {
		Method method = getRequestMethod(bctrl);
		if (method != null) {
			return method.getAnnotations();
		} else {
			return new Annotation[0];
		}
	}

	public static boolean hasAnnotation(BaseController bctrl, Class<?> cls) {
		for (Annotation as : getAnnotation(bctrl)) {
			if (as.annotationType().getSimpleName().equals(cls.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	private static Method getRequestMethod(BaseController bctrl) {
		try {
			MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
			HttpServletRequest request = bctrl.getRequest();
			String reqPath = methodNameResolver.getHandlerMethodName(request);
			Method[] methods = bctrl.getClass().getDeclaredMethods();
			for (Method method : methods) {
				Annotation[] ass = method.getAnnotations();
				if (ass != null) {
					for (Annotation as : ass) {
						if (as.annotationType().getSimpleName()
								.equals(RequestMapping.class.getSimpleName())) {
							RequestMapping rm = (RequestMapping) as;
							String[] aa = rm.value();
							if (aa != null && aa.length > 0) {
								if (aa[0].substring(1).equals(reqPath)) {
									return method;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static boolean isReturnModelAndView(BaseController bctrl) {
		Method method = getRequestMethod(bctrl);
		if (method != null) {
			return method.getGenericReturnType().equals(ModelAndViewExt.class)
					|| method.getGenericReturnType().equals(ModelAndView.class);
		}
		return false;
	}

}