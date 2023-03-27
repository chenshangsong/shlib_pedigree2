package  cn.sh.library.pedigree.framework.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.sh.library.pedigree.framework.commom.FWConstant;
import cn.sh.library.pedigree.framework.commom.MsgId;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.exception.ExclusiveException;
import cn.sh.library.pedigree.framework.exception.NoDataException;
import cn.sh.library.pedigree.framework.exception.SingleLoginException;
import cn.sh.library.pedigree.framework.exception.SysException;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.ControllerUtil;
import cn.sh.library.pedigree.framework.util.StringUtil;
import cn.sh.library.pedigree.utils.DateUtilC;

/**
 * 类名 : ExceptionHandler <br>
 * 机能概要 : 系统异常处理</br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class ExceptionHandler implements HandlerExceptionResolver {
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception ex) {
		logger.error("操作错误："+DateUtilC.getNowTime()+":"+request.getRequestURI()+"--"+request.getMethod()+"---"+ex.toString());
		if(arg2 instanceof BaseController){
			BaseController bctrl = (BaseController)arg2;
			TransactionStatus trstatus = bctrl.getDbTransaction().getStatus();
			if(trstatus!= null && trstatus.isNewTransaction()){
				bctrl.getDbTransaction().rollback();
			}
			if(ex instanceof MyBatisSystemException ){
				logger.error(StringUtil.getExceptionString(ex));
				if(((MyBatisSystemException) ex).contains(ExclusiveException.class)){
					if (ControllerUtil.isReturnModelAndView(bctrl)) {
						ModelAndView mave = new ModelAndView();
						mave.addObject("errCode", MsgId.E0008);
						mave.addObject("errMsg", CodeMsgUtil.getMessage(MsgId.E0008));
						mave.setViewName("/common/exception");
						return mave;
					} else {
						ControllerUtil.printException(bctrl, new ExclusiveException(MsgId.E0008));
						return new ModelAndView();  
					}
				}else{
					if (ControllerUtil.isReturnModelAndView(bctrl)) {
						ModelAndView mave = new ModelAndView();
						mave.addObject("errCode", MsgId.E9000);
						mave.addObject("errMsg", CodeMsgUtil.getMessage(MsgId.E9000));
						mave.setViewName("/common/exception");
						return mave;
					} else {
						ControllerUtil.printException(bctrl, new ExclusiveException(MsgId.E9000));
						return new ModelAndView();  
					}
				}
			}else if(ex instanceof SingleLoginException){
				SingleLoginException slEx = (SingleLoginException)ex;
				request.getSession().setAttribute(FWConstant.SYS_SESSION, null);
				if (ControllerUtil.isReturnModelAndView(bctrl)) {
					ModelAndView mave = new ModelAndView();
					mave.addObject("errCode",slEx.getErrDto().getValue());
					mave.addObject("errMsg",slEx.getErrDto().getLabel());
					mave.setViewName("/common/singleLogin");
					return mave;
				} else {
					ControllerUtil.printException(bctrl,
							(SingleLoginException)ex);
					return new ModelAndView();
				}
			}else if (ex instanceof BadSqlGrammarException) {
				logger.error(StringUtil.getExceptionString(ex));
				
				SysException sysEx = new SysException(MsgId.E9000);
				if(ControllerUtil.isReturnModelAndView(bctrl)){
					ModelAndView mave= new ModelAndView();
					mave.addObject("errCode",sysEx.getErrDto().getValue());
					mave.addObject("errMsg",sysEx.getErrDto().getLabel());
					mave.setViewName("/common/exception");
					return mave;
				}else{
					ControllerUtil.printException(bctrl, sysEx);
					return new ModelAndView(); 
				}
			}else if (ex instanceof SysException) {
				logger.error(StringUtil.getExceptionString(ex));
				
				SysException sysEx = (SysException)ex;
				if(ControllerUtil.isReturnModelAndView(bctrl)){
					ModelAndView mave= new ModelAndView();
					mave.addObject("errCode",sysEx.getErrDto().getValue());
					mave.addObject("errMsg",sysEx.getErrDto().getLabel());
					mave.setViewName("/common/exception");
					return mave;
				}else{
					ControllerUtil.printException(bctrl, (SysException)ex);
					return new ModelAndView(); 
				}
			}else if (ex instanceof NoDataException) {
				NoDataException ndEx = (NoDataException)ex;
				if(ControllerUtil.isReturnModelAndView(bctrl)){
					ModelAndView mave= new ModelAndView();
					if(ndEx.getErrDto() != null){
						mave.addObject("value",ndEx.getErrDto().getValue());
						mave.addObject("label",ndEx.getErrDto().getLabel());
					}else{
						mave.addObject("value",MsgId.E0031);
						mave.addObject("label",CodeMsgUtil.getMessage(MsgId.E0031));
					}
					mave.setViewName("/common/nodata");
					return mave;
				}else{
					if(ndEx.getErrDto() != null){
						ControllerUtil.printDto(bctrl, ndEx.getErrDto());
					}else{
						ControllerUtil.printMessageDto(bctrl, MsgId.E0031);
					}
					return new ModelAndView(); 
				}
			}else if (ex instanceof MaxUploadSizeExceededException) {
				if(ControllerUtil.isReturnModelAndView(bctrl)){
					ModelAndView mave= new ModelAndView();
					mave.addObject("value",MsgId.E0022);
					mave.addObject("label",CodeMsgUtil.getMessage(MsgId.E0022, "5M"));
					mave.setViewName("/common/exception");
					return mave;
				}else{
					ControllerUtil.printMessageDto(bctrl, MsgId.E0022, "5M");
					return new ModelAndView(); 
				}
			} else{
				logger.error(StringUtil.getExceptionString(ex));
								
				if(ControllerUtil.isReturnModelAndView(bctrl)){
					ModelAndView mave= new ModelAndView();
					mave.addObject("errCode",MsgId.E9000);
					mave.addObject("errMsg",CodeMsgUtil.getMessage(MsgId.E9000));
					mave.setViewName("/common/exception");
					return mave;
				}else{
					ControllerUtil.printException(bctrl, new SysException(MsgId.E9000));
					return new ModelAndView(); 
				}
			}
			
		}
		
		return new ModelAndView("redirect:"+request.getContextPath());
	}

}
