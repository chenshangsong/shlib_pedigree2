package cn.sh.library.pedigree.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.dto.ClassView;
import cn.sh.library.pedigree.dto.Vocab;
import cn.sh.library.pedigree.services.VocabService;

/**
 * Created by Yi on 2014/10/27 0027.
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    private Logger logger = Logger.getLogger(ViewController.class);

    @Resource
    private VocabService vocabService;

    @RequestMapping
    public String home(Model model){
        return "/home";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        return "vocab/list";
    }

    @RequestMapping(value = "/list{type}", method = RequestMethod.POST)
    public String listVocab(Model model, @PathVariable("type") String type){
        List<Vocab> vocabList = null;
        try {
        	 if( "Classes".equals(type)) {
             	
                 vocabList = this.vocabService.listClasses();
             } else if("Properties".equals(type)){
                 vocabList = this.vocabService.listProperties();
             } else {
                 throw new IllegalArgumentException();
             }
             String rdfsLink = this.vocabService.getLink("rdfs:t");
             if(StringUtils.isNotEmpty(rdfsLink)) {
                 model.addAttribute("rdfsLink", rdfsLink.substring(0, rdfsLink.length() - 1));
             }
             int size = vocabList.size();
             int pageSize = size / 4 + (size % 4 > 0 ? 1 : 0);
             model.addAttribute("type", type);
             model.addAttribute("vocabList", vocabList);
             model.addAttribute("vocabList1", vocabList.subList(0, pageSize));
             model.addAttribute("vocabList2", vocabList.subList(pageSize, pageSize * 2));
             model.addAttribute("vocabList3", vocabList.subList(pageSize * 2, pageSize * 3));
             model.addAttribute("vocabList4", vocabList.subList(pageSize * 3, size));
		} catch (Exception e) {
			System.out.println(e);
		}
       
        return "vocab/list_vocab_frag";
       // return "vocab/list";
    }

    @ResponseBody
    @RequestMapping("/comment")
    public String comment(String className){
        return this.vocabService.getComment(className);
    }

    @RequestMapping("/shlgen.rdf")
    public void rdf(HttpServletResponse response){
        String rdfData = this.vocabService.rdfData();
        response.setContentType("application/rdf+xml");
        try {
            response.getWriter().write(rdfData);
        } catch( Exception e){
            logger.error(e);
        }
    }

    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public String classView(Model model){
        return "vocab/class";
    }

    @ResponseBody
    @RequestMapping(value = "/classes")
    public List<ClassView> classes(){
        List<ClassView> resultList = this.vocabService.listClassViews(null);
        return resultList;
    }

    @RequestMapping("/properties")
    public String properties(Model model, String className){
        Map<String, Map<String, String>> properties = this.vocabService.listClassProperties(className);
        if( properties == null ){
            properties = new HashMap<>();
        }
        if(MapUtils.isEmpty(properties)){
            if( !className.startsWith("shlgen:")) {
                model.addAttribute("link", this.vocabService.getLink(className));
            }
        }
        model.addAttribute("properties", properties);
        return "vocab/class_properties_frag";
    }

    @RequestMapping(value = "/model", method = RequestMethod.GET)
    public String model(Model model){
        List<String> classList = this.vocabService.listModelClasses();
        model.addAttribute("classList", classList);
        return "vocab/model";
    }


    @ResponseBody
    @RequestMapping(value = "/modelGraph", method = RequestMethod.POST)
    public String modelGraph(Model model, String className, String uri){
        String modelGraph = this.vocabService.modelGraph(className, uri);
        return modelGraph;
    }

}
