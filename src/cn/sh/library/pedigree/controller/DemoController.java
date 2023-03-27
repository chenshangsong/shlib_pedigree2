package cn.sh.library.pedigree.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * Created by ly on 2014-12-29.
 */
@Controller
@RequestMapping("/service")
public class DemoController {
    @RequestMapping
    public String index(){
        return "service/index";
    }

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String list(){
        return "service/map";
    }
}