package cn.sh.library.pedigree.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.dto.Pager;
import cn.sh.library.pedigree.services.DynastyService;

/**
 * @author liuyi
 * @date 2014/12/27 0027
 */
@Controller
@RequestMapping("/service/dynasty")
public class DynastyController {

    @Resource
    private DynastyService dynastyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){ return "dynasty/list"; }

    @ResponseBody
    @RequestMapping(value="/dynasties", method = RequestMethod.GET)
    public List<Map<String, String>> list(Model model){
        return this.dynastyService.list();
    }

    @ResponseBody
    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public Map<String, Object> listPersons(String uri, Pager pager){
        Map<String, Object> result = new HashMap<>();
        result.put("persons", this.dynastyService.listPersons(uri, pager));
        result.put("pager", pager);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/works", method = RequestMethod.GET)
    public  Map<String, Object> listWorks(String uri, Pager pager){
        Map<String, Object> result = new HashMap<>();
        result.put("works", this.dynastyService.listWorks(uri, pager));
        result.put("pager", pager);
        return result;
    }

}
