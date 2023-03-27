package cn.sh.library.pedigree.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sh.library.pedigree.services.VocabService;

/**
 * Created by Yi on 2014/11/19 0019.
 */
@Controller
@RequestMapping("/vocab")
public class VocabController {

    private Logger logger = Logger.getLogger(VocabController.class);

    @Resource
    private VocabService vocabService;

    @RequestMapping("/{term}")
    public void term(HttpServletRequest request, HttpServletResponse response, @PathVariable("term") String term) {
//        Enumeration<String> headers = request.getHeaderNames();
//        while( headers.hasMoreElements() ){
//            String header = headers.nextElement();
//            System.out.println(header + " ===> " + request.getHeader(header));
//        }
        term = "shl:" + term;
        String accept = request.getHeader("accept");
//        logger.info("accept: " + accept);
        if("application/rdf+xml".equalsIgnoreCase(accept)){
            try {
                String rdfData = this.vocabService.rdfData(term);
                response.setContentType("application/rdf+xml");
                response.getWriter().write(rdfData);
            } catch( Exception e ){
                logger.error(e);
            }
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/view/list#" + term);
            } catch( Exception e ){
                logger.error(e);
            }
        }
    }

}
