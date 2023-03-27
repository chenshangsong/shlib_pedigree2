package  cn.sh.library.pedigree.framework.handler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 类名 : AopLogHandler <br>
 * 机能概要 : service层面向截面的拦截</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
@Aspect
public class AopLogHandler {
//	private static final Logger logger = Logger.getLogger(AopLogHandler.class);

	/*
	 * aPointcut定義
	 */
	@Pointcut("execution(* cn.com.fujitsu.gbcard.service..*.*(..))")
	public void aPointcut() {
	};

	/*
	 * After-Returning advice:
	 */
	@Around("aPointcut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		long startTime = System.currentTimeMillis();
		Object result;
		try {
			result = joinPoint.proceed();
		} finally {
//			printLog("doAround--service method ", joinPoint.getSignature().toLongString(),
//					"excute time: ", (System.currentTimeMillis() - startTime), " ms.");
		}
		return result;
	}

	/*
	 * Before advice:
	 */
	@Before("aPointcut()")
	public void doBefore(JoinPoint joinPoint) {
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		if(sra != null){
//			HttpServletRequest request = sra.getRequest();
//			@SuppressWarnings("unchecked")
//			Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute(FWConstants.SYS_SESSION);
//			DtoUser user = (DtoUser) map.get(FWConstants.LOGIN_USER);
//			String userId = user.getUserId();
//			
//			Object[] objs = joinPoint.getArgs();
//			for(Object obj :objs){
//				if(obj instanceof BaseDto){
//					BaseDto dto = (BaseDto) obj;
//					Map<String,Object> basemap = dto.getBaseMap(); 
//					map.put(FWConstants.LOGIN_USER, userId);
//					dto.setBaseMap(basemap);
//				}else if(obj instanceof List){
//					List<?> list = (List<?>) obj;
//					for(Object _obj :list){
//						if(_obj instanceof BaseDto){
//							BaseDto dto = (BaseDto) _obj;
//							Map<String,Object> basemap = dto.getBaseMap(); 
//							map.put(FWConstants.LOGIN_USER, userId);
//							dto.setBaseMap(basemap);
//						}
//					}
//				}
//			} 
//		}
//		
//		printLog("doBefore--service method:", joinPoint.getSignature().toLongString(),
//				"interceptor before the excute of method");
	}

	/*
	 * After-Returning advice:
	 */
	@AfterReturning(pointcut = "aPointcut()", returning = "obj")
	public void doAfterReturning(JoinPoint joinPoint, Object obj) {
//		printLog("doAfterReturning--service method:", joinPoint.getSignature()
//				.toLongString(), " interceptor after excute method,Object of return:", obj);
	}

	/*
	 * After advice:
	 */
	@After("aPointcut()")
	public void doAfter(JoinPoint jointPoint) {
//		printLog("doAfter service--interceptor after excute method :");
	}

	/*
	 * AfterThrowing advice:
	 */
	@AfterThrowing(pointcut = "aPointcut()",throwing="throwable")
	public void doAfterThrowing(JoinPoint jointPoint,Throwable throwable) {
//		logger.debug("doAfterThrowing--service exception while executing method :");
	}

//	private void printLog(Object... msgs) {
//		StringBuffer msg = new StringBuffer();
//		for (int i = 0; i < msgs.length; i++) {
//			msg.append(msgs[i]);
//		}
//		logger.debug(msg);
//	}
}
