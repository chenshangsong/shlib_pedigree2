package  cn.sh.library.pedigree.framework.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import cn.sh.library.pedigree.framework.commom.MsgId;
import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.util.CodeMsgUtil;
import cn.sh.library.pedigree.framework.util.ControllerUtil;

public class SystemExceptionResolver extends AbstractHandlerExceptionResolver {

	@Override
    protected ModelAndView doResolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
            Exception arg3)  {
		if (arg3 instanceof MaxUploadSizeExceededException) {
			if(arg2 instanceof BaseController){
				BaseController bctrl = (BaseController)arg2;
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
			}
		}
		return null;
    }
}